var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Pair at types.ceylon (1:0-7:0)
function Pair(x, y, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$pair=$$;
    
    //AttributeGetterDefinition string at types.ceylon (4:4-6:4)
    function getString(){
        return $$$cl15.String("(").plus(x.getString()).plus($$$cl15.String(", ")).plus(y.getString()).plus($$$cl15.String(")"));
    }
    $$.getString$=$$.getString;
    $$.getString=getString;
    return $$;
}

//ClassDefinition Complex at types.ceylon (9:0-17:0)
function Complex(x, y, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$complex=$$;
    Pair(x,y,$$);
    
    //AttributeGetterDefinition string at types.ceylon (11:4-13:4)
    function getString(){
        return x.getString().plus($$$cl15.String("+")).plus(y.getString()).plus($$$cl15.String("i"));
    }
    $$.getString$=$$.getString;
    $$.getString=getString;
    
    //AttributeGetterDefinition pairString at types.ceylon (14:4-16:4)
    function getPairString(){
        return $$.getString$();
    }
    $$.getPairString=getPairString;
    return $$;
}

//InterfaceDefinition List at types.ceylon (19:0-24:0)
function List($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$list=$$;
    
    //AttributeGetterDefinition empty at types.ceylon (21:4-23:4)
    function getEmpty(){
        return $$.getSize().equals($$$cl15.Integer(0));
    }
    $$.getEmpty=getEmpty;
    return $$;
}

//ClassDefinition ConcreteList at types.ceylon (26:0-34:0)
function ConcreteList(xs, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$concreteList=$$;
    List($$);
    
    //AttributeGetterDefinition size at types.ceylon (28:4-30:4)
    function getSize(){
        return $$$cl15.Integer(0);
    }
    $$.getSize$=$$.getSize;
    $$.getSize=getSize;
    
    //AttributeGetterDefinition empty at types.ceylon (31:4-33:4)
    function getEmpty(){
        return $$$cl15.getTrue();
    }
    $$.getEmpty$=$$.getEmpty;
    $$.getEmpty=getEmpty;
    return $$;
}

//ClassDefinition Couple at types.ceylon (36:0-40:0)
function Couple(x, y, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$couple=$$;
    Pair(x,y,$$);
    
    //AttributeDeclaration x at types.ceylon (38:4-38:18)
    var $x=x;
    function getX(){
        return $x;
    }
    $$.getX=getX;
    
    //AttributeDeclaration y at types.ceylon (39:4-39:18)
    var $y=y;
    function getY(){
        return $y;
    }
    $$.getY=getY;
    return $$;
}

//MethodDefinition test at types.ceylon (42:0-49:0)
function test(){
    
    //AttributeDeclaration pair at types.ceylon (43:4-43:39)
    var $pair=Pair($$$cl15.String("hello"),$$$cl15.String("world"));
    function getPair(){
        return $pair;
    }
    $$$cl15.print(getPair());
    
    //AttributeDeclaration zero at types.ceylon (45:4-45:34)
    var $zero=Complex($$$cl15.Float(0.0),$$$cl15.Float(0.0));
    function getZero(){
        return $zero;
    }
    $$$cl15.print(getZero());
    $$$cl15.print(getZero().getPairString());
    $$$cl15.print(ConcreteList().getEmpty());
}
this.test=test;
