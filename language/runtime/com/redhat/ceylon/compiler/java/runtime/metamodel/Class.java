package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.Class$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class Class 
    extends ClassOrInterface
    implements ceylon.language.metamodel.Class {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Class.class);
    
    public Class(com.redhat.ceylon.compiler.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public Class$impl $ceylon$language$metamodel$Class$impl() {
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
    public AppliedClassType<? extends Object, ? super Sequential<? extends Object>> apply(){
        return apply(apply$types());
    }

    @Override
    public AppliedClassType<? extends Object, ? super Sequential<? extends Object>> apply(@Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.AppliedProducedType> types){
        Iterator iterator = types.iterator();
        Object it;
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.AppliedProducedType pt = (ceylon.language.metamodel.AppliedProducedType) it;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType modelPt = Metamodel.getModel(pt);
            producedTypes.add(modelPt);
        }
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedClassType = declaration.getProducedReference(null, producedTypes).getType();
        return (AppliedClassType)Metamodel.getAppliedMetamodel(appliedClassType);
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
