package com.gangbean.stockservice.service;

import com.gangbean.stockservice.domain.Account;
import com.gangbean.stockservice.domain.TradeReservation;
import com.gangbean.stockservice.dto.PaymentReservationResponse;
import com.gangbean.stockservice.exception.AccountNotExistsException;
import com.gangbean.stockservice.repository.AccountRepository;
import com.gangbean.stockservice.repository.TradeReservationRepository;

import java.time.LocalDateTime;

public class TradeReservationService {

    private final TradeReservationRepository tradeReservationRepository;

    private final AccountRepository accountRepository;

    public TradeReservationService(TradeReservationRepository tradeReservationRepository, AccountRepository accountRepository) {
        this.tradeReservationRepository = tradeReservationRepository;
        this.accountRepository = accountRepository;
    }

    public PaymentReservationResponse responseOfPaymentReservation(Long accountId, LocalDateTime tradeAt, Long amount) {
        Account fromAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotExistsException("입력된 ID에 해당하는 계좌가 존재하지 않습니다: " + accountId));

        TradeReservation tradeReservation = tradeReservationRepository.save(new TradeReservation(fromAccount, tradeAt, amount));
        return PaymentReservationResponse.responseOf(tradeReservation);
    }
}