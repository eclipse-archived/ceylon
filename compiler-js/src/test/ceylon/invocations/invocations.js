(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$f475=require('functions/0.1/functions-0.1');
var $$$c2=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$476,nums$477){
    if(nums$477===undefined){nums$477=$$$cl1.getEmpty();}
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("C:",2),(opt$478=chars$476.getFirst(),opt$478!==null?opt$478:$$$cl1.String("?",1)).getString(),$$$cl1.String(" #",2),(opt$479=nums$477.get((0)),opt$479!==null?opt$479:$$$cl1.String("?",1)).getString()]).getString();
};
var opt$478,opt$479;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f475.helloWorld();
    ($$$f475.helloWorld());
    $$$f475.hello($$$cl1.String("world",5));
    (name$480=$$$cl1.String("world",5),$$$f475.hello(name$480));
    var name$480;
    $$$f475.helloAll([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    (names$481=$$$cl1.Tuple($$$cl1.String("someone",7),$$$cl1.Tuple($$$cl1.String("someone else",12),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),$$$f475.helloAll(names$481));
    var names$481;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$482=$$$f475.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$483=(obj$484=(99),$$$f475.toString(obj$484));
    var obj$484;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$485=$$$f475.add($$$cl1.Float(1.0),$$$cl1.Float(1.0).getNegativeValue());
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$486=(x$487=$$$cl1.Float(1.0),y$488=$$$cl1.Float(1.0).getNegativeValue(),$$$f475.add(x$487,y$488));
    var x$487,y$488;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$489(i$490){
        $$$cl1.print(i$490);
    };
    $$$f475.repeat((10),p$489);
    testNamedArguments();
    testQualified();
    $$$c2.check(mixseqs($$$cl1.Tuple($$$cl1.Character(97),$$$cl1.Tuple($$$cl1.Character(98),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:a #1",6)));
    $$$c2.check(mixseqs([$$$cl1.Character(98),$$$cl1.Character(99)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:b #2",6)));
    $$$c2.check(mixseqs($$$cl1.String("hola",4).getSequence(),[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:h #3",6)));
    $$$c2.check((chars$491=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$492=$$$cl1.String("hola",4).getIterator();
        var c$493=$$$cl1.getFinished();
        var next$c$493=function(){return c$493=it$492.next();}
        next$c$493();
        return function(){
            if(c$493!==$$$cl1.getFinished()){
                var c$493$494=c$493;
                function getC$493(){return c$493$494;}
                var tmpvar$495=getC$493().getUppercased();
                next$c$493();
                return tmpvar$495;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$491,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$491;
    $$$c2.check((nums$496=$$$cl1.Tuple((2),$$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$497=$$$cl1.String("hola",4),mixseqs(chars$497,nums$496)).equals($$$cl1.String("C:h #2",6)));
    var nums$496,chars$497;
    $$$c2.check((nums$498=$$$cl1.Tuple((4),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$499=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$500=$$$cl1.String("hola",4).getIterator();
        var c$501=$$$cl1.getFinished();
        var next$c$501=function(){return c$501=it$500.next();}
        next$c$501();
        return function(){
            if(c$501!==$$$cl1.getFinished()){
                var c$501$502=c$501;
                function getC$501(){return c$501$502;}
                var tmpvar$503=getC$501();
                next$c$501();
                return tmpvar$503;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$499,nums$498)).equals($$$cl1.String("C:h #4",6)));
    var nums$498,chars$499;
    $$$c2.check((chars$504=$$$cl1.String("hola",4),mixseqs(chars$504,$$$cl1.getEmpty())).equals($$$cl1.String("C:h #?",6)));
    var chars$504;
    $$$c2.check((chars$505=[$$$cl1.Character(72),$$$cl1.Character(73)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$505,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$505;
    testSpread();
    $$$c2.results();
}
exports.test=test;

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$506,desc$507,match$508){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$509 = iter$506.getIterator();
    var i$510;while ((i$510=it$509.next())!==$$$cl1.getFinished()){
        if(match$508(i$510)){
            return $$$cl1.StringBuilder().appendAll([desc$507.getString(),$$$cl1.String(": ",2),i$510.getString()]).getString();
        }
    }
    return $$$cl1.String("[NOT FOUND]",11);
};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$511,count$512,discount$513,comments$514){
    if(count$512===undefined){count$512=(1);}
    if(discount$513===undefined){discount$513=$$$cl1.Float(0.0);}
    if(comments$514===undefined){comments$514=$$$cl1.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$515=$$$cl1.String(", ",2).join($$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$516=comments$514.getIterator();
        var c$517=$$$cl1.getFinished();
        var next$c$517=function(){return c$517=it$516.next();}
        next$c$517();
        return function(){
            if(c$517!==$$$cl1.getFinished()){
                var c$517$518=c$517;
                function getC$517(){return c$517$518;}
                var tmpvar$519=$$$cl1.StringBuilder().appendAll([$$$cl1.String("\'",1),getC$517().getString(),$$$cl1.String("\'",1)]).getString();
                next$c$517();
                return tmpvar$519;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Order \'",7),product$511.getString(),$$$cl1.String("\', quantity ",12),count$512.getString(),$$$cl1.String(", discount ",11)]).getString().plus($$$cl1.StringBuilder().appendAll([discount$513.getString(),$$$cl1.String(", comments: ",12),commentStr$515.getString()]).getString());
};

//MethodDefinition testNamedArguments at named.ceylon (21:0-64:0)
function testNamedArguments(){
    $$$c2.check((iter$520=(function(){
        //ObjectArgument iter at named.ceylon (23:4-27:4)
        function iter$521(){
            var $$iter$521=new iter$521.$$;
            $$$cl1.Iterable($$iter$521);
            $$$cl1.add_type_arg($$iter$521,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$521,'Element',{t:$$$cl1.Integer});
            
            //AttributeGetterDefinition iterator at named.ceylon (24:6-26:6)
            var getIterator=function(){
                return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).getIterator();
            }
            $$iter$521.getIterator=getIterator;
            return $$iter$521;
        }
        function $init$iter$521(){
            if (iter$521.$$===undefined){
                $$$cl1.initTypeProto(iter$521,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            return iter$521;
        }
        $init$iter$521();
        return iter$521(new iter$521.$$);
    }()),desc$522=(function(){
        //AttributeArgument desc at named.ceylon (28:4-30:4)
        return $$$cl1.String("Even",4);
    }()),match$523=function (i$524){
        return i$524.remainder((2)).equals((0));
    },namedFunc(iter$520,desc$522,match$523)).equals($$$cl1.String("Even: 8",7)),$$$cl1.String("named arguments 1",17));
    var iter$520,desc$522,match$523;
    $$$c2.check((iter$525=(function(){
        //ObjectArgument iter at named.ceylon (36:4-40:4)
        function iter$526(){
            var $$iter$526=new iter$526.$$;
            $$$cl1.Iterable($$iter$526);
            $$$cl1.add_type_arg($$iter$526,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$526,'Element',{t:$$$cl1.Integer});
            
            //AttributeGetterDefinition iterator at named.ceylon (37:6-39:6)
            var getIterator=function(){
                return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).getIterator();
            }
            $$iter$526.getIterator=getIterator;
            return $$iter$526;
        }
        function $init$iter$526(){
            if (iter$526.$$===undefined){
                $$$cl1.initTypeProto(iter$526,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            return iter$526;
        }
        $init$iter$526();
        return iter$526(new iter$526.$$);
    }()),desc$527=(function(){
        //AttributeArgument desc at named.ceylon (41:4-43:4)
        return $$$cl1.String("Odd",3);
    }()),match$528=function (x$529){
        return x$529.remainder((2)).equals((1));
    },namedFunc(iter$525,desc$527,match$528)).equals($$$cl1.String("Odd: 9",6)),$$$cl1.String("named arguments 2",17));
    var iter$525,desc$527,match$528;
    $$$c2.check((desc$530=$$$cl1.String("Even",4),match$531=function (x$532){
        return x$532.equals((2));
    },iter$533=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),namedFunc(iter$533,desc$530,match$531)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 3",17));
    var desc$530,match$531,iter$533;
    $$$c2.check((desc$534=$$$cl1.String("Even",4),match$535=function (x$536){
        return x$536.equals((2));
    },iter$537=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (54:4-54:21)
        var it$538=$$$cl1.Range((10),(1),{Element:{t:$$$cl1.Integer}}).getIterator();
        var i$539=$$$cl1.getFinished();
        var next$i$539=function(){return i$539=it$538.next();}
        next$i$539();
        return function(){
            if(i$539!==$$$cl1.getFinished()){
                var i$539$540=i$539;
                function getI$539(){return i$539$540;}
                var tmpvar$541=getI$539();
                next$i$539();
                return tmpvar$541;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),namedFunc(iter$537,desc$534,match$535)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 4",17));
    var desc$534,match$535,iter$537;
    $$$c2.check((product$542=$$$cl1.String("Mouse",5),order(product$542,undefined,undefined,undefined)).equals($$$cl1.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl1.String("defaulted & sequenced named [1]",31));
    var product$542;
    $$$c2.check((product$543=$$$cl1.String("Rhinoceros",10),discount$544=$$$cl1.Float(10.0),order(product$543,undefined,discount$544,undefined)).equals($$$cl1.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl1.String("defaulted & sequenced named [2]",31));
    var product$543,discount$544;
    $$$c2.check((product$545=$$$cl1.String("Bee",3),count$546=(531),comments$547=[$$$cl1.String("Express delivery",16),$$$cl1.String("Send individually",17)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}),order(product$545,count$546,undefined,comments$547)).equals($$$cl1.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl1.String("defaulted & sequenced named [3]",31));
    var product$545,count$546,comments$547;
};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
    
    //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
    function somethingElse(x$548){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("something ",10),x$548.getString(),$$$cl1.String(" else",5)]).getString();
    }
    $$ambiguousParent.somethingElse=somethingElse;
}
function $init$AmbiguousParent(){
    if (AmbiguousParent.$$===undefined){
        $$$cl1.initTypeProto(AmbiguousParent,'invocations::AmbiguousParent');
    }
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
    
    //AttributeGetterDefinition whatever at qualified.ceylon (15:4-15:55)
    var getWhatever=function(){
        return (1);
    }
    $$ambiguous1.getWhatever=getWhatever;
    
    //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
    function somethingElse(x$549){
        if(x$549.remainder((2)).equals((0))){
            return $$ambiguous1.somethingElse$$invocations$AmbiguousParent(x$549);
        }
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous1 something ",21),x$549.getString(),$$$cl1.String(" else",5)]).getString();
    }
    $$ambiguous1.somethingElse=somethingElse;
}
function $init$Ambiguous1(){
    if (Ambiguous1.$$===undefined){
        $$$cl1.initTypeProto(Ambiguous1,'invocations::Ambiguous1',$init$AmbiguousParent());
    }
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
    
    //AttributeGetterDefinition whatever at qualified.ceylon (27:4-27:55)
    var getWhatever=function(){
        return (2);
    }
    $$ambiguous2.getWhatever=getWhatever;
    
    //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
    function somethingElse(x$550){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous2 ",11),x$550.getString(),$$$cl1.String(" something else",15)]).getString();
    }
    $$ambiguous2.somethingElse=somethingElse;
}
function $init$Ambiguous2(){
    if (Ambiguous2.$$===undefined){
        $$$cl1.initTypeProto(Ambiguous2,'invocations::Ambiguous2',$init$AmbiguousParent());
    }
    return Ambiguous2;
}
exports.$init$Ambiguous2=$init$Ambiguous2;
$init$Ambiguous2();

//ClassDefinition QualifyAmbiguousSupertypes at qualified.ceylon (33:0-47:0)
function QualifyAmbiguousSupertypes(one$551, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$551=one$551;
    Ambiguous1($$qualifyAmbiguousSupertypes);
    $$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.doSomething;
    $$qualifyAmbiguousSupertypes.getWhatever$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.getWhatever;
    $$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1=$$qualifyAmbiguousSupertypes.somethingElse;
    Ambiguous2($$qualifyAmbiguousSupertypes);
    $$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.doSomething;
    $$qualifyAmbiguousSupertypes.getWhatever$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.getWhatever;
    $$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2=$$qualifyAmbiguousSupertypes.somethingElse;
    
    //MethodDefinition doSomething at qualified.ceylon (35:4-37:4)
    function doSomething(){
        return (opt$552=(one$551?$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1():null),opt$552!==null?opt$552:$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2());
        var opt$552;
    }
    $$qualifyAmbiguousSupertypes.doSomething=doSomething;
    
    //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
    var getWhatever=function(){
        if(one$551){
            return $$qualifyAmbiguousSupertypes.getWhatever$$invocations$Ambiguous1();
        }
        return $$qualifyAmbiguousSupertypes.getWhatever$$invocations$Ambiguous2();
    }
    $$qualifyAmbiguousSupertypes.getWhatever=getWhatever;
    
    //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
    function somethingElse(x$553){
        return (opt$554=(one$551?$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1(x$553):null),opt$554!==null?opt$554:$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2(x$553));
        var opt$554;
    }
    $$qualifyAmbiguousSupertypes.somethingElse=somethingElse;
    return $$qualifyAmbiguousSupertypes;
}
function $init$QualifyAmbiguousSupertypes(){
    if (QualifyAmbiguousSupertypes.$$===undefined){
        $$$cl1.initTypeProto(QualifyAmbiguousSupertypes,'invocations::QualifyAmbiguousSupertypes',$$$cl1.Basic,$init$Ambiguous1(),$init$Ambiguous2());
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
    var a$555=(0);
    var getA=function(){return a$555;};
    $$qualifiedA.getA=getA;
    var setA=function(a$556){return a$555=a$556;};
    $$qualifiedA.setA=setA;
    return $$qualifiedA;
}
function $init$QualifiedA(){
    if (QualifiedA.$$===undefined){
        $$$cl1.initTypeProto(QualifiedA,'invocations::QualifiedA',$$$cl1.Basic);
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
    $$qualifiedB.getA$$invocations$QualifiedA=$$qualifiedB.getA;
    $$qualifiedB.setA$$invocations$QualifiedA=$$qualifiedB.setA;
    $$qualifiedB.getA$$invocations$QualifiedA=$$qualifiedB.getA;
    $$qualifiedB.setA$$invocations$QualifiedA=$$qualifiedB.setA;
    $$qualifiedB.getA$$invocations$QualifiedA=$$qualifiedB.getA;
    $$qualifiedB.setA$$invocations$QualifiedA=$$qualifiedB.setA;
    
    //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
    var a$557=(0);
    var getA=function(){return a$557;};
    $$qualifiedB.getA=getA;
    var setA=function(a$558){return a$557=a$558;};
    $$qualifiedB.setA=setA;
    
    //MethodDefinition f at qualified.ceylon (54:2-56:2)
    function f(){
        (olda$559=$$qualifiedB.getA$$invocations$QualifiedA(),$$qualifiedB.setA$$invocations$QualifiedA(olda$559.getSuccessor()),olda$559);
        var olda$559;
    }
    $$qualifiedB.f=f;
    
    //MethodDefinition g at qualified.ceylon (57:2-59:2)
    function g(){
        return ($$qualifiedB.setA$$invocations$QualifiedA($$qualifiedB.getA$$invocations$QualifiedA().getSuccessor()));
    }
    $$qualifiedB.g=g;
    
    //AttributeGetterDefinition supera at qualified.ceylon (60:2-60:48)
    var getSupera=function(){
        return $$qualifiedB.getA$$invocations$QualifiedA();
    }
    $$qualifiedB.getSupera=getSupera;
    return $$qualifiedB;
}
function $init$QualifiedB(){
    if (QualifiedB.$$===undefined){
        $$$cl1.initTypeProto(QualifiedB,'invocations::QualifiedB',QualifiedA);
    }
    return QualifiedB;
}
exports.$init$QualifiedB=$init$QualifiedB;
$init$QualifiedB();

//ClassDefinition TestList at qualified.ceylon (63:0-75:0)
function TestList($$testList){
    $init$TestList();
    if ($$testList===undefined)$$testList=new TestList.$$;
    $$testList.$$targs$$={Element:{t:$$$cl1.String}};
    $$$cl1.List($$testList);
    $$$cl1.add_type_arg($$testList,'Element',{t:$$$cl1.String});
    $$testList.equals$$ceylon$language$List=$$testList.equals;
    $$testList.getHash$$ceylon$language$List=$$testList.getHash;
    
    //AttributeDeclaration clone at qualified.ceylon (64:4-64:41)
    var clone$560=$$$cl1.getEmpty();
    var getClone=function(){return clone$560;};
    $$testList.getClone=getClone;
    
    //MethodDefinition get at qualified.ceylon (65:4-65:60)
    function get(index$561){
        return null;
    }
    $$testList.get=get;
    
    //AttributeDeclaration lastIndex at qualified.ceylon (66:4-66:43)
    var lastIndex$562=null;
    var getLastIndex=function(){return lastIndex$562;};
    $$testList.getLastIndex=getLastIndex;
    
    //AttributeDeclaration reversed at qualified.ceylon (67:4-67:44)
    var reversed$563=$$$cl1.getEmpty();
    var getReversed=function(){return reversed$563;};
    $$testList.getReversed=getReversed;
    
    //MethodDefinition segment at qualified.ceylon (68:4-68:82)
    function segment(from$564,length$565){
        return $$$cl1.getEmpty();
    }
    $$testList.segment=segment;
    
    //MethodDefinition span at qualified.ceylon (69:4-69:75)
    function span(from$566,to$567){
        return $$$cl1.getEmpty();
    }
    $$testList.span=span;
    
    //MethodDefinition spanTo at qualified.ceylon (70:4-70:63)
    function spanTo(to$568){
        return $$$cl1.getEmpty();
    }
    $$testList.spanTo=spanTo;
    
    //MethodDefinition spanFrom at qualified.ceylon (71:4-71:67)
    function spanFrom(from$569){
        return $$$cl1.getEmpty();
    }
    $$testList.spanFrom=spanFrom;
    
    //AttributeDeclaration iterator at qualified.ceylon (72:4-72:59)
    var iterator$570=$$$cl1.getEmptyIterator();
    var getIterator=function(){return iterator$570;};
    $$testList.getIterator=getIterator;
    
    //MethodDefinition equals at qualified.ceylon (73:4-73:75)
    function equals(that$571){
        return $$testList.equals$$ceylon$language$List(that$571);
    }
    $$testList.equals=equals;
    
    //AttributeGetterDefinition hash at qualified.ceylon (74:4-74:52)
    var getHash=function(){
        return $$testList.getHash$$ceylon$language$List();
    }
    $$testList.getHash=getHash;
    return $$testList;
}
function $init$TestList(){
    if (TestList.$$===undefined){
        $$$cl1.initTypeProto(TestList,'invocations::TestList',$$$cl1.Basic,$$$cl1.List);
    }
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDefinition testQualified at qualified.ceylon (77:0-96:0)
function testQualified(){
    
    //AttributeDeclaration q1 at qualified.ceylon (78:4-78:47)
    var q1$572=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (79:4-79:48)
    var q2$573=QualifyAmbiguousSupertypes(false);
    $$$c2.check(q1$572.doSomething().equals($$$cl1.String("ambiguous 1",11)),$$$cl1.String("qualified super calls [1]",25));
    $$$c2.check(q2$573.doSomething().equals($$$cl1.String("ambiguous 2",11)),$$$cl1.String("qualified super calls [2]",25));
    $$$c2.check(q1$572.getWhatever().equals((1)),$$$cl1.String("qualified super attrib [1]",26));
    $$$c2.check(q2$573.getWhatever().equals((2)),$$$cl1.String("qualified super attrib [2]",26));
    $$$c2.check(q1$572.somethingElse((5)).equals($$$cl1.String("Ambiguous1 something 5 else",27)),$$$cl1.String("qualified super method [1]",26));
    $$$c2.check(q1$572.somethingElse((6)).equals($$$cl1.String("something 6 else",16)),$$$cl1.String("qualified super method [2]",26));
    $$$c2.check(q2$573.somethingElse((5)).equals($$$cl1.String("Ambiguous2 5 something else",27)),$$$cl1.String("qualified super method [3]",26));
    $$$c2.check(q2$573.somethingElse((6)).equals($$$cl1.String("Ambiguous2 6 something else",27)),$$$cl1.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (88:4-88:27)
    var qb$574=QualifiedB();
    $$$c2.check(qb$574.getA().equals(qb$574.getSupera()),$$$cl1.String("Qualified attribute [1]",23));
    qb$574.f();
    $$$c2.check((tmp$575=qb$574,tmp$575.setA(tmp$575.getA().getSuccessor())).equals(qb$574.getSupera()),$$$cl1.String("Qualified attribute [2]",23));
    var tmp$575;
    $$$c2.check((tmp$576=qb$574,tmp$576.setA(tmp$576.getA().getSuccessor())).equals(qb$574.g()),$$$cl1.String("Qualified attribute [3]",23));
    var tmp$576;
    
    //AttributeDeclaration tl at qualified.ceylon (93:4-93:25)
    var tl$577=TestList();
    $$$c2.check(tl$577.getHash().equals($$$cl1.getEmpty().getHash()),$$$cl1.String("List::hash",10));
    $$$c2.check(tl$577.equals($$$cl1.getEmpty()),$$$cl1.String("List::equals",12));
};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$578){
    if(a$578===undefined){a$578=$$$cl1.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$579=(0);
    var setR$579=function(r$580){return r$579=r$580;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$581 = a$578.getIterator();
    var i$582;while ((i$582=it$581.next())!==$$$cl1.getFinished()){
        (r$579=r$579.plus(i$582));
    }
    return r$579;
};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$583){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$584=(0);
    var setR$584=function(r$585){return r$584=r$585;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$586 = a$583.getIterator();
    var i$587;while ((i$587=it$586.next())!==$$$cl1.getFinished()){
        (r$584=r$584.plus(i$587));
    }
    return r$584;
};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$588=$$$cl1.Tuple((8),$$$cl1.Tuple((9),$$$cl1.Tuple((10),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((6)),$$$cl1.String("spread [1]",10));
    $$$c2.check(spread1(ints$588).equals((27)),$$$cl1.String("spread [2]",10));
    $$$c2.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain(ints$588,{Element:{t:$$$cl1.Integer}})).equals((30)),$$$cl1.String("spread [3]",10));
    $$$c2.check(spread1($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$589=ints$588.getIterator();
        var i$590=$$$cl1.getFinished();
        var next$i$590=function(){return i$590=it$589.next();}
        next$i$590();
        return function(){
            if(i$590!==$$$cl1.getFinished()){
                var i$590$591=i$590;
                function getI$590(){return i$590$591;}
                var tmpvar$592=getI$590().times((10));
                next$i$590();
                return tmpvar$592;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((270)),$$$cl1.String("spread [4]",10));
    $$$c2.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$593=ints$588.getIterator();
        var i$594=$$$cl1.getFinished();
        var next$i$594=function(){return i$594=it$593.next();}
        next$i$594();
        return function(){
            if(i$594!==$$$cl1.getFinished()){
                var i$594$595=i$594;
                function getI$594(){return i$594$595;}
                var tmpvar$596=getI$594().times((10));
                next$i$594();
                return tmpvar$596;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((275)),$$$cl1.String("spread [5]",10));
    $$$c2.check((a$597=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$597)).equals((6)),$$$cl1.String("spread [6]",10));
    var a$597;
    $$$c2.check((a$598=ints$588,spread2(a$598)).equals((27)),$$$cl1.String("spread [7]",10));
    var a$598;
    $$$c2.check((a$599=[(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain(ints$588,{Element:{t:$$$cl1.Integer}}),spread2(a$599)).equals((30)),$$$cl1.String("spread [8]",10));
    var a$599;
    $$$c2.check((a$600=$$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$601=ints$588.getIterator();
        var i$602=$$$cl1.getFinished();
        var next$i$602=function(){return i$602=it$601.next();}
        next$i$602();
        return function(){
            if(i$602!==$$$cl1.getFinished()){
                var i$602$603=i$602;
                function getI$602(){return i$602$603;}
                var tmpvar$604=getI$602().times((10));
                next$i$602();
                return tmpvar$604;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$600)).equals((270)),$$$cl1.String("spread [9]",10));
    var a$600;
    $$$c2.check((a$605=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$606=ints$588.getIterator();
        var i$607=$$$cl1.getFinished();
        var next$i$607=function(){return i$607=it$606.next();}
        next$i$607();
        return function(){
            if(i$607!==$$$cl1.getFinished()){
                var i$607$608=i$607;
                function getI$607(){return i$607$608;}
                var tmpvar$609=getI$607().times((10));
                next$i$607();
                return tmpvar$609;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$605)).equals((275)),$$$cl1.String("spread [10]",11));
    var a$605;
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
