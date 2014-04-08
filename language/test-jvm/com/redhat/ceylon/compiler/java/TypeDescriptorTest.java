package com.redhat.ceylon.compiler.java;

import org.junit.Assert;
import org.junit.Test;

import ceylon.language.Float;
import ceylon.language.Integer;
import ceylon.language.String;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public class TypeDescriptorTest {
    @Test
    public void testUnionDuplicates(){
        Assert.assertEquals(Integer.$TypeDescriptor$, TypeDescriptor.union(Integer.$TypeDescriptor$));
        Assert.assertEquals(Integer.$TypeDescriptor$, TypeDescriptor.union(Integer.$TypeDescriptor$, Integer.$TypeDescriptor$));
        Assert.assertEquals(TypeDescriptor.union(String.$TypeDescriptor$, Integer.$TypeDescriptor$), 
                            TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$, Integer.$TypeDescriptor$));
    }

    @Test
    public void testIntersectionDuplicates(){
        Assert.assertEquals(Integer.$TypeDescriptor$, TypeDescriptor.intersection(Integer.$TypeDescriptor$));
        Assert.assertEquals(Integer.$TypeDescriptor$, TypeDescriptor.intersection(Integer.$TypeDescriptor$, Integer.$TypeDescriptor$));
        Assert.assertEquals(TypeDescriptor.intersection(String.$TypeDescriptor$, Integer.$TypeDescriptor$), 
                            TypeDescriptor.intersection(Integer.$TypeDescriptor$, String.$TypeDescriptor$, Integer.$TypeDescriptor$));
    }

    @Test
    public void testUnionIntersectionCommutativity(){
        Assert.assertEquals(TypeDescriptor.union(String.$TypeDescriptor$, Integer.$TypeDescriptor$), 
                            TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$));
        Assert.assertEquals(TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                            TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$));
        Assert.assertEquals(TypeDescriptor.intersection(String.$TypeDescriptor$, Integer.$TypeDescriptor$), 
                            TypeDescriptor.intersection(Integer.$TypeDescriptor$, String.$TypeDescriptor$));
        Assert.assertEquals(TypeDescriptor.intersection(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                            TypeDescriptor.intersection(Integer.$TypeDescriptor$, String.$TypeDescriptor$));
    }

    @Test
    public void testUnionIntersectionEquality(){
        Assert.assertFalse(TypeDescriptor.union(String.$TypeDescriptor$, Integer.$TypeDescriptor$).equals(
                           TypeDescriptor.union(Float.$TypeDescriptor$, String.$TypeDescriptor$)));
        Assert.assertFalse(TypeDescriptor.union(String.$TypeDescriptor$, Integer.$TypeDescriptor$).equals(
                           TypeDescriptor.union(String.$TypeDescriptor$, Integer.$TypeDescriptor$, Float.$TypeDescriptor$)));
        Assert.assertFalse(TypeDescriptor.intersection(String.$TypeDescriptor$, Integer.$TypeDescriptor$).equals(
                           TypeDescriptor.intersection(Float.$TypeDescriptor$, String.$TypeDescriptor$)));
        Assert.assertFalse(TypeDescriptor.intersection(String.$TypeDescriptor$, Integer.$TypeDescriptor$).equals(
                           TypeDescriptor.intersection(String.$TypeDescriptor$, Integer.$TypeDescriptor$, Float.$TypeDescriptor$)));
    }
}
