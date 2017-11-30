(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"nesting","$mod-version":"0.1","nesting":{"Y2":{"super":{"$pk":"nesting","$nm":"X2"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"h"}],"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"d"}],"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$tp":[{"$mt":"tpm","$nm":"Element"}],"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"d2"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"d2":{"$t":{"$nm":"Element"},"$mt":"attr","$nm":"d2"},"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$at":{"d":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"d"}},"$nm":"SubRef1"}},"$at":{"h":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"h"}},"$nm":"Y2"},"SubRef51":{"super":{"$pk":"nesting","$nm":"SubRef5"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"subg55"}],"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"subg55":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"subg55"}},"$nm":"Inner"}},"$nm":"SubRef51"},"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"baz":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$c":{"Baz":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$nm":"Baz"}},"$nm":"baz"}},"$c":{"B":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"foobar":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"foobar"},"quxx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"quxx"}},"$nm":"C"}},"$at":{"qux":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"qux"}},"$nm":"B"}},"$at":{"foo":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"foo"}},"$nm":"A"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"Unwrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$an":{"shared":[]},"$nm":"o"}},"$nm":"Unwrapper"},"SubRef2":{"super":{"$pk":"nesting","$nm":"RefineTest2"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef2"},"Wrapper":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Wrapper"},"O":{"$i":{"InnerInterface":{"$mt":"ifc","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerInterface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"test1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test1"},"test2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test2"},"test3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test3","$o":{"obj":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"nesting","$nm":"InnerInterface"}],"$mt":"obj","$nm":"obj"}}}},"$c":{"InnerClass":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"InnerClass"}},"$at":{"s":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"s"}},"$nm":"O","$o":{"innerObject":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$nm":"innerObject"}}},"outr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}]],"$mt":"mthd","$an":{"shared":[]},"$m":{"inr":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$nm":"inr"}},"$nm":"outr"},"testRefinement2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement2"},"Holder":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"o"}],"$mt":"cls","$an":{"shared":[]},"$m":{"get":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$an":{"shared":[]},"$nm":"get"}},"$at":{"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"string"},"o":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"attr","$nm":"o"}},"$nm":"Holder"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"C3":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"x"}},"$nm":"C3"},"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"C1"},"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C1"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"C2":{"super":{"$pk":"nesting","$nm":"C3"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"C2"},"Y":{"super":{"$pk":"nesting","$nm":"X"},"$mt":"cls","$an":{"shared":[]},"$c":{"SubRef1":{"super":{"$pk":"nesting","$nm":"RefineTest1"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$at":{"suborigin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"suborigin"}},"$nm":"Inner"}},"$nm":"SubRef1"}},"$nm":"Y"},"X":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$nm":"RefineTest1"}},"$nm":"X"},"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$c":{"NameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"NameTest"},"outerf":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"outerf"},"Issue60Abs":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"abstract":[],"shared":[]},"$c":{"Inner60Abs":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$nm":"Inner60Abs"}},"$nm":"Issue60Abs"},"X2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"a"}],"$mt":"cls","$an":{"shared":[]},"$c":{"RefineTest1":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$m":{"outerx":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"outerx"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$nm":"Element"},"$mt":"prm","$nm":"c"}],"$mt":"cls","$tp":[{"satisfies":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"}],"$nm":"Element"}],"$an":{"shared":[],"formal":[]},"$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"y"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"c":{"$t":{"$nm":"Element"},"$mt":"attr","$nm":"c"},"origin":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"origin"}},"$nm":"Inner"}},"$at":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"b"}},"$nm":"RefineTest1"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"a"}},"$nm":"X2"},"Issue60":{"super":{"$pk":"nesting","$nm":"Issue60Abs"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner60":{"super":{"$pk":"nesting","$nm":"Inner60Abs"},"$mt":"cls","$an":{"shared":[]},"$nm":"Inner60"}},"$nm":"Issue60"},"OuterC1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"},"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"OuterC1"},"SubRef31":{"super":{"$pk":"nesting","$nm":"SubRef3"},"$mt":"cls","$an":{"shared":[]},"$c":{"Inner":{"super":{"$pk":"nesting","$nm":"Inner"},"$mt":"cls","$an":{"shared":[],"actual":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"SubRef31"},"OuterC2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$c":{"B":{"super":{"$pk":"nesting","$nm":"A"},"$mt":"cls","$nm":"B"}},"$nm":"tst"}},"$c":{"A":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"tst":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"tst"}},"$nm":"A"}},"$nm":"OuterC2"},"testRefinement":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testRefinement"},"returner":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"o"}]],"$mt":"mthd","$m":{"produce":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"mthd","$nm":"produce"}},"$nm":"returner"},"Outer":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"noop"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"noop":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"noop"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"printName"}},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"int"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"gttr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Inner"}},"$at":{"inner":{"$t":{"$pk":"nesting","$nm":"Inner"},"$mt":"attr","$nm":"inner"},"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"int"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"name"},"float":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"attr","$an":{"shared":[]},"$nm":"float"}},"$nm":"Outer"},"producer":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Object"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"mthd","$m":{"produce":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$nm":"produce"}},"$nm":"producer"},"RefineTest5":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"f"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"g"}],"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$at":{"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"g"}},"$nm":"Inner"}},"$at":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"f"}},"$nm":"RefineTest5"},"RefineTest4":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"d"}],"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"e"}],"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$at":{"e":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"e"}},"$nm":"Inner"}},"$at":{"d":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"d"}},"$nm":"RefineTest4"},"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest","$o":{"nameTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}}}}},"$at":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"x"}},"$nm":"nameTest"}}},"RefineTest3":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"default":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"x"}},"$nm":"Inner"}},"$nm":"RefineTest3"},"RefineTest2":{"abstract":"1","super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[],"abstract":[]},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"hello":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"hello"}},"$nm":"Inner"}},"$nm":"RefineTest2"},"SubRef5":{"super":{"$pk":"nesting","$nm":"RefineTest5"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef5"},"SubRef4":{"super":{"$pk":"nesting","$nm":"RefineTest4"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef4"},"SubRef3":{"super":{"$pk":"nesting","$nm":"RefineTest3"},"$mt":"cls","$an":{"shared":[]},"$m":{"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[]},"$nm":"x"}},"$nm":"SubRef3"}},"$mod-bin":"6.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//ClassDef Outer at nesting.ceylon (3:0-28:0)
function Outer(name$997, $$outer){
    $init$Outer();
    if ($$outer===undefined)$$outer=new Outer.$$;
    $$outer.name$997_=name$997;
    
    //AttributeDecl int at nesting.ceylon (4:4-4:18)
    $$outer.int$998_=(10);
    $$outer.$prop$getInt$998={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Outer,d:['nesting','Outer','$at','int']};}};
    $$outer.$prop$getInt$998.get=function(){return int$998};
    
    //AttributeDecl float at nesting.ceylon (5:4-5:34)
    $$outer.float$999_=$$outer.int$998.$float;
    $$outer.$prop$getFloat={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Float},$cont:Outer,$an:function(){return[$$$cl1.shared()];},d:['nesting','Outer','$at','float']};}};
    $$outer.$prop$getFloat.get=function(){return $float};
    
    //AttributeDecl inner at nesting.ceylon (21:4-21:25)
    $$outer.inner$1000_=$$outer.Inner$Outer();
    $$outer.$prop$getInner$1000={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Outer.Inner$Outer},$cont:Outer,d:['nesting','Outer','$at','inner']};}};
    $$outer.$prop$getInner$1000.get=function(){return inner$1000};
    $$$cl1.print($$outer.inner$1000.$int);
    $$$cl1.print($$outer.inner$1000.$float);
    $$outer.inner$1000.noop();
    $$outer.noop$1001();
    return $$outer;
}
Outer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['nesting','Outer']};};
exports.Outer=Outer;
function $init$Outer(){
    if (Outer.$$===undefined){
        $$$cl1.initTypeProto(Outer,'nesting::Outer',$$$cl1.Basic);
        (function($$outer){
            
            //AttributeDecl int at nesting.ceylon (4:4-4:18)
            $$$cl1.defineAttr($$outer,'int$998',function(){return this.int$998_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Outer,d:['nesting','Outer','$at','int']};});
            
            //AttributeDecl float at nesting.ceylon (5:4-5:34)
            $$$cl1.defineAttr($$outer,'$float',function(){return this.float$999_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Float},$cont:Outer,$an:function(){return[$$$cl1.shared()];},d:['nesting','Outer','$at','float']};});
            
            //MethodDef noop at nesting.ceylon (6:4-6:17)
            $$outer.noop$1001=function noop$1001(){
                var $$outer=this;
            };$$outer.noop$1001.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Outer,d:['nesting','Outer','$m','noop']};};
            
            //ClassDef Inner at nesting.ceylon (7:4-20:4)
            function Inner$Outer($$inner$1002){
                $init$Inner$Outer();
                if ($$inner$1002===undefined)$$inner$1002=new this.Inner$Outer.$$;
                $$inner$1002.$$outer=this;
                return $$inner$1002;
            }
            Inner$Outer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:Outer,d:['nesting','Outer','$c','Inner']};};
            function $init$Inner$Outer(){
                if (Inner$Outer.$$===undefined){
                    $$$cl1.initTypeProto(Inner$Outer,'nesting::Outer.Inner',$$$cl1.Basic);
                    Outer.Inner$Outer=Inner$Outer;
                    (function($$inner$1002){
                        
                        //MethodDef printName at nesting.ceylon (8:8-10:8)
                        $$inner$1002.printName$1003=function printName$1003(){
                            var $$inner$1002=this;
                            $$$cl1.print($$inner$1002.$$outer.name$997);
                        };$$inner$1002.printName$1003.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Inner$Outer,d:['nesting','Outer','$c','Inner','$m','printName']};};
                        
                        //AttributeGetterDef int at nesting.ceylon (11:8-13:8)
                        $$$cl1.defineAttr($$inner$1002,'$int',function(){
                            var $$inner$1002=this;
                            return $$inner$1002.$$outer.int$998;
                        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Inner$Outer,$an:function(){return[$$$cl1.shared()];},d:['nesting','Outer','$c','Inner','$at','int']};});
                        //AttributeGetterDef float at nesting.ceylon (14:8-16:8)
                        $$$cl1.defineAttr($$inner$1002,'$float',function(){
                            var $$inner$1002=this;
                            return $$inner$1002.$$outer.$float;
                        },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Float},$cont:Inner$Outer,$an:function(){return[$$$cl1.shared()];},d:['nesting','Outer','$c','Inner','$at','float']};});
                        //MethodDef noop at nesting.ceylon (17:8-19:8)
                        $$inner$1002.noop=function noop(){
                            var $$inner$1002=this;
                            $$inner$1002.$$outer.noop$1001();
                        };$$inner$1002.noop.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Inner$Outer,$an:function(){return[$$$cl1.shared()];},d:['nesting','Outer','$c','Inner','$m','noop']};};
                    })(Inner$Outer.$$.prototype);
                }
                return Inner$Outer;
            }
            $$outer.$init$Inner$Outer=$init$Inner$Outer;
            $init$Inner$Outer();
            $$outer.Inner$Outer=Inner$Outer;
            
            //AttributeDecl inner at nesting.ceylon (21:4-21:25)
            $$$cl1.defineAttr($$outer,'inner$1000',function(){return this.inner$1000_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Outer.Inner$Outer},$cont:Outer,d:['nesting','Outer','$at','inner']};});
            $$$cl1.defineAttr($$outer,'name$997',function(){return this.name$997_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Outer,d:['nesting','Outer','$at','name']};});
        })(Outer.$$.prototype);
    }
    return Outer;
}
exports.$init$Outer=$init$Outer;
$init$Outer();

//MethodDef outr at nesting.ceylon (30:0-42:0)
function outr(name$1004){
    
    //AttributeDecl uname at nesting.ceylon (31:4-31:34)
    var uname$1005=name$1004.uppercased;
    
    //MethodDef inr at nesting.ceylon (32:4-34:4)
    function inr$1006(){
        return name$1004;
    };inr$1006.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],d:['nesting','outr','$m','inr']};};
    
    //AttributeGetterDef uinr at nesting.ceylon (35:4-37:4)
    function getUinr$1007(){
        return uname$1005;
    }
    ;$prop$getUinr$1007={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},d:['nesting','outr','$at','uinr']};}};
    $prop$getUinr$1007.get=function(){return uinr$1007};
    
    //AttributeDecl result at nesting.ceylon (38:4-38:25)
    var result$1008=inr$1006();
    
    //AttributeDecl uresult at nesting.ceylon (39:4-39:25)
    var uresult$1009=getUinr$1007();
    $$$cl1.print(result$1008);
    $$$cl1.print(uresult$1009);
}
exports.outr=outr;
outr.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['nesting','outr']};};

