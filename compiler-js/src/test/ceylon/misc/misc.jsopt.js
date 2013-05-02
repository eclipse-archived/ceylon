(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$pt":"v","$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"Bivariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"In"},{"variance":"out","$nm":"Out"}],"$an":{"shared":[]},"$nm":"Bivariant"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"},"Container":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Outer"}],"$an":{"shared":[]},"$c":{"Member":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Inner"}],"$an":{"shared":[]},"$c":{"Child":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"InnerMost"}],"$an":{"shared":[]},"$nm":"Child"}},"$nm":"Member"}},"$nm":"Container"},"Covariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Covariant"},"runtimeMethod":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"param"}]],"$mt":"mthd","$nm":"runtimeMethod"},"Top1":{"$mt":"ifc","$an":{"shared":[]},"$nm":"Top1"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"Middle1":{"satisfies":[{"$pk":"misc","$nm":"Top1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Middle1"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"Invariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Element"}],"$an":{"shared":[]},"$nm":"Invariant"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"Contravariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Contravariant"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"testReifiedRuntime":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testReifiedRuntime"},"Bottom1":{"satisfies":[{"$pk":"misc","$nm":"Middle1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Bottom1"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"}}}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2593=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    return $$aliasingClass;
}
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl2592.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl2592.Basic);
        (function($$aliasingClass){
            
            //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
            function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
                $$aliasingIface$AliasingClass.$$outer=this;
            }
            function $init$AliasingIface$AliasingClass(){
                if (AliasingIface$AliasingClass.$$===undefined){
                    $$$cl2592.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
                    AliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
                    (function($$aliasingIface$AliasingClass){
                        
                        //MethodDefinition aliasingIface at aliases.ceylon (7:8-7:54)
                        $$aliasingIface$AliasingClass.aliasingIface=function aliasingIface(){
                            var $$aliasingIface$AliasingClass=this;
                            return true;
                        };$$aliasingIface$AliasingClass.aliasingIface.$$metamodel$$={$nm:'aliasingIface',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[]};
                    })(AliasingIface$AliasingClass.$$.prototype);
                }
                AliasingIface$AliasingClass.$$.$$metamodel$$={$nm:'AliasingIface',$mt:'ifc','satisfies':[]};
                return AliasingIface$AliasingClass;
            }
            $$aliasingClass.$init$AliasingIface$AliasingClass=$init$AliasingIface$AliasingClass;
            $init$AliasingIface$AliasingClass();
            $$aliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
            
            //ClassDefinition AliasingInner at aliases.ceylon (9:4-11:4)
            function AliasingInner$AliasingClass($$aliasingInner$AliasingClass){
                $init$AliasingInner$AliasingClass();
                if ($$aliasingInner$AliasingClass===undefined)$$aliasingInner$AliasingClass=new this.AliasingInner$AliasingClass.$$;
                $$aliasingInner$AliasingClass.$$outer=this;
                return $$aliasingInner$AliasingClass;
            }
            function $init$AliasingInner$AliasingClass(){
                if (AliasingInner$AliasingClass.$$===undefined){
                    $$$cl2592.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl2592.Basic);
                    AliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
                    (function($$aliasingInner$AliasingClass){
                        
                        //MethodDefinition aliasingInner at aliases.ceylon (10:8-10:54)
                        $$aliasingInner$AliasingClass.aliasingInner=function aliasingInner(){
                            var $$aliasingInner$AliasingClass=this;
                            return true;
                        };$$aliasingInner$AliasingClass.aliasingInner.$$metamodel$$={$nm:'aliasingInner',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[]};
                    })(AliasingInner$AliasingClass.$$.prototype);
                }
                AliasingInner$AliasingClass.$$.$$metamodel$$={$nm:'AliasingInner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return AliasingInner$AliasingClass;
            }
            $$aliasingClass.$init$AliasingInner$AliasingClass=$init$AliasingInner$AliasingClass;
            $init$AliasingInner$AliasingClass();
            $$aliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
        })(AliasingClass.$$.prototype);
    }
    AliasingClass.$$.$$metamodel$$={$nm:'AliasingClass',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return AliasingClass;
}
exports.$init$AliasingClass=$init$AliasingClass;
$init$AliasingClass();

//ClassDefinition AliasingSubclass at aliases.ceylon (14:0-22:0)
function AliasingSubclass($$aliasingSubclass){
    $init$AliasingSubclass();
    if ($$aliasingSubclass===undefined)$$aliasingSubclass=new AliasingSubclass.$$;
    AliasingClass($$aliasingSubclass);
    return $$aliasingSubclass;
}
function $init$AliasingSubclass(){
    if (AliasingSubclass.$$===undefined){
        $$$cl2592.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
        (function($$aliasingSubclass){
            
            //ClassDeclaration InnerAlias at aliases.ceylon (15:4-15:48)
            $$aliasingSubclass.InnerAlias$AliasingSubclass=$$aliasingSubclass.AliasingInner$AliasingClass;
            
            //ClassDefinition SubAlias at aliases.ceylon (16:4-16:50)
            function SubAlias$AliasingSubclass($$subAlias$AliasingSubclass){
                $init$SubAlias$AliasingSubclass();
                if ($$subAlias$AliasingSubclass===undefined)$$subAlias$AliasingSubclass=new this.SubAlias$AliasingSubclass.$$;
                $$subAlias$AliasingSubclass.$$outer=this;
                $$subAlias$AliasingSubclass.$$outer.InnerAlias$AliasingSubclass($$subAlias$AliasingSubclass);
                return $$subAlias$AliasingSubclass;
            }
            function $init$SubAlias$AliasingSubclass(){
                if (SubAlias$AliasingSubclass.$$===undefined){
                    $$$cl2592.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
                    AliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
                }
                SubAlias$AliasingSubclass.$$.$$metamodel$$={$nm:'SubAlias',$mt:'cls','super':{t:AliasingClass.AliasingInner$AliasingClass},'satisfies':[]};
                return SubAlias$AliasingSubclass;
            }
            $$aliasingSubclass.$init$SubAlias$AliasingSubclass=$init$SubAlias$AliasingSubclass;
            $init$SubAlias$AliasingSubclass();
            $$aliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
            
            //MethodDefinition aliasingSubclass at aliases.ceylon (18:4-20:4)
            $$aliasingSubclass.aliasingSubclass=function aliasingSubclass(){
                var $$aliasingSubclass=this;
                return $$aliasingSubclass.SubAlias$AliasingSubclass().aliasingInner();
            };$$aliasingSubclass.aliasingSubclass.$$metamodel$$={$nm:'aliasingSubclass',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[]};
            //InterfaceDeclaration AliasedIface at aliases.ceylon (21:4-21:50)
            $$aliasingSubclass.AliasedIface$AliasingSubclass=$$aliasingSubclass.AliasingIface$AliasingClass;
        })(AliasingSubclass.$$.prototype);
    }
    AliasingSubclass.$$.$$metamodel$$={$nm:'AliasingSubclass',$mt:'cls','super':{t:AliasingClass},'satisfies':[]};
    return AliasingSubclass;
}
exports.$init$AliasingSubclass=$init$AliasingSubclass;
$init$AliasingSubclass();

