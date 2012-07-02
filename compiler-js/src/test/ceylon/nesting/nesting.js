(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.3.1/ceylon.language');
var $$$a12=require('default/assert');

//ClassDefinition Outer at nesting.ceylon (3:0-28:0)
function Outer(name$330, $$outer){
    $init$Outer();
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$330=name$330;
    
    //AttributeDeclaration int at nesting.ceylon (4:4-4:18)
    var int$331=$$$cl1.Integer(10);
    var getInt$331=function(){return int$331;};
    $$outer.getInt$331=getInt$331;
    
    //AttributeDeclaration float at nesting.ceylon (5:4-5:34)
    var float$332=getInt$331().getFloat();
    var getFloat=function(){return float$332;};
    $$outer.getFloat=getFloat;
    
    //MethodDefinition noop at nesting.ceylon (6:4-6:17)
    function noop$333(){
        
    }
    $$outer.noop$333=noop$333;
    
    //ClassDefinition Inner at nesting.ceylon (7:4-20:4)
    function Inner$334($$inner$334){
        $init$Inner$334();
        if ($$inner$334===undefined)$$inner$334=new Inner$334.$$;
        
        //MethodDefinition printName at nesting.ceylon (8:8-10:8)
        function printName$335(){
            $$$cl1.print(name$330);
        }
        
        //AttributeGetterDefinition int at nesting.ceylon (11:8-13:8)
        var getInt=function(){
            return $$outer.getInt$331();
        }
        $$inner$334.getInt=getInt;
        
        //AttributeGetterDefinition float at nesting.ceylon (14:8-16:8)
        var getFloat=function(){
            return $$outer.getFloat();
        }
        $$inner$334.getFloat=getFloat;
        
        //MethodDefinition noop at nesting.ceylon (17:8-19:8)
        function noop(){
            $$outer.noop$333();
        }
        $$inner$334.noop=noop;
        return $$inner$334;
    }
    function $init$Inner$334(){
        if (Inner$334.$$===undefined){
            $$$cl1.initTypeProto(Inner$334,'nesting.Outer.Inner',$$$cl1.IdentifiableObject);
        }
        return Inner$334;
    }
    $$outer.$init$Inner$334=$init$Inner$334;
    $init$Inner$334();
    
    //AttributeDeclaration inner at nesting.ceylon (21:4-21:25)
    var inner$336=Inner$334();
    $$$cl1.print(inner$336.getInt());
    $$$cl1.print(inner$336.getFloat());
    inner$336.noop();
    noop$333();
    return $$outer;
}
exports.Outer=Outer;
function $init$Outer(){
    if (Outer.$$===undefined){
        $$$cl1.initTypeProto(Outer,'nesting.Outer',$$$cl1.IdentifiableObject);
    }
    return Outer;
}
exports.$init$Outer=$init$Outer;
$init$Outer();

//MethodDefinition outr at nesting.ceylon (30:0-42:0)
function outr(name$337){
    
    //AttributeDeclaration uname at nesting.ceylon (31:4-31:34)
    var uname$338=name$337.getUppercased();
    
    //MethodDefinition inr at nesting.ceylon (32:4-34:4)
    function inr$339(){
        return name$337;
    }
    
    //AttributeGetterDefinition uinr at nesting.ceylon (35:4-37:4)
    var getUinr$340=function(){
        return uname$338;
    }
    
    //AttributeDeclaration result at nesting.ceylon (38:4-38:25)
    var result$341=inr$339();
    
    //AttributeDeclaration uresult at nesting.ceylon (39:4-39:25)
    var uresult$342=getUinr$340();
    $$$cl1.print(result$341);
    $$$cl1.print(uresult$342);
}
exports.outr=outr;

//ClassDefinition Holder at nesting.ceylon (44:0-51:0)
function Holder(o$343, $$holder){
    $init$Holder();
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$343=o$343;
    
    //MethodDefinition get at nesting.ceylon (45:4-47:4)
    function get(){
        return o$343;
    }
    $$holder.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (48:4-50:4)
    var getString=function(){
        return o$343.getString();
    }
    $$holder.getString=getString;
    return $$holder;
}
exports.Holder=Holder;
function $init$Holder(){
    if (Holder.$$===undefined){
        $$$cl1.initTypeProto(Holder,'nesting.Holder',$$$cl1.IdentifiableObject);
    }
    return Holder;
}
exports.$init$Holder=$init$Holder;
$init$Holder();

//ClassDefinition Wrapper at nesting.ceylon (53:0-61:0)
function Wrapper($$wrapper){
    $init$Wrapper();
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (54:4-54:18)
    var o$344=$$$cl1.Integer(100);
    var getO$344=function(){return o$344;};
    $$wrapper.getO$344=getO$344;
    
    //MethodDefinition get at nesting.ceylon (55:4-57:4)
    function get(){
        return getO$344();
    }
    $$wrapper.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (58:4-60:4)
    var getString=function(){
        return getO$344().getString();
    }
    $$wrapper.getString=getString;
    return $$wrapper;
}
exports.Wrapper=Wrapper;
function $init$Wrapper(){
    if (Wrapper.$$===undefined){
        $$$cl1.initTypeProto(Wrapper,'nesting.Wrapper',$$$cl1.IdentifiableObject);
    }
    return Wrapper;
}
exports.$init$Wrapper=$init$Wrapper;
$init$Wrapper();

//ClassDefinition Unwrapper at nesting.ceylon (63:0-71:0)
function Unwrapper($$unwrapper){
    $init$Unwrapper();
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (64:4-64:27)
    var o$345=$$$cl1.Float(23.56);
    var getO=function(){return o$345;};
    $$unwrapper.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (65:4-67:4)
    function get(){
        return $$unwrapper.getO();
    }
    $$unwrapper.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (68:4-70:4)
    var getString=function(){
        return $$unwrapper.getO().getString();
    }
    $$unwrapper.getString=getString;
    return $$unwrapper;
}
exports.Unwrapper=Unwrapper;
function $init$Unwrapper(){
    if (Unwrapper.$$===undefined){
        $$$cl1.initTypeProto(Unwrapper,'nesting.Unwrapper',$$$cl1.IdentifiableObject);
    }
    return Unwrapper;
}
exports.$init$Unwrapper=$init$Unwrapper;
$init$Unwrapper();

//MethodDefinition producer at nesting.ceylon (73:0-77:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (74:4-74:18)
    var o$346=$$$cl1.Integer(123);
    
    //MethodDefinition produce at nesting.ceylon (75:4-75:35)
    function produce$347(){
        return o$346;
    }
    return produce$347;
}

