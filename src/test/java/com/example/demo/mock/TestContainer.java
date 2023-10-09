package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {
    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
//    public final UserReadService userReadService;
//    public final UserUpdateService userUpdateService;
//    public final UserCreateService userCreateService;
//    public final AuthenticationService authenticationService;
    public final PostService postService;
    public final CertificationService certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostController postController;
    public final PostCreateController postCreateController;

    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder){
        // 어떻게 보면 이런게 의존성역전이고
        // 이게 스프링 IoC 컨테이너가 해주는것이기도하다.
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .postRepository(this.postRepository)
                .userRepository(this.userRepository)
                .clockHolder(clockHolder)
                .build();

        this.certificationService = new CertificationService(this.mailSender);

        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(uuidHolder)
                .clockHolder(clockHolder)
                .userRepository(new FakeUserRepository())
                .certificationService(this.certificationService)
                .build();

//        this.userReadService = userService;
//        this.userUpdateService = userService;
//        this.userCreateService = userService;
//        this.authenticationService = userService;
//
//        this.userController = UserController.builder()
//                .userReadService(userReadService)
//                .userUpdateService(userUpdateService)
//                .userCreateService(userCreateService)
//                .authenticationService(authenticationService)
//                .build();

        this.userController = UserController.builder().userService(userService).build();


//        this.userCreateController = UserCreateController.builder()
//                .userCreateService(userCreateService)
//                .build();

        this.userCreateController = UserCreateController.builder()
                .userService(userService)
                .build();

        this.postController = PostController.builder()
                .postService(postService)
                .build();
        this.postCreateController = PostCreateController.builder()
                .postService(postService)
                .build();

    }


}
