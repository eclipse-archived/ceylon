(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$nm":"f"}},"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$c":{"GetterTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"i"}},"$nm":"GetterTest"}},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"iter"}]],"$mt":"mthd","$nm":"f"}},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$an":{"shared":[]},"$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"more"}],"$mt":"cls","$at":{"more":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$nm":"more"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"seq":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$nm":"seq"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$nm":"f"}},"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"x"}},"$nm":"tst"}},"$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"f"},"callFunction":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"expect"}]],"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"f"}},"$nm":"callFunction"}},"$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"s2"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"MethodRefTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$an":{"shared":[]},"$nm":"name"}],"$mt":"cls","$m":{"suffix":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"suffix"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"kid"}],"$mt":"cls","$an":{"shared":[]},"$m":{"prefix":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"prefix"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"kid":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"kid"}},"$nm":"Inner"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"MethodRefTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testStaticMethodReferences":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testStaticMethodReferences"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"f1"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"f2"}},"$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"nombre":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}]],"$mt":"mthd","$nm":"nombre"},"comp":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$nm":"comp"},"ms1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"}]],"$mt":"mthd","$nm":"ms1"},"md2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$nm":"md2"},"apat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"c"}]],"$mt":"mthd","$nm":"apat"}},"$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"},"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LeaveThereHereForMetamodelTests":{"$mt":"ifc","$an":{"shared":[]},"$nm":"LeaveThereHereForMetamodelTests"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1","$mod-bin":"6.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//MethodDef find at anonymous.ceylon (3:0-10:0)
function find(a$4467,f$4468,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$4469 = a$4467.iterator();
    var e$4470;while ((e$4470=it$4469.next())!==$$$cl4138.getFinished()){
        if(f$4468(e$4470)){
            return e$4470;
        }
    }
    return null;
};find.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Array,a:{Element:'Element'}},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.Boolean},$an:function(){return[];}}],$tp:{Element:{}},d:['functions','find']};};

//MethodDef find2 at anonymous.ceylon (12:0-20:0)
function find2(a$4471,f$4472,$$$mptypes){
    if(f$4472===undefined){f$4472=function (x$4473){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$4474 = a$4471.iterator();
    var e$4475;while ((e$4475=it$4474.next())!==$$$cl4138.getFinished()){
        if(f$4472(e$4475)){
            return e$4475;
        }
    }
    if ($$$cl4138.getFinished() === e$4475){
        return null;
    }
};find2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Array,a:{Element:'Element'}},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl4138.Boolean},$an:function(){return[];}}],$tp:{Element:{}},d:['functions','find2']};};

//MethodDef subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$4476){
    return (function (i$4477){
        return i$4477.minus(howMuch$4476).string;
    });
};subtract.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl4138.Integer}}]},Return:{t:$$$cl4138.String}}},$ps:[{$nm:'howMuch',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','subtract']};};

//MethodDef testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl4138.print($$$cl4138.String("Testing anonymous functions...",30));
    
    //AttributeDecl nums at anonymous.ceylon (28:2-28:31)
    var nums$4478=(elements$4479=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),$$$cl4138.Array(elements$4479,{Element:{t:$$$cl4138.Integer}}));
    var elements$4479;
    
    //AttributeDecl found at anonymous.ceylon (30:2-30:58)
    var found$4480=find(nums$4478,$$$cl4138.$JsCallable((function (i$4481){
        return i$4481.remainder((2)).equals((0));
    }),[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Boolean}}),{Element:{t:$$$cl4138.Integer}});
    function setFound$4480(found$4482){return found$4480=found$4482;};
    var i$4483;
    if((i$4483=found$4480)!==null){
        $$$c4139.check(i$4483.equals((2)),$$$cl4138.String("anonfunc positional",19));
    }else {
        $$$c4139.fail($$$cl4138.String("anonfunc positional",19));
    }
    found$4480=(f$4484=function (i$4485){
        return i$4485.remainder((2)).equals((0));
    },a$4486=nums$4478,find(a$4486,f$4484,{Element:{t:$$$cl4138.Integer}}));
    var f$4484,a$4486;
    var i$4487;
    if((i$4487=found$4480)!==null){
        $$$c4139.check(i$4487.equals((2)),$$$cl4138.String("anonfunc named",14));
    }else {
        $$$c4139.fail($$$cl4138.String("anonfunc named",14));
    }
    
    //MethodDef callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$4488(f$4489,expect$4490){
        $$$c4139.check(f$4489((0)).equals(expect$4490),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("anon func returns ",18),f$4489((0)).string,$$$cl4138.String(" instead of ",12),expect$4490.string]).string);
    };callFunction$4488.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.String},$an:function(){return[];}},{$nm:'expect',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','testAnonymous','$m','callFunction']};};
    
    //MethodDef f at anonymous.ceylon (50:2-52:2)
    function f$4491(i$4492){
        return i$4492.plus((12)).string;
    };f$4491.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','testAnonymous','$m','f']};};
    callFunction$4488($$$cl4138.$JsCallable(f$4491,[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}}),$$$cl4138.String("12",2));
    callFunction$4488($$$cl4138.$JsCallable((function (i$4493){
        return i$4493.times((3)).string;
    }),[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}}),$$$cl4138.String("0",1));
    (expect$4494=$$$cl4138.String("0",1),f$4495=function (i$4496){
        return i$4496.power((2)).string;
    },callFunction$4488(f$4495,expect$4494));
    var expect$4494,f$4495;
    
    //AttributeDecl f2 at anonymous.ceylon (64:2-64:41)
    var f2$4497=$$$cl4138.$JsCallable((function (i$4498){
        return i$4498.minus((10)).string;
    }),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}});
    callFunction$4488($$$cl4138.$JsCallable(f2$4497,[],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}}),$$$cl4138.String("-10",3));
    callFunction$4488($$$cl4138.$JsCallable(subtract((5)),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}}),$$$cl4138.String("-5",2));
    found$4480=find2(nums$4478,$$$cl4138.$JsCallable((function (i$4499){
        return i$4499.compare((2)).equals($$$cl4138.getLarger());
    }),[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Boolean}}),{Element:{t:$$$cl4138.Integer}});
    var i$4500;
    if((i$4500=found$4480)!==null){
        $$$c4139.check(i$4500.equals((3)),$$$cl4138.String("anonfunc i>2 [1]",16));
    }else {
        $$$c4139.fail($$$cl4138.String("anonfunc i>2 [2]",16));
    }
    found$4480=find2(nums$4478,undefined,{Element:{t:$$$cl4138.Integer}});
    var i$4501;
    if((i$4501=found$4480)!==null){
        $$$c4139.check(i$4501.equals((1)),$$$cl4138.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c4139.fail($$$cl4138.String("anonfunc defaulted param [2]",28));
    }
};testAnonymous.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testAnonymous']};};

//InterfaceDef LeaveThereHereForMetamodelTests at anonymous.ceylon (78:0-78:49)
function LeaveThereHereForMetamodelTests($$leaveThereHereForMetamodelTests){
}
LeaveThereHereForMetamodelTests.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl4138.shared()];},d:['functions','LeaveThereHereForMetamodelTests']};};
exports.LeaveThereHereForMetamodelTests=LeaveThereHereForMetamodelTests;
function $init$LeaveThereHereForMetamodelTests(){
    if (LeaveThereHereForMetamodelTests.$$===undefined){
        $$$cl4138.initTypeProtoI(LeaveThereHereForMetamodelTests,'functions::LeaveThereHereForMetamodelTests');
    }
    return LeaveThereHereForMetamodelTests;
}
exports.$init$LeaveThereHereForMetamodelTests=$init$LeaveThereHereForMetamodelTests;
$init$LeaveThereHereForMetamodelTests();

//MethodDef helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl4138.print($$$cl4138.String("hello world",11));
}
exports.helloWorld=helloWorld;
helloWorld.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['functions','helloWorld']};};

//MethodDef hello at functions.ceylon (7:0-9:0)
function hello(name$4502){
    $$$cl4138.print($$$cl4138.String("hello",5).plus(name$4502));
}
exports.hello=hello;
hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['functions','hello']};};

//MethodDef helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$4503){
    if(names$4503===undefined){names$4503=$$$cl4138.getEmpty();}
}
exports.helloAll=helloAll;
helloAll.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.String}}},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['functions','helloAll']};};