//MethodDefinition returner at nesting.ceylon (79:0-82:0)
function returner(o$348){
    
    //MethodDefinition produce at nesting.ceylon (80:4-80:35)
    function produce$349(){
        return o$348;
    }
    return produce$349;
}

//ClassDefinition A at nesting.ceylon (84:0-105:0)
function A($$a){
    $init$A();
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDeclaration foo at nesting.ceylon (85:4-85:22)
    var foo$350=$$$cl1.String("foo",3);
    var getFoo$350=function(){return foo$350;};
    $$a.getFoo$350=getFoo$350;
    
    //ClassDefinition B at nesting.ceylon (86:4-96:4)
    function B$A($$b$A){
        $init$B$A();
        if ($$b$A===undefined)$$b$A=new B$A.$$;
        
        //AttributeDeclaration qux at nesting.ceylon (87:8-87:26)
        var qux$351=$$$cl1.String("qux",3);
        var getQux$351=function(){return qux$351;};
        $$b$A.getQux$351=getQux$351;
        
        //ClassDefinition C at nesting.ceylon (88:8-95:8)
        function C$B$A($$c$B$A){
            $init$C$B$A();
            if ($$c$B$A===undefined)$$c$B$A=new C$B$A.$$;
            
            //MethodDefinition foobar at nesting.ceylon (89:12-91:12)
            function foobar(){
                return getFoo$350();
            }
            $$c$B$A.foobar=foobar;
            
            //MethodDefinition quxx at nesting.ceylon (92:12-94:12)
            function quxx(){
                return getQux$351();
            }
            $$c$B$A.quxx=quxx;
            return $$c$B$A;
        }
        $$b$A.C$B$A=C$B$A;
        function $init$C$B$A(){
            if (C$B$A.$$===undefined){
                $$$cl1.initTypeProto(C$B$A,'nesting.A.B.C',$$$cl1.IdentifiableObject);
            }
            return C$B$A;
        }
        $$b$A.$init$C$B$A=$init$C$B$A;
        $init$C$B$A();
        return $$b$A;
    }
    $$a.B$A=B$A;
    function $init$B$A(){
        if (B$A.$$===undefined){
            $$$cl1.initTypeProto(B$A,'nesting.A.B',$$$cl1.IdentifiableObject);
        }
        return B$A;
    }
    $$a.$init$B$A=$init$B$A;
    $init$B$A();
    
    //MethodDefinition baz at nesting.ceylon (97:4-104:4)
    function baz(){
        
        //ClassDefinition Baz at nesting.ceylon (98:8-102:8)
        function Baz$352($$baz$352){
            $init$Baz$352();
            if ($$baz$352===undefined)$$baz$352=new Baz$352.$$;
            
            //MethodDefinition get at nesting.ceylon (99:12-101:12)
            function get(){
                return getFoo$350();
            }
            $$baz$352.get=get;
            return $$baz$352;
        }
        function $init$Baz$352(){
            if (Baz$352.$$===undefined){
                $$$cl1.initTypeProto(Baz$352,'nesting.A.baz.Baz',$$$cl1.IdentifiableObject);
            }
            return Baz$352;
        }
        $init$Baz$352();
        return Baz$352().get();
    }
    $$a.baz=baz;
    return $$a;
}
function $init$A(){
    if (A.$$===undefined){
        $$$cl1.initTypeProto(A,'nesting.A',$$$cl1.IdentifiableObject);
    }
    return A;
}
exports.$init$A=$init$A;
$init$A();

