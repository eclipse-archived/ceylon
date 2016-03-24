(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$m":{"p":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"p"}},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments","$o":{"iter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"obj","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"iter"}}},"spread5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"d"}]],"$mt":"mthd","$nm":"spread5"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"spread3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"b"}]],"$mt":"mthd","$nm":"spread3"},"spread4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"}]],"$mt":"mthd","$nm":"spread4"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$m":{"match":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"match"}},"$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"},"one":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"attr","$nm":"one"}},"$nm":"QualifyAmbiguousSupertypes"},"staticJoinTest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"attr","$nm":"staticJoinTest"}},"$mod-bin":"6.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
var $$$f4764=require('functions/0.1/functions-0.1');
$$$cl4138.$addmod$($$$f4764,'functions/0.1');
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//MethodDecl mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$4765,nums$4766){
    if(nums$4766===undefined){nums$4766=$$$cl4138.getEmpty();}
    return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("C:",2),(opt$4767=chars$4765.first,opt$4767!==null?opt$4767:$$$cl4138.String("?",1)).string,$$$cl4138.String(" #",2),(opt$4768=nums$4766.$get((0)),opt$4768!==null?opt$4768:$$$cl4138.String("?",1)).string]).string;
};
mixseqs.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'chars',$mt:'prm',$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}}},$an:function(){return[];}},{$nm:'nums',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Integer}}},$an:function(){return[];}}],d:['invocations','mixseqs']};};
var opt$4767,opt$4768;

//AttributeDecl staticJoinTest at invocations.ceylon (5:0-5:54)
var staticJoinTest$4769;function $valinit$staticJoinTest$4769(){if (staticJoinTest$4769===undefined)staticJoinTest$4769=$$$cl4138.$JsCallable(function(x){return $$$cl4138.String.$$.prototype.join.bind(x);},[{$nm:'p2',$mt:'prm',$t:{t:$$$cl4138.String}}],{Arguments:{t:'T', l:[{t:$$$cl4138.String}]},Return:{t:$$$cl4138.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}}]},Return:{t:$$$cl4138.String}}}});return staticJoinTest$4769;};$valinit$staticJoinTest$4769();
function getStaticJoinTest(){return $valinit$staticJoinTest$4769();}
exports.getStaticJoinTest=getStaticJoinTest;
var $prop$getStaticJoinTest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl4138.String}}]},Return:{t:$$$cl4138.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}}}]},Return:{t:$$$cl4138.String}}}}},d:['invocations','staticJoinTest']};}};
exports.$prop$getStaticJoinTest=$prop$getStaticJoinTest;
$prop$getStaticJoinTest.get=getStaticJoinTest;
getStaticJoinTest.$$metamodel$$=$prop$getStaticJoinTest.$$metamodel$$;

