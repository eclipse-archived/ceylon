package com.redhat.ceylon.compiler.java.runtime.model;

import java.util.ArrayList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.AssertionException;
import ceylon.language.Basic;
import ceylon.language.Identifiable;
import ceylon.language.Null;

import com.redhat.ceylon.compiler.java.language.BooleanArray;
import com.redhat.ceylon.compiler.java.language.ByteArray;
import com.redhat.ceylon.compiler.java.language.CharArray;
import com.redhat.ceylon.compiler.java.language.DoubleArray;
import com.redhat.ceylon.compiler.java.language.FloatArray;
import com.redhat.ceylon.compiler.java.language.IntArray;
import com.redhat.ceylon.compiler.java.language.LongArray;
import com.redhat.ceylon.compiler.java.language.ObjectArray;
import com.redhat.ceylon.compiler.java.language.ShortArray;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Util;

public abstract class TypeDescriptor {

    public static final TypeDescriptor NothingType = new Nothing();

    //
    // Methods

    public abstract ProducedType toProducedType(RuntimeModuleManager moduleManager);
    
    public abstract java.lang.Class<?> getArrayElementClass();
    
    
    public abstract boolean containsNull();
	

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

		@Override
        public java.lang.Class<?> getArrayElementClass() {
			if (klass==Null.class ||
				klass==Object.class ||
				klass==Anything.class ||
				klass==Basic.class ||
				klass==Identifiable.class) {
				return java.lang.Object.class;
			}
			if (klass==Exception.class) {
				return java.lang.Throwable.class;
			}
			if (klass==ObjectArray.class) {
				return java.lang.Object[].class;
			}
			if (klass==BooleanArray.class) {
				return boolean[].class;
			}
			if (klass==LongArray.class) {
				return long[].class;
			}
			if (klass==IntArray.class) {
				return int[].class;
			}
			if (klass==ShortArray.class) {
				return short[].class;
			}
			if (klass==ByteArray.class) {
				return byte[].class;
			}
			if (klass==DoubleArray.class) {
				return double[].class;
			}
			if (klass==FloatArray.class) {
				return float[].class;
			}
			if (klass==CharArray.class) {
				return char[].class;
			}
	        return klass;
        }

		@Override
        public boolean containsNull() {
	        return klass==Null.class;
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

		@Override
        public java.lang.Class<?> getArrayElementClass() {
	        return member.getArrayElementClass();
        }

		@Override
        public boolean containsNull() {
	        return false;
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
        
        @Override
        public String toString() {
            return "ceylon.language.Nothing";
        }
        
		@Override
        public java.lang.Class<?> getArrayElementClass() {
	        return java.lang.Object.class;
        }

		@Override
        public boolean containsNull() {
	        return false;
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
                Util.addToUnion(caseTypes,member.toProducedType(moduleManager));
            ret.setCaseTypes(caseTypes);
            return ret.getType();
        }

		@Override
        public java.lang.Class<?> getArrayElementClass() {
			java.lang.Class<?> result = null;
			for (TypeDescriptor td: members) {
	        	if (td instanceof Nothing || 
	        		td instanceof Class && td.containsNull()) {
	        		continue;
	        	}
	        	java.lang.Class<?> c = td.getArrayElementClass();
	        	if (result==null) {
	        		result = c;
	        	}
	        	else if (result!=c) {
	        		return java.lang.Object.class;
	        	}
	        }
	        return result==null ? 
	        		java.lang.Object.class : result;
        }

		@Override
        public boolean containsNull() {
			for (TypeDescriptor td: members) {
				if (td.containsNull()) return true;
			}
	        return false;
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
            Unit unit = moduleManager.getModelLoader().getUnit();
			IntersectionType ret = new IntersectionType(unit);
            ArrayList<ProducedType> satisfiedTypes = new ArrayList<ProducedType>(members.length);
            for(TypeDescriptor member : members)
            	Util.addToIntersection(satisfiedTypes, member.toProducedType(moduleManager), unit);
            ret.setSatisfiedTypes(satisfiedTypes);
            return ret.canonicalize().getType();
        }
        
		@Override
        public java.lang.Class<?> getArrayElementClass() {
			java.lang.Class<?> result = null;
			for (TypeDescriptor td: members) {
				java.lang.Class<?> c = td.getArrayElementClass();
	        	if (result==null) {
	        		result = c;
	        	}
	        	else if (result.isAssignableFrom(c)) {
	        		result = c;
	        	}
	        	else if (c.isAssignableFrom(result)) {
	        		//do nothing
	        	}
	        	else if (result!=c) {
	        		return java.lang.Object.class;
	        	}
	        }
	        return result==null ? 
	        		java.lang.Object.class : result;
        }

		@Override
        public boolean containsNull() {
			for (TypeDescriptor td: members) {
				if (!td.containsNull()) return false;
			}
	        return true;
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
            throw new AssertionException("members can't be null or empty");
        return new Union(members);
    }
    
    public static TypeDescriptor intersection(TypeDescriptor... members){
        if(members == null || members.length == 0)
            throw new AssertionException("members can't be null or empty");
        return new Intersection(members);
    }
}
