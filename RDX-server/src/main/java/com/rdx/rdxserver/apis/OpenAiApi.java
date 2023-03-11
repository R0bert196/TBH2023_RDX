package com.rdx.rdxserver.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdx.rdxserver.entities.AppUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


import java.util.Map;

@Service
public class OpenAiApi {

    private final RestTemplate restTemplate;

    @Value("${openAi.api}")
    private String openAiApi;

    @Value("${openAi.key}")
    private String openApiKey;

    public OpenAiApi(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public float[] getTextEmbeddings(String profileText) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openApiKey);
        headers.set("Content-Type", "application/json");

        Map<String, String  > body = new HashMap<>();
        body.put("model", "text-embedding-ada-002");
        body.put("input", profileText);

        String json = mapper.writeValueAsString(body);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response =  this.restTemplate.postForEntity(openAiApi, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }
//        String json = mapper.writeValueAsString(response.getBody());


        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode embeddingNode = rootNode.path("data").get(0).path("embedding");
        float[] embedding = new float[embeddingNode.size()];
        for (int i = 0; i < embeddingNode.size(); i++) {
            embedding[i] = embeddingNode.get(i).floatValue();
        }
        return embedding;
    }

}


