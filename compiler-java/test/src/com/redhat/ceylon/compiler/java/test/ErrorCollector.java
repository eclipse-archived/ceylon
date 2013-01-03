/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.test;

import java.util.Locale;
import java.util.TreeSet;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;

import com.redhat.ceylon.compiler.java.test.CompilerError.Classification;

/**
 * A {@link DiagnosticListener} which collects the {@link Diagnostic}s 
 * generated during compilation
 * @author tom
 */
public class ErrorCollector implements DiagnosticListener<FileObject> {
    
    private final TreeSet<CompilerError> actualErrors = new TreeSet<CompilerError>();

    static Classification classifyDiagnostic(Diagnostic<? extends FileObject> diagnostic) {
        String code = diagnostic.getCode();
        if (diagnostic.getMessage(Locale.getDefault()).startsWith("Compiler error:")) {
            return Classification.CRASH;
        } else if (code != null && code.startsWith("compiler.err.ceylon")) {
            return Classification.FRONTEND;
        } else if (code != null && code.startsWith("compiler.err")) {
            return Classification.BACKEND;
        }
        return Classification.UNCLASSIFIED;
    }
    
    @Override
    public void report(Diagnostic<? extends FileObject> diagnostic) {
        if(diagnostic.getSource() != null)
            System.err.print(diagnostic.getSource().getName() + ":");
        if(diagnostic.getLineNumber() != -1)
            System.err.print(diagnostic.getLineNumber() + ":");
        System.err.println(diagnostic.getKind().toString() + ":" 
                + diagnostic.getMessage(null));
        
        actualErrors.add(new CompilerError(diagnostic.getKind(),
                diagnostic.getSource() != null ? diagnostic.getSource().getName() : null,
                diagnostic.getLineNumber(),
                diagnostic.getMessage(Locale.getDefault()),
                classifyDiagnostic(diagnostic)));
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
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CompilerError e : actualErrors) {
            sb.append(e).append(System.lineSeparator());
        }
        return sb.substring(0, sb.length()-System.lineSeparator().length());
    }
    
    public String getAssertionFailureMessage() {
        return "Compilation failed" + System.lineSeparator() + this;
    }
    
    
}