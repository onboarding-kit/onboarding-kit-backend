package com.api.onboardingkit.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getCurrentSocialId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return authentication.getName(); // 여기서는 sub (socialId) 값
    }

    public static String getCurrentSocialType() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Claims claims = (Claims) authentication.getCredentials(); // JWT claims 전체
        return claims.get("socialType", String.class); // ex: "kakao", "google"
    }

}