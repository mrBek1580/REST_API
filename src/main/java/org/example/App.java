package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class App {
    public static void main(String[] args) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        restTemplate.getMessageConverters().add(jsonHttpMessageConverter);

        String url = "http://94.198.50.185:7081/api/users";
        ResponseEntity<String> responce = restTemplate.getForEntity(url, String.class);
        String cookie = responce.toString().split("Set-Cookie:")[1].split(";")[0].replace("\"", "");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", cookie);

        String res = "";

        User user = new User();
        user.setId(3L);
        user.setName("James");
        user.setLastName("Brown");
        user.setAge((byte)19);
        HttpEntity<User> firstRequest = new HttpEntity<>(user, httpHeaders);
        res += restTemplate.postForObject(url, firstRequest, String.class);

        user.setName("Thomas");
        user.setLastName("Shelby");
        firstRequest = new HttpEntity<>(user, httpHeaders);
        res += restTemplate.exchange(url, HttpMethod.PUT, firstRequest, String.class).getBody();

        HttpEntity<?> secondRequest = new HttpEntity<>(null, httpHeaders);
        res += restTemplate.exchange(url + "/3", HttpMethod.DELETE, secondRequest, String.class).getBody();

        System.out.println(res);
    }
}
