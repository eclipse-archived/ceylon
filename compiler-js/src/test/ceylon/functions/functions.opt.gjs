(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"functions","functions":{"LazyExprBase":{"$mt":"ifc","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"formal":[]},"$nm":"s1"}},"$nm":"LazyExprBase"},"find2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$nm":"f"}},"$nm":"find2"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testGetterMethodDefinitions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$c":{"GetterTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"i"}},"$nm":"GetterTest"}},"$nm":"testGetterMethodDefinitions"},"repeat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"times"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"iter"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"iter"}]],"$mt":"mthd","$nm":"f"}},"$nm":"repeat"},"add":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$nm":"y"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"add"},"lazy_i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"lazy_i1"},"multiDefaulted":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$def":"1","$nm":"name"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"amat"}]],"$mt":"mthd","$nm":"multiDefaulted"},"lx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"lx"},"testDefaultedParams":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testDefaultedParams"},"$pkg-shared":"1","Issue105":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$an":{"shared":[]},"$nm":"i"},{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"more"}],"$mt":"cls","$at":{"more":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"functions","$nm":"Issue105"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$nm":"more"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"i"}},"$nm":"Issue105"},"MySequence":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"prm","$nm":"seq"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"functions","$nm":"MySequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"seq":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$nm":"seq"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"first":{"$t":{"$nm":"Element"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MySequence"},"multiCompare":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"y"}]],"$mt":"mthd","$nm":"multiCompare"},"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"},"find":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$nm":"Element"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"ceylon.language","$nm":"Array"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$tp":[{"$nm":"Element"}],"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$nm":"f"}},"$nm":"find"},"namedArgFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$def":"1","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$def":"1","$nm":"y"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"z"}]],"$mt":"mthd","$nm":"namedArgFunc"},"testMethodReference":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"x"}},"$nm":"tst"}},"$nm":"testMethodReference"},"lazy_f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"lazy_f2"},"lazy_f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"lazy_f1"},"testAnonymous":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"f"},"callFunction":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"expect"}]],"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"f"}},"$nm":"callFunction"}},"$nm":"testAnonymous"},"multiFullname":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"nombre"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"apat"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"amat"}]],"$mt":"mthd","$nm":"multiFullname"},"LazyExprTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"s2"},"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f3"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"i1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest"},"MethodRefTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$an":{"shared":[]},"$nm":"name"}],"$mt":"cls","$m":{"suffix":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"suffix"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"kid"}],"$mt":"cls","$an":{"shared":[]},"$m":{"prefix":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"prefix"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"kid":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"kid"}},"$nm":"Inner"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"MethodRefTest"},"RefHelper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"RefHelper"},"testStaticMethodReferences":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testStaticMethodReferences"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"},"subtract":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"howMuch"}]],"$mt":"mthd","$nm":"subtract"},"LazyExprTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"functions","$nm":"LazyExprBase"}],"$mt":"cls","$m":{"s2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s2"}},"$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"s1"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"x"}},"$nm":"LazyExprTest2"},"testLazyExpressions":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"f","$def":"1","$nm":"f"}]],"$mt":"mthd","$nm":"f1"},"f2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"f2"}},"$nm":"testLazyExpressions"},"testMultipleParamLists":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"nombre":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}]],"$mt":"mthd","$nm":"nombre"},"comp":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Comparison"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$nm":"comp"},"ms1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"}]],"$mt":"mthd","$nm":"ms1"},"md2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$nm":"md2"},"apat":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"c"}]],"$mt":"mthd","$nm":"apat"}},"$nm":"testMultipleParamLists"},"DefParamTest3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"DefParamTest3"},"DefParamTest2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}],"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"DefParamTest2"},"DefParamTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}],"$mt":"cls","$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"s"},"i3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i3"},"i2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i2"},"i1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"i1"}},"$nm":"DefParamTest1"},"toString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"obj"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"toString"},"LeaveThereHereForMetamodelTests":{"$mt":"ifc","$an":{"shared":[]},"$nm":"LeaveThereHereForMetamodelTests"},"LazyExprTest4":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"s1"},"assigned":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"assigned"}},"$nm":"LazyExprTest4"},"helloAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"names"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"helloAll"},"LazyExprTest3":{"super":{"$pk":"functions","$nm":"LazyExprTest2"},"$mt":"cls","$at":{"s1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"s1"}},"$nm":"LazyExprTest3"},"multiSequenced":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"names"}],[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"count"}]],"$mt":"mthd","$nm":"multiSequenced"},"defParamTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i1"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i2"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"i3"}]],"$mt":"mthd","$nm":"defParamTest"}},"$mod-version":"0.1","$mod-bin":"6.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//MethodDef find at anonymous.ceylon (3:0-10:0)
function find(a$333,f$334,$$$mptypes){
    //'for' statement at anonymous.ceylon (4:2-8:2)
    var it$335 = a$333.iterator();
    var e$336;while ((e$336=it$335.next())!==$$$cl1.getFinished()){
        if(f$334(e$336)){
            return e$336;
        }
    }
    return null;
};find.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Array,a:{Element:'Element'}},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl1.Boolean},$an:function(){return[];}}],$tp:{Element:{}},d:['functions','find']};};

//MethodDef find2 at anonymous.ceylon (12:0-20:0)
function find2(a$337,f$338,$$$mptypes){
    if(f$338===undefined){f$338=function (x$339){
        return true;
    };}
    //'for' statement at anonymous.ceylon (13:2-19:2)
    var it$340 = a$337.iterator();
    var e$341;while ((e$341=it$340.next())!==$$$cl1.getFinished()){
        if(f$338(e$341)){
            return e$341;
        }
    }
    if ($$$cl1.getFinished() === e$341){
        return null;
    }
};find2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},'Element']},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Array,a:{Element:'Element'}},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl1.Boolean},$an:function(){return[];}}],$tp:{Element:{}},d:['functions','find2']};};

//MethodDef subtract at anonymous.ceylon (22:0-24:0)
function subtract(howMuch$342){
    return (function (i$343){
        return i$343.minus(howMuch$342).string;
    });
};subtract.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl1.Integer}}]},Return:{t:$$$cl1.String}}},$ps:[{$nm:'howMuch',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','subtract']};};

//MethodDef testAnonymous at anonymous.ceylon (26:0-76:0)
function testAnonymous(){
    $$$cl1.print($$$cl1.String("Testing anonymous functions...",30));
    
    //AttributeDecl nums at anonymous.ceylon (28:2-28:31)
    var nums$344=(elements$345=[(1),(2),(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),$$$cl1.Array(elements$345,{Element:{t:$$$cl1.Integer}}));
    var elements$345;
    
    //AttributeDecl found at anonymous.ceylon (30:2-30:58)
    var found$346=find(nums$344,$$$cl1.$JsCallable((function (i$347){
        return i$347.remainder((2)).equals((0));
    }),[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Boolean}}),{Element:{t:$$$cl1.Integer}});
    function setFound$346(found$348){return found$346=found$348;};
    var i$349;
    if((i$349=found$346)!==null){
        $$$c2.check(i$349.equals((2)),$$$cl1.String("anonfunc positional",19));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc positional",19));
    }
    found$346=(f$350=function (i$351){
        return i$351.remainder((2)).equals((0));
    },a$352=nums$344,find(a$352,f$350,{Element:{t:$$$cl1.Integer}}));
    var f$350,a$352;
    var i$353;
    if((i$353=found$346)!==null){
        $$$c2.check(i$353.equals((2)),$$$cl1.String("anonfunc named",14));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc named",14));
    }
    
    //MethodDef callFunction at anonymous.ceylon (46:2-48:2)
    function callFunction$354(f$355,expect$356){
        $$$c2.check(f$355((0)).equals(expect$356),$$$cl1.StringBuilder().appendAll([$$$cl1.String("anon func returns ",18),f$355((0)).string,$$$cl1.String(" instead of ",12),expect$356.string]).string);
    };callFunction$354.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl1.String},$an:function(){return[];}},{$nm:'expect',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','testAnonymous','$m','callFunction']};};
    
    //MethodDef f at anonymous.ceylon (50:2-52:2)
    function f$357(i$358){
        return i$358.plus((12)).string;
    };f$357.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','testAnonymous','$m','f']};};
    callFunction$354($$$cl1.$JsCallable(f$357,[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}}),$$$cl1.String("12",2));
    callFunction$354($$$cl1.$JsCallable((function (i$359){
        return i$359.times((3)).string;
    }),[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}}),$$$cl1.String("0",1));
    (expect$360=$$$cl1.String("0",1),f$361=function (i$362){
        return i$362.power((2)).string;
    },callFunction$354(f$361,expect$360));
    var expect$360,f$361;
    
    //AttributeDecl f2 at anonymous.ceylon (64:2-64:41)
    var f2$363=$$$cl1.$JsCallable((function (i$364){
        return i$364.minus((10)).string;
    }),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}});
    callFunction$354($$$cl1.$JsCallable(f2$363,[],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}}),$$$cl1.String("-10",3));
    callFunction$354($$$cl1.$JsCallable(subtract((5)),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}}),$$$cl1.String("-5",2));
    found$346=find2(nums$344,$$$cl1.$JsCallable((function (i$365){
        return i$365.compare((2)).equals($$$cl1.getLarger());
    }),[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Boolean}}),{Element:{t:$$$cl1.Integer}});
    var i$366;
    if((i$366=found$346)!==null){
        $$$c2.check(i$366.equals((3)),$$$cl1.String("anonfunc i>2 [1]",16));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc i>2 [2]",16));
    }
    found$346=find2(nums$344,undefined,{Element:{t:$$$cl1.Integer}});
    var i$367;
    if((i$367=found$346)!==null){
        $$$c2.check(i$367.equals((1)),$$$cl1.String("anonfunc defaulted param [1]",28));
    }else {
        $$$c2.fail($$$cl1.String("anonfunc defaulted param [2]",28));
    }
};testAnonymous.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testAnonymous']};};

