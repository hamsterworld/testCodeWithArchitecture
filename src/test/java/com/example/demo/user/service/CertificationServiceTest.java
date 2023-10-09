package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {

    @Test
    void 이메일과컨텐츠가제대로만들어져서보내는지테스트한다() {
        // 준비
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        // 실행
        certificationService.send("ssoboro1@gmail.com",1,"aaaaa-aaaa-aaaaaa-aaaaa");

        // 검증
        assertThat(fakeMailSender.email).isEqualTo("ssoboro1@gmail.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        // content 검증
    }

}