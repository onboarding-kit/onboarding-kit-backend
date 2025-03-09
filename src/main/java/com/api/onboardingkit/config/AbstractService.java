package com.api.onboardingkit.config;

public abstract class AbstractService {

    protected Long getUserNo() {
        return SecurityUtil.getCurrentUserNo();
    }
}