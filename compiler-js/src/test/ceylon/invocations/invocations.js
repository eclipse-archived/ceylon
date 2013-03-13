(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1","functions\/0.1"],"$mod-name":"invocations","$mod-version":"0.1","invocations":{"AmbiguousParent":{"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"formal":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$an":{"shared":[],"formal":[]},"$nm":"whatever"}},"$nm":"AmbiguousParent"},"testQualified":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testQualified"},"testSpread":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSpread"},"Ambiguous1":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous1"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"mixseqs":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Character"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"chars"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"nums"}]],"$mt":"mthd","$nm":"mixseqs"},"Ambiguous2":{"satisfies":[{"$pk":"invocations","$nm":"AmbiguousParent"}],"$mt":"ifc","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"default":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"default":[],"actual":[]},"$nm":"whatever"}},"$nm":"Ambiguous2"},"QualifiedB":{"super":{"$pk":"invocations","$nm":"QualifiedA"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"g":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"g"}},"$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"a"},"supera":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[]},"$nm":"supera"}},"$nm":"QualifiedB"},"QualifiedA":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"a":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"default":[],"variable":[]},"$nm":"a"}},"$nm":"QualifiedA"},"spread2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread2"},"spread1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"}],"$pk":"ceylon.language","$nm":"Sequential"},"$mt":"prm","seq":"1","$pt":"v","$nm":"a"}]],"$mt":"mthd","$nm":"spread1"},"testNamedArguments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNamedArguments"},"TestList":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"satisfies":[{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"}],"$mt":"cls","$m":{"iterator":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"Iterator"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"iterator"},"get":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"}]},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"index"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"get"},"equals":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Object"},"$mt":"prm","$pt":"v","$nm":"that"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"equals"},"spanTo":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanTo"},"segment":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"length"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"segment"},"spanFrom":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"spanFrom"},"span":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"from"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"to"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"span"}},"$at":{"lastIndex":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"lastIndex"},"clone":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"clone"},"hash":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"hash"},"reversed":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"reversed"},"rest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"}],"$pk":"ceylon.language","$nm":"List"},"$mt":"attr","$an":{"shared":[],"actual":[]},"$nm":"rest"}},"$nm":"TestList"},"namedFunc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$nm":"iter"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"desc"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"i"}]],"$mt":"prm","$pt":"f","$nm":"match"}]],"$mt":"mthd","$nm":"namedFunc"},"order":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"product"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$def":"1","$nm":"count"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Float"},"$mt":"prm","$pt":"v","$def":"1","$nm":"discount"},{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Null"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"prm","$pt":"v","$def":"1","$nm":"comments"}]],"$mt":"mthd","$nm":"order"},"QualifyAmbiguousSupertypes":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Boolean"},"$mt":"prm","$pt":"v","$nm":"one"}],"satisfies":[{"$pk":"invocations","$nm":"Ambiguous1"},{"$pk":"invocations","$nm":"Ambiguous2"}],"$mt":"cls","$m":{"doSomething":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"doSomething"},"somethingElse":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[],"actual":[]},"$nm":"somethingElse"}},"$at":{"whatever":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"whatever"}},"$nm":"QualifyAmbiguousSupertypes"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$f595=require('functions/0.1/functions-0.1');
var $$$c2=require('check/0.1/check-0.1');

//MethodDeclaration mixseqs at invocations.ceylon (4:0-4:107)
var mixseqs=function (chars$596,nums$597){
    if(nums$597===undefined){nums$597=$$$cl1.getEmpty();}
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("C:",2),(opt$598=chars$596.first,opt$598!==null?opt$598:$$$cl1.String("?",1)).string,$$$cl1.String(" #",2),(opt$599=nums$597.get((0)),opt$599!==null?opt$599:$$$cl1.String("?",1)).string]).string;
};
var opt$598,opt$599;

