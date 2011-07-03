package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.list;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a named, annotated program element:
 * a class, interface, type parameter, parameter, 
 * method, local, or attribute.
 * 
 * @author Gavin King
 *
 */
public abstract class Declaration extends Element {
	
	String name;
	boolean shared;
	boolean formal;
	boolean actual;
	boolean def;
	List<Annotation> annotations = new ArrayList<Annotation>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
    public boolean isParameterized() {
        return false;
    }
    
	public List<Annotation> getAnnotations() {
		return annotations;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + 
			"[" + name + "]";
	}
	
	@Override @Deprecated
    public List<String> getQualifiedName() {
        return list( getContainer().getQualifiedName(), getName() );
    }
    
	@Override
	public String getQualifiedNameString() {
        return getContainer().getQualifiedNameString() + "." + getName();
    }
    
    public boolean isActual() {
        return actual;
    }
    
    public void setActual(boolean actual) {
        this.actual = actual;
    }
    
    public boolean isFormal() {
        return formal;
    }
    
    public void setFormal(boolean formal) {
        this.formal = formal;
    }
    
    public boolean isDefault() {
        return def;
    }
    
    public void setDefault(boolean def) {
        this.def = def;
    }
    
    /**
     * Determine if this declaration is visible
     * in the given scope, by considering if it
     * is shared or directly defined in a 
     * containing scope.
     * 
     * Note that this implementation is not quite 
     * right, since for a shared member 
     * declaration it does not check if the 
     * containing declaration is also visible in 
     * the given scope, but this is okay for now 
     * because of how this method is used.
     */
    public boolean isVisible(Scope scope) {
        if (isShared()) {
            return true;
        }
        else {
            return isDefinedInScope(scope);
        }
    }
    
    /**
     * Determine if this declaration is directly
     * defined in a containing scope of the given
     * scope. 
     */
    public boolean isDefinedInScope(Scope scope) {
        while (scope!=null) {
            if ( getContainer()==scope ) {
                return true;
            }
            scope = scope.getContainer();
        }
        return false;
    }

    public boolean isCaptured() {
        return false;
    }
    
    public boolean isToplevel() {
        return getContainer() instanceof Package;
    }

    public boolean isClassMember() {
        return getContainer() instanceof Class;
    }

    public boolean isInterfaceMember() {
        return getContainer() instanceof Interface;
    }

    public boolean isClassOrInterfaceMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    /**
     * Get a produced reference for this declaration 
     * by binding explicit or inferred type arguments 
     * and type arguments of the type of which this
     * declaration is a member, in the case that this 
     * is a member.
     * 
     * @param outerType the qualifying produced 
     *        type or null if this is not a
     *        nested type declaration
     * @param typeArguments arguments to the type 
     *        parameters of this declaration
     */
    public abstract ProducedReference getProducedReference(ProducedType pt, 
            List<ProducedType> typeArguments);

}
