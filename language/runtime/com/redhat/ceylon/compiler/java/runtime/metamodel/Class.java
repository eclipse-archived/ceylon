package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Empty;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.Class$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
public class Class<Type, Arguments extends Sequential<? extends Object>> 
    extends ClassOrInterface<Type>
    implements ceylon.language.metamodel.Class<Type, Arguments> {

    @Ignore
    private TypeDescriptor $reifiedArguments;

    public Class(com.redhat.ceylon.compiler.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    protected void init() {
        super.init();
        $reifiedArguments = Empty.$TypeDescriptor;
    }
    
    @Override
    @Ignore
    public Class$impl<Type, Arguments> $ceylon$language$metamodel$Class$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type $call(Object... args) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.ProducedType> apply$types(){
        return (Sequential) empty_.getEmpty$();
    }

    @Ignore
    @Override
    public ClassType<? extends Type, ? super Sequential<? extends Object>> apply(){
        return apply(apply$types());
    }

    @Override
    public ClassType<? extends Type, ? super Sequential<? extends Object>> apply(@Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.ProducedType> types){
        Iterator iterator = types.iterator();
        Object it;
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.ProducedType pt = (ceylon.language.metamodel.ProducedType) it;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType modelPt = Metamodel.getModel(pt);
            producedTypes.add(modelPt);
        }
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedClassType = declaration.getProducedReference(null, producedTypes).getType();
        return (ClassType)Metamodel.getMetamodel(appliedClassType);
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(Class.class, $reifiedType, $reifiedArguments);
    }

    @Ignore
    TypeDescriptor $getReifiedArguments(){
        checkInit();
        return $reifiedArguments;
    }
}
