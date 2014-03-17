(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$m":{"p":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"p"}},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments","$o":{"iter":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"}],"$mt":"obj","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"}},"$nm":"iter"}}},"spread5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"d"}]],"$mt":"mthd","$nm":"spread5"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"spread3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$nm":"b"}]],"$mt":"mthd","$nm":"spread3"},"spread4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"c"}]],"$mt":"mthd","$nm":"spread4"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$m":{"match":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"i"}]],"$mt":"mthd","$nm":"match"}},"$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"},"one":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"attr","$nm":"one"}},"$nm":"QualifyAmbiguousSupertypes"},"staticJoinTest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$mt":"attr","$nm":"staticJoinTest"}},"$mod-bin":"6.0"};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
var $$$f645=require('functions/0.1/functions-0.1');
$$$cl1.$addmod$($$$f645,'functions/0.1');
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//MethodDecl mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$646,nums$647){
    if(nums$647===undefined){nums$647=$$$cl1.getEmpty();}
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("C:",2),(opt$648=chars$646.first,opt$648!==null?opt$648:$$$cl1.String("?",1)).string,$$$cl1.String(" #",2),(opt$649=nums$647.$get((0)),opt$649!==null?opt$649:$$$cl1.String("?",1)).string]).string;
};
mixseqs.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'chars',$mt:'prm',$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}},$an:function(){return[];}},{$nm:'nums',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}},$an:function(){return[];}}],d:['invocations','mixseqs']};};
var opt$648,opt$649;

//AttributeDecl staticJoinTest at invocations.ceylon (5:0-5:54)
var staticJoinTest$650;function $valinit$staticJoinTest$650(){if (staticJoinTest$650===undefined)staticJoinTest$650=$$$cl1.$JsCallable(function(x){return $$$cl1.String.$$.prototype.join.bind(x);},[{$nm:'p2',$mt:'prm',$t:{t:$$$cl1.String}}],{Arguments:{t:'T', l:[{t:$$$cl1.String}]},Return:{t:$$$cl1.Callable,a:{Arguments:{t:'T', l:[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}}]},Return:{t:$$$cl1.String}}}});return staticJoinTest$650;};$valinit$staticJoinTest$650();
function getStaticJoinTest(){return $valinit$staticJoinTest$650();}
exports.getStaticJoinTest=getStaticJoinTest;
var $prop$getStaticJoinTest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl1.String}}]},Return:{t:$$$cl1.Callable,a:{Arguments:{t:'T',l:[{$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}}}]},Return:{t:$$$cl1.String}}}}},d:['invocations','staticJoinTest']};}};
exports.$prop$getStaticJoinTest=$prop$getStaticJoinTest;
$prop$getStaticJoinTest.get=getStaticJoinTest;
getStaticJoinTest.$$metamodel$$=$prop$getStaticJoinTest.$$metamodel$$;

