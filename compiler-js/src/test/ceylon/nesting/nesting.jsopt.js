(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"nesting","$mod-version":"0.1","nesting":{"Y2":{"super":{"$pk":"nesting","$nm":"X2"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"h"}],"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"d"}],"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"d2"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y2"},"SubRef51":{"super":{"$pk":"nesting","$nm":"SubRef5"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"subg55"}],"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef51"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"baz":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"baz"}},"$c":{"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"foobar":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"foobar"},"quxx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"quxx"}},"$nm":"C"}},"$at":{"qux":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"qux"}},"$nm":"B"}},"$at":{"foo":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"foo"}},"$nm":"A"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Unwrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$an":{"shared":[]},"$nm":"o"}},"$nm":"Unwrapper"},"SubRef2":{"super":{"$pk":"nesting","$nm":"RefineTest2"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef2"},"Wrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Wrapper"},"O":{"$i":{"InnerInterface":{"$mt":"ifc","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerInterface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"test1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test1"},"test2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test2"},"test3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test3"}},"$c":{"InnerClass":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerClass"}},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"s"}},"$nm":"O","$o":{"innerObject":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"innerObject"}}},"outr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"outr"},"testRefinement2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement2"},"Holder":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"o"}],"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"}},"$nm":"Holder"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C3":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"x"}},"$nm":"C3"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"},"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C3"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"},"Y":{"super":{"$pk":"nesting","$nm":"X"},"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y"},"X":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X"},"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"},"outerf":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$nm":"outerf"},"X2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"a"}],"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"c"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X2"},"OuterC1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"OuterC1"},"SubRef31":{"super":{"$pk":"nesting","$nm":"SubRef3"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef31"},"OuterC2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"}},"$nm":"OuterC2"},"testRefinement":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement"},"returner":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"o"}]],"$mt":"mthd","$nm":"returner"},"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"noop"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"noop"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"printName"}},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"gttr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Inner"}},"$at":{"inner":{"$t":{"$pk":"nesting","$nm":"Inner"},"$mt":"attr","$nm":"inner"},"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"attr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Outer"},"producer":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"mthd","$nm":"producer"},"RefineTest5":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"f"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"g"}],"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest5"},"RefineTest4":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"d"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"e"}],"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest4"},"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}},"RefineTest3":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest3"},"RefineTest2":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest2"},"SubRef5":{"super":{"$pk":"nesting","$nm":"RefineTest5"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef5"},"SubRef4":{"super":{"$pk":"nesting","$nm":"RefineTest4"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef4"},"SubRef3":{"super":{"$pk":"nesting","$nm":"RefineTest3"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef3"}}};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2244=require('check/0.1/check-0.1');

//ClassDefinition Outer at nesting.ceylon (3:0-28:0)
function Outer(name$2984, $$outer){
    $init$Outer();
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$2984=name$2984;
    
    //AttributeDeclaration int at nesting.ceylon (4:4-4:18)
    $$outer.int$2985=(10);
    
    //AttributeDeclaration float at nesting.ceylon (5:4-5:34)
    $$outer.float$2986=$$outer.getInt$2985().getFloat();
    
    //AttributeDeclaration inner at nesting.ceylon (21:4-21:25)
    $$outer.inner$2987=$$outer.Inner$2988();
    $$$cl2243.print($$outer.getInner$2987().getInt());
    $$$cl2243.print($$outer.getInner$2987().getFloat());
    $$outer.getInner$2987().noop();
    $$outer.noop$2989();
    return $$outer;
}
exports.Outer=Outer;
function $init$Outer(){
    if (Outer.$$===undefined){
        $$$cl2243.initTypeProto(Outer,'nesting::Outer',$$$cl2243.Basic);
        (function($$outer){
            
            //AttributeDeclaration int at nesting.ceylon (4:4-4:18)
            $$outer.getInt$2985=function getInt$2985(){
                return this.int$2985;
            };
            
            //AttributeDeclaration float at nesting.ceylon (5:4-5:34)
            $$outer.getFloat=function getFloat(){
                return this.float$2986;
            };
            
            //MethodDefinition noop at nesting.ceylon (6:4-6:17)
            $$outer.noop$2989=function noop$2989(){
                var $$outer=this;
            };
            //ClassDefinition Inner at nesting.ceylon (7:4-20:4)
            function Inner$2988($$inner$2988){
                $init$Inner$2988();
                if ($$inner$2988===undefined)$$inner$2988=new this.Inner$2988.$$;
                $$inner$2988.$$outer=this;
                return $$inner$2988;
            }
            function $init$Inner$2988(){
                if (Inner$2988.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$2988,'nesting::Outer.Inner',$$$cl2243.Basic);
                    (function($$inner$2988){
                        
                        //MethodDefinition printName at nesting.ceylon (8:8-10:8)
                        $$inner$2988.printName$2990=function printName$2990(){
                            var $$inner$2988=this;
                            $$$cl2243.print($$inner$2988.$$outer.name$2984);
                        };
                        //AttributeGetterDefinition int at nesting.ceylon (11:8-13:8)
                        $$inner$2988.getInt=function getInt(){
                            var $$inner$2988=this;
                            return $$inner$2988.$$outer.getInt$2985();
                        };
                        //AttributeGetterDefinition float at nesting.ceylon (14:8-16:8)
                        $$inner$2988.getFloat=function getFloat(){
                            var $$inner$2988=this;
                            return $$inner$2988.$$outer.getFloat();
                        };
                        //MethodDefinition noop at nesting.ceylon (17:8-19:8)
                        $$inner$2988.noop=function noop(){
                            var $$inner$2988=this;
                            $$inner$2988.$$outer.noop$2989();
                        };
                    })(Inner$2988.$$.prototype);
                }
                return Inner$2988;
            }
            $$outer.$init$Inner$2988=$init$Inner$2988;
            $init$Inner$2988();
            $$outer.Inner$2988=Inner$2988;
            
            //AttributeDeclaration inner at nesting.ceylon (21:4-21:25)
            $$outer.getInner$2987=function getInner$2987(){
                return this.inner$2987;
            };
        })(Outer.$$.prototype);
    }
    return Outer;
}
exports.$init$Outer=$init$Outer;
$init$Outer();

//MethodDefinition outr at nesting.ceylon (30:0-42:0)
function outr(name$2991){
    
    //AttributeDeclaration uname at nesting.ceylon (31:4-31:34)
    var uname$2992=name$2991.getUppercased();
    
    //MethodDefinition inr at nesting.ceylon (32:4-34:4)
    function inr$2993(){
        return name$2991;
    };
    
    //AttributeGetterDefinition uinr at nesting.ceylon (35:4-37:4)
    var getUinr$2994=function(){
        return uname$2992;
    };
    
    //AttributeDeclaration result at nesting.ceylon (38:4-38:25)
    var result$2995=inr$2993();
    
    //AttributeDeclaration uresult at nesting.ceylon (39:4-39:25)
    var uresult$2996=getUinr$2994();
    $$$cl2243.print(result$2995);
    $$$cl2243.print(uresult$2996);
}
exports.outr=outr;

//ClassDefinition Holder at nesting.ceylon (44:0-51:0)
function Holder(o$2997, $$holder){
    $init$Holder();
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$2997=o$2997;
    return $$holder;
}
exports.Holder=Holder;
function $init$Holder(){
    if (Holder.$$===undefined){
        $$$cl2243.initTypeProto(Holder,'nesting::Holder',$$$cl2243.Basic);
        (function($$holder){
            
            //MethodDefinition get at nesting.ceylon (45:4-47:4)
            $$holder.get=function get(){
                var $$holder=this;
                return $$holder.o$2997;
            };
            //AttributeGetterDefinition string at nesting.ceylon (48:4-50:4)
            $$holder.getString=function getString(){
                var $$holder=this;
                return $$holder.o$2997.getString();
            };
        })(Holder.$$.prototype);
    }
    return Holder;
}
exports.$init$Holder=$init$Holder;
$init$Holder();

//ClassDefinition Wrapper at nesting.ceylon (53:0-61:0)
function Wrapper($$wrapper){
    $init$Wrapper();
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (54:4-54:18)
    $$wrapper.o$2998=(100);
    return $$wrapper;
}
exports.Wrapper=Wrapper;
function $init$Wrapper(){
    if (Wrapper.$$===undefined){
        $$$cl2243.initTypeProto(Wrapper,'nesting::Wrapper',$$$cl2243.Basic);
        (function($$wrapper){
            
            //AttributeDeclaration o at nesting.ceylon (54:4-54:18)
            $$wrapper.getO$2998=function getO$2998(){
                return this.o$2998;
            };
            
            //MethodDefinition get at nesting.ceylon (55:4-57:4)
            $$wrapper.get=function get(){
                var $$wrapper=this;
                return $$wrapper.getO$2998();
            };
            //AttributeGetterDefinition string at nesting.ceylon (58:4-60:4)
            $$wrapper.getString=function getString(){
                var $$wrapper=this;
                return $$wrapper.getO$2998().getString();
            };
        })(Wrapper.$$.prototype);
    }
    return Wrapper;
}
exports.$init$Wrapper=$init$Wrapper;
$init$Wrapper();

//ClassDefinition Unwrapper at nesting.ceylon (63:0-71:0)
function Unwrapper($$unwrapper){
    $init$Unwrapper();
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (64:4-64:27)
    $$unwrapper.o$2999=$$$cl2243.Float(23.56);
    return $$unwrapper;
}
exports.Unwrapper=Unwrapper;
function $init$Unwrapper(){
    if (Unwrapper.$$===undefined){
        $$$cl2243.initTypeProto(Unwrapper,'nesting::Unwrapper',$$$cl2243.Basic);
        (function($$unwrapper){
            
            //AttributeDeclaration o at nesting.ceylon (64:4-64:27)
            $$unwrapper.getO=function getO(){
                return this.o$2999;
            };
            
            //MethodDefinition get at nesting.ceylon (65:4-67:4)
            $$unwrapper.get=function get(){
                var $$unwrapper=this;
                return $$unwrapper.getO();
            };
            //AttributeGetterDefinition string at nesting.ceylon (68:4-70:4)
            $$unwrapper.getString=function getString(){
                var $$unwrapper=this;
                return $$unwrapper.getO().getString();
            };
        })(Unwrapper.$$.prototype);
    }
    return Unwrapper;
}
exports.$init$Unwrapper=$init$Unwrapper;
$init$Unwrapper();

//MethodDefinition producer at nesting.ceylon (73:0-77:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (74:4-74:18)
    var o$3000=(123);
    
    //MethodDefinition produce at nesting.ceylon (75:4-75:35)
    function produce$3001(){
        return o$3000;
    };
    return produce$3001;
};

//MethodDefinition returner at nesting.ceylon (79:0-82:0)
function returner(o$3002){
    
    //MethodDefinition produce at nesting.ceylon (80:4-80:35)
    function produce$3003(){
        return o$3002;
    };
    return produce$3003;
};

//ClassDefinition A at nesting.ceylon (84:0-105:0)
function A($$a){
    $init$A();
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDeclaration foo at nesting.ceylon (85:4-85:22)
    $$a.foo$3004=$$$cl2243.String("foo",3);
    return $$a;
}
function $init$A(){
    if (A.$$===undefined){
        $$$cl2243.initTypeProto(A,'nesting::A',$$$cl2243.Basic);
        (function($$a){
            
            //AttributeDeclaration foo at nesting.ceylon (85:4-85:22)
            $$a.getFoo$3004=function getFoo$3004(){
                return this.foo$3004;
            };
            
            //ClassDefinition B at nesting.ceylon (86:4-96:4)
            function B$A($$b$A){
                $init$B$A();
                if ($$b$A===undefined)$$b$A=new this.B$A.$$;
                $$b$A.$$a=this;
                
                //AttributeDeclaration qux at nesting.ceylon (87:8-87:26)
                $$b$A.qux$3005=$$$cl2243.String("qux",3);
                return $$b$A;
            }
            function $init$B$A(){
                if (B$A.$$===undefined){
                    $$$cl2243.initTypeProto(B$A,'nesting::A.B',$$$cl2243.Basic);
                    (function($$b$A){
                        
                        //AttributeDeclaration qux at nesting.ceylon (87:8-87:26)
                        $$b$A.getQux$3005=function getQux$3005(){
                            return this.qux$3005;
                        };
                        
                        //ClassDefinition C at nesting.ceylon (88:8-95:8)
                        function C$B$A($$c$B$A){
                            $init$C$B$A();
                            if ($$c$B$A===undefined)$$c$B$A=new this.C$B$A.$$;
                            $$c$B$A.$$b$A=this;
                            return $$c$B$A;
                        }
                        function $init$C$B$A(){
                            if (C$B$A.$$===undefined){
                                $$$cl2243.initTypeProto(C$B$A,'nesting::A.B.C',$$$cl2243.Basic);
                                (function($$c$B$A){
                                    
                                    //MethodDefinition foobar at nesting.ceylon (89:12-91:12)
                                    $$c$B$A.foobar=function foobar(){
                                        var $$c$B$A=this;
                                        return $$c$B$A.$$b$A.$$a.getFoo$3004();
                                    };
                                    //MethodDefinition quxx at nesting.ceylon (92:12-94:12)
                                    $$c$B$A.quxx=function quxx(){
                                        var $$c$B$A=this;
                                        return $$c$B$A.$$b$A.getQux$3005();
                                    };
                                })(C$B$A.$$.prototype);
                            }
                            return C$B$A;
                        }
                        $$b$A.$init$C$B$A=$init$C$B$A;
                        $init$C$B$A();
                        $$b$A.C$B$A=C$B$A;
                    })(B$A.$$.prototype);
                }
                return B$A;
            }
            $$a.$init$B$A=$init$B$A;
            $init$B$A();
            $$a.B$A=B$A;
            
            //MethodDefinition baz at nesting.ceylon (97:4-104:4)
            $$a.baz=function baz(){
                var $$a=this;
                
                //ClassDefinition Baz at nesting.ceylon (98:8-102:8)
                function Baz$3006($$baz$3006){
                    $init$Baz$3006();
                    if ($$baz$3006===undefined)$$baz$3006=new Baz$3006.$$;
                    return $$baz$3006;
                }
                function $init$Baz$3006(){
                    if (Baz$3006.$$===undefined){
                        $$$cl2243.initTypeProto(Baz$3006,'nesting::A.baz.Baz',$$$cl2243.Basic);
                        (function($$baz$3006){
                            
                            //MethodDefinition get at nesting.ceylon (99:12-101:12)
                            $$baz$3006.get=function get(){
                                var $$baz$3006=this;
                                return $$a.getFoo$3004();
                            };
                        })(Baz$3006.$$.prototype);
                    }
                    return Baz$3006;
                }
                $init$Baz$3006();
                return Baz$3006().get();
            };
        })(A.$$.prototype);
    }
    return A;
}
exports.$init$A=$init$A;
$init$A();

//ClassDefinition O at nesting.ceylon (107:0-134:0)
function O($$o){
    $init$O();
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDeclaration s at nesting.ceylon (108:4-108:22)
    $$o.s$3007=$$$cl2243.String("hello",5);
    
    //ObjectDefinition innerObject at nesting.ceylon (114:4-118:4)
    $$o.innerObject$3008=$$o.innerObject$3009();
    return $$o;
}
function $init$O(){
    if (O.$$===undefined){
        $$$cl2243.initTypeProto(O,'nesting::O',$$$cl2243.Basic);
        (function($$o){
            
            //AttributeDeclaration s at nesting.ceylon (108:4-108:22)
            $$o.getS$3007=function getS$3007(){
                return this.s$3007;
            };
            
            //ClassDefinition InnerClass at nesting.ceylon (109:4-113:4)
            function InnerClass$3010($$innerClass$3010){
                $init$InnerClass$3010();
                if ($$innerClass$3010===undefined)$$innerClass$3010=new this.InnerClass$3010.$$;
                $$innerClass$3010.$$o=this;
                return $$innerClass$3010;
            }
            function $init$InnerClass$3010(){
                if (InnerClass$3010.$$===undefined){
                    $$$cl2243.initTypeProto(InnerClass$3010,'nesting::O.InnerClass',$$$cl2243.Basic);
                    (function($$innerClass$3010){
                        
                        //MethodDefinition f at nesting.ceylon (110:8-112:8)
                        $$innerClass$3010.f=function f(){
                            var $$innerClass$3010=this;
                            return $$innerClass$3010.$$o.getS$3007();
                        };
                    })(InnerClass$3010.$$.prototype);
                }
                return InnerClass$3010;
            }
            $$o.$init$InnerClass$3010=$init$InnerClass$3010;
            $init$InnerClass$3010();
            $$o.InnerClass$3010=InnerClass$3010;
            
            //ObjectDefinition innerObject at nesting.ceylon (114:4-118:4)
            function innerObject$3009(){
                var $$innerObject$3009=new this.innerObject$3009.$$;
                $$innerObject$3009.$$o=this;
                return $$innerObject$3009;
            }
            function $init$innerObject$3009(){
                if (innerObject$3009.$$===undefined){
                    $$$cl2243.initTypeProto(innerObject$3009,'nesting::O.innerObject',$$$cl2243.Basic);
                }
                return innerObject$3009;
            }
            $$o.$init$innerObject$3009=$init$innerObject$3009;
            $init$innerObject$3009();
            (function($$innerObject$3009){
                
                //MethodDefinition f at nesting.ceylon (115:8-117:8)
                $$innerObject$3009.f=function f(){
                    var $$innerObject$3009=this;
                    return $$innerObject$3009.$$o.getS$3007();
                };
            })(innerObject$3009.$$.prototype);
            var getInnerObject$3008=function(){
                return this.innerObject$3008;
            }
            $$o.getInnerObject$3008=getInnerObject$3008;
            $$o.innerObject$3009=innerObject$3009;
            
            //InterfaceDefinition InnerInterface at nesting.ceylon (119:4-123:4)
            function InnerInterface$3011($$innerInterface$3011){
                $$innerInterface$3011.$$o=this;
            }
            function $init$InnerInterface$3011(){
                if (InnerInterface$3011.$$===undefined){
                    $$$cl2243.initTypeProto(InnerInterface$3011,'nesting::O.InnerInterface');
                    (function($$innerInterface$3011){
                        
                        //MethodDefinition f at nesting.ceylon (120:8-122:8)
                        $$innerInterface$3011.f=function f(){
                            var $$innerInterface$3011=this;
                            return $$innerInterface$3011.$$o.getS$3007();
                        };
                    })(InnerInterface$3011.$$.prototype);
                }
                return InnerInterface$3011;
            }
            $$o.$init$InnerInterface$3011=$init$InnerInterface$3011;
            $init$InnerInterface$3011();
            $$o.InnerInterface$3011=InnerInterface$3011;
            
            //MethodDefinition test1 at nesting.ceylon (124:4-126:4)
            $$o.test1=function test1(){
                var $$o=this;
                return $$o.InnerClass$3010().f();
            };
            //MethodDefinition test2 at nesting.ceylon (127:4-129:4)
            $$o.test2=function test2(){
                var $$o=this;
                return $$o.getInnerObject$3008().f();
            };
            //MethodDefinition test3 at nesting.ceylon (130:4-133:4)
            $$o.test3=function test3(){
                var $$o=this;
                
                //ObjectDefinition obj at nesting.ceylon (131:8-131:45)
                function obj$3012(){
                    var $$obj$3012=new obj$3012.$$;
                    $$o.InnerInterface$3011($$obj$3012);
                    return $$obj$3012;
                }
                function $init$obj$3012(){
                    if (obj$3012.$$===undefined){
                        $$$cl2243.initTypeProto(obj$3012,'nesting::O.test3.obj',$$$cl2243.Basic,$$o.$init$InnerInterface$3011());
                    }
                    return obj$3012;
                }
                $init$obj$3012();
                var obj$3013=obj$3012();
                var getObj$3013=function(){
                    return obj$3013;
                }
                return getObj$3013().f();
            };
        })(O.$$.prototype);
    }
    return O;
}
exports.$init$O=$init$O;
$init$O();

//ClassDefinition OuterC1 at nesting.ceylon (136:0-142:0)
function OuterC1($$outerC1){
    $init$OuterC1();
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    return $$outerC1;
}
function $init$OuterC1(){
    if (OuterC1.$$===undefined){
        $$$cl2243.initTypeProto(OuterC1,'nesting::OuterC1',$$$cl2243.Basic);
        (function($$outerC1){
            
            //ClassDefinition A at nesting.ceylon (137:4-139:4)
            function A$3014($$a$3014){
                $init$A$3014();
                if ($$a$3014===undefined)$$a$3014=new this.A$3014.$$;
                $$a$3014.$$outerC1=this;
                return $$a$3014;
            }
            function $init$A$3014(){
                if (A$3014.$$===undefined){
                    $$$cl2243.initTypeProto(A$3014,'nesting::OuterC1.A',$$$cl2243.Basic);
                    (function($$a$3014){
                        
                        //MethodDefinition tst at nesting.ceylon (138:8-138:55)
                        $$a$3014.tst=function tst(){
                            var $$a$3014=this;
                            return $$$cl2243.String("OuterC1.A.tst()",15);
                        };
                    })(A$3014.$$.prototype);
                }
                return A$3014;
            }
            $$outerC1.$init$A$3014=$init$A$3014;
            $init$A$3014();
            $$outerC1.A$3014=A$3014;
            
            //ClassDefinition B at nesting.ceylon (140:4-140:27)
            function B$3015($$b$3015){
                $init$B$3015();
                if ($$b$3015===undefined)$$b$3015=new this.B$3015.$$;
                $$b$3015.$$outerC1=this;
                $$b$3015.$$outerC1.A$3014($$b$3015);
                return $$b$3015;
            }
            function $init$B$3015(){
                if (B$3015.$$===undefined){
                    $$$cl2243.initTypeProto(B$3015,'nesting::OuterC1.B',$$outerC1.A$3014);
                }
                return B$3015;
            }
            $$outerC1.$init$B$3015=$init$B$3015;
            $init$B$3015();
            $$outerC1.B$3015=B$3015;
            
            //MethodDefinition tst at nesting.ceylon (141:4-141:42)
            $$outerC1.tst=function tst(){
                var $$outerC1=this;
                return $$outerC1.B$3015().tst();
            };
        })(OuterC1.$$.prototype);
    }
    return OuterC1;
}
exports.$init$OuterC1=$init$OuterC1;
$init$OuterC1();

//MethodDefinition outerf at nesting.ceylon (144:0-150:0)
function outerf(){
    
    //ClassDefinition A at nesting.ceylon (145:4-147:4)
    function A$3016($$a$3016){
        $init$A$3016();
        if ($$a$3016===undefined)$$a$3016=new A$3016.$$;
        return $$a$3016;
    }
    function $init$A$3016(){
        if (A$3016.$$===undefined){
            $$$cl2243.initTypeProto(A$3016,'nesting::outerf.A',$$$cl2243.Basic);
            (function($$a$3016){
                
                //MethodDefinition tst at nesting.ceylon (146:8-146:54)
                $$a$3016.tst=function tst(){
                    var $$a$3016=this;
                    return $$$cl2243.String("outerf.A.tst()",14);
                };
            })(A$3016.$$.prototype);
        }
        return A$3016;
    }
    $init$A$3016();
    
    //ClassDefinition B at nesting.ceylon (148:4-148:27)
    function B$3017($$b$3017){
        $init$B$3017();
        if ($$b$3017===undefined)$$b$3017=new B$3017.$$;
        A$3016($$b$3017);
        return $$b$3017;
    }
    function $init$B$3017(){
        if (B$3017.$$===undefined){
            $$$cl2243.initTypeProto(B$3017,'nesting::outerf.B',A$3016);
        }
        return B$3017;
    }
    $init$B$3017();
    return B$3017().tst();
};

//ClassDefinition OuterC2 at nesting.ceylon (152:0-160:0)
function OuterC2($$outerC2){
    $init$OuterC2();
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    return $$outerC2;
}
function $init$OuterC2(){
    if (OuterC2.$$===undefined){
        $$$cl2243.initTypeProto(OuterC2,'nesting::OuterC2',$$$cl2243.Basic);
        (function($$outerC2){
            
            //ClassDefinition A at nesting.ceylon (153:4-155:4)
            function A$3018($$a$3018){
                $init$A$3018();
                if ($$a$3018===undefined)$$a$3018=new this.A$3018.$$;
                $$a$3018.$$outerC2=this;
                return $$a$3018;
            }
            function $init$A$3018(){
                if (A$3018.$$===undefined){
                    $$$cl2243.initTypeProto(A$3018,'nesting::OuterC2.A',$$$cl2243.Basic);
                    (function($$a$3018){
                        
                        //MethodDefinition tst at nesting.ceylon (154:8-154:55)
                        $$a$3018.tst=function tst(){
                            var $$a$3018=this;
                            return $$$cl2243.String("OuterC2.A.tst()",15);
                        };
                    })(A$3018.$$.prototype);
                }
                return A$3018;
            }
            $$outerC2.$init$A$3018=$init$A$3018;
            $init$A$3018();
            $$outerC2.A$3018=A$3018;
            
            //MethodDefinition tst at nesting.ceylon (156:4-159:4)
            $$outerC2.tst=function tst(){
                var $$outerC2=this;
                
                //ClassDefinition B at nesting.ceylon (157:8-157:31)
                function B$3019($$b$3019){
                    $init$B$3019();
                    if ($$b$3019===undefined)$$b$3019=new B$3019.$$;
                    $$outerC2.A$3018($$b$3019);
                    return $$b$3019;
                }
                function $init$B$3019(){
                    if (B$3019.$$===undefined){
                        $$$cl2243.initTypeProto(B$3019,'nesting::OuterC2.tst.B',$$outerC2.A$3018);
                    }
                    return B$3019;
                }
                $init$B$3019();
                return B$3019().tst();
            };
        })(OuterC2.$$.prototype);
    }
    return OuterC2;
}
exports.$init$OuterC2=$init$OuterC2;
$init$OuterC2();

//ClassDefinition NameTest at nesting.ceylon (162:0-178:0)
function NameTest($$nameTest){
    $init$NameTest();
    if ($$nameTest===undefined)$$nameTest=new NameTest.$$;
    
    //AttributeDeclaration x at nesting.ceylon (163:4-163:25)
    $$nameTest.x$3020=$$$cl2243.String("1",1);
    return $$nameTest;
}
exports.NameTest=NameTest;
function $init$NameTest(){
    if (NameTest.$$===undefined){
        $$$cl2243.initTypeProto(NameTest,'nesting::NameTest',$$$cl2243.Basic);
        (function($$nameTest){
            
            //AttributeDeclaration x at nesting.ceylon (163:4-163:25)
            $$nameTest.getX=function getX(){
                return this.x$3020;
            };
            
            //ClassDefinition NameTest at nesting.ceylon (164:4-176:4)
            function NameTest$NameTest($$nameTest$NameTest){
                $init$NameTest$NameTest();
                if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new this.NameTest$NameTest.$$;
                $$nameTest$NameTest.$$nameTest=this;
                
                //AttributeDeclaration x at nesting.ceylon (165:8-165:29)
                $$nameTest$NameTest.x$3021=$$$cl2243.String("2",1);
                return $$nameTest$NameTest;
            }
            function $init$NameTest$NameTest(){
                if (NameTest$NameTest.$$===undefined){
                    $$$cl2243.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest',$$$cl2243.Basic);
                    (function($$nameTest$NameTest){
                        
                        //AttributeDeclaration x at nesting.ceylon (165:8-165:29)
                        $$nameTest$NameTest.getX=function getX(){
                            return this.x$3021;
                        };
                        
                        //MethodDefinition f at nesting.ceylon (166:8-175:8)
                        $$nameTest$NameTest.f=function f(){
                            var $$nameTest$NameTest=this;
                            
                            //ClassDefinition NameTest at nesting.ceylon (167:12-173:12)
                            function NameTest$3022($$nameTest$3022){
                                $init$NameTest$3022();
                                if ($$nameTest$3022===undefined)$$nameTest$3022=new NameTest$3022.$$;
                                
                                //AttributeDeclaration x at nesting.ceylon (168:16-168:37)
                                $$nameTest$3022.x$3023=$$$cl2243.String("3",1);
                                return $$nameTest$3022;
                            }
                            function $init$NameTest$3022(){
                                if (NameTest$3022.$$===undefined){
                                    $$$cl2243.initTypeProto(NameTest$3022,'nesting::NameTest.NameTest.f.NameTest',$$$cl2243.Basic);
                                    (function($$nameTest$3022){
                                        
                                        //AttributeDeclaration x at nesting.ceylon (168:16-168:37)
                                        $$nameTest$3022.getX=function getX(){
                                            return this.x$3023;
                                        };
                                        
                                        //ClassDefinition NameTest at nesting.ceylon (169:16-171:16)
                                        function NameTest$NameTest($$nameTest$NameTest){
                                            $init$NameTest$NameTest();
                                            if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new this.NameTest$NameTest.$$;
                                            $$nameTest$NameTest.$$nameTest$3022=this;
                                            
                                            //AttributeDeclaration x at nesting.ceylon (170:20-170:41)
                                            $$nameTest$NameTest.x$3024=$$$cl2243.String("4",1);
                                            return $$nameTest$NameTest;
                                        }
                                        function $init$NameTest$NameTest(){
                                            if (NameTest$NameTest.$$===undefined){
                                                $$$cl2243.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest.f.NameTest.NameTest',$$$cl2243.Basic);
                                                (function($$nameTest$NameTest){
                                                    
                                                    //AttributeDeclaration x at nesting.ceylon (170:20-170:41)
                                                    $$nameTest$NameTest.getX=function getX(){
                                                        return this.x$3024;
                                                    };
                                                })(NameTest$NameTest.$$.prototype);
                                            }
                                            return NameTest$NameTest;
                                        }
                                        $$nameTest$3022.$init$NameTest$NameTest=$init$NameTest$NameTest;
                                        $init$NameTest$NameTest();
                                        $$nameTest$3022.NameTest$NameTest=NameTest$NameTest;
                                        
                                        //MethodDefinition f at nesting.ceylon (172:16-172:66)
                                        $$nameTest$3022.f=function f(){
                                            var $$nameTest$3022=this;
                                            return $$nameTest$3022.getX().plus($$nameTest$3022.NameTest$NameTest().getX());
                                        };
                                    })(NameTest$3022.$$.prototype);
                                }
                                return NameTest$3022;
                            }
                            $init$NameTest$3022();
                            return $$nameTest$NameTest.$$nameTest.getX().plus($$nameTest$NameTest.getX()).plus(NameTest$3022().f());
                        };
                    })(NameTest$NameTest.$$.prototype);
                }
                return NameTest$NameTest;
            }
            $$nameTest.$init$NameTest$NameTest=$init$NameTest$NameTest;
            $init$NameTest$NameTest();
            $$nameTest.NameTest$NameTest=NameTest$NameTest;
            
            //MethodDefinition f at nesting.ceylon (177:4-177:52)
            $$nameTest.f=function f(){
                var $$nameTest=this;
                return $$nameTest.NameTest$NameTest().f();
            };
        })(NameTest.$$.prototype);
    }
    return NameTest;
}
exports.$init$NameTest=$init$NameTest;
$init$NameTest();

//ObjectDefinition nameTest at nesting.ceylon (180:0-196:0)
function nameTest(){
    var $$nameTest=new nameTest.$$;
    
    //AttributeDeclaration x at nesting.ceylon (181:4-181:25)
    $$nameTest.x$3025=$$$cl2243.String("1",1);
    
    //ObjectDefinition nameTest at nesting.ceylon (182:4-194:4)
    $$nameTest.nameTest$3026=$$nameTest.nameTest$nameTest();
    return $$nameTest;
}
function $init$nameTest(){
    if (nameTest.$$===undefined){
        $$$cl2243.initTypeProto(nameTest,'nesting::nameTest',$$$cl2243.Basic);
    }
    return nameTest;
}
exports.$init$nameTest=$init$nameTest;
$init$nameTest();
(function($$nameTest){
    
    //AttributeDeclaration x at nesting.ceylon (181:4-181:25)
    $$nameTest.getX=function getX(){
        return this.x$3025;
    };
    
    //ObjectDefinition nameTest at nesting.ceylon (182:4-194:4)
    function nameTest$nameTest(){
        var $$nameTest$nameTest=new this.nameTest$nameTest.$$;
        $$nameTest$nameTest.$$nameTest=this;
        
        //AttributeDeclaration x at nesting.ceylon (183:8-183:29)
        $$nameTest$nameTest.x$3027=$$$cl2243.String("2",1);
        return $$nameTest$nameTest;
    }
    function $init$nameTest$nameTest(){
        if (nameTest$nameTest.$$===undefined){
            $$$cl2243.initTypeProto(nameTest$nameTest,'nesting::nameTest.nameTest',$$$cl2243.Basic);
        }
        return nameTest$nameTest;
    }
    $$nameTest.$init$nameTest$nameTest=$init$nameTest$nameTest;
    $init$nameTest$nameTest();
    (function($$nameTest$nameTest){
        
        //AttributeDeclaration x at nesting.ceylon (183:8-183:29)
        $$nameTest$nameTest.getX=function getX(){
            return this.x$3027;
        };
        
        //MethodDefinition f at nesting.ceylon (184:8-193:8)
        $$nameTest$nameTest.f=function f(){
            var $$nameTest$nameTest=this;
            
            //ObjectDefinition nameTest at nesting.ceylon (185:12-191:12)
            function nameTest$3028(){
                var $$nameTest$3028=new nameTest$3028.$$;
                
                //AttributeDeclaration x at nesting.ceylon (186:16-186:37)
                $$nameTest$3028.x$3029=$$$cl2243.String("3",1);
                
                //ObjectDefinition nameTest at nesting.ceylon (187:16-189:16)
                $$nameTest$3028.nameTest$3030=$$nameTest$3028.nameTest$nameTest();
                return $$nameTest$3028;
            }
            function $init$nameTest$3028(){
                if (nameTest$3028.$$===undefined){
                    $$$cl2243.initTypeProto(nameTest$3028,'nesting::nameTest.nameTest.f.nameTest',$$$cl2243.Basic);
                }
                return nameTest$3028;
            }
            $init$nameTest$3028();
            (function($$nameTest$3028){
                
                //AttributeDeclaration x at nesting.ceylon (186:16-186:37)
                $$nameTest$3028.getX=function getX(){
                    return this.x$3029;
                };
                
                //ObjectDefinition nameTest at nesting.ceylon (187:16-189:16)
                function nameTest$nameTest(){
                    var $$nameTest$nameTest=new this.nameTest$nameTest.$$;
                    $$nameTest$nameTest.$$nameTest$3028=this;
                    
                    //AttributeDeclaration x at nesting.ceylon (188:20-188:41)
                    $$nameTest$nameTest.x$3031=$$$cl2243.String("4",1);
                    return $$nameTest$nameTest;
                }
                function $init$nameTest$nameTest(){
                    if (nameTest$nameTest.$$===undefined){
                        $$$cl2243.initTypeProto(nameTest$nameTest,'nesting::nameTest.nameTest.f.nameTest.nameTest',$$$cl2243.Basic);
                    }
                    return nameTest$nameTest;
                }
                $$nameTest$3028.$init$nameTest$nameTest=$init$nameTest$nameTest;
                $init$nameTest$nameTest();
                (function($$nameTest$nameTest){
                    
                    //AttributeDeclaration x at nesting.ceylon (188:20-188:41)
                    $$nameTest$nameTest.getX=function getX(){
                        return this.x$3031;
                    };
                })(nameTest$nameTest.$$.prototype);
                var getNameTest=function(){
                    return this.nameTest$3030;
                }
                $$nameTest$3028.getNameTest=getNameTest;
                $$nameTest$3028.nameTest$nameTest=nameTest$nameTest;
                
                //MethodDefinition f at nesting.ceylon (190:16-190:64)
                $$nameTest$3028.f=function f(){
                    var $$nameTest$3028=this;
                    return $$nameTest$3028.getX().plus($$nameTest$3028.getNameTest().getX());
                };
            })(nameTest$3028.$$.prototype);
            var nameTest$3032=nameTest$3028();
            var getNameTest$3032=function(){
                return nameTest$3032;
            }
            return $$nameTest$nameTest.$$nameTest.getX().plus($$nameTest$nameTest.getX()).plus(getNameTest$3032().f());
        };
    })(nameTest$nameTest.$$.prototype);
    var getNameTest=function(){
        return this.nameTest$3026;
    }
    $$nameTest.getNameTest=getNameTest;
    $$nameTest.nameTest$nameTest=nameTest$nameTest;
    
    //MethodDefinition f at nesting.ceylon (195:4-195:50)
    $$nameTest.f=function f(){
        var $$nameTest=this;
        return $$nameTest.getNameTest().f();
    };
})(nameTest.$$.prototype);
var nameTest$3033=nameTest();
var getNameTest=function(){
    return nameTest$3033;
}
exports.getNameTest=getNameTest;

//ClassDefinition C1 at nesting.ceylon (198:0-209:0)
function C1($$c1){
    $init$C1();
    if ($$c1===undefined)$$c1=new C1.$$;
    
    //AttributeDeclaration x at nesting.ceylon (199:4-199:33)
    $$c1.x$3034=$$$cl2243.String("1",1);
    return $$c1;
}
exports.C1=C1;
function $init$C1(){
    if (C1.$$===undefined){
        $$$cl2243.initTypeProto(C1,'nesting::C1',$$$cl2243.Basic);
        (function($$c1){
            
            //AttributeDeclaration x at nesting.ceylon (199:4-199:33)
            $$c1.getX=function getX(){
                return this.x$3034;
            };
            
            //ClassDefinition C1 at nesting.ceylon (200:4-202:4)
            function C1$C1($$c1$C1){
                $init$C1$C1();
                if ($$c1$C1===undefined)$$c1$C1=new this.C1$C1.$$;
                $$c1$C1.$$c1=this;
                
                //AttributeDeclaration x at nesting.ceylon (201:8-201:38)
                $$c1$C1.x$3035=$$$cl2243.String("11",2);
                return $$c1$C1;
            }
            function $init$C1$C1(){
                if (C1$C1.$$===undefined){
                    $$$cl2243.initTypeProto(C1$C1,'nesting::C1.C1',$$$cl2243.Basic);
                    (function($$c1$C1){
                        
                        //AttributeDeclaration x at nesting.ceylon (201:8-201:38)
                        $$c1$C1.getX=function getX(){
                            return this.x$3035;
                        };
                    })(C1$C1.$$.prototype);
                }
                return C1$C1;
            }
            $$c1.$init$C1$C1=$init$C1$C1;
            $init$C1$C1();
            $$c1.C1$C1=C1$C1;
            
            //ClassDefinition C3 at nesting.ceylon (203:4-208:4)
            function C3$C1($$c3$C1){
                $init$C3$C1();
                if ($$c3$C1===undefined)$$c3$C1=new this.C3$C1.$$;
                $$c3$C1.$$c1=this;
                $$c3$C1.$$c1.C1$C1($$c3$C1);
                
                //AttributeDeclaration x at nesting.ceylon (204:8-204:45)
                $$c3$C1.x$3036=$$$cl2243.String("13",2);
                return $$c3$C1;
            }
            function $init$C3$C1(){
                if (C3$C1.$$===undefined){
                    $$$cl2243.initTypeProto(C3$C1,'nesting::C1.C3',$$c1.C1$C1);
                    (function($$c3$C1){
                        
                        //AttributeDeclaration x at nesting.ceylon (204:8-204:45)
                        $$c3$C1.getX=function getX(){
                            return this.x$3036;
                        };
                        
                        //MethodDefinition f at nesting.ceylon (205:8-207:8)
                        $$c3$C1.f=function f(){
                            var $$c3$C1=this;
                            return $$$cl2243.StringBuilder().appendAll([$$c3$C1.$$c1.getX().getString(),$$$cl2243.String("-",1),$$c3$C1.getT$all()['nesting::C1.C1'].$$.prototype.getX.call(this).getString(),$$$cl2243.String("-",1),$$c3$C1.$$c1.C1$C1().getX().getString(),$$$cl2243.String("-",1),$$c3$C1.getX().getString(),$$$cl2243.String("-",1),$$c3$C1.$$c1.C3$C1().getX().getString()]).getString();
                        };
                    })(C3$C1.$$.prototype);
                }
                return C3$C1;
            }
            $$c1.$init$C3$C1=$init$C3$C1;
            $init$C3$C1();
            $$c1.C3$C1=C3$C1;
        })(C1.$$.prototype);
    }
    return C1;
}
exports.$init$C1=$init$C1;
$init$C1();

//ClassDefinition C2 at nesting.ceylon (210:0-221:0)
function C2($$c2){
    $init$C2();
    if ($$c2===undefined)$$c2=new C2.$$;
    C1($$c2);
    
    //AttributeDeclaration x at nesting.ceylon (211:4-211:32)
    $$c2.x$3037=$$$cl2243.String("2",1);
    return $$c2;
}
exports.C2=C2;
function $init$C2(){
    if (C2.$$===undefined){
        $$$cl2243.initTypeProto(C2,'nesting::C2',C1);
        (function($$c2){
            
            //AttributeDeclaration x at nesting.ceylon (211:4-211:32)
            $$c2.getX=function getX(){
                return this.x$3037;
            };
            
            //ClassDefinition C2 at nesting.ceylon (212:4-220:4)
            function C2$C2($$c2$C2){
                $init$C2$C2();
                if ($$c2$C2===undefined)$$c2$C2=new this.C2$C2.$$;
                $$c2$C2.$$c2=this;
                $$c2$C2.$$c2.getT$all()['nesting::C1'].$$.prototype.C1$C1.call(this,$$c2$C2);
                
                //AttributeDeclaration x at nesting.ceylon (213:8-213:37)
                $$c2$C2.x$3038=$$$cl2243.String("22",2);
                return $$c2$C2;
            }
            function $init$C2$C2(){
                if (C2$C2.$$===undefined){
                    $$$cl2243.initTypeProto(C2$C2,'nesting::C2.C2',$$c2.getT$all()['nesting::C1'].$$.prototype.C1$C1);
                    (function($$c2$C2){
                        
                        //AttributeDeclaration x at nesting.ceylon (213:8-213:37)
                        $$c2$C2.getX=function getX(){
                            return this.x$3038;
                        };
                        
                        //ClassDefinition C2 at nesting.ceylon (214:8-216:8)
                        function C2$C2$C2($$c2$C2$C2){
                            $init$C2$C2$C2();
                            if ($$c2$C2$C2===undefined)$$c2$C2$C2=new this.C2$C2$C2.$$;
                            $$c2$C2$C2.$$c2$C2=this;
                            $$c2$C2$C2.$$c2$C2.$$c2.C3$C1($$c2$C2$C2);
                            
                            //AttributeDeclaration x at nesting.ceylon (215:12-215:42)
                            $$c2$C2$C2.x$3039=$$$cl2243.String("222",3);
                            return $$c2$C2$C2;
                        }
                        function $init$C2$C2$C2(){
                            if (C2$C2$C2.$$===undefined){
                                $$$cl2243.initTypeProto(C2$C2$C2,'nesting::C2.C2.C2',$$c2.C3$C1);
                                (function($$c2$C2$C2){
                                    
                                    //AttributeDeclaration x at nesting.ceylon (215:12-215:42)
                                    $$c2$C2$C2.getX=function getX(){
                                        return this.x$3039;
                                    };
                                })(C2$C2$C2.$$.prototype);
                            }
                            return C2$C2$C2;
                        }
                        $$c2$C2.$init$C2$C2$C2=$init$C2$C2$C2;
                        $init$C2$C2$C2();
                        $$c2$C2.C2$C2$C2=C2$C2$C2;
                        
                        //MethodDefinition f at nesting.ceylon (217:8-219:8)
                        $$c2$C2.f=function f(){
                            var $$c2$C2=this;
                            return $$$cl2243.StringBuilder().appendAll([$$c2$C2.$$c2.getX().getString(),$$$cl2243.String("-",1),$$c2$C2.$$c2.C1$C1().getX().getString(),$$$cl2243.String("-",1),$$c2$C2.getX().getString(),$$$cl2243.String("-",1),$$c2$C2.getT$all()['nesting::C1.C1'].$$.prototype.getX.call(this).getString(),$$$cl2243.String("-",1),$$c2$C2.$$c2.C3$C1().getX().getString(),$$$cl2243.String("-",1),$$c2$C2.C2$C2$C2().getX().getString(),$$$cl2243.String("-",1),$$c2$C2.C2$C2$C2().f().getString(),$$$cl2243.String("-",1),$$c2$C2.$$c2.C3$C1().f().getString()]).getString();
                        };
                    })(C2$C2.$$.prototype);
                }
                return C2$C2;
            }
            $$c2.$init$C2$C2=$init$C2$C2;
            $init$C2$C2();
            $$c2.C2$C2=C2$C2;
        })(C2.$$.prototype);
    }
    return C2;
}
exports.$init$C2=$init$C2;
$init$C2();

//MethodDefinition test at nesting.ceylon (223:0-253:0)
function test(){
    outr($$$cl2243.String("Hello",5));
    $$$c2244.check(Holder($$$cl2243.String("ok",2)).get().getString().equals($$$cl2243.String("ok",2)),$$$cl2243.String("holder(ok)",10));
    $$$c2244.check(Holder($$$cl2243.String("ok",2)).getString().equals($$$cl2243.String("ok",2)),$$$cl2243.String("holder.string",13));
    $$$c2244.check(Wrapper().get().getString().equals($$$cl2243.String("100",3)),$$$cl2243.String("wrapper 1",9));
    $$$c2244.check(Wrapper().getString().equals($$$cl2243.String("100",3)),$$$cl2243.String("wrapper 2",9));
    $$$c2244.check(Unwrapper().get().getString().equals($$$cl2243.String("23.56",5)),$$$cl2243.String("unwrapper 1",11));
    $$$c2244.check(Unwrapper().getO().getString().equals($$$cl2243.String("23.56",5)),$$$cl2243.String("unwrapper 2",11));
    $$$c2244.check(Unwrapper().getString().equals($$$cl2243.String("23.56",5)),$$$cl2243.String("unwrapper 3",11));
    $$$c2244.check($$$cl2243.isOfType(producer(),{t:$$$cl2243.Callable,a:{Arguments:{t:$$$cl2243.Empty},Return:{t:$$$cl2243.Integer}}}),$$$cl2243.String("function 1",10));
    $$$c2244.check($$$cl2243.isOfType(producer()(),{t:$$$cl2243.Integer}),$$$cl2243.String("function 2",10));
    $$$c2244.check((123).equals(producer()()),$$$cl2243.String("function 3",10));
    $$$c2244.check($$$cl2243.String("something",9).equals(returner($$$cl2243.String("something",9))()),$$$cl2243.String("function 4",10));
    $$$c2244.check(A().B$A().C$B$A().foobar().equals($$$cl2243.String("foo",3)),$$$cl2243.String("foobar",6));
    $$$c2244.check(A().B$A().C$B$A().quxx().equals($$$cl2243.String("qux",3)),$$$cl2243.String("quxx",4));
    $$$c2244.check(A().baz().equals($$$cl2243.String("foo",3)),$$$cl2243.String("baz",3));
    $$$c2244.check(O().test1().equals($$$cl2243.String("hello",5)),$$$cl2243.String("method instantiating inner class",32));
    $$$c2244.check(O().test2().equals($$$cl2243.String("hello",5)),$$$cl2243.String("method accessing inner object",29));
    $$$c2244.check(O().test3().equals($$$cl2243.String("hello",5)),$$$cl2243.String("method deriving inner interface",31));
    $$$c2244.check(OuterC1().tst().equals($$$cl2243.String("OuterC1.A.tst()",15)),$$$cl2243.String("",0));
    $$$c2244.check(outerf().equals($$$cl2243.String("outerf.A.tst()",14)),$$$cl2243.String("",0));
    $$$c2244.check(OuterC2().tst().equals($$$cl2243.String("OuterC2.A.tst()",15)),$$$cl2243.String("",0));
    Outer($$$cl2243.String("Hello",5));
    $$$c2244.check(NameTest().f().equals($$$cl2243.String("1234",4)),$$$cl2243.String("Nested class with same name",27));
    $$$c2244.check(getNameTest().f().equals($$$cl2243.String("1234",4)),$$$cl2243.String("Nested object with same name",28));
    $$$c2244.check(C1().C3$C1().f().equals($$$cl2243.String("1-11-11-13-13",13)),$$$cl2243.String("Several nested classes with same name (1)",41));
    $$$c2244.check(C2().C2$C2().f().equals($$$cl2243.String("2-11-22-11-13-222-2-11-11-222-13-2-11-11-13-13",46)),$$$cl2243.String("Several nested classes with same name (2)",41));
    testRefinement();
    testRefinement2();
    $$$c2244.results();
}
exports.test=test;

//ClassDefinition X at refinement.ceylon (4:0-17:0)
function X($$x){
    $init$X();
    if ($$x===undefined)$$x=new X.$$;
    return $$x;
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2243.initTypeProto(X,'nesting::X',$$$cl2243.Basic);
        (function($$x){
            
            //ClassDefinition RefineTest1 at refinement.ceylon (5:4-16:4)
            function RefineTest1$X($$refineTest1$X){
                $init$RefineTest1$X();
                if ($$refineTest1$X===undefined)$$refineTest1$X=new this.RefineTest1$X.$$;
                $$refineTest1$X.$$x=this;
                return $$refineTest1$X;
            }
            function $init$RefineTest1$X(){
                if (RefineTest1$X.$$===undefined){
                    $$$cl2243.initTypeProto(RefineTest1$X,'nesting::X.RefineTest1',$$$cl2243.Basic);
                    (function($$refineTest1$X){
                        
                        //ClassDefinition Inner at refinement.ceylon (6:8-12:8)
                        function Inner$RefineTest1$X($$inner$RefineTest1$X){
                            $init$Inner$RefineTest1$X();
                            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new this.Inner$RefineTest1$X.$$;
                            $$inner$RefineTest1$X.$$refineTest1$X=this;
                            
                            //AttributeDeclaration origin at refinement.ceylon (7:12-7:54)
                            $$inner$RefineTest1$X.origin$3040=$$$cl2243.String("RefineTest1.Inner",17);
                            return $$inner$RefineTest1$X;
                        }
                        function $init$Inner$RefineTest1$X(){
                            if (Inner$RefineTest1$X.$$===undefined){
                                $$$cl2243.initTypeProto(Inner$RefineTest1$X,'nesting::X.RefineTest1.Inner',$$$cl2243.Basic);
                                (function($$inner$RefineTest1$X){
                                    
                                    //AttributeDeclaration origin at refinement.ceylon (7:12-7:54)
                                    $$inner$RefineTest1$X.getOrigin=function getOrigin(){
                                        return this.origin$3040;
                                    };
                                    
                                    //MethodDefinition x at refinement.ceylon (8:12-10:12)
                                    $$inner$RefineTest1$X.x=function x(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl2243.String("x and ",6).plus($$inner$RefineTest1$X.y());
                                    };
                                })(Inner$RefineTest1$X.$$.prototype);
                            }
                            return Inner$RefineTest1$X;
                        }
                        $$refineTest1$X.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
                        $init$Inner$RefineTest1$X();
                        $$refineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
                        
                        //MethodDefinition outerx at refinement.ceylon (13:8-15:8)
                        $$refineTest1$X.outerx=function outerx(){
                            var $$refineTest1$X=this;
                            return $$refineTest1$X.Inner$RefineTest1$X().x();
                        };
                    })(RefineTest1$X.$$.prototype);
                }
                return RefineTest1$X;
            }
            $$x.$init$RefineTest1$X=$init$RefineTest1$X;
            $init$RefineTest1$X();
            $$x.RefineTest1$X=RefineTest1$X;
        })(X.$$.prototype);
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition RefineTest2 at refinement.ceylon (20:0-24:0)
function RefineTest2($$refineTest2){
    $init$RefineTest2();
    if ($$refineTest2===undefined)$$refineTest2=new RefineTest2.$$;
    return $$refineTest2;
}
exports.RefineTest2=RefineTest2;
function $init$RefineTest2(){
    if (RefineTest2.$$===undefined){
        $$$cl2243.initTypeProto(RefineTest2,'nesting::RefineTest2',$$$cl2243.Basic);
        (function($$refineTest2){
            
            //ClassDefinition Inner at refinement.ceylon (21:4-23:4)
            function Inner$RefineTest2($$inner$RefineTest2){
                $init$Inner$RefineTest2();
                if ($$inner$RefineTest2===undefined)$$inner$RefineTest2=new this.Inner$RefineTest2.$$;
                $$inner$RefineTest2.$$refineTest2=this;
                return $$inner$RefineTest2;
            }
            function $init$Inner$RefineTest2(){
                if (Inner$RefineTest2.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$RefineTest2,'nesting::RefineTest2.Inner',$$$cl2243.Basic);
                    (function($$inner$RefineTest2){
                        
                        //MethodDefinition hello at refinement.ceylon (22:8-22:71)
                        $$inner$RefineTest2.hello=function hello(){
                            var $$inner$RefineTest2=this;
                            return $$$cl2243.String("hello from RefineTest2.Inner",28);
                        };
                    })(Inner$RefineTest2.$$.prototype);
                }
                return Inner$RefineTest2;
            }
            $$refineTest2.$init$Inner$RefineTest2=$init$Inner$RefineTest2;
            $init$Inner$RefineTest2();
            $$refineTest2.Inner$RefineTest2=Inner$RefineTest2;
        })(RefineTest2.$$.prototype);
    }
    return RefineTest2;
}
exports.$init$RefineTest2=$init$RefineTest2;
$init$RefineTest2();

//ClassDefinition RefineTest3 at refinement.ceylon (27:0-33:0)
function RefineTest3($$refineTest3){
    $init$RefineTest3();
    if ($$refineTest3===undefined)$$refineTest3=new RefineTest3.$$;
    return $$refineTest3;
}
exports.RefineTest3=RefineTest3;
function $init$RefineTest3(){
    if (RefineTest3.$$===undefined){
        $$$cl2243.initTypeProto(RefineTest3,'nesting::RefineTest3',$$$cl2243.Basic);
        (function($$refineTest3){
            
            //ClassDefinition Inner at refinement.ceylon (28:4-32:4)
            function Inner$RefineTest3($$inner$RefineTest3){
                $init$Inner$RefineTest3();
                if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new this.Inner$RefineTest3.$$;
                $$inner$RefineTest3.$$refineTest3=this;
                return $$inner$RefineTest3;
            }
            function $init$Inner$RefineTest3(){
                if (Inner$RefineTest3.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$RefineTest3,'nesting::RefineTest3.Inner',$$$cl2243.Basic);
                    (function($$inner$RefineTest3){
                        
                        //MethodDefinition x at refinement.ceylon (29:8-31:8)
                        $$inner$RefineTest3.x=function x(){
                            var $$inner$RefineTest3=this;
                            return $$$cl2243.String("x",1);
                        };
                    })(Inner$RefineTest3.$$.prototype);
                }
                return Inner$RefineTest3;
            }
            $$refineTest3.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
            $init$Inner$RefineTest3();
            $$refineTest3.Inner$RefineTest3=Inner$RefineTest3;
        })(RefineTest3.$$.prototype);
    }
    return RefineTest3;
}
exports.$init$RefineTest3=$init$RefineTest3;
$init$RefineTest3();

