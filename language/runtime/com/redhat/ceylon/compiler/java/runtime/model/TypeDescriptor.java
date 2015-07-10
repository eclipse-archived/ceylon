package com.redhat.ceylon.compiler.java.runtime.model;

import static com.redhat.ceylon.compiler.java.Util.isBasic;
import static com.redhat.ceylon.compiler.java.Util.isIdentifiable;
import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getProducedType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ceylon.language.Anything;
import ceylon.language.AssertionError;
import ceylon.language.Basic;
import ceylon.language.Empty;
import ceylon.language.Identifiable;
import ceylon.language.Null;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.null_;

import com.redhat.ceylon.compiler.java.language.BooleanArray;
import com.redhat.ceylon.compiler.java.language.ByteArray;
import com.redhat.ceylon.compiler.java.language.CharArray;
import com.redhat.ceylon.compiler.java.language.DoubleArray;
import com.redhat.ceylon.compiler.java.language.FloatArray;
import com.redhat.ceylon.compiler.java.language.IntArray;
import com.redhat.ceylon.compiler.java.language.LongArray;
import com.redhat.ceylon.compiler.java.language.ObjectArray;
import com.redhat.ceylon.compiler.java.language.ShortArray;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.model.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.model.loader.model.FunctionOrValueInterface;
import com.redhat.ceylon.model.loader.model.LocalDeclarationContainer;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

public abstract class TypeDescriptor {

    public static final TypeDescriptor NothingType = new Nothing();
    private static final Variance[] NO_VARIANCE = new Variance[0];

    //
    // Methods

    public abstract Type toType(RuntimeModuleManager moduleManager);
    
    public abstract java.lang.Class<?> getArrayElementClass();
    
    public abstract boolean containsNull();
    
    public abstract boolean equals(Object other);
    
    public abstract int hashCode();
    
    @Override
    public final String toString() {
        StringBuilder b = new StringBuilder();
        stringTo(b);
        return b.toString();
    }
    
    protected abstract void stringTo(StringBuilder sb);
    
    /**
     * Returns a hashcode for the given elements 
     * computed so that the order of the elements doesn't matter
     */
    static int unorderedHashCode(TypeDescriptor[] array) {
        int hash = 0;
        for (int i = 0; i < array.length; i++) {
            hash ^= array[i].hashCode();
        }
        return hash;
    }
	

    //
    // Subtypes

    public static interface QualifiableTypeDescriptor {
        public Type toProducedType(Type qualifyingType, RuntimeModuleManager moduleManager);
    }
    
    public static abstract class Generic extends TypeDescriptor {
        protected final TypeDescriptor[] typeArguments;
        protected final Variance[] useSiteVariance;

        public Generic(Variance[] useSiteVariance, TypeDescriptor[] typeArguments){
            this.typeArguments = typeArguments;
            if (useSiteVariance != NO_VARIANCE
                    && useSiteVariance.length != typeArguments.length) {
                throw new IllegalArgumentException("Undefined variance");
            }
            this.useSiteVariance = useSiteVariance;
        }

        public TypeDescriptor[] getTypeArguments() {
            return typeArguments;
        }
        
        public boolean isGeneric() {
            return getTypeArguments().length>0;
        }

        protected boolean equals(Generic other) {
            return Arrays.equals(typeArguments, other.typeArguments)
                    && Arrays.equals(useSiteVariance, other.useSiteVariance);
        }
        /** Appends the type arguments */
        protected void stringTo(StringBuilder b) {
            if(typeArguments.length > 0){
                b.append("<");
                for(int i=0;i<typeArguments.length;i++){
                    if(i>0) {
                        b.append(",");
                    }
                    if (useSiteVariance != NO_VARIANCE) {
                        b.append(useSiteVariance[i].getPretty()).append(' ');
                    }
                    typeArguments[i].stringTo(b);
                }
                b.append(">");
            }
        }
        
        protected Type applyUseSiteVariance(TypeDeclaration decl, Type type) {
            // apply use site variance if required
            if(useSiteVariance.length != 0 && 
                    decl instanceof com.redhat.ceylon.model.typechecker.model.Generic){
                List<TypeParameter> typeParameters = 
                        ((com.redhat.ceylon.model.typechecker.model.Generic)decl).getTypeParameters();
                int i = 0;
                for(TypeParameter typeParameter : typeParameters){
                    // bail if we have more type parameters than provided use site variance
                    if(i >= useSiteVariance.length)
                        break;
                    switch(useSiteVariance[i]){
                    case IN:
                        type.setVariance(typeParameter, SiteVariance.IN);
                        break;
                    case OUT:
                        type.setVariance(typeParameter, SiteVariance.OUT);
                        break;
                    case NONE:
                    default:
                        break;
                    }
                    i++;
                }
            }
            return type;
        }
    }