//InterfaceDef LeaveThereHereForMetamodelTests at anonymous.ceylon (78:0-78:49)
function LeaveThereHereForMetamodelTests($$leaveThereHereForMetamodelTests){
}
LeaveThereHereForMetamodelTests.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl1.shared()];},d:['functions','LeaveThereHereForMetamodelTests']};};
exports.LeaveThereHereForMetamodelTests=LeaveThereHereForMetamodelTests;
function $init$LeaveThereHereForMetamodelTests(){
    if (LeaveThereHereForMetamodelTests.$$===undefined){
        $$$cl1.initTypeProtoI(LeaveThereHereForMetamodelTests,'functions::LeaveThereHereForMetamodelTests');
    }
    return LeaveThereHereForMetamodelTests;
}
exports.$init$LeaveThereHereForMetamodelTests=$init$LeaveThereHereForMetamodelTests;
$init$LeaveThereHereForMetamodelTests();

//MethodDef helloWorld at functions.ceylon (3:0-5:0)
function helloWorld(){
    $$$cl1.print($$$cl1.String("hello world",11));
}
exports.helloWorld=helloWorld;
helloWorld.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['functions','helloWorld']};};

//MethodDef hello at functions.ceylon (7:0-9:0)
function hello(name$368){
    $$$cl1.print($$$cl1.String("hello",5).plus(name$368));
}
exports.hello=hello;
hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['functions','hello']};};

//MethodDef helloAll at functions.ceylon (11:0-11:37)
function helloAll(names$369){
    if(names$369===undefined){names$369=$$$cl1.getEmpty();}
}
exports.helloAll=helloAll;
helloAll.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['functions','helloAll']};};

//MethodDef toString at functions.ceylon (13:0-15:0)
function toString(obj$370){
    return obj$370.string;
}
exports.toString=toString;
toString.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'obj',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['functions','toString']};};

//MethodDef add at functions.ceylon (17:0-19:0)
function add(x$371,y$372){
    return x$371.plus(y$372);
}
exports.add=add;
add.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Float},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Float},$an:function(){return[];}},{$nm:'y',$mt:'prm',$t:{t:$$$cl1.Float},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['functions','add']};};

//MethodDef repeat at functions.ceylon (21:0-23:0)
function repeat(times$373,f$374){
    f$374((0));
}
exports.repeat=repeat;
repeat.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'times',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl1.Anything},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['functions','repeat']};};

//ClassDef MySequence at functions.ceylon (25:0-44:0)
function MySequence(seq$375, $$targs$$,$$mySequence){
    $init$MySequence();
    if ($$mySequence===undefined)$$mySequence=new MySequence.$$;
    $$$cl1.set_type_args($$mySequence,$$targs$$);
    $$mySequence.seq$375_=seq$375;
    $$$cl1.Sequence($$mySequence.$$targs$$===undefined?$$targs$$:{Element:$$mySequence.$$targs$$.Element},$$mySequence);
    
    //AttributeDecl last at functions.ceylon (40:4-40:42)
    $$mySequence.$prop$getLast={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','last']};}};
    $$mySequence.$prop$getLast.get=function(){return last};
    
    //AttributeDecl size at functions.ceylon (42:4-42:42)
    $$mySequence.$prop$getSize={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','size']};}};
    $$mySequence.$prop$getSize.get=function(){return size};
    return $$mySequence;
}
MySequence.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'seq',$mt:'prm',$t:{t:$$$cl1.Sequence,a:{Element:'Element'}},$an:function(){return[];}}],$tp:{Element:{'var':'out'}},satisfies:[{t:$$$cl1.Sequence,a:{Element:'Element'}}],d:['functions','MySequence']};};
function $init$MySequence(){
    if (MySequence.$$===undefined){
        $$$cl1.initTypeProto(MySequence,'functions::MySequence',$$$cl1.Basic,$$$cl1.Sequence);
        (function($$mySequence){
            
            //AttributeGetterDef lastIndex at functions.ceylon (27:4-27:60)
            $$$cl1.defineAttr($$mySequence,'lastIndex',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.lastIndex;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','lastIndex']};});
            //AttributeGetterDef first at functions.ceylon (28:4-28:52)
            $$$cl1.defineAttr($$mySequence,'first',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.first;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','first']};});
            //AttributeGetterDef rest at functions.ceylon (29:4-29:52)
            $$$cl1.defineAttr($$mySequence,'rest',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.rest;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','rest']};});
            //MethodDef get at functions.ceylon (30:4-30:67)
            $$mySequence.$get=function $get(index$376){
                var $$mySequence=this;
                return $$mySequence.seq$375.$get(index$376);
            };$$mySequence.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},'Element']},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','get']};};
            
            //MethodDef span at functions.ceylon (31:4-31:88)
            $$mySequence.span=function span(from$377,to$378){
                var $$mySequence=this;
                return $$mySequence.seq$375.span(from$377,to$378);
            };$$mySequence.span.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','span']};};
            
            //MethodDef spanFrom at functions.ceylon (32:4-32:80)
            $$mySequence.spanFrom=function spanFrom(from$379){
                var $$mySequence=this;
                return $$mySequence.seq$375.spanFrom(from$379);
            };$$mySequence.spanFrom.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','spanFrom']};};
            
            //MethodDef spanTo at functions.ceylon (33:4-33:72)
            $$mySequence.spanTo=function spanTo(to$380){
                var $$mySequence=this;
                return $$mySequence.seq$375.spanTo(to$380);
            };$$mySequence.spanTo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','spanTo']};};
            
            //MethodDef segment at functions.ceylon (34:4-34:102)
            $$mySequence.segment=function segment(from$381,length$382){
                var $$mySequence=this;
                return $$mySequence.seq$375.segment(from$381,length$382);
            };$$mySequence.segment.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:'Element'}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'length',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','segment']};};
            
            //AttributeGetterDef clone at functions.ceylon (35:4-35:59)
            $$$cl1.defineAttr($$mySequence,'clone',function(){
                var $$mySequence=this;
                return $$mySequence;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:MySequence,a:{Element:'Element'}},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','clone']};});
            //AttributeGetterDef string at functions.ceylon (36:4-36:53)
            $$$cl1.defineAttr($$mySequence,'string',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','string']};});
            //AttributeGetterDef hash at functions.ceylon (37:4-37:50)
            $$$cl1.defineAttr($$mySequence,'hash',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.hash;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','hash']};});
            //MethodDef equals at functions.ceylon (38:4-38:75)
            $$mySequence.equals=function equals(other$383){
                var $$mySequence=this;
                return $$mySequence.seq$375.equals(other$383);
            };$$mySequence.equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','equals']};};
            
            //AttributeGetterDef reversed at functions.ceylon (39:4-39:68)
            $$$cl1.defineAttr($$mySequence,'reversed',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.reversed;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequence,a:{Element:'Element'}},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','reversed']};});
            //AttributeDecl last at functions.ceylon (40:4-40:42)
            $$$cl1.defineAttr($$mySequence,'last',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.last;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','last']};});
            
            //MethodDecl iterator at functions.ceylon (41:4-41:64)
            $$mySequence.iterator=function (){
                var $$mySequence=this;
                return $$mySequence.seq$375.iterator();
            };
            $$mySequence.iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterator,a:{Element:'Element'}},$ps:[],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','iterator']};};
            
            //AttributeDecl size at functions.ceylon (42:4-42:42)
            $$$cl1.defineAttr($$mySequence,'size',function(){
                var $$mySequence=this;
                return $$mySequence.seq$375.size;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$at','size']};});
            
            //MethodDecl contains at functions.ceylon (43:4-43:71)
            $$mySequence.contains=function (other$384){
                var $$mySequence=this;
                return $$mySequence.seq$375.contains(other$384);
            };
            $$mySequence.contains.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$cont:MySequence,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MySequence','$m','contains']};};
            $$$cl1.defineAttr($$mySequence,'seq$375',function(){return this.seq$375_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequence,a:{Element:'Element'}},$cont:MySequence,d:['functions','MySequence','$at','seq']};});
        })(MySequence.$$.prototype);
    }
    return MySequence;
}
exports.$init$MySequence=$init$MySequence;
$init$MySequence();

//ClassDef RefHelper at functions.ceylon (46:0-48:0)
function RefHelper($$refHelper){
    $init$RefHelper();
    if ($$refHelper===undefined)$$refHelper=new RefHelper.$$;
    return $$refHelper;
}
RefHelper.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['functions','RefHelper']};};
function $init$RefHelper(){
    if (RefHelper.$$===undefined){
        $$$cl1.initTypeProto(RefHelper,'functions::RefHelper',$$$cl1.Basic);
        (function($$refHelper){
            
            //MethodDef f at functions.ceylon (47:4-47:47)
            $$refHelper.f=function f(i$385){
                var $$refHelper=this;
                return true;
            };$$refHelper.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:RefHelper,$an:function(){return[$$$cl1.shared()];},d:['functions','RefHelper','$m','f']};};
        })(RefHelper.$$.prototype);
    }
    return RefHelper;
}
exports.$init$RefHelper=$init$RefHelper;
$init$RefHelper();