//ClassDefinition AliasingSub2 at aliases.ceylon (24:0-30:0)
function AliasingSub2($$aliasingSub2){
    $init$AliasingSub2();
    if ($$aliasingSub2===undefined)$$aliasingSub2=new AliasingSub2.$$;
    AliasingSubclass($$aliasingSub2);
    return $$aliasingSub2;
}
function $init$AliasingSub2(){
    if (AliasingSub2.$$===undefined){
        $$$cl2592.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
        (function($$aliasingSub2){
            
            //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
            $$$cl2592.defineAttr($$aliasingSub2,'iface',function(){
                var $$aliasingSub2=this;
                
                //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
                function aliased$3395(){
                    var $$aliased$3395=new aliased$3395.$$;
                    $$aliasingSub2.AliasingIface$AliasingClass($$aliased$3395);
                    return $$aliased$3395;
                }
                function $init$aliased$3395(){
                    if (aliased$3395.$$===undefined){
                        $$$cl2592.initTypeProto(aliased$3395,'misc::AliasingSub2.iface.aliased',$$$cl2592.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
                    }
                    aliased$3395.$$.$$metamodel$$={$nm:'aliased',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:AliasingClass.AliasingIface$AliasingClass}]};
                    return aliased$3395;
                }
                $init$aliased$3395();
                var aliased$3396=aliased$3395();
                var getAliased$3396=function(){
                    return aliased$3396;
                }
                return getAliased$3396();
            });
        })(AliasingSub2.$$.prototype);
    }
    AliasingSub2.$$.$$metamodel$$={$nm:'AliasingSub2',$mt:'cls','super':{t:AliasingSubclass},'satisfies':[]};
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$3397, $$targs$$,$$listleton){return $$$cl2592.Singleton(l$3397,{Element:{t:$$$cl2592.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl2592.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$3398, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl2592.Integer}};
    $$$cl2592.Sequence($$miMatrix);
    $$$cl2592.add_type_arg($$miMatrix,'Cell',{t:$$$cl2592.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    $$miMatrix.sb$3399_=$$$cl2592.SequenceBuilder({Element:{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$3400 = $$$cl2592.Range((1),gridSize$3398,{Element:{t:$$$cl2592.Integer}}).iterator();
    var i$3401;while ((i$3401=it$3400.next())!==$$$cl2592.getFinished()){
        $$miMatrix.sb$3399.append($$$cl2592.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$3402=$$$cl2592.Range((1),gridSize$3398,{Element:{t:$$$cl2592.Integer}}).iterator();
            var j$3403=$$$cl2592.getFinished();
            var next$j$3403=function(){return j$3403=it$3402.next();}
            next$j$3403();
            return function(){
                if(j$3403!==$$$cl2592.getFinished()){
                    var j$3403$3404=j$3403;
                    var tmpvar$3405=j$3403$3404;
                    next$j$3403();
                    return tmpvar$3405;
                }
                return $$$cl2592.getFinished();
            }
        },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}).sequence);
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$3406;
    if($$$cl2592.nonempty((g$3406=$$miMatrix.sb$3399.sequence))){
        var grid$3407=g$3406;
        $$$cl2592.defineAttr($$miMatrix,'grid$3407',function(){return grid$3407;});
    }else {
        var grid$3407=$$$cl2592.Tuple($$$cl2592.Tuple((1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}});
        $$$cl2592.defineAttr($$miMatrix,'grid$3407',function(){return grid$3407;});
    }
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    $$miMatrix.string$3408_=$$miMatrix.grid$3407.string;
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    $$miMatrix.hash$3409_=$$miMatrix.grid$3407.hash;
    var span=$$$cl2592.$JsCallable((opt$3410=$$miMatrix.grid$3407,$$$cl2592.JsCallable(opt$3410,opt$3410!==null?opt$3410.span:null)),[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'to',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}}}});
    $$miMatrix.span=span;
    var opt$3410;
    var segment=$$$cl2592.$JsCallable((opt$3411=$$miMatrix.grid$3407,$$$cl2592.JsCallable(opt$3411,opt$3411!==null?opt$3411.segment:null)),[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'length',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}}}});
    $$miMatrix.segment=segment;
    var opt$3411;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    $$miMatrix.reversed$3412_=$$miMatrix.grid$3407.reversed;
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    $$miMatrix.lastIndex$3413_=$$miMatrix.grid$3407.lastIndex;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    $$miMatrix.rest$3414_=$$miMatrix.grid$3407.rest;
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    $$miMatrix.first$3415_=$$miMatrix.grid$3407.first;
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl2592.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl2592.Basic,$$$cl2592.Sequence);
        (function($$miMatrix){
            
            //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
            $$$cl2592.defineAttr($$miMatrix,'sb$3399',function(){return this.sb$3399_;});
            
            //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
            
            //MethodDefinition iterator at aliases.ceylon (46:4-46:76)
            $$miMatrix.iterator=function iterator(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3407.iterator();
            };$$miMatrix.iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl2592.Iterator,a:{Element:{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}}},$ps:[]};
            //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
            $$$cl2592.defineAttr($$miMatrix,'string',function(){return this.string$3408_;});
            
            //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
            $$$cl2592.defineAttr($$miMatrix,'hash',function(){return this.hash$3409_;});
            
            //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
            $$miMatrix.equals=function (other$3416){
                var $$miMatrix=this;
                return $$miMatrix.grid$3407.equals(other$3416);
            };
            equals$$metamodel$$={$nm:'equals',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl2592.Object}}]};
            
            //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
            $$$cl2592.defineAttr($$miMatrix,'reversed',function(){return this.reversed$3412_;});
            
            //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
            $$$cl2592.defineAttr($$miMatrix,'lastIndex',function(){return this.lastIndex$3413_;});
            
            //MethodDeclaration get at aliases.ceylon (54:4-54:55)
            $$miMatrix.get=function (i$3417){
                var $$miMatrix=this;
                return $$miMatrix.grid$3407.get(i$3417);
            };
            get$$metamodel$$={$nm:'get',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}]},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            
            //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
            $$$cl2592.defineAttr($$miMatrix,'rest',function(){return this.rest$3414_;});
            
            //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
            $$$cl2592.defineAttr($$miMatrix,'first',function(){return this.first$3415_;});
            
            //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
            $$$cl2592.defineAttr($$miMatrix,'clone',function(){
                var $$miMatrix=this;
                return $$miMatrix;
            });
            
            //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
            $$$cl2592.defineAttr($$miMatrix,'size',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3407.size;
            });
            
            //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
            $$miMatrix.contains=function (other$3418){
                var $$miMatrix=this;
                return $$miMatrix.grid$3407.contains(other$3418);
            };
            contains$$metamodel$$={$nm:'contains',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl2592.Object}}]};
            $$$cl2592.defineAttr($$miMatrix,'last',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3407.last;
            });
            
            //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
            $$miMatrix.spanTo=function (to$3419){
                var $$miMatrix=this;
                return (opt$3420=(to$3419.compare((0)).equals($$$cl2592.getSmaller())?$$$cl2592.getEmpty():null),opt$3420!==null?opt$3420:$$miMatrix.span((0),to$3419));
            };
            spanTo$$metamodel$$={$nm:'spanTo',$mt:'mthd',$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            
            //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
            $$miMatrix.spanFrom=function (from$3421){
                var $$miMatrix=this;
                return $$miMatrix.span(from$3421,$$miMatrix.size);
            };
            spanFrom$$metamodel$$={$nm:'spanFrom',$mt:'mthd',$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Sequence,a:{Element:{t:$$$cl2592.Integer}}}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(MiMatrix.$$.prototype);
    }
    MiMatrix.$$.$$metamodel$$={$nm:'MiMatrix',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:$$$cl2592.Sequence,a:{Cell:{t:$$$cl2592.Integer}}}]};
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();
var opt$3420;

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl2592.print($$$cl2592.String("testing type aliases",20));
    $$$c2593.check(AliasingSubclass().aliasingSubclass(),$$$cl2592.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$3422($$innerSubalias$3422){return AliasingSubclass($$innerSubalias$3422);}
    InnerSubalias$3422.$$=AliasingSubclass.$$;
    $$$c2593.check(InnerSubalias$3422().aliasingSubclass(),$$$cl2592.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$3423(aif$3424){
        return aif$3424.aliasingIface();
    };use$3423.$$metamodel$$={$nm:'use',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'aif',$mt:'prm',$t:{t:AliasingClass.AliasingIface$AliasingClass}}]};//use$3423.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:AliasingClass.AliasingIface$AliasingClass},Element:{t:AliasingClass.AliasingIface$AliasingClass}}},Return:{t:$$$cl2592.Boolean}};
    $$$c2593.check(use$3423(AliasingSub2().iface),$$$cl2592.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$3425=(5);
    $$$c2593.check($$$cl2592.isOfType(xxxxx$3425,{t:$$$cl2592.Integer}),$$$cl2592.String("Type alias",10));
    $$$c2593.check(Listleton($$$cl2592.Tuple($$$cl2592.Tuple((1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.Tuple($$$cl2592.Tuple((2),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.Tuple($$$cl2592.Tuple((3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}).first,{T:{t:$$$cl2592.Integer}}).string.equals($$$cl2592.String("[[1]]",5)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("class alias ",12),Listleton($$$cl2592.Tuple($$$cl2592.Tuple((1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.Tuple($$$cl2592.Tuple((2),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.Tuple($$$cl2592.Tuple((3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}},First:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}}}).first,{T:{t:$$$cl2592.Integer}}).string,$$$cl2592.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2593.check(MiMatrix((2)).string.equals($$$cl2592.String("[[1, 2], [1, 2]]",16)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("interface alias ",16),MiMatrix((2)).string,$$$cl2592.String(" instead of [[1, 2], [1, 2]]",28)]).string);
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$3426=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$3427=$$$cl2592.String("XXXX",4);
    $$$c2593.check($$$cl2592.isOfType(xxxxx1$3426,{ t:'u', l:[{t:$$$cl2592.String},{t:$$$cl2592.Integer}]}),$$$cl2592.String("is String|Integer",17));
    $$$c2593.check($$$cl2592.isOfType(xxxxx2$3427,{ t:'i', l:[{t:$$$cl2592.String},{t:$$$cl2592.List,a:{Element:{t:$$$cl2592.Anything}}}]}),$$$cl2592.String("is String&List",14));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$3428=function (bits$3429){
        if(bits$3429===undefined){bits$3429=$$$cl2592.getEmpty();}
        return $$$cl2592.any(bits$3429);
    };
    cualquiera$3428$$metamodel$$={$nm:'cualquiera',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'bits',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Boolean}}}}]};
    $$$c2593.check(cualquiera$3428([true,true,true].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.true$3430}})),$$$cl2592.String("seq arg method alias",20));
};testAliasing.$$metamodel$$={$nm:'testAliasing',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testAliasing.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//ClassDefinition LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
    $$lateTestChild.parent$3431_=undefined;
    return $$lateTestChild;
}
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl2592.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl2592.Basic);
        (function($$lateTestChild){
            
            //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
            $$$cl2592.defineAttr($$lateTestChild,'parent',function(){if (this.parent$3431_===undefined)throw $$$cl2592.InitializationException($$$cl2592.String('Attempt to read unitialized attribute «parent»'));return this.parent$3431_;},function(parent$3432){if(this.parent$3431_!==undefined)throw $$$cl2592.InitializationException($$$cl2592.String('Attempt to reassign immutable attribute «parent»'));return this.parent$3431_=parent$3432;});
        })(LateTestChild.$$.prototype);
    }
    LateTestChild.$$.$$metamodel$$={$nm:'LateTestChild',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDefinition LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children$3433, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children$3433===undefined){children$3433=$$$cl2592.getEmpty();}
    
    //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
    $$lateTestParent.children$3434_=children$3433;
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$3435 = $$lateTestParent.children.iterator();
    var child$3436;while ((child$3436=it$3435.next())!==$$$cl2592.getFinished()){
        (child$3436.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl2592.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl2592.Basic);
        (function($$lateTestParent){
            
            //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
            $$$cl2592.defineAttr($$lateTestParent,'children',function(){return this.children$3434_;});
        })(LateTestParent.$$.prototype);
    }
    LateTestParent.$$.$$metamodel$$={$nm:'LateTestParent',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDefinition testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDeclaration kids at late_support.ceylon (15:4-15:51)
    var kids$3437=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl2592.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$3437);
    try{
        LateTestParent(kids$3437);
        $$$c2593.fail($$$cl2592.String("reassigning to late attribute should fail",41));
    }catch(ex$3438){
        if (ex$3438.getT$name === undefined) ex$3438=$$$cl2592.NativeException(ex$3438);
        if($$$cl2592.isOfType(ex$3438,{t:$$$cl2592.InitializationException})){
            $$$c2593.check(true);
        }
        else if($$$cl2592.isOfType(ex$3438,{t:$$$cl2592.Exception})){
            $$$c2593.fail($$$cl2592.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3438}
    }
    try{
        $$$cl2592.print(LateTestChild().parent);
        $$$c2593.fail($$$cl2592.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$3439){
        if (ex$3439.getT$name === undefined) ex$3439=$$$cl2592.NativeException(ex$3439);
        if($$$cl2592.isOfType(ex$3439,{t:$$$cl2592.InitializationException})){
            $$$c2593.check(true);
        }
        else if($$$cl2592.isOfType(ex$3439,{t:$$$cl2592.Exception})){
            $$$c2593.fail($$$cl2592.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3439}
    }
};testLate.$$metamodel$$={$nm:'testLate',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testLate.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2592.initTypeProto(X,'misc::X');
        (function($$x){
            
            //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl2592.print($$$cl2592.String("hello world",11));
            };$$x.helloWorld.$$metamodel$$={$nm:'helloWorld',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
        })(X.$$.prototype);
    }
    X.$$.$$metamodel$$={$nm:'X',$mt:'ifc','satisfies':[]};
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$3440, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    $$foo.name$3441_=name$3440;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    $$foo.counter$3442_=(0);
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    $$foo.string$3443_=$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("Foo(",4),$$foo.name.string,$$$cl2592.String(")",1)]).string;
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl2592.initTypeProto(Foo,'misc::Foo',$$$cl2592.Basic);
        (function($$foo){
            
            //AttributeDeclaration name at misc.ceylon (8:4-8:22)
            $$$cl2592.defineAttr($$foo,'name',function(){return this.name$3441_;});
            
            //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
            $$$cl2592.defineAttr($$foo,'counter$3442',function(){return this.counter$3442_;},function(counter$3444){return this.counter$3442_=counter$3444;});
            
            //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
            $$$cl2592.defineAttr($$foo,'count',function(){
                var $$foo=this;
                return $$foo.counter$3442;
            });
            //MethodDefinition inc at misc.ceylon (11:4-11:43)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.counter$3442=$$foo.counter$3442.plus((1));
            };$$foo.inc.$$metamodel$$={$nm:'inc',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
            //MethodDefinition printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl2592.print($$$cl2592.String("foo name = ",11).plus($$foo.name));
            };$$foo.printName.$$metamodel$$={$nm:'printName',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
            //AttributeDeclaration string at misc.ceylon (15:4-15:57)
            $$$cl2592.defineAttr($$foo,'string',function(){return this.string$3443_;});
        })(Foo.$$.prototype);
    }
    Foo.$$.$$metamodel$$={$nm:'Foo',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Foo;
}
exports.$init$Foo=$init$Foo;
$init$Foo();

