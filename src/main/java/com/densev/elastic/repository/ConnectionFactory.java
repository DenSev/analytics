package com.densev.elastic.repository;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created on 20.01.2017.
 */
@Repository
public class ConnectionFactory {
    Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    private Client elasticClient;
    private final String esClusterName = "e-san";
    private final String esAddress = "127.0.0.1";
    private final int esTransportPort = 9300;
    private final String esTimeout = "200s";

    @PostConstruct
    public void init() {
        try {
            final Settings settings = Settings.builder()
                    .put("cluster.name", esClusterName)
                    .put("transport.tcp.connect_timeout", esTimeout)
                    .put("client.transport.sniff", true)
                    .build();
            elasticClient = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esAddress), esTransportPort));
        } catch (UnknownHostException e) {
            logger.error("Failed to connect to elasticsearch with base url {}, and elastic cluster '%s'", esAddress, esClusterName);
            throw new RuntimeException(e);
        }
    }

    public Client getElasticClient(){
        return this.elasticClient;
    }

}
