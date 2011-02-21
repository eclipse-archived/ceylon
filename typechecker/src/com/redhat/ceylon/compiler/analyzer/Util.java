package com.redhat.ceylon.compiler.analyzer;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Import;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Parameter;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Setter;
import com.redhat.ceylon.compiler.model.TypeDeclaration;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Tree;

public class Util {

    public static Declaration getDeclaration(Scope scope, Unit unit, Tree.Identifier id, Context context) {
        return getDeclaration(scope, unit, true, id.getText(), context);
    }

    public static Declaration getMemberDeclaration(TypeDeclaration scope, Tree.Identifier id, Context context) {
        return getDeclaration(scope, null, false, id.getText(), context);
    }

    public static Declaration getExternalDeclaration(Package pkg, Tree.Identifier id, Context context) {
        return getDeclaration(pkg, null, false, id.getText(), context);
    }

    public static Declaration getLanguageModuleDeclaration(String name, Context context) {
        return getLanguageModuleDeclaration(name, context, new HashSet<Scope>());
    }
    
    private static Declaration getDeclaration(Scope scope, Unit unit, boolean includeParameters, String name, Context context) {
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
            Declaration d = getLocalDeclaration(scope, includeParameters, name);
            if (d!=null) {
                return d;
            }
            scope = scope.getContainer();
        }
        return context==null ? null : getLanguageModuleDeclaration(name, context, traversedScopes);
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
                    final Declaration d = getLocalDeclarationIgnoringSupertypes(languageScope, false, name);
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
    private static Declaration getLocalDeclaration(Scope scope, boolean includeParameters, String name) {
        Declaration d = getLocalDeclarationIgnoringSupertypes(scope, includeParameters, name);
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
                Declaration d = getLocalDeclaration(std, false, name);
                if (d!=null) {
                    return d;
                }
            }
        }
        return null;
    }

    private static Declaration getLocalDeclarationIgnoringSupertypes(Scope scope,
            boolean includeParameters, String name) {
        for ( Declaration d: scope.getMembers() ) {
            if ( !(d instanceof Setter) && (includeParameters || !(d instanceof Parameter)) ) {
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
    private static Declaration getImportedDeclaration(Unit u, String name) {
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
