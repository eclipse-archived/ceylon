(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"nesting","$mod-version":"0.1","nesting":{"Y2":{"super":{"$pk":"nesting","$nm":"X2"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"h"}],"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"d"}],"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"d2"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"d2":{"$t":{"$nm":"Element"},"$mt":"attr","$nm":"d2"},"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$at":{"d":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"d"}},"$nm":"SubRef1"}},"$at":{"h":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"h"}},"$nm":"Y2"},"SubRef51":{"super":{"$pk":"nesting","$nm":"SubRef5"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"subg55"}],"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"subg55":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"subg55"}},"$nm":"Inner"}},"$nm":"SubRef51"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"baz":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$c":{"Baz":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$nm":"Baz"}},"$nm":"baz"}},"$c":{"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"foobar":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"foobar"},"quxx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"quxx"}},"$nm":"C"}},"$at":{"qux":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"qux"}},"$nm":"B"}},"$at":{"foo":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"foo"}},"$nm":"A"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Unwrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$an":{"shared":[]},"$nm":"o"}},"$nm":"Unwrapper"},"SubRef2":{"super":{"$pk":"nesting","$nm":"RefineTest2"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef2"},"Wrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Wrapper"},"O":{"$i":{"InnerInterface":{"$mt":"ifc","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerInterface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"test1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test1"},"test2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test2"},"test3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test3","$o":{"obj":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"nesting","$nm":"InnerInterface"}],"$mt":"obj","$nm":"obj"}}}},"$c":{"InnerClass":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerClass"}},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"s"}},"$nm":"O","$o":{"innerObject":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"innerObject"}}},"outr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$m":{"inr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$nm":"inr"}},"$nm":"outr"},"testRefinement2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement2"},"Holder":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"o"}],"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Holder"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C3":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"x"}},"$nm":"C3"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"},"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C3"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"},"Y":{"super":{"$pk":"nesting","$nm":"X"},"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y"},"X":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X"},"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"},"outerf":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"outerf"},"Issue60Abs":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"abstract":[],"shared":[]},"$c":{"Inner60Abs":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$nm":"Inner60Abs"}},"$nm":"Issue60Abs"},"X2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"a"}],"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"c"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"c":{"$t":{"$nm":"Element"},"$mt":"attr","$nm":"c"},"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$at":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"b"}},"$nm":"RefineTest1"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"a"}},"$nm":"X2"},"Issue60":{"super":{"$pk":"nesting","$nm":"Issue60Abs"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner60":{"super":{"$pk":"nesting","$nm":"Inner60Abs"},"$mt":"cls","$an":{"shared":[]},"$nm":"Inner60"}},"$nm":"Issue60"},"OuterC1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"OuterC1"},"SubRef31":{"super":{"$pk":"nesting","$nm":"SubRef3"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef31"},"OuterC2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$c":{"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"}},"$nm":"OuterC2"},"testRefinement":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement"},"returner":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"o"}]],"$mt":"mthd","$m":{"produce":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$nm":"produce"}},"$nm":"returner"},"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"noop"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"noop"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"printName"}},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"gttr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Inner"}},"$at":{"inner":{"$t":{"$pk":"nesting","$nm":"Inner"},"$mt":"attr","$nm":"inner"},"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"int"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"name"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"attr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Outer"},"producer":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"mthd","$m":{"produce":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"produce"}},"$nm":"producer"},"RefineTest5":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"f"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"g"}],"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"g"}},"$nm":"Inner"}},"$at":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"f"}},"$nm":"RefineTest5"},"RefineTest4":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"d"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"e"}],"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$at":{"e":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"e"}},"$nm":"Inner"}},"$at":{"d":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"d"}},"$nm":"RefineTest4"},"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}}}}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}},"RefineTest3":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest3"},"RefineTest2":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest2"},"SubRef5":{"super":{"$pk":"nesting","$nm":"RefineTest5"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef5"},"SubRef4":{"super":{"$pk":"nesting","$nm":"RefineTest4"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef4"},"SubRef3":{"super":{"$pk":"nesting","$nm":"RefineTest3"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef3"}},"$mod-bin":"6.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//ClassDef Outer at nesting.ceylon (3:0-28:0)
function Outer(name$5093, $$outer){
    $init$Outer();
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$5093_=name$5093;
    $$$cl4138.defineAttr($$outer,'name$5093',function(){return this.name$5093_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Outer,d:['nesting','Outer','$at','name']};});
    
    //AttributeDecl int at nesting.ceylon (4:4-4:18)
    var int$5094=(10);
    $$$cl4138.defineAttr($$outer,'int$5094',function(){return int$5094;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Outer,d:['nesting','Outer','$at','int']};});
    $$outer.$prop$getInt$5094={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Outer,d:['nesting','Outer','$at','int']};}};
    $$outer.$prop$getInt$5094.get=function(){return int$5094};
    
    //AttributeDecl float at nesting.ceylon (5:4-5:34)
    var $float=int$5094.$float;
    $$$cl4138.defineAttr($$outer,'$float',function(){return $float;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Float},$cont:Outer,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Outer','$at','float']};});
    $$outer.$prop$getFloat={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Float},$cont:Outer,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Outer','$at','float']};}};
    $$outer.$prop$getFloat.get=function(){return $float};
    
    //MethodDef noop at nesting.ceylon (6:4-6:17)
    function noop$5095(){
    }
    $$outer.noop$5095=noop$5095;
    noop$5095.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Outer,d:['nesting','Outer','$m','noop']};};
    
    //ClassDef Inner at nesting.ceylon (7:4-20:4)
    function Inner$Outer($$inner$5096){
        $init$Inner$Outer();
        if ($$inner$5096===undefined)$$inner$5096=new Inner$Outer.$$;
        $$inner$5096.$$outer=this;
        
        //MethodDef printName at nesting.ceylon (8:8-10:8)
        function printName$5097(){
            $$$cl4138.print(name$5093);
        };printName$5097.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Inner$Outer,d:['nesting','Outer','$c','Inner','$m','printName']};};
        
        //AttributeGetterDef int at nesting.ceylon (11:8-13:8)
        $$$cl4138.defineAttr($$inner$5096,'$int',function(){
            return $$outer.int$5094;
        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Inner$Outer,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Outer','$c','Inner','$at','int']};});
        
        //AttributeGetterDef float at nesting.ceylon (14:8-16:8)
        $$$cl4138.defineAttr($$inner$5096,'$float',function(){
            return $$outer.$float;
        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Float},$cont:Inner$Outer,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Outer','$c','Inner','$at','float']};});
        
        //MethodDef noop at nesting.ceylon (17:8-19:8)
        function noop(){
            $$outer.noop$5095();
        }
        $$inner$5096.noop=noop;
        noop.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Inner$Outer,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Outer','$c','Inner','$m','noop']};};
        return $$inner$5096;
    }
    Inner$Outer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:Outer,d:['nesting','Outer','$c','Inner']};};
    function $init$Inner$Outer(){
        if (Inner$Outer.$$===undefined){
            $$$cl4138.initTypeProto(Inner$Outer,'nesting::Outer.Inner',$$$cl4138.Basic);
            Outer.Inner$Outer=Inner$Outer;
        }
        return Inner$Outer;
    }
    $$outer.$init$Inner$Outer=$init$Inner$Outer;
    $init$Inner$Outer();
    
    //AttributeDecl inner at nesting.ceylon (21:4-21:25)
    var inner$5098=Inner$Outer();
    $$outer.$prop$getInner$5098={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Outer.Inner$Outer},$cont:Outer,d:['nesting','Outer','$at','inner']};}};
    $$outer.$prop$getInner$5098.get=function(){return inner$5098};
    $$$cl4138.print(inner$5098.$int);
    $$$cl4138.print(inner$5098.$float);
    inner$5098.noop();
    noop$5095();
    return $$outer;
}
Outer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Outer']};};
exports.Outer=Outer;
function $init$Outer(){
    if (Outer.$$===undefined){
        $$$cl4138.initTypeProto(Outer,'nesting::Outer',$$$cl4138.Basic);
    }
    return Outer;
}
exports.$init$Outer=$init$Outer;
$init$Outer();

//MethodDef outr at nesting.ceylon (30:0-42:0)
function outr(name$5099){
    
    //AttributeDecl uname at nesting.ceylon (31:4-31:34)
    var uname$5100=name$5099.uppercased;
    
    //MethodDef inr at nesting.ceylon (32:4-34:4)
    function inr$5101(){
        return name$5099;
    };inr$5101.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],d:['nesting','outr','$m','inr']};};
    
    //AttributeGetterDef uinr at nesting.ceylon (35:4-37:4)
    function getUinr$5102(){
        return uname$5100;
    }
    ;$prop$getUinr$5102={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},d:['nesting','outr','$at','uinr']};}};
    $prop$getUinr$5102.get=function(){return uinr$5102};
    
    //AttributeDecl result at nesting.ceylon (38:4-38:25)
    var result$5103=inr$5101();
    
    //AttributeDecl uresult at nesting.ceylon (39:4-39:25)
    var uresult$5104=getUinr$5102();
    $$$cl4138.print(result$5103);
    $$$cl4138.print(uresult$5104);
}
exports.outr=outr;
outr.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['nesting','outr']};};

//ClassDef Holder at nesting.ceylon (44:0-51:0)
function Holder(o$5105, $$holder){
    $init$Holder();
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$5105_=o$5105;
    $$$cl4138.defineAttr($$holder,'o$5105',function(){return this.o$5105_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$cont:Holder,d:['nesting','Holder','$at','o']};});
    
    //MethodDef get at nesting.ceylon (45:4-47:4)
    function $get(){
        return o$5105;
    }
    $$holder.$get=$get;
    $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$ps:[],$cont:Holder,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Holder','$m','get']};};
    
    //AttributeGetterDef string at nesting.ceylon (48:4-50:4)
    $$$cl4138.defineAttr($$holder,'string',function(){
        return o$5105.string;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Holder,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Holder','$at','string']};});
    return $$holder;
}
Holder.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'o',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Holder']};};
exports.Holder=Holder;
function $init$Holder(){
    if (Holder.$$===undefined){
        $$$cl4138.initTypeProto(Holder,'nesting::Holder',$$$cl4138.Basic);
    }
    return Holder;
}
exports.$init$Holder=$init$Holder;
$init$Holder();

//ClassDef Wrapper at nesting.ceylon (53:0-61:0)
function Wrapper($$wrapper){
    $init$Wrapper();
    if ($$wrapper===undefined)$$wrapper=new Wrapper.$$;
    
    //AttributeDecl o at nesting.ceylon (54:4-54:18)
    var o$5106=(100);
    $$$cl4138.defineAttr($$wrapper,'o$5106',function(){return o$5106;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$cont:Wrapper,d:['nesting','Wrapper','$at','o']};});
    $$wrapper.$prop$getO$5106={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$cont:Wrapper,d:['nesting','Wrapper','$at','o']};}};
    $$wrapper.$prop$getO$5106.get=function(){return o$5106};
    
    //MethodDef get at nesting.ceylon (55:4-57:4)
    function $get(){
        return o$5106;
    }
    $$wrapper.$get=$get;
    $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$ps:[],$cont:Wrapper,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Wrapper','$m','get']};};
    
    //AttributeGetterDef string at nesting.ceylon (58:4-60:4)
    $$$cl4138.defineAttr($$wrapper,'string',function(){
        return o$5106.string;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Wrapper,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Wrapper','$at','string']};});
    return $$wrapper;
}
Wrapper.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Wrapper']};};
exports.Wrapper=Wrapper;
function $init$Wrapper(){
    if (Wrapper.$$===undefined){
        $$$cl4138.initTypeProto(Wrapper,'nesting::Wrapper',$$$cl4138.Basic);
    }
    return Wrapper;
}
exports.$init$Wrapper=$init$Wrapper;
$init$Wrapper();

//ClassDef Unwrapper at nesting.ceylon (63:0-71:0)
function Unwrapper($$unwrapper){
    $init$Unwrapper();
    if ($$unwrapper===undefined)$$unwrapper=new Unwrapper.$$;
    
    //AttributeDecl o at nesting.ceylon (64:4-64:27)
    var o=$$$cl4138.Float(23.56);
    $$$cl4138.defineAttr($$unwrapper,'o',function(){return o;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$cont:Unwrapper,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Unwrapper','$at','o']};});
    $$unwrapper.$prop$getO={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$cont:Unwrapper,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Unwrapper','$at','o']};}};
    $$unwrapper.$prop$getO.get=function(){return o};
    
    //MethodDef get at nesting.ceylon (65:4-67:4)
    function $get(){
        return $$unwrapper.o;
    }
    $$unwrapper.$get=$get;
    $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$ps:[],$cont:Unwrapper,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Unwrapper','$m','get']};};
    
    //AttributeGetterDef string at nesting.ceylon (68:4-70:4)
    $$$cl4138.defineAttr($$unwrapper,'string',function(){
        return $$unwrapper.o.string;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Unwrapper,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Unwrapper','$at','string']};});
    return $$unwrapper;
}
Unwrapper.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Unwrapper']};};
exports.Unwrapper=Unwrapper;
function $init$Unwrapper(){
    if (Unwrapper.$$===undefined){
        $$$cl4138.initTypeProto(Unwrapper,'nesting::Unwrapper',$$$cl4138.Basic);
    }
    return Unwrapper;
}
exports.$init$Unwrapper=$init$Unwrapper;
$init$Unwrapper();

//MethodDef producer at nesting.ceylon (73:0-77:0)
function producer(){
    
    //AttributeDecl o at nesting.ceylon (74:4-74:19)
    var o$5107=(123);
    
    //MethodDef produce at nesting.ceylon (75:4-75:35)
    function produce$5108(){
        return o$5107;
    };produce$5108.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],d:['nesting','producer','$m','produce']};};
    return produce$5108;
};producer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Callable,a:{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Object}}},$ps:[],d:['nesting','producer']};};

