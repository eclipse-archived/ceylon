(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//AttributeDeclaration assertionCount at members.ceylon (1:0-1:34)
var $assertionCount=$$$cl15.Integer(0);
function getAssertionCount(){
    return $assertionCount;
}
exports.getAssertionCount=getAssertionCount;
function setAssertionCount(assertionCount){
    $assertionCount=assertionCount; return assertionCount;
}
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at members.ceylon (2:0-2:32)
var $failureCount=$$$cl15.Integer(0);
function getFailureCount(){
    return $failureCount;
}
exports.getFailureCount=getFailureCount;
function setFailureCount(failureCount){
    $failureCount=failureCount; return failureCount;
}
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at members.ceylon (4:0-10:0)
function assert(assertion,message){
    if(message===undefined){message=$$$cl15.String("",0)}
    (setAssertionCount(getAssertionCount().plus($$$cl15.Integer(1))),getAssertionCount());
    if ((assertion.equals($$$cl15.getFalse()))===$$$cl15.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl15.Integer(1))),getFailureCount());
        $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertion failed \""),message.getString(),$$$cl15.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at members.ceylon (12:0-14:0)
function fail(message){
    assert($$$cl15.getFalse(),message);
}
exports.fail=fail;

//MethodDefinition results at members.ceylon (16:0-19:0)
function results(){
    $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertions ",11),getAssertionCount().getString(),$$$cl15.String(", failures ",11),getFailureCount().getString(),$$$cl15.String("",0)])).getString());
}
exports.results=results;

//ClassDefinition Counter at members.ceylon (21:0-35:0)
function Counter(initialCount, $$counter){
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initialCount===undefined){initialCount=$$$cl15.Integer(0)}
    $$counter.initialCount=initialCount;
    
    //AttributeDeclaration currentCount at members.ceylon (22:4-22:45)
    var $currentCount=initialCount;
    function getCurrentCount(){
        return $currentCount;
    }
    $$counter.getCurrentCount=getCurrentCount;
    function setCurrentCount(currentCount){
        $currentCount=currentCount; return currentCount;
    }
    $$counter.setCurrentCount=setCurrentCount;
    
    //AttributeGetterDefinition count at members.ceylon (23:4-25:4)
    function getCount(){
        return getCurrentCount();
    }
    $$counter.getCount=getCount;
    
    //MethodDefinition inc at members.ceylon (26:4-28:4)
    function inc(){
        setCurrentCount(getCurrentCount().plus($$$cl15.Integer(1)));
    }
    $$counter.inc=inc;
    
    //AttributeGetterDefinition initialCount at members.ceylon (29:4-31:4)
    function getInitialCount(){
        return initialCount;
    }
    $$counter.getInitialCount=getInitialCount;
    
    //AttributeGetterDefinition string at members.ceylon (32:4-34:4)
    function getString(){
        return $$$cl15.String("Counter[",8).plus($$counter.getCount().getString()).plus($$$cl15.String("]",1));
    }
    $$counter.getString=getString;
    return $$counter;
}
exports.Counter=Counter;
$$$cl15.initType(Counter,'members.Counter',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Counter,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition Issue10C1 at members.ceylon (37:0-51:0)
function Issue10C1(arg1, $$issue10C1){
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1=arg1;
    
    //AttributeDeclaration i1 at members.ceylon (38:4-38:18)
    var $i1=$$$cl15.Integer(3);
    function getI1(){
        return $i1;
    }
    $$issue10C1.getI1=getI1;
    
    //AttributeDeclaration i2 at members.ceylon (39:4-39:18)
    var $i2=$$$cl15.Integer(5);
    function getI2(){
        return $i2;
    }
    $$issue10C1.getI2=getI2;
    
    //AttributeDeclaration i3 at members.ceylon (40:4-40:33)
    var $i3=$$$cl15.Integer(7);
    function getI3(){
        return $i3;
    }
    $$issue10C1.getI3=getI3;
    
    //MethodDefinition f1 at members.ceylon (41:4-41:39)
    function f1(){
        return arg1;
    }
    $$issue10C1.f1=f1;
    
    //MethodDefinition f2 at members.ceylon (42:4-42:37)
    function f2(){
        return getI1();
    }
    $$issue10C1.f2=f2;
    
    //MethodDefinition f3 at members.ceylon (43:4-43:37)
    function f3(){
        return getI2();
    }
    $$issue10C1.f3=f3;
    
    //MethodDefinition f4 at members.ceylon (44:4-44:37)
    function f4(){
        return $$issue10C1.getI3();
    }
    $$issue10C1.f4=f4;
    
    //MethodDefinition f5 at members.ceylon (45:4-45:29)
    function f5(){
        return $$$cl15.Integer(9);
    }
    
    //MethodDefinition f6 at members.ceylon (46:4-46:39)
    function f6(){
        return f5();
    }
    $$issue10C1.f6=f6;
    
    //MethodDefinition f7 at members.ceylon (47:4-47:30)
    function f7(){
        return $$$cl15.Integer(11);
    }
    
    //MethodDefinition f8 at members.ceylon (48:4-48:39)
    function f8(){
        return f7();
    }
    $$issue10C1.f8=f8;
    
    //MethodDefinition f9 at members.ceylon (49:4-49:45)
    function f9(){
        return $$$cl15.Integer(13);
    }
    $$issue10C1.f9=f9;
    
    //MethodDefinition f10 at members.ceylon (50:4-50:40)
    function f10(){
        return $$issue10C1.f9();
    }
    $$issue10C1.f10=f10;
    return $$issue10C1;
}
$$$cl15.initType(Issue10C1,'members.Issue10C1',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Issue10C1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition Issue10C2 at members.ceylon (52:0-62:0)
function Issue10C2(arg1, $$issue10C2){
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1=arg1;
    Issue10C1($$$cl15.Integer(1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (53:4-53:18)
    var $i1=$$$cl15.Integer(4);
    function getI1(){
        return $i1;
    }
    $$issue10C2.getI1=getI1;
    
    //AttributeDeclaration i2 at members.ceylon (54:4-54:25)
    var $i2=$$$cl15.Integer(6);
    function getI2(){
        return $i2;
    }
    $$issue10C2.getI2=getI2;
    
    //AttributeDeclaration i3 at members.ceylon (55:4-55:32)
    var $i3=$$$cl15.Integer(8);
    function getI3(){
        return $i3;
    }
    $$issue10C2.getI3=getI3;
    
    //MethodDefinition f11 at members.ceylon (56:4-56:40)
    function f11(){
        return arg1;
    }
    $$issue10C2.f11=f11;
    
    //MethodDefinition f12 at members.ceylon (57:4-57:38)
    function f12(){
        return getI1();
    }
    $$issue10C2.f12=f12;
    
    //MethodDefinition f5 at members.ceylon (58:4-58:30)
    function f5(){
        return $$$cl15.Integer(10);
    }
    
    //MethodDefinition f13 at members.ceylon (59:4-59:40)
    function f13(){
        return f5();
    }
    $$issue10C2.f13=f13;
    
    //MethodDefinition f7 at members.ceylon (60:4-60:37)
    function f7(){
        return $$$cl15.Integer(12);
    }
    $$issue10C2.f7=f7;
    
    //MethodDefinition f9 at members.ceylon (61:4-61:44)
    function f9(){
        return $$$cl15.Integer(14);
    }
    $$issue10C2.f9=f9;
    return $$issue10C2;
}
$$$cl15.initType(Issue10C2,'members.Issue10C2',Issue10C1);

//MethodDefinition testIssue10 at members.ceylon (64:0-80:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (65:4-65:28)
    var $obj=Issue10C2($$$cl15.Integer(2));
    function getObj(){
        return $obj;
    }
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f1)})()().equals($$$cl15.Integer(1)),$$$cl15.String("Issue #10 (parameter)",21));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f11)})()().equals($$$cl15.Integer(2)),$$$cl15.String("Issue #10 (parameter)",21));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f2)})()().equals($$$cl15.Integer(3)),$$$cl15.String("Issue #10 (non-shared attribute)",32));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f12)})()().equals($$$cl15.Integer(4)),$$$cl15.String("Issue #10 (non-shared attribute)",32));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f3)})()().equals($$$cl15.Integer(5)),$$$cl15.String("Issue #10 (non-shared attribute)",32));
    assert(getObj().getI2().equals($$$cl15.Integer(6)),$$$cl15.String("Issue #10 (shared attribute)",28));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f4)})()().equals($$$cl15.Integer(8)),$$$cl15.String("Issue #10 (shared attribute)",28));
    assert(getObj().getI3().equals($$$cl15.Integer(8)),$$$cl15.String("Issue #10 (shared attribute)",28));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f6)})()().equals($$$cl15.Integer(9)),$$$cl15.String("Issue #10 (non-shared method)",29));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f13)})()().equals($$$cl15.Integer(10)),$$$cl15.String("Issue #10 (non-shared method)",29));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f8)})()().equals($$$cl15.Integer(11)),$$$cl15.String("Issue #10 (non-shared method)",29));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f7)})()().equals($$$cl15.Integer(12)),$$$cl15.String("Issue #10 (shared method)",25));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f10)})()().equals($$$cl15.Integer(14)),$$$cl15.String("Issue #10 (shared method)",25));
    assert((function(){var $=getObj();return $$$cl15.JsCallable($, $.f9)})()().equals($$$cl15.Integer(14)),$$$cl15.String("Issue #10 (shared method)",25));
}

