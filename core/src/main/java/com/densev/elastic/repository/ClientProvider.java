package com.densev.elastic.repository;

import org.elasticsearch.client.RestHighLevelClient;

/**
 * Created on 06/30/2017.
 */
public interface ClientProvider {

    RestHighLevelClient getClient();

}
