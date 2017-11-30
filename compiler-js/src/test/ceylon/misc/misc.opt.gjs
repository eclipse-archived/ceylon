(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1","members\/0.1"],"$mod-name":"misc","$mod-version":"0.1","$mod-bin":"6.0","misc":{"doIt":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f"}},"$nm":"doIt"},"testLate":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testLate"},"F":{"super":{"$pk":"misc","$nm":"Foo"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"name"}],"$mt":"cls","$alias":"1","$nm":"F"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test","$o":{"x":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$m":{"y":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"y"}},"$nm":"x"}}},"var":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"var"},"AliasingClass":{"$i":{"AliasingIface":{"$mt":"ifc","$an":{"shared":[]},"$m":{"aliasingIface":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingIface"}},"$nm":"AliasingIface"}},"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$c":{"AliasingInner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"aliasingInner":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingInner"}},"$nm":"AliasingInner"}},"$nm":"AliasingClass"},"Matrix":{"$mt":"ifc","$tp":[{"$nm":"Cell"}],"$alias":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"Cell"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequence"},"$nm":"Matrix"},"Test2":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T1"}],"$pk":"misc","$nm":"TestInterface1"}],"$mt":"cls","$tp":[{"$nm":"T1"}],"$nm":"Test2"},"MiMatrix":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"gridSize"}],"satisfies":[{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"contains":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"other"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"contains"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"gridSize":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"gridSize"},"clone":{"$t":{"$pk":"misc","$nm":"MiMatrix"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"last":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"last"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"string"},"sb":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"SequenceBuilder"},"$mt":"attr","$nm":"sb"},"grid":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"grid"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"},"reversed":{"$t":{"$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"misc","$nm":"Matrix"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"first":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequence"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"first"},"size":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"size"}},"$nm":"MiMatrix"},"LateTestParent":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$hdn":"1","$mt":"prm","seq":"1","$an":{"shared":[]},"$nm":"children"}],"$mt":"cls","$at":{"children":{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$pk":"misc","$nm":"LateTestChild"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"attr","$an":{"shared":[]},"$nm":"children"}},"$nm":"LateTestParent"},"TestObjects":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"}],"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator","$o":{"iter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"}],"$mt":"obj","$m":{"next":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Finished"}]},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"next"}},"$at":{"index":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"index"}},"$nm":"iter"}}}},"$at":{"b":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"b"},"c":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"c"},"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"a"}},"$nm":"TestObjects"},"Issue225Alias":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$nm":"Issue225Alias"},"Bivariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"In"},{"variance":"out","$nm":"Out"}],"$an":{"shared":[]},"$nm":"Bivariant"},"testAliasing":{"$i":{"AliasedIface2":{"$mt":"ifc","$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface2"}},"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"use":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$pk":"misc","$nm":"AliasedIface2"},"$mt":"prm","$nm":"aif"}]],"$mt":"mthd","$nm":"use"},"cualquiera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Boolean"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"bits"}]],"$mt":"mthd","$nm":"cualquiera"}},"$c":{"InnerSubalias":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$alias":"1","$nm":"InnerSubalias"}},"$nm":"testAliasing"},"AliasingSub2":{"super":{"$pk":"misc","$nm":"AliasingSubclass"},"$mt":"cls","$at":{"iface":{"$t":{"$pk":"misc","$nm":"AliasedIface"},"$mt":"gttr","$an":{"shared":[]},"$nm":"iface","$o":{"aliased":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$pk":"misc","$nm":"AliasedIface"}],"$mt":"obj","$nm":"aliased"}}}},"$nm":"AliasingSub2"},"Foo":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$hdn":"1","$mt":"prm","$an":{"shared":[]},"$nm":"name"}],"$mt":"cls","$an":{"shared":[]},"$m":{"inc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"inc"},"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"printName"}},"$at":{"count":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"count"},"string":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"string"},"counter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"counter"},"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"Foo"},"Bar":{"super":{"$pk":"misc","$nm":"Foo"},"satisfies":[{"$pk":"misc","$nm":"X"}],"$mt":"cls","$an":{"shared":[]},"$m":{"printName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"printName"}},"$c":{"Inner":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$an":{"shared":[]},"$m":{"incOuter":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"incOuter"}},"$nm":"Inner"}},"$nm":"Bar"},"X":{"$mt":"ifc","$an":{"shared":[]},"$m":{"helloWorld":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"helloWorld"}},"$nm":"X"},"Container":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Outer"}],"$an":{"shared":[]},"$c":{"Member":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Inner"}],"$an":{"shared":[]},"$c":{"Child":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"InnerMost"}],"$an":{"shared":[]},"$nm":"Child"}},"$nm":"Member"}},"$nm":"Container"},"Covariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"out","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Covariant"},"runtimeMethod":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"param"}]],"$mt":"mthd","$nm":"runtimeMethod"},"Top1":{"$mt":"ifc","$an":{"shared":[]},"$nm":"Top1"},"Strinteger":{"$mt":"als","$an":{"shared":[]},"$alias":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$nm":"Strinteger"},"Middle1":{"satisfies":[{"$pk":"misc","$nm":"Top1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Middle1"},"m1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"m1"},"m2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"m2"},"m3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"m3"},"container249":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"attr","$nm":"container249"},"Test1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"misc","$nm":"TestInterface1"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$nm":"Test1"},"foob":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$at":{"name":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$an":{"shared":[]},"$nm":"name"}},"$nm":"foob"},"LateTestChild":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"parent":{"$t":{"$pk":"misc","$nm":"LateTestParent"},"$mt":"attr","$an":{"shared":[],"late":[]},"$nm":"parent"}},"$nm":"LateTestChild"},"Invariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"Element"}],"$an":{"shared":[]},"$nm":"Invariant"},"issue225_1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$mt":"prm","$nm":"content"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"issue225_1"},"AliasingSubclass":{"$i":{"AliasedIface":{"$mt":"ifc","$an":{"shared":[]},"$alias":{"$pk":"misc","$nm":"AliasingIface"},"$nm":"AliasedIface"}},"super":{"$pk":"misc","$nm":"AliasingClass"},"$mt":"cls","$m":{"aliasingSubclass":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"mthd","$an":{"shared":[]},"$nm":"aliasingSubclass"}},"$c":{"InnerAlias":{"super":{"$pk":"misc","$nm":"AliasingInner"},"$mt":"cls","$an":{"shared":[]},"$alias":"1","$nm":"InnerAlias"},"SubAlias":{"super":{"$pk":"misc","$nm":"InnerAlias"},"$mt":"cls","$an":{"shared":[]},"$nm":"SubAlias"}},"$nm":"AliasingSubclass"},"issue225_2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$pk":"misc","$nm":"Issue225Alias"},"$mt":"prm","$nm":"content"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"issue225_2"},"testStackTrace":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testStackTrace"},"TestInterface1":{"$mt":"ifc","$tp":[{"$nm":"T"}],"$nm":"TestInterface1"},"Contravariant":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"variance":"in","$nm":"Element"}],"$an":{"shared":[]},"$nm":"Contravariant"},"object249":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"obj","$an":{"shared":[]},"$at":{"int":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[]},"$nm":"int"}},"$nm":"object249"},"test_objects":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"test_objects"},"testReifiedRuntime":{"$i":{"TestInterface2":{"$mt":"ifc","$tp":[{"variance":"in","$nm":"T"}],"$nm":"TestInterface2"}},"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$c":{"Local":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$tp":[{"$nm":"T"}],"$nm":"Local"},"Test3":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"misc","$nm":"TestInterface2"}],"$mt":"cls","$tp":[{"variance":"in","$nm":"T"}],"$nm":"Test3"},"Test4":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$tp":[{"$mt":"tpm","$nm":"T1"}],"$pk":"misc","$nm":"TestInterface2"}],"$mt":"cls","$tp":[{"variance":"in","$nm":"T1"}],"$nm":"Test4"}},"$nm":"testReifiedRuntime"},"Bottom1":{"satisfies":[{"$pk":"misc","$nm":"Middle1"}],"$mt":"ifc","$an":{"shared":[]},"$nm":"Bottom1"},"Listleton":{"super":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"}],"$pk":"ceylon.language","$nm":"Singleton"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$mt":"tpm","$nm":"T"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"prm","$nm":"l"}],"$mt":"cls","$tp":[{"$nm":"T"}],"$alias":"1","$nm":"Listleton"},"Test284":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$pk":"misc","$nm":"Strinteger"},"$mt":"prm","$nm":"x"}],"$mt":"cls","$an":{"shared":[]},"$at":{"x":{"$t":{"$pk":"misc","$nm":"Strinteger"},"$mt":"attr","$nm":"x"}},"$nm":"Test284"},"printAll":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"strings"}]],"$mt":"mthd","$nm":"printAll"},"printBoth":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"x"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"y"}]],"$mt":"mthd","$nm":"printBoth"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//TypeAliasDecl Strinteger at aliases.ceylon (3:0-3:41)
function Strinteger(){var tmpvar$856={t:'u', l:[{t:$$$cl1.String},{t:$$$cl1.Integer}]};tmpvar$856.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl1.shared()];},d:['misc','Strinteger']};};return tmpvar$856;}
exports.Strinteger=Strinteger;

//ClassDef Test284 at aliases.ceylon (5:0-7:0)
function Test284(x$857, $$test284){
    $init$Test284();
    if ($$test284===undefined)$$test284=new Test284.$$;
    $$test284.x$857_=x$857;
    $$$cl1.print($$test284.x$857);
    return $$test284;
}
Test284.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'x',$mt:'prm',$t:Strinteger(),$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['misc','Test284']};};
exports.Test284=Test284;
function $init$Test284(){
    if (Test284.$$===undefined){
        $$$cl1.initTypeProto(Test284,'misc::Test284',$$$cl1.Basic);
        (function($$test284){
            $$$cl1.defineAttr($$test284,'x$857',function(){return this.x$857_;},undefined,function(){return{mod:$$METAMODEL$$,$t:Strinteger(),$cont:Test284,d:['misc','Test284','$at','x']};});
        })(Test284.$$.prototype);
    }
    return Test284;
}
exports.$init$Test284=$init$Test284;
$init$Test284();

