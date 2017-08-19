package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.ClassFlags.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.Collections;
import java.util.List;


public class Class extends ClassOrInterface implements Functional {
    
    private static final int EMPTY_VALUE = 1<<23;
    private static final int FALSE_VALUE = 1<<22;
    private static final int TRUE_VALUE = 1<<21;
    private static final int NULL_VALUE = 1<<20;
    private static final int RANGE = 1<<12;
    private static final int ARRAY = 1<<13;
    private static final int THROWABLE = 1<<14;
    private static final int EXCEPTION = 1<<15;
    private static final int ENTRY = 1<<11;
    private static final int TUPLE = 1<<10;
    private static final int BYTE = 1<<9;
    private static final int INTEGER = 1<<8;
    private static final int FLOAT = 1<<7;
    private static final int CHARACTER = 1<<6;
    private static final int STRING = 1<<5;
    private static final int BOOLEAN = 1<<4;
    private static final int BASIC = 1<<3;
    private static final int NULL = 1<<2;
    private static final int OBJECT = 1<<1;
    private static final int ANYTHING = 1<<0;
    
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
    
    public boolean hasStaticMembers() {
        for (Declaration dec: getMembers()) {
            if (dec.isStatic()) {
                return true;
            }
        }
        return false;
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
                if (dec instanceof FunctionOrValue 
                        && dec.getName()==null) {
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
            return defaultConstructor!=null 
                && defaultConstructor.isSealed();
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
            return defaultConstructor==null ? null : 
                defaultConstructor.getParameterList();
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
                FunctionOrValue fov = 
                        (FunctionOrValue) d;
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
        return (flags&FINAL)!=0
            || (flags&ANONYMOUS)!=0;
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
    
    private int code;
    
    private void setCode() {
        if (code==0) {
            Scope scope = getContainer();
            if (scope instanceof Package) {
                Package p = (Package) scope;
                if (p.isLanguagePackage()) {
                    String name = getName();
                    if (name!=null) {
                        switch (name) {
                        case "Anything":
                            code = ANYTHING; break;
                        case "Object":
                            code = OBJECT; break;
                        case "Null":
                            code = NULL; break;
                        case "Basic":
                            code = BASIC; break;
                        case "Boolean":
                            code = BOOLEAN; break;
                        case "String":
                            code = STRING; break;
                        case "Character":
                            code = CHARACTER; break;
                        case "Float":
                            code = FLOAT; break;
                        case "Integer":
                            code = INTEGER; break;
                        case "Byte":
                            code = BYTE; break;
                        case "Tuple":
                            code = TUPLE; break;
                        case "Entry":
                            code = ENTRY; break;
                        case "Range":
                            code = RANGE; break;
                        case "Array":
                            code = ARRAY; break;
                        case "Throwable":
                            code = THROWABLE; break;
                        case "Exception":
                            code = EXCEPTION; break;
                        case "null":
                            code = NULL_VALUE; break;
                        case "true":
                            code = TRUE_VALUE; break;
                        case "false":
                            code = FALSE_VALUE; break;
                        case "empty":
                            code = EMPTY_VALUE; break;
                        default:
                            code = -1;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isAnything() {
        setCode();
        return code == ANYTHING;
    }
    
    @Override
    public boolean isObject() {
        setCode();
        return code == OBJECT;
    }
    
    @Override
    public boolean isNull() {
        setCode();
        return code == NULL;
    }

    @Override
    public boolean isNullValue() {
        setCode();
        return code == NULL_VALUE;
    }

    @Override
    public boolean isTrueValue() {
        setCode();
        return code == TRUE_VALUE;
    }

    @Override
    public boolean isFalseValue() {
        setCode();
        return code == FALSE_VALUE;
    }

    @Override
    boolean isEmptyValue() {
        setCode();
        return code == EMPTY_VALUE;
    }

    @Override
    public boolean isBasic() {
        setCode();
        return code == BASIC;
    }

    @Override
    public boolean isBoolean() {
        setCode();
        return code == BOOLEAN;
    }

    @Override
    public boolean isString() {
        setCode();
        return code == STRING;
    }

    @Override
    public boolean isCharacter() {
        setCode();
        return code == CHARACTER;
    }

    @Override
    public boolean isFloat() {
        setCode();
        return code == FLOAT;
    }

    @Override
    public boolean isInteger() {
        setCode();
        return code == INTEGER;
    }

    @Override
    public boolean isByte() {
        setCode();
        return code == BYTE;
    }

    @Override
    public boolean isTuple() {
        setCode();
        return code == TUPLE;
    }
    
    @Override
    public boolean isEntry() {
        setCode();
        return code == ENTRY;
    }
    
    @Override
    public boolean isRange() {
        setCode();
        return code == RANGE;
    }
    
    @Override
    public boolean isArray() {
        setCode();
        return code == ARRAY;
    }

    @Override
    public boolean isThrowable() {
        setCode();
        return code == THROWABLE;
    }

    @Override
    public boolean isException() {
        setCode();
        return code == EXCEPTION;
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
            return !isAnything() 
                && !isNull() && !isNullValue();
        }
        else if (dec.isNull()) {
            return isNull() || isNullValue();
        }
        else if (dec instanceof Class 
                    && equals(dec)) {
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
            if (et!=null 
                    && et.getDeclaration()
                        .inherits(dec)) {
                return true;
            }
            if (dec instanceof Interface) {
                List<Type> sts = getSatisfiedTypes();
                for (int i=0, s=sts.size(); i<s; i++) {
                    Type st = sts.get(i);
                    if (st.getDeclaration()
                            .inherits(dec)) {
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
    private Class realClass;
    
    @Override
    public boolean isSequentialType() {
        if (sequentialType==0) {
            sequentialType = 
                    isSequentialTypeInternal() ? 
                            1 : -1;
        }
        return sequentialType>0;
    }

    @Override
    public boolean isSequenceType() {
        if (sequenceType==0) {
            sequenceType = 
                    isSequenceTypeInternal() ? 
                            1 : -1;
        }
        return sequenceType>0;
    }

    private boolean inLanguagePackage() {
        return getUnit()
                .getPackage()
                .isLanguagePackage();
    }
    
    private boolean isSequentialTypeInternal() {
        if (!inLanguagePackage()) {
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
        if (!inLanguagePackage()) {
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
                FunctionOrValue model = p.getModel();
                if (model!=null 
                        && model.getType()!=null) {
                    Type type;
                    if (model.isFunctional()) {
                        type = model.getTypedReference()
                                .getFullType();
                    }
                    else {
                        type = model.getType();
                    }
                    params.append(type.asString())
                          .append(" ");
                }
                params.append(p.getName());
            }
            params.append(")");
        }
        return "class " + toStringName() + params;
    }

    public Class getRealClass() {
        return realClass;
    }

    public void setRealClass(Class realClass) {
        this.realClass = realClass;
    }
}
