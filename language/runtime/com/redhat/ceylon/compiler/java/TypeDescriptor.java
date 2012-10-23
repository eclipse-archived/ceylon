package com.redhat.ceylon.compiler.java;

import java.lang.reflect.Type;

public class TypeDescriptor {

    public static final TypeDescriptor[] NO_TYPES = new TypeDescriptor[0];

    public static final TypeDescriptor BottomType = new Bottom();
    
    public static class Class extends TypeDescriptor {
        private Type klass;
        private TypeDescriptor[] typeArguments;

        public Class(Type klass, TypeDescriptor... params){
            this.klass = klass;
            this.typeArguments = params;
        }

        public Type getKlass() {
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
    }
    
    private static class Bottom extends TypeDescriptor {
        // FIXME: equals?
    }
    
    private abstract static class Composite extends TypeDescriptor {
        private TypeDescriptor[] members;

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
