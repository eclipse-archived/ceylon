
//class Outer at nesting.ceylon (1:0-8:0)
function Outer(name){
    var $thisOuter={};
    
    //class Inner at nesting.ceylon (2:4-6:4)
    function Inner(){
        var $thisInner={};
        
        //function printName at nesting.ceylon (3:8-5:8)
        function printName(){
            print(name);
        }
        return $thisInner;
    }
    $thisOuter.Inner=Inner;
    
    //value inner at nesting.ceylon (7:4-7:25)
    var $inner=Inner();
    function getInner(){
        return $inner;
    }
    return $thisOuter;
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