//ClassDefinition AssignTest at members.ceylon (82:0-86:0)
function AssignTest($$assignTest){
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (83:4-83:34)
    var $x=$$$cl15.Integer(1);
    function getX(){
        return $x;
    }
    $$assignTest.getX=getX;
    function setX(x){
        $x=x; return x;
    }
    $$assignTest.setX=setX;
    
    //AttributeGetterDefinition y at members.ceylon (84:4-84:33)
    function getY(){
        return $$assignTest.getX();
    }
    $$assignTest.getY=getY;
    
    //AttributeSetterDefinition y at members.ceylon (85:4-85:23)
    function setY(y){
        $$assignTest.setX(y);
    }
    $$assignTest.setY=setY;
    return $$assignTest;
}
$$$cl15.initType(AssignTest,'members.AssignTest',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(AssignTest,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition test at members.ceylon (88:0-103:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (89:4-89:24)
    var $c=Counter($$$cl15.Integer(0));
    function getC(){
        return $c;
    }
    assert(getC().getCount().equals($$$cl15.Integer(0)),$$$cl15.String("counter 1",9));
    (function(){var $=getC();return $$$cl15.JsCallable($, $.inc)})()();
    (function(){var $=getC();return $$$cl15.JsCallable($, $.inc)})()();
    assert(getC().getCount().equals($$$cl15.Integer(2)),$$$cl15.String("counter 2",9));
    assert(getC().getString().equals($$$cl15.String("Counter[2]",10)),$$$cl15.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (97:4-97:27)
    var $at=AssignTest();
    function getAt(){
        return $at;
    }
    getAt().setX($$$cl15.Integer(5));
    assert(getAt().getX().equals($$$cl15.Integer(5)),$$$cl15.String("assign to member",16));
    getAt().setY($$$cl15.Integer(2));
    assert(getAt().getY().equals($$$cl15.Integer(2)),$$$cl15.String("assign using setter",19));
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
