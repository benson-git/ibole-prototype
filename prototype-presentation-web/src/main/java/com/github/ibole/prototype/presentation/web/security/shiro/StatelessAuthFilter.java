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

package com.github.ibole.prototype.presentation.web.security.shiro;

import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.jose4j.JoseUtils;

import com.google.common.base.Strings;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class StatelessAuthFilter extends AccessControlFilter {

  private Logger logger = LoggerFactory.getLogger(StatelessAuthFilter.class);

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
      Object mappedValue) throws Exception {
    return false;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    JwtObject jwtObject = JwtObject.getEmpty();
    try {

      String jwtToken = WsWebUtil.getTokenFromHeader((HttpServletRequest) request);

      if (Strings.isNullOrEmpty(jwtToken)) {
        onLoginFail(response);
        return false;
      }
      jwtObject = JoseUtils.claimsOfTokenWithoutValidation(jwtToken);
      StatelessToken token =
          new StatelessToken(jwtToken, jwtObject.getLoginId(), jwtObject.getClientId());
      // Delegate to Realm to authenticate the request
      Subject subject = getSubject(request, response);
      subject.login(token);
      logger.debug("Authenticated successfully for '{}' from '{}'!", jwtObject.getLoginId(),
          jwtObject.getClientId());
    } catch (Exception e) {
      logger.error("Authenticated failed for '{}' from '{}' - ", jwtObject.getLoginId(),
          jwtObject.getClientId(), e);
      onLoginFail(response); // failed to authenticate
      return false;
    }
    return true;
  }

  // Response with the 401 status code when the authentication is failed. 
  private void onLoginFail(ServletResponse response) throws IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    httpResponse.setHeader(HttpStatus.UNAUTHORIZED.value()+"", HttpStatus.UNAUTHORIZED.getReasonPhrase());
  }
}
