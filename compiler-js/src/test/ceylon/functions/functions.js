(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"more"}],"$mt":"cls","$at":{"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$pt":"v","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"last":{"$t":{"$nm":"Element"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1"};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition find at anonymous.ceylon (3:0-10:0)
function find(a$187,f$188,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$189 = a$187.getIterator();
    var e$190;while ((e$190=it$189.next())!==$$$cl1.getFinished()){
        if(f$188(e$190)){
            return e$190;
        }
    }
    return null;
};

//MethodDefinition find2 at anonymous.ceylon (12:0-20:0)
function find2(a$191,f$192,$$$mptypes){
    if(f$192===undefined){f$192=function (x$193){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$194 = a$191.getIterator();
    var e$195;while ((e$195=it$194.next())!==$$$cl1.getFinished()){
        if(f$192(e$195)){
            return e$195;
        }
    }
    if ($$$cl1.getFinished() === e$195){
        return null;
    }
};

//MethodDefinition subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$196){
    return function (i$197){
        return i$197.minus(howMuch$196).getString();
    };
};

//MethodDefinition testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl1.print($$$cl1.String("Testing anonymous functions...",30));
    
    //AttributeDeclaration nums at anonymous.ceylon (28:2-28:31)
    var nums$198=(elements$199=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),$$$cl1.array(elements$199,{Element:{t:$$$cl1.Integer}}));
    var elements$199;
    
    //AttributeDeclaration found at anonymous.ceylon (30:2-30:58)
    var found$200=find(nums$198,function (i$201){
        return i$201.remainder((2)).equals((0));
    },{Element:{t:$$$cl1.Integer}});
    var setFound$200=function(found$202){return found$200=found$202;};
    var i$203;
    if((i$203=found$200)!==null){
        $$$c2.check(i$203.equals((2)),$$$cl1.String("anonfunc positional",19));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc positional",19));
    }
    found$200=(f$204=function (i$205){
        return i$205.remainder((2)).equals((0));
    },a$206=nums$198,find(a$206,f$204,{Element:{t:$$$cl1.Integer}}));
    var f$204,a$206;
    var i$207;
    if((i$207=found$200)!==null){
        $$$c2.check(i$207.equals((2)),$$$cl1.String("anonfunc named",14));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc named",14));
    }
    
    //MethodDefinition callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$208(f$209,expect$210){
        $$$c2.check(f$209((0)).equals(expect$210),$$$cl1.StringBuilder().appendAll([$$$cl1.String("anon func returns ",18),f$209((0)).getString(),$$$cl1.String(" instead of ",12),expect$210.getString()]).getString());
    };
    
    //MethodDefinition f at anonymous.ceylon (50:2-52:2)
    function f$211(i$212){
        return i$212.plus((12)).getString();
    };
    callFunction$208(f$211,$$$cl1.String("12",2));
    callFunction$208(function (i$213){
        return i$213.times((3)).getString();
    },$$$cl1.String("0",1));
    (expect$214=$$$cl1.String("0",1),f$215=function (i$216){
        return i$216.power((2)).getString();
    },callFunction$208(f$215,expect$214));
    var expect$214,f$215;
    
    //AttributeDeclaration f2 at anonymous.ceylon (64:2-64:41)
    var f2$217=function (i$218){
        return i$218.minus((10)).getString();
    };
    callFunction$208(f2$217,$$$cl1.String("-10",3));
    callFunction$208(subtract((5)),$$$cl1.String("-5",2));
    found$200=find2(nums$198,function (i$219){
        return i$219.compare((2)).equals($$$cl1.getLarger());
    },{Element:{t:$$$cl1.Integer}});
    var i$220;
    if((i$220=found$200)!==null){
        $$$c2.check(i$220.equals((3)),$$$cl1.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc i>2 [2]",16));
    }
    found$200=find2(nums$198,undefined,{Element:{t:$$$cl1.Integer}});
    var i$221;
    if((i$221=found$200)!==null){
        $$$c2.check(i$221.equals((1)),$$$cl1.String("anonfunc defaulted param [1]",28));
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
function hello(name$222){
    $$$cl1.print($$$cl1.String("hello",5).plus(name$222));
}
exports.hello=hello;

//MethodDefinition helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$223){
    if(names$223===undefined){names$223=$$$cl1.getEmpty();}
}
exports.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (13:0-15:0)
function toString(obj$224){
    return obj$224.getString();
}
exports.toString=toString;

//MethodDefinition add at functions.ceylon (17:0-19:0)
function add(x$225,y$226){
    return x$225.plus(y$226);
}
exports.add=add;

//MethodDefinition repeat at functions.ceylon (21:0-23:0)
function repeat(times$227,f$228){
    f$228((0));
}
exports.repeat=repeat;

//ClassDefinition MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$229, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl1.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$229=seq$229;
    $$$cl1.Sequence($$mySequence);
    
    //AttributeGetterDefinition lastIndex at functions.ceylon (27:4-27:60)
    var getLastIndex=function(){
        return seq$229.getLastIndex();
    }
    $$mySequence.getLastIndex=getLastIndex;
    
    //AttributeGetterDefinition first at functions.ceylon (28:4-28:52)
    var getFirst=function(){
        return seq$229.getFirst();
    }
    $$mySequence.getFirst=getFirst;
    
    //AttributeGetterDefinition rest at functions.ceylon (29:4-29:52)
    var getRest=function(){
        return seq$229.getRest();
    }
    $$mySequence.getRest=getRest;
    
    //MethodDefinition get at functions.ceylon (30:4-30:67)
    function get(index$230){
        return seq$229.get(index$230);
    }
    $$mySequence.get=get;
    
    //MethodDefinition span at functions.ceylon (31:4-31:88)
    function span(from$231,to$232){
        return seq$229.span(from$231,to$232);
    }
    $$mySequence.span=span;
    
    //MethodDefinition spanFrom at functions.ceylon (32:4-32:80)
    function spanFrom(from$233){
        return seq$229.spanFrom(from$233);
    }
    $$mySequence.spanFrom=spanFrom;
    
    //MethodDefinition spanTo at functions.ceylon (33:4-33:72)
    function spanTo(to$234){
        return seq$229.spanTo(to$234);
    }
    $$mySequence.spanTo=spanTo;
    
    //MethodDefinition segment at functions.ceylon (34:4-34:102)
    function segment(from$235,length$236){
        return seq$229.segment(from$235,length$236);
    }
    $$mySequence.segment=segment;
    
    //AttributeGetterDefinition clone at functions.ceylon (35:4-35:59)
    var getClone=function(){
        return $$mySequence;
    }
    $$mySequence.getClone=getClone;
    
    //AttributeGetterDefinition string at functions.ceylon (36:4-36:53)
    var getString=function(){
        return seq$229.getString();
    }
    $$mySequence.getString=getString;
    
    //AttributeGetterDefinition hash at functions.ceylon (37:4-37:50)
    var getHash=function(){
        return seq$229.getHash();
    }
    $$mySequence.getHash=getHash;
    
    //MethodDefinition equals at functions.ceylon (38:4-38:75)
    function equals(other$237){
        return seq$229.equals(other$237);
    }
    $$mySequence.equals=equals;
    
    //AttributeGetterDefinition reversed at functions.ceylon (39:4-39:68)
    var getReversed=function(){
        return seq$229.getReversed();
    }
    $$mySequence.getReversed=getReversed;
    
    //AttributeDeclaration last at functions.ceylon (40:4-40:42)
    var getLast=function(){return seq$229.getLast();};
    $$mySequence.getLast=getLast;
    
    //AttributeDeclaration iterator at functions.ceylon (41:4-41:60)
    var getIterator=function(){return seq$229.getIterator();};
    $$mySequence.getIterator=getIterator;
    
    //AttributeDeclaration size at functions.ceylon (42:4-42:42)
    var getSize=function(){return seq$229.getSize();};
    $$mySequence.getSize=getSize;
    
    //MethodDeclaration contains at functions.ceylon (43:4-43:71)
    var contains=function (other$238){
        return seq$229.contains(other$238);
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
    function f(i$239){
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
    var obj1$240=RefHelper();
    
    //AttributeDeclaration obj2 at functions.ceylon (52:4-52:43)
    var obj2$241=MySequence($$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Element:{t:$$$cl1.String}});
    
    //MethodDefinition tst at functions.ceylon (53:4-55:4)
    function tst$242(x$243){
        return x$243((0));
    };
    $$$c2.check(tst$242((opt$244=obj1$240,$$$cl1.JsCallable(opt$244,opt$244!==null?opt$244.f:null))),$$$cl1.String("Reference to method",19));
    var opt$244;
    $$$c2.check(tst$242((opt$245=obj2$241,$$$cl1.JsCallable(opt$245,opt$245!==null?opt$245.defines:null))),$$$cl1.String("Reference to method from ceylon.language",40));
    var opt$245;
};

//MethodDefinition defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$246,i2$247,i3$248){
    if(i2$247===undefined){i2$247=i1$246.plus((1));}
    if(i3$248===undefined){i3$248=i1$246.plus(i2$247);}
    return $$$cl1.StringBuilder().appendAll([i1$246.getString(),$$$cl1.String(",",1),i2$247.getString(),$$$cl1.String(",",1),i3$248.getString()]).getString();
};

