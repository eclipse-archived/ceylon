(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"members","$mod-version":"0.1","$mod-bin":"6.0","members":{"test_outer_inner_safety":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"cons":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$pk":"members","$nm":"Inner"}]},"$mt":"mthd","$nm":"cons"}},"$c":{"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$nm":"Inner"}},"$nm":"Outer"}},"$nm":"test_outer_inner_safety"},"Issue50":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"z":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"z"}},"$nm":"Issue50"},"$pkg-shared":"1","Util":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"Util"},"Issue10C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"arg1"}],"$mt":"cls","$m":{"f6":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f6"},"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f7"},"f8":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f8"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"f9"},"f10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f10"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f4"}},"$at":{"arg1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"arg1"},"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C1"},"Issue10C2":{"super":{"$pk":"members","$nm":"Issue10C1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"arg1"}],"$mt":"cls","$m":{"f7":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f7"},"f9":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"f9"},"f12":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f12"},"f11":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f11"},"f13":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f13"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"f5"}},"$at":{"arg1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"arg1"},"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"Issue10C2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Counter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"initCount"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"initCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"initCount"},"currentCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"currentCount"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"initialCount":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"initialCount"}},"$nm":"Counter"},"AliasMemberTest":{"$i":{"I2Alias":{"$mt":"ifc","$alias":{"$pk":"members","$nm":"I2"},"$nm":"I2Alias"},"I1Alias":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"members","$nm":"I1"},"$nm":"I1Alias"},"I1":{"$mt":"ifc","$an":{"shared":[]},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I1"},"I2":{"$mt":"ifc","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[]},"$nm":"s"}},"$nm":"I2"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"b"},"f1":{"$t":{"$pk":"members","$nm":"Util"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f2":{"$t":{"$pk":"members","$nm":"A"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$c":{"AliasB":{"super":{"$pk":"members","$nm":"B"},"$mt":"cls","$alias":"1","$nm":"AliasB"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I1Alias"}],"$mt":"cls","$an":{"shared":[]},"$nm":"A"},"AliasA":{"super":{"$pk":"members","$nm":"A"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"AliasA"},"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"members","$nm":"I2Alias"}],"$mt":"cls","$nm":"B"}},"$nm":"AliasMemberTest"},"AssignTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x"}},"$nm":"AssignTest"},"testIssue10":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIssue10"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//ClassDef Counter at members.ceylon (3:0-17:0)
function Counter(initCount$820, $$counter){
    $init$Counter();
    if ($$counter===undefined)$$counter=new Counter.$$;
    if(initCount$820===undefined){initCount$820=(0);}
    $$counter.initCount$820_=initCount$820;
    
    //AttributeDecl currentCount at members.ceylon (4:4-4:41)
    $$counter.currentCount$821_=$$counter.initCount$820;
    $$counter.$prop$getCurrentCount$821={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Counter,$an:function(){return[$$$cl1.variable()];},d:['members','Counter','$at','currentCount']};}};
    $$counter.$prop$getCurrentCount$821.get=function(){return currentCount$821};
    return $$counter;
}
Counter.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'initCount',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['members','Counter']};};
exports.Counter=Counter;
function $init$Counter(){
    if (Counter.$$===undefined){
        $$$cl1.initTypeProto(Counter,'members::Counter',$$$cl1.Basic);
        (function($$counter){
            
            //AttributeDecl currentCount at members.ceylon (4:4-4:41)
            $$$cl1.defineAttr($$counter,'currentCount$821',function(){return this.currentCount$821_;},function(currentCount$822){return this.currentCount$821_=currentCount$822;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Counter,$an:function(){return[$$$cl1.variable()];},d:['members','Counter','$at','currentCount']};});
            
            //AttributeGetterDef count at members.ceylon (5:4-7:4)
            $$$cl1.defineAttr($$counter,'count',function(){
                var $$counter=this;
                return $$counter.currentCount$821;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Counter,$an:function(){return[$$$cl1.shared()];},d:['members','Counter','$at','count']};});
            //MethodDef inc at members.ceylon (8:4-10:4)
            $$counter.inc=function inc(){
                var $$counter=this;
                $$counter.currentCount$821=$$counter.currentCount$821.plus((1));
            };$$counter.inc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Counter,$an:function(){return[$$$cl1.shared()];},d:['members','Counter','$m','inc']};};
            
            //AttributeGetterDef initialCount at members.ceylon (11:4-13:4)
            $$$cl1.defineAttr($$counter,'initialCount',function(){
                var $$counter=this;
                return $$counter.initCount$820;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Counter,$an:function(){return[$$$cl1.shared()];},d:['members','Counter','$at','initialCount']};});
            //AttributeGetterDef string at members.ceylon (14:4-16:4)
            $$$cl1.defineAttr($$counter,'string',function(){
                var $$counter=this;
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Counter[",8),$$counter.count.string,$$$cl1.String("]",1)]).string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Counter,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['members','Counter','$at','string']};});$$$cl1.defineAttr($$counter,'initCount$820',function(){return this.initCount$820_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Counter,d:['members','Counter','$at','initCount']};});
        })(Counter.$$.prototype);
    }
    return Counter;
}
exports.$init$Counter=$init$Counter;
$init$Counter();

