(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$320,f$321,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$322 = a$320.iterator();
    var e$323;while ((e$323=it$322.next())!==$$$cl1.getFinished()){
        if(f$321(e$323)){
            return e$323;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$324,f$325,$$$mptypes){
    if(f$325===undefined){f$325=function (x$326){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$327 = a$324.iterator();
    var e$328;while ((e$328=it$327.next())!==$$$cl1.getFinished()){
        if(f$325(e$328)){
            return e$328;
        }
    }
    if ($$$cl1.getFinished() === e$328){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$329){
    return function (i$330){
        return i$330.minus(howMuch$329).string;
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl1.print($$$cl1.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$331=(elements$332=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),$$$cl1.array(elements$332,{Element:{t:$$$cl1.Integer}}));
    var elements$332;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$333=find(nums$331,$$$cl1.$JsCallable(function (i$334){
        return i$334.remainder((2)).equals((0));
    },{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}}),{Element:{t:$$$cl1.Integer}});
    var setFound$333=function(found$335){return found$333=found$335;};
    var i$336;
    if((i$336=found$333)!==null){
        $$$c2.check(i$336.equals((2)),$$$cl1.String("anonfunc positional",19));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc positional",19));
    }
    found$333=(f$337=function (i$338){
        return i$338.remainder((2)).equals((0));
    },a$339=nums$331,find(a$339,f$337,{Element:{t:$$$cl1.Integer}}));
    var f$337,a$339;
    var i$340;
    if((i$340=found$333)!==null){
        $$$c2.check(i$340.equals((2)),$$$cl1.String("anonfunc named",14));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$341(f$342,expect$343){
        $$$c2.check(f$342((0)).equals(expect$343),$$$cl1.StringBuilder().appendAll([$$$cl1.String("anon func returns ",18),f$342((0)).string,$$$cl1.String(" instead of ",12),expect$343.string]).string);
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$344(i$345){
        return i$345.plus((12)).string;
    };
    callFunction$341($$$cl1.$JsCallable(f$344,{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("12",2));
    callFunction$341($$$cl1.$JsCallable(function (i$346){
        return i$346.times((3)).string;
    },{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("0",1));
    (expect$347=$$$cl1.String("0",1),f$348=function (i$349){
        return i$349.power((2)).string;
    },callFunction$341(f$348,expect$347));
    var expect$347,f$348;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$350=$$$cl1.$JsCallable(function (i$351){
        return i$351.minus((10)).string;
    },{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}});
    callFunction$341($$$cl1.$JsCallable(f2$350,{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("-10",3));
    callFunction$341($$$cl1.$JsCallable(subtract((5)),{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}}),$$$cl1.String("-5",2));
    found$333=find2(nums$331,$$$cl1.$JsCallable(function (i$352){
        return i$352.compare((2)).equals($$$cl1.getLarger());
    },{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}}),{Element:{t:$$$cl1.Integer}});
    var i$353;
    if((i$353=found$333)!==null){
        $$$c2.check(i$353.equals((3)),$$$cl1.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc i>2 [2]",16));
    }
    found$333=find2(nums$331,undefined,{Element:{t:$$$cl1.Integer}});
    var i$354;
    if((i$354=found$333)!==null){
        $$$c2.check(i$354.equals((1)),$$$cl1.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc defaulted param [2]",28));
    }
};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl1.print($$$cl1.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$355){
    $$$cl1.print($$$cl1.String("hello",5).plus(name$355));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$356){
    if(names$356===undefined){names$356=$$$cl1.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$357){
    return obj$357.string;
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$358,y$359){
    return x$358.plus(y$359);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$360,f$361){
    f$361((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$362, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl1.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$362=seq$362;
    $$$cl1.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
    $$$cl1.defineAttr($$mySequence,'lastIndex',function(){
        return seq$362.lastIndex;
    });
    
    //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
    $$$cl1.defineAttr($$mySequence,'first',function(){
        return seq$362.first;
    });
    
    //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
    $$$cl1.defineAttr($$mySequence,'rest',function(){
        return seq$362.rest;
    });
    
    //MethodDefinition get at functions.ceylon (30:4-30:67)
    function get(index$363){
        return seq$362.get(index$363);
    }
    $$mySequence.get=get;
    
    //MethodDefinition span at functions.ceylon (31:4-31:88)
    function span(from$364,to$365){
        return seq$362.span(from$364,to$365);
    }
    $$mySequence.span=span;
    
    //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
    function spanFrom(from$366){
        return seq$362.spanFrom(from$366);
    }
    $$mySequence.spanFrom=spanFrom;
    
    //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
    function spanTo(to$367){
        return seq$362.spanTo(to$367);
    }
    $$mySequence.spanTo=spanTo;
    
    //MethodDefinition segment at functions.ceylon (34:4-34:102)
    function segment(from$368,length$369){
        return seq$362.segment(from$368,length$369);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
    $$$cl1.defineAttr($$mySequence,'clone',function(){
        return $$mySequence;
    });
    
    //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
    $$$cl1.defineAttr($$mySequence,'string',function(){
        return seq$362.string;
    });
    
    //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
    $$$cl1.defineAttr($$mySequence,'hash',function(){
        return seq$362.hash;
    });
    
    //MethodDefinition equals at functions.ceylon (38:4-38:75)
    function equals(other$370){
        return seq$362.equals(other$370);
    }
    $$mySequence.equals=equals;
    
    //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
    $$$cl1.defineAttr($$mySequence,'reversed',function(){
        return seq$362.reversed;
    });
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    $$$cl1.defineAttr($$mySequence,'last',function(){return seq$362.last;});
    
    //MethodDeclaration iterator at functions.ceylon (41:4-41:64)
    var iterator=function (){
        return seq$362.iterator();
    };
    $$mySequence.iterator=iterator;
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    $$$cl1.defineAttr($$mySequence,'size',function(){return seq$362.size;});
    
    //MethodDeclaration contains at functions.ceylon (43:4-43:71)
    var contains=function (other$371){
        return seq$362.contains(other$371);
    };
    $$mySequence.contains=contains;
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl1.initTypeProto(MySequence,'functions::MySequence',$$$cl1.Basic,$$$cl1.Sequence);
    }
    return MySequence;
}
exports.$init$MySequence=$init$MySequence;
$init$MySequence();

//ClassDefinition RefHelper at functions.ceylon (46:0-48:0)
function RefHelper($$refHelper){
    $init$RefHelper();
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    
    //MethodDefinition f at functions.ceylon (47:4-47:47)
    function f(i$372){
        return true;
    }
    $$refHelper.f=f;
    return $$refHelper;
}
function $init$RefHelper(){
    if (RefHelper.$$===undefined){
        $$$cl1.initTypeProto(RefHelper,'functions::RefHelper',$$$cl1.Basic);
    }
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDefinition testMethodReference at functions.ceylon (50:0-58:0)
function testMethodReference(){
    
    //AttributeDeclaration obj1 at functions.ceylon (51:4-51:28)
    var obj1$373=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$374=MySequence($$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Element:{t:$$$cl1.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$375(x$376){
        return x$376((0));
    };
    $$$c2.check(tst$375($$$cl1.$JsCallable((opt$377=obj1$373,$$$cl1.JsCallable(opt$377,opt$377!==null?opt$377.f:null)),{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}})),$$$cl1.String("Reference to method",19));
    var opt$377;
    $$$c2.check(tst$375($$$cl1.$JsCallable((opt$378=obj2$374,$$$cl1.JsCallable(opt$378,opt$378!==null?opt$378.defines:null)),{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Boolean}})),$$$cl1.String("Reference to method from ceylon.language",40));
    var opt$378;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$379,i2$380,i3$381){
    if(i2$380===undefined){i2$380=i1$379.plus((1));}
    if(i3$381===undefined){i3$381=i1$379.plus(i2$380);}
    return $$$cl1.StringBuilder().appendAll([i1$379.string,$$$cl1.String(",",1),i2$380.string,$$$cl1.String(",",1),i3$381.string]).string;
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$382, i2$383, i3$384, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$383===undefined){i2$383=i1$382.plus((1));}
    if(i3$384===undefined){i3$384=i1$382.plus(i2$383);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    var s=$$$cl1.StringBuilder().appendAll([i1$382.string,$$$cl1.String(",",1),i2$383.string,$$$cl1.String(",",1),i3$384.string]).string;
    $$$cl1.defineAttr($$defParamTest1,'s',function(){return s;});
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl1.Basic);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$385, i2$386, i3$387, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$385=i1$385;
    if(i2$386===undefined){i2$386=i1$385.plus((1));}
    $$defParamTest2.i2$386=i2$386;
    if(i3$387===undefined){i3$387=i1$385.plus(i2$386);}
    $$defParamTest2.i3$387=i3$387;
    
    //MethodDefinition f at functions.ceylon (67:4-67:55)
    function f(){
        return $$$cl1.StringBuilder().appendAll([i1$385.string,$$$cl1.String(",",1),i2$386.string,$$$cl1.String(",",1),i3$387.string]).string;
    }
    $$defParamTest2.f=f;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl1.Basic);
    }
    return DefParamTest2;
}
exports.$init$DefParamTest2=$init$DefParamTest2;
$init$DefParamTest2();

//ClassDefinition DefParamTest3 at functions.ceylon (69:0-73:0)
function DefParamTest3($$defParamTest3){
    $init$DefParamTest3();
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    
    //MethodDefinition f at functions.ceylon (70:4-72:4)
    function f(i1$388,i2$389,i3$390){
        if(i2$389===undefined){i2$389=i1$388.plus((1));}
        if(i3$390===undefined){i3$390=i1$388.plus(i2$389);}
        return $$$cl1.StringBuilder().appendAll([i1$388.string,$$$cl1.String(",",1),i2$389.string,$$$cl1.String(",",1),i3$390.string]).string;
    }
    $$defParamTest3.f=f;
    return $$defParamTest3;
}
function $init$DefParamTest3(){
    if (DefParamTest3.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl1.Basic);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2.check(defParamTest((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters 1",22));
    $$$c2.check(defParamTest((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters 2",22));
    $$$c2.check(defParamTest((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters 3",22));
    $$$c2.check((i1$391=(1),defParamTest(i1$391,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters named 1",28));
    var i1$391;
    $$$c2.check((i1$392=(1),i2$393=(3),defParamTest(i1$392,i2$393,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters named 2",28));
    var i1$392,i2$393;
    $$$c2.check((i1$394=(1),i3$395=(0),defParamTest(i1$394,undefined,i3$395)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters named 3",28));
    var i1$394,i3$395;
    $$$c2.check(DefParamTest1((1)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class 1",28));
    $$$c2.check(DefParamTest1((1),(3)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class 2",28));
    $$$c2.check(DefParamTest1((1),(3),(0)).s.equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class 3",28));
    $$$c2.check((i1$396=(1),DefParamTest1(i1$396,undefined,undefined)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class named 1",34));
    var i1$396;
    $$$c2.check((i1$397=(1),i2$398=(3),DefParamTest1(i1$397,i2$398,undefined)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class named 2",34));
    var i1$397,i2$398;
    $$$c2.check((i1$399=(1),i3$400=(0),DefParamTest1(i1$399,undefined,i3$400)).s.equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class named 3",34));
    var i1$399,i3$400;
    $$$c2.check(DefParamTest2((1)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 1",29));
    $$$c2.check(DefParamTest2((1),(3)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 2",29));
    $$$c2.check(DefParamTest2((1),(3),(0)).f().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class2 3",29));
    $$$c2.check((i1$401=(1),DefParamTest2(i1$401,undefined,undefined)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 named 1",35));
    var i1$401;
    $$$c2.check((i1$402=(1),i2$403=(3),DefParamTest2(i1$402,i2$403,undefined)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 named 2",35));
    var i1$402,i2$403;
    $$$c2.check((i1$404=(1),i3$405=(0),DefParamTest2(i1$404,undefined,i3$405)).f().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class2 named 3",35));
    var i1$404,i3$405;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$406=DefParamTest3();
    $$$c2.check(tst$406.f((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters 1",29));
    $$$c2.check(tst$406.f((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters 2",29));
    $$$c2.check(tst$406.f((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted method parameters 3",29));
    $$$c2.check((i1$407=(1),(opt$408=tst$406,$$$cl1.JsCallable(opt$408,opt$408!==null?opt$408.f:null))(i1$407,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters named 1",35));
    var i1$407,opt$408;
    $$$c2.check((i1$409=(1),i2$410=(3),(opt$411=tst$406,$$$cl1.JsCallable(opt$411,opt$411!==null?opt$411.f:null))(i1$409,i2$410,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters named 2",35));
    var i1$409,i2$410,opt$411;
    $$$c2.check((i1$412=(1),i3$413=(0),(opt$414=tst$406,$$$cl1.JsCallable(opt$414,opt$414!==null?opt$414.f:null))(i1$412,undefined,i3$413)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted method parameters named 3",35));
    var i1$412,i3$413,opt$414;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$415($$getterTest$415){
        $init$GetterTest$415();
        if ($$getterTest$415===undefined)$$getterTest$415=new GetterTest$415.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        var i$416=(0);
        $$$cl1.defineAttr($$getterTest$415,'i$416',function(){return i$416;},function(i$417){return i$416=i$417;});
        
        //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
        $$$cl1.defineAttr($$getterTest$415,'x',function(){
            return (i$416=i$416.successor);
        });
        return $$getterTest$415;
    }
    function $init$GetterTest$415(){
        if (GetterTest$415.$$===undefined){
            $$$cl1.initTypeProto(GetterTest$415,'functions::testGetterMethodDefinitions.GetterTest',$$$cl1.Basic);
        }
        return GetterTest$415;
    }
    $init$GetterTest$415();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$418=GetterTest$415();
    $$$c2.check(gt$418.x.equals((1)),$$$cl1.String("getter defined as method 1",26));
    $$$c2.check(gt$418.x.equals((2)),$$$cl1.String("getter defined as method 2",26));
    $$$c2.check(gt$418.x.equals((3)),$$$cl1.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$419,y$420,z$421){
    if(x$419===undefined){x$419=$$$cl1.String("x",1);}
    if(y$420===undefined){y$420=x$419.plus($$$cl1.String("y",1));}
    if(z$421===undefined){z$421=$$$cl1.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$422=x$419.plus($$$cl1.String(",",1)).plus(y$420);
    var setResult$422=function(result$423){return result$422=result$423;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$424 = z$421.iterator();
    var s$425;while ((s$425=it$424.next())!==$$$cl1.getFinished()){
        (result$422=result$422.plus($$$cl1.String(",",1).plus(s$425)));
    }
    return result$422;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$426, more$427, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$427===undefined){more$427=$$$cl1.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    var i=i$426;
    $$$cl1.defineAttr($$issue105,'i',function(){return i;});
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl1.initTypeProto(Issue105,'functions::Issue105',$$$cl1.Basic);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2.check((namedArgFunc(undefined,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("x,xy",4)),$$$cl1.String("named arguments 1",17));
    $$$c2.check((x$428=$$$cl1.String("a",1),namedArgFunc(x$428,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("a,ay",4)),$$$cl1.String("named arguments 2",17));
    var x$428;
    $$$c2.check((y$429=$$$cl1.String("b",1),namedArgFunc(undefined,y$429,$$$cl1.getEmpty())).equals($$$cl1.String("x,b",3)),$$$cl1.String("named arguments 3",17));
    var y$429;
    $$$c2.check((z$430=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$430)).equals($$$cl1.String("x,xy,c",6)),$$$cl1.String("named arguments 4",17));
    var z$430;
    $$$c2.check((x$431=$$$cl1.String("a",1),y$432=$$$cl1.String("b",1),z$433=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$431,y$432,z$433)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 5",17));
    var x$431,y$432,z$433;
    $$$c2.check((y$434=$$$cl1.String("b",1),x$435=$$$cl1.String("a",1),z$436=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$435,y$434,z$436)).equals($$$cl1.String("a,b,c,d",7)),$$$cl1.String("named arguments 6",17));
    var y$434,x$435,z$436;
    $$$c2.check((x$437=$$$cl1.String("a",1),z$438=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$437,undefined,z$438)).equals($$$cl1.String("a,ay,c",6)),$$$cl1.String("named arguments 7",17));
    var x$437,z$438;
    $$$c2.check((y$439=$$$cl1.String("b",1),z$440=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,y$439,z$440)).equals($$$cl1.String("x,b,c",5)),$$$cl1.String("named arguments 8",17));
    var y$439,z$440;
    $$$c2.check((y$441=$$$cl1.String("b",1),x$442=$$$cl1.String("a",1),namedArgFunc(x$442,y$441,$$$cl1.getEmpty())).equals($$$cl1.String("a,b",3)),$$$cl1.String("named arguments 9",17));
    var y$441,x$442;
    $$$c2.check((z$443=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$443)).equals($$$cl1.String("x,xy,c,d",8)),$$$cl1.String("named arguments 11",18));
    var z$443;
    $$$c2.check((y$444=$$$cl1.String("b",1),z$445=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),x$446=$$$cl1.String("a",1),namedArgFunc(x$446,y$444,z$445)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 12",18));
    var y$444,z$445,x$446;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$447=(i$448=(1),more$449=$$$cl1.Tuple((i$450=(2),Issue105(i$450,$$$cl1.getEmpty())),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$448,more$449));
    var i$448,more$449,i$450;
    $$$c2.check(issue105$447.i.equals((1)),$$$cl1.String("issue #105",10));
};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl1.initTypeProto(LazyExprBase,'functions::LazyExprBase');
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
    var x=(1000);
    $$$cl1.defineAttr($$lazyExprTest,'x',function(){return x;},function(x$451){return x=x$451;});
    
    //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
    var f1=function (i$452,f$453){
        if(f$453===undefined){f$453=function (){
            return $$$cl1.StringBuilder().appendAll([i$452.string,$$$cl1.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor).string]).string;
        };}
        return $$$cl1.StringBuilder().appendAll([i$452.string,$$$cl1.String(":",1),f$453().string]).string;
    };
    $$lazyExprTest.f1=f1;
    
    //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
    var f2=function (i$454){
        return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$454);
    };
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
    $$lazyExprTest.s2=function (i$455){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl1.String(".",1),i$455.string]).string;
    };
    
    //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
    var f3=function (f$456){
        return f$456(($$lazyExprTest.x=$$lazyExprTest.x.successor));
    };
    $$lazyExprTest.f3=f3;
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl1.Basic,$init$LazyExprBase());
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$457=(1000);
var getLx=function(){return lx$457;};
exports.getLx=getLx;
var setLx=function(lx$458){return lx$457=lx$458;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$459,f$460){
    if(f$460===undefined){f$460=function (){
        return $$$cl1.StringBuilder().appendAll([i$459.string,$$$cl1.String(".",1),(setLx(getLx().successor)).string]).string;
    };}
    return f$460();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$461){
    return (2).times((setLx(getLx().successor))).plus(i$461);
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
    var x=(1000);
    $$$cl1.defineAttr($$lazyExprTest2,'x',function(){return x;},function(x$462){return x=x$462;});
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    $$$cl1.defineAttr($$lazyExprTest2,'s1',function(){return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;});
    
    //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
    var s2=function (i$463){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl1.String("-",1),i$463.string]).string;
    };
    $$lazyExprTest2.s2=s2;
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl1.Basic,$init$LazyExprBase());
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
    var s1=$$$cl1.String("s1",2);
    $$$cl1.defineAttr($$lazyExprTest3,'s1',function(){return s1;},function(s1$464){return s1=s1$464;});
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
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
    $$$cl1.copySuperAttr($$lazyExprTest4,'s1','$$functions$LazyExprTest2');
    
    //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
    var assigned=$$$cl1.String("",0);
    $$$cl1.defineAttr($$lazyExprTest4,'assigned',function(){return assigned;},function(assigned$465){return assigned=assigned$465;});
    
    //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
    $$$cl1.defineAttr($$lazyExprTest4,'s1',function(){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("s1-",3),$$lazyExprTest4.s1$$functions$LazyExprTest2.string]).string;
    },function(s1$466){
        $$lazyExprTest4.assigned=s1$466;
    });
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
    }
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDefinition testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDeclaration tst at functions.ceylon (181:4-181:30)
    var tst$467=LazyExprTest();
    (tst$467.x=(1));
    $$$c2.check(tst$467.f1((3)).equals($$$cl1.String("3:3.2",5)),$$$cl1.String("=> defaulted param",18));
    $$$c2.check(tst$467.f2((3)).equals((9)),$$$cl1.String("=> method",9));
    $$$c2.check(tst$467.i1.equals((4)),$$$cl1.String("=> attribute",12));
    $$$c2.check(tst$467.i2.equals((10)),$$$cl1.String("=> attribute specifier",22));
    $$$c2.check(tst$467.s1.equals($$$cl1.String("6.1",3)),$$$cl1.String("=> attribute refinement",23));
    $$$c2.check(tst$467.s2((5)).equals($$$cl1.String("7.5",3)),$$$cl1.String("=> method refinement",20));
    setLx((1));
    $$$c2.check(lazy_f1((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param toplevel",27));
    $$$c2.check(lazy_f2((3)).equals((9)),$$$cl1.String("=> method toplevel",18));
    $$$c2.check(getLazy_i1().equals((4)),$$$cl1.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$468=(1000);
    var setX$468=function(x$469){return x$468=x$469;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$470=function (i$471,f$472){
        if(f$472===undefined){f$472=function (){
            return $$$cl1.StringBuilder().appendAll([i$471.string,$$$cl1.String(".",1),(x$468=x$468.successor).string]).string;
        };}
        return f$472();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$473=function (i$474){
        return (2).times((x$468=x$468.successor)).plus(i$474);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$475=function(){return (x$468=x$468.successor);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$476;
    var getI2$476=function(){
        return (x$468=x$468.successor).times((2));
    };
    x$468=(1);
    $$$c2.check(f1$470((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param local",24));
    $$$c2.check(f2$473((3)).equals((9)),$$$cl1.String("=> method local",15));
    $$$c2.check(getI1$475().equals((4)),$$$cl1.String("=> attribute local",18));
    $$$c2.check(getI2$476().equals((10)),$$$cl1.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$477=LazyExprTest3();
    (tst3$477.x=(1));
    $$$c2.check(tst3$477.s1.equals($$$cl1.String("s1",2)),$$$cl1.String("=> override variable 1",22));
    (tst3$477.s1=$$$cl1.String("abc",3));
    $$$c2.check(tst3$477.s1.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$478=LazyExprTest4();
    (tst4$478.x=(1));
    $$$c2.check(tst4$478.s1.equals($$$cl1.String("s1-2",4)),$$$cl1.String("=> override getter/setter 1",27));
    (tmp$479=tst4$478,tmp$479.s1=$$$cl1.String("abc",3),tmp$479.s1);
    var tmp$479;
    $$$c2.check(tst4$478.s1.equals($$$cl1.String("s1-4",4)),$$$cl1.String("=> override getter/setter 2",27));
    $$$c2.check(tst4$478.assigned.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override getter/setter 3",27));
    (tst$467.x=(1));
    x$468=(10);
    $$$c2.check((i$480=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$468=x$468.successor);
    }()),(opt$481=tst$467,$$$cl1.JsCallable(opt$481,opt$481!==null?opt$481.f1:null))(i$480,undefined)).equals($$$cl1.String("11:11.2",7)),$$$cl1.String("=> named arg",12));
    var i$480,opt$481;
    $$$c2.check((i$482=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$468=x$468.successor);
    }()),f$483=function (){return (x$468=x$468.successor).string;},(opt$484=tst$467,$$$cl1.JsCallable(opt$484,opt$484!==null?opt$484.f1:null))(i$482,f$483)).equals($$$cl1.String("12:13",5)),$$$cl1.String("=> named arg function",21));
    var i$482,f$483,opt$484;
    $$$c2.check((f$485=function (i$486){return $$$cl1.StringBuilder().appendAll([i$486.string,$$$cl1.String("-",1),(x$468=x$468.successor).string]).string;},(opt$487=tst$467,$$$cl1.JsCallable(opt$487,opt$487!==null?opt$487.f3:null))(f$485)).equals($$$cl1.String("3-14",4)),$$$cl1.String("=> named arg function with param",32));
    var f$485,opt$487;
};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl1.String("test",4));
    helloAll([$$$cl1.String("Gavin",5),$$$cl1.String("Enrique",7),$$$cl1.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
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

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$488,y$489){
        return x$488.compare(y$489);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$490){
    return function(apat$491){
        return function(amat$492){
            return $$$cl1.StringBuilder().appendAll([nombre$490.string,$$$cl1.String(" ",1),apat$491.string,$$$cl1.String(" ",1),amat$492.string]).string;
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$493){
    if(name$493===undefined){name$493=$$$cl1.String("A",1);}
    return function(apat$494){
        return function(amat$495){
            return $$$cl1.StringBuilder().appendAll([name$493.string,$$$cl1.String(" ",1),apat$494.string,$$$cl1.String(" ",1),amat$495.string]).string;
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$496){
    if(names$496===undefined){names$496=$$$cl1.getEmpty();}
    return function(count$497){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$498=$$$cl1.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$499 = names$496.iterator();
        var name$500;while ((name$500=it$499.next())!==$$$cl1.getFinished()){
            sb$498.append(name$500).append($$$cl1.String(" ",1));
        }
        sb$498.append($$$cl1.String("count ",6)).append(count$497.string);
        return sb$498.string;
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl1.print($$$cl1.String("Testing multiple parameter lists...",35));
    $$$c2.check(multiCompare()((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 1",15));
    $$$c2.check(multiCompare()((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 2",15));
    $$$c2.check(multiCompare()((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$501=function (a$502,b$503){
        return multiCompare()(a$502,b$503);
    };
    $$$c2.check(comp$501((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 4",15));
    $$$c2.check(comp$501((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 5",15));
    $$$c2.check(comp$501((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 6",15));
    $$$c2.check(multiFullname($$$cl1.String("a",1))($$$cl1.String("b",1).valueOf())($$$cl1.String("c",1).valueOf()).equals($$$cl1.String("a b c",5)),$$$cl1.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$504=function (c$505){
        return multiFullname($$$cl1.String("A",1))($$$cl1.String("B",1).valueOf())(c$505.valueOf());
    };
    $$$c2.check(apat$504($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$506=function (name$507){
        return multiFullname($$$cl1.String("Name",4))(name$507.valueOf());
    };
    $$$c2.check(nombre$506($$$cl1.String("Z",1))($$$cl1.String("L",1).valueOf()).equals($$$cl1.String("Name Z L",8)),$$$cl1.String("Multi callable 2",16));
    $$$c2.check(multiDefaulted()($$$cl1.String("B",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$508=$$$cl1.$JsCallable(multiDefaulted(),{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.String}}}});
    $$$c2.check(md1$508($$$cl1.String("B",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 2",17));
    $$$c2.check(md1$508($$$cl1.String("B",1).valueOf())($$$cl1.String("Z",1).valueOf()).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 3",17));
    $$$c2.check(md1$508($$$cl1.String("Z",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A Z C",5)),$$$cl1.String("Multi defaulted 4",17));
    $$$c2.check(md1$508($$$cl1.String("Y",1).valueOf())($$$cl1.String("Z",1).valueOf()).equals($$$cl1.String("A Y Z",5)),$$$cl1.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$509=function (x$510){
        return multiDefaulted()($$$cl1.String("B",1).valueOf())(x$510.valueOf());
    };
    $$$c2.check(md2$509($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 6",17));
    $$$c2.check(md2$509($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 7",17));
    $$$c2.check(multiSequenced([$$$cl1.String("A",1),$$$cl1.String("B",1),$$$cl1.String("C",1)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}))((1)).equals($$$cl1.String("A B C count 1",13)),$$$cl1.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$511=function (c$512){
        return multiSequenced([$$$cl1.String("x",1),$$$cl1.String("y",1),$$$cl1.String("z",1)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}))(c$512);
    };
    $$$c2.check(ms1$511((5)).equals($$$cl1.String("x y z count 5",13)),$$$cl1.String("Multi sequenced 2",17));
    $$$c2.check(ms1$511((10)).equals($$$cl1.String("x y z count 10",14)),$$$cl1.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
