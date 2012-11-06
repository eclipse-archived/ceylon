void splitFunction() {
    Float f(Float x);
    Float g(Float x);
    Integer h(Float x);
    Float i(Float x);
    Float j(Object x);
    Float k(Float x);
    Float l(Float x);
    
    f(Float x) = x**0.5;
    j(Float x) = x**0.5;
    
    @error g(Float y, Float x) = x**0.5;
    h(@error Integer x) = x**2;
    @error i() = 0.5;
    k(@error Object x) = 1.0;
    @error l(Float x) = 3;
}