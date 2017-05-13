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

import javax.validation.constraints.NotNull;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * User login information, only use for login case.
 * 
 * @author bwang
 *
 */
public class LoginRequest {

  @NotNull(message="{username.not.empty}") 
  private String username;
  @NotNull(message="{password.not.empty}")
  private String password;
  //nullable for the login from PC side, notnull for Mobile.
  private String clientId;
  
  public LoginRequest() {
    //do nothing
  }
  /**
   * 
   * @param username String
   * @param password String
   * @param clientId String
   */
  public LoginRequest(String username, String password, String clientId) {
    this.username = username;
    this.password = password;
    this.clientId = clientId;
  }
  
  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }
  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }
  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }
  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
  /**
   * @return the clientId
   */
  public String getClientId() {
    return clientId;
  }
  /**
   * @param clientId the clientId to set
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }
  
  
}
