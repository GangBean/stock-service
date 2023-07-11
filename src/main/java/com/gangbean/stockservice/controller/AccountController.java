package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.dto.AccountInfoResponse;
import com.gangbean.stockservice.dto.AccountOpenRequest;
import com.gangbean.stockservice.dto.BankInfoResponse;
import com.gangbean.stockservice.dto.ExceptionResponse;
import com.gangbean.stockservice.exception.account.AccountNotExistsException;
import com.gangbean.stockservice.exception.account.AccountNotOwnedByLoginUser;
import com.gangbean.stockservice.exception.account.AccountServiceException;
import com.gangbean.stockservice.service.AccountService;
import com.gangbean.stockservice.service.BankService;
import com.gangbean.stockservice.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    private final BankService bankService;

    private final MemberService memberService;

    public AccountController(AccountService accountService, BankService bankService, MemberService memberService) {
        this.accountService = accountService;
        this.bankService = bankService;
        this.memberService = memberService;
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> closeAccount(@PathVariable Long id, @AuthenticationPrincipal User loginUser) {
        Member member = memberService.memberOf(loginUser.getUsername()).asMember();
        accountService.close(id, member);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountInfoResponse> openAccount(@RequestBody AccountOpenRequest request
            , @AuthenticationPrincipal User loginUser) {
        BankInfoResponse bankInfoResponse = bankService.validateBank(request.bankName(), request.bankNumber());
        Member member = memberService.memberOf(loginUser.getUsername()).asMember();
        AccountInfoResponse response = accountService.responseOfAccountCreate(request, member, bankInfoResponse.asBank());
        return ResponseEntity.created(URI.create("/api/accounts/" + response.getId())).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountServiceException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountNotExistsException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountNotOwnedByLoginUser e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

}
