package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class Util {
    
    /**
     * Search for the declaration referred to by a qualified 
     * name in containing scopes and imports, finally looking 
     * in the language module.
     * 
     *  TODO: I would just love to make this method go away
     *        by somehow treating declarations in the language 
     *        module like any other imported declaration!
     *        
     */
    static Declaration getBaseDeclaration(Scope scope, Unit unit, Tree.Identifier id, Context context) {
        Declaration d = scope.getMemberOrParameter(unit, id.getText());
        if (d!=null) {
            return d;
        }
        else {
            return getLanguageModuleDeclaration(id.getText(), context);
        }
    }
    
    /**
     * Search for a declaration in the language module. 
     */
    static Declaration getLanguageModuleDeclaration(String name, Context context) {
        //all elements in ceylon.language are auto-imported
        //traverse all default module packages provided they have not been traversed yet
        if (context==null) return null;
        Module languageModule = context.getModules().getLanguageModule();
        if ( languageModule != null && languageModule.isAvailable() ) {
            if ("Bottom".equals(name)) {
                return new BottomType();
            }
            for (Package languageScope : languageModule.getPackages() ) {
                Declaration d = languageScope.getMember(name);
                if (d != null) {
                    return d;
                }
            }
        }
        return null;
    }
    
    /**
     * Get the class or interface that "this" and "super" 
     * refer to. 
     */
    static ClassOrInterface getContainingClassOrInterface(Scope scope) {
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                return (ClassOrInterface) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Get the class or interface that "outer" refers to. 
     */
    static ProducedType getOuterClassOrInterface(Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    return ((ClassOrInterface) scope).getType();
                }
                else {
                    foundInner = true;
                }
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
    
    /**
     * Convenience method to bind a single type argument 
     * to a toplevel type declaration.  
     */
    static ProducedType producedType(TypeDeclaration declaration, ProducedType typeArgument) {
        return declaration.getProducedType(null, Collections.singletonList(typeArgument));
    }

    /**
     * Convenience method to bind a list of type arguments
     * to a toplevel type declaration.  
     */
    static ProducedType producedType(TypeDeclaration declaration, ProducedType... typeArguments) {
        return declaration.getProducedType(null, Arrays.asList(typeArguments));
    }

    static String name(Tree.Identifier id) {
        if (id==null) {
            return "program element with missing name";
        }
        else {
            return id.getText();
        }
    }

}
