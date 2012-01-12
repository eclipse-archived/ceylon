var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition expect at members.ceylon (1:0-8:0)
function expect(actual,expected,text){
    if ((actual.equals(expected))===$$$cl15.getTrue()){
        $$$cl15.print($$$cl15.String("[ok] ",5).plus(text).plus($$$cl15.String(": '",3)).plus(actual.getString()).plus($$$cl15.String("'",1)));
    }
    else {
        $$$cl15.print($$$cl15.String("[NOT OK] ",9).plus(text).plus($$$cl15.String(": actual='",10)).plus(actual.getString()).plus($$$cl15.String("', expected='",13)).plus(expected.getString()).plus($$$cl15.String("'",1)));
    }
    
}

//ClassDefinition Counter at members.ceylon (10:0-24:0)
function $Counter(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $Counter.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Counter.prototype[$+'$CeylonObject$']=$m}
}

//AttributeDeclaration currentCount at members.ceylon (11:4-11:45)
$Counter.prototype.getCurrentCount$Counter$=function getCurrentCount$Counter$(){
    return this.currentCount$Counter;
}
$Counter.prototype.setCurrentCount$Counter$=function setCurrentCount$Counter$(currentCount){
    this.currentCount$Counter=currentCount;
}

//AttributeGetterDefinition count at members.ceylon (12:4-14:4)
$Counter.prototype.getCount=function getCount(){
    var $$counter=this;
    return $$counter.getCurrentCount$Counter$();
}

//MethodDefinition inc at members.ceylon (15:4-17:4)
$Counter.prototype.inc=function inc(){
    var $$counter=this;
    $$counter.setCurrentCount$Counter$($$counter.getCurrentCount$Counter$().plus($$$cl15.Integer(1)));
}

//AttributeGetterDefinition initialCount at members.ceylon (18:4-20:4)
$Counter.prototype.getInitialCount=function getInitialCount(){
    var $$counter=this;
    return $$counter.initialCount$Counter;
}

//AttributeGetterDefinition string at members.ceylon (21:4-23:4)
$Counter.prototype.getString=function getString(){
    var $$counter=this;
    return $$$cl15.String("Counter[",8).plus($$counter.getCount().getString()).plus($$$cl15.String("]",1));
}
function Counter(initialCount$Counter, $$counter){
    if ($$counter===undefined)$$counter=new $Counter;
    $$counter.initialCount$Counter=initialCount$Counter;
    
    //AttributeDeclaration currentCount at members.ceylon (11:4-11:45)
    $$counter.currentCount$Counter=$$counter.initialCount$Counter;
    return $$counter;
}
this.Counter=Counter;

//ClassDefinition Issue10C1 at members.ceylon (26:0-40:0)
function $Issue10C1(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $Issue10C1.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Issue10C1.prototype[$+'$CeylonObject$']=$m}
}

//AttributeDeclaration i1 at members.ceylon (27:4-27:18)
$Issue10C1.prototype.getI1$Issue10C1$=function getI1$Issue10C1$(){
    return this.i1$Issue10C1;
}

//AttributeDeclaration i2 at members.ceylon (28:4-28:18)
$Issue10C1.prototype.getI2$Issue10C1$=function getI2$Issue10C1$(){
    return this.i2$Issue10C1;
}

//AttributeDeclaration i3 at members.ceylon (29:4-29:33)
$Issue10C1.prototype.getI3=function getI3(){
    return this.i3;
}

//MethodDefinition f1 at members.ceylon (30:4-30:39)
$Issue10C1.prototype.f1=function f1(){
    var $$issue10C1=this;
    return $$issue10C1.arg1$Issue10C1;
}

//MethodDefinition f2 at members.ceylon (31:4-31:37)
$Issue10C1.prototype.f2=function f2(){
    var $$issue10C1=this;
    return $$issue10C1.getI1$Issue10C1$();
}

//MethodDefinition f3 at members.ceylon (32:4-32:37)
$Issue10C1.prototype.f3=function f3(){
    var $$issue10C1=this;
    return $$issue10C1.getI2$Issue10C1$();
}

//MethodDefinition f4 at members.ceylon (33:4-33:37)
$Issue10C1.prototype.f4=function f4(){
    var $$issue10C1=this;
    return $$issue10C1.getI3();
}

//MethodDefinition f5 at members.ceylon (34:4-34:29)
$Issue10C1.prototype.f5$Issue10C1$=function f5$Issue10C1$(){
    var $$issue10C1=this;
    return $$$cl15.Integer(9);
}

//MethodDefinition f6 at members.ceylon (35:4-35:39)
$Issue10C1.prototype.f6=function f6(){
    var $$issue10C1=this;
    return $$issue10C1.f5$Issue10C1$();
}

//MethodDefinition f7 at members.ceylon (36:4-36:30)
$Issue10C1.prototype.f7$Issue10C1$=function f7$Issue10C1$(){
    var $$issue10C1=this;
    return $$$cl15.Integer(11);
}

//MethodDefinition f8 at members.ceylon (37:4-37:39)
$Issue10C1.prototype.f8=function f8(){
    var $$issue10C1=this;
    return $$issue10C1.f7$Issue10C1$();
}

//MethodDefinition f9 at members.ceylon (38:4-38:45)
$Issue10C1.prototype.f9=function f9(){
    var $$issue10C1=this;
    return $$$cl15.Integer(13);
}

