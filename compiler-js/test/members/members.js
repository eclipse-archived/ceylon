var $ceylon$language=require('ceylon/language/ceylon.language');

//class Counter at members.ceylon (1:0-9:0)
function Counter(initialCount){
    var $thisCounter=new CeylonObject();
    
    //value currentCount at members.ceylon (2:4-2:45)
    var $currentCount=initialCount;
    function getCurrentCount(){
        return $currentCount;
    }
    function setCurrentCount(currentCount){
        $currentCount=currentCount;
    }
    
    //value count at members.ceylon (3:4-5:4)
    function getCount(){
        return getCurrentCount();
    }
    $thisCounter.getCount=getCount;
    
    //function inc at members.ceylon (6:4-8:4)
    function inc(){
        setCurrentCount(getCurrentCount().plus($ceylon$language.Integer(1)));
    }
    $thisCounter.inc=inc;
    return $thisCounter;
}
this.Counter=Counter;
