(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}};
var $$$cl2381=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$f2988=require('functions/0.1/functions-0.1');
var $$$c2382=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$2989,nums$2990){
    if(nums$2990===undefined){nums$2990=$$$cl2381.getEmpty();}
    return $$$cl2381.StringBuilder().appendAll([$$$cl2381.String("C:",2),(opt$2991=chars$2989.first,opt$2991!==null?opt$2991:$$$cl2381.String("?",1)).string,$$$cl2381.String(" #",2),(opt$2992=nums$2990.get((0)),opt$2992!==null?opt$2992:$$$cl2381.String("?",1)).string]).string;
};
var opt$2991,opt$2992;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f2988.helloWorld();
    ($$$f2988.helloWorld());
    $$$f2988.hello($$$cl2381.String("world",5));
    (name$2993=$$$cl2381.String("world",5),$$$f2988.hello(name$2993));
    var name$2993;
    $$$f2988.helloAll([$$$cl2381.String("someone",7),$$$cl2381.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}));
    (names$2994=$$$cl2381.Tuple($$$cl2381.String("someone",7),$$$cl2381.Tuple($$$cl2381.String("someone else",12),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),$$$f2988.helloAll(names$2994));
    var names$2994;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$2995=$$$f2988.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$2996=(obj$2997=(99),$$$f2988.toString(obj$2997));
    var obj$2997;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$2998=$$$f2988.add($$$cl2381.Float(1.0),$$$cl2381.Float(1.0).negativeValue);
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$2999=(x$3000=$$$cl2381.Float(1.0),y$3001=$$$cl2381.Float(1.0).negativeValue,$$$f2988.add(x$3000,y$3001));
    var x$3000,y$3001;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$3002(i$3003){
        $$$cl2381.print(i$3003);
    };
    $$$f2988.repeat((10),$$$cl2381.$JsCallable(p$3002,{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Anything}}));
    testNamedArguments();
    testQualified();
    $$$c2382.check(mixseqs($$$cl2381.Tuple($$$cl2381.Character(97),$$$cl2381.Tuple($$$cl2381.Character(98),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Character},Element:{t:$$$cl2381.Character}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Character},Element:{t:$$$cl2381.Character}}},First:{t:$$$cl2381.Character},Element:{t:$$$cl2381.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}})).equals($$$cl2381.String("C:a #1",6)));
    $$$c2382.check(mixseqs([$$$cl2381.Character(98),$$$cl2381.Character(99)].reifyCeylonType({Absent:{t:$$$cl2381.Nothing},Element:{t:$$$cl2381.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}})).equals($$$cl2381.String("C:b #2",6)));
    $$$c2382.check(mixseqs($$$cl2381.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}})).equals($$$cl2381.String("C:h #3",6)));
    $$$c2382.check((chars$3004=$$$cl2381.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$3005=$$$cl2381.String("hola",4).iterator();
        var c$3006=$$$cl2381.getFinished();
        var next$c$3006=function(){return c$3006=it$3005.next();}
        next$c$3006();
        return function(){
            if(c$3006!==$$$cl2381.getFinished()){
                var c$3006$3007=c$3006;
                var tmpvar$3008=c$3006$3007.uppercased;
                next$c$3006();
                return tmpvar$3008;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Character}}),mixseqs(chars$3004,$$$cl2381.getEmpty())).equals($$$cl2381.String("C:H #?",6)));
    var chars$3004;
    $$$c2382.check((nums$3009=$$$cl2381.Tuple((2),$$$cl2381.Tuple((1),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),chars$3010=$$$cl2381.String("hola",4).valueOf(),mixseqs(chars$3010,nums$3009)).equals($$$cl2381.String("C:h #2",6)));
    var nums$3009,chars$3010;
    $$$c2382.check((nums$3011=$$$cl2381.Tuple((4),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),chars$3012=$$$cl2381.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$3013=$$$cl2381.String("hola",4).iterator();
        var c$3014=$$$cl2381.getFinished();
        var next$c$3014=function(){return c$3014=it$3013.next();}
        next$c$3014();
        return function(){
            if(c$3014!==$$$cl2381.getFinished()){
                var c$3014$3015=c$3014;
                var tmpvar$3016=c$3014$3015;
                next$c$3014();
                return tmpvar$3016;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Character}}),mixseqs(chars$3012,nums$3011)).equals($$$cl2381.String("C:h #4",6)));
    var nums$3011,chars$3012;
    $$$c2382.check((chars$3017=$$$cl2381.String("hola",4).valueOf(),mixseqs(chars$3017,$$$cl2381.getEmpty())).equals($$$cl2381.String("C:h #?",6)));
    var chars$3017;
    $$$c2382.check((chars$3018=[$$$cl2381.Character(72),$$$cl2381.Character(73)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Character}}),mixseqs(chars$3018,$$$cl2381.getEmpty())).equals($$$cl2381.String("C:H #?",6)));
    var chars$3018;
    testSpread();
    $$$c2382.results();
}
exports.test=test;

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$3019,desc$3020,match$3021){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$3022 = iter$3019.iterator();
    var i$3023;while ((i$3023=it$3022.next())!==$$$cl2381.getFinished()){
        if(match$3021(i$3023)){
            return $$$cl2381.StringBuilder().appendAll([desc$3020.string,$$$cl2381.String(": ",2),i$3023.string]).string;
        }
    }
    return $$$cl2381.String("[NOT FOUND]",11);
};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$3024,count$3025,discount$3026,comments$3027){
    if(count$3025===undefined){count$3025=(1);}
    if(discount$3026===undefined){discount$3026=$$$cl2381.Float(0.0);}
    if(comments$3027===undefined){comments$3027=$$$cl2381.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$3028=(strings$3029=$$$cl2381.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$3030=comments$3027.iterator();
        var c$3031=$$$cl2381.getFinished();
        var next$c$3031=function(){return c$3031=it$3030.next();}
        next$c$3031();
        return function(){
            if(c$3031!==$$$cl2381.getFinished()){
                var c$3031$3032=c$3031;
                var tmpvar$3033=$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("\'",1),c$3031$3032.string,$$$cl2381.String("\'",1)]).string;
                next$c$3031();
                return tmpvar$3033;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}),(opt$3034=$$$cl2381.String(", ",2),$$$cl2381.JsCallable(opt$3034,opt$3034!==null?opt$3034.join:null))(strings$3029));
    var strings$3029,opt$3034;
    return $$$cl2381.StringBuilder().appendAll([$$$cl2381.String("Order \'",7),product$3024.string,$$$cl2381.String("\', quantity ",12),count$3025.string,$$$cl2381.String(", discount ",11)]).string.plus($$$cl2381.StringBuilder().appendAll([discount$3026.string,$$$cl2381.String(", comments: ",12),commentStr$3028.string]).string);
};

