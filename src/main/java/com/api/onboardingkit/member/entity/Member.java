package com.api.onboardingkit.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String email;  // 소셜 로그인 이메일
    private String name;   // 소셜 로그인 사용자 이름
    private String nickname; // 닉네임
    private String role;  // 직무 (백엔드, 프론트엔드, 디자이너 등)
    private String detailRole;  // 상세 직무
    private int experience; // 연차

    @Setter
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)") // ENUM 대신 문자열 저장
    private SocialType socialType; // GOOGLE, KAKAO, APPLE

    private String socialId; // 소셜 로그인 ID

    public void updateProfile(String nickname, String role, String detailRole, int experience) {
        this.nickname = nickname;
        this.role = role;
        this.detailRole = detailRole;
        this.experience = experience;
    }

}
