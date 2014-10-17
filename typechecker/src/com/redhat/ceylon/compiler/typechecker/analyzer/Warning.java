package com.redhat.ceylon.compiler.typechecker.analyzer;

public enum Warning {
    filenameNonAscii,
    filenameClaselessCollision,
    deprecation,
    compilerAnnotation,
    doclink,
    expressionTypeNothing,
    unusedDeclaration,
    unusedImport,
    ceylonNamespace,
    javaNamespace, 
    suppressedAlready, 
    suppressesNothing;
}