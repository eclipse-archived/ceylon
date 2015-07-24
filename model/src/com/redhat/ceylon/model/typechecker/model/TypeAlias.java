package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addHashForModule;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeAlias extends TypeDeclaration {
    
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    private List<TypeParameter> typeParameters = emptyList();
    
    private boolean anonymous;
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        Type et = getExtendedType();
        if (et!=null) {
            et.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().inherits(dec);
            }
            finally {
                Type.decDepth();
            }
        }
        return false;
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }
    
	@Override
	public DeclarationKind getDeclarationKind() {
		return DeclarationKind.TYPE;
	}
	
	@Override
	public boolean isAlias() {
		return true;
	}
	
	@Override
	public boolean isAnonymous() {
	    return anonymous;
	}
	
	public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
	
	@Override
	public boolean isNamed() {
	    return !anonymous;
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
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    public boolean isEmptyType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isEmptyType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isTupleType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isTupleType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isSequentialType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isSequentialType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isSequenceType() {
        Type et = getExtendedType();
        if (et!=null) {
            Type.checkDepth();
            Type.incDepth();
            try {
                return et.getDeclaration().isSequenceType();
            }
            finally {
                Type.decDepth();
            }
        }
        else {
            return false;
        }
    }
    
    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        ret = addHashForModule(ret, this);
        if(isToplevel())
            ret = (37 * ret) + 
                    getQualifiedNameString().hashCode();
        else{
            ret = (37 * ret) + getContainer().hashCode();
            ret = (37 * ret) + Objects.hashCode(getName());
        }
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if(o == null || !(o instanceof ClassOrInterface))
            return false;
        ClassOrInterface b = (ClassOrInterface) o;
        if (!ModelUtil.sameModule(this, b)) {
            return false;
        }
        if (isToplevel()) {
            if (!b.isToplevel()) {
                return false;
            }
            return getQualifiedNameString()
                    .equals(b.getQualifiedNameString());
        }
        else {
            if(b.isToplevel()) {
                return false;
            }
            return getContainer().equals(b.getContainer())
                    && Objects.equals(getName(),b.getName());
        }
    }

    @Override
    public void clearProducedTypeCache() {
        ModelUtil.clearProducedTypeCache(this);
    }
    
    @Override
    public String toString() {
        return "type " + toStringName();
    }
}
