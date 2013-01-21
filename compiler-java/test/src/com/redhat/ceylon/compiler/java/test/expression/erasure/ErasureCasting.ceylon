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
@nomodel
interface EC_A {
    shared formal Integer int();
}

@nomodel
interface EC_B {}

@nomodel
interface EC_C {}

@nomodel
interface EC_ABC satisfies EC_A & EC_B & EC_C {}

@nomodel
interface EC_Parameterised<out T>{
    shared formal T t();
}

@nomodel
interface EC_ParameterisedSelfBound<out T> of T {
    shared formal T t();
}

@nomodel
interface EC_DoubleParameters<T1,T2> satisfies EC_Parameterised<T1|T2>{}

@nomodel
void erasureCasting(EC_A & EC_B & EC_C tripleIntersectionParam,
                    EC_Parameterised<EC_A & EC_B & EC_C> rawParam,
                    EC_Parameterised<EC_Parameterised<EC_A & EC_B>> hasRawParam,
                    EC_DoubleParameters<EC_A,EC_A> hasLostParameter,
                    Callable<EC_A&EC_B,[]> erasedReturnCallable,
                    Callable<EC_A,[EC_A&EC_B]> erasedParamCallable,
                    EC_Parameterised<Nothing> hasErasedParameter,
                    EC_A a){
    /*
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
    Integer[] sequence2 = false then {} else 0..2;

    // those ones require casts because there's a bound on its type parameter
    // See https://github.com/ceylon/ceylon-compiler/issues/953
    ec_boundsOnElement(nothing);

    ec_boundsOnElement2(empty);
    
    ec_boundsOnElement3(hasErasedParameter);
    
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
*/
    Empty|Sequence<Integer&EC_A> rawSequence = {};
    if(nonempty rawSequence){}
}

@nomodel
void ec_boundsOnElement<Element>(Element elements) 
        given Element satisfies EC_Parameterised<Element> {
}

@nomodel
void ec_boundsOnElement2<Element>(Sequential<Element> elements) 
        given Element satisfies EC_A {
}

@nomodel
void ec_boundsOnElement3<Element>(EC_Parameterised<Element> elements) 
        given Element satisfies EC_A {
}

// Sequential<Sequential<Element|String>> erases to Sequential<Sequential<Object>>
// note that this bug is specific to Sequential, it does not happen with EC_Parameterised
@nomodel
Sequential<Sequential<Element|String>> ec_methodWithErasedBounds<Element>(Element data){
    return nothing;
}

@nomodel
EC_Parameterised<EC_Parameterised<Element|String>> ec_methodWithErasedBounds2<Element>(Element data){
    return nothing;
}
