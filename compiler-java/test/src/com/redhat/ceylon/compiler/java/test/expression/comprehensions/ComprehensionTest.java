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
package com.redhat.ceylon.compiler.java.test.expression.comprehensions;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class ComprehensionTest extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.expression.comprehensions", "1.0");
    }
    
    @Test
    public void testComprehensions1() {
        compareWithJavaSource("comp1");
    }
    
    @Test
    public void testComprehensions2() {
        compareWithJavaSource("comp2");
    }

    @Test
    public void testComprehensionForForIf() {
        compareWithJavaSource("for_for_if");
    }
    
    @Test
    public void testComprehensionForForIfBooleanConditions() {
        compareWithJavaSource("ForForIfBooleanConditions");
    }
    
    @Test
    public void testComprehensionForIfIsBooleanConditions() {
        compareWithJavaSource("ForIfIsBooleanConditions");
    }

    @Test
    public void testComprehensionForIfFor() {
        compareWithJavaSource("for_if_for");
    }

    @Test
    public void testComprehensionForIfIf() {
        compareWithJavaSource("for_if_if");
    }

    @Test
    public void testComprehensionIsCond() {
        compareWithJavaSource("is_cond");
    }
    
    @Test
    public void testComprehensionIs() {
        compareWithJavaSource("Is");
    }

    @Test
    public void testComprehensionNonempty() {
        compareWithJavaSource("nonempty_cond");
    }
    
    @Test
    public void testComprehensionForIfExists(){
        compareWithJavaSource("ForIfExists");
    }
    
    @Test
    public void testComprehensionForIfExistsSequence(){
        compareWithJavaSource("ForIfExistsSequence");
    }
    
    @Test
    public void testComprehensionForIfExistsWithMethod(){
        compareWithJavaSource("ForIfExistsWithMethod");
    }
    
    @Test
    public void testComprehensionForIfIsFoo(){
        compile("FooBar.ceylon");
        compareWithJavaSource("ForIfIsFoo");
    }
    
    @Test
    public void testComprehensionForIfIsNotNull(){
        compareWithJavaSource("ForIfIsNotNull");
    }
    
    @Test
    public void testComprehensionForIfIsNotObject(){
        compareWithJavaSource("ForIfIsNotObject");
    }
    
    @Test
    public void testComprehensionForIfIsNullUnion(){
        compareWithJavaSource("ForIfIsNullUnion");
    }
    
    @Test
    public void testComprehensionForIfIsWithIntersection(){
        compile("FooBar.ceylon");
        compareWithJavaSource("ForIfIsWithIntersection");
    }
    
    @Test
    public void testComprehensionForIfIsWithMethod(){
        compile("FooBar.ceylon");
        compareWithJavaSource("ForIfIsWithMethod");
    }

    @Test
    public void testComprehensionForIfIsWithUnion(){
        compile("FooBar.ceylon");
        compareWithJavaSource("ForIfIsWithUnion");
    }
    
    @Test
    public void testComprehensionForIfIsNull2(){
        compareWithJavaSource("ForIfIsNull2");
    }
    
    @Test
    public void testComprehensionForIfIsNull(){
        compareWithJavaSource("ForIfIsNull");
    }

    @Test
    public void testComprehensionForIfNonEmptySequence(){
        compareWithJavaSource("ForIfNonEmptySequence");
    }
    
    @Test
    public void testComprehensionForIfConditionListBoolBool(){
        compareWithJavaSource("ForIfConditionListBoolBool");
    }
    @Test
    public void testComprehensionForIfConditionListIsIs(){
        compareWithJavaSource("ForIfConditionListIsIs");
    }
    @Test
    public void testComprehensionForIfConditionListBoolBoolIs(){
        compareWithJavaSource("ForIfConditionListBoolBoolIs");
    }
    @Test
    public void testComprehensionForIfConditionListBoolIsBool(){
        compareWithJavaSource("ForIfConditionListBoolIsBool");
    }
    @Test
    public void testComprehensionForIfConditionListExistsIsBool(){
        compareWithJavaSource("ForIfConditionListExistsIsBool");
    }
    @Test
    public void testComprehensionForIfConditionListIsBool(){
        compareWithJavaSource("ForIfConditionListIsBool");
    }
    @Test
    public void testComprehensionForIfConditionListIsBoolBool(){
        compareWithJavaSource("ForIfConditionListIsBoolBool");
    }
    @Test
    public void testComprehensionForIfConditionListNonemptyIsBool(){
        compareWithJavaSource("ForIfConditionListNonemptyIsBool");
    }


    @Test
    public void testComprehensionExists() {
        compareWithJavaSource("exists_cond");
    }

    @Test
    public void testComprehensionEmptiness() {
        compareWithJavaSource("Emptiness");
    }
    
    @Test
    public void testIfExists() {
    	compareWithJavaSource("if_exists");
    }
    
    @Test
    @Ignore("#1337")
    public void testOptimizedTupleEnumWithComprehension() throws ReflectiveOperationException {
        compile("OptimizedTupleEnumWithComprehension.ceylon");
        Object seq = run("com.redhat.ceylon.compiler.java.test.expression.comprehensions.optimizedTupleEnumWithComprehensionOverTuple");
        assertArraySequenceWithArraySize(seq, 10);
        seq = run("com.redhat.ceylon.compiler.java.test.expression.comprehensions.optimizedTupleEnumWithComprehensionOverArraySequence");
        assertArraySequenceWithArraySize(seq, 10);
        seq = run("com.redhat.ceylon.compiler.java.test.expression.comprehensions.optimizedTupleEnumWithComprehensionOverRange");
        assertArraySequenceWithArraySize(seq, 10);
        seq = run("com.redhat.ceylon.compiler.java.test.expression.comprehensions.optimizedTupleEnumWithComprehensionOverString");
        assertArraySequenceWithArraySize(seq, 10);
        seq = run("com.redhat.ceylon.compiler.java.test.expression.comprehensions.optimizedTupleEnumWithComprehensionOverArray");
        assertArraySequenceWithArraySize(seq, 10);
    }

    private void assertArraySequenceWithArraySize(Object seq, int size)
            throws IllegalAccessException {
        Assert.assertEquals("ceylon.language.ArraySequence", seq.getClass().getName());
        Field length = null;
        for (Field field : seq.getClass().getDeclaredFields()) {
            if ("array".equals(field.getName())) {
                length = field;
                length.setAccessible(true);
                break;
            }
        }
        Assert.assertNotNull(length);
        Object[] array = (Object[])length.get(seq);
        Assert.assertEquals(size, array.length);
    }
    
    @Test
    public void testComprehensionMemberQual() {
        compareWithJavaSource("ComprehensionMemberQual");
    }

}
