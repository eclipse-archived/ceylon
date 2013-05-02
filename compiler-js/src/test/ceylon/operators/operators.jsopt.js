(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}}
var $$$cl2592=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2593=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$3570=$$$cl2592.Tuple((1),$$$cl2592.Tuple((2),$$$cl2592.Tuple((3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$3571=$$$cl2592.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$3572=$$$cl2592.String("hola",4).iterator();
        var c$3573=$$$cl2592.getFinished();
        var next$c$3573=function(){return c$3573=it$3572.next();}
        next$c$3573();
        return function(){
            if(c$3573!==$$$cl2592.getFinished()){
                var c$3573$3574=c$3573;
                var tmpvar$3575=c$3573$3574;
                next$c$3573();
                return tmpvar$3575;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$3576=$$$cl2592.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$3577=$$$cl2592.String("hola",4).iterator();
        var c$3578=$$$cl2592.getFinished();
        var next$c$3578=function(){return c$3578=it$3577.next();}
        next$c$3578();
        return function(){
            if(c$3578!==$$$cl2592.getFinished()){
                var c$3578$3579=c$3578;
                var tmpvar$3580=c$3578$3579;
                next$c$3578();
                return tmpvar$3580;
            }
            return $$$cl2592.getFinished();
        }
    },{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}}).sequence;
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$3581=[(0)].reifyCeylonType({Absent:{t:$$$cl2592.Nothing},Element:{t:$$$cl2592.Integer}}).chain(seq$3570,{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$3582=[$$$cl2592.Character(65)].reifyCeylonType({Absent:{t:$$$cl2592.Nothing},Element:{t:$$$cl2592.Character}}).chain(lcomp$3571,{Absent:{t:$$$cl2592.Null},Element:{t:$$$cl2592.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$3583=$$$cl2592.Tuple((1),$$$cl2592.Tuple((2),$$$cl2592.Tuple((3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$3584=$$$cl2592.Tuple((0),seq$3570,{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}}},First:{t:$$$cl2592.Integer},Element:{t:$$$cl2592.Integer}});
    $$$c2593.check($$$cl2592.className(seq$3570).startsWith($$$cl2592.String("ceylon.language::Tuple",22)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{1,2,3} is not a Tuple but a ",29),$$$cl2592.className(seq$3570).string]).string);
    $$$c2593.check((!$$$cl2592.className(lcomp$3571).startsWith($$$cl2592.String("ceylon.language::Tuple",22))),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("lazy comprehension is a Tuple ",30),$$$cl2592.className(lcomp$3571).string]).string);
    $$$c2593.check($$$cl2592.className(ecomp$3576).startsWith($$$cl2592.String("ceylon.language::ArraySequence",30)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("eager comprehension is not a Tuple but a ",41),$$$cl2592.className(ecomp$3576).string]).string);
    $$$c2593.check((!$$$cl2592.className(s2$3581).startsWith($$$cl2592.String("ceylon.language::Tuple",22))),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{0,*seq} is a Tuple ",20),$$$cl2592.className(s2$3581).string]).string);
    $$$c2593.check((!$$$cl2592.className(s3$3582).startsWith($$$cl2592.String("ceylon.language::Tuple",22))),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{x,*iter} is a Tuple ",21),$$$cl2592.className(s3$3582).string]).string);
    $$$c2593.check($$$cl2592.className(t1$3583).startsWith($$$cl2592.String("ceylon.language::Tuple",22)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("[1,2,3] is not a Tuple but a ",29),$$$cl2592.className(t1$3583).string]).string);
    $$$c2593.check($$$cl2592.className(t2$3584).startsWith($$$cl2592.String("ceylon.language::Tuple",22)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("[0,*seq] is not a Tuple but a ",30),$$$cl2592.className(t2$3584).string]).string);
    $$$c2593.check(seq$3570.equals(t1$3583),$$$cl2592.String("{1,2,3} != [1,2,3]",18));
    $$$c2593.check((!$$$cl2592.className(t2$3584).equals($$$cl2592.className(s2$3581))),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{0,*seq} != [0,*seq] ",21),$$$cl2592.className(t2$3584).string,$$$cl2592.String(" vs",3),$$$cl2592.className(s2$3581).string]).string);
    $$$c2593.check(seq$3570.size.equals((3)),$$$cl2592.String("seq.size!=3",11));
    $$$c2593.check(lcomp$3571.sequence.size.equals((4)),$$$cl2592.String("lcomp.size!=4",13));
    $$$c2593.check(ecomp$3576.size.equals((4)),$$$cl2592.String("ecomp.size!=4",13));
    $$$c2593.check(s2$3581.size.equals((4)),$$$cl2592.String("s2.size!=4",10));
    $$$c2593.check(s3$3582.sequence.size.equals((5)),$$$cl2592.String("s3.size!=5",10));
    $$$c2593.check(t1$3583.size.equals((3)),$$$cl2592.String("t1.size!=3",10));
    $$$c2593.check(t2$3584.size.equals((4)),$$$cl2592.String("t2.size!=4",10));
    $$$c2593.check((!$$$cl2592.className(lcomp$3571).startsWith($$$cl2592.String("ceylon.language::Tuple",22))),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{*comp} is not Tuple but ",25),$$$cl2592.className(lcomp$3571).string]).string);
    $$$c2593.check($$$cl2592.className(ecomp$3576).startsWith($$$cl2592.String("ceylon.language::ArraySequence",30)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{*ecomp} is not Tuple but ",26),$$$cl2592.className(ecomp$3576).string]).string);
    $$$c2593.check($$$cl2592.className(seq$3570).startsWith($$$cl2592.String("ceylon.language::Tuple",22)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("{*seq} is not Tuple but ",24),$$$cl2592.className(seq$3570).string]).string);
};testEnumerations.$$metamodel$$={$nm:'testEnumerations',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testEnumerations.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$3585=(-(4));
    var setI1$3585=function(i1$3586){return i1$3585=i1$3586;};
    i1$3585=(-i1$3585);
    $$$c2593.check(i1$3585.equals((4)),$$$cl2592.String("negation",8));
    i1$3585=(+(-(987654)));
    $$$c2593.check(i1$3585.equals((-(987654))),$$$cl2592.String("positive",8));
    i1$3585=(+(0));
    $$$c2593.check(i1$3585.equals((0)),$$$cl2592.String("+0=0",4));
    i1$3585=(-(0));
    $$$c2593.check(i1$3585.equals((0)),$$$cl2592.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$3587=(123).plus((456));
    var setI2$3587=function(i2$3588){return i2$3587=i2$3588;};
    $$$c2593.check(i2$3587.equals((579)),$$$cl2592.String("addition",8));
    i1$3585=i2$3587.minus((16));
    $$$c2593.check(i1$3585.equals((563)),$$$cl2592.String("subtraction",11));
    i2$3587=(-i1$3585).plus(i2$3587).minus((1));
    $$$c2593.check(i2$3587.equals((15)),$$$cl2592.String("-i1+i2-1",8));
    i1$3585=(3).times((7));
    $$$c2593.check(i1$3585.equals((21)),$$$cl2592.String("multiplication",14));
    i2$3587=i1$3585.times((2));
    $$$c2593.check(i2$3587.equals((42)),$$$cl2592.String("multiplication",14));
    i2$3587=(17).divided((4));
    $$$c2593.check(i2$3587.equals((4)),$$$cl2592.String("integer division",16));
    i1$3585=i2$3587.times((516)).divided((-i1$3585));
    $$$c2593.check(i1$3585.equals((-(98))),$$$cl2592.String("i2*516/-i1",10));
    i1$3585=(15).remainder((4));
    $$$c2593.check(i1$3585.equals((3)),$$$cl2592.String("modulo",6));
    i2$3587=(312).remainder((12));
    $$$c2593.check(i2$3587.equals((0)),$$$cl2592.String("modulo",6));
    i1$3585=(2).power((10));
    $$$c2593.check(i1$3585.equals((1024)),$$$cl2592.String("power",5));
    i2$3587=(10).power((6));
    $$$c2593.check(i2$3587.equals((1000000)),$$$cl2592.String("power",5));
};testIntegerOperators.$$metamodel$$={$nm:'testIntegerOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testIntegerOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$3589=$$$cl2592.Float(4.2).negativeValue;
    var setF1$3589=function(f1$3590){return f1$3589=f1$3590;};
    f1$3589=f1$3589.negativeValue;
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(4.2)),$$$cl2592.String("negation",8));
    f1$3589=(+$$$cl2592.Float(987654.9925567).negativeValue);
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(987654.9925567).negativeValue),$$$cl2592.String("positive",8));
    f1$3589=(+$$$cl2592.Float(0.0));
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(0.0)),$$$cl2592.String("+0.0=0.0",8));
    f1$3589=$$$cl2592.Float(0.0).negativeValue;
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(0.0)),$$$cl2592.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$3591=$$$cl2592.Float(3.14159265).plus($$$cl2592.Float(456.0));
    var setF2$3591=function(f2$3592){return f2$3591=f2$3592;};
    $$$c2593.check(f2$3591.equals($$$cl2592.Float(459.14159265)),$$$cl2592.String("addition",8));
    f1$3589=f2$3591.minus($$$cl2592.Float(0.0016));
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(459.13999265)),$$$cl2592.String("subtraction",11));
    f2$3591=f1$3589.negativeValue.plus(f2$3591).minus($$$cl2592.Float(1.2));
    $$$c2593.check(f2$3591.equals($$$cl2592.Float(1.1984000000000037).negativeValue),$$$cl2592.String("-f1+f2-1.2",10));
    f1$3589=$$$cl2592.Float(3.0).times($$$cl2592.Float(0.79));
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(2.37)),$$$cl2592.String("multiplication",14));
    f2$3591=f1$3589.times($$$cl2592.Float(2.0e13));
    $$$c2593.check(f2$3591.equals($$$cl2592.Float(47400000000000.0)),$$$cl2592.String("multiplication",14));
    f2$3591=$$$cl2592.Float(17.1).divided($$$cl2592.Float(4.0E-18));
    $$$c2593.check(f2$3591.equals($$$cl2592.Float(4275000000000000000.0)),$$$cl2592.String("division",8));
    f1$3589=f2$3591.times($$$cl2592.Float(51.6e2)).divided(f1$3589.negativeValue);
    $$$c2593.check(f2$3591.equals($$$cl2592.Float(4275000000000000000.0)),$$$cl2592.String("f2*51.6e2/-f1",13));
    f1$3589=$$$cl2592.Float(150.0).power($$$cl2592.Float(0.5));
    $$$c2593.check(f1$3589.equals($$$cl2592.Float(12.24744871391589)),$$$cl2592.String("power",5));
};testFloatOperators.$$metamodel$$={$nm:'testFloatOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testFloatOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//ClassDefinition OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl2592.initTypeProto(OpTest1,'operators::OpTest1',$$$cl2592.Basic);
    }
    OpTest1.$$.$$metamodel$$={$nm:'OpTest1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$3593=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$3594=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$3595=(o1$3593===o2$3594);
    var setB1$3595=function(b1$3596){return b1$3595=b1$3596;};
    $$$c2593.check((!b1$3595),$$$cl2592.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$3597=(o1$3593===o1$3593);
    var setB2$3597=function(b2$3598){return b2$3597=b2$3598;};
    $$$c2593.check(b2$3597,$$$cl2592.String("identity",8));
    b1$3595=o1$3593.equals(o2$3594);
    $$$c2593.check((!b1$3595),$$$cl2592.String("equals",6));
    b2$3597=o1$3593.equals(o1$3593);
    $$$c2593.check(b2$3597,$$$cl2592.String("equals",6));
    b1$3595=(1).equals((2));
    $$$c2593.check((!b1$3595),$$$cl2592.String("equals",6));
    b2$3597=(!(1).equals((2)));
    $$$c2593.check(b2$3597,$$$cl2592.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$3599=(!b2$3597);
    var setB3$3599=function(b3$3600){return b3$3599=b3$3600;};
    $$$c2593.check((!b3$3599),$$$cl2592.String("not",3));
    b1$3595=(true&&false);
    $$$c2593.check((!b1$3595),$$$cl2592.String("and",3));
    b2$3597=(b1$3595&&true);
    $$$c2593.check((!b2$3597),$$$cl2592.String("and",3));
    b3$3599=(true&&true);
    $$$c2593.check(b3$3599,$$$cl2592.String("and",3));
    b1$3595=(true||false);
    $$$c2593.check(b1$3595,$$$cl2592.String("or",2));
    b2$3597=(false||b1$3595);
    $$$c2593.check(b2$3597,$$$cl2592.String("or",2));
    b3$3599=(false||false);
    $$$c2593.check((!b3$3599),$$$cl2592.String("or",2));
};testBooleanOperators.$$metamodel$$={$nm:'testBooleanOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testBooleanOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-152:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$3601=$$$cl2592.String("str1",4).compare($$$cl2592.String("str2",4));
    $$$c2593.check(c1$3601.equals($$$cl2592.getSmaller()),$$$cl2592.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$3602=$$$cl2592.String("str2",4).compare($$$cl2592.String("str1",4));
    $$$c2593.check(c2$3602.equals($$$cl2592.getLarger()),$$$cl2592.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$3603=$$$cl2592.String("str1",4).compare($$$cl2592.String("str1",4));
    $$$c2593.check(c3$3603.equals($$$cl2592.getEqual()),$$$cl2592.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$3604=$$$cl2592.String("",0).compare($$$cl2592.String("",0));
    $$$c2593.check(c4$3604.equals($$$cl2592.getEqual()),$$$cl2592.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$3605=$$$cl2592.String("str1",4).compare($$$cl2592.String("",0));
    $$$c2593.check(c5$3605.equals($$$cl2592.getLarger()),$$$cl2592.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$3606=$$$cl2592.String("",0).compare($$$cl2592.String("str2",4));
    $$$c2593.check(c6$3606.equals($$$cl2592.getSmaller()),$$$cl2592.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$3607=$$$cl2592.String("str1",4).compare($$$cl2592.String("str2",4)).equals($$$cl2592.getSmaller());
    var setB1$3607=function(b1$3608){return b1$3607=b1$3608;};
    $$$c2593.check(b1$3607,$$$cl2592.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$3609=$$$cl2592.String("str1",4).compare($$$cl2592.String("str2",4)).equals($$$cl2592.getLarger());
    var setB2$3609=function(b2$3610){return b2$3609=b2$3610;};
    $$$c2593.check((!b2$3609),$$$cl2592.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$3611=($$$cl2592.String("str1",4).compare($$$cl2592.String("str2",4))!==$$$cl2592.getLarger());
    var setB3$3611=function(b3$3612){return b3$3611=b3$3612;};
    $$$c2593.check(b3$3611,$$$cl2592.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$3613=($$$cl2592.String("str1",4).compare($$$cl2592.String("str2",4))!==$$$cl2592.getSmaller());
    var setB4$3613=function(b4$3614){return b4$3613=b4$3614;};
    $$$c2593.check((!b4$3613),$$$cl2592.String("large as",8));
    b1$3607=$$$cl2592.String("str1",4).compare($$$cl2592.String("str1",4)).equals($$$cl2592.getSmaller());
    $$$c2593.check((!b1$3607),$$$cl2592.String("smaller",7));
    b2$3609=$$$cl2592.String("str1",4).compare($$$cl2592.String("str1",4)).equals($$$cl2592.getLarger());
    $$$c2593.check((!b2$3609),$$$cl2592.String("larger",6));
    b3$3611=($$$cl2592.String("str1",4).compare($$$cl2592.String("str1",4))!==$$$cl2592.getLarger());
    $$$c2593.check(b3$3611,$$$cl2592.String("small as",8));
    b4$3613=($$$cl2592.String("str1",4).compare($$$cl2592.String("str1",4))!==$$$cl2592.getSmaller());
    $$$c2593.check(b4$3613,$$$cl2592.String("large as",8));
    
    //AttributeDeclaration a at operators.ceylon (140:4-140:15)
    var a$3615=(0);
    
    //AttributeDeclaration c at operators.ceylon (141:4-141:16)
    var c$3616=(10);
    $$$c2593.check((tmpvar$3617=(5),tmpvar$3617.compare(a$3615)===$$$cl2592.getLarger()&&tmpvar$3617.compare(c$3616)===$$$cl2592.getSmaller()),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<5<",3),c$3616.string]).string);
    $$$c2593.check((tmpvar$3618=(0),tmpvar$3618.compare(a$3615)!==$$$cl2592.getSmaller()&&tmpvar$3618.compare(c$3616)===$$$cl2592.getSmaller()),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<=0<",4),c$3616.string]).string);
    $$$c2593.check((tmpvar$3619=(10),tmpvar$3619.compare(a$3615)===$$$cl2592.getLarger()&&tmpvar$3619.compare(c$3616)!==$$$cl2592.getLarger()),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<10<=",5),c$3616.string]).string);
    $$$c2593.check((tmpvar$3620=(0),tmpvar$3620.compare(a$3615)!==$$$cl2592.getSmaller()&&tmpvar$3620.compare(c$3616)!==$$$cl2592.getLarger()),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<=0<=",5),c$3616.string]).string);
    $$$c2593.check((tmpvar$3621=(10),tmpvar$3621.compare(a$3615)!==$$$cl2592.getSmaller()&&tmpvar$3621.compare(c$3616)!==$$$cl2592.getLarger()),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<=10<=",6),c$3616.string]).string);
    $$$c2593.check((!(tmpvar$3622=(15),tmpvar$3622.compare(a$3615)===$$$cl2592.getLarger()&&tmpvar$3622.compare(c$3616)===$$$cl2592.getSmaller())),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<15<",4),c$3616.string,$$$cl2592.String(" WTF",4)]).string);
    $$$c2593.check((!(tmpvar$3623=(10),tmpvar$3623.compare(a$3615)!==$$$cl2592.getSmaller()&&tmpvar$3623.compare(c$3616)===$$$cl2592.getSmaller())),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<=10<",5),c$3616.string,$$$cl2592.String(" WTF",4)]).string);
    $$$c2593.check((!(tmpvar$3624=(0),tmpvar$3624.compare(a$3615)===$$$cl2592.getLarger()&&tmpvar$3624.compare(c$3616)!==$$$cl2592.getLarger())),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<0<=",4),c$3616.string,$$$cl2592.String(" WTF",4)]).string);
    $$$c2593.check((!(tmpvar$3625=(11),tmpvar$3625.compare(a$3615)!==$$$cl2592.getSmaller()&&tmpvar$3625.compare(c$3616)!==$$$cl2592.getLarger())),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<=11<=",6),c$3616.string,$$$cl2592.String(" WTF",4)]).string);
    $$$c2593.check((!(tmpvar$3626=(-(1)),tmpvar$3626.compare(a$3615)!==$$$cl2592.getSmaller()&&tmpvar$3626.compare(c$3616)!==$$$cl2592.getLarger())),$$$cl2592.StringBuilder().appendAll([a$3615.string,$$$cl2592.String("<=-1<=",6),c$3616.string,$$$cl2592.String(" WTF",4)]).string);
};testComparisonOperators.$$metamodel$$={$nm:'testComparisonOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testComparisonOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testOtherOperators at operators.ceylon (154:0-166:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (155:4-155:42)
    var entry$3627=$$$cl2592.Entry((47),$$$cl2592.String("hi there",8),{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.String}});
    $$$c2593.check(entry$3627.key.equals((47)),$$$cl2592.String("entry key",9));
    $$$c2593.check(entry$3627.item.equals($$$cl2592.String("hi there",8)),$$$cl2592.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (158:4-158:30)
    var entry2$3628=$$$cl2592.Entry(true,entry$3627,{Key:{t:$$$cl2592.true$3430},Item:{t:$$$cl2592.Entry,a:{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.String}}}});
    $$$c2593.check(entry2$3628.key.equals(true),$$$cl2592.String("entry key",9));
    $$$c2593.check(entry2$3628.item.equals($$$cl2592.Entry((47),$$$cl2592.String("hi there",8),{Key:{t:$$$cl2592.Integer},Item:{t:$$$cl2592.String}})),$$$cl2592.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (162:4-162:41)
    var s1$3629=(opt$3630=(true?$$$cl2592.String("ok",2):null),opt$3630!==null?opt$3630:$$$cl2592.String("noo",3));
    var opt$3630;
    $$$c2593.check(s1$3629.equals($$$cl2592.String("ok",2)),$$$cl2592.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (164:4-164:47)
    var s2$3631=(opt$3632=(false?$$$cl2592.String("what?",5):null),opt$3632!==null?opt$3632:$$$cl2592.String("great",5));
    var opt$3632;
    $$$c2593.check(s2$3631.equals($$$cl2592.String("great",5)),$$$cl2592.String("then/else 2",11));
};testOtherOperators.$$metamodel$$={$nm:'testOtherOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testOtherOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testCollectionOperators at operators.ceylon (168:0-180:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (169:4-169:33)
    var seq1$3633=$$$cl2592.Tuple($$$cl2592.String("one",3),$$$cl2592.Tuple($$$cl2592.String("two",3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (170:4-170:23)
    var s1$3634=seq1$3633.get((0));
    $$$c2593.check(s1$3634.equals($$$cl2592.String("one",3)),$$$cl2592.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (172:4-172:28)
    var s2$3635=seq1$3633.get((2));
    $$$c2593.check((!$$$cl2592.exists(s2$3635)),$$$cl2592.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (174:4-174:29)
    var s3$3636=seq1$3633.get((-(1)));
    $$$c2593.check((!$$$cl2592.exists(s3$3636)),$$$cl2592.String("lookup",6));
};testCollectionOperators.$$metamodel$$={$nm:'testCollectionOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testCollectionOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//ClassDefinition NullsafeTest at operators.ceylon (182:0-187:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl2592.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl2592.Basic);
        (function($$nullsafeTest){
            
            //MethodDefinition f at operators.ceylon (183:4-183:33)
            $$nullsafeTest.f=function f(){
                var $$nullsafeTest=this;
                return (1);
            };$$nullsafeTest.f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl2592.Integer},$ps:[]};
            //MethodDefinition f2 at operators.ceylon (184:4-186:4)
            $$nullsafeTest.f2=function f2(x$3637){
                var $$nullsafeTest=this;
                return x$3637();
            };$$nullsafeTest.f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]},$ps:[{$nm:'x',$mt:'prm',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}}]};
        })(NullsafeTest.$$.prototype);
    }
    NullsafeTest.$$.$$metamodel$$={$nm:'NullsafeTest',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (189:0-191:0)
function nullsafeTest(f$3638){
    return f$3638();
};nullsafeTest.$$metamodel$$={$nm:'nullsafeTest',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]},$ps:[{$nm:'f',$mt:'prm',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}}]};//nullsafeTest.$$targs$$={Arguments:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]},Element:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}}},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}};

//MethodDefinition testNullsafeOperators at operators.ceylon (193:0-234:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (194:4-194:27)
    var seq$3639=$$$cl2592.Tuple($$$cl2592.String("hi",2),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (195:4-195:34)
    var s1$3640=(opt$3641=seq$3639.get((0)),opt$3641!==null?opt$3641:$$$cl2592.String("null",4));
    var opt$3641;
    $$$c2593.check(s1$3640.equals($$$cl2592.String("hi",2)),$$$cl2592.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (197:4-197:34)
    var s2$3642=(opt$3643=seq$3639.get((1)),opt$3643!==null?opt$3643:$$$cl2592.String("null",4));
    var opt$3643;
    $$$c2593.check(s2$3642.equals($$$cl2592.String("null",4)),$$$cl2592.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (200:4-200:21)
    var s3$3644=null;
    
    //AttributeDeclaration s4 at operators.ceylon (201:4-201:23)
    var s4$3645=$$$cl2592.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (202:4-202:42)
    var s5$3646=(opt$3647=(opt$3648=s3$3644,opt$3648!==null?opt$3648.uppercased:null),opt$3647!==null?opt$3647:$$$cl2592.String("null",4));
    var opt$3647,opt$3648;
    
    //AttributeDeclaration s6 at operators.ceylon (203:4-203:42)
    var s6$3649=(opt$3650=(opt$3651=s4$3645,opt$3651!==null?opt$3651.uppercased:null),opt$3650!==null?opt$3650:$$$cl2592.String("null",4));
    var opt$3650,opt$3651;
    $$$c2593.check(s5$3646.equals($$$cl2592.String("null",4)),$$$cl2592.String("nullsafe member 1",17));
    $$$c2593.check(s6$3649.equals($$$cl2592.String("TEST",4)),$$$cl2592.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (206:4-206:28)
    var obj$3652=null;
    
    //AttributeDeclaration i at operators.ceylon (207:4-207:25)
    var i$3653=(opt$3654=obj$3652,$$$cl2592.JsCallable(opt$3654,opt$3654!==null?opt$3654.f:null))();
    var opt$3654;
    $$$c2593.check((!$$$cl2592.exists(i$3653)),$$$cl2592.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (209:4-209:37)
    var f2$3655=$$$cl2592.$JsCallable((opt$3656=obj$3652,$$$cl2592.JsCallable(opt$3656,opt$3656!==null?opt$3656.f:null)),[],{Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}});
    var opt$3656;
    $$$c2593.check((!$$$cl2592.exists(nullsafeTest($$$cl2592.$JsCallable(f2$3655,[],{Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}})))),$$$cl2592.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (211:4-211:38)
    var f3$3657=$$$cl2592.$JsCallable((opt$3658=obj$3652,$$$cl2592.JsCallable(opt$3658,opt$3658!==null?opt$3658.f:null)),[/*WARNING: got ceylon.language::Null|ceylon.language::Callable<ceylon.language::Null|ceylon.language::Integer,ceylon.language::Empty> instead of Callable*/],{Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}});
    var opt$3658;
    $$$c2593.check($$$cl2592.exists(f3$3657),$$$cl2592.String("nullsafe method ref 2",21));
    (opt$3659=obj$3652,$$$cl2592.JsCallable(opt$3659,opt$3659!==null?opt$3659.f:null))();
    var opt$3659;
    $$$c2593.check((!$$$cl2592.exists((opt$3660=obj$3652,$$$cl2592.JsCallable(opt$3660,opt$3660!==null?opt$3660.f:null))())),$$$cl2592.String("nullsafe simple call",20));
    var opt$3660;
    
    //MethodDefinition getNullsafe at operators.ceylon (215:4-215:46)
    function getNullsafe$3661(){
        return obj$3652;
    };getNullsafe$3661.$$metamodel$$={$nm:'getNullsafe',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:NullsafeTest}]},$ps:[]};//getNullsafe$3661.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:NullsafeTest}]}};
    
    //MethodDeclaration f4 at operators.ceylon (216:4-216:39)
    var f4$3662=function (){
        return (opt$3663=getNullsafe$3661(),$$$cl2592.JsCallable(opt$3663,opt$3663!==null?opt$3663.f:null))();
    };
    f4$3662.$$metamodel$$={$nm:'f4',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]},$ps:[]};
    var opt$3663;
    
    //AttributeDeclaration result_f4 at operators.ceylon (217:4-217:29)
    var result_f4$3664=f4$3662();
    $$$c2593.check((!$$$cl2592.exists(result_f4$3664)),$$$cl2592.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (219:4-219:36)
    var i2$3665=(opt$3666=getNullsafe$3661(),$$$cl2592.JsCallable(opt$3666,opt$3666!==null?opt$3666.f:null))();
    var opt$3666;
    $$$c2593.check((!$$$cl2592.exists(i2$3665)),$$$cl2592.String("nullsafe invoke 3",17));
    $$$c2593.check((!$$$cl2592.exists(NullsafeTest().f2($$$cl2592.$JsCallable((opt$3667=getNullsafe$3661(),$$$cl2592.JsCallable(opt$3667,opt$3667!==null?opt$3667.f:null)),[],{Arguments:{t:$$$cl2592.Empty},Return:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]}})))),$$$cl2592.String("nullsafe method ref 3",21));
    var opt$3667;
    
    //AttributeDeclaration obj2 at operators.ceylon (222:4-222:39)
    var obj2$3668=NullsafeTest();
    var i3$3669;
    if((i3$3669=(opt$3670=obj2$3668,$$$cl2592.JsCallable(opt$3670,opt$3670!==null?opt$3670.f:null))())!==null){
        $$$c2593.check(i3$3669.equals((1)),$$$cl2592.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2593.fail($$$cl2592.String("nullsafe invoke 4 (null)",24));
    }
    var opt$3670;
    
    //MethodDeclaration obj2_f at operators.ceylon (228:4-228:34)
    var obj2_f$3671=function (){
        return (opt$3672=obj2$3668,$$$cl2592.JsCallable(opt$3672,opt$3672!==null?opt$3672.f:null))();
    };
    obj2_f$3671.$$metamodel$$={$nm:'obj2_f',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl2592.Null},{t:$$$cl2592.Integer}]},$ps:[]};
    var opt$3672;
    var i3$3673;
    if((i3$3673=obj2_f$3671())!==null){
        $$$c2593.check(i3$3673.equals((1)),$$$cl2592.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2593.fail($$$cl2592.String("nullsafe method ref 4 (null)",28));
    }
};testNullsafeOperators.$$metamodel$$={$nm:'testNullsafeOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testNullsafeOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testIncDecOperators at operators.ceylon (236:0-311:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (237:4-237:27)
    var x0$3674=(1);
    var setX0$3674=function(x0$3675){return x0$3674=x0$3675;};
    
    //AttributeGetterDefinition x at operators.ceylon (238:4-238:27)
    var getX$3676=function(){
        return x0$3674;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (238:29-238:48)
    var setX$3676=function(x$3677){
        x0$3674=x$3677;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (240:4-240:27)
    var i1$3678=(1);
    var setI1$3678=function(i1$3679){return i1$3678=i1$3679;};
    
    //MethodDefinition f1 at operators.ceylon (241:4-248:4)
    function f1$3680(){
        
        //AttributeDeclaration i2 at operators.ceylon (242:8-242:25)
        var i2$3681=(i1$3678=i1$3678.successor);
        
        //AttributeDeclaration x2 at operators.ceylon (243:8-243:24)
        var x2$3682=(setX$3676(getX$3676().successor),getX$3676());
        $$$c2593.check(i1$3678.equals((2)),$$$cl2592.String("prefix increment 1",18));
        $$$c2593.check(i2$3681.equals((2)),$$$cl2592.String("prefix increment 2",18));
        $$$c2593.check(getX$3676().equals((2)),$$$cl2592.String("prefix increment 3",18));
        $$$c2593.check(x2$3682.equals((2)),$$$cl2592.String("prefix increment 4",18));
    };f1$3680.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//f1$3680.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
    f1$3680();
    
    //ClassDefinition C1 at operators.ceylon (251:4-255:4)
    function C1$3683($$c1$3683){
        $init$C1$3683();
        if ($$c1$3683===undefined)$$c1$3683=new C1$3683.$$;
        
        //AttributeDeclaration i at operators.ceylon (252:8-252:37)
        $$c1$3683.i$3684_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (253:8-253:31)
        $$c1$3683.x0$3685_=(1);
        return $$c1$3683;
    }
    function $init$C1$3683(){
        if (C1$3683.$$===undefined){
            $$$cl2592.initTypeProto(C1$3683,'operators::testIncDecOperators.C1',$$$cl2592.Basic);
            (function($$c1$3683){
                
                //AttributeDeclaration i at operators.ceylon (252:8-252:37)
                $$$cl2592.defineAttr($$c1$3683,'i',function(){return this.i$3684_;},function(i$3686){return this.i$3684_=i$3686;});
                
                //AttributeDeclaration x0 at operators.ceylon (253:8-253:31)
                $$$cl2592.defineAttr($$c1$3683,'x0$3685',function(){return this.x0$3685_;},function(x0$3687){return this.x0$3685_=x0$3687;});
                
                //AttributeGetterDefinition x at operators.ceylon (254:8-254:38)
                $$$cl2592.defineAttr($$c1$3683,'x',function(){
                    var $$c1$3683=this;
                    return $$c1$3683.x0$3685;
                },function(x$3688){
                    var $$c1$3683=this;
                    $$c1$3683.x0$3685=x$3688;
                });
            })(C1$3683.$$.prototype);
        }
        C1$3683.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return C1$3683;
    }
    $init$C1$3683();
    
    //AttributeDeclaration c1 at operators.ceylon (256:4-256:16)
    var c1$3689=C1$3683();
    
    //AttributeDeclaration i3 at operators.ceylon (257:4-257:27)
    var i3$3690=(0);
    var setI3$3690=function(i3$3691){return i3$3690=i3$3691;};
    
    //MethodDefinition f2 at operators.ceylon (258:4-261:4)
    function f2$3692(){
        (i3$3690=i3$3690.successor);
        return c1$3689;
    };f2$3692.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:C1$3683},$ps:[]};//f2$3692.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:C1$3683}};
    
    //AttributeDeclaration i4 at operators.ceylon (262:4-262:25)
    var i4$3693=(tmp$3694=f2$3692(),tmp$3694.i=tmp$3694.i.successor);
    var tmp$3694;
    
    //AttributeDeclaration x4 at operators.ceylon (263:4-263:25)
    var x4$3695=(tmp$3696=f2$3692(),tmp$3696.x=tmp$3696.x.successor,tmp$3696.x);
    var tmp$3696;
    $$$c2593.check(i4$3693.equals((2)),$$$cl2592.String("prefix increment 5",18));
    $$$c2593.check(c1$3689.i.equals((2)),$$$cl2592.String("prefix increment 6",18));
    $$$c2593.check(x4$3695.equals((2)),$$$cl2592.String("prefix increment 7",18));
    $$$c2593.check(c1$3689.x.equals((2)),$$$cl2592.String("prefix increment 8",18));
    $$$c2593.check(i3$3690.equals((2)),$$$cl2592.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (270:4-274:4)
    function f3$3697(){
        
        //AttributeDeclaration i2 at operators.ceylon (271:8-271:25)
        var i2$3698=(i1$3678=i1$3678.predecessor);
        $$$c2593.check(i1$3678.equals((1)),$$$cl2592.String("prefix decrement",16));
        $$$c2593.check(i2$3698.equals((1)),$$$cl2592.String("prefix decrement",16));
    };f3$3697.$$metamodel$$={$nm:'f3',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//f3$3697.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
    f3$3697();
    
    //AttributeDeclaration i5 at operators.ceylon (277:4-277:25)
    var i5$3699=(tmp$3700=f2$3692(),tmp$3700.i=tmp$3700.i.predecessor);
    var tmp$3700;
    $$$c2593.check(i5$3699.equals((1)),$$$cl2592.String("prefix decrement",16));
    $$$c2593.check(c1$3689.i.equals((1)),$$$cl2592.String("prefix decrement",16));
    $$$c2593.check(i3$3690.equals((3)),$$$cl2592.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (282:4-289:4)
    function f4$3701(){
        
        //AttributeDeclaration i2 at operators.ceylon (283:8-283:25)
        var i2$3702=(oldi1$3703=i1$3678,i1$3678=oldi1$3703.successor,oldi1$3703);
        var oldi1$3703;
        
        //AttributeDeclaration x2 at operators.ceylon (284:8-284:24)
        var x2$3704=(oldx$3705=getX$3676(),setX$3676(oldx$3705.successor),oldx$3705);
        var oldx$3705;
        $$$c2593.check(i1$3678.equals((2)),$$$cl2592.String("postfix increment 1",19));
        $$$c2593.check(i2$3702.equals((1)),$$$cl2592.String("postfix increment 2",19));
        $$$c2593.check(getX$3676().equals((3)),$$$cl2592.String("postfix increment 3",19));
        $$$c2593.check(x2$3704.equals((2)),$$$cl2592.String("postfix increment 4",19));
    };f4$3701.$$metamodel$$={$nm:'f4',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//f4$3701.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
    f4$3701();
    
    //AttributeDeclaration i6 at operators.ceylon (292:4-292:25)
    var i6$3706=(tmp$3707=f2$3692(),oldi$3708=tmp$3707.i,tmp$3707.i=oldi$3708.successor,oldi$3708);
    var tmp$3707,oldi$3708;
    
    //AttributeDeclaration x6 at operators.ceylon (293:4-293:25)
    var x6$3709=(tmp$3710=f2$3692(),oldx$3711=tmp$3710.x,tmp$3710.x=oldx$3711.successor,oldx$3711);
    var tmp$3710,oldx$3711;
    $$$c2593.check(i6$3706.equals((1)),$$$cl2592.String("postfix increment 5",19));
    $$$c2593.check(c1$3689.i.equals((2)),$$$cl2592.String("postfix increment 6",19));
    $$$c2593.check(x6$3709.equals((2)),$$$cl2592.String("postfix increment 7 ",20));
    $$$c2593.check(c1$3689.x.equals((3)),$$$cl2592.String("postfix increment 8 ",20));
    $$$c2593.check(i3$3690.equals((5)),$$$cl2592.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (300:4-304:4)
    function f5$3712(){
        
        //AttributeDeclaration i2 at operators.ceylon (301:8-301:25)
        var i2$3713=(oldi1$3714=i1$3678,i1$3678=oldi1$3714.predecessor,oldi1$3714);
        var oldi1$3714;
        $$$c2593.check(i1$3678.equals((1)),$$$cl2592.String("postfix decrement",17));
        $$$c2593.check(i2$3713.equals((2)),$$$cl2592.String("postfix decrement",17));
    };f5$3712.$$metamodel$$={$nm:'f5',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//f5$3712.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
    f5$3712();
    
    //AttributeDeclaration i7 at operators.ceylon (307:4-307:25)
    var i7$3715=(tmp$3716=f2$3692(),oldi$3717=tmp$3716.i,tmp$3716.i=oldi$3717.predecessor,oldi$3717);
    var tmp$3716,oldi$3717;
    $$$c2593.check(i7$3715.equals((2)),$$$cl2592.String("postfix decrement",17));
    $$$c2593.check(c1$3689.i.equals((1)),$$$cl2592.String("postfix decrement",17));
    $$$c2593.check(i3$3690.equals((6)),$$$cl2592.String("postfix decrement",17));
};testIncDecOperators.$$metamodel$$={$nm:'testIncDecOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testIncDecOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (313:0-364:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (314:4-314:27)
    var i1$3718=(1);
    var setI1$3718=function(i1$3719){return i1$3718=i1$3719;};
    
    //AttributeDeclaration x0 at operators.ceylon (315:4-315:27)
    var x0$3720=(1);
    var setX0$3720=function(x0$3721){return x0$3720=x0$3721;};
    
    //AttributeGetterDefinition x at operators.ceylon (316:4-316:27)
    var getX$3722=function(){
        return x0$3720;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (316:29-316:46)
    var setX$3722=function(x$3723){
        x0$3720=x$3723;
    };
    (i1$3718=i1$3718.plus((10)));
    (setX$3722(getX$3722().plus((10))),getX$3722());
    $$$c2593.check(i1$3718.equals((11)),$$$cl2592.String("+= operator 1",13));
    $$$c2593.check(getX$3722().equals((11)),$$$cl2592.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (322:4-322:36)
    var i2$3724=(i1$3718=i1$3718.plus((-(5))));
    var setI2$3724=function(i2$3725){return i2$3724=i2$3725;};
    
    //AttributeDeclaration x2 at operators.ceylon (323:4-323:35)
    var x2$3726=(setX$3722(getX$3722().plus((-(5)))),getX$3722());
    var setX2$3726=function(x2$3727){return x2$3726=x2$3727;};
    $$$c2593.check(i2$3724.equals((6)),$$$cl2592.String("+= operator 3",13));
    $$$c2593.check(i1$3718.equals((6)),$$$cl2592.String("+= operator 4",13));
    $$$c2593.check(x2$3726.equals((6)),$$$cl2592.String("+= operator 5",13));
    $$$c2593.check(getX$3722().equals((6)),$$$cl2592.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (329:4-333:4)
    function C1$3728($$c1$3728){
        $init$C1$3728();
        if ($$c1$3728===undefined)$$c1$3728=new C1$3728.$$;
        
        //AttributeDeclaration i at operators.ceylon (330:8-330:37)
        $$c1$3728.i$3729_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (331:8-331:31)
        $$c1$3728.x0$3730_=(1);
        return $$c1$3728;
    }
    function $init$C1$3728(){
        if (C1$3728.$$===undefined){
            $$$cl2592.initTypeProto(C1$3728,'operators::testArithmeticAssignOperators.C1',$$$cl2592.Basic);
            (function($$c1$3728){
                
                //AttributeDeclaration i at operators.ceylon (330:8-330:37)
                $$$cl2592.defineAttr($$c1$3728,'i',function(){return this.i$3729_;},function(i$3731){return this.i$3729_=i$3731;});
                
                //AttributeDeclaration x0 at operators.ceylon (331:8-331:31)
                $$$cl2592.defineAttr($$c1$3728,'x0$3730',function(){return this.x0$3730_;},function(x0$3732){return this.x0$3730_=x0$3732;});
                
                //AttributeGetterDefinition x at operators.ceylon (332:8-332:38)
                $$$cl2592.defineAttr($$c1$3728,'x',function(){
                    var $$c1$3728=this;
                    return $$c1$3728.x0$3730;
                },function(x$3733){
                    var $$c1$3728=this;
                    $$c1$3728.x0$3730=x$3733;
                });
            })(C1$3728.$$.prototype);
        }
        C1$3728.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return C1$3728;
    }
    $init$C1$3728();
    
    //AttributeDeclaration c1 at operators.ceylon (334:4-334:16)
    var c1$3734=C1$3728();
    
    //AttributeDeclaration i3 at operators.ceylon (335:4-335:27)
    var i3$3735=(0);
    var setI3$3735=function(i3$3736){return i3$3735=i3$3736;};
    
    //MethodDefinition f at operators.ceylon (336:4-339:4)
    function f$3737(){
        (i3$3735=i3$3735.successor);
        return c1$3734;
    };f$3737.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:C1$3728},$ps:[]};//f$3737.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:C1$3728}};
    i2$3724=(tmp$3738=f$3737(),tmp$3738.i=tmp$3738.i.plus((11)));
    var tmp$3738;
    x2$3726=(tmp$3739=f$3737(),tmp$3739.x=tmp$3739.x.plus((11)),tmp$3739.x);
    var tmp$3739;
    $$$c2593.check(i2$3724.equals((12)),$$$cl2592.String("+= operator 7",13));
    $$$c2593.check(c1$3734.i.equals((12)),$$$cl2592.String("+= operator 8",13));
    $$$c2593.check(x2$3726.equals((12)),$$$cl2592.String("+= operator 9",13));
    $$$c2593.check(c1$3734.x.equals((12)),$$$cl2592.String("+= operator 10",14));
    $$$c2593.check(i3$3735.equals((2)),$$$cl2592.String("+= operator 11",14));
    i2$3724=(i1$3718=i1$3718.minus((14)));
    $$$c2593.check(i1$3718.equals((-(8))),$$$cl2592.String("-= operator",11));
    $$$c2593.check(i2$3724.equals((-(8))),$$$cl2592.String("-= operator",11));
    i2$3724=(i1$3718=i1$3718.times((-(3))));
    $$$c2593.check(i1$3718.equals((24)),$$$cl2592.String("*= operator",11));
    $$$c2593.check(i2$3724.equals((24)),$$$cl2592.String("*= operator",11));
    i2$3724=(i1$3718=i1$3718.divided((5)));
    $$$c2593.check(i1$3718.equals((4)),$$$cl2592.String("/= operator",11));
    $$$c2593.check(i2$3724.equals((4)),$$$cl2592.String("/= operator",11));
    i2$3724=(i1$3718=i1$3718.remainder((3)));
    $$$c2593.check(i1$3718.equals((1)),$$$cl2592.String("%= operator",11));
    $$$c2593.check(i2$3724.equals((1)),$$$cl2592.String("%= operator",11));
};testArithmeticAssignOperators.$$metamodel$$={$nm:'testArithmeticAssignOperators',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testArithmeticAssignOperators.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testAssignmentOperator at operators.ceylon (366:0-396:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (367:4-367:27)
    var i1$3740=(1);
    var setI1$3740=function(i1$3741){return i1$3740=i1$3741;};
    
    //AttributeDeclaration i2 at operators.ceylon (368:4-368:27)
    var i2$3742=(2);
    var setI2$3742=function(i2$3743){return i2$3742=i2$3743;};
    
    //AttributeDeclaration i3 at operators.ceylon (369:4-369:27)
    var i3$3744=(3);
    var setI3$3744=function(i3$3745){return i3$3744=i3$3745;};
    $$$c2593.check((i1$3740=(i2$3742=i3$3744)).equals((3)),$$$cl2592.String("assignment 1",12));
    $$$c2593.check(i1$3740.equals((3)),$$$cl2592.String("assignment 2",12));
    $$$c2593.check(i2$3742.equals((3)),$$$cl2592.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (374:4-374:28)
    var getX1$3746=function(){
        return i1$3740;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (374:30-374:51)
    var setX1$3746=function(x1$3747){
        i1$3740=x1$3747;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (375:4-375:28)
    var getX2$3748=function(){
        return i2$3742;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (375:30-375:51)
    var setX2$3748=function(x2$3749){
        i2$3742=x2$3749;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (376:4-376:28)
    var getX3$3750=function(){
        return i3$3744;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (376:30-376:51)
    var setX3$3750=function(x3$3751){
        i3$3744=x3$3751;
    };
    i1$3740=(1);
    i2$3742=(2);
    $$$c2593.check((setX1$3746((setX2$3748(getX3$3750()),getX2$3748())),getX1$3746()).equals((3)),$$$cl2592.String("assignment 4",12));
    $$$c2593.check(getX1$3746().equals((3)),$$$cl2592.String("assignment 5",12));
    $$$c2593.check(getX2$3748().equals((3)),$$$cl2592.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (383:4-387:4)
    function C$3752($$c$3752){
        $init$C$3752();
        if ($$c$3752===undefined)$$c$3752=new C$3752.$$;
        
        //AttributeDeclaration i at operators.ceylon (384:8-384:37)
        $$c$3752.i$3753_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (385:8-385:31)
        $$c$3752.x0$3754_=(1);
        return $$c$3752;
    }
    function $init$C$3752(){
        if (C$3752.$$===undefined){
            $$$cl2592.initTypeProto(C$3752,'operators::testAssignmentOperator.C',$$$cl2592.Basic);
            (function($$c$3752){
                
                //AttributeDeclaration i at operators.ceylon (384:8-384:37)
                $$$cl2592.defineAttr($$c$3752,'i',function(){return this.i$3753_;},function(i$3755){return this.i$3753_=i$3755;});
                
                //AttributeDeclaration x0 at operators.ceylon (385:8-385:31)
                $$$cl2592.defineAttr($$c$3752,'x0$3754',function(){return this.x0$3754_;},function(x0$3756){return this.x0$3754_=x0$3756;});
                
                //AttributeGetterDefinition x at operators.ceylon (386:8-386:38)
                $$$cl2592.defineAttr($$c$3752,'x',function(){
                    var $$c$3752=this;
                    return $$c$3752.x0$3754;
                },function(x$3757){
                    var $$c$3752=this;
                    $$c$3752.x0$3754=x$3757;
                });
            })(C$3752.$$.prototype);
        }
        C$3752.$$.$$metamodel$$={$nm:'C',$mt:'cls','super':{t:$$$cl2592.Basic},'satisfies':[]};
        return C$3752;
    }
    $init$C$3752();
    
    //AttributeDeclaration o1 at operators.ceylon (388:4-388:14)
    var o1$3758=C$3752();
    
    //AttributeDeclaration o2 at operators.ceylon (389:4-389:14)
    var o2$3759=C$3752();
    $$$c2593.check((o1$3758.i=(o2$3759.i=(3))).equals((3)),$$$cl2592.String("assignment 7",12));
    $$$c2593.check(o1$3758.i.equals((3)),$$$cl2592.String("assignment 8",12));
    $$$c2593.check(o2$3759.i.equals((3)),$$$cl2592.String("assignment 9",12));
    $$$c2593.check((tmp$3760=o1$3758,tmp$3760.x=(tmp$3761=o2$3759,tmp$3761.x=(3),tmp$3761.x),tmp$3760.x).equals((3)),$$$cl2592.String("assignment 10",13));
    var tmp$3760,tmp$3761;
    $$$c2593.check(o1$3758.x.equals((3)),$$$cl2592.String("assignment 11",13));
    $$$c2593.check(o2$3759.x.equals((3)),$$$cl2592.String("assignment 12",13));
};testAssignmentOperator.$$metamodel$$={$nm:'testAssignmentOperator',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testAssignmentOperator.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition testSegments at operators.ceylon (398:0-426:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (399:4-399:97)
    var seq$3762=$$$cl2592.Tuple($$$cl2592.String("one",3),$$$cl2592.Tuple($$$cl2592.String("two",3),$$$cl2592.Tuple($$$cl2592.String("three",5),$$$cl2592.Tuple($$$cl2592.String("four",4),$$$cl2592.Tuple($$$cl2592.String("five",4),$$$cl2592.Tuple($$$cl2592.String("six",3),$$$cl2592.Tuple($$$cl2592.String("seven",5),$$$cl2592.Tuple($$$cl2592.String("eight",5),$$$cl2592.Tuple($$$cl2592.String("nine",4),$$$cl2592.Tuple($$$cl2592.String("ten",3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}});
    $$$c2593.check(seq$3762.segment((1),(2)).equals($$$cl2592.Tuple($$$cl2592.String("two",3),$$$cl2592.Tuple($$$cl2592.String("three",5),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}})),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("seq[1:2] ",9),seq$3762.segment((1),(2)).string]).string);
    $$$c2593.check(seq$3762.segment((3),(5)).equals($$$cl2592.Tuple($$$cl2592.String("four",4),$$$cl2592.Tuple($$$cl2592.String("five",4),$$$cl2592.Tuple($$$cl2592.String("six",3),$$$cl2592.Tuple($$$cl2592.String("seven",5),$$$cl2592.Tuple($$$cl2592.String("eight",5),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}})),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("seq[3:5] ",9),seq$3762.segment((3),(5)).string]).string);
    $$$c2593.check($$$cl2592.String("test",4).segment((1),(2)).equals($$$cl2592.String("es",2)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("test[1:2] ",10),$$$cl2592.String("test",4).segment((1),(2)).string]).string);
    $$$c2593.check($$$cl2592.String("hello",5).segment((2),(2)).equals($$$cl2592.String("ll",2)),$$$cl2592.StringBuilder().appendAll([$$$cl2592.String("hello[2:2] ",11),$$$cl2592.String("hello",5).segment((2),(2)).string]).string);
    $$$c2593.check((function(){
        //SpreadOp at 404:10-404:24
        var lst$3763=[];
        var it$3764=seq$3762.iterator();
        var elem$3765;
        while ((elem$3765=it$3764.next())!==$$$cl2592.getFinished()){
            lst$3763.push(elem$3765.uppercased);
        }
        return $$$cl2592.ArraySequence(lst$3763);
    }()).equals($$$cl2592.Tuple($$$cl2592.String("ONE",3),$$$cl2592.Tuple($$$cl2592.String("TWO",3),$$$cl2592.Tuple($$$cl2592.String("THREE",5),$$$cl2592.Tuple($$$cl2592.String("FOUR",4),$$$cl2592.Tuple($$$cl2592.String("FIVE",4),$$$cl2592.Tuple($$$cl2592.String("SIX",3),$$$cl2592.Tuple($$$cl2592.String("SEVEN",5),$$$cl2592.Tuple($$$cl2592.String("EIGHT",5),$$$cl2592.Tuple($$$cl2592.String("NINE",4),$$$cl2592.Tuple($$$cl2592.String("TEN",3),$$$cl2592.getEmpty(),{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}),{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Tuple,a:{Rest:{t:$$$cl2592.Empty},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}}},First:{t:$$$cl2592.String},Element:{t:$$$cl2592.String}})),$$$cl2592.String("spread op",9));
    
    //AttributeDeclaration s2 at operators.ceylon (405:4-405:18)
    var s2$3766=(function(){var tmpvar$3767=(3);
    if (tmpvar$3767>0){
    var tmpvar$3768=(0);
    var tmpvar$3769=tmpvar$3768;
    for (var i=1; i<tmpvar$3767; i++){tmpvar$3769=tmpvar$3769.successor;}
    return $$$cl2592.Range(tmpvar$3768,tmpvar$3769,{Element:{t:$$$cl2592.Integer}})
    }else return $$$cl2592.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (406:4-406:18)
    var s3$3770=(function(){var tmpvar$3771=(5);
    if (tmpvar$3771>0){
    var tmpvar$3772=(2);
    var tmpvar$3773=tmpvar$3772;
    for (var i=1; i<tmpvar$3771; i++){tmpvar$3773=tmpvar$3773.successor;}
    return $$$cl2592.Range(tmpvar$3772,tmpvar$3773,{Element:{t:$$$cl2592.Integer}})
    }else return $$$cl2592.getEmpty();}());
    $$$c2593.check(s2$3766.size.equals((3)),$$$cl2592.String("0:3 [1]",7));
    var x$3774;
    if((x$3774=s2$3766.get((0)))!==null){
        $$$c2593.check(x$3774.equals((0)),$$$cl2592.String("0:3 [2]",7));
    }else {
        $$$c2593.fail($$$cl2592.String("0:3 [2]",7));
    }
    var x$3775;
    if((x$3775=s2$3766.get((2)))!==null){
        $$$c2593.check(x$3775.equals((2)),$$$cl2592.String("0:3 [3]",7));
    }else {
        $$$c2593.fail($$$cl2592.String("0:3 [3]",7));
    }
    $$$c2593.check(s3$3770.size.equals((5)),$$$cl2592.String("2:5 [1]",7));
    var x$3776;
    if((x$3776=s3$3770.get((0)))!==null){
        $$$c2593.check(x$3776.equals((2)),$$$cl2592.String("2:5 [1]",7));
    }else {
        $$$c2593.fail($$$cl2592.String("2:5 [1]",7));
    }
    var x$3777;
    if((x$3777=s3$3770.get((2)))!==null){
        $$$c2593.check(x$3777.equals((4)),$$$cl2592.String("2:5 [2]",7));
    }else {
        $$$c2593.fail($$$cl2592.String("2:5 [2]",7));
    }
    var x$3778;
    if((x$3778=s3$3770.get((4)))!==null){
        $$$c2593.check(x$3778.equals((6)),$$$cl2592.String("2:5 [3]",7));
    }else {
        $$$c2593.fail($$$cl2592.String("2:5 [3]",7));
    }
    $$$c2593.check((!$$$cl2592.nonempty((function(){var tmpvar$3779=(0);
    if (tmpvar$3779>0){
    var tmpvar$3780=(1);
    var tmpvar$3781=tmpvar$3780;
    for (var i=1; i<tmpvar$3779; i++){tmpvar$3781=tmpvar$3781.successor;}
    return $$$cl2592.Range(tmpvar$3780,tmpvar$3781,{Element:{t:$$$cl2592.Integer}})
    }else return $$$cl2592.getEmpty();}()))),$$$cl2592.String("1:0 empty",9));
    $$$c2593.check((!$$$cl2592.nonempty((function(){var tmpvar$3782=(-(1));
    if (tmpvar$3782>0){
    var tmpvar$3783=(1);
    var tmpvar$3784=tmpvar$3783;
    for (var i=1; i<tmpvar$3782; i++){tmpvar$3784=tmpvar$3784.successor;}
    return $$$cl2592.Range(tmpvar$3783,tmpvar$3784,{Element:{t:$$$cl2592.Integer}})
    }else return $$$cl2592.getEmpty();}()))),$$$cl2592.String("1:-1 empty",10));
};testSegments.$$metamodel$$={$nm:'testSegments',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//testSegments.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};

//MethodDefinition test at operators.ceylon (428:0-442:0)
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
    $$$c2593.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl2592.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl2592.Empty},Return:{t:$$$cl2592.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
