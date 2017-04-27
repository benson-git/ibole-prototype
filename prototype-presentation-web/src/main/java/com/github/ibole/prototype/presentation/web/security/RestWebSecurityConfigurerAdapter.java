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

package com.github.ibole.prototype.presentation.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.accept.ContentNegotiationStrategy;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
@EnableWebSecurity
@Configuration   
public class RestWebSecurityConfigurerAdapter extends
   WebSecurityConfigurerAdapter {
  
  @Bean
  public RestAuthenticationEntryPoint unauthorizedHandler() {
    return new RestAuthenticationEntryPoint();
  }
  
  //@Autowired
  //private UserDetailsService customUserDetailsService;
  
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .inMemoryAuthentication()
        .withUser("user")  
          .password("password")
          .roles("USER")
          .and()
        .withUser("admin") 
          .password("password")
          .roles("ADMIN","USER");
    //registry.userDetailsService(customUserDetailsService);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {

    web.ignoring().antMatchers("/documentation/**", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
        "/configuration/security", "/swagger-ui.html", "/webjars/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/signup","/about").permitAll() 
        .antMatchers("/users/**").hasRole("ADMIN") 
        .anyRequest().authenticated() // 7
        .and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler());
  }
  
  @Override
  public void setContentNegotationStrategy(
          ContentNegotiationStrategy contentNegotiationStrategy) {
    //super.setContentNegotationStrategy(contentNegotiationStrategy);
  }
  
}