//MethodDef test at invocations.ceylon (7:0-35:0)
function test(){
    $$$f645.helloWorld();
    ($$$f645.helloWorld());
    $$$f645.hello($$$cl1.String("world",5));
    (name$651=$$$cl1.String("world",5),$$$f645.hello(name$651));
    var name$651;
    $$$f645.helloAll([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}));
    (names$652=$$$cl1.Tuple($$$cl1.String("someone",7),$$$cl1.Tuple($$$cl1.String("someone else",12),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),$$$f645.helloAll(names$652));
    var names$652;
    
    //AttributeDecl s1 at invocations.ceylon (14:4-14:28)
    var s1$653=$$$f645.toString((99));
    
    //AttributeDecl s2 at invocations.ceylon (15:4-15:36)
    var s2$654=(obj$655=(99),$$$f645.toString(obj$655));
    var obj$655;
    
    //AttributeDecl f1 at invocations.ceylon (16:4-16:29)
    var f1$656=$$$f645.add($$$cl1.Float(1.0),$$$cl1.Float(1.0).negativeValue);
    
    //AttributeDecl f2 at invocations.ceylon (17:4-17:37)
    var f2$657=(x$658=$$$cl1.Float(1.0),y$659=$$$cl1.Float(1.0).negativeValue,$$$f645.add(x$658,y$659));
    var x$658,y$659;
    
    //MethodDef p at invocations.ceylon (18:4-20:4)
    function p$660(i$661){
        $$$cl1.print(i$661);
    };p$660.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['invocations','test','$m','p']};};
    $$$f645.repeat((10),$$$cl1.$JsCallable(p$660,[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Anything}}));
    testNamedArguments();
    testQualified();
    $$$c2.check(mixseqs($$$cl1.Tuple($$$cl1.Character(97),$$$cl1.Tuple($$$cl1.Character(98),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),{Rest:{t:'T', l:[{t:$$$cl1.Character}]},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:a #1",6)));
    $$$c2.check(mixseqs([$$$cl1.Character(98),$$$cl1.Character(99)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:b #2",6)));
    $$$c2.check(mixseqs($$$cl1.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:h #3",6)));
    $$$c2.check((chars$662=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (27:18-27:47)
        var it$663=$$$cl1.String("hola",4).iterator();
        var c$664=$$$cl1.getFinished();
        var next$c$664=function(){return c$664=it$663.next();}
        next$c$664();
        return function(){
            if(c$664!==$$$cl1.getFinished()){
                var c$664$665=c$664;
                var tmpvar$666=c$664$665.uppercased;
                next$c$664();
                return tmpvar$666;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}),mixseqs(chars$662,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$662;
    $$$c2.check((nums$667=$$$cl1.Tuple((2),$$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$668=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$668,nums$667)).equals($$$cl1.String("C:h #2",6)));
    var nums$667,chars$668;
    $$$c2.check((nums$669=$$$cl1.Tuple((4),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$670=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (29:28-29:46)
        var it$671=$$$cl1.String("hola",4).iterator();
        var c$672=$$$cl1.getFinished();
        var next$c$672=function(){return c$672=it$671.next();}
        next$c$672();
        return function(){
            if(c$672!==$$$cl1.getFinished()){
                var c$672$673=c$672;
                var tmpvar$674=c$672$673;
                next$c$672();
                return tmpvar$674;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}),mixseqs(chars$670,nums$669)).equals($$$cl1.String("C:h #4",6)));
    var nums$669,chars$670;
    $$$c2.check((chars$675=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$675,$$$cl1.getEmpty())).equals($$$cl1.String("C:h #?",6)));
    var chars$675;
    $$$c2.check((chars$676=[$$$cl1.Character(72),$$$cl1.Character(73)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}),mixseqs(chars$676,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$676;
    $$$c2.check(getStaticJoinTest()($$$cl1.String("**",2))([$$$cl1.String("a",1),$$$cl1.String("b",1),$$$cl1.String("c",1)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.String}})).equals($$$cl1.String("a**b**c",7)),$$$cl1.String("static String.join test",23));
    testSpread();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['invocations','test']};};
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[],
'functions/0.1':[]
};};

//MethodDef namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$677,desc$678,match$679){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$680 = iter$677.iterator();
    var i$681;while ((i$681=it$680.next())!==$$$cl1.getFinished()){
        if(match$679(i$681)){
            return $$$cl1.StringBuilder().appendAll([desc$678.string,$$$cl1.String(": ",2),i$681.string]).string;
        }
    }
    return $$$cl1.String("[NOT FOUND]",11);
};namedFunc.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'iter',$mt:'prm',$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}},$an:function(){return[];}},{$nm:'desc',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}},{$nm:'match',$mt:'prm',$pt:'f',$t:{t:$$$cl1.Boolean},$an:function(){return[];}}],d:['invocations','namedFunc']};};

//MethodDef order at named.ceylon (14:0-19:0)
function order(product$682,count$683,discount$684,comments$685){
    if(count$683===undefined){count$683=(1);}
    if(discount$684===undefined){discount$684=$$$cl1.Float(0.0);}
    if(comments$685===undefined){comments$685=$$$cl1.getEmpty();}
    
    //AttributeDecl commentStr at named.ceylon (16:4-16:64)
    var commentStr$686=(strings$687=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$688=comments$685.iterator();
        var c$689=$$$cl1.getFinished();
        var next$c$689=function(){return c$689=it$688.next();}
        next$c$689();
        return function(){
            if(c$689!==$$$cl1.getFinished()){
                var c$689$690=c$689;
                var tmpvar$691=$$$cl1.StringBuilder().appendAll([$$$cl1.String("\'",1),c$689$690.string,$$$cl1.String("\'",1)]).string;
                next$c$689();
                return tmpvar$691;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}),(opt$692=$$$cl1.String(", ",2),$$$cl1.JsCallable(opt$692,opt$692!==null?opt$692.join:null))(strings$687));
    var strings$687,opt$692;
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Order \'",7),product$682.string,$$$cl1.String("\', quantity ",12),count$683.string,$$$cl1.String(", discount ",11)]).string.plus($$$cl1.StringBuilder().appendAll([discount$684.string,$$$cl1.String(", comments: ",12),commentStr$686.string]).string);
};order.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'product',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}},{$nm:'count',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'discount',$mt:'prm',$def:1,$t:{t:$$$cl1.Float},$an:function(){return[];}},{$nm:'comments',$mt:'prm',$def:1,$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}},$an:function(){return[];}}],d:['invocations','order']};};

