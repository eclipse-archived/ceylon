package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.model.typechecker.model.Util.getNativeDeclaration;

import java.util.HashSet;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.mirror.ClassMirror;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Visitor which checks that every native declaration is provided, and that every
 * use-site of these native declarations is also resolved.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author Tako Schotanus <tako@ceylon-lang.org>
 */
public class MissingNativeVisitor extends Visitor {
    private final Backend forBackend;
    private final AbstractModelLoader loader;
    
    private final HashSet<String> declarations;

    public MissingNativeVisitor(Backend forBackend, AbstractModelLoader loader) {
        this.forBackend = forBackend;
        this.loader = loader;
        declarations = new HashSet<String>();
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
        if (checkNativeExistence(decl)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.ObjectDefinition decl) {
        if (checkNativeExistence(decl)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.AttributeDeclaration decl){
        if (checkNativeExistence(decl)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.AttributeGetterDefinition decl){
        if (checkNativeExistence(decl)) {
            super.visit(decl);
        }
    }
    
    public void visit(Tree.AttributeSetterDefinition decl) {
        if(!Decl.isToplevel(decl) || !Decl.isNative(decl))
            return;
        Setter model = (Setter)decl.getDeclarationModel();
        if (checkNativeExistence(decl, model.getGetter())) {
            super.visit(decl);
        }
    }

    public void visit(Tree.AnyMethod decl) {
        if (checkNativeExistence(decl)) {
            super.visit(decl);
        }
    }

    private boolean checkNativeExistence(Tree.Declaration decl) {
        if(!Decl.isToplevel(decl) || !Decl.isNative(decl))
            return true;
        Declaration model = decl.getDeclarationModel();
        if (declarations.contains(model.getName())) {
            return false;
        }
        declarations.add(model.getName());
        return checkNativeExistence(decl, model);
    }
    
    private boolean checkNativeExistence(Node node, Declaration model) {
        if(model == null)
            return true;
        if(!Decl.isToplevel(model) || !Decl.isNative(model))
            return true;
        Package pkg = Decl.getPackage(model);
        if(pkg == null)
            return true;
        
        boolean nodeIsDecl = node instanceof Tree.ClassOrInterface || node instanceof Tree.AnyMethod || node instanceof Tree.AnyAttribute;
        if (nodeIsDecl
                && !model.getNative().isEmpty()
                && !forBackend.nativeAnnotation.equals(model.getNative())) {
            // We don't care about declarations for other backends
            return false;
        }
        
        boolean ok = true;
        if (model instanceof Method || model instanceof Class || model instanceof Value) {
            Declaration m = pkg.getDirectMember(model.getName(), null, false);
            if (m != null) {
                if (!m.isNative() || !nodeIsDecl) {
                    // An error will already have been added by the typechecker
                    return true;
                }
                // Native declarations are a bit weird, if there are multiple they
                // will all have the same list of overloads containing each of them.
                // We here check to see if any of them are for this backend
                m = getNativeDeclaration(m, forBackend);
                if (m != null) {
                    return true;
                }
            }
        }
        
        String pkgName = Util.quoteJavaKeywords(pkg.getNameAsString());
        String qualifiedName = Naming.toplevelClassName(pkgName, model);
        ClassMirror classMirror = loader.lookupClassMirror(pkg.getModule(), qualifiedName);
        ok = ok && (classMirror != null);
        if(!ok)
            node.addError("native implementation not found for: '" + model.getName() + "'", Backend.Java);

        return true;
    }
}
