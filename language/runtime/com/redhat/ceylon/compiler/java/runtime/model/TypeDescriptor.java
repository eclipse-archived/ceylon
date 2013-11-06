package com.redhat.ceylon.compiler.java.runtime.model;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;

public abstract class TypeDescriptor {

    public static final TypeDescriptor NothingType = new Nothing();

    //
    // Methods

    public abstract ProducedType toProducedType(RuntimeModuleManager moduleManager);

    //
    // Subtypes
    
    public static class Class extends TypeDescriptor {
        private java.lang.Class<?> klass;
        private TypeDescriptor[] typeArguments;

        public Class(java.lang.Class<?> klass, TypeDescriptor[] typeArguments){
            this.klass = klass;
            this.typeArguments = typeArguments;
        }

        public java.lang.Class<?> getKlass() {
            return klass;
        }

        public TypeDescriptor[] getTypeArguments() {
            return typeArguments;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null || obj instanceof Class == false)
                return false;
            Class other = (Class) obj;
            if(klass != other.klass)
                return false;
            if(typeArguments.length != other.typeArguments.length)
                return false;
            for(int i=0;i<typeArguments.length;i++){
                if(!typeArguments[i].equals(other.typeArguments[i]))
                    return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(klass);
            if(typeArguments.length > 0){
                b.append("<");
                for(int i=0;i<typeArguments.length;i++){
                    if(i>0)
                        b.append(",");
                    b.append(typeArguments[i]);
                }
                b.append(">");
            }
            return b.toString();
        }

        @Override
        public ProducedType toProducedType(RuntimeModuleManager moduleManager){
            return toProducedType(null, moduleManager);
        }
        
        public ProducedType toProducedType(ProducedType qualifyingType, RuntimeModuleManager moduleManager){
            // FIXME: is this really enough?
            String typeName = klass.getName();
            Module module = moduleManager.findModuleForClass(klass);
            TypeDeclaration decl = (TypeDeclaration) moduleManager.getModelLoader().getDeclaration(module, typeName, DeclarationType.TYPE);
            List<ProducedType> typeArgs = new ArrayList<ProducedType>(typeArguments.length);
            for(TypeDescriptor typeArg : typeArguments){
                typeArgs.add(typeArg.toProducedType(moduleManager));
            }
            return decl.getProducedType(qualifyingType, typeArgs);
        }
    }

    public static class Member extends TypeDescriptor {

        private TypeDescriptor container;
        private TypeDescriptor member;

        public Member(TypeDescriptor container, TypeDescriptor member) {
            this.member = member;
            this.container = container;
        }

        public TypeDescriptor getContainer() {
            return container;
        }

        public TypeDescriptor getMember() {
            return member;
        }
        
        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null || obj instanceof Member == false)
                return false;
            Member other = (Member) obj;
            return container.equals(other.container)
                    && member.equals(other.member);
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(container);
            b.append(".");
            b.append(member);
            return b.toString();
        }

        @Override
        public ProducedType toProducedType(RuntimeModuleManager moduleManager) {
            return ((Class)member).toProducedType(container.toProducedType(moduleManager), moduleManager);
        }
    }
    
    private static class Nothing extends TypeDescriptor {
        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public ProducedType toProducedType(RuntimeModuleManager moduleManager) {
            return new NothingType(moduleManager.getModelLoader().getUnit()).getType();
        }
    }
    
    private abstract static class Composite extends TypeDescriptor {
        protected final TypeDescriptor[] members;

        public Composite(TypeDescriptor[] members) {
            this.members = members;
        }

        public TypeDescriptor[] getMembers() {
            return members;
        }

        protected boolean equals(Composite other) {
            if(members.length != other.members.length)
                return false;
            for(int i=0;i<members.length;i++){
                if(!members[i].equals(other.members[i]))
                    return false;
            }
            return true;
        }

        protected String toString(String sep) {
            StringBuilder b = new StringBuilder();
            if(members.length > 0){
                for(int i=0;i<members.length;i++){
                    if(i>0)
                        b.append(sep);
                    b.append(members[i]);
                }
            }
            return b.toString();
        }
    }

    public static class Union extends Composite {

        public Union(TypeDescriptor[] members) {
            super(members);
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null || obj instanceof Union == false)
                return false;
            return super.equals((Union)obj);
        }

        @Override
        public String toString() {
            return super.toString("|");
        }

        @Override
        public ProducedType toProducedType(RuntimeModuleManager moduleManager) {
            UnionType ret = new UnionType(moduleManager.getModelLoader().getUnit());
            ArrayList<ProducedType> caseTypes = new ArrayList<ProducedType>(members.length);
            for(TypeDescriptor member : members)
                caseTypes.add(member.toProducedType(moduleManager));
            ret.setCaseTypes(caseTypes);
            return ret.getType();
        }
    }

    public static class Intersection extends Composite {

        public Intersection(TypeDescriptor[] members) {
            super(members);
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null || obj instanceof Intersection == false)
                return false;
            return super.equals((Intersection)obj);
        }

        @Override
        public String toString() {
            return super.toString("&");
        }

        @Override
        public ProducedType toProducedType(RuntimeModuleManager moduleManager) {
            IntersectionType ret = new IntersectionType(moduleManager.getModelLoader().getUnit());
            for(TypeDescriptor member : members)
                ret.getSatisfiedTypes().add(member.toProducedType(moduleManager));
            return ret.getType();
        }
    }

    //
    // Factory methods
    
    public static TypeDescriptor member(TypeDescriptor container, TypeDescriptor member){
        return new Member(container, member);
    }
    
    public static TypeDescriptor klass(java.lang.Class<?> klass, TypeDescriptor... typeArguments) {
        return new Class(klass, typeArguments);
    }

    public static TypeDescriptor union(TypeDescriptor... members){
        if(members == null || members.length == 0)
            throw new IllegalArgumentException("members can't be null or empty");
        return new Union(members);
    }

    public static TypeDescriptor intersection(TypeDescriptor... members){
        if(members == null || members.length == 0)
            throw new IllegalArgumentException("members can't be null or empty");
        return new Intersection(members);
    }
}
