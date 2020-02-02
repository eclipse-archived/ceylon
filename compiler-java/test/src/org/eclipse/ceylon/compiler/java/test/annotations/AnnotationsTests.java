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
package org.eclipse.ceylon.compiler.java.test.annotations;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.model.cmr.JDKUtils;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;

public class AnnotationsTests extends CompilerTests {
    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(String main){
        return new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.annotations", "1.2.3");
    }
    
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
    public void testConstructor(){
        compareWithJavaSource("Constructor");
    }
    @Test
    public void testConstructorClass(){
        compareWithJavaSource("ConstructorClass");
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
    @Test
    public void testUseSiteVariance(){
        compareWithJavaSource("UseSiteVariance");
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
    
    @Test
    public void testAnnotationTarget(){
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
        Class<?> klass = loadClass("org.eclipse.ceylon.compiler.java.test.annotations.modules.c.$module_",
                new ModuleWithArtifact("org.eclipse.ceylon.compiler.java.test.annotations.modules.c", "0.1"));
        System.out.println(Arrays.toString(klass.getField("org$eclipse$ceylon$module-resolver").getAnnotations()));
    }
    
    @Test
    public void testDeprecated(){
        compareWithJavaSource("Deprecated");
    }
    
    @Test
    public void testRecursiveConstructors(){
        // 1246, 6787
        compile("RecursiveConstructors.ceylon");
        compile("RecursiveConstructors2.ceylon");
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
    
    @Test
    public void testBug1565(){
        compareWithJavaSource("Bug1565");
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testHeuristicTargetUnambiguous(){
        // those cases where the intersection of the possible @Taget's
        // and the actual transformed outputs of the annotated element is 
        // a singletone: we can unambiguously determine which target 
        // to apply it to
        compile("Targets.java");
        compareWithJavaSource("HeuristicTargetUnambiguous");
    }
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testHeuristicTargetImpossible(){
        // those cases where the intersection of the possible @Taget's
        // and the actual transformed outputs of the annotated element is empty
        compile("Targets.java");
        compile("HeuristicTargetImpossible.ceylon");
    }
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testHeuristicTargetAmbiguous(){
        // those cases where the intersection of the possible @Taget's
        // and the actual transformed outputs of the annotated element has 
        // two or more elements
        compile("Targets.java");
        compareWithJavaSource("HeuristicTargetAmbiguous");
    }
    @Test
    public void testHeuristicTargetDisambiguated(){
        // those cases where the intersection of the possible @Taget's
        // and the actual transformed outputs of the annotated element has 
        // two or more elements
        compile("Targets.java");
        compareWithJavaSource("HeuristicTargetDisambiguated");
    }
    @Test
    public void testSetterTarget(){
        compile("Targets.java");
        compareWithJavaSource("SetterTarget");
    }
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void test5751(){
        compile("Targets.java");
        assertErrors("5751", true,
                CompilerError.warning(3, "ambiguous annotation target: methodTarget could be applied to several targets, use one of methodTarget__GETTER, methodTarget__SETTER to disambiguate"),
                CompilerError.warning(6, "ambiguous annotation target: methodTarget could be applied to several targets, use one of methodTarget__GETTER, methodTarget__SETTER to disambiguate"),
                CompilerError.warning(14, "the 'late' attribute 's4' cannot be properly initialized just by setting the field value because it has an optional type: depending on the semantics of 'fieldTarget' consider making it non-'late'"),
                CompilerError.warning(17, "the 'late' attribute 's5' cannot be properly initialized just by setting the field value because it has an optional type: depending on the semantics of 'fieldTarget' consider making it non-'late'"),
                CompilerError.warning(21, "the 'late' attribute 's6' cannot be properly initialized just by setting the field value because it has an optional type: depending on the semantics of 'fieldTarget' consider making it non-'late'"),
                CompilerError.warning(26, "the 'late' attribute 's7' cannot be properly initialized just by setting the field value because it has an optional type: depending on the semantics of 'fieldOrMethodTarget' consider annotating the JavaBean Property getter with fieldOrMethodTarget__GETTER or its setter with fieldOrMethodTarget__SETTER or making it non-'late'"),
                CompilerError.warning(28, "the 'late' attribute 's8' cannot be properly initialized just by setting the field value because it is erased to a primitive type: depending on the semantics of 'fieldOrMethodTarget' consider annotating the JavaBean Property getter with fieldOrMethodTarget__GETTER or its setter with fieldOrMethodTarget__SETTER or making it non-'late'")
                );
    }
    
    @Test
    public void testBug2029(){
        compareWithJavaSource("Bug2029");
    }
    
    @Test
    public void testBug2045(){
        compareWithJavaSource("Bug2045");
    }
    
    @Test
    public void testBug2116(){
        compareWithJavaSource("Bug2116");
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testSingletonConstructorTargets(){
        compile("Targets.java");
        compareWithJavaSource("SingletonConstructorTargets");
    }
    
    @Test
    @Ignore("Temporarily disabling for work on 1.3.4")
    public void testBug2160(){
        compile("Targets.java");
        compareWithJavaSource("Bug2160");
    }
    
    @Test
    public void testBug2315(){
        compareWithJavaSource("Bug2315");
    }
    
    @Test
    @Ignore
    public void testBug6085(){
        compareWithJavaSource("bug6085/Bug6085");
    }
    
    @Test
    public void testBug5779(){
        compareWithJavaSource("Bug5779");
    }
    
    @Test
    public void testRepeatable8(){
        Assume.assumeTrue(JDKUtils.jdk == JDKUtils.JDK.JDK8 || JDKUtils.jdk == JDKUtils.JDK.JDK9);
        ArrayList<String> options = new ArrayList<String>(defaultOptions);
        options.add("-target");
        options.add("8");
        compareWithJavaSource(options, "Repeatable8.src", "Repeatable.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.annotations.RepeatableUse");
    }
    @Test
    public void testRepeatable7(){
        ArrayList<String> options = new ArrayList<String>(defaultOptions);
        options.add("-target");
        options.add("7");
        options.add("-source");
        options.add("7");
        compareWithJavaSource(options, "Repeatable7.src", "Repeatable.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.annotations.RepeatableUse");
    }
    
    @Test
    public void testAnnotationCharacterSequence(){
        //compareWithJavaSource("AnnotationCharacterSequence");
        compile("AnnotationCharacterSequence.ceylon");
        run("org.eclipse.ceylon.compiler.java.test.annotations.AnnotationCharacterSequenceUse");
    }
    
    @Test
    public void testAnnotationSpreadArgs() {
        compareWithJavaSource("AnnotationSpreadArgs");
    }
    
    @Test
    public void testAnnotationContructorHiddenParams() {
        compareWithJavaSource("AnnotationConstructorHiddenParameters");
    }
 }
