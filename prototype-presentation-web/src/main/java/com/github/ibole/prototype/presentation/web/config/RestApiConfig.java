/*
 * Copyright 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.ibole.prototype.presentation.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * Restful API 访问路径: 
 * http://IP:port/{context-path}/documentation/swagger-ui.html 
 * 
 * @author bwang
 *
 */
@Configuration
@EnableWebMvc //NOTE: Only needed in a non-springboot application
@ComponentScan("com.github.ibole.prototype.presentation.web.controller")
@EnableSwagger2
public class RestApiConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {

    registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs");
    registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui",
        "/swagger-resources/configuration/ui");
    registry.addRedirectViewController("/documentation/swagger-resources/configuration/security",
        "/swagger-resources/configuration/security");
    registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/documentation/swagger-ui.html**").addResourceLocations(
        "classpath:/META-INF/resources/swagger-ui.html");
    registry.addResourceHandler("/documentation/webjars/**").addResourceLocations(
        "classpath:/META-INF/resources/webjars/");
  }

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(
            RequestHandlerSelectors
                .basePackage("com.github.ibole.prototype.presentation.web.controller"))
        .paths(PathSelectors.any()).build();
  }

  private ApiInfo apiInfo() {
    Contact contact = new Contact("bwang", "", "");
    return new ApiInfoBuilder()
        .title("Spring Boot中使用Swagger2构建RESTful APIs")
        .description(
            "更多Spring Boot相关文章请关注：https://github.com/benson-git/ibole-spring-cloud-example")
        .termsOfServiceUrl("https://github.com/benson-git/ibole-spring-cloud-example")
        .contact(contact).version("1.0").build();
  }
}
