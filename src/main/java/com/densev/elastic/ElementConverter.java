package com.densev.elastic;

import com.densev.elastic.model.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Created by Dzianis_Sevastseyenk on 02/06/2017.
 */
@Component
public class ElementConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ElementConverter.class);

    @Autowired
    JsonBuilderFactory jsonFactory;

    public String convert(Element element) {

        JsonObjectBuilder fieldSettings = jsonFactory.createObjectBuilder()
            .add("type", element.getElementType().getEsType());
        if (!element.isAnalyzed() && !element.isIndexed()) {
            fieldSettings.add("index", "no");
        } else if(element.isIndexed()){
            fieldSettings.add("index", "not_analyzed");
        }
        JsonObject field = jsonFactory.createObjectBuilder()
            .add(element.getElementName(), fieldSettings)
            .build();

        LOG.info(jsonFactory.getConfigInUse().toString());


        return field.toString();
    }
}
