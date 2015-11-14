package com.redhat.ceylon.model.typechecker.model;

@SuppressWarnings("serial")
public class DecidabilityException
        extends RuntimeException {

    DecidabilityException() {
        super("possible undecidability: typechecker detected possible nontermination and aborted algorithm");
    }

}
