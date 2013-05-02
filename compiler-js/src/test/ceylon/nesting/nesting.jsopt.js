(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"nesting","$mod-version":"0.1","nesting":{"Y2":{"super":{"$pk":"nesting","$nm":"X2"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"h"}],"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"d"}],"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"d2"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y2"},"SubRef51":{"super":{"$pk":"nesting","$nm":"SubRef5"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"subg55"}],"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef51"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"baz":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"baz"}},"$c":{"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"foobar":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"foobar"},"quxx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"quxx"}},"$nm":"C"}},"$at":{"qux":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"qux"}},"$nm":"B"}},"$at":{"foo":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"foo"}},"$nm":"A"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Unwrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$an":{"shared":[]},"$nm":"o"}},"$nm":"Unwrapper"},"SubRef2":{"super":{"$pk":"nesting","$nm":"RefineTest2"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef2"},"Wrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Wrapper"},"O":{"$i":{"InnerInterface":{"$mt":"ifc","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerInterface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"test1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test1"},"test2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test2"},"test3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test3"}},"$c":{"InnerClass":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerClass"}},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"s"}},"$nm":"O","$o":{"innerObject":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"innerObject"}}},"outr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"outr"},"testRefinement2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement2"},"Holder":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"o"}],"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"}},"$nm":"Holder"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C3":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"x"}},"$nm":"C3"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"},"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C3"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"},"Y":{"super":{"$pk":"nesting","$nm":"X"},"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y"},"X":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X"},"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"},"outerf":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$nm":"outerf"},"X2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"a"}],"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"c"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X2"},"OuterC1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"OuterC1"},"SubRef31":{"super":{"$pk":"nesting","$nm":"SubRef3"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef31"},"OuterC2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"}},"$nm":"OuterC2"},"testRefinement":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement"},"returner":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"o"}]],"$mt":"mthd","$nm":"returner"},"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"noop"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"noop"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"printName"}},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"gttr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Inner"}},"$at":{"inner":{"$t":{"$pk":"nesting","$nm":"Inner"},"$mt":"attr","$nm":"inner"},"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"attr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Outer"},"producer":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"mthd","$nm":"producer"},"RefineTest5":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"f"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"g"}],"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest5"},"RefineTest4":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"d"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"e"}],"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest4"},"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}},"RefineTest3":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest3"},"RefineTest2":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest2"},"SubRef5":{"super":{"$pk":"nesting","$nm":"RefineTest5"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef5"},"SubRef4":{"super":{"$pk":"nesting","$nm":"RefineTest4"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef4"},"SubRef3":{"super":{"$pk":"nesting","$nm":"RefineTest3"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef3"}}}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2593=require('check/0.1/check-0.1');

//ClassDefinition Outer at nesting.ceylon (3:0-28:0)
function Outer(name$3494, $$outer){
    $init$Outer();
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$3494=name$3494;
    
    //AttributeDeclaration int at nesting.ceylon (4:4-4:18)
    $$outer.int$3495_=(10);
    
    //AttributeDeclaration float at nesting.ceylon (5:4-5:34)
    $$outer.float$3496_=$$outer.int$3495.$float;
    
    //AttributeDeclaration inner at nesting.ceylon (21:4-21:25)
    $$outer.inner$3497_=$$outer.Inner$3498();
    $$$cl2592.print($$outer.inner$3497.$int);
    $$$cl2592.print($$outer.inner$3497.$float);
    $$outer.inner$3497.noop();
    $$outer.noop$3499();
    return $$outer;
}
exports.Outer=Outer;
function $init$Outer(){
    if (Outer.$$===undefined){
        $$$cl2592.initTypeProto(Outer,'nesting::Outer',$$$cl2592.Basic);
        (function($$outer){
            
            //AttributeDeclaration int at nesting.ceylon (4:4-4:18)
            $$$cl2592.defineAttr($$outer,'int$3495',function(){return this.int$3495_;});
            
            //AttributeDeclaration float at nesting.ceylon (5:4-5:34)
            $$$cl2592.defineAttr($$outer,'$float',function(){return this.float$3496_;});
            
            //MethodDefinition noop at nesting.ceylon (6:4-6:17)
            $$outer.noop$3499=function noop$3499(){
                var $$outer=this;
            };$$outer.noop$3499.$$metamodel$$={$nm:'noop',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
            //ClassDefinition Inner at nesting.ceylon (7:4-20:4)
            function Inner$3498($$inner$3498){
                $init$Inner$3498();
                if ($$inner$3498===undefined)$$inner$3498=new this.Inner$3498.$$;
                $$inner$3498.$$outer=this;
                return $$inner$3498;
            }
            function $init$Inner$3498(){
                if (Inner$3498.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$3498,'nesting::Outer.Inner',$$$cl2592.Basic);
                    Outer.Inner$3498=Inner$3498;
                    (function($$inner$3498){
                        
                        //MethodDefinition printName at nesting.ceylon (8:8-10:8)
                        $$inner$3498.printName$3500=function printName$3500(){
                            var $$inner$3498=this;
                            $$$cl2592.print($$inner$3498.$$outer.name$3494);
                        };$$inner$3498.printName$3500.$$metamodel$$={$nm:'printName',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
                        //AttributeGetterDefinition int at nesting.ceylon (11:8-13:8)
                        $$$cl2592.defineAttr($$inner$3498,'$int',function(){
                            var $$inner$3498=this;
                            return $$inner$3498.$$outer.int$3495;
                        });
                        //AttributeGetterDefinition float at nesting.ceylon (14:8-16:8)
                        $$$cl2592.defineAttr($$inner$3498,'$float',function(){
                            var $$inner$3498=this;
                            return $$inner$3498.$$outer.$float;
                        });
                        //MethodDefinition noop at nesting.ceylon (17:8-19:8)
                        $$inner$3498.noop=function noop(){
                            var $$inner$3498=this;
                            $$inner$3498.$$outer.noop$3499();
                        };$$inner$3498.noop.$$metamodel$$={$nm:'noop',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
                    })(Inner$3498.$$.prototype);
                }
                Inner$3498.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return Inner$3498;
            }
            $$outer.$init$Inner$3498=$init$Inner$3498;
            $init$Inner$3498();
            $$outer.Inner$3498=Inner$3498;
            
            //AttributeDeclaration inner at nesting.ceylon (21:4-21:25)
            $$$cl2592.defineAttr($$outer,'inner$3497',function(){return this.inner$3497_;});
        })(Outer.$$.prototype);
    }
    Outer.$$.$$metamodel$$={$nm:'Outer',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Outer;
}
exports.$init$Outer=$init$Outer;
$init$Outer();

//MethodDefinition outr at nesting.ceylon (30:0-42:0)
function outr(name$3501){
    
    //AttributeDeclaration uname at nesting.ceylon (31:4-31:34)
    var uname$3502=name$3501.uppercased;
    
    //MethodDefinition inr at nesting.ceylon (32:4-34:4)
    function inr$3503(){
        return name$3501;
    };inr$3503.$$metamodel$$={$nm:'inr',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};//inr$3503.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.String}};
    
    //AttributeGetterDefinition uinr at nesting.ceylon (35:4-37:4)
    var getUinr$3504=function(){
        return uname$3502;
    };
    
    //AttributeDeclaration result at nesting.ceylon (38:4-38:25)
    var result$3505=inr$3503();
    
    //AttributeDeclaration uresult at nesting.ceylon (39:4-39:25)
    var uresult$3506=getUinr$3504();
    $$$cl2592.print(result$3505);
    $$$cl2592.print(uresult$3506);
}
exports.outr=outr;
outr.$$metamodel$$={$nm:'outr',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl2592.String}}]};//outr.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.Anything}};

//ClassDefinition Holder at nesting.ceylon (44:0-51:0)
function Holder(o$3507, $$holder){
    $init$Holder();
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$3507=o$3507;
    return $$holder;
}
exports.Holder=Holder;
function $init$Holder(){
    if (Holder.$$===undefined){
        $$$cl2592.initTypeProto(Holder,'nesting::Holder',$$$cl2592.Basic);
        (function($$holder){
            
            //MethodDefinition get at nesting.ceylon (45:4-47:4)
            $$holder.get=function get(){
                var $$holder=this;
                return $$holder.o$3507;
            };$$holder.get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl2592.Object},$ps:[]};
            //AttributeGetterDefinition string at nesting.ceylon (48:4-50:4)
            $$$cl2592.defineAttr($$holder,'string',function(){
                var $$holder=this;
                return $$holder.o$3507.string;
            });
        })(Holder.$$.prototype);
    }
    Holder.$$.$$metamodel$$={$nm:'Holder',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Holder;
}
exports.$init$Holder=$init$Holder;
$init$Holder();

//ClassDefinition Wrapper at nesting.ceylon (53:0-61:0)
function Wrapper($$wrapper){
    $init$Wrapper();
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (54:4-54:18)
    $$wrapper.o$3508_=(100);
    return $$wrapper;
}
exports.Wrapper=Wrapper;
function $init$Wrapper(){
    if (Wrapper.$$===undefined){
        $$$cl2592.initTypeProto(Wrapper,'nesting::Wrapper',$$$cl2592.Basic);
        (function($$wrapper){
            
            //AttributeDeclaration o at nesting.ceylon (54:4-54:18)
            $$$cl2592.defineAttr($$wrapper,'o$3508',function(){return this.o$3508_;});
            
            //MethodDefinition get at nesting.ceylon (55:4-57:4)
            $$wrapper.get=function get(){
                var $$wrapper=this;
                return $$wrapper.o$3508;
            };$$wrapper.get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl2592.Object},$ps:[]};
            //AttributeGetterDefinition string at nesting.ceylon (58:4-60:4)
            $$$cl2592.defineAttr($$wrapper,'string',function(){
                var $$wrapper=this;
                return $$wrapper.o$3508.string;
            });
        })(Wrapper.$$.prototype);
    }
    Wrapper.$$.$$metamodel$$={$nm:'Wrapper',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Wrapper;
}
exports.$init$Wrapper=$init$Wrapper;
$init$Wrapper();

//ClassDefinition Unwrapper at nesting.ceylon (63:0-71:0)
function Unwrapper($$unwrapper){
    $init$Unwrapper();
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (64:4-64:27)
    $$unwrapper.o$3509_=$$$cl2592.Float(23.56);
    return $$unwrapper;
}
exports.Unwrapper=Unwrapper;
function $init$Unwrapper(){
    if (Unwrapper.$$===undefined){
        $$$cl2592.initTypeProto(Unwrapper,'nesting::Unwrapper',$$$cl2592.Basic);
        (function($$unwrapper){
            
            //AttributeDeclaration o at nesting.ceylon (64:4-64:27)
            $$$cl2592.defineAttr($$unwrapper,'o',function(){return this.o$3509_;});
            
            //MethodDefinition get at nesting.ceylon (65:4-67:4)
            $$unwrapper.get=function get(){
                var $$unwrapper=this;
                return $$unwrapper.o;
            };$$unwrapper.get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl2592.Object},$ps:[]};
            //AttributeGetterDefinition string at nesting.ceylon (68:4-70:4)
            $$$cl2592.defineAttr($$unwrapper,'string',function(){
                var $$unwrapper=this;
                return $$unwrapper.o.string;
            });
        })(Unwrapper.$$.prototype);
    }
    Unwrapper.$$.$$metamodel$$={$nm:'Unwrapper',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return Unwrapper;
}
exports.$init$Unwrapper=$init$Unwrapper;
$init$Unwrapper();