//MethodDef test at invocations.ceylon (7:0-35:0)
function test(){
    $$$f4764.helloWorld();
    ($$$f4764.helloWorld());
    $$$f4764.hello($$$cl4138.String("world",5));
    (name$4770=$$$cl4138.String("world",5),$$$f4764.hello(name$4770));
    var name$4770;
    $$$f4764.helloAll([$$$cl4138.String("someone",7),$$$cl4138.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}));
    (names$4771=$$$cl4138.Tuple($$$cl4138.String("someone",7),$$$cl4138.Tuple($$$cl4138.String("someone else",12),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),$$$f4764.helloAll(names$4771));
    var names$4771;
    
    //AttributeDecl s1 at invocations.ceylon (14:4-14:28)
    var s1$4772=$$$f4764.toString((99));
    
    //AttributeDecl s2 at invocations.ceylon (15:4-15:36)
    var s2$4773=(obj$4774=(99),$$$f4764.toString(obj$4774));
    var obj$4774;
    
    //AttributeDecl f1 at invocations.ceylon (16:4-16:29)
    var f1$4775=$$$f4764.add($$$cl4138.Float(1.0),$$$cl4138.Float(1.0).negativeValue);
    
    //AttributeDecl f2 at invocations.ceylon (17:4-17:37)
    var f2$4776=(x$4777=$$$cl4138.Float(1.0),y$4778=$$$cl4138.Float(1.0).negativeValue,$$$f4764.add(x$4777,y$4778));
    var x$4777,y$4778;
    
    //MethodDef p at invocations.ceylon (18:4-20:4)
    function p$4779(i$4780){
        $$$cl4138.print(i$4780);
    };p$4779.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['invocations','test','$m','p']};};
    $$$f4764.repeat((10),$$$cl4138.$JsCallable(p$4779,[{$nm:'i',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Anything}}));
    testNamedArguments();
    testQualified();
    $$$c4139.check(mixseqs($$$cl4138.Tuple($$$cl4138.Character(97),$$$cl4138.Tuple($$$cl4138.Character(98),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Character},Element:{t:$$$cl4138.Character}}),{Rest:{t:'T', l:[{t:$$$cl4138.Character}]},First:{t:$$$cl4138.Character},Element:{t:$$$cl4138.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}})).equals($$$cl4138.String("C:a #1",6)));
    $$$c4139.check(mixseqs([$$$cl4138.Character(98),$$$cl4138.Character(99)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}})).equals($$$cl4138.String("C:b #2",6)));
    $$$c4139.check(mixseqs($$$cl4138.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}})).equals($$$cl4138.String("C:h #3",6)));
    $$$c4139.check((chars$4781=$$$cl4138.Comprehension(function(){
        //Comprehension at invocations.ceylon (27:18-27:47)
        var it$4782=$$$cl4138.String("hola",4).iterator();
        var c$4783=$$$cl4138.getFinished();
        var next$c$4783=function(){return c$4783=it$4782.next();}
        next$c$4783();
        return function(){
            if(c$4783!==$$$cl4138.getFinished()){
                var c$4783$4784=c$4783;
                var tmpvar$4785=c$4783$4784.uppercased;
                next$c$4783();
                return tmpvar$4785;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}}),mixseqs(chars$4781,$$$cl4138.getEmpty())).equals($$$cl4138.String("C:H #?",6)));
    var chars$4781;
    $$$c4139.check((nums$4786=$$$cl4138.Tuple((2),$$$cl4138.Tuple((1),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),chars$4787=$$$cl4138.String("hola",4).valueOf(),mixseqs(chars$4787,nums$4786)).equals($$$cl4138.String("C:h #2",6)));
    var nums$4786,chars$4787;
    $$$c4139.check((nums$4788=$$$cl4138.Tuple((4),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),chars$4789=$$$cl4138.Comprehension(function(){
        //Comprehension at invocations.ceylon (29:28-29:46)
        var it$4790=$$$cl4138.String("hola",4).iterator();
        var c$4791=$$$cl4138.getFinished();
        var next$c$4791=function(){return c$4791=it$4790.next();}
        next$c$4791();
        return function(){
            if(c$4791!==$$$cl4138.getFinished()){
                var c$4791$4792=c$4791;
                var tmpvar$4793=c$4791$4792;
                next$c$4791();
                return tmpvar$4793;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}}),mixseqs(chars$4789,nums$4788)).equals($$$cl4138.String("C:h #4",6)));
    var nums$4788,chars$4789;
    $$$c4139.check((chars$4794=$$$cl4138.String("hola",4).valueOf(),mixseqs(chars$4794,$$$cl4138.getEmpty())).equals($$$cl4138.String("C:h #?",6)));
    var chars$4794;
    $$$c4139.check((chars$4795=[$$$cl4138.Character(72),$$$cl4138.Character(73)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}}),mixseqs(chars$4795,$$$cl4138.getEmpty())).equals($$$cl4138.String("C:H #?",6)));
    var chars$4795;
    $$$c4139.check(getStaticJoinTest()($$$cl4138.String("**",2))([$$$cl4138.String("a",1),$$$cl4138.String("b",1),$$$cl4138.String("c",1)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.String}})).equals($$$cl4138.String("a**b**c",7)),$$$cl4138.String("static String.join test",23));
    testSpread();
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['invocations','test']};};
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[],
'functions/0.1':[]
};};

//MethodDef namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$4796,desc$4797,match$4798){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$4799 = iter$4796.iterator();
    var i$4800;while ((i$4800=it$4799.next())!==$$$cl4138.getFinished()){
        if(match$4798(i$4800)){
            return $$$cl4138.StringBuilder().appendAll([desc$4797.string,$$$cl4138.String(": ",2),i$4800.string]).string;
        }
    }
    return $$$cl4138.String("[NOT FOUND]",11);
};namedFunc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'iter',$mt:'prm',$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}},$an:function(){return[];}},{$nm:'desc',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}},{$nm:'match',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.Boolean},$an:function(){return[];}}],d:['invocations','namedFunc']};};

//MethodDef order at named.ceylon (14:0-19:0)
function order(product$4801,count$4802,discount$4803,comments$4804){
    if(count$4802===undefined){count$4802=(1);}
    if(discount$4803===undefined){discount$4803=$$$cl4138.Float(0.0);}
    if(comments$4804===undefined){comments$4804=$$$cl4138.getEmpty();}
    
    //AttributeDecl commentStr at named.ceylon (16:4-16:64)
    var commentStr$4805=(strings$4806=$$$cl4138.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$4807=comments$4804.iterator();
        var c$4808=$$$cl4138.getFinished();
        var next$c$4808=function(){return c$4808=it$4807.next();}
        next$c$4808();
        return function(){
            if(c$4808!==$$$cl4138.getFinished()){
                var c$4808$4809=c$4808;
                var tmpvar$4810=$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("\'",1),c$4808$4809.string,$$$cl4138.String("\'",1)]).string;
                next$c$4808();
                return tmpvar$4810;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}),(opt$4811=$$$cl4138.String(", ",2),$$$cl4138.JsCallable(opt$4811,opt$4811!==null?opt$4811.join:null))(strings$4806));
    var strings$4806,opt$4811;
    return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("Order \'",7),product$4801.string,$$$cl4138.String("\', quantity ",12),count$4802.string,$$$cl4138.String(", discount ",11)]).string.plus($$$cl4138.StringBuilder().appendAll([discount$4803.string,$$$cl4138.String(", comments: ",12),commentStr$4805.string]).string);
};order.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'product',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}},{$nm:'count',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'discount',$mt:'prm',$def:1,$t:{t:$$$cl4138.Float},$an:function(){return[];}},{$nm:'comments',$mt:'prm',$def:1,$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}},$an:function(){return[];}}],d:['invocations','order']};};