//ClassDefinition O at nesting.ceylon (107:0-134:0)
function O($$o){
    $init$O();
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDeclaration s at nesting.ceylon (108:4-108:22)
    var s$353=$$$cl1.String("hello",5);
    var getS$353=function(){return s$353;};
    $$o.getS$353=getS$353;
    
    //ClassDefinition InnerClass at nesting.ceylon (109:4-113:4)
    function InnerClass$354($$innerClass$354){
        $init$InnerClass$354();
        if ($$innerClass$354===undefined)$$innerClass$354=new InnerClass$354.$$;
        
        //MethodDefinition f at nesting.ceylon (110:8-112:8)
        function f(){
            return getS$353();
        }
        $$innerClass$354.f=f;
        return $$innerClass$354;
    }
    function $init$InnerClass$354(){
        if (InnerClass$354.$$===undefined){
            $$$cl1.initTypeProto(InnerClass$354,'nesting.O.InnerClass',$$$cl1.IdentifiableObject);
        }
        return InnerClass$354;
    }
    $$o.$init$InnerClass$354=$init$InnerClass$354;
    $init$InnerClass$354();
    
    //ObjectDefinition innerObject at nesting.ceylon (114:4-118:4)
    function innerObject$355(){
        var $$innerObject$355=new innerObject$355.$$;
        
        //MethodDefinition f at nesting.ceylon (115:8-117:8)
        function f(){
            return getS$353();
        }
        $$innerObject$355.f=f;
        return $$innerObject$355;
    }
    function $init$innerObject$355(){
        if (innerObject$355.$$===undefined){
            $$$cl1.initTypeProto(innerObject$355,'nesting.O.innerObject',$$$cl1.IdentifiableObject);
        }
        return innerObject$355;
    }
    $$o.$init$innerObject$355=$init$innerObject$355;
    $init$innerObject$355();
    var innerObject$356=innerObject$355(new innerObject$355.$$);
    var getInnerObject$356=function(){
        return innerObject$356;
    }
    
    //InterfaceDefinition InnerInterface at nesting.ceylon (119:4-123:4)
    function InnerInterface$357($$innerInterface$357){
        
        //MethodDefinition f at nesting.ceylon (120:8-122:8)
        function f(){
            return getS$353();
        }
        $$innerInterface$357.f=f;
        
    }
    function $init$InnerInterface$357(){
        if (InnerInterface$357.$$===undefined){
            $$$cl1.initTypeProtoI(InnerInterface$357,'nesting.O.InnerInterface');
        }
        return InnerInterface$357;
    }
    $$o.$init$InnerInterface$357=$init$InnerInterface$357;
    $init$InnerInterface$357();
    
    //MethodDefinition test1 at nesting.ceylon (124:4-126:4)
    function test1(){
        return InnerClass$354().f();
    }
    $$o.test1=test1;
    
    //MethodDefinition test2 at nesting.ceylon (127:4-129:4)
    function test2(){
        return getInnerObject$356().f();
    }
    $$o.test2=test2;
    
    //MethodDefinition test3 at nesting.ceylon (130:4-133:4)
    function test3(){
        
        //ObjectDefinition obj at nesting.ceylon (131:8-131:45)
        function obj$358(){
            var $$obj$358=new obj$358.$$;
            InnerInterface$357($$obj$358);
            return $$obj$358;
        }
        function $init$obj$358(){
            if (obj$358.$$===undefined){
                $$$cl1.initTypeProto(obj$358,'nesting.O.test3.obj',$$$cl1.IdentifiableObject,$init$InnerInterface$357());
            }
            return obj$358;
        }
        $init$obj$358();
        var obj$359=obj$358(new obj$358.$$);
        var getObj$359=function(){
            return obj$359;
        }
        return getObj$359().f();
    }
    $$o.test3=test3;
    return $$o;
}
function $init$O(){
    if (O.$$===undefined){
        $$$cl1.initTypeProto(O,'nesting.O',$$$cl1.IdentifiableObject);
    }
    return O;
}
exports.$init$O=$init$O;
$init$O();

