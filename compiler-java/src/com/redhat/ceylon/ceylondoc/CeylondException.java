package com.redhat.ceylon.ceylondoc;


@SuppressWarnings("serial")
public class CeylondException extends RuntimeException {

    public CeylondException(String message, Exception cause) {
        super(message, cause);
    }

}
