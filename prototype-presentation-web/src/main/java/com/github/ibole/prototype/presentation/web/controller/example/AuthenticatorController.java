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

package com.github.ibole.prototype.presentation.web.controller.example;

import com.github.ibole.infrastructure.common.UserPrincipalProto.AuthTokenInfo;
import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.TokenHandlingException;
import com.github.ibole.prototype.presentation.web.model.example.LoginResponse;
import com.github.ibole.prototype.presentation.web.model.example.LoginRequest;
import com.github.ibole.prototype.presentation.web.model.example.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
 * @author bwang
 *
 */
@RestController
@Component
@RequestMapping("/api/v1/auth")
public class AuthenticatorController {

  @Autowired
  private WsService wsService;

  @Autowired
  private TokenAuthenticator tokenMgr;

  /**
   * User login with username and password provided.
   */
  @ResponseBody
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginUser,
      HttpServletRequest request, HttpServletResponse response) {
    User user = wsService.findWsUser(loginUser.getUsername(), loginUser.getPassword());
    LoginResponse result = LoginResponse.DEFAULT;
    if (user != null) {
      try {
        String refreshToken =
            tokenMgr.createRefreshToken(buildJwtObject(loginUser.getUsername(), 7200, request));
        String accessToken =
            tokenMgr.createAccessToken(buildJwtObject(loginUser.getUsername(), 3600, request));
        result.setRefreshToken(refreshToken);
        result.setAccessToken(accessToken);
        result.setAuthenticated(true);
      } catch (TokenHandlingException e) {
        result.setAuthenticated(false);
        result.setErrorMessage(e.getMessage());
        return new ResponseEntity<LoginResponse>(result, HttpStatus.UNAUTHORIZED);
      }
      return new ResponseEntity<LoginResponse>(result, HttpStatus.OK);
    } else {
      result.setAuthenticated(false);
      return new ResponseEntity<LoginResponse>(result, HttpStatus.UNAUTHORIZED);
    }
  }

  /**
   * 
   * Renew a new access token with the refresh token and username.
   */
  @RequestMapping(value = "/renew", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LoginResponse> renew(@Valid @RequestBody AuthTokenInfo reqTokenInfo,
      HttpServletRequest request, HttpServletResponse response) {
    LoginResponse resTokenInfo = LoginResponse.DEFAULT;

   // if(reqTokenInfo.get)
    //tokenMgr.validRefreshToken(token, clientId)

    return new ResponseEntity<LoginResponse>(resTokenInfo, HttpStatus.OK);
  }


  private JwtObject buildJwtObject(String loginId, int ttl, HttpServletRequest request) {
    JwtObject claim = new JwtObject();
    claim.setClientId(request.getRemoteAddr());
    claim.setLoginId(loginId);
    claim.setAudience(loginId);
    claim.setTtlSeconds(ttl);
    return claim;
  }
}
