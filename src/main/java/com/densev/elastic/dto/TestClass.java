package com.densev.elastic.dto;

/**
 * Created on 20.01.2017.
 */
public class TestClass {

    private final Integer id;
    private final String text;

    private TestClass(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public static class Builder {

        private static int id;
        private static String text;


        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder test(String text){
            this.text = text;
            return this;
        }

        public TestClass build(){
            return new TestClass(id, text);
        }

    }


}
