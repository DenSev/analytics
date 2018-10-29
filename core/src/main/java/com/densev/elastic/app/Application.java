package com.densev.elastic.app;

import com.densev.elastic.repository.ElasticSearchRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 10/17/2017.
 */
@Component
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ElasticSearchRepository repository;

    public void run() throws IOException {

        Set<String> ids = new HashSet<>();
        Set<String> nonUniqueIds = new HashSet<>();

        SearchSourceBuilder searchRequest = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        SearchResponse response = repository.search(searchRequest, "search-index-alias");
        do {
            for (SearchHit hit : response.getHits().getHits()) {
                if (!ids.add(hit.getId())) {
                    nonUniqueIds.add(hit.getId());
                }
            }
            response = repository.scroll(response.getScrollId());
        } while (response.getHits().getHits().length != 0);

        System.out.println(nonUniqueIds);
    }


    public static void main(String... args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        Application application = context.getBean(Application.class);
        application.run();
    }
}
