package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//Note that this class exists to support
//autocompletion in the IDE
public class ImportList implements Scope {
    
    Package container;
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
        return null;
    }
    
    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, List<ProducedType> signature) {
        return getContainer().getMemberOrParameter(unit, name, signature);
    }
    
    @Override
    public Declaration getMember(String name, List<ProducedType> signature) {
        return getContainer().getMember(name, signature);
    }
    
    @Override
    public Declaration getDirectMemberOrParameter(String name, List<ProducedType> signature) {
        return getContainer().getDirectMemberOrParameter(name, signature);
    }
    
    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature) {
        return getContainer().getDirectMember(name, signature);
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
        return container;
    }
    
    public void setContainer(Package container) {
        this.container = container;
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
