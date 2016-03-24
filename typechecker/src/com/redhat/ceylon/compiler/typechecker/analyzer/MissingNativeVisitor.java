package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getNativeDeclaration;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.Type;

/**
 * Visitor which checks that every native declaration is provided, and that every
 * use-site of these native declarations is also resolved.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author Tako Schotanus <tako@ceylon-lang.org>
 */
public class MissingNativeVisitor extends Visitor {
    private final Backend forBackend;
    
    public MissingNativeVisitor(Backend forBackend) {
        this.forBackend = forBackend;
    }

    //
    // Use-sites
    
    public void visit(Tree.MemberOrTypeExpression expr){
        checkNativeReference(expr, expr.getDeclaration());
        super.visit(expr);
    }

    public void visit(Tree.SimpleType expr){
        Type model = expr.getTypeModel();
        if(model != null)
            checkNativeReference(expr, model.getDeclaration());
        super.visit(expr);
    }

    //
    // Declaration
    
    public void visit(Tree.ClassOrInterface decl) {
        if (checkNativeDeclaration(decl)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.ObjectDefinition decl) {
        if (checkNativeDeclaration(decl)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.AttributeDeclaration decl){
        if (checkNativeDeclaration(decl)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.AttributeGetterDefinition decl){
        if (checkNativeDeclaration(decl)) {
            super.visit(decl);
        }
    }
    
    public void visit(Tree.AttributeSetterDefinition decl) {
        Setter model = (Setter)decl.getDeclarationModel();
        if (!model.isToplevel() || !model.isNative())
            return;
        if (checkNativeExistence(decl, model.getGetter(), true)) {
            super.visit(decl);
        }
    }

    public void visit(Tree.AnyMethod decl) {
        if (checkNativeDeclaration(decl)) {
            super.visit(decl);
        }
    }

    private void checkNativeReference(Node node, Declaration model) {
        checkNativeExistence(node, model, false);
    }
    
    private boolean checkNativeDeclaration(Tree.Declaration decl) {
        Declaration model = decl.getDeclarationModel();
        return checkNativeExistence(decl, model, true);
    }
    
    private boolean checkNativeExistence(Node node, Declaration model, boolean nodeIsDecl) {
        if (model == null)
            return true;
        if (!model.isToplevel() || !model.isNative())
            return true;
        Package pkg = ModelUtil.getPackage(model);
        if (pkg == null)
            return true;
        
        if (nodeIsDecl
                && !model.isNativeHeader()
                && !ModelUtil.isForBackend(model.getNativeBackends(), forBackend)) {
            // We don't care about declarations for other backends
            return false;
        }
        
        if (ModelUtil.isForBackend(model.getNativeBackends(), forBackend)) {
            return true;
        } else {
            Declaration hdr;
            if (model.isNativeHeader()) {
                hdr = model;
            } else {
                hdr = getNativeHeader(model);
            }
            if (hdr != null) {
                if (ModelUtil.isImplemented(hdr)) {
                    return true;
                }
                Declaration impl = getNativeDeclaration(hdr, forBackend);
                if (impl != null) {
                    return true;
                }
            } else {
                // If there's no header then the model is for the
                // wrong backend and an error will have been added
                // already by the typechecker
                return true;
            }
        }
        
        boolean ok = checkNative(node, model);
        if (!ok) {
            node.addError("no native implementation for backend: native '" + model.getName() + "' is not implemented for the '" + forBackend.nativeAnnotation + "' backend", forBackend);
        }

        return true;
    }
    
    // Override this method in the backend to perform backend-specific tests
    // Returning true means everything's okay and no error needs to be added
    protected boolean checkNative(Node node, Declaration model) {
        return false;
    }
}