//MethodDef testMethodReference at functions.ceylon (50:0-58:0)
function testMethodReference(){
    
    //AttributeDecl obj1 at functions.ceylon (51:4-51:28)
    var obj1$386=RefHelper();
    
    //AttributeDecl obj2 at functions.ceylon (52:4-52:43)
    var obj2$387=MySequence($$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Element:{t:$$$cl1.String}});
    
    //MethodDef tst at functions.ceylon (53:4-55:4)
    function tst$388(x$389){
        return x$389((0));
    };tst$388.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'x',$mt:'prm',$pt:'f',$t:{t:$$$cl1.Boolean},$an:function(){return[];}}],d:['functions','testMethodReference','$m','tst']};};
    $$$c2.check(tst$388($$$cl1.$JsCallable((opt$390=obj1$386,$$$cl1.JsCallable(opt$390,opt$390!==null?opt$390.f:null)),[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Boolean}})),$$$cl1.String("Reference to method",19));
    var opt$390;
    $$$c2.check(tst$388($$$cl1.$JsCallable((opt$391=obj2$387,$$$cl1.JsCallable(opt$391,opt$391!==null?opt$391.defines:null)),[{$nm:'index',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Boolean}})),$$$cl1.String("Reference to method from ceylon.language",40));
    var opt$391;
};testMethodReference.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testMethodReference']};};

//MethodDef defParamTest at functions.ceylon (60:0-62:0)
function defParamTest(i1$392,i2$393,i3$394){
    if(i2$393===undefined){i2$393=i1$392.plus((1));}
    if(i3$394===undefined){i3$394=i1$392.plus(i2$393);}
    return $$$cl1.StringBuilder().appendAll([i1$392.string,$$$cl1.String(",",1),i2$393.string,$$$cl1.String(",",1),i3$394.string]).string;
};defParamTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','defParamTest']};};

//ClassDef DefParamTest1 at functions.ceylon (63:0-65:0)
function DefParamTest1(i1$395, i2$396, i3$397, $$defParamTest1){
    $init$DefParamTest1();
    if ($$defParamTest1===undefined)$$defParamTest1=new DefParamTest1.$$;
    $$defParamTest1.i1$395_=i1$395;
    if(i2$396===undefined){i2$396=$$defParamTest1.i1$395.plus((1));}
    $$defParamTest1.i2$396_=i2$396;
    if(i3$397===undefined){i3$397=$$defParamTest1.i1$395.plus($$defParamTest1.i2$396);}
    $$defParamTest1.i3$397_=i3$397;
    
    //AttributeDecl s at functions.ceylon (64:4-64:44)
    $$defParamTest1.s$398_=$$$cl1.StringBuilder().appendAll([$$defParamTest1.i1$395.string,$$$cl1.String(",",1),$$defParamTest1.i2$396.string,$$$cl1.String(",",1),$$defParamTest1.i3$397.string]).string;
    $$defParamTest1.$prop$getS={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:DefParamTest1,$an:function(){return[$$$cl1.shared()];},d:['functions','DefParamTest1','$at','s']};}};
    $$defParamTest1.$prop$getS.get=function(){return s};
    return $$defParamTest1;
}
DefParamTest1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','DefParamTest1']};};
function $init$DefParamTest1(){
    if (DefParamTest1.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest1,'functions::DefParamTest1',$$$cl1.Basic);
        (function($$defParamTest1){
            
            //AttributeDecl s at functions.ceylon (64:4-64:44)
            $$$cl1.defineAttr($$defParamTest1,'s',function(){return this.s$398_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:DefParamTest1,$an:function(){return[$$$cl1.shared()];},d:['functions','DefParamTest1','$at','s']};});
            $$$cl1.defineAttr($$defParamTest1,'i1$395',function(){return this.i1$395_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:DefParamTest1,d:['functions','DefParamTest1','$at','i1']};});
            $$$cl1.defineAttr($$defParamTest1,'i2$396',function(){return this.i2$396_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:DefParamTest1,d:['functions','DefParamTest1','$at','i2']};});
            $$$cl1.defineAttr($$defParamTest1,'i3$397',function(){return this.i3$397_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:DefParamTest1,d:['functions','DefParamTest1','$at','i3']};});
        })(DefParamTest1.$$.prototype);
    }
    return DefParamTest1;
}
exports.$init$DefParamTest1=$init$DefParamTest1;
$init$DefParamTest1();

//ClassDef DefParamTest2 at functions.ceylon (66:0-68:0)
function DefParamTest2(i1$399, i2$400, i3$401, $$defParamTest2){
    $init$DefParamTest2();
    if ($$defParamTest2===undefined)$$defParamTest2=new DefParamTest2.$$;
    $$defParamTest2.i1$399_=i1$399;
    if(i2$400===undefined){i2$400=$$defParamTest2.i1$399.plus((1));}
    $$defParamTest2.i2$400_=i2$400;
    if(i3$401===undefined){i3$401=$$defParamTest2.i1$399.plus($$defParamTest2.i2$400);}
    $$defParamTest2.i3$401_=i3$401;
    return $$defParamTest2;
}
DefParamTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','DefParamTest2']};};
function $init$DefParamTest2(){
    if (DefParamTest2.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest2,'functions::DefParamTest2',$$$cl1.Basic);
        (function($$defParamTest2){
            
            //MethodDef f at functions.ceylon (67:4-67:55)
            $$defParamTest2.f=function f(){
                var $$defParamTest2=this;
                return $$$cl1.StringBuilder().appendAll([$$defParamTest2.i1$399.string,$$$cl1.String(",",1),$$defParamTest2.i2$400.string,$$$cl1.String(",",1),$$defParamTest2.i3$401.string]).string;
            };$$defParamTest2.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:DefParamTest2,$an:function(){return[$$$cl1.shared()];},d:['functions','DefParamTest2','$m','f']};};
            $$$cl1.defineAttr($$defParamTest2,'i1$399',function(){return this.i1$399_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:DefParamTest2,d:['functions','DefParamTest2','$at','i1']};});
            $$$cl1.defineAttr($$defParamTest2,'i2$400',function(){return this.i2$400_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:DefParamTest2,d:['functions','DefParamTest2','$at','i2']};});
            $$$cl1.defineAttr($$defParamTest2,'i3$401',function(){return this.i3$401_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:DefParamTest2,d:['functions','DefParamTest2','$at','i3']};});
        })(DefParamTest2.$$.prototype);
    }
    return DefParamTest2;
}
exports.$init$DefParamTest2=$init$DefParamTest2;
$init$DefParamTest2();

//ClassDef DefParamTest3 at functions.ceylon (69:0-73:0)
function DefParamTest3($$defParamTest3){
    $init$DefParamTest3();
    if ($$defParamTest3===undefined)$$defParamTest3=new DefParamTest3.$$;
    return $$defParamTest3;
}
DefParamTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['functions','DefParamTest3']};};
function $init$DefParamTest3(){
    if (DefParamTest3.$$===undefined){
        $$$cl1.initTypeProto(DefParamTest3,'functions::DefParamTest3',$$$cl1.Basic);
        (function($$defParamTest3){
            
            //MethodDef f at functions.ceylon (70:4-72:4)
            $$defParamTest3.f$defs$i2=function(i1$402,i2$403,i3$404){var $$defParamTest3=this;
            return i1$402.plus((1));};
            $$defParamTest3.f$defs$i3=function(i1$402,i2$403,i3$404){var $$defParamTest3=this;
            return i1$402.plus(i2$403);};
            $$defParamTest3.f=function f(i1$402,i2$403,i3$404){
                var $$defParamTest3=this;
                if(i2$403===undefined){i2$403=$$defParamTest3.f$defs$i2(i1$402,i2$403,i3$404);}
                if(i3$404===undefined){i3$404=$$defParamTest3.f$defs$i3(i1$402,i2$403,i3$404);}
                return $$$cl1.StringBuilder().appendAll([i1$402.string,$$$cl1.String(",",1),i2$403.string,$$$cl1.String(",",1),i3$404.string]).string;
            };$$defParamTest3.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i3',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:DefParamTest3,$an:function(){return[$$$cl1.shared()];},d:['functions','DefParamTest3','$m','f']};};
        })(DefParamTest3.$$.prototype);
    }
    return DefParamTest3;
}
exports.$init$DefParamTest3=$init$DefParamTest3;
$init$DefParamTest3();

