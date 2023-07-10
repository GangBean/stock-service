package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.UserDto;
import com.gangbean.stockservice.domain.Authority;
import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.domain.Role;
import com.gangbean.stockservice.exception.member.DuplicateMemberException;
import com.gangbean.stockservice.exception.NotFoundMemberException;
import com.gangbean.stockservice.repository.UserRepository;
import com.gangbean.stockservice.util.SecurityUtil;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
            .authorityName(Role.ROLE_USER)
            .build();

        Member member = Member.builder()
            .username(userDto.getUsername())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .nickname(userDto.getNickname())
            .authorities(Collections.singleton(authority))
            .activated(true)
            .build();

        return UserDto.from(userRepository.save(member));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}
