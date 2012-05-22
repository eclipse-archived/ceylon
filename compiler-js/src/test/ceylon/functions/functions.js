(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.3/ceylon.language');
var $$$a12=require('default/assert');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$104,f$105){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$106 = a$104.getIterator();
    var e$107;while ((e$107=it$106.next())!==$$$cl1.getExhausted()){
        
        if ((f$105(e$107))===$$$cl1.getTrue()){
            return e$107;
        }
        
    }
    return $$$cl1.getNull();
}

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$108,f$109){
    if(f$109===undefined){f$109=function (elem$110){return $$$cl1.getTrue()};}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$111 = a$108.getIterator();
    var e$112;while ((e$112=it$111.next())!==$$$cl1.getExhausted()){
        
        if ((f$109(e$112))===$$$cl1.getTrue()){
            return e$112;
        }
        
    }
    if ($$$cl1.getExhausted() === e$112){
        return $$$cl1.getNull();
    }
    
}

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$113){
    return function (i$114){return i$114.minus(howMuch$113).getString()};
}

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl1.print($$$cl1.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$115=$$$cl1.array($$$cl1.ArraySequence([$$$cl1.Integer(1),$$$cl1.Integer(2),$$$cl1.Integer(3),$$$cl1.Integer(4),$$$cl1.Integer(5)]));
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:56)
    var found$116=find(nums$115,function (i$117){return i$117.remainder($$$cl1.Integer(2)).equals($$$cl1.Integer(0))});
    var setFound$116=function(found$118){found$116=found$118; return found$116;};
    var i$119;
    if((i$119=found$116)!==null){
        $$$a12.assert(i$119.equals($$$cl1.Integer(2)),$$$cl1.String("anonfunc positional",19));
    }
    else {
        $$$a12.fail($$$cl1.String("anonfunc positional",19));
    }
    
    setFound$116((function (){var f$105=function (i$120){
        return i$120.remainder($$$cl1.Integer(2)).equals($$$cl1.Integer(0));
    }
    ;var a$104=nums$115;return find(a$104,f$105)}()));
    var i$121;
    if((i$121=found$116)!==null){
        $$$a12.assert(i$121.equals($$$cl1.Integer(2)),$$$cl1.String("anonfunc named",14));
    }
    else {
        $$$a12.fail($$$cl1.String("anonfunc named",14));
    }
    
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$122(f$123,expect$124){
        $$$a12.assert(f$123($$$cl1.Integer(0)).equals(expect$124),$$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("anon func returns ",18),f$123($$$cl1.Integer(0)).getString(),$$$cl1.String(" instead of ",12),expect$124.getString(),$$$cl1.String("",0)])).getString());
    }
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$125(i$126){
        return i$126.plus($$$cl1.Integer(12)).getString();
    }
    callFunction$122(f$125,$$$cl1.String("12",2));
    callFunction$122(function (i$127){return i$127.times($$$cl1.Integer(3)).getString()},$$$cl1.String("0",1));
    (function (){var expect$124=$$$cl1.String("0",1);var f$123=function (i$128){
        return i$128.power($$$cl1.Integer(2)).getString();
    }
    ;return callFunction$122(f$123,expect$124)}());
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:38)
    var f2$129=function (i$130){return i$130.minus($$$cl1.Integer(10)).getString()};
    callFunction$122(f2$129,$$$cl1.String("-10",3));
    callFunction$122(subtract($$$cl1.Integer(5)),$$$cl1.String("-5",2));
    setFound$116(find2(nums$115,function (i$131){return i$131.compare($$$cl1.Integer(2)).equals($$$cl1.getLarger())}));
    var i$132;
    if((i$132=found$116)!==null){
        $$$a12.assert(i$132.equals($$$cl1.Integer(3)),$$$cl1.String("anonfunc i>2 [1]",16));
    }
    else {
        $$$a12.fail($$$cl1.String("anonfunc i>2 [2]",16));
    }
    
    setFound$116(find2(nums$115));
    var i$133;
    if((i$133=found$116)!==null){
        $$$a12.assert(i$133.equals($$$cl1.Integer(1)),$$$cl1.String("anonfunc defaulted param [1]",28));
    }
    else {
        $$$a12.fail($$$cl1.String("anonfunc defaulted param [2]",28));
    }
    
}
var $$$cl1=require('ceylon/language/0.3/ceylon.language');
var $$$a12=require('default/assert');

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl1.print($$$cl1.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$134){
    $$$cl1.print($$$cl1.String("hello",5).plus(name$134));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:39)
