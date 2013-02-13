(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$3=$$$cl1.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl1.String("King",4);
};

//AttributeDeclaration flag at attributes.ceylon (9:0-9:25)
var flag$4=(0);
var getFlag=function(){return flag$4;};
exports.getFlag=getFlag;
var setFlag=function(flag$5){return flag$4=flag$5;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$6){
    setFlag((1));
};

//MethodDefinition test at attributes.ceylon (15:0-28:0)
function test(){
    $$$c2.checkEqual(getLastName(),$$$cl1.String("King",4),$$$cl1.String("toplevel getter",15));
    setLastName($$$cl1.String("Duke",4));
    $$$c2.checkEqual(getFlag(),(1),$$$cl1.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$7=function(){
        return (5);
    };
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:25)
    var setX$7=function(x$8){
        setFlag((2));
    };
    $$$c2.checkEqual(getX$7(),(5),$$$cl1.String("local getter",12));
    setX$7((7));
    $$$c2.checkEqual(getFlag(),(2),$$$cl1.String("local setter",12));
    testNewSyntax();
    $$$c2.results();
}
exports.test=test;

//MethodDefinition newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$9,a$10,b$11){
    return f$9(a$10,b$11);
};

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-10:0)
function TestNewSyntax(proc$12, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    $$testNewSyntax.proc=proc$12;
    return $$testNewSyntax;
}
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl1.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl1.Basic);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDefinition newSyntaxTest2 at syntax.ceylon (12:0-15:0)
function newSyntaxTest2(a$13,b$14){
    
    //AttributeDeclaration a at syntax.ceylon (13:2-13:11)
    var a$15=a$13;
    return a$15.plus(b$14);
};

//MethodDefinition testNewSyntax at syntax.ceylon (17:0-21:0)
function testNewSyntax(){
    $$$c2.check(newSyntaxTest(function (i1$16,i2$17){
        return i1$16.plus(i2$17);
    },(2),(3)).equals((5)),$$$cl1.String("new syntax for functions in methods",35));
    $$$c2.check(TestNewSyntax(function (s$18){
        return s$18.getReversed();
    }).proc($$$cl1.String("hola",4)).equals($$$cl1.String("aloh",4)),$$$cl1.String("new syntax for functions in classes",35));
    $$$c2.check(newSyntaxTest(newSyntaxTest2,(6),(4)).equals((10)),$$$cl1.String("new syntax for attributes in methods",36));
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
