(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"}
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$322,f$323,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$324 = a$322.iterator();
    var e$325;while ((e$325=it$324.next())!==$$$cl1.getFinished()){
        if(f$323(e$325)){
            return e$325;
        }
    }
    return null;
};find.$$metamodel$$={$nm:'find',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Array,a:{Element:'Element'}}},{$nm:'f',$mt:'prm',$t:{t:$$$cl1.Boolean}}],$tp:{Element:{}}};//find.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Boolean},Element:{t:$$$cl1.Boolean}}},Return:{ t:'u', l:[{t:$$$cl1.Null},$$$mptypes.Element]}};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$326,f$327,$$$mptypes){
    if(f$327===undefined){f$327=function (x$328){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$329 = a$326.iterator();
    var e$330;while ((e$330=it$329.next())!==$$$cl1.getFinished()){
        if(f$327(e$330)){
            return e$330;
        }
    }
    if ($$$cl1.getFinished() === e$330){
        return null;
    }
};find2.$$metamodel$$={$nm:'find2',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Array,a:{Element:'Element'}}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl1.Boolean}}],$tp:{Element:{}}};//find2.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Boolean},Element:{t:$$$cl1.Boolean}}},Return:{ t:'u', l:[{t:$$$cl1.Null},$$$mptypes.Element]}};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$331){
    return function (i$332){
        return i$332.minus(howMuch$331).string;
    };
};subtract.$$metamodel$$={$nm:'subtract',$mt:'mthd',$t:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}},$ps:[{$nm:'howMuch',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//subtract.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}}};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl1.print($$$cl1.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$333=(elements$334=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),$$$cl1.array(elements$334,{Element:{t:$$$cl1.Integer}}));
    var elements$334;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$335=find(nums$333,$$$cl1.$JsCallable(function (i$336){
        return i$336.remainder((2)).equals((0));
    },[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}}),{Element:{t:$$$cl1.Integer}});
    var setFound$335=function(found$337){return found$335=found$337;};
    var i$338;
    if((i$338=found$335)!==null){
        $$$c2.check(i$338.equals((2)),$$$cl1.String("anonfunc positional",19));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc positional",19));
    }
    found$335=(f$339=function (i$340){
        return i$340.remainder((2)).equals((0));
    },a$341=nums$333,find(a$341,f$339,{Element:{t:$$$cl1.Integer}}));
    var f$339,a$341;
    var i$342;
    if((i$342=found$335)!==null){
        $$$c2.check(i$342.equals((2)),$$$cl1.String("anonfunc named",14));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$343(f$344,expect$345){
        $$$c2.check(f$344((0)).equals(expect$345),$$$cl1.StringBuilder().appendAll([$$$cl1.String("anon func returns ",18),f$344((0)).string,$$$cl1.String(" instead of ",12),expect$345.string]).string);
    };callFunction$343.$$metamodel$$={$nm:'callFunction',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl1.String}},{$nm:'expect',$mt:'prm',$t:{t:$$$cl1.String}}]};//callFunction$343.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.Anything}};
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$346(i$347){
        return i$347.plus((12)).string;
    };f$346.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//f$346.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};
    callFunction$343($$$cl1.$JsCallable(f$346,[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("12",2));
    callFunction$343($$$cl1.$JsCallable(function (i$348){
        return i$348.times((3)).string;
    },[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("0",1));
    (expect$349=$$$cl1.String("0",1),f$350=function (i$351){
        return i$351.power((2)).string;
    },callFunction$343(f$350,expect$349));
    var expect$349,f$350;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$352=$$$cl1.$JsCallable(function (i$353){
        return i$353.minus((10)).string;
    },[{$nm:'p1',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}});
    callFunction$343($$$cl1.$JsCallable(f2$352,[/*INVOKE callable params 1*/],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("-10",3));
    callFunction$343($$$cl1.$JsCallable(subtract((5)),/*Callable from Invocation +  [InvocationExpression] <String(Integer)> (66:15-66:25)
|  +  [BaseMemberExpression] <String(Integer)(Integer)> (66:15-66:22) => Method[subtract:String(Integer)]
|  |  + subtract [Identifier] (66:15-66:22)
|  |  +  [InferredTypeArguments]
|  + () [PositionalArgumentList] (66:23-66:25)
|  |  +  [ListedArgument] (66:24-66:24) => ValueParameter[subtract#howMuch:Integer]
|  |  |  +  [Expression] <Integer> (66:24-66:24)
|  |  |  |  + 5 [NaturalLiteral] <Integer> (66:24-66:24)
*/[/*INVOKE callable params 1*/],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("-5",2));
    found$335=find2(nums$333,$$$cl1.$JsCallable(function (i$354){
        return i$354.compare((2)).equals($$$cl1.getLarger());
    },[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}}),{Element:{t:$$$cl1.Integer}});
    var i$355;
    if((i$355=found$335)!==null){
        $$$c2.check(i$355.equals((3)),$$$cl1.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc i>2 [2]",16));
    }
    found$335=find2(nums$333,undefined,{Element:{t:$$$cl1.Integer}});
    var i$356;
    if((i$356=found$335)!==null){
        $$$c2.check(i$356.equals((1)),$$$cl1.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc defaulted param [2]",28));
    }
};testAnonymous.$$metamodel$$={$nm:'testAnonymous',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testAnonymous.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl1.print($$$cl1.String("hello world",11));
}
exports.helloWorld=helloWorld;
helloWorld.$$metamodel$$={$nm:'helloWorld',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//helloWorld.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$357){
    $$$cl1.print($$$cl1.String("hello",5).plus(name$357));
}
exports.hello=hello;
hello.$$metamodel$$={$nm:'hello',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String}}]};//hello.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.Anything}};

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$358){
    if(names$358===undefined){names$358=$$$cl1.getEmpty();}
}
exports.helloAll=helloAll;
helloAll.$$metamodel$$={$nm:'helloAll',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}]};//helloAll.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},Element:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}},Return:{t:$$$cl1.Anything}};

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$359){
    return obj$359.string;
}
exports.toString=toString;
toString.$$metamodel$$={$nm:'toString',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'obj',$mt:'prm',$t:{t:$$$cl1.Object}}]};//toString.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Object},Element:{t:$$$cl1.Object}}},Return:{t:$$$cl1.String}};

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$360,y$361){
    return x$360.plus(y$361);
}
exports.add=add;
add.$$metamodel$$={$nm:'add',$mt:'mthd',$t:{t:$$$cl1.Float},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Float}},{$nm:'y',$mt:'prm',$t:{t:$$$cl1.Float}}]};//add.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Float},Element:{t:$$$cl1.Float}}},Return:{t:$$$cl1.Float}};

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$362,f$363){
    f$363((0));
}
exports.repeat=repeat;
repeat.$$metamodel$$={$nm:'repeat',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'times',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'f',$mt:'prm',$t:{t:$$$cl1.Anything}}]};//repeat.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Anything},Element:{t:$$$cl1.Anything}}},Return:{t:$$$cl1.Anything}};

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$364, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl1.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$364=seq$364;
    $$$cl1.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
    $$$cl1.defineAttr($$mySequence,'lastIndex',function(){
        return seq$364.lastIndex;
    });
    
    //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
    $$$cl1.defineAttr($$mySequence,'first',function(){
        return seq$364.first;
    });
    
    //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
    $$$cl1.defineAttr($$mySequence,'rest',function(){
        return seq$364.rest;
    });
    
    //MethodDefinition get at functions.ceylon (30:4-30:67)
    function get(index$365){
        return seq$364.get(index$365);
    }
    $$mySequence.get=get;
    get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},'Element']},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//get.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{ t:'u', l:[{t:$$$cl1.Null},$$mySequence.$$targs$$.Element]}};
    
    //MethodDefinition span at functions.ceylon (31:4-31:88)
    function span(from$366,to$367){
        return seq$364.span(from$366,to$367);
    }
    $$mySequence.span=span;
    span.$$metamodel$$={$nm:'span',$mt:'mthd',$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//span.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Sequential,a:{Element:$$mySequence.$$targs$$.Element}}};
    
    //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
    function spanFrom(from$368){
        return seq$364.spanFrom(from$368);
    }
    $$mySequence.spanFrom=spanFrom;
    spanFrom.$$metamodel$$={$nm:'spanFrom',$mt:'mthd',$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//spanFrom.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Sequential,a:{Element:$$mySequence.$$targs$$.Element}}};
    
    //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
    function spanTo(to$369){
        return seq$364.spanTo(to$369);
    }
    $$mySequence.spanTo=spanTo;
    spanTo.$$metamodel$$={$nm:'spanTo',$mt:'mthd',$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//spanTo.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Sequential,a:{Element:$$mySequence.$$targs$$.Element}}};
    
    //MethodDefinition segment at functions.ceylon (34:4-34:102)
    function segment(from$370,length$371){
        return seq$364.segment(from$370,length$371);
    }
    $$mySequence.segment=segment;
    segment.$$metamodel$$={$nm:'segment',$mt:'mthd',$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'length',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//segment.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Sequential,a:{Element:$$mySequence.$$targs$$.Element}}};
    
    //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
    $$$cl1.defineAttr($$mySequence,'clone',function(){
        return $$mySequence;
    });
    
    //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
    $$$cl1.defineAttr($$mySequence,'string',function(){
        return seq$364.string;
    });
    
    //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
    $$$cl1.defineAttr($$mySequence,'hash',function(){
        return seq$364.hash;
    });
    
    //MethodDefinition equals at functions.ceylon (38:4-38:75)
    function equals(other$372){
        return seq$364.equals(other$372);
    }
    $$mySequence.equals=equals;
    equals.$$metamodel$$={$nm:'equals',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object}}]};//equals.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Object},Element:{t:$$$cl1.Object}}},Return:{t:$$$cl1.Boolean}};
    
    //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
    $$$cl1.defineAttr($$mySequence,'reversed',function(){
        return seq$364.reversed;
    });
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    $$$cl1.defineAttr($$mySequence,'last',function(){return seq$364.last;});
    
    //MethodDeclaration iterator at functions.ceylon (41:4-41:64)
    var iterator=function (){
        return seq$364.iterator();
    };
    iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl1.Iterator,a:{Element:'Element'}},$ps:[]};
    $$mySequence.iterator=iterator;
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    $$$cl1.defineAttr($$mySequence,'size',function(){return seq$364.size;});
    
    //MethodDeclaration contains at functions.ceylon (43:4-43:71)
    var contains=function (other$373){
        return seq$364.contains(other$373);
    };
    contains.$$metamodel$$={$nm:'contains',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object}}]};
    $$mySequence.contains=contains;
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl1.initTypeProto(MySequence,'functions::MySequence',$$$cl1.Basic,$$$cl1.Sequence);
    }
    MySequence.$$.$$metamodel$$={$nm:'MySequence',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Element:{'var':'out',}},'satisfies':[{t:$$$cl1.Sequence,a:{Element:'Element'}}]};
    return MySequence;
}
exports.$init$MySequence=$init$MySequence;
$init$MySequence();

