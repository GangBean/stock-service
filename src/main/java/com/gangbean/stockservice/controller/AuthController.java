package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.dto.LoginDto;
import com.gangbean.stockservice.dto.TokenDto;
import com.gangbean.stockservice.entity.Member;
import com.gangbean.stockservice.jwt.JwtFilter;
import com.gangbean.stockservice.service.MemberService;
import com.gangbean.stockservice.service.RefreshTokenService;
import com.gangbean.stockservice.util.SecurityUtil;
import java.util.Date;
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

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    public AuthController (
        AuthenticationManagerBuilder authenticationManagerBuilder,
        MemberService memberService,
        RefreshTokenService refreshTokenService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.memberService = memberService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/reissue")
    @Transactional
    public ResponseEntity<TokenDto> reissue (@Valid @RequestHeader HttpHeaders httpHeaders,
        @Valid @RequestBody TokenDto tokenDto) {
        String accessToken = SecurityUtil.resolveToken(httpHeaders);

        TokenDto reissuedTokenDto = reissuedTokenWithInfoOf(accessToken, new Date(), tokenDto);

        HttpHeaders responseHeaders = responseHeadersWithTokenValuesOf(
            SecurityUtil.ACCESS_TOKEN_PREFIX + reissuedTokenDto.getAccessToken(),
            JwtFilter.REFRESH_TOKEN_PREFIX + reissuedTokenDto.getRefreshToken() + JwtFilter.REFRESH_TOKEN_SUFFIX);

        return new ResponseEntity<>(reissuedTokenDto, responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login (@Valid @RequestBody LoginDto loginDto) {
        Authentication loginUserAuthentication = authenticationGeneratedByAuthenticationManagerWithUserOf(loginDto);
        Member requestMember = memberService.memberWithANameOf(loginDto.getName());

        TokenDto loginMemberToken = refreshTokenService.tokenGeneratedFromLoginResultOf(loginUserAuthentication, requestMember);

        HttpHeaders httpHeaders = responseHeadersWithTokenValuesOf(
            SecurityUtil.ACCESS_TOKEN_PREFIX + loginMemberToken.getAccessToken(),
            JwtFilter.REFRESH_TOKEN_PREFIX + loginMemberToken.getRefreshToken() + JwtFilter.REFRESH_TOKEN_SUFFIX);

        return new ResponseEntity<>(loginMemberToken, httpHeaders, HttpStatus.OK);
    }

    private Authentication authenticationGeneratedByAuthenticationManagerWithUserOf (LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword());

        Authentication authentication = authenticationGeneratedByAuthenticationManagerWithTokenOf(usernamePasswordAuthenticationToken);

        saveAuthenticationOnSecurityContextHolder(authentication);
        return authentication;
    }

    private Authentication authenticationGeneratedByAuthenticationManagerWithTokenOf (
        UsernamePasswordAuthenticationToken authenticationToken) {
        return authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);
    }

    private void saveAuthenticationOnSecurityContextHolder (Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private HttpHeaders responseHeadersWithTokenValuesOf (String accessToken, String setCookieHeader) {
        HttpHeaders newHeaders = new HttpHeaders();

        newHeaders.add(SecurityUtil.AUTHORIZATION_HEADER, accessToken);
        newHeaders.add(SET_COOKIE_HEADER, setCookieHeader);

        return newHeaders;
    }

    private TokenDto reissuedTokenWithInfoOf (String accessToken, Date now, TokenDto tokenDto) {
        return refreshTokenService.reissue(accessToken, now, tokenDto);
    }
}