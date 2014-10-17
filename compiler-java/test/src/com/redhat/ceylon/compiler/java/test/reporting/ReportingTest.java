package com.redhat.ceylon.compiler.java.test.reporting;

import java.util.Arrays;

import javax.tools.Diagnostic.Kind;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ReportingTest extends CompilerTest {
    
    public ReportingTest() {
        super();
        defaultOptions.clear();
        defaultOptions.addAll(Arrays.asList("-out", destDir, "-cacherep", cacheDir, "-g", 
                "-cp", getClassPathAsPath()));
    }
    
    @Test
    public void testWarningSuppressionCmdLine() {
        
    }
    
    @Test
    public void testAnnoSuppressionUnusedDecl() {
        assertErrors(new String[]{"UnusedDeclaration.ceylon"},
                defaultOptions,
                null,
                new Kind[]{Kind.WARNING});
    }
    
    @Test
    public void testOptionSuppressionUnusedImport() {
        defaultOptions.add("-suppress-warnings");
        defaultOptions.add("unusedImport");
        assertErrors(new String[]{"UnusedImport.ceylon"},
                defaultOptions,
                null,
                new Kind[]{Kind.WARNING});
    }
    
    @Test
    public void testAnnoSuppressesNothing() {
        assertErrors(new String[]{"SuppressesNothing.ceylon"},
                defaultOptions,
                null,
                new Kind[]{Kind.WARNING},
                new CompilerError(Kind.WARNING, "", 1, "suppresses no warnings"));
    }
    
    @Test
    public void testAnnoAlreadySuppressed() {
        // We warn about warnings which are suppressed by an annotation on an outer declaration
        assertErrors(new String[]{"AlreadySuppressed.ceylon"},
                defaultOptions,
                null,
                new Kind[]{Kind.WARNING},
                new CompilerError(Kind.WARNING, "", 3, "warnings already suppressed by annotation"));
    }
    
    @Test
    public void testAnnoAlreadySuppressed2() {
        // 
        defaultOptions.add("-suppress-warnings");
        defaultOptions.add("unusedDeclaration");
        assertErrors(new String[]{"AlreadySuppressed.ceylon"},
                defaultOptions,
                null,
                new Kind[]{Kind.WARNING},
                new CompilerError(Kind.WARNING, "", 3, "warnings already suppressed by annotation"));
    }
    
    @Test
    public void testUnknownWarningInAnno() {
        assertErrors(new String[]{"UnknownWarningInAnno.ceylon"},
                defaultOptions,
                null,
                new Kind[]{Kind.WARNING},
                new CompilerError(Kind.WARNING, "", 1, "unknown warning: blahblah"));
    }
    

}
