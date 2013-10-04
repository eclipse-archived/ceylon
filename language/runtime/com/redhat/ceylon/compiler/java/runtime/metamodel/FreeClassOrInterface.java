package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.SequenceBuilder;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.ClassOrInterfaceDeclaration$impl;
import ceylon.language.meta.declaration.GenericDeclaration$impl;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.Member;

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
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class FreeClassOrInterface
    extends FreeNestableDeclaration
    implements ceylon.language.meta.declaration.ClassOrInterfaceDeclaration, AnnotationBearing {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeClassOrInterface.class);
    
    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.meta.declaration.FunctionDeclaration.class, Anything.$TypeDescriptor, Empty.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $AttributeTypeDescriptor = TypeDescriptor.klass(ceylon.language.meta.declaration.ValueDeclaration.class, Anything.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $ClassOrInterfaceTypeDescriptor = TypeDescriptor.klass(ceylon.language.meta.declaration.ClassOrInterfaceDeclaration.class, Anything.$TypeDescriptor);
    
    private volatile boolean initialised = false;
    private ceylon.language.meta.declaration.OpenClassType superclass;
    private Sequential<ceylon.language.meta.declaration.OpenInterfaceType> interfaces;
    private Sequential<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters;

    private List<ceylon.language.meta.declaration.NestableDeclaration> declarations;

    private Sequential<? extends ceylon.language.meta.declaration.OpenType> caseTypes;

    public FreeClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterfaceDeclaration$impl $ceylon$language$meta$declaration$ClassOrInterfaceDeclaration$impl() {
        return null;
    }

    @Override
    @Ignore
    public GenericDeclaration$impl $ceylon$language$meta$declaration$GenericDeclaration$impl() {
        return null;
    }

    protected void init(){
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) this.declaration;
        
        ProducedType superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (ceylon.language.meta.declaration.OpenClassType) Metamodel.getMetamodel(superType);
        
        List<ProducedType> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.meta.declaration.OpenInterfaceType[] interfaces = new ceylon.language.meta.declaration.OpenInterfaceType[satisfiedTypes.size()];
        int i=0;
        for(ProducedType pt : satisfiedTypes){
            interfaces[i++] = (ceylon.language.meta.declaration.OpenInterfaceType) Metamodel.getMetamodel(pt);
        }
        this.interfaces = Util.sequentialInstance(ceylon.language.meta.declaration.OpenInterfaceType.$TypeDescriptor, interfaces);

        if(declaration.getCaseTypes() != null)
            this.caseTypes = Metamodel.getMetamodelSequential(declaration.getCaseTypes());
        else
            this.caseTypes = (Sequential)empty_.get_();

        this.typeParameters = Metamodel.getTypeParameters(declaration);
        
        List<com.redhat.ceylon.compiler.typechecker.model.Declaration> memberModelDeclarations = declaration.getMembers();
        i=0;
        this.declarations = new LinkedList<ceylon.language.meta.declaration.NestableDeclaration>();
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
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Sequential<? extends Kind> 
    memberDeclarations(@Ignore TypeDescriptor $reifiedKind) {
        
        Predicates.Predicate<?> predicate = Predicates.isDeclarationOfKind($reifiedKind);
        
        return filteredMembers($reifiedKind, predicate);
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<Kind>")
    @TypeParameters({
            @TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"),
            @TypeParameter("Annotation")
    })
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration, Annotation> Sequential<? extends Kind> 
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
            return (Sequential<? extends Kind>)empty_.get_();
        }
        checkInit();
        SequenceBuilder<Kind> members = new SequenceBuilder<Kind>($reifiedKind, declarations.size());
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declarations){
            if (predicate.accept(((FreeNestableDeclaration)decl).declaration)) {
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
    @TypeParameters(@TypeParameter(value = "Kind", satisfies = "ceylon.language.meta.declaration::NestableDeclaration"))
    public <Kind extends ceylon.language.meta.declaration.NestableDeclaration> Kind 
    getMemberDeclaration(@Ignore TypeDescriptor $reifiedKind, @Name("name") String name) {
        
        Predicates.Predicate<?> predicate = Predicates.and(
                Predicates.isDeclarationNamed(name),
                Predicates.isDeclarationOfKind($reifiedKind)
        );
        
        return filteredMember($reifiedKind, predicate);
    }
    
    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenInterfaceType>")
    public Sequential<? extends ceylon.language.meta.declaration.OpenInterfaceType> getSatisfiedTypes() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::OpenClassType|ceylon.language::Null")
    public ceylon.language.meta.declaration.OpenClassType getExtendedType() {
        checkInit();
        return superclass;
    }


    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::OpenType>")
    @Override
    public ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getCaseTypes(){
        checkInit();
        return caseTypes;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.declaration::TypeParameter>")
    public Sequential<? extends ceylon.language.meta.declaration.TypeParameter> getTypeParameterDeclarations() {
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
    @TypeInfo("ceylon.language.meta.declaration::TypeParameter|ceylon.language::Null")
    public ceylon.language.meta.declaration.TypeParameter getTypeParameterDeclaration(@Name("name") String name) {
        return Metamodel.findDeclarationByName(getTypeParameterDeclarations(), name);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Override
    public <Type> ceylon.language.meta.model.ClassOrInterface<Type> apply(@Ignore TypeDescriptor $reifiedType){
        return apply($reifiedType, (Sequential)empty_.get_());
    }

    @SuppressWarnings("unchecked")
    @Override
    @TypeInfo("ceylon.language.meta.model::ClassOrInterface<Type>")
    @TypeParameters({
        @TypeParameter("Type"),
    })
    public <Type extends Object> ceylon.language.meta.model.ClassOrInterface<Type> apply(@Ignore TypeDescriptor $reifiedType,
            @Name("typeArguments") @TypeInfo("ceylon.language::Sequential<ceylon.language.meta.model::Type<ceylon.language::Anything>>") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(!getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a member declaration with no container type: use memberApply");
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(typeArguments);
        Metamodel.checkTypeArguments(null, declaration, producedTypes);
        com.redhat.ceylon.compiler.typechecker.model.ProducedReference appliedType = declaration.getProducedReference(null, producedTypes);
        Metamodel.checkReifiedTypeArgument("apply", "ClassOrInterface<$1>", Variance.OUT, appliedType.getType(), $reifiedType);
        return (ClassOrInterface<Type>) Metamodel.getAppliedMetamodel(appliedType.getType());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Ignore
    @Override
    public <Container, Type extends Object>
        java.lang.Object memberApply(TypeDescriptor $reifiedContainer,
                                     TypeDescriptor $reifiedType,
                                     ceylon.language.meta.model.Type<? extends Container> containerType){
        
        return this.<Container, Type>memberApply($reifiedContainer,
                                                 $reifiedType,
                                                 containerType,
                                                 (Sequential)empty_.get_());
    }

    @TypeInfo("ceylon.language.meta.model::Member<Container,ceylon.language.meta.model::ClassOrInterface<Type>>&ceylon.language.meta.model::ClassOrInterface<Type>")
    @TypeParameters({
        @TypeParameter("Container"),
        @TypeParameter("Type"),
    })
    @Override
    public <Container, Type extends Object>
        java.lang.Object memberApply(
                @Ignore TypeDescriptor $reifiedContainer,
                @Ignore TypeDescriptor $reifiedType,
                @Name("containerType") ceylon.language.meta.model.Type<? extends Container> containerType,
                @Name("typeArguments") @Sequenced Sequential<? extends ceylon.language.meta.model.Type<?>> typeArguments){
        if(getToplevel())
            // FIXME: change type
            throw new RuntimeException("Cannot apply a toplevel declaration to a container type: use apply");

        ceylon.language.meta.model.Member<? extends Container, ceylon.language.meta.model.ClassOrInterface<?>> member 
            = getAppliedClassOrInterface(null, null, typeArguments, containerType);
        
        // This is all very ugly but we're trying to make it cheaper and friendlier than just checking the full type and showing
        // implementation types to the user, such as AppliedMemberClass
        TypeDescriptor actualReifiedContainer;
        if(member instanceof AppliedMemberClass)
            actualReifiedContainer = ((AppliedMemberClass)member).$reifiedContainer;
        else
            actualReifiedContainer = ((AppliedMemberInterface)member).$reifiedContainer;
        ProducedType actualType = Metamodel.getModel((ceylon.language.meta.model.Type<?>) member);
        Metamodel.checkReifiedTypeArgument("memberApply", "Member<$1,ClassOrInterface<$2>>&ClassOrInterface<$2>", 
                Variance.IN, Metamodel.getProducedType(actualReifiedContainer), $reifiedContainer, 
                Variance.OUT, actualType, $reifiedType);
        return member;
    }

    @SuppressWarnings("unchecked")
    <Container, Kind extends ceylon.language.meta.model.ClassOrInterface<? extends Object>>
    ceylon.language.meta.model.Member<Container, Kind> getAppliedClassOrInterface(@Ignore TypeDescriptor $reifiedContainer, 
                                                                        @Ignore TypeDescriptor $reifiedKind, 
                                                                        Sequential<? extends ceylon.language.meta.model.Type<?>> types,
                                                                        ceylon.language.meta.model.Type<Container> container){
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = Metamodel.getProducedTypes(types);
        ProducedType qualifyingType = Metamodel.getModel(container);
        Metamodel.checkQualifyingType(qualifyingType, declaration);
        Metamodel.checkTypeArguments(qualifyingType, declaration, producedTypes);
        ProducedReference producedReference = declaration.getProducedReference(qualifyingType, producedTypes);
        final ProducedType appliedType = producedReference.getType();
        return (Member<Container, Kind>) Metamodel.getAppliedMetamodel(appliedType);
    }

    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }

    FreeFunction findMethod(String name) {
        FreeNestableDeclaration decl = this.findDeclaration(null, name);
        return decl instanceof FreeFunction ? (FreeFunction)decl : null;
    }

    FreeAttribute findValue(String name) {
        FreeNestableDeclaration decl = this.findDeclaration(null, name);
        return decl instanceof FreeAttribute ? (FreeAttribute)decl : null;
    }


    FreeClassOrInterface findType(String name) {
        FreeNestableDeclaration decl = this.findDeclaration(null, name);
        return decl instanceof FreeClassOrInterface ? (FreeClassOrInterface)decl : null;
    }

    <T extends FreeNestableDeclaration> T findDeclaration(@Ignore TypeDescriptor $reifiedT, String name) {
        checkInit();
        for(ceylon.language.meta.declaration.NestableDeclaration decl : declarations){
            // skip anonymous types which can't be looked up by name
            if(decl instanceof ceylon.language.meta.declaration.ClassDeclaration
                    && ((ceylon.language.meta.declaration.ClassDeclaration) decl).getAnonymous())
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
