package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

/**
 * The bottom type Nothing.
 * 
 * @author Gavin King
 *
 */
public class NothingType extends TypeDeclaration {
    
    public NothingType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getName() {
        return "Nothing";
    }
    
    @Override
    public Scope getContainer() {
    	return unit.getAnythingDeclaration().getContainer();
    }
    
    @Override
    public String getQualifiedNameString() {
        return "ceylon.language::Nothing";
    }
    
    @Override
    public String toString() {
        return "type Nothing";
    }
    
    @Override
    public boolean isShared() {
        return true;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        return true;
    }
    
    @Override
    public boolean equals(Object object) {
    	return object instanceof NothingType;
    }

    @Override
    protected int hashCodeForCache() {
        return 17987123;
    }
    
    @Override
    protected boolean equalsForCache(Object o) {
        return equals(o);
    }
    
    @Override
    protected boolean needsSatisfiedTypes() {
        return false;
    }
    
}