//ClassDefinition OuterC1 at nesting.ceylon (136:0-142:0)
function OuterC1($$outerC1){
    $init$OuterC1();
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    
    //ClassDefinition A at nesting.ceylon (137:4-139:4)
    function A$360($$a$360){
        $init$A$360();
        if ($$a$360===undefined)$$a$360=new A$360.$$;
        
        //MethodDefinition tst at nesting.ceylon (138:8-138:55)
        function tst(){
            return $$$cl1.String("OuterC1.A.tst()",15);
        }
        $$a$360.tst=tst;
        return $$a$360;
    }
    function $init$A$360(){
        if (A$360.$$===undefined){
            $$$cl1.initTypeProto(A$360,'nesting.OuterC1.A',$$$cl1.IdentifiableObject);
        }
        return A$360;
    }
    $$outerC1.$init$A$360=$init$A$360;
    $init$A$360();
    
    //ClassDefinition B at nesting.ceylon (140:4-140:27)
    function B$361($$b$361){
        $init$B$361();
        if ($$b$361===undefined)$$b$361=new B$361.$$;
        A$360($$b$361);
        return $$b$361;
    }
    function $init$B$361(){
        if (B$361.$$===undefined){
            $$$cl1.initType(B$361,'nesting.OuterC1.B',A$360);
        }
        return B$361;
    }
    $$outerC1.$init$B$361=$init$B$361;
    $init$B$361();
    
    //MethodDefinition tst at nesting.ceylon (141:4-141:42)
    function tst(){
        return B$361().tst();
    }
    $$outerC1.tst=tst;
    return $$outerC1;
}
function $init$OuterC1(){
    if (OuterC1.$$===undefined){
        $$$cl1.initTypeProto(OuterC1,'nesting.OuterC1',$$$cl1.IdentifiableObject);
    }
    return OuterC1;
}
exports.$init$OuterC1=$init$OuterC1;
$init$OuterC1();

//MethodDefinition outerf at nesting.ceylon (144:0-150:0)
function outerf(){
    
    //ClassDefinition A at nesting.ceylon (145:4-147:4)
    function A$362($$a$362){
        $init$A$362();
        if ($$a$362===undefined)$$a$362=new A$362.$$;
        
        //MethodDefinition tst at nesting.ceylon (146:8-146:54)
        function tst(){
            return $$$cl1.String("outerf.A.tst()",14);
        }
        $$a$362.tst=tst;
        return $$a$362;
    }
    function $init$A$362(){
        if (A$362.$$===undefined){
            $$$cl1.initTypeProto(A$362,'nesting.outerf.A',$$$cl1.IdentifiableObject);
        }
        return A$362;
    }
    $init$A$362();
    
    //ClassDefinition B at nesting.ceylon (148:4-148:27)
    function B$363($$b$363){
        $init$B$363();
        if ($$b$363===undefined)$$b$363=new B$363.$$;
        A$362($$b$363);
        return $$b$363;
    }
    function $init$B$363(){
        if (B$363.$$===undefined){
            $$$cl1.initType(B$363,'nesting.outerf.B',A$362);
        }
        return B$363;
    }
    $init$B$363();
    return B$363().tst();
}

//ClassDefinition OuterC2 at nesting.ceylon (152:0-160:0)
function OuterC2($$outerC2){
    $init$OuterC2();
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    
    //ClassDefinition A at nesting.ceylon (153:4-155:4)
    function A$364($$a$364){
        $init$A$364();
        if ($$a$364===undefined)$$a$364=new A$364.$$;
        
        //MethodDefinition tst at nesting.ceylon (154:8-154:55)
        function tst(){
            return $$$cl1.String("OuterC2.A.tst()",15);
        }
        $$a$364.tst=tst;
        return $$a$364;
    }
    function $init$A$364(){
        if (A$364.$$===undefined){
            $$$cl1.initTypeProto(A$364,'nesting.OuterC2.A',$$$cl1.IdentifiableObject);
        }
        return A$364;
    }
    $$outerC2.$init$A$364=$init$A$364;
    $init$A$364();
    
    //MethodDefinition tst at nesting.ceylon (156:4-159:4)
    function tst(){
        
        //ClassDefinition B at nesting.ceylon (157:8-157:31)
        function B$365($$b$365){
            $init$B$365();
            if ($$b$365===undefined)$$b$365=new B$365.$$;
            A$364($$b$365);
            return $$b$365;
        }
        function $init$B$365(){
            if (B$365.$$===undefined){
                $$$cl1.initType(B$365,'nesting.OuterC2.tst.B',A$364);
            }
            return B$365;
        }
        $init$B$365();
        return B$365().tst();
    }
    $$outerC2.tst=tst;
    return $$outerC2;
}
function $init$OuterC2(){
    if (OuterC2.$$===undefined){
        $$$cl1.initTypeProto(OuterC2,'nesting.OuterC2',$$$cl1.IdentifiableObject);
    }
    return OuterC2;
}
exports.$init$OuterC2=$init$OuterC2;
$init$OuterC2();

