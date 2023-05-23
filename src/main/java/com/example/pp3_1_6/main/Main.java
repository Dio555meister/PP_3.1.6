package com.example.pp3_1_6.main;

import com.example.pp3_1_6.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Main {

    public static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, String.class);
        String cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);

        ResponseEntity<String> response = post(restTemplate, headers);
        ResponseEntity<String> responseEntityPut = put(restTemplate, headers);
        ResponseEntity<String> responseEntityDelete = delete(restTemplate, headers);
        output(response, responseEntityPut, responseEntityDelete);

    }

    private static ResponseEntity<String> delete(RestTemplate restTemplate, HttpHeaders headers) {
        HttpEntity<Object> entityDelete = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntityDelete = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, entityDelete, String.class);
        System.out.println();
        System.out.println(responseEntityDelete.getStatusCode());
        System.out.println(responseEntityDelete.getBody());
        return responseEntityDelete;
    }

    private static ResponseEntity<String> put(RestTemplate restTemplate, HttpHeaders headers) {
        User userPut = new User(3L, "Thomas", "Shelby", (byte) 28);
        HttpEntity<User> entityPut = new HttpEntity<>(userPut, headers);
        ResponseEntity<String> responseEntityPut = restTemplate.exchange(URL, HttpMethod.PUT, entityPut, String.class);
        System.out.println();
        System.out.println(responseEntityPut.getStatusCode());
        System.out.println(responseEntityPut.getBody());
        return responseEntityPut;
    }

    private static ResponseEntity<String> post(RestTemplate restTemplate, HttpHeaders headers) {
        User user = new User(3L, "James", "Brown", (byte) 28);
        HttpEntity<User> userPost = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL, userPost, String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        return response;
    }

    private static void output(ResponseEntity<String> response, ResponseEntity<String> responseEntityPut, ResponseEntity<String> responseEntityDelete) {
        System.out.println();
        System.out.println(response.getBody() + responseEntityPut.getBody() + responseEntityDelete.getBody());
    }
}

