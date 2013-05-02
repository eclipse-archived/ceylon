(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"members","$mod-version":"0.1","members":{"test_outer_inner_safety":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_outer_inner_safety"},"Issue50":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"z":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"z"}},"$nm":"Issue50"},"$pkg-shared":"1","Util":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"Util"},"Issue10C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f6":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f6"},"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f7"},"f8":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f8"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"f9"},"f10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f10"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f4"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C1"},"Issue10C2":{"super":{"$pk":"members","$nm":"Issue10C1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f7"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"f9"},"f12":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f12"},"f11":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f11"},"f13":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f13"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Counter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"initCount"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"currentCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"currentCount"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"initialCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"initialCount"}},"$nm":"Counter"},"AliasMemberTest":{"$i":{"I2Alias":{"$mt":"ifc","$alias":{"$pk":"members","$nm":"I2"},"$nm":"I2Alias"},"I1Alias":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"members","$nm":"I1"},"$nm":"I1Alias"},"I1":{"$mt":"ifc","$an":{"shared":[]},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I1"},"I2":{"$mt":"ifc","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I2"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"b"},"f1":{"$t":{"$pk":"members","$nm":"Util"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f2":{"$t":{"$pk":"members","$nm":"A"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$c":{"AliasB":{"super":{"$pk":"members","$nm":"B"},"$mt":"cls","$alias":"1","$nm":"AliasB"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I1Alias"}],"$mt":"cls","$an":{"shared":[]},"$nm":"A"},"AliasA":{"super":{"$pk":"members","$nm":"A"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"AliasA"},"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I2Alias"}],"$mt":"cls","$nm":"B"}},"$nm":"AliasMemberTest"},"AssignTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"AssignTest"},"testIssue10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIssue10"}}}
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2=require('check/0.1/check-0.1');

