/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ibole.prototype.presentation.web.config;

import com.github.ibole.infrastructure.common.mapper.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Collections;
import java.util.List;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * TODO: Zero config with Spring Configuration.
 * @author bwang
 *
 */
@Configuration  
@EnableWebMvc  
@ComponentScan(basePackages = "com.github.ibole.prototype.presentation.web.controller", useDefaultFilters = false, includeFilters = {  
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})  
}) 
public class AppConfig extends WebMvcConfigurationSupport {

  @Bean  
  public MappingJackson2HttpMessageConverter converter() {  
      Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
      builder.configure(new JsonMapper());
      builder.indentOutput(true);
      List<MediaType> supportedMediaTypes = Collections.emptyList();
      supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
      MappingJackson2HttpMessageConverter jsonMessageConverter  = new MappingJackson2HttpMessageConverter(builder.build());
      jsonMessageConverter .setSupportedMediaTypes(supportedMediaTypes);
      return jsonMessageConverter;
  }  
}
