package com.densev.elastic.repository;

import com.densev.elastic.logging.annotations.LogAll;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
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

    @LogAll
    public SearchResponse search(SearchSourceBuilder searchSourceBuilder, String index) throws IOException {
        SearchRequest searchRequest = new SearchRequest(new String[]{index}, searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(10L));
        SearchResponse searchResponse = client.search(searchRequest);
        return searchResponse;
    }

    ListenableActionFuture<SearchResponse> searchAsync(SearchSourceBuilder searchSourceBuilder, String index) {
        throw new UnsupportedOperationException();
    }

    public SearchResponse scroll(String scrollId) throws IOException {
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(new Scroll(TimeValue.timeValueMinutes(10L)));
        return client.searchScroll(scrollRequest);
    }

}