//MethodDef testNamedArguments at named.ceylon (21:0-63:0)
function testNamedArguments(){
    $$$c4139.check((iter$4812=(function(){
        //ObjectArgument iter at named.ceylon (23:4-26:4)
        function iter$4813(){
            var $$iter$4813=new iter$4813.$$;
            $$$cl4138.Iterable({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}},$$iter$4813);
            $$$cl4138.add_type_arg($$iter$4813,'Absent',{t:$$$cl4138.Null});
            $$$cl4138.add_type_arg($$iter$4813,'Element',{t:$$$cl4138.Integer});
            
            //MethodDecl iterator at named.ceylon (24:6-25:38)
            var iterator=function (){
                return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Integer}}).iterator();
            };
            iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterator,a:{Element:{t:$$$cl4138.Integer}}},$ps:[],$cont:iter$4813,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','testNamedArguments','$o','iter','$m','iterator']};};
            $$iter$4813.iterator=iterator;
            return $$iter$4813;
        }
        iter$4813.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},satisfies:[{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}}],d:['invocations','testNamedArguments','$o','iter']};};
        function $init$iter$4813(){
            if (iter$4813.$$===undefined){
                $$$cl4138.initTypeProto(iter$4813,'invocations::testNamedArguments.iter',$$$cl4138.Basic,$$$cl4138.Iterable);
            }
            return iter$4813;
        }
        $init$iter$4813();
        return iter$4813(new iter$4813.$$);
    }()),desc$4814=(function(){
        //AttributeArgument desc at named.ceylon (27:4-29:4)
        return $$$cl4138.String("Even",4);
    }()),match$4815=function (i$4816){
        return i$4816.remainder((2)).equals((0));
    },namedFunc(iter$4812,desc$4814,match$4815)).equals($$$cl4138.String("Even: 8",7)),$$$cl4138.String("named arguments 1",17));
    var iter$4812,desc$4814,match$4815;
    $$$c4139.check((iter$4817=(function(){
        //ObjectArgument iter at named.ceylon (35:4-39:4)
        function iter$4818(){
            var $$iter$4818=new iter$4818.$$;
            $$$cl4138.Iterable({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}},$$iter$4818);
            $$$cl4138.add_type_arg($$iter$4818,'Absent',{t:$$$cl4138.Null});
            $$$cl4138.add_type_arg($$iter$4818,'Element',{t:$$$cl4138.Integer});
            
            //MethodDef iterator at named.ceylon (36:6-38:6)
            function iterator(){
                return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Integer}}).iterator();
            }
            $$iter$4818.iterator=iterator;
            iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterator,a:{Element:{t:$$$cl4138.Integer}}},$ps:[],$cont:iter$4818,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','testNamedArguments','$o','iter','$m','iterator']};};
            return $$iter$4818;
        }
        iter$4818.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},satisfies:[{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}}],d:['invocations','testNamedArguments','$o','iter']};};
        function $init$iter$4818(){
            if (iter$4818.$$===undefined){
                $$$cl4138.initTypeProto(iter$4818,'invocations::testNamedArguments.iter',$$$cl4138.Basic,$$$cl4138.Iterable);
            }
            return iter$4818;
        }
        $init$iter$4818();
        return iter$4818(new iter$4818.$$);
    }()),desc$4819=(function(){
        //AttributeArgument desc at named.ceylon (40:4-42:4)
        return $$$cl4138.String("Odd",3);
    }()),match$4820=(function (x$4821){
        return x$4821.remainder((2)).equals((1));
    }),namedFunc(iter$4817,desc$4819,match$4820)).equals($$$cl4138.String("Odd: 9",6)),$$$cl4138.String("named arguments 2",17));
    var iter$4817,desc$4819,match$4820;
    $$$c4139.check((desc$4822=$$$cl4138.String("Even",4),match$4823=(function (x$4824){
        return x$4824.equals((2));
    }),iter$4825=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),namedFunc(iter$4825,desc$4822,match$4823)).equals($$$cl4138.String("Even: 2",7)),$$$cl4138.String("named arguments 3",17));
    var desc$4822,match$4823,iter$4825;
    $$$c4139.check((desc$4826=$$$cl4138.String("Even",4),match$4827=(function (x$4828){
        return x$4828.equals((2));
    }),iter$4829=$$$cl4138.Comprehension(function(){
        //Comprehension at named.ceylon (53:4-53:21)
        var it$4830=$$$cl4138.Range((10),(1),{Element:{t:$$$cl4138.Integer}}).iterator();
        var i$4831=$$$cl4138.getFinished();
        var next$i$4831=function(){return i$4831=it$4830.next();}
        next$i$4831();
        return function(){
            if(i$4831!==$$$cl4138.getFinished()){
                var i$4831$4832=i$4831;
                var tmpvar$4833=i$4831$4832;
                next$i$4831();
                return tmpvar$4833;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),namedFunc(iter$4829,desc$4826,match$4827)).equals($$$cl4138.String("Even: 2",7)),$$$cl4138.String("named arguments 4",17));
    var desc$4826,match$4827,iter$4829;
    $$$c4139.check((product$4834=$$$cl4138.String("Mouse",5),order(product$4834,undefined,undefined,undefined)).equals($$$cl4138.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl4138.String("defaulted & sequenced named [1]",31));
    var product$4834;
    $$$c4139.check((product$4835=$$$cl4138.String("Rhinoceros",10),discount$4836=$$$cl4138.Float(10.0),order(product$4835,undefined,discount$4836,undefined)).equals($$$cl4138.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl4138.String("defaulted & sequenced named [2]",31));
    var product$4835,discount$4836;
    $$$c4139.check((product$4837=$$$cl4138.String("Bee",3),count$4838=(531),comments$4839=[$$$cl4138.String("Express delivery",16).valueOf(),$$$cl4138.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}),order(product$4837,count$4838,undefined,comments$4839)).equals($$$cl4138.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl4138.String("defaulted & sequenced named [3]",31));
    var product$4837,count$4838,comments$4839;
};testNamedArguments.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['invocations','testNamedArguments']};};

//InterfaceDef AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
    $$ambiguousParent.$prop$getWhatever={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:AmbiguousParent,$an:function(){return[$$$cl4138.shared(),$$$cl4138.formal()];},d:['invocations','AmbiguousParent','$at','whatever']};}};
    
    //MethodDef somethingElse at qualified.ceylon (7:4-9:4)
    function somethingElse(x$4840){
        return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("something ",10),x$4840.string,$$$cl4138.String(" else",5)]).string;
    }
    $$ambiguousParent.somethingElse=somethingElse;
    somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:AmbiguousParent,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default()];},d:['invocations','AmbiguousParent','$m','somethingElse']};};
}
AmbiguousParent.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['invocations','AmbiguousParent']};};
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl4138.initTypeProtoI(AmbiguousParent,'invocations::AmbiguousParent');
    }
    return AmbiguousParent;
}
exports.$init$AmbiguousParent=$init$AmbiguousParent;
$init$AmbiguousParent();

