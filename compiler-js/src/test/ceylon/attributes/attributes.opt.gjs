(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","$mod-bin":"6.0","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"s"}]],"$hdn":"1","$mt":"prm","$pt":"f","$an":{"shared":[]},"$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastName"},"forwardAttributeTest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$mt":"tpm","$pk":"attributes","$nm":"Nothing"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"attr","$nm":"forwardAttributeTest"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$hdn":"1","$mt":"prm","$pt":"f","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$nm":"f"}},"$nm":"newSyntaxTest"},"fat3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"fat3"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"},"fat1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"fat1"},"fat2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"fat2"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//AttributeDecl firstName at attributes.ceylon (3:0-3:26)
var firstName$28;function $valinit$firstName$28(){if (firstName$28===undefined)firstName$28=$$$cl1.String("Gavin",5);return firstName$28;};$valinit$firstName$28();
function getFirstName(){return $valinit$firstName$28();}
var $prop$getFirstName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},d:['attributes','firstName']};}};
exports.$prop$getFirstName=$prop$getFirstName;
$prop$getFirstName.get=function(){return firstName$28};

//AttributeGetterDef lastName at attributes.ceylon (5:0-7:0)
function getLastName(){
    return $$$cl1.String("King",4);
}
exports.getLastName=getLastName;
var $prop$getLastName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},d:['attributes','lastName']};}};
exports.$prop$getLastName=$prop$getLastName;
$prop$getLastName.get=getLastName;
getLastName.$$metamodel$$=$prop$getLastName.$$metamodel$$;

//AttributeDecl flag at attributes.ceylon (9:0-9:25)
var flag$29;function $valinit$flag$29(){if (flag$29===undefined)flag$29=(0);return flag$29;};$valinit$flag$29();
function getFlag(){return $valinit$flag$29();}
exports.getFlag=getFlag;
function setFlag(flag$30){return flag$29=flag$30;};
exports.setFlag=setFlag;
var $prop$getFlag={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$an:function(){return[$$$cl1.variable()];},d:['attributes','flag']};}};
exports.$prop$getFlag=$prop$getFlag;
$prop$getFlag.get=getFlag;
getFlag.$$metamodel$$=$prop$getFlag.$$metamodel$$;
$prop$getFlag.set=setFlag;
if (setFlag.$$metamodel$$===undefined)setFlag.$$metamodel$$=$prop$getFlag.$$metamodel$$;

//AttributeSetterDef lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$31){
    setFlag((1));
};setLastName.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['attributes','lastName']};};
$prop$getLastName.set=setLastName;
if (setLastName.$$metamodel$$===undefined)setLastName.$$metamodel$$=$prop$getLastName.$$metamodel$$;

//MethodDef test at attributes.ceylon (16:0-30:0)
function test(){
    $$$c2.checkEqual(getLastName(),$$$cl1.String("King",4),$$$cl1.String("toplevel getter",15));
    setLastName($$$cl1.String("Duke",4));
    $$$c2.checkEqual(getFlag(),(1),$$$cl1.String("toplevel setter",15));
    
    //AttributeGetterDef x at attributes.ceylon (21:4-21:26)
    function getX$32(){
        return (5);
    }
    ;$prop$getX$32={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['attributes','test','$at','x']};}};
    $prop$getX$32.get=function(){return x$32};
    
    //AttributeSetterDef x at attributes.ceylon (22:4-22:25)
    var setX$32=function(x$33){
        setFlag((2));
    };setX$32.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['attributes','test','$at','x']};};
    $prop$getX$32.set=setX$32;
    if (setX$32.$$metamodel$$===undefined)setX$32.$$metamodel$$=$prop$getX$32.$$metamodel$$;
    $$$c2.checkEqual(getX$32(),(5),$$$cl1.String("local getter",12));
    setX$32((7));
    $$$c2.checkEqual(getFlag(),(2),$$$cl1.String("local setter",12));
    testNewSyntax();
    $$$c2.check(getForwardAttributeTest().first.equals((1)),$$$cl1.String("forwardAttributeTest",20));
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['attributes','test']};};

//AttributeDecl forwardAttributeTest at attributes.ceylon (31:0-31:54)
var forwardAttributeTest$34;function $valinit$forwardAttributeTest$34(){if (forwardAttributeTest$34===undefined)forwardAttributeTest$34=[getFat1(),getFat2(),getFat3()].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}});return forwardAttributeTest$34;};$valinit$forwardAttributeTest$34();
function getForwardAttributeTest(){return $valinit$forwardAttributeTest$34();}
exports.getForwardAttributeTest=getForwardAttributeTest;
var $prop$getForwardAttributeTest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Iterable,a:{Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}},d:['attributes','forwardAttributeTest']};}};
exports.$prop$getForwardAttributeTest=$prop$getForwardAttributeTest;
$prop$getForwardAttributeTest.get=getForwardAttributeTest;
getForwardAttributeTest.$$metamodel$$=$prop$getForwardAttributeTest.$$metamodel$$;

//AttributeDecl fat1 at attributes.ceylon (32:0-32:16)
var fat1$35;function $valinit$fat1$35(){if (fat1$35===undefined)fat1$35=(1);return fat1$35;};$valinit$fat1$35();
function getFat1(){return $valinit$fat1$35();}
var $prop$getFat1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['attributes','fat1']};}};
exports.$prop$getFat1=$prop$getFat1;
$prop$getFat1.get=function(){return fat1$35};

