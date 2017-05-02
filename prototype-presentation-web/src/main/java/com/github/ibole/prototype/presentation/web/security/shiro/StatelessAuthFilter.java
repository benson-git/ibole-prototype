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

import com.github.ibole.infrastructure.common.utils.Constants;

import com.google.common.base.Strings;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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

  @SuppressWarnings("unchecked")
  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws Exception {
    // 1、客户端生成的消息摘要
    String clientDigest = request.getParameter(Constants.STATELESS_PARAM_DIGEST);
    // 2、客户端传入的用户身份
    String loginId;
    try {
      loginId = request.getParameter(Constants.STATELESS_PARAM_USERNAME);
    } catch (Throwable throwable) {
      logger.error("User digest information not found from the request parameters: " + request.getParameterMap());
      return false;
    }
    
    if(Strings.isNullOrEmpty(clientDigest) || Strings.isNullOrEmpty(loginId)) {
      onLoginFail(response); 
      return false;
    }
    // 3、客户端请求的参数列表
    Map<String, String[]> params = new LinkedHashMap<>(request.getParameterMap());
    params.remove(Constants.STATELESS_PARAM_DIGEST);
    logger.debug("Filter param:");
    params.forEach((key, value) -> logger.debug(key + "-" + Arrays.toString(value)));
    // 4、生成无状态Token
    StatelessToken token = new StatelessToken(loginId, params, clientDigest);
    try {
      // 5、委托给Realm进行登录
      Subject subject = getSubject(request, response);
      subject.login(token);
      logger.info("Authenticated successfully!");
    } catch (Exception e) {
      logger.error("Authenticated failed - ", e);
      onLoginFail(response); // 6、登录失败
      return false;
    }
    return true;
  }

  // 登录失败时默认返回401状态码
  private void onLoginFail(ServletResponse response) throws IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    httpResponse.setHeader(HttpStatus.UNAUTHORIZED.value()+"", HttpStatus.UNAUTHORIZED.getReasonPhrase());
  }
}
