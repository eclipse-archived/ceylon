(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl2328=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2329=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$2647,f$2648,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$2649 = a$2647.iterator;
    var e$2650;while ((e$2650=it$2649.next())!==$$$cl2328.getFinished()){
        if(f$2648(e$2650)){
            return e$2650;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$2651,f$2652,$$$mptypes){
    if(f$2652===undefined){f$2652=function (x$2653){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$2654 = a$2651.iterator;
    var e$2655;while ((e$2655=it$2654.next())!==$$$cl2328.getFinished()){
        if(f$2652(e$2655)){
            return e$2655;
        }
    }
    if ($$$cl2328.getFinished() === e$2655){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$2656){
    return function (i$2657){
        return i$2657.minus(howMuch$2656).string;
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl2328.print($$$cl2328.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$2658=(elements$2659=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.Integer}}),$$$cl2328.array(elements$2659,{Element:{t:$$$cl2328.Integer}}));
    var elements$2659;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$2660=find(nums$2658,function (i$2661){
        return i$2661.remainder((2)).equals((0));
    },{Element:{t:$$$cl2328.Integer}});
    var setFound$2660=function(found$2662){return found$2660=found$2662;};
    var i$2663;
    if((i$2663=found$2660)!==null){
        $$$c2329.check(i$2663.equals((2)),$$$cl2328.String("anonfunc positional",19));
    }else {
        $$$c2329.fail($$$cl2328.String("anonfunc positional",19));
    }
    found$2660=(f$2664=function (i$2665){
        return i$2665.remainder((2)).equals((0));
    },a$2666=nums$2658,find(a$2666,f$2664,{Element:{t:$$$cl2328.Integer}}));
    var f$2664,a$2666;
    var i$2667;
    if((i$2667=found$2660)!==null){
        $$$c2329.check(i$2667.equals((2)),$$$cl2328.String("anonfunc named",14));
    }else {
        $$$c2329.fail($$$cl2328.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$2668(f$2669,expect$2670){
        $$$c2329.check(f$2669((0)).equals(expect$2670),$$$cl2328.StringBuilder().appendAll([$$$cl2328.String("anon func returns ",18),f$2669((0)).string,$$$cl2328.String(" instead of ",12),expect$2670.string]).string);
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$2671(i$2672){
        return i$2672.plus((12)).string;
    };
    callFunction$2668(f$2671,$$$cl2328.String("12",2));
    callFunction$2668(function (i$2673){
        return i$2673.times((3)).string;
    },$$$cl2328.String("0",1));
    (expect$2674=$$$cl2328.String("0",1),f$2675=function (i$2676){
        return i$2676.power((2)).string;
    },callFunction$2668(f$2675,expect$2674));
    var expect$2674,f$2675;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$2677=function (i$2678){
        return i$2678.minus((10)).string;
    };
    callFunction$2668(f2$2677,$$$cl2328.String("-10",3));
    callFunction$2668(subtract((5)),$$$cl2328.String("-5",2));
    found$2660=find2(nums$2658,function (i$2679){
        return i$2679.compare((2)).equals($$$cl2328.getLarger());
    },{Element:{t:$$$cl2328.Integer}});
    var i$2680;
    if((i$2680=found$2660)!==null){
        $$$c2329.check(i$2680.equals((3)),$$$cl2328.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2329.fail($$$cl2328.String("anonfunc i>2 [2]",16));
    }
    found$2660=find2(nums$2658,undefined,{Element:{t:$$$cl2328.Integer}});
    var i$2681;
    if((i$2681=found$2660)!==null){
        $$$c2329.check(i$2681.equals((1)),$$$cl2328.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2329.fail($$$cl2328.String("anonfunc defaulted param [2]",28));
    }
};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl2328.print($$$cl2328.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$2682){
    $$$cl2328.print($$$cl2328.String("hello",5).plus(name$2682));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$2683){
    if(names$2683===undefined){names$2683=$$$cl2328.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$2684){
    return obj$2684.string;
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$2685,y$2686){
    return x$2685.plus(y$2686);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$2687,f$2688){
    f$2688((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$2689, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl2328.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$2689=seq$2689;
    $$$cl2328.Sequence($$mySequence);
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    
    //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl2328.initTypeProto(MySequence,'functions::MySequence',$$$cl2328.Basic,$$$cl2328.Sequence);
        (function($$mySequence){
            
            //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
            $$$cl2328.defineAttr($$mySequence,'lastIndex',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.lastIndex;
            });
            //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
            $$$cl2328.defineAttr($$mySequence,'first',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.first;
            });
            //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
            $$$cl2328.defineAttr($$mySequence,'rest',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.rest;
            });
            //MethodDefinition get at functions.ceylon (30:4-30:67)
            $$mySequence.get=function get(index$2690){
                var $$mySequence=this;
                return $$mySequence.seq$2689.get(index$2690);
            };
            //MethodDefinition span at functions.ceylon (31:4-31:88)
            $$mySequence.span=function span(from$2691,to$2692){
                var $$mySequence=this;
                return $$mySequence.seq$2689.span(from$2691,to$2692);
            };
            //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
            $$mySequence.spanFrom=function spanFrom(from$2693){
                var $$mySequence=this;
                return $$mySequence.seq$2689.spanFrom(from$2693);
            };
            //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
            $$mySequence.spanTo=function spanTo(to$2694){
                var $$mySequence=this;
                return $$mySequence.seq$2689.spanTo(to$2694);
            };
            //MethodDefinition segment at functions.ceylon (34:4-34:102)
            $$mySequence.segment=function segment(from$2695,length$2696){
                var $$mySequence=this;
                return $$mySequence.seq$2689.segment(from$2695,length$2696);
            };
            //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
            $$$cl2328.defineAttr($$mySequence,'clone',function(){
                var $$mySequence=this;
                return $$mySequence;
            });
            //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
            $$$cl2328.defineAttr($$mySequence,'string',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.string;
            });
            //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
            $$$cl2328.defineAttr($$mySequence,'hash',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.hash;
            });
            //MethodDefinition equals at functions.ceylon (38:4-38:75)
            $$mySequence.equals=function equals(other$2697){
                var $$mySequence=this;
                return $$mySequence.seq$2689.equals(other$2697);
            };
            //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
            $$$cl2328.defineAttr($$mySequence,'reversed',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.reversed;
            });
            //AttributeDeclaration last at functions.ceylon (40:4-40:42)
            $$$cl2328.defineAttr($$mySequence,'last',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.last;
            });
            
            //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
            $$$cl2328.defineAttr($$mySequence,'iterator',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.iterator;
            });
            
            //AttributeDeclaration size at functions.ceylon (42:4-42:42)
            $$$cl2328.defineAttr($$mySequence,'size',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2689.size;
            });
            
            //MethodDeclaration contains at functions.ceylon (43:4-43:71)
            $$mySequence.contains=function (other$2698){
                var $$mySequence=this;
                return $$mySequence.seq$2689.contains(other$2698);
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
        $$$cl2328.initTypeProto(RefHelper,'functions::RefHelper',$$$cl2328.Basic);
        (function($$refHelper){
            
            //MethodDefinition f at functions.ceylon (47:4-47:47)
            $$refHelper.f=function f(i$2699){
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
    var obj1$2700=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$2701=MySequence($$$cl2328.Tuple($$$cl2328.String("hi",2),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),{Element:{t:$$$cl2328.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$2702(x$2703){
        return x$2703((0));
    };
    $$$c2329.check(tst$2702((opt$2704=obj1$2700,$$$cl2328.JsCallable(opt$2704,opt$2704!==null?opt$2704.f:null))),$$$cl2328.String("Reference to method",19));
    var opt$2704;
    $$$c2329.check(tst$2702((opt$2705=obj2$2701,$$$cl2328.JsCallable(opt$2705,opt$2705!==null?opt$2705.defines:null))),$$$cl2328.String("Reference to method from ceylon.language",40));
    var opt$2705;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$2706,i2$2707,i3$2708){
    if(i2$2707===undefined){i2$2707=i1$2706.plus((1));}
    if(i3$2708===undefined){i3$2708=i1$2706.plus(i2$2707);}
    return $$$cl2328.StringBuilder().appendAll([i1$2706.string,$$$cl2328.String(",",1),i2$2707.string,$$$cl2328.String(",",1),i3$2708.string]).string;
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$2709, i2$2710, i3$2711, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$2710===undefined){i2$2710=i1$2709.plus((1));}
    if(i3$2711===undefined){i3$2711=i1$2709.plus(i2$2710);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    $$defParamTest1.s$2712_=$$$cl2328.StringBuilder().appendAll([i1$2709.string,$$$cl2328.String(",",1),i2$2710.string,$$$cl2328.String(",",1),i3$2711.string]).string;
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl2328.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl2328.Basic);
        (function($$defParamTest1){
            
            //AttributeDeclaration s at functions.ceylon (64:4-64:44)
            $$$cl2328.defineAttr($$defParamTest1,'s',function(){return this.s$2712_;});
        })(DefParamTest1.$$.prototype);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$2713, i2$2714, i3$2715, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$2713=i1$2713;
    if(i2$2714===undefined){i2$2714=$$defParamTest2.i1$2713.plus((1));}
    $$defParamTest2.i2$2714=i2$2714;
    if(i3$2715===undefined){i3$2715=$$defParamTest2.i1$2713.plus($$defParamTest2.i2$2714);}
    $$defParamTest2.i3$2715=i3$2715;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl2328.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl2328.Basic);
        (function($$defParamTest2){
            
            //MethodDefinition f at functions.ceylon (67:4-67:55)
            $$defParamTest2.f=function f(){
                var $$defParamTest2=this;
                return $$$cl2328.StringBuilder().appendAll([$$defParamTest2.i1$2713.string,$$$cl2328.String(",",1),$$defParamTest2.i2$2714.string,$$$cl2328.String(",",1),$$defParamTest2.i3$2715.string]).string;
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
        $$$cl2328.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl2328.Basic);
        (function($$defParamTest3){
            
            //MethodDefinition f at functions.ceylon (70:4-72:4)
            $$defParamTest3.f=function f(i1$2716,i2$2717,i3$2718){
                var $$defParamTest3=this;
                if(i2$2717===undefined){i2$2717=i1$2716.plus((1));}
                if(i3$2718===undefined){i3$2718=i1$2716.plus(i2$2717);}
                return $$$cl2328.StringBuilder().appendAll([i1$2716.string,$$$cl2328.String(",",1),i2$2717.string,$$$cl2328.String(",",1),i3$2718.string]).string;
            };
        })(DefParamTest3.$$.prototype);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2329.check(defParamTest((1)).equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted parameters 1",22));
    $$$c2329.check(defParamTest((1),(3)).equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted parameters 2",22));
    $$$c2329.check(defParamTest((1),(3),(0)).equals($$$cl2328.String("1,3,0",5)),$$$cl2328.String("defaulted parameters 3",22));
    $$$c2329.check((i1$2719=(1),defParamTest(i1$2719,undefined,undefined)).equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted parameters named 1",28));
    var i1$2719;
    $$$c2329.check((i1$2720=(1),i2$2721=(3),defParamTest(i1$2720,i2$2721,undefined)).equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted parameters named 2",28));
    var i1$2720,i2$2721;
    $$$c2329.check((i1$2722=(1),i3$2723=(0),defParamTest(i1$2722,undefined,i3$2723)).equals($$$cl2328.String("1,2,0",5)),$$$cl2328.String("defaulted parameters named 3",28));
    var i1$2722,i3$2723;
    $$$c2329.check(DefParamTest1((1)).s.equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted parameters class 1",28));
    $$$c2329.check(DefParamTest1((1),(3)).s.equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted parameters class 2",28));
    $$$c2329.check(DefParamTest1((1),(3),(0)).s.equals($$$cl2328.String("1,3,0",5)),$$$cl2328.String("defaulted parameters class 3",28));
    $$$c2329.check((i1$2724=(1),DefParamTest1(i1$2724,undefined,undefined)).s.equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted parameters class named 1",34));
    var i1$2724;
    $$$c2329.check((i1$2725=(1),i2$2726=(3),DefParamTest1(i1$2725,i2$2726,undefined)).s.equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted parameters class named 2",34));
    var i1$2725,i2$2726;
    $$$c2329.check((i1$2727=(1),i3$2728=(0),DefParamTest1(i1$2727,undefined,i3$2728)).s.equals($$$cl2328.String("1,2,0",5)),$$$cl2328.String("defaulted parameters class named 3",34));
    var i1$2727,i3$2728;
    $$$c2329.check(DefParamTest2((1)).f().equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted parameters class2 1",29));
    $$$c2329.check(DefParamTest2((1),(3)).f().equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted parameters class2 2",29));
    $$$c2329.check(DefParamTest2((1),(3),(0)).f().equals($$$cl2328.String("1,3,0",5)),$$$cl2328.String("defaulted parameters class2 3",29));
    $$$c2329.check((i1$2729=(1),DefParamTest2(i1$2729,undefined,undefined)).f().equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted parameters class2 named 1",35));
    var i1$2729;
    $$$c2329.check((i1$2730=(1),i2$2731=(3),DefParamTest2(i1$2730,i2$2731,undefined)).f().equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted parameters class2 named 2",35));
    var i1$2730,i2$2731;
    $$$c2329.check((i1$2732=(1),i3$2733=(0),DefParamTest2(i1$2732,undefined,i3$2733)).f().equals($$$cl2328.String("1,2,0",5)),$$$cl2328.String("defaulted parameters class2 named 3",35));
    var i1$2732,i3$2733;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$2734=DefParamTest3();
    $$$c2329.check(tst$2734.f((1)).equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted method parameters 1",29));
    $$$c2329.check(tst$2734.f((1),(3)).equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted method parameters 2",29));
    $$$c2329.check(tst$2734.f((1),(3),(0)).equals($$$cl2328.String("1,3,0",5)),$$$cl2328.String("defaulted method parameters 3",29));
    $$$c2329.check((i1$2735=(1),(opt$2736=tst$2734,$$$cl2328.JsCallable(opt$2736,opt$2736!==null?opt$2736.f:null))(i1$2735,undefined,undefined)).equals($$$cl2328.String("1,2,3",5)),$$$cl2328.String("defaulted method parameters named 1",35));
    var i1$2735,opt$2736;
    $$$c2329.check((i1$2737=(1),i2$2738=(3),(opt$2739=tst$2734,$$$cl2328.JsCallable(opt$2739,opt$2739!==null?opt$2739.f:null))(i1$2737,i2$2738,undefined)).equals($$$cl2328.String("1,3,4",5)),$$$cl2328.String("defaulted method parameters named 2",35));
    var i1$2737,i2$2738,opt$2739;
    $$$c2329.check((i1$2740=(1),i3$2741=(0),(opt$2742=tst$2734,$$$cl2328.JsCallable(opt$2742,opt$2742!==null?opt$2742.f:null))(i1$2740,undefined,i3$2741)).equals($$$cl2328.String("1,2,0",5)),$$$cl2328.String("defaulted method parameters named 3",35));
    var i1$2740,i3$2741,opt$2742;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$2743($$getterTest$2743){
        $init$GetterTest$2743();
        if ($$getterTest$2743===undefined)$$getterTest$2743=new GetterTest$2743.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        $$getterTest$2743.i$2744_=(0);
        return $$getterTest$2743;
    }
    function $init$GetterTest$2743(){
        if (GetterTest$2743.$$===undefined){
            $$$cl2328.initTypeProto(GetterTest$2743,'functions::testGetterMethodDefinitions.GetterTest',$$$cl2328.Basic);
            (function($$getterTest$2743){
                
                //AttributeDeclaration i at functions.ceylon (107:4-107:24)
                $$$cl2328.defineAttr($$getterTest$2743,'i$2744',function(){return this.i$2744_;},function(i$2745){return this.i$2744_=i$2745;});
                
                //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
                $$$cl2328.defineAttr($$getterTest$2743,'x',function(){
                    var $$getterTest$2743=this;
                    return ($$getterTest$2743.i$2744=$$getterTest$2743.i$2744.successor);
                });
            })(GetterTest$2743.$$.prototype);
        }
        return GetterTest$2743;
    }
    $init$GetterTest$2743();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$2746=GetterTest$2743();
    $$$c2329.check(gt$2746.x.equals((1)),$$$cl2328.String("getter defined as method 1",26));
    $$$c2329.check(gt$2746.x.equals((2)),$$$cl2328.String("getter defined as method 2",26));
    $$$c2329.check(gt$2746.x.equals((3)),$$$cl2328.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$2747,y$2748,z$2749){
    if(x$2747===undefined){x$2747=$$$cl2328.String("x",1);}
    if(y$2748===undefined){y$2748=x$2747.plus($$$cl2328.String("y",1));}
    if(z$2749===undefined){z$2749=$$$cl2328.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$2750=x$2747.plus($$$cl2328.String(",",1)).plus(y$2748);
    var setResult$2750=function(result$2751){return result$2750=result$2751;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$2752 = z$2749.iterator;
    var s$2753;while ((s$2753=it$2752.next())!==$$$cl2328.getFinished()){
        (result$2750=result$2750.plus($$$cl2328.String(",",1).plus(s$2753)));
    }
    return result$2750;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$2754, more$2755, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$2755===undefined){more$2755=$$$cl2328.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    $$issue105.i$2756_=i$2754;
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl2328.initTypeProto(Issue105,'functions::Issue105',$$$cl2328.Basic);
        (function($$issue105){
            
            //AttributeDeclaration i at functions.ceylon (123:4-123:20)
            $$$cl2328.defineAttr($$issue105,'i',function(){return this.i$2756_;});
        })(Issue105.$$.prototype);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2329.check((namedArgFunc(undefined,undefined,$$$cl2328.getEmpty())).equals($$$cl2328.String("x,xy",4)),$$$cl2328.String("named arguments 1",17));
    $$$c2329.check((x$2757=$$$cl2328.String("a",1),namedArgFunc(x$2757,undefined,$$$cl2328.getEmpty())).equals($$$cl2328.String("a,ay",4)),$$$cl2328.String("named arguments 2",17));
    var x$2757;
    $$$c2329.check((y$2758=$$$cl2328.String("b",1),namedArgFunc(undefined,y$2758,$$$cl2328.getEmpty())).equals($$$cl2328.String("x,b",3)),$$$cl2328.String("named arguments 3",17));
    var y$2758;
    $$$c2329.check((z$2759=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),namedArgFunc(undefined,undefined,z$2759)).equals($$$cl2328.String("x,xy,c",6)),$$$cl2328.String("named arguments 4",17));
    var z$2759;
    $$$c2329.check((x$2760=$$$cl2328.String("a",1),y$2761=$$$cl2328.String("b",1),z$2762=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),namedArgFunc(x$2760,y$2761,z$2762)).equals($$$cl2328.String("a,b,c",5)),$$$cl2328.String("named arguments 5",17));
    var x$2760,y$2761,z$2762;
    $$$c2329.check((y$2763=$$$cl2328.String("b",1),x$2764=$$$cl2328.String("a",1),z$2765=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.Tuple($$$cl2328.String("d",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),namedArgFunc(x$2764,y$2763,z$2765)).equals($$$cl2328.String("a,b,c,d",7)),$$$cl2328.String("named arguments 6",17));
    var y$2763,x$2764,z$2765;
    $$$c2329.check((x$2766=$$$cl2328.String("a",1),z$2767=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),namedArgFunc(x$2766,undefined,z$2767)).equals($$$cl2328.String("a,ay,c",6)),$$$cl2328.String("named arguments 7",17));
    var x$2766,z$2767;
    $$$c2329.check((y$2768=$$$cl2328.String("b",1),z$2769=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),namedArgFunc(undefined,y$2768,z$2769)).equals($$$cl2328.String("x,b,c",5)),$$$cl2328.String("named arguments 8",17));
    var y$2768,z$2769;
    $$$c2329.check((y$2770=$$$cl2328.String("b",1),x$2771=$$$cl2328.String("a",1),namedArgFunc(x$2771,y$2770,$$$cl2328.getEmpty())).equals($$$cl2328.String("a,b",3)),$$$cl2328.String("named arguments 9",17));
    var y$2770,x$2771;
    $$$c2329.check((z$2772=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.Tuple($$$cl2328.String("d",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),{Rest:{t:$$$cl2328.Tuple,a:{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),namedArgFunc(undefined,undefined,z$2772)).equals($$$cl2328.String("x,xy,c,d",8)),$$$cl2328.String("named arguments 11",18));
    var z$2772;
    $$$c2329.check((y$2773=$$$cl2328.String("b",1),z$2774=$$$cl2328.Tuple($$$cl2328.String("c",1),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:$$$cl2328.String},Element:{t:$$$cl2328.String}}),x$2775=$$$cl2328.String("a",1),namedArgFunc(x$2775,y$2773,z$2774)).equals($$$cl2328.String("a,b,c",5)),$$$cl2328.String("named arguments 12",18));
    var y$2773,z$2774,x$2775;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$2776=(i$2777=(1),more$2778=$$$cl2328.Tuple((i$2779=(2),Issue105(i$2779,$$$cl2328.getEmpty())),$$$cl2328.getEmpty(),{Rest:{t:$$$cl2328.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$2777,more$2778));
    var i$2777,more$2778,i$2779;
    $$$c2329.check(issue105$2776.i.equals((1)),$$$cl2328.String("issue #105",10));
};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl2328.initTypeProto(LazyExprBase,'functions::LazyExprBase');
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
    $$lazyExprTest.x$2780_=(1000);
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl2328.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl2328.Basic,$init$LazyExprBase());
        (function($$lazyExprTest){
            
            //AttributeDeclaration x at functions.ceylon (149:4-149:36)
            $$$cl2328.defineAttr($$lazyExprTest,'x',function(){return this.x$2780_;},function(x$2781){return this.x$2780_=x$2781;});
            
            //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
            $$lazyExprTest.f1=function (i$2782,f$2783){
                var $$lazyExprTest=this;
                if(f$2783===undefined){f$2783=function (){
                    return $$$cl2328.StringBuilder().appendAll([i$2782.string,$$$cl2328.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor).string]).string;
                };}
                return $$$cl2328.StringBuilder().appendAll([i$2782.string,$$$cl2328.String(":",1),f$2783().string]).string;
            };
            
            //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
            $$lazyExprTest.f2=function (i$2784){
                var $$lazyExprTest=this;
                return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$2784);
            };
            
            //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
            $$$cl2328.defineAttr($$lazyExprTest,'i1',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor);
            });
            
            //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
            $$$cl2328.defineAttr($$lazyExprTest,'i2',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor).times((2));
            });
            $$$cl2328.defineAttr($$lazyExprTest,'s1',function(){
                var $$lazyExprTest=this;
                return $$$cl2328.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl2328.String(".1",2)]).string;
            });
            $$lazyExprTest.s2=function (i$2785){
                var $$lazyExprTest=this;
                return $$$cl2328.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl2328.String(".",1),i$2785.string]).string;
            };
            //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
            $$lazyExprTest.f3=function (f$2786){
                var $$lazyExprTest=this;
                return f$2786(($$lazyExprTest.x=$$lazyExprTest.x.successor));
            };
        })(LazyExprTest.$$.prototype);
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$2787=(1000);
var getLx=function(){return lx$2787;};
exports.getLx=getLx;
var setLx=function(lx$2788){return lx$2787=lx$2788;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$2789,f$2790){
    if(f$2790===undefined){f$2790=function (){
        return $$$cl2328.StringBuilder().appendAll([i$2789.string,$$$cl2328.String(".",1),(setLx(getLx().successor)).string]).string;
    };}
    return f$2790();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$2791){
    return (2).times((setLx(getLx().successor))).plus(i$2791);
};

//AttributeDeclaration lazy_i1 at functions.ceylon (164:0-164:23)
var getLazy_i1=function(){return (setLx(getLx().successor));};
exports.getLazy_i1=getLazy_i1;

//ClassDefinition LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDeclaration x at functions.ceylon (167:4-167:35)
    $$lazyExprTest2.x$2792_=(1000);
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl2328.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl2328.Basic,$init$LazyExprBase());
        (function($$lazyExprTest2){
            
            //AttributeDeclaration x at functions.ceylon (167:4-167:35)
            $$$cl2328.defineAttr($$lazyExprTest2,'x',function(){return this.x$2792_;},function(x$2793){return this.x$2792_=x$2793;});
            
            //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
            $$$cl2328.defineAttr($$lazyExprTest2,'s1',function(){
                var $$lazyExprTest2=this;
                return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;
            });
            
            //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
            $$lazyExprTest2.s2=function (i$2794){
                var $$lazyExprTest2=this;
                return $$$cl2328.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl2328.String("-",1),i$2794.string]).string;
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
    $$lazyExprTest3.s1$2795_=$$$cl2328.String("s1",2);
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl2328.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
        (function($$lazyExprTest3){
            
            //AttributeDeclaration s1 at functions.ceylon (172:4-172:43)
            $$$cl2328.defineAttr($$lazyExprTest3,'s1',function(){return this.s1$2795_;},function(s1$2796){return this.s1$2795_=s1$2796;});
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
    $$lazyExprTest4.assigned$2797_=$$$cl2328.String("",0);
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl2328.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
        (function($$lazyExprTest4){
            
            //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
            $$$cl2328.defineAttr($$lazyExprTest4,'assigned',function(){return this.assigned$2797_;},function(assigned$2798){return this.assigned$2797_=assigned$2798;});
            
            //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
            $$$cl2328.defineAttr($$lazyExprTest4,'s1',function(){
                var $$lazyExprTest4=this;
                return $$$cl2328.StringBuilder().appendAll([$$$cl2328.String("s1-",3),$$$cl2328.attrGetter($$lazyExprTest4.getT$all()['functions::LazyExprTest2'],'s1').call(this).string]).string;
            },function(s1$2799){
                var $$lazyExprTest4=this;
                $$lazyExprTest4.assigned=s1$2799;
            });
        })(LazyExprTest4.$$.prototype);
    }
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDefinition testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDeclaration tst at functions.ceylon (181:4-181:30)
    var tst$2800=LazyExprTest();
    (tst$2800.x=(1));
    $$$c2329.check(tst$2800.f1((3)).equals($$$cl2328.String("3:3.2",5)),$$$cl2328.String("=> defaulted param",18));
    $$$c2329.check(tst$2800.f2((3)).equals((9)),$$$cl2328.String("=> method",9));
    $$$c2329.check(tst$2800.i1.equals((4)),$$$cl2328.String("=> attribute",12));
    $$$c2329.check(tst$2800.i2.equals((10)),$$$cl2328.String("=> attribute specifier",22));
    $$$c2329.check(tst$2800.s1.equals($$$cl2328.String("6.1",3)),$$$cl2328.String("=> attribute refinement",23));
    $$$c2329.check(tst$2800.s2((5)).equals($$$cl2328.String("7.5",3)),$$$cl2328.String("=> method refinement",20));
    setLx((1));
    $$$c2329.check(lazy_f1((3)).equals($$$cl2328.String("3.2",3)),$$$cl2328.String("=> defaulted param toplevel",27));
    $$$c2329.check(lazy_f2((3)).equals((9)),$$$cl2328.String("=> method toplevel",18));
    $$$c2329.check(getLazy_i1().equals((4)),$$$cl2328.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$2801=(1000);
    var setX$2801=function(x$2802){return x$2801=x$2802;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$2803=function (i$2804,f$2805){
        if(f$2805===undefined){f$2805=function (){
            return $$$cl2328.StringBuilder().appendAll([i$2804.string,$$$cl2328.String(".",1),(x$2801=x$2801.successor).string]).string;
        };}
        return f$2805();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$2806=function (i$2807){
        return (2).times((x$2801=x$2801.successor)).plus(i$2807);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$2808=function(){return (x$2801=x$2801.successor);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$2809;
    var getI2$2809=function(){
        return (x$2801=x$2801.successor).times((2));
    };
    x$2801=(1);
    $$$c2329.check(f1$2803((3)).equals($$$cl2328.String("3.2",3)),$$$cl2328.String("=> defaulted param local",24));
    $$$c2329.check(f2$2806((3)).equals((9)),$$$cl2328.String("=> method local",15));
    $$$c2329.check(getI1$2808().equals((4)),$$$cl2328.String("=> attribute local",18));
    $$$c2329.check(getI2$2809().equals((10)),$$$cl2328.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$2810=LazyExprTest3();
    (tst3$2810.x=(1));
    $$$c2329.check(tst3$2810.s1.equals($$$cl2328.String("s1",2)),$$$cl2328.String("=> override variable 1",22));
    (tst3$2810.s1=$$$cl2328.String("abc",3));
    $$$c2329.check(tst3$2810.s1.equals($$$cl2328.String("abc",3)),$$$cl2328.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$2811=LazyExprTest4();
    (tst4$2811.x=(1));
    $$$c2329.check(tst4$2811.s1.equals($$$cl2328.String("s1-2",4)),$$$cl2328.String("=> override getter/setter 1",27));
    (tmp$2812=tst4$2811,tmp$2812.s1=$$$cl2328.String("abc",3),tmp$2812.s1);
    var tmp$2812;
    $$$c2329.check(tst4$2811.s1.equals($$$cl2328.String("s1-4",4)),$$$cl2328.String("=> override getter/setter 2",27));
    $$$c2329.check(tst4$2811.assigned.equals($$$cl2328.String("abc",3)),$$$cl2328.String("=> override getter/setter 3",27));
    (tst$2800.x=(1));
    x$2801=(10);
    $$$c2329.check((i$2813=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$2801=x$2801.successor);
    }()),(opt$2814=tst$2800,$$$cl2328.JsCallable(opt$2814,opt$2814!==null?opt$2814.f1:null))(i$2813,undefined)).equals($$$cl2328.String("11:11.2",7)),$$$cl2328.String("=> named arg",12));
    var i$2813,opt$2814;
    $$$c2329.check((i$2815=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$2801=x$2801.successor);
    }()),f$2816=function (){return (x$2801=x$2801.successor).string;},(opt$2817=tst$2800,$$$cl2328.JsCallable(opt$2817,opt$2817!==null?opt$2817.f1:null))(i$2815,f$2816)).equals($$$cl2328.String("12:13",5)),$$$cl2328.String("=> named arg function",21));
    var i$2815,f$2816,opt$2817;
    $$$c2329.check((f$2818=function (i$2819){return $$$cl2328.StringBuilder().appendAll([i$2819.string,$$$cl2328.String("-",1),(x$2801=x$2801.successor).string]).string;},(opt$2820=tst$2800,$$$cl2328.JsCallable(opt$2820,opt$2820!==null?opt$2820.f3:null))(f$2818)).equals($$$cl2328.String("3-14",4)),$$$cl2328.String("=> named arg function with param",32));
    var f$2818,opt$2820;
};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl2328.String("test",4));
    helloAll([$$$cl2328.String("Gavin",5),$$$cl2328.String("Enrique",7),$$$cl2328.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.String}}));
    $$$c2329.check(toString((5)).equals($$$cl2328.String("5",1)),$$$cl2328.String("toString(obj)",13));
    $$$c2329.check(add($$$cl2328.Float(1.5),$$$cl2328.Float(2.5)).equals($$$cl2328.Float(4.0)),$$$cl2328.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    $$$c2329.results();
}
exports.test=test;

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$2821,y$2822){
        return x$2821.compare(y$2822);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$2823){
    return function(apat$2824){
        return function(amat$2825){
            return $$$cl2328.StringBuilder().appendAll([nombre$2823.string,$$$cl2328.String(" ",1),apat$2824.string,$$$cl2328.String(" ",1),amat$2825.string]).string;
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$2826){
    if(name$2826===undefined){name$2826=$$$cl2328.String("A",1);}
    return function(apat$2827){
        return function(amat$2828){
            return $$$cl2328.StringBuilder().appendAll([name$2826.string,$$$cl2328.String(" ",1),apat$2827.string,$$$cl2328.String(" ",1),amat$2828.string]).string;
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$2829){
    if(names$2829===undefined){names$2829=$$$cl2328.getEmpty();}
    return function(count$2830){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$2831=$$$cl2328.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$2832 = names$2829.iterator;
        var name$2833;while ((name$2833=it$2832.next())!==$$$cl2328.getFinished()){
            sb$2831.append(name$2833).append($$$cl2328.String(" ",1));
        }
        sb$2831.append($$$cl2328.String("count ",6)).append(count$2830.string);
        return sb$2831.string;
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl2328.print($$$cl2328.String("Testing multiple parameter lists...",35));
    $$$c2329.check(multiCompare()((1),(1)).equals($$$cl2328.getEqual()),$$$cl2328.String("Multi compare 1",15));
    $$$c2329.check(multiCompare()((1),(2)).equals($$$cl2328.getSmaller()),$$$cl2328.String("Multi compare 2",15));
    $$$c2329.check(multiCompare()((2),(1)).equals($$$cl2328.getLarger()),$$$cl2328.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$2834=function (a$2835,b$2836){
        return multiCompare()(a$2835,b$2836);
    };
    $$$c2329.check(comp$2834((1),(1)).equals($$$cl2328.getEqual()),$$$cl2328.String("Multi compare 4",15));
    $$$c2329.check(comp$2834((1),(2)).equals($$$cl2328.getSmaller()),$$$cl2328.String("Multi compare 5",15));
    $$$c2329.check(comp$2834((2),(1)).equals($$$cl2328.getLarger()),$$$cl2328.String("Multi compare 6",15));
    $$$c2329.check(multiFullname($$$cl2328.String("a",1))($$$cl2328.String("b",1).valueOf())($$$cl2328.String("c",1).valueOf()).equals($$$cl2328.String("a b c",5)),$$$cl2328.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$2837=function (c$2838){
        return multiFullname($$$cl2328.String("A",1))($$$cl2328.String("B",1).valueOf())(c$2838.valueOf());
    };
    $$$c2329.check(apat$2837($$$cl2328.String("C",1)).equals($$$cl2328.String("A B C",5)),$$$cl2328.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$2839=function (name$2840){
        return multiFullname($$$cl2328.String("Name",4))(name$2840.valueOf());
    };
    $$$c2329.check(nombre$2839($$$cl2328.String("Z",1))($$$cl2328.String("L",1).valueOf()).equals($$$cl2328.String("Name Z L",8)),$$$cl2328.String("Multi callable 2",16));
    $$$c2329.check(multiDefaulted()($$$cl2328.String("B",1).valueOf())($$$cl2328.String("C",1).valueOf()).equals($$$cl2328.String("A B C",5)),$$$cl2328.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$2841=multiDefaulted();
    $$$c2329.check(md1$2841($$$cl2328.String("B",1).valueOf())($$$cl2328.String("C",1).valueOf()).equals($$$cl2328.String("A B C",5)),$$$cl2328.String("Multi defaulted 2",17));
    $$$c2329.check(md1$2841($$$cl2328.String("B",1).valueOf())($$$cl2328.String("Z",1).valueOf()).equals($$$cl2328.String("A B Z",5)),$$$cl2328.String("Multi defaulted 3",17));
    $$$c2329.check(md1$2841($$$cl2328.String("Z",1).valueOf())($$$cl2328.String("C",1).valueOf()).equals($$$cl2328.String("A Z C",5)),$$$cl2328.String("Multi defaulted 4",17));
    $$$c2329.check(md1$2841($$$cl2328.String("Y",1).valueOf())($$$cl2328.String("Z",1).valueOf()).equals($$$cl2328.String("A Y Z",5)),$$$cl2328.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$2842=function (x$2843){
        return multiDefaulted()($$$cl2328.String("B",1).valueOf())(x$2843.valueOf());
    };
    $$$c2329.check(md2$2842($$$cl2328.String("C",1)).equals($$$cl2328.String("A B C",5)),$$$cl2328.String("Multi defaulted 6",17));
    $$$c2329.check(md2$2842($$$cl2328.String("Z",1)).equals($$$cl2328.String("A B Z",5)),$$$cl2328.String("Multi defaulted 7",17));
    $$$c2329.check(multiSequenced([$$$cl2328.String("A",1),$$$cl2328.String("B",1),$$$cl2328.String("C",1)].reifyCeylonType({Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.String}}))((1)).equals($$$cl2328.String("A B C count 1",13)),$$$cl2328.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$2844=function (c$2845){
        return multiSequenced([$$$cl2328.String("x",1),$$$cl2328.String("y",1),$$$cl2328.String("z",1)].reifyCeylonType({Absent:{t:$$$cl2328.Anything},Element:{t:$$$cl2328.String}}))(c$2845);
    };
    $$$c2329.check(ms1$2844((5)).equals($$$cl2328.String("x y z count 5",13)),$$$cl2328.String("Multi sequenced 2",17));
    $$$c2329.check(ms1$2844((10)).equals($$$cl2328.String("x y z count 10",14)),$$$cl2328.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
