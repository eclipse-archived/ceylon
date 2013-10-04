package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.meta.declaration.ClassDeclaration$impl;
import ceylon.language.meta.declaration.FunctionalDeclaration$impl;
import ceylon.language.meta.model.ClassOrInterface;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public class FreeClass 
    extends FreeClassOrInterface
    implements ceylon.language.meta.declaration.ClassDeclaration {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClass.class);
    private Sequential<? extends ceylon.language.meta.declaration.FunctionOrValueDeclaration> parameters;
    
    public FreeClass(com.redhat.ceylon.compiler.typechecker.model.Class declaration) {
        super(declaration);
    }
    
    @Override
    protected void init() {
        super.init();
        // anonymous classes don't have parameter lists
        if(!declaration.isAnonymous()){
            ParameterList parameterList = ((com.redhat.ceylon.compiler.typechecker.model.Class)declaration).getParameterList();
            List<Parameter> modelParameters = parameterList.getParameters();
            ceylon.language.meta.declaration.FunctionOrValueDeclaration[] parameters = new ceylon.language.meta.declaration.FunctionOrValueDeclaration[modelParameters.size()];
            int i=0;
            for(Parameter modelParameter : modelParameters){
                parameters[i] = (ceylon.language.meta.declaration.FunctionOrValueDeclaration) Metamodel.getOrCreateMetamodel(modelParameter.getModel());
                i++;
            }
            this.parameters = Util.sequentialInstance(ceylon.language.meta.declaration.FunctionOrValueDeclaration.$TypeDescriptor, parameters);
        }else{
            this.parameters = (Sequential) empty_.get_();
        }
    }
    
    @Override
    @Ignore
    public ClassDeclaration$impl $ceylon$language$meta$declaration$ClassDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public FunctionalDeclaration$impl $ceylon$language$meta$declaration$FunctionalDeclaration$impl() {
        return null;
    }

    @Override
    public boolean getAnonymous(){
        return declaration.isAnonymous();
    }

    @Override
    public boolean getAnnotation(){
        return declaration.isAnnotation();
    }

    @Override
    public boolean getAbstract() {
        return ((com.redhat.ceylon.compiler.typechecker.model.Class)declaration).isAbstract();
    }

    @Override
    public boolean getFinal() {
        return ((com.redhat.ceylon.compiler.typechecker.model.Class)declaration).isFinal();
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::FunctionOrValueDeclaration>")
    public Sequential<? extends ceylon.language.meta.declaration.FunctionOrValueDeclaration> getParameterDeclarations(){
        checkInit();
        return parameters;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionOrValueDeclaration|ceylon.language::Null")
    public ceylon.language.meta.declaration.FunctionOrValueDeclaration getParameterDeclaration(@Name("name") String name){
        checkInit();
        Iterator<?> iterator = parameters.iterator();
        Object o;
        while((o = iterator.next()) != finished_.get_()){
            ceylon.language.meta.declaration.FunctionOrValueDeclaration pd = (ceylon.language.meta.declaration.FunctionOrValueDeclaration) o;
            if(pd.getName().equals(name))
                return pd;
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Type, Arguments extends Sequential<? extends Object>> ceylon.language.meta.model.Class<Type, Arguments> classApply(TypeDescriptor $reifiedType,
            TypeDescriptor $reifiedArguments){
        return classApply($reifiedType, $reifiedArguments, (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language.meta.model::Class<Type,Arguments>")
    @TypeParameters({
        @TypeParameter("Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    public <Type, Arguments extends Sequential<? extends Object>> ceylon.language.meta.model.Class<Type, Arguments> classApply(TypeDescriptor $reifiedType,
            TypeDescriptor $reifiedArguments,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(!getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a member declaration with no container type: use memberApply");
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(typeArguments);
        Metamodel.checkTypeArguments(null, declaration, producedTypes);
        com.redhat.ceylon.compiler.typechecker.model.ProducedReference appliedType = declaration.getProducedReference(null, producedTypes);
        AppliedClass<Type, Arguments> ret = (AppliedClass<Type, Arguments>) Metamodel.getAppliedMetamodel(appliedType.getType());;
        Metamodel.checkReifiedTypeArgument("classApply", "Class<$1,$2>", Variance.OUT, appliedType.getType(), $reifiedType, 
                Variance.IN, Metamodel.getProducedType(ret.$reifiedArguments), $reifiedArguments);
        return ret;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
        ceylon.language.meta.model.MemberClass<Container, Type, Arguments> memberClassApply(TypeDescriptor $reifiedContainer,
                                                                                       TypeDescriptor $reifiedType,
                                                                                       TypeDescriptor $reifiedArguments,
                                                                                       ceylon.language.meta.model.Type<? extends Container> containerType){
        
        return this.<Container, Type, Arguments>memberClassApply($reifiedContainer,
                                                 $reifiedType,
                                                 $reifiedArguments,
                                                 containerType,
                                                 (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language.meta.model::MemberClass<Container,Type,Arguments>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
        @TypeParameter(value = "Arguments", satisfies = "ceylon.language::Sequential<ceylon.language::Anything>")
    })
    @Override
    public <Container, Type, Arguments extends Sequential<? extends Object>>
    ceylon.language.meta.model.MemberClass<Container, Type, Arguments> memberClassApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Ignore TypeDescriptor $reifiedArguments,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a toplevel declaration to a container type: use apply");

        ceylon.language.meta.model.MemberClass<Container, Type, Arguments> member 
            = (ceylon.language.meta.model.MemberClass)
                getAppliedClassOrInterface(null, null, typeArguments, containerType);
        
        // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
        // implementation types to the user, such as AppliedMemberClass
        TypeDescriptor actualReifiedContainer = ((AppliedMemberClass)member).$reifiedContainer;
        TypeDescriptor actualReifiedArguments = ((AppliedMemberClass)member).$reifiedArguments;
        ProducedType actualType = Metamodel.getModel((ceylon.language.meta.model.Type<?>) member);
        Metamodel.checkReifiedTypeArgument("memberApply", "Member<$1,Class<$2,$3>>&Class<$2,$3>", 
                Variance.IN, Metamodel.getProducedType(actualReifiedContainer), $reifiedContainer, 
                Variance.OUT, actualType, $reifiedType,
                Variance.IN, Metamodel.getProducedType(actualReifiedArguments), $reifiedArguments);
        return member;
    }

    @Override
    public int hashCode() {
        int result = 1;
        java.lang.Object container = getContainer();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + getName().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.declaration.ClassDeclaration == false)
            return false;
        ceylon.language.meta.declaration.ClassDeclaration other = (ceylon.language.meta.declaration.ClassDeclaration) obj;
        if(!Util.eq(other.getContainer(), getContainer()))
            return false;
        return getName().equals(other.getName());
    }

    @Override
    public String toString() {
        return "class "+super.toString();
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
