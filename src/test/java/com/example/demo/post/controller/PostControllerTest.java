package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostControllerTest {

    @Test
    void 사용자는게시물을단건조회할수있다(){
        // 준비
        TestContainer testContainer = TestContainer.builder()
                .build();

        User user1 = User.builder()
                .id(1L)
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();
        testContainer.userRepository.save(user1);

        Post post = Post.builder()
                .id(1L)
                .createdAt(100L)
                .content("helloWorld")
                .build();
        testContainer.postRepository.save(post);


        // 실행
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1);

        // 검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
        assertThat(result.getBody().getContent()).isEqualTo("helloWorld");
    }
}