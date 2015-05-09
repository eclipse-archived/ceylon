package com.redhat.ceylon.compiler.typechecker.analyzer;

public enum Warning {
    filenameNonAscii,
    filenameCaselessCollision,
    deprecation,
    compilerAnnotation,
    doclink,
    expressionTypeNothing,
    unusedDeclaration,
    unusedImport,
    ceylonNamespace,
    javaNamespace, 
    suppressedAlready, 
    suppressesNothing, 
    unknownWarning, 
    ambiguousAnnotation,
    similarModule,
    importsOtherJdk,
    javaAnnotationElement
}