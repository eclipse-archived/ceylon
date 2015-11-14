package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.analyzer.MissingNativeVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Declaration;

/**
 * Visitor which checks that every native declaration is provided, and that every
 * use-site of these native declarations is also resolved.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 * @author Tako Schotanus <tako@ceylon-lang.org>
 */
public class JvmMissingNativeVisitor extends MissingNativeVisitor {
    private final AbstractModelLoader loader;
    
    public JvmMissingNativeVisitor(AbstractModelLoader loader) {
        super(Backend.Java);
        this.loader = loader;
    }
    
    protected boolean checkNative(Node node, Declaration model) {
//      String pkgName = Util.quoteJavaKeywords(pkg.getNameAsString());
//      String qualifiedName = Naming.toplevelClassName(pkgName, model);
//      ClassMirror classMirror = loader.lookupClassMirror(pkg.getModule(), qualifiedName);
//      ok = ok && (classMirror != null);
        return false;
    }
}
