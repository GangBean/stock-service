package com.gangbean.stockservice.dto;

import com.gangbean.stockservice.entity.Account;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AccountInfoListResponse {

    private final List<AccountInfoResponse> list;

    public AccountInfoListResponse(List<AccountInfoResponse> list) {
        this.list = list;
    }

    public static AccountInfoListResponse responseOf(List<Account> list) {
        return new AccountInfoListResponse(list.stream()
                .map(AccountInfoResponse::responseOf)
                .collect(Collectors.toList()));
    }

    public List<AccountInfoResponse> accountInfoList() {
        return list;
    }

    @Override
    public String toString() {
        return "AccountInfoListResponse{" +
                "list=" + list +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInfoListResponse that = (AccountInfoListResponse) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}