//MethodDef returner at nesting.ceylon (79:0-82:0)
function returner(o$5109){
    
    //MethodDef produce at nesting.ceylon (80:4-80:35)
    function produce$5110(){
        return o$5109;
    };produce$5110.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Object},$ps:[],d:['nesting','returner','$m','produce']};};
    return produce$5110;
};returner.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Callable,a:{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Object}}},$ps:[{$nm:'o',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],d:['nesting','returner']};};

//ClassDef A at nesting.ceylon (84:0-105:0)
function A($$a){
    $init$A();
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDecl foo at nesting.ceylon (85:4-85:22)
    var foo$5111=$$$cl4138.String("foo",3);
    $$$cl4138.defineAttr($$a,'foo$5111',function(){return foo$5111;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:A,d:['nesting','A','$at','foo']};});
    $$a.$prop$getFoo$5111={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:A,d:['nesting','A','$at','foo']};}};
    $$a.$prop$getFoo$5111.get=function(){return foo$5111};
    
    //ClassDef B at nesting.ceylon (86:4-96:4)
    function B$A($$b$A){
        $init$B$A();
        if ($$b$A===undefined)$$b$A=new B$A.$$;
        $$b$A.$$outer=this;
        
        //AttributeDecl qux at nesting.ceylon (87:8-87:26)
        var qux$5112=$$$cl4138.String("qux",3);
        $$$cl4138.defineAttr($$b$A,'qux$5112',function(){return qux$5112;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:B$A,d:['nesting','A','$c','B','$at','qux']};});
        $$b$A.$prop$getQux$5112={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:B$A,d:['nesting','A','$c','B','$at','qux']};}};
        $$b$A.$prop$getQux$5112.get=function(){return qux$5112};
        
        //ClassDef C at nesting.ceylon (88:8-95:8)
        function C$B$A($$c$B$A){
            $init$C$B$A();
            if ($$c$B$A===undefined)$$c$B$A=new C$B$A.$$;
            $$c$B$A.$$outer=this;
            
            //MethodDef foobar at nesting.ceylon (89:12-91:12)
            function foobar(){
                return foo$5111;
            }
            $$c$B$A.foobar=foobar;
            foobar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:C$B$A,$an:function(){return[$$$cl4138.shared()];},d:['nesting','A','$c','B','$c','C','$m','foobar']};};
            
            //MethodDef quxx at nesting.ceylon (92:12-94:12)
            function quxx(){
                return qux$5112;
            }
            $$c$B$A.quxx=quxx;
            quxx.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:C$B$A,$an:function(){return[$$$cl4138.shared()];},d:['nesting','A','$c','B','$c','C','$m','quxx']};};
            return $$c$B$A;
        }
        C$B$A.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:B$A,$an:function(){return[$$$cl4138.shared()];},d:['nesting','A','$c','B','$c','C']};};
        $$b$A.C$B$A=C$B$A;
        function $init$C$B$A(){
            if (C$B$A.$$===undefined){
                $$$cl4138.initTypeProto(C$B$A,'nesting::A.B.C',$$$cl4138.Basic);
                A.B$A.C$B$A=C$B$A;
            }
            return C$B$A;
        }
        $$b$A.$init$C$B$A=$init$C$B$A;
        $init$C$B$A();
        return $$b$A;
    }
    B$A.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:A,$an:function(){return[$$$cl4138.shared()];},d:['nesting','A','$c','B']};};
    $$a.B$A=B$A;
    function $init$B$A(){
        if (B$A.$$===undefined){
            $$$cl4138.initTypeProto(B$A,'nesting::A.B',$$$cl4138.Basic);
            A.B$A=B$A;
        }
        return B$A;
    }
    $$a.$init$B$A=$init$B$A;
    $init$B$A();
    
    //MethodDef baz at nesting.ceylon (97:4-104:4)
    function baz(){
        
        //ClassDef Baz at nesting.ceylon (98:8-102:8)
        function Baz$5113($$baz$5113){
            $init$Baz$5113();
            if ($$baz$5113===undefined)$$baz$5113=new Baz$5113.$$;
            
            //MethodDef get at nesting.ceylon (99:12-101:12)
            function $get(){
                return foo$5111;
            }
            $$baz$5113.$get=$get;
            $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Baz$5113,$an:function(){return[$$$cl4138.shared()];},d:['nesting','A','$m','baz','$c','Baz','$m','get']};};
            return $$baz$5113;
        }
        Baz$5113.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','A','$m','baz','$c','Baz']};};
        function $init$Baz$5113(){
            if (Baz$5113.$$===undefined){
                $$$cl4138.initTypeProto(Baz$5113,'nesting::A.baz.Baz',$$$cl4138.Basic);
            }
            return Baz$5113;
        }
        $init$Baz$5113();
        return Baz$5113().$get();
    }
    $$a.baz=baz;
    baz.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:A,$an:function(){return[$$$cl4138.shared()];},d:['nesting','A','$m','baz']};};
    return $$a;
}
A.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','A']};};
function $init$A(){
    if (A.$$===undefined){
        $$$cl4138.initTypeProto(A,'nesting::A',$$$cl4138.Basic);
    }
    return A;
}
exports.$init$A=$init$A;
$init$A();

//ClassDef O at nesting.ceylon (107:0-134:0)
function O($$o){
    $init$O();
    if ($$o===undefined)$$o=new O.$$;
    
    //AttributeDecl s at nesting.ceylon (108:4-108:22)
    var s$5114=$$$cl4138.String("hello",5);
    $$$cl4138.defineAttr($$o,'s$5114',function(){return s$5114;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:O,d:['nesting','O','$at','s']};});
    $$o.$prop$getS$5114={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:O,d:['nesting','O','$at','s']};}};
    $$o.$prop$getS$5114.get=function(){return s$5114};
    
    //ClassDef InnerClass at nesting.ceylon (109:4-113:4)
    function InnerClass$O($$innerClass$5115){
        $init$InnerClass$O();
        if ($$innerClass$5115===undefined)$$innerClass$5115=new InnerClass$O.$$;
        $$innerClass$5115.$$outer=this;
        
        //MethodDef f at nesting.ceylon (110:8-112:8)
        function f(){
            return s$5114;
        }
        $$innerClass$5115.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:InnerClass$O,$an:function(){return[$$$cl4138.shared()];},d:['nesting','O','$c','InnerClass','$m','f']};};
        return $$innerClass$5115;
    }
    InnerClass$O.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:O,d:['nesting','O','$c','InnerClass']};};
    function $init$InnerClass$O(){
        if (InnerClass$O.$$===undefined){
            $$$cl4138.initTypeProto(InnerClass$O,'nesting::O.InnerClass',$$$cl4138.Basic);
            O.InnerClass$O=InnerClass$O;
        }
        return InnerClass$O;
    }
    $$o.$init$InnerClass$O=$init$InnerClass$O;
    $init$InnerClass$O();
    
    //ObjectDef innerObject at nesting.ceylon (114:4-118:4)
    function innerObject$5116(){
        var $$innerObject$5116=new innerObject$5116.$$;
        $$innerObject$5116.$$outer=this;
        
        //MethodDef f at nesting.ceylon (115:8-117:8)
        function f(){
            return s$5114;
        }
        $$innerObject$5116.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:innerObject$5116,$an:function(){return[$$$cl4138.shared()];},d:['nesting','O','$o','innerObject','$m','f']};};
        return $$innerObject$5116;
    };innerObject$5116.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$cont:O,d:['nesting','O','$o','innerObject']};};
    function $init$innerObject$5116(){
        if (innerObject$5116.$$===undefined){
            $$$cl4138.initTypeProto(innerObject$5116,'nesting::O.innerObject',$$$cl4138.Basic);
            O.innerObject$5116=innerObject$5116;
        }
        return innerObject$5116;
    }
    $$o.$init$innerObject$5116=$init$innerObject$5116;
    $init$innerObject$5116();
    var innerObject$5117=innerObject$5116();
    $$$cl4138.defineAttr($$o,'innerObject$5117',function(){return innerObject$5117;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:O.innerObject$5116},$cont:O,d:['nesting','O','$o','innerObject']};});
    
    //InterfaceDef InnerInterface at nesting.ceylon (119:4-123:4)
    function InnerInterface$O($$innerInterface$5118){
        $$innerInterface$5118.$$outer=this;
        
        //MethodDef f at nesting.ceylon (120:8-122:8)
        function f(){
            return s$5114;
        }
        $$innerInterface$5118.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:InnerInterface$O,$an:function(){return[$$$cl4138.shared()];},d:['nesting','O','$i','InnerInterface','$m','f']};};
    }
    InnerInterface$O.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:O,d:['nesting','O','$i','InnerInterface']};};
    function $init$InnerInterface$O(){
        if (InnerInterface$O.$$===undefined){
            $$$cl4138.initTypeProtoI(InnerInterface$O,'nesting::O.InnerInterface');
            O.InnerInterface$O=InnerInterface$O;
        }
        return InnerInterface$O;
    }
    $$o.$init$InnerInterface$O=$init$InnerInterface$O;
    $init$InnerInterface$O();
    
    //MethodDef test1 at nesting.ceylon (124:4-126:4)
    function test1(){
        return InnerClass$O().f();
    }
    $$o.test1=test1;
    test1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:O,$an:function(){return[$$$cl4138.shared()];},d:['nesting','O','$m','test1']};};
    
    //MethodDef test2 at nesting.ceylon (127:4-129:4)
    function test2(){
        return innerObject$5117.f();
    }
    $$o.test2=test2;
    test2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:O,$an:function(){return[$$$cl4138.shared()];},d:['nesting','O','$m','test2']};};
    
    //MethodDef test3 at nesting.ceylon (130:4-133:4)
    function test3(){
        
        //ObjectDef obj at nesting.ceylon (131:8-131:45)
        function obj$5119(){
            var $$obj$5119=new obj$5119.$$;
            InnerInterface$O($$obj$5119);
            return $$obj$5119;
        };obj$5119.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},satisfies:[{t:O.InnerInterface$O}],d:['nesting','O','$m','test3','$o','obj']};};
        function $init$obj$5119(){
            if (obj$5119.$$===undefined){
                $$$cl4138.initTypeProto(obj$5119,'nesting::O.test3.obj',$$$cl4138.Basic,$init$InnerInterface$O());
            }
            return obj$5119;
        }
        $init$obj$5119();
        var obj$5120;
        function getObj$5120(){
            if (obj$5120===undefined){obj$5120=$init$obj$5119()();obj$5120.$$metamodel$$=getObj$5120.$$metamodel$$;}
            return obj$5120;
        }
        getObj$5120.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:obj$5119},d:['nesting','O','$m','test3','$o','obj']};};
        $prop$getObj$5120={get:getObj$5120,$$metamodel$$:getObj$5120.$$metamodel$$};
        return getObj$5120().f();
    }
    $$o.test3=test3;
    test3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:O,$an:function(){return[$$$cl4138.shared()];},d:['nesting','O','$m','test3']};};
    return $$o;
}
O.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','O']};};
function $init$O(){
    if (O.$$===undefined){
        $$$cl4138.initTypeProto(O,'nesting::O',$$$cl4138.Basic);
    }
    return O;
}
exports.$init$O=$init$O;
$init$O();

