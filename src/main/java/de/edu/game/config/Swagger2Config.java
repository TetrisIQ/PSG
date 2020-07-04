package de.edu.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
/**
 * Configuration for the Swagger (SpringFox) API Documentation
 */
public class Swagger2Config {


    @Bean
    public Docket actuatorApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Lernspiels V. 0.1.0")
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("de.edu.game"))
                .paths(PathSelectors.regex("/.*"))
                .build().apiInfo(apiEndPointsInfo())
                .securityContexts(Arrays.asList(actuatorSecurityContext()))
                .securitySchemes(Arrays.asList(basicAuthScheme()));
    }

    /**
     * Defines witch endpoints need Authentications
     * For testing Regular expressions use: https://www.freeformatter.com/java-regex-tester.html
     *
     * @return @{@link SecurityContext}
     */
    private SecurityContext actuatorSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(basicAuthReference()))
                .forPaths(PathSelectors.regex("/game(/\\w*)*|/user/login/?|/admin(/\\w*)*"))
                .build();
    }

    /**
     * Define the Authentication schema
     * @return @{@link SecurityScheme}
     */
    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }


    /**
     * General information about the Project
     * @return @{@link ApiInfo}
     */
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("A programmable space game (PSG)")
                .description("Entwicklung eines Lernspiels für die Programmierausbildung")
                .contact(new Contact("Alexander Werner", "https://github.com/TetrisIQ/psg", "psg@tetrisiq.de"))
                .license("")
                .licenseUrl("")
                .version("0.1.0")
                .build();
    }
}
