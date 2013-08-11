package com.redhat.ceylon.compiler.typechecker.model;

/**
 * An unscientific categorization of the kinds of
 * declarations we have (for use in heuristic 
 * comparisons in the IDE). Note that this 
 * classification doesn't really relate to anything
 * in the language spec.
 * 
 * @author Gavin King
 */
public enum DeclarationKind {
    TYPE, TYPE_PARAMETER, MEMBER, SETTER
}
