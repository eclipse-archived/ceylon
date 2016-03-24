(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","$mod-bin":"6.0","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f"}},"$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test","$o":{"x":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"y"}},"$nm":"x"}}},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"Test2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T1"}],"$pk":"misc","$nm":"TestInterface1"}],"$mt":"cls","$tp":[{"$nm":"T1"}],"$nm":"Test2"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"gridSize":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"gridSize"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$an":{"shared":[]},"$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator","$o":{"iter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"}],"$mt":"obj","$m":{"next":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Finished"}]},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"next"}},"$at":{"index":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"index"}},"$nm":"iter"}}}},"$at":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"b"},"c":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"c"},"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"a"}},"$nm":"TestObjects"},"Issue225Alias":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$nm":"Issue225Alias"},"Bivariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"In"},{"variance":"out","$nm":"Out"}],"$an":{"shared":[]},"$nm":"Bivariant"},"testAliasing":{"$i":{"AliasedIface2":{"$mt":"ifc","$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface2"}},"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"use":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$pk":"misc","$nm":"AliasedIface2"},"$mt":"prm","$nm":"aif"}]],"$mt":"mthd","$nm":"use"},"cualquiera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Boolean"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"bits"}]],"$mt":"mthd","$nm":"cualquiera"}},"$c":{"InnerSubalias":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$alias":"1","$nm":"InnerSubalias"}},"$nm":"testAliasing"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface","$o":{"aliased":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"misc","$nm":"AliasedIface"}],"$mt":"obj","$nm":"aliased"}}}},"$nm":"AliasingSub2"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$an":{"shared":[]},"$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"},"Container":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Outer"}],"$an":{"shared":[]},"$c":{"Member":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Inner"}],"$an":{"shared":[]},"$c":{"Child":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"InnerMost"}],"$an":{"shared":[]},"$nm":"Child"}},"$nm":"Member"}},"$nm":"Container"},"Covariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Covariant"},"runtimeMethod":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"param"}]],"$mt":"mthd","$nm":"runtimeMethod"},"Top1":{"$mt":"ifc","$an":{"shared":[]},"$nm":"Top1"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"Middle1":{"satisfies":[{"$pk":"misc","$nm":"Top1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Middle1"},"m1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"m1"},"m2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"m2"},"m3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"m3"},"container249":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"attr","$nm":"container249"},"Test1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"misc","$nm":"TestInterface1"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$nm":"Test1"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"Invariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Element"}],"$an":{"shared":[]},"$nm":"Invariant"},"issue225_1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$mt":"prm","$nm":"content"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"issue225_1"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"issue225_2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$pk":"misc","$nm":"Issue225Alias"},"$mt":"prm","$nm":"content"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"issue225_2"},"testStackTrace":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testStackTrace"},"TestInterface1":{"$mt":"ifc","$tp":[{"$nm":"T"}],"$nm":"TestInterface1"},"Contravariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Contravariant"},"object249":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"int"}},"$nm":"object249"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"testReifiedRuntime":{"$i":{"TestInterface2":{"$mt":"ifc","$tp":[{"variance":"in","$nm":"T"}],"$nm":"TestInterface2"}},"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$c":{"Local":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"T"}],"$nm":"Local"},"Test3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"misc","$nm":"TestInterface2"}],"$mt":"cls","$tp":[{"variance":"in","$nm":"T"}],"$nm":"Test3"},"Test4":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T1"}],"$pk":"misc","$nm":"TestInterface2"}],"$mt":"cls","$tp":[{"variance":"in","$nm":"T1"}],"$nm":"Test4"}},"$nm":"testReifiedRuntime"},"Bottom1":{"satisfies":[{"$pk":"misc","$nm":"Middle1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Bottom1"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"Test284":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$pk":"misc","$nm":"Strinteger"},"$mt":"prm","$nm":"x"}],"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$pk":"misc","$nm":"Strinteger"},"$mt":"attr","$nm":"x"}},"$nm":"Test284"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//TypeAliasDecl Strinteger at aliases.ceylon (3:0-3:41)
function Strinteger(){var tmpvar$4964={t:'u', l:[{t:$$$cl4138.String},{t:$$$cl4138.Integer}]};tmpvar$4964.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl4138.shared()];},d:['misc','Strinteger']};};return tmpvar$4964;}
exports.Strinteger=Strinteger;

//ClassDef Test284 at aliases.ceylon (5:0-7:0)
function Test284(x$4965, $$test284){
    $init$Test284();
    if ($$test284===undefined)$$test284=new Test284.$$;
    $$test284.x$4965_=x$4965;
    $$$cl4138.defineAttr($$test284,'x$4965',function(){return this.x$4965_;},undefined,function(){return{mod:$$METAMODEL$$,$t:Strinteger(),$cont:Test284,d:['misc','Test284','$at','x']};});
    $$$cl4138.print(x$4965);
    return $$test284;
}
Test284.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'x',$mt:'prm',$t:Strinteger(),$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['misc','Test284']};};
exports.Test284=Test284;
function $init$Test284(){
    if (Test284.$$===undefined){
        $$$cl4138.initTypeProto(Test284,'misc::Test284',$$$cl4138.Basic);
    }
    return Test284;
}
exports.$init$Test284=$init$Test284;
$init$Test284();

//ClassDef AliasingClass at aliases.ceylon (9:0-16:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    
    //InterfaceDef AliasingIface at aliases.ceylon (10:4-12:4)
    function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
        $$aliasingIface$AliasingClass.$$outer=this;
        
        //MethodDef aliasingIface at aliases.ceylon (11:8-11:54)
        function aliasingIface(){
            return true;
        }
        $$aliasingIface$AliasingClass.aliasingIface=aliasingIface;
        aliasingIface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[],$cont:AliasingIface$AliasingClass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingClass','$i','AliasingIface','$m','aliasingIface']};};
    }
    AliasingIface$AliasingClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasingClass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingClass','$i','AliasingIface']};};
    $$aliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
    function $init$AliasingIface$AliasingClass(){
        if (AliasingIface$AliasingClass.$$===undefined){
            $$$cl4138.initTypeProtoI(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
            AliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
        }
        return AliasingIface$AliasingClass;
    }
    $$aliasingClass.$init$AliasingIface$AliasingClass=$init$AliasingIface$AliasingClass;
    $init$AliasingIface$AliasingClass();
    
    //ClassDef AliasingInner at aliases.ceylon (13:4-15:4)
    function AliasingInner$AliasingClass($$aliasingInner$AliasingClass){
        $init$AliasingInner$AliasingClass();
        if ($$aliasingInner$AliasingClass===undefined)$$aliasingInner$AliasingClass=new AliasingInner$AliasingClass.$$;
        $$aliasingInner$AliasingClass.$$outer=this;
        
        //MethodDef aliasingInner at aliases.ceylon (14:8-14:54)
        function aliasingInner(){
            return true;
        }
        $$aliasingInner$AliasingClass.aliasingInner=aliasingInner;
        aliasingInner.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[],$cont:AliasingInner$AliasingClass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingClass','$c','AliasingInner','$m','aliasingInner']};};
        return $$aliasingInner$AliasingClass;
    }
    AliasingInner$AliasingClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:AliasingClass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingClass','$c','AliasingInner']};};
    $$aliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
    function $init$AliasingInner$AliasingClass(){
        if (AliasingInner$AliasingClass.$$===undefined){
            $$$cl4138.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl4138.Basic);
            AliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
        }
        return AliasingInner$AliasingClass;
    }
    $$aliasingClass.$init$AliasingInner$AliasingClass=$init$AliasingInner$AliasingClass;
    $init$AliasingInner$AliasingClass();
    return $$aliasingClass;
}
AliasingClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingClass']};};
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl4138.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl4138.Basic);
    }
    return AliasingClass;
}
exports.$init$AliasingClass=$init$AliasingClass;
$init$AliasingClass();

//ClassDef AliasingSubclass at aliases.ceylon (18:0-26:0)
function AliasingSubclass($$aliasingSubclass){
    $init$AliasingSubclass();
    if ($$aliasingSubclass===undefined)$$aliasingSubclass=new AliasingSubclass.$$;
    AliasingClass($$aliasingSubclass);
    
    //ClassDecl InnerAlias at aliases.ceylon (19:4-19:48)
    function InnerAlias$AliasingSubclass($$innerAlias$AliasingSubclass){return $$aliasingSubclass.AliasingInner$AliasingClass($$innerAlias$AliasingSubclass);}
    InnerAlias$AliasingSubclass.$$=$$aliasingSubclass.AliasingInner$AliasingClass.$$;
    InnerAlias$AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingClass.AliasingInner$AliasingClass},$ps:[],$cont:AliasingSubclass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingSubclass','$c','InnerAlias']};};
    $$aliasingSubclass.InnerAlias$AliasingSubclass=InnerAlias$AliasingSubclass;
    
    //ClassDef SubAlias at aliases.ceylon (20:4-20:50)
    function SubAlias$AliasingSubclass($$subAlias$AliasingSubclass){
        $init$SubAlias$AliasingSubclass();
        if ($$subAlias$AliasingSubclass===undefined)$$subAlias$AliasingSubclass=new SubAlias$AliasingSubclass.$$;
        $$subAlias$AliasingSubclass.$$outer=this;
        $$aliasingSubclass.InnerAlias$AliasingSubclass($$subAlias$AliasingSubclass);
        return $$subAlias$AliasingSubclass;
    }
    SubAlias$AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingSubclass.InnerAlias$AliasingSubclass},$ps:[],$cont:AliasingSubclass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingSubclass','$c','SubAlias']};};
    $$aliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
    function $init$SubAlias$AliasingSubclass(){
        if (SubAlias$AliasingSubclass.$$===undefined){
            $$$cl4138.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
            AliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
        }
        return SubAlias$AliasingSubclass;
    }
    $$aliasingSubclass.$init$SubAlias$AliasingSubclass=$init$SubAlias$AliasingSubclass;
    $init$SubAlias$AliasingSubclass();
    
    //MethodDef aliasingSubclass at aliases.ceylon (22:4-24:4)
    function aliasingSubclass(){
        return $$aliasingSubclass.SubAlias$AliasingSubclass().aliasingInner();
    }
    $$aliasingSubclass.aliasingSubclass=aliasingSubclass;
    aliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[],$cont:AliasingSubclass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingSubclass','$m','aliasingSubclass']};};
    
    //InterfaceDecl AliasedIface at aliases.ceylon (25:4-25:50)
    function AliasedIface$AliasingSubclass($$aliasedIface$AliasingSubclass){$$aliasingSubclass.AliasingIface$AliasingClass($$aliasedIface$AliasingSubclass);}
    AliasedIface$AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasingSubclass,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingSubclass','$i','AliasedIface']};};
    $$aliasingSubclass.AliasedIface$AliasingSubclass=AliasedIface$AliasingSubclass;
    return $$aliasingSubclass;
}
AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingClass},$ps:[],d:['misc','AliasingSubclass']};};
function $init$AliasingSubclass(){
    if (AliasingSubclass.$$===undefined){
        $$$cl4138.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',$init$AliasingClass());
    }
    return AliasingSubclass;
}
exports.$init$AliasingSubclass=$init$AliasingSubclass;
$init$AliasingSubclass();

