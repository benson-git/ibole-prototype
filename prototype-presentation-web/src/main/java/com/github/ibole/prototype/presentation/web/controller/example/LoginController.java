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

import com.github.ibole.infrastructure.common.utils.Constants;
import com.github.ibole.prototype.presentation.web.model.example.User;
import com.github.ibole.prototype.presentation.web.security.shiro.WsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

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
@Component
@RequestMapping("/api/v1") 
public class LoginController {
  
  @Autowired
  private WsService wsService;

  @ApiOperation(value="登录", notes="用户登录")
  @ResponseBody
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<User> login(HttpServletRequest request) {
    String userName = request.getParameter(Constants.STATELESS_PARAM_USERNAME);
    String password = request.getParameter(Constants.WS_PARAM_PASSWORD);
    User user = wsService.findWsUser(userName, password);
    if (user != null) {
      return new ResponseEntity<User>(HttpStatus.OK);
    } else {
      return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
    }
  }
}
