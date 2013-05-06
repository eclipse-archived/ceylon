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
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
public abstract class ClassOrInterface
    extends Declaration
    implements ceylon.language.metamodel.ClassOrInterface {

    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(ClassOrInterface.class);
    
    @Ignore
    static final TypeDescriptor $InterfacesTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.InterfaceType.class, Anything.$TypeDescriptor);

    @Ignore
    private static final TypeDescriptor $FunctionTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.Function.class, Anything.$TypeDescriptor, Empty.$TypeDescriptor);
    @Ignore
    private static final TypeDescriptor $ValueTypeDescriptor = TypeDescriptor.klass(ceylon.language.metamodel.Value.class, Anything.$TypeDescriptor);
    
    private volatile boolean initialised = false;
    private ceylon.language.metamodel.ClassType superclass;
    private Sequential<ceylon.language.metamodel.InterfaceType> interfaces;
    private Sequential<ceylon.language.metamodel.TypeParameter> typeParameters;

    private Sequential<ceylon.language.metamodel.Member<? extends Object, ceylon.language.metamodel.Function>> functions;
    private Sequential<ceylon.language.metamodel.Member<? extends Object, ceylon.language.metamodel.Value>> values;

    public ClassOrInterface(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration) {
        super(declaration);
    }

    @Override
    @Ignore
    public ClassOrInterface$impl $ceylon$language$metamodel$ClassOrInterface$impl() {
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
        List<ceylon.language.metamodel.Member<? extends Object, Function>> functions = new LinkedList<ceylon.language.metamodel.Member<? extends Object, Function>>();
        List<ceylon.language.metamodel.Member<? extends Object, Value>> values = new LinkedList<ceylon.language.metamodel.Member<? extends Object, Value>>();
        for(com.redhat.ceylon.compiler.typechecker.model.Declaration memberModelDeclaration : memberModelDeclarations){
            if(memberModelDeclaration instanceof Method){
                functions.add(new Member(this, new Function((Method) memberModelDeclaration)));
            }else if(memberModelDeclaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value){
                values.add(new Member(this, new Value((MethodOrValue) memberModelDeclaration)));
            }
        }

        TypeDescriptor functionMemberTD = TypeDescriptor.klass(ceylon.language.metamodel.Member.class, $TypeDescriptor, $FunctionTypeDescriptor);
        this.functions = (Sequential)Util.sequentialInstance(functionMemberTD, functions.toArray(new ceylon.language.metamodel.Member[functions.size()]));

        TypeDescriptor valueMemberTD = TypeDescriptor.klass(ceylon.language.metamodel.Member.class, $TypeDescriptor, $ValueTypeDescriptor);
        this.values = (Sequential)Util.sequentialInstance(valueMemberTD, values.toArray(new ceylon.language.metamodel.Member[values.size()]));
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
            if(klass.getKlass() == ceylon.language.metamodel.Value.class){
                return (Sequential) values;
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
    @TypeInfo("ceylon.language::Sequential<ceylon.language.metamodel::InterfaceType>")
    public Sequential<? extends ceylon.language.metamodel.InterfaceType> getInterfaces() {
        checkInit();
        return interfaces;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassType|ceylon.language::Null")
    public ceylon.language.metamodel.ClassType getSuperclass() {
        checkInit();
        return superclass;
    }

    @Override
    public boolean supertypeOf(@Name("type") @TypeInfo("ceylon.language.metamodel::ClassOrInterface") 
                               ceylon.language.metamodel.ClassOrInterface type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean subtypeOf(@Name("type") @TypeInfo("ceylon.language.metamodel::ClassOrInterface") 
                             ceylon.language.metamodel.ClassOrInterface type) {
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
        return $TypeDescriptor;
    }

    Function findMethod(String name) {
        checkInit();
        Iterator iterator = functions.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            Member member = (Member) it;
            Function f = (Function) member.declaration;
            if(f.getName().equals(name))
                return f;
        }
        return null;
    }

    Value findValue(String name) {
        checkInit();
        Iterator iterator = values.iterator();
        Object it;
        while((it = iterator.next()) != finished_.getFinished$()){
            Member member = (Member) it;
            Value f = (Value) member.declaration;
            if(f.getName().equals(name))
                return f;
        }
        return null;
    }
}
