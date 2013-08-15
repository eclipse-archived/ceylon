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

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class StructureTest extends CompilerTest {
    
    //
    // Packages
    
    @Test
    public void testPkgPackage(){
        compareWithJavaSource("pkg/pkg");
    }

    @Test
    public void testPkgPackageMetadata(){
        compareWithJavaSource("pkg/package");
    }

    //
    // Modules
    
    @Test
    public void testMdlModule(){
        compareWithJavaSource("modules/single/module");
    }

    //
    // Attributes
    
    @Test
    public void testAtrClassAttribute(){
        compareWithJavaSource("attribute/ClassAttribute");
    }
    @Test
    public void testAtrClassAttributeLate(){
        compareWithJavaSource("attribute/ClassAttributeLate");
    }
    @Test
    public void testAtrClassAttributeWithInitializer(){
        compareWithJavaSource("attribute/ClassAttributeWithInitializer");
    }
    @Test
    public void testAtrClassAttributeGetter(){
        compareWithJavaSource("attribute/ClassAttributeGetter");
    }
    @Test
    public void testAtrClassAttributeGetterSetter(){
        compareWithJavaSource("attribute/ClassAttributeGetterSetter");
    }
    @Test
    public void testAtrClassVariable(){
        compareWithJavaSource("attribute/ClassVariable");
    }
    @Test
    public void testAtrClassVariableWithInitializer(){
        compareWithJavaSource("attribute/ClassVariableWithInitializer");
    }
    @Test
    public void testAtrInnerAttributeGetter(){
        compareWithJavaSource("attribute/InnerAttributeGetter");
    }
    @Test
    public void testAtrInnerAttributeGetterSetter(){
        compareWithJavaSource("attribute/InnerAttributeGetterSetter");
    }
    @Test
    public void testAtrInnerAttributeGetterLateInitialisation(){
        compareWithJavaSource("attribute/InnerAttributeGetterLateInitialisation");
    }
    @Test
    public void testAtrClassAttributeWithConflictingMethods(){
        compareWithJavaSource("attribute/ClassAttributeWithConflictingMethods");
    }
    @Test
    public void testAtrInnerAttributeGetterWithConflictingMethods(){
        compareWithJavaSource("attribute/InnerAttributeGetterWithConflictingMethods");
    }
    @Test
    public void testAtrFormalFunctionAttribute(){
        compareWithJavaSource("attribute/FormalFunctionAttribute");
    }
    @Test
    public void testAtrAttributeHiding() {
        compareWithJavaSource("attribute/AttributeHiding");
    }
    @Test
    public void testAtrParametersAndMembers(){
        compareWithJavaSource("attribute/ParametersAndMembers");
    }
    
    //
    // Classes
    
    @Test
    public void testKlsAbstractFormal(){
        compareWithJavaSource("klass/AbstractFormal");
    }
    @Test
    public void testKlsFinal(){
        compareWithJavaSource("klass/Final");
    }
    @Test
    public void testKlsCaseTypes(){
        compareWithJavaSource("klass/CaseTypes");
    }
    @Test
    public void testKlsDefaultedInitializerParameter(){
        compareWithJavaSource("klass/DefaultedInitializerParameter");
    }
    @Test
    public void testKlsExtends(){
        compareWithJavaSource("klass/Extends");
    }
    @Test
    public void testKlsExtendsGeneric(){
        compareWithJavaSource("klass/ExtendsGeneric");
    }
    @Test
    public void testKlsInitializerParameter(){
        compareWithJavaSource("klass/InitializerParameter");
    }
    @Test
    public void testKlsInitializerVarargs(){
        compareWithJavaSource("klass/InitializerVarargs");
    }
    @Test
    public void testKlsInnerClass(){
        compareWithJavaSource("klass/InnerClass");
    }
    @Test
    public void testKlsInterface(){
        compareWithJavaSource("klass/Interface");
    }
    @Test
    public void testKlsInterfaceWithConcreteMembers(){
        compareWithJavaSource("klass/InterfaceWithConcreteMembers");
    }
    @Test
    public void testKlsInterfaceWithMembers(){
        compareWithJavaSource("klass/InterfaceWithMembers");
    }
    @Test
    public void testKlsClass(){
        compareWithJavaSource("klass/Klass");
    }
    @Test
    public void testKlsVariadic(){
        compareWithJavaSource("klass/Variadic");
    }
    @Test
    public void testKlsKlassMethodTypeParams(){
        compareWithJavaSource("klass/KlassMethodTypeParams");
    }
    @Test
    public void testKlsKlassTypeParams(){
        compareWithJavaSource("klass/KlassTypeParams");
    }
    @Test
    public void testKlsKlassTypeParamsSatisfies(){
        compareWithJavaSource("klass/KlassTypeParamsSatisfies");
    }
    @Test
    public void testKlsKlassWithObjectMember(){
        compareWithJavaSource("klass/KlassWithObjectMember");
    }
    @Test
    public void testKlsLocalClass(){
        compareWithJavaSource("klass/LocalClass");
    }
    @Test
    public void testKlsDoublyLocalClass(){
        compareWithJavaSource("klass/DoublyLocalClass");
    }
    @Test
    public void testKlsLocalClassWithLocalObject(){
        compareWithJavaSource("klass/LocalClassWithLocalObject");
    }
    @Test
    public void testKlsPublicClass(){
        compareWithJavaSource("klass/PublicKlass");
    }
    @Test
    public void testKlsSatisfies(){
        compareWithJavaSource("klass/Satisfies");
    }
    @Test
    public void testKlsSatisfiesErasure(){
        compareWithJavaSource("klass/SatisfiesErasure");
    }
    @Test
    public void testKlsSatisfiesGeneric(){
        compareWithJavaSource("klass/SatisfiesGeneric");
    }
    @Test
    public void testKlsSatisfiesWithMembers(){
        compareWithJavaSource("klass/SatisfiesWithMembers");
    }
    @Test
    public void testKlsRefinedVarianceInheritance(){
        // See https://github.com/ceylon/ceylon-compiler/issues/319
        //compareWithJavaSource("klass/RefinedVarianceInheritance");
        compileAndRun("com.redhat.ceylon.compiler.java.test.structure.klass.rvi_run", "klass/RefinedVarianceInheritance.ceylon");
    }
    @Test
    public void testKlsRefinedVarianceInheritance2(){
        // See https://github.com/ceylon/ceylon-compiler/issues/354
        compareWithJavaSource("klass/RefinedVarianceInheritance2");
    }
    @Test
    public void testKlsRefinementAndIntersection(){
        // See https://github.com/ceylon/ceylon-compiler/issues/651
        compareWithJavaSource("klass/RefinementAndIntersection");
    }
    @Test
    public void testKlsRefinementIntersectionComposition(){
        // See https://github.com/ceylon/ceylon-compiler/issues/696
        compareWithJavaSource("klass/RefinementIntersectionComposition");
    }
    @Test
    public void testKlsRefinementWidening(){
        compareWithJavaSource("klass/RefinementWidening");
    }
    @Test
    public void testKlsRefinementNarrowing(){
        compareWithJavaSource("klass/RefinementNarrowing");
    }
    @Test
    public void testKlsVariance(){
        compareWithJavaSource("klass/Variance");
    }
    @Test
    public void testKlsObjectInMethod(){
        compareWithJavaSource("klass/ObjectInMethod");
    }
    @Test
    public void testKlsObjectInStatement(){
        compareWithJavaSource("klass/ObjectInStatement");
    }
    @Test
    public void testKlsInitializerObjectInStatement(){
        compareWithJavaSource("klass/InitializerObjectInStatement");
    }
    @Test
    public void testKlsKlassInStatement(){
        compareWithJavaSource("klass/KlassInStatement");
    }
    @Test
    public void testKlsInitializerKlassInStatement(){
        compareWithJavaSource("klass/InitializerKlassInStatement");
    }
    @Test
    public void testKlsObjectInGetter(){
        compareWithJavaSource("klass/ObjectInGetter");
    }
    @Test
    public void testKlsObjectInSetter(){
        compareWithJavaSource("klass/ObjectInSetter");
    }
    @Test
    public void testKlsClassInGetter(){
        compareWithJavaSource("klass/KlassInGetter");
    }
    @Test
    public void testKlsClassInSetter(){
        compareWithJavaSource("klass/KlassInSetter");
    }
    @Test
    public void testKlsInnerClassUsingOutersTypeParam(){
        compareWithJavaSource("klass/InnerClassUsingOutersTypeParam");
    }
    @Test
    public void testKlsInnerClassUsingOutersTypeParam2(){
        compareWithJavaSource("klass/InnerClassUsingOutersTypeParam2");
    }
    @Test
    public void testKlsUninitializedMethod(){
        compareWithJavaSource("klass/UninitializedMethod");
    }
    
    @Test
    public void testKlsDeferredMethodInitialization(){
        compareWithJavaSource("klass/DeferredMethodInitialization");
    }
    
    @Test
    public void testKlsDeferredMethodInitializationMultipleSpecification(){
        compareWithJavaSource("klass/DeferredMethodInitializationMultipleSpecification");
    }
    
    @Test
    public void testKlsDeferredFunctionInitialization(){
        compareWithJavaSource("klass/DeferredFunctionInitialization");
    }
    @Test
    public void testKlsTypeParamRename(){
        compareWithJavaSource("klass/TypeParamRename");
    }
    
    @Test
    public void testKlsMethodInitializerParameter(){
        compareWithJavaSource("klass/MethodInitializerParameter");
    }
    
    @Test
    public void testKlsFunctionalParameter(){
        compareWithJavaSource("klass/FunctionalParameter");
    }
    @Test
    public void testKlsFunctionalParameterActual(){
        compareWithJavaSource("klass/FunctionalParameterActual");
    }
    @Test
    public void testKlsFunctionalParameterDefault(){
        compareWithJavaSource("klass/FunctionalParameterDefault");
    }
    @Test
    public void testKlsFunctionalParameterNotCaptured(){
        compareWithJavaSource("klass/FunctionalParameterNotCaptured");
    }
    
    @Test
    public void testKlsOverrideDefaultedInitParam(){
        compareWithJavaSource("klass/OverrideDefaultedInitParam");
    }
    
    @Test
    public void testKlsRefiningVoidMembers(){
        compareWithJavaSource("klass/RefiningVoidMembers");
    }
    
    @Test
    public void testKlsKlassHiding(){
        compareWithJavaSource("klass/KlassHiding");
    }
    
    @Test
    public void testKlsSharedParameter(){
        compareWithJavaSource("klass/SharedParameter");
    }
    
    @Test
    public void testKlsActualParameter(){
        compareWithJavaSource("klass/ActualParameter");
    }
    
    @Test
    public void testKlsDefaultParameter(){
        compareWithJavaSource("klass/DefaultParameter");
    }
    @Test
    public void testKlsSubclassException(){
        compareWithJavaSource("klass/SubclassException");
    }
    //
    // Methods
    
    @Test
    public void testMthActualMethodShortcut(){
        compareWithJavaSource("method/ActualMethodShortcut");
    }
    @Test
    public void testMthMethodRefinementWithSpecifiers(){
        compareWithJavaSource("method/MethodRefinementWithSpecifiers");
    }
    @Test
    public void testMthDefaultMethodSpecified(){
        compareWithJavaSource("method/DefaultMethodSpecified");
    }
    @Test
    public void testMthLocalMethod(){
        compareWithJavaSource("method/LocalMethod");
    }
    @Test
    public void testMthMethod(){
        compareWithJavaSource("method/Method");
    }
    @Test
    public void testMthVariadic(){
        compareWithJavaSource("method/Variadic");
    }
    @Test
    public void testMthMethodErasure(){
        compareWithJavaSource("method/MethodErasure");
    }
    @Test
    public void testMthMethodTypeParams(){
        compareWithJavaSource("method/MethodTypeParams");
    }
    @Test
    public void testMthMethodWithDefaultParams(){
        compareWithJavaSource("method/MethodWithDefaultParams");
    }
    @Test
    public void testMthMethodWithLocalObject(){
        compareWithJavaSource("method/MethodWithLocalObject");
    }
    @Test
    public void testMthMethodWithParam(){
        compareWithJavaSource("method/MethodWithParam");
    }
    @Test
    public void testMthMethodWithVarargs(){
        compareWithJavaSource("method/MethodWithVarargs");
    }
    @Test
    public void testMthPublicMethod(){
        compareWithJavaSource("method/PublicMethod");
    }
    @Test
    public void testMthFunctionInStatement(){
        compareWithJavaSource("method/FunctionInStatement");
    }
    @Test
    public void testMthFunctionInGetter(){
        compareWithJavaSource("method/FunctionInGetter");
    }
    @Test
    public void testMthFunctionInSetter(){
        compareWithJavaSource("method/FunctionInSetter");
    }
    @Test
    public void testMthMethodSpecifyingNullaryTopLevel(){
        compareWithJavaSource("method/MethodSpecifyingNullaryTopLevel");
    }
    @Test
    public void testMthMethodSpecifyingUnaryTopLevel(){
        compareWithJavaSource("method/MethodSpecifyingUnaryTopLevel");
    }
    @Test
    public void testMthMethodSpecifyingTopLevelWithResult(){
        compareWithJavaSource("method/MethodSpecifyingTopLevelWithResult");
    }
    @Test
    public void testMthMethodSpecifyingCallable(){
        compareWithJavaSource("method/MethodSpecifyingCallable");
    }
    @Test
    public void testMthMethodSpecifyingInitializer(){
        compareWithJavaSource("method/MethodSpecifyingInitializer");
    }
    
    @Test
    public void testMthMethodSpecifyingTopLevelWithTypeParam(){
        compareWithJavaSource("method/MethodSpecifyingTopLevelWithTypeParam");
    }
    @Test
    public void testMthMethodSpecifyingTopLevelWithTypeParamMixed(){
        compareWithJavaSource("method/MethodSpecifyingTopLevelWithTypeParamMixed");
    }
    @Test
    public void testMthMethodSpecifyingMethod(){
        compareWithJavaSource("method/MethodSpecifyingMethod");
    }
    @Test
    public void testMthMethodSpecifyingGetter(){
        compareWithJavaSource("method/MethodSpecifyingGetter");
    }
    @Test
    public void testMthMethodSpecifyingInitParam(){
        compareWithJavaSource("method/MethodSpecifyingInitParam");
    }
    @Test
    public void testMthMethodDefaultedParamCaptureInitParam(){
        compareWithJavaSource("method/MethodDefaultedParamCaptureInitParam");
    }
    @Test
    public void testMthRefinedMethodSpecifyingTopLevel(){
        compareWithJavaSource("method/RefinedMethodSpecifyingTopLevel");
    }
    @Test
    public void testMthLocalMethodSpecifyingMethod(){
        compareWithJavaSource("method/LocalMethodSpecifyingMethod");
    }
    @Test
    public void testMthLocalMethodSpecifyingParam(){
        compareWithJavaSource("method/LocalMethodSpecifyingParam");
    }
    @Test
    public void testMthVarargsMethodSpecifyingMethodWithIterable(){
        compareWithJavaSource("method/VarargsMethodSpecifyingMethodWithIterable");
    }
    @Test
    public void testMthVarargsMethodSpecifyingMethodWithVarargs(){
        compareWithJavaSource("method/VarargsMethodSpecifyingMethodWithVarargs");
    }
    @Test
    public void testMthIterableMethodSpecifyingMethodWithVarargs(){
        compareWithJavaSource("method/IterableMethodSpecifyingMethodWithVarargs");
    }
    @Test
    public void testTwoParamLists(){
        compareWithJavaSource("method/TwoParamLists");
    }
    @Test
    public void testTwoParamListsDefaulted(){
        compareWithJavaSource("method/TwoParamListsDefaulted");
    }
    @Test
    public void testThreeParamLists(){
        compareWithJavaSource("method/ThreeParamLists");
    }
    @Test
    public void testTwoParamListsVoid(){
        compareWithJavaSource("method/TwoParamListsVoid");
    }
    @Test
    public void testTwoParamListsTP(){
        compareWithJavaSource("method/TwoParamListsTP");
    }
    @Test
    public void testCallableEscaping(){
        compareWithJavaSource("method/CallableEscaping");
    }
    @Test
    public void testMethodInitializerParameter() {
        compareWithJavaSource("method/MethodInitializerParameter");
    }
    @Test
    public void testMethodRefinementAndVarianceEdgeCases() {
        compareWithJavaSource("method/MethodRefinementAndVarianceEdgeCases");
    }
    @Test
    public void testMethodHiding() {
        compareWithJavaSource("method/MethodHiding");
    }

    //
    // Aliases

    @Test
    public void testAlsClassAlias() {
        compareWithJavaSource("alias/ClassAlias");
    }

    @Test
    public void testAlsTypeAlias() {
        compareWithJavaSource("alias/TypeAlias");
    }

    @Test
    public void testAlsInterfaceAlias() {
        compareWithJavaSource("alias/InterfaceAlias");
    }

    @Test
    public void testAlsLocalClassAlias() {
        compareWithJavaSource("alias/LocalClassAlias");
    }

    @Test
    public void testAlsLocalTypeAlias() {
        compareWithJavaSource("alias/LocalTypeAlias");
    }

    @Test
    public void testAlsLocalInterfaceAlias() {
        compareWithJavaSource("alias/LocalInterfaceAlias");
    }

    @Test
    public void testAlsMemberClassAlias() {
        compareWithJavaSource("alias/MemberClassAlias");
    }

    @Test
    public void testAlsMemberTypeAlias() {
        compareWithJavaSource("alias/MemberTypeAlias");
    }

    @Test
    public void testAlsClassAliasWithParameters() {
        compareWithJavaSource("alias/ClassAliasWithParameters");
    }

    @Test
    public void testAlsMemberClassAliasWithParameters() {
        compareWithJavaSource("alias/MemberClassAliasWithParameters");
    }

    @Test
    public void testAlsClassAliasWithTypeParameters() {
        compareWithJavaSource("alias/ClassAliasWithTypeParameters");
    }

    @Test
    public void testAlsTypeAliasWithTypeParameters() {
        compareWithJavaSource("alias/TypeAliasWithTypeParameters");
    }

    @Test
    public void testAlsMemberClassAliasWithTypeParameters() {
        compareWithJavaSource("alias/MemberClassAliasWithTypeParameters");
    }

    @Test
    public void testAlsInterfaceAliasWithTypeParameters() {
        compareWithJavaSource("alias/InterfaceAliasWithTypeParameters");
    }

    @Test
    public void testAlsClassAliasFromModelLoader() {
        compile("alias/ClassAlias.ceylon", 
                "alias/ClassAliasWithParameters.ceylon",
                "alias/ClassAliasWithTypeParameters.ceylon");
        compareWithJavaSource("alias/ClassAliasFromModelLoader");
    }

    @Test
    public void testAlsTypeAliasFromModelLoader() {
        compile("alias/TypeAlias.ceylon", 
                "alias/TypeAliasWithTypeParameters.ceylon",
                "alias/MemberTypeAlias.ceylon");
        compareWithJavaSource("alias/TypeAliasFromModelLoader");
    }

    @Test
    public void testAlsMemberClassAliasFromModelLoader() {
        compile("alias/MemberClassAlias.ceylon", 
                "alias/MemberClassAliasWithParameters.ceylon",
                "alias/MemberClassAliasWithTypeParameters.ceylon");
        compareWithJavaSource("alias/MemberClassAliasFromModelLoader");
    }

    @Test
    public void testAlsInterfaceAliasFromModelLoader() {
        compile("alias/InterfaceAlias.ceylon", 
                "alias/InterfaceAliasWithTypeParameters.ceylon");
        compareWithJavaSource("alias/InterfaceAliasFromModelLoader");
    }

    @Test
    public void testAlsMemberClassAliasTricks() {
        compareWithJavaSource("alias/MemberClassAliasTricks");
    }

    @Test
    public void testAlsMemberInterfaceAliasTricks() {
        compareWithJavaSource("alias/MemberInterfaceAliasTricks");
    }
    
    @Test
    public void testAlsMemberOverriding(){
        compareWithJavaSource("alias/MemberOverriding");
    }

    //
    // Reified generics
    
    @Test
    public void testRfdClass(){
        compareWithJavaSource("reified/Class");
    }

    @Test
    public void testRfdRefinedVarianceInheritance(){
        compareWithJavaSource("reified/RVI");
    }

    @Test
    public void testRfdMethod(){
        compareWithJavaSource("reified/Method");
    }

    @Test
    public void testRfdComposite(){
        compareWithJavaSource("reified/Composite");
    }

    @Test
    public void testRfdCallable(){
        compareWithJavaSource("reified/Callable");
    }

    @Test
    public void testRfdTest(){
        compareWithJavaSource("reified/Test");
    }

    @Test
    public void testRfdRuntime(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.structure.reified.runtime", "reified/Runtime.ceylon");
    }

    @Test
    public void testRfdUtilCalls(){
        compareWithJavaSource("reified/UtilCalls");
    }

    @Test
    public void testRfdAliases(){
        compareWithJavaSource("reified/Aliases");
    }

    @Test
    public void testRfdMembers(){
        compareWithJavaSource("reified/Members");
    }

    @Test
    public void testRfdInterop(){
        compile("reified/JavaClass.java", "reified/JavaInterface.java");
        compareWithJavaSource("reified/Interop");
    }

    @Test
    public void testRfdModelLoader(){
        compile("reified/Class.ceylon", "reified/Method.ceylon");
        compareWithJavaSource("reified/ModelLoader");
    }
}