//MethodDef testNamedArguments at named.ceylon (21:0-63:0)
function testNamedArguments(){
    $$$c2.check((iter$693=(function(){
        //ObjectArgument iter at named.ceylon (23:4-26:4)
        function iter$694(){
            var $$iter$694=new iter$694.$$;
            $$$cl1.Iterable({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}},$$iter$694);
            $$$cl1.add_type_arg($$iter$694,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$694,'Element',{t:$$$cl1.Integer});
            return $$iter$694;
        }
        iter$694.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},satisfies:[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}],d:['invocations','testNamedArguments','$o','iter']};};
        function $init$iter$694(){
            if (iter$694.$$===undefined){
                $$$cl1.initTypeProto(iter$694,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
                (function($$iter$694){
                    
                    //MethodDecl iterator at named.ceylon (24:6-25:38)
                    $$iter$694.iterator=function (){
                        var $$iter$694=this;
                        return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator();
                    };
                    $$iter$694.iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}},$ps:[],$cont:iter$694,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','testNamedArguments','$o','iter','$m','iterator']};};
                })(iter$694.$$.prototype);
            }
            return iter$694;
        }
        $init$iter$694();
        return iter$694(new iter$694.$$);
    }()),desc$695=(function(){
        //AttributeArgument desc at named.ceylon (27:4-29:4)
        return $$$cl1.String("Even",4);
    }()),match$696=function (i$697){
        return i$697.remainder((2)).equals((0));
    },namedFunc(iter$693,desc$695,match$696)).equals($$$cl1.String("Even: 8",7)),$$$cl1.String("named arguments 1",17));
    var iter$693,desc$695,match$696;
    $$$c2.check((iter$698=(function(){
        //ObjectArgument iter at named.ceylon (35:4-39:4)
        function iter$699(){
            var $$iter$699=new iter$699.$$;
            $$$cl1.Iterable({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}},$$iter$699);
            $$$cl1.add_type_arg($$iter$699,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$699,'Element',{t:$$$cl1.Integer});
            return $$iter$699;
        }
        iter$699.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},satisfies:[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}],d:['invocations','testNamedArguments','$o','iter']};};
        function $init$iter$699(){
            if (iter$699.$$===undefined){
                $$$cl1.initTypeProto(iter$699,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
                (function($$iter$699){
                    
                    //MethodDef iterator at named.ceylon (36:6-38:6)
                    $$iter$699.iterator=function iterator(){
                        var $$iter$699=this;
                        return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator();
                    };$$iter$699.iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}},$ps:[],$cont:iter$699,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','testNamedArguments','$o','iter','$m','iterator']};};
                })(iter$699.$$.prototype);
            }
            return iter$699;
        }
        $init$iter$699();
        return iter$699(new iter$699.$$);
    }()),desc$700=(function(){
        //AttributeArgument desc at named.ceylon (40:4-42:4)
        return $$$cl1.String("Odd",3);
    }()),match$701=(function (x$702){
        return x$702.remainder((2)).equals((1));
    }),namedFunc(iter$698,desc$700,match$701)).equals($$$cl1.String("Odd: 9",6)),$$$cl1.String("named arguments 2",17));
    var iter$698,desc$700,match$701;
    $$$c2.check((desc$703=$$$cl1.String("Even",4),match$704=(function (x$705){
        return x$705.equals((2));
    }),iter$706=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),namedFunc(iter$706,desc$703,match$704)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 3",17));
    var desc$703,match$704,iter$706;
    $$$c2.check((desc$707=$$$cl1.String("Even",4),match$708=(function (x$709){
        return x$709.equals((2));
    }),iter$710=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (53:4-53:21)
        var it$711=$$$cl1.Range((10),(1),{Element:{t:$$$cl1.Integer}}).iterator();
        var i$712=$$$cl1.getFinished();
        var next$i$712=function(){return i$712=it$711.next();}
        next$i$712();
        return function(){
            if(i$712!==$$$cl1.getFinished()){
                var i$712$713=i$712;
                var tmpvar$714=i$712$713;
                next$i$712();
                return tmpvar$714;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),namedFunc(iter$710,desc$707,match$708)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 4",17));
    var desc$707,match$708,iter$710;
    $$$c2.check((product$715=$$$cl1.String("Mouse",5),order(product$715,undefined,undefined,undefined)).equals($$$cl1.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl1.String("defaulted & sequenced named [1]",31));
    var product$715;
    $$$c2.check((product$716=$$$cl1.String("Rhinoceros",10),discount$717=$$$cl1.Float(10.0),order(product$716,undefined,discount$717,undefined)).equals($$$cl1.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl1.String("defaulted & sequenced named [2]",31));
    var product$716,discount$717;
    $$$c2.check((product$718=$$$cl1.String("Bee",3),count$719=(531),comments$720=[$$$cl1.String("Express delivery",16).valueOf(),$$$cl1.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}),order(product$718,count$719,undefined,comments$720)).equals($$$cl1.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl1.String("defaulted & sequenced named [3]",31));
    var product$718,count$719,comments$720;
};testNamedArguments.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['invocations','testNamedArguments']};};

//InterfaceDef AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
}
AmbiguousParent.$$metamodel$$=function(){return{mod:$$METAMODEL$$,d:['invocations','AmbiguousParent']};};
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl1.initTypeProtoI(AmbiguousParent,'invocations::AmbiguousParent');
        (function($$ambiguousParent){
            $$ambiguousParent.doSomething={$fml:1,$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:AmbiguousParent,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['invocations','AmbiguousParent','$m','doSomething']};}};
            //AttributeDecl whatever at qualified.ceylon (6:4-6:34)
            $$ambiguousParent.$prop$getWhatever={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:AmbiguousParent,$an:function(){return[$$$cl1.shared(),$$$cl1.formal()];},d:['invocations','AmbiguousParent','$at','whatever']};}};
            
            //MethodDef somethingElse at qualified.ceylon (7:4-9:4)
            $$ambiguousParent.somethingElse=function somethingElse(x$721){
                var $$ambiguousParent=this;
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("something ",10),x$721.string,$$$cl1.String(" else",5)]).string;
            };$$ambiguousParent.somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:AmbiguousParent,$an:function(){return[$$$cl1.shared(),$$$cl1.$default()];},d:['invocations','AmbiguousParent','$m','somethingElse']};};
        })(AmbiguousParent.$$.prototype);
    }
    return AmbiguousParent;
}
exports.$init$AmbiguousParent=$init$AmbiguousParent;
$init$AmbiguousParent();

