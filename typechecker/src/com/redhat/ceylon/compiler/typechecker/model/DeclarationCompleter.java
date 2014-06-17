package com.redhat.ceylon.compiler.typechecker.model;

/**
 * Completer for declarations that will only complete certain fields which
 * are expensive to compute, on demand.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface DeclarationCompleter {

    /**
     * Completes the <tt>actual</tt> and <tt>refinedDeclaration</tt> members of the
     * given declaration.
     */
    public void completeActual(Declaration decl);
}
