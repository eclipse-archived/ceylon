package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.tools.JavaFileObject;

public class JavaFileObjectFacade extends FileObjectFacade implements javax.tools.JavaFileObject {

    public JavaFileObjectFacade(JavaFileObject f) {
        super(f);
    }

    @Override
    public javax.lang.model.element.Modifier getAccessLevel() {
        return Facades.facade(((JavaFileObject)f).getAccessLevel());
    }

    @Override
    public Kind getKind() {
        return Facades.facade(((JavaFileObject)f).getKind());
    }

    @Override
    public javax.lang.model.element.NestingKind getNestingKind() {
        return Facades.facade(((JavaFileObject)f).getNestingKind());
    }

    @Override
    public boolean isNameCompatible(String arg0, Kind arg1) {
        return ((JavaFileObject)f).isNameCompatible(arg0, Wrappers.wrap(arg1));
    }

}
