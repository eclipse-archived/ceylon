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
        ret = (37 * ret) + getQualifiedNameString().hashCode();
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if(o == null || o instanceof ClassOrInterface == false)
            return false;
        ClassOrInterface b = (ClassOrInterface) o;
        if(!Util.sameModule(this, b))
            return false;
        return getQualifiedNameString().equals(b.getQualifiedNameString());
    }

    @Override
    public void clearProducedTypeCache() {
        Util.clearProducedTypeCache(this);
    }
}
