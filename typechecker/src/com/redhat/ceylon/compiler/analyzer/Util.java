package com.redhat.ceylon.compiler.analyzer;

import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.ClassOrInterface;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.Import;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Setter;
import com.redhat.ceylon.compiler.model.TypeDeclaration;
import com.redhat.ceylon.compiler.model.TypedDeclaration;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Tree;

public class Util {

    /**
     * Resolve the type against the scope in which it
     * occurs. Imports are taken into account.
     */
    static TypeDeclaration getDeclaration(Tree.Type that, Context context) {
        final TypeDeclaration declaration = (TypeDeclaration) getDeclaration(that.getScope(), 
                that.getUnit(),
                that.getIdentifier(), context);
        //checkForError(that, declaration);
        return declaration;
    }
    
    /**
     * Resolve the type against the given scope. Imports 
     * are ignored.
     */
    static TypeDeclaration getDeclaration(Scope scope, Tree.Type that, Context context) {
        final TypeDeclaration declaration = (TypeDeclaration) getDeclaration(scope, null,
                that.getIdentifier(), context);
        //checkForError(that, declaration);
        return declaration;
    }
    
    /**
     * Resolve the type against the given scope. Imports 
     * are ignored.
     */
    static TypeDeclaration getDeclaration(Tree.TypeConstraint that, Context context) {
        final TypeDeclaration declaration = (TypeDeclaration) getDeclaration(that.getScope(), that.getUnit(),
                that.getIdentifier(), context);
        //checkForError(that, declaration);
        return declaration;
    }
    
    /**
     * Resolve the type against the scope in which it
     * occurs. Imports are taken into account.
     */
    static TypedDeclaration getDeclaration(Tree.Member that, Context context) {
        final TypedDeclaration declaration = (TypedDeclaration) getDeclaration(that.getScope(), that.getUnit(),
                that.getIdentifier(), context);
        //checkForError(that, declaration);
        return declaration;
    }

    /**
     * Resolve the member against the given scope. Imports 
     * are ignored.
     */
    static TypedDeclaration getDeclaration(Scope scope, Tree.Member that, Context context) {
        final TypedDeclaration declaration = (TypedDeclaration) getDeclaration(scope, null,
                that.getIdentifier(), context);
        //checkForError(that, declaration);
        return declaration;
    }

    /**
     * Resolve the declaration against the given package.
     */
    static Declaration getDeclaration(Package pkg, Tree.ImportMemberOrType that, Context context) {
        final Declaration declaration = getDeclaration(pkg, null, that.getIdentifier(), context);
        //checkForError(that, declaration);
        return declaration;
    }

    /*private static void checkForError(Tree.Member that, Declaration declaration) {
        if (declaration == null) {
            that.getErrors().add( new AnalysisError(that, "Member not found: " + 
                    that.getIdentifier().getText() ) );
        }
    }

    private static void checkForError(Tree.Type that, Declaration declaration) {
        if (declaration == null) {
            that.getErrors().add( new AnalysisError(that, "Type not found: " + 
                    that.getIdentifier().getText() ) );
        }
    }

    private static void checkForError(Tree.ImportMemberOrType that, Declaration declaration) {
        if (declaration == null) {
            that.getErrors().add( new AnalysisError(that, "Import not found: " + 
                    that.getIdentifier().getText() ) );
        }
    }*/

    private static Declaration getDeclaration(Scope scope, Unit unit, Tree.Identifier id, Context context) {
        return getDeclaration(scope, unit, id.getText(), context);
    }

    private static Declaration getDeclaration(Scope scope, Unit unit, String name, Context context) {
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
                    final Declaration d = getLocalDeclaration(languageScope, name);
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
        for ( Declaration s: scope.getMembers() ) {
            if ( !(s instanceof Setter) ) {
                Declaration d = (Declaration) s;
                if (d.getName()!=null && d.getName().equals(name)) {
                    return d;
                }
            }
        }
        if (scope instanceof ClassOrInterface) {
            ClassOrInterface ci = (ClassOrInterface) scope;
            ProducedType et = ci.getExtendedType();
            if (et!=null) {
                Declaration d = getLocalDeclaration( (Class) et.getDeclaration(), name );
                if (d!=null) {
                    return d;
                }
            }
        }
        if (scope instanceof TypeDeclaration) {
            TypeDeclaration ci = (TypeDeclaration) scope;
            for (ProducedType st: ci.getSatisfiedTypes()) {
                TypeDeclaration sid = st.getDeclaration();
                Declaration d = getLocalDeclaration( (ClassOrInterface) sid, name );
                if (d!=null) {
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
    
    public static String name(Tree.Declaration dec) {
        if (dec.getIdentifier()==null) {
            return "declaration with missing name";
        }
        else {
            return dec.getIdentifier().getText();
        }
    }
    
    public static String name(Tree.NamedArgument arg) {
        if (arg.getIdentifier()==null) {
            return "named argument with missing name";
        }
        else {
            return arg.getIdentifier().getText();
        }
    }

}
