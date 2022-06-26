package com.company.reviewsapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ReviewsApiApplication {

    public static void main(String[] args) { SpringApplication.run(ReviewsApiApplication.class, args); }

    @Value("${omdbApi}")
    private String omdbApiUrl;

    @Bean
    public WebClient omdbApiClient() {
        return WebClient.create(omdbApiUrl);
    }
}
