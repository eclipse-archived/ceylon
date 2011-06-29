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
	
    public List<String> getQualifiedName() {
        return list( getContainer().getQualifiedName(), getName() );
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
    
    public abstract ProducedReference getProducedReference(ProducedType pt, List<ProducedType> typeArguments);

    public boolean isVisible(Scope scope) {
        if (isShared()) {
            return true;
        }
        else {
            do {
                if ( getContainer()==scope ) {
                    return true;
                }
                scope = scope.getContainer();
            }
            while (scope!=null);
            return false;
        }
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

}
