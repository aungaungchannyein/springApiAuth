package com.appsdeveloperblog.app.ws.mobile_app_ws;



//import io.swagger.v3.oas.models.ExternalDocumentation;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
@Configuration
@OpenAPIDefinition(info = @Info(title = "My API", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

//
//    Contact contact = new Contact(
//            "Sergey Kargopolov",
//            "http://www.appsdeveloperblog.com",
//            "developer@appsdeveloperblog.com"
//    );
//
//    List<VendorExtension> vendorExtensions = new ArrayList<>();
//
//    ApiInfo apiInfo = new ApiInfo(
//            "Photo app RESTful Web Service documentation",
//            "This pages documents Photo app RESTful Web Service endpoints",
//            "1.0",
//            "http://www.appsdeveloperblog.com/service.html",
//            contact,
//            "Apache 2.0",
//            "http://www.apache.org/licenses/LICENSE-2.0",
//            vendorExtensions);

//    @Bean
//    public Docket apiDocket() {
//
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
////                .protocols(new HashSet<>(Arrays.asList("HTTP","HTTPs")))
////                .apiInfo(apiInfo)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.appsdeveloperblog.app.ws"))
//                .paths(PathSelectors.any())
//                .build();
//
//        return docket;
//
//    }
//
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .pathsToMatch("**/**")
//                .build();
//    }

//
//    @Bean
//    public OpenAPI springShopOpenAPI() {
//        return new OpenAPI()
//                .info(new Info().title("SpringShop API")
//                        .description("Spring shop sample application")
//                        .version("v0.0.1")
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
//                .externalDocs(new ExternalDocumentation()
//                        .description("SpringShop Wiki Documentation")
//                        .url("https://springshop.wiki.github.org/docs"));
//    }
}
