package com.densev.elastic.utility;

import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonBuilderFactory;

/**
 * Created by Dzianis_Sevastseyenk on 02/06/2017.
 */
@Component
public class JsonFactoryProvider {

    private JsonBuilderFactory factory;

    public JsonFactoryProvider(){
        this.factory = Json.createBuilderFactory(null);
    }

    public JsonBuilderFactory getFactory() {
        return factory;
    }
}
