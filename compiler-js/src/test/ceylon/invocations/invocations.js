(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}}
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$f597=require('functions/0.1/functions-0.1');
var $$$c2=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$598,nums$599){
    if(nums$599===undefined){nums$599=$$$cl1.getEmpty();}
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("C:",2),(opt$600=chars$598.first,opt$600!==null?opt$600:$$$cl1.String("?",1)).string,$$$cl1.String(" #",2),(opt$601=nums$599.get((0)),opt$601!==null?opt$601:$$$cl1.String("?",1)).string]).string;
};
mixseqs$$metamodel$$={$nm:'mixseqs',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'chars',$mt:'prm',$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}}},{$nm:'nums',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}}}]};
var opt$600,opt$601;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f597.helloWorld();
    ($$$f597.helloWorld());
    $$$f597.hello($$$cl1.String("world",5));
    (name$602=$$$cl1.String("world",5),$$$f597.hello(name$602));
    var name$602;
    $$$f597.helloAll([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}));
    (names$603=$$$cl1.Tuple($$$cl1.String("someone",7),$$$cl1.Tuple($$$cl1.String("someone else",12),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),$$$f597.helloAll(names$603));
    var names$603;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$604=$$$f597.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$605=(obj$606=(99),$$$f597.toString(obj$606));
    var obj$606;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$607=$$$f597.add($$$cl1.Float(1.0),$$$cl1.Float(1.0).negativeValue);
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$608=(x$609=$$$cl1.Float(1.0),y$610=$$$cl1.Float(1.0).negativeValue,$$$f597.add(x$609,y$610));
    var x$609,y$610;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$611(i$612){
        $$$cl1.print(i$612);
    };p$611.$$metamodel$$={$nm:'p',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//p$611.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Anything}};
    $$$f597.repeat((10),$$$cl1.$JsCallable(p$611,[{$nm:'i',$mt:'prm',$t:{t:$$$cl1.Integer}}],{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Anything}}));
    testNamedArguments();
    testQualified();
    $$$c2.check(mixseqs($$$cl1.Tuple($$$cl1.Character(97),$$$cl1.Tuple($$$cl1.Character(98),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:a #1",6)));
    $$$c2.check(mixseqs([$$$cl1.Character(98),$$$cl1.Character(99)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:b #2",6)));
    $$$c2.check(mixseqs($$$cl1.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:h #3",6)));
    $$$c2.check((chars$613=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$614=$$$cl1.String("hola",4).iterator();
        var c$615=$$$cl1.getFinished();
        var next$c$615=function(){return c$615=it$614.next();}
        next$c$615();
        return function(){
            if(c$615!==$$$cl1.getFinished()){
                var c$615$616=c$615;
                var tmpvar$617=c$615$616.uppercased;
                next$c$615();
                return tmpvar$617;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}),mixseqs(chars$613,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$613;
    $$$c2.check((nums$618=$$$cl1.Tuple((2),$$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$619=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$619,nums$618)).equals($$$cl1.String("C:h #2",6)));
    var nums$618,chars$619;
    $$$c2.check((nums$620=$$$cl1.Tuple((4),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$621=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$622=$$$cl1.String("hola",4).iterator();
        var c$623=$$$cl1.getFinished();
        var next$c$623=function(){return c$623=it$622.next();}
        next$c$623();
        return function(){
            if(c$623!==$$$cl1.getFinished()){
                var c$623$624=c$623;
                var tmpvar$625=c$623$624;
                next$c$623();
                return tmpvar$625;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}),mixseqs(chars$621,nums$620)).equals($$$cl1.String("C:h #4",6)));
    var nums$620,chars$621;
    $$$c2.check((chars$626=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$626,$$$cl1.getEmpty())).equals($$$cl1.String("C:h #?",6)));
    var chars$626;
    $$$c2.check((chars$627=[$$$cl1.Character(72),$$$cl1.Character(73)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}),mixseqs(chars$627,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$627;
    testSpread();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$628,desc$629,match$630){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$631 = iter$628.iterator();
    var i$632;while ((i$632=it$631.next())!==$$$cl1.getFinished()){
        if(match$630(i$632)){
            return $$$cl1.StringBuilder().appendAll([desc$629.string,$$$cl1.String(": ",2),i$632.string]).string;
        }
    }
    return $$$cl1.String("[NOT FOUND]",11);
};namedFunc.$$metamodel$$={$nm:'namedFunc',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'iter',$mt:'prm',$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}},{$nm:'desc',$mt:'prm',$t:{t:$$$cl1.String}},{$nm:'match',$mt:'prm',$t:{t:$$$cl1.Boolean}}]};//namedFunc.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Boolean},Element:{t:$$$cl1.Boolean}}},Return:{t:$$$cl1.String}};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$633,count$634,discount$635,comments$636){
    if(count$634===undefined){count$634=(1);}
    if(discount$635===undefined){discount$635=$$$cl1.Float(0.0);}
    if(comments$636===undefined){comments$636=$$$cl1.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$637=(strings$638=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$639=comments$636.iterator();
        var c$640=$$$cl1.getFinished();
        var next$c$640=function(){return c$640=it$639.next();}
        next$c$640();
        return function(){
            if(c$640!==$$$cl1.getFinished()){
                var c$640$641=c$640;
                var tmpvar$642=$$$cl1.StringBuilder().appendAll([$$$cl1.String("\'",1),c$640$641.string,$$$cl1.String("\'",1)]).string;
                next$c$640();
                return tmpvar$642;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}),(opt$643=$$$cl1.String(", ",2),$$$cl1.JsCallable(opt$643,opt$643!==null?opt$643.join:null))(strings$638));
    var strings$638,opt$643;
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Order \'",7),product$633.string,$$$cl1.String("\', quantity ",12),count$634.string,$$$cl1.String(", discount ",11)]).string.plus($$$cl1.StringBuilder().appendAll([discount$635.string,$$$cl1.String(", comments: ",12),commentStr$637.string]).string);
};order.$$metamodel$$={$nm:'order',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'product',$mt:'prm',$t:{t:$$$cl1.String}},{$nm:'count',$mt:'prm',$def:1,$t:{t:$$$cl1.Integer}},{$nm:'discount',$mt:'prm',$def:1,$t:{t:$$$cl1.Float}},{$nm:'comments',$mt:'prm',$def:1,$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}}}]};//order.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}},Element:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}}}},Return:{t:$$$cl1.String}};

//MethodDefinition testNamedArguments at named.ceylon (21:0-63:0)
function testNamedArguments(){
    $$$c2.check((iter$644=(function(){
        //ObjectArgument iter at named.ceylon (23:4-26:4)
        function iter$645(){
            var $$iter$645=new iter$645.$$;
            $$$cl1.Iterable($$iter$645);
            $$$cl1.add_type_arg($$iter$645,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$645,'Element',{t:$$$cl1.Integer});
            
            //MethodDeclaration iterator at named.ceylon (24:6-25:38)
            var iterator=function (){
                return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator();
            };
            iterator$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}},$ps:[]};
            $$iter$645.iterator=iterator;
            return $$iter$645;
        }
        function $init$iter$645(){
            if (iter$645.$$===undefined){
                $$$cl1.initTypeProto(iter$645,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            iter$645.$$.$$metamodel$$={$nm:'iter',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}]};
            return iter$645;
        }
        $init$iter$645();
        return iter$645(new iter$645.$$);
    }()),desc$646=(function(){
        //AttributeArgument desc at named.ceylon (27:4-29:4)
        return $$$cl1.String("Even",4);
    }()),match$647=function (i$648){
        return i$648.remainder((2)).equals((0));
    },namedFunc(iter$644,desc$646,match$647)).equals($$$cl1.String("Even: 8",7)),$$$cl1.String("named arguments 1",17));
    var iter$644,desc$646,match$647;
    $$$c2.check((iter$649=(function(){
        //ObjectArgument iter at named.ceylon (35:4-39:4)
        function iter$650(){
            var $$iter$650=new iter$650.$$;
            $$$cl1.Iterable($$iter$650);
            $$$cl1.add_type_arg($$iter$650,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$650,'Element',{t:$$$cl1.Integer});
            
            //MethodDefinition iterator at named.ceylon (36:6-38:6)
            function iterator(){
                return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator();
            }
            $$iter$650.iterator=iterator;
            iterator.$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}},$ps:[]};//iterator.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.Integer}}}};
            return $$iter$650;
        }
        function $init$iter$650(){
            if (iter$650.$$===undefined){
                $$$cl1.initTypeProto(iter$650,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            iter$650.$$.$$metamodel$$={$nm:'iter',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}]};
            return iter$650;
        }
        $init$iter$650();
        return iter$650(new iter$650.$$);
    }()),desc$651=(function(){
        //AttributeArgument desc at named.ceylon (40:4-42:4)
        return $$$cl1.String("Odd",3);
    }()),match$652=function (x$653){
        return x$653.remainder((2)).equals((1));
    },namedFunc(iter$649,desc$651,match$652)).equals($$$cl1.String("Odd: 9",6)),$$$cl1.String("named arguments 2",17));
    var iter$649,desc$651,match$652;
    $$$c2.check((desc$654=$$$cl1.String("Even",4),match$655=function (x$656){
        return x$656.equals((2));
    },iter$657=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),namedFunc(iter$657,desc$654,match$655)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 3",17));
    var desc$654,match$655,iter$657;
    $$$c2.check((desc$658=$$$cl1.String("Even",4),match$659=function (x$660){
        return x$660.equals((2));
    },iter$661=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (53:4-53:21)
        var it$662=$$$cl1.Range((10),(1),{Element:{t:$$$cl1.Integer}}).iterator();
        var i$663=$$$cl1.getFinished();
        var next$i$663=function(){return i$663=it$662.next();}
        next$i$663();
        return function(){
            if(i$663!==$$$cl1.getFinished()){
                var i$663$664=i$663;
                var tmpvar$665=i$663$664;
                next$i$663();
                return tmpvar$665;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),namedFunc(iter$661,desc$658,match$659)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 4",17));
    var desc$658,match$659,iter$661;
    $$$c2.check((product$666=$$$cl1.String("Mouse",5),order(product$666,undefined,undefined,undefined)).equals($$$cl1.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl1.String("defaulted & sequenced named [1]",31));
    var product$666;
    $$$c2.check((product$667=$$$cl1.String("Rhinoceros",10),discount$668=$$$cl1.Float(10.0),order(product$667,undefined,discount$668,undefined)).equals($$$cl1.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl1.String("defaulted & sequenced named [2]",31));
    var product$667,discount$668;
    $$$c2.check((product$669=$$$cl1.String("Bee",3),count$670=(531),comments$671=[$$$cl1.String("Express delivery",16).valueOf(),$$$cl1.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}),order(product$669,count$670,undefined,comments$671)).equals($$$cl1.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl1.String("defaulted & sequenced named [3]",31));
    var product$669,count$670,comments$671;
};testNamedArguments.$$metamodel$$={$nm:'testNamedArguments',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testNamedArguments.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
    
    //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
    function somethingElse(x$672){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("something ",10),x$672.string,$$$cl1.String(" else",5)]).string;
    }
    $$ambiguousParent.somethingElse=somethingElse;
    somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//somethingElse.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};
}
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl1.initTypeProto(AmbiguousParent,'invocations::AmbiguousParent');
    }
    AmbiguousParent.$$.$$metamodel$$={$nm:'AmbiguousParent',$mt:'ifc','satisfies':[]};
    return AmbiguousParent;
}
exports.$init$AmbiguousParent=$init$AmbiguousParent;
$init$AmbiguousParent();

//InterfaceDefinition Ambiguous1 at qualified.ceylon (11:0-22:0)
function Ambiguous1($$ambiguous1){
    AmbiguousParent($$ambiguous1);
    $$ambiguous1.somethingElse$$invocations$AmbiguousParent=$$ambiguous1.somethingElse;
    
    //MethodDefinition doSomething at qualified.ceylon (12:4-14:4)
    function doSomething(){
        return $$$cl1.String("ambiguous 1",11);
    }
    $$ambiguous1.doSomething=doSomething;
    doSomething.$$metamodel$$={$nm:'doSomething',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//doSomething.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //AttributeGetterDefinition whatever at qualified.ceylon (15:4-15:55)
    $$$cl1.defineAttr($$ambiguous1,'whatever',function(){
        return (1);
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
    function somethingElse(x$673){
        if(x$673.remainder((2)).equals((0))){
            return $$ambiguous1.somethingElse$$invocations$AmbiguousParent(x$673);
        }
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous1 something ",21),x$673.string,$$$cl1.String(" else",5)]).string;
    }
    $$ambiguous1.somethingElse=somethingElse;
    somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//somethingElse.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};
}
function $init$Ambiguous1(){
    if (Ambiguous1.$$===undefined){
        $$$cl1.initTypeProto(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
    }
    Ambiguous1.$$.$$metamodel$$={$nm:'Ambiguous1',$mt:'ifc','satisfies':[{t:AmbiguousParent}]};
    return Ambiguous1;
}
exports.$init$Ambiguous1=$init$Ambiguous1;
$init$Ambiguous1();

//InterfaceDefinition Ambiguous2 at qualified.ceylon (23:0-31:0)
function Ambiguous2($$ambiguous2){
    AmbiguousParent($$ambiguous2);
    
    //MethodDefinition doSomething at qualified.ceylon (24:4-26:4)
    function doSomething(){
        return $$$cl1.String("ambiguous 2",11);
    }
    $$ambiguous2.doSomething=doSomething;
    doSomething.$$metamodel$$={$nm:'doSomething',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//doSomething.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //AttributeGetterDefinition whatever at qualified.ceylon (27:4-27:55)
    $$$cl1.defineAttr($$ambiguous2,'whatever',function(){
        return (2);
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
    function somethingElse(x$674){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous2 ",11),x$674.string,$$$cl1.String(" something else",15)]).string;
    }
    $$ambiguous2.somethingElse=somethingElse;
    somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//somethingElse.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};
}
function $init$Ambiguous2(){
    if (Ambiguous2.$$===undefined){
        $$$cl1.initTypeProto(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
    }
    Ambiguous2.$$.$$metamodel$$={$nm:'Ambiguous2',$mt:'ifc','satisfies':[{t:AmbiguousParent}]};
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDefinition QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$675, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$675=one$675;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    $$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.doSomething;
    $$$cl1.copySuperAttr($$qualifyAmbiguousSupertypes,'whatever','$$invocations$Ambiguous1');
    $$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.somethingElse;
    Ambiguous2($$qualifyAmbiguousSupertypes);
    $$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.doSomething;
    $$$cl1.copySuperAttr($$qualifyAmbiguousSupertypes,'whatever','$$invocations$Ambiguous2');
    $$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.somethingElse;
    
    //MethodDefinition doSomething at qualified.ceylon (35:4-37:4)
    function doSomething(){
        return (opt$676=(one$675?$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1():null),opt$676!==null?opt$676:$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2());
        var opt$676;
    }
    $$qualifyAmbiguousSupertypes.doSomething=doSomething;
    doSomething.$$metamodel$$={$nm:'doSomething',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[]};//doSomething.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.String}};
    
    //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
    $$$cl1.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
        if(one$675){
            return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous1;
        }
        return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous2;
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
    function somethingElse(x$677){
        return (opt$678=(one$675?$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1(x$677):null),opt$678!==null?opt$678:$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2(x$677));
        var opt$678;
    }
    $$qualifyAmbiguousSupertypes.somethingElse=somethingElse;
    somethingElse.$$metamodel$$={$nm:'somethingElse',$mt:'mthd',$t:{t:$$$cl1.String},$ps:[{$nm:'x',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//somethingElse.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.String}};
    return $$qualifyAmbiguousSupertypes;
}
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl1.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl1.Basic,$init$Ambiguous1(),$init$Ambiguous2());
    }
    QualifyAmbiguousSupertypes.$$.$$metamodel$$={$nm:'QualifyAmbiguousSupertypes',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:Ambiguous1},{t:Ambiguous2}]};
    return QualifyAmbiguousSupertypes;
}
exports.$init$QualifyAmbiguousSupertypes=$init$QualifyAmbiguousSupertypes;
$init$QualifyAmbiguousSupertypes();

//ClassDefinition QualifiedA at qualified.ceylon (49:0-51:0)
function QualifiedA($$qualifiedA){
    $init$QualifiedA();
    if ($$qualifiedA===undefined)$$qualifiedA=new QualifiedA.$$;
    
    //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
    var a=(0);
    $$$cl1.defineAttr($$qualifiedA,'a',function(){return a;},function(a$679){return a=a$679;});
    return $$qualifiedA;
}
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl1.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl1.Basic);
    }
    QualifiedA.$$.$$metamodel$$={$nm:'QualifiedA',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return QualifiedA;
}
exports.$init$QualifiedA=$init$QualifiedA;
$init$QualifiedA();

//ClassDefinition QualifiedB at qualified.ceylon (52:0-61:0)
function QualifiedB($$qualifiedB){
    $init$QualifiedB();
    if ($$qualifiedB===undefined)$$qualifiedB=new QualifiedB.$$;
    QualifiedA($$qualifiedB);
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    
    //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
    var a=(0);
    $$$cl1.defineAttr($$qualifiedB,'a',function(){return a;},function(a$680){return a=a$680;});
    
    //MethodDefinition f at qualified.ceylon (54:2-56:2)
    function f(){
        (olda$681=$$qualifiedB.a$$invocations$QualifiedA,$$qualifiedB.a$$invocations$QualifiedA=olda$681.successor,olda$681);
        var olda$681;
    }
    $$qualifiedB.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    
    //MethodDefinition g at qualified.ceylon (57:2-59:2)
    function g(){
        return ($$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a$$invocations$QualifiedA.successor);
    }
    $$qualifiedB.g=g;
    g.$$metamodel$$={$nm:'g',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//g.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //AttributeGetterDefinition supera at qualified.ceylon (60:2-60:48)
    $$$cl1.defineAttr($$qualifiedB,'supera',function(){
        return $$qualifiedB.a$$invocations$QualifiedA;
    });
    return $$qualifiedB;
}
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl1.initTypeProto(QualifiedB,'invocations::QualifiedB',QualifiedA);
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
    $$testList.$$targs$$={Element:{t:$$$cl1.String}};
    $$$cl1.List($$testList);
    $$$cl1.add_type_arg($$testList,'Element',{t:$$$cl1.String});
    $$testList.equals$$ceylon$language$List=$$testList.equals;
    $$$cl1.copySuperAttr($$testList,'hash','$$ceylon$language$List');
    
    //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
    var clone=$$$cl1.getEmpty();
    $$$cl1.defineAttr($$testList,'clone',function(){return clone;});
    
    //MethodDefinition get at qualified.ceylon (65:4-65:60)
    function get(index$682){
        return null;
    }
    $$testList.get=get;
    get.$$metamodel$$={$nm:'get',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.String}]},$ps:[{$nm:'index',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//get.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.String}]}};
    
    //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
    var lastIndex=null;
    $$$cl1.defineAttr($$testList,'lastIndex',function(){return lastIndex;});
    
    //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
    var rest=$$$cl1.getEmpty();
    $$$cl1.defineAttr($$testList,'rest',function(){return rest;});
    
    //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
    var reversed=$$$cl1.getEmpty();
    $$$cl1.defineAttr($$testList,'reversed',function(){return reversed;});
    
    //MethodDefinition segment at qualified.ceylon (69:4-69:82)
    function segment(from$683,length$684){
        return $$$cl1.getEmpty();
    }
    $$testList.segment=segment;
    segment.$$metamodel$$={$nm:'segment',$mt:'mthd',$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'length',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//segment.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}}};
    
    //MethodDefinition span at qualified.ceylon (70:4-70:75)
    function span(from$685,to$686){
        return $$$cl1.getEmpty();
    }
    $$testList.span=span;
    span.$$metamodel$$={$nm:'span',$mt:'mthd',$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}},{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//span.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}}};
    
    //MethodDefinition spanTo at qualified.ceylon (71:4-71:63)
    function spanTo(to$687){
        return $$$cl1.getEmpty();
    }
    $$testList.spanTo=spanTo;
    spanTo.$$metamodel$$={$nm:'spanTo',$mt:'mthd',$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'to',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//spanTo.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}}};
    
    //MethodDefinition spanFrom at qualified.ceylon (72:4-72:67)
    function spanFrom(from$688){
        return $$$cl1.getEmpty();
    }
    $$testList.spanFrom=spanFrom;
    spanFrom.$$metamodel$$={$nm:'spanFrom',$mt:'mthd',$t:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}},$ps:[{$nm:'from',$mt:'prm',$t:{t:$$$cl1.Integer}}]};//spanFrom.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}}};
    
    //MethodDeclaration iterator at qualified.ceylon (73:4-73:62)
    var iterator=function (){
        return $$$cl1.getEmptyIterator();
    };
    iterator$$metamodel$$={$nm:'iterator',$mt:'mthd',$t:{t:$$$cl1.Iterator,a:{Element:{t:$$$cl1.String}}},$ps:[]};
    $$testList.iterator=iterator;
    
    //MethodDefinition equals at qualified.ceylon (74:4-74:75)
    function equals(that$689){
        return $$testList.equals$$ceylon$language$List(that$689);
    }
    $$testList.equals=equals;
    equals.$$metamodel$$={$nm:'equals',$mt:'mthd',$t:{t:$$$cl1.Boolean},$ps:[{$nm:'that',$mt:'prm',$t:{t:$$$cl1.Object}}]};//equals.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Object},Element:{t:$$$cl1.Object}}},Return:{t:$$$cl1.Boolean}};
    
    //AttributeGetterDefinition hash at qualified.ceylon (75:4-75:52)
    $$$cl1.defineAttr($$testList,'hash',function(){
        return $$testList.hash$$ceylon$language$List;
    });
    return $$testList;
}
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl1.initTypeProto(TestList,'invocations::TestList',$$$cl1.Basic,$$$cl1.List);
    }
    TestList.$$.$$metamodel$$={$nm:'TestList',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[{t:$$$cl1.List,a:{Element:{t:$$$cl1.String}}}]};
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDefinition testQualified at qualified.ceylon (78:0-97:0)
function testQualified(){
    
    //AttributeDeclaration q1 at qualified.ceylon (79:4-79:47)
    var q1$690=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (80:4-80:48)
    var q2$691=QualifyAmbiguousSupertypes(false);
    $$$c2.check(q1$690.doSomething().equals($$$cl1.String("ambiguous 1",11)),$$$cl1.String("qualified super calls [1]",25));
    $$$c2.check(q2$691.doSomething().equals($$$cl1.String("ambiguous 2",11)),$$$cl1.String("qualified super calls [2]",25));
    $$$c2.check(q1$690.whatever.equals((1)),$$$cl1.String("qualified super attrib [1]",26));
    $$$c2.check(q2$691.whatever.equals((2)),$$$cl1.String("qualified super attrib [2]",26));
    $$$c2.check(q1$690.somethingElse((5)).equals($$$cl1.String("Ambiguous1 something 5 else",27)),$$$cl1.String("qualified super method [1]",26));
    $$$c2.check(q1$690.somethingElse((6)).equals($$$cl1.String("something 6 else",16)),$$$cl1.String("qualified super method [2]",26));
    $$$c2.check(q2$691.somethingElse((5)).equals($$$cl1.String("Ambiguous2 5 something else",27)),$$$cl1.String("qualified super method [3]",26));
    $$$c2.check(q2$691.somethingElse((6)).equals($$$cl1.String("Ambiguous2 6 something else",27)),$$$cl1.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (89:4-89:27)
    var qb$692=QualifiedB();
    $$$c2.check(qb$692.a.equals(qb$692.supera),$$$cl1.String("Qualified attribute [1]",23));
    qb$692.f();
    $$$c2.check((tmp$693=qb$692,tmp$693.a=tmp$693.a.successor).equals(qb$692.supera),$$$cl1.String("Qualified attribute [2]",23));
    var tmp$693;
    $$$c2.check((tmp$694=qb$692,tmp$694.a=tmp$694.a.successor).equals(qb$692.g()),$$$cl1.String("Qualified attribute [3]",23));
    var tmp$694;
    
    //AttributeDeclaration tl at qualified.ceylon (94:4-94:25)
    var tl$695=TestList();
    $$$c2.check(tl$695.hash.equals($$$cl1.getEmpty().hash),$$$cl1.String("List::hash",10));
    $$$c2.check(tl$695.equals($$$cl1.getEmpty()),$$$cl1.String("List::equals",12));
};testQualified.$$metamodel$$={$nm:'testQualified',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testQualified.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$696){
    if(a$696===undefined){a$696=$$$cl1.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$697=(0);
    var setR$697=function(r$698){return r$697=r$698;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$699 = a$696.iterator();
    var i$700;while ((i$700=it$699.next())!==$$$cl1.getFinished()){
        (r$697=r$697.plus(i$700));
    }
    return r$697;
};spread1.$$metamodel$$={$nm:'spread1',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',seq:1,$t:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}}}]};//spread1.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Sequential,a:{Element:{t:$$$cl1.Integer}}}}},Return:{t:$$$cl1.Integer}};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$701){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$702=(0);
    var setR$702=function(r$703){return r$702=r$703;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$704 = a$701.iterator();
    var i$705;while ((i$705=it$704.next())!==$$$cl1.getFinished()){
        (r$702=r$702.plus(i$705));
    }
    return r$702;
};spread2.$$metamodel$$={$nm:'spread2',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}}]};//spread2.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}},Element:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}}}},Return:{t:$$$cl1.Integer}};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$706=$$$cl1.Tuple((8),$$$cl1.Tuple((9),$$$cl1.Tuple((10),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals((6)),$$$cl1.String("spread [1]",10));
    $$$c2.check(spread1(ints$706).equals((27)),$$$cl1.String("spread [2]",10));
    $$$c2.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain(ints$706,{Element:{t:$$$cl1.Integer}})).equals((30)),$$$cl1.String("spread [3]",10));
    $$$c2.check(spread1($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$707=ints$706.iterator();
        var i$708=$$$cl1.getFinished();
        var next$i$708=function(){return i$708=it$707.next();}
        next$i$708();
        return function(){
            if(i$708!==$$$cl1.getFinished()){
                var i$708$709=i$708;
                var tmpvar$710=i$708$709.times((10));
                next$i$708();
                return tmpvar$710;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals((270)),$$$cl1.String("spread [4]",10));
    $$$c2.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$711=ints$706.iterator();
        var i$712=$$$cl1.getFinished();
        var next$i$712=function(){return i$712=it$711.next();}
        next$i$712();
        return function(){
            if(i$712!==$$$cl1.getFinished()){
                var i$712$713=i$712;
                var tmpvar$714=i$712$713.times((10));
                next$i$712();
                return tmpvar$714;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}})).equals((275)),$$$cl1.String("spread [5]",10));
    $$$c2.check((a$715=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),spread2(a$715)).equals((6)),$$$cl1.String("spread [6]",10));
    var a$715;
    $$$c2.check((a$716=ints$706,spread2(a$716)).equals((27)),$$$cl1.String("spread [7]",10));
    var a$716;
    $$$c2.check((a$717=[(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain(ints$706,{Element:{t:$$$cl1.Integer}}),spread2(a$717)).equals((30)),$$$cl1.String("spread [8]",10));
    var a$717;
    $$$c2.check((a$718=$$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$719=ints$706.iterator();
        var i$720=$$$cl1.getFinished();
        var next$i$720=function(){return i$720=it$719.next();}
        next$i$720();
        return function(){
            if(i$720!==$$$cl1.getFinished()){
                var i$720$721=i$720;
                var tmpvar$722=i$720$721.times((10));
                next$i$720();
                return tmpvar$722;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),spread2(a$718)).equals((270)),$$$cl1.String("spread [9]",10));
    var a$718;
    $$$c2.check((a$723=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$724=ints$706.iterator();
        var i$725=$$$cl1.getFinished();
        var next$i$725=function(){return i$725=it$724.next();}
        next$i$725();
        return function(){
            if(i$725!==$$$cl1.getFinished()){
                var i$725$726=i$725;
                var tmpvar$727=i$725$726.times((10));
                next$i$725();
                return tmpvar$727;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}}),spread2(a$723)).equals((275)),$$$cl1.String("spread [10]",11));
    var a$723;
};testSpread.$$metamodel$$={$nm:'testSpread',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testSpread.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
