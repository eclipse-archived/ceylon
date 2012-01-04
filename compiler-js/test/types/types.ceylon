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

shared void test() {
    value pair = Pair("hello", "world");
    print(pair);
    value zero = Complex(0.0, 0.0);
    print(zero);
    print(zero.pairString);
    print(ConcreteList().empty);
}