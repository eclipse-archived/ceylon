var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Counter at members.ceylon (1:0-15:0)
function Counter(initialCount, $$counter){
    if ($$counter===undefined)$$counter=new CeylonObject;
    
    //AttributeDeclaration currentCount at members.ceylon (2:4-2:45)
    var $currentCount=initialCount;
    function getCurrentCount(){
        return $currentCount;
    }
    $$counter.getCurrentCount=getCurrentCount;
    function setCurrentCount(currentCount){
        $currentCount=currentCount;
    }
    $$counter.setCurrentCount=setCurrentCount;
    
    //AttributeGetterDefinition count at members.ceylon (3:4-5:4)
    function getCount(){
        return getCurrentCount();
    }
    $$counter.getCount=getCount;
    
    //MethodDefinition inc at members.ceylon (6:4-8:4)
    function inc(){
        setCurrentCount(getCurrentCount().plus($$$cl15.Integer(1)));
    }
    $$counter.inc=inc;
    
    //AttributeGetterDefinition initialCount at members.ceylon (9:4-11:4)
    function getInitialCount(){
        return initialCount;
    }
    $$counter.getInitialCount=getInitialCount;
    
    //AttributeGetterDefinition string at members.ceylon (12:4-14:4)
    function getString(){
        return $$$cl15.String("Counter[").plus($$counter.getCount().getString()).plus($$$cl15.String("]"));
    }
    $$counter.getString=getString;
    return $$counter;
}
this.Counter=Counter;

//MethodDefinition test at members.ceylon (17:0-23:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (18:4-18:24)
    var $c=Counter($$$cl15.Integer(0));
    function getC(){
        return $c;
    }
    $$$cl15.print(getC().getCount());
    getC().inc();
    getC().inc();
    $$$cl15.print(getC().getCount());
    $$$cl15.print(getC());
}
this.test=test;
