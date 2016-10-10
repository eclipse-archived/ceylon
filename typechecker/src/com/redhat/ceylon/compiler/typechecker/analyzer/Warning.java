package com.redhat.ceylon.compiler.typechecker.analyzer;

public enum Warning {
    filenameNonAscii("File names are not ASCII"),
    filenameCaselessCollision("File names case collisions"),
    deprecation("Deprecations"),
    compilerAnnotation("Compiler annotations"),
    doclink("Broken documentation links"),
    expressionTypeNothing("Expressions of type 'Nothing'"),
    unusedDeclaration("Unused declarations"),
    unusedImport("Unused imports"),
    ceylonNamespace("Discouraged 'ceylon' namespaces"),
    javaNamespace("Discouraged 'java' namespaces"),
    suppressedAlready("Already suppressed warnings"),
    suppressesNothing("Warnings suppressing 'nothing'"),
    unknownWarning("Unknown warnings"),
    ambiguousAnnotation("Ambiguous annotations"),
    similarModule("Similar modules"),
    importsOtherJdk("Imports another JDK"),
    javaAnnotationElement("Java annotation elements"),
    syntaxDeprecation("Deprecated syntax"),
    smallIgnored("'small' is ignored"),
    literalNotSmall("Literal not 'small'"),
    disjointEquals("Disjoint 'equals'"),
    disjointContainment("Disjoint containments"),
    redundantNarrowing("Redundant narrowings"),
    redundantIteration("Redundant iterations"),
    missingImportPrefix("Missing import prefixes"),
    uncheckedTypeArguments("Unchecked type arguments"),
    expressionTypeCallable("Expressions of type 'Callable'"), 
    inferredNotNull("Inferred not null types");
    /*
     * Don't forget to update the documentation
     * of the suppressWarnings annotation in
     * language/src/ceylon/language/annotations.ceylon
     * when adding new warnings.
     */

    /** Used by the IDEs */
    private String description;
    Warning(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
