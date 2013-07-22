package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

/**
 * An attribute setter.
 *
 * @author Gavin King
 */
public class Setter extends MethodOrValue implements Scope {

	private Value getter;
	private Parameter parameter;

    public Value getGetter() {
        return getter;
    }

    public void setGetter(Value getter) {
        this.getter = getter;
    }
    
    public Parameter getParameter() {
		return parameter;
	}
    
    public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

    @Override @Deprecated
    public List<String> getQualifiedName() {
        return getter.getQualifiedName();
    }

    @Override
    public String getQualifiedNameString() {
        return getter.getQualifiedNameString();
    }
    
    @Override
    public boolean isShared() {
        if (getter!=null) {
            return getter.isShared();
        }
        else {
            return super.isShared();
        }
    }
    
    @Override
    public boolean isVariable() {
        return true;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.SETTER;
    }
    
    @Override
    public void setSetter(Setter setter) {
        throw new UnsupportedOperationException();
    }

}
