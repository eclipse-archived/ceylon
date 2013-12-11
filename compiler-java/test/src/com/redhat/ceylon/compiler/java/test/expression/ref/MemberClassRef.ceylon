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
class MemberClassRef() {
    shared class Nullary() { 
        shared actual String string => "nullary()";
    }
    shared class Unary(String s)  { 
        shared actual String string => "unary(``s``)";
    }
    shared class Binary(String s1, String s2) { 
        shared actual String string => "binary(``s1``, ``s2``)";
    }
    shared class Ternary(String s1, String s2, String s3) { 
        shared actual String string => "ternary(``s1``, ``s2``, ``s3``)";
    }
    shared class Nary(String s1, String s2, String s3, String s4) { 
        shared actual String string => "nary(``s1``, ``s2``, ``s3``, ``s4``)";
    }
    
    shared void simple() {
        Nullary()(MemberClassRef) nullaryRef = MemberClassRef.Nullary;
        assert("nullary()" == nullaryRef(this)().string);
        
        Unary(String)(MemberClassRef) unaryRef = MemberClassRef.Unary;
        assert("unary(u)" == unaryRef(this)("u").string);
        
        Binary(String, String)(MemberClassRef) binaryRef = MemberClassRef.Binary;
        assert("binary(b1, b2)" == binaryRef(this)("b1", "b2").string);
        
        Ternary(String, String, String)(MemberClassRef) ternaryRef = MemberClassRef.Ternary;
        assert("ternary(t1, t2, t3)" == ternaryRef(this)("t1", "t2", "t3").string);
        
        Nary(String, String, String, String)(MemberClassRef) naryRef = MemberClassRef.Nary;
        assert("nary(n1, n2, n3, n4)" == naryRef(this)("n1", "n2", "n3", "n4").string);
    }
    
    shared class UnaryDefaulted(String s="s") { 
        shared actual String string => "unaryDefaulted(``s``)";
    }
    shared class BinaryDefaulted(String s1="s1", String s2="s2") { 
        shared actual String string => "binaryDefaulted(``s1``, ``s2``)";
    }
    shared class TernaryDefaulted(String s1="s1", String s2="s2", String s3="s3") { 
        shared actual String string => "ternaryDefaulted(``s1``, ``s2``, ``s3``)";
    }
    shared class NaryDefaulted(String s1="s1", String s2="s2", String s3="s3", String s4="s4") { 
        shared actual String string => "naryDefaulted(``s1``, ``s2``, ``s3``, ``s4``)";
    }    
    
    shared void defaulted() {
        // can't have a defaulted nullary
        
        UnaryDefaulted(String=)(MemberClassRef) unaryRef = MemberClassRef.UnaryDefaulted;
        assert("unaryDefaulted(u)" == unaryRef(this)("u").string);
        assert("unaryDefaulted(s)" == unaryRef(this)().string);
        
        BinaryDefaulted(String=, String=)(MemberClassRef) binaryRef = MemberClassRef.BinaryDefaulted;
        assert("binaryDefaulted(b1, b2)" == binaryRef(this)("b1", "b2").string);
        assert("binaryDefaulted(b1, s2)" == binaryRef(this)("b1").string);
        assert("binaryDefaulted(s1, s2)" == binaryRef(this)().string);
        
        TernaryDefaulted(String=, String=, String=)(MemberClassRef) ternaryRef = MemberClassRef.TernaryDefaulted;
        assert("ternaryDefaulted(t1, t2, t3)" == ternaryRef(this)("t1", "t2", "t3").string);
        assert("ternaryDefaulted(t1, t2, s3)" == ternaryRef(this)("t1", "t2").string);
        assert("ternaryDefaulted(t1, s2, s3)" == ternaryRef(this)("t1").string);
        assert("ternaryDefaulted(s1, s2, s3)" == ternaryRef(this)().string);
        
        NaryDefaulted(String=, String=, String=, String=)(MemberClassRef) naryRef = MemberClassRef.NaryDefaulted;
        assert("naryDefaulted(n1, n2, n3, n4)" == naryRef(this)("n1", "n2", "n3", "n4").string);
        assert("naryDefaulted(n1, n2, n3, s4)" == naryRef(this)("n1", "n2", "n3").string);
        assert("naryDefaulted(n1, n2, s3, s4)" == naryRef(this)("n1", "n2").string);
        assert("naryDefaulted(n1, s2, s3, s4)" == naryRef(this)("n1").string);
        assert("naryDefaulted(s1, s2, s3, s4)" == naryRef(this)().string);
    }
    
    
    shared class NullarySequenced(String* s) { 
        shared actual String string => "nullarySequenced(``s``)";
    }
    shared class UnarySequenced(String s1, String* s) { 
        shared actual String string => "unarySequenced(``s1``; ``s``)";
    }
    shared class BinarySequenced(String s1, String s2, String* s) { 
        shared actual String string => "binarySequenced(``s1``, ``s2``; ``s``)";
    }
    shared class TernarySequenced(String s1, String s2, String s3, String* s) { 
        shared actual String string => "ternarySequenced(``s1``, ``s2``, ``s3``; ``s``)";
    }
    shared class NarySequenced(String s1, String s2, String s3, String s4, String* s) { 
        shared actual String string => "narySequenced(``s1``, ``s2``, ``s3``, ``s4``; ``s``)";
    }
    