//ClassDef AliasingClass at aliases.ceylon (9:0-16:0)
function AliasingClass($$aliasingClass){
    $init$AliasingClass();
    if ($$aliasingClass===undefined)$$aliasingClass=new AliasingClass.$$;
    return $$aliasingClass;
}
AliasingClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingClass']};};
exports.AliasingClass=AliasingClass;
function $init$AliasingClass(){
    if (AliasingClass.$$===undefined){
        $$$cl1.initTypeProto(AliasingClass,'misc::AliasingClass',$$$cl1.Basic);
        (function($$aliasingClass){
            
            //InterfaceDef AliasingIface at aliases.ceylon (10:4-12:4)
            function AliasingIface$AliasingClass($$aliasingIface$AliasingClass){
                $$aliasingIface$AliasingClass.$$outer=this;
            }
            AliasingIface$AliasingClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasingClass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingClass','$i','AliasingIface']};};
            function $init$AliasingIface$AliasingClass(){
                if (AliasingIface$AliasingClass.$$===undefined){
                    $$$cl1.initTypeProtoI(AliasingIface$AliasingClass,'misc::AliasingClass.AliasingIface');
                    AliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
                    (function($$aliasingIface$AliasingClass){
                        
                        //MethodDef aliasingIface at aliases.ceylon (11:8-11:54)
                        $$aliasingIface$AliasingClass.aliasingIface=function aliasingIface(){
                            var $$aliasingIface$AliasingClass=this;
                            return true;
                        };$$aliasingIface$AliasingClass.aliasingIface.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[],$cont:AliasingIface$AliasingClass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingClass','$i','AliasingIface','$m','aliasingIface']};};
                    })(AliasingIface$AliasingClass.$$.prototype);
                }
                return AliasingIface$AliasingClass;
            }
            $$aliasingClass.$init$AliasingIface$AliasingClass=$init$AliasingIface$AliasingClass;
            $init$AliasingIface$AliasingClass();
            $$aliasingClass.AliasingIface$AliasingClass=AliasingIface$AliasingClass;
            
            //ClassDef AliasingInner at aliases.ceylon (13:4-15:4)
            function AliasingInner$AliasingClass($$aliasingInner$AliasingClass){
                $init$AliasingInner$AliasingClass();
                if ($$aliasingInner$AliasingClass===undefined)$$aliasingInner$AliasingClass=new this.AliasingInner$AliasingClass.$$;
                $$aliasingInner$AliasingClass.$$outer=this;
                return $$aliasingInner$AliasingClass;
            }
            AliasingInner$AliasingClass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:AliasingClass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingClass','$c','AliasingInner']};};
            function $init$AliasingInner$AliasingClass(){
                if (AliasingInner$AliasingClass.$$===undefined){
                    $$$cl1.initTypeProto(AliasingInner$AliasingClass,'misc::AliasingClass.AliasingInner',$$$cl1.Basic);
                    AliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
                    (function($$aliasingInner$AliasingClass){
                        
                        //MethodDef aliasingInner at aliases.ceylon (14:8-14:54)
                        $$aliasingInner$AliasingClass.aliasingInner=function aliasingInner(){
                            var $$aliasingInner$AliasingClass=this;
                            return true;
                        };$$aliasingInner$AliasingClass.aliasingInner.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[],$cont:AliasingInner$AliasingClass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingClass','$c','AliasingInner','$m','aliasingInner']};};
                    })(AliasingInner$AliasingClass.$$.prototype);
                }
                return AliasingInner$AliasingClass;
            }
            $$aliasingClass.$init$AliasingInner$AliasingClass=$init$AliasingInner$AliasingClass;
            $init$AliasingInner$AliasingClass();
            $$aliasingClass.AliasingInner$AliasingClass=AliasingInner$AliasingClass;
        })(AliasingClass.$$.prototype);
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
    return $$aliasingSubclass;
}
AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingClass},$ps:[],d:['misc','AliasingSubclass']};};
function $init$AliasingSubclass(){
    if (AliasingSubclass.$$===undefined){
        $$$cl1.initTypeProto(AliasingSubclass,'misc::AliasingSubclass',$init$AliasingClass());
        (function($$aliasingSubclass){
            
            //ClassDecl InnerAlias at aliases.ceylon (19:4-19:48)
            function InnerAlias$AliasingSubclass($$innerAlias$AliasingSubclass){return $$aliasingSubclass.AliasingInner$AliasingClass($$innerAlias$AliasingSubclass);}
            InnerAlias$AliasingSubclass.$$=$$aliasingSubclass.AliasingInner$AliasingClass.$$;
            InnerAlias$AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingClass.AliasingInner$AliasingClass},$ps:[],$cont:AliasingSubclass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingSubclass','$c','InnerAlias']};};
            $$aliasingSubclass.InnerAlias$AliasingSubclass=InnerAlias$AliasingSubclass;
            
            //ClassDef SubAlias at aliases.ceylon (20:4-20:50)
            function SubAlias$AliasingSubclass($$subAlias$AliasingSubclass){
                $init$SubAlias$AliasingSubclass();
                if ($$subAlias$AliasingSubclass===undefined)$$subAlias$AliasingSubclass=new this.SubAlias$AliasingSubclass.$$;
                $$subAlias$AliasingSubclass.$$outer=this;
                $$subAlias$AliasingSubclass.$$outer.InnerAlias$AliasingSubclass($$subAlias$AliasingSubclass);
                return $$subAlias$AliasingSubclass;
            }
            SubAlias$AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingSubclass.InnerAlias$AliasingSubclass},$ps:[],$cont:AliasingSubclass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingSubclass','$c','SubAlias']};};
            function $init$SubAlias$AliasingSubclass(){
                if (SubAlias$AliasingSubclass.$$===undefined){
                    $$$cl1.initTypeProto(SubAlias$AliasingSubclass,'misc::AliasingSubclass.SubAlias',$$aliasingSubclass.InnerAlias$AliasingSubclass);
                    AliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
                }
                return SubAlias$AliasingSubclass;
            }
            $$aliasingSubclass.$init$SubAlias$AliasingSubclass=$init$SubAlias$AliasingSubclass;
            $init$SubAlias$AliasingSubclass();
            $$aliasingSubclass.SubAlias$AliasingSubclass=SubAlias$AliasingSubclass;
            
            //MethodDef aliasingSubclass at aliases.ceylon (22:4-24:4)
            $$aliasingSubclass.aliasingSubclass=function aliasingSubclass(){
                var $$aliasingSubclass=this;
                return $$aliasingSubclass.SubAlias$AliasingSubclass().aliasingInner();
            };$$aliasingSubclass.aliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[],$cont:AliasingSubclass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingSubclass','$m','aliasingSubclass']};};
            
            //InterfaceDecl AliasedIface at aliases.ceylon (25:4-25:50)
            function AliasedIface$AliasingSubclass($$aliasedIface$AliasingSubclass){$$aliasingSubclass.AliasingIface$AliasingClass($$aliasedIface$AliasingSubclass);}
            AliasedIface$AliasingSubclass.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$cont:AliasingSubclass,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingSubclass','$i','AliasedIface']};};
            $$aliasingSubclass.AliasedIface$AliasingSubclass=AliasedIface$AliasingSubclass;
        })(AliasingSubclass.$$.prototype);
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
    return $$aliasingSub2;
}
AliasingSub2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingSubclass},$ps:[],d:['misc','AliasingSub2']};};
function $init$AliasingSub2(){
    if (AliasingSub2.$$===undefined){
        $$$cl1.initTypeProto(AliasingSub2,'misc::AliasingSub2',$init$AliasingSubclass());
        (function($$aliasingSub2){
            
            //AttributeGetterDef iface at aliases.ceylon (29:4-33:4)
            $$$cl1.defineAttr($$aliasingSub2,'iface',function(){
                var $$aliasingSub2=this;
                
                //ObjectDef aliased at aliases.ceylon (30:8-31:8)
                function aliased$858(){
                    var $$aliased$858=new aliased$858.$$;
                    $$aliasingSub2.AliasedIface$AliasingSubclass($$aliased$858);
                    return $$aliased$858;
                };aliased$858.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},satisfies:[{t:AliasingSubclass.AliasedIface$AliasingSubclass}],d:['misc','AliasingSub2','$at','iface','$o','aliased']};};
                function $init$aliased$858(){
                    if (aliased$858.$$===undefined){
                        $$$cl1.initTypeProto(aliased$858,'misc::AliasingSub2.iface.aliased',$$$cl1.Basic,$$aliasingSub2.$init$AliasingIface$AliasingClass());
                    }
                    return aliased$858;
                }
                $init$aliased$858();
                var aliased$859;
                function getAliased$859(){
                    if (aliased$859===undefined){aliased$859=$init$aliased$858()();aliased$859.$$metamodel$$=getAliased$859.$$metamodel$$;}
                    return aliased$859;
                }
                getAliased$859.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:aliased$858},d:['misc','AliasingSub2','$at','iface','$o','aliased']};};
                $prop$getAliased$859={get:getAliased$859,$$metamodel$$:getAliased$859.$$metamodel$$};
                return getAliased$859();
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:AliasingSubclass.AliasedIface$AliasingSubclass},$cont:AliasingSub2,$an:function(){return[$$$cl1.shared()];},d:['misc','AliasingSub2','$at','iface']};});
        })(AliasingSub2.$$.prototype);
    }
    return AliasingSub2;
}
exports.$init$AliasingSub2=$init$AliasingSub2;
$init$AliasingSub2();

//InterfaceDecl Matrix at aliases.ceylon (36:0-36:43)
function Matrix($$targs$$,$$matrix){$$$cl1.Sequence({Element:{t:$$$cl1.Sequence,a:{Element:$$targs$$.Cell}}},$$matrix);}
Matrix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$tp:{Cell:{}},d:['misc','Matrix']};};

//ClassDecl Listleton at aliases.ceylon (37:0-37:54)
function Listleton(l$860, $$targs$$,$$listleton){return $$$cl1.Singleton(l$860,{Element:{t:$$$cl1.List,a:{Element:$$targs$$.T}}},$$listleton);}
Listleton.$$=$$$cl1.Singleton.$$;
Listleton.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.List,a:{Element:'T'}}}},$ps:[{$nm:'l',$mt:'prm',$t:{t:$$$cl1.List,a:{Element:'T'}},$an:function(){return[];}}],$tp:{T:{}},d:['misc','Listleton']};};

