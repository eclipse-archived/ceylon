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

import static org.eclipse.ceylon.model.typechecker.model.Module.LANGUAGE_MODULE_NAME;

public class LanguageModuleCache  implements LanguageModuleProvider {
    
    private static final class CacheTypeFactory extends Unit {
        public Type getType(TypeDeclaration td) {
            Type result = super.getType(td);
            result.setCached();
            return result;
        }
    }

    private final Module module;

    LanguageModuleCache(Module module) {
        this.module = module;
        assert(module == module.getLanguageModule());
    }

    private Unit typeFactory = null;

    private Interface correspondenceDeclaration = null;
    private Class anythingDeclaration = null;
    private Class nullDeclaration = null;
    private Interface emptyDeclaration = null;
    private Interface sequenceDeclaration = null;
    private Class objectDeclaration = null;
    private Class basicDeclaration = null;
    private Interface identifiableDeclaration = null;
    private Class throwableDeclaration = null;
    private Class exceptionDeclaration = null;
    private Interface categoryDeclaration = null;
    private Interface iterableDeclaration = null;
    private Interface sequentialDeclaration = null;
    private Interface listDeclaration = null;
    private Interface collectionDeclaration = null;
    private Interface iteratorDeclaration = null;
    private Interface callableDeclaration = null;
    private Interface scalableDeclaration = null;
    private Interface summableDeclaration = null;
    private Interface multiplicableDeclaration = null;
    private Interface numericDeclaration = null;
    private Interface integralDeclaration = null;
    private Interface invertibleDeclaration = null;
    private Interface exponentiableDeclaration = null;
    private Interface setDeclaration = null;
    private Interface mapDeclaration = null;
    private Class comparisonDeclaration = null;
    private Class booleanDeclaration = null;
    private Interface binaryDeclaration = null;
    private Class stringDeclaration = null;
    private Class floatDeclaration = null;
    private Class integerDeclaration = null;
    private Class characterDeclaration = null;
    private Class byteDeclaration = null;
    private Interface comparableDeclaration = null;
    private Interface usableDeclaration = null;
    private Interface destroyableDeclaration = null;
    private Interface obtainableDeclaration = null;
    private Interface ordinalDeclaration = null;
    private Interface enumerableDeclaration = null;
    private Class rangeDeclaration = null;
    private Class spanDeclaration = null;
    private Class measureDeclaration = null;
    private Class tupleDeclaration = null;
    private TypeDeclaration arrayDeclaration = null;
    private Interface rangedDeclaration = null;
    private Class entryDeclaration = null;
    private NothingType nothingDeclaration = null;
    private UnknownType unknownDeclaration = null;
    private TypeDeclaration annotationDeclaration = null;
    private TypeDeclaration constrainedAnnotationDeclaration = null;
    private TypeDeclaration sequencedAnnotationDeclaration = null;
    private TypeDeclaration optionalAnnotationDeclaration = null;
    
    private Type unknownType = null;
    private Type nothingType = null;
    private Type emptyType = null;
    private Type anythingType = null;
    private Type objectType = null;
    private Type identifiableType = null;
    private Type basicType = null;
    private Type nullType = null;
    private Type throwableType = null;
    private Type exceptionType = null;
    private Type booleanType = null;
    private Type stringType = null;
    private Type integerType = null;
    private Type floatType = null;
    private Type characterType = null;
    private Type byteType = null;
    private Type comparisonType = null;
    private Type destroyableType = null;
    private Type obtainableType = null;

    private Package languagePackage = null;
    
