package com.api.onboardingkit.global.response;

import com.api.onboardingkit.global.response.exception.CustomException;
import com.api.onboardingkit.global.response.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class ResponseTestService {
    public String getTestData(boolean shouldThrowError) {
        if (shouldThrowError) {
            throw new CustomException(ErrorCode.TEST_ERROR_CODE);
        }
        return "Test data";
    }
}