//ClassDefinition NameTest at nesting.ceylon (162:0-178:0)
function NameTest($$nameTest){
    $init$NameTest();
    if ($$nameTest===undefined)$$nameTest=new NameTest.$$;
    
    //AttributeDeclaration x at nesting.ceylon (163:4-163:25)
    var x$366=$$$cl1.String("1",1);
    var getX=function(){return x$366;};
    $$nameTest.getX=getX;
    
    //ClassDefinition NameTest at nesting.ceylon (164:4-176:4)
    function NameTest$NameTest($$nameTest$NameTest){
        $init$NameTest$NameTest();
        if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new NameTest$NameTest.$$;
        
        //AttributeDeclaration x at nesting.ceylon (165:8-165:29)
        var x$367=$$$cl1.String("2",1);
        var getX=function(){return x$367;};
        $$nameTest$NameTest.getX=getX;
        
        //MethodDefinition f at nesting.ceylon (166:8-175:8)
        function f(){
            
            //ClassDefinition NameTest at nesting.ceylon (167:12-173:12)
            function NameTest$368($$nameTest$368){
                $init$NameTest$368();
                if ($$nameTest$368===undefined)$$nameTest$368=new NameTest$368.$$;
                
                //AttributeDeclaration x at nesting.ceylon (168:16-168:37)
                var x$369=$$$cl1.String("3",1);
                var getX=function(){return x$369;};
                $$nameTest$368.getX=getX;
                
                //ClassDefinition NameTest at nesting.ceylon (169:16-171:16)
                function NameTest$NameTest($$nameTest$NameTest){
                    $init$NameTest$NameTest();
                    if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new NameTest$NameTest.$$;
                    
                    //AttributeDeclaration x at nesting.ceylon (170:20-170:41)
                    var x$370=$$$cl1.String("4",1);
                    var getX=function(){return x$370;};
                    $$nameTest$NameTest.getX=getX;
                    return $$nameTest$NameTest;
                }
                $$nameTest$368.NameTest$NameTest=NameTest$NameTest;
                function $init$NameTest$NameTest(){
                    if (NameTest$NameTest.$$===undefined){
                        $$$cl1.initTypeProto(NameTest$NameTest,'nesting.NameTest.NameTest.f.NameTest.NameTest',$$$cl1.IdentifiableObject);
                    }
                    return NameTest$NameTest;
                }
                $$nameTest$368.$init$NameTest$NameTest=$init$NameTest$NameTest;
                $init$NameTest$NameTest();
                
                //MethodDefinition f at nesting.ceylon (172:16-172:66)
                function f(){
                    return $$nameTest$368.getX().plus($$nameTest$368.NameTest$NameTest().getX());
                }
                $$nameTest$368.f=f;
                return $$nameTest$368;
            }
            function $init$NameTest$368(){
                if (NameTest$368.$$===undefined){
                    $$$cl1.initTypeProto(NameTest$368,'nesting.NameTest.NameTest.f.NameTest',$$$cl1.IdentifiableObject);
                }
                return NameTest$368;
            }
            $init$NameTest$368();
            return $$nameTest.getX().plus($$nameTest$NameTest.getX()).plus(NameTest$368().f());
        }
        $$nameTest$NameTest.f=f;
        return $$nameTest$NameTest;
    }
    $$nameTest.NameTest$NameTest=NameTest$NameTest;
    function $init$NameTest$NameTest(){
        if (NameTest$NameTest.$$===undefined){
            $$$cl1.initTypeProto(NameTest$NameTest,'nesting.NameTest.NameTest',$$$cl1.IdentifiableObject);
        }
        return NameTest$NameTest;
    }
    $$nameTest.$init$NameTest$NameTest=$init$NameTest$NameTest;
    $init$NameTest$NameTest();
    
    //MethodDefinition f at nesting.ceylon (177:4-177:52)
    function f(){
        return $$nameTest.NameTest$NameTest().f();
    }
    $$nameTest.f=f;
    return $$nameTest;
}
exports.NameTest=NameTest;
function $init$NameTest(){
    if (NameTest.$$===undefined){
        $$$cl1.initTypeProto(NameTest,'nesting.NameTest',$$$cl1.IdentifiableObject);
    }
    return NameTest;
}
exports.$init$NameTest=$init$NameTest;
$init$NameTest();