    public void reset() {
        typeFactory = null;

        correspondenceDeclaration = null;
        anythingDeclaration = null;
        nullDeclaration = null;
        emptyDeclaration = null;
        sequenceDeclaration = null;
        objectDeclaration = null;
        basicDeclaration = null;
        identifiableDeclaration = null;
        throwableDeclaration = null;
        exceptionDeclaration = null;
        categoryDeclaration = null;
        iterableDeclaration = null;
        sequentialDeclaration = null;
        listDeclaration = null;
        collectionDeclaration = null;
        iteratorDeclaration = null;
        callableDeclaration = null;
        scalableDeclaration = null;
        summableDeclaration = null;
        numericDeclaration = null;
        integralDeclaration = null;
        invertibleDeclaration = null;
        exponentiableDeclaration = null;
        setDeclaration = null;
        mapDeclaration = null;
        comparisonDeclaration = null;
        booleanDeclaration = null;
        stringDeclaration = null;
        floatDeclaration = null;
        integerDeclaration = null;
        characterDeclaration = null;
        byteDeclaration = null;
        comparableDeclaration = null;
        usableDeclaration = null;
        destroyableDeclaration = null;
        obtainableDeclaration = null;
        ordinalDeclaration = null;
        enumerableDeclaration = null;
        rangeDeclaration = null;
        spanDeclaration = null;
        measureDeclaration = null;
        tupleDeclaration = null;
        arrayDeclaration = null;
        rangedDeclaration = null;
        entryDeclaration = null;
        nothingDeclaration = null;
        annotationDeclaration = null;
        constrainedAnnotationDeclaration = null;
        sequencedAnnotationDeclaration = null;
        optionalAnnotationDeclaration = null;
        
        nothingType = null;
        emptyType = null;
        anythingType = null;
        objectType = null;
        identifiableType = null;
        basicType = null;
        nullType = null;
        throwableType = null;
        exceptionType = null;
        booleanType = null;
        stringType = null;
        integerType = null;
        floatType = null;
        characterType = null;
        byteType = null;
        comparisonType = null;
        destroyableType = null;
        obtainableType = null;

        languagePackage = null;
    }
    
    public synchronized Unit getTypeFactory() {
        if (typeFactory == null) {
            if (module.isAvailable()) {
                Package languagePackage =
                        module.getPackage(Module.LANGUAGE_MODULE_NAME);
                if (languagePackage != null) {
                    typeFactory = new CacheTypeFactory();
                    typeFactory.setPackage(languagePackage);
                }
            }
        }
        return typeFactory;
    }
    
