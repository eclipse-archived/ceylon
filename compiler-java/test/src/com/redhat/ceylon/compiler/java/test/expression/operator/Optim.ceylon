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
variable Integer optimToplevelInteger = 0;

@nomodel
abstract class OptimSuperclass<I,F,C,S,B>(){
    formal shared void boxedIntegerArithmetic(I n2);
    formal shared void boxedFloatArithmetic(F n2);
    formal shared void boxedStringOperators(S s2);
    formal shared void boxedBooleanLogical(B b2);

    formal shared void boxedIntegerComparison(I n2);
    formal shared void boxedFloatComparison(F n2);
    formal shared void boxedStringComparison(S s2);
    formal shared void boxedCharacterComparison(C c2);
    formal shared void boxedBooleanComparison(B b2);
}

@nomodel
class Optim() extends OptimSuperclass<Integer,Float,Character,String,Boolean>(){
    Integer getInteger() { return 1; }
    Float getFloat() { return 1.0; }
    T getBoxed<T>(T t){ return t; }
    
    void unboxedIntegerArithmetic(){
        variable Integer n1 = 0;
        variable Integer n2 = 0;
        variable Integer n3 = 0;

        n1++;
        ++n1;
        n1--;
        --n1;
        
        n1 = +n2;
        n1 = +0;
        n1 = +getInteger();
        n1 = -n2;
        n1 = -0;
        n1 = -getInteger();

        n1 = n2 + n3;
        n1 = n2 - n3;
        n1 = n2 * n3;
        n1 = n2 / n3;
        n1 = n2 % n3;
        n1 = n2 ^ n3;
        
        n1 = n2 += n3;
        n1 = n2 *= n3;
        n1 = n2 /= n3;
        n1 = n2 %= n3;
    }

    shared actual void boxedIntegerArithmetic(Integer n2){
        variable Integer n1 = 0;
        variable Integer n3 = 0;

        n1++;
        ++n1;
        n1--;
        --n1;
        
        n1 = +n2;
        n1 = +0;
        n1 = +getBoxed(1);
        n1 = -n2;
        n1 = -0;
        n1 = -getBoxed(1);

        n1 = n2 + n3;
        n1 = n2 - n3;
        n1 = n2 * n3;
        n1 = n2 / n3;
        n1 = n2 % n3;
        n1 = n2 ^ n3;
        
        n1 = n3 += n2;
        n1 = n3 *= n2;
        n1 = n3 /= n2;
        n1 = n3 %= n2;
    }

    void unboxedFloatArithmetic(){
        variable Float n1 = 0.0;
        variable Float n2 = 0.0;
        variable Float n3 = 0.0;

        n1 = +n2;
        n1 = +0.0;
        n1 = +getFloat();
        n1 = -n2;
        n1 = -0.0;
        n1 = -getFloat();

        n1 = n2 + n3;
        n1 = n2 - n3;
        n1 = n2 * n3;
        n1 = n2 / n3;
        n1 = n2 ^ n3;
        
        n1 = n2 += n3;
        n1 = n2 *= n3;
        n1 = n2 /= n3;
    }

    shared actual void boxedFloatArithmetic(Float n2){
        variable Float n1 = 0.0;
        variable Float n3 = 0.0;

        n1 = +n2;
        n1 = +0.0;
        n1 = +getBoxed(0.0);
        n1 = -n2;
        n1 = -0.0;
        n1 = -getBoxed(0.0);

        n1 = n2 + n3;
        n1 = n2 - n3;
        n1 = n2 * n3;
        n1 = n2 / n3;
        n1 = n2 ^ n3;
        
        n1 = n3 += n2;
        n1 = n3 *= n2;
        n1 = n3 /= n2;
    }

    void unboxedCharacterArithmetic(){
        variable Character n1 = 'a';
        variable Character n2 = 'a';
        variable Character n3 = 'a';

        n1++;
        ++n1;
        n1--;
        --n1;
    }

    void unboxedBooleanLogical(){
        variable Boolean b1 = false;
        variable Boolean b2 = false;
        variable Boolean b3 = false;
        
        b1 = b2;
        b1 = !b2;
        b1 = b2 && b3;
        b1 = b2 || b3;

        b1 = b2 ||= b3;
        b1 = b2 &&= b3;
    }

