(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}};
var $$$cl2381=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2382=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$3344=$$$cl2381.Tuple((1),$$$cl2381.Tuple((2),$$$cl2381.Tuple((3),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$3345=$$$cl2381.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$3346=$$$cl2381.String("hola",4).iterator();
        var c$3347=$$$cl2381.getFinished();
        var next$c$3347=function(){return c$3347=it$3346.next();}
        next$c$3347();
        return function(){
            if(c$3347!==$$$cl2381.getFinished()){
                var c$3347$3348=c$3347;
                var tmpvar$3349=c$3347$3348;
                next$c$3347();
                return tmpvar$3349;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$3350=$$$cl2381.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$3351=$$$cl2381.String("hola",4).iterator();
        var c$3352=$$$cl2381.getFinished();
        var next$c$3352=function(){return c$3352=it$3351.next();}
        next$c$3352();
        return function(){
            if(c$3352!==$$$cl2381.getFinished()){
                var c$3352$3353=c$3352;
                var tmpvar$3354=c$3352$3353;
                next$c$3352();
                return tmpvar$3354;
            }
            return $$$cl2381.getFinished();
        }
    },{Absent:{t:$$$cl2381.Anything},Element:{t:$$$cl2381.Character}}).sequence;
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$3355=[(0)].reifyCeylonType({Absent:{t:$$$cl2381.Nothing},Element:{t:$$$cl2381.Integer}}).chain(seq$3344,{Absent:{t:$$$cl2381.Null},Element:{t:$$$cl2381.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$3356=[$$$cl2381.Character(65)].reifyCeylonType({Absent:{t:$$$cl2381.Nothing},Element:{t:$$$cl2381.Character}}).chain(lcomp$3345,{Absent:{t:$$$cl2381.Null},Element:{t:$$$cl2381.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$3357=$$$cl2381.Tuple((1),$$$cl2381.Tuple((2),$$$cl2381.Tuple((3),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$3358=$$$cl2381.Tuple((0),seq$3344,{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}}},First:{t:$$$cl2381.Integer},Element:{t:$$$cl2381.Integer}});
    $$$c2382.check($$$cl2381.className(seq$3344).startsWith($$$cl2381.String("ceylon.language::Tuple",22)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{1,2,3} is not a Tuple but a ",29),$$$cl2381.className(seq$3344).string]).string);
    $$$c2382.check((!$$$cl2381.className(lcomp$3345).startsWith($$$cl2381.String("ceylon.language::Tuple",22))),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("lazy comprehension is a Tuple ",30),$$$cl2381.className(lcomp$3345).string]).string);
    $$$c2382.check($$$cl2381.className(ecomp$3350).startsWith($$$cl2381.String("ceylon.language::ArraySequence",30)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("eager comprehension is not a Tuple but a ",41),$$$cl2381.className(ecomp$3350).string]).string);
    $$$c2382.check((!$$$cl2381.className(s2$3355).startsWith($$$cl2381.String("ceylon.language::Tuple",22))),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{0,*seq} is a Tuple ",20),$$$cl2381.className(s2$3355).string]).string);
    $$$c2382.check((!$$$cl2381.className(s3$3356).startsWith($$$cl2381.String("ceylon.language::Tuple",22))),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{x,*iter} is a Tuple ",21),$$$cl2381.className(s3$3356).string]).string);
    $$$c2382.check($$$cl2381.className(t1$3357).startsWith($$$cl2381.String("ceylon.language::Tuple",22)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("[1,2,3] is not a Tuple but a ",29),$$$cl2381.className(t1$3357).string]).string);
    $$$c2382.check($$$cl2381.className(t2$3358).startsWith($$$cl2381.String("ceylon.language::Tuple",22)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("[0,*seq] is not a Tuple but a ",30),$$$cl2381.className(t2$3358).string]).string);
    $$$c2382.check(seq$3344.equals(t1$3357),$$$cl2381.String("{1,2,3} != [1,2,3]",18));
    $$$c2382.check((!$$$cl2381.className(t2$3358).equals($$$cl2381.className(s2$3355))),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{0,*seq} != [0,*seq] ",21),$$$cl2381.className(t2$3358).string,$$$cl2381.String(" vs",3),$$$cl2381.className(s2$3355).string]).string);
    $$$c2382.check(seq$3344.size.equals((3)),$$$cl2381.String("seq.size!=3",11));
    $$$c2382.check(lcomp$3345.sequence.size.equals((4)),$$$cl2381.String("lcomp.size!=4",13));
    $$$c2382.check(ecomp$3350.size.equals((4)),$$$cl2381.String("ecomp.size!=4",13));
    $$$c2382.check(s2$3355.size.equals((4)),$$$cl2381.String("s2.size!=4",10));
    $$$c2382.check(s3$3356.sequence.size.equals((5)),$$$cl2381.String("s3.size!=5",10));
    $$$c2382.check(t1$3357.size.equals((3)),$$$cl2381.String("t1.size!=3",10));
    $$$c2382.check(t2$3358.size.equals((4)),$$$cl2381.String("t2.size!=4",10));
    $$$c2382.check((!$$$cl2381.className(lcomp$3345).startsWith($$$cl2381.String("ceylon.language::Tuple",22))),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{*comp} is not Tuple but ",25),$$$cl2381.className(lcomp$3345).string]).string);
    $$$c2382.check($$$cl2381.className(ecomp$3350).startsWith($$$cl2381.String("ceylon.language::ArraySequence",30)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{*ecomp} is not Tuple but ",26),$$$cl2381.className(ecomp$3350).string]).string);
    $$$c2382.check($$$cl2381.className(seq$3344).startsWith($$$cl2381.String("ceylon.language::Tuple",22)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("{*seq} is not Tuple but ",24),$$$cl2381.className(seq$3344).string]).string);
};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$3359=(-(4));
    var setI1$3359=function(i1$3360){return i1$3359=i1$3360;};
    i1$3359=(-i1$3359);
    $$$c2382.check(i1$3359.equals((4)),$$$cl2381.String("negation",8));
    i1$3359=(+(-(987654)));
    $$$c2382.check(i1$3359.equals((-(987654))),$$$cl2381.String("positive",8));
    i1$3359=(+(0));
    $$$c2382.check(i1$3359.equals((0)),$$$cl2381.String("+0=0",4));
    i1$3359=(-(0));
    $$$c2382.check(i1$3359.equals((0)),$$$cl2381.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$3361=(123).plus((456));
    var setI2$3361=function(i2$3362){return i2$3361=i2$3362;};
    $$$c2382.check(i2$3361.equals((579)),$$$cl2381.String("addition",8));
    i1$3359=i2$3361.minus((16));
    $$$c2382.check(i1$3359.equals((563)),$$$cl2381.String("subtraction",11));
    i2$3361=(-i1$3359).plus(i2$3361).minus((1));
    $$$c2382.check(i2$3361.equals((15)),$$$cl2381.String("-i1+i2-1",8));
    i1$3359=(3).times((7));
    $$$c2382.check(i1$3359.equals((21)),$$$cl2381.String("multiplication",14));
    i2$3361=i1$3359.times((2));
    $$$c2382.check(i2$3361.equals((42)),$$$cl2381.String("multiplication",14));
    i2$3361=(17).divided((4));
    $$$c2382.check(i2$3361.equals((4)),$$$cl2381.String("integer division",16));
    i1$3359=i2$3361.times((516)).divided((-i1$3359));
    $$$c2382.check(i1$3359.equals((-(98))),$$$cl2381.String("i2*516/-i1",10));
    i1$3359=(15).remainder((4));
    $$$c2382.check(i1$3359.equals((3)),$$$cl2381.String("modulo",6));
    i2$3361=(312).remainder((12));
    $$$c2382.check(i2$3361.equals((0)),$$$cl2381.String("modulo",6));
    i1$3359=(2).power((10));
    $$$c2382.check(i1$3359.equals((1024)),$$$cl2381.String("power",5));
    i2$3361=(10).power((6));
    $$$c2382.check(i2$3361.equals((1000000)),$$$cl2381.String("power",5));
};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$3363=$$$cl2381.Float(4.2).negativeValue;
    var setF1$3363=function(f1$3364){return f1$3363=f1$3364;};
    f1$3363=f1$3363.negativeValue;
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(4.2)),$$$cl2381.String("negation",8));
    f1$3363=(+$$$cl2381.Float(987654.9925567).negativeValue);
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(987654.9925567).negativeValue),$$$cl2381.String("positive",8));
    f1$3363=(+$$$cl2381.Float(0.0));
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(0.0)),$$$cl2381.String("+0.0=0.0",8));
    f1$3363=$$$cl2381.Float(0.0).negativeValue;
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(0.0)),$$$cl2381.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$3365=$$$cl2381.Float(3.14159265).plus($$$cl2381.Float(456.0));
    var setF2$3365=function(f2$3366){return f2$3365=f2$3366;};
    $$$c2382.check(f2$3365.equals($$$cl2381.Float(459.14159265)),$$$cl2381.String("addition",8));
    f1$3363=f2$3365.minus($$$cl2381.Float(0.0016));
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(459.13999265)),$$$cl2381.String("subtraction",11));
    f2$3365=f1$3363.negativeValue.plus(f2$3365).minus($$$cl2381.Float(1.2));
    $$$c2382.check(f2$3365.equals($$$cl2381.Float(1.1984000000000037).negativeValue),$$$cl2381.String("-f1+f2-1.2",10));
    f1$3363=$$$cl2381.Float(3.0).times($$$cl2381.Float(0.79));
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(2.37)),$$$cl2381.String("multiplication",14));
    f2$3365=f1$3363.times($$$cl2381.Float(2.0e13));
    $$$c2382.check(f2$3365.equals($$$cl2381.Float(47400000000000.0)),$$$cl2381.String("multiplication",14));
    f2$3365=$$$cl2381.Float(17.1).divided($$$cl2381.Float(4.0E-18));
    $$$c2382.check(f2$3365.equals($$$cl2381.Float(4275000000000000000.0)),$$$cl2381.String("division",8));
    f1$3363=f2$3365.times($$$cl2381.Float(51.6e2)).divided(f1$3363.negativeValue);
    $$$c2382.check(f2$3365.equals($$$cl2381.Float(4275000000000000000.0)),$$$cl2381.String("f2*51.6e2/-f1",13));
    f1$3363=$$$cl2381.Float(150.0).power($$$cl2381.Float(0.5));
    $$$c2382.check(f1$3363.equals($$$cl2381.Float(12.24744871391589)),$$$cl2381.String("power",5));
};

