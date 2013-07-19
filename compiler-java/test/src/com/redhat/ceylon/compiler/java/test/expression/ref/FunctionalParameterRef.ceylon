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
class FunctionalParameterRef(
    shared String nullary(),
    shared String unary(String s),
    shared String binary(String s1, String s2),
    shared String ternary(String s1, String s2, String s3),
    shared String nary(String s1, String s2, String s3, String s4),
    /*shared String unaryDefaulted(String s="s"),
    shared String binaryDefaulted(String s1="s1", String s2="s2"),
    shared String ternaryDefaulted(String s1="s1", String s2="s2", String s3="s3"),
    shared String naryDefaulted(String s1="s1", String s2="s2", String s3="s3", String s4="s4"),*/
    shared String nullarySequenced(String* s),
    shared String unarySequenced(String s1, String* s),
    shared String binarySequenced(String s1, String s2, String* s),
    shared String ternarySequenced(String s1, String s2, String s3, String* s),
    shared String narySequenced(String s1, String s2, String s3, String s4, String* s),
    shared String unaryUnaryMpl(String s)(Integer i)
    /*shared String nullaryParameterized<T1>()
            given T1 satisfies Object,
    shared String unaryParameterized<T1>(T1 s)
            given T1 satisfies Object,
    shared String binaryParameterized<T1, T2>(T1 s1, T2 s2)
            given T1 satisfies Object
            given T2 satisfies Object,
    shared String ternaryParameterized<T1, T2, T3>(T1 s1, T2 s2, T3 s3)
            given T1 satisfies Object
            given T2 satisfies Object
            given T3 satisfies Object,
    shared String naryParameterized<T1,T2,T3,T4>(T1 s1, T2 s2, T3 s3, T4 s4)
            given T1 satisfies Object
            given T2 satisfies Object
            given T3 satisfies Object
            given T4 satisfies Object*/
    ) {
    
    shared void simple() {
        String()(FunctionalParameterRef) nullaryRef = FunctionalParameterRef.nullary;
        assert("nullary()" == nullaryRef(this)());
        
        String(String)(FunctionalParameterRef) unaryRef = FunctionalParameterRef.unary;
        assert("unary(u)" == unaryRef(this)("u"));
        
        String(String, String)(FunctionalParameterRef) binaryRef = FunctionalParameterRef.binary;
        assert("binary(b1, b2)" == binaryRef(this)("b1", "b2"));
        
        String(String, String, String)(FunctionalParameterRef) ternaryRef = FunctionalParameterRef.ternary;
        assert("ternary(t1, t2, t3)" == ternaryRef(this)("t1", "t2", "t3"));
        
        String(String, String, String, String)(FunctionalParameterRef) naryRef = FunctionalParameterRef.nary;
        assert("nary(n1, n2, n3, n4)" == naryRef(this)("n1", "n2", "n3", "n4"));
    }

    /*    
    shared void defaulted() {
        // can't have a defaulted nullary
        
        String(String=)(FunctionalParameterRef) unaryRef = FunctionalParameterRef.unaryDefaulted;
        assert("unaryDefaulted(u)" == unaryRef(this)("u"));
        assert("unaryDefaulted(s)" == unaryRef(this)());
        
        String(String=, String=)(FunctionalParameterRef) binaryRef = FunctionalParameterRef.binaryDefaulted;
        assert("binaryDefaulted(b1, b2)" == binaryRef(this)("b1", "b2"));
        assert("binaryDefaulted(b1, s2)" == binaryRef(this)("b1"));
        assert("binaryDefaulted(s1, s2)" == binaryRef(this)());
        
        String(String=, String=, String=)(FunctionalParameterRef) ternaryRef = FunctionalParameterRef.ternaryDefaulted;
        assert("ternaryDefaulted(t1, t2, t3)" == ternaryRef(this)("t1", "t2", "t3"));
        assert("ternaryDefaulted(t1, t2, s3)" == ternaryRef(this)("t1", "t2"));
        assert("ternaryDefaulted(t1, s2, s3)" == ternaryRef(this)("t1"));
        assert("ternaryDefaulted(s1, s2, s3)" == ternaryRef(this)());
        
        String(String=, String=, String=, String=)(FunctionalParameterRef) naryRef = FunctionalParameterRef.naryDefaulted;
        assert("naryDefaulted(n1, n2, n3, n4)" == naryRef(this)("n1", "n2", "n3", "n4"));
        assert("naryDefaulted(n1, n2, n3, s4)" == naryRef(this)("n1", "n2", "n3"));
        assert("naryDefaulted(n1, n2, s3, s4)" == naryRef(this)("n1", "n2"));
        assert("naryDefaulted(n1, s2, s3, s4)" == naryRef(this)("n1"));
        assert("naryDefaulted(s1, s2, s3, s4)" == naryRef(this)());
    }*/
    
    shared void sequenced() {
        String(String*)(FunctionalParameterRef) nullaryRef = FunctionalParameterRef.nullarySequenced;
        assert("nullarySequenced({})" == nullaryRef(this)());
        assert("nullarySequenced([r1])" == nullaryRef(this)("r1"));
        assert("nullarySequenced([r1, r2])" == nullaryRef(this)("r1", "r2"));
        
        String(String, String*)(FunctionalParameterRef) unaryRef = FunctionalParameterRef.unarySequenced;
        assert("unarySequenced(s; {})" == unaryRef(this)("s"));
        assert("unarySequenced(s; [r1])" == unaryRef(this)("s", "r1"));
        assert("unarySequenced(s; [r1, r2])" == unaryRef(this)("s", "r1", "r2"));
        
        String(String, String, String*)(FunctionalParameterRef) binaryRef = FunctionalParameterRef.binarySequenced;
        assert("binarySequenced(s1, s2; {})" == binaryRef(this)("s1", "s2"));
        assert("binarySequenced(s1, s2; [r1])" == binaryRef(this)("s1", "s2", "r1"));
        assert("binarySequenced(s1, s2; [r1, r2])" == binaryRef(this)("s1", "s2", "r1", "r2"));
        
        String(String, String, String, String*)(FunctionalParameterRef) ternaryRef = FunctionalParameterRef.ternarySequenced;
        assert("ternarySequenced(s1, s2, s3; {})" == ternaryRef(this)("s1", "s2", "s3"));
        assert("ternarySequenced(s1, s2, s3; [r1])" == ternaryRef(this)("s1", "s2", "s3", "r1"));
        assert("ternarySequenced(s1, s2, s3; [r1, r2])" == ternaryRef(this)("s1", "s2", "s3", "r1", "r2"));
        
        String(String, String, String, String, String*)(FunctionalParameterRef) naryRef = FunctionalParameterRef.narySequenced;
        assert("narySequenced(s1, s2, s3, s4; {})" == naryRef(this)("s1", "s2", "s3", "s4"));
        assert("narySequenced(s1, s2, s3, s4; [r1])" == naryRef(this)("s1", "s2", "s3", "s4", "r1"));
        assert("narySequenced(s1, s2, s3, s4; [r1, r2])" == naryRef(this)("s1", "s2", "s3", "s4", "r1", "r2"));
        
    }
    
    shared void mpl() {
        String(Integer)(String)(FunctionalParameterRef) unaryUnaryRef = FunctionalParameterRef.unaryUnaryMpl;
        assert("unaryUnaryMpl(s, 1)" == unaryUnaryRef(this)("s")(1));
    }
    
    shared void assortedLanguage() {
        Boolean(Object)(Object) objectEqualsRef = Object.equals;
        assert(objectEqualsRef(this)(this));
        assert(!objectEqualsRef(this)(""));
        Boolean(Object)(String) stringEqualsRef = String.equals;
        assert(stringEqualsRef("")(""));
        assert(!stringEqualsRef("")(this));
        Integer(Integer)(Integer) integerPlus = Integer.plus;
        assert(2 == integerPlus(1)(1));
        assert(1 == integerPlus(0)(1));
        String(String)(String) stringPlus = String.plus;
        assert("foobar" == stringPlus("foo")("bar"));
        assert(stringEqualsRef("foobar")(stringPlus("foo")("bar")));
    }
 
    /*
    shared void parameterizedMethod<X>(X x) 
            given X satisfies Object {
        String()(FunctionalParameterRef) nullaryRef = FunctionalParameterRef.nullaryParameterized<String>;
        assert("nullary()" == nullaryRef(this)());
        
        String()(FunctionalParameterRef) nullaryXRef = FunctionalParameterRef.nullaryParameterized<X>;
        assert("nullary()" == nullaryXRef(this)());
        
        String(String)(FunctionalParameterRef) unaryRef = FunctionalParameterRef.unaryParameterized<String>;
        assert("unary(u)" == unaryRef(this)("u"));
        
        String(X)(FunctionalParameterRef) unaryXRef = FunctionalParameterRef.unaryParameterized<X>;
        assert("unary(foo)" == unaryXRef(this)(x));
        
        String(String, String)(FunctionalParameterRef) binaryRef = FunctionalParameterRef.binaryParameterized<String, String>;
        assert("binary(b1, b2)" == binaryRef(this)("b1", "b2"));
        
        String(String, String, String)(FunctionalParameterRef) ternaryRef = FunctionalParameterRef.ternaryParameterized<String, String, String>;
        assert("ternary(t1, t2, t3)" == ternaryRef(this)("t1", "t2", "t3"));
        
        String(String, String, String, String)(FunctionalParameterRef) naryRef = FunctionalParameterRef.naryParameterized<String, String, String, String>;
        assert("nary(n1, n2, n3, n4)" == naryRef(this)("n1", "n2", "n3", "n4"));
    }
    */
    
    // TODO Type parameterized qualifying type
    
    shared class Inner(String s) {
        shared String m(String s2) => "Inner(``s``).m(``s2``)";
    }
    shared void innerClass() {
        String(String)(FunctionalParameterRef.Inner) innerMRef = FunctionalParameterRef.Inner.m;
        assert("Inner(foo).m(bar)" == innerMRef(Inner("foo"))("bar"));
    } 
}

