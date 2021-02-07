package com.ldg.app.contentcenter;

import com.ldg.app.response.ReslutDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ContentCenterApplicationTests {

    @Test
    void contextLoads() {


    }

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10000; i++) {
            restTemplate.getForObject("http://localhost:9090/test/string", ReslutDto.class);
            Thread.sleep(500);
        }
    }

}
