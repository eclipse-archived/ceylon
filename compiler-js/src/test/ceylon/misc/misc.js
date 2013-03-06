(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$pt":"v","$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$at":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    
    //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
    function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
        
        //MethodDefinition aliasingIface at aliases.ceylon (7:8-7:54)
        function aliasingIface(){
            return true;
        }
        $$aliasingIface$AliasingClass.aliasingIface=aliasingIface;
    }
    $$aliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
    function $init$AliasingIface$AliasingClass(){
        if (AliasingIface$AliasingClass.$$===undefined){
            $$$cl1.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
        }
        return AliasingIface$AliasingClass;
    }
    $$aliasingClass.$init$AliasingIface$AliasingClass=$init$AliasingIface$AliasingClass;
    $init$AliasingIface$AliasingClass();
    
    //ClassDefinition AliasingInner at aliases.ceylon (9:4-11:4)
    function AliasingInner$AliasingClass($$aliasingInner$AliasingClass){
        $init$AliasingInner$AliasingClass();
        if ($$aliasingInner$AliasingClass===undefined)$$aliasingInner$AliasingClass=new AliasingInner$AliasingClass.$$;
        
        //MethodDefinition aliasingInner at aliases.ceylon (10:8-10:54)
        function aliasingInner(){
            return true;
        }
        $$aliasingInner$AliasingClass.aliasingInner=aliasingInner;
        return $$aliasingInner$AliasingClass;
    }
    $$aliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
    function $init$AliasingInner$AliasingClass(){
        if (AliasingInner$AliasingClass.$$===undefined){
            $$$cl1.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl1.Basic);
        }
        return AliasingInner$AliasingClass;
    }
    $$aliasingClass.$init$AliasingInner$AliasingClass=$init$AliasingInner$AliasingClass;
    $init$AliasingInner$AliasingClass();
    return $$aliasingClass;
}
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl1.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl1.Basic);
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
    
    //ClassDeclaration InnerAlias at aliases.ceylon (15:4-15:48)
    function InnerAlias$AliasingSubclass($$innerAlias$AliasingSubclass){return $$aliasingSubclass.AliasingInner$AliasingClass($$innerAlias$AliasingSubclass);}
    InnerAlias$AliasingSubclass.$$=$$aliasingSubclass.AliasingInner$AliasingClass.$$;
    $$aliasingSubclass.InnerAlias$AliasingSubclass=InnerAlias$AliasingSubclass;
    
    //ClassDefinition SubAlias at aliases.ceylon (16:4-16:50)
    function SubAlias$AliasingSubclass($$subAlias$AliasingSubclass){
        $init$SubAlias$AliasingSubclass();
        if ($$subAlias$AliasingSubclass===undefined)$$subAlias$AliasingSubclass=new SubAlias$AliasingSubclass.$$;
        $$aliasingSubclass.InnerAlias$AliasingSubclass($$subAlias$AliasingSubclass);
        return $$subAlias$AliasingSubclass;
    }
    $$aliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
    function $init$SubAlias$AliasingSubclass(){
        if (SubAlias$AliasingSubclass.$$===undefined){
            $$$cl1.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
        }
        return SubAlias$AliasingSubclass;
    }
    $$aliasingSubclass.$init$SubAlias$AliasingSubclass=$init$SubAlias$AliasingSubclass;
    $init$SubAlias$AliasingSubclass();
    
    //MethodDefinition aliasingSubclass at aliases.ceylon (18:4-20:4)
    function aliasingSubclass(){
        return $$aliasingSubclass.SubAlias$AliasingSubclass().aliasingInner();
    }
    $$aliasingSubclass.aliasingSubclass=aliasingSubclass;
    
    //InterfaceDeclaration AliasedIface at aliases.ceylon (21:4-21:50)
    var AliasedIface$AliasingSubclass=$$aliasingSubclass.AliasingIface$AliasingClass;
    $$aliasingSubclass.AliasedIface$AliasingSubclass=AliasedIface$AliasingSubclass;
    return $$aliasingSubclass;
}
function $init$AliasingSubclass(){
    if (AliasingSubclass.$$===undefined){
        $$$cl1.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
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
    
    //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
    $$$cl1.defineAttr($$aliasingSub2,'iface',function(){
        
        //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
        function aliased$774(){
            var $$aliased$774=new aliased$774.$$;
            $$aliasingSub2.AliasingIface$AliasingClass($$aliased$774);
            return $$aliased$774;
        }
        function $init$aliased$774(){
            if (aliased$774.$$===undefined){
                $$$cl1.initTypeProto(aliased$774,'misc::AliasingSub2.iface.aliased',$$$cl1.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
            }
            return aliased$774;
        }
        $init$aliased$774();
        var aliased$775=aliased$774();
        var getAliased$775=function(){
            return aliased$775;
        }
        return getAliased$775();
    });
    return $$aliasingSub2;
}
function $init$AliasingSub2(){
    if (AliasingSub2.$$===undefined){
        $$$cl1.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$776, $$targs$$,$$listleton){return $$$cl1.Singleton(l$776,{Element:{t:$$$cl1.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl1.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$777, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl1.Integer}};
    $$$cl1.Sequence($$miMatrix);
    $$$cl1.add_type_arg($$miMatrix,'Cell',{t:$$$cl1.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    var sb$778=$$$cl1.SequenceBuilder({Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$779 = $$$cl1.Range((1),gridSize$777,{Element:{t:$$$cl1.Integer}}).iterator;
    var i$780;while ((i$780=it$779.next())!==$$$cl1.getFinished()){
        sb$778.append($$$cl1.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$781=$$$cl1.Range((1),gridSize$777,{Element:{t:$$$cl1.Integer}}).iterator;
            var j$782=$$$cl1.getFinished();
            var next$j$782=function(){return j$782=it$781.next();}
            next$j$782();
            return function(){
                if(j$782!==$$$cl1.getFinished()){
                    var j$782$783=j$782;
                    var tmpvar$784=j$782$783;
                    next$j$782();
                    return tmpvar$784;
                }
                return $$$cl1.getFinished();
            }
        },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).sequence);
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$785;
    if($$$cl1.nonempty((g$785=sb$778.sequence))){
        var grid$786=g$785;
        $$$cl1.defineAttr($$miMatrix,'grid$786',function(){return grid$786;});
    }else {
        var grid$786=$$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}});
        $$$cl1.defineAttr($$miMatrix,'grid$786',function(){return grid$786;});
    }
    
    //AttributeGetterDefinition iterator at aliases.ceylon (46:4-46:72)
    $$$cl1.defineAttr($$miMatrix,'iterator',function(){
        return grid$786.iterator;
    });
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    var string=grid$786.string;
    $$$cl1.defineAttr($$miMatrix,'string',function(){return string;});
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    var hash=grid$786.hash;
    $$$cl1.defineAttr($$miMatrix,'hash',function(){return hash;});
    
    //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
    var equals=function (other$787){
        return grid$786.equals(other$787);
    };
    $$miMatrix.equals=equals;
    var span=(opt$788=grid$786,$$$cl1.JsCallable(opt$788,opt$788!==null?opt$788.span:null));
    $$miMatrix.span=span;
    var opt$788;
    var segment=(opt$789=grid$786,$$$cl1.JsCallable(opt$789,opt$789!==null?opt$789.segment:null));
    $$miMatrix.segment=segment;
    var opt$789;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    var reversed=grid$786.reversed;
    $$$cl1.defineAttr($$miMatrix,'reversed',function(){return reversed;});
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    var lastIndex=grid$786.lastIndex;
    $$$cl1.defineAttr($$miMatrix,'lastIndex',function(){return lastIndex;});
    
    //MethodDeclaration get at aliases.ceylon (54:4-54:55)
    var get=function (i$790){
        return grid$786.get(i$790);
    };
    $$miMatrix.get=get;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    var rest=grid$786.rest;
    $$$cl1.defineAttr($$miMatrix,'rest',function(){return rest;});
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    var first=grid$786.first;
    $$$cl1.defineAttr($$miMatrix,'first',function(){return first;});
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    $$$cl1.defineAttr($$miMatrix,'clone',function(){return $$miMatrix;});
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    $$$cl1.defineAttr($$miMatrix,'size',function(){return grid$786.size;});
    
    //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
    var contains=function (other$791){
        return grid$786.contains(other$791);
    };
    $$miMatrix.contains=contains;
    $$$cl1.defineAttr($$miMatrix,'last',function(){
        return grid$786.last;
    });
    
    //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
    var spanTo=function (to$792){
        return (opt$793=(to$792.compare((0)).equals($$$cl1.getSmaller())?$$$cl1.getEmpty():null),opt$793!==null?opt$793:$$miMatrix.span((0),to$792));
    };
    $$miMatrix.spanTo=spanTo;
    var opt$793;
    
    //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
    var spanFrom=function (from$794){
        return $$miMatrix.span(from$794,$$miMatrix.size);
    };
    $$miMatrix.spanFrom=spanFrom;
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl1.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl1.Basic,$$$cl1.Sequence);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl1.print($$$cl1.String("testing type aliases",20));
    $$$c2.check(AliasingSubclass().aliasingSubclass(),$$$cl1.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$795($$innerSubalias$795){return AliasingSubclass($$innerSubalias$795);}
    InnerSubalias$795.$$=AliasingSubclass.$$;
    $$$c2.check(InnerSubalias$795().aliasingSubclass(),$$$cl1.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$796(aif$797){
        return aif$797.aliasingIface();
    };
    $$$c2.check(use$796(AliasingSub2().iface),$$$cl1.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$798=(5);
    $$$c2.check($$$cl1.isOfType(xxxxx$798,{t:$$$cl1.Integer}),$$$cl1.String("Type alias",10));
    $$$c2.check(Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}).first,{T:{t:$$$cl1.Integer}}).string.equals($$$cl1.String("[[1]]",5)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("class alias ",12),Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}).first,{T:{t:$$$cl1.Integer}}).string,$$$cl1.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2.check(MiMatrix((2)).string.equals($$$cl1.String("{ { 1, 2 }, { 1, 2 } }",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("interface alias ",16),MiMatrix((2)).string,$$$cl1.String(" instead of { { 1, 2 }, { 1, 2 } }",34)]).string);
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$799=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$800=$$$cl1.String("XXXX",4);
    $$$c2.check($$$cl1.isOfType(xxxxx1$799,{ t:'u', l:[{t:$$$cl1.String},{t:$$$cl1.Integer}]}),$$$cl1.String("is String|Integer",17));
    $$$c2.check($$$cl1.isOfType(xxxxx2$800,{ t:'i', l:[{t:$$$cl1.String},{t:$$$cl1.List,a:{Element:{t:$$$cl1.Anything}}}]}),$$$cl1.String("is String&List",14));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$801=function (bits$802){
        if(bits$802===undefined){bits$802=$$$cl1.getEmpty();}
        return $$$cl1.any(bits$802);
    };
    $$$c2.check(cualquiera$801([true,true,true].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.true$803}})),$$$cl1.String("seq arg method alias",20));
};