    private Declaration getLanguageModuleDeclaration(String name) {
        if (module.isAvailable()) {
            if (languagePackage==null) {
                languagePackage = 
                        module.getPackage(LANGUAGE_MODULE_NAME);
            }
            if (languagePackage != null) {
                Declaration d = 
                        languagePackage.getMember(name, 
                                null, false);
                if (d != null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }

    public UnknownType getUnknownDeclaration() {
        if (unknownDeclaration == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                unknownDeclaration = new UnknownType(theTypeFactory);
            }
        }
        return unknownDeclaration;
    }

    public NothingType getNothingDeclaration() {
        if (nothingDeclaration == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                nothingDeclaration = new NothingType(theTypeFactory);
            }
        }
        return nothingDeclaration;
    }

    public Class getObjectDeclaration() {
        if (objectDeclaration == null) {
            objectDeclaration = (Class) getLanguageModuleDeclaration(objectName);
        }
        return objectDeclaration;
    }

    public Class getNullDeclaration() {
        if (nullDeclaration == null) {
            nullDeclaration = (Class) getLanguageModuleDeclaration(nullName);
        }
        return nullDeclaration;
    }

    public Class getAnythingDeclaration() {
        if (anythingDeclaration == null) {
            anythingDeclaration = (Class) getLanguageModuleDeclaration(anythingName);
        }
        return anythingDeclaration;
    }

    @Override
    public Interface getCorrespondenceDeclaration() {
        if (correspondenceDeclaration == null) {
            correspondenceDeclaration = (Interface) getLanguageModuleDeclaration(correspondenceName);
        }
        return correspondenceDeclaration;
    }

    @Override
    public Interface getEmptyDeclaration() {
        if (emptyDeclaration == null) {
            emptyDeclaration = (Interface) getLanguageModuleDeclaration(emptyName);
        }
        return emptyDeclaration;
    }

    @Override
    public Interface getSequenceDeclaration() {
        if (sequenceDeclaration == null) {
            sequenceDeclaration = (Interface) getLanguageModuleDeclaration(sequenceName);
        }
        return sequenceDeclaration;
    }

    @Override
    public Class getBasicDeclaration() {
        if (basicDeclaration == null) {
            basicDeclaration = (Class) getLanguageModuleDeclaration(basicName);
        }
        return basicDeclaration;
    }

    @Override
    public Interface getIdentifiableDeclaration() {
        if (identifiableDeclaration == null) {
            identifiableDeclaration = (Interface) getLanguageModuleDeclaration(identifiableName);
        }
        return identifiableDeclaration;
    }

    @Override
    public Class getThrowableDeclaration() {
        if (throwableDeclaration == null) {
            throwableDeclaration = (Class) getLanguageModuleDeclaration(throwableName);
        }
        return throwableDeclaration;
    }

    @Override
    public Class getExceptionDeclaration() {
        if (exceptionDeclaration == null) {
            exceptionDeclaration = (Class) getLanguageModuleDeclaration(exceptionName);
        }
        return exceptionDeclaration;
    }

    @Override
    public Interface getCategoryDeclaration() {
        if (categoryDeclaration == null) {
            categoryDeclaration = (Interface) getLanguageModuleDeclaration(categoryName);
        }
        return categoryDeclaration;
    }

    @Override
    public Interface getIterableDeclaration() {
        if (iterableDeclaration == null) {
            iterableDeclaration = (Interface) getLanguageModuleDeclaration(iterableName);
        }
        return iterableDeclaration;
    }

    @Override
    public Interface getSequentialDeclaration() {
        if (sequentialDeclaration == null) {
            sequentialDeclaration = (Interface) getLanguageModuleDeclaration(sequentialName);
        }
        return sequentialDeclaration;
    }

    @Override
    public Interface getListDeclaration() {
        if (listDeclaration == null) {
            listDeclaration = (Interface) getLanguageModuleDeclaration(listName);
        }
        return listDeclaration;
    }

    @Override
    public Interface getCollectionDeclaration() {
        if (collectionDeclaration == null) {
            collectionDeclaration = (Interface) getLanguageModuleDeclaration(collectionName);
        }
        return collectionDeclaration;
    }

    @Override
    public Interface getIteratorDeclaration() {
        if (iteratorDeclaration == null) {
            iteratorDeclaration = (Interface) getLanguageModuleDeclaration(iteratorName);
        }
        return iteratorDeclaration;
    }

    @Override
    public Interface getCallableDeclaration() {
        if (callableDeclaration == null) {
            callableDeclaration = (Interface) getLanguageModuleDeclaration(callableName);
        }
        return callableDeclaration;
    }

    @Override
    public Interface getScalableDeclaration() {
        if (scalableDeclaration == null) {
            scalableDeclaration = (Interface) getLanguageModuleDeclaration(scalableName);
        }
        return scalableDeclaration;
    }

    @Override
    public Interface getSummableDeclaration() {
        if (summableDeclaration == null) {
            summableDeclaration = (Interface) getLanguageModuleDeclaration(summableName);
        }
        return summableDeclaration;
    }

    @Override
    public Interface getMultiplicableDeclaration() {
        if (multiplicableDeclaration == null) {
            multiplicableDeclaration = (Interface) getLanguageModuleDeclaration(multiplicableName);
        }
        return multiplicableDeclaration;
    }

    @Override
    public Interface getNumericDeclaration() {
        if (numericDeclaration == null) {
            numericDeclaration = (Interface) getLanguageModuleDeclaration(numericName);
        }
        return numericDeclaration;
    }

    @Override
    public Interface getIntegralDeclaration() {
        if (integralDeclaration == null) {
            integralDeclaration = (Interface) getLanguageModuleDeclaration(integralName);
        }
        return integralDeclaration;
    }

    @Override
    public Interface getInvertableDeclaration() {
        if (invertibleDeclaration == null) {
            invertibleDeclaration = (Interface) getLanguageModuleDeclaration(invertibleName);
        }
        return invertibleDeclaration;
    }

    @Override
    public Interface getExponentiableDeclaration() {
        if (exponentiableDeclaration == null) {
            exponentiableDeclaration = (Interface) getLanguageModuleDeclaration(exponentiableName);
        }
        return exponentiableDeclaration;
    }

    @Override
    public Interface getSetDeclaration() {
        if (setDeclaration == null) {
            setDeclaration = (Interface) getLanguageModuleDeclaration(setName);
        }
        return setDeclaration;
    }

    @Override
    public Interface getMapDeclaration() {
        if (mapDeclaration == null) {
            mapDeclaration = (Interface) getLanguageModuleDeclaration(mapName);
        }
        return mapDeclaration;
    }

    @Override
    public Class getComparisonDeclaration() {
        if (comparisonDeclaration == null) {
            comparisonDeclaration = (Class) getLanguageModuleDeclaration(comparisonName);
        }
        return comparisonDeclaration;
    }

    @Override
    public Class getBooleanDeclaration() {
        if (booleanDeclaration == null) {
            booleanDeclaration = (Class) getLanguageModuleDeclaration(booleanName);
        }
        return booleanDeclaration;
    }

    @Override
    public Interface getBinaryDeclaration() {
        if (binaryDeclaration == null) {
            binaryDeclaration = (Interface) getLanguageModuleDeclaration(binaryName);
        }
        return binaryDeclaration;
    }

    @Override
    public Class getStringDeclaration() {
        if (stringDeclaration == null) {
            stringDeclaration = (Class) getLanguageModuleDeclaration(stringName);
        }
        return stringDeclaration;
    }

    @Override
    public Class getFloatDeclaration() {
        if (floatDeclaration == null) {
            floatDeclaration = (Class) getLanguageModuleDeclaration(floatName);
        }
        return floatDeclaration;
    }

    @Override
    public Class getIntegerDeclaration() {
        if (integerDeclaration == null) {
            integerDeclaration = (Class) getLanguageModuleDeclaration(integerName);
        }
        return integerDeclaration;
    }

    @Override
    public Class getCharacterDeclaration() {
        if (characterDeclaration == null) {
            characterDeclaration = (Class) getLanguageModuleDeclaration(characterName);
        }
        return characterDeclaration;
    }

    @Override
    public Class getByteDeclaration() {
        if (byteDeclaration == null) {
            byteDeclaration = (Class) getLanguageModuleDeclaration(byteName);
        }
        return byteDeclaration;
    }

    @Override
    public Interface getComparableDeclaration() {
        if (comparableDeclaration == null) {
            comparableDeclaration = (Interface) getLanguageModuleDeclaration(comparableName);
        }
        return comparableDeclaration;
    }

    @Override
    public Interface getUsableDeclaration() {
        if (usableDeclaration == null) {
            usableDeclaration = (Interface) getLanguageModuleDeclaration(usableName);
        }
        return usableDeclaration;
    }

    @Override
    public Interface getDestroyableDeclaration() {
        if (destroyableDeclaration == null) {
            destroyableDeclaration = (Interface) getLanguageModuleDeclaration(destroyableName);
        }
        return destroyableDeclaration;
    }

    @Override
    public Interface getObtainableDeclaration() {
        if (obtainableDeclaration == null) {
            obtainableDeclaration = (Interface) getLanguageModuleDeclaration(obtainableName);
        }
        return obtainableDeclaration;
    }

    @Override
    public Interface getOrdinalDeclaration() {
        if (ordinalDeclaration == null) {
            ordinalDeclaration = (Interface) getLanguageModuleDeclaration(ordinalName);
        }
        return ordinalDeclaration;
    }

    @Override
    public Interface getEnumerableDeclaration() {
        if (enumerableDeclaration == null) {
            enumerableDeclaration = (Interface) getLanguageModuleDeclaration(enumerableName);
        }
        return enumerableDeclaration;
    }

    @Override
    public Class getRangeDeclaration() {
        if (rangeDeclaration == null) {
            rangeDeclaration = (Class) getLanguageModuleDeclaration(rangeName);
        }
        return rangeDeclaration;
    }

    @Override
    public Class getSpanDeclaration() {
        if (spanDeclaration == null) {
            spanDeclaration = (Class) getLanguageModuleDeclaration(spanName);
        }
        return spanDeclaration;
    }

    @Override
    public Class getMeasureDeclaration() {
        if (measureDeclaration == null) {
            measureDeclaration = (Class) getLanguageModuleDeclaration(measureName);
        }
        return measureDeclaration;
    }

    @Override
    public Class getTupleDeclaration() {
        if (tupleDeclaration == null) {
            tupleDeclaration = (Class) getLanguageModuleDeclaration(tupleName);
        }
        return tupleDeclaration;
    }

    @Override
    public TypeDeclaration getArrayDeclaration() {
        if (arrayDeclaration == null) {
            arrayDeclaration = (Class) getLanguageModuleDeclaration(arrayName);
        }
        return arrayDeclaration;
    }

    @Override
    public Interface getRangedDeclaration() {
        if (rangedDeclaration == null) {
            rangedDeclaration = (Interface) getLanguageModuleDeclaration(rangedName);
        }
        return rangedDeclaration;
    }

    @Override
    public Class getEntryDeclaration() {
        if (entryDeclaration == null) {
            entryDeclaration = (Class) getLanguageModuleDeclaration(entryName);
        }
        return entryDeclaration;
    }

    @Override
    public TypeDeclaration getAnnotationDeclaration() {
        if (annotationDeclaration == null) {
            annotationDeclaration = (TypeDeclaration) getLanguageModuleDeclaration(annotationName);
        }
        return annotationDeclaration;
    }

    @Override
    public TypeDeclaration getConstrainedAnnotationDeclaration() {
        if (constrainedAnnotationDeclaration == null) {
            constrainedAnnotationDeclaration = (TypeDeclaration) getLanguageModuleDeclaration(constrainedAnnotationName);
        }
        return constrainedAnnotationDeclaration;
    }

    @Override
    public TypeDeclaration getSequencedAnnotationDeclaration() {
        if (sequencedAnnotationDeclaration == null) {
            sequencedAnnotationDeclaration = (TypeDeclaration) getLanguageModuleDeclaration(sequencedAnnotationName);
        }
        return sequencedAnnotationDeclaration;
    }

    @Override
    public TypeDeclaration getOptionalAnnotationDeclaration() {
        if (optionalAnnotationDeclaration == null) {
            optionalAnnotationDeclaration = (TypeDeclaration) getLanguageModuleDeclaration(optionalAnnotationName);
        }
        return optionalAnnotationDeclaration;
    }

    
    public Type getUnknownType() {
        if (unknownType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                unknownType = getUnknownDeclaration().getType();
            }
        }
        return unknownType;
    }
    
