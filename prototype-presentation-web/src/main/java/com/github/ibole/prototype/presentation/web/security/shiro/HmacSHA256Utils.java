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

import org.apache.commons.codec.binary.Hex;

import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class HmacSHA256Utils {

  public static String digest(String key, String content) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      byte[] secretByte = key.getBytes(Constants.SYSTEM_ENCODING);
      byte[] dataBytes = content.getBytes(Constants.SYSTEM_ENCODING);

      SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
      mac.init(secret);

      byte[] doFinal = mac.doFinal(dataBytes);
      byte[] hexB = new Hex().encode(doFinal);
      return new String(hexB, Constants.SYSTEM_ENCODING);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String digest(String key, Map<String, ?> map) {
    StringBuilder s = new StringBuilder();
    for (Object values : map.values()) {
      if (values instanceof String[]) {
        for (String value : (String[]) values) {
          s.append(value);
        }
      } else if (values instanceof List) {
        for (String value : (List<String>) values) {
          s.append(value);
        }
      } else {
        s.append(values);
      }
    }
    return digest(key, s.toString());
  }

}
