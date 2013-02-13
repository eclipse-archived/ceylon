(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"members","$mod-version":"0.1","members":{"test_outer_inner_safety":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_outer_inner_safety"},"Issue50":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"z":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"z"}},"$nm":"Issue50"},"$pkg-shared":"1","Util":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"Util"},"Issue10C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f6":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f6"},"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f7"},"f8":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f8"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"f9"},"f10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f10"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f4"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C1"},"Issue10C2":{"super":{"$pk":"members","$nm":"Issue10C1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f7"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"f9"},"f12":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f12"},"f11":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f11"},"f13":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f13"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Counter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"initCount"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"currentCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"currentCount"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"initialCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"initialCount"}},"$nm":"Counter"},"AliasMemberTest":{"$i":{"I2Alias":{"$mt":"ifc","$alias":{"$pk":"members","$nm":"I2"},"$nm":"I2Alias"},"I1Alias":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"members","$nm":"I1"},"$nm":"I1Alias"},"I1":{"$mt":"ifc","$an":{"shared":[]},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I1"},"I2":{"$mt":"ifc","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I2"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"b"},"f1":{"$t":{"$pk":"members","$nm":"Util"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f2":{"$t":{"$pk":"members","$nm":"A"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$c":{"AliasB":{"super":{"$pk":"members","$nm":"B"},"$mt":"cls","$alias":"1","$nm":"AliasB"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I1Alias"}],"$mt":"cls","$an":{"shared":[]},"$nm":"A"},"AliasA":{"super":{"$pk":"members","$nm":"A"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"AliasA"},"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I2Alias"}],"$mt":"cls","$nm":"B"}},"$nm":"AliasMemberTest"},"AssignTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"AssignTest"},"testIssue10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIssue10"}}};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2244=require('check/0.1/check-0.1');

