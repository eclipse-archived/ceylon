(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"last":{"$t":{"$nm":"Element"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl2309=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2310=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$2608,f$2609,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$2610 = a$2608.iterator;
    var e$2611;while ((e$2611=it$2610.next())!==$$$cl2309.getFinished()){
        if(f$2609(e$2611)){
            return e$2611;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$2612,f$2613,$$$mptypes){
    if(f$2613===undefined){f$2613=function (x$2614){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$2615 = a$2612.iterator;
    var e$2616;while ((e$2616=it$2615.next())!==$$$cl2309.getFinished()){
        if(f$2613(e$2616)){
            return e$2616;
        }
    }
    if ($$$cl2309.getFinished() === e$2616){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$2617){
    return function (i$2618){
        return i$2618.minus(howMuch$2617).string;
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl2309.print($$$cl2309.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$2619=(elements$2620=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),$$$cl2309.array(elements$2620,{Element:{t:$$$cl2309.Integer}}));
    var elements$2620;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$2621=find(nums$2619,function (i$2622){
        return i$2622.remainder((2)).equals((0));
    },{Element:{t:$$$cl2309.Integer}});
    var setFound$2621=function(found$2623){return found$2621=found$2623;};
    var i$2624;
    if((i$2624=found$2621)!==null){
        $$$c2310.check(i$2624.equals((2)),$$$cl2309.String("anonfunc positional",19));
    }else {
        $$$c2310.fail($$$cl2309.String("anonfunc positional",19));
    }
    found$2621=(f$2625=function (i$2626){
        return i$2626.remainder((2)).equals((0));
    },a$2627=nums$2619,find(a$2627,f$2625,{Element:{t:$$$cl2309.Integer}}));
    var f$2625,a$2627;
    var i$2628;
    if((i$2628=found$2621)!==null){
        $$$c2310.check(i$2628.equals((2)),$$$cl2309.String("anonfunc named",14));
    }else {
        $$$c2310.fail($$$cl2309.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$2629(f$2630,expect$2631){
        $$$c2310.check(f$2630((0)).equals(expect$2631),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("anon func returns ",18),f$2630((0)).string,$$$cl2309.String(" instead of ",12),expect$2631.string]).string);
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$2632(i$2633){
        return i$2633.plus((12)).string;
    };
    callFunction$2629(f$2632,$$$cl2309.String("12",2));
    callFunction$2629(function (i$2634){
        return i$2634.times((3)).string;
    },$$$cl2309.String("0",1));
    (expect$2635=$$$cl2309.String("0",1),f$2636=function (i$2637){
        return i$2637.power((2)).string;
    },callFunction$2629(f$2636,expect$2635));
    var expect$2635,f$2636;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$2638=function (i$2639){
        return i$2639.minus((10)).string;
    };
    callFunction$2629(f2$2638,$$$cl2309.String("-10",3));
    callFunction$2629(subtract((5)),$$$cl2309.String("-5",2));
    found$2621=find2(nums$2619,function (i$2640){
        return i$2640.compare((2)).equals($$$cl2309.getLarger());
    },{Element:{t:$$$cl2309.Integer}});
    var i$2641;
    if((i$2641=found$2621)!==null){
        $$$c2310.check(i$2641.equals((3)),$$$cl2309.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2310.fail($$$cl2309.String("anonfunc i>2 [2]",16));
    }
    found$2621=find2(nums$2619,undefined,{Element:{t:$$$cl2309.Integer}});
    var i$2642;
    if((i$2642=found$2621)!==null){
        $$$c2310.check(i$2642.equals((1)),$$$cl2309.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2310.fail($$$cl2309.String("anonfunc defaulted param [2]",28));
    }
};

//MethodDefinition helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl2309.print($$$cl2309.String("hello world",11));
}
exports.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (7:0-9:0)
function hello(name$2643){
    $$$cl2309.print($$$cl2309.String("hello",5).plus(name$2643));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$2644){
    if(names$2644===undefined){names$2644=$$$cl2309.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$2645){
    return obj$2645.string;
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$2646,y$2647){
    return x$2646.plus(y$2647);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$2648,f$2649){
    f$2649((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$2650, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl2309.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$2650=seq$2650;
    $$$cl2309.Sequence($$mySequence);
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    
    //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    return $$mySequence;
}
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl2309.initTypeProto(MySequence,'functions::MySequence',$$$cl2309.Basic,$$$cl2309.Sequence);
        (function($$mySequence){
            
            //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
            $$$cl2309.defineAttr($$mySequence,'lastIndex',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.lastIndex;
            });
            //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
            $$$cl2309.defineAttr($$mySequence,'first',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.first;
            });
            //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
            $$$cl2309.defineAttr($$mySequence,'rest',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.rest;
            });
            //MethodDefinition get at functions.ceylon (30:4-30:67)
            $$mySequence.get=function get(index$2651){
                var $$mySequence=this;
                return $$mySequence.seq$2650.get(index$2651);
            };
            //MethodDefinition span at functions.ceylon (31:4-31:88)
            $$mySequence.span=function span(from$2652,to$2653){
                var $$mySequence=this;
                return $$mySequence.seq$2650.span(from$2652,to$2653);
            };
            //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
            $$mySequence.spanFrom=function spanFrom(from$2654){
                var $$mySequence=this;
                return $$mySequence.seq$2650.spanFrom(from$2654);
            };
            //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
            $$mySequence.spanTo=function spanTo(to$2655){
                var $$mySequence=this;
                return $$mySequence.seq$2650.spanTo(to$2655);
            };
            //MethodDefinition segment at functions.ceylon (34:4-34:102)
            $$mySequence.segment=function segment(from$2656,length$2657){
                var $$mySequence=this;
                return $$mySequence.seq$2650.segment(from$2656,length$2657);
            };
            //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
            $$$cl2309.defineAttr($$mySequence,'clone',function(){
                var $$mySequence=this;
                return $$mySequence;
            });
            //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
            $$$cl2309.defineAttr($$mySequence,'string',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.string;
            });
            //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
            $$$cl2309.defineAttr($$mySequence,'hash',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.hash;
            });
            //MethodDefinition equals at functions.ceylon (38:4-38:75)
            $$mySequence.equals=function equals(other$2658){
                var $$mySequence=this;
                return $$mySequence.seq$2650.equals(other$2658);
            };
            //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
            $$$cl2309.defineAttr($$mySequence,'reversed',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.reversed;
            });
            //AttributeDeclaration last at functions.ceylon (40:4-40:42)
            $$$cl2309.defineAttr($$mySequence,'last',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.last;
            });
            
            //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
            $$$cl2309.defineAttr($$mySequence,'iterator',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.iterator;
            });
            
            //AttributeDeclaration size at functions.ceylon (42:4-42:42)
            $$$cl2309.defineAttr($$mySequence,'size',function(){
                var $$mySequence=this;
                return $$mySequence.seq$2650.size;
            });
            
            //MethodDeclaration contains at functions.ceylon (43:4-43:71)
            $$mySequence.contains=function (other$2659){
                var $$mySequence=this;
                return $$mySequence.seq$2650.contains(other$2659);
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
        $$$cl2309.initTypeProto(RefHelper,'functions::RefHelper',$$$cl2309.Basic);
        (function($$refHelper){
            
            //MethodDefinition f at functions.ceylon (47:4-47:47)
            $$refHelper.f=function f(i$2660){
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
    var obj1$2661=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$2662=MySequence($$$cl2309.Tuple($$$cl2309.String("hi",2),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Element:{t:$$$cl2309.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$2663(x$2664){
        return x$2664((0));
    };
    $$$c2310.check(tst$2663((opt$2665=obj1$2661,$$$cl2309.JsCallable(opt$2665,opt$2665!==null?opt$2665.f:null))),$$$cl2309.String("Reference to method",19));
    var opt$2665;
    $$$c2310.check(tst$2663((opt$2666=obj2$2662,$$$cl2309.JsCallable(opt$2666,opt$2666!==null?opt$2666.defines:null))),$$$cl2309.String("Reference to method from ceylon.language",40));
    var opt$2666;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$2667,i2$2668,i3$2669){
    if(i2$2668===undefined){i2$2668=i1$2667.plus((1));}
    if(i3$2669===undefined){i3$2669=i1$2667.plus(i2$2668);}
    return $$$cl2309.StringBuilder().appendAll([i1$2667.string,$$$cl2309.String(",",1),i2$2668.string,$$$cl2309.String(",",1),i3$2669.string]).string;
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$2670, i2$2671, i3$2672, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$2671===undefined){i2$2671=i1$2670.plus((1));}
    if(i3$2672===undefined){i3$2672=i1$2670.plus(i2$2671);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    $$defParamTest1.s$2673_=$$$cl2309.StringBuilder().appendAll([i1$2670.string,$$$cl2309.String(",",1),i2$2671.string,$$$cl2309.String(",",1),i3$2672.string]).string;
    return $$defParamTest1;
}
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl2309.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl2309.Basic);
        (function($$defParamTest1){
            
            //AttributeDeclaration s at functions.ceylon (64:4-64:44)
            $$$cl2309.defineAttr($$defParamTest1,'s',function(){return this.s$2673_;});
        })(DefParamTest1.$$.prototype);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDefinition DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$2674, i2$2675, i3$2676, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$2674=i1$2674;
    if(i2$2675===undefined){i2$2675=$$defParamTest2.i1$2674.plus((1));}
    $$defParamTest2.i2$2675=i2$2675;
    if(i3$2676===undefined){i3$2676=$$defParamTest2.i1$2674.plus($$defParamTest2.i2$2675);}
    $$defParamTest2.i3$2676=i3$2676;
    return $$defParamTest2;
}
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl2309.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl2309.Basic);
        (function($$defParamTest2){
            
            //MethodDefinition f at functions.ceylon (67:4-67:55)
            $$defParamTest2.f=function f(){
                var $$defParamTest2=this;
                return $$$cl2309.StringBuilder().appendAll([$$defParamTest2.i1$2674.string,$$$cl2309.String(",",1),$$defParamTest2.i2$2675.string,$$$cl2309.String(",",1),$$defParamTest2.i3$2676.string]).string;
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
        $$$cl2309.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl2309.Basic);
        (function($$defParamTest3){
            
            //MethodDefinition f at functions.ceylon (70:4-72:4)
            $$defParamTest3.f=function f(i1$2677,i2$2678,i3$2679){
                var $$defParamTest3=this;
                if(i2$2678===undefined){i2$2678=i1$2677.plus((1));}
                if(i3$2679===undefined){i3$2679=i1$2677.plus(i2$2678);}
                return $$$cl2309.StringBuilder().appendAll([i1$2677.string,$$$cl2309.String(",",1),i2$2678.string,$$$cl2309.String(",",1),i3$2679.string]).string;
            };
        })(DefParamTest3.$$.prototype);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDefinition testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2310.check(defParamTest((1)).equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted parameters 1",22));
    $$$c2310.check(defParamTest((1),(3)).equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted parameters 2",22));
    $$$c2310.check(defParamTest((1),(3),(0)).equals($$$cl2309.String("1,3,0",5)),$$$cl2309.String("defaulted parameters 3",22));
    $$$c2310.check((i1$2680=(1),defParamTest(i1$2680,undefined,undefined)).equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted parameters named 1",28));
    var i1$2680;
    $$$c2310.check((i1$2681=(1),i2$2682=(3),defParamTest(i1$2681,i2$2682,undefined)).equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted parameters named 2",28));
    var i1$2681,i2$2682;
    $$$c2310.check((i1$2683=(1),i3$2684=(0),defParamTest(i1$2683,undefined,i3$2684)).equals($$$cl2309.String("1,2,0",5)),$$$cl2309.String("defaulted parameters named 3",28));
    var i1$2683,i3$2684;
    $$$c2310.check(DefParamTest1((1)).s.equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted parameters class 1",28));
    $$$c2310.check(DefParamTest1((1),(3)).s.equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted parameters class 2",28));
    $$$c2310.check(DefParamTest1((1),(3),(0)).s.equals($$$cl2309.String("1,3,0",5)),$$$cl2309.String("defaulted parameters class 3",28));
    $$$c2310.check((i1$2685=(1),DefParamTest1(i1$2685,undefined,undefined)).s.equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted parameters class named 1",34));
    var i1$2685;
    $$$c2310.check((i1$2686=(1),i2$2687=(3),DefParamTest1(i1$2686,i2$2687,undefined)).s.equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted parameters class named 2",34));
    var i1$2686,i2$2687;
    $$$c2310.check((i1$2688=(1),i3$2689=(0),DefParamTest1(i1$2688,undefined,i3$2689)).s.equals($$$cl2309.String("1,2,0",5)),$$$cl2309.String("defaulted parameters class named 3",34));
    var i1$2688,i3$2689;
    $$$c2310.check(DefParamTest2((1)).f().equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted parameters class2 1",29));
    $$$c2310.check(DefParamTest2((1),(3)).f().equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted parameters class2 2",29));
    $$$c2310.check(DefParamTest2((1),(3),(0)).f().equals($$$cl2309.String("1,3,0",5)),$$$cl2309.String("defaulted parameters class2 3",29));
    $$$c2310.check((i1$2690=(1),DefParamTest2(i1$2690,undefined,undefined)).f().equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted parameters class2 named 1",35));
    var i1$2690;
    $$$c2310.check((i1$2691=(1),i2$2692=(3),DefParamTest2(i1$2691,i2$2692,undefined)).f().equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted parameters class2 named 2",35));
    var i1$2691,i2$2692;
    $$$c2310.check((i1$2693=(1),i3$2694=(0),DefParamTest2(i1$2693,undefined,i3$2694)).f().equals($$$cl2309.String("1,2,0",5)),$$$cl2309.String("defaulted parameters class2 named 3",35));
    var i1$2693,i3$2694;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$2695=DefParamTest3();
    $$$c2310.check(tst$2695.f((1)).equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted method parameters 1",29));
    $$$c2310.check(tst$2695.f((1),(3)).equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted method parameters 2",29));
    $$$c2310.check(tst$2695.f((1),(3),(0)).equals($$$cl2309.String("1,3,0",5)),$$$cl2309.String("defaulted method parameters 3",29));
    $$$c2310.check((i1$2696=(1),(opt$2697=tst$2695,$$$cl2309.JsCallable(opt$2697,opt$2697!==null?opt$2697.f:null))(i1$2696,undefined,undefined)).equals($$$cl2309.String("1,2,3",5)),$$$cl2309.String("defaulted method parameters named 1",35));
    var i1$2696,opt$2697;
    $$$c2310.check((i1$2698=(1),i2$2699=(3),(opt$2700=tst$2695,$$$cl2309.JsCallable(opt$2700,opt$2700!==null?opt$2700.f:null))(i1$2698,i2$2699,undefined)).equals($$$cl2309.String("1,3,4",5)),$$$cl2309.String("defaulted method parameters named 2",35));
    var i1$2698,i2$2699,opt$2700;
    $$$c2310.check((i1$2701=(1),i3$2702=(0),(opt$2703=tst$2695,$$$cl2309.JsCallable(opt$2703,opt$2703!==null?opt$2703.f:null))(i1$2701,undefined,i3$2702)).equals($$$cl2309.String("1,2,0",5)),$$$cl2309.String("defaulted method parameters named 3",35));
    var i1$2701,i3$2702,opt$2703;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$2704($$getterTest$2704){
        $init$GetterTest$2704();
        if ($$getterTest$2704===undefined)$$getterTest$2704=new GetterTest$2704.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        $$getterTest$2704.i$2705_=(0);
        return $$getterTest$2704;
    }
    function $init$GetterTest$2704(){
        if (GetterTest$2704.$$===undefined){
            $$$cl2309.initTypeProto(GetterTest$2704,'functions::testGetterMethodDefinitions.GetterTest',$$$cl2309.Basic);
            (function($$getterTest$2704){
                
                //AttributeDeclaration i at functions.ceylon (107:4-107:24)
                $$$cl2309.defineAttr($$getterTest$2704,'i$2705',function(){return this.i$2705_;},function(i$2706){return this.i$2705_=i$2706;});
                
                //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
                $$$cl2309.defineAttr($$getterTest$2704,'x',function(){
                    var $$getterTest$2704=this;
                    return ($$getterTest$2704.i$2705=$$getterTest$2704.i$2705.successor,$$getterTest$2704.i$2705);
                });
            })(GetterTest$2704.$$.prototype);
        }
        return GetterTest$2704;
    }
    $init$GetterTest$2704();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$2707=GetterTest$2704();
    $$$c2310.check(gt$2707.x.equals((1)),$$$cl2309.String("getter defined as method 1",26));
    $$$c2310.check(gt$2707.x.equals((2)),$$$cl2309.String("getter defined as method 2",26));
    $$$c2310.check(gt$2707.x.equals((3)),$$$cl2309.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$2708,y$2709,z$2710){
    if(x$2708===undefined){x$2708=$$$cl2309.String("x",1);}
    if(y$2709===undefined){y$2709=x$2708.plus($$$cl2309.String("y",1));}
    if(z$2710===undefined){z$2710=$$$cl2309.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$2711=x$2708.plus($$$cl2309.String(",",1)).plus(y$2709);
    var setResult$2711=function(result$2712){return result$2711=result$2712;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$2713 = z$2710.iterator;
    var s$2714;while ((s$2714=it$2713.next())!==$$$cl2309.getFinished()){
        (result$2711=result$2711.plus($$$cl2309.String(",",1).plus(s$2714)),result$2711);
    }
    return result$2711;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$2715, more$2716, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$2716===undefined){more$2716=$$$cl2309.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    $$issue105.i$2717_=i$2715;
    return $$issue105;
}
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl2309.initTypeProto(Issue105,'functions::Issue105',$$$cl2309.Basic);
        (function($$issue105){
            
            //AttributeDeclaration i at functions.ceylon (123:4-123:20)
            $$$cl2309.defineAttr($$issue105,'i',function(){return this.i$2717_;});
        })(Issue105.$$.prototype);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDefinition testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2310.check((namedArgFunc(undefined,undefined,$$$cl2309.getEmpty())).equals($$$cl2309.String("x,xy",4)),$$$cl2309.String("named arguments 1",17));
    $$$c2310.check((x$2718=$$$cl2309.String("a",1),namedArgFunc(x$2718,undefined,$$$cl2309.getEmpty())).equals($$$cl2309.String("a,ay",4)),$$$cl2309.String("named arguments 2",17));
    var x$2718;
    $$$c2310.check((y$2719=$$$cl2309.String("b",1),namedArgFunc(undefined,y$2719,$$$cl2309.getEmpty())).equals($$$cl2309.String("x,b",3)),$$$cl2309.String("named arguments 3",17));
    var y$2719;
    $$$c2310.check((z$2720=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),namedArgFunc(undefined,undefined,z$2720)).equals($$$cl2309.String("x,xy,c",6)),$$$cl2309.String("named arguments 4",17));
    var z$2720;
    $$$c2310.check((x$2721=$$$cl2309.String("a",1),y$2722=$$$cl2309.String("b",1),z$2723=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),namedArgFunc(x$2721,y$2722,z$2723)).equals($$$cl2309.String("a,b,c",5)),$$$cl2309.String("named arguments 5",17));
    var x$2721,y$2722,z$2723;
    $$$c2310.check((y$2724=$$$cl2309.String("b",1),x$2725=$$$cl2309.String("a",1),z$2726=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.Tuple($$$cl2309.String("d",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),namedArgFunc(x$2725,y$2724,z$2726)).equals($$$cl2309.String("a,b,c,d",7)),$$$cl2309.String("named arguments 6",17));
    var y$2724,x$2725,z$2726;
    $$$c2310.check((x$2727=$$$cl2309.String("a",1),z$2728=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),namedArgFunc(x$2727,undefined,z$2728)).equals($$$cl2309.String("a,ay,c",6)),$$$cl2309.String("named arguments 7",17));
    var x$2727,z$2728;
    $$$c2310.check((y$2729=$$$cl2309.String("b",1),z$2730=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),namedArgFunc(undefined,y$2729,z$2730)).equals($$$cl2309.String("x,b,c",5)),$$$cl2309.String("named arguments 8",17));
    var y$2729,z$2730;
    $$$c2310.check((y$2731=$$$cl2309.String("b",1),x$2732=$$$cl2309.String("a",1),namedArgFunc(x$2732,y$2731,$$$cl2309.getEmpty())).equals($$$cl2309.String("a,b",3)),$$$cl2309.String("named arguments 9",17));
    var y$2731,x$2732;
    $$$c2310.check((z$2733=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.Tuple($$$cl2309.String("d",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),namedArgFunc(undefined,undefined,z$2733)).equals($$$cl2309.String("x,xy,c,d",8)),$$$cl2309.String("named arguments 11",18));
    var z$2733;
    $$$c2310.check((y$2734=$$$cl2309.String("b",1),z$2735=$$$cl2309.Tuple($$$cl2309.String("c",1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),x$2736=$$$cl2309.String("a",1),namedArgFunc(x$2736,y$2734,z$2735)).equals($$$cl2309.String("a,b,c",5)),$$$cl2309.String("named arguments 12",18));
    var y$2734,z$2735,x$2736;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$2737=(i$2738=(1),more$2739=$$$cl2309.Tuple((i$2740=(2),Issue105(i$2740,$$$cl2309.getEmpty())),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$2738,more$2739));
    var i$2738,more$2739,i$2740;
    $$$c2310.check(issue105$2737.i.equals((1)),$$$cl2309.String("issue #105",10));
};

//InterfaceDefinition LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl2309.initTypeProto(LazyExprBase,'functions::LazyExprBase');
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
    $$lazyExprTest.x$2741_=(1000);
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    return $$lazyExprTest;
}
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl2309.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl2309.Basic,$init$LazyExprBase());
        (function($$lazyExprTest){
            
            //AttributeDeclaration x at functions.ceylon (149:4-149:36)
            $$$cl2309.defineAttr($$lazyExprTest,'x',function(){return this.x$2741_;},function(x$2742){return this.x$2741_=x$2742;});
            
            //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
            $$lazyExprTest.f1=function (i$2743,f$2744){
                var $$lazyExprTest=this;
                if(f$2744===undefined){f$2744=function (){
                    return $$$cl2309.StringBuilder().appendAll([i$2743.string,$$$cl2309.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).string]).string;
                };}
                return $$$cl2309.StringBuilder().appendAll([i$2743.string,$$$cl2309.String(":",1),f$2744().string]).string;
            };
            
            //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
            $$lazyExprTest.f2=function (i$2745){
                var $$lazyExprTest=this;
                return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x)).plus(i$2745);
            };
            
            //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
            $$$cl2309.defineAttr($$lazyExprTest,'i1',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x);
            });
            
            //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
            $$$cl2309.defineAttr($$lazyExprTest,'i2',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).times((2));
            });
            $$$cl2309.defineAttr($$lazyExprTest,'s1',function(){
                var $$lazyExprTest=this;
                return $$$cl2309.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).string,$$$cl2309.String(".1",2)]).string;
            });
            $$lazyExprTest.s2=function (i$2746){
                var $$lazyExprTest=this;
                return $$$cl2309.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).string,$$$cl2309.String(".",1),i$2746.string]).string;
            };
            //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
            $$lazyExprTest.f3=function (f$2747){
                var $$lazyExprTest=this;
                return f$2747(($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x));
            };
        })(LazyExprTest.$$.prototype);
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDeclaration lx at functions.ceylon (161:0-161:26)
var lx$2748=(1000);
var getLx=function(){return lx$2748;};
exports.getLx=getLx;
var setLx=function(lx$2749){return lx$2748=lx$2749;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$2750,f$2751){
    if(f$2751===undefined){f$2751=function (){
        return $$$cl2309.StringBuilder().appendAll([i$2750.string,$$$cl2309.String(".",1),(setLx(getLx().successor),getLx()).string]).string;
    };}
    return f$2751();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$2752){
    return (2).times((setLx(getLx().successor),getLx())).plus(i$2752);
};

//AttributeDeclaration lazy_i1 at functions.ceylon (164:0-164:23)
var getLazy_i1=function(){return (setLx(getLx().successor),getLx());};
exports.getLazy_i1=getLazy_i1;

//ClassDefinition LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDeclaration x at functions.ceylon (167:4-167:35)
    $$lazyExprTest2.x$2753_=(1000);
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    return $$lazyExprTest2;
}
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl2309.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl2309.Basic,$init$LazyExprBase());
        (function($$lazyExprTest2){
            
            //AttributeDeclaration x at functions.ceylon (167:4-167:35)
            $$$cl2309.defineAttr($$lazyExprTest2,'x',function(){return this.x$2753_;},function(x$2754){return this.x$2753_=x$2754;});
            
            //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
            $$$cl2309.defineAttr($$lazyExprTest2,'s1',function(){
                var $$lazyExprTest2=this;
                return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor,$$lazyExprTest2.x).string;
            });
            
            //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
            $$lazyExprTest2.s2=function (i$2755){
                var $$lazyExprTest2=this;
                return $$$cl2309.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor,$$lazyExprTest2.x).string,$$$cl2309.String("-",1),i$2755.string]).string;
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
    $$lazyExprTest3.s1$2756_=$$$cl2309.String("s1",2);
    return $$lazyExprTest3;
}
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl2309.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',LazyExprTest2);
        (function($$lazyExprTest3){
            
            //AttributeDeclaration s1 at functions.ceylon (172:4-172:43)
            $$$cl2309.defineAttr($$lazyExprTest3,'s1',function(){return this.s1$2756_;},function(s1$2757){return this.s1$2756_=s1$2757;});
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
    $$lazyExprTest4.assigned$2758_=$$$cl2309.String("",0);
    return $$lazyExprTest4;
}
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl2309.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',LazyExprTest2);
        (function($$lazyExprTest4){
            
            //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
            $$$cl2309.defineAttr($$lazyExprTest4,'assigned',function(){return this.assigned$2758_;},function(assigned$2759){return this.assigned$2758_=assigned$2759;});
            
            //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
            $$$cl2309.defineAttr($$lazyExprTest4,'s1',function(){
                var $$lazyExprTest4=this;
                return $$$cl2309.StringBuilder().appendAll([$$$cl2309.String("s1-",3),$$$cl2309.attrGetter($$lazyExprTest4.getT$all()['functions::LazyExprTest2'],'s1').call(this).string]).string;
            },function(s1$2760){
                var $$lazyExprTest4=this;
                $$lazyExprTest4.assigned=s1$2760;
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
    var tst$2761=LazyExprTest();
    (tmp$2762=tst$2761,tmp$2762.x=(1),tmp$2762.x);
    var tmp$2762;
    $$$c2310.check(tst$2761.f1((3)).equals($$$cl2309.String("3:3.2",5)),$$$cl2309.String("=> defaulted param",18));
    $$$c2310.check(tst$2761.f2((3)).equals((9)),$$$cl2309.String("=> method",9));
    $$$c2310.check(tst$2761.i1.equals((4)),$$$cl2309.String("=> attribute",12));
    $$$c2310.check(tst$2761.i2.equals((10)),$$$cl2309.String("=> attribute specifier",22));
    $$$c2310.check(tst$2761.s1.equals($$$cl2309.String("6.1",3)),$$$cl2309.String("=> attribute refinement",23));
    $$$c2310.check(tst$2761.s2((5)).equals($$$cl2309.String("7.5",3)),$$$cl2309.String("=> method refinement",20));
    setLx((1));
    $$$c2310.check(lazy_f1((3)).equals($$$cl2309.String("3.2",3)),$$$cl2309.String("=> defaulted param toplevel",27));
    $$$c2310.check(lazy_f2((3)).equals((9)),$$$cl2309.String("=> method toplevel",18));
    $$$c2310.check(getLazy_i1().equals((4)),$$$cl2309.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$2763=(1000);
    var setX$2763=function(x$2764){return x$2763=x$2764;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$2765=function (i$2766,f$2767){
        if(f$2767===undefined){f$2767=function (){
            return $$$cl2309.StringBuilder().appendAll([i$2766.string,$$$cl2309.String(".",1),(x$2763=x$2763.successor,x$2763).string]).string;
        };}
        return f$2767();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$2768=function (i$2769){
        return (2).times((x$2763=x$2763.successor,x$2763)).plus(i$2769);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$2770=function(){return (x$2763=x$2763.successor,x$2763);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$2771;
    var getI2$2771=function(){
        return (x$2763=x$2763.successor,x$2763).times((2));
    };
    x$2763=(1);
    $$$c2310.check(f1$2765((3)).equals($$$cl2309.String("3.2",3)),$$$cl2309.String("=> defaulted param local",24));
    $$$c2310.check(f2$2768((3)).equals((9)),$$$cl2309.String("=> method local",15));
    $$$c2310.check(getI1$2770().equals((4)),$$$cl2309.String("=> attribute local",18));
    $$$c2310.check(getI2$2771().equals((10)),$$$cl2309.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$2772=LazyExprTest3();
    (tmp$2773=tst3$2772,tmp$2773.x=(1),tmp$2773.x);
    var tmp$2773;
    $$$c2310.check(tst3$2772.s1.equals($$$cl2309.String("s1",2)),$$$cl2309.String("=> override variable 1",22));
    (tmp$2774=tst3$2772,tmp$2774.s1=$$$cl2309.String("abc",3),tmp$2774.s1);
    var tmp$2774;
    $$$c2310.check(tst3$2772.s1.equals($$$cl2309.String("abc",3)),$$$cl2309.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$2775=LazyExprTest4();
    (tmp$2776=tst4$2775,tmp$2776.x=(1),tmp$2776.x);
    var tmp$2776;
    $$$c2310.check(tst4$2775.s1.equals($$$cl2309.String("s1-2",4)),$$$cl2309.String("=> override getter/setter 1",27));
    (tmp$2777=tst4$2775,tmp$2777.s1=$$$cl2309.String("abc",3),tmp$2777.s1);
    var tmp$2777;
    $$$c2310.check(tst4$2775.s1.equals($$$cl2309.String("s1-4",4)),$$$cl2309.String("=> override getter/setter 2",27));
    $$$c2310.check(tst4$2775.assigned.equals($$$cl2309.String("abc",3)),$$$cl2309.String("=> override getter/setter 3",27));
    (tmp$2778=tst$2761,tmp$2778.x=(1),tmp$2778.x);
    var tmp$2778;
    x$2763=(10);
    $$$c2310.check((i$2779=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$2763=x$2763.successor,x$2763);
    }()),(opt$2780=tst$2761,$$$cl2309.JsCallable(opt$2780,opt$2780!==null?opt$2780.f1:null))(i$2779,undefined)).equals($$$cl2309.String("11:11.2",7)),$$$cl2309.String("=> named arg",12));
    var i$2779,opt$2780;
    $$$c2310.check((i$2781=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$2763=x$2763.successor,x$2763);
    }()),f$2782=function (){return (x$2763=x$2763.successor,x$2763).string;},(opt$2783=tst$2761,$$$cl2309.JsCallable(opt$2783,opt$2783!==null?opt$2783.f1:null))(i$2781,f$2782)).equals($$$cl2309.String("12:13",5)),$$$cl2309.String("=> named arg function",21));
    var i$2781,f$2782,opt$2783;
    $$$c2310.check((f$2784=function (i$2785){return $$$cl2309.StringBuilder().appendAll([i$2785.string,$$$cl2309.String("-",1),(x$2763=x$2763.successor,x$2763).string]).string;},(opt$2786=tst$2761,$$$cl2309.JsCallable(opt$2786,opt$2786!==null?opt$2786.f3:null))(f$2784)).equals($$$cl2309.String("3-14",4)),$$$cl2309.String("=> named arg function with param",32));
    var f$2784,opt$2786;
};

//MethodDefinition test at functions.ceylon (227:0-242:0)
function test(){
    helloWorld();
    hello($$$cl2309.String("test",4));
    helloAll([$$$cl2309.String("Gavin",5),$$$cl2309.String("Enrique",7),$$$cl2309.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}));
    $$$c2310.check(toString((5)).equals($$$cl2309.String("5",1)),$$$cl2309.String("toString(obj)",13));
    $$$c2310.check(add($$$cl2309.Float(1.5),$$$cl2309.Float(2.5)).equals($$$cl2309.Float(4.0)),$$$cl2309.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    $$$c2310.results();
}
exports.test=test;

//MethodDefinition multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$2787,y$2788){
        return x$2787.compare(y$2788);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$2789){
    return function(apat$2790){
        return function(amat$2791){
            return $$$cl2309.StringBuilder().appendAll([nombre$2789.string,$$$cl2309.String(" ",1),apat$2790.string,$$$cl2309.String(" ",1),amat$2791.string]).string;
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$2792){
    if(name$2792===undefined){name$2792=$$$cl2309.String("A",1);}
    return function(apat$2793){
        return function(amat$2794){
            return $$$cl2309.StringBuilder().appendAll([name$2792.string,$$$cl2309.String(" ",1),apat$2793.string,$$$cl2309.String(" ",1),amat$2794.string]).string;
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$2795){
    if(names$2795===undefined){names$2795=$$$cl2309.getEmpty();}
    return function(count$2796){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$2797=$$$cl2309.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$2798 = names$2795.iterator;
        var name$2799;while ((name$2799=it$2798.next())!==$$$cl2309.getFinished()){
            sb$2797.append(name$2799).append($$$cl2309.String(" ",1));
        }
        sb$2797.append($$$cl2309.String("count ",6)).append(count$2796.string);
        return sb$2797.string;
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl2309.print($$$cl2309.String("Testing multiple parameter lists...",35));
    $$$c2310.check(multiCompare()((1),(1)).equals($$$cl2309.getEqual()),$$$cl2309.String("Multi compare 1",15));
    $$$c2310.check(multiCompare()((1),(2)).equals($$$cl2309.getSmaller()),$$$cl2309.String("Multi compare 2",15));
    $$$c2310.check(multiCompare()((2),(1)).equals($$$cl2309.getLarger()),$$$cl2309.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$2800=function (a$2801,b$2802){
        return multiCompare()(a$2801,b$2802);
    };
    $$$c2310.check(comp$2800((1),(1)).equals($$$cl2309.getEqual()),$$$cl2309.String("Multi compare 4",15));
    $$$c2310.check(comp$2800((1),(2)).equals($$$cl2309.getSmaller()),$$$cl2309.String("Multi compare 5",15));
    $$$c2310.check(comp$2800((2),(1)).equals($$$cl2309.getLarger()),$$$cl2309.String("Multi compare 6",15));
    $$$c2310.check(multiFullname($$$cl2309.String("a",1))($$$cl2309.String("b",1).valueOf())($$$cl2309.String("c",1).valueOf()).equals($$$cl2309.String("a b c",5)),$$$cl2309.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$2803=function (c$2804){
        return multiFullname($$$cl2309.String("A",1))($$$cl2309.String("B",1).valueOf())(c$2804.valueOf());
    };
    $$$c2310.check(apat$2803($$$cl2309.String("C",1)).equals($$$cl2309.String("A B C",5)),$$$cl2309.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$2805=function (name$2806){
        return multiFullname($$$cl2309.String("Name",4))(name$2806.valueOf());
    };
    $$$c2310.check(nombre$2805($$$cl2309.String("Z",1))($$$cl2309.String("L",1).valueOf()).equals($$$cl2309.String("Name Z L",8)),$$$cl2309.String("Multi callable 2",16));
    $$$c2310.check(multiDefaulted()($$$cl2309.String("B",1).valueOf())($$$cl2309.String("C",1).valueOf()).equals($$$cl2309.String("A B C",5)),$$$cl2309.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$2807=multiDefaulted();
    $$$c2310.check(md1$2807($$$cl2309.String("B",1).valueOf())($$$cl2309.String("C",1).valueOf()).equals($$$cl2309.String("A B C",5)),$$$cl2309.String("Multi defaulted 2",17));
    $$$c2310.check(md1$2807($$$cl2309.String("B",1).valueOf())($$$cl2309.String("Z",1).valueOf()).equals($$$cl2309.String("A B Z",5)),$$$cl2309.String("Multi defaulted 3",17));
    $$$c2310.check(md1$2807($$$cl2309.String("Z",1).valueOf())($$$cl2309.String("C",1).valueOf()).equals($$$cl2309.String("A Z C",5)),$$$cl2309.String("Multi defaulted 4",17));
    $$$c2310.check(md1$2807($$$cl2309.String("Y",1).valueOf())($$$cl2309.String("Z",1).valueOf()).equals($$$cl2309.String("A Y Z",5)),$$$cl2309.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$2808=function (x$2809){
        return multiDefaulted()($$$cl2309.String("B",1).valueOf())(x$2809.valueOf());
    };
    $$$c2310.check(md2$2808($$$cl2309.String("C",1)).equals($$$cl2309.String("A B C",5)),$$$cl2309.String("Multi defaulted 6",17));
    $$$c2310.check(md2$2808($$$cl2309.String("Z",1)).equals($$$cl2309.String("A B Z",5)),$$$cl2309.String("Multi defaulted 7",17));
    $$$c2310.check(multiSequenced([$$$cl2309.String("A",1),$$$cl2309.String("B",1),$$$cl2309.String("C",1)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}))((1)).equals($$$cl2309.String("A B C count 1",13)),$$$cl2309.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$2810=function (c$2811){
        return multiSequenced([$$$cl2309.String("x",1),$$$cl2309.String("y",1),$$$cl2309.String("z",1)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}))(c$2811);
    };
    $$$c2310.check(ms1$2810((5)).equals($$$cl2309.String("x y z count 5",13)),$$$cl2309.String("Multi sequenced 2",17));
    $$$c2310.check(ms1$2810((10)).equals($$$cl2309.String("x y z count 10",14)),$$$cl2309.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
