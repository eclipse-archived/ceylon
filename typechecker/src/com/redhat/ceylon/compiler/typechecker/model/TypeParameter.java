package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypeParameter extends TypeDeclaration implements Functional {

    private boolean covariant;
    private boolean contravariant;
    private Declaration declaration;
    private ParameterList parameterList;
    private TypeDeclaration selfTypedDeclaration;
    private ProducedType defaultTypeArgument;
    private boolean defaulted;
    private boolean constrained;
    private Boolean hasNonErasedBounds;
    private List<Declaration> members = new ArrayList<Declaration>(0);
    private boolean captured;

    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }
    
    public boolean isInvariant() {
    	return !covariant && !contravariant;
    }
    
    public boolean isCovariant() {
        return covariant;
    }

    public void setCovariant(boolean covariant) {
        this.covariant = covariant;
    }

    public boolean isContravariant() {
        return contravariant;
    }

    public void setContravariant(boolean contravariant) {
        this.contravariant = contravariant;
    }
        
    @Override
    public boolean isOverloaded() {
    	return false;
    }
    
    @Override
    public boolean isSelfType() {
        return selfTypedDeclaration!=null;
    }
    
    public TypeDeclaration getSelfTypedDeclaration() {
        return selfTypedDeclaration;
    }
    
    public void setSelfTypedDeclaration(TypeDeclaration selfTypedDeclaration) {
        this.selfTypedDeclaration = selfTypedDeclaration;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }

    @Override
    public List<ParameterList> getParameterLists() {
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
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE_PARAMETER;
    }
    
    @Override
    public String getQualifiedNameString() {
    	return getName();
    }
    
    @Override
    public boolean isAbstraction() {
        return false;
    }
    
    @Override
    public List<Declaration> getOverloads() {
        return null;
    }
    
    @Override
    public Parameter getParameter(String name) {
        return null;
    }
    
    @Override
    public boolean isDeclaredVoid() {
        return false;
    }
    
    public ProducedType getDefaultTypeArgument() {
		return defaultTypeArgument;
	}
    
    public void setDefaultTypeArgument(ProducedType defaultTypeArgument) {
		this.defaultTypeArgument = defaultTypeArgument;
	}
    
    public boolean isDefaulted() {
		return defaulted;
	}
    
    public void setDefaulted(boolean defaulted) {
		this.defaulted = defaulted;
	}
    
    public boolean isConstrained() {
		return constrained;
	}
    
    public void setConstrained(boolean constrained) {
		this.constrained = constrained;
	}

    public Boolean hasNonErasedBounds() {
        return hasNonErasedBounds;
    }

    public void setNonErasedBounds(boolean hasNonErasedBounds) {
        this.hasNonErasedBounds = hasNonErasedBounds;
    }
    
    @Override
    public boolean isFunctional() {
        return true;
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        ret = (37 * ret) + getDeclaration().hashCodeForCache();
        ret = (37 * ret) + getName().hashCode();
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if(o == null || o instanceof TypeParameter == false)
            return false;
        TypeParameter b = (TypeParameter) o;
        return getDeclaration().equalsForCache(b.getDeclaration())
                && getName().equals(b.getName());
    }

    @Override
    public void clearProducedTypeCache() {
        Util.clearProducedTypeCache(this);
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }
    
    @Override
    public boolean isCaptured() {
        return captured;
    }
}