//InterfaceDef Ambiguous1 at qualified.ceylon (11:0-22:0)
function Ambiguous1($$ambiguous1){
    AmbiguousParent($$ambiguous1);
    $$ambiguous1.somethingElse$$invocations$AmbiguousParent=$$ambiguous1.somethingElse;
    
    //MethodDef doSomething at qualified.ceylon (12:4-14:4)
    function doSomething(){
        return $$$cl4138.String("ambiguous 1",11);
    }
    $$ambiguous1.doSomething=doSomething;
    doSomething.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Ambiguous1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['invocations','Ambiguous1','$m','doSomething']};};
    
    //AttributeGetterDef whatever at qualified.ceylon (15:4-15:55)
    $$$cl4138.defineAttr($$ambiguous1,'whatever',function(){
        return (1);
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Ambiguous1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['invocations','Ambiguous1','$at','whatever']};});
    
    //MethodDef somethingElse at qualified.ceylon (16:4-21:4)
    function somethingElse(x$4841){
        if(x$4841.remainder((2)).equals((0))){
            return $$ambiguous1.somethingElse$$invocations$AmbiguousParent(x$4841);
        }
        return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("Ambiguous1 something ",21),x$4841.string,$$$cl4138.String(" else",5)]).string;
    }
    $$ambiguous1.somethingElse=somethingElse;
    somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:Ambiguous1,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['invocations','Ambiguous1','$m','somethingElse']};};
}
Ambiguous1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:AmbiguousParent}],d:['invocations','Ambiguous1']};};
function $init$Ambiguous1(){
    if (Ambiguous1.$$===undefined){
        $$$cl4138.initTypeProtoI(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
    }
    return Ambiguous1;
}
exports.$init$Ambiguous1=$init$Ambiguous1;
$init$Ambiguous1();

//InterfaceDef Ambiguous2 at qualified.ceylon (23:0-31:0)
function Ambiguous2($$ambiguous2){
    AmbiguousParent($$ambiguous2);
    
    //MethodDef doSomething at qualified.ceylon (24:4-26:4)
    function doSomething(){
        return $$$cl4138.String("ambiguous 2",11);
    }
    $$ambiguous2.doSomething=doSomething;
    doSomething.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:Ambiguous2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['invocations','Ambiguous2','$m','doSomething']};};
    
    //AttributeGetterDef whatever at qualified.ceylon (27:4-27:55)
    $$$cl4138.defineAttr($$ambiguous2,'whatever',function(){
        return (2);
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:Ambiguous2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['invocations','Ambiguous2','$at','whatever']};});
    
    //MethodDef somethingElse at qualified.ceylon (28:4-30:4)
    function somethingElse(x$4842){
        return $$$cl4138.StringBuilder().appendAll([$$$cl4138.String("Ambiguous2 ",11),x$4842.string,$$$cl4138.String(" something else",15)]).string;
    }
    $$ambiguous2.somethingElse=somethingElse;
    somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:Ambiguous2,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.$default()];},d:['invocations','Ambiguous2','$m','somethingElse']};};
}
Ambiguous2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:AmbiguousParent}],d:['invocations','Ambiguous2']};};
function $init$Ambiguous2(){
    if (Ambiguous2.$$===undefined){
        $$$cl4138.initTypeProtoI(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
    }
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDef QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$4843, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$4843_=one$4843;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    $$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.doSomething;
    $$$cl4138.copySuperAttr($$qualifyAmbiguousSupertypes,'whatever','$$invocations$Ambiguous1');
    $$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.somethingElse;
    Ambiguous2($$qualifyAmbiguousSupertypes);
    $$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.doSomething;
    $$$cl4138.copySuperAttr($$qualifyAmbiguousSupertypes,'whatever','$$invocations$Ambiguous2');
    $$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.somethingElse;
    $$$cl4138.defineAttr($$qualifyAmbiguousSupertypes,'one$4843',function(){return this.one$4843_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$cont:QualifyAmbiguousSupertypes,d:['invocations','QualifyAmbiguousSupertypes','$at','one']};});
    
    //MethodDef doSomething at qualified.ceylon (35:4-37:4)
    function doSomething(){
        return (opt$4844=(one$4843?$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1():null),opt$4844!==null?opt$4844:$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2());
        var opt$4844;
    }
    $$qualifyAmbiguousSupertypes.doSomething=doSomething;
    doSomething.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[],$cont:QualifyAmbiguousSupertypes,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','QualifyAmbiguousSupertypes','$m','doSomething']};};
    
    //AttributeGetterDef whatever at qualified.ceylon (38:4-43:4)
    $$$cl4138.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
        if(one$4843){
            return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous1;
        }
        return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous2;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:QualifyAmbiguousSupertypes,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','QualifyAmbiguousSupertypes','$at','whatever']};});
    
    //MethodDef somethingElse at qualified.ceylon (44:4-46:4)
    function somethingElse(x$4845){
        return (opt$4846=(one$4843?$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1(x$4845):null),opt$4846!==null?opt$4846:$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2(x$4845));
        var opt$4846;
    }
    $$qualifyAmbiguousSupertypes.somethingElse=somethingElse;
    somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:QualifyAmbiguousSupertypes,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','QualifyAmbiguousSupertypes','$m','somethingElse']};};
    return $$qualifyAmbiguousSupertypes;
}
QualifyAmbiguousSupertypes.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'one',$mt:'prm',$t:{t:$$$cl4138.Boolean},$an:function(){return[];}}],satisfies:[{t:Ambiguous1},{t:Ambiguous2}],d:['invocations','QualifyAmbiguousSupertypes']};};
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl4138.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl4138.Basic,$init$Ambiguous1(),$init$Ambiguous2());
    }
    return QualifyAmbiguousSupertypes;
}
exports.$init$QualifyAmbiguousSupertypes=$init$QualifyAmbiguousSupertypes;
$init$QualifyAmbiguousSupertypes();

