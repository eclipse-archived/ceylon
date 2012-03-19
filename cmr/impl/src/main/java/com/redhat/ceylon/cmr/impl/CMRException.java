package com.redhat.ceylon.cmr.impl;


@SuppressWarnings("serial")
public class CMRException extends RuntimeException {

    public CMRException(String message) {
        super(message);
    }

    public CMRException(Exception x) {
        super(x);
    }

}
