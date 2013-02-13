(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"doIt"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$at":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"TestObjects"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface"}},"$nm":"AliasingSub2"},"testAliasing":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAliasing"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$pt":"v","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"}}};
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
    var getIface=function(){
        
        //ObjectDefinition aliased at aliases.ceylon (26:8-27:8)
        function aliased$668(){
            var $$aliased$668=new aliased$668.$$;
            $$aliasingSub2.AliasingIface$AliasingClass($$aliased$668);
            return $$aliased$668;
        }
        function $init$aliased$668(){
            if (aliased$668.$$===undefined){
                $$$cl1.initTypeProto(aliased$668,'misc::AliasingSub2.iface.aliased',$$$cl1.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
            }
            return aliased$668;
        }
        $init$aliased$668();
        var aliased$669=aliased$668();
        var getAliased$669=function(){
            return aliased$669;
        }
        return getAliased$669();
    }
    $$aliasingSub2.getIface=getIface;
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
function Listleton(l$670, $$targs$$,$$listleton){return $$$cl1.Singleton(l$670,{Element:{t:$$$cl1.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl1.Singleton.$$;

//ClassDefinition MiMatrix at aliases.ceylon (35:0-66:0)
function MiMatrix(gridSize$671, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl1.Integer}};
    $$$cl1.Sequence($$miMatrix);
    $$$cl1.add_type_arg($$miMatrix,'Cell',{t:$$$cl1.Integer});
    
    //AttributeDeclaration sb at aliases.ceylon (36:4-36:44)
    var sb$672=$$$cl1.SequenceBuilder({Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}});
    //'for' statement at aliases.ceylon (37:4-39:4)
    var it$673 = $$$cl1.Range((1),gridSize$671,{Element:{t:$$$cl1.Integer}}).getIterator();
    var i$674;while ((i$674=it$673.next())!==$$$cl1.getFinished()){
        sb$672.append($$$cl1.Comprehension(function(){
            //Comprehension at aliases.ceylon (38:20-38:43)
            var it$675=$$$cl1.Range((1),gridSize$671,{Element:{t:$$$cl1.Integer}}).getIterator();
            var j$676=$$$cl1.getFinished();
            var next$j$676=function(){return j$676=it$675.next();}
            next$j$676();
            return function(){
                if(j$676!==$$$cl1.getFinished()){
                    var j$676$677=j$676;
                    function getJ$676(){return j$676$677;}
                    var tmpvar$678=getJ$676();
                    next$j$676();
                    return tmpvar$678;
                }
                return $$$cl1.getFinished();
            }
        },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).getSequence());
    }
    
    //AttributeDeclaration grid at aliases.ceylon (40:4-40:24)
    var g$679;
    if($$$cl1.nonempty((g$679=sb$672.getSequence()))){
        var grid$680=g$679;
        var getGrid$680=function(){return grid$680;};
        $$miMatrix.getGrid$680=getGrid$680;
    }else {
        var grid$680=$$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}});
        var getGrid$680=function(){return grid$680;};
        $$miMatrix.getGrid$680=getGrid$680;
    }
    
    //AttributeGetterDefinition iterator at aliases.ceylon (46:4-46:72)
    var getIterator=function(){
        return getGrid$680().getIterator();
    }
    $$miMatrix.getIterator=getIterator;
    
    //AttributeDeclaration string at aliases.ceylon (47:4-47:45)
    var string$681=getGrid$680().getString();
    var getString=function(){return string$681;};
    $$miMatrix.getString=getString;
    
    //AttributeDeclaration hash at aliases.ceylon (48:4-48:42)
    var hash$682=getGrid$680().getHash();
    var getHash=function(){return hash$682;};
    $$miMatrix.getHash=getHash;
    
    //MethodDeclaration equals at aliases.ceylon (49:4-49:68)
    var equals=function (other$683){
        return getGrid$680().equals(other$683);
    };
    $$miMatrix.equals=equals;
    var span=(opt$684=getGrid$680(),$$$cl1.JsCallable(opt$684,opt$684!==null?opt$684.span:null));
    $$miMatrix.span=span;
    var opt$684;
    var segment=(opt$685=getGrid$680(),$$$cl1.JsCallable(opt$685,opt$685!==null?opt$685.segment:null));
    $$miMatrix.segment=segment;
    var opt$685;
    
    //AttributeDeclaration reversed at aliases.ceylon (52:4-52:58)
    var reversed$686=getGrid$680().getReversed();
    var getReversed=function(){return reversed$686;};
    $$miMatrix.getReversed=getReversed;
    
    //AttributeDeclaration lastIndex at aliases.ceylon (53:4-53:52)
    var lastIndex$687=getGrid$680().getLastIndex();
    var getLastIndex=function(){return lastIndex$687;};
    $$miMatrix.getLastIndex=getLastIndex;
    
    //MethodDeclaration get at aliases.ceylon (54:4-54:55)
    var get=function (i$688){
        return getGrid$680().get(i$688);
    };
    $$miMatrix.get=get;
    
    //AttributeDeclaration rest at aliases.ceylon (55:4-55:47)
    var rest$689=getGrid$680().getRest();
    var getRest=function(){return rest$689;};
    $$miMatrix.getRest=getRest;
    
    //AttributeDeclaration first at aliases.ceylon (56:4-56:47)
    var first$690=getGrid$680().getFirst();
    var getFirst=function(){return first$690;};
    $$miMatrix.getFirst=getFirst;
    
    //AttributeDeclaration clone at aliases.ceylon (57:4-57:40)
    var getClone=function(){return $$miMatrix;};
    $$miMatrix.getClone=getClone;
    
    //AttributeDeclaration size at aliases.ceylon (58:4-58:43)
    var getSize=function(){return getGrid$680().getSize();};
    $$miMatrix.getSize=getSize;
    
    //MethodDeclaration contains at aliases.ceylon (59:4-59:72)
    var contains=function (other$691){
        return getGrid$680().contains(other$691);
    };
    $$miMatrix.contains=contains;
    $$miMatrix.getLast=function(){
        return getGrid$680().getLast();
    };
    
    //MethodDeclaration spanTo at aliases.ceylon (61:4-62:41)
    var spanTo=function (to$692){
        return (opt$693=(to$692.compare((0)).equals($$$cl1.getSmaller())?$$$cl1.getEmpty():null),opt$693!==null?opt$693:$$miMatrix.span((0),to$692));
    };
    $$miMatrix.spanTo=spanTo;
    var opt$693;
    
    //MethodDeclaration spanFrom at aliases.ceylon (64:4-65:28)
    var spanFrom=function (from$694){
        return $$miMatrix.span(from$694,$$miMatrix.getSize());
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
    function InnerSubalias$695($$innerSubalias$695){return AliasingSubclass($$innerSubalias$695);}
    InnerSubalias$695.$$=AliasingSubclass.$$;
    $$$c2.check(InnerSubalias$695().aliasingSubclass(),$$$cl1.String("Aliased top-level class",23));
    
    //MethodDefinition use at aliases.ceylon (74:4-74:65)
    function use$696(aif$697){
        return aif$697.aliasingIface();
    };
    $$$c2.check(use$696(AliasingSub2().getIface()),$$$cl1.String("Aliased member interface",24));
    
    //AttributeDeclaration xxxxx at aliases.ceylon (76:4-76:24)
    var xxxxx$698=(5);
    $$$c2.check($$$cl1.isOfType(xxxxx$698,{t:$$$cl1.Integer}),$$$cl1.String("Type alias",10));
    $$$c2.check(Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}).getFirst(),{T:{t:$$$cl1.Integer}}).getString().equals($$$cl1.String("[[1]]",5)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("class alias ",12),Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}},First:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}}}).getFirst(),{T:{t:$$$cl1.Integer}}).getString(),$$$cl1.String(" instead of [ [ 1 ] ]",21)]).getString());
    $$$c2.check(MiMatrix((2)).getString().equals($$$cl1.String("{ { 1, 2 }, { 1, 2 } }",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("interface alias ",16),MiMatrix((2)).getString(),$$$cl1.String(" instead of { { 1, 2 }, { 1, 2 } }",34)]).getString());
    
    //AttributeDeclaration xxxxx1 at aliases.ceylon (80:4-80:21)
    var xxxxx1$699=(6);
    
    //AttributeDeclaration xxxxx2 at aliases.ceylon (81:4-81:26)
    var xxxxx2$700=$$$cl1.String("XXXX",4);
    $$$c2.check($$$cl1.isOfType(xxxxx1$699,{ t:'u', l:[{t:$$$cl1.String},{t:$$$cl1.Integer}]}),$$$cl1.String("is String|Integer",17));
    $$$c2.check($$$cl1.isOfType(xxxxx2$700,{ t:'i', l:[{t:$$$cl1.String},{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Anything}}}]}),$$$cl1.String("is String&Sequence",18));
    
    //MethodDeclaration cualquiera at aliases.ceylon (84:4-84:51)
    var cualquiera$701=function (bits$702){
        if(bits$702===undefined){bits$702=$$$cl1.getEmpty();}
        return $$$cl1.any(bits$702);
    };
    $$$c2.check(cualquiera$701([true,true,true].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.$true}})),$$$cl1.String("seq arg method alias",20));
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
function Foo(name$703, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:22)
    var name$704=name$703;
    var getName=function(){return name$704;};
    $$foo.getName=getName;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:28)
    var counter$705=(0);
    var getCounter$705=function(){return counter$705;};
    $$foo.getCounter$705=getCounter$705;
    var setCounter$705=function(counter$706){return counter$705=counter$706;};
    $$foo.setCounter$705=setCounter$705;
    
    //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
    var getCount=function(){
        return getCounter$705();
    }
    $$foo.getCount=getCount;
    
    //MethodDefinition inc at misc.ceylon (11:4-11:43)
    function inc(){
        setCounter$705(getCounter$705().plus((1)));
    }
    $$foo.inc=inc;
    
    //MethodDefinition printName at misc.ceylon (12:4-14:4)
    function printName(){
        $$$cl1.print($$$cl1.String("foo name = ",11).plus($$foo.getName()));
    }
    $$foo.printName=printName;
    
    //AttributeDeclaration string at misc.ceylon (15:4-15:57)
    var string$707=$$$cl1.StringBuilder().appendAll([$$$cl1.String("Foo(",4),$$foo.getName().getString(),$$$cl1.String(")",1)]).getString();
    var getString=function(){return string$707;};
    $$foo.getString=getString;
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
        $$$cl1.print($$$cl1.String("bar name = ",11).plus($$bar.getName()));
        $$bar.printName$$misc$Foo();
        $$bar.printName$$misc$Foo();
    }
    $$bar.printName=printName;
    
    //ClassDefinition Inner at misc.ceylon (25:4-31:4)
    function Inner$Bar($$inner$Bar){
        $init$Inner$Bar();
        if ($$inner$Bar===undefined)$$inner$Bar=new Inner$Bar.$$;
        $$$cl1.print($$$cl1.String("creating inner class of :",25).plus($$bar.getName()));
        
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
function printBoth(x$708,y$709){
    $$$cl1.print(x$708.plus($$$cl1.String(", ",2)).plus(y$709));
};

//MethodDefinition doIt at misc.ceylon (40:0-42:0)
function doIt(f$710){
    f$710();
    f$710();
};

//ObjectDefinition foob at misc.ceylon (44:0-46:0)
function foob(){
    var $$foob=new foob.$$;
    
    //AttributeDeclaration name at misc.ceylon (45:4-45:30)
    var name$711=$$$cl1.String("Gavin",5);
    var getName=function(){return name$711;};
    $$foob.getName=getName;
    return $$foob;
}
function $init$foob(){
    if (foob.$$===undefined){
        $$$cl1.initTypeProto(foob,'misc::foob',$$$cl1.Basic);
    }
    return foob;
}
exports.$init$foob=$init$foob;
$init$foob();
var foob$712=foob();
var getFoob=function(){
    return foob$712;
}

//MethodDefinition printAll at misc.ceylon (48:0-48:32)
function printAll(strings$713){
    if(strings$713===undefined){strings$713=$$$cl1.getEmpty();}
};

//ClassDeclaration F at misc.ceylon (50:0-50:33)
function F(name$714, $$f){return Foo(name$714,$$f);}
F.$$=Foo.$$;

//MethodDefinition var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;

//ClassDefinition TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$715, b$716, c$717, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}};
    $$testObjects.a$715=a$715;
    $$testObjects.b$716=b$716;
    $$testObjects.c$717=c$717;
    $$$cl1.Iterable($$testObjects);
    $$$cl1.add_type_arg($$testObjects,'Absent',{t:$$$cl1.Null});
    $$$cl1.add_type_arg($$testObjects,'Element',{t:$$$cl1.Integer});
    
    //AttributeGetterDefinition iterator at objects.ceylon (4:2-16:2)
    var getIterator=function(){
        
        //ObjectDefinition iter at objects.ceylon (5:4-14:4)
        function iter$718($$targs$$){
            var $$iter$718=new iter$718.$$;
            $$iter$718.$$targs$$=$$targs$$;
            $$$cl1.Iterator($$iter$718);
            $$$cl1.add_type_arg($$iter$718,'Element',{t:$$$cl1.Integer});
            
            //AttributeDeclaration index at objects.ceylon (6:6-6:30)
            var index$719=(0);
            var getIndex$719=function(){return index$719;};
            $$iter$718.getIndex$719=getIndex$719;
            var setIndex$719=function(index$720){return index$719=index$720;};
            $$iter$718.setIndex$719=setIndex$719;
            
            //MethodDefinition next at objects.ceylon (7:6-13:6)
            function next(){
                (oldindex$721=getIndex$719(),setIndex$719(oldindex$721.getSuccessor()),oldindex$721);
                var oldindex$721;
                if(getIndex$719().equals((1))){
                    return a$715;
                }else {
                    if(getIndex$719().equals((2))){
                        return b$716;
                    }else {
                        if(getIndex$719().equals((3))){
                            return c$717;
                        }
                    }
                }
                return $$$cl1.getFinished();
            }
            $$iter$718.next=next;
            return $$iter$718;
        }
        function $init$iter$718(){
            if (iter$718.$$===undefined){
                $$$cl1.initTypeProto(iter$718,'misc::TestObjects.iterator.iter',$$$cl1.Basic,$$$cl1.Iterator);
            }
            return iter$718;
        }
        $init$iter$718();
        var iter$722=iter$718({Element:{t:$$$cl1.Integer}});
        var getIter$722=function(){
            return iter$722;
        }
        return getIter$722();
    }
    $$testObjects.getIterator=getIterator;
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
    var t1$723=TestObjects((1),(2),(3)).getIterator();
    
    //AttributeDeclaration t2 at objects.ceylon (22:2-22:40)
    var t2$724=TestObjects((1),(2),(3)).getIterator();
    var i$725;
    if($$$cl1.isOfType((i$725=t1$723.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$725.equals((1)),$$$cl1.String("objects 1",9));
    }
    var i$726;
    if($$$cl1.isOfType((i$726=t1$723.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$726.equals((2)),$$$cl1.String("objects 2",9));
    }
    var i$727;
    if($$$cl1.isOfType((i$727=t2$724.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$727.equals((1)),$$$cl1.String("objects 3",9));
    }
    var i$728;
    if($$$cl1.isOfType((i$728=t1$723.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$728.equals((3)),$$$cl1.String("objects 4",9));
    }
    $$$c2.check($$$cl1.isOfType(t1$723.next(),{t:$$$cl1.Finished}),$$$cl1.String("objects 5",9));
    var i$729;
    if($$$cl1.isOfType((i$729=t2$724.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$729.equals((2)),$$$cl1.String("objects 6",9));
    }
    var i$730;
    if($$$cl1.isOfType((i$730=t2$724.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$730.equals((3)),$$$cl1.String("objects 7",9));
    }
};
var $$$m731=require('members/0.1/members-0.1');

//MethodDefinition test at testit.ceylon (4:0-43:0)
function test(){
    
    //AttributeDeclaration name at testit.ceylon (5:4-5:24)
    var name$732=$$$cl1.String("hello",5);
    $$$cl1.print(name$732);
    
    //AttributeDeclaration foo at testit.ceylon (7:4-7:28)
    var foo$733=Foo($$$cl1.String("goodbye",7));
    printBoth(name$732,foo$733.getName());
    (y$734=$$$cl1.String("y",1),x$735=$$$cl1.String("x",1),printBoth(x$735,y$734));
    var y$734,x$735;
    foo$733.inc();
    foo$733.inc();
    $$$c2.check(foo$733.getCount().equals((3)),$$$cl1.String("Foo.count",9));
    $$$c2.check(foo$733.getString().equals($$$cl1.String("Foo(goodbye)",12)),$$$cl1.String("Foo.string",10));
    foo$733.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt((opt$736=foo$733,$$$cl1.JsCallable(opt$736,opt$736!==null?opt$736.inc:null)));
    var opt$736;
    $$$c2.check(foo$733.getCount().equals((5)),$$$cl1.String("Foo.count [2]",13));
    doIt(Bar);
    $$$cl1.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (20:4-24:4)
    function x$737(){
        var $$x$737=new x$737.$$;
        
        //MethodDefinition y at testit.ceylon (21:8-23:8)
        function y(){
            $$$cl1.print($$$cl1.String("xy",2));
        }
        $$x$737.y=y;
        return $$x$737;
    }
    function $init$x$737(){
        if (x$737.$$===undefined){
            $$$cl1.initTypeProto(x$737,'misc::test.x',$$$cl1.Basic);
        }
        return x$737;
    }
    $init$x$737();
    var x$738=x$737();
    var getX$738=function(){
        return x$738;
    }
    getX$738().y();
    
    //AttributeDeclaration b at testit.ceylon (26:4-26:17)
    var b$739=Bar();
    b$739.Inner$Bar().incOuter();
    b$739.Inner$Bar().incOuter();
    b$739.Inner$Bar().incOuter();
    $$$c2.check(b$739.getCount().equals((4)),$$$cl1.String("Bar.count",9));
    printAll([$$$cl1.String("hello",5),$$$cl1.String("world",5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    (strings$740=$$$cl1.Tuple($$$cl1.String("hello",5),$$$cl1.Tuple($$$cl1.String("world",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),printAll(strings$740));
    var strings$740;
    
    //AttributeDeclaration c at testit.ceylon (34:4-34:26)
    var c$741=$$$m731.Counter((0));
    c$741.inc();
    c$741.inc();
    $$$c2.check(c$741.getCount().equals((2)),$$$cl1.String("Counter.count",13));
    
    //AttributeDeclaration v2 at testit.ceylon (38:4-38:20)
    var v2$742=$var();
    test_objects();
    testAliasing();
    $$$c2.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
