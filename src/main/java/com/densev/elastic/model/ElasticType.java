package com.densev.elastic.model;

/**
 * Created by Dzianis_Sevastseyenk on 02/01/2017.
 */
public enum ElasticType {


    //numeric
    BYTE("byte"),
    DOUBLE("double"),
    FLOAT("float"),
    INT("integer"),
    LONG("long"),
    SHORT("short"),
    //string
    TEXT("text"),
    KEYWORD("keyword"),
    //other
    DATE("date"),
    BOOLEAN("boolean"),
    BLOB("blob");


    private String esType;

    ElasticType(String esType) {
        this.esType = esType;
    }

    public String getEsType() {
        return esType;
    }
}
