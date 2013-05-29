package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.util.List;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.metamodel.declaration.ClassDeclaration$impl;

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
    implements ceylon.language.metamodel.declaration.ClassDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClass.class);
    private Sequential<? extends ceylon.language.metamodel.declaration.Parameter> parameters;
    
    public FreeClass(com.redhat.ceylon.compiler.typechecker.model.Class declaration) {
        super(declaration);
    }

    private static java.lang.reflect.Constructor<?> findConstructor(Class<?> javaClass) {
        // How to find the right Method, just go for the one with the longest parameter list?
        // OR go via the Method in AppliedFunction?
        java.lang.reflect.Constructor<?> best = null;
        int numBestParams = -1;
        int numBest = 0;
        for (java.lang.reflect.Constructor<?> meth : javaClass.getDeclaredConstructors()) {
            if (meth.isSynthetic()
                    || meth.getAnnotation(Ignore.class) != null) {
                continue;
            }
            
            Class<?>[] parameterTypes = meth.getParameterTypes();
            if (parameterTypes.length > numBestParams) {
                best = meth;
                numBestParams = parameterTypes.length;
                numBest = 1;
            } else if (parameterTypes.length == numBestParams) {
                numBest++;
            }
        }
        if (best == null) {
            throw new RuntimeException("Couldn't find method " + javaClass);
        }
        if (numBest > 1) {
            throw new RuntimeException("Method arity ambiguity " + javaClass);
        }
        return best;
    }
    
    @Override
    protected void init() {
        super.init();
        ParameterList parameterList = ((com.redhat.ceylon.compiler.typechecker.model.Class)declaration).getParameterList();
        List<Parameter> modelParameters = parameterList.getParameters();
        ceylon.language.metamodel.declaration.Parameter[] parameters = new ceylon.language.metamodel.declaration.Parameter[modelParameters.size()];
        Annotation[][] parameterAnnotations = findConstructor(Metamodel.getJavaClass(declaration)).getParameterAnnotations();
        int i=0;
        for(Parameter modelParameter : modelParameters){
            parameters[i] = new FreeParameter(modelParameter, parameterAnnotations[i]);
            i++;
        }
        this.parameters = Util.sequentialInstance(ceylon.language.metamodel.declaration.Parameter.$TypeDescriptor, parameters);
    }
    
    @Override
    @Ignore
    public ClassDeclaration$impl $ceylon$language$metamodel$declaration$ClassDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::Parameter>")
    public Sequential<? extends ceylon.language.metamodel.declaration.Parameter> getParameters(){
        return parameters;
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.AppliedType> apply$types(){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> apply(){
        return apply(apply$types());
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> apply(
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::AppliedType>") 
            Sequential<? extends ceylon.language.metamodel.AppliedType> types){
        return bindAndApply(null, types);
    }

    @Ignore
    @Override
    public Sequential<? extends ceylon.language.metamodel.AppliedType> bindAndApply$types(Object instance){
        return (Sequential) empty_.$get();
    }

    @Ignore
    @Override
    public ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> bindAndApply(Object instance){
        return bindAndApply(instance, bindAndApply$types(instance));
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> bindAndApply(
            @Name("instance") @TypeInfo("ceylon.language::Object") Object instance,
            @Name("types") @Sequenced @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::AppliedType>") 
            Sequential<? extends ceylon.language.metamodel.AppliedType> types){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        // FIXME: this is wrong because it does not include the container type
        com.redhat.ceylon.compiler.typechecker.model.ProducedType appliedClassType = declaration.getProducedReference(null, producedTypes).getType();
        return new AppliedClassType(appliedClassType, instance);
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
