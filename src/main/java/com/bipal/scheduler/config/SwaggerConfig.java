package com.bipal.scheduler.config;

import com.bipal.scheduler.controller.SchedulerController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SchedulerController.class.getPackage().getName()))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Scheduler API",
                "Scheduler Api",
                "API v0.1",
                "Free to use",
                new Contact("Bipal Adhikari", "", ""),
                "MIT License",
                "https://opensource.org/licenses/MIT",
                new ArrayList<>()
        );
    }
}


