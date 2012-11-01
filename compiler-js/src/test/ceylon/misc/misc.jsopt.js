(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.5/ceylon.language');

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
    
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl1.initTypeProtoI(X,'misc.X');
        (function($$x){
            
            //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl1.print($$$cl1.String("hello world",11));
            }
            
        })(X.$$.prototype);
        
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-16:0)
function Foo(name$297$, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    $$foo.name$298$=name$297$;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    $$foo.counter$299$=$$$cl1.Integer(0);
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl1.initTypeProto(Foo,'misc.Foo',$$$cl1.IdentifiableObject);
        (function($$foo){
            
            //AttributeDeclaration name at misc.ceylon (8:4-8:22)
            $$foo.getName=function getName(){
                return this.name$298$;
            }
            
            //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
            $$foo.getCounter$299$=function getCounter$299$(){
                return this.counter$299$;
            }
            $$foo.setCounter$299$=function setCounter$299$(counter$300){
                this.counter$299$=counter$300; return counter$300;
            }
            
            //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
            $$foo.getCount=function getCount(){
                var $$foo=this;
                return $$foo.getCounter$299$();
            }
            
            //MethodDefinition inc at misc.ceylon (11:4-11:44)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.setCounter$299$($$foo.getCounter$299$().plus($$$cl1.Integer(1)));
            }
            
            //MethodDefinition printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl1.print($$$cl1.String("foo name = ",11).plus($$foo.getName()));
            }
            
        })(Foo.$$.prototype);
        
    }
    return Foo;
}
exports.$init$Foo=$init$Foo;
$init$Foo();

//ClassDefinition Bar at misc.ceylon (18:0-32:0)
function Bar($$bar){
    $init$Bar();
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl1.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl1.initTypeProto(Bar,'misc.Bar',Foo,$init$X());
        (function($$bar){
            
            //MethodDefinition printName at misc.ceylon (19:4-22:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl1.print($$$cl1.String("bar name = ",11).plus($$bar.getName()));
                $$bar.printName$$misc$Foo$();
            }
            
            //ClassDefinition Inner at misc.ceylon (23:4-29:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$bar=this;
                $$$cl1.print($$$cl1.String("creating inner class of :",25).plus($$inner$Bar.$$bar.getName()));
                return $$inner$Bar;
            }
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl1.initTypeProto(Inner$Bar,'misc.Bar.Inner',$$$cl1.IdentifiableObject);
                    (function($$inner$Bar){
                        
                        //MethodDefinition incOuter at misc.ceylon (26:8-28:8)
                        $$inner$Bar.incOuter=function incOuter(){
                            var $$inner$Bar=this;
                            $$inner$Bar.$$bar.inc();
                        }
                        
                    })(Inner$Bar.$$.prototype);
                    
                }
                return Inner$Bar;
            }
            $$bar.$init$Inner$Bar=$init$Inner$Bar;
            $init$Inner$Bar();
            $$bar.Inner$Bar=Inner$Bar;
            
        })(Bar.$$.prototype);
        
    }
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDefinition printBoth at misc.ceylon (34:0-36:0)
function printBoth(x$301,y$302){
    $$$cl1.print(x$301.plus($$$cl1.String(", ",2)).plus(y$302));
}

//MethodDefinition doIt at misc.ceylon (38:0-40:0)
function doIt(f$303){
    f$303();
    f$303();
}

//ObjectDefinition foob at misc.ceylon (42:0-44:0)
function foob(){
    var $$foob=new foob.$$;
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    $$foob.name$304$=$$$cl1.String("Gavin",5);
    return $$foob;
}
function $init$foob(){
    if (foob.$$===undefined){
        $$$cl1.initTypeProto(foob,'misc.foob',$$$cl1.IdentifiableObject);
        
    }
    return foob;
}
exports.$init$foob=$init$foob;
$init$foob();
(function($$foob){
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    $$foob.getName=function getName(){
        return this.name$304$;
    }
    
})(foob.$$.prototype);
var foob$305=foob(new foob.$$);
var getFoob=function(){
    return foob$305;
}

