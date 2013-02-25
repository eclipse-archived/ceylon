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
package com.redhat.ceylon.compiler.java.test.recovery;

import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class RecoveryTest extends CompilerTest {
    
    @Test
    public void testRcvBrokenClass(){
        compile(3, "BrokenClass.ceylon");
    }

    @Test
    public void testRcvBrokenMethod(){
        compile(3, "BrokenMethod.ceylon");
    }

    @Test
    public void testRcvBrokenAttribute(){
        compile(10, "BrokenAttribute.ceylon");
    }

    @Test
    public void testRcvClassWithBrokenMembers(){
        compile(30, "ClassWithBrokenMembers.ceylon");
    }

    @Test
    public void testRcvDuplicateDeclarations(){
        // this is https://github.com/ceylon/ceylon-compiler/issues/250
        compile(1, "DuplicateDeclaration1.ceylon", "DuplicateDeclaration2.ceylon");
    }

    @Test
    public void testRcvM3Features(){
        compile(1, "M3Features.ceylon");
    }

    private void compile(int expectedErrors, String... ceylon){
        DiagnosticCollector<JavaFileObject> errorCollector = new DiagnosticCollector<JavaFileObject>();
        // Stef: can't seem to be able to get this cast right no matter what I try
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Boolean success = getCompilerTask(defaultOptions, (DiagnosticListener)errorCollector , ceylon).call();
        Assert.assertEquals(expectedErrors, getErrorCount(errorCollector));
        Assert.assertFalse(success);
    }

    private int getErrorCount(DiagnosticCollector<JavaFileObject> errorCollector) {
        int errors = 0;
        for(Diagnostic<? extends JavaFileObject> diagnostic : errorCollector.getDiagnostics()){
            if(diagnostic.getKind() == Kind.ERROR)
                errors++;
            if(diagnostic.getSource() != null){
                System.err.println("("+diagnostic.getKind()+") "+diagnostic.getSource().getName()
                        +"["+diagnostic.getLineNumber()+","+diagnostic.getColumnNumber()+"]: "
                        +diagnostic.getMessage(Locale.getDefault()));
            }else{
                System.err.println("("+diagnostic.getKind()+"): "
                        +diagnostic.getMessage(Locale.getDefault()));
            }
        }
        return errors;
    }

}