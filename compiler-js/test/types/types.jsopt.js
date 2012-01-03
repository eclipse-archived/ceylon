var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Pair at types.ceylon (1:0-1:27)
function $Pair(){}
for(var $ in CeylonObject.prototype){$Pair.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Pair.prototype[$+'$']=CeylonObject.prototype[$]}
function Pair(x, y, $$){
    if ($$===undefined)$$=new $Pair;
    var $$pair=$$;
    return $$;
}

//ClassDefinition Complex at types.ceylon (3:0-4:41)
function $Complex(){}
for(var $ in $Pair.prototype){$Complex.prototype[$]=$Pair.prototype[$]}
for(var $ in $Pair.prototype){$Complex.prototype[$+'$']=$Pair.prototype[$]}
function Complex(x, y, $$){
    if ($$===undefined)$$=new $Complex;
    var $$complex=$$;
    Pair(x,y,$$);
    return $$;
}

//InterfaceDefinition List at types.ceylon (6:0-6:23)
function $List(){}
function List($$){
    if ($$===undefined)$$=new $List;
    var $$list=$$;
    return $$;
}

//ClassDefinition ConcreteList at types.ceylon (8:0-9:27)
function $ConcreteList(){}
for(var $ in CeylonObject.prototype){$ConcreteList.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$ConcreteList.prototype[$+'$']=CeylonObject.prototype[$]}
for(var $ in $List.prototype){$ConcreteList.prototype[$]=$List.prototype[$]}
for(var $ in $List.prototype){$ConcreteList.prototype[$+'$']=$List.prototype[$]}
function ConcreteList(xs, $$){
    if ($$===undefined)$$=new $ConcreteList;
    var $$concreteList=$$;
    List($$);
    return $$;
}

//ClassDefinition Couple at types.ceylon (11:0-15:0)
function $Couple(){}
for(var $ in $Pair.prototype){$Couple.prototype[$]=$Pair.prototype[$]}
for(var $ in $Pair.prototype){$Couple.prototype[$+'$']=$Pair.prototype[$]}

//AttributeDeclaration x at types.ceylon (13:4-13:18)
$Couple.prototype.getX=function getX(){
    return this.x;
}

//AttributeDeclaration y at types.ceylon (14:4-14:18)
$Couple.prototype.getY=function getY(){
    return this.y;
}
function Couple(x, y, $$){
    if ($$===undefined)$$=new $Couple;
    var $$couple=$$;
    Pair(x,y,$$);
    
    //AttributeDeclaration x at types.ceylon (13:4-13:18)
    $$.x=x;
    
    //AttributeDeclaration y at types.ceylon (14:4-14:18)
    $$.y=y;
    return $$;
}
