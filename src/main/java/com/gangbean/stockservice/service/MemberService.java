package com.gangbean.stockservice.service;

import com.gangbean.stockservice.dto.UserDto;
import com.gangbean.stockservice.entity.Authority;
import com.gangbean.stockservice.entity.Member;
import com.gangbean.stockservice.entity.Role;
import com.gangbean.stockservice.exception.DuplicateMemberException;
import com.gangbean.stockservice.exception.NotFoundMemberException;
import com.gangbean.stockservice.repository.MemberRepository;
import com.gangbean.stockservice.repository.RefreshTokenRepository;
import com.gangbean.stockservice.util.SecurityUtil;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
        RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (memberRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
            .authorityName(Role.ROLE_USER)
            .build();

        Member member = Member.builder()
            .username(userDto.getUsername())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .nickname(userDto.getNickname())
            .birthDay(userDto.getBirthday())
            .authorities(Collections.singleton(authority))
            .build();

        return UserDto.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(memberRepository.findOneWithAuthoritiesByUsername(username).orElse(
            Member.builder().build()));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
            SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByUsername)
                .orElseThrow(() -> new NotFoundMemberException("일치하는 회원 정보가 존재하지 않습니다: " + SecurityUtil.getCurrentUsername()))
        );
    }

    public Member memberWithANameOf(String userName) {
        return memberRepository.findByUsername(userName)
            .orElseThrow(() -> new NotFoundMemberException("이름에 해당하는 회원이 존재하지 않습니다: " + userName));
    }

    @Transactional
    public void withdraw(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundMemberException("존재하지 않는 회원 ID 입니다: " + memberId);
        }
        refreshTokenRepository.deleteAllByMemberId(memberId);
        memberRepository.deleteById(memberId);
    }
}