//MethodDef toString at functions.ceylon (13:0-15:0)
function toString(obj$4504){
    return obj$4504.string;
}
exports.toString=toString;
toString.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'obj',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['functions','toString']};};

//MethodDef add at functions.ceylon (17:0-19:0)
function add(x$4505,y$4506){
    return x$4505.plus(y$4506);
}
exports.add=add;
add.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Float},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Float},$an:function(){return[];}},{$nm:'y',$mt:'prm',$t:{t:$$$cl4138.Float},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['functions','add']};};

//MethodDef repeat at functions.ceylon (21:0-23:0)
function repeat(times$4507,f$4508){
    f$4508((0));
}
exports.repeat=repeat;
repeat.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'times',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.Anything},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['functions','repeat']};};

//ClassDef MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$4509, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl4138.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$4509_=seq$4509;
    $$$cl4138.Sequence($$mySequence.$$targs$$===undefined?$$targs$$:{Element:$$mySequence.$$targs$$.Element},$$mySequence);
    $$$cl4138.defineAttr($$mySequence,'seq$4509',function(){return this.seq$4509_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequence,a:{Element:'Element'}},$cont:MySequence,d:['functions','MySequence','$at','seq']};});
    
    //AttributeGetterDef lastIndex at functions.ceylon (27:4-27:60)
    $$$cl4138.defineAttr($$mySequence,'lastIndex',function(){
        return seq$4509.lastIndex;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','lastIndex']};});
    
    //AttributeGetterDef first at functions.ceylon (28:4-28:52)
    $$$cl4138.defineAttr($$mySequence,'first',function(){
        return seq$4509.first;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','first']};});
    
    //AttributeGetterDef rest at functions.ceylon (29:4-29:52)
    $$$cl4138.defineAttr($$mySequence,'rest',function(){
        return seq$4509.rest;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:'Element'}},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','rest']};});
    
    //MethodDef get at functions.ceylon (30:4-30:67)
    function $get(index$4510){
        return seq$4509.$get(index$4510);
    }
    $$mySequence.$get=$get;
    $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},'Element']},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','get']};};
    
    //MethodDef span at functions.ceylon (31:4-31:88)
    function span(from$4511,to$4512){
        return seq$4509.span(from$4511,to$4512);
    }
    $$mySequence.span=span;
    span.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'to',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','span']};};
    
    //MethodDef spanFrom at functions.ceylon (32:4-32:80)
    function spanFrom(from$4513){
        return seq$4509.spanFrom(from$4513);
    }
    $$mySequence.spanFrom=spanFrom;
    spanFrom.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','spanFrom']};};
    
    //MethodDef spanTo at functions.ceylon (33:4-33:72)
    function spanTo(to$4514){
        return seq$4509.spanTo(to$4514);
    }
    $$mySequence.spanTo=spanTo;
    spanTo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:'Element'}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','spanTo']};};
    
    //MethodDef segment at functions.ceylon (34:4-34:102)
    function segment(from$4515,length$4516){
        return seq$4509.segment(from$4515,length$4516);
    }
    $$mySequence.segment=segment;
    segment.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'length',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','segment']};};
    
    //AttributeGetterDef clone at functions.ceylon (35:4-35:59)
    $$$cl4138.defineAttr($$mySequence,'clone',function(){
        return $$mySequence;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:MySequence,a:{Element:'Element'}},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','clone']};});
    
    //AttributeGetterDef string at functions.ceylon (36:4-36:53)
    $$$cl4138.defineAttr($$mySequence,'string',function(){
        return seq$4509.string;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','string']};});
    
    //AttributeGetterDef hash at functions.ceylon (37:4-37:50)
    $$$cl4138.defineAttr($$mySequence,'hash',function(){
        return seq$4509.hash;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','hash']};});
    
    //MethodDef equals at functions.ceylon (38:4-38:75)
    function equals(other$4517){
        return seq$4509.equals(other$4517);
    }
    $$mySequence.equals=equals;
    equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','equals']};};
    
    //AttributeGetterDef reversed at functions.ceylon (39:4-39:68)
    $$$cl4138.defineAttr($$mySequence,'reversed',function(){
        return seq$4509.reversed;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequence,a:{Element:'Element'}},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','reversed']};});
    
    //AttributeDecl last at functions.ceylon (40:4-40:42)
    $$$cl4138.defineAttr($$mySequence,'last',function(){return seq$4509.last;},undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','last']};});
    $$mySequence.$prop$getLast={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','last']};}};
    $$mySequence.$prop$getLast.get=function(){return last};
    
    //MethodDecl iterator at functions.ceylon (41:4-41:64)
    var iterator=function (){
        return seq$4509.iterator();
    };
    iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterator,a:{Element:'Element'}},$ps:[],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','iterator']};};
    $$mySequence.iterator=iterator;
    
    //AttributeDecl size at functions.ceylon (42:4-42:42)
    $$$cl4138.defineAttr($$mySequence,'size',function(){return seq$4509.size;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','size']};});
    $$mySequence.$prop$getSize={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$at','size']};}};
    $$mySequence.$prop$getSize.get=function(){return size};
    
    //MethodDecl contains at functions.ceylon (43:4-43:71)
    var contains=function (other$4518){
        return seq$4509.contains(other$4518);
    };
    contains.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MySequence','$m','contains']};};
    $$mySequence.contains=contains;
    return $$mySequence;
}
MySequence.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'seq',$mt:'prm',$t:{t:$$$cl4138.Sequence,a:{Element:'Element'}},$an:function(){return[];}}],$tp:{Element:{'var':'out'}},satisfies:[{t:$$$cl4138.Sequence,a:{Element:'Element'}}],d:['functions','MySequence']};};
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl4138.initTypeProto(MySequence,'functions::MySequence',$$$cl4138.Basic,$$$cl4138.Sequence);
    }
    return MySequence;
}
exports.$init$MySequence=$init$MySequence;
$init$MySequence();

//ClassDef RefHelper at functions.ceylon (46:0-48:0)
function RefHelper($$refHelper){
    $init$RefHelper();
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    
    //MethodDef f at functions.ceylon (47:4-47:47)
    function f(i$4519){
        return true;
    }
    $$refHelper.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:RefHelper,$an:function(){return[$$$cl4138.shared()];},d:['functions','RefHelper','$m','f']};};
    return $$refHelper;
}
RefHelper.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['functions','RefHelper']};};
function $init$RefHelper(){
    if (RefHelper.$$===undefined){
        $$$cl4138.initTypeProto(RefHelper,'functions::RefHelper',$$$cl4138.Basic);
    }
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDef testMethodReference at functions.ceylon (50:0-58:0)
function testMethodReference(){
    
    //AttributeDecl obj1 at functions.ceylon (51:4-51:28)
    var obj1$4520=RefHelper();
    
    //AttributeDecl obj2 at functions.ceylon (52:4-52:43)
    var obj2$4521=MySequence($$$cl4138.Tuple($$$cl4138.String("hi",2),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Element:{t:$$$cl4138.String}});
    
    //MethodDef tst at functions.ceylon (53:4-55:4)
    function tst$4522(x$4523){
        return x$4523((0));
    };tst$4522.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'x',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.Boolean},$an:function(){return[];}}],d:['functions','testMethodReference','$m','tst']};};
    $$$c4139.check(tst$4522($$$cl4138.$JsCallable((opt$4524=obj1$4520,$$$cl4138.JsCallable(opt$4524,opt$4524!==null?opt$4524.f:null)),[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Boolean}})),$$$cl4138.String("Reference to method",19));
    var opt$4524;
    $$$c4139.check(tst$4522($$$cl4138.$JsCallable((opt$4525=obj2$4521,$$$cl4138.JsCallable(opt$4525,opt$4525!==null?opt$4525.defines:null)),[{$nm:'index',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Boolean}})),$$$cl4138.String("Reference to method from ceylon.language",40));
    var opt$4525;
};testMethodReference.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testMethodReference']};};

//MethodDef defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$4526,i2$4527,i3$4528){
    if(i2$4527===undefined){i2$4527=i1$4526.plus((1));}
    if(i3$4528===undefined){i3$4528=i1$4526.plus(i2$4527);}
    return $$$cl4138.StringBuilder().appendAll([i1$4526.string,$$$cl4138.String(",",1),i2$4527.string,$$$cl4138.String(",",1),i3$4528.string]).string;
};defParamTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','defParamTest']};};

