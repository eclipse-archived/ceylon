var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//class Counter at members.ceylon (1:0-9:0)
function Counter(initialCount){
    var $$counter=new CeylonObject;
    
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
    $$counter.getCount=getCount;
    
    //function inc at members.ceylon (6:4-8:4)
    function inc(){
        setCurrentCount(getCurrentCount().plus($$$cl15.Integer(1)));
    }
    $$counter.inc=inc;
    
    //AttributeGetterDefinition initialCount at members.ceylon (9:4-11:4)
    function getInitialCount(){
        return initialCount;
    }
    $$counter.getInitialCount=getInitialCount;
    return $$counter;
}
this.Counter=Counter;
