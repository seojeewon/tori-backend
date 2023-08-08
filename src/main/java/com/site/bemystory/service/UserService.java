package com.site.bemystory.service;

import com.site.bemystory.domain.User;
import com.site.bemystory.exception.AppException;
import com.site.bemystory.exception.ErrorCode;
import com.site.bemystory.repository.UserRepository;
import com.site.bemystory.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000 * 60 * 60L;

    public String join(String userName, String password, String email) {
        //username 중복 CHECK
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 있습니다");
                });

        // 저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .email(email)
                .build();
        userRepository.save(user);
        return "SUCCESS";
    }

    public String login(String userName, String password) {
        //userName 없음
        User selectedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));
        //password 틀림
        if(!encoder.matches(password, selectedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        String token = JwtTokenUtil.createToken(selectedUser.getUserName(), key, expireTimeMs);
        //error 없으면 token 발행
        return token;
    }

    public User findUserId(String userName){
        return userRepository.findByUserName(userName).orElseThrow();
    }
}