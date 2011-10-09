package com.redhat.ceylon.compiler.loader;

import java.util.EnumSet;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.util.Context;

public class CeylonClassReader extends ClassReader {

    public static CeylonClassReader instance(Context context) {
        CeylonClassReader instance = (CeylonClassReader) context.get(classReaderKey);
        if (instance == null)
            instance = new CeylonClassReader(context, true);
        return instance;
    }

    protected CeylonClassReader(Context context, boolean definitive) {
        super(context, definitive);
    }

    @Override
    protected JavaFileObject preferredFileObject(JavaFileObject a, JavaFileObject b) {
        return a.getKind() == Kind.CLASS ? a : b;
    }

    @Override
    protected EnumSet<JavaFileObject.Kind> getPackageFileKinds() {
        return EnumSet.of(JavaFileObject.Kind.CLASS);
    }
}
