(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","$mod-bin":"6.0","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$m":{"x":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"x"}},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x0":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x0"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"i"}},"$nm":"C"}},"$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f":{"$t":{"$pk":"operators","$nm":"C1"},"$mt":"mthd","$nm":"f"}},"$c":{"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x0":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x0"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"i"}},"$nm":"C1"}},"$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"compareStringNumber":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"compareStringNumber"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"obj2_f":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"obj2_f"},"getNullsafe":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$pk":"operators","$nm":"NullsafeTest"}]},"$mt":"mthd","$nm":"getNullsafe"},"f4":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"f4"}},"$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f3"},"f2":{"$t":{"$pk":"operators","$nm":"C1"},"$mt":"mthd","$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f4"}},"$c":{"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x0":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x0"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"i"}},"$nm":"C1"}},"$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$m":{"f":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"f"}},"$nm":"nullsafeTest"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl1=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl1.$addmod$($$$cl1,'ceylon.language/1.0.0');
var $$$c2=require('check/0.1/check-0.1');
$$$cl1.$addmod$($$$c2,'check/0.1');

//MethodDef testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDecl seq at collections.ceylon (4:4-4:21)
    var seq$1073=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer},{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDecl lcomp at collections.ceylon (5:4-5:37)
    var lcomp$1074=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$1075=$$$cl1.String("hola",4).iterator();
        var c$1076=$$$cl1.getFinished();
        var next$c$1076=function(){return c$1076=it$1075.next();}
        next$c$1076();
        return function(){
            if(c$1076!==$$$cl1.getFinished()){
                var c$1076$1077=c$1076;
                var tmpvar$1078=c$1076$1077;
                next$c$1076();
                return tmpvar$1078;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDecl ecomp at collections.ceylon (6:4-6:37)
    var ecomp$1079=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$1080=$$$cl1.String("hola",4).iterator();
        var c$1081=$$$cl1.getFinished();
        var next$c$1081=function(){return c$1081=it$1080.next();}
        next$c$1081();
        return function(){
            if(c$1081!==$$$cl1.getFinished()){
                var c$1081$1082=c$1081;
                var tmpvar$1083=c$1081$1082;
                next$c$1081();
                return tmpvar$1083;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}).sequence;
    
    //AttributeDecl s2 at collections.ceylon (7:4-7:24)
    var s2$1084=[(0)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).chain(seq$1073,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}});
    
    //AttributeDecl s3 at collections.ceylon (8:4-8:28)
    var s3$1085=[$$$cl1.Character(65)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}).chain(lcomp$1074,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDecl t1 at collections.ceylon (9:4-9:20)
    var t1$1086=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:'T', l:[{t:$$$cl1.Integer},{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDecl t2 at collections.ceylon (10:4-10:22)
    var t2$1087=$$$cl1.Tuple((0),seq$1073,{Rest:{t:'T', l:[{t:$$$cl1.Integer},{t:$$$cl1.Integer},{t:$$$cl1.Integer}]},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.className(seq$1073).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{1,2,3} is not a Tuple but a ",29),$$$cl1.className(seq$1073).string]).string);
    $$$c2.check((!$$$cl1.className(lcomp$1074).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("lazy comprehension is a Tuple ",30),$$$cl1.className(lcomp$1074).string]).string);
    $$$c2.check($$$cl1.className(ecomp$1079).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("eager comprehension is not a Tuple but a ",41),$$$cl1.className(ecomp$1079).string]).string);
    $$$c2.check((!$$$cl1.className(s2$1084).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} is a Tuple ",20),$$$cl1.className(s2$1084).string]).string);
    $$$c2.check((!$$$cl1.className(s3$1085).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{x,*iter} is a Tuple ",21),$$$cl1.className(s3$1085).string]).string);
    $$$c2.check($$$cl1.className(t1$1086).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[1,2,3] is not a Tuple but a ",29),$$$cl1.className(t1$1086).string]).string);
    $$$c2.check($$$cl1.className(t2$1087).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[0,*seq] is not a Tuple but a ",30),$$$cl1.className(t2$1087).string]).string);
    $$$c2.check(seq$1073.equals(t1$1086),$$$cl1.String("{1,2,3} != [1,2,3]",18));
    $$$c2.check((!$$$cl1.className(t2$1087).equals($$$cl1.className(s2$1084))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} != [0,*seq] ",21),$$$cl1.className(t2$1087).string,$$$cl1.String(" vs",3),$$$cl1.className(s2$1084).string]).string);
    $$$c2.check(seq$1073.size.equals((3)),$$$cl1.String("seq.size!=3",11));
    $$$c2.check(lcomp$1074.sequence.size.equals((4)),$$$cl1.String("lcomp.size!=4",13));
    $$$c2.check(ecomp$1079.size.equals((4)),$$$cl1.String("ecomp.size!=4",13));
    $$$c2.check(s2$1084.size.equals((4)),$$$cl1.String("s2.size!=4",10));
    $$$c2.check(s3$1085.sequence.size.equals((5)),$$$cl1.String("s3.size!=5",10));
    $$$c2.check(t1$1086.size.equals((3)),$$$cl1.String("t1.size!=3",10));
    $$$c2.check(t2$1087.size.equals((4)),$$$cl1.String("t2.size!=4",10));
    $$$c2.check((!$$$cl1.className(lcomp$1074).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*comp} is not Tuple but ",25),$$$cl1.className(lcomp$1074).string]).string);
    $$$c2.check($$$cl1.className(ecomp$1079).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*ecomp} is not Tuple but ",26),$$$cl1.className(ecomp$1079).string]).string);
    $$$c2.check($$$cl1.className(seq$1073).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*seq} is not Tuple but ",24),$$$cl1.className(seq$1073).string]).string);
};testEnumerations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testEnumerations']};};
exports.$mod$ans$=function(){return[$$$cl1.by([$$$cl1.String("Enrique Zamudio",15),$$$cl1.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDecl i1 at operators.ceylon (5:4-5:28)
    var i1$1088=(-(4));
    function setI1$1088(i1$1089){return i1$1088=i1$1089;};
    i1$1088=(-i1$1088);
    $$$c2.check(i1$1088.equals((4)),$$$cl1.String("negation",8));
    i1$1088=(+(-(987654)));
    $$$c2.check(i1$1088.equals((-(987654))),$$$cl1.String("positive",8));
    i1$1088=(+(0));
    $$$c2.check(i1$1088.equals((0)),$$$cl1.String("+0=0",4));
    i1$1088=(-(0));
    $$$c2.check(i1$1088.equals((0)),$$$cl1.String("+0=0",4));
    
    //AttributeDecl i2 at operators.ceylon (15:4-15:35)
    var i2$1090=(123).plus((456));
    function setI2$1090(i2$1091){return i2$1090=i2$1091;};
    $$$c2.check(i2$1090.equals((579)),$$$cl1.String("addition",8));
    i1$1088=i2$1090.minus((16));
    $$$c2.check(i1$1088.equals((563)),$$$cl1.String("subtraction",11));
    i2$1090=(-i1$1088).plus(i2$1090).minus((1));
    $$$c2.check(i2$1090.equals((15)),$$$cl1.String("-i1+i2-1",8));
    i1$1088=(3).times((7));
    $$$c2.check(i1$1088.equals((21)),$$$cl1.String("multiplication",14));
    i2$1090=i1$1088.times((2));
    $$$c2.check(i2$1090.equals((42)),$$$cl1.String("multiplication",14));
    i2$1090=(17).divided((4));
    $$$c2.check(i2$1090.equals((4)),$$$cl1.String("integer division",16));
    i1$1088=i2$1090.times((516)).divided((-i1$1088));
    $$$c2.check(i1$1088.equals((-(98))),$$$cl1.String("i2*516/-i1",10));
    i1$1088=(15).remainder((4));
    $$$c2.check(i1$1088.equals((3)),$$$cl1.String("modulo",6));
    i2$1090=(312).remainder((12));
    $$$c2.check(i2$1090.equals((0)),$$$cl1.String("modulo",6));
    i1$1088=(2).power((10));
    $$$c2.check(i1$1088.equals((1024)),$$$cl1.String("power",5));
    i2$1090=(10).power((6));
    $$$c2.check(i2$1090.equals((1000000)),$$$cl1.String("power",5));
};testIntegerOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testIntegerOperators']};};

//MethodDef testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDecl f1 at operators.ceylon (44:4-44:28)
    var f1$1092=$$$cl1.Float(4.2).negativeValue;
    function setF1$1092(f1$1093){return f1$1092=f1$1093;};
    f1$1092=f1$1092.negativeValue;
    $$$c2.check(f1$1092.equals($$$cl1.Float(4.2)),$$$cl1.String("negation",8));
    f1$1092=(+$$$cl1.Float(987654.9925567).negativeValue);
    $$$c2.check(f1$1092.equals($$$cl1.Float(987654.9925567).negativeValue),$$$cl1.String("positive",8));
    f1$1092=(+$$$cl1.Float(0.0));
    $$$c2.check(f1$1092.equals($$$cl1.Float(0.0)),$$$cl1.String("+0.0=0.0",8));
    f1$1092=$$$cl1.Float(0.0).negativeValue;
    $$$c2.check(f1$1092.equals($$$cl1.Float(0.0)),$$$cl1.String("-0.0=0.0",8));
    
    //AttributeDecl f2 at operators.ceylon (54:4-54:42)
    var f2$1094=$$$cl1.Float(3.14159265).plus($$$cl1.Float(456.0));
    function setF2$1094(f2$1095){return f2$1094=f2$1095;};
    $$$c2.check(f2$1094.equals($$$cl1.Float(459.14159265)),$$$cl1.String("addition",8));
    f1$1092=f2$1094.minus($$$cl1.Float(0.0016));
    $$$c2.check(f1$1092.equals($$$cl1.Float(459.13999265)),$$$cl1.String("subtraction",11));
    f2$1094=f1$1092.negativeValue.plus(f2$1094).minus($$$cl1.Float(1.2));
    $$$c2.check(f2$1094.equals($$$cl1.Float(1.1984000000000037).negativeValue),$$$cl1.String("-f1+f2-1.2",10));
    f1$1092=$$$cl1.Float(3.0).times($$$cl1.Float(0.79));
    $$$c2.check(f1$1092.equals($$$cl1.Float(2.37)),$$$cl1.String("multiplication",14));
    f2$1094=f1$1092.times($$$cl1.Float(2.0e13));
    $$$c2.check(f2$1094.equals($$$cl1.Float(47400000000000.0)),$$$cl1.String("multiplication",14));
    f2$1094=$$$cl1.Float(17.1).divided($$$cl1.Float(4.0E-18));
    $$$c2.check(f2$1094.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("division",8));
    f1$1092=f2$1094.times($$$cl1.Float(51.6e2)).divided(f1$1092.negativeValue);
    $$$c2.check(f2$1094.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("f2*51.6e2/-f1",13));
    f1$1092=$$$cl1.Float(150.0).power($$$cl1.Float(0.5));
    $$$c2.check(f1$1092.equals($$$cl1.Float(12.24744871391589)),$$$cl1.String("power",5));
};testFloatOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testFloatOperators']};};

//ClassDef OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
OpTest1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['operators','OpTest1']};};
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl1.initTypeProto(OpTest1,'operators::OpTest1',$$$cl1.Basic);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDef testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDecl o1 at operators.ceylon (77:4-77:24)
    var o1$1096=OpTest1();
    
    //AttributeDecl o2 at operators.ceylon (78:4-78:24)
    var o2$1097=OpTest1();
    
    //AttributeDecl b1 at operators.ceylon (79:4-79:35)
    var b1$1098=(o1$1096===o2$1097);
    function setB1$1098(b1$1099){return b1$1098=b1$1099;};
    $$$c2.check((!b1$1098),$$$cl1.String("identity",8));
    
    //AttributeDecl b2 at operators.ceylon (81:4-81:35)
    var b2$1100=(o1$1096===o1$1096);
    function setB2$1100(b2$1101){return b2$1100=b2$1101;};
    $$$c2.check(b2$1100,$$$cl1.String("identity",8));
    b1$1098=o1$1096.equals(o2$1097);
    $$$c2.check((!b1$1098),$$$cl1.String("equals",6));
    b2$1100=o1$1096.equals(o1$1096);
    $$$c2.check(b2$1100,$$$cl1.String("equals",6));
    b1$1098=(1).equals((2));
    $$$c2.check((!b1$1098),$$$cl1.String("equals",6));
    b2$1100=(!(1).equals((2)));
    $$$c2.check(b2$1100,$$$cl1.String("not equal",9));
    
    //AttributeDecl b3 at operators.ceylon (92:4-92:29)
    var b3$1102=(!b2$1100);
    function setB3$1102(b3$1103){return b3$1102=b3$1103;};
    $$$c2.check((!b3$1102),$$$cl1.String("not",3));
    b1$1098=(true&&false);
    $$$c2.check((!b1$1098),$$$cl1.String("and",3));
    b2$1100=(b1$1098&&true);
    $$$c2.check((!b2$1100),$$$cl1.String("and",3));
    b3$1102=(true&&true);
    $$$c2.check(b3$1102,$$$cl1.String("and",3));
    b1$1098=(true||false);
    $$$c2.check(b1$1098,$$$cl1.String("or",2));
    b2$1100=(false||b1$1098);
    $$$c2.check(b2$1100,$$$cl1.String("or",2));
    b3$1102=(false||false);
    $$$c2.check((!b3$1102),$$$cl1.String("or",2));
};testBooleanOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testBooleanOperators']};};

//MethodDef testComparisonOperators at operators.ceylon (109:0-152:0)
function testComparisonOperators(){
    
    //AttributeDecl c1 at operators.ceylon (110:4-110:37)
    var c1$1104=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4));
    $$$c2.check(c1$1104.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDecl c2 at operators.ceylon (112:4-112:37)
    var c2$1105=$$$cl1.String("str2",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c2$1105.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDecl c3 at operators.ceylon (114:4-114:37)
    var c3$1106=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c3$1106.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDecl c4 at operators.ceylon (116:4-116:29)
    var c4$1107=$$$cl1.String("",0).compare($$$cl1.String("",0));
    $$$c2.check(c4$1107.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDecl c5 at operators.ceylon (118:4-118:33)
    var c5$1108=$$$cl1.String("str1",4).compare($$$cl1.String("",0));
    $$$c2.check(c5$1108.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDecl c6 at operators.ceylon (120:4-120:33)
    var c6$1109=$$$cl1.String("",0).compare($$$cl1.String("str2",4));
    $$$c2.check(c6$1109.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDecl b1 at operators.ceylon (123:4-123:41)
    var b1$1110=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getSmaller());
    function setB1$1110(b1$1111){return b1$1110=b1$1111;};
    $$$c2.check(b1$1110,$$$cl1.String("smaller",7));
    
    //AttributeDecl b2 at operators.ceylon (125:4-125:41)
    var b2$1112=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getLarger());
    function setB2$1112(b2$1113){return b2$1112=b2$1113;};
    $$$c2.check((!b2$1112),$$$cl1.String("larger",6));
    
    //AttributeDecl b3 at operators.ceylon (127:4-127:42)
    var b3$1114=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getLarger());
    function setB3$1114(b3$1115){return b3$1114=b3$1115;};
    $$$c2.check(b3$1114,$$$cl1.String("small as",8));
    
    //AttributeDecl b4 at operators.ceylon (129:4-129:42)
    var b4$1116=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getSmaller());
    function setB4$1116(b4$1117){return b4$1116=b4$1117;};
    $$$c2.check((!b4$1116),$$$cl1.String("large as",8));
    b1$1110=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getSmaller());
    $$$c2.check((!b1$1110),$$$cl1.String("smaller",7));
    b2$1112=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getLarger());
    $$$c2.check((!b2$1112),$$$cl1.String("larger",6));
    b3$1114=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getLarger());
    $$$c2.check(b3$1114,$$$cl1.String("small as",8));
    b4$1116=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getSmaller());
    $$$c2.check(b4$1116,$$$cl1.String("large as",8));
    
    //AttributeDecl a at operators.ceylon (140:4-140:15)
    var a$1118=(0);
    
    //AttributeDecl c at operators.ceylon (141:4-141:16)
    var c$1119=(10);
    $$$c2.check((tmpvar$1120=(5),tmpvar$1120.compare(a$1118)===$$$cl1.getLarger()&&tmpvar$1120.compare(c$1119)===$$$cl1.getSmaller()),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<5<",3),c$1119.string]).string);
    $$$c2.check((tmpvar$1121=(0),tmpvar$1121.compare(a$1118)!==$$$cl1.getSmaller()&&tmpvar$1121.compare(c$1119)===$$$cl1.getSmaller()),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<=0<",4),c$1119.string]).string);
    $$$c2.check((tmpvar$1122=(10),tmpvar$1122.compare(a$1118)===$$$cl1.getLarger()&&tmpvar$1122.compare(c$1119)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<10<=",5),c$1119.string]).string);
    $$$c2.check((tmpvar$1123=(0),tmpvar$1123.compare(a$1118)!==$$$cl1.getSmaller()&&tmpvar$1123.compare(c$1119)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<=0<=",5),c$1119.string]).string);
    $$$c2.check((tmpvar$1124=(10),tmpvar$1124.compare(a$1118)!==$$$cl1.getSmaller()&&tmpvar$1124.compare(c$1119)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<=10<=",6),c$1119.string]).string);
    $$$c2.check((!(tmpvar$1125=(15),tmpvar$1125.compare(a$1118)===$$$cl1.getLarger()&&tmpvar$1125.compare(c$1119)===$$$cl1.getSmaller())),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<15<",4),c$1119.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$1126=(10),tmpvar$1126.compare(a$1118)!==$$$cl1.getSmaller()&&tmpvar$1126.compare(c$1119)===$$$cl1.getSmaller())),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<=10<",5),c$1119.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$1127=(0),tmpvar$1127.compare(a$1118)===$$$cl1.getLarger()&&tmpvar$1127.compare(c$1119)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<0<=",4),c$1119.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$1128=(11),tmpvar$1128.compare(a$1118)!==$$$cl1.getSmaller()&&tmpvar$1128.compare(c$1119)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<=11<=",6),c$1119.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$1129=(-(1)),tmpvar$1129.compare(a$1118)!==$$$cl1.getSmaller()&&tmpvar$1129.compare(c$1119)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$1118.string,$$$cl1.String("<=-1<=",6),c$1119.string,$$$cl1.String(" WTF",4)]).string);
};testComparisonOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testComparisonOperators']};};

//MethodDef testOtherOperators at operators.ceylon (154:0-166:0)
function testOtherOperators(){
    
    //AttributeDecl entry at operators.ceylon (155:4-155:42)
    var entry$1130=$$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}});
    $$$c2.check(entry$1130.key.equals((47)),$$$cl1.String("entry key",9));
    $$$c2.check(entry$1130.item.equals($$$cl1.String("hi there",8)),$$$cl1.String("entry item",10));
    
    //AttributeDecl entry2 at operators.ceylon (158:4-158:30)
    var entry2$1131=$$$cl1.Entry(true,entry$1130,{Key:{t:$$$cl1.true$901},Item:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}}}});
    $$$c2.check(entry2$1131.key.equals(true),$$$cl1.String("entry key",9));
    $$$c2.check(entry2$1131.item.equals($$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}})),$$$cl1.String("entry item",10));
    
    //AttributeDecl s1 at operators.ceylon (162:4-162:41)
    var s1$1132=(opt$1133=(true?$$$cl1.String("ok",2):null),opt$1133!==null?opt$1133:$$$cl1.String("noo",3));
    var opt$1133;
    $$$c2.check(s1$1132.equals($$$cl1.String("ok",2)),$$$cl1.String("then/else 1",11));
    
    //AttributeDecl s2 at operators.ceylon (164:4-164:47)
    var s2$1134=(opt$1135=(false?$$$cl1.String("what?",5):null),opt$1135!==null?opt$1135:$$$cl1.String("great",5));
    var opt$1135;
    $$$c2.check(s2$1134.equals($$$cl1.String("great",5)),$$$cl1.String("then/else 2",11));
};testOtherOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testOtherOperators']};};

