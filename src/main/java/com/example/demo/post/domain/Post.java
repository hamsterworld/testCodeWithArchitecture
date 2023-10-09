package com.example.demo.post.domain;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

@Getter
public class Post {

    private Long id;

    private String content;

    private Long createdAt;

    private Long modifiedAt;

    private User writer;

    @Builder
    public Post(Long id, String content, Long createdAt, Long modifiedAt, User writer) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static Post create(User writer, PostCreate postCreate, ClockHolder clockHolder) {
        return Post.builder()
                .writer(writer)
                .content(postCreate.getContent())
                // 아래처럼 Clock 에 아직도 직접의존하고있다. 변경해주자.
//                .createdAt(Clock.systemUTC().millis())
                .createdAt(clockHolder.millis())
                .build();
    }

    public Post update(PostUpdate postUpdate, ClockHolder clockHolder) {
        return Post.builder()
                .id(id)
                .content(postUpdate.getContent())
                .createdAt(createdAt)
                // 아래처럼 Clock 에 아직도 직접의존하고있다. 변경해주자.
//                .modifiedAt(Clock.systemUTC().millis())
                .modifiedAt(clockHolder.millis())
                .writer(writer)
                .build();
    }
}