//ClassDefinition Y at refinement.ceylon (35:0-47:0)
function Y($$y){
    $init$Y();
    if ($$y===undefined)$$y=new Y.$$;
    X($$y);
    return $$y;
}
exports.Y=Y;
function $init$Y(){
    if (Y.$$===undefined){
        $$$cl2243.initTypeProto(Y,'nesting::Y',X);
        (function($$y){
            
            //ClassDefinition SubRef1 at refinement.ceylon (36:4-46:4)
            function SubRef1$Y($$subRef1$Y){
                $init$SubRef1$Y();
                if ($$subRef1$Y===undefined)$$subRef1$Y=new this.SubRef1$Y.$$;
                $$subRef1$Y.$$y=this;
                $$subRef1$Y.$$y.RefineTest1$X($$subRef1$Y);
                return $$subRef1$Y;
            }
            function $init$SubRef1$Y(){
                if (SubRef1$Y.$$===undefined){
                    $$$cl2243.initTypeProto(SubRef1$Y,'nesting::Y.SubRef1',$$y.RefineTest1$X);
                    (function($$subRef1$Y){
                        
                        //ClassDefinition Inner at refinement.ceylon (37:6-45:6)
                        function Inner$RefineTest1$X($$inner$RefineTest1$X){
                            $init$Inner$RefineTest1$X();
                            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new this.Inner$RefineTest1$X.$$;
                            $$inner$RefineTest1$X.$$subRef1$Y=this;
                            $$inner$RefineTest1$X.$$subRef1$Y.getT$all()['nesting::X.RefineTest1'].$$.prototype.Inner$RefineTest1$X.call(this,$$inner$RefineTest1$X);
                            
                            //AttributeDeclaration suborigin at refinement.ceylon (38:10-38:51)
                            $$inner$RefineTest1$X.suborigin$3041=$$$cl2243.String("SubRef1.Inner",13);
                            return $$inner$RefineTest1$X;
                        }
                        function $init$Inner$RefineTest1$X(){
                            if (Inner$RefineTest1$X.$$===undefined){
                                $$$cl2243.initTypeProto(Inner$RefineTest1$X,'nesting::Y.SubRef1.Inner',$$subRef1$Y.getT$all()['nesting::X.RefineTest1'].$$.prototype.Inner$RefineTest1$X);
                                (function($$inner$RefineTest1$X){
                                    
                                    //AttributeDeclaration suborigin at refinement.ceylon (38:10-38:51)
                                    $$inner$RefineTest1$X.getSuborigin=function getSuborigin(){
                                        return this.suborigin$3041;
                                    };
                                    
                                    //MethodDefinition x at refinement.ceylon (39:10-41:10)
                                    $$inner$RefineTest1$X.x=function x(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl2243.String("REFINED ",8).plus($$inner$RefineTest1$X.getT$all()['nesting::X.RefineTest1.Inner'].$$.prototype.x.call(this));
                                    };
                                    //MethodDefinition y at refinement.ceylon (42:10-44:10)
                                    $$inner$RefineTest1$X.y=function y(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl2243.String("y",1);
                                    };
                                })(Inner$RefineTest1$X.$$.prototype);
                            }
                            return Inner$RefineTest1$X;
                        }
                        $$subRef1$Y.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
                        $init$Inner$RefineTest1$X();
                        $$subRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
                    })(SubRef1$Y.$$.prototype);
                }
                return SubRef1$Y;
            }
            $$y.$init$SubRef1$Y=$init$SubRef1$Y;
            $init$SubRef1$Y();
            $$y.SubRef1$Y=SubRef1$Y;
        })(Y.$$.prototype);
    }
    return Y;
}
exports.$init$Y=$init$Y;
$init$Y();

