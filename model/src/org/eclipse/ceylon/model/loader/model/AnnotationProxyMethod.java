/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.loader.model;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.ceylon.model.loader.ModelCompleter;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;

/**
 * Used for annotation interop.
 * 
 * The completer only sets members, parameterLists and annotationConstructor, so load for them.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyMethod extends Function {
    
    public final AnnotationProxyClass proxyClass;
    
    private OutputElement annotationTarget;

    private ModelCompleter completer;

    private boolean isLoaded2;

    private boolean isLoaded;

    private OutputElement outputElement;
    
    public AnnotationProxyMethod(ModelCompleter completer, AnnotationProxyClass proxyClass, OutputElement outputElement) {
        this.completer = completer;
        this.proxyClass = proxyClass;
        this.outputElement = outputElement;
    }

    public AnnotationProxyClass getProxyClass() {
        return proxyClass;
    }
    
    public void setAnnotationTarget(OutputElement annotationTarget) {
        this.annotationTarget = annotationTarget;
    }
    
    /**
     * If this is a disambiguating proxy annotation method, then this is the 
     * Java program element that the constructor targets. Otherwise null
     */
    public OutputElement getAnnotationTarget() {
        return this.annotationTarget;
    }
    
    /**
     * The elements in the {@code @Target} annotation, or null if 
     * the annotation type lacks the {@code @Target} annotation.
     */
    @Override
    public EnumSet<AnnotationTarget> getAnnotationTargets() {
        return outputElement != null 
                ? EnumSet.of(outputElement.toAnnotationTarget()) 
                : AnnotationTarget.getAnnotationTarget(proxyClass.iface);
    }

    @Override
    public Object getAnnotationConstructor() {
        load();
        return super.getAnnotationConstructor();
    }
    
    @Override
    public List<ParameterList> getParameterLists() {
        load();
        return super.getParameterLists();
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }
    
    private void load() {
        if(!isLoaded2){
            completer.synchronizedRun(new Runnable() {
                @Override
                public void run() {
                    if(!isLoaded){
                        isLoaded = true;
                        completer.complete(AnnotationProxyMethod.this);
                        isLoaded2 = true;
                    }
                }
            });
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }
}
