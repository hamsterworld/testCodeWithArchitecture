package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


class PostCreateControllerTest {

    @Test
    void 사용자는게시물을작성할수있다() {

        // 준비
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1534646L)
                .build();

        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloWorld")
                .build();

        // 실행
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

        // 검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getCreatedAt()).isEqualTo(534646L);
        assertThat(result.getBody().getContent()).isEqualTo("helloWorld");
    }
}