//ObjectDefinition nameTest at nesting.ceylon (180:0-196:0)
function nameTest(){
    var $$nameTest=new nameTest.$$;
    
    //AttributeDeclaration x at nesting.ceylon (181:4-181:25)
    var x$371=$$$cl1.String("1",1);
    var getX=function(){return x$371;};
    $$nameTest.getX=getX;
    
    //ObjectDefinition nameTest at nesting.ceylon (182:4-194:4)
    function nameTest$nameTest(){
        var $$nameTest$nameTest=new nameTest$nameTest.$$;
        
        //AttributeDeclaration x at nesting.ceylon (183:8-183:29)
        var x$372=$$$cl1.String("2",1);
        var getX=function(){return x$372;};
        $$nameTest$nameTest.getX=getX;
        
        //MethodDefinition f at nesting.ceylon (184:8-193:8)
        function f(){
            
            //ObjectDefinition nameTest at nesting.ceylon (185:12-191:12)
            function nameTest$373(){
                var $$nameTest$373=new nameTest$373.$$;
                
                //AttributeDeclaration x at nesting.ceylon (186:16-186:37)
                var x$374=$$$cl1.String("3",1);
                var getX=function(){return x$374;};
                $$nameTest$373.getX=getX;
                
                //ObjectDefinition nameTest at nesting.ceylon (187:16-189:16)
                function nameTest$nameTest(){
                    var $$nameTest$nameTest=new nameTest$nameTest.$$;
                    
                    //AttributeDeclaration x at nesting.ceylon (188:20-188:41)
                    var x$375=$$$cl1.String("4",1);
                    var getX=function(){return x$375;};
                    $$nameTest$nameTest.getX=getX;
                    return $$nameTest$nameTest;
                }
                function $init$nameTest$nameTest(){
                    if (nameTest$nameTest.$$===undefined){
                        $$$cl1.initTypeProto(nameTest$nameTest,'nesting.nameTest.nameTest.f.nameTest.nameTest',$$$cl1.IdentifiableObject);
                    }
                    return nameTest$nameTest;
                }
                $$nameTest$373.$init$nameTest$nameTest=$init$nameTest$nameTest;
                $init$nameTest$nameTest();
                var nameTest$376=nameTest$nameTest(new nameTest$nameTest.$$);
                var getNameTest=function(){
                    return nameTest$376;
                }
                $$nameTest$373.getNameTest=getNameTest;
                
                //MethodDefinition f at nesting.ceylon (190:16-190:64)
                function f(){
                    return $$nameTest$373.getX().plus($$nameTest$373.getNameTest().getX());
                }
                $$nameTest$373.f=f;
                return $$nameTest$373;
            }
            function $init$nameTest$373(){
                if (nameTest$373.$$===undefined){
                    $$$cl1.initTypeProto(nameTest$373,'nesting.nameTest.nameTest.f.nameTest',$$$cl1.IdentifiableObject);
                }
                return nameTest$373;
            }
            $init$nameTest$373();
            var nameTest$377=nameTest$373(new nameTest$373.$$);
            var getNameTest$377=function(){
                return nameTest$377;
            }
            return $$nameTest.getX().plus($$nameTest$nameTest.getX()).plus(getNameTest$377().f());
        }
        $$nameTest$nameTest.f=f;
        return $$nameTest$nameTest;
    }
    function $init$nameTest$nameTest(){
        if (nameTest$nameTest.$$===undefined){
            $$$cl1.initTypeProto(nameTest$nameTest,'nesting.nameTest.nameTest',$$$cl1.IdentifiableObject);
        }
        return nameTest$nameTest;
    }
    $$nameTest.$init$nameTest$nameTest=$init$nameTest$nameTest;
    $init$nameTest$nameTest();
    var nameTest$378=nameTest$nameTest(new nameTest$nameTest.$$);
    var getNameTest=function(){
        return nameTest$378;
    }
    $$nameTest.getNameTest=getNameTest;
    
    //MethodDefinition f at nesting.ceylon (195:4-195:50)
    function f(){
        return $$nameTest.getNameTest().f();
    }
    $$nameTest.f=f;
    return $$nameTest;
}
function $init$nameTest(){
    if (nameTest.$$===undefined){
        $$$cl1.initTypeProto(nameTest,'nesting.nameTest',$$$cl1.IdentifiableObject);
    }
    return nameTest;
}
exports.$init$nameTest=$init$nameTest;
$init$nameTest();
var nameTest$379=nameTest(new nameTest.$$);
var getNameTest=function(){
    return nameTest$379;
}
exports.getNameTest=getNameTest;

