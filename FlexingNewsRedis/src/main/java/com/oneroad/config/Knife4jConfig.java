package com.oneroad.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("FlexingNews文档接口")
                        .description("# 用Swagger和Knife4j实现")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("外部文档")
                        .url("https://springshop.wiki.github.org/docs"));
    }
}

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//
//@Configuration
//@EnableSwagger2WebMvc
//public class Knife4jConfig {
//    @Bean
//    public Docket Api() {
//        //使用Swagger2语法
//        Docket docket = new Docket(DocumentationType.SWAGGER_2)
//                //接口信息描述
//                .apiInfo(new ApiInfoBuilder()
//                        //设置网站标题
//                        .title("FlexingNews文档接口")
//                        //描述，可用md语法
//                        .description("# 用Swagger和Knife4j实现")
//                        //服务条款URL
//                        .termsOfServiceUrl(".....")
//                        //设置作者，服务器url，邮箱
//                        .contact(new Contact("ONEROAD", "http://localhost:8080", "1920979147@qq.com"))
//                        //许可证
//                        .license("...")
//                        //许可证url
//                        .licenseUrl("...")
//                        //版本
//                        .version("1.0")
//                        .build())
//                //设置包路径
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.ondroad"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
//}