//ClassDef MiMatrix at aliases.ceylon (39:0-70:0)
function MiMatrix(gridSize$861, $$miMatrix){
    $init$MiMatrix();
    if ($$miMatrix===undefined)$$miMatrix=new MiMatrix.$$;
    $$miMatrix.$$targs$$={Cell:{t:$$$cl1.Integer}};
    $$miMatrix.gridSize$861_=gridSize$861;
    Matrix({Cell:{t:$$$cl1.Integer}},$$miMatrix);
    $$$cl1.add_type_arg($$miMatrix,'Cell',{t:$$$cl1.Integer});
    
    //AttributeDecl sb at aliases.ceylon (40:4-40:44)
    $$miMatrix.sb$862_=$$$cl1.SequenceBuilder({Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}});
    $$miMatrix.$prop$getSb$862={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.SequenceBuilder,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$cont:MiMatrix,d:['misc','MiMatrix','$at','sb']};}};
    $$miMatrix.$prop$getSb$862.get=function(){return sb$862};
    //'for' statement at aliases.ceylon (41:4-43:4)
    var it$863 = $$$cl1.Range((1),$$miMatrix.gridSize$861,{Element:{t:$$$cl1.Integer}}).iterator();
    var i$864;while ((i$864=it$863.next())!==$$$cl1.getFinished()){
        $$miMatrix.sb$862.append($$$cl1.Comprehension(function(){
            //Comprehension at aliases.ceylon (42:20-42:43)
            var it$865=$$$cl1.Range((1),$$miMatrix.gridSize$861,{Element:{t:$$$cl1.Integer}}).iterator();
            var j$866=$$$cl1.getFinished();
            var next$j$866=function(){return j$866=it$865.next();}
            next$j$866();
            return function(){
                if(j$866!==$$$cl1.getFinished()){
                    var j$866$867=j$866;
                    var tmpvar$868=j$866$867;
                    next$j$866();
                    return tmpvar$868;
                }
                return $$$cl1.getFinished();
            }
        },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).sequence);
    }
    
    //AttributeDecl grid at aliases.ceylon (44:4-44:24)
    $$miMatrix.$prop$getGrid$869={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl1.Integer}}},$cont:MiMatrix,d:['misc','MiMatrix','$at','grid']};}};
    var g$870;
    if($$$cl1.nonempty((g$870=$$miMatrix.sb$862.sequence))){
        var grid$869=g$870;
        $$$cl1.defineAttr($$miMatrix,'grid$869',function(){return grid$869;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','grid']};});
    }else {
        var grid$869=$$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}});
        $$$cl1.defineAttr($$miMatrix,'grid$869',function(){return grid$869;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','grid']};});
    }
    
    //AttributeDecl string at aliases.ceylon (51:4-51:45)
    $$miMatrix.string$871_=$$miMatrix.grid$869.string;
    $$miMatrix.$prop$getString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','string']};}};
    $$miMatrix.$prop$getString.get=function(){return string};
    
    //AttributeDecl hash at aliases.ceylon (52:4-52:42)
    $$miMatrix.hash$872_=$$miMatrix.grid$869.hash;
    $$miMatrix.$prop$getHash={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','hash']};}};
    $$miMatrix.$prop$getHash.get=function(){return hash};
    $$miMatrix.span=function span(from$873,to$874){return (opt$875=$$miMatrix.grid$869,$$$cl1.JsCallable(opt$875,opt$875!==null?opt$875.span:null))(from$873,to$874);};
    var opt$875;
    $$miMatrix.segment=function segment(from$876,length$877){return (opt$878=$$miMatrix.grid$869,$$$cl1.JsCallable(opt$878,opt$878!==null?opt$878.segment:null))(from$876,length$877);};
    var opt$878;
    
    //AttributeDecl reversed at aliases.ceylon (56:4-56:58)
    $$miMatrix.reversed$879_=$$miMatrix.grid$869.reversed;
    $$miMatrix.$prop$getReversed={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','reversed']};}};
    $$miMatrix.$prop$getReversed.get=function(){return reversed};
    
    //AttributeDecl lastIndex at aliases.ceylon (57:4-57:52)
    $$miMatrix.lastIndex$880_=$$miMatrix.grid$869.lastIndex;
    $$miMatrix.$prop$getLastIndex={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','lastIndex']};}};
    $$miMatrix.$prop$getLastIndex.get=function(){return lastIndex};
    
    //AttributeDecl rest at aliases.ceylon (59:4-59:47)
    $$miMatrix.rest$881_=$$miMatrix.grid$869.rest;
    $$miMatrix.$prop$getRest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','rest']};}};
    $$miMatrix.$prop$getRest.get=function(){return rest};
    
    //AttributeDecl first at aliases.ceylon (60:4-60:47)
    $$miMatrix.first$882_=$$miMatrix.grid$869.first;
    $$miMatrix.$prop$getFirst={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','first']};}};
    $$miMatrix.$prop$getFirst.get=function(){return first};
    
    //AttributeDecl clone at aliases.ceylon (61:4-61:40)
    $$miMatrix.$prop$getClone={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:MiMatrix},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','clone']};}};
    $$miMatrix.$prop$getClone.get=function(){return clone};
    
    //AttributeDecl size at aliases.ceylon (62:4-62:43)
    $$miMatrix.$prop$getSize={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','size']};}};
    $$miMatrix.$prop$getSize.get=function(){return size};
    return $$miMatrix;
}
MiMatrix.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'gridSize',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],satisfies:[{t:Matrix,a:{Cell:{t:$$$cl1.Integer}}}],d:['misc','MiMatrix']};};
function $init$MiMatrix(){
    if (MiMatrix.$$===undefined){
        $$$cl1.initTypeProto(MiMatrix,'misc::MiMatrix',$$$cl1.Basic,$$$cl1.Sequence);
        (function($$miMatrix){
            
            //AttributeDecl sb at aliases.ceylon (40:4-40:44)
            $$$cl1.defineAttr($$miMatrix,'sb$862',function(){return this.sb$862_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.SequenceBuilder,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$cont:MiMatrix,d:['misc','MiMatrix','$at','sb']};});
            
            //AttributeDecl grid at aliases.ceylon (44:4-44:24)
            
            //MethodDef iterator at aliases.ceylon (50:4-50:76)
            $$miMatrix.iterator=function iterator(){
                var $$miMatrix=this;
                return $$miMatrix.grid$869.iterator();
            };$$miMatrix.iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$ps:[],$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$m','iterator']};};
            
            //AttributeDecl string at aliases.ceylon (51:4-51:45)
            $$$cl1.defineAttr($$miMatrix,'string',function(){return this.string$871_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','string']};});
            
            //AttributeDecl hash at aliases.ceylon (52:4-52:42)
            $$$cl1.defineAttr($$miMatrix,'hash',function(){return this.hash$872_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','hash']};});
            
            //MethodDecl equals at aliases.ceylon (53:4-53:68)
            $$miMatrix.equals=function (other$883){
                var $$miMatrix=this;
                return $$miMatrix.grid$869.equals(other$883);
            };
            $$miMatrix.equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$m','equals']};};
            
            //AttributeDecl reversed at aliases.ceylon (56:4-56:58)
            $$$cl1.defineAttr($$miMatrix,'reversed',function(){return this.reversed$879_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:Matrix,a:{Cell:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','reversed']};});
            
            //AttributeDecl lastIndex at aliases.ceylon (57:4-57:52)
            $$$cl1.defineAttr($$miMatrix,'lastIndex',function(){return this.lastIndex$880_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','lastIndex']};});
            
            //MethodDecl get at aliases.ceylon (58:4-58:55)
            $$miMatrix.$get=function (i$884){
                var $$miMatrix=this;
                return $$miMatrix.grid$869.$get(i$884);
            };
            $$miMatrix.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}]},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$m','get']};};
            
            //AttributeDecl rest at aliases.ceylon (59:4-59:47)
            $$$cl1.defineAttr($$miMatrix,'rest',function(){return this.rest$881_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','rest']};});
            
            //AttributeDecl first at aliases.ceylon (60:4-60:47)
            $$$cl1.defineAttr($$miMatrix,'first',function(){return this.first$882_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','first']};});
            
            //AttributeDecl clone at aliases.ceylon (61:4-61:40)
            $$$cl1.defineAttr($$miMatrix,'clone',function(){
                var $$miMatrix=this;
                return $$miMatrix;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:MiMatrix},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','clone']};});
            
            //AttributeDecl size at aliases.ceylon (62:4-62:43)
            $$$cl1.defineAttr($$miMatrix,'size',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$869.size;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','size']};});
            
            //MethodDecl contains at aliases.ceylon (63:4-63:72)
            $$miMatrix.contains=function (other$885){
                var $$miMatrix=this;
                return $$miMatrix.grid$869.contains(other$885);
            };
            $$miMatrix.contains.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'other',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$m','contains']};};
            $$$cl1.defineAttr($$miMatrix,'last',function(){
                var $$miMatrix=this;
                return $$miMatrix.grid$869.last;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}},$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$at','last']};});
            
            //MethodDecl spanTo at aliases.ceylon (65:4-66:41)
            $$miMatrix.spanTo=function (to$886){
                var $$miMatrix=this;
                return (opt$887=(to$886.compare((0)).equals($$$cl1.getSmaller())?$$$cl1.getEmpty():null),opt$887!==null?opt$887:$$miMatrix.span((0),to$886));
            };
            $$miMatrix.spanTo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$m','spanTo']};};
            
            //MethodDecl spanFrom at aliases.ceylon (68:4-69:28)
            $$miMatrix.spanFrom=function (from$888){
                var $$miMatrix=this;
                return $$miMatrix.span(from$888,$$miMatrix.size);
            };
            $$miMatrix.spanFrom.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Sequence,a:{Element:{t:$$$cl1.Integer}}}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:MiMatrix,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','MiMatrix','$m','spanFrom']};};
            $$$cl1.defineAttr($$miMatrix,'gridSize$861',function(){return this.gridSize$861_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:MiMatrix,d:['misc','MiMatrix','$at','gridSize']};});
        })(MiMatrix.$$.prototype);
    }
    return MiMatrix;
}
exports.$init$MiMatrix=$init$MiMatrix;
$init$MiMatrix();
var opt$887;

//MethodDef issue225_1 at aliases.ceylon (72:0-72:47)
function issue225_1(content$889){
}
exports.issue225_1=issue225_1;
issue225_1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'content',$mt:'prm',$t:{t:'u', l:[{t:$$$cl1.Integer},{t:$$$cl1.String}]},$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['misc','issue225_1']};};

//TypeAliasDecl Issue225Alias at aliases.ceylon (73:0-73:44)
function Issue225Alias(){var tmpvar$890={t:'u', l:[{t:$$$cl1.Integer},{t:$$$cl1.String}]};tmpvar$890.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl1.shared()];},d:['misc','Issue225Alias']};};return tmpvar$890;}
exports.Issue225Alias=Issue225Alias;

//MethodDef issue225_2 at aliases.ceylon (74:0-74:46)
function issue225_2(content$891){
}
exports.issue225_2=issue225_2;
issue225_2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'content',$mt:'prm',$t:Issue225Alias(),$an:function(){return[];}}],$an:function(){return[$$$cl1.shared()];},d:['misc','issue225_2']};};

//MethodDef testAliasing at aliases.ceylon (76:0-94:0)
function testAliasing(){
    $$$cl1.print($$$cl1.String("testing type aliases",20));
    $$$c2.check(AliasingSubclass().aliasingSubclass(),$$$cl1.String("Aliased member class",20));
    
    //ClassDecl InnerSubalias at aliases.ceylon (79:4-79:47)
    function InnerSubalias$892($$innerSubalias$892){return AliasingSubclass($$innerSubalias$892);}
    InnerSubalias$892.$$=AliasingSubclass.$$;
    InnerSubalias$892.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:AliasingSubclass},$ps:[],d:['misc','testAliasing','$c','InnerSubalias']};};
    $$$c2.check(InnerSubalias$892().aliasingSubclass(),$$$cl1.String("Aliased top-level class",23));
    
    //InterfaceDecl AliasedIface2 at aliases.ceylon (81:4-81:58)
    function AliasedIface2$893($$aliasedIface2$893){AliasingIface$AliasingClass($$aliasedIface2$893);}
    AliasedIface2$893.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['misc','testAliasing','$i','AliasedIface2']};};
    
    //MethodDef use at aliases.ceylon (82:4-82:65)
    function use$894(aif$895){
        return aif$895.aliasingIface();
    };use$894.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'aif',$mt:'prm',$t:{t:AliasedIface2$893},$an:function(){return[];}}],d:['misc','testAliasing','$m','use']};};
    $$$c2.check(use$894(AliasingSub2().iface),$$$cl1.String("Aliased member interface",24));
    
    //AttributeDecl xxxxx at aliases.ceylon (84:4-84:24)
    var xxxxx$896=(5);
    $$$c2.check($$$cl1.isOfType(xxxxx$896,{t:$$$cl1.Integer}),$$$cl1.String("Type alias",10));
    $$$c2.check(Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl1.Integer}]}]},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl1.Integer}]},{t:'T', l:[{t:$$$cl1.Integer}]}]},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}}).first,{T:{t:$$$cl1.Integer}}).string.equals($$$cl1.String("[[1]]",5)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("class alias ",12),Listleton($$$cl1.Tuple($$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.Tuple($$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl1.Integer}]}]},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}}),{Rest:{t:'T', l:[{t:'T', l:[{t:$$$cl1.Integer}]},{t:'T', l:[{t:$$$cl1.Integer}]}]},First:{t:'T', l:[{t:$$$cl1.Integer}]},Element:{t:'T', l:[{t:$$$cl1.Integer}]}}).first,{T:{t:$$$cl1.Integer}}).string,$$$cl1.String(" instead of [ [ 1 ] ]",21)]).string);
    $$$c2.check(MiMatrix((2)).string.equals($$$cl1.String("[[1, 2], [1, 2]]",16)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("interface alias ",16),MiMatrix((2)).string,$$$cl1.String(" instead of [[1, 2], [1, 2]]",28)]).string);
    
    //AttributeDecl xxxxx1 at aliases.ceylon (88:4-88:21)
    var xxxxx1$897=(6);
    
    //AttributeDecl xxxxx2 at aliases.ceylon (89:4-89:26)
    var xxxxx2$898=$$$cl1.String("XXXX",4);
    $$$c2.check($$$cl1.isOfType(xxxxx1$897,{t:'u', l:[{t:$$$cl1.String},{t:$$$cl1.Integer}]}),$$$cl1.String("is String|Integer",17));
    $$$c2.check($$$cl1.isOfType(xxxxx2$898,{t:'i', l:[{t:$$$cl1.String},{t:$$$cl1.List,a:{Element:{t:$$$cl1.Anything}}}]}),$$$cl1.String("is String&List",14));
    
    //MethodDecl cualquiera at aliases.ceylon (92:4-92:51)
    var cualquiera$899=function (bits$900){
        if(bits$900===undefined){bits$900=$$$cl1.getEmpty();}
        return $$$cl1.any(bits$900);
    };
    cualquiera$899.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'bits',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Boolean}}},$an:function(){return[];}}],d:['misc','testAliasing','$m','cualquiera']};};
    $$$c2.check(cualquiera$899([true,true,true].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.true$901}})),$$$cl1.String("seq arg method alias",20));
};testAliasing.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','testAliasing']};};

