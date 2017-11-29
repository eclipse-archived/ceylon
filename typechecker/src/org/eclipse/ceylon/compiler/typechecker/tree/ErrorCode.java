/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.tree;

public enum ErrorCode {
    UNKNOWN_PARSER(-1),
    UNKNOWN(0);
    
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
