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
    return this.getCurrentCount();
}

//MethodDefinition inc at members.ceylon (6:4-8:4)
$Counter.prototype.inc=function inc(){
    this.setCurrentCount(this.getCurrentCount().plus($$$cl15.Integer(1)));
}

//AttributeGetterDefinition initialCount at members.ceylon (9:4-11:4)
$Counter.prototype.getInitialCount=function getInitialCount(){
    return this.initialCount;
}

//AttributeGetterDefinition string at members.ceylon (12:4-14:4)
$Counter.prototype.getString=function getString(){
    return $$$cl15.String("Counter[").plus(this.getCount().getString()).plus($$$cl15.String("]"));
}
function Counter(initialCount, $$){
    if ($$===undefined)$$=new $Counter;
    var $$counter=$$;
    $$.initialCount=initialCount;
    
    //AttributeDeclaration currentCount at members.ceylon (2:4-2:45)
    $$.currentCount=$$.initialCount;
    return $$;
}
this.Counter=Counter;
