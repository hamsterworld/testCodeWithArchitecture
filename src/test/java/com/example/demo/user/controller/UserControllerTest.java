package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.example.demo.user.domain.UserStatus.ACTIVE;
import static com.example.demo.user.domain.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() {
        // 준비
//        UserController.builder()
//                .userService() userService 를 fake 로 구현하기 너무힘들다. 이테스트에서는 특정유저정보를 가져오는 getMethod 만 필요로 하기때문이다. 그래서 interface 를 분리해주겠다.
//                .build();


        // 아래처럼 stub 을하는건 또선호하지않는다.
//        UserController userController = UserController.builder()
//                .userReadService(new UserReadService() {
//                    @Override
//                    public User getById(long id) {
//                        return User.builder()
//                                .id(id)
//                                .email("email1")
//                                .nickname("name1")
//                                .address("seoul")
//                                .certificationCode("aaaa-a-aaaa-aaaa")
//                                .status(ACTIVE)
//                                .lastLoginAt(0L)
//                                .build();
//                    }
//
//                    @Override
//                    public User getByEmail(String email) {
//                        return null;
//                    }
//
//                    @Override
//                    public User getByIdOrElseThrow(long id) {
//                        return null;
//                    }
//                })
//                .build();

        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("email1")
            .nickname("name1")
            .address("seoul")
            .certificationCode("aaaa-a-aaaa-aaaa")
            .status(ACTIVE)
            .lastLoginAt(100L)
            .build());


        // 실행
//        ResponseEntity<UserResponse> result = userController.getUserById(1L);

        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

        // 검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("email1");
        assertThat(result.getBody().getNickname()).isEqualTo("name1");
        assertThat(result.getBody().getStatus()).isEqualTo(ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getId()).isEqualTo(1L);

    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다(){

        // 아래와 같은 stub 은 별루 선호하지 않는다.
        // 준비
//        UserController userController = UserController.builder()
//                .userReadService(new UserReadService() {
//                    @Override
//                    public User getById(long id) {
//                        throw new ResourceNotFoundException("Users", id);
//                    }
//
//                    @Override
//                    public User getByEmail(String email) {
//                        return null;
//                    }
//
//                    @Override
//                    public User getByIdOrElseThrow(long id) {
//                        return null;
//                    }
//                })
//                .build();
//
//        // 실행, 검증
//        assertThatThrownBy(() -> userController.getUserById(1234567)).isInstanceOf(ResourceNotFoundException.class);

        // 준비
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(ACTIVE)
                .lastLoginAt(100L)
                .build());

        // 실행,검증
        assertThatThrownBy(() ->  testContainer.userController.getUserById(143534)).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다(){
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(PENDING)
                .lastLoginAt(100L)
                .build());


        // 실행
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaa-a-aaaa-aaaa");

        // 검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(302));
        assertThat(testContainer.userRepository.findById(1).get().getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void 사용자는인증코드가일치하지않을경우권한없음을에러를내려준다(){
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(PENDING)
                .lastLoginAt(100L)
                .build());


        // 실행,검증
        assertThatThrownBy(() -> testContainer.userController.verifyEmail(1,"aaaa-a-aaaa-aaaa")).isInstanceOf(CertificationCodeNotMatchedException.class);

    }

    @Test
    void 사용자는내정보를불러올때개인정보인주소도갖고올수있다(){
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1656573443)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(PENDING)
                .lastLoginAt(100L)
                .build());

        // 실행
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("email1");

        //검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("email1");
        assertThat(result.getBody().getNickname()).isEqualTo("name1");
        assertThat(result.getBody().getStatus()).isEqualTo(ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("seoul");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1656573443L);
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void 사용자는내정보를수정할수있다(){
        TestContainer testContainer = TestContainer.builder().build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("email1")
                .nickname("name1")
                .address("seoul")
                .certificationCode("aaaa-a-aaaa-aaaa")
                .status(PENDING)
                .lastLoginAt(100L)
                .build());

        // 실행
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("email1", UserUpdate.builder()
                        .address("경기도")
                        .nickname("name2")
                .build());

        // 검증
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getEmail()).isEqualTo("email1");
        assertThat(result.getBody().getNickname()).isEqualTo("name2");
        assertThat(result.getBody().getStatus()).isEqualTo(ACTIVE);
        assertThat(result.getBody().getAddress()).isEqualTo("경기도");
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(100L);
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

}