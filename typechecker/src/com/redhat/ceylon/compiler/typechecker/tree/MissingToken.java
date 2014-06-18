package com.redhat.ceylon.compiler.typechecker.tree;

import org.antlr.runtime.CommonToken;

public class MissingToken extends CommonToken {

    private static final long serialVersionUID = -4523870670663136503L;

    public MissingToken(int type, String text) {
        super(type, text);
    }

}