    public static class Class extends Generic implements QualifiableTypeDescriptor {
        protected final java.lang.Class<?> klass;

        public Class(java.lang.Class<?> klass, Variance[] useSiteVariance, TypeDescriptor[] typeArguments){
            super(useSiteVariance, typeArguments);
            this.klass = klass;
        }

        public java.lang.Class<?> getKlass() {
            return klass;
        }
        
        @Override
        public boolean is(TypeDescriptor instanceType) {
            //NOTE: instanceType must be the type descriptor
            //       for a non-null instance
            if (klass==Anything.class || klass==ceylon.language.Object.class) {
                return true;
            }
            if (klass==Null.class || klass==null_.class) {
                return false;
            }
            if (instanceType instanceof Class) {
                java.lang.Class<?> instanceKlass = ((Class)instanceType).klass;
                if (klass==Basic.class) {
                    return isBasic(instanceKlass);
                }
                if (klass==Identifiable.class) {
                    return isIdentifiable(instanceKlass);
                }
                java.lang.Class<?> realKlass;
                if (klass==Exception.class) {
                    realKlass = java.lang.Exception.class;
                }
                else if (klass==Throwable.class) {
                    realKlass = java.lang.Throwable.class;
                }
                else if (klass==ceylon.language.Annotation.class
                        || klass==ceylon.language.ConstrainedAnnotation.class) {
                    realKlass = java.lang.annotation.Annotation.class;
                }
                else {
                    realKlass = klass;
                }
                boolean isSubclass =
                        //(realKlass.getModifiers()&Modifier.FINAL)!=0 ?
                        //realKlass==instanceKlass :
                        realKlass.isAssignableFrom(instanceKlass);  
                if (!isSubclass) {
                    return false;
                }
                else if (!isGeneric()) {
                    return true;
                }
                //no sure if this optimization is really worth it...
//                else if (super.equals((Generic)instanceType)) {
//                    return true;
//                }
            }
            else if (instanceType instanceof Union) {
                for (TypeDescriptor member: ((Union) instanceType).members) {
                    if (!is(member)) {
                        return false;
                    }
                }
                return true;
            }
            else if (instanceType instanceof Intersection) {
                for (TypeDescriptor member: ((Intersection) instanceType).members) {
                    if (is(member)) {
                        return true;
                    }
                }
                return false;
            }
            return super.is(instanceType);
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
            // now compare type arguments
            return super.equals(other);
        }
        
        @Override
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "class".hashCode();
            ret = 37 * ret + Arrays.hashCode(typeArguments);
            ret = 37 * ret + Arrays.hashCode(useSiteVariance);
            ret = 37 * ret + klass.hashCode();
            return  ret;
        }
        
        @Override
        protected void stringTo(StringBuilder sb) {
            String className = klass.getName();
            sb.append(className);
            if (typeArguments.length != 0) {
                // add type arguments
                super.stringTo(sb);
            }
        }

        @Override
        public Type toType(RuntimeModuleManager moduleManager){
            return toProducedType(null, moduleManager);
        }
        
        @Override
        public Type toProducedType(Type qualifyingType, RuntimeModuleManager moduleManager){
            // FIXME: is this really enough?
            String typeName = klass.getName();
            Module module = moduleManager.findModuleForClass(klass);
            TypeDeclaration decl = (TypeDeclaration) moduleManager.getModelLoader().getDeclaration(module, typeName, DeclarationType.TYPE);
            List<Type> typeArgs = new ArrayList<Type>(typeArguments.length);
            for(TypeDescriptor typeArg : typeArguments){
                typeArgs.add(Metamodel.getProducedType(typeArg));
            }
            Type type = decl.appliedType(qualifyingType, typeArgs);
            type = applyUseSiteVariance(decl, type);
            return type;
        }

        @Override
        public java.lang.Class<?> getArrayElementClass() {
            if (klass==Null.class ||
                klass==ceylon.language.Object.class ||
                klass==Anything.class ||
                klass==Basic.class ||
                klass==Identifiable.class) {
                return java.lang.Object.class;
            }
            if (klass==ceylon.language.Exception.class ||
                klass==ceylon.language.Throwable.class) {
                return java.lang.Throwable.class;
            }
            if (klass==ceylon.language.Annotation.class
                    || klass==ceylon.language.ConstrainedAnnotation.class){
                return java.lang.annotation.Annotation.class;
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
            return klass==Null.class || klass == null_.class || klass==Anything.class;
        }

        public TypeDescriptor getSequenceElement() {
            if(klass == ceylon.language.Tuple.class ||
                    klass == Sequence.class ||
                    klass == Sequential.class)
                return typeArguments[0];
            if(klass == Empty.class)
                return NothingType;
            return null;
        }

        public TypeDescriptor getTupleFirstElement() {
            return typeArguments[1];
        }

        public TypeDescriptor getTupleRest() {
            return typeArguments[2];
        }
}