//ClassDef DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$4529, i2$4530, i3$4531, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    $$defParamTest1.i1$4529_=i1$4529;
    if(i2$4530===undefined){i2$4530=i1$4529.plus((1));}
    $$defParamTest1.i2$4530_=i2$4530;
    if(i3$4531===undefined){i3$4531=i1$4529.plus(i2$4530);}
    $$defParamTest1.i3$4531_=i3$4531;
    $$$cl4138.defineAttr($$defParamTest1,'i1$4529',function(){return this.i1$4529_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:DefParamTest1,d:['functions','DefParamTest1','$at','i1']};});
    $$$cl4138.defineAttr($$defParamTest1,'i2$4530',function(){return this.i2$4530_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:DefParamTest1,d:['functions','DefParamTest1','$at','i2']};});
    $$$cl4138.defineAttr($$defParamTest1,'i3$4531',function(){return this.i3$4531_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:DefParamTest1,d:['functions','DefParamTest1','$at','i3']};});
    
    //AttributeDecl s at functions.ceylon (64:4-64:44)
    var s=$$$cl4138.StringBuilder().appendAll([i1$4529.string,$$$cl4138.String(",",1),i2$4530.string,$$$cl4138.String(",",1),i3$4531.string]).string;
    $$$cl4138.defineAttr($$defParamTest1,'s',function(){return s;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:DefParamTest1,$an:function(){return[$$$cl4138.shared()];},d:['functions','DefParamTest1','$at','s']};});
    $$defParamTest1.$prop$getS={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:DefParamTest1,$an:function(){return[$$$cl4138.shared()];},d:['functions','DefParamTest1','$at','s']};}};
    $$defParamTest1.$prop$getS.get=function(){return s};
    return $$defParamTest1;
}
DefParamTest1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','DefParamTest1']};};
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl4138.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl4138.Basic);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDef DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$4532, i2$4533, i3$4534, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$4532_=i1$4532;
    if(i2$4533===undefined){i2$4533=i1$4532.plus((1));}
    $$defParamTest2.i2$4533_=i2$4533;
    if(i3$4534===undefined){i3$4534=i1$4532.plus(i2$4533);}
    $$defParamTest2.i3$4534_=i3$4534;
    $$$cl4138.defineAttr($$defParamTest2,'i1$4532',function(){return this.i1$4532_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:DefParamTest2,d:['functions','DefParamTest2','$at','i1']};});
    $$$cl4138.defineAttr($$defParamTest2,'i2$4533',function(){return this.i2$4533_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:DefParamTest2,d:['functions','DefParamTest2','$at','i2']};});
    $$$cl4138.defineAttr($$defParamTest2,'i3$4534',function(){return this.i3$4534_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:DefParamTest2,d:['functions','DefParamTest2','$at','i3']};});
    
    //MethodDef f at functions.ceylon (67:4-67:55)
    function f(){
        return $$$cl4138.StringBuilder().appendAll([i1$4532.string,$$$cl4138.String(",",1),i2$4533.string,$$$cl4138.String(",",1),i3$4534.string]).string;
    }
    $$defParamTest2.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:DefParamTest2,$an:function(){return[$$$cl4138.shared()];},d:['functions','DefParamTest2','$m','f']};};
    return $$defParamTest2;
}
DefParamTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','DefParamTest2']};};
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl4138.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl4138.Basic);
    }
    return DefParamTest2;
}
exports.$init$DefParamTest2=$init$DefParamTest2;
$init$DefParamTest2();

