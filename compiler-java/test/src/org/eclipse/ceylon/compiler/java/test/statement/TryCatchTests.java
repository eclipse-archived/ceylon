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
package org.eclipse.ceylon.compiler.java.test.statement;

import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.junit.Test;

public class TryCatchTests extends CompilerTests {

    @Override
    protected String transformDestDir(String name) {
        return name + "-trycatch";
    }
    
    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main) {
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.statement.trycatch", "1");
    }

    @Test
    public void testTryExceptionTypes(){
        compile("trycatch/JavaThrower.java");
        compareWithJavaSource("trycatch/ExceptionTypes");
    }
    
    @Test
    public void testTryExceptionStrings(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.statement.trycatch.exceptionStrings",
                "trycatch/ExceptionStrings.ceylon");
    }
    
    @Test
    public void testTryExceptionAttr(){
        compareWithJavaSource("trycatch/ExceptionAttr");
    }
    
    @Test
    public void testTryExceptionAttributes(){
        compareWithJavaSource("trycatch/ExceptionAttributes");
    }
    
    @Test
    public void testTryExceptionSuppressed(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.statement.trycatch.exceptionSuppressed",
                "trycatch/ExceptionSuppressed.ceylon");
    }
    
    @Test
    public void testTryBareThrow(){
        compareWithJavaSource("trycatch/Throw");
    }
    
    @Test
    public void testTryThrowNothing(){
        compareWithJavaSource("trycatch/ThrowNothing");
    }

    @Test
    public void testTryThrowException(){
        compareWithJavaSource("trycatch/ThrowException");
    }
    
    @Test
    public void testTryThrowExceptionNamedArgs(){
        compareWithJavaSource("trycatch/ThrowExceptionNamedArgs");
    }
    
    @Test
    public void testTryThrowExceptionSubclass(){
        compareWithJavaSource("trycatch/ThrowExceptionSubclass");
    }
    
    @Test
    public void testTryThrowMethodResult(){
        compareWithJavaSource("trycatch/ThrowMethodResult");
    }
    
    @Test
    public void testTryThrowThrowable(){
        compareWithJavaSource("trycatch/ThrowThrowable");
    }
    
    @Test
    public void testTryThrowNpe(){
        compareWithJavaSource("trycatch/ThrowNpe");
    }
    
    @Test
    public void testTryTryFinally(){
        compareWithJavaSource("trycatch/TryFinally");
    }
    
    @Test
    public void testTryTryCatch(){
        compareWithJavaSource("trycatch/TryCatch");
    }
    
    @Test
    public void testTryTryCatchError(){
        compareWithJavaSource("trycatch/TryCatchError");
        run("org.eclipse.ceylon.compiler.java.test.statement.trycatch.tryCatchError");
    }
    
    @Test
    public void testTryTryCatchErrorAssertionError(){
        compareWithJavaSource("trycatch/TryCatchErrorAssertionError");
        run("org.eclipse.ceylon.compiler.java.test.statement.trycatch.tryCatchErrorAssertionError");
    }
    
    @Test
    public void testTryTryCatchFinally(){
        compareWithJavaSource("trycatch/TryCatchFinally");
    }
    
    @Test
    public void testTryTryCatchSubclass(){
        compareWithJavaSource("trycatch/TryCatchSubclass");
    }
    
    @Test
    public void testTryTryCatchUnion(){
        compareWithJavaSource("trycatch/TryCatchUnion");
    }
    
    @Test
    public void testTryTryCatchGenericIntersection(){
        compareWithJavaSource("trycatch/TryCatchGenericIntersection");
        //compile("trycatch/TryCatchGenericIntersection.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.statement.trycatch.tryCatchGenericIntersection");
    }
    
    @Test
    public void testTryTryWithResource(){
        compareWithJavaSource("trycatch/TryWithResource");
    }
    
    @Test
    public void testTryReplaceExceptionAtJavaCallSite(){
        compile("trycatch/JavaThrower.java");
        compareWithJavaSource("trycatch/WrapExceptionAtJavaCallSite");
    }
    
    @Test
    public void testTryTrySelfSuppression(){
        compileAndRun("org.eclipse.ceylon.compiler.java.test.statement.trycatch.trySelfSuppression", 
                "trycatch/TrySelfSuppression.ceylon");
    }

}
