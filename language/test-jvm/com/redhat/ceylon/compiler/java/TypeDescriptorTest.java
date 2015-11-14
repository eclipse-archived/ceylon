package com.redhat.ceylon.compiler.java;

import org.junit.Assert;
import org.junit.Test;

import ceylon.language.Empty;
import ceylon.language.Float;
import ceylon.language.Integer;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.Tuple;

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
    
    @Test
    public void testTupleTypeString(){
        TypeDescriptor tuple2 = TypeDescriptor.tuple(false, false, -1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals("[ceylon.language.Integer,ceylon.language.String]", tuple2.toString());

        TypeDescriptor tuple2Star = TypeDescriptor.tuple(true, false, -1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals("[ceylon.language.Integer,ceylon.language.String*]", tuple2Star.toString());

        TypeDescriptor tuple2Plus = TypeDescriptor.tuple(true, true, -1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals("[ceylon.language.Integer,ceylon.language.String+]", tuple2Plus.toString());

        TypeDescriptor tuple1Or2 = TypeDescriptor.tuple(false, false, 1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals("[ceylon.language.Integer,ceylon.language.String=]", tuple1Or2.toString());

        TypeDescriptor tuple0OrN = TypeDescriptor.tuple(true, false, 0, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals("[ceylon.language.Integer=,ceylon.language.String*]", tuple0OrN.toString());
    }

    @Test
    public void testTupleTypeUnwrap(){
        TypeDescriptor tuple1Unwrapped = TypeDescriptor.klass(Tuple.class, Integer.$TypeDescriptor$, Integer.$TypeDescriptor$, Empty.$TypeDescriptor$);
        TypeDescriptor tuple1 = TypeDescriptor.tuple(false, false, -1, Integer.$TypeDescriptor$);
        Assert.assertEquals(tuple1Unwrapped, tuple1);
        Assert.assertEquals(tuple1, tuple1Unwrapped);

        TypeDescriptor tuple2Unwrapped = TypeDescriptor.klass(Tuple.class, 
                TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                Integer.$TypeDescriptor$, 
                TypeDescriptor.klass(Tuple.class,
                                     String.$TypeDescriptor$,
                                     String.$TypeDescriptor$,
                                     Empty.$TypeDescriptor$));
        TypeDescriptor tuple2 = TypeDescriptor.tuple(false, false, -1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals(tuple2Unwrapped, tuple2);
        Assert.assertEquals(tuple2, tuple2Unwrapped);

        TypeDescriptor tuple2PlusUnwrapped = TypeDescriptor.klass(Tuple.class, 
                TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                Integer.$TypeDescriptor$, 
                TypeDescriptor.klass(Sequence.class,
                                     String.$TypeDescriptor$));
        TypeDescriptor tuple2Plus = TypeDescriptor.tuple(true, true, -1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals(tuple2PlusUnwrapped, tuple2Plus);
        Assert.assertEquals(tuple2Plus, tuple2PlusUnwrapped);

        TypeDescriptor tuple2StarUnwrapped = TypeDescriptor.klass(Tuple.class, 
                TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                Integer.$TypeDescriptor$, 
                TypeDescriptor.klass(Sequential.class,
                                     String.$TypeDescriptor$));
        TypeDescriptor tuple2Star = TypeDescriptor.tuple(true, false, -1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals(tuple2StarUnwrapped, tuple2Star);
        Assert.assertEquals(tuple2Star, tuple2StarUnwrapped);

        TypeDescriptor tuple1Or2Unwrapped = TypeDescriptor.klass(Tuple.class, 
                TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                Integer.$TypeDescriptor$,
                TypeDescriptor.union(Empty.$TypeDescriptor$,
                                     TypeDescriptor.klass(Tuple.class,
                                                          String.$TypeDescriptor$,
                                                          String.$TypeDescriptor$,
                                                          Empty.$TypeDescriptor$)));
        TypeDescriptor tuple1Or2 = TypeDescriptor.tuple(false, false, 1, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals(tuple1Or2Unwrapped, tuple1Or2);
        Assert.assertEquals(tuple1Or2, tuple1Or2Unwrapped);

        TypeDescriptor tuple0Or1Or2Unwrapped = TypeDescriptor.union(Empty.$TypeDescriptor$,
                TypeDescriptor.klass(Tuple.class, 
                                     TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$), 
                                     Integer.$TypeDescriptor$,
                                     TypeDescriptor.union(Empty.$TypeDescriptor$,
                                                          TypeDescriptor.klass(Tuple.class,
                                                                               String.$TypeDescriptor$,
                                                                               String.$TypeDescriptor$,
                                                                               Empty.$TypeDescriptor$))));
        TypeDescriptor tuple0Or1Or2 = TypeDescriptor.tuple(false, false, 0, Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        Assert.assertEquals(tuple0Or1Or2Unwrapped, tuple0Or1Or2);
        Assert.assertEquals(tuple0Or1Or2, tuple0Or1Or2Unwrapped);
    }
    
    @Test
    public void testPartialTuples(){
        TypeDescriptor zeroOrOneTuple = TypeDescriptor.tupleWithRest(Empty.$TypeDescriptor$, TypeDescriptor.NothingType, 0, Integer.$TypeDescriptor$);
        TypeDescriptor zeroOrOneTupleFinal = TypeDescriptor.tuple(false, false, 0, Integer.$TypeDescriptor$);
        Assert.assertEquals(zeroOrOneTuple, zeroOrOneTupleFinal);

        TypeDescriptor oneTuple = TypeDescriptor.tupleWithRest(Empty.$TypeDescriptor$, TypeDescriptor.NothingType, -1, Integer.$TypeDescriptor$);
        TypeDescriptor oneTupleFinal = TypeDescriptor.tuple(false, false, -1, Integer.$TypeDescriptor$);
        Assert.assertEquals(oneTuple, oneTupleFinal);

        TypeDescriptor twoTuple = TypeDescriptor.tupleWithRest(oneTuple, Integer.$TypeDescriptor$, -1, Integer.$TypeDescriptor$);
        TypeDescriptor twoTupleFinal = TypeDescriptor.tuple(false, false, -1, Integer.$TypeDescriptor$, Integer.$TypeDescriptor$);
        Assert.assertEquals(twoTuple, twoTupleFinal);

        TypeDescriptor oneOrTwoTuple = TypeDescriptor.tupleWithRest(zeroOrOneTuple, Integer.$TypeDescriptor$, -1, Integer.$TypeDescriptor$);
        TypeDescriptor oneOrTwoTupleFinal = TypeDescriptor.tuple(false, false, 1, Integer.$TypeDescriptor$, Integer.$TypeDescriptor$);
        Assert.assertEquals(oneOrTwoTuple, oneOrTwoTupleFinal);
        
        TypeDescriptor zeroOrN = TypeDescriptor.klass(Sequential.class, Integer.$TypeDescriptor$);
        TypeDescriptor oneOrN = TypeDescriptor.klass(Sequence.class, Integer.$TypeDescriptor$);
        
        TypeDescriptor oneOrNTuple = TypeDescriptor.tupleWithRest(zeroOrN, Integer.$TypeDescriptor$, -1, Integer.$TypeDescriptor$);
        TypeDescriptor oneOrNTupleFinal = TypeDescriptor.tuple(true, false, -1, Integer.$TypeDescriptor$, Integer.$TypeDescriptor$);
        Assert.assertEquals(oneOrNTuple, oneOrNTupleFinal);

        TypeDescriptor twoOrNTuple = TypeDescriptor.tupleWithRest(oneOrN, Integer.$TypeDescriptor$, -1, Integer.$TypeDescriptor$);
        TypeDescriptor twoOrNTupleFinal = TypeDescriptor.tuple(true, true, -1, Integer.$TypeDescriptor$, Integer.$TypeDescriptor$);
        Assert.assertEquals(twoOrNTuple, twoOrNTupleFinal);
    }
    
    @Test
    public void testDegenerateTuples(){
        TypeDescriptor intOrString = TypeDescriptor.union(Integer.$TypeDescriptor$, String.$TypeDescriptor$);
        TypeDescriptor intTuple = TypeDescriptor.tuple(false, false, -1, Integer.$TypeDescriptor$);
        TypeDescriptor stringTuple = TypeDescriptor.tuple(false, false, -1, String.$TypeDescriptor$);
        
        // [Integer, *<[Integer]|[String]>]
        TypeDescriptor intThenIntOrStringTuple = TypeDescriptor.klass(Tuple.class, intOrString, Integer.$TypeDescriptor$, TypeDescriptor.union(intTuple, stringTuple));
        // make sure it's not a TypeDescriptor.Tuple
        Assert.assertEquals("ceylon.language.Tuple<ceylon.language.Integer|ceylon.language.String,ceylon.language.Integer,[ceylon.language.Integer]|[ceylon.language.String]>", 
                intThenIntOrStringTuple.toString());

        // [Integer, *<[Integer]|[String]>]
        TypeDescriptor intThenIntOrStringTuple2 = TypeDescriptor.tupleWithRest(TypeDescriptor.union(intTuple, stringTuple), intOrString, -1, Integer.$TypeDescriptor$);
        // make sure it's not a TypeDescriptor.Tuple
        Assert.assertTrue(intThenIntOrStringTuple2 instanceof TypeDescriptor.Class);
        Assert.assertEquals(intThenIntOrStringTuple, intThenIntOrStringTuple2);
}
}
