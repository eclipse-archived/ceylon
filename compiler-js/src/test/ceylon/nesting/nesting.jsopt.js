(function(define) { define(function(require, exports, module) {
var $$$cl160=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at nesting.ceylon (1:0-1:34)
var assertionCount$161=$$$cl160.Integer(0);
var getAssertionCount=function(){return assertionCount$161;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount$162){assertionCount$161=assertionCount$162; return assertionCount$161;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at nesting.ceylon (2:0-2:32)
var failureCount$163=$$$cl160.Integer(0);
var getFailureCount=function(){return failureCount$163;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount$164){failureCount$163=failureCount$164; return failureCount$163;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at nesting.ceylon (4:0-10:0)
function assert(assertion$165,message$166){
    if(message$166===undefined){message$166=$$$cl160.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl160.Integer(1))),getAssertionCount());
    if ((assertion$165.equals($$$cl160.getFalse()))===$$$cl160.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl160.Integer(1))),getFailureCount());
        $$$cl160.print($$$cl160.StringBuilder().appendAll($$$cl160.ArraySequence([$$$cl160.String("assertion failed \""),message$166.getString(),$$$cl160.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at nesting.ceylon (12:0-14:0)
function fail(message$167){
    assert($$$cl160.getFalse(),message$167);
}
exports.fail=fail;

//MethodDefinition results at nesting.ceylon (16:0-19:0)
function results(){
    $$$cl160.print($$$cl160.StringBuilder().appendAll($$$cl160.ArraySequence([$$$cl160.String("assertions ",11),getAssertionCount().getString(),$$$cl160.String(", failures ",11),getFailureCount().getString(),$$$cl160.String("",0)])).getString());
}
exports.results=results;

//ClassDefinition Outer at nesting.ceylon (21:0-46:0)
function Outer(name$168$, $$outer){
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$168$=name$168$;
    
    //AttributeDeclaration int at nesting.ceylon (22:4-22:18)
    $$outer.int$169$=$$$cl160.Integer(10);
    
    //AttributeDeclaration float at nesting.ceylon (23:4-23:34)
    $$outer.float$170$=$$outer.getInt$169$().getFloat();
    
    //AttributeDeclaration inner at nesting.ceylon (39:4-39:25)
    $$outer.inner$171$=$$outer.Inner$172$();
    $$$cl160.print($$outer.getInner$171$().getInt());
    $$$cl160.print($$outer.getInner$171$().getFloat());
    $$outer.getInner$171$().noop();
    $$outer.noop$173$();
    return $$outer;
}
exports.Outer=Outer;
$$$cl160.initTypeProto(Outer,'nesting.Outer',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration int at nesting.ceylon (22:4-22:18)
    $proto$.getInt$169$=function getInt$169$(){
        return this.int$169$;
    }
    
    //AttributeDeclaration float at nesting.ceylon (23:4-23:34)
    $proto$.getFloat=function getFloat(){
        return this.float$170$;
    }
    
    //MethodDefinition noop at nesting.ceylon (24:4-24:17)
    $proto$.noop$173$=function noop$173$(){
        var $$outer=this;
        
    }
    
    //ClassDefinition Inner at nesting.ceylon (25:4-38:4)
    function Inner$172$($$inner$172){
        if ($$inner$172===undefined)$$inner$172=new this.Inner$172$.$$;
        $$inner$172.$$outer=this;
        return $$inner$172;
    }
    $$$cl160.initTypeProto(Inner$172$,'nesting.Outer.Inner',$$$cl160.IdentifiableObject);
    ;(function($proto$){
        
        //MethodDefinition printName at nesting.ceylon (26:8-28:8)
        $proto$.printName$174$=function printName$174$(){
            var $$inner$172=this;
            $$$cl160.print($$inner$172.$$outer.name$168$);
        }
        
        //AttributeGetterDefinition int at nesting.ceylon (29:8-31:8)
        $proto$.getInt=function getInt(){
            var $$inner$172=this;
            return $$inner$172.$$outer.getInt$169$();
        }
        
        //AttributeGetterDefinition float at nesting.ceylon (32:8-34:8)
        $proto$.getFloat=function getFloat(){
            var $$inner$172=this;
            return $$inner$172.$$outer.getFloat();
        }
        
        //MethodDefinition noop at nesting.ceylon (35:8-37:8)
        $proto$.noop=function noop(){
            var $$inner$172=this;
            $$inner$172.$$outer.noop$173$();
        }
        
    })(Inner$172$.$$.prototype);
    $proto$.Inner$172$=Inner$172$;
    
    //AttributeDeclaration inner at nesting.ceylon (39:4-39:25)
    $proto$.getInner$171$=function getInner$171$(){
        return this.inner$171$;
    }
    
})(Outer.$$.prototype);

//MethodDefinition outr at nesting.ceylon (48:0-60:0)
function outr(name$175){
    
    //AttributeDeclaration uname at nesting.ceylon (49:4-49:34)
    var uname$176=name$175.getUppercased();
    
    //MethodDefinition inr at nesting.ceylon (50:4-52:4)
    function inr$177(){
        return name$175;
    }
    
    //AttributeGetterDefinition uinr at nesting.ceylon (53:4-55:4)
    var getUinr$178=function(){
        return uname$176;
    }
    
    //AttributeDeclaration result at nesting.ceylon (56:4-56:25)
    var result$179=inr$177();
    
    //AttributeDeclaration uresult at nesting.ceylon (57:4-57:25)
    var uresult$180=getUinr$178();
    $$$cl160.print(result$179);
    $$$cl160.print(uresult$180);
}
exports.outr=outr;

//ClassDefinition Holder at nesting.ceylon (62:0-69:0)
function Holder(o$181$, $$holder){
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$181$=o$181$;
    return $$holder;
}
exports.Holder=Holder;
$$$cl160.initTypeProto(Holder,'nesting.Holder',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //MethodDefinition get at nesting.ceylon (63:4-65:4)
    $proto$.get=function get(){
        var $$holder=this;
        return $$holder.o$181$;
    }
    
    //AttributeGetterDefinition string at nesting.ceylon (66:4-68:4)
    $proto$.getString=function getString(){
        var $$holder=this;
        return $$holder.o$181$.getString();
    }
    
})(Holder.$$.prototype);

//ClassDefinition Wrapper at nesting.ceylon (71:0-79:0)
function Wrapper($$wrapper){
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (72:4-72:18)
    $$wrapper.o$182$=$$$cl160.Integer(100);
    return $$wrapper;
}
exports.Wrapper=Wrapper;
$$$cl160.initTypeProto(Wrapper,'nesting.Wrapper',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration o at nesting.ceylon (72:4-72:18)
    $proto$.getO$182$=function getO$182$(){
        return this.o$182$;
    }
    
    //MethodDefinition get at nesting.ceylon (73:4-75:4)
    $proto$.get=function get(){
        var $$wrapper=this;
        return $$wrapper.getO$182$();
    }
    
    //AttributeGetterDefinition string at nesting.ceylon (76:4-78:4)
    $proto$.getString=function getString(){
        var $$wrapper=this;
        return $$wrapper.getO$182$().getString();
    }
    
})(Wrapper.$$.prototype);

//ClassDefinition Unwrapper at nesting.ceylon (81:0-89:0)
function Unwrapper($$unwrapper){
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (82:4-82:27)
    $$unwrapper.o$183$=$$$cl160.Float(23.56);
    return $$unwrapper;
}
exports.Unwrapper=Unwrapper;
$$$cl160.initTypeProto(Unwrapper,'nesting.Unwrapper',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration o at nesting.ceylon (82:4-82:27)
    $proto$.getO=function getO(){
        return this.o$183$;
    }
    
    //MethodDefinition get at nesting.ceylon (83:4-85:4)
    $proto$.get=function get(){
        var $$unwrapper=this;
        return $$unwrapper.getO();
    }
    
    //AttributeGetterDefinition string at nesting.ceylon (86:4-88:4)
    $proto$.getString=function getString(){
        var $$unwrapper=this;
        return $$unwrapper.getO().getString();
    }
    
})(Unwrapper.$$.prototype);

//MethodDefinition producer at nesting.ceylon (91:0-95:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (92:4-92:18)
    var o$184=$$$cl160.Integer(123);
    
    //MethodDefinition produce at nesting.ceylon (93:4-93:35)
    function produce$185(){
        return o$184;
    }
    return produce$185;
}

//MethodDefinition returner at nesting.ceylon (97:0-100:0)
function returner(o$186){
    
    //MethodDefinition produce at nesting.ceylon (98:4-98:35)
    function produce$187(){
        return o$186;
    }
    return produce$187;
}

//ClassDefinition A at nesting.ceylon (102:0-123:0)
function A($$a){
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDeclaration foo at nesting.ceylon (103:4-103:22)
    $$a.foo$188$=$$$cl160.String("foo",3);
    return $$a;
}
$$$cl160.initTypeProto(A,'nesting.A',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration foo at nesting.ceylon (103:4-103:22)
    $proto$.getFoo$188$=function getFoo$188$(){
        return this.foo$188$;
    }
    
    //ClassDefinition B at nesting.ceylon (104:4-114:4)
    function B$A($$b$A){
        if ($$b$A===undefined)$$b$A=new this.B$A.$$;
        $$b$A.$$a=this;
        
        //AttributeDeclaration qux at nesting.ceylon (105:8-105:26)
        $$b$A.qux$189$=$$$cl160.String("qux",3);
        return $$b$A;
    }
    $$$cl160.initTypeProto(B$A,'nesting.A.B',$$$cl160.IdentifiableObject);
    ;(function($proto$){
        
        //AttributeDeclaration qux at nesting.ceylon (105:8-105:26)
        $proto$.getQux$189$=function getQux$189$(){
            return this.qux$189$;
        }
        
        //ClassDefinition C at nesting.ceylon (106:8-113:8)
        function C$B$A($$c$B$A){
            if ($$c$B$A===undefined)$$c$B$A=new this.C$B$A.$$;
            $$c$B$A.$$b$A=this;
            return $$c$B$A;
        }
        $$$cl160.initTypeProto(C$B$A,'nesting.A.B.C',$$$cl160.IdentifiableObject);
        ;(function($proto$){
            
            //MethodDefinition foobar at nesting.ceylon (107:12-109:12)
            $proto$.foobar=function foobar(){
                var $$c$B$A=this;
                return $$c$B$A.$$b$A.$$a.getFoo$188$();
            }
            
            //MethodDefinition quxx at nesting.ceylon (110:12-112:12)
            $proto$.quxx=function quxx(){
                var $$c$B$A=this;
                return $$c$B$A.$$b$A.getQux$189$();
            }
            
        })(C$B$A.$$.prototype);
        $proto$.C$B$A=C$B$A;
        
    })(B$A.$$.prototype);
    $proto$.B$A=B$A;
    
    //MethodDefinition baz at nesting.ceylon (115:4-122:4)
    $proto$.baz=function baz(){
        var $$a=this;
        
        //ClassDefinition Baz at nesting.ceylon (116:8-120:8)
        function Baz$190($$baz$190){
            if ($$baz$190===undefined)$$baz$190=new Baz$190.$$;
            return $$baz$190;
        }
        $$$cl160.initTypeProto(Baz$190,'nesting.A.baz.Baz',$$$cl160.IdentifiableObject);
        ;(function($proto$){
            
            //MethodDefinition get at nesting.ceylon (117:12-119:12)
            $proto$.get=function get(){
                var $$baz$190=this;
                return $$a.getFoo$188$();
            }
            
        })(Baz$190.$$.prototype);
        return Baz$190().get();
    }
    
})(A.$$.prototype);

//ClassDefinition O at nesting.ceylon (125:0-152:0)
function O($$o){
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDeclaration s at nesting.ceylon (126:4-126:22)
    $$o.s$191$=$$$cl160.String("hello",5);
    
    //ObjectDefinition innerObject at nesting.ceylon (132:4-136:4)
    $$o.innerObject$192$=$$o.innerObject$193$();
    return $$o;
}
$$$cl160.initTypeProto(O,'nesting.O',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //AttributeDeclaration s at nesting.ceylon (126:4-126:22)
    $proto$.getS$191$=function getS$191$(){
        return this.s$191$;
    }
    
    //ClassDefinition InnerClass at nesting.ceylon (127:4-131:4)
    function InnerClass$194$($$innerClass$194){
        if ($$innerClass$194===undefined)$$innerClass$194=new this.InnerClass$194$.$$;
        $$innerClass$194.$$o=this;
        return $$innerClass$194;
    }
    $$$cl160.initTypeProto(InnerClass$194$,'nesting.O.InnerClass',$$$cl160.IdentifiableObject);
    ;(function($proto$){
        
        //MethodDefinition f at nesting.ceylon (128:8-130:8)
        $proto$.f=function f(){
            var $$innerClass$194=this;
            return $$innerClass$194.$$o.getS$191$();
        }
        
    })(InnerClass$194$.$$.prototype);
    $proto$.InnerClass$194$=InnerClass$194$;
    
    //ObjectDefinition innerObject at nesting.ceylon (132:4-136:4)
    function innerObject$193$(){
        var $$innerObject$193=new this.innerObject$193$.$$;
        $$innerObject$193.$$o=this;
        return $$innerObject$193;
    }
    $$$cl160.initTypeProto(innerObject$193$,'nesting.O.innerObject',$$$cl160.IdentifiableObject);
    $proto$.getInnerObject$192$=function getInnerObject$192$(){
        return this.innerObject$192$;
    }
    ;(function($proto$){
        
        //MethodDefinition f at nesting.ceylon (133:8-135:8)
        $proto$.f=function f(){
            var $$innerObject$193=this;
            return $$innerObject$193.$$o.getS$191$();
        }
        
    })(innerObject$193$.$$.prototype);
    $proto$.innerObject$193$=innerObject$193$;
    
    //InterfaceDefinition InnerInterface at nesting.ceylon (137:4-141:4)
    function InnerInterface$195$($$innerInterface$195){
        $$innerInterface$195.$$o=this;
        
    }
    $$$cl160.initTypeProtoI(InnerInterface$195$,'nesting.O.InnerInterface');
    ;(function($proto$){
        
        //MethodDefinition f at nesting.ceylon (138:8-140:8)
        $proto$.f=function f(){
            var $$innerInterface$195=this;
            return $$innerInterface$195.$$o.getS$191$();
        }
        
    })(InnerInterface$195$.$$.prototype);
    $proto$.InnerInterface$195$=InnerInterface$195$;
    
    //MethodDefinition test1 at nesting.ceylon (142:4-144:4)
    $proto$.test1=function test1(){
        var $$o=this;
        return $$o.InnerClass$194$().f();
    }
    
    //MethodDefinition test2 at nesting.ceylon (145:4-147:4)
    $proto$.test2=function test2(){
        var $$o=this;
        return $$o.getInnerObject$192$().f();
    }
    
    //MethodDefinition test3 at nesting.ceylon (148:4-151:4)
    $proto$.test3=function test3(){
        var $$o=this;
        
        //ObjectDefinition obj at nesting.ceylon (149:8-149:45)
        function obj$196(){
            var $$obj$196=new obj$196.$$;
            $$o.InnerInterface$195$($$obj$196);
            return $$obj$196;
        }
        $$$cl160.initTypeProto(obj$196,'nesting.O.test3.obj',$$$cl160.IdentifiableObject,$$o.InnerInterface$195$);
        var obj$197=obj$196(new obj$196.$$);
        function getObj$197(){
            return obj$197;
        }
        return getObj$197().f();
    }
    
})(O.$$.prototype);

