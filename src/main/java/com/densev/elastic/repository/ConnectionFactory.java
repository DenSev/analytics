package com.densev.elastic.repository;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode.Role;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created on 20.01.2017.
 */
@Repository
public class ConnectionFactory implements ClientProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);
    private static final int NO_NODE_EXCEPTION_THRESHOLD = 5;
    private static final int LAST_NO_NODE_EXCEPTION_MINUTES_THRESHOLD = 5;

    private TransportClient elasticClient;
    private final String esClusterName = "e-san";
    private final String esAddress = "127.0.0.1";
    private final int esTransportPort = 9300;
    private final String esTimeout = "200s";
    private final boolean isSniffingEnabled = true;
    private final boolean connectToCoordinator = true;


    private DateTime lastNoNodeAvailableException;
    private int noNodeAvailableExceptionCount = 0;
    private final ReentrantLock elasticClientInitLock = new ReentrantLock();

    @PostConstruct
    private void init() {
        try {
            final Settings settings = Settings.builder()
                .put("cluster.name", esClusterName)
                .put("transport.tcp.connect_timeout", esTimeout)
                .put("client.transport.sniff", isSniffingEnabled)
                .build();


            TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esAddress), esTransportPort));

            NodesInfoResponse nodesInfo = client.admin().cluster().nodesInfo(new NodesInfoRequest()).actionGet();
            List<NodeInfo> nodes = nodesInfo.getNodes();
            client.close();

            List<TransportAddress> addresses = new ArrayList<TransportAddress>();
            if (connectToCoordinator) {
                for (NodeInfo nodeInfo : nodes) {
                    // adding only coordinating nodes, nodes with the empty role list are interpreted as
                    // "coordinating" ones.
                    if (nodeInfo.getNode().getRoles().isEmpty()) {
                        addresses.add(nodeInfo.getNode().getAddress());
                    }
                }
            }

            if (addresses.isEmpty() && !connectToCoordinator) {
                for (NodeInfo nodeInfo : nodes) {
                    // adding adding only master nodes
                    if (nodeInfo.getNode().getRoles().contains(Role.MASTER)) {
                        addresses.add(nodeInfo.getNode().getAddress());
                    }
                }
                LOG.info("");
            }

            elasticClient = new PreBuiltTransportClient(settings);
            elasticClient.addTransportAddresses(addresses.toArray(new TransportAddress[addresses.size()]));
        } catch (UnknownHostException e) {
            LOG.error("Failed to connect to elasticsearch with base url {}, and elastic cluster {}", esAddress, esClusterName);
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void tearDown() {
        if (this.elasticClient != null) {
            this.elasticClient.close();
        }
    }

    @Override
    public void reinit(NoNodeAvailableException nnae) {
        try {
            if (!elasticClientInitLock.isLocked() && elasticClientInitLock.tryLock()) {
                // Log message for appropriate debugging.
                LOG.warn("ReInit ES connection attempt {} {}", noNodeAvailableExceptionCount, ExceptionUtils.getStackTrace(nnae));

                //reset noNodeAvailableException count to zero if past time threshold
                if (lastNoNodeAvailableException != null
                    && lastNoNodeAvailableException
                    .plusMinutes(LAST_NO_NODE_EXCEPTION_MINUTES_THRESHOLD)
                    .isBeforeNow()) {
                    noNodeAvailableExceptionCount = 0;
                }

                if ((lastNoNodeAvailableException == null ||
                    // if last no node exception was earlier than threshold
                    lastNoNodeAvailableException.plusMinutes(LAST_NO_NODE_EXCEPTION_MINUTES_THRESHOLD).isBeforeNow())
                    // and if NoNodeAvailableException count is less than threshold
                    && noNodeAvailableExceptionCount++ < NO_NODE_EXCEPTION_THRESHOLD) {
                    init();
                    lastNoNodeAvailableException = DateTime.now();
                    LOG.error("Reinitializing ES connection, due to exception {}", nnae);
                    return;
                } else {
                    LOG.warn(
                        "The ReInit attempt {} was executed less then {} minutes ago or the " +
                            "number of permitted attempts was exceeded for the threshold interval.",
                        noNodeAvailableExceptionCount,
                        LAST_NO_NODE_EXCEPTION_MINUTES_THRESHOLD
                    );
                }

                throw nnae;
            } else {
                LOG.info("Waiting untill ReInit ES connection attempt # {} will be finished.", noNodeAvailableExceptionCount);
                // if the lock is not available then the current thread will wait till the reinit completed.
                elasticClientInitLock.lock();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (elasticClientInitLock.isHeldByCurrentThread()) {
                elasticClientInitLock.unlock();
            }
        }
    }

    @Override
    public Client getClient() {
        return this.elasticClient;
    }

}
