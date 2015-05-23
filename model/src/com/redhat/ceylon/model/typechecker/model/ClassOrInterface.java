package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    	    TypeDeclaration ctd = 
    	            (TypeDeclaration) 
    	                d.getContainer();
    	    ProducedType st = getType().getSupertype(ctd);
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

    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
    }

    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec instanceof ClassOrInterface && 
                equals(dec)) {
            return true;
        }
        else {
            return super.inherits(dec);
        }
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        ret = Util.addHashForModule(ret, this);
        if (isToplevel()) {
            ret = (37 * ret) + getQualifiedNameString().hashCode();
        }
        else {
            ret = (37 * ret) + getContainer().hashCode();
            ret = (37 * ret)  + Objects.hashCode(getName());
        }
        return ret;
    }
    
    @Override
    protected boolean equalsForCache(Object o) {
        if (o == null || !(o instanceof ClassOrInterface)) {
            return false;
        }
        ClassOrInterface b = (ClassOrInterface) o;
        if (!Util.sameModule(this, b)) {
            return false;
        }
        if (isToplevel()) {
            if (!b.isToplevel()) return false;
            return getQualifiedNameString()
                        .equals(b.getQualifiedNameString());
        }
        else {
            if (b.isToplevel()) return false;
            return getContainer().equals(b.getContainer()) && 
                    Objects.equals(getName(), b.getName());
        }
    }
    
    @Override
    public void clearProducedTypeCache() {
        Util.clearProducedTypeCache(this);
    }
    
}
