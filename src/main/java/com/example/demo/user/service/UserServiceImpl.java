package com.example.demo.user.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.user.controller.port.*;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;


import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Builder
//public class UserServiceImpl implements UserService { 이제 서비스를 단일 인터페이스가 아니라 여러개의 인터페이스에 의존하도록 한다.
//public class UserServiceImpl implements UserCreateService, UserUpdateService, UserReadService,AuthenticationService { //근데 또 테스트컨테이너쓰니까 그럴필요 없는것같다.
// 다시하나로 통일ㅋ
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CertificationService certificationService;
    private final UuidHolder uuidHolder;
    private final ClockHolder clockHolder;

    @Override
    public User getById(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE).orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", email));
    }
    @Override
    public User getByIdOrElseThrow(long id) {
        return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }
    @Override
    @Transactional
    public User create(UserCreate userCreate) {
        User user = User.from(userCreate,uuidHolder);
        user = userRepository.save(user);
        certificationService.send(userCreate.getEmail(),user.getId(),user.getCertificationCode());
        return user;
    }
    @Override
    @Transactional
    public User update(long id, UserUpdate userUpdate) {
        User user = getById(id);
        User update = user.update(userUpdate);
        return userRepository.save(update);
    }
    @Override
    @Transactional
    public void login(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        User login = user.login(clockHolder);
        // Jpa 와의 연결이 끊어졌기때문에 save 해준다.
        userRepository.save(login);
    }
    @Override
    @Transactional
    public void verifyEmail(long id, String certificationCode) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
        User certificate = user.certificate(certificationCode);
        // Jpa 와의 연결이 끊어졌기때문에 save 해준다.
        userRepository.save(certificate);
    }

}