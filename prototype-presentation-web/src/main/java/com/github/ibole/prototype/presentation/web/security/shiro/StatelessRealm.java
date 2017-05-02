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

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
public class StatelessRealm extends AuthorizingRealm {

  private Logger logger = LoggerFactory.getLogger(StatelessRealm.class);

  @Autowired
  private WsService wsService;


  @Override
  public boolean supports(AuthenticationToken token) {
     //仅支持StatelessToken类型的Token
     return token instanceof StatelessToken;
  }

  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
     String loginId =String.valueOf(principals.getPrimaryPrincipal());
     SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
     authorizationInfo.setRoles(wsService.findUserRoles(loginId));
     authorizationInfo.setStringPermissions(wsService.findUserPermissions(loginId));
     return authorizationInfo;
  }
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
     StatelessToken statelessToken = (StatelessToken) token;
     String loginId = String.valueOf(statelessToken.getPrincipal()); 
     User wsUser = wsService.findWsUserById(loginId);
     //验证用户以及账套
     if(wsUser == null) {
        throw new UnknownAccountException();//没找到帐号
     }
//   if(user.getUsestatus() != Constants.DDL_USESTATUS_YES
//         || wsUser.getUsestatus() != Constants.DDL_USESTATUS_YES) {
//      throw new LockedAccountException(); //帐号锁定
//   }
     //验证摘要信息
     String serverDigetst = HmacSHA256Utils.digest(wsUser.getDigestkey(), statelessToken.getParams());
     logger.info(serverDigetst);
     return new SimpleAuthenticationInfo(
         wsUser.getUserName(), //用户名
           serverDigetst,
           getName()  //realm name
     );
  }
}