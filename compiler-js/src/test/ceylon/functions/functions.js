(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"last":{"$t":{"$nm":"Element"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$300,f$301,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$302 = a$300.iterator;
    var e$303;while ((e$303=it$302.next())!==$$$cl1.getFinished()){
        if(f$301(e$303)){
            return e$303;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$304,f$305,$$$mptypes){
    if(f$305===undefined){f$305=function (x$306){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$307 = a$304.iterator;
    var e$308;while ((e$308=it$307.next())!==$$$cl1.getFinished()){
        if(f$305(e$308)){
            return e$308;
        }
    }
    if ($$$cl1.getFinished() === e$308){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$309){
    return function (i$310){
        return i$310.minus(howMuch$309).string;
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl1.print($$$cl1.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$311=(elements$312=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),$$$cl1.array(elements$312,{Element:{t:$$$cl1.Integer}}));
    var elements$312;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$313=find(nums$311,function (i$314){
        return i$314.remainder((2)).equals((0));
    },{Element:{t:$$$cl1.Integer}});
    var setFound$313=function(found$315){return found$313=found$315;};
    var i$316;
    if((i$316=found$313)!==null){
        $$$c2.check(i$316.equals((2)),$$$cl1.String("anonfunc positional",19));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc positional",19));
    }
    found$313=(f$317=function (i$318){
        return i$318.remainder((2)).equals((0));
    },a$319=nums$311,find(a$319,f$317,{Element:{t:$$$cl1.Integer}}));
    var f$317,a$319;
    var i$320;
    if((i$320=found$313)!==null){
        $$$c2.check(i$320.equals((2)),$$$cl1.String("anonfunc named",14));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$321(f$322,expect$323){
        $$$c2.check(f$322((0)).equals(expect$323),$$$cl1.StringBuilder().appendAll([$$$cl1.String("anon func returns ",18),f$322((0)).string,$$$cl1.String(" instead of ",12),expect$323.string]).string);
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$324(i$325){
        return i$325.plus((12)).string;
    };
    callFunction$321(f$324,$$$cl1.String("12",2));
    callFunction$321(function (i$326){
        return i$326.times((3)).string;
    },$$$cl1.String("0",1));
    (expect$327=$$$cl1.String("0",1),f$328=function (i$329){
        return i$329.power((2)).string;
    },callFunction$321(f$328,expect$327));
    var expect$327,f$328;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$330=function (i$331){
        return i$331.minus((10)).string;
    };
    callFunction$321(f2$330,$$$cl1.String("-10",3));
    callFunction$321(subtract((5)),$$$cl1.String("-5",2));
    found$313=find2(nums$311,function (i$332){
        return i$332.compare((2)).equals($$$cl1.getLarger());
    },{Element:{t:$$$cl1.Integer}});
    var i$333;
    if((i$333=found$313)!==null){
        $$$c2.check(i$333.equals((3)),$$$cl1.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc i>2 [2]",16));
    }
    found$313=find2(nums$311,undefined,{Element:{t:$$$cl1.Integer}});
    var i$334;
    if((i$334=found$313)!==null){
        $$$c2.check(i$334.equals((1)),$$$cl1.String("anonfunc defaulted param [1]",28));
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
function hello(name$335){
    $$$cl1.print($$$cl1.String("hello",5).plus(name$335));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$336){
    if(names$336===undefined){names$336=$$$cl1.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$337){
    return obj$337.string;
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$338,y$339){
    return x$338.plus(y$339);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$340,f$341){
    f$341((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$342, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl1.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$342=seq$342;
    $$$cl1.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
    $$$cl1.defineAttr($$mySequence,'lastIndex',function(){
        return seq$342.lastIndex;
    });
    
    //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
    $$$cl1.defineAttr($$mySequence,'first',function(){
        return seq$342.first;
    });
    
    //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
    $$$cl1.defineAttr($$mySequence,'rest',function(){
        return seq$342.rest;
    });
    
    //MethodDefinition get at functions.ceylon (30:4-30:67)
    function get(index$343){
        return seq$342.get(index$343);
    }
    $$mySequence.get=get;
    
    //MethodDefinition span at functions.ceylon (31:4-31:88)
    function span(from$344,to$345){
        return seq$342.span(from$344,to$345);
    }
    $$mySequence.span=span;
    
    //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
    function spanFrom(from$346){
        return seq$342.spanFrom(from$346);
    }
    $$mySequence.spanFrom=spanFrom;
    
    //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
    function spanTo(to$347){
        return seq$342.spanTo(to$347);
    }
    $$mySequence.spanTo=spanTo;
    
    //MethodDefinition segment at functions.ceylon (34:4-34:102)
    function segment(from$348,length$349){
        return seq$342.segment(from$348,length$349);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
    $$$cl1.defineAttr($$mySequence,'clone',function(){
        return $$mySequence;
    });
    
    //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
    $$$cl1.defineAttr($$mySequence,'string',function(){
        return seq$342.string;
    });
    
    //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
    $$$cl1.defineAttr($$mySequence,'hash',function(){
        return seq$342.hash;
    });
    
    //MethodDefinition equals at functions.ceylon (38:4-38:75)
    function equals(other$350){
        return seq$342.equals(other$350);
    }
    $$mySequence.equals=equals;
    
    //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
    $$$cl1.defineAttr($$mySequence,'reversed',function(){
        return seq$342.reversed;
    });
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    $$$cl1.defineAttr($$mySequence,'last',function(){return seq$342.last;});
    
    //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
    $$$cl1.defineAttr($$mySequence,'iterator',function(){return seq$342.iterator;});
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    $$$cl1.defineAttr($$mySequence,'size',function(){return seq$342.size;});
    
    //MethodDeclaration contains at functions.ceylon (43:4-43:71)
    var contains=function (other$351){
        return seq$342.contains(other$351);
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
    function f(i$352){
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
    var obj1$353=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$354=MySequence($$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Element:{t:$$$cl1.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$355(x$356){
        return x$356((0));
    };
    $$$c2.check(tst$355((opt$357=obj1$353,$$$cl1.JsCallable(opt$357,opt$357!==null?opt$357.f:null))),$$$cl1.String("Reference to method",19));
    var opt$357;
    $$$c2.check(tst$355((opt$358=obj2$354,$$$cl1.JsCallable(opt$358,opt$358!==null?opt$358.defines:null))),$$$cl1.String("Reference to method from ceylon.language",40));
    var opt$358;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$359,i2$360,i3$361){
    if(i2$360===undefined){i2$360=i1$359.plus((1));}
    if(i3$361===undefined){i3$361=i1$359.plus(i2$360);}
    return $$$cl1.StringBuilder().appendAll([i1$359.string,$$$cl1.String(",",1),i2$360.string,$$$cl1.String(",",1),i3$361.string]).string;
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$362, i2$363, i3$364, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$363===undefined){i2$363=i1$362.plus((1));}
    if(i3$364===undefined){i3$364=i1$362.plus(i2$363);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    var s=$$$cl1.StringBuilder().appendAll([i1$362.string,$$$cl1.String(",",1),i2$363.string,$$$cl1.String(",",1),i3$364.string]).string;
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
function DefParamTest2(i1$365, i2$366, i3$367, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$365=i1$365;
    if(i2$366===undefined){i2$366=i1$365.plus((1));}
    $$defParamTest2.i2$366=i2$366;
    if(i3$367===undefined){i3$367=i1$365.plus(i2$366);}
    $$defParamTest2.i3$367=i3$367;
    
    //MethodDefinition f at functions.ceylon (67:4-67:55)
    function f(){
        return $$$cl1.StringBuilder().appendAll([i1$365.string,$$$cl1.String(",",1),i2$366.string,$$$cl1.String(",",1),i3$367.string]).string;
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
    function f(i1$368,i2$369,i3$370){
        if(i2$369===undefined){i2$369=i1$368.plus((1));}
        if(i3$370===undefined){i3$370=i1$368.plus(i2$369);}
        return $$$cl1.StringBuilder().appendAll([i1$368.string,$$$cl1.String(",",1),i2$369.string,$$$cl1.String(",",1),i3$370.string]).string;
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
    $$$c2.check((i1$371=(1),defParamTest(i1$371,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters named 1",28));
    var i1$371;
    $$$c2.check((i1$372=(1),i2$373=(3),defParamTest(i1$372,i2$373,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters named 2",28));
    var i1$372,i2$373;
    $$$c2.check((i1$374=(1),i3$375=(0),defParamTest(i1$374,undefined,i3$375)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters named 3",28));
    var i1$374,i3$375;
    $$$c2.check(DefParamTest1((1)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class 1",28));
    $$$c2.check(DefParamTest1((1),(3)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class 2",28));
    $$$c2.check(DefParamTest1((1),(3),(0)).s.equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class 3",28));
    $$$c2.check((i1$376=(1),DefParamTest1(i1$376,undefined,undefined)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class named 1",34));
    var i1$376;
    $$$c2.check((i1$377=(1),i2$378=(3),DefParamTest1(i1$377,i2$378,undefined)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class named 2",34));
    var i1$377,i2$378;
    $$$c2.check((i1$379=(1),i3$380=(0),DefParamTest1(i1$379,undefined,i3$380)).s.equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class named 3",34));
    var i1$379,i3$380;
    $$$c2.check(DefParamTest2((1)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 1",29));
    $$$c2.check(DefParamTest2((1),(3)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 2",29));
    $$$c2.check(DefParamTest2((1),(3),(0)).f().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class2 3",29));
    $$$c2.check((i1$381=(1),DefParamTest2(i1$381,undefined,undefined)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 named 1",35));
    var i1$381;
    $$$c2.check((i1$382=(1),i2$383=(3),DefParamTest2(i1$382,i2$383,undefined)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 named 2",35));
    var i1$382,i2$383;
    $$$c2.check((i1$384=(1),i3$385=(0),DefParamTest2(i1$384,undefined,i3$385)).f().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class2 named 3",35));
    var i1$384,i3$385;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$386=DefParamTest3();
    $$$c2.check(tst$386.f((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters 1",29));
    $$$c2.check(tst$386.f((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters 2",29));
    $$$c2.check(tst$386.f((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted method parameters 3",29));
    $$$c2.check((i1$387=(1),(opt$388=tst$386,$$$cl1.JsCallable(opt$388,opt$388!==null?opt$388.f:null))(i1$387,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters named 1",35));
    var i1$387,opt$388;
    $$$c2.check((i1$389=(1),i2$390=(3),(opt$391=tst$386,$$$cl1.JsCallable(opt$391,opt$391!==null?opt$391.f:null))(i1$389,i2$390,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters named 2",35));
    var i1$389,i2$390,opt$391;
    $$$c2.check((i1$392=(1),i3$393=(0),(opt$394=tst$386,$$$cl1.JsCallable(opt$394,opt$394!==null?opt$394.f:null))(i1$392,undefined,i3$393)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted method parameters named 3",35));
    var i1$392,i3$393,opt$394;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$395($$getterTest$395){
        $init$GetterTest$395();
        if ($$getterTest$395===undefined)$$getterTest$395=new GetterTest$395.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        var i$396=(0);
        $$$cl1.defineAttr($$getterTest$395,'i$396',function(){return i$396;},function(i$397){return i$396=i$397;});
        
        //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
        $$$cl1.defineAttr($$getterTest$395,'x',function(){
            return (i$396=i$396.successor,i$396);
        });
        return $$getterTest$395;
    }
    function $init$GetterTest$395(){
        if (GetterTest$395.$$===undefined){
            $$$cl1.initTypeProto(GetterTest$395,'functions::testGetterMethodDefinitions.GetterTest',$$$cl1.Basic);
        }
        return GetterTest$395;
    }
    $init$GetterTest$395();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$398=GetterTest$395();
    $$$c2.check(gt$398.x.equals((1)),$$$cl1.String("getter defined as method 1",26));
    $$$c2.check(gt$398.x.equals((2)),$$$cl1.String("getter defined as method 2",26));
    $$$c2.check(gt$398.x.equals((3)),$$$cl1.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$399,y$400,z$401){
    if(x$399===undefined){x$399=$$$cl1.String("x",1);}
    if(y$400===undefined){y$400=x$399.plus($$$cl1.String("y",1));}
    if(z$401===undefined){z$401=$$$cl1.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$402=x$399.plus($$$cl1.String(",",1)).plus(y$400);
    var setResult$402=function(result$403){return result$402=result$403;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$404 = z$401.iterator;
    var s$405;while ((s$405=it$404.next())!==$$$cl1.getFinished()){
        (result$402=result$402.plus($$$cl1.String(",",1).plus(s$405)),result$402);
    }
    return result$402;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$406, more$407, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$407===undefined){more$407=$$$cl1.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    var i=i$406;
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
    $$$c2.check((x$408=$$$cl1.String("a",1),namedArgFunc(x$408,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("a,ay",4)),$$$cl1.String("named arguments 2",17));
    var x$408;
    $$$c2.check((y$409=$$$cl1.String("b",1),namedArgFunc(undefined,y$409,$$$cl1.getEmpty())).equals($$$cl1.String("x,b",3)),$$$cl1.String("named arguments 3",17));
    var y$409;
    $$$c2.check((z$410=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$410)).equals($$$cl1.String("x,xy,c",6)),$$$cl1.String("named arguments 4",17));
    var z$410;
    $$$c2.check((x$411=$$$cl1.String("a",1),y$412=$$$cl1.String("b",1),z$413=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$411,y$412,z$413)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 5",17));
    var x$411,y$412,z$413;
    $$$c2.check((y$414=$$$cl1.String("b",1),x$415=$$$cl1.String("a",1),z$416=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$415,y$414,z$416)).equals($$$cl1.String("a,b,c,d",7)),$$$cl1.String("named arguments 6",17));
    var y$414,x$415,z$416;
    $$$c2.check((x$417=$$$cl1.String("a",1),z$418=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$417,undefined,z$418)).equals($$$cl1.String("a,ay,c",6)),$$$cl1.String("named arguments 7",17));
    var x$417,z$418;
    $$$c2.check((y$419=$$$cl1.String("b",1),z$420=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,y$419,z$420)).equals($$$cl1.String("x,b,c",5)),$$$cl1.String("named arguments 8",17));
    var y$419,z$420;
    $$$c2.check((y$421=$$$cl1.String("b",1),x$422=$$$cl1.String("a",1),namedArgFunc(x$422,y$421,$$$cl1.getEmpty())).equals($$$cl1.String("a,b",3)),$$$cl1.String("named arguments 9",17));
    var y$421,x$422;
    $$$c2.check((z$423=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$423)).equals($$$cl1.String("x,xy,c,d",8)),$$$cl1.String("named arguments 11",18));
    var z$423;
    $$$c2.check((y$424=$$$cl1.String("b",1),z$425=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),x$426=$$$cl1.String("a",1),namedArgFunc(x$426,y$424,z$425)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 12",18));
    var y$424,z$425,x$426;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$427=(i$428=(1),more$429=$$$cl1.Tuple((i$430=(2),Issue105(i$430,$$$cl1.getEmpty())),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$428,more$429));
    var i$428,more$429,i$430;
    $$$c2.check(issue105$427.i.equals((1)),$$$cl1.String("issue #105",10));
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
    $$$cl1.defineAttr($$lazyExprTest,'x',function(){return x;},function(x$431){return x=x$431;});
    
    //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
    var f1=function (i$432,f$433){
        if(f$433===undefined){f$433=function (){
            return $$$cl1.StringBuilder().appendAll([i$432.string,$$$cl1.String(".",1),($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).string]).string;
        };}
        return $$$cl1.StringBuilder().appendAll([i$432.string,$$$cl1.String(":",1),f$433().string]).string;
    };
    $$lazyExprTest.f1=f1;
    
    //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
    var f2=function (i$434){
        return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x)).plus(i$434);
    };
    $$lazyExprTest.f2=f2;
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    $$$cl1.defineAttr($$lazyExprTest,'i1',function(){return ($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x);});
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    $$$cl1.defineAttr($$lazyExprTest,'i2',function(){
        return ($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).times((2));
    });
    $$$cl1.defineAttr($$lazyExprTest,'s1',function(){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).string,$$$cl1.String(".1",2)]).string;
    });
    $$lazyExprTest.s2=function (i$435){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x).string,$$$cl1.String(".",1),i$435.string]).string;
    };
    
    //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
    var f3=function (f$436){
        return f$436(($$lazyExprTest.x=$$lazyExprTest.x.successor,$$lazyExprTest.x));
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
var lx$437=(1000);
var getLx=function(){return lx$437;};
exports.getLx=getLx;
var setLx=function(lx$438){return lx$437=lx$438;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$439,f$440){
    if(f$440===undefined){f$440=function (){
        return $$$cl1.StringBuilder().appendAll([i$439.string,$$$cl1.String(".",1),(setLx(getLx().successor),getLx()).string]).string;
    };}
    return f$440();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$441){
    return (2).times((setLx(getLx().successor),getLx())).plus(i$441);
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
    var x=(1000);
    $$$cl1.defineAttr($$lazyExprTest2,'x',function(){return x;},function(x$442){return x=x$442;});
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    $$$cl1.defineAttr($$lazyExprTest2,'s1',function(){return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor,$$lazyExprTest2.x).string;});
    
    //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
    var s2=function (i$443){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor,$$lazyExprTest2.x).string,$$$cl1.String("-",1),i$443.string]).string;
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
    $$$cl1.defineAttr($$lazyExprTest3,'s1',function(){return s1;},function(s1$444){return s1=s1$444;});
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
    $$$cl1.defineAttr($$lazyExprTest4,'assigned',function(){return assigned;},function(assigned$445){return assigned=assigned$445;});
    
    //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
    $$$cl1.defineAttr($$lazyExprTest4,'s1',function(){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("s1-",3),$$lazyExprTest4.s1$$functions$LazyExprTest2.string]).string;
    },function(s1$446){
        $$lazyExprTest4.assigned=s1$446;
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
    var tst$447=LazyExprTest();
    (tmp$448=tst$447,tmp$448.x=(1),tmp$448.x);
    var tmp$448;
    $$$c2.check(tst$447.f1((3)).equals($$$cl1.String("3:3.2",5)),$$$cl1.String("=> defaulted param",18));
    $$$c2.check(tst$447.f2((3)).equals((9)),$$$cl1.String("=> method",9));
    $$$c2.check(tst$447.i1.equals((4)),$$$cl1.String("=> attribute",12));
    $$$c2.check(tst$447.i2.equals((10)),$$$cl1.String("=> attribute specifier",22));
    $$$c2.check(tst$447.s1.equals($$$cl1.String("6.1",3)),$$$cl1.String("=> attribute refinement",23));
    $$$c2.check(tst$447.s2((5)).equals($$$cl1.String("7.5",3)),$$$cl1.String("=> method refinement",20));
    setLx((1));
    $$$c2.check(lazy_f1((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param toplevel",27));
    $$$c2.check(lazy_f2((3)).equals((9)),$$$cl1.String("=> method toplevel",18));
    $$$c2.check(getLazy_i1().equals((4)),$$$cl1.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$449=(1000);
    var setX$449=function(x$450){return x$449=x$450;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$451=function (i$452,f$453){
        if(f$453===undefined){f$453=function (){
            return $$$cl1.StringBuilder().appendAll([i$452.string,$$$cl1.String(".",1),(x$449=x$449.successor,x$449).string]).string;
        };}
        return f$453();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$454=function (i$455){
        return (2).times((x$449=x$449.successor,x$449)).plus(i$455);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$456=function(){return (x$449=x$449.successor,x$449);};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$457;
    var getI2$457=function(){
        return (x$449=x$449.successor,x$449).times((2));
    };
    x$449=(1);
    $$$c2.check(f1$451((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param local",24));
    $$$c2.check(f2$454((3)).equals((9)),$$$cl1.String("=> method local",15));
    $$$c2.check(getI1$456().equals((4)),$$$cl1.String("=> attribute local",18));
    $$$c2.check(getI2$457().equals((10)),$$$cl1.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$458=LazyExprTest3();
    (tmp$459=tst3$458,tmp$459.x=(1),tmp$459.x);
    var tmp$459;
    $$$c2.check(tst3$458.s1.equals($$$cl1.String("s1",2)),$$$cl1.String("=> override variable 1",22));
    (tmp$460=tst3$458,tmp$460.s1=$$$cl1.String("abc",3),tmp$460.s1);
    var tmp$460;
    $$$c2.check(tst3$458.s1.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$461=LazyExprTest4();
    (tmp$462=tst4$461,tmp$462.x=(1),tmp$462.x);
    var tmp$462;
    $$$c2.check(tst4$461.s1.equals($$$cl1.String("s1-2",4)),$$$cl1.String("=> override getter/setter 1",27));
    (tmp$463=tst4$461,tmp$463.s1=$$$cl1.String("abc",3),tmp$463.s1);
    var tmp$463;
    $$$c2.check(tst4$461.s1.equals($$$cl1.String("s1-4",4)),$$$cl1.String("=> override getter/setter 2",27));
    $$$c2.check(tst4$461.assigned.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override getter/setter 3",27));
    (tmp$464=tst$447,tmp$464.x=(1),tmp$464.x);
    var tmp$464;
    x$449=(10);
    $$$c2.check((i$465=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$449=x$449.successor,x$449);
    }()),(opt$466=tst$447,$$$cl1.JsCallable(opt$466,opt$466!==null?opt$466.f1:null))(i$465,undefined)).equals($$$cl1.String("11:11.2",7)),$$$cl1.String("=> named arg",12));
    var i$465,opt$466;
    $$$c2.check((i$467=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$449=x$449.successor,x$449);
    }()),f$468=function (){return (x$449=x$449.successor,x$449).string;},(opt$469=tst$447,$$$cl1.JsCallable(opt$469,opt$469!==null?opt$469.f1:null))(i$467,f$468)).equals($$$cl1.String("12:13",5)),$$$cl1.String("=> named arg function",21));
    var i$467,f$468,opt$469;
    $$$c2.check((f$470=function (i$471){return $$$cl1.StringBuilder().appendAll([i$471.string,$$$cl1.String("-",1),(x$449=x$449.successor,x$449).string]).string;},(opt$472=tst$447,$$$cl1.JsCallable(opt$472,opt$472!==null?opt$472.f3:null))(f$470)).equals($$$cl1.String("3-14",4)),$$$cl1.String("=> named arg function with param",32));
    var f$470,opt$472;
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
    return function(x$473,y$474){
        return x$473.compare(y$474);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$475){
    return function(apat$476){
        return function(amat$477){
            return $$$cl1.StringBuilder().appendAll([nombre$475.string,$$$cl1.String(" ",1),apat$476.string,$$$cl1.String(" ",1),amat$477.string]).string;
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$478){
    if(name$478===undefined){name$478=$$$cl1.String("A",1);}
    return function(apat$479){
        return function(amat$480){
            return $$$cl1.StringBuilder().appendAll([name$478.string,$$$cl1.String(" ",1),apat$479.string,$$$cl1.String(" ",1),amat$480.string]).string;
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$481){
    if(names$481===undefined){names$481=$$$cl1.getEmpty();}
    return function(count$482){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$483=$$$cl1.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$484 = names$481.iterator;
        var name$485;while ((name$485=it$484.next())!==$$$cl1.getFinished()){
            sb$483.append(name$485).append($$$cl1.String(" ",1));
        }
        sb$483.append($$$cl1.String("count ",6)).append(count$482.string);
        return sb$483.string;
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl1.print($$$cl1.String("Testing multiple parameter lists...",35));
    $$$c2.check(multiCompare()((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 1",15));
    $$$c2.check(multiCompare()((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 2",15));
    $$$c2.check(multiCompare()((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$486=function (a$487,b$488){
        return multiCompare()(a$487,b$488);
    };
    $$$c2.check(comp$486((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 4",15));
    $$$c2.check(comp$486((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 5",15));
    $$$c2.check(comp$486((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 6",15));
    $$$c2.check(multiFullname($$$cl1.String("a",1))($$$cl1.String("b",1).valueOf())($$$cl1.String("c",1).valueOf()).equals($$$cl1.String("a b c",5)),$$$cl1.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$489=function (c$490){
        return multiFullname($$$cl1.String("A",1))($$$cl1.String("B",1).valueOf())(c$490.valueOf());
    };
    $$$c2.check(apat$489($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$491=function (name$492){
        return multiFullname($$$cl1.String("Name",4))(name$492.valueOf());
    };
    $$$c2.check(nombre$491($$$cl1.String("Z",1))($$$cl1.String("L",1).valueOf()).equals($$$cl1.String("Name Z L",8)),$$$cl1.String("Multi callable 2",16));
    $$$c2.check(multiDefaulted()($$$cl1.String("B",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$493=multiDefaulted();
    $$$c2.check(md1$493($$$cl1.String("B",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 2",17));
    $$$c2.check(md1$493($$$cl1.String("B",1).valueOf())($$$cl1.String("Z",1).valueOf()).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 3",17));
    $$$c2.check(md1$493($$$cl1.String("Z",1).valueOf())($$$cl1.String("C",1).valueOf()).equals($$$cl1.String("A Z C",5)),$$$cl1.String("Multi defaulted 4",17));
    $$$c2.check(md1$493($$$cl1.String("Y",1).valueOf())($$$cl1.String("Z",1).valueOf()).equals($$$cl1.String("A Y Z",5)),$$$cl1.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$494=function (x$495){
        return multiDefaulted()($$$cl1.String("B",1).valueOf())(x$495.valueOf());
    };
    $$$c2.check(md2$494($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 6",17));
    $$$c2.check(md2$494($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 7",17));
    $$$c2.check(multiSequenced([$$$cl1.String("A",1),$$$cl1.String("B",1),$$$cl1.String("C",1)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}))((1)).equals($$$cl1.String("A B C count 1",13)),$$$cl1.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$496=function (c$497){
        return multiSequenced([$$$cl1.String("x",1),$$$cl1.String("y",1),$$$cl1.String("z",1)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}))(c$497);
    };
    $$$c2.check(ms1$496((5)).equals($$$cl1.String("x y z count 5",13)),$$$cl1.String("Multi sequenced 2",17));
    $$$c2.check(ms1$496((10)).equals($$$cl1.String("x y z count 10",14)),$$$cl1.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
