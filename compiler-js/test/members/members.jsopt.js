var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Counter at members.ceylon (1:0-15:0)
function $Counter(){}
for(var $ in CeylonObject.prototype){$Counter.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Counter.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration currentCount at members.ceylon (2:4-2:45)
$Counter.prototype.getCurrentCount=function getCurrentCount(){
    return this.currentCount;
}
$Counter.prototype.setCurrentCount=function setCurrentCount(currentCount){
    this.currentCount=currentCount;
}

//AttributeGetterDefinition count at members.ceylon (3:4-5:4)
$Counter.prototype.getCount=function getCount(){
    var $$=this;
    var $$counter=this;
    return $$counter.getCurrentCount();
}

//MethodDefinition inc at members.ceylon (6:4-8:4)
$Counter.prototype.inc=function inc(){
    var $$=this;
    var $$counter=this;
    $$counter.setCurrentCount($$counter.getCurrentCount().plus($$$cl15.Integer(1)));
}

//AttributeGetterDefinition initialCount at members.ceylon (9:4-11:4)
$Counter.prototype.getInitialCount=function getInitialCount(){
    var $$=this;
    var $$counter=this;
    return $$counter.initialCount;
}

//AttributeGetterDefinition string at members.ceylon (12:4-14:4)
$Counter.prototype.getString=function getString(){
    var $$=this;
    var $$counter=this;
    return $$$cl15.String("Counter[").plus($$counter.getCount().getString()).plus($$$cl15.String("]"));
}
function Counter(initialCount, $$){
    if ($$===undefined)$$=new $Counter;
    var $$counter=$$;
    $$.initialCount=initialCount;
    
    //AttributeDeclaration currentCount at members.ceylon (2:4-2:45)
    $$.currentCount=$$counter.initialCount;
    return $$;
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
