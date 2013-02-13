(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$f2716=require('functions/0.1/functions-0.1');
var $$$c2244=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$2717,nums$2718){
    if(nums$2718===undefined){nums$2718=$$$cl2243.getEmpty();}
    return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("C:",2),(opt$2719=chars$2717.getFirst(),opt$2719!==null?opt$2719:$$$cl2243.String("?",1)).getString(),$$$cl2243.String(" #",2),(opt$2720=nums$2718.get((0)),opt$2720!==null?opt$2720:$$$cl2243.String("?",1)).getString()]).getString();
};
var opt$2719,opt$2720;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f2716.helloWorld();
    ($$$f2716.helloWorld());
    $$$f2716.hello($$$cl2243.String("world",5));
    (name$2721=$$$cl2243.String("world",5),$$$f2716.hello(name$2721));
    var name$2721;
    $$$f2716.helloAll([$$$cl2243.String("someone",7),$$$cl2243.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}));
    (names$2722=$$$cl2243.Tuple($$$cl2243.String("someone",7),$$$cl2243.Tuple($$$cl2243.String("someone else",12),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),$$$f2716.helloAll(names$2722));
    var names$2722;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$2723=$$$f2716.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$2724=(obj$2725=(99),$$$f2716.toString(obj$2725));
    var obj$2725;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$2726=$$$f2716.add($$$cl2243.Float(1.0),$$$cl2243.Float(1.0).getNegativeValue());
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$2727=(x$2728=$$$cl2243.Float(1.0),y$2729=$$$cl2243.Float(1.0).getNegativeValue(),$$$f2716.add(x$2728,y$2729));
    var x$2728,y$2729;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$2730(i$2731){
        $$$cl2243.print(i$2731);
    };
    $$$f2716.repeat((10),p$2730);
    testNamedArguments();
    testQualified();
    $$$c2244.check(mixseqs($$$cl2243.Tuple($$$cl2243.Character(97),$$$cl2243.Tuple($$$cl2243.Character(98),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Character},Element:{t:$$$cl2243.Character}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Character},Element:{t:$$$cl2243.Character}}},First:{t:$$$cl2243.Character},Element:{t:$$$cl2243.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}})).equals($$$cl2243.String("C:a #1",6)));
    $$$c2244.check(mixseqs([$$$cl2243.Character(98),$$$cl2243.Character(99)].reifyCeylonType({Absent:{t:$$$cl2243.Nothing},Element:{t:$$$cl2243.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}})).equals($$$cl2243.String("C:b #2",6)));
    $$$c2244.check(mixseqs($$$cl2243.String("hola",4).getSequence(),[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}})).equals($$$cl2243.String("C:h #3",6)));
    $$$c2244.check((chars$2732=$$$cl2243.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$2733=$$$cl2243.String("hola",4).getIterator();
        var c$2734=$$$cl2243.getFinished();
        var next$c$2734=function(){return c$2734=it$2733.next();}
        next$c$2734();
        return function(){
            if(c$2734!==$$$cl2243.getFinished()){
                var c$2734$2735=c$2734;
                function getC$2734(){return c$2734$2735;}
                var tmpvar$2736=getC$2734().getUppercased();
                next$c$2734();
                return tmpvar$2736;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Character}}),mixseqs(chars$2732,$$$cl2243.getEmpty())).equals($$$cl2243.String("C:H #?",6)));
    var chars$2732;
    $$$c2244.check((nums$2737=$$$cl2243.Tuple((2),$$$cl2243.Tuple((1),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),chars$2738=$$$cl2243.String("hola",4),mixseqs(chars$2738,nums$2737)).equals($$$cl2243.String("C:h #2",6)));
    var nums$2737,chars$2738;
    $$$c2244.check((nums$2739=$$$cl2243.Tuple((4),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),chars$2740=$$$cl2243.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$2741=$$$cl2243.String("hola",4).getIterator();
        var c$2742=$$$cl2243.getFinished();
        var next$c$2742=function(){return c$2742=it$2741.next();}
        next$c$2742();
        return function(){
            if(c$2742!==$$$cl2243.getFinished()){
                var c$2742$2743=c$2742;
                function getC$2742(){return c$2742$2743;}
                var tmpvar$2744=getC$2742();
                next$c$2742();
                return tmpvar$2744;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Character}}),mixseqs(chars$2740,nums$2739)).equals($$$cl2243.String("C:h #4",6)));
    var nums$2739,chars$2740;
    $$$c2244.check((chars$2745=$$$cl2243.String("hola",4),mixseqs(chars$2745,$$$cl2243.getEmpty())).equals($$$cl2243.String("C:h #?",6)));
    var chars$2745;
    $$$c2244.check((chars$2746=[$$$cl2243.Character(72),$$$cl2243.Character(73)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Character}}),mixseqs(chars$2746,$$$cl2243.getEmpty())).equals($$$cl2243.String("C:H #?",6)));
    var chars$2746;
    testSpread();
    $$$c2244.results();
}
exports.test=test;

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$2747,desc$2748,match$2749){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$2750 = iter$2747.getIterator();
    var i$2751;while ((i$2751=it$2750.next())!==$$$cl2243.getFinished()){
        if(match$2749(i$2751)){
            return $$$cl2243.StringBuilder().appendAll([desc$2748.getString(),$$$cl2243.String(": ",2),i$2751.getString()]).getString();
        }
    }
    return $$$cl2243.String("[NOT FOUND]",11);
};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$2752,count$2753,discount$2754,comments$2755){
    if(count$2753===undefined){count$2753=(1);}
    if(discount$2754===undefined){discount$2754=$$$cl2243.Float(0.0);}
    if(comments$2755===undefined){comments$2755=$$$cl2243.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$2756=$$$cl2243.String(", ",2).join($$$cl2243.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$2757=comments$2755.getIterator();
        var c$2758=$$$cl2243.getFinished();
        var next$c$2758=function(){return c$2758=it$2757.next();}
        next$c$2758();
        return function(){
            if(c$2758!==$$$cl2243.getFinished()){
                var c$2758$2759=c$2758;
                function getC$2758(){return c$2758$2759;}
                var tmpvar$2760=$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("\'",1),getC$2758().getString(),$$$cl2243.String("\'",1)]).getString();
                next$c$2758();
                return tmpvar$2760;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}));
    return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("Order \'",7),product$2752.getString(),$$$cl2243.String("\', quantity ",12),count$2753.getString(),$$$cl2243.String(", discount ",11)]).getString().plus($$$cl2243.StringBuilder().appendAll([discount$2754.getString(),$$$cl2243.String(", comments: ",12),commentStr$2756.getString()]).getString());
};

