package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.Declaration;
import ceylon.language.meta.declaration.FunctionOrValueDeclaration;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;

public class FunctionalUtil {
    public static Sequential<FunctionOrValueDeclaration> getParameters(Functional declaration) {
        ParameterList parameterList = ((Functional)declaration).getFirstParameterList();
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
