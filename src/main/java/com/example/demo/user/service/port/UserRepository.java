package com.example.demo.user.service.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

// 외부 연동을 담당하는 post package 에 두자.
// 이렇게해야 Service Layer 가 UserRepository 에 의존하는 그림이 나온다.
// 만약에 infrastructure 에 있다면 해당 layer 에 의존하는것처럼 보이기때문이다.
public interface UserRepository {

    Optional<User> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<User> findByEmailAndStatus(String email, UserStatus userStatus);

    User save(User user);

    Optional<User> findById(long id);

}