//ClassDef DefParamTest3 at functions.ceylon (69:0-73:0)
function DefParamTest3($$defParamTest3){
    $init$DefParamTest3();
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    
    //MethodDef f at functions.ceylon (70:4-72:4)
    $$defParamTest3.f$defs$i2=function(i1$4535,i2$4536,i3$4537){return i1$4535.plus((1));};
    $$defParamTest3.f$defs$i3=function(i1$4535,i2$4536,i3$4537){return i1$4535.plus(i2$4536);};
    function f(i1$4535,i2$4536,i3$4537){
        if(i2$4536===undefined){i2$4536=$$defParamTest3.f$defs$i2(i1$4535,i2$4536,i3$4537);}
        if(i3$4537===undefined){i3$4537=$$defParamTest3.f$defs$i3(i1$4535,i2$4536,i3$4537);}
        return $$$cl4138.StringBuilder().appendAll([i1$4535.string,$$$cl4138.String(",",1),i2$4536.string,$$$cl4138.String(",",1),i3$4537.string]).string;
    }
    $$defParamTest3.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:DefParamTest3,$an:function(){return[$$$cl4138.shared()];},d:['functions','DefParamTest3','$m','f']};};
    return $$defParamTest3;
}
DefParamTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['functions','DefParamTest3']};};
function $init$DefParamTest3(){
    if (DefParamTest3.$$===undefined){
        $$$cl4138.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl4138.Basic);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDef testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c4139.check(defParamTest((1)).equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted parameters 1",22));
    $$$c4139.check(defParamTest((1),(3)).equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted parameters 2",22));
    $$$c4139.check(defParamTest((1),(3),(0)).equals($$$cl4138.String("1,3,0",5)),$$$cl4138.String("defaulted parameters 3",22));
    $$$c4139.check((i1$4538=(1),defParamTest(i1$4538,undefined,undefined)).equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted parameters named 1",28));
    var i1$4538;
    $$$c4139.check((i1$4539=(1),i2$4540=(3),defParamTest(i1$4539,i2$4540,undefined)).equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted parameters named 2",28));
    var i1$4539,i2$4540;
    $$$c4139.check((i1$4541=(1),i3$4542=(0),defParamTest(i1$4541,undefined,i3$4542)).equals($$$cl4138.String("1,2,0",5)),$$$cl4138.String("defaulted parameters named 3",28));
    var i1$4541,i3$4542;
    $$$c4139.check(DefParamTest1((1)).s.equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted parameters class 1",28));
    $$$c4139.check(DefParamTest1((1),(3)).s.equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted parameters class 2",28));
    $$$c4139.check(DefParamTest1((1),(3),(0)).s.equals($$$cl4138.String("1,3,0",5)),$$$cl4138.String("defaulted parameters class 3",28));
    $$$c4139.check((i1$4543=(1),DefParamTest1(i1$4543,undefined,undefined)).s.equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted parameters class named 1",34));
    var i1$4543;
    $$$c4139.check((i1$4544=(1),i2$4545=(3),DefParamTest1(i1$4544,i2$4545,undefined)).s.equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted parameters class named 2",34));
    var i1$4544,i2$4545;
    $$$c4139.check((i1$4546=(1),i3$4547=(0),DefParamTest1(i1$4546,undefined,i3$4547)).s.equals($$$cl4138.String("1,2,0",5)),$$$cl4138.String("defaulted parameters class named 3",34));
    var i1$4546,i3$4547;
    $$$c4139.check(DefParamTest2((1)).f().equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted parameters class2 1",29));
    $$$c4139.check(DefParamTest2((1),(3)).f().equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted parameters class2 2",29));
    $$$c4139.check(DefParamTest2((1),(3),(0)).f().equals($$$cl4138.String("1,3,0",5)),$$$cl4138.String("defaulted parameters class2 3",29));
    $$$c4139.check((i1$4548=(1),DefParamTest2(i1$4548,undefined,undefined)).f().equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted parameters class2 named 1",35));
    var i1$4548;
    $$$c4139.check((i1$4549=(1),i2$4550=(3),DefParamTest2(i1$4549,i2$4550,undefined)).f().equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted parameters class2 named 2",35));
    var i1$4549,i2$4550;
    $$$c4139.check((i1$4551=(1),i3$4552=(0),DefParamTest2(i1$4551,undefined,i3$4552)).f().equals($$$cl4138.String("1,2,0",5)),$$$cl4138.String("defaulted parameters class2 named 3",35));
    var i1$4551,i3$4552;
    
    //AttributeDecl tst at functions.ceylon (96:4-96:31)
    var tst$4553=DefParamTest3();
    $$$c4139.check(tst$4553.f((1)).equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted method parameters 1",29));
    $$$c4139.check(tst$4553.f((1),(3)).equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted method parameters 2",29));
    $$$c4139.check(tst$4553.f((1),(3),(0)).equals($$$cl4138.String("1,3,0",5)),$$$cl4138.String("defaulted method parameters 3",29));
    $$$c4139.check((i1$4554=(1),(opt$4555=tst$4553,$$$cl4138.JsCallable(opt$4555,opt$4555!==null?opt$4555.f:null))(i1$4554,undefined,undefined)).equals($$$cl4138.String("1,2,3",5)),$$$cl4138.String("defaulted method parameters named 1",35));
    var i1$4554,opt$4555;
    $$$c4139.check((i1$4556=(1),i2$4557=(3),(opt$4558=tst$4553,$$$cl4138.JsCallable(opt$4558,opt$4558!==null?opt$4558.f:null))(i1$4556,i2$4557,undefined)).equals($$$cl4138.String("1,3,4",5)),$$$cl4138.String("defaulted method parameters named 2",35));
    var i1$4556,i2$4557,opt$4558;
    $$$c4139.check((i1$4559=(1),i3$4560=(0),(opt$4561=tst$4553,$$$cl4138.JsCallable(opt$4561,opt$4561!==null?opt$4561.f:null))(i1$4559,undefined,i3$4560)).equals($$$cl4138.String("1,2,0",5)),$$$cl4138.String("defaulted method parameters named 3",35));
    var i1$4559,i3$4560,opt$4561;
};testDefaultedParams.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testDefaultedParams']};};

//MethodDef testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDef GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$4562($$getterTest$4562){
        $init$GetterTest$4562();
        if ($$getterTest$4562===undefined)$$getterTest$4562=new GetterTest$4562.$$;
        
        //AttributeDecl i at functions.ceylon (107:4-107:24)
        var i$4563=(0);
        $$$cl4138.defineAttr($$getterTest$4562,'i$4563',function(){return i$4563;},function(i$4564){return i$4563=i$4564;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:GetterTest$4562,$an:function(){return[$$$cl4138.variable()];},d:['functions','testGetterMethodDefinitions','$c','GetterTest','$at','i']};});
        $$getterTest$4562.$prop$getI$4563={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:GetterTest$4562,$an:function(){return[$$$cl4138.variable()];},d:['functions','testGetterMethodDefinitions','$c','GetterTest','$at','i']};}};
        $$getterTest$4562.$prop$getI$4563.get=function(){return i$4563};
        
        //AttributeGetterDef x at functions.ceylon (108:4-108:35)
        $$$cl4138.defineAttr($$getterTest$4562,'x',function(){
            return (i$4563=i$4563.successor);
        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:GetterTest$4562,$an:function(){return[$$$cl4138.shared()];},d:['functions','testGetterMethodDefinitions','$c','GetterTest','$at','x']};});
        return $$getterTest$4562;
    }
    GetterTest$4562.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['functions','testGetterMethodDefinitions','$c','GetterTest']};};
    function $init$GetterTest$4562(){
        if (GetterTest$4562.$$===undefined){
            $$$cl4138.initTypeProto(GetterTest$4562,'functions::testGetterMethodDefinitions.GetterTest',$$$cl4138.Basic);
        }
        return GetterTest$4562;
    }
    $init$GetterTest$4562();
    
    //AttributeDecl gt at functions.ceylon (110:2-110:25)
    var gt$4565=GetterTest$4562();
    $$$c4139.check(gt$4565.x.equals((1)),$$$cl4138.String("getter defined as method 1",26));
    $$$c4139.check(gt$4565.x.equals((2)),$$$cl4138.String("getter defined as method 2",26));
    $$$c4139.check(gt$4565.x.equals((3)),$$$cl4138.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;
testGetterMethodDefinitions.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['functions','testGetterMethodDefinitions']};};

//MethodDef namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$4566,y$4567,z$4568){
    if(x$4566===undefined){x$4566=$$$cl4138.String("x",1);}
    if(y$4567===undefined){y$4567=x$4566.plus($$$cl4138.String("y",1));}
    if(z$4568===undefined){z$4568=$$$cl4138.getEmpty();}
    
    //AttributeDecl result at functions.ceylon (117:4-117:40)
    var result$4569=x$4566.plus($$$cl4138.String(",",1)).plus(y$4567);
    function setResult$4569(result$4570){return result$4569=result$4570;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$4571 = z$4568.iterator();
    var s$4572;while ((s$4572=it$4571.next())!==$$$cl4138.getFinished()){
        (result$4569=result$4569.plus($$$cl4138.String(",",1).plus(s$4572)));
    }
    return result$4569;
};namedArgFunc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$def:1,$t:{t:$$$cl4138.String},$an:function(){return[];}},{$nm:'y',$mt:'prm',$def:1,$t:{t:$$$cl4138.String},$an:function(){return[];}},{$nm:'z',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.String}}},$an:function(){return[];}}],d:['functions','namedArgFunc']};};

//ClassDef Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i, more$4573, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$4573===undefined){more$4573=$$$cl4138.getEmpty();}
    $$issue105.more$4573_=more$4573;
    $$$cl4138.defineAttr($$issue105,'more$4573',function(){return this.more$4573_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:Issue105}}},$cont:Issue105,d:['functions','Issue105','$at','more']};});
    
    //AttributeDecl i at functions.ceylon (123:4-123:20)
    var i=i;
    $$$cl4138.defineAttr($$issue105,'i',function(){return i;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue105,$an:function(){return[$$$cl4138.shared()];},d:['functions','Issue105','$at','i']};});
    $$issue105.$prop$getI={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Issue105,$an:function(){return[$$$cl4138.shared()];},d:['functions','Issue105','$at','i']};}};
    $$issue105.$prop$getI.get=function(){return i};
    return $$issue105;
}
Issue105.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[$$$cl4138.shared()];}},{$nm:'more',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:Issue105}}},$an:function(){return[];}}],d:['functions','Issue105']};};
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl4138.initTypeProto(Issue105,'functions::Issue105',$$$cl4138.Basic);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDef testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c4139.check((namedArgFunc(undefined,undefined,$$$cl4138.getEmpty())).equals($$$cl4138.String("x,xy",4)),$$$cl4138.String("named arguments 1",17));
    $$$c4139.check((x$4574=$$$cl4138.String("a",1),namedArgFunc(x$4574,undefined,$$$cl4138.getEmpty())).equals($$$cl4138.String("a,ay",4)),$$$cl4138.String("named arguments 2",17));
    var x$4574;
    $$$c4139.check((y$4575=$$$cl4138.String("b",1),namedArgFunc(undefined,y$4575,$$$cl4138.getEmpty())).equals($$$cl4138.String("x,b",3)),$$$cl4138.String("named arguments 3",17));
    var y$4575;
    $$$c4139.check((z$4576=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),namedArgFunc(undefined,undefined,z$4576)).equals($$$cl4138.String("x,xy,c",6)),$$$cl4138.String("named arguments 4",17));
    var z$4576;
    $$$c4139.check((x$4577=$$$cl4138.String("a",1),y$4578=$$$cl4138.String("b",1),z$4579=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),namedArgFunc(x$4577,y$4578,z$4579)).equals($$$cl4138.String("a,b,c",5)),$$$cl4138.String("named arguments 5",17));
    var x$4577,y$4578,z$4579;
    $$$c4139.check((y$4580=$$$cl4138.String("b",1),x$4581=$$$cl4138.String("a",1),z$4582=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.Tuple($$$cl4138.String("d",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),namedArgFunc(x$4581,y$4580,z$4582)).equals($$$cl4138.String("a,b,c,d",7)),$$$cl4138.String("named arguments 6",17));
    var y$4580,x$4581,z$4582;
    $$$c4139.check((x$4583=$$$cl4138.String("a",1),z$4584=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),namedArgFunc(x$4583,undefined,z$4584)).equals($$$cl4138.String("a,ay,c",6)),$$$cl4138.String("named arguments 7",17));
    var x$4583,z$4584;
    $$$c4139.check((y$4585=$$$cl4138.String("b",1),z$4586=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),namedArgFunc(undefined,y$4585,z$4586)).equals($$$cl4138.String("x,b,c",5)),$$$cl4138.String("named arguments 8",17));
    var y$4585,z$4586;
    $$$c4139.check((y$4587=$$$cl4138.String("b",1),x$4588=$$$cl4138.String("a",1),namedArgFunc(x$4588,y$4587,$$$cl4138.getEmpty())).equals($$$cl4138.String("a,b",3)),$$$cl4138.String("named arguments 9",17));
    var y$4587,x$4588;
    $$$c4139.check((z$4589=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.Tuple($$$cl4138.String("d",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),namedArgFunc(undefined,undefined,z$4589)).equals($$$cl4138.String("x,xy,c,d",8)),$$$cl4138.String("named arguments 11",18));
    var z$4589;
    $$$c4139.check((y$4590=$$$cl4138.String("b",1),z$4591=$$$cl4138.Tuple($$$cl4138.String("c",1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),x$4592=$$$cl4138.String("a",1),namedArgFunc(x$4592,y$4590,z$4591)).equals($$$cl4138.String("a,b,c",5)),$$$cl4138.String("named arguments 12",18));
    var y$4590,z$4591,x$4592;
    
    //AttributeDecl issue105 at functions.ceylon (140:4-140:64)
    var issue105$4593=(i$4594=(1),more$4595=$$$cl4138.Tuple((i$4596=(2),Issue105(i$4596,$$$cl4138.getEmpty())),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$4594,more$4595));
    var i$4594,more$4595,i$4596;
    $$$c4139.check(issue105$4593.i.equals((1)),$$$cl4138.String("issue #105",10));
};testNamedArguments.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testNamedArguments']};};

//InterfaceDef LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
    $$lazyExprBase.$prop$getS1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprBase,$an:function(){return[$$$cl4138.shared(),$$$cl4138.formal()];},d:['functions','LazyExprBase','$at','s1']};}};
}
LazyExprBase.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['functions','LazyExprBase']};};
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl4138.initTypeProtoI(LazyExprBase,'functions::LazyExprBase');
    }
    return LazyExprBase;
}
exports.$init$LazyExprBase=$init$LazyExprBase;
$init$LazyExprBase();

