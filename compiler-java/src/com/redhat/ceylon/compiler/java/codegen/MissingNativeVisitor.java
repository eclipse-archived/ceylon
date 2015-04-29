package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Overloadable;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
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
    private final AbstractModelLoader loader;

    private final HashMap<Declaration, Boolean> checked;
    
    public MissingNativeVisitor(Backend forBackend, AbstractModelLoader loader) {
        this.forBackend = forBackend;
        this.loader = loader;
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
    
    private void checkNativeExistence(Node node, Declaration model) {
        if(model == null)
            return;
        if(!Decl.isToplevel(model) || !Decl.isNative(model))
            return;
        Package pkg = Decl.getPackage(model);
        if(pkg == null)
            return;
        
        if ((node instanceof Tree.ClassOrInterface || node instanceof Tree.AnyMethod || node instanceof Tree.AnyAttribute)
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
                if (model instanceof Method || model instanceof Class || model instanceof Value) {
                    Declaration m = pkg.getDirectMember(model.getName(), null, false);
                    if (m != null) {
                        if (!m.isNative()) {
                            // An error will already have been added by the typechecker
                            return;
                        }
                        // Native declarations are a bit weird, if there are multiple they
                        // will all have the same list of overloads containing each of them.
                        // We here check to see if any of them are for this backend
                        if (forBackend.nativeAnnotation.equals(Decl.getNative(m))) {
                            return;
                        } else if (m instanceof Overloadable && ((Overloadable)m).getOverloads() != null) {
                            for (Declaration o : ((Overloadable)m).getOverloads()) {
                                if (forBackend.nativeAnnotation.equals(Decl.getNative(o))) {
                                    return;
                                }
                            }
                        }
                    }
                }
                
                String pkgName = Util.quoteJavaKeywords(pkg.getNameAsString());
                String qualifiedName = Naming.toplevelClassName(pkgName, model);
                ClassMirror classMirror = loader.lookupClassMirror(pkg.getModule(), qualifiedName);
                ok = (classMirror != null);
            } finally {
                checked.put(model, ok);
            }
        }
        if(!ok)
            node.addError("native header not found");
    }
}