//ClassDef Holder at nesting.ceylon (44:0-51:0)
function Holder(o$1010, $$holder){
    $init$Holder();
    if ($$holder===undefined)$$holder=new Holder.$$;
    $$holder.o$1010_=o$1010;
    return $$holder;
}
Holder.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'o',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['nesting','Holder']};};
exports.Holder=Holder;
function $init$Holder(){
    if (Holder.$$===undefined){
        $$$cl1.initTypeProto(Holder,'nesting::Holder',$$$cl1.Basic);
        (function($$holder){
            
            //MethodDef get at nesting.ceylon (45:4-47:4)
            $$holder.$get=function $get(){
                var $$holder=this;
                return $$holder.o$1010;
            };$$holder.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$ps:[],$cont:Holder,$an:function(){return[$$$cl1.shared()];},d:['nesting','Holder','$m','get']};};
            
            //AttributeGetterDef string at nesting.ceylon (48:4-50:4)
            $$$cl1.defineAttr($$holder,'string',function(){
                var $$holder=this;
                return $$holder.o$1010.string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Holder,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Holder','$at','string']};});$$$cl1.defineAttr($$holder,'o$1010',function(){return this.o$1010_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$cont:Holder,d:['nesting','Holder','$at','o']};});
        })(Holder.$$.prototype);
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
    $$wrapper.o$1011_=(100);
    $$wrapper.$prop$getO$1011={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$cont:Wrapper,d:['nesting','Wrapper','$at','o']};}};
    $$wrapper.$prop$getO$1011.get=function(){return o$1011};
    return $$wrapper;
}
Wrapper.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','Wrapper']};};
exports.Wrapper=Wrapper;
function $init$Wrapper(){
    if (Wrapper.$$===undefined){
        $$$cl1.initTypeProto(Wrapper,'nesting::Wrapper',$$$cl1.Basic);
        (function($$wrapper){
            
            //AttributeDecl o at nesting.ceylon (54:4-54:18)
            $$$cl1.defineAttr($$wrapper,'o$1011',function(){return this.o$1011_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$cont:Wrapper,d:['nesting','Wrapper','$at','o']};});
            
            //MethodDef get at nesting.ceylon (55:4-57:4)
            $$wrapper.$get=function $get(){
                var $$wrapper=this;
                return $$wrapper.o$1011;
            };$$wrapper.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$ps:[],$cont:Wrapper,$an:function(){return[$$$cl1.shared()];},d:['nesting','Wrapper','$m','get']};};
            
            //AttributeGetterDef string at nesting.ceylon (58:4-60:4)
            $$$cl1.defineAttr($$wrapper,'string',function(){
                var $$wrapper=this;
                return $$wrapper.o$1011.string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Wrapper,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Wrapper','$at','string']};});
        })(Wrapper.$$.prototype);
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
    $$unwrapper.o$1012_=$$$cl1.Float(23.56);
    $$unwrapper.$prop$getO={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$cont:Unwrapper,$an:function(){return[$$$cl1.shared()];},d:['nesting','Unwrapper','$at','o']};}};
    $$unwrapper.$prop$getO.get=function(){return o};
    return $$unwrapper;
}
Unwrapper.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','Unwrapper']};};
exports.Unwrapper=Unwrapper;
function $init$Unwrapper(){
    if (Unwrapper.$$===undefined){
        $$$cl1.initTypeProto(Unwrapper,'nesting::Unwrapper',$$$cl1.Basic);
        (function($$unwrapper){
            
            //AttributeDecl o at nesting.ceylon (64:4-64:27)
            $$$cl1.defineAttr($$unwrapper,'o',function(){return this.o$1012_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$cont:Unwrapper,$an:function(){return[$$$cl1.shared()];},d:['nesting','Unwrapper','$at','o']};});
            
            //MethodDef get at nesting.ceylon (65:4-67:4)
            $$unwrapper.$get=function $get(){
                var $$unwrapper=this;
                return $$unwrapper.o;
            };$$unwrapper.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$ps:[],$cont:Unwrapper,$an:function(){return[$$$cl1.shared()];},d:['nesting','Unwrapper','$m','get']};};
            
            //AttributeGetterDef string at nesting.ceylon (68:4-70:4)
            $$$cl1.defineAttr($$unwrapper,'string',function(){
                var $$unwrapper=this;
                return $$unwrapper.o.string;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Unwrapper,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Unwrapper','$at','string']};});
        })(Unwrapper.$$.prototype);
    }
    return Unwrapper;
}
exports.$init$Unwrapper=$init$Unwrapper;
$init$Unwrapper();

//MethodDef producer at nesting.ceylon (73:0-77:0)
function producer(){
    
    //AttributeDecl o at nesting.ceylon (74:4-74:19)
    var o$1013=(123);
    
    //MethodDef produce at nesting.ceylon (75:4-75:35)
    function produce$1014(){
        return o$1013;
    };produce$1014.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],d:['nesting','producer','$m','produce']};};
    return produce$1014;
};producer.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}}},$ps:[],d:['nesting','producer']};};

//MethodDef returner at nesting.ceylon (79:0-82:0)
function returner(o$1015){
    
    //MethodDef produce at nesting.ceylon (80:4-80:35)
    function produce$1016(){
        return o$1015;
    };produce$1016.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Object},$ps:[],d:['nesting','returner','$m','produce']};};
    return produce$1016;
};returner.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}}},$ps:[{$nm:'o',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],d:['nesting','returner']};};

//ClassDef A at nesting.ceylon (84:0-105:0)
function A($$a){
    $init$A();
    if ($$a===undefined)$$a=new A.$$;
    
    //AttributeDecl foo at nesting.ceylon (85:4-85:22)
    $$a.foo$1017_=$$$cl1.String("foo",3);
    $$a.$prop$getFoo$1017={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:A,d:['nesting','A','$at','foo']};}};
    $$a.$prop$getFoo$1017.get=function(){return foo$1017};
    return $$a;
}
A.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','A']};};
function $init$A(){
    if (A.$$===undefined){
        $$$cl1.initTypeProto(A,'nesting::A',$$$cl1.Basic);
        (function($$a){
            
            //AttributeDecl foo at nesting.ceylon (85:4-85:22)
            $$$cl1.defineAttr($$a,'foo$1017',function(){return this.foo$1017_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:A,d:['nesting','A','$at','foo']};});
            
            //ClassDef B at nesting.ceylon (86:4-96:4)
            function B$A($$b$A){
                $init$B$A();
                if ($$b$A===undefined)$$b$A=new this.B$A.$$;
                $$b$A.$$outer=this;
                
                //AttributeDecl qux at nesting.ceylon (87:8-87:26)
                $$b$A.qux$1018_=$$$cl1.String("qux",3);
                $$b$A.$prop$getQux$1018={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:B$A,d:['nesting','A','$c','B','$at','qux']};}};
                $$b$A.$prop$getQux$1018.get=function(){return qux$1018};
                return $$b$A;
            }
            B$A.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:A,$an:function(){return[$$$cl1.shared()];},d:['nesting','A','$c','B']};};
            function $init$B$A(){
                if (B$A.$$===undefined){
                    $$$cl1.initTypeProto(B$A,'nesting::A.B',$$$cl1.Basic);
                    A.B$A=B$A;
                    (function($$b$A){
                        
                        //AttributeDecl qux at nesting.ceylon (87:8-87:26)
                        $$$cl1.defineAttr($$b$A,'qux$1018',function(){return this.qux$1018_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:B$A,d:['nesting','A','$c','B','$at','qux']};});
                        
                        //ClassDef C at nesting.ceylon (88:8-95:8)
                        function C$B$A($$c$B$A){
                            $init$C$B$A();
                            if ($$c$B$A===undefined)$$c$B$A=new this.C$B$A.$$;
                            $$c$B$A.$$outer=this;
                            return $$c$B$A;
                        }
                        C$B$A.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:B$A,$an:function(){return[$$$cl1.shared()];},d:['nesting','A','$c','B','$c','C']};};
                        function $init$C$B$A(){
                            if (C$B$A.$$===undefined){
                                $$$cl1.initTypeProto(C$B$A,'nesting::A.B.C',$$$cl1.Basic);
                                A.B$A.C$B$A=C$B$A;
                                (function($$c$B$A){
                                    
                                    //MethodDef foobar at nesting.ceylon (89:12-91:12)
                                    $$c$B$A.foobar=function foobar(){
                                        var $$c$B$A=this;
                                        return $$c$B$A.$$outer.$$outer.foo$1017;
                                    };$$c$B$A.foobar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:C$B$A,$an:function(){return[$$$cl1.shared()];},d:['nesting','A','$c','B','$c','C','$m','foobar']};};
                                    
                                    //MethodDef quxx at nesting.ceylon (92:12-94:12)
                                    $$c$B$A.quxx=function quxx(){
                                        var $$c$B$A=this;
                                        return $$c$B$A.$$outer.qux$1018;
                                    };$$c$B$A.quxx.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:C$B$A,$an:function(){return[$$$cl1.shared()];},d:['nesting','A','$c','B','$c','C','$m','quxx']};};
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
            
            //MethodDef baz at nesting.ceylon (97:4-104:4)
            $$a.baz=function baz(){
                var $$a=this;
                
                //ClassDef Baz at nesting.ceylon (98:8-102:8)
                function Baz$1019($$baz$1019){
                    $init$Baz$1019();
                    if ($$baz$1019===undefined)$$baz$1019=new Baz$1019.$$;
                    return $$baz$1019;
                }
                Baz$1019.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','A','$m','baz','$c','Baz']};};
                function $init$Baz$1019(){
                    if (Baz$1019.$$===undefined){
                        $$$cl1.initTypeProto(Baz$1019,'nesting::A.baz.Baz',$$$cl1.Basic);
                        (function($$baz$1019){
                            
                            //MethodDef get at nesting.ceylon (99:12-101:12)
                            $$baz$1019.$get=function $get(){
                                var $$baz$1019=this;
                                return $$a.foo$1017;
                            };$$baz$1019.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Baz$1019,$an:function(){return[$$$cl1.shared()];},d:['nesting','A','$m','baz','$c','Baz','$m','get']};};
                        })(Baz$1019.$$.prototype);
                    }
                    return Baz$1019;
                }
                $init$Baz$1019();
                return Baz$1019().$get();
            };$$a.baz.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:A,$an:function(){return[$$$cl1.shared()];},d:['nesting','A','$m','baz']};};
        })(A.$$.prototype);
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
    $$o.s$1020_=$$$cl1.String("hello",5);
    $$o.$prop$getS$1020={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:O,d:['nesting','O','$at','s']};}};
    $$o.$prop$getS$1020.get=function(){return s$1020};
    
    //ObjectDef innerObject at nesting.ceylon (114:4-118:4)
    $$o.innerObject$1021_=$$o.innerObject$1022();
    return $$o;
}
O.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','O']};};
function $init$O(){
    if (O.$$===undefined){
        $$$cl1.initTypeProto(O,'nesting::O',$$$cl1.Basic);
        (function($$o){
            
            //AttributeDecl s at nesting.ceylon (108:4-108:22)
            $$$cl1.defineAttr($$o,'s$1020',function(){return this.s$1020_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:O,d:['nesting','O','$at','s']};});
            
            //ClassDef InnerClass at nesting.ceylon (109:4-113:4)
            function InnerClass$O($$innerClass$1023){
                $init$InnerClass$O();
                if ($$innerClass$1023===undefined)$$innerClass$1023=new this.InnerClass$O.$$;
                $$innerClass$1023.$$outer=this;
                return $$innerClass$1023;
            }
            InnerClass$O.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:O,d:['nesting','O','$c','InnerClass']};};
            function $init$InnerClass$O(){
                if (InnerClass$O.$$===undefined){
                    $$$cl1.initTypeProto(InnerClass$O,'nesting::O.InnerClass',$$$cl1.Basic);
                    O.InnerClass$O=InnerClass$O;
                    (function($$innerClass$1023){
                        
                        //MethodDef f at nesting.ceylon (110:8-112:8)
                        $$innerClass$1023.f=function f(){
                            var $$innerClass$1023=this;
                            return $$innerClass$1023.$$outer.s$1020;
                        };$$innerClass$1023.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:InnerClass$O,$an:function(){return[$$$cl1.shared()];},d:['nesting','O','$c','InnerClass','$m','f']};};
                    })(InnerClass$O.$$.prototype);
                }
                return InnerClass$O;
            }
            $$o.$init$InnerClass$O=$init$InnerClass$O;
            $init$InnerClass$O();
            $$o.InnerClass$O=InnerClass$O;
            
            //ObjectDef innerObject at nesting.ceylon (114:4-118:4)
            function innerObject$1022(){
                var $$innerObject$1022=new this.innerObject$1022.$$;
                $$innerObject$1022.$$outer=this;
                return $$innerObject$1022;
            };innerObject$1022.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$cont:O,d:['nesting','O','$o','innerObject']};};
            function $init$innerObject$1022(){
                if (innerObject$1022.$$===undefined){
                    $$$cl1.initTypeProto(innerObject$1022,'nesting::O.innerObject',$$$cl1.Basic);
                    O.innerObject$1022=innerObject$1022;
                }
                return innerObject$1022;
            }
            $$o.$init$innerObject$1022=$init$innerObject$1022;
            $init$innerObject$1022();
            (function($$innerObject$1022){
                
                //MethodDef f at nesting.ceylon (115:8-117:8)
                $$innerObject$1022.f=function f(){
                    var $$innerObject$1022=this;
                    return $$innerObject$1022.$$outer.s$1020;
                };$$innerObject$1022.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:innerObject$1022,$an:function(){return[$$$cl1.shared()];},d:['nesting','O','$o','innerObject','$m','f']};};
            })(innerObject$1022.$$.prototype);
            $$$cl1.defineAttr($$o,'innerObject$1021',function(){return this.innerObject$1021_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:O.innerObject$1022},$cont:O,d:['nesting','O','$o','innerObject']};});
            $$o.innerObject$1022=innerObject$1022;$$o.innerObject$1022.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:O.innerObject$1022},$cont:O,d:['nesting','O','$o','innerObject']};};
            
            //InterfaceDef InnerInterface at nesting.ceylon (119:4-123:4)
            function InnerInterface$O($$innerInterface$1024){
                $$innerInterface$1024.$$outer=this;
            }
            InnerInterface$O.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:O,d:['nesting','O','$i','InnerInterface']};};
            function $init$InnerInterface$O(){
                if (InnerInterface$O.$$===undefined){
                    $$$cl1.initTypeProtoI(InnerInterface$O,'nesting::O.InnerInterface');
                    O.InnerInterface$O=InnerInterface$O;
                    (function($$innerInterface$1024){
                        
                        //MethodDef f at nesting.ceylon (120:8-122:8)
                        $$innerInterface$1024.f=function f(){
                            var $$innerInterface$1024=this;
                            return $$innerInterface$1024.$$outer.s$1020;
                        };$$innerInterface$1024.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:InnerInterface$O,$an:function(){return[$$$cl1.shared()];},d:['nesting','O','$i','InnerInterface','$m','f']};};
                    })(InnerInterface$O.$$.prototype);
                }
                return InnerInterface$O;
            }
            $$o.$init$InnerInterface$O=$init$InnerInterface$O;
            $init$InnerInterface$O();
            $$o.InnerInterface$O=InnerInterface$O;
            
            //MethodDef test1 at nesting.ceylon (124:4-126:4)
            $$o.test1=function test1(){
                var $$o=this;
                return $$o.InnerClass$O().f();
            };$$o.test1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:O,$an:function(){return[$$$cl1.shared()];},d:['nesting','O','$m','test1']};};
            
            //MethodDef test2 at nesting.ceylon (127:4-129:4)
            $$o.test2=function test2(){
                var $$o=this;
                return $$o.innerObject$1021.f();
            };$$o.test2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:O,$an:function(){return[$$$cl1.shared()];},d:['nesting','O','$m','test2']};};
            
            //MethodDef test3 at nesting.ceylon (130:4-133:4)
            $$o.test3=function test3(){
                var $$o=this;
                
                //ObjectDef obj at nesting.ceylon (131:8-131:45)
                function obj$1025(){
                    var $$obj$1025=new obj$1025.$$;
                    $$o.InnerInterface$O($$obj$1025);
                    return $$obj$1025;
                };obj$1025.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},satisfies:[{t:O.InnerInterface$O}],d:['nesting','O','$m','test3','$o','obj']};};
                function $init$obj$1025(){
                    if (obj$1025.$$===undefined){
                        $$$cl1.initTypeProto(obj$1025,'nesting::O.test3.obj',$$$cl1.Basic,$$o.$init$InnerInterface$O());
                    }
                    return obj$1025;
                }
                $init$obj$1025();
                var obj$1026;
                function getObj$1026(){
                    if (obj$1026===undefined){obj$1026=$init$obj$1025()();obj$1026.$$metamodel$$=getObj$1026.$$metamodel$$;}
                    return obj$1026;
                }
                getObj$1026.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:obj$1025},d:['nesting','O','$m','test3','$o','obj']};};
                $prop$getObj$1026={get:getObj$1026,$$metamodel$$:getObj$1026.$$metamodel$$};
                return getObj$1026().f();
            };$$o.test3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:O,$an:function(){return[$$$cl1.shared()];},d:['nesting','O','$m','test3']};};
        })(O.$$.prototype);
    }
    return O;
}
exports.$init$O=$init$O;
$init$O();

