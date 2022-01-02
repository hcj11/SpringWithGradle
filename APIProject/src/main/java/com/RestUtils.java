package com;

import org.springframework.web.client.RestTemplate;

public class RestUtils {
    public static RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        return  restTemplate;
    }
}