//ClassDef AliasingSub2 at aliases.ceylon (28:0-34:0)
function AliasingSub2($$aliasingSub2){
    $init$AliasingSub2();
    if ($$aliasingSub2===undefined)$$aliasingSub2=new AliasingSub2.$$;
    AliasingSubclass($$aliasingSub2);
    
    //AttributeGetterDef iface at aliases.ceylon (29:4-33:4)
    $$$cl4138.defineAttr($$aliasingSub2,'iface',function(){
        
        //ObjectDef aliased at aliases.ceylon (30:8-31:8)
        function aliased$4966(){
            var $$aliased$4966=new aliased$4966.$$;
            $$aliasingSub2.AliasedIface$AliasingSubclass($$aliased$4966);
            return $$aliased$4966;
        };aliased$4966.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},satisfies:[{t:AliasingSubclass.AliasedIface$AliasingSubclass}],d:['misc','AliasingSub2','$at','iface','$o','aliased']};};
        function $init$aliased$4966(){
            if (aliased$4966.$$===undefined){
                $$$cl4138.initTypeProto(aliased$4966,'misc::AliasingSub2.iface.aliased',$$$cl4138.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
            }
            return aliased$4966;
        }
        $init$aliased$4966();
        var aliased$4967;
        function getAliased$4967(){
            if (aliased$4967===undefined){aliased$4967=$init$aliased$4966()();aliased$4967.$$metamodel$$=getAliased$4967.$$metamodel$$;}
            return aliased$4967;
        }
        getAliased$4967.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:aliased$4966},d:['misc','AliasingSub2','$at','iface','$o','aliased']};};
        $prop$getAliased$4967={get:getAliased$4967,$$metamodel$$:getAliased$4967.$$metamodel$$};
        return getAliased$4967();
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:AliasingSubclass.AliasedIface$AliasingSubclass},$cont:AliasingSub2,$an:function(){return[$$$cl4138.shared()];},d:['misc','AliasingSub2','$at','iface']};});
    return $$aliasingSub2;
}
AliasingSub2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingSubclass},$ps:[],d:['misc','AliasingSub2']};};
function $init$AliasingSub2(){
    if (AliasingSub2.$$===undefined){
        $$$cl4138.initTypeProto(AliasingSub2,'misc::AliasingSub2',$init$AliasingSubclass());
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//InterfaceDecl Matrix at aliases.ceylon (36:0-36:43)
function Matrix($$targs$$,$$matrix){$$$cl4138.Sequence({Element:{t:$$$cl4138.Sequence,a:{Element:$$targs$$.Cell}}},$$matrix);}
Matrix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$tp:{Cell:{}},d:['misc','Matrix']};};

//ClassDecl Listleton at aliases.ceylon (37:0-37:54)
function Listleton(l$4968, $$targs$$,$$listleton){return $$$cl4138.Singleton(l$4968,{Element:{t:$$$cl4138.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl4138.Singleton.$$;
Listleton.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.List,a:{Element:'T'}}}},$ps:[{$nm:'l',$mt:'prm',$t:{t:$$$cl4138.List,a:{Element:'T'}},$an:function(){return[];}}],$tp:{T:{}},d:['misc','Listleton']};};

