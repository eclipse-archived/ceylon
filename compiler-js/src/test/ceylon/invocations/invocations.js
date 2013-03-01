(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$f578=require('functions/0.1/functions-0.1');
var $$$c2=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$579,nums$580){
    if(nums$580===undefined){nums$580=$$$cl1.getEmpty();}
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("C:",2),(opt$581=chars$579.first,opt$581!==null?opt$581:$$$cl1.String("?",1)).string,$$$cl1.String(" #",2),(opt$582=nums$580.get((0)),opt$582!==null?opt$582:$$$cl1.String("?",1)).string]).string;
};
var opt$581,opt$582;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f578.helloWorld();
    ($$$f578.helloWorld());
    $$$f578.hello($$$cl1.String("world",5));
    (name$583=$$$cl1.String("world",5),$$$f578.hello(name$583));
    var name$583;
    $$$f578.helloAll([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    (names$584=$$$cl1.Tuple($$$cl1.String("someone",7),$$$cl1.Tuple($$$cl1.String("someone else",12),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),$$$f578.helloAll(names$584));
    var names$584;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$585=$$$f578.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$586=(obj$587=(99),$$$f578.toString(obj$587));
    var obj$587;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$588=$$$f578.add($$$cl1.Float(1.0),$$$cl1.Float(1.0).negativeValue);
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$589=(x$590=$$$cl1.Float(1.0),y$591=$$$cl1.Float(1.0).negativeValue,$$$f578.add(x$590,y$591));
    var x$590,y$591;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$592(i$593){
        $$$cl1.print(i$593);
    };
    $$$f578.repeat((10),p$592);
    testNamedArguments();
    testQualified();
    $$$c2.check(mixseqs($$$cl1.Tuple($$$cl1.Character(97),$$$cl1.Tuple($$$cl1.Character(98),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:a #1",6)));
    $$$c2.check(mixseqs([$$$cl1.Character(98),$$$cl1.Character(99)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:b #2",6)));
    $$$c2.check(mixseqs($$$cl1.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:h #3",6)));
    $$$c2.check((chars$594=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$595=$$$cl1.String("hola",4).iterator;
        var c$596=$$$cl1.getFinished();
        var next$c$596=function(){return c$596=it$595.next();}
        next$c$596();
        return function(){
            if(c$596!==$$$cl1.getFinished()){
                var c$596$597=c$596;
                var tmpvar$598=c$596$597.uppercased;
                next$c$596();
                return tmpvar$598;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$594,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$594;
    $$$c2.check((nums$599=$$$cl1.Tuple((2),$$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$600=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$600,nums$599)).equals($$$cl1.String("C:h #2",6)));
    var nums$599,chars$600;
    $$$c2.check((nums$601=$$$cl1.Tuple((4),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$602=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$603=$$$cl1.String("hola",4).iterator;
        var c$604=$$$cl1.getFinished();
        var next$c$604=function(){return c$604=it$603.next();}
        next$c$604();
        return function(){
            if(c$604!==$$$cl1.getFinished()){
                var c$604$605=c$604;
                var tmpvar$606=c$604$605;
                next$c$604();
                return tmpvar$606;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$602,nums$601)).equals($$$cl1.String("C:h #4",6)));
    var nums$601,chars$602;
    $$$c2.check((chars$607=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$607,$$$cl1.getEmpty())).equals($$$cl1.String("C:h #?",6)));
    var chars$607;
    $$$c2.check((chars$608=[$$$cl1.Character(72),$$$cl1.Character(73)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$608,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$608;
    testSpread();
    $$$c2.results();
}
exports.test=test;

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$609,desc$610,match$611){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$612 = iter$609.iterator;
    var i$613;while ((i$613=it$612.next())!==$$$cl1.getFinished()){
        if(match$611(i$613)){
            return $$$cl1.StringBuilder().appendAll([desc$610.string,$$$cl1.String(": ",2),i$613.string]).string;
        }
    }
    return $$$cl1.String("[NOT FOUND]",11);
};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$614,count$615,discount$616,comments$617){
    if(count$615===undefined){count$615=(1);}
    if(discount$616===undefined){discount$616=$$$cl1.Float(0.0);}
    if(comments$617===undefined){comments$617=$$$cl1.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$618=$$$cl1.String(", ",2).join($$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$619=comments$617.iterator;
        var c$620=$$$cl1.getFinished();
        var next$c$620=function(){return c$620=it$619.next();}
        next$c$620();
        return function(){
            if(c$620!==$$$cl1.getFinished()){
                var c$620$621=c$620;
                var tmpvar$622=$$$cl1.StringBuilder().appendAll([$$$cl1.String("\'",1),c$620$621.string,$$$cl1.String("\'",1)]).string;
                next$c$620();
                return tmpvar$622;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Order \'",7),product$614.string,$$$cl1.String("\', quantity ",12),count$615.string,$$$cl1.String(", discount ",11)]).string.plus($$$cl1.StringBuilder().appendAll([discount$616.string,$$$cl1.String(", comments: ",12),commentStr$618.string]).string);
};

//MethodDefinition testNamedArguments at named.ceylon (21:0-64:0)
function testNamedArguments(){
    $$$c2.check((iter$623=(function(){
        //ObjectArgument iter at named.ceylon (23:4-27:4)
        function iter$624(){
            var $$iter$624=new iter$624.$$;
            $$$cl1.Iterable($$iter$624);
            $$$cl1.add_type_arg($$iter$624,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$624,'Element',{t:$$$cl1.Integer});
            
            //AttributeGetterDefinition iterator at named.ceylon (24:6-26:6)
            $$$cl1.defineAttr($$iter$624,'iterator',function(){
                return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator;
            });
            return $$iter$624;
        }
        function $init$iter$624(){
            if (iter$624.$$===undefined){
                $$$cl1.initTypeProto(iter$624,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            return iter$624;
        }
        $init$iter$624();
        return iter$624(new iter$624.$$);
    }()),desc$625=(function(){
        //AttributeArgument desc at named.ceylon (28:4-30:4)
        return $$$cl1.String("Even",4);
    }()),match$626=function (i$627){
        return i$627.remainder((2)).equals((0));
    },namedFunc(iter$623,desc$625,match$626)).equals($$$cl1.String("Even: 8",7)),$$$cl1.String("named arguments 1",17));
    var iter$623,desc$625,match$626;
    $$$c2.check((iter$628=(function(){
        //ObjectArgument iter at named.ceylon (36:4-40:4)
        function iter$629(){
            var $$iter$629=new iter$629.$$;
            $$$cl1.Iterable($$iter$629);
            $$$cl1.add_type_arg($$iter$629,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$629,'Element',{t:$$$cl1.Integer});
            
            //AttributeGetterDefinition iterator at named.ceylon (37:6-39:6)
            $$$cl1.defineAttr($$iter$629,'iterator',function(){
                return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator;
            });
            return $$iter$629;
        }
        function $init$iter$629(){
            if (iter$629.$$===undefined){
                $$$cl1.initTypeProto(iter$629,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            return iter$629;
        }
        $init$iter$629();
        return iter$629(new iter$629.$$);
    }()),desc$630=(function(){
        //AttributeArgument desc at named.ceylon (41:4-43:4)
        return $$$cl1.String("Odd",3);
    }()),match$631=function (x$632){
        return x$632.remainder((2)).equals((1));
    },namedFunc(iter$628,desc$630,match$631)).equals($$$cl1.String("Odd: 9",6)),$$$cl1.String("named arguments 2",17));
    var iter$628,desc$630,match$631;
    $$$c2.check((desc$633=$$$cl1.String("Even",4),match$634=function (x$635){
        return x$635.equals((2));
    },iter$636=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),namedFunc(iter$636,desc$633,match$634)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 3",17));
    var desc$633,match$634,iter$636;
    $$$c2.check((desc$637=$$$cl1.String("Even",4),match$638=function (x$639){
        return x$639.equals((2));
    },iter$640=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (54:4-54:21)
        var it$641=$$$cl1.Range((10),(1),{Element:{t:$$$cl1.Integer}}).iterator;
        var i$642=$$$cl1.getFinished();
        var next$i$642=function(){return i$642=it$641.next();}
        next$i$642();
        return function(){
            if(i$642!==$$$cl1.getFinished()){
                var i$642$643=i$642;
                var tmpvar$644=i$642$643;
                next$i$642();
                return tmpvar$644;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),namedFunc(iter$640,desc$637,match$638)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 4",17));
    var desc$637,match$638,iter$640;
    $$$c2.check((product$645=$$$cl1.String("Mouse",5),order(product$645,undefined,undefined,undefined)).equals($$$cl1.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl1.String("defaulted & sequenced named [1]",31));
    var product$645;
    $$$c2.check((product$646=$$$cl1.String("Rhinoceros",10),discount$647=$$$cl1.Float(10.0),order(product$646,undefined,discount$647,undefined)).equals($$$cl1.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl1.String("defaulted & sequenced named [2]",31));
    var product$646,discount$647;
    $$$c2.check((product$648=$$$cl1.String("Bee",3),count$649=(531),comments$650=[$$$cl1.String("Express delivery",16).valueOf(),$$$cl1.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}),order(product$648,count$649,undefined,comments$650)).equals($$$cl1.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl1.String("defaulted & sequenced named [3]",31));
    var product$648,count$649,comments$650;
};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
    
    //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
    function somethingElse(x$651){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("something ",10),x$651.string,$$$cl1.String(" else",5)]).string;
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
    $$$cl1.defineAttr($$ambiguous1,'whatever',function(){
        return (1);
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (16:4-21:4)
    function somethingElse(x$652){
        if(x$652.remainder((2)).equals((0))){
            return $$ambiguous1.somethingElse$$invocations$AmbiguousParent(x$652);
        }
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous1 something ",21),x$652.string,$$$cl1.String(" else",5)]).string;
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
    $$$cl1.defineAttr($$ambiguous2,'whatever',function(){
        return (2);
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (28:4-30:4)
    function somethingElse(x$653){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous2 ",11),x$653.string,$$$cl1.String(" something else",15)]).string;
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
function QualifyAmbiguousSupertypes(one$654, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$654=one$654;
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
        return (opt$655=(one$654?$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1():null),opt$655!==null?opt$655:$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2());
        var opt$655;
    }
    $$qualifyAmbiguousSupertypes.doSomething=doSomething;
    
    //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
    $$$cl1.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
        if(one$654){
            return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous1;
        }
        return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous2;
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
    function somethingElse(x$656){
        return (opt$657=(one$654?$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1(x$656):null),opt$657!==null?opt$657:$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2(x$656));
        var opt$657;
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
    var a=(0);
    $$$cl1.defineAttr($$qualifiedA,'a',function(){return a;},function(a$658){return a=a$658;});
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
    $$$cl1.copySuperAttr($$qualifiedB,'a','$$invocations$QualifiedA');
    $$$cl1.copySuperAttr($$qualifiedB,'a','$$invocations$QualifiedA');
    $$$cl1.copySuperAttr($$qualifiedB,'a','$$invocations$QualifiedA');
    
    //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
    var a=(0);
    $$$cl1.defineAttr($$qualifiedB,'a',function(){return a;},function(a$659){return a=a$659;});
    
    //MethodDefinition f at qualified.ceylon (54:2-56:2)
    function f(){
        (olda$660=$$qualifiedB.a$$invocations$QualifiedA,$$qualifiedB.a$$invocations$QualifiedA=olda$660.successor,olda$660);
        var olda$660;
    }
    $$qualifiedB.f=f;
    
    //MethodDefinition g at qualified.ceylon (57:2-59:2)
    function g(){
        return ($$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a$$invocations$QualifiedA.successor,$$qualifiedB.a$$invocations$QualifiedA);
    }
    $$qualifiedB.g=g;
    
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
    function get(index$661){
        return null;
    }
    $$testList.get=get;
    
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
    function segment(from$662,length$663){
        return $$$cl1.getEmpty();
    }
    $$testList.segment=segment;
    
    //MethodDefinition span at qualified.ceylon (70:4-70:75)
    function span(from$664,to$665){
        return $$$cl1.getEmpty();
    }
    $$testList.span=span;
    
    //MethodDefinition spanTo at qualified.ceylon (71:4-71:63)
    function spanTo(to$666){
        return $$$cl1.getEmpty();
    }
    $$testList.spanTo=spanTo;
    
    //MethodDefinition spanFrom at qualified.ceylon (72:4-72:67)
    function spanFrom(from$667){
        return $$$cl1.getEmpty();
    }
    $$testList.spanFrom=spanFrom;
    
    //AttributeDeclaration iterator at qualified.ceylon (73:4-73:59)
    var iterator=$$$cl1.getEmptyIterator();
    $$$cl1.defineAttr($$testList,'iterator',function(){return iterator;});
    
    //MethodDefinition equals at qualified.ceylon (74:4-74:75)
    function equals(that$668){
        return $$testList.equals$$ceylon$language$List(that$668);
    }
    $$testList.equals=equals;
    
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
    return TestList;
}
exports.$init$TestList=$init$TestList;
$init$TestList();

//MethodDefinition testQualified at qualified.ceylon (78:0-97:0)
function testQualified(){
    
    //AttributeDeclaration q1 at qualified.ceylon (79:4-79:47)
    var q1$669=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (80:4-80:48)
    var q2$670=QualifyAmbiguousSupertypes(false);
    $$$c2.check(q1$669.doSomething().equals($$$cl1.String("ambiguous 1",11)),$$$cl1.String("qualified super calls [1]",25));
    $$$c2.check(q2$670.doSomething().equals($$$cl1.String("ambiguous 2",11)),$$$cl1.String("qualified super calls [2]",25));
    $$$c2.check(q1$669.whatever.equals((1)),$$$cl1.String("qualified super attrib [1]",26));
    $$$c2.check(q2$670.whatever.equals((2)),$$$cl1.String("qualified super attrib [2]",26));
    $$$c2.check(q1$669.somethingElse((5)).equals($$$cl1.String("Ambiguous1 something 5 else",27)),$$$cl1.String("qualified super method [1]",26));
    $$$c2.check(q1$669.somethingElse((6)).equals($$$cl1.String("something 6 else",16)),$$$cl1.String("qualified super method [2]",26));
    $$$c2.check(q2$670.somethingElse((5)).equals($$$cl1.String("Ambiguous2 5 something else",27)),$$$cl1.String("qualified super method [3]",26));
    $$$c2.check(q2$670.somethingElse((6)).equals($$$cl1.String("Ambiguous2 6 something else",27)),$$$cl1.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (89:4-89:27)
    var qb$671=QualifiedB();
    $$$c2.check(qb$671.a.equals(qb$671.supera),$$$cl1.String("Qualified attribute [1]",23));
    qb$671.f();
    $$$c2.check((tmp$672=qb$671,tmp$672.a=tmp$672.a.successor,tmp$672.a).equals(qb$671.supera),$$$cl1.String("Qualified attribute [2]",23));
    var tmp$672;
    $$$c2.check((tmp$673=qb$671,tmp$673.a=tmp$673.a.successor,tmp$673.a).equals(qb$671.g()),$$$cl1.String("Qualified attribute [3]",23));
    var tmp$673;
    
    //AttributeDeclaration tl at qualified.ceylon (94:4-94:25)
    var tl$674=TestList();
    $$$c2.check(tl$674.hash.equals($$$cl1.getEmpty().hash),$$$cl1.String("List::hash",10));
    $$$c2.check(tl$674.equals($$$cl1.getEmpty()),$$$cl1.String("List::equals",12));
};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$675){
    if(a$675===undefined){a$675=$$$cl1.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$676=(0);
    var setR$676=function(r$677){return r$676=r$677;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$678 = a$675.iterator;
    var i$679;while ((i$679=it$678.next())!==$$$cl1.getFinished()){
        (r$676=r$676.plus(i$679),r$676);
    }
    return r$676;
};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$680){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$681=(0);
    var setR$681=function(r$682){return r$681=r$682;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$683 = a$680.iterator;
    var i$684;while ((i$684=it$683.next())!==$$$cl1.getFinished()){
        (r$681=r$681.plus(i$684),r$681);
    }
    return r$681;
};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$685=$$$cl1.Tuple((8),$$$cl1.Tuple((9),$$$cl1.Tuple((10),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((6)),$$$cl1.String("spread [1]",10));
    $$$c2.check(spread1(ints$685).equals((27)),$$$cl1.String("spread [2]",10));
    $$$c2.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain(ints$685,{Element:{t:$$$cl1.Integer}})).equals((30)),$$$cl1.String("spread [3]",10));
    $$$c2.check(spread1($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$686=ints$685.iterator;
        var i$687=$$$cl1.getFinished();
        var next$i$687=function(){return i$687=it$686.next();}
        next$i$687();
        return function(){
            if(i$687!==$$$cl1.getFinished()){
                var i$687$688=i$687;
                var tmpvar$689=i$687$688.times((10));
                next$i$687();
                return tmpvar$689;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((270)),$$$cl1.String("spread [4]",10));
    $$$c2.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$690=ints$685.iterator;
        var i$691=$$$cl1.getFinished();
        var next$i$691=function(){return i$691=it$690.next();}
        next$i$691();
        return function(){
            if(i$691!==$$$cl1.getFinished()){
                var i$691$692=i$691;
                var tmpvar$693=i$691$692.times((10));
                next$i$691();
                return tmpvar$693;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((275)),$$$cl1.String("spread [5]",10));
    $$$c2.check((a$694=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$694)).equals((6)),$$$cl1.String("spread [6]",10));
    var a$694;
    $$$c2.check((a$695=ints$685,spread2(a$695)).equals((27)),$$$cl1.String("spread [7]",10));
    var a$695;
    $$$c2.check((a$696=[(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain(ints$685,{Element:{t:$$$cl1.Integer}}),spread2(a$696)).equals((30)),$$$cl1.String("spread [8]",10));
    var a$696;
    $$$c2.check((a$697=$$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$698=ints$685.iterator;
        var i$699=$$$cl1.getFinished();
        var next$i$699=function(){return i$699=it$698.next();}
        next$i$699();
        return function(){
            if(i$699!==$$$cl1.getFinished()){
                var i$699$700=i$699;
                var tmpvar$701=i$699$700.times((10));
                next$i$699();
                return tmpvar$701;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$697)).equals((270)),$$$cl1.String("spread [9]",10));
    var a$697;
    $$$c2.check((a$702=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$703=ints$685.iterator;
        var i$704=$$$cl1.getFinished();
        var next$i$704=function(){return i$704=it$703.next();}
        next$i$704();
        return function(){
            if(i$704!==$$$cl1.getFinished()){
                var i$704$705=i$704;
                var tmpvar$706=i$704$705.times((10));
                next$i$704();
                return tmpvar$706;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$702)).equals((275)),$$$cl1.String("spread [10]",11));
    var a$702;
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
