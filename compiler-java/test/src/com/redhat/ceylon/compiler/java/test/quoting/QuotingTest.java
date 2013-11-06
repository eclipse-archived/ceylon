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
        compareWithJavaSource("goto/KeywordInPackage");
    }
    
    @Test
    public void testTwoKeywordsInPackage(){
        compareWithJavaSource("goto/transient/TwoKeywordsInPackage");
    }
    
    @Test
    public void testKeywordInClassValue(){
        compareWithJavaSource("goto/KeywordInClassValue");
    }
    
    @Test
    public void testKeywordInToplevelValue(){
        compareWithJavaSource("goto/KeywordInToplevelValue");
    }
    
    @Test
    public void testKeywordInClassGetter(){
        compareWithJavaSource("goto/KeywordInClassGetter");
    }
    
    @Test
    public void testKeywordInToplevelGetter(){
        compareWithJavaSource("goto/KeywordInToplevelGetter");
    }
    
    @Test
    public void testKeywordInClassMethod(){
        compareWithJavaSource("goto/KeywordInClassMethod");
    }
    
    @Test
    public void testKeywordInToplevelMethod(){
        compareWithJavaSource("goto/KeywordInToplevelMethod");
    }
    
    @Test
    public void testKeywordInToplevelObject(){
        compareWithJavaSource("goto/KeywordInToplevelObject");
    }
    
    @Test
    public void testKeywordInClassObject(){
        compareWithJavaSource("goto/KeywordInClassObject");
    }
    
    @Test
    public void testKeywordInMethodObject(){
        compareWithJavaSource("goto/KeywordInMethodObject");
    }
    
    @Test
    public void testKeywordInGetterObject(){
        compareWithJavaSource("goto/KeywordInGetterObject");
    }
    
    @Test
    public void testKeywordInSetterObject(){
        compareWithJavaSource("goto/KeywordInSetterObject");
    }
    
    @Test
    public void testKeywordInImport(){
        compareWithJavaSource("goto/KeywordInImport.src", "goto/KeywordInImport.ceylon", "goto/transient/TwoKeywordsInPackage.ceylon");
    }
    
    @Test
    public void testKeywordInInnerClassContainer(){
        compareWithJavaSource("goto/KeywordInInnerClassContainer");
    }
    
    @Test
    public void testKeywordInToplevelAssignment(){
        compareWithJavaSource("goto/KeywordInToplevelAssignment");
    }
    
    @Test
    public void testMangledToplevelAttribute(){
        compareWithJavaSource("goto/MangledToplevelAttribute");
    }
    
    @Test
    public void testMangledToplevelMethod(){
        compareWithJavaSource("goto/MangledToplevelMethod");
    }
    
    @Test
    public void testMangledMembers(){
        compareWithJavaSource("goto/MangledMembers");
    }
    
    @Test
    public void testKeywordInCallable(){
        compareWithJavaSource("goto/KeywordInCallable");
    }
    
    @Test
    public void testKeywordInInnerClass(){
        compareWithJavaSource("goto/KeywordInInnerClass");
    }
    
    @Test
    public void testKeywordInAnnotationClass(){
        compareWithJavaSource("goto/KeywordInAnnotation");
    }
    
    @Test
    public void testKeywordInAnnotation(){
        compareWithJavaSource("goto/KeywordInAnnotation");
    }
    
    @Test
    public void testCaseCollision(){
        try {
            compileAndRun("com.redhat.ceylon.compiler.java.test.quoting.$goto.CaseCollision", "goto/CaseCollision.ceylon");
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getCause().getCause().getMessage().contains("i am the class"));
        }
        try {
            compileAndRun("com.redhat.ceylon.compiler.java.test.quoting.$goto.caseCollision", "goto/CaseCollision.ceylon");
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
    public void testCaseLowerInterface(){
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

    @Test
    public void testQuotedTopLevel(){
        compile("goto/KeywordInToplevelMethod.ceylon");
        compile("goto/KeywordInTopLevelUsage.ceylon");
    }
}