//ClassDef OuterC1 at nesting.ceylon (136:0-142:0)
function OuterC1($$outerC1){
    $init$OuterC1();
    if ($$outerC1===undefined)$$outerC1=new OuterC1.$$;
    return $$outerC1;
}
OuterC1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','OuterC1']};};
function $init$OuterC1(){
    if (OuterC1.$$===undefined){
        $$$cl1.initTypeProto(OuterC1,'nesting::OuterC1',$$$cl1.Basic);
        (function($$outerC1){
            
            //ClassDef A at nesting.ceylon (137:4-139:4)
            function A$OuterC1($$a$1027){
                $init$A$OuterC1();
                if ($$a$1027===undefined)$$a$1027=new this.A$OuterC1.$$;
                $$a$1027.$$outer=this;
                return $$a$1027;
            }
            A$OuterC1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:OuterC1,d:['nesting','OuterC1','$c','A']};};
            function $init$A$OuterC1(){
                if (A$OuterC1.$$===undefined){
                    $$$cl1.initTypeProto(A$OuterC1,'nesting::OuterC1.A',$$$cl1.Basic);
                    OuterC1.A$OuterC1=A$OuterC1;
                    (function($$a$1027){
                        
                        //MethodDef tst at nesting.ceylon (138:8-138:55)
                        $$a$1027.tst=function tst(){
                            var $$a$1027=this;
                            return $$$cl1.String("OuterC1.A.tst()",15);
                        };$$a$1027.tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:A$OuterC1,$an:function(){return[$$$cl1.shared()];},d:['nesting','OuterC1','$c','A','$m','tst']};};
                    })(A$OuterC1.$$.prototype);
                }
                return A$OuterC1;
            }
            $$outerC1.$init$A$OuterC1=$init$A$OuterC1;
            $init$A$OuterC1();
            $$outerC1.A$OuterC1=A$OuterC1;
            
            //ClassDef B at nesting.ceylon (140:4-140:27)
            function B$OuterC1($$b$1028){
                $init$B$OuterC1();
                if ($$b$1028===undefined)$$b$1028=new this.B$OuterC1.$$;
                $$b$1028.$$outer=this;
                $$b$1028.$$outer.A$OuterC1($$b$1028);
                return $$b$1028;
            }
            B$OuterC1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OuterC1.A$OuterC1},$ps:[],$cont:OuterC1,d:['nesting','OuterC1','$c','B']};};
            function $init$B$OuterC1(){
                if (B$OuterC1.$$===undefined){
                    $$$cl1.initTypeProto(B$OuterC1,'nesting::OuterC1.B',$$outerC1.A$OuterC1);
                    OuterC1.B$OuterC1=B$OuterC1;
                }
                return B$OuterC1;
            }
            $$outerC1.$init$B$OuterC1=$init$B$OuterC1;
            $init$B$OuterC1();
            $$outerC1.B$OuterC1=B$OuterC1;
            
            //MethodDef tst at nesting.ceylon (141:4-141:42)
            $$outerC1.tst=function tst(){
                var $$outerC1=this;
                return $$outerC1.B$OuterC1().tst();
            };$$outerC1.tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:OuterC1,$an:function(){return[$$$cl1.shared()];},d:['nesting','OuterC1','$m','tst']};};
        })(OuterC1.$$.prototype);
    }
    return OuterC1;
}
exports.$init$OuterC1=$init$OuterC1;
$init$OuterC1();

//MethodDef outerf at nesting.ceylon (144:0-150:0)
function outerf(){
    
    //ClassDef A at nesting.ceylon (145:4-147:4)
    function A$1029($$a$1029){
        $init$A$1029();
        if ($$a$1029===undefined)$$a$1029=new A$1029.$$;
        return $$a$1029;
    }
    A$1029.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','outerf','$c','A']};};
    function $init$A$1029(){
        if (A$1029.$$===undefined){
            $$$cl1.initTypeProto(A$1029,'nesting::outerf.A',$$$cl1.Basic);
            (function($$a$1029){
                
                //MethodDef tst at nesting.ceylon (146:8-146:54)
                $$a$1029.tst=function tst(){
                    var $$a$1029=this;
                    return $$$cl1.String("outerf.A.tst()",14);
                };$$a$1029.tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:A$1029,$an:function(){return[$$$cl1.shared()];},d:['nesting','outerf','$c','A','$m','tst']};};
            })(A$1029.$$.prototype);
        }
        return A$1029;
    }
    $init$A$1029();
    
    //ClassDef B at nesting.ceylon (148:4-148:27)
    function B$1030($$b$1030){
        $init$B$1030();
        if ($$b$1030===undefined)$$b$1030=new B$1030.$$;
        A$1029($$b$1030);
        return $$b$1030;
    }
    B$1030.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:A$1029},$ps:[],d:['nesting','outerf','$c','B']};};
    function $init$B$1030(){
        if (B$1030.$$===undefined){
            $$$cl1.initTypeProto(B$1030,'nesting::outerf.B',$init$A$1029());
        }
        return B$1030;
    }
    $init$B$1030();
    return B$1030().tst();
};outerf.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],d:['nesting','outerf']};};