//InterfaceDef Ambiguous1 at qualified.ceylon (11:0-22:0)
function Ambiguous1($$ambiguous1){
    AmbiguousParent($$ambiguous1);
}
Ambiguous1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:AmbiguousParent}],d:['invocations','Ambiguous1']};};
function $init$Ambiguous1(){
    if (Ambiguous1.$$===undefined){
        $$$cl1.initTypeProtoI(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
        (function($$ambiguous1){
            
            //MethodDef doSomething at qualified.ceylon (12:4-14:4)
            $$ambiguous1.doSomething=function doSomething(){
                var $$ambiguous1=this;
                return $$$cl1.String("ambiguous 1",11);
            };$$ambiguous1.doSomething.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Ambiguous1,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['invocations','Ambiguous1','$m','doSomething']};};
            
            //AttributeGetterDef whatever at qualified.ceylon (15:4-15:55)
            $$$cl1.defineAttr($$ambiguous1,'whatever',function(){
                var $$ambiguous1=this;
                return (1);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Ambiguous1,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['invocations','Ambiguous1','$at','whatever']};});
            //MethodDef somethingElse at qualified.ceylon (16:4-21:4)
            $$ambiguous1.somethingElse=function somethingElse(x$722){
                var $$ambiguous1=this;
                if(x$722.remainder((2)).equals((0))){
                    return $$ambiguous1.getT$all()['invocations::AmbiguousParent'].$$.prototype.somethingElse.call(this,x$722);
                }
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous1 something ",21),x$722.string,$$$cl1.String(" else",5)]).string;
            };$$ambiguous1.somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:Ambiguous1,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['invocations','Ambiguous1','$m','somethingElse']};};
        })(Ambiguous1.$$.prototype);
    }
    return Ambiguous1;
}
exports.$init$Ambiguous1=$init$Ambiguous1;
$init$Ambiguous1();

