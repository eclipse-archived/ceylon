package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isNamed;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
import static com.redhat.ceylon.compiler.typechecker.model.Util.format;

import java.util.ArrayList;
import java.util.List;

public class Package implements Scope {
    
	List<String> name;
	Module module;
	List<Declaration> members = new ArrayList<Declaration>();

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

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

	@Override
	public Scope getContainer() {
		return null;
	}
	
	public String getNameAsString() {
	    return format(name);
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
        for ( Declaration d: getMembers() ) {
            if ( isResolvable(d) && /*d.isShared() &&*/ isNamed(name, d) ) {
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
        return getDirectMemberOrParameter(name);
    }

}