//ClassDef QualifiedA at qualified.ceylon (49:0-51:0)
function QualifiedA($$qualifiedA){
    $init$QualifiedA();
    if ($$qualifiedA===undefined)$$qualifiedA=new QualifiedA.$$;
    
    //AttributeDecl a at qualified.ceylon (50:2-50:37)
    var a=(0);
    $$$cl4138.defineAttr($$qualifiedA,'a',function(){return a;},function(a$4847){return a=a$4847;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:QualifiedA,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default(),$$$cl4138.variable()];},d:['invocations','QualifiedA','$at','a']};});
    $$qualifiedA.$prop$getA={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:QualifiedA,$an:function(){return[$$$cl4138.shared(),$$$cl4138.$default(),$$$cl4138.variable()];},d:['invocations','QualifiedA','$at','a']};}};
    $$qualifiedA.$prop$getA.get=function(){return a};
    return $$qualifiedA;
}
QualifiedA.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['invocations','QualifiedA']};};
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl4138.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl4138.Basic);
    }
    return QualifiedA;
}
exports.$init$QualifiedA=$init$QualifiedA;
$init$QualifiedA();

//ClassDef QualifiedB at qualified.ceylon (52:0-61:0)
function QualifiedB($$qualifiedB){
    $init$QualifiedB();
    if ($$qualifiedB===undefined)$$qualifiedB=new QualifiedB.$$;
    QualifiedA($$qualifiedB);
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    
    //AttributeDecl a at qualified.ceylon (53:2-53:36)
    var a=(0);
    $$$cl4138.defineAttr($$qualifiedB,'a',function(){return a;},function(a$4848){return a=a$4848;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:QualifiedB,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual(),$$$cl4138.variable()];},d:['invocations','QualifiedB','$at','a']};});
    $$qualifiedB.$prop$getA.get=function(){return a};
    
    //MethodDef f at qualified.ceylon (54:2-56:2)
    function f(){
        (tmp$4849=$$qualifiedB,olda$4850=tmp$4849.a$$invocations$QualifiedA,tmp$4849.a$$invocations$QualifiedA=olda$4850.successor,olda$4850);
        var tmp$4849,olda$4850;
    }
    $$qualifiedB.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$cont:QualifiedB,$an:function(){return[$$$cl4138.shared()];},d:['invocations','QualifiedB','$m','f']};};
    
    //MethodDef g at qualified.ceylon (57:2-59:2)
    function g(){
        return (tmp$4851=$$qualifiedB,tmp$4851.a$$invocations$QualifiedA=tmp$4851.a$$invocations$QualifiedA.successor);
        var tmp$4851;
    }
    $$qualifiedB.g=g;
    g.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:QualifiedB,$an:function(){return[$$$cl4138.shared()];},d:['invocations','QualifiedB','$m','g']};};
    
    //AttributeGetterDef supera at qualified.ceylon (60:2-60:58)
    $$$cl4138.defineAttr($$qualifiedB,'supera',function(){
        return $$qualifiedB.a$$invocations$QualifiedA;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:QualifiedB,$an:function(){return[$$$cl4138.shared()];},d:['invocations','QualifiedB','$at','supera']};});
    return $$qualifiedB;
}
QualifiedB.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:QualifiedA},$ps:[],d:['invocations','QualifiedB']};};
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl4138.initTypeProto(QualifiedB,'invocations::QualifiedB',$init$QualifiedA());
    }
    return QualifiedB;
}
exports.$init$QualifiedB=$init$QualifiedB;
$init$QualifiedB();

