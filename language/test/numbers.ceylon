shared void numbers() {
    assert(1==1, "natural equals");
    assert(1!=2, "natural not equals");
    assert(+1==+1, "integer equals");
    assert(+1!=+2, "integer not equals");
    assert(1.0==1.0, "float equals");
    assert(1.0!=2.0, "float not equals");
    assert(1!=+1, "natural equals integer");
    assert(1!=1.0, "natural equals float");
    assert(+1!=1.0, "integer equals float");
    assert(+1!=1, "natural equals integer");
    assert(1.0!=1, "natural equals float");
    assert(1.0!=+1, "integer equals float");
    assert(1<2, "natural comparison");
    assert(-1<+2, "integer comparison");
    assert(-0.5<0.0, "float comparison");
    assert(1+1==2, "natural addition");
    assert(1-1==0, "natural subtraction");
    assert(2*2==4, "natural multiplication");
    assert(2**3==8, "natural exponentiation");
    assert(+1 + -1==+0, "integer addition");
    assert(+1 - -1==+2, "integer subtraction");
    assert(-2*+2==-4, "integer multiplication");
    assert(-2**(+3)==-8, "integer exponentiation");
    assert(1.0+0.5==1.5, "float addition");
    assert(1.5-0.5==1.0, "float subtraction");
    assert(2.0*2.0==4.0, "float multiplication");
    assert(2.0**3.0==8.0, "float exponentiation");
}