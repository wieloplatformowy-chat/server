package net.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Mariusz Gorzycki
 * @since 20.03.2016.
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(metadata());
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Chat Backend")
                .description("Api documentation")
                .version("0.2.1 - Friends")
                .contact("projektchat22@gmail.com")
                .license("Copyright © 2016")
                .build();
    }
}
