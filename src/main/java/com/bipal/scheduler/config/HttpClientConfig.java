package com.bipal.scheduler.config;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
    @Bean
    public AsyncHttpClient getHttpClient() {
        return Dsl.asyncHttpClient();
    }
}