//MethodDef m1 at exceptions.ceylon (1:0-3:0)
function m1(){
    throw $$$cl1.wrapexc($$$cl1.Exception($$$cl1.String("Catch me!",9)),'2:2-2:30','misc/exceptions.ceylon');
};m1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','m1']};};

//MethodDef m2 at exceptions.ceylon (5:0-7:0)
function m2(){
    m1();
};m2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','m2']};};

//MethodDef m3 at exceptions.ceylon (9:0-11:0)
function m3(){
    m2();
};m3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','m3']};};

//MethodDef testStackTrace at exceptions.ceylon (13:0-21:0)
function testStackTrace(){
    try{
        $$$cl1.print($$$cl1.String("Coming up, a strack trace...",28));
        m3();
    }catch(ex$902){
        if (ex$902.getT$name === undefined) ex$902=$$$cl1.NativeException(ex$902);
        if($$$cl1.isOfType(ex$902,{t:$$$cl1.Exception})){
            ex$902.printStackTrace();
        }
        else{throw ex$902}
    }
    $$$cl1.print($$$cl1.String("You should have seen a stack trace",34));
};testStackTrace.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','testStackTrace']};};

//ClassDef LateTestChild at late_support.ceylon (3:0-5:0)
function LateTestChild($$lateTestChild){
    $init$LateTestChild();
    if ($$lateTestChild===undefined)$$lateTestChild=new LateTestChild.$$;
    
    //AttributeDecl parent at late_support.ceylon (4:4-4:37)
    $$lateTestChild.parent$903_=undefined;
    $$lateTestChild.$prop$getParent={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:LateTestParent},$cont:LateTestChild,$an:function(){return[$$$cl1.shared(),$$$cl1.late()];},d:['misc','LateTestChild','$at','parent']};}};
    $$lateTestChild.$prop$getParent.get=function(){return parent};
    return $$lateTestChild;
}
LateTestChild.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['misc','LateTestChild']};};
function $init$LateTestChild(){
    if (LateTestChild.$$===undefined){
        $$$cl1.initTypeProto(LateTestChild,'misc::LateTestChild',$$$cl1.Basic);
        (function($$lateTestChild){
            
            //AttributeDecl parent at late_support.ceylon (4:4-4:37)
            $$$cl1.defineAttr($$lateTestChild,'parent',function(){if (this.parent$903_===undefined)throw $$$cl1.InitializationException($$$cl1.String('Attempt to read unitialized attribute «parent»'));return this.parent$903_;},function(parent$904){if(this.parent$903_!==undefined)throw $$$cl1.InitializationException($$$cl1.String('Attempt to reassign immutable attribute «parent»'));return this.parent$903_=parent$904;},function(){return{mod:$$METAMODEL$$,$t:{t:LateTestParent},$cont:LateTestChild,$an:function(){return[$$$cl1.shared(),$$$cl1.late()];},d:['misc','LateTestChild','$at','parent']};});
        })(LateTestChild.$$.prototype);
    }
    return LateTestChild;
}
exports.$init$LateTestChild=$init$LateTestChild;
$init$LateTestChild();

//ClassDef LateTestParent at late_support.ceylon (7:0-12:0)
function LateTestParent(children, $$lateTestParent){
    $init$LateTestParent();
    if ($$lateTestParent===undefined)$$lateTestParent=new LateTestParent.$$;
    if(children===undefined){children=$$$cl1.getEmpty();}
    
    //AttributeDecl children at late_support.ceylon (8:4-8:34)
    $$lateTestParent.children$905_=children;
    $$lateTestParent.$prop$getChildren={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:LateTestChild}}},$cont:LateTestParent,$an:function(){return[$$$cl1.shared()];},d:['misc','LateTestParent','$at','children']};}};
    $$lateTestParent.$prop$getChildren.get=function(){return children};
    //'for' statement at late_support.ceylon (9:4-11:4)
    var it$906 = $$lateTestParent.children.iterator();
    var child$907;while ((child$907=it$906.next())!==$$$cl1.getFinished()){
        (child$907.parent=$$lateTestParent);
    }
    return $$lateTestParent;
}
LateTestParent.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'children',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:LateTestChild}}},$an:function(){return[$$$cl1.shared()];}}],d:['misc','LateTestParent']};};
function $init$LateTestParent(){
    if (LateTestParent.$$===undefined){
        $$$cl1.initTypeProto(LateTestParent,'misc::LateTestParent',$$$cl1.Basic);
        (function($$lateTestParent){
            
            //AttributeDecl children at late_support.ceylon (8:4-8:34)
            $$$cl1.defineAttr($$lateTestParent,'children',function(){return this.children$905_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Sequential,a:{Element:{t:LateTestChild}}},$cont:LateTestParent,$an:function(){return[$$$cl1.shared()];},d:['misc','LateTestParent','$at','children']};});
        })(LateTestParent.$$.prototype);
    }
    return LateTestParent;
}
exports.$init$LateTestParent=$init$LateTestParent;
$init$LateTestParent();

//MethodDef testLate at late_support.ceylon (14:0-33:0)
function testLate(){
    
    //AttributeDecl kids at late_support.ceylon (15:4-15:51)
    var kids$908=[LateTestChild(),LateTestChild()].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:LateTestChild}});
    LateTestParent(kids$908);
    try{
        LateTestParent(kids$908);
        $$$c2.fail($$$cl1.String("reassigning to late attribute should fail",41));
    }catch(ex$909){
        if (ex$909.getT$name === undefined) ex$909=$$$cl1.NativeException(ex$909);
        if($$$cl1.isOfType(ex$909,{t:$$$cl1.InitializationException})){
            $$$c2.check(true);
        }
        else if($$$cl1.isOfType(ex$909,{t:$$$cl1.Exception})){
            $$$c2.fail($$$cl1.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$909}
    }
    try{
        $$$cl1.print(LateTestChild().parent);
        $$$c2.fail($$$cl1.String("Reading uninitialized late attribute should fail",48));
    }catch(ex$910){
        if (ex$910.getT$name === undefined) ex$910=$$$cl1.NativeException(ex$910);
        if($$$cl1.isOfType(ex$910,{t:$$$cl1.InitializationException})){
            $$$c2.check(true);
        }
        else if($$$cl1.isOfType(ex$910,{t:$$$cl1.Exception})){
            $$$c2.fail($$$cl1.String("wrong exception thrown for late attribute",41));
        }
        else{throw ex$910}
    }
};testLate.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','testLate']};};

