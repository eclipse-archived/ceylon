(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$pt":"v","$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"Bivariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"In"},{"variance":"out","$nm":"Out"}],"$an":{"shared":[]},"$nm":"Bivariant"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"},"Container":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Outer"}],"$an":{"shared":[]},"$c":{"Member":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Inner"}],"$an":{"shared":[]},"$c":{"Child":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"InnerMost"}],"$an":{"shared":[]},"$nm":"Child"}},"$nm":"Member"}},"$nm":"Container"},"Covariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Covariant"},"runtimeMethod":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"param"}]],"$mt":"mthd","$nm":"runtimeMethod"},"Top1":{"$mt":"ifc","$an":{"shared":[]},"$nm":"Top1"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"Middle1":{"satisfies":[{"$pk":"misc","$nm":"Top1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Middle1"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"Invariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Element"}],"$an":{"shared":[]},"$nm":"Invariant"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"Contravariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Contravariant"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"testReifiedRuntime":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testReifiedRuntime"},"Bottom1":{"satisfies":[{"$pk":"misc","$nm":"Middle1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Bottom1"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"}}}
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2=require('check/0.1/check-0.1');

//ClassDefinition AliasingClass at aliases.ceylon (5:0-12:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    
    //InterfaceDefinition AliasingIface at aliases.ceylon (6:4-8:4)
    function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
        $$aliasingIface$AliasingClass.$$outer=this;
        
        //MethodDefinition aliasingIface at aliases.ceylon (7:8-7:54)
        function aliasingIface(){
            return true;
        }
        $$aliasingIface$AliasingClass.aliasingIface=aliasingIface;
        aliasingIface.$$metamodel$$={$nm:'aliasingIface',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[]};//aliasingIface.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Boolean}};
    }
    $$aliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
    function $init$AliasingIface$AliasingClass(){
        if (AliasingIface$AliasingClass.$$===undefined){
            $$$cl1.initTypeProto(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
            AliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
        }
        AliasingIface$AliasingClass.$$.$$metamodel$$={$nm:'AliasingIface',$mt:'ifc','satisfies':[]};
        return AliasingIface$AliasingClass;
    }
    $$aliasingClass.$init$AliasingIface$AliasingClass=$init$AliasingIface$AliasingClass;
    $init$AliasingIface$AliasingClass();
    
    //ClassDefinition AliasingInner at aliases.ceylon (9:4-11:4)
    function AliasingInner$AliasingClass($$aliasingInner$AliasingClass){
        $init$AliasingInner$AliasingClass();
        if ($$aliasingInner$AliasingClass===undefined)$$aliasingInner$AliasingClass=new AliasingInner$AliasingClass.$$;
        $$aliasingInner$AliasingClass.$$outer=this;
        
        //MethodDefinition aliasingInner at aliases.ceylon (10:8-10:54)
        function aliasingInner(){
            return true;
        }
        $$aliasingInner$AliasingClass.aliasingInner=aliasingInner;
        aliasingInner.$$metamodel$$={$nm:'aliasingInner',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[]};//aliasingInner.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Boolean}};
        return $$aliasingInner$AliasingClass;
    }
    $$aliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
    function $init$AliasingInner$AliasingClass(){
        if (AliasingInner$AliasingClass.$$===undefined){
            $$$cl1.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl1.Basic);
            AliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
        }
        AliasingInner$AliasingClass.$$.$$metamodel$$={$nm:'AliasingInner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
    AliasingClass.$$.$$metamodel$$={$nm:'AliasingClass',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
        $$subAlias$AliasingSubclass.$$outer=this;
        $$aliasingSubclass.InnerAlias$AliasingSubclass($$subAlias$AliasingSubclass);
        return $$subAlias$AliasingSubclass;
    }
    $$aliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
    function $init$SubAlias$AliasingSubclass(){
        if (SubAlias$AliasingSubclass.$$===undefined){
            $$$cl1.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
            AliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
        }
        SubAlias$AliasingSubclass.$$.$$metamodel$$={$nm:'SubAlias',$mt:'cls','super':{t:AliasingClass.AliasingInner$AliasingClass},'satisfies':[]};
        return SubAlias$AliasingSubclass;
    }
    $$aliasingSubclass.$init$SubAlias$AliasingSubclass=$init$SubAlias$AliasingSubclass;
    $init$SubAlias$AliasingSubclass();
    
    //MethodDefinition aliasingSubclass at aliases.ceylon (18:4-20:4)
    function aliasingSubclass(){
        return $$aliasingSubclass.SubAlias$AliasingSubclass().aliasingInner();
    }
    $$aliasingSubclass.aliasingSubclass=aliasingSubclass;
    aliasingSubclass.$$metamodel$$={$nm:'aliasingSubclass',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[]};//aliasingSubclass.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Boolean}};
    
    //InterfaceDeclaration AliasedIface at aliases.ceylon (21:4-21:50)
    var AliasedIface$AliasingSubclass=$$aliasingSubclass.AliasingIface$AliasingClass;
    $$aliasingSubclass.AliasedIface$AliasingSubclass=AliasedIface$AliasingSubclass;
    return $$aliasingSubclass;
}
function $init$AliasingSubclass(){
    if (AliasingSubclass.$$===undefined){
        $$$cl1.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',AliasingClass);
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
    
    //AttributeGetterDefinition iface at aliases.ceylon (25:4-29:4)
    $$$cl1.defineAttr($$aliasingSub2,'iface',function(){
        
        //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
        function aliased$780(){
            var $$aliased$780=new aliased$780.$$;
            $$aliasingSub2.AliasingIface$AliasingClass($$aliased$780);
            return $$aliased$780;
        }
        function $init$aliased$780(){
            if (aliased$780.$$===undefined){
                $$$cl1.initTypeProto(aliased$780,'misc::AliasingSub2.iface.aliased',$$$cl1.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
            }
            aliased$780.$$.$$metamodel$$={$nm:'aliased',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:AliasingClass.AliasingIface$AliasingClass}]};
            return aliased$780;
        }
        $init$aliased$780();
        var aliased$781=aliased$780();
        var getAliased$781=function(){
            return aliased$781;
        }
        return getAliased$781();
    });
    return $$aliasingSub2;
}
function $init$AliasingSub2(){
    if (AliasingSub2.$$===undefined){
        $$$cl1.initTypeProto(AliasingSub2,'misc::AliasingSub2',AliasingSubclass);
    }
    AliasingSub2.$$.$$metamodel$$={$nm:'AliasingSub2',$mt:'cls','super':{t:AliasingSubclass},'satisfies':[]};
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//ClassDeclaration Listleton at aliases.ceylon (33:0-33:54)
function Listleton(l$782, $$targs$$,$$listleton){return $$$cl1.Singleton(l$782,{Element:{t:$$$cl1.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl1.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$783, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl1.Integer}};
    $$$cl1.Sequence($$miMatrix);
    $$$cl1.add_type_arg($$miMatrix,'Cell',{t:$$$cl1.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    var sb$784=$$$cl1.SequenceBuilder({Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$785 = $$$cl1.Range((1),gridSize$783,{Element:{t:$$$cl1.Integer}}).iterator();
    var i$786;while ((i$786=it$785.next())!==$$$cl1.getFinished()){
        sb$784.append($$$cl1.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$787=$$$cl1.Range((1),gridSize$783,{Element:{t:$$$cl1.Integer}}).iterator();
            var j$788=$$$cl1.getFinished();
            var next$j$788=function(){return j$788=it$787.next();}
            next$j$788();
            return function(){
                if(j$788!==$$$cl1.getFinished()){
                    var j$788$789=j$788;
                    var tmpvar$790=j$788$789;
                    next$j$788();
                    return tmpvar$790;
                }
                return $$$cl1.getFinished();
            }
        },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).sequence);
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$791;
    if($$$cl1.nonempty((g$791=sb$784.sequence))){
        var grid$792=g$791;
        $$$cl1.defineAttr($$miMatrix,'grid$792',function(){return grid$792;});
    }else {
        var grid$792=$$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}});
        $$$cl1.defineAttr($$miMatrix,'grid$792',function(){return grid$792;});
    }
    
    //MethodDefinition iterator at aliases.ceylon (46:4-46:76)
    function iterator(){
        return grid$792.iterator();
    }
    $$miMatrix.iterator=iterator;
    iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$ps:[]};//iterator.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}}};
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    var string=grid$792.string;
    $$$cl1.defineAttr($$miMatrix,'string',function(){return string;});
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    var hash=grid$792.hash;
    $$$cl1.defineAttr($$miMatrix,'hash',function(){return hash;});
    
    //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
    var equals=function (other$793){
        return grid$792.equals(other$793);
    };
    equals.$$metamodel$$={$nm:'equals',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object}}]};
    $$miMatrix.equals=equals;
    var span=$$$cl1.$JsCallable((opt$794=grid$792,$$$cl1.JsCallable(opt$794,opt$794!==null?opt$794.span:null)),[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}}});
    $$miMatrix.span=span;
    var opt$794;
    var segment=$$$cl1.$JsCallable((opt$795=grid$792,$$$cl1.JsCallable(opt$795,opt$795!==null?opt$795.segment:null)),[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'length',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}}});
    $$miMatrix.segment=segment;
    var opt$795;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    var reversed=grid$792.reversed;
    $$$cl1.defineAttr($$miMatrix,'reversed',function(){return reversed;});
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    var lastIndex=grid$792.lastIndex;
    $$$cl1.defineAttr($$miMatrix,'lastIndex',function(){return lastIndex;});
    
    //MethodDeclaration get at aliases.ceylon (54:4-54:55)
    var get=function (i$796){
        return grid$792.get(i$796);
    };
    get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}]},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$miMatrix.get=get;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    var rest=grid$792.rest;
    $$$cl1.defineAttr($$miMatrix,'rest',function(){return rest;});
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    var first=grid$792.first;
    $$$cl1.defineAttr($$miMatrix,'first',function(){return first;});
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    $$$cl1.defineAttr($$miMatrix,'clone',function(){return $$miMatrix;});
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    $$$cl1.defineAttr($$miMatrix,'size',function(){return grid$792.size;});
    
    //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
    var contains=function (other$797){
        return grid$792.contains(other$797);
    };
    contains.$$metamodel$$={$nm:'contains',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object}}]};
    $$miMatrix.contains=contains;
    $$$cl1.defineAttr($$miMatrix,'last',function(){
        return grid$792.last;
    });
    
    //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
    var spanTo=function (to$798){
        return (opt$799=(to$798.compare((0)).equals($$$cl1.getSmaller())?$$$cl1.getEmpty():null),opt$799!==null?opt$799:$$miMatrix.span((0),to$798));
    };
    spanTo.$$metamodel$$={$nm:'spanTo',$mt:'mthd',$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$miMatrix.spanTo=spanTo;
    var opt$799;
    
    //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
    var spanFrom=function (from$800){
        return $$miMatrix.span(from$800,$$miMatrix.size);
    };
    spanFrom.$$metamodel$$={$nm:'spanFrom',$mt:'mthd',$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$miMatrix.spanFrom=spanFrom;
    return $$miMatrix;
}
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl1.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl1.Basic,$$$cl1.Sequence);
    }
    MiMatrix.$$.$$metamodel$$={$nm:'MiMatrix',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:$$$cl1.Sequence,a:{Cell:{t:$$$cl1.Integer}}}]};
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();

