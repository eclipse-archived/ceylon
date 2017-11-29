/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
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
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;

/**
 * Used for annotation proxies for interop.
 * 
 * The completer only sets members and parameterlist, so we only load on access of those
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyClass extends Class {

    public final LazyInterface iface;
    private ModelCompleter completer;
    private boolean isLoaded2;
    private boolean isLoaded;

    public AnnotationProxyClass(ModelCompleter completer, LazyInterface iface) {
        this.iface = iface;
        this.completer = completer;
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }
    
    @Override
    public ParameterList getParameterList() {
        // getParameterLists() depends on this, so we cover both in one override
        load();
        return super.getParameterList();
    }
    
    @Override
    public boolean isSealed() {
        // super.isSealed depends on the parameter list
        load();
        return super.isSealed();
    }

    private void load() {
        if(!isLoaded2){
            completer.synchronizedRun(new Runnable() {
                @Override
                public void run() {
                    if(!isLoaded){
                        isLoaded = true;
                        completer.complete(AnnotationProxyClass.this);
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
