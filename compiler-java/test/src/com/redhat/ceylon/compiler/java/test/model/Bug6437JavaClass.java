package com.redhat.ceylon.compiler.java.test.model;

import java.util.Collection;
import java.util.List;

public class Bug6437JavaClass implements Bug6437JavaInterface {
    @Override
    public final List<String> getJavaIdentifiers(Collection<? extends String> definitions) {
        return null;
    }

    @Override
    public final List<String> getJavaIdentifiers(String... definitions) {
        return null;
    }

    @Override
    public final List<String> getFullJavaIdentifiers(Collection<? extends String> definitions) {
        return null;
    }

    @Override
    public final List<String> getFullJavaIdentifiers(String... definitions) {
        return null;
    }

}
