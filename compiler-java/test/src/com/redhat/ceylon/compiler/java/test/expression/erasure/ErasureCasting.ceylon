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
@noanno
interface EC_A {
    shared formal Integer int();
}

@noanno
interface EC_B {}

@noanno
interface EC_C {}

@noanno
interface EC_ABC satisfies EC_A & EC_B & EC_C {}

@noanno
interface EC_Parameterised<out T>{
    shared formal T t();
}

@noanno
interface EC_ParameterisedSelfBound<out T> of T {
    shared formal T t();
}

@noanno
interface EC_DoubleParameters<T1,T2> satisfies EC_Parameterised<T1|T2>{}

@noanno
void erasureCasting(EC_A & EC_B & EC_C tripleIntersectionParam,
                    EC_Parameterised<EC_A & EC_B & EC_C> rawParam,
                    EC_Parameterised<EC_Parameterised<EC_A & EC_B>> hasRawParam,
                    EC_DoubleParameters<EC_A,EC_A> hasLostParameter,
                    Callable<EC_A&EC_B,[]> erasedReturnCallable,
                    Callable<EC_A,[EC_A&EC_B]> erasedParamCallable,
                    EC_Parameterised<Nothing> hasErasedParameter,
                    EC_A a,
                    Sequence<String> nonEmptyStringSequence,
                    Sequence<EC_A&EC_B>&EC_A erasedSequence){
    // from an erased type to another erased type: no cast
    EC_A & EC_B doubleIntersectionAttr = tripleIntersectionParam;
    
    // from an erased type to a non-erased type: cast
    EC_A aAttr = tripleIntersectionParam;
    
    // Null is erased to Object, so we need to cast it
    Null n = null;
    EC_A? aAttrOrNull = n;
    
    // never cast for null
    EC_A? aAttrOrNullValue = null;
    
    // inside a callable we have forcibly erased parameters that we want to cast even if
    // the typechecker would tell us the expression normally requires no casting
    Callable<Anything,[EC_A]> callable = (EC_A a) => a;
    // requires a cast
    Callable<EC_A,[]> ecACallable = erasedReturnCallable;
    // does not require casts since Callable Argument type param is suppressed
    Callable<EC_A,[EC_ABC]> ecABCCallable= erasedParamCallable;
    
    // requires no cast because erasure is the same
    EC_Parameterised<EC_A&EC_B> parameterisedRawAttr = rawParam;
    
    // requires a single cast, no raw cast required because it's already raw
    EC_Parameterised<EC_A> parameterisedAttr = rawParam;
    
    // requires a raw cast then normal cast
    EC_Parameterised<EC_Parameterised<EC_A>> parameterisedParameterisedAttr = hasRawParam;
    
    // requires a raw cast because nothing is assignable to anything
    EC_A aAttrFromNothing = nothing;
    
    // requires a cast because empty is Sequential<Nothing>
    Sequential<Integer> sequence = empty;

    // special case?
    Integer[] sequence2 = false then [] else 0..2;

    // those ones require casts because there's a bound on its type parameter
    // See https://github.com/ceylon/ceylon-compiler/issues/953
    ec_boundsOnElement(nothing);

    ec_boundsOnElement2(empty);
    
    ec_boundsOnElement3(hasErasedParameter);

    // make sure that callable goes raw when capturing methods, to be able to bypass complex erasure rules
    value callableToBoundsOnElement3 = ec_boundsOnElement3<Nothing>;

    // this one requires a cast too, because the inferred type for elements (Empty) is different
    // from the Java declaration that it is erased into a Sequantial<Integer> 
    ec_boundsOnElement4<Integer, Empty>(empty);

    // this one should have a cast to the EC_A bound
    ec_boundsOnElement5(tripleIntersectionParam);

    // This one lost a type parameter in its hierarchy, so we need to cast it
    // See https://github.com/ceylon/ceylon-compiler/issues/947
    EC_Parameterised<EC_A> lostParamAttr = hasLostParameter;

    // See https://github.com/ceylon/ceylon-compiler/issues/935
    // the method has an erased return type,
    // when applied with String, we end up with
    // Sequential<Sequential<String|String>> which maps to Sequential<Sequential<String>>
    // so we need a raw cast 
    value sequentialSequentialString = ec_methodWithErasedBounds("");

    // this one is the same
    value parameterisedParameterisedString = ec_methodWithErasedBounds2("");

    // this used to fail because of self-bounds
    // See https://github.com/ceylon/ceylon-compiler/issues/966
    if (is EC_ParameterisedSelfBound<Anything> a) {
        a.t();
    }
    // this works
    if (is EC_Parameterised<Anything> a) {
        a.t();
    }
    
    // this one does an unnecessary cast for now, plus a necessary forced downcast
    Empty|Sequence<Integer&EC_A> rawSequence = [];
    if(nonempty rawSequence){}
    
    // make sure we can assign nothing to primitives, by considering that nothing is a boxed Boolean
    Boolean bool = nothing;

    // should not require a cast
    if(exists nonOptionalString = ec_TOrOptionalString<String>()){}

    // should have no inserted cast for spread op
    EC_ParameterisedTOrString<String>[] sequenceOfErasedMethod = [];
    Sequential<String> sequenceOfStrings = sequenceOfErasedMethod*.t();
    
    // downcasting from "Sequential SequenceBuilder.getSequence()" to Sequence
    Sequence<Integer> nonEmptyIntegerSequence = nonEmptyStringSequence*.size;

    // make sure we can iterate erased sequences
    for(erasedValue in erasedSequence){}

    // spread on erased sequence
    Sequence<Integer> sequenceOfInts = erasedSequence*.int();

    // tuple as ranged
    [Integer,String,Singleton<Character>] t = [1, "2", Singleton('3')];
    value t2 = t[0..1];

    // make sure we don't consider nonRawTuple as raw invalidly
    value nonRawTuple = EC_Tuple<Integer|String,Integer>(1);
    value nonRawTupleFirst = nonRawTuple.first;

    // getFirst returns an erased type that we need to unbox
    // make sure we're calling the value type static decl, which must be marked as not raw
    value unboxedInteger = "abc".withLeading('d').first;

    // nonempty comprehension must be cast from Sequential to Sequence
    value nonEmptySequence = [ for (c in 1..2) c ];
    // this one should not require a cast, because we're really expecting a Sequential, which is what comprehension.sequence gives
    // us, but currently the problem is that we get the tuple expression of type Sequence, which we don't need to cast to the
    // expected type Sequential, and within the Tuple expression, we evaluate the comprehension using an expected type of Sequence
    // (because that's the type of the tuple literal), and we think we need that cast, even though we don't
    // we might be able to improve this logic somehow in the future.
    Integer[] integerSequential = [ for (c in 1..2) c ];
    
    // because accessing tuples with integer constant indices is equivalent to downcasting, we need to make sure casting is added
    [Sequential<[Integer,String]>,Sequential<String>] tupleOfSequentialSomething = nothing;
    String[] sequenceOfStrings2 = tupleOfSequentialSomething[1];
    value sequenceOfSequenceOfStrings = tupleOfSequentialSomething[1...];

    // make sure we don't pick up type parameter constraints on callable type parameters past the first
    Callable<Callable<Anything,[]>,[]> callableOfCallable = nothing;
    value callableResult = ec_callableWithBonds(callableOfCallable)();
    
    // make sure both of these calls go raw
    Null n1 = min(empty);
    Null n2 = min{};
}