//ClassDef Issue10C1 at members.ceylon (19:0-33:0)
function Issue10C1(arg1$823, $$issue10C1){
    $init$Issue10C1();
    if ($$issue10C1===undefined)$$issue10C1=new Issue10C1.$$;
    $$issue10C1.arg1$823_=arg1$823;
    
    //AttributeDecl i1 at members.ceylon (20:4-20:18)
    $$issue10C1.i1$824_=(3);
    $$issue10C1.$prop$getI1$824={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i1']};}};
    $$issue10C1.$prop$getI1$824.get=function(){return i1$824};
    
    //AttributeDecl i2 at members.ceylon (21:4-21:18)
    $$issue10C1.i2$825_=(5);
    $$issue10C1.$prop$getI2$825={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i2']};}};
    $$issue10C1.$prop$getI2$825.get=function(){return i2$825};
    
    //AttributeDecl i3 at members.ceylon (22:4-22:33)
    $$issue10C1.i3$826_=(7);
    $$issue10C1.$prop$getI3={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['members','Issue10C1','$at','i3']};}};
    $$issue10C1.$prop$getI3.get=function(){return i3};
    return $$issue10C1;
}
Issue10C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'arg1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['members','Issue10C1']};};
function $init$Issue10C1(){
    if (Issue10C1.$$===undefined){
        $$$cl1.initTypeProto(Issue10C1,'members::Issue10C1',$$$cl1.Basic);
        (function($$issue10C1){
            
            //AttributeDecl i1 at members.ceylon (20:4-20:18)
            $$$cl1.defineAttr($$issue10C1,'i1$824',function(){return this.i1$824_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i1']};});
            
            //AttributeDecl i2 at members.ceylon (21:4-21:18)
            $$$cl1.defineAttr($$issue10C1,'i2$825',function(){return this.i2$825_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','i2']};});
            
            //AttributeDecl i3 at members.ceylon (22:4-22:33)
            $$$cl1.defineAttr($$issue10C1,'i3',function(){return this.i3$826_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['members','Issue10C1','$at','i3']};});
            
            //MethodDef f1 at members.ceylon (23:4-23:39)
            $$issue10C1.f1=function f1(){
                var $$issue10C1=this;
                return $$issue10C1.arg1$823;
            };$$issue10C1.f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f1']};};
            
            //MethodDef f2 at members.ceylon (24:4-24:37)
            $$issue10C1.f2=function f2(){
                var $$issue10C1=this;
                return $$issue10C1.i1$824;
            };$$issue10C1.f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f2']};};
            
            //MethodDef f3 at members.ceylon (25:4-25:37)
            $$issue10C1.f3=function f3(){
                var $$issue10C1=this;
                return $$issue10C1.i2$825;
            };$$issue10C1.f3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f3']};};
            
            //MethodDef f4 at members.ceylon (26:4-26:37)
            $$issue10C1.f4=function f4(){
                var $$issue10C1=this;
                return $$issue10C1.i3;
            };$$issue10C1.f4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f4']};};
            
            //MethodDef f5 at members.ceylon (27:4-27:29)
            $$issue10C1.f5$827=function f5$827(){
                var $$issue10C1=this;
                return (9);
            };$$issue10C1.f5$827.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,d:['members','Issue10C1','$m','f5']};};
            
            //MethodDef f6 at members.ceylon (28:4-28:39)
            $$issue10C1.f6=function f6(){
                var $$issue10C1=this;
                return $$issue10C1.f5$827();
            };$$issue10C1.f6.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f6']};};
            
            //MethodDef f7 at members.ceylon (29:4-29:30)
            $$issue10C1.f7$828=function f7$828(){
                var $$issue10C1=this;
                return (11);
            };$$issue10C1.f7$828.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,d:['members','Issue10C1','$m','f7']};};
            
            //MethodDef f8 at members.ceylon (30:4-30:39)
            $$issue10C1.f8=function f8(){
                var $$issue10C1=this;
                return $$issue10C1.f7$828();
            };$$issue10C1.f8.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f8']};};
            
            //MethodDef f9 at members.ceylon (31:4-31:45)
            $$issue10C1.f9=function f9(){
                var $$issue10C1=this;
                return (13);
            };$$issue10C1.f9.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['members','Issue10C1','$m','f9']};};
            
            //MethodDef f10 at members.ceylon (32:4-32:40)
            $$issue10C1.f10=function f10(){
                var $$issue10C1=this;
                return $$issue10C1.f9();
            };$$issue10C1.f10.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C1,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C1','$m','f10']};};
            $$$cl1.defineAttr($$issue10C1,'arg1$823',function(){return this.arg1$823_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C1,d:['members','Issue10C1','$at','arg1']};});
        })(Issue10C1.$$.prototype);
    }
    return Issue10C1;
}
exports.$init$Issue10C1=$init$Issue10C1;
$init$Issue10C1();