//ClassDef LazyExprTest at functions.ceylon (148:0-159:0)
function LazyExprTest($$lazyExprTest){
    $init$LazyExprTest();
    if ($$lazyExprTest===undefined)$$lazyExprTest=new LazyExprTest.$$;
    LazyExprBase($$lazyExprTest);
    
    //AttributeDecl x at functions.ceylon (149:4-149:36)
    var x=(1000);
    $$$cl4138.defineAttr($$lazyExprTest,'x',function(){return x;},function(x$4597){return x=x$4597;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['functions','LazyExprTest','$at','x']};});
    $$lazyExprTest.$prop$getX={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['functions','LazyExprTest','$at','x']};}};
    $$lazyExprTest.$prop$getX.get=function(){return x};
    
    //MethodDecl f1 at functions.ceylon (150:4-150:83)
    $$lazyExprTest.f1$defs$f=function(i$4598,f$4599){return function (){
        return $$$cl4138.StringBuilder().appendAll([i$4598.string,$$$cl4138.String(".",1),$$lazyExprTest.x.plus((1)).string]).string;
    };};
    var f1=function (i$4598,f$4599){
        if(f$4599===undefined){f$4599=$$lazyExprTest.f1$defs$f(i$4598,f$4599);}
        return $$$cl4138.StringBuilder().appendAll([i$4598.string,$$$cl4138.String(":",1),f$4599().string]).string;
    };
    f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl4138.String},$an:function(){return[];}}],$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','LazyExprTest','$m','f1']};};
    $$lazyExprTest.f1=f1;
    
    //MethodDecl f2 at functions.ceylon (151:4-151:45)
    var f2=function (i$4600){
        return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$4600);
    };
    f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','LazyExprTest','$m','f2']};};
    $$lazyExprTest.f2=f2;
    
    //AttributeDecl i1 at functions.ceylon (152:4-152:28)
    $$$cl4138.defineAttr($$lazyExprTest,'i1',function(){return ($$lazyExprTest.x=$$lazyExprTest.x.successor);},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','LazyExprTest','$at','i1']};});
    $$lazyExprTest.$prop$getI1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','LazyExprTest','$at','i1']};}};
    $$lazyExprTest.$prop$getI1.get=function(){return i1};
    
    //AttributeDecl i2 at functions.ceylon (153:4-153:21)
    $$lazyExprTest.$prop$getI2={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','LazyExprTest','$at','i2']};}};
    $$$cl4138.defineAttr($$lazyExprTest,'i2',function(){
        return ($$lazyExprTest.x=$$lazyExprTest.x.successor).times((2));
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','LazyExprTest','$at','i2']};});
    $$$cl4138.defineAttr($$lazyExprTest,'s1',function(){
        return $$$cl4138.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl4138.String(".1",2)]).string;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','LazyExprTest','$at','s1']};});
    $$lazyExprTest.s2=function (i$4601){
        return $$$cl4138.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl4138.String(".",1),i$4601.string]).string;
    };
    
    //MethodDecl f3 at functions.ceylon (158:4-158:51)
    var f3=function (f$4602){
        return f$4602(($$lazyExprTest.x=$$lazyExprTest.x.successor));
    };
    f3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$cont:LazyExprTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','LazyExprTest','$m','f3']};};
    $$lazyExprTest.f3=f3;
    return $$lazyExprTest;
}
LazyExprTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],satisfies:[{t:LazyExprBase}],d:['functions','LazyExprTest']};};
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl4138.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl4138.Basic,$init$LazyExprBase());
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDecl lx at functions.ceylon (161:0-161:26)
var lx$4603;function $valinit$lx$4603(){if (lx$4603===undefined)lx$4603=(1000);return lx$4603;};$valinit$lx$4603();
function getLx(){return $valinit$lx$4603();}
exports.getLx=getLx;
function setLx(lx$4604){return lx$4603=lx$4604;};
exports.setLx=setLx;
var $prop$getLx={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$an:function(){return[$$$cl4138.variable()];},d:['functions','lx']};}};
exports.$prop$getLx=$prop$getLx;
$prop$getLx.get=getLx;
getLx.$$metamodel$$=$prop$getLx.$$metamodel$$;
$prop$getLx.set=setLx;
if (setLx.$$metamodel$$===undefined)setLx.$$metamodel$$=$prop$getLx.$$metamodel$$;

//MethodDecl lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$4605,f$4606){
    if(f$4606===undefined){f$4606=function (){
        return $$$cl4138.StringBuilder().appendAll([i$4605.string,$$$cl4138.String(".",1),getLx().plus((1)).string]).string;
    };}
    return f$4606();
};
lazy_f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','lazy_f1']};};

//MethodDecl lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$4607){
    return (2).times((setLx(getLx().successor))).plus(i$4607);
};
lazy_f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','lazy_f2']};};

//AttributeDecl lazy_i1 at functions.ceylon (164:0-164:23)
function getLazy_i1(){return (setLx(getLx().successor));};
exports.getLazy_i1=getLazy_i1;
var $prop$getLazy_i1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['functions','lazy_i1']};}};
exports.$prop$getLazy_i1=$prop$getLazy_i1;
$prop$getLazy_i1.get=getLazy_i1;
getLazy_i1.$$metamodel$$=$prop$getLazy_i1.$$metamodel$$;

//ClassDef LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDecl x at functions.ceylon (167:4-167:35)
    var x=(1000);
    $$$cl4138.defineAttr($$lazyExprTest2,'x',function(){return x;},function(x$4608){return x=x$4608;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:LazyExprTest2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['functions','LazyExprTest2','$at','x']};});
    $$lazyExprTest2.$prop$getX.get=function(){return x};
    
    //AttributeDecl s1 at functions.ceylon (168:4-168:51)
    $$$cl4138.defineAttr($$lazyExprTest2,'s1',function(){return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprTest2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['functions','LazyExprTest2','$at','s1']};});
    $$lazyExprTest2.$prop$getS1.get=function(){return s1};
    
    //MethodDecl s2 at functions.ceylon (169:4-169:65)
    var s2=function (i$4609){
        return $$$cl4138.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl4138.String("-",1),i$4609.string]).string;
    };
    s2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:LazyExprTest2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['functions','LazyExprTest2','$m','s2']};};
    $$lazyExprTest2.s2=s2;
    return $$lazyExprTest2;
}
LazyExprTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],satisfies:[{t:LazyExprBase}],d:['functions','LazyExprTest2']};};
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl4138.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl4138.Basic,$init$LazyExprBase());
    }
    return LazyExprTest2;
}
exports.$init$LazyExprTest2=$init$LazyExprTest2;
$init$LazyExprTest2();