//ClassDef OuterC2 at nesting.ceylon (152:0-160:0)
function OuterC2($$outerC2){
    $init$OuterC2();
    if ($$outerC2===undefined)$$outerC2=new OuterC2.$$;
    return $$outerC2;
}
OuterC2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','OuterC2']};};
function $init$OuterC2(){
    if (OuterC2.$$===undefined){
        $$$cl1.initTypeProto(OuterC2,'nesting::OuterC2',$$$cl1.Basic);
        (function($$outerC2){
            
            //ClassDef A at nesting.ceylon (153:4-155:4)
            function A$OuterC2($$a$1031){
                $init$A$OuterC2();
                if ($$a$1031===undefined)$$a$1031=new this.A$OuterC2.$$;
                $$a$1031.$$outer=this;
                return $$a$1031;
            }
            A$OuterC2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:OuterC2,d:['nesting','OuterC2','$c','A']};};
            function $init$A$OuterC2(){
                if (A$OuterC2.$$===undefined){
                    $$$cl1.initTypeProto(A$OuterC2,'nesting::OuterC2.A',$$$cl1.Basic);
                    OuterC2.A$OuterC2=A$OuterC2;
                    (function($$a$1031){
                        
                        //MethodDef tst at nesting.ceylon (154:8-154:55)
                        $$a$1031.tst=function tst(){
                            var $$a$1031=this;
                            return $$$cl1.String("OuterC2.A.tst()",15);
                        };$$a$1031.tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:A$OuterC2,$an:function(){return[$$$cl1.shared()];},d:['nesting','OuterC2','$c','A','$m','tst']};};
                    })(A$OuterC2.$$.prototype);
                }
                return A$OuterC2;
            }
            $$outerC2.$init$A$OuterC2=$init$A$OuterC2;
            $init$A$OuterC2();
            $$outerC2.A$OuterC2=A$OuterC2;
            
            //MethodDef tst at nesting.ceylon (156:4-159:4)
            $$outerC2.tst=function tst(){
                var $$outerC2=this;
                
                //ClassDef B at nesting.ceylon (157:8-157:31)
                function B$1032($$b$1032){
                    $init$B$1032();
                    if ($$b$1032===undefined)$$b$1032=new B$1032.$$;
                    $$outerC2.A$OuterC2($$b$1032);
                    return $$b$1032;
                }
                B$1032.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:OuterC2.A$OuterC2},$ps:[],d:['nesting','OuterC2','$m','tst','$c','B']};};
                function $init$B$1032(){
                    if (B$1032.$$===undefined){
                        $$$cl1.initTypeProto(B$1032,'nesting::OuterC2.tst.B',$$outerC2.A$OuterC2);
                    }
                    return B$1032;
                }
                $init$B$1032();
                return B$1032().tst();
            };$$outerC2.tst.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:OuterC2,$an:function(){return[$$$cl1.shared()];},d:['nesting','OuterC2','$m','tst']};};
        })(OuterC2.$$.prototype);
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
    $$nameTest.x$1033_=$$$cl1.String("1",1);
    $$nameTest.$prop$getX={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$at','x']};}};
    $$nameTest.$prop$getX.get=function(){return x};
    return $$nameTest;
}
NameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest']};};
exports.NameTest=NameTest;
function $init$NameTest(){
    if (NameTest.$$===undefined){
        $$$cl1.initTypeProto(NameTest,'nesting::NameTest',$$$cl1.Basic);
        (function($$nameTest){
            
            //AttributeDecl x at nesting.ceylon (163:4-163:25)
            $$$cl1.defineAttr($$nameTest,'x',function(){return this.x$1033_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$at','x']};});
            
            //ClassDef NameTest at nesting.ceylon (164:4-176:4)
            function NameTest$NameTest($$nameTest$NameTest){
                $init$NameTest$NameTest();
                if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new this.NameTest$NameTest.$$;
                $$nameTest$NameTest.$$outer=this;
                
                //AttributeDecl x at nesting.ceylon (165:8-165:29)
                $$nameTest$NameTest.x$1034_=$$$cl1.String("2",1);
                $$nameTest$NameTest.$prop$getX.get=function(){return x};
                return $$nameTest$NameTest;
            }
            NameTest$NameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest']};};
            function $init$NameTest$NameTest(){
                if (NameTest$NameTest.$$===undefined){
                    $$$cl1.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest',$$$cl1.Basic);
                    NameTest.NameTest$NameTest=NameTest$NameTest;
                    (function($$nameTest$NameTest){
                        
                        //AttributeDecl x at nesting.ceylon (165:8-165:29)
                        $$$cl1.defineAttr($$nameTest$NameTest,'x',function(){return this.x$1034_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:NameTest$NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest','$at','x']};});
                        
                        //MethodDef f at nesting.ceylon (166:8-175:8)
                        $$nameTest$NameTest.f=function f(){
                            var $$nameTest$NameTest=this;
                            
                            //ClassDef NameTest at nesting.ceylon (167:12-173:12)
                            function NameTest$1035($$nameTest$1035){
                                $init$NameTest$1035();
                                if ($$nameTest$1035===undefined)$$nameTest$1035=new NameTest$1035.$$;
                                
                                //AttributeDecl x at nesting.ceylon (168:16-168:37)
                                $$nameTest$1035.x$1036_=$$$cl1.String("3",1);
                                $$nameTest$1035.$prop$getX.get=function(){return x};
                                return $$nameTest$1035;
                            }
                            NameTest$1035.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest']};};
                            function $init$NameTest$1035(){
                                if (NameTest$1035.$$===undefined){
                                    $$$cl1.initTypeProto(NameTest$1035,'nesting::NameTest.NameTest.f.NameTest',$$$cl1.Basic);
                                    (function($$nameTest$1035){
                                        
                                        //AttributeDecl x at nesting.ceylon (168:16-168:37)
                                        $$$cl1.defineAttr($$nameTest$1035,'x',function(){return this.x$1036_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:NameTest$1035,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$at','x']};});
                                        
                                        //ClassDef NameTest at nesting.ceylon (169:16-171:16)
                                        function NameTest$NameTest($$nameTest$NameTest){
                                            $init$NameTest$NameTest();
                                            if ($$nameTest$NameTest===undefined)$$nameTest$NameTest=new this.NameTest$NameTest.$$;
                                            $$nameTest$NameTest.$$outer=this;
                                            
                                            //AttributeDecl x at nesting.ceylon (170:20-170:41)
                                            $$nameTest$NameTest.x$1037_=$$$cl1.String("4",1);
                                            $$nameTest$NameTest.$prop$getX.get=function(){return x};
                                            return $$nameTest$NameTest;
                                        }
                                        NameTest$NameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:NameTest$1035,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$c','NameTest']};};
                                        function $init$NameTest$NameTest(){
                                            if (NameTest$NameTest.$$===undefined){
                                                $$$cl1.initTypeProto(NameTest$NameTest,'nesting::NameTest.NameTest.f.NameTest.NameTest',$$$cl1.Basic);
                                                NameTest$1035.NameTest$NameTest=NameTest$NameTest;
                                                (function($$nameTest$NameTest){
                                                    
                                                    //AttributeDecl x at nesting.ceylon (170:20-170:41)
                                                    $$$cl1.defineAttr($$nameTest$NameTest,'x',function(){return this.x$1037_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:NameTest$NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$c','NameTest','$at','x']};});
                                                })(NameTest$NameTest.$$.prototype);
                                            }
                                            return NameTest$NameTest;
                                        }
                                        $$nameTest$1035.$init$NameTest$NameTest=$init$NameTest$NameTest;
                                        $init$NameTest$NameTest();
                                        $$nameTest$1035.NameTest$NameTest=NameTest$NameTest;
                                        
                                        //MethodDef f at nesting.ceylon (172:16-172:66)
                                        $$nameTest$1035.f=function f(){
                                            var $$nameTest$1035=this;
                                            return $$nameTest$1035.x.plus($$nameTest$1035.NameTest$NameTest().x);
                                        };$$nameTest$1035.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:NameTest$1035,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f','$c','NameTest','$m','f']};};
                                    })(NameTest$1035.$$.prototype);
                                }
                                return NameTest$1035;
                            }
                            $init$NameTest$1035();
                            return $$nameTest$NameTest.$$outer.x.plus($$nameTest$NameTest.x).plus(NameTest$1035().f());
                        };$$nameTest$NameTest.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:NameTest$NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$c','NameTest','$m','f']};};
                    })(NameTest$NameTest.$$.prototype);
                }
                return NameTest$NameTest;
            }
            $$nameTest.$init$NameTest$NameTest=$init$NameTest$NameTest;
            $init$NameTest$NameTest();
            $$nameTest.NameTest$NameTest=NameTest$NameTest;
            
            //MethodDef f at nesting.ceylon (177:4-177:52)
            $$nameTest.f=function f(){
                var $$nameTest=this;
                return $$nameTest.NameTest$NameTest().f();
            };$$nameTest.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:NameTest,$an:function(){return[$$$cl1.shared()];},d:['nesting','NameTest','$m','f']};};
        })(NameTest.$$.prototype);
    }
    return NameTest;
}
exports.$init$NameTest=$init$NameTest;
$init$NameTest();

//ObjectDef nameTest at nesting.ceylon (180:0-196:0)
function nameTest$1038(){
    var $$nameTest=new nameTest$1038.$$;
    
    //AttributeDecl x at nesting.ceylon (181:4-181:25)
    $$nameTest.x$1039_=$$$cl1.String("1",1);
    $$nameTest.$prop$getX.get=function(){return x};
    
    //ObjectDef nameTest at nesting.ceylon (182:4-194:4)
    $$nameTest.nameTest$1040_=$$nameTest.nameTest$1041();
    return $$nameTest;
};nameTest$1038.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest']};};
function $init$nameTest$1038(){
    if (nameTest$1038.$$===undefined){
        $$$cl1.initTypeProto(nameTest$1038,'nesting::nameTest',$$$cl1.Basic);
    }
    return nameTest$1038;
}
exports.$init$nameTest$1038=$init$nameTest$1038;
$init$nameTest$1038();
(function($$nameTest){
    
    //AttributeDecl x at nesting.ceylon (181:4-181:25)
    $$$cl1.defineAttr($$nameTest,'x',function(){return this.x$1039_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:nameTest$1038,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$at','x']};});
    
    //ObjectDef nameTest at nesting.ceylon (182:4-194:4)
    function nameTest$1041(){
        var $$nameTest$nameTest=new this.nameTest$1041.$$;
        $$nameTest$nameTest.$$outer=this;
        
        //AttributeDecl x at nesting.ceylon (183:8-183:29)
        $$nameTest$nameTest.x$1042_=$$$cl1.String("2",1);
        $$nameTest$nameTest.$prop$getX.get=function(){return x};
        return $$nameTest$nameTest;
    };nameTest$1041.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$cont:nameTest$1038,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest']};};
    function $init$nameTest$1041(){
        if (nameTest$1041.$$===undefined){
            $$$cl1.initTypeProto(nameTest$1041,'nesting::nameTest.nameTest',$$$cl1.Basic);
            nameTest$1038.nameTest$1041=nameTest$1041;
        }
        return nameTest$1041;
    }
    $$nameTest.$init$nameTest$1041=$init$nameTest$1041;
    $init$nameTest$1041();
    (function($$nameTest$nameTest){
        
        //AttributeDecl x at nesting.ceylon (183:8-183:29)
        $$$cl1.defineAttr($$nameTest$nameTest,'x',function(){return this.x$1042_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:nameTest$1041,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$at','x']};});
        
        //MethodDef f at nesting.ceylon (184:8-193:8)
        $$nameTest$nameTest.f=function f(){
            var $$nameTest$nameTest=this;
            
            //ObjectDef nameTest at nesting.ceylon (185:12-191:12)
            function nameTest$1043(){
                var $$nameTest$1043=new nameTest$1043.$$;
                
                //AttributeDecl x at nesting.ceylon (186:16-186:37)
                $$nameTest$1043.x$1044_=$$$cl1.String("3",1);
                $$nameTest$1043.$prop$getX.get=function(){return x};
                
                //ObjectDef nameTest at nesting.ceylon (187:16-189:16)
                $$nameTest$1043.nameTest$1045_=$$nameTest$1043.nameTest$1046();
                return $$nameTest$1043;
            };nameTest$1043.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest']};};
            function $init$nameTest$1043(){
                if (nameTest$1043.$$===undefined){
                    $$$cl1.initTypeProto(nameTest$1043,'nesting::nameTest.nameTest.f.nameTest',$$$cl1.Basic);
                }
                return nameTest$1043;
            }
            $init$nameTest$1043();
            (function($$nameTest$1043){
                
                //AttributeDecl x at nesting.ceylon (186:16-186:37)
                $$$cl1.defineAttr($$nameTest$1043,'x',function(){return this.x$1044_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:nameTest$1043,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$at','x']};});
                
                //ObjectDef nameTest at nesting.ceylon (187:16-189:16)
                function nameTest$1046(){
                    var $$nameTest$nameTest=new this.nameTest$1046.$$;
                    $$nameTest$nameTest.$$outer=this;
                    
                    //AttributeDecl x at nesting.ceylon (188:20-188:41)
                    $$nameTest$nameTest.x$1047_=$$$cl1.String("4",1);
                    $$nameTest$nameTest.$prop$getX.get=function(){return x};
                    return $$nameTest$nameTest;
                };nameTest$1046.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$cont:nameTest$1043,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest']};};
                function $init$nameTest$1046(){
                    if (nameTest$1046.$$===undefined){
                        $$$cl1.initTypeProto(nameTest$1046,'nesting::nameTest.nameTest.f.nameTest.nameTest',$$$cl1.Basic);
                        nameTest$1043.nameTest$1046=nameTest$1046;
                    }
                    return nameTest$1046;
                }
                $$nameTest$1043.$init$nameTest$1046=$init$nameTest$1046;
                $init$nameTest$1046();
                (function($$nameTest$nameTest){
                    
                    //AttributeDecl x at nesting.ceylon (188:20-188:41)
                    $$$cl1.defineAttr($$nameTest$nameTest,'x',function(){return this.x$1047_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:nameTest$1046,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest','$at','x']};});
                })(nameTest$1046.$$.prototype);
                $$$cl1.defineAttr($$nameTest$1043,'nameTest',function(){return this.nameTest$1045_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$1043.nameTest$1046},$cont:nameTest$1043,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest']};});
                $$nameTest$1043.nameTest$1046=nameTest$1046;$$nameTest$1043.nameTest$1046.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$1043.nameTest$1046},$cont:nameTest$1043,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$o','nameTest']};};
                
                //MethodDef f at nesting.ceylon (190:16-190:64)
                $$nameTest$1043.f=function f(){
                    var $$nameTest$1043=this;
                    return $$nameTest$1043.x.plus($$nameTest$1043.nameTest.x);
                };$$nameTest$1043.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:nameTest$1043,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest','$m','f']};};
            })(nameTest$1043.$$.prototype);
            var nameTest$1048;
            function getNameTest$1048(){
                if (nameTest$1048===undefined){nameTest$1048=$init$nameTest$1043()();nameTest$1048.$$metamodel$$=getNameTest$1048.$$metamodel$$;}
                return nameTest$1048;
            }
            getNameTest$1048.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$1043},d:['nesting','nameTest','$o','nameTest','$m','f','$o','nameTest']};};
            $prop$getNameTest$1048={get:getNameTest$1048,$$metamodel$$:getNameTest$1048.$$metamodel$$};
            return $$nameTest$nameTest.$$outer.x.plus($$nameTest$nameTest.x).plus(getNameTest$1048().f());
        };$$nameTest$nameTest.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:nameTest$1041,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest','$m','f']};};
    })(nameTest$1041.$$.prototype);
    $$$cl1.defineAttr($$nameTest,'nameTest',function(){return this.nameTest$1040_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$1038.nameTest$1041},$cont:nameTest$1038,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest']};});
    $$nameTest.nameTest$1041=nameTest$1041;$$nameTest.nameTest$1041.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$1038.nameTest$1041},$cont:nameTest$1038,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$o','nameTest']};};
    
    //MethodDef f at nesting.ceylon (195:4-195:50)
    $$nameTest.f=function f(){
        var $$nameTest=this;
        return $$nameTest.nameTest.f();
    };$$nameTest.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:nameTest$1038,$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest','$m','f']};};
})(nameTest$1038.$$.prototype);
var nameTest$1049;
function getNameTest(){
    if (nameTest$1049===undefined){nameTest$1049=$init$nameTest$1038()();nameTest$1049.$$metamodel$$=getNameTest.$$metamodel$$;}
    return nameTest$1049;
}
exports.getNameTest=getNameTest;
getNameTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:nameTest$1038},$an:function(){return[$$$cl1.shared()];},d:['nesting','nameTest']};};
$prop$getNameTest={get:getNameTest,$$metamodel$$:getNameTest.$$metamodel$$};
exports.$prop$getNameTest=$prop$getNameTest;

//ClassDef C1 at nesting.ceylon (198:0-209:0)
function C1($$c1){
    $init$C1();
    if ($$c1===undefined)$$c1=new C1.$$;
    
    //AttributeDecl x at nesting.ceylon (199:4-199:33)
    $$c1.x$1050_=$$$cl1.String("1",1);
    $$c1.$prop$getX.get=function(){return x};
    return $$c1;
}
C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','C1']};};
exports.C1=C1;
function $init$C1(){
    if (C1.$$===undefined){
        $$$cl1.initTypeProto(C1,'nesting::C1',$$$cl1.Basic);
        (function($$c1){
            
            //AttributeDecl x at nesting.ceylon (199:4-199:33)
            $$$cl1.defineAttr($$c1,'x',function(){return this.x$1050_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:C1,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','C1','$at','x']};});
            
            //ClassDef C1 at nesting.ceylon (200:4-202:4)
            function C1$C1($$c1$C1){
                $init$C1$C1();
                if ($$c1$C1===undefined)$$c1$C1=new this.C1$C1.$$;
                $$c1$C1.$$outer=this;
                
                //AttributeDecl x at nesting.ceylon (201:8-201:38)
                $$c1$C1.x$1051_=$$$cl1.String("11",2);
                $$c1$C1.$prop$getX.get=function(){return x};
                return $$c1$C1;
            }
            C1$C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:C1,$an:function(){return[$$$cl1.shared()];},d:['nesting','C1','$c','C1']};};
            function $init$C1$C1(){
                if (C1$C1.$$===undefined){
                    $$$cl1.initTypeProto(C1$C1,'nesting::C1.C1',$$$cl1.Basic);
                    C1.C1$C1=C1$C1;
                    (function($$c1$C1){
                        
                        //AttributeDecl x at nesting.ceylon (201:8-201:38)
                        $$$cl1.defineAttr($$c1$C1,'x',function(){return this.x$1051_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:C1$C1,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','C1','$c','C1','$at','x']};});
                    })(C1$C1.$$.prototype);
                }
                return C1$C1;
            }
            $$c1.$init$C1$C1=$init$C1$C1;
            $init$C1$C1();
            $$c1.C1$C1=C1$C1;
            
            //ClassDef C3 at nesting.ceylon (203:4-208:4)
            function C3$C1($$c3$C1){
                $init$C3$C1();
                if ($$c3$C1===undefined)$$c3$C1=new this.C3$C1.$$;
                $$c3$C1.$$outer=this;
                $$c3$C1.$$outer.C1$C1($$c3$C1);
                
                //AttributeDecl x at nesting.ceylon (204:8-204:45)
                $$c3$C1.x$1052_=$$$cl1.String("13",2);
                $$c3$C1.$prop$getX.get=function(){return x};
                return $$c3$C1;
            }
            C3$C1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1.C1$C1},$ps:[],$cont:C1,$an:function(){return[$$$cl1.shared()];},d:['nesting','C1','$c','C3']};};
            function $init$C3$C1(){
                if (C3$C1.$$===undefined){
                    $$$cl1.initTypeProto(C3$C1,'nesting::C1.C3',$$c1.C1$C1);
                    C1.C3$C1=C3$C1;
                    (function($$c3$C1){
                        
                        //AttributeDecl x at nesting.ceylon (204:8-204:45)
                        $$$cl1.defineAttr($$c3$C1,'x',function(){return this.x$1052_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:C3$C1,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['nesting','C1','$c','C3','$at','x']};});
                        
                        //MethodDef f at nesting.ceylon (205:8-207:8)
                        $$c3$C1.f=function f(){
                            var $$c3$C1=this;
                            return $$$cl1.StringBuilder().appendAll([$$c3$C1.$$outer.x.string,$$$cl1.String("-",1),$$$cl1.attrGetter($$c3$C1.getT$all()['nesting::C1.C1'],'x').call(this).string,$$$cl1.String("-",1),$$c3$C1.$$outer.C1$C1().x.string,$$$cl1.String("-",1),$$c3$C1.x.string,$$$cl1.String("-",1),$$c3$C1.$$outer.C3$C1().x.string]).string;
                        };$$c3$C1.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:C3$C1,$an:function(){return[$$$cl1.shared()];},d:['nesting','C1','$c','C3','$m','f']};};
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

//ClassDef C2 at nesting.ceylon (210:0-221:0)
function C2($$c2){
    $init$C2();
    if ($$c2===undefined)$$c2=new C2.$$;
    C1($$c2);
    
    //AttributeDecl x at nesting.ceylon (211:4-211:32)
    $$c2.x$1053_=$$$cl1.String("2",1);
    $$c2.$prop$getX.get=function(){return x};
    return $$c2;
}
C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','C2']};};
exports.C2=C2;
function $init$C2(){
    if (C2.$$===undefined){
        $$$cl1.initTypeProto(C2,'nesting::C2',$init$C1());
        (function($$c2){
            
            //AttributeDecl x at nesting.ceylon (211:4-211:32)
            $$$cl1.defineAttr($$c2,'x',function(){return this.x$1053_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:C2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','C2','$at','x']};});
            
            //ClassDef C2 at nesting.ceylon (212:4-220:4)
            function C2$C2($$c2$C2){
                $init$C2$C2();
                if ($$c2$C2===undefined)$$c2$C2=new this.C2$C2.$$;
                $$c2$C2.$$outer=this;
                $$c2$C2.$$outer.getT$all()['nesting::C1'].$$.prototype.C1$C1.call(this,$$c2$C2);
                
                //AttributeDecl x at nesting.ceylon (213:8-213:37)
                $$c2$C2.x$1054_=$$$cl1.String("22",2);
                $$c2$C2.$prop$getX.get=function(){return x};
                return $$c2$C2;
            }
            C2$C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1.C1$C1},$ps:[],$cont:C2,$an:function(){return[$$$cl1.shared()];},d:['nesting','C2','$c','C2']};};
            function $init$C2$C2(){
                if (C2$C2.$$===undefined){
                    $$$cl1.initTypeProto(C2$C2,'nesting::C2.C2',$$c2.getT$all()['nesting::C1'].$$.prototype.C1$C1);
                    C2.C2$C2=C2$C2;
                    (function($$c2$C2){
                        
                        //AttributeDecl x at nesting.ceylon (213:8-213:37)
                        $$$cl1.defineAttr($$c2$C2,'x',function(){return this.x$1054_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:C2$C2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','C2','$c','C2','$at','x']};});
                        
                        //ClassDef C2 at nesting.ceylon (214:8-216:8)
                        function C2$C2$C2($$c2$C2$C2){
                            $init$C2$C2$C2();
                            if ($$c2$C2$C2===undefined)$$c2$C2$C2=new this.C2$C2$C2.$$;
                            $$c2$C2$C2.$$outer=this;
                            $$c2$C2$C2.$$outer.$$outer.C3$C1($$c2$C2$C2);
                            
                            //AttributeDecl x at nesting.ceylon (215:12-215:42)
                            $$c2$C2$C2.x$1055_=$$$cl1.String("222",3);
                            $$c2$C2$C2.$prop$getX.get=function(){return x};
                            return $$c2$C2$C2;
                        }
                        C2$C2$C2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:C1.C3$C1},$ps:[],$cont:C2$C2,$an:function(){return[$$$cl1.shared()];},d:['nesting','C2','$c','C2','$c','C2']};};
                        function $init$C2$C2$C2(){
                            if (C2$C2$C2.$$===undefined){
                                $$$cl1.initTypeProto(C2$C2$C2,'nesting::C2.C2.C2',$$c2.C3$C1);
                                C2.C2$C2.C2$C2$C2=C2$C2$C2;
                                (function($$c2$C2$C2){
                                    
                                    //AttributeDecl x at nesting.ceylon (215:12-215:42)
                                    $$$cl1.defineAttr($$c2$C2$C2,'x',function(){return this.x$1055_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:C2$C2$C2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','C2','$c','C2','$c','C2','$at','x']};});
                                })(C2$C2$C2.$$.prototype);
                            }
                            return C2$C2$C2;
                        }
                        $$c2$C2.$init$C2$C2$C2=$init$C2$C2$C2;
                        $init$C2$C2$C2();
                        $$c2$C2.C2$C2$C2=C2$C2$C2;
                        
                        //MethodDef f at nesting.ceylon (217:8-219:8)
                        $$c2$C2.f=function f(){
                            var $$c2$C2=this;
                            return $$$cl1.StringBuilder().appendAll([$$c2$C2.$$outer.x.string,$$$cl1.String("-",1),$$c2$C2.$$outer.C1$C1().x.string,$$$cl1.String("-",1),$$c2$C2.x.string,$$$cl1.String("-",1),$$$cl1.attrGetter($$c2$C2.getT$all()['nesting::C1.C1'],'x').call(this).string,$$$cl1.String("-",1),$$c2$C2.$$outer.C3$C1().x.string,$$$cl1.String("-",1),$$c2$C2.C2$C2$C2().x.string,$$$cl1.String("-",1),$$c2$C2.C2$C2$C2().f().string,$$$cl1.String("-",1),$$c2$C2.$$outer.C3$C1().f().string]).string;
                        };$$c2$C2.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:C2$C2,$an:function(){return[$$$cl1.shared()];},d:['nesting','C2','$c','C2','$m','f']};};
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

//MethodDef test at nesting.ceylon (223:0-253:0)
function test(){
    outr($$$cl1.String("Hello",5));
    $$$c2.check(Holder($$$cl1.String("ok",2)).$get().string.equals($$$cl1.String("ok",2)),$$$cl1.String("holder(ok)",10));
    $$$c2.check(Holder($$$cl1.String("ok",2)).string.equals($$$cl1.String("ok",2)),$$$cl1.String("holder.string",13));
    $$$c2.check(Wrapper().$get().string.equals($$$cl1.String("100",3)),$$$cl1.String("wrapper 1",9));
    $$$c2.check(Wrapper().string.equals($$$cl1.String("100",3)),$$$cl1.String("wrapper 2",9));
    $$$c2.check(Unwrapper().$get().string.equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 1",11));
    $$$c2.check(Unwrapper().o.string.equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 2",11));
    $$$c2.check(Unwrapper().string.equals($$$cl1.String("23.56",5)),$$$cl1.String("unwrapper 3",11));
    $$$c2.check($$$cl1.isOfType(producer(),{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}}}),$$$cl1.StringBuilder().appendAll([$$$cl1.String("function 1 is ",14),$$$cl1.className($$$cl1.$JsCallable(producer(),[],{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Object}})).string]).string);
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
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','test']};};

//ClassDef X at refinement.ceylon (4:0-17:0)
function X($$x){
    $init$X();
    if ($$x===undefined)$$x=new X.$$;
    return $$x;
}
X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','X']};};
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl1.initTypeProto(X,'nesting::X',$$$cl1.Basic);
        (function($$x){
            
            //ClassDef RefineTest1 at refinement.ceylon (5:4-16:4)
            function RefineTest1$X($$refineTest1$X){
                $init$RefineTest1$X();
                if ($$refineTest1$X===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
                $$refineTest1$X.$$outer=this;
                return $$refineTest1$X;
            }
            RefineTest1$X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:X,$an:function(){return[$$$cl1.abstract(),$$$cl1.shared()];},d:['nesting','X','$c','RefineTest1']};};
            function $init$RefineTest1$X(){
                if (RefineTest1$X.$$===undefined){
                    $$$cl1.initTypeProto(RefineTest1$X,'nesting::X.RefineTest1',$$$cl1.Basic);
                    X.RefineTest1$X=RefineTest1$X;
                    (function($$refineTest1$X){
                        
                        //ClassDef Inner at refinement.ceylon (6:8-12:8)
                        function Inner$RefineTest1$X($$inner$RefineTest1$X){
                            $init$Inner$RefineTest1$X();
                            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new this.Inner$RefineTest1$X.$$;
                            $$inner$RefineTest1$X.$$outer=this;
                            
                            //AttributeDecl origin at refinement.ceylon (7:12-7:54)
                            $$inner$RefineTest1$X.origin$1056_=$$$cl1.String("RefineTest1.Inner",17);
                            $$inner$RefineTest1$X.$prop$getOrigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$at','origin']};}};
                            $$inner$RefineTest1$X.$prop$getOrigin.get=function(){return origin};
                            return $$inner$RefineTest1$X;
                        }
                        Inner$RefineTest1$X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:RefineTest1$X,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['nesting','X','$c','RefineTest1','$c','Inner']};};
                        function $init$Inner$RefineTest1$X(){
                            if (Inner$RefineTest1$X.$$===undefined){
                                $$$cl1.initTypeProto(Inner$RefineTest1$X,'nesting::X.RefineTest1.Inner',$$$cl1.Basic);
                                X.RefineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
                                (function($$inner$RefineTest1$X){
                                    
                                    //AttributeDecl origin at refinement.ceylon (7:12-7:54)
                                    $$$cl1.defineAttr($$inner$RefineTest1$X,'origin',function(){return this.origin$1056_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$at','origin']};});
                                    
                                    //MethodDef x at refinement.ceylon (8:12-10:12)
                                    $$inner$RefineTest1$X.x=function x(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl1.String("x and ",6).plus($$inner$RefineTest1$X.y());
                                    };$$inner$RefineTest1$X.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$m','x']};};
                                    $$inner$RefineTest1$X.y={$fml:1,$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['nesting','X','$c','RefineTest1','$c','Inner','$m','y']};}};
                                })(Inner$RefineTest1$X.$$.prototype);
                            }
                            return Inner$RefineTest1$X;
                        }
                        $$refineTest1$X.$init$Inner$RefineTest1$X=$init$Inner$RefineTest1$X;
                        $init$Inner$RefineTest1$X();
                        $$refineTest1$X.Inner$RefineTest1$X=Inner$RefineTest1$X;
                        
                        //MethodDef outerx at refinement.ceylon (13:8-15:8)
                        $$refineTest1$X.outerx=function outerx(){
                            var $$refineTest1$X=this;
                            return $$refineTest1$X.Inner$RefineTest1$X().x();
                        };$$refineTest1$X.outerx.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:RefineTest1$X,$an:function(){return[$$$cl1.shared()];},d:['nesting','X','$c','RefineTest1','$m','outerx']};};
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

//ClassDef RefineTest2 at refinement.ceylon (20:0-24:0)
function RefineTest2($$refineTest2){
    $init$RefineTest2();
    if ($$refineTest2===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
    return $$refineTest2;
}
RefineTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.abstract(),$$$cl1.shared()];},d:['nesting','RefineTest2']};};
exports.RefineTest2=RefineTest2;
function $init$RefineTest2(){
    if (RefineTest2.$$===undefined){
        $$$cl1.initTypeProto(RefineTest2,'nesting::RefineTest2',$$$cl1.Basic);
        (function($$refineTest2){
            
            //ClassDef Inner at refinement.ceylon (21:4-23:4)
            function Inner$RefineTest2($$inner$RefineTest2){
                $init$Inner$RefineTest2();
                if ($$inner$RefineTest2===undefined)$$inner$RefineTest2=new this.Inner$RefineTest2.$$;
                $$inner$RefineTest2.$$outer=this;
                return $$inner$RefineTest2;
            }
            Inner$RefineTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:RefineTest2,$an:function(){return[$$$cl1.shared()];},d:['nesting','RefineTest2','$c','Inner']};};
            function $init$Inner$RefineTest2(){
                if (Inner$RefineTest2.$$===undefined){
                    $$$cl1.initTypeProto(Inner$RefineTest2,'nesting::RefineTest2.Inner',$$$cl1.Basic);
                    RefineTest2.Inner$RefineTest2=Inner$RefineTest2;
                    (function($$inner$RefineTest2){
                        
                        //MethodDef hello at refinement.ceylon (22:8-22:71)
                        $$inner$RefineTest2.hello=function hello(){
                            var $$inner$RefineTest2=this;
                            return $$$cl1.String("hello from RefineTest2.Inner",28);
                        };$$inner$RefineTest2.hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest2,$an:function(){return[$$$cl1.shared()];},d:['nesting','RefineTest2','$c','Inner','$m','hello']};};
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

//ClassDef RefineTest3 at refinement.ceylon (27:0-33:0)
function RefineTest3($$refineTest3){
    $init$RefineTest3();
    if ($$refineTest3===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
    return $$refineTest3;
}
RefineTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.abstract(),$$$cl1.shared()];},d:['nesting','RefineTest3']};};
exports.RefineTest3=RefineTest3;
function $init$RefineTest3(){
    if (RefineTest3.$$===undefined){
        $$$cl1.initTypeProto(RefineTest3,'nesting::RefineTest3',$$$cl1.Basic);
        (function($$refineTest3){
            
            //ClassDef Inner at refinement.ceylon (28:4-32:4)
            function Inner$RefineTest3($$inner$RefineTest3){
                $init$Inner$RefineTest3();
                if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new this.Inner$RefineTest3.$$;
                $$inner$RefineTest3.$$outer=this;
                return $$inner$RefineTest3;
            }
            Inner$RefineTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:RefineTest3,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','RefineTest3','$c','Inner']};};
            function $init$Inner$RefineTest3(){
                if (Inner$RefineTest3.$$===undefined){
                    $$$cl1.initTypeProto(Inner$RefineTest3,'nesting::RefineTest3.Inner',$$$cl1.Basic);
                    RefineTest3.Inner$RefineTest3=Inner$RefineTest3;
                    (function($$inner$RefineTest3){
                        
                        //MethodDef x at refinement.ceylon (29:8-31:8)
                        $$inner$RefineTest3.x=function x(){
                            var $$inner$RefineTest3=this;
                            return $$$cl1.String("x",1);
                        };$$inner$RefineTest3.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest3,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','RefineTest3','$c','Inner','$m','x']};};
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

//ClassDef Y at refinement.ceylon (35:0-47:0)
function Y($$y){
    $init$Y();
    if ($$y===undefined)$$y=new Y.$$;
    X($$y);
    return $$y;
}
Y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','Y']};};
exports.Y=Y;
function $init$Y(){
    if (Y.$$===undefined){
        $$$cl1.initTypeProto(Y,'nesting::Y',$init$X());
        (function($$y){
            
            //ClassDef SubRef1 at refinement.ceylon (36:4-46:4)
            function SubRef1$Y($$subRef1$Y){
                $init$SubRef1$Y();
                if ($$subRef1$Y===undefined)$$subRef1$Y=new this.SubRef1$Y.$$;
                $$subRef1$Y.$$outer=this;
                $$subRef1$Y.$$outer.RefineTest1$X($$subRef1$Y);
                return $$subRef1$Y;
            }
            SubRef1$Y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X.RefineTest1$X},$ps:[],$cont:Y,$an:function(){return[$$$cl1.shared()];},d:['nesting','Y','$c','SubRef1']};};
            function $init$SubRef1$Y(){
                if (SubRef1$Y.$$===undefined){
                    $$$cl1.initTypeProto(SubRef1$Y,'nesting::Y.SubRef1',$$y.RefineTest1$X);
                    Y.SubRef1$Y=SubRef1$Y;
                    (function($$subRef1$Y){
                        
                        //ClassDef Inner at refinement.ceylon (37:6-45:6)
                        function Inner$RefineTest1$X($$inner$RefineTest1$X){
                            $init$Inner$RefineTest1$X();
                            if ($$inner$RefineTest1$X===undefined)$$inner$RefineTest1$X=new this.Inner$RefineTest1$X.$$;
                            $$inner$RefineTest1$X.$$outer=this;
                            $$inner$RefineTest1$X.$$outer.getT$all()['nesting::X.RefineTest1'].$$.prototype.Inner$RefineTest1$X.call(this,$$inner$RefineTest1$X);
                            
                            //AttributeDecl suborigin at refinement.ceylon (38:10-38:51)
                            $$inner$RefineTest1$X.suborigin$1057_=$$$cl1.String("SubRef1.Inner",13);
                            $$inner$RefineTest1$X.$prop$getSuborigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$at','suborigin']};}};
                            $$inner$RefineTest1$X.$prop$getSuborigin.get=function(){return suborigin};
                            return $$inner$RefineTest1$X;
                        }
                        Inner$RefineTest1$X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X.RefineTest1$X.Inner$RefineTest1$X},$ps:[],$cont:SubRef1$Y,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Y','$c','SubRef1','$c','Inner']};};
                        function $init$Inner$RefineTest1$X(){
                            if (Inner$RefineTest1$X.$$===undefined){
                                $$$cl1.initTypeProto(Inner$RefineTest1$X,'nesting::Y.SubRef1.Inner',$$subRef1$Y.getT$all()['nesting::X.RefineTest1'].$$.prototype.Inner$RefineTest1$X);
                                Y.SubRef1$Y.Inner$RefineTest1$X=Inner$RefineTest1$X;
                                (function($$inner$RefineTest1$X){
                                    
                                    //AttributeDecl suborigin at refinement.ceylon (38:10-38:51)
                                    $$$cl1.defineAttr($$inner$RefineTest1$X,'suborigin',function(){return this.suborigin$1057_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$at','suborigin']};});
                                    
                                    //MethodDef x at refinement.ceylon (39:10-41:10)
                                    $$inner$RefineTest1$X.x=function x(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl1.String("REFINED ",8).plus($$inner$RefineTest1$X.getT$all()['nesting::X.RefineTest1.Inner'].$$.prototype.x.call(this));
                                    };$$inner$RefineTest1$X.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$m','x']};};
                                    
                                    //MethodDef y at refinement.ceylon (42:10-44:10)
                                    $$inner$RefineTest1$X.y=function y(){
                                        var $$inner$RefineTest1$X=this;
                                        return $$$cl1.String("y",1);
                                    };$$inner$RefineTest1$X.y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Y','$c','SubRef1','$c','Inner','$m','y']};};
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

//ClassDef SubRef2 at refinement.ceylon (49:0-53:0)
function SubRef2($$subRef2){
    $init$SubRef2();
    if ($$subRef2===undefined)$$subRef2=new SubRef2.$$;
    RefineTest2($$subRef2);
    return $$subRef2;
}
SubRef2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest2},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef2']};};
exports.SubRef2=SubRef2;
function $init$SubRef2(){
    if (SubRef2.$$===undefined){
        $$$cl1.initTypeProto(SubRef2,'nesting::SubRef2',$init$RefineTest2());
        (function($$subRef2){
            
            //MethodDef x at refinement.ceylon (50:4-52:4)
            $$subRef2.x=function x(){
                var $$subRef2=this;
                return $$subRef2.Inner$RefineTest2().hello();
            };$$subRef2.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:SubRef2,$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef2','$m','x']};};
        })(SubRef2.$$.prototype);
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
    return $$subRef3;
}
SubRef3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest3},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef3']};};
exports.SubRef3=SubRef3;
function $init$SubRef3(){
    if (SubRef3.$$===undefined){
        $$$cl1.initTypeProto(SubRef3,'nesting::SubRef3',$init$RefineTest3());
        (function($$subRef3){
            
            //MethodDef x at refinement.ceylon (56:4-58:4)
            $$subRef3.x=function x(){
                var $$subRef3=this;
                return $$subRef3.Inner$RefineTest3().x();
            };$$subRef3.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:SubRef3,$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef3','$m','x']};};
        })(SubRef3.$$.prototype);
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
    return $$subRef31;
}
SubRef31.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:SubRef3},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef31']};};
exports.SubRef31=SubRef31;
function $init$SubRef31(){
    if (SubRef31.$$===undefined){
        $$$cl1.initTypeProto(SubRef31,'nesting::SubRef31',$init$SubRef3());
        (function($$subRef31){
            
            //ClassDef Inner at refinement.ceylon (61:4-63:4)
            function Inner$RefineTest3($$inner$RefineTest3){
                $init$Inner$RefineTest3();
                if ($$inner$RefineTest3===undefined)$$inner$RefineTest3=new this.Inner$RefineTest3.$$;
                $$inner$RefineTest3.$$outer=this;
                $$inner$RefineTest3.$$outer.getT$all()['nesting::RefineTest3'].$$.prototype.Inner$RefineTest3.call(this,$$inner$RefineTest3);
                return $$inner$RefineTest3;
            }
            Inner$RefineTest3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest3.Inner$RefineTest3},$ps:[],$cont:SubRef31,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','SubRef31','$c','Inner']};};
            function $init$Inner$RefineTest3(){
                if (Inner$RefineTest3.$$===undefined){
                    $$$cl1.initTypeProto(Inner$RefineTest3,'nesting::SubRef31.Inner',$$subRef31.getT$all()['nesting::RefineTest3'].$$.prototype.Inner$RefineTest3);
                    SubRef31.Inner$RefineTest3=Inner$RefineTest3;
                    (function($$inner$RefineTest3){
                        
                        //MethodDef x at refinement.ceylon (62:8-62:51)
                        $$inner$RefineTest3.x=function x(){
                            var $$inner$RefineTest3=this;
                            return $$$cl1.String("equis",5);
                        };$$inner$RefineTest3.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest3,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','SubRef31','$c','Inner','$m','x']};};
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

//MethodDef testRefinement at refinement.ceylon (66:0-76:0)
function testRefinement(){
    
    //AttributeDecl c1 at refinement.ceylon (67:4-67:36)
    var c1$1058=Y().SubRef1$Y().Inner$RefineTest1$X();
    $$$c2.check($$$cl1.className(c1$1058).equals($$$cl1.String("nesting::Y.SubRef1.Inner",24)),$$$cl1.String("classname is ",13).plus($$$cl1.className(c1$1058)));
    $$$c2.check(c1$1058.origin.equals($$$cl1.String("RefineTest1.Inner",17)),$$$cl1.String("refinement [1]",14));
    $$$c2.check(c1$1058.suborigin.equals($$$cl1.String("SubRef1.Inner",13)),$$$cl1.String("refinement [2]",14));
    $$$c2.check(c1$1058.x().equals($$$cl1.String("REFINED x and y",15)),$$$cl1.String("refinement [3]",14));
    $$$c2.check(c1$1058.x().equals(Y().SubRef1$Y().outerx()),$$$cl1.String("refinement [4]",14));
    $$$c2.check(SubRef2().x().equals($$$cl1.String("hello from RefineTest2.Inner",28)),$$$cl1.String("refinement [5]",14));
    $$$c2.check(SubRef3().x().equals($$$cl1.String("x",1)),$$$cl1.String("refinement [6]",14));
    $$$c2.check(SubRef31().x().equals($$$cl1.String("equis",5)),$$$cl1.String("refinement [7]",14));
};testRefinement.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['nesting','testRefinement']};};

//ClassDef X2 at refinement2.ceylon (4:0-18:0)
function X2(a$1059, $$x2){
    $init$X2();
    if ($$x2===undefined)$$x2=new X2.$$;
    $$x2.a$1059_=a$1059;
    return $$x2;
}
X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['nesting','X2']};};
exports.X2=X2;
function $init$X2(){
    if (X2.$$===undefined){
        $$$cl1.initTypeProto(X2,'nesting::X2',$$$cl1.Basic);
        (function($$x2){
            
            //ClassDef RefineTest1 at refinement2.ceylon (5:4-17:4)
            function RefineTest1$X2(b$1060, $$refineTest1$X2){
                $init$RefineTest1$X2();
                if ($$refineTest1$X2===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
                $$refineTest1$X2.$$outer=this;
                $$refineTest1$X2.b$1060_=b$1060;
                return $$refineTest1$X2;
            }
            RefineTest1$X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:X2,$an:function(){return[$$$cl1.abstract(),$$$cl1.shared()];},d:['nesting','X2','$c','RefineTest1']};};
            function $init$RefineTest1$X2(){
                if (RefineTest1$X2.$$===undefined){
                    $$$cl1.initTypeProto(RefineTest1$X2,'nesting::X2.RefineTest1',$$$cl1.Basic);
                    X2.RefineTest1$X2=RefineTest1$X2;
                    (function($$refineTest1$X2){
                        
                        //ClassDef Inner at refinement2.ceylon (6:8-13:8)
                        function Inner$RefineTest1$X2(c$1061, $$targs$$,$$inner$RefineTest1$X2){
                            $init$Inner$RefineTest1$X2();
                            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new this.Inner$RefineTest1$X2.$$;
                            $$$cl1.set_type_args($$inner$RefineTest1$X2,$$targs$$);
                            $$inner$RefineTest1$X2.$$outer=this;
                            $$inner$RefineTest1$X2.c$1061_=c$1061;
                            
                            //AttributeDecl origin at refinement2.ceylon (8:12-8:62)
                            $$inner$RefineTest1$X2.origin$1062_=$$$cl1.StringBuilder().appendAll([$$$cl1.String("RefineTest1.Inner (",19),$$inner$RefineTest1$X2.c$1061.string,$$$cl1.String(")",1)]).string;
                            $$inner$RefineTest1$X2.$prop$getOrigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$at','origin']};}};
                            $$inner$RefineTest1$X2.$prop$getOrigin.get=function(){return origin};
                            return $$inner$RefineTest1$X2;
                        }
                        Inner$RefineTest1$X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'c',$mt:'prm',$t:'Element',$an:function(){return[];}}],$cont:RefineTest1$X2,$tp:{Element:{'satisfies':[{t:$$$cl1.Object}]}},$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['nesting','X2','$c','RefineTest1','$c','Inner']};};
                        function $init$Inner$RefineTest1$X2(){
                            if (Inner$RefineTest1$X2.$$===undefined){
                                $$$cl1.initTypeProto(Inner$RefineTest1$X2,'nesting::X2.RefineTest1.Inner',$$$cl1.Basic);
                                X2.RefineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                                (function($$inner$RefineTest1$X2){
                                    
                                    //AttributeDecl origin at refinement2.ceylon (8:12-8:62)
                                    $$$cl1.defineAttr($$inner$RefineTest1$X2,'origin',function(){return this.origin$1062_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$at','origin']};});
                                    
                                    //MethodDef x at refinement2.ceylon (9:12-11:12)
                                    $$inner$RefineTest1$X2.x=function x(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("x and ",6),$$inner$RefineTest1$X2.y().string,$$$cl1.String(" and a:",7),$$inner$RefineTest1$X2.$$outer.$$outer.a$1059.string,$$$cl1.String(", b:",4),$$inner$RefineTest1$X2.$$outer.b$1060.string,$$$cl1.String(", c:",4),$$inner$RefineTest1$X2.c$1061.string,$$$cl1.String(".",1)]).string;
                                    };$$inner$RefineTest1$X2.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$m','x']};};
                                    $$inner$RefineTest1$X2.y={$fml:1,$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['nesting','X2','$c','RefineTest1','$c','Inner','$m','y']};}};$$$cl1.defineAttr($$inner$RefineTest1$X2,'c$1061',function(){return this.c$1061_;},undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:Inner$RefineTest1$X2,d:['nesting','X2','$c','RefineTest1','$c','Inner','$at','c']};});
                                })(Inner$RefineTest1$X2.$$.prototype);
                            }
                            return Inner$RefineTest1$X2;
                        }
                        $$refineTest1$X2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
                        $init$Inner$RefineTest1$X2();
                        $$refineTest1$X2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                        
                        //MethodDef outerx at refinement2.ceylon (14:8-16:8)
                        $$refineTest1$X2.outerx=function outerx(){
                            var $$refineTest1$X2=this;
                            return $$refineTest1$X2.Inner$RefineTest1$X2($$refineTest1$X2.$$outer.a$1059.uppercased,{Element:{t:$$$cl1.String}}).x();
                        };$$refineTest1$X2.outerx.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:RefineTest1$X2,$an:function(){return[$$$cl1.shared()];},d:['nesting','X2','$c','RefineTest1','$m','outerx']};};
                        $$$cl1.defineAttr($$refineTest1$X2,'b$1060',function(){return this.b$1060_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:RefineTest1$X2,d:['nesting','X2','$c','RefineTest1','$at','b']};});
                    })(RefineTest1$X2.$$.prototype);
                }
                return RefineTest1$X2;
            }
            $$x2.$init$RefineTest1$X2=$init$RefineTest1$X2;
            $init$RefineTest1$X2();
            $$x2.RefineTest1$X2=RefineTest1$X2;
            $$$cl1.defineAttr($$x2,'a$1059',function(){return this.a$1059_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:X2,d:['nesting','X2','$at','a']};});
        })(X2.$$.prototype);
    }
    return X2;
}
exports.$init$X2=$init$X2;
$init$X2();

//ClassDef RefineTest4 at refinement2.ceylon (21:0-25:0)
function RefineTest4(d$1063, $$refineTest4){
    $init$RefineTest4();
    if ($$refineTest4===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
    $$refineTest4.d$1063_=d$1063;
    return $$refineTest4;
}
RefineTest4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'d',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$an:function(){return[$$$cl1.abstract(),$$$cl1.shared()];},d:['nesting','RefineTest4']};};
exports.RefineTest4=RefineTest4;
function $init$RefineTest4(){
    if (RefineTest4.$$===undefined){
        $$$cl1.initTypeProto(RefineTest4,'nesting::RefineTest4',$$$cl1.Basic);
        (function($$refineTest4){
            
            //ClassDef Inner at refinement2.ceylon (22:4-24:4)
            function Inner$RefineTest4(e$1064, $$inner$RefineTest4){
                $init$Inner$RefineTest4();
                if ($$inner$RefineTest4===undefined)$$inner$RefineTest4=new this.Inner$RefineTest4.$$;
                $$inner$RefineTest4.$$outer=this;
                $$inner$RefineTest4.e$1064_=e$1064;
                return $$inner$RefineTest4;
            }
            Inner$RefineTest4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'e',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:RefineTest4,$an:function(){return[$$$cl1.shared()];},d:['nesting','RefineTest4','$c','Inner']};};
            function $init$Inner$RefineTest4(){
                if (Inner$RefineTest4.$$===undefined){
                    $$$cl1.initTypeProto(Inner$RefineTest4,'nesting::RefineTest4.Inner',$$$cl1.Basic);
                    RefineTest4.Inner$RefineTest4=Inner$RefineTest4;
                    (function($$inner$RefineTest4){
                        
                        //MethodDef hello at refinement2.ceylon (23:8-23:83)
                        $$inner$RefineTest4.hello=function hello(){
                            var $$inner$RefineTest4=this;
                            return $$$cl1.StringBuilder().appendAll([$$$cl1.String("hello from RefineTest2.Inner with ",34),$$inner$RefineTest4.e$1064.string,$$$cl1.String(".",1)]).string;
                        };$$inner$RefineTest4.hello.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest4,$an:function(){return[$$$cl1.shared()];},d:['nesting','RefineTest4','$c','Inner','$m','hello']};};
                        $$$cl1.defineAttr($$inner$RefineTest4,'e$1064',function(){return this.e$1064_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Inner$RefineTest4,d:['nesting','RefineTest4','$c','Inner','$at','e']};});
                    })(Inner$RefineTest4.$$.prototype);
                }
                return Inner$RefineTest4;
            }
            $$refineTest4.$init$Inner$RefineTest4=$init$Inner$RefineTest4;
            $init$Inner$RefineTest4();
            $$refineTest4.Inner$RefineTest4=Inner$RefineTest4;
            $$$cl1.defineAttr($$refineTest4,'d$1063',function(){return this.d$1063_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:RefineTest4,d:['nesting','RefineTest4','$at','d']};});
        })(RefineTest4.$$.prototype);
    }
    return RefineTest4;
}
exports.$init$RefineTest4=$init$RefineTest4;
$init$RefineTest4();

//ClassDef RefineTest5 at refinement2.ceylon (28:0-34:0)
function RefineTest5(f$1065, $$refineTest5){
    $init$RefineTest5();
    if ($$refineTest5===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
    $$refineTest5.f$1065_=f$1065;
    return $$refineTest5;
}
RefineTest5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$an:function(){return[$$$cl1.abstract(),$$$cl1.shared()];},d:['nesting','RefineTest5']};};
exports.RefineTest5=RefineTest5;
function $init$RefineTest5(){
    if (RefineTest5.$$===undefined){
        $$$cl1.initTypeProto(RefineTest5,'nesting::RefineTest5',$$$cl1.Basic);
        (function($$refineTest5){
            
            //ClassDef Inner at refinement2.ceylon (29:4-33:4)
            function Inner$RefineTest5(g$1066, $$inner$RefineTest5){
                $init$Inner$RefineTest5();
                if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new this.Inner$RefineTest5.$$;
                $$inner$RefineTest5.$$outer=this;
                $$inner$RefineTest5.g$1066_=g$1066;
                return $$inner$RefineTest5;
            }
            Inner$RefineTest5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'g',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$cont:RefineTest5,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','RefineTest5','$c','Inner']};};
            function $init$Inner$RefineTest5(){
                if (Inner$RefineTest5.$$===undefined){
                    $$$cl1.initTypeProto(Inner$RefineTest5,'nesting::RefineTest5.Inner',$$$cl1.Basic);
                    RefineTest5.Inner$RefineTest5=Inner$RefineTest5;
                    (function($$inner$RefineTest5){
                        
                        //MethodDef x at refinement2.ceylon (30:8-32:8)
                        $$inner$RefineTest5.x=function x(){
                            var $$inner$RefineTest5=this;
                            return $$inner$RefineTest5.g$1066.repeat($$inner$RefineTest5.$$outer.f$1065);
                        };$$inner$RefineTest5.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest5,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['nesting','RefineTest5','$c','Inner','$m','x']};};
                        $$$cl1.defineAttr($$inner$RefineTest5,'g$1066',function(){return this.g$1066_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest5,d:['nesting','RefineTest5','$c','Inner','$at','g']};});
                    })(Inner$RefineTest5.$$.prototype);
                }
                return Inner$RefineTest5;
            }
            $$refineTest5.$init$Inner$RefineTest5=$init$Inner$RefineTest5;
            $init$Inner$RefineTest5();
            $$refineTest5.Inner$RefineTest5=Inner$RefineTest5;
            $$$cl1.defineAttr($$refineTest5,'f$1065',function(){return this.f$1065_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:RefineTest5,d:['nesting','RefineTest5','$at','f']};});
        })(RefineTest5.$$.prototype);
    }
    return RefineTest5;
}
exports.$init$RefineTest5=$init$RefineTest5;
$init$RefineTest5();

//ClassDef Y2 at refinement2.ceylon (36:0-49:0)
function Y2(h$1067, $$y2){
    $init$Y2();
    if ($$y2===undefined)$$y2=new Y2.$$;
    $$y2.h$1067_=h$1067;
    X2($$y2.h$1067,$$y2);
    return $$y2;
}
Y2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X2},$ps:[{$nm:'h',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['nesting','Y2']};};
exports.Y2=Y2;
function $init$Y2(){
    if (Y2.$$===undefined){
        $$$cl1.initTypeProto(Y2,'nesting::Y2',$init$X2());
        (function($$y2){
            
            //ClassDef SubRef1 at refinement2.ceylon (37:4-48:4)
            function SubRef1$Y2(d$1068, $$subRef1$Y2){
                $init$SubRef1$Y2();
                if ($$subRef1$Y2===undefined)$$subRef1$Y2=new this.SubRef1$Y2.$$;
                $$subRef1$Y2.$$outer=this;
                $$subRef1$Y2.d$1068_=d$1068;
                $$subRef1$Y2.$$outer.RefineTest1$X2((1),$$subRef1$Y2);
                return $$subRef1$Y2;
            }
            SubRef1$Y2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X2.RefineTest1$X2},$ps:[{$nm:'d',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:Y2,$an:function(){return[$$$cl1.shared()];},d:['nesting','Y2','$c','SubRef1']};};
            function $init$SubRef1$Y2(){
                if (SubRef1$Y2.$$===undefined){
                    $$$cl1.initTypeProto(SubRef1$Y2,'nesting::Y2.SubRef1',$$y2.RefineTest1$X2);
                    Y2.SubRef1$Y2=SubRef1$Y2;
                    (function($$subRef1$Y2){
                        
                        //ClassDef Inner at refinement2.ceylon (38:6-47:6)
                        function Inner$RefineTest1$X2(d2$1069, $$targs$$,$$inner$RefineTest1$X2){
                            $init$Inner$RefineTest1$X2();
                            if ($$inner$RefineTest1$X2===undefined)$$inner$RefineTest1$X2=new this.Inner$RefineTest1$X2.$$;
                            $$$cl1.set_type_args($$inner$RefineTest1$X2,$$targs$$);
                            $$inner$RefineTest1$X2.$$outer=this;
                            $$inner$RefineTest1$X2.d2$1069_=d2$1069;
                            $$inner$RefineTest1$X2.$$outer.getT$all()['nesting::X2.RefineTest1'].$$.prototype.Inner$RefineTest1$X2.call(this,$$inner$RefineTest1$X2.d2$1069,{Element:$$inner$RefineTest1$X2.$$targs$$.Element},$$inner$RefineTest1$X2);
                            
                            //AttributeDecl suborigin at refinement2.ceylon (40:10-40:51)
                            $$inner$RefineTest1$X2.suborigin$1070_=$$$cl1.String("SubRef1.Inner",13);
                            $$inner$RefineTest1$X2.$prop$getSuborigin={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$at','suborigin']};}};
                            $$inner$RefineTest1$X2.$prop$getSuborigin.get=function(){return suborigin};
                            return $$inner$RefineTest1$X2;
                        }
                        Inner$RefineTest1$X2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:X2.RefineTest1$X2.Inner$RefineTest1$X2,a:{Element:'Element'}},$ps:[{$nm:'d2',$mt:'prm',$t:'Element',$an:function(){return[];}}],$cont:SubRef1$Y2,$tp:{Element:{'satisfies':[{t:$$$cl1.Object}]}},$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Y2','$c','SubRef1','$c','Inner']};};
                        function $init$Inner$RefineTest1$X2(){
                            if (Inner$RefineTest1$X2.$$===undefined){
                                $$$cl1.initTypeProto(Inner$RefineTest1$X2,'nesting::Y2.SubRef1.Inner',$$subRef1$Y2.getT$all()['nesting::X2.RefineTest1'].$$.prototype.Inner$RefineTest1$X2);
                                Y2.SubRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                                (function($$inner$RefineTest1$X2){
                                    
                                    //AttributeDecl suborigin at refinement2.ceylon (40:10-40:51)
                                    $$$cl1.defineAttr($$inner$RefineTest1$X2,'suborigin',function(){return this.suborigin$1070_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$at','suborigin']};});
                                    
                                    //MethodDef x at refinement2.ceylon (41:10-43:10)
                                    $$inner$RefineTest1$X2.x=function x(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl1.String("REFINED ",8).plus($$inner$RefineTest1$X2.getT$all()['nesting::X2.RefineTest1.Inner'].$$.prototype.x.call(this));
                                    };$$inner$RefineTest1$X2.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$m','x']};};
                                    
                                    //MethodDef y at refinement2.ceylon (44:10-46:10)
                                    $$inner$RefineTest1$X2.y=function y(){
                                        var $$inner$RefineTest1$X2=this;
                                        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("y",1),$$inner$RefineTest1$X2.$$outer.$$outer.h$1067.string,$$$cl1.String(",d:",3),$$inner$RefineTest1$X2.$$outer.d$1068.string,$$$cl1.String(",d2:",4),$$inner$RefineTest1$X2.d2$1069.string,$$$cl1.String(".",1)]).string;
                                    };$$inner$RefineTest1$X2.y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest1$X2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','Y2','$c','SubRef1','$c','Inner','$m','y']};};
                                    $$$cl1.defineAttr($$inner$RefineTest1$X2,'d2$1069',function(){return this.d2$1069_;},undefined,function(){return{mod:$$METAMODEL$$,$t:'Element',$cont:Inner$RefineTest1$X2,d:['nesting','Y2','$c','SubRef1','$c','Inner','$at','d2']};});
                                })(Inner$RefineTest1$X2.$$.prototype);
                            }
                            return Inner$RefineTest1$X2;
                        }
                        $$subRef1$Y2.$init$Inner$RefineTest1$X2=$init$Inner$RefineTest1$X2;
                        $init$Inner$RefineTest1$X2();
                        $$subRef1$Y2.Inner$RefineTest1$X2=Inner$RefineTest1$X2;
                        $$$cl1.defineAttr($$subRef1$Y2,'d$1068',function(){return this.d$1068_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:SubRef1$Y2,d:['nesting','Y2','$c','SubRef1','$at','d']};});
                    })(SubRef1$Y2.$$.prototype);
                }
                return SubRef1$Y2;
            }
            $$y2.$init$SubRef1$Y2=$init$SubRef1$Y2;
            $init$SubRef1$Y2();
            $$y2.SubRef1$Y2=SubRef1$Y2;
            $$$cl1.defineAttr($$y2,'h$1067',function(){return this.h$1067_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Y2,d:['nesting','Y2','$at','h']};});
        })(Y2.$$.prototype);
    }
    return Y2;
}
exports.$init$Y2=$init$Y2;
$init$Y2();

//ClassDef SubRef4 at refinement2.ceylon (51:0-55:0)
function SubRef4($$subRef4){
    $init$SubRef4();
    if ($$subRef4===undefined)$$subRef4=new SubRef4.$$;
    RefineTest4($$$cl1.String("t4",2),$$subRef4);
    return $$subRef4;
}
SubRef4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest4},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef4']};};
exports.SubRef4=SubRef4;
function $init$SubRef4(){
    if (SubRef4.$$===undefined){
        $$$cl1.initTypeProto(SubRef4,'nesting::SubRef4',$init$RefineTest4());
        (function($$subRef4){
            
            //MethodDef x at refinement2.ceylon (52:4-54:4)
            $$subRef4.x=function x(){
                var $$subRef4=this;
                return $$subRef4.Inner$RefineTest4((5)).hello();
            };$$subRef4.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:SubRef4,$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef4','$m','x']};};
        })(SubRef4.$$.prototype);
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
    return $$subRef5;
}
SubRef5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest5},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef5']};};
exports.SubRef5=SubRef5;
function $init$SubRef5(){
    if (SubRef5.$$===undefined){
        $$$cl1.initTypeProto(SubRef5,'nesting::SubRef5',$init$RefineTest5());
        (function($$subRef5){
            
            //MethodDef x at refinement2.ceylon (58:4-60:4)
            $$subRef5.x=function x(){
                var $$subRef5=this;
                return $$subRef5.Inner$RefineTest5($$$cl1.String("sr5",3)).x();
            };$$subRef5.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:SubRef5,$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef5','$m','x']};};
        })(SubRef5.$$.prototype);
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
    return $$subRef51;
}
SubRef51.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:SubRef5},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','SubRef51']};};
exports.SubRef51=SubRef51;
function $init$SubRef51(){
    if (SubRef51.$$===undefined){
        $$$cl1.initTypeProto(SubRef51,'nesting::SubRef51',$init$SubRef5());
        (function($$subRef51){
            
            //ClassDef Inner at refinement2.ceylon (63:4-65:4)
            function Inner$RefineTest5(subg55$1071, $$inner$RefineTest5){
                $init$Inner$RefineTest5();
                if ($$inner$RefineTest5===undefined)$$inner$RefineTest5=new this.Inner$RefineTest5.$$;
                $$inner$RefineTest5.$$outer=this;
                $$inner$RefineTest5.subg55$1071_=subg55$1071;
                $$inner$RefineTest5.$$outer.getT$all()['nesting::RefineTest5'].$$.prototype.Inner$RefineTest5.call(this,$$inner$RefineTest5.subg55$1071,$$inner$RefineTest5);
                return $$inner$RefineTest5;
            }
            Inner$RefineTest5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:RefineTest5.Inner$RefineTest5},$ps:[{$nm:'subg55',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],$cont:SubRef51,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','SubRef51','$c','Inner']};};
            function $init$Inner$RefineTest5(){
                if (Inner$RefineTest5.$$===undefined){
                    $$$cl1.initTypeProto(Inner$RefineTest5,'nesting::SubRef51.Inner',$$subRef51.getT$all()['nesting::RefineTest5'].$$.prototype.Inner$RefineTest5);
                    SubRef51.Inner$RefineTest5=Inner$RefineTest5;
                    (function($$inner$RefineTest5){
                        
                        //MethodDef x at refinement2.ceylon (64:8-64:62)
                        $$inner$RefineTest5.x=function x(){
                            var $$inner$RefineTest5=this;
                            return $$$cl1.StringBuilder().appendAll([$$$cl1.String("equis",5),$$inner$RefineTest5.subg55$1071.string,$$$cl1.String(".",1)]).string;
                        };$$inner$RefineTest5.x.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Inner$RefineTest5,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['nesting','SubRef51','$c','Inner','$m','x']};};
                        $$$cl1.defineAttr($$inner$RefineTest5,'subg55$1071',function(){return this.subg55$1071_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Inner$RefineTest5,d:['nesting','SubRef51','$c','Inner','$at','subg55']};});
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

//ClassDef Issue60 at refinement2.ceylon (68:0-70:0)
function Issue60($$issue60){
    $init$Issue60();
    if ($$issue60===undefined)$$issue60=new Issue60.$$;
    Issue60Abs($$issue60);
    return $$issue60;
}
Issue60.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Issue60Abs},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['nesting','Issue60']};};
exports.Issue60=Issue60;
function $init$Issue60(){
    if (Issue60.$$===undefined){
        $$$cl1.initTypeProto(Issue60,'nesting::Issue60',$init$Issue60Abs());
        (function($$issue60){
            
            //ClassDef Inner60 at refinement2.ceylon (69:2-69:46)
            function Inner60$Issue60($$inner60$Issue60){
                $init$Inner60$Issue60();
                if ($$inner60$Issue60===undefined)$$inner60$Issue60=new this.Inner60$Issue60.$$;
                $$inner60$Issue60.$$outer=this;
                $$inner60$Issue60.$$outer.Inner60Abs$Issue60Abs($$inner60$Issue60);
                return $$inner60$Issue60;
            }
            Inner60$Issue60.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Issue60Abs.Inner60Abs$Issue60Abs},$ps:[],$cont:Issue60,$an:function(){return[$$$cl1.shared()];},d:['nesting','Issue60','$c','Inner60']};};
            function $init$Inner60$Issue60(){
                if (Inner60$Issue60.$$===undefined){
                    $$$cl1.initTypeProto(Inner60$Issue60,'nesting::Issue60.Inner60',$$issue60.Inner60Abs$Issue60Abs);
                    Issue60.Inner60$Issue60=Inner60$Issue60;
                }
                return Inner60$Issue60;
            }
            $$issue60.$init$Inner60$Issue60=$init$Inner60$Issue60;
            $init$Inner60$Issue60();
            $$issue60.Inner60$Issue60=Inner60$Issue60;
        })(Issue60.$$.prototype);
    }
    return Issue60;
}
exports.$init$Issue60=$init$Issue60;
$init$Issue60();

//ClassDef Issue60Abs at refinement2.ceylon (71:0-73:0)
function Issue60Abs($$issue60Abs){
    $init$Issue60Abs();
    if ($$issue60Abs===undefined)$$$cl1.throwexc($$$cl1.InvocationException$meta$model($$$cl1.String("Cannot instantiate abstract class")),'?','?')
    return $$issue60Abs;
}
Issue60Abs.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared(),$$$cl1.abstract()];},d:['nesting','Issue60Abs']};};
exports.Issue60Abs=Issue60Abs;
function $init$Issue60Abs(){
    if (Issue60Abs.$$===undefined){
        $$$cl1.initTypeProto(Issue60Abs,'nesting::Issue60Abs',$$$cl1.Basic);
        (function($$issue60Abs){
            
            //ClassDef Inner60Abs at refinement2.ceylon (72:2-72:28)
            function Inner60Abs$Issue60Abs($$inner60Abs$Issue60Abs){
                $init$Inner60Abs$Issue60Abs();
                if ($$inner60Abs$Issue60Abs===undefined)$$inner60Abs$Issue60Abs=new this.Inner60Abs$Issue60Abs.$$;
                $$inner60Abs$Issue60Abs.$$outer=this;
                return $$inner60Abs$Issue60Abs;
            }
            Inner60Abs$Issue60Abs.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:Issue60Abs,$an:function(){return[$$$cl1.shared()];},d:['nesting','Issue60Abs','$c','Inner60Abs']};};
            function $init$Inner60Abs$Issue60Abs(){
                if (Inner60Abs$Issue60Abs.$$===undefined){
                    $$$cl1.initTypeProto(Inner60Abs$Issue60Abs,'nesting::Issue60Abs.Inner60Abs',$$$cl1.Basic);
                    Issue60Abs.Inner60Abs$Issue60Abs=Inner60Abs$Issue60Abs;
                }
                return Inner60Abs$Issue60Abs;
            }
            $$issue60Abs.$init$Inner60Abs$Issue60Abs=$init$Inner60Abs$Issue60Abs;
            $init$Inner60Abs$Issue60Abs();
            $$issue60Abs.Inner60Abs$Issue60Abs=Inner60Abs$Issue60Abs;
        })(Issue60Abs.$$.prototype);
    }
    return Issue60Abs;
}
exports.$init$Issue60Abs=$init$Issue60Abs;
$init$Issue60Abs();

