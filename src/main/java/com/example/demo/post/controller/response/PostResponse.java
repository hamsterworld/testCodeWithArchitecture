package com.example.demo.post.controller.response;

import com.example.demo.user.controller.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

// Controller 의 View 같은 개념입니다. 그래서 따로 dto 로 빠졌습니다.
@Getter
@Setter
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;
}