//ClassDef MiMatrix at aliases.ceylon (39:0-70:0)
function MiMatrix(gridSize$4969, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl4138.Integer}};
    $$miMatrix.gridSize$4969_=gridSize$4969;
    Matrix({Cell:{t:$$$cl4138.Integer}},$$miMatrix);
    $$$cl4138.add_type_arg($$miMatrix,'Cell',{t:$$$cl4138.Integer});
    $$$cl4138.defineAttr($$miMatrix,'gridSize$4969',function(){return this.gridSize$4969_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,d:['misc','MiMatrix','$at','gridSize']};});
    
    //AttributeDecl sb at aliases.ceylon (40:4-40:44)
    var sb$4970=$$$cl4138.SequenceBuilder({Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}});
    $$miMatrix.$prop$getSb$4970={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.SequenceBuilder,a:{Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}}},$cont:MiMatrix,d:['misc','MiMatrix','$at','sb']};}};
    $$miMatrix.$prop$getSb$4970.get=function(){return sb$4970};
    //'for' statement at aliases.ceylon (41:4-43:4)
    var it$4971 = $$$cl4138.Range((1),gridSize$4969,{Element:{t:$$$cl4138.Integer}}).iterator();
    var i$4972;while ((i$4972=it$4971.next())!==$$$cl4138.getFinished()){
        sb$4970.append($$$cl4138.Comprehension(function(){
            //Comprehension at aliases.ceylon (42:20-42:43)
            var it$4973=$$$cl4138.Range((1),gridSize$4969,{Element:{t:$$$cl4138.Integer}}).iterator();
            var j$4974=$$$cl4138.getFinished();
            var next$j$4974=function(){return j$4974=it$4973.next();}
            next$j$4974();
            return function(){
                if(j$4974!==$$$cl4138.getFinished()){
                    var j$4974$4975=j$4974;
                    var tmpvar$4976=j$4974$4975;
                    next$j$4974();
                    return tmpvar$4976;
                }
                return $$$cl4138.getFinished();
            }
        },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}).sequence);
    }
    
    //AttributeDecl grid at aliases.ceylon (44:4-44:24)
    $$miMatrix.$prop$getGrid$4977={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl4138.Integer}}},$cont:MiMatrix,d:['misc','MiMatrix','$at','grid']};}};
    var g$4978;
    if($$$cl4138.nonempty((g$4978=sb$4970.sequence))){
        var grid$4977=g$4978;
        $$$cl4138.defineAttr($$miMatrix,'grid$4977',function(){return grid$4977;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','grid']};});
    }else {
        var grid$4977=$$$cl4138.Tuple($$$cl4138.Tuple((1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}});
        $$$cl4138.defineAttr($$miMatrix,'grid$4977',function(){return grid$4977;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','grid']};});
    }
    
    //MethodDef iterator at aliases.ceylon (50:4-50:76)
    function iterator(){
        return grid$4977.iterator();
    }
    $$miMatrix.iterator=iterator;
    iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterator,a:{Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}}},$ps:[],$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$m','iterator']};};
    
    //AttributeDecl string at aliases.ceylon (51:4-51:45)
    var string=grid$4977.string;
    $$$cl4138.defineAttr($$miMatrix,'string',function(){return string;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','string']};});
    $$miMatrix.$prop$getString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','string']};}};
    $$miMatrix.$prop$getString.get=function(){return string};
    
    //AttributeDecl hash at aliases.ceylon (52:4-52:42)
    var hash=grid$4977.hash;
    $$$cl4138.defineAttr($$miMatrix,'hash',function(){return hash;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','hash']};});
    $$miMatrix.$prop$getHash={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','hash']};}};
    $$miMatrix.$prop$getHash.get=function(){return hash};
    
    //MethodDecl equals at aliases.ceylon (53:4-53:68)
    var equals=function (other$4979){
        return grid$4977.equals(other$4979);
    };
    equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$m','equals']};};
    $$miMatrix.equals=equals;
    $$miMatrix.span=function span(from$4980,to$4981){return (opt$4982=grid$4977,$$$cl4138.JsCallable(opt$4982,opt$4982!==null?opt$4982.span:null))(from$4980,to$4981);};
    var opt$4982;
    $$miMatrix.segment=function segment(from$4983,length$4984){return (opt$4985=grid$4977,$$$cl4138.JsCallable(opt$4985,opt$4985!==null?opt$4985.segment:null))(from$4983,length$4984);};
    var opt$4985;
    
    //AttributeDecl reversed at aliases.ceylon (56:4-56:58)
    var reversed=grid$4977.reversed;
    $$$cl4138.defineAttr($$miMatrix,'reversed',function(){return reversed;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','reversed']};});
    $$miMatrix.$prop$getReversed={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','reversed']};}};
    $$miMatrix.$prop$getReversed.get=function(){return reversed};
    
    //AttributeDecl lastIndex at aliases.ceylon (57:4-57:52)
    var lastIndex=grid$4977.lastIndex;
    $$$cl4138.defineAttr($$miMatrix,'lastIndex',function(){return lastIndex;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','lastIndex']};});
    $$miMatrix.$prop$getLastIndex={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','lastIndex']};}};
    $$miMatrix.$prop$getLastIndex.get=function(){return lastIndex};
    
    //MethodDecl get at aliases.ceylon (58:4-58:55)
    var $get=function (i$4986){
        return grid$4977.$get(i$4986);
    };
    $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}]},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$m','get']};};
    $$miMatrix.$get=$get;
    
    //AttributeDecl rest at aliases.ceylon (59:4-59:47)
    var rest=grid$4977.rest;
    $$$cl4138.defineAttr($$miMatrix,'rest',function(){return rest;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','rest']};});
    $$miMatrix.$prop$getRest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','rest']};}};
    $$miMatrix.$prop$getRest.get=function(){return rest};
    
    //AttributeDecl first at aliases.ceylon (60:4-60:47)
    var first=grid$4977.first;
    $$$cl4138.defineAttr($$miMatrix,'first',function(){return first;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','first']};});
    $$miMatrix.$prop$getFirst={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','first']};}};
    $$miMatrix.$prop$getFirst.get=function(){return first};
    
    //AttributeDecl clone at aliases.ceylon (61:4-61:40)
    $$$cl4138.defineAttr($$miMatrix,'clone',function(){return $$miMatrix;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:MiMatrix},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','clone']};});
    $$miMatrix.$prop$getClone={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:MiMatrix},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','clone']};}};
    $$miMatrix.$prop$getClone.get=function(){return clone};
    
    //AttributeDecl size at aliases.ceylon (62:4-62:43)
    $$$cl4138.defineAttr($$miMatrix,'size',function(){return grid$4977.size;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','size']};});
    $$miMatrix.$prop$getSize={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','size']};}};
    $$miMatrix.$prop$getSize.get=function(){return size};
    
    //MethodDecl contains at aliases.ceylon (63:4-63:72)
    var contains=function (other$4987){
        return grid$4977.contains(other$4987);
    };
    contains.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$m','contains']};};
    $$miMatrix.contains=contains;
    $$$cl4138.defineAttr($$miMatrix,'last',function(){
        return grid$4977.last;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$at','last']};});
    
    //MethodDecl spanTo at aliases.ceylon (65:4-66:41)
    var spanTo=function (to$4988){
        return (opt$4989=(to$4988.compare((0)).equals($$$cl4138.getSmaller())?$$$cl4138.getEmpty():null),opt$4989!==null?opt$4989:$$miMatrix.span((0),to$4988));
    };
    spanTo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$m','spanTo']};};
    $$miMatrix.spanTo=spanTo;
    var opt$4989;
    
    //MethodDecl spanFrom at aliases.ceylon (68:4-69:28)
    var spanFrom=function (from$4990){
        return $$miMatrix.span(from$4990,$$miMatrix.size);
    };
    spanFrom.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Sequence,a:{Element:{t:$$$cl4138.Integer}}}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','MiMatrix','$m','spanFrom']};};
    $$miMatrix.spanFrom=spanFrom;
    return $$miMatrix;
}
MiMatrix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'gridSize',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],satisfies:[{t:Matrix,a:{Cell:{t:$$$cl4138.Integer}}}],d:['misc','MiMatrix']};};
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl4138.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl4138.Basic,$$$cl4138.Sequence);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();

//MethodDef issue225_1 at aliases.ceylon (72:0-72:47)
function issue225_1(content$4991){
}
exports.issue225_1=issue225_1;
issue225_1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'content',$mt:'prm',$t:{t:'u', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.String}]},$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['misc','issue225_1']};};

//TypeAliasDecl Issue225Alias at aliases.ceylon (73:0-73:44)
function Issue225Alias(){var tmpvar$4992={t:'u', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.String}]};tmpvar$4992.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl4138.shared()];},d:['misc','Issue225Alias']};};return tmpvar$4992;}
exports.Issue225Alias=Issue225Alias;

//MethodDef issue225_2 at aliases.ceylon (74:0-74:46)
function issue225_2(content$4993){
}
exports.issue225_2=issue225_2;
issue225_2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'content',$mt:'prm',$t:Issue225Alias(),$an:function(){return[];}}],$an:function(){return[$$$cl4138.shared()];},d:['misc','issue225_2']};};

//MethodDef testAliasing at aliases.ceylon (76:0-94:0)
function testAliasing(){
    $$$cl4138.print($$$cl4138.String("testing type aliases",20));
    $$$c4139.check(AliasingSubclass().aliasingSubclass(),$$$cl4138.String("Aliased member class",20));
    
    //ClassDecl InnerSubalias at aliases.ceylon (79:4-79:47)
    function InnerSubalias$4994($$innerSubalias$4994){return AliasingSubclass($$innerSubalias$4994);}
    InnerSubalias$4994.$$=AliasingSubclass.$$;
    InnerSubalias$4994.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingSubclass},$ps:[],d:['misc','testAliasing','$c','InnerSubalias']};};
    $$$c4139.check(InnerSubalias$4994().aliasingSubclass(),$$$cl4138.String("Aliased top-level class",23));
    
    //InterfaceDecl AliasedIface2 at aliases.ceylon (81:4-81:58)
    function AliasedIface2$4995($$aliasedIface2$4995){$$aliasingClass.AliasingIface$AliasingClass($$aliasedIface2$4995);}
    AliasedIface2$4995.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['misc','testAliasing','$i','AliasedIface2']};};
    
    //MethodDef use at aliases.ceylon (82:4-82:65)
    function use$4996(aif$4997){
        return aif$4997.aliasingIface();
    };use$4996.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'aif',$mt:'prm',$t:{t:AliasedIface2$4995},$an:function(){return[];}}],d:['misc','testAliasing','$m','use']};};
    $$$c4139.check(use$4996(AliasingSub2().iface),$$$cl4138.String("Aliased member interface",24));
    
    //AttributeDecl xxxxx at aliases.ceylon (84:4-84:24)
    var xxxxx$4998=(5);
    $$$c4139.check($$$cl4138.isOfType(xxxxx$4998,{t:$$$cl4138.Integer}),$$$cl4138.String("Type alias",10));
    $$$c4139.check(Listleton($$$cl4138.Tuple($$$cl4138.Tuple((1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.Tuple($$$cl4138.Tuple((2),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.Tuple($$$cl4138.Tuple((3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl4138.Integer}]}]},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl4138.Integer}]},{t:'T', l:[{t:$$$cl4138.Integer}]}]},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}}).first,{T:{t:$$$cl4138.Integer}}).string.equals($$$cl4138.String("[[1]]",5)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("class alias ",12),Listleton($$$cl4138.Tuple($$$cl4138.Tuple((1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.Tuple($$$cl4138.Tuple((2),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.Tuple($$$cl4138.Tuple((3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl4138.Integer}]}]},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl4138.Integer}]},{t:'T', l:[{t:$$$cl4138.Integer}]}]},First:{t:'T', l:[{t:$$$cl4138.Integer}]},Element:{t:'T', l:[{t:$$$cl4138.Integer}]}}).first,{T:{t:$$$cl4138.Integer}}).string,$$$cl4138.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c4139.check(MiMatrix((2)).string.equals($$$cl4138.String("[[1, 2], [1, 2]]",16)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("interface alias ",16),MiMatrix((2)).string,$$$cl4138.String(" instead of [[1, 2], [1, 2]]",28)]).string);
    
    //AttributeDecl xxxxx1 at aliases.ceylon (88:4-88:21)
    var xxxxx1$4999=(6);
    
    //AttributeDecl xxxxx2 at aliases.ceylon (89:4-89:26)
    var xxxxx2$5000=$$$cl4138.String("XXXX",4);
    $$$c4139.check($$$cl4138.isOfType(xxxxx1$4999,{t:'u', l:[{t:$$$cl4138.String},{t:$$$cl4138.Integer}]}),$$$cl4138.String("is String|Integer",17));
    $$$c4139.check($$$cl4138.isOfType(xxxxx2$5000,{t:'i', l:[{t:$$$cl4138.String},{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.Anything}}}]}),$$$cl4138.String("is String&List",14));
    
    //MethodDecl cualquiera at aliases.ceylon (92:4-92:51)
    var cualquiera$5001=function (bits$5002){
        if(bits$5002===undefined){bits$5002=$$$cl4138.getEmpty();}
        return $$$cl4138.any(bits$5002);
    };
    cualquiera$5001.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'bits',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Boolean}}},$an:function(){return[];}}],d:['misc','testAliasing','$m','cualquiera']};};
    $$$c4139.check(cualquiera$5001([true,true,true].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.true$5003}})),$$$cl4138.String("seq arg method alias",20));
};testAliasing.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','testAliasing']};};

//MethodDef m1 at exceptions.ceylon (1:0-3:0)
function m1(){
    throw $$$cl4138.wrapexc($$$cl4138.Exception($$$cl4138.String("Catch me!",9)),'2:2-2:30','misc/exceptions.ceylon');
};m1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','m1']};};

//MethodDef m2 at exceptions.ceylon (5:0-7:0)
function m2(){
    m1();
};m2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','m2']};};

//MethodDef m3 at exceptions.ceylon (9:0-11:0)
function m3(){
    m2();
};m3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','m3']};};

//MethodDef testStackTrace at exceptions.ceylon (13:0-21:0)
function testStackTrace(){
    try{
        $$$cl4138.print($$$cl4138.String("Coming up, a strack trace...",28));
        m3();
    }catch(ex$5004){
        if (ex$5004.getT$name === undefined) ex$5004=$$$cl4138.NativeException(ex$5004);
        if($$$cl4138.isOfType(ex$5004,{t:$$$cl4138.Exception})){
            ex$5004.printStackTrace();
        }
        else{throw ex$5004}
    }
    $$$cl4138.print($$$cl4138.String("You should have seen a stack trace",34));
};testStackTrace.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','testStackTrace']};};

