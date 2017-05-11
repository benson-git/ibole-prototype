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

package com.github.ibole.prototype.presentation.web.controller.example;

import com.github.ibole.prototype.presentation.web.model.example.User;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/api/v1/users")  
public class UserRestController{
  
  private static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());  
  
  @ApiOperation(value="问候", notes="不需要传入参数")
  @RequestMapping(value="/greet", method = RequestMethod.GET)
  @ResponseBody
  public String greet(HttpServletRequest req, HttpServletResponse res){
    String str = "Hello";
    if (str.equals("Hello")) {
       throw new NullPointerException("Custom Null point!");
    }
    return str;
  }
  
  @ApiOperation(value = "获取用户列表", notes = "")  
  @RequestMapping(value = { "" }, method = RequestMethod.GET)  
  public List<User> getUserList() {  
      List<User> r = new ArrayList<User>(users.values());  
      return r;  
  }  
  
  @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "用户ID", paramType="path", required = true, dataType = "Long"),
  })
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)  
  public User findById(@PathVariable("id") Long id) {  
      //return userService.findById(1L);  
    return users.get(id);
  } 


//  @RequestMapping(value = "/{id}")  
//  @ResponseBody
//  public User findById(@PathVariable("id") Long id) {  
//      //return userService.findById(1L);  
//    return new User(1l, "a");
//  } 
  @ApiOperation(value="创建新用户", notes="根据User对象创建用户")
  @ApiImplicitParam(name = "user", value = "用户详细模型user", required = true, dataType = "User")  
  @SuppressWarnings({"rawtypes", "unchecked"})
  @RequestMapping(method = RequestMethod.POST)  
  public ResponseEntity<User> save( @Valid @RequestBody User user, UriComponentsBuilder uriComponentsBuilder) {  
      //save user  
      users.put(user.getId(), user);
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
  
  @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息")  
  @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户ID", paramType="path", required = true, dataType = "Long"),  
          @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "User") })   
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)   
  public ResponseEntity<User> update(@PathVariable("id") Long id, @RequestBody User user) { 
    //userService.updateUser(user);
    User u = users.get(id);  
    u.setUsername(user.getUsername());  
    users.put(id, u); 
    return new ResponseEntity<User>(u, HttpStatus.OK); 
  } 

//@RequestMapping(value = "/{id}", method = RequestMethod.PUT)   
//@ResponseStatus(HttpStatus.OK)
//@ResponseBody
//public User update(@RequestBody User user) {  
//  //user = userService.updateUser(user);
//  return user; 
//} 
  @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")  
  @ApiImplicitParam(name = "id",  value = "用户ID", paramType="path", required = true, dataType = "Long")  
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)  
  public ResponseEntity<User> delete(@PathVariable("id") Long id) {
    //userService.deleteUserById(id);
    users.remove(id); 
    return new ResponseEntity<User>(HttpStatus.NO_CONTENT); 
  }  
}