package com.densev.elastic.app;

import com.densev.elastic.repository.ConnectionFactory;
import com.densev.elastic.utility.JsonFactoryProvider;
import com.densev.elastic.utility.MapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.json.JsonBuilderFactory;

/**
 * Created on 02.15.2018.
 */
@Configuration
@ComponentScan("com.densev.elastic")
@EnableAspectJAutoProxy
public class ApplicationConfig {

    @Bean
    public ObjectMapper getMapper(@Autowired MapperProvider mapperProvider) {
        return mapperProvider.getMapper();
    }

    @Bean
    public RestHighLevelClient getClient(@Autowired ConnectionFactory connectionFactory) {
        return connectionFactory.getClient();
    }

    @Bean
    public JsonBuilderFactory getJsonFactory(@Autowired JsonFactoryProvider jsonFactoryProvider) {
        return jsonFactoryProvider.getFactory();
    }
}
