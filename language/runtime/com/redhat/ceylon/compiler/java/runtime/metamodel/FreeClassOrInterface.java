package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.untyped.ClassOrInterface$impl;
import ceylon.language.metamodel.untyped.Declaration;
import ceylon.language.metamodel.untyped.Parameterised$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
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

    private List<ceylon.language.metamodel.untyped.Declaration> declarations;

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
        this.declarations = new LinkedList<ceylon.language.metamodel.untyped.Declaration>();
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            if(memberModelDeclaration instanceof Method){
                declarations.add(new FreeFunction((Method) memberModelDeclaration));
            }else if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value){
                declarations.add(new FreeValue((MethodOrValue) memberModelDeclaration));
            }else if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
                declarations.add(Metamodel.getOrCreateMetamodel(memberModelDeclaration));
            }
        }
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
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration"))
    public <Kind extends ceylon.language.metamodel.untyped.Declaration> Sequential<? extends Kind> 
        members(@Ignore TypeDescriptor $reifiedKind) {
        
        checkInit();
        
        if($reifiedKind instanceof TypeDescriptor.Class){
            List<Kind> members = new ArrayList<Kind>(declarations.size());
            java.lang.Class<?> declarationClass = ((TypeDescriptor.Class) $reifiedKind).getKlass();
            for(ceylon.language.metamodel.untyped.Declaration decl : declarations){
                if((declarationClass == ceylon.language.metamodel.untyped.Function.class
                        && decl instanceof ceylon.language.metamodel.untyped.Function)
                    || (declarationClass == ceylon.language.metamodel.untyped.Class.class
                            && decl instanceof ceylon.language.metamodel.untyped.Class)
                    || (declarationClass == ceylon.language.metamodel.untyped.Interface.class
                            && decl instanceof ceylon.language.metamodel.untyped.Interface)
                    || (declarationClass == ceylon.language.metamodel.untyped.ClassOrInterface.class
                            && decl instanceof ceylon.language.metamodel.untyped.ClassOrInterface)
                    || (declarationClass == ceylon.language.metamodel.untyped.Value.class
                            && decl instanceof ceylon.language.metamodel.untyped.Value)
                    || declarationClass == ceylon.language.metamodel.untyped.Declaration.class){
                    members.add((Kind) decl);
                }
            }
            return (Sequential)Util.sequentialInstance($reifiedKind, members.toArray());
        }
        throw new RuntimeException("Not supported yet");
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({
            @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration"),
            @TypeParameter("Annotation")
    })
    public <Kind extends ceylon.language.metamodel.untyped.Declaration, Annotation> Sequential<? extends Kind> 
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
        return this.<FreeFunction>findDeclaration(name);
    }

    FreeValue findValue(String name) {
        return this.<FreeValue>findDeclaration(name);
    }


    FreeClassOrInterface findType(String name) {
        return this.<FreeClassOrInterface>findDeclaration(name);
    }

    <T extends FreeDeclaration> T findDeclaration(String name) {
        checkInit();
        for(ceylon.language.metamodel.untyped.Declaration decl : declarations){
            // in theory we can't have several members with the same name so no need to check the type
            // FIXME: interop and overloading
            if(decl.getName().equals(name))
                return (T) decl;
        }
        return null;
    }
}