void functionalParameterRef() {
    value mr = FunctionalParameterRef{
        function nullary() => "nullary()";
        function unary(String s) => "unary(``s``)";
        function binary(String s1, String s2) => "binary(``s1``, ``s2``)";
        function ternary(String s1, String s2, String s3) => "ternary(``s1``, ``s2``, ``s3``)";
        function nary(String s1, String s2, String s3, String s4) => "nary(``s1``, ``s2``, ``s3``, ``s4``)";
        /*function unaryDefaulted(String s="s") => "unaryDefaulted(``s``)";
        function binaryDefaulted(String s1="s1", String s2="s2") => "binaryDefaulted(``s1``, ``s2``)";
        function ternaryDefaulted(String s1="s1", String s2="s2", String s3="s3") => "ternaryDefaulted(``s1``, ``s2``, ``s3``)";
        function naryDefaulted(String s1="s1", String s2="s2", String s3="s3", String s4="s4") => "naryDefaulted(``s1``, ``s2``, ``s3``, ``s4``)";*/
        function nullarySequenced(String* s) => "nullarySequenced(``s``)";
        function unarySequenced(String s1, String* s) => "unarySequenced(``s1``; ``s``)";
        function binarySequenced(String s1, String s2, String* s) => "binarySequenced(``s1``, ``s2``; ``s``)";
        function ternarySequenced(String s1, String s2, String s3, String* s) => "ternarySequenced(``s1``, ``s2``, ``s3``; ``s``)";
        function narySequenced(String s1, String s2, String s3, String s4, String* s) => "narySequenced(``s1``, ``s2``, ``s3``, ``s4``; ``s``)";
        function unaryUnaryMpl(String s)(Integer i) => "unaryUnaryMpl(``s``, ``i``)";
        /*function nullaryParameterized<T1>()
                given T1 satisfies Object
            => "nullary()";
        function unaryParameterized<T1>(T1 s)
                given T1 satisfies Object 
            => "unary(``s``)";
        function binaryParameterized<T1, T2>(T1 s1, T2 s2)
                given T1 satisfies Object
                given T2 satisfies Object
            => "binary(``s1``, ``s2``)";
        function ternaryParameterized<T1, T2, T3>(T1 s1, T2 s2, T3 s3)
                given T1 satisfies Object
                given T2 satisfies Object
                given T3 satisfies Object
            => "ternary(``s1``, ``s2``, ``s3``)";
        function naryParameterized<T1,T2,T3,T4>(T1 s1, T2 s2, T3 s3, T4 s4)
                given T1 satisfies Object
                given T2 satisfies Object
                given T3 satisfies Object
                given T4 satisfies Object
            => "nary(``s1``, ``s2``, ``s3``, ``s4``)";*/
    };
    mr.simple();
    //mr.defaulted();
    mr.sequenced();
    mr.mpl();
    mr.assortedLanguage();
    //mr.parameterizedMethod("foo");
    mr.innerClass();
}