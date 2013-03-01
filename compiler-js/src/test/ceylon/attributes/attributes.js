(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$3=$$$cl1.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl1.String("King",4);
}
exports.getLastName=getLastName;

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

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc$12, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
    var privString$13=$$$cl1.String("0",1);
    $$$cl1.defineAttr($$testNewSyntax,'privString$13',function(){return privString$13;},function(privString$14){return privString$13=privString$14;});
    
    //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
    $$$cl1.defineAttr($$testNewSyntax,'pubString',function(){return privString$13;},function(pubString$15){return (privString$13=pubString$15,privString$13);});
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

//MethodDefinition newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$16,b$17){
    
    //AttributeDeclaration a at syntax.ceylon (16:2-16:11)
    var a$18=a$16;
    return a$18.plus(b$17);
};

//MethodDefinition testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c2.check(newSyntaxTest(function (i1$19,i2$20){
        return i1$19.plus(i2$20);
    },(2),(3)).equals((5)),$$$cl1.String("new syntax for functions in methods",35));
    $$$c2.check(TestNewSyntax(function (s$21){
        return s$21.reversed;
    }).proc($$$cl1.String("hola",4)).equals($$$cl1.String("aloh",4)),$$$cl1.String("new syntax for functions in classes",35));
    $$$c2.check(newSyntaxTest(newSyntaxTest2,(6),(4)).equals((10)),$$$cl1.String("new syntax for attributes in methods",36));
    
    //AttributeDeclaration fats at syntax.ceylon (24:2-24:45)
    var fats$22=TestNewSyntax(function (s$23){
        return s$23;
    });
    $$$c2.check(fats$22.pubString.equals($$$cl1.String("0",1)),$$$cl1.String("fat arrow getter",16));
    (tmp$24=fats$22,tmp$24.pubString=$$$cl1.String("HEY!",4),tmp$24.pubString);
    var tmp$24;
    $$$c2.check(fats$22.pubString.equals($$$cl1.String("HEY!",4)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("fat arrow setter still returns \'",32),fats$22.pubString.string,$$$cl1.String("\'",1)]).string);
};
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
