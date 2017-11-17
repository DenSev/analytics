package com.densev.elastic.repository;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dzianis_Sevastseyenk on 10/20/2017.
 */
public class DistributedClient {

    private static final Logger LOG = LoggerFactory.getLogger(DistributedClient.class);

    List<HttpClient> clients;

    private final JsonFactory jsonFactory = new JsonFactory();


    public DistributedClient(String clusterUrl, String clusterPort) throws IOException {
        HttpClient masterClient = HttpClientBuilder.create().build();

        HttpGet nodes = new HttpGet("http://" + clusterUrl + ":" + clusterPort + "/_nodes/http");


        HttpResponse response = masterClient.execute(nodes);
        HttpEntity entity = response.getEntity();

        List<HttpHost> hosts = readHosts(entity);
        System.out.println(hosts);

    }


    private List<HttpHost> readHosts(HttpEntity entity) throws IOException {
        try (InputStream inputStream = entity.getContent()) {
            JsonParser parser = jsonFactory.createParser(inputStream);
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                throw new IOException("expected data to start with an object");
            }
            List<HttpHost> hosts = new ArrayList<>();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                    if ("nodes".equals(parser.getCurrentName())) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            JsonToken token = parser.nextToken();
                            assert token == JsonToken.START_OBJECT;
                            String nodeId = parser.getCurrentName();
                            HttpHost sniffedHost = readHost(nodeId, parser, Scheme.HTTP);
                            if (sniffedHost != null) {
                                LOG.trace("adding node [{}]", nodeId);
                                hosts.add(sniffedHost);
                            }
                        }
                    } else {
                        parser.skipChildren();
                    }
                }
            }
            return hosts;
        }
    }

    private static HttpHost readHost(String nodeId, JsonParser parser, Scheme scheme) throws IOException {
        HttpHost httpHost = null;
        String fieldName = null;
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (parser.getCurrentToken() == JsonToken.FIELD_NAME) {
                fieldName = parser.getCurrentName();
            } else if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                if ("http".equals(fieldName)) {
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        if (parser.getCurrentToken() == JsonToken.VALUE_STRING && "publish_address".equals(parser.getCurrentName())) {
                            URI boundAddressAsURI = URI.create(scheme + "://" + parser.getValueAsString());
                            httpHost = new HttpHost(boundAddressAsURI.getHost(), boundAddressAsURI.getPort(),
                                boundAddressAsURI.getScheme());
                        } else if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                            parser.skipChildren();
                        }
                    }
                } else {
                    parser.skipChildren();
                }
            }
        }
        //http section is not present if http is not enabled on the node, ignore such nodes
        if (httpHost == null) {
            LOG.debug("skipping node [{}] with http disabled", nodeId);
            return null;
        }
        return httpHost;
    }

    public enum Scheme {
        HTTP("http"), HTTPS("https");

        private final String name;

        Scheme(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
