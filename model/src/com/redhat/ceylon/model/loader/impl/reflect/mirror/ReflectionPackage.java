package com.redhat.ceylon.model.loader.impl.reflect.mirror;

import com.redhat.ceylon.model.loader.mirror.PackageMirror;

public class ReflectionPackage implements PackageMirror {

    private String pkg;

    public ReflectionPackage(Class<?> klass) {
        pkg = ReflectionUtils.getPackageName(klass);
    }

    @Override
    public String getQualifiedName() {
        return pkg;
    }

}