//ClassDef TestList at qualified.ceylon (63:0-76:0)
function TestList($$testList){
    $init$TestList();
    if ($$testList===undefined)$$testList=new TestList.$$;
    $$testList.$$targs$$={Element:{t:$$$cl4138.String}};
    $$$cl4138.List({Element:{t:$$$cl4138.String}},$$testList);
    $$$cl4138.add_type_arg($$testList,'Element',{t:$$$cl4138.String});
    $$testList.equals$$ceylon$language$List=$$testList.equals;
    $$$cl4138.copySuperAttr($$testList,'hash','$$ceylon$language$List');
    
    //AttributeDecl clone at qualified.ceylon (64:4-64:41)
    var clone=$$$cl4138.getEmpty();
    $$$cl4138.defineAttr($$testList,'clone',function(){return clone;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','clone']};});
    $$testList.$prop$getClone={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','clone']};}};
    $$testList.$prop$getClone.get=function(){return clone};
    
    //MethodDef get at qualified.ceylon (65:4-65:60)
    function $get(index$4852){
        return null;
    }
    $$testList.$get=$get;
    $get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.String}]},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','get']};};
    
    //AttributeDecl lastIndex at qualified.ceylon (66:4-66:43)
    var lastIndex=null;
    $$$cl4138.defineAttr($$testList,'lastIndex',function(){return lastIndex;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','lastIndex']};});
    $$testList.$prop$getLastIndex={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','lastIndex']};}};
    $$testList.$prop$getLastIndex.get=function(){return lastIndex};
    
    //AttributeDecl rest at qualified.ceylon (67:4-67:40)
    var rest=$$$cl4138.getEmpty();
    $$$cl4138.defineAttr($$testList,'rest',function(){return rest;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','rest']};});
    $$testList.$prop$getRest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','rest']};}};
    $$testList.$prop$getRest.get=function(){return rest};
    
    //AttributeDecl reversed at qualified.ceylon (68:4-68:44)
    var reversed=$$$cl4138.getEmpty();
    $$$cl4138.defineAttr($$testList,'reversed',function(){return reversed;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','reversed']};});
    $$testList.$prop$getReversed={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','reversed']};}};
    $$testList.$prop$getReversed.get=function(){return reversed};
    
    //MethodDef segment at qualified.ceylon (69:4-69:82)
    function segment(from$4853,length$4854){
        return $$$cl4138.getEmpty();
    }
    $$testList.segment=segment;
    segment.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'length',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','segment']};};
    
    //MethodDef span at qualified.ceylon (70:4-70:75)
    function span(from$4855,to$4856){
        return $$$cl4138.getEmpty();
    }
    $$testList.span=span;
    span.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'to',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','span']};};
    
    //MethodDef spanTo at qualified.ceylon (71:4-71:63)
    function spanTo(to$4857){
        return $$$cl4138.getEmpty();
    }
    $$testList.spanTo=spanTo;
    spanTo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','spanTo']};};
    
    //MethodDef spanFrom at qualified.ceylon (72:4-72:67)
    function spanFrom(from$4858){
        return $$$cl4138.getEmpty();
    }
    $$testList.spanFrom=spanFrom;
    spanFrom.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','spanFrom']};};
    
    //MethodDecl iterator at qualified.ceylon (73:4-73:62)
    var iterator=function (){
        return $$$cl4138.getEmptyIterator();
    };
    iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterator,a:{Element:{t:$$$cl4138.String}}},$ps:[],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','iterator']};};
    $$testList.iterator=iterator;
    
    //MethodDef equals at qualified.ceylon (74:4-74:93)
    function equals(that$4859){
        return $$testList.equals$$ceylon$language$List(that$4859);
    }
    $$testList.equals=equals;
    equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Boolean},$ps:[{$nm:'that',$mt:'prm',$t:{t:$$$cl4138.Object},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$m','equals']};};
    
    //AttributeGetterDef hash at qualified.ceylon (75:4-75:70)
    $$$cl4138.defineAttr($$testList,'hash',function(){
        return $$testList.hash$$ceylon$language$List;
    },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:TestList,$an:function(){return[$$$cl4138.shared(),$$$cl4138.actual()];},d:['invocations','TestList','$at','hash']};});
    return $$testList;
}
TestList.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],satisfies:[{t:$$$cl4138.List,a:{Element:{t:$$$cl4138.String}}}],d:['invocations','TestList']};};
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl4138.initTypeProto(TestList,'invocations::TestList',$$$cl4138.Basic,$$$cl4138.List);
    }
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDef testQualified at qualified.ceylon (78:0-97:0)
function testQualified(){
    
    //AttributeDecl q1 at qualified.ceylon (79:4-79:47)
    var q1$4860=QualifyAmbiguousSupertypes(true);
    
    //AttributeDecl q2 at qualified.ceylon (80:4-80:48)
    var q2$4861=QualifyAmbiguousSupertypes(false);
    $$$c4139.check(q1$4860.doSomething().equals($$$cl4138.String("ambiguous 1",11)),$$$cl4138.String("qualified super calls [1]",25));
    $$$c4139.check(q2$4861.doSomething().equals($$$cl4138.String("ambiguous 2",11)),$$$cl4138.String("qualified super calls [2]",25));
    $$$c4139.check(q1$4860.whatever.equals((1)),$$$cl4138.String("qualified super attrib [1]",26));
    $$$c4139.check(q2$4861.whatever.equals((2)),$$$cl4138.String("qualified super attrib [2]",26));
    $$$c4139.check(q1$4860.somethingElse((5)).equals($$$cl4138.String("Ambiguous1 something 5 else",27)),$$$cl4138.String("qualified super method [1]",26));
    $$$c4139.check(q1$4860.somethingElse((6)).equals($$$cl4138.String("something 6 else",16)),$$$cl4138.String("qualified super method [2]",26));
    $$$c4139.check(q2$4861.somethingElse((5)).equals($$$cl4138.String("Ambiguous2 5 something else",27)),$$$cl4138.String("qualified super method [3]",26));
    $$$c4139.check(q2$4861.somethingElse((6)).equals($$$cl4138.String("Ambiguous2 6 something else",27)),$$$cl4138.String("qualified super method [4]",26));
    
    //AttributeDecl qb at qualified.ceylon (89:4-89:27)
    var qb$4862=QualifiedB();
    $$$c4139.check(qb$4862.a.equals(qb$4862.supera),$$$cl4138.String("Qualified attribute [1]",23));
    qb$4862.f();
    $$$c4139.check((tmp$4863=qb$4862,tmp$4863.a=tmp$4863.a.successor).equals(qb$4862.supera),$$$cl4138.String("Qualified attribute [2]",23));
    var tmp$4863;
    $$$c4139.check((tmp$4864=qb$4862,tmp$4864.a=tmp$4864.a.successor).equals(qb$4862.g()),$$$cl4138.String("Qualified attribute [3]",23));
    var tmp$4864;
    
    //AttributeDecl tl at qualified.ceylon (94:4-94:25)
    var tl$4865=TestList();
    $$$c4139.check(tl$4865.hash.equals($$$cl4138.getEmpty().hash),$$$cl4138.String("super of List.hash",18));
    $$$c4139.check(tl$4865.equals($$$cl4138.getEmpty()),$$$cl4138.String("super of List.equals",20));
};testQualified.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['invocations','testQualified']};};

