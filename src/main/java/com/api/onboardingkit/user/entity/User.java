package com.api.onboardingkit.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")  // 예약어 충돌 방지
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;  // 소셜 로그인 이메일
    private String name;   // 소셜 로그인 사용자 이름
    private String nickname; // 닉네임
    private String role;  // 직무 (백엔드, 프론트엔드, 디자이너 등)
    private String detailRole;  // 상세 직무
    private int experience; // 연차

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)") // ENUM 대신 문자열 저장
    private SocialType socialType; // GOOGLE, KAKAO, APPLE 등

    private String socialId; // 소셜 로그인 ID

    public void updateProfile(String nickname, String role, String detailRole, int experience) {
        this.nickname = nickname;
        this.role = role;
        this.detailRole = detailRole;
        this.experience = experience;
    }
}
