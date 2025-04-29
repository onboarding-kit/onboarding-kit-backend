package com.api.onboardingkit.config;
import com.api.onboardingkit.config.exception.CustomException;
import com.api.onboardingkit.config.exception.ErrorCode;
import com.api.onboardingkit.global.response.ResponseTestController;
import com.api.onboardingkit.global.response.ResponseTestService;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResponseTestController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ResponseTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResponseTestService responseTestService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void isOk() throws Exception {
        given(responseTestService.getTestData(false))
                .willReturn("정상 데이터");

        mockMvc.perform(get("/res/test")
                        .param("throwError", "false"))
                .andExpect(status().isOk());
    }

    @Test
    void isBad() throws Exception {
        doThrow(new CustomException(ErrorCode.TEST_ERROR_CODE))
                .when(responseTestService).getTestData(true);

        mockMvc.perform(get("/res/test")
                        .param("throwError", "true"))
                .andExpect(status().isBadRequest());
    }

}
