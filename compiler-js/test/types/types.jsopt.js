var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Pair at types.ceylon (1:0-1:27)
function $Pair(){}
function Pair(x,y){
    var $$pair=new $Pair;
    return $$pair;
}

//ClassDefinition Complex at types.ceylon (3:0-4:41)
function $Complex(){}
for(var $ in $Pair.prototype){$Complex.prototype[$]=$Pair.prototype[$]}
function Complex(x,y){
    var $$complex=new $Complex;
    $$complex.$superPair=Pair($$complex.x,$$complex.y);
    return $$complex;
}

//InterfaceDefinition List at types.ceylon (6:0-6:23)
function $List(){}
function List(){
    var $$list=new $List;
    return $$list;
}

//ClassDefinition ConcreteList at types.ceylon (8:0-9:27)
function $ConcreteList(){}
for(var $ in $List.prototype){$ConcreteList.prototype[$]=$List.prototype[$]}
function ConcreteList(xs){
    var $$concreteList=new $ConcreteList;
    var $superList=List();
    return $$concreteList;
}
