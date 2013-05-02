(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$f3201=require('functions/0.1/functions-0.1');
var $$$c2593=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$3202,nums$3203){
    if(nums$3203===undefined){nums$3203=$$$cl2592.getEmpty();}
    return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("C:",2),(opt$3204=chars$3202.first,opt$3204!==null?opt$3204:$$$cl2592.String("?",1)).string,$$$cl2592.String(" #",2),(opt$3205=nums$3203.get((0)),opt$3205!==null?opt$3205:$$$cl2592.String("?",1)).string]).string;
};
mixseqs$$metamodel$$={$nm:'mixseqs',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'chars',$mt:'prm',$t:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}}}},{$nm:'nums',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Integer}}}}]};
var opt$3204,opt$3205;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f3201.helloWorld();
    ($$$f3201.helloWorld());
    $$$f3201.hello($$$cl2592.String("world",5));
    (name$3206=$$$cl2592.String("world",5),$$$f3201.hello(name$3206));
    var name$3206;
    $$$f3201.helloAll([$$$cl2592.String("someone",7),$$$cl2592.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}));
    (names$3207=$$$cl2592.Tuple($$$cl2592.String("someone",7),$$$cl2592.Tuple($$$cl2592.String("someone else",12),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),$$$f3201.helloAll(names$3207));
    var names$3207;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$3208=$$$f3201.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$3209=(obj$3210=(99),$$$f3201.toString(obj$3210));
    var obj$3210;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$3211=$$$f3201.add($$$cl2592.Float(1.0),$$$cl2592.Float(1.0).negativeValue);
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$3212=(x$3213=$$$cl2592.Float(1.0),y$3214=$$$cl2592.Float(1.0).negativeValue,$$$f3201.add(x$3213,y$3214));
    var x$3213,y$3214;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$3215(i$3216){
        $$$cl2592.print(i$3216);
    };p$3215.$$metamodel$$={$nm:'p',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};//p$3215.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Anything}};
    $$$f3201.repeat((10),$$$cl2592.$JsCallable(p$3215,[{$nm:'i',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Anything}}));
    testNamedArguments();
    testQualified();
    $$$c2593.check(mixseqs($$$cl2592.Tuple($$$cl2592.Character(97),$$$cl2592.Tuple($$$cl2592.Character(98),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Character},Element:{t:$$$cl2592.Character}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Character},Element:{t:$$$cl2592.Character}}},First:{t:$$$cl2592.Character},Element:{t:$$$cl2592.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}})).equals($$$cl2592.String("C:a #1",6)));
    $$$c2593.check(mixseqs([$$$cl2592.Character(98),$$$cl2592.Character(99)].reifyCeylonType({Absent:{t:$$$cl2592.Nothing},Element:{t:$$$cl2592.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}})).equals($$$cl2592.String("C:b #2",6)));
    $$$c2593.check(mixseqs($$$cl2592.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}})).equals($$$cl2592.String("C:h #3",6)));
    $$$c2593.check((chars$3217=$$$cl2592.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$3218=$$$cl2592.String("hola",4).iterator();
        var c$3219=$$$cl2592.getFinished();
        var next$c$3219=function(){return c$3219=it$3218.next();}
        next$c$3219();
        return function(){
            if(c$3219!==$$$cl2592.getFinished()){
                var c$3219$3220=c$3219;
                var tmpvar$3221=c$3219$3220.uppercased;
                next$c$3219();
                return tmpvar$3221;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}}),mixseqs(chars$3217,$$$cl2592.getEmpty())).equals($$$cl2592.String("C:H #?",6)));
    var chars$3217;
    $$$c2593.check((nums$3222=$$$cl2592.Tuple((2),$$$cl2592.Tuple((1),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),chars$3223=$$$cl2592.String("hola",4).valueOf(),mixseqs(chars$3223,nums$3222)).equals($$$cl2592.String("C:h #2",6)));
    var nums$3222,chars$3223;
    $$$c2593.check((nums$3224=$$$cl2592.Tuple((4),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),chars$3225=$$$cl2592.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$3226=$$$cl2592.String("hola",4).iterator();
        var c$3227=$$$cl2592.getFinished();
        var next$c$3227=function(){return c$3227=it$3226.next();}
        next$c$3227();
        return function(){
            if(c$3227!==$$$cl2592.getFinished()){
                var c$3227$3228=c$3227;
                var tmpvar$3229=c$3227$3228;
                next$c$3227();
                return tmpvar$3229;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}}),mixseqs(chars$3225,nums$3224)).equals($$$cl2592.String("C:h #4",6)));
    var nums$3224,chars$3225;
    $$$c2593.check((chars$3230=$$$cl2592.String("hola",4).valueOf(),mixseqs(chars$3230,$$$cl2592.getEmpty())).equals($$$cl2592.String("C:h #?",6)));
    var chars$3230;
    $$$c2593.check((chars$3231=[$$$cl2592.Character(72),$$$cl2592.Character(73)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}}),mixseqs(chars$3231,$$$cl2592.getEmpty())).equals($$$cl2592.String("C:H #?",6)));
    var chars$3231;
    testSpread();
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$3232,desc$3233,match$3234){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$3235 = iter$3232.iterator();
    var i$3236;while ((i$3236=it$3235.next())!==$$$cl2592.getFinished()){
        if(match$3234(i$3236)){
            return $$$cl2592.StringBuilder().appendAll([desc$3233.string,$$$cl2592.String(": ",2),i$3236.string]).string;
        }
    }
    return $$$cl2592.String("[NOT FOUND]",11);
};namedFunc.$$metamodel$$={$nm:'namedFunc',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'iter',$mt:'prm',$t:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}}},{$nm:'desc',$mt:'prm',$t:{t:$$$cl2592.String}},{$nm:'match',$mt:'prm',$t:{t:$$$cl2592.Boolean}}]};//namedFunc.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Boolean},Element:{t:$$$cl2592.Boolean}}},Return:{t:$$$cl2592.String}};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$3237,count$3238,discount$3239,comments$3240){
    if(count$3238===undefined){count$3238=(1);}
    if(discount$3239===undefined){discount$3239=$$$cl2592.Float(0.0);}
    if(comments$3240===undefined){comments$3240=$$$cl2592.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$3241=(strings$3242=$$$cl2592.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$3243=comments$3240.iterator();
        var c$3244=$$$cl2592.getFinished();
        var next$c$3244=function(){return c$3244=it$3243.next();}
        next$c$3244();
        return function(){
            if(c$3244!==$$$cl2592.getFinished()){
                var c$3244$3245=c$3244;
                var tmpvar$3246=$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("\'",1),c$3244$3245.string,$$$cl2592.String("\'",1)]).string;
                next$c$3244();
                return tmpvar$3246;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}),(opt$3247=$$$cl2592.String(", ",2),$$$cl2592.JsCallable(opt$3247,opt$3247!==null?opt$3247.join:null))(strings$3242));
    var strings$3242,opt$3247;
    return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("Order \'",7),product$3237.string,$$$cl2592.String("\', quantity ",12),count$3238.string,$$$cl2592.String(", discount ",11)]).string.plus($$$cl2592.StringBuilder().appendAll([discount$3239.string,$$$cl2592.String(", comments: ",12),commentStr$3241.string]).string);
};order.$$metamodel$$={$nm:'order',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'product',$mt:'prm',$t:{t:$$$cl2592.String}},{$nm:'count',$mt:'prm',$def:1,$t:{t:$$$cl2592.Integer}},{$nm:'discount',$mt:'prm',$def:1,$t:{t:$$$cl2592.Float}},{$nm:'comments',$mt:'prm',$def:1,$t:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}}}]};//order.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}},Element:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}}}},Return:{t:$$$cl2592.String}};

