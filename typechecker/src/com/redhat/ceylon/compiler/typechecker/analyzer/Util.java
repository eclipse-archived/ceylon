package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Import;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class Util {

    public static Declaration getDeclaration(Scope scope, Unit unit, Tree.Identifier id, Context context) {
        return getDeclaration(scope, unit, true, id.getText(), context);
    }

    public static Declaration getMemberDeclaration(TypeDeclaration scope, Tree.Identifier id, Context context) {
        return getDeclaration(scope, null, false, id.getText());
    }

    public static Declaration getExternalDeclaration(Package pkg, Tree.Identifier id, Context context) {
        return getDeclaration(pkg, null, false, id.getText(), context);
    }

    private static Declaration getDeclaration(Scope scope, Unit unit, boolean includeParameters, String name, Context context) {
        Declaration d  = getDeclaration(scope, unit, includeParameters, name);
        return d==null ? getLanguageModuleDeclaration(name, context) : d;
    }

    private static Declaration getDeclaration(Scope scope, Unit unit, boolean includeParameters, String name) {
        while (scope!=null) {
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
        return null;
    }

    public static Declaration getLanguageModuleDeclaration(String name, Context context) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        if (context==null) return null;
        final Module languageModule = context.getModules().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            if ("Bottom".equals(name)) {
                return new BottomType();
            }
            for (Scope languageScope : languageModule.getPackages() ) {
                final Declaration d = getLocalDeclarationIgnoringSupertypes(languageScope, false, name);
                if (d != null) {
                    return d;
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
                if (d!=null && d.isShared()) {
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

    static ClassOrInterface getContainingClassOrInterface(Node that) {
        Scope scope = that.getScope();
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                return (ClassOrInterface) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    static List<ProducedType> getTypeArguments(Tree.TypeArgumentList tal) {
        List<ProducedType> typeArguments = new ArrayList<ProducedType>();
        if (tal!=null) {
            for (Tree.Type ta: tal.getTypes()) {
                ProducedType t = ta.getTypeModel();
                if (t==null) {
                    ta.addError("could not resolve type argument");
                    typeArguments.add(null);
                }
                else {
                    typeArguments.add(t);
                }
            }
        }
        return typeArguments;
    }

    static boolean acceptsTypeArguments(Declaration d, List<ProducedType> typeArguments, 
            Tree.TypeArgumentList tal, Node parent) {
        if (d instanceof Generic) {
            List<TypeParameter> params = ((Generic) d).getTypeParameters();
            if ( params.size()==typeArguments.size() ) {
                for (int i=0; i<params.size(); i++) {
                    TypeParameter param = params.get(i);
                    ProducedType arg = typeArguments.get(i);
                    Map<TypeParameter, ProducedType> self = Collections.singletonMap(param, arg);
                    for (ProducedType st: param.getSatisfiedTypes()) {
                        ProducedType sts = st.substitute(self);
                        if (arg!=null && !arg.isSubtypeOf(sts)) {
                            tal.getTypes().get(i).addError("type parameter " + param.getName() 
                                    + " of declaration " + d.getName()
                                    + " has argument " + arg.getProducedTypeName() 
                                    + " not assignable to " + sts.getProducedTypeName());
                            return false;
                        }
                    }
                }
                return true;
            }
            else {
                if (tal==null) {
                    parent.addError("requires type arguments (until we implement type inference)");
                }
                else {
                    tal.addError("wrong number of type arguments");
                }
                return false;
            }
        }
        else {
            boolean empty = typeArguments.isEmpty();
            if (!empty) {
                tal.addError("does not accept type arguments");
            }
            return empty;
        }
    }

    static ProducedType getDeclaringType(Declaration d, ProducedType containingType) {
        return containingType.getSupertype((TypeDeclaration) d.getContainer());
    }

    static ProducedType getDeclaringType(Node that, Declaration d) {
        //look for it as a declared or inherited 
        //member of the current class or interface
        Scope scope = that.getScope();
        while ( !(scope instanceof Package) ) {
            if (scope instanceof ClassOrInterface) {
                ProducedType st = getDeclaringType(d, ((ClassOrInterface) scope).getType());
                if (st!=null) {
                    return st;
                }
            }
            scope = scope.getContainer();
        }
        return null;
    }

    public static void addToUnion(List<ProducedType> list, ProducedType pt) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (Iterator<ProducedType> iter = pt.getDeclaration().getCaseTypes().iterator(); iter.hasNext();) {
                ProducedType t = iter.next();
                addToUnion( list, t.substitute(pt.getTypeArguments()) );
            }
        }
        else {
            Boolean included = false;
            for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                ProducedType t = iter.next();
                if (pt.isSubtypeOf(t)) {
                    included = true;
                    break;
                }
                else if (pt.isSupertypeOf(t)) {
                    iter.remove();
                }
            }
            if (!included) {
                list.add(pt);
            }
        }
    }

}