//MethodDefinition testNamedArguments at named.ceylon (21:0-63:0)
function testNamedArguments(){
    $$$c2382.check((iter$3035=(function(){
        //ObjectArgument iter at named.ceylon (23:4-26:4)
        function iter$3036(){
            var $$iter$3036=new iter$3036.$$;
            $$$cl2381.Iterable($$iter$3036);
            $$$cl2381.add_type_arg($$iter$3036,'Absent',{t:$$$cl2381.Null});
            $$$cl2381.add_type_arg($$iter$3036,'Element',{t:$$$cl2381.Integer});
            return $$iter$3036;
        }
        function $init$iter$3036(){
            if (iter$3036.$$===undefined){
                $$$cl2381.initTypeProto(iter$3036,'invocations::testNamedArguments.iter',$$$cl2381.Basic,$$$cl2381.Iterable);
                (function($$iter$3036){
                    
                    //MethodDeclaration iterator at named.ceylon (24:6-25:38)
                    $$iter$3036.iterator=function (){
                        var $$iter$3036=this;
                        return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl2381.Nothing},Element:{t:$$$cl2381.Integer}}).iterator();
                    };
                })(iter$3036.$$.prototype);
            }
            return iter$3036;
        }
        $init$iter$3036();
        return iter$3036(new iter$3036.$$);
    }()),desc$3037=(function(){
        //AttributeArgument desc at named.ceylon (27:4-29:4)
        return $$$cl2381.String("Even",4);
    }()),match$3038=function (i$3039){
        return i$3039.remainder((2)).equals((0));
    },namedFunc(iter$3035,desc$3037,match$3038)).equals($$$cl2381.String("Even: 8",7)),$$$cl2381.String("named arguments 1",17));
    var iter$3035,desc$3037,match$3038;
    $$$c2382.check((iter$3040=(function(){
        //ObjectArgument iter at named.ceylon (35:4-39:4)
        function iter$3041(){
            var $$iter$3041=new iter$3041.$$;
            $$$cl2381.Iterable($$iter$3041);
            $$$cl2381.add_type_arg($$iter$3041,'Absent',{t:$$$cl2381.Null});
            $$$cl2381.add_type_arg($$iter$3041,'Element',{t:$$$cl2381.Integer});
            return $$iter$3041;
        }
        function $init$iter$3041(){
            if (iter$3041.$$===undefined){
                $$$cl2381.initTypeProto(iter$3041,'invocations::testNamedArguments.iter',$$$cl2381.Basic,$$$cl2381.Iterable);
                (function($$iter$3041){
                    
                    //MethodDefinition iterator at named.ceylon (36:6-38:6)
                    $$iter$3041.iterator=function iterator(){
                        var $$iter$3041=this;
                        return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl2381.Nothing},Element:{t:$$$cl2381.Integer}}).iterator();
                    };
                })(iter$3041.$$.prototype);
            }
            return iter$3041;
        }
        $init$iter$3041();
        return iter$3041(new iter$3041.$$);
    }()),desc$3042=(function(){
        //AttributeArgument desc at named.ceylon (40:4-42:4)
        return $$$cl2381.String("Odd",3);
    }()),match$3043=function (x$3044){
        return x$3044.remainder((2)).equals((1));
    },namedFunc(iter$3040,desc$3042,match$3043)).equals($$$cl2381.String("Odd: 9",6)),$$$cl2381.String("named arguments 2",17));
    var iter$3040,desc$3042,match$3043;
    $$$c2382.check((desc$3045=$$$cl2381.String("Even",4),match$3046=function (x$3047){
        return x$3047.equals((2));
    },iter$3048=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),namedFunc(iter$3048,desc$3045,match$3046)).equals($$$cl2381.String("Even: 2",7)),$$$cl2381.String("named arguments 3",17));
    var desc$3045,match$3046,iter$3048;
    $$$c2382.check((desc$3049=$$$cl2381.String("Even",4),match$3050=function (x$3051){
        return x$3051.equals((2));
    },iter$3052=$$$cl2381.Comprehension(function(){
        //Comprehension at named.ceylon (53:4-53:21)
        var it$3053=$$$cl2381.Range((10),(1),{Element:{t:$$$cl2381.Integer}}).iterator();
        var i$3054=$$$cl2381.getFinished();
        var next$i$3054=function(){return i$3054=it$3053.next();}
        next$i$3054();
        return function(){
            if(i$3054!==$$$cl2381.getFinished()){
                var i$3054$3055=i$3054;
                var tmpvar$3056=i$3054$3055;
                next$i$3054();
                return tmpvar$3056;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),namedFunc(iter$3052,desc$3049,match$3050)).equals($$$cl2381.String("Even: 2",7)),$$$cl2381.String("named arguments 4",17));
    var desc$3049,match$3050,iter$3052;
    $$$c2382.check((product$3057=$$$cl2381.String("Mouse",5),order(product$3057,undefined,undefined,undefined)).equals($$$cl2381.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl2381.String("defaulted & sequenced named [1]",31));
    var product$3057;
    $$$c2382.check((product$3058=$$$cl2381.String("Rhinoceros",10),discount$3059=$$$cl2381.Float(10.0),order(product$3058,undefined,discount$3059,undefined)).equals($$$cl2381.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl2381.String("defaulted & sequenced named [2]",31));
    var product$3058,discount$3059;
    $$$c2382.check((product$3060=$$$cl2381.String("Bee",3),count$3061=(531),comments$3062=[$$$cl2381.String("Express delivery",16).valueOf(),$$$cl2381.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.String}}),order(product$3060,count$3061,undefined,comments$3062)).equals($$$cl2381.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl2381.String("defaulted & sequenced named [3]",31));
    var product$3060,count$3061,comments$3062;
};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
}
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl2381.initTypeProto(AmbiguousParent,'invocations::AmbiguousParent');
        (function($$ambiguousParent){
            
            //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
            $$ambiguousParent.somethingElse=function somethingElse(x$3063){
                var $$ambiguousParent=this;
                return $$$cl2381.StringBuilder().appendAll([$$$cl2381.String("something ",10),x$3063.string,$$$cl2381.String(" else",5)]).string;
            };
        })(AmbiguousParent.$$.prototype);
    }
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
        $$$cl2381.initTypeProto(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
        (function($$ambiguous1){
            
            //MethodDefinition doSomething at qualified.ceylon (12:4-14:4)
            $$ambiguous1.doSomething=function doSomething(){
                var $$ambiguous1=this;
                return $$$cl2381.String("ambiguous 1",11);
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (15:4-15:55)
            $$$cl2381.defineAttr($$ambiguous1,'whatever',function(){
                var $$ambiguous1=this;
                return (1);
            });
            //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
            $$ambiguous1.somethingElse=function somethingElse(x$3064){
                var $$ambiguous1=this;
                if(x$3064.remainder((2)).equals((0))){
                    return $$ambiguous1.getT$all()['invocations::AmbiguousParent'].$$.prototype.somethingElse.call(this,x$3064);
                }
                return $$$cl2381.StringBuilder().appendAll([$$$cl2381.String("Ambiguous1 something ",21),x$3064.string,$$$cl2381.String(" else",5)]).string;
            };
        })(Ambiguous1.$$.prototype);
    }
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
        $$$cl2381.initTypeProto(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
        (function($$ambiguous2){
            
            //MethodDefinition doSomething at qualified.ceylon (24:4-26:4)
            $$ambiguous2.doSomething=function doSomething(){
                var $$ambiguous2=this;
                return $$$cl2381.String("ambiguous 2",11);
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (27:4-27:55)
            $$$cl2381.defineAttr($$ambiguous2,'whatever',function(){
                var $$ambiguous2=this;
                return (2);
            });
            //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
            $$ambiguous2.somethingElse=function somethingElse(x$3065){
                var $$ambiguous2=this;
                return $$$cl2381.StringBuilder().appendAll([$$$cl2381.String("Ambiguous2 ",11),x$3065.string,$$$cl2381.String(" something else",15)]).string;
            };
        })(Ambiguous2.$$.prototype);
    }
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDefinition QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$3066, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$3066=one$3066;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    Ambiguous2($$qualifyAmbiguousSupertypes);
    return $$qualifyAmbiguousSupertypes;
}
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl2381.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl2381.Basic,$init$Ambiguous1(),$init$Ambiguous2());
        (function($$qualifyAmbiguousSupertypes){
            
            //MethodDefinition doSomething at qualified.ceylon (35:4-37:4)
            $$qualifyAmbiguousSupertypes.doSomething=function doSomething(){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$3067=($$qualifyAmbiguousSupertypes.one$3066?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.doSomething.call(this):null),opt$3067!==null?opt$3067:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.doSomething.call(this));
                var opt$3067;
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
            $$$cl2381.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
                var $$qualifyAmbiguousSupertypes=this;
                if($$qualifyAmbiguousSupertypes.one$3066){
                    return $$$cl2381.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'],'whatever').call(this);
                }
                return $$$cl2381.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'],'whatever').call(this);
            });
            //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
            $$qualifyAmbiguousSupertypes.somethingElse=function somethingElse(x$3068){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$3069=($$qualifyAmbiguousSupertypes.one$3066?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.somethingElse.call(this,x$3068):null),opt$3069!==null?opt$3069:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.somethingElse.call(this,x$3068));
                var opt$3069;
            };
        })(QualifyAmbiguousSupertypes.$$.prototype);
    }
    return QualifyAmbiguousSupertypes;
}
exports.$init$QualifyAmbiguousSupertypes=$init$QualifyAmbiguousSupertypes;
$init$QualifyAmbiguousSupertypes();

