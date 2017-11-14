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
package org.eclipse.ceylon.compiler.java.test.reporting;

import java.util.Arrays;

import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.javax.tools.Diagnostic.Kind;
import org.junit.Test;

public class ReportingTests extends CompilerTests {
    
    public ReportingTests() {
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
        compilesWithoutWarnings("UnusedDeclaration.ceylon");
    }
    
    @Test
    public void testOptionSuppressionUnusedImport() {
        compilesWithoutWarnings(Arrays.asList("-suppress-warnings", "unusedImport"), "UnusedImport.ceylon");
    }
    
    @Test
    public void testAnnoSuppressesNothing() {
        assertErrors(new String[]{"SuppressesNothing.ceylon"},
                defaultOptions,
                null,
                new CompilerError(Kind.WARNING, "", 1, "suppresses no warnings"));
    }
    
    @Test
    public void testAnnoAlreadySuppressed() {
        // We warn about warnings which are suppressed by an annotation on an outer declaration
        assertErrors(new String[]{"AlreadySuppressed.ceylon"},
                defaultOptions,
                null,
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
                new CompilerError(Kind.WARNING, "", 3, "warnings already suppressed by annotation"));
    }
    
    @Test
    public void testUnknownWarningInAnno() {
        assertErrors(new String[]{"UnknownWarningInAnno.ceylon"},
                defaultOptions,
                null,
                new CompilerError(Kind.WARNING, "", 1, "unknown warning: blahblah"));
    }
    

}