//MethodDefinition producer at nesting.ceylon (73:0-77:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (74:4-74:18)
    var o$3510=(123);
    
    //MethodDefinition produce at nesting.ceylon (75:4-75:35)
    function produce$3511(){
        return o$3510;
    };produce$3511.$$metamodel$$={$nm:'produce',$mt:'mthd',$t:{t:$$$cl2592.Object},$ps:[]};//produce$3511.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}};
    return produce$3511;
};producer.$$metamodel$$={$nm:'producer',$mt:'mthd',$t:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}}},$ps:[]};//producer.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}}}};

//MethodDefinition returner at nesting.ceylon (79:0-82:0)
function returner(o$3512){
    
    //MethodDefinition produce at nesting.ceylon (80:4-80:35)
    function produce$3513(){
        return o$3512;
    };produce$3513.$$metamodel$$={$nm:'produce',$mt:'mthd',$t:{t:$$$cl2592.Object},$ps:[]};//produce$3513.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}};
    return produce$3513;
};returner.$$metamodel$$={$nm:'returner',$mt:'mthd',$t:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}}},$ps:[{$nm:'o',$mt:'prm',$t:{t:$$$cl2592.Object}}]};//returner.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Object},Element:{t:$$$cl2592.Object}}},Return:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}}}};

//ClassDefinition A at nesting.ceylon (84:0-105:0)
function A($$a){
    $init$A();
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDeclaration foo at nesting.ceylon (85:4-85:22)
    $$a.foo$3514_=$$$cl2592.String("foo",3);
    return $$a;
}
function $init$A(){
    if (A.$$===undefined){
        $$$cl2592.initTypeProto(A,'nesting::A',$$$cl2592.Basic);
        (function($$a){
            
            //AttributeDeclaration foo at nesting.ceylon (85:4-85:22)
            $$$cl2592.defineAttr($$a,'foo$3514',function(){return this.foo$3514_;});
            
            //ClassDefinition B at nesting.ceylon (86:4-96:4)
            function B$A($$b$A){
                $init$B$A();
                if ($$b$A===undefined)$$b$A=new this.B$A.$$;
                $$b$A.$$outer=this;
                
                //AttributeDeclaration qux at nesting.ceylon (87:8-87:26)
                $$b$A.qux$3515_=$$$cl2592.String("qux",3);
                return $$b$A;
            }
            function $init$B$A(){
                if (B$A.$$===undefined){
                    $$$cl2592.initTypeProto(B$A,'nesting::A.B',$$$cl2592.Basic);
                    A.B$A=B$A;
                    (function($$b$A){
                        
                        //AttributeDeclaration qux at nesting.ceylon (87:8-87:26)
                        $$$cl2592.defineAttr($$b$A,'qux$3515',function(){return this.qux$3515_;});
                        
                        //ClassDefinition C at nesting.ceylon (88:8-95:8)
                        function C$B$A($$c$B$A){
                            $init$C$B$A();
                            if ($$c$B$A===undefined)$$c$B$A=new this.C$B$A.$$;
                            $$c$B$A.$$outer=this;
                            return $$c$B$A;
                        }
                        function $init$C$B$A(){
                            if (C$B$A.$$===undefined){
                                $$$cl2592.initTypeProto(C$B$A,'nesting::A.B.C',$$$cl2592.Basic);
                                A.B$A.C$B$A=C$B$A;
                                (function($$c$B$A){
                                    
                                    //MethodDefinition foobar at nesting.ceylon (89:12-91:12)
                                    $$c$B$A.foobar=function foobar(){
                                        var $$c$B$A=this;
                                        return $$c$B$A.$$outer.$$outer.foo$3514;
                                    };$$c$B$A.foobar.$$metamodel$$={$nm:'foobar',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                    //MethodDefinition quxx at nesting.ceylon (92:12-94:12)
                                    $$c$B$A.quxx=function quxx(){
                                        var $$c$B$A=this;
                                        return $$c$B$A.$$outer.qux$3515;
                                    };$$c$B$A.quxx.$$metamodel$$={$nm:'quxx',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                })(C$B$A.$$.prototype);
                            }
                            C$B$A.$$.$$metamodel$$={$nm:'C',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                            return C$B$A;
                        }
                        $$b$A.$init$C$B$A=$init$C$B$A;
                        $init$C$B$A();
                        $$b$A.C$B$A=C$B$A;
                    })(B$A.$$.prototype);
                }
                B$A.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return B$A;
            }
            $$a.$init$B$A=$init$B$A;
            $init$B$A();
            $$a.B$A=B$A;
            
            //MethodDefinition baz at nesting.ceylon (97:4-104:4)
            $$a.baz=function baz(){
                var $$a=this;
                
                //ClassDefinition Baz at nesting.ceylon (98:8-102:8)
                function Baz$3516($$baz$3516){
                    $init$Baz$3516();
                    if ($$baz$3516===undefined)$$baz$3516=new Baz$3516.$$;
                    return $$baz$3516;
                }
                function $init$Baz$3516(){
                    if (Baz$3516.$$===undefined){
                        $$$cl2592.initTypeProto(Baz$3516,'nesting::A.baz.Baz',$$$cl2592.Basic);
                        (function($$baz$3516){
                            
                            //MethodDefinition get at nesting.ceylon (99:12-101:12)
                            $$baz$3516.get=function get(){
                                var $$baz$3516=this;
                                return $$a.foo$3514;
                            };$$baz$3516.get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                        })(Baz$3516.$$.prototype);
                    }
                    Baz$3516.$$.$$metamodel$$={$nm:'Baz',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                    return Baz$3516;
                }
                $init$Baz$3516();
                return Baz$3516().get();
            };$$a.baz.$$metamodel$$={$nm:'baz',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(A.$$.prototype);
    }
    A.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return A;
}
exports.$init$A=$init$A;
$init$A();

//ClassDefinition O at nesting.ceylon (107:0-134:0)
function O($$o){
    $init$O();
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDeclaration s at nesting.ceylon (108:4-108:22)
    $$o.s$3517_=$$$cl2592.String("hello",5);
    
    //ObjectDefinition innerObject at nesting.ceylon (114:4-118:4)
    $$o.innerObject$3518_=$$o.innerObject$3519();
    return $$o;
}
function $init$O(){
    if (O.$$===undefined){
        $$$cl2592.initTypeProto(O,'nesting::O',$$$cl2592.Basic);
        (function($$o){
            
            //AttributeDeclaration s at nesting.ceylon (108:4-108:22)
            $$$cl2592.defineAttr($$o,'s$3517',function(){return this.s$3517_;});
            
            //ClassDefinition InnerClass at nesting.ceylon (109:4-113:4)
            function InnerClass$3520($$innerClass$3520){
                $init$InnerClass$3520();
                if ($$innerClass$3520===undefined)$$innerClass$3520=new this.InnerClass$3520.$$;
                $$innerClass$3520.$$outer=this;
                return $$innerClass$3520;
            }
            function $init$InnerClass$3520(){
                if (InnerClass$3520.$$===undefined){
                    $$$cl2592.initTypeProto(InnerClass$3520,'nesting::O.InnerClass',$$$cl2592.Basic);
                    O.InnerClass$3520=InnerClass$3520;
                    (function($$innerClass$3520){
                        
                        //MethodDefinition f at nesting.ceylon (110:8-112:8)
                        $$innerClass$3520.f=function f(){
                            var $$innerClass$3520=this;
                            return $$innerClass$3520.$$outer.s$3517;
                        };$$innerClass$3520.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(InnerClass$3520.$$.prototype);
                }
                InnerClass$3520.$$.$$metamodel$$={$nm:'InnerClass',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return InnerClass$3520;
            }
            $$o.$init$InnerClass$3520=$init$InnerClass$3520;
            $init$InnerClass$3520();
            $$o.InnerClass$3520=InnerClass$3520;
            
            //ObjectDefinition innerObject at nesting.ceylon (114:4-118:4)
            function innerObject$3519(){
                var $$innerObject$3519=new this.innerObject$3519.$$;
                $$innerObject$3519.$$outer=this;
                return $$innerObject$3519;
            }
            function $init$innerObject$3519(){
                if (innerObject$3519.$$===undefined){
                    $$$cl2592.initTypeProto(innerObject$3519,'nesting::O.innerObject',$$$cl2592.Basic);
                    O.innerObject$3519=innerObject$3519;
                }
                innerObject$3519.$$.$$metamodel$$={$nm:'innerObject',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return innerObject$3519;
            }
            $$o.$init$innerObject$3519=$init$innerObject$3519;
            $init$innerObject$3519();
            (function($$innerObject$3519){
                
                //MethodDefinition f at nesting.ceylon (115:8-117:8)
                $$innerObject$3519.f=function f(){
                    var $$innerObject$3519=this;
                    return $$innerObject$3519.$$outer.s$3517;
                };$$innerObject$3519.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            })(innerObject$3519.$$.prototype);
            $$$cl2592.defineAttr($$o,'innerObject$3518',function(){return this.innerObject$3518_;});
            $$o.innerObject$3519=innerObject$3519;
            
            //InterfaceDefinition InnerInterface at nesting.ceylon (119:4-123:4)
            function InnerInterface$3521($$innerInterface$3521){
                $$innerInterface$3521.$$outer=this;
            }
            function $init$InnerInterface$3521(){
                if (InnerInterface$3521.$$===undefined){
                    $$$cl2592.initTypeProto(InnerInterface$3521,'nesting::O.InnerInterface');
                    O.InnerInterface$3521=InnerInterface$3521;
                    (function($$innerInterface$3521){
                        
                        //MethodDefinition f at nesting.ceylon (120:8-122:8)
                        $$innerInterface$3521.f=function f(){
                            var $$innerInterface$3521=this;
                            return $$innerInterface$3521.$$outer.s$3517;
                        };$$innerInterface$3521.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(InnerInterface$3521.$$.prototype);
                }
                InnerInterface$3521.$$.$$metamodel$$={$nm:'InnerInterface',$mt:'ifc','satisfies':[]};
                return InnerInterface$3521;
            }
            $$o.$init$InnerInterface$3521=$init$InnerInterface$3521;
            $init$InnerInterface$3521();
            $$o.InnerInterface$3521=InnerInterface$3521;
            
            //MethodDefinition test1 at nesting.ceylon (124:4-126:4)
            $$o.test1=function test1(){
                var $$o=this;
                return $$o.InnerClass$3520().f();
            };$$o.test1.$$metamodel$$={$nm:'test1',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            //MethodDefinition test2 at nesting.ceylon (127:4-129:4)
            $$o.test2=function test2(){
                var $$o=this;
                return $$o.innerObject$3518.f();
            };$$o.test2.$$metamodel$$={$nm:'test2',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            //MethodDefinition test3 at nesting.ceylon (130:4-133:4)
            $$o.test3=function test3(){
                var $$o=this;
                
                //ObjectDefinition obj at nesting.ceylon (131:8-131:45)
                function obj$3522(){
                    var $$obj$3522=new obj$3522.$$;
                    $$o.InnerInterface$3521($$obj$3522);
                    return $$obj$3522;
                }
                function $init$obj$3522(){
                    if (obj$3522.$$===undefined){
                        $$$cl2592.initTypeProto(obj$3522,'nesting::O.test3.obj',$$$cl2592.Basic,$$o.$init$InnerInterface$3521());
                    }
                    obj$3522.$$.$$metamodel$$={$nm:'obj',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:O.InnerInterface$3521}]};
                    return obj$3522;
                }
                $init$obj$3522();
                var obj$3523=obj$3522();
                var getObj$3523=function(){
                    return obj$3523;
                }
                return getObj$3523().f();
            };$$o.test3.$$metamodel$$={$nm:'test3',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(O.$$.prototype);
    }
    O.$$.$$metamodel$$={$nm:'O',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
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
        $$$cl2592.initTypeProto(OuterC1,'nesting::OuterC1',$$$cl2592.Basic);
        (function($$outerC1){
            
            //ClassDefinition A at nesting.ceylon (137:4-139:4)
            function A$3524($$a$3524){
                $init$A$3524();
                if ($$a$3524===undefined)$$a$3524=new this.A$3524.$$;
                $$a$3524.$$outer=this;
                return $$a$3524;
            }
            function $init$A$3524(){
                if (A$3524.$$===undefined){
                    $$$cl2592.initTypeProto(A$3524,'nesting::OuterC1.A',$$$cl2592.Basic);
                    OuterC1.A$3524=A$3524;
                    (function($$a$3524){
                        
                        //MethodDefinition tst at nesting.ceylon (138:8-138:55)
                        $$a$3524.tst=function tst(){
                            var $$a$3524=this;
                            return $$$cl2592.String("OuterC1.A.tst()",15);
                        };$$a$3524.tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(A$3524.$$.prototype);
                }
                A$3524.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return A$3524;
            }
            $$outerC1.$init$A$3524=$init$A$3524;
            $init$A$3524();
            $$outerC1.A$3524=A$3524;
            
            //ClassDefinition B at nesting.ceylon (140:4-140:27)
            function B$3525($$b$3525){
                $init$B$3525();
                if ($$b$3525===undefined)$$b$3525=new this.B$3525.$$;
                $$b$3525.$$outer=this;
                $$b$3525.$$outer.A$3524($$b$3525);
                return $$b$3525;
            }
            function $init$B$3525(){
                if (B$3525.$$===undefined){
                    $$$cl2592.initTypeProto(B$3525,'nesting::OuterC1.B',$$outerC1.A$3524);
                    OuterC1.B$3525=B$3525;
                }
                B$3525.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:OuterC1.A$3524},'satisfies':[]};
                return B$3525;
            }
            $$outerC1.$init$B$3525=$init$B$3525;
            $init$B$3525();
            $$outerC1.B$3525=B$3525;
            
            //MethodDefinition tst at nesting.ceylon (141:4-141:42)
            $$outerC1.tst=function tst(){
                var $$outerC1=this;
                return $$outerC1.B$3525().tst();
            };$$outerC1.tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(OuterC1.$$.prototype);
    }
    OuterC1.$$.$$metamodel$$={$nm:'OuterC1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return OuterC1;
}
exports.$init$OuterC1=$init$OuterC1;
$init$OuterC1();

//MethodDefinition outerf at nesting.ceylon (144:0-150:0)
function outerf(){
    
    //ClassDefinition A at nesting.ceylon (145:4-147:4)
    function A$3526($$a$3526){
        $init$A$3526();
        if ($$a$3526===undefined)$$a$3526=new A$3526.$$;
        return $$a$3526;
    }
    function $init$A$3526(){
        if (A$3526.$$===undefined){
            $$$cl2592.initTypeProto(A$3526,'nesting::outerf.A',$$$cl2592.Basic);
            (function($$a$3526){
                
                //MethodDefinition tst at nesting.ceylon (146:8-146:54)
                $$a$3526.tst=function tst(){
                    var $$a$3526=this;
                    return $$$cl2592.String("outerf.A.tst()",14);
                };$$a$3526.tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            })(A$3526.$$.prototype);
        }
        A$3526.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return A$3526;
    }
    $init$A$3526();
    
    //ClassDefinition B at nesting.ceylon (148:4-148:27)
    function B$3527($$b$3527){
        $init$B$3527();
        if ($$b$3527===undefined)$$b$3527=new B$3527.$$;
        A$3526($$b$3527);
        return $$b$3527;
    }
    function $init$B$3527(){
        if (B$3527.$$===undefined){
            $$$cl2592.initTypeProto(B$3527,'nesting::outerf.B',A$3526);
        }
        B$3527.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:A$3526},'satisfies':[]};
        return B$3527;
    }
    $init$B$3527();
    return B$3527().tst();
};outerf.$$metamodel$$={$nm:'outerf',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};//outerf.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.String}};

//ClassDefinition OuterC2 at nesting.ceylon (152:0-160:0)
function OuterC2($$outerC2){
    $init$OuterC2();
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    return $$outerC2;
}
function $init$OuterC2(){
    if (OuterC2.$$===undefined){
        $$$cl2592.initTypeProto(OuterC2,'nesting::OuterC2',$$$cl2592.Basic);
        (function($$outerC2){
            
            //ClassDefinition A at nesting.ceylon (153:4-155:4)
            function A$3528($$a$3528){
                $init$A$3528();
                if ($$a$3528===undefined)$$a$3528=new this.A$3528.$$;
                $$a$3528.$$outer=this;
                return $$a$3528;
            }
            function $init$A$3528(){
                if (A$3528.$$===undefined){
                    $$$cl2592.initTypeProto(A$3528,'nesting::OuterC2.A',$$$cl2592.Basic);
                    OuterC2.A$3528=A$3528;
                    (function($$a$3528){
                        
                        //MethodDefinition tst at nesting.ceylon (154:8-154:55)
                        $$a$3528.tst=function tst(){
                            var $$a$3528=this;
                            return $$$cl2592.String("OuterC2.A.tst()",15);
                        };$$a$3528.tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(A$3528.$$.prototype);
                }
                A$3528.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return A$3528;
            }
            $$outerC2.$init$A$3528=$init$A$3528;
            $init$A$3528();
            $$outerC2.A$3528=A$3528;
            
            //MethodDefinition tst at nesting.ceylon (156:4-159:4)
            $$outerC2.tst=function tst(){
                var $$outerC2=this;
                
                //ClassDefinition B at nesting.ceylon (157:8-157:31)
                function B$3529($$b$3529){
                    $init$B$3529();
                    if ($$b$3529===undefined)$$b$3529=new B$3529.$$;
                    $$outerC2.A$3528($$b$3529);
                    return $$b$3529;
                }
                function $init$B$3529(){
                    if (B$3529.$$===undefined){
                        $$$cl2592.initTypeProto(B$3529,'nesting::OuterC2.tst.B',$$outerC2.A$3528);
                    }
                    B$3529.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:OuterC2.A$3528},'satisfies':[]};
                    return B$3529;
                }
                $init$B$3529();
                return B$3529().tst();
            };$$outerC2.tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(OuterC2.$$.prototype);
    }
    OuterC2.$$.$$metamodel$$={$nm:'OuterC2',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return OuterC2;
}
exports.$init$OuterC2=$init$OuterC2;
$init$OuterC2();

//ClassDefinition NameTest at nesting.ceylon (162:0-178:0)
function NameTest($$nameTest){
    $init$NameTest();
    if ($$nameTest===undefined)$$nameTest=new NameTest.$$;
    
    //AttributeDeclaration x at nesting.ceylon (163:4-163:25)
    $$nameTest.x$3530_=$$$cl2592.String("1",1);
    return $$nameTest;
}
exports.NameTest=NameTest;
function $init$NameTest(){
    if (NameTest.$$===undefined){
        $$$cl2592.initTypeProto(NameTest,'nesting::NameTest',$$$cl2592.Basic);
        (function($$nameTest){
            
            //AttributeDeclaration x at nesting.ceylon (163:4-163:25)
            $$$cl2592.defineAttr($$nameTest,'x',function(){return this.x$3530_;});
            
            //ClassDefinition NameTest at nesting.ceylon (164:4-176:4)
            function NameTest$NameTest($$nameTest$NameTest){
                $init$NameTest$NameTest();
                if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new this.NameTest$NameTest.$$;
                $$nameTest$NameTest.$$outer=this;
                
                //AttributeDeclaration x at nesting.ceylon (165:8-165:29)
                $$nameTest$NameTest.x$3531_=$$$cl2592.String("2",1);
                return $$nameTest$NameTest;
            }
            function $init$NameTest$NameTest(){
                if (NameTest$NameTest.$$===undefined){
                    $$$cl2592.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest',$$$cl2592.Basic);
                    NameTest.NameTest$NameTest=NameTest$NameTest;
                    (function($$nameTest$NameTest){
                        
                        //AttributeDeclaration x at nesting.ceylon (165:8-165:29)
                        $$$cl2592.defineAttr($$nameTest$NameTest,'x',function(){return this.x$3531_;});
                        
                        //MethodDefinition f at nesting.ceylon (166:8-175:8)
                        $$nameTest$NameTest.f=function f(){
                            var $$nameTest$NameTest=this;
                            
                            //ClassDefinition NameTest at nesting.ceylon (167:12-173:12)
                            function NameTest$3532($$nameTest$3532){
                                $init$NameTest$3532();
                                if ($$nameTest$3532===undefined)$$nameTest$3532=new NameTest$3532.$$;
                                
                                //AttributeDeclaration x at nesting.ceylon (168:16-168:37)
                                $$nameTest$3532.x$3533_=$$$cl2592.String("3",1);
                                return $$nameTest$3532;
                            }
                            function $init$NameTest$3532(){
                                if (NameTest$3532.$$===undefined){
                                    $$$cl2592.initTypeProto(NameTest$3532,'nesting::NameTest.NameTest.f.NameTest',$$$cl2592.Basic);
                                    (function($$nameTest$3532){
                                        
                                        //AttributeDeclaration x at nesting.ceylon (168:16-168:37)
                                        $$$cl2592.defineAttr($$nameTest$3532,'x',function(){return this.x$3533_;});
                                        
                                        //ClassDefinition NameTest at nesting.ceylon (169:16-171:16)
                                        function NameTest$NameTest($$nameTest$NameTest){
                                            $init$NameTest$NameTest();
                                            if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new this.NameTest$NameTest.$$;
                                            $$nameTest$NameTest.$$outer=this;
                                            
                                            //AttributeDeclaration x at nesting.ceylon (170:20-170:41)
                                            $$nameTest$NameTest.x$3534_=$$$cl2592.String("4",1);
                                            return $$nameTest$NameTest;
                                        }
                                        function $init$NameTest$NameTest(){
                                            if (NameTest$NameTest.$$===undefined){
                                                $$$cl2592.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest.f.NameTest.NameTest',$$$cl2592.Basic);
                                                NameTest$3532.NameTest$NameTest=NameTest$NameTest;
                                                (function($$nameTest$NameTest){
                                                    
                                                    //AttributeDeclaration x at nesting.ceylon (170:20-170:41)
                                                    $$$cl2592.defineAttr($$nameTest$NameTest,'x',function(){return this.x$3534_;});
                                                })(NameTest$NameTest.$$.prototype);
                                            }
                                            NameTest$NameTest.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                                            return NameTest$NameTest;
                                        }
                                        $$nameTest$3532.$init$NameTest$NameTest=$init$NameTest$NameTest;
                                        $init$NameTest$NameTest();
                                        $$nameTest$3532.NameTest$NameTest=NameTest$NameTest;
                                        
                                        //MethodDefinition f at nesting.ceylon (172:16-172:66)
                                        $$nameTest$3532.f=function f(){
                                            var $$nameTest$3532=this;
                                            return $$nameTest$3532.x.plus($$nameTest$3532.NameTest$NameTest().x);
                                        };$$nameTest$3532.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                    })(NameTest$3532.$$.prototype);
                                }
                                NameTest$3532.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                                return NameTest$3532;
                            }
                            $init$NameTest$3532();
                            return $$nameTest$NameTest.$$outer.x.plus($$nameTest$NameTest.x).plus(NameTest$3532().f());
                        };$$nameTest$NameTest.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(NameTest$NameTest.$$.prototype);
                }
                NameTest$NameTest.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return NameTest$NameTest;
            }
            $$nameTest.$init$NameTest$NameTest=$init$NameTest$NameTest;
            $init$NameTest$NameTest();
            $$nameTest.NameTest$NameTest=NameTest$NameTest;
            
            //MethodDefinition f at nesting.ceylon (177:4-177:52)
            $$nameTest.f=function f(){
                var $$nameTest=this;
                return $$nameTest.NameTest$NameTest().f();
            };$$nameTest.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(NameTest.$$.prototype);
    }
    NameTest.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return NameTest;
}
exports.$init$NameTest=$init$NameTest;
$init$NameTest();

//ObjectDefinition nameTest at nesting.ceylon (180:0-196:0)
function nameTest$3535(){
    var $$nameTest=new nameTest$3535.$$;
    
    //AttributeDeclaration x at nesting.ceylon (181:4-181:25)
    $$nameTest.x$3536_=$$$cl2592.String("1",1);
    
    //ObjectDefinition nameTest at nesting.ceylon (182:4-194:4)
    $$nameTest.nameTest$3537_=$$nameTest.nameTest$3538();
    return $$nameTest;
}
function $init$nameTest$3535(){
    if (nameTest$3535.$$===undefined){
        $$$cl2592.initTypeProto(nameTest$3535,'nesting::nameTest',$$$cl2592.Basic);
    }
    nameTest$3535.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return nameTest$3535;
}
exports.$init$nameTest$3535=$init$nameTest$3535;
$init$nameTest$3535();
(function($$nameTest){
    
    //AttributeDeclaration x at nesting.ceylon (181:4-181:25)
    $$$cl2592.defineAttr($$nameTest,'x',function(){return this.x$3536_;});
    
    //ObjectDefinition nameTest at nesting.ceylon (182:4-194:4)
    function nameTest$3538(){
        var $$nameTest$nameTest=new this.nameTest$3538.$$;
        $$nameTest$nameTest.$$outer=this;
        
        //AttributeDeclaration x at nesting.ceylon (183:8-183:29)
        $$nameTest$nameTest.x$3539_=$$$cl2592.String("2",1);
        return $$nameTest$nameTest;
    }
    function $init$nameTest$3538(){
        if (nameTest$3538.$$===undefined){
            $$$cl2592.initTypeProto(nameTest$3538,'nesting::nameTest.nameTest',$$$cl2592.Basic);
            nameTest$3535.nameTest$3538=nameTest$3538;
        }
        nameTest$3538.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return nameTest$3538;
    }
    $$nameTest.$init$nameTest$3538=$init$nameTest$3538;
    $init$nameTest$3538();
    (function($$nameTest$nameTest){
        
        //AttributeDeclaration x at nesting.ceylon (183:8-183:29)
        $$$cl2592.defineAttr($$nameTest$nameTest,'x',function(){return this.x$3539_;});
        
        //MethodDefinition f at nesting.ceylon (184:8-193:8)
        $$nameTest$nameTest.f=function f(){
            var $$nameTest$nameTest=this;
            
            //ObjectDefinition nameTest at nesting.ceylon (185:12-191:12)
            function nameTest$3540(){
                var $$nameTest$3540=new nameTest$3540.$$;
                
                //AttributeDeclaration x at nesting.ceylon (186:16-186:37)
                $$nameTest$3540.x$3541_=$$$cl2592.String("3",1);
                
                //ObjectDefinition nameTest at nesting.ceylon (187:16-189:16)
                $$nameTest$3540.nameTest$3542_=$$nameTest$3540.nameTest$3543();
                return $$nameTest$3540;
            }
            function $init$nameTest$3540(){
                if (nameTest$3540.$$===undefined){
                    $$$cl2592.initTypeProto(nameTest$3540,'nesting::nameTest.nameTest.f.nameTest',$$$cl2592.Basic);
                }
                nameTest$3540.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return nameTest$3540;
            }
            $init$nameTest$3540();
            (function($$nameTest$3540){
                
                //AttributeDeclaration x at nesting.ceylon (186:16-186:37)
                $$$cl2592.defineAttr($$nameTest$3540,'x',function(){return this.x$3541_;});
                
                //ObjectDefinition nameTest at nesting.ceylon (187:16-189:16)
                function nameTest$3543(){
                    var $$nameTest$nameTest=new this.nameTest$3543.$$;
                    $$nameTest$nameTest.$$outer=this;
                    
                    //AttributeDeclaration x at nesting.ceylon (188:20-188:41)
                    $$nameTest$nameTest.x$3544_=$$$cl2592.String("4",1);
                    return $$nameTest$nameTest;
                }
                function $init$nameTest$3543(){
                    if (nameTest$3543.$$===undefined){
                        $$$cl2592.initTypeProto(nameTest$3543,'nesting::nameTest.nameTest.f.nameTest.nameTest',$$$cl2592.Basic);
                        nameTest$3540.nameTest$3543=nameTest$3543;
                    }
                    nameTest$3543.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                    return nameTest$3543;
                }
                $$nameTest$3540.$init$nameTest$3543=$init$nameTest$3543;
                $init$nameTest$3543();
                (function($$nameTest$nameTest){
                    
                    //AttributeDeclaration x at nesting.ceylon (188:20-188:41)
                    $$$cl2592.defineAttr($$nameTest$nameTest,'x',function(){return this.x$3544_;});
                })(nameTest$3543.$$.prototype);
                $$$cl2592.defineAttr($$nameTest$3540,'nameTest',function(){return this.nameTest$3542_;});
                $$nameTest$3540.nameTest$3543=nameTest$3543;
                
                //MethodDefinition f at nesting.ceylon (190:16-190:64)
                $$nameTest$3540.f=function f(){
                    var $$nameTest$3540=this;
                    return $$nameTest$3540.x.plus($$nameTest$3540.nameTest.x);
                };$$nameTest$3540.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            })(nameTest$3540.$$.prototype);
            var nameTest$3545=nameTest$3540();
            var getNameTest$3545=function(){
                return nameTest$3545;
            }
            return $$nameTest$nameTest.$$outer.x.plus($$nameTest$nameTest.x).plus(getNameTest$3545().f());
        };$$nameTest$nameTest.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
    })(nameTest$3538.$$.prototype);
    $$$cl2592.defineAttr($$nameTest,'nameTest',function(){return this.nameTest$3537_;});
    $$nameTest.nameTest$3538=nameTest$3538;
    
    //MethodDefinition f at nesting.ceylon (195:4-195:50)
    $$nameTest.f=function f(){
        var $$nameTest=this;
        return $$nameTest.nameTest.f();
    };$$nameTest.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
})(nameTest$3535.$$.prototype);
var nameTest$3546=nameTest$3535();
var getNameTest=function(){
    return nameTest$3546;
}
exports.getNameTest=getNameTest;

//ClassDefinition C1 at nesting.ceylon (198:0-209:0)
function C1($$c1){
    $init$C1();
    if ($$c1===undefined)$$c1=new C1.$$;
    
    //AttributeDeclaration x at nesting.ceylon (199:4-199:33)
    $$c1.x$3547_=$$$cl2592.String("1",1);
    return $$c1;
}
exports.C1=C1;
function $init$C1(){
    if (C1.$$===undefined){
        $$$cl2592.initTypeProto(C1,'nesting::C1',$$$cl2592.Basic);
        (function($$c1){
            
            //AttributeDeclaration x at nesting.ceylon (199:4-199:33)
            $$$cl2592.defineAttr($$c1,'x',function(){return this.x$3547_;});
            
            //ClassDefinition C1 at nesting.ceylon (200:4-202:4)
            function C1$C1($$c1$C1){
                $init$C1$C1();
                if ($$c1$C1===undefined)$$c1$C1=new this.C1$C1.$$;
                $$c1$C1.$$outer=this;
                
                //AttributeDeclaration x at nesting.ceylon (201:8-201:38)
                $$c1$C1.x$3548_=$$$cl2592.String("11",2);
                return $$c1$C1;
            }
            function $init$C1$C1(){
                if (C1$C1.$$===undefined){
                    $$$cl2592.initTypeProto(C1$C1,'nesting::C1.C1',$$$cl2592.Basic);
                    C1.C1$C1=C1$C1;
                    (function($$c1$C1){
                        
                        //AttributeDeclaration x at nesting.ceylon (201:8-201:38)
                        $$$cl2592.defineAttr($$c1$C1,'x',function(){return this.x$3548_;});
                    })(C1$C1.$$.prototype);
                }
                C1$C1.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return C1$C1;
            }
            $$c1.$init$C1$C1=$init$C1$C1;
            $init$C1$C1();
            $$c1.C1$C1=C1$C1;
            
            //ClassDefinition C3 at nesting.ceylon (203:4-208:4)
            function C3$C1($$c3$C1){
                $init$C3$C1();
                if ($$c3$C1===undefined)$$c3$C1=new this.C3$C1.$$;
                $$c3$C1.$$outer=this;
                $$c3$C1.$$outer.C1$C1($$c3$C1);
                
                //AttributeDeclaration x at nesting.ceylon (204:8-204:45)
                $$c3$C1.x$3549_=$$$cl2592.String("13",2);
                return $$c3$C1;
            }
            function $init$C3$C1(){
                if (C3$C1.$$===undefined){
                    $$$cl2592.initTypeProto(C3$C1,'nesting::C1.C3',$$c1.C1$C1);
                    C1.C3$C1=C3$C1;
                    (function($$c3$C1){
                        
                        //AttributeDeclaration x at nesting.ceylon (204:8-204:45)
                        $$$cl2592.defineAttr($$c3$C1,'x',function(){return this.x$3549_;});
                        
                        //MethodDefinition f at nesting.ceylon (205:8-207:8)
                        $$c3$C1.f=function f(){
                            var $$c3$C1=this;
                            return $$$cl2592.StringBuilder().appendAll([$$c3$C1.$$outer.x.string,$$$cl2592.String("-",1),$$$cl2592.attrGetter($$c3$C1.getT$all()['nesting::C1.C1'],'x').call(this).string,$$$cl2592.String("-",1),$$c3$C1.$$outer.C1$C1().x.string,$$$cl2592.String("-",1),$$c3$C1.x.string,$$$cl2592.String("-",1),$$c3$C1.$$outer.C3$C1().x.string]).string;
                        };$$c3$C1.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(C3$C1.$$.prototype);
                }
                C3$C1.$$.$$metamodel$$={$nm:'C3',$mt:'cls','super':{t:C1.C1$C1},'satisfies':[]};
                return C3$C1;
            }
            $$c1.$init$C3$C1=$init$C3$C1;
            $init$C3$C1();
            $$c1.C3$C1=C3$C1;
        })(C1.$$.prototype);
    }
    C1.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
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
    $$c2.x$3550_=$$$cl2592.String("2",1);
    return $$c2;
}
exports.C2=C2;
function $init$C2(){
    if (C2.$$===undefined){
        $$$cl2592.initTypeProto(C2,'nesting::C2',C1);
        (function($$c2){
            
            //AttributeDeclaration x at nesting.ceylon (211:4-211:32)
            $$$cl2592.defineAttr($$c2,'x',function(){return this.x$3550_;});
            
            //ClassDefinition C2 at nesting.ceylon (212:4-220:4)
            function C2$C2($$c2$C2){
                $init$C2$C2();
                if ($$c2$C2===undefined)$$c2$C2=new this.C2$C2.$$;
                $$c2$C2.$$outer=this;
                $$c2$C2.$$outer.getT$all()['nesting::C1'].$$.prototype.C1$C1.call(this,$$c2$C2);
                
                //AttributeDeclaration x at nesting.ceylon (213:8-213:37)
                $$c2$C2.x$3551_=$$$cl2592.String("22",2);
                return $$c2$C2;
            }
            function $init$C2$C2(){
                if (C2$C2.$$===undefined){
                    $$$cl2592.initTypeProto(C2$C2,'nesting::C2.C2',$$c2.getT$all()['nesting::C1'].$$.prototype.C1$C1);
                    C2.C2$C2=C2$C2;
                    (function($$c2$C2){
                        
                        //AttributeDeclaration x at nesting.ceylon (213:8-213:37)
                        $$$cl2592.defineAttr($$c2$C2,'x',function(){return this.x$3551_;});
                        
                        //ClassDefinition C2 at nesting.ceylon (214:8-216:8)
                        function C2$C2$C2($$c2$C2$C2){
                            $init$C2$C2$C2();
                            if ($$c2$C2$C2===undefined)$$c2$C2$C2=new this.C2$C2$C2.$$;
                            $$c2$C2$C2.$$outer=this;
                            $$c2$C2$C2.$$outer.$$outer.C3$C1($$c2$C2$C2);
                            
                            //AttributeDeclaration x at nesting.ceylon (215:12-215:42)
                            $$c2$C2$C2.x$3552_=$$$cl2592.String("222",3);
                            return $$c2$C2$C2;
                        }
                        function $init$C2$C2$C2(){
                            if (C2$C2$C2.$$===undefined){
                                $$$cl2592.initTypeProto(C2$C2$C2,'nesting::C2.C2.C2',$$c2.C3$C1);
                                C2.C2$C2.C2$C2$C2=C2$C2$C2;
                                (function($$c2$C2$C2){
                                    
                                    //AttributeDeclaration x at nesting.ceylon (215:12-215:42)
                                    $$$cl2592.defineAttr($$c2$C2$C2,'x',function(){return this.x$3552_;});
                                })(C2$C2$C2.$$.prototype);
                            }
                            C2$C2$C2.$$.$$metamodel$$={$nm:'C2',$mt:'cls','super':{t:C1.C3$C1},'satisfies':[]};
                            return C2$C2$C2;
                        }
                        $$c2$C2.$init$C2$C2$C2=$init$C2$C2$C2;
                        $init$C2$C2$C2();
                        $$c2$C2.C2$C2$C2=C2$C2$C2;
                        
                        //MethodDefinition f at nesting.ceylon (217:8-219:8)
                        $$c2$C2.f=function f(){
                            var $$c2$C2=this;
                            return $$$cl2592.StringBuilder().appendAll([$$c2$C2.$$outer.x.string,$$$cl2592.String("-",1),$$c2$C2.$$outer.C1$C1().x.string,$$$cl2592.String("-",1),$$c2$C2.x.string,$$$cl2592.String("-",1),$$$cl2592.attrGetter($$c2$C2.getT$all()['nesting::C1.C1'],'x').call(this).string,$$$cl2592.String("-",1),$$c2$C2.$$outer.C3$C1().x.string,$$$cl2592.String("-",1),$$c2$C2.C2$C2$C2().x.string,$$$cl2592.String("-",1),$$c2$C2.C2$C2$C2().f().string,$$$cl2592.String("-",1),$$c2$C2.$$outer.C3$C1().f().string]).string;
                        };$$c2$C2.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(C2$C2.$$.prototype);
                }
                C2$C2.$$.$$metamodel$$={$nm:'C2',$mt:'cls','super':{t:C1.C1$C1},'satisfies':[]};
                return C2$C2;
            }
            $$c2.$init$C2$C2=$init$C2$C2;
            $init$C2$C2();
            $$c2.C2$C2=C2$C2;
        })(C2.$$.prototype);
    }
    C2.$$.$$metamodel$$={$nm:'C2',$mt:'cls','super':{t:C1},'satisfies':[]};
    return C2;
}
exports.$init$C2=$init$C2;
$init$C2();

//MethodDefinition test at nesting.ceylon (223:0-253:0)
function test(){
    outr($$$cl2592.String("Hello",5));
    $$$c2593.check(Holder($$$cl2592.String("ok",2)).get().string.equals($$$cl2592.String("ok",2)),$$$cl2592.String("holder(ok)",10));
    $$$c2593.check(Holder($$$cl2592.String("ok",2)).string.equals($$$cl2592.String("ok",2)),$$$cl2592.String("holder.string",13));
    $$$c2593.check(Wrapper().get().string.equals($$$cl2592.String("100",3)),$$$cl2592.String("wrapper 1",9));
    $$$c2593.check(Wrapper().string.equals($$$cl2592.String("100",3)),$$$cl2592.String("wrapper 2",9));
    $$$c2593.check(Unwrapper().get().string.equals($$$cl2592.String("23.56",5)),$$$cl2592.String("unwrapper 1",11));
    $$$c2593.check(Unwrapper().o.string.equals($$$cl2592.String("23.56",5)),$$$cl2592.String("unwrapper 2",11));
    $$$c2593.check(Unwrapper().string.equals($$$cl2592.String("23.56",5)),$$$cl2592.String("unwrapper 3",11));
    $$$c2593.check($$$cl2592.isOfType(producer(),{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Integer}}}),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("function 1 is ",14),$$$cl2592.className($$$cl2592.$JsCallable(producer(),/*Callable from Invocation +  [InvocationExpression] <Object()> (232:73-232:82)
|  +  [BaseMemberExpression] <Object()()> (232:73-232:80) => Method[producer:Object()]
|  |  + producer [Identifier] (232:73-232:80)
|  |  +  [InferredTypeArguments]
|  + () [PositionalArgumentList] (232:81-232:82)
*/[/*INVOKE callable params 1*/],{Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Object}})).string]).string);
    $$$c2593.check($$$cl2592.isOfType(producer()(),{t:$$$cl2592.Integer}),$$$cl2592.String("function 2",10));
    $$$c2593.check((123).equals(producer()()),$$$cl2592.String("function 3",10));
    $$$c2593.check($$$cl2592.String("something",9).equals(returner($$$cl2592.String("something",9))()),$$$cl2592.String("function 4",10));
    $$$c2593.check(A().B$A().C$B$A().foobar().equals($$$cl2592.String("foo",3)),$$$cl2592.String("foobar",6));
    $$$c2593.check(A().B$A().C$B$A().quxx().equals($$$cl2592.String("qux",3)),$$$cl2592.String("quxx",4));
    $$$c2593.check(A().baz().equals($$$cl2592.String("foo",3)),$$$cl2592.String("baz",3));
    $$$c2593.check(O().test1().equals($$$cl2592.String("hello",5)),$$$cl2592.String("method instantiating inner class",32));
    $$$c2593.check(O().test2().equals($$$cl2592.String("hello",5)),$$$cl2592.String("method accessing inner object",29));
    $$$c2593.check(O().test3().equals($$$cl2592.String("hello",5)),$$$cl2592.String("method deriving inner interface",31));
    $$$c2593.check(OuterC1().tst().equals($$$cl2592.String("OuterC1.A.tst()",15)),$$$cl2592.String("",0));
    $$$c2593.check(outerf().equals($$$cl2592.String("outerf.A.tst()",14)),$$$cl2592.String("",0));
    $$$c2593.check(OuterC2().tst().equals($$$cl2592.String("OuterC2.A.tst()",15)),$$$cl2592.String("",0));
    Outer($$$cl2592.String("Hello",5));
    $$$c2593.check(NameTest().f().equals($$$cl2592.String("1234",4)),$$$cl2592.String("Nested class with same name",27));
    $$$c2593.check(getNameTest().f().equals($$$cl2592.String("1234",4)),$$$cl2592.String("Nested object with same name",28));
    $$$c2593.check(C1().C3$C1().f().equals($$$cl2592.String("1-11-11-13-13",13)),$$$cl2592.String("Several nested classes with same name (1)",41));
    $$$c2593.check(C2().C2$C2().f().equals($$$cl2592.String("2-11-22-11-13-222-2-11-11-222-13-2-11-11-13-13",46)),$$$cl2592.String("Several nested classes with same name (2)",41));
    testRefinement();
    testRefinement2();
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//ClassDefinition X at refinement.ceylon (4:0-17:0)
function X($$x){
    $init$X();
    if ($$x===undefined)$$x=new X.$$;
    return $$x;
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl2592.initTypeProto(X,'nesting::X',$$$cl2592.Basic);
        (function($$x){
            
            //ClassDefinition RefineTest1 at refinement.ceylon (5:4-16:4)
            function RefineTest1$X($$refineTest1$X){
                $init$RefineTest1$X();
                if ($$refineTest1$X===undefined)$$refineTest1$X=new this.RefineTest1$X.$$;
                $$refineTest1$X.$$outer=this;
                return $$refineTest1$X;
            }
            function $init$RefineTest1$X(){
                if (RefineTest1$X.$$===undefined){
                    $$$cl2592.initTypeProto(RefineTest1$X,'nesting::X.RefineTest1',$$$cl2592.Basic);
                    X.RefineTest1$X=RefineTest1$X;
                    (function($$refineTest1$X){
                        
                        //ClassDefinition Inner at refinement.ceylon (6:8-12:8)
                        function Inner$RefineTest1$X($$inner$RefineTest1$X){
                            $init$Inner$RefineTest1$X();
                            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new this.Inner$RefineTest1$X.$$;
                            $$inner$RefineTest1$X.$$outer=this;
                            
                            //AttributeDeclaration origin at refinement.ceylon (7:12-7:54)
                            $$inner$RefineTest1$X.origin$3553_=$$$cl2592.String("RefineTest1.Inner",17);
                            return $$inner$RefineTest1$X;
                        }
                        function $init$Inner$RefineTest1$X(){
                            if (Inner$RefineTest1$X.$$===undefined){
                                $$$cl2592.initTypeProto(Inner$RefineTest1$X,'nesting::X.RefineTest1.Inner',$$$cl2592.Basic);
                                X.RefineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
                                (function($$inner$RefineTest1$X){
                                    
                                    //AttributeDeclaration origin at refinement.ceylon (7:12-7:54)
                                    $$$cl2592.defineAttr($$inner$RefineTest1$X,'origin',function(){return this.origin$3553_;});
                                    
                                    //MethodDefinition x at refinement.ceylon (8:12-10:12)
                                    $$inner$RefineTest1$X.x=function x(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl2592.String("x and ",6).plus($$inner$RefineTest1$X.y());
                                    };$$inner$RefineTest1$X.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                })(Inner$RefineTest1$X.$$.prototype);
                            }
                            Inner$RefineTest1$X.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                            return Inner$RefineTest1$X;
                        }
                        $$refineTest1$X.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
                        $init$Inner$RefineTest1$X();
                        $$refineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
                        
                        //MethodDefinition outerx at refinement.ceylon (13:8-15:8)
                        $$refineTest1$X.outerx=function outerx(){
                            var $$refineTest1$X=this;
                            return $$refineTest1$X.Inner$RefineTest1$X().x();
                        };$$refineTest1$X.outerx.$$metamodel$$={$nm:'outerx',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(RefineTest1$X.$$.prototype);
                }
                RefineTest1$X.$$.$$metamodel$$={$nm:'RefineTest1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return RefineTest1$X;
            }
            $$x.$init$RefineTest1$X=$init$RefineTest1$X;
            $init$RefineTest1$X();
            $$x.RefineTest1$X=RefineTest1$X;
        })(X.$$.prototype);
    }
    X.$$.$$metamodel$$={$nm:'X',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
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
        $$$cl2592.initTypeProto(RefineTest2,'nesting::RefineTest2',$$$cl2592.Basic);
        (function($$refineTest2){
            
            //ClassDefinition Inner at refinement.ceylon (21:4-23:4)
            function Inner$RefineTest2($$inner$RefineTest2){
                $init$Inner$RefineTest2();
                if ($$inner$RefineTest2===undefined)$$inner$RefineTest2=new this.Inner$RefineTest2.$$;
                $$inner$RefineTest2.$$outer=this;
                return $$inner$RefineTest2;
            }
            function $init$Inner$RefineTest2(){
                if (Inner$RefineTest2.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$RefineTest2,'nesting::RefineTest2.Inner',$$$cl2592.Basic);
                    RefineTest2.Inner$RefineTest2=Inner$RefineTest2;
                    (function($$inner$RefineTest2){
                        
                        //MethodDefinition hello at refinement.ceylon (22:8-22:71)
                        $$inner$RefineTest2.hello=function hello(){
                            var $$inner$RefineTest2=this;
                            return $$$cl2592.String("hello from RefineTest2.Inner",28);
                        };$$inner$RefineTest2.hello.$$metamodel$$={$nm:'hello',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(Inner$RefineTest2.$$.prototype);
                }
                Inner$RefineTest2.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return Inner$RefineTest2;
            }
            $$refineTest2.$init$Inner$RefineTest2=$init$Inner$RefineTest2;
            $init$Inner$RefineTest2();
            $$refineTest2.Inner$RefineTest2=Inner$RefineTest2;
        })(RefineTest2.$$.prototype);
    }
    RefineTest2.$$.$$metamodel$$={$nm:'RefineTest2',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
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
        $$$cl2592.initTypeProto(RefineTest3,'nesting::RefineTest3',$$$cl2592.Basic);
        (function($$refineTest3){
            
            //ClassDefinition Inner at refinement.ceylon (28:4-32:4)
            function Inner$RefineTest3($$inner$RefineTest3){
                $init$Inner$RefineTest3();
                if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new this.Inner$RefineTest3.$$;
                $$inner$RefineTest3.$$outer=this;
                return $$inner$RefineTest3;
            }
            function $init$Inner$RefineTest3(){
                if (Inner$RefineTest3.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$RefineTest3,'nesting::RefineTest3.Inner',$$$cl2592.Basic);
                    RefineTest3.Inner$RefineTest3=Inner$RefineTest3;
                    (function($$inner$RefineTest3){
                        
                        //MethodDefinition x at refinement.ceylon (29:8-31:8)
                        $$inner$RefineTest3.x=function x(){
                            var $$inner$RefineTest3=this;
                            return $$$cl2592.String("x",1);
                        };$$inner$RefineTest3.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(Inner$RefineTest3.$$.prototype);
                }
                Inner$RefineTest3.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return Inner$RefineTest3;
            }
            $$refineTest3.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
            $init$Inner$RefineTest3();
            $$refineTest3.Inner$RefineTest3=Inner$RefineTest3;
        })(RefineTest3.$$.prototype);
    }
    RefineTest3.$$.$$metamodel$$={$nm:'RefineTest3',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
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
        $$$cl2592.initTypeProto(Y,'nesting::Y',X);
        (function($$y){
            
            //ClassDefinition SubRef1 at refinement.ceylon (36:4-46:4)
            function SubRef1$Y($$subRef1$Y){
                $init$SubRef1$Y();
                if ($$subRef1$Y===undefined)$$subRef1$Y=new this.SubRef1$Y.$$;
                $$subRef1$Y.$$outer=this;
                $$subRef1$Y.$$outer.RefineTest1$X($$subRef1$Y);
                return $$subRef1$Y;
            }
            function $init$SubRef1$Y(){
                if (SubRef1$Y.$$===undefined){
                    $$$cl2592.initTypeProto(SubRef1$Y,'nesting::Y.SubRef1',$$y.RefineTest1$X);
                    Y.SubRef1$Y=SubRef1$Y;
                    (function($$subRef1$Y){
                        
                        //ClassDefinition Inner at refinement.ceylon (37:6-45:6)
                        function Inner$RefineTest1$X($$inner$RefineTest1$X){
                            $init$Inner$RefineTest1$X();
                            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new this.Inner$RefineTest1$X.$$;
                            $$inner$RefineTest1$X.$$outer=this;
                            $$inner$RefineTest1$X.$$outer.getT$all()['nesting::X.RefineTest1'].$$.prototype.Inner$RefineTest1$X.call(this,$$inner$RefineTest1$X);
                            
                            //AttributeDeclaration suborigin at refinement.ceylon (38:10-38:51)
                            $$inner$RefineTest1$X.suborigin$3554_=$$$cl2592.String("SubRef1.Inner",13);
                            return $$inner$RefineTest1$X;
                        }
                        function $init$Inner$RefineTest1$X(){
                            if (Inner$RefineTest1$X.$$===undefined){
                                $$$cl2592.initTypeProto(Inner$RefineTest1$X,'nesting::Y.SubRef1.Inner',$$subRef1$Y.getT$all()['nesting::X.RefineTest1'].$$.prototype.Inner$RefineTest1$X);
                                Y.SubRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
                                (function($$inner$RefineTest1$X){
                                    
                                    //AttributeDeclaration suborigin at refinement.ceylon (38:10-38:51)
                                    $$$cl2592.defineAttr($$inner$RefineTest1$X,'suborigin',function(){return this.suborigin$3554_;});
                                    
                                    //MethodDefinition x at refinement.ceylon (39:10-41:10)
                                    $$inner$RefineTest1$X.x=function x(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl2592.String("REFINED ",8).plus($$inner$RefineTest1$X.getT$all()['nesting::X.RefineTest1.Inner'].$$.prototype.x.call(this));
                                    };$$inner$RefineTest1$X.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                    //MethodDefinition y at refinement.ceylon (42:10-44:10)
                                    $$inner$RefineTest1$X.y=function y(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl2592.String("y",1);
                                    };$$inner$RefineTest1$X.y.$$metamodel$$={$nm:'y',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                })(Inner$RefineTest1$X.$$.prototype);
                            }
                            Inner$RefineTest1$X.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:X.RefineTest1$X.Inner$RefineTest1$X},'satisfies':[]};
                            return Inner$RefineTest1$X;
                        }
                        $$subRef1$Y.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
                        $init$Inner$RefineTest1$X();
                        $$subRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
                    })(SubRef1$Y.$$.prototype);
                }
                SubRef1$Y.$$.$$metamodel$$={$nm:'SubRef1',$mt:'cls','super':{t:X.RefineTest1$X},'satisfies':[]};
                return SubRef1$Y;
            }
            $$y.$init$SubRef1$Y=$init$SubRef1$Y;
            $init$SubRef1$Y();
            $$y.SubRef1$Y=SubRef1$Y;
        })(Y.$$.prototype);
    }
    Y.$$.$$metamodel$$={$nm:'Y',$mt:'cls','super':{t:X},'satisfies':[]};
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
        $$$cl2592.initTypeProto(SubRef2,'nesting::SubRef2',RefineTest2);
        (function($$subRef2){
            
            //MethodDefinition x at refinement.ceylon (50:4-52:4)
            $$subRef2.x=function x(){
                var $$subRef2=this;
                return $$subRef2.Inner$RefineTest2().hello();
            };$$subRef2.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(SubRef2.$$.prototype);
    }
    SubRef2.$$.$$metamodel$$={$nm:'SubRef2',$mt:'cls','super':{t:RefineTest2},'satisfies':[]};
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
        $$$cl2592.initTypeProto(SubRef3,'nesting::SubRef3',RefineTest3);
        (function($$subRef3){
            
            //MethodDefinition x at refinement.ceylon (56:4-58:4)
            $$subRef3.x=function x(){
                var $$subRef3=this;
                return $$subRef3.Inner$RefineTest3().x();
            };$$subRef3.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(SubRef3.$$.prototype);
    }
    SubRef3.$$.$$metamodel$$={$nm:'SubRef3',$mt:'cls','super':{t:RefineTest3},'satisfies':[]};
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
        $$$cl2592.initTypeProto(SubRef31,'nesting::SubRef31',SubRef3);
        (function($$subRef31){
            
            //ClassDefinition Inner at refinement.ceylon (61:4-63:4)
            function Inner$RefineTest3($$inner$RefineTest3){
                $init$Inner$RefineTest3();
                if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new this.Inner$RefineTest3.$$;
                $$inner$RefineTest3.$$outer=this;
                $$inner$RefineTest3.$$outer.getT$all()['nesting::RefineTest3'].$$.prototype.Inner$RefineTest3.call(this,$$inner$RefineTest3);
                return $$inner$RefineTest3;
            }
            function $init$Inner$RefineTest3(){
                if (Inner$RefineTest3.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$RefineTest3,'nesting::SubRef31.Inner',$$subRef31.getT$all()['nesting::RefineTest3'].$$.prototype.Inner$RefineTest3);
                    SubRef31.Inner$RefineTest3=Inner$RefineTest3;
                    (function($$inner$RefineTest3){
                        
                        //MethodDefinition x at refinement.ceylon (62:8-62:51)
                        $$inner$RefineTest3.x=function x(){
                            var $$inner$RefineTest3=this;
                            return $$$cl2592.String("equis",5);
                        };$$inner$RefineTest3.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(Inner$RefineTest3.$$.prototype);
                }
                Inner$RefineTest3.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:RefineTest3.Inner$RefineTest3},'satisfies':[]};
                return Inner$RefineTest3;
            }
            $$subRef31.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
            $init$Inner$RefineTest3();
            $$subRef31.Inner$RefineTest3=Inner$RefineTest3;
        })(SubRef31.$$.prototype);
    }
    SubRef31.$$.$$metamodel$$={$nm:'SubRef31',$mt:'cls','super':{t:SubRef3},'satisfies':[]};
    return SubRef31;
}
exports.$init$SubRef31=$init$SubRef31;
$init$SubRef31();

//MethodDefinition testRefinement at refinement.ceylon (66:0-76:0)
function testRefinement(){
    
    //AttributeDeclaration c1 at refinement.ceylon (67:4-67:36)
    var c1$3555=Y().SubRef1$Y().Inner$RefineTest1$X();
    $$$c2593.check($$$cl2592.className(c1$3555).equals($$$cl2592.String("nesting::Y.SubRef1.Inner",24)),$$$cl2592.String("classname is ",13).plus($$$cl2592.className(c1$3555)));
    $$$c2593.check(c1$3555.origin.equals($$$cl2592.String("RefineTest1.Inner",17)),$$$cl2592.String("refinement [1]",14));
    $$$c2593.check(c1$3555.suborigin.equals($$$cl2592.String("SubRef1.Inner",13)),$$$cl2592.String("refinement [2]",14));
    $$$c2593.check(c1$3555.x().equals($$$cl2592.String("REFINED x and y",15)),$$$cl2592.String("refinement [3]",14));
    $$$c2593.check(c1$3555.x().equals(Y().SubRef1$Y().outerx()),$$$cl2592.String("refinement [4]",14));
    $$$c2593.check(SubRef2().x().equals($$$cl2592.String("hello from RefineTest2.Inner",28)),$$$cl2592.String("refinement [5]",14));
    $$$c2593.check(SubRef3().x().equals($$$cl2592.String("x",1)),$$$cl2592.String("refinement [6]",14));
    $$$c2593.check(SubRef31().x().equals($$$cl2592.String("equis",5)),$$$cl2592.String("refinement [7]",14));
};testRefinement.$$metamodel$$={$nm:'testRefinement',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testRefinement.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//ClassDefinition X2 at refinement2.ceylon (4:0-18:0)
function X2(a$3556, $$x2){
    $init$X2();
    if ($$x2===undefined)$$x2=new X2.$$;
    $$x2.a$3556=a$3556;
    return $$x2;
}
exports.X2=X2;
function $init$X2(){
    if (X2.$$===undefined){
        $$$cl2592.initTypeProto(X2,'nesting::X2',$$$cl2592.Basic);
        (function($$x2){
            
            //ClassDefinition RefineTest1 at refinement2.ceylon (5:4-17:4)
            function RefineTest1$X2(b$3557, $$refineTest1$X2){
                $init$RefineTest1$X2();
                if ($$refineTest1$X2===undefined)$$refineTest1$X2=new this.RefineTest1$X2.$$;
                $$refineTest1$X2.$$outer=this;
                $$refineTest1$X2.b$3557=b$3557;
                return $$refineTest1$X2;
            }
            function $init$RefineTest1$X2(){
                if (RefineTest1$X2.$$===undefined){
                    $$$cl2592.initTypeProto(RefineTest1$X2,'nesting::X2.RefineTest1',$$$cl2592.Basic);
                    X2.RefineTest1$X2=RefineTest1$X2;
                    (function($$refineTest1$X2){
                        
                        //ClassDefinition Inner at refinement2.ceylon (6:8-13:8)
                        function Inner$RefineTest1$X2(c$3558, $$targs$$,$$inner$RefineTest1$X2){
                            $init$Inner$RefineTest1$X2();
                            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new this.Inner$RefineTest1$X2.$$;
                            $$$cl2592.set_type_args($$inner$RefineTest1$X2,$$targs$$);
                            $$inner$RefineTest1$X2.$$outer=this;
                            $$inner$RefineTest1$X2.c$3558=c$3558;
                            
                            //AttributeDeclaration origin at refinement2.ceylon (8:12-8:62)
                            $$inner$RefineTest1$X2.origin$3559_=$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("RefineTest1.Inner (",19),$$inner$RefineTest1$X2.c$3558.string,$$$cl2592.String(")",1)]).string;
                            return $$inner$RefineTest1$X2;
                        }
                        function $init$Inner$RefineTest1$X2(){
                            if (Inner$RefineTest1$X2.$$===undefined){
                                $$$cl2592.initTypeProto(Inner$RefineTest1$X2,'nesting::X2.RefineTest1.Inner',$$$cl2592.Basic);
                                X2.RefineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                                (function($$inner$RefineTest1$X2){
                                    
                                    //AttributeDeclaration origin at refinement2.ceylon (8:12-8:62)
                                    $$$cl2592.defineAttr($$inner$RefineTest1$X2,'origin',function(){return this.origin$3559_;});
                                    
                                    //MethodDefinition x at refinement2.ceylon (9:12-11:12)
                                    $$inner$RefineTest1$X2.x=function x(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("x and ",6),$$inner$RefineTest1$X2.y().string,$$$cl2592.String(" and a:",7),$$inner$RefineTest1$X2.$$outer.$$outer.a$3556.string,$$$cl2592.String(", b:",4),$$inner$RefineTest1$X2.$$outer.b$3557.string,$$$cl2592.String(", c:",4),$$inner$RefineTest1$X2.c$3558.string,$$$cl2592.String(".",1)]).string;
                                    };$$inner$RefineTest1$X2.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                })(Inner$RefineTest1$X2.$$.prototype);
                            }
                            Inner$RefineTest1$X2.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},$tp:{Element:{'satisfies':[{t:$$$cl2592.Object}]}},'satisfies':[]};
                            return Inner$RefineTest1$X2;
                        }
                        $$refineTest1$X2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
                        $init$Inner$RefineTest1$X2();
                        $$refineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                        
                        //MethodDefinition outerx at refinement2.ceylon (14:8-16:8)
                        $$refineTest1$X2.outerx=function outerx(){
                            var $$refineTest1$X2=this;
                            return $$refineTest1$X2.Inner$RefineTest1$X2($$refineTest1$X2.$$outer.a$3556.uppercased,{Element:{t:$$$cl2592.String}}).x();
                        };$$refineTest1$X2.outerx.$$metamodel$$={$nm:'outerx',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(RefineTest1$X2.$$.prototype);
                }
                RefineTest1$X2.$$.$$metamodel$$={$nm:'RefineTest1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return RefineTest1$X2;
            }
            $$x2.$init$RefineTest1$X2=$init$RefineTest1$X2;
            $init$RefineTest1$X2();
            $$x2.RefineTest1$X2=RefineTest1$X2;
        })(X2.$$.prototype);
    }
    X2.$$.$$metamodel$$={$nm:'X2',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return X2;
}
exports.$init$X2=$init$X2;
$init$X2();

//ClassDefinition RefineTest4 at refinement2.ceylon (21:0-25:0)
function RefineTest4(d$3560, $$refineTest4){
    $init$RefineTest4();
    if ($$refineTest4===undefined)$$refineTest4=new RefineTest4.$$;
    return $$refineTest4;
}
exports.RefineTest4=RefineTest4;
function $init$RefineTest4(){
    if (RefineTest4.$$===undefined){
        $$$cl2592.initTypeProto(RefineTest4,'nesting::RefineTest4',$$$cl2592.Basic);
        (function($$refineTest4){
            
            //ClassDefinition Inner at refinement2.ceylon (22:4-24:4)
            function Inner$RefineTest4(e$3561, $$inner$RefineTest4){
                $init$Inner$RefineTest4();
                if ($$inner$RefineTest4===undefined)$$inner$RefineTest4=new this.Inner$RefineTest4.$$;
                $$inner$RefineTest4.$$outer=this;
                $$inner$RefineTest4.e$3561=e$3561;
                return $$inner$RefineTest4;
            }
            function $init$Inner$RefineTest4(){
                if (Inner$RefineTest4.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$RefineTest4,'nesting::RefineTest4.Inner',$$$cl2592.Basic);
                    RefineTest4.Inner$RefineTest4=Inner$RefineTest4;
                    (function($$inner$RefineTest4){
                        
                        //MethodDefinition hello at refinement2.ceylon (23:8-23:83)
                        $$inner$RefineTest4.hello=function hello(){
                            var $$inner$RefineTest4=this;
                            return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("hello from RefineTest2.Inner with ",34),$$inner$RefineTest4.e$3561.string,$$$cl2592.String(".",1)]).string;
                        };$$inner$RefineTest4.hello.$$metamodel$$={$nm:'hello',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(Inner$RefineTest4.$$.prototype);
                }
                Inner$RefineTest4.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return Inner$RefineTest4;
            }
            $$refineTest4.$init$Inner$RefineTest4=$init$Inner$RefineTest4;
            $init$Inner$RefineTest4();
            $$refineTest4.Inner$RefineTest4=Inner$RefineTest4;
        })(RefineTest4.$$.prototype);
    }
    RefineTest4.$$.$$metamodel$$={$nm:'RefineTest4',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return RefineTest4;
}
exports.$init$RefineTest4=$init$RefineTest4;
$init$RefineTest4();

//ClassDefinition RefineTest5 at refinement2.ceylon (28:0-34:0)
function RefineTest5(f$3562, $$refineTest5){
    $init$RefineTest5();
    if ($$refineTest5===undefined)$$refineTest5=new RefineTest5.$$;
    $$refineTest5.f$3562=f$3562;
    return $$refineTest5;
}
exports.RefineTest5=RefineTest5;
function $init$RefineTest5(){
    if (RefineTest5.$$===undefined){
        $$$cl2592.initTypeProto(RefineTest5,'nesting::RefineTest5',$$$cl2592.Basic);
        (function($$refineTest5){
            
            //ClassDefinition Inner at refinement2.ceylon (29:4-33:4)
            function Inner$RefineTest5(g$3563, $$inner$RefineTest5){
                $init$Inner$RefineTest5();
                if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new this.Inner$RefineTest5.$$;
                $$inner$RefineTest5.$$outer=this;
                $$inner$RefineTest5.g$3563=g$3563;
                return $$inner$RefineTest5;
            }
            function $init$Inner$RefineTest5(){
                if (Inner$RefineTest5.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$RefineTest5,'nesting::RefineTest5.Inner',$$$cl2592.Basic);
                    RefineTest5.Inner$RefineTest5=Inner$RefineTest5;
                    (function($$inner$RefineTest5){
                        
                        //MethodDefinition x at refinement2.ceylon (30:8-32:8)
                        $$inner$RefineTest5.x=function x(){
                            var $$inner$RefineTest5=this;
                            return $$inner$RefineTest5.g$3563.repeat($$inner$RefineTest5.$$outer.f$3562);
                        };$$inner$RefineTest5.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(Inner$RefineTest5.$$.prototype);
                }
                Inner$RefineTest5.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
                return Inner$RefineTest5;
            }
            $$refineTest5.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
            $init$Inner$RefineTest5();
            $$refineTest5.Inner$RefineTest5=Inner$RefineTest5;
        })(RefineTest5.$$.prototype);
    }
    RefineTest5.$$.$$metamodel$$={$nm:'RefineTest5',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return RefineTest5;
}
exports.$init$RefineTest5=$init$RefineTest5;
$init$RefineTest5();

//ClassDefinition Y2 at refinement2.ceylon (36:0-49:0)
function Y2(h$3564, $$y2){
    $init$Y2();
    if ($$y2===undefined)$$y2=new Y2.$$;
    $$y2.h$3564=h$3564;
    X2($$y2.h$3564,$$y2);
    return $$y2;
}
exports.Y2=Y2;
function $init$Y2(){
    if (Y2.$$===undefined){
        $$$cl2592.initTypeProto(Y2,'nesting::Y2',X2);
        (function($$y2){
            
            //ClassDefinition SubRef1 at refinement2.ceylon (37:4-48:4)
            function SubRef1$Y2(d$3565, $$subRef1$Y2){
                $init$SubRef1$Y2();
                if ($$subRef1$Y2===undefined)$$subRef1$Y2=new this.SubRef1$Y2.$$;
                $$subRef1$Y2.$$outer=this;
                $$subRef1$Y2.d$3565=d$3565;
                $$subRef1$Y2.$$outer.RefineTest1$X2((1),$$subRef1$Y2);
                return $$subRef1$Y2;
            }
            function $init$SubRef1$Y2(){
                if (SubRef1$Y2.$$===undefined){
                    $$$cl2592.initTypeProto(SubRef1$Y2,'nesting::Y2.SubRef1',$$y2.RefineTest1$X2);
                    Y2.SubRef1$Y2=SubRef1$Y2;
                    (function($$subRef1$Y2){
                        
                        //ClassDefinition Inner at refinement2.ceylon (38:6-47:6)
                        function Inner$RefineTest1$X2(d2$3566, $$targs$$,$$inner$RefineTest1$X2){
                            $init$Inner$RefineTest1$X2();
                            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new this.Inner$RefineTest1$X2.$$;
                            $$$cl2592.set_type_args($$inner$RefineTest1$X2,$$targs$$);
                            $$inner$RefineTest1$X2.$$outer=this;
                            $$inner$RefineTest1$X2.d2$3566=d2$3566;
                            $$inner$RefineTest1$X2.$$outer.getT$all()['nesting::X2.RefineTest1'].$$.prototype.Inner$RefineTest1$X2.call(this,$$inner$RefineTest1$X2.d2$3566,{Element:$$inner$RefineTest1$X2.$$targs$$.Element},$$inner$RefineTest1$X2);
                            
                            //AttributeDeclaration suborigin at refinement2.ceylon (40:10-40:51)
                            $$inner$RefineTest1$X2.suborigin$3567_=$$$cl2592.String("SubRef1.Inner",13);
                            return $$inner$RefineTest1$X2;
                        }
                        function $init$Inner$RefineTest1$X2(){
                            if (Inner$RefineTest1$X2.$$===undefined){
                                $$$cl2592.initTypeProto(Inner$RefineTest1$X2,'nesting::Y2.SubRef1.Inner',$$subRef1$Y2.getT$all()['nesting::X2.RefineTest1'].$$.prototype.Inner$RefineTest1$X2);
                                Y2.SubRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                                (function($$inner$RefineTest1$X2){
                                    
                                    //AttributeDeclaration suborigin at refinement2.ceylon (40:10-40:51)
                                    $$$cl2592.defineAttr($$inner$RefineTest1$X2,'suborigin',function(){return this.suborigin$3567_;});
                                    
                                    //MethodDefinition x at refinement2.ceylon (41:10-43:10)
                                    $$inner$RefineTest1$X2.x=function x(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl2592.String("REFINED ",8).plus($$inner$RefineTest1$X2.getT$all()['nesting::X2.RefineTest1.Inner'].$$.prototype.x.call(this));
                                    };$$inner$RefineTest1$X2.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                    //MethodDefinition y at refinement2.ceylon (44:10-46:10)
                                    $$inner$RefineTest1$X2.y=function y(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("y",1),$$inner$RefineTest1$X2.$$outer.$$outer.h$3564.string,$$$cl2592.String(",d:",3),$$inner$RefineTest1$X2.$$outer.d$3565.string,$$$cl2592.String(",d2:",4),$$inner$RefineTest1$X2.d2$3566.string,$$$cl2592.String(".",1)]).string;
                                    };$$inner$RefineTest1$X2.y.$$metamodel$$={$nm:'y',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                                })(Inner$RefineTest1$X2.$$.prototype);
                            }
                            Inner$RefineTest1$X2.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:X2.RefineTest1$X2.Inner$RefineTest1$X2,a:{Element:'Element'}},$tp:{Element:{'satisfies':[{t:$$$cl2592.Object}]}},'satisfies':[]};
                            return Inner$RefineTest1$X2;
                        }
                        $$subRef1$Y2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
                        $init$Inner$RefineTest1$X2();
                        $$subRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                    })(SubRef1$Y2.$$.prototype);
                }
                SubRef1$Y2.$$.$$metamodel$$={$nm:'SubRef1',$mt:'cls','super':{t:X2.RefineTest1$X2},'satisfies':[]};
                return SubRef1$Y2;
            }
            $$y2.$init$SubRef1$Y2=$init$SubRef1$Y2;
            $init$SubRef1$Y2();
            $$y2.SubRef1$Y2=SubRef1$Y2;
        })(Y2.$$.prototype);
    }
    Y2.$$.$$metamodel$$={$nm:'Y2',$mt:'cls','super':{t:X2},'satisfies':[]};
    return Y2;
}
exports.$init$Y2=$init$Y2;
$init$Y2();

//ClassDefinition SubRef4 at refinement2.ceylon (51:0-55:0)
function SubRef4($$subRef4){
    $init$SubRef4();
    if ($$subRef4===undefined)$$subRef4=new SubRef4.$$;
    RefineTest4($$$cl2592.String("t4",2),$$subRef4);
    return $$subRef4;
}
exports.SubRef4=SubRef4;
function $init$SubRef4(){
    if (SubRef4.$$===undefined){
        $$$cl2592.initTypeProto(SubRef4,'nesting::SubRef4',RefineTest4);
        (function($$subRef4){
            
            //MethodDefinition x at refinement2.ceylon (52:4-54:4)
            $$subRef4.x=function x(){
                var $$subRef4=this;
                return $$subRef4.Inner$RefineTest4((5)).hello();
            };$$subRef4.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(SubRef4.$$.prototype);
    }
    SubRef4.$$.$$metamodel$$={$nm:'SubRef4',$mt:'cls','super':{t:RefineTest4},'satisfies':[]};
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
        $$$cl2592.initTypeProto(SubRef5,'nesting::SubRef5',RefineTest5);
        (function($$subRef5){
            
            //MethodDefinition x at refinement2.ceylon (58:4-60:4)
            $$subRef5.x=function x(){
                var $$subRef5=this;
                return $$subRef5.Inner$RefineTest5($$$cl2592.String("sr5",3)).x();
            };$$subRef5.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
        })(SubRef5.$$.prototype);
    }
    SubRef5.$$.$$metamodel$$={$nm:'SubRef5',$mt:'cls','super':{t:RefineTest5},'satisfies':[]};
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
        $$$cl2592.initTypeProto(SubRef51,'nesting::SubRef51',SubRef5);
        (function($$subRef51){
            
            //ClassDefinition Inner at refinement2.ceylon (63:4-65:4)
            function Inner$RefineTest5(subg55$3568, $$inner$RefineTest5){
                $init$Inner$RefineTest5();
                if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new this.Inner$RefineTest5.$$;
                $$inner$RefineTest5.$$outer=this;
                $$inner$RefineTest5.subg55$3568=subg55$3568;
                $$inner$RefineTest5.$$outer.getT$all()['nesting::RefineTest5'].$$.prototype.Inner$RefineTest5.call(this,$$inner$RefineTest5.subg55$3568,$$inner$RefineTest5);
                return $$inner$RefineTest5;
            }
            function $init$Inner$RefineTest5(){
                if (Inner$RefineTest5.$$===undefined){
                    $$$cl2592.initTypeProto(Inner$RefineTest5,'nesting::SubRef51.Inner',$$subRef51.getT$all()['nesting::RefineTest5'].$$.prototype.Inner$RefineTest5);
                    SubRef51.Inner$RefineTest5=Inner$RefineTest5;
                    (function($$inner$RefineTest5){
                        
                        //MethodDefinition x at refinement2.ceylon (64:8-64:62)
                        $$inner$RefineTest5.x=function x(){
                            var $$inner$RefineTest5=this;
                            return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("equis",5),$$inner$RefineTest5.subg55$3568.string,$$$cl2592.String(".",1)]).string;
                        };$$inner$RefineTest5.x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
                    })(Inner$RefineTest5.$$.prototype);
                }
                Inner$RefineTest5.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:RefineTest5.Inner$RefineTest5},'satisfies':[]};
                return Inner$RefineTest5;
            }
            $$subRef51.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
            $init$Inner$RefineTest5();
            $$subRef51.Inner$RefineTest5=Inner$RefineTest5;
        })(SubRef51.$$.prototype);
    }
    SubRef51.$$.$$metamodel$$={$nm:'SubRef51',$mt:'cls','super':{t:SubRef5},'satisfies':[]};
    return SubRef51;
}
exports.$init$SubRef51=$init$SubRef51;
$init$SubRef51();

//MethodDefinition testRefinement2 at refinement2.ceylon (68:0-78:0)
function testRefinement2(){
    
    //AttributeDeclaration c1 at refinement2.ceylon (69:4-69:54)
    var c1$3569=Y2($$$cl2592.String("y2",2)).SubRef1$Y2((99)).Inner$RefineTest1$X2($$$cl2592.String("with parm",9),{Element:{t:$$$cl2592.String}});
    $$$c2593.check($$$cl2592.className(c1$3569).equals($$$cl2592.String("nesting::Y2.SubRef1.Inner",25)),$$$cl2592.String("classname is ",13).plus($$$cl2592.className(c1$3569)));
    $$$c2593.check(c1$3569.origin.equals($$$cl2592.String("RefineTest1.Inner (with parm)",29)),$$$cl2592.String("refinement [1] ",15).plus(c1$3569.origin));
    $$$c2593.check(c1$3569.suborigin.equals($$$cl2592.String("SubRef1.Inner",13)),$$$cl2592.String("refinement [2] ",15).plus(c1$3569.suborigin));
    $$$c2593.check(c1$3569.x().equals($$$cl2592.String("REFINED x and yy2,d:99,d2:with parm. and a:y2, b:1, c:with parm.",64)),$$$cl2592.String("refinement [3] ",15).plus(c1$3569.x()));
    $$$c2593.check(Y2($$$cl2592.String("y3",2)).SubRef1$Y2((10)).outerx().equals($$$cl2592.String("REFINED x and yy3,d:10,d2:Y3. and a:y3, b:1, c:Y3.",50)),$$$cl2592.String("refinement [4] ",15).plus(Y2($$$cl2592.String("y3",2)).SubRef1$Y2((10)).outerx()));
    $$$c2593.check(SubRef4().x().equals($$$cl2592.String("hello from RefineTest2.Inner with 5.",36)),$$$cl2592.String("refinement [5] ",15).plus(SubRef4().x()));
    $$$c2593.check(SubRef5().x().equals($$$cl2592.String("sr5sr5sr5sr5sr5sr5",18)),$$$cl2592.String("refinement [6] ",15).plus(SubRef5().x()));
    $$$c2593.check(SubRef51().x().equals($$$cl2592.String("equissr5.",9)),$$$cl2592.String("refinement [7] ",15).plus(SubRef51().x()));
};testRefinement2.$$metamodel$$={$nm:'testRefinement2',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testRefinement2.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