//ClassDefinition OuterC1 at nesting.ceylon (154:0-160:0)
function OuterC1($$outerC1){
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    return $$outerC1;
}
$$$cl160.initTypeProto(OuterC1,'nesting.OuterC1',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //ClassDefinition A at nesting.ceylon (155:4-157:4)
    function A$198$($$a$198){
        if ($$a$198===undefined)$$a$198=new this.A$198$.$$;
        $$a$198.$$outerC1=this;
        return $$a$198;
    }
    $$$cl160.initTypeProto(A$198$,'nesting.OuterC1.A',$$$cl160.IdentifiableObject);
    ;(function($proto$){
        
        //MethodDefinition tst at nesting.ceylon (156:8-156:55)
        $proto$.tst=function tst(){
            var $$a$198=this;
            return $$$cl160.String("OuterC1.A.tst()",15);
        }
        
    })(A$198$.$$.prototype);
    $proto$.A$198$=A$198$;
    
    //ClassDefinition B at nesting.ceylon (158:4-158:27)
    function B$199$($$b$199){
        if ($$b$199===undefined)$$b$199=new this.B$199$.$$;
        $$b$199.$$outerC1=this;
        $$b$199.$$outerC1.A$198$($$b$199);
        return $$b$199;
    }
    $$$cl160.initTypeProto(B$199$,'nesting.OuterC1.B',A$198$);
    $proto$.B$199$=B$199$;
    
    //MethodDefinition tst at nesting.ceylon (159:4-159:42)
    $proto$.tst=function tst(){
        var $$outerC1=this;
        return $$outerC1.B$199$().tst();
    }
    
})(OuterC1.$$.prototype);

