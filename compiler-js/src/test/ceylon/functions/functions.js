(function(define) { define(function(require, exports, module) {
var $$$cl115=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at functions.ceylon (1:0-1:34)
var assertionCount$116=$$$cl115.Integer(0);
var getAssertionCount=function(){return assertionCount$116;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount$117){assertionCount$116=assertionCount$117; return assertionCount$116;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at functions.ceylon (2:0-2:32)
var failureCount$118=$$$cl115.Integer(0);
var getFailureCount=function(){return failureCount$118;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount$119){failureCount$118=failureCount$119; return failureCount$118;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at functions.ceylon (4:0-10:0)
function assert(assertion$120,message$121){
    if(message$121===undefined){message$121=$$$cl115.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl115.Integer(1))),getAssertionCount());
    if ((assertion$120.equals($$$cl115.getFalse()))===$$$cl115.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl115.Integer(1))),getFailureCount());
        $$$cl115.print($$$cl115.StringBuilder().appendAll($$$cl115.ArraySequence([$$$cl115.String("assertion failed \""),message$121.getString(),$$$cl115.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at functions.ceylon (12:0-14:0)
function fail(message$122){
    assert($$$cl115.getFalse(),message$122);
}
exports.fail=fail;

//MethodDefinition results at functions.ceylon (16:0-19:0)
function results(){
    $$$cl115.print($$$cl115.StringBuilder().appendAll($$$cl115.ArraySequence([$$$cl115.String("assertions ",11),getAssertionCount().getString(),$$$cl115.String(", failures ",11),getFailureCount().getString(),$$$cl115.String("",0)])).getString());
}
exports.results=results;

//MethodDefinition helloWorld at functions.ceylon (22:0-24:0)
function helloWorld(){
    $$$cl115.print($$$cl115.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (26:0-28:0)
function hello(name$123){
    $$$cl115.print($$$cl115.String("hello",5).plus(name$123));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (30:0-30:39)
function helloAll(names$124){
    if(names$124===undefined){names$124=$$$cl115.empty;}
    
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (32:0-34:0)
function toString(obj$125){
    return obj$125.getString();
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (36:0-38:0)
function add(x$126,y$127){
    return x$126.plus(y$127);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (40:0-42:0)
function repeat(times$128,f$129){
    f$129($$$cl115.Integer(0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (44:0-56:0)
function MySequence(seq$130, $$mySequence){
    /* REIFIED GENERICS SOON! <out Element> */
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$mySequence.seq$130=seq$130;
    $$$cl115.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (46:4-46:60)
    var getLastIndex=function(){
        return seq$130.getLastIndex();
    }
    $$mySequence.getLastIndex=getLastIndex;
    
    //AttributeGetterDefinition first at functions.ceylon (47:4-47:52)
    var getFirst=function(){
        return seq$130.getFirst();
    }
    $$mySequence.getFirst=getFirst;
    
    //AttributeGetterDefinition rest at functions.ceylon (48:4-48:52)
    var getRest=function(){
        return seq$130.getRest();
    }
    $$mySequence.getRest=getRest;
    
    //MethodDefinition item at functions.ceylon (49:4-49:68)
    function item(index$131){
        return seq$130.item(index$131);
    }
    $$mySequence.item=item;
    
    //MethodDefinition span at functions.ceylon (50:4-50:89)
    function span(from$132,to$133){
        return seq$130.span(from$132,to$133);
    }
    $$mySequence.span=span;
    
    //MethodDefinition segment at functions.ceylon (51:4-51:102)
    function segment(from$134,length$135){
        return seq$130.segment(from$134,length$135);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (52:4-52:59)
    var getClone=function(){
        return $$mySequence;
    }
    $$mySequence.getClone=getClone;
    
    //AttributeGetterDefinition string at functions.ceylon (53:4-53:53)
    var getString=function(){
        return seq$130.getString();
    }
    $$mySequence.getString=getString;
    
    //AttributeGetterDefinition hash at functions.ceylon (54:4-54:50)
    var getHash=function(){
        return seq$130.getHash();
    }
    $$mySequence.getHash=getHash;
    
    //MethodDefinition equals at functions.ceylon (55:4-55:75)
    function equals(other$136){
        return seq$130.equals(other$136);
    }
    $$mySequence.equals=equals;
    return $$mySequence;
}
$$$cl115.initTypeProto(MySequence,'functions.MySequence',$$$cl115.IdentifiableObject,$$$cl115.Sequence);

//ClassDefinition RefHelper at functions.ceylon (58:0-60:0)
function RefHelper($$refHelper){
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    
    //MethodDefinition f at functions.ceylon (59:4-59:47)
    function f(i$137){
        return $$$cl115.getTrue();
    }
    $$refHelper.f=f;
    return $$refHelper;
}
$$$cl115.initTypeProto(RefHelper,'functions.RefHelper',$$$cl115.IdentifiableObject);

//MethodDefinition testMethodReference at functions.ceylon (62:0-70:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (63:4-63:28)
    var obj1$138=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (64:4-64:43)
    var obj2$139=MySequence/* REIFIED GENERICS SOON!! <ceylon.language.String> */($$$cl115.ArraySequence([$$$cl115.String("hi",2)]));
    
    //MethodDefinition tst at functions.ceylon (65:4-67:4)
    function tst$140(x$141){
        return x$141($$$cl115.Integer(0));
    }
    assert(tst$140((function(){var $=obj1$138;return $$$cl115.JsCallable($, $.f)})()),$$$cl115.String("Reference to method",19));
    assert(tst$140((function(){var $=obj2$139;return $$$cl115.JsCallable($, $.defines)})()),$$$cl115.String("Reference to method from ceylon.language",40));
}

//MethodDefinition defParamTest at functions.ceylon (72:0-74:0)
function defParamTest(i1$142,i2$143,i3$144){
    if(i2$143===undefined){i2$143=i1$142.plus($$$cl115.Integer(1));}
    if(i3$144===undefined){i3$144=i1$142.plus(i2$143);}
    return $$$cl115.StringBuilder().appendAll($$$cl115.ArraySequence([$$$cl115.String("",0),i1$142.getString(),$$$cl115.String(",",1),i2$143.getString(),$$$cl115.String(",",1),i3$144.getString(),$$$cl115.String("",0)])).getString();
}

//ClassDefinition DefParamTest1 at functions.ceylon (75:0-77:0)
function DefParamTest1(i1$145, i2$146, i3$147, $$defParamTest1){
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$146===undefined){i2$146=i1$145.plus($$$cl115.Integer(1));}
    if(i3$147===undefined){i3$147=i1$145.plus(i2$146);}
    
    //AttributeDeclaration s at functions.ceylon (76:4-76:44)
    var s$148=$$$cl115.StringBuilder().appendAll($$$cl115.ArraySequence([$$$cl115.String("",0),i1$145.getString(),$$$cl115.String(",",1),i2$146.getString(),$$$cl115.String(",",1),i3$147.getString(),$$$cl115.String("",0)])).getString();
    var getS=function(){return s$148;};
    $$defParamTest1.getS=getS;
    return $$defParamTest1;
}
$$$cl115.initTypeProto(DefParamTest1,'functions.DefParamTest1',$$$cl115.IdentifiableObject);

//ClassDefinition DefParamTest2 at functions.ceylon (78:0-80:0)
function DefParamTest2(i1$149, i2$150, i3$151, $$defParamTest2){
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$149=i1$149;
    if(i2$150===undefined){i2$150=i1$149.plus($$$cl115.Integer(1));}
    $$defParamTest2.i2$150=i2$150;
    if(i3$151===undefined){i3$151=i1$149.plus(i2$150);}
    $$defParamTest2.i3$151=i3$151;
    
    //MethodDefinition f at functions.ceylon (79:4-79:55)
    function f(){
        return $$$cl115.StringBuilder().appendAll($$$cl115.ArraySequence([$$$cl115.String("",0),i1$149.getString(),$$$cl115.String(",",1),i2$150.getString(),$$$cl115.String(",",1),i3$151.getString(),$$$cl115.String("",0)])).getString();
    }
    $$defParamTest2.f=f;
    return $$defParamTest2;
}
$$$cl115.initTypeProto(DefParamTest2,'functions.DefParamTest2',$$$cl115.IdentifiableObject);

//ClassDefinition DefParamTest3 at functions.ceylon (81:0-85:0)
function DefParamTest3($$defParamTest3){
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    
    //MethodDefinition f at functions.ceylon (82:4-84:4)
    function f(i1$152,i2$153,i3$154){
        if(i2$153===undefined){i2$153=i1$152.plus($$$cl115.Integer(1));}
        if(i3$154===undefined){i3$154=i1$152.plus(i2$153);}
        return $$$cl115.StringBuilder().appendAll($$$cl115.ArraySequence([$$$cl115.String("",0),i1$152.getString(),$$$cl115.String(",",1),i2$153.getString(),$$$cl115.String(",",1),i3$154.getString(),$$$cl115.String("",0)])).getString();
    }
    $$defParamTest3.f=f;
    return $$defParamTest3;
}
$$$cl115.initTypeProto(DefParamTest3,'functions.DefParamTest3',$$$cl115.IdentifiableObject);

//MethodDefinition testDefaultedParams at functions.ceylon (86:0-115:0)
function testDefaultedParams(){
    assert(defParamTest($$$cl115.Integer(1)).equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted parameters 1",22));
    assert(defParamTest($$$cl115.Integer(1),$$$cl115.Integer(3)).equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted parameters 2",22));
    assert(defParamTest($$$cl115.Integer(1),$$$cl115.Integer(3),$$$cl115.Integer(0)).equals($$$cl115.String("1,3,0",5)),$$$cl115.String("defaulted parameters 3",22));
    assert((function (){var i1$142=$$$cl115.Integer(1);return defParamTest(i1$142,undefined,undefined)}()).equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted parameters named 1",28));
    assert((function (){var i1$142=$$$cl115.Integer(1);var i2$143=$$$cl115.Integer(3);return defParamTest(i1$142,i2$143,undefined)}()).equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted parameters named 2",28));
    assert((function (){var i1$142=$$$cl115.Integer(1);var i3$144=$$$cl115.Integer(0);return defParamTest(i1$142,undefined,i3$144)}()).equals($$$cl115.String("1,2,0",5)),$$$cl115.String("defaulted parameters named 3",28));
    assert(DefParamTest1($$$cl115.Integer(1)).getS().equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted parameters class 1",28));
    assert(DefParamTest1($$$cl115.Integer(1),$$$cl115.Integer(3)).getS().equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted parameters class 2",28));
    assert(DefParamTest1($$$cl115.Integer(1),$$$cl115.Integer(3),$$$cl115.Integer(0)).getS().equals($$$cl115.String("1,3,0",5)),$$$cl115.String("defaulted parameters class 3",28));
    assert((function (){var i1$145=$$$cl115.Integer(1);return DefParamTest1(i1$145,undefined,undefined)}()).getS().equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted parameters class named 1",34));
    assert((function (){var i1$145=$$$cl115.Integer(1);var i2$146=$$$cl115.Integer(3);return DefParamTest1(i1$145,i2$146,undefined)}()).getS().equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted parameters class named 2",34));
    assert((function (){var i1$145=$$$cl115.Integer(1);var i3$147=$$$cl115.Integer(0);return DefParamTest1(i1$145,undefined,i3$147)}()).getS().equals($$$cl115.String("1,2,0",5)),$$$cl115.String("defaulted parameters class named 3",34));
    assert(DefParamTest2($$$cl115.Integer(1)).f().equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted parameters class2 1",29));
    assert(DefParamTest2($$$cl115.Integer(1),$$$cl115.Integer(3)).f().equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted parameters class2 2",29));
    assert(DefParamTest2($$$cl115.Integer(1),$$$cl115.Integer(3),$$$cl115.Integer(0)).f().equals($$$cl115.String("1,3,0",5)),$$$cl115.String("defaulted parameters class2 3",29));
    assert((function (){var i1$149=$$$cl115.Integer(1);return DefParamTest2(i1$149,undefined,undefined)}()).f().equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted parameters class2 named 1",35));
    assert((function (){var i1$149=$$$cl115.Integer(1);var i2$150=$$$cl115.Integer(3);return DefParamTest2(i1$149,i2$150,undefined)}()).f().equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted parameters class2 named 2",35));
    assert((function (){var i1$149=$$$cl115.Integer(1);var i3$151=$$$cl115.Integer(0);return DefParamTest2(i1$149,undefined,i3$151)}()).f().equals($$$cl115.String("1,2,0",5)),$$$cl115.String("defaulted parameters class2 named 3",35));
    
    //AttributeDeclaration tst at functions.ceylon (108:4-108:31)
    var tst$155=DefParamTest3();
    assert(tst$155.f($$$cl115.Integer(1)).equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted method parameters 1",29));
    assert(tst$155.f($$$cl115.Integer(1),$$$cl115.Integer(3)).equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted method parameters 2",29));
    assert(tst$155.f($$$cl115.Integer(1),$$$cl115.Integer(3),$$$cl115.Integer(0)).equals($$$cl115.String("1,3,0",5)),$$$cl115.String("defaulted method parameters 3",29));
    assert((function (){var i1$152=$$$cl115.Integer(1);return (function(){var $=tst$155;return $$$cl115.JsCallable($, $.f)})()(i1$152,undefined,undefined)}()).equals($$$cl115.String("1,2,3",5)),$$$cl115.String("defaulted method parameters named 1",35));
    assert((function (){var i1$152=$$$cl115.Integer(1);var i2$153=$$$cl115.Integer(3);return (function(){var $=tst$155;return $$$cl115.JsCallable($, $.f)})()(i1$152,i2$153,undefined)}()).equals($$$cl115.String("1,3,4",5)),$$$cl115.String("defaulted method parameters named 2",35));
    assert((function (){var i1$152=$$$cl115.Integer(1);var i3$154=$$$cl115.Integer(0);return (function(){var $=tst$155;return $$$cl115.JsCallable($, $.f)})()(i1$152,undefined,i3$154)}()).equals($$$cl115.String("1,2,0",5)),$$$cl115.String("defaulted method parameters named 3",35));
}

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (117:0-126:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (118:2-121:2)
    function GetterTest$156($$getterTest$156){
        if ($$getterTest$156===undefined)$$getterTest$156=new GetterTest$156.$$;
        
        //AttributeDeclaration i at functions.ceylon (119:4-119:25)
        var i$157=$$$cl115.Integer(0);
        var getI$157=function(){return i$157;};
        $$getterTest$156.getI$157=getI$157;
        var setI$157=function(i$158){i$157=i$158; return i$157;};
        $$getterTest$156.setI$157=setI$157;
        
        //AttributeGetterDefinition x at functions.ceylon (120:4-120:35)
        var getX=function(){
            return (setI$157(getI$157().getSuccessor()),getI$157());
        }
        $$getterTest$156.getX=getX;
        return $$getterTest$156;
    }
    $$$cl115.initTypeProto(GetterTest$156,'functions.testGetterMethodDefinitions.GetterTest',$$$cl115.IdentifiableObject);
    
    //AttributeDeclaration gt at functions.ceylon (122:2-122:25)
    var gt$159=GetterTest$156();
    assert(gt$159.getX().equals($$$cl115.Integer(1)),$$$cl115.String("getter defined as method 1",26));
    assert(gt$159.getX().equals($$$cl115.Integer(2)),$$$cl115.String("getter defined as method 2",26));
    assert(gt$159.getX().equals($$$cl115.Integer(3)),$$$cl115.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition test at functions.ceylon (128:0-139:0)
function test(){
    helloWorld();
    hello($$$cl115.String("test",4));
    helloAll($$$cl115.ArraySequence([$$$cl115.String("Gavin",5),$$$cl115.String("Enrique",7),$$$cl115.String("Ivo",3)]));
    assert(toString($$$cl115.Integer(5)).equals($$$cl115.String("5",1)),$$$cl115.String("toString(obj)",13));
    assert(add($$$cl115.Float(1.5),$$$cl115.Float(2.5)).equals($$$cl115.Float(4.0)),$$$cl115.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
