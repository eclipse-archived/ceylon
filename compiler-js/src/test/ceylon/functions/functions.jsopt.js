(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl2381=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2382=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$2700,f$2701,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$2702 = a$2700.iterator();
    var e$2703;while ((e$2703=it$2702.next())!==$$$cl2381.getFinished()){
        if(f$2701(e$2703)){
            return e$2703;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$2704,f$2705,$$$mptypes){
    if(f$2705===undefined){f$2705=function (x$2706){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$2707 = a$2704.iterator();
    var e$2708;while ((e$2708=it$2707.next())!==$$$cl2381.getFinished()){
        if(f$2705(e$2708)){
            return e$2708;
        }
    }
    if ($$$cl2381.getFinished() === e$2708){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$2709){
    return function (i$2710){
        return i$2710.minus(howMuch$2709).string;
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl2381.print($$$cl2381.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$2711=(elements$2712=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),$$$cl2381.array(elements$2712,{Element:{t:$$$cl2381.Integer}}));
    var elements$2712;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$2713=find(nums$2711,$$$cl2381.$JsCallable(function (i$2714){
        return i$2714.remainder((2)).equals((0));
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Boolean}}),{Element:{t:$$$cl2381.Integer}});
    var setFound$2713=function(found$2715){return found$2713=found$2715;};
    var i$2716;
    if((i$2716=found$2713)!==null){
        $$$c2382.check(i$2716.equals((2)),$$$cl2381.String("anonfunc positional",19));
    }else {
        $$$c2382.fail($$$cl2381.String("anonfunc positional",19));
    }
    found$2713=(f$2717=function (i$2718){
        return i$2718.remainder((2)).equals((0));
    },a$2719=nums$2711,find(a$2719,f$2717,{Element:{t:$$$cl2381.Integer}}));
    var f$2717,a$2719;
    var i$2720;
    if((i$2720=found$2713)!==null){
        $$$c2382.check(i$2720.equals((2)),$$$cl2381.String("anonfunc named",14));
    }else {
        $$$c2382.fail($$$cl2381.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$2721(f$2722,expect$2723){
        $$$c2382.check(f$2722((0)).equals(expect$2723),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("anon func returns ",18),f$2722((0)).string,$$$cl2381.String(" instead of ",12),expect$2723.string]).string);
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$2724(i$2725){
        return i$2725.plus((12)).string;
    };
    callFunction$2721($$$cl2381.$JsCallable(f$2724,{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.String}}),$$$cl2381.String("12",2));
    callFunction$2721($$$cl2381.$JsCallable(function (i$2726){
        return i$2726.times((3)).string;
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.String}}),$$$cl2381.String("0",1));
    (expect$2727=$$$cl2381.String("0",1),f$2728=function (i$2729){
        return i$2729.power((2)).string;
    },callFunction$2721(f$2728,expect$2727));
    var expect$2727,f$2728;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$2730=$$$cl2381.$JsCallable(function (i$2731){
        return i$2731.minus((10)).string;
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.String}});
    callFunction$2721($$$cl2381.$JsCallable(f2$2730,{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.String}}),$$$cl2381.String("-10",3));
    callFunction$2721($$$cl2381.$JsCallable(subtract((5)),{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.String}}),$$$cl2381.String("-5",2));
    found$2713=find2(nums$2711,$$$cl2381.$JsCallable(function (i$2732){
        return i$2732.compare((2)).equals($$$cl2381.getLarger());
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Boolean}}),{Element:{t:$$$cl2381.Integer}});
    var i$2733;
    if((i$2733=found$2713)!==null){
        $$$c2382.check(i$2733.equals((3)),$$$cl2381.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2382.fail($$$cl2381.String("anonfunc i>2 [2]",16));
    }
    found$2713=find2(nums$2711,undefined,{Element:{t:$$$cl2381.Integer}});
    var i$2734;
    if((i$2734=found$2713)!==null){
        $$$c2382.check(i$2734.equals((1)),$$$cl2381.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2382.fail($$$cl2381.String("anonfunc defaulted param [2]",28));
    }
};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl2381.print($$$cl2381.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$2735){
    $$$cl2381.print($$$cl2381.String("hello",5).plus(name$2735));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$2736){
    if(names$2736===undefined){names$2736=$$$cl2381.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$2737){
    return obj$2737.string;
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$2738,y$2739){
    return x$2738.plus(y$2739);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$2740,f$2741){
    f$2741((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$2742, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl2381.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$2742=seq$2742;
    $$$cl2381.Sequence($$mySequence);
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl2381.initTypeProto(MySequence,'functions::MySequence',$$$cl2381.Basic,$$$cl2381.Sequence);
        (function($$mySequence){
            
            //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
            $$$cl2381.defineAttr($$mySequence,'lastIndex',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.lastIndex;
            });
            //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
            $$$cl2381.defineAttr($$mySequence,'first',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.first;
            });
            //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
            $$$cl2381.defineAttr($$mySequence,'rest',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.rest;
            });
            //MethodDefinition get at functions.ceylon (30:4-30:67)
            $$mySequence.get=function get(index$2743){
                var $$mySequence=this;
                return $$mySequence.seq$2742.get(index$2743);
            };
            //MethodDefinition span at functions.ceylon (31:4-31:88)
            $$mySequence.span=function span(from$2744,to$2745){
                var $$mySequence=this;
                return $$mySequence.seq$2742.span(from$2744,to$2745);
            };
            //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
            $$mySequence.spanFrom=function spanFrom(from$2746){
                var $$mySequence=this;
                return $$mySequence.seq$2742.spanFrom(from$2746);
            };
            //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
            $$mySequence.spanTo=function spanTo(to$2747){
                var $$mySequence=this;
                return $$mySequence.seq$2742.spanTo(to$2747);
            };
            //MethodDefinition segment at functions.ceylon (34:4-34:102)
            $$mySequence.segment=function segment(from$2748,length$2749){
                var $$mySequence=this;
                return $$mySequence.seq$2742.segment(from$2748,length$2749);
            };
            //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
            $$$cl2381.defineAttr($$mySequence,'clone',function(){
                var $$mySequence=this;
                return $$mySequence;
            });
            //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
            $$$cl2381.defineAttr($$mySequence,'string',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.string;
            });
            //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
            $$$cl2381.defineAttr($$mySequence,'hash',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.hash;
            });
            //MethodDefinition equals at functions.ceylon (38:4-38:75)
            $$mySequence.equals=function equals(other$2750){
                var $$mySequence=this;
                return $$mySequence.seq$2742.equals(other$2750);
            };
            //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
            $$$cl2381.defineAttr($$mySequence,'reversed',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.reversed;
            });
            //AttributeDeclaration last at functions.ceylon (40:4-40:42)
            $$$cl2381.defineAttr($$mySequence,'last',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.last;
            });
            
            //MethodDeclaration iterator at functions.ceylon (41:4-41:64)
            $$mySequence.iterator=function (){
                var $$mySequence=this;
                return $$mySequence.seq$2742.iterator();
            };
            
            //AttributeDeclaration size at functions.ceylon (42:4-42:42)
            $$$cl2381.defineAttr($$mySequence,'size',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2742.size;
            });
            
            //MethodDeclaration contains at functions.ceylon (43:4-43:71)
            $$mySequence.contains=function (other$2751){
                var $$mySequence=this;
                return $$mySequence.seq$2742.contains(other$2751);
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
        $$$cl2381.initTypeProto(RefHelper,'functions::RefHelper',$$$cl2381.Basic);
        (function($$refHelper){
            
            //MethodDefinition f at functions.ceylon (47:4-47:47)
            $$refHelper.f=function f(i$2752){
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
    var obj1$2753=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$2754=MySequence($$$cl2381.Tuple($$$cl2381.String("hi",2),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Element:{t:$$$cl2381.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$2755(x$2756){
        return x$2756((0));
    };
    $$$c2382.check(tst$2755($$$cl2381.$JsCallable((opt$2757=obj1$2753,$$$cl2381.JsCallable(opt$2757,opt$2757!==null?opt$2757.f:null)),{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Boolean}})),$$$cl2381.String("Reference to method",19));
    var opt$2757;
    $$$c2382.check(tst$2755($$$cl2381.$JsCallable((opt$2758=obj2$2754,$$$cl2381.JsCallable(opt$2758,opt$2758!==null?opt$2758.defines:null)),{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Boolean}})),$$$cl2381.String("Reference to method from ceylon.language",40));
    var opt$2758;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$2759,i2$2760,i3$2761){
    if(i2$2760===undefined){i2$2760=i1$2759.plus((1));}
    if(i3$2761===undefined){i3$2761=i1$2759.plus(i2$2760);}
    return $$$cl2381.StringBuilder().appendAll([i1$2759.string,$$$cl2381.String(",",1),i2$2760.string,$$$cl2381.String(",",1),i3$2761.string]).string;
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$2762, i2$2763, i3$2764, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$2763===undefined){i2$2763=i1$2762.plus((1));}
    if(i3$2764===undefined){i3$2764=i1$2762.plus(i2$2763);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    $$defParamTest1.s$2765_=$$$cl2381.StringBuilder().appendAll([i1$2762.string,$$$cl2381.String(",",1),i2$2763.string,$$$cl2381.String(",",1),i3$2764.string]).string;
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl2381.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl2381.Basic);
        (function($$defParamTest1){
            
            //AttributeDeclaration s at functions.ceylon (64:4-64:44)
            $$$cl2381.defineAttr($$defParamTest1,'s',function(){return this.s$2765_;});
        })(DefParamTest1.$$.prototype);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$2766, i2$2767, i3$2768, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$2766=i1$2766;
    if(i2$2767===undefined){i2$2767=$$defParamTest2.i1$2766.plus((1));}
    $$defParamTest2.i2$2767=i2$2767;
    if(i3$2768===undefined){i3$2768=$$defParamTest2.i1$2766.plus($$defParamTest2.i2$2767);}
    $$defParamTest2.i3$2768=i3$2768;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl2381.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl2381.Basic);
        (function($$defParamTest2){
            
            //MethodDefinition f at functions.ceylon (67:4-67:55)
            $$defParamTest2.f=function f(){
                var $$defParamTest2=this;
                return $$$cl2381.StringBuilder().appendAll([$$defParamTest2.i1$2766.string,$$$cl2381.String(",",1),$$defParamTest2.i2$2767.string,$$$cl2381.String(",",1),$$defParamTest2.i3$2768.string]).string;
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
        $$$cl2381.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl2381.Basic);
        (function($$defParamTest3){
            
            //MethodDefinition f at functions.ceylon (70:4-72:4)
            $$defParamTest3.f=function f(i1$2769,i2$2770,i3$2771){
                var $$defParamTest3=this;
                if(i2$2770===undefined){i2$2770=i1$2769.plus((1));}
                if(i3$2771===undefined){i3$2771=i1$2769.plus(i2$2770);}
                return $$$cl2381.StringBuilder().appendAll([i1$2769.string,$$$cl2381.String(",",1),i2$2770.string,$$$cl2381.String(",",1),i3$2771.string]).string;
            };
        })(DefParamTest3.$$.prototype);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2382.check(defParamTest((1)).equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted parameters 1",22));
    $$$c2382.check(defParamTest((1),(3)).equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted parameters 2",22));
    $$$c2382.check(defParamTest((1),(3),(0)).equals($$$cl2381.String("1,3,0",5)),$$$cl2381.String("defaulted parameters 3",22));
    $$$c2382.check((i1$2772=(1),defParamTest(i1$2772,undefined,undefined)).equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted parameters named 1",28));
    var i1$2772;
    $$$c2382.check((i1$2773=(1),i2$2774=(3),defParamTest(i1$2773,i2$2774,undefined)).equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted parameters named 2",28));
    var i1$2773,i2$2774;
    $$$c2382.check((i1$2775=(1),i3$2776=(0),defParamTest(i1$2775,undefined,i3$2776)).equals($$$cl2381.String("1,2,0",5)),$$$cl2381.String("defaulted parameters named 3",28));
    var i1$2775,i3$2776;
    $$$c2382.check(DefParamTest1((1)).s.equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted parameters class 1",28));
    $$$c2382.check(DefParamTest1((1),(3)).s.equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted parameters class 2",28));
    $$$c2382.check(DefParamTest1((1),(3),(0)).s.equals($$$cl2381.String("1,3,0",5)),$$$cl2381.String("defaulted parameters class 3",28));
    $$$c2382.check((i1$2777=(1),DefParamTest1(i1$2777,undefined,undefined)).s.equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted parameters class named 1",34));
    var i1$2777;
    $$$c2382.check((i1$2778=(1),i2$2779=(3),DefParamTest1(i1$2778,i2$2779,undefined)).s.equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted parameters class named 2",34));
    var i1$2778,i2$2779;
    $$$c2382.check((i1$2780=(1),i3$2781=(0),DefParamTest1(i1$2780,undefined,i3$2781)).s.equals($$$cl2381.String("1,2,0",5)),$$$cl2381.String("defaulted parameters class named 3",34));
    var i1$2780,i3$2781;
    $$$c2382.check(DefParamTest2((1)).f().equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted parameters class2 1",29));
    $$$c2382.check(DefParamTest2((1),(3)).f().equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted parameters class2 2",29));
    $$$c2382.check(DefParamTest2((1),(3),(0)).f().equals($$$cl2381.String("1,3,0",5)),$$$cl2381.String("defaulted parameters class2 3",29));
    $$$c2382.check((i1$2782=(1),DefParamTest2(i1$2782,undefined,undefined)).f().equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted parameters class2 named 1",35));
    var i1$2782;
    $$$c2382.check((i1$2783=(1),i2$2784=(3),DefParamTest2(i1$2783,i2$2784,undefined)).f().equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted parameters class2 named 2",35));
    var i1$2783,i2$2784;
    $$$c2382.check((i1$2785=(1),i3$2786=(0),DefParamTest2(i1$2785,undefined,i3$2786)).f().equals($$$cl2381.String("1,2,0",5)),$$$cl2381.String("defaulted parameters class2 named 3",35));
    var i1$2785,i3$2786;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$2787=DefParamTest3();
    $$$c2382.check(tst$2787.f((1)).equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted method parameters 1",29));
    $$$c2382.check(tst$2787.f((1),(3)).equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted method parameters 2",29));
    $$$c2382.check(tst$2787.f((1),(3),(0)).equals($$$cl2381.String("1,3,0",5)),$$$cl2381.String("defaulted method parameters 3",29));
    $$$c2382.check((i1$2788=(1),(opt$2789=tst$2787,$$$cl2381.JsCallable(opt$2789,opt$2789!==null?opt$2789.f:null))(i1$2788,undefined,undefined)).equals($$$cl2381.String("1,2,3",5)),$$$cl2381.String("defaulted method parameters named 1",35));
    var i1$2788,opt$2789;
    $$$c2382.check((i1$2790=(1),i2$2791=(3),(opt$2792=tst$2787,$$$cl2381.JsCallable(opt$2792,opt$2792!==null?opt$2792.f:null))(i1$2790,i2$2791,undefined)).equals($$$cl2381.String("1,3,4",5)),$$$cl2381.String("defaulted method parameters named 2",35));
    var i1$2790,i2$2791,opt$2792;
    $$$c2382.check((i1$2793=(1),i3$2794=(0),(opt$2795=tst$2787,$$$cl2381.JsCallable(opt$2795,opt$2795!==null?opt$2795.f:null))(i1$2793,undefined,i3$2794)).equals($$$cl2381.String("1,2,0",5)),$$$cl2381.String("defaulted method parameters named 3",35));
    var i1$2793,i3$2794,opt$2795;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$2796($$getterTest$2796){
        $init$GetterTest$2796();
        if ($$getterTest$2796===undefined)$$getterTest$2796=new GetterTest$2796.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        $$getterTest$2796.i$2797_=(0);
        return $$getterTest$2796;
    }
    function $init$GetterTest$2796(){
        if (GetterTest$2796.$$===undefined){
            $$$cl2381.initTypeProto(GetterTest$2796,'functions::testGetterMethodDefinitions.GetterTest',$$$cl2381.Basic);
            (function($$getterTest$2796){
                
                //AttributeDeclaration i at functions.ceylon (107:4-107:24)
                $$$cl2381.defineAttr($$getterTest$2796,'i$2797',function(){return this.i$2797_;},function(i$2798){return this.i$2797_=i$2798;});
                
                //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
                $$$cl2381.defineAttr($$getterTest$2796,'x',function(){
                    var $$getterTest$2796=this;
                    return ($$getterTest$2796.i$2797=$$getterTest$2796.i$2797.successor);
                });
            })(GetterTest$2796.$$.prototype);
        }
        return GetterTest$2796;
    }
    $init$GetterTest$2796();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$2799=GetterTest$2796();
    $$$c2382.check(gt$2799.x.equals((1)),$$$cl2381.String("getter defined as method 1",26));
    $$$c2382.check(gt$2799.x.equals((2)),$$$cl2381.String("getter defined as method 2",26));
    $$$c2382.check(gt$2799.x.equals((3)),$$$cl2381.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$2800,y$2801,z$2802){
    if(x$2800===undefined){x$2800=$$$cl2381.String("x",1);}
    if(y$2801===undefined){y$2801=x$2800.plus($$$cl2381.String("y",1));}
    if(z$2802===undefined){z$2802=$$$cl2381.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$2803=x$2800.plus($$$cl2381.String(",",1)).plus(y$2801);
    var setResult$2803=function(result$2804){return result$2803=result$2804;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$2805 = z$2802.iterator();
    var s$2806;while ((s$2806=it$2805.next())!==$$$cl2381.getFinished()){
        (result$2803=result$2803.plus($$$cl2381.String(",",1).plus(s$2806)));
    }
    return result$2803;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$2807, more$2808, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$2808===undefined){more$2808=$$$cl2381.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    $$issue105.i$2809_=i$2807;
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl2381.initTypeProto(Issue105,'functions::Issue105',$$$cl2381.Basic);
        (function($$issue105){
            
            //AttributeDeclaration i at functions.ceylon (123:4-123:20)
            $$$cl2381.defineAttr($$issue105,'i',function(){return this.i$2809_;});
        })(Issue105.$$.prototype);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2382.check((namedArgFunc(undefined,undefined,$$$cl2381.getEmpty())).equals($$$cl2381.String("x,xy",4)),$$$cl2381.String("named arguments 1",17));
    $$$c2382.check((x$2810=$$$cl2381.String("a",1),namedArgFunc(x$2810,undefined,$$$cl2381.getEmpty())).equals($$$cl2381.String("a,ay",4)),$$$cl2381.String("named arguments 2",17));
    var x$2810;
    $$$c2382.check((y$2811=$$$cl2381.String("b",1),namedArgFunc(undefined,y$2811,$$$cl2381.getEmpty())).equals($$$cl2381.String("x,b",3)),$$$cl2381.String("named arguments 3",17));
    var y$2811;
    $$$c2382.check((z$2812=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),namedArgFunc(undefined,undefined,z$2812)).equals($$$cl2381.String("x,xy,c",6)),$$$cl2381.String("named arguments 4",17));
    var z$2812;
    $$$c2382.check((x$2813=$$$cl2381.String("a",1),y$2814=$$$cl2381.String("b",1),z$2815=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),namedArgFunc(x$2813,y$2814,z$2815)).equals($$$cl2381.String("a,b,c",5)),$$$cl2381.String("named arguments 5",17));
    var x$2813,y$2814,z$2815;
    $$$c2382.check((y$2816=$$$cl2381.String("b",1),x$2817=$$$cl2381.String("a",1),z$2818=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.Tuple($$$cl2381.String("d",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),namedArgFunc(x$2817,y$2816,z$2818)).equals($$$cl2381.String("a,b,c,d",7)),$$$cl2381.String("named arguments 6",17));
    var y$2816,x$2817,z$2818;
    $$$c2382.check((x$2819=$$$cl2381.String("a",1),z$2820=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),namedArgFunc(x$2819,undefined,z$2820)).equals($$$cl2381.String("a,ay,c",6)),$$$cl2381.String("named arguments 7",17));
    var x$2819,z$2820;
    $$$c2382.check((y$2821=$$$cl2381.String("b",1),z$2822=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),namedArgFunc(undefined,y$2821,z$2822)).equals($$$cl2381.String("x,b,c",5)),$$$cl2381.String("named arguments 8",17));
    var y$2821,z$2822;
    $$$c2382.check((y$2823=$$$cl2381.String("b",1),x$2824=$$$cl2381.String("a",1),namedArgFunc(x$2824,y$2823,$$$cl2381.getEmpty())).equals($$$cl2381.String("a,b",3)),$$$cl2381.String("named arguments 9",17));
    var y$2823,x$2824;
    $$$c2382.check((z$2825=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.Tuple($$$cl2381.String("d",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),namedArgFunc(undefined,undefined,z$2825)).equals($$$cl2381.String("x,xy,c,d",8)),$$$cl2381.String("named arguments 11",18));
    var z$2825;
    $$$c2382.check((y$2826=$$$cl2381.String("b",1),z$2827=$$$cl2381.Tuple($$$cl2381.String("c",1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),x$2828=$$$cl2381.String("a",1),namedArgFunc(x$2828,y$2826,z$2827)).equals($$$cl2381.String("a,b,c",5)),$$$cl2381.String("named arguments 12",18));
    var y$2826,z$2827,x$2828;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$2829=(i$2830=(1),more$2831=$$$cl2381.Tuple((i$2832=(2),Issue105(i$2832,$$$cl2381.getEmpty())),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$2830,more$2831));
    var i$2830,more$2831,i$2832;
    $$$c2382.check(issue105$2829.i.equals((1)),$$$cl2381.String("issue #105",10));
};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl2381.initTypeProto(LazyExprBase,'functions::LazyExprBase');
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
    $$lazyExprTest.x$2833_=(1000);
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl2381.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl2381.Basic,$init$LazyExprBase());
        (function($$lazyExprTest){
            
            //AttributeDeclaration x at functions.ceylon (149:4-149:36)
            $$$cl2381.defineAttr($$lazyExprTest,'x',function(){return this.x$2833_;},function(x$2834){return this.x$2833_=x$2834;});
            
            //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
            $$lazyExprTest.f1=function (i$2835,f$2836){
                var $$lazyExprTest=this;
                if(f$2836===undefined){f$2836=function (){
                    return $$$cl2381.StringBuilder().appendAll([i$2835.string,$$$cl2381.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor).string]).string;
                };}
                return $$$cl2381.StringBuilder().appendAll([i$2835.string,$$$cl2381.String(":",1),f$2836().string]).string;
            };
            
            //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
            $$lazyExprTest.f2=function (i$2837){
                var $$lazyExprTest=this;
                return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$2837);
            };
            
            //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
            $$$cl2381.defineAttr($$lazyExprTest,'i1',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor);
            });
            
            //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
            $$$cl2381.defineAttr($$lazyExprTest,'i2',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor).times((2));
            });
            $$$cl2381.defineAttr($$lazyExprTest,'s1',function(){
                var $$lazyExprTest=this;
                return $$$cl2381.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl2381.String(".1",2)]).string;
            });
            $$lazyExprTest.s2=function (i$2838){
                var $$lazyExprTest=this;
                return $$$cl2381.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl2381.String(".",1),i$2838.string]).string;
            };
            //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
            $$lazyExprTest.f3=function (f$2839){
                var $$lazyExprTest=this;
                return f$2839(($$lazyExprTest.x=$$lazyExprTest.x.successor));
            };
        })(LazyExprTest.$$.prototype);
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$2840=(1000);
var getLx=function(){return lx$2840;};
exports.getLx=getLx;
var setLx=function(lx$2841){return lx$2840=lx$2841;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$2842,f$2843){
    if(f$2843===undefined){f$2843=function (){
        return $$$cl2381.StringBuilder().appendAll([i$2842.string,$$$cl2381.String(".",1),(setLx(getLx().successor)).string]).string;
    };}
    return f$2843();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$2844){
    return (2).times((setLx(getLx().successor))).plus(i$2844);
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
    $$lazyExprTest2.x$2845_=(1000);
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl2381.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl2381.Basic,$init$LazyExprBase());
        (function($$lazyExprTest2){
            
            //AttributeDeclaration x at functions.ceylon (167:4-167:35)
            $$$cl2381.defineAttr($$lazyExprTest2,'x',function(){return this.x$2845_;},function(x$2846){return this.x$2845_=x$2846;});
            
            //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
            $$$cl2381.defineAttr($$lazyExprTest2,'s1',function(){
                var $$lazyExprTest2=this;
                return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;
            });
            
            //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
            $$lazyExprTest2.s2=function (i$2847){
                var $$lazyExprTest2=this;
                return $$$cl2381.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl2381.String("-",1),i$2847.string]).string;
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
    $$lazyExprTest3.s1$2848_=$$$cl2381.String("s1",2);
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl2381.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
        (function($$lazyExprTest3){
            
            //AttributeDeclaration s1 at functions.ceylon (172:4-172:43)
            $$$cl2381.defineAttr($$lazyExprTest3,'s1',function(){return this.s1$2848_;},function(s1$2849){return this.s1$2848_=s1$2849;});
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
    $$lazyExprTest4.assigned$2850_=$$$cl2381.String("",0);
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl2381.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
        (function($$lazyExprTest4){
            
            //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
            $$$cl2381.defineAttr($$lazyExprTest4,'assigned',function(){return this.assigned$2850_;},function(assigned$2851){return this.assigned$2850_=assigned$2851;});
            
            //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
            $$$cl2381.defineAttr($$lazyExprTest4,'s1',function(){
                var $$lazyExprTest4=this;
                return $$$cl2381.StringBuilder().appendAll([$$$cl2381.String("s1-",3),$$$cl2381.attrGetter($$lazyExprTest4.getT$all()['functions::LazyExprTest2'],'s1').call(this).string]).string;
            },function(s1$2852){
                var $$lazyExprTest4=this;
                $$lazyExprTest4.assigned=s1$2852;
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
    var tst$2853=LazyExprTest();
    (tst$2853.x=(1));
    $$$c2382.check(tst$2853.f1((3)).equals($$$cl2381.String("3:3.2",5)),$$$cl2381.String("=> defaulted param",18));
    $$$c2382.check(tst$2853.f2((3)).equals((9)),$$$cl2381.String("=> method",9));
    $$$c2382.check(tst$2853.i1.equals((4)),$$$cl2381.String("=> attribute",12));
    $$$c2382.check(tst$2853.i2.equals((10)),$$$cl2381.String("=> attribute specifier",22));
    $$$c2382.check(tst$2853.s1.equals($$$cl2381.String("6.1",3)),$$$cl2381.String("=> attribute refinement",23));
    $$$c2382.check(tst$2853.s2((5)).equals($$$cl2381.String("7.5",3)),$$$cl2381.String("=> method refinement",20));
    setLx((1));
    $$$c2382.check(lazy_f1((3)).equals($$$cl2381.String("3.2",3)),$$$cl2381.String("=> defaulted param toplevel",27));
    $$$c2382.check(lazy_f2((3)).equals((9)),$$$cl2381.String("=> method toplevel",18));
    $$$c2382.check(getLazy_i1().equals((4)),$$$cl2381.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$2854=(1000);
    var setX$2854=function(x$2855){return x$2854=x$2855;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$2856=function (i$2857,f$2858){
        if(f$2858===undefined){f$2858=function (){
            return $$$cl2381.StringBuilder().appendAll([i$2857.string,$$$cl2381.String(".",1),(x$2854=x$2854.successor).string]).string;
        };}
        return f$2858();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$2859=function (i$2860){
        return (2).times((x$2854=x$2854.successor)).plus(i$2860);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$2861=function(){return (x$2854=x$2854.successor);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$2862;
    var getI2$2862=function(){
        return (x$2854=x$2854.successor).times((2));
    };
    x$2854=(1);
    $$$c2382.check(f1$2856((3)).equals($$$cl2381.String("3.2",3)),$$$cl2381.String("=> defaulted param local",24));
    $$$c2382.check(f2$2859((3)).equals((9)),$$$cl2381.String("=> method local",15));
    $$$c2382.check(getI1$2861().equals((4)),$$$cl2381.String("=> attribute local",18));
    $$$c2382.check(getI2$2862().equals((10)),$$$cl2381.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$2863=LazyExprTest3();
    (tst3$2863.x=(1));
    $$$c2382.check(tst3$2863.s1.equals($$$cl2381.String("s1",2)),$$$cl2381.String("=> override variable 1",22));
    (tst3$2863.s1=$$$cl2381.String("abc",3));
    $$$c2382.check(tst3$2863.s1.equals($$$cl2381.String("abc",3)),$$$cl2381.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$2864=LazyExprTest4();
    (tst4$2864.x=(1));
    $$$c2382.check(tst4$2864.s1.equals($$$cl2381.String("s1-2",4)),$$$cl2381.String("=> override getter/setter 1",27));
    (tmp$2865=tst4$2864,tmp$2865.s1=$$$cl2381.String("abc",3),tmp$2865.s1);
    var tmp$2865;
    $$$c2382.check(tst4$2864.s1.equals($$$cl2381.String("s1-4",4)),$$$cl2381.String("=> override getter/setter 2",27));
    $$$c2382.check(tst4$2864.assigned.equals($$$cl2381.String("abc",3)),$$$cl2381.String("=> override getter/setter 3",27));
    (tst$2853.x=(1));
    x$2854=(10);
    $$$c2382.check((i$2866=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$2854=x$2854.successor);
    }()),(opt$2867=tst$2853,$$$cl2381.JsCallable(opt$2867,opt$2867!==null?opt$2867.f1:null))(i$2866,undefined)).equals($$$cl2381.String("11:11.2",7)),$$$cl2381.String("=> named arg",12));
    var i$2866,opt$2867;
    $$$c2382.check((i$2868=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$2854=x$2854.successor);
    }()),f$2869=function (){return (x$2854=x$2854.successor).string;},(opt$2870=tst$2853,$$$cl2381.JsCallable(opt$2870,opt$2870!==null?opt$2870.f1:null))(i$2868,f$2869)).equals($$$cl2381.String("12:13",5)),$$$cl2381.String("=> named arg function",21));
    var i$2868,f$2869,opt$2870;
    $$$c2382.check((f$2871=function (i$2872){return $$$cl2381.StringBuilder().appendAll([i$2872.string,$$$cl2381.String("-",1),(x$2854=x$2854.successor).string]).string;},(opt$2873=tst$2853,$$$cl2381.JsCallable(opt$2873,opt$2873!==null?opt$2873.f3:null))(f$2871)).equals($$$cl2381.String("3-14",4)),$$$cl2381.String("=> named arg function with param",32));
    var f$2871,opt$2873;
};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl2381.String("test",4));
    helloAll([$$$cl2381.String("Gavin",5),$$$cl2381.String("Enrique",7),$$$cl2381.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}));
    $$$c2382.check(toString((5)).equals($$$cl2381.String("5",1)),$$$cl2381.String("toString(obj)",13));
    $$$c2382.check(add($$$cl2381.Float(1.5),$$$cl2381.Float(2.5)).equals($$$cl2381.Float(4.0)),$$$cl2381.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    $$$c2382.results();
}
exports.test=test;

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$2874,y$2875){
        return x$2874.compare(y$2875);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$2876){
    return function(apat$2877){
        return function(amat$2878){
            return $$$cl2381.StringBuilder().appendAll([nombre$2876.string,$$$cl2381.String(" ",1),apat$2877.string,$$$cl2381.String(" ",1),amat$2878.string]).string;
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$2879){
    if(name$2879===undefined){name$2879=$$$cl2381.String("A",1);}
    return function(apat$2880){
        return function(amat$2881){
            return $$$cl2381.StringBuilder().appendAll([name$2879.string,$$$cl2381.String(" ",1),apat$2880.string,$$$cl2381.String(" ",1),amat$2881.string]).string;
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$2882){
    if(names$2882===undefined){names$2882=$$$cl2381.getEmpty();}
    return function(count$2883){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$2884=$$$cl2381.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$2885 = names$2882.iterator();
        var name$2886;while ((name$2886=it$2885.next())!==$$$cl2381.getFinished()){
            sb$2884.append(name$2886).append($$$cl2381.String(" ",1));
        }
        sb$2884.append($$$cl2381.String("count ",6)).append(count$2883.string);
        return sb$2884.string;
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl2381.print($$$cl2381.String("Testing multiple parameter lists...",35));
    $$$c2382.check(multiCompare()((1),(1)).equals($$$cl2381.getEqual()),$$$cl2381.String("Multi compare 1",15));
    $$$c2382.check(multiCompare()((1),(2)).equals($$$cl2381.getSmaller()),$$$cl2381.String("Multi compare 2",15));
    $$$c2382.check(multiCompare()((2),(1)).equals($$$cl2381.getLarger()),$$$cl2381.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$2887=function (a$2888,b$2889){
        return multiCompare()(a$2888,b$2889);
    };
    $$$c2382.check(comp$2887((1),(1)).equals($$$cl2381.getEqual()),$$$cl2381.String("Multi compare 4",15));
    $$$c2382.check(comp$2887((1),(2)).equals($$$cl2381.getSmaller()),$$$cl2381.String("Multi compare 5",15));
    $$$c2382.check(comp$2887((2),(1)).equals($$$cl2381.getLarger()),$$$cl2381.String("Multi compare 6",15));
    $$$c2382.check(multiFullname($$$cl2381.String("a",1))($$$cl2381.String("b",1).valueOf())($$$cl2381.String("c",1).valueOf()).equals($$$cl2381.String("a b c",5)),$$$cl2381.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$2890=function (c$2891){
        return multiFullname($$$cl2381.String("A",1))($$$cl2381.String("B",1).valueOf())(c$2891.valueOf());
    };
    $$$c2382.check(apat$2890($$$cl2381.String("C",1)).equals($$$cl2381.String("A B C",5)),$$$cl2381.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$2892=function (name$2893){
        return multiFullname($$$cl2381.String("Name",4))(name$2893.valueOf());
    };
    $$$c2382.check(nombre$2892($$$cl2381.String("Z",1))($$$cl2381.String("L",1).valueOf()).equals($$$cl2381.String("Name Z L",8)),$$$cl2381.String("Multi callable 2",16));
    $$$c2382.check(multiDefaulted()($$$cl2381.String("B",1).valueOf())($$$cl2381.String("C",1).valueOf()).equals($$$cl2381.String("A B C",5)),$$$cl2381.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$2894=$$$cl2381.$JsCallable(multiDefaulted(),{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},Return:{t:$$$cl2381.Callable,a:{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},Return:{t:$$$cl2381.String}}}});
    $$$c2382.check(md1$2894($$$cl2381.String("B",1).valueOf())($$$cl2381.String("C",1).valueOf()).equals($$$cl2381.String("A B C",5)),$$$cl2381.String("Multi defaulted 2",17));
    $$$c2382.check(md1$2894($$$cl2381.String("B",1).valueOf())($$$cl2381.String("Z",1).valueOf()).equals($$$cl2381.String("A B Z",5)),$$$cl2381.String("Multi defaulted 3",17));
    $$$c2382.check(md1$2894($$$cl2381.String("Z",1).valueOf())($$$cl2381.String("C",1).valueOf()).equals($$$cl2381.String("A Z C",5)),$$$cl2381.String("Multi defaulted 4",17));
    $$$c2382.check(md1$2894($$$cl2381.String("Y",1).valueOf())($$$cl2381.String("Z",1).valueOf()).equals($$$cl2381.String("A Y Z",5)),$$$cl2381.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$2895=function (x$2896){
        return multiDefaulted()($$$cl2381.String("B",1).valueOf())(x$2896.valueOf());
    };
    $$$c2382.check(md2$2895($$$cl2381.String("C",1)).equals($$$cl2381.String("A B C",5)),$$$cl2381.String("Multi defaulted 6",17));
    $$$c2382.check(md2$2895($$$cl2381.String("Z",1)).equals($$$cl2381.String("A B Z",5)),$$$cl2381.String("Multi defaulted 7",17));
    $$$c2382.check(multiSequenced([$$$cl2381.String("A",1),$$$cl2381.String("B",1),$$$cl2381.String("C",1)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}))((1)).equals($$$cl2381.String("A B C count 1",13)),$$$cl2381.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$2897=function (c$2898){
        return multiSequenced([$$$cl2381.String("x",1),$$$cl2381.String("y",1),$$$cl2381.String("z",1)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}))(c$2898);
    };
    $$$c2382.check(ms1$2897((5)).equals($$$cl2381.String("x y z count 5",13)),$$$cl2381.String("Multi sequenced 2",17));
    $$$c2382.check(ms1$2897((10)).equals($$$cl2381.String("x y z count 10",14)),$$$cl2381.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
