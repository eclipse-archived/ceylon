package com.redhat.ceylon.compiler.java.test.structure;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTests;

public class ConstructorTests extends CompilerTests {
    @Override
    protected String transformDestDir(String name) {
        return name + "-4";
    }
    // Tests for constructors
    @Test
    public void testCtorNullaryCtor(){
        compareWithJavaSource("constructor/NullaryCtor");
        compareWithJavaSource("constructor/NullaryCtorUse");
    }
    @Test
    public void testCtorUnaryCtor(){
        compareWithJavaSource("constructor/UnaryCtor");
        compareWithJavaSource("constructor/UnaryCtorUse");
    }
    @Test
    public void testCtorDefaultedParameterCtor(){
        compareWithJavaSource("constructor/DefaultedParameterCtor");
    }
    @Test
    public void testCtorSequencedParameterCtor(){
        compareWithJavaSource("constructor/SequencedParameterCtor");
    }
    @Test
    public void testCtorCaptureInit(){
        compareWithJavaSource("constructor/CtorCaptureInit");
    }
    @Test
    public void testCtorGenericClass(){
        compareWithJavaSource("constructor/CtorGenericClass");
    }
    @Test
    public void testCtorChaining(){
        compareWithJavaSource("constructor/CtorChaining");
    }
    
    @Test
    public void testCtorClassMemberClassCtor(){
        compareWithJavaSource("constructor/ClassMemberClassCtor");
    }
    
    @Test
    public void testCtorClassMemberCtorChaining() {
        compareWithJavaSource("constructor/ClassMemberCtorChaining");
    }
    
    @Test
    public void testCtorClassMemberCtorAlias() {
        compareWithJavaSource("constructor/ClassMemberCtorAlias");
    }
    
    @Test
    public void testCtorInterfaceMemberClassCtor(){
        // This need default parameters to be a good test
        compareWithJavaSource("constructor/InterfaceMemberClassCtor");
    }
    
    @Test
    public void testCtorInterfaceMemberCtorChaining() {
        compareWithJavaSource("constructor/InterfaceMemberCtorChaining");
    }
    
    /*@Test
    public void testCtorClassRefinedMemberClassCtor(){
        compareWithJavaSource("constructor/ClassRefinedMemberClassCtor");
    }
    @Test
    public void testCtorInterfaceRefinedMemberClassCtor(){
        compareWithJavaSource("constructor/InterfaceRefinedMemberClassCtor");
    }
    */
    
    @Test
    public void testCtorLocalClassCtor(){
        compareWithJavaSource("constructor/LocalClassCtor");
    }
    
    @Test
    public void testCtorClassAliasCtor(){
        compareWithJavaSource("constructor/ClassAliasCtor");
        compareWithJavaSource("constructor/ClassAliasCtorUse");
    }
    
    @Test
    public void testCtorShadowing(){
        compareWithJavaSource("constructor/Shadowing");
    }
    
    @Test
    public void testCtorLocalConstructors(){
        compareWithJavaSource("constructor/LocalConstructors");
    }
    
    @Test
    public void testCtorBug1961(){
        compareWithJavaSource("constructor/Bug1961");
    }
    
    @Test
    public void testCtorBug1981(){
        compareWithJavaSource("constructor/Bug1981");
    }
    
    @Test
    public void testCtorContainingClassDecl(){
        compareWithJavaSource("constructor/CtorContainingClassDecl");
    }
    
    @Test
    public void testCtorContainingObjectDecl(){
        compareWithJavaSource("constructor/CtorContainingObjectDecl");
    }
    
    @Test
    public void testCtorObjectDeclContainingClassWithCtor(){
        compareWithJavaSource("constructor/ObjectDeclContainingClassWithCtor");
    }
    
    @Test
    public void testCtorAliasBoxing(){
        compareWithJavaSource("constructor/CtorAliasBoxing");
    }
    
    @Test
    public void testCtorMemberClassOfNestedAnonymousClass(){
        compareWithJavaSource("constructor/CtorMemberClassOfNestedAnonymousClass");
    }
    
    @Test
    public void testCtorInitializingMethod() {
        compareWithJavaSource("constructor/CtorInitializingMethod");
    }
    
    @Test
    public void testCtorIntermixedStatementsAndConstructors() {
        compareWithJavaSource("constructor/CtorIntermixedStatementsAndConstructors");
    }
    
    @Test
    public void testCtorAbstractCtor() {
        compile("constructor/checker.ceylon");
        compareWithJavaSource("constructor/CtorAbstractCtor");
        //compile("constructor/CtorAbstractCtor.ceylon");
        run("com.redhat.ceylon.compiler.java.test.structure.constructor.runCtorAbstractCtor");
    }
    
    @Test
    public void testCtorConcreteDelegation() {
        compile("constructor/checker.ceylon");
        compareWithJavaSource("constructor/CtorConcreteDelegation");
        //compile("constructor/CtorConcreteDelegation.ceylon");
        run("com.redhat.ceylon.compiler.java.test.structure.constructor.runCtorConcreteDelegation");
    }
    
    @Test
    public void testCtorDelegationMemberClass() {
        compile("constructor/checker.ceylon");
        compareWithJavaSource("constructor/CtorDelegationMemberClass");
        //compile("constructor/CtorDelegationMemberClass.ceylon");
        run("com.redhat.ceylon.compiler.java.test.structure.constructor.runCtorDelegationMemberClass");
    }
    
    @Test
    public void testCtorDelegationClassRefs() {
        compile("constructor/checker.ceylon");
        compareWithJavaSource("constructor/CtorDelegationClassRefs");
        //compile("constructor/CtorDelegationClassRefs.ceylon");
        run("com.redhat.ceylon.compiler.java.test.structure.constructor.runCtorDelegationClassRefs");
    }
    
    @Test
    public void testCtorDelegationExtendDefault() {
        compareWithJavaSource("constructor/CtorDelegationExtendDefault");
    }
    
    @Test
    public void testCtorReturn() {
        compareWithJavaSource("constructor/CtorReturn");
    }
    
    @Test
    public void testCtorSingletonCtors() {
        compareWithJavaSource("constructor/SingletonCtors");
        //compile("constructor/SingletonCtors.ceylon");
        run("com.redhat.ceylon.compiler.java.test.structure.constructor.singletonCtors");
    }
    
    @Test
    public void testCtorSingletonCtorDelegation() {
        compareWithJavaSource("constructor/SingletonCtorDelegation");
        //compile("constructor/SingletonCtors.ceylon");
        run("com.redhat.ceylon.compiler.java.test.structure.constructor.singletonCtorDelegation");
    }
    
    @Test
    public void testCtorOfObjectSubclass() {
        compareWithJavaSource("constructor/CtorOfObjectSubclass");
    }
    
    @Test
    public void testCtorWithUnsharedField() {
        compareWithJavaSource("constructor/CtorWithUnsharedField");
    }
    
    @Test
    @Ignore
    public void testCtorExtendingCtorWithDefaultParameter() {
        compareWithJavaSource("constructor/ExtendingCtorWithDefaultParameter");
    }
}
