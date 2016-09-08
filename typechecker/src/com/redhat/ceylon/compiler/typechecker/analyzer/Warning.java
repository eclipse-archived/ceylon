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
    javaAnnotationElement,
    syntaxDeprecation,
    smallIgnored,
    literalNotSmall, 
    disjointEquals,
    disjointContainment,
    redundantNarrowing, 
    redundantIteration,
    missingImportPrefix,
    uncheckedTypeArguments, 
    expressionTypeCallable
    /*
     * Don't forget to update the documentation
     * of the suppressWarnings annotation in
     * language/src/ceylon/language/annotations.ceylon
     * when adding new warnings.
     */
}
