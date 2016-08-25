package com.redhat.ceylon.compiler.java.test.model;

import java.util.Collection;
import java.util.List;

public interface Bug6437JavaInterface {
    List<String> getJavaIdentifiers(Collection<? extends String> definitions);

    List<String> getJavaIdentifiers(String... definitions);

    List<String> getFullJavaIdentifiers(Collection<? extends String> definitions);

    List<String> getFullJavaIdentifiers(String... definitions);

}
