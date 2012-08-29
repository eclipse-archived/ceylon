class OrderedPair<out X,out Y>(x, y) 
        given X satisfies Object
        given Y satisfies Object {
    shared X x;
    shared Y y;
    shared actual default String string {
        return "(" x ", " y ")";
    }
    shared actual Boolean equals(Object other) {
        if (is OrderedPair<Object,Object> other) {
            return x==other.x && y==other.y;
        }
        else {
            return false;
        }
    }
}

class Complex(Float re, Float im) 
        extends OrderedPair<Float,Float>(re,im) 
        satisfies Summable<Complex> {
    shared actual String string {
        return "" re "+" im "i";
    }
    shared actual Complex plus(Complex other) {
        return Complex(re+other.re,im+other.im);
    }
}

void complex() {
    check(Complex(0.0,1.0)+Complex(1.0,0.0)==Complex(1.0,1.0), "");
    check(Complex(0.0,1.0)+Complex(1.0,0.0)!=Complex(0.0,0.0), "");
    check(Complex(0.0,1.0)==OrderedPair(0.0,1.0), "");
}
