package com.api.onboardingkit.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /**
     * 현재 로그인한 사용자의 user_no 가져오기
     */
//    public static Long getCurrentMemberId() {
//        // todo. 테스트를 위해 user_no를 하드코딩
//        return 12345L;
//    }

    //현재 로그인한 사용자의 user_no 가져오기
//    public static Long getCurrentSocialId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
//            throw new IllegalStateException("로그인이 필요합니다.");
//        }
//        return Long.parseLong(authentication.getName()); // todo. JWT에서 user_no를 String 형태로 저장한다고 가정
//    }

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