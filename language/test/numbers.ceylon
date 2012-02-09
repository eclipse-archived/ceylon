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
    
    Object? obj(Object? x) { return x; }
    assert(is Integer obj(1+1), "natural addition");
    assert(is Integer obj(1-2), "natural subtraction");
    assert(is Integer obj(+1+1), "integer addition");
    assert(is Integer obj(+1-2), "integer subtraction");
    
    assert(1.negativeValue==-1, "natural negative");
    assert(-1.negativeValue==+1, "integer negative");
    assert(1.0.negativeValue==-1.0, "float negative");
    assert(1.positiveValue==1, "natural positive");
    assert(-1.positiveValue==-1, "integer positive");
    assert(1.0.positiveValue==1.0, "float positive");
    
    assert(12.string=="12", "natural string 12");
    assert((-12).string=="-12", "integer string -12");
    assert((-5.5).string=="-5.5", "float string -5.5");
    assert((1.0).string=="1.0", "float string 1.0");
    
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
    
    function addIntegers(Integer x, Integer y) {
        return x+y;
    }
    function addFloats(Float x, Float y) {
        return x+y;
    }
    function multiplyIntegerByFloat(Integer x, Float y) {
        return x*y;
    }
    function multiplyFloatByInteger(Float x, Integer y) {
        return x*y;
    }
    function multiplyIntegerByInteger(Integer x, Integer y) {
        return x*y;
    }
        
    assert(add(1,2)==3, "add(1,2)==3");
    assert(add(-1,+2)==+1, "add(-1,+2)==+1");
    assert(add(1.5,-2.5)==-1.0, "add(1.5,-2.5)==-1.0");
    
    assert(exp(1,2)==1, "exp(1,2)==1");
    assert(exp(-1,+2)==+1, "exp(-1,+2)==+1");
    assert(exp(2.0,2.0)==4.0, "exp(2.0,2.0)==4.0");
    
    assert(addIntegers(2, 4)==6, "addIntegers(2, 4)==6");
    assert(addIntegers(-2, -4)==-6, "addIntegers(-2, -4)==-6");
    assert(addFloats(-1.0, 1.0)==0.0, "addFloats(-1.0, 1.0)==0.0");
    
    assert(multiplyIntegerByFloat(3, 1.5)==4.5, "multiplyIntegerByFloat(3, 1.5)==4.5");
    assert(multiplyFloatByInteger(1.5, -1)==-1.5, "multiplyFloatByInteger(1.5, -1)==-1.5");
    assert(multiplyIntegerByInteger(1, -1)==-1, "multiplyIntegerByInteger(1, -1)==-1");
    
    assert(1.hash==(3-1)/2.hash, "natural hash");
    assert((+0).hash==(-1+(+1))*+100.hash, "integer hash");
    assert((2.2*2.2*2.2).hash==(2.2**3.0).hash, "float hash");
    
    assert(1.6.integer==2, "float natural");
    assert(1.1.integer==1, "float natural");
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
    //eZL
    assert(max({ 2, 4, 6, 8, 7, 250, 5, 3, 1 })==250, "max");
    assert(min({ 200, 400, 600, 800, 700, 500, 300, 150 })==150, "min");

    //eZL
    assert(smallest(1,2)==1,          "smallest naturals");
    assert(smallest(-100, 100)==-100, "smallest integers");
    assert(smallest(-1.5, 5.2)==-1.5, "smallest floats");
    assert(largest(1,2)==2,           "largest naturals");
    assert(largest(-100, 100)==100,   "largest integers");
    assert(largest(-1.5, 5.2)==5.2,   "largest floats");

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
    
    variable Integer vi;
    variable Integer vj;
    vi:=vj:=2;
    assert(vi==2&&vj==2, "multi assign");
    vi+=1;
    vj*=2;
    
    class Inner() {
        shared variable Integer vi:=0;
        shared variable Integer vj:=0;
    }
    value inner = Inner();
    inner.vi:=inner.vj:=2;
    inner.vi+=1;
    inner.vj*=2;
    
    assert(1_000==1k, "integer literal");
    assert(1_000_000==1M, "integer literal");
    assert(1_000_000_000==1G, "integer literal");
    assert(1_000_000_000_000==1T, "integer literal");
    assert(1_000_000_000_000_000==1P, "integer literal");
    
    assert(1_000.0==1.0k && 1.0k==1.0e+3, "float literal");
    assert(1_000_000.0==1.0M && 1.0M==1.0e6, "float literal");
    assert(1_000_000_000.0==1.0G && 1.0G==1.0e+9, "float literal");
    assert(1_000_000_000_000.0==1.0T && 1.0T==1.0e+12, "float literal");
    assert(1_000_000_000_000_000.0==1.0P && 1.0P==1.0e+15, "float literal");

    assert(0.001==1.0m && 1.0m==1.0e-3, "float literal");
    assert(0.000_001==1.0u && 1.0u==1.0e-6, "float literal");
    assert(0.000_000_001==1.0n && 1.0n==1.0e-9, "float literal");
    assert(0.000_000_000_001==1.0p && 1.0p==1.0e-12, "float literal");
    assert(0.000_000_000_000_001==1.0f && 1.0f==1.0e-15, "float literal");
    
    // infinity and .undefined
    assert(1.0/0.0==infinity, "infinity == infinity");
    assert(1.0!=infinity, "1 != infinity");
    assert(-1.0/0.0==-infinity, "-infinity == -infinity");
    assert(1.0!=-infinity, "1 != -infinity");
    
    assert((0.0/0.0).undefined, "NaN undefined");
    assert(!(1.0).undefined, "1 not undefined");
    assert(!infinity.undefined, "infinity not undefined");
    
    assert(1.0.finite, "1 finite");
    assert(!infinity.finite, "infinity is not finite");
    assert(!(-infinity).finite, "-infinity is not finite");
    assert(!(0.0/0.0).finite, "NaN not finite");
    
    assert(!1.0.infinite, "1 not infinite");
    assert(infinity.infinite, "infinity is infinite");
    assert((-infinity).infinite, "-infinity is infinite");
    assert(!(0.0/0.0).infinite, "NaN not infinite");
    
    // parseInteger()
    assert(parseInteger("-123")?0==-123, "parse integer");
    
    assert(1_000==parseInteger("1_000")?"", "parseInteger(1_000)");
	assert(1000==parseInteger("1000")?"", "parseInteger(1000)");
	assert(1k==parseInteger("1k")?"", "parseInteger(1k)");
	assert(+1_000==parseInteger("+1_000")?"", "parseInteger(+1_000)");
	assert(+1000==parseInteger("+1000")?"", "parseInteger(+1000)");
	assert(+1k==parseInteger("+1k")?"", "parseInteger(+1k)");
	assert(-1_000==parseInteger("-1_000")?"", "parseInteger(-1_000)");
	assert(-1000==parseInteger("-1000")?"", "parseInteger(-1000)");
	assert(-1k==parseInteger("-1k")?"", "parseInteger(-1k)");
	
	assert(0==parseInteger("0")?"", "parseInteger(0)");
	assert(00==parseInteger("00")?"", "parseInteger(00)");
	assert(0_000==parseInteger("0_000")?"", "parseInteger(0_000)");
	assert(-00==parseInteger("-00")?"", "parseInteger(-00)");
	assert(+00==parseInteger("+00")?"", "parseInteger(+00)");
	assert(0k==parseInteger("0k")?"", "parseInteger(0k)");
	
	assert(1==parseInteger("1")?"", "parseInteger(1)");
	assert(01==parseInteger("01")?"", "parseInteger(01)");
	assert(0_001==parseInteger("0_001")?"", "parseInteger(0_001)");
	assert(+1==parseInteger("+1")?"", "parseInteger(+1)");
	assert(+01==parseInteger("+01")?"", "parseInteger(+01)");
	assert(+0_001==parseInteger("+0_001")?"", "parseInteger(+0_001)");
	
	assert(-1==parseInteger("-1")?"", "parseInteger(-1)");
	assert(-01==parseInteger("-01")?"", "parseInteger(-01)");
	assert(-0_001==parseInteger("-0_001")?"", "parseInteger(-0_001)");
	
	assert(1k==parseInteger("1k")?"", "parseInteger(1k)");
	assert(1M==parseInteger("1M")?"", "parseInteger(1M)");
	assert(1G==parseInteger("1G")?"", "parseInteger(1G)");
	assert(1T==parseInteger("1T")?"", "parseInteger(1T)");
	assert(1P==parseInteger("1P")?"", "parseInteger(1P)");
	assert(-1k==parseInteger("-1k")?"", "parseInteger(-1k)");
	assert(-1M==parseInteger("-1M")?"", "parseInteger(-1M)");
	assert(-1G==parseInteger("-1G")?"", "parseInteger(-1G)");
	assert(-1T==parseInteger("-1T")?"", "parseInteger(-1T)");
	assert(-1P==parseInteger("-1P")?"", "parseInteger(-1P)");
	
	assert(9223372036854775807==parseInteger("9223372036854775807")?"", "parseInteger(9223372036854775807)");
	assert(9_223_372_036_854_775_807==parseInteger("9_223_372_036_854_775_807")?"", "parseInteger(9_223_372_036_854_775_807)");
	assert(-9223372036854775808==parseInteger("-9223372036854775808")?"", "parseInteger(-9223372036854775808)");
	assert(-9_223_372_036_854_775_808==parseInteger("-9_223_372_036_854_775_808")?"", "parseInteger(-9_223_372_036_854_775_808)");
	
	assert(!exists parseInteger(""), "parseInteger()");
	assert(!exists parseInteger("+"), "parseInteger(+)");
	assert(!exists parseInteger("-"), "parseInteger(-)");
	assert(!exists parseInteger("foo"), "parseInteger(foo)");
	assert(!exists parseInteger(" 0"), "parseInteger( 0)");
	assert(!exists parseInteger("0 "), "parseInteger(0 )");
	assert(!exists parseInteger("0+0"), "parseInteger(0+0)");
	assert(!exists parseInteger("0-0"), "parseInteger(0-0)");
	assert(!exists parseInteger("0+"), "parseInteger(0+)");
	assert(!exists parseInteger("0-"), "parseInteger(0-)");
	assert(!exists parseInteger("k"), "parseInteger(k)");
	assert(!exists parseInteger("k1"), "parseInteger(k1)");
	assert(!exists parseInteger("+k"), "parseInteger(+k)");
	assert(!exists parseInteger("-k"), "parseInteger(-k)");
	assert(!exists parseInteger("1kk"), "parseInteger(1kk)");
	assert(!exists parseInteger("0_"), "parseInteger(0_)");
	assert(!exists parseInteger("_0"), "parseInteger(_0)");
	assert(!exists parseInteger("0_0"), "parseInteger(0_0)");
	assert(!exists parseInteger("0_00"), "parseInteger(0_00)");
	assert(!exists parseInteger("0_0000"), "parseInteger(0_0000)");
	assert(!exists parseInteger("0_000_0"), "parseInteger(0_000_0)");
	assert(!exists parseInteger("0000_000"), "parseInteger(0000_000)");
	assert(!exists parseInteger("9223372036854775808"), "parseInteger(9223372036854775808)");
	assert(!exists parseInteger("-9223372036854775809"), "parseInteger(-9223372036854775809)");
	
	// parseFloat
	assert(parseFloat("12.34e3")?0.0==12.34e3, "parseFloat(12.34e3)");
	assert(parseFloat("12.340e3")?0.0==12.34e3, "parseFloat(12.340e3)");
	assert(parseFloat("123.4e2")?0.0==12.34e3, "parseFloat(123.4e2)");
	assert(parseFloat("1234e1")?0.0==12.34e3, "parseFloat(1234e1)");
	assert(parseFloat("1234e+1")?0.0==12.34e3, "parseFloat(1234e+1)");
	assert(parseFloat("12340e0")?0.0==12.34e3, "parseFloat(12340e0)");
	assert(parseFloat("12340")?0.0==12.34e3, "parseFloat(12340)");
	assert(parseFloat("12340.0")?0.0==12.34e3, "parseFloat(12340.0)");
	assert(parseFloat("123400e-1")?0.0==12.34e3, "parseFloat(123400e-1)");
	
	assert(parseFloat("012340")?0.0==12.34e3, "parseFloat(012340)");
	assert(parseFloat("+12340")?0.0==12.34e3, "parseFloat(+12340)");
	
	assert(parseFloat("-12340")?0.0==-12.34e3, "parseFloat(-12340)");
}
