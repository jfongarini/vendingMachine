package com.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ConnectionService {

    @Autowired
    private RestTemplate restTemplate;

    public String getData(String URL) throws JsonProcessingException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(URL);
        return restTemplate.getForObject(builder.buildAndExpand().toUri(), String.class);
    }
}
