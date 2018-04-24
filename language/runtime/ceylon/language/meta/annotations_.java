/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language.meta;

import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.getReflectedAnnotationClass;

import org.eclipse.ceylon.compiler.java.metadata.Annotations;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;

import ceylon.language.Annotation;
import ceylon.language.AssertionError;
import ceylon.language.OptionalAnnotation;
import ceylon.language.Sequence;
import ceylon.language.Sequential;

@Ceylon(major = 8)
@Method
public final class annotations_ {
    
    private annotations_() {}
    
    // TODO @Shared$annotation
    @SuppressWarnings("unchecked")
    @Annotations({@org.eclipse.ceylon.compiler.java.metadata.Annotation("shared")})
    @TypeInfo("Values")
    @TypeParameters({
        @TypeParameter(value = "Value", 
                satisfies = {"ceylon.language::ConstrainedAnnotation<Value,Values,ProgramElement>"}), 
        @TypeParameter(value = "Values"), 
        @TypeParameter(value = "ProgramElement", 
                satisfies = {"ceylon.language::Annotated"}, 
                variance=Variance.IN)
    })
    public static <Value extends java.lang.annotation.Annotation, Values, ProgramElement extends ceylon.language.Annotated>Values annotations(@org.eclipse.ceylon.compiler.java.metadata.Ignore
            final org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $reified$Value, @org.eclipse.ceylon.compiler.java.metadata.Ignore
            final org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $reified$Values, @org.eclipse.ceylon.compiler.java.metadata.Ignore
            final org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor $reified$ProgramElement, @org.eclipse.ceylon.compiler.java.metadata.Name("annotationType")
            @org.eclipse.ceylon.compiler.java.metadata.TypeInfo(
                    value = "ceylon.language.meta.model::Class<ceylon.language::ConstrainedAnnotation<Value,Values,ProgramElement>,ceylon.language::Nothing>",
                    erased = true)
            final ceylon.language.meta.model.Class annotationType, 
            @org.eclipse.ceylon.compiler.java.metadata.Name("programElement")
            @org.eclipse.ceylon.compiler.java.metadata.TypeInfo(
                    value = "ProgramElement",
                    erased = true)
            final ProgramElement programElement) {
        Sequential<? extends Annotation> results = 
                Metamodel.annotations($reified$Value, programElement);
        boolean optional = OptionalAnnotation.class
                .isAssignableFrom(getReflectedAnnotationClass(annotationType));
        if (optional) {
            if (results.getSize() > 1L) {
                throw new AssertionError("optional annotation occurs more than once");
            }
            Object singleResult = 
                    results instanceof Sequence ? 
                            ((Sequence<?>) results).getFirst() : 
                            null;
            return (Values) singleResult;   
        }
        else {
            return (Values) results;
        }
    }
}