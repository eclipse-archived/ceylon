package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;


public abstract class ClassOrInterface extends TypeDeclaration {

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
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    public ProducedType getDeclaringType(Declaration d) {
        //look for it as a declared or inherited 
        //member of the current class or interface
    	if (d.isMember()) {
	        ProducedType st = getType().getSupertype((TypeDeclaration) d.getContainer());
	        //return st;
	        if (st!=null) {
	            return st;
	        }
	        else {
	            return getContainer().getDeclaringType(d);
	        }
    	}
    	else {
    		return null;
    	}
    }

    public abstract boolean isAbstract();

    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
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