//ClassDef Issue10C2 at members.ceylon (34:0-44:0)
function Issue10C2(arg1$829, $$issue10C2){
    $init$Issue10C2();
    if ($$issue10C2===undefined)$$issue10C2=new Issue10C2.$$;
    $$issue10C2.arg1$829_=arg1$829;
    Issue10C1((1),$$issue10C2);
    
    //AttributeDecl i1 at members.ceylon (35:4-35:18)
    $$issue10C2.i1$830_=(4);
    $$issue10C2.$prop$getI1$830={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C2,d:['members','Issue10C2','$at','i1']};}};
    $$issue10C2.$prop$getI1$830.get=function(){return i1$830};
    
    //AttributeDecl i2 at members.ceylon (36:4-36:25)
    $$issue10C2.i2$831_=(6);
    $$issue10C2.$prop$getI2={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C2,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C2','$at','i2']};}};
    $$issue10C2.$prop$getI2.get=function(){return i2};
    
    //AttributeDecl i3 at members.ceylon (37:4-37:32)
    $$issue10C2.i3$832_=(8);
    $$issue10C2.$prop$getI3.get=function(){return i3};
    return $$issue10C2;
}
Issue10C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Issue10C1},$ps:[{$nm:'arg1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['members','Issue10C2']};};
function $init$Issue10C2(){
    if (Issue10C2.$$===undefined){
        $$$cl1.initTypeProto(Issue10C2,'members::Issue10C2',$init$Issue10C1());
        (function($$issue10C2){
            
            //AttributeDecl i1 at members.ceylon (35:4-35:18)
            $$$cl1.defineAttr($$issue10C2,'i1$830',function(){return this.i1$830_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C2,d:['members','Issue10C2','$at','i1']};});
            
            //AttributeDecl i2 at members.ceylon (36:4-36:25)
            $$$cl1.defineAttr($$issue10C2,'i2',function(){return this.i2$831_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C2,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C2','$at','i2']};});
            
            //AttributeDecl i3 at members.ceylon (37:4-37:32)
            $$$cl1.defineAttr($$issue10C2,'i3',function(){return this.i3$832_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['members','Issue10C2','$at','i3']};});
            
            //MethodDef f11 at members.ceylon (38:4-38:40)
            $$issue10C2.f11=function f11(){
                var $$issue10C2=this;
                return $$issue10C2.arg1$829;
            };$$issue10C2.f11.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C2','$m','f11']};};
            
            //MethodDef f12 at members.ceylon (39:4-39:38)
            $$issue10C2.f12=function f12(){
                var $$issue10C2=this;
                return $$issue10C2.i1$830;
            };$$issue10C2.f12.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C2','$m','f12']};};
            
            //MethodDef f5 at members.ceylon (40:4-40:30)
            $$issue10C2.f5$833=function f5$833(){
                var $$issue10C2=this;
                return (10);
            };$$issue10C2.f5$833.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C2,d:['members','Issue10C2','$m','f5']};};
            
            //MethodDef f13 at members.ceylon (41:4-41:40)
            $$issue10C2.f13=function f13(){
                var $$issue10C2=this;
                return $$issue10C2.f5$833();
            };$$issue10C2.f13.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C2','$m','f13']};};
            
            //MethodDef f7 at members.ceylon (42:4-42:37)
            $$issue10C2.f7=function f7(){
                var $$issue10C2=this;
                return (12);
            };$$issue10C2.f7.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl1.shared()];},d:['members','Issue10C2','$m','f7']};};
            
            //MethodDef f9 at members.ceylon (43:4-43:44)
            $$issue10C2.f9=function f9(){
                var $$issue10C2=this;
                return (14);
            };$$issue10C2.f9.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:Issue10C2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['members','Issue10C2','$m','f9']};};
            $$$cl1.defineAttr($$issue10C2,'arg1$829',function(){return this.arg1$829_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue10C2,d:['members','Issue10C2','$at','arg1']};});
        })(Issue10C2.$$.prototype);
    }
    return Issue10C2;
}
exports.$init$Issue10C2=$init$Issue10C2;
$init$Issue10C2();