//ClassDefinition SubRef2 at refinement.ceylon (49:0-53:0)
function SubRef2($$subRef2){
    $init$SubRef2();
    if ($$subRef2===undefined)$$subRef2=new SubRef2.$$;
    RefineTest2($$subRef2);
    return $$subRef2;
}
exports.SubRef2=SubRef2;
function $init$SubRef2(){
    if (SubRef2.$$===undefined){
        $$$cl2243.initTypeProto(SubRef2,'nesting::SubRef2',RefineTest2);
        (function($$subRef2){
            
            //MethodDefinition x at refinement.ceylon (50:4-52:4)
            $$subRef2.x=function x(){
                var $$subRef2=this;
                return $$subRef2.Inner$RefineTest2().hello();
            };
        })(SubRef2.$$.prototype);
    }
    return SubRef2;
}
exports.$init$SubRef2=$init$SubRef2;
$init$SubRef2();

//ClassDefinition SubRef3 at refinement.ceylon (55:0-59:0)
function SubRef3($$subRef3){
    $init$SubRef3();
    if ($$subRef3===undefined)$$subRef3=new SubRef3.$$;
    RefineTest3($$subRef3);
    return $$subRef3;
}
exports.SubRef3=SubRef3;
function $init$SubRef3(){
    if (SubRef3.$$===undefined){
        $$$cl2243.initTypeProto(SubRef3,'nesting::SubRef3',RefineTest3);
        (function($$subRef3){
            
            //MethodDefinition x at refinement.ceylon (56:4-58:4)
            $$subRef3.x=function x(){
                var $$subRef3=this;
                return $$subRef3.Inner$RefineTest3().x();
            };
        })(SubRef3.$$.prototype);
    }
    return SubRef3;
}
exports.$init$SubRef3=$init$SubRef3;
$init$SubRef3();

