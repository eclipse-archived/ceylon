(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}};
var $$$cl2309=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$f2899=require('functions/0.1/functions-0.1');
var $$$c2310=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$2900,nums$2901){
    if(nums$2901===undefined){nums$2901=$$$cl2309.getEmpty();}
    return $$$cl2309.StringBuilder().appendAll([$$$cl2309.String("C:",2),(opt$2902=chars$2900.first,opt$2902!==null?opt$2902:$$$cl2309.String("?",1)).string,$$$cl2309.String(" #",2),(opt$2903=nums$2901.get((0)),opt$2903!==null?opt$2903:$$$cl2309.String("?",1)).string]).string;
};
var opt$2902,opt$2903;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f2899.helloWorld();
    ($$$f2899.helloWorld());
    $$$f2899.hello($$$cl2309.String("world",5));
    (name$2904=$$$cl2309.String("world",5),$$$f2899.hello(name$2904));
    var name$2904;
    $$$f2899.helloAll([$$$cl2309.String("someone",7),$$$cl2309.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}));
    (names$2905=$$$cl2309.Tuple($$$cl2309.String("someone",7),$$$cl2309.Tuple($$$cl2309.String("someone else",12),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),$$$f2899.helloAll(names$2905));
    var names$2905;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$2906=$$$f2899.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$2907=(obj$2908=(99),$$$f2899.toString(obj$2908));
    var obj$2908;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$2909=$$$f2899.add($$$cl2309.Float(1.0),$$$cl2309.Float(1.0).negativeValue);
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$2910=(x$2911=$$$cl2309.Float(1.0),y$2912=$$$cl2309.Float(1.0).negativeValue,$$$f2899.add(x$2911,y$2912));
    var x$2911,y$2912;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$2913(i$2914){
        $$$cl2309.print(i$2914);
    };
    $$$f2899.repeat((10),p$2913);
    testNamedArguments();
    testQualified();
    $$$c2310.check(mixseqs($$$cl2309.Tuple($$$cl2309.Character(97),$$$cl2309.Tuple($$$cl2309.Character(98),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Character},Element:{t:$$$cl2309.Character}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Character},Element:{t:$$$cl2309.Character}}},First:{t:$$$cl2309.Character},Element:{t:$$$cl2309.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}})).equals($$$cl2309.String("C:a #1",6)));
    $$$c2310.check(mixseqs([$$$cl2309.Character(98),$$$cl2309.Character(99)].reifyCeylonType({Absent:{t:$$$cl2309.Nothing},Element:{t:$$$cl2309.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}})).equals($$$cl2309.String("C:b #2",6)));
    $$$c2310.check(mixseqs($$$cl2309.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}})).equals($$$cl2309.String("C:h #3",6)));
    $$$c2310.check((chars$2915=$$$cl2309.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$2916=$$$cl2309.String("hola",4).iterator;
        var c$2917=$$$cl2309.getFinished();
        var next$c$2917=function(){return c$2917=it$2916.next();}
        next$c$2917();
        return function(){
            if(c$2917!==$$$cl2309.getFinished()){
                var c$2917$2918=c$2917;
                var tmpvar$2919=c$2917$2918.uppercased;
                next$c$2917();
                return tmpvar$2919;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Character}}),mixseqs(chars$2915,$$$cl2309.getEmpty())).equals($$$cl2309.String("C:H #?",6)));
    var chars$2915;
    $$$c2310.check((nums$2920=$$$cl2309.Tuple((2),$$$cl2309.Tuple((1),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),chars$2921=$$$cl2309.String("hola",4).valueOf(),mixseqs(chars$2921,nums$2920)).equals($$$cl2309.String("C:h #2",6)));
    var nums$2920,chars$2921;
    $$$c2310.check((nums$2922=$$$cl2309.Tuple((4),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),chars$2923=$$$cl2309.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$2924=$$$cl2309.String("hola",4).iterator;
        var c$2925=$$$cl2309.getFinished();
        var next$c$2925=function(){return c$2925=it$2924.next();}
        next$c$2925();
        return function(){
            if(c$2925!==$$$cl2309.getFinished()){
                var c$2925$2926=c$2925;
                var tmpvar$2927=c$2925$2926;
                next$c$2925();
                return tmpvar$2927;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Character}}),mixseqs(chars$2923,nums$2922)).equals($$$cl2309.String("C:h #4",6)));
    var nums$2922,chars$2923;
    $$$c2310.check((chars$2928=$$$cl2309.String("hola",4).valueOf(),mixseqs(chars$2928,$$$cl2309.getEmpty())).equals($$$cl2309.String("C:h #?",6)));
    var chars$2928;
    $$$c2310.check((chars$2929=[$$$cl2309.Character(72),$$$cl2309.Character(73)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Character}}),mixseqs(chars$2929,$$$cl2309.getEmpty())).equals($$$cl2309.String("C:H #?",6)));
    var chars$2929;
    testSpread();
    $$$c2310.results();
}
exports.test=test;

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$2930,desc$2931,match$2932){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$2933 = iter$2930.iterator;
    var i$2934;while ((i$2934=it$2933.next())!==$$$cl2309.getFinished()){
        if(match$2932(i$2934)){
            return $$$cl2309.StringBuilder().appendAll([desc$2931.string,$$$cl2309.String(": ",2),i$2934.string]).string;
        }
    }
    return $$$cl2309.String("[NOT FOUND]",11);
};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$2935,count$2936,discount$2937,comments$2938){
    if(count$2936===undefined){count$2936=(1);}
    if(discount$2937===undefined){discount$2937=$$$cl2309.Float(0.0);}
    if(comments$2938===undefined){comments$2938=$$$cl2309.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$2939=$$$cl2309.String(", ",2).join($$$cl2309.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$2940=comments$2938.iterator;
        var c$2941=$$$cl2309.getFinished();
        var next$c$2941=function(){return c$2941=it$2940.next();}
        next$c$2941();
        return function(){
            if(c$2941!==$$$cl2309.getFinished()){
                var c$2941$2942=c$2941;
                var tmpvar$2943=$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("\'",1),c$2941$2942.string,$$$cl2309.String("\'",1)]).string;
                next$c$2941();
                return tmpvar$2943;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}));
    return $$$cl2309.StringBuilder().appendAll([$$$cl2309.String("Order \'",7),product$2935.string,$$$cl2309.String("\', quantity ",12),count$2936.string,$$$cl2309.String(", discount ",11)]).string.plus($$$cl2309.StringBuilder().appendAll([discount$2937.string,$$$cl2309.String(", comments: ",12),commentStr$2939.string]).string);
};