//MethodDefinition outerf at nesting.ceylon (162:0-168:0)
function outerf(){
    
    //ClassDefinition A at nesting.ceylon (163:4-165:4)
    function A$200($$a$200){
        if ($$a$200===undefined)$$a$200=new A$200.$$;
        return $$a$200;
    }
    $$$cl160.initTypeProto(A$200,'nesting.outerf.A',$$$cl160.IdentifiableObject);
    ;(function($proto$){
        
        //MethodDefinition tst at nesting.ceylon (164:8-164:54)
        $proto$.tst=function tst(){
            var $$a$200=this;
            return $$$cl160.String("outerf.A.tst()",14);
        }
        
    })(A$200.$$.prototype);
    
    //ClassDefinition B at nesting.ceylon (166:4-166:27)
    function B$201($$b$201){
        if ($$b$201===undefined)$$b$201=new B$201.$$;
        A$200($$b$201);
        return $$b$201;
    }
    $$$cl160.initTypeProto(B$201,'nesting.outerf.B',A$200);
    return B$201().tst();
}

//ClassDefinition OuterC2 at nesting.ceylon (170:0-178:0)
function OuterC2($$outerC2){
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    return $$outerC2;
}
$$$cl160.initTypeProto(OuterC2,'nesting.OuterC2',$$$cl160.IdentifiableObject);
;(function($proto$){
    
    //ClassDefinition A at nesting.ceylon (171:4-173:4)
    function A$202$($$a$202){
        if ($$a$202===undefined)$$a$202=new this.A$202$.$$;
        $$a$202.$$outerC2=this;
        return $$a$202;
    }
    $$$cl160.initTypeProto(A$202$,'nesting.OuterC2.A',$$$cl160.IdentifiableObject);
    ;(function($proto$){
        
        //MethodDefinition tst at nesting.ceylon (172:8-172:55)
        $proto$.tst=function tst(){
            var $$a$202=this;
            return $$$cl160.String("OuterC2.A.tst()",15);
        }
        
    })(A$202$.$$.prototype);
    $proto$.A$202$=A$202$;
    
    //MethodDefinition tst at nesting.ceylon (174:4-177:4)
    $proto$.tst=function tst(){
        var $$outerC2=this;
        
        //ClassDefinition B at nesting.ceylon (175:8-175:31)
        function B$203($$b$203){
            if ($$b$203===undefined)$$b$203=new B$203.$$;
            $$outerC2.A$202$($$b$203);
            return $$b$203;
        }
        $$$cl160.initTypeProto(B$203,'nesting.OuterC2.tst.B',$$outerC2.A$202$);
        return B$203().tst();
    }
    
})(OuterC2.$$.prototype);

