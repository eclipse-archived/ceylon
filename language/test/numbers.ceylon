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
    assert(1-1==+0, "natural subtraction");
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
    
    assert(is Natural 1+1, "natural addition");
    assert(is Integer 1-2, "natural subtraction");
    assert(is Integer +1+1, "integer addition");
    assert(is Integer +1-2, "integer subtraction");
    
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
    
    function add<T>(T x, T y) given T satisfies Numeric<T> {
        return x.plus(y);
    }
    function exp<T>(T x, T y) given T satisfies Numeric<T> {
        return x.power(y);
    }
        
    function addNaturals(Natural x, Natural y) {
        return x+y;
    }
    function addIntegers(Integer x, Integer y) {
        return x+y;
    }
    function addFloats(Float x, Float y) {
        return x+y;
    }
    function multiplyNaturalByFloat(Natural x, Float y) {
        return x*y;
    }
    function multiplyFloatByInteger(Float x, Integer y) {
        return x*y;
    }
    function multiplyNaturalByInteger(Natural x, Integer y) {
        return x*y;
    }
        
    assert(add(1,2)==3, "");
    assert(add(-1,+2)==+1, "");
    assert(add(1.5,-2.5)==-1.0, "");
    
    assert(exp(1,2)==1, "");
    assert(exp(-1,+2)==+1, "");
    assert(exp(2.0,2.0)==4.0, "");
    
    assert(addNaturals(2, 4)==6, "");
    assert(addIntegers(-2, -4)==-6, "");
    assert(addFloats(-1.0, 1.0)==0.0, "");
    
    assert(multiplyNaturalByFloat(3, 1.5)==4.5, "");
    assert(multiplyFloatByInteger(1.5, -1)==-1.5, "");                
    assert(multiplyNaturalByInteger(1, -1)==-1, "");
    
    assert(1.hash==(3-1)/2.hash, "natural hash");
    assert((+0).hash==(-1+(+1))*+100.hash, "integer hash");
    assert((2.2*2.2*2.2).hash==(2.2**3.0).hash, "float hash");
    
    assert(1.6.natural==2, "float natural");
    assert(1.1.natural==1, "float natural");
    assert((-1.6).integer==-2, "float integer");
    assert((-1.1).integer==-1, "float integer");
    assert(2.float==2.0, "natural float");
    assert((-3).float==-3.0, "integer float");
    assert(4.integer==+4, "natural integer");
    
    assert(1.plus { other=2; }.equals { that=3; }, "natural named args");
                
    variable value i:=0;
    for (x in 1..10) {
        i:=i+1;
        assert(x>=1&&x<=10, "natural range");
    }
    assert(i==10, "natural range");

    i:=0;
    for (x in -5..+5) {
        i:=i+1;
        assert(x>=-5&&x<=+5, "integer range");
    }
    assert(i==11, "integer range");
    
    assert(min({1, 5})==1, "min naturals");
    assert(min({-1, +5})==-1, "min integers");
    assert(min({-1.5, 5.2})==-1.5, "min floats");
    assert(max({1, 5})==5, "max naturals");
    assert(max({-1, +5})==+5, "max integers");
    assert(max({-1.5, 5.2})==5.2, "max floats");

    variable value count := 0;
    count++;
    ++count;
    count+=2;
    count*=3;
    assert(count==12, "natural increment");
    assert(--count==11, "natural decrement");
    assert(count--==11, "natural decrement");
    
    variable value intcount := +0;
    intcount++;
    ++intcount;
    intcount+=2;
    intcount*=-3;
    intcount--;
    assert(intcount==-13, "integer increment");
    assert(--intcount==-14, "integer decrement");
    assert(intcount--==-14, "integer decrement");
    
    variable value floatcount := 0.0;
    floatcount+=2.0;
    floatcount*=3.0;
    floatcount/=1.5;
    assert(floatcount==4.0, "float increment");
    assert((floatcount*=2.5)==10.0, "float scale");
    assert((floatcount/=2.0)==5.0, "float scale");
    assert((floatcount:=-2.0)==-2.0, "float assign");
    
    variable Natural vi;
    variable Natural vj;
    vi:=vj:=2;
    assert(vi==2&&vj==2, "multi assign");
    vi+=1;
    vj*=2;
    
    class Inner() {
        shared variable Natural vi:=0;
        shared variable Natural vj:=0;
    }
    value inner = Inner();
    inner.vi:=inner.vj:=2;
    inner.vi+=1;
    inner.vj*=2;
    
}