package com.redhat.ceylon.compiler.typechecker.tree;

public enum ErrorCode {
    UNKNOWN_PARSER(-1),
    UNKNOWN(0),
    SERVICE_CLASS_CANNOT_BE_GENERIC(1603),
    SERVICE_CLASS_ERROR(1604)
    ;
    
    public final int code;
    
    private ErrorCode(int code){
        this.code = code;
    }
    
    public static ErrorCode getErrorCode(int code){
        for(ErrorCode errorCode : values()){
            if(errorCode.code == code)
                return errorCode;
        }
        return null;
    }
}
