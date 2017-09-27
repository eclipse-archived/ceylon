package org.eclipse.ceylon.model.typechecker.model;

import static java.util.Collections.emptyList;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isNameMatching;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isResolvable;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.lookupMember;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.lookupMemberForBackend;
import static org.eclipse.ceylon.model.typechecker.model.Unit.isToplevelImport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backends;

/**
 * Any program element of relevance to the model.
 * 
 * @author Gavin King
 *
 */
public abstract class Element implements Scoped, ImportScope {
    
    Element() {}
    
    private Scope container;
    private Scope scope;
    protected Unit unit;
    
    private List<Import> imports = null;
    
    @Override
    public List<Import> getImports() {
        return imports;
    }
    
    @Override
    public void addImport(Import imp) {
        if (imports==null) {
            imports = new ArrayList<Import>(3);
        }
        imports.add(imp);
    }

    @Override
    public void removeImport(Import imp) {
        if (imports!=null) {
            imports.remove(imp);
        }
    }
    
    public Import getImport(String name) {
        List<Import> imports = getImports();
        if (imports!=null) {
            for (Import i: imports) {
                if (!i.isAmbiguous() &&
                        i.getTypeDeclaration()==null &&
                        i.getAlias().equals(name)) {
                    return i;
                }
            }
        }
        return null;
    }

    @Override
    public List<Declaration> getMembers() {
        return emptyList();
    }
    
    @Override
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit compilationUnit) {
        this.unit = compilationUnit;
    }

    /**
     * The "real" scope of the element, ignoring that
     * conditions (in an assert, if, or while) each have
     * their own "fake" scope that does not apply to regular
     * declarations that occur within the fake scope.
     * 
     * @see ConditionScope
     */
    @Override
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
    @Override
    public Scope getScope() {
        return scope;
    }
    
    public void setScope(Scope scope) {
        this.scope = scope;
    }
    
    @Override
    public String getQualifiedNameString() {
        return getContainer().getQualifiedNameString();
    }
    
    /**
     * Search only directly inside this scope.
     */
    @Override
    public Declaration getDirectMember(String name, 
            List<Type> signature, boolean variadic) {
        return getDirectMember(name, signature, variadic, 
                false);
    }

    /**
     * Search only directly inside this scope.
     */
    public Declaration getDirectMember(String name, 
            List<Type> signature, boolean variadic, 
            boolean onlyExactMatches) {
        return lookupMember(getMembers(), 
                name, signature, variadic, 
                onlyExactMatches);
    }

    /**
     * Search only directly inside this scope for a member
     * with the given name and any of the given backends
     */
    @Override
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
            List<Type> signature, boolean variadic, 
            boolean onlyExactMatches) {
        return getDirectMember(name, signature, variadic, 
                onlyExactMatches);
    }

    /**
     * Search only this scope, including members inherited 
     * by the scope, without considering containing scopes 
     * or imports. We're not looking for un-shared direct 
     * members, but return them anyway, to let the caller 
     * produce a nicer error.
     */
    @Override
    public Declaration getMember(String name, 
            List<Type> signature, boolean variadic) {
        return getMember(name, signature, variadic, false);
    }
    
    /**
     * Search in this scope, taking into account containing 
     * scopes, imports, and members inherited by this scope
     * and containing scopes, returning even un-shared 
     * declarations of this scope and containing scopes.
     */
    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, 
            List<Type> signature, boolean variadic) {
        return getMemberOrParameter(unit, name, signature, 
                variadic, false);
    }
    
    //TODO: copy/pasted from Unit
    private Declaration getImportedDeclaration(String name, 
            List<Type> signature, boolean ellipsis) {
        List<Import> imports = getImports();
        if (imports!=null) {
            for (Import i: imports) {
                if (!i.isAmbiguous() && 
                        i.getAlias().equals(name)) {
                    //in case of an overloaded member, this will
                    //be the "abstraction", so search for the 
                    //correct overloaded version
                    Declaration d = i.getDeclaration();
                    if (isToplevelImport(i, d)) {
                        return d.getContainer()
                                .getMember(d.getName(), 
                                        signature, ellipsis);
                    }
                }
            }
        }
        return null;
    }
    
    //TODO: copy/pasted from Unit
    public Declaration getImportedDeclaration(TypeDeclaration td, 
            String name, List<Type> signature, 
            boolean ellipsis) {
        List<Import> imports = getImports();
        if (imports!=null) {
            for (Import i: imports) {
                TypeDeclaration itd = i.getTypeDeclaration();
                if (itd!=null && td.inherits(itd) && 
                        !i.isAmbiguous() &&
                        i.getAlias().equals(name)) {
                    //in case of an overloaded member, this will
                    //be the "abstraction", so search for the 
                    //correct overloaded version
                    Declaration d = i.getDeclaration();
                    return d.getContainer()
                            .getMember(d.getName(), 
                                    signature, ellipsis);
                }
            }
        }
        return null;
    }
    
    /**
     * Search in this scope, taking into account containing 
     * scopes, imports, and members inherited by this scope
     * and containing scopes, returning even un-shared 
     * declarations of this scope and containing scopes.
     */
    public Declaration getMemberOrParameter(Unit unit, String name, 
            List<Type> signature, boolean variadic, 
            boolean onlyExactMatches) {
        Declaration d = 
                getImportedDeclaration(name, 
                        signature, variadic);
        if (d!=null) {
            return d;
        }
        d = getMemberOrParameter(name, signature, variadic);
        if (d!=null) {
            return d;
        }
        else if (getScope()!=null) {
            return getScope()
                    .getMemberOrParameter(unit, name, 
                            signature, variadic);
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
            List<Type> signature, boolean variadic) {
        return getMemberOrParameter(name, signature, variadic, 
                false);
    }

    /**
     * Search only this scope, including members inherited 
     * by this scope, without considering containing scopes 
     * or imports. We are even interested in un-shared 
     * direct members.
     */
    protected Declaration getMemberOrParameter(String name, 
            List<Type> signature, boolean variadic, 
            boolean onlyExactMatches) {
        return getDirectMember(name, signature, variadic, 
                onlyExactMatches);
    }

    @Override
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
    
    @Override
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
    
    @Override
    public Type getDeclaringType(Declaration d) {
        if (d.isMember()) {
            return getContainer().getDeclaringType(d);
        }
        else {
            return null;
        }
    }
    
    @Override
    public Map<String, DeclarationWithProximity> 
    getMatchingDeclarations(Unit unit, String startingWith, 
            int proximity, Cancellable canceller) {
        Map<String, DeclarationWithProximity> result = 
                getScope()
                    .getMatchingDeclarations(unit, 
                            startingWith, proximity+1, canceller);
        for (Declaration d: getMembers()) {
            if (canceller != null
                    && canceller.isCancelled()) {
                return Collections.emptyMap();
            }
            
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

    @Override
    public Backends getScopedBackends() {
        return getScope().getScopedBackends();
    }
}
