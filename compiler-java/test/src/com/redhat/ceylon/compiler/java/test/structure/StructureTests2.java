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
package com.redhat.ceylon.compiler.java.test.structure;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;

public class StructureTests2 extends CompilerTests {
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-2";
    }
    
    //
    // Toplevel
    
    @Test
    public void testTopToplevelAttribute(){
        compareWithJavaSource("toplevel/ToplevelAttribute");
    }
    @Test
    public void testTopToplevelAttributeLate(){
        compareWithJavaSource("toplevel/ToplevelAttributeLate");
    }
    @Test
    public void testTopToplevelAttributeGenerics(){
        compareWithJavaSource("toplevel/ToplevelAttributeGenerics");
    }
    @Test
    public void testTopToplevelAttributeShared(){
        compareWithJavaSource("toplevel/ToplevelAttributeShared");
    }
    @Test
    public void testTopToplevelGetter(){
        compareWithJavaSource("toplevel/ToplevelGetter");
    }
    @Test
    public void testTopToplevelGetterSetter(){
        compareWithJavaSource("toplevel/ToplevelGetterSetter");
    }
    @Test
    public void testTopToplevelMethods(){
        compareWithJavaSource("toplevel/ToplevelMethods");
    }
    @Test
    public void testTopToplevelMethodWithDefaultedParams(){
        compareWithJavaSource("toplevel/ToplevelMethodWithDefaultedParams");
    }
    @Test
    public void testTopToplevelFunctionWithVariableParameter(){
        compareWithJavaSource("toplevel/ToplevelFunctionWithVariableParameter");
    }
    @Test
    public void testTopToplevelObject(){
        compareWithJavaSource("toplevel/ToplevelObject");
    }
    @Test
    public void testTopToplevelObjectWithMembers(){
        compareWithJavaSource("toplevel/ToplevelObjectWithMembers");
    }
    @Test
    public void testTopToplevelObjectShared(){
        compareWithJavaSource("toplevel/ToplevelObjectShared");
    }
    @Test
    public void testTopToplevelObjectWithSupertypes(){
        compareWithJavaSource("toplevel/ToplevelObjectWithSupertypes");
    }
    @Test
    public void testTopToplevelVariable(){
        compareWithJavaSource("toplevel/ToplevelVariable");
    }
    @Test
    public void testTopToplevelVariableShared(){
        compareWithJavaSource("toplevel/ToplevelVariableShared");
    }
    @Test
    public void testMthTopLevelSpecifyingTopLevel(){
        compareWithJavaSource("toplevel/TopLevelSpecifyingTopLevel");
    }
    //
    // Type
    
    @Test
    public void testTypBasicTypes(){
        compareWithJavaSource("type/BasicTypes");
    }
    @Test
    public void testTypBottom(){
        compareWithJavaSource("type/Bottom");
    }
    @Test
    public void testTypGenericBottom(){
        compareWithJavaSource("type/GenericBottom");
    }
    @Test
    public void testTypConversions(){
        compareWithJavaSource("type/Conversions");
    }
    @Test
    public void testTypOptionalType(){
        compareWithJavaSource("type/OptionalType");
    }
    @Test
    public void testTypSequenceType(){
        compareWithJavaSource("type/SequenceType");
    }
    @Test
    public void testTypTupleType(){
        compareWithJavaSource("type/TupleType");
    }
    @Test
    public void testTypUnionSelfType(){
        compareWithJavaSource("type/UnionSelfType");
    }
    
    //
    // import
    
    @Test
    public void testImpImportAliasAndWildcard(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportAliasAndWildcard");
    }
    
    private void compileImportedPackage() {
        compile("importIt/pkg/C1.ceylon", "importIt/pkg/C2.ceylon");
    }

    @Test
    public void testImpImportAttrSingle(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportAttrSingle");
    }

    @Test
    public void testImpImportMethodSingle(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportMethodSingle");
    }
    
    @Test
    public void testImpImportTypeSingle(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportTypeSingle");
    }
    
    @Test
    public void testImpImportTypeMultiple(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportTypeMultiple");
    }
    
    @Test
    public void testImpImportTypeAlias(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportTypeAlias");
    }
    
    @Test
    public void testImpImportWildcard(){
        compileImportedPackage();
        compareWithJavaSource("importIt/ImportWildcard");
    }
    
    @Test
    public void testImpImportWildcardSinglePass(){
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(getPackagePath()+"/importIt/src");
        CeyloncTaskImpl task = getCompilerTask(options, "importIt/src/pkg/File.ceylon", "importIt/src/pkg/sub/ConcreteDirectory.ceylon");
        Assert.assertTrue(task.call());
        task = getCompilerTask(options, "importIt/src/pkg/File.ceylon", "importIt/src/pkg/sub/ConcreteDirectory.ceylon");
        Assert.assertTrue(task.call());
    }

    @Test
    public void testImpImportJavaRuntimeTypeSingle(){
        compareWithJavaSource("importIt/ImportJavaRuntimeTypeSingle");
    }

    @Test
    public void testImpImportJavaRuntimeTypeWildcard(){
        compareWithJavaSource("importIt/ImportJavaRuntimeTypeWildcard");
    }

    @Test
    public void testImpImportCeylonLanguageType(){
        compareWithJavaSource("importIt/ImportCeylonLanguageType");
    }
    
    @Test
    public void testImpImportObjectMembers(){
        compareWithJavaSource("importIt/ImportObjectMembers.src", "importIt/ImportObjectMembers.ceylon", "importIt/obj/Object.ceylon");
    }
    
    @Test
    public void testImpImportConstructor(){
        compareWithJavaSource("importIt/ImportConstructor.src", "importIt/ImportConstructor.ceylon", "importIt/ctor/Ctor.ceylon");
    }
    
    // Tests for nesting of declarations
    /*@Test
    public void testNstNesting(){
        compareWithJavaSource("nesting/Nesting");
    }*/
    @Test
    public void testNstCcc(){
        compareWithJavaSource("nesting/ccc/CCC");
    }
    @Test
    public void testNstCci(){
        compareWithJavaSource("nesting/cci/CCI");
    }
    @Test
    public void testNstCic(){
        compareWithJavaSource("nesting/cic/CIC");
    }
    @Test
    public void testNstCii(){
        compareWithJavaSource("nesting/cii/CII");
    }
    @Test
    public void testNstIcc(){
        compareWithJavaSource("nesting/icc/ICC");
    }
    @Test
    public void testNstIci(){
        compareWithJavaSource("nesting/ici/ICI");
    }
    @Test
    public void testNstIic(){
        compareWithJavaSource("nesting/iic/IIC");
    }
    @Test
    public void testNstIii(){
        compareWithJavaSource("nesting/iii/III");
    }
    @Test
    public void testNstLocals(){
        compareWithJavaSource("nesting/Locals");
    }
    
    @Test
    public void testNstClassMethodDefaultedParameter(){
        compareWithJavaSource("nesting/ClassMethodDefaultedParameter");
    }
    
    @Test
    public void testNstFunctionDefaultedParameter(){
        compareWithJavaSource("nesting/FunctionDefaultedParameter");
    }
    
    @Test
    public void testNstClassInitDefaultedParameter(){
        compareWithJavaSource("nesting/ClassInitDefaultedParameter");
    }
    
    @Test
    public void testNstNestedInterface(){
        compareWithJavaSource("nesting/NestedInterface");
    }
    
    @Test
    public void testNstObjects(){
        compareWithJavaSource("nesting/Objects");
    }
    
    @Test
    public void testNstInterfaceWithinObject(){
        compareWithJavaSource("nesting/InterfaceWithinObject");
    }
    
    @Test
    public void testNstInterfaceWithinFunction(){
        compareWithJavaSource("nesting/InterfaceWithinFunction");
    }
    
    @Test
    public void testNstMethodReturningInner(){
        compareWithJavaSource("nesting/MethodReturningInner");
    }

    // Tests for concrete members of interfaces
    @Test
    public void testCncConcrete(){
        compareWithJavaSource("concrete/Concrete");
    }
    @Test
    public void testCncConcreteAttribute(){
        compareWithJavaSource("concrete/ConcreteAttribute");
    }

    @Test
    public void testCncDefaultSetter(){
        compareWithJavaSource("concrete/DefaultSetter");
    }
    
    @Test
    public void testCncListImplementor(){
        compareWithJavaSource("concrete/ListImplementor");
    }
    
    @Test
    public void testCncNameCollision(){
        compareWithJavaSource("concrete/NameCollision");
    }

    @Test
    public void testCncUnionTypeArg(){
        compareWithJavaSource("concrete/UnionTypeArg");
    }
    
    @Test
    public void testCncRaw(){
        compareWithJavaSource("concrete/Raw");
    }
    
    @Test
    public void testCncThis(){
        compareWithJavaSource("concrete/This");
    }
    
    @Test
    public void testCncConcreteInterface(){
        compareWithJavaSource("concrete/ConcreteInterface");
    }

    @Test
    public void testCncInterfaceMethodDefaultedParameter(){
        compareWithJavaSource("concrete/InterfaceMethodDefaultedParameter");
    }

    @Test
    public void testCncInterfaceErasure(){
        compareWithJavaSource("concrete/InterfaceErasure");
    }

    @Test
    public void testCncConcreteMethodBySpecification(){
        compareWithJavaSource("concrete/ConcreteMethodBySpecification");
    }
    
    @Test
    public void testCncSatisfaction(){
        compareWithJavaSource("concrete/Satisfaction");
    }
    
    @Test
    public void testCncConcreteGetter(){
        compareWithJavaSource("concrete/ConcreteGetter");
    }
    
    @Test
    public void testCncAbstractSatisfier(){
        compareWithJavaSource("concrete/AbstractSatisfier");
    }
    
    @Test
    public void testCncIntersectionSatisfier(){
        compareWithJavaSource("concrete/IntersectionSatisfier");
    }
    
    @Test
    public void testCncValueRefiningGetterSetter(){
        compile("concrete/ValueRefiningGetterSetter.ceylon");
    }
    
    @Test
    public void testCncMultipleInheritance(){
        compareWithJavaSource("concrete/MultipleInheritance");
    }
    
    @Test
    public void testCncLazySpec(){
        compareWithJavaSource("concrete/LazySpec");
    }

    @Test
    public void testCncCapturedTypeParam(){
        compareWithJavaSource("concrete/CapturedTypeParam");
    }
    
    @Test
    public void testCncInterfaceQualifiedMembers(){
        compareWithJavaSource("concrete/InterfaceQualifiedMembers");
    }
    
    @Test
    public void testCncString(){
        compareWithJavaSource("concrete/ConcreteString");
        run("com.redhat.ceylon.compiler.java.test.structure.concrete.concreteString");
    }
    
    @Test
    public void testCncAppliedTypeDefaultedParameter(){
        compareWithJavaSource("concrete/AppliedTypeDefaultedParameter");
    }
    
    @Test
    public void testCncRefinedMethodWithDefaultTypeParameter(){
        compareWithJavaSource("concrete/RefinedMethodWithDefaultTypeParameter");
    }
    
    @Test
    public void testCncBug2316(){
        compareWithJavaSource("concrete/Bug2316");
        run("com.redhat.ceylon.compiler.java.test.structure.concrete.bug2316");
    }
}
