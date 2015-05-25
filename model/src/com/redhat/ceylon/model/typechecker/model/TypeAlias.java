package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeAlias extends TypeDeclaration {
    
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
    private boolean anonymous;
    
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
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        ret = Util.addHashForModule(ret, this);
        if(isToplevel())
            ret = (37 * ret) + getQualifiedNameString().hashCode();
        else{
            ret = (37 * ret) + getContainer().hashCode();
            ret = (37 * ret) + Objects.hashCode(getName());
        }
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if(o == null || o instanceof ClassOrInterface == false)
            return false;
        ClassOrInterface b = (ClassOrInterface) o;
        if(!Util.sameModule(this, b))
            return false;
        if(isToplevel()){
            if(!b.isToplevel())
                return false;
            return getQualifiedNameString().equals(b.getQualifiedNameString());
        }else{
            if(b.isToplevel())
                return false;
            return getContainer().equals(b.getContainer())
                    && Objects.equals(getName(),b.getName());
        }
    }

    @Override
    public void clearProducedTypeCache() {
        Util.clearProducedTypeCache(this);
    }
    
    @Override
    public String toString() {
        return "type " + toStringName();
    }
}