//ClassDefinition OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl2381.initTypeProto(OpTest1,'operators::OpTest1',$$$cl2381.Basic);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$3367=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$3368=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$3369=(o1$3367===o2$3368);
    var setB1$3369=function(b1$3370){return b1$3369=b1$3370;};
    $$$c2382.check((!b1$3369),$$$cl2381.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$3371=(o1$3367===o1$3367);
    var setB2$3371=function(b2$3372){return b2$3371=b2$3372;};
    $$$c2382.check(b2$3371,$$$cl2381.String("identity",8));
    b1$3369=o1$3367.equals(o2$3368);
    $$$c2382.check((!b1$3369),$$$cl2381.String("equals",6));
    b2$3371=o1$3367.equals(o1$3367);
    $$$c2382.check(b2$3371,$$$cl2381.String("equals",6));
    b1$3369=(1).equals((2));
    $$$c2382.check((!b1$3369),$$$cl2381.String("equals",6));
    b2$3371=(!(1).equals((2)));
    $$$c2382.check(b2$3371,$$$cl2381.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$3373=(!b2$3371);
    var setB3$3373=function(b3$3374){return b3$3373=b3$3374;};
    $$$c2382.check((!b3$3373),$$$cl2381.String("not",3));
    b1$3369=(true&&false);
    $$$c2382.check((!b1$3369),$$$cl2381.String("and",3));
    b2$3371=(b1$3369&&true);
    $$$c2382.check((!b2$3371),$$$cl2381.String("and",3));
    b3$3373=(true&&true);
    $$$c2382.check(b3$3373,$$$cl2381.String("and",3));
    b1$3369=(true||false);
    $$$c2382.check(b1$3369,$$$cl2381.String("or",2));
    b2$3371=(false||b1$3369);
    $$$c2382.check(b2$3371,$$$cl2381.String("or",2));
    b3$3373=(false||false);
    $$$c2382.check((!b3$3373),$$$cl2381.String("or",2));
};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-152:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$3375=$$$cl2381.String("str1",4).compare($$$cl2381.String("str2",4));
    $$$c2382.check(c1$3375.equals($$$cl2381.getSmaller()),$$$cl2381.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$3376=$$$cl2381.String("str2",4).compare($$$cl2381.String("str1",4));
    $$$c2382.check(c2$3376.equals($$$cl2381.getLarger()),$$$cl2381.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$3377=$$$cl2381.String("str1",4).compare($$$cl2381.String("str1",4));
    $$$c2382.check(c3$3377.equals($$$cl2381.getEqual()),$$$cl2381.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$3378=$$$cl2381.String("",0).compare($$$cl2381.String("",0));
    $$$c2382.check(c4$3378.equals($$$cl2381.getEqual()),$$$cl2381.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$3379=$$$cl2381.String("str1",4).compare($$$cl2381.String("",0));
    $$$c2382.check(c5$3379.equals($$$cl2381.getLarger()),$$$cl2381.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$3380=$$$cl2381.String("",0).compare($$$cl2381.String("str2",4));
    $$$c2382.check(c6$3380.equals($$$cl2381.getSmaller()),$$$cl2381.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$3381=$$$cl2381.String("str1",4).compare($$$cl2381.String("str2",4)).equals($$$cl2381.getSmaller());
    var setB1$3381=function(b1$3382){return b1$3381=b1$3382;};
    $$$c2382.check(b1$3381,$$$cl2381.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$3383=$$$cl2381.String("str1",4).compare($$$cl2381.String("str2",4)).equals($$$cl2381.getLarger());
    var setB2$3383=function(b2$3384){return b2$3383=b2$3384;};
    $$$c2382.check((!b2$3383),$$$cl2381.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$3385=($$$cl2381.String("str1",4).compare($$$cl2381.String("str2",4))!==$$$cl2381.getLarger());
    var setB3$3385=function(b3$3386){return b3$3385=b3$3386;};
    $$$c2382.check(b3$3385,$$$cl2381.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$3387=($$$cl2381.String("str1",4).compare($$$cl2381.String("str2",4))!==$$$cl2381.getSmaller());
    var setB4$3387=function(b4$3388){return b4$3387=b4$3388;};
    $$$c2382.check((!b4$3387),$$$cl2381.String("large as",8));
    b1$3381=$$$cl2381.String("str1",4).compare($$$cl2381.String("str1",4)).equals($$$cl2381.getSmaller());
    $$$c2382.check((!b1$3381),$$$cl2381.String("smaller",7));
    b2$3383=$$$cl2381.String("str1",4).compare($$$cl2381.String("str1",4)).equals($$$cl2381.getLarger());
    $$$c2382.check((!b2$3383),$$$cl2381.String("larger",6));
    b3$3385=($$$cl2381.String("str1",4).compare($$$cl2381.String("str1",4))!==$$$cl2381.getLarger());
    $$$c2382.check(b3$3385,$$$cl2381.String("small as",8));
    b4$3387=($$$cl2381.String("str1",4).compare($$$cl2381.String("str1",4))!==$$$cl2381.getSmaller());
    $$$c2382.check(b4$3387,$$$cl2381.String("large as",8));
    
    //AttributeDeclaration a at operators.ceylon (140:4-140:15)
    var a$3389=(0);
    
    //AttributeDeclaration c at operators.ceylon (141:4-141:16)
    var c$3390=(10);
    $$$c2382.check((tmpvar$3391=(5),tmpvar$3391.compare(a$3389)===$$$cl2381.getLarger()&&tmpvar$3391.compare(c$3390)===$$$cl2381.getSmaller()),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<5<",3),c$3390.string]).string);
    $$$c2382.check((tmpvar$3392=(0),tmpvar$3392.compare(a$3389)!==$$$cl2381.getSmaller()&&tmpvar$3392.compare(c$3390)===$$$cl2381.getSmaller()),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<=0<",4),c$3390.string]).string);
    $$$c2382.check((tmpvar$3393=(10),tmpvar$3393.compare(a$3389)===$$$cl2381.getLarger()&&tmpvar$3393.compare(c$3390)!==$$$cl2381.getLarger()),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<10<=",5),c$3390.string]).string);
    $$$c2382.check((tmpvar$3394=(0),tmpvar$3394.compare(a$3389)!==$$$cl2381.getSmaller()&&tmpvar$3394.compare(c$3390)!==$$$cl2381.getLarger()),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<=0<=",5),c$3390.string]).string);
    $$$c2382.check((tmpvar$3395=(10),tmpvar$3395.compare(a$3389)!==$$$cl2381.getSmaller()&&tmpvar$3395.compare(c$3390)!==$$$cl2381.getLarger()),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<=10<=",6),c$3390.string]).string);
    $$$c2382.check((!(tmpvar$3396=(15),tmpvar$3396.compare(a$3389)===$$$cl2381.getLarger()&&tmpvar$3396.compare(c$3390)===$$$cl2381.getSmaller())),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<15<",4),c$3390.string,$$$cl2381.String(" WTF",4)]).string);
    $$$c2382.check((!(tmpvar$3397=(10),tmpvar$3397.compare(a$3389)!==$$$cl2381.getSmaller()&&tmpvar$3397.compare(c$3390)===$$$cl2381.getSmaller())),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<=10<",5),c$3390.string,$$$cl2381.String(" WTF",4)]).string);
    $$$c2382.check((!(tmpvar$3398=(0),tmpvar$3398.compare(a$3389)===$$$cl2381.getLarger()&&tmpvar$3398.compare(c$3390)!==$$$cl2381.getLarger())),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<0<=",4),c$3390.string,$$$cl2381.String(" WTF",4)]).string);
    $$$c2382.check((!(tmpvar$3399=(11),tmpvar$3399.compare(a$3389)!==$$$cl2381.getSmaller()&&tmpvar$3399.compare(c$3390)!==$$$cl2381.getLarger())),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<=11<=",6),c$3390.string,$$$cl2381.String(" WTF",4)]).string);
    $$$c2382.check((!(tmpvar$3400=(-(1)),tmpvar$3400.compare(a$3389)!==$$$cl2381.getSmaller()&&tmpvar$3400.compare(c$3390)!==$$$cl2381.getLarger())),$$$cl2381.StringBuilder().appendAll([a$3389.string,$$$cl2381.String("<=-1<=",6),c$3390.string,$$$cl2381.String(" WTF",4)]).string);
};

//MethodDefinition testOtherOperators at operators.ceylon (154:0-166:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (155:4-155:42)
    var entry$3401=$$$cl2381.Entry((47),$$$cl2381.String("hi there",8),{Key:{t:$$$cl2381.Integer},Item:{t:$$$cl2381.String}});
    $$$c2382.check(entry$3401.key.equals((47)),$$$cl2381.String("entry key",9));
    $$$c2382.check(entry$3401.item.equals($$$cl2381.String("hi there",8)),$$$cl2381.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (158:4-158:30)
    var entry2$3402=$$$cl2381.Entry(true,entry$3401,{Key:{t:$$$cl2381.true$3217},Item:{t:$$$cl2381.Entry,a:{Key:{t:$$$cl2381.Integer},Item:{t:$$$cl2381.String}}}});
    $$$c2382.check(entry2$3402.key.equals(true),$$$cl2381.String("entry key",9));
    $$$c2382.check(entry2$3402.item.equals($$$cl2381.Entry((47),$$$cl2381.String("hi there",8),{Key:{t:$$$cl2381.Integer},Item:{t:$$$cl2381.String}})),$$$cl2381.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (162:4-162:41)
    var s1$3403=(opt$3404=(true?$$$cl2381.String("ok",2):null),opt$3404!==null?opt$3404:$$$cl2381.String("noo",3));
    var opt$3404;
    $$$c2382.check(s1$3403.equals($$$cl2381.String("ok",2)),$$$cl2381.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (164:4-164:47)
    var s2$3405=(opt$3406=(false?$$$cl2381.String("what?",5):null),opt$3406!==null?opt$3406:$$$cl2381.String("great",5));
    var opt$3406;
    $$$c2382.check(s2$3405.equals($$$cl2381.String("great",5)),$$$cl2381.String("then/else 2",11));
};

//MethodDefinition testCollectionOperators at operators.ceylon (168:0-180:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (169:4-169:33)
    var seq1$3407=$$$cl2381.Tuple($$$cl2381.String("one",3),$$$cl2381.Tuple($$$cl2381.String("two",3),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (170:4-170:23)
    var s1$3408=seq1$3407.get((0));
    $$$c2382.check(s1$3408.equals($$$cl2381.String("one",3)),$$$cl2381.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (172:4-172:28)
    var s2$3409=seq1$3407.get((2));
    $$$c2382.check((!$$$cl2381.exists(s2$3409)),$$$cl2381.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (174:4-174:29)
    var s3$3410=seq1$3407.get((-(1)));
    $$$c2382.check((!$$$cl2381.exists(s3$3410)),$$$cl2381.String("lookup",6));
};

//ClassDefinition NullsafeTest at operators.ceylon (182:0-187:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl2381.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl2381.Basic);
        (function($$nullsafeTest){
            
            //MethodDefinition f at operators.ceylon (183:4-183:33)
            $$nullsafeTest.f=function f(){
                var $$nullsafeTest=this;
                return (1);
            };
            //MethodDefinition f2 at operators.ceylon (184:4-186:4)
            $$nullsafeTest.f2=function f2(x$3411){
                var $$nullsafeTest=this;
                return x$3411();
            };
        })(NullsafeTest.$$.prototype);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (189:0-191:0)
function nullsafeTest(f$3412){
    return f$3412();
};

//MethodDefinition testNullsafeOperators at operators.ceylon (193:0-234:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (194:4-194:27)
    var seq$3413=$$$cl2381.Tuple($$$cl2381.String("hi",2),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (195:4-195:34)
    var s1$3414=(opt$3415=seq$3413.get((0)),opt$3415!==null?opt$3415:$$$cl2381.String("null",4));
    var opt$3415;
    $$$c2382.check(s1$3414.equals($$$cl2381.String("hi",2)),$$$cl2381.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (197:4-197:34)
    var s2$3416=(opt$3417=seq$3413.get((1)),opt$3417!==null?opt$3417:$$$cl2381.String("null",4));
    var opt$3417;
    $$$c2382.check(s2$3416.equals($$$cl2381.String("null",4)),$$$cl2381.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (200:4-200:21)
    var s3$3418=null;
    
    //AttributeDeclaration s4 at operators.ceylon (201:4-201:23)
    var s4$3419=$$$cl2381.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (202:4-202:42)
    var s5$3420=(opt$3421=(opt$3422=s3$3418,opt$3422!==null?opt$3422.uppercased:null),opt$3421!==null?opt$3421:$$$cl2381.String("null",4));
    var opt$3421,opt$3422;
    
    //AttributeDeclaration s6 at operators.ceylon (203:4-203:42)
    var s6$3423=(opt$3424=(opt$3425=s4$3419,opt$3425!==null?opt$3425.uppercased:null),opt$3424!==null?opt$3424:$$$cl2381.String("null",4));
    var opt$3424,opt$3425;
    $$$c2382.check(s5$3420.equals($$$cl2381.String("null",4)),$$$cl2381.String("nullsafe member 1",17));
    $$$c2382.check(s6$3423.equals($$$cl2381.String("TEST",4)),$$$cl2381.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (206:4-206:28)
    var obj$3426=null;
    
    //AttributeDeclaration i at operators.ceylon (207:4-207:25)
    var i$3427=(opt$3428=obj$3426,$$$cl2381.JsCallable(opt$3428,opt$3428!==null?opt$3428.f:null))();
    var opt$3428;
    $$$c2382.check((!$$$cl2381.exists(i$3427)),$$$cl2381.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (209:4-209:37)
    var f2$3429=$$$cl2381.$JsCallable((opt$3430=obj$3426,$$$cl2381.JsCallable(opt$3430,opt$3430!==null?opt$3430.f:null)),{Arguments:{t:$$$cl2381.Empty},Return:{ t:'u', l:[{t:$$$cl2381.Null},{t:$$$cl2381.Integer}]}});
    var opt$3430;
    $$$c2382.check((!$$$cl2381.exists(nullsafeTest($$$cl2381.$JsCallable(f2$3429,{Arguments:{t:$$$cl2381.Empty},Return:{ t:'u', l:[{t:$$$cl2381.Null},{t:$$$cl2381.Integer}]}})))),$$$cl2381.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (211:4-211:38)
    var f3$3431=$$$cl2381.$JsCallable((opt$3432=obj$3426,$$$cl2381.JsCallable(opt$3432,opt$3432!==null?opt$3432.f:null)),{Arguments:{t:$$$cl2381.Empty},Return:{ t:'u', l:[{t:$$$cl2381.Null},{t:$$$cl2381.Integer}]}});
    var opt$3432;
    $$$c2382.check($$$cl2381.exists(f3$3431),$$$cl2381.String("nullsafe method ref 2",21));
    (opt$3433=obj$3426,$$$cl2381.JsCallable(opt$3433,opt$3433!==null?opt$3433.f:null))();
    var opt$3433;
    $$$c2382.check((!$$$cl2381.exists((opt$3434=obj$3426,$$$cl2381.JsCallable(opt$3434,opt$3434!==null?opt$3434.f:null))())),$$$cl2381.String("nullsafe simple call",20));
    var opt$3434;
    
    //MethodDefinition getNullsafe at operators.ceylon (215:4-215:46)
    function getNullsafe$3435(){
        return obj$3426;
    };
    
    //MethodDeclaration f4 at operators.ceylon (216:4-216:39)
    var f4$3436=function (){
        return (opt$3437=getNullsafe$3435(),$$$cl2381.JsCallable(opt$3437,opt$3437!==null?opt$3437.f:null))();
    };
    var opt$3437;
    
    //AttributeDeclaration result_f4 at operators.ceylon (217:4-217:29)
    var result_f4$3438=f4$3436();
    $$$c2382.check((!$$$cl2381.exists(result_f4$3438)),$$$cl2381.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (219:4-219:36)
    var i2$3439=(opt$3440=getNullsafe$3435(),$$$cl2381.JsCallable(opt$3440,opt$3440!==null?opt$3440.f:null))();
    var opt$3440;
    $$$c2382.check((!$$$cl2381.exists(i2$3439)),$$$cl2381.String("nullsafe invoke 3",17));
    $$$c2382.check((!$$$cl2381.exists(NullsafeTest().f2($$$cl2381.$JsCallable((opt$3441=getNullsafe$3435(),$$$cl2381.JsCallable(opt$3441,opt$3441!==null?opt$3441.f:null)),{Arguments:{t:$$$cl2381.Empty},Return:{ t:'u', l:[{t:$$$cl2381.Null},{t:$$$cl2381.Integer}]}})))),$$$cl2381.String("nullsafe method ref 3",21));
    var opt$3441;
    
    //AttributeDeclaration obj2 at operators.ceylon (222:4-222:39)
    var obj2$3442=NullsafeTest();
    var i3$3443;
    if((i3$3443=(opt$3444=obj2$3442,$$$cl2381.JsCallable(opt$3444,opt$3444!==null?opt$3444.f:null))())!==null){
        $$$c2382.check(i3$3443.equals((1)),$$$cl2381.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2382.fail($$$cl2381.String("nullsafe invoke 4 (null)",24));
    }
    var opt$3444;
    
    //MethodDeclaration obj2_f at operators.ceylon (228:4-228:34)
    var obj2_f$3445=function (){
        return (opt$3446=obj2$3442,$$$cl2381.JsCallable(opt$3446,opt$3446!==null?opt$3446.f:null))();
    };
    var opt$3446;
    var i3$3447;
    if((i3$3447=obj2_f$3445())!==null){
        $$$c2382.check(i3$3447.equals((1)),$$$cl2381.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2382.fail($$$cl2381.String("nullsafe method ref 4 (null)",28));
    }
};

//MethodDefinition testIncDecOperators at operators.ceylon (236:0-311:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (237:4-237:27)
    var x0$3448=(1);
    var setX0$3448=function(x0$3449){return x0$3448=x0$3449;};
    
    //AttributeGetterDefinition x at operators.ceylon (238:4-238:27)
    var getX$3450=function(){
        return x0$3448;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (238:29-238:48)
    var setX$3450=function(x$3451){
        x0$3448=x$3451;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (240:4-240:27)
    var i1$3452=(1);
    var setI1$3452=function(i1$3453){return i1$3452=i1$3453;};
    
    //MethodDefinition f1 at operators.ceylon (241:4-248:4)
    function f1$3454(){
        
        //AttributeDeclaration i2 at operators.ceylon (242:8-242:25)
        var i2$3455=(i1$3452=i1$3452.successor);
        
        //AttributeDeclaration x2 at operators.ceylon (243:8-243:24)
        var x2$3456=(setX$3450(getX$3450().successor),getX$3450());
        $$$c2382.check(i1$3452.equals((2)),$$$cl2381.String("prefix increment 1",18));
        $$$c2382.check(i2$3455.equals((2)),$$$cl2381.String("prefix increment 2",18));
        $$$c2382.check(getX$3450().equals((2)),$$$cl2381.String("prefix increment 3",18));
        $$$c2382.check(x2$3456.equals((2)),$$$cl2381.String("prefix increment 4",18));
    };
    f1$3454();
    
    //ClassDefinition C1 at operators.ceylon (251:4-255:4)
    function C1$3457($$c1$3457){
        $init$C1$3457();
        if ($$c1$3457===undefined)$$c1$3457=new C1$3457.$$;
        
        //AttributeDeclaration i at operators.ceylon (252:8-252:37)
        $$c1$3457.i$3458_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (253:8-253:31)
        $$c1$3457.x0$3459_=(1);
        return $$c1$3457;
    }
    function $init$C1$3457(){
        if (C1$3457.$$===undefined){
            $$$cl2381.initTypeProto(C1$3457,'operators::testIncDecOperators.C1',$$$cl2381.Basic);
            (function($$c1$3457){
                
                //AttributeDeclaration i at operators.ceylon (252:8-252:37)
                $$$cl2381.defineAttr($$c1$3457,'i',function(){return this.i$3458_;},function(i$3460){return this.i$3458_=i$3460;});
                
                //AttributeDeclaration x0 at operators.ceylon (253:8-253:31)
                $$$cl2381.defineAttr($$c1$3457,'x0$3459',function(){return this.x0$3459_;},function(x0$3461){return this.x0$3459_=x0$3461;});
                
                //AttributeGetterDefinition x at operators.ceylon (254:8-254:38)
                $$$cl2381.defineAttr($$c1$3457,'x',function(){
                    var $$c1$3457=this;
                    return $$c1$3457.x0$3459;
                },function(x$3462){
                    var $$c1$3457=this;
                    $$c1$3457.x0$3459=x$3462;
                });
            })(C1$3457.$$.prototype);
        }
        return C1$3457;
    }
    $init$C1$3457();
    
    //AttributeDeclaration c1 at operators.ceylon (256:4-256:16)
    var c1$3463=C1$3457();
    
    //AttributeDeclaration i3 at operators.ceylon (257:4-257:27)
    var i3$3464=(0);
    var setI3$3464=function(i3$3465){return i3$3464=i3$3465;};
    
    //MethodDefinition f2 at operators.ceylon (258:4-261:4)
    function f2$3466(){
        (i3$3464=i3$3464.successor);
        return c1$3463;
    };
    
    //AttributeDeclaration i4 at operators.ceylon (262:4-262:25)
    var i4$3467=(tmp$3468=f2$3466(),tmp$3468.i=tmp$3468.i.successor);
    var tmp$3468;
    
    //AttributeDeclaration x4 at operators.ceylon (263:4-263:25)
    var x4$3469=(tmp$3470=f2$3466(),tmp$3470.x=tmp$3470.x.successor,tmp$3470.x);
    var tmp$3470;
    $$$c2382.check(i4$3467.equals((2)),$$$cl2381.String("prefix increment 5",18));
    $$$c2382.check(c1$3463.i.equals((2)),$$$cl2381.String("prefix increment 6",18));
    $$$c2382.check(x4$3469.equals((2)),$$$cl2381.String("prefix increment 7",18));
    $$$c2382.check(c1$3463.x.equals((2)),$$$cl2381.String("prefix increment 8",18));
    $$$c2382.check(i3$3464.equals((2)),$$$cl2381.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (270:4-274:4)
    function f3$3471(){
        
        //AttributeDeclaration i2 at operators.ceylon (271:8-271:25)
        var i2$3472=(i1$3452=i1$3452.predecessor);
        $$$c2382.check(i1$3452.equals((1)),$$$cl2381.String("prefix decrement",16));
        $$$c2382.check(i2$3472.equals((1)),$$$cl2381.String("prefix decrement",16));
    };
    f3$3471();
    
    //AttributeDeclaration i5 at operators.ceylon (277:4-277:25)
    var i5$3473=(tmp$3474=f2$3466(),tmp$3474.i=tmp$3474.i.predecessor);
    var tmp$3474;
    $$$c2382.check(i5$3473.equals((1)),$$$cl2381.String("prefix decrement",16));
    $$$c2382.check(c1$3463.i.equals((1)),$$$cl2381.String("prefix decrement",16));
    $$$c2382.check(i3$3464.equals((3)),$$$cl2381.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (282:4-289:4)
    function f4$3475(){
        
        //AttributeDeclaration i2 at operators.ceylon (283:8-283:25)
        var i2$3476=(oldi1$3477=i1$3452,i1$3452=oldi1$3477.successor,oldi1$3477);
        var oldi1$3477;
        
        //AttributeDeclaration x2 at operators.ceylon (284:8-284:24)
        var x2$3478=(oldx$3479=getX$3450(),setX$3450(oldx$3479.successor),oldx$3479);
        var oldx$3479;
        $$$c2382.check(i1$3452.equals((2)),$$$cl2381.String("postfix increment 1",19));
        $$$c2382.check(i2$3476.equals((1)),$$$cl2381.String("postfix increment 2",19));
        $$$c2382.check(getX$3450().equals((3)),$$$cl2381.String("postfix increment 3",19));
        $$$c2382.check(x2$3478.equals((2)),$$$cl2381.String("postfix increment 4",19));
    };
    f4$3475();
    
    //AttributeDeclaration i6 at operators.ceylon (292:4-292:25)
    var i6$3480=(tmp$3481=f2$3466(),oldi$3482=tmp$3481.i,tmp$3481.i=oldi$3482.successor,oldi$3482);
    var tmp$3481,oldi$3482;
    
    //AttributeDeclaration x6 at operators.ceylon (293:4-293:25)
    var x6$3483=(tmp$3484=f2$3466(),oldx$3485=tmp$3484.x,tmp$3484.x=oldx$3485.successor,oldx$3485);
    var tmp$3484,oldx$3485;
    $$$c2382.check(i6$3480.equals((1)),$$$cl2381.String("postfix increment 5",19));
    $$$c2382.check(c1$3463.i.equals((2)),$$$cl2381.String("postfix increment 6",19));
    $$$c2382.check(x6$3483.equals((2)),$$$cl2381.String("postfix increment 7 ",20));
    $$$c2382.check(c1$3463.x.equals((3)),$$$cl2381.String("postfix increment 8 ",20));
    $$$c2382.check(i3$3464.equals((5)),$$$cl2381.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (300:4-304:4)
    function f5$3486(){
        
        //AttributeDeclaration i2 at operators.ceylon (301:8-301:25)
        var i2$3487=(oldi1$3488=i1$3452,i1$3452=oldi1$3488.predecessor,oldi1$3488);
        var oldi1$3488;
        $$$c2382.check(i1$3452.equals((1)),$$$cl2381.String("postfix decrement",17));
        $$$c2382.check(i2$3487.equals((2)),$$$cl2381.String("postfix decrement",17));
    };
    f5$3486();
    
    //AttributeDeclaration i7 at operators.ceylon (307:4-307:25)
    var i7$3489=(tmp$3490=f2$3466(),oldi$3491=tmp$3490.i,tmp$3490.i=oldi$3491.predecessor,oldi$3491);
    var tmp$3490,oldi$3491;
    $$$c2382.check(i7$3489.equals((2)),$$$cl2381.String("postfix decrement",17));
    $$$c2382.check(c1$3463.i.equals((1)),$$$cl2381.String("postfix decrement",17));
    $$$c2382.check(i3$3464.equals((6)),$$$cl2381.String("postfix decrement",17));
};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (313:0-364:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (314:4-314:27)
    var i1$3492=(1);
    var setI1$3492=function(i1$3493){return i1$3492=i1$3493;};
    
    //AttributeDeclaration x0 at operators.ceylon (315:4-315:27)
    var x0$3494=(1);
    var setX0$3494=function(x0$3495){return x0$3494=x0$3495;};
    
    //AttributeGetterDefinition x at operators.ceylon (316:4-316:27)
    var getX$3496=function(){
        return x0$3494;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (316:29-316:46)
    var setX$3496=function(x$3497){
        x0$3494=x$3497;
    };
    (i1$3492=i1$3492.plus((10)));
    (setX$3496(getX$3496().plus((10))),getX$3496());
    $$$c2382.check(i1$3492.equals((11)),$$$cl2381.String("+= operator 1",13));
    $$$c2382.check(getX$3496().equals((11)),$$$cl2381.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (322:4-322:36)
    var i2$3498=(i1$3492=i1$3492.plus((-(5))));
    var setI2$3498=function(i2$3499){return i2$3498=i2$3499;};
    
    //AttributeDeclaration x2 at operators.ceylon (323:4-323:35)
    var x2$3500=(setX$3496(getX$3496().plus((-(5)))),getX$3496());
    var setX2$3500=function(x2$3501){return x2$3500=x2$3501;};
    $$$c2382.check(i2$3498.equals((6)),$$$cl2381.String("+= operator 3",13));
    $$$c2382.check(i1$3492.equals((6)),$$$cl2381.String("+= operator 4",13));
    $$$c2382.check(x2$3500.equals((6)),$$$cl2381.String("+= operator 5",13));
    $$$c2382.check(getX$3496().equals((6)),$$$cl2381.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (329:4-333:4)
    function C1$3502($$c1$3502){
        $init$C1$3502();
        if ($$c1$3502===undefined)$$c1$3502=new C1$3502.$$;
        
        //AttributeDeclaration i at operators.ceylon (330:8-330:37)
        $$c1$3502.i$3503_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (331:8-331:31)
        $$c1$3502.x0$3504_=(1);
        return $$c1$3502;
    }
    function $init$C1$3502(){
        if (C1$3502.$$===undefined){
            $$$cl2381.initTypeProto(C1$3502,'operators::testArithmeticAssignOperators.C1',$$$cl2381.Basic);
            (function($$c1$3502){
                
                //AttributeDeclaration i at operators.ceylon (330:8-330:37)
                $$$cl2381.defineAttr($$c1$3502,'i',function(){return this.i$3503_;},function(i$3505){return this.i$3503_=i$3505;});
                
                //AttributeDeclaration x0 at operators.ceylon (331:8-331:31)
                $$$cl2381.defineAttr($$c1$3502,'x0$3504',function(){return this.x0$3504_;},function(x0$3506){return this.x0$3504_=x0$3506;});
                
                //AttributeGetterDefinition x at operators.ceylon (332:8-332:38)
                $$$cl2381.defineAttr($$c1$3502,'x',function(){
                    var $$c1$3502=this;
                    return $$c1$3502.x0$3504;
                },function(x$3507){
                    var $$c1$3502=this;
                    $$c1$3502.x0$3504=x$3507;
                });
            })(C1$3502.$$.prototype);
        }
        return C1$3502;
    }
    $init$C1$3502();
    
    //AttributeDeclaration c1 at operators.ceylon (334:4-334:16)
    var c1$3508=C1$3502();
    
    //AttributeDeclaration i3 at operators.ceylon (335:4-335:27)
    var i3$3509=(0);
    var setI3$3509=function(i3$3510){return i3$3509=i3$3510;};
    
    //MethodDefinition f at operators.ceylon (336:4-339:4)
    function f$3511(){
        (i3$3509=i3$3509.successor);
        return c1$3508;
    };
    i2$3498=(tmp$3512=f$3511(),tmp$3512.i=tmp$3512.i.plus((11)));
    var tmp$3512;
    x2$3500=(tmp$3513=f$3511(),tmp$3513.x=tmp$3513.x.plus((11)),tmp$3513.x);
    var tmp$3513;
    $$$c2382.check(i2$3498.equals((12)),$$$cl2381.String("+= operator 7",13));
    $$$c2382.check(c1$3508.i.equals((12)),$$$cl2381.String("+= operator 8",13));
    $$$c2382.check(x2$3500.equals((12)),$$$cl2381.String("+= operator 9",13));
    $$$c2382.check(c1$3508.x.equals((12)),$$$cl2381.String("+= operator 10",14));
    $$$c2382.check(i3$3509.equals((2)),$$$cl2381.String("+= operator 11",14));
    i2$3498=(i1$3492=i1$3492.minus((14)));
    $$$c2382.check(i1$3492.equals((-(8))),$$$cl2381.String("-= operator",11));
    $$$c2382.check(i2$3498.equals((-(8))),$$$cl2381.String("-= operator",11));
    i2$3498=(i1$3492=i1$3492.times((-(3))));
    $$$c2382.check(i1$3492.equals((24)),$$$cl2381.String("*= operator",11));
    $$$c2382.check(i2$3498.equals((24)),$$$cl2381.String("*= operator",11));
    i2$3498=(i1$3492=i1$3492.divided((5)));
    $$$c2382.check(i1$3492.equals((4)),$$$cl2381.String("/= operator",11));
    $$$c2382.check(i2$3498.equals((4)),$$$cl2381.String("/= operator",11));
    i2$3498=(i1$3492=i1$3492.remainder((3)));
    $$$c2382.check(i1$3492.equals((1)),$$$cl2381.String("%= operator",11));
    $$$c2382.check(i2$3498.equals((1)),$$$cl2381.String("%= operator",11));
};

//MethodDefinition testAssignmentOperator at operators.ceylon (366:0-396:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (367:4-367:27)
    var i1$3514=(1);
    var setI1$3514=function(i1$3515){return i1$3514=i1$3515;};
    
    //AttributeDeclaration i2 at operators.ceylon (368:4-368:27)
    var i2$3516=(2);
    var setI2$3516=function(i2$3517){return i2$3516=i2$3517;};
    
    //AttributeDeclaration i3 at operators.ceylon (369:4-369:27)
    var i3$3518=(3);
    var setI3$3518=function(i3$3519){return i3$3518=i3$3519;};
    $$$c2382.check((i1$3514=(i2$3516=i3$3518)).equals((3)),$$$cl2381.String("assignment 1",12));
    $$$c2382.check(i1$3514.equals((3)),$$$cl2381.String("assignment 2",12));
    $$$c2382.check(i2$3516.equals((3)),$$$cl2381.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (374:4-374:28)
    var getX1$3520=function(){
        return i1$3514;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (374:30-374:51)
    var setX1$3520=function(x1$3521){
        i1$3514=x1$3521;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (375:4-375:28)
    var getX2$3522=function(){
        return i2$3516;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (375:30-375:51)
    var setX2$3522=function(x2$3523){
        i2$3516=x2$3523;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (376:4-376:28)
    var getX3$3524=function(){
        return i3$3518;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (376:30-376:51)
    var setX3$3524=function(x3$3525){
        i3$3518=x3$3525;
    };
    i1$3514=(1);
    i2$3516=(2);
    $$$c2382.check((setX1$3520((setX2$3522(getX3$3524()),getX2$3522())),getX1$3520()).equals((3)),$$$cl2381.String("assignment 4",12));
    $$$c2382.check(getX1$3520().equals((3)),$$$cl2381.String("assignment 5",12));
    $$$c2382.check(getX2$3522().equals((3)),$$$cl2381.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (383:4-387:4)
    function C$3526($$c$3526){
        $init$C$3526();
        if ($$c$3526===undefined)$$c$3526=new C$3526.$$;
        
        //AttributeDeclaration i at operators.ceylon (384:8-384:37)
        $$c$3526.i$3527_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (385:8-385:31)
        $$c$3526.x0$3528_=(1);
        return $$c$3526;
    }
    function $init$C$3526(){
        if (C$3526.$$===undefined){
            $$$cl2381.initTypeProto(C$3526,'operators::testAssignmentOperator.C',$$$cl2381.Basic);
            (function($$c$3526){
                
                //AttributeDeclaration i at operators.ceylon (384:8-384:37)
                $$$cl2381.defineAttr($$c$3526,'i',function(){return this.i$3527_;},function(i$3529){return this.i$3527_=i$3529;});
                
                //AttributeDeclaration x0 at operators.ceylon (385:8-385:31)
                $$$cl2381.defineAttr($$c$3526,'x0$3528',function(){return this.x0$3528_;},function(x0$3530){return this.x0$3528_=x0$3530;});
                
                //AttributeGetterDefinition x at operators.ceylon (386:8-386:38)
                $$$cl2381.defineAttr($$c$3526,'x',function(){
                    var $$c$3526=this;
                    return $$c$3526.x0$3528;
                },function(x$3531){
                    var $$c$3526=this;
                    $$c$3526.x0$3528=x$3531;
                });
            })(C$3526.$$.prototype);
        }
        return C$3526;
    }
    $init$C$3526();
    
    //AttributeDeclaration o1 at operators.ceylon (388:4-388:14)
    var o1$3532=C$3526();
    
    //AttributeDeclaration o2 at operators.ceylon (389:4-389:14)
    var o2$3533=C$3526();
    $$$c2382.check((o1$3532.i=(o2$3533.i=(3))).equals((3)),$$$cl2381.String("assignment 7",12));
    $$$c2382.check(o1$3532.i.equals((3)),$$$cl2381.String("assignment 8",12));
    $$$c2382.check(o2$3533.i.equals((3)),$$$cl2381.String("assignment 9",12));
    $$$c2382.check((tmp$3534=o1$3532,tmp$3534.x=(tmp$3535=o2$3533,tmp$3535.x=(3),tmp$3535.x),tmp$3534.x).equals((3)),$$$cl2381.String("assignment 10",13));
    var tmp$3534,tmp$3535;
    $$$c2382.check(o1$3532.x.equals((3)),$$$cl2381.String("assignment 11",13));
    $$$c2382.check(o2$3533.x.equals((3)),$$$cl2381.String("assignment 12",13));
};

//MethodDefinition testSegments at operators.ceylon (398:0-425:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (399:4-399:97)
    var seq$3536=$$$cl2381.Tuple($$$cl2381.String("one",3),$$$cl2381.Tuple($$$cl2381.String("two",3),$$$cl2381.Tuple($$$cl2381.String("three",5),$$$cl2381.Tuple($$$cl2381.String("four",4),$$$cl2381.Tuple($$$cl2381.String("five",4),$$$cl2381.Tuple($$$cl2381.String("six",3),$$$cl2381.Tuple($$$cl2381.String("seven",5),$$$cl2381.Tuple($$$cl2381.String("eight",5),$$$cl2381.Tuple($$$cl2381.String("nine",4),$$$cl2381.Tuple($$$cl2381.String("ten",3),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}});
    $$$c2382.check(seq$3536.segment((1),(2)).equals($$$cl2381.Tuple($$$cl2381.String("two",3),$$$cl2381.Tuple($$$cl2381.String("three",5),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}})),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("seq[1:2] ",9),seq$3536.segment((1),(2)).string]).string);
    $$$c2382.check(seq$3536.segment((3),(5)).equals($$$cl2381.Tuple($$$cl2381.String("four",4),$$$cl2381.Tuple($$$cl2381.String("five",4),$$$cl2381.Tuple($$$cl2381.String("six",3),$$$cl2381.Tuple($$$cl2381.String("seven",5),$$$cl2381.Tuple($$$cl2381.String("eight",5),$$$cl2381.getEmpty(),{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}),{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Tuple,a:{Rest:{t:$$$cl2381.Empty},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}}},First:{t:$$$cl2381.String},Element:{t:$$$cl2381.String}})),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("seq[3:5] ",9),seq$3536.segment((3),(5)).string]).string);
    $$$c2382.check($$$cl2381.String("test",4).segment((1),(2)).equals($$$cl2381.String("es",2)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("test[1:2] ",10),$$$cl2381.String("test",4).segment((1),(2)).string]).string);
    $$$c2382.check($$$cl2381.String("hello",5).segment((2),(2)).equals($$$cl2381.String("ll",2)),$$$cl2381.StringBuilder().appendAll([$$$cl2381.String("hello[2:2] ",11),$$$cl2381.String("hello",5).segment((2),(2)).string]).string);
    
    //AttributeDeclaration s2 at operators.ceylon (404:4-404:18)
    var s2$3537=(function(){var tmpvar$3538=(3);
    if (tmpvar$3538>0){
    var tmpvar$3539=(0);
    var tmpvar$3540=tmpvar$3539;
    for (var i=1; i<tmpvar$3538; i++){tmpvar$3540=tmpvar$3540.successor;}
    return $$$cl2381.Range(tmpvar$3539,tmpvar$3540)
    }else return $$$cl2381.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (405:4-405:18)
    var s3$3541=(function(){var tmpvar$3542=(5);
    if (tmpvar$3542>0){
    var tmpvar$3543=(2);
    var tmpvar$3544=tmpvar$3543;
    for (var i=1; i<tmpvar$3542; i++){tmpvar$3544=tmpvar$3544.successor;}
    return $$$cl2381.Range(tmpvar$3543,tmpvar$3544)
    }else return $$$cl2381.getEmpty();}());
    $$$c2382.check(s2$3537.size.equals((3)),$$$cl2381.String("0:3 [1]",7));
    var x$3545;
    if((x$3545=s2$3537.get((0)))!==null){
        $$$c2382.check(x$3545.equals((0)),$$$cl2381.String("0:3 [2]",7));
    }else {
        $$$c2382.fail($$$cl2381.String("0:3 [2]",7));
    }
    var x$3546;
    if((x$3546=s2$3537.get((2)))!==null){
        $$$c2382.check(x$3546.equals((2)),$$$cl2381.String("0:3 [3]",7));
    }else {
        $$$c2382.fail($$$cl2381.String("0:3 [3]",7));
    }
    $$$c2382.check(s3$3541.size.equals((5)),$$$cl2381.String("2:5 [1]",7));
    var x$3547;
    if((x$3547=s3$3541.get((0)))!==null){
        $$$c2382.check(x$3547.equals((2)),$$$cl2381.String("2:5 [1]",7));
    }else {
        $$$c2382.fail($$$cl2381.String("2:5 [1]",7));
    }
    var x$3548;
    if((x$3548=s3$3541.get((2)))!==null){
        $$$c2382.check(x$3548.equals((4)),$$$cl2381.String("2:5 [2]",7));
    }else {
        $$$c2382.fail($$$cl2381.String("2:5 [2]",7));
    }
    var x$3549;
    if((x$3549=s3$3541.get((4)))!==null){
        $$$c2382.check(x$3549.equals((6)),$$$cl2381.String("2:5 [3]",7));
    }else {
        $$$c2382.fail($$$cl2381.String("2:5 [3]",7));
    }
    $$$c2382.check((!$$$cl2381.nonempty((function(){var tmpvar$3550=(0);
    if (tmpvar$3550>0){
    var tmpvar$3551=(1);
    var tmpvar$3552=tmpvar$3551;
    for (var i=1; i<tmpvar$3550; i++){tmpvar$3552=tmpvar$3552.successor;}
    return $$$cl2381.Range(tmpvar$3551,tmpvar$3552)
    }else return $$$cl2381.getEmpty();}()))),$$$cl2381.String("1:0 empty",9));
    $$$c2382.check((!$$$cl2381.nonempty((function(){var tmpvar$3553=(-(1));
    if (tmpvar$3553>0){
    var tmpvar$3554=(1);
    var tmpvar$3555=tmpvar$3554;
    for (var i=1; i<tmpvar$3553; i++){tmpvar$3555=tmpvar$3555.successor;}
    return $$$cl2381.Range(tmpvar$3554,tmpvar$3555)
    }else return $$$cl2381.getEmpty();}()))),$$$cl2381.String("1:-1 empty",10));
};

//MethodDefinition test at operators.ceylon (427:0-441:0)
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
    $$$c2382.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