    public Type getNothingType() {
        if (nothingType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                nothingType = theTypeFactory.getType(getNothingDeclaration());
            }
        }
        return nothingType;
    }

    public Type getObjectType() {
        if (objectType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                objectType = theTypeFactory.getType(getObjectDeclaration());
            }
        }
        return objectType;
    }

    public Type getNullType() {
        if (nullType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                nullType = theTypeFactory.getType(getNullDeclaration());
            }
        }
        return nullType;
    }

    public Type getAnythingType() {
        if (anythingType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                anythingType = theTypeFactory.getType(getAnythingDeclaration());
            }
        }
        return anythingType;
    }

    
    @Override
    public Type getEmptyType() {
        if (emptyType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                emptyType = theTypeFactory.getType(getEmptyDeclaration());
            }
        }
        return emptyType;
    }

    @Override
    public Type getIdentifiableType() {
        if (identifiableType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                identifiableType = theTypeFactory.getType(getIdentifiableDeclaration());
            }
        }
        return identifiableType;
    }

    @Override
    public Type getBasicType() {
        if (basicType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                basicType = theTypeFactory.getType(getBasicDeclaration());
            }
        }
        return basicType;
    }

    @Override
    public Type getThrowableType() {
        if (throwableType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                throwableType = theTypeFactory.getType(getThrowableDeclaration());
            }
        }
        return throwableType;
    }

    @Override
    public Type getExceptionType() {
        if (exceptionType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                exceptionType = theTypeFactory.getType(getExceptionDeclaration());
            }
        }
        return exceptionType;
    }

    @Override
    public Type getBooleanType() {
        if (booleanType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                booleanType = theTypeFactory.getType(getBooleanDeclaration());
            }
        }
        return booleanType;
    }

    @Override
    public Type getStringType() {
        if (stringType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                stringType = theTypeFactory.getType(getStringDeclaration());
            }
        }
        return stringType;
    }

    @Override
    public Type getIntegerType() {
        if (integerType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                integerType = theTypeFactory.getType(getIntegerDeclaration());
            }
        }
        return integerType;
    }

    @Override
    public Type getFloatType() {
        if (floatType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                floatType = theTypeFactory.getType(getFloatDeclaration());
            }
        }
        return floatType;
    }

    @Override
    public Type getCharacterType() {
        if (characterType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                characterType = theTypeFactory.getType(getCharacterDeclaration());
            }
        }
        return characterType;
    }

    @Override
    public Type getByteType() {
        if (byteType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                byteType = theTypeFactory.getType(getByteDeclaration());
            }
        }
        return byteType;
    }

    @Override
    public Type getComparisonType() {
        if (comparisonType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                comparisonType = theTypeFactory.getType(getComparisonDeclaration());
            }
        }
        return comparisonType;
    }

    @Override
    public Type getDestroyableType() {
        if (destroyableType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                destroyableType = theTypeFactory.getType(getDestroyableDeclaration());
            }
        }
        return destroyableType;
    }

    @Override
    public Type getObtainableType() {
        if (obtainableType == null) {
            Unit theTypeFactory = getTypeFactory();
            if (theTypeFactory != null) {
                obtainableType = theTypeFactory.getType(getObtainableDeclaration());
            }
        }
        return obtainableType;
    }
}