//MethodDefinition testAliasing at aliases.ceylon (68:0-86:0)
function testAliasing(){
    $$$cl1.print($$$cl1.String("testing type aliases",20));
    $$$c2.check(AliasingSubclass().aliasingSubclass(),$$$cl1.String("Aliased member class",20));
    
    //ClassDeclaration InnerSubalias at aliases.ceylon (71:4-71:47)
    function InnerSubalias$801($$innerSubalias$801){return AliasingSubclass($$innerSubalias$801);}
    InnerSubalias$801.$$=AliasingSubclass.$$;
    $$$c2.check(InnerSubalias$801().aliasingSubclass(),$$$cl1.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$802(aif$803){
        return aif$803.aliasingIface();
    };use$802.$$metamodel$$={$nm:'use',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'aif',$mt:'prm',$t:{t:AliasingClass.AliasingIface$AliasingClass}}]};//use$802.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:AliasingClass.AliasingIface$AliasingClass},Element:{t:AliasingClass.AliasingIface$AliasingClass}}},Return:{t:$$$cl1.Boolean}};
    $$$c2.check(use$802(AliasingSub2().iface),$$$cl1.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$804=(5);
    $$$c2.check($$$cl1.isOfType(xxxxx$804,{t:$$$cl1.Integer}),$$$cl1.String("Type alias",10));
    $$$c2.check(Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}).first,{T:{t:$$$cl1.Integer}}).string.equals($$$cl1.String("[[1]]",5)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("class alias ",12),Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}).first,{T:{t:$$$cl1.Integer}}).string,$$$cl1.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2.check(MiMatrix((2)).string.equals($$$cl1.String("[[1, 2], [1, 2]]",16)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("interface alias ",16),MiMatrix((2)).string,$$$cl1.String(" instead of [[1, 2], [1, 2]]",28)]).string);
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$805=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$806=$$$cl1.String("XXXX",4);
    $$$c2.check($$$cl1.isOfType(xxxxx1$805,{ t:'u', l:[{t:$$$cl1.String},{t:$$$cl1.Integer}]}),$$$cl1.String("is String|Integer",17));
    $$$c2.check($$$cl1.isOfType(xxxxx2$806,{ t:'i', l:[{t:$$$cl1.String},{t:$$$cl1.List,a:{Element:{t:$$$cl1.Anything}}}]}),$$$cl1.String("is String&List",14));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$807=function (bits$808){
        if(bits$808===undefined){bits$808=$$$cl1.getEmpty();}
        return $$$cl1.any(bits$808);
    };
    cualquiera$807.$$metamodel$$={$nm:'cualquiera',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'bits',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Boolean}}}}]};
    $$$c2.check(cualquiera$807([true,true,true].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.true$809}})),$$$cl1.String("seq arg method alias",20));
};testAliasing.$$metamodel$$={$nm:'testAliasing',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testAliasing.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//ClassDefinition LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDeclaration parent at late_support.ceylon (4:4-4:37)
    var parent;
    $$$cl1.defineAttr($$lateTestChild,'parent',function(){if (parent===undefined)throw $$$cl1.InitializationException($$$cl1.String('Attempt to read unitialized attribute «parent»'));return parent;},function(parent$810){if(parent!==undefined)throw $$$cl1.InitializationException($$$cl1.String('Attempt to reassign immutable attribute «parent»'));return parent=parent$810;});
    return $$lateTestChild;
}
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl1.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl1.Basic);
    }
    LateTestChild.$$.$$metamodel$$={$nm:'LateTestChild',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDefinition LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children$811, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children$811===undefined){children$811=$$$cl1.getEmpty();}
    
    //AttributeDeclaration children at late_support.ceylon (8:4-8:34)
    var children=children$811;
    $$$cl1.defineAttr($$lateTestParent,'children',function(){return children;});
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$812 = $$lateTestParent.children.iterator();
    var child$813;while ((child$813=it$812.next())!==$$$cl1.getFinished()){
        (child$813.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl1.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl1.Basic);
    }
    LateTestParent.$$.$$metamodel$$={$nm:'LateTestParent',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDefinition testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDeclaration kids at late_support.ceylon (15:4-15:51)
    var kids$814=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$814);
    try{
        LateTestParent(kids$814);
        $$$c2.fail($$$cl1.String("reassigning to late attribute should fail",41));
    }catch(ex$815){
        if (ex$815.getT$name === undefined) ex$815=$$$cl1.NativeException(ex$815);
        if($$$cl1.isOfType(ex$815,{t:$$$cl1.InitializationException})){
            $$$c2.check(true);
        }
        else if($$$cl1.isOfType(ex$815,{t:$$$cl1.Exception})){
            $$$c2.fail($$$cl1.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$815}
    }
    try{
        $$$cl1.print(LateTestChild().parent);
        $$$c2.fail($$$cl1.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$816){
        if (ex$816.getT$name === undefined) ex$816=$$$cl1.NativeException(ex$816);
        if($$$cl1.isOfType(ex$816,{t:$$$cl1.InitializationException})){
            $$$c2.check(true);
        }
        else if($$$cl1.isOfType(ex$816,{t:$$$cl1.Exception})){
            $$$c2.fail($$$cl1.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$816}
    }
};testLate.$$metamodel$$={$nm:'testLate',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testLate.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$x){
    
    //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
    function helloWorld(){
        $$$cl1.print($$$cl1.String("hello world",11));
    }
    $$x.helloWorld=helloWorld;
    helloWorld.$$metamodel$$={$nm:'helloWorld',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//helloWorld.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl1.initTypeProto(X,'misc::X');
    }
    X.$$.$$metamodel$$={$nm:'X',$mt:'ifc','satisfies':[]};
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition Foo at misc.ceylon (7:0-17:0)
function Foo(name$817, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    var name=name$817;
    $$$cl1.defineAttr($$foo,'name',function(){return name;});
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    var counter$818=(0);
    $$$cl1.defineAttr($$foo,'counter$818',function(){return counter$818;},function(counter$819){return counter$818=counter$819;});
    
    //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
    $$$cl1.defineAttr($$foo,'count',function(){
        return counter$818;
    });
    
    //MethodDefinition inc at misc.ceylon (11:4-11:43)
    function inc(){
        counter$818=counter$818.plus((1));
    }
    $$foo.inc=inc;
    inc.$$metamodel$$={$nm:'inc',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//inc.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    
    //MethodDefinition printName at misc.ceylon (12:4-14:4)
    function printName(){
        $$$cl1.print($$$cl1.String("foo name = ",11).plus($$foo.name));
    }
    $$foo.printName=printName;
    printName.$$metamodel$$={$nm:'printName',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//printName.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    
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
    Foo.$$.$$metamodel$$={$nm:'Foo',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
    printName.$$metamodel$$={$nm:'printName',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//printName.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    
    //ClassDefinition Inner at misc.ceylon (25:4-31:4)
    function Inner$Bar($$inner$Bar){
        $init$Inner$Bar();
        if ($$inner$Bar===undefined)$$inner$Bar=new Inner$Bar.$$;
        $$inner$Bar.$$outer=this;
        $$$cl1.print($$$cl1.String("creating inner class of :",25).plus($$bar.name));
        
        //MethodDefinition incOuter at misc.ceylon (28:8-30:8)
        function incOuter(){
            $$bar.inc();
        }
        $$inner$Bar.incOuter=incOuter;
        incOuter.$$metamodel$$={$nm:'incOuter',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//incOuter.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
        return $$inner$Bar;
    }
    $$bar.Inner$Bar=Inner$Bar;
    function $init$Inner$Bar(){
        if (Inner$Bar.$$===undefined){
            $$$cl1.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl1.Basic);
            Bar.Inner$Bar=Inner$Bar;
        }
        Inner$Bar.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
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
    Bar.$$.$$metamodel$$={$nm:'Bar',$mt:'cls','super':{t:Foo},'satisfies':[{t:X}]};
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDefinition printBoth at misc.ceylon (36:0-38:0)
function printBoth(x$820,y$821){
    $$$cl1.print(x$820.plus($$$cl1.String(", ",2)).plus(y$821));
};printBoth.$$metamodel$$={$nm:'printBoth',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.String}},{$nm:'y',$mt:'prm',$t:{t:$$$cl1.String}}]};//printBoth.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.Anything}};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$822){
    f$822();
    f$822();
};doIt.$$metamodel$$={$nm:'doIt',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl1.Anything}}]};//doIt.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Anything},Element:{t:$$$cl1.Anything}}},Return:{t:$$$cl1.Anything}};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob$823(){
    var $$foob=new foob$823.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    var name=$$$cl1.String("Gavin",5);
    $$$cl1.defineAttr($$foob,'name',function(){return name;});
    return $$foob;
}
function $init$foob$823(){
    if (foob$823.$$===undefined){
        $$$cl1.initTypeProto(foob$823,'misc::foob',$$$cl1.Basic);
    }
    foob$823.$$.$$metamodel$$={$nm:'foob',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return foob$823;
}
exports.$init$foob$823=$init$foob$823;
$init$foob$823();
var foob$824=foob$823();
var getFoob=function(){
    return foob$824;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$825){
    if(strings$825===undefined){strings$825=$$$cl1.getEmpty();}
};printAll.$$metamodel$$={$nm:'printAll',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'strings',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}]};//printAll.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},Element:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}},Return:{t:$$$cl1.Anything}};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$826, $$f){return Foo(name$826,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;
$var.$$metamodel$$={$nm:'var',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//$var.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$827, b$828, c$829, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}};
    $$testObjects.a$827=a$827;
    $$testObjects.b$828=b$828;
    $$testObjects.c$829=c$829;
    $$$cl1.Iterable($$testObjects);
    $$$cl1.add_type_arg($$testObjects,'Absent',{t:$$$cl1.Null});
    $$$cl1.add_type_arg($$testObjects,'Element',{t:$$$cl1.Integer});
    
    //MethodDefinition iterator at objects.ceylon (4:2-16:2)
    function iterator(){
        
        //ObjectDefinition iter at objects.ceylon (5:4-14:4)
        function iter$830($$targs$$){
            var $$iter$830=new iter$830.$$;
            $$iter$830.$$targs$$=$$targs$$;
            $$$cl1.Iterator($$iter$830);
            $$$cl1.add_type_arg($$iter$830,'Element',{t:$$$cl1.Integer});
            
            //AttributeDeclaration index at objects.ceylon (6:6-6:30)
            var index$831=(0);
            $$$cl1.defineAttr($$iter$830,'index$831',function(){return index$831;},function(index$832){return index$831=index$832;});
            
            //MethodDefinition next at objects.ceylon (7:6-13:6)
            function next(){
                (oldindex$833=index$831,index$831=oldindex$833.successor,oldindex$833);
                var oldindex$833;
                if(index$831.equals((1))){
                    return a$827;
                }else {
                    if(index$831.equals((2))){
                        return b$828;
                    }else {
                        if(index$831.equals((3))){
                            return c$829;
                        }
                    }
                }
                return $$$cl1.getFinished();
            }
            $$iter$830.next=next;
            next.$$metamodel$$={$nm:'next',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Integer},{t:$$$cl1.Finished}]},$ps:[]};//next.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Integer},{t:$$$cl1.Finished}]}};
            return $$iter$830;
        }
        function $init$iter$830(){
            if (iter$830.$$===undefined){
                $$$cl1.initTypeProto(iter$830,'misc::TestObjects.iterator.iter',$$$cl1.Basic,$$$cl1.Iterator);
            }
            iter$830.$$.$$metamodel$$={$nm:'iter',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}}]};
            return iter$830;
        }
        $init$iter$830();
        var iter$834=iter$830({Element:{t:$$$cl1.Integer}});
        var getIter$834=function(){
            return iter$834;
        }
        return getIter$834();
    }
    $$testObjects.iterator=iterator;
    iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}},$ps:[]};//iterator.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}}};
    return $$testObjects;
}
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl1.initTypeProto(TestObjects,'misc::TestObjects',$$$cl1.Basic,$$$cl1.Iterable);
    }
    TestObjects.$$.$$metamodel$$={$nm:'TestObjects',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}]};
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDefinition test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl1.print($$$cl1.String("testing objects",15));
    
    //AttributeDeclaration t1 at objects.ceylon (21:2-21:42)
    var t1$835=TestObjects((1),(2),(3)).iterator();
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:42)
    var t2$836=TestObjects((1),(2),(3)).iterator();
    var i$837;
    if($$$cl1.isOfType((i$837=t1$835.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$837.equals((1)),$$$cl1.String("objects 1",9));
    }
    var i$838;
    if($$$cl1.isOfType((i$838=t1$835.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$838.equals((2)),$$$cl1.String("objects 2",9));
    }
    var i$839;
    if($$$cl1.isOfType((i$839=t2$836.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$839.equals((1)),$$$cl1.String("objects 3",9));
    }
    var i$840;
    if($$$cl1.isOfType((i$840=t1$835.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$840.equals((3)),$$$cl1.String("objects 4",9));
    }
    $$$c2.check($$$cl1.isOfType(t1$835.next(),{t:$$$cl1.Finished}),$$$cl1.String("objects 5",9));
    var i$841;
    if($$$cl1.isOfType((i$841=t2$836.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$841.equals((2)),$$$cl1.String("objects 6",9));
    }
    var i$842;
    if($$$cl1.isOfType((i$842=t2$836.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$842.equals((3)),$$$cl1.String("objects 7",9));
    }
};test_objects.$$metamodel$$={$nm:'test_objects',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test_objects.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//InterfaceDefinition Top1 at reifiedRuntime.ceylon (3:0-3:22)
function Top1($$top1){
}
exports.Top1=Top1;
function $init$Top1(){
    if (Top1.$$===undefined){
        $$$cl1.initTypeProto(Top1,'misc::Top1');
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
        $$$cl1.initTypeProto(Middle1,'misc::Middle1',$init$Top1());
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
        $$$cl1.initTypeProto(Bottom1,'misc::Bottom1',$init$Middle1());
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
    $$$cl1.set_type_args($$invariant,$$targs$$);
    return $$invariant;
}
exports.Invariant=Invariant;
function $init$Invariant(){
    if (Invariant.$$===undefined){
        $$$cl1.initTypeProto(Invariant,'misc::Invariant',$$$cl1.Basic);
    }
    Invariant.$$.$$metamodel$$={$nm:'Invariant',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Element:{}},'satisfies':[]};
    return Invariant;
}
exports.$init$Invariant=$init$Invariant;
$init$Invariant();

//ClassDefinition Covariant at reifiedRuntime.ceylon (8:0-8:38)
function Covariant($$targs$$,$$covariant){
    $init$Covariant();
    if ($$covariant===undefined)$$covariant=new Covariant.$$;
    $$$cl1.set_type_args($$covariant,$$targs$$);
    return $$covariant;
}
exports.Covariant=Covariant;
function $init$Covariant(){
    if (Covariant.$$===undefined){
        $$$cl1.initTypeProto(Covariant,'misc::Covariant',$$$cl1.Basic);
    }
    Covariant.$$.$$metamodel$$={$nm:'Covariant',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Element:{'var':'out',}},'satisfies':[]};
    return Covariant;
}
exports.$init$Covariant=$init$Covariant;
$init$Covariant();

//ClassDefinition Contravariant at reifiedRuntime.ceylon (9:0-9:41)
function Contravariant($$targs$$,$$contravariant){
    $init$Contravariant();
    if ($$contravariant===undefined)$$contravariant=new Contravariant.$$;
    $$$cl1.set_type_args($$contravariant,$$targs$$);
    return $$contravariant;
}
exports.Contravariant=Contravariant;
function $init$Contravariant(){
    if (Contravariant.$$===undefined){
        $$$cl1.initTypeProto(Contravariant,'misc::Contravariant',$$$cl1.Basic);
    }
    Contravariant.$$.$$metamodel$$={$nm:'Contravariant',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Element:{'var':'in',}},'satisfies':[]};
    return Contravariant;
}
exports.$init$Contravariant=$init$Contravariant;
$init$Contravariant();

//ClassDefinition Bivariant at reifiedRuntime.ceylon (10:0-10:41)
function Bivariant($$targs$$,$$bivariant){
    $init$Bivariant();
    if ($$bivariant===undefined)$$bivariant=new Bivariant.$$;
    $$$cl1.set_type_args($$bivariant,$$targs$$);
    return $$bivariant;
}
exports.Bivariant=Bivariant;
function $init$Bivariant(){
    if (Bivariant.$$===undefined){
        $$$cl1.initTypeProto(Bivariant,'misc::Bivariant',$$$cl1.Basic);
    }
    Bivariant.$$.$$metamodel$$={$nm:'Bivariant',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{In:{'var':'in',},Out:{'var':'out',}},'satisfies':[]};
    return Bivariant;
}
exports.$init$Bivariant=$init$Bivariant;
$init$Bivariant();

//ClassDefinition Container at reifiedRuntime.ceylon (12:0-16:0)
function Container($$targs$$,$$container){
    $init$Container();
    if ($$container===undefined)$$container=new Container.$$;
    $$$cl1.set_type_args($$container,$$targs$$);
    
    //ClassDefinition Member at reifiedRuntime.ceylon (13:4-15:4)
    function Member$Container($$targs$$,$$member$Container){
        $init$Member$Container();
        if ($$member$Container===undefined)$$member$Container=new Member$Container.$$;
        $$$cl1.set_type_args($$member$Container,$$targs$$);
        $$member$Container.$$outer=this;
        
        //ClassDefinition Child at reifiedRuntime.ceylon (14:8-14:40)
        function Child$Member$Container($$targs$$,$$child$Member$Container){
            $init$Child$Member$Container();
            if ($$child$Member$Container===undefined)$$child$Member$Container=new Child$Member$Container.$$;
            $$$cl1.set_type_args($$child$Member$Container,$$targs$$);
            $$child$Member$Container.$$outer=this;
            return $$child$Member$Container;
        }
        $$member$Container.Child$Member$Container=Child$Member$Container;
        function $init$Child$Member$Container(){
            if (Child$Member$Container.$$===undefined){
                $$$cl1.initTypeProto(Child$Member$Container,'misc::Container.Member.Child',$$$cl1.Basic);
                Container.Member$Container.Child$Member$Container=Child$Member$Container;
            }
            Child$Member$Container.$$.$$metamodel$$={$nm:'Child',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{InnerMost:{}},'satisfies':[]};
            return Child$Member$Container;
        }
        $$member$Container.$init$Child$Member$Container=$init$Child$Member$Container;
        $init$Child$Member$Container();
        return $$member$Container;
    }
    $$container.Member$Container=Member$Container;
    function $init$Member$Container(){
        if (Member$Container.$$===undefined){
            $$$cl1.initTypeProto(Member$Container,'misc::Container.Member',$$$cl1.Basic);
            Container.Member$Container=Member$Container;
        }
        Member$Container.$$.$$metamodel$$={$nm:'Member',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Inner:{}},'satisfies':[]};
        return Member$Container;
    }
    $$container.$init$Member$Container=$init$Member$Container;
    $init$Member$Container();
    return $$container;
}
exports.Container=Container;
function $init$Container(){
    if (Container.$$===undefined){
        $$$cl1.initTypeProto(Container,'misc::Container',$$$cl1.Basic);
    }
    Container.$$.$$metamodel$$={$nm:'Container',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Outer:{}},'satisfies':[]};
    return Container;
}
exports.$init$Container=$init$Container;
$init$Container();

//MethodDefinition runtimeMethod at reifiedRuntime.ceylon (18:0-20:0)
function runtimeMethod(param$843){
    return $$$cl1.getNothing();
};runtimeMethod.$$metamodel$$={$nm:'runtimeMethod',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'param',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//runtimeMethod.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};

//MethodDefinition testReifiedRuntime at reifiedRuntime.ceylon (22:0-81:0)
function testReifiedRuntime(){
    $$$cl1.print($$$cl1.String("Reified generics",16));
    
    //AttributeDeclaration member at reifiedRuntime.ceylon (24:4-24:57)
    var member$844=Container({Outer:{t:$$$cl1.String}}).Member$Container({Inner:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.isOfType(member$844,{t:Container.Member$Container,a:{Outer:{t:$$$cl1.String},Inner:{t:$$$cl1.Integer}}}),$$$cl1.String("reified runtime inner 1",23));
    $$$c2.check((!$$$cl1.isOfType(member$844,{t:Container.Member$Container,a:{Outer:{t:$$$cl1.Integer},Inner:{t:$$$cl1.Integer}}})),$$$cl1.String("reified runtime inner 2",23));
    $$$c2.check((!$$$cl1.isOfType(member$844,{t:Container.Member$Container,a:{Outer:{t:$$$cl1.String},Inner:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime inner 3",23));
    
    //AttributeDeclaration member2 at reifiedRuntime.ceylon (29:4-29:77)
    var member2$845=Container({Outer:{t:$$$cl1.String}}).Member$Container({Inner:{t:$$$cl1.Integer}}).Child$Member$Container({InnerMost:{t:$$$cl1.Character}});
    $$$c2.check($$$cl1.isOfType(member2$845,{t:Container.Member$Container.Child$Member$Container,a:{Outer:{t:$$$cl1.String},Inner:{t:$$$cl1.Integer},InnerMost:{t:$$$cl1.Character}}}),$$$cl1.String("reified runtime inner 4",23));
    
    //AttributeDeclaration invTop1 at reifiedRuntime.ceylon (32:4-32:38)
    var invTop1$846=Invariant({Element:{t:Top1}});
    $$$c2.check($$$cl1.isOfType(invTop1$846,{t:Invariant,a:{Element:{t:Top1}}}),$$$cl1.String("reified runtime invariant 1",27));
    $$$c2.check((!$$$cl1.isOfType(invTop1$846,{t:Invariant,a:{Element:{t:Middle1}}})),$$$cl1.String("reified runtime invariant 2",27));
    $$$c2.check((!$$$cl1.isOfType(invTop1$846,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl1.String("reified runtime invariant 3",27));
    
    //AttributeDeclaration invMiddle1 at reifiedRuntime.ceylon (37:4-37:44)
    var invMiddle1$847=Invariant({Element:{t:Middle1}});
    $$$c2.check((!$$$cl1.isOfType(invMiddle1$847,{t:Invariant,a:{Element:{t:Top1}}})),$$$cl1.String("reified runtime invariant 4",27));
    $$$c2.check($$$cl1.isOfType(invMiddle1$847,{t:Invariant,a:{Element:{t:Middle1}}}),$$$cl1.String("reified runtime invariant 5",27));
    $$$c2.check((!$$$cl1.isOfType(invMiddle1$847,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl1.String("reified runtime invariant 6",27));
    
    //AttributeDeclaration covMiddle1 at reifiedRuntime.ceylon (42:4-42:44)
    var covMiddle1$848=Covariant({Element:{t:Middle1}});
    $$$c2.check($$$cl1.isOfType(covMiddle1$848,{t:Covariant,a:{Element:{t:Top1}}}),$$$cl1.String("reified runtime covariant 1",27));
    $$$c2.check($$$cl1.isOfType(covMiddle1$848,{t:Covariant,a:{Element:{t:Middle1}}}),$$$cl1.String("reified runtime covariant 2",27));
    $$$c2.check((!$$$cl1.isOfType(covMiddle1$848,{t:Covariant,a:{Element:{t:Bottom1}}})),$$$cl1.String("reified runtime covariant 3",27));
    
    //AttributeDeclaration contravMiddle1 at reifiedRuntime.ceylon (47:4-47:52)
    var contravMiddle1$849=Contravariant({Element:{t:Middle1}});
    $$$c2.check((!$$$cl1.isOfType(contravMiddle1$849,{t:Contravariant,a:{Element:{t:Top1}}})),$$$cl1.String("reified runtime contravariant 1",31));
    $$$c2.check($$$cl1.isOfType(contravMiddle1$849,{t:Contravariant,a:{Element:{t:Middle1}}}),$$$cl1.String("reified runtime contravariant 2",31));
    $$$c2.check($$$cl1.isOfType(contravMiddle1$849,{t:Contravariant,a:{Element:{t:Bottom1}}}),$$$cl1.String("reified runtime contravariant 3",31));
    
    //AttributeDeclaration bivMiddle1 at reifiedRuntime.ceylon (52:4-52:52)
    var bivMiddle1$850=Bivariant({Out:{t:Middle1},In:{t:Middle1}});
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Top1},In:{t:Top1}}})),$$$cl1.String("reified runtime bivariant 1",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Top1}}})),$$$cl1.String("reified runtime bivariant 2",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Top1}}})),$$$cl1.String("reified runtime bivariant 3",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Top1},In:{t:Middle1}}}),$$$cl1.String("reified runtime bivariant 4",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Middle1}}}),$$$cl1.String("reified runtime bivariant 5",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Middle1}}})),$$$cl1.String("reified runtime bivariant 6",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Top1},In:{t:Bottom1}}}),$$$cl1.String("reified runtime bivariant 7",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Bottom1}}}),$$$cl1.String("reified runtime bivariant 8",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$850,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Bottom1}}})),$$$cl1.String("reified runtime bivariant 9",27));
    
    //ClassDefinition Local at reifiedRuntime.ceylon (63:4-63:21)
    function Local$851($$targs$$,$$local$851){
        $init$Local$851();
        if ($$local$851===undefined)$$local$851=new Local$851.$$;
        $$$cl1.set_type_args($$local$851,$$targs$$);
        return $$local$851;
    }
    function $init$Local$851(){
        if (Local$851.$$===undefined){
            $$$cl1.initTypeProto(Local$851,'misc::testReifiedRuntime.Local',$$$cl1.Basic);
        }
        Local$851.$$.$$metamodel$$={$nm:'Local',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{T:{}},'satisfies':[]};
        return Local$851;
    }
    $init$Local$851();
    
    //AttributeDeclaration localInteger at reifiedRuntime.ceylon (65:4-65:42)
    var localInteger$852=Local$851({T:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.isOfType(localInteger$852,{t:Local$851,a:{T:{t:$$$cl1.Integer}}}),$$$cl1.String("reified runtime local 1",23));
    
    //AttributeDeclaration m at reifiedRuntime.ceylon (68:4-68:28)
    var m$853=$$$cl1.$JsCallable(runtimeMethod,[],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.isOfType(m$853,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}}),$$$cl1.String("reified runtime callable 1",26));
    $$$c2.check((!$$$cl1.isOfType(m$853,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Integer}}})),$$$cl1.String("reified runtime callable 2",26));
    $$$c2.check((!$$$cl1.isOfType(m$853,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime callable 3",26));
    $$$c2.check((!$$$cl1.isOfType(m$853,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime callable 4",26));
    
    //AttributeDeclaration m2 at reifiedRuntime.ceylon (73:4-73:34)
    var m2$854=$$$cl1.$JsCallable(testReifiedRuntime,[],{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}});
    $$$c2.check($$$cl1.isOfType(m2$854,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}}}),$$$cl1.String("reified runtime callable 5",26));
    $$$c2.check((!$$$cl1.isOfType(m2$854,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime callable 6",26));
    $$$c2.check((!$$$cl1.isOfType(m2$854,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Anything}}})),$$$cl1.String("reified runtime callable 7",26));
    
    //AttributeDeclaration rec1 at reifiedRuntime.ceylon (78:4-78:80)
    var rec1$855=$$$cl1.Singleton($$$cl1.Entry((1),$$$cl1.Singleton($$$cl1.String("x",1),{Element:{t:$$$cl1.String}}),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.String}}}}),{Element:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.String}}}}}});
    $$$c2.check($$$cl1.isOfType(rec1$855,{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.String}}}}}}}),$$$cl1.String("#188 [1]",8));
    $$$c2.check((!$$$cl1.isOfType(rec1$855,{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.Integer}}}}}}})),$$$cl1.String("#188 [2]",8));
};testReifiedRuntime.$$metamodel$$={$nm:'testReifiedRuntime',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testReifiedRuntime.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
var $$$m856=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-44:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$857=$$$cl1.String("hello",5);
    $$$cl1.print(name$857);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$858=Foo($$$cl1.String("goodbye",7));
    printBoth(name$857,foo$858.name);
    (y$859=$$$cl1.String("y",1),x$860=$$$cl1.String("x",1),printBoth(x$860,y$859));
    var y$859,x$860;
    foo$858.inc();
    foo$858.inc();
    $$$c2.check(foo$858.count.equals((3)),$$$cl1.String("Foo.count",9));
    $$$c2.check(foo$858.string.equals($$$cl1.String("Foo(goodbye)",12)),$$$cl1.String("Foo.string",10));
    foo$858.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt($$$cl1.$JsCallable((opt$861=foo$858,$$$cl1.JsCallable(opt$861,opt$861!==null?opt$861.inc:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}}));
    var opt$861;
    $$$c2.check(foo$858.count.equals((5)),$$$cl1.String("Foo.count [2]",13));
    doIt($$$cl1.$JsCallable(Bar,[],{Arguments:{t:$$$cl1.Empty},Return:{t:Bar}}));
    $$$cl1.print(getFoob().name);
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$862(){
        var $$x$862=new x$862.$$;
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        function y(){
            $$$cl1.print($$$cl1.String("xy",2));
        }
        $$x$862.y=y;
        y.$$metamodel$$={$nm:'y',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//y.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
        return $$x$862;
    }
    function $init$x$862(){
        if (x$862.$$===undefined){
            $$$cl1.initTypeProto(x$862,'misc::test.x',$$$cl1.Basic);
        }
        x$862.$$.$$metamodel$$={$nm:'x',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return x$862;
    }
    $init$x$862();
    var x$863=x$862();
    var getX$863=function(){
        return x$863;
    }
    getX$863().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$864=Bar();
    b$864.Inner$Bar().incOuter();
    b$864.Inner$Bar().incOuter();
    b$864.Inner$Bar().incOuter();
    $$$c2.check(b$864.count.equals((4)),$$$cl1.String("Bar.count",9));
    printAll([$$$cl1.String("hello",5),$$$cl1.String("world",5)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}));
    (strings$865=$$$cl1.Tuple($$$cl1.String("hello",5),$$$cl1.Tuple($$$cl1.String("world",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),printAll(strings$865));
    var strings$865;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$866=$$$m856.Counter((0));
    c$866.inc();
    c$866.inc();
    $$$c2.check(c$866.count.equals((2)),$$$cl1.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$867=$var();
    test_objects();
    testAliasing();
    testLate();
    testReifiedRuntime();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