//ClassDefinition QualifiedA at qualified.ceylon (49:0-51:0)
function QualifiedA($$qualifiedA){
    $init$QualifiedA();
    if ($$qualifiedA===undefined)$$qualifiedA=new QualifiedA.$$;
    
    //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
    $$qualifiedA.a$3070_=(0);
    return $$qualifiedA;
}
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl2381.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl2381.Basic);
        (function($$qualifiedA){
            
            //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
            $$$cl2381.defineAttr($$qualifiedA,'a',function(){return this.a$3070_;},function(a$3071){return this.a$3070_=a$3071;});
        })(QualifiedA.$$.prototype);
    }
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
    $$qualifiedB.a$3072_=(0);
    return $$qualifiedB;
}
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl2381.initTypeProto(QualifiedB,'invocations::QualifiedB',QualifiedA);
        (function($$qualifiedB){
            
            //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
            $$$cl2381.defineAttr($$qualifiedB,'a',function(){return this.a$3072_;},function(a$3073){return this.a$3072_=a$3073;});
            
            //MethodDefinition f at qualified.ceylon (54:2-56:2)
            $$qualifiedB.f=function f(){
                var $$qualifiedB=this;
                (olda$3074=$$$cl2381.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this),$$$cl2381.attrSetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this,olda$3074.successor),olda$3074);
                var olda$3074;
            };
            //MethodDefinition g at qualified.ceylon (57:2-59:2)
            $$qualifiedB.g=function g(){
                var $$qualifiedB=this;
                return ($$$cl2381.attrSetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this,$$$cl2381.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this).successor));
            };
            //AttributeGetterDefinition supera at qualified.ceylon (60:2-60:48)
            $$$cl2381.defineAttr($$qualifiedB,'supera',function(){
                var $$qualifiedB=this;
                return $$$cl2381.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this);
            });
        })(QualifiedB.$$.prototype);
    }
    return QualifiedB;
}
exports.$init$QualifiedB=$init$QualifiedB;
$init$QualifiedB();

