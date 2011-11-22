package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNamed;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Package implements Scope {

	private List<String> name;
    private Module module;
    private List<Unit> units = new ArrayList<Unit>();
    private String doc;
    
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
    
    public List<Unit> getUnits() {
        return units;
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
    public Declaration getDirectMemberOrParameter(String name) {
        /*for ( Declaration d: getMembers() ) {
            if ( isResolvable(d) && isNamed(name, d) ) {
                return d;
            }
        }
        return null;*/
        return getDirectMember(name);
    }

    /**
     * Search only inside the package, ignoring imports
     */
    @Override
    public Declaration getMember(String name) {
        return getDirectMember(name);
    }

    @Override
    public Declaration getDirectMember(String name) {
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && /*d.isShared() &&*/ isNamed(name, d)) {
                return d;
            }
        }
        return null;
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
    public Declaration getMemberOrParameter(Unit unit, String name) {
        //this implements the rule that imports hide 
        //toplevel members of a package
        Declaration d = unit.getImportedDeclaration(name);
        if (d!=null) {
            return d;
        }
        d = getDirectMemberOrParameter(name);
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
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
    	Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && isNameMatching(startingWith, d)) {
                result.put(d.getName(), new DeclarationWithProximity(d, proximity+1));
            }
        }
        if (unit!=null) {
        	result.putAll(unit.getMatchingImportedDeclarations(startingWith, proximity));
        }
    	return result;
    }

    Map<String, DeclarationWithProximity> getImportableDeclarations(Unit unit, String startingWith, List<Import> imports, int proximity) {
        Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
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
                    result.put(d.getName(), new DeclarationWithProximity(d, proximity));
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

}