function helloAll(names$135){
    if(names$135===undefined){names$135=$$$cl1.empty;}
    
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$136){
    return obj$136.getString();
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$137,y$138){
    return x$137.plus(y$138);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$139,f$140){
    f$140($$$cl1.Integer(0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-37:0)
function MySequence(seq$141, $$mySequence){
    /* REIFIED GENERICS SOON! <out Element> */
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$mySequence.seq$141=seq$141;
    $$$cl1.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
    var getLastIndex=function(){
        return seq$141.getLastIndex();
    }
    $$mySequence.getLastIndex=getLastIndex;
    
    //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
    var getFirst=function(){
        return seq$141.getFirst();
    }
    $$mySequence.getFirst=getFirst;
    
    //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
    var getRest=function(){
        return seq$141.getRest();
    }
    $$mySequence.getRest=getRest;
    
    //MethodDefinition item at functions.ceylon (30:4-30:68)
    function item(index$142){
        return seq$141.item(index$142);
    }
    $$mySequence.item=item;
    
    //MethodDefinition span at functions.ceylon (31:4-31:89)
    function span(from$143,to$144){
        return seq$141.span(from$143,to$144);
    }
    $$mySequence.span=span;
    
    //MethodDefinition segment at functions.ceylon (32:4-32:102)
    function segment(from$145,length$146){
        return seq$141.segment(from$145,length$146);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (33:4-33:59)
    var getClone=function(){
        return $$mySequence;
    }
    $$mySequence.getClone=getClone;
    
    //AttributeGetterDefinition string at functions.ceylon (34:4-34:53)
    var getString=function(){
        return seq$141.getString();
    }
    $$mySequence.getString=getString;
    
    //AttributeGetterDefinition hash at functions.ceylon (35:4-35:50)
    var getHash=function(){
        return seq$141.getHash();
    }
    $$mySequence.getHash=getHash;
    
    //MethodDefinition equals at functions.ceylon (36:4-36:75)
    function equals(other$147){
        return seq$141.equals(other$147);
    }
    $$mySequence.equals=equals;
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl1.initTypeProto(MySequence,'functions.MySequence',$$$cl1.IdentifiableObject,$$$cl1.Sequence);
    }
    return MySequence;
}
exports.$init$MySequence=$init$MySequence;
$init$MySequence();

//ClassDefinition RefHelper at functions.ceylon (39:0-41:0)
function RefHelper($$refHelper){
    $init$RefHelper();
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    
    //MethodDefinition f at functions.ceylon (40:4-40:47)
    function f(i$148){
        return $$$cl1.getTrue();
    }
    $$refHelper.f=f;
    return $$refHelper;
}
function $init$RefHelper(){
    if (RefHelper.$$===undefined){
        $$$cl1.initTypeProto(RefHelper,'functions.RefHelper',$$$cl1.IdentifiableObject);
    }
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDefinition testMethodReference at functions.ceylon (43:0-51:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (44:4-44:28)
    var obj1$149=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (45:4-45:43)
    var obj2$150=MySequence/* REIFIED GENERICS SOON!! <ceylon.language.String> */($$$cl1.ArraySequence([$$$cl1.String("hi",2)]));
    
    //MethodDefinition tst at functions.ceylon (46:4-48:4)
    function tst$151(x$152){
        return x$152($$$cl1.Integer(0));
    }
    $$$a12.assert(tst$151((function(){var $=obj1$149;return $$$cl1.JsCallable($, $==null?null:$.f)})()),$$$cl1.String("Reference to method",19));
    $$$a12.assert(tst$151((function(){var $=obj2$150;return $$$cl1.JsCallable($, $==null?null:$.defines)})()),$$$cl1.String("Reference to method from ceylon.language",40));
}

//MethodDefinition defParamTest at functions.ceylon (53:0-55:0)
function defParamTest(i1$153,i2$154,i3$155){
    if(i2$154===undefined){i2$154=i1$153.plus($$$cl1.Integer(1));}
    if(i3$155===undefined){i3$155=i1$153.plus(i2$154);}
    return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),i1$153.getString(),$$$cl1.String(",",1),i2$154.getString(),$$$cl1.String(",",1),i3$155.getString(),$$$cl1.String("",0)])).getString();
}