//ClassDefinition LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
    var parent;
    $$$cl1.defineAttr($$lateTestChild,'parent',function(){if (parent===undefined)throw $$$cl1.InitializationException($$$cl1.String('Attempt to read unitialized attribute «parent»'));return parent;},function(parent$804){if(parent!==undefined)throw $$$cl1.InitializationException($$$cl1.String('Attempt to reassign immutable attribute «parent»'));return parent=parent$804;});
    return $$lateTestChild;
}
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl1.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl1.Basic);
    }
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDefinition LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children$805, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children$805===undefined){children$805=$$$cl1.getEmpty();}
    
    //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
    var children=children$805;
    $$$cl1.defineAttr($$lateTestParent,'children',function(){return children;});
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$806 = $$lateTestParent.children.iterator;
    var child$807;while ((child$807=it$806.next())!==$$$cl1.getFinished()){
        (child$807.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl1.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl1.Basic);
    }
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDefinition testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDeclaration kids at late_support.ceylon (15:4-15:51)
    var kids$808=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$808);
    try{
        LateTestParent(kids$808);
        $$$c2.fail($$$cl1.String("reassigning to late attribute should fail",41));
    }catch(ex$809){
        if (ex$809.getT$name === undefined) ex$809=$$$cl1.NativeException(ex$809);
        if($$$cl1.isOfType(ex$809,{t:$$$cl1.InitializationException})){
            $$$c2.check(true);
        }
        else if($$$cl1.isOfType(ex$809,{t:$$$cl1.Exception})){
            $$$c2.fail($$$cl1.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$809}
    }
    try{
        $$$cl1.print(LateTestChild().parent);
        $$$c2.fail($$$cl1.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$810){
        if (ex$810.getT$name === undefined) ex$810=$$$cl1.NativeException(ex$810);
        if($$$cl1.isOfType(ex$810,{t:$$$cl1.InitializationException})){
            $$$c2.check(true);
        }
        else if($$$cl1.isOfType(ex$810,{t:$$$cl1.Exception})){
            $$$c2.fail($$$cl1.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$810}
    }
};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
    
    //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
    function helloWorld(){
        $$$cl1.print($$$cl1.String("hello world",11));
    }
    $$x.helloWorld=helloWorld;
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl1.initTypeProto(X,'misc::X');
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$811, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    var name=name$811;
    $$$cl1.defineAttr($$foo,'name',function(){return name;});
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    var counter$812=(0);
    $$$cl1.defineAttr($$foo,'counter$812',function(){return counter$812;},function(counter$813){return counter$812=counter$813;});
    
    //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
    $$$cl1.defineAttr($$foo,'count',function(){
        return counter$812;
    });
    
    //MethodDefinition inc at misc.ceylon (11:4-11:43)
    function inc(){
        counter$812=counter$812.plus((1));
    }
    $$foo.inc=inc;
    
    //MethodDefinition printName at misc.ceylon (12:4-14:4)
    function printName(){
        $$$cl1.print($$$cl1.String("foo name = ",11).plus($$foo.name));
    }
    $$foo.printName=printName;
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    var string=$$$cl1.StringBuilder().appendAll([$$$cl1.String("Foo(",4),$$foo.name.string,$$$cl1.String(")",1)]).string;
    $$$cl1.defineAttr($$foo,'string',function(){return string;});
    $$foo.inc();
    return $$foo;
}
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl1.initTypeProto(Foo,'misc::Foo',$$$cl1.Basic);
    }
    return Foo;
}
exports.$init$Foo=$init$Foo;
$init$Foo();

