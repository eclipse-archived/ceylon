(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"members","$mod-version":"0.1","$mod-bin":"6.0","members":{"test_outer_inner_safety":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"cons":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$pk":"members","$nm":"Inner"}]},"$mt":"mthd","$nm":"cons"}},"$c":{"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$nm":"Inner"}},"$nm":"Outer"}},"$nm":"test_outer_inner_safety"},"Issue50":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"z":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"z"}},"$nm":"Issue50"},"$pkg-shared":"1","Util":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"Util"},"Issue10C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"arg1"}],"$mt":"cls","$m":{"f6":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f6"},"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f7"},"f8":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f8"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"f9"},"f10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f10"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f4"}},"$at":{"arg1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"arg1"},"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C1"},"Issue10C2":{"super":{"$pk":"members","$nm":"Issue10C1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"arg1"}],"$mt":"cls","$m":{"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f7"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"f9"},"f12":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f12"},"f11":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f11"},"f13":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f13"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"}},"$at":{"arg1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"arg1"},"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Counter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"initCount"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"initCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"initCount"},"currentCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"currentCount"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"initialCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"initialCount"}},"$nm":"Counter"},"AliasMemberTest":{"$i":{"I2Alias":{"$mt":"ifc","$alias":{"$pk":"members","$nm":"I2"},"$nm":"I2Alias"},"I1Alias":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"members","$nm":"I1"},"$nm":"I1Alias"},"I1":{"$mt":"ifc","$an":{"shared":[]},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I1"},"I2":{"$mt":"ifc","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I2"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"b"},"f1":{"$t":{"$pk":"members","$nm":"Util"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f2":{"$t":{"$pk":"members","$nm":"A"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$c":{"AliasB":{"super":{"$pk":"members","$nm":"B"},"$mt":"cls","$alias":"1","$nm":"AliasB"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I1Alias"}],"$mt":"cls","$an":{"shared":[]},"$nm":"A"},"AliasA":{"super":{"$pk":"members","$nm":"A"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"AliasA"},"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I2Alias"}],"$mt":"cls","$nm":"B"}},"$nm":"AliasMemberTest"},"AssignTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x"}},"$nm":"AssignTest"},"testIssue10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIssue10"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//ClassDef Counter at members.ceylon (3:0-17:0)
function Counter(initCount$4933, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$4933===undefined){initCount$4933=(0);}
    $$counter.initCount$4933_=initCount$4933;
    $$$cl4138.defineAttr($$counter,'initCount$4933',function(){return this.initCount$4933_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Counter,d:['members','Counter','$at','initCount']};});
    
    //AttributeDecl currentCount at members.ceylon (4:4-4:41)
    var currentCount$4934=initCount$4933;
    $$$cl4138.defineAttr($$counter,'currentCount$4934',function(){return currentCount$4934;},function(currentCount$4935){return currentCount$4934=currentCount$4935;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Counter,$an:function(){return[$$$cl4138.variable()];},d:['members','Counter','$at','currentCount']};});
    $$counter.$prop$getCurrentCount$4934={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Counter,$an:function(){return[$$$cl4138.variable()];},d:['members','Counter','$at','currentCount']};}};
    $$counter.$prop$getCurrentCount$4934.get=function(){return currentCount$4934};
    
    //AttributeGetterDef count at members.ceylon (5:4-7:4)
    $$$cl4138.defineAttr($$counter,'count',function(){
        return currentCount$4934;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Counter,$an:function(){return[$$$cl4138.shared()];},d:['members','Counter','$at','count']};});
    
    //MethodDef inc at members.ceylon (8:4-10:4)
    function inc(){
        currentCount$4934=currentCount$4934.plus((1));
    }
    $$counter.inc=inc;
    inc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Counter,$an:function(){return[$$$cl4138.shared()];},d:['members','Counter','$m','inc']};};
    
    //AttributeGetterDef initialCount at members.ceylon (11:4-13:4)
    $$$cl4138.defineAttr($$counter,'initialCount',function(){
        return initCount$4933;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Counter,$an:function(){return[$$$cl4138.shared()];},d:['members','Counter','$at','initialCount']};});
    
    //AttributeGetterDef string at members.ceylon (14:4-16:4)
    $$$cl4138.defineAttr($$counter,'string',function(){
        return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("Counter[",8),$$counter.count.string,$$$cl4138.String("]",1)]).string;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Counter,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['members','Counter','$at','string']};});
    return $$counter;
}
Counter.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'initCount',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['members','Counter']};};
exports.Counter=Counter;
function $init$Counter(){
    if (Counter.$$===undefined){
        $$$cl4138.initTypeProto(Counter,'members::Counter',$$$cl4138.Basic);
    }
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDef Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$4936, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$4936_=arg1$4936;
    $$$cl4138.defineAttr($$issue10C1,'arg1$4936',function(){return this.arg1$4936_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','arg1']};});
    
    //AttributeDecl i1 at members.ceylon (20:4-20:18)
    var i1$4937=(3);
    $$$cl4138.defineAttr($$issue10C1,'i1$4937',function(){return i1$4937;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i1']};});
    $$issue10C1.$prop$getI1$4937={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i1']};}};
    $$issue10C1.$prop$getI1$4937.get=function(){return i1$4937};
    
    //AttributeDecl i2 at members.ceylon (21:4-21:18)
    var i2$4938=(5);
    $$$cl4138.defineAttr($$issue10C1,'i2$4938',function(){return i2$4938;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i2']};});
    $$issue10C1.$prop$getI2$4938={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i2']};}};
    $$issue10C1.$prop$getI2$4938.get=function(){return i2$4938};
    
    //AttributeDecl i3 at members.ceylon (22:4-22:33)
    var i3=(7);
    $$$cl4138.defineAttr($$issue10C1,'i3',function(){return i3;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['members','Issue10C1','$at','i3']};});
    $$issue10C1.$prop$getI3={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['members','Issue10C1','$at','i3']};}};
    $$issue10C1.$prop$getI3.get=function(){return i3};
    
    //MethodDef f1 at members.ceylon (23:4-23:39)
    function f1(){
        return arg1$4936;
    }
    $$issue10C1.f1=f1;
    f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f1']};};
    
    //MethodDef f2 at members.ceylon (24:4-24:37)
    function f2(){
        return i1$4937;
    }
    $$issue10C1.f2=f2;
    f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f2']};};
    
    //MethodDef f3 at members.ceylon (25:4-25:37)
    function f3(){
        return i2$4938;
    }
    $$issue10C1.f3=f3;
    f3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f3']};};
    
    //MethodDef f4 at members.ceylon (26:4-26:37)
    function f4(){
        return $$issue10C1.i3;
    }
    $$issue10C1.f4=f4;
    f4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f4']};};
    
    //MethodDef f5 at members.ceylon (27:4-27:29)
    function f5$4939(){
        return (9);
    };f5$4939.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,d:['members','Issue10C1','$m','f5']};};
    
    //MethodDef f6 at members.ceylon (28:4-28:39)
    function f6(){
        return f5$4939();
    }
    $$issue10C1.f6=f6;
    f6.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f6']};};
    
    //MethodDef f7 at members.ceylon (29:4-29:30)
    function f7$4940(){
        return (11);
    };f7$4940.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,d:['members','Issue10C1','$m','f7']};};
    
    //MethodDef f8 at members.ceylon (30:4-30:39)
    function f8(){
        return f7$4940();
    }
    $$issue10C1.f8=f8;
    f8.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f8']};};
    
    //MethodDef f9 at members.ceylon (31:4-31:45)
    function f9(){
        return (13);
    }
    $$issue10C1.f9=f9;
    f9.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['members','Issue10C1','$m','f9']};};
    
    //MethodDef f10 at members.ceylon (32:4-32:40)
    function f10(){
        return $$issue10C1.f9();
    }
    $$issue10C1.f10=f10;
    f10.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C1','$m','f10']};};
    return $$issue10C1;
}
Issue10C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'arg1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['members','Issue10C1']};};
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl4138.initTypeProto(Issue10C1,'members::Issue10C1',$$$cl4138.Basic);
    }
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDef Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$4941, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$4941_=arg1$4941;
    Issue10C1((1),$$issue10C2);
    $$$cl4138.defineAttr($$issue10C2,'arg1$4941',function(){return this.arg1$4941_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C2,d:['members','Issue10C2','$at','arg1']};});
    
    //AttributeDecl i1 at members.ceylon (35:4-35:18)
    var i1$4942=(4);
    $$$cl4138.defineAttr($$issue10C2,'i1$4942',function(){return i1$4942;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C2,d:['members','Issue10C2','$at','i1']};});
    $$issue10C2.$prop$getI1$4942={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C2,d:['members','Issue10C2','$at','i1']};}};
    $$issue10C2.$prop$getI1$4942.get=function(){return i1$4942};
    
    //AttributeDecl i2 at members.ceylon (36:4-36:25)
    var i2=(6);
    $$$cl4138.defineAttr($$issue10C2,'i2',function(){return i2;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C2,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C2','$at','i2']};});
    $$issue10C2.$prop$getI2={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C2,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C2','$at','i2']};}};
    $$issue10C2.$prop$getI2.get=function(){return i2};
    
    //AttributeDecl i3 at members.ceylon (37:4-37:32)
    var i3=(8);
    $$$cl4138.defineAttr($$issue10C2,'i3',function(){return i3;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue10C2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['members','Issue10C2','$at','i3']};});
    $$issue10C2.$prop$getI3.get=function(){return i3};
    
    //MethodDef f11 at members.ceylon (38:4-38:40)
    function f11(){
        return arg1$4941;
    }
    $$issue10C2.f11=f11;
    f11.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C2','$m','f11']};};
    
    //MethodDef f12 at members.ceylon (39:4-39:38)
    function f12(){
        return i1$4942;
    }
    $$issue10C2.f12=f12;
    f12.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C2','$m','f12']};};
    
    //MethodDef f5 at members.ceylon (40:4-40:30)
    function f5$4943(){
        return (10);
    };f5$4943.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C2,d:['members','Issue10C2','$m','f5']};};
    
    //MethodDef f13 at members.ceylon (41:4-41:40)
    function f13(){
        return f5$4943();
    }
    $$issue10C2.f13=f13;
    f13.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C2','$m','f13']};};
    
    //MethodDef f7 at members.ceylon (42:4-42:37)
    function f7(){
        return (12);
    }
    $$issue10C2.f7=f7;
    f7.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue10C2','$m','f7']};};
    
    //MethodDef f9 at members.ceylon (43:4-43:44)
    function f9(){
        return (14);
    }
    $$issue10C2.f9=f9;
    f9.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['members','Issue10C2','$m','f9']};};
    return $$issue10C2;
}
Issue10C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Issue10C1},$ps:[{$nm:'arg1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['members','Issue10C2']};};
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl4138.initTypeProto(Issue10C2,'members::Issue10C2',$init$Issue10C1());
    }
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDef testIssue10 at members.ceylon (46:0-63:0)
function testIssue10(){
    
    //AttributeDecl obj at members.ceylon (47:4-47:28)
    var obj$4944=Issue10C2((2));
    $$$c4139.check(obj$4944.f1().equals((1)),$$$cl4138.String("Issue #10 (parameter)",21));
    $$$c4139.check(obj$4944.f11().equals((2)),$$$cl4138.String("Issue #10 (parameter)",21));
    $$$c4139.check(obj$4944.f2().equals((3)),$$$cl4138.String("Issue #10 (non-shared attribute)",32));
    $$$c4139.check(obj$4944.f12().equals((4)),$$$cl4138.String("Issue #10 (non-shared attribute)",32));
    $$$c4139.check(obj$4944.f3().equals((5)),$$$cl4138.String("Issue #10 (non-shared attribute)",32));
    $$$c4139.check(obj$4944.i2.equals((6)),$$$cl4138.String("Issue #10 (shared attribute)",28));
    $$$c4139.check(obj$4944.f4().equals((8)),$$$cl4138.String("Issue #10 (shared attribute)",28));
    $$$c4139.check(obj$4944.i3.equals((8)),$$$cl4138.String("Issue #10 (shared attribute)",28));
    $$$c4139.check(obj$4944.f6().equals((9)),$$$cl4138.String("Issue #10 (non-shared method)",29));
    $$$c4139.check(obj$4944.f13().equals((10)),$$$cl4138.String("Issue #10 (non-shared method)",29));
    $$$c4139.check(obj$4944.f8().equals((11)),$$$cl4138.String("Issue #10 (non-shared method)",29));
    $$$c4139.check(obj$4944.f7().equals((12)),$$$cl4138.String("Issue #10 (shared method)",25));
    $$$c4139.check(obj$4944.f10().equals((14)),$$$cl4138.String("Issue #10 (shared method)",25));
    $$$c4139.check(obj$4944.f9().equals((14)),$$$cl4138.String("Issue #10 (shared method)",25));
    $$$c4139.check((!obj$4944.string.empty),$$$cl4138.String("Issue #113 (inheritance)",24));
};testIssue10.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['members','testIssue10']};};

