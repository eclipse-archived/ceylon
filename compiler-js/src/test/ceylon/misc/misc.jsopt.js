(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$pt":"v","$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$at":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"}}};
var $$$cl2328=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2329=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    return $$aliasingClass;
}
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl2328.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl2328.Basic);
        (function($$aliasingClass){
            
            //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
            function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
                $$aliasingIface$AliasingClass.$$aliasingClass=this;
            }
            function $init$AliasingIface$AliasingClass(){
                if (AliasingIface$AliasingClass.$$===undefined){
                    $$$cl2328.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
                    (function($$aliasingIface$AliasingClass){
                        
                        //MethodDefinition aliasingIface at aliases.ceylon (7:8-7:54)
                        $$aliasingIface$AliasingClass.aliasingIface=function aliasingIface(){
                            var $$aliasingIface$AliasingClass=this;
                            return true;
                        };
                    })(AliasingIface$AliasingClass.$$.prototype);
                }
                return AliasingIface$AliasingClass;
            }
            $$aliasingClass.$init$AliasingIface$AliasingClass=$init$AliasingIface$AliasingClass;
            $init$AliasingIface$AliasingClass();
            $$aliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
            
            //ClassDefinition AliasingInner at aliases.ceylon (9:4-11:4)
            function AliasingInner$AliasingClass($$aliasingInner$AliasingClass){
                $init$AliasingInner$AliasingClass();
                if ($$aliasingInner$AliasingClass===undefined)$$aliasingInner$AliasingClass=new this.AliasingInner$AliasingClass.$$;
                $$aliasingInner$AliasingClass.$$aliasingClass=this;
                return $$aliasingInner$AliasingClass;
            }
            function $init$AliasingInner$AliasingClass(){
                if (AliasingInner$AliasingClass.$$===undefined){
                    $$$cl2328.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl2328.Basic);
                    (function($$aliasingInner$AliasingClass){
                        
                        //MethodDefinition aliasingInner at aliases.ceylon (10:8-10:54)
                        $$aliasingInner$AliasingClass.aliasingInner=function aliasingInner(){
                            var $$aliasingInner$AliasingClass=this;
                            return true;
                        };
                    })(AliasingInner$AliasingClass.$$.prototype);
                }
                return AliasingInner$AliasingClass;
            }
            $$aliasingClass.$init$AliasingInner$AliasingClass=$init$AliasingInner$AliasingClass;
            $init$AliasingInner$AliasingClass();
            $$aliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
        })(AliasingClass.$$.prototype);
    }
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
        $$$cl2328.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
        (function($$aliasingSubclass){
            
            //ClassDeclaration InnerAlias at aliases.ceylon (15:4-15:48)
            $$aliasingSubclass.InnerAlias$AliasingSubclass=$$aliasingSubclass.AliasingInner$AliasingClass;
            
            //ClassDefinition SubAlias at aliases.ceylon (16:4-16:50)
            function SubAlias$AliasingSubclass($$subAlias$AliasingSubclass){
                $init$SubAlias$AliasingSubclass();
                if ($$subAlias$AliasingSubclass===undefined)$$subAlias$AliasingSubclass=new this.SubAlias$AliasingSubclass.$$;
                $$subAlias$AliasingSubclass.$$aliasingSubclass=this;
                $$subAlias$AliasingSubclass.$$aliasingSubclass.InnerAlias$AliasingSubclass($$subAlias$AliasingSubclass);
                return $$subAlias$AliasingSubclass;
            }
            function $init$SubAlias$AliasingSubclass(){
                if (SubAlias$AliasingSubclass.$$===undefined){
                    $$$cl2328.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
                }
                return SubAlias$AliasingSubclass;
            }
            $$aliasingSubclass.$init$SubAlias$AliasingSubclass=$init$SubAlias$AliasingSubclass;
            $init$SubAlias$AliasingSubclass();
            $$aliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
            
            //MethodDefinition aliasingSubclass at aliases.ceylon (18:4-20:4)
            $$aliasingSubclass.aliasingSubclass=function aliasingSubclass(){
                var $$aliasingSubclass=this;
                return $$aliasingSubclass.SubAlias$AliasingSubclass().aliasingInner();
            };
            //InterfaceDeclaration AliasedIface at aliases.ceylon (21:4-21:50)
            $$aliasingSubclass.AliasedIface$AliasingSubclass=$$aliasingSubclass.AliasingIface$AliasingClass;
        })(AliasingSubclass.$$.prototype);
    }
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
        $$$cl2328.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
        (function($$aliasingSub2){
            
            //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
            $$$cl2328.defineAttr($$aliasingSub2,'iface',function(){
                var $$aliasingSub2=this;
                
                //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
                function aliased$3126(){
                    var $$aliased$3126=new aliased$3126.$$;
                    $$aliasingSub2.AliasingIface$AliasingClass($$aliased$3126);
                    return $$aliased$3126;
                }
                function $init$aliased$3126(){
                    if (aliased$3126.$$===undefined){
                        $$$cl2328.initTypeProto(aliased$3126,'misc::AliasingSub2.iface.aliased',$$$cl2328.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
                    }
                    return aliased$3126;
                }
                $init$aliased$3126();
                var aliased$3127=aliased$3126();
                var getAliased$3127=function(){
                    return aliased$3127;
                }
                return getAliased$3127();
            });
        })(AliasingSub2.$$.prototype);
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$3128, $$targs$$,$$listleton){return $$$cl2328.Singleton(l$3128,{Element:{t:$$$cl2328.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl2328.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$3129, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl2328.Integer}};
    $$$cl2328.Sequence($$miMatrix);
    $$$cl2328.add_type_arg($$miMatrix,'Cell',{t:$$$cl2328.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    $$miMatrix.sb$3130_=$$$cl2328.SequenceBuilder({Element:{t:$$$cl2328.Sequence,a:{Element:{t:$$$cl2328.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$3131 = $$$cl2328.Range((1),gridSize$3129,{Element:{t:$$$cl2328.Integer}}).iterator;
    var i$3132;while ((i$3132=it$3131.next())!==$$$cl2328.getFinished()){
        $$miMatrix.sb$3130.append($$$cl2328.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$3133=$$$cl2328.Range((1),gridSize$3129,{Element:{t:$$$cl2328.Integer}}).iterator;
            var j$3134=$$$cl2328.getFinished();
            var next$j$3134=function(){return j$3134=it$3133.next();}
            next$j$3134();
            return function(){
                if(j$3134!==$$$cl2328.getFinished()){
                    var j$3134$3135=j$3134;
                    var tmpvar$3136=j$3134$3135;
                    next$j$3134();
                    return tmpvar$3136;
                }
                return $$$cl2328.getFinished();
            }
        },{Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.Integer}}).sequence);
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$3137;
    if($$$cl2328.nonempty((g$3137=$$miMatrix.sb$3130.sequence))){
        var grid$3138=g$3137;
        $$$cl2328.defineAttr($$miMatrix,'grid$3138',function(){return grid$3138;});
    }else {
        var grid$3138=$$$cl2328.Tuple($$$cl2328.Tuple((1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}});
        $$$cl2328.defineAttr($$miMatrix,'grid$3138',function(){return grid$3138;});
    }
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    $$miMatrix.string$3139_=$$miMatrix.grid$3138.string;
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    $$miMatrix.hash$3140_=$$miMatrix.grid$3138.hash;
    var span=(opt$3141=$$miMatrix.grid$3138,$$$cl2328.JsCallable(opt$3141,opt$3141!==null?opt$3141.span:null));
    $$miMatrix.span=span;
    var opt$3141;
    var segment=(opt$3142=$$miMatrix.grid$3138,$$$cl2328.JsCallable(opt$3142,opt$3142!==null?opt$3142.segment:null));
    $$miMatrix.segment=segment;
    var opt$3142;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    $$miMatrix.reversed$3143_=$$miMatrix.grid$3138.reversed;
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    $$miMatrix.lastIndex$3144_=$$miMatrix.grid$3138.lastIndex;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    $$miMatrix.rest$3145_=$$miMatrix.grid$3138.rest;
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    $$miMatrix.first$3146_=$$miMatrix.grid$3138.first;
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl2328.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl2328.Basic,$$$cl2328.Sequence);
        (function($$miMatrix){
            
            //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
            $$$cl2328.defineAttr($$miMatrix,'sb$3130',function(){return this.sb$3130_;});
            
            //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
            
            //AttributeGetterDefinition iterator at aliases.ceylon (46:4-46:72)
            $$$cl2328.defineAttr($$miMatrix,'iterator',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3138.iterator;
            });
            //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
            $$$cl2328.defineAttr($$miMatrix,'string',function(){return this.string$3139_;});
            
            //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
            $$$cl2328.defineAttr($$miMatrix,'hash',function(){return this.hash$3140_;});
            
            //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
            $$miMatrix.equals=function (other$3147){
                var $$miMatrix=this;
                return $$miMatrix.grid$3138.equals(other$3147);
            };
            
            //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
            $$$cl2328.defineAttr($$miMatrix,'reversed',function(){return this.reversed$3143_;});
            
            //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
            $$$cl2328.defineAttr($$miMatrix,'lastIndex',function(){return this.lastIndex$3144_;});
            
            //MethodDeclaration get at aliases.ceylon (54:4-54:55)
            $$miMatrix.get=function (i$3148){
                var $$miMatrix=this;
                return $$miMatrix.grid$3138.get(i$3148);
            };
            
            //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
            $$$cl2328.defineAttr($$miMatrix,'rest',function(){return this.rest$3145_;});
            
            //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
            $$$cl2328.defineAttr($$miMatrix,'first',function(){return this.first$3146_;});
            
            //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
            $$$cl2328.defineAttr($$miMatrix,'clone',function(){
                var $$miMatrix=this;
                return $$miMatrix;
            });
            
            //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
            $$$cl2328.defineAttr($$miMatrix,'size',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3138.size;
            });
            
            //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
            $$miMatrix.contains=function (other$3149){
                var $$miMatrix=this;
                return $$miMatrix.grid$3138.contains(other$3149);
            };
            $$$cl2328.defineAttr($$miMatrix,'last',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3138.last;
            });
            
            //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
            $$miMatrix.spanTo=function (to$3150){
                var $$miMatrix=this;
                return (opt$3151=(to$3150.compare((0)).equals($$$cl2328.getSmaller())?$$$cl2328.getEmpty():null),opt$3151!==null?opt$3151:$$miMatrix.span((0),to$3150));
            };
            
            //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
            $$miMatrix.spanFrom=function (from$3152){
                var $$miMatrix=this;
                return $$miMatrix.span(from$3152,$$miMatrix.size);
            };
        })(MiMatrix.$$.prototype);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();
var opt$3151;

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl2328.print($$$cl2328.String("testing type aliases",20));
    $$$c2329.check(AliasingSubclass().aliasingSubclass(),$$$cl2328.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$3153($$innerSubalias$3153){return AliasingSubclass($$innerSubalias$3153);}
    InnerSubalias$3153.$$=AliasingSubclass.$$;
    $$$c2329.check(InnerSubalias$3153().aliasingSubclass(),$$$cl2328.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$3154(aif$3155){
        return aif$3155.aliasingIface();
    };
    $$$c2329.check(use$3154(AliasingSub2().iface),$$$cl2328.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$3156=(5);
    $$$c2329.check($$$cl2328.isOfType(xxxxx$3156,{t:$$$cl2328.Integer}),$$$cl2328.String("Type alias",10));
    $$$c2329.check(Listleton($$$cl2328.Tuple($$$cl2328.Tuple((1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.Tuple($$$cl2328.Tuple((2),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.Tuple($$$cl2328.Tuple((3),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}).first,{T:{t:$$$cl2328.Integer}}).string.equals($$$cl2328.String("[[1]]",5)),$$$cl2328.StringBuilder().appendAll([$$$cl2328.String("class alias ",12),Listleton($$$cl2328.Tuple($$$cl2328.Tuple((1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.Tuple($$$cl2328.Tuple((2),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.Tuple($$$cl2328.Tuple((3),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}},First:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}},Element:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.Integer},Element:{t:$$$cl2328.Integer}}}}).first,{T:{t:$$$cl2328.Integer}}).string,$$$cl2328.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2329.check(MiMatrix((2)).string.equals($$$cl2328.String("{ { 1, 2 }, { 1, 2 } }",22)),$$$cl2328.StringBuilder().appendAll([$$$cl2328.String("interface alias ",16),MiMatrix((2)).string,$$$cl2328.String(" instead of { { 1, 2 }, { 1, 2 } }",34)]).string);
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$3157=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$3158=$$$cl2328.String("XXXX",4);
    $$$c2329.check($$$cl2328.isOfType(xxxxx1$3157,{ t:'u', l:[{t:$$$cl2328.String},{t:$$$cl2328.Integer}]}),$$$cl2328.String("is String|Integer",17));
    $$$c2329.check($$$cl2328.isOfType(xxxxx2$3158,{ t:'i', l:[{t:$$$cl2328.String},{t:$$$cl2328.List,a:{Element:{t:$$$cl2328.Anything}}}]}),$$$cl2328.String("is String&List",14));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$3159=function (bits$3160){
        if(bits$3160===undefined){bits$3160=$$$cl2328.getEmpty();}
        return $$$cl2328.any(bits$3160);
    };
    $$$c2329.check(cualquiera$3159([true,true,true].reifyCeylonType({Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.true$3161}})),$$$cl2328.String("seq arg method alias",20));
};

//ClassDefinition LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
    $$lateTestChild.parent$3162_=undefined;
    return $$lateTestChild;
}
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl2328.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl2328.Basic);
        (function($$lateTestChild){
            
            //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
            $$$cl2328.defineAttr($$lateTestChild,'parent',function(){if (this.parent$3162_===undefined)throw $$$cl2328.InitializationException($$$cl2328.String('Attempt to read unitialized attribute «parent»'));return this.parent$3162_;},function(parent$3163){if(this.parent$3162_!==undefined)throw $$$cl2328.InitializationException($$$cl2328.String('Attempt to reassign immutable attribute «parent»'));return this.parent$3162_=parent$3163;});
        })(LateTestChild.$$.prototype);
    }
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDefinition LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children$3164, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children$3164===undefined){children$3164=$$$cl2328.getEmpty();}
    
    //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
    $$lateTestParent.children$3165_=children$3164;
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$3166 = $$lateTestParent.children.iterator;
    var child$3167;while ((child$3167=it$3166.next())!==$$$cl2328.getFinished()){
        (child$3167.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl2328.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl2328.Basic);
        (function($$lateTestParent){
            
            //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
            $$$cl2328.defineAttr($$lateTestParent,'children',function(){return this.children$3165_;});
        })(LateTestParent.$$.prototype);
    }
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDefinition testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDeclaration kids at late_support.ceylon (15:4-15:51)
    var kids$3168=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl2328.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$3168);
    try{
        LateTestParent(kids$3168);
        $$$c2329.fail($$$cl2328.String("reassigning to late attribute should fail",41));
    }catch(ex$3169){
        if (ex$3169.getT$name === undefined) ex$3169=$$$cl2328.NativeException(ex$3169);
        if($$$cl2328.isOfType(ex$3169,{t:$$$cl2328.InitializationException})){
            $$$c2329.check(true);
        }
        else if($$$cl2328.isOfType(ex$3169,{t:$$$cl2328.Exception})){
            $$$c2329.fail($$$cl2328.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3169}
    }
    try{
        $$$cl2328.print(LateTestChild().parent);
        $$$c2329.fail($$$cl2328.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$3170){
        if (ex$3170.getT$name === undefined) ex$3170=$$$cl2328.NativeException(ex$3170);
        if($$$cl2328.isOfType(ex$3170,{t:$$$cl2328.InitializationException})){
            $$$c2329.check(true);
        }
        else if($$$cl2328.isOfType(ex$3170,{t:$$$cl2328.Exception})){
            $$$c2329.fail($$$cl2328.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3170}
    }
};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2328.initTypeProto(X,'misc::X');
        (function($$x){
            
            //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl2328.print($$$cl2328.String("hello world",11));
            };
        })(X.$$.prototype);
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$3171, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    $$foo.name$3172_=name$3171;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    $$foo.counter$3173_=(0);
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    $$foo.string$3174_=$$$cl2328.StringBuilder().appendAll([$$$cl2328.String("Foo(",4),$$foo.name.string,$$$cl2328.String(")",1)]).string;
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl2328.initTypeProto(Foo,'misc::Foo',$$$cl2328.Basic);
        (function($$foo){
            
            //AttributeDeclaration name at misc.ceylon (8:4-8:22)
            $$$cl2328.defineAttr($$foo,'name',function(){return this.name$3172_;});
            
            //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
            $$$cl2328.defineAttr($$foo,'counter$3173',function(){return this.counter$3173_;},function(counter$3175){return this.counter$3173_=counter$3175;});
            
            //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
            $$$cl2328.defineAttr($$foo,'count',function(){
                var $$foo=this;
                return $$foo.counter$3173;
            });
            //MethodDefinition inc at misc.ceylon (11:4-11:43)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.counter$3173=$$foo.counter$3173.plus((1));
            };
            //MethodDefinition printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl2328.print($$$cl2328.String("foo name = ",11).plus($$foo.name));
            };
            //AttributeDeclaration string at misc.ceylon (15:4-15:57)
            $$$cl2328.defineAttr($$foo,'string',function(){return this.string$3174_;});
        })(Foo.$$.prototype);
    }
    return Foo;
}
exports.$init$Foo=$init$Foo;
$init$Foo();

//ClassDefinition Bar at misc.ceylon (19:0-34:0)
function Bar($$bar){
    $init$Bar();
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl2328.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl2328.initTypeProto(Bar,'misc::Bar',Foo,$init$X());
        (function($$bar){
            
            //MethodDefinition printName at misc.ceylon (20:4-24:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl2328.print($$$cl2328.String("bar name = ",11).plus($$bar.name));
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
            };
            //ClassDefinition Inner at misc.ceylon (25:4-31:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$bar=this;
                $$$cl2328.print($$$cl2328.String("creating inner class of :",25).plus($$inner$Bar.$$bar.name));
                return $$inner$Bar;
            }
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl2328.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl2328.Basic);
                    (function($$inner$Bar){
                        
                        //MethodDefinition incOuter at misc.ceylon (28:8-30:8)
                        $$inner$Bar.incOuter=function incOuter(){
                            var $$inner$Bar=this;
                            $$inner$Bar.$$bar.inc();
                        };
                    })(Inner$Bar.$$.prototype);
                }
                return Inner$Bar;
            }
            $$bar.$init$Inner$Bar=$init$Inner$Bar;
            $init$Inner$Bar();
            $$bar.Inner$Bar=Inner$Bar;
        })(Bar.$$.prototype);
    }
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDefinition printBoth at misc.ceylon (36:0-38:0)
function printBoth(x$3176,y$3177){
    $$$cl2328.print(x$3176.plus($$$cl2328.String(", ",2)).plus(y$3177));
};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$3178){
    f$3178();
    f$3178();
};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob$3179(){
    var $$foob=new foob$3179.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$foob.name$3180_=$$$cl2328.String("Gavin",5);
    return $$foob;
}
function $init$foob$3179(){
    if (foob$3179.$$===undefined){
        $$$cl2328.initTypeProto(foob$3179,'misc::foob',$$$cl2328.Basic);
    }
    return foob$3179;
}
exports.$init$foob$3179=$init$foob$3179;
$init$foob$3179();
(function($$foob){
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$$cl2328.defineAttr($$foob,'name',function(){return this.name$3180_;});
})(foob$3179.$$.prototype);
var foob$3181=foob$3179();
var getFoob=function(){
    return foob$3181;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$3182){
    if(strings$3182===undefined){strings$3182=$$$cl2328.getEmpty();}
};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$3183, $$f){return Foo(name$3183,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$3184, b$3185, c$3186, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl2328.Null},Element:{t:$$$cl2328.Integer}};
    $$testObjects.a$3184=a$3184;
    $$testObjects.b$3185=b$3185;
    $$testObjects.c$3186=c$3186;
    $$$cl2328.Iterable($$testObjects);
    $$$cl2328.add_type_arg($$testObjects,'Absent',{t:$$$cl2328.Null});
    $$$cl2328.add_type_arg($$testObjects,'Element',{t:$$$cl2328.Integer});
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl2328.initTypeProto(TestObjects,'misc::TestObjects',$$$cl2328.Basic,$$$cl2328.Iterable);
        (function($$testObjects){
            
            //AttributeGetterDefinition iterator at objects.ceylon (4:2-16:2)
            $$$cl2328.defineAttr($$testObjects,'iterator',function(){
                var $$testObjects=this;
                
                //ObjectDefinition iter at objects.ceylon (5:4-14:4)
                function iter$3187($$targs$$){
                    var $$iter$3187=new iter$3187.$$;
                    $$iter$3187.$$targs$$=$$targs$$;
                    $$$cl2328.Iterator($$iter$3187);
                    $$$cl2328.add_type_arg($$iter$3187,'Element',{t:$$$cl2328.Integer});
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$iter$3187.index$3188_=(0);
                    return $$iter$3187;
                }
                function $init$iter$3187(){
                    if (iter$3187.$$===undefined){
                        $$$cl2328.initTypeProto(iter$3187,'misc::TestObjects.iterator.iter',$$$cl2328.Basic,$$$cl2328.Iterator);
                    }
                    return iter$3187;
                }
                $init$iter$3187();
                (function($$iter$3187){
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$$cl2328.defineAttr($$iter$3187,'index$3188',function(){return this.index$3188_;},function(index$3189){return this.index$3188_=index$3189;});
                    
                    //MethodDefinition next at objects.ceylon (7:6-13:6)
                    $$iter$3187.next=function next(){
                        var $$iter$3187=this;
                        (oldindex$3190=$$iter$3187.index$3188,$$iter$3187.index$3188=oldindex$3190.successor,oldindex$3190);
                        var oldindex$3190;
                        if($$iter$3187.index$3188.equals((1))){
                            return $$testObjects.a$3184;
                        }else {
                            if($$iter$3187.index$3188.equals((2))){
                                return $$testObjects.b$3185;
                            }else {
                                if($$iter$3187.index$3188.equals((3))){
                                    return $$testObjects.c$3186;
                                }
                            }
                        }
                        return $$$cl2328.getFinished();
                    };
                })(iter$3187.$$.prototype);
                var iter$3191=iter$3187({Element:{t:$$$cl2328.Integer}});
                var getIter$3191=function(){
                    return iter$3191;
                }
                return getIter$3191();
            });
        })(TestObjects.$$.prototype);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl2328.print($$$cl2328.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:40)
    var t1$3192=TestObjects((1),(2),(3)).iterator;
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:40)
    var t2$3193=TestObjects((1),(2),(3)).iterator;
    var i$3194;
    if($$$cl2328.isOfType((i$3194=t1$3192.next()),{t:$$$cl2328.Integer})){
        $$$c2329.check(i$3194.equals((1)),$$$cl2328.String("objects 1",9));
    }
    var i$3195;
    if($$$cl2328.isOfType((i$3195=t1$3192.next()),{t:$$$cl2328.Integer})){
        $$$c2329.check(i$3195.equals((2)),$$$cl2328.String("objects 2",9));
    }
    var i$3196;
    if($$$cl2328.isOfType((i$3196=t2$3193.next()),{t:$$$cl2328.Integer})){
        $$$c2329.check(i$3196.equals((1)),$$$cl2328.String("objects 3",9));
    }
    var i$3197;
    if($$$cl2328.isOfType((i$3197=t1$3192.next()),{t:$$$cl2328.Integer})){
        $$$c2329.check(i$3197.equals((3)),$$$cl2328.String("objects 4",9));
    }
    $$$c2329.check($$$cl2328.isOfType(t1$3192.next(),{t:$$$cl2328.Finished}),$$$cl2328.String("objects 5",9));
    var i$3198;
    if($$$cl2328.isOfType((i$3198=t2$3193.next()),{t:$$$cl2328.Integer})){
        $$$c2329.check(i$3198.equals((2)),$$$cl2328.String("objects 6",9));
    }
    var i$3199;
    if($$$cl2328.isOfType((i$3199=t2$3193.next()),{t:$$$cl2328.Integer})){
        $$$c2329.check(i$3199.equals((3)),$$$cl2328.String("objects 7",9));
    }
};
var $$$m3200=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-44:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$3201=$$$cl2328.String("hello",5);
    $$$cl2328.print(name$3201);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$3202=Foo($$$cl2328.String("goodbye",7));
    printBoth(name$3201,foo$3202.name);
    (y$3203=$$$cl2328.String("y",1),x$3204=$$$cl2328.String("x",1),printBoth(x$3204,y$3203));
    var y$3203,x$3204;
    foo$3202.inc();
    foo$3202.inc();
    $$$c2329.check(foo$3202.count.equals((3)),$$$cl2328.String("Foo.count",9));
    $$$c2329.check(foo$3202.string.equals($$$cl2328.String("Foo(goodbye)",12)),$$$cl2328.String("Foo.string",10));
    foo$3202.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((opt$3205=foo$3202,$$$cl2328.JsCallable(opt$3205,opt$3205!==null?opt$3205.inc:null)));
    var opt$3205;
    $$$c2329.check(foo$3202.count.equals((5)),$$$cl2328.String("Foo.count [2]",13));
    doIt(Bar);
    $$$cl2328.print(getFoob().name);
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$3206(){
        var $$x$3206=new x$3206.$$;
        return $$x$3206;
    }
    function $init$x$3206(){
        if (x$3206.$$===undefined){
            $$$cl2328.initTypeProto(x$3206,'misc::test.x',$$$cl2328.Basic);
        }
        return x$3206;
    }
    $init$x$3206();
    (function($$x$3206){
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        $$x$3206.y=function y(){
            var $$x$3206=this;
            $$$cl2328.print($$$cl2328.String("xy",2));
        };
    })(x$3206.$$.prototype);
    var x$3207=x$3206();
    var getX$3207=function(){
        return x$3207;
    }
    getX$3207().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$3208=Bar();
    b$3208.Inner$Bar().incOuter();
    b$3208.Inner$Bar().incOuter();
    b$3208.Inner$Bar().incOuter();
    $$$c2329.check(b$3208.count.equals((4)),$$$cl2328.String("Bar.count",9));
    printAll([$$$cl2328.String("hello",5),$$$cl2328.String("world",5)].reifyCeylonType({Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.String}}));
    (strings$3209=$$$cl2328.Tuple($$$cl2328.String("hello",5),$$$cl2328.Tuple($$$cl2328.String("world",5),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),printAll(strings$3209));
    var strings$3209;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$3210=$$$m3200.Counter((0));
    c$3210.inc();
    c$3210.inc();
    $$$c2329.check(c$3210.count.equals((2)),$$$cl2328.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$3211=$var();
    test_objects();
    testAliasing();
    testLate();
    $$$c2329.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
