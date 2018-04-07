/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

public interface LanguageModuleProvider {
    public static final String correspondenceName = "Correspondence";
    public static final String anythingName = "Anything";
    public static final String nullName = "Null";
    public static final String emptyName = "Empty";
    public static final String sequenceName = "Sequence";
    public static final String objectName = "Object";
    public static final String basicName = "Basic";
    public static final String identifiableName = "Identifiable";
    public static final String throwableName = "Throwable";
    public static final String exceptionName = "Exception";
    public static final String categoryName = "Category";
    public static final String iterableName = "Iterable";
    public static final String sequentialName = "Sequential";
    public static final String listName = "List";
    public static final String collectionName = "Collection";
    public static final String iteratorName = "Iterator";
    public static final String callableName = "Callable";
    public static final String scalableName = "Scalable";
    public static final String summableName = "Summable";
    public static final String multiplicableName = "Multiplicable";
    public static final String numericName = "Numeric";
    public static final String integralName = "Integral";
    public static final String invertibleName = "Invertible";
    public static final String exponentiableName = "Exponentiable";
    public static final String setName = "Set";
    public static final String mapName = "Map";
    public static final String comparisonName = "Comparison";
    public static final String booleanName = "Boolean";
    public static final String stringName = "String";
    public static final String floatName = "Float";
    public static final String integerName = "Integer";
    public static final String characterName = "Character";
    public static final String byteName = "Byte";
    public static final String comparableName = "Comparable";
    public static final String usableName = "Usable";
    public static final String destroyableName = "Destroyable";
    public static final String obtainableName = "Obtainable";
    public static final String ordinalName = "Ordinal";
    public static final String enumerableName = "Enumerable";
    public static final String rangeName = "Range";
    public static final String spanName = "Span";
    public static final String measureName = "Measure";
    public static final String tupleName = "Tuple";
    public static final String arrayName = "Array";
    public static final String rangedName = "Ranged";
    public static final String entryName = "Entry";
    public static final String nothingName = "Nothing";
    public static final String annotationName = "Annotation";
    public static final String constrainedAnnotationName = "ConstrainedAnnotation";
    public static final String sequencedAnnotationName = "SequencedAnnotation";
    public static final String optionalAnnotationName = "OptionalAnnotation";
    
    Interface getCorrespondenceDeclaration();
    Class getAnythingDeclaration();
    Class getNullDeclaration();
    Interface getEmptyDeclaration();
    Interface getSequenceDeclaration();
    Class getObjectDeclaration();
    Class getBasicDeclaration();
    Interface getIdentifiableDeclaration();
    Class getThrowableDeclaration();
    Class getExceptionDeclaration();
    Interface getCategoryDeclaration();
    Interface getIterableDeclaration();
    Interface getSequentialDeclaration();
    Interface getListDeclaration();
    Interface getCollectionDeclaration();
    Interface getIteratorDeclaration();
    Interface getCallableDeclaration();
    Interface getScalableDeclaration();
    Interface getSummableDeclaration();
    Interface getMultiplicableDeclaration();
    Interface getNumericDeclaration();
    Interface getIntegralDeclaration();
    Interface getInvertableDeclaration();
    Interface getExponentiableDeclaration();
    Interface getSetDeclaration();
    Interface getMapDeclaration();
    Class getComparisonDeclaration();
    Class getBooleanDeclaration();
    Class getStringDeclaration();
    Class getFloatDeclaration();
    Class getIntegerDeclaration();
    Class getCharacterDeclaration();
    Class getByteDeclaration();
    Interface getComparableDeclaration();
    Interface getUsableDeclaration();
    Interface getDestroyableDeclaration();
    Interface getObtainableDeclaration();
    Interface getOrdinalDeclaration();
    Interface getEnumerableDeclaration();
    Class getRangeDeclaration();
    Class getSpanDeclaration();
    Class getMeasureDeclaration();
    Class getTupleDeclaration();
    TypeDeclaration getArrayDeclaration();
    Interface getRangedDeclaration();
    Class getEntryDeclaration();
    NothingType getNothingDeclaration();
    TypeDeclaration getAnnotationDeclaration();
    TypeDeclaration getConstrainedAnnotationDeclaration();
    TypeDeclaration getSequencedAnnotationDeclaration();
    TypeDeclaration getOptionalAnnotationDeclaration();
    
    Type getNothingType();
    Type getEmptyType();
    Type getAnythingType();
    Type getObjectType();
    Type getIdentifiableType();
    Type getBasicType();
    Type getNullType();
    Type getThrowableType();
    Type getExceptionType();
    Type getBooleanType();
    Type getStringType();
    Type getIntegerType();
    Type getFloatType();
    Type getCharacterType();
    Type getByteType();
    Type getComparisonType();
    Type getDestroyableType();
    Type getObtainableType();
    Type getUnknownType();
}
