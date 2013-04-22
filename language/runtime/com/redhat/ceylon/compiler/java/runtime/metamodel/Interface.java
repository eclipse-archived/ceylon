package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.metamodel.Interface$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class Interface 
    extends ClassOrInterface
    implements ceylon.language.metamodel.Interface {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Interface.class);
    
    public Interface(com.redhat.ceylon.compiler.typechecker.model.Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Interface$impl $ceylon$language$metamodel$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.AppliedProducedType> apply$types(){
        return (Sequential) empty_.getEmpty$();
    }

    @Ignore
    @Override
    public AppliedInterfaceType<? extends Object> apply(){
        return apply(apply$types());
    }

    @Override
    public AppliedInterfaceType<? extends Object> apply(@Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.AppliedProducedType> types){
        return null;
    }
    
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
