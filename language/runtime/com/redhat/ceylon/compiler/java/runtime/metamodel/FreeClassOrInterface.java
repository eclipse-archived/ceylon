package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.untyped.ClassOrInterface$impl;
import ceylon.language.metamodel.untyped.Parameterised$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeClassOrInterface
    extends FreeDeclaration
    implements ceylon.language.metamodel.untyped.ClassOrInterface {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClassOrInterface.class);
    
    @Ignore
    static final TypeDescriptor $InterfacesTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.untyped.ParameterisedType.class, ceylon.language.metamodel.untyped.Interface.$TypeDescriptor);

    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.untyped.Function.class, Anything.$TypeDescriptor, Empty.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $ValueTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.untyped.Value.class, Anything.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $ClassOrInterfaceTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.untyped.ClassOrInterface.class, Anything.$TypeDescriptor);
    
    private volatile boolean initialised = false;
    private ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Class> superclass;
    private Sequential<ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Interface>> interfaces;
    private Sequential<ceylon.language.metamodel.untyped.TypeParameter> typeParameters;

    private Sequential<ceylon.language.metamodel.untyped.Member<ceylon.language.metamodel.untyped.Function>> functions;
    private Sequential<ceylon.language.metamodel.untyped.Member<ceylon.language.metamodel.untyped.Value>> values;
    private Sequential<ceylon.language.metamodel.untyped.Member<ceylon.language.metamodel.untyped.ClassOrInterface>> types;

    public FreeClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterface$impl $ceylon$language$metamodel$untyped$ClassOrInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Parameterised$impl $ceylon$language$metamodel$untyped$Parameterised$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void init(){
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) this.declaration;
        
        ProducedType superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Class>) Metamodel.getMetamodel(superType);
        
        List<ProducedType> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Interface>[] interfaces = new ceylon.language.metamodel.untyped.ParameterisedType[satisfiedTypes.size()];
        int i=0;
        for(ProducedType pt : satisfiedTypes){
            interfaces[i++] = (ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Interface>) Metamodel.getMetamodel(pt);
        }
        this.interfaces = (Sequential)Util.sequentialInstance($InterfacesTypeDescriptor, interfaces);
        
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.metamodel.untyped.TypeParameter[] typeParametersArray = new ceylon.language.metamodel.untyped.TypeParameter[typeParameters.size()];
        i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        this.typeParameters = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.untyped.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        List<com.redhat.ceylon.compiler.typechecker.model.Declaration> memberModelDeclarations = declaration.getMembers();
        i=0;
        List<ceylon.language.metamodel.untyped.Member<FreeFunction>> functions = new LinkedList<ceylon.language.metamodel.untyped.Member<FreeFunction>>();
        List<ceylon.language.metamodel.untyped.Member<FreeValue>> values = new LinkedList<ceylon.language.metamodel.untyped.Member<FreeValue>>();
        List<ceylon.language.metamodel.untyped.Member<FreeClassOrInterface>> types = new LinkedList<ceylon.language.metamodel.untyped.Member<FreeClassOrInterface>>();
        // FIXME: this is just wrong for implementing members()
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            if(memberModelDeclaration instanceof Method){
                functions.add(new FreeMember(this, new FreeFunction((Method) memberModelDeclaration)));
            }else if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value){
                values.add(new FreeMember(this, new FreeValue((MethodOrValue) memberModelDeclaration)));
            }else if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
                types.add(new FreeMember(this, Metamodel.getOrCreateMetamodel(memberModelDeclaration)));
            }
        }

        TypeDescriptor functionMemberTD = TypeDescriptor.klass(ceylon.language.metamodel.untyped.Member.class, $FunctionTypeDescriptor);
        this.functions = (Sequential)Util.sequentialInstance(functionMemberTD, functions.toArray(new ceylon.language.metamodel.untyped.Member[functions.size()]));

        TypeDescriptor valueMemberTD = TypeDescriptor.klass(ceylon.language.metamodel.untyped.Member.class, $ValueTypeDescriptor);
        this.values = (Sequential)Util.sequentialInstance(valueMemberTD, values.toArray(new ceylon.language.metamodel.untyped.Member[values.size()]));

        TypeDescriptor typesMemberTD = TypeDescriptor.klass(ceylon.language.metamodel.untyped.Member.class, $ClassOrInterfaceTypeDescriptor);
        this.types = (Sequential)Util.sequentialInstance(typesMemberTD, types.toArray(new ceylon.language.metamodel.untyped.Member[types.size()]));
    }
    
    protected final void checkInit(){
        if(!initialised){
            // FIXME: lock on model loader?
            synchronized(this){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.untyped::Member<Kind>>")
    public <Kind extends ceylon.language.metamodel.untyped.Declaration> Sequential<? extends ceylon.language.metamodel.untyped.Member<Kind>> 
        members(@Ignore TypeDescriptor $reifiedKind) {
        
        checkInit();
        if($reifiedKind instanceof TypeDescriptor.Class){
            TypeDescriptor.Class klass = (TypeDescriptor.Class) $reifiedKind;
            if(klass.getKlass() == ceylon.language.metamodel.untyped.Function.class){
                return (Sequential) functions;
            }
            if(klass.getKlass() == ceylon.language.metamodel.untyped.Value.class){
                return (Sequential) values;
            }
        }
        throw new RuntimeException("Not supported yet");
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.untyped::Member<Kind>>")
    public <Kind extends ceylon.language.metamodel.untyped.Declaration, Annotation> Sequential<? extends ceylon.language.metamodel.untyped.Member<Kind>> 
        annotatedMembers(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        
        checkInit();
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.untyped::ParameterisedType<ceylon.language.metamodel.untyped::Interface>>")
    public Sequential<? extends ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Interface>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::ParameterisedType<ceylon.language.metamodel.untyped::Class>|ceylon.language::Null")
    public ceylon.language.metamodel.untyped.ParameterisedType<ceylon.language.metamodel.untyped.Class> getSuperclass() {
        checkInit();
        return superclass;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.untyped::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.untyped.TypeParameter> getTypeParameters() {
        checkInit();
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.untyped.TypeParameter getTypeParameter(@Name("name") String name) {
        checkInit();
        Iterator<? extends ceylon.language.metamodel.untyped.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.untyped.TypeParameter tp = (ceylon.language.metamodel.untyped.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @Ignore
    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.ClassOrInterface<? extends Object>>
        Sequential<? extends ceylon.language.metamodel.AppliedType> memberApply$types(TypeDescriptor $reifiedContainer,
                                                                                      TypeDescriptor $reifiedKind){
        
        return (Sequential) empty_.getEmpty$();
    }

    @Ignore
    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.ClassOrInterface<? extends Object>>
        ceylon.language.metamodel.Member<Container, Kind> memberApply(TypeDescriptor $reifiedContainer,
                                                                      TypeDescriptor $reifiedKind){
        
        return this.<Container, Kind>memberApply($reifiedContainer,
                                                 $reifiedKind,
                                                 this.<Container, Kind>memberApply$types($reifiedContainer, $reifiedKind));
    }

    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.ClassOrInterface<? extends Object>>
        ceylon.language.metamodel.Member<Container, Kind> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedKind,
                @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.AppliedType> types){
        // FIXME: check this
        AppliedClassOrInterfaceType<Container> containerType = (AppliedClassOrInterfaceType<Container>) Metamodel.getAppliedMetamodel($reifiedContainer);
        return getAppliedClassOrInterface($reifiedContainer, $reifiedKind, types, containerType);
    }

    <Type, Kind extends ceylon.language.metamodel.ClassOrInterface<? extends Object>>
    ceylon.language.metamodel.Member<Type, Kind> getAppliedClassOrInterface(TypeDescriptor $reifiedType, TypeDescriptor $reifiedKind, 
                                                                            Sequential<? extends ceylon.language.metamodel.AppliedType> types,
                                                                            AppliedClassOrInterfaceType<Type> container){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        // FIXME: this is wrong because it does not include the container type
        final ProducedType appliedType = declaration.getProducedReference(null, producedTypes).getType();
        return new AppliedMember<Type, Kind>($reifiedType, $reifiedKind, container){
            @Override
            protected Kind bindTo(Object instance) {
                return (Kind) (declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface 
                        ? new AppliedInterfaceType(appliedType)
                        : new AppliedClassType(appliedType, instance));
            }
        };
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    FreeFunction findMethod(String name) {
        checkInit();
        Iterator iterator = functions.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            FreeMember member = (FreeMember) it;
            FreeFunction f = (FreeFunction) member.declaration;
            if(f.getName().equals(name))
                return f;
        }
        return null;
    }

    FreeValue findValue(String name) {
        checkInit();
        Iterator iterator = values.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            FreeMember member = (FreeMember) it;
            FreeValue f = (FreeValue) member.declaration;
            if(f.getName().equals(name))
                return f;
        }
        return null;
    }


    FreeClassOrInterface findType(String name) {
        checkInit();
        Iterator iterator = types.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            FreeMember member = (FreeMember) it;
            FreeClassOrInterface f = (FreeClassOrInterface) member.declaration;
            if(f.getName().equals(name))
                return f;
        }
        return null;
    }
}
