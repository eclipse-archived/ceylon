(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.2/ceylon.language');
var $$$m2=require('default/members');

//MethodDefinition test at testit.ceylon (3:0-38:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (4:4-4:24)
    var name$3=$$$cl1.String("hello",5);
    $$$cl1.print(name$3);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$4=Foo($$$cl1.String("goodbye",7));
    printBoth(name$3,foo$4.getName());
    (function (){var y$5=$$$cl1.String("y",1);var x$6=$$$cl1.String("x",1);return printBoth(x$6,y$5)}());
    foo$4.inc();
    foo$4.inc();
    $$$cl1.print(foo$4.getCount());
    foo$4.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((function(){var $=foo$4;return $$$cl1.JsCallable($, $.inc)})());
    $$$cl1.print(foo$4.getCount());
    doIt(Bar);
    $$$cl1.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (19:4-23:4)
    function x$7(){
        var $$x$7=new x$7.$$;
        return $$x$7;
    }
    $$$cl1.initTypeProto(x$7,'misc.test.x',$$$cl1.IdentifiableObject);
    var x$8=x$7(new x$7.$$);
    function getX$8(){
        return x$8;
    }
    ;(function($proto$){
        
        //MethodDefinition y at testit.ceylon (20:8-22:8)
        $proto$.y=function y(){
            var $$x$7=this;
            $$$cl1.print($$$cl1.String("xy",2));
        }
        
    })(x$7.$$.prototype);
    getX$8().y();
    
    //AttributeDeclaration b at testit.ceylon (25:4-25:17)
    var b$9=Bar();
    b$9.Inner$Bar().incOuter();
    b$9.Inner$Bar().incOuter();
    b$9.Inner$Bar().incOuter();
    $$$cl1.print(b$9.getCount());
    printAll($$$cl1.ArraySequence([$$$cl1.String("hello",5),$$$cl1.String("world",5)]));
    (function (){var strings$10=$$$cl1.ArraySequence([$$$cl1.String("hello",5),$$$cl1.String("world",5)]);return printAll(strings$10)}());
    
    //AttributeDeclaration c at testit.ceylon (33:4-33:26)
    var c$11=$$$m2.Counter($$$cl1.Integer(0));
    c$11.inc();
    c$11.inc();
    $$$cl1.print(c$11.getCount());
    
    //AttributeDeclaration v2 at testit.ceylon (37:4-37:20)
    var v2$12=$var();
    
}
exports.test=test;
var $$$cl13=require('ceylon/language/0.2/ceylon.language');

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
    
}
exports.X=X;
$$$cl13.initTypeProtoI(X,'misc.X');
;(function($proto$){
    
    //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
    $proto$.helloWorld=function helloWorld(){
        var $$x=this;
        $$$cl13.print($$$cl13.String("hello world",11));
    }
    
})(X.$$.prototype);

//ClassDefinition Foo at misc.ceylon (7:0-16:0)
function Foo(name$14$, $$foo){
    if ($$foo===undefined)$$foo=new Foo.$$;
    $$foo.name$14$=name$14$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:29)
    $$foo.name$15$=$$foo.name$14$;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    $$foo.counter$16$=$$$cl13.Integer(0);
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
$$$cl13.initTypeProto(Foo,'misc.Foo',$$$cl13.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:29)
    $proto$.getName=function getName(){
        return this.name$15$;
    }
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    $proto$.getCounter$16$=function getCounter$16$(){
        return this.counter$16$;
    }
    $proto$.setCounter$16$=function setCounter$16$(counter$17){
        this.counter$16$=counter$17; return counter$17;
    }
    
    //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
    $proto$.getCount=function getCount(){
        var $$foo=this;
        return $$foo.getCounter$16$();
    }
    
    //MethodDefinition inc at misc.ceylon (11:4-11:44)
    $proto$.inc=function inc(){
        var $$foo=this;
        $$foo.setCounter$16$($$foo.getCounter$16$().plus($$$cl13.Integer(1)));
    }
    
    //MethodDefinition printName at misc.ceylon (12:4-14:4)
    $proto$.printName=function printName(){
        var $$foo=this;
        $$$cl13.print($$$cl13.String("foo name = ",11).plus($$foo.name$14$));
    }
    
})(Foo.$$.prototype);

//ClassDefinition Bar at misc.ceylon (18:0-32:0)
function Bar($$bar){
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl13.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
$$$cl13.initTypeProto(Bar,'misc.Bar',Foo,X);
;(function($proto$){
    
    //MethodDefinition printName at misc.ceylon (19:4-22:4)
    $proto$.printName=function printName(){
        var $$bar=this;
        $$$cl13.print($$$cl13.String("bar name = ",11).plus($$bar.getName()));
        $$bar.printName$$misc$Foo$();
    }
    
    //ClassDefinition Inner at misc.ceylon (23:4-29:4)
    function Inner$Bar($$inner$Bar){
        if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
        $$inner$Bar.$$bar=this;
        $$$cl13.print($$$cl13.String("creating inner class of :",25).plus($$inner$Bar.$$bar.getName()));
        return $$inner$Bar;
    }
    $$$cl13.initTypeProto(Inner$Bar,'misc.Bar.Inner',$$$cl13.IdentifiableObject);
    ;(function($proto$){
        
        //MethodDefinition incOuter at misc.ceylon (26:8-28:8)
        $proto$.incOuter=function incOuter(){
            var $$inner$Bar=this;
            $$inner$Bar.$$bar.inc();
        }
        
    })(Inner$Bar.$$.prototype);
    $proto$.Inner$Bar=Inner$Bar;
    
})(Bar.$$.prototype);

//MethodDefinition printBoth at misc.ceylon (34:0-36:0)
function printBoth(x$18,y$19){
    $$$cl13.print(x$18.plus($$$cl13.String(", ",2)).plus(y$19));
}

//MethodDefinition doIt at misc.ceylon (38:0-40:0)
function doIt(f$20){
    f$20();
    f$20();
}

//ObjectDefinition foob at misc.ceylon (42:0-44:0)
function foob(){
    var $$foob=new foob.$$;
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    $$foob.name$21$=$$$cl13.String("Gavin",5);
    return $$foob;
}
$$$cl13.initTypeProto(foob,'misc.foob',$$$cl13.IdentifiableObject);
var foob$22=foob(new foob.$$);
function getFoob(){
    return foob$22;
}
;(function($proto$){
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    $proto$.getName=function getName(){
        return this.name$21$;
    }
    
})(foob.$$.prototype);

//MethodDefinition printAll at misc.ceylon (46:0-46:34)
function printAll(strings$23){
    if(strings$23===undefined){strings$23=$$$cl13.empty;}
    
}

//ClassDeclaration F at misc.ceylon (48:0-48:26)
var F=Foo;

//MethodDefinition var at misc.ceylon (50:0-50:33)
function $var(){
    return $$$cl13.Integer(5);
}
exports.$var=$var;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
