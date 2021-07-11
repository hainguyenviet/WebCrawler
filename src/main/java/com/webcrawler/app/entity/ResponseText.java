package com.webcrawler.app.entity;

import lombok.Data;

import java.util.List;

@Data
public class ResponseText {
    private String url;
    private List<String> elementContain;

    public ResponseText(String url, List<String> elementContain) {
        this.url =url;
        this.elementContain = elementContain;
    };

}
