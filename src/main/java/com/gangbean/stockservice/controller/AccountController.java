package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.dto.AccountInfoResponse;
import com.gangbean.stockservice.dto.AccountOpenRequest;
import com.gangbean.stockservice.dto.BankInfoResponse;
import com.gangbean.stockservice.dto.ExceptionResponse;
import com.gangbean.stockservice.exception.account.AccountServiceException;
import com.gangbean.stockservice.service.AccountService;
import com.gangbean.stockservice.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    private final BankService bankService;

    public AccountController(AccountService accountService, BankService bankService) {
        this.accountService = accountService;
        this.bankService = bankService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountInfoResponse> openAccount(@RequestBody AccountOpenRequest request
            , @AuthenticationPrincipal Member member) {
        BankInfoResponse bankInfoResponse = bankService.validateBank(request.bankName(), request.bankNumber());
        AccountInfoResponse response = accountService.responseOfAccountCreate(request, member, bankInfoResponse.asBank());
        return ResponseEntity.created(URI.create("/api/accounts/" + response.getId())).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountServiceException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
