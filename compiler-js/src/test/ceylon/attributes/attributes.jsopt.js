(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"String"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$pt":"v","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$nm":"lastName"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Empty"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Tuple"}],"$pk":"ceylon.language","$nm":"Callable"},"$hdn":"1","$mt":"prm","$pt":"v","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$pt":"v","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"}}}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2593=require('check/0.1/check-0.1');

//AttributeDeclaration firstName at attributes.ceylon (3:0-3:26)
var firstName$2594=$$$cl2592.String("Gavin",5);

//AttributeGetterDefinition lastName at attributes.ceylon (5:0-7:0)
var getLastName=function(){
    return $$$cl2592.String("King",4);
}
exports.getLastName=getLastName;

//AttributeDeclaration flag at attributes.ceylon (9:0-9:25)
var flag$2595=(0);
var getFlag=function(){return flag$2595;};
exports.getFlag=getFlag;
var setFlag=function(flag$2596){return flag$2595=flag$2596;};
exports.setFlag=setFlag;

//AttributeSetterDefinition lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$2597){
    setFlag((1));
};

//MethodDefinition test at attributes.ceylon (15:0-28:0)
function test(){
    $$$c2593.checkEqual(getLastName(),$$$cl2592.String("King",4),$$$cl2592.String("toplevel getter",15));
    setLastName($$$cl2592.String("Duke",4));
    $$$c2593.checkEqual(getFlag(),(1),$$$cl2592.String("toplevel setter",15));
    
    //AttributeGetterDefinition x at attributes.ceylon (20:4-20:26)
    var getX$2598=function(){
        return (5);
    };
    
    //AttributeSetterDefinition x at attributes.ceylon (21:4-21:25)
    var setX$2598=function(x$2599){
        setFlag((2));
    };
    $$$c2593.checkEqual(getX$2598(),(5),$$$cl2592.String("local getter",12));
    setX$2598((7));
    $$$c2593.checkEqual(getFlag(),(2),$$$cl2592.String("local setter",12));
    testNewSyntax();
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$2600,a$2601,b$2602){
    return f$2600(a$2601,b$2602);
};newSyntaxTest.$$metamodel$$={$nm:'newSyntaxTest',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'f',$mt:'prm',$t:{t:$$$cl2592.Callable,a:{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Integer}}}},{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'b',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};//newSyntaxTest.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Integer}};

//ClassDefinition TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc$2603, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
    $$testNewSyntax.privString$2604_=$$$cl2592.String("0",1);
    
    //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
    $$testNewSyntax.proc=proc$2603;
    return $$testNewSyntax;
}
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl2592.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl2592.Basic);
        (function($$testNewSyntax){
            
            //AttributeDeclaration privString at syntax.ceylon (9:2-9:34)
            $$$cl2592.defineAttr($$testNewSyntax,'privString$2604',function(){return this.privString$2604_;},function(privString$2605){return this.privString$2604_=privString$2605;});
            
            //AttributeDeclaration pubString at syntax.ceylon (10:2-10:39)
            $$$cl2592.defineAttr($$testNewSyntax,'pubString',function(){
                var $$testNewSyntax=this;
                return $$testNewSyntax.privString$2604;
            },function(pubString$2606){var $$testNewSyntax=this;
            return ($$testNewSyntax.privString$2604=pubString$2606);});
        })(TestNewSyntax.$$.prototype);
    }
    TestNewSyntax.$$.$$metamodel$$={$nm:'TestNewSyntax',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDefinition newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$2607,b$2608){
    
    //AttributeDeclaration a at syntax.ceylon (16:2-16:11)
    var a$2609=a$2607;
    return a$2609.plus(b$2608);
};newSyntaxTest2.$$metamodel$$={$nm:'newSyntaxTest2',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'b',$mt:'prm',$t:{t:$$$cl2592.Integer}}]};//newSyntaxTest2.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Integer}};

//MethodDefinition testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c2593.check(newSyntaxTest($$$cl2592.$JsCallable(function (i1$2610,i2$2611){
        return i1$2610.plus(i2$2611);
    },[{$nm:'i1',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'i2',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Integer}}),(2),(3)).equals((5)),$$$cl2592.String("new syntax for functions in methods",35));
    $$$c2593.check(TestNewSyntax($$$cl2592.$JsCallable(function (s$2612){
        return s$2612.reversed;
    },[{$nm:'s',$mt:'prm',$t:{t:$$$cl2592.String}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}})).proc($$$cl2592.String("hola",4)).equals($$$cl2592.String("aloh",4)),$$$cl2592.String("new syntax for functions in classes",35));
    $$$c2593.check(newSyntaxTest($$$cl2592.$JsCallable(newSyntaxTest2,[{$nm:'a',$mt:'prm',$t:{t:$$$cl2592.Integer}},{$nm:'b',$mt:'prm',$t:{t:$$$cl2592.Integer}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},Return:{t:$$$cl2592.Integer}}),(6),(4)).equals((10)),$$$cl2592.String("new syntax for attributes in methods",36));
    
    //AttributeDeclaration fats at syntax.ceylon (24:2-24:45)
    var fats$2613=TestNewSyntax($$$cl2592.$JsCallable(function (s$2614){
        return s$2614;
    },[{$nm:'s',$mt:'prm',$t:{t:$$$cl2592.String}}],{Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},Return:{t:$$$cl2592.String}}));
    $$$c2593.check(fats$2613.pubString.equals($$$cl2592.String("0",1)),$$$cl2592.String("fat arrow getter",16));
    (tmp$2615=fats$2613,tmp$2615.pubString=$$$cl2592.String("HEY!",4),tmp$2615.pubString);
    var tmp$2615;
    $$$c2593.check(fats$2613.pubString.equals($$$cl2592.String("HEY!",4)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("fat arrow setter still returns \'",32),fats$2613.pubString.string,$$$cl2592.String("\'",1)]).string);
};testNewSyntax.$$metamodel$$={$nm:'testNewSyntax',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testNewSyntax.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
