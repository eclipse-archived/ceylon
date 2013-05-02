(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"members","$mod-version":"0.1","members":{"test_outer_inner_safety":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_outer_inner_safety"},"Issue50":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"z":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"z"}},"$nm":"Issue50"},"$pkg-shared":"1","Util":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"Util"},"Issue10C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f6":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f6"},"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f7"},"f8":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f8"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"f9"},"f10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f10"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f4"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C1"},"Issue10C2":{"super":{"$pk":"members","$nm":"Issue10C1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"arg1"}],"$mt":"cls","$m":{"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f7"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"f9"},"f12":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f12"},"f11":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f11"},"f13":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f13"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Counter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"initCount"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"currentCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"currentCount"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"initialCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"initialCount"}},"$nm":"Counter"},"AliasMemberTest":{"$i":{"I2Alias":{"$mt":"ifc","$alias":{"$pk":"members","$nm":"I2"},"$nm":"I2Alias"},"I1Alias":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"members","$nm":"I1"},"$nm":"I1Alias"},"I1":{"$mt":"ifc","$an":{"shared":[]},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I1"},"I2":{"$mt":"ifc","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I2"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"b"},"f1":{"$t":{"$pk":"members","$nm":"Util"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f2":{"$t":{"$pk":"members","$nm":"A"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$c":{"AliasB":{"super":{"$pk":"members","$nm":"B"},"$mt":"cls","$alias":"1","$nm":"AliasB"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I1Alias"}],"$mt":"cls","$an":{"shared":[]},"$nm":"A"},"AliasA":{"super":{"$pk":"members","$nm":"A"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"AliasA"},"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I2Alias"}],"$mt":"cls","$nm":"B"}},"$nm":"AliasMemberTest"},"AssignTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"AssignTest"},"testIssue10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIssue10"}}}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2593=require('check/0.1/check-0.1');