//ClassDef LazyExprTest3 at functions.ceylon (171:0-173:0)
function LazyExprTest3($$lazyExprTest3){
    $init$LazyExprTest3();
    if ($$lazyExprTest3===undefined)$$lazyExprTest3=new LazyExprTest3.$$;
    LazyExprTest2($$lazyExprTest3);
    
    //AttributeDecl s1 at functions.ceylon (172:4-172:43)
    var s1=$$$cl4138.String("s1",2);
    $$$cl4138.defineAttr($$lazyExprTest3,'s1',function(){return s1;},function(s1$4610){return s1=s1$4610;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprTest3,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.variable()];},d:['functions','LazyExprTest3','$at','s1']};});
    $$lazyExprTest3.$prop$getS1.get=function(){return s1};
    return $$lazyExprTest3;
}
LazyExprTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:LazyExprTest2},$ps:[],d:['functions','LazyExprTest3']};};
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl4138.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',$init$LazyExprTest2());
    }
    return LazyExprTest3;
}
exports.$init$LazyExprTest3=$init$LazyExprTest3;
$init$LazyExprTest3();

//ClassDef LazyExprTest4 at functions.ceylon (174:0-178:0)
function LazyExprTest4($$lazyExprTest4){
    $init$LazyExprTest4();
    if ($$lazyExprTest4===undefined)$$lazyExprTest4=new LazyExprTest4.$$;
    LazyExprTest2($$lazyExprTest4);
    $$$cl4138.copySuperAttr($$lazyExprTest4,'s1','$$functions$LazyExprTest2');
    
    //AttributeDecl assigned at functions.ceylon (175:4-175:40)
    var assigned=$$$cl4138.String("",0);
    $$$cl4138.defineAttr($$lazyExprTest4,'assigned',function(){return assigned;},function(assigned$4611){return assigned=assigned$4611;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprTest4,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['functions','LazyExprTest4','$at','assigned']};});
    $$lazyExprTest4.$prop$getAssigned={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprTest4,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['functions','LazyExprTest4','$at','assigned']};}};
    $$lazyExprTest4.$prop$getAssigned.get=function(){return assigned};
    
    //AttributeGetterDef s1 at functions.ceylon (176:4-176:56)
    $$$cl4138.defineAttr($$lazyExprTest4,'s1',function(){
        return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("s1-",3),$$lazyExprTest4.s1$$functions$LazyExprTest2.string]).string;
    },function(s1$4612){
        $$lazyExprTest4.assigned=s1$4612;
    },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:LazyExprTest4,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','LazyExprTest4','$at','s1']};});
    return $$lazyExprTest4;
}
LazyExprTest4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:LazyExprTest2},$ps:[],d:['functions','LazyExprTest4']};};
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl4138.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',$init$LazyExprTest2());
    }
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDef testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDecl tst at functions.ceylon (181:4-181:30)
    var tst$4613=LazyExprTest();
    (tst$4613.x=(1));
    $$$c4139.check(tst$4613.f1((3)).equals($$$cl4138.String("3:3.2",5)),$$$cl4138.String("=> defaulted param",18));
    $$$c4139.check(tst$4613.f2((3)).equals((7)),$$$cl4138.String("=> method",9));
    $$$c4139.check(tst$4613.i1.equals((3)),$$$cl4138.String("=> attribute",12));
    $$$c4139.check(tst$4613.i2.equals((8)),$$$cl4138.String("=> attribute specifier",22));
    $$$c4139.check(tst$4613.s1.equals($$$cl4138.String("5.1",3)),$$$cl4138.String("=> attribute refinement",23));
    $$$c4139.check(tst$4613.s2((5)).equals($$$cl4138.String("6.5",3)),$$$cl4138.String("=> method refinement",20));
    setLx((1));
    $$$c4139.check(lazy_f1((3)).equals($$$cl4138.String("3.2",3)),$$$cl4138.String("=> defaulted param toplevel",27));
    $$$c4139.check(lazy_f2((3)).equals((7)),$$$cl4138.String("=> method toplevel",18));
    $$$c4139.check(getLazy_i1().equals((3)),$$$cl4138.String("=> attribute toplevel",21));
    
    //AttributeDecl x at functions.ceylon (195:4-195:29)
    var x$4614=(1000);
    function setX$4614(x$4615){return x$4614=x$4615;};
    
    //MethodDecl f1 at functions.ceylon (196:4-196:64)
    var f1$4616=function (i$4617,f$4618){
        if(f$4618===undefined){f$4618=function (){
            return $$$cl4138.StringBuilder().appendAll([i$4617.string,$$$cl4138.String(".",1),x$4614.plus((1)).string]).string;
        };}
        return f$4618();
    };
    f1$4616.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','testLazyExpressions','$m','f1']};};
    
    //MethodDecl f2 at functions.ceylon (197:4-197:38)
    var f2$4619=function (i$4620){
        return (2).times((x$4614=x$4614.successor)).plus(i$4620);
    };
    f2$4619.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','testLazyExpressions','$m','f2']};};
    
    //AttributeDecl i1 at functions.ceylon (198:4-198:21)
    function getI1$4621(){return (x$4614=x$4614.successor);};
    
    //AttributeDecl i2 at functions.ceylon (199:4-199:14)
    var i2$4622;
    var getI2$4622=function(){
        return (x$4614=x$4614.successor).times((2));
    };
    x$4614=(1);
    $$$c4139.check(f1$4616((3)).equals($$$cl4138.String("3.2",3)),$$$cl4138.String("=> defaulted param local",24));
    $$$c4139.check(f2$4619((3)).equals((7)),$$$cl4138.String("=> method local",15));
    $$$c4139.check(getI1$4621().equals((3)),$$$cl4138.String("=> attribute local",18));
    $$$c4139.check(getI2$4622().equals((8)),$$$cl4138.String("=> attribute specifier local",28));
    
    //AttributeDecl tst3 at functions.ceylon (208:4-208:32)
    var tst3$4623=LazyExprTest3();
    (tst3$4623.x=(1));
    $$$c4139.check(tst3$4623.s1.equals($$$cl4138.String("s1",2)),$$$cl4138.String("=> override variable 1",22));
    (tst3$4623.s1=$$$cl4138.String("abc",3));
    $$$c4139.check(tst3$4623.s1.equals($$$cl4138.String("abc",3)),$$$cl4138.String("=> override variable 2",22));
    
    //AttributeDecl tst4 at functions.ceylon (213:4-213:32)
    var tst4$4624=LazyExprTest4();
    (tst4$4624.x=(1));
    $$$c4139.check(tst4$4624.s1.equals($$$cl4138.String("s1-2",4)),$$$cl4138.String("=> override getter/setter 1",27));
    (tmp$4625=tst4$4624,tmp$4625.s1=$$$cl4138.String("abc",3),tmp$4625.s1);
    var tmp$4625;
    $$$c4139.check(tst4$4624.s1.equals($$$cl4138.String("s1-4",4)),$$$cl4138.String("=> override getter/setter 2",27));
    $$$c4139.check(tst4$4624.assigned.equals($$$cl4138.String("abc",3)),$$$cl4138.String("=> override getter/setter 3",27));
    (tst$4613.x=(1));
    x$4614=(10);
    $$$c4139.check((i$4626=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$4614=x$4614.successor);
    }()),(opt$4627=tst$4613,$$$cl4138.JsCallable(opt$4627,opt$4627!==null?opt$4627.f1:null))(i$4626,undefined)).equals($$$cl4138.String("11:11.2",7)),$$$cl4138.String("=> named arg",12));
    var i$4626,opt$4627;
    $$$c4139.check((i$4628=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$4614=x$4614.successor);
    }()),f$4629=function (){return (x$4614=x$4614.successor).string;},(opt$4630=tst$4613,$$$cl4138.JsCallable(opt$4630,opt$4630!==null?opt$4630.f1:null))(i$4628,f$4629)).equals($$$cl4138.String("12:13",5)),$$$cl4138.String("=> named arg function",21));
    var i$4628,f$4629,opt$4630;
    $$$c4139.check((f$4631=function (i$4632){return $$$cl4138.StringBuilder().appendAll([i$4632.string,$$$cl4138.String("-",1),(x$4614=x$4614.successor).string]).string;},(opt$4633=tst$4613,$$$cl4138.JsCallable(opt$4633,opt$4633!==null?opt$4633.f3:null))(f$4631)).equals($$$cl4138.String("2-14",4)),$$$cl4138.String("=> named arg function with param",32));
    var f$4631,opt$4633;
};testLazyExpressions.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testLazyExpressions']};};