//ClassDef OuterC1 at nesting.ceylon (136:0-142:0)
function OuterC1($$outerC1){
    $init$OuterC1();
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    
    //ClassDef A at nesting.ceylon (137:4-139:4)
    function A$OuterC1($$a$5121){
        $init$A$OuterC1();
        if ($$a$5121===undefined)$$a$5121=new A$OuterC1.$$;
        $$a$5121.$$outer=this;
        
        //MethodDef tst at nesting.ceylon (138:8-138:55)
        function tst(){
            return $$$cl4138.String("OuterC1.A.tst()",15);
        }
        $$a$5121.tst=tst;
        tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:A$OuterC1,$an:function(){return[$$$cl4138.shared()];},d:['nesting','OuterC1','$c','A','$m','tst']};};
        return $$a$5121;
    }
    A$OuterC1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:OuterC1,d:['nesting','OuterC1','$c','A']};};
    function $init$A$OuterC1(){
        if (A$OuterC1.$$===undefined){
            $$$cl4138.initTypeProto(A$OuterC1,'nesting::OuterC1.A',$$$cl4138.Basic);
            OuterC1.A$OuterC1=A$OuterC1;
        }
        return A$OuterC1;
    }
    $$outerC1.$init$A$OuterC1=$init$A$OuterC1;
    $init$A$OuterC1();
    
    //ClassDef B at nesting.ceylon (140:4-140:27)
    function B$OuterC1($$b$5122){
        $init$B$OuterC1();
        if ($$b$5122===undefined)$$b$5122=new B$OuterC1.$$;
        $$b$5122.$$outer=this;
        A$OuterC1($$b$5122);
        return $$b$5122;
    }
    B$OuterC1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OuterC1.A$OuterC1},$ps:[],$cont:OuterC1,d:['nesting','OuterC1','$c','B']};};
    function $init$B$OuterC1(){
        if (B$OuterC1.$$===undefined){
            $$$cl4138.initTypeProto(B$OuterC1,'nesting::OuterC1.B',A$OuterC1);
            OuterC1.B$OuterC1=B$OuterC1;
        }
        return B$OuterC1;
    }
    $$outerC1.$init$B$OuterC1=$init$B$OuterC1;
    $init$B$OuterC1();
    
    //MethodDef tst at nesting.ceylon (141:4-141:42)
    function tst(){
        return B$OuterC1().tst();
    }
    $$outerC1.tst=tst;
    tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:OuterC1,$an:function(){return[$$$cl4138.shared()];},d:['nesting','OuterC1','$m','tst']};};
    return $$outerC1;
}
OuterC1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','OuterC1']};};
function $init$OuterC1(){
    if (OuterC1.$$===undefined){
        $$$cl4138.initTypeProto(OuterC1,'nesting::OuterC1',$$$cl4138.Basic);
    }
    return OuterC1;
}
exports.$init$OuterC1=$init$OuterC1;
$init$OuterC1();

//MethodDef outerf at nesting.ceylon (144:0-150:0)
function outerf(){
    
    //ClassDef A at nesting.ceylon (145:4-147:4)
    function A$5123($$a$5123){
        $init$A$5123();
        if ($$a$5123===undefined)$$a$5123=new A$5123.$$;
        
        //MethodDef tst at nesting.ceylon (146:8-146:54)
        function tst(){
            return $$$cl4138.String("outerf.A.tst()",14);
        }
        $$a$5123.tst=tst;
        tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:A$5123,$an:function(){return[$$$cl4138.shared()];},d:['nesting','outerf','$c','A','$m','tst']};};
        return $$a$5123;
    }
    A$5123.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','outerf','$c','A']};};
    function $init$A$5123(){
        if (A$5123.$$===undefined){
            $$$cl4138.initTypeProto(A$5123,'nesting::outerf.A',$$$cl4138.Basic);
        }
        return A$5123;
    }
    $init$A$5123();
    
    //ClassDef B at nesting.ceylon (148:4-148:27)
    function B$5124($$b$5124){
        $init$B$5124();
        if ($$b$5124===undefined)$$b$5124=new B$5124.$$;
        A$5123($$b$5124);
        return $$b$5124;
    }
    B$5124.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:A$5123},$ps:[],d:['nesting','outerf','$c','B']};};
    function $init$B$5124(){
        if (B$5124.$$===undefined){
            $$$cl4138.initTypeProto(B$5124,'nesting::outerf.B',$init$A$5123());
        }
        return B$5124;
    }
    $init$B$5124();
    return B$5124().tst();
};outerf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],d:['nesting','outerf']};};

//ClassDef OuterC2 at nesting.ceylon (152:0-160:0)
function OuterC2($$outerC2){
    $init$OuterC2();
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    
    //ClassDef A at nesting.ceylon (153:4-155:4)
    function A$OuterC2($$a$5125){
        $init$A$OuterC2();
        if ($$a$5125===undefined)$$a$5125=new A$OuterC2.$$;
        $$a$5125.$$outer=this;
        
        //MethodDef tst at nesting.ceylon (154:8-154:55)
        function tst(){
            return $$$cl4138.String("OuterC2.A.tst()",15);
        }
        $$a$5125.tst=tst;
        tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:A$OuterC2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','OuterC2','$c','A','$m','tst']};};
        return $$a$5125;
    }
    A$OuterC2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:OuterC2,d:['nesting','OuterC2','$c','A']};};
    function $init$A$OuterC2(){
        if (A$OuterC2.$$===undefined){
            $$$cl4138.initTypeProto(A$OuterC2,'nesting::OuterC2.A',$$$cl4138.Basic);
            OuterC2.A$OuterC2=A$OuterC2;
        }
        return A$OuterC2;
    }
    $$outerC2.$init$A$OuterC2=$init$A$OuterC2;
    $init$A$OuterC2();
    
    //MethodDef tst at nesting.ceylon (156:4-159:4)
    function tst(){
        
        //ClassDef B at nesting.ceylon (157:8-157:31)
        function B$5126($$b$5126){
            $init$B$5126();
            if ($$b$5126===undefined)$$b$5126=new B$5126.$$;
            A$OuterC2($$b$5126);
            return $$b$5126;
        }
        B$5126.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OuterC2.A$OuterC2},$ps:[],d:['nesting','OuterC2','$m','tst','$c','B']};};
        function $init$B$5126(){
            if (B$5126.$$===undefined){
                $$$cl4138.initTypeProto(B$5126,'nesting::OuterC2.tst.B',A$OuterC2);
            }
            return B$5126;
        }
        $init$B$5126();
        return B$5126().tst();
    }
    $$outerC2.tst=tst;
    tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:OuterC2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','OuterC2','$m','tst']};};
    return $$outerC2;
}
OuterC2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','OuterC2']};};
function $init$OuterC2(){
    if (OuterC2.$$===undefined){
        $$$cl4138.initTypeProto(OuterC2,'nesting::OuterC2',$$$cl4138.Basic);
    }
    return OuterC2;
}
exports.$init$OuterC2=$init$OuterC2;
$init$OuterC2();

