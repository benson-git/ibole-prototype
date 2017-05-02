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

import java.util.Map;

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
public class StatelessToken implements AuthenticationToken {

  private static final long serialVersionUID = -4328416906451280823L;
  private Object principal;
  private Map<String, ?> params;
  private String clientDigest;

  public StatelessToken(String loginId, Map<String, ?> params, String clientDigest) {
    this.principal = loginId;
    this.params = params;
    this.clientDigest = clientDigest;
  }

  public void setPrincipal(Object principal) {
    this.principal = principal;
  }

  public Map<String, ?> getParams() {
    return params;
  }

  public void setParams(Map<String, ?> params) {
    this.params = params;
  }

  public String getClientDigest() {
    return clientDigest;
  }

  public void setClientDigest(String clientDigest) {
    this.clientDigest = clientDigest;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public Object getCredentials() {
    return clientDigest;
  }

  @Override
  public String toString() {
    return "StatelessToken{" + "principal=" + principal + ", params=" + params + ", clientDigest='"
        + clientDigest + '\'' + '}';
  }
}