//ClassDefinition DefParamTest1 at functions.ceylon (56:0-58:0)
function DefParamTest1(i1$156, i2$157, i3$158, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$157===undefined){i2$157=i1$156.plus($$$cl1.Integer(1));}
    if(i3$158===undefined){i3$158=i1$156.plus(i2$157);}
    
    //AttributeDeclaration s at functions.ceylon (57:4-57:44)
    var s$159=$$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),i1$156.getString(),$$$cl1.String(",",1),i2$157.getString(),$$$cl1.String(",",1),i3$158.getString(),$$$cl1.String("",0)])).getString();
    var getS=function(){return s$159;};
    $$defParamTest1.getS=getS;
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest1,'functions.DefParamTest1',$$$cl1.IdentifiableObject);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (59:0-61:0)
function DefParamTest2(i1$160, i2$161, i3$162, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$160=i1$160;
    if(i2$161===undefined){i2$161=i1$160.plus($$$cl1.Integer(1));}
    $$defParamTest2.i2$161=i2$161;
    if(i3$162===undefined){i3$162=i1$160.plus(i2$161);}
    $$defParamTest2.i3$162=i3$162;
    
    //MethodDefinition f at functions.ceylon (60:4-60:55)
    function f(){
        return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),i1$160.getString(),$$$cl1.String(",",1),i2$161.getString(),$$$cl1.String(",",1),i3$162.getString(),$$$cl1.String("",0)])).getString();
    }
    $$defParamTest2.f=f;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest2,'functions.DefParamTest2',$$$cl1.IdentifiableObject);
    }
    return DefParamTest2;
}
exports.$init$DefParamTest2=$init$DefParamTest2;
$init$DefParamTest2();