//MethodDefinition testNamedArguments at named.ceylon (21:0-64:0)
function testNamedArguments(){
    $$$c2310.check((iter$2944=(function(){
        //ObjectArgument iter at named.ceylon (23:4-27:4)
        function iter$2945(){
            var $$iter$2945=new iter$2945.$$;
            $$$cl2309.Iterable($$iter$2945);
            $$$cl2309.add_type_arg($$iter$2945,'Absent',{t:$$$cl2309.Null});
            $$$cl2309.add_type_arg($$iter$2945,'Element',{t:$$$cl2309.Integer});
            return $$iter$2945;
        }
        function $init$iter$2945(){
            if (iter$2945.$$===undefined){
                $$$cl2309.initTypeProto(iter$2945,'invocations::testNamedArguments.iter',$$$cl2309.Basic,$$$cl2309.Iterable);
                (function($$iter$2945){
                    
                    //AttributeGetterDefinition iterator at named.ceylon (24:6-26:6)
                    $$$cl2309.defineAttr($$iter$2945,'iterator',function(){
                        var $$iter$2945=this;
                        return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl2309.Nothing},Element:{t:$$$cl2309.Integer}}).iterator;
                    });
                })(iter$2945.$$.prototype);
            }
            return iter$2945;
        }
        $init$iter$2945();
        return iter$2945(new iter$2945.$$);
    }()),desc$2946=(function(){
        //AttributeArgument desc at named.ceylon (28:4-30:4)
        return $$$cl2309.String("Even",4);
    }()),match$2947=function (i$2948){
        return i$2948.remainder((2)).equals((0));
    },namedFunc(iter$2944,desc$2946,match$2947)).equals($$$cl2309.String("Even: 8",7)),$$$cl2309.String("named arguments 1",17));
    var iter$2944,desc$2946,match$2947;
    $$$c2310.check((iter$2949=(function(){
        //ObjectArgument iter at named.ceylon (36:4-40:4)
        function iter$2950(){
            var $$iter$2950=new iter$2950.$$;
            $$$cl2309.Iterable($$iter$2950);
            $$$cl2309.add_type_arg($$iter$2950,'Absent',{t:$$$cl2309.Null});
            $$$cl2309.add_type_arg($$iter$2950,'Element',{t:$$$cl2309.Integer});
            return $$iter$2950;
        }
        function $init$iter$2950(){
            if (iter$2950.$$===undefined){
                $$$cl2309.initTypeProto(iter$2950,'invocations::testNamedArguments.iter',$$$cl2309.Basic,$$$cl2309.Iterable);
                (function($$iter$2950){
                    
                    //AttributeGetterDefinition iterator at named.ceylon (37:6-39:6)
                    $$$cl2309.defineAttr($$iter$2950,'iterator',function(){
                        var $$iter$2950=this;
                        return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl2309.Nothing},Element:{t:$$$cl2309.Integer}}).iterator;
                    });
                })(iter$2950.$$.prototype);
            }
            return iter$2950;
        }
        $init$iter$2950();
        return iter$2950(new iter$2950.$$);
    }()),desc$2951=(function(){
        //AttributeArgument desc at named.ceylon (41:4-43:4)
        return $$$cl2309.String("Odd",3);
    }()),match$2952=function (x$2953){
        return x$2953.remainder((2)).equals((1));
    },namedFunc(iter$2949,desc$2951,match$2952)).equals($$$cl2309.String("Odd: 9",6)),$$$cl2309.String("named arguments 2",17));
    var iter$2949,desc$2951,match$2952;
    $$$c2310.check((desc$2954=$$$cl2309.String("Even",4),match$2955=function (x$2956){
        return x$2956.equals((2));
    },iter$2957=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),namedFunc(iter$2957,desc$2954,match$2955)).equals($$$cl2309.String("Even: 2",7)),$$$cl2309.String("named arguments 3",17));
    var desc$2954,match$2955,iter$2957;
    $$$c2310.check((desc$2958=$$$cl2309.String("Even",4),match$2959=function (x$2960){
        return x$2960.equals((2));
    },iter$2961=$$$cl2309.Comprehension(function(){
        //Comprehension at named.ceylon (54:4-54:21)
        var it$2962=$$$cl2309.Range((10),(1),{Element:{t:$$$cl2309.Integer}}).iterator;
        var i$2963=$$$cl2309.getFinished();
        var next$i$2963=function(){return i$2963=it$2962.next();}
        next$i$2963();
        return function(){
            if(i$2963!==$$$cl2309.getFinished()){
                var i$2963$2964=i$2963;
                var tmpvar$2965=i$2963$2964;
                next$i$2963();
                return tmpvar$2965;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),namedFunc(iter$2961,desc$2958,match$2959)).equals($$$cl2309.String("Even: 2",7)),$$$cl2309.String("named arguments 4",17));
    var desc$2958,match$2959,iter$2961;
    $$$c2310.check((product$2966=$$$cl2309.String("Mouse",5),order(product$2966,undefined,undefined,undefined)).equals($$$cl2309.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl2309.String("defaulted & sequenced named [1]",31));
    var product$2966;
    $$$c2310.check((product$2967=$$$cl2309.String("Rhinoceros",10),discount$2968=$$$cl2309.Float(10.0),order(product$2967,undefined,discount$2968,undefined)).equals($$$cl2309.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl2309.String("defaulted & sequenced named [2]",31));
    var product$2967,discount$2968;
    $$$c2310.check((product$2969=$$$cl2309.String("Bee",3),count$2970=(531),comments$2971=[$$$cl2309.String("Express delivery",16).valueOf(),$$$cl2309.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.String}}),order(product$2969,count$2970,undefined,comments$2971)).equals($$$cl2309.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl2309.String("defaulted & sequenced named [3]",31));
    var product$2969,count$2970,comments$2971;
};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
}
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl2309.initTypeProto(AmbiguousParent,'invocations::AmbiguousParent');
        (function($$ambiguousParent){
            
            //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
            $$ambiguousParent.somethingElse=function somethingElse(x$2972){
                var $$ambiguousParent=this;
                return $$$cl2309.StringBuilder().appendAll([$$$cl2309.String("something ",10),x$2972.string,$$$cl2309.String(" else",5)]).string;
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
        $$$cl2309.initTypeProto(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
        (function($$ambiguous1){
            
            //MethodDefinition doSomething at qualified.ceylon (12:4-14:4)
            $$ambiguous1.doSomething=function doSomething(){
                var $$ambiguous1=this;
                return $$$cl2309.String("ambiguous 1",11);
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (15:4-15:55)
            $$$cl2309.defineAttr($$ambiguous1,'whatever',function(){
                var $$ambiguous1=this;
                return (1);
            });
            //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
            $$ambiguous1.somethingElse=function somethingElse(x$2973){
                var $$ambiguous1=this;
                if(x$2973.remainder((2)).equals((0))){
                    return $$ambiguous1.getT$all()['invocations::AmbiguousParent'].$$.prototype.somethingElse.call(this,x$2973);
                }
                return $$$cl2309.StringBuilder().appendAll([$$$cl2309.String("Ambiguous1 something ",21),x$2973.string,$$$cl2309.String(" else",5)]).string;
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
        $$$cl2309.initTypeProto(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
        (function($$ambiguous2){
            
            //MethodDefinition doSomething at qualified.ceylon (24:4-26:4)
            $$ambiguous2.doSomething=function doSomething(){
                var $$ambiguous2=this;
                return $$$cl2309.String("ambiguous 2",11);
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (27:4-27:55)
            $$$cl2309.defineAttr($$ambiguous2,'whatever',function(){
                var $$ambiguous2=this;
                return (2);
            });
            //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
            $$ambiguous2.somethingElse=function somethingElse(x$2974){
                var $$ambiguous2=this;
                return $$$cl2309.StringBuilder().appendAll([$$$cl2309.String("Ambiguous2 ",11),x$2974.string,$$$cl2309.String(" something else",15)]).string;
            };
        })(Ambiguous2.$$.prototype);
    }
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDefinition QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$2975, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$2975=one$2975;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    Ambiguous2($$qualifyAmbiguousSupertypes);
    return $$qualifyAmbiguousSupertypes;
}
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl2309.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl2309.Basic,$init$Ambiguous1(),$init$Ambiguous2());
        (function($$qualifyAmbiguousSupertypes){
            
            //MethodDefinition doSomething at qualified.ceylon (35:4-37:4)
            $$qualifyAmbiguousSupertypes.doSomething=function doSomething(){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$2976=($$qualifyAmbiguousSupertypes.one$2975?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.doSomething.call(this):null),opt$2976!==null?opt$2976:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.doSomething.call(this));
                var opt$2976;
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
            $$$cl2309.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
                var $$qualifyAmbiguousSupertypes=this;
                if($$qualifyAmbiguousSupertypes.one$2975){
                    return $$$cl2309.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'],'whatever').call(this);
                }
                return $$$cl2309.attrGetter($$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'],'whatever').call(this);
            });
            //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
            $$qualifyAmbiguousSupertypes.somethingElse=function somethingElse(x$2977){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$2978=($$qualifyAmbiguousSupertypes.one$2975?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.somethingElse.call(this,x$2977):null),opt$2978!==null?opt$2978:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.somethingElse.call(this,x$2977));
                var opt$2978;
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
    $$qualifiedA.a$2979_=(0);
    return $$qualifiedA;
}
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl2309.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl2309.Basic);
        (function($$qualifiedA){
            
            //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
            $$$cl2309.defineAttr($$qualifiedA,'a',function(){return this.a$2979_;},function(a$2980){return this.a$2979_=a$2980;});
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
    $$qualifiedB.a$2981_=(0);
    return $$qualifiedB;
}
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl2309.initTypeProto(QualifiedB,'invocations::QualifiedB',QualifiedA);
        (function($$qualifiedB){
            
            //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
            $$$cl2309.defineAttr($$qualifiedB,'a',function(){return this.a$2981_;},function(a$2982){return this.a$2981_=a$2982;});
            
            //MethodDefinition f at qualified.ceylon (54:2-56:2)
            $$qualifiedB.f=function f(){
                var $$qualifiedB=this;
                (olda$2983=$$$cl2309.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this),$$$cl2309.attrSetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this,olda$2983.successor),olda$2983);
                var olda$2983;
            };
            //MethodDefinition g at qualified.ceylon (57:2-59:2)
            $$qualifiedB.g=function g(){
                var $$qualifiedB=this;
                return ($$$cl2309.attrSetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this,$$$cl2309.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this).successor),$$$cl2309.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this));
            };
            //AttributeGetterDefinition supera at qualified.ceylon (60:2-60:48)
            $$$cl2309.defineAttr($$qualifiedB,'supera',function(){
                var $$qualifiedB=this;
                return $$$cl2309.attrGetter($$qualifiedB.getT$all()['invocations::QualifiedA'],'a').call(this);
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
    $$testList.$$targs$$={Element:{t:$$$cl2309.String}};
    $$$cl2309.List($$testList);
    $$$cl2309.add_type_arg($$testList,'Element',{t:$$$cl2309.String});
    
    //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
    $$testList.clone$2984_=$$$cl2309.getEmpty();
    
    //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
    $$testList.lastIndex$2985_=null;
    
    //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
    $$testList.rest$2986_=$$$cl2309.getEmpty();
    
    //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
    $$testList.reversed$2987_=$$$cl2309.getEmpty();
    
    //AttributeDeclaration iterator at qualified.ceylon (73:4-73:59)
    $$testList.iterator$2988_=$$$cl2309.getEmptyIterator();
    return $$testList;
}
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl2309.initTypeProto(TestList,'invocations::TestList',$$$cl2309.Basic,$$$cl2309.List);
        (function($$testList){
            
            //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
            $$$cl2309.defineAttr($$testList,'clone',function(){return this.clone$2984_;});
            
            //MethodDefinition get at qualified.ceylon (65:4-65:60)
            $$testList.get=function get(index$2989){
                var $$testList=this;
                return null;
            };
            //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
            $$$cl2309.defineAttr($$testList,'lastIndex',function(){return this.lastIndex$2985_;});
            
            //AttributeDeclaration rest at qualified.ceylon (67:4-67:40)
            $$$cl2309.defineAttr($$testList,'rest',function(){return this.rest$2986_;});
            
            //AttributeDeclaration reversed at qualified.ceylon (68:4-68:44)
            $$$cl2309.defineAttr($$testList,'reversed',function(){return this.reversed$2987_;});
            
            //MethodDefinition segment at qualified.ceylon (69:4-69:82)
            $$testList.segment=function segment(from$2990,length$2991){
                var $$testList=this;
                return $$$cl2309.getEmpty();
            };
            //MethodDefinition span at qualified.ceylon (70:4-70:75)
            $$testList.span=function span(from$2992,to$2993){
                var $$testList=this;
                return $$$cl2309.getEmpty();
            };
            //MethodDefinition spanTo at qualified.ceylon (71:4-71:63)
            $$testList.spanTo=function spanTo(to$2994){
                var $$testList=this;
                return $$$cl2309.getEmpty();
            };
            //MethodDefinition spanFrom at qualified.ceylon (72:4-72:67)
            $$testList.spanFrom=function spanFrom(from$2995){
                var $$testList=this;
                return $$$cl2309.getEmpty();
            };
            //AttributeDeclaration iterator at qualified.ceylon (73:4-73:59)
            $$$cl2309.defineAttr($$testList,'iterator',function(){return this.iterator$2988_;});
            
            //MethodDefinition equals at qualified.ceylon (74:4-74:75)
            $$testList.equals=function equals(that$2996){
                var $$testList=this;
                return $$testList.getT$all()['ceylon.language::List'].$$.prototype.equals.call(this,that$2996);
            };
            //AttributeGetterDefinition hash at qualified.ceylon (75:4-75:52)
            $$$cl2309.defineAttr($$testList,'hash',function(){
                var $$testList=this;
                return $$$cl2309.attrGetter($$testList.getT$all()['ceylon.language::List'],'hash').call(this);
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
    var q1$2997=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (80:4-80:48)
    var q2$2998=QualifyAmbiguousSupertypes(false);
    $$$c2310.check(q1$2997.doSomething().equals($$$cl2309.String("ambiguous 1",11)),$$$cl2309.String("qualified super calls [1]",25));
    $$$c2310.check(q2$2998.doSomething().equals($$$cl2309.String("ambiguous 2",11)),$$$cl2309.String("qualified super calls [2]",25));
    $$$c2310.check(q1$2997.whatever.equals((1)),$$$cl2309.String("qualified super attrib [1]",26));
    $$$c2310.check(q2$2998.whatever.equals((2)),$$$cl2309.String("qualified super attrib [2]",26));
    $$$c2310.check(q1$2997.somethingElse((5)).equals($$$cl2309.String("Ambiguous1 something 5 else",27)),$$$cl2309.String("qualified super method [1]",26));
    $$$c2310.check(q1$2997.somethingElse((6)).equals($$$cl2309.String("something 6 else",16)),$$$cl2309.String("qualified super method [2]",26));
    $$$c2310.check(q2$2998.somethingElse((5)).equals($$$cl2309.String("Ambiguous2 5 something else",27)),$$$cl2309.String("qualified super method [3]",26));
    $$$c2310.check(q2$2998.somethingElse((6)).equals($$$cl2309.String("Ambiguous2 6 something else",27)),$$$cl2309.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (89:4-89:27)
    var qb$2999=QualifiedB();
    $$$c2310.check(qb$2999.a.equals(qb$2999.supera),$$$cl2309.String("Qualified attribute [1]",23));
    qb$2999.f();
    $$$c2310.check((tmp$3000=qb$2999,tmp$3000.a=tmp$3000.a.successor,tmp$3000.a).equals(qb$2999.supera),$$$cl2309.String("Qualified attribute [2]",23));
    var tmp$3000;
    $$$c2310.check((tmp$3001=qb$2999,tmp$3001.a=tmp$3001.a.successor,tmp$3001.a).equals(qb$2999.g()),$$$cl2309.String("Qualified attribute [3]",23));
    var tmp$3001;
    
    //AttributeDeclaration tl at qualified.ceylon (94:4-94:25)
    var tl$3002=TestList();
    $$$c2310.check(tl$3002.hash.equals($$$cl2309.getEmpty().hash),$$$cl2309.String("List::hash",10));
    $$$c2310.check(tl$3002.equals($$$cl2309.getEmpty()),$$$cl2309.String("List::equals",12));
};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$3003){
    if(a$3003===undefined){a$3003=$$$cl2309.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$3004=(0);
    var setR$3004=function(r$3005){return r$3004=r$3005;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$3006 = a$3003.iterator;
    var i$3007;while ((i$3007=it$3006.next())!==$$$cl2309.getFinished()){
        (r$3004=r$3004.plus(i$3007),r$3004);
    }
    return r$3004;
};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$3008){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$3009=(0);
    var setR$3009=function(r$3010){return r$3009=r$3010;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$3011 = a$3008.iterator;
    var i$3012;while ((i$3012=it$3011.next())!==$$$cl2309.getFinished()){
        (r$3009=r$3009.plus(i$3012),r$3009);
    }
    return r$3009;
};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$3013=$$$cl2309.Tuple((8),$$$cl2309.Tuple((9),$$$cl2309.Tuple((10),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}});
    $$$c2310.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}})).equals((6)),$$$cl2309.String("spread [1]",10));
    $$$c2310.check(spread1(ints$3013).equals((27)),$$$cl2309.String("spread [2]",10));
    $$$c2310.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}).chain(ints$3013,{Element:{t:$$$cl2309.Integer}})).equals((30)),$$$cl2309.String("spread [3]",10));
    $$$c2310.check(spread1($$$cl2309.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$3014=ints$3013.iterator;
        var i$3015=$$$cl2309.getFinished();
        var next$i$3015=function(){return i$3015=it$3014.next();}
        next$i$3015();
        return function(){
            if(i$3015!==$$$cl2309.getFinished()){
                var i$3015$3016=i$3015;
                var tmpvar$3017=i$3015$3016.times((10));
                next$i$3015();
                return tmpvar$3017;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}})).equals((270)),$$$cl2309.String("spread [4]",10));
    $$$c2310.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}).chain($$$cl2309.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$3018=ints$3013.iterator;
        var i$3019=$$$cl2309.getFinished();
        var next$i$3019=function(){return i$3019=it$3018.next();}
        next$i$3019();
        return function(){
            if(i$3019!==$$$cl2309.getFinished()){
                var i$3019$3020=i$3019;
                var tmpvar$3021=i$3019$3020.times((10));
                next$i$3019();
                return tmpvar$3021;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}})).equals((275)),$$$cl2309.String("spread [5]",10));
    $$$c2310.check((a$3022=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),spread2(a$3022)).equals((6)),$$$cl2309.String("spread [6]",10));
    var a$3022;
    $$$c2310.check((a$3023=ints$3013,spread2(a$3023)).equals((27)),$$$cl2309.String("spread [7]",10));
    var a$3023;
    $$$c2310.check((a$3024=[(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}).chain(ints$3013,{Element:{t:$$$cl2309.Integer}}),spread2(a$3024)).equals((30)),$$$cl2309.String("spread [8]",10));
    var a$3024;
    $$$c2310.check((a$3025=$$$cl2309.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$3026=ints$3013.iterator;
        var i$3027=$$$cl2309.getFinished();
        var next$i$3027=function(){return i$3027=it$3026.next();}
        next$i$3027();
        return function(){
            if(i$3027!==$$$cl2309.getFinished()){
                var i$3027$3028=i$3027;
                var tmpvar$3029=i$3027$3028.times((10));
                next$i$3027();
                return tmpvar$3029;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),spread2(a$3025)).equals((270)),$$$cl2309.String("spread [9]",10));
    var a$3025;
    $$$c2310.check((a$3030=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}).chain($$$cl2309.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$3031=ints$3013.iterator;
        var i$3032=$$$cl2309.getFinished();
        var next$i$3032=function(){return i$3032=it$3031.next();}
        next$i$3032();
        return function(){
            if(i$3032!==$$$cl2309.getFinished()){
                var i$3032$3033=i$3032;
                var tmpvar$3034=i$3032$3033.times((10));
                next$i$3032();
                return tmpvar$3034;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Integer}}),spread2(a$3030)).equals((275)),$$$cl2309.String("spread [10]",11));
    var a$3030;
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
