package com.densev.elastic.repository;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created on 20.01.2017.
 */
@Repository
public class ConnectionFactory implements ClientProvider {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionFactory.class);

    private final String esAddress = "es-inventory-2.qa-21.vip.aws2";
    private final int esTransportPort = 80;
    private RestClient basicClient;
    private RestHighLevelClient client;
    private Sniffer sniffer;

    @PostConstruct
    private void init() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(esAddress, esTransportPort, "http"));
        this.basicClient = builder.build();
        this.sniffer = Sniffer.builder(basicClient).build();
        this.client = new RestHighLevelClient(this.basicClient);
    }

    @PreDestroy
    public void tearDown() throws IOException {
        if (this.basicClient != null) {
            this.basicClient.close();
        }
        if (this.sniffer != null) {
            this.sniffer.close();
        }
    }

    @Override
    public RestHighLevelClient getClient() {
        return this.client;
    }
}
