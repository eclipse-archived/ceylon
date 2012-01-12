var $$$cl15=require('ceylon/language/0.1/ceylon.language');
var $$$m7=require('default/members');

//MethodDefinition testit at testit.ceylon (3:0-35:0)
function testit(){
    
    //AttributeDeclaration name at testit.ceylon (4:4-4:24)
    var $name=$$$cl15.String("hello",5);
    function getName(){
        return $name;
    }
    $$$cl15.print(getName());
    
    //AttributeDeclaration foo at testit.ceylon (6:4-6:24)
    var $foo=F($$$cl15.String("goodbye",7));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=$$$cl15.String("y",1);var $x=$$$cl15.String("x",1);return printBoth($x,$y)}());
    getFoo().inc();
    getFoo().inc();
    $$$cl15.print(getFoo().getCount());
    getFoo().printName();
    Bar().printName();
    Bar().Inner();
    doIt(function(){var $=getFoo();$.inc.apply($,arguments)});
    $$$cl15.print(getFoo().getCount());
    doIt(Bar);
    $$$cl15.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (18:4-22:4)
    function $x(){}
    for(var $ in CeylonObject.prototype){
        var $m=CeylonObject.prototype[$];
        $x.prototype[$]=$m;
        if($.charAt($.length-1)!=='$'){$x.prototype[$+'$CeylonObject$']=$m}
    }
    
    //MethodDefinition y at testit.ceylon (19:8-21:8)
    $x.prototype.y=function y(){
        var $$x=this;
        $$$cl15.print($$$cl15.String("xy",2));
    }
    var $x=function x(){
        var $$x=new $x;
        return $$x;
    }();
    function getX(){
        return $x;
    }
    getX().y();
    
    //AttributeDeclaration b at testit.ceylon (24:4-24:17)
    var $b=Bar();
    function getB(){
        return $b;
    }
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    $$$cl15.print(getB().getCount());
    printAll($$$cl15.ArraySequence([$$$cl15.String("hello",5),$$$cl15.String("world",5)]));
    (function (){var $strings=$$$cl15.ArraySequence([$$$cl15.String("hello",5),$$$cl15.String("world",5)]);return printAll($strings)}());
    
    //AttributeDeclaration c at testit.ceylon (32:4-32:26)
    var $c=$$$m7.Counter($$$cl15.Integer(0));
    function getC(){
        return $c;
    }
    getC().inc();
    getC().inc();
    $$$cl15.print(getC().getCount());
}
this.testit=testit;
var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function $X(){}

//MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
$X.prototype.helloWorld=function helloWorld(){
    var $$x=this;
    $$$cl15.print($$$cl15.String("hello world",11));
}
function X($$x){
    if ($$x===undefined)$$x=new $X;
    return $$x;
}
this.X=X;

//ClassDefinition Foo at misc.ceylon (7:0-16:0)
function $Foo(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $Foo.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Foo.prototype[$+'$CeylonObject$']=$m}
}

//AttributeDeclaration name at misc.ceylon (8:4-8:29)
$Foo.prototype.getName=function getName(){
    return this.name;
}

//AttributeDeclaration counter at misc.ceylon (9:4-9:29)
$Foo.prototype.getCounter$Foo$=function getCounter$Foo$(){
    return this.counter$Foo;
}
$Foo.prototype.setCounter$Foo$=function setCounter$Foo$(counter){
    this.counter$Foo=counter;
}

//AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
$Foo.prototype.getCount=function getCount(){
    var $$foo=this;
    return $$foo.getCounter$Foo$();
}

//MethodDefinition inc at misc.ceylon (11:4-11:44)
$Foo.prototype.inc=function inc(){
    var $$foo=this;
    $$foo.setCounter$Foo$($$foo.getCounter$Foo$().plus($$$cl15.Integer(1)));
}

//MethodDefinition printName at misc.ceylon (12:4-14:4)
$Foo.prototype.printName=function printName(){
    var $$foo=this;
    $$$cl15.print($$$cl15.String("foo name = ",11).plus($$foo.name$Foo));
}
function Foo(name$Foo, $$foo){
    if ($$foo===undefined)$$foo=new $Foo;
    $$foo.name$Foo=name$Foo;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:29)
    $$foo.name=$$foo.name$Foo;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    $$foo.counter$Foo=$$$cl15.Integer(0);
    $$foo.inc();
    return $$foo;
}
this.Foo=Foo;

//ClassDefinition Bar at misc.ceylon (18:0-32:0)
function $Bar(){}
for(var $ in $Foo.prototype){
    var $m=$Foo.prototype[$];
    $Bar.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Bar.prototype[$+'$Foo$']=$m}
}
for(var $ in $X.prototype){
    var $m=$X.prototype[$];
    $Bar.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$Bar.prototype[$+'$X$']=$m}
}

//MethodDefinition printName at misc.ceylon (19:4-22:4)
$Bar.prototype.printName=function printName(){
    var $$bar=this;
    $$$cl15.print($$$cl15.String("bar name = ",11).plus($$bar.getName()));
    $$bar.printName$Foo$();
}
function Bar($$bar){
    if ($$bar===undefined)$$bar=new $Bar;
    Foo($$$cl15.String("Hello",5),$$bar);
    X($$bar);
    
    //ClassDefinition Inner at misc.ceylon (23:4-29:4)
    function $Inner(){}
    for(var $ in CeylonObject.prototype){
        var $m=CeylonObject.prototype[$];
        $Inner.prototype[$]=$m;
        if($.charAt($.length-1)!=='$'){$Inner.prototype[$+'$CeylonObject$']=$m}
    }
    
    //MethodDefinition incOuter at misc.ceylon (26:8-28:8)
    $Inner.prototype.incOuter=function incOuter(){
        var $$inner=this;
        $$bar.inc();
    }
    function Inner($$inner){
        if ($$inner===undefined)$$inner=new $Inner;
        $$$cl15.print($$$cl15.String("creating inner class of :",25).plus($$bar.getName()));
        return $$inner;
    }
    $$bar.Inner=Inner;
    return $$bar;
}
this.Bar=Bar;

//MethodDefinition printBoth at misc.ceylon (34:0-36:0)
function printBoth(x,y){
    $$$cl15.print(x.plus($$$cl15.String(", ",2)).plus(y));
}

//MethodDefinition doIt at misc.ceylon (38:0-40:0)
function doIt(f){
    f();
    f();
}

//ObjectDefinition foob at misc.ceylon (42:0-44:0)
function $foob(){}
for(var $ in CeylonObject.prototype){
    var $m=CeylonObject.prototype[$];
    $foob.prototype[$]=$m;
    if($.charAt($.length-1)!=='$'){$foob.prototype[$+'$CeylonObject$']=$m}
}

//AttributeDeclaration name at misc.ceylon (43:4-43:30)
$foob.prototype.getName=function getName(){
    return this.name;
}
var $foob=function foob(){
    var $$foob=new $foob;
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    $$foob.name=$$$cl15.String("Gavin",5);
    return $$foob;
}();
function getFoob(){
    return $foob;
}

//MethodDefinition printAll at misc.ceylon (46:0-46:34)
function printAll(strings){}

//ClassDeclaration F at misc.ceylon (48:0-48:26)
var F=Foo;
