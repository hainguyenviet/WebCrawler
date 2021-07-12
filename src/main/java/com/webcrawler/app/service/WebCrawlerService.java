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
                e.getValue().select(":containsOwn("+text+")").size() > 0)
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        return findText;
    }

    public static void main(String[] args) throws IOException {
        //1. Pick a URL from the frontier
        List<RequestUrls> ingestUrls = new ArrayList<RequestUrls>();
        RequestUrls g =new RequestUrls();g.setUrl("https://www.google.com");
        RequestUrls b =new RequestUrls();b.setUrl("https://www.bing.com");
        RequestUrls y =new RequestUrls();y.setUrl("https://search.yahoo.com/");
        ingestUrls.add(b);
        ingestUrls.add(g);
        ingestUrls.add(y);

        WebCrawlerService webCrawlerService = new WebCrawlerService();
        webCrawlerService.ingestUrls(ingestUrls);
        Map<String, Document> findText = webCrawlerService.findDocumentContainText("google");
        findText.entrySet().stream().forEach(e -> {
            Elements elements =  e.getValue().select(":containsOwn(google)");
                System.out.println("Url:"+e.getKey());
                elements.forEach(element -> {
                    System.out.println(element.html());
                });


        });

    }

}
