package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Authority;
import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.domain.Role;
import com.gangbean.stockservice.dto.SignupRequest;
import com.gangbean.stockservice.dto.SignupResponse;
import com.gangbean.stockservice.exception.NotFoundMemberException;
import com.gangbean.stockservice.exception.member.DuplicateMemberException;
import com.gangbean.stockservice.exception.member.MemberNotFoundException;
import com.gangbean.stockservice.repository.MemberRepository;
import com.gangbean.stockservice.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SignupResponse signup(SignupRequest signupResponse) {
        if (memberRepository.findOneWithAuthoritiesByUsername(signupResponse.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
            .authorityName(Role.ROLE_USER)
            .build();

        Member member = Member.builder()
            .username(signupResponse.getUsername())
            .password(passwordEncoder.encode(signupResponse.getPassword()))
            .nickname(signupResponse.getNickname())
            .authorities(Collections.singleton(authority))
            .activated(true)
            .build();

        return SignupResponse.from(memberRepository.save(member));
    }

    public SignupResponse getUserWithAuthorities(String username) {
        return SignupResponse.from(memberRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    public SignupResponse getMyUserWithAuthorities() {
        return SignupResponse.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    public SignupResponse memberOf(String username) {
        return SignupResponse.from(memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("ID 혹은 패스워드가 잘못되었습니다.")));
    }
}
