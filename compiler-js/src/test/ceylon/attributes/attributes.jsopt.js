(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}};
var $$$cl2309=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2310=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$2311=$$$cl2309.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl2309.String("King",4);
}
exports.getLastName=getLastName;

//AttributeDeclaration flag at attributes.ceylon (9:0-9:25)
var flag$2312=(0);
var getFlag=function(){return flag$2312;};
exports.getFlag=getFlag;
var setFlag=function(flag$2313){return flag$2312=flag$2313;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$2314){
    setFlag((1));
};

//MethodDefinition test at attributes.ceylon (15:0-28:0)
function test(){
    $$$c2310.checkEqual(getLastName(),$$$cl2309.String("King",4),$$$cl2309.String("toplevel getter",15));
    setLastName($$$cl2309.String("Duke",4));
    $$$c2310.checkEqual(getFlag(),(1),$$$cl2309.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$2315=function(){
        return (5);
    };
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:25)
    var setX$2315=function(x$2316){
        setFlag((2));
    };
    $$$c2310.checkEqual(getX$2315(),(5),$$$cl2309.String("local getter",12));
    setX$2315((7));
    $$$c2310.checkEqual(getFlag(),(2),$$$cl2309.String("local setter",12));
    testNewSyntax();
    $$$c2310.results();
}
exports.test=test;

//MethodDefinition newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$2317,a$2318,b$2319){
    return f$2317(a$2318,b$2319);
};

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc$2320, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
    $$testNewSyntax.privString$2321_=$$$cl2309.String("0",1);
    
    //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
    $$testNewSyntax.proc=proc$2320;
    return $$testNewSyntax;
}
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl2309.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl2309.Basic);
        (function($$testNewSyntax){
            
            //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
            $$$cl2309.defineAttr($$testNewSyntax,'privString$2321',function(){return this.privString$2321_;},function(privString$2322){return this.privString$2321_=privString$2322;});
            
            //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
            $$$cl2309.defineAttr($$testNewSyntax,'pubString',function(){
                var $$testNewSyntax=this;
                return $$testNewSyntax.privString$2321;
            },function(pubString$2323){var $$testNewSyntax=this;
            return ($$testNewSyntax.privString$2321=pubString$2323,$$testNewSyntax.privString$2321);});
        })(TestNewSyntax.$$.prototype);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDefinition newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$2324,b$2325){
    
    //AttributeDeclaration a at syntax.ceylon (16:2-16:11)
    var a$2326=a$2324;
    return a$2326.plus(b$2325);
};

//MethodDefinition testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c2310.check(newSyntaxTest(function (i1$2327,i2$2328){
        return i1$2327.plus(i2$2328);
    },(2),(3)).equals((5)),$$$cl2309.String("new syntax for functions in methods",35));
    $$$c2310.check(TestNewSyntax(function (s$2329){
        return s$2329.reversed;
    }).proc($$$cl2309.String("hola",4)).equals($$$cl2309.String("aloh",4)),$$$cl2309.String("new syntax for functions in classes",35));
    $$$c2310.check(newSyntaxTest(newSyntaxTest2,(6),(4)).equals((10)),$$$cl2309.String("new syntax for attributes in methods",36));
    
    //AttributeDeclaration fats at syntax.ceylon (24:2-24:45)
    var fats$2330=TestNewSyntax(function (s$2331){
        return s$2331;
    });
    $$$c2310.check(fats$2330.pubString.equals($$$cl2309.String("0",1)),$$$cl2309.String("fat arrow getter",16));
    (tmp$2332=fats$2330,tmp$2332.pubString=$$$cl2309.String("HEY!",4),tmp$2332.pubString);
    var tmp$2332;
    $$$c2310.check(fats$2330.pubString.equals($$$cl2309.String("HEY!",4)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("fat arrow setter still returns \'",32),fats$2330.pubString.string,$$$cl2309.String("\'",1)]).string);
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
