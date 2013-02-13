(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$at":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"}}};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2244=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    return $$aliasingClass;
}
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl2243.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl2243.Basic);
        (function($$aliasingClass){
            
            //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
            function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
                $$aliasingIface$AliasingClass.$$aliasingClass=this;
            }
            function $init$AliasingIface$AliasingClass(){
                if (AliasingIface$AliasingClass.$$===undefined){
                    $$$cl2243.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
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
                    $$$cl2243.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl2243.Basic);
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
        $$$cl2243.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
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
                    $$$cl2243.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
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
        $$$cl2243.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
        (function($$aliasingSub2){
            
            //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
            $$aliasingSub2.getIface=function getIface(){
                var $$aliasingSub2=this;
                
                //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
                function aliased$2909(){
                    var $$aliased$2909=new aliased$2909.$$;
                    $$aliasingSub2.AliasingIface$AliasingClass($$aliased$2909);
                    return $$aliased$2909;
                }
                function $init$aliased$2909(){
                    if (aliased$2909.$$===undefined){
                        $$$cl2243.initTypeProto(aliased$2909,'misc::AliasingSub2.iface.aliased',$$$cl2243.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
                    }
                    return aliased$2909;
                }
                $init$aliased$2909();
                var aliased$2910=aliased$2909();
                var getAliased$2910=function(){
                    return aliased$2910;
                }
                return getAliased$2910();
            };
        })(AliasingSub2.$$.prototype);
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$2911, $$targs$$,$$listleton){return $$$cl2243.Singleton(l$2911,{Element:{t:$$$cl2243.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl2243.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$2912, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl2243.Integer}};
    $$$cl2243.Sequence($$miMatrix);
    $$$cl2243.add_type_arg($$miMatrix,'Cell',{t:$$$cl2243.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    $$miMatrix.sb$2913=$$$cl2243.SequenceBuilder({Element:{t:$$$cl2243.Sequence,a:{Element:{t:$$$cl2243.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$2914 = $$$cl2243.Range((1),gridSize$2912,{Element:{t:$$$cl2243.Integer}}).getIterator();
    var i$2915;while ((i$2915=it$2914.next())!==$$$cl2243.getFinished()){
        $$miMatrix.getSb$2913().append($$$cl2243.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$2916=$$$cl2243.Range((1),gridSize$2912,{Element:{t:$$$cl2243.Integer}}).getIterator();
            var j$2917=$$$cl2243.getFinished();
            var next$j$2917=function(){return j$2917=it$2916.next();}
            next$j$2917();
            return function(){
                if(j$2917!==$$$cl2243.getFinished()){
                    var j$2917$2918=j$2917;
                    function getJ$2917(){return j$2917$2918;}
                    var tmpvar$2919=getJ$2917();
                    next$j$2917();
                    return tmpvar$2919;
                }
                return $$$cl2243.getFinished();
            }
        },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}).getSequence());
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$2920;
    if($$$cl2243.nonempty((g$2920=$$miMatrix.getSb$2913().getSequence()))){
        var grid$2921=g$2920;
        var getGrid$2921=function(){return grid$2921;};
        $$miMatrix.getGrid$2921=getGrid$2921;
    }else {
        var grid$2921=$$$cl2243.Tuple($$$cl2243.Tuple((1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}});
        var getGrid$2921=function(){return grid$2921;};
        $$miMatrix.getGrid$2921=getGrid$2921;
    }
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    $$miMatrix.string$2922=$$miMatrix.getGrid$2921().getString();
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    $$miMatrix.hash$2923=$$miMatrix.getGrid$2921().getHash();
    var span=(opt$2924=$$miMatrix.getGrid$2921(),$$$cl2243.JsCallable(opt$2924,opt$2924!==null?opt$2924.span:null));
    $$miMatrix.span=span;
    var opt$2924;
    var segment=(opt$2925=$$miMatrix.getGrid$2921(),$$$cl2243.JsCallable(opt$2925,opt$2925!==null?opt$2925.segment:null));
    $$miMatrix.segment=segment;
    var opt$2925;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    $$miMatrix.reversed$2926=$$miMatrix.getGrid$2921().getReversed();
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    $$miMatrix.lastIndex$2927=$$miMatrix.getGrid$2921().getLastIndex();
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    $$miMatrix.rest$2928=$$miMatrix.getGrid$2921().getRest();
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    $$miMatrix.first$2929=$$miMatrix.getGrid$2921().getFirst();
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl2243.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl2243.Basic,$$$cl2243.Sequence);
        (function($$miMatrix){
            
            //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
            $$miMatrix.getSb$2913=function getSb$2913(){
                return this.sb$2913;
            };
            
            //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
            
            //AttributeGetterDefinition iterator at aliases.ceylon (46:4-46:72)
            $$miMatrix.getIterator=function getIterator(){
                var $$miMatrix=this;
                return $$miMatrix.getGrid$2921().getIterator();
            };
            //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
            $$miMatrix.getString=function getString(){
                return this.string$2922;
            };
            
            //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
            $$miMatrix.getHash=function getHash(){
                return this.hash$2923;
            };
            
            //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
            $$miMatrix.equals=function (other$2930){
                var $$miMatrix=this;
                return $$miMatrix.getGrid$2921().equals(other$2930);
            };
            
            //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
            $$miMatrix.getReversed=function getReversed(){
                return this.reversed$2926;
            };
            
            //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
            $$miMatrix.getLastIndex=function getLastIndex(){
                return this.lastIndex$2927;
            };
            
            //MethodDeclaration get at aliases.ceylon (54:4-54:55)
            $$miMatrix.get=function (i$2931){
                var $$miMatrix=this;
                return $$miMatrix.getGrid$2921().get(i$2931);
            };
            
            //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
            $$miMatrix.getRest=function getRest(){
                return this.rest$2928;
            };
            
            //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
            $$miMatrix.getFirst=function getFirst(){
                return this.first$2929;
            };
            
            //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
            $$miMatrix.getClone=function getClone(){
                var $$miMatrix=this;
                return $$miMatrix;
            };
            
            //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
            $$miMatrix.getSize=function getSize(){
                var $$miMatrix=this;
                return $$miMatrix.getGrid$2921().getSize();
            };
            
            //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
            $$miMatrix.contains=function (other$2932){
                var $$miMatrix=this;
                return $$miMatrix.getGrid$2921().contains(other$2932);
            };
            $$miMatrix.getLast=function(){
                var $$miMatrix=this;
                return $$miMatrix.getGrid$2921().getLast();
            };
            
            //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
            $$miMatrix.spanTo=function (to$2933){
                var $$miMatrix=this;
                return (opt$2934=(to$2933.compare((0)).equals($$$cl2243.getSmaller())?$$$cl2243.getEmpty():null),opt$2934!==null?opt$2934:$$miMatrix.span((0),to$2933));
            };
            
            //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
            $$miMatrix.spanFrom=function (from$2935){
                var $$miMatrix=this;
                return $$miMatrix.span(from$2935,$$miMatrix.getSize());
            };
        })(MiMatrix.$$.prototype);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();
var opt$2934;

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl2243.print($$$cl2243.String("testing type aliases",20));
    $$$c2244.check(AliasingSubclass().aliasingSubclass(),$$$cl2243.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$2936($$innerSubalias$2936){return AliasingSubclass($$innerSubalias$2936);}
    InnerSubalias$2936.$$=AliasingSubclass.$$;
    $$$c2244.check(InnerSubalias$2936().aliasingSubclass(),$$$cl2243.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$2937(aif$2938){
        return aif$2938.aliasingIface();
    };
    $$$c2244.check(use$2937(AliasingSub2().getIface()),$$$cl2243.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$2939=(5);
    $$$c2244.check($$$cl2243.isOfType(xxxxx$2939,{t:$$$cl2243.Integer}),$$$cl2243.String("Type alias",10));
    $$$c2244.check(Listleton($$$cl2243.Tuple($$$cl2243.Tuple((1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.Tuple($$$cl2243.Tuple((2),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.Tuple($$$cl2243.Tuple((3),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}).getFirst(),{T:{t:$$$cl2243.Integer}}).getString().equals($$$cl2243.String("[[1]]",5)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("class alias ",12),Listleton($$$cl2243.Tuple($$$cl2243.Tuple((1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.Tuple($$$cl2243.Tuple((2),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.Tuple($$$cl2243.Tuple((3),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}},First:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},Element:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}}}).getFirst(),{T:{t:$$$cl2243.Integer}}).getString(),$$$cl2243.String(" instead of [ [ 1 ] ]",21)]).getString());
    $$$c2244.check(MiMatrix((2)).getString().equals($$$cl2243.String("{ { 1, 2 }, { 1, 2 } }",22)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("interface alias ",16),MiMatrix((2)).getString(),$$$cl2243.String(" instead of { { 1, 2 }, { 1, 2 } }",34)]).getString());
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$2940=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$2941=$$$cl2243.String("XXXX",4);
    $$$c2244.check($$$cl2243.isOfType(xxxxx1$2940,{ t:'u', l:[{t:$$$cl2243.String},{t:$$$cl2243.Integer}]}),$$$cl2243.String("is String|Integer",17));
    $$$c2244.check($$$cl2243.isOfType(xxxxx2$2941,{ t:'i', l:[{t:$$$cl2243.String},{t:$$$cl2243.Sequence,a:{Element:{t:$$$cl2243.Anything}}}]}),$$$cl2243.String("is String&Sequence",18));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$2942=function (bits$2943){
        if(bits$2943===undefined){bits$2943=$$$cl2243.getEmpty();}
        return $$$cl2243.any(bits$2943);
    };
    $$$c2244.check(cualquiera$2942([true,true,true].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.$true}})),$$$cl2243.String("seq arg method alias",20));
};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2243.initTypeProto(X,'misc::X');
        (function($$x){
            
            //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl2243.print($$$cl2243.String("hello world",11));
            };
        })(X.$$.prototype);
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$2944, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    $$foo.name$2945=name$2944;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    $$foo.counter$2946=(0);
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    $$foo.string$2947=$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("Foo(",4),$$foo.getName().getString(),$$$cl2243.String(")",1)]).getString();
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl2243.initTypeProto(Foo,'misc::Foo',$$$cl2243.Basic);
        (function($$foo){
            
            //AttributeDeclaration name at misc.ceylon (8:4-8:22)
            $$foo.getName=function getName(){
                return this.name$2945;
            };
            
            //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
            $$foo.getCounter$2946=function getCounter$2946(){
                return this.counter$2946;
            };
            $$foo.setCounter$2946=function setCounter$2946(counter$2948){
                return this.counter$2946=counter$2948;
            };
            
            //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
            $$foo.getCount=function getCount(){
                var $$foo=this;
                return $$foo.getCounter$2946();
            };
            //MethodDefinition inc at misc.ceylon (11:4-11:43)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.setCounter$2946($$foo.getCounter$2946().plus((1)));
            };
            //MethodDefinition printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl2243.print($$$cl2243.String("foo name = ",11).plus($$foo.getName()));
            };
            //AttributeDeclaration string at misc.ceylon (15:4-15:57)
            $$foo.getString=function getString(){
                return this.string$2947;
            };
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
    Foo($$$cl2243.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl2243.initTypeProto(Bar,'misc::Bar',Foo,$init$X());
        (function($$bar){
            
            //MethodDefinition printName at misc.ceylon (20:4-24:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl2243.print($$$cl2243.String("bar name = ",11).plus($$bar.getName()));
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
            };
            //ClassDefinition Inner at misc.ceylon (25:4-31:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$bar=this;
                $$$cl2243.print($$$cl2243.String("creating inner class of :",25).plus($$inner$Bar.$$bar.getName()));
                return $$inner$Bar;
            }
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl2243.Basic);
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
function printBoth(x$2949,y$2950){
    $$$cl2243.print(x$2949.plus($$$cl2243.String(", ",2)).plus(y$2950));
};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$2951){
    f$2951();
    f$2951();
};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob(){
    var $$foob=new foob.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$foob.name$2952=$$$cl2243.String("Gavin",5);
    return $$foob;
}
function $init$foob(){
    if (foob.$$===undefined){
        $$$cl2243.initTypeProto(foob,'misc::foob',$$$cl2243.Basic);
    }
    return foob;
}
exports.$init$foob=$init$foob;
$init$foob();
(function($$foob){
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    $$foob.getName=function getName(){
        return this.name$2952;
    };
})(foob.$$.prototype);
var foob$2953=foob();
var getFoob=function(){
    return foob$2953;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$2954){
    if(strings$2954===undefined){strings$2954=$$$cl2243.getEmpty();}
};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$2955, $$f){return Foo(name$2955,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$2956, b$2957, c$2958, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl2243.Null},Element:{t:$$$cl2243.Integer}};
    $$testObjects.a$2956=a$2956;
    $$testObjects.b$2957=b$2957;
    $$testObjects.c$2958=c$2958;
    $$$cl2243.Iterable($$testObjects);
    $$$cl2243.add_type_arg($$testObjects,'Absent',{t:$$$cl2243.Null});
    $$$cl2243.add_type_arg($$testObjects,'Element',{t:$$$cl2243.Integer});
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl2243.initTypeProto(TestObjects,'misc::TestObjects',$$$cl2243.Basic,$$$cl2243.Iterable);
        (function($$testObjects){
            
            //AttributeGetterDefinition iterator at objects.ceylon (4:2-16:2)
            $$testObjects.getIterator=function getIterator(){
                var $$testObjects=this;
                
                //ObjectDefinition iter at objects.ceylon (5:4-14:4)
                function iter$2959($$targs$$){
                    var $$iter$2959=new iter$2959.$$;
                    $$iter$2959.$$targs$$=$$targs$$;
                    $$$cl2243.Iterator($$iter$2959);
                    $$$cl2243.add_type_arg($$iter$2959,'Element',{t:$$$cl2243.Integer});
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$iter$2959.index$2960=(0);
                    return $$iter$2959;
                }
                function $init$iter$2959(){
                    if (iter$2959.$$===undefined){
                        $$$cl2243.initTypeProto(iter$2959,'misc::TestObjects.iterator.iter',$$$cl2243.Basic,$$$cl2243.Iterator);
                    }
                    return iter$2959;
                }
                $init$iter$2959();
                (function($$iter$2959){
                    
                    //AttributeDeclaration index at objects.ceylon (6:6-6:30)
                    $$iter$2959.getIndex$2960=function getIndex$2960(){
                        return this.index$2960;
                    };
                    $$iter$2959.setIndex$2960=function setIndex$2960(index$2961){
                        return this.index$2960=index$2961;
                    };
                    
                    //MethodDefinition next at objects.ceylon (7:6-13:6)
                    $$iter$2959.next=function next(){
                        var $$iter$2959=this;
                        (oldindex$2962=$$iter$2959.getIndex$2960(),$$iter$2959.setIndex$2960(oldindex$2962.getSuccessor()),oldindex$2962);
                        var oldindex$2962;
                        if($$iter$2959.getIndex$2960().equals((1))){
                            return $$testObjects.a$2956;
                        }else {
                            if($$iter$2959.getIndex$2960().equals((2))){
                                return $$testObjects.b$2957;
                            }else {
                                if($$iter$2959.getIndex$2960().equals((3))){
                                    return $$testObjects.c$2958;
                                }
                            }
                        }
                        return $$$cl2243.getFinished();
                    };
                })(iter$2959.$$.prototype);
                var iter$2963=iter$2959({Element:{t:$$$cl2243.Integer}});
                var getIter$2963=function(){
                    return iter$2963;
                }
                return getIter$2963();
            };
        })(TestObjects.$$.prototype);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl2243.print($$$cl2243.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:40)
    var t1$2964=TestObjects((1),(2),(3)).getIterator();
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:40)
    var t2$2965=TestObjects((1),(2),(3)).getIterator();
    var i$2966;
    if($$$cl2243.isOfType((i$2966=t1$2964.next()),{t:$$$cl2243.Integer})){
        $$$c2244.check(i$2966.equals((1)),$$$cl2243.String("objects 1",9));
    }
    var i$2967;
    if($$$cl2243.isOfType((i$2967=t1$2964.next()),{t:$$$cl2243.Integer})){
        $$$c2244.check(i$2967.equals((2)),$$$cl2243.String("objects 2",9));
    }
    var i$2968;
    if($$$cl2243.isOfType((i$2968=t2$2965.next()),{t:$$$cl2243.Integer})){
        $$$c2244.check(i$2968.equals((1)),$$$cl2243.String("objects 3",9));
    }
    var i$2969;
    if($$$cl2243.isOfType((i$2969=t1$2964.next()),{t:$$$cl2243.Integer})){
        $$$c2244.check(i$2969.equals((3)),$$$cl2243.String("objects 4",9));
    }
    $$$c2244.check($$$cl2243.isOfType(t1$2964.next(),{t:$$$cl2243.Finished}),$$$cl2243.String("objects 5",9));
    var i$2970;
    if($$$cl2243.isOfType((i$2970=t2$2965.next()),{t:$$$cl2243.Integer})){
        $$$c2244.check(i$2970.equals((2)),$$$cl2243.String("objects 6",9));
    }
    var i$2971;
    if($$$cl2243.isOfType((i$2971=t2$2965.next()),{t:$$$cl2243.Integer})){
        $$$c2244.check(i$2971.equals((3)),$$$cl2243.String("objects 7",9));
    }
};
var $$$m2972=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-43:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$2973=$$$cl2243.String("hello",5);
    $$$cl2243.print(name$2973);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$2974=Foo($$$cl2243.String("goodbye",7));
    printBoth(name$2973,foo$2974.getName());
    (y$2975=$$$cl2243.String("y",1),x$2976=$$$cl2243.String("x",1),printBoth(x$2976,y$2975));
    var y$2975,x$2976;
    foo$2974.inc();
    foo$2974.inc();
    $$$c2244.check(foo$2974.getCount().equals((3)),$$$cl2243.String("Foo.count",9));
    $$$c2244.check(foo$2974.getString().equals($$$cl2243.String("Foo(goodbye)",12)),$$$cl2243.String("Foo.string",10));
    foo$2974.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((opt$2977=foo$2974,$$$cl2243.JsCallable(opt$2977,opt$2977!==null?opt$2977.inc:null)));
    var opt$2977;
    $$$c2244.check(foo$2974.getCount().equals((5)),$$$cl2243.String("Foo.count [2]",13));
    doIt(Bar);
    $$$cl2243.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$2978(){
        var $$x$2978=new x$2978.$$;
        return $$x$2978;
    }
    function $init$x$2978(){
        if (x$2978.$$===undefined){
            $$$cl2243.initTypeProto(x$2978,'misc::test.x',$$$cl2243.Basic);
        }
        return x$2978;
    }
    $init$x$2978();
    (function($$x$2978){
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        $$x$2978.y=function y(){
            var $$x$2978=this;
            $$$cl2243.print($$$cl2243.String("xy",2));
        };
    })(x$2978.$$.prototype);
    var x$2979=x$2978();
    var getX$2979=function(){
        return x$2979;
    }
    getX$2979().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$2980=Bar();
    b$2980.Inner$Bar().incOuter();
    b$2980.Inner$Bar().incOuter();
    b$2980.Inner$Bar().incOuter();
    $$$c2244.check(b$2980.getCount().equals((4)),$$$cl2243.String("Bar.count",9));
    printAll([$$$cl2243.String("hello",5),$$$cl2243.String("world",5)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}));
    (strings$2981=$$$cl2243.Tuple($$$cl2243.String("hello",5),$$$cl2243.Tuple($$$cl2243.String("world",5),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),printAll(strings$2981));
    var strings$2981;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$2982=$$$m2972.Counter((0));
    c$2982.inc();
    c$2982.inc();
    $$$c2244.check(c$2982.getCount().equals((2)),$$$cl2243.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$2983=$var();
    test_objects();
    testAliasing();
    $$$c2244.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
