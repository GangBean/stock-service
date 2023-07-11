package com.gangbean.stockservice.exception.member;

public class DuplicateMemberException extends MemberServiceException {
    public DuplicateMemberException(String message) {
        super(message);
    }
}