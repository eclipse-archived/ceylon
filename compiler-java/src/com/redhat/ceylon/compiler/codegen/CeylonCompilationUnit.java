package com.redhat.ceylon.compiler.codegen;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.List;

/**
 * Stupid wrapper to store a ceylon CU in a javac CU.
 */
public class CeylonCompilationUnit extends JCCompilationUnit {

    public final CompilationUnit ceylonTree;
    public final PhasedUnit phasedUnit;

    protected CeylonCompilationUnit(List<JCAnnotation> packageAnnotations, JCExpression pid, List<JCTree> defs, JavaFileObject sourcefile, PackageSymbol packge, Scope namedImportScope, Scope starImportScope, Tree.CompilationUnit ceylonTree, PhasedUnit phasedUnit) {
        super(packageAnnotations, pid, defs, sourcefile, packge, namedImportScope, starImportScope);
        this.ceylonTree = ceylonTree;
        this.phasedUnit = phasedUnit;
    }

}