//MethodDef testIssue10 at members.ceylon (46:0-63:0)
function testIssue10(){
    
    //AttributeDecl obj at members.ceylon (47:4-47:28)
    var obj$834=Issue10C2((2));
    $$$c2.check(obj$834.f1().equals((1)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$c2.check(obj$834.f11().equals((2)),$$$cl1.String("Issue #10 (parameter)",21));
    $$$c2.check(obj$834.f2().equals((3)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$834.f12().equals((4)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$834.f3().equals((5)),$$$cl1.String("Issue #10 (non-shared attribute)",32));
    $$$c2.check(obj$834.i2.equals((6)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$834.f4().equals((8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$834.i3.equals((8)),$$$cl1.String("Issue #10 (shared attribute)",28));
    $$$c2.check(obj$834.f6().equals((9)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$834.f13().equals((10)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$834.f8().equals((11)),$$$cl1.String("Issue #10 (non-shared method)",29));
    $$$c2.check(obj$834.f7().equals((12)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check(obj$834.f10().equals((14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check(obj$834.f9().equals((14)),$$$cl1.String("Issue #10 (shared method)",25));
    $$$c2.check((!obj$834.string.empty),$$$cl1.String("Issue #113 (inheritance)",24));
};testIssue10.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['members','testIssue10']};};

//ClassDef AssignTest at members.ceylon (65:0-69:0)
function AssignTest($$assignTest){
    $init$AssignTest();
    if ($$assignTest===undefined)$$assignTest=new AssignTest.$$;
    
    //AttributeDecl x at members.ceylon (66:4-66:33)
    $$assignTest.x$835_=(1);
    $$assignTest.$prop$getX={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:AssignTest,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['members','AssignTest','$at','x']};}};
    $$assignTest.$prop$getX.get=function(){return x};
    return $$assignTest;
}
AssignTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['members','AssignTest']};};
function $init$AssignTest(){
    if (AssignTest.$$===undefined){
        $$$cl1.initTypeProto(AssignTest,'members::AssignTest',$$$cl1.Basic);
        (function($$assignTest){
            
            //AttributeDecl x at members.ceylon (66:4-66:33)
            $$$cl1.defineAttr($$assignTest,'x',function(){return this.x$835_;},function(x$836){return this.x$835_=x$836;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:AssignTest,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['members','AssignTest','$at','x']};});
            
            //AttributeGetterDef y at members.ceylon (67:4-67:33)
            $$$cl1.defineAttr($$assignTest,'y',function(){
                var $$assignTest=this;
                return $$assignTest.x;
            },function(y$837){
                var $$assignTest=this;
                $$assignTest.x=y$837;
            },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:AssignTest,$an:function(){return[$$$cl1.shared()];},d:['members','AssignTest','$at','y']};});
        })(AssignTest.$$.prototype);
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
    $$issue50.$prop$getZ={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Issue50,$an:function(){return[$$$cl1.shared()];},d:['members','Issue50','$at','z']};}};
    var z=$$$cl1.String("ok",2);
    $$$cl1.defineAttr($$issue50,'z',function(){return z;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Issue50,$an:function(){return[$$$cl1.shared(),$$$cl1.shared(),$$$cl1.actual()];},d:['members','Issue50','$at','z']};});
    return $$issue50;
}
Issue50.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['members','Issue50']};};
function $init$Issue50(){
    if (Issue50.$$===undefined){
        $$$cl1.initTypeProto(Issue50,'members::Issue50',$$$cl1.Basic);
        (function($$issue50){
            
            //AttributeDecl z at members.ceylon (72:4-72:19)
        })(Issue50.$$.prototype);
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
    $$util.s$838_=$$$cl1.String("123",3);
    $$util.$prop$getS={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Util,$an:function(){return[$$$cl1.shared()];},d:['members','Util','$at','s']};}};
    $$util.$prop$getS.get=function(){return s};
    return $$util;
}
Util.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['members','Util']};};
function $init$Util(){
    if (Util.$$===undefined){
        $$$cl1.initTypeProto(Util,'members::Util',$$$cl1.Basic);
        (function($$util){
            
            //AttributeDecl s at members.ceylon (77:4-77:27)
            $$$cl1.defineAttr($$util,'s',function(){return this.s$838_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Util,$an:function(){return[$$$cl1.shared()];},d:['members','Util','$at','s']};});
        })(Util.$$.prototype);
    }
    return Util;
}
exports.$init$Util=$init$Util;
$init$Util();