//MethodDefinition testNamedArguments at named.ceylon (21:0-63:0)
function testNamedArguments(){
    $$$c2593.check((iter$3248=(function(){
        //ObjectArgument iter at named.ceylon (23:4-26:4)
        function iter$3249(){
            var $$iter$3249=new iter$3249.$$;
            $$$cl2592.Iterable($$iter$3249);
            $$$cl2592.add_type_arg($$iter$3249,'Absent',{t:$$$cl2592.Null});
            $$$cl2592.add_type_arg($$iter$3249,'Element',{t:$$$cl2592.Integer});
            return $$iter$3249;
        }
        function $init$iter$3249(){
            if (iter$3249.$$===undefined){
                $$$cl2592.initTypeProto(iter$3249,'invocations::testNamedArguments.iter',$$$cl2592.Basic,$$$cl2592.Iterable);
                (function($$iter$3249){
                    
                    //MethodDeclaration iterator at named.ceylon (24:6-25:38)
                    $$iter$3249.iterator=function (){
                        var $$iter$3249=this;
                        return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl2592.Nothing},Element:{t:$$$cl2592.Integer}}).iterator();
                    };
                    iterator$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl2592.Iterator,a:{Element:{t:$$$cl2592.Integer}}},$ps:[]};
                })(iter$3249.$$.prototype);
            }
            iter$3249.$$.$$metamodel$$={$nm:'iter',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}}]};
            return iter$3249;
        }
        $init$iter$3249();
        return iter$3249(new iter$3249.$$);
    }()),desc$3250=(function(){
        //AttributeArgument desc at named.ceylon (27:4-29:4)
        return $$$cl2592.String("Even",4);
    }()),match$3251=function (i$3252){
        return i$3252.remainder((2)).equals((0));
    },namedFunc(iter$3248,desc$3250,match$3251)).equals($$$cl2592.String("Even: 8",7)),$$$cl2592.String("named arguments 1",17));
    var iter$3248,desc$3250,match$3251;
    $$$c2593.check((iter$3253=(function(){
        //ObjectArgument iter at named.ceylon (35:4-39:4)
        function iter$3254(){
            var $$iter$3254=new iter$3254.$$;
            $$$cl2592.Iterable($$iter$3254);
            $$$cl2592.add_type_arg($$iter$3254,'Absent',{t:$$$cl2592.Null});
            $$$cl2592.add_type_arg($$iter$3254,'Element',{t:$$$cl2592.Integer});
            return $$iter$3254;
        }
        function $init$iter$3254(){
            if (iter$3254.$$===undefined){
                $$$cl2592.initTypeProto(iter$3254,'invocations::testNamedArguments.iter',$$$cl2592.Basic,$$$cl2592.Iterable);
                (function($$iter$3254){
                    
                    //MethodDefinition iterator at named.ceylon (36:6-38:6)
                    $$iter$3254.iterator=function iterator(){
                        var $$iter$3254=this;
                        return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl2592.Nothing},Element:{t:$$$cl2592.Integer}}).iterator();
                    };$$iter$3254.iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl2592.Iterator,a:{Element:{t:$$$cl2592.Integer}}},$ps:[]};
                })(iter$3254.$$.prototype);
            }
            iter$3254.$$.$$metamodel$$={$nm:'iter',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}}]};
            return iter$3254;
        }
        $init$iter$3254();
        return iter$3254(new iter$3254.$$);
    }()),desc$3255=(function(){
        //AttributeArgument desc at named.ceylon (40:4-42:4)
        return $$$cl2592.String("Odd",3);
    }()),match$3256=function (x$3257){
        return x$3257.remainder((2)).equals((1));
    },namedFunc(iter$3253,desc$3255,match$3256)).equals($$$cl2592.String("Odd: 9",6)),$$$cl2592.String("named arguments 2",17));
    var iter$3253,desc$3255,match$3256;
    $$$c2593.check((desc$3258=$$$cl2592.String("Even",4),match$3259=function (x$3260){
        return x$3260.equals((2));
    },iter$3261=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),namedFunc(iter$3261,desc$3258,match$3259)).equals($$$cl2592.String("Even: 2",7)),$$$cl2592.String("named arguments 3",17));
    var desc$3258,match$3259,iter$3261;
    $$$c2593.check((desc$3262=$$$cl2592.String("Even",4),match$3263=function (x$3264){
        return x$3264.equals((2));
    },iter$3265=$$$cl2592.Comprehension(function(){
        //Comprehension at named.ceylon (53:4-53:21)
        var it$3266=$$$cl2592.Range((10),(1),{Element:{t:$$$cl2592.Integer}}).iterator();
        var i$3267=$$$cl2592.getFinished();
        var next$i$3267=function(){return i$3267=it$3266.next();}
        next$i$3267();
        return function(){
            if(i$3267!==$$$cl2592.getFinished()){
                var i$3267$3268=i$3267;
                var tmpvar$3269=i$3267$3268;
                next$i$3267();
                return tmpvar$3269;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),namedFunc(iter$3265,desc$3262,match$3263)).equals($$$cl2592.String("Even: 2",7)),$$$cl2592.String("named arguments 4",17));
    var desc$3262,match$3263,iter$3265;
    $$$c2593.check((product$3270=$$$cl2592.String("Mouse",5),order(product$3270,undefined,undefined,undefined)).equals($$$cl2592.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl2592.String("defaulted & sequenced named [1]",31));
    var product$3270;
    $$$c2593.check((product$3271=$$$cl2592.String("Rhinoceros",10),discount$3272=$$$cl2592.Float(10.0),order(product$3271,undefined,discount$3272,undefined)).equals($$$cl2592.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl2592.String("defaulted & sequenced named [2]",31));
    var product$3271,discount$3272;
    $$$c2593.check((product$3273=$$$cl2592.String("Bee",3),count$3274=(531),comments$3275=[$$$cl2592.String("Express delivery",16).valueOf(),$$$cl2592.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.String}}),order(product$3273,count$3274,undefined,comments$3275)).equals($$$cl2592.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl2592.String("defaulted & sequenced named [3]",31));
    var product$3273,count$3274,comments$3275;
};testNamedArguments.$$metamodel$$={$nm:'testNamedArguments',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testNamedArguments.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
}
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl2592.initTypeProto(AmbiguousParent,'invocations::AmbiguousParent');
        (function($$ambiguousParent){
            
            //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
            $$ambiguousParent.somethingElse=function somethingElse(x$3276){
                var $$ambiguousParent=this;
                return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("something ",10),x$3276.string,$$$cl2592.String(" else",5)]).string;
            };$$ambiguousParent.somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(AmbiguousParent.$$.prototype);
    }
    AmbiguousParent.$$.$$metamodel$$={$nm:'AmbiguousParent',$mt:'ifc','satisfies':[]};
    return AmbiguousParent;
}
exports.$init$AmbiguousParent=$init$AmbiguousParent;
$init$AmbiguousParent();

//InterfaceDefinition Ambiguous1 at qualified.ceylon (11:0-22:0)
function Ambiguous1($$ambiguous1){
    AmbiguousParent($$ambiguous1);
}
function $init$Ambiguous1(){
    if (Ambiguous1.$$===undefined){
        $$$cl2592.initTypeProto(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
        (function($$ambiguous1){
            
            //MethodDefinition doSomething at qualified.ceylon (12:4-14:4)
            $$ambiguous1.doSomething=function doSomething(){
                var $$ambiguous1=this;
                return $$$cl2592.String("ambiguous 1",11);
            };$$ambiguous1.doSomething.$$metamodel$$={$nm:'doSomething',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            //AttributeGetterDefinition whatever at qualified.ceylon (15:4-15:55)
            $$$cl2592.defineAttr($$ambiguous1,'whatever',function(){
                var $$ambiguous1=this;
                return (1);
            });
            //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
            $$ambiguous1.somethingElse=function somethingElse(x$3277){
                var $$ambiguous1=this;
                if(x$3277.remainder((2)).equals((0))){
                    return $$ambiguous1.getT$all()['invocations::AmbiguousParent'].$$.prototype.somethingElse.call(this,x$3277);
                }
                return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("Ambiguous1 something ",21),x$3277.string,$$$cl2592.String(" else",5)]).string;
            };$$ambiguous1.somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(Ambiguous1.$$.prototype);
    }
    Ambiguous1.$$.$$metamodel$$={$nm:'Ambiguous1',$mt:'ifc','satisfies':[{t:AmbiguousParent}]};
    return Ambiguous1;
}
exports.$init$Ambiguous1=$init$Ambiguous1;
$init$Ambiguous1();

//InterfaceDefinition Ambiguous2 at qualified.ceylon (23:0-31:0)
function Ambiguous2($$ambiguous2){
    AmbiguousParent($$ambiguous2);
}
function $init$Ambiguous2(){
    if (Ambiguous2.$$===undefined){
        $$$cl2592.initTypeProto(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
        (function($$ambiguous2){
            
            //MethodDefinition doSomething at qualified.ceylon (24:4-26:4)
            $$ambiguous2.doSomething=function doSomething(){
                var $$ambiguous2=this;
                return $$$cl2592.String("ambiguous 2",11);
            };$$ambiguous2.doSomething.$$metamodel$$={$nm:'doSomething',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            //AttributeGetterDefinition whatever at qualified.ceylon (27:4-27:55)
            $$$cl2592.defineAttr($$ambiguous2,'whatever',function(){
                var $$ambiguous2=this;
                return (2);
            });
            //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
            $$ambiguous2.somethingElse=function somethingElse(x$3278){
                var $$ambiguous2=this;
                return $$$cl2592.StringBuilder().appendAll([$$$cl2592.String("Ambiguous2 ",11),x$3278.string,$$$cl2592.String(" something else",15)]).string;
            };$$ambiguous2.somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(Ambiguous2.$$.prototype);
    }
    Ambiguous2.$$.$$metamodel$$={$nm:'Ambiguous2',$mt:'ifc','satisfies':[{t:AmbiguousParent}]};
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDefinition QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$3279, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$3279=one$3279;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    Ambiguous2($$qualifyAmbiguousSupertypes);
    return $$qualifyAmbiguousSupertypes;
}
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl2592.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl2592.Basic,$init$Ambiguous1(),$init$Ambiguous2());
        (function($$qualifyAmbiguousSupertypes){
            
            //MethodDefinition doSomething at qualified.ceylon (35:4-37:4)
            $$qualifyAmbiguousSupertypes.doSomething=function doSomething(){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$3280=($$qualifyAmbiguousSupertypes.one$3279?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.doSomething.call(this):null),opt$3280!==null?opt$3280:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.doSomething.call(this));
                var opt$3280;
            };$$qualifyAmbiguousSupertypes.doSomething.$$metamodel$$={$nm:'doSomething',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[]};
            //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
            $$$cl2592.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
                var $$qualifyAmbiguousSupertypes=this;
                if($$qualifyAmbiguousSupertypes.one$3279){
                    return $$$cl2592.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'],'whatever').call(this);
                }
                return $$$cl2592.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'],'whatever').call(this);
            });
            //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
            $$qualifyAmbiguousSupertypes.somethingElse=function somethingElse(x$3281){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$3282=($$qualifyAmbiguousSupertypes.one$3279?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.somethingElse.call(this,x$3281):null),opt$3282!==null?opt$3282:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.somethingElse.call(this,x$3281));
                var opt$3282;
            };$$qualifyAmbiguousSupertypes.somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl2592.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
        })(QualifyAmbiguousSupertypes.$$.prototype);
    }
    QualifyAmbiguousSupertypes.$$.$$metamodel$$={$nm:'QualifyAmbiguousSupertypes',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:Ambiguous1},{t:Ambiguous2}]};
    return QualifyAmbiguousSupertypes;
}
exports.$init$QualifyAmbiguousSupertypes=$init$QualifyAmbiguousSupertypes;
$init$QualifyAmbiguousSupertypes();

