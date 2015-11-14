package com.redhat.ceylon.model.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backends;

//Note that this class exists to support
//autocompletion in the IDE
public class ImportList implements Scope {
    
    private Scope container;
    private ImportableScope importedScope;
    private List<Import> imports = new ArrayList<Import>();
    
    @Override
    public boolean isToplevel() {
        return false;
    }
    
    @Override
    public List<Declaration> getMembers() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getQualifiedNameString() {
        return getContainer().getQualifiedNameString();
    }
    
    @Override
    public Type getDeclaringType(Declaration d) {
        return null;
    }
    
    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<Type> signature, boolean ellipsis) {
        return getContainer().getMemberOrParameter(unit, name, signature, ellipsis);
    }
    
    @Override
    public Declaration getMember(String name, List<Type> signature, boolean ellipsis) {
        return getContainer().getMember(name, signature, ellipsis);
    }
    
    @Override
    public Declaration getDirectMember(String name, List<Type> signature, boolean ellipsis) {
        return getContainer().getDirectMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getDirectMemberForBackend(String name, Backends backends) {
        return getContainer().getDirectMemberForBackend(name, backends);
    }

    @Override
    public boolean isInherited(Declaration d) {
        return false;
    }
    
    @Override
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        return null;
    }
    
    @Override
    public Scope getContainer() {
        return container;
    }
    
    @Override
    public Scope getScope() {
    	return container;
    }
    
    public void setContainer(Scope container) {
        this.container = container;
    }
    
    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit,
            String startingWith, int proximity) {
        if (importedScope!=null) {
            if (unit.getPackage().equals(importedScope)) {
                return unit.getPackage().getMatchingDeclarations(unit, startingWith, proximity);
            }
            else {
                return importedScope.getImportableDeclarations(unit, startingWith, imports, proximity);
            }
        }
        else {
            return Collections.emptyMap();
        }
    }
    
    public ImportableScope getImportedScope() {
        return importedScope;
    }
    
    public void setImportedScope(ImportableScope importedScope) {
        this.importedScope = importedScope;
    }
        
    public List<Import> getImports() {
        return imports;
    }
    
    public boolean hasImport(Declaration d) {
        for (Import i: getImports()) {
            if (!i.isAmbiguous() &&
            		i.getDeclaration().equals(d)) {
                return true;
            }
        }
        return false;
    }
     
    public Import getImport(String alias) {
        for (Import i: getImports()) {
            if (!i.isAmbiguous() &&
            		i.getAlias().equals(alias)) {
                return i;
            }
        }
        return null;
    }
    
    @Override
    public Backends getScopedBackends() {
        return getScope().getScopedBackends();
    }
}
