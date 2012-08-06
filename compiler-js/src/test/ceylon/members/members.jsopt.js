(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.4/ceylon.language');
var $$$a12=require('default/assert');

//ClassDefinition Counter at members.ceylon (3:0-17:0)
function Counter(initCount$264$, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$264$===undefined){initCount$264$=$$$cl1.Integer(0);}
    $$counter.initCount$264$=initCount$264$;
    
    //AttributeDeclaration currentCount at members.ceylon (4:4-4:42)
    $$counter.currentCount$265$=$$counter.initCount$264$;
    return $$counter;
}
exports.Counter=Counter;
function $init$Counter(){
    if (Counter.$$===undefined){
        $$$cl1.initTypeProto(Counter,'members.Counter',$$$cl1.IdentifiableObject);
        (function($$counter){
            
            //AttributeDeclaration currentCount at members.ceylon (4:4-4:42)
            $$counter.getCurrentCount$265$=function getCurrentCount$265$(){
                return this.currentCount$265$;
            }
            $$counter.setCurrentCount$265$=function setCurrentCount$265$(currentCount$266){
                this.currentCount$265$=currentCount$266; return currentCount$266;
            }
            
            //AttributeGetterDefinition count at members.ceylon (5:4-7:4)
            $$counter.getCount=function getCount(){
                var $$counter=this;
                return $$counter.getCurrentCount$265$();
            }
            
            //MethodDefinition inc at members.ceylon (8:4-10:4)
            $$counter.inc=function inc(){
                var $$counter=this;
                $$counter.setCurrentCount$265$($$counter.getCurrentCount$265$().plus($$$cl1.Integer(1)));
            }
            
            //AttributeGetterDefinition initialCount at members.ceylon (11:4-13:4)
            $$counter.getInitialCount=function getInitialCount(){
                var $$counter=this;
                return $$counter.initCount$264$;
            }
            
            //AttributeGetterDefinition string at members.ceylon (14:4-16:4)
            $$counter.getString=function getString(){
                var $$counter=this;
                return $$$cl1.String("Counter[",8).plus($$counter.getCount().getString()).plus($$$cl1.String("]",1));
            }
            
        })(Counter.$$.prototype);
        
    }
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDefinition Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$267$, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$267$=arg1$267$;
    
    //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
    $$issue10C1.i1$268$=$$$cl1.Integer(3);
    
    //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
    $$issue10C1.i2$269$=$$$cl1.Integer(5);
    
    //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
    $$issue10C1.i3$270$=$$$cl1.Integer(7);
    return $$issue10C1;
}
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl1.initTypeProto(Issue10C1,'members.Issue10C1',$$$cl1.IdentifiableObject);
        (function($$issue10C1){
            
            //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
            $$issue10C1.getI1$268$=function getI1$268$(){
                return this.i1$268$;
            }
            
            //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
            $$issue10C1.getI2$269$=function getI2$269$(){
                return this.i2$269$;
            }
            
            //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
            $$issue10C1.getI3=function getI3(){
                return this.i3$270$;
            }
            
            //MethodDefinition f1 at members.ceylon (23:4-23:39)
            $$issue10C1.f1=function f1(){
                var $$issue10C1=this;
                return $$issue10C1.arg1$267$;
            }
            
            //MethodDefinition f2 at members.ceylon (24:4-24:37)
            $$issue10C1.f2=function f2(){
                var $$issue10C1=this;
                return $$issue10C1.getI1$268$();
            }
            
            //MethodDefinition f3 at members.ceylon (25:4-25:37)
            $$issue10C1.f3=function f3(){
                var $$issue10C1=this;
                return $$issue10C1.getI2$269$();
            }
            
            //MethodDefinition f4 at members.ceylon (26:4-26:37)
            $$issue10C1.f4=function f4(){
                var $$issue10C1=this;
                return $$issue10C1.getI3();
            }
            
            //MethodDefinition f5 at members.ceylon (27:4-27:29)
            $$issue10C1.f5$271$=function f5$271$(){
                var $$issue10C1=this;
                return $$$cl1.Integer(9);
            }
            
            //MethodDefinition f6 at members.ceylon (28:4-28:39)
            $$issue10C1.f6=function f6(){
                var $$issue10C1=this;
                return $$issue10C1.f5$271$();
            }
            
            //MethodDefinition f7 at members.ceylon (29:4-29:30)
            $$issue10C1.f7$272$=function f7$272$(){
                var $$issue10C1=this;
                return $$$cl1.Integer(11);
            }
            
            //MethodDefinition f8 at members.ceylon (30:4-30:39)
            $$issue10C1.f8=function f8(){
                var $$issue10C1=this;
                return $$issue10C1.f7$272$();
            }
            
            //MethodDefinition f9 at members.ceylon (31:4-31:45)
            $$issue10C1.f9=function f9(){
                var $$issue10C1=this;
                return $$$cl1.Integer(13);
            }
            
            //MethodDefinition f10 at members.ceylon (32:4-32:40)
            $$issue10C1.f10=function f10(){
                var $$issue10C1=this;
                return $$issue10C1.f9();
            }
            
        })(Issue10C1.$$.prototype);
        
    }
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDefinition Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$273$, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$273$=arg1$273$;
    Issue10C1($$$cl1.Integer(1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
    $$issue10C2.i1$274$=$$$cl1.Integer(4);
    
    //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
    $$issue10C2.i2$275$=$$$cl1.Integer(6);
    
    //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
    $$issue10C2.i3$276$=$$$cl1.Integer(8);
    return $$issue10C2;
}
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl1.initTypeProto(Issue10C2,'members.Issue10C2',Issue10C1);
        (function($$issue10C2){
            
            //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
            $$issue10C2.getI1$274$=function getI1$274$(){
                return this.i1$274$;
            }
            
            //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
            $$issue10C2.getI2=function getI2(){
                return this.i2$275$;
            }
            
            //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
            $$issue10C2.getI3=function getI3(){
                return this.i3$276$;
            }
            
            //MethodDefinition f11 at members.ceylon (38:4-38:40)
            $$issue10C2.f11=function f11(){
                var $$issue10C2=this;
                return $$issue10C2.arg1$273$;
            }
            
            //MethodDefinition f12 at members.ceylon (39:4-39:38)
            $$issue10C2.f12=function f12(){
                var $$issue10C2=this;
                return $$issue10C2.getI1$274$();
            }
            
            //MethodDefinition f5 at members.ceylon (40:4-40:30)
            $$issue10C2.f5$277$=function f5$277$(){
                var $$issue10C2=this;
                return $$$cl1.Integer(10);
            }
            
            //MethodDefinition f13 at members.ceylon (41:4-41:40)
            $$issue10C2.f13=function f13(){
                var $$issue10C2=this;
                return $$issue10C2.f5$277$();
            }
            
            //MethodDefinition f7 at members.ceylon (42:4-42:37)
            $$issue10C2.f7=function f7(){
                var $$issue10C2=this;
                return $$$cl1.Integer(12);
            }
            
            //MethodDefinition f9 at members.ceylon (43:4-43:44)
            $$issue10C2.f9=function f9(){
                var $$issue10C2=this;
                return $$$cl1.Integer(14);
            }
            
        })(Issue10C2.$$.prototype);
        
    }
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDefinition testIssue10 at members.ceylon (46:0-62:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (47:4-47:28)
    var obj$278=Issue10C2($$$cl1.Integer(2));
    $$$a12.assert(obj$278.f1().equals($$$cl1.Integer(1)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$a12.assert(obj$278.f11().equals($$$cl1.Integer(2)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$a12.assert(obj$278.f2().equals($$$cl1.Integer(3)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$a12.assert(obj$278.f12().equals($$$cl1.Integer(4)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$a12.assert(obj$278.f3().equals($$$cl1.Integer(5)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$a12.assert(obj$278.getI2().equals($$$cl1.Integer(6)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$a12.assert(obj$278.f4().equals($$$cl1.Integer(8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$a12.assert(obj$278.getI3().equals($$$cl1.Integer(8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$a12.assert(obj$278.f6().equals($$$cl1.Integer(9)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$a12.assert(obj$278.f13().equals($$$cl1.Integer(10)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$a12.assert(obj$278.f8().equals($$$cl1.Integer(11)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$a12.assert(obj$278.f7().equals($$$cl1.Integer(12)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$a12.assert(obj$278.f10().equals($$$cl1.Integer(14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$a12.assert(obj$278.f9().equals($$$cl1.Integer(14)),$$$cl1.String("Issue #10 (shared method)",25));
}

//ClassDefinition AssignTest at members.ceylon (64:0-68:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (65:4-65:34)
    $$assignTest.x$279$=$$$cl1.Integer(1);
    return $$assignTest;
}
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl1.initTypeProto(AssignTest,'members.AssignTest',$$$cl1.IdentifiableObject);
        (function($$assignTest){
            
            //AttributeDeclaration x at members.ceylon (65:4-65:34)
            $$assignTest.getX=function getX(){
                return this.x$279$;
            }
            $$assignTest.setX=function setX(x$280){
                this.x$279$=x$280; return x$280;
            }
            
            //AttributeGetterDefinition y at members.ceylon (66:4-66:33)
            $$assignTest.getY=function getY(){
                var $$assignTest=this;
                return $$assignTest.getX();
            }
            
            //AttributeSetterDefinition y at members.ceylon (67:4-67:23)
            $$assignTest.setY=function setY(y$281){
                var $$assignTest=this;
                $$assignTest.setX(y$281);
            }
            
        })(AssignTest.$$.prototype);
        
    }
    return AssignTest;
}
exports.$init$AssignTest=$init$AssignTest;
$init$AssignTest();

//ClassDefinition Issue50 at members.ceylon (70:0-73:0)
function Issue50($$issue50){
    $init$Issue50();
    if ($$issue50===undefined)$$issue50=new Issue50.$$;
    
    //AttributeDeclaration z at members.ceylon (71:4-71:19)
    $$issue50.z$282$=$$$cl1.String("ok",2)
    return $$issue50;
}
function $init$Issue50(){
    if (Issue50.$$===undefined){
        $$$cl1.initTypeProto(Issue50,'members.Issue50',$$$cl1.IdentifiableObject);
        (function($$issue50){
            
            //AttributeDeclaration z at members.ceylon (71:4-71:19)
            $$issue50.getZ=function getZ(){
                return this.z$282$;
            }
            
        })(Issue50.$$.prototype);
        
    }
    return Issue50;
}
exports.$init$Issue50=$init$Issue50;
$init$Issue50();

//ClassDefinition Util at members.ceylon (75:0-77:0)
function Util($$util){
    $init$Util();
    if ($$util===undefined)$$util=new Util.$$;
    
    //AttributeDeclaration s at members.ceylon (76:4-76:27)
    $$util.s$283$=$$$cl1.String("123",3);
    return $$util;
}
function $init$Util(){
    if (Util.$$===undefined){
        $$$cl1.initTypeProto(Util,'members.Util',$$$cl1.IdentifiableObject);
        (function($$util){
            
            //AttributeDeclaration s at members.ceylon (76:4-76:27)
            $$util.getS=function getS(){
                return this.s$283$;
            }
            
        })(Util.$$.prototype);
        
    }
    return Util;
}
exports.$init$Util=$init$Util;
$init$Util();

//ClassDefinition AliasMemberTest at members.ceylon (78:0-91:0)
function AliasMemberTest($$aliasMemberTest){
    $init$AliasMemberTest();
    if ($$aliasMemberTest===undefined)$$aliasMemberTest=new AliasMemberTest.$$;
    
    //MethodDeclaration f1 at members.ceylon (89:4-89:27)
    var f1=Util;
    $$aliasMemberTest.f1=f1;
    
    //MethodDeclaration f2 at members.ceylon (90:4-90:26)
    var f2=$$aliasMemberTest.AliasA$AliasMemberTest;
    $$aliasMemberTest.f2=f2;
    return $$aliasMemberTest;
}
function $init$AliasMemberTest(){
    if (AliasMemberTest.$$===undefined){
        $$$cl1.initTypeProto(AliasMemberTest,'members.AliasMemberTest',$$$cl1.IdentifiableObject);
        (function($$aliasMemberTest){
            
            //InterfaceDefinition I1 at members.ceylon (79:4-79:55)
            function I1$AliasMemberTest($$i1$AliasMemberTest){
                $$i1$AliasMemberTest.$$aliasMemberTest=this;
                
            }
            function $init$I1$AliasMemberTest(){
                if (I1$AliasMemberTest.$$===undefined){
                    $$$cl1.initTypeProtoI(I1$AliasMemberTest,'members.AliasMemberTest.I1');
                    (function($$i1$AliasMemberTest){
                        
                        //AttributeGetterDefinition s at members.ceylon (79:25-79:53)
                        $$i1$AliasMemberTest.getS=function getS(){
                            var $$i1$AliasMemberTest=this;
                            return $$$cl1.String("A",1);
                        }
                        
                    })(I1$AliasMemberTest.$$.prototype);
                    
                }
                return I1$AliasMemberTest;
            }
            $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
            $init$I1$AliasMemberTest();
            $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
            
            //InterfaceDeclaration I1Alias at members.ceylon (80:4-80:33)
            $$aliasMemberTest.I1Alias$AliasMemberTest=$$aliasMemberTest.I1$AliasMemberTest;
            
            //InterfaceDefinition I2 at members.ceylon (81:4-81:48)
            function I2$284$($$i2$284){
                $$i2$284.$$aliasMemberTest=this;
                
            }
            function $init$I2$284$(){
                if (I2$284$.$$===undefined){
                    $$$cl1.initTypeProtoI(I2$284$,'members.AliasMemberTest.I2');
                    (function($$i2$284){
                        
                        //AttributeGetterDefinition s at members.ceylon (81:18-81:46)
                        $$i2$284.getS=function getS(){
                            var $$i2$284=this;
                            return $$$cl1.String("B",1);
                        }
                        
                    })(I2$284$.$$.prototype);
                    
                }
                return I2$284$;
            }
            $$aliasMemberTest.$init$I2$284$=$init$I2$284$;
            $init$I2$284$();
            $$aliasMemberTest.I2$284$=I2$284$;
            
            //InterfaceDeclaration I2Alias at members.ceylon (82:4-82:26)
            $$aliasMemberTest.I2Alias$285$=$$aliasMemberTest.I2$284$;
            
            //ClassDefinition A at members.ceylon (83:4-83:40)
            function A$AliasMemberTest($$a$AliasMemberTest){
                $init$A$AliasMemberTest();
                if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new this.A$AliasMemberTest.$$;
                $$a$AliasMemberTest.$$aliasMemberTest=this;
                $$a$AliasMemberTest.$$aliasMemberTest.I1Alias$AliasMemberTest($$a$AliasMemberTest);
                return $$a$AliasMemberTest;
            }
            function $init$A$AliasMemberTest(){
                if (A$AliasMemberTest.$$===undefined){
                    $$$cl1.initTypeProto(A$AliasMemberTest,'members.AliasMemberTest.A',$$$cl1.IdentifiableObject,$$aliasMemberTest.$init$I1$AliasMemberTest());
                    
                }
                return A$AliasMemberTest;
            }
            $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
            $init$A$AliasMemberTest();
            $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
            
            //ClassDefinition B at members.ceylon (84:4-84:33)
            function B$286$($$b$286){
                $init$B$286$();
                if ($$b$286===undefined)$$b$286=new this.B$286$.$$;
                $$b$286.$$aliasMemberTest=this;
                $$b$286.$$aliasMemberTest.I2Alias$285$($$b$286);
                return $$b$286;
            }
            function $init$B$286$(){
                if (B$286$.$$===undefined){
                    $$$cl1.initTypeProto(B$286$,'members.AliasMemberTest.B',$$$cl1.IdentifiableObject,$$aliasMemberTest.$init$I2$284$());
                    
                }
                return B$286$;
            }
            $$aliasMemberTest.$init$B$286$=$init$B$286$;
            $init$B$286$();
            $$aliasMemberTest.B$286$=B$286$;
            
            //ClassDeclaration AliasA at members.ceylon (85:4-85:29)
            $$aliasMemberTest.AliasA$AliasMemberTest=$$aliasMemberTest.A$AliasMemberTest;
            
            //ClassDeclaration AliasB at members.ceylon (86:4-86:22)
            $$aliasMemberTest.AliasB$287$=$$aliasMemberTest.B$286$;
            
            //MethodDefinition b at members.ceylon (87:4-87:43)
            $$aliasMemberTest.b=function b(){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasB$287$().getS();
            }
            
        })(AliasMemberTest.$$.prototype);
        
    }
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDefinition test at members.ceylon (93:0-115:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (94:4-94:24)
    var c$288=Counter($$$cl1.Integer(0));
    $$$a12.assert(c$288.getCount().equals($$$cl1.Integer(0)),$$$cl1.String("counter 1",9));
    c$288.inc();
    c$288.inc();
    $$$a12.assert(c$288.getCount().equals($$$cl1.Integer(2)),$$$cl1.String("counter 2",9));
    $$$a12.assert(c$288.getString().equals($$$cl1.String("Counter[2]",10)),$$$cl1.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (102:4-102:27)
    var at$289=AssignTest();
    at$289.setX($$$cl1.Integer(5));
    $$$a12.assert(at$289.getX().equals($$$cl1.Integer(5)),$$$cl1.String("assign to member",16));
    at$289.setY($$$cl1.Integer(2));
    $$$a12.assert(at$289.getY().equals($$$cl1.Integer(2)),$$$cl1.String("assign using setter",19));
    $$$a12.assert(Issue50().getZ().equals($$$cl1.String("ok",2)),$$$cl1.String("Issue #50",9));
    test_outer_inner_safety();
    $$$a12.assert(AliasMemberTest().AliasA$AliasMemberTest().getS().equals($$$cl1.String("A",1)),$$$cl1.String("shared inner alias class",24));
    $$$a12.assert(AliasMemberTest().b().equals($$$cl1.String("B",1)),$$$cl1.String("non-shared inner alias class",28));
    $$$a12.assert(AliasMemberTest().f1().getS().equals($$$cl1.String("123",3)),$$$cl1.String("alias method member 1",21));
    $$$a12.assert(AliasMemberTest().f2().getS().equals($$$cl1.String("A",1)),$$$cl1.String("alias method member 2",21));
    $$$a12.results();
}
exports.test=test;
var $$$cl1=require('ceylon/language/0.4/ceylon.language');
var $$$a12=require('default/assert');

//MethodDefinition test_outer_inner_safety at outer.ceylon (3:0-18:0)
function test_outer_inner_safety(){
    
    //ClassDefinition Outer at outer.ceylon (4:2-6:2)
    function Outer$290($$outer$290){
        $init$Outer$290();
        if ($$outer$290===undefined)$$outer$290=new Outer$290.$$;
        return $$outer$290;
    }
    function $init$Outer$290(){
        if (Outer$290.$$===undefined){
            $$$cl1.initTypeProto(Outer$290,'members.test_outer_inner_safety.Outer',$$$cl1.IdentifiableObject);
            (function($$outer$290){
                
                //ClassDefinition Inner at outer.ceylon (5:4-5:27)
                function Inner$Outer($$inner$Outer){
                    $init$Inner$Outer();
                    if ($$inner$Outer===undefined)$$inner$Outer=new this.Inner$Outer.$$;
                    $$inner$Outer.$$outer$290=this;
                    return $$inner$Outer;
                }
                function $init$Inner$Outer(){
                    if (Inner$Outer.$$===undefined){
                        $$$cl1.initTypeProto(Inner$Outer,'members.test_outer_inner_safety.Outer.Inner',$$$cl1.IdentifiableObject);
                        
                    }
                    return Inner$Outer;
                }
                $$outer$290.$init$Inner$Outer=$init$Inner$Outer;
                $init$Inner$Outer();
                $$outer$290.Inner$Outer=Inner$Outer;
                
            })(Outer$290.$$.prototype);
            
        }
        return Outer$290;
    }
    $init$Outer$290();
    
    //AttributeDeclaration o at outer.ceylon (7:2-7:17)
    var o$291=$$$cl1.getNull();
    
    //AttributeDeclaration i1 at outer.ceylon (8:2-8:30)
    var i1$292=(function(){var $=o$291;return $$$cl1.JsCallable($, $==null?null:$.Inner$Outer)})()();
    
    //MethodDeclaration cons at outer.ceylon (9:2-9:32)
    var cons$293=(function(){var $=o$291;return $$$cl1.JsCallable($, $==null?null:$.Inner$Outer)})();
    var i1$294;
    if((i1$294=i1$292)!==null){
        $$$a12.fail($$$cl1.String("i1 should be null",17));
    }
    
    $$$a12.assert($$$cl1.className(cons$293).equals($$$cl1.String("ceylon.language.JsCallable",26)),$$$cl1.String("cons is Callable",16));
    
    //AttributeDeclaration i2 at outer.ceylon (14:2-14:33)
    var i2$295=cons$293();
    var i2$296;
    if((i2$296=i2$295)!==null){
        $$$a12.fail($$$cl1.String("i2 should not exist",19));
    }
    
}
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
