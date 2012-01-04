package com.redhat.ceylon.compiler.reflectionmodelloader;

import com.redhat.ceylon.compiler.modelloader.mirror.PackageMirror;

public class ReflectionPackage implements PackageMirror {

    private Package pkg;

    public ReflectionPackage(Package pkg) {
        this.pkg = pkg;
    }

    @Override
    public String getQualifiedName() {
        return pkg.getName();
    }

}