//ClassDefinition Bar at misc.ceylon (19:0-34:0)
function Bar($$bar){
    $init$Bar();
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl2592.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl2592.initTypeProto(Bar,'misc::Bar',Foo,$init$X());
        (function($$bar){
            
            //MethodDefinition printName at misc.ceylon (20:4-24:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl2592.print($$$cl2592.String("bar name = ",11).plus($$bar.name));
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
            };$$bar.printName.$$metamodel$$={$nm:'printName',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
            //ClassDefinition Inner at misc.ceylon (25:4-31:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$outer=this;
                $$$cl2592.print($$$cl2592.String("creating inner class of :",25).plus($$inner$Bar.$$outer.name));
                return $$inner$Bar;
            }
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl2592.Basic);
                    Bar.Inner$Bar=Inner$Bar;
                    (function($$inner$Bar){
                        
                        //MethodDefinition incOuter at misc.ceylon (28:8-30:8)
                        $$inner$Bar.incOuter=function incOuter(){
                            var $$inner$Bar=this;
                            $$inner$Bar.$$outer.inc();
                        };$$inner$Bar.incOuter.$$metamodel$$={$nm:'incOuter',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
                    })(Inner$Bar.$$.prototype);
                }
                Inner$Bar.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return Inner$Bar;
            }
            $$bar.$init$Inner$Bar=$init$Inner$Bar;
            $init$Inner$Bar();
            $$bar.Inner$Bar=Inner$Bar;
        })(Bar.$$.prototype);
    }
    Bar.$$.$$metamodel$$={$nm:'Bar',$mt:'cls','super':{t:Foo},'satisfies':[{t:X}]};
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDefinition printBoth at misc.ceylon (36:0-38:0)
function printBoth(x$3445,y$3446){
    $$$cl2592.print(x$3445.plus($$$cl2592.String(", ",2)).plus(y$3446));
};printBoth.$$metamodel$$={$nm:'printBoth',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.String}},{$nm:'y',$mt:'prm',$t:{t:$$$cl2592.String}}]};//printBoth.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.Anything}};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$3447){
    f$3447();
    f$3447();
};doIt.$$metamodel$$={$nm:'doIt',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl2592.Anything}}]};//doIt.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Anything},Element:{t:$$$cl2592.Anything}}},Return:{t:$$$cl2592.Anything}};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob$3448(){
    var $$foob=new foob$3448.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$foob.name$3449_=$$$cl2592.String("Gavin",5);
    return $$foob;
}
function $init$foob$3448(){
    if (foob$3448.$$===undefined){
        $$$cl2592.initTypeProto(foob$3448,'misc::foob',$$$cl2592.Basic);
    }
    foob$3448.$$.$$metamodel$$={$nm:'foob',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return foob$3448;
}
exports.$init$foob$3448=$init$foob$3448;
$init$foob$3448();
(function($$foob){
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$$cl2592.defineAttr($$foob,'name',function(){return this.name$3449_;});
})(foob$3448.$$.prototype);
var foob$3450=foob$3448();
var getFoob=function(){
    return foob$3450;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$3451){
    if(strings$3451===undefined){strings$3451=$$$cl2592.getEmpty();}
};printAll.$$metamodel$$={$nm:'printAll',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'strings',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}]};//printAll.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}},Element:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}},Return:{t:$$$cl2592.Anything}};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$3452, $$f){return Foo(name$3452,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;
$var.$$metamodel$$={$nm:'var',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};//$var.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Integer}};

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$3453, b$3454, c$3455, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}};
    $$testObjects.a$3453=a$3453;
    $$testObjects.b$3454=b$3454;
    $$testObjects.c$3455=c$3455;
    $$$cl2592.Iterable($$testObjects);
    $$$cl2592.add_type_arg($$testObjects,'Absent',{t:$$$cl2592.Null});
    $$$cl2592.add_type_arg($$testObjects,'Element',{t:$$$cl2592.Integer});
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl2592.initTypeProto(TestObjects,'misc::TestObjects',$$$cl2592.Basic,$$$cl2592.Iterable);
        (function($$testObjects){
            
            //MethodDefinition iterator at objects.ceylon (4:2-16:2)
            $$testObjects.iterator=function iterator(){
                var $$testObjects=this;
                
                //ObjectDefinition iter at objects.ceylon (5:4-14:4)
                function iter$3456($$targs$$){
                    var $$iter$3456=new iter$3456.$$;
                    $$iter$3456.$$targs$$=$$targs$$;
                    $$$cl2592.Iterator($$iter$3456);
                    $$$cl2592.add_type_arg($$iter$3456,'Element',{t:$$$cl2592.Integer});
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$iter$3456.index$3457_=(0);
                    return $$iter$3456;
                }
                function $init$iter$3456(){
                    if (iter$3456.$$===undefined){
                        $$$cl2592.initTypeProto(iter$3456,'misc::TestObjects.iterator.iter',$$$cl2592.Basic,$$$cl2592.Iterator);
                    }
                    iter$3456.$$.$$metamodel$$={$nm:'iter',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:$$$cl2592.Iterator,a:{Element:{t:$$$cl2592.Integer}}}]};
                    return iter$3456;
                }
                $init$iter$3456();
                (function($$iter$3456){
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$$cl2592.defineAttr($$iter$3456,'index$3457',function(){return this.index$3457_;},function(index$3458){return this.index$3457_=index$3458;});
                    
                    //MethodDefinition next at objects.ceylon (7:6-13:6)
                    $$iter$3456.next=function next(){
                        var $$iter$3456=this;
                        (oldindex$3459=$$iter$3456.index$3457,$$iter$3456.index$3457=oldindex$3459.successor,oldindex$3459);
                        var oldindex$3459;
                        if($$iter$3456.index$3457.equals((1))){
                            return $$testObjects.a$3453;
                        }else {
                            if($$iter$3456.index$3457.equals((2))){
                                return $$testObjects.b$3454;
                            }else {
                                if($$iter$3456.index$3457.equals((3))){
                                    return $$testObjects.c$3455;
                                }
                            }
                        }
                        return $$$cl2592.getFinished();
                    };$$iter$3456.next.$$metamodel$$={$nm:'next',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Integer},{t:$$$cl2592.Finished}]},$ps:[]};
                })(iter$3456.$$.prototype);
                var iter$3460=iter$3456({Element:{t:$$$cl2592.Integer}});
                var getIter$3460=function(){
                    return iter$3460;
                }
                return getIter$3460();
            };$$testObjects.iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl2592.Iterator,a:{Element:{t:$$$cl2592.Integer}}},$ps:[]};
        })(TestObjects.$$.prototype);
    }
    TestObjects.$$.$$metamodel$$={$nm:'TestObjects',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}}]};
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl2592.print($$$cl2592.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:42)
    var t1$3461=TestObjects((1),(2),(3)).iterator();
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:42)
    var t2$3462=TestObjects((1),(2),(3)).iterator();
    var i$3463;
    if($$$cl2592.isOfType((i$3463=t1$3461.next()),{t:$$$cl2592.Integer})){
        $$$c2593.check(i$3463.equals((1)),$$$cl2592.String("objects 1",9));
    }
    var i$3464;
    if($$$cl2592.isOfType((i$3464=t1$3461.next()),{t:$$$cl2592.Integer})){
        $$$c2593.check(i$3464.equals((2)),$$$cl2592.String("objects 2",9));
    }
    var i$3465;
    if($$$cl2592.isOfType((i$3465=t2$3462.next()),{t:$$$cl2592.Integer})){
        $$$c2593.check(i$3465.equals((1)),$$$cl2592.String("objects 3",9));
    }
    var i$3466;
    if($$$cl2592.isOfType((i$3466=t1$3461.next()),{t:$$$cl2592.Integer})){
        $$$c2593.check(i$3466.equals((3)),$$$cl2592.String("objects 4",9));
    }
    $$$c2593.check($$$cl2592.isOfType(t1$3461.next(),{t:$$$cl2592.Finished}),$$$cl2592.String("objects 5",9));
    var i$3467;
    if($$$cl2592.isOfType((i$3467=t2$3462.next()),{t:$$$cl2592.Integer})){
        $$$c2593.check(i$3467.equals((2)),$$$cl2592.String("objects 6",9));
    }
    var i$3468;
    if($$$cl2592.isOfType((i$3468=t2$3462.next()),{t:$$$cl2592.Integer})){
        $$$c2593.check(i$3468.equals((3)),$$$cl2592.String("objects 7",9));
    }
};test_objects.$$metamodel$$={$nm:'test_objects',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test_objects.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//InterfaceDefinition Top1 at reifiedRuntime.ceylon (3:0-3:22)
function Top1($$top1){
}
exports.Top1=Top1;
function $init$Top1(){
    if (Top1.$$===undefined){
        $$$cl2592.initTypeProto(Top1,'misc::Top1');
    }
    Top1.$$.$$metamodel$$={$nm:'Top1',$mt:'ifc','satisfies':[]};
    return Top1;
}
exports.$init$Top1=$init$Top1;
$init$Top1();

//InterfaceDefinition Middle1 at reifiedRuntime.ceylon (4:0-4:40)
function Middle1($$middle1){
    Top1($$middle1);
}
exports.Middle1=Middle1;
function $init$Middle1(){
    if (Middle1.$$===undefined){
        $$$cl2592.initTypeProto(Middle1,'misc::Middle1',$init$Top1());
    }
    Middle1.$$.$$metamodel$$={$nm:'Middle1',$mt:'ifc','satisfies':[{t:Top1}]};
    return Middle1;
}
exports.$init$Middle1=$init$Middle1;
$init$Middle1();

//InterfaceDefinition Bottom1 at reifiedRuntime.ceylon (5:0-5:43)
function Bottom1($$bottom1){
    Middle1($$bottom1);
}
exports.Bottom1=Bottom1;
function $init$Bottom1(){
    if (Bottom1.$$===undefined){
        $$$cl2592.initTypeProto(Bottom1,'misc::Bottom1',$init$Middle1());
    }
    Bottom1.$$.$$metamodel$$={$nm:'Bottom1',$mt:'ifc','satisfies':[{t:Middle1}]};
    return Bottom1;
}
exports.$init$Bottom1=$init$Bottom1;
$init$Bottom1();

//ClassDefinition Invariant at reifiedRuntime.ceylon (7:0-7:34)
function Invariant($$targs$$,$$invariant){
    $init$Invariant();
    if ($$invariant===undefined)$$invariant=new Invariant.$$;
    $$$cl2592.set_type_args($$invariant,$$targs$$);
    return $$invariant;
}
exports.Invariant=Invariant;
function $init$Invariant(){
    if (Invariant.$$===undefined){
        $$$cl2592.initTypeProto(Invariant,'misc::Invariant',$$$cl2592.Basic);
    }
    Invariant.$$.$$metamodel$$={$nm:'Invariant',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Element:{}},'satisfies':[]};
    return Invariant;
}
exports.$init$Invariant=$init$Invariant;
$init$Invariant();

