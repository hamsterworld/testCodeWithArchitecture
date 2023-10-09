package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void init(){

        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.postService = PostServiceImpl.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(156454646L))
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

        User user2 = User.builder()
                .id(2L)
                .email("email2")
                .nickname("name2")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        fakeUserRepository.save(user1);

        fakeUserRepository.save(user2);

        fakePostRepository.save(Post.builder()
                .id(1L)
                .createdAt(1456767456L)
                .modifiedAt(0L)
                .writer(user1)
                .content("hello world1")
                .build());

        fakePostRepository.save(Post.builder()
                .id(2L)
                .createdAt(1456767456L)
                .modifiedAt(0L)
                .writer(user2)
                .content("hello world2")
                .build());
    }


    @Test
    void getPostById() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }
}