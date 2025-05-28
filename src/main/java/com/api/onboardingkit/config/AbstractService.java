package com.api.onboardingkit.config;

public abstract class AbstractService {

    protected Long getMemberId() {
        return Long.parseLong(SecurityUtil.getCurrentSocialId());
    }
}