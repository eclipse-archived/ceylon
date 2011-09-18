package com.redhat.ceylon.compiler.codegen;

import java.io.IOException;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.jvm.ClassWriter;
import com.sun.tools.javac.util.Context;

public class CeylonClassWriter extends ClassWriter {
    public static ClassWriter instance(Context context) {
        ClassWriter instance = context.get(classWriterKey);
        if (instance == null)
            instance = new CeylonClassWriter(context);
        return instance;
    }

    private CeyloncFileManager fileManager;
    private CeylonModelLoader ceylonModelLoader;

    public CeylonClassWriter(Context context) {
        super(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
        ceylonModelLoader = context.get(CeylonModelLoader.class);
    }

    @Override
    public JavaFileObject writeClass(ClassSymbol c) throws IOException,
            PoolOverflow, StringOverflow {
        String packageName = c.packge().getQualifiedName().toString();
        Package pkg = ceylonModelLoader.findPackage(packageName);
        if(pkg == null)
            throw new RuntimeException("Failed to find package: "+packageName);
        Module module = pkg.getModule();
        fileManager.setModule(module);
        return super.writeClass(c);
    }
}