//ClassDef LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDecl parent at late_support.ceylon (4:4-4:37)
    var parent;
    $$$cl4138.defineAttr($$lateTestChild,'parent',function(){if (parent===undefined)throw $$$cl4138.InitializationException($$$cl4138.String('Attempt to read unitialized attribute «parent»'));return parent;},function(parent$5005){if(parent!==undefined)throw $$$cl4138.InitializationException($$$cl4138.String('Attempt to reassign immutable attribute «parent»'));return parent=parent$5005;},function(){return{mod:$$METAMODEL$$,$t:{t:LateTestParent},$cont:LateTestChild,$an:function(){return[$$$cl4138.shared(),$$$cl4138.late()];},d:['misc','LateTestChild','$at','parent']};});
    $$lateTestChild.$prop$getParent={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:LateTestParent},$cont:LateTestChild,$an:function(){return[$$$cl4138.shared(),$$$cl4138.late()];},d:['misc','LateTestChild','$at','parent']};}};
    $$lateTestChild.$prop$getParent.get=function(){return parent};
    return $$lateTestChild;
}
LateTestChild.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['misc','LateTestChild']};};
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl4138.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl4138.Basic);
    }
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDef LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children===undefined){children=$$$cl4138.getEmpty();}
    
    //AttributeDecl children at late_support.ceylon (8:4-8:34)
    var children=children;
    $$$cl4138.defineAttr($$lateTestParent,'children',function(){return children;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:LateTestChild}}},$cont:LateTestParent,$an:function(){return[$$$cl4138.shared()];},d:['misc','LateTestParent','$at','children']};});
    $$lateTestParent.$prop$getChildren={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Sequential,a:{Element:{t:LateTestChild}}},$cont:LateTestParent,$an:function(){return[$$$cl4138.shared()];},d:['misc','LateTestParent','$at','children']};}};
    $$lateTestParent.$prop$getChildren.get=function(){return children};
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$5006 = $$lateTestParent.children.iterator();
    var child$5007;while ((child$5007=it$5006.next())!==$$$cl4138.getFinished()){
        (child$5007.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
LateTestParent.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'children',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:LateTestChild}}},$an:function(){return[$$$cl4138.shared()];}}],d:['misc','LateTestParent']};};
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl4138.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl4138.Basic);
    }
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDef testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDecl kids at late_support.ceylon (15:4-15:51)
    var kids$5008=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$5008);
    try{
        LateTestParent(kids$5008);
        $$$c4139.fail($$$cl4138.String("reassigning to late attribute should fail",41));
    }catch(ex$5009){
        if (ex$5009.getT$name === undefined) ex$5009=$$$cl4138.NativeException(ex$5009);
        if($$$cl4138.isOfType(ex$5009,{t:$$$cl4138.InitializationException})){
            $$$c4139.check(true);
        }
        else if($$$cl4138.isOfType(ex$5009,{t:$$$cl4138.Exception})){
            $$$c4139.fail($$$cl4138.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$5009}
    }
    try{
        $$$cl4138.print(LateTestChild().parent);
        $$$c4139.fail($$$cl4138.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$5010){
        if (ex$5010.getT$name === undefined) ex$5010=$$$cl4138.NativeException(ex$5010);
        if($$$cl4138.isOfType(ex$5010,{t:$$$cl4138.InitializationException})){
            $$$c4139.check(true);
        }
        else if($$$cl4138.isOfType(ex$5010,{t:$$$cl4138.Exception})){
            $$$c4139.fail($$$cl4138.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$5010}
    }
};testLate.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','testLate']};};

//InterfaceDef X at misc.ceylon (1:0-5:0)
function X($$x){
    
    //MethodDef helloWorld at misc.ceylon (2:4-4:4)
    function helloWorld(){
        $$$cl4138.print($$$cl4138.String("hello world",11));
    }
    $$x.helloWorld=helloWorld;
    helloWorld.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:X,$an:function(){return[$$$cl4138.shared()];},d:['misc','X','$m','helloWorld']};};
}
X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl4138.shared()];},d:['misc','X']};};
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl4138.initTypeProtoI(X,'misc::X');
    }
    return X;
}
exports.$init$X=$init$X;
$init$X();

//ClassDef Foo at misc.ceylon (7:0-17:0)
function Foo(name, $$foo){
    $init$Foo();
    if ($$foo===undefined)$$foo=new Foo.$$;
    
    //AttributeDecl name at misc.ceylon (8:4-8:22)
    var name=name;
    $$$cl4138.defineAttr($$foo,'name',function(){return name;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Foo,$an:function(){return[$$$cl4138.shared()];},d:['misc','Foo','$at','name']};});
    $$foo.$prop$getName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Foo,$an:function(){return[$$$cl4138.shared()];},d:['misc','Foo','$at','name']};}};
    $$foo.$prop$getName.get=function(){return name};
    
    //AttributeDecl counter at misc.ceylon (9:4-9:28)
    var counter$5011=(0);
    $$$cl4138.defineAttr($$foo,'counter$5011',function(){return counter$5011;},function(counter$5012){return counter$5011=counter$5012;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Foo,$an:function(){return[$$$cl4138.variable()];},d:['misc','Foo','$at','counter']};});
    $$foo.$prop$getCounter$5011={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Foo,$an:function(){return[$$$cl4138.variable()];},d:['misc','Foo','$at','counter']};}};
    $$foo.$prop$getCounter$5011.get=function(){return counter$5011};
    
    //AttributeGetterDef count at misc.ceylon (10:4-10:43)
    $$$cl4138.defineAttr($$foo,'count',function(){
        return counter$5011;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Foo,$an:function(){return[$$$cl4138.shared()];},d:['misc','Foo','$at','count']};});
    
    //MethodDef inc at misc.ceylon (11:4-11:43)
    function inc(){
        counter$5011=counter$5011.plus((1));
    }
    $$foo.inc=inc;
    inc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Foo,$an:function(){return[$$$cl4138.shared()];},d:['misc','Foo','$m','inc']};};
    
    //MethodDef printName at misc.ceylon (12:4-14:4)
    function printName(){
        $$$cl4138.print($$$cl4138.String("foo name = ",11).plus($$foo.name));
    }
    $$foo.printName=printName;
    printName.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Foo,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['misc','Foo','$m','printName']};};
    
    //AttributeDecl string at misc.ceylon (15:4-15:57)
    var string=$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("Foo(",4),$$foo.name.string,$$$cl4138.String(")",1)]).string;
    $$$cl4138.defineAttr($$foo,'string',function(){return string;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Foo,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default(),$$$cl4138.actual()];},d:['misc','Foo','$at','string']};});
    $$foo.$prop$getString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:Foo,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default(),$$$cl4138.actual()];},d:['misc','Foo','$at','string']};}};
    $$foo.$prop$getString.get=function(){return string};
    $$foo.inc();
    return $$foo;
}
Foo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[$$$cl4138.shared()];}}],$an:function(){return[$$$cl4138.shared()];},d:['misc','Foo']};};
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl4138.initTypeProto(Foo,'misc::Foo',$$$cl4138.Basic);
    }
    return Foo;
}
exports.$init$Foo=$init$Foo;
$init$Foo();

//ClassDef Bar at misc.ceylon (19:0-34:0)
function Bar($$bar){
    $init$Bar();
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl4138.String("Hello",5),$$bar);
    $$bar.printName$$misc$Foo=$$bar.printName;
    $$bar.printName$$misc$Foo=$$bar.printName;
    X($$bar);
    
    //MethodDef printName at misc.ceylon (20:4-24:4)
    function printName(){
        $$$cl4138.print($$$cl4138.String("bar name = ",11).plus($$bar.name));
        $$bar.printName$$misc$Foo();
        $$bar.printName$$misc$Foo();
    }
    $$bar.printName=printName;
    printName.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Bar,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','Bar','$m','printName']};};
    
    //ClassDef Inner at misc.ceylon (25:4-31:4)
    function Inner$Bar($$inner$Bar){
        $init$Inner$Bar();
        if ($$inner$Bar===undefined)$$inner$Bar=new Inner$Bar.$$;
        $$inner$Bar.$$outer=this;
        $$$cl4138.print($$$cl4138.String("creating inner class of :",25).plus($$bar.name));
        
        //MethodDef incOuter at misc.ceylon (28:8-30:8)
        function incOuter(){
            $$bar.inc();
        }
        $$inner$Bar.incOuter=incOuter;
        incOuter.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:Inner$Bar,$an:function(){return[$$$cl4138.shared()];},d:['misc','Bar','$c','Inner','$m','incOuter']};};
        return $$inner$Bar;
    }
    Inner$Bar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:Bar,$an:function(){return[$$$cl4138.shared()];},d:['misc','Bar','$c','Inner']};};
    $$bar.Inner$Bar=Inner$Bar;
    function $init$Inner$Bar(){
        if (Inner$Bar.$$===undefined){
            $$$cl4138.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl4138.Basic);
            Bar.Inner$Bar=Inner$Bar;
        }
        return Inner$Bar;
    }
    $$bar.$init$Inner$Bar=$init$Inner$Bar;
    $init$Inner$Bar();
    return $$bar;
}
Bar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Foo},$ps:[],satisfies:[{t:X}],$an:function(){return[$$$cl4138.shared()];},d:['misc','Bar']};};
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl4138.initTypeProto(Bar,'misc::Bar',$init$Foo(),$init$X());
    }
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDef printBoth at misc.ceylon (36:0-38:0)
function printBoth(x$5013,y$5014){
    $$$cl4138.print(x$5013.plus($$$cl4138.String(", ",2)).plus(y$5014));
};printBoth.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}},{$nm:'y',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['misc','printBoth']};};

