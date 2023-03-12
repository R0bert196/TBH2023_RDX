package com.rdx.rdxserver.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


import java.util.Map;

@Service
public class OpenAiApi {

    private final ObjectMapper mapper;

    private final RestTemplate restTemplate;

    @Value("${openAi.embeddings}")
    private String openAiEmbeddingsApi;
    @Value("${openAi.completions}")
    private String openAiCompletionsApi;

    @Value("${openAi.key}")
    private String openApiKey;

    public OpenAiApi(RestTemplateBuilder restTemplateBuilder) {
        this.mapper = new ObjectMapper();;
        this.restTemplate = restTemplateBuilder.build();
    }


    public float[] getTextEmbeddings(String profileText) throws JsonProcessingException {


        HttpHeaders headers = getHttpHeaders();

        Map<String, String  > body = new HashMap<>();
        body.put("model", "text-embedding-ada-002");
        body.put("input", profileText);

        String json = mapper.writeValueAsString(body);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response =  this.restTemplate.postForEntity(openAiEmbeddingsApi, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode embeddingNode = rootNode.path("data").get(0).path("embedding");
        float[] embedding = new float[embeddingNode.size()];
        for (int i = 0; i < embeddingNode.size(); i++) {
            embedding[i] = embeddingNode.get(i).floatValue();
        }
        return embedding;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openApiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public String getIdealProfileForDescription(String textCV) throws JsonProcessingException {
        HttpHeaders headers = getHttpHeaders();

        //leave it as String even though we are poluting, easier to read and can lead to errors if the input data contains special characters that need to be escaped

        String jsonString = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"temperature\": 0.5,\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"system\",\n" +
                "      \"content\": \"Identify the language of the text. Convert this personal description below in a job profile, in the orignal language of the text, without adding any new information unless it is implied by the original text. Do not repeat yourself.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"%s\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        jsonString = String.format(jsonString, textCV);

        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);

        ResponseEntity<String> response =  this.restTemplate.postForEntity(openAiCompletionsApi, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        JsonNode responseJson = mapper.readTree(response.getBody());

        String content = responseJson.get("choices").get(0).get("message").get("content").asText();

        return content;

    }
}


