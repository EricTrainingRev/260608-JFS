package com.theblind.todo;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.theblind.todo.TodoApplication;
import static org.springframework.boot.SpringApplication.run;

public class UserRegistrationTests {
	ApplicationContext app;
    HttpClient webClient;

    /**
     * Before every test, reset the database, restart the Javalin app, and create a new webClient and ObjectMapper
     * for interacting locally on the web.
     * @throws InterruptedException
     */
    @BeforeEach
    public void setUp() throws InterruptedException {
        webClient = HttpClient.newHttpClient();
        String[] args = new String[] {};
        app = SpringApplication.run(TodoApplication.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
    	Thread.sleep(500);
    	SpringApplication.exit(app);
    }

    /**
     * Sending an http request to POST localhost:8080/auth/register when username does not exist in the system
     * 
     * Expected Response:
     *  Status Code: 201
     */
    @Test
    public void registerUserSuccessful() throws IOException, InterruptedException {
        String json = "{\"username\":\"john_doe\",\"password\":\"AbcDe**123\"}";
    	HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        int status = response.statusCode();
        Assertions.assertEquals(201, status, "Expected Status Code 201 - Actual Code was: " + status);
    }

     /**
     * Sending an http request to POST localhost:8080/auth/register when username already exists in system
     * 
     * Expected Response:
     *  Status Code: 400
     */
    @Test
    public void registerUserDuplicateUsername() throws IOException, InterruptedException {
    	String json = "{\"username\":\"john_doe\",\"password\":\"AbcDe**123\"}";
    	HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response1 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status1 = response1.statusCode();
        int status2 = response2.statusCode();
        Assertions.assertEquals(201, status1, "Expected Status Code 201 - Actual Code was: " + status1);
        Assertions.assertEquals(400, status2, "Expected Status Code 400 - Actual Code was: " + status2);
    }

    /**
     * Sending an http request to POST localhost:8080/auth/register when username is invalid
     * 
     * Expected Response:
     *  Status Code: 400
     */
    @Test
    public void registerUserInvalidUsername() throws IOException, InterruptedException {
        String json = "{\"username\":\"john\",\"password\":\"AbcDe**123\"}";
    	HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        int status = response.statusCode();
        Assertions.assertEquals(400, status, "Expected Status Code 400 - Actual Code was: " + status);
    }
    
    /**
     * Sending an http request to POST localhost:8080/auth/register when password is invalid
     * 
     * Expected Response:
     *  Status Code: 400
     */
    @Test
    public void registerUserInvalidPassword() throws IOException, InterruptedException {
        String json = "{\"username\":\"john_doe\",\"password\":\"abc\"}";
    	HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        int status = response.statusCode();
        Assertions.assertEquals(400, status, "Expected Status Code 400 - Actual Code was: " + status);
    }

    /**
     * Sending an http request to POST localhost:8080/auth/register when username AND password is invalid
     * 
     * Expected Response:
     *  Status Code: 400
     */
    @Test
    public void registerUserInvalidUsernameAndPassword() throws IOException, InterruptedException {
        String json = "{\"username\":\"john\",\"password\":\"abc\"}";
    	HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/register"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        int status = response.statusCode();
        Assertions.assertEquals(400, status, "Expected Status Code 400 - Actual Code was: " + status);
    }
}