//ClassDefinition Counter at members.ceylon (3:0-17:0)
function Counter(initCount$2873, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$2873===undefined){initCount$2873=(0);}
    $$counter.initCount$2873=initCount$2873;
    
    //AttributeDeclaration currentCount at members.ceylon (4:4-4:41)
    $$counter.currentCount$2874=$$counter.initCount$2873;
    return $$counter;
}
exports.Counter=Counter;
function $init$Counter(){
    if (Counter.$$===undefined){
        $$$cl2243.initTypeProto(Counter,'members::Counter',$$$cl2243.Basic);
        (function($$counter){
            
            //AttributeDeclaration currentCount at members.ceylon (4:4-4:41)
            $$counter.getCurrentCount$2874=function getCurrentCount$2874(){
                return this.currentCount$2874;
            };
            $$counter.setCurrentCount$2874=function setCurrentCount$2874(currentCount$2875){
                return this.currentCount$2874=currentCount$2875;
            };
            
            //AttributeGetterDefinition count at members.ceylon (5:4-7:4)
            $$counter.getCount=function getCount(){
                var $$counter=this;
                return $$counter.getCurrentCount$2874();
            };
            //MethodDefinition inc at members.ceylon (8:4-10:4)
            $$counter.inc=function inc(){
                var $$counter=this;
                $$counter.setCurrentCount$2874($$counter.getCurrentCount$2874().plus((1)));
            };
            //AttributeGetterDefinition initialCount at members.ceylon (11:4-13:4)
            $$counter.getInitialCount=function getInitialCount(){
                var $$counter=this;
                return $$counter.initCount$2873;
            };
            //AttributeGetterDefinition string at members.ceylon (14:4-16:4)
            $$counter.getString=function getString(){
                var $$counter=this;
                return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("Counter[",8),$$counter.getCount().getString(),$$$cl2243.String("]",1)]).getString();
            };
        })(Counter.$$.prototype);
    }
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDefinition Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$2876, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$2876=arg1$2876;
    
    //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
    $$issue10C1.i1$2877=(3);
    
    //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
    $$issue10C1.i2$2878=(5);
    
    //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
    $$issue10C1.i3$2879=(7);
    return $$issue10C1;
}
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl2243.initTypeProto(Issue10C1,'members::Issue10C1',$$$cl2243.Basic);
        (function($$issue10C1){
            
            //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
            $$issue10C1.getI1$2877=function getI1$2877(){
                return this.i1$2877;
            };
            
            //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
            $$issue10C1.getI2$2878=function getI2$2878(){
                return this.i2$2878;
            };
            
            //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
            $$issue10C1.getI3=function getI3(){
                return this.i3$2879;
            };
            
            //MethodDefinition f1 at members.ceylon (23:4-23:39)
            $$issue10C1.f1=function f1(){
                var $$issue10C1=this;
                return $$issue10C1.arg1$2876;
            };
            //MethodDefinition f2 at members.ceylon (24:4-24:37)
            $$issue10C1.f2=function f2(){
                var $$issue10C1=this;
                return $$issue10C1.getI1$2877();
            };
            //MethodDefinition f3 at members.ceylon (25:4-25:37)
            $$issue10C1.f3=function f3(){
                var $$issue10C1=this;
                return $$issue10C1.getI2$2878();
            };
            //MethodDefinition f4 at members.ceylon (26:4-26:37)
            $$issue10C1.f4=function f4(){
                var $$issue10C1=this;
                return $$issue10C1.getI3();
            };
            //MethodDefinition f5 at members.ceylon (27:4-27:29)
            $$issue10C1.f5$2880=function f5$2880(){
                var $$issue10C1=this;
                return (9);
            };
            //MethodDefinition f6 at members.ceylon (28:4-28:39)
            $$issue10C1.f6=function f6(){
                var $$issue10C1=this;
                return $$issue10C1.f5$2880();
            };
            //MethodDefinition f7 at members.ceylon (29:4-29:30)
            $$issue10C1.f7$2881=function f7$2881(){
                var $$issue10C1=this;
                return (11);
            };
            //MethodDefinition f8 at members.ceylon (30:4-30:39)
            $$issue10C1.f8=function f8(){
                var $$issue10C1=this;
                return $$issue10C1.f7$2881();
            };
            //MethodDefinition f9 at members.ceylon (31:4-31:45)
            $$issue10C1.f9=function f9(){
                var $$issue10C1=this;
                return (13);
            };
            //MethodDefinition f10 at members.ceylon (32:4-32:40)
            $$issue10C1.f10=function f10(){
                var $$issue10C1=this;
                return $$issue10C1.f9();
            };
        })(Issue10C1.$$.prototype);
    }
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDefinition Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$2882, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$2882=arg1$2882;
    Issue10C1((1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
    $$issue10C2.i1$2883=(4);
    
    //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
    $$issue10C2.i2$2884=(6);
    
    //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
    $$issue10C2.i3$2885=(8);
    return $$issue10C2;
}
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl2243.initTypeProto(Issue10C2,'members::Issue10C2',Issue10C1);
        (function($$issue10C2){
            
            //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
            $$issue10C2.getI1$2883=function getI1$2883(){
                return this.i1$2883;
            };
            
            //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
            $$issue10C2.getI2=function getI2(){
                return this.i2$2884;
            };
            
            //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
            $$issue10C2.getI3=function getI3(){
                return this.i3$2885;
            };
            
            //MethodDefinition f11 at members.ceylon (38:4-38:40)
            $$issue10C2.f11=function f11(){
                var $$issue10C2=this;
                return $$issue10C2.arg1$2882;
            };
            //MethodDefinition f12 at members.ceylon (39:4-39:38)
            $$issue10C2.f12=function f12(){
                var $$issue10C2=this;
                return $$issue10C2.getI1$2883();
            };
            //MethodDefinition f5 at members.ceylon (40:4-40:30)
            $$issue10C2.f5$2886=function f5$2886(){
                var $$issue10C2=this;
                return (10);
            };
            //MethodDefinition f13 at members.ceylon (41:4-41:40)
            $$issue10C2.f13=function f13(){
                var $$issue10C2=this;
                return $$issue10C2.f5$2886();
            };
            //MethodDefinition f7 at members.ceylon (42:4-42:37)
            $$issue10C2.f7=function f7(){
                var $$issue10C2=this;
                return (12);
            };
            //MethodDefinition f9 at members.ceylon (43:4-43:44)
            $$issue10C2.f9=function f9(){
                var $$issue10C2=this;
                return (14);
            };
        })(Issue10C2.$$.prototype);
    }
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDefinition testIssue10 at members.ceylon (46:0-63:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (47:4-47:28)
    var obj$2887=Issue10C2((2));
    $$$c2244.check(obj$2887.f1().equals((1)),$$$cl2243.String("Issue #10 (parameter)",21));
    $$$c2244.check(obj$2887.f11().equals((2)),$$$cl2243.String("Issue #10 (parameter)",21));
    $$$c2244.check(obj$2887.f2().equals((3)),$$$cl2243.String("Issue #10 (non-shared attribute)",32));
    $$$c2244.check(obj$2887.f12().equals((4)),$$$cl2243.String("Issue #10 (non-shared attribute)",32));
    $$$c2244.check(obj$2887.f3().equals((5)),$$$cl2243.String("Issue #10 (non-shared attribute)",32));
    $$$c2244.check(obj$2887.getI2().equals((6)),$$$cl2243.String("Issue #10 (shared attribute)",28));
    $$$c2244.check(obj$2887.f4().equals((8)),$$$cl2243.String("Issue #10 (shared attribute)",28));
    $$$c2244.check(obj$2887.getI3().equals((8)),$$$cl2243.String("Issue #10 (shared attribute)",28));
    $$$c2244.check(obj$2887.f6().equals((9)),$$$cl2243.String("Issue #10 (non-shared method)",29));
    $$$c2244.check(obj$2887.f13().equals((10)),$$$cl2243.String("Issue #10 (non-shared method)",29));
    $$$c2244.check(obj$2887.f8().equals((11)),$$$cl2243.String("Issue #10 (non-shared method)",29));
    $$$c2244.check(obj$2887.f7().equals((12)),$$$cl2243.String("Issue #10 (shared method)",25));
    $$$c2244.check(obj$2887.f10().equals((14)),$$$cl2243.String("Issue #10 (shared method)",25));
    $$$c2244.check(obj$2887.f9().equals((14)),$$$cl2243.String("Issue #10 (shared method)",25));
    $$$c2244.check((!obj$2887.getString().getEmpty()),$$$cl2243.String("Issue #113 (inheritance)",24));
};

