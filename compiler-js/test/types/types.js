var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Pair at types.ceylon (1:0-1:27)
function Pair(x, y, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$pair=$$;
    return $$;
}

//ClassDefinition Complex at types.ceylon (3:0-4:41)
function Complex(x, y, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$complex=$$;
    Pair(x,y,$$);
    return $$;
}

//InterfaceDefinition List at types.ceylon (6:0-6:23)
function List($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$list=$$;
    return $$;
}

//ClassDefinition ConcreteList at types.ceylon (8:0-9:27)
function ConcreteList(xs, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$concreteList=$$;
    List($$);
    return $$;
}

//ClassDefinition Couple at types.ceylon (11:0-15:0)
function Couple(x, y, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$couple=$$;
    Pair(x,y,$$);
    
    //AttributeDeclaration x at types.ceylon (13:4-13:18)
    var $x=x;
    function getX(){
        return $x;
    }
    $$.getX=getX;
    
    //AttributeDeclaration y at types.ceylon (14:4-14:18)
    var $y=y;
    function getY(){
        return $y;
    }
    $$.getY=getY;
    return $$;
}
