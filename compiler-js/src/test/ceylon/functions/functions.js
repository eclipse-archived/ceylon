(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//AttributeDeclaration assertionCount at functions.ceylon (1:0-1:34)
var $assertionCount=$$$cl15.Integer(0);
function getAssertionCount(){
    return $assertionCount;
}
exports.getAssertionCount=getAssertionCount;
function setAssertionCount(assertionCount){
    $assertionCount=assertionCount; return assertionCount;
}
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at functions.ceylon (2:0-2:32)
var $failureCount=$$$cl15.Integer(0);
function getFailureCount(){
    return $failureCount;
}
exports.getFailureCount=getFailureCount;
function setFailureCount(failureCount){
    $failureCount=failureCount; return failureCount;
}
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at functions.ceylon (4:0-10:0)
function assert(assertion,message){
    if(message===undefined){message=$$$cl15.String("",0)}
    (setAssertionCount(getAssertionCount().plus($$$cl15.Integer(1))),getAssertionCount());
    if ((assertion.equals($$$cl15.getFalse()))===$$$cl15.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl15.Integer(1))),getFailureCount());
        $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertion failed \""),message.getString(),$$$cl15.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at functions.ceylon (12:0-14:0)
function fail(message){
    assert($$$cl15.getFalse(),message);
}
exports.fail=fail;

//MethodDefinition results at functions.ceylon (16:0-19:0)
function results(){
    $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertions ",11),getAssertionCount().getString(),$$$cl15.String(", failures ",11),getFailureCount().getString(),$$$cl15.String("",0)])).getString());
}
exports.results=results;

