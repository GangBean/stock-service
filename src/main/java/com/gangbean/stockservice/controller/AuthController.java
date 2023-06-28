package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.dto.LoginDto;
import com.gangbean.stockservice.dto.TokenDto;
import com.gangbean.stockservice.entity.Member;
import com.gangbean.stockservice.entity.RefreshToken;
import com.gangbean.stockservice.jwt.JwtFilter;
import com.gangbean.stockservice.jwt.TokenProvider;
import com.gangbean.stockservice.repository.RefreshTokenRepository;
import com.gangbean.stockservice.service.MemberService;
import com.gangbean.stockservice.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    public static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
        MemberService memberService, RefreshTokenRepository refreshTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.memberService = memberService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/reissue")
    @Transactional
    public ResponseEntity<TokenDto> reissue(@Valid @RequestHeader HttpHeaders httpHeaders,
        @Valid @RequestBody TokenDto tokenDto) {
        LOGGER.info("HEADER: " + httpHeaders.toString());
        String accessToken = SecurityUtil.resolveToken(httpHeaders);
        LOGGER.info(accessToken);
        Claims claims = tokenProvider.claimsOf(accessToken);
        Long loginMemberId = claims.get(TokenProvider.MEMBER_ID, Long.class);

        Date now = new Date();
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByRefreshToken(
            tokenDto.getRefreshToken());

        if (refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh Token이 존재하지 않습니다.");
        }
        if (refreshToken.get().getExpiration().before(now)) {
            throw new IllegalArgumentException("이미 만료된 Refresh Token 입니다.");
        }
        if (refreshToken.get().getMember().getId() != loginMemberId) {
            throw new IllegalArgumentException("로그인한 유저의 Refresh Token이 아닙니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String reissuedAccessToken = tokenProvider.createAccessToken(authentication, tokenProvider.accessTokenExpiration(now), loginMemberId);
        refreshToken.get().reissue(tokenProvider.refreshTokenExpiration(now));
        refreshTokenRepository.save(refreshToken.get());

        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.add(SecurityUtil.AUTHORIZATION_HEADER, SecurityUtil.ACCESS_TOKEN_PREFIX + accessToken);
        newHeaders.add(SET_COOKIE_HEADER, String.valueOf(httpHeaders.get(SET_COOKIE_HEADER)));

        return new ResponseEntity<>(new TokenDto(reissuedAccessToken, tokenDto.getRefreshToken()), newHeaders, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        Authentication loginUserAuthentication = authenticationOf(loginDto);
        Member loginMember = memberService.getMemberByUsername(loginDto.getName());

        Date now = new Date();
        String accessToken = tokenProvider.createAccessToken(loginUserAuthentication, tokenProvider.accessTokenExpiration(now),
            loginMember.getId());

        Date refreshTokenExpiration = tokenProvider.refreshTokenExpiration(now);
        String refreshToken = tokenProvider.createRefreshToken(loginUserAuthentication, refreshTokenExpiration,
            loginMember.getId());

        refreshTokenRepository.save(RefreshToken.builder()
                .member(loginMember)
                .refreshToken(refreshToken)
                .expiration(refreshTokenExpiration)
            .build());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityUtil.AUTHORIZATION_HEADER,
            SecurityUtil.ACCESS_TOKEN_PREFIX + accessToken);
        httpHeaders.add(SET_COOKIE_HEADER,
            JwtFilter.REFRESH_TOKEN_PREFIX + refreshToken + JwtFilter.REFRESH_TOKEN_SUFFIX);

        return new ResponseEntity<>(new TokenDto(accessToken, refreshToken), httpHeaders, HttpStatus.OK);
    }

    private Authentication authenticationOf(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}