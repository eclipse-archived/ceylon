package com.redhat.ceylon.model.loader.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Setter;

/**
 * Setter subclass which can contain local declarations.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class SetterWithLocalDeclarations extends Setter implements LocalDeclarationContainer {

    private Map<String,Declaration> localDeclarations;
    public final ClassMirror classMirror;
    
    public SetterWithLocalDeclarations(ClassMirror classMirror) {
        this.classMirror = classMirror;
    }
    
    @Override
    public Declaration getLocalDeclaration(String name) {
        if(localDeclarations == null)
            return null;
        return localDeclarations.get(name);
    }

    @Override
    public void addLocalDeclaration(Declaration declaration) {
        if(localDeclarations == null)
            localDeclarations = new HashMap<String, Declaration>();
        localDeclarations.put(declaration.getPrefixedName(), declaration);
    }

}
