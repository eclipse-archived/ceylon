class OrderedPair<out X,out Y>(X x, Y y) 
        given X satisfies Format&Equality
        given Y satisfies Format&Equality {
    shared X x = x;
    shared Y y = y;
    shared actual default String string {
        return "(" x ", " y ")";
    }
    shared actual Boolean equals(Equality other) {
        if (is OrderedPair<Format&Equality,Format&Equality> other) {
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
    assert(Complex(0.0,1.0)+Complex(1.0,0.0)==Complex(1.0,1.0), "");
    assert(Complex(0.0,1.0)+Complex(1.0,0.0)!=Complex(0.0,0.0), "");
    assert(Complex(0.0,1.0)==OrderedPair(0.0,1.0), "");
}