package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.metamodel.AppliedType;
import ceylon.language.metamodel.untyped.Interface$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeInterface 
    extends FreeClassOrInterface
    implements ceylon.language.metamodel.untyped.Interface {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeInterface.class);
    
    public FreeInterface(com.redhat.ceylon.compiler.typechecker.model.Interface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Interface$impl $ceylon$language$metamodel$untyped$Interface$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.AppliedType> apply$types(){
        return (Sequential) empty_.getEmpty$();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Interface<? extends Object> apply(){
        return apply(apply$types());
    }

    @Override
    public ceylon.language.metamodel.Interface<? extends Object> apply(@Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.AppliedType> types){
        return null;
    }
    
    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
