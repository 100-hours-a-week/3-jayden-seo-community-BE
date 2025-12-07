package com.kakao_tech.community.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {
    private final String field;

    public DuplicateException(String message, String field) {
        super(message);
        this.field = field;
    }
}
