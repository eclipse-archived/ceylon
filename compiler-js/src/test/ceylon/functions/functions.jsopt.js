(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2593=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$2913,f$2914,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$2915 = a$2913.iterator();
    var e$2916;while ((e$2916=it$2915.next())!==$$$cl2592.getFinished()){
        if(f$2914(e$2916)){
            return e$2916;
        }
    }
    return null;
};find.$$metamodel$$={$nm:'find',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Array,a:{Element:'Element'}}},{$nm:'f',$mt:'prm',$t:{t:$$$cl2592.Boolean}}],$tp:{Element:{}}};//find.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Boolean},Element:{t:$$$cl2592.Boolean}}},Return:{ t:'u', l:[{t:$$$cl2592.Null},$$$mptypes.Element]}};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$2917,f$2918,$$$mptypes){
    if(f$2918===undefined){f$2918=function (x$2919){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$2920 = a$2917.iterator();
    var e$2921;while ((e$2921=it$2920.next())!==$$$cl2592.getFinished()){
        if(f$2918(e$2921)){
            return e$2921;
        }
    }
    if ($$$cl2592.getFinished() === e$2921){
        return null;
    }
};find2.$$metamodel$$={$nm:'find2',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Array,a:{Element:'Element'}}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl2592.Boolean}}],$tp:{Element:{}}};//find2.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Boolean},Element:{t:$$$cl2592.Boolean}}},Return:{ t:'u', l:[{t:$$$cl2592.Null},$$$mptypes.Element]}};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$2922){
    return function (i$2923){
        return i$2923.minus(howMuch$2922).string;
    };
};subtract.$$metamodel$$={$nm:'subtract',$mt:'mthd',$t:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}},$ps:[{$nm:'howMuch',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};//subtract.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}}};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl2592.print($$$cl2592.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$2924=(elements$2925=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),$$$cl2592.array(elements$2925,{Element:{t:$$$cl2592.Integer}}));
    var elements$2925;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$2926=find(nums$2924,$$$cl2592.$JsCallable(function (i$2927){
        return i$2927.remainder((2)).equals((0));
    },[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Boolean}}),{Element:{t:$$$cl2592.Integer}});
    var setFound$2926=function(found$2928){return found$2926=found$2928;};
    var i$2929;
    if((i$2929=found$2926)!==null){
        $$$c2593.check(i$2929.equals((2)),$$$cl2592.String("anonfunc positional",19));
    }else {
        $$$c2593.fail($$$cl2592.String("anonfunc positional",19));
    }
    found$2926=(f$2930=function (i$2931){
        return i$2931.remainder((2)).equals((0));
    },a$2932=nums$2924,find(a$2932,f$2930,{Element:{t:$$$cl2592.Integer}}));
    var f$2930,a$2932;
    var i$2933;
    if((i$2933=found$2926)!==null){
        $$$c2593.check(i$2933.equals((2)),$$$cl2592.String("anonfunc named",14));
    }else {
        $$$c2593.fail($$$cl2592.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$2934(f$2935,expect$2936){
        $$$c2593.check(f$2935((0)).equals(expect$2936),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("anon func returns ",18),f$2935((0)).string,$$$cl2592.String(" instead of ",12),expect$2936.string]).string);
    };callFunction$2934.$$metamodel$$={$nm:'callFunction',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl2592.String}},{$nm:'expect',$mt:'prm',$t:{t:$$$cl2592.String}}]};//callFunction$2934.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.Anything}};
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$2937(i$2938){
        return i$2938.plus((12)).string;
    };f$2937.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};//f$2937.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}};
    callFunction$2934($$$cl2592.$JsCallable(f$2937,[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}),$$$cl2592.String("12",2));
    callFunction$2934($$$cl2592.$JsCallable(function (i$2939){
        return i$2939.times((3)).string;
    },[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}),$$$cl2592.String("0",1));
    (expect$2940=$$$cl2592.String("0",1),f$2941=function (i$2942){
        return i$2942.power((2)).string;
    },callFunction$2934(f$2941,expect$2940));
    var expect$2940,f$2941;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$2943=$$$cl2592.$JsCallable(function (i$2944){
        return i$2944.minus((10)).string;
    },[],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}});
    callFunction$2934($$$cl2592.$JsCallable(f2$2943,[],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}),$$$cl2592.String("-10",3));
    callFunction$2934($$$cl2592.$JsCallable(subtract((5)),/*Callable from Invocation +  [InvocationExpression] <String(Integer)> (66:15-66:25)
|  +  [BaseMemberExpression] <String(Integer)(Integer)> (66:15-66:22) => Method[subtract:String(Integer)]
|  |  + subtract [Identifier] (66:15-66:22)
|  |  +  [InferredTypeArguments]
|  + () [PositionalArgumentList] (66:23-66:25)
|  |  +  [ListedArgument] (66:24-66:24) => ValueParameter[subtract#howMuch:Integer]
|  |  |  +  [Expression] <Integer> (66:24-66:24)
|  |  |  |  + 5 [NaturalLiteral] <Integer> (66:24-66:24)
*/[],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}}),$$$cl2592.String("-5",2));
    found$2926=find2(nums$2924,$$$cl2592.$JsCallable(function (i$2945){
        return i$2945.compare((2)).equals($$$cl2592.getLarger());
    },[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Boolean}}),{Element:{t:$$$cl2592.Integer}});
    var i$2946;
    if((i$2946=found$2926)!==null){
        $$$c2593.check(i$2946.equals((3)),$$$cl2592.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2593.fail($$$cl2592.String("anonfunc i>2 [2]",16));
    }
    found$2926=find2(nums$2924,undefined,{Element:{t:$$$cl2592.Integer}});
    var i$2947;
    if((i$2947=found$2926)!==null){
        $$$c2593.check(i$2947.equals((1)),$$$cl2592.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2593.fail($$$cl2592.String("anonfunc defaulted param [2]",28));
    }
};testAnonymous.$$metamodel$$={$nm:'testAnonymous',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testAnonymous.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl2592.print($$$cl2592.String("hello world",11));
}
exports.helloWorld=helloWorld;
helloWorld.$$metamodel$$={$nm:'helloWorld',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//helloWorld.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$2948){
    $$$cl2592.print($$$cl2592.String("hello",5).plus(name$2948));
}
exports.hello=hello;
hello.$$metamodel$$={$nm:'hello',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl2592.String}}]};//hello.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.Anything}};

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$2949){
    if(names$2949===undefined){names$2949=$$$cl2592.getEmpty();}
}
exports.helloAll=helloAll;
helloAll.$$metamodel$$={$nm:'helloAll',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}]};//helloAll.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}},Element:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}},Return:{t:$$$cl2592.Anything}};

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$2950){
    return obj$2950.string;
}
exports.toString=toString;
toString.$$metamodel$$={$nm:'toString',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'obj',$mt:'prm',$t:{t:$$$cl2592.Object}}]};//toString.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Object},Element:{t:$$$cl2592.Object}}},Return:{t:$$$cl2592.String}};

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$2951,y$2952){
    return x$2951.plus(y$2952);
}
exports.add=add;
add.$$metamodel$$={$nm:'add',$mt:'mthd',$t:{t:$$$cl2592.Float},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.Float}},{$nm:'y',$mt:'prm',$t:{t:$$$cl2592.Float}}]};//add.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Float},Element:{t:$$$cl2592.Float}}},Return:{t:$$$cl2592.Float}};

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$2953,f$2954){
    f$2954((0));
}
exports.repeat=repeat;
repeat.$$metamodel$$={$nm:'repeat',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'times',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'f',$mt:'prm',$t:{t:$$$cl2592.Anything}}]};//repeat.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Anything},Element:{t:$$$cl2592.Anything}}},Return:{t:$$$cl2592.Anything}};

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$2955, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl2592.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$2955=seq$2955;
    $$$cl2592.Sequence($$mySequence);
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl2592.initTypeProto(MySequence,'functions::MySequence',$$$cl2592.Basic,$$$cl2592.Sequence);
        (function($$mySequence){
            
            //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
            $$$cl2592.defineAttr($$mySequence,'lastIndex',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.lastIndex;
            });
            //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
            $$$cl2592.defineAttr($$mySequence,'first',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.first;
            });
            //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
            $$$cl2592.defineAttr($$mySequence,'rest',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.rest;
            });
            //MethodDefinition get at functions.ceylon (30:4-30:67)
            $$mySequence.get=function get(index$2956){
                var $$mySequence=this;
                return $$mySequence.seq$2955.get(index$2956);
            };$$mySequence.get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},'Element']},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition span at functions.ceylon (31:4-31:88)
            $$mySequence.span=function span(from$2957,to$2958){
                var $$mySequence=this;
                return $$mySequence.seq$2955.span(from$2957,to$2958);
            };$$mySequence.span.$$metamodel$$={$nm:'span',$mt:'mthd',$t:{t:$$$cl2592.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'to',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
            $$mySequence.spanFrom=function spanFrom(from$2959){
                var $$mySequence=this;
                return $$mySequence.seq$2955.spanFrom(from$2959);
            };$$mySequence.spanFrom.$$metamodel$$={$nm:'spanFrom',$mt:'mthd',$t:{t:$$$cl2592.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
            $$mySequence.spanTo=function spanTo(to$2960){
                var $$mySequence=this;
                return $$mySequence.seq$2955.spanTo(to$2960);
            };$$mySequence.spanTo.$$metamodel$$={$nm:'spanTo',$mt:'mthd',$t:{t:$$$cl2592.Sequential,a:{Element:'Element'}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition segment at functions.ceylon (34:4-34:102)
            $$mySequence.segment=function segment(from$2961,length$2962){
                var $$mySequence=this;
                return $$mySequence.seq$2955.segment(from$2961,length$2962);
            };$$mySequence.segment.$$metamodel$$={$nm:'segment',$mt:'mthd',$t:{t:$$$cl2592.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'length',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
            $$$cl2592.defineAttr($$mySequence,'clone',function(){
                var $$mySequence=this;
                return $$mySequence;
            });
            //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
            $$$cl2592.defineAttr($$mySequence,'string',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.string;
            });
            //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
            $$$cl2592.defineAttr($$mySequence,'hash',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.hash;
            });
            //MethodDefinition equals at functions.ceylon (38:4-38:75)
            $$mySequence.equals=function equals(other$2963){
                var $$mySequence=this;
                return $$mySequence.seq$2955.equals(other$2963);
            };$$mySequence.equals.$$metamodel$$={$nm:'equals',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl2592.Object}}]};
            //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
            $$$cl2592.defineAttr($$mySequence,'reversed',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.reversed;
            });
            //AttributeDeclaration last at functions.ceylon (40:4-40:42)
            $$$cl2592.defineAttr($$mySequence,'last',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.last;
            });
            
            //MethodDeclaration iterator at functions.ceylon (41:4-41:64)
            $$mySequence.iterator=function (){
                var $$mySequence=this;
                return $$mySequence.seq$2955.iterator();
            };
            iterator$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl2592.Iterator,a:{Element:'Element'}},$ps:[]};
            
            //AttributeDeclaration size at functions.ceylon (42:4-42:42)
            $$$cl2592.defineAttr($$mySequence,'size',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2955.size;
            });
            
            //MethodDeclaration contains at functions.ceylon (43:4-43:71)
            $$mySequence.contains=function (other$2964){
                var $$mySequence=this;
                return $$mySequence.seq$2955.contains(other$2964);
            };
            contains$$metamodel$$={$nm:'contains',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl2592.Object}}]};
        })(MySequence.$$.prototype);
    }
    MySequence.$$.$$metamodel$$={$nm:'MySequence',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Element:{'var':'out',}},'satisfies':[{t:$$$cl2592.Sequence,a:{Element:'Element'}}]};
    return MySequence;
}
exports.$init$MySequence=$init$MySequence;
$init$MySequence();

