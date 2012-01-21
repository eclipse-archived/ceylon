var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition expect at types.ceylon (1:0-8:0)
function expect(actual,expected,text){
    if ((actual.equals(expected))===$$$cl15.getTrue()){
        $$$cl15.print($$$cl15.String("[ok] ",5).plus(text).plus($$$cl15.String(": '",3)).plus(actual.getString()).plus($$$cl15.String("'",1)));
    }
    else {
        $$$cl15.print($$$cl15.String("[NOT OK] ",9).plus(text).plus($$$cl15.String(": actual='",10)).plus(actual.getString()).plus($$$cl15.String("', expected='",13)).plus(expected.getString()).plus($$$cl15.String("'",1)));
    }
    
}

//ClassDefinition Pair at types.ceylon (10:0-16:0)
function Pair(x, y, $$pair){
    if ($$pair===undefined)$$pair=new CeylonObject;
    
    //AttributeGetterDefinition string at types.ceylon (13:4-15:4)
    function getString(){
        return $$$cl15.String("(",1).plus(x.getString()).plus($$$cl15.String(", ",2)).plus(y.getString()).plus($$$cl15.String(")",1));
    }
    $$pair.getString=getString;
    return $$pair;
}

//ClassDefinition Complex at types.ceylon (18:0-26:0)
function Complex(x, y, $$complex){
    if ($$complex===undefined)$$complex=new CeylonObject;
    Pair(x,y,$$complex);
    $$complex.getString$Pair=$$complex.getString;
    
    //AttributeGetterDefinition string at types.ceylon (20:4-22:4)
    function getString(){
        return x.getString().plus($$$cl15.String("+",1)).plus(y.getString()).plus($$$cl15.String("i",1));
    }
    $$complex.getString=getString;
    
    //AttributeGetterDefinition pairString at types.ceylon (23:4-25:4)
    function getPairString(){
        return $$complex.getString$Pair();
    }
    $$complex.getPairString=getPairString;
    return $$complex;
}

//InterfaceDefinition List at types.ceylon (28:0-33:0)
function List($$list){
    if ($$list===undefined)$$list=new CeylonObject;
    
    //AttributeGetterDefinition empty at types.ceylon (30:4-32:4)
    function getEmpty(){
        return $$list.getSize().equals($$$cl15.Integer(0));
    }
    $$list.getEmpty=getEmpty;
    return $$list;
}

//ClassDefinition ConcreteList at types.ceylon (35:0-43:0)
function ConcreteList(xs, $$concreteList){
    if ($$concreteList===undefined)$$concreteList=new CeylonObject;
    List($$concreteList);
    
    //AttributeGetterDefinition size at types.ceylon (37:4-39:4)
    function getSize(){
        return $$$cl15.Integer(0);
    }
    $$concreteList.getSize=getSize;
    
    //AttributeGetterDefinition empty at types.ceylon (40:4-42:4)
    function getEmpty(){
        return $$$cl15.getTrue();
    }
    $$concreteList.getEmpty=getEmpty;
    return $$concreteList;
}

//ClassDefinition Couple at types.ceylon (45:0-50:0)
function Couple(x, y, $$couple){
    if ($$couple===undefined)$$couple=new CeylonObject;
    Pair(x,y,$$couple);
    
    //AttributeDeclaration x at types.ceylon (48:4-48:18)
    var $x=x;
    function getX(){
        return $x;
    }
    $$couple.getX=getX;
    
    //AttributeDeclaration y at types.ceylon (49:4-49:18)
    var $y=y;
    function getY(){
        return $y;
    }
    $$couple.getY=getY;
    return $$couple;
}

//ClassDefinition Issue9C1 at types.ceylon (52:0-54:0)
function Issue9C1($$issue9C1){
    if ($$issue9C1===undefined)$$issue9C1=new CeylonObject;
    
    //MethodDefinition test at types.ceylon (53:4-53:47)
    function test(){
        return $$$cl15.String("1",1);
    }
    $$issue9C1.test=test;
    return $$issue9C1;
}

//ClassDefinition Issue9C2 at types.ceylon (55:0-64:0)
function Issue9C2($$issue9C2){
    if ($$issue9C2===undefined)$$issue9C2=new CeylonObject;
    Issue9C1($$issue9C2);
    $$issue9C2.test$Issue9C1=$$issue9C2.test;
    
    //AttributeDeclaration flag1 at types.ceylon (56:4-56:35)
    var $flag1=$$$cl15.getFalse();
    function getFlag1(){
        return $flag1;
    }
    $$issue9C2.getFlag1=getFlag1;
    function setFlag1(flag1){
        $flag1=flag1; return flag1;
    }
    $$issue9C2.setFlag1=setFlag1;
    
    //MethodDefinition test at types.ceylon (57:4-63:4)
    function test(){
        if ((getFlag1())===$$$cl15.getTrue()){
            return $$$cl15.String("ERR1",4);
        }
        
        setFlag1($$$cl15.getTrue());
        return $$issue9C2.test$Issue9C1().plus($$$cl15.String("2",1));
    }
    $$issue9C2.test=test;
    return $$issue9C2;
}

//ClassDefinition Issue9C3 at types.ceylon (65:0-74:0)
function Issue9C3($$issue9C3){
    if ($$issue9C3===undefined)$$issue9C3=new CeylonObject;
    Issue9C2($$issue9C3);
    $$issue9C3.test$Issue9C2=$$issue9C3.test;
    
    //AttributeDeclaration flag2 at types.ceylon (66:4-66:35)
    var $flag2=$$$cl15.getFalse();
    function getFlag2(){
        return $flag2;
    }
    $$issue9C3.getFlag2=getFlag2;
    function setFlag2(flag2){
        $flag2=flag2; return flag2;
    }
    $$issue9C3.setFlag2=setFlag2;
    
    //MethodDefinition test at types.ceylon (67:4-73:4)
    function test(){
        if ((getFlag2())===$$$cl15.getTrue()){
            return $$$cl15.String("ERR2",4);
        }
        
        setFlag2($$$cl15.getTrue());
        return $$issue9C3.test$Issue9C2().plus($$$cl15.String("3",1));
    }
    $$issue9C3.test=test;
    return $$issue9C3;
}

//MethodDefinition testIssue9 at types.ceylon (76:0-79:0)
function testIssue9(){
    
    //AttributeDeclaration obj at types.ceylon (77:4-77:26)
    var $obj=Issue9C3();
    function getObj(){
        return $obj;
    }
    expect(getObj().test(),$$$cl15.String("123",3),$$$cl15.String("Issue #9",8));
}

//MethodDefinition test at types.ceylon (81:0-90:0)
function test(){
    
    //AttributeDeclaration pair at types.ceylon (82:4-82:39)
    var $pair=Pair($$$cl15.String("hello",5),$$$cl15.String("world",5));
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
