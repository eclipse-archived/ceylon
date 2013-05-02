(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"nesting","$mod-version":"0.1","nesting":{"Y2":{"super":{"$pk":"nesting","$nm":"X2"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"h"}],"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"d"}],"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"d2"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y2"},"SubRef51":{"super":{"$pk":"nesting","$nm":"SubRef5"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"subg55"}],"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef51"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"baz":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"baz"}},"$c":{"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"foobar":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"foobar"},"quxx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"quxx"}},"$nm":"C"}},"$at":{"qux":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"qux"}},"$nm":"B"}},"$at":{"foo":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"foo"}},"$nm":"A"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Unwrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$an":{"shared":[]},"$nm":"o"}},"$nm":"Unwrapper"},"SubRef2":{"super":{"$pk":"nesting","$nm":"RefineTest2"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef2"},"Wrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Wrapper"},"O":{"$i":{"InnerInterface":{"$mt":"ifc","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerInterface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"test1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test1"},"test2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test2"},"test3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test3"}},"$c":{"InnerClass":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerClass"}},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"s"}},"$nm":"O","$o":{"innerObject":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"innerObject"}}},"outr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"outr"},"testRefinement2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement2"},"Holder":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"o"}],"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"}},"$nm":"Holder"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C3":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"x"}},"$nm":"C3"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"},"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C3"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"},"Y":{"super":{"$pk":"nesting","$nm":"X"},"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y"},"X":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X"},"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"},"outerf":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$nm":"outerf"},"X2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"a"}],"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":"Element","$mt":"prm","$pt":"v","$nm":"c"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X2"},"OuterC1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"OuterC1"},"SubRef31":{"super":{"$pk":"nesting","$nm":"SubRef3"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef31"},"OuterC2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"}},"$nm":"OuterC2"},"testRefinement":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement"},"returner":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"o"}]],"$mt":"mthd","$nm":"returner"},"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"noop"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"noop"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"printName"}},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"gttr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Inner"}},"$at":{"inner":{"$t":{"$pk":"nesting","$nm":"Inner"},"$mt":"attr","$nm":"inner"},"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"attr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Outer"},"producer":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"mthd","$nm":"producer"},"RefineTest5":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"f"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"g"}],"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest5"},"RefineTest4":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"d"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"e"}],"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest4"},"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}},"RefineTest3":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest3"},"RefineTest2":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest2"},"SubRef5":{"super":{"$pk":"nesting","$nm":"RefineTest5"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef5"},"SubRef4":{"super":{"$pk":"nesting","$nm":"RefineTest4"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef4"},"SubRef3":{"super":{"$pk":"nesting","$nm":"RefineTest3"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef3"}}}
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2=require('check/0.1/check-0.1');

//ClassDefinition Outer at nesting.ceylon (3:0-28:0)
function Outer(name$868, $$outer){
    $init$Outer();
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$868=name$868;
    
    //AttributeDeclaration int at nesting.ceylon (4:4-4:18)
    var int$869=(10);
    $$$cl1.defineAttr($$outer,'int$869',function(){return int$869;});
    
    //AttributeDeclaration float at nesting.ceylon (5:4-5:34)
    var $float=int$869.$float;
    $$$cl1.defineAttr($$outer,'$float',function(){return $float;});
    
    //MethodDefinition noop at nesting.ceylon (6:4-6:17)
    function noop$870(){
    }
    $$outer.noop$870=noop$870;
    noop$870.$$metamodel$$={$nm:'noop',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//noop$870.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    
    //ClassDefinition Inner at nesting.ceylon (7:4-20:4)
    function Inner$871($$inner$871){
        $init$Inner$871();
        if ($$inner$871===undefined)$$inner$871=new Inner$871.$$;
        $$inner$871.$$outer=this;
        
        //AttributeGetterDefinition int at nesting.ceylon (11:8-13:8)
        $$$cl1.defineAttr($$inner$871,'$int',function(){
            return $$outer.int$869;
        });
        
        //AttributeGetterDefinition float at nesting.ceylon (14:8-16:8)
        $$$cl1.defineAttr($$inner$871,'$float',function(){
            return $$outer.$float;
        });
        
        //MethodDefinition noop at nesting.ceylon (17:8-19:8)
        function noop(){
            $$outer.noop$870();
        }
        $$inner$871.noop=noop;
        noop.$$metamodel$$={$nm:'noop',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//noop.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
        return $$inner$871;
    }
    function $init$Inner$871(){
        if (Inner$871.$$===undefined){
            $$$cl1.initTypeProto(Inner$871,'nesting::Outer.Inner',$$$cl1.Basic);
            Outer.Inner$871=Inner$871;
        }
        Inner$871.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return Inner$871;
    }
    $$outer.$init$Inner$871=$init$Inner$871;
    $init$Inner$871();
    
    //AttributeDeclaration inner at nesting.ceylon (21:4-21:25)
    var inner$872=Inner$871();
    $$$cl1.print(inner$872.$int);
    $$$cl1.print(inner$872.$float);
    inner$872.noop();
    noop$870();
    return $$outer;
}
exports.Outer=Outer;
function $init$Outer(){
    if (Outer.$$===undefined){
        $$$cl1.initTypeProto(Outer,'nesting::Outer',$$$cl1.Basic);
    }
    Outer.$$.$$metamodel$$={$nm:'Outer',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Outer;
}
exports.$init$Outer=$init$Outer;
$init$Outer();

//MethodDefinition outr at nesting.ceylon (30:0-42:0)
function outr(name$873){
    
    //AttributeDeclaration uname at nesting.ceylon (31:4-31:34)
    var uname$874=name$873.uppercased;
    
    //MethodDefinition inr at nesting.ceylon (32:4-34:4)
    function inr$875(){
        return name$873;
    };inr$875.$$metamodel$$={$nm:'inr',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//inr$875.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //AttributeGetterDefinition uinr at nesting.ceylon (35:4-37:4)
    var getUinr$876=function(){
        return uname$874;
    };
    
    //AttributeDeclaration result at nesting.ceylon (38:4-38:25)
    var result$877=inr$875();
    
    //AttributeDeclaration uresult at nesting.ceylon (39:4-39:25)
    var uresult$878=getUinr$876();
    $$$cl1.print(result$877);
    $$$cl1.print(uresult$878);
}
exports.outr=outr;
outr.$$metamodel$$={$nm:'outr',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String}}]};//outr.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},Return:{t:$$$cl1.Anything}};

//ClassDefinition Holder at nesting.ceylon (44:0-51:0)
function Holder(o$879, $$holder){
    $init$Holder();
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$879=o$879;
    
    //MethodDefinition get at nesting.ceylon (45:4-47:4)
    function get(){
        return o$879;
    }
    $$holder.get=get;
    get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl1.Object},$ps:[]};//get.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}};
    
    //AttributeGetterDefinition string at nesting.ceylon (48:4-50:4)
    $$$cl1.defineAttr($$holder,'string',function(){
        return o$879.string;
    });
    return $$holder;
}
exports.Holder=Holder;
function $init$Holder(){
    if (Holder.$$===undefined){
        $$$cl1.initTypeProto(Holder,'nesting::Holder',$$$cl1.Basic);
    }
    Holder.$$.$$metamodel$$={$nm:'Holder',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Holder;
}
exports.$init$Holder=$init$Holder;
$init$Holder();

//ClassDefinition Wrapper at nesting.ceylon (53:0-61:0)
function Wrapper($$wrapper){
    $init$Wrapper();
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (54:4-54:18)
    var o$880=(100);
    $$$cl1.defineAttr($$wrapper,'o$880',function(){return o$880;});
    
    //MethodDefinition get at nesting.ceylon (55:4-57:4)
    function get(){
        return o$880;
    }
    $$wrapper.get=get;
    get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl1.Object},$ps:[]};//get.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}};
    
    //AttributeGetterDefinition string at nesting.ceylon (58:4-60:4)
    $$$cl1.defineAttr($$wrapper,'string',function(){
        return o$880.string;
    });
    return $$wrapper;
}
exports.Wrapper=Wrapper;
function $init$Wrapper(){
    if (Wrapper.$$===undefined){
        $$$cl1.initTypeProto(Wrapper,'nesting::Wrapper',$$$cl1.Basic);
    }
    Wrapper.$$.$$metamodel$$={$nm:'Wrapper',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Wrapper;
}
exports.$init$Wrapper=$init$Wrapper;
$init$Wrapper();

//ClassDefinition Unwrapper at nesting.ceylon (63:0-71:0)
function Unwrapper($$unwrapper){
    $init$Unwrapper();
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDeclaration o at nesting.ceylon (64:4-64:27)
    var o=$$$cl1.Float(23.56);
    $$$cl1.defineAttr($$unwrapper,'o',function(){return o;});
    
    //MethodDefinition get at nesting.ceylon (65:4-67:4)
    function get(){
        return $$unwrapper.o;
    }
    $$unwrapper.get=get;
    get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl1.Object},$ps:[]};//get.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}};
    
    //AttributeGetterDefinition string at nesting.ceylon (68:4-70:4)
    $$$cl1.defineAttr($$unwrapper,'string',function(){
        return $$unwrapper.o.string;
    });
    return $$unwrapper;
}
exports.Unwrapper=Unwrapper;
function $init$Unwrapper(){
    if (Unwrapper.$$===undefined){
        $$$cl1.initTypeProto(Unwrapper,'nesting::Unwrapper',$$$cl1.Basic);
    }
    Unwrapper.$$.$$metamodel$$={$nm:'Unwrapper',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return Unwrapper;
}
exports.$init$Unwrapper=$init$Unwrapper;
$init$Unwrapper();

//MethodDefinition producer at nesting.ceylon (73:0-77:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (74:4-74:18)
    var o$881=(123);
    
    //MethodDefinition produce at nesting.ceylon (75:4-75:35)
    function produce$882(){
        return o$881;
    };produce$882.$$metamodel$$={$nm:'produce',$mt:'mthd',$t:{t:$$$cl1.Object},$ps:[]};//produce$882.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}};
    return produce$882;
};producer.$$metamodel$$={$nm:'producer',$mt:'mthd',$t:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}}},$ps:[]};//producer.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}}}};

