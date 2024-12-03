package com.example.userservice.entity;


import com.example.common.global.BaseEntity;
import com.example.common.global.oauth.kakao.dto.OauthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "USER")
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; //회원 id

    @NotBlank
    private String socialId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;

    @NotBlank
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String job;

    private int workExperience;
}