//ClassDef AssignTest at members.ceylon (65:0-69:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDecl x at members.ceylon (66:4-66:33)
    var x=(1);
    $$$cl4138.defineAttr($$assignTest,'x',function(){return x;},function(x$4945){return x=x$4945;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:AssignTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['members','AssignTest','$at','x']};});
    $$assignTest.$prop$getX={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:AssignTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['members','AssignTest','$at','x']};}};
    $$assignTest.$prop$getX.get=function(){return x};
    
    //AttributeGetterDef y at members.ceylon (67:4-67:33)
    $$$cl4138.defineAttr($$assignTest,'y',function(){
        return $$assignTest.x;
    },function(y$4946){
        $$assignTest.x=y$4946;
    },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:AssignTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AssignTest','$at','y']};});
    return $$assignTest;
}
AssignTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['members','AssignTest']};};
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl4138.initTypeProto(AssignTest,'members::AssignTest',$$$cl4138.Basic);
    }
    return AssignTest;
}
exports.$init$AssignTest=$init$AssignTest;
$init$AssignTest();

//ClassDef Issue50 at members.ceylon (71:0-74:0)
function Issue50($$issue50){
    $init$Issue50();
    if ($$issue50===undefined)$$issue50=new Issue50.$$;
    
    //AttributeDecl z at members.ceylon (72:4-72:19)
    $$issue50.$prop$getZ={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Issue50,$an:function(){return[$$$cl4138.shared()];},d:['members','Issue50','$at','z']};}};
    var z=$$$cl4138.String("ok",2);
    $$$cl4138.defineAttr($$issue50,'z',function(){return z;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Issue50,$an:function(){return[$$$cl4138.shared(),$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual()];},d:['members','Issue50','$at','z']};});
    return $$issue50;
}
Issue50.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['members','Issue50']};};
function $init$Issue50(){
    if (Issue50.$$===undefined){
        $$$cl4138.initTypeProto(Issue50,'members::Issue50',$$$cl4138.Basic);
    }
    return Issue50;
}
exports.$init$Issue50=$init$Issue50;
$init$Issue50();