//AttributeGetterDef fat2 at attributes.ceylon (33:0-33:25)
function getFat2(){
    return (2);
}
;var $prop$getFat2={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['attributes','fat2']};}};
exports.$prop$getFat2=$prop$getFat2;
$prop$getFat2.get=function(){return fat2$36};

//AttributeDecl fat3 at attributes.ceylon (34:0-34:17)
function getFat3(){return (3);};
var $prop$getFat3={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['attributes','fat3']};}};
exports.$prop$getFat3=$prop$getFat3;
$prop$getFat3.get=function(){return fat3$37};
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$38,a$39,b$40){
    return f$38(a$39,b$40);
};newSyntaxTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['attributes','newSyntaxTest']};};

//ClassDef TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDecl privString at syntax.ceylon (9:2-9:34)
    $$testNewSyntax.privString$41_=$$$cl1.String("0",1);
    $$testNewSyntax.$prop$getPrivString$41={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:TestNewSyntax,$an:function(){return[$$$cl1.variable()];},d:['attributes','TestNewSyntax','$at','privString']};}};
    $$testNewSyntax.$prop$getPrivString$41.get=function(){return privString$41};
    
    //AttributeDecl pubString at syntax.ceylon (10:2-10:39)
    $$testNewSyntax.$prop$getPubString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:TestNewSyntax,$an:function(){return[$$$cl1.shared()];},d:['attributes','TestNewSyntax','$at','pubString']};}};
    $$testNewSyntax.$prop$getPubString.get=function(){return pubString};
    $$testNewSyntax.proc=proc;
    return $$testNewSyntax;
}
TestNewSyntax.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[{$nm:'proc',$mt:'prm',$pt:'f',$t:{t:$$$cl1.String},$an:function(){return[$$$cl1.shared()];}}],d:['attributes','TestNewSyntax']};};
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl1.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl1.Basic);
        (function($$testNewSyntax){
            
            //AttributeDecl privString at syntax.ceylon (9:2-9:34)
            $$$cl1.defineAttr($$testNewSyntax,'privString$41',function(){return this.privString$41_;},function(privString$42){return this.privString$41_=privString$42;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:TestNewSyntax,$an:function(){return[$$$cl1.variable()];},d:['attributes','TestNewSyntax','$at','privString']};});
            
            //AttributeDecl pubString at syntax.ceylon (10:2-10:39)
            $$$cl1.defineAttr($$testNewSyntax,'pubString',function(){
                var $$testNewSyntax=this;
                return $$testNewSyntax.privString$41;
            },function(pubString$43){var $$testNewSyntax=this;
            return ($$testNewSyntax.privString$41=pubString$43);},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.String},$cont:TestNewSyntax,$an:function(){return[$$$cl1.shared()];},d:['attributes','TestNewSyntax','$at','pubString']};});
        })(TestNewSyntax.$$.prototype);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDef newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$44,b$45){
    
    //AttributeDecl a at syntax.ceylon (16:2-16:11)
    var a$44=a$44;
    return a$44.plus(b$45);
};newSyntaxTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],d:['attributes','newSyntaxTest2']};};

//MethodDef testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c2.check(newSyntaxTest($$$cl1.$JsCallable((function (i1$46,i2$47){
        return i1$46.plus(i2$47);
    }),[{$nm:'i1',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer},{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Integer}}),(2),(3)).equals((5)),$$$cl1.String("new syntax for functions in methods",35));
    $$$c2.check(TestNewSyntax($$$cl1.$JsCallable((function (s$48){
        return s$48.reversed;
    }),[{$nm:'s',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.String}]},Return:{t:$$$cl1.String}})).proc($$$cl1.String("hola",4)).equals($$$cl1.String("aloh",4)),$$$cl1.String("new syntax for functions in classes",35));
    $$$c2.check(newSyntaxTest($$$cl1.$JsCallable(newSyntaxTest2,[{$nm:'a',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl1.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.Integer},{t:$$$cl1.Integer}]},Return:{t:$$$cl1.Integer}}),(6),(4)).equals((10)),$$$cl1.String("new syntax for attributes in methods",36));
    
    //AttributeDecl fats at syntax.ceylon (24:2-24:45)
    var fats$49=TestNewSyntax($$$cl1.$JsCallable((function (s$50){
        return s$50;
    }),[{$nm:'s',$mt:'prm',$t:{t:$$$cl1.String},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl1.String}]},Return:{t:$$$cl1.String}}));
    $$$c2.check(fats$49.pubString.equals($$$cl1.String("0",1)),$$$cl1.String("fat arrow getter",16));
    (tmp$51=fats$49,tmp$51.pubString=$$$cl1.String("HEY!",4),tmp$51.pubString);
    var tmp$51;
    $$$c2.check(fats$49.pubString.equals($$$cl1.String("HEY!",4)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("fat arrow setter still returns \'",32),fats$49.pubString.string,$$$cl1.String("\'",1)]).string);
};testNewSyntax.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['attributes','testNewSyntax']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