//MethodDef testCollectionOperators at operators.ceylon (168:0-180:0)
function testCollectionOperators(){
    
    //AttributeDecl seq1 at operators.ceylon (169:4-169:33)
    var seq1$1136=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDecl s1 at operators.ceylon (170:4-170:23)
    var s1$1137=seq1$1136.$get((0));
    $$$c2.check(s1$1137.equals($$$cl1.String("one",3)),$$$cl1.String("lookup",6));
    
    //AttributeDecl s2 at operators.ceylon (172:4-172:28)
    var s2$1138=seq1$1136.$get((2));
    $$$c2.check((!$$$cl1.exists(s2$1138)),$$$cl1.String("lookup",6));
    
    //AttributeDecl s3 at operators.ceylon (174:4-174:29)
    var s3$1139=seq1$1136.$get((-(1)));
    $$$c2.check((!$$$cl1.exists(s3$1139)),$$$cl1.String("lookup",6));
};testCollectionOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testCollectionOperators']};};

//ClassDef NullsafeTest at operators.ceylon (182:0-187:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    return $$nullsafeTest;
}
NullsafeTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['operators','NullsafeTest']};};
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl1.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl1.Basic);
        (function($$nullsafeTest){
            
            //MethodDef f at operators.ceylon (183:4-183:33)
            $$nullsafeTest.f=function f(){
                var $$nullsafeTest=this;
                return (1);
            };$$nullsafeTest.f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$ps:[],$cont:NullsafeTest,$an:function(){return[$$$cl1.shared()];},d:['operators','NullsafeTest','$m','f']};};
            
            //MethodDef f2 at operators.ceylon (184:4-186:4)
            $$nullsafeTest.f2=function f2(x$1140){
                var $$nullsafeTest=this;
                return x$1140();
            };$$nullsafeTest.f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[{$nm:'x',$mt:'prm',$pt:'f',$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$an:function(){return[];}}],$cont:NullsafeTest,$an:function(){return[$$$cl1.shared()];},d:['operators','NullsafeTest','$m','f2']};};
        })(NullsafeTest.$$.prototype);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDef nullsafeTest at operators.ceylon (189:0-191:0)
