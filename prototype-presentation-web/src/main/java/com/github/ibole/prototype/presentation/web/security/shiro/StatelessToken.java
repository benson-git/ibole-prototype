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

import org.apache.shiro.authc.AuthenticationToken;

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
 * Perform bearer token authentication In Shiro.
 * @author bwang
 *
 */
public class StatelessToken implements AuthenticationToken {

  private static final long serialVersionUID = -4328416906451280823L;
  private Object principal;
  private String token;
  private String clientId;

  public StatelessToken(String token, String loginId, String clientId) {
    this.principal = loginId;
    this.token = token;
    this.clientId = clientId;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String clientDigest) {
    this.token = clientDigest;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public Object getCredentials() {
    return token;
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

  @Override
  public String toString() {
    return "StatelessToken{" + "principal=" + principal + ", clientId=" + clientId +", token='"
        + token + '\'' + '}';
  }
}
