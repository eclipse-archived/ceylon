package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ConstructorDeclaration;
import ceylon.language.meta.model.Class;
import ceylon.language.meta.model.Constructor;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedConstructor<Type, Arguments extends Sequential<? extends Object>>
        implements Constructor<Type, Arguments>{
    protected FreeConstructor declaration;
    private ceylon.language.meta.model.Type<Object> type;
    private List<ProducedType> parameterProducedTypes;
    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;
    private Class<Type, ? extends Object> container;
    
    private volatile boolean initialized = false;
    
    private int firstDefaulted;
    private int variadicIndex;
    private MethodHandle[] dispatch;
    
    public AppliedConstructor(
            Class<Type, ? extends Object> container,
            ProducedReference appliedFunction,
            FreeConstructor declaration) {
        this.container = container;
        this.declaration = declaration;
        
        com.redhat.ceylon.compiler.typechecker.model.Constructor decl = declaration.constructor;
        List<Parameter> parameters = decl.getParameterLists().get(0).getParameters();
        
        this.type = Metamodel.getAppliedMetamodel(Metamodel.getFunctionReturnType(appliedFunction));
        
        // get a list of produced parameter types
        this.parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, appliedFunction);
        this.parameterTypes = Metamodel.getAppliedMetamodelSequential(this.parameterProducedTypes);
    }
    
    protected void checkInit() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            }
        }
    }
    
    private void init() {
        //initConstructor(declaration.constructor);
    }

    @Override
    public Type apply() {
        return apply(empty_.get_());
    }

    @Override
    public Type apply(Sequential<? extends Object> arg0) {
        checkInit();
        // TODO 
        return null;
    }

    @Override
    public Type namedApply(
            Iterable<? extends Entry<? extends String, ? extends Object>, ? extends Object> arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type>")
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes() {
        return this.parameterTypes;
    }

    @TypeInfo("ceylon.language.meta.model::Class<Type,ceylon.language::Nothing>")
    @Override
    public Class<Type,? extends Object> getContainer() {
        return container;
    }

    @Override
    public ConstructorDeclaration getDeclaration() {
        return declaration;
    }
    
    @Override
    public Type $call$() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call$(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object arg0, Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call$(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2,
            Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call$(Object... args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $callvariadic$(Object... argsAndVarargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short $getVariadicParameterIndex$() {
        // TODO Auto-generated method stub
        return 0;
    }
    

    @Override
    public java.lang.String toString() {
        return Metamodel.toTypeString(getContainer()) + "." + getDeclaration().getName();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other instanceof AppliedConstructor) {
            return this.getDeclaration().getName().equals(((AppliedConstructor)other).getDeclaration().getName())
                    && this.getContainer().equals(((AppliedConstructor)other).getContainer());
                    
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return this.getContainer().hashCode() ^ this.getDeclaration().getName().hashCode();
    }
}
