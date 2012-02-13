var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition expect at functions.ceylon (1:0-8:0)
function expect(actual,expected,text){
    if ((actual.equals(expected))===$$$cl15.getTrue()){
        $$$cl15.print($$$cl15.String("[ok] ",5).plus(text).plus($$$cl15.String(": '",3)).plus(actual.getString()).plus($$$cl15.String("'",1)));
    }
    else {
        $$$cl15.print($$$cl15.String("[NOT OK] ",9).plus(text).plus($$$cl15.String(": actual='",10)).plus(actual.getString()).plus($$$cl15.String("', expected='",13)).plus(expected.getString()).plus($$$cl15.String("'",1)));
    }
    
}

//MethodDefinition helloWorld at functions.ceylon (10:0-12:0)
function helloWorld(){
    $$$cl15.print($$$cl15.String("hello world",11));
}
this.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (14:0-16:0)
function hello(name){
    $$$cl15.print($$$cl15.String("hello",5).plus(name));
}
this.hello=hello;

//MethodDefinition helloAll at functions.ceylon (18:0-18:39)
function helloAll(names){}
this.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (20:0-22:0)
function toString(obj){
    return obj.getString();
}
this.toString=toString;

//MethodDefinition add at functions.ceylon (24:0-26:0)
function add(x,y){
    return x.plus(y);
}
this.add=add;

//MethodDefinition repeat at functions.ceylon (28:0-30:0)
function repeat(times,f){
    f($$$cl15.Integer(0));
}
this.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (32:0-42:0)
function $MySequence(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $MySequence.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$MySequence.prototype[$+'$CeylonObject$']=$m}
}
for(var $ in $$$cl15.$Sequence.prototype){
    var $m=$$$cl15.$Sequence.prototype[$];
    $MySequence.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$MySequence.prototype[$+'$$$cl15.$Sequence$']=$m}
}
function MySequence(seq, $$mySequence){
    if ($$mySequence===undefined)$$mySequence=new $MySequence;
    $$$cl15.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (34:4-34:60)
    function getLastIndex(){
        return seq.getLastIndex();
    }
    $$mySequence.getLastIndex=getLastIndex;
    
    //AttributeGetterDefinition first at functions.ceylon (35:4-35:52)
    function getFirst(){
        return seq.getFirst();
    }
    $$mySequence.getFirst=getFirst;
    
    //AttributeGetterDefinition rest at functions.ceylon (36:4-36:52)
    function getRest(){
        return seq.getRest();
    }
    $$mySequence.getRest=getRest;
    
    //MethodDefinition item at functions.ceylon (37:4-37:68)
    function item(index){
        return seq.item(index);
    }
    $$mySequence.item=item;
    
    //MethodDefinition span at functions.ceylon (38:4-38:89)
    function span(from,to){
        return seq.span(from,to);
    }
    $$mySequence.span=span;
    
    //MethodDefinition segment at functions.ceylon (39:4-39:102)
    function segment(from,length){
        return seq.segment(from,length);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (40:4-40:59)
    function getClone(){
        return $$mySequence;
    }
    $$mySequence.getClone=getClone;
    
    //AttributeGetterDefinition string at functions.ceylon (41:4-41:53)
    function getString(){
        return seq.getString();
    }
    $$mySequence.getString=getString;
    return $$mySequence;
}

//ClassDefinition RefHelper at functions.ceylon (44:0-46:0)
function $RefHelper(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $RefHelper.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$RefHelper.prototype[$+'$CeylonObject$']=$m}
}
function RefHelper($$refHelper){
    if ($$refHelper===undefined)$$refHelper=new $RefHelper;
    
    //MethodDefinition f at functions.ceylon (45:4-45:47)
    function f(i){
        return $$$cl15.getTrue();
    }
    $$refHelper.f=f;
    return $$refHelper;
}

//MethodDefinition testMethodReference at functions.ceylon (48:0-56:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (49:4-49:28)
    var $obj1=RefHelper();
    function getObj1(){
        return $obj1;
    }
    
    //AttributeDeclaration obj2 at functions.ceylon (50:4-50:43)
    var $obj2=MySequence($$$cl15.ArraySequence([$$$cl15.String("hi",2)]));
    function getObj2(){
        return $obj2;
    }
    
    //MethodDefinition tst at functions.ceylon (51:4-53:4)
    function tst(x){
        return x($$$cl15.Integer(0));
    }
    expect(tst(getObj1().f),$$$cl15.getTrue(),$$$cl15.String("Reference to method",19));
    expect(tst(function(){var $=getObj2();return $.defines.apply($,arguments)}),$$$cl15.getTrue(),$$$cl15.String("Reference to method from ceylon.language",40));
}

//MethodDefinition test at functions.ceylon (58:0-60:0)
function test(){
    testMethodReference();
}
this.test=test;
