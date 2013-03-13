(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"members","$mod-version":"0.1","members":{"test_outer_inner_safety":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_outer_inner_safety"},"Issue50":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"z":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"z"}},"$nm":"Issue50"},"$pkg-shared":"1","Util":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"Util"},"Issue10C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f6":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f6"},"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f7"},"f8":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f8"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"f9"},"f10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f10"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f4"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C1"},"Issue10C2":{"super":{"$pk":"members","$nm":"Issue10C1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f7"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"f9"},"f12":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f12"},"f11":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f11"},"f13":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f13"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Counter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"initCount"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"currentCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"currentCount"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"initialCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"initialCount"}},"$nm":"Counter"},"AliasMemberTest":{"$i":{"I2Alias":{"$mt":"ifc","$alias":{"$pk":"members","$nm":"I2"},"$nm":"I2Alias"},"I1Alias":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"members","$nm":"I1"},"$nm":"I1Alias"},"I1":{"$mt":"ifc","$an":{"shared":[]},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I1"},"I2":{"$mt":"ifc","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I2"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"b"},"f1":{"$t":{"$pk":"members","$nm":"Util"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f2":{"$t":{"$pk":"members","$nm":"A"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$c":{"AliasB":{"super":{"$pk":"members","$nm":"B"},"$mt":"cls","$alias":"1","$nm":"AliasB"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I1Alias"}],"$mt":"cls","$an":{"shared":[]},"$nm":"A"},"AliasA":{"super":{"$pk":"members","$nm":"A"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"AliasA"},"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I2Alias"}],"$mt":"cls","$nm":"B"}},"$nm":"AliasMemberTest"},"AssignTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"AssignTest"},"testIssue10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIssue10"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//ClassDefinition Counter at members.ceylon (3:0-17:0)
function Counter(initCount$748, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$748===undefined){initCount$748=(0);}
    $$counter.initCount$748=initCount$748;
    
    //AttributeDeclaration currentCount at members.ceylon (4:4-4:41)
    var currentCount$749=initCount$748;
    $$$cl1.defineAttr($$counter,'currentCount$749',function(){return currentCount$749;},function(currentCount$750){return currentCount$749=currentCount$750;});
    
    //AttributeGetterDefinition count at members.ceylon (5:4-7:4)
    $$$cl1.defineAttr($$counter,'count',function(){
        return currentCount$749;
    });
    
    //MethodDefinition inc at members.ceylon (8:4-10:4)
    function inc(){
        currentCount$749=currentCount$749.plus((1));
    }
    $$counter.inc=inc;
    
    //AttributeGetterDefinition initialCount at members.ceylon (11:4-13:4)
    $$$cl1.defineAttr($$counter,'initialCount',function(){
        return initCount$748;
    });
    
    //AttributeGetterDefinition string at members.ceylon (14:4-16:4)
    $$$cl1.defineAttr($$counter,'string',function(){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Counter[",8),$$counter.count.string,$$$cl1.String("]",1)]).string;
    });
    return $$counter;
}
exports.Counter=Counter;
function $init$Counter(){
    if (Counter.$$===undefined){
        $$$cl1.initTypeProto(Counter,'members::Counter',$$$cl1.Basic);
    }
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDefinition Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$751, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$751=arg1$751;
    
    //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
    var i1$752=(3);
    $$$cl1.defineAttr($$issue10C1,'i1$752',function(){return i1$752;});
    
    //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
    var i2$753=(5);
    $$$cl1.defineAttr($$issue10C1,'i2$753',function(){return i2$753;});
    
    //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
    var i3=(7);
    $$$cl1.defineAttr($$issue10C1,'i3',function(){return i3;});
    
    //MethodDefinition f1 at members.ceylon (23:4-23:39)
    function f1(){
        return arg1$751;
    }
    $$issue10C1.f1=f1;
    
    //MethodDefinition f2 at members.ceylon (24:4-24:37)
    function f2(){
        return i1$752;
    }
    $$issue10C1.f2=f2;
    
    //MethodDefinition f3 at members.ceylon (25:4-25:37)
    function f3(){
        return i2$753;
    }
    $$issue10C1.f3=f3;
    
    //MethodDefinition f4 at members.ceylon (26:4-26:37)
    function f4(){
        return $$issue10C1.i3;
    }
    $$issue10C1.f4=f4;
    
    //MethodDefinition f5 at members.ceylon (27:4-27:29)
    function f5$754(){
        return (9);
    };
    
    //MethodDefinition f6 at members.ceylon (28:4-28:39)
    function f6(){
        return f5$754();
    }
    $$issue10C1.f6=f6;
    
    //MethodDefinition f7 at members.ceylon (29:4-29:30)
    function f7$755(){
        return (11);
    };
    
    //MethodDefinition f8 at members.ceylon (30:4-30:39)
    function f8(){
        return f7$755();
    }
    $$issue10C1.f8=f8;
    
    //MethodDefinition f9 at members.ceylon (31:4-31:45)
    function f9(){
        return (13);
    }
    $$issue10C1.f9=f9;
    
    //MethodDefinition f10 at members.ceylon (32:4-32:40)
    function f10(){
        return $$issue10C1.f9();
    }
    $$issue10C1.f10=f10;
    return $$issue10C1;
}
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl1.initTypeProto(Issue10C1,'members::Issue10C1',$$$cl1.Basic);
    }
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDefinition Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$756, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$756=arg1$756;
    Issue10C1((1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
    var i1$757=(4);
    $$$cl1.defineAttr($$issue10C2,'i1$757',function(){return i1$757;});
    
    //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
    var i2=(6);
    $$$cl1.defineAttr($$issue10C2,'i2',function(){return i2;});
    
    //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
    var i3=(8);
    $$$cl1.defineAttr($$issue10C2,'i3',function(){return i3;});
    
    //MethodDefinition f11 at members.ceylon (38:4-38:40)
    function f11(){
        return arg1$756;
    }
    $$issue10C2.f11=f11;
    
    //MethodDefinition f12 at members.ceylon (39:4-39:38)
    function f12(){
        return i1$757;
    }
    $$issue10C2.f12=f12;
    
    //MethodDefinition f5 at members.ceylon (40:4-40:30)
    function f5$758(){
        return (10);
    };
    
    //MethodDefinition f13 at members.ceylon (41:4-41:40)
    function f13(){
        return f5$758();
    }
    $$issue10C2.f13=f13;
    
    //MethodDefinition f7 at members.ceylon (42:4-42:37)
    function f7(){
        return (12);
    }
    $$issue10C2.f7=f7;
    
    //MethodDefinition f9 at members.ceylon (43:4-43:44)
    function f9(){
        return (14);
    }
    $$issue10C2.f9=f9;
    return $$issue10C2;
}
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl1.initTypeProto(Issue10C2,'members::Issue10C2',Issue10C1);
    }
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDefinition testIssue10 at members.ceylon (46:0-63:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (47:4-47:28)
    var obj$759=Issue10C2((2));
    $$$c2.check(obj$759.f1().equals((1)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$c2.check(obj$759.f11().equals((2)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$c2.check(obj$759.f2().equals((3)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$759.f12().equals((4)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$759.f3().equals((5)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$759.i2.equals((6)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$759.f4().equals((8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$759.i3.equals((8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$759.f6().equals((9)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$759.f13().equals((10)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$759.f8().equals((11)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$759.f7().equals((12)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check(obj$759.f10().equals((14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check(obj$759.f9().equals((14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check((!obj$759.string.empty),$$$cl1.String("Issue #113 (inheritance)",24));
};

//ClassDefinition AssignTest at members.ceylon (65:0-69:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (66:4-66:33)
    var x=(1);
    $$$cl1.defineAttr($$assignTest,'x',function(){return x;},function(x$760){return x=x$760;});
    
    //AttributeGetterDefinition y at members.ceylon (67:4-67:33)
    $$$cl1.defineAttr($$assignTest,'y',function(){
        return $$assignTest.x;
    },function(y$761){
        $$assignTest.x=y$761;
    });
    return $$assignTest;
}
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl1.initTypeProto(AssignTest,'members::AssignTest',$$$cl1.Basic);
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
    var z=$$$cl1.String("ok",2);
    $$$cl1.defineAttr($$issue50,'z',function(){return z;});
    return $$issue50;
}
function $init$Issue50(){
    if (Issue50.$$===undefined){
        $$$cl1.initTypeProto(Issue50,'members::Issue50',$$$cl1.Basic);
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
    var s=$$$cl1.String("123",3);
    $$$cl1.defineAttr($$util,'s',function(){return s;});
    return $$util;
}
function $init$Util(){
    if (Util.$$===undefined){
        $$$cl1.initTypeProto(Util,'members::Util',$$$cl1.Basic);
    }
    return Util;
}
exports.$init$Util=$init$Util;
$init$Util();

//ClassDefinition AliasMemberTest at members.ceylon (79:0-92:0)
function AliasMemberTest($$aliasMemberTest){
    $init$AliasMemberTest();
    if ($$aliasMemberTest===undefined)$$aliasMemberTest=new AliasMemberTest.$$;
    
    //InterfaceDefinition I1 at members.ceylon (80:4-80:55)
    function I1$AliasMemberTest($$i1$AliasMemberTest){
        
        //AttributeGetterDefinition s at members.ceylon (80:25-80:53)
        $$$cl1.defineAttr($$i1$AliasMemberTest,'s',function(){
            return $$$cl1.String("A",1);
        });
    }
    $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
    function $init$I1$AliasMemberTest(){
        if (I1$AliasMemberTest.$$===undefined){
            $$$cl1.initTypeProto(I1$AliasMemberTest,'members::AliasMemberTest.I1');
        }
        return I1$AliasMemberTest;
    }
    $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
    $init$I1$AliasMemberTest();
    
    //InterfaceDeclaration I1Alias at members.ceylon (81:4-81:34)
    var I1Alias$AliasMemberTest=$$aliasMemberTest.I1$AliasMemberTest;
    $$aliasMemberTest.I1Alias$AliasMemberTest=I1Alias$AliasMemberTest;
    
    //InterfaceDefinition I2 at members.ceylon (82:4-82:48)
    function I2$762($$i2$762){
        
        //AttributeGetterDefinition s at members.ceylon (82:18-82:46)
        $$$cl1.defineAttr($$i2$762,'s',function(){
            return $$$cl1.String("B",1);
        });
    }
    function $init$I2$762(){
        if (I2$762.$$===undefined){
            $$$cl1.initTypeProto(I2$762,'members::AliasMemberTest.I2');
        }
        return I2$762;
    }
    $$aliasMemberTest.$init$I2$762=$init$I2$762;
    $init$I2$762();
    
    //InterfaceDeclaration I2Alias at members.ceylon (83:4-83:27)
    var I2Alias$763=I2$762;
    
    //ClassDefinition A at members.ceylon (84:4-84:40)
    function A$AliasMemberTest($$a$AliasMemberTest){
        $init$A$AliasMemberTest();
        if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new A$AliasMemberTest.$$;
        $$aliasMemberTest.I1$AliasMemberTest($$a$AliasMemberTest);
        return $$a$AliasMemberTest;
    }
    $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
    function $init$A$AliasMemberTest(){
        if (A$AliasMemberTest.$$===undefined){
            $$$cl1.initTypeProto(A$AliasMemberTest,'members::AliasMemberTest.A',$$$cl1.Basic,$$aliasMemberTest.$init$I1$AliasMemberTest());
        }
        return A$AliasMemberTest;
    }
    $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
    $init$A$AliasMemberTest();
    
    //ClassDefinition B at members.ceylon (85:4-85:33)
    function B$764($$b$764){
        $init$B$764();
        if ($$b$764===undefined)$$b$764=new B$764.$$;
        I2$762($$b$764);
        return $$b$764;
    }
    function $init$B$764(){
        if (B$764.$$===undefined){
            $$$cl1.initTypeProto(B$764,'members::AliasMemberTest.B',$$$cl1.Basic,$init$I2$762());
        }
        return B$764;
    }
    $$aliasMemberTest.$init$B$764=$init$B$764;
    $init$B$764();
    
    //ClassDeclaration AliasA at members.ceylon (86:4-86:32)
    function AliasA$AliasMemberTest($$aliasA$AliasMemberTest){return $$aliasMemberTest.A$AliasMemberTest($$aliasA$AliasMemberTest);}
    AliasA$AliasMemberTest.$$=$$aliasMemberTest.A$AliasMemberTest.$$;
    $$aliasMemberTest.AliasA$AliasMemberTest=AliasA$AliasMemberTest;
    
    //ClassDeclaration AliasB at members.ceylon (87:4-87:25)
    function AliasB$765($$aliasB$765){return B$764($$aliasB$765);}
    AliasB$765.$$=B$764.$$;
    
    //MethodDefinition b at members.ceylon (88:4-88:43)
    function b(){
        return AliasB$765().s;
    }
    $$aliasMemberTest.b=b;
    
    //MethodDeclaration f1 at members.ceylon (90:4-90:30)
    var f1=function (){
        return Util();
    };
    $$aliasMemberTest.f1=f1;
    
    //MethodDeclaration f2 at members.ceylon (91:4-91:29)
    var f2=function (){
        return $$aliasMemberTest.AliasA$AliasMemberTest();
    };
    $$aliasMemberTest.f2=f2;
    return $$aliasMemberTest;
}
function $init$AliasMemberTest(){
    if (AliasMemberTest.$$===undefined){
        $$$cl1.initTypeProto(AliasMemberTest,'members::AliasMemberTest',$$$cl1.Basic);
    }
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDefinition test at members.ceylon (94:0-116:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (95:4-95:24)
    var c$766=Counter((0));
    $$$c2.check(c$766.count.equals((0)),$$$cl1.String("counter 1",9));
    c$766.inc();
    c$766.inc();
    $$$c2.check(c$766.count.equals((2)),$$$cl1.String("counter 2",9));
    $$$c2.check(c$766.string.equals($$$cl1.String("Counter[2]",10)),$$$cl1.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (103:4-103:27)
    var at$767=AssignTest();
    (at$767.x=(5));
    $$$c2.check(at$767.x.equals((5)),$$$cl1.String("assign to member",16));
    (tmp$768=at$767,tmp$768.y=(2),tmp$768.y);
    var tmp$768;
    $$$c2.check(at$767.y.equals((2)),$$$cl1.String("assign using setter",19));
    $$$c2.check(Issue50().z.equals($$$cl1.String("ok",2)),$$$cl1.String("Issue #50",9));
    test_outer_inner_safety();
    $$$c2.check(AliasMemberTest().AliasA$AliasMemberTest().s.equals($$$cl1.String("A",1)),$$$cl1.String("shared inner alias class",24));
    $$$c2.check(AliasMemberTest().b().equals($$$cl1.String("B",1)),$$$cl1.String("non-shared inner alias class",28));
    $$$c2.check(AliasMemberTest().f1().s.equals($$$cl1.String("123",3)),$$$cl1.String("alias method member 1",21));
    $$$c2.check(AliasMemberTest().f2().s.equals($$$cl1.String("A",1)),$$$cl1.String("alias method member 2",21));
    $$$c2.results();
}
exports.test=test;

//MethodDefinition test_outer_inner_safety at outer.ceylon (3:0-18:0)
function test_outer_inner_safety(){
    
    //ClassDefinition Outer at outer.ceylon (4:2-6:2)
    function Outer$769($$outer$769){
        $init$Outer$769();
        if ($$outer$769===undefined)$$outer$769=new Outer$769.$$;
        
        //ClassDefinition Inner at outer.ceylon (5:4-5:27)
        function Inner$Outer($$inner$Outer){
            $init$Inner$Outer();
            if ($$inner$Outer===undefined)$$inner$Outer=new Inner$Outer.$$;
            return $$inner$Outer;
        }
        $$outer$769.Inner$Outer=Inner$Outer;
        function $init$Inner$Outer(){
            if (Inner$Outer.$$===undefined){
                $$$cl1.initTypeProto(Inner$Outer,'members::test_outer_inner_safety.Outer.Inner',$$$cl1.Basic);
            }
            return Inner$Outer;
        }
        $$outer$769.$init$Inner$Outer=$init$Inner$Outer;
        $init$Inner$Outer();
        return $$outer$769;
    }
    function $init$Outer$769(){
        if (Outer$769.$$===undefined){
            $$$cl1.initTypeProto(Outer$769,'members::test_outer_inner_safety.Outer',$$$cl1.Basic);
        }
        return Outer$769;
    }
    $init$Outer$769();
    
    //AttributeDeclaration o at outer.ceylon (7:2-7:17)
    var o$770=null;
    
    //AttributeDeclaration i1 at outer.ceylon (8:2-8:30)
    var i1$771=(opt$772=o$770,$$$cl1.JsCallable(opt$772,opt$772!==null?opt$772.Inner$Outer:null))();
    var opt$772;
    
    //MethodDeclaration cons at outer.ceylon (9:2-9:35)
    var cons$773=function (){
        return (opt$774=o$770,$$$cl1.JsCallable(opt$774,opt$774!==null?opt$774.Inner$Outer:null))();
    };
    var opt$774;
    var i1$775;
    if((i1$775=i1$771)!==null){
        $$$c2.fail($$$cl1.String("i1 should be null",17));
    }
    $$$c2.check($$$cl1.className($$$cl1.$JsCallable(cons$773,{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:Outer$769.Inner$Outer}]}})).equals($$$cl1.String("ceylon.language::JsCallable",27)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("cons is Callable, ",18),$$$cl1.className($$$cl1.$JsCallable(cons$773,{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:Outer$769.Inner$Outer}]}})).string]).string);
    
    //AttributeDeclaration i2 at outer.ceylon (14:2-14:30)
    var i2$776=cons$773();
    var i2$777;
    if((i2$777=i2$776)!==null){
        $$$c2.fail($$$cl1.String("i2 should not exist",19));
    }
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