//ClassDefinition RefHelper at functions.ceylon (46:0-48:0)
function RefHelper($$refHelper){
    $init$RefHelper();
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    return $$refHelper;
}
function $init$RefHelper(){
    if (RefHelper.$$===undefined){
        $$$cl2592.initTypeProto(RefHelper,'functions::RefHelper',$$$cl2592.Basic);
        (function($$refHelper){
            
            //MethodDefinition f at functions.ceylon (47:4-47:47)
            $$refHelper.f=function f(i$2965){
                var $$refHelper=this;
                return true;
            };$$refHelper.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(RefHelper.$$.prototype);
    }
    RefHelper.$$.$$metamodel$$={$nm:'RefHelper',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDefinition testMethodReference at functions.ceylon (50:0-58:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (51:4-51:28)
    var obj1$2966=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$2967=MySequence($$$cl2592.Tuple($$$cl2592.String("hi",2),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Element:{t:$$$cl2592.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$2968(x$2969){
        return x$2969((0));
    };tst$2968.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.Boolean}}]};//tst$2968.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Boolean},Element:{t:$$$cl2592.Boolean}}},Return:{t:$$$cl2592.Boolean}};
    $$$c2593.check(tst$2968($$$cl2592.$JsCallable((opt$2970=obj1$2966,$$$cl2592.JsCallable(opt$2970,opt$2970!==null?opt$2970.f:null)),[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Boolean}})),$$$cl2592.String("Reference to method",19));
    var opt$2970;
    $$$c2593.check(tst$2968($$$cl2592.$JsCallable((opt$2971=obj2$2967,$$$cl2592.JsCallable(opt$2971,opt$2971!==null?opt$2971.defines:null)),[{$nm:'index',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Boolean}})),$$$cl2592.String("Reference to method from ceylon.language",40));
    var opt$2971;
};testMethodReference.$$metamodel$$={$nm:'testMethodReference',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testMethodReference.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$2972,i2$2973,i3$2974){
    if(i2$2973===undefined){i2$2973=i1$2972.plus((1));}
    if(i3$2974===undefined){i3$2974=i1$2972.plus(i2$2973);}
    return $$$cl2592.StringBuilder().appendAll([i1$2972.string,$$$cl2592.String(",",1),i2$2973.string,$$$cl2592.String(",",1),i3$2974.string]).string;
};defParamTest.$$metamodel$$={$nm:'defParamTest',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl2592.Integer}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl2592.Integer}}]};//defParamTest.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.String}};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$2975, i2$2976, i3$2977, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$2976===undefined){i2$2976=i1$2975.plus((1));}
    if(i3$2977===undefined){i3$2977=i1$2975.plus(i2$2976);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    $$defParamTest1.s$2978_=$$$cl2592.StringBuilder().appendAll([i1$2975.string,$$$cl2592.String(",",1),i2$2976.string,$$$cl2592.String(",",1),i3$2977.string]).string;
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl2592.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl2592.Basic);
        (function($$defParamTest1){
            
            //AttributeDeclaration s at functions.ceylon (64:4-64:44)
            $$$cl2592.defineAttr($$defParamTest1,'s',function(){return this.s$2978_;});
        })(DefParamTest1.$$.prototype);
    }
    DefParamTest1.$$.$$metamodel$$={$nm:'DefParamTest1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$2979, i2$2980, i3$2981, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$2979=i1$2979;
    if(i2$2980===undefined){i2$2980=$$defParamTest2.i1$2979.plus((1));}
    $$defParamTest2.i2$2980=i2$2980;
    if(i3$2981===undefined){i3$2981=$$defParamTest2.i1$2979.plus($$defParamTest2.i2$2980);}
    $$defParamTest2.i3$2981=i3$2981;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl2592.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl2592.Basic);
        (function($$defParamTest2){
            
            //MethodDefinition f at functions.ceylon (67:4-67:55)
            $$defParamTest2.f=function f(){
                var $$defParamTest2=this;
                return $$$cl2592.StringBuilder().appendAll([$$defParamTest2.i1$2979.string,$$$cl2592.String(",",1),$$defParamTest2.i2$2980.string,$$$cl2592.String(",",1),$$defParamTest2.i3$2981.string]).string;
            };$$defParamTest2.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(DefParamTest2.$$.prototype);
    }
    DefParamTest2.$$.$$metamodel$$={$nm:'DefParamTest2',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return DefParamTest2;
}
exports.$init$DefParamTest2=$init$DefParamTest2;
$init$DefParamTest2();

//ClassDefinition DefParamTest3 at functions.ceylon (69:0-73:0)
function DefParamTest3($$defParamTest3){
    $init$DefParamTest3();
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    return $$defParamTest3;
}
function $init$DefParamTest3(){
    if (DefParamTest3.$$===undefined){
        $$$cl2592.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl2592.Basic);
        (function($$defParamTest3){
            
            //MethodDefinition f at functions.ceylon (70:4-72:4)
            $$defParamTest3.f=function f(i1$2982,i2$2983,i3$2984){
                var $$defParamTest3=this;
                if(i2$2983===undefined){i2$2983=i1$2982.plus((1));}
                if(i3$2984===undefined){i3$2984=i1$2982.plus(i2$2983);}
                return $$$cl2592.StringBuilder().appendAll([i1$2982.string,$$$cl2592.String(",",1),i2$2983.string,$$$cl2592.String(",",1),i3$2984.string]).string;
            };$$defParamTest3.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl2592.Integer}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl2592.Integer}}]};
        })(DefParamTest3.$$.prototype);
    }
    DefParamTest3.$$.$$metamodel$$={$nm:'DefParamTest3',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2593.check(defParamTest((1)).equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted parameters 1",22));
    $$$c2593.check(defParamTest((1),(3)).equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted parameters 2",22));
    $$$c2593.check(defParamTest((1),(3),(0)).equals($$$cl2592.String("1,3,0",5)),$$$cl2592.String("defaulted parameters 3",22));
    $$$c2593.check((i1$2985=(1),defParamTest(i1$2985,undefined,undefined)).equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted parameters named 1",28));
    var i1$2985;
    $$$c2593.check((i1$2986=(1),i2$2987=(3),defParamTest(i1$2986,i2$2987,undefined)).equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted parameters named 2",28));
    var i1$2986,i2$2987;
    $$$c2593.check((i1$2988=(1),i3$2989=(0),defParamTest(i1$2988,undefined,i3$2989)).equals($$$cl2592.String("1,2,0",5)),$$$cl2592.String("defaulted parameters named 3",28));
    var i1$2988,i3$2989;
    $$$c2593.check(DefParamTest1((1)).s.equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted parameters class 1",28));
    $$$c2593.check(DefParamTest1((1),(3)).s.equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted parameters class 2",28));
    $$$c2593.check(DefParamTest1((1),(3),(0)).s.equals($$$cl2592.String("1,3,0",5)),$$$cl2592.String("defaulted parameters class 3",28));
    $$$c2593.check((i1$2990=(1),DefParamTest1(i1$2990,undefined,undefined)).s.equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted parameters class named 1",34));
    var i1$2990;
    $$$c2593.check((i1$2991=(1),i2$2992=(3),DefParamTest1(i1$2991,i2$2992,undefined)).s.equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted parameters class named 2",34));
    var i1$2991,i2$2992;
    $$$c2593.check((i1$2993=(1),i3$2994=(0),DefParamTest1(i1$2993,undefined,i3$2994)).s.equals($$$cl2592.String("1,2,0",5)),$$$cl2592.String("defaulted parameters class named 3",34));
    var i1$2993,i3$2994;
    $$$c2593.check(DefParamTest2((1)).f().equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted parameters class2 1",29));
    $$$c2593.check(DefParamTest2((1),(3)).f().equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted parameters class2 2",29));
    $$$c2593.check(DefParamTest2((1),(3),(0)).f().equals($$$cl2592.String("1,3,0",5)),$$$cl2592.String("defaulted parameters class2 3",29));
    $$$c2593.check((i1$2995=(1),DefParamTest2(i1$2995,undefined,undefined)).f().equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted parameters class2 named 1",35));
    var i1$2995;
    $$$c2593.check((i1$2996=(1),i2$2997=(3),DefParamTest2(i1$2996,i2$2997,undefined)).f().equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted parameters class2 named 2",35));
    var i1$2996,i2$2997;
    $$$c2593.check((i1$2998=(1),i3$2999=(0),DefParamTest2(i1$2998,undefined,i3$2999)).f().equals($$$cl2592.String("1,2,0",5)),$$$cl2592.String("defaulted parameters class2 named 3",35));
    var i1$2998,i3$2999;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$3000=DefParamTest3();
    $$$c2593.check(tst$3000.f((1)).equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted method parameters 1",29));
    $$$c2593.check(tst$3000.f((1),(3)).equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted method parameters 2",29));
    $$$c2593.check(tst$3000.f((1),(3),(0)).equals($$$cl2592.String("1,3,0",5)),$$$cl2592.String("defaulted method parameters 3",29));
    $$$c2593.check((i1$3001=(1),(opt$3002=tst$3000,$$$cl2592.JsCallable(opt$3002,opt$3002!==null?opt$3002.f:null))(i1$3001,undefined,undefined)).equals($$$cl2592.String("1,2,3",5)),$$$cl2592.String("defaulted method parameters named 1",35));
    var i1$3001,opt$3002;
    $$$c2593.check((i1$3003=(1),i2$3004=(3),(opt$3005=tst$3000,$$$cl2592.JsCallable(opt$3005,opt$3005!==null?opt$3005.f:null))(i1$3003,i2$3004,undefined)).equals($$$cl2592.String("1,3,4",5)),$$$cl2592.String("defaulted method parameters named 2",35));
    var i1$3003,i2$3004,opt$3005;
    $$$c2593.check((i1$3006=(1),i3$3007=(0),(opt$3008=tst$3000,$$$cl2592.JsCallable(opt$3008,opt$3008!==null?opt$3008.f:null))(i1$3006,undefined,i3$3007)).equals($$$cl2592.String("1,2,0",5)),$$$cl2592.String("defaulted method parameters named 3",35));
    var i1$3006,i3$3007,opt$3008;
};testDefaultedParams.$$metamodel$$={$nm:'testDefaultedParams',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testDefaultedParams.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$3009($$getterTest$3009){
        $init$GetterTest$3009();
        if ($$getterTest$3009===undefined)$$getterTest$3009=new GetterTest$3009.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        $$getterTest$3009.i$3010_=(0);
        return $$getterTest$3009;
    }
    function $init$GetterTest$3009(){
        if (GetterTest$3009.$$===undefined){
            $$$cl2592.initTypeProto(GetterTest$3009,'functions::testGetterMethodDefinitions.GetterTest',$$$cl2592.Basic);
            (function($$getterTest$3009){
                
                //AttributeDeclaration i at functions.ceylon (107:4-107:24)
                $$$cl2592.defineAttr($$getterTest$3009,'i$3010',function(){return this.i$3010_;},function(i$3011){return this.i$3010_=i$3011;});
                
                //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
                $$$cl2592.defineAttr($$getterTest$3009,'x',function(){
                    var $$getterTest$3009=this;
                    return ($$getterTest$3009.i$3010=$$getterTest$3009.i$3010.successor);
                });
            })(GetterTest$3009.$$.prototype);
        }
        GetterTest$3009.$$.$$metamodel$$={$nm:'GetterTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return GetterTest$3009;
    }
    $init$GetterTest$3009();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$3012=GetterTest$3009();
    $$$c2593.check(gt$3012.x.equals((1)),$$$cl2592.String("getter defined as method 1",26));
    $$$c2593.check(gt$3012.x.equals((2)),$$$cl2592.String("getter defined as method 2",26));
    $$$c2593.check(gt$3012.x.equals((3)),$$$cl2592.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;
testGetterMethodDefinitions.$$metamodel$$={$nm:'testGetterMethodDefinitions',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testGetterMethodDefinitions.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$3013,y$3014,z$3015){
    if(x$3013===undefined){x$3013=$$$cl2592.String("x",1);}
    if(y$3014===undefined){y$3014=x$3013.plus($$$cl2592.String("y",1));}
    if(z$3015===undefined){z$3015=$$$cl2592.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$3016=x$3013.plus($$$cl2592.String(",",1)).plus(y$3014);
    var setResult$3016=function(result$3017){return result$3016=result$3017;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$3018 = z$3015.iterator();
    var s$3019;while ((s$3019=it$3018.next())!==$$$cl2592.getFinished()){
        (result$3016=result$3016.plus($$$cl2592.String(",",1).plus(s$3019)));
    }
    return result$3016;
};namedArgFunc.$$metamodel$$={$nm:'namedArgFunc',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'x',$mt:'prm',$def:1,$t:{t:$$$cl2592.String}},{$nm:'y',$mt:'prm',$def:1,$t:{t:$$$cl2592.String}},{$nm:'z',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}]};//namedArgFunc.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}},Element:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}},Return:{t:$$$cl2592.String}};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$3020, more$3021, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$3021===undefined){more$3021=$$$cl2592.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    $$issue105.i$3022_=i$3020;
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl2592.initTypeProto(Issue105,'functions::Issue105',$$$cl2592.Basic);
        (function($$issue105){
            
            //AttributeDeclaration i at functions.ceylon (123:4-123:20)
            $$$cl2592.defineAttr($$issue105,'i',function(){return this.i$3022_;});
        })(Issue105.$$.prototype);
    }
    Issue105.$$.$$metamodel$$={$nm:'Issue105',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2593.check((namedArgFunc(undefined,undefined,$$$cl2592.getEmpty())).equals($$$cl2592.String("x,xy",4)),$$$cl2592.String("named arguments 1",17));
    $$$c2593.check((x$3023=$$$cl2592.String("a",1),namedArgFunc(x$3023,undefined,$$$cl2592.getEmpty())).equals($$$cl2592.String("a,ay",4)),$$$cl2592.String("named arguments 2",17));
    var x$3023;
    $$$c2593.check((y$3024=$$$cl2592.String("b",1),namedArgFunc(undefined,y$3024,$$$cl2592.getEmpty())).equals($$$cl2592.String("x,b",3)),$$$cl2592.String("named arguments 3",17));
    var y$3024;
    $$$c2593.check((z$3025=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),namedArgFunc(undefined,undefined,z$3025)).equals($$$cl2592.String("x,xy,c",6)),$$$cl2592.String("named arguments 4",17));
    var z$3025;
    $$$c2593.check((x$3026=$$$cl2592.String("a",1),y$3027=$$$cl2592.String("b",1),z$3028=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),namedArgFunc(x$3026,y$3027,z$3028)).equals($$$cl2592.String("a,b,c",5)),$$$cl2592.String("named arguments 5",17));
    var x$3026,y$3027,z$3028;
    $$$c2593.check((y$3029=$$$cl2592.String("b",1),x$3030=$$$cl2592.String("a",1),z$3031=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.Tuple($$$cl2592.String("d",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),namedArgFunc(x$3030,y$3029,z$3031)).equals($$$cl2592.String("a,b,c,d",7)),$$$cl2592.String("named arguments 6",17));
    var y$3029,x$3030,z$3031;
    $$$c2593.check((x$3032=$$$cl2592.String("a",1),z$3033=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),namedArgFunc(x$3032,undefined,z$3033)).equals($$$cl2592.String("a,ay,c",6)),$$$cl2592.String("named arguments 7",17));
    var x$3032,z$3033;
    $$$c2593.check((y$3034=$$$cl2592.String("b",1),z$3035=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),namedArgFunc(undefined,y$3034,z$3035)).equals($$$cl2592.String("x,b,c",5)),$$$cl2592.String("named arguments 8",17));
    var y$3034,z$3035;
    $$$c2593.check((y$3036=$$$cl2592.String("b",1),x$3037=$$$cl2592.String("a",1),namedArgFunc(x$3037,y$3036,$$$cl2592.getEmpty())).equals($$$cl2592.String("a,b",3)),$$$cl2592.String("named arguments 9",17));
    var y$3036,x$3037;
    $$$c2593.check((z$3038=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.Tuple($$$cl2592.String("d",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),namedArgFunc(undefined,undefined,z$3038)).equals($$$cl2592.String("x,xy,c,d",8)),$$$cl2592.String("named arguments 11",18));
    var z$3038;
    $$$c2593.check((y$3039=$$$cl2592.String("b",1),z$3040=$$$cl2592.Tuple($$$cl2592.String("c",1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),x$3041=$$$cl2592.String("a",1),namedArgFunc(x$3041,y$3039,z$3040)).equals($$$cl2592.String("a,b,c",5)),$$$cl2592.String("named arguments 12",18));
    var y$3039,z$3040,x$3041;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$3042=(i$3043=(1),more$3044=$$$cl2592.Tuple((i$3045=(2),Issue105(i$3045,$$$cl2592.getEmpty())),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$3043,more$3044));
    var i$3043,more$3044,i$3045;
    $$$c2593.check(issue105$3042.i.equals((1)),$$$cl2592.String("issue #105",10));
};testNamedArguments.$$metamodel$$={$nm:'testNamedArguments',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testNamedArguments.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl2592.initTypeProto(LazyExprBase,'functions::LazyExprBase');
        (function($$lazyExprBase){
        })(LazyExprBase.$$.prototype);
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
    $$lazyExprTest.x$3046_=(1000);
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl2592.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl2592.Basic,$init$LazyExprBase());
        (function($$lazyExprTest){
            
            //AttributeDeclaration x at functions.ceylon (149:4-149:36)
            $$$cl2592.defineAttr($$lazyExprTest,'x',function(){return this.x$3046_;},function(x$3047){return this.x$3046_=x$3047;});
            
            //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
            $$lazyExprTest.f1=function (i$3048,f$3049){
                var $$lazyExprTest=this;
                if(f$3049===undefined){f$3049=function (){
                    return $$$cl2592.StringBuilder().appendAll([i$3048.string,$$$cl2592.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor).string]).string;
                };}
                return $$$cl2592.StringBuilder().appendAll([i$3048.string,$$$cl2592.String(":",1),f$3049().string]).string;
            };
            f1$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl2592.String}}]};
            
            //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
            $$lazyExprTest.f2=function (i$3050){
                var $$lazyExprTest=this;
                return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$3050);
            };
            f2$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            
            //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
            $$$cl2592.defineAttr($$lazyExprTest,'i1',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor);
            });
            
            //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
            $$$cl2592.defineAttr($$lazyExprTest,'i2',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor).times((2));
            });
            $$$cl2592.defineAttr($$lazyExprTest,'s1',function(){
                var $$lazyExprTest=this;
                return $$$cl2592.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl2592.String(".1",2)]).string;
            });
            $$lazyExprTest.s2=function (i$3051){
                var $$lazyExprTest=this;
                return $$$cl2592.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl2592.String(".",1),i$3051.string]).string;
            };
            //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
            $$lazyExprTest.f3=function (f$3052){
                var $$lazyExprTest=this;
                return f$3052(($$lazyExprTest.x=$$lazyExprTest.x.successor));
            };
            f3$$metamodel$$={$nm:'f3',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl2592.String}}]};
        })(LazyExprTest.$$.prototype);
    }
    LazyExprTest.$$.$$metamodel$$={$nm:'LazyExprTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:LazyExprBase}]};
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$3053=(1000);
var getLx=function(){return lx$3053;};
exports.getLx=getLx;
var setLx=function(lx$3054){return lx$3053=lx$3054;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$3055,f$3056){
    if(f$3056===undefined){f$3056=function (){
        return $$$cl2592.StringBuilder().appendAll([i$3055.string,$$$cl2592.String(".",1),(setLx(getLx().successor)).string]).string;
    };}
    return f$3056();
};
lazy_f1$$metamodel$$={$nm:'lazy_f1',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl2592.String}}]};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$3057){
    return (2).times((setLx(getLx().successor))).plus(i$3057);
};
lazy_f2$$metamodel$$={$nm:'lazy_f2',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};

