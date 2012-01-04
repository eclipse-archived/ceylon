package com.redhat.ceylon.compiler.reflectionmodelloader;

import com.redhat.ceylon.compiler.modelloader.refl.ReflPackage;

public class ReflectionPackage implements ReflPackage {

    private Package pkg;

    public ReflectionPackage(Package pkg) {
        this.pkg = pkg;
    }

    @Override
    public String getQualifiedName() {
        return pkg.getName();
    }

}