//ClassDefinition DefParamTest3 at functions.ceylon (62:0-66:0)
function DefParamTest3($$defParamTest3){
    $init$DefParamTest3();
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    
    //MethodDefinition f at functions.ceylon (63:4-65:4)
    function f(i1$163,i2$164,i3$165){
        if(i2$164===undefined){i2$164=i1$163.plus($$$cl1.Integer(1));}
        if(i3$165===undefined){i3$165=i1$163.plus(i2$164);}
        return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),i1$163.getString(),$$$cl1.String(",",1),i2$164.getString(),$$$cl1.String(",",1),i3$165.getString(),$$$cl1.String("",0)])).getString();
    }
    $$defParamTest3.f=f;
    return $$defParamTest3;
}
function $init$DefParamTest3(){
    if (DefParamTest3.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest3,'functions.DefParamTest3',$$$cl1.IdentifiableObject);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (67:0-96:0)
function testDefaultedParams(){
    $$$a12.assert(defParamTest($$$cl1.Integer(1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters 1",22));
    $$$a12.assert(defParamTest($$$cl1.Integer(1),$$$cl1.Integer(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters 2",22));
    $$$a12.assert(defParamTest($$$cl1.Integer(1),$$$cl1.Integer(3),$$$cl1.Integer(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters 3",22));
    $$$a12.assert((function (){var i1$153=$$$cl1.Integer(1);return defParamTest(i1$153,undefined,undefined)}()).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters named 1",28));
    $$$a12.assert((function (){var i1$153=$$$cl1.Integer(1);var i2$154=$$$cl1.Integer(3);return defParamTest(i1$153,i2$154,undefined)}()).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters named 2",28));
    $$$a12.assert((function (){var i1$153=$$$cl1.Integer(1);var i3$155=$$$cl1.Integer(0);return defParamTest(i1$153,undefined,i3$155)}()).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters named 3",28));
    $$$a12.assert(DefParamTest1($$$cl1.Integer(1)).getS().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class 1",28));
    $$$a12.assert(DefParamTest1($$$cl1.Integer(1),$$$cl1.Integer(3)).getS().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class 2",28));
    $$$a12.assert(DefParamTest1($$$cl1.Integer(1),$$$cl1.Integer(3),$$$cl1.Integer(0)).getS().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class 3",28));
    $$$a12.assert((function (){var i1$156=$$$cl1.Integer(1);return DefParamTest1(i1$156,undefined,undefined)}()).getS().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class named 1",34));
    $$$a12.assert((function (){var i1$156=$$$cl1.Integer(1);var i2$157=$$$cl1.Integer(3);return DefParamTest1(i1$156,i2$157,undefined)}()).getS().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class named 2",34));
    $$$a12.assert((function (){var i1$156=$$$cl1.Integer(1);var i3$158=$$$cl1.Integer(0);return DefParamTest1(i1$156,undefined,i3$158)}()).getS().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class named 3",34));
    $$$a12.assert(DefParamTest2($$$cl1.Integer(1)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 1",29));
    $$$a12.assert(DefParamTest2($$$cl1.Integer(1),$$$cl1.Integer(3)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 2",29));
    $$$a12.assert(DefParamTest2($$$cl1.Integer(1),$$$cl1.Integer(3),$$$cl1.Integer(0)).f().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class2 3",29));
    $$$a12.assert((function (){var i1$160=$$$cl1.Integer(1);return DefParamTest2(i1$160,undefined,undefined)}()).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 named 1",35));
    $$$a12.assert((function (){var i1$160=$$$cl1.Integer(1);var i2$161=$$$cl1.Integer(3);return DefParamTest2(i1$160,i2$161,undefined)}()).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 named 2",35));
    $$$a12.assert((function (){var i1$160=$$$cl1.Integer(1);var i3$162=$$$cl1.Integer(0);return DefParamTest2(i1$160,undefined,i3$162)}()).f().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class2 named 3",35));
    
    //AttributeDeclaration tst at functions.ceylon (89:4-89:31)
    var tst$166=DefParamTest3();
    $$$a12.assert(tst$166.f($$$cl1.Integer(1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters 1",29));
    $$$a12.assert(tst$166.f($$$cl1.Integer(1),$$$cl1.Integer(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters 2",29));
    $$$a12.assert(tst$166.f($$$cl1.Integer(1),$$$cl1.Integer(3),$$$cl1.Integer(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted method parameters 3",29));
    $$$a12.assert((function (){var i1$163=$$$cl1.Integer(1);return (function(){var $=tst$166;return $$$cl1.JsCallable($, $==null?null:$.f)})()(i1$163,undefined,undefined)}()).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters named 1",35));
    $$$a12.assert((function (){var i1$163=$$$cl1.Integer(1);var i2$164=$$$cl1.Integer(3);return (function(){var $=tst$166;return $$$cl1.JsCallable($, $==null?null:$.f)})()(i1$163,i2$164,undefined)}()).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters named 2",35));
    $$$a12.assert((function (){var i1$163=$$$cl1.Integer(1);var i3$165=$$$cl1.Integer(0);return (function(){var $=tst$166;return $$$cl1.JsCallable($, $==null?null:$.f)})()(i1$163,undefined,i3$165)}()).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted method parameters named 3",35));
}

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (98:0-107:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (99:2-102:2)
    function GetterTest$167($$getterTest$167){
        $init$GetterTest$167();
        if ($$getterTest$167===undefined)$$getterTest$167=new GetterTest$167.$$;
        
        //AttributeDeclaration i at functions.ceylon (100:4-100:25)
        var i$168=$$$cl1.Integer(0);
        var getI$168=function(){return i$168;};
        $$getterTest$167.getI$168=getI$168;
        var setI$168=function(i$169){i$168=i$169; return i$168;};
        $$getterTest$167.setI$168=setI$168;
        
        //AttributeGetterDefinition x at functions.ceylon (101:4-101:35)
        var getX=function(){
            return (setI$168(getI$168().getSuccessor()),getI$168());
        }
        $$getterTest$167.getX=getX;
        return $$getterTest$167;
    }
    function $init$GetterTest$167(){
        if (GetterTest$167.$$===undefined){
            $$$cl1.initTypeProto(GetterTest$167,'functions.testGetterMethodDefinitions.GetterTest',$$$cl1.IdentifiableObject);
        }
        return GetterTest$167;
    }
    $init$GetterTest$167();
    
    //AttributeDeclaration gt at functions.ceylon (103:2-103:25)
    var gt$170=GetterTest$167();
    $$$a12.assert(gt$170.getX().equals($$$cl1.Integer(1)),$$$cl1.String("getter defined as method 1",26));
    $$$a12.assert(gt$170.getX().equals($$$cl1.Integer(2)),$$$cl1.String("getter defined as method 2",26));
    $$$a12.assert(gt$170.getX().equals($$$cl1.Integer(3)),$$$cl1.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition test at functions.ceylon (109:0-122:0)
function test(){
    helloWorld();
    hello($$$cl1.String("test",4));
    helloAll($$$cl1.ArraySequence([$$$cl1.String("Gavin",5),$$$cl1.String("Enrique",7),$$$cl1.String("Ivo",3)]));
    $$$a12.assert(toString($$$cl1.Integer(5)).equals($$$cl1.String("5",1)),$$$cl1.String("toString(obj)",13));
    $$$a12.assert(add($$$cl1.Float(1.5),$$$cl1.Float(2.5)).equals($$$cl1.Float(4.0)),$$$cl1.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    $$$a12.results();
}
exports.test=test;
var $$$cl1=require('ceylon/language/0.3/ceylon.language');
var $$$a12=require('default/assert');

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$171,y$172){
        return x$171.compare(y$172);
    }
}

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$173){
    return function(apat$174){
        return function(amat$175){
            return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),nombre$173.getString(),$$$cl1.String(" ",1),apat$174.getString(),$$$cl1.String(" ",1),amat$175.getString(),$$$cl1.String("",0)])).getString();
        }
    }
}

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$176){
    if(name$176===undefined){name$176=$$$cl1.String("A",1);}
    return function(apat$177){
        return function(amat$178){
            return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),name$176.getString(),$$$cl1.String(" ",1),apat$177.getString(),$$$cl1.String(" ",1),amat$178.getString(),$$$cl1.String("",0)])).getString();
        }
    }
}

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$179){
    if(names$179===undefined){names$179=$$$cl1.empty;}
    return function(count$180){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$181=$$$cl1.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$182 = names$179.getIterator();
        var name$183;while ((name$183=it$182.next())!==$$$cl1.getExhausted()){
            
            sb$181.append(name$183).append($$$cl1.String(" ",1));
        }
        sb$181.append($$$cl1.String("count ",6)).append(count$180.getString());
        return sb$181.getString();
    }
}

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl1.print($$$cl1.String("Testing multiple parameter lists...",35));
    $$$a12.assert(multiCompare()($$$cl1.Integer(1),$$$cl1.Integer(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 1",15));
    $$$a12.assert(multiCompare()($$$cl1.Integer(1),$$$cl1.Integer(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 2",15));
    $$$a12.assert(multiCompare()($$$cl1.Integer(2),$$$cl1.Integer(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:54)
    var comp$184=multiCompare();
    $$$a12.assert(comp$184($$$cl1.Integer(1),$$$cl1.Integer(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 4",15));
    $$$a12.assert(comp$184($$$cl1.Integer(1),$$$cl1.Integer(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 5",15));
    $$$a12.assert(comp$184($$$cl1.Integer(2),$$$cl1.Integer(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 6",15));
    $$$a12.assert(multiFullname($$$cl1.String("a",1))($$$cl1.String("b",1))($$$cl1.String("c",1)).equals($$$cl1.String("a b c",5)),$$$cl1.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:51)
    var apat$185=multiFullname($$$cl1.String("A",1))($$$cl1.String("B",1));
    $$$a12.assert(apat$185($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:54)
    var nombre$186=multiFullname($$$cl1.String("Name",4));
    $$$a12.assert($$$cl1.isOfType(nombre$186($$$cl1.String("Z",1)),'ceylon.language.Callable')/* REIFIED GENERICS SOON!!! term Type[String(String)] model {TypeParameter[Callable#CallableArgument]=Type[String], TypeParameter[Callable#Return]=Type[String]}<ceylon.language.String> <ceylon.language.String> */,$$$cl1.String("Multi callable 1",16));
    $$$a12.assert(nombre$186($$$cl1.String("Z",1))($$$cl1.String("L",1)).equals($$$cl1.String("Name Z L",8)),$$$cl1.String("Multi callable 2",16));
    $$$a12.assert(multiDefaulted()($$$cl1.String("B",1))($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:65)
    var md1$187=multiDefaulted();
    $$$a12.assert(md1$187($$$cl1.String("B",1))($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 2",17));
    $$$a12.assert(md1$187($$$cl1.String("B",1))($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 3",17));
    $$$a12.assert(md1$187($$$cl1.String("Z",1))($$$cl1.String("C",1)).equals($$$cl1.String("A Z C",5)),$$$cl1.String("Multi defaulted 4",17));
    $$$a12.assert(md1$187($$$cl1.String("Y",1))($$$cl1.String("Z",1)).equals($$$cl1.String("A Y Z",5)),$$$cl1.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:48)
    var md2$188=multiDefaulted()($$$cl1.String("B",1));
    $$$a12.assert(md2$188($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 6",17));
    $$$a12.assert(md2$188($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 7",17));
    $$$a12.assert(multiSequenced($$$cl1.ArraySequence([$$$cl1.String("A",1),$$$cl1.String("B",1),$$$cl1.String("C",1)]))($$$cl1.Integer(1)).equals($$$cl1.String("A B C count 1",13)),$$$cl1.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:55)
    var ms1$189=multiSequenced($$$cl1.ArraySequence([$$$cl1.String("x",1),$$$cl1.String("y",1),$$$cl1.String("z",1)]));
    $$$a12.assert(ms1$189($$$cl1.Integer(5)).equals($$$cl1.String("x y z count 5",13)),$$$cl1.String("Multi sequenced 2",17));
    $$$a12.assert(ms1$189($$$cl1.Integer(10)).equals($$$cl1.String("x y z count 10",14)),$$$cl1.String("Multi sequenced 3",17));
}
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
