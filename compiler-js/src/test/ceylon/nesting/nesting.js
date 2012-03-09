(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//AttributeDeclaration assertionCount at nesting.ceylon (1:0-1:34)
var $assertionCount=$$$cl15.Integer(0);
function getAssertionCount(){
    return $assertionCount;
}
exports.getAssertionCount=getAssertionCount;
function setAssertionCount(assertionCount){
    $assertionCount=assertionCount; return assertionCount;
}
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at nesting.ceylon (2:0-2:32)
var $failureCount=$$$cl15.Integer(0);
function getFailureCount(){
    return $failureCount;
}
exports.getFailureCount=getFailureCount;
function setFailureCount(failureCount){
    $failureCount=failureCount; return failureCount;
}
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at nesting.ceylon (4:0-10:0)
function assert(assertion,message){
    if(message===undefined){message=$$$cl15.String("",0)}
    (setAssertionCount(getAssertionCount().plus($$$cl15.Integer(1))),getAssertionCount());
    if ((assertion.equals($$$cl15.getFalse()))===$$$cl15.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl15.Integer(1))),getFailureCount());
        $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertion failed \""),message.getString(),$$$cl15.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at nesting.ceylon (12:0-14:0)
function fail(message){
    assert($$$cl15.getFalse(),message);
}
exports.fail=fail;

//MethodDefinition results at nesting.ceylon (16:0-19:0)
function results(){
    $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertions ",11),getAssertionCount().getString(),$$$cl15.String(", failures ",11),getFailureCount().getString(),$$$cl15.String("",0)])).getString());
}
exports.results=results;