//MethodDefinition test at nesting.ceylon (180:0-204:0)
function test(){
    outr($$$cl160.String("Hello",5));
    assert(Holder($$$cl160.String("ok",2)).get().getString().equals($$$cl160.String("ok",2)),$$$cl160.String("holder(ok)",10));
    assert(Holder($$$cl160.String("ok",2)).getString().equals($$$cl160.String("ok",2)),$$$cl160.String("holder.string",13));
    assert(Wrapper().get().getString().equals($$$cl160.String("100",3)),$$$cl160.String("wrapper 1",9));
    assert(Wrapper().getString().equals($$$cl160.String("100",3)),$$$cl160.String("wrapper 2",9));
    assert(Unwrapper().get().getString().equals($$$cl160.String("23.56",5)),$$$cl160.String("unwrapper 1",11));
    assert(Unwrapper().getO().getString().equals($$$cl160.String("23.56",5)),$$$cl160.String("unwrapper 2",11));
    assert(Unwrapper().getString().equals($$$cl160.String("23.56",5)),$$$cl160.String("unwrapper 3",11));
    assert($$$cl160.isOfType(producer()(),'ceylon.language.Integer'),$$$cl160.String("function 2",10));
    assert($$$cl160.Integer(123).equals(producer()()),$$$cl160.String("function 3",10));
    assert($$$cl160.String("something",9).equals(returner($$$cl160.String("something",9))()),$$$cl160.String("function 4",10));
    assert(A().B$A().C$B$A().foobar().equals($$$cl160.String("foo",3)),$$$cl160.String("foobar",6));
    assert(A().B$A().C$B$A().quxx().equals($$$cl160.String("qux",3)),$$$cl160.String("quxx",4));
    assert(A().baz().equals($$$cl160.String("foo",3)),$$$cl160.String("baz",3));
    assert(O().test1().equals($$$cl160.String("hello",5)),$$$cl160.String("method instantiating inner class",32));
    assert(O().test2().equals($$$cl160.String("hello",5)),$$$cl160.String("method accessing inner object",29));
    assert(O().test3().equals($$$cl160.String("hello",5)),$$$cl160.String("method deriving inner interface",31));
    assert(OuterC1().tst().equals($$$cl160.String("OuterC1.A.tst()",15)),$$$cl160.String("",0));
    assert(outerf().equals($$$cl160.String("outerf.A.tst()",14)),$$$cl160.String("",0));
    assert(OuterC2().tst().equals($$$cl160.String("OuterC2.A.tst()",15)),$$$cl160.String("",0));
    Outer($$$cl160.String("Hello",5));
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
