package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isNameMatching;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isResolvable;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.lookupMember;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.lookupMemberForBackend;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backends;

/**
 * Any program element of relevance to the model.
 * 
 * @author Gavin King
 *
 */
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

    /**
     * The "real" scope of the element, ignoring that
     * conditions (in an assert, if, or while) each have
     * their own "fake" scope.
     * 
     * @see ConditionScope
     */
    public Scope getContainer() {
        return container;
    }

    public void setContainer(Scope scope) {
        this.container = scope;
    }
    
    /**
     * The scope of the element, taking into account that
     * conditions (in an assert, if, or while) each have
     * their own "fake" scope.
     * 
     * @see ConditionScope
     */
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
     * Search only directly inside this scope.
     */
    public Declaration getDirectMember(String name, 
            List<Type> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis, 
                false);
    }

    /**
     * Search only directly inside this scope.
     */
    public Declaration getDirectMember(String name, 
            List<Type> signature, boolean ellipsis, 
            boolean onlyExactMatches) {
        return lookupMember(getMembers(), 
                name, signature, ellipsis, 
                onlyExactMatches);
    }

    /**
     * Search only directly inside this scope for a member
     * with the given name and any of the given backends
     */
    public Declaration getDirectMemberForBackend(String name, 
            Backends backends) {
        return lookupMemberForBackend(getMembers(), 
                name, backends);
    }

    /**
     * Search only this scope, including members inherited 
     * by the scope, without considering containing scopes 
     * or imports. We're not looking for un-shared direct 
     * members, but return them anyway, to let the caller 
     * produce a nicer error.
     */
    public Declaration getMember(String name, 
            List<Type> signature, boolean ellipsis, 
            boolean onlyExactMatches) {
        return getDirectMember(name, signature, ellipsis, 
                onlyExactMatches);
    }

    /**
     * Search only this scope, including members inherited 
     * by the scope, without considering containing scopes 
     * or imports. We're not looking for un-shared direct 
     * members, but return them anyway, to let the caller 
     * produce a nicer error.
     */
    public Declaration getMember(String name, 
            List<Type> signature, boolean ellipsis) {
        return getMember(name, signature, ellipsis, false);
    }
    
    /**
     * Search in this scope, taking into account containing 
     * scopes, imports, and members inherited by this scope
     * and containing scopes, returning even un-shared 
     * declarations of this scope and containing scopes.
     */
    public Declaration getMemberOrParameter(Unit unit, String name, 
            List<Type> signature, boolean ellipsis) {
        return getMemberOrParameter(unit, name, signature, 
                ellipsis, false);
    }
    
    /**
     * Search in this scope, taking into account containing 
     * scopes, imports, and members inherited by this scope
     * and containing scopes, returning even un-shared 
     * declarations of this scope and containing scopes.
     */
    public Declaration getMemberOrParameter(Unit unit, String name, 
            List<Type> signature, boolean ellipsis, 
            boolean onlyExactMatches) {
        Declaration d = 
                getMemberOrParameter(name, signature, ellipsis);
        if (d!=null) {
            return d;
        }
        else if (getScope()!=null) {
            return getScope()
                    .getMemberOrParameter(unit, name, 
                            signature, ellipsis);
        }
        else {
            //union type or bottom type 
            return null;
        }
    }
    
    /**
     * Search only this scope, including members inherited 
     * by this scope, without considering containing scopes 
     * or imports. We are even interested in un-shared 
     * direct members.
     */
    protected Declaration getMemberOrParameter(String name, 
            List<Type> signature, boolean ellipsis) {
        return getMemberOrParameter(name, signature, ellipsis, 
                false);
    }

    /**
     * Search only this scope, including members inherited 
     * by this scope, without considering containing scopes 
     * or imports. We are even interested in un-shared 
     * direct members.
     */
    protected Declaration getMemberOrParameter(String name, 
            List<Type> signature, boolean ellipsis, 
            boolean onlyExactMatches) {
        return getDirectMember(name, signature, ellipsis, 
                onlyExactMatches);
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
    
    public Type getDeclaringType(Declaration d) {
        if (d.isMember()) {
            return getContainer().getDeclaringType(d);
        }
        else {
            return null;
        }
    }
    
    public Map<String, DeclarationWithProximity> 
    getMatchingDeclarations(Unit unit, String startingWith, 
            int proximity) {
    	Map<String, DeclarationWithProximity> result = 
    	        getScope()
    			    .getMatchingDeclarations(unit, 
    			            startingWith, proximity+1);
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && !isOverloadedVersion(d)){
                if(isNameMatching(startingWith, d)) {
                    result.put(d.getName(unit), 
                            new DeclarationWithProximity(d, 
                                    proximity));
                }
                for(String alias : d.getAliases()){
                    if(isNameMatching(startingWith, alias)){
                        result.put(alias, 
                                new DeclarationWithProximity(
                                        alias, d, proximity));
                    }
                }
            }
        }
    	return result;
    }

}
