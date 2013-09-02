package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
import static com.redhat.ceylon.compiler.typechecker.model.Util.lookupMember;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;

public abstract class Element {
    
    Element() {}
    
	private Scope container;
	private Scope scope;
	protected Unit unit;
    
	public List<Declaration> getMembers() {
        return emptyList();
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit compilationUnit) {
        this.unit = compilationUnit;
    }

    public Scope getContainer() {
        return container;
    }

    public void setContainer(Scope scope) {
        this.container = scope;
    }
    
    public Scope getScope() {
		return scope;
	}

    public void setScope(Scope scope) {
    	this.scope = scope;
    }
    
    public String getQualifiedNameString() {
        return getContainer().getQualifiedNameString();
    }
    
    /**
     * Search only directly inside the given scope,
     * without considering containing scopes or
     * imports.
     */
    protected Declaration getMemberOrParameter(String name, List<ProducedType> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis);
    }
    
    /**
     * Search only directly inside the given scope,
     * without considering containing scopes.
     */
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis);
    }

    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis) {
        return lookupMember(getMembers(), name, signature, ellipsis);
    }

    public ProducedType getDeclaringType(Declaration d) {
    	if (d.isMember()) {
    		return getContainer().getDeclaringType(d);
    	}
    	else {
    		return null;
    	}
    }

    /**
     * Search in the given scope, taking into account
     * containing scopes and imports
     */
    public Declaration getMemberOrParameter(Unit unit, String name, List<ProducedType> signature, boolean ellipsis) {
        Declaration d = getMemberOrParameter(name, signature, ellipsis);
        if (d!=null) {
            return d;
        }
        else if (getScope()!=null) {
            return getScope().getMemberOrParameter(unit, name, signature, ellipsis);
        }
        else {
            //union type or bottom type 
            return null;
        }
    }
    
    public boolean isInherited(Declaration d) {
        if (d.getContainer()==this) {
            return false;
        }
        else if (getContainer()!=null) {
            return getContainer().isInherited(d);
        }
        else {
            return false;
        }
    }
    
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        if (d.getContainer()==this) {
            return null;
        }
        else if (getContainer()!=null) {
            return getContainer().getInheritingDeclaration(d);
        }
        else {
            return null;
        }
    }
    
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
    	Map<String, DeclarationWithProximity> result = getScope()
    			.getMatchingDeclarations(unit, startingWith, proximity+1);
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && !isOverloadedVersion(d) && isNameMatching(startingWith, d)) {
                result.put(d.getName(), new DeclarationWithProximity(d, proximity));
            }
        }
    	return result;
    }

}