//ClassDefinition RefHelper at functions.ceylon (46:0-48:0)
function RefHelper($$refHelper){
    $init$RefHelper();
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    
    //MethodDefinition f at functions.ceylon (47:4-47:47)
    function f(i$374){
        return true;
    }
    $$refHelper.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//f.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}};
    return $$refHelper;
}
function $init$RefHelper(){
    if (RefHelper.$$===undefined){
        $$$cl1.initTypeProto(RefHelper,'functions::RefHelper',$$$cl1.Basic);
    }
    RefHelper.$$.$$metamodel$$={$nm:'RefHelper',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDefinition testMethodReference at functions.ceylon (50:0-58:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (51:4-51:28)
    var obj1$375=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$376=MySequence($$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Element:{t:$$$cl1.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$377(x$378){
        return x$378((0));
    };tst$377.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Boolean}}]};//tst$377.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Boolean},Element:{t:$$$cl1.Boolean}}},Return:{t:$$$cl1.Boolean}};
    $$$c2.check(tst$377($$$cl1.$JsCallable((opt$379=obj1$375,$$$cl1.JsCallable(opt$379,opt$379!==null?opt$379.f:null)),[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}})),$$$cl1.String("Reference to method",19));
    var opt$379;
    $$$c2.check(tst$377($$$cl1.$JsCallable((opt$380=obj2$376,$$$cl1.JsCallable(opt$380,opt$380!==null?opt$380.defines:null)),[{$nm:'index',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}})),$$$cl1.String("Reference to method from ceylon.language",40));
    var opt$380;
};testMethodReference.$$metamodel$$={$nm:'testMethodReference',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testMethodReference.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$381,i2$382,i3$383){
    if(i2$382===undefined){i2$382=i1$381.plus((1));}
    if(i3$383===undefined){i3$383=i1$381.plus(i2$382);}
    return $$$cl1.StringBuilder().appendAll([i1$381.string,$$$cl1.String(",",1),i2$382.string,$$$cl1.String(",",1),i3$383.string]).string;
};defParamTest.$$metamodel$$={$nm:'defParamTest',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer}}]};//defParamTest.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$384, i2$385, i3$386, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$385===undefined){i2$385=i1$384.plus((1));}
    if(i3$386===undefined){i3$386=i1$384.plus(i2$385);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    var s=$$$cl1.StringBuilder().appendAll([i1$384.string,$$$cl1.String(",",1),i2$385.string,$$$cl1.String(",",1),i3$386.string]).string;
    $$$cl1.defineAttr($$defParamTest1,'s',function(){return s;});
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl1.Basic);
    }
    DefParamTest1.$$.$$metamodel$$={$nm:'DefParamTest1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$387, i2$388, i3$389, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$387=i1$387;
    if(i2$388===undefined){i2$388=i1$387.plus((1));}
    $$defParamTest2.i2$388=i2$388;
    if(i3$389===undefined){i3$389=i1$387.plus(i2$388);}
    $$defParamTest2.i3$389=i3$389;
    
    //MethodDefinition f at functions.ceylon (67:4-67:55)
    function f(){
        return $$$cl1.StringBuilder().appendAll([i1$387.string,$$$cl1.String(",",1),i2$388.string,$$$cl1.String(",",1),i3$389.string]).string;
    }
    $$defParamTest2.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl1.Basic);
    }
    DefParamTest2.$$.$$metamodel$$={$nm:'DefParamTest2',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return DefParamTest2;
}
exports.$init$DefParamTest2=$init$DefParamTest2;
$init$DefParamTest2();