//ClassDefinition Covariant at reifiedRuntime.ceylon (8:0-8:38)
function Covariant($$targs$$,$$covariant){
    $init$Covariant();
    if ($$covariant===undefined)$$covariant=new Covariant.$$;
    $$$cl2592.set_type_args($$covariant,$$targs$$);
    return $$covariant;
}
exports.Covariant=Covariant;
function $init$Covariant(){
    if (Covariant.$$===undefined){
        $$$cl2592.initTypeProto(Covariant,'misc::Covariant',$$$cl2592.Basic);
    }
    Covariant.$$.$$metamodel$$={$nm:'Covariant',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Element:{'var':'out',}},'satisfies':[]};
    return Covariant;
}
exports.$init$Covariant=$init$Covariant;
$init$Covariant();

//ClassDefinition Contravariant at reifiedRuntime.ceylon (9:0-9:41)
function Contravariant($$targs$$,$$contravariant){
    $init$Contravariant();
    if ($$contravariant===undefined)$$contravariant=new Contravariant.$$;
    $$$cl2592.set_type_args($$contravariant,$$targs$$);
    return $$contravariant;
}
exports.Contravariant=Contravariant;
function $init$Contravariant(){
    if (Contravariant.$$===undefined){
        $$$cl2592.initTypeProto(Contravariant,'misc::Contravariant',$$$cl2592.Basic);
    }
    Contravariant.$$.$$metamodel$$={$nm:'Contravariant',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Element:{'var':'in',}},'satisfies':[]};
    return Contravariant;
}
exports.$init$Contravariant=$init$Contravariant;
$init$Contravariant();

