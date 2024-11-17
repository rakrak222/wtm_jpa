package org.wtm.web.auth.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String email) {
        super(String.format("%s은 회원가입 정보가 없는 이메일입니다.", email));
    }
}