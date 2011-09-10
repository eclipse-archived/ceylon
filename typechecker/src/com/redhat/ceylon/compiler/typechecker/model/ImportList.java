package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//Note that this class exists to support
//autocompletion in the IDE
public class ImportList implements Scope {
    
    Package importedPackage;
    List<Import> imports = new ArrayList<Import>();
    
    @Override
    public List<Declaration> getMembers() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<String> getQualifiedName() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getQualifiedNameString() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ProducedType getDeclaringType(Declaration d) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Declaration getMemberOrParameter(Unit unit, String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Declaration getMember(String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Declaration getDirectMemberOrParameter(String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Declaration getDirectMember(String name) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean isInherited(Declaration d) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Scope getContainer() {
        return null;
    }
    
    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit,
            String startingWith, int proximity) {
        if (importedPackage!=null) {
            return importedPackage.getImportableDeclarations(unit, startingWith, imports, proximity);
        }
        else {
            return Collections.emptyMap();
        }
    }
    
    public Package getImportedPackage() {
        return importedPackage;
    }
    
    public void setImportedPackage(Package importedPackage) {
        this.importedPackage = importedPackage;
    }
    
    public List<Import> getImports() {
        return imports;
    }
    
}