//MethodDef doIt at misc.ceylon (40:0-42:0)
function doIt(f$5015){
    f$5015();
    f$5015();
};doIt.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.Anything},$an:function(){return[];}}],d:['misc','doIt']};};

//ObjectDef foob at misc.ceylon (44:0-46:0)
function foob$5016(){
    var $$foob=new foob$5016.$$;
    
    //AttributeDecl name at misc.ceylon (45:4-45:30)
    var name=$$$cl4138.String("Gavin",5);
    $$$cl4138.defineAttr($$foob,'name',function(){return name;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:foob$5016,$an:function(){return[$$$cl4138.shared()];},d:['misc','foob','$at','name']};});
    $$foob.$prop$getName.get=function(){return name};
    return $$foob;
};foob$5016.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},d:['misc','foob']};};
function $init$foob$5016(){
    if (foob$5016.$$===undefined){
        $$$cl4138.initTypeProto(foob$5016,'misc::foob',$$$cl4138.Basic);
    }
    return foob$5016;
}
exports.$init$foob$5016=$init$foob$5016;
$init$foob$5016();
var foob$5017;
function getFoob(){
    if (foob$5017===undefined){foob$5017=$init$foob$5016()();foob$5017.$$metamodel$$=getFoob.$$metamodel$$;}
    return foob$5017;
}
getFoob.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:foob$5016},d:['misc','foob']};};
$prop$getFoob={get:getFoob,$$metamodel$$:getFoob.$$metamodel$$};
exports.$prop$getFoob=$prop$getFoob;

//MethodDef printAll at misc.ceylon (48:0-48:32)
function printAll(strings$5018){
    if(strings$5018===undefined){strings$5018=$$$cl4138.getEmpty();}
};printAll.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'strings',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.String}}},$an:function(){return[];}}],d:['misc','printAll']};};

//ClassDecl F at misc.ceylon (50:0-50:33)
function F(name$5019, $$f){return Foo(name$5019,$$f);}
F.$$=Foo.$$;
F.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Foo},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],d:['misc','F']};};

//MethodDef var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;
$var.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['misc','var']};};

//AttributeDecl container249 at misc.ceylon (55:0-55:41)
var container249$5020;function $valinit$container249$5020(){if (container249$5020===undefined)container249$5020=$$$cl4138.Tuple(getObject249().$int,$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}});return container249$5020;};$valinit$container249$5020();
function getContainer249(){return $valinit$container249$5020();}
var $prop$getContainer249={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}},d:['misc','container249']};}};
exports.$prop$getContainer249=$prop$getContainer249;
$prop$getContainer249.get=function(){return container249$5020};

//ObjectDef object249 at misc.ceylon (56:0-58:0)
function object249$5021(){
    var $$object249=new object249$5021.$$;
    
    //AttributeDecl int at misc.ceylon (57:2-57:24)
    var $int=(1);
    $$$cl4138.defineAttr($$object249,'$int',function(){return $int;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:object249$5021,$an:function(){return[$$$cl4138.shared()];},d:['misc','object249','$at','int']};});
    $$object249.$prop$getInt={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:object249$5021,$an:function(){return[$$$cl4138.shared()];},d:['misc','object249','$at','int']};}};
    $$object249.$prop$getInt.get=function(){return $int};
    return $$object249;
};object249$5021.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$an:function(){return[$$$cl4138.shared()];},d:['misc','object249']};};
function $init$object249$5021(){
    if (object249$5021.$$===undefined){
        $$$cl4138.initTypeProto(object249$5021,'misc::object249',$$$cl4138.Basic);
    }
    return object249$5021;
}
exports.$init$object249$5021=$init$object249$5021;
$init$object249$5021();
var object249$5022;
function getObject249(){
    if (object249$5022===undefined){object249$5022=$init$object249$5021()();object249$5022.$$metamodel$$=getObject249.$$metamodel$$;}
    return object249$5022;
}
exports.getObject249=getObject249;
getObject249.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:object249$5021},$an:function(){return[$$$cl4138.shared()];},d:['misc','object249']};};
$prop$getObject249={get:getObject249,$$metamodel$$:getObject249.$$metamodel$$};
exports.$prop$getObject249=$prop$getObject249;
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[],
'members/0.1':[]
};};

//ClassDef TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$5023, b$5024, c$5025, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}};
    $$testObjects.a$5023_=a$5023;
    $$testObjects.b$5024_=b$5024;
    $$testObjects.c$5025_=c$5025;
    $$$cl4138.Iterable({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}},$$testObjects);
    $$$cl4138.add_type_arg($$testObjects,'Absent',{t:$$$cl4138.Null});
    $$$cl4138.add_type_arg($$testObjects,'Element',{t:$$$cl4138.Integer});
    $$$cl4138.defineAttr($$testObjects,'a$5023',function(){return this.a$5023_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:TestObjects,d:['misc','TestObjects','$at','a']};});
    $$$cl4138.defineAttr($$testObjects,'b$5024',function(){return this.b$5024_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:TestObjects,d:['misc','TestObjects','$at','b']};});
    $$$cl4138.defineAttr($$testObjects,'c$5025',function(){return this.c$5025_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:TestObjects,d:['misc','TestObjects','$at','c']};});
    
    //MethodDef iterator at objects.ceylon (4:2-16:2)
    function iterator(){
        
        //ObjectDef iter at objects.ceylon (5:4-14:4)
        function iter$5026($$targs$$){
            var $$iter$5026=new iter$5026.$$;
            $$iter$5026.$$targs$$=$$targs$$;
            $$$cl4138.Iterator({Element:{t:$$$cl4138.Integer}},$$iter$5026);
            $$$cl4138.add_type_arg($$iter$5026,'Element',{t:$$$cl4138.Integer});
            
            //AttributeDecl index at objects.ceylon (6:6-6:30)
            var index$5027=(0);
            $$$cl4138.defineAttr($$iter$5026,'index$5027',function(){return index$5027;},function(index$5028){return index$5027=index$5028;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:iter$5026,$an:function(){return[$$$cl4138.variable()];},d:['misc','TestObjects','$m','iterator','$o','iter','$at','index']};});
            $$iter$5026.$prop$getIndex$5027={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:iter$5026,$an:function(){return[$$$cl4138.variable()];},d:['misc','TestObjects','$m','iterator','$o','iter','$at','index']};}};
            $$iter$5026.$prop$getIndex$5027.get=function(){return index$5027};
            
            //MethodDef next at objects.ceylon (7:6-13:6)
            function next(){
                (oldindex$5029=index$5027,index$5027=oldindex$5029.successor,oldindex$5029);
                var oldindex$5029;
                if(index$5027.equals((1))){
                    return a$5023;
                }else {
                    if(index$5027.equals((2))){
                        return b$5024;
                    }else {
                        if(index$5027.equals((3))){
                            return c$5025;
                        }
                    }
                }
                return $$$cl4138.getFinished();
            }
            $$iter$5026.next=next;
            next.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Finished}]},$ps:[],$cont:iter$5026,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','TestObjects','$m','iterator','$o','iter','$m','next']};};
            return $$iter$5026;
        };iter$5026.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},satisfies:[{t:$$$cl4138.Iterator,a:{Element:{t:$$$cl4138.Integer}}}],d:['misc','TestObjects','$m','iterator','$o','iter']};};
        function $init$iter$5026(){
            if (iter$5026.$$===undefined){
                $$$cl4138.initTypeProto(iter$5026,'misc::TestObjects.iterator.iter',$$$cl4138.Basic,$$$cl4138.Iterator);
            }
            return iter$5026;
        }
        $init$iter$5026();
        var iter$5030;
        function getIter$5030(){
            if (iter$5030===undefined){iter$5030=$init$iter$5026()({Element:{t:$$$cl4138.Integer}});iter$5030.$$metamodel$$=getIter$5030.$$metamodel$$;}
            return iter$5030;
        }
        getIter$5030.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:iter$5026},d:['misc','TestObjects','$m','iterator','$o','iter']};};
        $prop$getIter$5030={get:getIter$5030,$$metamodel$$:getIter$5030.$$metamodel$$};
        return getIter$5030();
    }
    $$testObjects.iterator=iterator;
    iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterator,a:{Element:{t:$$$cl4138.Integer}}},$ps:[],$cont:TestObjects,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['misc','TestObjects','$m','iterator']};};
    return $$testObjects;
}
TestObjects.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'c',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],satisfies:[{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}}],d:['misc','TestObjects']};};
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl4138.initTypeProto(TestObjects,'misc::TestObjects',$$$cl4138.Basic,$$$cl4138.Iterable);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDef test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl4138.print($$$cl4138.String("testing objects",15));
    
    //AttributeDecl t1 at objects.ceylon (21:2-21:42)
    var t1$5031=TestObjects((1),(2),(3)).iterator();
    
    //AttributeDecl t2 at objects.ceylon (22:2-22:42)
    var t2$5032=TestObjects((1),(2),(3)).iterator();
    var i$5033;
    if($$$cl4138.isOfType((i$5033=t1$5031.next()),{t:$$$cl4138.Integer})){
        $$$c4139.check(i$5033.equals((1)),$$$cl4138.String("objects 1",9));
    }
    var i$5034;
    if($$$cl4138.isOfType((i$5034=t1$5031.next()),{t:$$$cl4138.Integer})){
        $$$c4139.check(i$5034.equals((2)),$$$cl4138.String("objects 2",9));
    }
    var i$5035;
    if($$$cl4138.isOfType((i$5035=t2$5032.next()),{t:$$$cl4138.Integer})){
        $$$c4139.check(i$5035.equals((1)),$$$cl4138.String("objects 3",9));
    }
    var i$5036;
    if($$$cl4138.isOfType((i$5036=t1$5031.next()),{t:$$$cl4138.Integer})){
        $$$c4139.check(i$5036.equals((3)),$$$cl4138.String("objects 4",9));
    }
    $$$c4139.check($$$cl4138.isOfType(t1$5031.next(),{t:$$$cl4138.Finished}),$$$cl4138.String("objects 5",9));
    var i$5037;
    if($$$cl4138.isOfType((i$5037=t2$5032.next()),{t:$$$cl4138.Integer})){
        $$$c4139.check(i$5037.equals((2)),$$$cl4138.String("objects 6",9));
    }
    var i$5038;
    if($$$cl4138.isOfType((i$5038=t2$5032.next()),{t:$$$cl4138.Integer})){
        $$$c4139.check(i$5038.equals((3)),$$$cl4138.String("objects 7",9));
    }
};test_objects.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','test_objects']};};

