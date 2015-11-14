(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"attributes","$mod-version":"0.1","$mod-bin":"6.0","attributes":{"TestNewSyntax":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$ps":[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"s"}]],"$hdn":"1","$mt":"prm","$pt":"f","$an":{"shared":[]},"$nm":"proc"}],"$mt":"cls","$m":{"proc":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"prm","$nm":"s"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"proc"}},"$at":{"pubString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"pubString"},"privString":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"attr","$an":{"variable":[]},"$nm":"privString"}},"$nm":"TestNewSyntax"},"lastName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"var":"1","$mt":"gttr","$an":{"shared":[],"actual":[]},"$nm":"lastName"},"forwardAttributeTest":{"$t":{"$md":"ceylon.language","$tp":[{"$md":"ceylon.language","$mt":"tpm","$pk":"ceylon.language","$nm":"Integer"},{"$mt":"tpm","$pk":"attributes","$nm":"Nothing"}],"$pk":"ceylon.language","$nm":"Iterable"},"$mt":"attr","$nm":"forwardAttributeTest"},"flag":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"flag"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"newSyntaxTest2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$hdn":"1","$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$nm":"newSyntaxTest2"},"testNewSyntax":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNewSyntax"},"newSyntaxTest":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$hdn":"1","$mt":"prm","$pt":"f","$nm":"f"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$ps":[[{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"a"},{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"prm","$nm":"b"}]],"$mt":"mthd","$nm":"f"}},"$nm":"newSyntaxTest"},"fat3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"fat3"},"firstName":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"String"},"$mt":"attr","$nm":"firstName"},"fat1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"attr","$nm":"fat1"},"fat2":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"gttr","$nm":"fat2"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//AttributeDecl firstName at attributes.ceylon (3:0-3:26)
var firstName$4162;function $valinit$firstName$4162(){if (firstName$4162===undefined)firstName$4162=$$$cl4138.String("Gavin",5);return firstName$4162;};$valinit$firstName$4162();
function getFirstName(){return $valinit$firstName$4162();}
var $prop$getFirstName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},d:['attributes','firstName']};}};
exports.$prop$getFirstName=$prop$getFirstName;
$prop$getFirstName.get=function(){return firstName$4162};

//AttributeGetterDef lastName at attributes.ceylon (5:0-7:0)
function getLastName(){
    return $$$cl4138.String("King",4);
}
exports.getLastName=getLastName;
var $prop$getLastName={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},d:['attributes','lastName']};}};
exports.$prop$getLastName=$prop$getLastName;
$prop$getLastName.get=getLastName;
getLastName.$$metamodel$$=$prop$getLastName.$$metamodel$$;

//AttributeDecl flag at attributes.ceylon (9:0-9:25)
var flag$4163;function $valinit$flag$4163(){if (flag$4163===undefined)flag$4163=(0);return flag$4163;};$valinit$flag$4163();
function getFlag(){return $valinit$flag$4163();}
exports.getFlag=getFlag;
function setFlag(flag$4164){return flag$4163=flag$4164;};
exports.setFlag=setFlag;
var $prop$getFlag={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$an:function(){return[$$$cl4138.variable()];},d:['attributes','flag']};}};
exports.$prop$getFlag=$prop$getFlag;
$prop$getFlag.get=getFlag;
getFlag.$$metamodel$$=$prop$getFlag.$$metamodel$$;
$prop$getFlag.set=setFlag;
if (setFlag.$$metamodel$$===undefined)setFlag.$$metamodel$$=$prop$getFlag.$$metamodel$$;

//AttributeSetterDef lastName at attributes.ceylon (10:0-13:0)
var setLastName=function(lastName$4165){
    setFlag((1));
};setLastName.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['attributes','lastName']};};
$prop$getLastName.set=setLastName;
if (setLastName.$$metamodel$$===undefined)setLastName.$$metamodel$$=$prop$getLastName.$$metamodel$$;

