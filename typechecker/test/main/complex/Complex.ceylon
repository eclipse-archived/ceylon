"A complex number class demonstrating operator polymorphism 
 in Ceylon."
class Complex(shared Float re, shared Float im) 
        satisfies Exponentiable<Complex,Integer> {
    
    positiveValue => this;

    negativeValue => Complex(-re,-im);

    plus(Complex other) => Complex(re+other.re, im+other.im);

    minus(Complex other) => Complex(re-other.re, im-other.im);

    times(Complex other) =>
            Complex(re*other.re-im*other.im, 
                    re*other.im+im*other.re);
    
    shared actual Complex divided(Complex other) {
        Float d = other.re^2 + other.im^2;
        return Complex((re*other.re+im*other.im)/d, 
                       (im*other.re-re*other.im)/d);
    }

    "Accepts non-negative powers."
    shared actual Complex power(Integer other) {
        doc "exponent must be non-negative"
        assert(other>=0);
        //lame impl
        variable Complex result = Complex(1.0, 0.0);
        for (i in 0:other) {
            result*=this;
        }
        return result;
    }

    string => im<0.0 then "``re``-``-im``i" 
                     else "``re``+``im``i";
    
    hash => re.hash + im.hash;
    
    shared actual Boolean equals(Object that) {
        if (is Complex that) {
            return re==that.re && im==that.im;
        }
        else {
            return false;
        }
    }
    
}

void testComplex() {
    Complex one = Complex(1.0, 0.0);
    Complex i = Complex(0.0, 1.0);
    Complex zero = Complex(0.0,0.0);
    Complex sum = one+i+zero;
    assert (sum==Complex(1.0, 1.0));
    Complex product = one*i*zero;
    assert (product==zero);
    Complex diff = -one-zero-i;
    assert (diff==Complex(-1.0, -1.0));
    Complex power = i^3;
    assert (power==Complex(-0.0, -1.0));
    Complex quot = one/i;
    assert(quot==-i);
}