//InterfaceDef Top1 at reifiedRuntime.ceylon (3:0-3:22)
function Top1($$top1){
}
Top1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl4138.shared()];},d:['misc','Top1']};};
exports.Top1=Top1;
function $init$Top1(){
    if (Top1.$$===undefined){
        $$$cl4138.initTypeProtoI(Top1,'misc::Top1');
    }
    return Top1;
}
exports.$init$Top1=$init$Top1;
$init$Top1();

//InterfaceDef Middle1 at reifiedRuntime.ceylon (4:0-4:40)
function Middle1($$middle1){
    Top1($$middle1);
}
Middle1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:Top1}],$an:function(){return[$$$cl4138.shared()];},d:['misc','Middle1']};};
exports.Middle1=Middle1;
function $init$Middle1(){
    if (Middle1.$$===undefined){
        $$$cl4138.initTypeProtoI(Middle1,'misc::Middle1',$init$Top1());
    }
    return Middle1;
}
exports.$init$Middle1=$init$Middle1;
$init$Middle1();

//InterfaceDef Bottom1 at reifiedRuntime.ceylon (5:0-5:43)
function Bottom1($$bottom1){
    Middle1($$bottom1);
}
Bottom1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:Middle1}],$an:function(){return[$$$cl4138.shared()];},d:['misc','Bottom1']};};
exports.Bottom1=Bottom1;
function $init$Bottom1(){
    if (Bottom1.$$===undefined){
        $$$cl4138.initTypeProtoI(Bottom1,'misc::Bottom1',$init$Middle1());
    }
    return Bottom1;
}
exports.$init$Bottom1=$init$Bottom1;
$init$Bottom1();

//ClassDef Invariant at reifiedRuntime.ceylon (7:0-7:34)
function Invariant($$targs$$,$$invariant){
    $init$Invariant();
    if ($$invariant===undefined)$$invariant=new Invariant.$$;
    $$$cl4138.set_type_args($$invariant,$$targs$$);
    return $$invariant;
}
Invariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{Element:{}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Invariant']};};
exports.Invariant=Invariant;
function $init$Invariant(){
    if (Invariant.$$===undefined){
        $$$cl4138.initTypeProto(Invariant,'misc::Invariant',$$$cl4138.Basic);
    }
    return Invariant;
}
exports.$init$Invariant=$init$Invariant;
$init$Invariant();

//ClassDef Covariant at reifiedRuntime.ceylon (8:0-8:38)
function Covariant($$targs$$,$$covariant){
    $init$Covariant();
    if ($$covariant===undefined)$$covariant=new Covariant.$$;
    $$$cl4138.set_type_args($$covariant,$$targs$$);
    return $$covariant;
}
Covariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{Element:{'var':'out'}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Covariant']};};
exports.Covariant=Covariant;
function $init$Covariant(){
    if (Covariant.$$===undefined){
        $$$cl4138.initTypeProto(Covariant,'misc::Covariant',$$$cl4138.Basic);
    }
    return Covariant;
}
exports.$init$Covariant=$init$Covariant;
$init$Covariant();

//ClassDef Contravariant at reifiedRuntime.ceylon (9:0-9:41)
function Contravariant($$targs$$,$$contravariant){
    $init$Contravariant();
    if ($$contravariant===undefined)$$contravariant=new Contravariant.$$;
    $$$cl4138.set_type_args($$contravariant,$$targs$$);
    return $$contravariant;
}
Contravariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{Element:{'var':'in'}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Contravariant']};};
exports.Contravariant=Contravariant;
function $init$Contravariant(){
    if (Contravariant.$$===undefined){
        $$$cl4138.initTypeProto(Contravariant,'misc::Contravariant',$$$cl4138.Basic);
    }
    return Contravariant;
}
exports.$init$Contravariant=$init$Contravariant;
$init$Contravariant();

//ClassDef Bivariant at reifiedRuntime.ceylon (10:0-10:41)
function Bivariant($$targs$$,$$bivariant){
    $init$Bivariant();
    if ($$bivariant===undefined)$$bivariant=new Bivariant.$$;
    $$$cl4138.set_type_args($$bivariant,$$targs$$);
    return $$bivariant;
}
Bivariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{In:{'var':'in'},Out:{'var':'out'}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Bivariant']};};
exports.Bivariant=Bivariant;
function $init$Bivariant(){
    if (Bivariant.$$===undefined){
        $$$cl4138.initTypeProto(Bivariant,'misc::Bivariant',$$$cl4138.Basic);
    }
    return Bivariant;
}
exports.$init$Bivariant=$init$Bivariant;
$init$Bivariant();

//ClassDef Container at reifiedRuntime.ceylon (12:0-16:0)
function Container($$targs$$,$$container){
    $init$Container();
    if ($$container===undefined)$$container=new Container.$$;
    $$$cl4138.set_type_args($$container,$$targs$$);
    
    //ClassDef Member at reifiedRuntime.ceylon (13:4-15:4)
    function Member$Container($$targs$$,$$member$Container){
        $init$Member$Container();
        if ($$member$Container===undefined)$$member$Container=new Member$Container.$$;
        $$$cl4138.set_type_args($$member$Container,$$targs$$);
        $$member$Container.$$outer=this;
        
        //ClassDef Child at reifiedRuntime.ceylon (14:8-14:40)
        function Child$Member$Container($$targs$$,$$child$Member$Container){
            $init$Child$Member$Container();
            if ($$child$Member$Container===undefined)$$child$Member$Container=new Child$Member$Container.$$;
            $$$cl4138.set_type_args($$child$Member$Container,$$targs$$);
            $$child$Member$Container.$$outer=this;
            return $$child$Member$Container;
        }
        Child$Member$Container.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:Member$Container,$tp:{InnerMost:{}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Container','$c','Member','$c','Child']};};
        $$member$Container.Child$Member$Container=Child$Member$Container;
        function $init$Child$Member$Container(){
            if (Child$Member$Container.$$===undefined){
                $$$cl4138.initTypeProto(Child$Member$Container,'misc::Container.Member.Child',$$$cl4138.Basic);
                Container.Member$Container.Child$Member$Container=Child$Member$Container;
            }
            return Child$Member$Container;
        }
        $$member$Container.$init$Child$Member$Container=$init$Child$Member$Container;
        $init$Child$Member$Container();
        return $$member$Container;
    }
    Member$Container.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$cont:Container,$tp:{Inner:{}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Container','$c','Member']};};
    $$container.Member$Container=Member$Container;
    function $init$Member$Container(){
        if (Member$Container.$$===undefined){
            $$$cl4138.initTypeProto(Member$Container,'misc::Container.Member',$$$cl4138.Basic);
            Container.Member$Container=Member$Container;
        }
        return Member$Container;
    }
    $$container.$init$Member$Container=$init$Member$Container;
    $init$Member$Container();
    return $$container;
}
Container.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{Outer:{}},$an:function(){return[$$$cl4138.shared()];},d:['misc','Container']};};
exports.Container=Container;
function $init$Container(){
    if (Container.$$===undefined){
        $$$cl4138.initTypeProto(Container,'misc::Container',$$$cl4138.Basic);
    }
    return Container;
}
exports.$init$Container=$init$Container;
$init$Container();

//InterfaceDef TestInterface1 at reifiedRuntime.ceylon (18:0-18:29)
function TestInterface1($$targs$$,$$testInterface1){
    $$$cl4138.set_type_args($$testInterface1,$$targs$$);
}
TestInterface1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$tp:{T:{}},d:['misc','TestInterface1']};};
function $init$TestInterface1(){
    if (TestInterface1.$$===undefined){
        $$$cl4138.initTypeProtoI(TestInterface1,'misc::TestInterface1');
    }
    return TestInterface1;
}
exports.$init$TestInterface1=$init$TestInterface1;
$init$TestInterface1();

//ClassDef Test1 at reifiedRuntime.ceylon (19:0-19:46)
function Test1($$targs$$,$$test1){
    $init$Test1();
    if ($$test1===undefined)$$test1=new Test1.$$;
    $$$cl4138.set_type_args($$test1,$$targs$$);
    TestInterface1($$test1.$$targs$$===undefined?$$targs$$:{T:$$test1.$$targs$$.T},$$test1);
    return $$test1;
}
Test1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{T:{}},satisfies:[{t:TestInterface1,a:{T:'T'}}],d:['misc','Test1']};};
function $init$Test1(){
    if (Test1.$$===undefined){
        $$$cl4138.initTypeProto(Test1,'misc::Test1',$$$cl4138.Basic,$init$TestInterface1());
    }
    return Test1;
}
exports.$init$Test1=$init$Test1;
$init$Test1();

