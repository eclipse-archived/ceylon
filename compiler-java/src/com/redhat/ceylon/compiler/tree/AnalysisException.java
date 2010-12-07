package com.redhat.ceylon.compiler.tree;

public class AnalysisException extends Exception {
    private String message;

    AnalysisException(String message) {
        this.message = message;
    }

    public String getMsg() {
        return message;
    }
}