//ClassDefinition SubRef31 at refinement.ceylon (60:0-64:0)
function SubRef31($$subRef31){
    $init$SubRef31();
    if ($$subRef31===undefined)$$subRef31=new SubRef31.$$;
    SubRef3($$subRef31);
    return $$subRef31;
}
exports.SubRef31=SubRef31;
function $init$SubRef31(){
    if (SubRef31.$$===undefined){
        $$$cl2243.initTypeProto(SubRef31,'nesting::SubRef31',SubRef3);
        (function($$subRef31){
            
            //ClassDefinition Inner at refinement.ceylon (61:4-63:4)
            function Inner$RefineTest3($$inner$RefineTest3){
                $init$Inner$RefineTest3();
                if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new this.Inner$RefineTest3.$$;
                $$inner$RefineTest3.$$subRef31=this;
                $$inner$RefineTest3.$$subRef31.getT$all()['nesting::RefineTest3'].$$.prototype.Inner$RefineTest3.call(this,$$inner$RefineTest3);
                return $$inner$RefineTest3;
            }
            function $init$Inner$RefineTest3(){
                if (Inner$RefineTest3.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$RefineTest3,'nesting::SubRef31.Inner',$$subRef31.getT$all()['nesting::RefineTest3'].$$.prototype.Inner$RefineTest3);
                    (function($$inner$RefineTest3){
                        
                        //MethodDefinition x at refinement.ceylon (62:8-62:51)
                        $$inner$RefineTest3.x=function x(){
                            var $$inner$RefineTest3=this;
                            return $$$cl2243.String("equis",5);
                        };
                    })(Inner$RefineTest3.$$.prototype);
                }
                return Inner$RefineTest3;
            }
            $$subRef31.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
            $init$Inner$RefineTest3();
            $$subRef31.Inner$RefineTest3=Inner$RefineTest3;
        })(SubRef31.$$.prototype);
    }
    return SubRef31;
}
exports.$init$SubRef31=$init$SubRef31;
$init$SubRef31();