//ClassDefinition TestList at qualified.ceylon (63:0-76:0)
function TestList($$testList){
    $init$TestList();
    if ($$testList===undefined)$$testList=new TestList.$$;
    $$testList.$$targs$$={Element:{t:$$$cl2381.String}};
    $$$cl2381.List($$testList);
    $$$cl2381.add_type_arg($$testList,'Element',{t:$$$cl2381.String});
    
    //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
    $$testList.clone$3075_=$$$cl2381.getEmpty();
    
    //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
    $$testList.lastIndex$3076_=null;
    
    //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
    $$testList.rest$3077_=$$$cl2381.getEmpty();
    
    //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
    $$testList.reversed$3078_=$$$cl2381.getEmpty();
    return $$testList;
}
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl2381.initTypeProto(TestList,'invocations::TestList',$$$cl2381.Basic,$$$cl2381.List);
        (function($$testList){
            
            //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
            $$$cl2381.defineAttr($$testList,'clone',function(){return this.clone$3075_;});
            
            //MethodDefinition get at qualified.ceylon (65:4-65:60)
            $$testList.get=function get(index$3079){
                var $$testList=this;
                return null;
            };
            //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
            $$$cl2381.defineAttr($$testList,'lastIndex',function(){return this.lastIndex$3076_;});
            
            //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
            $$$cl2381.defineAttr($$testList,'rest',function(){return this.rest$3077_;});
            
            //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
            $$$cl2381.defineAttr($$testList,'reversed',function(){return this.reversed$3078_;});
            
            //MethodDefinition segment at qualified.ceylon (69:4-69:82)
            $$testList.segment=function segment(from$3080,length$3081){
                var $$testList=this;
                return $$$cl2381.getEmpty();
            };
            //MethodDefinition span at qualified.ceylon (70:4-70:75)
            $$testList.span=function span(from$3082,to$3083){
                var $$testList=this;
                return $$$cl2381.getEmpty();
            };
            //MethodDefinition spanTo at qualified.ceylon (71:4-71:63)
            $$testList.spanTo=function spanTo(to$3084){
                var $$testList=this;
                return $$$cl2381.getEmpty();
            };
            //MethodDefinition spanFrom at qualified.ceylon (72:4-72:67)
            $$testList.spanFrom=function spanFrom(from$3085){
                var $$testList=this;
                return $$$cl2381.getEmpty();
            };
            //MethodDeclaration iterator at qualified.ceylon (73:4-73:62)
            $$testList.iterator=function (){
                var $$testList=this;
                return $$$cl2381.getEmptyIterator();
            };
            
            //MethodDefinition equals at qualified.ceylon (74:4-74:75)
            $$testList.equals=function equals(that$3086){
                var $$testList=this;
                return $$testList.getT$all()['ceylon.language::List'].$$.prototype.equals.call(this,that$3086);
            };
            //AttributeGetterDefinition hash at qualified.ceylon (75:4-75:52)
            $$$cl2381.defineAttr($$testList,'hash',function(){
                var $$testList=this;
                return $$$cl2381.attrGetter($$testList.getT$all()['ceylon.language::List'],'hash').call(this);
            });
        })(TestList.$$.prototype);
    }
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDefinition testQualified at qualified.ceylon (78:0-97:0)
function testQualified(){
    
    //AttributeDeclaration q1 at qualified.ceylon (79:4-79:47)
    var q1$3087=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (80:4-80:48)
    var q2$3088=QualifyAmbiguousSupertypes(false);
    $$$c2382.check(q1$3087.doSomething().equals($$$cl2381.String("ambiguous 1",11)),$$$cl2381.String("qualified super calls [1]",25));
    $$$c2382.check(q2$3088.doSomething().equals($$$cl2381.String("ambiguous 2",11)),$$$cl2381.String("qualified super calls [2]",25));
    $$$c2382.check(q1$3087.whatever.equals((1)),$$$cl2381.String("qualified super attrib [1]",26));
    $$$c2382.check(q2$3088.whatever.equals((2)),$$$cl2381.String("qualified super attrib [2]",26));
    $$$c2382.check(q1$3087.somethingElse((5)).equals($$$cl2381.String("Ambiguous1 something 5 else",27)),$$$cl2381.String("qualified super method [1]",26));
    $$$c2382.check(q1$3087.somethingElse((6)).equals($$$cl2381.String("something 6 else",16)),$$$cl2381.String("qualified super method [2]",26));
    $$$c2382.check(q2$3088.somethingElse((5)).equals($$$cl2381.String("Ambiguous2 5 something else",27)),$$$cl2381.String("qualified super method [3]",26));
    $$$c2382.check(q2$3088.somethingElse((6)).equals($$$cl2381.String("Ambiguous2 6 something else",27)),$$$cl2381.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (89:4-89:27)
    var qb$3089=QualifiedB();
    $$$c2382.check(qb$3089.a.equals(qb$3089.supera),$$$cl2381.String("Qualified attribute [1]",23));
    qb$3089.f();
    $$$c2382.check((tmp$3090=qb$3089,tmp$3090.a=tmp$3090.a.successor).equals(qb$3089.supera),$$$cl2381.String("Qualified attribute [2]",23));
    var tmp$3090;
    $$$c2382.check((tmp$3091=qb$3089,tmp$3091.a=tmp$3091.a.successor).equals(qb$3089.g()),$$$cl2381.String("Qualified attribute [3]",23));
    var tmp$3091;
    
    //AttributeDeclaration tl at qualified.ceylon (94:4-94:25)
    var tl$3092=TestList();
    $$$c2382.check(tl$3092.hash.equals($$$cl2381.getEmpty().hash),$$$cl2381.String("List::hash",10));
    $$$c2382.check(tl$3092.equals($$$cl2381.getEmpty()),$$$cl2381.String("List::equals",12));
};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$3093){
    if(a$3093===undefined){a$3093=$$$cl2381.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$3094=(0);
    var setR$3094=function(r$3095){return r$3094=r$3095;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$3096 = a$3093.iterator();
    var i$3097;while ((i$3097=it$3096.next())!==$$$cl2381.getFinished()){
        (r$3094=r$3094.plus(i$3097));
    }
    return r$3094;
};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$3098){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$3099=(0);
    var setR$3099=function(r$3100){return r$3099=r$3100;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$3101 = a$3098.iterator();
    var i$3102;while ((i$3102=it$3101.next())!==$$$cl2381.getFinished()){
        (r$3099=r$3099.plus(i$3102));
    }
    return r$3099;
};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$3103=$$$cl2381.Tuple((8),$$$cl2381.Tuple((9),$$$cl2381.Tuple((10),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}});
    $$$c2382.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}})).equals((6)),$$$cl2381.String("spread [1]",10));
    $$$c2382.check(spread1(ints$3103).equals((27)),$$$cl2381.String("spread [2]",10));
    $$$c2382.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}).chain(ints$3103,{Element:{t:$$$cl2381.Integer}})).equals((30)),$$$cl2381.String("spread [3]",10));
    $$$c2382.check(spread1($$$cl2381.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$3104=ints$3103.iterator();
        var i$3105=$$$cl2381.getFinished();
        var next$i$3105=function(){return i$3105=it$3104.next();}
        next$i$3105();
        return function(){
            if(i$3105!==$$$cl2381.getFinished()){
                var i$3105$3106=i$3105;
                var tmpvar$3107=i$3105$3106.times((10));
                next$i$3105();
                return tmpvar$3107;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}})).equals((270)),$$$cl2381.String("spread [4]",10));
    $$$c2382.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}).chain($$$cl2381.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$3108=ints$3103.iterator();
        var i$3109=$$$cl2381.getFinished();
        var next$i$3109=function(){return i$3109=it$3108.next();}
        next$i$3109();
        return function(){
            if(i$3109!==$$$cl2381.getFinished()){
                var i$3109$3110=i$3109;
                var tmpvar$3111=i$3109$3110.times((10));
                next$i$3109();
                return tmpvar$3111;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}})).equals((275)),$$$cl2381.String("spread [5]",10));
    $$$c2382.check((a$3112=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),spread2(a$3112)).equals((6)),$$$cl2381.String("spread [6]",10));
    var a$3112;
    $$$c2382.check((a$3113=ints$3103,spread2(a$3113)).equals((27)),$$$cl2381.String("spread [7]",10));
    var a$3113;
    $$$c2382.check((a$3114=[(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}).chain(ints$3103,{Element:{t:$$$cl2381.Integer}}),spread2(a$3114)).equals((30)),$$$cl2381.String("spread [8]",10));
    var a$3114;
    $$$c2382.check((a$3115=$$$cl2381.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$3116=ints$3103.iterator();
        var i$3117=$$$cl2381.getFinished();
        var next$i$3117=function(){return i$3117=it$3116.next();}
        next$i$3117();
        return function(){
            if(i$3117!==$$$cl2381.getFinished()){
                var i$3117$3118=i$3117;
                var tmpvar$3119=i$3117$3118.times((10));
                next$i$3117();
                return tmpvar$3119;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),spread2(a$3115)).equals((270)),$$$cl2381.String("spread [9]",10));
    var a$3115;
    $$$c2382.check((a$3120=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}).chain($$$cl2381.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$3121=ints$3103.iterator();
        var i$3122=$$$cl2381.getFinished();
        var next$i$3122=function(){return i$3122=it$3121.next();}
        next$i$3122();
        return function(){
            if(i$3122!==$$$cl2381.getFinished()){
                var i$3122$3123=i$3122;
                var tmpvar$3124=i$3122$3123.times((10));
                next$i$3122();
                return tmpvar$3124;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Integer}}),spread2(a$3120)).equals((275)),$$$cl2381.String("spread [10]",11));
    var a$3120;
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