//MethodDef testDefaultedParams at functions.ceylon (74:0-103:0)
function testDefaultedParams(){
    $$$c2.check(defParamTest((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters 1",22));
    $$$c2.check(defParamTest((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters 2",22));
    $$$c2.check(defParamTest((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters 3",22));
    $$$c2.check((i1$405=(1),defParamTest(i1$405,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters named 1",28));
    var i1$405;
    $$$c2.check((i1$406=(1),i2$407=(3),defParamTest(i1$406,i2$407,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters named 2",28));
    var i1$406,i2$407;
    $$$c2.check((i1$408=(1),i3$409=(0),defParamTest(i1$408,undefined,i3$409)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters named 3",28));
    var i1$408,i3$409;
    $$$c2.check(DefParamTest1((1)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class 1",28));
    $$$c2.check(DefParamTest1((1),(3)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class 2",28));
    $$$c2.check(DefParamTest1((1),(3),(0)).s.equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class 3",28));
    $$$c2.check((i1$410=(1),DefParamTest1(i1$410,undefined,undefined)).s.equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class named 1",34));
    var i1$410;
    $$$c2.check((i1$411=(1),i2$412=(3),DefParamTest1(i1$411,i2$412,undefined)).s.equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class named 2",34));
    var i1$411,i2$412;
    $$$c2.check((i1$413=(1),i3$414=(0),DefParamTest1(i1$413,undefined,i3$414)).s.equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class named 3",34));
    var i1$413,i3$414;
    $$$c2.check(DefParamTest2((1)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 1",29));
    $$$c2.check(DefParamTest2((1),(3)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 2",29));
    $$$c2.check(DefParamTest2((1),(3),(0)).f().equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted parameters class2 3",29));
    $$$c2.check((i1$415=(1),DefParamTest2(i1$415,undefined,undefined)).f().equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted parameters class2 named 1",35));
    var i1$415;
    $$$c2.check((i1$416=(1),i2$417=(3),DefParamTest2(i1$416,i2$417,undefined)).f().equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted parameters class2 named 2",35));
    var i1$416,i2$417;
    $$$c2.check((i1$418=(1),i3$419=(0),DefParamTest2(i1$418,undefined,i3$419)).f().equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted parameters class2 named 3",35));
    var i1$418,i3$419;
    
    //AttributeDecl tst at functions.ceylon (96:4-96:31)
    var tst$420=DefParamTest3();
    $$$c2.check(tst$420.f((1)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters 1",29));
    $$$c2.check(tst$420.f((1),(3)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters 2",29));
    $$$c2.check(tst$420.f((1),(3),(0)).equals($$$cl1.String("1,3,0",5)),$$$cl1.String("defaulted method parameters 3",29));
    $$$c2.check((i1$421=(1),(opt$422=tst$420,$$$cl1.JsCallable(opt$422,opt$422!==null?opt$422.f:null))(i1$421,undefined,undefined)).equals($$$cl1.String("1,2,3",5)),$$$cl1.String("defaulted method parameters named 1",35));
    var i1$421,opt$422;
    $$$c2.check((i1$423=(1),i2$424=(3),(opt$425=tst$420,$$$cl1.JsCallable(opt$425,opt$425!==null?opt$425.f:null))(i1$423,i2$424,undefined)).equals($$$cl1.String("1,3,4",5)),$$$cl1.String("defaulted method parameters named 2",35));
    var i1$423,i2$424,opt$425;
    $$$c2.check((i1$426=(1),i3$427=(0),(opt$428=tst$420,$$$cl1.JsCallable(opt$428,opt$428!==null?opt$428.f:null))(i1$426,undefined,i3$427)).equals($$$cl1.String("1,2,0",5)),$$$cl1.String("defaulted method parameters named 3",35));
    var i1$426,i3$427,opt$428;
};testDefaultedParams.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testDefaultedParams']};};

//MethodDef testGetterMethodDefinitions at functions.ceylon (105:0-114:0)
function testGetterMethodDefinitions(){
    
    //ClassDef GetterTest at functions.ceylon (106:2-109:2)
    function GetterTest$429($$getterTest$429){
        $init$GetterTest$429();
        if ($$getterTest$429===undefined)$$getterTest$429=new GetterTest$429.$$;
        
        //AttributeDecl i at functions.ceylon (107:4-107:24)
        $$getterTest$429.i$430_=(0);
        $$getterTest$429.$prop$getI$430={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:GetterTest$429,$an:function(){return[$$$cl1.variable()];},d:['functions','testGetterMethodDefinitions','$c','GetterTest','$at','i']};}};
        $$getterTest$429.$prop$getI$430.get=function(){return i$430};
        return $$getterTest$429;
    }
    GetterTest$429.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['functions','testGetterMethodDefinitions','$c','GetterTest']};};
    function $init$GetterTest$429(){
        if (GetterTest$429.$$===undefined){
            $$$cl1.initTypeProto(GetterTest$429,'functions::testGetterMethodDefinitions.GetterTest',$$$cl1.Basic);
            (function($$getterTest$429){
                
                //AttributeDecl i at functions.ceylon (107:4-107:24)
                $$$cl1.defineAttr($$getterTest$429,'i$430',function(){return this.i$430_;},function(i$431){return this.i$430_=i$431;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:GetterTest$429,$an:function(){return[$$$cl1.variable()];},d:['functions','testGetterMethodDefinitions','$c','GetterTest','$at','i']};});
                
                //AttributeGetterDef x at functions.ceylon (108:4-108:35)
                $$$cl1.defineAttr($$getterTest$429,'x',function(){
                    var $$getterTest$429=this;
                    return ($$getterTest$429.i$430=$$getterTest$429.i$430.successor);
                },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:GetterTest$429,$an:function(){return[$$$cl1.shared()];},d:['functions','testGetterMethodDefinitions','$c','GetterTest','$at','x']};});
            })(GetterTest$429.$$.prototype);
        }
        return GetterTest$429;
    }
    $init$GetterTest$429();
    
    //AttributeDecl gt at functions.ceylon (110:2-110:25)
    var gt$432=GetterTest$429();
    $$$c2.check(gt$432.x.equals((1)),$$$cl1.String("getter defined as method 1",26));
    $$$c2.check(gt$432.x.equals((2)),$$$cl1.String("getter defined as method 2",26));
    $$$c2.check(gt$432.x.equals((3)),$$$cl1.String("getter defined as method 3",26));
}
exports.testGetterMethodDefinitions=testGetterMethodDefinitions;
testGetterMethodDefinitions.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['functions','testGetterMethodDefinitions']};};

//MethodDef namedArgFunc at functions.ceylon (116:0-120:0)
function namedArgFunc(x$433,y$434,z$435){
    if(x$433===undefined){x$433=$$$cl1.String("x",1);}
    if(y$434===undefined){y$434=x$433.plus($$$cl1.String("y",1));}
    if(z$435===undefined){z$435=$$$cl1.getEmpty();}
    
    //AttributeDecl result at functions.ceylon (117:4-117:40)
    var result$436=x$433.plus($$$cl1.String(",",1)).plus(y$434);
    function setResult$436(result$437){return result$436=result$437;};
    //'for' statement at functions.ceylon (118:4-118:38)
    var it$438 = z$435.iterator();
    var s$439;while ((s$439=it$438.next())!==$$$cl1.getFinished()){
        (result$436=result$436.plus($$$cl1.String(",",1).plus(s$439)));
    }
    return result$436;
};namedArgFunc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$def:1,$t:{t:$$$cl1.String},$an:function(){return[];}},{$nm:'y',$mt:'prm',$def:1,$t:{t:$$$cl1.String},$an:function(){return[];}},{$nm:'z',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},$an:function(){return[];}}],d:['functions','namedArgFunc']};};

//ClassDef Issue105 at functions.ceylon (122:0-124:0)
function Issue105(i, more$440, $$issue105){
    $init$Issue105();
    if ($$issue105===undefined)$$issue105=new Issue105.$$;
    if(more$440===undefined){more$440=$$$cl1.getEmpty();}
    $$issue105.more$440_=more$440;
    
    //AttributeDecl i at functions.ceylon (123:4-123:20)
    $$issue105.i$441_=i;
    $$issue105.$prop$getI={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue105,$an:function(){return[$$$cl1.shared()];},d:['functions','Issue105','$at','i']};}};
    $$issue105.$prop$getI.get=function(){return i};
    return $$issue105;
}
Issue105.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[$$$cl1.shared()];}},{$nm:'more',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:Issue105}}},$an:function(){return[];}}],d:['functions','Issue105']};};
function $init$Issue105(){
    if (Issue105.$$===undefined){
        $$$cl1.initTypeProto(Issue105,'functions::Issue105',$$$cl1.Basic);
        (function($$issue105){
            
            //AttributeDecl i at functions.ceylon (123:4-123:20)
            $$$cl1.defineAttr($$issue105,'i',function(){return this.i$441_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Issue105,$an:function(){return[$$$cl1.shared()];},d:['functions','Issue105','$at','i']};});
            $$$cl1.defineAttr($$issue105,'more$440',function(){return this.more$440_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:Issue105}}},$cont:Issue105,d:['functions','Issue105','$at','more']};});
        })(Issue105.$$.prototype);
    }
    return Issue105;
}
exports.$init$Issue105=$init$Issue105;
$init$Issue105();

//MethodDef testNamedArguments at functions.ceylon (126:0-142:0)
function testNamedArguments(){
    $$$c2.check((namedArgFunc(undefined,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("x,xy",4)),$$$cl1.String("named arguments 1",17));
    $$$c2.check((x$442=$$$cl1.String("a",1),namedArgFunc(x$442,undefined,$$$cl1.getEmpty())).equals($$$cl1.String("a,ay",4)),$$$cl1.String("named arguments 2",17));
    var x$442;
    $$$c2.check((y$443=$$$cl1.String("b",1),namedArgFunc(undefined,y$443,$$$cl1.getEmpty())).equals($$$cl1.String("x,b",3)),$$$cl1.String("named arguments 3",17));
    var y$443;
    $$$c2.check((z$444=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$444)).equals($$$cl1.String("x,xy,c",6)),$$$cl1.String("named arguments 4",17));
    var z$444;
    $$$c2.check((x$445=$$$cl1.String("a",1),y$446=$$$cl1.String("b",1),z$447=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$445,y$446,z$447)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 5",17));
    var x$445,y$446,z$447;
    $$$c2.check((y$448=$$$cl1.String("b",1),x$449=$$$cl1.String("a",1),z$450=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$449,y$448,z$450)).equals($$$cl1.String("a,b,c,d",7)),$$$cl1.String("named arguments 6",17));
    var y$448,x$449,z$450;
    $$$c2.check((x$451=$$$cl1.String("a",1),z$452=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(x$451,undefined,z$452)).equals($$$cl1.String("a,ay,c",6)),$$$cl1.String("named arguments 7",17));
    var x$451,z$452;
    $$$c2.check((y$453=$$$cl1.String("b",1),z$454=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,y$453,z$454)).equals($$$cl1.String("x,b,c",5)),$$$cl1.String("named arguments 8",17));
    var y$453,z$454;
    $$$c2.check((y$455=$$$cl1.String("b",1),x$456=$$$cl1.String("a",1),namedArgFunc(x$456,y$455,$$$cl1.getEmpty())).equals($$$cl1.String("a,b",3)),$$$cl1.String("named arguments 9",17));
    var y$455,x$456;
    $$$c2.check((z$457=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.Tuple($$$cl1.String("d",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),namedArgFunc(undefined,undefined,z$457)).equals($$$cl1.String("x,xy,c,d",8)),$$$cl1.String("named arguments 11",18));
    var z$457;
    $$$c2.check((y$458=$$$cl1.String("b",1),z$459=$$$cl1.Tuple($$$cl1.String("c",1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),x$460=$$$cl1.String("a",1),namedArgFunc(x$460,y$458,z$459)).equals($$$cl1.String("a,b,c",5)),$$$cl1.String("named arguments 12",18));
    var y$458,z$459,x$460;
    
    //AttributeDecl issue105 at functions.ceylon (140:4-140:64)
    var issue105$461=(i$462=(1),more$463=$$$cl1.Tuple((i$464=(2),Issue105(i$464,$$$cl1.getEmpty())),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:Issue105},Element:{t:Issue105}}),Issue105(i$462,more$463));
    var i$462,more$463,i$464;
    $$$c2.check(issue105$461.i.equals((1)),$$$cl1.String("issue #105",10));
};testNamedArguments.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testNamedArguments']};};

//InterfaceDef LazyExprBase at functions.ceylon (144:0-147:0)
function LazyExprBase($$lazyExprBase){
}
LazyExprBase.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['functions','LazyExprBase']};};
function $init$LazyExprBase(){
    if (LazyExprBase.$$===undefined){
        $$$cl1.initTypeProtoI(LazyExprBase,'functions::LazyExprBase');
        (function($$lazyExprBase){
            
            //AttributeDecl s1 at functions.ceylon (145:4-145:27)
            $$lazyExprBase.$prop$getS1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprBase,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['functions','LazyExprBase','$at','s1']};}};
            $$lazyExprBase.s2={$fml:1,$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:LazyExprBase,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['functions','LazyExprBase','$m','s2']};}};
        })(LazyExprBase.$$.prototype);
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
    $$lazyExprTest.x$465_=(1000);
    $$lazyExprTest.$prop$getX={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['functions','LazyExprTest','$at','x']};}};
    $$lazyExprTest.$prop$getX.get=function(){return x};
    
    //AttributeDecl i1 at functions.ceylon (152:4-152:28)
    $$lazyExprTest.$prop$getI1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared()];},d:['functions','LazyExprTest','$at','i1']};}};
    $$lazyExprTest.$prop$getI1.get=function(){return i1};
    
    //AttributeDecl i2 at functions.ceylon (153:4-153:21)
    $$lazyExprTest.$prop$getI2={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared()];},d:['functions','LazyExprTest','$at','i2']};}};
    return $$lazyExprTest;
}
LazyExprTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],satisfies:[{t:LazyExprBase}],d:['functions','LazyExprTest']};};
function $init$LazyExprTest(){
    if (LazyExprTest.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest,'functions::LazyExprTest',$$$cl1.Basic,$init$LazyExprBase());
        (function($$lazyExprTest){
            
            //AttributeDecl x at functions.ceylon (149:4-149:36)
            $$$cl1.defineAttr($$lazyExprTest,'x',function(){return this.x$465_;},function(x$466){return this.x$465_=x$466;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['functions','LazyExprTest','$at','x']};});
            
            //MethodDecl f1 at functions.ceylon (150:4-150:83)
            $$lazyExprTest.f1$defs$f=function(i$467,f$468){var $$lazyExprTest=this;
            return function (){
                return $$$cl1.StringBuilder().appendAll([i$467.string,$$$cl1.String(".",1),$$lazyExprTest.x.plus((1)).string]).string;
            };};
            $$lazyExprTest.f1=function (i$467,f$468){
                var $$lazyExprTest=this;
                if(f$468===undefined){f$468=$$lazyExprTest.f1$defs$f(i$467,f$468);}
                return $$$cl1.StringBuilder().appendAll([i$467.string,$$$cl1.String(":",1),f$468().string]).string;
            };
            $$lazyExprTest.f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl1.String},$an:function(){return[];}}],$cont:LazyExprTest,$an:function(){return[$$$cl1.shared()];},d:['functions','LazyExprTest','$m','f1']};};
            
            //MethodDecl f2 at functions.ceylon (151:4-151:45)
            $$lazyExprTest.f2=function (i$469){
                var $$lazyExprTest=this;
                return (2).times(($$lazyExprTest.x=$$lazyExprTest.x.successor)).plus(i$469);
            };
            $$lazyExprTest.f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:LazyExprTest,$an:function(){return[$$$cl1.shared()];},d:['functions','LazyExprTest','$m','f2']};};
            
            //AttributeDecl i1 at functions.ceylon (152:4-152:28)
            $$$cl1.defineAttr($$lazyExprTest,'i1',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared()];},d:['functions','LazyExprTest','$at','i1']};});
            
            //AttributeDecl i2 at functions.ceylon (153:4-153:21)
            $$$cl1.defineAttr($$lazyExprTest,'i2',function(){
                var $$lazyExprTest=this;
                return ($$lazyExprTest.x=$$lazyExprTest.x.successor).times((2));
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared(),$$$cl1.shared(),$$$cl1.actual()];},d:['functions','LazyExprTest','$at','i2']};});
            $$$cl1.defineAttr($$lazyExprTest,'s1',function(){
                var $$lazyExprTest=this;
                return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl1.String(".1",2)]).string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprTest,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','LazyExprTest','$at','s1']};});
            $$lazyExprTest.s2=function (i$470){
                var $$lazyExprTest=this;
                return $$$cl1.StringBuilder().appendAll([($$lazyExprTest.x=$$lazyExprTest.x.successor).string,$$$cl1.String(".",1),i$470.string]).string;
            };
            //MethodDecl f3 at functions.ceylon (158:4-158:51)
            $$lazyExprTest.f3=function (f$471){
                var $$lazyExprTest=this;
                return f$471(($$lazyExprTest.x=$$lazyExprTest.x.successor));
            };
            $$lazyExprTest.f3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl1.String},$an:function(){return[];}}],$cont:LazyExprTest,$an:function(){return[$$$cl1.shared()];},d:['functions','LazyExprTest','$m','f3']};};
        })(LazyExprTest.$$.prototype);
    }
    return LazyExprTest;
}
exports.$init$LazyExprTest=$init$LazyExprTest;
$init$LazyExprTest();

