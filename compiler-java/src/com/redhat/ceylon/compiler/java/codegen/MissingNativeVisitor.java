package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Visitor which checks that every native declaration is provided, and that every
 * use-site of these native declarations is also resolved.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class MissingNativeVisitor extends Visitor {
    
    private AbstractModelLoader loader;

    public MissingNativeVisitor(AbstractModelLoader loader) {
        this.loader = loader;
    }

    //
    // Use-sites
    
    public void visit(Tree.MemberOrTypeExpression expr){
        checkNativeExistence(expr, expr.getDeclaration());
        super.visit(expr);
    }

    public void visit(Tree.SimpleType expr){
        ProducedType model = expr.getTypeModel();
        if(model != null)
            checkNativeExistence(expr, model.getDeclaration());
        super.visit(expr);
    }

    //
    // Declaration
    
    public void visit(Tree.ClassOrInterface decl) {
        checkNativeExistence(decl);
        super.visit(decl);
    }

    public void visit(Tree.ObjectDefinition decl) {
        checkNativeExistence(decl);
        super.visit(decl);
    }

    public void visit(Tree.AttributeDeclaration decl){
        checkNativeExistence(decl);
        super.visit(decl);
    }

    public void visit(Tree.AttributeGetterDefinition decl){
        checkNativeExistence(decl);
        super.visit(decl);
    }
    
    public void visit(Tree.AttributeSetterDefinition decl) {
        checkNativeExistence(decl);
        super.visit(decl);
    }

    public void visit(Tree.AnyMethod decl) {
        checkNativeExistence(decl);
        super.visit(decl);
    }

    private void checkNativeExistence(Tree.Declaration decl) {
        if(!Decl.isToplevel(decl) || !Decl.isNative(decl))
            return;
        Declaration model = decl.getDeclarationModel();
        checkNativeExistence(decl, model);
    }
    
    private void checkNativeExistence(Node node, Declaration model){
        if(model == null)
            return;
        if(!Decl.isToplevel(model) || !Decl.isNative(model))
            return;
        Package pkg = Decl.getPackage(model);
        if(pkg == null)
            return;
        String pkgName = Util.quoteJavaKeywords(pkg.getNameAsString());
        String qualifiedName = Naming.toplevelClassName(pkgName, model);
        ClassMirror classMirror = loader.lookupClassMirror(pkg.getModule(), qualifiedName);
        if(classMirror == null)
            node.addError("native declaration not found");
    }
}