//MethodDefinition printAll at misc.ceylon (46:0-46:34)
function printAll(strings$306){
    if(strings$306===undefined){strings$306=$$$cl1.empty;}
    
}

//ClassDeclaration F at misc.ceylon (48:0-48:26)
var F=Foo;

//MethodDefinition var at misc.ceylon (50:0-50:33)
function $var(){
    return $$$cl1.Integer(5);
}
exports.$var=$var;
var $$$cl1=require('ceylon/language/0.5/ceylon.language');
var $$$a12=require('default/assert');

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$307$, b$308$, c$309$, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.a$307$=a$307$;
    $$testObjects.b$308$=b$308$;
    $$testObjects.c$309$=c$309$;
    $$$cl1.Iterable($$testObjects);
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl1.initTypeProto(TestObjects,'misc.TestObjects',$$$cl1.IdentifiableObject,$$$cl1.Iterable);
        (function($$testObjects){
            
            //AttributeGetterDefinition iterator at objects.ceylon (4:2-16:2)
            $$testObjects.getIterator=function getIterator(){
                var $$testObjects=this;
                
                //ObjectDefinition iter at objects.ceylon (5:4-14:4)
                function iter$310(){
                    var $$iter$310=new iter$310.$$;
                    $$$cl1.Iterator($$iter$310);
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:31)
                    $$iter$310.index$311$=$$$cl1.Integer(0);
                    return $$iter$310;
                }
                function $init$iter$310(){
                    if (iter$310.$$===undefined){
                        $$$cl1.initTypeProto(iter$310,'misc.TestObjects.iterator.iter',$$$cl1.IdentifiableObject,$$$cl1.Iterator);
                        
                    }
                    return iter$310;
                }
                $init$iter$310();
                (function($$iter$310){
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:31)
                    $$iter$310.getIndex$311$=function getIndex$311$(){
                        return this.index$311$;
                    }
                    $$iter$310.setIndex$311$=function setIndex$311$(index$312){
                        this.index$311$=index$312; return index$312;
                    }
                    
                    //MethodDefinition next at objects.ceylon (7:6-13:6)
                    $$iter$310.next=function next(){
                        var $$iter$310=this;
                        (function($){$$iter$310.setIndex$311$($.getSuccessor());return $}($$iter$310.getIndex$311$()));
                        if (($$iter$310.getIndex$311$().equals($$$cl1.Integer(1)))===$$$cl1.getTrue()){
                            return $$testObjects.a$307$;
                        }
                        else {
                            if (($$iter$310.getIndex$311$().equals($$$cl1.Integer(2)))===$$$cl1.getTrue()){
                                return $$testObjects.b$308$;
                            }
                            else {
                                if (($$iter$310.getIndex$311$().equals($$$cl1.Integer(3)))===$$$cl1.getTrue()){
                                    return $$testObjects.c$309$;
                                }
                                
                            }
                            
                        }
                        
                        return $$$cl1.getExhausted();
                    }
                    
                })(iter$310.$$.prototype);
                var iter$313=iter$310(new iter$310.$$);
                var getIter$313=function(){
                    return iter$313;
                }
                return getIter$313();
            }
            
        })(TestObjects.$$.prototype);
        
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl1.print($$$cl1.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:40)
    var t1$314=TestObjects($$$cl1.Integer(1),$$$cl1.Integer(2),$$$cl1.Integer(3)).getIterator();
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:40)
    var t2$315=TestObjects($$$cl1.Integer(1),$$$cl1.Integer(2),$$$cl1.Integer(3)).getIterator();
    var i$316;
    if($$$cl1.isOfType((i$316=t1$314.next()),'ceylon.language.Integer')===$$$cl1.getTrue()){
        $$$a12.assert(i$316.equals($$$cl1.Integer(1)),$$$cl1.String("objects 1",9));
    }
    
    var i$317;
    if($$$cl1.isOfType((i$317=t1$314.next()),'ceylon.language.Integer')===$$$cl1.getTrue()){
        $$$a12.assert(i$317.equals($$$cl1.Integer(2)),$$$cl1.String("objects 2",9));
    }
    
    var i$318;
    if($$$cl1.isOfType((i$318=t2$315.next()),'ceylon.language.Integer')===$$$cl1.getTrue()){
        $$$a12.assert(i$318.equals($$$cl1.Integer(1)),$$$cl1.String("objects 3",9));
    }
    
    var i$319;
    if($$$cl1.isOfType((i$319=t1$314.next()),'ceylon.language.Integer')===$$$cl1.getTrue()){
        $$$a12.assert(i$319.equals($$$cl1.Integer(3)),$$$cl1.String("objects 4",9));
    }
    
    $$$a12.assert($$$cl1.isOfType(t1$314.next(),'ceylon.language.Finished'),$$$cl1.String("objects 5",9));
    var i$320;
    if($$$cl1.isOfType((i$320=t2$315.next()),'ceylon.language.Integer')===$$$cl1.getTrue()){
        $$$a12.assert(i$320.equals($$$cl1.Integer(2)),$$$cl1.String("objects 6",9));
    }
    
    var i$321;
    if($$$cl1.isOfType((i$321=t2$315.next()),'ceylon.language.Integer')===$$$cl1.getTrue()){
        $$$a12.assert(i$321.equals($$$cl1.Integer(3)),$$$cl1.String("objects 7",9));
    }
    
}
var $$$cl1=require('ceylon/language/0.5/ceylon.language');
var $$$m322=require('default/members');

