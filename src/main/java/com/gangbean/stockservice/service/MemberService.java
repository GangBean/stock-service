package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Authority;
import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.domain.Role;
import com.gangbean.stockservice.dto.MemberDto;
import com.gangbean.stockservice.exception.NotFoundMemberException;
import com.gangbean.stockservice.exception.member.DuplicateMemberException;
import com.gangbean.stockservice.exception.member.MemberNotFoundException;
import com.gangbean.stockservice.repository.MemberRepository;
import com.gangbean.stockservice.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberDto signup(MemberDto memberDto) {
        if (memberRepository.findOneWithAuthoritiesByUsername(memberDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
            .authorityName(Role.ROLE_USER)
            .build();

        Member member = Member.builder()
            .username(memberDto.getUsername())
            .password(passwordEncoder.encode(memberDto.getPassword()))
            .nickname(memberDto.getNickname())
            .authorities(Collections.singleton(authority))
            .activated(true)
            .build();

        return MemberDto.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberDto getUserWithAuthorities(String username) {
        return MemberDto.from(memberRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDto getMyUserWithAuthorities() {
        return MemberDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    public MemberDto memberOf(String username) {
        return MemberDto.from(memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("ID 혹은 패스워드가 잘못되었습니다.")));
    }
}
