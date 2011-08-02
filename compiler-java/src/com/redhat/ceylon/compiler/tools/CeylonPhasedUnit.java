package com.redhat.ceylon.compiler.tools;

import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleBuilder;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.sun.tools.javac.util.Position.LineMap;

public class CeylonPhasedUnit extends PhasedUnit {

    private JavaFileObject fileObject;
    private LineMap lineMap;

    public CeylonPhasedUnit(VirtualFile unitFile, VirtualFile srcDir,
            CompilationUnit cu, Package p, ModuleBuilder moduleBuilder,
            Context context, JavaFileObject fileObject, LineMap map) {
        super(unitFile, srcDir, cu, p, moduleBuilder, context);
        this.fileObject = fileObject;
        this.lineMap = map;
    }

    public JavaFileObject getFileObject() {
        return fileObject;
    }

    public LineMap getLineMap() {
        return lineMap;
    }
}
