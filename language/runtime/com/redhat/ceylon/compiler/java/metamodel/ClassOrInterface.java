package com.redhat.ceylon.compiler.java.metamodel;

import java.util.List;

import ceylon.language.Anything;
import ceylon.language.ArraySequence;
import ceylon.language.Sequential;
import ceylon.language.metamodel.Class;
import ceylon.language.metamodel.ClassOrInterface$impl;
import ceylon.language.metamodel.Interface;
import ceylon.language.metamodel.Member;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({@TypeParameter(value = "Type", variance = Variance.OUT)})
public class ClassOrInterface<Type>
    extends Declaration
    implements ceylon.language.metamodel.ClassOrInterface<Type> {

    @Ignore
    private static final TypeDescriptor $InterfacesTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.Interface.class, Anything.$TypeDescriptor);
    
    @Ignore
    protected TypeDescriptor $reifiedType;
    private volatile boolean initialised = false;
    private ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> superclass;
    private Sequential<ceylon.language.metamodel.Interface<? extends Object>> interfaces;

    public ClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterface$impl<Type> $ceylon$language$metamodel$ClassOrInterface$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void init(){
        this.$reifiedType = Util.getTypeDescriptorForDeclaration(declaration);
        com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration = (com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) this.declaration;
        ProducedType superType = declaration.getExtendedType();
        if(superType != null)
            this.superclass = (Class) Util.getMetamodel(superType);
        List<ProducedType> satisfiedTypes = declaration.getSatisfiedTypes();
        ceylon.language.metamodel.Interface[] interfaces = new ceylon.language.metamodel.Interface[satisfiedTypes.size()];
        int i=0;
        for(ProducedType pt : satisfiedTypes){
            interfaces[i++] = (Interface) Util.getMetamodel(pt);
        }
        this.interfaces = ArraySequence.instance($InterfacesTypeDescriptor, interfaces);
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
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Member<Subtype,Kind>>")
    public <Subtype, Kind extends ceylon.language.metamodel.Declaration, Annotation> Sequential<? extends Member<Subtype, Kind>> annotatedMembers(@Ignore TypeDescriptor $reifiedSubtype, @Ignore TypeDescriptor $reifiedKind, @Ignore TypeDescriptor $reifiedAnnotation) {
        checkInit();
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::Interface<ceylon.language::Anything>>")
    public Sequential<? extends ceylon.language.metamodel.Interface<? extends Object>> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Sequential<ceylon.language::Nothing>>")
    public ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> getSuperclass() {
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
    public TypeDescriptor $getType() {
        checkInit();
        return TypeDescriptor.klass(ClassOrInterface.class, $reifiedType);
    }
}