//MethodDefinition helloWorld at functions.ceylon (22:0-24:0)
function helloWorld(){
    $$$cl15.print($$$cl15.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (26:0-28:0)
function hello(name){
    $$$cl15.print($$$cl15.String("hello",5).plus(name));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (30:0-30:39)
function helloAll(names){
    
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (32:0-34:0)
function toString(obj){
    return obj.getString();
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (36:0-38:0)
function add(x,y){
    return x.plus(y);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (40:0-42:0)
function repeat(times,f){
    f($$$cl15.Integer(0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (44:0-56:0)
function MySequence(seq, $$mySequence){
    /* REIFIED GENERICS SOON! <out Element> */
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$mySequence.seq=seq;
    $$$cl15.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (46:4-46:60)
    function getLastIndex(){
        return seq.getLastIndex();
    }
    $$mySequence.getLastIndex=getLastIndex;
    
    //AttributeGetterDefinition first at functions.ceylon (47:4-47:52)
    function getFirst(){
        return seq.getFirst();
    }
    $$mySequence.getFirst=getFirst;
    
    //AttributeGetterDefinition rest at functions.ceylon (48:4-48:52)
    function getRest(){
        return seq.getRest();
    }
    $$mySequence.getRest=getRest;
    
    //MethodDefinition item at functions.ceylon (49:4-49:68)
    function item(index){
        return seq.item(index);
    }
    $$mySequence.item=item;
    
    //MethodDefinition span at functions.ceylon (50:4-50:89)
    function span(from,to){
        return (function(){var $=seq;return $$$cl15.JsCallable($, $.span)})()(from,to);
    }
    $$mySequence.span=span;
    
    //MethodDefinition segment at functions.ceylon (51:4-51:102)
    function segment(from,length){
        return (function(){var $=seq;return $$$cl15.JsCallable($, $.segment)})()(from,length);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (52:4-52:59)
    function getClone(){
        return $$mySequence;
    }
    $$mySequence.getClone=getClone;
    
    //AttributeGetterDefinition string at functions.ceylon (53:4-53:53)
    function getString(){
        return seq.getString();
    }
    $$mySequence.getString=getString;
    
    //AttributeGetterDefinition hash at functions.ceylon (54:4-54:50)
    function getHash(){
        return seq.getHash();
    }
    $$mySequence.getHash=getHash;
    
    //MethodDefinition equals at functions.ceylon (55:4-55:75)
    function equals(other){
        return (function(){var $=seq;return $$$cl15.JsCallable($, $.equals)})()(other);
    }
    $$mySequence.equals=equals;
    return $$mySequence;
}
$$$cl15.initType(MySequence,'functions.MySequence',$$$cl15.IdentifiableObject,$$$cl15.Sequence);
$$$cl15.inheritProto(MySequence,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$',$$$cl15.Sequence);

//ClassDefinition RefHelper at functions.ceylon (58:0-60:0)
function RefHelper($$refHelper){
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    
    //MethodDefinition f at functions.ceylon (59:4-59:47)
    function f(i){
        return $$$cl15.getTrue();
    }
    $$refHelper.f=f;
    return $$refHelper;
}
$$$cl15.initType(RefHelper,'functions.RefHelper',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(RefHelper,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition testMethodReference at functions.ceylon (62:0-70:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (63:4-63:28)
    var $obj1=RefHelper();
    function getObj1(){
        return $obj1;
    }
    
    //AttributeDeclaration obj2 at functions.ceylon (64:4-64:43)
    var $obj2=MySequence/* REIFIED GENERICS SOON!! <ceylon.language.String> */($$$cl15.ArraySequence([$$$cl15.String("hi",2)]));
    function getObj2(){
        return $obj2;
    }
    
    //MethodDefinition tst at functions.ceylon (65:4-67:4)
    function tst(x){
        return x($$$cl15.Integer(0));
    }
    assert(tst((function(){var $=getObj1();return $$$cl15.JsCallable($, $.f)})()),$$$cl15.String("Reference to method",19));
    assert(tst((function(){var $=getObj2();return $$$cl15.JsCallable($, $.defines)})()),$$$cl15.String("Reference to method from ceylon.language",40));
}

//MethodDefinition defParamTest at functions.ceylon (72:0-74:0)
function defParamTest(i1,i2,i3){
    if(i2===undefined){i2=i1.plus($$$cl15.Integer(1))}
    if(i3===undefined){i3=i1.plus(i2)}
    return $$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("",0),i1.getString(),$$$cl15.String(",",1),i2.getString(),$$$cl15.String(",",1),i3.getString(),$$$cl15.String("",0)])).getString();
}

//ClassDefinition DefParamTest1 at functions.ceylon (75:0-77:0)
function DefParamTest1(i1, i2, i3, $$defParamTest1){
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2===undefined){i2=i1.plus($$$cl15.Integer(1))}
    if(i3===undefined){i3=i1.plus(i2)}
    
    //AttributeDeclaration s at functions.ceylon (76:4-76:44)
    var $s=$$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("",0),i1.getString(),$$$cl15.String(",",1),i2.getString(),$$$cl15.String(",",1),i3.getString(),$$$cl15.String("",0)])).getString();
    function getS(){
        return $s;
    }
    $$defParamTest1.getS=getS;
    return $$defParamTest1;
}
$$$cl15.initType(DefParamTest1,'functions.DefParamTest1',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(DefParamTest1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition DefParamTest2 at functions.ceylon (78:0-80:0)
function DefParamTest2(i1, i2, i3, $$defParamTest2){
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1=i1;
    if(i2===undefined){i2=i1.plus($$$cl15.Integer(1))}
    $$defParamTest2.i2=i2;
    if(i3===undefined){i3=i1.plus(i2)}
    $$defParamTest2.i3=i3;
    
    //MethodDefinition f at functions.ceylon (79:4-79:55)
    function f(){
        return $$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("",0),i1.getString(),$$$cl15.String(",",1),i2.getString(),$$$cl15.String(",",1),i3.getString(),$$$cl15.String("",0)])).getString();
    }
    $$defParamTest2.f=f;
    return $$defParamTest2;
}
$$$cl15.initType(DefParamTest2,'functions.DefParamTest2',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(DefParamTest2,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition DefParamTest3 at functions.ceylon (81:0-85:0)
function DefParamTest3($$defParamTest3){
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    
    //MethodDefinition f at functions.ceylon (82:4-84:4)
    function f(i1,i2,i3){
        if(i2===undefined){i2=i1.plus($$$cl15.Integer(1))}
        if(i3===undefined){i3=i1.plus(i2)}
        return $$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("",0),i1.getString(),$$$cl15.String(",",1),i2.getString(),$$$cl15.String(",",1),i3.getString(),$$$cl15.String("",0)])).getString();
    }
    $$defParamTest3.f=f;
    return $$defParamTest3;
}
$$$cl15.initType(DefParamTest3,'functions.DefParamTest3',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(DefParamTest3,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition testDefaultedParams at functions.ceylon (86:0-115:0)
function testDefaultedParams(){
    assert(defParamTest($$$cl15.Integer(1)).equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted parameters 1",22));
    assert(defParamTest($$$cl15.Integer(1),$$$cl15.Integer(3)).equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted parameters 2",22));
    assert(defParamTest($$$cl15.Integer(1),$$$cl15.Integer(3),$$$cl15.Integer(0)).equals($$$cl15.String("1,3,0",5)),$$$cl15.String("defaulted parameters 3",22));
    assert((function (){var $i1=$$$cl15.Integer(1);return defParamTest($i1,undefined,undefined)}()).equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted parameters named 1",28));
    assert((function (){var $i1=$$$cl15.Integer(1);var $i2=$$$cl15.Integer(3);return defParamTest($i1,$i2,undefined)}()).equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted parameters named 2",28));
    assert((function (){var $i1=$$$cl15.Integer(1);var $i3=$$$cl15.Integer(0);return defParamTest($i1,undefined,$i3)}()).equals($$$cl15.String("1,2,0",5)),$$$cl15.String("defaulted parameters named 3",28));
    assert(DefParamTest1($$$cl15.Integer(1)).getS().equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted parameters class 1",28));
    assert(DefParamTest1($$$cl15.Integer(1),$$$cl15.Integer(3)).getS().equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted parameters class 2",28));
    assert(DefParamTest1($$$cl15.Integer(1),$$$cl15.Integer(3),$$$cl15.Integer(0)).getS().equals($$$cl15.String("1,3,0",5)),$$$cl15.String("defaulted parameters class 3",28));
    assert((function (){var $i1=$$$cl15.Integer(1);return DefParamTest1($i1,undefined,undefined)}()).getS().equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted parameters class named 1",34));
    assert((function (){var $i1=$$$cl15.Integer(1);var $i2=$$$cl15.Integer(3);return DefParamTest1($i1,$i2,undefined)}()).getS().equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted parameters class named 2",34));
    assert((function (){var $i1=$$$cl15.Integer(1);var $i3=$$$cl15.Integer(0);return DefParamTest1($i1,undefined,$i3)}()).getS().equals($$$cl15.String("1,2,0",5)),$$$cl15.String("defaulted parameters class named 3",34));
    assert((function(){var $=DefParamTest2($$$cl15.Integer(1));return $$$cl15.JsCallable($, $.f)})()().equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted parameters class2 1",29));
    assert((function(){var $=DefParamTest2($$$cl15.Integer(1),$$$cl15.Integer(3));return $$$cl15.JsCallable($, $.f)})()().equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted parameters class2 2",29));
    assert((function(){var $=DefParamTest2($$$cl15.Integer(1),$$$cl15.Integer(3),$$$cl15.Integer(0));return $$$cl15.JsCallable($, $.f)})()().equals($$$cl15.String("1,3,0",5)),$$$cl15.String("defaulted parameters class2 3",29));
    assert((function(){var $=(function (){var $i1=$$$cl15.Integer(1);return DefParamTest2($i1,undefined,undefined)}());return $$$cl15.JsCallable($, $.f)})()().equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted parameters class2 named 1",35));
    assert((function(){var $=(function (){var $i1=$$$cl15.Integer(1);var $i2=$$$cl15.Integer(3);return DefParamTest2($i1,$i2,undefined)}());return $$$cl15.JsCallable($, $.f)})()().equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted parameters class2 named 2",35));
    assert((function(){var $=(function (){var $i1=$$$cl15.Integer(1);var $i3=$$$cl15.Integer(0);return DefParamTest2($i1,undefined,$i3)}());return $$$cl15.JsCallable($, $.f)})()().equals($$$cl15.String("1,2,0",5)),$$$cl15.String("defaulted parameters class2 named 3",35));
    
    //AttributeDeclaration tst at functions.ceylon (108:4-108:31)
    var $tst=DefParamTest3();
    function getTst(){
        return $tst;
    }
    assert((function(){var $=getTst();return $$$cl15.JsCallable($, $.f)})()($$$cl15.Integer(1)).equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted method parameters 1",29));
    assert((function(){var $=getTst();return $$$cl15.JsCallable($, $.f)})()($$$cl15.Integer(1),$$$cl15.Integer(3)).equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted method parameters 2",29));
    assert((function(){var $=getTst();return $$$cl15.JsCallable($, $.f)})()($$$cl15.Integer(1),$$$cl15.Integer(3),$$$cl15.Integer(0)).equals($$$cl15.String("1,3,0",5)),$$$cl15.String("defaulted method parameters 3",29));
    assert((function (){var $i1=$$$cl15.Integer(1);return (function(){var $=getTst();return $$$cl15.JsCallable($, $.f)})()($i1,undefined,undefined)}()).equals($$$cl15.String("1,2,3",5)),$$$cl15.String("defaulted method parameters named 1",35));
    assert((function (){var $i1=$$$cl15.Integer(1);var $i2=$$$cl15.Integer(3);return (function(){var $=getTst();return $$$cl15.JsCallable($, $.f)})()($i1,$i2,undefined)}()).equals($$$cl15.String("1,3,4",5)),$$$cl15.String("defaulted method parameters named 2",35));
    assert((function (){var $i1=$$$cl15.Integer(1);var $i3=$$$cl15.Integer(0);return (function(){var $=getTst();return $$$cl15.JsCallable($, $.f)})()($i1,undefined,$i3)}()).equals($$$cl15.String("1,2,0",5)),$$$cl15.String("defaulted method parameters named 3",35));
}

//MethodDefinition test at functions.ceylon (117:0-127:0)
function test(){
    helloWorld();
    hello($$$cl15.String("test",4));
    helloAll($$$cl15.ArraySequence([$$$cl15.String("Gavin",5),$$$cl15.String("Enrique",7),$$$cl15.String("Ivo",3)]));
    assert(toString($$$cl15.Integer(5)).equals($$$cl15.String("5",1)),$$$cl15.String("toString(obj)",13));
    assert(add($$$cl15.Float(1.5),$$$cl15.Float(2.5)).equals($$$cl15.Float(4.0)),$$$cl15.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
