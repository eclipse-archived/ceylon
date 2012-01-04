var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Pair at types.ceylon (1:0-7:0)
function Pair(x, y, $$pair){
    if ($$pair===undefined)$$pair=new CeylonObject;
    
    //AttributeGetterDefinition string at types.ceylon (4:4-6:4)
    function getString(){
        return $$$cl15.String("(").plus(x.getString()).plus($$$cl15.String(", ")).plus(y.getString()).plus($$$cl15.String(")"));
    }
    $$pair.getString=getString;
    return $$pair;
}

//ClassDefinition Complex at types.ceylon (9:0-17:0)
function Complex(x, y, $$complex){
    if ($$complex===undefined)$$complex=new CeylonObject;
    Pair(x,y,$$complex);
    $$complex.getString$=$$complex.getString;
    
    //AttributeGetterDefinition string at types.ceylon (11:4-13:4)
    function getString(){
        return x.getString().plus($$$cl15.String("+")).plus(y.getString()).plus($$$cl15.String("i"));
    }
    $$complex.getString=getString;
    
    //AttributeGetterDefinition pairString at types.ceylon (14:4-16:4)
    function getPairString(){
        return $$complex.getString$();
    }
    $$complex.getPairString=getPairString;
    return $$complex;
}

//InterfaceDefinition List at types.ceylon (19:0-24:0)
function List($$list){
    if ($$list===undefined)$$list=new CeylonObject;
    
    //AttributeGetterDefinition empty at types.ceylon (21:4-23:4)
    function getEmpty(){
        return $$list.getSize().equals($$$cl15.Integer(0));
    }
    $$list.getEmpty=getEmpty;
    return $$list;
}

//ClassDefinition ConcreteList at types.ceylon (26:0-34:0)
function ConcreteList(xs, $$concreteList){
    if ($$concreteList===undefined)$$concreteList=new CeylonObject;
    List($$concreteList);
    
    //AttributeGetterDefinition size at types.ceylon (28:4-30:4)
    function getSize(){
        return $$$cl15.Integer(0);
    }
    $$concreteList.getSize=getSize;
    
    //AttributeGetterDefinition empty at types.ceylon (31:4-33:4)
    function getEmpty(){
        return $$$cl15.getTrue();
    }
    $$concreteList.getEmpty=getEmpty;
    return $$concreteList;
}

//ClassDefinition Couple at types.ceylon (36:0-41:0)
function Couple(x, y, $$couple){
    if ($$couple===undefined)$$couple=new CeylonObject;
    Pair(x,y,$$couple);
    
    //AttributeDeclaration x at types.ceylon (39:4-39:18)
    var $x=x;
    function getX(){
        return $x;
    }
    $$couple.getX=getX;
    
    //AttributeDeclaration y at types.ceylon (40:4-40:18)
    var $y=y;
    function getY(){
        return $y;
    }
    $$couple.getY=getY;
    return $$couple;
}

//MethodDefinition test at types.ceylon (43:0-50:0)
function test(){
    
    //AttributeDeclaration pair at types.ceylon (44:4-44:39)
    var $pair=Pair($$$cl15.String("hello"),$$$cl15.String("world"));
    function getPair(){
        return $pair;
    }
    $$$cl15.print(getPair());
    
    //AttributeDeclaration zero at types.ceylon (46:4-46:34)
    var $zero=Complex($$$cl15.Float(0.0),$$$cl15.Float(0.0));
    function getZero(){
        return $zero;
    }
    $$$cl15.print(getZero());
    $$$cl15.print(getZero().getPairString());
    $$$cl15.print(ConcreteList().getEmpty());
}
this.test=test;
