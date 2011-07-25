package com.redhat.ceylon.compiler.codegen;

import java.io.IOException;

import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
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

    public CeylonClassWriter(Context context) {
        super(context);
        fileManager = (CeyloncFileManager) context.get(JavaFileManager.class);
    }

    @Override
    public JavaFileObject writeClass(ClassSymbol c) throws IOException,
            PoolOverflow, StringOverflow {
        fileManager.setPackage(c.packge().getQualifiedName().toString());
        return super.writeClass(c);
    }
}