//InterfaceDef Ambiguous2 at qualified.ceylon (23:0-31:0)
function Ambiguous2($$ambiguous2){
    AmbiguousParent($$ambiguous2);
}
Ambiguous2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,satisfies:[{t:AmbiguousParent}],d:['invocations','Ambiguous2']};};
function $init$Ambiguous2(){
    if (Ambiguous2.$$===undefined){
        $$$cl1.initTypeProtoI(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
        (function($$ambiguous2){
            
            //MethodDef doSomething at qualified.ceylon (24:4-26:4)
            $$ambiguous2.doSomething=function doSomething(){
                var $$ambiguous2=this;
                return $$$cl1.String("ambiguous 2",11);
            };$$ambiguous2.doSomething.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:Ambiguous2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['invocations','Ambiguous2','$m','doSomething']};};
            
            //AttributeGetterDef whatever at qualified.ceylon (27:4-27:55)
            $$$cl1.defineAttr($$ambiguous2,'whatever',function(){
                var $$ambiguous2=this;
                return (2);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:Ambiguous2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['invocations','Ambiguous2','$at','whatever']};});
            //MethodDef somethingElse at qualified.ceylon (28:4-30:4)
            $$ambiguous2.somethingElse=function somethingElse(x$723){
                var $$ambiguous2=this;
                return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous2 ",11),x$723.string,$$$cl1.String(" something else",15)]).string;
            };$$ambiguous2.somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:Ambiguous2,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.$default()];},d:['invocations','Ambiguous2','$m','somethingElse']};};
        })(Ambiguous2.$$.prototype);
    }
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDef QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$724, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$724_=one$724;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    Ambiguous2($$qualifyAmbiguousSupertypes);
    return $$qualifyAmbiguousSupertypes;
}
QualifyAmbiguousSupertypes.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'one',$mt:'prm',$t:{t:$$$cl1.Boolean},$an:function(){return[];}}],satisfies:[{t:Ambiguous1},{t:Ambiguous2}],d:['invocations','QualifyAmbiguousSupertypes']};};
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl1.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl1.Basic,$init$Ambiguous1(),$init$Ambiguous2());
        (function($$qualifyAmbiguousSupertypes){
            
            //MethodDef doSomething at qualified.ceylon (35:4-37:4)
            $$qualifyAmbiguousSupertypes.doSomething=function doSomething(){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$725=($$qualifyAmbiguousSupertypes.one$724?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.doSomething.call(this):null),opt$725!==null?opt$725:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.doSomething.call(this));
                var opt$725;
            };$$qualifyAmbiguousSupertypes.doSomething.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[],$cont:QualifyAmbiguousSupertypes,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','QualifyAmbiguousSupertypes','$m','doSomething']};};
            
            //AttributeGetterDef whatever at qualified.ceylon (38:4-43:4)
            $$$cl1.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
                var $$qualifyAmbiguousSupertypes=this;
                if($$qualifyAmbiguousSupertypes.one$724){
                    return $$$cl1.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'],'whatever').call(this);
                }
                return $$$cl1.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'],'whatever').call(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:QualifyAmbiguousSupertypes,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','QualifyAmbiguousSupertypes','$at','whatever']};});
            //MethodDef somethingElse at qualified.ceylon (44:4-46:4)
            $$qualifyAmbiguousSupertypes.somethingElse=function somethingElse(x$726){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$727=($$qualifyAmbiguousSupertypes.one$724?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.somethingElse.call(this,x$726):null),opt$727!==null?opt$727:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.somethingElse.call(this,x$726));
                var opt$727;
            };$$qualifyAmbiguousSupertypes.somethingElse.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:QualifyAmbiguousSupertypes,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','QualifyAmbiguousSupertypes','$m','somethingElse']};};
            $$$cl1.defineAttr($$qualifyAmbiguousSupertypes,'one$724',function(){return this.one$724_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$cont:QualifyAmbiguousSupertypes,d:['invocations','QualifyAmbiguousSupertypes','$at','one']};});
        })(QualifyAmbiguousSupertypes.$$.prototype);
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
    $$qualifiedA.a$728_=(0);
    $$qualifiedA.$prop$getA={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:QualifiedA,$an:function(){return[$$$cl1.shared(),$$$cl1.$default(),$$$cl1.variable()];},d:['invocations','QualifiedA','$at','a']};}};
    $$qualifiedA.$prop$getA.get=function(){return a};
    return $$qualifiedA;
}
QualifiedA.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['invocations','QualifiedA']};};
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl1.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl1.Basic);
        (function($$qualifiedA){
            
            //AttributeDecl a at qualified.ceylon (50:2-50:37)
            $$$cl1.defineAttr($$qualifiedA,'a',function(){return this.a$728_;},function(a$729){return this.a$728_=a$729;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:QualifiedA,$an:function(){return[$$$cl1.shared(),$$$cl1.$default(),$$$cl1.variable()];},d:['invocations','QualifiedA','$at','a']};});
        })(QualifiedA.$$.prototype);
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
    
    //AttributeDecl a at qualified.ceylon (53:2-53:36)
    $$qualifiedB.a$730_=(0);
    $$qualifiedB.$prop$getA.get=function(){return a};
    return $$qualifiedB;
}
QualifiedB.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:QualifiedA},$ps:[],d:['invocations','QualifiedB']};};
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl1.initTypeProto(QualifiedB,'invocations::QualifiedB',$init$QualifiedA());
        (function($$qualifiedB){
            
            //AttributeDecl a at qualified.ceylon (53:2-53:36)
            $$$cl1.defineAttr($$qualifiedB,'a',function(){return this.a$730_;},function(a$731){return this.a$730_=a$731;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:QualifiedB,$an:function(){return[$$$cl1.shared(),$$$cl1.actual(),$$$cl1.variable()];},d:['invocations','QualifiedB','$at','a']};});
            
            //MethodDef f at qualified.ceylon (54:2-56:2)
            $$qualifiedB.f=function f(){
                var $$qualifiedB=this;
                (tmp$732=$$qualifiedB,olda$733=$$$cl1.attrGetter(tmp$732.getT$all()['invocations::QualifiedA'],'a').call(this),$$$cl1.attrSetter(tmp$732.getT$all()['invocations::QualifiedA'],'a').call(this,olda$733.successor),olda$733);
                var tmp$732,olda$733;
            };$$qualifiedB.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$cont:QualifiedB,$an:function(){return[$$$cl1.shared()];},d:['invocations','QualifiedB','$m','f']};};
            
            //MethodDef g at qualified.ceylon (57:2-59:2)
            $$qualifiedB.g=function g(){
                var $$qualifiedB=this;
                return (tmp$734=$$qualifiedB,$$$cl1.attrSetter(tmp$734.getT$all()['invocations::QualifiedA'],'a').call(this,$$$cl1.attrGetter(tmp$734.getT$all()['invocations::QualifiedA'],'a').call(this).successor));
                var tmp$734;
            };$$qualifiedB.g.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:QualifiedB,$an:function(){return[$$$cl1.shared()];},d:['invocations','QualifiedB','$m','g']};};
            
            //AttributeGetterDef supera at qualified.ceylon (60:2-60:58)
            $$$cl1.defineAttr($$qualifiedB,'supera',function(){
                var $$qualifiedB=this;
                return $$$cl1.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:QualifiedB,$an:function(){return[$$$cl1.shared()];},d:['invocations','QualifiedB','$at','supera']};});
        })(QualifiedB.$$.prototype);
    }
    return QualifiedB;
}
exports.$init$QualifiedB=$init$QualifiedB;
$init$QualifiedB();

