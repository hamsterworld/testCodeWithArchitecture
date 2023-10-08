package com.example.demo.user.infrastructure;

import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);
}
