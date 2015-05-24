package com.redhat.ceylon.model.typechecker.model;

public class DecidabilityException
        extends RuntimeException {

    DecidabilityException() {
        super("possible undecidability: typechecker detected possible nontermination and aborted algorithm");
    }

}