//MethodDefinition test at testit.ceylon (3:0-39:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (4:4-4:24)
    var name$323=$$$cl1.String("hello",5);
    $$$cl1.print(name$323);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$324=Foo($$$cl1.String("goodbye",7));
    printBoth(name$323,foo$324.getName());
    (function (){var y$302=$$$cl1.String("y",1);var x$301=$$$cl1.String("x",1);return printBoth(x$301,y$302)}());
    foo$324.inc();
    foo$324.inc();
    $$$cl1.print(foo$324.getCount());
    foo$324.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((function(){var $=foo$324;return $$$cl1.JsCallable($, $==null?null:$.inc)})());
    $$$cl1.print(foo$324.getCount());
    doIt(Bar);
    $$$cl1.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (19:4-23:4)
    function x$325(){
        var $$x$325=new x$325.$$;
        return $$x$325;
    }
    function $init$x$325(){
        if (x$325.$$===undefined){
            $$$cl1.initTypeProto(x$325,'misc.test.x',$$$cl1.IdentifiableObject);
            
        }
        return x$325;
    }
    $init$x$325();
    (function($$x$325){
        
        //MethodDefinition y at testit.ceylon (20:8-22:8)
        $$x$325.y=function y(){
            var $$x$325=this;
            $$$cl1.print($$$cl1.String("xy",2));
        }
        
    })(x$325.$$.prototype);
    var x$326=x$325(new x$325.$$);
    var getX$326=function(){
        return x$326;
    }
    getX$326().y();
    
    //AttributeDeclaration b at testit.ceylon (25:4-25:17)
    var b$327=Bar();
    b$327.Inner$Bar().incOuter();
    b$327.Inner$Bar().incOuter();
    b$327.Inner$Bar().incOuter();
    $$$cl1.print(b$327.getCount());
    printAll($$$cl1.ArraySequence([$$$cl1.String("hello",5),$$$cl1.String("world",5)]));
    (function (){var strings$306=$$$cl1.ArraySequence([$$$cl1.String("hello",5),$$$cl1.String("world",5)]);return printAll(strings$306)}());
    
    //AttributeDeclaration c at testit.ceylon (33:4-33:26)
    var c$328=$$$m322.Counter($$$cl1.Integer(0));
    c$328.inc();
    c$328.inc();
    $$$cl1.print(c$328.getCount());
    
    //AttributeDeclaration v2 at testit.ceylon (37:4-37:20)
    var v2$329=$var();
    test_objects();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