//ClassDef NameTest at nesting.ceylon (162:0-178:0)
function NameTest($$nameTest){
    $init$NameTest();
    if ($$nameTest===undefined)$$nameTest=new NameTest.$$;
    
    //AttributeDecl x at nesting.ceylon (163:4-163:25)
    var x=$$$cl4138.String("1",1);
    $$$cl4138.defineAttr($$nameTest,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$at','x']};});
    $$nameTest.$prop$getX={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$at','x']};}};
    $$nameTest.$prop$getX.get=function(){return x};
    
    //ClassDef NameTest at nesting.ceylon (164:4-176:4)
    function NameTest$NameTest($$nameTest$NameTest){
        $init$NameTest$NameTest();
        if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new NameTest$NameTest.$$;
        $$nameTest$NameTest.$$outer=this;
        
        //AttributeDecl x at nesting.ceylon (165:8-165:29)
        var x=$$$cl4138.String("2",1);
        $$$cl4138.defineAttr($$nameTest$NameTest,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:NameTest$NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest','$at','x']};});
        $$nameTest$NameTest.$prop$getX.get=function(){return x};
        
        //MethodDef f at nesting.ceylon (166:8-175:8)
        function f(){
            
            //ClassDef NameTest at nesting.ceylon (167:12-173:12)
            function NameTest$5127($$nameTest$5127){
                $init$NameTest$5127();
                if ($$nameTest$5127===undefined)$$nameTest$5127=new NameTest$5127.$$;
                
                //AttributeDecl x at nesting.ceylon (168:16-168:37)
                var x=$$$cl4138.String("3",1);
                $$$cl4138.defineAttr($$nameTest$5127,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:NameTest$5127,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$at','x']};});
                $$nameTest$5127.$prop$getX.get=function(){return x};
                
                //ClassDef NameTest at nesting.ceylon (169:16-171:16)
                function NameTest$NameTest($$nameTest$NameTest){
                    $init$NameTest$NameTest();
                    if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new NameTest$NameTest.$$;
                    $$nameTest$NameTest.$$outer=this;
                    
                    //AttributeDecl x at nesting.ceylon (170:20-170:41)
                    var x=$$$cl4138.String("4",1);
                    $$$cl4138.defineAttr($$nameTest$NameTest,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:NameTest$NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$c','NameTest','$at','x']};});
                    $$nameTest$NameTest.$prop$getX.get=function(){return x};
                    return $$nameTest$NameTest;
                }
                NameTest$NameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:NameTest$5127,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$c','NameTest']};};
                $$nameTest$5127.NameTest$NameTest=NameTest$NameTest;
                function $init$NameTest$NameTest(){
                    if (NameTest$NameTest.$$===undefined){
                        $$$cl4138.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest.f.NameTest.NameTest',$$$cl4138.Basic);
                        NameTest$5127.NameTest$NameTest=NameTest$NameTest;
                    }
                    return NameTest$NameTest;
                }
                $$nameTest$5127.$init$NameTest$NameTest=$init$NameTest$NameTest;
                $init$NameTest$NameTest();
                
                //MethodDef f at nesting.ceylon (172:16-172:66)
                function f(){
                    return $$nameTest$5127.x.plus($$nameTest$5127.NameTest$NameTest().x);
                }
                $$nameTest$5127.f=f;
                f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:NameTest$5127,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$m','f']};};
                return $$nameTest$5127;
            }
            NameTest$5127.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest']};};
            function $init$NameTest$5127(){
                if (NameTest$5127.$$===undefined){
                    $$$cl4138.initTypeProto(NameTest$5127,'nesting::NameTest.NameTest.f.NameTest',$$$cl4138.Basic);
                }
                return NameTest$5127;
            }
            $init$NameTest$5127();
            return $$nameTest.x.plus($$nameTest$NameTest.x).plus(NameTest$5127().f());
        }
        $$nameTest$NameTest.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:NameTest$NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f']};};
        return $$nameTest$NameTest;
    }
    NameTest$NameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$c','NameTest']};};
    $$nameTest.NameTest$NameTest=NameTest$NameTest;
    function $init$NameTest$NameTest(){
        if (NameTest$NameTest.$$===undefined){
            $$$cl4138.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest',$$$cl4138.Basic);
            NameTest.NameTest$NameTest=NameTest$NameTest;
        }
        return NameTest$NameTest;
    }
    $$nameTest.$init$NameTest$NameTest=$init$NameTest$NameTest;
    $init$NameTest$NameTest();
    
    //MethodDef f at nesting.ceylon (177:4-177:52)
    function f(){
        return $$nameTest.NameTest$NameTest().f();
    }
    $$nameTest.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:NameTest,$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest','$m','f']};};
    return $$nameTest;
}
NameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','NameTest']};};
exports.NameTest=NameTest;
function $init$NameTest(){
    if (NameTest.$$===undefined){
        $$$cl4138.initTypeProto(NameTest,'nesting::NameTest',$$$cl4138.Basic);
    }
    return NameTest;
}
exports.$init$NameTest=$init$NameTest;
$init$NameTest();

//ObjectDef nameTest at nesting.ceylon (180:0-196:0)
function nameTest$5128(){
    var $$nameTest=new nameTest$5128.$$;
    
    //AttributeDecl x at nesting.ceylon (181:4-181:25)
    var x=$$$cl4138.String("1",1);
    $$$cl4138.defineAttr($$nameTest,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:nameTest$5128,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$at','x']};});
    $$nameTest.$prop$getX.get=function(){return x};
    
    //ObjectDef nameTest at nesting.ceylon (182:4-194:4)
    function nameTest$5129(){
        var $$nameTest$nameTest=new nameTest$5129.$$;
        $$nameTest$nameTest.$$outer=this;
        
        //AttributeDecl x at nesting.ceylon (183:8-183:29)
        var x=$$$cl4138.String("2",1);
        $$$cl4138.defineAttr($$nameTest$nameTest,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:nameTest$5129,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$at','x']};});
        $$nameTest$nameTest.$prop$getX.get=function(){return x};
        
        //MethodDef f at nesting.ceylon (184:8-193:8)
        function f(){
            
            //ObjectDef nameTest at nesting.ceylon (185:12-191:12)
            function nameTest$5130(){
                var $$nameTest$5130=new nameTest$5130.$$;
                
                //AttributeDecl x at nesting.ceylon (186:16-186:37)
                var x=$$$cl4138.String("3",1);
                $$$cl4138.defineAttr($$nameTest$5130,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:nameTest$5130,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$at','x']};});
                $$nameTest$5130.$prop$getX.get=function(){return x};
                
                //ObjectDef nameTest at nesting.ceylon (187:16-189:16)
                function nameTest$5131(){
                    var $$nameTest$nameTest=new nameTest$5131.$$;
                    $$nameTest$nameTest.$$outer=this;
                    
                    //AttributeDecl x at nesting.ceylon (188:20-188:41)
                    var x=$$$cl4138.String("4",1);
                    $$$cl4138.defineAttr($$nameTest$nameTest,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:nameTest$5131,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest','$at','x']};});
                    $$nameTest$nameTest.$prop$getX.get=function(){return x};
                    return $$nameTest$nameTest;
                };nameTest$5131.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$cont:nameTest$5130,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest']};};
                function $init$nameTest$5131(){
                    if (nameTest$5131.$$===undefined){
                        $$$cl4138.initTypeProto(nameTest$5131,'nesting::nameTest.nameTest.f.nameTest.nameTest',$$$cl4138.Basic);
                        nameTest$5130.nameTest$5131=nameTest$5131;
                    }
                    return nameTest$5131;
                }
                $$nameTest$5130.$init$nameTest$5131=$init$nameTest$5131;
                $init$nameTest$5131();
                var nameTest=nameTest$5131();
                $$$cl4138.defineAttr($$nameTest$5130,'nameTest',function(){return nameTest;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$5130.nameTest$5131},$cont:nameTest$5130,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest']};});
                
                //MethodDef f at nesting.ceylon (190:16-190:64)
                function f(){
                    return $$nameTest$5130.x.plus($$nameTest$5130.nameTest.x);
                }
                $$nameTest$5130.f=f;
                f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:nameTest$5130,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$m','f']};};
                return $$nameTest$5130;
            };nameTest$5130.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest']};};
            function $init$nameTest$5130(){
                if (nameTest$5130.$$===undefined){
                    $$$cl4138.initTypeProto(nameTest$5130,'nesting::nameTest.nameTest.f.nameTest',$$$cl4138.Basic);
                }
                return nameTest$5130;
            }
            $init$nameTest$5130();
            var nameTest$5132;
            function getNameTest$5132(){
                if (nameTest$5132===undefined){nameTest$5132=$init$nameTest$5130()();nameTest$5132.$$metamodel$$=getNameTest$5132.$$metamodel$$;}
                return nameTest$5132;
            }
            getNameTest$5132.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$5130},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest']};};
            $prop$getNameTest$5132={get:getNameTest$5132,$$metamodel$$:getNameTest$5132.$$metamodel$$};
            return $$nameTest.x.plus($$nameTest$nameTest.x).plus(getNameTest$5132().f());
        }
        $$nameTest$nameTest.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:nameTest$5129,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f']};};
        return $$nameTest$nameTest;
    };nameTest$5129.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$cont:nameTest$5128,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest']};};
    function $init$nameTest$5129(){
        if (nameTest$5129.$$===undefined){
            $$$cl4138.initTypeProto(nameTest$5129,'nesting::nameTest.nameTest',$$$cl4138.Basic);
            nameTest$5128.nameTest$5129=nameTest$5129;
        }
        return nameTest$5129;
    }
    $$nameTest.$init$nameTest$5129=$init$nameTest$5129;
    $init$nameTest$5129();
    var nameTest=nameTest$5129();
    $$$cl4138.defineAttr($$nameTest,'nameTest',function(){return nameTest;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$5128.nameTest$5129},$cont:nameTest$5128,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$o','nameTest']};});
    
    //MethodDef f at nesting.ceylon (195:4-195:50)
    function f(){
        return $$nameTest.nameTest.f();
    }
    $$nameTest.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:nameTest$5128,$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest','$m','f']};};
    return $$nameTest;
};nameTest$5128.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest']};};
function $init$nameTest$5128(){
    if (nameTest$5128.$$===undefined){
        $$$cl4138.initTypeProto(nameTest$5128,'nesting::nameTest',$$$cl4138.Basic);
    }
    return nameTest$5128;
}
exports.$init$nameTest$5128=$init$nameTest$5128;
$init$nameTest$5128();
var nameTest$5133;
function getNameTest(){
    if (nameTest$5133===undefined){nameTest$5133=$init$nameTest$5128()();nameTest$5133.$$metamodel$$=getNameTest.$$metamodel$$;}
    return nameTest$5133;
}
exports.getNameTest=getNameTest;
getNameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$5128},$an:function(){return[$$$cl4138.shared()];},d:['nesting','nameTest']};};
$prop$getNameTest={get:getNameTest,$$metamodel$$:getNameTest.$$metamodel$$};
exports.$prop$getNameTest=$prop$getNameTest;

//ClassDef C1 at nesting.ceylon (198:0-209:0)
function C1($$c1){
    $init$C1();
    if ($$c1===undefined)$$c1=new C1.$$;
    
    //AttributeDecl x at nesting.ceylon (199:4-199:33)
    var x=$$$cl4138.String("1",1);
    $$$cl4138.defineAttr($$c1,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:C1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','C1','$at','x']};});
    $$c1.$prop$getX.get=function(){return x};
    
    //ClassDef C1 at nesting.ceylon (200:4-202:4)
    function C1$C1($$c1$C1){
        $init$C1$C1();
        if ($$c1$C1===undefined)$$c1$C1=new C1$C1.$$;
        $$c1$C1.$$outer=this;
        
        //AttributeDecl x at nesting.ceylon (201:8-201:38)
        var x=$$$cl4138.String("11",2);
        $$$cl4138.defineAttr($$c1$C1,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:C1$C1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','C1','$c','C1','$at','x']};});
        $$c1$C1.$prop$getX.get=function(){return x};
        return $$c1$C1;
    }
    C1$C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:C1,$an:function(){return[$$$cl4138.shared()];},d:['nesting','C1','$c','C1']};};
    $$c1.C1$C1=C1$C1;
    function $init$C1$C1(){
        if (C1$C1.$$===undefined){
            $$$cl4138.initTypeProto(C1$C1,'nesting::C1.C1',$$$cl4138.Basic);
            C1.C1$C1=C1$C1;
        }
        return C1$C1;
    }
    $$c1.$init$C1$C1=$init$C1$C1;
    $init$C1$C1();
    
    //ClassDef C3 at nesting.ceylon (203:4-208:4)
    function C3$C1($$c3$C1){
        $init$C3$C1();
        if ($$c3$C1===undefined)$$c3$C1=new C3$C1.$$;
        $$c3$C1.$$outer=this;
        $$c1.C1$C1($$c3$C1);
        $$c3$C1.x$$nesting$C1$C1=$$c3$C1.x;
        
        //AttributeDecl x at nesting.ceylon (204:8-204:45)
        var x=$$$cl4138.String("13",2);
        $$$cl4138.defineAttr($$c3$C1,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:C3$C1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['nesting','C1','$c','C3','$at','x']};});
        $$c3$C1.$prop$getX.get=function(){return x};
        
        //MethodDef f at nesting.ceylon (205:8-207:8)
        function f(){
            return $$$cl4138.StringBuilder().appendAll([$$c1.x.string,$$$cl4138.String("-",1),$$c3$C1.x$$nesting$C1$C1.string,$$$cl4138.String("-",1),$$c1.C1$C1().x.string,$$$cl4138.String("-",1),$$c3$C1.x.string,$$$cl4138.String("-",1),$$c1.C3$C1().x.string]).string;
        }
        $$c3$C1.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:C3$C1,$an:function(){return[$$$cl4138.shared()];},d:['nesting','C1','$c','C3','$m','f']};};
        return $$c3$C1;
    }
    C3$C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1.C1$C1},$ps:[],$cont:C1,$an:function(){return[$$$cl4138.shared()];},d:['nesting','C1','$c','C3']};};
    $$c1.C3$C1=C3$C1;
    function $init$C3$C1(){
        if (C3$C1.$$===undefined){
            $$$cl4138.initTypeProto(C3$C1,'nesting::C1.C3',$$c1.C1$C1);
            C1.C3$C1=C3$C1;
        }
        return C3$C1;
    }
    $$c1.$init$C3$C1=$init$C3$C1;
    $init$C3$C1();
    return $$c1;
}
C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','C1']};};
exports.C1=C1;
function $init$C1(){
    if (C1.$$===undefined){
        $$$cl4138.initTypeProto(C1,'nesting::C1',$$$cl4138.Basic);
    }
    return C1;
}
exports.$init$C1=$init$C1;
$init$C1();