//ClassDefinition Bivariant at reifiedRuntime.ceylon (10:0-10:41)
function Bivariant($$targs$$,$$bivariant){
    $init$Bivariant();
    if ($$bivariant===undefined)$$bivariant=new Bivariant.$$;
    $$$cl2592.set_type_args($$bivariant,$$targs$$);
    return $$bivariant;
}
exports.Bivariant=Bivariant;
function $init$Bivariant(){
    if (Bivariant.$$===undefined){
        $$$cl2592.initTypeProto(Bivariant,'misc::Bivariant',$$$cl2592.Basic);
    }
    Bivariant.$$.$$metamodel$$={$nm:'Bivariant',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{In:{'var':'in',},Out:{'var':'out',}},'satisfies':[]};
    return Bivariant;
}
exports.$init$Bivariant=$init$Bivariant;
$init$Bivariant();

//ClassDefinition Container at reifiedRuntime.ceylon (12:0-16:0)
function Container($$targs$$,$$container){
    $init$Container();
    if ($$container===undefined)$$container=new Container.$$;
    $$$cl2592.set_type_args($$container,$$targs$$);
    return $$container;
}
exports.Container=Container;
function $init$Container(){
    if (Container.$$===undefined){
        $$$cl2592.initTypeProto(Container,'misc::Container',$$$cl2592.Basic);
        (function($$container){
            
            //ClassDefinition Member at reifiedRuntime.ceylon (13:4-15:4)
            function Member$Container($$targs$$,$$member$Container){
                $init$Member$Container();
                if ($$member$Container===undefined)$$member$Container=new this.Member$Container.$$;
                $$$cl2592.set_type_args($$member$Container,$$targs$$);
                $$member$Container.$$outer=this;
                return $$member$Container;
            }
            function $init$Member$Container(){
                if (Member$Container.$$===undefined){
                    $$$cl2592.initTypeProto(Member$Container,'misc::Container.Member',$$$cl2592.Basic);
                    Container.Member$Container=Member$Container;
                    (function($$member$Container){
                        
                        //ClassDefinition Child at reifiedRuntime.ceylon (14:8-14:40)
                        function Child$Member$Container($$targs$$,$$child$Member$Container){
                            $init$Child$Member$Container();
                            if ($$child$Member$Container===undefined)$$child$Member$Container=new this.Child$Member$Container.$$;
                            $$$cl2592.set_type_args($$child$Member$Container,$$targs$$);
                            $$child$Member$Container.$$outer=this;
                            return $$child$Member$Container;
                        }
                        function $init$Child$Member$Container(){
                            if (Child$Member$Container.$$===undefined){
                                $$$cl2592.initTypeProto(Child$Member$Container,'misc::Container.Member.Child',$$$cl2592.Basic);
                                Container.Member$Container.Child$Member$Container=Child$Member$Container;
                            }
                            Child$Member$Container.$$.$$metamodel$$={$nm:'Child',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{InnerMost:{}},'satisfies':[]};
                            return Child$Member$Container;
                        }
                        $$member$Container.$init$Child$Member$Container=$init$Child$Member$Container;
                        $init$Child$Member$Container();
                        $$member$Container.Child$Member$Container=Child$Member$Container;
                    })(Member$Container.$$.prototype);
                }
                Member$Container.$$.$$metamodel$$={$nm:'Member',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Inner:{}},'satisfies':[]};
                return Member$Container;
            }
            $$container.$init$Member$Container=$init$Member$Container;
            $init$Member$Container();
            $$container.Member$Container=Member$Container;
        })(Container.$$.prototype);
    }
    Container.$$.$$metamodel$$={$nm:'Container',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Outer:{}},'satisfies':[]};
    return Container;
}
exports.$init$Container=$init$Container;
$init$Container();

