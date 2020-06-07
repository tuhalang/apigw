package com.tuhalang.apigw.controller;

import com.tuhalang.apigw.configuration.*;
import com.tuhalang.apigw.utils.Convertor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfiguration.class})
public class AccountControllerTest {

    private MockMvc mockMvc;

    @Autowired
    AccountController accountController;

    @Mock
    View mockView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).setSingleView(mockView).build();
    }

    @Test
    public void loginSuccessTest(){
        try {
            MvcResult result = mockMvc.perform(post("/wsSignIn")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "\t\"wsRequest\":{\n" +
                            "\t\t\"username\":\"phamhung\",\n" +
                            "\t\t\"password\":\"123456\"\n" +
                            "\t}\n" +
                            "}")
                    ).andReturn();

            Map<String, Object> map = Convertor.jsonToMap(result.getResponse().getContentAsString());

            Assert.assertNotNull(map);
            Assert.assertEquals(map.get("errorCode"),"0");
            Assert.assertNotNull(map.get("wsResponse"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginFailedTest(){
        try {
            MvcResult result = mockMvc.perform(post("/wsSignIn")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "\t\"wsRequest\":{\n" +
                            "\t\t\"username\":\"phamhung\",\n" +
                            "\t\t\"password\":\"1234567\"\n" +
                            "\t}\n" +
                            "}")
            ).andReturn();

            Map<String, Object> map = Convertor.jsonToMap(result.getResponse().getContentAsString());

            Assert.assertNotNull(map);
            Assert.assertEquals(map.get("errorCode"),"1");
            Assert.assertNull(map.get("wsResponse"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
