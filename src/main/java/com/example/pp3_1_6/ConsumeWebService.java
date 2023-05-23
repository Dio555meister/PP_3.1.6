package com.example.pp3_1_6;

import com.example.pp3_1_6.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ConsumeWebService {

    private final RestTemplate restTemplate;
    private final String URL = "http://94.198.50.185:7081/api/users/";

    private List<String> cookie;
    private StringBuilder result = new StringBuilder();

    @Autowired
    public ConsumeWebService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        ConsumeWebService webService = new ConsumeWebService(new RestTemplate());

        webService.getAllUsers();
        System.out.println("--------------------------------------------------------");

        webService.addUser(new User(3L, "James", "Brown", (byte) 30));
        System.out.println("--------------------------------------------------------");


        webService.editUser(new User(3L, "Thomas", "Shelby", (byte) 30));
        System.out.println("--------------------------------------------------------");

        webService.deleteUser(3L);
        System.out.println("--------------------------------------------------------");

        System.out.println(webService.result);
    }

    @GetMapping(value = "/template/api/users")
    public ResponseEntity<String> getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
        cookie = response.getHeaders().get("Set-Cookie");

        return response;
    }

    @PostMapping(value = "/template/api/users")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", String.join(";", cookie));
        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);

        result.append(response.getBody());

        return response;
    }

    @PutMapping(value = "/template/api/users")
    public ResponseEntity<String> editUser(@RequestBody User user) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Cookie", cookie.stream().collect(Collectors.joining(";")));
        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);

        result.append(response.getBody());

        return response;
    }

    @DeleteMapping(value = "/template/api/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie.stream().collect(Collectors.joining(";")));
        HttpEntity<User> entity = new HttpEntity<User>(headers);

        ResponseEntity<String> response = restTemplate.exchange(URL + id, HttpMethod.DELETE, entity, String.class);

        result.append(response.getBody());

        return response;
    }
}