//InterfaceDef X at misc.ceylon (1:0-5:0)
function X($$x){
}
X.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl1.shared()];},d:['misc','X']};};
exports.X=X;
function $init$X(){
    if (X.$$===undefined){
        $$$cl1.initTypeProtoI(X,'misc::X');
        (function($$x){
            
            //MethodDef helloWorld at misc.ceylon (2:4-4:4)
            $$x.helloWorld=function helloWorld(){
                var $$x=this;
                $$$cl1.print($$$cl1.String("hello world",11));
            };$$x.helloWorld.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:X,$an:function(){return[$$$cl1.shared()];},d:['misc','X','$m','helloWorld']};};
        })(X.$$.prototype);
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
    $$foo.name$911_=name;
    $$foo.$prop$getName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Foo,$an:function(){return[$$$cl1.shared()];},d:['misc','Foo','$at','name']};}};
    $$foo.$prop$getName.get=function(){return name};
    
    //AttributeDecl counter at misc.ceylon (9:4-9:28)
    $$foo.counter$912_=(0);
    $$foo.$prop$getCounter$912={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Foo,$an:function(){return[$$$cl1.variable()];},d:['misc','Foo','$at','counter']};}};
    $$foo.$prop$getCounter$912.get=function(){return counter$912};
    
    //AttributeDecl string at misc.ceylon (15:4-15:57)
    $$foo.string$913_=$$$cl1.StringBuilder().appendAll([$$$cl1.String("Foo(",4),$$foo.name.string,$$$cl1.String(")",1)]).string;
    $$foo.$prop$getString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Foo,$an:function(){return[$$$cl1.shared(),$$$cl1.$default(),$$$cl1.actual()];},d:['misc','Foo','$at','string']};}};
    $$foo.$prop$getString.get=function(){return string};
    $$foo.inc();
    return $$foo;
}
Foo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[$$$cl1.shared()];}}],$an:function(){return[$$$cl1.shared()];},d:['misc','Foo']};};
exports.Foo=Foo;
function $init$Foo(){
    if (Foo.$$===undefined){
        $$$cl1.initTypeProto(Foo,'misc::Foo',$$$cl1.Basic);
        (function($$foo){
            
            //AttributeDecl name at misc.ceylon (8:4-8:22)
            $$$cl1.defineAttr($$foo,'name',function(){return this.name$911_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Foo,$an:function(){return[$$$cl1.shared()];},d:['misc','Foo','$at','name']};});
            
            //AttributeDecl counter at misc.ceylon (9:4-9:28)
            $$$cl1.defineAttr($$foo,'counter$912',function(){return this.counter$912_;},function(counter$914){return this.counter$912_=counter$914;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Foo,$an:function(){return[$$$cl1.variable()];},d:['misc','Foo','$at','counter']};});
            
            //AttributeGetterDef count at misc.ceylon (10:4-10:43)
            $$$cl1.defineAttr($$foo,'count',function(){
                var $$foo=this;
                return $$foo.counter$912;
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Foo,$an:function(){return[$$$cl1.shared()];},d:['misc','Foo','$at','count']};});
            //MethodDef inc at misc.ceylon (11:4-11:43)
            $$foo.inc=function inc(){
                var $$foo=this;
                $$foo.counter$912=$$foo.counter$912.plus((1));
            };$$foo.inc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Foo,$an:function(){return[$$$cl1.shared()];},d:['misc','Foo','$m','inc']};};
            
            //MethodDef printName at misc.ceylon (12:4-14:4)
            $$foo.printName=function printName(){
                var $$foo=this;
                $$$cl1.print($$$cl1.String("foo name = ",11).plus($$foo.name));
            };$$foo.printName.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Foo,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['misc','Foo','$m','printName']};};
            
            //AttributeDecl string at misc.ceylon (15:4-15:57)
            $$$cl1.defineAttr($$foo,'string',function(){return this.string$913_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:Foo,$an:function(){return[$$$cl1.shared(),$$$cl1.$default(),$$$cl1.actual()];},d:['misc','Foo','$at','string']};});
        })(Foo.$$.prototype);
    }
    return Foo;
}
exports.$init$Foo=$init$Foo;
$init$Foo();

//ClassDef Bar at misc.ceylon (19:0-34:0)
function Bar($$bar){
    $init$Bar();
    if ($$bar===undefined)$$bar=new Bar.$$;
    Foo($$$cl1.String("Hello",5),$$bar);
    X($$bar);
    return $$bar;
}
Bar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Foo},$ps:[],satisfies:[{t:X}],$an:function(){return[$$$cl1.shared()];},d:['misc','Bar']};};
exports.Bar=Bar;
function $init$Bar(){
    if (Bar.$$===undefined){
        $$$cl1.initTypeProto(Bar,'misc::Bar',$init$Foo(),$init$X());
        (function($$bar){
            
            //MethodDef printName at misc.ceylon (20:4-24:4)
            $$bar.printName=function printName(){
                var $$bar=this;
                $$$cl1.print($$$cl1.String("bar name = ",11).plus($$bar.name));
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
                $$bar.getT$all()['misc::Foo'].$$.prototype.printName.call(this);
            };$$bar.printName.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Bar,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','Bar','$m','printName']};};
            
            //ClassDef Inner at misc.ceylon (25:4-31:4)
            function Inner$Bar($$inner$Bar){
                $init$Inner$Bar();
                if ($$inner$Bar===undefined)$$inner$Bar=new this.Inner$Bar.$$;
                $$inner$Bar.$$outer=this;
                $$$cl1.print($$$cl1.String("creating inner class of :",25).plus($$inner$Bar.$$outer.name));
                return $$inner$Bar;
            }
            Inner$Bar.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:Bar,$an:function(){return[$$$cl1.shared()];},d:['misc','Bar','$c','Inner']};};
            function $init$Inner$Bar(){
                if (Inner$Bar.$$===undefined){
                    $$$cl1.initTypeProto(Inner$Bar,'misc::Bar.Inner',$$$cl1.Basic);
                    Bar.Inner$Bar=Inner$Bar;
                    (function($$inner$Bar){
                        
                        //MethodDef incOuter at misc.ceylon (28:8-30:8)
                        $$inner$Bar.incOuter=function incOuter(){
                            var $$inner$Bar=this;
                            $$inner$Bar.$$outer.inc();
                        };$$inner$Bar.incOuter.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:Inner$Bar,$an:function(){return[$$$cl1.shared()];},d:['misc','Bar','$c','Inner','$m','incOuter']};};
                    })(Inner$Bar.$$.prototype);
                }
                return Inner$Bar;
            }
            $$bar.$init$Inner$Bar=$init$Inner$Bar;
            $init$Inner$Bar();
            $$bar.Inner$Bar=Inner$Bar;
        })(Bar.$$.prototype);
    }
    return Bar;
}
exports.$init$Bar=$init$Bar;
$init$Bar();

//MethodDef printBoth at misc.ceylon (36:0-38:0)
function printBoth(x$915,y$916){
    $$$cl1.print(x$915.plus($$$cl1.String(", ",2)).plus(y$916));
};printBoth.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}},{$nm:'y',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['misc','printBoth']};};

//MethodDef doIt at misc.ceylon (40:0-42:0)
function doIt(f$917){
    f$917();
    f$917();
};doIt.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl1.Anything},$an:function(){return[];}}],d:['misc','doIt']};};

//ObjectDef foob at misc.ceylon (44:0-46:0)
function foob$918(){
    var $$foob=new foob$918.$$;
    
    //AttributeDecl name at misc.ceylon (45:4-45:30)
    $$foob.name$919_=$$$cl1.String("Gavin",5);
    $$foob.$prop$getName.get=function(){return name};
    return $$foob;
};foob$918.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},d:['misc','foob']};};
function $init$foob$918(){
    if (foob$918.$$===undefined){
        $$$cl1.initTypeProto(foob$918,'misc::foob',$$$cl1.Basic);
    }
    return foob$918;
}
exports.$init$foob$918=$init$foob$918;
$init$foob$918();
(function($$foob){
    
    //AttributeDecl name at misc.ceylon (45:4-45:30)
    $$$cl1.defineAttr($$foob,'name',function(){return this.name$919_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:foob$918,$an:function(){return[$$$cl1.shared()];},d:['misc','foob','$at','name']};});
})(foob$918.$$.prototype);
var foob$920;
function getFoob(){
    if (foob$920===undefined){foob$920=$init$foob$918()();foob$920.$$metamodel$$=getFoob.$$metamodel$$;}
    return foob$920;
}
getFoob.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:foob$918},d:['misc','foob']};};
$prop$getFoob={get:getFoob,$$metamodel$$:getFoob.$$metamodel$$};
exports.$prop$getFoob=$prop$getFoob;

//MethodDef printAll at misc.ceylon (48:0-48:32)
function printAll(strings$921){
    if(strings$921===undefined){strings$921=$$$cl1.getEmpty();}
};printAll.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'strings',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.String}}},$an:function(){return[];}}],d:['misc','printAll']};};

//ClassDecl F at misc.ceylon (50:0-50:33)
function F(name$922, $$f){return Foo(name$922,$$f);}
F.$$=Foo.$$;
F.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:Foo},$ps:[{$nm:'name',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],d:['misc','F']};};

//MethodDef var at misc.ceylon (52:0-52:33)
function $var(){
    return (5);
}
exports.$var=$var;
$var.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['misc','var']};};

//AttributeDecl container249 at misc.ceylon (55:0-55:41)
var container249$923;function $valinit$container249$923(){if (container249$923===undefined)container249$923=$$$cl1.Tuple(getObject249().$int,$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});return container249$923;};$valinit$container249$923();
function getContainer249(){return $valinit$container249$923();}
var $prop$getContainer249={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}},d:['misc','container249']};}};
exports.$prop$getContainer249=$prop$getContainer249;
$prop$getContainer249.get=function(){return container249$923};

//ObjectDef object249 at misc.ceylon (56:0-58:0)
function object249$924(){
    var $$object249=new object249$924.$$;
    
    //AttributeDecl int at misc.ceylon (57:2-57:24)
    $$object249.int$925_=(1);
    $$object249.$prop$getInt={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:object249$924,$an:function(){return[$$$cl1.shared()];},d:['misc','object249','$at','int']};}};
    $$object249.$prop$getInt.get=function(){return $int};
    return $$object249;
};object249$924.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$an:function(){return[$$$cl1.shared()];},d:['misc','object249']};};
function $init$object249$924(){
    if (object249$924.$$===undefined){
        $$$cl1.initTypeProto(object249$924,'misc::object249',$$$cl1.Basic);
    }
    return object249$924;
}
exports.$init$object249$924=$init$object249$924;
$init$object249$924();
(function($$object249){
    
    //AttributeDecl int at misc.ceylon (57:2-57:24)
    $$$cl1.defineAttr($$object249,'$int',function(){return this.int$925_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:object249$924,$an:function(){return[$$$cl1.shared()];},d:['misc','object249','$at','int']};});
})(object249$924.$$.prototype);
var object249$926;
function getObject249(){
    if (object249$926===undefined){object249$926=$init$object249$924()();object249$926.$$metamodel$$=getObject249.$$metamodel$$;}
    return object249$926;
}
exports.getObject249=getObject249;
getObject249.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:object249$924},$an:function(){return[$$$cl1.shared()];},d:['misc','object249']};};
$prop$getObject249={get:getObject249,$$metamodel$$:getObject249.$$metamodel$$};
exports.$prop$getObject249=$prop$getObject249;
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[],
'members/0.1':[]
};};

