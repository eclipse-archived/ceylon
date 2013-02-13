(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2244=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$2245=$$$cl2243.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl2243.String("King",4);
};

//AttributeDeclaration flag at attributes.ceylon (9:0-9:25)
var flag$2246=(0);
var getFlag=function(){return flag$2246;};
exports.getFlag=getFlag;
var setFlag=function(flag$2247){return flag$2246=flag$2247;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$2248){
    setFlag((1));
};

//MethodDefinition test at attributes.ceylon (15:0-28:0)
function test(){
    $$$c2244.checkEqual(getLastName(),$$$cl2243.String("King",4),$$$cl2243.String("toplevel getter",15));
    setLastName($$$cl2243.String("Duke",4));
    $$$c2244.checkEqual(getFlag(),(1),$$$cl2243.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$2249=function(){
        return (5);
    };
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:25)
    var setX$2249=function(x$2250){
        setFlag((2));
    };
    $$$c2244.checkEqual(getX$2249(),(5),$$$cl2243.String("local getter",12));
    setX$2249((7));
    $$$c2244.checkEqual(getFlag(),(2),$$$cl2243.String("local setter",12));
    testNewSyntax();
    $$$c2244.results();
}
exports.test=test;

//MethodDefinition newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$2251,a$2252,b$2253){
    return f$2251(a$2252,b$2253);
};

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-10:0)
function TestNewSyntax(proc$2254, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    $$testNewSyntax.proc=proc$2254;
    return $$testNewSyntax;
}
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl2243.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl2243.Basic);
        (function($$testNewSyntax){
        })(TestNewSyntax.$$.prototype);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDefinition newSyntaxTest2 at syntax.ceylon (12:0-15:0)
function newSyntaxTest2(a$2255,b$2256){
    
    //AttributeDeclaration a at syntax.ceylon (13:2-13:11)
    var a$2257=a$2255;
    return a$2257.plus(b$2256);
};

//MethodDefinition testNewSyntax at syntax.ceylon (17:0-21:0)
function testNewSyntax(){
    $$$c2244.check(newSyntaxTest(function (i1$2258,i2$2259){
        return i1$2258.plus(i2$2259);
    },(2),(3)).equals((5)),$$$cl2243.String("new syntax for functions in methods",35));
    $$$c2244.check(TestNewSyntax(function (s$2260){
        return s$2260.getReversed();
    }).proc($$$cl2243.String("hola",4)).equals($$$cl2243.String("aloh",4)),$$$cl2243.String("new syntax for functions in classes",35));
    $$$c2244.check(newSyntaxTest(newSyntaxTest2,(6),(4)).equals((10)),$$$cl2243.String("new syntax for attributes in methods",36));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
