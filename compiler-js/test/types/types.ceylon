void expect(Equality actual, Equality expected, String text) {
    if (actual == expected) {
        print("[ok] " + text + ": '" + actual.string + "'");
    } else {
        print("[NOT OK] " + text + ": actual='" + actual.string + "', expected='"
              + expected.string + "'");
    }
}

class Pair<X,Y>(X x, Y y) 
        given X satisfies Object
        given Y satisfies Object {
    shared actual default String string {
        return "(" + x.string + ", " + y.string + ")"; 
    }
}

class Complex(Float x, Float y) 
        extends Pair<Float, Float>(x,y) {
    shared actual String string {
        return x.string + "+" + y.string + "i"; 
    }
    shared String pairString {
        return super.string;
    }
}

interface List<out X> {
    shared formal Integer size;
    shared default Boolean empty {
        return size==0;
    }
}

class ConcreteList<out X>(X... xs) 
        satisfies List<X> {
    shared actual Integer size {
        return 0;
    }
    shared actual Boolean empty {
        return true;
    }
}

class Couple<X>(X x, X y) 
        extends Pair<X,X>(x,y) 
        given X satisfies Object {
    shared X x = x;
    shared X y = y;
}

class Issue9C1() {
    shared default String test() { return "1"; }
}
class Issue9C2() extends Issue9C1() {
    variable Boolean flag1 := false;
    shared actual default String test() {
        if (flag1) {
            return "ERR1";
        }
        flag1 := true;
        return super.test() + "2";
    }
}
class Issue9C3() extends Issue9C2() {
    variable Boolean flag2 := false;
    shared actual default String test() {
        if (flag2) {
            return "ERR2";
        }
        flag2 := true;
        return super.test() + "3";
    }
}

void testIssue9() {
    value obj = Issue9C3();
    expect(obj.test(), "123", "Issue #9");
}

shared void test() {
    value pair = Pair("hello", "world");
    print(pair);
    value zero = Complex(0.0, 0.0);
    print(zero);
    print(zero.pairString);
    print(ConcreteList().empty);
    
    testIssue9();
}