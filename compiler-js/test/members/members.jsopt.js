var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Counter at members.ceylon (1:0-12:0)
function $Counter(){}

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
function Counter(initialCount){
    var $$counter=new $Counter;
    $$counter.initialCount=initialCount;
    
    //AttributeDeclaration currentCount at members.ceylon (2:4-2:45)
    $$counter.currentCount=initialCount;
    return $$counter;
}
this.Counter=Counter;