//ClassDefinition Outer at nesting.ceylon (21:0-46:0)
function Outer(name, $$outer){
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name=name;
    
    //AttributeDeclaration int at nesting.ceylon (22:4-22:18)
    var $int=$$$cl15.Integer(10);
    function getInt(){
        return $int;
    }
    $$outer.getInt=getInt;
    
    //AttributeDeclaration float at nesting.ceylon (23:4-23:34)
    var $float=getInt().getFloat();
    function getFloat(){
        return $float;
    }
    $$outer.getFloat=getFloat;
    
    //MethodDefinition noop at nesting.ceylon (24:4-24:17)
    function noop(){
        
    }
    $$outer.noop=noop;
    
    //ClassDefinition Inner at nesting.ceylon (25:4-38:4)
    function Inner($$inner){
        if ($$inner===undefined)$$inner=new Inner.$$;
        
        //MethodDefinition printName at nesting.ceylon (26:8-28:8)
        function printName(){
            $$$cl15.print(name);
        }
        
        //AttributeGetterDefinition int at nesting.ceylon (29:8-31:8)
        function getInt(){
            return $$outer.getInt();
        }
        $$inner.getInt=getInt;
        
        //AttributeGetterDefinition float at nesting.ceylon (32:8-34:8)
        function getFloat(){
            return $$outer.getFloat();
        }
        $$inner.getFloat=getFloat;
        
        //MethodDefinition noop at nesting.ceylon (35:8-37:8)
        function noop(){
            (function(){var $=$$outer;return $$$cl15.JsCallable($, $.noop)})()();
        }
        $$inner.noop=noop;
        return $$inner;
    }
    $$$cl15.initType(Inner,'nesting.Outer.Inner',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(Inner,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //AttributeDeclaration inner at nesting.ceylon (39:4-39:25)
    var $inner=Inner();
    function getInner(){
        return $inner;
    }
    $$$cl15.print(getInner().getInt());
    $$$cl15.print(getInner().getFloat());
    (function(){var $=getInner();return $$$cl15.JsCallable($, $.noop)})()();
    noop();
    return $$outer;
}
exports.Outer=Outer;
$$$cl15.initType(Outer,'nesting.Outer',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Outer,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition outr at nesting.ceylon (48:0-60:0)
function outr(name){
    
    //AttributeDeclaration uname at nesting.ceylon (49:4-49:34)
    var $uname=name.getUppercased();
    function getUname(){
        return $uname;
    }
    
    //MethodDefinition inr at nesting.ceylon (50:4-52:4)
    function inr(){
        return name;
    }
    
    //AttributeGetterDefinition uinr at nesting.ceylon (53:4-55:4)
    function getUinr(){
        return getUname();
    }
    
    //AttributeDeclaration result at nesting.ceylon (56:4-56:25)
    var $result=inr();
    function getResult(){
        return $result;
    }
    
    //AttributeDeclaration uresult at nesting.ceylon (57:4-57:25)
    var $uresult=getUinr();
    function getUresult(){
        return $uresult;
    }
    $$$cl15.print(getResult());
    $$$cl15.print(getUresult());
}
exports.outr=outr;

//ClassDefinition Holder at nesting.ceylon (62:0-69:0)
function Holder(o, $$holder){
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o=o;
    
    //MethodDefinition get at nesting.ceylon (63:4-65:4)
    function get(){
        return o;
    }
    $$holder.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (66:4-68:4)
    function getString(){
        return o.getString();
    }
    $$holder.getString=getString;
    return $$holder;
}
exports.Holder=Holder;
$$$cl15.initType(Holder,'nesting.Holder',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Holder,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition Wrapper at nesting.ceylon (71:0-79:0)
function Wrapper($$wrapper){
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (72:4-72:18)
    var $o=$$$cl15.Integer(100);
    function getO(){
        return $o;
    }
    $$wrapper.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (73:4-75:4)
    function get(){
        return getO();
    }
    $$wrapper.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (76:4-78:4)
    function getString(){
        return getO().getString();
    }
    $$wrapper.getString=getString;
    return $$wrapper;
}
exports.Wrapper=Wrapper;
$$$cl15.initType(Wrapper,'nesting.Wrapper',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Wrapper,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition Unwrapper at nesting.ceylon (81:0-89:0)
function Unwrapper($$unwrapper){
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (82:4-82:27)
    var $o=$$$cl15.Float(23.56);
    function getO(){
        return $o;
    }
    $$unwrapper.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (83:4-85:4)
    function get(){
        return $$unwrapper.getO();
    }
    $$unwrapper.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (86:4-88:4)
    function getString(){
        return $$unwrapper.getO().getString();
    }
    $$unwrapper.getString=getString;
    return $$unwrapper;
}
exports.Unwrapper=Unwrapper;
$$$cl15.initType(Unwrapper,'nesting.Unwrapper',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(Unwrapper,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition producer at nesting.ceylon (91:0-95:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (92:4-92:18)
    var $o=$$$cl15.Integer(123);
    function getO(){
        return $o;
    }
    
    //MethodDefinition produce at nesting.ceylon (93:4-93:35)
    function produce(){
        return getO();
    }
    return produce;
}

//MethodDefinition returner at nesting.ceylon (97:0-100:0)
function returner(o){
    
    //MethodDefinition produce at nesting.ceylon (98:4-98:35)
    function produce(){
        return o;
    }
    return produce;
}

//ClassDefinition A at nesting.ceylon (102:0-123:0)
function A($$a){
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDeclaration foo at nesting.ceylon (103:4-103:22)
    var $foo=$$$cl15.String("foo",3);
    function getFoo(){
        return $foo;
    }
    $$a.getFoo=getFoo;
    
    //ClassDefinition B at nesting.ceylon (104:4-114:4)
    function B($$b){
        if ($$b===undefined)$$b=new B.$$;
        
        //AttributeDeclaration qux at nesting.ceylon (105:8-105:26)
        var $qux=$$$cl15.String("qux",3);
        function getQux(){
            return $qux;
        }
        $$b.getQux=getQux;
        
        //ClassDefinition C at nesting.ceylon (106:8-113:8)
        function C($$c){
            if ($$c===undefined)$$c=new C.$$;
            
            //MethodDefinition foobar at nesting.ceylon (107:12-109:12)
            function foobar(){
                return getFoo();
            }
            $$c.foobar=foobar;
            
            //MethodDefinition quxx at nesting.ceylon (110:12-112:12)
            function quxx(){
                return getQux();
            }
            $$c.quxx=quxx;
            return $$c;
        }
        $$b.C=C;
        $$$cl15.initType(C,'nesting.A.B.C',$$$cl15.IdentifiableObject);
        $$$cl15.inheritProto(C,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
        return $$b;
    }
    $$a.B=B;
    $$$cl15.initType(B,'nesting.A.B',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(B,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //MethodDefinition baz at nesting.ceylon (115:4-122:4)
    function baz(){
        
        //ClassDefinition Baz at nesting.ceylon (116:8-120:8)
        function Baz($$baz){
            if ($$baz===undefined)$$baz=new Baz.$$;
            
            //MethodDefinition get at nesting.ceylon (117:12-119:12)
            function get(){
                return getFoo();
            }
            $$baz.get=get;
            return $$baz;
        }
        $$$cl15.initType(Baz,'nesting.A.baz.Baz',$$$cl15.IdentifiableObject);
        $$$cl15.inheritProto(Baz,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
        return (function(){var $=Baz();return $$$cl15.JsCallable($, $.get)})()();
    }
    $$a.baz=baz;
    return $$a;
}
$$$cl15.initType(A,'nesting.A',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(A,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition O at nesting.ceylon (125:0-152:0)
function O($$o){
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDeclaration s at nesting.ceylon (126:4-126:22)
    var $s=$$$cl15.String("hello",5);
    function getS(){
        return $s;
    }
    $$o.getS=getS;
    
    //ClassDefinition InnerClass at nesting.ceylon (127:4-131:4)
    function InnerClass($$innerClass){
        if ($$innerClass===undefined)$$innerClass=new InnerClass.$$;
        
        //MethodDefinition f at nesting.ceylon (128:8-130:8)
        function f(){
            return getS();
        }
        $$innerClass.f=f;
        return $$innerClass;
    }
    $$$cl15.initType(InnerClass,'nesting.O.InnerClass',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(InnerClass,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //ObjectDefinition innerObject at nesting.ceylon (132:4-136:4)
    function innerObject(){
        var $$innerObject=new innerObject.$$;
        
        //MethodDefinition f at nesting.ceylon (133:8-135:8)
        function f(){
            return getS();
        }
        $$innerObject.f=f;
        return $$innerObject;
    }
    $$$cl15.initType(innerObject,'nesting.O.innerObject',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(innerObject,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    var o$innerObject=innerObject(new innerObject.$$);
    function getInnerObject(){
        return o$innerObject;
    }
    
    //InterfaceDefinition InnerInterface at nesting.ceylon (137:4-141:4)
    function InnerInterface($$innerInterface){
        
        //MethodDefinition f at nesting.ceylon (138:8-140:8)
        function f(){
            return getS();
        }
        $$innerInterface.f=f;
        
    }
    $$$cl15.initType(InnerInterface,'nesting.O.InnerInterface');
    
    //MethodDefinition test1 at nesting.ceylon (142:4-144:4)
    function test1(){
        return (function(){var $=InnerClass();return $$$cl15.JsCallable($, $.f)})()();
    }
    $$o.test1=test1;
    
    //MethodDefinition test2 at nesting.ceylon (145:4-147:4)
    function test2(){
        return (function(){var $=getInnerObject();return $$$cl15.JsCallable($, $.f)})()();
    }
    $$o.test2=test2;
    
    //MethodDefinition test3 at nesting.ceylon (148:4-151:4)
    function test3(){
        
        //ObjectDefinition obj at nesting.ceylon (149:8-149:45)
        function obj(){
            var $$obj=new obj.$$;
            InnerInterface($$obj);
            return $$obj;
        }
        $$$cl15.initType(obj,'nesting.O.test3.obj',$$$cl15.IdentifiableObject,InnerInterface);
        $$$cl15.inheritProto(obj,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
        var o$obj=obj(new obj.$$);
        function getObj(){
            return o$obj;
        }
        return (function(){var $=getObj();return $$$cl15.JsCallable($, $.f)})()();
    }
    $$o.test3=test3;
    return $$o;
}
$$$cl15.initType(O,'nesting.O',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(O,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//ClassDefinition OuterC1 at nesting.ceylon (154:0-160:0)
function OuterC1($$outerC1){
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    
    //ClassDefinition A at nesting.ceylon (155:4-157:4)
    function A($$a){
        if ($$a===undefined)$$a=new A.$$;
        
        //MethodDefinition tst at nesting.ceylon (156:8-156:55)
        function tst(){
            return $$$cl15.String("OuterC1.A.tst()",15);
        }
        $$a.tst=tst;
        return $$a;
    }
    $$$cl15.initType(A,'nesting.OuterC1.A',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(A,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //ClassDefinition B at nesting.ceylon (158:4-158:27)
    function B($$b){
        if ($$b===undefined)$$b=new B.$$;
        A($$b);
        return $$b;
    }
    $$$cl15.initType(B,'nesting.OuterC1.B',A);
    
    //MethodDefinition tst at nesting.ceylon (159:4-159:42)
    function tst(){
        return (function(){var $=B();return $$$cl15.JsCallable($, $.tst)})()();
    }
    $$outerC1.tst=tst;
    return $$outerC1;
}
$$$cl15.initType(OuterC1,'nesting.OuterC1',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(OuterC1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition outerf at nesting.ceylon (162:0-168:0)
function outerf(){
    
    //ClassDefinition A at nesting.ceylon (163:4-165:4)
    function A($$a){
        if ($$a===undefined)$$a=new A.$$;
        
        //MethodDefinition tst at nesting.ceylon (164:8-164:54)
        function tst(){
            return $$$cl15.String("outerf.A.tst()",14);
        }
        $$a.tst=tst;
        return $$a;
    }
    $$$cl15.initType(A,'nesting.outerf.A',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(A,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //ClassDefinition B at nesting.ceylon (166:4-166:27)
    function B($$b){
        if ($$b===undefined)$$b=new B.$$;
        A($$b);
        return $$b;
    }
    $$$cl15.initType(B,'nesting.outerf.B',A);
    return (function(){var $=B();return $$$cl15.JsCallable($, $.tst)})()();
}

//ClassDefinition OuterC2 at nesting.ceylon (170:0-178:0)
function OuterC2($$outerC2){
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    
    //ClassDefinition A at nesting.ceylon (171:4-173:4)
    function A($$a){
        if ($$a===undefined)$$a=new A.$$;
        
        //MethodDefinition tst at nesting.ceylon (172:8-172:55)
        function tst(){
            return $$$cl15.String("OuterC2.A.tst()",15);
        }
        $$a.tst=tst;
        return $$a;
    }
    $$$cl15.initType(A,'nesting.OuterC2.A',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(A,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //MethodDefinition tst at nesting.ceylon (174:4-177:4)
    function tst(){
        
        //ClassDefinition B at nesting.ceylon (175:8-175:31)
        function B($$b){
            if ($$b===undefined)$$b=new B.$$;
            A($$b);
            return $$b;
        }
        $$$cl15.initType(B,'nesting.OuterC2.tst.B',A);
        return (function(){var $=B();return $$$cl15.JsCallable($, $.tst)})()();
    }
    $$outerC2.tst=tst;
    return $$outerC2;
}
$$$cl15.initType(OuterC2,'nesting.OuterC2',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(OuterC2,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition test at nesting.ceylon (180:0-204:0)
function test(){
    outr($$$cl15.String("Hello",5));
    assert((function(){var $=Holder($$$cl15.String("ok",2));return $$$cl15.JsCallable($, $.get)})()().getString().equals($$$cl15.String("ok",2)),$$$cl15.String("holder(ok)",10));
    assert(Holder($$$cl15.String("ok",2)).getString().equals($$$cl15.String("ok",2)),$$$cl15.String("holder.string",13));
    assert((function(){var $=Wrapper();return $$$cl15.JsCallable($, $.get)})()().getString().equals($$$cl15.String("100",3)),$$$cl15.String("wrapper 1",9));
    assert(Wrapper().getString().equals($$$cl15.String("100",3)),$$$cl15.String("wrapper 2",9));
    assert((function(){var $=Unwrapper();return $$$cl15.JsCallable($, $.get)})()().getString().equals($$$cl15.String("23.56",5)),$$$cl15.String("unwrapper 1",11));
    assert(Unwrapper().getO().getString().equals($$$cl15.String("23.56",5)),$$$cl15.String("unwrapper 2",11));
    assert(Unwrapper().getString().equals($$$cl15.String("23.56",5)),$$$cl15.String("unwrapper 3",11));
    assert($$$cl15.isOfType(producer()(),'ceylon.language.Integer'),$$$cl15.String("function 2",10));
    assert($$$cl15.Integer(123).equals(producer()()),$$$cl15.String("function 3",10));
    assert($$$cl15.String("something",9).equals(returner($$$cl15.String("something",9))()),$$$cl15.String("function 4",10));
    assert((function(){var $=A().B().C();return $$$cl15.JsCallable($, $.foobar)})()().equals($$$cl15.String("foo",3)),$$$cl15.String("foobar",6));
    assert((function(){var $=A().B().C();return $$$cl15.JsCallable($, $.quxx)})()().equals($$$cl15.String("qux",3)),$$$cl15.String("quxx",4));
    assert((function(){var $=A();return $$$cl15.JsCallable($, $.baz)})()().equals($$$cl15.String("foo",3)),$$$cl15.String("baz",3));
    assert((function(){var $=O();return $$$cl15.JsCallable($, $.test1)})()().equals($$$cl15.String("hello",5)),$$$cl15.String("method instantiating inner class",32));
    assert((function(){var $=O();return $$$cl15.JsCallable($, $.test2)})()().equals($$$cl15.String("hello",5)),$$$cl15.String("method accessing inner object",29));
    assert((function(){var $=O();return $$$cl15.JsCallable($, $.test3)})()().equals($$$cl15.String("hello",5)),$$$cl15.String("method deriving inner interface",31));
    assert((function(){var $=OuterC1();return $$$cl15.JsCallable($, $.tst)})()().equals($$$cl15.String("OuterC1.A.tst()",15)),$$$cl15.String("",0));
    assert(outerf().equals($$$cl15.String("outerf.A.tst()",14)),$$$cl15.String("",0));
    assert((function(){var $=OuterC2();return $$$cl15.JsCallable($, $.tst)})()().equals($$$cl15.String("OuterC2.A.tst()",15)),$$$cl15.String("",0));
    Outer($$$cl15.String("Hello",5));
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