//ClassDef Util at members.ceylon (76:0-78:0)
function Util($$util){
    $init$Util();
    if ($$util===undefined)$$util=new Util.$$;
    
    //AttributeDecl s at members.ceylon (77:4-77:27)
    var s=$$$cl4138.String("123",3);
    $$$cl4138.defineAttr($$util,'s',function(){return s;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Util,$an:function(){return[$$$cl4138.shared()];},d:['members','Util','$at','s']};});
    $$util.$prop$getS={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Util,$an:function(){return[$$$cl4138.shared()];},d:['members','Util','$at','s']};}};
    $$util.$prop$getS.get=function(){return s};
    return $$util;
}
Util.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['members','Util']};};
function $init$Util(){
    if (Util.$$===undefined){
        $$$cl4138.initTypeProto(Util,'members::Util',$$$cl4138.Basic);
    }
    return Util;
}
exports.$init$Util=$init$Util;
$init$Util();

//ClassDef AliasMemberTest at members.ceylon (79:0-92:0)
function AliasMemberTest($$aliasMemberTest){
    $init$AliasMemberTest();
    if ($$aliasMemberTest===undefined)$$aliasMemberTest=new AliasMemberTest.$$;
    
    //InterfaceDef I1 at members.ceylon (80:4-80:55)
    function I1$AliasMemberTest($$i1$AliasMemberTest){
        $$i1$AliasMemberTest.$$outer=this;
        
        //AttributeGetterDef s at members.ceylon (80:25-80:53)
        $$$cl4138.defineAttr($$i1$AliasMemberTest,'s',function(){
            return $$$cl4138.String("A",1);
        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:I1$AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$i','I1','$at','s']};});
    }
    I1$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$i','I1']};};
    $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
    function $init$I1$AliasMemberTest(){
        if (I1$AliasMemberTest.$$===undefined){
            $$$cl4138.initTypeProtoI(I1$AliasMemberTest,'members::AliasMemberTest.I1');
            AliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
        }
        return I1$AliasMemberTest;
    }
    $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
    $init$I1$AliasMemberTest();
    
    //InterfaceDecl I1Alias at members.ceylon (81:4-81:34)
    function I1Alias$AliasMemberTest($$i1Alias$AliasMemberTest){$$aliasMemberTest.I1$AliasMemberTest($$i1Alias$AliasMemberTest);}
    I1Alias$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$i','I1Alias']};};
    $$aliasMemberTest.I1Alias$AliasMemberTest=I1Alias$AliasMemberTest;
    
    //InterfaceDef I2 at members.ceylon (82:4-82:48)
    function I2$AliasMemberTest($$i2$4947){
        $$i2$4947.$$outer=this;
        
        //AttributeGetterDef s at members.ceylon (82:18-82:46)
        $$$cl4138.defineAttr($$i2$4947,'s',function(){
            return $$$cl4138.String("B",1);
        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:I2$AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$i','I2','$at','s']};});
    }
    I2$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,d:['members','AliasMemberTest','$i','I2']};};
    function $init$I2$AliasMemberTest(){
        if (I2$AliasMemberTest.$$===undefined){
            $$$cl4138.initTypeProtoI(I2$AliasMemberTest,'members::AliasMemberTest.I2');
            AliasMemberTest.I2$AliasMemberTest=I2$AliasMemberTest;
        }
        return I2$AliasMemberTest;
    }
    $$aliasMemberTest.$init$I2$AliasMemberTest=$init$I2$AliasMemberTest;
    $init$I2$AliasMemberTest();
    
    //InterfaceDecl I2Alias at members.ceylon (83:4-83:27)
    function I2Alias$AliasMemberTest($$i2Alias$4948){I2$AliasMemberTest($$i2Alias$4948);}
    I2Alias$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,d:['members','AliasMemberTest','$i','I2Alias']};};
    
    //ClassDef A at members.ceylon (84:4-84:40)
    function A$AliasMemberTest($$a$AliasMemberTest){
        $init$A$AliasMemberTest();
        if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new A$AliasMemberTest.$$;
        $$a$AliasMemberTest.$$outer=this;
        $$aliasMemberTest.I1Alias$AliasMemberTest($$a$AliasMemberTest);
        return $$a$AliasMemberTest;
    }
    A$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:AliasMemberTest,satisfies:[{t:AliasMemberTest.I1Alias$AliasMemberTest}],$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$c','A']};};
    $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
    function $init$A$AliasMemberTest(){
        if (A$AliasMemberTest.$$===undefined){
            $$$cl4138.initTypeProto(A$AliasMemberTest,'members::AliasMemberTest.A',$$$cl4138.Basic,$$aliasMemberTest.$init$I1$AliasMemberTest());
            AliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
        }
        return A$AliasMemberTest;
    }
    $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
    $init$A$AliasMemberTest();
    
    //ClassDef B at members.ceylon (85:4-85:33)
    function B$AliasMemberTest($$b$4949){
        $init$B$AliasMemberTest();
        if ($$b$4949===undefined)$$b$4949=new B$AliasMemberTest.$$;
        $$b$4949.$$outer=this;
        I2Alias$AliasMemberTest($$b$4949);
        return $$b$4949;
    }
    B$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:AliasMemberTest,satisfies:[{t:AliasMemberTest.I2Alias$AliasMemberTest}],d:['members','AliasMemberTest','$c','B']};};
    function $init$B$AliasMemberTest(){
        if (B$AliasMemberTest.$$===undefined){
            $$$cl4138.initTypeProto(B$AliasMemberTest,'members::AliasMemberTest.B',$$$cl4138.Basic,$init$I2$AliasMemberTest());
            AliasMemberTest.B$AliasMemberTest=B$AliasMemberTest;
        }
        return B$AliasMemberTest;
    }
    $$aliasMemberTest.$init$B$AliasMemberTest=$init$B$AliasMemberTest;
    $init$B$AliasMemberTest();
    
    //ClassDecl AliasA at members.ceylon (86:4-86:32)
    function AliasA$AliasMemberTest($$aliasA$AliasMemberTest){return $$aliasMemberTest.A$AliasMemberTest($$aliasA$AliasMemberTest);}
    AliasA$AliasMemberTest.$$=$$aliasMemberTest.A$AliasMemberTest.$$;
    AliasA$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasMemberTest.A$AliasMemberTest},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$c','AliasA']};};
    $$aliasMemberTest.AliasA$AliasMemberTest=AliasA$AliasMemberTest;
    
    //ClassDecl AliasB at members.ceylon (87:4-87:25)
    function AliasB$AliasMemberTest($$aliasB$4950){return B$AliasMemberTest($$aliasB$4950);}
    AliasB$AliasMemberTest.$$=B$AliasMemberTest.$$;
    AliasB$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasMemberTest.B$AliasMemberTest},$ps:[],$cont:AliasMemberTest,d:['members','AliasMemberTest','$c','AliasB']};};
    
    //MethodDef b at members.ceylon (88:4-88:43)
    function b(){
        return AliasB$AliasMemberTest().s;
    }
    $$aliasMemberTest.b=b;
    b.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$m','b']};};
    
    //MethodDecl f1 at members.ceylon (90:4-90:30)
    var f1=function (){
        return Util();
    };
    f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Util},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$m','f1']};};
    $$aliasMemberTest.f1=f1;
    
    //MethodDecl f2 at members.ceylon (91:4-91:29)
    var f2=function (){
        return $$aliasMemberTest.AliasA$AliasMemberTest();
    };
    f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:AliasMemberTest.A$AliasMemberTest},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl4138.shared()];},d:['members','AliasMemberTest','$m','f2']};};
    $$aliasMemberTest.f2=f2;
    return $$aliasMemberTest;
}
AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['members','AliasMemberTest']};};
function $init$AliasMemberTest(){
    if (AliasMemberTest.$$===undefined){
        $$$cl4138.initTypeProto(AliasMemberTest,'members::AliasMemberTest',$$$cl4138.Basic);
    }
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDef test at members.ceylon (94:0-116:0)
function test(){
    
    //AttributeDecl c at members.ceylon (95:4-95:24)
    var c$4951=Counter((0));
    $$$c4139.check(c$4951.count.equals((0)),$$$cl4138.String("counter 1",9));
    c$4951.inc();
    c$4951.inc();
    $$$c4139.check(c$4951.count.equals((2)),$$$cl4138.String("counter 2",9));
    $$$c4139.check(c$4951.string.equals($$$cl4138.String("Counter[2]",10)),$$$cl4138.String("counter.string",14));
    testIssue10();
    
    //AttributeDecl at at members.ceylon (103:4-103:27)
    var at$4952=AssignTest();
    (at$4952.x=(5));
    $$$c4139.check(at$4952.x.equals((5)),$$$cl4138.String("assign to member",16));
    (tmp$4953=at$4952,tmp$4953.y=(2),tmp$4953.y);
    var tmp$4953;
    $$$c4139.check(at$4952.y.equals((2)),$$$cl4138.String("assign using setter",19));
    $$$c4139.check(Issue50().z.equals($$$cl4138.String("ok",2)),$$$cl4138.String("Issue #50",9));
    test_outer_inner_safety();
    $$$c4139.check(AliasMemberTest().AliasA$AliasMemberTest().s.equals($$$cl4138.String("A",1)),$$$cl4138.String("shared inner alias class",24));
    $$$c4139.check(AliasMemberTest().b().equals($$$cl4138.String("B",1)),$$$cl4138.String("non-shared inner alias class",28));
    $$$c4139.check(AliasMemberTest().f1().s.equals($$$cl4138.String("123",3)),$$$cl4138.String("alias method member 1",21));
    $$$c4139.check(AliasMemberTest().f2().s.equals($$$cl4138.String("A",1)),$$$cl4138.String("alias method member 2",21));
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['members','test']};};
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef test_outer_inner_safety at outer.ceylon (3:0-20:0)
function test_outer_inner_safety(){
    
    //ClassDef Outer at outer.ceylon (4:2-6:2)
    function Outer$4954($$outer$4954){
        $init$Outer$4954();
        if ($$outer$4954===undefined)$$outer$4954=new Outer$4954.$$;
        
        //ClassDef Inner at outer.ceylon (5:4-5:27)
        function Inner$Outer($$inner$Outer){
            $init$Inner$Outer();
            if ($$inner$Outer===undefined)$$inner$Outer=new Inner$Outer.$$;
            $$inner$Outer.$$outer=this;
            return $$inner$Outer;
        }
        Inner$Outer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:Outer$4954,$an:function(){return[$$$cl4138.shared()];},d:['members','test_outer_inner_safety','$c','Outer','$c','Inner']};};
        $$outer$4954.Inner$Outer=Inner$Outer;
        function $init$Inner$Outer(){
            if (Inner$Outer.$$===undefined){
                $$$cl4138.initTypeProto(Inner$Outer,'members::test_outer_inner_safety.Outer.Inner',$$$cl4138.Basic);
                Outer$4954.Inner$Outer=Inner$Outer;
            }
            return Inner$Outer;
        }
        $$outer$4954.$init$Inner$Outer=$init$Inner$Outer;
        $init$Inner$Outer();
        return $$outer$4954;
    }
    Outer$4954.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['members','test_outer_inner_safety','$c','Outer']};};
    function $init$Outer$4954(){
        if (Outer$4954.$$===undefined){
            $$$cl4138.initTypeProto(Outer$4954,'members::test_outer_inner_safety.Outer',$$$cl4138.Basic);
        }
        return Outer$4954;
    }
    $init$Outer$4954();
    
    //AttributeDecl o at outer.ceylon (7:2-7:17)
    var o$4955=null;
    
    //AttributeDecl i1 at outer.ceylon (8:2-8:30)
    var i1$4956=(opt$4957=o$4955,$$$cl4138.JsCallable(opt$4957,opt$4957!==null?opt$4957.Inner$Outer:null))();
    var opt$4957;
    
    //MethodDecl cons at outer.ceylon (9:2-9:35)
    var cons$4958=function (){
        return (opt$4959=o$4955,$$$cl4138.JsCallable(opt$4959,opt$4959!==null?opt$4959.Inner$Outer:null))();
    };
    cons$4958.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:Outer$4954.Inner$Outer}]},$ps:[],d:['members','test_outer_inner_safety','$m','cons']};};
    var opt$4959;
    var i1$4960;
    if((i1$4960=i1$4956)!==null){
        $$$c4139.fail($$$cl4138.String("i1 should be null",17));
    }
    
    //AttributeDecl cons_cname at outer.ceylon (13:2-13:36)
    var cons_cname$4961=$$$cl4138.className($$$cl4138.$JsCallable(cons$4958,[],{Arguments:{t:$$$cl4138.Empty},Return:{t:'u', l:[{t:$$$cl4138.Null},{t:Outer$4954.Inner$Outer}]}}));
    $$$c4139.check((cons_cname$4961.equals($$$cl4138.String("ceylon.language::Callable",25))||cons_cname$4961.equals($$$cl4138.String("ceylon.language::JsCallable",27))),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("cons is Callable, ",18),cons_cname$4961.string]).string);
    
    //AttributeDecl i2 at outer.ceylon (16:2-16:30)
    var i2$4962=cons$4958();
    var i2$4963;
    if((i2$4963=i2$4962)!==null){
        $$$c4139.fail($$$cl4138.String("i2 should not exist",19));
    }
};test_outer_inner_safety.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['members','test_outer_inner_safety']};};
exports.$pkg$ans$members=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}})),$$$cl4138.shared()];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