//ClassDefinition AssignTest at members.ceylon (65:0-69:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (66:4-66:33)
    $$assignTest.x$2888=(1);
    return $$assignTest;
}
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl2243.initTypeProto(AssignTest,'members::AssignTest',$$$cl2243.Basic);
        (function($$assignTest){
            
            //AttributeDeclaration x at members.ceylon (66:4-66:33)
            $$assignTest.getX=function getX(){
                return this.x$2888;
            };
            $$assignTest.setX=function setX(x$2889){
                return this.x$2888=x$2889;
            };
            
            //AttributeGetterDefinition y at members.ceylon (67:4-67:33)
            $$assignTest.getY=function getY(){
                var $$assignTest=this;
                return $$assignTest.getX();
            };
            //AttributeSetterDefinition y at members.ceylon (68:4-68:22)
            $$assignTest.setY=function setY(y$2890){
                var $$assignTest=this;
                $$assignTest.setX(y$2890);
            };
        })(AssignTest.$$.prototype);
    }
    return AssignTest;
}
exports.$init$AssignTest=$init$AssignTest;
$init$AssignTest();

//ClassDefinition Issue50 at members.ceylon (71:0-74:0)
function Issue50($$issue50){
    $init$Issue50();
    if ($$issue50===undefined)$$issue50=new Issue50.$$;
    
    //AttributeDeclaration z at members.ceylon (72:4-72:19)
    var z$2891=$$$cl2243.String("ok",2);
    var getZ=function(){return z$2891;};
    $$issue50.getZ=getZ;
    return $$issue50;
}
function $init$Issue50(){
    if (Issue50.$$===undefined){
        $$$cl2243.initTypeProto(Issue50,'members::Issue50',$$$cl2243.Basic);
        (function($$issue50){
            
            //AttributeDeclaration z at members.ceylon (72:4-72:19)
            $$issue50.getZ=function(){return this.z$2891;};
        })(Issue50.$$.prototype);
    }
    return Issue50;
}
exports.$init$Issue50=$init$Issue50;
$init$Issue50();

