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
package com.redhat.ceylon.compiler.java.test.expression;

import org.junit.Ignore;
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
    public void testComprehensionForIfIsNotNull(){
        compareWithJavaSource("comprehensions/ForIfIsNotNull");
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
    public void testComprehensionForIfIsNull2(){
        compareWithJavaSource("comprehensions/ForIfIsNull2");
    }
    
    @Test
    public void testComprehensionForIfIsNull(){
        compareWithJavaSource("comprehensions/ForIfIsNull");
    }

    @Test
    public void testComprehensionForIfNonEmptySequence(){
        compareWithJavaSource("comprehensions/ForIfNonEmptySequence");
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

    @Test
    public void testComprehensionEmptiness() {
        compareWithJavaSource("comprehensions/Emptiness");
    }
    
    @Test
    public void testRefAttributeRef() {
        compareWithJavaSource("ref/AttributeRef");
        //compile("ref/AttributeRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.attributeRef");
    }
    
    @Test
    public void testRefValueParameterRef() {
        compareWithJavaSource("ref/ValueParameterRef");
        //compile("ref/ValueParameterRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.valueParameterRef");
    }
    
    @Test
    public void testRefMethodRef() {
        compareWithJavaSource("ref/MethodRef");
        //compile("ref/MethodRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.methodRef");
    }
    
    @Test
    public void testRefFunctionalParameterRef() {
        compareWithJavaSource("ref/FunctionalParameterRef");
        //compile("ref/FunctionalParameterRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.functionalParameterRef");
    }
    
    @Test
    public void testRefMemberClassRef() {
        compareWithJavaSource("ref/MemberClassRef");
        //compile("ref/MemberClassRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.memberClassRef");
    }
    
    @Test
    public void testRefMemberClassRefInFunction() {
        compareWithJavaSource("ref/MemberClassRefInFunction");
        //compile("ref/MemberClassRef.ceylon");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.memberClassRefInFunction");
    }
    
    @Test
    public void testRefFunrefs() {
        compareWithJavaSource("ref/funrefs");
        run("com.redhat.ceylon.compiler.java.test.expression.ref.funrefs");
        
    }

}