//ClassDef C2 at nesting.ceylon (210:0-221:0)
function C2($$c2){
    $init$C2();
    if ($$c2===undefined)$$c2=new C2.$$;
    C1($$c2);
    $$c2.C1$C1$$nesting$C1=$$c2.C1$C1;
    
    //AttributeDecl x at nesting.ceylon (211:4-211:32)
    var x=$$$cl4138.String("2",1);
    $$$cl4138.defineAttr($$c2,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:C2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','C2','$at','x']};});
    $$c2.$prop$getX.get=function(){return x};
    
    //ClassDef C2 at nesting.ceylon (212:4-220:4)
    function C2$C2($$c2$C2){
        $init$C2$C2();
        if ($$c2$C2===undefined)$$c2$C2=new C2$C2.$$;
        $$c2$C2.$$outer=this;
        $$c2.C1$C1$$nesting$C1($$c2$C2);
        $$c2$C2.x$$nesting$C1$C1=$$c2$C2.x;
        
        //AttributeDecl x at nesting.ceylon (213:8-213:37)
        var x=$$$cl4138.String("22",2);
        $$$cl4138.defineAttr($$c2$C2,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:C2$C2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','C2','$c','C2','$at','x']};});
        $$c2$C2.$prop$getX.get=function(){return x};
        
        //ClassDef C2 at nesting.ceylon (214:8-216:8)
        function C2$C2$C2($$c2$C2$C2){
            $init$C2$C2$C2();
            if ($$c2$C2$C2===undefined)$$c2$C2$C2=new C2$C2$C2.$$;
            $$c2$C2$C2.$$outer=this;
            $$c2.C3$C1($$c2$C2$C2);
            
            //AttributeDecl x at nesting.ceylon (215:12-215:42)
            var x=$$$cl4138.String("222",3);
            $$$cl4138.defineAttr($$c2$C2$C2,'x',function(){return x;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:C2$C2$C2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','C2','$c','C2','$c','C2','$at','x']};});
            $$c2$C2$C2.$prop$getX.get=function(){return x};
            return $$c2$C2$C2;
        }
        C2$C2$C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1.C3$C1},$ps:[],$cont:C2$C2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','C2','$c','C2','$c','C2']};};
        $$c2$C2.C2$C2$C2=C2$C2$C2;
        function $init$C2$C2$C2(){
            if (C2$C2$C2.$$===undefined){
                $$$cl4138.initTypeProto(C2$C2$C2,'nesting::C2.C2.C2',$$c2.C3$C1);
                C2.C2$C2.C2$C2$C2=C2$C2$C2;
            }
            return C2$C2$C2;
        }
        $$c2$C2.$init$C2$C2$C2=$init$C2$C2$C2;
        $init$C2$C2$C2();
        
        //MethodDef f at nesting.ceylon (217:8-219:8)
        function f(){
            return $$$cl4138.StringBuilder().appendAll([$$c2.x.string,$$$cl4138.String("-",1),$$c2.C1$C1().x.string,$$$cl4138.String("-",1),$$c2$C2.x.string,$$$cl4138.String("-",1),$$c2$C2.x$$nesting$C1$C1.string,$$$cl4138.String("-",1),$$c2.C3$C1().x.string,$$$cl4138.String("-",1),$$c2$C2.C2$C2$C2().x.string,$$$cl4138.String("-",1),$$c2$C2.C2$C2$C2().f().string,$$$cl4138.String("-",1),$$c2.C3$C1().f().string]).string;
        }
        $$c2$C2.f=f;
        f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:C2$C2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','C2','$c','C2','$m','f']};};
        return $$c2$C2;
    }
    C2$C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1.C1$C1},$ps:[],$cont:C2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','C2','$c','C2']};};
    $$c2.C2$C2=C2$C2;
    function $init$C2$C2(){
        if (C2$C2.$$===undefined){
            $$$cl4138.initTypeProto(C2$C2,'nesting::C2.C2',$$c2.C1$C1$$nesting$C1);
            C2.C2$C2=C2$C2;
        }
        return C2$C2;
    }
    $$c2.$init$C2$C2=$init$C2$C2;
    $init$C2$C2();
    return $$c2;
}
C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','C2']};};
exports.C2=C2;
function $init$C2(){
    if (C2.$$===undefined){
        $$$cl4138.initTypeProto(C2,'nesting::C2',$init$C1());
    }
    return C2;
}
exports.$init$C2=$init$C2;
$init$C2();

//MethodDef test at nesting.ceylon (223:0-253:0)
function test(){
    outr($$$cl4138.String("Hello",5));
    $$$c4139.check(Holder($$$cl4138.String("ok",2)).$get().string.equals($$$cl4138.String("ok",2)),$$$cl4138.String("holder(ok)",10));
    $$$c4139.check(Holder($$$cl4138.String("ok",2)).string.equals($$$cl4138.String("ok",2)),$$$cl4138.String("holder.string",13));
    $$$c4139.check(Wrapper().$get().string.equals($$$cl4138.String("100",3)),$$$cl4138.String("wrapper 1",9));
    $$$c4139.check(Wrapper().string.equals($$$cl4138.String("100",3)),$$$cl4138.String("wrapper 2",9));
    $$$c4139.check(Unwrapper().$get().string.equals($$$cl4138.String("23.56",5)),$$$cl4138.String("unwrapper 1",11));
    $$$c4139.check(Unwrapper().o.string.equals($$$cl4138.String("23.56",5)),$$$cl4138.String("unwrapper 2",11));
    $$$c4139.check(Unwrapper().string.equals($$$cl4138.String("23.56",5)),$$$cl4138.String("unwrapper 3",11));
    $$$c4139.check($$$cl4138.isOfType(producer(),{t:$$$cl4138.Callable,a:{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Integer}}}),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("function 1 is ",14),$$$cl4138.className($$$cl4138.$JsCallable(producer(),[],{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Object}})).string]).string);
    $$$c4139.check($$$cl4138.isOfType(producer()(),{t:$$$cl4138.Integer}),$$$cl4138.String("function 2",10));
    $$$c4139.check((123).equals(producer()()),$$$cl4138.String("function 3",10));
    $$$c4139.check($$$cl4138.String("something",9).equals(returner($$$cl4138.String("something",9))()),$$$cl4138.String("function 4",10));
    $$$c4139.check(A().B$A().C$B$A().foobar().equals($$$cl4138.String("foo",3)),$$$cl4138.String("foobar",6));
    $$$c4139.check(A().B$A().C$B$A().quxx().equals($$$cl4138.String("qux",3)),$$$cl4138.String("quxx",4));
    $$$c4139.check(A().baz().equals($$$cl4138.String("foo",3)),$$$cl4138.String("baz",3));
    $$$c4139.check(O().test1().equals($$$cl4138.String("hello",5)),$$$cl4138.String("method instantiating inner class",32));
    $$$c4139.check(O().test2().equals($$$cl4138.String("hello",5)),$$$cl4138.String("method accessing inner object",29));
    $$$c4139.check(O().test3().equals($$$cl4138.String("hello",5)),$$$cl4138.String("method deriving inner interface",31));
    $$$c4139.check(OuterC1().tst().equals($$$cl4138.String("OuterC1.A.tst()",15)),$$$cl4138.String("",0));
    $$$c4139.check(outerf().equals($$$cl4138.String("outerf.A.tst()",14)),$$$cl4138.String("",0));
    $$$c4139.check(OuterC2().tst().equals($$$cl4138.String("OuterC2.A.tst()",15)),$$$cl4138.String("",0));
    Outer($$$cl4138.String("Hello",5));
    $$$c4139.check(NameTest().f().equals($$$cl4138.String("1234",4)),$$$cl4138.String("Nested class with same name",27));
    $$$c4139.check(getNameTest().f().equals($$$cl4138.String("1234",4)),$$$cl4138.String("Nested object with same name",28));
    $$$c4139.check(C1().C3$C1().f().equals($$$cl4138.String("1-11-11-13-13",13)),$$$cl4138.String("Several nested classes with same name (1)",41));
    $$$c4139.check(C2().C2$C2().f().equals($$$cl4138.String("2-11-22-11-13-222-2-11-11-222-13-2-11-11-13-13",46)),$$$cl4138.String("Several nested classes with same name (2)",41));
    testRefinement();
    testRefinement2();
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','test']};};

//ClassDef X at refinement.ceylon (4:0-17:0)
function X($$x){
    $init$X();
    if ($$x===undefined)$$x=new X.$$;
    
    //ClassDef RefineTest1 at refinement.ceylon (5:4-16:4)
    function RefineTest1$X($$refineTest1$X){
        $init$RefineTest1$X();
        if ($$refineTest1$X===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
        $$refineTest1$X.$$outer=this;
        
        //ClassDef Inner at refinement.ceylon (6:8-12:8)
        function Inner$RefineTest1$X($$inner$RefineTest1$X){
            $init$Inner$RefineTest1$X();
            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new Inner$RefineTest1$X.$$;
            $$inner$RefineTest1$X.$$outer=this;
            
            //AttributeDecl origin at refinement.ceylon (7:12-7:54)
            var origin=$$$cl4138.String("RefineTest1.Inner",17);
            $$$cl4138.defineAttr($$inner$RefineTest1$X,'origin',function(){return origin;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$at','origin']};});
            $$inner$RefineTest1$X.$prop$getOrigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$at','origin']};}};
            $$inner$RefineTest1$X.$prop$getOrigin.get=function(){return origin};
            
            //MethodDef x at refinement.ceylon (8:12-10:12)
            function x(){
                return $$$cl4138.String("x and ",6).plus($$inner$RefineTest1$X.y());
            }
            $$inner$RefineTest1$X.x=x;
            x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$m','x']};};
            return $$inner$RefineTest1$X;
        }
        Inner$RefineTest1$X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:RefineTest1$X,$an:function(){return[$$$cl4138.shared(),$$$cl4138.formal()];},d:['nesting','X','$c','RefineTest1','$c','Inner']};};
        $$refineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
        function $init$Inner$RefineTest1$X(){
            if (Inner$RefineTest1$X.$$===undefined){
                $$$cl4138.initTypeProto(Inner$RefineTest1$X,'nesting::X.RefineTest1.Inner',$$$cl4138.Basic);
                X.RefineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
            }
            return Inner$RefineTest1$X;
        }
        $$refineTest1$X.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
        $init$Inner$RefineTest1$X();
        
        //MethodDef outerx at refinement.ceylon (13:8-15:8)
        function outerx(){
            return $$refineTest1$X.Inner$RefineTest1$X().x();
        }
        $$refineTest1$X.outerx=outerx;
        outerx.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:RefineTest1$X,$an:function(){return[$$$cl4138.shared()];},d:['nesting','X','$c','RefineTest1','$m','outerx']};};
        return $$refineTest1$X;
    }
    RefineTest1$X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:X,$an:function(){return[$$$cl4138.abstract(),$$$cl4138.shared()];},d:['nesting','X','$c','RefineTest1']};};
    $$x.RefineTest1$X=RefineTest1$X;
    function $init$RefineTest1$X(){
        if (RefineTest1$X.$$===undefined){
            $$$cl4138.initTypeProto(RefineTest1$X,'nesting::X.RefineTest1',$$$cl4138.Basic);
            X.RefineTest1$X=RefineTest1$X;
        }
        return RefineTest1$X;
    }
    $$x.$init$RefineTest1$X=$init$RefineTest1$X;
    $init$RefineTest1$X();
    return $$x;
}
X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','X']};};
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl4138.initTypeProto(X,'nesting::X',$$$cl4138.Basic);
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDef RefineTest2 at refinement.ceylon (20:0-24:0)
function RefineTest2($$refineTest2){
    $init$RefineTest2();
    if ($$refineTest2===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
    
    //ClassDef Inner at refinement.ceylon (21:4-23:4)
    function Inner$RefineTest2($$inner$RefineTest2){
        $init$Inner$RefineTest2();
        if ($$inner$RefineTest2===undefined)$$inner$RefineTest2=new Inner$RefineTest2.$$;
        $$inner$RefineTest2.$$outer=this;
        
        //MethodDef hello at refinement.ceylon (22:8-22:71)
        function hello(){
            return $$$cl4138.String("hello from RefineTest2.Inner",28);
        }
        $$inner$RefineTest2.hello=hello;
        hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','RefineTest2','$c','Inner','$m','hello']};};
        return $$inner$RefineTest2;
    }
    Inner$RefineTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:RefineTest2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','RefineTest2','$c','Inner']};};
    $$refineTest2.Inner$RefineTest2=Inner$RefineTest2;
    function $init$Inner$RefineTest2(){
        if (Inner$RefineTest2.$$===undefined){
            $$$cl4138.initTypeProto(Inner$RefineTest2,'nesting::RefineTest2.Inner',$$$cl4138.Basic);
            RefineTest2.Inner$RefineTest2=Inner$RefineTest2;
        }
        return Inner$RefineTest2;
    }
    $$refineTest2.$init$Inner$RefineTest2=$init$Inner$RefineTest2;
    $init$Inner$RefineTest2();
    return $$refineTest2;
}
RefineTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.abstract(),$$$cl4138.shared()];},d:['nesting','RefineTest2']};};
exports.RefineTest2=RefineTest2;
function $init$RefineTest2(){
    if (RefineTest2.$$===undefined){
        $$$cl4138.initTypeProto(RefineTest2,'nesting::RefineTest2',$$$cl4138.Basic);
    }
    return RefineTest2;
}
exports.$init$RefineTest2=$init$RefineTest2;
$init$RefineTest2();

//ClassDef RefineTest3 at refinement.ceylon (27:0-33:0)
function RefineTest3($$refineTest3){
    $init$RefineTest3();
    if ($$refineTest3===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
    
    //ClassDef Inner at refinement.ceylon (28:4-32:4)
    function Inner$RefineTest3($$inner$RefineTest3){
        $init$Inner$RefineTest3();
        if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new Inner$RefineTest3.$$;
        $$inner$RefineTest3.$$outer=this;
        
        //MethodDef x at refinement.ceylon (29:8-31:8)
        function x(){
            return $$$cl4138.String("x",1);
        }
        $$inner$RefineTest3.x=x;
        x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest3,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','RefineTest3','$c','Inner','$m','x']};};
        return $$inner$RefineTest3;
    }
    Inner$RefineTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:RefineTest3,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','RefineTest3','$c','Inner']};};
    $$refineTest3.Inner$RefineTest3=Inner$RefineTest3;
    function $init$Inner$RefineTest3(){
        if (Inner$RefineTest3.$$===undefined){
            $$$cl4138.initTypeProto(Inner$RefineTest3,'nesting::RefineTest3.Inner',$$$cl4138.Basic);
            RefineTest3.Inner$RefineTest3=Inner$RefineTest3;
        }
        return Inner$RefineTest3;
    }
    $$refineTest3.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
    $init$Inner$RefineTest3();
    return $$refineTest3;
}
RefineTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.abstract(),$$$cl4138.shared()];},d:['nesting','RefineTest3']};};
exports.RefineTest3=RefineTest3;
function $init$RefineTest3(){
    if (RefineTest3.$$===undefined){
        $$$cl4138.initTypeProto(RefineTest3,'nesting::RefineTest3',$$$cl4138.Basic);
    }
    return RefineTest3;
}
exports.$init$RefineTest3=$init$RefineTest3;
$init$RefineTest3();