//ClassDef AliasMemberTest at members.ceylon (79:0-92:0)
function AliasMemberTest($$aliasMemberTest){
    $init$AliasMemberTest();
    if ($$aliasMemberTest===undefined)$$aliasMemberTest=new AliasMemberTest.$$;
    return $$aliasMemberTest;
}
AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['members','AliasMemberTest']};};
function $init$AliasMemberTest(){
    if (AliasMemberTest.$$===undefined){
        $$$cl1.initTypeProto(AliasMemberTest,'members::AliasMemberTest',$$$cl1.Basic);
        (function($$aliasMemberTest){
            
            //InterfaceDef I1 at members.ceylon (80:4-80:55)
            function I1$AliasMemberTest($$i1$AliasMemberTest){
                $$i1$AliasMemberTest.$$outer=this;
            }
            I1$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$i','I1']};};
            function $init$I1$AliasMemberTest(){
                if (I1$AliasMemberTest.$$===undefined){
                    $$$cl1.initTypeProtoI(I1$AliasMemberTest,'members::AliasMemberTest.I1');
                    AliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
                    (function($$i1$AliasMemberTest){
                        
                        //AttributeGetterDef s at members.ceylon (80:25-80:53)
                        $$$cl1.defineAttr($$i1$AliasMemberTest,'s',function(){
                            var $$i1$AliasMemberTest=this;
                            return $$$cl1.String("A",1);
                        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:I1$AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$i','I1','$at','s']};});
                    })(I1$AliasMemberTest.$$.prototype);
                }
                return I1$AliasMemberTest;
            }
            $$aliasMemberTest.$init$I1$AliasMemberTest=$init$I1$AliasMemberTest;
            $init$I1$AliasMemberTest();
            $$aliasMemberTest.I1$AliasMemberTest=I1$AliasMemberTest;
            
            //InterfaceDecl I1Alias at members.ceylon (81:4-81:34)
            function I1Alias$AliasMemberTest($$i1Alias$AliasMemberTest){$$aliasMemberTest.I1$AliasMemberTest($$i1Alias$AliasMemberTest);}
            I1Alias$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$i','I1Alias']};};
            $$aliasMemberTest.I1Alias$AliasMemberTest=I1Alias$AliasMemberTest;
            
            //InterfaceDef I2 at members.ceylon (82:4-82:48)
            function I2$AliasMemberTest($$i2$839){
                $$i2$839.$$outer=this;
            }
            I2$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,d:['members','AliasMemberTest','$i','I2']};};
            function $init$I2$AliasMemberTest(){
                if (I2$AliasMemberTest.$$===undefined){
                    $$$cl1.initTypeProtoI(I2$AliasMemberTest,'members::AliasMemberTest.I2');
                    AliasMemberTest.I2$AliasMemberTest=I2$AliasMemberTest;
                    (function($$i2$839){
                        
                        //AttributeGetterDef s at members.ceylon (82:18-82:46)
                        $$$cl1.defineAttr($$i2$839,'s',function(){
                            var $$i2$839=this;
                            return $$$cl1.String("B",1);
                        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:I2$AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$i','I2','$at','s']};});
                    })(I2$AliasMemberTest.$$.prototype);
                }
                return I2$AliasMemberTest;
            }
            $$aliasMemberTest.$init$I2$AliasMemberTest=$init$I2$AliasMemberTest;
            $init$I2$AliasMemberTest();
            $$aliasMemberTest.I2$AliasMemberTest=I2$AliasMemberTest;
            
            //InterfaceDecl I2Alias at members.ceylon (83:4-83:27)
            function I2Alias$AliasMemberTest($$i2Alias$840){$$aliasMemberTest.I2$AliasMemberTest($$i2Alias$840);}
            I2Alias$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasMemberTest,d:['members','AliasMemberTest','$i','I2Alias']};};
            $$aliasMemberTest.I2Alias$AliasMemberTest=I2Alias$AliasMemberTest;
            
            //ClassDef A at members.ceylon (84:4-84:40)
            function A$AliasMemberTest($$a$AliasMemberTest){
                $init$A$AliasMemberTest();
                if ($$a$AliasMemberTest===undefined)$$a$AliasMemberTest=new this.A$AliasMemberTest.$$;
                $$a$AliasMemberTest.$$outer=this;
                $$a$AliasMemberTest.$$outer.I1Alias$AliasMemberTest($$a$AliasMemberTest);
                return $$a$AliasMemberTest;
            }
            A$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:AliasMemberTest,satisfies:[{t:AliasMemberTest.I1Alias$AliasMemberTest}],$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$c','A']};};
            function $init$A$AliasMemberTest(){
                if (A$AliasMemberTest.$$===undefined){
                    $$$cl1.initTypeProto(A$AliasMemberTest,'members::AliasMemberTest.A',$$$cl1.Basic,$$aliasMemberTest.$init$I1$AliasMemberTest());
                    AliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
                }
                return A$AliasMemberTest;
            }
            $$aliasMemberTest.$init$A$AliasMemberTest=$init$A$AliasMemberTest;
            $init$A$AliasMemberTest();
            $$aliasMemberTest.A$AliasMemberTest=A$AliasMemberTest;
            
            //ClassDef B at members.ceylon (85:4-85:33)
            function B$AliasMemberTest($$b$841){
                $init$B$AliasMemberTest();
                if ($$b$841===undefined)$$b$841=new this.B$AliasMemberTest.$$;
                $$b$841.$$outer=this;
                $$b$841.$$outer.I2Alias$AliasMemberTest($$b$841);
                return $$b$841;
            }
            B$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:AliasMemberTest,satisfies:[{t:AliasMemberTest.I2Alias$AliasMemberTest}],d:['members','AliasMemberTest','$c','B']};};
            function $init$B$AliasMemberTest(){
                if (B$AliasMemberTest.$$===undefined){
                    $$$cl1.initTypeProto(B$AliasMemberTest,'members::AliasMemberTest.B',$$$cl1.Basic,$$aliasMemberTest.$init$I2$AliasMemberTest());
                    AliasMemberTest.B$AliasMemberTest=B$AliasMemberTest;
                }
                return B$AliasMemberTest;
            }
            $$aliasMemberTest.$init$B$AliasMemberTest=$init$B$AliasMemberTest;
            $init$B$AliasMemberTest();
            $$aliasMemberTest.B$AliasMemberTest=B$AliasMemberTest;
            
            //ClassDecl AliasA at members.ceylon (86:4-86:32)
            function AliasA$AliasMemberTest($$aliasA$AliasMemberTest){return $$aliasMemberTest.A$AliasMemberTest($$aliasA$AliasMemberTest);}
            AliasA$AliasMemberTest.$$=$$aliasMemberTest.A$AliasMemberTest.$$;
            AliasA$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasMemberTest.A$AliasMemberTest},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$c','AliasA']};};
            $$aliasMemberTest.AliasA$AliasMemberTest=AliasA$AliasMemberTest;
            
            //ClassDecl AliasB at members.ceylon (87:4-87:25)
            function AliasB$AliasMemberTest($$aliasB$842){return $$aliasMemberTest.B$AliasMemberTest($$aliasB$842);}
            AliasB$AliasMemberTest.$$=$$aliasMemberTest.B$AliasMemberTest.$$;
            AliasB$AliasMemberTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasMemberTest.B$AliasMemberTest},$ps:[],$cont:AliasMemberTest,d:['members','AliasMemberTest','$c','AliasB']};};
            $$aliasMemberTest.AliasB$AliasMemberTest=AliasB$AliasMemberTest;
            
            //MethodDef b at members.ceylon (88:4-88:43)
            $$aliasMemberTest.b=function b(){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasB$AliasMemberTest().s;
            };$$aliasMemberTest.b.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$m','b']};};
            
            //MethodDecl f1 at members.ceylon (90:4-90:30)
            $$aliasMemberTest.f1=function (){
                var $$aliasMemberTest=this;
                return Util();
            };
            $$aliasMemberTest.f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:Util},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$m','f1']};};
            
            //MethodDecl f2 at members.ceylon (91:4-91:29)
            $$aliasMemberTest.f2=function (){
                var $$aliasMemberTest=this;
                return $$aliasMemberTest.AliasA$AliasMemberTest();
            };
            $$aliasMemberTest.f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:AliasMemberTest.A$AliasMemberTest},$ps:[],$cont:AliasMemberTest,$an:function(){return[$$$cl1.shared()];},d:['members','AliasMemberTest','$m','f2']};};
        })(AliasMemberTest.$$.prototype);
    }
    return AliasMemberTest;
}
exports.$init$AliasMemberTest=$init$AliasMemberTest;
$init$AliasMemberTest();