//ClassDefinition Util at members.ceylon (76:0-78:0)
function Util($$util){
    $init$Util();
    if ($$util===undefined)$$util=new Util.$$;
    
    //AttributeDeclaration s at members.ceylon (77:4-77:27)
    $$util.s$2892=$$$cl2243.String("123",3);
    return $$util;
}
function $init$Util(){
    if (Util.$$===undefined){
        $$$cl2243.initTypeProto(Util,'members::Util',$$$cl2243.Basic);
        (function($$util){
            
            //AttributeDeclaration s at members.ceylon (77:4-77:27)
            $$util.getS=function getS(){
                return this.s$2892;
            };
        })(Util.$$.prototype);
    }
    return Util;
}
exports.$init$Util=$init$Util;
$init$Util();

//ClassDefinition AliasMemberTest at members.ceylon (79:0-92:0)
function AliasMemberTest($$aliasMemberTest){
    $init$AliasMemberTest();
    if ($$aliasMemberTest===undefined)$$aliasMemberTest=new AliasMemberTest.$$;
    return $$aliasMemberTest;
}
function $init$AliasMemberTest(){
    if (AliasMemberTest.$$===undefined){
        $$$cl2243.initTypeProto(AliasMemberTest,'members::AliasMemberTest',$$$cl2243.Basic);
        (function($$aliasMemberTest){
            
            //InterfaceDefinition I1 at members.ceylon (80:4-80:55)
            function I1$AliasMemberTest($$i1$AliasMemberTest){
                $$i1$AliasMemberTest.$$aliasMemberTest=this;
            }
            function $init$I1$AliasMemberTest(){
                if (I1$AliasMemberTest.$$===undefined){
                    $$$cl2243.initTypeProto(I1$AliasMemberTest,'members::AliasMemberTest.I1');
                    (function($$i1$AliasMemberTest){
                        
                        //AttributeGetterDefinition s at members.ceylon (80:25-80:53)
                        $$i1$AliasMemberTest.getS=function getS(){
                            var $$i1$AliasMemberTest=this;
                            return $$$cl2243.String("A",1);
                        };
                    })(I1$AliasMemberTest.$$.prototype);
                }
                return I1$AliasMemberTest;
            }
            $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
            $init$I1$AliasMemberTest();
            $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
            
            //InterfaceDeclaration I1Alias at members.ceylon (81:4-81:34)
            $$aliasMemberTest.I1Alias$AliasMemberTest=$$aliasMemberTest.I1$AliasMemberTest;
            
            //InterfaceDefinition I2 at members.ceylon (82:4-82:48)
            function I2$2893($$i2$2893){
                $$i2$2893.$$aliasMemberTest=this;
            }
            function $init$I2$2893(){
                if (I2$2893.$$===undefined){
                    $$$cl2243.initTypeProto(I2$2893,'members::AliasMemberTest.I2');
                    (function($$i2$2893){
                        
                        //AttributeGetterDefinition s at members.ceylon (82:18-82:46)
                        $$i2$2893.getS=function getS(){
                            var $$i2$2893=this;
                            return $$$cl2243.String("B",1);
                        };
                    })(I2$2893.$$.prototype);
                }
                return I2$2893;
            }
            $$aliasMemberTest.$init$I2$2893=$init$I2$2893;
            $init$I2$2893();
            $$aliasMemberTest.I2$2893=I2$2893;
            
            //InterfaceDeclaration I2Alias at members.ceylon (83:4-83:27)
            $$aliasMemberTest.I2Alias$2894=$$aliasMemberTest.I2$2893;
            
            //ClassDefinition A at members.ceylon (84:4-84:40)
            function A$AliasMemberTest($$a$AliasMemberTest){
                $init$A$AliasMemberTest();
                if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new this.A$AliasMemberTest.$$;
                $$a$AliasMemberTest.$$aliasMemberTest=this;
                $$a$AliasMemberTest.$$aliasMemberTest.I1$AliasMemberTest($$a$AliasMemberTest);
                return $$a$AliasMemberTest;
            }
            function $init$A$AliasMemberTest(){
                if (A$AliasMemberTest.$$===undefined){
                    $$$cl2243.initTypeProto(A$AliasMemberTest,'members::AliasMemberTest.A',$$$cl2243.Basic,$$aliasMemberTest.$init$I1$AliasMemberTest());
                }
                return A$AliasMemberTest;
            }
            $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
            $init$A$AliasMemberTest();
            $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
            
            //ClassDefinition B at members.ceylon (85:4-85:33)
            function B$2895($$b$2895){
                $init$B$2895();
                if ($$b$2895===undefined)$$b$2895=new this.B$2895.$$;
                $$b$2895.$$aliasMemberTest=this;
                $$b$2895.$$aliasMemberTest.I2$2893($$b$2895);
                return $$b$2895;
            }
            function $init$B$2895(){
                if (B$2895.$$===undefined){
                    $$$cl2243.initTypeProto(B$2895,'members::AliasMemberTest.B',$$$cl2243.Basic,$$aliasMemberTest.$init$I2$2893());
                }
                return B$2895;
            }
            $$aliasMemberTest.$init$B$2895=$init$B$2895;
            $init$B$2895();
            $$aliasMemberTest.B$2895=B$2895;
            
            //ClassDeclaration AliasA at members.ceylon (86:4-86:32)
            $$aliasMemberTest.AliasA$AliasMemberTest=$$aliasMemberTest.A$AliasMemberTest;
            
            //ClassDeclaration AliasB at members.ceylon (87:4-87:25)
            $$aliasMemberTest.AliasB$2896=$$aliasMemberTest.B$2895;
            
            //MethodDefinition b at members.ceylon (88:4-88:43)
            $$aliasMemberTest.b=function b(){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasB$2896().getS();
            };
            //MethodDeclaration f1 at members.ceylon (90:4-90:30)
            $$aliasMemberTest.f1=function (){
                var $$aliasMemberTest=this;
                return Util();
            };
            
            //MethodDeclaration f2 at members.ceylon (91:4-91:29)
            $$aliasMemberTest.f2=function (){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasA$AliasMemberTest();
            };
        })(AliasMemberTest.$$.prototype);
    }
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDefinition test at members.ceylon (94:0-116:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (95:4-95:24)
    var c$2897=Counter((0));
    $$$c2244.check(c$2897.getCount().equals((0)),$$$cl2243.String("counter 1",9));
    c$2897.inc();
    c$2897.inc();
    $$$c2244.check(c$2897.getCount().equals((2)),$$$cl2243.String("counter 2",9));
    $$$c2244.check(c$2897.getString().equals($$$cl2243.String("Counter[2]",10)),$$$cl2243.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (103:4-103:27)
    var at$2898=AssignTest();
    (at$2898.setX((5)));
    $$$c2244.check(at$2898.getX().equals((5)),$$$cl2243.String("assign to member",16));
    (tmp$2899=at$2898,tmp$2899.setY((2)),tmp$2899.getY());
    var tmp$2899;
    $$$c2244.check(at$2898.getY().equals((2)),$$$cl2243.String("assign using setter",19));
    $$$c2244.check(Issue50().getZ().equals($$$cl2243.String("ok",2)),$$$cl2243.String("Issue #50",9));
    test_outer_inner_safety();
    $$$c2244.check(AliasMemberTest().AliasA$AliasMemberTest().getS().equals($$$cl2243.String("A",1)),$$$cl2243.String("shared inner alias class",24));
    $$$c2244.check(AliasMemberTest().b().equals($$$cl2243.String("B",1)),$$$cl2243.String("non-shared inner alias class",28));
    $$$c2244.check(AliasMemberTest().f1().getS().equals($$$cl2243.String("123",3)),$$$cl2243.String("alias method member 1",21));
    $$$c2244.check(AliasMemberTest().f2().getS().equals($$$cl2243.String("A",1)),$$$cl2243.String("alias method member 2",21));
    $$$c2244.results();
}
exports.test=test;

//MethodDefinition test_outer_inner_safety at outer.ceylon (3:0-18:0)
function test_outer_inner_safety(){
    
    //ClassDefinition Outer at outer.ceylon (4:2-6:2)
    function Outer$2900($$outer$2900){
        $init$Outer$2900();
        if ($$outer$2900===undefined)$$outer$2900=new Outer$2900.$$;
        return $$outer$2900;
    }
    function $init$Outer$2900(){
        if (Outer$2900.$$===undefined){
            $$$cl2243.initTypeProto(Outer$2900,'members::test_outer_inner_safety.Outer',$$$cl2243.Basic);
            (function($$outer$2900){
                
                //ClassDefinition Inner at outer.ceylon (5:4-5:27)
                function Inner$Outer($$inner$Outer){
                    $init$Inner$Outer();
                    if ($$inner$Outer===undefined)$$inner$Outer=new this.Inner$Outer.$$;
                    $$inner$Outer.$$outer$2900=this;
                    return $$inner$Outer;
                }
                function $init$Inner$Outer(){
                    if (Inner$Outer.$$===undefined){
                        $$$cl2243.initTypeProto(Inner$Outer,'members::test_outer_inner_safety.Outer.Inner',$$$cl2243.Basic);
                    }
                    return Inner$Outer;
                }
                $$outer$2900.$init$Inner$Outer=$init$Inner$Outer;
                $init$Inner$Outer();
                $$outer$2900.Inner$Outer=Inner$Outer;
            })(Outer$2900.$$.prototype);
        }
        return Outer$2900;
    }
    $init$Outer$2900();
    
    //AttributeDeclaration o at outer.ceylon (7:2-7:17)
    var o$2901=null;
    
    //AttributeDeclaration i1 at outer.ceylon (8:2-8:30)
    var i1$2902=(opt$2903=o$2901,$$$cl2243.JsCallable(opt$2903,opt$2903!==null?opt$2903.Inner$Outer:null))();
    var opt$2903;
    
    //MethodDeclaration cons at outer.ceylon (9:2-9:35)
    var cons$2904=function (){
        return (opt$2905=o$2901,$$$cl2243.JsCallable(opt$2905,opt$2905!==null?opt$2905.Inner$Outer:null))();
    };
    var opt$2905;
    var i1$2906;
    if((i1$2906=i1$2902)!==null){
        $$$c2244.fail($$$cl2243.String("i1 should be null",17));
    }
    $$$c2244.check($$$cl2243.className(cons$2904).equals($$$cl2243.String("ceylon.language::JsCallable",27)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("cons is Callable, ",18),$$$cl2243.className(cons$2904).getString()]).getString());
    
    //AttributeDeclaration i2 at outer.ceylon (14:2-14:30)
    var i2$2907=cons$2904();
    var i2$2908;
    if((i2$2908=i2$2907)!==null){
        $$$c2244.fail($$$cl2243.String("i2 should not exist",19));
    }
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
