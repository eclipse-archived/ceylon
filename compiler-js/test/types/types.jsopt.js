var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Pair at types.ceylon (1:0-7:0)
function $Pair(){}
for(var $ in CeylonObject.prototype){$Pair.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Pair.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeGetterDefinition string at types.ceylon (4:4-6:4)
$Pair.prototype.getString=function getString(){
    return $$$cl15.String("(").plus(this.x.getString()).plus($$$cl15.String(", ")).plus(this.y.getString()).plus($$$cl15.String(")"));
}
function Pair(x, y, $$){
    if ($$===undefined)$$=new $Pair;
    var $$pair=$$;
    $$.x=x;
    $$.y=y;
    return $$;
}

//ClassDefinition Complex at types.ceylon (9:0-17:0)
function $Complex(){}
for(var $ in $Pair.prototype){$Complex.prototype[$]=$Pair.prototype[$]}
for(var $ in $Pair.prototype){$Complex.prototype[$+'$']=$Pair.prototype[$]}

//AttributeGetterDefinition string at types.ceylon (11:4-13:4)
$Complex.prototype.getString=function getString(){
    return this.x.getString().plus($$$cl15.String("+")).plus(this.y.getString()).plus($$$cl15.String("i"));
}

//AttributeGetterDefinition pairString at types.ceylon (14:4-16:4)
$Complex.prototype.getPairString=function getPairString(){
    return this.getString$();
}
function Complex(x, y, $$){
    if ($$===undefined)$$=new $Complex;
    var $$complex=$$;
    Pair($$.x,$$.y,$$);
    $$.x=x;
    $$.y=y;
    return $$;
}

//InterfaceDefinition List at types.ceylon (19:0-24:0)
function $List(){}

//AttributeGetterDefinition empty at types.ceylon (21:4-23:4)
$List.prototype.getEmpty=function getEmpty(){
    return this.getSize().equals($$$cl15.Integer(0));
}
function List($$){
    if ($$===undefined)$$=new $List;
    var $$list=$$;
    return $$;
}

//ClassDefinition ConcreteList at types.ceylon (26:0-34:0)
function $ConcreteList(){}
for(var $ in CeylonObject.prototype){$ConcreteList.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$ConcreteList.prototype[$+'$']=CeylonObject.prototype[$]}
for(var $ in $List.prototype){$ConcreteList.prototype[$]=$List.prototype[$]}
for(var $ in $List.prototype){$ConcreteList.prototype[$+'$']=$List.prototype[$]}

//AttributeGetterDefinition size at types.ceylon (28:4-30:4)
$ConcreteList.prototype.getSize=function getSize(){
    return $$$cl15.Integer(0);
}

//AttributeGetterDefinition empty at types.ceylon (31:4-33:4)
$ConcreteList.prototype.getEmpty=function getEmpty(){
    return $$$cl15.getTrue();
}
function ConcreteList(xs, $$){
    if ($$===undefined)$$=new $ConcreteList;
    var $$concreteList=$$;
    List($$);
    return $$;
}

//ClassDefinition Couple at types.ceylon (36:0-40:0)
function $Couple(){}
for(var $ in $Pair.prototype){$Couple.prototype[$]=$Pair.prototype[$]}
for(var $ in $Pair.prototype){$Couple.prototype[$+'$']=$Pair.prototype[$]}

//AttributeDeclaration x at types.ceylon (38:4-38:18)
$Couple.prototype.getX=function getX(){
    return this.x;
}

//AttributeDeclaration y at types.ceylon (39:4-39:18)
$Couple.prototype.getY=function getY(){
    return this.y;
}
function Couple(x, y, $$){
    if ($$===undefined)$$=new $Couple;
    var $$couple=$$;
    Pair(x,y,$$);
    
    //AttributeDeclaration x at types.ceylon (38:4-38:18)
    $$.x=x;
    
    //AttributeDeclaration y at types.ceylon (39:4-39:18)
    $$.y=y;
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