//ClassDefinition Bar at misc.ceylon (19:0-34:0)
function Bar($$bar){
    $init$Bar();
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl1.String("Hello",5),$$bar);
    $$bar.printName$$misc$Foo=$$bar.printName;
    $$bar.printName$$misc$Foo=$$bar.printName;
    X($$bar);
    
    //MethodDefinition printName at misc.ceylon (20:4-24:4)
    function printName(){
        $$$cl1.print($$$cl1.String("bar name = ",11).plus($$bar.name));
        $$bar.printName$$misc$Foo();
        $$bar.printName$$misc$Foo();
    }
    $$bar.printName=printName;
    
    //ClassDefinition Inner at misc.ceylon (25:4-31:4)
    function Inner$Bar($$inner$Bar){
        $init$Inner$Bar();
        if ($$inner$Bar===undefined)$$inner$Bar=new Inner$Bar.$$;
        $$$cl1.print($$$cl1.String("creating inner class of :",25).plus($$bar.name));
        
        //MethodDefinition incOuter at misc.ceylon (28:8-30:8)
        function incOuter(){
            $$bar.inc();
        }
        $$inner$Bar.incOuter=incOuter;
        return $$inner$Bar;
    }
    $$bar.Inner$Bar=Inner$Bar;
    function $init$Inner$Bar(){
        if (Inner$Bar.$$===undefined){
            $$$cl1.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl1.Basic);
        }
        return Inner$Bar;
    }
    $$bar.$init$Inner$Bar=$init$Inner$Bar;
    $init$Inner$Bar();
    return $$bar;
}
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl1.initTypeProto(Bar,'misc::Bar',Foo,$init$X());
    }
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDefinition printBoth at misc.ceylon (36:0-38:0)
function printBoth(x$814,y$815){
    $$$cl1.print(x$814.plus($$$cl1.String(", ",2)).plus(y$815));
};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$816){
    f$816();
    f$816();
};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob$817(){
    var $$foob=new foob$817.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    var name=$$$cl1.String("Gavin",5);
    $$$cl1.defineAttr($$foob,'name',function(){return name;});
    return $$foob;
}
function $init$foob$817(){
    if (foob$817.$$===undefined){
        $$$cl1.initTypeProto(foob$817,'misc::foob',$$$cl1.Basic);
    }
    return foob$817;
}
exports.$init$foob$817=$init$foob$817;
$init$foob$817();
var foob$818=foob$817();
var getFoob=function(){
    return foob$818;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$819){
    if(strings$819===undefined){strings$819=$$$cl1.getEmpty();}
};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$820, $$f){return Foo(name$820,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$821, b$822, c$823, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}};
    $$testObjects.a$821=a$821;
    $$testObjects.b$822=b$822;
    $$testObjects.c$823=c$823;
    $$$cl1.Iterable($$testObjects);
    $$$cl1.add_type_arg($$testObjects,'Absent',{t:$$$cl1.Null});
    $$$cl1.add_type_arg($$testObjects,'Element',{t:$$$cl1.Integer});
    
    //AttributeGetterDefinition iterator at objects.ceylon (4:2-16:2)
    $$$cl1.defineAttr($$testObjects,'iterator',function(){
        
        //ObjectDefinition iter at objects.ceylon (5:4-14:4)
        function iter$824($$targs$$){
            var $$iter$824=new iter$824.$$;
            $$iter$824.$$targs$$=$$targs$$;
            $$$cl1.Iterator($$iter$824);
            $$$cl1.add_type_arg($$iter$824,'Element',{t:$$$cl1.Integer});
            
            //AttributeDeclaration index at objects.ceylon (6:6-6:30)
            var index$825=(0);
            $$$cl1.defineAttr($$iter$824,'index$825',function(){return index$825;},function(index$826){return index$825=index$826;});
            
            //MethodDefinition next at objects.ceylon (7:6-13:6)
            function next(){
                (oldindex$827=index$825,index$825=oldindex$827.successor,oldindex$827);
                var oldindex$827;
                if(index$825.equals((1))){
                    return a$821;
                }else {
                    if(index$825.equals((2))){
                        return b$822;
                    }else {
                        if(index$825.equals((3))){
                            return c$823;
                        }
                    }
                }
                return $$$cl1.getFinished();
            }
            $$iter$824.next=next;
            return $$iter$824;
        }
        function $init$iter$824(){
            if (iter$824.$$===undefined){
                $$$cl1.initTypeProto(iter$824,'misc::TestObjects.iterator.iter',$$$cl1.Basic,$$$cl1.Iterator);
            }
            return iter$824;
        }
        $init$iter$824();
        var iter$828=iter$824({Element:{t:$$$cl1.Integer}});
        var getIter$828=function(){
            return iter$828;
        }
        return getIter$828();
    });
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl1.initTypeProto(TestObjects,'misc::TestObjects',$$$cl1.Basic,$$$cl1.Iterable);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl1.print($$$cl1.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:40)
    var t1$829=TestObjects((1),(2),(3)).iterator;
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:40)
    var t2$830=TestObjects((1),(2),(3)).iterator;
    var i$831;
    if($$$cl1.isOfType((i$831=t1$829.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$831.equals((1)),$$$cl1.String("objects 1",9));
    }
    var i$832;
    if($$$cl1.isOfType((i$832=t1$829.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$832.equals((2)),$$$cl1.String("objects 2",9));
    }
    var i$833;
    if($$$cl1.isOfType((i$833=t2$830.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$833.equals((1)),$$$cl1.String("objects 3",9));
    }
    var i$834;
    if($$$cl1.isOfType((i$834=t1$829.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$834.equals((3)),$$$cl1.String("objects 4",9));
    }
    $$$c2.check($$$cl1.isOfType(t1$829.next(),{t:$$$cl1.Finished}),$$$cl1.String("objects 5",9));
    var i$835;
    if($$$cl1.isOfType((i$835=t2$830.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$835.equals((2)),$$$cl1.String("objects 6",9));
    }
    var i$836;
    if($$$cl1.isOfType((i$836=t2$830.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$836.equals((3)),$$$cl1.String("objects 7",9));
    }
};
var $$$m837=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-44:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$838=$$$cl1.String("hello",5);
    $$$cl1.print(name$838);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$839=Foo($$$cl1.String("goodbye",7));
    printBoth(name$838,foo$839.name);
    (y$840=$$$cl1.String("y",1),x$841=$$$cl1.String("x",1),printBoth(x$841,y$840));
    var y$840,x$841;
    foo$839.inc();
    foo$839.inc();
    $$$c2.check(foo$839.count.equals((3)),$$$cl1.String("Foo.count",9));
    $$$c2.check(foo$839.string.equals($$$cl1.String("Foo(goodbye)",12)),$$$cl1.String("Foo.string",10));
    foo$839.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((opt$842=foo$839,$$$cl1.JsCallable(opt$842,opt$842!==null?opt$842.inc:null)));
    var opt$842;
    $$$c2.check(foo$839.count.equals((5)),$$$cl1.String("Foo.count [2]",13));
    doIt(Bar);
    $$$cl1.print(getFoob().name);
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$843(){
        var $$x$843=new x$843.$$;
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        function y(){
            $$$cl1.print($$$cl1.String("xy",2));
        }
        $$x$843.y=y;
        return $$x$843;
    }
    function $init$x$843(){
        if (x$843.$$===undefined){
            $$$cl1.initTypeProto(x$843,'misc::test.x',$$$cl1.Basic);
        }
        return x$843;
    }
    $init$x$843();
    var x$844=x$843();
    var getX$844=function(){
        return x$844;
    }
    getX$844().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$845=Bar();
    b$845.Inner$Bar().incOuter();
    b$845.Inner$Bar().incOuter();
    b$845.Inner$Bar().incOuter();
    $$$c2.check(b$845.count.equals((4)),$$$cl1.String("Bar.count",9));
    printAll([$$$cl1.String("hello",5),$$$cl1.String("world",5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    (strings$846=$$$cl1.Tuple($$$cl1.String("hello",5),$$$cl1.Tuple($$$cl1.String("world",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),printAll(strings$846));
    var strings$846;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$847=$$$m837.Counter((0));
    c$847.inc();
    c$847.inc();
    $$$c2.check(c$847.count.equals((2)),$$$cl1.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$848=$var();
    test_objects();
    testAliasing();
    testLate();
    $$$c2.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
