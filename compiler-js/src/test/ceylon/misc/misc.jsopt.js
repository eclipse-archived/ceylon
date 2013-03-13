(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$pt":"v","$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"}}};
var $$$cl2381=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2382=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    return $$aliasingClass;
}
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl2381.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl2381.Basic);
        (function($$aliasingClass){
            
            //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
            function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
                $$aliasingIface$AliasingClass.$$aliasingClass=this;
            }
            function $init$AliasingIface$AliasingClass(){
                if (AliasingIface$AliasingClass.$$===undefined){
                    $$$cl2381.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
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
                    $$$cl2381.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl2381.Basic);
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
        $$$cl2381.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
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
                    $$$cl2381.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
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
        $$$cl2381.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
        (function($$aliasingSub2){
            
            //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
            $$$cl2381.defineAttr($$aliasingSub2,'iface',function(){
                var $$aliasingSub2=this;
                
                //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
                function aliased$3182(){
                    var $$aliased$3182=new aliased$3182.$$;
                    $$aliasingSub2.AliasingIface$AliasingClass($$aliased$3182);
                    return $$aliased$3182;
                }
                function $init$aliased$3182(){
                    if (aliased$3182.$$===undefined){
                        $$$cl2381.initTypeProto(aliased$3182,'misc::AliasingSub2.iface.aliased',$$$cl2381.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
                    }
                    return aliased$3182;
                }
                $init$aliased$3182();
                var aliased$3183=aliased$3182();
                var getAliased$3183=function(){
                    return aliased$3183;
                }
                return getAliased$3183();
            });
        })(AliasingSub2.$$.prototype);
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$3184, $$targs$$,$$listleton){return $$$cl2381.Singleton(l$3184,{Element:{t:$$$cl2381.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl2381.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$3185, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl2381.Integer}};
    $$$cl2381.Sequence($$miMatrix);
    $$$cl2381.add_type_arg($$miMatrix,'Cell',{t:$$$cl2381.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    $$miMatrix.sb$3186_=$$$cl2381.SequenceBuilder({Element:{t:$$$cl2381.Sequence,a:{Element:{t:$$$cl2381.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$3187 = $$$cl2381.Range((1),gridSize$3185,{Element:{t:$$$cl2381.Integer}}).iterator();
    var i$3188;while ((i$3188=it$3187.next())!==$$$cl2381.getFinished()){
        $$miMatrix.sb$3186.append($$$cl2381.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$3189=$$$cl2381.Range((1),gridSize$3185,{Element:{t:$$$cl2381.Integer}}).iterator();
            var j$3190=$$$cl2381.getFinished();
            var next$j$3190=function(){return j$3190=it$3189.next();}
            next$j$3190();
            return function(){
                if(j$3190!==$$$cl2381.getFinished()){
                    var j$3190$3191=j$3190;
                    var tmpvar$3192=j$3190$3191;
                    next$j$3190();
                    return tmpvar$3192;
                }
                return $$$cl2381.getFinished();
            }
        },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}).sequence);
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$3193;
    if($$$cl2381.nonempty((g$3193=$$miMatrix.sb$3186.sequence))){
        var grid$3194=g$3193;
        $$$cl2381.defineAttr($$miMatrix,'grid$3194',function(){return grid$3194;});
    }else {
        var grid$3194=$$$cl2381.Tuple($$$cl2381.Tuple((1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}});
        $$$cl2381.defineAttr($$miMatrix,'grid$3194',function(){return grid$3194;});
    }
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    $$miMatrix.string$3195_=$$miMatrix.grid$3194.string;
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    $$miMatrix.hash$3196_=$$miMatrix.grid$3194.hash;
    var span=$$$cl2381.$JsCallable((opt$3197=$$miMatrix.grid$3194,$$$cl2381.JsCallable(opt$3197,opt$3197!==null?opt$3197.span:null)),{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Sequential,a:{Element:{t:$$$cl2381.Sequence,a:{Element:{t:$$$cl2381.Integer}}}}}});
    $$miMatrix.span=span;
    var opt$3197;
    var segment=$$$cl2381.$JsCallable((opt$3198=$$miMatrix.grid$3194,$$$cl2381.JsCallable(opt$3198,opt$3198!==null?opt$3198.segment:null)),{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Sequential,a:{Element:{t:$$$cl2381.Sequence,a:{Element:{t:$$$cl2381.Integer}}}}}});
    $$miMatrix.segment=segment;
    var opt$3198;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    $$miMatrix.reversed$3199_=$$miMatrix.grid$3194.reversed;
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    $$miMatrix.lastIndex$3200_=$$miMatrix.grid$3194.lastIndex;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    $$miMatrix.rest$3201_=$$miMatrix.grid$3194.rest;
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    $$miMatrix.first$3202_=$$miMatrix.grid$3194.first;
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl2381.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl2381.Basic,$$$cl2381.Sequence);
        (function($$miMatrix){
            
            //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
            $$$cl2381.defineAttr($$miMatrix,'sb$3186',function(){return this.sb$3186_;});
            
            //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
            
            //MethodDefinition iterator at aliases.ceylon (46:4-46:76)
            $$miMatrix.iterator=function iterator(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3194.iterator();
            };
            //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
            $$$cl2381.defineAttr($$miMatrix,'string',function(){return this.string$3195_;});
            
            //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
            $$$cl2381.defineAttr($$miMatrix,'hash',function(){return this.hash$3196_;});
            
            //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
            $$miMatrix.equals=function (other$3203){
                var $$miMatrix=this;
                return $$miMatrix.grid$3194.equals(other$3203);
            };
            
            //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
            $$$cl2381.defineAttr($$miMatrix,'reversed',function(){return this.reversed$3199_;});
            
            //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
            $$$cl2381.defineAttr($$miMatrix,'lastIndex',function(){return this.lastIndex$3200_;});
            
            //MethodDeclaration get at aliases.ceylon (54:4-54:55)
            $$miMatrix.get=function (i$3204){
                var $$miMatrix=this;
                return $$miMatrix.grid$3194.get(i$3204);
            };
            
            //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
            $$$cl2381.defineAttr($$miMatrix,'rest',function(){return this.rest$3201_;});
            
            //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
            $$$cl2381.defineAttr($$miMatrix,'first',function(){return this.first$3202_;});
            
            //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
            $$$cl2381.defineAttr($$miMatrix,'clone',function(){
                var $$miMatrix=this;
                return $$miMatrix;
            });
            
            //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
            $$$cl2381.defineAttr($$miMatrix,'size',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3194.size;
            });
            
            //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
            $$miMatrix.contains=function (other$3205){
                var $$miMatrix=this;
                return $$miMatrix.grid$3194.contains(other$3205);
            };
            $$$cl2381.defineAttr($$miMatrix,'last',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3194.last;
            });
            
            //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
            $$miMatrix.spanTo=function (to$3206){
                var $$miMatrix=this;
                return (opt$3207=(to$3206.compare((0)).equals($$$cl2381.getSmaller())?$$$cl2381.getEmpty():null),opt$3207!==null?opt$3207:$$miMatrix.span((0),to$3206));
            };
            
            //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
            $$miMatrix.spanFrom=function (from$3208){
                var $$miMatrix=this;
                return $$miMatrix.span(from$3208,$$miMatrix.size);
            };
        })(MiMatrix.$$.prototype);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();
var opt$3207;

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl2381.print($$$cl2381.String("testing type aliases",20));
    $$$c2382.check(AliasingSubclass().aliasingSubclass(),$$$cl2381.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$3209($$innerSubalias$3209){return AliasingSubclass($$innerSubalias$3209);}
    InnerSubalias$3209.$$=AliasingSubclass.$$;
    $$$c2382.check(InnerSubalias$3209().aliasingSubclass(),$$$cl2381.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$3210(aif$3211){
        return aif$3211.aliasingIface();
    };
    $$$c2382.check(use$3210(AliasingSub2().iface),$$$cl2381.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$3212=(5);
    $$$c2382.check($$$cl2381.isOfType(xxxxx$3212,{t:$$$cl2381.Integer}),$$$cl2381.String("Type alias",10));
    $$$c2382.check(Listleton($$$cl2381.Tuple($$$cl2381.Tuple((1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.Tuple($$$cl2381.Tuple((2),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.Tuple($$$cl2381.Tuple((3),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}).first,{T:{t:$$$cl2381.Integer}}).string.equals($$$cl2381.String("[[1]]",5)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("class alias ",12),Listleton($$$cl2381.Tuple($$$cl2381.Tuple((1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.Tuple($$$cl2381.Tuple((2),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.Tuple($$$cl2381.Tuple((3),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}},First:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Element:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}}}).first,{T:{t:$$$cl2381.Integer}}).string,$$$cl2381.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2382.check(MiMatrix((2)).string.equals($$$cl2381.String("[[1, 2], [1, 2]]",16)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("interface alias ",16),MiMatrix((2)).string,$$$cl2381.String(" instead of [[1, 2], [1, 2]]",28)]).string);
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$3213=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$3214=$$$cl2381.String("XXXX",4);
    $$$c2382.check($$$cl2381.isOfType(xxxxx1$3213,{ t:'u', l:[{t:$$$cl2381.String},{t:$$$cl2381.Integer}]}),$$$cl2381.String("is String|Integer",17));
    $$$c2382.check($$$cl2381.isOfType(xxxxx2$3214,{ t:'i', l:[{t:$$$cl2381.String},{t:$$$cl2381.List,a:{Element:{t:$$$cl2381.Anything}}}]}),$$$cl2381.String("is String&List",14));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$3215=function (bits$3216){
        if(bits$3216===undefined){bits$3216=$$$cl2381.getEmpty();}
        return $$$cl2381.any(bits$3216);
    };
    $$$c2382.check(cualquiera$3215([true,true,true].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.true$3217}})),$$$cl2381.String("seq arg method alias",20));
};

//ClassDefinition LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
    $$lateTestChild.parent$3218_=undefined;
    return $$lateTestChild;
}
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl2381.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl2381.Basic);
        (function($$lateTestChild){
            
            //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
            $$$cl2381.defineAttr($$lateTestChild,'parent',function(){if (this.parent$3218_===undefined)throw $$$cl2381.InitializationException($$$cl2381.String('Attempt to read unitialized attribute «parent»'));return this.parent$3218_;},function(parent$3219){if(this.parent$3218_!==undefined)throw $$$cl2381.InitializationException($$$cl2381.String('Attempt to reassign immutable attribute «parent»'));return this.parent$3218_=parent$3219;});
        })(LateTestChild.$$.prototype);
    }
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDefinition LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children$3220, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children$3220===undefined){children$3220=$$$cl2381.getEmpty();}
    
    //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
    $$lateTestParent.children$3221_=children$3220;
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$3222 = $$lateTestParent.children.iterator();
    var child$3223;while ((child$3223=it$3222.next())!==$$$cl2381.getFinished()){
        (child$3223.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl2381.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl2381.Basic);
        (function($$lateTestParent){
            
            //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
            $$$cl2381.defineAttr($$lateTestParent,'children',function(){return this.children$3221_;});
        })(LateTestParent.$$.prototype);
    }
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDefinition testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDeclaration kids at late_support.ceylon (15:4-15:51)
    var kids$3224=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl2381.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$3224);
    try{
        LateTestParent(kids$3224);
        $$$c2382.fail($$$cl2381.String("reassigning to late attribute should fail",41));
    }catch(ex$3225){
        if (ex$3225.getT$name === undefined) ex$3225=$$$cl2381.NativeException(ex$3225);
        if($$$cl2381.isOfType(ex$3225,{t:$$$cl2381.InitializationException})){
            $$$c2382.check(true);
        }
        else if($$$cl2381.isOfType(ex$3225,{t:$$$cl2381.Exception})){
            $$$c2382.fail($$$cl2381.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3225}
    }
    try{
        $$$cl2381.print(LateTestChild().parent);
        $$$c2382.fail($$$cl2381.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$3226){
        if (ex$3226.getT$name === undefined) ex$3226=$$$cl2381.NativeException(ex$3226);
        if($$$cl2381.isOfType(ex$3226,{t:$$$cl2381.InitializationException})){
            $$$c2382.check(true);
        }
        else if($$$cl2381.isOfType(ex$3226,{t:$$$cl2381.Exception})){
            $$$c2382.fail($$$cl2381.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3226}
    }
};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2381.initTypeProto(X,'misc::X');
        (function($$x){
            
            //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl2381.print($$$cl2381.String("hello world",11));
            };
        })(X.$$.prototype);
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$3227, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    $$foo.name$3228_=name$3227;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    $$foo.counter$3229_=(0);
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    $$foo.string$3230_=$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("Foo(",4),$$foo.name.string,$$$cl2381.String(")",1)]).string;
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl2381.initTypeProto(Foo,'misc::Foo',$$$cl2381.Basic);
        (function($$foo){
            
            //AttributeDeclaration name at misc.ceylon (8:4-8:22)
            $$$cl2381.defineAttr($$foo,'name',function(){return this.name$3228_;});
            
            //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
            $$$cl2381.defineAttr($$foo,'counter$3229',function(){return this.counter$3229_;},function(counter$3231){return this.counter$3229_=counter$3231;});
            
            //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
            $$$cl2381.defineAttr($$foo,'count',function(){
                var $$foo=this;
                return $$foo.counter$3229;
            });
            //MethodDefinition inc at misc.ceylon (11:4-11:43)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.counter$3229=$$foo.counter$3229.plus((1));
            };
            //MethodDefinition printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl2381.print($$$cl2381.String("foo name = ",11).plus($$foo.name));
            };
            //AttributeDeclaration string at misc.ceylon (15:4-15:57)
            $$$cl2381.defineAttr($$foo,'string',function(){return this.string$3230_;});
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
    Foo($$$cl2381.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl2381.initTypeProto(Bar,'misc::Bar',Foo,$init$X());
        (function($$bar){
            
            //MethodDefinition printName at misc.ceylon (20:4-24:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl2381.print($$$cl2381.String("bar name = ",11).plus($$bar.name));
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
            };
            //ClassDefinition Inner at misc.ceylon (25:4-31:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$bar=this;
                $$$cl2381.print($$$cl2381.String("creating inner class of :",25).plus($$inner$Bar.$$bar.name));
                return $$inner$Bar;
            }
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl2381.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl2381.Basic);
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
function printBoth(x$3232,y$3233){
    $$$cl2381.print(x$3232.plus($$$cl2381.String(", ",2)).plus(y$3233));
};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$3234){
    f$3234();
    f$3234();
};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob$3235(){
    var $$foob=new foob$3235.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$foob.name$3236_=$$$cl2381.String("Gavin",5);
    return $$foob;
}
function $init$foob$3235(){
    if (foob$3235.$$===undefined){
        $$$cl2381.initTypeProto(foob$3235,'misc::foob',$$$cl2381.Basic);
    }
    return foob$3235;
}
exports.$init$foob$3235=$init$foob$3235;
$init$foob$3235();
(function($$foob){
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$$cl2381.defineAttr($$foob,'name',function(){return this.name$3236_;});
})(foob$3235.$$.prototype);
var foob$3237=foob$3235();
var getFoob=function(){
    return foob$3237;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$3238){
    if(strings$3238===undefined){strings$3238=$$$cl2381.getEmpty();}
};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$3239, $$f){return Foo(name$3239,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$3240, b$3241, c$3242, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl2381.Null},Element:{t:$$$cl2381.Integer}};
    $$testObjects.a$3240=a$3240;
    $$testObjects.b$3241=b$3241;
    $$testObjects.c$3242=c$3242;
    $$$cl2381.Iterable($$testObjects);
    $$$cl2381.add_type_arg($$testObjects,'Absent',{t:$$$cl2381.Null});
    $$$cl2381.add_type_arg($$testObjects,'Element',{t:$$$cl2381.Integer});
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl2381.initTypeProto(TestObjects,'misc::TestObjects',$$$cl2381.Basic,$$$cl2381.Iterable);
        (function($$testObjects){
            
            //MethodDefinition iterator at objects.ceylon (4:2-16:2)
            $$testObjects.iterator=function iterator(){
                var $$testObjects=this;
                
                //ObjectDefinition iter at objects.ceylon (5:4-14:4)
                function iter$3243($$targs$$){
                    var $$iter$3243=new iter$3243.$$;
                    $$iter$3243.$$targs$$=$$targs$$;
                    $$$cl2381.Iterator($$iter$3243);
                    $$$cl2381.add_type_arg($$iter$3243,'Element',{t:$$$cl2381.Integer});
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$iter$3243.index$3244_=(0);
                    return $$iter$3243;
                }
                function $init$iter$3243(){
                    if (iter$3243.$$===undefined){
                        $$$cl2381.initTypeProto(iter$3243,'misc::TestObjects.iterator.iter',$$$cl2381.Basic,$$$cl2381.Iterator);
                    }
                    return iter$3243;
                }
                $init$iter$3243();
                (function($$iter$3243){
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$$cl2381.defineAttr($$iter$3243,'index$3244',function(){return this.index$3244_;},function(index$3245){return this.index$3244_=index$3245;});
                    
                    //MethodDefinition next at objects.ceylon (7:6-13:6)
                    $$iter$3243.next=function next(){
                        var $$iter$3243=this;
                        (oldindex$3246=$$iter$3243.index$3244,$$iter$3243.index$3244=oldindex$3246.successor,oldindex$3246);
                        var oldindex$3246;
                        if($$iter$3243.index$3244.equals((1))){
                            return $$testObjects.a$3240;
                        }else {
                            if($$iter$3243.index$3244.equals((2))){
                                return $$testObjects.b$3241;
                            }else {
                                if($$iter$3243.index$3244.equals((3))){
                                    return $$testObjects.c$3242;
                                }
                            }
                        }
                        return $$$cl2381.getFinished();
                    };
                })(iter$3243.$$.prototype);
                var iter$3247=iter$3243({Element:{t:$$$cl2381.Integer}});
                var getIter$3247=function(){
                    return iter$3247;
                }
                return getIter$3247();
            };
        })(TestObjects.$$.prototype);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl2381.print($$$cl2381.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:42)
    var t1$3248=TestObjects((1),(2),(3)).iterator();
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:42)
    var t2$3249=TestObjects((1),(2),(3)).iterator();
    var i$3250;
    if($$$cl2381.isOfType((i$3250=t1$3248.next()),{t:$$$cl2381.Integer})){
        $$$c2382.check(i$3250.equals((1)),$$$cl2381.String("objects 1",9));
    }
    var i$3251;
    if($$$cl2381.isOfType((i$3251=t1$3248.next()),{t:$$$cl2381.Integer})){
        $$$c2382.check(i$3251.equals((2)),$$$cl2381.String("objects 2",9));
    }
    var i$3252;
    if($$$cl2381.isOfType((i$3252=t2$3249.next()),{t:$$$cl2381.Integer})){
        $$$c2382.check(i$3252.equals((1)),$$$cl2381.String("objects 3",9));
    }
    var i$3253;
    if($$$cl2381.isOfType((i$3253=t1$3248.next()),{t:$$$cl2381.Integer})){
        $$$c2382.check(i$3253.equals((3)),$$$cl2381.String("objects 4",9));
    }
    $$$c2382.check($$$cl2381.isOfType(t1$3248.next(),{t:$$$cl2381.Finished}),$$$cl2381.String("objects 5",9));
    var i$3254;
    if($$$cl2381.isOfType((i$3254=t2$3249.next()),{t:$$$cl2381.Integer})){
        $$$c2382.check(i$3254.equals((2)),$$$cl2381.String("objects 6",9));
    }
    var i$3255;
    if($$$cl2381.isOfType((i$3255=t2$3249.next()),{t:$$$cl2381.Integer})){
        $$$c2382.check(i$3255.equals((3)),$$$cl2381.String("objects 7",9));
    }
};
var $$$m3256=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-44:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$3257=$$$cl2381.String("hello",5);
    $$$cl2381.print(name$3257);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$3258=Foo($$$cl2381.String("goodbye",7));
    printBoth(name$3257,foo$3258.name);
    (y$3259=$$$cl2381.String("y",1),x$3260=$$$cl2381.String("x",1),printBoth(x$3260,y$3259));
    var y$3259,x$3260;
    foo$3258.inc();
    foo$3258.inc();
    $$$c2382.check(foo$3258.count.equals((3)),$$$cl2381.String("Foo.count",9));
    $$$c2382.check(foo$3258.string.equals($$$cl2381.String("Foo(goodbye)",12)),$$$cl2381.String("Foo.string",10));
    foo$3258.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt($$$cl2381.$JsCallable((opt$3261=foo$3258,$$$cl2381.JsCallable(opt$3261,opt$3261!==null?opt$3261.inc:null)),{Arguments:{t:$$$cl2381.Empty},Return:{t:$$$cl2381.Anything}}));
    var opt$3261;
    $$$c2382.check(foo$3258.count.equals((5)),$$$cl2381.String("Foo.count [2]",13));
    doIt($$$cl2381.$JsCallable(Bar,{Arguments:{t:$$$cl2381.Empty},Return:{t:Bar}}));
    $$$cl2381.print(getFoob().name);
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$3262(){
        var $$x$3262=new x$3262.$$;
        return $$x$3262;
    }
    function $init$x$3262(){
        if (x$3262.$$===undefined){
            $$$cl2381.initTypeProto(x$3262,'misc::test.x',$$$cl2381.Basic);
        }
        return x$3262;
    }
    $init$x$3262();
    (function($$x$3262){
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        $$x$3262.y=function y(){
            var $$x$3262=this;
            $$$cl2381.print($$$cl2381.String("xy",2));
        };
    })(x$3262.$$.prototype);
    var x$3263=x$3262();
    var getX$3263=function(){
        return x$3263;
    }
    getX$3263().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$3264=Bar();
    b$3264.Inner$Bar().incOuter();
    b$3264.Inner$Bar().incOuter();
    b$3264.Inner$Bar().incOuter();
    $$$c2382.check(b$3264.count.equals((4)),$$$cl2381.String("Bar.count",9));
    printAll([$$$cl2381.String("hello",5),$$$cl2381.String("world",5)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}));
    (strings$3265=$$$cl2381.Tuple($$$cl2381.String("hello",5),$$$cl2381.Tuple($$$cl2381.String("world",5),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),printAll(strings$3265));
    var strings$3265;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$3266=$$$m3256.Counter((0));
    c$3266.inc();
    c$3266.inc();
    $$$c2382.check(c$3266.count.equals((2)),$$$cl2381.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$3267=$var();
    test_objects();
    testAliasing();
    testLate();
    $$$c2382.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
