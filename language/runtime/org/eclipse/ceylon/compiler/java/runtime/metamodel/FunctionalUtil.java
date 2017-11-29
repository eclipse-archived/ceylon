/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.Declaration;
import ceylon.language.meta.declaration.FunctionOrValueDeclaration;

public class FunctionalUtil {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Sequential<FunctionOrValueDeclaration> getParameters(Functional declaration) {
        ParameterList parameterList = ((Functional)declaration).getFirstParameterList();
        if(parameterList == null)
            return (Sequential)empty_.get_();
        List<Parameter> modelParameters = parameterList.getParameters();
        ceylon.language.meta.declaration.FunctionOrValueDeclaration[] parameters = new ceylon.language.meta.declaration.FunctionOrValueDeclaration[modelParameters.size()];
        int i=0;
        for(Parameter modelParameter : modelParameters){
            parameters[i] = (ceylon.language.meta.declaration.FunctionOrValueDeclaration)Metamodel.getOrCreateMetamodel(modelParameter.getModel());
            i++;
        }
        return Util.sequentialWrapper(ceylon.language.meta.declaration.FunctionOrValueDeclaration.$TypeDescriptor$, parameters);
    }
    
    public static FunctionOrValueDeclaration getParameterDeclaration(Sequential<? extends FunctionOrValueDeclaration> parameterList, String name) {
        Iterator<?> iterator = parameterList.iterator();
        Object o;
        while((o = iterator.next()) != finished_.get_()){
            ceylon.language.meta.declaration.FunctionOrValueDeclaration pd = (ceylon.language.meta.declaration.FunctionOrValueDeclaration) o;
            if(((Declaration)pd).getName().equals(name))
                return pd;
        }
        return null;
    }
}
