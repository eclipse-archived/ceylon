var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function $X(){}

//MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
$X.prototype.helloWorld=function helloWorld(){
    $$$cl15.print($$$cl15.String("hello world"));
}
function X(){
    var $$x=new $X;
    return $$x;
}
this.X=X;

//ClassDefinition Foo at misc.ceylon (7:0-16:0)
function $Foo(){}

//AttributeDeclaration name at misc.ceylon (8:4-8:29)
$Foo.prototype.getName=function getName(){
    return this.name;
}

//AttributeDeclaration counter at misc.ceylon (9:4-9:29)
$Foo.prototype.getCounter=function getCounter(){
    return this.counter;
}
$Foo.prototype.setCounter=function setCounter(counter){
    this.counter=counter;
}

//AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
$Foo.prototype.getCount=function getCount(){
    return this.getCounter();
}

//MethodDefinition inc at misc.ceylon (11:4-11:44)
$Foo.prototype.inc=function inc(){
    this.setCounter(this.getCounter().plus($$$cl15.Integer(1)));
}

//MethodDefinition printName at misc.ceylon (12:4-14:4)
$Foo.prototype.printName=function printName(){
    $$$cl15.print($$$cl15.String("foo name = ").plus(this.name));
}
function Foo(name){
    var $$foo=new $Foo;
    $$foo.name=name;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:29)
    $$foo.name=name;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    $$foo.counter=$$$cl15.Integer(0);
    $$foo.inc();
    return $$foo;
}
this.Foo=Foo;

//ClassDefinition Bar at misc.ceylon (18:0-32:0)
function $Bar(){}
for(var $ in $Foo.prototype){$Bar.prototype[$]=$Foo.prototype[$]}
for(var $ in $X.prototype){$Bar.prototype[$]=$X.prototype[$]}

//MethodDefinition printName at misc.ceylon (19:4-22:4)
$Bar.prototype.printName=function printName(){
    $$$cl15.print($$$cl15.String("bar name = ").plus(this.getName()));
    this.super.printName();
}
function Bar(){
    var $$bar=new $Bar;
    $$bar.super=Foo($$$cl15.String("Hello"));
    for(var $ in $$bar.super){if($$bar.super.hasOwnProperty($))$$bar[$]=$$bar.super[$]}
    var $superX=X();
    for(var $ in $superX){if($superX.hasOwnProperty($))$$bar[$]=$superX[$]}
    
    //ClassDefinition Inner at misc.ceylon (23:4-29:4)
    function $Inner(){}
    
    //MethodDefinition incOuter at misc.ceylon (26:8-28:8)
    $Inner.prototype.incOuter=function incOuter(){
        $$bar.inc();
    }
    function Inner(){
        var $$inner=new $Inner;
        $$$cl15.print($$$cl15.String("creating inner class of :").plus($$bar.getName()));
        return $$inner;
    }
    $$bar.Inner=Inner;
    return $$bar;
}
this.Bar=Bar;

//MethodDefinition printBoth at misc.ceylon (34:0-36:0)
function printBoth(x,y){
    $$$cl15.print(x.plus($$$cl15.String(", ")).plus(y));
}

//MethodDefinition doIt at misc.ceylon (38:0-40:0)
function doIt(f){
    f();
    f();
}

//ObjectDefinition foob at misc.ceylon (42:0-44:0)
function $foob(){}

//AttributeDeclaration name at misc.ceylon (43:4-43:30)
$foob.prototype.getName=function getName(){
    return this.name;
}
var $foob=function foob(){
    var $$foob=new $foob;
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    $$foob.name=$$$cl15.String("Gavin");
    return $$foob;
}();
function getFoob(){
    return $foob;
}

//MethodDefinition printAll at misc.ceylon (46:0-46:34)
function printAll(strings){}

//ClassDeclaration F at misc.ceylon (48:0-48:26)
var F=Foo;
var $$$cl15=require('ceylon/language/0.1/ceylon.language');
var $$$m7=require('default/members');

//MethodDefinition testit at testit.ceylon (3:0-35:0)
function testit(){
    
    //AttributeDeclaration name at testit.ceylon (4:4-4:24)
    var $name=$$$cl15.String("hello");
    function getName(){
        return $name;
    }
    $$$cl15.print(getName());
    
    //AttributeDeclaration foo at testit.ceylon (6:4-6:24)
    var $foo=F($$$cl15.String("goodbye"));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=$$$cl15.String("y");var $x=$$$cl15.String("x");return printBoth($x,$y)}());
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
    
    //MethodDefinition y at testit.ceylon (19:8-21:8)
    $x.prototype.y=function y(){
        $$$cl15.print($$$cl15.String("xy"));
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
    printAll($$$cl15.ArraySequence([$$$cl15.String("hello"),$$$cl15.String("world")]));
    (function (){var $strings=$$$cl15.ArraySequence([$$$cl15.String("hello"),$$$cl15.String("world")]);return printAll($strings)}());
    
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