//ClassDef Y at refinement.ceylon (35:0-47:0)
function Y($$y){
    $init$Y();
    if ($$y===undefined)$$y=new Y.$$;
    X($$y);
    
    //ClassDef SubRef1 at refinement.ceylon (36:4-46:4)
    function SubRef1$Y($$subRef1$Y){
        $init$SubRef1$Y();
        if ($$subRef1$Y===undefined)$$subRef1$Y=new SubRef1$Y.$$;
        $$subRef1$Y.$$outer=this;
        $$y.RefineTest1$X($$subRef1$Y);
        $$subRef1$Y.Inner$RefineTest1$X$$nesting$X$RefineTest1=$$subRef1$Y.Inner$RefineTest1$X;
        
        //ClassDef Inner at refinement.ceylon (37:6-45:6)
        function Inner$RefineTest1$X($$inner$RefineTest1$X){
            $init$Inner$RefineTest1$X();
            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new Inner$RefineTest1$X.$$;
            $$inner$RefineTest1$X.$$outer=this;
            $$subRef1$Y.Inner$RefineTest1$X$$nesting$X$RefineTest1($$inner$RefineTest1$X);
            $$inner$RefineTest1$X.x$$nesting$X$RefineTest1$Inner=$$inner$RefineTest1$X.x;
            
            //AttributeDecl suborigin at refinement.ceylon (38:10-38:51)
            var suborigin=$$$cl4138.String("SubRef1.Inner",13);
            $$$cl4138.defineAttr($$inner$RefineTest1$X,'suborigin',function(){return suborigin;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$at','suborigin']};});
            $$inner$RefineTest1$X.$prop$getSuborigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$at','suborigin']};}};
            $$inner$RefineTest1$X.$prop$getSuborigin.get=function(){return suborigin};
            
            //MethodDef x at refinement.ceylon (39:10-41:10)
            function x(){
                return $$$cl4138.String("REFINED ",8).plus($$inner$RefineTest1$X.x$$nesting$X$RefineTest1$Inner());
            }
            $$inner$RefineTest1$X.x=x;
            x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$m','x']};};
            
            //MethodDef y at refinement.ceylon (42:10-44:10)
            function y(){
                return $$$cl4138.String("y",1);
            }
            $$inner$RefineTest1$X.y=y;
            y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$m','y']};};
            return $$inner$RefineTest1$X;
        }
        Inner$RefineTest1$X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X.RefineTest1$X.Inner$RefineTest1$X},$ps:[],$cont:SubRef1$Y,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Y','$c','SubRef1','$c','Inner']};};
        $$subRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
        function $init$Inner$RefineTest1$X(){
            if (Inner$RefineTest1$X.$$===undefined){
                $$$cl4138.initTypeProto(Inner$RefineTest1$X,'nesting::Y.SubRef1.Inner',$$subRef1$Y.Inner$RefineTest1$X$$nesting$X$RefineTest1);
                Y.SubRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
            }
            return Inner$RefineTest1$X;
        }
        $$subRef1$Y.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
        $init$Inner$RefineTest1$X();
        return $$subRef1$Y;
    }
    SubRef1$Y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X.RefineTest1$X},$ps:[],$cont:Y,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y','$c','SubRef1']};};
    $$y.SubRef1$Y=SubRef1$Y;
    function $init$SubRef1$Y(){
        if (SubRef1$Y.$$===undefined){
            $$$cl4138.initTypeProto(SubRef1$Y,'nesting::Y.SubRef1',$$y.RefineTest1$X);
            Y.SubRef1$Y=SubRef1$Y;
        }
        return SubRef1$Y;
    }
    $$y.$init$SubRef1$Y=$init$SubRef1$Y;
    $init$SubRef1$Y();
    return $$y;
}
Y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y']};};
exports.Y=Y;
function $init$Y(){
    if (Y.$$===undefined){
        $$$cl4138.initTypeProto(Y,'nesting::Y',$init$X());
    }
    return Y;
}
exports.$init$Y=$init$Y;
$init$Y();

//ClassDef SubRef2 at refinement.ceylon (49:0-53:0)
function SubRef2($$subRef2){
    $init$SubRef2();
    if ($$subRef2===undefined)$$subRef2=new SubRef2.$$;
    RefineTest2($$subRef2);
    
    //MethodDef x at refinement.ceylon (50:4-52:4)
    function x(){
        return $$subRef2.Inner$RefineTest2().hello();
    }
    $$subRef2.x=x;
    x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:SubRef2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef2','$m','x']};};
    return $$subRef2;
}
SubRef2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest2},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef2']};};
exports.SubRef2=SubRef2;
function $init$SubRef2(){
    if (SubRef2.$$===undefined){
        $$$cl4138.initTypeProto(SubRef2,'nesting::SubRef2',$init$RefineTest2());
    }
    return SubRef2;
}
exports.$init$SubRef2=$init$SubRef2;
$init$SubRef2();

//ClassDef SubRef3 at refinement.ceylon (55:0-59:0)
function SubRef3($$subRef3){
    $init$SubRef3();
    if ($$subRef3===undefined)$$subRef3=new SubRef3.$$;
    RefineTest3($$subRef3);
    
    //MethodDef x at refinement.ceylon (56:4-58:4)
    function x(){
        return $$subRef3.Inner$RefineTest3().x();
    }
    $$subRef3.x=x;
    x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:SubRef3,$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef3','$m','x']};};
    return $$subRef3;
}
SubRef3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest3},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef3']};};
exports.SubRef3=SubRef3;
function $init$SubRef3(){
    if (SubRef3.$$===undefined){
        $$$cl4138.initTypeProto(SubRef3,'nesting::SubRef3',$init$RefineTest3());
    }
    return SubRef3;
}
exports.$init$SubRef3=$init$SubRef3;
$init$SubRef3();

//ClassDef SubRef31 at refinement.ceylon (60:0-64:0)
function SubRef31($$subRef31){
    $init$SubRef31();
    if ($$subRef31===undefined)$$subRef31=new SubRef31.$$;
    SubRef3($$subRef31);
    $$subRef31.Inner$RefineTest3$$nesting$RefineTest3=$$subRef31.Inner$RefineTest3;
    
    //ClassDef Inner at refinement.ceylon (61:4-63:4)
    function Inner$RefineTest3($$inner$RefineTest3){
        $init$Inner$RefineTest3();
        if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new Inner$RefineTest3.$$;
        $$inner$RefineTest3.$$outer=this;
        $$subRef31.Inner$RefineTest3$$nesting$RefineTest3($$inner$RefineTest3);
        
        //MethodDef x at refinement.ceylon (62:8-62:51)
        function x(){
            return $$$cl4138.String("equis",5);
        }
        $$inner$RefineTest3.x=x;
        x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest3,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','SubRef31','$c','Inner','$m','x']};};
        return $$inner$RefineTest3;
    }
    Inner$RefineTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest3.Inner$RefineTest3},$ps:[],$cont:SubRef31,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','SubRef31','$c','Inner']};};
    $$subRef31.Inner$RefineTest3=Inner$RefineTest3;
    function $init$Inner$RefineTest3(){
        if (Inner$RefineTest3.$$===undefined){
            $$$cl4138.initTypeProto(Inner$RefineTest3,'nesting::SubRef31.Inner',$$subRef31.Inner$RefineTest3$$nesting$RefineTest3);
            SubRef31.Inner$RefineTest3=Inner$RefineTest3;
        }
        return Inner$RefineTest3;
    }
    $$subRef31.$init$Inner$RefineTest3=$init$Inner$RefineTest3;
    $init$Inner$RefineTest3();
    return $$subRef31;
}
SubRef31.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:SubRef3},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef31']};};
exports.SubRef31=SubRef31;
function $init$SubRef31(){
    if (SubRef31.$$===undefined){
        $$$cl4138.initTypeProto(SubRef31,'nesting::SubRef31',$init$SubRef3());
    }
    return SubRef31;
}
exports.$init$SubRef31=$init$SubRef31;
$init$SubRef31();

//MethodDef testRefinement at refinement.ceylon (66:0-76:0)
function testRefinement(){
    
    //AttributeDecl c1 at refinement.ceylon (67:4-67:36)
    var c1$5134=Y().SubRef1$Y().Inner$RefineTest1$X();
    $$$c4139.check($$$cl4138.className(c1$5134).equals($$$cl4138.String("nesting::Y.SubRef1.Inner",24)),$$$cl4138.String("classname is ",13).plus($$$cl4138.className(c1$5134)));
    $$$c4139.check(c1$5134.origin.equals($$$cl4138.String("RefineTest1.Inner",17)),$$$cl4138.String("refinement [1]",14));
    $$$c4139.check(c1$5134.suborigin.equals($$$cl4138.String("SubRef1.Inner",13)),$$$cl4138.String("refinement [2]",14));
    $$$c4139.check(c1$5134.x().equals($$$cl4138.String("REFINED x and y",15)),$$$cl4138.String("refinement [3]",14));
    $$$c4139.check(c1$5134.x().equals(Y().SubRef1$Y().outerx()),$$$cl4138.String("refinement [4]",14));
    $$$c4139.check(SubRef2().x().equals($$$cl4138.String("hello from RefineTest2.Inner",28)),$$$cl4138.String("refinement [5]",14));
    $$$c4139.check(SubRef3().x().equals($$$cl4138.String("x",1)),$$$cl4138.String("refinement [6]",14));
    $$$c4139.check(SubRef31().x().equals($$$cl4138.String("equis",5)),$$$cl4138.String("refinement [7]",14));
};testRefinement.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['nesting','testRefinement']};};

