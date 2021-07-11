package com.webcrawler.app.controller;

import com.webcrawler.app.entity.RequestUrls;
import com.webcrawler.app.entity.ResponseText;
import com.webcrawler.app.service.WebCrawlerService;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("crawler")
public class CrawlerController {

    @Autowired
    WebCrawlerService crawlerService;

    @PostMapping("/ingest")
    public ResponseEntity ingestUrls(@RequestBody RequestUrls urls_list) {
        try {
            crawlerService.ingestUrls(urls_list.getUrls());
            return ResponseEntity.ok("Ingest Urls ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body("Bad Request");
        }
    }

    @PostMapping("/findText")
    public ResponseEntity findText(@RequestBody String textFind) {
        Map<String, Document> findText = crawlerService.findDocumentContainText(textFind);
        List<ResponseText> response= findText.entrySet().stream()
                .filter(e -> e.getValue().select(":containsOwn("+textFind+")").size() > 0)
                .map(e -> {
                    Elements elements = e.getValue().select(":containsOwn("+textFind+")");
                    List<String> elementContain = elements.stream().map(el -> el.html()).collect(Collectors.toList());
                    return new ResponseText(e.getKey(), elementContain);
                }).collect(Collectors.toList());
        if(!response.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok("No result found");
        }
    }
}