@noanno
void valueOrNada<Value,Nada>(Value|Nada valueOrNada) 
        given Nada satisfies Null {
    // this looks like valueOrNada would be unboxed to Value, because isOptional may return true
    // but it is really not, it's erased to Object, so we must make sure it is properly marked
    // as such
    if (exists valueOrNada) {
    }
}

@noanno
interface EC_ParameterisedTOrString<T> {
    shared formal T|String t();
}

@noanno
T|String? ec_TOrOptionalString<T>() { return null; }


@noanno
void ec_boundsOnElement<Element>(Element elements) 
        given Element satisfies EC_Parameterised<Element> {
}

@noanno
void ec_boundsOnElement2<Element>(Sequential<Element> elements) 
        given Element satisfies EC_A {
}

@noanno
void ec_boundsOnElement3<Element>(EC_Parameterised<Element> elements) 
        given Element satisfies EC_A {
}

@noanno
void ec_boundsOnElement4<Element, Rest>(Rest&Element[] elements) 
        given Rest of Empty|Sequence<Element> {
}

@noanno
void ec_boundsOnElement5<Element>(Element t)
        given Element satisfies EC_A {
}

// Sequential<Sequential<Element|String>> erases to Sequential<Sequential<Object>>
// note that this bug is specific to Sequential, it does not happen with EC_Parameterised
@noanno
Sequential<Sequential<Element|String>> ec_methodWithErasedBounds<Element>(Element data){
    return nothing;
}

@noanno
EC_Parameterised<EC_Parameterised<Element|String>> ec_methodWithErasedBounds2<Element>(Element data){
    return nothing;
}

@noanno
interface EC_ErasedMember<Element, Absent>
    given Absent satisfies Null {
    // should be erased
    shared default Absent|Element attr => nothing;
    shared default Absent|Element m() => nothing;
    
    shared default void defaultedParams(Absent|Element p = nothing){}
    
    shared default class Class(Absent|Element p = nothing){}
}

@noanno
class EC_ErasedMemberImpl<Element>() satisfies EC_ErasedMember<Element, Null> {
    // make sure the generated bridges are appropriately casted
}

@noanno
class EC_Tuple<out Element, out First>(first)
    given First satisfies Element{
    shared First first;
}

@noanno
// See shuffle
Callable<Callable<Result,FirstArgs>,SecondArgs> ec_callableWithBonds<Result,FirstArgs,SecondArgs>(Callable<Callable<Result,SecondArgs>,FirstArgs> c)
        given FirstArgs satisfies Anything[]
        given SecondArgs satisfies Anything[]
{
    return nothing;
}

@noanno
class Bug1327(){
    Arguments foo<Arguments>(Arguments args)
            given Arguments satisfies String[] {
        return args;
    }
    void m(){
        Empty e1 = foo([]);
        Empty e2 = bug1327_foo([]);
        Empty e3 = Bug1327_2<Integer>().foo([]);
    }
}

@noanno
class Bug1327_2<Element>(){
    shared Arguments foo<Arguments>(Arguments args)
            given Arguments satisfies Element[] {
        return args;
    }
}

@noanno
shared Arguments bug1327_foo<Arguments>(Arguments args)
        given Arguments satisfies String[] {
    return args;
}