//ClassDef TestObjects at objects.ceylon (3:0-17:0)
function TestObjects(a$927, b$928, c$929, $$testObjects){
    $init$TestObjects();
    if ($$testObjects===undefined)$$testObjects=new TestObjects.$$;
    $$testObjects.$$targs$$={Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}};
    $$testObjects.a$927_=a$927;
    $$testObjects.b$928_=b$928;
    $$testObjects.c$929_=c$929;
    $$$cl1.Iterable({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}},$$testObjects);
    $$$cl1.add_type_arg($$testObjects,'Absent',{t:$$$cl1.Null});
    $$$cl1.add_type_arg($$testObjects,'Element',{t:$$$cl1.Integer});
    return $$testObjects;
}
TestObjects.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'c',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],satisfies:[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}],d:['misc','TestObjects']};};
function $init$TestObjects(){
    if (TestObjects.$$===undefined){
        $$$cl1.initTypeProto(TestObjects,'misc::TestObjects',$$$cl1.Basic,$$$cl1.Iterable);
        (function($$testObjects){
            
            //MethodDef iterator at objects.ceylon (4:2-16:2)
            $$testObjects.iterator=function iterator(){
                var $$testObjects=this;
                
                //ObjectDef iter at objects.ceylon (5:4-14:4)
                function iter$930($$targs$$){
                    var $$iter$930=new iter$930.$$;
                    $$iter$930.$$targs$$=$$targs$$;
                    $$$cl1.Iterator({Element:{t:$$$cl1.Integer}},$$iter$930);
                    $$$cl1.add_type_arg($$iter$930,'Element',{t:$$$cl1.Integer});
                    
                    //AttributeDecl index at objects.ceylon (6:6-6:30)
                    $$iter$930.index$931_=(0);
                    $$iter$930.$prop$getIndex$931={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:iter$930,$an:function(){return[$$$cl1.variable()];},d:['misc','TestObjects','$m','iterator','$o','iter','$at','index']};}};
                    $$iter$930.$prop$getIndex$931.get=function(){return index$931};
                    return $$iter$930;
                };iter$930.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},satisfies:[{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}}],d:['misc','TestObjects','$m','iterator','$o','iter']};};
                function $init$iter$930(){
                    if (iter$930.$$===undefined){
                        $$$cl1.initTypeProto(iter$930,'misc::TestObjects.iterator.iter',$$$cl1.Basic,$$$cl1.Iterator);
                    }
                    return iter$930;
                }
                $init$iter$930();
                (function($$iter$930){
                    
                    //AttributeDecl index at objects.ceylon (6:6-6:30)
                    $$$cl1.defineAttr($$iter$930,'index$931',function(){return this.index$931_;},function(index$932){return this.index$931_=index$932;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:iter$930,$an:function(){return[$$$cl1.variable()];},d:['misc','TestObjects','$m','iterator','$o','iter','$at','index']};});
                    
                    //MethodDef next at objects.ceylon (7:6-13:6)
                    $$iter$930.next=function next(){
                        var $$iter$930=this;
                        (oldindex$933=$$iter$930.index$931,$$iter$930.index$931=oldindex$933.successor,oldindex$933);
                        var oldindex$933;
                        if($$iter$930.index$931.equals((1))){
                            return $$testObjects.a$927;
                        }else {
                            if($$iter$930.index$931.equals((2))){
                                return $$testObjects.b$928;
                            }else {
                                if($$iter$930.index$931.equals((3))){
                                    return $$testObjects.c$929;
                                }
                            }
                        }
                        return $$$cl1.getFinished();
                    };$$iter$930.next.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Integer},{t:$$$cl1.Finished}]},$ps:[],$cont:iter$930,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','TestObjects','$m','iterator','$o','iter','$m','next']};};
                })(iter$930.$$.prototype);
                var iter$934;
                function getIter$934(){
                    if (iter$934===undefined){iter$934=$init$iter$930()({Element:{t:$$$cl1.Integer}});iter$934.$$metamodel$$=getIter$934.$$metamodel$$;}
                    return iter$934;
                }
                getIter$934.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:iter$930},d:['misc','TestObjects','$m','iterator','$o','iter']};};
                $prop$getIter$934={get:getIter$934,$$metamodel$$:getIter$934.$$metamodel$$};
                return getIter$934();
            };$$testObjects.iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}},$ps:[],$cont:TestObjects,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['misc','TestObjects','$m','iterator']};};
            $$$cl1.defineAttr($$testObjects,'a$927',function(){return this.a$927_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:TestObjects,d:['misc','TestObjects','$at','a']};});
            $$$cl1.defineAttr($$testObjects,'b$928',function(){return this.b$928_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:TestObjects,d:['misc','TestObjects','$at','b']};});
            $$$cl1.defineAttr($$testObjects,'c$929',function(){return this.c$929_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:TestObjects,d:['misc','TestObjects','$at','c']};});
        })(TestObjects.$$.prototype);
    }
    return TestObjects;
}
exports.$init$TestObjects=$init$TestObjects;
$init$TestObjects();

//MethodDef test_objects at objects.ceylon (19:0-42:0)
function test_objects(){
    $$$cl1.print($$$cl1.String("testing objects",15));
    
    //AttributeDecl t1 at objects.ceylon (21:2-21:42)
    var t1$935=TestObjects((1),(2),(3)).iterator();
    
    //AttributeDecl t2 at objects.ceylon (22:2-22:42)
    var t2$936=TestObjects((1),(2),(3)).iterator();
    var i$937;
    if($$$cl1.isOfType((i$937=t1$935.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$937.equals((1)),$$$cl1.String("objects 1",9));
    }
    var i$938;
    if($$$cl1.isOfType((i$938=t1$935.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$938.equals((2)),$$$cl1.String("objects 2",9));
    }
    var i$939;
    if($$$cl1.isOfType((i$939=t2$936.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$939.equals((1)),$$$cl1.String("objects 3",9));
    }
    var i$940;
    if($$$cl1.isOfType((i$940=t1$935.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$940.equals((3)),$$$cl1.String("objects 4",9));
    }
    $$$c2.check($$$cl1.isOfType(t1$935.next(),{t:$$$cl1.Finished}),$$$cl1.String("objects 5",9));
    var i$941;
    if($$$cl1.isOfType((i$941=t2$936.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$941.equals((2)),$$$cl1.String("objects 6",9));
    }
    var i$942;
    if($$$cl1.isOfType((i$942=t2$936.next()),{t:$$$cl1.Integer})){
        $$$c2.check(i$942.equals((3)),$$$cl1.String("objects 7",9));
    }
};test_objects.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','test_objects']};};

//InterfaceDef Top1 at reifiedRuntime.ceylon (3:0-3:22)
function Top1($$top1){
}
Top1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$an:function(){return[$$$cl1.shared()];},d:['misc','Top1']};};
exports.Top1=Top1;
function $init$Top1(){
    if (Top1.$$===undefined){
        $$$cl1.initTypeProtoI(Top1,'misc::Top1');
    }
    return Top1;
}
exports.$init$Top1=$init$Top1;
$init$Top1();

//InterfaceDef Middle1 at reifiedRuntime.ceylon (4:0-4:40)
function Middle1($$middle1){
    Top1($$middle1);
}
Middle1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:Top1}],$an:function(){return[$$$cl1.shared()];},d:['misc','Middle1']};};
exports.Middle1=Middle1;
function $init$Middle1(){
    if (Middle1.$$===undefined){
        $$$cl1.initTypeProtoI(Middle1,'misc::Middle1',$init$Top1());
    }
    return Middle1;
}
exports.$init$Middle1=$init$Middle1;
$init$Middle1();

//InterfaceDef Bottom1 at reifiedRuntime.ceylon (5:0-5:43)
function Bottom1($$bottom1){
    Middle1($$bottom1);
}
Bottom1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:Middle1}],$an:function(){return[$$$cl1.shared()];},d:['misc','Bottom1']};};
exports.Bottom1=Bottom1;
function $init$Bottom1(){
    if (Bottom1.$$===undefined){
        $$$cl1.initTypeProtoI(Bottom1,'misc::Bottom1',$init$Middle1());
    }
    return Bottom1;
}
exports.$init$Bottom1=$init$Bottom1;
$init$Bottom1();

//ClassDef Invariant at reifiedRuntime.ceylon (7:0-7:34)
function Invariant($$targs$$,$$invariant){
    $init$Invariant();
    if ($$invariant===undefined)$$invariant=new Invariant.$$;
    $$$cl1.set_type_args($$invariant,$$targs$$);
    return $$invariant;
}
Invariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{Element:{}},$an:function(){return[$$$cl1.shared()];},d:['misc','Invariant']};};
exports.Invariant=Invariant;
function $init$Invariant(){
    if (Invariant.$$===undefined){
        $$$cl1.initTypeProto(Invariant,'misc::Invariant',$$$cl1.Basic);
    }
    return Invariant;
}
exports.$init$Invariant=$init$Invariant;
$init$Invariant();

//ClassDef Covariant at reifiedRuntime.ceylon (8:0-8:38)
function Covariant($$targs$$,$$covariant){
    $init$Covariant();
    if ($$covariant===undefined)$$covariant=new Covariant.$$;
    $$$cl1.set_type_args($$covariant,$$targs$$);
    return $$covariant;
}
Covariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{Element:{'var':'out'}},$an:function(){return[$$$cl1.shared()];},d:['misc','Covariant']};};
exports.Covariant=Covariant;
function $init$Covariant(){
    if (Covariant.$$===undefined){
        $$$cl1.initTypeProto(Covariant,'misc::Covariant',$$$cl1.Basic);
    }
    return Covariant;
}
exports.$init$Covariant=$init$Covariant;
$init$Covariant();

//ClassDef Contravariant at reifiedRuntime.ceylon (9:0-9:41)
function Contravariant($$targs$$,$$contravariant){
    $init$Contravariant();
    if ($$contravariant===undefined)$$contravariant=new Contravariant.$$;
    $$$cl1.set_type_args($$contravariant,$$targs$$);
    return $$contravariant;
}
Contravariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{Element:{'var':'in'}},$an:function(){return[$$$cl1.shared()];},d:['misc','Contravariant']};};
exports.Contravariant=Contravariant;
function $init$Contravariant(){
    if (Contravariant.$$===undefined){
        $$$cl1.initTypeProto(Contravariant,'misc::Contravariant',$$$cl1.Basic);
    }
    return Contravariant;
}
exports.$init$Contravariant=$init$Contravariant;
$init$Contravariant();

//ClassDef Bivariant at reifiedRuntime.ceylon (10:0-10:41)
function Bivariant($$targs$$,$$bivariant){
    $init$Bivariant();
    if ($$bivariant===undefined)$$bivariant=new Bivariant.$$;
    $$$cl1.set_type_args($$bivariant,$$targs$$);
    return $$bivariant;
}
Bivariant.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{In:{'var':'in'},Out:{'var':'out'}},$an:function(){return[$$$cl1.shared()];},d:['misc','Bivariant']};};
exports.Bivariant=Bivariant;
function $init$Bivariant(){
    if (Bivariant.$$===undefined){
        $$$cl1.initTypeProto(Bivariant,'misc::Bivariant',$$$cl1.Basic);
    }
    return Bivariant;
}
exports.$init$Bivariant=$init$Bivariant;
$init$Bivariant();