//ClassDef TestList at qualified.ceylon (63:0-76:0)
function TestList($$testList){
    $init$TestList();
    if ($$testList===undefined)$$testList=new TestList.$$;
    $$testList.$$targs$$={Element:{t:$$$cl1.String}};
    $$$cl1.List({Element:{t:$$$cl1.String}},$$testList);
    $$$cl1.add_type_arg($$testList,'Element',{t:$$$cl1.String});
    
    //AttributeDecl clone at qualified.ceylon (64:4-64:41)
    $$testList.clone$735_=$$$cl1.getEmpty();
    $$testList.$prop$getClone={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','clone']};}};
    $$testList.$prop$getClone.get=function(){return clone};
    
    //AttributeDecl lastIndex at qualified.ceylon (66:4-66:43)
    $$testList.lastIndex$736_=null;
    $$testList.$prop$getLastIndex={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','lastIndex']};}};
    $$testList.$prop$getLastIndex.get=function(){return lastIndex};
    
    //AttributeDecl rest at qualified.ceylon (67:4-67:40)
    $$testList.rest$737_=$$$cl1.getEmpty();
    $$testList.$prop$getRest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','rest']};}};
    $$testList.$prop$getRest.get=function(){return rest};
    
    //AttributeDecl reversed at qualified.ceylon (68:4-68:44)
    $$testList.reversed$738_=$$$cl1.getEmpty();
    $$testList.$prop$getReversed={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','reversed']};}};
    $$testList.$prop$getReversed.get=function(){return reversed};
    return $$testList;
}
TestList.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],satisfies:[{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}}],d:['invocations','TestList']};};
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl1.initTypeProto(TestList,'invocations::TestList',$$$cl1.Basic,$$$cl1.List);
        (function($$testList){
            
            //AttributeDecl clone at qualified.ceylon (64:4-64:41)
            $$$cl1.defineAttr($$testList,'clone',function(){return this.clone$735_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','clone']};});
            
            //MethodDef get at qualified.ceylon (65:4-65:60)
            $$testList.$get=function $get(index$739){
                var $$testList=this;
                return null;
            };$$testList.$get.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.String}]},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','get']};};
            
            //AttributeDecl lastIndex at qualified.ceylon (66:4-66:43)
            $$$cl1.defineAttr($$testList,'lastIndex',function(){return this.lastIndex$736_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','lastIndex']};});
            
            //AttributeDecl rest at qualified.ceylon (67:4-67:40)
            $$$cl1.defineAttr($$testList,'rest',function(){return this.rest$737_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','rest']};});
            
            //AttributeDecl reversed at qualified.ceylon (68:4-68:44)
            $$$cl1.defineAttr($$testList,'reversed',function(){return this.reversed$738_;},undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','reversed']};});
            
            //MethodDef segment at qualified.ceylon (69:4-69:82)
            $$testList.segment=function segment(from$740,length$741){
                var $$testList=this;
                return $$$cl1.getEmpty();
            };$$testList.segment.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'length',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','segment']};};
            
            //MethodDef span at qualified.ceylon (70:4-70:75)
            $$testList.span=function span(from$742,to$743){
                var $$testList=this;
                return $$$cl1.getEmpty();
            };$$testList.span.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','span']};};
            
            //MethodDef spanTo at qualified.ceylon (71:4-71:63)
            $$testList.spanTo=function spanTo(to$744){
                var $$testList=this;
                return $$$cl1.getEmpty();
            };$$testList.spanTo.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','spanTo']};};
            
            //MethodDef spanFrom at qualified.ceylon (72:4-72:67)
            $$testList.spanFrom=function spanFrom(from$745){
                var $$testList=this;
                return $$$cl1.getEmpty();
            };$$testList.spanFrom.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','spanFrom']};};
            
            //MethodDecl iterator at qualified.ceylon (73:4-73:62)
            $$testList.iterator=function (){
                var $$testList=this;
                return $$$cl1.getEmptyIterator();
            };
            $$testList.iterator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.String}}},$ps:[],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','iterator']};};
            
            //MethodDef equals at qualified.ceylon (74:4-74:93)
            $$testList.equals=function equals(that$746){
                var $$testList=this;
                return $$testList.getT$all()['ceylon.language::List'].$$.prototype.equals.call(this,that$746);
            };$$testList.equals.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Boolean},$ps:[{$nm:'that',$mt:'prm',$t:{t:$$$cl1.Object},$an:function(){return[];}}],$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$m','equals']};};
            
            //AttributeGetterDef hash at qualified.ceylon (75:4-75:70)
            $$$cl1.defineAttr($$testList,'hash',function(){
                var $$testList=this;
                return $$$cl1.attrGetter($$testList.getT$all()['ceylon.language::List'],'hash').call(this);
            },undefined,function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:TestList,$an:function(){return[$$$cl1.shared(),$$$cl1.actual()];},d:['invocations','TestList','$at','hash']};});
        })(TestList.$$.prototype);
    }
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDef testQualified at qualified.ceylon (78:0-97:0)
function testQualified(){
    
    //AttributeDecl q1 at qualified.ceylon (79:4-79:47)
    var q1$747=QualifyAmbiguousSupertypes(true);
    
    //AttributeDecl q2 at qualified.ceylon (80:4-80:48)
    var q2$748=QualifyAmbiguousSupertypes(false);
    $$$c2.check(q1$747.doSomething().equals($$$cl1.String("ambiguous 1",11)),$$$cl1.String("qualified super calls [1]",25));
    $$$c2.check(q2$748.doSomething().equals($$$cl1.String("ambiguous 2",11)),$$$cl1.String("qualified super calls [2]",25));
    $$$c2.check(q1$747.whatever.equals((1)),$$$cl1.String("qualified super attrib [1]",26));
    $$$c2.check(q2$748.whatever.equals((2)),$$$cl1.String("qualified super attrib [2]",26));
    $$$c2.check(q1$747.somethingElse((5)).equals($$$cl1.String("Ambiguous1 something 5 else",27)),$$$cl1.String("qualified super method [1]",26));
    $$$c2.check(q1$747.somethingElse((6)).equals($$$cl1.String("something 6 else",16)),$$$cl1.String("qualified super method [2]",26));
    $$$c2.check(q2$748.somethingElse((5)).equals($$$cl1.String("Ambiguous2 5 something else",27)),$$$cl1.String("qualified super method [3]",26));
    $$$c2.check(q2$748.somethingElse((6)).equals($$$cl1.String("Ambiguous2 6 something else",27)),$$$cl1.String("qualified super method [4]",26));
    
    //AttributeDecl qb at qualified.ceylon (89:4-89:27)
    var qb$749=QualifiedB();
    $$$c2.check(qb$749.a.equals(qb$749.supera),$$$cl1.String("Qualified attribute [1]",23));
    qb$749.f();
    $$$c2.check((tmp$750=qb$749,tmp$750.a=tmp$750.a.successor).equals(qb$749.supera),$$$cl1.String("Qualified attribute [2]",23));
    var tmp$750;
    $$$c2.check((tmp$751=qb$749,tmp$751.a=tmp$751.a.successor).equals(qb$749.g()),$$$cl1.String("Qualified attribute [3]",23));
    var tmp$751;
    
    //AttributeDecl tl at qualified.ceylon (94:4-94:25)
    var tl$752=TestList();
    $$$c2.check(tl$752.hash.equals($$$cl1.getEmpty().hash),$$$cl1.String("super of List.hash",18));
    $$$c2.check(tl$752.equals($$$cl1.getEmpty()),$$$cl1.String("super of List.equals",20));
};testQualified.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['invocations','testQualified']};};