//AttributeDecl lx at functions.ceylon (161:0-161:26)
var lx$472;function $valinit$lx$472(){if (lx$472===undefined)lx$472=(1000);return lx$472;};$valinit$lx$472();
function getLx(){return $valinit$lx$472();}
exports.getLx=getLx;
function setLx(lx$473){return lx$472=lx$473;};
exports.setLx=setLx;
var $prop$getLx={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$an:function(){return[$$$cl1.variable()];},d:['functions','lx']};}};
exports.$prop$getLx=$prop$getLx;
$prop$getLx.get=getLx;
getLx.$$metamodel$$=$prop$getLx.$$metamodel$$;
$prop$getLx.set=setLx;
if (setLx.$$metamodel$$===undefined)setLx.$$metamodel$$=$prop$getLx.$$metamodel$$;

//MethodDecl lazy_f1 at functions.ceylon (162:0-162:66)
var lazy_f1=function (i$474,f$475){
    if(f$475===undefined){f$475=function (){
        return $$$cl1.StringBuilder().appendAll([i$474.string,$$$cl1.String(".",1),getLx().plus((1)).string]).string;
    };}
    return f$475();
};
lazy_f1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','lazy_f1']};};

//MethodDecl lazy_f2 at functions.ceylon (163:0-163:40)
var lazy_f2=function (i$476){
    return (2).times((setLx(getLx().successor))).plus(i$476);
};
lazy_f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','lazy_f2']};};