//ClassDef Container at reifiedRuntime.ceylon (12:0-16:0)
function Container($$targs$$,$$container){
    $init$Container();
    if ($$container===undefined)$$container=new Container.$$;
    $$$cl1.set_type_args($$container,$$targs$$);
    return $$container;
}
Container.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{Outer:{}},$an:function(){return[$$$cl1.shared()];},d:['misc','Container']};};
exports.Container=Container;
function $init$Container(){
    if (Container.$$===undefined){
        $$$cl1.initTypeProto(Container,'misc::Container',$$$cl1.Basic);
        (function($$container){
            
            //ClassDef Member at reifiedRuntime.ceylon (13:4-15:4)
            function Member$Container($$targs$$,$$member$Container){
                $init$Member$Container();
                if ($$member$Container===undefined)$$member$Container=new this.Member$Container.$$;
                $$$cl1.set_type_args($$member$Container,$$targs$$);
                $$member$Container.$$outer=this;
                return $$member$Container;
            }
            Member$Container.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:Container,$tp:{Inner:{}},$an:function(){return[$$$cl1.shared()];},d:['misc','Container','$c','Member']};};
            function $init$Member$Container(){
                if (Member$Container.$$===undefined){
                    $$$cl1.initTypeProto(Member$Container,'misc::Container.Member',$$$cl1.Basic);
                    Container.Member$Container=Member$Container;
                    (function($$member$Container){
                        
                        //ClassDef Child at reifiedRuntime.ceylon (14:8-14:40)
                        function Child$Member$Container($$targs$$,$$child$Member$Container){
                            $init$Child$Member$Container();
                            if ($$child$Member$Container===undefined)$$child$Member$Container=new this.Child$Member$Container.$$;
                            $$$cl1.set_type_args($$child$Member$Container,$$targs$$);
                            $$child$Member$Container.$$outer=this;
                            return $$child$Member$Container;
                        }
                        Child$Member$Container.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$cont:Member$Container,$tp:{InnerMost:{}},$an:function(){return[$$$cl1.shared()];},d:['misc','Container','$c','Member','$c','Child']};};
                        function $init$Child$Member$Container(){
                            if (Child$Member$Container.$$===undefined){
                                $$$cl1.initTypeProto(Child$Member$Container,'misc::Container.Member.Child',$$$cl1.Basic);
                                Container.Member$Container.Child$Member$Container=Child$Member$Container;
                            }
                            return Child$Member$Container;
                        }
                        $$member$Container.$init$Child$Member$Container=$init$Child$Member$Container;
                        $init$Child$Member$Container();
                        $$member$Container.Child$Member$Container=Child$Member$Container;
                    })(Member$Container.$$.prototype);
                }
                return Member$Container;
            }
            $$container.$init$Member$Container=$init$Member$Container;
            $init$Member$Container();
            $$container.Member$Container=Member$Container;
        })(Container.$$.prototype);
    }
    return Container;
}
exports.$init$Container=$init$Container;
$init$Container();

//InterfaceDef TestInterface1 at reifiedRuntime.ceylon (18:0-18:29)
function TestInterface1($$targs$$,$$testInterface1){
    $$$cl1.set_type_args($$testInterface1,$$targs$$);
}
TestInterface1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$tp:{T:{}},d:['misc','TestInterface1']};};
function $init$TestInterface1(){
    if (TestInterface1.$$===undefined){
        $$$cl1.initTypeProtoI(TestInterface1,'misc::TestInterface1');
    }
    return TestInterface1;
}
exports.$init$TestInterface1=$init$TestInterface1;
$init$TestInterface1();

//ClassDef Test1 at reifiedRuntime.ceylon (19:0-19:46)
function Test1($$targs$$,$$test1){
    $init$Test1();
    if ($$test1===undefined)$$test1=new Test1.$$;
    $$$cl1.set_type_args($$test1,$$targs$$);
    TestInterface1($$test1.$$targs$$===undefined?$$targs$$:{T:$$test1.$$targs$$.T},$$test1);
    return $$test1;
}
Test1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{T:{}},satisfies:[{t:TestInterface1,a:{T:'T'}}],d:['misc','Test1']};};
function $init$Test1(){
    if (Test1.$$===undefined){
        $$$cl1.initTypeProto(Test1,'misc::Test1',$$$cl1.Basic,$init$TestInterface1());
    }
    return Test1;
}
exports.$init$Test1=$init$Test1;
$init$Test1();

//ClassDef Test2 at reifiedRuntime.ceylon (20:0-20:48)
function Test2($$targs$$,$$test2){
    $init$Test2();
    if ($$test2===undefined)$$test2=new Test2.$$;
    $$$cl1.set_type_args($$test2,$$targs$$);
    TestInterface1($$test2.$$targs$$===undefined?$$targs$$:{T:$$test2.$$targs$$.T1},$$test2);
    return $$test2;
}
Test2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{T1:{}},satisfies:[{t:TestInterface1,a:{T:'T1'}}],d:['misc','Test2']};};
function $init$Test2(){
    if (Test2.$$===undefined){
        $$$cl1.initTypeProto(Test2,'misc::Test2',$$$cl1.Basic,$init$TestInterface1());
    }
    return Test2;
}
exports.$init$Test2=$init$Test2;
$init$Test2();

//MethodDef runtimeMethod at reifiedRuntime.ceylon (22:0-24:0)
function runtimeMethod(param$943){
    return $$$cl1.getNothing();
};runtimeMethod.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'param',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['misc','runtimeMethod']};};

