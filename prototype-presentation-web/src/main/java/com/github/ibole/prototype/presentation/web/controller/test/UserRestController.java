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

package com.github.ibole.prototype.presentation.web.controller.test;

import com.github.ibole.prototype.presentation.web.model.test.User;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


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
@RestController
@Component("greeterAction")
@RequestMapping("/api/v1//users")  
public class UserRestController{
  
  @RequestMapping(value="/greet")
  @ResponseBody
  public String greet(HttpServletRequest req, HttpServletResponse res){
    String str = "Hello";
    if (str.equals("Hello")) {
       throw new NullPointerException("Custom Null point!");
    }
    return str;
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)  
  public User findById(@PathVariable("id") Long id) {  
      //return userService.findById(1L);  
    return new User(1l, "a");
  } 


//  @RequestMapping(value = "/{id}")  
//  @ResponseBody
//  public User findById(@PathVariable("id") Long id) {  
//      //return userService.findById(1L);  
//    return new User(1l, "a");
//  } 

  @SuppressWarnings({"rawtypes", "unchecked"})
  @RequestMapping(method = RequestMethod.POST)  
  public ResponseEntity<User> save( @Valid @RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {  
      //save user  
      user.setId(1L);  
      MultiValueMap headers = new HttpHeaders();  
      headers.set(HttpHeaders.LOCATION, uriComponentsBuilder.path("/api/v1//users/{id}").buildAndExpand(user.getId()).toUriString());  
      return new ResponseEntity(user, headers, HttpStatus.CREATED);  
  }  

  
//  @SuppressWarnings({"rawtypes", "unchecked"})
//  @RequestMapping(method = RequestMethod.POST)  
//  @ResponseStatus(HttpStatus.CREATED)
//  @ResponseBody
//  public User save(@RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {  
//      //save user  
//      user.setId(1L);   
//      return user;  
//  } 
  
  
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)   
  public ResponseEntity<User> update(@RequestBody User user) {  
    //userService.updateUser(user);
    return new ResponseEntity<User>(user, HttpStatus.OK); 
  } 

//@RequestMapping(value = "/{id}", method = RequestMethod.PUT)   
//@ResponseStatus(HttpStatus.OK)
//@ResponseBody
//public User update(@RequestBody User user) {  
//  //user = userService.updateUser(user);
//  return user; 
//} 
  
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)  
  public ResponseEntity<User> delete(@PathVariable("id") Long id) { 
    //userService.deleteUserById(id);
    return new ResponseEntity<User>(HttpStatus.NO_CONTENT);  
  }  
}