//MethodDef test at members.ceylon (94:0-116:0)
function test(){
    
    //AttributeDecl c at members.ceylon (95:4-95:24)
    var c$843=Counter((0));
    $$$c2.check(c$843.count.equals((0)),$$$cl1.String("counter 1",9));
    c$843.inc();
    c$843.inc();
    $$$c2.check(c$843.count.equals((2)),$$$cl1.String("counter 2",9));
    $$$c2.check(c$843.string.equals($$$cl1.String("Counter[2]",10)),$$$cl1.String("counter.string",14));
    testIssue10();
    
    //AttributeDecl at at members.ceylon (103:4-103:27)
    var at$844=AssignTest();
    (at$844.x=(5));
    $$$c2.check(at$844.x.equals((5)),$$$cl1.String("assign to member",16));
    (tmp$845=at$844,tmp$845.y=(2),tmp$845.y);
    var tmp$845;
    $$$c2.check(at$844.y.equals((2)),$$$cl1.String("assign using setter",19));
    $$$c2.check(Issue50().z.equals($$$cl1.String("ok",2)),$$$cl1.String("Issue #50",9));
    test_outer_inner_safety();
    $$$c2.check(AliasMemberTest().AliasA$AliasMemberTest().s.equals($$$cl1.String("A",1)),$$$cl1.String("shared inner alias class",24));
    $$$c2.check(AliasMemberTest().b().equals($$$cl1.String("B",1)),$$$cl1.String("non-shared inner alias class",28));
    $$$c2.check(AliasMemberTest().f1().s.equals($$$cl1.String("123",3)),$$$cl1.String("alias method member 1",21));
    $$$c2.check(AliasMemberTest().f2().s.equals($$$cl1.String("A",1)),$$$cl1.String("alias method member 2",21));
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['members','test']};};
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef test_outer_inner_safety at outer.ceylon (3:0-20:0)
function test_outer_inner_safety(){
    
    //ClassDef Outer at outer.ceylon (4:2-6:2)
    function Outer$846($$outer$846){
        $init$Outer$846();
        if ($$outer$846===undefined)$$outer$846=new Outer$846.$$;
        return $$outer$846;
    }
    Outer$846.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['members','test_outer_inner_safety','$c','Outer']};};
    function $init$Outer$846(){
        if (Outer$846.$$===undefined){
            $$$cl1.initTypeProto(Outer$846,'members::test_outer_inner_safety.Outer',$$$cl1.Basic);
            (function($$outer$846){
                
                //ClassDef Inner at outer.ceylon (5:4-5:27)
                function Inner$Outer($$inner$Outer){
                    $init$Inner$Outer();
                    if ($$inner$Outer===undefined)$$inner$Outer=new this.Inner$Outer.$$;
                    $$inner$Outer.$$outer=this;
                    return $$inner$Outer;
                }
                Inner$Outer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:Outer$846,$an:function(){return[$$$cl1.shared()];},d:['members','test_outer_inner_safety','$c','Outer','$c','Inner']};};
                function $init$Inner$Outer(){
                    if (Inner$Outer.$$===undefined){
                        $$$cl1.initTypeProto(Inner$Outer,'members::test_outer_inner_safety.Outer.Inner',$$$cl1.Basic);
                        Outer$846.Inner$Outer=Inner$Outer;
                    }
                    return Inner$Outer;
                }
                $$outer$846.$init$Inner$Outer=$init$Inner$Outer;
                $init$Inner$Outer();
                $$outer$846.Inner$Outer=Inner$Outer;
            })(Outer$846.$$.prototype);
        }
        return Outer$846;
    }
    $init$Outer$846();
    
    //AttributeDecl o at outer.ceylon (7:2-7:17)
    var o$847=null;
    
    //AttributeDecl i1 at outer.ceylon (8:2-8:30)
    var i1$848=(opt$849=o$847,$$$cl1.JsCallable(opt$849,opt$849!==null?opt$849.Inner$Outer:null))();
    var opt$849;
    
    //MethodDecl cons at outer.ceylon (9:2-9:35)
    var cons$850=function (){
        return (opt$851=o$847,$$$cl1.JsCallable(opt$851,opt$851!==null?opt$851.Inner$Outer:null))();
    };
    cons$850.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:Outer$846.Inner$Outer}]},$ps:[],d:['members','test_outer_inner_safety','$m','cons']};};
    var opt$851;
    var i1$852;
    if((i1$852=i1$848)!==null){
        $$$c2.fail($$$cl1.String("i1 should be null",17));
    }
    
    //AttributeDecl cons_cname at outer.ceylon (13:2-13:36)
    var cons_cname$853=$$$cl1.className($$$cl1.$JsCallable(cons$850,[],{Arguments:{t:$$$cl1.Empty},Return:{t:'u', l:[{t:$$$cl1.Null},{t:Outer$846.Inner$Outer}]}}));
    $$$c2.check((cons_cname$853.equals($$$cl1.String("ceylon.language::Callable",25))||cons_cname$853.equals($$$cl1.String("ceylon.language::JsCallable",27))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("cons is Callable, ",18),cons_cname$853.string]).string);
    
    //AttributeDecl i2 at outer.ceylon (16:2-16:30)
    var i2$854=cons$850();
    var i2$855;
    if((i2$855=i2$854)!==null){
        $$$c2.fail($$$cl1.String("i2 should not exist",19));
    }
};test_outer_inner_safety.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['members','test_outer_inner_safety']};};
exports.$pkg$ans$members=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}})),$$$cl1.shared()];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