//AttributeDecl lazy_i1 at functions.ceylon (164:0-164:23)
function getLazy_i1(){return (setLx(getLx().successor));};
exports.getLazy_i1=getLazy_i1;
var $prop$getLazy_i1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['functions','lazy_i1']};}};
exports.$prop$getLazy_i1=$prop$getLazy_i1;
$prop$getLazy_i1.get=getLazy_i1;
getLazy_i1.$$metamodel$$=$prop$getLazy_i1.$$metamodel$$;

//ClassDef LazyExprTest2 at functions.ceylon (166:0-170:0)
function LazyExprTest2($$lazyExprTest2){
    $init$LazyExprTest2();
    if ($$lazyExprTest2===undefined)$$lazyExprTest2=new LazyExprTest2.$$;
    LazyExprBase($$lazyExprTest2);
    
    //AttributeDecl x at functions.ceylon (167:4-167:35)
    $$lazyExprTest2.x$477_=(1000);
    $$lazyExprTest2.$prop$getX.get=function(){return x};
    
    //AttributeDecl s1 at functions.ceylon (168:4-168:51)
    $$lazyExprTest2.$prop$getS1.get=function(){return s1};
    return $$lazyExprTest2;
}
LazyExprTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],satisfies:[{t:LazyExprBase}],d:['functions','LazyExprTest2']};};
function $init$LazyExprTest2(){
    if (LazyExprTest2.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest2,'functions::LazyExprTest2',$$$cl1.Basic,$init$LazyExprBase());
        (function($$lazyExprTest2){
            
            //AttributeDecl x at functions.ceylon (167:4-167:35)
            $$$cl1.defineAttr($$lazyExprTest2,'x',function(){return this.x$477_;},function(x$478){return this.x$477_=x$478;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:LazyExprTest2,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['functions','LazyExprTest2','$at','x']};});
            
            //AttributeDecl s1 at functions.ceylon (168:4-168:51)
            $$$cl1.defineAttr($$lazyExprTest2,'s1',function(){
                var $$lazyExprTest2=this;
                return ($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprTest2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['functions','LazyExprTest2','$at','s1']};});
            
            //MethodDecl s2 at functions.ceylon (169:4-169:65)
            $$lazyExprTest2.s2=function (i$479){
                var $$lazyExprTest2=this;
                return $$$cl1.StringBuilder().appendAll([($$lazyExprTest2.x=$$lazyExprTest2.x.successor).string,$$$cl1.String("-",1),i$479.string]).string;
            };
            $$lazyExprTest2.s2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:LazyExprTest2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['functions','LazyExprTest2','$m','s2']};};
        })(LazyExprTest2.$$.prototype);
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
    $$lazyExprTest3.s1$480_=$$$cl1.String("s1",2);
    $$lazyExprTest3.$prop$getS1.get=function(){return s1};
    return $$lazyExprTest3;
}
LazyExprTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:LazyExprTest2},$ps:[],d:['functions','LazyExprTest3']};};
function $init$LazyExprTest3(){
    if (LazyExprTest3.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest3,'functions::LazyExprTest3',$init$LazyExprTest2());
        (function($$lazyExprTest3){
            
            //AttributeDecl s1 at functions.ceylon (172:4-172:43)
            $$$cl1.defineAttr($$lazyExprTest3,'s1',function(){return this.s1$480_;},function(s1$481){return this.s1$480_=s1$481;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprTest3,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.variable()];},d:['functions','LazyExprTest3','$at','s1']};});
        })(LazyExprTest3.$$.prototype);
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
    
    //AttributeDecl assigned at functions.ceylon (175:4-175:40)
    $$lazyExprTest4.assigned$482_=$$$cl1.String("",0);
    $$lazyExprTest4.$prop$getAssigned={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprTest4,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['functions','LazyExprTest4','$at','assigned']};}};
    $$lazyExprTest4.$prop$getAssigned.get=function(){return assigned};
    return $$lazyExprTest4;
}
LazyExprTest4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:LazyExprTest2},$ps:[],d:['functions','LazyExprTest4']};};
function $init$LazyExprTest4(){
    if (LazyExprTest4.$$===undefined){
        $$$cl1.initTypeProto(LazyExprTest4,'functions::LazyExprTest4',$init$LazyExprTest2());
        (function($$lazyExprTest4){
            
            //AttributeDecl assigned at functions.ceylon (175:4-175:40)
            $$$cl1.defineAttr($$lazyExprTest4,'assigned',function(){return this.assigned$482_;},function(assigned$483){return this.assigned$482_=assigned$483;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprTest4,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['functions','LazyExprTest4','$at','assigned']};});
            
            //AttributeGetterDef s1 at functions.ceylon (176:4-176:56)
            $$$cl1.defineAttr($$lazyExprTest4,'s1',function(){
                var $$lazyExprTest4=this;
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("s1-",3),$$$cl1.attrGetter($$lazyExprTest4.getT$all()['functions::LazyExprTest2'],'s1').call(this).string]).string;
            },function(s1$484){
                var $$lazyExprTest4=this;
                $$lazyExprTest4.assigned=s1$484;
            },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:LazyExprTest4,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','LazyExprTest4','$at','s1']};});
        })(LazyExprTest4.$$.prototype);
    }
    return LazyExprTest4;
}
exports.$init$LazyExprTest4=$init$LazyExprTest4;
$init$LazyExprTest4();

