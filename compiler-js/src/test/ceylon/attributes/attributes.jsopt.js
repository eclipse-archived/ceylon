(function(define) { define(function(require, exports, module) {
var $$$cl376=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at attributes.ceylon (1:0-1:34)
var assertionCount$377=$$$cl376.Integer(0);
var getAssertionCount=function(){return assertionCount$377;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount$378){assertionCount$377=assertionCount$378; return assertionCount$377;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at attributes.ceylon (2:0-2:32)
var failureCount$379=$$$cl376.Integer(0);
var getFailureCount=function(){return failureCount$379;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount$380){failureCount$379=failureCount$380; return failureCount$379;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at attributes.ceylon (4:0-10:0)
function assert(assertion$381,message$382){
    if(message$382===undefined){message$382=$$$cl376.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl376.Integer(1))),getAssertionCount());
    if ((assertion$381.equals($$$cl376.getFalse()))===$$$cl376.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl376.Integer(1))),getFailureCount());
        $$$cl376.print($$$cl376.StringBuilder().appendAll($$$cl376.ArraySequence([$$$cl376.String("assertion failed \""),message$382.getString(),$$$cl376.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition assertEqual at attributes.ceylon (11:0-17:0)
function assertEqual(actual$383,expected$384,message$385){
    if(message$385===undefined){message$385=$$$cl376.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl376.Integer(1))),getAssertionCount());
    if ((actual$383.equals(expected$384).equals($$$cl376.getFalse()))===$$$cl376.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl376.Integer(1))),getFailureCount());
        $$$cl376.print($$$cl376.StringBuilder().appendAll($$$cl376.ArraySequence([$$$cl376.String("assertion failed \""),message$385.getString(),$$$cl376.String("\": '"),actual$383.getString(),$$$cl376.String("'!='",4),expected$384.getString(),$$$cl376.String("'",1)])).getString());
    }
    
}
exports.assertEqual=assertEqual;

//MethodDefinition results at attributes.ceylon (19:0-22:0)
function results(){
    $$$cl376.print($$$cl376.StringBuilder().appendAll($$$cl376.ArraySequence([$$$cl376.String("assertions ",11),getAssertionCount().getString(),$$$cl376.String(", failures ",11),getFailureCount().getString(),$$$cl376.String("",0)])).getString());
}
exports.results=results;

//AttributeDeclaration firstName at attributes.ceylon (24:0-24:26)
var firstName$386=$$$cl376.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (26:0-28:0)
var getLastName=function(){
    return $$$cl376.String("King",4);
}

//AttributeDeclaration flag at attributes.ceylon (30:0-30:26)
var flag$387=$$$cl376.Integer(0);
var getFlag=function(){return flag$387;};
exports.getFlag=getFlag;
var setFlag=function(flag$388){flag$387=flag$388; return flag$387;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (31:0-34:0)
var setLastName=function(lastName$389){
    setFlag($$$cl376.Integer(1));
}

//MethodDefinition test at attributes.ceylon (36:0-48:0)
function test(){
    assertEqual(getLastName(),$$$cl376.String("King",4),$$$cl376.String("toplevel getter",15));
    setLastName($$$cl376.String("Duke",4));
    assertEqual(getFlag(),$$$cl376.Integer(1),$$$cl376.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (41:4-41:26)
    var getX$390=function(){
        return $$$cl376.Integer(5);
    }
    
    //AttributeSetterDefinition x at attributes.ceylon (42:4-42:26)
    var setX$390=function(x$391){
        setFlag($$$cl376.Integer(2));
    }
    assertEqual(getX$390(),$$$cl376.Integer(5),$$$cl376.String("local getter",12));
    setX$390($$$cl376.Integer(7));
    assertEqual(getFlag(),$$$cl376.Integer(2),$$$cl376.String("local setter",12));
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
