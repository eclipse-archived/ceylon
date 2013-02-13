(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"last":{"$t":{"$nm":"Element"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2244=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$2428,f$2429,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$2430 = a$2428.getIterator();
    var e$2431;while ((e$2431=it$2430.next())!==$$$cl2243.getFinished()){
        if(f$2429(e$2431)){
            return e$2431;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$2432,f$2433,$$$mptypes){
    if(f$2433===undefined){f$2433=function (x$2434){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$2435 = a$2432.getIterator();
    var e$2436;while ((e$2436=it$2435.next())!==$$$cl2243.getFinished()){
        if(f$2433(e$2436)){
            return e$2436;
        }
    }
    if ($$$cl2243.getFinished() === e$2436){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$2437){
    return function (i$2438){
        return i$2438.minus(howMuch$2437).getString();
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl2243.print($$$cl2243.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$2439=(elements$2440=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),$$$cl2243.array(elements$2440,{Element:{t:$$$cl2243.Integer}}));
    var elements$2440;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$2441=find(nums$2439,function (i$2442){
        return i$2442.remainder((2)).equals((0));
    },{Element:{t:$$$cl2243.Integer}});
    var setFound$2441=function(found$2443){return found$2441=found$2443;};
    var i$2444;
    if((i$2444=found$2441)!==null){
        $$$c2244.check(i$2444.equals((2)),$$$cl2243.String("anonfunc positional",19));
    }else {
        $$$c2244.fail($$$cl2243.String("anonfunc positional",19));
    }
    found$2441=(f$2445=function (i$2446){
        return i$2446.remainder((2)).equals((0));
    },a$2447=nums$2439,find(a$2447,f$2445,{Element:{t:$$$cl2243.Integer}}));
    var f$2445,a$2447;
    var i$2448;
    if((i$2448=found$2441)!==null){
        $$$c2244.check(i$2448.equals((2)),$$$cl2243.String("anonfunc named",14));
    }else {
        $$$c2244.fail($$$cl2243.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$2449(f$2450,expect$2451){
        $$$c2244.check(f$2450((0)).equals(expect$2451),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("anon func returns ",18),f$2450((0)).getString(),$$$cl2243.String(" instead of ",12),expect$2451.getString()]).getString());
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$2452(i$2453){
        return i$2453.plus((12)).getString();
    };
    callFunction$2449(f$2452,$$$cl2243.String("12",2));
    callFunction$2449(function (i$2454){
        return i$2454.times((3)).getString();
    },$$$cl2243.String("0",1));
    (expect$2455=$$$cl2243.String("0",1),f$2456=function (i$2457){
        return i$2457.power((2)).getString();
    },callFunction$2449(f$2456,expect$2455));
    var expect$2455,f$2456;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$2458=function (i$2459){
        return i$2459.minus((10)).getString();
    };
    callFunction$2449(f2$2458,$$$cl2243.String("-10",3));
    callFunction$2449(subtract((5)),$$$cl2243.String("-5",2));
    found$2441=find2(nums$2439,function (i$2460){
        return i$2460.compare((2)).equals($$$cl2243.getLarger());
    },{Element:{t:$$$cl2243.Integer}});
    var i$2461;
    if((i$2461=found$2441)!==null){
        $$$c2244.check(i$2461.equals((3)),$$$cl2243.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2244.fail($$$cl2243.String("anonfunc i>2 [2]",16));
    }
    found$2441=find2(nums$2439,undefined,{Element:{t:$$$cl2243.Integer}});
    var i$2462;
    if((i$2462=found$2441)!==null){
        $$$c2244.check(i$2462.equals((1)),$$$cl2243.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2244.fail($$$cl2243.String("anonfunc defaulted param [2]",28));
    }
};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl2243.print($$$cl2243.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$2463){
    $$$cl2243.print($$$cl2243.String("hello",5).plus(name$2463));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$2464){
    if(names$2464===undefined){names$2464=$$$cl2243.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$2465){
    return obj$2465.getString();
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$2466,y$2467){
    return x$2466.plus(y$2467);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$2468,f$2469){
    f$2469((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$2470, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl2243.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$2470=seq$2470;
    $$$cl2243.Sequence($$mySequence);
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    
    //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl2243.initTypeProto(MySequence,'functions::MySequence',$$$cl2243.Basic,$$$cl2243.Sequence);
        (function($$mySequence){
            
            //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
            $$mySequence.getLastIndex=function getLastIndex(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getLastIndex();
            };
            //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
            $$mySequence.getFirst=function getFirst(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getFirst();
            };
            //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
            $$mySequence.getRest=function getRest(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getRest();
            };
            //MethodDefinition get at functions.ceylon (30:4-30:67)
            $$mySequence.get=function get(index$2471){
                var $$mySequence=this;
                return $$mySequence.seq$2470.get(index$2471);
            };
            //MethodDefinition span at functions.ceylon (31:4-31:88)
            $$mySequence.span=function span(from$2472,to$2473){
                var $$mySequence=this;
                return $$mySequence.seq$2470.span(from$2472,to$2473);
            };
            //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
            $$mySequence.spanFrom=function spanFrom(from$2474){
                var $$mySequence=this;
                return $$mySequence.seq$2470.spanFrom(from$2474);
            };
            //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
            $$mySequence.spanTo=function spanTo(to$2475){
                var $$mySequence=this;
                return $$mySequence.seq$2470.spanTo(to$2475);
            };
            //MethodDefinition segment at functions.ceylon (34:4-34:102)
            $$mySequence.segment=function segment(from$2476,length$2477){
                var $$mySequence=this;
                return $$mySequence.seq$2470.segment(from$2476,length$2477);
            };
            //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
            $$mySequence.getClone=function getClone(){
                var $$mySequence=this;
                return $$mySequence;
            };
            //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
            $$mySequence.getString=function getString(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getString();
            };
            //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
            $$mySequence.getHash=function getHash(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getHash();
            };
            //MethodDefinition equals at functions.ceylon (38:4-38:75)
            $$mySequence.equals=function equals(other$2478){
                var $$mySequence=this;
                return $$mySequence.seq$2470.equals(other$2478);
            };
            //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
            $$mySequence.getReversed=function getReversed(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getReversed();
            };
            //AttributeDeclaration last at functions.ceylon (40:4-40:42)
            $$mySequence.getLast=function getLast(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getLast();
            };
            
            //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
            $$mySequence.getIterator=function getIterator(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getIterator();
            };
            
            //AttributeDeclaration size at functions.ceylon (42:4-42:42)
            $$mySequence.getSize=function getSize(){
                var $$mySequence=this;
                return $$mySequence.seq$2470.getSize();
            };
            
            //MethodDeclaration contains at functions.ceylon (43:4-43:71)
            $$mySequence.contains=function (other$2479){
                var $$mySequence=this;
                return $$mySequence.seq$2470.contains(other$2479);
            };
        })(MySequence.$$.prototype);
    }
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
        $$$cl2243.initTypeProto(RefHelper,'functions::RefHelper',$$$cl2243.Basic);
        (function($$refHelper){
            
            //MethodDefinition f at functions.ceylon (47:4-47:47)
            $$refHelper.f=function f(i$2480){
                var $$refHelper=this;
                return true;
            };
        })(RefHelper.$$.prototype);
    }
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDefinition testMethodReference at functions.ceylon (50:0-58:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (51:4-51:28)
    var obj1$2481=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$2482=MySequence($$$cl2243.Tuple($$$cl2243.String("hi",2),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Element:{t:$$$cl2243.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$2483(x$2484){
        return x$2484((0));
    };
    $$$c2244.check(tst$2483((opt$2485=obj1$2481,$$$cl2243.JsCallable(opt$2485,opt$2485!==null?opt$2485.f:null))),$$$cl2243.String("Reference to method",19));
    var opt$2485;
    $$$c2244.check(tst$2483((opt$2486=obj2$2482,$$$cl2243.JsCallable(opt$2486,opt$2486!==null?opt$2486.defines:null))),$$$cl2243.String("Reference to method from ceylon.language",40));
    var opt$2486;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$2487,i2$2488,i3$2489){
    if(i2$2488===undefined){i2$2488=i1$2487.plus((1));}
    if(i3$2489===undefined){i3$2489=i1$2487.plus(i2$2488);}
    return $$$cl2243.StringBuilder().appendAll([i1$2487.getString(),$$$cl2243.String(",",1),i2$2488.getString(),$$$cl2243.String(",",1),i3$2489.getString()]).getString();
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$2490, i2$2491, i3$2492, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$2491===undefined){i2$2491=i1$2490.plus((1));}
    if(i3$2492===undefined){i3$2492=i1$2490.plus(i2$2491);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    $$defParamTest1.s$2493=$$$cl2243.StringBuilder().appendAll([i1$2490.getString(),$$$cl2243.String(",",1),i2$2491.getString(),$$$cl2243.String(",",1),i3$2492.getString()]).getString();
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl2243.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl2243.Basic);
        (function($$defParamTest1){
            
            //AttributeDeclaration s at functions.ceylon (64:4-64:44)
            $$defParamTest1.getS=function getS(){
                return this.s$2493;
            };
        })(DefParamTest1.$$.prototype);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$2494, i2$2495, i3$2496, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$2494=i1$2494;
    if(i2$2495===undefined){i2$2495=$$defParamTest2.i1$2494.plus((1));}
    $$defParamTest2.i2$2495=i2$2495;
    if(i3$2496===undefined){i3$2496=$$defParamTest2.i1$2494.plus($$defParamTest2.i2$2495);}
    $$defParamTest2.i3$2496=i3$2496;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl2243.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl2243.Basic);
        (function($$defParamTest2){
            
            //MethodDefinition f at functions.ceylon (67:4-67:55)
            $$defParamTest2.f=function f(){
                var $$defParamTest2=this;
                return $$$cl2243.StringBuilder().appendAll([$$defParamTest2.i1$2494.getString(),$$$cl2243.String(",",1),$$defParamTest2.i2$2495.getString(),$$$cl2243.String(",",1),$$defParamTest2.i3$2496.getString()]).getString();
            };
        })(DefParamTest2.$$.prototype);
    }
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
        $$$cl2243.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl2243.Basic);
        (function($$defParamTest3){
            
            //MethodDefinition f at functions.ceylon (70:4-72:4)
            $$defParamTest3.f=function f(i1$2497,i2$2498,i3$2499){
                var $$defParamTest3=this;
                if(i2$2498===undefined){i2$2498=i1$2497.plus((1));}
                if(i3$2499===undefined){i3$2499=i1$2497.plus(i2$2498);}
                return $$$cl2243.StringBuilder().appendAll([i1$2497.getString(),$$$cl2243.String(",",1),i2$2498.getString(),$$$cl2243.String(",",1),i3$2499.getString()]).getString();
            };
        })(DefParamTest3.$$.prototype);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2244.check(defParamTest((1)).equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted parameters 1",22));
    $$$c2244.check(defParamTest((1),(3)).equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted parameters 2",22));
    $$$c2244.check(defParamTest((1),(3),(0)).equals($$$cl2243.String("1,3,0",5)),$$$cl2243.String("defaulted parameters 3",22));
    $$$c2244.check((i1$2500=(1),defParamTest(i1$2500,undefined,undefined)).equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted parameters named 1",28));
    var i1$2500;
    $$$c2244.check((i1$2501=(1),i2$2502=(3),defParamTest(i1$2501,i2$2502,undefined)).equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted parameters named 2",28));
    var i1$2501,i2$2502;
    $$$c2244.check((i1$2503=(1),i3$2504=(0),defParamTest(i1$2503,undefined,i3$2504)).equals($$$cl2243.String("1,2,0",5)),$$$cl2243.String("defaulted parameters named 3",28));
    var i1$2503,i3$2504;
    $$$c2244.check(DefParamTest1((1)).getS().equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted parameters class 1",28));
    $$$c2244.check(DefParamTest1((1),(3)).getS().equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted parameters class 2",28));
    $$$c2244.check(DefParamTest1((1),(3),(0)).getS().equals($$$cl2243.String("1,3,0",5)),$$$cl2243.String("defaulted parameters class 3",28));
    $$$c2244.check((i1$2505=(1),DefParamTest1(i1$2505,undefined,undefined)).getS().equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted parameters class named 1",34));
    var i1$2505;
    $$$c2244.check((i1$2506=(1),i2$2507=(3),DefParamTest1(i1$2506,i2$2507,undefined)).getS().equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted parameters class named 2",34));
    var i1$2506,i2$2507;
    $$$c2244.check((i1$2508=(1),i3$2509=(0),DefParamTest1(i1$2508,undefined,i3$2509)).getS().equals($$$cl2243.String("1,2,0",5)),$$$cl2243.String("defaulted parameters class named 3",34));
    var i1$2508,i3$2509;
    $$$c2244.check(DefParamTest2((1)).f().equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted parameters class2 1",29));
    $$$c2244.check(DefParamTest2((1),(3)).f().equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted parameters class2 2",29));
    $$$c2244.check(DefParamTest2((1),(3),(0)).f().equals($$$cl2243.String("1,3,0",5)),$$$cl2243.String("defaulted parameters class2 3",29));
    $$$c2244.check((i1$2510=(1),DefParamTest2(i1$2510,undefined,undefined)).f().equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted parameters class2 named 1",35));
    var i1$2510;
    $$$c2244.check((i1$2511=(1),i2$2512=(3),DefParamTest2(i1$2511,i2$2512,undefined)).f().equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted parameters class2 named 2",35));
    var i1$2511,i2$2512;
    $$$c2244.check((i1$2513=(1),i3$2514=(0),DefParamTest2(i1$2513,undefined,i3$2514)).f().equals($$$cl2243.String("1,2,0",5)),$$$cl2243.String("defaulted parameters class2 named 3",35));
    var i1$2513,i3$2514;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$2515=DefParamTest3();
    $$$c2244.check(tst$2515.f((1)).equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted method parameters 1",29));
    $$$c2244.check(tst$2515.f((1),(3)).equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted method parameters 2",29));
    $$$c2244.check(tst$2515.f((1),(3),(0)).equals($$$cl2243.String("1,3,0",5)),$$$cl2243.String("defaulted method parameters 3",29));
    $$$c2244.check((i1$2516=(1),(opt$2517=tst$2515,$$$cl2243.JsCallable(opt$2517,opt$2517!==null?opt$2517.f:null))(i1$2516,undefined,undefined)).equals($$$cl2243.String("1,2,3",5)),$$$cl2243.String("defaulted method parameters named 1",35));
    var i1$2516,opt$2517;
    $$$c2244.check((i1$2518=(1),i2$2519=(3),(opt$2520=tst$2515,$$$cl2243.JsCallable(opt$2520,opt$2520!==null?opt$2520.f:null))(i1$2518,i2$2519,undefined)).equals($$$cl2243.String("1,3,4",5)),$$$cl2243.String("defaulted method parameters named 2",35));
    var i1$2518,i2$2519,opt$2520;
    $$$c2244.check((i1$2521=(1),i3$2522=(0),(opt$2523=tst$2515,$$$cl2243.JsCallable(opt$2523,opt$2523!==null?opt$2523.f:null))(i1$2521,undefined,i3$2522)).equals($$$cl2243.String("1,2,0",5)),$$$cl2243.String("defaulted method parameters named 3",35));
    var i1$2521,i3$2522,opt$2523;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$2524($$getterTest$2524){
        $init$GetterTest$2524();
        if ($$getterTest$2524===undefined)$$getterTest$2524=new GetterTest$2524.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        $$getterTest$2524.i$2525=(0);
        return $$getterTest$2524;
    }
    function $init$GetterTest$2524(){
        if (GetterTest$2524.$$===undefined){
            $$$cl2243.initTypeProto(GetterTest$2524,'functions::testGetterMethodDefinitions.GetterTest',$$$cl2243.Basic);
            (function($$getterTest$2524){
                
                //AttributeDeclaration i at functions.ceylon (107:4-107:24)
                $$getterTest$2524.getI$2525=function getI$2525(){
                    return this.i$2525;
                };
                $$getterTest$2524.setI$2525=function setI$2525(i$2526){
                    return this.i$2525=i$2526;
                };
                
                //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
                $$getterTest$2524.getX=function getX(){
                    var $$getterTest$2524=this;
                    return ($$getterTest$2524.setI$2525($$getterTest$2524.getI$2525().getSuccessor()));
                };
            })(GetterTest$2524.$$.prototype);
        }
        return GetterTest$2524;
    }
    $init$GetterTest$2524();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$2527=GetterTest$2524();
    $$$c2244.check(gt$2527.getX().equals((1)),$$$cl2243.String("getter defined as method 1",26));
    $$$c2244.check(gt$2527.getX().equals((2)),$$$cl2243.String("getter defined as method 2",26));
    $$$c2244.check(gt$2527.getX().equals((3)),$$$cl2243.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$2528,y$2529,z$2530){
    if(x$2528===undefined){x$2528=$$$cl2243.String("x",1);}
    if(y$2529===undefined){y$2529=x$2528.plus($$$cl2243.String("y",1));}
    if(z$2530===undefined){z$2530=$$$cl2243.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$2531=x$2528.plus($$$cl2243.String(",",1)).plus(y$2529);
    var setResult$2531=function(result$2532){return result$2531=result$2532;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$2533 = z$2530.getIterator();
    var s$2534;while ((s$2534=it$2533.next())!==$$$cl2243.getFinished()){
        (result$2531=result$2531.plus($$$cl2243.String(",",1).plus(s$2534)));
    }
    return result$2531;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$2535, more$2536, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$2536===undefined){more$2536=$$$cl2243.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    $$issue105.i$2537=i$2535;
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl2243.initTypeProto(Issue105,'functions::Issue105',$$$cl2243.Basic);
        (function($$issue105){
            
            //AttributeDeclaration i at functions.ceylon (123:4-123:20)
            $$issue105.getI=function getI(){
                return this.i$2537;
            };
        })(Issue105.$$.prototype);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2244.check((namedArgFunc(undefined,undefined,$$$cl2243.getEmpty())).equals($$$cl2243.String("x,xy",4)),$$$cl2243.String("named arguments 1",17));
    $$$c2244.check((x$2538=$$$cl2243.String("a",1),namedArgFunc(x$2538,undefined,$$$cl2243.getEmpty())).equals($$$cl2243.String("a,ay",4)),$$$cl2243.String("named arguments 2",17));
    var x$2538;
    $$$c2244.check((y$2539=$$$cl2243.String("b",1),namedArgFunc(undefined,y$2539,$$$cl2243.getEmpty())).equals($$$cl2243.String("x,b",3)),$$$cl2243.String("named arguments 3",17));
    var y$2539;
    $$$c2244.check((z$2540=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),namedArgFunc(undefined,undefined,z$2540)).equals($$$cl2243.String("x,xy,c",6)),$$$cl2243.String("named arguments 4",17));
    var z$2540;
    $$$c2244.check((x$2541=$$$cl2243.String("a",1),y$2542=$$$cl2243.String("b",1),z$2543=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),namedArgFunc(x$2541,y$2542,z$2543)).equals($$$cl2243.String("a,b,c",5)),$$$cl2243.String("named arguments 5",17));
    var x$2541,y$2542,z$2543;
    $$$c2244.check((y$2544=$$$cl2243.String("b",1),x$2545=$$$cl2243.String("a",1),z$2546=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.Tuple($$$cl2243.String("d",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),namedArgFunc(x$2545,y$2544,z$2546)).equals($$$cl2243.String("a,b,c,d",7)),$$$cl2243.String("named arguments 6",17));
    var y$2544,x$2545,z$2546;
    $$$c2244.check((x$2547=$$$cl2243.String("a",1),z$2548=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),namedArgFunc(x$2547,undefined,z$2548)).equals($$$cl2243.String("a,ay,c",6)),$$$cl2243.String("named arguments 7",17));
    var x$2547,z$2548;
    $$$c2244.check((y$2549=$$$cl2243.String("b",1),z$2550=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),namedArgFunc(undefined,y$2549,z$2550)).equals($$$cl2243.String("x,b,c",5)),$$$cl2243.String("named arguments 8",17));
    var y$2549,z$2550;
    $$$c2244.check((y$2551=$$$cl2243.String("b",1),x$2552=$$$cl2243.String("a",1),namedArgFunc(x$2552,y$2551,$$$cl2243.getEmpty())).equals($$$cl2243.String("a,b",3)),$$$cl2243.String("named arguments 9",17));
    var y$2551,x$2552;
    $$$c2244.check((z$2553=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.Tuple($$$cl2243.String("d",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),namedArgFunc(undefined,undefined,z$2553)).equals($$$cl2243.String("x,xy,c,d",8)),$$$cl2243.String("named arguments 11",18));
    var z$2553;
    $$$c2244.check((y$2554=$$$cl2243.String("b",1),z$2555=$$$cl2243.Tuple($$$cl2243.String("c",1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),x$2556=$$$cl2243.String("a",1),namedArgFunc(x$2556,y$2554,z$2555)).equals($$$cl2243.String("a,b,c",5)),$$$cl2243.String("named arguments 12",18));
    var y$2554,z$2555,x$2556;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$2557=(i$2558=(1),more$2559=$$$cl2243.Tuple((i$2560=(2),Issue105(i$2560,$$$cl2243.getEmpty())),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$2558,more$2559));
    var i$2558,more$2559,i$2560;
    $$$c2244.check(issue105$2557.getI().equals((1)),$$$cl2243.String("issue #105",10));
};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl2243.initTypeProto(LazyExprBase,'functions::LazyExprBase');
        (function($$lazyExprBase){
        })(LazyExprBase.$$.prototype);
    }
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
    $$lazyExprTest.x$2561=(1000);
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl2243.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl2243.Basic,$init$LazyExprBase());
        (function($$lazyExprTest){
            
            //AttributeDeclaration x at functions.ceylon (149:4-149:36)
            $$lazyExprTest.getX=function getX(){
                return this.x$2561;
            };
            $$lazyExprTest.setX=function setX(x$2562){
                return this.x$2561=x$2562;
            };
            
            //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
            $$lazyExprTest.f1=function (i$2563,f$2564){
                var $$lazyExprTest=this;
                if(f$2564===undefined){f$2564=function (){
                    return $$$cl2243.StringBuilder().appendAll([i$2563.getString(),$$$cl2243.String(".",1),($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).getString()]).getString();
                };}
                return $$$cl2243.StringBuilder().appendAll([i$2563.getString(),$$$cl2243.String(":",1),f$2564().getString()]).getString();
            };
            
            //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
            $$lazyExprTest.f2=function (i$2565){
                var $$lazyExprTest=this;
                return (2).times(($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor()))).plus(i$2565);
            };
            
            //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
            $$lazyExprTest.getI1=function getI1(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor()));
            };
            
            //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
            $$lazyExprTest.getI2=function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).times((2));
            };
            $$lazyExprTest.getS1=function(){
                var $$lazyExprTest=this;
                return $$$cl2243.StringBuilder().appendAll([($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).getString(),$$$cl2243.String(".1",2)]).getString();
            };
            $$lazyExprTest.s2=function (i$2566){
                var $$lazyExprTest=this;
                return $$$cl2243.StringBuilder().appendAll([($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).getString(),$$$cl2243.String(".",1),i$2566.getString()]).getString();
            };
            //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
            $$lazyExprTest.f3=function (f$2567){
                var $$lazyExprTest=this;
                return f$2567(($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())));
            };
        })(LazyExprTest.$$.prototype);
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$2568=(1000);
var getLx=function(){return lx$2568;};
exports.getLx=getLx;
var setLx=function(lx$2569){return lx$2568=lx$2569;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$2570,f$2571){
    if(f$2571===undefined){f$2571=function (){
        return $$$cl2243.StringBuilder().appendAll([i$2570.getString(),$$$cl2243.String(".",1),(setLx(getLx().getSuccessor())).getString()]).getString();
    };}
    return f$2571();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$2572){
    return (2).times((setLx(getLx().getSuccessor()))).plus(i$2572);
};

//AttributeDeclaration lazy_i1 at functions.ceylon (164:0-164:23)
var getLazy_i1=function(){return (setLx(getLx().getSuccessor()));};
exports.getLazy_i1=getLazy_i1;

//ClassDefinition LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDeclaration x at functions.ceylon (167:4-167:35)
    $$lazyExprTest2.x$2573=(1000);
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl2243.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl2243.Basic,$init$LazyExprBase());
        (function($$lazyExprTest2){
            
            //AttributeDeclaration x at functions.ceylon (167:4-167:35)
            $$lazyExprTest2.getX=function getX(){
                return this.x$2573;
            };
            $$lazyExprTest2.setX=function setX(x$2574){
                return this.x$2573=x$2574;
            };
            
            //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
            $$lazyExprTest2.getS1=function getS1(){
                var $$lazyExprTest2=this;
                return ($$lazyExprTest2.setX($$lazyExprTest2.getX().getSuccessor())).getString();
            };
            
            //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
            $$lazyExprTest2.s2=function (i$2575){
                var $$lazyExprTest2=this;
                return $$$cl2243.StringBuilder().appendAll([($$lazyExprTest2.setX($$lazyExprTest2.getX().getSuccessor())).getString(),$$$cl2243.String("-",1),i$2575.getString()]).getString();
            };
        })(LazyExprTest2.$$.prototype);
    }
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
    $$lazyExprTest3.s1$2576=$$$cl2243.String("s1",2);
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl2243.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
        (function($$lazyExprTest3){
            
            //AttributeDeclaration s1 at functions.ceylon (172:4-172:43)
            $$lazyExprTest3.getS1=function getS1(){
                return this.s1$2576;
            };
            $$lazyExprTest3.setS1=function setS1(s1$2577){
                return this.s1$2576=s1$2577;
            };
        })(LazyExprTest3.$$.prototype);
    }
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
    $$lazyExprTest4.assigned$2578=$$$cl2243.String("",0);
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl2243.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
        (function($$lazyExprTest4){
            
            //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
            $$lazyExprTest4.getAssigned=function getAssigned(){
                return this.assigned$2578;
            };
            $$lazyExprTest4.setAssigned=function setAssigned(assigned$2579){
                return this.assigned$2578=assigned$2579;
            };
            
            //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
            $$lazyExprTest4.getS1=function getS1(){
                var $$lazyExprTest4=this;
                return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("s1-",3),$$lazyExprTest4.getT$all()['functions::LazyExprTest2'].$$.prototype.getS1.call(this).getString()]).getString();
            };
            //AttributeSetterDefinition s1 at functions.ceylon (177:4-177:31)
            $$lazyExprTest4.setS1=function setS1(s1$2580){
                var $$lazyExprTest4=this;
                $$lazyExprTest4.setAssigned(s1$2580);
            };
        })(LazyExprTest4.$$.prototype);
    }
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDefinition testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDeclaration tst at functions.ceylon (181:4-181:30)
    var tst$2581=LazyExprTest();
    (tst$2581.setX((1)));
    $$$c2244.check(tst$2581.f1((3)).equals($$$cl2243.String("3:3.2",5)),$$$cl2243.String("=> defaulted param",18));
    $$$c2244.check(tst$2581.f2((3)).equals((9)),$$$cl2243.String("=> method",9));
    $$$c2244.check(tst$2581.getI1().equals((4)),$$$cl2243.String("=> attribute",12));
    $$$c2244.check(tst$2581.getI2().equals((10)),$$$cl2243.String("=> attribute specifier",22));
    $$$c2244.check(tst$2581.getS1().equals($$$cl2243.String("6.1",3)),$$$cl2243.String("=> attribute refinement",23));
    $$$c2244.check(tst$2581.s2((5)).equals($$$cl2243.String("7.5",3)),$$$cl2243.String("=> method refinement",20));
    setLx((1));
    $$$c2244.check(lazy_f1((3)).equals($$$cl2243.String("3.2",3)),$$$cl2243.String("=> defaulted param toplevel",27));
    $$$c2244.check(lazy_f2((3)).equals((9)),$$$cl2243.String("=> method toplevel",18));
    $$$c2244.check(getLazy_i1().equals((4)),$$$cl2243.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$2582=(1000);
    var setX$2582=function(x$2583){return x$2582=x$2583;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$2584=function (i$2585,f$2586){
        if(f$2586===undefined){f$2586=function (){
            return $$$cl2243.StringBuilder().appendAll([i$2585.getString(),$$$cl2243.String(".",1),(x$2582=x$2582.getSuccessor()).getString()]).getString();
        };}
        return f$2586();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$2587=function (i$2588){
        return (2).times((x$2582=x$2582.getSuccessor())).plus(i$2588);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$2589=function(){return (x$2582=x$2582.getSuccessor());};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$2590;
    var getI2$2590=function(){
        return (x$2582=x$2582.getSuccessor()).times((2));
    };
    x$2582=(1);
    $$$c2244.check(f1$2584((3)).equals($$$cl2243.String("3.2",3)),$$$cl2243.String("=> defaulted param local",24));
    $$$c2244.check(f2$2587((3)).equals((9)),$$$cl2243.String("=> method local",15));
    $$$c2244.check(getI1$2589().equals((4)),$$$cl2243.String("=> attribute local",18));
    $$$c2244.check(getI2$2590().equals((10)),$$$cl2243.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$2591=LazyExprTest3();
    (tst3$2591.setX((1)));
    $$$c2244.check(tst3$2591.getS1().equals($$$cl2243.String("s1",2)),$$$cl2243.String("=> override variable 1",22));
    (tst3$2591.setS1($$$cl2243.String("abc",3)));
    $$$c2244.check(tst3$2591.getS1().equals($$$cl2243.String("abc",3)),$$$cl2243.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$2592=LazyExprTest4();
    (tst4$2592.setX((1)));
    $$$c2244.check(tst4$2592.getS1().equals($$$cl2243.String("s1-2",4)),$$$cl2243.String("=> override getter/setter 1",27));
    (tmp$2593=tst4$2592,tmp$2593.setS1($$$cl2243.String("abc",3)),tmp$2593.getS1());
    var tmp$2593;
    $$$c2244.check(tst4$2592.getS1().equals($$$cl2243.String("s1-4",4)),$$$cl2243.String("=> override getter/setter 2",27));
    $$$c2244.check(tst4$2592.getAssigned().equals($$$cl2243.String("abc",3)),$$$cl2243.String("=> override getter/setter 3",27));
    (tst$2581.setX((1)));
    x$2582=(10);
    $$$c2244.check((i$2594=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$2582=x$2582.getSuccessor());
    }()),(opt$2595=tst$2581,$$$cl2243.JsCallable(opt$2595,opt$2595!==null?opt$2595.f1:null))(i$2594,undefined)).equals($$$cl2243.String("11:11.2",7)),$$$cl2243.String("=> named arg",12));
    var i$2594,opt$2595;
    $$$c2244.check((i$2596=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$2582=x$2582.getSuccessor());
    }()),f$2597=function (){return (x$2582=x$2582.getSuccessor()).getString();},(opt$2598=tst$2581,$$$cl2243.JsCallable(opt$2598,opt$2598!==null?opt$2598.f1:null))(i$2596,f$2597)).equals($$$cl2243.String("12:13",5)),$$$cl2243.String("=> named arg function",21));
    var i$2596,f$2597,opt$2598;
    $$$c2244.check((f$2599=function (i$2600){return $$$cl2243.StringBuilder().appendAll([i$2600.getString(),$$$cl2243.String("-",1),(x$2582=x$2582.getSuccessor()).getString()]).getString();},(opt$2601=tst$2581,$$$cl2243.JsCallable(opt$2601,opt$2601!==null?opt$2601.f3:null))(f$2599)).equals($$$cl2243.String("3-14",4)),$$$cl2243.String("=> named arg function with param",32));
    var f$2599,opt$2601;
};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl2243.String("test",4));
    helloAll([$$$cl2243.String("Gavin",5),$$$cl2243.String("Enrique",7),$$$cl2243.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}));
    $$$c2244.check(toString((5)).equals($$$cl2243.String("5",1)),$$$cl2243.String("toString(obj)",13));
    $$$c2244.check(add($$$cl2243.Float(1.5),$$$cl2243.Float(2.5)).equals($$$cl2243.Float(4.0)),$$$cl2243.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    $$$c2244.results();
}
exports.test=test;

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$2602,y$2603){
        return x$2602.compare(y$2603);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$2604){
    return function(apat$2605){
        return function(amat$2606){
            return $$$cl2243.StringBuilder().appendAll([nombre$2604.getString(),$$$cl2243.String(" ",1),apat$2605.getString(),$$$cl2243.String(" ",1),amat$2606.getString()]).getString();
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$2607){
    if(name$2607===undefined){name$2607=$$$cl2243.String("A",1);}
    return function(apat$2608){
        return function(amat$2609){
            return $$$cl2243.StringBuilder().appendAll([name$2607.getString(),$$$cl2243.String(" ",1),apat$2608.getString(),$$$cl2243.String(" ",1),amat$2609.getString()]).getString();
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$2610){
    if(names$2610===undefined){names$2610=$$$cl2243.getEmpty();}
    return function(count$2611){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$2612=$$$cl2243.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$2613 = names$2610.getIterator();
        var name$2614;while ((name$2614=it$2613.next())!==$$$cl2243.getFinished()){
            sb$2612.append(name$2614).append($$$cl2243.String(" ",1));
        }
        sb$2612.append($$$cl2243.String("count ",6)).append(count$2611.getString());
        return sb$2612.getString();
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl2243.print($$$cl2243.String("Testing multiple parameter lists...",35));
    $$$c2244.check(multiCompare()((1),(1)).equals($$$cl2243.getEqual()),$$$cl2243.String("Multi compare 1",15));
    $$$c2244.check(multiCompare()((1),(2)).equals($$$cl2243.getSmaller()),$$$cl2243.String("Multi compare 2",15));
    $$$c2244.check(multiCompare()((2),(1)).equals($$$cl2243.getLarger()),$$$cl2243.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$2615=function (a$2616,b$2617){
        return multiCompare()(a$2616,b$2617);
    };
    $$$c2244.check(comp$2615((1),(1)).equals($$$cl2243.getEqual()),$$$cl2243.String("Multi compare 4",15));
    $$$c2244.check(comp$2615((1),(2)).equals($$$cl2243.getSmaller()),$$$cl2243.String("Multi compare 5",15));
    $$$c2244.check(comp$2615((2),(1)).equals($$$cl2243.getLarger()),$$$cl2243.String("Multi compare 6",15));
    $$$c2244.check(multiFullname($$$cl2243.String("a",1))($$$cl2243.String("b",1))($$$cl2243.String("c",1)).equals($$$cl2243.String("a b c",5)),$$$cl2243.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$2618=function (c$2619){
        return multiFullname($$$cl2243.String("A",1))($$$cl2243.String("B",1))(c$2619);
    };
    $$$c2244.check(apat$2618($$$cl2243.String("C",1)).equals($$$cl2243.String("A B C",5)),$$$cl2243.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$2620=function (name$2621){
        return multiFullname($$$cl2243.String("Name",4))(name$2621);
    };
    $$$c2244.check(nombre$2620($$$cl2243.String("Z",1))($$$cl2243.String("L",1)).equals($$$cl2243.String("Name Z L",8)),$$$cl2243.String("Multi callable 2",16));
    $$$c2244.check(multiDefaulted()($$$cl2243.String("B",1))($$$cl2243.String("C",1)).equals($$$cl2243.String("A B C",5)),$$$cl2243.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$2622=multiDefaulted();
    $$$c2244.check(md1$2622($$$cl2243.String("B",1))($$$cl2243.String("C",1)).equals($$$cl2243.String("A B C",5)),$$$cl2243.String("Multi defaulted 2",17));
    $$$c2244.check(md1$2622($$$cl2243.String("B",1))($$$cl2243.String("Z",1)).equals($$$cl2243.String("A B Z",5)),$$$cl2243.String("Multi defaulted 3",17));
    $$$c2244.check(md1$2622($$$cl2243.String("Z",1))($$$cl2243.String("C",1)).equals($$$cl2243.String("A Z C",5)),$$$cl2243.String("Multi defaulted 4",17));
    $$$c2244.check(md1$2622($$$cl2243.String("Y",1))($$$cl2243.String("Z",1)).equals($$$cl2243.String("A Y Z",5)),$$$cl2243.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$2623=function (x$2624){
        return multiDefaulted()($$$cl2243.String("B",1))(x$2624);
    };
    $$$c2244.check(md2$2623($$$cl2243.String("C",1)).equals($$$cl2243.String("A B C",5)),$$$cl2243.String("Multi defaulted 6",17));
    $$$c2244.check(md2$2623($$$cl2243.String("Z",1)).equals($$$cl2243.String("A B Z",5)),$$$cl2243.String("Multi defaulted 7",17));
    $$$c2244.check(multiSequenced([$$$cl2243.String("A",1),$$$cl2243.String("B",1),$$$cl2243.String("C",1)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}))((1)).equals($$$cl2243.String("A B C count 1",13)),$$$cl2243.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$2625=function (c$2626){
        return multiSequenced([$$$cl2243.String("x",1),$$$cl2243.String("y",1),$$$cl2243.String("z",1)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}))(c$2626);
    };
    $$$c2244.check(ms1$2625((5)).equals($$$cl2243.String("x y z count 5",13)),$$$cl2243.String("Multi sequenced 2",17));
    $$$c2244.check(ms1$2625((10)).equals($$$cl2243.String("x y z count 10",14)),$$$cl2243.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
