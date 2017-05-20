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

import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.TokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.TokenHandlingException;
import com.github.ibole.infrastructure.security.jwt.TokenStatus;
import com.github.ibole.infrastructure.web.security.model.LoginRequest;
import com.github.ibole.infrastructure.web.security.model.LoginResponse;
import com.github.ibole.infrastructure.web.security.model.TokenRenewRequest;
import com.github.ibole.infrastructure.web.security.model.TokenRenewResponse;
import com.github.ibole.infrastructure.web.security.model.UserModel;
import com.github.ibole.infrastructure.web.security.spring.shiro.WsUserService;

import com.google.common.base.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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

  private Logger logger = LoggerFactory.getLogger(getClass().getName());

  @Autowired
  private WsUserService wsService;

  @Autowired
  private TokenAuthenticator tokenMgr;

  /**
   * UserModel login with username and password provided.
   */
  @ResponseBody
  @RequestMapping(value = "/login", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginUser,
      HttpServletRequest request, HttpServletResponse response) {
    UserModel user = wsService.findWsUser(loginUser.getUsername(), loginUser.getPassword());
    LoginResponse result = new LoginResponse();
    if (user != null) {
      try {
        String refreshToken =
            tokenMgr.createRefreshToken(buildJwtObject(loginUser.getUsername(), 360, request));
        String accessToken =
            tokenMgr.createAccessToken(buildJwtObject(loginUser.getUsername(), 60, request));
        result.setRefreshToken(refreshToken);
        result.setAccessToken(accessToken);
        result.setAuthenticated(true);
      } catch (TokenHandlingException e) {
        logger.error("Login error happen with user '{}'", loginUser.getUsername(), e);
        result.setAuthenticated(false);
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
  public ResponseEntity<TokenRenewResponse> renew(
      @Valid @RequestBody TokenRenewRequest reqTokenInfo, HttpServletRequest request,
      HttpServletResponse response) {

    ResponseEntity<TokenRenewResponse> entityResponse =
        new ResponseEntity<TokenRenewResponse>(HttpStatus.UNAUTHORIZED);
    TokenRenewResponse newTokenResponse = new TokenRenewResponse();

    if (Strings.isNullOrEmpty(reqTokenInfo.getClientId())) {
      // the request is from pc
      reqTokenInfo.setClientId("TODO"/*request.getRemoteAddr()*/);
    }

    TokenStatus status =
        tokenMgr.validRefreshToken(reqTokenInfo.getRefreshToken(), reqTokenInfo.getClientId());
    newTokenResponse.setTokenStatus(status);

    try {
      if (status.isValidated()) {
        String accessToken = tokenMgr.renewAccessToken(reqTokenInfo.getRefreshToken(), 120);
        newTokenResponse.setAccessToken(accessToken);
        newTokenResponse.setLoginRequired(false);
        entityResponse = ResponseEntity.status(HttpStatus.OK).body(newTokenResponse);
      } else {
        newTokenResponse.setLoginRequired(true);
        entityResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(newTokenResponse);
      }

    } catch (TokenHandlingException e) {
      logger.error("Renew token error happen with refresh token '{}' from client '{}'",
          reqTokenInfo.getRefreshToken(), reqTokenInfo.getClientId(), e);
      newTokenResponse.setLoginRequired(true);
      entityResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(newTokenResponse);
    }
    return entityResponse;
  }


  private JwtObject buildJwtObject(String loginId, int ttl, HttpServletRequest request) {
    JwtObject claim = new JwtObject();
    claim.setClientId("TODO"/*request.getRemoteAddr()*/);
    claim.setLoginId(loginId);
    claim.setAudience(loginId);
    claim.setTtlSeconds(ttl);
    return claim;
  }
}
