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

import com.alibaba.fastjson.JSONArray;
import com.github.ibole.infrastructure.common.utils.Constants;
import com.github.ibole.infrastructure.common.utils.JsonUtils;
import com.github.ibole.prototype.presentation.web.security.exception.AuthenticationServiceException;
import com.github.ibole.prototype.presentation.web.security.exception.HttpErrorStatus;

import com.google.common.base.Strings;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

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
public class WsWebUtil {

  public static long resolveEmployeeId(HttpServletRequest request) {
    return Long.valueOf(request.getParameter(Constants.STATELESS_PARAM_USERNAME));
  }

  public static <T> T resolveParamJsonToObject(HttpServletRequest request, String parameter,
      Class<T> clazz) {
    return JsonUtils.fromJson(request.getParameter(parameter), clazz);
  }

  public static <T> List<T> resolveParamListJsonToObject(HttpServletRequest request,
      String parameter, Class<T> clazz) {
    List<T> resultObjList = JSONArray.parseArray(request.getParameter(parameter), clazz);
    return resultObjList;
  }

  public static String getTokenFromHeader(HttpServletRequest request) {
    String authentications = request.getHeader(Constants.HEADER_AUTH_NAME);
    if (Strings.isNullOrEmpty(authentications)
        || authentications.length() < Constants.HEADER_AUTH_PREFIX.length()) {
      throw new AuthenticationServiceException(HttpErrorStatus.ACCOUNT_INVALID);
    }

    return authentications.substring(Constants.HEADER_AUTH_PREFIX.length(),
        authentications.length());
  }
  
  public static void customServletReponse(ServletResponse response, HttpStatus httpStatus,
      HttpErrorStatus customErrorStatus) throws IOException {
    HttpServletResponse httpResponse = WebUtils.toHttp(response);
    httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    httpResponse.setStatus(httpStatus.value());
    httpResponse.setHeader(httpStatus.value() + "", httpStatus.getReasonPhrase());
    httpResponse.getWriter().write(customErrorStatus.toJson());
  }
}