//MethodDef test at functions.ceylon (227:0-243:0)
function test(){
    helloWorld();
    hello($$$cl4138.String("test",4));
    helloAll([$$$cl4138.String("Gavin",5),$$$cl4138.String("Enrique",7),$$$cl4138.String("Ivo",3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}));
    $$$c4139.check(toString((5)).equals($$$cl4138.String("5",1)),$$$cl4138.String("toString(obj)",13));
    $$$c4139.check(add($$$cl4138.Float(1.5),$$$cl4138.Float(2.5)).equals($$$cl4138.Float(4.0)),$$$cl4138.String("add(Float,Float)",16));
    testMethodReference();
    testDefaultedParams();
    testGetterMethodDefinitions();
    testMultipleParamLists();
    testAnonymous();
    testNamedArguments();
    testLazyExpressions();
    testStaticMethodReferences();
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['functions','test']};};

//ClassDef MethodRefTest at method_refs.ceylon (4:0-13:0)
function MethodRefTest(name, $$methodRefTest){
    $init$MethodRefTest();
    if ($$methodRefTest===undefined)$$methodRefTest=new MethodRefTest.$$;
    
    //AttributeDecl name at method_refs.ceylon (5:4-5:22)
    var name=name;
    $$$cl4138.defineAttr($$methodRefTest,'name',function(){return name;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MethodRefTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','MethodRefTest','$at','name']};});
    $$methodRefTest.$prop$getName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MethodRefTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','MethodRefTest','$at','name']};}};
    $$methodRefTest.$prop$getName.get=function(){return name};
    
    //AttributeDecl string at method_refs.ceylon (6:4-6:59)
    $$$cl4138.defineAttr($$methodRefTest,'string',function(){return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("MethodRefTest ",14),$$methodRefTest.name.string]).string;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MethodRefTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MethodRefTest','$at','string']};});
    $$methodRefTest.$prop$getString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MethodRefTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MethodRefTest','$at','string']};}};
    $$methodRefTest.$prop$getString.get=function(){return string};
    
    //MethodDecl suffix at method_refs.ceylon (7:4-7:58)
    var suffix=function (x$4634){
        return $$$cl4138.StringBuilder().appendAll([$$methodRefTest.string.string,$$$cl4138.String(" #",2),x$4634.string]).string;
    };
    suffix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MethodRefTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','MethodRefTest','$m','suffix']};};
    $$methodRefTest.suffix=suffix;
    
    //ClassDef Inner at method_refs.ceylon (9:4-12:4)
    function Inner$MethodRefTest(kid$4635, $$inner$MethodRefTest){
        $init$Inner$MethodRefTest();
        if ($$inner$MethodRefTest===undefined)$$inner$MethodRefTest=new Inner$MethodRefTest.$$;
        $$inner$MethodRefTest.$$outer=this;
        $$inner$MethodRefTest.kid$4635_=kid$4635;
        $$$cl4138.defineAttr($$inner$MethodRefTest,'kid$4635',function(){return this.kid$4635_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$MethodRefTest,d:['functions','MethodRefTest','$c','Inner','$at','kid']};});
        
        //AttributeDecl string at method_refs.ceylon (10:8-10:69)
        $$$cl4138.defineAttr($$inner$MethodRefTest,'string',function(){return $$$cl4138.StringBuilder().appendAll([$$methodRefTest.string.string,$$$cl4138.String(" sub-",5),kid$4635.string]).string;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$MethodRefTest,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['functions','MethodRefTest','$c','Inner','$at','string']};});
        $$inner$MethodRefTest.$prop$getString.get=function(){return string};
        
        //MethodDecl prefix at method_refs.ceylon (11:8-11:62)
        var prefix=function (x$4636){
            return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("#",1),x$4636.string,$$$cl4138.String(" ",1),$$inner$MethodRefTest.string.string]).string;
        };
        prefix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:Inner$MethodRefTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','MethodRefTest','$c','Inner','$m','prefix']};};
        $$inner$MethodRefTest.prefix=prefix;
        return $$inner$MethodRefTest;
    }
    Inner$MethodRefTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'kid',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$cont:MethodRefTest,$an:function(){return[$$$cl4138.shared()];},d:['functions','MethodRefTest','$c','Inner']};};
    $$methodRefTest.Inner$MethodRefTest=Inner$MethodRefTest;
    function $init$Inner$MethodRefTest(){
        if (Inner$MethodRefTest.$$===undefined){
            $$$cl4138.initTypeProto(Inner$MethodRefTest,'functions::MethodRefTest.Inner',$$$cl4138.Basic);
            MethodRefTest.Inner$MethodRefTest=Inner$MethodRefTest;
        }
        return Inner$MethodRefTest;
    }
    $$methodRefTest.$init$Inner$MethodRefTest=$init$Inner$MethodRefTest;
    $init$Inner$MethodRefTest();
    return $$methodRefTest;
}
MethodRefTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[$$$cl4138.shared()];}}],d:['functions','MethodRefTest']};};
function $init$MethodRefTest(){
    if (MethodRefTest.$$===undefined){
        $$$cl4138.initTypeProto(MethodRefTest,'functions::MethodRefTest',$$$cl4138.Basic);
    }
    return MethodRefTest;
}
exports.$init$MethodRefTest=$init$MethodRefTest;
$init$MethodRefTest();

//MethodDef testStaticMethodReferences at method_refs.ceylon (15:0-30:0)
function testStaticMethodReferences(){
    $$$cl4138.print($$$cl4138.String("Testing static method references...",35));
    
    //AttributeDecl mr at method_refs.ceylon (17:4-17:37)
    var mr$4637=MethodRefTest($$$cl4138.String("TEST",4));
    
    //AttributeDecl mref at method_refs.ceylon (18:4-18:41)
    var mref$4638=$$$cl4138.$JsCallable(function($O$) {return $$$cl4138.JsCallable($O$,$O$.suffix);}(mr$4637),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}});
    $$$c4139.check(mref$4638((1)).equals($$$cl4138.String("MethodRefTest TEST #1",21)),$$$cl4138.String("Static method ref 1",19));
    $$$c4139.check(function($O$) {return $O$.string;}(mr$4637).equals($$$cl4138.String("MethodRefTest TEST",18)),$$$cl4138.String("Static method ref 2",19));
    $$$c4139.check(mref$4638((1)).equals(function($O$) {return $$$cl4138.JsCallable($O$,$O$.suffix);}(mr$4637)((1))),$$$cl4138.String("Static method ref 3",19));
    
    //AttributeDecl mri at method_refs.ceylon (22:4-22:30)
    var mri$4639=mr$4637.Inner$MethodRefTest($$$cl4138.String("T2",2));
    
    //AttributeDecl iref at method_refs.ceylon (23:4-23:48)
    var iref$4640=$$$cl4138.$JsCallable(function($O$) {return $$$cl4138.JsCallable($O$,$O$.prefix);}(mri$4639),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}});
    $$$c4139.check(iref$4640((1)).equals($$$cl4138.String("#1 MethodRefTest TEST sub-T2",28)),$$$cl4138.String("Static method ref 4",19));
    $$$c4139.check(function($O$) {return $O$.string;}(mri$4639).equals($$$cl4138.String("MethodRefTest TEST sub-T2",25)),$$$cl4138.String("Static method ref 5",19));
    $$$c4139.check(iref$4640((1)).equals(function($O$) {return $$$cl4138.JsCallable($O$,$O$.prefix);}(mri$4639)((1))),$$$cl4138.String("Static method ref 6",19));
    
    //AttributeDecl ints at method_refs.ceylon (27:4-27:34)
    var ints$4641=(elems$4642=[(1),(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),$$$cl4138.LazyList(elems$4642,{Element:{t:$$$cl4138.Integer}}));
    var elems$4642;
    $$$c4139.check((opt$4643=function($O$) {return $$$cl4138.JsCallable($O$,$O$.$get);}(ints$4641)((1)),opt$4643!==null?opt$4643:(-(1))).equals((2)),$$$cl4138.String("Static method ref 7",19));
    var opt$4643;
    $$$c4139.check(function($O$) {return $$$cl4138.JsCallable($O$,$O$.$map);}(ints$4641,{Result:{t:$$$cl4138.String}})($$$cl4138.$JsCallable((function (x$4644){
        return x$4644.string;
    }),[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}}),{Result:{t:$$$cl4138.String}}).sequence.equals([$$$cl4138.String("1",1),$$$cl4138.String("2",1),$$$cl4138.String("3",1),$$$cl4138.String("4",1)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.String}})),$$$cl4138.String("Static method ref 8",19));
};testStaticMethodReferences.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testStaticMethodReferences']};};
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$4645,y$4646){
        return x$4645.compare(y$4646);
    }
};multiCompare.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Comparison},$ps:[],d:['functions','multiCompare']};};

