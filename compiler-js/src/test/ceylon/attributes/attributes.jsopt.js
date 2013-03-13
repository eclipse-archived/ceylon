(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}};
var $$$cl2381=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2382=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$2383=$$$cl2381.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl2381.String("King",4);
}
exports.getLastName=getLastName;

//AttributeDeclaration flag at attributes.ceylon (9:0-9:25)
var flag$2384=(0);
var getFlag=function(){return flag$2384;};
exports.getFlag=getFlag;
var setFlag=function(flag$2385){return flag$2384=flag$2385;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$2386){
    setFlag((1));
};

//MethodDefinition test at attributes.ceylon (15:0-28:0)
function test(){
    $$$c2382.checkEqual(getLastName(),$$$cl2381.String("King",4),$$$cl2381.String("toplevel getter",15));
    setLastName($$$cl2381.String("Duke",4));
    $$$c2382.checkEqual(getFlag(),(1),$$$cl2381.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$2387=function(){
        return (5);
    };
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:25)
    var setX$2387=function(x$2388){
        setFlag((2));
    };
    $$$c2382.checkEqual(getX$2387(),(5),$$$cl2381.String("local getter",12));
    setX$2387((7));
    $$$c2382.checkEqual(getFlag(),(2),$$$cl2381.String("local setter",12));
    testNewSyntax();
    $$$c2382.results();
}
exports.test=test;

//MethodDefinition newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$2389,a$2390,b$2391){
    return f$2389(a$2390,b$2391);
};

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc$2392, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
    $$testNewSyntax.privString$2393_=$$$cl2381.String("0",1);
    
    //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
    $$testNewSyntax.proc=proc$2392;
    return $$testNewSyntax;
}
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl2381.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl2381.Basic);
        (function($$testNewSyntax){
            
            //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
            $$$cl2381.defineAttr($$testNewSyntax,'privString$2393',function(){return this.privString$2393_;},function(privString$2394){return this.privString$2393_=privString$2394;});
            
            //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
            $$$cl2381.defineAttr($$testNewSyntax,'pubString',function(){
                var $$testNewSyntax=this;
                return $$testNewSyntax.privString$2393;
            },function(pubString$2395){var $$testNewSyntax=this;
            return ($$testNewSyntax.privString$2393=pubString$2395);});
        })(TestNewSyntax.$$.prototype);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDefinition newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$2396,b$2397){
    
    //AttributeDeclaration a at syntax.ceylon (16:2-16:11)
    var a$2398=a$2396;
    return a$2398.plus(b$2397);
};

//MethodDefinition testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c2382.check(newSyntaxTest($$$cl2381.$JsCallable(function (i1$2399,i2$2400){
        return i1$2399.plus(i2$2400);
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Integer}}),(2),(3)).equals((5)),$$$cl2381.String("new syntax for functions in methods",35));
    $$$c2382.check(TestNewSyntax($$$cl2381.$JsCallable(function (s$2401){
        return s$2401.reversed;
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},Return:{t:$$$cl2381.String}})).proc($$$cl2381.String("hola",4)).equals($$$cl2381.String("aloh",4)),$$$cl2381.String("new syntax for functions in classes",35));
    $$$c2382.check(newSyntaxTest($$$cl2381.$JsCallable(newSyntaxTest2,{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},Return:{t:$$$cl2381.Integer}}),(6),(4)).equals((10)),$$$cl2381.String("new syntax for attributes in methods",36));
    
    //AttributeDeclaration fats at syntax.ceylon (24:2-24:45)
    var fats$2402=TestNewSyntax($$$cl2381.$JsCallable(function (s$2403){
        return s$2403;
    },{Arguments:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},Return:{t:$$$cl2381.String}}));
    $$$c2382.check(fats$2402.pubString.equals($$$cl2381.String("0",1)),$$$cl2381.String("fat arrow getter",16));
    (tmp$2404=fats$2402,tmp$2404.pubString=$$$cl2381.String("HEY!",4),tmp$2404.pubString);
    var tmp$2404;
    $$$c2382.check(fats$2402.pubString.equals($$$cl2381.String("HEY!",4)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("fat arrow setter still returns \'",32),fats$2402.pubString.string,$$$cl2381.String("\'",1)]).string);
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