    shared actual void boxedBooleanLogical(Boolean b2){
        variable Boolean b1 = false;
        variable Boolean b3 = false;
        
        b1 = b2;
        b1 = !b2;
        b1 = b2 && b3;
        b1 = b2 || b3;

        b1 = b3 ||= b2;
        b1 = b3 &&= b2;
    }

    void unboxedStringOperators(){
        variable String s1 = "";
        variable String s2 = "";
        variable String s3 = "";

        s1 = s2 + s3;

        s1 = s2 += s3;
    }

    shared actual void boxedStringOperators(String s2){
        variable String s1 = "";
        variable String s3 = "";

        s1 = s2 + s3;

        s1 = s3 += s2;
    }

    // FIXME: shouldn't this be optimisable and used as a field?
    variable Integer fieldInteger = 0;
    Integer fieldGetterInteger{ return 0; } assign fieldGetterInteger{}

    void nonOptimisableArithmetic(){
        Integer localGetterInteger { return 0; } assign localGetterInteger{}

        fieldInteger = fieldInteger++;
        this.fieldInteger = this.fieldInteger++;
        fieldGetterInteger = fieldGetterInteger++;
        localGetterInteger = localGetterInteger++;
        optimToplevelInteger = optimToplevelInteger++;
        
        fieldInteger = ++fieldInteger;
        this.fieldInteger = ++this.fieldInteger;
        fieldGetterInteger = ++fieldGetterInteger;
        localGetterInteger = ++localGetterInteger;
        optimToplevelInteger = ++optimToplevelInteger;
        
        fieldInteger = fieldInteger += fieldInteger;
        this.fieldInteger = this.fieldInteger += this.fieldInteger;
        fieldGetterInteger = fieldGetterInteger += fieldGetterInteger;
        localGetterInteger = localGetterInteger += localGetterInteger;
        optimToplevelInteger = optimToplevelInteger += optimToplevelInteger;
    }

    void unboxedIntegerComparison(){
        variable Integer n1 = 0;
        variable Integer n2 = 0;
        variable Boolean sync;
        
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    shared actual void boxedIntegerComparison(Integer n2){
        variable Integer n1 = 0;
        variable Boolean sync;
        
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    void unboxedFloatComparison(){
        variable Float n1 = 0.0;
        variable Float n2 = 0.0;
        variable Boolean sync;
        
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    shared actual void boxedFloatComparison(Float n2){
        variable Float n1 = 0.0;
        variable Boolean sync;
        
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    void unboxedCharacterComparison(){
        variable Character n1 = 'a';
        variable Character n2 = 'a';
        variable Boolean sync;
        
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    shared actual void boxedCharacterComparison(Character n2){
        variable Character n1 = 'a';
        variable Boolean sync;
        
        sync = n1 == n2;
        sync = n1 != n2;
        Comparison c = n1 <=> n2;
        sync = n1 < n2;
        sync = n1 > n2;
        sync = n1 <= n2;
        sync = n1 >= n2;
    }

    void unboxedBooleanComparison(){
        variable Boolean b1 = false;
        variable Boolean b2 = false;
        variable Boolean sync;
        
        sync = b1 === b2;
        sync = b1 == b2;
        sync = b1 != b2;
    }

    shared actual void boxedBooleanComparison(Boolean b2){
        variable Boolean b1 = false;
        variable Boolean sync;
        
        sync = b1 === b2;
        sync = b1 == b2;
        sync = b1 != b2;
    }

    void unboxedStringComparison(){
        variable String s1 = "";
        variable String s2 = "";
        variable Boolean sync;
        
        sync = s1 == s2;
        sync = s1 != s2;
        Comparison c = s1 <=> s2;
        sync = s1 < s2;
        sync = s1 > s2;
        sync = s1 <= s2;
        sync = s1 >= s2;
    }

    shared actual void boxedStringComparison(String s2){
        variable String s1 = "";
        variable Boolean sync;
        
        sync = s1 == s2;
        sync = s1 != s2;
        Comparison c = s1 <=> s2;
        sync = s1 < s2;
        sync = s1 > s2;
        sync = s1 <= s2;
        sync = s1 >= s2;
    }

    void nonOptimisableComparison(Basic o){
        variable String s1 = "";
        variable Boolean b2 = false;
        variable Boolean sync;
        
        sync = o === b2;
        sync = s1 == b2;
        sync = s1 != b2;
    }
}