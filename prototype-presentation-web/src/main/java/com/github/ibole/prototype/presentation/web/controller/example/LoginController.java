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

import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.TokenHandlingException;
import com.github.ibole.prototype.presentation.web.model.example.LoginUser;
import com.github.ibole.prototype.presentation.web.model.example.User;
import com.github.ibole.prototype.presentation.web.security.shiro.StatelessToken;
import com.github.ibole.prototype.presentation.web.security.shiro.WsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

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
@Component
@RequestMapping("/api/v1") 
public class LoginController {
  
  @Autowired
  private WsService wsService;
  
  @Autowired
  private TokenAuthenticator tokenAuthenticator;

  @ApiOperation(value="登录", notes="用户登录")
  @ResponseBody
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<StatelessToken> login(@Valid @RequestBody LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
    User user = wsService.findWsUser(loginUser.getUsername(), loginUser.getPassword());
    StatelessToken token;
    if (user != null) {
      try {
        tokenAuthenticator.createRefreshToken(buildJwtObject(loginUser.getUsername(), 7200, request));
        String tokenStr = tokenAuthenticator.createAccessToken(buildJwtObject(loginUser.getUsername(), 3600, request));
        token = new StatelessToken(loginUser.getUsername(), null, tokenStr);
      } catch (TokenHandlingException e) {
        e.printStackTrace();
        return new ResponseEntity<StatelessToken>(HttpStatus.UNAUTHORIZED);
      }
      return new ResponseEntity<StatelessToken>(token, HttpStatus.OK);
    } else {
      return new ResponseEntity<StatelessToken>(HttpStatus.UNAUTHORIZED);
    }
  }
  
  private JwtObject buildJwtObject(String loginId, int ttl, HttpServletRequest request){
    JwtObject claim = new JwtObject();
    claim.setClientId(request.getRemoteAddr());
    claim.setLoginId(loginId);
    claim.setAudience(loginId);
    claim.setTtlSeconds(ttl);
    return claim;
  }
}
