(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
    
    //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
    function helloWorld(){
        $$$cl15.print($$$cl15.String("hello world",11));
    }
    $$x.helloWorld=helloWorld;
    
}
exports.X=X;
$$$cl15.initType(X,'misc.X');

//ClassDefinition Foo at misc.ceylon (7:0-16:0)
function Foo(name, $$foo){
    if ($$foo===undefined)$$foo=new Foo.$$;
    $$foo.name=name;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:29)
    var $name=name;
    function getName(){
        return $name;
    }
    $$foo.getName=getName;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    var $counter=$$$cl15.Integer(0);
    function getCounter(){
        return $counter;
    }
    $$foo.getCounter=getCounter;
    function setCounter(counter){
        $counter=counter; return counter;
    }
    $$foo.setCounter=setCounter;
    
    //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
    function getCount(){
        return getCounter();
    }
    $$foo.getCount=getCount;
    
    //MethodDefinition inc at misc.ceylon (11:4-11:44)
    function inc(){
        setCounter(getCounter().plus($$$cl15.Integer(1)));
    }
    $$foo.inc=inc;
    
    //MethodDefinition printName at misc.ceylon (12:4-14:4)
    function printName(){
        $$$cl15.print($$$cl15.String("foo name = ",11).plus(name));
    }
    $$foo.printName=printName;
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
$$$cl15.initType(Foo,'misc.Foo',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Foo,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition Bar at misc.ceylon (18:0-32:0)
function Bar($$bar){
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl15.String("Hello",5),$$bar);
    $$bar.printName$Foo$=$$bar.printName;
    X($$bar);
    
    //MethodDefinition printName at misc.ceylon (19:4-22:4)
    function printName(){
        $$$cl15.print($$$cl15.String("bar name = ",11).plus($$bar.getName()));
        (function(){var $=$$bar;return $$$cl15.JsCallable($, $.printName$Foo$)})()();
    }
    $$bar.printName=printName;
    
    //ClassDefinition Inner at misc.ceylon (23:4-29:4)
    function Inner($$inner){
        if ($$inner===undefined)$$inner=new Inner.$$;
        $$$cl15.print($$$cl15.String("creating inner class of :",25).plus($$bar.getName()));
        
        //MethodDefinition incOuter at misc.ceylon (26:8-28:8)
        function incOuter(){
            $$bar.inc();
        }
        $$inner.incOuter=incOuter;
        return $$inner;
    }
    $$bar.Inner=Inner;
    $$$cl15.initType(Inner,'misc.Bar.Inner',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(Inner,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    return $$bar;
}
exports.Bar=Bar;
$$$cl15.initType(Bar,'misc.Bar',Foo,X);

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
function foob(){
    var $$foob=new foob.$$;
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    var $name=$$$cl15.String("Gavin",5);
    function getName(){
        return $name;
    }
    $$foob.getName=getName;
    return $$foob;
}
$$$cl15.initType(foob,'misc.foob',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(foob,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
var o$foob=foob(new foob.$$);
function getFoob(){
    return o$foob;
}

//MethodDefinition printAll at misc.ceylon (46:0-46:34)
function printAll(strings){
    
}

//ClassDeclaration F at misc.ceylon (48:0-48:26)
var F=Foo;
var $$$cl15=require('ceylon/language/0.1/ceylon.language');
var $$$m7=require('default/members');

//MethodDefinition test at testit.ceylon (3:0-36:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (4:4-4:24)
    var $name=$$$cl15.String("hello",5);
    function getName(){
        return $name;
    }
    $$$cl15.print(getName());
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var $foo=Foo($$$cl15.String("goodbye",7));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=$$$cl15.String("y",1);var $x=$$$cl15.String("x",1);return printBoth($x,$y)}());
    (function(){var $=getFoo();return $$$cl15.JsCallable($, $.inc)})()();
    (function(){var $=getFoo();return $$$cl15.JsCallable($, $.inc)})()();
    $$$cl15.print(getFoo().getCount());
    (function(){var $=getFoo();return $$$cl15.JsCallable($, $.printName)})()();
    (function(){var $=Bar();return $$$cl15.JsCallable($, $.printName)})()();
    Bar().Inner();
    doIt((function(){var $=getFoo();return $$$cl15.JsCallable($, $.inc)})());
    $$$cl15.print(getFoo().getCount());
    doIt(Bar);
    $$$cl15.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (19:4-23:4)
    function x(){
        var $$x=new x.$$;
        
        //MethodDefinition y at testit.ceylon (20:8-22:8)
        function y(){
            $$$cl15.print($$$cl15.String("xy",2));
        }
        $$x.y=y;
        return $$x;
    }
    $$$cl15.initType(x,'misc.test.x',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(x,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    var o$x=x(new x.$$);
    function getX(){
        return o$x;
    }
    (function(){var $=getX();return $$$cl15.JsCallable($, $.y)})()();
    
    //AttributeDeclaration b at testit.ceylon (25:4-25:17)
    var $b=Bar();
    function getB(){
        return $b;
    }
    (function(){var $=getB().Inner();return $$$cl15.JsCallable($, $.incOuter)})()();
    (function(){var $=getB().Inner();return $$$cl15.JsCallable($, $.incOuter)})()();
    (function(){var $=getB().Inner();return $$$cl15.JsCallable($, $.incOuter)})()();
    $$$cl15.print(getB().getCount());
    printAll($$$cl15.ArraySequence([$$$cl15.String("hello",5),$$$cl15.String("world",5)]));
    (function (){var $strings=$$$cl15.ArraySequence([$$$cl15.String("hello",5),$$$cl15.String("world",5)]);return printAll($strings)}());
    
    //AttributeDeclaration c at testit.ceylon (33:4-33:26)
    var $c=$$$m7.Counter($$$cl15.Integer(0));
    function getC(){
        return $c;
    }
    (function(){var $=getC();return $$$cl15.JsCallable($, $.inc)})()();
    (function(){var $=getC();return $$$cl15.JsCallable($, $.inc)})()();
    $$$cl15.print(getC().getCount());
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
