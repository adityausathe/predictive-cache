package com.adus.predictivecache.client.domains;

import com.adus.predictivecache.framework.config.PredictivelyCachedResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class MockPageServer {

    @GetMapping(value = "/page-1")
    @PredictivelyCachedResource(id = "page-1")
    public String page1() {
        String data = "page_1::response";
        idle(data);
        return data;
    }

    @GetMapping(value = "/page-2")
    @PredictivelyCachedResource(id = "page-2")
    public String page2() {
        String data = "page_2::response";
        idle(data);
        return data;
    }

    @GetMapping(value = "/page-3")
    @PredictivelyCachedResource(id = "page-3")
    public String page3() {
        String data = "page_3::response";
        idle(data);
        return data;
    }

    @GetMapping(value = "/page-4")
    @PredictivelyCachedResource
    public String page4WithAutoResourceId() {
        String data = "page_4::response";
        idle(data);
        return data;
    }

    @GetMapping(value = "/page-5")
    public String uncachablePage5() {
        String data = "page_5::response";
        idle(data);
        return data;
    }

    private void idle(String pageData) {
        log.info("Thread: " + Thread.currentThread().getName() + " <<<>>> PageData: " + pageData + " <<<>>> Started");
        try {
            // fake processing
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Thread: " + Thread.currentThread().getName() + " <<<>>> PageData: " + pageData + " <<<>>> Ended");
    }
}