//ClassDefinition DefParamTest3 at functions.ceylon (69:0-73:0)
function DefParamTest3($$defParamTest3){
    $init$DefParamTest3();
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    
    //MethodDefinition f at functions.ceylon (70:4-72:4)
    function f(i1$390,i2$391,i3$392){
        if(i2$391===undefined){i2$391=i1$390.plus((1));}
        if(i3$392===undefined){i3$392=i1$390.plus(i2$391);}
        return $$$cl1.StringBuilder().appendAll([i1$390.string,$$$cl1.String(",",1),i2$391.string,$$$cl1.String(",",1),i3$392.string]).string;
    }
    $$defParamTest3.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer}}]};//f.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};
    return $$defParamTest3;
}
function $init$DefParamTest3(){
    if (DefParamTest3.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl1.Basic);
    }
    DefParamTest3.$$.$$metamodel$$={$nm:'DefParamTest3',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2.check(defParamTest((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters 1",22));
    $$$c2.check(defParamTest((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters 2",22));
    $$$c2.check(defParamTest((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters 3",22));
    $$$c2.check((i1$393=(1),defParamTest(i1$393,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters named 1",28));
    var i1$393;
    $$$c2.check((i1$394=(1),i2$395=(3),defParamTest(i1$394,i2$395,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters named 2",28));
    var i1$394,i2$395;
    $$$c2.check((i1$396=(1),i3$397=(0),defParamTest(i1$396,undefined,i3$397)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters named 3",28));
    var i1$396,i3$397;
    $$$c2.check(DefParamTest1((1)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class 1",28));
    $$$c2.check(DefParamTest1((1),(3)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class 2",28));
    $$$c2.check(DefParamTest1((1),(3),(0)).s.equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class 3",28));
    $$$c2.check((i1$398=(1),DefParamTest1(i1$398,undefined,undefined)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class named 1",34));
    var i1$398;
    $$$c2.check((i1$399=(1),i2$400=(3),DefParamTest1(i1$399,i2$400,undefined)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class named 2",34));
    var i1$399,i2$400;
    $$$c2.check((i1$401=(1),i3$402=(0),DefParamTest1(i1$401,undefined,i3$402)).s.equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class named 3",34));
    var i1$401,i3$402;
    $$$c2.check(DefParamTest2((1)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 1",29));
    $$$c2.check(DefParamTest2((1),(3)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 2",29));
    $$$c2.check(DefParamTest2((1),(3),(0)).f().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class2 3",29));
    $$$c2.check((i1$403=(1),DefParamTest2(i1$403,undefined,undefined)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 named 1",35));
    var i1$403;
    $$$c2.check((i1$404=(1),i2$405=(3),DefParamTest2(i1$404,i2$405,undefined)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 named 2",35));
    var i1$404,i2$405;
    $$$c2.check((i1$406=(1),i3$407=(0),DefParamTest2(i1$406,undefined,i3$407)).f().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class2 named 3",35));
    var i1$406,i3$407;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$408=DefParamTest3();
    $$$c2.check(tst$408.f((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters 1",29));
    $$$c2.check(tst$408.f((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters 2",29));
    $$$c2.check(tst$408.f((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted method parameters 3",29));
    $$$c2.check((i1$409=(1),(opt$410=tst$408,$$$cl1.JsCallable(opt$410,opt$410!==null?opt$410.f:null))(i1$409,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters named 1",35));
    var i1$409,opt$410;
    $$$c2.check((i1$411=(1),i2$412=(3),(opt$413=tst$408,$$$cl1.JsCallable(opt$413,opt$413!==null?opt$413.f:null))(i1$411,i2$412,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters named 2",35));
    var i1$411,i2$412,opt$413;
    $$$c2.check((i1$414=(1),i3$415=(0),(opt$416=tst$408,$$$cl1.JsCallable(opt$416,opt$416!==null?opt$416.f:null))(i1$414,undefined,i3$415)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted method parameters named 3",35));
    var i1$414,i3$415,opt$416;
};testDefaultedParams.$$metamodel$$={$nm:'testDefaultedParams',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testDefaultedParams.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$417($$getterTest$417){
        $init$GetterTest$417();
        if ($$getterTest$417===undefined)$$getterTest$417=new GetterTest$417.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        var i$418=(0);
        $$$cl1.defineAttr($$getterTest$417,'i$418',function(){return i$418;},function(i$419){return i$418=i$419;});
        
        //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
        $$$cl1.defineAttr($$getterTest$417,'x',function(){
            return (i$418=i$418.successor);
        });
        return $$getterTest$417;
    }
    function $init$GetterTest$417(){
        if (GetterTest$417.$$===undefined){
            $$$cl1.initTypeProto(GetterTest$417,'functions::testGetterMethodDefinitions.GetterTest',$$$cl1.Basic);
        }
        GetterTest$417.$$.$$metamodel$$={$nm:'GetterTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return GetterTest$417;
    }
    $init$GetterTest$417();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$420=GetterTest$417();
    $$$c2.check(gt$420.x.equals((1)),$$$cl1.String("getter defined as method 1",26));
    $$$c2.check(gt$420.x.equals((2)),$$$cl1.String("getter defined as method 2",26));
    $$$c2.check(gt$420.x.equals((3)),$$$cl1.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;
testGetterMethodDefinitions.$$metamodel$$={$nm:'testGetterMethodDefinitions',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testGetterMethodDefinitions.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$421,y$422,z$423){
    if(x$421===undefined){x$421=$$$cl1.String("x",1);}
    if(y$422===undefined){y$422=x$421.plus($$$cl1.String("y",1));}
    if(z$423===undefined){z$423=$$$cl1.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$424=x$421.plus($$$cl1.String(",",1)).plus(y$422);
    var setResult$424=function(result$425){return result$424=result$425;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$426 = z$423.iterator();
    var s$427;while ((s$427=it$426.next())!==$$$cl1.getFinished()){
        (result$424=result$424.plus($$$cl1.String(",",1).plus(s$427)));
    }
    return result$424;
};namedArgFunc.$$metamodel$$={$nm:'namedArgFunc',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$def:1,$t:{t:$$$cl1.String}},{$nm:'y',$mt:'prm',$def:1,$t:{t:$$$cl1.String}},{$nm:'z',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}]};//namedArgFunc.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},Element:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}},Return:{t:$$$cl1.String}};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$428, more$429, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$429===undefined){more$429=$$$cl1.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    var i=i$428;
    $$$cl1.defineAttr($$issue105,'i',function(){return i;});
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl1.initTypeProto(Issue105,'functions::Issue105',$$$cl1.Basic);
    }
    Issue105.$$.$$metamodel$$={$nm:'Issue105',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2.check((namedArgFunc(undefined,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("x,xy",4)),$$$cl1.String("named arguments 1",17));
    $$$c2.check((x$430=$$$cl1.String("a",1),namedArgFunc(x$430,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("a,ay",4)),$$$cl1.String("named arguments 2",17));
    var x$430;
    $$$c2.check((y$431=$$$cl1.String("b",1),namedArgFunc(undefined,y$431,$$$cl1.getEmpty())).equals($$$cl1.String("x,b",3)),$$$cl1.String("named arguments 3",17));
    var y$431;
    $$$c2.check((z$432=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$432)).equals($$$cl1.String("x,xy,c",6)),$$$cl1.String("named arguments 4",17));
    var z$432;
    $$$c2.check((x$433=$$$cl1.String("a",1),y$434=$$$cl1.String("b",1),z$435=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$433,y$434,z$435)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 5",17));
    var x$433,y$434,z$435;
    $$$c2.check((y$436=$$$cl1.String("b",1),x$437=$$$cl1.String("a",1),z$438=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$437,y$436,z$438)).equals($$$cl1.String("a,b,c,d",7)),$$$cl1.String("named arguments 6",17));
    var y$436,x$437,z$438;
    $$$c2.check((x$439=$$$cl1.String("a",1),z$440=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$439,undefined,z$440)).equals($$$cl1.String("a,ay,c",6)),$$$cl1.String("named arguments 7",17));
    var x$439,z$440;
    $$$c2.check((y$441=$$$cl1.String("b",1),z$442=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,y$441,z$442)).equals($$$cl1.String("x,b,c",5)),$$$cl1.String("named arguments 8",17));
    var y$441,z$442;
    $$$c2.check((y$443=$$$cl1.String("b",1),x$444=$$$cl1.String("a",1),namedArgFunc(x$444,y$443,$$$cl1.getEmpty())).equals($$$cl1.String("a,b",3)),$$$cl1.String("named arguments 9",17));
    var y$443,x$444;
    $$$c2.check((z$445=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$445)).equals($$$cl1.String("x,xy,c,d",8)),$$$cl1.String("named arguments 11",18));
    var z$445;
    $$$c2.check((y$446=$$$cl1.String("b",1),z$447=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),x$448=$$$cl1.String("a",1),namedArgFunc(x$448,y$446,z$447)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 12",18));
    var y$446,z$447,x$448;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$449=(i$450=(1),more$451=$$$cl1.Tuple((i$452=(2),Issue105(i$452,$$$cl1.getEmpty())),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$450,more$451));
    var i$450,more$451,i$452;
    $$$c2.check(issue105$449.i.equals((1)),$$$cl1.String("issue #105",10));
};testNamedArguments.$$metamodel$$={$nm:'testNamedArguments',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testNamedArguments.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl1.initTypeProto(LazyExprBase,'functions::LazyExprBase');
    }
    LazyExprBase.$$.$$metamodel$$={$nm:'LazyExprBase',$mt:'ifc','satisfies':[]};
    return LazyExprBase;
}
exports.$init$LazyExprBase=$init$LazyExprBase;
$init$LazyExprBase();

//ClassDefinition LazyExprTest at functions.ceylon (148:0-159:0)
function LazyExprTest($$lazyExprTest){
    $init$LazyExprTest();
    if ($$lazyExprTest===undefined)$$lazyExprTest=new LazyExprTest.$$;
    LazyExprBase($$lazyExprTest);
    
    //AttributeDeclaration x at functions.ceylon (149:4-149:36)
    var x=(1000);
    $$$cl1.defineAttr($$lazyExprTest,'x',function(){return x;},function(x$453){return x=x$453;});
    
    //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
    var f1=function (i$454,f$455){
        if(f$455===undefined){f$455=function (){
            return $$$cl1.StringBuilder().appendAll([i$454.string,$$$cl1.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor).string]).string;
        };}
        return $$$cl1.StringBuilder().appendAll([i$454.string,$$$cl1.String(":",1),f$455().string]).string;
    };
    f1.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl1.String}}]};
    $$lazyExprTest.f1=f1;
    
    //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
    var f2=function (i$456){
        return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$456);
    };
    f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$lazyExprTest.f2=f2;
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    $$$cl1.defineAttr($$lazyExprTest,'i1',function(){return ($$lazyExprTest.x=$$lazyExprTest.x.successor);});
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    $$$cl1.defineAttr($$lazyExprTest,'i2',function(){
        return ($$lazyExprTest.x=$$lazyExprTest.x.successor).times((2));
    });
    $$$cl1.defineAttr($$lazyExprTest,'s1',function(){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl1.String(".1",2)]).string;
    });
    $$lazyExprTest.s2=function (i$457){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl1.String(".",1),i$457.string]).string;
    };
    
    //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
    var f3=function (f$458){
        return f$458(($$lazyExprTest.x=$$lazyExprTest.x.successor));
    };
    f3.$$metamodel$$={$nm:'f3',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl1.String}}]};
    $$lazyExprTest.f3=f3;
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl1.Basic,$init$LazyExprBase());
    }
    LazyExprTest.$$.$$metamodel$$={$nm:'LazyExprTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:LazyExprBase}]};
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$459=(1000);
var getLx=function(){return lx$459;};
exports.getLx=getLx;
var setLx=function(lx$460){return lx$459=lx$460;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$461,f$462){
    if(f$462===undefined){f$462=function (){
        return $$$cl1.StringBuilder().appendAll([i$461.string,$$$cl1.String(".",1),(setLx(getLx().successor)).string]).string;
    };}
    return f$462();
};
lazy_f1.$$metamodel$$={$nm:'lazy_f1',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl1.String}}]};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$463){
    return (2).times((setLx(getLx().successor))).plus(i$463);
};
lazy_f2.$$metamodel$$={$nm:'lazy_f2',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};

//AttributeDeclaration lazy_i1 at functions.ceylon (164:0-164:23)
var getLazy_i1=function(){return (setLx(getLx().successor));};
exports.getLazy_i1=getLazy_i1;

//ClassDefinition LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDeclaration x at functions.ceylon (167:4-167:35)
    var x=(1000);
    $$$cl1.defineAttr($$lazyExprTest2,'x',function(){return x;},function(x$464){return x=x$464;});
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    $$$cl1.defineAttr($$lazyExprTest2,'s1',function(){return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;});
    
    //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
    var s2=function (i$465){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl1.String("-",1),i$465.string]).string;
    };
    s2.$$metamodel$$={$nm:'s2',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$lazyExprTest2.s2=s2;
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl1.Basic,$init$LazyExprBase());
    }
    LazyExprTest2.$$.$$metamodel$$={$nm:'LazyExprTest2',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:LazyExprBase}]};
    return LazyExprTest2;
}
exports.$init$LazyExprTest2=$init$LazyExprTest2;
$init$LazyExprTest2();

//ClassDefinition LazyExprTest3 at functions.ceylon (171:0-173:0)
function LazyExprTest3($$lazyExprTest3){
    $init$LazyExprTest3();
    if ($$lazyExprTest3===undefined)$$lazyExprTest3=new LazyExprTest3.$$;
    LazyExprTest2($$lazyExprTest3);
    
    //AttributeDeclaration s1 at functions.ceylon (172:4-172:43)
    var s1=$$$cl1.String("s1",2);
    $$$cl1.defineAttr($$lazyExprTest3,'s1',function(){return s1;},function(s1$466){return s1=s1$466;});
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
    }
    LazyExprTest3.$$.$$metamodel$$={$nm:'LazyExprTest3',$mt:'cls','super':{t:LazyExprTest2},'satisfies':[]};
    return LazyExprTest3;
}
exports.$init$LazyExprTest3=$init$LazyExprTest3;
$init$LazyExprTest3();

//ClassDefinition LazyExprTest4 at functions.ceylon (174:0-178:0)
function LazyExprTest4($$lazyExprTest4){
    $init$LazyExprTest4();
    if ($$lazyExprTest4===undefined)$$lazyExprTest4=new LazyExprTest4.$$;
    LazyExprTest2($$lazyExprTest4);
    $$$cl1.copySuperAttr($$lazyExprTest4,'s1','$$functions$LazyExprTest2');
    
    //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
    var assigned=$$$cl1.String("",0);
    $$$cl1.defineAttr($$lazyExprTest4,'assigned',function(){return assigned;},function(assigned$467){return assigned=assigned$467;});
    
    //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
    $$$cl1.defineAttr($$lazyExprTest4,'s1',function(){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("s1-",3),$$lazyExprTest4.s1$$functions$LazyExprTest2.string]).string;
    },function(s1$468){
        $$lazyExprTest4.assigned=s1$468;
    });
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
    }
    LazyExprTest4.$$.$$metamodel$$={$nm:'LazyExprTest4',$mt:'cls','super':{t:LazyExprTest2},'satisfies':[]};
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDefinition testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDeclaration tst at functions.ceylon (181:4-181:30)
    var tst$469=LazyExprTest();
    (tst$469.x=(1));
    $$$c2.check(tst$469.f1((3)).equals($$$cl1.String("3:3.2",5)),$$$cl1.String("=> defaulted param",18));
    $$$c2.check(tst$469.f2((3)).equals((9)),$$$cl1.String("=> method",9));
    $$$c2.check(tst$469.i1.equals((4)),$$$cl1.String("=> attribute",12));
    $$$c2.check(tst$469.i2.equals((10)),$$$cl1.String("=> attribute specifier",22));
    $$$c2.check(tst$469.s1.equals($$$cl1.String("6.1",3)),$$$cl1.String("=> attribute refinement",23));
    $$$c2.check(tst$469.s2((5)).equals($$$cl1.String("7.5",3)),$$$cl1.String("=> method refinement",20));
    setLx((1));
    $$$c2.check(lazy_f1((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param toplevel",27));
    $$$c2.check(lazy_f2((3)).equals((9)),$$$cl1.String("=> method toplevel",18));
    $$$c2.check(getLazy_i1().equals((4)),$$$cl1.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$470=(1000);
    var setX$470=function(x$471){return x$470=x$471;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$472=function (i$473,f$474){
        if(f$474===undefined){f$474=function (){
            return $$$cl1.StringBuilder().appendAll([i$473.string,$$$cl1.String(".",1),(x$470=x$470.successor).string]).string;
        };}
        return f$474();
    };
    f1$472.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl1.String}}]};
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$475=function (i$476){
        return (2).times((x$470=x$470.successor)).plus(i$476);
    };
    f2$475.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$477=function(){return (x$470=x$470.successor);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$478;
    var getI2$478=function(){
        return (x$470=x$470.successor).times((2));
    };
    x$470=(1);
    $$$c2.check(f1$472((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param local",24));
    $$$c2.check(f2$475((3)).equals((9)),$$$cl1.String("=> method local",15));
    $$$c2.check(getI1$477().equals((4)),$$$cl1.String("=> attribute local",18));
    $$$c2.check(getI2$478().equals((10)),$$$cl1.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$479=LazyExprTest3();
    (tst3$479.x=(1));
    $$$c2.check(tst3$479.s1.equals($$$cl1.String("s1",2)),$$$cl1.String("=> override variable 1",22));
    (tst3$479.s1=$$$cl1.String("abc",3));
    $$$c2.check(tst3$479.s1.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$480=LazyExprTest4();
    (tst4$480.x=(1));
    $$$c2.check(tst4$480.s1.equals($$$cl1.String("s1-2",4)),$$$cl1.String("=> override getter/setter 1",27));
    (tmp$481=tst4$480,tmp$481.s1=$$$cl1.String("abc",3),tmp$481.s1);
    var tmp$481;
    $$$c2.check(tst4$480.s1.equals($$$cl1.String("s1-4",4)),$$$cl1.String("=> override getter/setter 2",27));
    $$$c2.check(tst4$480.assigned.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override getter/setter 3",27));
    (tst$469.x=(1));
    x$470=(10);
    $$$c2.check((i$482=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$470=x$470.successor);
    }()),(opt$483=tst$469,$$$cl1.JsCallable(opt$483,opt$483!==null?opt$483.f1:null))(i$482,undefined)).equals($$$cl1.String("11:11.2",7)),$$$cl1.String("=> named arg",12));
    var i$482,opt$483;
    $$$c2.check((i$484=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$470=x$470.successor);
    }()),f$485=function (){return (x$470=x$470.successor).string;},(opt$486=tst$469,$$$cl1.JsCallable(opt$486,opt$486!==null?opt$486.f1:null))(i$484,f$485)).equals($$$cl1.String("12:13",5)),$$$cl1.String("=> named arg function",21));
    var i$484,f$485,opt$486;
    $$$c2.check((f$487=function (i$488){return $$$cl1.StringBuilder().appendAll([i$488.string,$$$cl1.String("-",1),(x$470=x$470.successor).string]).string;},(opt$489=tst$469,$$$cl1.JsCallable(opt$489,opt$489!==null?opt$489.f3:null))(f$487)).equals($$$cl1.String("3-14",4)),$$$cl1.String("=> named arg function with param",32));
    var f$487,opt$489;
};testLazyExpressions.$$metamodel$$={$nm:'testLazyExpressions',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testLazyExpressions.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl1.String("test",4));
    helloAll([$$$cl1.String("Gavin",5),$$$cl1.String("Enrique",7),$$$cl1.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}));
    $$$c2.check(toString((5)).equals($$$cl1.String("5",1)),$$$cl1.String("toString(obj)",13));
    $$$c2.check(add($$$cl1.Float(1.5),$$$cl1.Float(2.5)).equals($$$cl1.Float(4.0)),$$$cl1.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$490,y$491){
        return x$490.compare(y$491);
    }
};multiCompare.$$metamodel$$={$nm:'multiCompare',$mt:'mthd',$t:{t:$$$cl1.Comparison},$ps:[]};//multiCompare.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Comparison}};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$492){
    return function(apat$493){
        return function(amat$494){
            return $$$cl1.StringBuilder().appendAll([nombre$492.string,$$$cl1.String(" ",1),apat$493.string,$$$cl1.String(" ",1),amat$494.string]).string;
        }
    }
};multiFullname.$$metamodel$$={$nm:'multiFullname',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'nombre',$mt:'prm',$t:{t:$$$cl1.String}}]};//multiFullname.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.String}};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$495){
    if(name$495===undefined){name$495=$$$cl1.String("A",1);}
    return function(apat$496){
        return function(amat$497){
            return $$$cl1.StringBuilder().appendAll([name$495.string,$$$cl1.String(" ",1),apat$496.string,$$$cl1.String(" ",1),amat$497.string]).string;
        }
    }
};multiDefaulted.$$metamodel$$={$nm:'multiDefaulted',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'name',$mt:'prm',$def:1,$t:{t:$$$cl1.String}}]};//multiDefaulted.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.String}};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$498){
    if(names$498===undefined){names$498=$$$cl1.getEmpty();}
    return function(count$499){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$500=$$$cl1.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$501 = names$498.iterator();
        var name$502;while ((name$502=it$501.next())!==$$$cl1.getFinished()){
            sb$500.append(name$502).append($$$cl1.String(" ",1));
        }
        sb$500.append($$$cl1.String("count ",6)).append(count$499.string);
        return sb$500.string;
    }
};multiSequenced.$$metamodel$$={$nm:'multiSequenced',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}]};//multiSequenced.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},Element:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}}}},Return:{t:$$$cl1.String}};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl1.print($$$cl1.String("Testing multiple parameter lists...",35));
    $$$c2.check(multiCompare()((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 1",15));
    $$$c2.check(multiCompare()((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 2",15));
    $$$c2.check(multiCompare()((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$503=function (a$504,b$505){
        return multiCompare()(a$504,b$505);
    };
    comp$503.$$metamodel$$={$nm:'comp',$mt:'mthd',$t:{t:$$$cl1.Comparison},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$$c2.check(comp$503((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 4",15));
    $$$c2.check(comp$503((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 5",15));
    $$$c2.check(comp$503((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 6",15));
    $$$c2.check(multiFullname($$$cl1.String("a",1))($$$cl1.String("b",1).valueOf())($$$cl1.String("c",1).valueOf()).equals($$$cl1.String("a b c",5)),$$$cl1.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$506=function (c$507){
        return multiFullname($$$cl1.String("A",1))($$$cl1.String("B",1).valueOf())(c$507.valueOf());
    };
    apat$506.$$metamodel$$={$nm:'apat',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl1.String}}]};
    $$$c2.check(apat$506($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$508=function (name$509){
        return multiFullname($$$cl1.String("Name",4))(name$509.valueOf());
    };
    nombre$508.$$metamodel$$={$nm:'nombre',$mt:'mthd',$t:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.String}}},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String}}]};
    $$$c2.check(nombre$508($$$cl1.String("Z",1))($$$cl1.String("L",1).valueOf()).equals($$$cl1.String("Name Z L",8)),$$$cl1.String("Multi callable 2",16));
    $$$c2.check(multiDefaulted()($$$cl1.String("B",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$510=$$$cl1.$JsCallable(multiDefaulted(),[{$nm:'p1',$mt:'prm',$t:{t:$$$cl1.String}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.String}}}});
    $$$c2.check(md1$510($$$cl1.String("B",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 2",17));
    $$$c2.check(md1$510($$$cl1.String("B",1).valueOf())($$$cl1.String("Z",1).valueOf()).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 3",17));
    $$$c2.check(md1$510($$$cl1.String("Z",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A Z C",5)),$$$cl1.String("Multi defaulted 4",17));
    $$$c2.check(md1$510($$$cl1.String("Y",1).valueOf())($$$cl1.String("Z",1).valueOf()).equals($$$cl1.String("A Y Z",5)),$$$cl1.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$511=function (x$512){
        return multiDefaulted()($$$cl1.String("B",1).valueOf())(x$512.valueOf());
    };
    md2$511.$$metamodel$$={$nm:'md2',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.String}}]};
    $$$c2.check(md2$511($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 6",17));
    $$$c2.check(md2$511($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 7",17));
    $$$c2.check(multiSequenced([$$$cl1.String("A",1),$$$cl1.String("B",1),$$$cl1.String("C",1)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))((1)).equals($$$cl1.String("A B C count 1",13)),$$$cl1.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$513=function (c$514){
        return multiSequenced([$$$cl1.String("x",1),$$$cl1.String("y",1),$$$cl1.String("z",1)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))(c$514);
    };
    ms1$513.$$metamodel$$={$nm:'ms1',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl1.Integer}}]};
    $$$c2.check(ms1$513((5)).equals($$$cl1.String("x y z count 5",13)),$$$cl1.String("Multi sequenced 2",17));
    $$$c2.check(ms1$513((10)).equals($$$cl1.String("x y z count 10",14)),$$$cl1.String("Multi sequenced 3",17));
};testMultipleParamLists.$$metamodel$$={$nm:'testMultipleParamLists',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testMultipleParamLists.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
