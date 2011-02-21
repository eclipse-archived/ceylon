package com.redhat.ceylon.compiler.analyzer;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Import;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Setter;
import com.redhat.ceylon.compiler.model.TypeDeclaration;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Tree;

public class Util {

    public static Declaration getDeclaration(Scope scope, Unit unit, Tree.Identifier id, Context context) {
        return getDeclaration(scope, unit, id.getText(), context);
    }

    public static Declaration getMemberDeclaration(TypeDeclaration scope, Tree.Identifier id, Context context) {
        return getDeclaration(scope, null, id.getText(), context);
    }

    public static Declaration getPackageDeclaration(Package pkg, Tree.Identifier id, Context context) {
        return getDeclaration(pkg, null, id.getText(), context);
    }

    public static Declaration getDeclaration(Scope scope, Unit unit, String name, Context context) {
        Set<Scope> traversedScopes = new HashSet<Scope>();
        while (scope!=null) {
            traversedScopes.add(scope);
            //imports hide declarations in same package
            //but not declarations in local scopes
            if (scope instanceof Package && unit!=null) {
                Declaration d = getImportedDeclaration(unit, name);
                if (d!=null) {
                    return d;
                }
            }
            Declaration d = getLocalDeclaration(scope, name);
            if (d!=null) {
                return d;
            }
            scope = scope.getContainer();
        }
        return getLanguageModuleDeclaration(name, context, traversedScopes);
    }

    public static Declaration getLanguageModuleDeclaration(String name,
            Context context) {
        return getLanguageModuleDeclaration(name, context, new HashSet<Scope>());
    }
    
    private static Declaration getLanguageModuleDeclaration(String name,
            Context context, Set<Scope> traversedScopes) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        final Module languageModule = context.getLanguageModule();
        if (languageModule != null) {
            for (Scope languageScope : languageModule.getPackages() ) {
                if ( !traversedScopes.contains(languageScope) ) {
                    traversedScopes.add(languageScope);
                    final Declaration d = getLocalDeclarationIgnoringSupertypes(languageScope, name);
                    if (d != null) {
                        return d;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Search only directly inside the given scope,
     * without considering containing scopes or 
     * imports. 
     */
    private static Declaration getLocalDeclaration(Scope scope, String name) {
        Declaration d = getLocalDeclarationIgnoringSupertypes(scope, name);
        if (d!=null) {
            return d;
        }
        else {
            if (scope instanceof TypeDeclaration) {
                return getSupertypeDeclaration( (TypeDeclaration) scope, name );
            }
            else {
                return null;
            }
        }
    }

    private static Declaration getSupertypeDeclaration(TypeDeclaration td, String name) {
        for (ProducedType st: td.getType().getSupertypes()) {
            TypeDeclaration std = st.getDeclaration();
            if (std!=td) {
                Declaration d = getLocalDeclaration(std, name);
                if (d!=null) {
                    return d;
                }
            }
        }
        return null;
    }

    private static Declaration getLocalDeclarationIgnoringSupertypes(Scope scope,
            String name) {
        for ( Declaration s: scope.getMembers() ) {
            if ( !(s instanceof Setter) ) {
                Declaration d = (Declaration) s;
                if (d.getName()!=null && d.getName().equals(name)) {
                    return d;
                }
            }
        }
        return null;
    }
    
    /**
     * Search the imports of a compilation unit 
     * for the declaration. 
     */
    static Declaration getImportedDeclaration(Unit u, String name) {
        for (Import i: u.getImports()) {
            Declaration d = i.getDeclaration();
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }
    
    public static String name(Tree.Identifier id) {
        if (id==null) {
            return "program element with missing name";
        }
        else {
            return id.getText();
        }
    }

}
