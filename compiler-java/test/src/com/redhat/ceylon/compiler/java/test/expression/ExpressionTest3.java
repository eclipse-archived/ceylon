package com.redhat.ceylon.compiler.java.test.expression;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ExpressionTest3 extends CompilerTest {

    @Override
    protected String transformDestDir(String name) {
        return name + "-3";
    }
    
    @Test
    public void testComprehensions1() {
        compareWithJavaSource("comprehensions/comp1");
    }
    
    @Test
    public void testComprehensions2() {
        compareWithJavaSource("comprehensions/comp2");
    }

    @Test
    public void testComprehensionForForIf() {
        compareWithJavaSource("comprehensions/for_for_if");
    }
    
    @Test
    public void testComprehensionForForIfBooleanConditions() {
        compareWithJavaSource("comprehensions/ForForIfBooleanConditions");
    }
    
    @Test
    public void testComprehensionForIfIsBooleanConditions() {
        compareWithJavaSource("comprehensions/ForIfIsBooleanConditions");
    }

    @Test
    public void testComprehensionForIfFor() {
        compareWithJavaSource("comprehensions/for_if_for");
    }

    @Test
    public void testComprehensionForIfIf() {
        compareWithJavaSource("comprehensions/for_if_if");
    }

    @Test
    public void testComprehensionIsCond() {
        compareWithJavaSource("comprehensions/is_cond");
    }
    
    @Test
    public void testComprehensionIs() {
        compareWithJavaSource("comprehensions/Is");
    }

    @Test
    public void testComprehensionNonempty() {
        compareWithJavaSource("comprehensions/nonempty_cond");
    }
    
    @Test
    public void testComprehensionForIfExists(){
        compareWithJavaSource("comprehensions/ForIfExists");
    }
    
    @Test
    public void testComprehensionForIfExistsSequence(){
        compareWithJavaSource("comprehensions/ForIfExistsSequence");
    }
    
    @Test
    public void testComprehensionForIfExistsWithMethod(){
        compareWithJavaSource("comprehensions/ForIfExistsWithMethod");
    }
    
    @Test
    public void testComprehensionForIfIsFoo(){
        compile("comprehensions/FooBar.ceylon");
        compareWithJavaSource("comprehensions/ForIfIsFoo");
    }
    
    @Test
    public void testComprehensionForIfIsNotNothing(){
        compareWithJavaSource("comprehensions/ForIfIsNotNothing");
    }
    
    @Test
    public void testComprehensionForIfIsNotObject(){
        compareWithJavaSource("comprehensions/ForIfIsNotObject");
    }
    
    @Test
    public void testComprehensionForIfIsNullUnion(){
        compareWithJavaSource("comprehensions/ForIfIsNullUnion");
    }
    
    @Test
    public void testComprehensionForIfIsWithIntersection(){
        compile("comprehensions/FooBar.ceylon");
        compareWithJavaSource("comprehensions/ForIfIsWithIntersection");
    }
    
    @Test
    public void testComprehensionForIfIsWithMethod(){
        compile("comprehensions/FooBar.ceylon");
        compareWithJavaSource("comprehensions/ForIfIsWithMethod");
    }

    @Test
    public void testComprehensionForIfIsWithUnion(){
        compile("comprehensions/FooBar.ceylon");
        compareWithJavaSource("comprehensions/ForIfIsWithUnion");
    }
    
    @Test
    public void testComprehensionForIfIsNothing2(){
        compareWithJavaSource("comprehensions/ForIfIsNothing2");
    }
    
    @Test
    public void testComprehensionForIfIsNothing(){
        compareWithJavaSource("comprehensions/ForIfIsNothing");
    }

    @Test
    public void testComprehensionForIfNonEmptySequence(){
        compareWithJavaSource("comprehensions/ForIfNonEmptySequence");
    }
    
    @Test
    public void testComprehensionForIfNonEmptyString(){
        compareWithJavaSource("comprehensions/ForIfNonEmptyString");
    }
    
    @Test
    public void testComprehensionForIfConditionListBoolBool(){
        compareWithJavaSource("comprehensions/ForIfConditionListBoolBool");
    }
    @Test
    public void testComprehensionForIfConditionListIsIs(){
        compareWithJavaSource("comprehensions/ForIfConditionListIsIs");
    }
    @Test
    public void testComprehensionForIfConditionListBoolBoolIs(){
        compareWithJavaSource("comprehensions/ForIfConditionListBoolBoolIs");
    }
    @Test
    public void testComprehensionForIfConditionListBoolIsBool(){
        compareWithJavaSource("comprehensions/ForIfConditionListBoolIsBool");
    }
    @Test
    public void testComprehensionForIfConditionListExistsIsBool(){
        compareWithJavaSource("comprehensions/ForIfConditionListExistsIsBool");
    }
    @Test
    public void testComprehensionForIfConditionListIsBool(){
        compareWithJavaSource("comprehensions/ForIfConditionListIsBool");
    }
    @Test
    public void testComprehensionForIfConditionListIsBoolBool(){
        compareWithJavaSource("comprehensions/ForIfConditionListIsBoolBool");
    }
    @Test
    public void testComprehensionForIfConditionListNonemptyIsBool(){
        compareWithJavaSource("comprehensions/ForIfConditionListNonemptyIsBool");
    }


    @Test
    public void testComprehensionExists() {
        compareWithJavaSource("comprehensions/exists_cond");
    }

}