//ClassDefinition C1 at nesting.ceylon (198:0-209:0)
function C1($$c1){
    $init$C1();
    if ($$c1===undefined)$$c1=new C1.$$;
    
    //AttributeDeclaration x at nesting.ceylon (199:4-199:33)
    var x$380=$$$cl1.String("1",1);
    var getX=function(){return x$380;};
    $$c1.getX=getX;
    
    //ClassDefinition C1 at nesting.ceylon (200:4-202:4)
    function C1$C1($$c1$C1){
        $init$C1$C1();
        if ($$c1$C1===undefined)$$c1$C1=new C1$C1.$$;
        
        //AttributeDeclaration x at nesting.ceylon (201:8-201:38)
        var x$381=$$$cl1.String("11",2);
        var getX=function(){return x$381;};
        $$c1$C1.getX=getX;
        return $$c1$C1;
    }
    $$c1.C1$C1=C1$C1;
    function $init$C1$C1(){
        if (C1$C1.$$===undefined){
            $$$cl1.initTypeProto(C1$C1,'nesting.C1.C1',$$$cl1.IdentifiableObject);
        }
        return C1$C1;
    }
    $$c1.$init$C1$C1=$init$C1$C1;
    $init$C1$C1();
    
    //ClassDefinition C3 at nesting.ceylon (203:4-208:4)
    function C3$C1($$c3$C1){
        $init$C3$C1();
        if ($$c3$C1===undefined)$$c3$C1=new C3$C1.$$;
        $$c1.C1$C1($$c3$C1);
        $$c3$C1.getX$$nesting$C1$C1$=$$c3$C1.getX;
        
        //AttributeDeclaration x at nesting.ceylon (204:8-204:45)
        var x$382=$$$cl1.String("13",2);
        var getX=function(){return x$382;};
        $$c3$C1.getX=getX;
        
        //MethodDefinition f at nesting.ceylon (205:8-207:8)
        function f(){
            return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),$$c1.getX().getString(),$$$cl1.String("-",1),$$c3$C1.getX$$nesting$C1$C1$().getString(),$$$cl1.String("-",1),$$c1.C1$C1().getX().getString(),$$$cl1.String("-",1),$$c3$C1.getX().getString(),$$$cl1.String("-",1),$$c1.C3$C1().getX().getString(),$$$cl1.String("",0)])).getString();
        }
        $$c3$C1.f=f;
        return $$c3$C1;
    }
    $$c1.C3$C1=C3$C1;
    function $init$C3$C1(){
        if (C3$C1.$$===undefined){
            $$$cl1.initType(C3$C1,'nesting.C1.C3',$$c1.C1$C1);
        }
        return C3$C1;
    }
    $$c1.$init$C3$C1=$init$C3$C1;
    $init$C3$C1();
    return $$c1;
}
exports.C1=C1;
function $init$C1(){
    if (C1.$$===undefined){
        $$$cl1.initTypeProto(C1,'nesting.C1',$$$cl1.IdentifiableObject);
    }
    return C1;
}
exports.$init$C1=$init$C1;
$init$C1();

//ClassDefinition C2 at nesting.ceylon (210:0-221:0)
function C2($$c2){
    $init$C2();
    if ($$c2===undefined)$$c2=new C2.$$;
    C1($$c2);
    
    //AttributeDeclaration x at nesting.ceylon (211:4-211:32)
    var x$383=$$$cl1.String("2",1);
    var getX=function(){return x$383;};
    $$c2.getX=getX;
    
    //ClassDefinition C2 at nesting.ceylon (212:4-220:4)
    function C2$C2($$c2$C2){
        $init$C2$C2();
        if ($$c2$C2===undefined)$$c2$C2=new C2$C2.$$;
        $$c2.C1$C1($$c2$C2);
        $$c2$C2.getX$$nesting$C1$C1$=$$c2$C2.getX;
        
        //AttributeDeclaration x at nesting.ceylon (213:8-213:37)
        var x$384=$$$cl1.String("22",2);
        var getX=function(){return x$384;};
        $$c2$C2.getX=getX;
        
        //ClassDefinition C2 at nesting.ceylon (214:8-216:8)
        function C2$C2$C2($$c2$C2$C2){
            $init$C2$C2$C2();
            if ($$c2$C2$C2===undefined)$$c2$C2$C2=new C2$C2$C2.$$;
            $$c2.C3$C1($$c2$C2$C2);
            
            //AttributeDeclaration x at nesting.ceylon (215:12-215:42)
            var x$385=$$$cl1.String("222",3);
            var getX=function(){return x$385;};
            $$c2$C2$C2.getX=getX;
            return $$c2$C2$C2;
        }
        $$c2$C2.C2$C2$C2=C2$C2$C2;
        function $init$C2$C2$C2(){
            if (C2$C2$C2.$$===undefined){
                $$$cl1.initType(C2$C2$C2,'nesting.C2.C2.C2',$$c2.C3$C1);
            }
            return C2$C2$C2;
        }
        $$c2$C2.$init$C2$C2$C2=$init$C2$C2$C2;
        $init$C2$C2$C2();
        
        //MethodDefinition f at nesting.ceylon (217:8-219:8)
        function f(){
            return $$$cl1.StringBuilder().appendAll($$$cl1.ArraySequence([$$$cl1.String("",0),$$c2.getX().getString(),$$$cl1.String("-",1),$$c2.C1$C1().getX().getString(),$$$cl1.String("-",1),$$c2$C2.getX().getString(),$$$cl1.String("-",1),$$c2$C2.getX$$nesting$C1$C1$().getString(),$$$cl1.String("-",1),$$c2.C3$C1().getX().getString(),$$$cl1.String("-",1),$$c2$C2.C2$C2$C2().getX().getString(),$$$cl1.String("-",1),$$c2$C2.C2$C2$C2().f().getString(),$$$cl1.String("-",1),$$c2.C3$C1().f().getString(),$$$cl1.String("",0)])).getString();
        }
        $$c2$C2.f=f;
        return $$c2$C2;
    }
    $$c2.C2$C2=C2$C2;
    function $init$C2$C2(){
        if (C2$C2.$$===undefined){
            $$$cl1.initType(C2$C2,'nesting.C2.C2',$$c2.C1$C1);
        }
        return C2$C2;
    }
    $$c2.$init$C2$C2=$init$C2$C2;
    $init$C2$C2();
    return $$c2;
}
exports.C2=C2;
function $init$C2(){
    if (C2.$$===undefined){
        $$$cl1.initType(C2,'nesting.C2',C1);
    }
    return C2;
}
exports.$init$C2=$init$C2;
$init$C2();

