package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class TypeAlias extends TypeDeclaration {

    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
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
            ret = (37 * ret) + getName().hashCode();
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
                    && getName().equals(b.getName());
        }
    }

    @Override
    public void clearProducedTypeCache() {
        Util.clearProducedTypeCache(this);
    }
}
