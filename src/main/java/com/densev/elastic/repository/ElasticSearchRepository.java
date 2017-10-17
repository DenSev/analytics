package com.densev.elastic.repository;

import com.densev.elastic.logging.annotations.LogExecutionTime;
import com.densev.elastic.logging.annotations.LogParams;
import com.densev.elastic.logging.annotations.LogResult;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by Dzianis_Sevastseyenk on 10/17/2017.
 */
@Repository
public class ElasticSearchRepository {

    @Autowired
    private RestHighLevelClient client;

    @LogParams
    @LogResult
    @LogExecutionTime
    public SearchResponse search(SearchSourceBuilder searchSourceBuilder, String index) throws IOException {
        SearchResponse searchResponse = client.search(new SearchRequest(new String[]{index}, searchSourceBuilder));
        return searchResponse;
    }

    ListenableActionFuture<SearchResponse> searchAsync(SearchSourceBuilder searchSourceBuilder, String index) {
        throw new UnsupportedOperationException();
    }

}
