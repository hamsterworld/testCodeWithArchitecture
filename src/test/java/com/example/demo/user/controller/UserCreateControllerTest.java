package com.example.demo.user.controller;

import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    void 사용자는회원가입을할수있고회원가입된사용자는PENDING상태이다(){
        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(() -> "aaaa-aaa-aaaa")
                .build();

        UserCreate userCreate = UserCreate.builder()
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .build();

        // 실행
        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);

        //검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("email1");
        assertThat(result.getBody().getNickname()).isEqualTo("name1");
        assertThat(result.getBody().getStatus()).isEqualTo(PENDING);
        assertThat(result.getBody().getLastLoginAt()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(testContainer.userRepository.findById(1).get().getCertificationCode()).isEqualTo("aaaa-aaa-aaaa");
    }

}