//MethodDefinition testNamedArguments at named.ceylon (21:0-64:0)
function testNamedArguments(){
    $$$c2244.check((iter$2761=(function(){
        //ObjectArgument iter at named.ceylon (23:4-27:4)
        function iter$2762(){
            var $$iter$2762=new iter$2762.$$;
            $$$cl2243.Iterable($$iter$2762);
            $$$cl2243.add_type_arg($$iter$2762,'Absent',{t:$$$cl2243.Null});
            $$$cl2243.add_type_arg($$iter$2762,'Element',{t:$$$cl2243.Integer});
            return $$iter$2762;
        }
        function $init$iter$2762(){
            if (iter$2762.$$===undefined){
                $$$cl2243.initTypeProto(iter$2762,'invocations::testNamedArguments.iter',$$$cl2243.Basic,$$$cl2243.Iterable);
                (function($$iter$2762){
                    
                    //AttributeGetterDefinition iterator at named.ceylon (24:6-26:6)
                    $$iter$2762.getIterator=function getIterator(){
                        var $$iter$2762=this;
                        return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl2243.Nothing},Element:{t:$$$cl2243.Integer}}).getIterator();
                    };
                })(iter$2762.$$.prototype);
            }
            return iter$2762;
        }
        $init$iter$2762();
        return iter$2762(new iter$2762.$$);
    }()),desc$2763=(function(){
        //AttributeArgument desc at named.ceylon (28:4-30:4)
        return $$$cl2243.String("Even",4);
    }()),match$2764=function (i$2765){
        return i$2765.remainder((2)).equals((0));
    },namedFunc(iter$2761,desc$2763,match$2764)).equals($$$cl2243.String("Even: 8",7)),$$$cl2243.String("named arguments 1",17));
    var iter$2761,desc$2763,match$2764;
    $$$c2244.check((iter$2766=(function(){
        //ObjectArgument iter at named.ceylon (36:4-40:4)
        function iter$2767(){
            var $$iter$2767=new iter$2767.$$;
            $$$cl2243.Iterable($$iter$2767);
            $$$cl2243.add_type_arg($$iter$2767,'Absent',{t:$$$cl2243.Null});
            $$$cl2243.add_type_arg($$iter$2767,'Element',{t:$$$cl2243.Integer});
            return $$iter$2767;
        }
        function $init$iter$2767(){
            if (iter$2767.$$===undefined){
                $$$cl2243.initTypeProto(iter$2767,'invocations::testNamedArguments.iter',$$$cl2243.Basic,$$$cl2243.Iterable);
                (function($$iter$2767){
                    
                    //AttributeGetterDefinition iterator at named.ceylon (37:6-39:6)
                    $$iter$2767.getIterator=function getIterator(){
                        var $$iter$2767=this;
                        return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl2243.Nothing},Element:{t:$$$cl2243.Integer}}).getIterator();
                    };
                })(iter$2767.$$.prototype);
            }
            return iter$2767;
        }
        $init$iter$2767();
        return iter$2767(new iter$2767.$$);
    }()),desc$2768=(function(){
        //AttributeArgument desc at named.ceylon (41:4-43:4)
        return $$$cl2243.String("Odd",3);
    }()),match$2769=function (x$2770){
        return x$2770.remainder((2)).equals((1));
    },namedFunc(iter$2766,desc$2768,match$2769)).equals($$$cl2243.String("Odd: 9",6)),$$$cl2243.String("named arguments 2",17));
    var iter$2766,desc$2768,match$2769;
    $$$c2244.check((desc$2771=$$$cl2243.String("Even",4),match$2772=function (x$2773){
        return x$2773.equals((2));
    },iter$2774=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),namedFunc(iter$2774,desc$2771,match$2772)).equals($$$cl2243.String("Even: 2",7)),$$$cl2243.String("named arguments 3",17));
    var desc$2771,match$2772,iter$2774;
    $$$c2244.check((desc$2775=$$$cl2243.String("Even",4),match$2776=function (x$2777){
        return x$2777.equals((2));
    },iter$2778=$$$cl2243.Comprehension(function(){
        //Comprehension at named.ceylon (54:4-54:21)
        var it$2779=$$$cl2243.Range((10),(1),{Element:{t:$$$cl2243.Integer}}).getIterator();
        var i$2780=$$$cl2243.getFinished();
        var next$i$2780=function(){return i$2780=it$2779.next();}
        next$i$2780();
        return function(){
            if(i$2780!==$$$cl2243.getFinished()){
                var i$2780$2781=i$2780;
                function getI$2780(){return i$2780$2781;}
                var tmpvar$2782=getI$2780();
                next$i$2780();
                return tmpvar$2782;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),namedFunc(iter$2778,desc$2775,match$2776)).equals($$$cl2243.String("Even: 2",7)),$$$cl2243.String("named arguments 4",17));
    var desc$2775,match$2776,iter$2778;
    $$$c2244.check((product$2783=$$$cl2243.String("Mouse",5),order(product$2783,undefined,undefined,undefined)).equals($$$cl2243.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl2243.String("defaulted & sequenced named [1]",31));
    var product$2783;
    $$$c2244.check((product$2784=$$$cl2243.String("Rhinoceros",10),discount$2785=$$$cl2243.Float(10.0),order(product$2784,undefined,discount$2785,undefined)).equals($$$cl2243.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl2243.String("defaulted & sequenced named [2]",31));
    var product$2784,discount$2785;
    $$$c2244.check((product$2786=$$$cl2243.String("Bee",3),count$2787=(531),comments$2788=[$$$cl2243.String("Express delivery",16),$$$cl2243.String("Send individually",17)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.String}}),order(product$2786,count$2787,undefined,comments$2788)).equals($$$cl2243.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl2243.String("defaulted & sequenced named [3]",31));
    var product$2786,count$2787,comments$2788;
};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
}
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl2243.initTypeProto(AmbiguousParent,'invocations::AmbiguousParent');
        (function($$ambiguousParent){
            
            //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
            $$ambiguousParent.somethingElse=function somethingElse(x$2789){
                var $$ambiguousParent=this;
                return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("something ",10),x$2789.getString(),$$$cl2243.String(" else",5)]).getString();
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
        $$$cl2243.initTypeProto(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
        (function($$ambiguous1){
            
            //MethodDefinition doSomething at qualified.ceylon (12:4-14:4)
            $$ambiguous1.doSomething=function doSomething(){
                var $$ambiguous1=this;
                return $$$cl2243.String("ambiguous 1",11);
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (15:4-15:55)
            $$ambiguous1.getWhatever=function getWhatever(){
                var $$ambiguous1=this;
                return (1);
            };
            //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
            $$ambiguous1.somethingElse=function somethingElse(x$2790){
                var $$ambiguous1=this;
                if(x$2790.remainder((2)).equals((0))){
                    return $$ambiguous1.getT$all()['invocations::AmbiguousParent'].$$.prototype.somethingElse.call(this,x$2790);
                }
                return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("Ambiguous1 something ",21),x$2790.getString(),$$$cl2243.String(" else",5)]).getString();
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
        $$$cl2243.initTypeProto(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
        (function($$ambiguous2){
            
            //MethodDefinition doSomething at qualified.ceylon (24:4-26:4)
            $$ambiguous2.doSomething=function doSomething(){
                var $$ambiguous2=this;
                return $$$cl2243.String("ambiguous 2",11);
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (27:4-27:55)
            $$ambiguous2.getWhatever=function getWhatever(){
                var $$ambiguous2=this;
                return (2);
            };
            //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
            $$ambiguous2.somethingElse=function somethingElse(x$2791){
                var $$ambiguous2=this;
                return $$$cl2243.StringBuilder().appendAll([$$$cl2243.String("Ambiguous2 ",11),x$2791.getString(),$$$cl2243.String(" something else",15)]).getString();
            };
        })(Ambiguous2.$$.prototype);
    }
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDefinition QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$2792, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$2792=one$2792;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    Ambiguous2($$qualifyAmbiguousSupertypes);
    return $$qualifyAmbiguousSupertypes;
}
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl2243.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl2243.Basic,$init$Ambiguous1(),$init$Ambiguous2());
        (function($$qualifyAmbiguousSupertypes){
            
            //MethodDefinition doSomething at qualified.ceylon (35:4-37:4)
            $$qualifyAmbiguousSupertypes.doSomething=function doSomething(){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$2793=($$qualifyAmbiguousSupertypes.one$2792?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.doSomething.call(this):null),opt$2793!==null?opt$2793:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.doSomething.call(this));
                var opt$2793;
            };
            //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
            $$qualifyAmbiguousSupertypes.getWhatever=function getWhatever(){
                var $$qualifyAmbiguousSupertypes=this;
                if($$qualifyAmbiguousSupertypes.one$2792){
                    return $$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.getWhatever.call(this);
                }
                return $$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.getWhatever.call(this);
            };
            //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
            $$qualifyAmbiguousSupertypes.somethingElse=function somethingElse(x$2794){
                var $$qualifyAmbiguousSupertypes=this;
                return (opt$2795=($$qualifyAmbiguousSupertypes.one$2792?$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous1'].$$.prototype.somethingElse.call(this,x$2794):null),opt$2795!==null?opt$2795:$$qualifyAmbiguousSupertypes.getT$all()['invocations::Ambiguous2'].$$.prototype.somethingElse.call(this,x$2794));
                var opt$2795;
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
    $$qualifiedA.a$2796=(0);
    return $$qualifiedA;
}
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl2243.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl2243.Basic);
        (function($$qualifiedA){
            
            //AttributeDeclaration a at qualified.ceylon (50:2-50:37)
            $$qualifiedA.getA=function getA(){
                return this.a$2796;
            };
            $$qualifiedA.setA=function setA(a$2797){
                return this.a$2796=a$2797;
            };
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
    $$qualifiedB.a$2798=(0);
    return $$qualifiedB;
}
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl2243.initTypeProto(QualifiedB,'invocations::QualifiedB',QualifiedA);
        (function($$qualifiedB){
            
            //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
            $$qualifiedB.getA=function getA(){
                return this.a$2798;
            };
            $$qualifiedB.setA=function setA(a$2799){
                return this.a$2798=a$2799;
            };
            
            //MethodDefinition f at qualified.ceylon (54:2-56:2)
            $$qualifiedB.f=function f(){
                var $$qualifiedB=this;
                (olda$2800=$$qualifiedB.getT$all()['invocations::QualifiedA'].$$.prototype.getA.call(this),$$qualifiedB.getT$all()['invocations::QualifiedA'].$$.prototype.setA.call(this,olda$2800.getSuccessor()),olda$2800);
                var olda$2800;
            };
            //MethodDefinition g at qualified.ceylon (57:2-59:2)
            $$qualifiedB.g=function g(){
                var $$qualifiedB=this;
                return ($$qualifiedB.getT$all()['invocations::QualifiedA'].$$.prototype.setA.call(this,$$qualifiedB.getT$all()['invocations::QualifiedA'].$$.prototype.getA.call(this).getSuccessor()));
            };
            //AttributeGetterDefinition supera at qualified.ceylon (60:2-60:48)
            $$qualifiedB.getSupera=function getSupera(){
                var $$qualifiedB=this;
                return $$qualifiedB.getT$all()['invocations::QualifiedA'].$$.prototype.getA.call(this);
            };
        })(QualifiedB.$$.prototype);
    }
    return QualifiedB;
}
exports.$init$QualifiedB=$init$QualifiedB;
$init$QualifiedB();

//ClassDefinition TestList at qualified.ceylon (63:0-75:0)
function TestList($$testList){
    $init$TestList();
    if ($$testList===undefined)$$testList=new TestList.$$;
    $$testList.$$targs$$={Element:{t:$$$cl2243.String}};
    $$$cl2243.List($$testList);
    $$$cl2243.add_type_arg($$testList,'Element',{t:$$$cl2243.String});
    
    //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
    $$testList.clone$2801=$$$cl2243.getEmpty();
    
    //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
    $$testList.lastIndex$2802=null;
    
    //AttributeDeclaration reversed at qualified.ceylon (67:4-67:44)
    $$testList.reversed$2803=$$$cl2243.getEmpty();
    
    //AttributeDeclaration iterator at qualified.ceylon (72:4-72:59)
    $$testList.iterator$2804=$$$cl2243.getEmptyIterator();
    return $$testList;
}
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl2243.initTypeProto(TestList,'invocations::TestList',$$$cl2243.Basic,$$$cl2243.List);
        (function($$testList){
            
            //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
            $$testList.getClone=function getClone(){
                return this.clone$2801;
            };
            
            //MethodDefinition get at qualified.ceylon (65:4-65:60)
            $$testList.get=function get(index$2805){
                var $$testList=this;
                return null;
            };
            //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
            $$testList.getLastIndex=function getLastIndex(){
                return this.lastIndex$2802;
            };
            
            //AttributeDeclaration reversed at qualified.ceylon (67:4-67:44)
            $$testList.getReversed=function getReversed(){
                return this.reversed$2803;
            };
            
            //MethodDefinition segment at qualified.ceylon (68:4-68:82)
            $$testList.segment=function segment(from$2806,length$2807){
                var $$testList=this;
                return $$$cl2243.getEmpty();
            };
            //MethodDefinition span at qualified.ceylon (69:4-69:75)
            $$testList.span=function span(from$2808,to$2809){
                var $$testList=this;
                return $$$cl2243.getEmpty();
            };
            //MethodDefinition spanTo at qualified.ceylon (70:4-70:63)
            $$testList.spanTo=function spanTo(to$2810){
                var $$testList=this;
                return $$$cl2243.getEmpty();
            };
            //MethodDefinition spanFrom at qualified.ceylon (71:4-71:67)
            $$testList.spanFrom=function spanFrom(from$2811){
                var $$testList=this;
                return $$$cl2243.getEmpty();
            };
            //AttributeDeclaration iterator at qualified.ceylon (72:4-72:59)
            $$testList.getIterator=function getIterator(){
                return this.iterator$2804;
            };
            
            //MethodDefinition equals at qualified.ceylon (73:4-73:75)
            $$testList.equals=function equals(that$2812){
                var $$testList=this;
                return $$testList.getT$all()['ceylon.language::List'].$$.prototype.equals.call(this,that$2812);
            };
            //AttributeGetterDefinition hash at qualified.ceylon (74:4-74:52)
            $$testList.getHash=function getHash(){
                var $$testList=this;
                return $$testList.getT$all()['ceylon.language::List'].$$.prototype.getHash.call(this);
            };
        })(TestList.$$.prototype);
    }
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDefinition testQualified at qualified.ceylon (77:0-96:0)
function testQualified(){
    
    //AttributeDeclaration q1 at qualified.ceylon (78:4-78:47)
    var q1$2813=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (79:4-79:48)
    var q2$2814=QualifyAmbiguousSupertypes(false);
    $$$c2244.check(q1$2813.doSomething().equals($$$cl2243.String("ambiguous 1",11)),$$$cl2243.String("qualified super calls [1]",25));
    $$$c2244.check(q2$2814.doSomething().equals($$$cl2243.String("ambiguous 2",11)),$$$cl2243.String("qualified super calls [2]",25));
    $$$c2244.check(q1$2813.getWhatever().equals((1)),$$$cl2243.String("qualified super attrib [1]",26));
    $$$c2244.check(q2$2814.getWhatever().equals((2)),$$$cl2243.String("qualified super attrib [2]",26));
    $$$c2244.check(q1$2813.somethingElse((5)).equals($$$cl2243.String("Ambiguous1 something 5 else",27)),$$$cl2243.String("qualified super method [1]",26));
    $$$c2244.check(q1$2813.somethingElse((6)).equals($$$cl2243.String("something 6 else",16)),$$$cl2243.String("qualified super method [2]",26));
    $$$c2244.check(q2$2814.somethingElse((5)).equals($$$cl2243.String("Ambiguous2 5 something else",27)),$$$cl2243.String("qualified super method [3]",26));
    $$$c2244.check(q2$2814.somethingElse((6)).equals($$$cl2243.String("Ambiguous2 6 something else",27)),$$$cl2243.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (88:4-88:27)
    var qb$2815=QualifiedB();
    $$$c2244.check(qb$2815.getA().equals(qb$2815.getSupera()),$$$cl2243.String("Qualified attribute [1]",23));
    qb$2815.f();
    $$$c2244.check((tmp$2816=qb$2815,tmp$2816.setA(tmp$2816.getA().getSuccessor())).equals(qb$2815.getSupera()),$$$cl2243.String("Qualified attribute [2]",23));
    var tmp$2816;
    $$$c2244.check((tmp$2817=qb$2815,tmp$2817.setA(tmp$2817.getA().getSuccessor())).equals(qb$2815.g()),$$$cl2243.String("Qualified attribute [3]",23));
    var tmp$2817;
    
    //AttributeDeclaration tl at qualified.ceylon (93:4-93:25)
    var tl$2818=TestList();
    $$$c2244.check(tl$2818.getHash().equals($$$cl2243.getEmpty().getHash()),$$$cl2243.String("List::hash",10));
    $$$c2244.check(tl$2818.equals($$$cl2243.getEmpty()),$$$cl2243.String("List::equals",12));
};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$2819){
    if(a$2819===undefined){a$2819=$$$cl2243.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$2820=(0);
    var setR$2820=function(r$2821){return r$2820=r$2821;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$2822 = a$2819.getIterator();
    var i$2823;while ((i$2823=it$2822.next())!==$$$cl2243.getFinished()){
        (r$2820=r$2820.plus(i$2823));
    }
    return r$2820;
};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$2824){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$2825=(0);
    var setR$2825=function(r$2826){return r$2825=r$2826;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$2827 = a$2824.getIterator();
    var i$2828;while ((i$2828=it$2827.next())!==$$$cl2243.getFinished()){
        (r$2825=r$2825.plus(i$2828));
    }
    return r$2825;
};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$2829=$$$cl2243.Tuple((8),$$$cl2243.Tuple((9),$$$cl2243.Tuple((10),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}});
    $$$c2244.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}})).equals((6)),$$$cl2243.String("spread [1]",10));
    $$$c2244.check(spread1(ints$2829).equals((27)),$$$cl2243.String("spread [2]",10));
    $$$c2244.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}).chain(ints$2829,{Element:{t:$$$cl2243.Integer}})).equals((30)),$$$cl2243.String("spread [3]",10));
    $$$c2244.check(spread1($$$cl2243.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$2830=ints$2829.getIterator();
        var i$2831=$$$cl2243.getFinished();
        var next$i$2831=function(){return i$2831=it$2830.next();}
        next$i$2831();
        return function(){
            if(i$2831!==$$$cl2243.getFinished()){
                var i$2831$2832=i$2831;
                function getI$2831(){return i$2831$2832;}
                var tmpvar$2833=getI$2831().times((10));
                next$i$2831();
                return tmpvar$2833;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}})).equals((270)),$$$cl2243.String("spread [4]",10));
    $$$c2244.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}).chain($$$cl2243.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$2834=ints$2829.getIterator();
        var i$2835=$$$cl2243.getFinished();
        var next$i$2835=function(){return i$2835=it$2834.next();}
        next$i$2835();
        return function(){
            if(i$2835!==$$$cl2243.getFinished()){
                var i$2835$2836=i$2835;
                function getI$2835(){return i$2835$2836;}
                var tmpvar$2837=getI$2835().times((10));
                next$i$2835();
                return tmpvar$2837;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}})).equals((275)),$$$cl2243.String("spread [5]",10));
    $$$c2244.check((a$2838=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),spread2(a$2838)).equals((6)),$$$cl2243.String("spread [6]",10));
    var a$2838;
    $$$c2244.check((a$2839=ints$2829,spread2(a$2839)).equals((27)),$$$cl2243.String("spread [7]",10));
    var a$2839;
    $$$c2244.check((a$2840=[(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}).chain(ints$2829,{Element:{t:$$$cl2243.Integer}}),spread2(a$2840)).equals((30)),$$$cl2243.String("spread [8]",10));
    var a$2840;
    $$$c2244.check((a$2841=$$$cl2243.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$2842=ints$2829.getIterator();
        var i$2843=$$$cl2243.getFinished();
        var next$i$2843=function(){return i$2843=it$2842.next();}
        next$i$2843();
        return function(){
            if(i$2843!==$$$cl2243.getFinished()){
                var i$2843$2844=i$2843;
                function getI$2843(){return i$2843$2844;}
                var tmpvar$2845=getI$2843().times((10));
                next$i$2843();
                return tmpvar$2845;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),spread2(a$2841)).equals((270)),$$$cl2243.String("spread [9]",10));
    var a$2841;
    $$$c2244.check((a$2846=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}).chain($$$cl2243.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$2847=ints$2829.getIterator();
        var i$2848=$$$cl2243.getFinished();
        var next$i$2848=function(){return i$2848=it$2847.next();}
        next$i$2848();
        return function(){
            if(i$2848!==$$$cl2243.getFinished()){
                var i$2848$2849=i$2848;
                function getI$2848(){return i$2848$2849;}
                var tmpvar$2850=getI$2848().times((10));
                next$i$2848();
                return tmpvar$2850;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Integer}}),spread2(a$2846)).equals((275)),$$$cl2243.String("spread [10]",11));
    var a$2846;
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