    shared void sequenced() {
        NullarySequenced(String*)(MemberClassRef) nullaryRef = MemberClassRef.NullarySequenced;
        assert("nullarySequenced([])" == nullaryRef(this)().string);
        assert("nullarySequenced([r1])" == nullaryRef(this)("r1").string);
        assert("nullarySequenced([r1, r2])" == nullaryRef(this)("r1", "r2").string);
        
        UnarySequenced(String, String*)(MemberClassRef) unaryRef = MemberClassRef.UnarySequenced;
        assert("unarySequenced(s; [])" == unaryRef(this)("s").string);
        assert("unarySequenced(s; [r1])" == unaryRef(this)("s", "r1").string);
        assert("unarySequenced(s; [r1, r2])" == unaryRef(this)("s", "r1", "r2").string);
        
        BinarySequenced(String, String, String*)(MemberClassRef) binaryRef = MemberClassRef.BinarySequenced;
        assert("binarySequenced(s1, s2; [])" == binaryRef(this)("s1", "s2").string);
        assert("binarySequenced(s1, s2; [r1])" == binaryRef(this)("s1", "s2", "r1").string);
        assert("binarySequenced(s1, s2; [r1, r2])" == binaryRef(this)("s1", "s2", "r1", "r2").string);
        
        TernarySequenced(String, String, String, String*)(MemberClassRef) ternaryRef = MemberClassRef.TernarySequenced;
        assert("ternarySequenced(s1, s2, s3; [])" == ternaryRef(this)("s1", "s2", "s3").string);
        assert("ternarySequenced(s1, s2, s3; [r1])" == ternaryRef(this)("s1", "s2", "s3", "r1").string);
        assert("ternarySequenced(s1, s2, s3; [r1, r2])" == ternaryRef(this)("s1", "s2", "s3", "r1", "r2").string);
        
        NarySequenced(String, String, String, String, String*)(MemberClassRef) naryRef = MemberClassRef.NarySequenced;
        assert("narySequenced(s1, s2, s3, s4; [])" == naryRef(this)("s1", "s2", "s3", "s4").string);
        assert("narySequenced(s1, s2, s3, s4; [r1])" == naryRef(this)("s1", "s2", "s3", "s4", "r1").string);
        assert("narySequenced(s1, s2, s3, s4; [r1, r2])" == naryRef(this)("s1", "s2", "s3", "s4", "r1", "r2").string);
        
    }
    shared class NullaryParameterized<T1>()
            given T1 satisfies Object {
        shared actual String string => "nullary()";
    }
    shared class UnaryParameterized<T1>(T1 s)
            given T1 satisfies Object {
        shared actual String string => "unary(``s``)";
    }
    shared class BinaryParameterized<T1, T2>(T1 s1, T2 s2)
            given T1 satisfies Object
            given T2 satisfies Object {
        shared actual String string => "binary(``s1``, ``s2``)";
    }
    shared class TernaryParameterized<T1, T2, T3>(T1 s1, T2 s2, T3 s3)
            given T1 satisfies Object
            given T2 satisfies Object
            given T3 satisfies Object {
        shared actual String string => "ternary(``s1``, ``s2``, ``s3``)";
    }
    shared class NaryParameterized<T1,T2,T3,T4>(T1 s1, T2 s2, T3 s3, T4 s4)
            given T1 satisfies Object
            given T2 satisfies Object
            given T3 satisfies Object
            given T4 satisfies Object {
        shared actual String string => "nary(``s1``, ``s2``, ``s3``, ``s4``)";
    }
       
    shared void parameterizedMethod<X>(X x) 
            given X satisfies Object {
        NullaryParameterized<String>()(MemberClassRef) nullaryRef = MemberClassRef.NullaryParameterized<String>;
        assert("nullary()" == nullaryRef(this)().string);
        
        NullaryParameterized<X>()(MemberClassRef) nullaryXRef = MemberClassRef.NullaryParameterized<X>;
        assert("nullary()" == nullaryXRef(this)().string);
        
        MemberClassRef.UnaryParameterized<String>(String)(MemberClassRef) unaryRef = MemberClassRef.UnaryParameterized<String>;
        assert("unary(u)" == unaryRef(this)("u").string);
        
        MemberClassRef.UnaryParameterized<X>(X)(MemberClassRef) unaryXRef = MemberClassRef.UnaryParameterized<X>;
        assert("unary(foo)" == unaryXRef(this)(x).string);
        
        MemberClassRef.BinaryParameterized<String, String>(String, String)(MemberClassRef) binaryRef = MemberClassRef.BinaryParameterized<String, String>;
        assert("binary(b1, b2)" == binaryRef(this)("b1", "b2").string);
        
        MemberClassRef.TernaryParameterized<String, String, String>(String, String, String)(MemberClassRef) ternaryRef = MemberClassRef.TernaryParameterized<String, String, String>;
        assert("ternary(t1, t2, t3)" == ternaryRef(this)("t1", "t2", "t3").string);
        
        MemberClassRef.NaryParameterized<String, String, String, String>(String, String, String, String)(MemberClassRef) naryRef = MemberClassRef.NaryParameterized<String, String, String, String>;
        assert("nary(n1, n2, n3, n4)" == naryRef(this)("n1", "n2", "n3", "n4").string);
    }
    
    // TODO parameterized Qualifier
    
    shared class Inner(String s) {
        shared class M(String s2) {
            shared actual String string => "Inner(``s``).M(``s2``)";
        }
    }
    shared void innerClass() {
        Inner.M(String)(MemberClassRef.Inner) innerMRef = MemberClassRef.Inner.M;
        assert("Inner(foo).M(bar)" == innerMRef(Inner("foo"))("bar").string);
    }
}

void memberClassRef() {
    value mcr = MemberClassRef();
    mcr.simple();
    mcr.defaulted();
    mcr.sequenced();
    mcr.parameterizedMethod("foo");
    mcr.innerClass();
}