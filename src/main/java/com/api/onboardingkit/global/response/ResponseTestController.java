package com.api.onboardingkit.global.response;

import com.api.onboardingkit.global.response.dto.Response;
import com.api.onboardingkit.global.response.dto.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResponseTestController {
    private final ResponseTestService responseTestService;

    @GetMapping("/res/test")
    public Response<String> getTest(@RequestParam boolean throwError) {
        String data = responseTestService.getTestData(throwError);
        return Response.success(data, SuccessStatus.TEST_SUCCESS_CODE);
    }
}
