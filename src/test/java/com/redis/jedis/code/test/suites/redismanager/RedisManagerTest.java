package com.redis.jedis.code.test.suites.redismanager;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class RedisManagerTest {

    private static String REDIS_PUSH_URI = "http://localhost:8080/api/redis/push/list/";
    private static String REDIS_POP_URI = "http://localhost:8080/api/redis/pop/list/leads";

    private static String LEAFTAP_DATA_URI = "http://localhost:8080/api/leaftap/mandatory/data";

    public static void main(String[] args) throws IOException, InterruptedException {

        redisListPushRequest(REDIS_PUSH_URI, listPushHeader("leads"), "15104");

        System.out.println(redisListPopRequest(REDIS_POP_URI));

        System.out.println(fetchLeafTapMandantoryTestData(LEAFTAP_DATA_URI));

    }

    public static String listPushHeader(String keyName) throws JsonProcessingException {
        var values = new HashMap<String, String>() {
            {
                put("key_name", keyName);
            }
        };

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(values);

        return requestBody;

    }

    public static void redisListPushRequest(String url, String requestBody, String pathParam) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .uri(URI.create(url+pathParam))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }

    public static String redisListPopRequest(String url) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        var objectMapper = new ObjectMapper();
        RedisPopResponse res = objectMapper.readValue(response.body(), new TypeReference<RedisPopResponse>() {});
        System.out.println(res.getLead_id());

        return response.body().replaceAll("[^0-9]", "");

    }

    public static String fetchLeafTapMandantoryTestData(String url) {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                                         .GET()
                                         .uri(URI.create(url))
                                         .build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }

        return getLeafTapMandatoryTestData(response, "company_name");

    }

    public static String getLeafTapMandatoryTestData(HttpResponse<String> response, String keyName) {
        var objectMapper = new ObjectMapper();
        LeafTapMandantoryDataResponse leafTapData = null;
        try {
            leafTapData = objectMapper.readValue(response.body(), new TypeReference<LeafTapMandantoryDataResponse>() {});
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }

        switch (keyName.toLowerCase()) {
            case "company_name": return leafTapData.getCompany_name();
            case "first_name": return leafTapData.getFirst_name();
            case "last_name": return leafTapData.getLast_name();
            default: return null;
        }
    }


}