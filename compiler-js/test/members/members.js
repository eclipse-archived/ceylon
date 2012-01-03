var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Counter at members.ceylon (1:0-15:0)
function Counter(initialCount, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$counter=$$;
    
    //AttributeDeclaration currentCount at members.ceylon (2:4-2:45)
    var $currentCount=initialCount;
    function getCurrentCount(){
        return $currentCount;
    }
    function setCurrentCount(currentCount){
        $currentCount=currentCount;
    }
    
    //AttributeGetterDefinition count at members.ceylon (3:4-5:4)
    function getCount(){
        return getCurrentCount();
    }
    $$.getCount=getCount;
    
    //MethodDefinition inc at members.ceylon (6:4-8:4)
    function inc(){
        setCurrentCount(getCurrentCount().plus($$$cl15.Integer(1)));
    }
    $$.inc=inc;
    
    //AttributeGetterDefinition initialCount at members.ceylon (9:4-11:4)
    function getInitialCount(){
        return initialCount;
    }
    $$.getInitialCount=getInitialCount;
    
    //AttributeGetterDefinition string at members.ceylon (12:4-14:4)
    function getString(){
        return $$$cl15.String("Counter[").plus($$.getCount().getString()).plus($$$cl15.String("]"));
    }
    $$.getString$=$$.getString;
    $$.getString=getString;
    return $$;
}
this.Counter=Counter;