//MethodDef testRefinement2 at refinement2.ceylon (75:0-85:0)
function testRefinement2(){
    
    //AttributeDecl c1 at refinement2.ceylon (76:4-76:54)
    var c1$1072=Y2($$$cl1.String("y2",2)).SubRef1$Y2((99)).Inner$RefineTest1$X2($$$cl1.String("with parm",9),{Element:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.className(c1$1072).equals($$$cl1.String("nesting::Y2.SubRef1.Inner",25)),$$$cl1.String("classname is ",13).plus($$$cl1.className(c1$1072)));
    $$$c2.check(c1$1072.origin.equals($$$cl1.String("RefineTest1.Inner (with parm)",29)),$$$cl1.String("refinement [1] ",15).plus(c1$1072.origin));
    $$$c2.check(c1$1072.suborigin.equals($$$cl1.String("SubRef1.Inner",13)),$$$cl1.String("refinement [2] ",15).plus(c1$1072.suborigin));
    $$$c2.check(c1$1072.x().equals($$$cl1.String("REFINED x and yy2,d:99,d2:with parm. and a:y2, b:1, c:with parm.",64)),$$$cl1.String("refinement [3] ",15).plus(c1$1072.x()));
    $$$c2.check(Y2($$$cl1.String("y3",2)).SubRef1$Y2((10)).outerx().equals($$$cl1.String("REFINED x and yy3,d:10,d2:Y3. and a:y3, b:1, c:Y3.",50)),$$$cl1.String("refinement [4] ",15).plus(Y2($$$cl1.String("y3",2)).SubRef1$Y2((10)).outerx()));
    $$$c2.check(SubRef4().x().equals($$$cl1.String("hello from RefineTest2.Inner with 5.",36)),$$$cl1.String("refinement [5] ",15).plus(SubRef4().x()));
    $$$c2.check(SubRef5().x().equals($$$cl1.String("sr5sr5sr5sr5sr5sr5",18)),$$$cl1.String("refinement [6] ",15).plus(SubRef5().x()));
    $$$c2.check(SubRef51().x().equals($$$cl1.String("equissr5.",9)),$$$cl1.String("refinement [7] ",15).plus(SubRef51().x()));
};testRefinement2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['nesting','testRefinement2']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
