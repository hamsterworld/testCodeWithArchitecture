package com.example.demo.post.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

// 애네는 controller 에서 request 로 같이 사용되고있어서, controller 의 하위패키지로 가야했나 싶엇으나
// service 에서 참조해야되는거라서 domain 에 넣기로했다.
@Getter
public class PostUpdate {

    private final String content;

    @Builder
    public PostUpdate(
        @JsonProperty("content") String content) {
        this.content = content;
    }
}