//ClassDefinition DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$249, i2$250, i3$251, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    if(i2$250===undefined){i2$250=i1$249.plus((1));}
    if(i3$251===undefined){i3$251=i1$249.plus(i2$250);}
    
    //AttributeDeclaration s at functions.ceylon (64:4-64:44)
    var s$252=$$$cl1.StringBuilder().appendAll([i1$249.getString(),$$$cl1.String(",",1),i2$250.getString(),$$$cl1.String(",",1),i3$251.getString()]).getString();
    var getS=function(){return s$252;};
    $$defParamTest1.getS=getS;
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
function DefParamTest2(i1$253, i2$254, i3$255, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$253=i1$253;
    if(i2$254===undefined){i2$254=i1$253.plus((1));}
    $$defParamTest2.i2$254=i2$254;
    if(i3$255===undefined){i3$255=i1$253.plus(i2$254);}
    $$defParamTest2.i3$255=i3$255;
    
    //MethodDefinition f at functions.ceylon (67:4-67:55)
    function f(){
        return $$$cl1.StringBuilder().appendAll([i1$253.getString(),$$$cl1.String(",",1),i2$254.getString(),$$$cl1.String(",",1),i3$255.getString()]).getString();
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
    function f(i1$256,i2$257,i3$258){
        if(i2$257===undefined){i2$257=i1$256.plus((1));}
        if(i3$258===undefined){i3$258=i1$256.plus(i2$257);}
        return $$$cl1.StringBuilder().appendAll([i1$256.getString(),$$$cl1.String(",",1),i2$257.getString(),$$$cl1.String(",",1),i3$258.getString()]).getString();
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
    $$$c2.check((i1$259=(1),defParamTest(i1$259,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters named 1",28));
    var i1$259;
    $$$c2.check((i1$260=(1),i2$261=(3),defParamTest(i1$260,i2$261,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters named 2",28));
    var i1$260,i2$261;
    $$$c2.check((i1$262=(1),i3$263=(0),defParamTest(i1$262,undefined,i3$263)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters named 3",28));
    var i1$262,i3$263;
    $$$c2.check(DefParamTest1((1)).getS().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class 1",28));
    $$$c2.check(DefParamTest1((1),(3)).getS().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class 2",28));
    $$$c2.check(DefParamTest1((1),(3),(0)).getS().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class 3",28));
    $$$c2.check((i1$264=(1),DefParamTest1(i1$264,undefined,undefined)).getS().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class named 1",34));
    var i1$264;
    $$$c2.check((i1$265=(1),i2$266=(3),DefParamTest1(i1$265,i2$266,undefined)).getS().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class named 2",34));
    var i1$265,i2$266;
    $$$c2.check((i1$267=(1),i3$268=(0),DefParamTest1(i1$267,undefined,i3$268)).getS().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class named 3",34));
    var i1$267,i3$268;
    $$$c2.check(DefParamTest2((1)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 1",29));
    $$$c2.check(DefParamTest2((1),(3)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 2",29));
    $$$c2.check(DefParamTest2((1),(3),(0)).f().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class2 3",29));
    $$$c2.check((i1$269=(1),DefParamTest2(i1$269,undefined,undefined)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 named 1",35));
    var i1$269;
    $$$c2.check((i1$270=(1),i2$271=(3),DefParamTest2(i1$270,i2$271,undefined)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 named 2",35));
    var i1$270,i2$271;
    $$$c2.check((i1$272=(1),i3$273=(0),DefParamTest2(i1$272,undefined,i3$273)).f().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class2 named 3",35));
    var i1$272,i3$273;
    
    //AttributeDeclaration tst at functions.ceylon (96:4-96:31)
    var tst$274=DefParamTest3();
    $$$c2.check(tst$274.f((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters 1",29));
    $$$c2.check(tst$274.f((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters 2",29));
    $$$c2.check(tst$274.f((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted method parameters 3",29));
    $$$c2.check((i1$275=(1),(opt$276=tst$274,$$$cl1.JsCallable(opt$276,opt$276!==null?opt$276.f:null))(i1$275,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters named 1",35));
    var i1$275,opt$276;
    $$$c2.check((i1$277=(1),i2$278=(3),(opt$279=tst$274,$$$cl1.JsCallable(opt$279,opt$279!==null?opt$279.f:null))(i1$277,i2$278,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters named 2",35));
    var i1$277,i2$278,opt$279;
    $$$c2.check((i1$280=(1),i3$281=(0),(opt$282=tst$274,$$$cl1.JsCallable(opt$282,opt$282!==null?opt$282.f:null))(i1$280,undefined,i3$281)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted method parameters named 3",35));
    var i1$280,i3$281,opt$282;
};

//MethodDefinition testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDefinition GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$283($$getterTest$283){
        $init$GetterTest$283();
        if ($$getterTest$283===undefined)$$getterTest$283=new GetterTest$283.$$;
        
        //AttributeDeclaration i at functions.ceylon (107:4-107:24)
        var i$284=(0);
        var getI$284=function(){return i$284;};
        $$getterTest$283.getI$284=getI$284;
        var setI$284=function(i$285){return i$284=i$285;};
        $$getterTest$283.setI$284=setI$284;
        
        //AttributeGetterDefinition x at functions.ceylon (108:4-108:35)
        var getX=function(){
            return (setI$284(getI$284().getSuccessor()));
        }
        $$getterTest$283.getX=getX;
        return $$getterTest$283;
    }
    function $init$GetterTest$283(){
        if (GetterTest$283.$$===undefined){
            $$$cl1.initTypeProto(GetterTest$283,'functions::testGetterMethodDefinitions.GetterTest',$$$cl1.Basic);
        }
        return GetterTest$283;
    }
    $init$GetterTest$283();
    
    //AttributeDeclaration gt at functions.ceylon (110:2-110:25)
    var gt$286=GetterTest$283();
    $$$c2.check(gt$286.getX().equals((1)),$$$cl1.String("getter defined as method 1",26));
    $$$c2.check(gt$286.getX().equals((2)),$$$cl1.String("getter defined as method 2",26));
    $$$c2.check(gt$286.getX().equals((3)),$$$cl1.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;

//MethodDefinition namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$287,y$288,z$289){
    if(x$287===undefined){x$287=$$$cl1.String("x",1);}
    if(y$288===undefined){y$288=x$287.plus($$$cl1.String("y",1));}
    if(z$289===undefined){z$289=$$$cl1.getEmpty();}
    
    //AttributeDeclaration result at functions.ceylon (117:4-117:40)
    var result$290=x$287.plus($$$cl1.String(",",1)).plus(y$288);
    var setResult$290=function(result$291){return result$290=result$291;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$292 = z$289.getIterator();
    var s$293;while ((s$293=it$292.next())!==$$$cl1.getFinished()){
        (result$290=result$290.plus($$$cl1.String(",",1).plus(s$293)));
    }
    return result$290;
};

//ClassDefinition Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i$294, more$295, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$295===undefined){more$295=$$$cl1.getEmpty();}
    
    //AttributeDeclaration i at functions.ceylon (123:4-123:20)
    var i$296=i$294;
    var getI=function(){return i$296;};
    $$issue105.getI=getI;
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
    $$$c2.check((x$297=$$$cl1.String("a",1),namedArgFunc(x$297,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("a,ay",4)),$$$cl1.String("named arguments 2",17));
    var x$297;
    $$$c2.check((y$298=$$$cl1.String("b",1),namedArgFunc(undefined,y$298,$$$cl1.getEmpty())).equals($$$cl1.String("x,b",3)),$$$cl1.String("named arguments 3",17));
    var y$298;
    $$$c2.check((z$299=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$299)).equals($$$cl1.String("x,xy,c",6)),$$$cl1.String("named arguments 4",17));
    var z$299;
    $$$c2.check((x$300=$$$cl1.String("a",1),y$301=$$$cl1.String("b",1),z$302=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$300,y$301,z$302)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 5",17));
    var x$300,y$301,z$302;
    $$$c2.check((y$303=$$$cl1.String("b",1),x$304=$$$cl1.String("a",1),z$305=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$304,y$303,z$305)).equals($$$cl1.String("a,b,c,d",7)),$$$cl1.String("named arguments 6",17));
    var y$303,x$304,z$305;
    $$$c2.check((x$306=$$$cl1.String("a",1),z$307=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$306,undefined,z$307)).equals($$$cl1.String("a,ay,c",6)),$$$cl1.String("named arguments 7",17));
    var x$306,z$307;
    $$$c2.check((y$308=$$$cl1.String("b",1),z$309=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,y$308,z$309)).equals($$$cl1.String("x,b,c",5)),$$$cl1.String("named arguments 8",17));
    var y$308,z$309;
    $$$c2.check((y$310=$$$cl1.String("b",1),x$311=$$$cl1.String("a",1),namedArgFunc(x$311,y$310,$$$cl1.getEmpty())).equals($$$cl1.String("a,b",3)),$$$cl1.String("named arguments 9",17));
    var y$310,x$311;
    $$$c2.check((z$312=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$312)).equals($$$cl1.String("x,xy,c,d",8)),$$$cl1.String("named arguments 11",18));
    var z$312;
    $$$c2.check((y$313=$$$cl1.String("b",1),z$314=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),x$315=$$$cl1.String("a",1),namedArgFunc(x$315,y$313,z$314)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 12",18));
    var y$313,z$314,x$315;
    
    //AttributeDeclaration issue105 at functions.ceylon (140:4-140:64)
    var issue105$316=(i$317=(1),more$318=$$$cl1.Tuple((i$319=(2),Issue105(i$319,$$$cl1.getEmpty())),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$317,more$318));
    var i$317,more$318,i$319;
    $$$c2.check(issue105$316.getI().equals((1)),$$$cl1.String("issue #105",10));
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
    var x$320=(1000);
    var getX=function(){return x$320;};
    $$lazyExprTest.getX=getX;
    var setX=function(x$321){return x$320=x$321;};
    $$lazyExprTest.setX=setX;
    
    //MethodDeclaration f1 at functions.ceylon (150:4-150:83)
    var f1=function (i$322,f$323){
        if(f$323===undefined){f$323=function (){
            return $$$cl1.StringBuilder().appendAll([i$322.getString(),$$$cl1.String(".",1),($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).getString()]).getString();
        };}
        return $$$cl1.StringBuilder().appendAll([i$322.getString(),$$$cl1.String(":",1),f$323().getString()]).getString();
    };
    $$lazyExprTest.f1=f1;
    
    //MethodDeclaration f2 at functions.ceylon (151:4-151:45)
    var f2=function (i$324){
        return (2).times(($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor()))).plus(i$324);
    };
    $$lazyExprTest.f2=f2;
    
    //AttributeDeclaration i1 at functions.ceylon (152:4-152:28)
    var getI1=function(){return ($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor()));};
    $$lazyExprTest.getI1=getI1;
    
    //AttributeDeclaration i2 at functions.ceylon (153:4-153:21)
    $$lazyExprTest.getI2=function(){
        return ($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).times((2));
    };
    $$lazyExprTest.getS1=function(){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).getString(),$$$cl1.String(".1",2)]).getString();
    };
    $$lazyExprTest.s2=function (i$325){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())).getString(),$$$cl1.String(".",1),i$325.getString()]).getString();
    };
    
    //MethodDeclaration f3 at functions.ceylon (158:4-158:51)
    var f3=function (f$326){
        return f$326(($$lazyExprTest.setX($$lazyExprTest.getX().getSuccessor())));
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
var lx$327=(1000);
var getLx=function(){return lx$327;};
exports.getLx=getLx;
var setLx=function(lx$328){return lx$327=lx$328;};
exports.setLx=setLx;

//MethodDeclaration lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$329,f$330){
    if(f$330===undefined){f$330=function (){
        return $$$cl1.StringBuilder().appendAll([i$329.getString(),$$$cl1.String(".",1),(setLx(getLx().getSuccessor())).getString()]).getString();
    };}
    return f$330();
};

//MethodDeclaration lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$331){
    return (2).times((setLx(getLx().getSuccessor()))).plus(i$331);
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
    var x$332=(1000);
    var getX=function(){return x$332;};
    $$lazyExprTest2.getX=getX;
    var setX=function(x$333){return x$332=x$333;};
    $$lazyExprTest2.setX=setX;
    
    //AttributeDeclaration s1 at functions.ceylon (168:4-168:51)
    var getS1=function(){return ($$lazyExprTest2.setX($$lazyExprTest2.getX().getSuccessor())).getString();};
    $$lazyExprTest2.getS1=getS1;
    
    //MethodDeclaration s2 at functions.ceylon (169:4-169:65)
    var s2=function (i$334){
        return $$$cl1.StringBuilder().appendAll([($$lazyExprTest2.setX($$lazyExprTest2.getX().getSuccessor())).getString(),$$$cl1.String("-",1),i$334.getString()]).getString();
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
    var s1$335=$$$cl1.String("s1",2);
    var getS1=function(){return s1$335;};
    $$lazyExprTest3.getS1=getS1;
    var setS1=function(s1$336){return s1$335=s1$336;};
    $$lazyExprTest3.setS1=setS1;
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
    $$lazyExprTest4.getS1$$functions$LazyExprTest2=$$lazyExprTest4.getS1;
    
    //AttributeDeclaration assigned at functions.ceylon (175:4-175:40)
    var assigned$337=$$$cl1.String("",0);
    var getAssigned=function(){return assigned$337;};
    $$lazyExprTest4.getAssigned=getAssigned;
    var setAssigned=function(assigned$338){return assigned$337=assigned$338;};
    $$lazyExprTest4.setAssigned=setAssigned;
    
    //AttributeGetterDefinition s1 at functions.ceylon (176:4-176:56)
    var getS1=function(){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("s1-",3),$$lazyExprTest4.getS1$$functions$LazyExprTest2().getString()]).getString();
    }
    $$lazyExprTest4.getS1=getS1;
    
    //AttributeSetterDefinition s1 at functions.ceylon (177:4-177:31)
    var setS1=function(s1$339){
        $$lazyExprTest4.setAssigned(s1$339);
    }
    $$lazyExprTest4.setS1=setS1;
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
    var tst$340=LazyExprTest();
    (tst$340.setX((1)));
    $$$c2.check(tst$340.f1((3)).equals($$$cl1.String("3:3.2",5)),$$$cl1.String("=> defaulted param",18));
    $$$c2.check(tst$340.f2((3)).equals((9)),$$$cl1.String("=> method",9));
    $$$c2.check(tst$340.getI1().equals((4)),$$$cl1.String("=> attribute",12));
    $$$c2.check(tst$340.getI2().equals((10)),$$$cl1.String("=> attribute specifier",22));
    $$$c2.check(tst$340.getS1().equals($$$cl1.String("6.1",3)),$$$cl1.String("=> attribute refinement",23));
    $$$c2.check(tst$340.s2((5)).equals($$$cl1.String("7.5",3)),$$$cl1.String("=> method refinement",20));
    setLx((1));
    $$$c2.check(lazy_f1((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param toplevel",27));
    $$$c2.check(lazy_f2((3)).equals((9)),$$$cl1.String("=> method toplevel",18));
    $$$c2.check(getLazy_i1().equals((4)),$$$cl1.String("=> attribute toplevel",21));
    
    //AttributeDeclaration x at functions.ceylon (195:4-195:29)
    var x$341=(1000);
    var setX$341=function(x$342){return x$341=x$342;};
    
    //MethodDeclaration f1 at functions.ceylon (196:4-196:64)
    var f1$343=function (i$344,f$345){
        if(f$345===undefined){f$345=function (){
            return $$$cl1.StringBuilder().appendAll([i$344.getString(),$$$cl1.String(".",1),(x$341=x$341.getSuccessor()).getString()]).getString();
        };}
        return f$345();
    };
    
    //MethodDeclaration f2 at functions.ceylon (197:4-197:38)
    var f2$346=function (i$347){
        return (2).times((x$341=x$341.getSuccessor())).plus(i$347);
    };
    
    //AttributeDeclaration i1 at functions.ceylon (198:4-198:21)
    var getI1$348=function(){return (x$341=x$341.getSuccessor());};
    
    //AttributeDeclaration i2 at functions.ceylon (199:4-199:14)
    var i2$349;
    var getI2$349=function(){
        return (x$341=x$341.getSuccessor()).times((2));
    };
    x$341=(1);
    $$$c2.check(f1$343((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param local",24));
    $$$c2.check(f2$346((3)).equals((9)),$$$cl1.String("=> method local",15));
    $$$c2.check(getI1$348().equals((4)),$$$cl1.String("=> attribute local",18));
    $$$c2.check(getI2$349().equals((10)),$$$cl1.String("=> attribute specifier local",28));
    
    //AttributeDeclaration tst3 at functions.ceylon (208:4-208:32)
    var tst3$350=LazyExprTest3();
    (tst3$350.setX((1)));
    $$$c2.check(tst3$350.getS1().equals($$$cl1.String("s1",2)),$$$cl1.String("=> override variable 1",22));
    (tst3$350.setS1($$$cl1.String("abc",3)));
    $$$c2.check(tst3$350.getS1().equals($$$cl1.String("abc",3)),$$$cl1.String("=> override variable 2",22));
    
    //AttributeDeclaration tst4 at functions.ceylon (213:4-213:32)
    var tst4$351=LazyExprTest4();
    (tst4$351.setX((1)));
    $$$c2.check(tst4$351.getS1().equals($$$cl1.String("s1-2",4)),$$$cl1.String("=> override getter/setter 1",27));
    (tmp$352=tst4$351,tmp$352.setS1($$$cl1.String("abc",3)),tmp$352.getS1());
    var tmp$352;
    $$$c2.check(tst4$351.getS1().equals($$$cl1.String("s1-4",4)),$$$cl1.String("=> override getter/setter 2",27));
    $$$c2.check(tst4$351.getAssigned().equals($$$cl1.String("abc",3)),$$$cl1.String("=> override getter/setter 3",27));
    (tst$340.setX((1)));
    x$341=(10);
    $$$c2.check((i$353=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$341=x$341.getSuccessor());
    }()),(opt$354=tst$340,$$$cl1.JsCallable(opt$354,opt$354!==null?opt$354.f1:null))(i$353,undefined)).equals($$$cl1.String("11:11.2",7)),$$$cl1.String("=> named arg",12));
    var i$353,opt$354;
    $$$c2.check((i$355=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$341=x$341.getSuccessor());
    }()),f$356=function (){return (x$341=x$341.getSuccessor()).getString();},(opt$357=tst$340,$$$cl1.JsCallable(opt$357,opt$357!==null?opt$357.f1:null))(i$355,f$356)).equals($$$cl1.String("12:13",5)),$$$cl1.String("=> named arg function",21));
    var i$355,f$356,opt$357;
    $$$c2.check((f$358=function (i$359){return $$$cl1.StringBuilder().appendAll([i$359.getString(),$$$cl1.String("-",1),(x$341=x$341.getSuccessor()).getString()]).getString();},(opt$360=tst$340,$$$cl1.JsCallable(opt$360,opt$360!==null?opt$360.f3:null))(f$358)).equals($$$cl1.String("3-14",4)),$$$cl1.String("=> named arg function with param",32));
    var f$358,opt$360;
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
    return function(x$361,y$362){
        return x$361.compare(y$362);
    }
};

//MethodDefinition multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$363){
    return function(apat$364){
        return function(amat$365){
            return $$$cl1.StringBuilder().appendAll([nombre$363.getString(),$$$cl1.String(" ",1),apat$364.getString(),$$$cl1.String(" ",1),amat$365.getString()]).getString();
        }
    }
};

//MethodDefinition multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$366){
    if(name$366===undefined){name$366=$$$cl1.String("A",1);}
    return function(apat$367){
        return function(amat$368){
            return $$$cl1.StringBuilder().appendAll([name$366.getString(),$$$cl1.String(" ",1),apat$367.getString(),$$$cl1.String(" ",1),amat$368.getString()]).getString();
        }
    }
};

//MethodDefinition multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$369){
    if(names$369===undefined){names$369=$$$cl1.getEmpty();}
    return function(count$370){
        
        //AttributeDeclaration sb at multiples.ceylon (13:4-13:30)
        var sb$371=$$$cl1.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$372 = names$369.getIterator();
        var name$373;while ((name$373=it$372.next())!==$$$cl1.getFinished()){
            sb$371.append(name$373).append($$$cl1.String(" ",1));
        }
        sb$371.append($$$cl1.String("count ",6)).append(count$370.getString());
        return sb$371.getString();
    }
};

//MethodDefinition testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl1.print($$$cl1.String("Testing multiple parameter lists...",35));
    $$$c2.check(multiCompare()((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 1",15));
    $$$c2.check(multiCompare()((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 2",15));
    $$$c2.check(multiCompare()((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 3",15));
    
    //MethodDeclaration comp at multiples.ceylon (26:4-26:60)
    var comp$374=function (a$375,b$376){
        return multiCompare()(a$375,b$376);
    };
    $$$c2.check(comp$374((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 4",15));
    $$$c2.check(comp$374((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 5",15));
    $$$c2.check(comp$374((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 6",15));
    $$$c2.check(multiFullname($$$cl1.String("a",1))($$$cl1.String("b",1))($$$cl1.String("c",1)).equals($$$cl1.String("a b c",5)),$$$cl1.String("Multi fullname 1",16));
    
    //MethodDeclaration apat at multiples.ceylon (31:4-31:55)
    var apat$377=function (c$378){
        return multiFullname($$$cl1.String("A",1))($$$cl1.String("B",1))(c$378);
    };
    $$$c2.check(apat$377($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi fullname 2",16));
    
    //MethodDeclaration nombre at multiples.ceylon (33:4-33:61)
    var nombre$379=function (name$380){
        return multiFullname($$$cl1.String("Name",4))(name$380);
    };
    $$$c2.check(nombre$379($$$cl1.String("Z",1))($$$cl1.String("L",1)).equals($$$cl1.String("Name Z L",8)),$$$cl1.String("Multi callable 2",16));
    $$$c2.check(multiDefaulted()($$$cl1.String("B",1))($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 1",17));
    
    //AttributeDeclaration md1 at multiples.ceylon (37:4-37:69)
    var md1$381=multiDefaulted();
    $$$c2.check(md1$381($$$cl1.String("B",1))($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 2",17));
    $$$c2.check(md1$381($$$cl1.String("B",1))($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 3",17));
    $$$c2.check(md1$381($$$cl1.String("Z",1))($$$cl1.String("C",1)).equals($$$cl1.String("A Z C",5)),$$$cl1.String("Multi defaulted 4",17));
    $$$c2.check(md1$381($$$cl1.String("Y",1))($$$cl1.String("Z",1)).equals($$$cl1.String("A Y Z",5)),$$$cl1.String("Multi defaulted 5",17));
    
    //MethodDeclaration md2 at multiples.ceylon (42:4-42:52)
    var md2$382=function (x$383){
        return multiDefaulted()($$$cl1.String("B",1))(x$383);
    };
    $$$c2.check(md2$382($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 6",17));
    $$$c2.check(md2$382($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 7",17));
    $$$c2.check(multiSequenced([$$$cl1.String("A",1),$$$cl1.String("B",1),$$$cl1.String("C",1)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}))((1)).equals($$$cl1.String("A B C count 1",13)),$$$cl1.String("Multi sequenced 1",17));
    
    //MethodDeclaration ms1 at multiples.ceylon (46:4-46:59)
    var ms1$384=function (c$385){
        return multiSequenced([$$$cl1.String("x",1),$$$cl1.String("y",1),$$$cl1.String("z",1)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}))(c$385);
    };
    $$$c2.check(ms1$384((5)).equals($$$cl1.String("x y z count 5",13)),$$$cl1.String("Multi sequenced 2",17));
    $$$c2.check(ms1$384((10)).equals($$$cl1.String("x y z count 10",14)),$$$cl1.String("Multi sequenced 3",17));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
