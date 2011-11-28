shared void numbers() {
    assert(1==1, "natural equals");
    assert(1!=2, "natural not equals");
    assert(+1==+1, "integer equals");
    assert(+1!=+2, "integer not equals");
    assert(1.0==1.0, "float equals");
    assert(1.0!=2.0, "float not equals");
    assert(1==+1, "natural equals integer");
    assert(1==1.0, "natural equals float");
    assert(+1==1.0, "integer equals float");
    assert(+1==1, "natural equals integer");
    assert(1.0==1, "natural equals float");
    assert(1.0==+1, "integer equals float");
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
    assert(2*2.5==5.0, "natural times float");
    assert(2.5*3==7.5, "natural times float");
    assert(-1*3==-3, "natural times integer");
    assert(2*-3==-6, "natural times integer");
    assert(-1*1.5==-1.5, "integer times float");
    assert(-1.5*+2==-3.0, "integer times float");
    
    assert(1.negativeValue==-1, "natural negative");
    assert(-1.negativeValue==+1, "integer negative");
    assert(1.0.negativeValue==-1.0, "float negative");
    assert(1.positiveValue==1, "natural positive");
    assert(-1.positiveValue==-1, "integer positive");
    assert(1.0.positiveValue==1.0, "float positive");
    
    assert(12.string=="12", "natural string");
    assert((-12).string=="-12", "integer string");
    assert((-5.5).string=="-5.5", "float string");
    
    assert(1.unit, "natural unit");
    assert(!2.unit, "natural unit");
    assert(0.zero, "natural zero");
    assert(!1.zero, "natural zero");
    assert(2.successor==3, "natural successor");
    assert(2.predecessor==1, "natural predecessor");
    assert((+1).unit, "integer unit");
    assert(!(-1).unit, "integer unit");
    assert((+0).zero, "integer zero");
    assert(!(+1).zero, "integer zero");
    assert((-2).successor==-1, "integer successor");
    assert((-2).predecessor==-3, "integer predecessor");
    
    assert(2.fractionalPart==0, "natural fractional");
    assert((-1).fractionalPart==0, "integer fractional");
    assert(1.5.fractionalPart==0.5, "float fractional");
    assert(2.wholePart==2, "natural fractional");
    assert((-1).wholePart==-1, "integer fractional");
    assert(1.5.wholePart==1.0, "float fractional");
    
    assert((+2).sign==+1, "integer sign");
    assert((-3).sign==-1, "integer sign");
    assert(2.0.sign==+1, "integer sign");
    assert((-3.0).sign==-1, "integer sign");
}