//ClassDefinition QualifiedA at qualified.ceylon (49:0-51:0)
function QualifiedA($$qualifiedA){
    $init$QualifiedA();
    if ($$qualifiedA===undefined)$$qualifiedA=new QualifiedA.$$;
    
    //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
    $$qualifiedA.a$3283_=(0);
    return $$qualifiedA;
}
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl2592.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl2592.Basic);
        (function($$qualifiedA){
            
            //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
            $$$cl2592.defineAttr($$qualifiedA,'a',function(){return this.a$3283_;},function(a$3284){return this.a$3283_=a$3284;});
        })(QualifiedA.$$.prototype);
    }
    QualifiedA.$$.$$metamodel$$={$nm:'QualifiedA',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return QualifiedA;
}
exports.$init$QualifiedA=$init$QualifiedA;
$init$QualifiedA();

//ClassDefinition QualifiedB at qualified.ceylon (52:0-61:0)
function QualifiedB($$qualifiedB){
    $init$QualifiedB();
    if ($$qualifiedB===undefined)$$qualifiedB=new QualifiedB.$$;
    QualifiedA($$qualifiedB);
    
    //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
    $$qualifiedB.a$3285_=(0);
    return $$qualifiedB;
}
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl2592.initTypeProto(QualifiedB,'invocations::QualifiedB',QualifiedA);
        (function($$qualifiedB){
            
            //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
            $$$cl2592.defineAttr($$qualifiedB,'a',function(){return this.a$3285_;},function(a$3286){return this.a$3285_=a$3286;});
            
            //MethodDefinition f at qualified.ceylon (54:2-56:2)
            $$qualifiedB.f=function f(){
                var $$qualifiedB=this;
                (olda$3287=$$$cl2592.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this),$$$cl2592.attrSetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this,olda$3287.successor),olda$3287);
                var olda$3287;
            };$$qualifiedB.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};
            //MethodDefinition g at qualified.ceylon (57:2-59:2)
            $$qualifiedB.g=function g(){
                var $$qualifiedB=this;
                return ($$$cl2592.attrSetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this,$$$cl2592.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this).successor));
            };$$qualifiedB.g.$$metamodel$$={$nm:'g',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //AttributeGetterDefinition supera at qualified.ceylon (60:2-60:48)
            $$$cl2592.defineAttr($$qualifiedB,'supera',function(){
                var $$qualifiedB=this;
                return $$$cl2592.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this);
            });
        })(QualifiedB.$$.prototype);
    }
    QualifiedB.$$.$$metamodel$$={$nm:'QualifiedB',$mt:'cls','super':{t:QualifiedA},'satisfies':[]};
    return QualifiedB;
}
exports.$init$QualifiedB=$init$QualifiedB;
$init$QualifiedB();