//MethodDefinition testRefinement at refinement.ceylon (66:0-76:0)
function testRefinement(){
    
    //AttributeDeclaration c1 at refinement.ceylon (67:4-67:36)
    var c1$3042=Y().SubRef1$Y().Inner$RefineTest1$X();
    $$$c2244.check($$$cl2243.className(c1$3042).equals($$$cl2243.String("nesting::Y.SubRef1.Inner",24)),$$$cl2243.String("classname is ",13).plus($$$cl2243.className(c1$3042)));
    $$$c2244.check(c1$3042.getOrigin().equals($$$cl2243.String("RefineTest1.Inner",17)),$$$cl2243.String("refinement [1]",14));
    $$$c2244.check(c1$3042.getSuborigin().equals($$$cl2243.String("SubRef1.Inner",13)),$$$cl2243.String("refinement [2]",14));
    $$$c2244.check(c1$3042.x().equals($$$cl2243.String("REFINED x and y",15)),$$$cl2243.String("refinement [3]",14));
    $$$c2244.check(c1$3042.x().equals(Y().SubRef1$Y().outerx()),$$$cl2243.String("refinement [4]",14));
    $$$c2244.check(SubRef2().x().equals($$$cl2243.String("hello from RefineTest2.Inner",28)),$$$cl2243.String("refinement [5]",14));
    $$$c2244.check(SubRef3().x().equals($$$cl2243.String("x",1)),$$$cl2243.String("refinement [6]",14));
    $$$c2244.check(SubRef31().x().equals($$$cl2243.String("equis",5)),$$$cl2243.String("refinement [7]",14));
};

