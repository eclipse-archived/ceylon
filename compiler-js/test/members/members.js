var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition expect at members.ceylon (1:0-8:0)
function expect(actual,expected,text){
    if ((actual.equals(expected))===$$$cl15.getTrue()){
        $$$cl15.print($$$cl15.String("[ok] ").plus(text).plus($$$cl15.String(": '")).plus(actual.getString()).plus($$$cl15.String("'")));
    }
    else {
        $$$cl15.print($$$cl15.String("[NOT OK] ").plus(text).plus($$$cl15.String(": actual='")).plus(actual.getString()).plus($$$cl15.String("', expected='")).plus(expected.getString()).plus($$$cl15.String("'")));
    }
    
}

//ClassDefinition Counter at members.ceylon (10:0-24:0)
function Counter(initialCount, $$counter){
    if ($$counter===undefined)$$counter=new CeylonObject;
    
    //AttributeDeclaration currentCount at members.ceylon (11:4-11:45)
    var $currentCount=initialCount;
    function getCurrentCount(){
        return $currentCount;
    }
    $$counter.getCurrentCount=getCurrentCount;
    function setCurrentCount(currentCount){
        $currentCount=currentCount;
    }
    $$counter.setCurrentCount=setCurrentCount;
    
    //AttributeGetterDefinition count at members.ceylon (12:4-14:4)
    function getCount(){
        return getCurrentCount();
    }
    $$counter.getCount=getCount;
    
    //MethodDefinition inc at members.ceylon (15:4-17:4)
    function inc(){
        setCurrentCount(getCurrentCount().plus($$$cl15.Integer(1)));
    }
    $$counter.inc=inc;
    
    //AttributeGetterDefinition initialCount at members.ceylon (18:4-20:4)
    function getInitialCount(){
        return initialCount;
    }
    $$counter.getInitialCount=getInitialCount;
    
    //AttributeGetterDefinition string at members.ceylon (21:4-23:4)
    function getString(){
        return $$$cl15.String("Counter[").plus($$counter.getCount().getString()).plus($$$cl15.String("]"));
    }
    $$counter.getString=getString;
    return $$counter;
}
this.Counter=Counter;

//ClassDefinition Issue10C1 at members.ceylon (26:0-34:0)
function Issue10C1(arg1, $$issue10C1){
    if ($$issue10C1===undefined)$$issue10C1=new CeylonObject;
    
    //AttributeDeclaration i1 at members.ceylon (27:4-27:18)
    var $i1=$$$cl15.Integer(3);
    function getI1(){
        return $i1;
    }
    $$issue10C1.getI1=getI1;
    
    //AttributeDeclaration i2 at members.ceylon (28:4-28:18)
    var $i2=$$$cl15.Integer(5);
    function getI2(){
        return $i2;
    }
    $$issue10C1.getI2=getI2;
    
    //AttributeDeclaration i3 at members.ceylon (29:4-29:33)
    var $i3=$$$cl15.Integer(7);
    function getI3(){
        return $i3;
    }
    $$issue10C1.getI3=getI3;
    
    //MethodDefinition f1 at members.ceylon (30:4-30:39)
    function f1(){
        return arg1;
    }
    $$issue10C1.f1=f1;
    
    //MethodDefinition f2 at members.ceylon (31:4-31:37)
    function f2(){
        return getI1();
    }
    $$issue10C1.f2=f2;
    
    //MethodDefinition f3 at members.ceylon (32:4-32:37)
    function f3(){
        return getI2();
    }
    $$issue10C1.f3=f3;
    
    //MethodDefinition f4 at members.ceylon (33:4-33:37)
    function f4(){
        return $$issue10C1.getI3();
    }
    $$issue10C1.f4=f4;
    return $$issue10C1;
}

//ClassDefinition Issue10C2 at members.ceylon (35:0-41:0)
function Issue10C2(arg1, $$issue10C2){
    if ($$issue10C2===undefined)$$issue10C2=new CeylonObject;
    Issue10C1($$$cl15.Integer(1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (36:4-36:18)
    var $i1=$$$cl15.Integer(4);
    function getI1(){
        return $i1;
    }
    $$issue10C2.getI1=getI1;
    
    //AttributeDeclaration i2 at members.ceylon (37:4-37:25)
    var $i2=$$$cl15.Integer(6);
    function getI2(){
        return $i2;
    }
    $$issue10C2.getI2=getI2;
    
    //AttributeDeclaration i3 at members.ceylon (38:4-38:32)
    var $i3=$$$cl15.Integer(8);
    function getI3(){
        return $i3;
    }
    $$issue10C2.getI3=getI3;
    
    //MethodDefinition f5 at members.ceylon (39:4-39:39)
    function f5(){
        return arg1;
    }
    $$issue10C2.f5=f5;
    
    //MethodDefinition f6 at members.ceylon (40:4-40:37)
    function f6(){
        return getI1();
    }
    $$issue10C2.f6=f6;
    return $$issue10C2;
}

//MethodDefinition testIssue10 at members.ceylon (43:0-53:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (44:4-44:28)
    var $obj=Issue10C2($$$cl15.Integer(2));
    function getObj(){
        return $obj;
    }
    expect(getObj().f1(),$$$cl15.Integer(1),$$$cl15.String("Issue #10 (parameter)"));
    expect(getObj().f5(),$$$cl15.Integer(2),$$$cl15.String("Issue #10 (parameter)"));
    expect(getObj().f2(),$$$cl15.Integer(3),$$$cl15.String("Issue #10 (non-shared attribute)"));
    expect(getObj().f6(),$$$cl15.Integer(4),$$$cl15.String("Issue #10 (non-shared attribute)"));
    expect(getObj().f3(),$$$cl15.Integer(5),$$$cl15.String("Issue #10 (non-shared attribute)"));
    expect(getObj().getI2(),$$$cl15.Integer(6),$$$cl15.String("Issue #10 (shared attribute)"));
    expect(getObj().f4(),$$$cl15.Integer(8),$$$cl15.String("Issue #10 (shared attribute)"));
    expect(getObj().getI3(),$$$cl15.Integer(8),$$$cl15.String("Issue #10 (shared attribute)"));
}

//MethodDefinition test at members.ceylon (55:0-63:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (56:4-56:24)
    var $c=Counter($$$cl15.Integer(0));
    function getC(){
        return $c;
    }
    $$$cl15.print(getC().getCount());
    getC().inc();
    getC().inc();
    $$$cl15.print(getC().getCount());
    $$$cl15.print(getC());
    testIssue10();
}
this.test=test;