//AttributeDeclaration lazy_i1 at functions.ceylon (164:0-164:23)
var getLazy_i1=function(){return (setLx(getLx().successor));};
exports.getLazy_i1=getLazy_i1;

//ClassDefinition LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDeclaration x at functions.ceylon (167:4-167:35)
    $$lazyExprTest2.x$3058_=(1000);
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl2592.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl2592.Basic,$init$LazyExprBase());
        (function($$lazyExprTest2){
            
            //AttributeDeclaration x at functions.ceylon (167:4-167:35)
            $$$cl2592.defineAttr($$lazyExprTest2,'x',function(){return this.x$3058_;},function(x$3059){return this.x$3058_=x$3059;});
            
            //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
            $$$cl2592.defineAttr($$lazyExprTest2,'s1',function(){
                var $$lazyExprTest2=this;
                return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;
            });
            
            //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
            $$lazyExprTest2.s2=function (i$3060){
                var $$lazyExprTest2=this;
                return $$$cl2592.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl2592.String("-",1),i$3060.string]).string;
            };
            s2$$metamodel$$={$nm:'s2',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(LazyExprTest2.$$.prototype);
    }
    LazyExprTest2.$$.$$metamodel$$={$nm:'LazyExprTest2',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:LazyExprBase}]};
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
    $$lazyExprTest3.s1$3061_=$$$cl2592.String("s1",2);
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl2592.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
        (function($$lazyExprTest3){
            
            //AttributeDeclaration s1 at functions.ceylon (172:4-172:43)
            $$$cl2592.defineAttr($$lazyExprTest3,'s1',function(){return this.s1$3061_;},function(s1$3062){return this.s1$3061_=s1$3062;});
        })(LazyExprTest3.$$.prototype);
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
    
    //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
    $$lazyExprTest4.assigned$3063_=$$$cl2592.String("",0);
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl2592.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
        (function($$lazyExprTest4){
            
            //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
            $$$cl2592.defineAttr($$lazyExprTest4,'assigned',function(){return this.assigned$3063_;},function(assigned$3064){return this.assigned$3063_=assigned$3064;});
            
            //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
            $$$cl2592.defineAttr($$lazyExprTest4,'s1',function(){
                var $$lazyExprTest4=this;
                return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("s1-",3),$$$cl2592.attrGetter($$lazyExprTest4.getT$all()['functions::LazyExprTest2'],'s1').call(this).string]).string;
            },function(s1$3065){
                var $$lazyExprTest4=this;
                $$lazyExprTest4.assigned=s1$3065;
            });
        })(LazyExprTest4.$$.prototype);
    }
    LazyExprTest4.$$.$$metamodel$$={$nm:'LazyExprTest4',$mt:'cls','super':{t:LazyExprTest2},'satisfies':[]};
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDefinition testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDeclaration tst at functions.ceylon (181:4-181:30)
    var tst$3066=LazyExprTest();
    (tst$3066.x=(1));
    $$$c2593.check(tst$3066.f1((3)).equals($$$cl2592.String("3:3.2",5)),$$$cl2592.String("=> defaulted param",18));
    $$$c2593.check(tst$3066.f2((3)).equals((9)),$$$cl2592.String("=> method",9));
    $$$c2593.check(tst$3066.i1.equals((4)),$$$cl2592.String("=> attribute",12));
    $$$c2593.check(tst$3066.i2.equals((10)),$$$cl2592.String("=> attribute specifier",22));
    $$$c2593.check(tst$3066.s1.equals($$$cl2592.String("6.1",3)),$$$cl2592.String("=> attribute refinement",23));
    $$$c2593.check(tst$3066.s2((5)).equals($$$cl2592.String("7.5",3)),$$$cl2592.String("=> method refinement",20));
    setLx((1));
    $$$c2593.check(lazy_f1((3)).equals($$$cl2592.String("3.2",3)),$$$cl2592.String("=> defaulted param toplevel",27));
    $$$c2593.check(lazy_f2((3)).equals((9)),$$$cl2592.String("=> method toplevel",18));
    $$$c2593.check(getLazy_i1().equals((4)),$$$cl2592.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$3067=(1000);
    var setX$3067=function(x$3068){return x$3067=x$3068;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$3069=function (i$3070,f$3071){
        if(f$3071===undefined){f$3071=function (){
            return $$$cl2592.StringBuilder().appendAll([i$3070.string,$$$cl2592.String(".",1),(x$3067=x$3067.successor).string]).string;
        };}
        return f$3071();
    };
    f1$3069$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'f',$mt:'prm',$def:1,$t:{t:$$$cl2592.String}}]};
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$3072=function (i$3073){
        return (2).times((x$3067=x$3067.successor)).plus(i$3073);
    };
    f2$3072$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$3074=function(){return (x$3067=x$3067.successor);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$3075;
    var getI2$3075=function(){
        return (x$3067=x$3067.successor).times((2));
    };
    x$3067=(1);
    $$$c2593.check(f1$3069((3)).equals($$$cl2592.String("3.2",3)),$$$cl2592.String("=> defaulted param local",24));
    $$$c2593.check(f2$3072((3)).equals((9)),$$$cl2592.String("=> method local",15));
    $$$c2593.check(getI1$3074().equals((4)),$$$cl2592.String("=> attribute local",18));
    $$$c2593.check(getI2$3075().equals((10)),$$$cl2592.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$3076=LazyExprTest3();
    (tst3$3076.x=(1));
    $$$c2593.check(tst3$3076.s1.equals($$$cl2592.String("s1",2)),$$$cl2592.String("=> override variable 1",22));
    (tst3$3076.s1=$$$cl2592.String("abc",3));
    $$$c2593.check(tst3$3076.s1.equals($$$cl2592.String("abc",3)),$$$cl2592.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$3077=LazyExprTest4();
    (tst4$3077.x=(1));
    $$$c2593.check(tst4$3077.s1.equals($$$cl2592.String("s1-2",4)),$$$cl2592.String("=> override getter/setter 1",27));
    (tmp$3078=tst4$3077,tmp$3078.s1=$$$cl2592.String("abc",3),tmp$3078.s1);
    var tmp$3078;
    $$$c2593.check(tst4$3077.s1.equals($$$cl2592.String("s1-4",4)),$$$cl2592.String("=> override getter/setter 2",27));
    $$$c2593.check(tst4$3077.assigned.equals($$$cl2592.String("abc",3)),$$$cl2592.String("=> override getter/setter 3",27));
    (tst$3066.x=(1));
    x$3067=(10);
    $$$c2593.check((i$3079=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$3067=x$3067.successor);
    }()),(opt$3080=tst$3066,$$$cl2592.JsCallable(opt$3080,opt$3080!==null?opt$3080.f1:null))(i$3079,undefined)).equals($$$cl2592.String("11:11.2",7)),$$$cl2592.String("=> named arg",12));
    var i$3079,opt$3080;
    $$$c2593.check((i$3081=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$3067=x$3067.successor);
    }()),f$3082=function (){return (x$3067=x$3067.successor).string;},(opt$3083=tst$3066,$$$cl2592.JsCallable(opt$3083,opt$3083!==null?opt$3083.f1:null))(i$3081,f$3082)).equals($$$cl2592.String("12:13",5)),$$$cl2592.String("=> named arg function",21));
    var i$3081,f$3082,opt$3083;
    $$$c2593.check((f$3084=function (i$3085){return $$$cl2592.StringBuilder().appendAll([i$3085.string,$$$cl2592.String("-",1),(x$3067=x$3067.successor).string]).string;},(opt$3086=tst$3066,$$$cl2592.JsCallable(opt$3086,opt$3086!==null?opt$3086.f3:null))(f$3084)).equals($$$cl2592.String("3-14",4)),$$$cl2592.String("=> named arg function with param",32));
    var f$3084,opt$3086;
};testLazyExpressions.$$metamodel$$={$nm:'testLazyExpressions',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testLazyExpressions.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl2592.String("test",4));
    helloAll([$$$cl2592.String("Gavin",5),$$$cl2592.String("Enrique",7),$$$cl2592.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}));
    $$$c2593.check(toString((5)).equals($$$cl2592.String("5",1)),$$$cl2592.String("toString(obj)",13));
    $$$c2593.check(add($$$cl2592.Float(1.5),$$$cl2592.Float(2.5)).equals($$$cl2592.Float(4.0)),$$$cl2592.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$3087,y$3088){
        return x$3087.compare(y$3088);
    }
};multiCompare.$$metamodel$$={$nm:'multiCompare',$mt:'mthd',$t:{t:$$$cl2592.Comparison},$ps:[]};//multiCompare.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Comparison}};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$3089){
    return function(apat$3090){
        return function(amat$3091){
            return $$$cl2592.StringBuilder().appendAll([nombre$3089.string,$$$cl2592.String(" ",1),apat$3090.string,$$$cl2592.String(" ",1),amat$3091.string]).string;
        }
    }
};multiFullname.$$metamodel$$={$nm:'multiFullname',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'nombre',$mt:'prm',$t:{t:$$$cl2592.String}}]};//multiFullname.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$3092){
    if(name$3092===undefined){name$3092=$$$cl2592.String("A",1);}
    return function(apat$3093){
        return function(amat$3094){
            return $$$cl2592.StringBuilder().appendAll([name$3092.string,$$$cl2592.String(" ",1),apat$3093.string,$$$cl2592.String(" ",1),amat$3094.string]).string;
        }
    }
};multiDefaulted.$$metamodel$$={$nm:'multiDefaulted',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'name',$mt:'prm',$def:1,$t:{t:$$$cl2592.String}}]};//multiDefaulted.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$3095){
    if(names$3095===undefined){names$3095=$$$cl2592.getEmpty();}
    return function(count$3096){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$3097=$$$cl2592.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$3098 = names$3095.iterator();
        var name$3099;while ((name$3099=it$3098.next())!==$$$cl2592.getFinished()){
            sb$3097.append(name$3099).append($$$cl2592.String(" ",1));
        }
        sb$3097.append($$$cl2592.String("count ",6)).append(count$3096.string);
        return sb$3097.string;
    }
};multiSequenced.$$metamodel$$={$nm:'multiSequenced',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}]};//multiSequenced.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}},Element:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.String}}}}},Return:{t:$$$cl2592.String}};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl2592.print($$$cl2592.String("Testing multiple parameter lists...",35));
    $$$c2593.check(multiCompare()((1),(1)).equals($$$cl2592.getEqual()),$$$cl2592.String("Multi compare 1",15));
    $$$c2593.check(multiCompare()((1),(2)).equals($$$cl2592.getSmaller()),$$$cl2592.String("Multi compare 2",15));
    $$$c2593.check(multiCompare()((2),(1)).equals($$$cl2592.getLarger()),$$$cl2592.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$3100=function (a$3101,b$3102){
        return multiCompare()(a$3101,b$3102);
    };
    comp$3100$$metamodel$$={$nm:'comp',$mt:'mthd',$t:{t:$$$cl2592.Comparison},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'b',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
    $$$c2593.check(comp$3100((1),(1)).equals($$$cl2592.getEqual()),$$$cl2592.String("Multi compare 4",15));
    $$$c2593.check(comp$3100((1),(2)).equals($$$cl2592.getSmaller()),$$$cl2592.String("Multi compare 5",15));
    $$$c2593.check(comp$3100((2),(1)).equals($$$cl2592.getLarger()),$$$cl2592.String("Multi compare 6",15));
    $$$c2593.check(multiFullname($$$cl2592.String("a",1))($$$cl2592.String("b",1).valueOf())($$$cl2592.String("c",1).valueOf()).equals($$$cl2592.String("a b c",5)),$$$cl2592.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$3103=function (c$3104){
        return multiFullname($$$cl2592.String("A",1))($$$cl2592.String("B",1).valueOf())(c$3104.valueOf());
    };
    apat$3103$$metamodel$$={$nm:'apat',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl2592.String}}]};
    $$$c2593.check(apat$3103($$$cl2592.String("C",1)).equals($$$cl2592.String("A B C",5)),$$$cl2592.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$3105=function (name$3106){
        return multiFullname($$$cl2592.String("Name",4))(name$3106.valueOf());
    };
    nombre$3105$$metamodel$$={$nm:'nombre',$mt:'mthd',$t:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}}},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl2592.String}}]};
    $$$c2593.check(nombre$3105($$$cl2592.String("Z",1))($$$cl2592.String("L",1).valueOf()).equals($$$cl2592.String("Name Z L",8)),$$$cl2592.String("Multi callable 2",16));
    $$$c2593.check(multiDefaulted()($$$cl2592.String("B",1).valueOf())($$$cl2592.String("C",1).valueOf()).equals($$$cl2592.String("A B C",5)),$$$cl2592.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$3107=$$$cl2592.$JsCallable(multiDefaulted(),[],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}}}});
    $$$c2593.check(md1$3107($$$cl2592.String("B",1).valueOf())($$$cl2592.String("C",1).valueOf()).equals($$$cl2592.String("A B C",5)),$$$cl2592.String("Multi defaulted 2",17));
    $$$c2593.check(md1$3107($$$cl2592.String("B",1).valueOf())($$$cl2592.String("Z",1).valueOf()).equals($$$cl2592.String("A B Z",5)),$$$cl2592.String("Multi defaulted 3",17));
    $$$c2593.check(md1$3107($$$cl2592.String("Z",1).valueOf())($$$cl2592.String("C",1).valueOf()).equals($$$cl2592.String("A Z C",5)),$$$cl2592.String("Multi defaulted 4",17));
    $$$c2593.check(md1$3107($$$cl2592.String("Y",1).valueOf())($$$cl2592.String("Z",1).valueOf()).equals($$$cl2592.String("A Y Z",5)),$$$cl2592.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$3108=function (x$3109){
        return multiDefaulted()($$$cl2592.String("B",1).valueOf())(x$3109.valueOf());
    };
    md2$3108$$metamodel$$={$nm:'md2',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.String}}]};
    $$$c2593.check(md2$3108($$$cl2592.String("C",1)).equals($$$cl2592.String("A B C",5)),$$$cl2592.String("Multi defaulted 6",17));
    $$$c2593.check(md2$3108($$$cl2592.String("Z",1)).equals($$$cl2592.String("A B Z",5)),$$$cl2592.String("Multi defaulted 7",17));
    $$$c2593.check(multiSequenced([$$$cl2592.String("A",1),$$$cl2592.String("B",1),$$$cl2592.String("C",1)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}))((1)).equals($$$cl2592.String("A B C count 1",13)),$$$cl2592.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$3110=function (c$3111){
        return multiSequenced([$$$cl2592.String("x",1),$$$cl2592.String("y",1),$$$cl2592.String("z",1)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}))(c$3111);
    };
    ms1$3110$$metamodel$$={$nm:'ms1',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
    $$$c2593.check(ms1$3110((5)).equals($$$cl2592.String("x y z count 5",13)),$$$cl2592.String("Multi sequenced 2",17));
    $$$c2593.check(ms1$3110((10)).equals($$$cl2592.String("x y z count 10",14)),$$$cl2592.String("Multi sequenced 3",17));
};testMultipleParamLists.$$metamodel$$={$nm:'testMultipleParamLists',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testMultipleParamLists.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
