package com.gangbean.stockservice.exception.member;

public class DuplicateMemberException extends MemberException {
    public DuplicateMemberException(String message) {
        super(message);
    }
}