//ClassDef X2 at refinement2.ceylon (4:0-18:0)
function X2(a$5135, $$x2){
    $init$X2();
    if ($$x2===undefined)$$x2=new X2.$$;
    $$x2.a$5135_=a$5135;
    $$$cl4138.defineAttr($$x2,'a$5135',function(){return this.a$5135_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:X2,d:['nesting','X2','$at','a']};});
    
    //ClassDef RefineTest1 at refinement2.ceylon (5:4-17:4)
    function RefineTest1$X2(b$5136, $$refineTest1$X2){
        $init$RefineTest1$X2();
        if ($$refineTest1$X2===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
        $$refineTest1$X2.$$outer=this;
        $$refineTest1$X2.b$5136_=b$5136;
        $$$cl4138.defineAttr($$refineTest1$X2,'b$5136',function(){return this.b$5136_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:RefineTest1$X2,d:['nesting','X2','$c','RefineTest1','$at','b']};});
        
        //ClassDef Inner at refinement2.ceylon (6:8-13:8)
        function Inner$RefineTest1$X2(c$5137, $$targs$$,$$inner$RefineTest1$X2){
            $init$Inner$RefineTest1$X2();
            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new Inner$RefineTest1$X2.$$;
            $$$cl4138.set_type_args($$inner$RefineTest1$X2,$$targs$$);
            $$inner$RefineTest1$X2.$$outer=this;
            $$inner$RefineTest1$X2.c$5137_=c$5137;
            $$$cl4138.defineAttr($$inner$RefineTest1$X2,'c$5137',function(){return this.c$5137_;},undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:Inner$RefineTest1$X2,d:['nesting','X2','$c','RefineTest1','$c','Inner','$at','c']};});
            
            //AttributeDecl origin at refinement2.ceylon (8:12-8:62)
            var origin=$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("RefineTest1.Inner (",19),c$5137.string,$$$cl4138.String(")",1)]).string;
            $$$cl4138.defineAttr($$inner$RefineTest1$X2,'origin',function(){return origin;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$at','origin']};});
            $$inner$RefineTest1$X2.$prop$getOrigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$at','origin']};}};
            $$inner$RefineTest1$X2.$prop$getOrigin.get=function(){return origin};
            
            //MethodDef x at refinement2.ceylon (9:12-11:12)
            function x(){
                return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("x and ",6),$$inner$RefineTest1$X2.y().string,$$$cl4138.String(" and a:",7),a$5135.string,$$$cl4138.String(", b:",4),b$5136.string,$$$cl4138.String(", c:",4),c$5137.string,$$$cl4138.String(".",1)]).string;
            }
            $$inner$RefineTest1$X2.x=x;
            x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$m','x']};};
            return $$inner$RefineTest1$X2;
        }
        Inner$RefineTest1$X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'c',$mt:'prm',$t:'Element',$an:function(){return[];}}],$cont:RefineTest1$X2,$tp:{Element:{'satisfies':[{t:$$$cl4138.Object}]}},$an:function(){return[$$$cl4138.shared(),$$$cl4138.formal()];},d:['nesting','X2','$c','RefineTest1','$c','Inner']};};
        $$refineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
        function $init$Inner$RefineTest1$X2(){
            if (Inner$RefineTest1$X2.$$===undefined){
                $$$cl4138.initTypeProto(Inner$RefineTest1$X2,'nesting::X2.RefineTest1.Inner',$$$cl4138.Basic);
                X2.RefineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
            }
            return Inner$RefineTest1$X2;
        }
        $$refineTest1$X2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
        $init$Inner$RefineTest1$X2();
        
        //MethodDef outerx at refinement2.ceylon (14:8-16:8)
        function outerx(){
            return $$refineTest1$X2.Inner$RefineTest1$X2(a$5135.uppercased,{Element:{t:$$$cl4138.String}}).x();
        }
        $$refineTest1$X2.outerx=outerx;
        outerx.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:RefineTest1$X2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','X2','$c','RefineTest1','$m','outerx']};};
        return $$refineTest1$X2;
    }
    RefineTest1$X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:X2,$an:function(){return[$$$cl4138.abstract(),$$$cl4138.shared()];},d:['nesting','X2','$c','RefineTest1']};};
    $$x2.RefineTest1$X2=RefineTest1$X2;
    function $init$RefineTest1$X2(){
        if (RefineTest1$X2.$$===undefined){
            $$$cl4138.initTypeProto(RefineTest1$X2,'nesting::X2.RefineTest1',$$$cl4138.Basic);
            X2.RefineTest1$X2=RefineTest1$X2;
        }
        return RefineTest1$X2;
    }
    $$x2.$init$RefineTest1$X2=$init$RefineTest1$X2;
    $init$RefineTest1$X2();
    return $$x2;
}
X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['nesting','X2']};};
exports.X2=X2;
function $init$X2(){
    if (X2.$$===undefined){
        $$$cl4138.initTypeProto(X2,'nesting::X2',$$$cl4138.Basic);
    }
    return X2;
}
exports.$init$X2=$init$X2;
$init$X2();

//ClassDef RefineTest4 at refinement2.ceylon (21:0-25:0)
function RefineTest4(d$5138, $$refineTest4){
    $init$RefineTest4();
    if ($$refineTest4===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
    $$refineTest4.d$5138_=d$5138;
    $$$cl4138.defineAttr($$refineTest4,'d$5138',function(){return this.d$5138_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:RefineTest4,d:['nesting','RefineTest4','$at','d']};});
    
    //ClassDef Inner at refinement2.ceylon (22:4-24:4)
    function Inner$RefineTest4(e$5139, $$inner$RefineTest4){
        $init$Inner$RefineTest4();
        if ($$inner$RefineTest4===undefined)$$inner$RefineTest4=new Inner$RefineTest4.$$;
        $$inner$RefineTest4.$$outer=this;
        $$inner$RefineTest4.e$5139_=e$5139;
        $$$cl4138.defineAttr($$inner$RefineTest4,'e$5139',function(){return this.e$5139_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Inner$RefineTest4,d:['nesting','RefineTest4','$c','Inner','$at','e']};});
        
        //MethodDef hello at refinement2.ceylon (23:8-23:83)
        function hello(){
            return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("hello from RefineTest2.Inner with ",34),e$5139.string,$$$cl4138.String(".",1)]).string;
        }
        $$inner$RefineTest4.hello=hello;
        hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest4,$an:function(){return[$$$cl4138.shared()];},d:['nesting','RefineTest4','$c','Inner','$m','hello']};};
        return $$inner$RefineTest4;
    }
    Inner$RefineTest4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'e',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:RefineTest4,$an:function(){return[$$$cl4138.shared()];},d:['nesting','RefineTest4','$c','Inner']};};
    $$refineTest4.Inner$RefineTest4=Inner$RefineTest4;
    function $init$Inner$RefineTest4(){
        if (Inner$RefineTest4.$$===undefined){
            $$$cl4138.initTypeProto(Inner$RefineTest4,'nesting::RefineTest4.Inner',$$$cl4138.Basic);
            RefineTest4.Inner$RefineTest4=Inner$RefineTest4;
        }
        return Inner$RefineTest4;
    }
    $$refineTest4.$init$Inner$RefineTest4=$init$Inner$RefineTest4;
    $init$Inner$RefineTest4();
    return $$refineTest4;
}
RefineTest4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'d',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$an:function(){return[$$$cl4138.abstract(),$$$cl4138.shared()];},d:['nesting','RefineTest4']};};
exports.RefineTest4=RefineTest4;
function $init$RefineTest4(){
    if (RefineTest4.$$===undefined){
        $$$cl4138.initTypeProto(RefineTest4,'nesting::RefineTest4',$$$cl4138.Basic);
    }
    return RefineTest4;
}
exports.$init$RefineTest4=$init$RefineTest4;
$init$RefineTest4();

//ClassDef RefineTest5 at refinement2.ceylon (28:0-34:0)
function RefineTest5(f$5140, $$refineTest5){
    $init$RefineTest5();
    if ($$refineTest5===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
    $$refineTest5.f$5140_=f$5140;
    $$$cl4138.defineAttr($$refineTest5,'f$5140',function(){return this.f$5140_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:RefineTest5,d:['nesting','RefineTest5','$at','f']};});
    
    //ClassDef Inner at refinement2.ceylon (29:4-33:4)
    function Inner$RefineTest5(g$5141, $$inner$RefineTest5){
        $init$Inner$RefineTest5();
        if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new Inner$RefineTest5.$$;
        $$inner$RefineTest5.$$outer=this;
        $$inner$RefineTest5.g$5141_=g$5141;
        $$$cl4138.defineAttr($$inner$RefineTest5,'g$5141',function(){return this.g$5141_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest5,d:['nesting','RefineTest5','$c','Inner','$at','g']};});
        
        //MethodDef x at refinement2.ceylon (30:8-32:8)
        function x(){
            return g$5141.repeat(f$5140);
        }
        $$inner$RefineTest5.x=x;
        x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest5,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','RefineTest5','$c','Inner','$m','x']};};
        return $$inner$RefineTest5;
    }
    Inner$RefineTest5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'g',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$cont:RefineTest5,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['nesting','RefineTest5','$c','Inner']};};
    $$refineTest5.Inner$RefineTest5=Inner$RefineTest5;
    function $init$Inner$RefineTest5(){
        if (Inner$RefineTest5.$$===undefined){
            $$$cl4138.initTypeProto(Inner$RefineTest5,'nesting::RefineTest5.Inner',$$$cl4138.Basic);
            RefineTest5.Inner$RefineTest5=Inner$RefineTest5;
        }
        return Inner$RefineTest5;
    }
    $$refineTest5.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
    $init$Inner$RefineTest5();
    return $$refineTest5;
}
RefineTest5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$an:function(){return[$$$cl4138.abstract(),$$$cl4138.shared()];},d:['nesting','RefineTest5']};};
exports.RefineTest5=RefineTest5;
function $init$RefineTest5(){
    if (RefineTest5.$$===undefined){
        $$$cl4138.initTypeProto(RefineTest5,'nesting::RefineTest5',$$$cl4138.Basic);
    }
    return RefineTest5;
}
exports.$init$RefineTest5=$init$RefineTest5;
$init$RefineTest5();

//ClassDef Y2 at refinement2.ceylon (36:0-49:0)
function Y2(h$5142, $$y2){
    $init$Y2();
    if ($$y2===undefined)$$y2=new Y2.$$;
    $$y2.h$5142_=h$5142;
    X2(h$5142,$$y2);
    $$$cl4138.defineAttr($$y2,'h$5142',function(){return this.h$5142_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Y2,d:['nesting','Y2','$at','h']};});
    
    //ClassDef SubRef1 at refinement2.ceylon (37:4-48:4)
    function SubRef1$Y2(d$5143, $$subRef1$Y2){
        $init$SubRef1$Y2();
        if ($$subRef1$Y2===undefined)$$subRef1$Y2=new SubRef1$Y2.$$;
        $$subRef1$Y2.$$outer=this;
        $$subRef1$Y2.d$5143_=d$5143;
        $$y2.RefineTest1$X2((1),$$subRef1$Y2);
        $$subRef1$Y2.Inner$RefineTest1$X2$$nesting$X2$RefineTest1=$$subRef1$Y2.Inner$RefineTest1$X2;
        $$$cl4138.defineAttr($$subRef1$Y2,'d$5143',function(){return this.d$5143_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:SubRef1$Y2,d:['nesting','Y2','$c','SubRef1','$at','d']};});
        
        //ClassDef Inner at refinement2.ceylon (38:6-47:6)
        function Inner$RefineTest1$X2(d2$5144, $$targs$$,$$inner$RefineTest1$X2){
            $init$Inner$RefineTest1$X2();
            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new Inner$RefineTest1$X2.$$;
            $$$cl4138.set_type_args($$inner$RefineTest1$X2,$$targs$$);
            $$inner$RefineTest1$X2.$$outer=this;
            $$inner$RefineTest1$X2.d2$5144_=d2$5144;
            $$subRef1$Y2.Inner$RefineTest1$X2$$nesting$X2$RefineTest1(d2$5144,{Element:$$inner$RefineTest1$X2.$$targs$$.Element},$$inner$RefineTest1$X2);
            $$inner$RefineTest1$X2.x$$nesting$X2$RefineTest1$Inner=$$inner$RefineTest1$X2.x;
            $$$cl4138.defineAttr($$inner$RefineTest1$X2,'d2$5144',function(){return this.d2$5144_;},undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:Inner$RefineTest1$X2,d:['nesting','Y2','$c','SubRef1','$c','Inner','$at','d2']};});
            
            //AttributeDecl suborigin at refinement2.ceylon (40:10-40:51)
            var suborigin=$$$cl4138.String("SubRef1.Inner",13);
            $$$cl4138.defineAttr($$inner$RefineTest1$X2,'suborigin',function(){return suborigin;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$at','suborigin']};});
            $$inner$RefineTest1$X2.$prop$getSuborigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$at','suborigin']};}};
            $$inner$RefineTest1$X2.$prop$getSuborigin.get=function(){return suborigin};
            
            //MethodDef x at refinement2.ceylon (41:10-43:10)
            function x(){
                return $$$cl4138.String("REFINED ",8).plus($$inner$RefineTest1$X2.x$$nesting$X2$RefineTest1$Inner());
            }
            $$inner$RefineTest1$X2.x=x;
            x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$m','x']};};
            
            //MethodDef y at refinement2.ceylon (44:10-46:10)
            function y(){
                return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("y",1),h$5142.string,$$$cl4138.String(",d:",3),d$5143.string,$$$cl4138.String(",d2:",4),d2$5144.string,$$$cl4138.String(".",1)]).string;
            }
            $$inner$RefineTest1$X2.y=y;
            y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$m','y']};};
            return $$inner$RefineTest1$X2;
        }
        Inner$RefineTest1$X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X2.RefineTest1$X2.Inner$RefineTest1$X2,a:{Element:'Element'}},$ps:[{$nm:'d2',$mt:'prm',$t:'Element',$an:function(){return[];}}],$cont:SubRef1$Y2,$tp:{Element:{'satisfies':[{t:$$$cl4138.Object}]}},$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','Y2','$c','SubRef1','$c','Inner']};};
        $$subRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
        function $init$Inner$RefineTest1$X2(){
            if (Inner$RefineTest1$X2.$$===undefined){
                $$$cl4138.initTypeProto(Inner$RefineTest1$X2,'nesting::Y2.SubRef1.Inner',$$subRef1$Y2.Inner$RefineTest1$X2$$nesting$X2$RefineTest1);
                Y2.SubRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
            }
            return Inner$RefineTest1$X2;
        }
        $$subRef1$Y2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
        $init$Inner$RefineTest1$X2();
        return $$subRef1$Y2;
    }
    SubRef1$Y2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X2.RefineTest1$X2},$ps:[{$nm:'d',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:Y2,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y2','$c','SubRef1']};};
    $$y2.SubRef1$Y2=SubRef1$Y2;
    function $init$SubRef1$Y2(){
        if (SubRef1$Y2.$$===undefined){
            $$$cl4138.initTypeProto(SubRef1$Y2,'nesting::Y2.SubRef1',$$y2.RefineTest1$X2);
            Y2.SubRef1$Y2=SubRef1$Y2;
        }
        return SubRef1$Y2;
    }
    $$y2.$init$SubRef1$Y2=$init$SubRef1$Y2;
    $init$SubRef1$Y2();
    return $$y2;
}
Y2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X2},$ps:[{$nm:'h',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Y2']};};
exports.Y2=Y2;
function $init$Y2(){
    if (Y2.$$===undefined){
        $$$cl4138.initTypeProto(Y2,'nesting::Y2',$init$X2());
    }
    return Y2;
}
exports.$init$Y2=$init$Y2;
$init$Y2();

//ClassDef SubRef4 at refinement2.ceylon (51:0-55:0)
function SubRef4($$subRef4){
    $init$SubRef4();
    if ($$subRef4===undefined)$$subRef4=new SubRef4.$$;
    RefineTest4($$$cl4138.String("t4",2),$$subRef4);
    
    //MethodDef x at refinement2.ceylon (52:4-54:4)
    function x(){
        return $$subRef4.Inner$RefineTest4((5)).hello();
    }
    $$subRef4.x=x;
    x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:SubRef4,$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef4','$m','x']};};
    return $$subRef4;
}
SubRef4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest4},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef4']};};
exports.SubRef4=SubRef4;
function $init$SubRef4(){
    if (SubRef4.$$===undefined){
        $$$cl4138.initTypeProto(SubRef4,'nesting::SubRef4',$init$RefineTest4());
    }
    return SubRef4;
}
exports.$init$SubRef4=$init$SubRef4;
$init$SubRef4();

//ClassDef SubRef5 at refinement2.ceylon (57:0-61:0)
function SubRef5($$subRef5){
    $init$SubRef5();
    if ($$subRef5===undefined)$$subRef5=new SubRef5.$$;
    RefineTest5((6),$$subRef5);
    
    //MethodDef x at refinement2.ceylon (58:4-60:4)
    function x(){
        return $$subRef5.Inner$RefineTest5($$$cl4138.String("sr5",3)).x();
    }
    $$subRef5.x=x;
    x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:SubRef5,$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef5','$m','x']};};
    return $$subRef5;
}
SubRef5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest5},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef5']};};
exports.SubRef5=SubRef5;
function $init$SubRef5(){
    if (SubRef5.$$===undefined){
        $$$cl4138.initTypeProto(SubRef5,'nesting::SubRef5',$init$RefineTest5());
    }
    return SubRef5;
}
exports.$init$SubRef5=$init$SubRef5;
$init$SubRef5();

