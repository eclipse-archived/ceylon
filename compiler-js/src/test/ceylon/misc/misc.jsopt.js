(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$pt":"v","$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$at":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"}}};
var $$$cl2309=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2310=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    return $$aliasingClass;
}
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl2309.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl2309.Basic);
        (function($$aliasingClass){
            
            //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
            function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
                $$aliasingIface$AliasingClass.$$aliasingClass=this;
            }
            function $init$AliasingIface$AliasingClass(){
                if (AliasingIface$AliasingClass.$$===undefined){
                    $$$cl2309.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
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
                    $$$cl2309.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl2309.Basic);
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
        $$$cl2309.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
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
                    $$$cl2309.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
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
        $$$cl2309.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
        (function($$aliasingSub2){
            
            //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
            $$$cl2309.defineAttr($$aliasingSub2,'iface',function(){
                var $$aliasingSub2=this;
                
                //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
                function aliased$3093(){
                    var $$aliased$3093=new aliased$3093.$$;
                    $$aliasingSub2.AliasingIface$AliasingClass($$aliased$3093);
                    return $$aliased$3093;
                }
                function $init$aliased$3093(){
                    if (aliased$3093.$$===undefined){
                        $$$cl2309.initTypeProto(aliased$3093,'misc::AliasingSub2.iface.aliased',$$$cl2309.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
                    }
                    return aliased$3093;
                }
                $init$aliased$3093();
                var aliased$3094=aliased$3093();
                var getAliased$3094=function(){
                    return aliased$3094;
                }
                return getAliased$3094();
            });
        })(AliasingSub2.$$.prototype);
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$3095, $$targs$$,$$listleton){return $$$cl2309.Singleton(l$3095,{Element:{t:$$$cl2309.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl2309.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$3096, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl2309.Integer}};
    $$$cl2309.Sequence($$miMatrix);
    $$$cl2309.add_type_arg($$miMatrix,'Cell',{t:$$$cl2309.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    $$miMatrix.sb$3097_=$$$cl2309.SequenceBuilder({Element:{t:$$$cl2309.Sequence,a:{Element:{t:$$$cl2309.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$3098 = $$$cl2309.Range((1),gridSize$3096,{Element:{t:$$$cl2309.Integer}}).iterator;
    var i$3099;while ((i$3099=it$3098.next())!==$$$cl2309.getFinished()){
        $$miMatrix.sb$3097.append($$$cl2309.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$3100=$$$cl2309.Range((1),gridSize$3096,{Element:{t:$$$cl2309.Integer}}).iterator;
            var j$3101=$$$cl2309.getFinished();
            var next$j$3101=function(){return j$3101=it$3100.next();}
            next$j$3101();
            return function(){
                if(j$3101!==$$$cl2309.getFinished()){
                    var j$3101$3102=j$3101;
                    var tmpvar$3103=j$3101$3102;
                    next$j$3101();
                    return tmpvar$3103;
                }
                return $$$cl2309.getFinished();
            }
        },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}).sequence);
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$3104;
    if($$$cl2309.nonempty((g$3104=$$miMatrix.sb$3097.sequence))){
        var grid$3105=g$3104;
        $$$cl2309.defineAttr($$miMatrix,'grid$3105',function(){return grid$3105;});
    }else {
        var grid$3105=$$$cl2309.Tuple($$$cl2309.Tuple((1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}});
        $$$cl2309.defineAttr($$miMatrix,'grid$3105',function(){return grid$3105;});
    }
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    $$miMatrix.string$3106_=$$miMatrix.grid$3105.string;
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    $$miMatrix.hash$3107_=$$miMatrix.grid$3105.hash;
    var span=(opt$3108=$$miMatrix.grid$3105,$$$cl2309.JsCallable(opt$3108,opt$3108!==null?opt$3108.span:null));
    $$miMatrix.span=span;
    var opt$3108;
    var segment=(opt$3109=$$miMatrix.grid$3105,$$$cl2309.JsCallable(opt$3109,opt$3109!==null?opt$3109.segment:null));
    $$miMatrix.segment=segment;
    var opt$3109;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    $$miMatrix.reversed$3110_=$$miMatrix.grid$3105.reversed;
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    $$miMatrix.lastIndex$3111_=$$miMatrix.grid$3105.lastIndex;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    $$miMatrix.rest$3112_=$$miMatrix.grid$3105.rest;
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    $$miMatrix.first$3113_=$$miMatrix.grid$3105.first;
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl2309.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl2309.Basic,$$$cl2309.Sequence);
        (function($$miMatrix){
            
            //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
            $$$cl2309.defineAttr($$miMatrix,'sb$3097',function(){return this.sb$3097_;});
            
            //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
            
            //AttributeGetterDefinition iterator at aliases.ceylon (46:4-46:72)
            $$$cl2309.defineAttr($$miMatrix,'iterator',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3105.iterator;
            });
            //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
            $$$cl2309.defineAttr($$miMatrix,'string',function(){return this.string$3106_;});
            
            //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
            $$$cl2309.defineAttr($$miMatrix,'hash',function(){return this.hash$3107_;});
            
            //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
            $$miMatrix.equals=function (other$3114){
                var $$miMatrix=this;
                return $$miMatrix.grid$3105.equals(other$3114);
            };
            
            //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
            $$$cl2309.defineAttr($$miMatrix,'reversed',function(){return this.reversed$3110_;});
            
            //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
            $$$cl2309.defineAttr($$miMatrix,'lastIndex',function(){return this.lastIndex$3111_;});
            
            //MethodDeclaration get at aliases.ceylon (54:4-54:55)
            $$miMatrix.get=function (i$3115){
                var $$miMatrix=this;
                return $$miMatrix.grid$3105.get(i$3115);
            };
            
            //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
            $$$cl2309.defineAttr($$miMatrix,'rest',function(){return this.rest$3112_;});
            
            //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
            $$$cl2309.defineAttr($$miMatrix,'first',function(){return this.first$3113_;});
            
            //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
            $$$cl2309.defineAttr($$miMatrix,'clone',function(){
                var $$miMatrix=this;
                return $$miMatrix;
            });
            
            //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
            $$$cl2309.defineAttr($$miMatrix,'size',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3105.size;
            });
            
            //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
            $$miMatrix.contains=function (other$3116){
                var $$miMatrix=this;
                return $$miMatrix.grid$3105.contains(other$3116);
            };
            $$$cl2309.defineAttr($$miMatrix,'last',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$3105.last;
            });
            
            //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
            $$miMatrix.spanTo=function (to$3117){
                var $$miMatrix=this;
                return (opt$3118=(to$3117.compare((0)).equals($$$cl2309.getSmaller())?$$$cl2309.getEmpty():null),opt$3118!==null?opt$3118:$$miMatrix.span((0),to$3117));
            };
            
            //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
            $$miMatrix.spanFrom=function (from$3119){
                var $$miMatrix=this;
                return $$miMatrix.span(from$3119,$$miMatrix.size);
            };
        })(MiMatrix.$$.prototype);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();
var opt$3118;

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl2309.print($$$cl2309.String("testing type aliases",20));
    $$$c2310.check(AliasingSubclass().aliasingSubclass(),$$$cl2309.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$3120($$innerSubalias$3120){return AliasingSubclass($$innerSubalias$3120);}
    InnerSubalias$3120.$$=AliasingSubclass.$$;
    $$$c2310.check(InnerSubalias$3120().aliasingSubclass(),$$$cl2309.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$3121(aif$3122){
        return aif$3122.aliasingIface();
    };
    $$$c2310.check(use$3121(AliasingSub2().iface),$$$cl2309.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$3123=(5);
    $$$c2310.check($$$cl2309.isOfType(xxxxx$3123,{t:$$$cl2309.Integer}),$$$cl2309.String("Type alias",10));
    $$$c2310.check(Listleton($$$cl2309.Tuple($$$cl2309.Tuple((1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.Tuple($$$cl2309.Tuple((2),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.Tuple($$$cl2309.Tuple((3),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}).first,{T:{t:$$$cl2309.Integer}}).string.equals($$$cl2309.String("[[1]]",5)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("class alias ",12),Listleton($$$cl2309.Tuple($$$cl2309.Tuple((1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.Tuple($$$cl2309.Tuple((2),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.Tuple($$$cl2309.Tuple((3),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}},First:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},Element:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}}}).first,{T:{t:$$$cl2309.Integer}}).string,$$$cl2309.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2310.check(MiMatrix((2)).string.equals($$$cl2309.String("{ { 1, 2 }, { 1, 2 } }",22)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("interface alias ",16),MiMatrix((2)).string,$$$cl2309.String(" instead of { { 1, 2 }, { 1, 2 } }",34)]).string);
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$3124=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$3125=$$$cl2309.String("XXXX",4);
    $$$c2310.check($$$cl2309.isOfType(xxxxx1$3124,{ t:'u', l:[{t:$$$cl2309.String},{t:$$$cl2309.Integer}]}),$$$cl2309.String("is String|Integer",17));
    $$$c2310.check($$$cl2309.isOfType(xxxxx2$3125,{ t:'i', l:[{t:$$$cl2309.String},{t:$$$cl2309.List,a:{Element:{t:$$$cl2309.Anything}}}]}),$$$cl2309.String("is String&List",14));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$3126=function (bits$3127){
        if(bits$3127===undefined){bits$3127=$$$cl2309.getEmpty();}
        return $$$cl2309.any(bits$3127);
    };
    $$$c2310.check(cualquiera$3126([true,true,true].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.true$3128}})),$$$cl2309.String("seq arg method alias",20));
};

//ClassDefinition LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
    $$lateTestChild.parent$3129_=undefined;
    return $$lateTestChild;
}
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl2309.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl2309.Basic);
        (function($$lateTestChild){
            
            //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
            $$$cl2309.defineAttr($$lateTestChild,'parent',function(){if (this.parent$3129_===undefined)throw $$$cl2309.InitializationException($$$cl2309.String('Attempt to read unitialized attribute «parent»'));return this.parent$3129_;},function(parent$3130){if(this.parent$3129_!==undefined)throw $$$cl2309.InitializationException($$$cl2309.String('Attempt to reassign immutable attribute «parent»'));return this.parent$3129_=parent$3130;});
        })(LateTestChild.$$.prototype);
    }
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDefinition LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children$3131, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children$3131===undefined){children$3131=$$$cl2309.getEmpty();}
    
    //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
    $$lateTestParent.children$3132_=children$3131;
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$3133 = $$lateTestParent.children.iterator;
    var child$3134;while ((child$3134=it$3133.next())!==$$$cl2309.getFinished()){
        (tmp$3135=child$3134,tmp$3135.parent=$$lateTestParent,tmp$3135.parent);
        var tmp$3135;
    }
    return $$lateTestParent;
}
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl2309.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl2309.Basic);
        (function($$lateTestParent){
            
            //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
            $$$cl2309.defineAttr($$lateTestParent,'children',function(){return this.children$3132_;});
        })(LateTestParent.$$.prototype);
    }
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDefinition testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDeclaration kids at late_support.ceylon (15:4-15:51)
    var kids$3136=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl2309.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$3136);
    try{
        LateTestParent(kids$3136);
        $$$c2310.fail($$$cl2309.String("reassigning to late attribute should fail",41));
    }catch(ex$3137){
        if (ex$3137.getT$name === undefined) ex$3137=$$$cl2309.NativeException(ex$3137);
        if($$$cl2309.isOfType(ex$3137,{t:$$$cl2309.InitializationException})){
            $$$c2310.check(true);
        }
        else if($$$cl2309.isOfType(ex$3137,{t:$$$cl2309.Exception})){
            $$$c2310.fail($$$cl2309.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3137}
    }
    try{
        $$$cl2309.print(LateTestChild().parent);
        $$$c2310.fail($$$cl2309.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$3138){
        if (ex$3138.getT$name === undefined) ex$3138=$$$cl2309.NativeException(ex$3138);
        if($$$cl2309.isOfType(ex$3138,{t:$$$cl2309.InitializationException})){
            $$$c2310.check(true);
        }
        else if($$$cl2309.isOfType(ex$3138,{t:$$$cl2309.Exception})){
            $$$c2310.fail($$$cl2309.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$3138}
    }
};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2309.initTypeProto(X,'misc::X');
        (function($$x){
            
            //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl2309.print($$$cl2309.String("hello world",11));
            };
        })(X.$$.prototype);
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$3139, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    $$foo.name$3140_=name$3139;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    $$foo.counter$3141_=(0);
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    $$foo.string$3142_=$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("Foo(",4),$$foo.name.string,$$$cl2309.String(")",1)]).string;
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl2309.initTypeProto(Foo,'misc::Foo',$$$cl2309.Basic);
        (function($$foo){
            
            //AttributeDeclaration name at misc.ceylon (8:4-8:22)
            $$$cl2309.defineAttr($$foo,'name',function(){return this.name$3140_;});
            
            //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
            $$$cl2309.defineAttr($$foo,'counter$3141',function(){return this.counter$3141_;},function(counter$3143){return this.counter$3141_=counter$3143;});
            
            //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
            $$$cl2309.defineAttr($$foo,'count',function(){
                var $$foo=this;
                return $$foo.counter$3141;
            });
            //MethodDefinition inc at misc.ceylon (11:4-11:43)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.counter$3141=$$foo.counter$3141.plus((1));
            };
            //MethodDefinition printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl2309.print($$$cl2309.String("foo name = ",11).plus($$foo.name));
            };
            //AttributeDeclaration string at misc.ceylon (15:4-15:57)
            $$$cl2309.defineAttr($$foo,'string',function(){return this.string$3142_;});
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
    Foo($$$cl2309.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl2309.initTypeProto(Bar,'misc::Bar',Foo,$init$X());
        (function($$bar){
            
            //MethodDefinition printName at misc.ceylon (20:4-24:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl2309.print($$$cl2309.String("bar name = ",11).plus($$bar.name));
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
            };
            //ClassDefinition Inner at misc.ceylon (25:4-31:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$bar=this;
                $$$cl2309.print($$$cl2309.String("creating inner class of :",25).plus($$inner$Bar.$$bar.name));
                return $$inner$Bar;
            }
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl2309.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl2309.Basic);
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
function printBoth(x$3144,y$3145){
    $$$cl2309.print(x$3144.plus($$$cl2309.String(", ",2)).plus(y$3145));
};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$3146){
    f$3146();
    f$3146();
};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob$3147(){
    var $$foob=new foob$3147.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$foob.name$3148_=$$$cl2309.String("Gavin",5);
    return $$foob;
}
function $init$foob$3147(){
    if (foob$3147.$$===undefined){
        $$$cl2309.initTypeProto(foob$3147,'misc::foob',$$$cl2309.Basic);
    }
    return foob$3147;
}
exports.$init$foob$3147=$init$foob$3147;
$init$foob$3147();
(function($$foob){
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$$cl2309.defineAttr($$foob,'name',function(){return this.name$3148_;});
})(foob$3147.$$.prototype);
var foob$3149=foob$3147();
var getFoob=function(){
    return foob$3149;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$3150){
    if(strings$3150===undefined){strings$3150=$$$cl2309.getEmpty();}
};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$3151, $$f){return Foo(name$3151,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$3152, b$3153, c$3154, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl2309.Null},Element:{t:$$$cl2309.Integer}};
    $$testObjects.a$3152=a$3152;
    $$testObjects.b$3153=b$3153;
    $$testObjects.c$3154=c$3154;
    $$$cl2309.Iterable($$testObjects);
    $$$cl2309.add_type_arg($$testObjects,'Absent',{t:$$$cl2309.Null});
    $$$cl2309.add_type_arg($$testObjects,'Element',{t:$$$cl2309.Integer});
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl2309.initTypeProto(TestObjects,'misc::TestObjects',$$$cl2309.Basic,$$$cl2309.Iterable);
        (function($$testObjects){
            
            //AttributeGetterDefinition iterator at objects.ceylon (4:2-16:2)
            $$$cl2309.defineAttr($$testObjects,'iterator',function(){
                var $$testObjects=this;
                
                //ObjectDefinition iter at objects.ceylon (5:4-14:4)
                function iter$3155($$targs$$){
                    var $$iter$3155=new iter$3155.$$;
                    $$iter$3155.$$targs$$=$$targs$$;
                    $$$cl2309.Iterator($$iter$3155);
                    $$$cl2309.add_type_arg($$iter$3155,'Element',{t:$$$cl2309.Integer});
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$iter$3155.index$3156_=(0);
                    return $$iter$3155;
                }
                function $init$iter$3155(){
                    if (iter$3155.$$===undefined){
                        $$$cl2309.initTypeProto(iter$3155,'misc::TestObjects.iterator.iter',$$$cl2309.Basic,$$$cl2309.Iterator);
                    }
                    return iter$3155;
                }
                $init$iter$3155();
                (function($$iter$3155){
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$$cl2309.defineAttr($$iter$3155,'index$3156',function(){return this.index$3156_;},function(index$3157){return this.index$3156_=index$3157;});
                    
                    //MethodDefinition next at objects.ceylon (7:6-13:6)
                    $$iter$3155.next=function next(){
                        var $$iter$3155=this;
                        (oldindex$3158=$$iter$3155.index$3156,$$iter$3155.index$3156=oldindex$3158.successor,oldindex$3158);
                        var oldindex$3158;
                        if($$iter$3155.index$3156.equals((1))){
                            return $$testObjects.a$3152;
                        }else {
                            if($$iter$3155.index$3156.equals((2))){
                                return $$testObjects.b$3153;
                            }else {
                                if($$iter$3155.index$3156.equals((3))){
                                    return $$testObjects.c$3154;
                                }
                            }
                        }
                        return $$$cl2309.getFinished();
                    };
                })(iter$3155.$$.prototype);
                var iter$3159=iter$3155({Element:{t:$$$cl2309.Integer}});
                var getIter$3159=function(){
                    return iter$3159;
                }
                return getIter$3159();
            });
        })(TestObjects.$$.prototype);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl2309.print($$$cl2309.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:40)
    var t1$3160=TestObjects((1),(2),(3)).iterator;
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:40)
    var t2$3161=TestObjects((1),(2),(3)).iterator;
    var i$3162;
    if($$$cl2309.isOfType((i$3162=t1$3160.next()),{t:$$$cl2309.Integer})){
        $$$c2310.check(i$3162.equals((1)),$$$cl2309.String("objects 1",9));
    }
    var i$3163;
    if($$$cl2309.isOfType((i$3163=t1$3160.next()),{t:$$$cl2309.Integer})){
        $$$c2310.check(i$3163.equals((2)),$$$cl2309.String("objects 2",9));
    }
    var i$3164;
    if($$$cl2309.isOfType((i$3164=t2$3161.next()),{t:$$$cl2309.Integer})){
        $$$c2310.check(i$3164.equals((1)),$$$cl2309.String("objects 3",9));
    }
    var i$3165;
    if($$$cl2309.isOfType((i$3165=t1$3160.next()),{t:$$$cl2309.Integer})){
        $$$c2310.check(i$3165.equals((3)),$$$cl2309.String("objects 4",9));
    }
    $$$c2310.check($$$cl2309.isOfType(t1$3160.next(),{t:$$$cl2309.Finished}),$$$cl2309.String("objects 5",9));
    var i$3166;
    if($$$cl2309.isOfType((i$3166=t2$3161.next()),{t:$$$cl2309.Integer})){
        $$$c2310.check(i$3166.equals((2)),$$$cl2309.String("objects 6",9));
    }
    var i$3167;
    if($$$cl2309.isOfType((i$3167=t2$3161.next()),{t:$$$cl2309.Integer})){
        $$$c2310.check(i$3167.equals((3)),$$$cl2309.String("objects 7",9));
    }
};
var $$$m3168=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-44:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$3169=$$$cl2309.String("hello",5);
    $$$cl2309.print(name$3169);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$3170=Foo($$$cl2309.String("goodbye",7));
    printBoth(name$3169,foo$3170.name);
    (y$3171=$$$cl2309.String("y",1),x$3172=$$$cl2309.String("x",1),printBoth(x$3172,y$3171));
    var y$3171,x$3172;
    foo$3170.inc();
    foo$3170.inc();
    $$$c2310.check(foo$3170.count.equals((3)),$$$cl2309.String("Foo.count",9));
    $$$c2310.check(foo$3170.string.equals($$$cl2309.String("Foo(goodbye)",12)),$$$cl2309.String("Foo.string",10));
    foo$3170.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((opt$3173=foo$3170,$$$cl2309.JsCallable(opt$3173,opt$3173!==null?opt$3173.inc:null)));
    var opt$3173;
    $$$c2310.check(foo$3170.count.equals((5)),$$$cl2309.String("Foo.count [2]",13));
    doIt(Bar);
    $$$cl2309.print(getFoob().name);
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$3174(){
        var $$x$3174=new x$3174.$$;
        return $$x$3174;
    }
    function $init$x$3174(){
        if (x$3174.$$===undefined){
            $$$cl2309.initTypeProto(x$3174,'misc::test.x',$$$cl2309.Basic);
        }
        return x$3174;
    }
    $init$x$3174();
    (function($$x$3174){
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        $$x$3174.y=function y(){
            var $$x$3174=this;
            $$$cl2309.print($$$cl2309.String("xy",2));
        };
    })(x$3174.$$.prototype);
    var x$3175=x$3174();
    var getX$3175=function(){
        return x$3175;
    }
    getX$3175().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$3176=Bar();
    b$3176.Inner$Bar().incOuter();
    b$3176.Inner$Bar().incOuter();
    b$3176.Inner$Bar().incOuter();
    $$$c2310.check(b$3176.count.equals((4)),$$$cl2309.String("Bar.count",9));
    printAll([$$$cl2309.String("hello",5),$$$cl2309.String("world",5)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}));
    (strings$3177=$$$cl2309.Tuple($$$cl2309.String("hello",5),$$$cl2309.Tuple($$$cl2309.String("world",5),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),printAll(strings$3177));
    var strings$3177;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$3178=$$$m3168.Counter((0));
    c$3178.inc();
    c$3178.inc();
    $$$c2310.check(c$3178.count.equals((2)),$$$cl2309.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$3179=$var();
    test_objects();
    testAliasing();
    testLate();
    $$$c2310.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
