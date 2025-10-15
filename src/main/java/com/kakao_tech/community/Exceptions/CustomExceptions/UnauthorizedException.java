package com.kakao_tech.community.Exceptions.CustomExceptions;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
