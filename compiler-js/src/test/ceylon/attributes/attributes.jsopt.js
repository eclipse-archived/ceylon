(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}};
var $$$cl2328=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2329=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$2330=$$$cl2328.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl2328.String("King",4);
}
exports.getLastName=getLastName;

//AttributeDeclaration flag at attributes.ceylon (9:0-9:25)
var flag$2331=(0);
var getFlag=function(){return flag$2331;};
exports.getFlag=getFlag;
var setFlag=function(flag$2332){return flag$2331=flag$2332;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$2333){
    setFlag((1));
};

//MethodDefinition test at attributes.ceylon (15:0-28:0)
function test(){
    $$$c2329.checkEqual(getLastName(),$$$cl2328.String("King",4),$$$cl2328.String("toplevel getter",15));
    setLastName($$$cl2328.String("Duke",4));
    $$$c2329.checkEqual(getFlag(),(1),$$$cl2328.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$2334=function(){
        return (5);
    };
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:25)
    var setX$2334=function(x$2335){
        setFlag((2));
    };
    $$$c2329.checkEqual(getX$2334(),(5),$$$cl2328.String("local getter",12));
    setX$2334((7));
    $$$c2329.checkEqual(getFlag(),(2),$$$cl2328.String("local setter",12));
    testNewSyntax();
    $$$c2329.results();
}
exports.test=test;

//MethodDefinition newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$2336,a$2337,b$2338){
    return f$2336(a$2337,b$2338);
};

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc$2339, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
    $$testNewSyntax.privString$2340_=$$$cl2328.String("0",1);
    
    //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
    $$testNewSyntax.proc=proc$2339;
    return $$testNewSyntax;
}
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl2328.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl2328.Basic);
        (function($$testNewSyntax){
            
            //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
            $$$cl2328.defineAttr($$testNewSyntax,'privString$2340',function(){return this.privString$2340_;},function(privString$2341){return this.privString$2340_=privString$2341;});
            
            //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
            $$$cl2328.defineAttr($$testNewSyntax,'pubString',function(){
                var $$testNewSyntax=this;
                return $$testNewSyntax.privString$2340;
            },function(pubString$2342){var $$testNewSyntax=this;
            return ($$testNewSyntax.privString$2340=pubString$2342);});
        })(TestNewSyntax.$$.prototype);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDefinition newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$2343,b$2344){
    
    //AttributeDeclaration a at syntax.ceylon (16:2-16:11)
    var a$2345=a$2343;
    return a$2345.plus(b$2344);
};

//MethodDefinition testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c2329.check(newSyntaxTest(function (i1$2346,i2$2347){
        return i1$2346.plus(i2$2347);
    },(2),(3)).equals((5)),$$$cl2328.String("new syntax for functions in methods",35));
    $$$c2329.check(TestNewSyntax(function (s$2348){
        return s$2348.reversed;
    }).proc($$$cl2328.String("hola",4)).equals($$$cl2328.String("aloh",4)),$$$cl2328.String("new syntax for functions in classes",35));
    $$$c2329.check(newSyntaxTest(newSyntaxTest2,(6),(4)).equals((10)),$$$cl2328.String("new syntax for attributes in methods",36));
    
    //AttributeDeclaration fats at syntax.ceylon (24:2-24:45)
    var fats$2349=TestNewSyntax(function (s$2350){
        return s$2350;
    });
    $$$c2329.check(fats$2349.pubString.equals($$$cl2328.String("0",1)),$$$cl2328.String("fat arrow getter",16));
    (tmp$2351=fats$2349,tmp$2351.pubString=$$$cl2328.String("HEY!",4),tmp$2351.pubString);
    var tmp$2351;
    $$$c2329.check(fats$2349.pubString.equals($$$cl2328.String("HEY!",4)),$$$cl2328.StringBuilder().appendAll([$$$cl2328.String("fat arrow setter still returns \'",32),fats$2349.pubString.string,$$$cl2328.String("\'",1)]).string);
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
