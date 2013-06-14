package com.redhat.ceylon.compiler;

public class SystemErrorException extends RuntimeException {

    SystemErrorException(String msg) {
        super(msg);
    }
}
