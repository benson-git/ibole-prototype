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

package com.github.ibole.prototype.presentation.web.model.example;


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
public class AuthTokenInfo {
  
  /**
   * A blank {@code AuthTokenInfo} that all fields are not set.
   */
  public static final AuthTokenInfo DEFAULT = new AuthTokenInfo();

  private String username;
  //identify whether the token that user provided is authenticated or not,
  // user need to login if loginRequired is true.
  private boolean loginRequired;
  
  private String refreshToken;
  
  private String accessToken;
  //token status or description
  private String description;
  
  private AuthTokenInfo() {
    //do nothing.
  }
  
  public AuthTokenInfo withRefreshToken(String refreshToken) {
    AuthTokenInfo tokenInfo = new AuthTokenInfo(this);
    tokenInfo.refreshToken = refreshToken;
    return tokenInfo;
  }
  
  public AuthTokenInfo withAccessToken(String accessToken) {
    AuthTokenInfo tokenInfo = new AuthTokenInfo(this);
    tokenInfo.accessToken = accessToken;
    return tokenInfo;
  }
  
  public AuthTokenInfo withUsername(String username) {
    AuthTokenInfo tokenInfo = new AuthTokenInfo(this);
    tokenInfo.username = username;
    return tokenInfo;
  }
  
  public AuthTokenInfo withLoginRequired(boolean loginRequired) {
    AuthTokenInfo tokenInfo = new AuthTokenInfo(this);
    tokenInfo.loginRequired = loginRequired;
    return tokenInfo;
  }
  
  public AuthTokenInfo withDescription(String description) {
    AuthTokenInfo tokenInfo = new AuthTokenInfo(this);
    tokenInfo.description = description;
    return tokenInfo;
  }
  
  /**
   * @return the refreshToken
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * @return the accessToken
   */
  public String getAccessToken() {
    return accessToken;
  }
  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }
  /**
   * @return the loginRequired
   */
  public boolean isLoginRequired() {
    return loginRequired;
  }
  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Copy constructor.
   */
  private AuthTokenInfo(AuthTokenInfo other) {
    username = other.username;
    loginRequired = other.loginRequired;
    refreshToken = other.refreshToken;
    accessToken = other.accessToken;
    description = other.description;
  }

}
