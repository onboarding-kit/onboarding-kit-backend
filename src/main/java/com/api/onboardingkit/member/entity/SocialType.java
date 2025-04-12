package com.api.onboardingkit.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    GOOGLE("google"),
    KAKAO("kakao"),
    APPLE("apple");

    private final String value;

    public static SocialType from(String input) {
        for (SocialType type : SocialType.values()) {
            if (type.getValue().equalsIgnoreCase(input)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown social type: " + input);
    }
}