    // FIXME: fix all calls to getTypeArguments()
    // FIXME: implement getSequenceElement
    public static class Tuple extends Class {

        private final TypeDescriptor[] elements;
        private final boolean variadic, atLeastOne;
        private final int firstDefaulted;
        // only set when requested
        private TypeDescriptor elementUnion;
        private TypeDescriptor rest;

        public Tuple(boolean variadic, boolean atLeastOne, int firstDefaulted, TypeDescriptor[] elements) {
            super(ceylon.language.Tuple.class, NO_VARIANCE, null);
            // make sure we have at least one non-variadic element
            if(elements.length < (variadic ? 2 : 1))
                throw new AssertionError("Not enough elements in a tuple (logic error): please report bug");
            this.elements = elements;
            this.variadic = variadic;
            this.atLeastOne = atLeastOne;
            this.firstDefaulted = firstDefaulted;
        }
        
        @Override
        public TypeDescriptor[] getTypeArguments() {
            return new TypeDescriptor[]{getSequenceElement(), getTupleFirstElement(), getTupleRest()};
        }
        
        public boolean isGeneric() {
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null || obj instanceof Tuple == false)
                return false;
            Tuple other = (Tuple) obj;
            if(klass != other.klass)
                return false;
            return Arrays.equals(elements, other.elements)
                    && variadic == other.variadic
                    && atLeastOne == other.atLeastOne
                    && firstDefaulted == other.firstDefaulted;
        }
        
