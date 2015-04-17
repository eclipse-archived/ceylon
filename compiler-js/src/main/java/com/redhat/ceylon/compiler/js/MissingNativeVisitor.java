package com.redhat.ceylon.compiler.js;

import java.util.HashMap;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Visitor which checks that every native declaration is provided, and that every
 * use-site of these native declarations is also resolved.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author Tako Schotanus <tako@ceylon-lang.org>
 */
public class MissingNativeVisitor extends Visitor {
    private final Backend forBackend;

    private final HashMap<Declaration, Boolean> checked;
    
    public MissingNativeVisitor(Backend forBackend) {
        this.forBackend = forBackend;
        checked = new HashMap<Declaration, Boolean>();
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
        validateNativeApi(decl);
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
        validateNativeApi(decl);
        super.visit(decl);
    }

    private void checkNativeExistence(Tree.Declaration decl) {
        Declaration model = decl.getDeclarationModel();
        checkNativeExistence(decl, model);
    }
    
    private void checkNativeExistence(Node node, Declaration model) {
        if(model == null)
            return;
        if(!model.isToplevel() || !model.isNative())
            return;
        Package pkg = getPackage(model);
        if(pkg == null)
            return;
        
        if ((node instanceof Tree.ClassOrInterface || node instanceof Tree.AnyMethod)
                && !model.getNative().isEmpty()
                && !forBackend.nativeAnnotation.equals(model.getNative())) {
            // We don't care about declarations for other backends
            return;
        }
        
        boolean ok = true;
        if (checked.containsKey(model)) {
            ok = checked.get(model);
        } else {
            try {
                if (model instanceof Method || model instanceof Class) {
                    Declaration m = pkg.getDirectMember(model.getName(), null, false);
                    if (m != null && m.isNative()) {
                        // Native declarations are a bit weird, if there are multiple they
                        // will all have the same list of overloads containing each of them.
                        // We here check to see if any of them are for this backend
                        if (forBackend.nativeAnnotation.equals(m.getNative())) {
                            return;
                        } else if (m instanceof Functional && ((Functional)m).getOverloads() != null) {
                            for (Declaration o : ((Functional)m).getOverloads()) {
                                if (forBackend.nativeAnnotation.equals(o.getNative())) {
                                    return;
                                }
                            }
                        }
                    }
                }
                
                // TODO If we got here it might be that we're dealing with
                // a true native implementation in JavaScript. If possible
                // we should detect if it actually exists
                //ok = checkNativeImplementationExists(model);
                ok = false;
            } finally {
                checked.put(model, ok);
            }
        }
        if(!ok)
            node.addError("native declaration not found");
    }

    private void validateNativeApi(Tree.Declaration node) {
        Declaration model = node.getDeclarationModel();
        if(model == null)
            return;
        if(!model.isToplevel() || !model.isNative())
            return;
        Package pkg = getPackage(model);
        if(pkg == null)
            return;
        
        Functional f = (Functional)model;
        if (f.getOverloads() !=  null) {
            Declaration template = null;
            if ("".equals(model.getNative())) {
                template = model;
            } else {
                for (Declaration d : f.getOverloads()) {
                    if ("".equals(d.getNative())) {
                        template = d;
                        break;
                    }
                }
            }
            if (template == null) {
                // Template-less native implementation, check it's not shared
                if (model.isShared()) {
                    node.addError("native implementation should have a template or not be shared");
                }
            } else {
                // Native implementation with template, check it's shared
                if (!model.isShared()) {
                    if (model == template) {
                        node.addError("native template should be shared");
                    } else {
                        node.addError("native implementation should be shared");
                    }
                }
            }
        } else {
            // External implementation, probably hand-written in JavaScript
            // TODO check anything?
        }
    }
    
    private static Package getPackage(Declaration decl){
        if (decl instanceof Scope) {
            return getPackageContainer((Scope)decl);
        } else {
            return getPackageContainer(decl.getContainer());
        }
    }

    private static Package getPackageContainer(Scope scope){
        // stop when null or when it's a Package
        while(scope != null
                && !(scope instanceof Package)){
            // stop if the container is not a Scope
            if(!(scope.getContainer() instanceof Scope))
                return null;
            scope = (Scope) scope.getContainer();
        }
        return (Package) scope;
    }
}
