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

package com.github.ibole.prototype.presentation.web.security.shiro;

import com.github.ibole.prototype.presentation.web.model.example.User;

import com.google.common.base.Strings;

import java.util.HashSet;
import java.util.Set;

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
public class WsService {

  /**
   * @param employeeid
   * @return
   */
  public Set<String> findUserRoles(String loginId) {
    Set<String> roles = new HashSet<String>();
    roles.add("admin");
    roles.add("user");
    return roles;
  }

  /**
   * @param employeeid
   * @return
   */
  public Set<String> findUserPermissions(String loginId) {
    Set<String> permissions = new HashSet<String>();
    permissions.add("user.read");
    permissions.add("user.create");
    permissions.add("user.update");
    permissions.add("user.delete");
    return permissions;
  }

  /**
   * @param loginId
   * @return
   */
  public User findWsUserById(String loginId) {
    if (Strings.isNullOrEmpty(loginId)){
      return null;
    }
    User user = new User();
    user.setUsername("test");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setDigestkey("password");
    return user;
  }

  /**
   * @param loginId
   * @return
   */
  public User findWsUser(String loginId, String password) {
    if (Strings.isNullOrEmpty(loginId) || Strings.isNullOrEmpty(password)){
      return null;
    }
    User user = new User();
    user.setUsername(loginId);
    user.setDigestkey(password);
    user.setFirstName("firstName");
    user.setLastName("lastName");
    return user;
  }
}