//MethodDefinition test at invocations.ceylon (6:0-33:0)
function test(){
    $$$f595.helloWorld();
    ($$$f595.helloWorld());
    $$$f595.hello($$$cl1.String("world",5));
    (name$600=$$$cl1.String("world",5),$$$f595.hello(name$600));
    var name$600;
    $$$f595.helloAll([$$$cl1.String("someone",7),$$$cl1.String("someone else",12)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}));
    (names$601=$$$cl1.Tuple($$$cl1.String("someone",7),$$$cl1.Tuple($$$cl1.String("someone else",12),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),$$$f595.helloAll(names$601));
    var names$601;
    
    //AttributeDeclaration s1 at invocations.ceylon (13:4-13:28)
    var s1$602=$$$f595.toString((99));
    
    //AttributeDeclaration s2 at invocations.ceylon (14:4-14:36)
    var s2$603=(obj$604=(99),$$$f595.toString(obj$604));
    var obj$604;
    
    //AttributeDeclaration f1 at invocations.ceylon (15:4-15:29)
    var f1$605=$$$f595.add($$$cl1.Float(1.0),$$$cl1.Float(1.0).negativeValue);
    
    //AttributeDeclaration f2 at invocations.ceylon (16:4-16:37)
    var f2$606=(x$607=$$$cl1.Float(1.0),y$608=$$$cl1.Float(1.0).negativeValue,$$$f595.add(x$607,y$608));
    var x$607,y$608;
    
    //MethodDefinition p at invocations.ceylon (17:4-19:4)
    function p$609(i$610){
        $$$cl1.print(i$610);
    };
    $$$f595.repeat((10),$$$cl1.$JsCallable(p$609,{Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},Return:{t:$$$cl1.Anything}}));
    testNamedArguments();
    testQualified();
    $$$c2.check(mixseqs($$$cl1.Tuple($$$cl1.Character(97),$$$cl1.Tuple($$$cl1.Character(98),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}},First:{t:$$$cl1.Character},Element:{t:$$$cl1.Character}}),[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:a #1",6)));
    $$$c2.check(mixseqs([$$$cl1.Character(98),$$$cl1.Character(99)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}),[(2),(3),(4)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:b #2",6)));
    $$$c2.check(mixseqs($$$cl1.String("hola",4).sequence,[(3),(4),(5)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals($$$cl1.String("C:h #3",6)));
    $$$c2.check((chars$611=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (26:18-26:47)
        var it$612=$$$cl1.String("hola",4).iterator();
        var c$613=$$$cl1.getFinished();
        var next$c$613=function(){return c$613=it$612.next();}
        next$c$613();
        return function(){
            if(c$613!==$$$cl1.getFinished()){
                var c$613$614=c$613;
                var tmpvar$615=c$613$614.uppercased;
                next$c$613();
                return tmpvar$615;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$611,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$611;
    $$$c2.check((nums$616=$$$cl1.Tuple((2),$$$cl1.Tuple((1),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$617=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$617,nums$616)).equals($$$cl1.String("C:h #2",6)));
    var nums$616,chars$617;
    $$$c2.check((nums$618=$$$cl1.Tuple((4),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),chars$619=$$$cl1.Comprehension(function(){
        //Comprehension at invocations.ceylon (28:28-28:46)
        var it$620=$$$cl1.String("hola",4).iterator();
        var c$621=$$$cl1.getFinished();
        var next$c$621=function(){return c$621=it$620.next();}
        next$c$621();
        return function(){
            if(c$621!==$$$cl1.getFinished()){
                var c$621$622=c$621;
                var tmpvar$623=c$621$622;
                next$c$621();
                return tmpvar$623;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$619,nums$618)).equals($$$cl1.String("C:h #4",6)));
    var nums$618,chars$619;
    $$$c2.check((chars$624=$$$cl1.String("hola",4).valueOf(),mixseqs(chars$624,$$$cl1.getEmpty())).equals($$$cl1.String("C:h #?",6)));
    var chars$624;
    $$$c2.check((chars$625=[$$$cl1.Character(72),$$$cl1.Character(73)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}),mixseqs(chars$625,$$$cl1.getEmpty())).equals($$$cl1.String("C:H #?",6)));
    var chars$625;
    testSpread();
    $$$c2.results();
}
exports.test=test;

//MethodDefinition namedFunc at named.ceylon (4:0-11:0)
function namedFunc(iter$626,desc$627,match$628){
    //'for' statement at named.ceylon (5:2-9:2)
    var it$629 = iter$626.iterator();
    var i$630;while ((i$630=it$629.next())!==$$$cl1.getFinished()){
        if(match$628(i$630)){
            return $$$cl1.StringBuilder().appendAll([desc$627.string,$$$cl1.String(": ",2),i$630.string]).string;
        }
    }
    return $$$cl1.String("[NOT FOUND]",11);
};

//MethodDefinition order at named.ceylon (14:0-19:0)
function order(product$631,count$632,discount$633,comments$634){
    if(count$632===undefined){count$632=(1);}
    if(discount$633===undefined){discount$633=$$$cl1.Float(0.0);}
    if(comments$634===undefined){comments$634=$$$cl1.getEmpty();}
    
    //AttributeDeclaration commentStr at named.ceylon (16:4-16:64)
    var commentStr$635=(strings$636=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (16:34-16:62)
        var it$637=comments$634.iterator();
        var c$638=$$$cl1.getFinished();
        var next$c$638=function(){return c$638=it$637.next();}
        next$c$638();
        return function(){
            if(c$638!==$$$cl1.getFinished()){
                var c$638$639=c$638;
                var tmpvar$640=$$$cl1.StringBuilder().appendAll([$$$cl1.String("\'",1),c$638$639.string,$$$cl1.String("\'",1)]).string;
                next$c$638();
                return tmpvar$640;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}),(opt$641=$$$cl1.String(", ",2),$$$cl1.JsCallable(opt$641,opt$641!==null?opt$641.join:null))(strings$636));
    var strings$636,opt$641;
    return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Order \'",7),product$631.string,$$$cl1.String("\', quantity ",12),count$632.string,$$$cl1.String(", discount ",11)]).string.plus($$$cl1.StringBuilder().appendAll([discount$633.string,$$$cl1.String(", comments: ",12),commentStr$635.string]).string);
};

//MethodDefinition testNamedArguments at named.ceylon (21:0-63:0)
function testNamedArguments(){
    $$$c2.check((iter$642=(function(){
        //ObjectArgument iter at named.ceylon (23:4-26:4)
        function iter$643(){
            var $$iter$643=new iter$643.$$;
            $$$cl1.Iterable($$iter$643);
            $$$cl1.add_type_arg($$iter$643,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$643,'Element',{t:$$$cl1.Integer});
            
            //MethodDeclaration iterator at named.ceylon (24:6-25:38)
            var iterator=function (){
                return [(1),(3),(5),(8),(9)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator();
            };
            $$iter$643.iterator=iterator;
            return $$iter$643;
        }
        function $init$iter$643(){
            if (iter$643.$$===undefined){
                $$$cl1.initTypeProto(iter$643,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            return iter$643;
        }
        $init$iter$643();
        return iter$643(new iter$643.$$);
    }()),desc$644=(function(){
        //AttributeArgument desc at named.ceylon (27:4-29:4)
        return $$$cl1.String("Even",4);
    }()),match$645=function (i$646){
        return i$646.remainder((2)).equals((0));
    },namedFunc(iter$642,desc$644,match$645)).equals($$$cl1.String("Even: 8",7)),$$$cl1.String("named arguments 1",17));
    var iter$642,desc$644,match$645;
    $$$c2.check((iter$647=(function(){
        //ObjectArgument iter at named.ceylon (35:4-39:4)
        function iter$648(){
            var $$iter$648=new iter$648.$$;
            $$$cl1.Iterable($$iter$648);
            $$$cl1.add_type_arg($$iter$648,'Absent',{t:$$$cl1.Null});
            $$$cl1.add_type_arg($$iter$648,'Element',{t:$$$cl1.Integer});
            
            //MethodDefinition iterator at named.ceylon (36:6-38:6)
            function iterator(){
                return [(2),(4),(6),(8),(9),(10)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).iterator();
            }
            $$iter$648.iterator=iterator;
            return $$iter$648;
        }
        function $init$iter$648(){
            if (iter$648.$$===undefined){
                $$$cl1.initTypeProto(iter$648,'invocations::testNamedArguments.iter',$$$cl1.Basic,$$$cl1.Iterable);
            }
            return iter$648;
        }
        $init$iter$648();
        return iter$648(new iter$648.$$);
    }()),desc$649=(function(){
        //AttributeArgument desc at named.ceylon (40:4-42:4)
        return $$$cl1.String("Odd",3);
    }()),match$650=function (x$651){
        return x$651.remainder((2)).equals((1));
    },namedFunc(iter$647,desc$649,match$650)).equals($$$cl1.String("Odd: 9",6)),$$$cl1.String("named arguments 2",17));
    var iter$647,desc$649,match$650;
    $$$c2.check((desc$652=$$$cl1.String("Even",4),match$653=function (x$654){
        return x$654.equals((2));
    },iter$655=[(1),(5),(4),(3),(2),(9)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),namedFunc(iter$655,desc$652,match$653)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 3",17));
    var desc$652,match$653,iter$655;
    $$$c2.check((desc$656=$$$cl1.String("Even",4),match$657=function (x$658){
        return x$658.equals((2));
    },iter$659=$$$cl1.Comprehension(function(){
        //Comprehension at named.ceylon (53:4-53:21)
        var it$660=$$$cl1.Range((10),(1),{Element:{t:$$$cl1.Integer}}).iterator();
        var i$661=$$$cl1.getFinished();
        var next$i$661=function(){return i$661=it$660.next();}
        next$i$661();
        return function(){
            if(i$661!==$$$cl1.getFinished()){
                var i$661$662=i$661;
                var tmpvar$663=i$661$662;
                next$i$661();
                return tmpvar$663;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),namedFunc(iter$659,desc$656,match$657)).equals($$$cl1.String("Even: 2",7)),$$$cl1.String("named arguments 4",17));
    var desc$656,match$657,iter$659;
    $$$c2.check((product$664=$$$cl1.String("Mouse",5),order(product$664,undefined,undefined,undefined)).equals($$$cl1.String("Order \'Mouse\', quantity 1, discount 0, comments: ",49)),$$$cl1.String("defaulted & sequenced named [1]",31));
    var product$664;
    $$$c2.check((product$665=$$$cl1.String("Rhinoceros",10),discount$666=$$$cl1.Float(10.0),order(product$665,undefined,discount$666,undefined)).equals($$$cl1.String("Order \'Rhinoceros\', quantity 1, discount 10, comments: ",55)),$$$cl1.String("defaulted & sequenced named [2]",31));
    var product$665,discount$666;
    $$$c2.check((product$667=$$$cl1.String("Bee",3),count$668=(531),comments$669=[$$$cl1.String("Express delivery",16).valueOf(),$$$cl1.String("Send individually",17).valueOf()].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.String}}),order(product$667,count$668,undefined,comments$669)).equals($$$cl1.String("Order \'Bee\', quantity 531, discount 0, comments: \'Express delivery\', \'Send individually\'",88)),$$$cl1.String("defaulted & sequenced named [3]",31));
    var product$667,count$668,comments$669;
};

//InterfaceDefinition AmbiguousParent at qualified.ceylon (4:0-10:0)
function AmbiguousParent($$ambiguousParent){
    
    //MethodDefinition somethingElse at qualified.ceylon (7:4-9:4)
    function somethingElse(x$670){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("something ",10),x$670.string,$$$cl1.String(" else",5)]).string;
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
    function somethingElse(x$671){
        if(x$671.remainder((2)).equals((0))){
            return $$ambiguous1.somethingElse$$invocations$AmbiguousParent(x$671);
        }
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous1 something ",21),x$671.string,$$$cl1.String(" else",5)]).string;
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
    function somethingElse(x$672){
        return $$$cl1.StringBuilder().appendAll([$$$cl1.String("Ambiguous2 ",11),x$672.string,$$$cl1.String(" something else",15)]).string;
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
function QualifyAmbiguousSupertypes(one$673, $$qualifyAmbiguousSupertypes){
    $init$QualifyAmbiguousSupertypes();
    if ($$qualifyAmbiguousSupertypes===undefined)$$qualifyAmbiguousSupertypes=new QualifyAmbiguousSupertypes.$$;
    $$qualifyAmbiguousSupertypes.one$673=one$673;
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
        return (opt$674=(one$673?$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous1():null),opt$674!==null?opt$674:$$qualifyAmbiguousSupertypes.doSomething$$invocations$Ambiguous2());
        var opt$674;
    }
    $$qualifyAmbiguousSupertypes.doSomething=doSomething;
    
    //AttributeGetterDefinition whatever at qualified.ceylon (38:4-43:4)
    $$$cl1.defineAttr($$qualifyAmbiguousSupertypes,'whatever',function(){
        if(one$673){
            return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous1;
        }
        return $$qualifyAmbiguousSupertypes.whatever$$invocations$Ambiguous2;
    });
    
    //MethodDefinition somethingElse at qualified.ceylon (44:4-46:4)
    function somethingElse(x$675){
        return (opt$676=(one$673?$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous1(x$675):null),opt$676!==null?opt$676:$$qualifyAmbiguousSupertypes.somethingElse$$invocations$Ambiguous2(x$675));
        var opt$676;
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
    $$$cl1.defineAttr($$qualifiedA,'a',function(){return a;},function(a$677){return a=a$677;});
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
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    $$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a;
    
    //AttributeDeclaration a at qualified.ceylon (53:2-53:36)
    var a=(0);
    $$$cl1.defineAttr($$qualifiedB,'a',function(){return a;},function(a$678){return a=a$678;});
    
    //MethodDefinition f at qualified.ceylon (54:2-56:2)
    function f(){
        (olda$679=$$qualifiedB.a$$invocations$QualifiedA,$$qualifiedB.a$$invocations$QualifiedA=olda$679.successor,olda$679);
        var olda$679;
    }
    $$qualifiedB.f=f;
    
    //MethodDefinition g at qualified.ceylon (57:2-59:2)
    function g(){
        return ($$qualifiedB.a$$invocations$QualifiedA=$$qualifiedB.a$$invocations$QualifiedA.successor);
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
    function get(index$680){
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
    function segment(from$681,length$682){
        return $$$cl1.getEmpty();
    }
    $$testList.segment=segment;
    
    //MethodDefinition span at qualified.ceylon (70:4-70:75)
    function span(from$683,to$684){
        return $$$cl1.getEmpty();
    }
    $$testList.span=span;
    
    //MethodDefinition spanTo at qualified.ceylon (71:4-71:63)
    function spanTo(to$685){
        return $$$cl1.getEmpty();
    }
    $$testList.spanTo=spanTo;
    
    //MethodDefinition spanFrom at qualified.ceylon (72:4-72:67)
    function spanFrom(from$686){
        return $$$cl1.getEmpty();
    }
    $$testList.spanFrom=spanFrom;
    
    //MethodDeclaration iterator at qualified.ceylon (73:4-73:62)
    var iterator=function (){
        return $$$cl1.getEmptyIterator();
    };
    $$testList.iterator=iterator;
    
    //MethodDefinition equals at qualified.ceylon (74:4-74:75)
    function equals(that$687){
        return $$testList.equals$$ceylon$language$List(that$687);
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
    var q1$688=QualifyAmbiguousSupertypes(true);
    
    //AttributeDeclaration q2 at qualified.ceylon (80:4-80:48)
    var q2$689=QualifyAmbiguousSupertypes(false);
    $$$c2.check(q1$688.doSomething().equals($$$cl1.String("ambiguous 1",11)),$$$cl1.String("qualified super calls [1]",25));
    $$$c2.check(q2$689.doSomething().equals($$$cl1.String("ambiguous 2",11)),$$$cl1.String("qualified super calls [2]",25));
    $$$c2.check(q1$688.whatever.equals((1)),$$$cl1.String("qualified super attrib [1]",26));
    $$$c2.check(q2$689.whatever.equals((2)),$$$cl1.String("qualified super attrib [2]",26));
    $$$c2.check(q1$688.somethingElse((5)).equals($$$cl1.String("Ambiguous1 something 5 else",27)),$$$cl1.String("qualified super method [1]",26));
    $$$c2.check(q1$688.somethingElse((6)).equals($$$cl1.String("something 6 else",16)),$$$cl1.String("qualified super method [2]",26));
    $$$c2.check(q2$689.somethingElse((5)).equals($$$cl1.String("Ambiguous2 5 something else",27)),$$$cl1.String("qualified super method [3]",26));
    $$$c2.check(q2$689.somethingElse((6)).equals($$$cl1.String("Ambiguous2 6 something else",27)),$$$cl1.String("qualified super method [4]",26));
    
    //AttributeDeclaration qb at qualified.ceylon (89:4-89:27)
    var qb$690=QualifiedB();
    $$$c2.check(qb$690.a.equals(qb$690.supera),$$$cl1.String("Qualified attribute [1]",23));
    qb$690.f();
    $$$c2.check((tmp$691=qb$690,tmp$691.a=tmp$691.a.successor).equals(qb$690.supera),$$$cl1.String("Qualified attribute [2]",23));
    var tmp$691;
    $$$c2.check((tmp$692=qb$690,tmp$692.a=tmp$692.a.successor).equals(qb$690.g()),$$$cl1.String("Qualified attribute [3]",23));
    var tmp$692;
    
    //AttributeDeclaration tl at qualified.ceylon (94:4-94:25)
    var tl$693=TestList();
    $$$c2.check(tl$693.hash.equals($$$cl1.getEmpty().hash),$$$cl1.String("List::hash",10));
    $$$c2.check(tl$693.equals($$$cl1.getEmpty()),$$$cl1.String("List::equals",12));
};

//MethodDefinition spread1 at spread.ceylon (4:0-10:0)
function spread1(a$694){
    if(a$694===undefined){a$694=$$$cl1.getEmpty();}
    
    //AttributeDeclaration r at spread.ceylon (5:2-5:23)
    var r$695=(0);
    var setR$695=function(r$696){return r$695=r$696;};
    //'for' statement at spread.ceylon (6:2-8:2)
    var it$697 = a$694.iterator();
    var i$698;while ((i$698=it$697.next())!==$$$cl1.getFinished()){
        (r$695=r$695.plus(i$698));
    }
    return r$695;
};

//MethodDefinition spread2 at spread.ceylon (12:0-18:0)
function spread2(a$699){
    
    //AttributeDeclaration r at spread.ceylon (13:2-13:23)
    var r$700=(0);
    var setR$700=function(r$701){return r$700=r$701;};
    //'for' statement at spread.ceylon (14:2-16:2)
    var it$702 = a$699.iterator();
    var i$703;while ((i$703=it$702.next())!==$$$cl1.getFinished()){
        (r$700=r$700.plus(i$703));
    }
    return r$700;
};

//MethodDefinition testSpread at spread.ceylon (20:0-32:0)
function testSpread(){
    
    //AttributeDeclaration ints at spread.ceylon (21:2-21:23)
    var ints$704=$$$cl1.Tuple((8),$$$cl1.Tuple((9),$$$cl1.Tuple((10),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check(spread1([(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((6)),$$$cl1.String("spread [1]",10));
    $$$c2.check(spread1(ints$704).equals((27)),$$$cl1.String("spread [2]",10));
    $$$c2.check(spread1([(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain(ints$704,{Element:{t:$$$cl1.Integer}})).equals((30)),$$$cl1.String("spread [3]",10));
    $$$c2.check(spread1($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (25:16-25:35)
        var it$705=ints$704.iterator();
        var i$706=$$$cl1.getFinished();
        var next$i$706=function(){return i$706=it$705.next();}
        next$i$706();
        return function(){
            if(i$706!==$$$cl1.getFinished()){
                var i$706$707=i$706;
                var tmpvar$708=i$706$707.times((10));
                next$i$706();
                return tmpvar$708;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((270)),$$$cl1.String("spread [4]",10));
    $$$c2.check(spread1([(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (26:20-26:39)
        var it$709=ints$704.iterator();
        var i$710=$$$cl1.getFinished();
        var next$i$710=function(){return i$710=it$709.next();}
        next$i$710();
        return function(){
            if(i$710!==$$$cl1.getFinished()){
                var i$710$711=i$710;
                var tmpvar$712=i$710$711.times((10));
                next$i$710();
                return tmpvar$712;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}})).equals((275)),$$$cl1.String("spread [5]",10));
    $$$c2.check((a$713=[(1),(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$713)).equals((6)),$$$cl1.String("spread [6]",10));
    var a$713;
    $$$c2.check((a$714=ints$704,spread2(a$714)).equals((27)),$$$cl1.String("spread [7]",10));
    var a$714;
    $$$c2.check((a$715=[(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain(ints$704,{Element:{t:$$$cl1.Integer}}),spread2(a$715)).equals((30)),$$$cl1.String("spread [8]",10));
    var a$715;
    $$$c2.check((a$716=$$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (30:16-30:35)
        var it$717=ints$704.iterator();
        var i$718=$$$cl1.getFinished();
        var next$i$718=function(){return i$718=it$717.next();}
        next$i$718();
        return function(){
            if(i$718!==$$$cl1.getFinished()){
                var i$718$719=i$718;
                var tmpvar$720=i$718$719.times((10));
                next$i$718();
                return tmpvar$720;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$716)).equals((270)),$$$cl1.String("spread [9]",10));
    var a$716;
    $$$c2.check((a$721=[(2),(3)].reifyCeylonType({Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}).chain($$$cl1.Comprehension(function(){
        //Comprehension at spread.ceylon (31:20-31:39)
        var it$722=ints$704.iterator();
        var i$723=$$$cl1.getFinished();
        var next$i$723=function(){return i$723=it$722.next();}
        next$i$723();
        return function(){
            if(i$723!==$$$cl1.getFinished()){
                var i$723$724=i$723;
                var tmpvar$725=i$723$724.times((10));
                next$i$723();
                return tmpvar$725;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Integer}}),spread2(a$721)).equals((275)),$$$cl1.String("spread [10]",11));
    var a$721;
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
