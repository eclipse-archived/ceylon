package com.redhat.ceylon.compiler.java.test;

import java.util.Locale;
import java.util.TreeSet;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;

public class ErrorCollector implements DiagnosticListener<FileObject> {
    
    private final TreeSet<CompilerError> actualErrors = new TreeSet<CompilerError>();

    @Override
    public void report(Diagnostic<? extends FileObject> diagnostic) {
        if(diagnostic.getSource() != null)
            System.err.print(diagnostic.getSource().getName() + ":");
        if(diagnostic.getLineNumber() != -1)
            System.err.print(diagnostic.getLineNumber() + ":");
        System.err.println(diagnostic.getKind().toString() + ":" 
                + diagnostic.getMessage(null));
        actualErrors.add(new CompilerError(diagnostic.getKind(),
                diagnostic.getSource().getName(),
                diagnostic.getLineNumber(),
                diagnostic.getMessage(Locale.getDefault())));
    }
    
    public TreeSet<CompilerError> get(Diagnostic.Kind kind) {
        TreeSet<CompilerError> result = new TreeSet<CompilerError>();
        for (CompilerError diagnostic : actualErrors) {
            if (diagnostic.kind == kind) {
                result.add(diagnostic);
            }
        }
        return result;
    }
    
    
}