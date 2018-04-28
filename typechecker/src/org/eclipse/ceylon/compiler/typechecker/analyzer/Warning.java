/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

public enum Warning {
    filenameNonAscii("Non-ASCII filenames"),
    filenameCaselessCollision("Filename case collisions"),
    deprecation("Deprecation"),
    compilerAnnotation("Compiler annotations"),
    doclink("Broken documentation links"),
    expressionTypeNothing("Expressions of type 'Nothing'"),
    unusedDeclaration("Unused declarations"),
    unusedImport("Unused imports"),
    redundantImportAlias("Redundant import aliases"),
    ceylonNamespace("Discouraged 'ceylon' namespaces"),
    javaNamespace("Discouraged 'java' namespaces"),
    packageName("Discouraged package or module name"),
    suppressedAlready("Redundant warning supression"),
    suppressesNothing("Unused warning suppression"),
    unknownWarning("Unknown warnings"),
    ambiguousAnnotation("Ambiguous annotations"),
    lateFieldAnnotation("Late field annotations"),
    similarModule("Similar modules"),
    importsOtherJdk("JDK version import mismatches"),
    javaAnnotationElement("Java annotation elements"),
    syntaxDeprecation("Syntax deprecation"),
    smallIgnored("Ignored 'small' annotations"),
    literalNotSmall("Non-'small' literal assignments"),
    disjointEquals("Disjoint 'equals' operands"),
    disjointContainment("Disjoint 'in' operands"),
    valueEqualityIgnored("Value equality ignored"),
    redundantNarrowing("Redundant narrowing"),
    redundantIteration("Redundant iteration"),
    missingImportPrefix("Missing import prefixes"),
    uncheckedTypeArguments("Unchecked type arguments"),
    expressionTypeCallable("Expressions of type 'Callable'"),
    expressionTypeIterable("Expressions of type 'Iterable'"), 
    inferredNotNull("Inferred not null types"),
    zeroFloatLiteral("Literal so small it is indistinguishable from zero"), 
    hidesLanguageModifier("Import hides a language modifier"), 
    implicitNarrowing("Implicit narrowing to covering type"), 
    catchType("Discouraged catch type"), 
    directiveInFinally("Control directives in 'finally'");
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