//MethodDef test at attributes.ceylon (16:0-30:0)
function test(){
    $$$c4139.checkEqual(getLastName(),$$$cl4138.String("King",4),$$$cl4138.String("toplevel getter",15));
    setLastName($$$cl4138.String("Duke",4));
    $$$c4139.checkEqual(getFlag(),(1),$$$cl4138.String("toplevel setter",15));
    
    //AttributeGetterDef x at attributes.ceylon (21:4-21:26)
    function getX$4166(){
        return (5);
    }
    ;$prop$getX$4166={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['attributes','test','$at','x']};}};
    $prop$getX$4166.get=function(){return x$4166};
    
    //AttributeSetterDef x at attributes.ceylon (22:4-22:25)
    var setX$4166=function(x$4167){
        setFlag((2));
    };setX$4166.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['attributes','test','$at','x']};};
    $prop$getX$4166.set=setX$4166;
    if (setX$4166.$$metamodel$$===undefined)setX$4166.$$metamodel$$=$prop$getX$4166.$$metamodel$$;
    $$$c4139.checkEqual(getX$4166(),(5),$$$cl4138.String("local getter",12));
    setX$4166((7));
    $$$c4139.checkEqual(getFlag(),(2),$$$cl4138.String("local setter",12));
    testNewSyntax();
    $$$c4139.check(getForwardAttributeTest().first.equals((1)),$$$cl4138.String("forwardAttributeTest",20));
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['attributes','test']};};

//AttributeDecl forwardAttributeTest at attributes.ceylon (31:0-31:54)
var forwardAttributeTest$4168;function $valinit$forwardAttributeTest$4168(){if (forwardAttributeTest$4168===undefined)forwardAttributeTest$4168=[getFat1(),getFat2(),getFat3()].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Integer}});return forwardAttributeTest$4168;};$valinit$forwardAttributeTest$4168();
function getForwardAttributeTest(){return $valinit$forwardAttributeTest$4168();}
exports.getForwardAttributeTest=getForwardAttributeTest;
var $prop$getForwardAttributeTest={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Iterable,a:{Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Integer}}},d:['attributes','forwardAttributeTest']};}};
exports.$prop$getForwardAttributeTest=$prop$getForwardAttributeTest;
$prop$getForwardAttributeTest.get=getForwardAttributeTest;
getForwardAttributeTest.$$metamodel$$=$prop$getForwardAttributeTest.$$metamodel$$;

//AttributeDecl fat1 at attributes.ceylon (32:0-32:16)
var fat1$4169;function $valinit$fat1$4169(){if (fat1$4169===undefined)fat1$4169=(1);return fat1$4169;};$valinit$fat1$4169();
function getFat1(){return $valinit$fat1$4169();}
var $prop$getFat1={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['attributes','fat1']};}};
exports.$prop$getFat1=$prop$getFat1;
$prop$getFat1.get=function(){return fat1$4169};

//AttributeGetterDef fat2 at attributes.ceylon (33:0-33:25)
function getFat2(){
    return (2);
}
;var $prop$getFat2={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['attributes','fat2']};}};
exports.$prop$getFat2=$prop$getFat2;
$prop$getFat2.get=function(){return fat2$4170};

//AttributeDecl fat3 at attributes.ceylon (34:0-34:17)
function getFat3(){return (3);};
var $prop$getFat3={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['attributes','fat3']};}};
exports.$prop$getFat3=$prop$getFat3;
$prop$getFat3.get=function(){return fat3$4171};
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef newSyntaxTest at syntax.ceylon (3:0-6:0)
function newSyntaxTest(f$4172,a$4173,b$4174){
    return f$4172(a$4173,b$4174);
};newSyntaxTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['attributes','newSyntaxTest']};};