//MethodDef spread1 at spread.ceylon (4:0-10:0)
function spread1(a$753){
    if(a$753===undefined){a$753=$$$cl1.getEmpty();}
    
    //AttributeDecl r at spread.ceylon (5:2-5:23)
    var r$754=(0);
    function setR$754(r$755){return r$754=r$755;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$756 = a$753.iterator();
    var i$757;while ((i$757=it$756.next())!==$$$cl1.getFinished()){
        (r$754=r$754.plus(i$757));
    }
    return r$754;
};spread1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}},$an:function(){return[];}}],d:['invocations','spread1']};};

//MethodDef spread2 at spread.ceylon (12:0-18:0)
function spread2(a$758){
    
    //AttributeDecl r at spread.ceylon (13:2-13:23)
    var r$759=(0);
    function setR$759(r$760){return r$759=r$760;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$761 = a$758.iterator();
    var i$762;while ((i$762=it$761.next())!==$$$cl1.getFinished()){
        (r$759=r$759.plus(i$762));
    }
    return r$759;
};spread2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}},$an:function(){return[];}}],d:['invocations','spread2']};};

//MethodDef spread3 at spread.ceylon (19:0-25:0)
function spread3(a$763,b$764){
    if(b$764===undefined){b$764=$$$cl1.getEmpty();}
    
    //AttributeDecl r at spread.ceylon (20:2-20:22)
    var r$765=a$763;
    function setR$765(r$766){return r$765=r$766;};
    //'for' statement at spread.ceylon (21:2-23:2)
    var it$767 = b$764.iterator();
    var i$768;while ((i$768=it$767.next())!==$$$cl1.getFinished()){
        (r$765=r$765.plus(i$768));
    }
    return r$765;
};spread3.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}},$an:function(){return[];}}],d:['invocations','spread3']};};

