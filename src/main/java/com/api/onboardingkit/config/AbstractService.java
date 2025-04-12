package com.api.onboardingkit.config;

public abstract class AbstractService {

    protected String getSocialId() {
        return SecurityUtil.getCurrentSocialId();
    }

    protected String getSocialType(){
        return SecurityUtil.getCurrentSocialType();
    }

    protected Long getUserNo() {
        return Long.parseLong(SecurityUtil.getCurrentSocialId());
    }
}