//MethodDef spread1 at spread.ceylon (4:0-10:0)
function spread1(a$4866){
    if(a$4866===undefined){a$4866=$$$cl4138.getEmpty();}
    
    //AttributeDecl r at spread.ceylon (5:2-5:23)
    var r$4867=(0);
    function setR$4867(r$4868){return r$4867=r$4868;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$4869 = a$4866.iterator();
    var i$4870;while ((i$4870=it$4869.next())!==$$$cl4138.getFinished()){
        (r$4867=r$4867.plus(i$4870));
    }
    return r$4867;
};spread1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'a',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Integer}}},$an:function(){return[];}}],d:['invocations','spread1']};};

//MethodDef spread2 at spread.ceylon (12:0-18:0)
function spread2(a$4871){
    
    //AttributeDecl r at spread.ceylon (13:2-13:23)
    var r$4872=(0);
    function setR$4872(r$4873){return r$4872=r$4873;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$4874 = a$4871.iterator();
    var i$4875;while ((i$4875=it$4874.next())!==$$$cl4138.getFinished()){
        (r$4872=r$4872.plus(i$4875));
    }
    return r$4872;
};spread2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}},$an:function(){return[];}}],d:['invocations','spread2']};};

//MethodDef spread3 at spread.ceylon (19:0-25:0)
function spread3(a$4876,b$4877){
    if(b$4877===undefined){b$4877=$$$cl4138.getEmpty();}
    
    //AttributeDecl r at spread.ceylon (20:2-20:22)
    var r$4878=a$4876;
    function setR$4878(r$4879){return r$4878=r$4879;};
    //'for' statement at spread.ceylon (21:2-23:2)
    var it$4880 = b$4877.iterator();
    var i$4881;while ((i$4881=it$4880.next())!==$$$cl4138.getFinished()){
        (r$4878=r$4878.plus(i$4881));
    }
    return r$4878;
};spread3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',seq:1,$t:{t:$$$cl4138.Sequential,a:{Element:{t:$$$cl4138.Integer}}},$an:function(){return[];}}],d:['invocations','spread3']};};

