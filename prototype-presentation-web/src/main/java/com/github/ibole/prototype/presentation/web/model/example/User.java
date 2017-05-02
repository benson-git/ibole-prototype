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
 * @author bwang
 *
 */
public class User {

  @NotNull(message="{id.not.empty}") 
  private Long id;
  @NotNull(message="{username.not.empty}") 
  private String userName;
  
  private String digestkey;

  public User() {}
  /**
   * @param i
   * @param string
   */
  public User(Long id, String userName) {
    this.id = id;
    this.userName = userName;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @param userName the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }
  /**
   * @return the digestkey
   */
  public String getDigestkey() {
    return digestkey;
  }
  /**
   * @param digestkey the digestkey to set
   */
  public void setDigestkey(String digestkey) {
    this.digestkey = digestkey;
  }
  
  
}
