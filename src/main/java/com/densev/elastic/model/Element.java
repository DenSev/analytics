package com.densev.elastic.model;

/**
 * Created by Dzianis_Sevastseyenk on 02/01/2017.
 */
public class Element {

    private String elementName;
    private String elementPath;
    private ElasticType elementType;
    private boolean isIndexed = true;
    private boolean isAnalyzed = false;
    private boolean hasDocValues = true;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getElementPath() {
        return elementPath;
    }

    public void setElementPath(String elementPath) {
        this.elementPath = elementPath;
    }

    public ElasticType getElementType() {
        return elementType;
    }

    public void setElementType(ElasticType elementType) {
        this.elementType = elementType;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public boolean isAnalyzed() {
        return isAnalyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        isAnalyzed = analyzed;
    }
}
