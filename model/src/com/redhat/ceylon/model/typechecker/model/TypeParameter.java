package com.redhat.ceylon.model.typechecker.model;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeParameter extends TypeDeclaration {

    private boolean covariant;
    private boolean contravariant;
    private Declaration declaration;
    private TypeDeclaration selfTypedDeclaration;
    private Type defaultTypeArgument;
    private boolean defaulted;
    private boolean constrained;
    private boolean typeConstructor;
    private Boolean hasNonErasedBounds;
    private List<Declaration> members = new ArrayList<Declaration>(0);
    private boolean captured;
    private List<TypeParameter> typeParameters = emptyList();

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
    
    public boolean isTypeConstructor() {
        return typeConstructor;
    }
    
    public void setTypeConstructor(boolean typeConstructor) {
        this.typeConstructor = typeConstructor;
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
    public boolean isParameterized() {
        return !typeParameters.isEmpty();
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }
    
    @Override
    public List<Declaration> getOverloads() {
        return null;
    }
    
    public Type getDefaultTypeArgument() {
		return defaultTypeArgument;
	}
    
    public void setDefaultTypeArgument(Type defaultTypeArgument) {
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
    public boolean isEmptyType() {
        for (Type st: getSatisfiedTypes()) {
            if (st.getDeclaration().isEmptyType()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isTupleType() {
        for (Type st: getSatisfiedTypes()) {
            if (st.getDeclaration().isTupleType()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSequenceType() {
        for (Type st: getSatisfiedTypes()) {
            if (st.getDeclaration().isSequenceType()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isSequentialType() {
        for (Type st: getSatisfiedTypes()) {
            if (st.getDeclaration().isSequentialType()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<Type> getSatisfiedTypes() {
        List<Type> satisfiedTypes = 
                super.getSatisfiedTypes();
        List<Type> sts = satisfiedTypes;
        for (int i=0, size=sts.size(); i<size; i++) {
            Type st = sts.get(i);
            if (st!=null) {
                TypeDeclaration std = st.getDeclaration();
                if (std==this || st.isTypeAlias()) {
                    if (sts == satisfiedTypes) {
                        sts = new ArrayList<Type>(sts);
                    }
                    sts.remove(i);
                    size--;
                    i--; 
                }
            }
        }
        return sts;
    }
    
    @Override
    public List<Type> getCaseTypes() {
        List<Type> caseTypes = 
                super.getCaseTypes();
        List<Type> cts = caseTypes;
        if (cts!=null) {
            for (int i=0, size=cts.size(); i<size; i++) {
                Type ct = cts.get(i);
                if (ct!=null) {
                    TypeDeclaration ctd = ct.getDeclaration();
                    if (ctd==this || ct.isTypeAlias()) {
                        if (cts==caseTypes) {
                            cts = new ArrayList<Type>(cts);
                        }
                        cts.remove(i);
                        i--;
                        size--;
                    }
                }
            }
        }
        return cts;
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        List<Type> stds = getSatisfiedTypes();
        for (int i=0, l=stds.size(); i<l; i++) {
            Type st = stds.get(i);
            st.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec==null) {
            return false;
        }
        else if (dec.isAnything()) {
            return true;
        }
        else {
            List<Type> sts = getSatisfiedTypes();
            for (int i=0, s=sts.size(); i<s; i++) {
                Type st = sts.get(i);
                if (st.getDeclaration().inherits(dec)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    @Override
    public boolean isFunctional() {
        return true;
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        ret = (37 * ret) + getDeclaration().hashCodeForCache();
        ret = (37 * ret) + Objects.hashCode(getName());
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if(o == null || !(o instanceof TypeParameter))
            return false;
        TypeParameter b = (TypeParameter) o;
        return getDeclaration().equalsForCache(b.getDeclaration())
                && Objects.equals(getName(),b.getName());
    }

    @Override
    public void clearProducedTypeCache() {
        ModelUtil.clearProducedTypeCache(this);
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }
    
    @Override
    public boolean isCaptured() {
        return captured;
    }
    
    @Override
    public String toString() {
        return "given " + toStringName();
    }
}
