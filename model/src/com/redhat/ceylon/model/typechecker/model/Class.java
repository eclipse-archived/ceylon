package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Class extends ClassOrInterface implements Functional {
    
    private boolean constructors;
    private boolean enumerated;
    private boolean abstr;
    private ParameterList parameterList;
    private boolean overloaded;
    private boolean abstraction;
    private boolean anonymous;
    private boolean javaEnum;
    private boolean named = true;
    private boolean fin;
    private boolean serializable;
    private List<Declaration> overloads;
    private List<Reference> unimplementedFormals = 
            Collections.<Reference>emptyList();

    public boolean hasConstructors() {
        return constructors;
    }
    
    public void setConstructors(boolean constructors) {
        this.constructors = constructors;
    }
    
    public boolean hasEnumerated() {
        return enumerated;
    }

    public void setEnumerated(boolean enumerated) {
        this.enumerated = enumerated;
    }
    
    @Override
    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * Return true if we have are anonymous and have a name which is not system-generated. Currently
     * only object expressions have no name.
     */
    @Override
    public boolean isNamed() {
        return named;
    }
    
    public void setNamed(boolean named) {
        this.named = named;
    }
    
    @Override
    public boolean isAbstract() {
        return abstr;
    }

    public void setAbstract(boolean isAbstract) {
        this.abstr = isAbstract;
    }
    
    public Constructor getDefaultConstructor() {
        if (constructors) {
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
        if (constructors) {
            Constructor defaultConstructor = getDefaultConstructor();
            if (defaultConstructor==null || 
                    defaultConstructor.getParameterLists().isEmpty()) {
                return null;
            }
            else {
                return defaultConstructor.getFirstParameterList();
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
            return Collections.emptyList();
        }
        else {
            return Collections.singletonList(parameterList);
        }
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterList = pl;
    }

    public Parameter getParameter(String name) {
        for (Declaration d : getMembers()) {
            if (d.isParameter() && ModelUtil.isNamed(name, d)) {
                return ((FunctionOrValue) d).getInitializerParameter();
            }
        }
        return null;
    }
    
    @Override
    public boolean isOverloaded() {
    	return overloaded;
    }
    
    public void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}
    
    @Override
    public Type getExtendedType() {
        Type et = super.getExtendedType();
        if (et==null) {
            //for Anything
            return null;
        }
        else if (et.isClass()) {
            return et;
        }
        else {
            return unit.getAnythingType();
        }
    }
    
    public void setAbstraction(boolean abstraction) {
        this.abstraction = abstraction;
    }
    
    @Override
    public boolean isAbstraction() {
        return abstraction;
    }
    
    @Override
    public boolean isFinal() {
		return fin||anonymous;
	}
    
    public void setFinal(boolean fin) {
		this.fin = fin;
	}

    @Override
    public List<Declaration> getOverloads() {
        return overloads;
    }

    public void setOverloads(List<Declaration> overloads) {
        this.overloads = overloads;
    }

    public void initOverloads(Class... initial) {
        overloads = 
                new ArrayList<Declaration>
                    (initial.length+1);
        for (Declaration d: initial) {
            overloads.add(d);
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
    boolean isAnything() {
        return getQualifiedNameString()
                .equals("ceylon.language::Anything");
    }
    
    @Override
    boolean isObject() {
        return getQualifiedNameString()
                .equals("ceylon.language::Object");
    }
    
    @Override
    boolean isNull() {
        return getQualifiedNameString()
                .equals("ceylon.language::Null");
    }

    @Override
    public boolean isBasic() {
        return getQualifiedNameString()
                .equals("ceylon.language::Basic");
    }

    @Override
    public boolean isBoolean() {
        return getQualifiedNameString()
                .equals("ceylon.language::Boolean");
    }

    @Override
    public boolean isString() {
        return getQualifiedNameString()
                .equals("ceylon.language::String");
    }

    @Override
    public boolean isCharacter() {
        return getQualifiedNameString()
                .equals("ceylon.language::Character");
    }

    @Override
    public boolean isFloat() {
        return getQualifiedNameString()
                .equals("ceylon.language::Float");
    }

    @Override
    public boolean isInteger() {
        return getQualifiedNameString()
                .equals("ceylon.language::Integer");
    }

    @Override
    public boolean isByte() {
        return getQualifiedNameString()
                .equals("ceylon.language::Byte");
    }

    @Override
    public boolean isTuple() {
        return getQualifiedNameString()
                .equals("ceylon.language::Tuple");
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec.isAnything()) {
            return true;
        }
//        else if (dec.isObject()) {
//            return !isAnything() && !isNull() &&
//            !getQualifiedNameString().equals("ceylon.language::null");
//        }
//        else 
        if (dec instanceof Class &&  equals(dec)) {
            return true;
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
        return serializable;
    }

    public void setSerializable(boolean serializable) {
        this.serializable = serializable;
    }

    public boolean isJavaEnum() {
        return javaEnum;
    }

    public void setJavaEnum(boolean javaEnum) {
        this.javaEnum = javaEnum;
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