//ClassDef TestNewSyntax at syntax.ceylon (8:0-13:0)
function TestNewSyntax(proc, $$testNewSyntax){
    $init$TestNewSyntax();
    if ($$testNewSyntax===undefined)$$testNewSyntax=new TestNewSyntax.$$;
    
    //AttributeDecl privString at syntax.ceylon (9:2-9:34)
    var privString$4175=$$$cl4138.String("0",1);
    $$$cl4138.defineAttr($$testNewSyntax,'privString$4175',function(){return privString$4175;},function(privString$4176){return privString$4175=privString$4176;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:TestNewSyntax,$an:function(){return[$$$cl4138.variable()];},d:['attributes','TestNewSyntax','$at','privString']};});
    $$testNewSyntax.$prop$getPrivString$4175={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:TestNewSyntax,$an:function(){return[$$$cl4138.variable()];},d:['attributes','TestNewSyntax','$at','privString']};}};
    $$testNewSyntax.$prop$getPrivString$4175.get=function(){return privString$4175};
    
    //AttributeDecl pubString at syntax.ceylon (10:2-10:39)
    $$$cl4138.defineAttr($$testNewSyntax,'pubString',function(){return privString$4175;},function(pubString$4177){return (privString$4175=pubString$4177);},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:TestNewSyntax,$an:function(){return[$$$cl4138.shared()];},d:['attributes','TestNewSyntax','$at','pubString']};});
    $$testNewSyntax.$prop$getPubString={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.String},$cont:TestNewSyntax,$an:function(){return[$$$cl4138.shared()];},d:['attributes','TestNewSyntax','$at','pubString']};}};
    $$testNewSyntax.$prop$getPubString.get=function(){return pubString};
    $$testNewSyntax.proc=proc;
    return $$testNewSyntax;
}
TestNewSyntax.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[{$nm:'proc',$mt:'prm',$pt:'f',$t:{t:$$$cl4138.String},$an:function(){return[$$$cl4138.shared()];}}],d:['attributes','TestNewSyntax']};};
function $init$TestNewSyntax(){
    if (TestNewSyntax.$$===undefined){
        $$$cl4138.initTypeProto(TestNewSyntax,'attributes::TestNewSyntax',$$$cl4138.Basic);
    }
    return TestNewSyntax;
}
exports.$init$TestNewSyntax=$init$TestNewSyntax;
$init$TestNewSyntax();

//MethodDef newSyntaxTest2 at syntax.ceylon (15:0-18:0)
function newSyntaxTest2(a$4178,b$4179){
    
    //AttributeDecl a at syntax.ceylon (16:2-16:11)
    var a$4178=a$4178;
    return a$4178.plus(b$4179);
};newSyntaxTest2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],d:['attributes','newSyntaxTest2']};};

//MethodDef testNewSyntax at syntax.ceylon (20:0-28:0)
function testNewSyntax(){
    $$$c4139.check(newSyntaxTest($$$cl4138.$JsCallable((function (i1$4180,i2$4181){
        return i1$4180.plus(i2$4181);
    }),[{$nm:'i1',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'i2',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Integer}}),(2),(3)).equals((5)),$$$cl4138.String("new syntax for functions in methods",35));
    $$$c4139.check(TestNewSyntax($$$cl4138.$JsCallable((function (s$4182){
        return s$4182.reversed;
    }),[{$nm:'s',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.String}]},Return:{t:$$$cl4138.String}})).proc($$$cl4138.String("hola",4)).equals($$$cl4138.String("aloh",4)),$$$cl4138.String("new syntax for functions in classes",35));
    $$$c4139.check(newSyntaxTest($$$cl4138.$JsCallable(newSyntaxTest2,[{$nm:'a',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}},{$nm:'b',$mt:'prm',$t:{t:$$$cl4138.Integer},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Integer}]},Return:{t:$$$cl4138.Integer}}),(6),(4)).equals((10)),$$$cl4138.String("new syntax for attributes in methods",36));
    
    //AttributeDecl fats at syntax.ceylon (24:2-24:45)
    var fats$4183=TestNewSyntax($$$cl4138.$JsCallable((function (s$4184){
        return s$4184;
    }),[{$nm:'s',$mt:'prm',$t:{t:$$$cl4138.String},$an:function(){return[];}}],{Arguments:{t:'T', l:[{t:$$$cl4138.String}]},Return:{t:$$$cl4138.String}}));
    $$$c4139.check(fats$4183.pubString.equals($$$cl4138.String("0",1)),$$$cl4138.String("fat arrow getter",16));
    (tmp$4185=fats$4183,tmp$4185.pubString=$$$cl4138.String("HEY!",4),tmp$4185.pubString);
    var tmp$4185;
    $$$c4139.check(fats$4183.pubString.equals($$$cl4138.String("HEY!",4)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("fat arrow setter still returns \'",32),fats$4183.pubString.string,$$$cl4138.String("\'",1)]).string);
};testNewSyntax.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['attributes','testNewSyntax']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
