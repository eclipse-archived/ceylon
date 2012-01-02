var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//class Outer at nesting.ceylon (1:0-8:0)
function Outer(name){
    var $$outer=new CeylonObject;
    
    //class Inner at nesting.ceylon (2:4-6:4)
    function Inner(){
        var $$inner=new CeylonObject;
        
        //function printName at nesting.ceylon (3:8-5:8)
        function printName(){
            $$$cl15.print(name);
        }
        return $$inner;
    }
    
    //value inner at nesting.ceylon (7:4-7:25)
    var $inner=Inner();
    function getInner(){
        return $inner;
    }
    return $$outer;
}

//function outr at nesting.ceylon (10:0-15:0)
function outr(name){
    
    //function inr at nesting.ceylon (11:4-13:4)
    function inr(){
        return name;
    }
    
    //value result at nesting.ceylon (14:4-14:25)
    var $result=inr();
    function getResult(){
        return $result;
    }
    
}