//MethodDef testReifiedRuntime at reifiedRuntime.ceylon (26:0-98:0)
function testReifiedRuntime(){
    $$$cl1.print($$$cl1.String("Reified generics",16));
    
    //AttributeDecl member at reifiedRuntime.ceylon (28:4-28:57)
    var member$944=Container({Outer:{t:$$$cl1.String}}).Member$Container({Inner:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.isOfType(member$944,{t:Container.Member$Container,a:{Outer:{t:$$$cl1.String},Inner:{t:$$$cl1.Integer}}}),$$$cl1.String("reified runtime inner 1",23));
    $$$c2.check((!$$$cl1.isOfType(member$944,{t:Container.Member$Container,a:{Outer:{t:$$$cl1.Integer},Inner:{t:$$$cl1.Integer}}})),$$$cl1.String("reified runtime inner 2",23));
    $$$c2.check((!$$$cl1.isOfType(member$944,{t:Container.Member$Container,a:{Outer:{t:$$$cl1.String},Inner:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime inner 3",23));
    
    //AttributeDecl member2 at reifiedRuntime.ceylon (33:4-33:77)
    var member2$945=Container({Outer:{t:$$$cl1.String}}).Member$Container({Inner:{t:$$$cl1.Integer}}).Child$Member$Container({InnerMost:{t:$$$cl1.Character}});
    $$$c2.check($$$cl1.isOfType(member2$945,{t:Container.Member$Container.Child$Member$Container,a:{Outer:{t:$$$cl1.String},Inner:{t:$$$cl1.Integer},InnerMost:{t:$$$cl1.Character}}}),$$$cl1.String("reified runtime inner 4",23));
    
    //AttributeDecl invTop1 at reifiedRuntime.ceylon (36:4-36:38)
    var invTop1$946=Invariant({Element:{t:Top1}});
    $$$c2.check($$$cl1.isOfType(invTop1$946,{t:Invariant,a:{Element:{t:Top1}}}),$$$cl1.String("reified runtime invariant 1",27));
    $$$c2.check((!$$$cl1.isOfType(invTop1$946,{t:Invariant,a:{Element:{t:Middle1}}})),$$$cl1.String("reified runtime invariant 2",27));
    $$$c2.check((!$$$cl1.isOfType(invTop1$946,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl1.String("reified runtime invariant 3",27));
    
    //AttributeDecl invMiddle1 at reifiedRuntime.ceylon (41:4-41:44)
    var invMiddle1$947=Invariant({Element:{t:Middle1}});
    $$$c2.check((!$$$cl1.isOfType(invMiddle1$947,{t:Invariant,a:{Element:{t:Top1}}})),$$$cl1.String("reified runtime invariant 4",27));
    $$$c2.check($$$cl1.isOfType(invMiddle1$947,{t:Invariant,a:{Element:{t:Middle1}}}),$$$cl1.String("reified runtime invariant 5",27));
    $$$c2.check((!$$$cl1.isOfType(invMiddle1$947,{t:Invariant,a:{Element:{t:Bottom1}}})),$$$cl1.String("reified runtime invariant 6",27));
    
    //AttributeDecl covMiddle1 at reifiedRuntime.ceylon (46:4-46:44)
    var covMiddle1$948=Covariant({Element:{t:Middle1}});
    $$$c2.check($$$cl1.isOfType(covMiddle1$948,{t:Covariant,a:{Element:{t:Top1}}}),$$$cl1.String("reified runtime covariant 1",27));
    $$$c2.check($$$cl1.isOfType(covMiddle1$948,{t:Covariant,a:{Element:{t:Middle1}}}),$$$cl1.String("reified runtime covariant 2",27));
    $$$c2.check((!$$$cl1.isOfType(covMiddle1$948,{t:Covariant,a:{Element:{t:Bottom1}}})),$$$cl1.String("reified runtime covariant 3",27));
    
    //AttributeDecl contravMiddle1 at reifiedRuntime.ceylon (51:4-51:52)
    var contravMiddle1$949=Contravariant({Element:{t:Middle1}});
    $$$c2.check((!$$$cl1.isOfType(contravMiddle1$949,{t:Contravariant,a:{Element:{t:Top1}}})),$$$cl1.String("reified runtime contravariant 1",31));
    $$$c2.check($$$cl1.isOfType(contravMiddle1$949,{t:Contravariant,a:{Element:{t:Middle1}}}),$$$cl1.String("reified runtime contravariant 2",31));
    $$$c2.check($$$cl1.isOfType(contravMiddle1$949,{t:Contravariant,a:{Element:{t:Bottom1}}}),$$$cl1.String("reified runtime contravariant 3",31));
    
    //AttributeDecl bivMiddle1 at reifiedRuntime.ceylon (56:4-56:52)
    var bivMiddle1$950=Bivariant({Out:{t:Middle1},In:{t:Middle1}});
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Top1},In:{t:Top1}}})),$$$cl1.String("reified runtime bivariant 1",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Top1}}})),$$$cl1.String("reified runtime bivariant 2",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Top1}}})),$$$cl1.String("reified runtime bivariant 3",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Top1},In:{t:Middle1}}}),$$$cl1.String("reified runtime bivariant 4",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Middle1}}}),$$$cl1.String("reified runtime bivariant 5",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Middle1}}})),$$$cl1.String("reified runtime bivariant 6",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Top1},In:{t:Bottom1}}}),$$$cl1.String("reified runtime bivariant 7",27));
    $$$c2.check($$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Middle1},In:{t:Bottom1}}}),$$$cl1.String("reified runtime bivariant 8",27));
    $$$c2.check((!$$$cl1.isOfType(bivMiddle1$950,{t:Bivariant,a:{Out:{t:Bottom1},In:{t:Bottom1}}})),$$$cl1.String("reified runtime bivariant 9",27));
    
    //ClassDef Local at reifiedRuntime.ceylon (67:4-67:21)
    function Local$951($$targs$$,$$local$951){
        $init$Local$951();
        if ($$local$951===undefined)$$local$951=new Local$951.$$;
        $$$cl1.set_type_args($$local$951,$$targs$$);
        return $$local$951;
    }
    Local$951.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{T:{}},d:['misc','testReifiedRuntime','$c','Local']};};
    function $init$Local$951(){
        if (Local$951.$$===undefined){
            $$$cl1.initTypeProto(Local$951,'misc::testReifiedRuntime.Local',$$$cl1.Basic);
        }
        return Local$951;
    }
    $init$Local$951();
    
    //AttributeDecl localInteger at reifiedRuntime.ceylon (69:4-69:42)
    var localInteger$952=Local$951({T:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.isOfType(localInteger$952,{t:Local$951,a:{T:{t:$$$cl1.Integer}}}),$$$cl1.String("reified runtime local 1",23));
    
    //AttributeDecl m at reifiedRuntime.ceylon (72:4-72:28)
    var m$953=$$$cl1.$JsCallable(runtimeMethod,[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.isOfType(m$953,{t:$$$cl1.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.String}}}),$$$cl1.String("reified runtime callable 1",26));
    $$$c2.check((!$$$cl1.isOfType(m$953,{t:$$$cl1.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Integer}}})),$$$cl1.String("reified runtime callable 2",26));
    $$$c2.check((!$$$cl1.isOfType(m$953,{t:$$$cl1.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl1.String}]},Return:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime callable 3",26));
    $$$c2.check((!$$$cl1.isOfType(m$953,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime callable 4",26));
    
    //AttributeDecl m2 at reifiedRuntime.ceylon (77:4-77:34)
    var m2$954=$$$cl1.$JsCallable(testReifiedRuntime,[],{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}});
    $$$c2.check($$$cl1.isOfType(m2$954,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}}}),$$$cl1.String("reified runtime callable 5",26));
    $$$c2.check((!$$$cl1.isOfType(m2$954,{t:$$$cl1.Callable,a:{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}}})),$$$cl1.String("reified runtime callable 6",26));
    $$$c2.check((!$$$cl1.isOfType(m2$954,{t:$$$cl1.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Anything}}})),$$$cl1.String("reified runtime callable 7",26));
    
    //AttributeDecl rec1 at reifiedRuntime.ceylon (82:4-82:80)
    var rec1$955=$$$cl1.Singleton($$$cl1.Entry((1),$$$cl1.Singleton($$$cl1.String("x",1),{Element:{t:$$$cl1.String}}),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.String}}}}),{Element:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.String}}}}}});
    $$$c2.check($$$cl1.isOfType(rec1$955,{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.String}}}}}}}),$$$cl1.String("#188 [1]",8));
    $$$c2.check((!$$$cl1.isOfType(rec1$955,{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.Singleton,a:{Element:{t:$$$cl1.Integer}}}}}}})),$$$cl1.String("#188 [2]",8));
    
    //InterfaceDef TestInterface2 at reifiedRuntime.ceylon (87:4-87:36)
    function TestInterface2$956($$targs$$,$$testInterface2$956){
        $$$cl1.set_type_args($$testInterface2$956,$$targs$$);
    }
    TestInterface2$956.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$tp:{T:{'var':'in'}},d:['misc','testReifiedRuntime','$i','TestInterface2']};};
    function $init$TestInterface2$956(){
        if (TestInterface2$956.$$===undefined){
            $$$cl1.initTypeProtoI(TestInterface2$956,'misc::testReifiedRuntime.TestInterface2');
        }
        return TestInterface2$956;
    }
    $init$TestInterface2$956();
    
    //ClassDef Test3 at reifiedRuntime.ceylon (88:4-88:53)
    function Test3$957($$targs$$,$$test3$957){
        $init$Test3$957();
        if ($$test3$957===undefined)$$test3$957=new Test3$957.$$;
        $$$cl1.set_type_args($$test3$957,$$targs$$);
        TestInterface2$956($$test3$957.$$targs$$===undefined?$$targs$$:{T:$$test3$957.$$targs$$.T},$$test3$957);
        return $$test3$957;
    }
    Test3$957.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{T:{'var':'in'}},satisfies:[{t:TestInterface2$956,a:{T:'T'}}],d:['misc','testReifiedRuntime','$c','Test3']};};
    function $init$Test3$957(){
        if (Test3$957.$$===undefined){
            $$$cl1.initTypeProto(Test3$957,'misc::testReifiedRuntime.Test3',$$$cl1.Basic,$init$TestInterface2$956());
        }
        return Test3$957;
    }
    $init$Test3$957();
    
    //ClassDef Test4 at reifiedRuntime.ceylon (89:4-89:55)
    function Test4$958($$targs$$,$$test4$958){
        $init$Test4$958();
        if ($$test4$958===undefined)$$test4$958=new Test4$958.$$;
        $$$cl1.set_type_args($$test4$958,$$targs$$);
        TestInterface2$956($$test4$958.$$targs$$===undefined?$$targs$$:{T:$$test4$958.$$targs$$.T1},$$test4$958);
        return $$test4$958;
    }
    Test4$958.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],$tp:{T1:{'var':'in'}},satisfies:[{t:TestInterface2$956,a:{T:'T1'}}],d:['misc','testReifiedRuntime','$c','Test4']};};
    function $init$Test4$958(){
        if (Test4$958.$$===undefined){
            $$$cl1.initTypeProto(Test4$958,'misc::testReifiedRuntime.Test4',$$$cl1.Basic,$init$TestInterface2$956());
        }
        return Test4$958;
    }
    $init$Test4$958();
    
    //AttributeDecl o1 at reifiedRuntime.ceylon (90:4-90:31)
    var o1$959=Test1({T:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.isOfType(o1$959,{t:TestInterface1,a:{T:{t:$$$cl1.String}}}),$$$cl1.String("Issue #221 [1]",14));
    
    //AttributeDecl o2 at reifiedRuntime.ceylon (92:4-92:31)
    var o2$960=Test2({T1:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.isOfType(o2$960,{t:TestInterface1,a:{T:{t:$$$cl1.String}}}),$$$cl1.String("Issue #221 [2]",14));
    
    //AttributeDecl o3 at reifiedRuntime.ceylon (94:4-94:31)
    var o3$961=Test3$957({T:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.isOfType(o3$961,{t:TestInterface2$956,a:{T:{t:$$$cl1.String}}}),$$$cl1.String("Issue #221 [3]",14));
    
    //AttributeDecl o4 at reifiedRuntime.ceylon (96:4-96:31)
    var o4$962=Test4$958({T1:{t:$$$cl1.String}});
    $$$c2.check($$$cl1.isOfType(o4$962,{t:TestInterface2$956,a:{T:{t:$$$cl1.String}}}),$$$cl1.String("Issue #221 [4]",14));
};testReifiedRuntime.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['misc','testReifiedRuntime']};};
var $$$m963=require('members/0.1/members-0.1');
$$$cl1.$addmod$($$$m963,'members/0.1');

//MethodDef test at testit.ceylon (4:0-45:0)
function test(){
    
    //AttributeDecl name at testit.ceylon (5:4-5:24)
    var name$964=$$$cl1.String("hello",5);
    $$$cl1.print(name$964);
    
    //AttributeDecl foo at testit.ceylon (7:4-7:28)
    var foo$965=Foo($$$cl1.String("goodbye",7));
    printBoth(name$964,foo$965.name);
    (y$966=$$$cl1.String("y",1),x$967=$$$cl1.String("x",1),printBoth(x$967,y$966));
    var y$966,x$967;
    foo$965.inc();
    foo$965.inc();
    $$$c2.check(foo$965.count.equals((3)),$$$cl1.String("Foo.count",9));
    $$$c2.check(foo$965.string.equals($$$cl1.String("Foo(goodbye)",12)),$$$cl1.String("Foo.string",10));
    foo$965.printName();
    Bar().printName();
    Bar().Inner$Bar();
    doIt($$$cl1.$JsCallable((opt$968=foo$965,$$$cl1.JsCallable(opt$968,opt$968!==null?opt$968.inc:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}}));
    var opt$968;
    $$$c2.check(foo$965.count.equals((5)),$$$cl1.String("Foo.count [2]",13));
    doIt($$$cl1.$JsCallable(Bar,[],{Arguments:{t:$$$cl1.Empty},Return:{t:Bar}}));
    $$$cl1.print(getFoob().name);
    
    //ObjectDef x at testit.ceylon (20:4-24:4)
    function x$969(){
        var $$x$969=new x$969.$$;
        return $$x$969;
    };x$969.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},d:['misc','test','$o','x']};};
    function $init$x$969(){
        if (x$969.$$===undefined){
            $$$cl1.initTypeProto(x$969,'misc::test.x',$$$cl1.Basic);
        }
        return x$969;
    }
    $init$x$969();
    (function($$x$969){
        
        //MethodDef y at testit.ceylon (21:8-23:8)
        $$x$969.y=function y(){
            var $$x$969=this;
            $$$cl1.print($$$cl1.String("xy",2));
        };$$x$969.y.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:x$969,$an:function(){return[$$$cl1.shared()];},d:['misc','test','$o','x','$m','y']};};
    })(x$969.$$.prototype);
    var x$970;
    function getX$970(){
        if (x$970===undefined){x$970=$init$x$969()();x$970.$$metamodel$$=getX$970.$$metamodel$$;}
        return x$970;
    }
    getX$970.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:x$969},d:['misc','test','$o','x']};};
    $prop$getX$970={get:getX$970,$$metamodel$$:getX$970.$$metamodel$$};
    getX$970().y();
    
    //AttributeDecl b at testit.ceylon (26:4-26:17)
    var b$971=Bar();
    b$971.Inner$Bar().incOuter();
    b$971.Inner$Bar().incOuter();
    b$971.Inner$Bar().incOuter();
    $$$c2.check(b$971.count.equals((4)),$$$cl1.String("Bar.count",9));
    printAll([$$$cl1.String("hello",5),$$$cl1.String("world",5)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}));
    (strings$972=$$$cl1.Tuple($$$cl1.String("hello",5),$$$cl1.Tuple($$$cl1.String("world",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),printAll(strings$972));
    var strings$972;
    
    //AttributeDecl c at testit.ceylon (34:4-34:26)
    var c$973=$$$m963.Counter((0));
    c$973.inc();
    c$973.inc();
    $$$c2.check(c$973.count.equals((2)),$$$cl1.String("Counter.count",13));
    
    //AttributeDecl v2 at testit.ceylon (38:4-38:20)
    var v2$974=$var();
    test_objects();
    testAliasing();
    testLate();
    testReifiedRuntime();
    testStackTrace();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['misc','test']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
