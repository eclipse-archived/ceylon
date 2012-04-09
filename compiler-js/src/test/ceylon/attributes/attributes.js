(function(define) { define(function(require, exports, module) {
var $$$cl380=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at attributes.ceylon (1:0-1:34)
var assertionCount$381=$$$cl380.Integer(0);
var getAssertionCount=function(){return assertionCount$381;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount$382){assertionCount$381=assertionCount$382; return assertionCount$381;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at attributes.ceylon (2:0-2:32)
var failureCount$383=$$$cl380.Integer(0);
var getFailureCount=function(){return failureCount$383;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount$384){failureCount$383=failureCount$384; return failureCount$383;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at attributes.ceylon (4:0-10:0)
function assert(assertion$385,message$386){
    if(message$386===undefined){message$386=$$$cl380.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl380.Integer(1))),getAssertionCount());
    if ((assertion$385.equals($$$cl380.getFalse()))===$$$cl380.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl380.Integer(1))),getFailureCount());
        $$$cl380.print($$$cl380.StringBuilder().appendAll($$$cl380.ArraySequence([$$$cl380.String("assertion failed \""),message$386.getString(),$$$cl380.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition assertEqual at attributes.ceylon (11:0-17:0)
function assertEqual(actual$387,expected$388,message$389){
    if(message$389===undefined){message$389=$$$cl380.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl380.Integer(1))),getAssertionCount());
    if ((actual$387.equals(expected$388).equals($$$cl380.getFalse()))===$$$cl380.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl380.Integer(1))),getFailureCount());
        $$$cl380.print($$$cl380.StringBuilder().appendAll($$$cl380.ArraySequence([$$$cl380.String("assertion failed \""),message$389.getString(),$$$cl380.String("\": '"),actual$387.getString(),$$$cl380.String("'!='",4),expected$388.getString(),$$$cl380.String("'",1)])).getString());
    }
    
}
exports.assertEqual=assertEqual;

//MethodDefinition results at attributes.ceylon (19:0-22:0)
function results(){
    $$$cl380.print($$$cl380.StringBuilder().appendAll($$$cl380.ArraySequence([$$$cl380.String("assertions ",11),getAssertionCount().getString(),$$$cl380.String(", failures ",11),getFailureCount().getString(),$$$cl380.String("",0)])).getString());
}
exports.results=results;

//AttributeDeclaration firstName at attributes.ceylon (24:0-24:26)
var firstName$390=$$$cl380.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (26:0-28:0)
var getLastName=function(){
    return $$$cl380.String("King",4);
}

//AttributeDeclaration flag at attributes.ceylon (30:0-30:26)
var flag$391=$$$cl380.Integer(0);
var getFlag=function(){return flag$391;};
exports.getFlag=getFlag;
var setFlag=function(flag$392){flag$391=flag$392; return flag$391;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (31:0-34:0)
var setLastName=function(lastName$393){
    setFlag($$$cl380.Integer(1));
}

//MethodDefinition test at attributes.ceylon (36:0-48:0)
function test(){
    assertEqual(getLastName(),$$$cl380.String("King",4),$$$cl380.String("toplevel getter",15));
    setLastName($$$cl380.String("Duke",4));
    assertEqual(getFlag(),$$$cl380.Integer(1),$$$cl380.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (41:4-41:26)
    var getX$394=function(){
        return $$$cl380.Integer(5);
    }
    
    //AttributeSetterDefinition x at attributes.ceylon (42:4-42:26)
    var setX$394=function(x$395){
        setFlag($$$cl380.Integer(2));
    }
    assertEqual(getX$394(),$$$cl380.Integer(5),$$$cl380.String("local getter",12));
    setX$394($$$cl380.Integer(7));
    assertEqual(getFlag(),$$$cl380.Integer(2),$$$cl380.String("local setter",12));
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