//MethodDefinition f10 at members.ceylon (39:4-39:40)
$Issue10C1.prototype.f10=function f10(){
    var $$issue10C1=this;
    return $$issue10C1.f9();
}
function Issue10C1(arg1$Issue10C1, $$issue10C1){
    if ($$issue10C1===undefined)$$issue10C1=new $Issue10C1;
    $$issue10C1.arg1$Issue10C1=arg1$Issue10C1;
    
    //AttributeDeclaration i1 at members.ceylon (27:4-27:18)
    $$issue10C1.i1$Issue10C1=$$$cl15.Integer(3);
    
    //AttributeDeclaration i2 at members.ceylon (28:4-28:18)
    $$issue10C1.i2$Issue10C1=$$$cl15.Integer(5);
    
    //AttributeDeclaration i3 at members.ceylon (29:4-29:33)
    $$issue10C1.i3=$$$cl15.Integer(7);
    return $$issue10C1;
}

//ClassDefinition Issue10C2 at members.ceylon (41:0-51:0)
function $Issue10C2(){}
for(var $ in $Issue10C1.prototype){
    var $m=$Issue10C1.prototype[$];
    $Issue10C2.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Issue10C2.prototype[$+'$Issue10C1$']=$m}
}

//AttributeDeclaration i1 at members.ceylon (42:4-42:18)
$Issue10C2.prototype.getI1$Issue10C2$=function getI1$Issue10C2$(){
    return this.i1$Issue10C2;
}

//AttributeDeclaration i2 at members.ceylon (43:4-43:25)
$Issue10C2.prototype.getI2=function getI2(){
    return this.i2;
}

//AttributeDeclaration i3 at members.ceylon (44:4-44:32)
$Issue10C2.prototype.getI3=function getI3(){
    return this.i3;
}

//MethodDefinition f11 at members.ceylon (45:4-45:40)
$Issue10C2.prototype.f11=function f11(){
    var $$issue10C2=this;
    return $$issue10C2.arg1$Issue10C2;
}

//MethodDefinition f12 at members.ceylon (46:4-46:38)
$Issue10C2.prototype.f12=function f12(){
    var $$issue10C2=this;
    return $$issue10C2.getI1$Issue10C2$();
}

//MethodDefinition f5 at members.ceylon (47:4-47:30)
$Issue10C2.prototype.f5$Issue10C2$=function f5$Issue10C2$(){
    var $$issue10C2=this;
    return $$$cl15.Integer(10);
}

//MethodDefinition f13 at members.ceylon (48:4-48:40)
$Issue10C2.prototype.f13=function f13(){
    var $$issue10C2=this;
    return $$issue10C2.f5$Issue10C2$();
}

//MethodDefinition f7 at members.ceylon (49:4-49:37)
$Issue10C2.prototype.f7=function f7(){
    var $$issue10C2=this;
    return $$$cl15.Integer(12);
}

//MethodDefinition f9 at members.ceylon (50:4-50:44)
$Issue10C2.prototype.f9=function f9(){
    var $$issue10C2=this;
    return $$$cl15.Integer(14);
}
function Issue10C2(arg1$Issue10C2, $$issue10C2){
    if ($$issue10C2===undefined)$$issue10C2=new $Issue10C2;
    $$issue10C2.arg1$Issue10C2=arg1$Issue10C2;
    Issue10C1($$$cl15.Integer(1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (42:4-42:18)
    $$issue10C2.i1$Issue10C2=$$$cl15.Integer(4);
    
    //AttributeDeclaration i2 at members.ceylon (43:4-43:25)
    $$issue10C2.i2=$$$cl15.Integer(6);
    
    //AttributeDeclaration i3 at members.ceylon (44:4-44:32)
    $$issue10C2.i3=$$$cl15.Integer(8);
    return $$issue10C2;
}

//MethodDefinition testIssue10 at members.ceylon (53:0-69:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (54:4-54:28)
    var $obj=Issue10C2($$$cl15.Integer(2));
    function getObj(){
        return $obj;
    }
    expect(getObj().f1(),$$$cl15.Integer(1),$$$cl15.String("Issue #10 (parameter)",21));
    expect(getObj().f11(),$$$cl15.Integer(2),$$$cl15.String("Issue #10 (parameter)",21));
    expect(getObj().f2(),$$$cl15.Integer(3),$$$cl15.String("Issue #10 (non-shared attribute)",32));
    expect(getObj().f12(),$$$cl15.Integer(4),$$$cl15.String("Issue #10 (non-shared attribute)",32));
    expect(getObj().f3(),$$$cl15.Integer(5),$$$cl15.String("Issue #10 (non-shared attribute)",32));
    expect(getObj().getI2(),$$$cl15.Integer(6),$$$cl15.String("Issue #10 (shared attribute)",28));
    expect(getObj().f4(),$$$cl15.Integer(8),$$$cl15.String("Issue #10 (shared attribute)",28));
    expect(getObj().getI3(),$$$cl15.Integer(8),$$$cl15.String("Issue #10 (shared attribute)",28));
    expect(getObj().f6(),$$$cl15.Integer(9),$$$cl15.String("Issue #10 (non-shared method)",29));
    expect(getObj().f13(),$$$cl15.Integer(10),$$$cl15.String("Issue #10 (non-shared method)",29));
    expect(getObj().f8(),$$$cl15.Integer(11),$$$cl15.String("Issue #10 (non-shared method)",29));
    expect(getObj().f7(),$$$cl15.Integer(12),$$$cl15.String("Issue #10 (shared method)",25));
    expect(getObj().f10(),$$$cl15.Integer(14),$$$cl15.String("Issue #10 (shared method)",25));
    expect(getObj().f9(),$$$cl15.Integer(14),$$$cl15.String("Issue #10 (shared method)",25));
}

//MethodDefinition test at members.ceylon (71:0-79:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (72:4-72:24)
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
