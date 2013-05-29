package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.declaration.Type;
import ceylon.language.metamodel.declaration.AttributeDeclaration$impl;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeAttribute 
    extends FreeDeclaration
    implements ceylon.language.metamodel.declaration.AttributeDeclaration, AnnotationBearing {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeAttribute.class);
    
    private Type type;

    protected FreeAttribute(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        super(declaration);

        this.type = Metamodel.getMetamodel(declaration.getType());
    }
    
    /** 
     * Instantiates a {@code FreeVariable} if the given value is variable 
     * otherwise a {@code FreeValue}.
     */
    public static FreeAttribute instance(com.redhat.ceylon.compiler.typechecker.model.Value declaration) {
        if (declaration.isVariable()) {
            return new FreeVariable(declaration);
        } else {
            return new FreeAttribute(declaration);
        }
    }

    @Override
    @Ignore
    public AttributeDeclaration$impl $ceylon$language$metamodel$declaration$AttributeDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object apply$instance() {
        return null;
    }

    @Override
    public ceylon.language.metamodel.Value<? extends Object> apply() {
        return apply(null);
    }
    
    @Override
    @TypeInfo("ceylon.language.metamodel::Value<ceylon.language::Anything>")
    public ceylon.language.metamodel.Value<? extends Object> apply(@Name @TypeInfo("ceylon.language::Anything") Object instance) {
        // FIXME: validate that instance is null for toplevels and not null for memberss
        com.redhat.ceylon.compiler.typechecker.model.Value modelDecl = (com.redhat.ceylon.compiler.typechecker.model.Value)declaration;
        return modelDecl.isVariable() ? new AppliedVariable(this, modelDecl.getType(), instance) : new AppliedValue(this, modelDecl.getType(), instance);
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::Type")
    public Type getType() {
        return type;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        Class<?> javaClass = Metamodel.getJavaClass(declaration);
        return Reflection.getDeclaredGetter(javaClass, Naming.getGetterName(declaration)).getAnnotations();
    }
}