//ClassDefinition X2 at refinement2.ceylon (4:0-18:0)
function X2(a$3043, $$x2){
    $init$X2();
    if ($$x2===undefined)$$x2=new X2.$$;
    $$x2.a$3043=a$3043;
    return $$x2;
}
exports.X2=X2;
function $init$X2(){
    if (X2.$$===undefined){
        $$$cl2243.initTypeProto(X2,'nesting::X2',$$$cl2243.Basic);
        (function($$x2){
            
            //ClassDefinition RefineTest1 at refinement2.ceylon (5:4-17:4)
            function RefineTest1$X2(b$3044, $$refineTest1$X2){
                $init$RefineTest1$X2();
                if ($$refineTest1$X2===undefined)$$refineTest1$X2=new this.RefineTest1$X2.$$;
                $$refineTest1$X2.$$x2=this;
                $$refineTest1$X2.b$3044=b$3044;
                return $$refineTest1$X2;
            }
            function $init$RefineTest1$X2(){
                if (RefineTest1$X2.$$===undefined){
                    $$$cl2243.initTypeProto(RefineTest1$X2,'nesting::X2.RefineTest1',$$$cl2243.Basic);
                    (function($$refineTest1$X2){
                        
                        //ClassDefinition Inner at refinement2.ceylon (6:8-13:8)
                        function Inner$RefineTest1$X2(c$3045, $$targs$$,$$inner$RefineTest1$X2){
                            $init$Inner$RefineTest1$X2();
                            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new this.Inner$RefineTest1$X2.$$;
                            $$$cl2243.set_type_args($$inner$RefineTest1$X2,$$targs$$);
                            $$inner$RefineTest1$X2.$$refineTest1$X2=this;
                            $$inner$RefineTest1$X2.c$3045=c$3045;
                            
                            //AttributeDeclaration origin at refinement2.ceylon (8:12-8:62)
                            $$inner$RefineTest1$X2.origin$3046=$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("RefineTest1.Inner (",19),$$inner$RefineTest1$X2.c$3045.getString(),$$$cl2243.String(")",1)]).getString();
                            return $$inner$RefineTest1$X2;
                        }
                        function $init$Inner$RefineTest1$X2(){
                            if (Inner$RefineTest1$X2.$$===undefined){
                                $$$cl2243.initTypeProto(Inner$RefineTest1$X2,'nesting::X2.RefineTest1.Inner',$$$cl2243.Basic);
                                (function($$inner$RefineTest1$X2){
                                    
                                    //AttributeDeclaration origin at refinement2.ceylon (8:12-8:62)
                                    $$inner$RefineTest1$X2.getOrigin=function getOrigin(){
                                        return this.origin$3046;
                                    };
                                    
                                    //MethodDefinition x at refinement2.ceylon (9:12-11:12)
                                    $$inner$RefineTest1$X2.x=function x(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("x and ",6),$$inner$RefineTest1$X2.y().getString(),$$$cl2243.String(" and a:",7),$$inner$RefineTest1$X2.$$refineTest1$X2.$$x2.a$3043.getString(),$$$cl2243.String(", b:",4),$$inner$RefineTest1$X2.$$refineTest1$X2.b$3044.getString(),$$$cl2243.String(", c:",4),$$inner$RefineTest1$X2.c$3045.getString(),$$$cl2243.String(".",1)]).getString();
                                    };
                                })(Inner$RefineTest1$X2.$$.prototype);
                            }
                            return Inner$RefineTest1$X2;
                        }
                        $$refineTest1$X2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
                        $init$Inner$RefineTest1$X2();
                        $$refineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                        
                        //MethodDefinition outerx at refinement2.ceylon (14:8-16:8)
                        $$refineTest1$X2.outerx=function outerx(){
                            var $$refineTest1$X2=this;
                            return $$refineTest1$X2.Inner$RefineTest1$X2($$refineTest1$X2.$$x2.a$3043.getUppercased(),{Element:{t:$$$cl2243.String}}).x();
                        };
                    })(RefineTest1$X2.$$.prototype);
                }
                return RefineTest1$X2;
            }
            $$x2.$init$RefineTest1$X2=$init$RefineTest1$X2;
            $init$RefineTest1$X2();
            $$x2.RefineTest1$X2=RefineTest1$X2;
        })(X2.$$.prototype);
    }
    return X2;
}
exports.$init$X2=$init$X2;
$init$X2();

