package com.gangbean.stockservice.controller;

import com.gangbean.stockservice.domain.Member;
import com.gangbean.stockservice.dto.ExceptionResponse;
import com.gangbean.stockservice.dto.StockBuyRequest;
import com.gangbean.stockservice.dto.StockBuyResponse;
import com.gangbean.stockservice.exception.StockNotEnoughBalanceException;
import com.gangbean.stockservice.exception.StockNotFoundException;
import com.gangbean.stockservice.exception.account.AccountNotEnoughBalanceException;
import com.gangbean.stockservice.exception.account.AccountNotExistsException;
import com.gangbean.stockservice.exception.account.AccountNotOwnedByLoginUser;
import com.gangbean.stockservice.exception.account.AccountServiceException;
import com.gangbean.stockservice.exception.stock.StockSellForBelowCurrentPriceException;
import com.gangbean.stockservice.service.AccountStockTradeService;
import com.gangbean.stockservice.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/api")
public class StockController {

    private final MemberService memberService;
    private final AccountStockTradeService accountStockTradeService;

    public StockController(MemberService memberService, AccountStockTradeService accountStockTradeService) {
        this.memberService = memberService;
        this.accountStockTradeService = accountStockTradeService;
    }

    @PostMapping("/accounts/{accountId}/stocks/{stockId}")
    public ResponseEntity<StockBuyResponse> buyStock(@PathVariable Long accountId, @PathVariable Long stockId
            , @AuthenticationPrincipal User loginUser, @RequestBody StockBuyRequest request) {
        Member member = memberService.memberOf(loginUser.getUsername()).asMember();
        StockBuyResponse response = accountStockTradeService.responseOfBuy(member, accountId
                , stockId, request.getAmount(), request.getPrice(), LocalDateTime.now());
        return ResponseEntity.created(URI.create(String.format("/api/accounts/%d/stocks/%d", accountId, stockId))).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountNotExistsException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(StockNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountNotOwnedByLoginUser e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(StockNotEnoughBalanceException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountNotEnoughBalanceException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(StockSellForBelowCurrentPriceException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(AccountServiceException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