//MethodDefinition runtimeMethod at reifiedRuntime.ceylon (18:0-20:0)
function runtimeMethod(param$3469){
    return $$$cl2592.getNothing();
};runtimeMethod.$$metamodel$$={$nm:'runtimeMethod',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'param',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};//runtimeMethod.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}};

//MethodDefinition testReifiedRuntime at reifiedRuntime.ceylon (22:0-81:0)
function testReifiedRuntime(){
    $$$cl2592.print($$$cl2592.String("Reified generics",16));
    
    //AttributeDeclaration member at reifiedRuntime.ceylon (24:4-24:57)
    var member$3470=Container({Outer:{t:$$$cl2592.String}}).Member$Container({Inner:{t:$$$cl2592.Integer}});
    $$$c2593.check($$$cl2592.isOfType(member$3470,{t:Container.Member$Container,a:{Outer:{t:$$$cl2592.String},Inner:{t:$$$cl2592.Integer}}}),$$$cl2592.String("reified runtime inner 1",23));
    $$$c2593.check((!$$$cl2592.isOfType(member$3470,{t:Container.Member$Container,a:{Outer:{t:$$$cl2592.Integer},Inner:{t:$$$cl2592.Integer}}})),$$$cl2592.String("reified runtime inner 2",23));
    $$$c2593.check((!$$$cl2592.isOfType(member$3470,{t:Container.Member$Container,a:{Outer:{t:$$$cl2592.String},Inner:{t:$$$cl2592.String}}})),$$$cl2592.String("reified runtime inner 3",23));
    
    //AttributeDeclaration member2 at reifiedRuntime.ceylon (29:4-29:77)
    var member2$3471=Container({Outer:{t:$$$cl2592.String}}).Member$Container({Inner:{t:$$$cl2592.Integer}}).Child$Member$Container({InnerMost:{t:$$$cl2592.Character}});
    $$$c2593.check($$$cl2592.isOfType(member2$3471,{t:Container.Member$Container.Child$Member$Container,a:{Outer:{t:$$$cl2592.String},Inner:{t:$$$cl2592.Integer},InnerMost:{t:$$$cl2592.Character}}}),$$$cl2592.String("reified runtime inner 4",23));
    
    //AttributeDeclaration invTop1 at reifiedRuntime.ceylon (32:4-32:38)
    var invTop1$3472=Invariant({Element:{t:Top1}});
    $$$c2593.check($$$cl2592.isOfType(invTop1$3472,{t:Invariant,a:{Element:{t:Top1}}}),$$$cl2592.String("reified runtime invariant 1",27));
    $$$c2593.check((!$$$cl2592.isOfType(invTop1$3472,{t:Invariant,a:{Element:{t:Middle1}}})),$$$cl2592.String("reified runtime invariant 2",27));
    $$$c2593.check((!$$$cl2592.isOfType(invTop1$3472,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl2592.String("reified runtime invariant 3",27));
    
    //AttributeDeclaration invMiddle1 at reifiedRuntime.ceylon (37:4-37:44)
    var invMiddle1$3473=Invariant({Element:{t:Middle1}});
    $$$c2593.check((!$$$cl2592.isOfType(invMiddle1$3473,{t:Invariant,a:{Element:{t:Top1}}})),$$$cl2592.String("reified runtime invariant 4",27));
    $$$c2593.check($$$cl2592.isOfType(invMiddle1$3473,{t:Invariant,a:{Element:{t:Middle1}}}),$$$cl2592.String("reified runtime invariant 5",27));
    $$$c2593.check((!$$$cl2592.isOfType(invMiddle1$3473,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl2592.String("reified runtime invariant 6",27));
    
    //AttributeDeclaration covMiddle1 at reifiedRuntime.ceylon (42:4-42:44)
    var covMiddle1$3474=Covariant({Element:{t:Middle1}});
    $$$c2593.check($$$cl2592.isOfType(covMiddle1$3474,{t:Covariant,a:{Element:{t:Top1}}}),$$$cl2592.String("reified runtime covariant 1",27));
    $$$c2593.check($$$cl2592.isOfType(covMiddle1$3474,{t:Covariant,a:{Element:{t:Middle1}}}),$$$cl2592.String("reified runtime covariant 2",27));
    $$$c2593.check((!$$$cl2592.isOfType(covMiddle1$3474,{t:Covariant,a:{Element:{t:Bottom1}}})),$$$cl2592.String("reified runtime covariant 3",27));
    
    //AttributeDeclaration contravMiddle1 at reifiedRuntime.ceylon (47:4-47:52)
    var contravMiddle1$3475=Contravariant({Element:{t:Middle1}});
    $$$c2593.check((!$$$cl2592.isOfType(contravMiddle1$3475,{t:Contravariant,a:{Element:{t:Top1}}})),$$$cl2592.String("reified runtime contravariant 1",31));
    $$$c2593.check($$$cl2592.isOfType(contravMiddle1$3475,{t:Contravariant,a:{Element:{t:Middle1}}}),$$$cl2592.String("reified runtime contravariant 2",31));
    $$$c2593.check($$$cl2592.isOfType(contravMiddle1$3475,{t:Contravariant,a:{Element:{t:Bottom1}}}),$$$cl2592.String("reified runtime contravariant 3",31));
    
    //AttributeDeclaration bivMiddle1 at reifiedRuntime.ceylon (52:4-52:52)
    var bivMiddle1$3476=Bivariant({Out:{t:Middle1},In:{t:Middle1}});
    $$$c2593.check((!$$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Top1},In:{t:Top1}}})),$$$cl2592.String("reified runtime bivariant 1",27));
    $$$c2593.check((!$$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Top1}}})),$$$cl2592.String("reified runtime bivariant 2",27));
    $$$c2593.check((!$$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Top1}}})),$$$cl2592.String("reified runtime bivariant 3",27));
    $$$c2593.check($$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Top1},In:{t:Middle1}}}),$$$cl2592.String("reified runtime bivariant 4",27));
    $$$c2593.check($$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Middle1}}}),$$$cl2592.String("reified runtime bivariant 5",27));
    $$$c2593.check((!$$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Middle1}}})),$$$cl2592.String("reified runtime bivariant 6",27));
    $$$c2593.check($$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Top1},In:{t:Bottom1}}}),$$$cl2592.String("reified runtime bivariant 7",27));
    $$$c2593.check($$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Bottom1}}}),$$$cl2592.String("reified runtime bivariant 8",27));
    $$$c2593.check((!$$$cl2592.isOfType(bivMiddle1$3476,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Bottom1}}})),$$$cl2592.String("reified runtime bivariant 9",27));
    
    //ClassDefinition Local at reifiedRuntime.ceylon (63:4-63:21)
    function Local$3477($$targs$$,$$local$3477){
        $init$Local$3477();
        if ($$local$3477===undefined)$$local$3477=new Local$3477.$$;
        $$$cl2592.set_type_args($$local$3477,$$targs$$);
        return $$local$3477;
    }
    function $init$Local$3477(){
        if (Local$3477.$$===undefined){
            $$$cl2592.initTypeProto(Local$3477,'misc::testReifiedRuntime.Local',$$$cl2592.Basic);
        }
        Local$3477.$$.$$metamodel$$={$nm:'Local',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{T:{}},'satisfies':[]};
        return Local$3477;
    }
    $init$Local$3477();
    
    //AttributeDeclaration localInteger at reifiedRuntime.ceylon (65:4-65:42)
    var localInteger$3478=Local$3477({T:{t:$$$cl2592.Integer}});
    $$$c2593.check($$$cl2592.isOfType(localInteger$3478,{t:Local$3477,a:{T:{t:$$$cl2592.Integer}}}),$$$cl2592.String("reified runtime local 1",23));
    
    //AttributeDeclaration m at reifiedRuntime.ceylon (68:4-68:28)
    var m$3479=$$$cl2592.$JsCallable(runtimeMethod,[],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}});
    $$$c2593.check($$$cl2592.isOfType(m$3479,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}}),$$$cl2592.String("reified runtime callable 1",26));
    $$$c2593.check((!$$$cl2592.isOfType(m$3479,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Integer}}})),$$$cl2592.String("reified runtime callable 2",26));
    $$$c2593.check((!$$$cl2592.isOfType(m$3479,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}}})),$$$cl2592.String("reified runtime callable 3",26));
    $$$c2593.check((!$$$cl2592.isOfType(m$3479,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.String}}})),$$$cl2592.String("reified runtime callable 4",26));
    
    //AttributeDeclaration m2 at reifiedRuntime.ceylon (73:4-73:34)
    var m2$3480=$$$cl2592.$JsCallable(testReifiedRuntime,[],{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}});
    $$$c2593.check($$$cl2592.isOfType(m2$3480,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}}}),$$$cl2592.String("reified runtime callable 5",26));
    $$$c2593.check((!$$$cl2592.isOfType(m2$3480,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.String}}})),$$$cl2592.String("reified runtime callable 6",26));
    $$$c2593.check((!$$$cl2592.isOfType(m2$3480,{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Anything}}})),$$$cl2592.String("reified runtime callable 7",26));
    
    //AttributeDeclaration rec1 at reifiedRuntime.ceylon (78:4-78:80)
    var rec1$3481=$$$cl2592.Singleton($$$cl2592.Entry((1),$$$cl2592.Singleton($$$cl2592.String("x",1),{Element:{t:$$$cl2592.String}}),{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.Singleton,a:{Element:{t:$$$cl2592.String}}}}),{Element:{t:$$$cl2592.Entry,a:{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.Singleton,a:{Element:{t:$$$cl2592.String}}}}}});
    $$$c2593.check($$$cl2592.isOfType(rec1$3481,{t:$$$cl2592.Singleton,a:{Element:{t:$$$cl2592.Entry,a:{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.Singleton,a:{Element:{t:$$$cl2592.String}}}}}}}),$$$cl2592.String("#188 [1]",8));
    $$$c2593.check((!$$$cl2592.isOfType(rec1$3481,{t:$$$cl2592.Singleton,a:{Element:{t:$$$cl2592.Entry,a:{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.Singleton,a:{Element:{t:$$$cl2592.Integer}}}}}}})),$$$cl2592.String("#188 [2]",8));
};testReifiedRuntime.$$metamodel$$={$nm:'testReifiedRuntime',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testReifiedRuntime.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
var $$$m3482=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-44:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$3483=$$$cl2592.String("hello",5);
    $$$cl2592.print(name$3483);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$3484=Foo($$$cl2592.String("goodbye",7));
    printBoth(name$3483,foo$3484.name);
    (y$3485=$$$cl2592.String("y",1),x$3486=$$$cl2592.String("x",1),printBoth(x$3486,y$3485));
    var y$3485,x$3486;
    foo$3484.inc();
    foo$3484.inc();
    $$$c2593.check(foo$3484.count.equals((3)),$$$cl2592.String("Foo.count",9));
    $$$c2593.check(foo$3484.string.equals($$$cl2592.String("Foo(goodbye)",12)),$$$cl2592.String("Foo.string",10));
    foo$3484.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt($$$cl2592.$JsCallable((opt$3487=foo$3484,$$$cl2592.JsCallable(opt$3487,opt$3487!==null?opt$3487.inc:null)),[],{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}}));
    var opt$3487;
    $$$c2593.check(foo$3484.count.equals((5)),$$$cl2592.String("Foo.count [2]",13));
    doIt($$$cl2592.$JsCallable(Bar,[],{Arguments:{t:$$$cl2592.Empty},Return:{t:Bar}}));
    $$$cl2592.print(getFoob().name);
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$3488(){
        var $$x$3488=new x$3488.$$;
        return $$x$3488;
    }
    function $init$x$3488(){
        if (x$3488.$$===undefined){
            $$$cl2592.initTypeProto(x$3488,'misc::test.x',$$$cl2592.Basic);
        }
        x$3488.$$.$$metamodel$$={$nm:'x',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return x$3488;
    }
    $init$x$3488();
    (function($$x$3488){
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        $$x$3488.y=function y(){
            var $$x$3488=this;
            $$$cl2592.print($$$cl2592.String("xy",2));
        };$$x$3488.y.$$metamodel$$={$nm:'y',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
    })(x$3488.$$.prototype);
    var x$3489=x$3488();
    var getX$3489=function(){
        return x$3489;
    }
    getX$3489().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$3490=Bar();
    b$3490.Inner$Bar().incOuter();
    b$3490.Inner$Bar().incOuter();
    b$3490.Inner$Bar().incOuter();
    $$$c2593.check(b$3490.count.equals((4)),$$$cl2592.String("Bar.count",9));
    printAll([$$$cl2592.String("hello",5),$$$cl2592.String("world",5)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}));
    (strings$3491=$$$cl2592.Tuple($$$cl2592.String("hello",5),$$$cl2592.Tuple($$$cl2592.String("world",5),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),printAll(strings$3491));
    var strings$3491;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$3492=$$$m3482.Counter((0));
    c$3492.inc();
    c$3492.inc();
    $$$c2593.check(c$3492.count.equals((2)),$$$cl2592.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$3493=$var();
    test_objects();
    testAliasing();
    testLate();
    testReifiedRuntime();
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