//ClassDefinition RefineTest4 at refinement2.ceylon (21:0-25:0)
function RefineTest4(d$3047, $$refineTest4){
    $init$RefineTest4();
    if ($$refineTest4===undefined)$$refineTest4=new RefineTest4.$$;
    return $$refineTest4;
}
exports.RefineTest4=RefineTest4;
function $init$RefineTest4(){
    if (RefineTest4.$$===undefined){
        $$$cl2243.initTypeProto(RefineTest4,'nesting::RefineTest4',$$$cl2243.Basic);
        (function($$refineTest4){
            
            //ClassDefinition Inner at refinement2.ceylon (22:4-24:4)
            function Inner$RefineTest4(e$3048, $$inner$RefineTest4){
                $init$Inner$RefineTest4();
                if ($$inner$RefineTest4===undefined)$$inner$RefineTest4=new this.Inner$RefineTest4.$$;
                $$inner$RefineTest4.$$refineTest4=this;
                $$inner$RefineTest4.e$3048=e$3048;
                return $$inner$RefineTest4;
            }
            function $init$Inner$RefineTest4(){
                if (Inner$RefineTest4.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$RefineTest4,'nesting::RefineTest4.Inner',$$$cl2243.Basic);
                    (function($$inner$RefineTest4){
                        
                        //MethodDefinition hello at refinement2.ceylon (23:8-23:83)
                        $$inner$RefineTest4.hello=function hello(){
                            var $$inner$RefineTest4=this;
                            return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("hello from RefineTest2.Inner with ",34),$$inner$RefineTest4.e$3048.getString(),$$$cl2243.String(".",1)]).getString();
                        };
                    })(Inner$RefineTest4.$$.prototype);
                }
                return Inner$RefineTest4;
            }
            $$refineTest4.$init$Inner$RefineTest4=$init$Inner$RefineTest4;
            $init$Inner$RefineTest4();
            $$refineTest4.Inner$RefineTest4=Inner$RefineTest4;
        })(RefineTest4.$$.prototype);
    }
    return RefineTest4;
}
exports.$init$RefineTest4=$init$RefineTest4;
$init$RefineTest4();

//ClassDefinition RefineTest5 at refinement2.ceylon (28:0-34:0)
function RefineTest5(f$3049, $$refineTest5){
    $init$RefineTest5();
    if ($$refineTest5===undefined)$$refineTest5=new RefineTest5.$$;
    $$refineTest5.f$3049=f$3049;
    return $$refineTest5;
}
exports.RefineTest5=RefineTest5;
function $init$RefineTest5(){
    if (RefineTest5.$$===undefined){
        $$$cl2243.initTypeProto(RefineTest5,'nesting::RefineTest5',$$$cl2243.Basic);
        (function($$refineTest5){
            
            //ClassDefinition Inner at refinement2.ceylon (29:4-33:4)
            function Inner$RefineTest5(g$3050, $$inner$RefineTest5){
                $init$Inner$RefineTest5();
                if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new this.Inner$RefineTest5.$$;
                $$inner$RefineTest5.$$refineTest5=this;
                $$inner$RefineTest5.g$3050=g$3050;
                return $$inner$RefineTest5;
            }
            function $init$Inner$RefineTest5(){
                if (Inner$RefineTest5.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$RefineTest5,'nesting::RefineTest5.Inner',$$$cl2243.Basic);
                    (function($$inner$RefineTest5){
                        
                        //MethodDefinition x at refinement2.ceylon (30:8-32:8)
                        $$inner$RefineTest5.x=function x(){
                            var $$inner$RefineTest5=this;
                            return $$inner$RefineTest5.g$3050.repeat($$inner$RefineTest5.$$refineTest5.f$3049);
                        };
                    })(Inner$RefineTest5.$$.prototype);
                }
                return Inner$RefineTest5;
            }
            $$refineTest5.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
            $init$Inner$RefineTest5();
            $$refineTest5.Inner$RefineTest5=Inner$RefineTest5;
        })(RefineTest5.$$.prototype);
    }
    return RefineTest5;
}
exports.$init$RefineTest5=$init$RefineTest5;
$init$RefineTest5();

