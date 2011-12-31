var $ceylon$language=require('../../runtime/ceylon.language.js');

//class Pair at types.ceylon (1:0-1:27)
function Pair(x,y){
    var $thisPair=new CeylonObject();
    return $thisPair;
}

//class Complex at types.ceylon (3:0-4:41)
function Complex(x,y){
    var $thisComplex=new CeylonObject();
    var $super=Pair(x,y);
    for(var $m in $super){$thisComplex[$m]=$super[$m]}
    return $thisComplex;
}

//interface List at types.ceylon (6:0-6:19)
function List(){
    var $thisList=new CeylonObject();
    return $thisList;
}

//class ConcreteList at types.ceylon (8:0-9:27)
function ConcreteList(xs){
    var $thisConcreteList=new CeylonObject();
    var $superList=List();
    for(var $m in $superList){$thisConcreteList[$m]=$superList[$m]}
    return $thisConcreteList;
}
