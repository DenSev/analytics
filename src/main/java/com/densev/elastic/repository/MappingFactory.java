package com.densev.elastic.repository;

import com.densev.elastic.ElementConverter;
import com.densev.elastic.model.Document;
import com.densev.elastic.model.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dzianis_Sevastseyenk on 02/06/2017.
 */
@Component
public class MappingFactory {

    @Autowired
    private ElementConverter elementConverter;

    public String getMappingsFromDocument(Document document){
        StringBuilder mappingBuilder = new StringBuilder();
        mappingBuilder.append("  \"mappings\": {\n" +
            "    \"").append(document.getId())
            .append(
            " \": {\n" +
            "      \"dynamic\": \"strict\",\n" +
            "      \"properties\": {");

        List<Element> elements = document.getElements();
        for (int i =0; i < elements.size()- 1; i++){

            mappingBuilder.append(elementConverter.convert(elements.get(i)))
            .append(",\n");

        }
        mappingBuilder.append(elementConverter.convert(elements.get(elements.size()-1)))
        .append("\n}\n}\n}");



        return mappingBuilder.toString();
    }
}
