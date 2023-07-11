package com.gangbean.stockservice.exception.member;

public class MemberNotFoundException extends MemberServiceException {
    public MemberNotFoundException(String message) {
        super(message);
    }
}
