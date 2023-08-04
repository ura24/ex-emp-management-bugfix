package com.example.form;


public class SearchForm {
    /** 検索する名前 */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SearchForm [name=" + name + "]";
    }

}
