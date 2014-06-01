@test
shared void numbers() {
    check(1==1, "natural equals");
    check(1!=2, "natural not equals");
    check(+1==+1, "integer equals");
    check(+1!=+2, "integer not equals");
    check(1.0==1.0, "float equals");
    check(0.0==-0.0 && 0.0.hash==(-0.0).hash, "float equals consistent with hash");
    check(1.0!=2.0, "float not equals");
    check(1==+1, "natural equals integer");
    check(1==1.0, "natural equals float");
    check(+1==1.0, "integer equals float");
    check(+1==1, "natural equals integer");
    check(1.0==1, "natural equals float");
    check(1.0==+1, "integer equals float");
    check(1<2, "natural comparison");
    check(-1<+2, "integer comparison");
    check(-0.5<0.0, "float comparison");
    check(1+1==2, "natural addition");
    check(1-1==+0, "natural subtraction");
    check(2*2==4, "natural multiplication");
    check(2^3==8, "natural exponentiation");
    check(+1 + -1==+0, "integer addition");
    check(+1 - -1==+2, "integer subtraction");
    check(-2*+2==-4, "integer multiplication");
    check(-2^(+3)==-8, "integer exponentiation");
    check(1.0+0.5==1.5, "float addition");
    check(1.5-0.5==1.0, "float subtraction");
    check(2.0*2.0==4.0, "float multiplication");
    check(2.0^3.0==8.0, "float exponentiation");
    check(2*2.5==5.0, "natural times float");
    check(2.5*3==7.5, "natural times float");
    check(-1*3==-3, "natural times integer");
    check(2*-3==-6, "natural times integer");
    check(-1*1.5==-1.5, "integer times float");
    check(-1.5*+2==-3.0, "integer times float");
    check(1.positive, "1.positive");
    check((-1).negative, "-1.negative");
    
    Object? obj(Object? x) { return x; }
    check(obj(1+1)  is Integer, "natural addition");
    check(obj(1-2)  is Integer, "natural subtraction");
    check(obj(+1+1) is Integer, "integer addition");
    check(obj(+1-2) is Integer, "integer subtraction");
    
    check(1.negated==-1, "natural negative");
    check(-1.negated==+1, "integer negative");
    check(1.0.negated==-1.0, "float negative");
    
    check(12.string=="12", "natural string 12");
    check((-12).string=="-12", "integer string -12");
    check((-5.5).string=="-5.5", "float string -5.5");
    check((1.0).string in {"1", "1.0"}, "float string 1.0");
    
    check(1.unit, "natural unit");
    check(!2.unit, "natural unit");
    check(0.zero, "natural zero");
    check(!1.zero, "natural zero");
    check(2.successor==3, "natural successor");
    check(2.predecessor==1, "natural predecessor");
    check((+1).unit, "integer unit");
    check(!(-1).unit, "integer unit");
    check((+0).zero, "integer zero");
    check(!(+1).zero, "integer zero");
    check((-2).successor==-1, "integer successor");
    check((-2).predecessor==-3, "integer predecessor");
    
    check(2.fractionalPart==0, "natural fractional");
    check((-1).fractionalPart==0, "integer fractional");
    check(1.5.fractionalPart==0.5, "float fractional");
    check(2.wholePart==2, "natural fractional");
    check((-1).wholePart==-1, "integer fractional");
    check(1.5.wholePart==1.0, "float fractional");
    
    check((+2).sign==+1, "integer sign");
    check((-3).sign==-1, "integer sign");
    check(2.0.sign==+1, "integer sign");
    check((-3.0).sign==-1, "integer sign");
    
    function add<T>(T x, T y) 
            given T satisfies Numeric<T> {
        return x.plus(y);
    }
    function exp<T>(T x, T y) 
            given T satisfies Exponentiable<T,T> {
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
        
    check(add(1,2)==3, "add(1,2)==3");
    check(add(-1,+2)==+1, "add(-1,+2)==+1");
    check(add(1.5,-2.5)==-1.0, "add(1.5,-2.5)==-1.0");
    
    check(exp(2,2)==4, "exp(2,2)==4");
    check(exp(1,2)==1, "exp(1,2)==1");
    check(exp(0,2)==0, "exp(0,2)==0");
    check(exp(-1,2)==1, "exp(-1,2)==1");
    check(exp(-2,2)==4, "exp(-2,2)==4");
    
    check(exp(2,1)==2, "exp(2,1)==2");
    check(exp(1,1)==1, "exp(1,1)==1");
    check(exp(0,1)==0, "exp(0,1)==0");
    check(exp(-1,1)==-1, "exp(-1,1)==-1");
    check(exp(-2,1)==-2, "exp(-2,1)==-2");
    
    check(exp(2,0)==1, "exp(2,0)==2");
    check(exp(1,0)==1, "exp(1,0)==1");
    check(exp(0,0)==1, "exp(0,0)==0");
    check(exp(-1,0)==1, "exp(-1,0)==-1");
    check(exp(-2,0)==1, "exp(-2,0)==-2");
    try { 
        exp(2,-1);
        fail("exp(2,-1) should throw");
    } catch (AssertionError e) {}
    check(exp(1,-1)==1, "exp(1,-1)==1");
    try {
        exp(0,-1);
        fail("exp(0,-1) should throw");
    } catch (AssertionError e) {}
    check(exp(-1,-1)==-1, "exp(-1,-1)==-1");
    try {
        exp(-2,-1);
        fail("exp(-2,-1) should throw");
    } catch (AssertionError e) {}
    
    try {
        exp(2,-2);
        fail("exp(2,-2)==2 should throw");
    } catch (AssertionError e) {}
    check(exp(1,-2)==1, "exp(1,-2)==1");
    try {
        exp(0,-2);
        fail("exp(2,-1) should throw");
    } catch (AssertionError e) {}
    check(exp(-1,-2)==1, "exp(-1,-2)==1");
    try {
        exp(-2,-2);
        fail("exp(-2,-2) should throw");
    } catch (AssertionError e) {}
    
    Integer twoToPowerTen = 2*2*2*2*2*2*2*2*2*2;
    check(exp(2,10)==twoToPowerTen, "exp(2,10)==twoToPowerTen");
    check(exp(2,20)==twoToPowerTen*twoToPowerTen, "exp(2,30)==twoToPowerTen*twoToPowerTen");
    check(exp(2,30)==twoToPowerTen*twoToPowerTen*twoToPowerTen, "exp(2,30)==twoToPowerTen*twoToPowerTen*twoToPowerTen");
    
    check(exp(2.0,2.0)==4.0, "exp(2.0,2.0)==4.0");
    
    check(addIntegers(2, 4)==6, "addIntegers(2, 4)==6");
    check(addIntegers(-2, -4)==-6, "addIntegers(-2, -4)==-6");
    check(addFloats(-1.0, 1.0)==0.0, "addFloats(-1.0, 1.0)==0.0");
    
    check(multiplyIntegerByFloat(3, 1.5)==4.5, "multiplyIntegerByFloat(3, 1.5)==4.5");
    check(multiplyFloatByInteger(1.5, -1)==-1.5, "multiplyFloatByInteger(1.5, -1)==-1.5");
    check(multiplyIntegerByInteger(1, -1)==-1, "multiplyIntegerByInteger(1, -1)==-1");
    
    check(1.hash==(3-1)/2.hash, "natural hash");
    check(1.hash!=(-1).hash, "natural hash inverted not same");
    check((+0).hash==(-1+(+1))*+100.hash, "integer hash");
    check((2.2*2.2*2.2).hash==(2.2^3.0).hash, "float hash");
    
    check(1.6.integer==1, "1.6.integer is `` 1.6.integer `` instead of 1");
    check(1.1.integer==1, "1.1.integer is `` 1.1.integer `` instead of 1");
    check((-1.6).integer==-1, "(-1.6).integer is `` (-1.6).integer `` instead of -2");
    check((-1.1).integer==-1, "(-1.1).integer is `` (-1.1).integer `` instead of -1");
    check(2.float==2.0, "integer float");
    check((-3).float==-3.0, "negative integer float");
    
    check(1.plus { other=2; }.equals { that=3; }, "natural named args");
                
    variable value i=0;
    for (x in 1..10) {
        i=i+1;
        check(x>=1&&x<=10, "natural range [1]");
    }
    check(i==10, "natural range [2]");

    i=0;
    for (x in -5..+5) {
        i=i+1;
        check(x>=-5&&x<=+5, "integer range");
    }
    check(i==11, "integer range");
    
    check(min([1, 5])==1, "min naturals");
    check(min([-1, +5])==-1, "min integers");
    check(min([-1.5, 5.2])==-1.5, "min floats");
    check(max([1, 5])==5, "max naturals");
    check(max([-1, +5])==+5, "max integers");
    check(max([-1.5, 5.2])==5.2, "max floats");
    //eZL
    check(max([ 2, 4, 6, 8, 7, 250, 5, 3, 1 ])==250, "max");
    check(min([ 200, 400, 600, 800, 700, 500, 300, 150 ])==150, "min");

    //eZL
    check(smallest(1,2)==1,          "smallest naturals");
    check(smallest(-100, 100)==-100, "smallest integers");
    check(smallest(-1.5, 5.2)==-1.5, "smallest floats");
    check(largest(1,2)==2,           "largest naturals");
    check(largest(-100, 100)==100,   "largest integers");
    check(largest(-1.5, 5.2)==5.2,   "largest floats");

    variable value count = 0;
    count++;
    ++count;
    count+=2;
    count*=3;
    check(count==12, "natural increment");
    check(--count==11, "natural decrement");
    check(count--==11, "natural decrement");
    
    variable value intcount = +0;
    intcount++;
    ++intcount;
    intcount+=2;
    intcount*=-3;
    intcount--;
    check(intcount==-13, "integer increment");
    check(--intcount==-14, "integer decrement");
    check(intcount--==-14, "integer decrement");
    
    variable value floatcount = 0.0;
    floatcount+=2.0;
    floatcount*=3.0;
    floatcount/=1.5;
    check(floatcount==4.0, "float increment");
    check((floatcount*=2.5)==10.0, "float scale");
    check((floatcount/=2.0)==5.0, "float scale");
    check((floatcount=-2.0)==-2.0, "float assign");
    
    variable Integer vi;
    variable Integer vj;
    vi=vj=2;
    check(vi==2&&vj==2, "multi assign");
    vi+=1;
    vj*=2;
    
    class Inner() {
        shared variable Integer vi=0;
        shared variable Integer vj=0;
    }
    value inner = Inner();
    inner.vi=inner.vj=2;
    inner.vi+=1;
    inner.vj*=2;
    
    check(1_000==1k, "integer literal 1");
    check(1_000_000==1M, "integer literal 2");
    check(1_000_000_000==1G, "integer literal 3");
    check(1_000_000_000_000==1T, "integer literal 4");
    check(1_000_000_000_000_000==1P, "integer literal 5");
    
    check(1_000.0==1.0k && 1.0k==1.0e+3, "float literal 6");
    check(1_000_000.0==1.0M && 1.0M==1.0e6, "float literal 7");
    check(1_000_000_000.0==1.0G && 1.0G==1.0e+9, "float literal 8");
    check(1_000_000_000_000.0==1.0T && 1.0T==1.0e+12, "float literal 9");
    check(1_000_000_000_000_000.0==1.0P && 1.0P==1.0e+15, "float literal 10");

    check(0.001==1.0m && 1.0m==1.0e-3, "float literal 1");
    check(0.000_001==1.0u && 1.0u==1.0e-6, "float literal 2");
    check(0.000_000_001==1.0n && 1.0n==1.0e-9, "float literal 3");
    check(0.000_000_000_001==1.0p && 1.0p==1.0e-12, "float literal 4");
    check(0.000_000_000_000_001==1.0f && 1.0f==1.0e-15, "float literal 5");
    
    // infinity and .undefined
    check(1.0/0.0==infinity, "infinity == infinity");
    check(1.0!=infinity, "1 != infinity");
    check(-1.0/0.0==-infinity, "-infinity == -infinity");
    check(1.0!=-infinity, "1 != -infinity");
    
    check((0.0/0.0).undefined, "NaN undefined");
    check(!(1.0).undefined, "1 not undefined");
    check(!infinity.undefined, "infinity not undefined");
    
    check(1.0.finite, "1 finite");
    check(!infinity.finite, "infinity is not finite");
    check(!(-infinity).finite, "-infinity is not finite");
    check(!(0.0/0.0).finite, "NaN not finite");
    
    check(!1.0.infinite, "1 not infinite");
    check(infinity.infinite, "infinity is infinite");
    check((-infinity).infinite, "-infinity is infinite");
    check(!(0.0/0.0).infinite, "NaN not infinite");

    //ArithmeticException
    try {
        print(0/0);
        fail("0/0 should throw");
    } catch (Exception ex) {
        check(true, "ArithmeticException");
    }
    checkParseInteger();
    checkFormatInteger();
    
    // parseFloat
    check((parseFloat("12.34e3") else 0.0)==12.34e3, "parseFloat(12.34e3)");
    check((parseFloat("12.340e3") else 0.0)==12.34e3, "parseFloat(12.340e3)");
    check((parseFloat("123.4e2") else 0.0)==12.34e3, "parseFloat(123.4e2)");
    check((parseFloat("1234.0e1") else 0.0)==12.34e3, "parseFloat(1234.0e1)");
    check((parseFloat("1234.0e+1") else 0.0)==12.34e3, "parseFloat(1234.0e+1)");
    check((parseFloat("12340.0e0") else 0.0)==12.34e3, "parseFloat(12340.0e0)");
    check((parseFloat("12340.0") else 0.0)==12.34e3, "parseFloat(12340.0)");
    check((parseFloat("12340.0") else 0.0)==12.34e3, "parseFloat(12340.0)");
    check((parseFloat("123400.0e-1") else 0.0)==12.34e3, "parseFloat(123400.0e-1)");
    
    check((parseFloat("012340.0") else 0.0)==12.34e3, "parseFloat(012340.0)");
    check((parseFloat("+12340.0") else 0.0)==12.34e3, "parseFloat(+12340.0)");
    
    check((parseFloat("-12340.0") else 0.0)==-12.34e3, "parseFloat(-12340.0)");
    
    check((parseFloat("1.234_0") else 0.0)==1.234_0, "parseFloat(1.234_0)");
    check((parseFloat("0.000_1") else 0.0)==0.000_1, "parseFloat(0.000_1)");
    check((parseFloat("1.234_56") else 0.0)==1.234_56, "parseFloat(1.234_56)");

    //type safety
    check(obj(1+1)    is Integer, "int+int Integer");
    check(obj(1+1.0)  is Float  , "int+float Float");
    check(obj(1-1)    is Integer, "int-int Integer");
    check(obj(1-1.0)  is Float  , "int-float Float");
    check(obj(1*1)    is Integer, "int*int Integer");
    check(obj(1*1.0)  is Float  , "int*float Float");
    check(obj(1/1)    is Integer, "int/int Integer");
    check(obj(1/1.0)  is Float  , "int/float Float");
    check(obj(2^2)   is Integer, "2^2 Integer");
    //check(is Float   obj(2^2.0), "2^2.0 Float");
    check(obj(2.0^2) is Float  , "2.0^2 Float");
    
    check(0.0.strictlyPositive, "positive zero strictly positive");
    check(!(-0.0).strictlyPositive, "negative zero not strictly positive");
    check(1.0.strictlyPositive, "positive one strictly positive");
    check(!(-1.0).strictlyPositive, "negative one not strictly positive");

    //number-related functions
    check(sum([1,2,3])==6, "sum()");
    check(max([1,3,2])==3, "max()");
    check(min([3,1,2])==1, "min()");

    // Bitwise operators, we need to test their boxed versions as well
    Binary<Integer> box(Integer i){
        return i;
    }
    check(#ff == 255, "xff value");
    check($11111111 == 255, "b11111111 value");
    //TODO this is still unsupported
    check(#ffff_ffff == 4294967295, "ffff_ffff");
    check($1111_1111 == 255, "1111_1111");

    if (runtime.integerSize== 64) {
        check(#ff.not == #ffffffffffffff00, "~xff == xffffffffffffff00");
        check(box(#ff).not == #ffffffffffffff00, "~xff == xffffffffffffff00 boxed");
        check(0.not == #ffffffffffffffff, "~0 == xffffffffffffffff");
        check(box(0).not == #ffffffffffffffff, "~0 == xffffffffffffffff boxed");
        check($1010101010101010101010101010101010101010101010101010101010101010.not == $0101010101010101010101010101010101010101010101010101010101010101, 
            "~b1010101010101010101010101010101010101010101010101010101010101010 == b0101010101010101010101010101010101010101010101010101010101010101");
        check(box($1010101010101010101010101010101010101010101010101010101010101010).not == $0101010101010101010101010101010101010101010101010101010101010101, 
            "~b1010101010101010101010101010101010101010101010101010101010101010 == b0101010101010101010101010101010101010101010101010101010101010101 boxed");
        check($1010101010101010101010101010101010101010101010101010101010101010.rightArithmeticShift(1) 
                == $1101010101010101010101010101010101010101010101010101010101010101, 
                "b1010101010101010101010101010101010101010101010101010101010101010 >>> 1 == b1101010101010101010101010101010101010101010101010101010101010101");
        check(box($1010101010101010101010101010101010101010101010101010101010101010).rightArithmeticShift(1) 
                == $1101010101010101010101010101010101010101010101010101010101010101, 
                "b1010101010101010101010101010101010101010101010101010101010101010 >>> 1 == b1101010101010101010101010101010101010101010101010101010101010101 boxed");
    } else if (runtime.integerSize == 53) {
        check(#ff.not == -256, "~xff == -256");
        check(box(#ff).not == -256, "~xff == -256 boxed");
        check(0.not == -1, "~0 == -1");
        check(box(0).not == -1, "~0 == -1 boxed");
        check($10101010101010101010101010101010101010101010101010101.not == -1431655766,
            "~b10101010101010101010101010101010101010101010101010101 == b10101010101010101010101010101010");
        check(box($10101010101010101010101010101010101010101010101010101).not == -1431655766,
            "~b10101010101010101010101010101010101010101010101010101 == b10101010101010101010101010101010 boxed");
        check($10101010101010101010101010101010101010101010101010101.rightArithmeticShift(1) 
                == $00101010101010101010101010101010,
                "b10101010101010101010101010101010101010101010101010101 >>> 1 == b00101010101010101010101010101010");
        check(box($10101010101010101010101010101010101010101010101010101).rightArithmeticShift(1) 
                == $00101010101010101010101010101010,
                "b10101010101010101010101010101010101010101010101010101 >>> 1 == b00101010101010101010101010101010 boxed");
    } else {
        fail("UNKNOWN INTEGER SIZE `` runtime.integerSize `` - please add number tests for this platform");
    }
    check(1.leftLogicalShift(2) == $100, "1<<2 == b100");
    check(box(1).leftLogicalShift(2) == $100, "1<<2 == b100 boxed");
    
    check($1100.rightLogicalShift(2) == $11, "b1100>>2 == b11");
    check(box($1100).rightLogicalShift(2) == $11, "b1100>>2 == b11 boxed");
    check($1010101010101010.rightLogicalShift(1) == $0101010101010101, "b1010101010101010>>1 == b0101010101010101");
    check(box($1010101010101010).rightLogicalShift(1) == $0101010101010101, "b1010101010101010>>1 == b0101010101010101 boxed");
    
    check($1100.rightArithmeticShift(2) == $11, "b1100>>>2 == b11");
    check(box($1100).rightArithmeticShift(2) == $11, "b1100>>>2 == b11 boxed");
    
    check($1001.or($0101) == $1101, "b1001&b0101 == b1101");
    check(box($1001).or($0101) == $1101, "b1001&b0101 == b1101 boxed");
    check($1001.xor($0101) == $1100, "b1001^b0101 == b1100");
    check(box($1001).xor($0101) == $1100, "b1001^b0101 == b1100 boxed");
    check($1001.and($1000) == $1000, "b1001&b1000 == b1000");
    check(box($1001).and($1000) == $1000, "b1001&b1000 == b1000 boxed");

    check(0.set(2) == $100, "0.set(2) == b100");
    check(0.set(2) == $100, "0.set(2) == b100");
    check(0.set(2, true) == $100, "0.set(2, true) == b100");
    check($110.set(2, false) == $010, "b110.set(2, false) == b010");
    check($110.clear(2) == $010, "b110.clear(2) == b010");
    check(0.flip(2) == $100, "0.flip(2) == b100");
    check($100.flip(2) == 0, "b100.flip(2) == 0");
    
    check(!($10).get(0), "b10.get(0) == false");
    check($10.get(1), "b10.get(1) == true");
    check(!($10).get(2), "b10.get(2) == false");
    
    Integer allOnes =     $1111111111111111111111111111111111111111111111111111111111111111;
    Integer leftmostOne = $1000000000000000000000000000000000000000000000000000000000000000;
    Integer rightmostOne =$0000000000000000000000000000000000000000000000000000000000000001;
    Integer allZeros =    $0000000000000000000000000000000000000000000000000000000000000000;
    value oobIndices = (-2..-1).chain(runtime.integerAddressableSize..runtime.integerAddressableSize+2);
    // by doing the tests using values of the real type and on type parameters
    // we test both jvm optimized and non-optimized paths
    void binaryOob<T>(T ones, T zeros, T leftmost, T rightmost)
        given T satisfies Binary<T> {
        for (oobIndex in oobIndices) {
            value ibIndex = ((oobIndex % runtime.integerAddressableSize) + runtime.integerAddressableSize) % runtime.integerAddressableSize;
            for (lhs in [ones, zeros, leftmost, rightmost]) {
                // Theses are weirdly circular
                check(lhs.leftLogicalShift(oobIndex) == lhs.leftLogicalShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightLogicalShift(oobIndex) == lhs.rightLogicalShift(ibIndex), "``lhs``.rightLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightArithmeticShift(oobIndex) == lhs.rightArithmeticShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.rightArithmeticShift(``ibIndex``)");
                // These should be noops
                check(lhs.clear(oobIndex) == lhs,      "``lhs``.clear(``oobIndex``) == ``lhs``");
                check(lhs.flip(oobIndex) == lhs,       "``lhs``.flip(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex) == lhs,        "``lhs``.set(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex, false) == lhs, "``lhs``.set(``oobIndex``, false) == ``lhs``");
                // This should be false
                check(lhs.get(oobIndex) == false,      "``lhs``.get(``oobIndex``) == false");
            }
            for (lhs in [allOnes, allZeros, leftmostOne, rightmostOne]) {
                // Theses are weirdly circular
                check(lhs.leftLogicalShift(oobIndex) == lhs.leftLogicalShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightLogicalShift(oobIndex) == lhs.rightLogicalShift(ibIndex), "``lhs``.rightLogicalShift(``oobIndex``) == ``lhs``.leftLogicalShift(``ibIndex``)");
                check(lhs.rightArithmeticShift(oobIndex) == lhs.rightArithmeticShift(ibIndex), "``lhs``.leftLogicalShift(``oobIndex``) == ``lhs``.rightArithmeticShift(``ibIndex``)");
                // These should be noops
                check(lhs.clear(oobIndex) == lhs,      "``lhs``.clear(``oobIndex``) == ``lhs``");
                check(lhs.flip(oobIndex) == lhs,       "``lhs``.flip(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex) == lhs,        "``lhs``.set(``oobIndex``) == ``lhs``");
                check(lhs.set(oobIndex, false) == lhs, "``lhs``.set(``oobIndex``, false) == ``lhs``");
                // This should be false
                check(lhs.get(oobIndex) == false,      "``lhs``.get(``oobIndex``) == false");
            }
        }
    }
    binaryOob(allOnes, allZeros, leftmostOne, rightmostOne);
    
}

void checkParseInteger() {
    check((parseInteger("-123") else 0)==-123, "parseInteger(-123)");
    
    check(1_000==(parseInteger("1_000") else ""), "parseInteger(1_000)");
    check(1000==(parseInteger("1000") else ""), "parseInteger(1000)");
    check(1k==(parseInteger("1k") else ""), "parseInteger(1k)");
    check(+1_000==(parseInteger("+1_000") else ""), "parseInteger(+1_000)");
    check(+1000==(parseInteger("+1000") else ""), "parseInteger(+1000)");
    check(+1k==(parseInteger("+1k") else ""), "parseInteger(+1k)");
    check(-1_000==(parseInteger("-1_000") else ""), "parseInteger(-1_000)");
    check(-1000==(parseInteger("-1000") else ""), "parseInteger(-1000)");
    check(-1k==(parseInteger("-1k") else ""), "parseInteger(-1k)");
    
    check(0==(parseInteger("0") else ""), "parseInteger(0)");
    check(00==(parseInteger("00") else ""), "parseInteger(00)");
    check(0_000==(parseInteger("0_000") else ""), "parseInteger(0_000)");
    check(-00==(parseInteger("-00") else ""), "parseInteger(-00)");
    check(+00==(parseInteger("+00") else ""), "parseInteger(+00)");
    check(0k==(parseInteger("0k") else ""), "parseInteger(0k)");
    
    check(1==(parseInteger("1") else ""), "parseInteger(1)");
    check(01==(parseInteger("01") else ""), "parseInteger(01)");
    check(0_001==(parseInteger("0_001") else ""), "parseInteger(0_001)");
    check(+1==(parseInteger("+1") else ""), "parseInteger(+1)");
    check(+01==(parseInteger("+01") else ""), "parseInteger(+01)");
    check(+0_001==(parseInteger("+0_001") else ""), "parseInteger(+0_001)");
    
    check(-1==(parseInteger("-1") else ""), "parseInteger(-1)");
    check(-01==(parseInteger("-01") else ""), "parseInteger(-01)");
    check(-0_001==(parseInteger("-0_001") else ""), "parseInteger(-0_001)");
    
    check(1k==(parseInteger("1k") else ""), "parseInteger(1k)");
    check(1M==(parseInteger("1M") else ""), "parseInteger(1M)");
    check(1G==(parseInteger("1G") else ""), "parseInteger(1G)");
    check(1T==(parseInteger("1T") else ""), "parseInteger(1T)");
    check(1P==(parseInteger("1P") else ""), "parseInteger(1P)");
    check(-1k==(parseInteger("-1k") else ""), "parseInteger(-1k)");
    check(-1M==(parseInteger("-1M") else ""), "parseInteger(-1M)");
    check(-1G==(parseInteger("-1G") else ""), "parseInteger(-1G)");
    check(-1T==(parseInteger("-1T") else ""), "parseInteger(-1T)");
    check(-1P==(parseInteger("-1P") else ""), "parseInteger(-1P)");
    check(!parseInteger("1k", 2) exists, "!parseInteger(1k, 2) exists");
    
    check($1111111 == (parseInteger("1111111", 2) else ""), "parseInteger(1111111, 2)");
    check($1111111 == (parseInteger("01111111", 2) else ""), "parseInteger(01111111, 2)");
    check($10000000 == (parseInteger("10000000", 2) else ""), "parseInteger(10000000, 2)");
    check(-($110000) == (parseInteger("-110000", 2) else ""), "parseInteger(-110000, 2)");
    check($1010_0011_1111 == (parseInteger("1010_0011_1111", 2) else ""), "parseInteger(1010_0011_1111, 2)");
    check($11_0110_0100 == (parseInteger("11_0110_0100", 2) else ""), "parseInteger(11_0110_0100, 2)");
    check(!parseInteger("11_01_00", 2) exists, "!parseInteger(11_01_00, 2) exists");
    
    check(1193046 == (parseInteger("123456", 16) else ""), "parseInteger(123456, 16)");
    check(255 == (parseInteger("ff", 16) else ""), "parseInteger(ff, 16)");
    check(!parseInteger("fk", 16) exists, "parseInteger(fk, 16)");
    check(#f_ffff == (parseInteger("f_ffff", 16) else ""), "parseInteger(f_ffff, 16)");
    check(#ff_ffff == (parseInteger("ff_ffff", 16) else ""), "parseInteger(ff_ffff, 16)");
    check(#fff_ffff == (parseInteger("fff_ffff", 16) else ""), "parseInteger(fff_ffff, 16)");
    check(#ffff_ffff == (parseInteger("ffff_ffff", 16) else ""), "parseInteger(ffff_ffff, 16)");
    check(#ffff_ffff_ffff == (parseInteger("ffff_ffff_ffff", 16) else ""), "parseInteger(ffff_ffff_ffff, 16)");
    check(#fff_ffff_ffff == (parseInteger("fff_ffff_ffff", 16) else ""), "parseInteger(ffff_ffff_ffff, 16)");
    check(#ff_ffff_ffff == (parseInteger("ff_ffff_ffff", 16) else ""), "parseInteger(ff_ffff_ffff, 16)");
    check(#f_ffff_ffff == (parseInteger("f_ffff_ffff", 16) else ""), "parseInteger(f_ffff_ffff, 16)");
    check(#ff_ff_ff_ff_ff_ff == (parseInteger("ff_ff_ff_ff_ff_ff", 16) else ""), "parseInteger(ff_ff_ff_ff_ff_ff, 16)");
    check(#f_ff_ff_ff_ff_ff == (parseInteger("f_ff_ff_ff_ff_ff", 16) else ""), "parseInteger(f_ff_ff_ff_ff_ff, 16)");
    check(!parseInteger("ff_ff_ff_ffff", 16) exists, "!parseInteger(ff_ff_ff_ffff, 16) exists");
    check(!parseInteger("ffff_ff_ff_ff", 16) exists, "!parseInteger(ffff_ff_ff_ff, 16) exists");
    check(255 == (parseInteger("FF", 16) else ""), "parseInteger(FF, 16)");
    check(!parseInteger("FK", 16) exists, "parseInteger(FK, 16)");
    check(#F_FFFF == (parseInteger("F_FFFF", 16) else ""), "parseInteger(F_FFFF, 16)");
    check(#FF_FFFF == (parseInteger("FF_FFFF", 16) else ""), "parseInteger(FF_FFFF, 16)");
    check(#FFF_FFFF == (parseInteger("FFF_FFFF", 16) else ""), "parseInteger(FFF_FFFF, 16)");
    check(#FFFF_FFFF == (parseInteger("FFFF_FFFF", 16) else ""), "parseInteger(FFFF_FFFF, 16)");
    check(#FFFF_FFFF_FFFF == (parseInteger("FFFF_FFFF_FFFF", 16) else ""), "parseInteger(FFFF_FFFF_FFFF, 16)");
    check(#FFF_FFFF_FFFF == (parseInteger("FFF_FFFF_FFFF", 16) else ""), "parseInteger(FFFF_FFFF_FFFF, 16)");
    check(#FF_FFFF_FFFF == (parseInteger("FF_FFFF_FFFF", 16) else ""), "parseInteger(FF_FFFF_FFFF, 16)");
    check(#F_FFFF_FFFF == (parseInteger("F_FFFF_FFFF", 16) else ""), "parseInteger(F_FFFF_FFFF, 16)");
    check(#FF_FF_FF_FF_FF_FF == (parseInteger("FF_FF_FF_FF_FF_FF", 16) else ""), "parseInteger(FF_FF_FF_FF_FF_FF, 16)");
    check(#F_FF_FF_FF_FF_FF == (parseInteger("F_FF_FF_FF_FF_FF", 16) else ""), "parseInteger(F_FF_FF_FF_FF_FF, 16)");
    check(!parseInteger("FF_FF_FF_FFFF", 16) exists, "!parseInteger(FF_FF_FF_FFFF, 16) exists");
    check(!parseInteger("FFFF_FF_FF_FF", 16) exists, "!parseInteger(FFFF_FF_FF_FF, 16) exists");
    
    check(!parseInteger("12_34", 8) exists, "!parseInteger(12_34, 8) exists");
    check(!parseInteger("123_456", 8) exists, "!parseInteger(123_456, 8) exists");
    check(!parseInteger("1234_5678", 8) exists, "!parseInteger(1234_5678, 8) exists");
    
    try {
        parseInteger("0", 1);
        fail("parseInteger(0, 1) should throw");
    } catch (AssertionError ex) {
        // OK
    }
    try {
        parseInteger("0", 37);
        fail("parseInteger(0, 37) should throw");
    } catch (AssertionError ex) {
        // OK
    }
    
    print("Testing `` runtime.integerSize ``-bit integers");
    if (runtime.integerSize == 64) {
        check(9223372036854775807==(parseInteger("9223372036854775807") else ""), "parseInteger(9223372036854775807)");
        check(9_223_372_036_854_775_807==(parseInteger("9_223_372_036_854_775_807") else ""), "parseInteger(9_223_372_036_854_775_807)");
        check(-9223372036854775808==(parseInteger("-9223372036854775808") else ""), "parseInteger(-9223372036854775808)");
        check(-9_223_372_036_854_775_808==(parseInteger("-9_223_372_036_854_775_808") else ""), "parseInteger(-9_223_372_036_854_775_808)");
        check(!parseInteger("9223372036854775808") exists, "parseInteger(9223372036854775808)");
        check(!parseInteger("-9223372036854775809") exists, "parseInteger(-9223372036854775809)");
        
        check(9223372036854775807 == (parseInteger("111111111111111111111111111111111111111111111111111111111111111", 2) else ""),
          "parseInteger(111111111111111111111111111111111111111111111111111111111111111, 2)");
        check(!parseInteger("1000000000000000000000000000000000000000000000000000000000000000", 2) exists,
          "parseInteger(1000000000000000000000000000000000000000000000000000000000000000, 2)");
        check(-9223372036854775808 == (parseInteger("-1000000000000000000000000000000000000000000000000000000000000000", 2) else ""),
          "parseInteger(-1000000000000000000000000000000000000000000000000000000000000000, 2)");
        check(!parseInteger("-1000000000000000000000000000000000000000000000000000000000000001", 2) exists,
          "parseInteger(-1000000000000000000000000000000000000000000000000000000000000001, 2)");
        
        check(9223372036854775807==(parseInteger("7fffffffffffffff", 16) else ""), "parseInteger(7fffffffffffffff, 16)");
        check(9_223_372_036_854_775_807==(parseInteger("7fff_ffff_ffff_ffff", 16) else ""), "parseInteger(7fff_ffff_ffff_ffff, 16)");
        check(-9223372036854775808==(parseInteger("-8000000000000000", 16) else ""), "parseInteger(-8000000000000000, 16)");
        check(-9_223_372_036_854_775_808==(parseInteger("-8000_0000_0000_0000", 16) else ""), "parseInteger(-8000_0000_0000_0000, 16)");
        check(!parseInteger("8000000000000000", 16) exists, "parseInteger(8000000000000000, 16)");
        check(!parseInteger("-8000000000000001", 16) exists, "parseInteger(-8000000000000001, 16)");
        
        check(9223372036854775807==(parseInteger("1y2p0ij32e8e7", 36) else ""), "parseInteger(1y2p0ij32e8e7, 36)");
        check(-9223372036854775808==(parseInteger("-1y2p0ij32e8e8", 36) else ""), "parseInteger(-1y2p0ij32e8e8, 36)");
        check(!parseInteger("1y2p0ij32e8e8", 36) exists, "parseInteger(1y2p0ij32e8e8, 36)");
        check(!parseInteger("-1y2p0ij32e8e9", 36) exists, "parseInteger(-1y2p0ij32e8e9, 36)");
    } else if (runtime.integerSize == 53) {
        check(9007199254740989==(parseInteger("9007199254740989") else ""), "parseInteger(9007199254740989)");
        check(9_007_199_254_740_989==(parseInteger("9_007_199_254_740_989") else ""), "parseInteger(9_007_199_254_740_989)");
        check(-9007199254740991==(parseInteger("-9007199254740991") else ""), "parseInteger(-9007199254740991)");
        check(-9_007_199_254_740_991==(parseInteger("-9_007_199_254_740_991") else ""), "parseInteger(-9_007_199_254_740_991)");
        check(!parseInteger("9007199254740990") exists, "parseInteger(9007199254740990)");
        check(!parseInteger("-9007199254740992") exists, "parseInteger(-9007199254740992)");
        
        check(9007199254740989 == (parseInteger("11111111111111111111111111111111111111111111111111101", 2) else ""),
          "parseInteger(11111111111111111111111111111111111111111111111111101, 2)");
        check(!parseInteger("11111111111111111111111111111111111111111111111111110", 2) exists,
          "parseInteger(11111111111111111111111111111111111111111111111111110, 2)");
        check(-9007199254740991 == (parseInteger("-11111111111111111111111111111111111111111111111111111", 2) else ""),
          "parseInteger(-11111111111111111111111111111111111111111111111111111, 2)");
        check(!parseInteger("-100000000000000000000000000000000000000000000000000000", 2) exists,
          "parseInteger(-100000000000000000000000000000000000000000000000000000, 2)");
        
        check(9007199254740989==(parseInteger("1ffffffffffffd", 16) else ""), "parseInteger(1ffffffffffffd, 16)");
        check(9_007_199_254_740_989==(parseInteger("1f_ffff_ffff_fffd", 16) else ""), "parseInteger(1f_ffff_ffff_fffd, 16)");
        check(-9007199254740991==(parseInteger("-1fffffffffffff", 16) else ""), "parseInteger(-1fffffffffffff, 16)");
        check(-9_007_199_254_740_991==(parseInteger("-1f_ffff_ffff_ffff", 16) else ""), "parseInteger(-1f_ffff_ffff_ffff, 16)");
        check(!parseInteger("1ffffffffffffe", 16) exists, "parseInteger(1ffffffffffffe, 16)");
        check(!parseInteger("-20000000000000", 16) exists, "parseInteger(-20000000000000, 16)");
        
        check(9007199254740989==(parseInteger("2gosa7pa2gt", 36) else ""), "parseInteger(2gosa7pa2gt, 36)");
        check(-9007199254740991==(parseInteger("-2gosa7pa2gv", 36) else ""), "parseInteger(-2gosa7pa2gv, 36)");
        check(!parseInteger("2gosa7pa2gu", 36) exists, "parseInteger(2gosa7pa2gu, 36)");
        check(!parseInteger("-2gosa7pa2gw", 36) exists, "parseInteger(-2gosa7pa2gw, 36)");
    } else {
        fail("UNKNOWN INTEGER SIZE `` runtime.integerSize `` - please add parseInteger() tests for this platform");
    }

    check(!parseInteger("") exists, "parseInteger()");
    check(!parseInteger("+") exists, "parseInteger(+)");
    check(!parseInteger("-") exists, "parseInteger(-)");
    check(!parseInteger("foo") exists, "parseInteger(foo)");
    check(!parseInteger(" 0") exists, "parseInteger( 0)");
    check(!parseInteger("0 ") exists, "parseInteger(0 )");
    check(!parseInteger("0+0") exists, "parseInteger(0+0)");
    check(!parseInteger("0-0") exists, "parseInteger(0-0)");
    check(!parseInteger("0+") exists, "parseInteger(0+)");
    check(!parseInteger("0-") exists, "parseInteger(0-)");
    check(!parseInteger("k") exists, "parseInteger(k)");
    check(!parseInteger("k1") exists, "parseInteger(k1)");
    check(!parseInteger("+k") exists, "parseInteger(+k)");
    check(!parseInteger("-k") exists, "parseInteger(-k)");
    check(!parseInteger("1kk") exists, "parseInteger(1kk)");
    check(!parseInteger("0_") exists, "parseInteger(0_)");
    check(!parseInteger("_0") exists, "parseInteger(_0)");
    check(!parseInteger("0_0") exists, "parseInteger(0_0)");
    check(!parseInteger("0_00") exists, "parseInteger(0_00)");
    check(!parseInteger("0_0000") exists, "parseInteger(0_0000)");
    check(!parseInteger("0_000_0") exists, "parseInteger(0_000_0)");
    check(!parseInteger("0000_000") exists, "parseInteger(0000_000)");
}


void checkFormatInteger() {
    check("0"==formatInteger(0), "formatInteger(0)");
    check("1"==formatInteger(1), "formatInteger(1)");
    check("-1"==formatInteger(-1), "formatInteger(-1)");
    check("1234567890"==formatInteger(1234567890), "formatInteger(1234567890)");
    check("-1234567890"==formatInteger(-1234567890), "formatInteger(-1234567890)");
    try {
        formatInteger(0, 1);
        fail("formatInteger(0, 1) should throw");
    } catch (AssertionError ex) {
        // OK
    }
    try {
        formatInteger(0, 37);
        fail("formatInteger(0, 37) should throw");
    } catch (AssertionError ex) {
        // OK
    }
    if (runtime.integerSize == 64) {
        Integer maxIntegerValue = 9_223_372_036_854_775_807;
        Integer minIntegerValue = -9_223_372_036_854_775_808;
        check("9223372036854775807"==formatInteger(maxIntegerValue), "formatInteger(``maxIntegerValue``)");
        check("-9223372036854775808"==formatInteger(minIntegerValue), "formatInteger(``minIntegerValue``)");
        
        check("111111111111111111111111111111111111111111111111111111111111111"==formatInteger(maxIntegerValue, 2), "formatInteger(``maxIntegerValue``, 2)");
        check("-1000000000000000000000000000000000000000000000000000000000000000"==formatInteger(minIntegerValue, 2), "formatInteger(``minIntegerValue``, 2)");
        
        check("7fffffffffffffff"==formatInteger(maxIntegerValue, 16), "formatInteger(``maxIntegerValue``, 16)");
        check("-8000000000000000"==formatInteger(minIntegerValue, 16), "formatInteger(``minIntegerValue``, 16)");
        
        check("1y2p0ij32e8e7"==formatInteger(maxIntegerValue, 36), "formatInteger(``maxIntegerValue``, 36)");
        check("-1y2p0ij32e8e8"==formatInteger(minIntegerValue, 36), "formatInteger(``minIntegerValue``, 36)");
    } else if (runtime.integerSize == 53) {
        Integer maxIntegerValue = 9_007_199_254_740_989;
        Integer minIntegerValue = -9_007_199_254_740_991;
        check("9007199254740989"==formatInteger(maxIntegerValue), "formatInteger(``maxIntegerValue``)");
        check("-9007199254740991"==formatInteger(minIntegerValue), "formatInteger(``minIntegerValue``)");
        
        check("11111111111111111111111111111111111111111111111111101"==formatInteger(maxIntegerValue, 2), "formatInteger(``maxIntegerValue``, 2)");
        check("-11111111111111111111111111111111111111111111111111111"==formatInteger(minIntegerValue, 2), "formatInteger(``minIntegerValue``, 2)");
        
        check("1ffffffffffffd"==formatInteger(maxIntegerValue, 16), "formatInteger(``maxIntegerValue``, 16)");
        check("-1fffffffffffff"==formatInteger(minIntegerValue, 16), "formatInteger(``minIntegerValue``, 16)");
        
        check("2gosa7pa2gt"==formatInteger(maxIntegerValue, 36), "formatInteger(``maxIntegerValue``, 36)");
        check("-2gosa7pa2gv"==formatInteger(minIntegerValue, 36), "formatInteger(``minIntegerValue``, 36)");
    } else {
        fail("UNKNOWN INTEGER SIZE `` runtime.integerSize `` - please add number formatInteger() tests for this platform");
    }
}