//ClassDef SubRef51 at refinement2.ceylon (62:0-66:0)
function SubRef51($$subRef51){
    $init$SubRef51();
    if ($$subRef51===undefined)$$subRef51=new SubRef51.$$;
    SubRef5($$subRef51);
    $$subRef51.Inner$RefineTest5$$nesting$RefineTest5=$$subRef51.Inner$RefineTest5;
    
    //ClassDef Inner at refinement2.ceylon (63:4-65:4)
    function Inner$RefineTest5(subg55$5145, $$inner$RefineTest5){
        $init$Inner$RefineTest5();
        if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new Inner$RefineTest5.$$;
        $$inner$RefineTest5.$$outer=this;
        $$inner$RefineTest5.subg55$5145_=subg55$5145;
        $$subRef51.Inner$RefineTest5$$nesting$RefineTest5(subg55$5145,$$inner$RefineTest5);
        $$$cl4138.defineAttr($$inner$RefineTest5,'subg55$5145',function(){return this.subg55$5145_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Inner$RefineTest5,d:['nesting','SubRef51','$c','Inner','$at','subg55']};});
        
        //MethodDef x at refinement2.ceylon (64:8-64:62)
        function x(){
            return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("equis",5),subg55$5145.string,$$$cl4138.String(".",1)]).string;
        }
        $$inner$RefineTest5.x=x;
        x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Inner$RefineTest5,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','SubRef51','$c','Inner','$m','x']};};
        return $$inner$RefineTest5;
    }
    Inner$RefineTest5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest5.Inner$RefineTest5},$ps:[{$nm:'subg55',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],$cont:SubRef51,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['nesting','SubRef51','$c','Inner']};};
    $$subRef51.Inner$RefineTest5=Inner$RefineTest5;
    function $init$Inner$RefineTest5(){
        if (Inner$RefineTest5.$$===undefined){
            $$$cl4138.initTypeProto(Inner$RefineTest5,'nesting::SubRef51.Inner',$$subRef51.Inner$RefineTest5$$nesting$RefineTest5);
            SubRef51.Inner$RefineTest5=Inner$RefineTest5;
        }
        return Inner$RefineTest5;
    }
    $$subRef51.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
    $init$Inner$RefineTest5();
    return $$subRef51;
}
SubRef51.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:SubRef5},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','SubRef51']};};
exports.SubRef51=SubRef51;
function $init$SubRef51(){
    if (SubRef51.$$===undefined){
        $$$cl4138.initTypeProto(SubRef51,'nesting::SubRef51',$init$SubRef5());
    }
    return SubRef51;
}
exports.$init$SubRef51=$init$SubRef51;
$init$SubRef51();

//ClassDef Issue60 at refinement2.ceylon (68:0-70:0)
function Issue60($$issue60){
    $init$Issue60();
    if ($$issue60===undefined)$$issue60=new Issue60.$$;
    Issue60Abs($$issue60);
    
    //ClassDef Inner60 at refinement2.ceylon (69:2-69:46)
    function Inner60$Issue60($$inner60$Issue60){
        $init$Inner60$Issue60();
        if ($$inner60$Issue60===undefined)$$inner60$Issue60=new Inner60$Issue60.$$;
        $$inner60$Issue60.$$outer=this;
        $$issue60.Inner60Abs$Issue60Abs($$inner60$Issue60);
        return $$inner60$Issue60;
    }
    Inner60$Issue60.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Issue60Abs.Inner60Abs$Issue60Abs},$ps:[],$cont:Issue60,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Issue60','$c','Inner60']};};
    $$issue60.Inner60$Issue60=Inner60$Issue60;
    function $init$Inner60$Issue60(){
        if (Inner60$Issue60.$$===undefined){
            $$$cl4138.initTypeProto(Inner60$Issue60,'nesting::Issue60.Inner60',$$issue60.Inner60Abs$Issue60Abs);
            Issue60.Inner60$Issue60=Inner60$Issue60;
        }
        return Inner60$Issue60;
    }
    $$issue60.$init$Inner60$Issue60=$init$Inner60$Issue60;
    $init$Inner60$Issue60();
    return $$issue60;
}
Issue60.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Issue60Abs},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['nesting','Issue60']};};
exports.Issue60=Issue60;
function $init$Issue60(){
    if (Issue60.$$===undefined){
        $$$cl4138.initTypeProto(Issue60,'nesting::Issue60',$init$Issue60Abs());
    }
    return Issue60;
}
exports.$init$Issue60=$init$Issue60;
$init$Issue60();

//ClassDef Issue60Abs at refinement2.ceylon (71:0-73:0)
function Issue60Abs($$issue60Abs){
    $init$Issue60Abs();
    if ($$issue60Abs===undefined)$$$cl4138.throwexc($$$cl4138.InvocationException$meta$model($$$cl4138.String("Cannot instantiate abstract class")),'?','?')
    
    //ClassDef Inner60Abs at refinement2.ceylon (72:2-72:28)
    function Inner60Abs$Issue60Abs($$inner60Abs$Issue60Abs){
        $init$Inner60Abs$Issue60Abs();
        if ($$inner60Abs$Issue60Abs===undefined)$$inner60Abs$Issue60Abs=new Inner60Abs$Issue60Abs.$$;
        $$inner60Abs$Issue60Abs.$$outer=this;
        return $$inner60Abs$Issue60Abs;
    }
    Inner60Abs$Issue60Abs.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:Issue60Abs,$an:function(){return[$$$cl4138.shared()];},d:['nesting','Issue60Abs','$c','Inner60Abs']};};
    $$issue60Abs.Inner60Abs$Issue60Abs=Inner60Abs$Issue60Abs;
    function $init$Inner60Abs$Issue60Abs(){
        if (Inner60Abs$Issue60Abs.$$===undefined){
            $$$cl4138.initTypeProto(Inner60Abs$Issue60Abs,'nesting::Issue60Abs.Inner60Abs',$$$cl4138.Basic);
            Issue60Abs.Inner60Abs$Issue60Abs=Inner60Abs$Issue60Abs;
        }
        return Inner60Abs$Issue60Abs;
    }
    $$issue60Abs.$init$Inner60Abs$Issue60Abs=$init$Inner60Abs$Issue60Abs;
    $init$Inner60Abs$Issue60Abs();
    return $$issue60Abs;
}
Issue60Abs.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared(),$$$cl4138.abstract()];},d:['nesting','Issue60Abs']};};
exports.Issue60Abs=Issue60Abs;
function $init$Issue60Abs(){
    if (Issue60Abs.$$===undefined){
        $$$cl4138.initTypeProto(Issue60Abs,'nesting::Issue60Abs',$$$cl4138.Basic);
    }
    return Issue60Abs;
}
exports.$init$Issue60Abs=$init$Issue60Abs;
$init$Issue60Abs();

//MethodDef testRefinement2 at refinement2.ceylon (75:0-85:0)
function testRefinement2(){
    
    //AttributeDecl c1 at refinement2.ceylon (76:4-76:54)
    var c1$5146=Y2($$$cl4138.String("y2",2)).SubRef1$Y2((99)).Inner$RefineTest1$X2($$$cl4138.String("with parm",9),{Element:{t:$$$cl4138.String}});
    $$$c4139.check($$$cl4138.className(c1$5146).equals($$$cl4138.String("nesting::Y2.SubRef1.Inner",25)),$$$cl4138.String("classname is ",13).plus($$$cl4138.className(c1$5146)));
    $$$c4139.check(c1$5146.origin.equals($$$cl4138.String("RefineTest1.Inner (with parm)",29)),$$$cl4138.String("refinement [1] ",15).plus(c1$5146.origin));
    $$$c4139.check(c1$5146.suborigin.equals($$$cl4138.String("SubRef1.Inner",13)),$$$cl4138.String("refinement [2] ",15).plus(c1$5146.suborigin));
    $$$c4139.check(c1$5146.x().equals($$$cl4138.String("REFINED x and yy2,d:99,d2:with parm. and a:y2, b:1, c:with parm.",64)),$$$cl4138.String("refinement [3] ",15).plus(c1$5146.x()));
    $$$c4139.check(Y2($$$cl4138.String("y3",2)).SubRef1$Y2((10)).outerx().equals($$$cl4138.String("REFINED x and yy3,d:10,d2:Y3. and a:y3, b:1, c:Y3.",50)),$$$cl4138.String("refinement [4] ",15).plus(Y2($$$cl4138.String("y3",2)).SubRef1$Y2((10)).outerx()));
    $$$c4139.check(SubRef4().x().equals($$$cl4138.String("hello from RefineTest2.Inner with 5.",36)),$$$cl4138.String("refinement [5] ",15).plus(SubRef4().x()));
    $$$c4139.check(SubRef5().x().equals($$$cl4138.String("sr5sr5sr5sr5sr5sr5",18)),$$$cl4138.String("refinement [6] ",15).plus(SubRef5().x()));
    $$$c4139.check(SubRef51().x().equals($$$cl4138.String("equissr5.",9)),$$$cl4138.String("refinement [7] ",15).plus(SubRef51().x()));
};testRefinement2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['nesting','testRefinement2']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
