package com.redhat.ceylon.compiler.java.runtime.tools;

import java.io.File;

public interface CompilationListener {
    // FIXME: add note?
    // FIXME: change so single method with Kind?
    void error(File file, long line, long column, String message);
    void warning(File file, long line, long column, String message);
    // FIXME: special API for default module? Else specify version for default module.
    void moduleCompiled(String module, String version);
}