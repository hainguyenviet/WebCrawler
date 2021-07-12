package com.webcrawler.app.service;

import com.webcrawler.app.entity.RequestUrls;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WebCrawlerService {
    private HashMap<String, Document> dataCrawlers;

    public WebCrawlerService() {
        dataCrawlers = new HashMap<String, Document>();
    }

    public void ingestUrls(List<RequestUrls> urls_list) throws IOException {
        for (RequestUrls url: urls_list) {
            if(!dataCrawlers.containsKey(url.getUrl())) {
                Document document = Jsoup.connect(url.getUrl()).get();
                dataCrawlers.put(url.getUrl(), document);
            }
        }
    }

    public Map<String, Document> findDocumentContainText(String text) {
        Map<String, Document> findText = dataCrawlers.entrySet().stream().filter(e ->
                e.getValue().select(":containsOwn("+text+")") != null)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return findText;
    }

//    public static void main(String[] args) throws IOException {
//        //1. Pick a URL from the frontier
//        List<String> ingestUrls = new ArrayList<String>();
//        ingestUrls.add("https://www.google.com");
//        ingestUrls.add("https://www.bing.com");
//        ingestUrls.add("https://search.yahoo.com/");
//
//        WebCrawlerService webCrawlerService = new WebCrawlerService();
//        webCrawlerService.ingestUrls(ingestUrls);
//        Map<Object, Document> findText = webCrawlerService.findDocumentContainText("http");
//        findText.entrySet().stream().forEach(e -> {
//            Elements elements =  e.getValue().select(":containsOwn(http)");
//            System.out.println("Url:"+e.getKey());
//            elements.forEach(element -> {
//                System.out.println(element.html());
//            });
//        });
//
//    }

}
