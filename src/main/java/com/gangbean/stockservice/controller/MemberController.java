package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.dto.ExceptionResponse;
import com.gangbean.stockservice.dto.SignupRequest;
import com.gangbean.stockservice.dto.SignupResponse;
import com.gangbean.stockservice.exception.member.DuplicateMemberException;
import com.gangbean.stockservice.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
    public ResponseEntity<SignupResponse> signup(
        @Valid @RequestBody SignupRequest signupRequest
    ) {
        return ResponseEntity.ok(memberService.signup(signupRequest));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<SignupResponse> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<SignupResponse> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(username));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(DuplicateMemberException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.CONFLICT);
    }
}