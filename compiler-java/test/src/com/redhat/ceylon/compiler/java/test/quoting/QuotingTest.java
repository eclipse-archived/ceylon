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
package com.redhat.ceylon.compiler.java.test.quoting;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class QuotingTest extends CompilerTest {
    
    //
    // Packages
    
    @Test
    public void testKeywordInPackage(){
        compareWithJavaSource("assert/KeywordInPackage");
    }
    
    @Test
    public void testTwoKeywordsInPackage(){
        compareWithJavaSource("assert/transient/TwoKeywordsInPackage");
    }
    
    @Test
    public void testKeywordInClassValue(){
        compareWithJavaSource("assert/KeywordInClassValue");
    }
    
    @Test
    public void testKeywordInToplevelValue(){
        compareWithJavaSource("assert/KeywordInToplevelValue");
    }
    
    @Test
    public void testKeywordInClassGetter(){
        compareWithJavaSource("assert/KeywordInClassGetter");
    }
    
    @Test
    public void testKeywordInToplevelGetter(){
        compareWithJavaSource("assert/KeywordInToplevelGetter");
    }
    
    @Test
    public void testKeywordInClassMethod(){
        compareWithJavaSource("assert/KeywordInClassMethod");
    }
    
    @Test
    public void testKeywordInToplevelMethod(){
        compareWithJavaSource("assert/KeywordInToplevelMethod");
    }
    
    @Test
    public void testKeywordInToplevelObject(){
        compareWithJavaSource("assert/KeywordInToplevelObject");
    }
    
    @Test
    public void testKeywordInClassObject(){
        compareWithJavaSource("assert/KeywordInClassObject");
    }
    
    @Test
    public void testKeywordInMethodObject(){
        compareWithJavaSource("assert/KeywordInMethodObject");
    }
    
    @Test
    public void testKeywordInGetterObject(){
        compareWithJavaSource("assert/KeywordInGetterObject");
    }
    
    @Test
    public void testKeywordInSetterObject(){
        compareWithJavaSource("assert/KeywordInSetterObject");
    }
    
    @Test
    public void testKeywordInImport(){
        compareWithJavaSource("assert/KeywordInImport.src", "assert/KeywordInImport.ceylon", "assert/transient/TwoKeywordsInPackage.ceylon");
    }
    
    @Test
    public void testKeywordInInnerClassContainer(){
        compareWithJavaSource("assert/KeywordInInnerClassContainer");
    }
    
    @Test
    public void testKeywordInToplevelAssignment(){
        compareWithJavaSource("assert/KeywordInToplevelAssignment");
    }
    
    @Test
    public void testMangledToplevelAttribute(){
        compareWithJavaSource("assert/MangledToplevelAttribute");
    }
    
    @Test
    public void testMangledToplevelMethod(){
        compareWithJavaSource("assert/MangledToplevelMethod");
    }
    
    @Test
    public void testKeywordInCallable(){
        compareWithJavaSource("assert/KeywordInCallable");
    }
    
    @Test
    public void testKeywordInInnerClass(){
        compareWithJavaSource("assert/KeywordInInnerClass");
    }
    
    @Test
    public void testCaseCollision(){
        try {
            compileAndRun("com.redhat.ceylon.compiler.java.test.quoting.$assert.CaseCollision", "assert/CaseCollision.ceylon");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getCause().getCause().getMessage().contains("i am the class"));
        }
        try {
            compileAndRun("com.redhat.ceylon.compiler.java.test.quoting.$assert.caseCollision", "assert/CaseCollision.ceylon");
        } catch (Exception e) {
            Assert.assertTrue(e.getCause().getCause().getMessage().contains("i am the method"));
        }
    }
    
    @Test
    public void testCaseLowerClass(){
        compareWithJavaSource("lettercase/LowerClass");
        compile("lettercase/LowerClassUsage.ceylon");
    }
    
    @Test
    public void testCaseLowerInterface_fail(){
        compareWithJavaSource("lettercase/LowerInterface");
        compile("lettercase/LowerInterfaceUsage.ceylon");
    }
    
    @Test
    public void testCaseUpperAttribute(){
        compareWithJavaSource("lettercase/UpperAttribute");
        compile("lettercase/UpperAttributeUsage.ceylon");
    }
    
    @Test
    public void testCaseUpperMethod(){
        compareWithJavaSource("lettercase/UpperMethod");
        compile("lettercase/UpperMethodUsage.ceylon");
    }
    
    @Test
    public void testCaseUpperObject(){
        compareWithJavaSource("lettercase/UpperObject");
        compile("lettercase/UpperObjectUsage.ceylon");
    }
}