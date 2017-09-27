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
    // functional parameters defaulted parameters not permitted
    shared String nullarySequenced(String* s),
    shared String unarySequenced(String s1, String* s),
    shared String binarySequenced(String s1, String s2, String* s),
    shared String ternarySequenced(String s1, String s2, String s3, String* s),
    shared String narySequenced(String s1, String s2, String s3, String s4, String* s),
    shared String unaryUnaryMpl(String s)(Integer i)
    // functional parameters with type parameters not permitted
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
    
    shared void sequenced() {
        String(String*)(FunctionalParameterRef) nullaryRef = FunctionalParameterRef.nullarySequenced;
        assert("nullarySequenced([])" == nullaryRef(this)());
        assert("nullarySequenced([r1])" == nullaryRef(this)("r1"));
        assert("nullarySequenced([r1, r2])" == nullaryRef(this)("r1", "r2"));
        
        String(String, String*)(FunctionalParameterRef) unaryRef = FunctionalParameterRef.unarySequenced;
        assert("unarySequenced(s; [])" == unaryRef(this)("s"));
        assert("unarySequenced(s; [r1])" == unaryRef(this)("s", "r1"));
        assert("unarySequenced(s; [r1, r2])" == unaryRef(this)("s", "r1", "r2"));
        
        String(String, String, String*)(FunctionalParameterRef) binaryRef = FunctionalParameterRef.binarySequenced;
        assert("binarySequenced(s1, s2; [])" == binaryRef(this)("s1", "s2"));
        assert("binarySequenced(s1, s2; [r1])" == binaryRef(this)("s1", "s2", "r1"));
        assert("binarySequenced(s1, s2; [r1, r2])" == binaryRef(this)("s1", "s2", "r1", "r2"));
        
        String(String, String, String, String*)(FunctionalParameterRef) ternaryRef = FunctionalParameterRef.ternarySequenced;
        assert("ternarySequenced(s1, s2, s3; [])" == ternaryRef(this)("s1", "s2", "s3"));
        assert("ternarySequenced(s1, s2, s3; [r1])" == ternaryRef(this)("s1", "s2", "s3", "r1"));
        assert("ternarySequenced(s1, s2, s3; [r1, r2])" == ternaryRef(this)("s1", "s2", "s3", "r1", "r2"));
        
        String(String, String, String, String, String*)(FunctionalParameterRef) naryRef = FunctionalParameterRef.narySequenced;
        assert("narySequenced(s1, s2, s3, s4; [])" == naryRef(this)("s1", "s2", "s3", "s4"));
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
        function nullarySequenced(String* s) => "nullarySequenced(``s``)";
        function unarySequenced(String s1, String* s) => "unarySequenced(``s1``; ``s``)";
        function binarySequenced(String s1, String s2, String* s) => "binarySequenced(``s1``, ``s2``; ``s``)";
        function ternarySequenced(String s1, String s2, String s3, String* s) => "ternarySequenced(``s1``, ``s2``, ``s3``; ``s``)";
        function narySequenced(String s1, String s2, String s3, String s4, String* s) => "narySequenced(``s1``, ``s2``, ``s3``, ``s4``; ``s``)";
        function unaryUnaryMpl(String s)(Integer i) => "unaryUnaryMpl(``s``, ``i``)";
    };
    mr.simple();
    mr.sequenced();
    mr.mpl();
    mr.assortedLanguage();
    mr.innerClass();
}