//MethodDefinition test at nesting.ceylon (223:0-251:0)
function test(){
    outr($$$cl1.String("Hello",5));
    $$$a12.assert(Holder($$$cl1.String("ok",2)).get().getString().equals($$$cl1.String("ok",2)),$$$cl1.String("holder(ok)",10));
    $$$a12.assert(Holder($$$cl1.String("ok",2)).getString().equals($$$cl1.String("ok",2)),$$$cl1.String("holder.string",13));
    $$$a12.assert(Wrapper().get().getString().equals($$$cl1.String("100",3)),$$$cl1.String("wrapper 1",9));
    $$$a12.assert(Wrapper().getString().equals($$$cl1.String("100",3)),$$$cl1.String("wrapper 2",9));
    $$$a12.assert(Unwrapper().get().getString().equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 1",11));
    $$$a12.assert(Unwrapper().getO().getString().equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 2",11));
    $$$a12.assert(Unwrapper().getString().equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 3",11));
    $$$a12.assert($$$cl1.isOfType(producer(),'ceylon.language.Callable')/* REIFIED GENERICS SOON!!! term Type[Object()] model {TypeParameter[Callable#Return]=Type[Object]}<ceylon.language.Object> */,$$$cl1.String("function 1",10));
    $$$a12.assert($$$cl1.isOfType(producer()(),'ceylon.language.Integer'),$$$cl1.String("function 2",10));
    $$$a12.assert($$$cl1.Integer(123).equals(producer()()),$$$cl1.String("function 3",10));
    $$$a12.assert($$$cl1.String("something",9).equals(returner($$$cl1.String("something",9))()),$$$cl1.String("function 4",10));
    $$$a12.assert(A().B$A().C$B$A().foobar().equals($$$cl1.String("foo",3)),$$$cl1.String("foobar",6));
    $$$a12.assert(A().B$A().C$B$A().quxx().equals($$$cl1.String("qux",3)),$$$cl1.String("quxx",4));
    $$$a12.assert(A().baz().equals($$$cl1.String("foo",3)),$$$cl1.String("baz",3));
    $$$a12.assert(O().test1().equals($$$cl1.String("hello",5)),$$$cl1.String("method instantiating inner class",32));
    $$$a12.assert(O().test2().equals($$$cl1.String("hello",5)),$$$cl1.String("method accessing inner object",29));
    $$$a12.assert(O().test3().equals($$$cl1.String("hello",5)),$$$cl1.String("method deriving inner interface",31));
    $$$a12.assert(OuterC1().tst().equals($$$cl1.String("OuterC1.A.tst()",15)),$$$cl1.String("",0));
    $$$a12.assert(outerf().equals($$$cl1.String("outerf.A.tst()",14)),$$$cl1.String("",0));
    $$$a12.assert(OuterC2().tst().equals($$$cl1.String("OuterC2.A.tst()",15)),$$$cl1.String("",0));
    Outer($$$cl1.String("Hello",5));
    $$$a12.assert(NameTest().f().equals($$$cl1.String("1234",4)),$$$cl1.String("Nested class with same name",27));
    $$$a12.assert(getNameTest().f().equals($$$cl1.String("1234",4)),$$$cl1.String("Nested object with same name",28));
    $$$a12.assert(C1().C3$C1().f().equals($$$cl1.String("1-11-11-13-13",13)),$$$cl1.String("Several nested classes with same name (1)",41));
    $$$a12.assert(C2().C2$C2().f().equals($$$cl1.String("2-11-22-11-13-222-2-11-11-222-13-2-11-11-13-13",46)),$$$cl1.String("Several nested classes with same name (2)",41));
    $$$a12.results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