//ClassDef Test2 at reifiedRuntime.ceylon (20:0-20:48)
function Test2($$targs$$,$$test2){
    $init$Test2();
    if ($$test2===undefined)$$test2=new Test2.$$;
    $$$cl4138.set_type_args($$test2,$$targs$$);
    TestInterface1($$test2.$$targs$$===undefined?$$targs$$:{T:$$test2.$$targs$$.T1},$$test2);
    return $$test2;
}
Test2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{T1:{}},satisfies:[{t:TestInterface1,a:{T:'T1'}}],d:['misc','Test2']};};
function $init$Test2(){
    if (Test2.$$===undefined){
        $$$cl4138.initTypeProto(Test2,'misc::Test2',$$$cl4138.Basic,$init$TestInterface1());
    }
    return Test2;
}
exports.$init$Test2=$init$Test2;
$init$Test2();

//MethodDef runtimeMethod at reifiedRuntime.ceylon (22:0-24:0)
function runtimeMethod(param$5039){
    return $$$cl4138.getNothing();
};runtimeMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'param',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['misc','runtimeMethod']};};

//MethodDef testReifiedRuntime at reifiedRuntime.ceylon (26:0-98:0)
function testReifiedRuntime(){
    $$$cl4138.print($$$cl4138.String("Reified generics",16));
    
    //AttributeDecl member at reifiedRuntime.ceylon (28:4-28:57)
    var member$5040=Container({Outer:{t:$$$cl4138.String}}).Member$Container({Inner:{t:$$$cl4138.Integer}});
    $$$c4139.check($$$cl4138.isOfType(member$5040,{t:Container.Member$Container,a:{Outer:{t:$$$cl4138.String},Inner:{t:$$$cl4138.Integer}}}),$$$cl4138.String("reified runtime inner 1",23));
    $$$c4139.check((!$$$cl4138.isOfType(member$5040,{t:Container.Member$Container,a:{Outer:{t:$$$cl4138.Integer},Inner:{t:$$$cl4138.Integer}}})),$$$cl4138.String("reified runtime inner 2",23));
    $$$c4139.check((!$$$cl4138.isOfType(member$5040,{t:Container.Member$Container,a:{Outer:{t:$$$cl4138.String},Inner:{t:$$$cl4138.String}}})),$$$cl4138.String("reified runtime inner 3",23));
    
    //AttributeDecl member2 at reifiedRuntime.ceylon (33:4-33:77)
    var member2$5041=Container({Outer:{t:$$$cl4138.String}}).Member$Container({Inner:{t:$$$cl4138.Integer}}).Child$Member$Container({InnerMost:{t:$$$cl4138.Character}});
    $$$c4139.check($$$cl4138.isOfType(member2$5041,{t:Container.Member$Container.Child$Member$Container,a:{Outer:{t:$$$cl4138.String},Inner:{t:$$$cl4138.Integer},InnerMost:{t:$$$cl4138.Character}}}),$$$cl4138.String("reified runtime inner 4",23));
    
    //AttributeDecl invTop1 at reifiedRuntime.ceylon (36:4-36:38)
    var invTop1$5042=Invariant({Element:{t:Top1}});
    $$$c4139.check($$$cl4138.isOfType(invTop1$5042,{t:Invariant,a:{Element:{t:Top1}}}),$$$cl4138.String("reified runtime invariant 1",27));
    $$$c4139.check((!$$$cl4138.isOfType(invTop1$5042,{t:Invariant,a:{Element:{t:Middle1}}})),$$$cl4138.String("reified runtime invariant 2",27));
    $$$c4139.check((!$$$cl4138.isOfType(invTop1$5042,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl4138.String("reified runtime invariant 3",27));
    
    //AttributeDecl invMiddle1 at reifiedRuntime.ceylon (41:4-41:44)
    var invMiddle1$5043=Invariant({Element:{t:Middle1}});
    $$$c4139.check((!$$$cl4138.isOfType(invMiddle1$5043,{t:Invariant,a:{Element:{t:Top1}}})),$$$cl4138.String("reified runtime invariant 4",27));
    $$$c4139.check($$$cl4138.isOfType(invMiddle1$5043,{t:Invariant,a:{Element:{t:Middle1}}}),$$$cl4138.String("reified runtime invariant 5",27));
    $$$c4139.check((!$$$cl4138.isOfType(invMiddle1$5043,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl4138.String("reified runtime invariant 6",27));
    
    //AttributeDecl covMiddle1 at reifiedRuntime.ceylon (46:4-46:44)
    var covMiddle1$5044=Covariant({Element:{t:Middle1}});
    $$$c4139.check($$$cl4138.isOfType(covMiddle1$5044,{t:Covariant,a:{Element:{t:Top1}}}),$$$cl4138.String("reified runtime covariant 1",27));
    $$$c4139.check($$$cl4138.isOfType(covMiddle1$5044,{t:Covariant,a:{Element:{t:Middle1}}}),$$$cl4138.String("reified runtime covariant 2",27));
    $$$c4139.check((!$$$cl4138.isOfType(covMiddle1$5044,{t:Covariant,a:{Element:{t:Bottom1}}})),$$$cl4138.String("reified runtime covariant 3",27));
    
    //AttributeDecl contravMiddle1 at reifiedRuntime.ceylon (51:4-51:52)
    var contravMiddle1$5045=Contravariant({Element:{t:Middle1}});
    $$$c4139.check((!$$$cl4138.isOfType(contravMiddle1$5045,{t:Contravariant,a:{Element:{t:Top1}}})),$$$cl4138.String("reified runtime contravariant 1",31));
    $$$c4139.check($$$cl4138.isOfType(contravMiddle1$5045,{t:Contravariant,a:{Element:{t:Middle1}}}),$$$cl4138.String("reified runtime contravariant 2",31));
    $$$c4139.check($$$cl4138.isOfType(contravMiddle1$5045,{t:Contravariant,a:{Element:{t:Bottom1}}}),$$$cl4138.String("reified runtime contravariant 3",31));
    
    //AttributeDecl bivMiddle1 at reifiedRuntime.ceylon (56:4-56:52)
    var bivMiddle1$5046=Bivariant({Out:{t:Middle1},In:{t:Middle1}});
    $$$c4139.check((!$$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Top1},In:{t:Top1}}})),$$$cl4138.String("reified runtime bivariant 1",27));
    $$$c4139.check((!$$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Top1}}})),$$$cl4138.String("reified runtime bivariant 2",27));
    $$$c4139.check((!$$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Top1}}})),$$$cl4138.String("reified runtime bivariant 3",27));
    $$$c4139.check($$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Top1},In:{t:Middle1}}}),$$$cl4138.String("reified runtime bivariant 4",27));
    $$$c4139.check($$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Middle1}}}),$$$cl4138.String("reified runtime bivariant 5",27));
    $$$c4139.check((!$$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Middle1}}})),$$$cl4138.String("reified runtime bivariant 6",27));
    $$$c4139.check($$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Top1},In:{t:Bottom1}}}),$$$cl4138.String("reified runtime bivariant 7",27));
    $$$c4139.check($$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Bottom1}}}),$$$cl4138.String("reified runtime bivariant 8",27));
    $$$c4139.check((!$$$cl4138.isOfType(bivMiddle1$5046,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Bottom1}}})),$$$cl4138.String("reified runtime bivariant 9",27));
    
    //ClassDef Local at reifiedRuntime.ceylon (67:4-67:21)
    function Local$5047($$targs$$,$$local$5047){
        $init$Local$5047();
        if ($$local$5047===undefined)$$local$5047=new Local$5047.$$;
        $$$cl4138.set_type_args($$local$5047,$$targs$$);
        return $$local$5047;
    }
    Local$5047.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{T:{}},d:['misc','testReifiedRuntime','$c','Local']};};
    function $init$Local$5047(){
        if (Local$5047.$$===undefined){
            $$$cl4138.initTypeProto(Local$5047,'misc::testReifiedRuntime.Local',$$$cl4138.Basic);
        }
        return Local$5047;
    }
    $init$Local$5047();
    
    //AttributeDecl localInteger at reifiedRuntime.ceylon (69:4-69:42)
    var localInteger$5048=Local$5047({T:{t:$$$cl4138.Integer}});
    $$$c4139.check($$$cl4138.isOfType(localInteger$5048,{t:Local$5047,a:{T:{t:$$$cl4138.Integer}}}),$$$cl4138.String("reified runtime local 1",23));
    
    //AttributeDecl m at reifiedRuntime.ceylon (72:4-72:28)
    var m$5049=$$$cl4138.$JsCallable(runtimeMethod,[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}});
    $$$c4139.check($$$cl4138.isOfType(m$5049,{t:$$$cl4138.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.String}}}),$$$cl4138.String("reified runtime callable 1",26));
    $$$c4139.check((!$$$cl4138.isOfType(m$5049,{t:$$$cl4138.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Integer}}})),$$$cl4138.String("reified runtime callable 2",26));
    $$$c4139.check((!$$$cl4138.isOfType(m$5049,{t:$$$cl4138.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl4138.String}]},Return:{t:$$$cl4138.String}}})),$$$cl4138.String("reified runtime callable 3",26));
    $$$c4139.check((!$$$cl4138.isOfType(m$5049,{t:$$$cl4138.Callable,a:{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.String}}})),$$$cl4138.String("reified runtime callable 4",26));
    
    //AttributeDecl m2 at reifiedRuntime.ceylon (77:4-77:34)
    var m2$5050=$$$cl4138.$JsCallable(testReifiedRuntime,[],{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Anything}});
    $$$c4139.check($$$cl4138.isOfType(m2$5050,{t:$$$cl4138.Callable,a:{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Anything}}}),$$$cl4138.String("reified runtime callable 5",26));
    $$$c4139.check((!$$$cl4138.isOfType(m2$5050,{t:$$$cl4138.Callable,a:{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.String}}})),$$$cl4138.String("reified runtime callable 6",26));
    $$$c4139.check((!$$$cl4138.isOfType(m2$5050,{t:$$$cl4138.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Anything}}})),$$$cl4138.String("reified runtime callable 7",26));
    
    //AttributeDecl rec1 at reifiedRuntime.ceylon (82:4-82:80)
    var rec1$5051=$$$cl4138.Singleton($$$cl4138.Entry((1),$$$cl4138.Singleton($$$cl4138.String("x",1),{Element:{t:$$$cl4138.String}}),{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.String}}}}),{Element:{t:$$$cl4138.Entry,a:{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.String}}}}}});
    $$$c4139.check($$$cl4138.isOfType(rec1$5051,{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.Entry,a:{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.String}}}}}}}),$$$cl4138.String("#188 [1]",8));
    $$$c4139.check((!$$$cl4138.isOfType(rec1$5051,{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.Entry,a:{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.Singleton,a:{Element:{t:$$$cl4138.Integer}}}}}}})),$$$cl4138.String("#188 [2]",8));
    
    //InterfaceDef TestInterface2 at reifiedRuntime.ceylon (87:4-87:36)
    function TestInterface2$5052($$targs$$,$$testInterface2$5052){
        $$$cl4138.set_type_args($$testInterface2$5052,$$targs$$);
    }
    TestInterface2$5052.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$tp:{T:{'var':'in'}},d:['misc','testReifiedRuntime','$i','TestInterface2']};};
    function $init$TestInterface2$5052(){
        if (TestInterface2$5052.$$===undefined){
            $$$cl4138.initTypeProtoI(TestInterface2$5052,'misc::testReifiedRuntime.TestInterface2');
        }
        return TestInterface2$5052;
    }
    $init$TestInterface2$5052();
    
    //ClassDef Test3 at reifiedRuntime.ceylon (88:4-88:53)
    function Test3$5053($$targs$$,$$test3$5053){
        $init$Test3$5053();
        if ($$test3$5053===undefined)$$test3$5053=new Test3$5053.$$;
        $$$cl4138.set_type_args($$test3$5053,$$targs$$);
        TestInterface2$5052($$test3$5053.$$targs$$===undefined?$$targs$$:{T:$$test3$5053.$$targs$$.T},$$test3$5053);
        return $$test3$5053;
    }
    Test3$5053.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{T:{'var':'in'}},satisfies:[{t:TestInterface2$5052,a:{T:'T'}}],d:['misc','testReifiedRuntime','$c','Test3']};};
    function $init$Test3$5053(){
        if (Test3$5053.$$===undefined){
            $$$cl4138.initTypeProto(Test3$5053,'misc::testReifiedRuntime.Test3',$$$cl4138.Basic,$init$TestInterface2$5052());
        }
        return Test3$5053;
    }
    $init$Test3$5053();
    
    //ClassDef Test4 at reifiedRuntime.ceylon (89:4-89:55)
    function Test4$5054($$targs$$,$$test4$5054){
        $init$Test4$5054();
        if ($$test4$5054===undefined)$$test4$5054=new Test4$5054.$$;
        $$$cl4138.set_type_args($$test4$5054,$$targs$$);
        TestInterface2$5052($$test4$5054.$$targs$$===undefined?$$targs$$:{T:$$test4$5054.$$targs$$.T1},$$test4$5054);
        return $$test4$5054;
    }
    Test4$5054.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],$tp:{T1:{'var':'in'}},satisfies:[{t:TestInterface2$5052,a:{T:'T1'}}],d:['misc','testReifiedRuntime','$c','Test4']};};
    function $init$Test4$5054(){
        if (Test4$5054.$$===undefined){
            $$$cl4138.initTypeProto(Test4$5054,'misc::testReifiedRuntime.Test4',$$$cl4138.Basic,$init$TestInterface2$5052());
        }
        return Test4$5054;
    }
    $init$Test4$5054();
    
    //AttributeDecl o1 at reifiedRuntime.ceylon (90:4-90:31)
    var o1$5055=Test1({T:{t:$$$cl4138.String}});
    $$$c4139.check($$$cl4138.isOfType(o1$5055,{t:TestInterface1,a:{T:{t:$$$cl4138.String}}}),$$$cl4138.String("Issue #221 [1]",14));
    
    //AttributeDecl o2 at reifiedRuntime.ceylon (92:4-92:31)
    var o2$5056=Test2({T1:{t:$$$cl4138.String}});
    $$$c4139.check($$$cl4138.isOfType(o2$5056,{t:TestInterface1,a:{T:{t:$$$cl4138.String}}}),$$$cl4138.String("Issue #221 [2]",14));
    
    //AttributeDecl o3 at reifiedRuntime.ceylon (94:4-94:31)
    var o3$5057=Test3$5053({T:{t:$$$cl4138.String}});
    $$$c4139.check($$$cl4138.isOfType(o3$5057,{t:TestInterface2$5052,a:{T:{t:$$$cl4138.String}}}),$$$cl4138.String("Issue #221 [3]",14));
    
    //AttributeDecl o4 at reifiedRuntime.ceylon (96:4-96:31)
    var o4$5058=Test4$5054({T1:{t:$$$cl4138.String}});
    $$$c4139.check($$$cl4138.isOfType(o4$5058,{t:TestInterface2$5052,a:{T:{t:$$$cl4138.String}}}),$$$cl4138.String("Issue #221 [4]",14));
};testReifiedRuntime.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['misc','testReifiedRuntime']};};
var $$$m5059=require('members/0.1/members-0.1');
$$$cl4138.$addmod$($$$m5059,'members/0.1');

