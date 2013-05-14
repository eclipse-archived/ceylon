package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.untyped.Class$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeClass 
    extends FreeClassOrInterface
    implements ceylon.language.metamodel.untyped.Class {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClass.class);
    private Sequential<? extends ceylon.language.metamodel.untyped.Parameter> parameters;
    
    public FreeClass(com.redhat.ceylon.compiler.typechecker.model.Class declaration) {
        super(declaration);
    }

    @Override
    protected void init() {
        super.init();
        ParameterList parameterList = ((com.redhat.ceylon.compiler.typechecker.model.Class)declaration).getParameterList();
        List<Parameter> modelParameters = parameterList.getParameters();
        ceylon.language.metamodel.untyped.Parameter[] parameters = new ceylon.language.metamodel.untyped.Parameter[modelParameters.size()];
        int i=0;
        for(Parameter modelParameter : modelParameters){
            parameters[i++] = new FreeParameter(modelParameter);
        }
        this.parameters = Util.sequentialInstance(ceylon.language.metamodel.untyped.Parameter.$TypeDescriptor, parameters);
    }
    
    @Override
    @Ignore
    public Class$impl $ceylon$language$metamodel$untyped$Class$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.untyped::Parameter>")
    public Sequential<? extends ceylon.language.metamodel.untyped.Parameter> getParameters(){
        return parameters;
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.AppliedType> apply$types(){
        return (Sequential) empty_.getEmpty$();
    }

    @Ignore
    @Override
    public AppliedClassType<? extends Object, ? super Sequential<? extends Object>> apply(){
        return apply(apply$types());
    }

    @Override
    public AppliedClassType<? extends Object, ? super Sequential<? extends Object>> apply(@Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.AppliedType> types){
        Iterator iterator = types.iterator();
        Object it;
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.AppliedType pt = (ceylon.language.metamodel.AppliedType) it;
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
