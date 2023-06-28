package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.dto.UserDto;
import com.gangbean.stockservice.jwt.TokenProvider;
import com.gangbean.stockservice.service.MemberService;
import com.gangbean.stockservice.util.SecurityUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public MemberController(MemberService memberService, TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(
        @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(memberService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(username));
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity withdraw(@Valid @RequestHeader HttpHeaders headers) {
        Long loginMemberId = tokenProvider.claimsOf(SecurityUtil.resolveToken(headers)).get(TokenProvider.MEMBER_ID, Long.class);
        memberService.withdraw(loginMemberId);
        return ResponseEntity.ok().build();
    }
}