//MethodDef test at testit.ceylon (4:0-45:0)
function test(){
    
    //AttributeDecl name at testit.ceylon (5:4-5:24)
    var name$5060=$$$cl4138.String("hello",5);
    $$$cl4138.print(name$5060);
    
    //AttributeDecl foo at testit.ceylon (7:4-7:28)
    var foo$5061=Foo($$$cl4138.String("goodbye",7));
    printBoth(name$5060,foo$5061.name);
    (y$5062=$$$cl4138.String("y",1),x$5063=$$$cl4138.String("x",1),printBoth(x$5063,y$5062));
    var y$5062,x$5063;
    foo$5061.inc();
    foo$5061.inc();
    $$$c4139.check(foo$5061.count.equals((3)),$$$cl4138.String("Foo.count",9));
    $$$c4139.check(foo$5061.string.equals($$$cl4138.String("Foo(goodbye)",12)),$$$cl4138.String("Foo.string",10));
    foo$5061.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt($$$cl4138.$JsCallable((opt$5064=foo$5061,$$$cl4138.JsCallable(opt$5064,opt$5064!==null?opt$5064.inc:null)),[],{Arguments:{t:$$$cl4138.Empty},Return:{t:$$$cl4138.Anything}}));
    var opt$5064;
    $$$c4139.check(foo$5061.count.equals((5)),$$$cl4138.String("Foo.count [2]",13));
    doIt($$$cl4138.$JsCallable(Bar,[],{Arguments:{t:$$$cl4138.Empty},Return:{t:Bar}}));
    $$$cl4138.print(getFoob().name);
    
    //ObjectDef x at testit.ceylon (20:4-24:4)
    function x$5065(){
        var $$x$5065=new x$5065.$$;
        
        //MethodDef y at testit.ceylon (21:8-23:8)
        function y(){
            $$$cl4138.print($$$cl4138.String("xy",2));
        }
        $$x$5065.y=y;
        y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:x$5065,$an:function(){return[$$$cl4138.shared()];},d:['misc','test','$o','x','$m','y']};};
        return $$x$5065;
    };x$5065.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},d:['misc','test','$o','x']};};
    function $init$x$5065(){
        if (x$5065.$$===undefined){
            $$$cl4138.initTypeProto(x$5065,'misc::test.x',$$$cl4138.Basic);
        }
        return x$5065;
    }
    $init$x$5065();
    var x$5066;
    function getX$5066(){
        if (x$5066===undefined){x$5066=$init$x$5065()();x$5066.$$metamodel$$=getX$5066.$$metamodel$$;}
        return x$5066;
    }
    getX$5066.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:x$5065},d:['misc','test','$o','x']};};
    $prop$getX$5066={get:getX$5066,$$metamodel$$:getX$5066.$$metamodel$$};
    getX$5066().y();
    
    //AttributeDecl b at testit.ceylon (26:4-26:17)
    var b$5067=Bar();
    b$5067.Inner$Bar().incOuter();
    b$5067.Inner$Bar().incOuter();
    b$5067.Inner$Bar().incOuter();
    $$$c4139.check(b$5067.count.equals((4)),$$$cl4138.String("Bar.count",9));
    printAll([$$$cl4138.String("hello",5),$$$cl4138.String("world",5)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}));
    (strings$5068=$$$cl4138.Tuple($$$cl4138.String("hello",5),$$$cl4138.Tuple($$$cl4138.String("world",5),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),printAll(strings$5068));
    var strings$5068;
    
    //AttributeDecl c at testit.ceylon (34:4-34:26)
    var c$5069=$$$m5059.Counter((0));
    c$5069.inc();
    c$5069.inc();
    $$$c4139.check(c$5069.count.equals((2)),$$$cl4138.String("Counter.count",13));
    
    //AttributeDecl v2 at testit.ceylon (38:4-38:20)
    var v2$5070=$var();
    test_objects();
    testAliasing();
    testLate();
    testReifiedRuntime();
    testStackTrace();
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['misc','test']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