//MethodDef multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$4647){
    return function(apat$4648){
        return function(amat$4649){
            return $$$cl4138.StringBuilder().appendAll([nombre$4647.string,$$$cl4138.String(" ",1),apat$4648.string,$$$cl4138.String(" ",1),amat$4649.string]).string;
        }
    }
};multiFullname.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'nombre',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','multiFullname']};};

//MethodDef multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$4650){
    if(name$4650===undefined){name$4650=$$$cl4138.String("A",1);}
    return function(apat$4651){
        return function(amat$4652){
            return $$$cl4138.StringBuilder().appendAll([name$4650.string,$$$cl4138.String(" ",1),apat$4651.string,$$$cl4138.String(" ",1),amat$4652.string]).string;
        }
    }
};multiDefaulted.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'name',$mt:'prm',$def:1,$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','multiDefaulted']};};

//MethodDef multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$4653){
    if(names$4653===undefined){names$4653=$$$cl4138.getEmpty();}
    return function(count$4654){
        
        //AttributeDecl sb at multiples.ceylon (13:4-13:30)
        var sb$4655=$$$cl4138.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$4656 = names$4653.iterator();
        var name$4657;while ((name$4657=it$4656.next())!==$$$cl4138.getFinished()){
            sb$4655.append(name$4657).append($$$cl4138.String(" ",1));
        }
        sb$4655.append($$$cl4138.String("count ",6)).append(count$4654.string);
        return sb$4655.string;
    }
};multiSequenced.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.String}}},$an:function(){return[];}}],d:['functions','multiSequenced']};};

//MethodDef testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl4138.print($$$cl4138.String("Testing multiple parameter lists...",35));
    $$$c4139.check(multiCompare()((1),(1)).equals($$$cl4138.getEqual()),$$$cl4138.String("Multi compare 1",15));
    $$$c4139.check(multiCompare()((1),(2)).equals($$$cl4138.getSmaller()),$$$cl4138.String("Multi compare 2",15));
    $$$c4139.check(multiCompare()((2),(1)).equals($$$cl4138.getLarger()),$$$cl4138.String("Multi compare 3",15));
    
    //MethodDecl comp at multiples.ceylon (26:4-26:60)
    var comp$4658=function (a$4659,b$4660){
        return multiCompare()(a$4659,b$4660);
    };
    comp$4658.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Comparison},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','comp']};};
    $$$c4139.check(comp$4658((1),(1)).equals($$$cl4138.getEqual()),$$$cl4138.String("Multi compare 4",15));
    $$$c4139.check(comp$4658((1),(2)).equals($$$cl4138.getSmaller()),$$$cl4138.String("Multi compare 5",15));
    $$$c4139.check(comp$4658((2),(1)).equals($$$cl4138.getLarger()),$$$cl4138.String("Multi compare 6",15));
    $$$c4139.check(multiFullname($$$cl4138.String("a",1))($$$cl4138.String("b",1))($$$cl4138.String("c",1)).equals($$$cl4138.String("a b c",5)),$$$cl4138.String("Multi fullname 1",16));
    
    //MethodDecl apat at multiples.ceylon (31:4-31:55)
    var apat$4661=function (c$4662){
        return multiFullname($$$cl4138.String("A",1))($$$cl4138.String("B",1))(c$4662);
    };
    apat$4661.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','apat']};};
    $$$c4139.check(apat$4661($$$cl4138.String("C",1)).equals($$$cl4138.String("A B C",5)),$$$cl4138.String("Multi fullname 2",16));
    
    //MethodDecl nombre at multiples.ceylon (33:4-33:61)
    var nombre$4663=function (name$4664){
        return multiFullname($$$cl4138.String("Name",4))(name$4664);
    };
    nombre$4663.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl4138.String}}]},Return:{t:$$$cl4138.String}}},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','nombre']};};
    $$$c4139.check(nombre$4663($$$cl4138.String("Z",1))($$$cl4138.String("L",1)).equals($$$cl4138.String("Name Z L",8)),$$$cl4138.String("Multi callable 2",16));
    $$$c4139.check(multiDefaulted()($$$cl4138.String("B",1))($$$cl4138.String("C",1)).equals($$$cl4138.String("A B C",5)),$$$cl4138.String("Multi defaulted 1",17));
    
    //AttributeDecl md1 at multiples.ceylon (37:4-37:69)
    var md1$4665=$$$cl4138.$JsCallable(multiDefaulted(),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.String}}],{Arguments:{t:'T', l:[{t:$$$cl4138.String}]},Return:{t:$$$cl4138.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl4138.String}]},Return:{t:$$$cl4138.String}}}});
    $$$c4139.check(md1$4665($$$cl4138.String("B",1))($$$cl4138.String("C",1)).equals($$$cl4138.String("A B C",5)),$$$cl4138.String("Multi defaulted 2",17));
    $$$c4139.check(md1$4665($$$cl4138.String("B",1))($$$cl4138.String("Z",1)).equals($$$cl4138.String("A B Z",5)),$$$cl4138.String("Multi defaulted 3",17));
    $$$c4139.check(md1$4665($$$cl4138.String("Z",1))($$$cl4138.String("C",1)).equals($$$cl4138.String("A Z C",5)),$$$cl4138.String("Multi defaulted 4",17));
    $$$c4139.check(md1$4665($$$cl4138.String("Y",1))($$$cl4138.String("Z",1)).equals($$$cl4138.String("A Y Z",5)),$$$cl4138.String("Multi defaulted 5",17));
    
    //MethodDecl md2 at multiples.ceylon (42:4-42:52)
    var md2$4666=function (x$4667){
        return multiDefaulted()($$$cl4138.String("B",1))(x$4667);
    };
    md2$4666.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','md2']};};
    $$$c4139.check(md2$4666($$$cl4138.String("C",1)).equals($$$cl4138.String("A B C",5)),$$$cl4138.String("Multi defaulted 6",17));
    $$$c4139.check(md2$4666($$$cl4138.String("Z",1)).equals($$$cl4138.String("A B Z",5)),$$$cl4138.String("Multi defaulted 7",17));
    $$$c4139.check(multiSequenced([$$$cl4138.String("A",1),$$$cl4138.String("B",1),$$$cl4138.String("C",1)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))((1)).equals($$$cl4138.String("A B C count 1",13)),$$$cl4138.String("Multi sequenced 1",17));
    
    //MethodDecl ms1 at multiples.ceylon (46:4-46:59)
    var ms1$4668=function (c$4669){
        return multiSequenced([$$$cl4138.String("x",1),$$$cl4138.String("y",1),$$$cl4138.String("z",1)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))(c$4669);
    };
    ms1$4668.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','ms1']};};
    $$$c4139.check(ms1$4668((5)).equals($$$cl4138.String("x y z count 5",13)),$$$cl4138.String("Multi sequenced 2",17));
    $$$c4139.check(ms1$4668((10)).equals($$$cl4138.String("x y z count 10",14)),$$$cl4138.String("Multi sequenced 3",17));
};testMultipleParamLists.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['functions','testMultipleParamLists']};};
exports.$pkg$ans$functions=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}})),$$$cl4138.shared()];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
