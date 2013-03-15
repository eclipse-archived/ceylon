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
package com.redhat.ceylon.compiler.java.test.annotations;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class UserAnnotationsTest extends CompilerTest {
    
    @Test
    public void testAnnotationClassNullary(){
        compareWithJavaSource("AnnotationClassNullary");
    }
    @Test
    public void testAnnotationClassString(){
        compareWithJavaSource("AnnotationClassString");
    }
    
    @Test
    public void testAnnotationClassInteger(){
        compareWithJavaSource("AnnotationClassInteger");
    }
    @Test
    public void testAnnotationClassBoolean(){
        compareWithJavaSource("AnnotationClassBoolean");
    }
    @Test
    public void testAnnotationClassCharacter(){
        compareWithJavaSource("AnnotationClassCharacter");
    }
    @Test
    public void testAnnotationClassFloat(){
        compareWithJavaSource("AnnotationClassFloat");
    }
    
    @Test
    public void testAnnotationClassAnnotationClass(){
        compareWithJavaSource("AnnotationClassAnnotationClass");
    }
    
    @Test
    public void testAnnotationModelLoading(){
        compile("AnnotationConstructor.ceylon");
        compile("AnnotationConstructorUse.ceylon");
    }
    
    // TODO Commit what I have
    // TODO rebase what I have
    // TODO Specify what we actually support
    // TODO Bootstrap
 // TODODONE tighten up the AnnotationConstructorVisitor
 // So it rejects stuff which we don't currently support
 // TODODONE Fix varargs, sequences and iterables 
 // TODODONE Add the $annotation method to annotation classes (or is it constructors...?)
 // TODO If we're supporting static arguments why can't we support defaulted parameters too?
    // TODO invocations of simple ctors and instantiation of annotation classes 
    // as simple invocation arguments.
 // TODO Think about the transformation of non-simple annotation constructors
 // and invocations
 // TODO Interop
    
    /* TODO Defaults @Test
    public void testAnnotationClassStringDefaulted(){
        compareWithJavaSource("AnnotationClassStringDefaulted");
    }
    @Test
    public void testAnnotationClassIntegerDefaulted(){
        compareWithJavaSource("AnnotationClassIntegerDefaulted");
    }
    @Test
    public void testAnnotationClassBooleanDefaulted(){
        compareWithJavaSource("AnnotationClassBooleanDefaulted");
    }
    @Test
    public void testAnnotationClassCharacterDefaulted(){
        compareWithJavaSource("AnnotationClassCharacterDefaulted");
    }
    @Test
    public void testAnnotationClassFloatDefaulted(){
        compareWithJavaSource("AnnotationClassFloatDefaulted");
    }
    
    @Test
    public void testAnnotationClassAnnotationClassDefaulted(){
        compareWithJavaSource("AnnotationClassAnnotationClassDefaulted");
    }*/
    /*
    @Test
    public void testAnnotationClassSequencesAndIterables(){
        compareWithJavaSource("AnnotationClassSequencesAndIterables");
    }
    */
    /*
    @Test
    public void testConstructorArguments(){
        compareWithJavaSource("ConstructorArguments");
    }
    
    @Test
    public void testDocAnnotation(){
        compareWithJavaSource("DocAnnotation");
    }
    
    @Test
    public void testByAnnotation(){
        compareWithJavaSource("ByAnnotation");
    }
    */
    
 }