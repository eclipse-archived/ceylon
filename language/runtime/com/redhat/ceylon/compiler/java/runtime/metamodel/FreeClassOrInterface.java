package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.Iterator;
import ceylon.language.SequenceBuilder;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration$impl;
import ceylon.language.metamodel.declaration.GenericDeclaration$impl;

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
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeClassOrInterface
    extends FreeTopLevelOrMemberDeclaration
    implements ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClassOrInterface.class);
    
    @Ignore
    static final TypeDescriptor $InterfacesTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.declaration.OpenParameterisedType.class, ceylon.language.metamodel.declaration.InterfaceDeclaration.$TypeDescriptor);

    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.declaration.FunctionDeclaration.class, Anything.$TypeDescriptor, Empty.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $AttributeTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.declaration.AttributeDeclaration.class, Anything.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $ClassOrInterfaceTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration.class, Anything.$TypeDescriptor);
    
    private volatile boolean initialised = false;
    private ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.ClassDeclaration> superclass;
    private Sequential<ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.InterfaceDeclaration>> interfaces;
    private Sequential<ceylon.language.metamodel.declaration.TypeParameter> typeParameters;

    private List<ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration> declarations;

    public FreeClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterfaceDeclaration$impl $ceylon$language$metamodel$declaration$ClassOrInterfaceDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$metamodel$declaration$GenericDeclaration$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void init(){
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) this.declaration;
        
        ProducedType superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.ClassDeclaration>) Metamodel.getMetamodel(superType);
        
        List<ProducedType> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.InterfaceDeclaration>[] interfaces = new ceylon.language.metamodel.declaration.OpenParameterisedType[satisfiedTypes.size()];
        int i=0;
        for(ProducedType pt : satisfiedTypes){
            interfaces[i++] = (ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.InterfaceDeclaration>) Metamodel.getMetamodel(pt);
        }
        this.interfaces = (Sequential)Util.sequentialInstance($InterfacesTypeDescriptor, interfaces);
        
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.metamodel.declaration.TypeParameter[] typeParametersArray = new ceylon.language.metamodel.declaration.TypeParameter[typeParameters.size()];
        i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        this.typeParameters = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.declaration.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        List<com.redhat.ceylon.compiler.typechecker.model.Declaration> memberModelDeclarations = declaration.getMembers();
        i=0;
        this.declarations = new LinkedList<ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration>();
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            if(memberModelDeclaration instanceof Method){
                declarations.add(new FreeFunction((Method) memberModelDeclaration));
            }else if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value){
                declarations.add(FreeAttribute.instance((com.redhat.ceylon.compiler.typechecker.model.Value)memberModelDeclaration));
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
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.declaration::TopLevelOrMemberDeclaration"))
    public <Kind extends ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration> Sequential<? extends Kind> 
    members(@Ignore TypeDescriptor $reifiedKind) {
        
        Predicates.Predicate predicate = Predicates.isDeclarationOfKind($reifiedKind);
        
        return filteredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({
            @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.declaration::TopLevelOrMemberDeclaration"),
            @TypeParameter("Annotation")
    })
    public <Kind extends ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration, Annotation> Sequential<? extends Kind> 
    annotatedMembers(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        
        Predicates.Predicate predicate = Predicates.and(
                Predicates.isDeclarationOfKind($reifiedKind),
                Predicates.isDeclarationAnnotatedWith($reifiedAnnotation));
        
        return filteredMembers($reifiedKind, predicate);
    }

    private <Kind> Sequential<? extends Kind> filteredMembers(
            TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return (Sequential<? extends Kind>)empty_.$get();
        }
        checkInit();
        SequenceBuilder<Kind> members = new SequenceBuilder<Kind>($reifiedKind, declarations.size());
        for(ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration decl : declarations){
            if (predicate.accept(((FreeTopLevelOrMemberDeclaration)decl).declaration)) {
                members.append((Kind) decl);
            }
        }
        return members.getSequence();
    }
    
    private <Kind> Kind filteredMember(
            TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return null;
        }
        List<com.redhat.ceylon.compiler.typechecker.model.Declaration> modelMembers = declaration.getMembers();
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration modelDecl : modelMembers){
            if (predicate.accept(modelDecl)) {
                return (Kind)Metamodel.getOrCreateMetamodel(modelDecl);
            }
        }
        return null;
    }
    
    @Override
    @TypeInfo("Kind")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.declaration::TopLevelOrMemberDeclaration"))
    public <Kind extends ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration> Kind 
    getMember(@Ignore TypeDescriptor $reifiedKind, @Name("name") String name) {
        
        Predicates.Predicate predicate = Predicates.and(
                Predicates.isDeclarationNamed(name),
                Predicates.isDeclarationOfKind($reifiedKind)
        );
        
        return filteredMember($reifiedKind, predicate);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::OpenParameterisedType<ceylon.language.metamodel.declaration::InterfaceDeclaration>>")
    public Sequential<? extends ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.InterfaceDeclaration>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::OpenParameterisedType<ceylon.language.metamodel.declaration::ClassDeclaration>|ceylon.language::Null")
    public ceylon.language.metamodel.declaration.OpenParameterisedType<ceylon.language.metamodel.declaration.ClassDeclaration> getSuperclass() {
        checkInit();
        return superclass;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.declaration.TypeParameter> getTypeParameters() {
        checkInit();
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.declaration.TypeParameter getTypeParameter(@Name("name") String name) {
        checkInit();
        Iterator<? extends ceylon.language.metamodel.declaration.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.$get()){
            ceylon.language.metamodel.declaration.TypeParameter tp = (ceylon.language.metamodel.declaration.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @Ignore
    @Override
    public <Container, 
            Kind extends ceylon.language.metamodel.ClassOrInterface<? extends Object>>
        Sequential<? extends ceylon.language.metamodel.Type> memberApply$types(TypeDescriptor $reifiedContainer,
                                                                                      TypeDescriptor $reifiedKind){
        
        return (Sequential) empty_.$get();
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
                @Name("types") @Sequenced Sequential<? extends ceylon.language.metamodel.Type> types){
        // FIXME: check this
        AppliedClassOrInterfaceType<Container> containerType = (AppliedClassOrInterfaceType<Container>) Metamodel.getAppliedMetamodel($reifiedContainer);
        return getAppliedClassOrInterface($reifiedContainer, $reifiedKind, types, containerType);
    }

    <Type, Kind extends ceylon.language.metamodel.ClassOrInterface<? extends Object>>
    ceylon.language.metamodel.Member<Type, Kind> getAppliedClassOrInterface(TypeDescriptor $reifiedType, TypeDescriptor $reifiedKind, 
                                                                            Sequential<? extends ceylon.language.metamodel.Type> types,
                                                                            AppliedClassOrInterfaceType<Type> container){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        final ProducedType appliedType = declaration.getProducedReference(container.producedType, producedTypes).getType();
        return new AppliedMember<Type, Kind>($reifiedType, $reifiedKind, container){
            @Override
            protected Kind bindTo(Object instance) {
                return (Kind) (declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface 
                        ? new AppliedInterfaceType(null, appliedType)
                        : new AppliedClassType(null, null, appliedType, instance));
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

    FreeAttribute findValue(String name) {
        return this.<FreeAttribute>findDeclaration(name);
    }


    FreeClassOrInterface findType(String name) {
        return this.<FreeClassOrInterface>findDeclaration(name);
    }

    <T extends FreeTopLevelOrMemberDeclaration> T findDeclaration(String name) {
        checkInit();
        for(ceylon.language.metamodel.declaration.TopLevelOrMemberDeclaration decl : declarations){
            // in theory we can't have several members with the same name so no need to check the type
            // FIXME: interop and overloading
            if(decl.getName().equals(name))
                return (T) decl;
        }
        return null;
    }
    
    @Override
    @Ignore
    public java.lang.annotation.Annotation[] $getJavaAnnotations() {
        checkInit();
        return Metamodel.getJavaClass(declaration).getAnnotations();
    }
}