//MethodDefinition returner at nesting.ceylon (79:0-82:0)
function returner(o$883){
    
    //MethodDefinition produce at nesting.ceylon (80:4-80:35)
    function produce$884(){
        return o$883;
    };produce$884.$$metamodel$$={$nm:'produce',$mt:'mthd',$t:{t:$$$cl1.Object},$ps:[]};//produce$884.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}};
    return produce$884;
};returner.$$metamodel$$={$nm:'returner',$mt:'mthd',$t:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}}},$ps:[{$nm:'o',$mt:'prm',$t:{t:$$$cl1.Object}}]};//returner.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Object},Element:{t:$$$cl1.Object}}},Return:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}}}};

//ClassDefinition A at nesting.ceylon (84:0-105:0)
function A($$a){
    $init$A();
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDeclaration foo at nesting.ceylon (85:4-85:22)
    var foo$885=$$$cl1.String("foo",3);
    $$$cl1.defineAttr($$a,'foo$885',function(){return foo$885;});
    
    //ClassDefinition B at nesting.ceylon (86:4-96:4)
    function B$A($$b$A){
        $init$B$A();
        if ($$b$A===undefined)$$b$A=new B$A.$$;
        $$b$A.$$outer=this;
        
        //AttributeDeclaration qux at nesting.ceylon (87:8-87:26)
        var qux$886=$$$cl1.String("qux",3);
        $$$cl1.defineAttr($$b$A,'qux$886',function(){return qux$886;});
        
        //ClassDefinition C at nesting.ceylon (88:8-95:8)
        function C$B$A($$c$B$A){
            $init$C$B$A();
            if ($$c$B$A===undefined)$$c$B$A=new C$B$A.$$;
            $$c$B$A.$$outer=this;
            
            //MethodDefinition foobar at nesting.ceylon (89:12-91:12)
            function foobar(){
                return foo$885;
            }
            $$c$B$A.foobar=foobar;
            foobar.$$metamodel$$={$nm:'foobar',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//foobar.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            
            //MethodDefinition quxx at nesting.ceylon (92:12-94:12)
            function quxx(){
                return qux$886;
            }
            $$c$B$A.quxx=quxx;
            quxx.$$metamodel$$={$nm:'quxx',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//quxx.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            return $$c$B$A;
        }
        $$b$A.C$B$A=C$B$A;
        function $init$C$B$A(){
            if (C$B$A.$$===undefined){
                $$$cl1.initTypeProto(C$B$A,'nesting::A.B.C',$$$cl1.Basic);
                A.B$A.C$B$A=C$B$A;
            }
            C$B$A.$$.$$metamodel$$={$nm:'C',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
            return C$B$A;
        }
        $$b$A.$init$C$B$A=$init$C$B$A;
        $init$C$B$A();
        return $$b$A;
    }
    $$a.B$A=B$A;
    function $init$B$A(){
        if (B$A.$$===undefined){
            $$$cl1.initTypeProto(B$A,'nesting::A.B',$$$cl1.Basic);
            A.B$A=B$A;
        }
        B$A.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return B$A;
    }
    $$a.$init$B$A=$init$B$A;
    $init$B$A();
    
    //MethodDefinition baz at nesting.ceylon (97:4-104:4)
    function baz(){
        
        //ClassDefinition Baz at nesting.ceylon (98:8-102:8)
        function Baz$887($$baz$887){
            $init$Baz$887();
            if ($$baz$887===undefined)$$baz$887=new Baz$887.$$;
            
            //MethodDefinition get at nesting.ceylon (99:12-101:12)
            function get(){
                return foo$885;
            }
            $$baz$887.get=get;
            get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//get.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            return $$baz$887;
        }
        function $init$Baz$887(){
            if (Baz$887.$$===undefined){
                $$$cl1.initTypeProto(Baz$887,'nesting::A.baz.Baz',$$$cl1.Basic);
            }
            Baz$887.$$.$$metamodel$$={$nm:'Baz',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
            return Baz$887;
        }
        $init$Baz$887();
        return Baz$887().get();
    }
    $$a.baz=baz;
    baz.$$metamodel$$={$nm:'baz',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//baz.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$a;
}
function $init$A(){
    if (A.$$===undefined){
        $$$cl1.initTypeProto(A,'nesting::A',$$$cl1.Basic);
    }
    A.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return A;
}
exports.$init$A=$init$A;
$init$A();

//ClassDefinition O at nesting.ceylon (107:0-134:0)
function O($$o){
    $init$O();
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDeclaration s at nesting.ceylon (108:4-108:22)
    var s$888=$$$cl1.String("hello",5);
    $$$cl1.defineAttr($$o,'s$888',function(){return s$888;});
    
    //ClassDefinition InnerClass at nesting.ceylon (109:4-113:4)
    function InnerClass$889($$innerClass$889){
        $init$InnerClass$889();
        if ($$innerClass$889===undefined)$$innerClass$889=new InnerClass$889.$$;
        $$innerClass$889.$$outer=this;
        
        //MethodDefinition f at nesting.ceylon (110:8-112:8)
        function f(){
            return s$888;
        }
        $$innerClass$889.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$innerClass$889;
    }
    function $init$InnerClass$889(){
        if (InnerClass$889.$$===undefined){
            $$$cl1.initTypeProto(InnerClass$889,'nesting::O.InnerClass',$$$cl1.Basic);
            O.InnerClass$889=InnerClass$889;
        }
        InnerClass$889.$$.$$metamodel$$={$nm:'InnerClass',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return InnerClass$889;
    }
    $$o.$init$InnerClass$889=$init$InnerClass$889;
    $init$InnerClass$889();
    
    //ObjectDefinition innerObject at nesting.ceylon (114:4-118:4)
    function innerObject$890(){
        var $$innerObject$890=new innerObject$890.$$;
        $$innerObject$890.$$outer=this;
        
        //MethodDefinition f at nesting.ceylon (115:8-117:8)
        function f(){
            return s$888;
        }
        $$innerObject$890.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$innerObject$890;
    }
    function $init$innerObject$890(){
        if (innerObject$890.$$===undefined){
            $$$cl1.initTypeProto(innerObject$890,'nesting::O.innerObject',$$$cl1.Basic);
            O.innerObject$890=innerObject$890;
        }
        innerObject$890.$$.$$metamodel$$={$nm:'innerObject',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return innerObject$890;
    }
    $$o.$init$innerObject$890=$init$innerObject$890;
    $init$innerObject$890();
    var innerObject$891=innerObject$890();
    $$$cl1.defineAttr($$o,'innerObject$891',function(){return innerObject$891;});
    
    //InterfaceDefinition InnerInterface at nesting.ceylon (119:4-123:4)
    function InnerInterface$892($$innerInterface$892){
        $$innerInterface$892.$$outer=this;
        
        //MethodDefinition f at nesting.ceylon (120:8-122:8)
        function f(){
            return s$888;
        }
        $$innerInterface$892.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    }
    function $init$InnerInterface$892(){
        if (InnerInterface$892.$$===undefined){
            $$$cl1.initTypeProto(InnerInterface$892,'nesting::O.InnerInterface');
            O.InnerInterface$892=InnerInterface$892;
        }
        InnerInterface$892.$$.$$metamodel$$={$nm:'InnerInterface',$mt:'ifc','satisfies':[]};
        return InnerInterface$892;
    }
    $$o.$init$InnerInterface$892=$init$InnerInterface$892;
    $init$InnerInterface$892();
    
    //MethodDefinition test1 at nesting.ceylon (124:4-126:4)
    function test1(){
        return InnerClass$889().f();
    }
    $$o.test1=test1;
    test1.$$metamodel$$={$nm:'test1',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//test1.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //MethodDefinition test2 at nesting.ceylon (127:4-129:4)
    function test2(){
        return innerObject$891.f();
    }
    $$o.test2=test2;
    test2.$$metamodel$$={$nm:'test2',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//test2.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //MethodDefinition test3 at nesting.ceylon (130:4-133:4)
    function test3(){
        
        //ObjectDefinition obj at nesting.ceylon (131:8-131:45)
        function obj$893(){
            var $$obj$893=new obj$893.$$;
            InnerInterface$892($$obj$893);
            return $$obj$893;
        }
        function $init$obj$893(){
            if (obj$893.$$===undefined){
                $$$cl1.initTypeProto(obj$893,'nesting::O.test3.obj',$$$cl1.Basic,$init$InnerInterface$892());
            }
            obj$893.$$.$$metamodel$$={$nm:'obj',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:O.InnerInterface$892}]};
            return obj$893;
        }
        $init$obj$893();
        var obj$894=obj$893();
        var getObj$894=function(){
            return obj$894;
        }
        return getObj$894().f();
    }
    $$o.test3=test3;
    test3.$$metamodel$$={$nm:'test3',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//test3.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$o;
}
function $init$O(){
    if (O.$$===undefined){
        $$$cl1.initTypeProto(O,'nesting::O',$$$cl1.Basic);
    }
    O.$$.$$metamodel$$={$nm:'O',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return O;
}
exports.$init$O=$init$O;
$init$O();

//ClassDefinition OuterC1 at nesting.ceylon (136:0-142:0)
function OuterC1($$outerC1){
    $init$OuterC1();
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    
    //ClassDefinition A at nesting.ceylon (137:4-139:4)
    function A$895($$a$895){
        $init$A$895();
        if ($$a$895===undefined)$$a$895=new A$895.$$;
        $$a$895.$$outer=this;
        
        //MethodDefinition tst at nesting.ceylon (138:8-138:55)
        function tst(){
            return $$$cl1.String("OuterC1.A.tst()",15);
        }
        $$a$895.tst=tst;
        tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//tst.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$a$895;
    }
    function $init$A$895(){
        if (A$895.$$===undefined){
            $$$cl1.initTypeProto(A$895,'nesting::OuterC1.A',$$$cl1.Basic);
            OuterC1.A$895=A$895;
        }
        A$895.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return A$895;
    }
    $$outerC1.$init$A$895=$init$A$895;
    $init$A$895();
    
    //ClassDefinition B at nesting.ceylon (140:4-140:27)
    function B$896($$b$896){
        $init$B$896();
        if ($$b$896===undefined)$$b$896=new B$896.$$;
        $$b$896.$$outer=this;
        A$895($$b$896);
        return $$b$896;
    }
    function $init$B$896(){
        if (B$896.$$===undefined){
            $$$cl1.initTypeProto(B$896,'nesting::OuterC1.B',A$895);
            OuterC1.B$896=B$896;
        }
        B$896.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:OuterC1.A$895},'satisfies':[]};
        return B$896;
    }
    $$outerC1.$init$B$896=$init$B$896;
    $init$B$896();
    
    //MethodDefinition tst at nesting.ceylon (141:4-141:42)
    function tst(){
        return B$896().tst();
    }
    $$outerC1.tst=tst;
    tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//tst.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$outerC1;
}
function $init$OuterC1(){
    if (OuterC1.$$===undefined){
        $$$cl1.initTypeProto(OuterC1,'nesting::OuterC1',$$$cl1.Basic);
    }
    OuterC1.$$.$$metamodel$$={$nm:'OuterC1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return OuterC1;
}
exports.$init$OuterC1=$init$OuterC1;
$init$OuterC1();

//MethodDefinition outerf at nesting.ceylon (144:0-150:0)
function outerf(){
    
    //ClassDefinition A at nesting.ceylon (145:4-147:4)
    function A$897($$a$897){
        $init$A$897();
        if ($$a$897===undefined)$$a$897=new A$897.$$;
        
        //MethodDefinition tst at nesting.ceylon (146:8-146:54)
        function tst(){
            return $$$cl1.String("outerf.A.tst()",14);
        }
        $$a$897.tst=tst;
        tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//tst.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$a$897;
    }
    function $init$A$897(){
        if (A$897.$$===undefined){
            $$$cl1.initTypeProto(A$897,'nesting::outerf.A',$$$cl1.Basic);
        }
        A$897.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return A$897;
    }
    $init$A$897();
    
    //ClassDefinition B at nesting.ceylon (148:4-148:27)
    function B$898($$b$898){
        $init$B$898();
        if ($$b$898===undefined)$$b$898=new B$898.$$;
        A$897($$b$898);
        return $$b$898;
    }
    function $init$B$898(){
        if (B$898.$$===undefined){
            $$$cl1.initTypeProto(B$898,'nesting::outerf.B',A$897);
        }
        B$898.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:A$897},'satisfies':[]};
        return B$898;
    }
    $init$B$898();
    return B$898().tst();
};outerf.$$metamodel$$={$nm:'outerf',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//outerf.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};

//ClassDefinition OuterC2 at nesting.ceylon (152:0-160:0)
function OuterC2($$outerC2){
    $init$OuterC2();
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    
    //ClassDefinition A at nesting.ceylon (153:4-155:4)
    function A$899($$a$899){
        $init$A$899();
        if ($$a$899===undefined)$$a$899=new A$899.$$;
        $$a$899.$$outer=this;
        
        //MethodDefinition tst at nesting.ceylon (154:8-154:55)
        function tst(){
            return $$$cl1.String("OuterC2.A.tst()",15);
        }
        $$a$899.tst=tst;
        tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//tst.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$a$899;
    }
    function $init$A$899(){
        if (A$899.$$===undefined){
            $$$cl1.initTypeProto(A$899,'nesting::OuterC2.A',$$$cl1.Basic);
            OuterC2.A$899=A$899;
        }
        A$899.$$.$$metamodel$$={$nm:'A',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return A$899;
    }
    $$outerC2.$init$A$899=$init$A$899;
    $init$A$899();
    
    //MethodDefinition tst at nesting.ceylon (156:4-159:4)
    function tst(){
        
        //ClassDefinition B at nesting.ceylon (157:8-157:31)
        function B$900($$b$900){
            $init$B$900();
            if ($$b$900===undefined)$$b$900=new B$900.$$;
            A$899($$b$900);
            return $$b$900;
        }
        function $init$B$900(){
            if (B$900.$$===undefined){
                $$$cl1.initTypeProto(B$900,'nesting::OuterC2.tst.B',A$899);
            }
            B$900.$$.$$metamodel$$={$nm:'B',$mt:'cls','super':{t:OuterC2.A$899},'satisfies':[]};
            return B$900;
        }
        $init$B$900();
        return B$900().tst();
    }
    $$outerC2.tst=tst;
    tst.$$metamodel$$={$nm:'tst',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//tst.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$outerC2;
}
function $init$OuterC2(){
    if (OuterC2.$$===undefined){
        $$$cl1.initTypeProto(OuterC2,'nesting::OuterC2',$$$cl1.Basic);
    }
    OuterC2.$$.$$metamodel$$={$nm:'OuterC2',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return OuterC2;
}
exports.$init$OuterC2=$init$OuterC2;
$init$OuterC2();

//ClassDefinition NameTest at nesting.ceylon (162:0-178:0)
function NameTest($$nameTest){
    $init$NameTest();
    if ($$nameTest===undefined)$$nameTest=new NameTest.$$;
    
    //AttributeDeclaration x at nesting.ceylon (163:4-163:25)
    var x=$$$cl1.String("1",1);
    $$$cl1.defineAttr($$nameTest,'x',function(){return x;});
    
    //ClassDefinition NameTest at nesting.ceylon (164:4-176:4)
    function NameTest$NameTest($$nameTest$NameTest){
        $init$NameTest$NameTest();
        if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new NameTest$NameTest.$$;
        $$nameTest$NameTest.$$outer=this;
        
        //AttributeDeclaration x at nesting.ceylon (165:8-165:29)
        var x=$$$cl1.String("2",1);
        $$$cl1.defineAttr($$nameTest$NameTest,'x',function(){return x;});
        
        //MethodDefinition f at nesting.ceylon (166:8-175:8)
        function f(){
            
            //ClassDefinition NameTest at nesting.ceylon (167:12-173:12)
            function NameTest$901($$nameTest$901){
                $init$NameTest$901();
                if ($$nameTest$901===undefined)$$nameTest$901=new NameTest$901.$$;
                
                //AttributeDeclaration x at nesting.ceylon (168:16-168:37)
                var x=$$$cl1.String("3",1);
                $$$cl1.defineAttr($$nameTest$901,'x',function(){return x;});
                
                //ClassDefinition NameTest at nesting.ceylon (169:16-171:16)
                function NameTest$NameTest($$nameTest$NameTest){
                    $init$NameTest$NameTest();
                    if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new NameTest$NameTest.$$;
                    $$nameTest$NameTest.$$outer=this;
                    
                    //AttributeDeclaration x at nesting.ceylon (170:20-170:41)
                    var x=$$$cl1.String("4",1);
                    $$$cl1.defineAttr($$nameTest$NameTest,'x',function(){return x;});
                    return $$nameTest$NameTest;
                }
                $$nameTest$901.NameTest$NameTest=NameTest$NameTest;
                function $init$NameTest$NameTest(){
                    if (NameTest$NameTest.$$===undefined){
                        $$$cl1.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest.f.NameTest.NameTest',$$$cl1.Basic);
                        NameTest$901.NameTest$NameTest=NameTest$NameTest;
                    }
                    NameTest$NameTest.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
                    return NameTest$NameTest;
                }
                $$nameTest$901.$init$NameTest$NameTest=$init$NameTest$NameTest;
                $init$NameTest$NameTest();
                
                //MethodDefinition f at nesting.ceylon (172:16-172:66)
                function f(){
                    return $$nameTest$901.x.plus($$nameTest$901.NameTest$NameTest().x);
                }
                $$nameTest$901.f=f;
                f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
                return $$nameTest$901;
            }
            function $init$NameTest$901(){
                if (NameTest$901.$$===undefined){
                    $$$cl1.initTypeProto(NameTest$901,'nesting::NameTest.NameTest.f.NameTest',$$$cl1.Basic);
                }
                NameTest$901.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
                return NameTest$901;
            }
            $init$NameTest$901();
            return $$nameTest.x.plus($$nameTest$NameTest.x).plus(NameTest$901().f());
        }
        $$nameTest$NameTest.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$nameTest$NameTest;
    }
    $$nameTest.NameTest$NameTest=NameTest$NameTest;
    function $init$NameTest$NameTest(){
        if (NameTest$NameTest.$$===undefined){
            $$$cl1.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest',$$$cl1.Basic);
            NameTest.NameTest$NameTest=NameTest$NameTest;
        }
        NameTest$NameTest.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return NameTest$NameTest;
    }
    $$nameTest.$init$NameTest$NameTest=$init$NameTest$NameTest;
    $init$NameTest$NameTest();
    
    //MethodDefinition f at nesting.ceylon (177:4-177:52)
    function f(){
        return $$nameTest.NameTest$NameTest().f();
    }
    $$nameTest.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$nameTest;
}
exports.NameTest=NameTest;
function $init$NameTest(){
    if (NameTest.$$===undefined){
        $$$cl1.initTypeProto(NameTest,'nesting::NameTest',$$$cl1.Basic);
    }
    NameTest.$$.$$metamodel$$={$nm:'NameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return NameTest;
}
exports.$init$NameTest=$init$NameTest;
$init$NameTest();

//ObjectDefinition nameTest at nesting.ceylon (180:0-196:0)
function nameTest$902(){
    var $$nameTest=new nameTest$902.$$;
    
    //AttributeDeclaration x at nesting.ceylon (181:4-181:25)
    var x=$$$cl1.String("1",1);
    $$$cl1.defineAttr($$nameTest,'x',function(){return x;});
    
    //ObjectDefinition nameTest at nesting.ceylon (182:4-194:4)
    function nameTest$903(){
        var $$nameTest$nameTest=new nameTest$903.$$;
        $$nameTest$nameTest.$$outer=this;
        
        //AttributeDeclaration x at nesting.ceylon (183:8-183:29)
        var x=$$$cl1.String("2",1);
        $$$cl1.defineAttr($$nameTest$nameTest,'x',function(){return x;});
        
        //MethodDefinition f at nesting.ceylon (184:8-193:8)
        function f(){
            
            //ObjectDefinition nameTest at nesting.ceylon (185:12-191:12)
            function nameTest$904(){
                var $$nameTest$904=new nameTest$904.$$;
                
                //AttributeDeclaration x at nesting.ceylon (186:16-186:37)
                var x=$$$cl1.String("3",1);
                $$$cl1.defineAttr($$nameTest$904,'x',function(){return x;});
                
                //ObjectDefinition nameTest at nesting.ceylon (187:16-189:16)
                function nameTest$905(){
                    var $$nameTest$nameTest=new nameTest$905.$$;
                    $$nameTest$nameTest.$$outer=this;
                    
                    //AttributeDeclaration x at nesting.ceylon (188:20-188:41)
                    var x=$$$cl1.String("4",1);
                    $$$cl1.defineAttr($$nameTest$nameTest,'x',function(){return x;});
                    return $$nameTest$nameTest;
                }
                function $init$nameTest$905(){
                    if (nameTest$905.$$===undefined){
                        $$$cl1.initTypeProto(nameTest$905,'nesting::nameTest.nameTest.f.nameTest.nameTest',$$$cl1.Basic);
                        nameTest$904.nameTest$905=nameTest$905;
                    }
                    nameTest$905.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
                    return nameTest$905;
                }
                $$nameTest$904.$init$nameTest$905=$init$nameTest$905;
                $init$nameTest$905();
                var nameTest=nameTest$905();
                $$$cl1.defineAttr($$nameTest$904,'nameTest',function(){return nameTest;});
                
                //MethodDefinition f at nesting.ceylon (190:16-190:64)
                function f(){
                    return $$nameTest$904.x.plus($$nameTest$904.nameTest.x);
                }
                $$nameTest$904.f=f;
                f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
                return $$nameTest$904;
            }
            function $init$nameTest$904(){
                if (nameTest$904.$$===undefined){
                    $$$cl1.initTypeProto(nameTest$904,'nesting::nameTest.nameTest.f.nameTest',$$$cl1.Basic);
                }
                nameTest$904.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
                return nameTest$904;
            }
            $init$nameTest$904();
            var nameTest$906=nameTest$904();
            var getNameTest$906=function(){
                return nameTest$906;
            }
            return $$nameTest.x.plus($$nameTest$nameTest.x).plus(getNameTest$906().f());
        }
        $$nameTest$nameTest.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$nameTest$nameTest;
    }
    function $init$nameTest$903(){
        if (nameTest$903.$$===undefined){
            $$$cl1.initTypeProto(nameTest$903,'nesting::nameTest.nameTest',$$$cl1.Basic);
            nameTest$902.nameTest$903=nameTest$903;
        }
        nameTest$903.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return nameTest$903;
    }
    $$nameTest.$init$nameTest$903=$init$nameTest$903;
    $init$nameTest$903();
    var nameTest=nameTest$903();
    $$$cl1.defineAttr($$nameTest,'nameTest',function(){return nameTest;});
    
    //MethodDefinition f at nesting.ceylon (195:4-195:50)
    function f(){
        return $$nameTest.nameTest.f();
    }
    $$nameTest.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$nameTest;
}
function $init$nameTest$902(){
    if (nameTest$902.$$===undefined){
        $$$cl1.initTypeProto(nameTest$902,'nesting::nameTest',$$$cl1.Basic);
    }
    nameTest$902.$$.$$metamodel$$={$nm:'nameTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return nameTest$902;
}
exports.$init$nameTest$902=$init$nameTest$902;
$init$nameTest$902();
var nameTest$907=nameTest$902();
var getNameTest=function(){
    return nameTest$907;
}
exports.getNameTest=getNameTest;

//ClassDefinition C1 at nesting.ceylon (198:0-209:0)
function C1($$c1){
    $init$C1();
    if ($$c1===undefined)$$c1=new C1.$$;
    
    //AttributeDeclaration x at nesting.ceylon (199:4-199:33)
    var x=$$$cl1.String("1",1);
    $$$cl1.defineAttr($$c1,'x',function(){return x;});
    
    //ClassDefinition C1 at nesting.ceylon (200:4-202:4)
    function C1$C1($$c1$C1){
        $init$C1$C1();
        if ($$c1$C1===undefined)$$c1$C1=new C1$C1.$$;
        $$c1$C1.$$outer=this;
        
        //AttributeDeclaration x at nesting.ceylon (201:8-201:38)
        var x=$$$cl1.String("11",2);
        $$$cl1.defineAttr($$c1$C1,'x',function(){return x;});
        return $$c1$C1;
    }
    $$c1.C1$C1=C1$C1;
    function $init$C1$C1(){
        if (C1$C1.$$===undefined){
            $$$cl1.initTypeProto(C1$C1,'nesting::C1.C1',$$$cl1.Basic);
            C1.C1$C1=C1$C1;
        }
        C1$C1.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return C1$C1;
    }
    $$c1.$init$C1$C1=$init$C1$C1;
    $init$C1$C1();
    
    //ClassDefinition C3 at nesting.ceylon (203:4-208:4)
    function C3$C1($$c3$C1){
        $init$C3$C1();
        if ($$c3$C1===undefined)$$c3$C1=new C3$C1.$$;
        $$c3$C1.$$outer=this;
        $$c1.C1$C1($$c3$C1);
        $$c3$C1.x$$nesting$C1$C1=$$c3$C1.x;
        
        //AttributeDeclaration x at nesting.ceylon (204:8-204:45)
        var x=$$$cl1.String("13",2);
        $$$cl1.defineAttr($$c3$C1,'x',function(){return x;});
        
        //MethodDefinition f at nesting.ceylon (205:8-207:8)
        function f(){
            return $$$cl1.StringBuilder().appendAll([$$c1.x.string,$$$cl1.String("-",1),$$c3$C1.x$$nesting$C1$C1.string,$$$cl1.String("-",1),$$c1.C1$C1().x.string,$$$cl1.String("-",1),$$c3$C1.x.string,$$$cl1.String("-",1),$$c1.C3$C1().x.string]).string;
        }
        $$c3$C1.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$c3$C1;
    }
    $$c1.C3$C1=C3$C1;
    function $init$C3$C1(){
        if (C3$C1.$$===undefined){
            $$$cl1.initTypeProto(C3$C1,'nesting::C1.C3',$$c1.C1$C1);
            C1.C3$C1=C3$C1;
        }
        C3$C1.$$.$$metamodel$$={$nm:'C3',$mt:'cls','super':{t:C1.C1$C1},'satisfies':[]};
        return C3$C1;
    }
    $$c1.$init$C3$C1=$init$C3$C1;
    $init$C3$C1();
    return $$c1;
}
exports.C1=C1;
function $init$C1(){
    if (C1.$$===undefined){
        $$$cl1.initTypeProto(C1,'nesting::C1',$$$cl1.Basic);
    }
    C1.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return C1;
}
exports.$init$C1=$init$C1;
$init$C1();

//ClassDefinition C2 at nesting.ceylon (210:0-221:0)
function C2($$c2){
    $init$C2();
    if ($$c2===undefined)$$c2=new C2.$$;
    C1($$c2);
    $$c2.C1$C1$$nesting$C1=$$c2.C1$C1;
    
    //AttributeDeclaration x at nesting.ceylon (211:4-211:32)
    var x=$$$cl1.String("2",1);
    $$$cl1.defineAttr($$c2,'x',function(){return x;});
    
    //ClassDefinition C2 at nesting.ceylon (212:4-220:4)
    function C2$C2($$c2$C2){
        $init$C2$C2();
        if ($$c2$C2===undefined)$$c2$C2=new C2$C2.$$;
        $$c2$C2.$$outer=this;
        $$c2.C1$C1$$nesting$C1($$c2$C2);
        $$c2$C2.x$$nesting$C1$C1=$$c2$C2.x;
        
        //AttributeDeclaration x at nesting.ceylon (213:8-213:37)
        var x=$$$cl1.String("22",2);
        $$$cl1.defineAttr($$c2$C2,'x',function(){return x;});
        
        //ClassDefinition C2 at nesting.ceylon (214:8-216:8)
        function C2$C2$C2($$c2$C2$C2){
            $init$C2$C2$C2();
            if ($$c2$C2$C2===undefined)$$c2$C2$C2=new C2$C2$C2.$$;
            $$c2$C2$C2.$$outer=this;
            $$c2.C3$C1($$c2$C2$C2);
            
            //AttributeDeclaration x at nesting.ceylon (215:12-215:42)
            var x=$$$cl1.String("222",3);
            $$$cl1.defineAttr($$c2$C2$C2,'x',function(){return x;});
            return $$c2$C2$C2;
        }
        $$c2$C2.C2$C2$C2=C2$C2$C2;
        function $init$C2$C2$C2(){
            if (C2$C2$C2.$$===undefined){
                $$$cl1.initTypeProto(C2$C2$C2,'nesting::C2.C2.C2',$$c2.C3$C1);
                C2.C2$C2.C2$C2$C2=C2$C2$C2;
            }
            C2$C2$C2.$$.$$metamodel$$={$nm:'C2',$mt:'cls','super':{t:C1.C3$C1},'satisfies':[]};
            return C2$C2$C2;
        }
        $$c2$C2.$init$C2$C2$C2=$init$C2$C2$C2;
        $init$C2$C2$C2();
        
        //MethodDefinition f at nesting.ceylon (217:8-219:8)
        function f(){
            return $$$cl1.StringBuilder().appendAll([$$c2.x.string,$$$cl1.String("-",1),$$c2.C1$C1().x.string,$$$cl1.String("-",1),$$c2$C2.x.string,$$$cl1.String("-",1),$$c2$C2.x$$nesting$C1$C1.string,$$$cl1.String("-",1),$$c2.C3$C1().x.string,$$$cl1.String("-",1),$$c2$C2.C2$C2$C2().x.string,$$$cl1.String("-",1),$$c2$C2.C2$C2$C2().f().string,$$$cl1.String("-",1),$$c2.C3$C1().f().string]).string;
        }
        $$c2$C2.f=f;
        f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$c2$C2;
    }
    $$c2.C2$C2=C2$C2;
    function $init$C2$C2(){
        if (C2$C2.$$===undefined){
            $$$cl1.initTypeProto(C2$C2,'nesting::C2.C2',$$c2.C1$C1$$nesting$C1);
            C2.C2$C2=C2$C2;
        }
        C2$C2.$$.$$metamodel$$={$nm:'C2',$mt:'cls','super':{t:C1.C1$C1},'satisfies':[]};
        return C2$C2;
    }
    $$c2.$init$C2$C2=$init$C2$C2;
    $init$C2$C2();
    return $$c2;
}
exports.C2=C2;
function $init$C2(){
    if (C2.$$===undefined){
        $$$cl1.initTypeProto(C2,'nesting::C2',C1);
    }
    C2.$$.$$metamodel$$={$nm:'C2',$mt:'cls','super':{t:C1},'satisfies':[]};
    return C2;
}
exports.$init$C2=$init$C2;
$init$C2();

//MethodDefinition test at nesting.ceylon (223:0-253:0)
function test(){
    outr($$$cl1.String("Hello",5));
    $$$c2.check(Holder($$$cl1.String("ok",2)).get().string.equals($$$cl1.String("ok",2)),$$$cl1.String("holder(ok)",10));
    $$$c2.check(Holder($$$cl1.String("ok",2)).string.equals($$$cl1.String("ok",2)),$$$cl1.String("holder.string",13));
    $$$c2.check(Wrapper().get().string.equals($$$cl1.String("100",3)),$$$cl1.String("wrapper 1",9));
    $$$c2.check(Wrapper().string.equals($$$cl1.String("100",3)),$$$cl1.String("wrapper 2",9));
    $$$c2.check(Unwrapper().get().string.equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 1",11));
    $$$c2.check(Unwrapper().o.string.equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 2",11));
    $$$c2.check(Unwrapper().string.equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 3",11));
    $$$c2.check($$$cl1.isOfType(producer(),{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}}}),$$$cl1.StringBuilder().appendAll([$$$cl1.String("function 1 is ",14),$$$cl1.className($$$cl1.$JsCallable(producer(),/*Callable from Invocation +  [InvocationExpression] <Object()> (232:73-232:82)
|  +  [BaseMemberExpression] <Object()()> (232:73-232:80) => Method[producer:Object()]
|  |  + producer [Identifier] (232:73-232:80)
|  |  +  [InferredTypeArguments]
|  + () [PositionalArgumentList] (232:81-232:82)
*/[],{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}})).string]).string);
    $$$c2.check($$$cl1.isOfType(producer()(),{t:$$$cl1.Integer}),$$$cl1.String("function 2",10));
    $$$c2.check((123).equals(producer()()),$$$cl1.String("function 3",10));
    $$$c2.check($$$cl1.String("something",9).equals(returner($$$cl1.String("something",9))()),$$$cl1.String("function 4",10));
    $$$c2.check(A().B$A().C$B$A().foobar().equals($$$cl1.String("foo",3)),$$$cl1.String("foobar",6));
    $$$c2.check(A().B$A().C$B$A().quxx().equals($$$cl1.String("qux",3)),$$$cl1.String("quxx",4));
    $$$c2.check(A().baz().equals($$$cl1.String("foo",3)),$$$cl1.String("baz",3));
    $$$c2.check(O().test1().equals($$$cl1.String("hello",5)),$$$cl1.String("method instantiating inner class",32));
    $$$c2.check(O().test2().equals($$$cl1.String("hello",5)),$$$cl1.String("method accessing inner object",29));
    $$$c2.check(O().test3().equals($$$cl1.String("hello",5)),$$$cl1.String("method deriving inner interface",31));
    $$$c2.check(OuterC1().tst().equals($$$cl1.String("OuterC1.A.tst()",15)),$$$cl1.String("",0));
    $$$c2.check(outerf().equals($$$cl1.String("outerf.A.tst()",14)),$$$cl1.String("",0));
    $$$c2.check(OuterC2().tst().equals($$$cl1.String("OuterC2.A.tst()",15)),$$$cl1.String("",0));
    Outer($$$cl1.String("Hello",5));
    $$$c2.check(NameTest().f().equals($$$cl1.String("1234",4)),$$$cl1.String("Nested class with same name",27));
    $$$c2.check(getNameTest().f().equals($$$cl1.String("1234",4)),$$$cl1.String("Nested object with same name",28));
    $$$c2.check(C1().C3$C1().f().equals($$$cl1.String("1-11-11-13-13",13)),$$$cl1.String("Several nested classes with same name (1)",41));
    $$$c2.check(C2().C2$C2().f().equals($$$cl1.String("2-11-22-11-13-222-2-11-11-222-13-2-11-11-13-13",46)),$$$cl1.String("Several nested classes with same name (2)",41));
    testRefinement();
    testRefinement2();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//ClassDefinition X at refinement.ceylon (4:0-17:0)
function X($$x){
    $init$X();
    if ($$x===undefined)$$x=new X.$$;
    
    //ClassDefinition RefineTest1 at refinement.ceylon (5:4-16:4)
    function RefineTest1$X($$refineTest1$X){
        $init$RefineTest1$X();
        if ($$refineTest1$X===undefined)$$refineTest1$X=new RefineTest1$X.$$;
        $$refineTest1$X.$$outer=this;
        
        //ClassDefinition Inner at refinement.ceylon (6:8-12:8)
        function Inner$RefineTest1$X($$inner$RefineTest1$X){
            $init$Inner$RefineTest1$X();
            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new Inner$RefineTest1$X.$$;
            $$inner$RefineTest1$X.$$outer=this;
            
            //AttributeDeclaration origin at refinement.ceylon (7:12-7:54)
            var origin=$$$cl1.String("RefineTest1.Inner",17);
            $$$cl1.defineAttr($$inner$RefineTest1$X,'origin',function(){return origin;});
            
            //MethodDefinition x at refinement.ceylon (8:12-10:12)
            function x(){
                return $$$cl1.String("x and ",6).plus($$inner$RefineTest1$X.y());
            }
            $$inner$RefineTest1$X.x=x;
            x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            return $$inner$RefineTest1$X;
        }
        $$refineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
        function $init$Inner$RefineTest1$X(){
            if (Inner$RefineTest1$X.$$===undefined){
                $$$cl1.initTypeProto(Inner$RefineTest1$X,'nesting::X.RefineTest1.Inner',$$$cl1.Basic);
                X.RefineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
            }
            Inner$RefineTest1$X.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
            return Inner$RefineTest1$X;
        }
        $$refineTest1$X.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
        $init$Inner$RefineTest1$X();
        
        //MethodDefinition outerx at refinement.ceylon (13:8-15:8)
        function outerx(){
            return $$refineTest1$X.Inner$RefineTest1$X().x();
        }
        $$refineTest1$X.outerx=outerx;
        outerx.$$metamodel$$={$nm:'outerx',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//outerx.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$refineTest1$X;
    }
    $$x.RefineTest1$X=RefineTest1$X;
    function $init$RefineTest1$X(){
        if (RefineTest1$X.$$===undefined){
            $$$cl1.initTypeProto(RefineTest1$X,'nesting::X.RefineTest1',$$$cl1.Basic);
            X.RefineTest1$X=RefineTest1$X;
        }
        RefineTest1$X.$$.$$metamodel$$={$nm:'RefineTest1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return RefineTest1$X;
    }
    $$x.$init$RefineTest1$X=$init$RefineTest1$X;
    $init$RefineTest1$X();
    return $$x;
}
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl1.initTypeProto(X,'nesting::X',$$$cl1.Basic);
    }
    X.$$.$$metamodel$$={$nm:'X',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDefinition RefineTest2 at refinement.ceylon (20:0-24:0)
function RefineTest2($$refineTest2){
    $init$RefineTest2();
    if ($$refineTest2===undefined)$$refineTest2=new RefineTest2.$$;
    
    //ClassDefinition Inner at refinement.ceylon (21:4-23:4)
    function Inner$RefineTest2($$inner$RefineTest2){
        $init$Inner$RefineTest2();
        if ($$inner$RefineTest2===undefined)$$inner$RefineTest2=new Inner$RefineTest2.$$;
        $$inner$RefineTest2.$$outer=this;
        
        //MethodDefinition hello at refinement.ceylon (22:8-22:71)
        function hello(){
            return $$$cl1.String("hello from RefineTest2.Inner",28);
        }
        $$inner$RefineTest2.hello=hello;
        hello.$$metamodel$$={$nm:'hello',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//hello.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$inner$RefineTest2;
    }
    $$refineTest2.Inner$RefineTest2=Inner$RefineTest2;
    function $init$Inner$RefineTest2(){
        if (Inner$RefineTest2.$$===undefined){
            $$$cl1.initTypeProto(Inner$RefineTest2,'nesting::RefineTest2.Inner',$$$cl1.Basic);
            RefineTest2.Inner$RefineTest2=Inner$RefineTest2;
        }
        Inner$RefineTest2.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return Inner$RefineTest2;
    }
    $$refineTest2.$init$Inner$RefineTest2=$init$Inner$RefineTest2;
    $init$Inner$RefineTest2();
    return $$refineTest2;
}
exports.RefineTest2=RefineTest2;
function $init$RefineTest2(){
    if (RefineTest2.$$===undefined){
        $$$cl1.initTypeProto(RefineTest2,'nesting::RefineTest2',$$$cl1.Basic);
    }
    RefineTest2.$$.$$metamodel$$={$nm:'RefineTest2',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return RefineTest2;
}
exports.$init$RefineTest2=$init$RefineTest2;
$init$RefineTest2();

//ClassDefinition RefineTest3 at refinement.ceylon (27:0-33:0)
function RefineTest3($$refineTest3){
    $init$RefineTest3();
    if ($$refineTest3===undefined)$$refineTest3=new RefineTest3.$$;
    
    //ClassDefinition Inner at refinement.ceylon (28:4-32:4)
    function Inner$RefineTest3($$inner$RefineTest3){
        $init$Inner$RefineTest3();
        if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new Inner$RefineTest3.$$;
        $$inner$RefineTest3.$$outer=this;
        
        //MethodDefinition x at refinement.ceylon (29:8-31:8)
        function x(){
            return $$$cl1.String("x",1);
        }
        $$inner$RefineTest3.x=x;
        x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$inner$RefineTest3;
    }
    $$refineTest3.Inner$RefineTest3=Inner$RefineTest3;
    function $init$Inner$RefineTest3(){
        if (Inner$RefineTest3.$$===undefined){
            $$$cl1.initTypeProto(Inner$RefineTest3,'nesting::RefineTest3.Inner',$$$cl1.Basic);
            RefineTest3.Inner$RefineTest3=Inner$RefineTest3;
        }
        Inner$RefineTest3.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return Inner$RefineTest3;
    }
    $$refineTest3.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
    $init$Inner$RefineTest3();
    return $$refineTest3;
}
exports.RefineTest3=RefineTest3;
function $init$RefineTest3(){
    if (RefineTest3.$$===undefined){
        $$$cl1.initTypeProto(RefineTest3,'nesting::RefineTest3',$$$cl1.Basic);
    }
    RefineTest3.$$.$$metamodel$$={$nm:'RefineTest3',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return RefineTest3;
}
exports.$init$RefineTest3=$init$RefineTest3;
$init$RefineTest3();

//ClassDefinition Y at refinement.ceylon (35:0-47:0)
function Y($$y){
    $init$Y();
    if ($$y===undefined)$$y=new Y.$$;
    X($$y);
    
    //ClassDefinition SubRef1 at refinement.ceylon (36:4-46:4)
    function SubRef1$Y($$subRef1$Y){
        $init$SubRef1$Y();
        if ($$subRef1$Y===undefined)$$subRef1$Y=new SubRef1$Y.$$;
        $$subRef1$Y.$$outer=this;
        $$y.RefineTest1$X($$subRef1$Y);
        $$subRef1$Y.Inner$RefineTest1$X$$nesting$X$RefineTest1=$$subRef1$Y.Inner$RefineTest1$X;
        
        //ClassDefinition Inner at refinement.ceylon (37:6-45:6)
        function Inner$RefineTest1$X($$inner$RefineTest1$X){
            $init$Inner$RefineTest1$X();
            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new Inner$RefineTest1$X.$$;
            $$inner$RefineTest1$X.$$outer=this;
            $$subRef1$Y.Inner$RefineTest1$X$$nesting$X$RefineTest1($$inner$RefineTest1$X);
            $$inner$RefineTest1$X.x$$nesting$X$RefineTest1$Inner=$$inner$RefineTest1$X.x;
            
            //AttributeDeclaration suborigin at refinement.ceylon (38:10-38:51)
            var suborigin=$$$cl1.String("SubRef1.Inner",13);
            $$$cl1.defineAttr($$inner$RefineTest1$X,'suborigin',function(){return suborigin;});
            
            //MethodDefinition x at refinement.ceylon (39:10-41:10)
            function x(){
                return $$$cl1.String("REFINED ",8).plus($$inner$RefineTest1$X.x$$nesting$X$RefineTest1$Inner());
            }
            $$inner$RefineTest1$X.x=x;
            x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            
            //MethodDefinition y at refinement.ceylon (42:10-44:10)
            function y(){
                return $$$cl1.String("y",1);
            }
            $$inner$RefineTest1$X.y=y;
            y.$$metamodel$$={$nm:'y',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//y.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            return $$inner$RefineTest1$X;
        }
        $$subRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
        function $init$Inner$RefineTest1$X(){
            if (Inner$RefineTest1$X.$$===undefined){
                $$$cl1.initTypeProto(Inner$RefineTest1$X,'nesting::Y.SubRef1.Inner',$$subRef1$Y.Inner$RefineTest1$X$$nesting$X$RefineTest1);
                Y.SubRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
            }
            Inner$RefineTest1$X.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:X.RefineTest1$X.Inner$RefineTest1$X},'satisfies':[]};
            return Inner$RefineTest1$X;
        }
        $$subRef1$Y.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
        $init$Inner$RefineTest1$X();
        return $$subRef1$Y;
    }
    $$y.SubRef1$Y=SubRef1$Y;
    function $init$SubRef1$Y(){
        if (SubRef1$Y.$$===undefined){
            $$$cl1.initTypeProto(SubRef1$Y,'nesting::Y.SubRef1',$$y.RefineTest1$X);
            Y.SubRef1$Y=SubRef1$Y;
        }
        SubRef1$Y.$$.$$metamodel$$={$nm:'SubRef1',$mt:'cls','super':{t:X.RefineTest1$X},'satisfies':[]};
        return SubRef1$Y;
    }
    $$y.$init$SubRef1$Y=$init$SubRef1$Y;
    $init$SubRef1$Y();
    return $$y;
}
exports.Y=Y;
function $init$Y(){
    if (Y.$$===undefined){
        $$$cl1.initTypeProto(Y,'nesting::Y',X);
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
    
    //MethodDefinition x at refinement.ceylon (50:4-52:4)
    function x(){
        return $$subRef2.Inner$RefineTest2().hello();
    }
    $$subRef2.x=x;
    x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$subRef2;
}
exports.SubRef2=SubRef2;
function $init$SubRef2(){
    if (SubRef2.$$===undefined){
        $$$cl1.initTypeProto(SubRef2,'nesting::SubRef2',RefineTest2);
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
    
    //MethodDefinition x at refinement.ceylon (56:4-58:4)
    function x(){
        return $$subRef3.Inner$RefineTest3().x();
    }
    $$subRef3.x=x;
    x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$subRef3;
}
exports.SubRef3=SubRef3;
function $init$SubRef3(){
    if (SubRef3.$$===undefined){
        $$$cl1.initTypeProto(SubRef3,'nesting::SubRef3',RefineTest3);
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
    $$subRef31.Inner$RefineTest3$$nesting$RefineTest3=$$subRef31.Inner$RefineTest3;
    
    //ClassDefinition Inner at refinement.ceylon (61:4-63:4)
    function Inner$RefineTest3($$inner$RefineTest3){
        $init$Inner$RefineTest3();
        if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new Inner$RefineTest3.$$;
        $$inner$RefineTest3.$$outer=this;
        $$subRef31.Inner$RefineTest3$$nesting$RefineTest3($$inner$RefineTest3);
        
        //MethodDefinition x at refinement.ceylon (62:8-62:51)
        function x(){
            return $$$cl1.String("equis",5);
        }
        $$inner$RefineTest3.x=x;
        x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$inner$RefineTest3;
    }
    $$subRef31.Inner$RefineTest3=Inner$RefineTest3;
    function $init$Inner$RefineTest3(){
        if (Inner$RefineTest3.$$===undefined){
            $$$cl1.initTypeProto(Inner$RefineTest3,'nesting::SubRef31.Inner',$$subRef31.Inner$RefineTest3$$nesting$RefineTest3);
            SubRef31.Inner$RefineTest3=Inner$RefineTest3;
        }
        Inner$RefineTest3.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:RefineTest3.Inner$RefineTest3},'satisfies':[]};
        return Inner$RefineTest3;
    }
    $$subRef31.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
    $init$Inner$RefineTest3();
    return $$subRef31;
}
exports.SubRef31=SubRef31;
function $init$SubRef31(){
    if (SubRef31.$$===undefined){
        $$$cl1.initTypeProto(SubRef31,'nesting::SubRef31',SubRef3);
    }
    SubRef31.$$.$$metamodel$$={$nm:'SubRef31',$mt:'cls','super':{t:SubRef3},'satisfies':[]};
    return SubRef31;
}
exports.$init$SubRef31=$init$SubRef31;
$init$SubRef31();

//MethodDefinition testRefinement at refinement.ceylon (66:0-76:0)
function testRefinement(){
    
    //AttributeDeclaration c1 at refinement.ceylon (67:4-67:36)
    var c1$908=Y().SubRef1$Y().Inner$RefineTest1$X();
    $$$c2.check($$$cl1.className(c1$908).equals($$$cl1.String("nesting::Y.SubRef1.Inner",24)),$$$cl1.String("classname is ",13).plus($$$cl1.className(c1$908)));
    $$$c2.check(c1$908.origin.equals($$$cl1.String("RefineTest1.Inner",17)),$$$cl1.String("refinement [1]",14));
    $$$c2.check(c1$908.suborigin.equals($$$cl1.String("SubRef1.Inner",13)),$$$cl1.String("refinement [2]",14));
    $$$c2.check(c1$908.x().equals($$$cl1.String("REFINED x and y",15)),$$$cl1.String("refinement [3]",14));
    $$$c2.check(c1$908.x().equals(Y().SubRef1$Y().outerx()),$$$cl1.String("refinement [4]",14));
    $$$c2.check(SubRef2().x().equals($$$cl1.String("hello from RefineTest2.Inner",28)),$$$cl1.String("refinement [5]",14));
    $$$c2.check(SubRef3().x().equals($$$cl1.String("x",1)),$$$cl1.String("refinement [6]",14));
    $$$c2.check(SubRef31().x().equals($$$cl1.String("equis",5)),$$$cl1.String("refinement [7]",14));
};testRefinement.$$metamodel$$={$nm:'testRefinement',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testRefinement.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//ClassDefinition X2 at refinement2.ceylon (4:0-18:0)
function X2(a$909, $$x2){
    $init$X2();
    if ($$x2===undefined)$$x2=new X2.$$;
    $$x2.a$909=a$909;
    
    //ClassDefinition RefineTest1 at refinement2.ceylon (5:4-17:4)
    function RefineTest1$X2(b$910, $$refineTest1$X2){
        $init$RefineTest1$X2();
        if ($$refineTest1$X2===undefined)$$refineTest1$X2=new RefineTest1$X2.$$;
        $$refineTest1$X2.$$outer=this;
        $$refineTest1$X2.b$910=b$910;
        
        //ClassDefinition Inner at refinement2.ceylon (6:8-13:8)
        function Inner$RefineTest1$X2(c$911, $$targs$$,$$inner$RefineTest1$X2){
            $init$Inner$RefineTest1$X2();
            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new Inner$RefineTest1$X2.$$;
            $$$cl1.set_type_args($$inner$RefineTest1$X2,$$targs$$);
            $$inner$RefineTest1$X2.$$outer=this;
            $$inner$RefineTest1$X2.c$911=c$911;
            
            //AttributeDeclaration origin at refinement2.ceylon (8:12-8:62)
            var origin=$$$cl1.StringBuilder().appendAll([$$$cl1.String("RefineTest1.Inner (",19),c$911.string,$$$cl1.String(")",1)]).string;
            $$$cl1.defineAttr($$inner$RefineTest1$X2,'origin',function(){return origin;});
            
            //MethodDefinition x at refinement2.ceylon (9:12-11:12)
            function x(){
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("x and ",6),$$inner$RefineTest1$X2.y().string,$$$cl1.String(" and a:",7),a$909.string,$$$cl1.String(", b:",4),b$910.string,$$$cl1.String(", c:",4),c$911.string,$$$cl1.String(".",1)]).string;
            }
            $$inner$RefineTest1$X2.x=x;
            x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            return $$inner$RefineTest1$X2;
        }
        $$refineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
        function $init$Inner$RefineTest1$X2(){
            if (Inner$RefineTest1$X2.$$===undefined){
                $$$cl1.initTypeProto(Inner$RefineTest1$X2,'nesting::X2.RefineTest1.Inner',$$$cl1.Basic);
                X2.RefineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
            }
            Inner$RefineTest1$X2.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},$tp:{Element:{'satisfies':[{t:$$$cl1.Object}]}},'satisfies':[]};
            return Inner$RefineTest1$X2;
        }
        $$refineTest1$X2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
        $init$Inner$RefineTest1$X2();
        
        //MethodDefinition outerx at refinement2.ceylon (14:8-16:8)
        function outerx(){
            return $$refineTest1$X2.Inner$RefineTest1$X2(a$909.uppercased,{Element:{t:$$$cl1.String}}).x();
        }
        $$refineTest1$X2.outerx=outerx;
        outerx.$$metamodel$$={$nm:'outerx',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//outerx.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$refineTest1$X2;
    }
    $$x2.RefineTest1$X2=RefineTest1$X2;
    function $init$RefineTest1$X2(){
        if (RefineTest1$X2.$$===undefined){
            $$$cl1.initTypeProto(RefineTest1$X2,'nesting::X2.RefineTest1',$$$cl1.Basic);
            X2.RefineTest1$X2=RefineTest1$X2;
        }
        RefineTest1$X2.$$.$$metamodel$$={$nm:'RefineTest1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return RefineTest1$X2;
    }
    $$x2.$init$RefineTest1$X2=$init$RefineTest1$X2;
    $init$RefineTest1$X2();
    return $$x2;
}
exports.X2=X2;
function $init$X2(){
    if (X2.$$===undefined){
        $$$cl1.initTypeProto(X2,'nesting::X2',$$$cl1.Basic);
    }
    X2.$$.$$metamodel$$={$nm:'X2',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return X2;
}
exports.$init$X2=$init$X2;
$init$X2();

//ClassDefinition RefineTest4 at refinement2.ceylon (21:0-25:0)
function RefineTest4(d$912, $$refineTest4){
    $init$RefineTest4();
    if ($$refineTest4===undefined)$$refineTest4=new RefineTest4.$$;
    
    //ClassDefinition Inner at refinement2.ceylon (22:4-24:4)
    function Inner$RefineTest4(e$913, $$inner$RefineTest4){
        $init$Inner$RefineTest4();
        if ($$inner$RefineTest4===undefined)$$inner$RefineTest4=new Inner$RefineTest4.$$;
        $$inner$RefineTest4.$$outer=this;
        $$inner$RefineTest4.e$913=e$913;
        
        //MethodDefinition hello at refinement2.ceylon (23:8-23:83)
        function hello(){
            return $$$cl1.StringBuilder().appendAll([$$$cl1.String("hello from RefineTest2.Inner with ",34),e$913.string,$$$cl1.String(".",1)]).string;
        }
        $$inner$RefineTest4.hello=hello;
        hello.$$metamodel$$={$nm:'hello',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//hello.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$inner$RefineTest4;
    }
    $$refineTest4.Inner$RefineTest4=Inner$RefineTest4;
    function $init$Inner$RefineTest4(){
        if (Inner$RefineTest4.$$===undefined){
            $$$cl1.initTypeProto(Inner$RefineTest4,'nesting::RefineTest4.Inner',$$$cl1.Basic);
            RefineTest4.Inner$RefineTest4=Inner$RefineTest4;
        }
        Inner$RefineTest4.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return Inner$RefineTest4;
    }
    $$refineTest4.$init$Inner$RefineTest4=$init$Inner$RefineTest4;
    $init$Inner$RefineTest4();
    return $$refineTest4;
}
exports.RefineTest4=RefineTest4;
function $init$RefineTest4(){
    if (RefineTest4.$$===undefined){
        $$$cl1.initTypeProto(RefineTest4,'nesting::RefineTest4',$$$cl1.Basic);
    }
    RefineTest4.$$.$$metamodel$$={$nm:'RefineTest4',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return RefineTest4;
}
exports.$init$RefineTest4=$init$RefineTest4;
$init$RefineTest4();

//ClassDefinition RefineTest5 at refinement2.ceylon (28:0-34:0)
function RefineTest5(f$914, $$refineTest5){
    $init$RefineTest5();
    if ($$refineTest5===undefined)$$refineTest5=new RefineTest5.$$;
    $$refineTest5.f$914=f$914;
    
    //ClassDefinition Inner at refinement2.ceylon (29:4-33:4)
    function Inner$RefineTest5(g$915, $$inner$RefineTest5){
        $init$Inner$RefineTest5();
        if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new Inner$RefineTest5.$$;
        $$inner$RefineTest5.$$outer=this;
        $$inner$RefineTest5.g$915=g$915;
        
        //MethodDefinition x at refinement2.ceylon (30:8-32:8)
        function x(){
            return g$915.repeat(f$914);
        }
        $$inner$RefineTest5.x=x;
        x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$inner$RefineTest5;
    }
    $$refineTest5.Inner$RefineTest5=Inner$RefineTest5;
    function $init$Inner$RefineTest5(){
        if (Inner$RefineTest5.$$===undefined){
            $$$cl1.initTypeProto(Inner$RefineTest5,'nesting::RefineTest5.Inner',$$$cl1.Basic);
            RefineTest5.Inner$RefineTest5=Inner$RefineTest5;
        }
        Inner$RefineTest5.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return Inner$RefineTest5;
    }
    $$refineTest5.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
    $init$Inner$RefineTest5();
    return $$refineTest5;
}
exports.RefineTest5=RefineTest5;
function $init$RefineTest5(){
    if (RefineTest5.$$===undefined){
        $$$cl1.initTypeProto(RefineTest5,'nesting::RefineTest5',$$$cl1.Basic);
    }
    RefineTest5.$$.$$metamodel$$={$nm:'RefineTest5',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return RefineTest5;
}
exports.$init$RefineTest5=$init$RefineTest5;
$init$RefineTest5();

//ClassDefinition Y2 at refinement2.ceylon (36:0-49:0)
function Y2(h$916, $$y2){
    $init$Y2();
    if ($$y2===undefined)$$y2=new Y2.$$;
    $$y2.h$916=h$916;
    X2(h$916,$$y2);
    
    //ClassDefinition SubRef1 at refinement2.ceylon (37:4-48:4)
    function SubRef1$Y2(d$917, $$subRef1$Y2){
        $init$SubRef1$Y2();
        if ($$subRef1$Y2===undefined)$$subRef1$Y2=new SubRef1$Y2.$$;
        $$subRef1$Y2.$$outer=this;
        $$subRef1$Y2.d$917=d$917;
        $$y2.RefineTest1$X2((1),$$subRef1$Y2);
        $$subRef1$Y2.Inner$RefineTest1$X2$$nesting$X2$RefineTest1=$$subRef1$Y2.Inner$RefineTest1$X2;
        
        //ClassDefinition Inner at refinement2.ceylon (38:6-47:6)
        function Inner$RefineTest1$X2(d2$918, $$targs$$,$$inner$RefineTest1$X2){
            $init$Inner$RefineTest1$X2();
            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new Inner$RefineTest1$X2.$$;
            $$$cl1.set_type_args($$inner$RefineTest1$X2,$$targs$$);
            $$inner$RefineTest1$X2.$$outer=this;
            $$inner$RefineTest1$X2.d2$918=d2$918;
            $$subRef1$Y2.Inner$RefineTest1$X2$$nesting$X2$RefineTest1(d2$918,{Element:$$inner$RefineTest1$X2.$$targs$$.Element},$$inner$RefineTest1$X2);
            $$inner$RefineTest1$X2.x$$nesting$X2$RefineTest1$Inner=$$inner$RefineTest1$X2.x;
            
            //AttributeDeclaration suborigin at refinement2.ceylon (40:10-40:51)
            var suborigin=$$$cl1.String("SubRef1.Inner",13);
            $$$cl1.defineAttr($$inner$RefineTest1$X2,'suborigin',function(){return suborigin;});
            
            //MethodDefinition x at refinement2.ceylon (41:10-43:10)
            function x(){
                return $$$cl1.String("REFINED ",8).plus($$inner$RefineTest1$X2.x$$nesting$X2$RefineTest1$Inner());
            }
            $$inner$RefineTest1$X2.x=x;
            x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            
            //MethodDefinition y at refinement2.ceylon (44:10-46:10)
            function y(){
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("y",1),h$916.string,$$$cl1.String(",d:",3),d$917.string,$$$cl1.String(",d2:",4),d2$918.string,$$$cl1.String(".",1)]).string;
            }
            $$inner$RefineTest1$X2.y=y;
            y.$$metamodel$$={$nm:'y',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//y.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
            return $$inner$RefineTest1$X2;
        }
        $$subRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
        function $init$Inner$RefineTest1$X2(){
            if (Inner$RefineTest1$X2.$$===undefined){
                $$$cl1.initTypeProto(Inner$RefineTest1$X2,'nesting::Y2.SubRef1.Inner',$$subRef1$Y2.Inner$RefineTest1$X2$$nesting$X2$RefineTest1);
                Y2.SubRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
            }
            Inner$RefineTest1$X2.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:X2.RefineTest1$X2.Inner$RefineTest1$X2,a:{Element:'Element'}},$tp:{Element:{'satisfies':[{t:$$$cl1.Object}]}},'satisfies':[]};
            return Inner$RefineTest1$X2;
        }
        $$subRef1$Y2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
        $init$Inner$RefineTest1$X2();
        return $$subRef1$Y2;
    }
    $$y2.SubRef1$Y2=SubRef1$Y2;
    function $init$SubRef1$Y2(){
        if (SubRef1$Y2.$$===undefined){
            $$$cl1.initTypeProto(SubRef1$Y2,'nesting::Y2.SubRef1',$$y2.RefineTest1$X2);
            Y2.SubRef1$Y2=SubRef1$Y2;
        }
        SubRef1$Y2.$$.$$metamodel$$={$nm:'SubRef1',$mt:'cls','super':{t:X2.RefineTest1$X2},'satisfies':[]};
        return SubRef1$Y2;
    }
    $$y2.$init$SubRef1$Y2=$init$SubRef1$Y2;
    $init$SubRef1$Y2();
    return $$y2;
}
exports.Y2=Y2;
function $init$Y2(){
    if (Y2.$$===undefined){
        $$$cl1.initTypeProto(Y2,'nesting::Y2',X2);
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
    RefineTest4($$$cl1.String("t4",2),$$subRef4);
    
    //MethodDefinition x at refinement2.ceylon (52:4-54:4)
    function x(){
        return $$subRef4.Inner$RefineTest4((5)).hello();
    }
    $$subRef4.x=x;
    x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$subRef4;
}
exports.SubRef4=SubRef4;
function $init$SubRef4(){
    if (SubRef4.$$===undefined){
        $$$cl1.initTypeProto(SubRef4,'nesting::SubRef4',RefineTest4);
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
    
    //MethodDefinition x at refinement2.ceylon (58:4-60:4)
    function x(){
        return $$subRef5.Inner$RefineTest5($$$cl1.String("sr5",3)).x();
    }
    $$subRef5.x=x;
    x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    return $$subRef5;
}
exports.SubRef5=SubRef5;
function $init$SubRef5(){
    if (SubRef5.$$===undefined){
        $$$cl1.initTypeProto(SubRef5,'nesting::SubRef5',RefineTest5);
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
    $$subRef51.Inner$RefineTest5$$nesting$RefineTest5=$$subRef51.Inner$RefineTest5;
    
    //ClassDefinition Inner at refinement2.ceylon (63:4-65:4)
    function Inner$RefineTest5(subg55$919, $$inner$RefineTest5){
        $init$Inner$RefineTest5();
        if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new Inner$RefineTest5.$$;
        $$inner$RefineTest5.$$outer=this;
        $$inner$RefineTest5.subg55$919=subg55$919;
        $$subRef51.Inner$RefineTest5$$nesting$RefineTest5(subg55$919,$$inner$RefineTest5);
        
        //MethodDefinition x at refinement2.ceylon (64:8-64:62)
        function x(){
            return $$$cl1.StringBuilder().appendAll([$$$cl1.String("equis",5),subg55$919.string,$$$cl1.String(".",1)]).string;
        }
        $$inner$RefineTest5.x=x;
        x.$$metamodel$$={$nm:'x',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//x.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
        return $$inner$RefineTest5;
    }
    $$subRef51.Inner$RefineTest5=Inner$RefineTest5;
    function $init$Inner$RefineTest5(){
        if (Inner$RefineTest5.$$===undefined){
            $$$cl1.initTypeProto(Inner$RefineTest5,'nesting::SubRef51.Inner',$$subRef51.Inner$RefineTest5$$nesting$RefineTest5);
            SubRef51.Inner$RefineTest5=Inner$RefineTest5;
        }
        Inner$RefineTest5.$$.$$metamodel$$={$nm:'Inner',$mt:'cls','super':{t:RefineTest5.Inner$RefineTest5},'satisfies':[]};
        return Inner$RefineTest5;
    }
    $$subRef51.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
    $init$Inner$RefineTest5();
    return $$subRef51;
}
exports.SubRef51=SubRef51;
function $init$SubRef51(){
    if (SubRef51.$$===undefined){
        $$$cl1.initTypeProto(SubRef51,'nesting::SubRef51',SubRef5);
    }
    SubRef51.$$.$$metamodel$$={$nm:'SubRef51',$mt:'cls','super':{t:SubRef5},'satisfies':[]};
    return SubRef51;
}
exports.$init$SubRef51=$init$SubRef51;
$init$SubRef51();

//MethodDefinition testRefinement2 at refinement2.ceylon (68:0-78:0)
function testRefinement2(){
    
    //AttributeDeclaration c1 at refinement2.ceylon (69:4-69:54)
    var c1$920=Y2($$$cl1.String("y2",2)).SubRef1$Y2((99)).Inner$RefineTest1$X2($$$cl1.String("with parm",9),{Element:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.className(c1$920).equals($$$cl1.String("nesting::Y2.SubRef1.Inner",25)),$$$cl1.String("classname is ",13).plus($$$cl1.className(c1$920)));
    $$$c2.check(c1$920.origin.equals($$$cl1.String("RefineTest1.Inner (with parm)",29)),$$$cl1.String("refinement [1] ",15).plus(c1$920.origin));
    $$$c2.check(c1$920.suborigin.equals($$$cl1.String("SubRef1.Inner",13)),$$$cl1.String("refinement [2] ",15).plus(c1$920.suborigin));
    $$$c2.check(c1$920.x().equals($$$cl1.String("REFINED x and yy2,d:99,d2:with parm. and a:y2, b:1, c:with parm.",64)),$$$cl1.String("refinement [3] ",15).plus(c1$920.x()));
    $$$c2.check(Y2($$$cl1.String("y3",2)).SubRef1$Y2((10)).outerx().equals($$$cl1.String("REFINED x and yy3,d:10,d2:Y3. and a:y3, b:1, c:Y3.",50)),$$$cl1.String("refinement [4] ",15).plus(Y2($$$cl1.String("y3",2)).SubRef1$Y2((10)).outerx()));
    $$$c2.check(SubRef4().x().equals($$$cl1.String("hello from RefineTest2.Inner with 5.",36)),$$$cl1.String("refinement [5] ",15).plus(SubRef4().x()));
    $$$c2.check(SubRef5().x().equals($$$cl1.String("sr5sr5sr5sr5sr5sr5",18)),$$$cl1.String("refinement [6] ",15).plus(SubRef5().x()));
    $$$c2.check(SubRef51().x().equals($$$cl1.String("equissr5.",9)),$$$cl1.String("refinement [7] ",15).plus(SubRef51().x()));
};testRefinement2.$$metamodel$$={$nm:'testRefinement2',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testRefinement2.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