function nullsafeTest(f$1141){
    return f$1141();
};nullsafeTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$an:function(){return[];}}],d:['operators','nullsafeTest']};};

//MethodDef testNullsafeOperators at operators.ceylon (193:0-234:0)
function testNullsafeOperators(){
    
    //AttributeDecl seq at operators.ceylon (194:4-194:27)
    var seq$1142=$$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDecl s1 at operators.ceylon (195:4-195:34)
    var s1$1143=(opt$1144=seq$1142.$get((0)),opt$1144!==null?opt$1144:$$$cl1.String("null",4));
    var opt$1144;
    $$$c2.check(s1$1143.equals($$$cl1.String("hi",2)),$$$cl1.String("default 1",9));
    
    //AttributeDecl s2 at operators.ceylon (197:4-197:34)
    var s2$1145=(opt$1146=seq$1142.$get((1)),opt$1146!==null?opt$1146:$$$cl1.String("null",4));
    var opt$1146;
    $$$c2.check(s2$1145.equals($$$cl1.String("null",4)),$$$cl1.String("default 2",9));
    
    //AttributeDecl s3 at operators.ceylon (200:4-200:21)
    var s3$1147=null;
    
    //AttributeDecl s4 at operators.ceylon (201:4-201:23)
    var s4$1148=$$$cl1.String("test",4);
    
    //AttributeDecl s5 at operators.ceylon (202:4-202:42)
    var s5$1149=(opt$1150=(opt$1151=s3$1147,opt$1151!==null?opt$1151.uppercased:null),opt$1150!==null?opt$1150:$$$cl1.String("null",4));
    var opt$1150,opt$1151;
    
    //AttributeDecl s6 at operators.ceylon (203:4-203:42)
    var s6$1152=(opt$1153=(opt$1154=s4$1148,opt$1154!==null?opt$1154.uppercased:null),opt$1153!==null?opt$1153:$$$cl1.String("null",4));
    var opt$1153,opt$1154;
    $$$c2.check(s5$1149.equals($$$cl1.String("null",4)),$$$cl1.String("nullsafe member 1",17));
    $$$c2.check(s6$1152.equals($$$cl1.String("TEST",4)),$$$cl1.String("nullsafe member 2",17));
    
    //AttributeDecl obj at operators.ceylon (206:4-206:28)
    var obj$1155=null;
    
    //AttributeDecl i at operators.ceylon (207:4-207:25)
    var i$1156=(opt$1157=obj$1155,$$$cl1.JsCallable(opt$1157,opt$1157!==null?opt$1157.f:null))();
    var opt$1157;
    $$$c2.check((!$$$cl1.exists(i$1156)),$$$cl1.String("nullsafe invoke",15));
    
    //AttributeDecl f2 at operators.ceylon (209:4-209:37)
    var f2$1158=$$$cl1.$JsCallable((opt$1159=obj$1155,$$$cl1.JsCallable(opt$1159,opt$1159!==null?opt$1159.f:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}});
    var opt$1159;
    $$$c2.check((!$$$cl1.exists(nullsafeTest($$$cl1.$JsCallable(f2$1158,[],{Arguments:{t:$$$cl1.Empty},Return:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}})))),$$$cl1.String("nullsafe method ref",19));
    
    //AttributeDecl f3 at operators.ceylon (211:4-211:38)
    var f3$1160=$$$cl1.$JsCallable((opt$1161=obj$1155,$$$cl1.JsCallable(opt$1161,opt$1161!==null?opt$1161.f:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}});
    var opt$1161;
    $$$c2.check($$$cl1.exists(f3$1160),$$$cl1.String("nullsafe method ref 2",21));
    (opt$1162=obj$1155,$$$cl1.JsCallable(opt$1162,opt$1162!==null?opt$1162.f:null))();
    var opt$1162;
    $$$c2.check((!$$$cl1.exists((opt$1163=obj$1155,$$$cl1.JsCallable(opt$1163,opt$1163!==null?opt$1163.f:null))())),$$$cl1.String("nullsafe simple call",20));
    var opt$1163;
    
    //MethodDef getNullsafe at operators.ceylon (215:4-215:46)
    function getNullsafe$1164(){
        return obj$1155;
    };getNullsafe$1164.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:NullsafeTest}]},$ps:[],d:['operators','testNullsafeOperators','$m','getNullsafe']};};
    
    //MethodDecl f4 at operators.ceylon (216:4-216:39)
    var f4$1165=function (){
        return (opt$1166=getNullsafe$1164(),$$$cl1.JsCallable(opt$1166,opt$1166!==null?opt$1166.f:null))();
    };
    f4$1165.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[],d:['operators','testNullsafeOperators','$m','f4']};};
    var opt$1166;
    
    //AttributeDecl result_f4 at operators.ceylon (217:4-217:29)
    var result_f4$1167=f4$1165();
    $$$c2.check((!$$$cl1.exists(result_f4$1167)),$$$cl1.String("nullsafe invoke 2",17));
    
    //AttributeDecl i2 at operators.ceylon (219:4-219:36)
    var i2$1168=(opt$1169=getNullsafe$1164(),$$$cl1.JsCallable(opt$1169,opt$1169!==null?opt$1169.f:null))();
    var opt$1169;
    $$$c2.check((!$$$cl1.exists(i2$1168)),$$$cl1.String("nullsafe invoke 3",17));
    $$$c2.check((!$$$cl1.exists(NullsafeTest().f2($$$cl1.$JsCallable((opt$1170=getNullsafe$1164(),$$$cl1.JsCallable(opt$1170,opt$1170!==null?opt$1170.f:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}})))),$$$cl1.String("nullsafe method ref 3",21));
    var opt$1170;
    
    //AttributeDecl obj2 at operators.ceylon (222:4-222:39)
    var obj2$1171=NullsafeTest();
    var i3$1172;
    if((i3$1172=(opt$1173=obj2$1171,$$$cl1.JsCallable(opt$1173,opt$1173!==null?opt$1173.f:null))())!==null){
        $$$c2.check(i3$1172.equals((1)),$$$cl1.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe invoke 4 (null)",24));
    }
    var opt$1173;
    
    //MethodDecl obj2_f at operators.ceylon (228:4-228:34)
    var obj2_f$1174=function (){
        return (opt$1175=obj2$1171,$$$cl1.JsCallable(opt$1175,opt$1175!==null?opt$1175.f:null))();
    };
    obj2_f$1174.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[],d:['operators','testNullsafeOperators','$m','obj2_f']};};
    var opt$1175;
    var i3$1176;
    if((i3$1176=obj2_f$1174())!==null){
        $$$c2.check(i3$1176.equals((1)),$$$cl1.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe method ref 4 (null)",28));
    }
};testNullsafeOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testNullsafeOperators']};};