//MethodDef testLazyExpressions at functions.ceylon (180:0-225:0)
function testLazyExpressions(){
    
    //AttributeDecl tst at functions.ceylon (181:4-181:30)
    var tst$485=LazyExprTest();
    (tst$485.x=(1));
    $$$c2.check(tst$485.f1((3)).equals($$$cl1.String("3:3.2",5)),$$$cl1.String("=> defaulted param",18));
    $$$c2.check(tst$485.f2((3)).equals((7)),$$$cl1.String("=> method",9));
    $$$c2.check(tst$485.i1.equals((3)),$$$cl1.String("=> attribute",12));
    $$$c2.check(tst$485.i2.equals((8)),$$$cl1.String("=> attribute specifier",22));
    $$$c2.check(tst$485.s1.equals($$$cl1.String("5.1",3)),$$$cl1.String("=> attribute refinement",23));
    $$$c2.check(tst$485.s2((5)).equals($$$cl1.String("6.5",3)),$$$cl1.String("=> method refinement",20));
    setLx((1));
    $$$c2.check(lazy_f1((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param toplevel",27));
    $$$c2.check(lazy_f2((3)).equals((7)),$$$cl1.String("=> method toplevel",18));
    $$$c2.check(getLazy_i1().equals((3)),$$$cl1.String("=> attribute toplevel",21));
    
    //AttributeDecl x at functions.ceylon (195:4-195:29)
    var x$486=(1000);
    function setX$486(x$487){return x$486=x$487;};
    
    //MethodDecl f1 at functions.ceylon (196:4-196:64)
    var f1$488=function (i$489,f$490){
        if(f$490===undefined){f$490=function (){
            return $$$cl1.StringBuilder().appendAll([i$489.string,$$$cl1.String(".",1),x$486.plus((1)).string]).string;
        };}
        return f$490();
    };
    f1$488.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'f',$mt:'prm',$pt:'f',$def:1,$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','testLazyExpressions','$m','f1']};};
    
    //MethodDecl f2 at functions.ceylon (197:4-197:38)
    var f2$491=function (i$492){
        return (2).times((x$486=x$486.successor)).plus(i$492);
    };
    f2$491.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','testLazyExpressions','$m','f2']};};
    
    //AttributeDecl i1 at functions.ceylon (198:4-198:21)
    function getI1$493(){return (x$486=x$486.successor);};
    
    //AttributeDecl i2 at functions.ceylon (199:4-199:14)
    var i2$494;
    var getI2$494=function(){
        return (x$486=x$486.successor).times((2));
    };
    x$486=(1);
    $$$c2.check(f1$488((3)).equals($$$cl1.String("3.2",3)),$$$cl1.String("=> defaulted param local",24));
    $$$c2.check(f2$491((3)).equals((7)),$$$cl1.String("=> method local",15));
    $$$c2.check(getI1$493().equals((3)),$$$cl1.String("=> attribute local",18));
    $$$c2.check(getI2$494().equals((8)),$$$cl1.String("=> attribute specifier local",28));
    
    //AttributeDecl tst3 at functions.ceylon (208:4-208:32)
    var tst3$495=LazyExprTest3();
    (tst3$495.x=(1));
    $$$c2.check(tst3$495.s1.equals($$$cl1.String("s1",2)),$$$cl1.String("=> override variable 1",22));
    (tst3$495.s1=$$$cl1.String("abc",3));
    $$$c2.check(tst3$495.s1.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override variable 2",22));
    
    //AttributeDecl tst4 at functions.ceylon (213:4-213:32)
    var tst4$496=LazyExprTest4();
    (tst4$496.x=(1));
    $$$c2.check(tst4$496.s1.equals($$$cl1.String("s1-2",4)),$$$cl1.String("=> override getter/setter 1",27));
    (tmp$497=tst4$496,tmp$497.s1=$$$cl1.String("abc",3),tmp$497.s1);
    var tmp$497;
    $$$c2.check(tst4$496.s1.equals($$$cl1.String("s1-4",4)),$$$cl1.String("=> override getter/setter 2",27));
    $$$c2.check(tst4$496.assigned.equals($$$cl1.String("abc",3)),$$$cl1.String("=> override getter/setter 3",27));
    (tst$485.x=(1));
    x$486=(10);
    $$$c2.check((i$498=(function(){
        //AttributeArgument i at functions.ceylon (222:17-222:23)
        return (x$486=x$486.successor);
    }()),(opt$499=tst$485,$$$cl1.JsCallable(opt$499,opt$499!==null?opt$499.f1:null))(i$498,undefined)).equals($$$cl1.String("11:11.2",7)),$$$cl1.String("=> named arg",12));
    var i$498,opt$499;
    $$$c2.check((i$500=(function(){
        //AttributeArgument i at functions.ceylon (223:17-223:23)
        return (x$486=x$486.successor);
    }()),f$501=function (){return (x$486=x$486.successor).string;},(opt$502=tst$485,$$$cl1.JsCallable(opt$502,opt$502!==null?opt$502.f1:null))(i$500,f$501)).equals($$$cl1.String("12:13",5)),$$$cl1.String("=> named arg function",21));
    var i$500,f$501,opt$502;
    $$$c2.check((f$503=function (i$504){return $$$cl1.StringBuilder().appendAll([i$504.string,$$$cl1.String("-",1),(x$486=x$486.successor).string]).string;},(opt$505=tst$485,$$$cl1.JsCallable(opt$505,opt$505!==null?opt$505.f3:null))(f$503)).equals($$$cl1.String("2-14",4)),$$$cl1.String("=> named arg function with param",32));
    var f$503,opt$505;
};testLazyExpressions.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testLazyExpressions']};};

//MethodDef test at functions.ceylon (227:0-243:0)
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
    testStaticMethodReferences();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['functions','test']};};

//ClassDef MethodRefTest at method_refs.ceylon (4:0-13:0)
function MethodRefTest(name, $$methodRefTest){
    $init$MethodRefTest();
    if ($$methodRefTest===undefined)$$methodRefTest=new MethodRefTest.$$;
    
    //AttributeDecl name at method_refs.ceylon (5:4-5:22)
    $$methodRefTest.name$506_=name;
    $$methodRefTest.$prop$getName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MethodRefTest,$an:function(){return[$$$cl1.shared()];},d:['functions','MethodRefTest','$at','name']};}};
    $$methodRefTest.$prop$getName.get=function(){return name};
    
    //AttributeDecl string at method_refs.ceylon (6:4-6:59)
    $$methodRefTest.$prop$getString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MethodRefTest,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MethodRefTest','$at','string']};}};
    $$methodRefTest.$prop$getString.get=function(){return string};
    return $$methodRefTest;
}
MethodRefTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[$$$cl1.shared()];}}],d:['functions','MethodRefTest']};};
function $init$MethodRefTest(){
    if (MethodRefTest.$$===undefined){
        $$$cl1.initTypeProto(MethodRefTest,'functions::MethodRefTest',$$$cl1.Basic);
        (function($$methodRefTest){
            
            //AttributeDecl name at method_refs.ceylon (5:4-5:22)
            $$$cl1.defineAttr($$methodRefTest,'name',function(){return this.name$506_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MethodRefTest,$an:function(){return[$$$cl1.shared()];},d:['functions','MethodRefTest','$at','name']};});
            
            //AttributeDecl string at method_refs.ceylon (6:4-6:59)
            $$$cl1.defineAttr($$methodRefTest,'string',function(){
                var $$methodRefTest=this;
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("MethodRefTest ",14),$$methodRefTest.name.string]).string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MethodRefTest,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MethodRefTest','$at','string']};});
            
            //MethodDecl suffix at method_refs.ceylon (7:4-7:58)
            $$methodRefTest.suffix=function (x$507){
                var $$methodRefTest=this;
                return $$$cl1.StringBuilder().appendAll([$$methodRefTest.string.string,$$$cl1.String(" #",2),x$507.string]).string;
            };
            $$methodRefTest.suffix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MethodRefTest,$an:function(){return[$$$cl1.shared()];},d:['functions','MethodRefTest','$m','suffix']};};
            
            //ClassDef Inner at method_refs.ceylon (9:4-12:4)
            function Inner$MethodRefTest(kid$508, $$inner$MethodRefTest){
                $init$Inner$MethodRefTest();
                if ($$inner$MethodRefTest===undefined)$$inner$MethodRefTest=new this.Inner$MethodRefTest.$$;
                $$inner$MethodRefTest.$$outer=this;
                $$inner$MethodRefTest.kid$508_=kid$508;
                
                //AttributeDecl string at method_refs.ceylon (10:8-10:69)
                $$inner$MethodRefTest.$prop$getString.get=function(){return string};
                return $$inner$MethodRefTest;
            }
            Inner$MethodRefTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'kid',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$cont:MethodRefTest,$an:function(){return[$$$cl1.shared()];},d:['functions','MethodRefTest','$c','Inner']};};
            function $init$Inner$MethodRefTest(){
                if (Inner$MethodRefTest.$$===undefined){
                    $$$cl1.initTypeProto(Inner$MethodRefTest,'functions::MethodRefTest.Inner',$$$cl1.Basic);
                    MethodRefTest.Inner$MethodRefTest=Inner$MethodRefTest;
                    (function($$inner$MethodRefTest){
                        
                        //AttributeDecl string at method_refs.ceylon (10:8-10:69)
                        $$$cl1.defineAttr($$inner$MethodRefTest,'string',function(){
                            var $$inner$MethodRefTest=this;
                            return $$$cl1.StringBuilder().appendAll([$$inner$MethodRefTest.$$outer.string.string,$$$cl1.String(" sub-",5),$$inner$MethodRefTest.kid$508.string]).string;
                        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$MethodRefTest,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['functions','MethodRefTest','$c','Inner','$at','string']};});
                        
                        //MethodDecl prefix at method_refs.ceylon (11:8-11:62)
                        $$inner$MethodRefTest.prefix=function (x$509){
                            var $$inner$MethodRefTest=this;
                            return $$$cl1.StringBuilder().appendAll([$$$cl1.String("#",1),x$509.string,$$$cl1.String(" ",1),$$inner$MethodRefTest.string.string]).string;
                        };
                        $$inner$MethodRefTest.prefix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:Inner$MethodRefTest,$an:function(){return[$$$cl1.shared()];},d:['functions','MethodRefTest','$c','Inner','$m','prefix']};};
                        $$$cl1.defineAttr($$inner$MethodRefTest,'kid$508',function(){return this.kid$508_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$MethodRefTest,d:['functions','MethodRefTest','$c','Inner','$at','kid']};});
                    })(Inner$MethodRefTest.$$.prototype);
                }
                return Inner$MethodRefTest;
            }
            $$methodRefTest.$init$Inner$MethodRefTest=$init$Inner$MethodRefTest;
            $init$Inner$MethodRefTest();
            $$methodRefTest.Inner$MethodRefTest=Inner$MethodRefTest;
        })(MethodRefTest.$$.prototype);
    }
    return MethodRefTest;
}
exports.$init$MethodRefTest=$init$MethodRefTest;
$init$MethodRefTest();

