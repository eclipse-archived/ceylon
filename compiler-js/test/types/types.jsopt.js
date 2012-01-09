var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition expect at types.ceylon (1:0-8:0)
function expect(actual,expected,text){
    if ((actual.equals(expected))===$$$cl15.getTrue()){
        $$$cl15.print($$$cl15.String("[ok] ").plus(text).plus($$$cl15.String(": '")).plus(actual.getString()).plus($$$cl15.String("'")));
    }
    else {
        $$$cl15.print($$$cl15.String("[NOT OK] ").plus(text).plus($$$cl15.String(": actual='")).plus(actual.getString()).plus($$$cl15.String("', expected='")).plus(expected.getString()).plus($$$cl15.String("'")));
    }
    
}

//ClassDefinition Pair at types.ceylon (10:0-16:0)
function $Pair(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $Pair.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Pair.prototype[$+'$CeylonObject$']=$m}
}

//AttributeGetterDefinition string at types.ceylon (13:4-15:4)
$Pair.prototype.getString=function getString(){
    var $$pair=this;
    return $$$cl15.String("(").plus($$pair.x$Pair.getString()).plus($$$cl15.String(", ")).plus($$pair.y$Pair.getString()).plus($$$cl15.String(")"));
}
function Pair(x$Pair, y$Pair, $$pair){
    if ($$pair===undefined)$$pair=new $Pair;
    $$pair.x$Pair=x$Pair;
    $$pair.y$Pair=y$Pair;
    return $$pair;
}

//ClassDefinition Complex at types.ceylon (18:0-26:0)
function $Complex(){}
for(var $ in $Pair.prototype){
    var $m=$Pair.prototype[$];
    $Complex.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Complex.prototype[$+'$Pair$']=$m}
}

//AttributeGetterDefinition string at types.ceylon (20:4-22:4)
$Complex.prototype.getString=function getString(){
    var $$complex=this;
    return $$complex.x$Complex.getString().plus($$$cl15.String("+")).plus($$complex.y$Complex.getString()).plus($$$cl15.String("i"));
}

//AttributeGetterDefinition pairString at types.ceylon (23:4-25:4)
$Complex.prototype.getPairString=function getPairString(){
    var $$complex=this;
    return $$complex.getString$Pair$();
}
function Complex(x$Complex, y$Complex, $$complex){
    if ($$complex===undefined)$$complex=new $Complex;
    $$complex.x$Complex=x$Complex;
    $$complex.y$Complex=y$Complex;
    Pair($$complex.x$Complex,$$complex.y$Complex,$$complex);
    return $$complex;
}

//InterfaceDefinition List at types.ceylon (28:0-33:0)
function $List(){}

//AttributeGetterDefinition empty at types.ceylon (30:4-32:4)
$List.prototype.getEmpty=function getEmpty(){
    var $$list=this;
    return $$list.getSize().equals($$$cl15.Integer(0));
}
function List($$list){
    if ($$list===undefined)$$list=new $List;
    return $$list;
}

//ClassDefinition ConcreteList at types.ceylon (35:0-43:0)
function $ConcreteList(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $ConcreteList.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$ConcreteList.prototype[$+'$CeylonObject$']=$m}
}
for(var $ in $List.prototype){
    var $m=$List.prototype[$];
    $ConcreteList.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$ConcreteList.prototype[$+'$List$']=$m}
}

//AttributeGetterDefinition size at types.ceylon (37:4-39:4)
$ConcreteList.prototype.getSize=function getSize(){
    var $$concreteList=this;
    return $$$cl15.Integer(0);
}

//AttributeGetterDefinition empty at types.ceylon (40:4-42:4)
$ConcreteList.prototype.getEmpty=function getEmpty(){
    var $$concreteList=this;
    return $$$cl15.getTrue();
}
function ConcreteList(xs$ConcreteList, $$concreteList){
    if ($$concreteList===undefined)$$concreteList=new $ConcreteList;
    List($$concreteList);
    return $$concreteList;
}

//ClassDefinition Couple at types.ceylon (45:0-50:0)
function $Couple(){}
for(var $ in $Pair.prototype){
    var $m=$Pair.prototype[$];
    $Couple.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Couple.prototype[$+'$Pair$']=$m}
}

//AttributeDeclaration x at types.ceylon (48:4-48:18)
$Couple.prototype.getX=function getX(){
    return this.x;
}

//AttributeDeclaration y at types.ceylon (49:4-49:18)
$Couple.prototype.getY=function getY(){
    return this.y;
}
function Couple(x$Couple, y$Couple, $$couple){
    if ($$couple===undefined)$$couple=new $Couple;
    Pair(x$Couple,y$Couple,$$couple);
    
    //AttributeDeclaration x at types.ceylon (48:4-48:18)
    $$couple.x=x$Couple;
    
    //AttributeDeclaration y at types.ceylon (49:4-49:18)
    $$couple.y=y$Couple;
    return $$couple;
}

