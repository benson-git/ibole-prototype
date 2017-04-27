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

package com.github.ibole.prototype.presentation.web.controller.example.test;

import com.github.ibole.prototype.presentation.web.controller.example.UserRestController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class ApplicationTests {

  private MockMvc mvc;

  @Before
  public void setUp() throws Exception {
    mvc = MockMvcBuilders.standaloneSetup(new UserRestController()).build();
  }

  @Test
  public void testUserController() throws Exception {
    // 测试UserController
    RequestBuilder request = null;

    // 1、get查一下user列表，应该为空
    request = get("/users/");
    mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));

    // 2、post提交一个user
    request = post("/users/").param("id", "1").param("userName", "测试大师");
    mvc.perform(request)
    // .andDo(MockMvcResultHandlers.print())
        .andExpect(content().string(equalTo("success")));

    // 3、get获取user列表，应该有刚才插入的数据
    request = get("/users/");
    mvc.perform(request).andExpect(status().isOk())
        .andExpect(content().string(equalTo("[{\"id\":1,\"userName\":\"测试大师\"}]")));

    // 4、put修改id为1的user
    request = put("/users/1").param("userName", "测试终极大师");
    mvc.perform(request).andExpect(content().string(equalTo("success")));

    // 5、get一个id为1的user
    request = get("/users/1");
    mvc.perform(request).andExpect(
        content().string(equalTo("{\"id\":1,\"userName\":\"测试终极大师\"}")));

    // 6、del删除id为1的user
    request = delete("/users/1");
    mvc.perform(request).andExpect(content().string(equalTo("success")));

    // 7、get查一下user列表，应该为空
    request = get("/users/");
    mvc.perform(request).andExpect(status().isOk()).andExpect(content().string(equalTo("[]")));

  }

}