        @Override
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "tuple".hashCode();
            ret = 37 * ret + Arrays.hashCode(elements);
            ret = 37 * ret + (variadic ? 1 : 0);
            ret = 37 * ret + (atLeastOne ? 1 : 0);
            ret = 37 * ret + firstDefaulted;
            return  ret;
        }

        @Override
        public boolean is(TypeDescriptor instanceType) {
            // special case for all-optional tuple which really is a union type of []|[X...]
            // super.is will treat us as Tuple, so we have to allow for Empty to be allowed in subtyping
            if(firstDefaulted == 0
                    && (instanceType == empty_.$TypeDescriptor$
                        || instanceType == Empty.$TypeDescriptor$))
                return true;
            return super.is(instanceType);
        }
        
        protected void stringTo(StringBuilder b) {
            b.append("[");
            int lastNonVariadic = variadic ? elements.length - 2 : elements.length - 1;
            for(int i=0;i<elements.length;i++){
                if(i>0) {
                    b.append(",");
                }
                elements[i].stringTo(b);
                if(i <= lastNonVariadic){
                    if(firstDefaulted >= 0 && i >= firstDefaulted)
                        b.append("=");
                }else{
                    // we're in the variadic one
                    b.append(atLeastOne ? "+" : "*");
                }
            }
            b.append("]");
        }

        @Override
        public Type toProducedType(Type qualifyingType, RuntimeModuleManager moduleManager){
            // FIXME: is this really enough?
            String typeName = klass.getName();
            Module module = moduleManager.findModuleForClass(klass);
            TypeDeclaration decl = (TypeDeclaration) moduleManager.getModelLoader().getDeclaration(module, typeName, DeclarationType.TYPE);
            List<Type> elemTypes = new ArrayList<Type>(elements.length);
            for(TypeDescriptor element : elements){
                elemTypes.add(Metamodel.getProducedType(element));
            }
            return decl.getUnit().getTupleType(elemTypes, variadic, atLeastOne, firstDefaulted);
        }
        
        @Override
        public TypeDescriptor getSequenceElement() {
            // that one's expensive so we cache it
            if(elementUnion == null){
                elementUnion = union(elements);
            }
            return elementUnion;
        }
        
        @Override
        public TypeDescriptor getTupleFirstElement() {
            return elements[0];
        }
        
        @Override
        public TypeDescriptor getTupleRest() {
            // that one's expensive so we cache it
            if(rest == null){
                if(variadic){
                    // there's no such thing as a variadic tuple of length 1: that's a Sequential
                    if(elements.length == 1)
                        throw new AssertionError("Variadic tuple of length 1: that's a bug please report it");
                    if(elements.length == 2){
                        // we extract the variadic part
                        if(atLeastOne)
                            rest = TypeDescriptor.klass(Sequence.class, elements[1]);
                        else
                            rest = TypeDescriptor.klass(Sequential.class, elements[1]);
                    }else{
                        // we have at least one element + variadic left
                        rest = makeTupleRest();
                    }
                }else{
                    // we have no tuples of length 0
                    if(elements.length == 1)
                        rest = Empty.$TypeDescriptor$;
                    else{
                        // we have at least one element left
                        rest = makeTupleRest();
                    }
                }
            }
            return rest;
        }

        private TypeDescriptor makeTupleRest() {
            TypeDescriptor[] newElements = new TypeDescriptor[elements.length-1];
            System.arraycopy(elements, 1, newElements, 0, newElements.length);
            int firstDefaulted = this.firstDefaulted;
            // -1 stays -1
            // 0 stays 0 (if we're defaulted, the rest will be too)
            if(firstDefaulted >= 1)
                firstDefaulted--;
            return new Tuple(variadic, atLeastOne, firstDefaulted, newElements);
        }
    }
    
    public static class FunctionOrValue extends Generic implements QualifiableTypeDescriptor {

        private final String name;
        private final java.lang.Class<?> klass;
        private final boolean local;
        
        /**
         * For members
         */
        public FunctionOrValue(String name, TypeDescriptor[] typeArguments) {
            // we can't have use-site variance for local types in functions, since they are only visible inside their declaration
            // not where they are used
            super(NO_VARIANCE, typeArguments);
            this.klass = null;
            this.name = name;
            if(name.isEmpty())
                local = false;
            else
                local = Character.isDigit(name.charAt(0));
        }

        /**
         * For toplevels
         */
        public FunctionOrValue(java.lang.Class<?> klass, TypeDescriptor[] typeArguments) {
            // we can't have use-site variance for local types in functions, since they are only visible inside their declaration
            // not where they are used
            super(NO_VARIANCE, typeArguments);
            this.klass = klass;
            this.name = null;
            this.local = false;
        }

        @Override
        public Type toType(RuntimeModuleManager moduleManager) {
            // FIXME: is this really enough?
            String typeName = klass.getName();
            // use the toplevel wrapper declaration
            Module module = moduleManager.findModuleForClass(klass);
            // FIXME: this is confuses setters and getters, but this should not matter since we only care about container 
            // functions and their type arguments
            TypedDeclaration declaration = (TypedDeclaration) moduleManager.getModelLoader().getDeclaration(module, typeName, DeclarationType.TYPE);
            return makeProducedType(null, declaration, moduleManager);
        }

        @Override
        public Type toProducedType(Type qualifyingType, RuntimeModuleManager moduleManager) {
            // need to find the local Declaration in its container
            Declaration qualifyingDeclaration = qualifyingType.getDeclaration();
            if(qualifyingDeclaration instanceof FunctionOrValueInterface)
                qualifyingDeclaration = ((FunctionOrValueInterface) qualifyingDeclaration).getUnderlyingDeclaration();
            // FIXME: this is confuses setters and getters, but this should not matter since we only care about container 
            // functions and their type arguments
            TypedDeclaration declaration;
            if(local)
                declaration = (TypedDeclaration) ((LocalDeclarationContainer)qualifyingDeclaration).getLocalDeclaration(name);
            else
                declaration = (TypedDeclaration) qualifyingDeclaration.getDirectMember(name, null, false);
            return makeProducedType(qualifyingType, declaration, moduleManager);
        }

        private Type makeProducedType(Type qualifyingType, TypedDeclaration declaration, RuntimeModuleManager moduleManager) {
            // add the type args
            List<Type> typeArgs = new ArrayList<Type>(typeArguments.length);
            for(TypeDescriptor typeArg : typeArguments){
                typeArgs.add(Metamodel.getProducedType(typeArg));
            }
            // wrap it
            return new FunctionOrValueInterface(declaration).appliedType(qualifyingType, typeArgs);
        }

        @Override
        public java.lang.Class<?> getArrayElementClass() {
            throw new AssertionError("Should never be called");
        }

        @Override
        public boolean containsNull() {
            throw new AssertionError("Should never be called");
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;
            if(obj == null || obj instanceof FunctionOrValue == false)
                return false;
            FunctionOrValue other = (FunctionOrValue) obj;
            if(name != null){
                if(!name.equals(other.name))
                    return false;
            }else{
                if(klass != other.klass)
                    return false;
            }
            // now compare type arguments
            return super.equals(other);
        }
        
        @Override
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "functionorvalue".hashCode();
            ret = 37 * ret + Arrays.hashCode(typeArguments);
            ret = 37 * ret + (klass != null ? klass.hashCode() : 0);
            ret = 37 * ret + (name != null ? name.hashCode() : 0);
            return  ret;
        }
        
        @Override
        public void stringTo(StringBuilder b) {
            if(klass != null)
                b.append(klass.getName());
            else
                b.append(name);
            // add type arguments
            super.stringTo(b);
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
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "member".hashCode();
            ret = 37 * ret + container.hashCode();
            ret = 37 * ret + member.hashCode();
            return ret;
        }
        
        @Override
        public void stringTo(StringBuilder sb) {
            container.stringTo(sb);
            sb.append(".");
            member.stringTo(sb);
        }

        @Override
        public Type toType(RuntimeModuleManager moduleManager) {
            Type qualifyingType = Metamodel.getProducedType(container);
            return ((QualifiableTypeDescriptor)member).toProducedType(qualifyingType, moduleManager);
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
    
    public static class Nothing extends TypeDescriptor {
        
        @Override
        public boolean is(TypeDescriptor instanceType) {
            return false;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }
        
        @Override
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "nothing".hashCode();
            return ret;
        }

        @Override
        public Type toType(RuntimeModuleManager moduleManager) {
            return new NothingType(moduleManager.getModelLoader().getUnit()).getType();
        }
        
        @Override
        public void stringTo(StringBuilder sb) {
            sb.append("ceylon.language.Nothing");
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
    
    public abstract static class Composite extends TypeDescriptor {
        protected final TypeDescriptor[] members;

        public Composite(TypeDescriptor[] members) {
            this.members = members;
        }

        public TypeDescriptor[] getMembers() {
            return members;
        }

        protected boolean equals(Composite other) {
            return allContained(members, other.members)
                    && allContained(other.members, members);
        }

        private static boolean allContained(TypeDescriptor[] a, TypeDescriptor[] b) {
            OUTER:
            for(int i=0;i<a.length;i++){
                TypeDescriptor ref = a[i];
                // find it anywhere
                for(int j=0;j<b.length;j++){
                    if(ref.equals(b[j]))
                        continue OUTER;
                }
                // not found
                return false;
            }
            return true;
        }

        protected abstract char getSep();
        
        @Override
        protected final void stringTo(StringBuilder sb) {
            char sep = getSep();
            if(members.length > 0){
                for(int i=0;i<members.length;i++){
                    if(i>0) {
                        sb.append(sep);
                    }
                    TypeDescriptor member = members[i];
                    if (member instanceof Composite) {
                        sb.append('<');
                    }
                    member.stringTo(sb);
                    if (member instanceof Composite) {
                        sb.append('>');
                    }
                }
            }
        }
    }

    public static class Union extends Composite {

        public Union(TypeDescriptor[] members) {
            super(members);
        }
        
        @Override
        public boolean is(TypeDescriptor instanceType) {
            if (instanceType instanceof Union) {
                for (TypeDescriptor instanceMember: ((Union)instanceType).members) {
                    if (!is(instanceMember)) {
                        return false;
                    }
                }
                return true;
            }
            else {
                for (TypeDescriptor member: members) {
                    if (member.is(instanceType)) {
                        return true;
                    }
                }
                return false;
            }
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
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "union".hashCode();
            ret = 37 * ret + unorderedHashCode(members);
            return ret;
        }

        @Override
        public char getSep() { 
            return '|';
        }

        @Override
        public Type toType(RuntimeModuleManager moduleManager) {
            ArrayList<Type> caseTypes = new ArrayList<Type>(members.length);
            for(TypeDescriptor member : members)
                ModelUtil.addToUnion(caseTypes,Metamodel.getProducedType(member));
            return ModelUtil.union(caseTypes, moduleManager.getModelLoader().getUnit());
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
        public boolean is(TypeDescriptor instanceType) {
            for (TypeDescriptor member: members) {
                if (!member.is(instanceType)) {
                    return false;
                }
            }
            return true;
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
        public int hashCode() {
            int ret = 17;
            ret = 37 * ret + "intersection".hashCode();
            ret = 37 * ret + unorderedHashCode(members);
            return ret;
        }

        @Override
        public char getSep() { 
            return '&';
        }

        @Override
        public Type toType(RuntimeModuleManager moduleManager) {
            Unit unit = moduleManager.getModelLoader().getUnit();
            ArrayList<Type> satisfiedTypes = new ArrayList<Type>(members.length);
            for(TypeDescriptor member : members)
                ModelUtil.addToIntersection(satisfiedTypes, Metamodel.getProducedType(member), unit);
            return ModelUtil.canonicalIntersection(satisfiedTypes, unit);
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
        // delegate
        return klass(klass, NO_VARIANCE, typeArguments);
    }

    public static TypeDescriptor klass(java.lang.Class<?> klass, Variance[] useSiteVariance, TypeDescriptor... typeArguments) {
        // special-case for Tuples because we want to unwrap them even if someone constructs them manually
        TypeDescriptor tuple = unwrapTupleType(klass, useSiteVariance, typeArguments, false);
        if(tuple != null)
            return tuple;
        return new Class(klass, useSiteVariance, typeArguments);
    }

    private static TypeDescriptor.Tuple unwrapTupleType(java.lang.Class<?> klass, Variance[] useSiteVariance, TypeDescriptor[] typeArguments, boolean allOptional) {
        if(klass != ceylon.language.Tuple.class 
                || (useSiteVariance != null && useSiteVariance != NO_VARIANCE))
            return null;
        if(typeArguments.length != 3)
            return null;
        List<TypeDescriptor> elementTypes = new LinkedList<TypeDescriptor>();
        boolean variadic = false;
        boolean atLeastOne = false;
        int firstDefaulted = allOptional ? 0 : -1;
        do{
            TypeDescriptor first = typeArguments[1];
            TypeDescriptor rest = typeArguments[2];
            
            elementTypes.add(first);
            if(rest.equals(Empty.$TypeDescriptor$)){
                // that's the last one
                break;
            }else if(rest instanceof TypeDescriptor.Tuple){
                Tuple restTuple = (Tuple) rest;
                return combineTuples(elementTypes, firstDefaulted, restTuple);
            }else if(rest instanceof TypeDescriptor.Class){
                Class restClass = (Class) rest;
                if(restClass.getKlass() == ceylon.language.Tuple.class){
                    // move to the next one
                    typeArguments = restClass.getTypeArguments();
                    // and loop
                }else if(restClass.getKlass() == Sequence.class){
                    // that's the last one
                    if(restClass.getTypeArguments().length != 1)
                        return null;
                    // add the sequence element type
                    elementTypes.add(restClass.getTypeArguments()[0]);
                    variadic = atLeastOne = true;
                    // and we're done
                    break;
                }else if(restClass.getKlass() == Sequential.class){
                    // that's the last one
                    if(restClass.getTypeArguments().length != 1)
                        return null;
                    // add the sequential element type
                    elementTypes.add(restClass.getTypeArguments()[0]);
                    variadic = true;
                    // and we're done
                    break;
                }else{
                    // no idea
                    return null;
                }
            }else if(rest instanceof TypeDescriptor.Union){
                // could be an optional
                Union restUnion = (Union) rest;
                if(restUnion.members.length != 2)
                    return null;
                TypeDescriptor alternative = null;
                if(restUnion.members[0] == Empty.$TypeDescriptor$){
                    alternative = restUnion.members[1];
                }else if(restUnion.members[1] == Empty.$TypeDescriptor$){
                    alternative = restUnion.members[0];
                }
                if(alternative == null)
                    return null;
                // record the first defaulted if it's the first
                if(firstDefaulted == -1){
                    // so if we have N elements, N is the index of the first defaulted one (the next element) 
                    firstDefaulted = elementTypes.size();
                }
                // now, it MUST be a tuple IIRC
                if(alternative instanceof TypeDescriptor.Tuple){
                    Tuple restTuple = (Tuple) alternative;
                    return combineTuples(elementTypes, firstDefaulted, restTuple);
                }else if(alternative instanceof TypeDescriptor.Class){
                    if(((TypeDescriptor.Class) alternative).getKlass() == ceylon.language.Tuple.class){
                        // we're good, go on and loop
                        typeArguments = ((TypeDescriptor.Class) alternative).getTypeArguments();
                    }else{
                        // no idea
                        return null;
                    }
                }else{
                    // no idea
                    return null;
                }
            }else{
                // no idea
                return null;
            }
        }while(true);
        
        return new Tuple(variadic, atLeastOne, firstDefaulted, elementTypes.toArray(new TypeDescriptor[elementTypes.size()]));
    }

    private static Tuple combineTuples(List<TypeDescriptor> elementTypes, int firstDefaulted, Tuple restTuple) {
        // combine it
        TypeDescriptor[] newElements = new TypeDescriptor[elementTypes.size() + restTuple.elements.length];
        newElements = elementTypes.toArray(newElements);
        System.arraycopy(restTuple.elements, 0, newElements, elementTypes.size(), restTuple.elements.length);
        // if we have defaulted params, keep that
        // if not, inherit and shift
        if(firstDefaulted == -1){
            firstDefaulted = restTuple.firstDefaulted;
            if(firstDefaulted != -1)
                firstDefaulted += elementTypes.size();
        }
        // trust the tuple for variadic since we can't have any of our own
        return new Tuple(restTuple.variadic, restTuple.atLeastOne, firstDefaulted, newElements);
    }

    private static Tuple combineTuples(TypeDescriptor[] elementTypes, int firstDefaulted, Tuple restTuple) {
        // combine it
        TypeDescriptor[] newElements = new TypeDescriptor[elementTypes.length + restTuple.elements.length];
        System.arraycopy(elementTypes, 0, newElements, 0, elementTypes.length);
        System.arraycopy(restTuple.elements, 0, newElements, elementTypes.length, restTuple.elements.length);
        // if we have defaulted params, keep that
        // if not, inherit and shift
        if(firstDefaulted == -1){
            firstDefaulted = restTuple.firstDefaulted;
            if(firstDefaulted != -1)
                firstDefaulted += elementTypes.length;
        }
        // trust the tuple for variadic since we can't have any of our own
        return new Tuple(restTuple.variadic, restTuple.atLeastOne, firstDefaulted, newElements);
    }

    /**
     * Optimisation for tuples
     * @param variadic true if the last element is a sequential/sequence
     * @param atLeastOne true if the last element is a sequence, false for sequential
     * @param firstDefaulted index of the first optional element, or -1 if all required
     * @param elements the tuple elements, where the last one may represent the element type of the variadic parameter, if any
     */
    public static TypeDescriptor tuple(boolean variadic, boolean atLeastOne, int firstDefaulted, TypeDescriptor... elements) {
        return new Tuple(variadic, atLeastOne, firstDefaulted, elements);
    }

    /**
     * Optimisation for tuples whose rest element is only known at runtime
     * @param rest the rest of this tuple, which we can try to merge if it's a valid tuple part
     * @param restElement the element type of the rest tuple
     * @param firstDefaulted index of the first optional element, or -1 if all required
     * @param elements the tuple elements, where the last one may represent the element type of the variadic parameter, if any
     */
    public static TypeDescriptor tupleWithRest(TypeDescriptor rest, TypeDescriptor restElement, int firstDefaulted, TypeDescriptor... elements) {
        // easy :)
        if(rest == Empty.$TypeDescriptor$)
            return tuple(false, false, firstDefaulted, elements);
        if(rest instanceof Tuple){
            // easy: merge because Tuple cannot be a degenerate tuple
            return combineTuples(elements, firstDefaulted, (Tuple)rest);
        }else if(rest instanceof Class){
            Class restClass = (Class)rest;
            if(restClass.klass == Sequence.class || restClass.klass == Sequential.class){
                // that's also rather easy
                TypeDescriptor[] newElements = new TypeDescriptor[elements.length+1];
                System.arraycopy(elements, 0, newElements, 0, elements.length);
                newElements[newElements.length-1] = restClass.getSequenceElement();
                return tuple(true, restClass.klass == Sequence.class, firstDefaulted, newElements);
            }
            // is it a standard tuple?
            Tuple restTuple = unwrapTupleType(restClass.klass, restClass.useSiteVariance, restClass.typeArguments, false);
            if(restTuple != null){
                // merge
                return combineTuples(elements, firstDefaulted, restTuple);
            }else{
                return makeDegenerateTuple(elements, firstDefaulted, restTuple, restElement);
            }
        }else{
            return makeDegenerateTuple(elements, firstDefaulted, rest, restElement);
        }
    }

    private static TypeDescriptor makeDegenerateTuple(TypeDescriptor[] elements, int firstDefaulted, TypeDescriptor rest, TypeDescriptor restElementType) {
        // build it backwards
        for(int i=elements.length-1;i>=0;i--){
            TypeDescriptor first = elements[i];
            restElementType = union(restElementType, first);
            // don't call klass() because it would try to unwrap the tuple
            rest = new Class(ceylon.language.Tuple.class, NO_VARIANCE, new TypeDescriptor[]{restElementType, first, rest});
            if(firstDefaulted != -1 && firstDefaulted <= i)
                rest = union(Empty.$TypeDescriptor$, rest);
        }
        return rest;
    }

    /**
     * For members
     */
    public static TypeDescriptor functionOrValue(String name, TypeDescriptor... typeArguments) {
        return new FunctionOrValue(name, typeArguments);
    }

    /**
     * For toplevel method/attributes
     */
    public static TypeDescriptor functionOrValue(java.lang.Class<?> klass, TypeDescriptor... typeArguments) {
        return new FunctionOrValue(klass, typeArguments);
    }

    public static TypeDescriptor union(TypeDescriptor... members){
        if(members == null || members.length == 0)
            throw new AssertionError("members can't be null or empty");
        members = flattenUnionOrIntersection(members, true);
        TypeDescriptor single = getSingleTypeDescriptorIfUnique(members);
        if(single != null)
            return single;
        members = removeDuplicates(members);
        
        // special-case for Tuples because we want to unwrap them even if someone constructs them manually
        if(members.length == 2){
            TypeDescriptor alternative = null;
            if(members[0].equals(Empty.$TypeDescriptor$))
                alternative = members[1];
            else if(members[1].equals(Empty.$TypeDescriptor$))
                alternative = members[0];
            if(alternative instanceof Tuple){
                // damn, so we have a []|[A] that we want to turn into a [A=]
                Tuple tuple = (Tuple) alternative;
                // trust the tuple on variadic, and same list of elements
                return new Tuple(tuple.variadic, tuple.atLeastOne, 0, tuple.elements);
            }else if(alternative instanceof Class){
                Class klass = (Class) alternative;
                TypeDescriptor tuple = unwrapTupleType(klass.getKlass(), klass.useSiteVariance, klass.getTypeArguments(), true);
                if(tuple != null)
                    return tuple;
            }
        }

        return new Union(members);
    }

    public static TypeDescriptor intersection(TypeDescriptor... members){
        if(members == null || members.length == 0)
            throw new AssertionError("members can't be null or empty");
        members = flattenUnionOrIntersection(members, false);
        TypeDescriptor single = getSingleTypeDescriptorIfUnique(members);
        if(single != null)
            return single;
        members = removeDuplicates(members);
        return new Intersection(members);
    }

    /**
     * Remove {@link Union}s or {@link Intersection}s from the given {@code members}, flattening 
     * the members of those Unions/Intersections found 
     * (plus the Union/Intersection members of <em>those</em>, etc) into the 
     * returned array:
     * <pre>
     * A&(B&(C&D))  =>  A&B&C&D
     * A|(B|(C|D))  =>  A|B|C|D
     * </pre>
     * @param members The members of the union or intersection.
     * @param union true to flatten Union within the members, false to flatten intersections.
     */
    private static TypeDescriptor[] flattenUnionOrIntersection(TypeDescriptor[] members, boolean union) {
        TypeDescriptor[] iterating = members;
        for (int ii = 0; ii < iterating.length; ii++) {
            TypeDescriptor td = iterating[ii];
            if (td instanceof Union && union
                    || td instanceof Intersection && !union) {
                TypeDescriptor[] extra = ((Composite)td).members;
                TypeDescriptor[] n = new TypeDescriptor[iterating.length -1 + extra.length];
                System.arraycopy(iterating, 0, n, 0, ii);
                System.arraycopy(extra, 0, n, ii, extra.length);
                if (ii + 1 < iterating.length) {
                    System.arraycopy(iterating, ii+1, n, ii+extra.length, iterating.length-ii-1);
                }
                iterating = n;
            }
        }
        return iterating;
    }

    private static TypeDescriptor[] removeDuplicates(TypeDescriptor[] members) {
        int duplicates = 0;
        for(int i=0;i<members.length;i++){
            TypeDescriptor ref = members[i];
            for(int j=i+1;j<members.length;j++){
                if(ref.equals(members[j])){
                    duplicates++;
                    // next ref
                    break;
                }
            }
        }
        if(duplicates > 0){
            TypeDescriptor[] unique = new TypeDescriptor[members.length-duplicates];
            REF:
            for(int i=0,u=0;i<members.length;i++){
                TypeDescriptor ref = members[i];
                for(int j=i+1;j<members.length;j++){
                    if(ref.equals(members[j])){
                        duplicates++;
                        // skip it
                        continue REF;
                    }
                }
                // it's unique: keep it
                unique[u++] = ref;
            }
            return unique;
        }
        return members;
    }

    /**
     * Returns a single type descriptor if they are all equal. Null otherwise.
     */
    private static TypeDescriptor getSingleTypeDescriptorIfUnique(TypeDescriptor[] members) {
        if(members.length == 1)
            return members[0];
        TypeDescriptor first = members[0];
        for(int i=1;i<members.length;i++){
            if(!members[i].equals(first)){
                return null;
            }
        }
        return first;
    }

    public boolean is(TypeDescriptor instanceType) {
        return this==instanceType ||
                getProducedType(instanceType).isSubtypeOf(getProducedType(this));
    }
}
