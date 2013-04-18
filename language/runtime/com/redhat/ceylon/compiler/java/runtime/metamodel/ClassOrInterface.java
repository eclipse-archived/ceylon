package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.Empty;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.metamodel.ClassOrInterface$impl;
import ceylon.language.metamodel.Parameterised$impl;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({@TypeParameter(value = "Type", variance = Variance.OUT)})
public class ClassOrInterface<Type>
    extends Declaration
    implements ceylon.language.metamodel.ClassOrInterface<Type> {

    @Ignore
    static final TypeDescriptor $InterfacesTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.InterfaceType.class, Anything.$TypeDescriptor);

    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.Function.class, Anything.$TypeDescriptor, Empty.$TypeDescriptor);;
    
    @Ignore
    protected TypeDescriptor $reifiedType;
    private volatile boolean initialised = false;
    private ceylon.language.metamodel.ClassType<? extends Object, ? super Sequential<? extends Object>> superclass;
    private Sequential<ceylon.language.metamodel.InterfaceType<? extends Object>> interfaces;
    private Sequential<ceylon.language.metamodel.TypeParameter> typeParameters;

    private Sequential<ceylon.language.metamodel.Member<Type, ceylon.language.metamodel.Function<? extends Object, ? super Sequential<? extends Object>>>> functions;

    public ClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$metamodel$ClassOrInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Parameterised$impl $ceylon$language$metamodel$Parameterised$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void init(){
        // FIXME: should be on ProducedType to get something fully reified
        this.$reifiedType = Metamodel.getTypeDescriptorForDeclaration(declaration);
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) this.declaration;
        
        ProducedType superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (ceylon.language.metamodel.ClassType) Metamodel.getMetamodel(superType);
        
        List<ProducedType> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.metamodel.InterfaceType[] interfaces = new ceylon.language.metamodel.InterfaceType[satisfiedTypes.size()];
        int i=0;
        for(ProducedType pt : satisfiedTypes){
            interfaces[i++] = (ceylon.language.metamodel.InterfaceType) Metamodel.getMetamodel(pt);
        }
        this.interfaces = (Sequential)Util.sequentialInstance($InterfacesTypeDescriptor, interfaces);
        
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.metamodel.TypeParameter[] typeParametersArray = new ceylon.language.metamodel.TypeParameter[typeParameters.size()];
        i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.TypeParameter(tp);
        }
        this.typeParameters = (Sequential)Util.sequentialInstance(ceylon.language.metamodel.TypeParameter.$TypeDescriptor, typeParametersArray);
        
        List<com.redhat.ceylon.compiler.typechecker.model.Declaration> memberModelDeclarations = declaration.getMembers();
        i=0;
        List<ceylon.language.metamodel.Member<? extends Object, Function>> functions = new LinkedList<>();
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            if(memberModelDeclaration instanceof Method){
                functions.add(new Member(this, new Function((Method) memberModelDeclaration)));
            }
        }
        this.functions = (Sequential)Util.sequentialInstance($FunctionTypeDescriptor, functions.toArray(new ceylon.language.metamodel.Member[functions.size()]));
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
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Member<Subtype,Kind>>")
    public <Subtype, Kind extends ceylon.language.metamodel.Declaration> Sequential<? extends Member<Subtype, Kind>> members(@Ignore TypeDescriptor $reifiedSubtype, @Ignore TypeDescriptor $reifiedKind) {
        checkInit();
        if($reifiedKind instanceof TypeDescriptor.Class){
            TypeDescriptor.Class klass = (TypeDescriptor.Class) $reifiedKind;
            if(klass.getKlass() == ceylon.language.metamodel.Function.class){
                return (Sequential) functions;
            }
        }
        throw new RuntimeException("Not supported yet");
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Member<Subtype,Kind>>")
    public <Subtype, Kind extends ceylon.language.metamodel.Declaration, Annotation> Sequential<? extends Member<Subtype, Kind>> annotatedMembers(@Ignore TypeDescriptor $reifiedSubtype, @Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        checkInit();
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::InterfaceType<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.metamodel.InterfaceType<? extends Object>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassType<ceylon.language::Anything,ceylon.language::Sequential<ceylon.language::Nothing>>|ceylon.language::Null")
    public ceylon.language.metamodel.ClassType<? extends Object, ? super Sequential<? extends Object>> getSuperclass() {
        checkInit();
        return superclass;
    }

    @Override
    public boolean supertypeOf(@Name("type") @TypeInfo("ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything>") 
                               ceylon.language.metamodel.ClassOrInterface<? extends Object> type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean subtypeOf(@Name("type") @TypeInfo("ceylon.language.metamodel::ClassOrInterface<ceylon.language::Anything>") 
                             ceylon.language.metamodel.ClassOrInterface<? extends Object> type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean typeOf(@Name("instance") @TypeInfo("ceylon.language::Anything") Object instance) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::TypeParameter>")
    public Sequential<? extends ceylon.language.metamodel.TypeParameter> getTypeParameters() {
        checkInit();
        return typeParameters;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::TypeParameter|ceylon.language::Null")
    public ceylon.language.metamodel.TypeParameter getTypeParameter(@Name("name") String name) {
        checkInit();
        Iterator<? extends ceylon.language.metamodel.TypeParameter> iterator = typeParameters.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            ceylon.language.metamodel.TypeParameter tp = (ceylon.language.metamodel.TypeParameter) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    @Override
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(ClassOrInterface.class, $reifiedType);
    }
    
    @Ignore
    TypeDescriptor $getReifiedType(){
        checkInit();
        return $reifiedType;
    }
}
