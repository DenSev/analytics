package com.densev.elastic.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Dzianis_Sevastseyenk on 02/01/2017.
 */
public class Document {

    private String id;
    private Map<String, Element> elementsMap;

    public Document() {
        elementsMap = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Element> getElements() {
        return newArrayList(elementsMap.values());
    }

    public void putElement(Element e) {
        elementsMap.put(e.getElementName(), e);
    }


}
