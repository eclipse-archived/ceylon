var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//class Pair at types.ceylon (1:0-1:27)
function Pair(x,y){
    var $$pair=new CeylonObject;
    return $$pair;
}

//class Complex at types.ceylon (3:0-4:41)
function Complex(x,y){
    var $$complex=new CeylonObject;
    var $$pair=Pair(x,y);
    return $$complex;
}

//interface List at types.ceylon (6:0-6:19)
function List(){
    var $$list=new CeylonObject;
    return $$list;
}

//class ConcreteList at types.ceylon (8:0-9:27)
function ConcreteList(xs){
    var $$concreteList=new CeylonObject;
    var $$list=List();
    return $$concreteList;
}

//ClassDefinition Couple at types.ceylon (11:0-15:0)
function Couple(x,y){
    var $$couple=new CeylonObject;
    var $$pair=Pair(x,y);
    
    //AttributeDeclaration x at types.ceylon (13:4-13:18)
    var $x=x;
    function getX(){
        return $x;
    }
    $$couple.getX=getX;
    
    //AttributeDeclaration y at types.ceylon (14:4-14:18)
    var $y=y;
    function getY(){
        return $y;
    }
    $$couple.getY=getY;
    return $$couple;
}
