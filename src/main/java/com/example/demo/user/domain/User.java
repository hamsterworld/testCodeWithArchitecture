package com.example.demo.user.domain;


import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.util.UUID;

@Getter
public class User {

    private final Long id;

    private final String email;

    private final String nickname;

    private final String address;

    private final String certificationCode;

    private final UserStatus status;

    private final Long lastLoginAt;


    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    // 이런거 보면 도메인객체에 이렇게 전달받아서 넣어줘도 되는구나.
    public static User from(UserCreate userCreate, UuidHolder uuidHolder){
//    public static User from(UserCreate userCreate, String uuid){ // 개인적으로 이게 더 맞지않나 생각이 든다.
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .status(UserStatus.PENDING)
                // 직접 의존 변경 uuid
                .certificationCode(uuidHolder.random())
                .build();
    }

    public User update(UserUpdate userUpdate){
        return User.builder()
                .id(id)
                .email(email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .certificationCode(certificationCode)
                .status(status)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User login(ClockHolder clockHolder){
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .certificationCode(certificationCode)
                .status(status)
                .lastLoginAt(clockHolder.millis())
                .build();
    }

    public User certificate(String certificationCode){
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .certificationCode(certificationCode)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(lastLoginAt)
                .build();
    }

}
