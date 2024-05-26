package CodeIt.Ytrip.common.exception;

import CodeIt.Ytrip.controller.TestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ApiExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestController testController;

    @Test
    public void testUserExceptionHandler() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/test/exception"))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println("result = " + result);
        System.out.println(result.getResponse().getContentAsString());
    }

}