//MethodDef spread4 at spread.ceylon (26:0-28:0)
function spread4(a$4882,b$4883,c$4884){
    return a$4882.plus(b$4883).plus(c$4884);
};spread4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'c',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['invocations','spread4']};};

//MethodDef spread5 at spread.ceylon (29:0-31:0)
function spread5(a$4885,b$4886,c$4887,d$4888){
    if(d$4888===undefined){d$4888=(20);}
    return a$4885.plus(b$4886).plus(c$4887).minus(d$4888);
};spread5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'c',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'d',$mt:'prm',$def:1,$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['invocations','spread5']};};

//MethodDef testSpread at spread.ceylon (33:0-48:0)
function testSpread(){
    
    //AttributeDecl ints at spread.ceylon (34:2-34:23)
    var ints$4889=$$$cl4138.Tuple((8),$$$cl4138.Tuple((9),$$$cl4138.Tuple((10),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}});
    $$$c4139.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}})).equals((6)),$$$cl4138.String("spread [1]",10));
    $$$c4139.check(spread1(ints$4889).equals((27)),$$$cl4138.String("spread [2]",10));
    $$$c4139.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}).chain(ints$4889,{Element:{t:$$$cl4138.Integer}})).equals((30)),$$$cl4138.String("spread [3]",10));
    $$$c4139.check(spread1($$$cl4138.Comprehension(function(){
        //Comprehension at spread.ceylon (38:16-38:35)
        var it$4890=ints$4889.iterator();
        var i$4891=$$$cl4138.getFinished();
        var next$i$4891=function(){return i$4891=it$4890.next();}
        next$i$4891();
        return function(){
            if(i$4891!==$$$cl4138.getFinished()){
                var i$4891$4892=i$4891;
                var tmpvar$4893=i$4891$4892.times((10));
                next$i$4891();
                return tmpvar$4893;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}})).equals((270)),$$$cl4138.String("spread [4]",10));
    $$$c4139.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}).chain($$$cl4138.Comprehension(function(){
        //Comprehension at spread.ceylon (39:20-39:39)
        var it$4894=ints$4889.iterator();
        var i$4895=$$$cl4138.getFinished();
        var next$i$4895=function(){return i$4895=it$4894.next();}
        next$i$4895();
        return function(){
            if(i$4895!==$$$cl4138.getFinished()){
                var i$4895$4896=i$4895;
                var tmpvar$4897=i$4895$4896.times((10));
                next$i$4895();
                return tmpvar$4897;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}})).equals((275)),$$$cl4138.String("spread [5]",10));
    $$$c4139.check((a$4898=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),spread2(a$4898)).equals((6)),$$$cl4138.String("spread [6]",10));
    var a$4898;
    $$$c4139.check((a$4899=ints$4889,spread2(a$4899)).equals((27)),$$$cl4138.String("spread [7]",10));
    var a$4899;
    $$$c4139.check((a$4900=[(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}).chain(ints$4889,{Element:{t:$$$cl4138.Integer}}),spread2(a$4900)).equals((30)),$$$cl4138.String("spread [8]",10));
    var a$4900;
    $$$c4139.check((a$4901=$$$cl4138.Comprehension(function(){
        //Comprehension at spread.ceylon (43:16-43:35)
        var it$4902=ints$4889.iterator();
        var i$4903=$$$cl4138.getFinished();
        var next$i$4903=function(){return i$4903=it$4902.next();}
        next$i$4903();
        return function(){
            if(i$4903!==$$$cl4138.getFinished()){
                var i$4903$4904=i$4903;
                var tmpvar$4905=i$4903$4904.times((10));
                next$i$4903();
                return tmpvar$4905;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),spread2(a$4901)).equals((270)),$$$cl4138.String("spread [9]",10));
    var a$4901;
    $$$c4139.check((a$4906=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}).chain($$$cl4138.Comprehension(function(){
        //Comprehension at spread.ceylon (44:20-44:39)
        var it$4907=ints$4889.iterator();
        var i$4908=$$$cl4138.getFinished();
        var next$i$4908=function(){return i$4908=it$4907.next();}
        next$i$4908();
        return function(){
            if(i$4908!==$$$cl4138.getFinished()){
                var i$4908$4909=i$4908;
                var tmpvar$4910=i$4908$4909.times((10));
                next$i$4908();
                return tmpvar$4910;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}}),spread2(a$4906)).equals((275)),$$$cl4138.String("spread [10]",11));
    var a$4906;
    $$$c4139.check(spread3((1),ints$4889).equals((28)),$$$cl4138.String("spread [11]",11));
    $$$c4139.check(spread4(ints$4889.$get(0),ints$4889.$get(1)||undefined,ints$4889.$get(2)||undefined).equals((27)),$$$cl4138.String("spread [12]",11));
    $$$c4139.check(spread5(ints$4889.$get(0),ints$4889.$get(1)||undefined,ints$4889.$get(2)||undefined,ints$4889.$get(3)||undefined).equals((7)),$$$cl4138.String("spread [13]",11));
};testSpread.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['invocations','testSpread']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