//MethodDef testIncDecOperators at operators.ceylon (236:0-311:0)
function testIncDecOperators(){
    
    //AttributeDecl x0 at operators.ceylon (237:4-237:27)
    var x0$1177=(1);
    function setX0$1177(x0$1178){return x0$1177=x0$1178;};
    
    //AttributeGetterDef x at operators.ceylon (238:4-238:27)
    function getX$1179(){
        return x0$1177;
    }
    ;$prop$getX$1179={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['operators','testIncDecOperators','$at','x']};}};
    $prop$getX$1179.get=function(){return x$1179};
    
    //AttributeSetterDef x at operators.ceylon (238:29-238:48)
    var setX$1179=function(x$1180){
        x0$1177=x$1180;
    };setX$1179.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['operators','testIncDecOperators','$at','x']};};
    $prop$getX$1179.set=setX$1179;
    if (setX$1179.$$metamodel$$===undefined)setX$1179.$$metamodel$$=$prop$getX$1179.$$metamodel$$;
    
    //AttributeDecl i1 at operators.ceylon (240:4-240:27)
    var i1$1181=(1);
    function setI1$1181(i1$1182){return i1$1181=i1$1182;};
    
    //MethodDef f1 at operators.ceylon (241:4-248:4)
    function f1$1183(){
        
        //AttributeDecl i2 at operators.ceylon (242:8-242:25)
        var i2$1184=(i1$1181=i1$1181.successor);
        
        //AttributeDecl x2 at operators.ceylon (243:8-243:24)
        var x2$1185=(setX$1179(getX$1179().successor),getX$1179());
        $$$c2.check(i1$1181.equals((2)),$$$cl1.String("prefix increment 1",18));
        $$$c2.check(i2$1184.equals((2)),$$$cl1.String("prefix increment 2",18));
        $$$c2.check(getX$1179().equals((2)),$$$cl1.String("prefix increment 3",18));
        $$$c2.check(x2$1185.equals((2)),$$$cl1.String("prefix increment 4",18));
    };f1$1183.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f1']};};
    f1$1183();
    
    //ClassDef C1 at operators.ceylon (251:4-255:4)
    function C1$1186($$c1$1186){
        $init$C1$1186();
        if ($$c1$1186===undefined)$$c1$1186=new C1$1186.$$;
        
        //AttributeDecl i at operators.ceylon (252:8-252:37)
        $$c1$1186.i$1187_=(1);
        $$c1$1186.$prop$getI={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1186,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','i']};}};
        $$c1$1186.$prop$getI.get=function(){return i};
        
        //AttributeDecl x0 at operators.ceylon (253:8-253:31)
        $$c1$1186.x0$1188_=(1);
        $$c1$1186.$prop$getX0$1188={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1186,$an:function(){return[$$$cl1.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','x0']};}};
        $$c1$1186.$prop$getX0$1188.get=function(){return x0$1188};
        return $$c1$1186;
    }
    C1$1186.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['operators','testIncDecOperators','$c','C1']};};
    function $init$C1$1186(){
        if (C1$1186.$$===undefined){
            $$$cl1.initTypeProto(C1$1186,'operators::testIncDecOperators.C1',$$$cl1.Basic);
            (function($$c1$1186){
                
                //AttributeDecl i at operators.ceylon (252:8-252:37)
                $$$cl1.defineAttr($$c1$1186,'i',function(){return this.i$1187_;},function(i$1189){return this.i$1187_=i$1189;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1186,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','i']};});
                
                //AttributeDecl x0 at operators.ceylon (253:8-253:31)
                $$$cl1.defineAttr($$c1$1186,'x0$1188',function(){return this.x0$1188_;},function(x0$1190){return this.x0$1188_=x0$1190;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1186,$an:function(){return[$$$cl1.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','x0']};});
                
                //AttributeGetterDef x at operators.ceylon (254:8-254:38)
                $$$cl1.defineAttr($$c1$1186,'x',function(){
                    var $$c1$1186=this;
                    return $$c1$1186.x0$1188;
                },function(x$1191){
                    var $$c1$1186=this;
                    $$c1$1186.x0$1188=x$1191;
                },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1186,$an:function(){return[$$$cl1.shared()];},d:['operators','testIncDecOperators','$c','C1','$at','x']};});
            })(C1$1186.$$.prototype);
        }
        return C1$1186;
    }
    $init$C1$1186();
    
    //AttributeDecl c1 at operators.ceylon (256:4-256:16)
    var c1$1192=C1$1186();
    
    //AttributeDecl i3 at operators.ceylon (257:4-257:27)
    var i3$1193=(0);
    function setI3$1193(i3$1194){return i3$1193=i3$1194;};
    
    //MethodDef f2 at operators.ceylon (258:4-261:4)
    function f2$1195(){
        (i3$1193=i3$1193.successor);
        return c1$1192;
    };f2$1195.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:C1$1186},$ps:[],d:['operators','testIncDecOperators','$m','f2']};};
    
    //AttributeDecl i4 at operators.ceylon (262:4-262:25)
    var i4$1196=(tmp$1197=f2$1195(),tmp$1197.i=tmp$1197.i.successor);
    var tmp$1197;
    
    //AttributeDecl x4 at operators.ceylon (263:4-263:25)
    var x4$1198=(tmp$1199=f2$1195(),tmp$1199.x=tmp$1199.x.successor,tmp$1199.x);
    var tmp$1199;
    $$$c2.check(i4$1196.equals((2)),$$$cl1.String("prefix increment 5",18));
    $$$c2.check(c1$1192.i.equals((2)),$$$cl1.String("prefix increment 6",18));
    $$$c2.check(x4$1198.equals((2)),$$$cl1.String("prefix increment 7",18));
    $$$c2.check(c1$1192.x.equals((2)),$$$cl1.String("prefix increment 8",18));
    $$$c2.check(i3$1193.equals((2)),$$$cl1.String("prefix increment 9",18));
    
    //MethodDef f3 at operators.ceylon (270:4-274:4)
    function f3$1200(){
        
        //AttributeDecl i2 at operators.ceylon (271:8-271:25)
        var i2$1201=(i1$1181=i1$1181.predecessor);
        $$$c2.check(i1$1181.equals((1)),$$$cl1.String("prefix decrement",16));
        $$$c2.check(i2$1201.equals((1)),$$$cl1.String("prefix decrement",16));
    };f3$1200.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f3']};};
    f3$1200();
    
    //AttributeDecl i5 at operators.ceylon (277:4-277:25)
    var i5$1202=(tmp$1203=f2$1195(),tmp$1203.i=tmp$1203.i.predecessor);
    var tmp$1203;
    $$$c2.check(i5$1202.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(c1$1192.i.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(i3$1193.equals((3)),$$$cl1.String("prefix decrement",16));
    
    //MethodDef f4 at operators.ceylon (282:4-289:4)
    function f4$1204(){
        
        //AttributeDecl i2 at operators.ceylon (283:8-283:25)
        var i2$1205=(oldi1$1206=i1$1181,i1$1181=oldi1$1206.successor,oldi1$1206);
        var oldi1$1206;
        
        //AttributeDecl x2 at operators.ceylon (284:8-284:24)
        var x2$1207=(oldx$1208=getX$1179(),setX$1179(oldx$1208.successor),oldx$1208);
        var oldx$1208;
        $$$c2.check(i1$1181.equals((2)),$$$cl1.String("postfix increment 1",19));
        $$$c2.check(i2$1205.equals((1)),$$$cl1.String("postfix increment 2",19));
        $$$c2.check(getX$1179().equals((3)),$$$cl1.String("postfix increment 3",19));
        $$$c2.check(x2$1207.equals((2)),$$$cl1.String("postfix increment 4",19));
    };f4$1204.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f4']};};
    f4$1204();
    
    //AttributeDecl i6 at operators.ceylon (292:4-292:25)
    var i6$1209=(tmp$1210=f2$1195(),oldi$1211=tmp$1210.i,tmp$1210.i=oldi$1211.successor,oldi$1211);
    var tmp$1210,oldi$1211;
    
    //AttributeDecl x6 at operators.ceylon (293:4-293:25)
    var x6$1212=(tmp$1213=f2$1195(),oldx$1214=tmp$1213.x,tmp$1213.x=oldx$1214.successor,oldx$1214);
    var tmp$1213,oldx$1214;
    $$$c2.check(i6$1209.equals((1)),$$$cl1.String("postfix increment 5",19));
    $$$c2.check(c1$1192.i.equals((2)),$$$cl1.String("postfix increment 6",19));
    $$$c2.check(x6$1212.equals((2)),$$$cl1.String("postfix increment 7 ",20));
    $$$c2.check(c1$1192.x.equals((3)),$$$cl1.String("postfix increment 8 ",20));
    $$$c2.check(i3$1193.equals((5)),$$$cl1.String("postfix increment 9",19));
    
    //MethodDef f5 at operators.ceylon (300:4-304:4)
    function f5$1215(){
        
        //AttributeDecl i2 at operators.ceylon (301:8-301:25)
        var i2$1216=(oldi1$1217=i1$1181,i1$1181=oldi1$1217.predecessor,oldi1$1217);
        var oldi1$1217;
        $$$c2.check(i1$1181.equals((1)),$$$cl1.String("postfix decrement",17));
        $$$c2.check(i2$1216.equals((2)),$$$cl1.String("postfix decrement",17));
    };f5$1215.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f5']};};
    f5$1215();
    
    //AttributeDecl i7 at operators.ceylon (307:4-307:25)
    var i7$1218=(tmp$1219=f2$1195(),oldi$1220=tmp$1219.i,tmp$1219.i=oldi$1220.predecessor,oldi$1220);
    var tmp$1219,oldi$1220;
    $$$c2.check(i7$1218.equals((2)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(c1$1192.i.equals((1)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(i3$1193.equals((6)),$$$cl1.String("postfix decrement",17));
};testIncDecOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testIncDecOperators']};};

//MethodDef testArithmeticAssignOperators at operators.ceylon (313:0-364:0)
function testArithmeticAssignOperators(){
    
    //AttributeDecl i1 at operators.ceylon (314:4-314:27)
    var i1$1221=(1);
    function setI1$1221(i1$1222){return i1$1221=i1$1222;};
    
    //AttributeDecl x0 at operators.ceylon (315:4-315:27)
    var x0$1223=(1);
    function setX0$1223(x0$1224){return x0$1223=x0$1224;};
    
    //AttributeGetterDef x at operators.ceylon (316:4-316:27)
    function getX$1225(){
        return x0$1223;
    }
    ;$prop$getX$1225={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['operators','testArithmeticAssignOperators','$at','x']};}};
    $prop$getX$1225.get=function(){return x$1225};
    
    //AttributeSetterDef x at operators.ceylon (316:29-316:46)
    var setX$1225=function(x$1226){
        x0$1223=x$1226;
    };setX$1225.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['operators','testArithmeticAssignOperators','$at','x']};};
    $prop$getX$1225.set=setX$1225;
    if (setX$1225.$$metamodel$$===undefined)setX$1225.$$metamodel$$=$prop$getX$1225.$$metamodel$$;
    (i1$1221=i1$1221.plus((10)));
    (setX$1225(getX$1225().plus((10))),getX$1225());
    $$$c2.check(i1$1221.equals((11)),$$$cl1.String("+= operator 1",13));
    $$$c2.check(getX$1225().equals((11)),$$$cl1.String("+= operator 2",13));
    
    //AttributeDecl i2 at operators.ceylon (322:4-322:36)
    var i2$1227=(i1$1221=i1$1221.plus((-(5))));
    function setI2$1227(i2$1228){return i2$1227=i2$1228;};
    
    //AttributeDecl x2 at operators.ceylon (323:4-323:35)
    var x2$1229=(setX$1225(getX$1225().plus((-(5)))),getX$1225());
    function setX2$1229(x2$1230){return x2$1229=x2$1230;};
    $$$c2.check(i2$1227.equals((6)),$$$cl1.String("+= operator 3",13));
    $$$c2.check(i1$1221.equals((6)),$$$cl1.String("+= operator 4",13));
    $$$c2.check(x2$1229.equals((6)),$$$cl1.String("+= operator 5",13));
    $$$c2.check(getX$1225().equals((6)),$$$cl1.String("+= operator 6",13));
    
    //ClassDef C1 at operators.ceylon (329:4-333:4)
    function C1$1231($$c1$1231){
        $init$C1$1231();
        if ($$c1$1231===undefined)$$c1$1231=new C1$1231.$$;
        
        //AttributeDecl i at operators.ceylon (330:8-330:37)
        $$c1$1231.i$1232_=(1);
        $$c1$1231.$prop$getI.get=function(){return i};
        
        //AttributeDecl x0 at operators.ceylon (331:8-331:31)
        $$c1$1231.x0$1233_=(1);
        $$c1$1231.$prop$getX0$1233={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1231,$an:function(){return[$$$cl1.variable()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','x0']};}};
        $$c1$1231.$prop$getX0$1233.get=function(){return x0$1233};
        return $$c1$1231;
    }
    C1$1231.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['operators','testArithmeticAssignOperators','$c','C1']};};
    function $init$C1$1231(){
        if (C1$1231.$$===undefined){
            $$$cl1.initTypeProto(C1$1231,'operators::testArithmeticAssignOperators.C1',$$$cl1.Basic);
            (function($$c1$1231){
                
                //AttributeDecl i at operators.ceylon (330:8-330:37)
                $$$cl1.defineAttr($$c1$1231,'i',function(){return this.i$1232_;},function(i$1234){return this.i$1232_=i$1234;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1231,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','i']};});
                
                //AttributeDecl x0 at operators.ceylon (331:8-331:31)
                $$$cl1.defineAttr($$c1$1231,'x0$1233',function(){return this.x0$1233_;},function(x0$1235){return this.x0$1233_=x0$1235;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1231,$an:function(){return[$$$cl1.variable()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','x0']};});
                
                //AttributeGetterDef x at operators.ceylon (332:8-332:38)
                $$$cl1.defineAttr($$c1$1231,'x',function(){
                    var $$c1$1231=this;
                    return $$c1$1231.x0$1233;
                },function(x$1236){
                    var $$c1$1231=this;
                    $$c1$1231.x0$1233=x$1236;
                },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C1$1231,$an:function(){return[$$$cl1.shared()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','x']};});
            })(C1$1231.$$.prototype);
        }
        return C1$1231;
    }
    $init$C1$1231();
    
    //AttributeDecl c1 at operators.ceylon (334:4-334:16)
    var c1$1237=C1$1231();
    
    //AttributeDecl i3 at operators.ceylon (335:4-335:27)
    var i3$1238=(0);
    function setI3$1238(i3$1239){return i3$1238=i3$1239;};
    
    //MethodDef f at operators.ceylon (336:4-339:4)
    function f$1240(){
        (i3$1238=i3$1238.successor);
        return c1$1237;
    };f$1240.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:C1$1231},$ps:[],d:['operators','testArithmeticAssignOperators','$m','f']};};
    i2$1227=(tmp$1241=f$1240(),tmp$1241.i=tmp$1241.i.plus((11)));
    var tmp$1241;
    x2$1229=(tmp$1242=f$1240(),tmp$1242.x=tmp$1242.x.plus((11)),tmp$1242.x);
    var tmp$1242;
    $$$c2.check(i2$1227.equals((12)),$$$cl1.String("+= operator 7",13));
    $$$c2.check(c1$1237.i.equals((12)),$$$cl1.String("+= operator 8",13));
    $$$c2.check(x2$1229.equals((12)),$$$cl1.String("+= operator 9",13));
    $$$c2.check(c1$1237.x.equals((12)),$$$cl1.String("+= operator 10",14));
    $$$c2.check(i3$1238.equals((2)),$$$cl1.String("+= operator 11",14));
    i2$1227=(i1$1221=i1$1221.minus((14)));
    $$$c2.check(i1$1221.equals((-(8))),$$$cl1.String("-= operator",11));
    $$$c2.check(i2$1227.equals((-(8))),$$$cl1.String("-= operator",11));
    i2$1227=(i1$1221=i1$1221.times((-(3))));
    $$$c2.check(i1$1221.equals((24)),$$$cl1.String("*= operator",11));
    $$$c2.check(i2$1227.equals((24)),$$$cl1.String("*= operator",11));
    i2$1227=(i1$1221=i1$1221.divided((5)));
    $$$c2.check(i1$1221.equals((4)),$$$cl1.String("/= operator",11));
    $$$c2.check(i2$1227.equals((4)),$$$cl1.String("/= operator",11));
    i2$1227=(i1$1221=i1$1221.remainder((3)));
    $$$c2.check(i1$1221.equals((1)),$$$cl1.String("%= operator",11));
    $$$c2.check(i2$1227.equals((1)),$$$cl1.String("%= operator",11));
};testArithmeticAssignOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testArithmeticAssignOperators']};};

//MethodDef testAssignmentOperator at operators.ceylon (366:0-396:0)
function testAssignmentOperator(){
    
    //AttributeDecl i1 at operators.ceylon (367:4-367:27)
    var i1$1243=(1);
    function setI1$1243(i1$1244){return i1$1243=i1$1244;};
    
    //AttributeDecl i2 at operators.ceylon (368:4-368:27)
    var i2$1245=(2);
    function setI2$1245(i2$1246){return i2$1245=i2$1246;};
    
    //AttributeDecl i3 at operators.ceylon (369:4-369:27)
    var i3$1247=(3);
    function setI3$1247(i3$1248){return i3$1247=i3$1248;};
    $$$c2.check((i1$1243=(i2$1245=i3$1247)).equals((3)),$$$cl1.String("assignment 1",12));
    $$$c2.check(i1$1243.equals((3)),$$$cl1.String("assignment 2",12));
    $$$c2.check(i2$1245.equals((3)),$$$cl1.String("assignment 3",12));
    
    //AttributeGetterDef x1 at operators.ceylon (374:4-374:28)
    function getX1$1249(){
        return i1$1243;
    }
    ;$prop$getX1$1249={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['operators','testAssignmentOperator','$at','x1']};}};
    $prop$getX1$1249.get=function(){return x1$1249};
    
    //AttributeSetterDef x1 at operators.ceylon (374:30-374:51)
    var setX1$1249=function(x1$1250){
        i1$1243=x1$1250;
    };setX1$1249.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['operators','testAssignmentOperator','$at','x1']};};
    $prop$getX1$1249.set=setX1$1249;
    if (setX1$1249.$$metamodel$$===undefined)setX1$1249.$$metamodel$$=$prop$getX1$1249.$$metamodel$$;
    
    //AttributeGetterDef x2 at operators.ceylon (375:4-375:28)
    function getX2$1251(){
        return i2$1245;
    }
    ;$prop$getX2$1251={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['operators','testAssignmentOperator','$at','x2']};}};
    $prop$getX2$1251.get=function(){return x2$1251};
    
    //AttributeSetterDef x2 at operators.ceylon (375:30-375:51)
    var setX2$1251=function(x2$1252){
        i2$1245=x2$1252;
    };setX2$1251.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['operators','testAssignmentOperator','$at','x2']};};
    $prop$getX2$1251.set=setX2$1251;
    if (setX2$1251.$$metamodel$$===undefined)setX2$1251.$$metamodel$$=$prop$getX2$1251.$$metamodel$$;
    
    //AttributeGetterDef x3 at operators.ceylon (376:4-376:28)
    function getX3$1253(){
        return i3$1247;
    }
    ;$prop$getX3$1253={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},d:['operators','testAssignmentOperator','$at','x3']};}};
    $prop$getX3$1253.get=function(){return x3$1253};
    
    //AttributeSetterDef x3 at operators.ceylon (376:30-376:51)
    var setX3$1253=function(x3$1254){
        i3$1247=x3$1254;
    };setX3$1253.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},d:['operators','testAssignmentOperator','$at','x3']};};
    $prop$getX3$1253.set=setX3$1253;
    if (setX3$1253.$$metamodel$$===undefined)setX3$1253.$$metamodel$$=$prop$getX3$1253.$$metamodel$$;
    i1$1243=(1);
    i2$1245=(2);
    $$$c2.check((setX1$1249((setX2$1251(getX3$1253()),getX2$1251())),getX1$1249()).equals((3)),$$$cl1.String("assignment 4",12));
    $$$c2.check(getX1$1249().equals((3)),$$$cl1.String("assignment 5",12));
    $$$c2.check(getX2$1251().equals((3)),$$$cl1.String("assignment 6",12));
    
    //ClassDef C at operators.ceylon (383:4-387:4)
    function C$1255($$c$1255){
        $init$C$1255();
        if ($$c$1255===undefined)$$c$1255=new C$1255.$$;
        
        //AttributeDecl i at operators.ceylon (384:8-384:37)
        $$c$1255.i$1256_=(1);
        $$c$1255.$prop$getI.get=function(){return i};
        
        //AttributeDecl x0 at operators.ceylon (385:8-385:31)
        $$c$1255.x0$1257_=(1);
        $$c$1255.$prop$getX0$1257={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C$1255,$an:function(){return[$$$cl1.variable()];},d:['operators','testAssignmentOperator','$c','C','$at','x0']};}};
        $$c$1255.$prop$getX0$1257.get=function(){return x0$1257};
        return $$c$1255;
    }
    C$1255.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl1.Basic},$ps:[],d:['operators','testAssignmentOperator','$c','C']};};
    function $init$C$1255(){
        if (C$1255.$$===undefined){
            $$$cl1.initTypeProto(C$1255,'operators::testAssignmentOperator.C',$$$cl1.Basic);
            (function($$c$1255){
                
                //AttributeDecl i at operators.ceylon (384:8-384:37)
                $$$cl1.defineAttr($$c$1255,'i',function(){return this.i$1256_;},function(i$1258){return this.i$1256_=i$1258;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C$1255,$an:function(){return[$$$cl1.shared(),$$$cl1.variable()];},d:['operators','testAssignmentOperator','$c','C','$at','i']};});
                
                //AttributeDecl x0 at operators.ceylon (385:8-385:31)
                $$$cl1.defineAttr($$c$1255,'x0$1257',function(){return this.x0$1257_;},function(x0$1259){return this.x0$1257_=x0$1259;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C$1255,$an:function(){return[$$$cl1.variable()];},d:['operators','testAssignmentOperator','$c','C','$at','x0']};});
                
                //AttributeGetterDef x at operators.ceylon (386:8-386:38)
                $$$cl1.defineAttr($$c$1255,'x',function(){
                    var $$c$1255=this;
                    return $$c$1255.x0$1257;
                },function(x$1260){
                    var $$c$1255=this;
                    $$c$1255.x0$1257=x$1260;
                },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Integer},$cont:C$1255,$an:function(){return[$$$cl1.shared()];},d:['operators','testAssignmentOperator','$c','C','$at','x']};});
            })(C$1255.$$.prototype);
        }
        return C$1255;
    }
    $init$C$1255();
    
    //AttributeDecl o1 at operators.ceylon (388:4-388:14)
    var o1$1261=C$1255();
    
    //AttributeDecl o2 at operators.ceylon (389:4-389:14)
    var o2$1262=C$1255();
    $$$c2.check((o1$1261.i=(o2$1262.i=(3))).equals((3)),$$$cl1.String("assignment 7",12));
    $$$c2.check(o1$1261.i.equals((3)),$$$cl1.String("assignment 8",12));
    $$$c2.check(o2$1262.i.equals((3)),$$$cl1.String("assignment 9",12));
    $$$c2.check((tmp$1263=o1$1261,tmp$1263.x=(tmp$1264=o2$1262,tmp$1264.x=(3),tmp$1264.x),tmp$1263.x).equals((3)),$$$cl1.String("assignment 10",13));
    var tmp$1263,tmp$1264;
    $$$c2.check(o1$1261.x.equals((3)),$$$cl1.String("assignment 11",13));
    $$$c2.check(o2$1262.x.equals((3)),$$$cl1.String("assignment 12",13));
};testAssignmentOperator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testAssignmentOperator']};};