//ClassDefinition Counter at members.ceylon (3:0-17:0)
function Counter(initCount$750, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$750===undefined){initCount$750=(0);}
    $$counter.initCount$750=initCount$750;
    
    //AttributeDeclaration currentCount at members.ceylon (4:4-4:41)
    var currentCount$751=initCount$750;
    $$$cl1.defineAttr($$counter,'currentCount$751',function(){return currentCount$751;},function(currentCount$752){return currentCount$751=currentCount$752;});
    
    //AttributeGetterDefinition count at members.ceylon (5:4-7:4)
    $$$cl1.defineAttr($$counter,'count',function(){
        return currentCount$751;
    });
    
    //MethodDefinition inc at members.ceylon (8:4-10:4)
    function inc(){
        currentCount$751=currentCount$751.plus((1));
    }
    $$counter.inc=inc;
    inc.$$metamodel$$={$nm:'inc',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//inc.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    
    //AttributeGetterDefinition initialCount at members.ceylon (11:4-13:4)
    $$$cl1.defineAttr($$counter,'initialCount',function(){
        return initCount$750;
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
    Counter.$$.$$metamodel$$={$nm:'Counter',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDefinition Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$753, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$753=arg1$753;
    
    //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
    var i1$754=(3);
    $$$cl1.defineAttr($$issue10C1,'i1$754',function(){return i1$754;});
    
    //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
    var i2$755=(5);
    $$$cl1.defineAttr($$issue10C1,'i2$755',function(){return i2$755;});
    
    //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
    var i3=(7);
    $$$cl1.defineAttr($$issue10C1,'i3',function(){return i3;});
    
    //MethodDefinition f1 at members.ceylon (23:4-23:39)
    function f1(){
        return arg1$753;
    }
    $$issue10C1.f1=f1;
    f1.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f1.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f2 at members.ceylon (24:4-24:37)
    function f2(){
        return i1$754;
    }
    $$issue10C1.f2=f2;
    f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f2.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f3 at members.ceylon (25:4-25:37)
    function f3(){
        return i2$755;
    }
    $$issue10C1.f3=f3;
    f3.$$metamodel$$={$nm:'f3',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f3.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f4 at members.ceylon (26:4-26:37)
    function f4(){
        return $$issue10C1.i3;
    }
    $$issue10C1.f4=f4;
    f4.$$metamodel$$={$nm:'f4',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f4.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f5 at members.ceylon (27:4-27:29)
    function f5$756(){
        return (9);
    };f5$756.$$metamodel$$={$nm:'f5',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f5$756.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f6 at members.ceylon (28:4-28:39)
    function f6(){
        return f5$756();
    }
    $$issue10C1.f6=f6;
    f6.$$metamodel$$={$nm:'f6',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f6.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f7 at members.ceylon (29:4-29:30)
    function f7$757(){
        return (11);
    };f7$757.$$metamodel$$={$nm:'f7',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f7$757.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f8 at members.ceylon (30:4-30:39)
    function f8(){
        return f7$757();
    }
    $$issue10C1.f8=f8;
    f8.$$metamodel$$={$nm:'f8',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f8.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f9 at members.ceylon (31:4-31:45)
    function f9(){
        return (13);
    }
    $$issue10C1.f9=f9;
    f9.$$metamodel$$={$nm:'f9',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f9.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f10 at members.ceylon (32:4-32:40)
    function f10(){
        return $$issue10C1.f9();
    }
    $$issue10C1.f10=f10;
    f10.$$metamodel$$={$nm:'f10',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f10.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    return $$issue10C1;
}
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl1.initTypeProto(Issue10C1,'members::Issue10C1',$$$cl1.Basic);
    }
    Issue10C1.$$.$$metamodel$$={$nm:'Issue10C1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDefinition Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$758, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$758=arg1$758;
    Issue10C1((1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
    var i1$759=(4);
    $$$cl1.defineAttr($$issue10C2,'i1$759',function(){return i1$759;});
    
    //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
    var i2=(6);
    $$$cl1.defineAttr($$issue10C2,'i2',function(){return i2;});
    
    //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
    var i3=(8);
    $$$cl1.defineAttr($$issue10C2,'i3',function(){return i3;});
    
    //MethodDefinition f11 at members.ceylon (38:4-38:40)
    function f11(){
        return arg1$758;
    }
    $$issue10C2.f11=f11;
    f11.$$metamodel$$={$nm:'f11',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f11.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f12 at members.ceylon (39:4-39:38)
    function f12(){
        return i1$759;
    }
    $$issue10C2.f12=f12;
    f12.$$metamodel$$={$nm:'f12',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f12.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f5 at members.ceylon (40:4-40:30)
    function f5$760(){
        return (10);
    };f5$760.$$metamodel$$={$nm:'f5',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f5$760.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f13 at members.ceylon (41:4-41:40)
    function f13(){
        return f5$760();
    }
    $$issue10C2.f13=f13;
    f13.$$metamodel$$={$nm:'f13',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f13.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f7 at members.ceylon (42:4-42:37)
    function f7(){
        return (12);
    }
    $$issue10C2.f7=f7;
    f7.$$metamodel$$={$nm:'f7',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f7.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f9 at members.ceylon (43:4-43:44)
    function f9(){
        return (14);
    }
    $$issue10C2.f9=f9;
    f9.$$metamodel$$={$nm:'f9',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f9.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    return $$issue10C2;
}
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl1.initTypeProto(Issue10C2,'members::Issue10C2',Issue10C1);
    }
    Issue10C2.$$.$$metamodel$$={$nm:'Issue10C2',$mt:'cls','super':{t:Issue10C1},'satisfies':[]};
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDefinition testIssue10 at members.ceylon (46:0-63:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (47:4-47:28)
    var obj$761=Issue10C2((2));
    $$$c2.check(obj$761.f1().equals((1)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$c2.check(obj$761.f11().equals((2)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$c2.check(obj$761.f2().equals((3)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$761.f12().equals((4)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$761.f3().equals((5)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$761.i2.equals((6)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$761.f4().equals((8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$761.i3.equals((8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$761.f6().equals((9)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$761.f13().equals((10)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$761.f8().equals((11)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$761.f7().equals((12)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check(obj$761.f10().equals((14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check(obj$761.f9().equals((14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check((!obj$761.string.empty),$$$cl1.String("Issue #113 (inheritance)",24));
};testIssue10.$$metamodel$$={$nm:'testIssue10',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testIssue10.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//ClassDefinition AssignTest at members.ceylon (65:0-69:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (66:4-66:33)
    var x=(1);
    $$$cl1.defineAttr($$assignTest,'x',function(){return x;},function(x$762){return x=x$762;});
    
    //AttributeGetterDefinition y at members.ceylon (67:4-67:33)
    $$$cl1.defineAttr($$assignTest,'y',function(){
        return $$assignTest.x;
    },function(y$763){
        $$assignTest.x=y$763;
    });
    return $$assignTest;
}
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl1.initTypeProto(AssignTest,'members::AssignTest',$$$cl1.Basic);
    }
    AssignTest.$$.$$metamodel$$={$nm:'AssignTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
    Issue50.$$.$$metamodel$$={$nm:'Issue50',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
    Util.$$.$$metamodel$$={$nm:'Util',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
        $$i1$AliasMemberTest.$$outer=this;
        
        //AttributeGetterDefinition s at members.ceylon (80:25-80:53)
        $$$cl1.defineAttr($$i1$AliasMemberTest,'s',function(){
            return $$$cl1.String("A",1);
        });
    }
    $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
    function $init$I1$AliasMemberTest(){
        if (I1$AliasMemberTest.$$===undefined){
            $$$cl1.initTypeProto(I1$AliasMemberTest,'members::AliasMemberTest.I1');
            AliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
        }
        I1$AliasMemberTest.$$.$$metamodel$$={$nm:'I1',$mt:'ifc','satisfies':[]};
        return I1$AliasMemberTest;
    }
    $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
    $init$I1$AliasMemberTest();
    
    //InterfaceDeclaration I1Alias at members.ceylon (81:4-81:34)
    var I1Alias$AliasMemberTest=$$aliasMemberTest.I1$AliasMemberTest;
    $$aliasMemberTest.I1Alias$AliasMemberTest=I1Alias$AliasMemberTest;
    
    //InterfaceDefinition I2 at members.ceylon (82:4-82:48)
    function I2$764($$i2$764){
        $$i2$764.$$outer=this;
        
        //AttributeGetterDefinition s at members.ceylon (82:18-82:46)
        $$$cl1.defineAttr($$i2$764,'s',function(){
            return $$$cl1.String("B",1);
        });
    }
    function $init$I2$764(){
        if (I2$764.$$===undefined){
            $$$cl1.initTypeProto(I2$764,'members::AliasMemberTest.I2');
            AliasMemberTest.I2$764=I2$764;
        }
        I2$764.$$.$$metamodel$$={$nm:'I2',$mt:'ifc','satisfies':[]};
        return I2$764;
    }
    $$aliasMemberTest.$init$I2$764=$init$I2$764;
    $init$I2$764();
    
    //InterfaceDeclaration I2Alias at members.ceylon (83:4-83:27)
    var I2Alias$765=I2$764;
    
    //ClassDefinition A at members.ceylon (84:4-84:40)
    function A$AliasMemberTest($$a$AliasMemberTest){
        $init$A$AliasMemberTest();
        if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new A$AliasMemberTest.$$;
        $$a$AliasMemberTest.$$outer=this;
        $$aliasMemberTest.I1$AliasMemberTest($$a$AliasMemberTest);
        return $$a$AliasMemberTest;
    }
    $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
    function $init$A$AliasMemberTest(){
        if (A$AliasMemberTest.$$===undefined){
            $$$cl1.initTypeProto(A$AliasMemberTest,'members::AliasMemberTest.A',$$$cl1.Basic,$$aliasMemberTest.$init$I1$AliasMemberTest());
            AliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
        }
        A$AliasMemberTest.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:AliasMemberTest.I1$AliasMemberTest}]};
        return A$AliasMemberTest;
    }
    $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
    $init$A$AliasMemberTest();
    
    //ClassDefinition B at members.ceylon (85:4-85:33)
    function B$766($$b$766){
        $init$B$766();
        if ($$b$766===undefined)$$b$766=new B$766.$$;
        $$b$766.$$outer=this;
        I2$764($$b$766);
        return $$b$766;
    }
    function $init$B$766(){
        if (B$766.$$===undefined){
            $$$cl1.initTypeProto(B$766,'members::AliasMemberTest.B',$$$cl1.Basic,$init$I2$764());
            AliasMemberTest.B$766=B$766;
        }
        B$766.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:AliasMemberTest.I2$764}]};
        return B$766;
    }
    $$aliasMemberTest.$init$B$766=$init$B$766;
    $init$B$766();
    
    //ClassDeclaration AliasA at members.ceylon (86:4-86:32)
    function AliasA$AliasMemberTest($$aliasA$AliasMemberTest){return $$aliasMemberTest.A$AliasMemberTest($$aliasA$AliasMemberTest);}
    AliasA$AliasMemberTest.$$=$$aliasMemberTest.A$AliasMemberTest.$$;
    $$aliasMemberTest.AliasA$AliasMemberTest=AliasA$AliasMemberTest;
    
    //ClassDeclaration AliasB at members.ceylon (87:4-87:25)
    function AliasB$767($$aliasB$767){return B$766($$aliasB$767);}
    AliasB$767.$$=B$766.$$;
    
    //MethodDefinition b at members.ceylon (88:4-88:43)
    function b(){
        return AliasB$767().s;
    }
    $$aliasMemberTest.b=b;
    b.$$metamodel$$={$nm:'b',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//b.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //MethodDeclaration f1 at members.ceylon (90:4-90:30)
    var f1=function (){
        return Util();
    };
    f1.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:Util},$ps:[]};
    $$aliasMemberTest.f1=f1;
    
    //MethodDeclaration f2 at members.ceylon (91:4-91:29)
    var f2=function (){
        return $$aliasMemberTest.AliasA$AliasMemberTest();
    };
    f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:AliasMemberTest.A$AliasMemberTest},$ps:[]};
    $$aliasMemberTest.f2=f2;
    return $$aliasMemberTest;
}
function $init$AliasMemberTest(){
    if (AliasMemberTest.$$===undefined){
        $$$cl1.initTypeProto(AliasMemberTest,'members::AliasMemberTest',$$$cl1.Basic);
    }
    AliasMemberTest.$$.$$metamodel$$={$nm:'AliasMemberTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDefinition test at members.ceylon (94:0-116:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (95:4-95:24)
    var c$768=Counter((0));
    $$$c2.check(c$768.count.equals((0)),$$$cl1.String("counter 1",9));
    c$768.inc();
    c$768.inc();
    $$$c2.check(c$768.count.equals((2)),$$$cl1.String("counter 2",9));
    $$$c2.check(c$768.string.equals($$$cl1.String("Counter[2]",10)),$$$cl1.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (103:4-103:27)
    var at$769=AssignTest();
    (at$769.x=(5));
    $$$c2.check(at$769.x.equals((5)),$$$cl1.String("assign to member",16));
    (tmp$770=at$769,tmp$770.y=(2),tmp$770.y);
    var tmp$770;
    $$$c2.check(at$769.y.equals((2)),$$$cl1.String("assign using setter",19));
    $$$c2.check(Issue50().z.equals($$$cl1.String("ok",2)),$$$cl1.String("Issue #50",9));
    test_outer_inner_safety();
    $$$c2.check(AliasMemberTest().AliasA$AliasMemberTest().s.equals($$$cl1.String("A",1)),$$$cl1.String("shared inner alias class",24));
    $$$c2.check(AliasMemberTest().b().equals($$$cl1.String("B",1)),$$$cl1.String("non-shared inner alias class",28));
    $$$c2.check(AliasMemberTest().f1().s.equals($$$cl1.String("123",3)),$$$cl1.String("alias method member 1",21));
    $$$c2.check(AliasMemberTest().f2().s.equals($$$cl1.String("A",1)),$$$cl1.String("alias method member 2",21));
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition test_outer_inner_safety at outer.ceylon (3:0-18:0)
function test_outer_inner_safety(){
    
    //ClassDefinition Outer at outer.ceylon (4:2-6:2)
    function Outer$771($$outer$771){
        $init$Outer$771();
        if ($$outer$771===undefined)$$outer$771=new Outer$771.$$;
        
        //ClassDefinition Inner at outer.ceylon (5:4-5:27)
        function Inner$Outer($$inner$Outer){
            $init$Inner$Outer();
            if ($$inner$Outer===undefined)$$inner$Outer=new Inner$Outer.$$;
            $$inner$Outer.$$outer=this;
            return $$inner$Outer;
        }
        $$outer$771.Inner$Outer=Inner$Outer;
        function $init$Inner$Outer(){
            if (Inner$Outer.$$===undefined){
                $$$cl1.initTypeProto(Inner$Outer,'members::test_outer_inner_safety.Outer.Inner',$$$cl1.Basic);
                Outer$771.Inner$Outer=Inner$Outer;
            }
            Inner$Outer.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
            return Inner$Outer;
        }
        $$outer$771.$init$Inner$Outer=$init$Inner$Outer;
        $init$Inner$Outer();
        return $$outer$771;
    }
    function $init$Outer$771(){
        if (Outer$771.$$===undefined){
            $$$cl1.initTypeProto(Outer$771,'members::test_outer_inner_safety.Outer',$$$cl1.Basic);
        }
        Outer$771.$$.$$metamodel$$={$nm:'Outer',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return Outer$771;
    }
    $init$Outer$771();
    
    //AttributeDeclaration o at outer.ceylon (7:2-7:17)
    var o$772=null;
    
    //AttributeDeclaration i1 at outer.ceylon (8:2-8:30)
    var i1$773=(opt$774=o$772,$$$cl1.JsCallable(opt$774,opt$774!==null?opt$774.Inner$Outer:null))();
    var opt$774;
    
    //MethodDeclaration cons at outer.ceylon (9:2-9:35)
    var cons$775=function (){
        return (opt$776=o$772,$$$cl1.JsCallable(opt$776,opt$776!==null?opt$776.Inner$Outer:null))();
    };
    cons$775.$$metamodel$$={$nm:'cons',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:Outer$771.Inner$Outer}]},$ps:[]};
    var opt$776;
    var i1$777;
    if((i1$777=i1$773)!==null){
        $$$c2.fail($$$cl1.String("i1 should be null",17));
    }
    $$$c2.check($$$cl1.className($$$cl1.$JsCallable(cons$775,[],{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:Outer$771.Inner$Outer}]}})).equals($$$cl1.String("ceylon.language::JsCallable",27)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("cons is Callable, ",18),$$$cl1.className($$$cl1.$JsCallable(cons$775,[],{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:Outer$771.Inner$Outer}]}})).string]).string);
    
    //AttributeDeclaration i2 at outer.ceylon (14:2-14:30)
    var i2$778=cons$775();
    var i2$779;
    if((i2$779=i2$778)!==null){
        $$$c2.fail($$$cl1.String("i2 should not exist",19));
    }
};test_outer_inner_safety.$$metamodel$$={$nm:'test_outer_inner_safety',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test_outer_inner_safety.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
