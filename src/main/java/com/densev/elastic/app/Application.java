package com.densev.elastic.app;

import com.densev.elastic.repository.ElasticSearchRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created on 10/17/2017.
 */
@Component
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ElasticSearchRepository repository;

    public void run() throws IOException {

        SearchSourceBuilder searchRequest = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        SearchResponse response = repository.search(searchRequest, "my_index2");

        LOG.debug(response.toString());
    }


    public static void main(String... args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        Application application = context.getBean(Application.class);
        application.run();
    }
}
