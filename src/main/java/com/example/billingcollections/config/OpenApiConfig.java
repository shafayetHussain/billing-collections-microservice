package com.example.billingcollections.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI configuration for the Billing Collections service.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI billingCollectionsOpenAPI() {

        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Billing and Collections API")
                        .description(
                                "Microservice for billing schedules, payment attempts, retries, and delinquency tracking.\n\n"
                                        + "## Authentication\n"
                                        + "This API uses HTTP Basic Authentication.\n\n"
                                        + "**Demo Credentials:**\n"
                                        + "- Username: `user`\n"
                                        + "- Password: `user123`\n\n"
                                        + "**For Postman users:**\n"
                                        + "Go to the Authorization tab, select **Basic Auth**, and enter the username and password.\n\n"
                                        + "**Or manually add this header:**\n"
                                        + "- `Authorization: Basic dXNlcjp1c2VyMTIz`\n\n"
                                        + "**Note:** In-memory credentials are used here only for demonstration purposes. "
                                        + "In a production system, this would be replaced with a proper external identity and access management solution."
                        )
                        .version("1.0")
                        .contact(new Contact()
                                .name("Engineering Team")
                                .email("shafatdumcj@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}