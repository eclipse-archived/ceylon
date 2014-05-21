package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
import static com.redhat.ceylon.compiler.typechecker.model.Util.lookupMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Package 
        implements ImportableScope, Referenceable, Annotated {

    private List<String> name;
    private Module module;
    private List<Unit> units = new ArrayList<Unit>();
    private boolean shared = false;
    private List<Annotation> annotations = new ArrayList<Annotation>();
    private Unit unit;
    private String nameAsString;
    
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
            return new ArrayList<Unit>(units);
        }
    }
    
    public void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
            members=null;
        }
    }
    
    public void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
            members=null;
        }
    }
    
    public boolean isShared() {
        return shared;
    }
    
    public void setShared(boolean shared) {
        this.shared = shared;
    }
    
    private List<Declaration> members;
    
    @Override
    public List<Declaration> getMembers() {
        synchronized (units) {
            //return getMembersInternal();
            if (members==null) {
                members = getMembersInternal();
            }
            return members;
        }
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members=null;
    }
    
    private List<Declaration> getMembersInternal() {
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

    @Override
    public Scope getScope() {
        return null;
    }

    public String getNameAsString() {
        if (nameAsString == null){
            nameAsString = formatPath(name);
        }
        return nameAsString;
    }

    @Override
    public String toString() {
        return "Package[" + getNameAsString() + "]";
    }
    
    @Override
    public String getQualifiedNameString() {
        return getNameAsString();
    }
    
    /**
     * Search only inside the package, ignoring imports
     */
    @Override
    public Declaration getMember(String name, List<ProducedType> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis);
    }

    @Override
    public Declaration getDirectMember(String name, List<ProducedType> signature, boolean ellipsis) {
        return lookupMember(getMembers(), name, signature, ellipsis);
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
            List<ProducedType> signature, boolean ellipsis) {
        //this implements the rule that imports hide 
        //toplevel members of a package
        //TODO: would it be better to look in the given unit 
        //      first, before checking imports?
        Declaration d = unit.getImportedDeclaration(name, signature, ellipsis);
        if (d!=null) {
            return d;
        }
        d = getDirectMember(name, signature, ellipsis);
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
            if (isResolvable(d) && !isOverloadedVersion(d) && isNameMatching(startingWith, d) ) {
                result.put(d.getName(), new DeclarationWithProximity(d, proximity+1));
            }
        }
        if (unit!=null) {
            result.putAll(unit.getMatchingImportedDeclarations(startingWith, proximity));
        }
        for (Map.Entry<String, DeclarationWithProximity> e: 
        	getModule().getAvailableDeclarations(startingWith).entrySet()) {
    		boolean already = false;
        	for (DeclarationWithProximity dwp: result.values()) {
        		if (dwp.getDeclaration().equals(e.getValue().getDeclaration())) {
        			already = true;
        			break;
        		}
        	}
    		if (!already) result.put(e.getKey(), e.getValue());
        }
        if ("Nothing".startsWith(startingWith)) {
            result.put("Nothing", 
                    new DeclarationWithProximity(new NothingType(unit), proximity+100));
        }
        return result;
    }

    public Map<String, DeclarationWithProximity> getImportableDeclarations(Unit unit, 
    		String startingWith, List<Import> imports, int proximity) {
        Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && d.isShared() && 
            		!isOverloadedVersion(d) &&
            		isNameMatching(startingWith, d)) {
                boolean already = false;
                for (Import i: imports) {
                    if (!i.isWildcardImport() && 
                            i.getDeclaration().equals(d)) {
                        already = true;
                        break;
                    }
                }
                if (!already) {
                    result.put(d.getName(), 
                    		new DeclarationWithProximity(d, proximity));
                }
            }
        }
        return result;
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public int hashCode() {
        // use the cached version, profiling says List.hashCode is expensive
        return getNameAsString().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Package) {
            return ((Package) obj).getNameAsString()
                    .equals(getNameAsString());
        }
        else {
            return false;
        }
    }
    
    @Override
    public Unit getUnit() {
    	return unit;
    }
    
    public void setUnit(Unit unit) {
		this.unit = unit;
	}
    
}
