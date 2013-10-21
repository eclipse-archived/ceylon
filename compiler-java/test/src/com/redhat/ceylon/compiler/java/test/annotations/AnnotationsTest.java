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

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class AnnotationsTest extends CompilerTest {
    @Test
    public void testTypeGrouping(){
        compareWithJavaSource("typeGrouping");
    }
    @Test
    public void testUnionTypeInfo(){
        compareWithJavaSource("UnionTypeInfo");
    }
    @Test
    public void testClass(){
        compareWithJavaSource("Klass");
    }
    @Test
    public void testAnonymousAnnotation(){
        compareWithJavaSource("AnonymousAnnotation");
    }
    @Test
    public void testInterface(){
        compareWithJavaSource("Interface");
    }
    @Test
    public void testMethod(){
        compareWithJavaSource("method");
    }
    @Test
    public void testAttribute(){
        compareWithJavaSource("attribute");
    }
    @Test
    public void testMemberClass(){
        compareWithJavaSource("MemberKlass");
    }
    @Test
    public void testMemberObject(){
        compareWithJavaSource("MemberObject");
    }
    @Test
    public void testKlassInMemberObject(){
        compareWithJavaSource("KlassInMemberObject");
    }
    @Test
    public void testLocalClass(){
        compareWithJavaSource("LocalKlass");
    }
    @Test
    public void testLocalMethod(){
        compareWithJavaSource("LocalMethod");
    }
    @Test
    public void testLocalObject(){
        compareWithJavaSource("LocalObject");
    }
    @Test
    public void testModule(){
        compareWithJavaSource("module");
    }
    @Test
    public void testTypeReferences(){
        compile("Interface.ceylon", "Klass.ceylon", "MemberKlass.ceylon", "KlassInMemberObject.ceylon");
        compareWithJavaSource("typereferences");
    }
    @Test
    public void testTypeParameters(){
        compareWithJavaSource("TypeParameters");
    }
    
    // User annotations
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
    public void testVariadicTricks(){
        compareWithJavaSource("VariadicTricks");
    }
    
    @Test
    public void testAnnotationClassMetamodelReference(){
        compareWithJavaSource("AnnotationClassMetamodelReference");
    }
    @Test
    public void testEnumeratedReference(){
        compareWithJavaSource("EnumeratedReference1");
        compareWithJavaSource("EnumeratedReference2");
    }
    @Test
    public void testEnumeratedReferenceAsDefaulted(){
        // 1247
        compareWithJavaSource("EnumeratedReferenceAsDefaulted");
    }
    @Test
    public void testQuoting(){
        compareWithJavaSource("Quoting_1");
        compareWithJavaSource("Quoting_2");
    }
    
    @Test
    public void testCollidingLiteralDefaults(){
        compareWithJavaSource("CollidingLiteralDefaults");
    }
    
    @Test
    public void testNested(){
        compareWithJavaSource("Nested");
        compareWithJavaSource("Nested_callsite");
    }
    
    @Test
    public void testAnnotationModelLoading(){
        compile("AnnotationConstructor.ceylon");
        compile("AnnotationConstructorUse.ceylon");
    }
    
    @Ignore("M6: Until it is implemented")
    @Test
    public void testAnnotationTarget_fail(){
        compareWithJavaSource("AnnotationTarget");
    }
    
    @Test
    public void testConstrained(){
        compareWithJavaSource("Constrained");
    }
    
    @Test
    public void testBug1277(){
        compareWithJavaSource("Bug1277");
    }
    
    @Test
    public void testBug1323(){
        compareWithJavaSource("Bug1323");
    }
    
    @Test
    public void testAnnotationModule() throws NoSuchFieldException, SecurityException{
        // TODO Add a package.ceylon, so we can check that too.
        compareWithJavaSource("modules/a/module");
        compareWithJavaSource("modules/a/package");
        compareWithJavaSource("modules/b/module");
        compareWithJavaSource("modules/c/module");
        Class<?> klass = loadClass("com.redhat.ceylon.compiler.java.test.annotations.modules.c.module_",
                new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.annotations.modules.c", "0.1"));
        System.out.println(Arrays.toString(klass.getField("com$redhat$ceylon$module-resolver").getAnnotations()));
    }
    
    @Test
    public void testDeprecated(){
        compareWithJavaSource("Deprecated");
    }
    
    @Test
    @Ignore("M6: #1246, pending decision about how to handle this case")
    public void testRecursiveConstructors(){
        // 1246
        compareWithJavaSource("RecursiveConstructors");
    }
    
    @Test
    public void testSequenceDefaults(){
        // 1254
        compareWithJavaSource("SequenceDefaults1");
        compareWithJavaSource("SequenceDefaults2");
    }
    
    @Test
    @Ignore("1.0?")
    public void testTuple(){
        // spec 768
        compareWithJavaSource("Tuple");
    }
    
    @Test
    @Ignore("1.0?")
    public void testParameterDefaults(){
        compareWithJavaSource("ParameterDefaults");
    }
    
    @Test
    public void testBug1384(){
        compareWithJavaSource("Bug1384");
    }
 }
