package com.festivo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Festivo Event Planning API")
                        .description("""
                                A comprehensive REST API for the Festivo Event Planning Platform.
                                
                                This API provides endpoints for:
                                - **Vendor Management**: Register, manage profiles, and list services
                                - **Event Planning**: Create and manage events with vendor bookings
                                - **Booking System**: Handle service bookings with payment processing
                                - **Real-time Communication**: Messaging between customers and vendors
                                - **Review System**: Rate and review vendors and services
                                - **Scheduling**: Manage vendor availability and time slots
                                
                                ## Authentication
                                The API uses JWT tokens for authentication. Include the token in the Authorization header:
                                ```
                                Authorization: Bearer <your-jwt-token>
                                ```
                                
                                ## Rate Limiting
                                API requests are rate-limited to ensure fair usage. Please respect the limits.
                                
                                ## Support
                                For API support, please contact our development team.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Festivo Development Team")
                                .email("dev@festivo.com")
                                .url("https://festivo.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.festivo.com")
                                .description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