//ClassDefinition Counter at members.ceylon (3:0-17:0)
function Counter(initCount$3360, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$3360===undefined){initCount$3360=(0);}
    $$counter.initCount$3360=initCount$3360;
    
    //AttributeDeclaration currentCount at members.ceylon (4:4-4:41)
    $$counter.currentCount$3361_=$$counter.initCount$3360;
    return $$counter;
}
exports.Counter=Counter;
function $init$Counter(){
    if (Counter.$$===undefined){
        $$$cl2592.initTypeProto(Counter,'members::Counter',$$$cl2592.Basic);
        (function($$counter){
            
            //AttributeDeclaration currentCount at members.ceylon (4:4-4:41)
            $$$cl2592.defineAttr($$counter,'currentCount$3361',function(){return this.currentCount$3361_;},function(currentCount$3362){return this.currentCount$3361_=currentCount$3362;});
            
            //AttributeGetterDefinition count at members.ceylon (5:4-7:4)
            $$$cl2592.defineAttr($$counter,'count',function(){
                var $$counter=this;
                return $$counter.currentCount$3361;
            });
            //MethodDefinition inc at members.ceylon (8:4-10:4)
            $$counter.inc=function inc(){
                var $$counter=this;
                $$counter.currentCount$3361=$$counter.currentCount$3361.plus((1));
            };$$counter.inc.$$metamodel$$={$nm:'inc',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
            //AttributeGetterDefinition initialCount at members.ceylon (11:4-13:4)
            $$$cl2592.defineAttr($$counter,'initialCount',function(){
                var $$counter=this;
                return $$counter.initCount$3360;
            });
            //AttributeGetterDefinition string at members.ceylon (14:4-16:4)
            $$$cl2592.defineAttr($$counter,'string',function(){
                var $$counter=this;
                return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("Counter[",8),$$counter.count.string,$$$cl2592.String("]",1)]).string;
            });
        })(Counter.$$.prototype);
    }
    Counter.$$.$$metamodel$$={$nm:'Counter',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDefinition Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$3363, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$3363=arg1$3363;
    
    //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
    $$issue10C1.i1$3364_=(3);
    
    //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
    $$issue10C1.i2$3365_=(5);
    
    //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
    $$issue10C1.i3$3366_=(7);
    return $$issue10C1;
}
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl2592.initTypeProto(Issue10C1,'members::Issue10C1',$$$cl2592.Basic);
        (function($$issue10C1){
            
            //AttributeDeclaration i1 at members.ceylon (20:4-20:18)
            $$$cl2592.defineAttr($$issue10C1,'i1$3364',function(){return this.i1$3364_;});
            
            //AttributeDeclaration i2 at members.ceylon (21:4-21:18)
            $$$cl2592.defineAttr($$issue10C1,'i2$3365',function(){return this.i2$3365_;});
            
            //AttributeDeclaration i3 at members.ceylon (22:4-22:33)
            $$$cl2592.defineAttr($$issue10C1,'i3',function(){return this.i3$3366_;});
            
            //MethodDefinition f1 at members.ceylon (23:4-23:39)
            $$issue10C1.f1=function f1(){
                var $$issue10C1=this;
                return $$issue10C1.arg1$3363;
            };$$issue10C1.f1.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f2 at members.ceylon (24:4-24:37)
            $$issue10C1.f2=function f2(){
                var $$issue10C1=this;
                return $$issue10C1.i1$3364;
            };$$issue10C1.f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f3 at members.ceylon (25:4-25:37)
            $$issue10C1.f3=function f3(){
                var $$issue10C1=this;
                return $$issue10C1.i2$3365;
            };$$issue10C1.f3.$$metamodel$$={$nm:'f3',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f4 at members.ceylon (26:4-26:37)
            $$issue10C1.f4=function f4(){
                var $$issue10C1=this;
                return $$issue10C1.i3;
            };$$issue10C1.f4.$$metamodel$$={$nm:'f4',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f5 at members.ceylon (27:4-27:29)
            $$issue10C1.f5$3367=function f5$3367(){
                var $$issue10C1=this;
                return (9);
            };$$issue10C1.f5$3367.$$metamodel$$={$nm:'f5',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f6 at members.ceylon (28:4-28:39)
            $$issue10C1.f6=function f6(){
                var $$issue10C1=this;
                return $$issue10C1.f5$3367();
            };$$issue10C1.f6.$$metamodel$$={$nm:'f6',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f7 at members.ceylon (29:4-29:30)
            $$issue10C1.f7$3368=function f7$3368(){
                var $$issue10C1=this;
                return (11);
            };$$issue10C1.f7$3368.$$metamodel$$={$nm:'f7',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f8 at members.ceylon (30:4-30:39)
            $$issue10C1.f8=function f8(){
                var $$issue10C1=this;
                return $$issue10C1.f7$3368();
            };$$issue10C1.f8.$$metamodel$$={$nm:'f8',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f9 at members.ceylon (31:4-31:45)
            $$issue10C1.f9=function f9(){
                var $$issue10C1=this;
                return (13);
            };$$issue10C1.f9.$$metamodel$$={$nm:'f9',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f10 at members.ceylon (32:4-32:40)
            $$issue10C1.f10=function f10(){
                var $$issue10C1=this;
                return $$issue10C1.f9();
            };$$issue10C1.f10.$$metamodel$$={$nm:'f10',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
        })(Issue10C1.$$.prototype);
    }
    Issue10C1.$$.$$metamodel$$={$nm:'Issue10C1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDefinition Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$3369, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$3369=arg1$3369;
    Issue10C1((1),$$issue10C2);
    
    //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
    $$issue10C2.i1$3370_=(4);
    
    //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
    $$issue10C2.i2$3371_=(6);
    
    //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
    $$issue10C2.i3$3372_=(8);
    return $$issue10C2;
}
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl2592.initTypeProto(Issue10C2,'members::Issue10C2',Issue10C1);
        (function($$issue10C2){
            
            //AttributeDeclaration i1 at members.ceylon (35:4-35:18)
            $$$cl2592.defineAttr($$issue10C2,'i1$3370',function(){return this.i1$3370_;});
            
            //AttributeDeclaration i2 at members.ceylon (36:4-36:25)
            $$$cl2592.defineAttr($$issue10C2,'i2',function(){return this.i2$3371_;});
            
            //AttributeDeclaration i3 at members.ceylon (37:4-37:32)
            $$$cl2592.defineAttr($$issue10C2,'i3',function(){return this.i3$3372_;});
            
            //MethodDefinition f11 at members.ceylon (38:4-38:40)
            $$issue10C2.f11=function f11(){
                var $$issue10C2=this;
                return $$issue10C2.arg1$3369;
            };$$issue10C2.f11.$$metamodel$$={$nm:'f11',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f12 at members.ceylon (39:4-39:38)
            $$issue10C2.f12=function f12(){
                var $$issue10C2=this;
                return $$issue10C2.i1$3370;
            };$$issue10C2.f12.$$metamodel$$={$nm:'f12',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f5 at members.ceylon (40:4-40:30)
            $$issue10C2.f5$3373=function f5$3373(){
                var $$issue10C2=this;
                return (10);
            };$$issue10C2.f5$3373.$$metamodel$$={$nm:'f5',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f13 at members.ceylon (41:4-41:40)
            $$issue10C2.f13=function f13(){
                var $$issue10C2=this;
                return $$issue10C2.f5$3373();
            };$$issue10C2.f13.$$metamodel$$={$nm:'f13',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f7 at members.ceylon (42:4-42:37)
            $$issue10C2.f7=function f7(){
                var $$issue10C2=this;
                return (12);
            };$$issue10C2.f7.$$metamodel$$={$nm:'f7',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f9 at members.ceylon (43:4-43:44)
            $$issue10C2.f9=function f9(){
                var $$issue10C2=this;
                return (14);
            };$$issue10C2.f9.$$metamodel$$={$nm:'f9',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
        })(Issue10C2.$$.prototype);
    }
    Issue10C2.$$.$$metamodel$$={$nm:'Issue10C2',$mt:'cls','super':{t:Issue10C1},'satisfies':[]};
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDefinition testIssue10 at members.ceylon (46:0-63:0)
function testIssue10(){
    
    //AttributeDeclaration obj at members.ceylon (47:4-47:28)
    var obj$3374=Issue10C2((2));
    $$$c2593.check(obj$3374.f1().equals((1)),$$$cl2592.String("Issue #10 (parameter)",21));
    $$$c2593.check(obj$3374.f11().equals((2)),$$$cl2592.String("Issue #10 (parameter)",21));
    $$$c2593.check(obj$3374.f2().equals((3)),$$$cl2592.String("Issue #10 (non-shared attribute)",32));
    $$$c2593.check(obj$3374.f12().equals((4)),$$$cl2592.String("Issue #10 (non-shared attribute)",32));
    $$$c2593.check(obj$3374.f3().equals((5)),$$$cl2592.String("Issue #10 (non-shared attribute)",32));
    $$$c2593.check(obj$3374.i2.equals((6)),$$$cl2592.String("Issue #10 (shared attribute)",28));
    $$$c2593.check(obj$3374.f4().equals((8)),$$$cl2592.String("Issue #10 (shared attribute)",28));
    $$$c2593.check(obj$3374.i3.equals((8)),$$$cl2592.String("Issue #10 (shared attribute)",28));
    $$$c2593.check(obj$3374.f6().equals((9)),$$$cl2592.String("Issue #10 (non-shared method)",29));
    $$$c2593.check(obj$3374.f13().equals((10)),$$$cl2592.String("Issue #10 (non-shared method)",29));
    $$$c2593.check(obj$3374.f8().equals((11)),$$$cl2592.String("Issue #10 (non-shared method)",29));
    $$$c2593.check(obj$3374.f7().equals((12)),$$$cl2592.String("Issue #10 (shared method)",25));
    $$$c2593.check(obj$3374.f10().equals((14)),$$$cl2592.String("Issue #10 (shared method)",25));
    $$$c2593.check(obj$3374.f9().equals((14)),$$$cl2592.String("Issue #10 (shared method)",25));
    $$$c2593.check((!obj$3374.string.empty),$$$cl2592.String("Issue #113 (inheritance)",24));
};testIssue10.$$metamodel$$={$nm:'testIssue10',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testIssue10.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//ClassDefinition AssignTest at members.ceylon (65:0-69:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDeclaration x at members.ceylon (66:4-66:33)
    $$assignTest.x$3375_=(1);
    return $$assignTest;
}
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl2592.initTypeProto(AssignTest,'members::AssignTest',$$$cl2592.Basic);
        (function($$assignTest){
            
            //AttributeDeclaration x at members.ceylon (66:4-66:33)
            $$$cl2592.defineAttr($$assignTest,'x',function(){return this.x$3375_;},function(x$3376){return this.x$3375_=x$3376;});
            
            //AttributeGetterDefinition y at members.ceylon (67:4-67:33)
            $$$cl2592.defineAttr($$assignTest,'y',function(){
                var $$assignTest=this;
                return $$assignTest.x;
            },function(y$3377){
                var $$assignTest=this;
                $$assignTest.x=y$3377;
            });
        })(AssignTest.$$.prototype);
    }
    AssignTest.$$.$$metamodel$$={$nm:'AssignTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return AssignTest;
}
exports.$init$AssignTest=$init$AssignTest;
$init$AssignTest();

//ClassDefinition Issue50 at members.ceylon (71:0-74:0)
function Issue50($$issue50){
    $init$Issue50();
    if ($$issue50===undefined)$$issue50=new Issue50.$$;
    
    //AttributeDeclaration z at members.ceylon (72:4-72:19)
    var z=$$$cl2592.String("ok",2);
    $$$cl2592.defineAttr($$issue50,'z',function(){return z;});
    return $$issue50;
}
function $init$Issue50(){
    if (Issue50.$$===undefined){
        $$$cl2592.initTypeProto(Issue50,'members::Issue50',$$$cl2592.Basic);
        (function($$issue50){
            
            //AttributeDeclaration z at members.ceylon (72:4-72:19)
        })(Issue50.$$.prototype);
    }
    Issue50.$$.$$metamodel$$={$nm:'Issue50',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Issue50;
}
exports.$init$Issue50=$init$Issue50;
$init$Issue50();

//ClassDefinition Util at members.ceylon (76:0-78:0)
function Util($$util){
    $init$Util();
    if ($$util===undefined)$$util=new Util.$$;
    
    //AttributeDeclaration s at members.ceylon (77:4-77:27)
    $$util.s$3378_=$$$cl2592.String("123",3);
    return $$util;
}
function $init$Util(){
    if (Util.$$===undefined){
        $$$cl2592.initTypeProto(Util,'members::Util',$$$cl2592.Basic);
        (function($$util){
            
            //AttributeDeclaration s at members.ceylon (77:4-77:27)
            $$$cl2592.defineAttr($$util,'s',function(){return this.s$3378_;});
        })(Util.$$.prototype);
    }
    Util.$$.$$metamodel$$={$nm:'Util',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
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
        $$$cl2592.initTypeProto(AliasMemberTest,'members::AliasMemberTest',$$$cl2592.Basic);
        (function($$aliasMemberTest){
            
            //InterfaceDefinition I1 at members.ceylon (80:4-80:55)
            function I1$AliasMemberTest($$i1$AliasMemberTest){
                $$i1$AliasMemberTest.$$outer=this;
            }
            function $init$I1$AliasMemberTest(){
                if (I1$AliasMemberTest.$$===undefined){
                    $$$cl2592.initTypeProto(I1$AliasMemberTest,'members::AliasMemberTest.I1');
                    AliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
                    (function($$i1$AliasMemberTest){
                        
                        //AttributeGetterDefinition s at members.ceylon (80:25-80:53)
                        $$$cl2592.defineAttr($$i1$AliasMemberTest,'s',function(){
                            var $$i1$AliasMemberTest=this;
                            return $$$cl2592.String("A",1);
                        });
                    })(I1$AliasMemberTest.$$.prototype);
                }
                I1$AliasMemberTest.$$.$$metamodel$$={$nm:'I1',$mt:'ifc','satisfies':[]};
                return I1$AliasMemberTest;
            }
            $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
            $init$I1$AliasMemberTest();
            $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
            
            //InterfaceDeclaration I1Alias at members.ceylon (81:4-81:34)
            $$aliasMemberTest.I1Alias$AliasMemberTest=$$aliasMemberTest.I1$AliasMemberTest;
            
            //InterfaceDefinition I2 at members.ceylon (82:4-82:48)
            function I2$3379($$i2$3379){
                $$i2$3379.$$outer=this;
            }
            function $init$I2$3379(){
                if (I2$3379.$$===undefined){
                    $$$cl2592.initTypeProto(I2$3379,'members::AliasMemberTest.I2');
                    AliasMemberTest.I2$3379=I2$3379;
                    (function($$i2$3379){
                        
                        //AttributeGetterDefinition s at members.ceylon (82:18-82:46)
                        $$$cl2592.defineAttr($$i2$3379,'s',function(){
                            var $$i2$3379=this;
                            return $$$cl2592.String("B",1);
                        });
                    })(I2$3379.$$.prototype);
                }
                I2$3379.$$.$$metamodel$$={$nm:'I2',$mt:'ifc','satisfies':[]};
                return I2$3379;
            }
            $$aliasMemberTest.$init$I2$3379=$init$I2$3379;
            $init$I2$3379();
            $$aliasMemberTest.I2$3379=I2$3379;
            
            //InterfaceDeclaration I2Alias at members.ceylon (83:4-83:27)
            $$aliasMemberTest.I2Alias$3380=$$aliasMemberTest.I2$3379;
            
            //ClassDefinition A at members.ceylon (84:4-84:40)
            function A$AliasMemberTest($$a$AliasMemberTest){
                $init$A$AliasMemberTest();
                if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new this.A$AliasMemberTest.$$;
                $$a$AliasMemberTest.$$outer=this;
                $$a$AliasMemberTest.$$outer.I1$AliasMemberTest($$a$AliasMemberTest);
                return $$a$AliasMemberTest;
            }
            function $init$A$AliasMemberTest(){
                if (A$AliasMemberTest.$$===undefined){
                    $$$cl2592.initTypeProto(A$AliasMemberTest,'members::AliasMemberTest.A',$$$cl2592.Basic,$$aliasMemberTest.$init$I1$AliasMemberTest());
                    AliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
                }
                A$AliasMemberTest.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:AliasMemberTest.I1$AliasMemberTest}]};
                return A$AliasMemberTest;
            }
            $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
            $init$A$AliasMemberTest();
            $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
            
            //ClassDefinition B at members.ceylon (85:4-85:33)
            function B$3381($$b$3381){
                $init$B$3381();
                if ($$b$3381===undefined)$$b$3381=new this.B$3381.$$;
                $$b$3381.$$outer=this;
                $$b$3381.$$outer.I2$3379($$b$3381);
                return $$b$3381;
            }
            function $init$B$3381(){
                if (B$3381.$$===undefined){
                    $$$cl2592.initTypeProto(B$3381,'members::AliasMemberTest.B',$$$cl2592.Basic,$$aliasMemberTest.$init$I2$3379());
                    AliasMemberTest.B$3381=B$3381;
                }
                B$3381.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:AliasMemberTest.I2$3379}]};
                return B$3381;
            }
            $$aliasMemberTest.$init$B$3381=$init$B$3381;
            $init$B$3381();
            $$aliasMemberTest.B$3381=B$3381;
            
            //ClassDeclaration AliasA at members.ceylon (86:4-86:32)
            $$aliasMemberTest.AliasA$AliasMemberTest=$$aliasMemberTest.A$AliasMemberTest;
            
            //ClassDeclaration AliasB at members.ceylon (87:4-87:25)
            $$aliasMemberTest.AliasB$3382=$$aliasMemberTest.B$3381;
            
            //MethodDefinition b at members.ceylon (88:4-88:43)
            $$aliasMemberTest.b=function b(){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasB$3382().s;
            };$$aliasMemberTest.b.$$metamodel$$={$nm:'b',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            //MethodDeclaration f1 at members.ceylon (90:4-90:30)
            $$aliasMemberTest.f1=function (){
                var $$aliasMemberTest=this;
                return Util();
            };
            $$aliasMemberTest.f1.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:Util},$ps:[]};
            
            //MethodDeclaration f2 at members.ceylon (91:4-91:29)
            $$aliasMemberTest.f2=function (){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasA$AliasMemberTest();
            };
            $$aliasMemberTest.f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:AliasMemberTest.A$AliasMemberTest},$ps:[]};
        })(AliasMemberTest.$$.prototype);
    }
    AliasMemberTest.$$.$$metamodel$$={$nm:'AliasMemberTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDefinition test at members.ceylon (94:0-116:0)
function test(){
    
    //AttributeDeclaration c at members.ceylon (95:4-95:24)
    var c$3383=Counter((0));
    $$$c2593.check(c$3383.count.equals((0)),$$$cl2592.String("counter 1",9));
    c$3383.inc();
    c$3383.inc();
    $$$c2593.check(c$3383.count.equals((2)),$$$cl2592.String("counter 2",9));
    $$$c2593.check(c$3383.string.equals($$$cl2592.String("Counter[2]",10)),$$$cl2592.String("counter.string",14));
    testIssue10();
    
    //AttributeDeclaration at at members.ceylon (103:4-103:27)
    var at$3384=AssignTest();
    (at$3384.x=(5));
    $$$c2593.check(at$3384.x.equals((5)),$$$cl2592.String("assign to member",16));
    (tmp$3385=at$3384,tmp$3385.y=(2),tmp$3385.y);
    var tmp$3385;
    $$$c2593.check(at$3384.y.equals((2)),$$$cl2592.String("assign using setter",19));
    $$$c2593.check(Issue50().z.equals($$$cl2592.String("ok",2)),$$$cl2592.String("Issue #50",9));
    test_outer_inner_safety();
    $$$c2593.check(AliasMemberTest().AliasA$AliasMemberTest().s.equals($$$cl2592.String("A",1)),$$$cl2592.String("shared inner alias class",24));
    $$$c2593.check(AliasMemberTest().b().equals($$$cl2592.String("B",1)),$$$cl2592.String("non-shared inner alias class",28));
    $$$c2593.check(AliasMemberTest().f1().s.equals($$$cl2592.String("123",3)),$$$cl2592.String("alias method member 1",21));
    $$$c2593.check(AliasMemberTest().f2().s.equals($$$cl2592.String("A",1)),$$$cl2592.String("alias method member 2",21));
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition test_outer_inner_safety at outer.ceylon (3:0-18:0)
function test_outer_inner_safety(){
    
    //ClassDefinition Outer at outer.ceylon (4:2-6:2)
    function Outer$3386($$outer$3386){
        $init$Outer$3386();
        if ($$outer$3386===undefined)$$outer$3386=new Outer$3386.$$;
        return $$outer$3386;
    }
    function $init$Outer$3386(){
        if (Outer$3386.$$===undefined){
            $$$cl2592.initTypeProto(Outer$3386,'members::test_outer_inner_safety.Outer',$$$cl2592.Basic);
            (function($$outer$3386){
                
                //ClassDefinition Inner at outer.ceylon (5:4-5:27)
                function Inner$Outer($$inner$Outer){
                    $init$Inner$Outer();
                    if ($$inner$Outer===undefined)$$inner$Outer=new this.Inner$Outer.$$;
                    $$inner$Outer.$$outer=this;
                    return $$inner$Outer;
                }
                function $init$Inner$Outer(){
                    if (Inner$Outer.$$===undefined){
                        $$$cl2592.initTypeProto(Inner$Outer,'members::test_outer_inner_safety.Outer.Inner',$$$cl2592.Basic);
                        Outer$3386.Inner$Outer=Inner$Outer;
                    }
                    Inner$Outer.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                    return Inner$Outer;
                }
                $$outer$3386.$init$Inner$Outer=$init$Inner$Outer;
                $init$Inner$Outer();
                $$outer$3386.Inner$Outer=Inner$Outer;
            })(Outer$3386.$$.prototype);
        }
        Outer$3386.$$.$$metamodel$$={$nm:'Outer',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return Outer$3386;
    }
    $init$Outer$3386();
    
    //AttributeDeclaration o at outer.ceylon (7:2-7:17)
    var o$3387=null;
    
    //AttributeDeclaration i1 at outer.ceylon (8:2-8:30)
    var i1$3388=(opt$3389=o$3387,$$$cl2592.JsCallable(opt$3389,opt$3389!==null?opt$3389.Inner$Outer:null))();
    var opt$3389;
    
    //MethodDeclaration cons at outer.ceylon (9:2-9:35)
    var cons$3390=function (){
        return (opt$3391=o$3387,$$$cl2592.JsCallable(opt$3391,opt$3391!==null?opt$3391.Inner$Outer:null))();
    };
    cons$3390.$$metamodel$$={$nm:'cons',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:Outer$3386.Inner$Outer}]},$ps:[]};
    var opt$3391;
    var i1$3392;
    if((i1$3392=i1$3388)!==null){
        $$$c2593.fail($$$cl2592.String("i1 should be null",17));
    }
    $$$c2593.check($$$cl2592.className($$$cl2592.$JsCallable(cons$3390,[],{Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:Outer$3386.Inner$Outer}]}})).equals($$$cl2592.String("ceylon.language::Callable",25)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("cons is Callable, ",18),$$$cl2592.className($$$cl2592.$JsCallable(cons$3390,[],{Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:Outer$3386.Inner$Outer}]}})).string]).string);
    
    //AttributeDeclaration i2 at outer.ceylon (14:2-14:30)
    var i2$3393=cons$3390();
    var i2$3394;
    if((i2$3394=i2$3393)!==null){
        $$$c2593.fail($$$cl2592.String("i2 should not exist",19));
    }
};test_outer_inner_safety.$$metamodel$$={$nm:'test_outer_inner_safety',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test_outer_inner_safety.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
