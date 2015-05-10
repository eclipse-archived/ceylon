package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

/**
 * Represents a declaration that can be overloaded where
 * several siblings with the same name might exist only
 * differing in their signature (think Java interop)
 * or in their backend implementation (think "native").
 */
public interface Overloadable {

    public abstract boolean isAbstraction();

    public abstract boolean isOverloaded();

    public abstract List<Declaration> getOverloads();

}