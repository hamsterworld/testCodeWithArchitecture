package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;

import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Builder
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    // 근데 잘보니까 UserRepository 에 getById 만필요한거같아서 밑에 의존성이나 이런게 너무 덕지덕지많다.
    // Uuid,mailSender 같은건 쓰지도않는데 넣어주는게 귀찮다.
    // Test 짜는게 귀찮아진다 => Test 에서 의존성을 줄이라고 신호를 보내는것
    // private final UserService userService;

    // 아래처럼 의존성을 2개만 주입받아서 사용하자.
    // userService 에 의존되어있는것들이 많아서 너무힘들었었다.
    // 이젠 2개면 충분 테스트코드가 귀찮다 => 의존성을 줄이라고 신호를 보내는것
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    @Override
    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }
    @Override
    public Post create(PostCreate postCreate) {
        // 실제 강의에서는 쓸데없이 getById 를 하나더 추가해서만들어줬는데 UserRepository 에 추가해줘서 흠.. 굳이?
        User writer = userRepository.findById(postCreate.getWriterId()).get();
        Post post = Post.create(writer,postCreate,clockHolder);
        return postRepository.save(post);
    }
    @Override
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getPostById(id);
        Post update = post.update(postUpdate,clockHolder);
        return postRepository.save(update);
    }
}