//MethodDef testStaticMethodReferences at method_refs.ceylon (15:0-30:0)
function testStaticMethodReferences(){
    $$$cl1.print($$$cl1.String("Testing static method references...",35));
    
    //AttributeDecl mr at method_refs.ceylon (17:4-17:37)
    var mr$510=MethodRefTest($$$cl1.String("TEST",4));
    
    //AttributeDecl mref at method_refs.ceylon (18:4-18:41)
    var mref$511=$$$cl1.$JsCallable(function($O$) {return $$$cl1.JsCallable($O$,$O$.suffix);}(mr$510),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}});
    $$$c2.check(mref$511((1)).equals($$$cl1.String("MethodRefTest TEST #1",21)),$$$cl1.String("Static method ref 1",19));
    $$$c2.check(function($O$) {return $O$.string;}(mr$510).equals($$$cl1.String("MethodRefTest TEST",18)),$$$cl1.String("Static method ref 2",19));
    $$$c2.check(mref$511((1)).equals(function($O$) {return $$$cl1.JsCallable($O$,$O$.suffix);}(mr$510)((1))),$$$cl1.String("Static method ref 3",19));
    
    //AttributeDecl mri at method_refs.ceylon (22:4-22:30)
    var mri$512=mr$510.Inner$MethodRefTest($$$cl1.String("T2",2));
    
    //AttributeDecl iref at method_refs.ceylon (23:4-23:48)
    var iref$513=$$$cl1.$JsCallable(function($O$) {return $$$cl1.JsCallable($O$,$O$.prefix);}(mri$512),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}});
    $$$c2.check(iref$513((1)).equals($$$cl1.String("#1 MethodRefTest TEST sub-T2",28)),$$$cl1.String("Static method ref 4",19));
    $$$c2.check(function($O$) {return $O$.string;}(mri$512).equals($$$cl1.String("MethodRefTest TEST sub-T2",25)),$$$cl1.String("Static method ref 5",19));
    $$$c2.check(iref$513((1)).equals(function($O$) {return $$$cl1.JsCallable($O$,$O$.prefix);}(mri$512)((1))),$$$cl1.String("Static method ref 6",19));
    
    //AttributeDecl ints at method_refs.ceylon (27:4-27:34)
    var ints$514=(elems$515=[(1),(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),$$$cl1.LazyList(elems$515,{Element:{t:$$$cl1.Integer}}));
    var elems$515;
    $$$c2.check((opt$516=function($O$) {return $$$cl1.JsCallable($O$,$O$.$get);}(ints$514)((1)),opt$516!==null?opt$516:(-(1))).equals((2)),$$$cl1.String("Static method ref 7",19));
    var opt$516;
    $$$c2.check(function($O$) {return $$$cl1.JsCallable($O$,$O$.$map);}(ints$514,{Result:{t:$$$cl1.String}})($$$cl1.$JsCallable((function (x$517){
        return x$517.string;
    }),[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}}),{Result:{t:$$$cl1.String}}).sequence.equals([$$$cl1.String("1",1),$$$cl1.String("2",1),$$$cl1.String("3",1),$$$cl1.String("4",1)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.String}})),$$$cl1.String("Static method ref 8",19));
};testStaticMethodReferences.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testStaticMethodReferences']};};
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef multiCompare at multiples.ceylon (3:0-5:0)
function multiCompare(){
    return function(x$518,y$519){
        return x$518.compare(y$519);
    }
};multiCompare.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Comparison},$ps:[],d:['functions','multiCompare']};};

//MethodDef multiFullname at multiples.ceylon (6:0-8:0)
function multiFullname(nombre$520){
    return function(apat$521){
        return function(amat$522){
            return $$$cl1.StringBuilder().appendAll([nombre$520.string,$$$cl1.String(" ",1),apat$521.string,$$$cl1.String(" ",1),amat$522.string]).string;
        }
    }
};multiFullname.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'nombre',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','multiFullname']};};

//MethodDef multiDefaulted at multiples.ceylon (9:0-11:0)
function multiDefaulted(name$523){
    if(name$523===undefined){name$523=$$$cl1.String("A",1);}
    return function(apat$524){
        return function(amat$525){
            return $$$cl1.StringBuilder().appendAll([name$523.string,$$$cl1.String(" ",1),apat$524.string,$$$cl1.String(" ",1),amat$525.string]).string;
        }
    }
};multiDefaulted.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'name',$mt:'prm',$def:1,$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','multiDefaulted']};};

//MethodDef multiSequenced at multiples.ceylon (12:0-19:0)
function multiSequenced(names$526){
    if(names$526===undefined){names$526=$$$cl1.getEmpty();}
    return function(count$527){
        
        //AttributeDecl sb at multiples.ceylon (13:4-13:30)
        var sb$528=$$$cl1.StringBuilder();
        //'for' statement at multiples.ceylon (14:4-16:4)
        var it$529 = names$526.iterator();
        var name$530;while ((name$530=it$529.next())!==$$$cl1.getFinished()){
            sb$528.append(name$530).append($$$cl1.String(" ",1));
        }
        sb$528.append($$$cl1.String("count ",6)).append(count$527.string);
        return sb$528.string;
    }
};multiSequenced.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'names',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},$an:function(){return[];}}],d:['functions','multiSequenced']};};

//MethodDef testMultipleParamLists at multiples.ceylon (21:0-49:0)
function testMultipleParamLists(){
    $$$cl1.print($$$cl1.String("Testing multiple parameter lists...",35));
    $$$c2.check(multiCompare()((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 1",15));
    $$$c2.check(multiCompare()((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 2",15));
    $$$c2.check(multiCompare()((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 3",15));
    
    //MethodDecl comp at multiples.ceylon (26:4-26:60)
    var comp$531=function (a$532,b$533){
        return multiCompare()(a$532,b$533);
    };
    comp$531.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Comparison},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','comp']};};
    $$$c2.check(comp$531((1),(1)).equals($$$cl1.getEqual()),$$$cl1.String("Multi compare 4",15));
    $$$c2.check(comp$531((1),(2)).equals($$$cl1.getSmaller()),$$$cl1.String("Multi compare 5",15));
    $$$c2.check(comp$531((2),(1)).equals($$$cl1.getLarger()),$$$cl1.String("Multi compare 6",15));
    $$$c2.check(multiFullname($$$cl1.String("a",1))($$$cl1.String("b",1))($$$cl1.String("c",1)).equals($$$cl1.String("a b c",5)),$$$cl1.String("Multi fullname 1",16));
    
    //MethodDecl apat at multiples.ceylon (31:4-31:55)
    var apat$534=function (c$535){
        return multiFullname($$$cl1.String("A",1))($$$cl1.String("B",1))(c$535);
    };
    apat$534.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','apat']};};
    $$$c2.check(apat$534($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi fullname 2",16));
    
    //MethodDecl nombre at multiples.ceylon (33:4-33:61)
    var nombre$536=function (name$537){
        return multiFullname($$$cl1.String("Name",4))(name$537);
    };
    nombre$536.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl1.String}}]},Return:{t:$$$cl1.String}}},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','nombre']};};
    $$$c2.check(nombre$536($$$cl1.String("Z",1))($$$cl1.String("L",1)).equals($$$cl1.String("Name Z L",8)),$$$cl1.String("Multi callable 2",16));
    $$$c2.check(multiDefaulted()($$$cl1.String("B",1))($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 1",17));
    
    //AttributeDecl md1 at multiples.ceylon (37:4-37:69)
    var md1$538=$$$cl1.$JsCallable(multiDefaulted(),[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.String}}],{Arguments:{t:'T', l:[{t:$$$cl1.String}]},Return:{t:$$$cl1.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl1.String}]},Return:{t:$$$cl1.String}}}});
    $$$c2.check(md1$538($$$cl1.String("B",1))($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 2",17));
    $$$c2.check(md1$538($$$cl1.String("B",1))($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 3",17));
    $$$c2.check(md1$538($$$cl1.String("Z",1))($$$cl1.String("C",1)).equals($$$cl1.String("A Z C",5)),$$$cl1.String("Multi defaulted 4",17));
    $$$c2.check(md1$538($$$cl1.String("Y",1))($$$cl1.String("Z",1)).equals($$$cl1.String("A Y Z",5)),$$$cl1.String("Multi defaulted 5",17));
    
    //MethodDecl md2 at multiples.ceylon (42:4-42:52)
    var md2$539=function (x$540){
        return multiDefaulted()($$$cl1.String("B",1))(x$540);
    };
    md2$539.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','md2']};};
    $$$c2.check(md2$539($$$cl1.String("C",1)).equals($$$cl1.String("A B C",5)),$$$cl1.String("Multi defaulted 6",17));
    $$$c2.check(md2$539($$$cl1.String("Z",1)).equals($$$cl1.String("A B Z",5)),$$$cl1.String("Multi defaulted 7",17));
    $$$c2.check(multiSequenced([$$$cl1.String("A",1),$$$cl1.String("B",1),$$$cl1.String("C",1)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))((1)).equals($$$cl1.String("A B C count 1",13)),$$$cl1.String("Multi sequenced 1",17));
    
    //MethodDecl ms1 at multiples.ceylon (46:4-46:59)
    var ms1$541=function (c$542){
        return multiSequenced([$$$cl1.String("x",1),$$$cl1.String("y",1),$$$cl1.String("z",1)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))(c$542);
    };
    ms1$541.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'c',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['functions','testMultipleParamLists','$m','ms1']};};
    $$$c2.check(ms1$541((5)).equals($$$cl1.String("x y z count 5",13)),$$$cl1.String("Multi sequenced 2",17));
    $$$c2.check(ms1$541((10)).equals($$$cl1.String("x y z count 10",14)),$$$cl1.String("Multi sequenced 3",17));
};testMultipleParamLists.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['functions','testMultipleParamLists']};};
exports.$pkg$ans$functions=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}})),$$$cl1.shared()];};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