//ClassDefinition TestList at qualified.ceylon (63:0-76:0)
function TestList($$testList){
    $init$TestList();
    if ($$testList===undefined)$$testList=new TestList.$$;
    $$testList.$$targs$$={Element:{t:$$$cl2592.String}};
    $$$cl2592.List($$testList);
    $$$cl2592.add_type_arg($$testList,'Element',{t:$$$cl2592.String});
    
    //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
    $$testList.clone$3288_=$$$cl2592.getEmpty();
    
    //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
    $$testList.lastIndex$3289_=null;
    
    //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
    $$testList.rest$3290_=$$$cl2592.getEmpty();
    
    //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
    $$testList.reversed$3291_=$$$cl2592.getEmpty();
    return $$testList;
}
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl2592.initTypeProto(TestList,'invocations::TestList',$$$cl2592.Basic,$$$cl2592.List);
        (function($$testList){
            
            //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
            $$$cl2592.defineAttr($$testList,'clone',function(){return this.clone$3288_;});
            
            //MethodDefinition get at qualified.ceylon (65:4-65:60)
            $$testList.get=function get(index$3292){
                var $$testList=this;
                return null;
            };$$testList.get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.String}]},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
            $$$cl2592.defineAttr($$testList,'lastIndex',function(){return this.lastIndex$3289_;});
            
            //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
            $$$cl2592.defineAttr($$testList,'rest',function(){return this.rest$3290_;});
            
            //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
            $$$cl2592.defineAttr($$testList,'reversed',function(){return this.reversed$3291_;});
            
            //MethodDefinition segment at qualified.ceylon (69:4-69:82)
            $$testList.segment=function segment(from$3293,length$3294){
                var $$testList=this;
                return $$$cl2592.getEmpty();
            };$$testList.segment.$$metamodel$$={$nm:'segment',$mt:'mthd',$t:{t:$$$cl2592.List,a:{Element:{t:$$$cl2592.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'length',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition span at qualified.ceylon (70:4-70:75)
            $$testList.span=function span(from$3295,to$3296){
                var $$testList=this;
                return $$$cl2592.getEmpty();
            };$$testList.span.$$metamodel$$={$nm:'span',$mt:'mthd',$t:{t:$$$cl2592.List,a:{Element:{t:$$$cl2592.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'to',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition spanTo at qualified.ceylon (71:4-71:63)
            $$testList.spanTo=function spanTo(to$3297){
                var $$testList=this;
                return $$$cl2592.getEmpty();
            };$$testList.spanTo.$$metamodel$$={$nm:'spanTo',$mt:'mthd',$t:{t:$$$cl2592.List,a:{Element:{t:$$$cl2592.String}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDefinition spanFrom at qualified.ceylon (72:4-72:67)
            $$testList.spanFrom=function spanFrom(from$3298){
                var $$testList=this;
                return $$$cl2592.getEmpty();
            };$$testList.spanFrom.$$metamodel$$={$nm:'spanFrom',$mt:'mthd',$t:{t:$$$cl2592.List,a:{Element:{t:$$$cl2592.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};
            //MethodDeclaration iterator at qualified.ceylon (73:4-73:62)
            $$testList.iterator=function (){
                var $$testList=this;
                return $$$cl2592.getEmptyIterator();
            };
            iterator$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl2592.Iterator,a:{Element:{t:$$$cl2592.String}}},$ps:[]};
            
            //MethodDefinition equals at qualified.ceylon (74:4-74:75)
            $$testList.equals=function equals(that$3299){
                var $$testList=this;
                return $$testList.getT$all()['ceylon.language::List'].$$.prototype.equals.call(this,that$3299);
            };$$testList.equals.$$metamodel$$={$nm:'equals',$mt:'mthd',$t:{t:$$$cl2592.Boolean},$ps:[{$nm:'that',$mt:'prm',$t:{t:$$$cl2592.Object}}]};
            //AttributeGetterDefinition hash at qualified.ceylon (75:4-75:52)
            $$$cl2592.defineAttr($$testList,'hash',function(){
                var $$testList=this;
                return $$$cl2592.attrGetter($$testList.getT$all()['ceylon.language::List'],'hash').call(this);
            });
        })(TestList.$$.prototype);
    }
    TestList.$$.$$metamodel$$={$nm:'TestList',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[{t:$$$cl2592.List,a:{Element:{t:$$$cl2592.String}}}]};
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDefinition testQualified at qualified.ceylon (78:0-97:0)
function testQualified(){
    
    //AttributeDeclaration q1 at qualified.ceylon (79:4-79:47)
    var q1$3300=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (80:4-80:48)
    var q2$3301=QualifyAmbiguousSupertypes(false);
    $$$c2593.check(q1$3300.doSomething().equals($$$cl2592.String("ambiguous 1",11)),$$$cl2592.String("qualified super calls [1]",25));
    $$$c2593.check(q2$3301.doSomething().equals($$$cl2592.String("ambiguous 2",11)),$$$cl2592.String("qualified super calls [2]",25));
    $$$c2593.check(q1$3300.whatever.equals((1)),$$$cl2592.String("qualified super attrib [1]",26));
    $$$c2593.check(q2$3301.whatever.equals((2)),$$$cl2592.String("qualified super attrib [2]",26));
    $$$c2593.check(q1$3300.somethingElse((5)).equals($$$cl2592.String("Ambiguous1 something 5 else",27)),$$$cl2592.String("qualified super method [1]",26));
    $$$c2593.check(q1$3300.somethingElse((6)).equals($$$cl2592.String("something 6 else",16)),$$$cl2592.String("qualified super method [2]",26));
    $$$c2593.check(q2$3301.somethingElse((5)).equals($$$cl2592.String("Ambiguous2 5 something else",27)),$$$cl2592.String("qualified super method [3]",26));
    $$$c2593.check(q2$3301.somethingElse((6)).equals($$$cl2592.String("Ambiguous2 6 something else",27)),$$$cl2592.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (89:4-89:27)
    var qb$3302=QualifiedB();
    $$$c2593.check(qb$3302.a.equals(qb$3302.supera),$$$cl2592.String("Qualified attribute [1]",23));
    qb$3302.f();
    $$$c2593.check((tmp$3303=qb$3302,tmp$3303.a=tmp$3303.a.successor).equals(qb$3302.supera),$$$cl2592.String("Qualified attribute [2]",23));
    var tmp$3303;
    $$$c2593.check((tmp$3304=qb$3302,tmp$3304.a=tmp$3304.a.successor).equals(qb$3302.g()),$$$cl2592.String("Qualified attribute [3]",23));
    var tmp$3304;
    
    //AttributeDeclaration tl at qualified.ceylon (94:4-94:25)
    var tl$3305=TestList();
    $$$c2593.check(tl$3305.hash.equals($$$cl2592.getEmpty().hash),$$$cl2592.String("List::hash",10));
    $$$c2593.check(tl$3305.equals($$$cl2592.getEmpty()),$$$cl2592.String("List::equals",12));
};testQualified.$$metamodel$$={$nm:'testQualified',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testQualified.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$3306){
    if(a$3306===undefined){a$3306=$$$cl2592.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$3307=(0);
    var setR$3307=function(r$3308){return r$3307=r$3308;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$3309 = a$3306.iterator();
    var i$3310;while ((i$3310=it$3309.next())!==$$$cl2592.getFinished()){
        (r$3307=r$3307.plus(i$3310));
    }
    return r$3307;
};spread1.$$metamodel$$={$nm:'spread1',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'a',$mt:'prm',seq:1,$t:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Integer}}}}]};//spread1.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Sequential,a:{Element:{t:$$$cl2592.Integer}}}}},Return:{t:$$$cl2592.Integer}};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$3311){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$3312=(0);
    var setR$3312=function(r$3313){return r$3312=r$3313;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$3314 = a$3311.iterator();
    var i$3315;while ((i$3315=it$3314.next())!==$$$cl2592.getFinished()){
        (r$3312=r$3312.plus(i$3315));
    }
    return r$3312;
};spread2.$$metamodel$$={$nm:'spread2',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}}}]};//spread2.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}},Element:{t:$$$cl2592.Iterable,a:{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}}}},Return:{t:$$$cl2592.Integer}};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$3316=$$$cl2592.Tuple((8),$$$cl2592.Tuple((9),$$$cl2592.Tuple((10),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}});
    $$$c2593.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}})).equals((6)),$$$cl2592.String("spread [1]",10));
    $$$c2593.check(spread1(ints$3316).equals((27)),$$$cl2592.String("spread [2]",10));
    $$$c2593.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}).chain(ints$3316,{Element:{t:$$$cl2592.Integer}})).equals((30)),$$$cl2592.String("spread [3]",10));
    $$$c2593.check(spread1($$$cl2592.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$3317=ints$3316.iterator();
        var i$3318=$$$cl2592.getFinished();
        var next$i$3318=function(){return i$3318=it$3317.next();}
        next$i$3318();
        return function(){
            if(i$3318!==$$$cl2592.getFinished()){
                var i$3318$3319=i$3318;
                var tmpvar$3320=i$3318$3319.times((10));
                next$i$3318();
                return tmpvar$3320;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}})).equals((270)),$$$cl2592.String("spread [4]",10));
    $$$c2593.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}).chain($$$cl2592.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$3321=ints$3316.iterator();
        var i$3322=$$$cl2592.getFinished();
        var next$i$3322=function(){return i$3322=it$3321.next();}
        next$i$3322();
        return function(){
            if(i$3322!==$$$cl2592.getFinished()){
                var i$3322$3323=i$3322;
                var tmpvar$3324=i$3322$3323.times((10));
                next$i$3322();
                return tmpvar$3324;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}})).equals((275)),$$$cl2592.String("spread [5]",10));
    $$$c2593.check((a$3325=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),spread2(a$3325)).equals((6)),$$$cl2592.String("spread [6]",10));
    var a$3325;
    $$$c2593.check((a$3326=ints$3316,spread2(a$3326)).equals((27)),$$$cl2592.String("spread [7]",10));
    var a$3326;
    $$$c2593.check((a$3327=[(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}).chain(ints$3316,{Element:{t:$$$cl2592.Integer}}),spread2(a$3327)).equals((30)),$$$cl2592.String("spread [8]",10));
    var a$3327;
    $$$c2593.check((a$3328=$$$cl2592.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$3329=ints$3316.iterator();
        var i$3330=$$$cl2592.getFinished();
        var next$i$3330=function(){return i$3330=it$3329.next();}
        next$i$3330();
        return function(){
            if(i$3330!==$$$cl2592.getFinished()){
                var i$3330$3331=i$3330;
                var tmpvar$3332=i$3330$3331.times((10));
                next$i$3330();
                return tmpvar$3332;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),spread2(a$3328)).equals((270)),$$$cl2592.String("spread [9]",10));
    var a$3328;
    $$$c2593.check((a$3333=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}).chain($$$cl2592.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$3334=ints$3316.iterator();
        var i$3335=$$$cl2592.getFinished();
        var next$i$3335=function(){return i$3335=it$3334.next();}
        next$i$3335();
        return function(){
            if(i$3335!==$$$cl2592.getFinished()){
                var i$3335$3336=i$3335;
                var tmpvar$3337=i$3335$3336.times((10));
                next$i$3335();
                return tmpvar$3337;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}}),spread2(a$3333)).equals((275)),$$$cl2592.String("spread [10]",11));
    var a$3333;
};testSpread.$$metamodel$$={$nm:'testSpread',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testSpread.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