//ClassDefinition Y2 at refinement2.ceylon (36:0-49:0)
function Y2(h$3051, $$y2){
    $init$Y2();
    if ($$y2===undefined)$$y2=new Y2.$$;
    $$y2.h$3051=h$3051;
    X2($$y2.h$3051,$$y2);
    return $$y2;
}
exports.Y2=Y2;
function $init$Y2(){
    if (Y2.$$===undefined){
        $$$cl2243.initTypeProto(Y2,'nesting::Y2',X2);
        (function($$y2){
            
            //ClassDefinition SubRef1 at refinement2.ceylon (37:4-48:4)
            function SubRef1$Y2(d$3052, $$subRef1$Y2){
                $init$SubRef1$Y2();
                if ($$subRef1$Y2===undefined)$$subRef1$Y2=new this.SubRef1$Y2.$$;
                $$subRef1$Y2.$$y2=this;
                $$subRef1$Y2.d$3052=d$3052;
                $$subRef1$Y2.$$y2.RefineTest1$X2((1),$$subRef1$Y2);
                return $$subRef1$Y2;
            }
            function $init$SubRef1$Y2(){
                if (SubRef1$Y2.$$===undefined){
                    $$$cl2243.initTypeProto(SubRef1$Y2,'nesting::Y2.SubRef1',$$y2.RefineTest1$X2);
                    (function($$subRef1$Y2){
                        
                        //ClassDefinition Inner at refinement2.ceylon (38:6-47:6)
                        function Inner$RefineTest1$X2(d2$3053, $$targs$$,$$inner$RefineTest1$X2){
                            $init$Inner$RefineTest1$X2();
                            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new this.Inner$RefineTest1$X2.$$;
                            $$$cl2243.set_type_args($$inner$RefineTest1$X2,$$targs$$);
                            $$inner$RefineTest1$X2.$$subRef1$Y2=this;
                            $$inner$RefineTest1$X2.d2$3053=d2$3053;
                            $$inner$RefineTest1$X2.$$subRef1$Y2.getT$all()['nesting::X2.RefineTest1'].$$.prototype.Inner$RefineTest1$X2.call(this,$$inner$RefineTest1$X2.d2$3053,{Element:$$inner$RefineTest1$X2.$$targs$$.Element},$$inner$RefineTest1$X2);
                            
                            //AttributeDeclaration suborigin at refinement2.ceylon (40:10-40:51)
                            $$inner$RefineTest1$X2.suborigin$3054=$$$cl2243.String("SubRef1.Inner",13);
                            return $$inner$RefineTest1$X2;
                        }
                        function $init$Inner$RefineTest1$X2(){
                            if (Inner$RefineTest1$X2.$$===undefined){
                                $$$cl2243.initTypeProto(Inner$RefineTest1$X2,'nesting::Y2.SubRef1.Inner',$$subRef1$Y2.getT$all()['nesting::X2.RefineTest1'].$$.prototype.Inner$RefineTest1$X2);
                                (function($$inner$RefineTest1$X2){
                                    
                                    //AttributeDeclaration suborigin at refinement2.ceylon (40:10-40:51)
                                    $$inner$RefineTest1$X2.getSuborigin=function getSuborigin(){
                                        return this.suborigin$3054;
                                    };
                                    
                                    //MethodDefinition x at refinement2.ceylon (41:10-43:10)
                                    $$inner$RefineTest1$X2.x=function x(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl2243.String("REFINED ",8).plus($$inner$RefineTest1$X2.getT$all()['nesting::X2.RefineTest1.Inner'].$$.prototype.x.call(this));
                                    };
                                    //MethodDefinition y at refinement2.ceylon (44:10-46:10)
                                    $$inner$RefineTest1$X2.y=function y(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("y",1),$$inner$RefineTest1$X2.$$subRef1$Y2.$$y2.h$3051.getString(),$$$cl2243.String(",d:",3),$$inner$RefineTest1$X2.$$subRef1$Y2.d$3052.getString(),$$$cl2243.String(",d2:",4),$$inner$RefineTest1$X2.d2$3053.getString(),$$$cl2243.String(".",1)]).getString();
                                    };
                                })(Inner$RefineTest1$X2.$$.prototype);
                            }
                            return Inner$RefineTest1$X2;
                        }
                        $$subRef1$Y2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
                        $init$Inner$RefineTest1$X2();
                        $$subRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                    })(SubRef1$Y2.$$.prototype);
                }
                return SubRef1$Y2;
            }
            $$y2.$init$SubRef1$Y2=$init$SubRef1$Y2;
            $init$SubRef1$Y2();
            $$y2.SubRef1$Y2=SubRef1$Y2;
        })(Y2.$$.prototype);
    }
    return Y2;
}
exports.$init$Y2=$init$Y2;
$init$Y2();

//ClassDefinition SubRef4 at refinement2.ceylon (51:0-55:0)
function SubRef4($$subRef4){
    $init$SubRef4();
    if ($$subRef4===undefined)$$subRef4=new SubRef4.$$;
    RefineTest4($$$cl2243.String("t4",2),$$subRef4);
    return $$subRef4;
}
exports.SubRef4=SubRef4;
function $init$SubRef4(){
    if (SubRef4.$$===undefined){
        $$$cl2243.initTypeProto(SubRef4,'nesting::SubRef4',RefineTest4);
        (function($$subRef4){
            
            //MethodDefinition x at refinement2.ceylon (52:4-54:4)
            $$subRef4.x=function x(){
                var $$subRef4=this;
                return $$subRef4.Inner$RefineTest4((5)).hello();
            };
        })(SubRef4.$$.prototype);
    }
    return SubRef4;
}
exports.$init$SubRef4=$init$SubRef4;
$init$SubRef4();

//ClassDefinition SubRef5 at refinement2.ceylon (57:0-61:0)
function SubRef5($$subRef5){
    $init$SubRef5();
    if ($$subRef5===undefined)$$subRef5=new SubRef5.$$;
    RefineTest5((6),$$subRef5);
    return $$subRef5;
}
exports.SubRef5=SubRef5;
function $init$SubRef5(){
    if (SubRef5.$$===undefined){
        $$$cl2243.initTypeProto(SubRef5,'nesting::SubRef5',RefineTest5);
        (function($$subRef5){
            
            //MethodDefinition x at refinement2.ceylon (58:4-60:4)
            $$subRef5.x=function x(){
                var $$subRef5=this;
                return $$subRef5.Inner$RefineTest5($$$cl2243.String("sr5",3)).x();
            };
        })(SubRef5.$$.prototype);
    }
    return SubRef5;
}
exports.$init$SubRef5=$init$SubRef5;
$init$SubRef5();

//ClassDefinition SubRef51 at refinement2.ceylon (62:0-66:0)
function SubRef51($$subRef51){
    $init$SubRef51();
    if ($$subRef51===undefined)$$subRef51=new SubRef51.$$;
    SubRef5($$subRef51);
    return $$subRef51;
}
exports.SubRef51=SubRef51;
function $init$SubRef51(){
    if (SubRef51.$$===undefined){
        $$$cl2243.initTypeProto(SubRef51,'nesting::SubRef51',SubRef5);
        (function($$subRef51){
            
            //ClassDefinition Inner at refinement2.ceylon (63:4-65:4)
            function Inner$RefineTest5(subg55$3055, $$inner$RefineTest5){
                $init$Inner$RefineTest5();
                if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new this.Inner$RefineTest5.$$;
                $$inner$RefineTest5.$$subRef51=this;
                $$inner$RefineTest5.subg55$3055=subg55$3055;
                $$inner$RefineTest5.$$subRef51.getT$all()['nesting::RefineTest5'].$$.prototype.Inner$RefineTest5.call(this,$$inner$RefineTest5.subg55$3055,$$inner$RefineTest5);
                return $$inner$RefineTest5;
            }
            function $init$Inner$RefineTest5(){
                if (Inner$RefineTest5.$$===undefined){
                    $$$cl2243.initTypeProto(Inner$RefineTest5,'nesting::SubRef51.Inner',$$subRef51.getT$all()['nesting::RefineTest5'].$$.prototype.Inner$RefineTest5);
                    (function($$inner$RefineTest5){
                        
                        //MethodDefinition x at refinement2.ceylon (64:8-64:62)
                        $$inner$RefineTest5.x=function x(){
                            var $$inner$RefineTest5=this;
                            return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("equis",5),$$inner$RefineTest5.subg55$3055.getString(),$$$cl2243.String(".",1)]).getString();
                        };
                    })(Inner$RefineTest5.$$.prototype);
                }
                return Inner$RefineTest5;
            }
            $$subRef51.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
            $init$Inner$RefineTest5();
            $$subRef51.Inner$RefineTest5=Inner$RefineTest5;
        })(SubRef51.$$.prototype);
    }
    return SubRef51;
}
exports.$init$SubRef51=$init$SubRef51;
$init$SubRef51();

//MethodDefinition testRefinement2 at refinement2.ceylon (68:0-78:0)
function testRefinement2(){
    
    //AttributeDeclaration c1 at refinement2.ceylon (69:4-69:54)
    var c1$3056=Y2($$$cl2243.String("y2",2)).SubRef1$Y2((99)).Inner$RefineTest1$X2($$$cl2243.String("with parm",9),{Element:{t:$$$cl2243.String}});
    $$$c2244.check($$$cl2243.className(c1$3056).equals($$$cl2243.String("nesting::Y2.SubRef1.Inner",25)),$$$cl2243.String("classname is ",13).plus($$$cl2243.className(c1$3056)));
    $$$c2244.check(c1$3056.getOrigin().equals($$$cl2243.String("RefineTest1.Inner (with parm)",29)),$$$cl2243.String("refinement [1] ",15).plus(c1$3056.getOrigin()));
    $$$c2244.check(c1$3056.getSuborigin().equals($$$cl2243.String("SubRef1.Inner",13)),$$$cl2243.String("refinement [2] ",15).plus(c1$3056.getSuborigin()));
    $$$c2244.check(c1$3056.x().equals($$$cl2243.String("REFINED x and yy2,d:99,d2:with parm. and a:y2, b:1, c:with parm.",64)),$$$cl2243.String("refinement [3] ",15).plus(c1$3056.x()));
    $$$c2244.check(Y2($$$cl2243.String("y3",2)).SubRef1$Y2((10)).outerx().equals($$$cl2243.String("REFINED x and yy3,d:10,d2:Y3. and a:y3, b:1, c:Y3.",50)),$$$cl2243.String("refinement [4] ",15).plus(Y2($$$cl2243.String("y3",2)).SubRef1$Y2((10)).outerx()));
    $$$c2244.check(SubRef4().x().equals($$$cl2243.String("hello from RefineTest2.Inner with 5.",36)),$$$cl2243.String("refinement [5] ",15).plus(SubRef4().x()));
    $$$c2244.check(SubRef5().x().equals($$$cl2243.String("sr5sr5sr5sr5sr5sr5",18)),$$$cl2243.String("refinement [6] ",15).plus(SubRef5().x()));
    $$$c2244.check(SubRef51().x().equals($$$cl2243.String("equissr5.",9)),$$$cl2243.String("refinement [7] ",15).plus(SubRef51().x()));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