//MethodDef testSegments at operators.ceylon (398:0-426:0)
function testSegments(){
    
    //AttributeDecl seq at operators.ceylon (399:4-399:97)
    var seq$1265=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.Tuple($$$cl1.String("nine",4),$$$cl1.Tuple($$$cl1.String("ten",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    $$$c2.check(seq$1265.segment((1),(2)).equals($$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[1:2] ",9),seq$1265.segment((1),(2)).string]).string);
    $$$c2.check(seq$1265.segment((3),(5)).equals($$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[3:5] ",9),seq$1265.segment((3),(5)).string]).string);
    $$$c2.check($$$cl1.String("test",4).segment((1),(2)).equals($$$cl1.String("es",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("test[1:2] ",10),$$$cl1.String("test",4).segment((1),(2)).string]).string);
    $$$c2.check($$$cl1.String("hello",5).segment((2),(2)).equals($$$cl1.String("ll",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("hello[2:2] ",11),$$$cl1.String("hello",5).segment((2),(2)).string]).string);
    $$$c2.check((function(){
        //SpreadOp at 404:10-404:24
        var lst$1266=[];
        var it$1267=seq$1265.iterator();
        var elem$1268;
        while ((elem$1268=it$1267.next())!==$$$cl1.getFinished()){
            lst$1266.push(elem$1268.uppercased);
        }
        return $$$cl1.ArraySequence(lst$1266);
    }()).equals($$$cl1.Tuple($$$cl1.String("ONE",3),$$$cl1.Tuple($$$cl1.String("TWO",3),$$$cl1.Tuple($$$cl1.String("THREE",5),$$$cl1.Tuple($$$cl1.String("FOUR",4),$$$cl1.Tuple($$$cl1.String("FIVE",4),$$$cl1.Tuple($$$cl1.String("SIX",3),$$$cl1.Tuple($$$cl1.String("SEVEN",5),$$$cl1.Tuple($$$cl1.String("EIGHT",5),$$$cl1.Tuple($$$cl1.String("NINE",4),$$$cl1.Tuple($$$cl1.String("TEN",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:'T', l:[{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String},{t:$$$cl1.String}]},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.String("spread op",9));
    
    //AttributeDecl s2 at operators.ceylon (405:4-405:18)
    var s2$1269=(function(){var tmpvar$1270=(3);
    if (tmpvar$1270>0){
    var tmpvar$1271=(0);
    var tmpvar$1272=tmpvar$1271;
    for (var i=1; i<tmpvar$1270; i++){tmpvar$1272=tmpvar$1272.successor;}
    return $$$cl1.Range(tmpvar$1271,tmpvar$1272,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}());
    
    //AttributeDecl s3 at operators.ceylon (406:4-406:18)
    var s3$1273=(function(){var tmpvar$1274=(5);
    if (tmpvar$1274>0){
    var tmpvar$1275=(2);
    var tmpvar$1276=tmpvar$1275;
    for (var i=1; i<tmpvar$1274; i++){tmpvar$1276=tmpvar$1276.successor;}
    return $$$cl1.Range(tmpvar$1275,tmpvar$1276,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}());
    $$$c2.check(s2$1269.size.equals((3)),$$$cl1.String("0:3 [1]",7));
    var x$1277;
    if((x$1277=s2$1269.$get((0)))!==null){
        $$$c2.check(x$1277.equals((0)),$$$cl1.String("0:3 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [2]",7));
    }
    var x$1278;
    if((x$1278=s2$1269.$get((2)))!==null){
        $$$c2.check(x$1278.equals((2)),$$$cl1.String("0:3 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [3]",7));
    }
    $$$c2.check(s3$1273.size.equals((5)),$$$cl1.String("2:5 [1]",7));
    var x$1279;
    if((x$1279=s3$1273.$get((0)))!==null){
        $$$c2.check(x$1279.equals((2)),$$$cl1.String("2:5 [1]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [1]",7));
    }
    var x$1280;
    if((x$1280=s3$1273.$get((2)))!==null){
        $$$c2.check(x$1280.equals((4)),$$$cl1.String("2:5 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [2]",7));
    }
    var x$1281;
    if((x$1281=s3$1273.$get((4)))!==null){
        $$$c2.check(x$1281.equals((6)),$$$cl1.String("2:5 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [3]",7));
    }
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1282=(0);
    if (tmpvar$1282>0){
    var tmpvar$1283=(1);
    var tmpvar$1284=tmpvar$1283;
    for (var i=1; i<tmpvar$1282; i++){tmpvar$1284=tmpvar$1284.successor;}
    return $$$cl1.Range(tmpvar$1283,tmpvar$1284,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:0 empty",9));
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1285=(-(1));
    if (tmpvar$1285>0){
    var tmpvar$1286=(1);
    var tmpvar$1287=tmpvar$1286;
    for (var i=1; i<tmpvar$1285; i++){tmpvar$1287=tmpvar$1287.successor;}
    return $$$cl1.Range(tmpvar$1286,tmpvar$1287,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:-1 empty",10));
};testSegments.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','testSegments']};};

//MethodDef compareStringNumber at operators.ceylon (428:0-438:0)
function compareStringNumber(){
    
    //AttributeDecl n1 at operators.ceylon (430:4-430:17)
    var n1$1288=(1);
    
    //AttributeDecl s1 at operators.ceylon (431:4-431:19)
    var s1$1289=$$$cl1.String("1",1);
    
    //AttributeDecl n2 at operators.ceylon (432:4-432:19)
    var n2$1290=$$$cl1.Float(1.0);
    
    //AttributeDecl s2 at operators.ceylon (433:4-433:21)
    var s2$1291=$$$cl1.String("1.0",3);
    $$$c2.check((!n1$1288.equals(s1$1289)),$$$cl1.String("Integer and String should NOT be equal!",39));
    $$$c2.check((!s1$1289.equals(n1$1288)),$$$cl1.String("String and Integer should NOT be equal!",39));
    $$$c2.check((!n2$1290.equals(s2$1291)),$$$cl1.String("Float and String sould NOT be equal",35));
    $$$c2.check((!s2$1291.equals(n2$1290)),$$$cl1.String("String and Float should NOT be equal",36));
};compareStringNumber.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],d:['operators','compareStringNumber']};};

//MethodDef test at operators.ceylon (440:0-455:0)
function test(){
    testIntegerOperators();
    testFloatOperators();
    testBooleanOperators();
    testComparisonOperators();
    testOtherOperators();
    testCollectionOperators();
    testNullsafeOperators();
    testIncDecOperators();
    testArithmeticAssignOperators();
    testAssignmentOperator();
    testSegments();
    testEnumerations();
    compareStringNumber();
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl1.Anything},$ps:[],$an:function(){return[$$$cl1.shared()];},d:['operators','test']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
