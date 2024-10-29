package org.wtm.web.user.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("올바른 비밀번호가 아닙니다.");
    }
}