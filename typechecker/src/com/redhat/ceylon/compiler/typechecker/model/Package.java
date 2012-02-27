package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
import static com.redhat.ceylon.compiler.typechecker.model.Util.lookupMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Package implements Scope {

    private List<String> name;
    private Module module;
    private List<Unit> units = new ArrayList<Unit>();
    private String doc;
    private boolean shared = false;
    private List<String> authors = new ArrayList<String>();
    
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
    
    public Iterable<Unit> getUnits() {
        synchronized (units) {
            List<Unit> copiedList = new ArrayList<Unit>(units.size());
            copiedList.addAll(units);
            return copiedList;
        }
    }
    
    public void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
    }
    
    public void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
    }
    
    public boolean isShared() {
        return shared;
    }
    
    public void setShared(boolean shared) {
        this.shared = shared;
    }
    
    @Override
    public List<Declaration> getMembers() {
        List<Declaration> result = new ArrayList<Declaration>();
        for (Unit unit: units) {
            for (Declaration d: unit.getDeclarations()) {
                if (d.getContainer().equals(this)) {
                    result.add(d);
                }
            }
        }
        return result;
    }

    @Override
    public Scope getContainer() {
        return null;
    }

    public String getNameAsString() {
        return formatPath(name);
    }

    @Override
    public String toString() {
        return "Package[" + getNameAsString() + "]";
    }

    @Override @Deprecated
    public List<String> getQualifiedName() {
        return getName();
    }

    @Override
    public String getQualifiedNameString() {
        return getNameAsString();
    }

    @Override
    public Declaration getDirectMemberOrParameter(String name, 
            List<ProducedType> signature) {
        return getDirectMember(name, signature);
    }

    /**
     * Search only inside the package, ignoring imports
     */
    @Override
    public Declaration getMember(String name, List<ProducedType> signature) {
        return getDirectMember(name, signature);
    }

    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature) {
        return lookupMember(getMembers(), name, signature, false);
    }

    @Override
    public ProducedType getDeclaringType(Declaration d) {
        return null;
    }

    /**
     * Search in the package, taking into account
     * imports
     */
    @Override
    public Declaration getMemberOrParameter(Unit unit, String name, 
            List<ProducedType> signature) {
        //this implements the rule that imports hide 
        //toplevel members of a package
        Declaration d = unit.getImportedDeclaration(name, signature);
        if (d!=null) {
            return d;
        }
        d = getDirectMemberOrParameter(name, signature);
        if (d!=null) {
            return d;
        }
        return unit.getLanguageModuleDeclaration(name);
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
    public Map<DeclarationKey, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
        Map<DeclarationKey, DeclarationWithProximity> result = new HashMap<DeclarationKey, DeclarationWithProximity>();
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && isNameMatching(startingWith, d)) {
                result.put(new DeclarationKey(d), new DeclarationWithProximity(d, proximity+1));
            }
        }
        if (unit!=null) {
            result.putAll(unit.getMatchingImportedDeclarations(startingWith, proximity));
        }
        return result;
    }

    Map<DeclarationKey, DeclarationWithProximity> getImportableDeclarations(Unit unit, String startingWith, List<Import> imports, int proximity) {
        Map<DeclarationKey, DeclarationWithProximity> result = new HashMap<DeclarationKey, DeclarationWithProximity>();
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && d.isShared() && isNameMatching(startingWith, d) ) {
                boolean already = false;
                for (Import i: imports) {
                    if (i.getDeclaration().equals(d)) {
                        already = true;
                        break;
                    }
                }
                if (!already) {
                    result.put(new DeclarationKey(d), new DeclarationWithProximity(d, proximity));
                }
            }
        }
        return result;
    }
    
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Package) {
            return ((Package) obj).getName().equals(getName());
        }
        else {
            return false;
        }
    }
    
    public String getDoc() {
        return doc;
    }
    
    public void setDoc(String license) {
        this.doc = license;
    }
    
    public List<String> getAuthors() {
        return authors;
    }

}
