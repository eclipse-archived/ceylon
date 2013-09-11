package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.SequenceBuilder;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.declaration.ClassOrInterfaceDeclaration$impl;
import ceylon.language.model.declaration.GenericDeclaration$impl;
import ceylon.language.model.declaration.OpenType;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeClassOrInterface
    extends FreeTopLevelOrMemberDeclaration
    implements ceylon.language.model.declaration.ClassOrInterfaceDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClassOrInterface.class);
    
    @Ignore
    static final TypeDescriptor $InterfacesTypeDescriptor = TypeDescriptor.klass(ceylon.language.model.declaration.OpenParameterisedType.class, ceylon.language.model.declaration.InterfaceDeclaration.$TypeDescriptor);

    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.model.declaration.FunctionDeclaration.class, Anything.$TypeDescriptor, Empty.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $AttributeTypeDescriptor = TypeDescriptor.klass(ceylon.language.model.declaration.ValueDeclaration.class, Anything.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $ClassOrInterfaceTypeDescriptor = TypeDescriptor.klass(ceylon.language.model.declaration.ClassOrInterfaceDeclaration.class, Anything.$TypeDescriptor);
    
    private volatile boolean initialised = false;
    private ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.ClassDeclaration> superclass;
    private Sequential<ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.InterfaceDeclaration>> interfaces;
    private Sequential<? extends ceylon.language.model.declaration.TypeParameter> typeParameters;

    private List<ceylon.language.model.declaration.TopLevelOrMemberDeclaration> declarations;

    private Sequential<? extends ceylon.language.model.declaration.OpenType> caseTypes;

    public FreeClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterfaceDeclaration$impl $ceylon$language$model$declaration$ClassOrInterfaceDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$model$declaration$GenericDeclaration$impl() {
        return null;
    }

    protected void init(){
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) this.declaration;
        
        ProducedType superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.ClassDeclaration>) Metamodel.getMetamodel(superType);
        
        List<ProducedType> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.InterfaceDeclaration>[] interfaces = new ceylon.language.model.declaration.OpenParameterisedType[satisfiedTypes.size()];
        int i=0;
        for(ProducedType pt : satisfiedTypes){
            interfaces[i++] = (ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.InterfaceDeclaration>) Metamodel.getMetamodel(pt);
        }
        this.interfaces = Util.sequentialInstance($InterfacesTypeDescriptor, interfaces);

        if(declaration.getCaseTypes() != null)
            this.caseTypes = Metamodel.getMetamodelSequential(declaration.getCaseTypes());
        else
            this.caseTypes = (Sequential)empty_.$get();

        this.typeParameters = Metamodel.getTypeParameters(declaration);
        
        List<com.redhat.ceylon.compiler.typechecker.model.Declaration> memberModelDeclarations = declaration.getMembers();
        i=0;
        this.declarations = new LinkedList<ceylon.language.model.declaration.TopLevelOrMemberDeclaration>();
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value
                    || memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.Method
                    || memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.TypeAlias
                    || memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)
                declarations.add(Metamodel.getOrCreateMetamodel(memberModelDeclaration));
        }
    }
    
    protected final void checkInit(){
        if(!initialised){
            synchronized(Metamodel.getLock()){
                if(!initialised){
                    init();
                    initialised = true;
                }
            }
        }
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.model.declaration::TopLevelOrMemberDeclaration"))
    public <Kind extends ceylon.language.model.declaration.TopLevelOrMemberDeclaration> Sequential<? extends Kind> 
    memberDeclarations(@Ignore TypeDescriptor $reifiedKind) {
        
        Predicates.Predicate<?> predicate = Predicates.isDeclarationOfKind($reifiedKind);
        
        return filteredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({
            @TypeParameter(value = "Kind", satisfies = "ceylon.language.model.declaration::TopLevelOrMemberDeclaration"),
            @TypeParameter("Annotation")
    })
    public <Kind extends ceylon.language.model.declaration.TopLevelOrMemberDeclaration, Annotation> Sequential<? extends Kind> 
    annotatedMemberDeclarations(@Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationOfKind($reifiedKind),
                Predicates.isDeclarationAnnotatedWith($reifiedAnnotation));
        
        return filteredMembers($reifiedKind, predicate);
    }

    private <Kind> Sequential<? extends Kind> filteredMembers(
            @Ignore TypeDescriptor $reifiedKind,
            Predicates.Predicate predicate) {
        if (predicate == Predicates.false_()) {
            return (Sequential<? extends Kind>)empty_.$get();
        }
        checkInit();
        SequenceBuilder<Kind> members = new SequenceBuilder<Kind>($reifiedKind, declarations.size());
        for(ceylon.language.model.declaration.TopLevelOrMemberDeclaration decl : declarations){
            if (predicate.accept(((FreeTopLevelOrMemberDeclaration)decl).declaration)) {
                members.append((Kind) decl);
            }
        }
        return members.getSequence();
    }
    
    private <Kind> Kind filteredMember(
            @Ignore TypeDescriptor $reifiedKind,
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
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.model.declaration::TopLevelOrMemberDeclaration"))
    public <Kind extends ceylon.language.model.declaration.TopLevelOrMemberDeclaration> Kind 
    getMemberDeclaration(@Ignore TypeDescriptor $reifiedKind, @Name("name") String name) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationNamed(name),
                Predicates.isDeclarationOfKind($reifiedKind)
        );
        
        return filteredMember($reifiedKind, predicate);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::OpenParameterisedType<ceylon.language.model.declaration::InterfaceDeclaration>>")
    public Sequential<? extends ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.InterfaceDeclaration>> getInterfaceDeclarations() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::OpenParameterisedType<ceylon.language.model.declaration::ClassDeclaration>|ceylon.language::Null")
    public ceylon.language.model.declaration.OpenParameterisedType<ceylon.language.model.declaration.ClassDeclaration> getSuperclassDeclaration() {
        checkInit();
        return superclass;
    }


    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.model.declaration.OpenType> getCaseTypes(){
        checkInit();
        return caseTypes;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.model.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.model.declaration.TypeParameter> getTypeParameterDeclarations() {
        checkInit();
        return typeParameters;
    }

    @Override
    public boolean getIsAlias(){
        return ((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)declaration).isAlias();
    }
    
    @Override
    public OpenType getOpenType() {
        return Metamodel.getMetamodel(((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)declaration).getType());
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.model.declaration.TypeParameter getTypeParameterDeclaration(@Name("name") String name) {
        return Metamodel.findDeclarationByName(getTypeParameterDeclarations(), name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Container, 
            Kind extends ceylon.language.model.ClassOrInterface<? extends Object>>
        ceylon.language.model.Member<Container, Kind> memberApply(TypeDescriptor $reifiedContainer,
                                                                      TypeDescriptor $reifiedKind){
        
        return this.<Container, Kind>memberApply($reifiedContainer,
                                                 $reifiedKind,
                                                 (Sequential)empty_.$get());
    }

    @TypeInfo("ceylon.language.model::Member<Container,Kind>")
    @TypeParameters({
        @TypeParameter(value = "Container"),
        @TypeParameter(value = "Kind", satisfies = "ceylon.language.model::ClassOrInterface<ceylon.language::Anything>")
    })
    @Override
    public <Container, 
            Kind extends ceylon.language.model.ClassOrInterface<? extends Object>>
        ceylon.language.model.Member<Container, Kind> memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedKind,
                @Name("types") @TypeInfo("ceylon.language::Sequential<ceylon.language.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.model.Type<?>> types){
        // FIXME: check this
        AppliedClassOrInterface<Container> containerType = (AppliedClassOrInterface<Container>) Metamodel.getAppliedMetamodel($reifiedContainer);
        return getAppliedClassOrInterface($reifiedContainer, $reifiedKind, types, containerType);
    }

    <Type, Kind extends ceylon.language.model.ClassOrInterface<? extends Object>>
    ceylon.language.model.Member<Type, Kind> getAppliedClassOrInterface(@Ignore TypeDescriptor $reifiedType, 
                                                                            @Ignore TypeDescriptor $reifiedKind, 
                                                                            Sequential<? extends ceylon.language.model.Type<?>> types,
                                                                            AppliedClassOrInterface<Type> container){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        ProducedReference producedReference = declaration.getProducedReference(container.producedType, producedTypes);
        final ProducedType appliedType = producedReference.getType();
        TypeDescriptor reifiedType = Metamodel.getTypeDescriptorForProducedType(appliedType);
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
            return new AppliedMemberInterface($reifiedType, reifiedType, appliedType);
        }else{
            TypeDescriptor reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), 
                    (com.redhat.ceylon.compiler.typechecker.model.Class)declaration, 
                    producedReference);
            return new AppliedMemberClass($reifiedType, reifiedType, reifiedArguments, appliedType);
        }
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    FreeFunction findMethod(String name) {
        FreeTopLevelOrMemberDeclaration decl = this.findDeclaration(null, name);
        return decl instanceof FreeFunction ? (FreeFunction)decl : null;
    }

    FreeAttribute findValue(String name) {
        FreeTopLevelOrMemberDeclaration decl = this.findDeclaration(null, name);
        return decl instanceof FreeAttribute ? (FreeAttribute)decl : null;
    }


    FreeClassOrInterface findType(String name) {
        FreeTopLevelOrMemberDeclaration decl = this.findDeclaration(null, name);
        return decl instanceof FreeClassOrInterface ? (FreeClassOrInterface)decl : null;
    }

    <T extends FreeTopLevelOrMemberDeclaration> T findDeclaration(@Ignore TypeDescriptor $reifiedT, String name) {
        checkInit();
        for(ceylon.language.model.declaration.TopLevelOrMemberDeclaration decl : declarations){
            // skip anonymous types which can't be looked up by name
            if(decl instanceof ceylon.language.model.declaration.ClassDeclaration
                    && ((ceylon.language.model.declaration.ClassDeclaration) decl).getAnonymous())
                continue;
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
