package com.redhat.ceylon.model.typechecker.model;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Class extends ClassOrInterface implements Functional {
    
    private static final int CONSTRUCTORS = 1<<11;
    private static final int ENUMERATED = 1<<12;
    private static final int ABSTRACT = 1<<13;
    private static final int FINAL = 1<<14;
    private static final int SERIALIZABLE = 1<<15;
    private static final int ANONYMOUS = 1<<16;
    private static final int JAVA_ENUM = 1<<17;
    private static final int VALUE_CONSTRUCTOR = 1<<18;
    private static final int OVERLOADED = 1<<19;
    private static final int ABSTRACTION = 1<<20;
    private static final int NO_NAME = 1<<24;
    
    private ParameterList parameterList;
    private List<Reference> unimplementedFormals = 
            Collections.<Reference>emptyList();

    public boolean hasConstructors() {
        return (flags&CONSTRUCTORS)!=0;
    }
    
    public void setConstructors(boolean constructors) {
        if (constructors) {
            flags|=CONSTRUCTORS;
        }
        else {
            flags&=(~CONSTRUCTORS);
        }
    }
    
    public boolean hasEnumerated() {
        return (flags&ENUMERATED)!=0;
    }

    public void setEnumerated(boolean enumerated) {
        if (enumerated) {
            flags|=ENUMERATED;
        }
        else {
            flags&=(~ENUMERATED);
        }
    }
    
    @Override
    public boolean isValueConstructor() {
        return (flags&VALUE_CONSTRUCTOR)!=0;
    }
    
    public void setValueConstructor(boolean valueConstructor) {
        if (valueConstructor) {
            flags|=VALUE_CONSTRUCTOR;
        }
        else {
            flags&=(~VALUE_CONSTRUCTOR);
        }
    }
    
    @Override
    public boolean isAnonymous() {
        return (flags&ANONYMOUS)!=0;
    }
    
    public void setAnonymous(boolean anonymous) {
        if (anonymous) {
            flags|=ANONYMOUS;
        }
        else {
            flags&=(~ANONYMOUS);
        }
    }
    
    @Override
    public boolean isObjectClass() {
        return (flags&ANONYMOUS)!=0;
    }
    
    /**
     * Return true if we have are anonymous and have a name 
     * which is not system-generated. Currently only object 
     * expressions have no name.
     */
    @Override
    public boolean isNamed() {
        return (flags&NO_NAME)==0;
    }
    
    public void setNamed(boolean named) {
        if (!named) {
            flags|=NO_NAME;
        }
        else {
            flags&=(~NO_NAME);
        }

    }
    
    @Override
    public boolean isAbstract() {
        return (flags&ABSTRACT)!=0;
    }

    public void setAbstract(boolean abstr) {
        if (abstr) {
            flags|=ABSTRACT;
        }
        else {
            flags&=(~ABSTRACT);
        }

    }
    
    public Constructor getDefaultConstructor() {
        if (hasConstructors()) {
            for (Declaration dec: getMembers()) {
                if (dec instanceof Constructor &&
                        dec.getName()==null) {
                    return (Constructor) dec;
                }
            }
            return null;
        }
        else {
            return null;
        }
    }
    
    public FunctionOrValue getDefaultConstructorFunctionOrValue() {
        if (hasConstructors()) {
            for (Declaration dec: getMembers()) {
                if (dec instanceof FunctionOrValue &&
                        dec.getName()==null) {
                    return (FunctionOrValue) dec;
                }
            }
            return null;
        }
        else {
            return null;
        }
    }
    
    @Override
    public boolean isSealed() {
        if (parameterList==null) {
            Constructor defaultConstructor = 
                    getDefaultConstructor();
            return defaultConstructor!=null &&
                    defaultConstructor.isSealed();
        }
        else {
            return super.isSealed();
        }
    }
    
    @Override
    public ParameterList getFirstParameterList() {
        return getParameterList();
    }
    
    public ParameterList getParameterList() {
        if (hasConstructors()) {
            Constructor defaultConstructor = 
                    getDefaultConstructor();
            if (defaultConstructor==null) {
                return null;
            }
            else {
                if (defaultConstructor.getParameterLists().isEmpty()) {
                    return null;
                }
                else {
                    return defaultConstructor.getFirstParameterList();
                }
            }
        }
        else {
            return parameterList;
        }
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }

    @Override
    public List<ParameterList> getParameterLists() {
        ParameterList parameterList = getParameterList();
        if (parameterList==null) {
            return emptyList();
        }
        else {
            return singletonList(parameterList);
        }
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterList = pl;
    }

    public Parameter getParameter(String name) {
        for (Declaration d : getMembers()) {
            if (d.isParameter() && 
                    ModelUtil.isNamed(name, d)) {
                FunctionOrValue fov = (FunctionOrValue) d;
                return fov.getInitializerParameter();
            }
        }
        return null;
    }
    
    @Override
    public boolean isOverloaded() {
    	return (flags&OVERLOADED)!=0;
    }
    
    public void setOverloaded(boolean overloaded) {
        if (overloaded) {
            flags|=OVERLOADED;
        }
        else {
            flags&=(~OVERLOADED);
        }

	}
    
    @Override
    public Type getExtendedType() {
        Type et = super.getExtendedType();
        if (et==null) {
            //for Anything
            return null;
        }
        else if (et.isUnknown()) {
            //used by the model loader to indicate a
            //superclass that could not be loaded
            //leave it intact because the UnknownType 
            //carries error information 
            return et;
        }
        else if (et.isClass()) {
            return et;
        }
        else {
            return unit.getAnythingType();
        }
    }
    
    public void setAbstraction(boolean abstraction) {
        if (abstraction) {
            flags|=ABSTRACTION;
        }
        else {
            flags&=(~ABSTRACTION);
        }
    }
    
    @Override
    public boolean isAbstraction() {
        return (flags&ABSTRACTION)!=0;
    }
    
    @Override
    public boolean isFinal() {
		return (flags&FINAL)!=0||(flags&ANONYMOUS)!=0;
	}
    
    public void setFinal(boolean fin) {
		if (fin) {
		    flags|=FINAL;
		}
		else {
		    flags&=(~FINAL);
		}
	}

    @Override
    public boolean isDeclaredVoid() {
        return false;
    }

    @Override
    public boolean isFunctional() {
        return true;
    }
    
    @Override
    public boolean isAnything() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Anything");
    }
    
    @Override
    public boolean isObject() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Object");
    }
    
    @Override
    public boolean isNull() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Null");
    }

    @Override
    public boolean isNullValue() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::null");
    }

    @Override
    boolean isEmptyValue() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::empty");
    }

    @Override
    public boolean isBasic() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Basic");
    }

    @Override
    public boolean isBoolean() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Boolean");
    }

    @Override
    public boolean isString() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::String");
    }

    @Override
    public boolean isCharacter() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Character");
    }

    @Override
    public boolean isFloat() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Float");
    }

    @Override
    public boolean isInteger() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Integer");
    }

    @Override
    public boolean isByte() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Byte");
    }

    @Override
    public boolean isTuple() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Tuple");
    }
    
    @Override
    public boolean isEntry() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Entry");
    }
    
    @Override
    public boolean isRange() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Range");
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec==null) {
            return false;
        }
        else if (dec.isAnything()) {
            return true;
        }
        else if (dec.isObject()) {
            return !isAnything() && 
                !isNull() && !isNullValue();
        }
        else if (dec.isNull()) {
            return isNull() || isNullValue();
        }
        else if (dec instanceof Class && equals(dec)) {
            return true;
        }
        else if (dec.isFinal() &&
                //take into account constructors 
                //of final Java classes
                !dec.isAbstraction()) {
            //cannot possibly be true, 
            //since dec is nonequal
            return false;
        }
        else {
            //TODO: optimize this to avoid walking the
            //      same supertypes multiple times
            Type et = getExtendedType();
            if (et!=null && 
                    et.getDeclaration().inherits(dec)) {
                return true;
            }
            if (dec instanceof Interface) {
                List<Type> sts = getSatisfiedTypes();
                for (int i = 0, s=sts.size(); i<s; i++) {
                    Type st = sts.get(i);
                    if (st.getDeclaration().inherits(dec)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public List<Reference> getUnimplementedFormals() {
        return unimplementedFormals;
    }
    
    public void setUnimplementedFormals(
            List<Reference> unimplementedFormals) {
        this.unimplementedFormals = unimplementedFormals;
    }

    public boolean isSerializable() {
        return (flags&SERIALIZABLE)!=0;
    }

    public void setSerializable(boolean serializable) {
        if (serializable) {
            flags|=SERIALIZABLE;
        }
        else {
            flags&=(~SERIALIZABLE);
        }
    }

    public boolean isJavaEnum() {
        return (flags&JAVA_ENUM)!=0;
    }

    public void setJavaEnum(boolean javaEnum) {
        if (javaEnum) {
            flags|=JAVA_ENUM;
        }
        else {
            flags&=(~JAVA_ENUM);
        }
    }
    
    @Override
    public boolean isEmptyType() {
        return isEmptyValue();
    }
    
    @Override
    public boolean isTupleType() {
        return isTuple();
    }
    
    private int sequentialType;
    
    private int sequenceType;
    
    @Override
    public boolean isSequentialType() {
        if (sequentialType==0) {
            sequentialType = 
                    isSequentialTypeInternal() ? 1 : -1;
        }
        return sequentialType>0;
    }

    @Override
    public boolean isSequenceType() {
        if (sequenceType==0) {
            sequenceType = 
                    isSequenceTypeInternal() ? 1 : -1;
        }
        return sequenceType>0;
    }

    private boolean isSequentialTypeInternal() {
        Package pack = getUnit().getPackage();
        if (!pack.getNameAsString()
                .equals(Module.LANGUAGE_MODULE_NAME)) {
            return false;
        }
        else if (isAnything() || isObject() || 
                isNull() || isBasic()) {
            return false;
        }
        else if (isEmptyValue() || isRange() || isTuple()) {
            return true;
        }
        else {
            //handles Measure/Span
            Type et = getExtendedType();
            if (et!=null && et.isRange()) {
                return true;
            }
            //handles misc direct impls of Sequence
            List<Type> sts = getSatisfiedTypes();
            for (Type st: sts) {
                if (st.isSequence()) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private boolean isSequenceTypeInternal() {
        Package pack = getUnit().getPackage();
        if (!pack.getNameAsString()
                .equals(Module.LANGUAGE_MODULE_NAME)) {
            return false;
        }
        else if (isAnything() || isObject() || 
                isNull() || isBasic()) {
            return false;
        }
        else if (isRange() || isTuple()) {
            return true;
        }
        else {
            //handles Measure/Span
            Type et = getExtendedType();
            if (et!=null && et.isRange()) {
                return true;
            }
            //handles misc direct impls of Sequence
            List<Type> sts = getSatisfiedTypes();
            for (Type st: sts) {
                if (st.isSequence()) {
                    return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        ParameterList list = getParameterList();
        if (list!=null) {
            params.append("(");
            boolean first = true;
            for (Parameter p: list.getParameters()) {
                if (first) {
                    first = false;
                }
                else {
                    params.append(", ");
                }
                if (p.getType()!=null) {
                    params.append(p.getType().asString());
                    params.append(" ");
                }
                params.append(p.getName());
            }
            params.append(")");
        }
        return "class " + toStringName() + params;
    }

}