//ClassDefinition Issue9C1 at types.ceylon (52:0-54:0)
function $Issue9C1(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $Issue9C1.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Issue9C1.prototype[$+'$CeylonObject$']=$m}
}

//MethodDefinition test at types.ceylon (53:4-53:47)
$Issue9C1.prototype.test=function test(){
    var $$issue9C1=this;
    return $$$cl15.String("1");
}
function Issue9C1($$issue9C1){
    if ($$issue9C1===undefined)$$issue9C1=new $Issue9C1;
    return $$issue9C1;
}

//ClassDefinition Issue9C2 at types.ceylon (55:0-64:0)
function $Issue9C2(){}
for(var $ in $Issue9C1.prototype){
    var $m=$Issue9C1.prototype[$];
    $Issue9C2.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Issue9C2.prototype[$+'$Issue9C1$']=$m}
}

//AttributeDeclaration flag1 at types.ceylon (56:4-56:35)
$Issue9C2.prototype.getFlag1$Issue9C2$=function getFlag1$Issue9C2$(){
    return this.flag1$Issue9C2;
}
$Issue9C2.prototype.setFlag1$Issue9C2$=function setFlag1$Issue9C2$(flag1){
    this.flag1$Issue9C2=flag1;
}

//MethodDefinition test at types.ceylon (57:4-63:4)
$Issue9C2.prototype.test=function test(){
    var $$issue9C2=this;
    if (($$issue9C2.getFlag1$Issue9C2$())===$$$cl15.getTrue()){
        var $$issue9C2=this;
        return $$$cl15.String("ERR1");
    }
    
    $$issue9C2.setFlag1$Issue9C2$($$$cl15.getTrue());
    return $$issue9C2.test$Issue9C1$().plus($$$cl15.String("2"));
}
function Issue9C2($$issue9C2){
    if ($$issue9C2===undefined)$$issue9C2=new $Issue9C2;
    Issue9C1($$issue9C2);
    
    //AttributeDeclaration flag1 at types.ceylon (56:4-56:35)
    $$issue9C2.flag1$Issue9C2=$$$cl15.getFalse();
    return $$issue9C2;
}

//ClassDefinition Issue9C3 at types.ceylon (65:0-74:0)
function $Issue9C3(){}
for(var $ in $Issue9C2.prototype){
    var $m=$Issue9C2.prototype[$];
    $Issue9C3.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Issue9C3.prototype[$+'$Issue9C2$']=$m}
}

//AttributeDeclaration flag2 at types.ceylon (66:4-66:35)
$Issue9C3.prototype.getFlag2$Issue9C3$=function getFlag2$Issue9C3$(){
    return this.flag2$Issue9C3;
}
$Issue9C3.prototype.setFlag2$Issue9C3$=function setFlag2$Issue9C3$(flag2){
    this.flag2$Issue9C3=flag2;
}

//MethodDefinition test at types.ceylon (67:4-73:4)
$Issue9C3.prototype.test=function test(){
    var $$issue9C3=this;
    if (($$issue9C3.getFlag2$Issue9C3$())===$$$cl15.getTrue()){
        var $$issue9C3=this;
        return $$$cl15.String("ERR2");
    }
    
    $$issue9C3.setFlag2$Issue9C3$($$$cl15.getTrue());
    return $$issue9C3.test$Issue9C2$().plus($$$cl15.String("3"));
}
function Issue9C3($$issue9C3){
    if ($$issue9C3===undefined)$$issue9C3=new $Issue9C3;
    Issue9C2($$issue9C3);
    
    //AttributeDeclaration flag2 at types.ceylon (66:4-66:35)
    $$issue9C3.flag2$Issue9C3=$$$cl15.getFalse();
    return $$issue9C3;
}

//MethodDefinition testIssue9 at types.ceylon (76:0-79:0)
function testIssue9(){
    
    //AttributeDeclaration obj at types.ceylon (77:4-77:26)
    var $obj=Issue9C3();
    function getObj(){
        return $obj;
    }
    expect(getObj().test(),$$$cl15.String("123"),$$$cl15.String("Issue #9"));
}

//MethodDefinition test at types.ceylon (81:0-90:0)
function test(){
    
    //AttributeDeclaration pair at types.ceylon (82:4-82:39)
    var $pair=Pair($$$cl15.String("hello"),$$$cl15.String("world"));
    function getPair(){
        return $pair;
    }
    $$$cl15.print(getPair());
    
    //AttributeDeclaration zero at types.ceylon (84:4-84:34)
    var $zero=Complex($$$cl15.Float(0.0),$$$cl15.Float(0.0));
    function getZero(){
        return $zero;
    }
    $$$cl15.print(getZero());
    $$$cl15.print(getZero().getPairString());
    $$$cl15.print(ConcreteList().getEmpty());
    testIssue9();
}
this.test=test;