//MethodDef spread4 at spread.ceylon (26:0-28:0)
function spread4(a$769,b$770,c$771){
    return a$769.plus(b$770).plus(c$771);
};spread4.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'c',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['invocations','spread4']};};

//MethodDef spread5 at spread.ceylon (29:0-31:0)
function spread5(a$772,b$773,c$774,d$775){
    if(d$775===undefined){d$775=(20);}
    return a$772.plus(b$773).plus(c$774).minus(d$775);
};spread5.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'c',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'d',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['invocations','spread5']};};

//MethodDef testSpread at spread.ceylon (33:0-48:0)
function testSpread(){
    
    //AttributeDecl ints at spread.ceylon (34:2-34:23)
    var ints$776=$$$cl1.Tuple((8),$$$cl1.Tuple((9),$$$cl1.Tuple((10),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer},{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals((6)),$$$cl1.String("spread [1]",10));
    $$$c2.check(spread1(ints$776).equals((27)),$$$cl1.String("spread [2]",10));
    $$$c2.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain(ints$776,{Element:{t:$$$cl1.Integer}})).equals((30)),$$$cl1.String("spread [3]",10));
    $$$c2.check(spread1($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (38:16-38:35)
        var it$777=ints$776.iterator();
        var i$778=$$$cl1.getFinished();
        var next$i$778=function(){return i$778=it$777.next();}
        next$i$778();
        return function(){
            if(i$778!==$$$cl1.getFinished()){
                var i$778$779=i$778;
                var tmpvar$780=i$778$779.times((10));
                next$i$778();
                return tmpvar$780;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals((270)),$$$cl1.String("spread [4]",10));
    $$$c2.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (39:20-39:39)
        var it$781=ints$776.iterator();
        var i$782=$$$cl1.getFinished();
        var next$i$782=function(){return i$782=it$781.next();}
        next$i$782();
        return function(){
            if(i$782!==$$$cl1.getFinished()){
                var i$782$783=i$782;
                var tmpvar$784=i$782$783.times((10));
                next$i$782();
                return tmpvar$784;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals((275)),$$$cl1.String("spread [5]",10));
    $$$c2.check((a$785=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),spread2(a$785)).equals((6)),$$$cl1.String("spread [6]",10));
    var a$785;
    $$$c2.check((a$786=ints$776,spread2(a$786)).equals((27)),$$$cl1.String("spread [7]",10));
    var a$786;
    $$$c2.check((a$787=[(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain(ints$776,{Element:{t:$$$cl1.Integer}}),spread2(a$787)).equals((30)),$$$cl1.String("spread [8]",10));
    var a$787;
    $$$c2.check((a$788=$$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (43:16-43:35)
        var it$789=ints$776.iterator();
        var i$790=$$$cl1.getFinished();
        var next$i$790=function(){return i$790=it$789.next();}
        next$i$790();
        return function(){
            if(i$790!==$$$cl1.getFinished()){
                var i$790$791=i$790;
                var tmpvar$792=i$790$791.times((10));
                next$i$790();
                return tmpvar$792;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),spread2(a$788)).equals((270)),$$$cl1.String("spread [9]",10));
    var a$788;
    $$$c2.check((a$793=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (44:20-44:39)
        var it$794=ints$776.iterator();
        var i$795=$$$cl1.getFinished();
        var next$i$795=function(){return i$795=it$794.next();}
        next$i$795();
        return function(){
            if(i$795!==$$$cl1.getFinished()){
                var i$795$796=i$795;
                var tmpvar$797=i$795$796.times((10));
                next$i$795();
                return tmpvar$797;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),spread2(a$793)).equals((275)),$$$cl1.String("spread [10]",11));
    var a$793;
    $$$c2.check(spread3((1),ints$776).equals((28)),$$$cl1.String("spread [11]",11));
    $$$c2.check(spread4(ints$776.$get(0),ints$776.$get(1)||undefined,ints$776.$get(2)||undefined).equals((27)),$$$cl1.String("spread [12]",11));
    $$$c2.check(spread5(ints$776.$get(0),ints$776.$get(1)||undefined,ints$776.$get(2)||undefined,ints$776.$get(3)||undefined).equals((7)),$$$cl1.String("spread [13]",11));
};testSpread.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['invocations','testSpread']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
