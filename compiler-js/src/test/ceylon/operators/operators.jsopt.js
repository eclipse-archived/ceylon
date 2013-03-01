(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}};
var $$$cl2309=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2310=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$3256=$$$cl2309.Tuple((1),$$$cl2309.Tuple((2),$$$cl2309.Tuple((3),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$3257=$$$cl2309.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$3258=$$$cl2309.String("hola",4).iterator;
        var c$3259=$$$cl2309.getFinished();
        var next$c$3259=function(){return c$3259=it$3258.next();}
        next$c$3259();
        return function(){
            if(c$3259!==$$$cl2309.getFinished()){
                var c$3259$3260=c$3259;
                var tmpvar$3261=c$3259$3260;
                next$c$3259();
                return tmpvar$3261;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$3262=$$$cl2309.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$3263=$$$cl2309.String("hola",4).iterator;
        var c$3264=$$$cl2309.getFinished();
        var next$c$3264=function(){return c$3264=it$3263.next();}
        next$c$3264();
        return function(){
            if(c$3264!==$$$cl2309.getFinished()){
                var c$3264$3265=c$3264;
                var tmpvar$3266=c$3264$3265;
                next$c$3264();
                return tmpvar$3266;
            }
            return $$$cl2309.getFinished();
        }
    },{Absent:{t:$$$cl2309.Anything},Element:{t:$$$cl2309.Character}}).sequence;
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$3267=[(0)].reifyCeylonType({Absent:{t:$$$cl2309.Nothing},Element:{t:$$$cl2309.Integer}}).chain(seq$3256,{Absent:{t:$$$cl2309.Null},Element:{t:$$$cl2309.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$3268=[$$$cl2309.Character(65)].reifyCeylonType({Absent:{t:$$$cl2309.Nothing},Element:{t:$$$cl2309.Character}}).chain(lcomp$3257,{Absent:{t:$$$cl2309.Null},Element:{t:$$$cl2309.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$3269=$$$cl2309.Tuple((1),$$$cl2309.Tuple((2),$$$cl2309.Tuple((3),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$3270=$$$cl2309.Tuple((0),seq$3256,{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}}},First:{t:$$$cl2309.Integer},Element:{t:$$$cl2309.Integer}});
    $$$c2310.check($$$cl2309.className(seq$3256).startsWith($$$cl2309.String("ceylon.language::Tuple",22)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{1,2,3} is not a Tuple but a ",29),$$$cl2309.className(seq$3256).string]).string);
    $$$c2310.check((!$$$cl2309.className(lcomp$3257).startsWith($$$cl2309.String("ceylon.language::Tuple",22))),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("lazy comprehension is a Tuple ",30),$$$cl2309.className(lcomp$3257).string]).string);
    $$$c2310.check($$$cl2309.className(ecomp$3262).startsWith($$$cl2309.String("ceylon.language::ArraySequence",30)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("eager comprehension is not a Tuple but a ",41),$$$cl2309.className(ecomp$3262).string]).string);
    $$$c2310.check((!$$$cl2309.className(s2$3267).startsWith($$$cl2309.String("ceylon.language::Tuple",22))),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{0,*seq} is a Tuple ",20),$$$cl2309.className(s2$3267).string]).string);
    $$$c2310.check((!$$$cl2309.className(s3$3268).startsWith($$$cl2309.String("ceylon.language::Tuple",22))),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{x,*iter} is a Tuple ",21),$$$cl2309.className(s3$3268).string]).string);
    $$$c2310.check($$$cl2309.className(t1$3269).startsWith($$$cl2309.String("ceylon.language::Tuple",22)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("[1,2,3] is not a Tuple but a ",29),$$$cl2309.className(t1$3269).string]).string);
    $$$c2310.check($$$cl2309.className(t2$3270).startsWith($$$cl2309.String("ceylon.language::Tuple",22)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("[0,*seq] is not a Tuple but a ",30),$$$cl2309.className(t2$3270).string]).string);
    $$$c2310.check(seq$3256.equals(t1$3269),$$$cl2309.String("{1,2,3} != [1,2,3]",18));
    $$$c2310.check((!$$$cl2309.className(t2$3270).equals($$$cl2309.className(s2$3267))),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{0,*seq} != [0,*seq] ",21),$$$cl2309.className(t2$3270).string,$$$cl2309.String(" vs",3),$$$cl2309.className(s2$3267).string]).string);
    $$$c2310.check(seq$3256.size.equals((3)),$$$cl2309.String("seq.size!=3",11));
    $$$c2310.check(lcomp$3257.sequence.size.equals((4)),$$$cl2309.String("lcomp.size!=4",13));
    $$$c2310.check(ecomp$3262.size.equals((4)),$$$cl2309.String("ecomp.size!=4",13));
    $$$c2310.check(s2$3267.size.equals((4)),$$$cl2309.String("s2.size!=4",10));
    $$$c2310.check(s3$3268.sequence.size.equals((5)),$$$cl2309.String("s3.size!=5",10));
    $$$c2310.check(t1$3269.size.equals((3)),$$$cl2309.String("t1.size!=3",10));
    $$$c2310.check(t2$3270.size.equals((4)),$$$cl2309.String("t2.size!=4",10));
    $$$c2310.check((!$$$cl2309.className(lcomp$3257).startsWith($$$cl2309.String("ceylon.language::Tuple",22))),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{*comp} is not Tuple but ",25),$$$cl2309.className(lcomp$3257).string]).string);
    $$$c2310.check($$$cl2309.className(ecomp$3262).startsWith($$$cl2309.String("ceylon.language::ArraySequence",30)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{*ecomp} is not Tuple but ",26),$$$cl2309.className(ecomp$3262).string]).string);
    $$$c2310.check($$$cl2309.className(seq$3256).startsWith($$$cl2309.String("ceylon.language::Tuple",22)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("{*seq} is not Tuple but ",24),$$$cl2309.className(seq$3256).string]).string);
};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$3271=(-(4));
    var setI1$3271=function(i1$3272){return i1$3271=i1$3272;};
    i1$3271=(-i1$3271);
    $$$c2310.check(i1$3271.equals((4)),$$$cl2309.String("negation",8));
    i1$3271=(+(-(987654)));
    $$$c2310.check(i1$3271.equals((-(987654))),$$$cl2309.String("positive",8));
    i1$3271=(+(0));
    $$$c2310.check(i1$3271.equals((0)),$$$cl2309.String("+0=0",4));
    i1$3271=(-(0));
    $$$c2310.check(i1$3271.equals((0)),$$$cl2309.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$3273=(123).plus((456));
    var setI2$3273=function(i2$3274){return i2$3273=i2$3274;};
    $$$c2310.check(i2$3273.equals((579)),$$$cl2309.String("addition",8));
    i1$3271=i2$3273.minus((16));
    $$$c2310.check(i1$3271.equals((563)),$$$cl2309.String("subtraction",11));
    i2$3273=(-i1$3271).plus(i2$3273).minus((1));
    $$$c2310.check(i2$3273.equals((15)),$$$cl2309.String("-i1+i2-1",8));
    i1$3271=(3).times((7));
    $$$c2310.check(i1$3271.equals((21)),$$$cl2309.String("multiplication",14));
    i2$3273=i1$3271.times((2));
    $$$c2310.check(i2$3273.equals((42)),$$$cl2309.String("multiplication",14));
    i2$3273=(17).divided((4));
    $$$c2310.check(i2$3273.equals((4)),$$$cl2309.String("integer division",16));
    i1$3271=i2$3273.times((516)).divided((-i1$3271));
    $$$c2310.check(i1$3271.equals((-(98))),$$$cl2309.String("i2*516/-i1",10));
    i1$3271=(15).remainder((4));
    $$$c2310.check(i1$3271.equals((3)),$$$cl2309.String("modulo",6));
    i2$3273=(312).remainder((12));
    $$$c2310.check(i2$3273.equals((0)),$$$cl2309.String("modulo",6));
    i1$3271=(2).power((10));
    $$$c2310.check(i1$3271.equals((1024)),$$$cl2309.String("power",5));
    i2$3273=(10).power((6));
    $$$c2310.check(i2$3273.equals((1000000)),$$$cl2309.String("power",5));
};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$3275=$$$cl2309.Float(4.2).negativeValue;
    var setF1$3275=function(f1$3276){return f1$3275=f1$3276;};
    f1$3275=f1$3275.negativeValue;
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(4.2)),$$$cl2309.String("negation",8));
    f1$3275=(+$$$cl2309.Float(987654.9925567).negativeValue);
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(987654.9925567).negativeValue),$$$cl2309.String("positive",8));
    f1$3275=(+$$$cl2309.Float(0.0));
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(0.0)),$$$cl2309.String("+0.0=0.0",8));
    f1$3275=$$$cl2309.Float(0.0).negativeValue;
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(0.0)),$$$cl2309.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$3277=$$$cl2309.Float(3.14159265).plus($$$cl2309.Float(456.0));
    var setF2$3277=function(f2$3278){return f2$3277=f2$3278;};
    $$$c2310.check(f2$3277.equals($$$cl2309.Float(459.14159265)),$$$cl2309.String("addition",8));
    f1$3275=f2$3277.minus($$$cl2309.Float(0.0016));
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(459.13999265)),$$$cl2309.String("subtraction",11));
    f2$3277=f1$3275.negativeValue.plus(f2$3277).minus($$$cl2309.Float(1.2));
    $$$c2310.check(f2$3277.equals($$$cl2309.Float(1.1984000000000037).negativeValue),$$$cl2309.String("-f1+f2-1.2",10));
    f1$3275=$$$cl2309.Float(3.0).times($$$cl2309.Float(0.79));
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(2.37)),$$$cl2309.String("multiplication",14));
    f2$3277=f1$3275.times($$$cl2309.Float(2.0e13));
    $$$c2310.check(f2$3277.equals($$$cl2309.Float(47400000000000.0)),$$$cl2309.String("multiplication",14));
    f2$3277=$$$cl2309.Float(17.1).divided($$$cl2309.Float(4.0E-18));
    $$$c2310.check(f2$3277.equals($$$cl2309.Float(4275000000000000000.0)),$$$cl2309.String("division",8));
    f1$3275=f2$3277.times($$$cl2309.Float(51.6e2)).divided(f1$3275.negativeValue);
    $$$c2310.check(f2$3277.equals($$$cl2309.Float(4275000000000000000.0)),$$$cl2309.String("f2*51.6e2/-f1",13));
    f1$3275=$$$cl2309.Float(150.0).power($$$cl2309.Float(0.5));
    $$$c2310.check(f1$3275.equals($$$cl2309.Float(12.24744871391589)),$$$cl2309.String("power",5));
};

//ClassDefinition OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl2309.initTypeProto(OpTest1,'operators::OpTest1',$$$cl2309.Basic);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$3279=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$3280=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$3281=(o1$3279===o2$3280);
    var setB1$3281=function(b1$3282){return b1$3281=b1$3282;};
    $$$c2310.check((!b1$3281),$$$cl2309.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$3283=(o1$3279===o1$3279);
    var setB2$3283=function(b2$3284){return b2$3283=b2$3284;};
    $$$c2310.check(b2$3283,$$$cl2309.String("identity",8));
    b1$3281=o1$3279.equals(o2$3280);
    $$$c2310.check((!b1$3281),$$$cl2309.String("equals",6));
    b2$3283=o1$3279.equals(o1$3279);
    $$$c2310.check(b2$3283,$$$cl2309.String("equals",6));
    b1$3281=(1).equals((2));
    $$$c2310.check((!b1$3281),$$$cl2309.String("equals",6));
    b2$3283=(!(1).equals((2)));
    $$$c2310.check(b2$3283,$$$cl2309.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$3285=(!b2$3283);
    var setB3$3285=function(b3$3286){return b3$3285=b3$3286;};
    $$$c2310.check((!b3$3285),$$$cl2309.String("not",3));
    b1$3281=(true&&false);
    $$$c2310.check((!b1$3281),$$$cl2309.String("and",3));
    b2$3283=(b1$3281&&true);
    $$$c2310.check((!b2$3283),$$$cl2309.String("and",3));
    b3$3285=(true&&true);
    $$$c2310.check(b3$3285,$$$cl2309.String("and",3));
    b1$3281=(true||false);
    $$$c2310.check(b1$3281,$$$cl2309.String("or",2));
    b2$3283=(false||b1$3281);
    $$$c2310.check(b2$3283,$$$cl2309.String("or",2));
    b3$3285=(false||false);
    $$$c2310.check((!b3$3285),$$$cl2309.String("or",2));
};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-139:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$3287=$$$cl2309.String("str1",4).compare($$$cl2309.String("str2",4));
    $$$c2310.check(c1$3287.equals($$$cl2309.getSmaller()),$$$cl2309.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$3288=$$$cl2309.String("str2",4).compare($$$cl2309.String("str1",4));
    $$$c2310.check(c2$3288.equals($$$cl2309.getLarger()),$$$cl2309.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$3289=$$$cl2309.String("str1",4).compare($$$cl2309.String("str1",4));
    $$$c2310.check(c3$3289.equals($$$cl2309.getEqual()),$$$cl2309.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$3290=$$$cl2309.String("",0).compare($$$cl2309.String("",0));
    $$$c2310.check(c4$3290.equals($$$cl2309.getEqual()),$$$cl2309.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$3291=$$$cl2309.String("str1",4).compare($$$cl2309.String("",0));
    $$$c2310.check(c5$3291.equals($$$cl2309.getLarger()),$$$cl2309.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$3292=$$$cl2309.String("",0).compare($$$cl2309.String("str2",4));
    $$$c2310.check(c6$3292.equals($$$cl2309.getSmaller()),$$$cl2309.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$3293=$$$cl2309.String("str1",4).compare($$$cl2309.String("str2",4)).equals($$$cl2309.getSmaller());
    var setB1$3293=function(b1$3294){return b1$3293=b1$3294;};
    $$$c2310.check(b1$3293,$$$cl2309.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$3295=$$$cl2309.String("str1",4).compare($$$cl2309.String("str2",4)).equals($$$cl2309.getLarger());
    var setB2$3295=function(b2$3296){return b2$3295=b2$3296;};
    $$$c2310.check((!b2$3295),$$$cl2309.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$3297=($$$cl2309.String("str1",4).compare($$$cl2309.String("str2",4))!==$$$cl2309.getLarger());
    var setB3$3297=function(b3$3298){return b3$3297=b3$3298;};
    $$$c2310.check(b3$3297,$$$cl2309.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$3299=($$$cl2309.String("str1",4).compare($$$cl2309.String("str2",4))!==$$$cl2309.getSmaller());
    var setB4$3299=function(b4$3300){return b4$3299=b4$3300;};
    $$$c2310.check((!b4$3299),$$$cl2309.String("large as",8));
    b1$3293=$$$cl2309.String("str1",4).compare($$$cl2309.String("str1",4)).equals($$$cl2309.getSmaller());
    $$$c2310.check((!b1$3293),$$$cl2309.String("smaller",7));
    b2$3295=$$$cl2309.String("str1",4).compare($$$cl2309.String("str1",4)).equals($$$cl2309.getLarger());
    $$$c2310.check((!b2$3295),$$$cl2309.String("larger",6));
    b3$3297=($$$cl2309.String("str1",4).compare($$$cl2309.String("str1",4))!==$$$cl2309.getLarger());
    $$$c2310.check(b3$3297,$$$cl2309.String("small as",8));
    b4$3299=($$$cl2309.String("str1",4).compare($$$cl2309.String("str1",4))!==$$$cl2309.getSmaller());
    $$$c2310.check(b4$3299,$$$cl2309.String("large as",8));
};

//MethodDefinition testOtherOperators at operators.ceylon (141:0-153:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (142:4-142:42)
    var entry$3301=$$$cl2309.Entry((47),$$$cl2309.String("hi there",8),{Key:{t:$$$cl2309.Integer},Item:{t:$$$cl2309.String}});
    $$$c2310.check(entry$3301.key.equals((47)),$$$cl2309.String("entry key",9));
    $$$c2310.check(entry$3301.item.equals($$$cl2309.String("hi there",8)),$$$cl2309.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (145:4-145:30)
    var entry2$3302=$$$cl2309.Entry(true,entry$3301,{Key:{t:$$$cl2309.true$3128},Item:{t:$$$cl2309.Entry,a:{Key:{t:$$$cl2309.Integer},Item:{t:$$$cl2309.String}}}});
    $$$c2310.check(entry2$3302.key.equals(true),$$$cl2309.String("entry key",9));
    $$$c2310.check(entry2$3302.item.equals($$$cl2309.Entry((47),$$$cl2309.String("hi there",8),{Key:{t:$$$cl2309.Integer},Item:{t:$$$cl2309.String}})),$$$cl2309.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (149:4-149:41)
    var s1$3303=(opt$3304=(true?$$$cl2309.String("ok",2):null),opt$3304!==null?opt$3304:$$$cl2309.String("noo",3));
    var opt$3304;
    $$$c2310.check(s1$3303.equals($$$cl2309.String("ok",2)),$$$cl2309.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (151:4-151:47)
    var s2$3305=(opt$3306=(false?$$$cl2309.String("what?",5):null),opt$3306!==null?opt$3306:$$$cl2309.String("great",5));
    var opt$3306;
    $$$c2310.check(s2$3305.equals($$$cl2309.String("great",5)),$$$cl2309.String("then/else 2",11));
};

//MethodDefinition testCollectionOperators at operators.ceylon (155:0-167:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (156:4-156:33)
    var seq1$3307=$$$cl2309.Tuple($$$cl2309.String("one",3),$$$cl2309.Tuple($$$cl2309.String("two",3),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (157:4-157:23)
    var s1$3308=seq1$3307.get((0));
    $$$c2310.check(s1$3308.equals($$$cl2309.String("one",3)),$$$cl2309.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (159:4-159:28)
    var s2$3309=seq1$3307.get((2));
    $$$c2310.check((!$$$cl2309.exists(s2$3309)),$$$cl2309.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (161:4-161:29)
    var s3$3310=seq1$3307.get((-(1)));
    $$$c2310.check((!$$$cl2309.exists(s3$3310)),$$$cl2309.String("lookup",6));
};

//ClassDefinition NullsafeTest at operators.ceylon (169:0-174:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl2309.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl2309.Basic);
        (function($$nullsafeTest){
            
            //MethodDefinition f at operators.ceylon (170:4-170:33)
            $$nullsafeTest.f=function f(){
                var $$nullsafeTest=this;
                return (1);
            };
            //MethodDefinition f2 at operators.ceylon (171:4-173:4)
            $$nullsafeTest.f2=function f2(x$3311){
                var $$nullsafeTest=this;
                return x$3311();
            };
        })(NullsafeTest.$$.prototype);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (176:0-178:0)
function nullsafeTest(f$3312){
    return f$3312();
};

//MethodDefinition testNullsafeOperators at operators.ceylon (180:0-221:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (181:4-181:27)
    var seq$3313=$$$cl2309.Tuple($$$cl2309.String("hi",2),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (182:4-182:34)
    var s1$3314=(opt$3315=seq$3313.get((0)),opt$3315!==null?opt$3315:$$$cl2309.String("null",4));
    var opt$3315;
    $$$c2310.check(s1$3314.equals($$$cl2309.String("hi",2)),$$$cl2309.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (184:4-184:34)
    var s2$3316=(opt$3317=seq$3313.get((1)),opt$3317!==null?opt$3317:$$$cl2309.String("null",4));
    var opt$3317;
    $$$c2310.check(s2$3316.equals($$$cl2309.String("null",4)),$$$cl2309.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (187:4-187:21)
    var s3$3318=null;
    
    //AttributeDeclaration s4 at operators.ceylon (188:4-188:23)
    var s4$3319=$$$cl2309.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (189:4-189:42)
    var s5$3320=(opt$3321=(opt$3322=s3$3318,opt$3322!==null?opt$3322.uppercased:null),opt$3321!==null?opt$3321:$$$cl2309.String("null",4));
    var opt$3321,opt$3322;
    
    //AttributeDeclaration s6 at operators.ceylon (190:4-190:42)
    var s6$3323=(opt$3324=(opt$3325=s4$3319,opt$3325!==null?opt$3325.uppercased:null),opt$3324!==null?opt$3324:$$$cl2309.String("null",4));
    var opt$3324,opt$3325;
    $$$c2310.check(s5$3320.equals($$$cl2309.String("null",4)),$$$cl2309.String("nullsafe member 1",17));
    $$$c2310.check(s6$3323.equals($$$cl2309.String("TEST",4)),$$$cl2309.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (193:4-193:28)
    var obj$3326=null;
    
    //AttributeDeclaration i at operators.ceylon (194:4-194:25)
    var i$3327=(opt$3328=obj$3326,$$$cl2309.JsCallable(opt$3328,opt$3328!==null?opt$3328.f:null))();
    var opt$3328;
    $$$c2310.check((!$$$cl2309.exists(i$3327)),$$$cl2309.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (196:4-196:37)
    var f2$3329=(opt$3330=obj$3326,$$$cl2309.JsCallable(opt$3330,opt$3330!==null?opt$3330.f:null));
    var opt$3330;
    $$$c2310.check((!$$$cl2309.exists(nullsafeTest(f2$3329))),$$$cl2309.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (198:4-198:38)
    var f3$3331=(opt$3332=obj$3326,$$$cl2309.JsCallable(opt$3332,opt$3332!==null?opt$3332.f:null));
    var opt$3332;
    $$$c2310.check($$$cl2309.exists(f3$3331),$$$cl2309.String("nullsafe method ref 2",21));
    (opt$3333=obj$3326,$$$cl2309.JsCallable(opt$3333,opt$3333!==null?opt$3333.f:null))();
    var opt$3333;
    $$$c2310.check((!$$$cl2309.exists((opt$3334=obj$3326,$$$cl2309.JsCallable(opt$3334,opt$3334!==null?opt$3334.f:null))())),$$$cl2309.String("nullsafe simple call",20));
    var opt$3334;
    
    //MethodDefinition getNullsafe at operators.ceylon (202:4-202:46)
    function getNullsafe$3335(){
        return obj$3326;
    };
    
    //MethodDeclaration f4 at operators.ceylon (203:4-203:39)
    var f4$3336=function (){
        return (opt$3337=getNullsafe$3335(),$$$cl2309.JsCallable(opt$3337,opt$3337!==null?opt$3337.f:null))();
    };
    var opt$3337;
    
    //AttributeDeclaration result_f4 at operators.ceylon (204:4-204:29)
    var result_f4$3338=f4$3336();
    $$$c2310.check((!$$$cl2309.exists(result_f4$3338)),$$$cl2309.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (206:4-206:36)
    var i2$3339=(opt$3340=getNullsafe$3335(),$$$cl2309.JsCallable(opt$3340,opt$3340!==null?opt$3340.f:null))();
    var opt$3340;
    $$$c2310.check((!$$$cl2309.exists(i2$3339)),$$$cl2309.String("nullsafe invoke 3",17));
    $$$c2310.check((!$$$cl2309.exists(NullsafeTest().f2((opt$3341=getNullsafe$3335(),$$$cl2309.JsCallable(opt$3341,opt$3341!==null?opt$3341.f:null))))),$$$cl2309.String("nullsafe method ref 3",21));
    var opt$3341;
    
    //AttributeDeclaration obj2 at operators.ceylon (209:4-209:39)
    var obj2$3342=NullsafeTest();
    var i3$3343;
    if((i3$3343=(opt$3344=obj2$3342,$$$cl2309.JsCallable(opt$3344,opt$3344!==null?opt$3344.f:null))())!==null){
        $$$c2310.check(i3$3343.equals((1)),$$$cl2309.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2310.fail($$$cl2309.String("nullsafe invoke 4 (null)",24));
    }
    var opt$3344;
    
    //MethodDeclaration obj2_f at operators.ceylon (215:4-215:34)
    var obj2_f$3345=function (){
        return (opt$3346=obj2$3342,$$$cl2309.JsCallable(opt$3346,opt$3346!==null?opt$3346.f:null))();
    };
    var opt$3346;
    var i3$3347;
    if((i3$3347=obj2_f$3345())!==null){
        $$$c2310.check(i3$3347.equals((1)),$$$cl2309.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2310.fail($$$cl2309.String("nullsafe method ref 4 (null)",28));
    }
};

//MethodDefinition testIncDecOperators at operators.ceylon (223:0-298:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (224:4-224:27)
    var x0$3348=(1);
    var setX0$3348=function(x0$3349){return x0$3348=x0$3349;};
    
    //AttributeGetterDefinition x at operators.ceylon (225:4-225:27)
    var getX$3350=function(){
        return x0$3348;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (225:29-225:48)
    var setX$3350=function(x$3351){
        x0$3348=x$3351;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (227:4-227:27)
    var i1$3352=(1);
    var setI1$3352=function(i1$3353){return i1$3352=i1$3353;};
    
    //MethodDefinition f1 at operators.ceylon (228:4-235:4)
    function f1$3354(){
        
        //AttributeDeclaration i2 at operators.ceylon (229:8-229:25)
        var i2$3355=(i1$3352=i1$3352.successor,i1$3352);
        
        //AttributeDeclaration x2 at operators.ceylon (230:8-230:24)
        var x2$3356=(setX$3350(getX$3350().successor),getX$3350());
        $$$c2310.check(i1$3352.equals((2)),$$$cl2309.String("prefix increment 1",18));
        $$$c2310.check(i2$3355.equals((2)),$$$cl2309.String("prefix increment 2",18));
        $$$c2310.check(getX$3350().equals((2)),$$$cl2309.String("prefix increment 3",18));
        $$$c2310.check(x2$3356.equals((2)),$$$cl2309.String("prefix increment 4",18));
    };
    f1$3354();
    
    //ClassDefinition C1 at operators.ceylon (238:4-242:4)
    function C1$3357($$c1$3357){
        $init$C1$3357();
        if ($$c1$3357===undefined)$$c1$3357=new C1$3357.$$;
        
        //AttributeDeclaration i at operators.ceylon (239:8-239:37)
        $$c1$3357.i$3358_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (240:8-240:31)
        $$c1$3357.x0$3359_=(1);
        return $$c1$3357;
    }
    function $init$C1$3357(){
        if (C1$3357.$$===undefined){
            $$$cl2309.initTypeProto(C1$3357,'operators::testIncDecOperators.C1',$$$cl2309.Basic);
            (function($$c1$3357){
                
                //AttributeDeclaration i at operators.ceylon (239:8-239:37)
                $$$cl2309.defineAttr($$c1$3357,'i',function(){return this.i$3358_;},function(i$3360){return this.i$3358_=i$3360;});
                
                //AttributeDeclaration x0 at operators.ceylon (240:8-240:31)
                $$$cl2309.defineAttr($$c1$3357,'x0$3359',function(){return this.x0$3359_;},function(x0$3361){return this.x0$3359_=x0$3361;});
                
                //AttributeGetterDefinition x at operators.ceylon (241:8-241:38)
                $$$cl2309.defineAttr($$c1$3357,'x',function(){
                    var $$c1$3357=this;
                    return $$c1$3357.x0$3359;
                },function(x$3362){
                    var $$c1$3357=this;
                    $$c1$3357.x0$3359=x$3362;
                });
            })(C1$3357.$$.prototype);
        }
        return C1$3357;
    }
    $init$C1$3357();
    
    //AttributeDeclaration c1 at operators.ceylon (243:4-243:16)
    var c1$3363=C1$3357();
    
    //AttributeDeclaration i3 at operators.ceylon (244:4-244:27)
    var i3$3364=(0);
    var setI3$3364=function(i3$3365){return i3$3364=i3$3365;};
    
    //MethodDefinition f2 at operators.ceylon (245:4-248:4)
    function f2$3366(){
        (i3$3364=i3$3364.successor,i3$3364);
        return c1$3363;
    };
    
    //AttributeDeclaration i4 at operators.ceylon (249:4-249:25)
    var i4$3367=(tmp$3368=f2$3366(),tmp$3368.i=tmp$3368.i.successor,tmp$3368.i);
    var tmp$3368;
    
    //AttributeDeclaration x4 at operators.ceylon (250:4-250:25)
    var x4$3369=(tmp$3370=f2$3366(),tmp$3370.x=tmp$3370.x.successor,tmp$3370.x);
    var tmp$3370;
    $$$c2310.check(i4$3367.equals((2)),$$$cl2309.String("prefix increment 5",18));
    $$$c2310.check(c1$3363.i.equals((2)),$$$cl2309.String("prefix increment 6",18));
    $$$c2310.check(x4$3369.equals((2)),$$$cl2309.String("prefix increment 7",18));
    $$$c2310.check(c1$3363.x.equals((2)),$$$cl2309.String("prefix increment 8",18));
    $$$c2310.check(i3$3364.equals((2)),$$$cl2309.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (257:4-261:4)
    function f3$3371(){
        
        //AttributeDeclaration i2 at operators.ceylon (258:8-258:25)
        var i2$3372=(i1$3352=i1$3352.predecessor,i1$3352);
        $$$c2310.check(i1$3352.equals((1)),$$$cl2309.String("prefix decrement",16));
        $$$c2310.check(i2$3372.equals((1)),$$$cl2309.String("prefix decrement",16));
    };
    f3$3371();
    
    //AttributeDeclaration i5 at operators.ceylon (264:4-264:25)
    var i5$3373=(tmp$3374=f2$3366(),tmp$3374.i=tmp$3374.i.predecessor,tmp$3374.i);
    var tmp$3374;
    $$$c2310.check(i5$3373.equals((1)),$$$cl2309.String("prefix decrement",16));
    $$$c2310.check(c1$3363.i.equals((1)),$$$cl2309.String("prefix decrement",16));
    $$$c2310.check(i3$3364.equals((3)),$$$cl2309.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (269:4-276:4)
    function f4$3375(){
        
        //AttributeDeclaration i2 at operators.ceylon (270:8-270:25)
        var i2$3376=(oldi1$3377=i1$3352,i1$3352=oldi1$3377.successor,oldi1$3377);
        var oldi1$3377;
        
        //AttributeDeclaration x2 at operators.ceylon (271:8-271:24)
        var x2$3378=(oldx$3379=getX$3350(),setX$3350(oldx$3379.successor),oldx$3379);
        var oldx$3379;
        $$$c2310.check(i1$3352.equals((2)),$$$cl2309.String("postfix increment 1",19));
        $$$c2310.check(i2$3376.equals((1)),$$$cl2309.String("postfix increment 2",19));
        $$$c2310.check(getX$3350().equals((3)),$$$cl2309.String("postfix increment 3",19));
        $$$c2310.check(x2$3378.equals((2)),$$$cl2309.String("postfix increment 4",19));
    };
    f4$3375();
    
    //AttributeDeclaration i6 at operators.ceylon (279:4-279:25)
    var i6$3380=(tmp$3381=f2$3366(),oldi$3382=tmp$3381.i,tmp$3381.i=oldi$3382.successor,oldi$3382);
    var tmp$3381,oldi$3382;
    
    //AttributeDeclaration x6 at operators.ceylon (280:4-280:25)
    var x6$3383=(tmp$3384=f2$3366(),oldx$3385=tmp$3384.x,tmp$3384.x=oldx$3385.successor,oldx$3385);
    var tmp$3384,oldx$3385;
    $$$c2310.check(i6$3380.equals((1)),$$$cl2309.String("postfix increment 5",19));
    $$$c2310.check(c1$3363.i.equals((2)),$$$cl2309.String("postfix increment 6",19));
    $$$c2310.check(x6$3383.equals((2)),$$$cl2309.String("postfix increment 7 ",20));
    $$$c2310.check(c1$3363.x.equals((3)),$$$cl2309.String("postfix increment 8 ",20));
    $$$c2310.check(i3$3364.equals((5)),$$$cl2309.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (287:4-291:4)
    function f5$3386(){
        
        //AttributeDeclaration i2 at operators.ceylon (288:8-288:25)
        var i2$3387=(oldi1$3388=i1$3352,i1$3352=oldi1$3388.predecessor,oldi1$3388);
        var oldi1$3388;
        $$$c2310.check(i1$3352.equals((1)),$$$cl2309.String("postfix decrement",17));
        $$$c2310.check(i2$3387.equals((2)),$$$cl2309.String("postfix decrement",17));
    };
    f5$3386();
    
    //AttributeDeclaration i7 at operators.ceylon (294:4-294:25)
    var i7$3389=(tmp$3390=f2$3366(),oldi$3391=tmp$3390.i,tmp$3390.i=oldi$3391.predecessor,oldi$3391);
    var tmp$3390,oldi$3391;
    $$$c2310.check(i7$3389.equals((2)),$$$cl2309.String("postfix decrement",17));
    $$$c2310.check(c1$3363.i.equals((1)),$$$cl2309.String("postfix decrement",17));
    $$$c2310.check(i3$3364.equals((6)),$$$cl2309.String("postfix decrement",17));
};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (300:0-351:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (301:4-301:27)
    var i1$3392=(1);
    var setI1$3392=function(i1$3393){return i1$3392=i1$3393;};
    
    //AttributeDeclaration x0 at operators.ceylon (302:4-302:27)
    var x0$3394=(1);
    var setX0$3394=function(x0$3395){return x0$3394=x0$3395;};
    
    //AttributeGetterDefinition x at operators.ceylon (303:4-303:27)
    var getX$3396=function(){
        return x0$3394;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (303:29-303:46)
    var setX$3396=function(x$3397){
        x0$3394=x$3397;
    };
    (i1$3392=i1$3392.plus((10)),i1$3392);
    (setX$3396(getX$3396().plus((10))),getX$3396());
    $$$c2310.check(i1$3392.equals((11)),$$$cl2309.String("+= operator 1",13));
    $$$c2310.check(getX$3396().equals((11)),$$$cl2309.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (309:4-309:36)
    var i2$3398=(i1$3392=i1$3392.plus((-(5))),i1$3392);
    var setI2$3398=function(i2$3399){return i2$3398=i2$3399;};
    
    //AttributeDeclaration x2 at operators.ceylon (310:4-310:35)
    var x2$3400=(setX$3396(getX$3396().plus((-(5)))),getX$3396());
    var setX2$3400=function(x2$3401){return x2$3400=x2$3401;};
    $$$c2310.check(i2$3398.equals((6)),$$$cl2309.String("+= operator 3",13));
    $$$c2310.check(i1$3392.equals((6)),$$$cl2309.String("+= operator 4",13));
    $$$c2310.check(x2$3400.equals((6)),$$$cl2309.String("+= operator 5",13));
    $$$c2310.check(getX$3396().equals((6)),$$$cl2309.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (316:4-320:4)
    function C1$3402($$c1$3402){
        $init$C1$3402();
        if ($$c1$3402===undefined)$$c1$3402=new C1$3402.$$;
        
        //AttributeDeclaration i at operators.ceylon (317:8-317:37)
        $$c1$3402.i$3403_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (318:8-318:31)
        $$c1$3402.x0$3404_=(1);
        return $$c1$3402;
    }
    function $init$C1$3402(){
        if (C1$3402.$$===undefined){
            $$$cl2309.initTypeProto(C1$3402,'operators::testArithmeticAssignOperators.C1',$$$cl2309.Basic);
            (function($$c1$3402){
                
                //AttributeDeclaration i at operators.ceylon (317:8-317:37)
                $$$cl2309.defineAttr($$c1$3402,'i',function(){return this.i$3403_;},function(i$3405){return this.i$3403_=i$3405;});
                
                //AttributeDeclaration x0 at operators.ceylon (318:8-318:31)
                $$$cl2309.defineAttr($$c1$3402,'x0$3404',function(){return this.x0$3404_;},function(x0$3406){return this.x0$3404_=x0$3406;});
                
                //AttributeGetterDefinition x at operators.ceylon (319:8-319:38)
                $$$cl2309.defineAttr($$c1$3402,'x',function(){
                    var $$c1$3402=this;
                    return $$c1$3402.x0$3404;
                },function(x$3407){
                    var $$c1$3402=this;
                    $$c1$3402.x0$3404=x$3407;
                });
            })(C1$3402.$$.prototype);
        }
        return C1$3402;
    }
    $init$C1$3402();
    
    //AttributeDeclaration c1 at operators.ceylon (321:4-321:16)
    var c1$3408=C1$3402();
    
    //AttributeDeclaration i3 at operators.ceylon (322:4-322:27)
    var i3$3409=(0);
    var setI3$3409=function(i3$3410){return i3$3409=i3$3410;};
    
    //MethodDefinition f at operators.ceylon (323:4-326:4)
    function f$3411(){
        (i3$3409=i3$3409.successor,i3$3409);
        return c1$3408;
    };
    i2$3398=(tmp$3412=f$3411(),tmp$3412.i=tmp$3412.i.plus((11)),tmp$3412.i);
    var tmp$3412;
    x2$3400=(tmp$3413=f$3411(),tmp$3413.x=tmp$3413.x.plus((11)),tmp$3413.x);
    var tmp$3413;
    $$$c2310.check(i2$3398.equals((12)),$$$cl2309.String("+= operator 7",13));
    $$$c2310.check(c1$3408.i.equals((12)),$$$cl2309.String("+= operator 8",13));
    $$$c2310.check(x2$3400.equals((12)),$$$cl2309.String("+= operator 9",13));
    $$$c2310.check(c1$3408.x.equals((12)),$$$cl2309.String("+= operator 10",14));
    $$$c2310.check(i3$3409.equals((2)),$$$cl2309.String("+= operator 11",14));
    i2$3398=(i1$3392=i1$3392.minus((14)),i1$3392);
    $$$c2310.check(i1$3392.equals((-(8))),$$$cl2309.String("-= operator",11));
    $$$c2310.check(i2$3398.equals((-(8))),$$$cl2309.String("-= operator",11));
    i2$3398=(i1$3392=i1$3392.times((-(3))),i1$3392);
    $$$c2310.check(i1$3392.equals((24)),$$$cl2309.String("*= operator",11));
    $$$c2310.check(i2$3398.equals((24)),$$$cl2309.String("*= operator",11));
    i2$3398=(i1$3392=i1$3392.divided((5)),i1$3392);
    $$$c2310.check(i1$3392.equals((4)),$$$cl2309.String("/= operator",11));
    $$$c2310.check(i2$3398.equals((4)),$$$cl2309.String("/= operator",11));
    i2$3398=(i1$3392=i1$3392.remainder((3)),i1$3392);
    $$$c2310.check(i1$3392.equals((1)),$$$cl2309.String("%= operator",11));
    $$$c2310.check(i2$3398.equals((1)),$$$cl2309.String("%= operator",11));
};

//MethodDefinition testAssignmentOperator at operators.ceylon (353:0-383:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (354:4-354:27)
    var i1$3414=(1);
    var setI1$3414=function(i1$3415){return i1$3414=i1$3415;};
    
    //AttributeDeclaration i2 at operators.ceylon (355:4-355:27)
    var i2$3416=(2);
    var setI2$3416=function(i2$3417){return i2$3416=i2$3417;};
    
    //AttributeDeclaration i3 at operators.ceylon (356:4-356:27)
    var i3$3418=(3);
    var setI3$3418=function(i3$3419){return i3$3418=i3$3419;};
    $$$c2310.check((i1$3414=(i2$3416=i3$3418,i2$3416),i1$3414).equals((3)),$$$cl2309.String("assignment 1",12));
    $$$c2310.check(i1$3414.equals((3)),$$$cl2309.String("assignment 2",12));
    $$$c2310.check(i2$3416.equals((3)),$$$cl2309.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (361:4-361:28)
    var getX1$3420=function(){
        return i1$3414;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (361:30-361:51)
    var setX1$3420=function(x1$3421){
        i1$3414=x1$3421;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (362:4-362:28)
    var getX2$3422=function(){
        return i2$3416;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (362:30-362:51)
    var setX2$3422=function(x2$3423){
        i2$3416=x2$3423;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (363:4-363:28)
    var getX3$3424=function(){
        return i3$3418;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (363:30-363:51)
    var setX3$3424=function(x3$3425){
        i3$3418=x3$3425;
    };
    i1$3414=(1);
    i2$3416=(2);
    $$$c2310.check((setX1$3420((setX2$3422(getX3$3424()),getX2$3422())),getX1$3420()).equals((3)),$$$cl2309.String("assignment 4",12));
    $$$c2310.check(getX1$3420().equals((3)),$$$cl2309.String("assignment 5",12));
    $$$c2310.check(getX2$3422().equals((3)),$$$cl2309.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (370:4-374:4)
    function C$3426($$c$3426){
        $init$C$3426();
        if ($$c$3426===undefined)$$c$3426=new C$3426.$$;
        
        //AttributeDeclaration i at operators.ceylon (371:8-371:37)
        $$c$3426.i$3427_=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (372:8-372:31)
        $$c$3426.x0$3428_=(1);
        return $$c$3426;
    }
    function $init$C$3426(){
        if (C$3426.$$===undefined){
            $$$cl2309.initTypeProto(C$3426,'operators::testAssignmentOperator.C',$$$cl2309.Basic);
            (function($$c$3426){
                
                //AttributeDeclaration i at operators.ceylon (371:8-371:37)
                $$$cl2309.defineAttr($$c$3426,'i',function(){return this.i$3427_;},function(i$3429){return this.i$3427_=i$3429;});
                
                //AttributeDeclaration x0 at operators.ceylon (372:8-372:31)
                $$$cl2309.defineAttr($$c$3426,'x0$3428',function(){return this.x0$3428_;},function(x0$3430){return this.x0$3428_=x0$3430;});
                
                //AttributeGetterDefinition x at operators.ceylon (373:8-373:38)
                $$$cl2309.defineAttr($$c$3426,'x',function(){
                    var $$c$3426=this;
                    return $$c$3426.x0$3428;
                },function(x$3431){
                    var $$c$3426=this;
                    $$c$3426.x0$3428=x$3431;
                });
            })(C$3426.$$.prototype);
        }
        return C$3426;
    }
    $init$C$3426();
    
    //AttributeDeclaration o1 at operators.ceylon (375:4-375:14)
    var o1$3432=C$3426();
    
    //AttributeDeclaration o2 at operators.ceylon (376:4-376:14)
    var o2$3433=C$3426();
    $$$c2310.check((tmp$3434=o1$3432,tmp$3434.i=(tmp$3435=o2$3433,tmp$3435.i=(3),tmp$3435.i),tmp$3434.i).equals((3)),$$$cl2309.String("assignment 7",12));
    var tmp$3434,tmp$3435;
    $$$c2310.check(o1$3432.i.equals((3)),$$$cl2309.String("assignment 8",12));
    $$$c2310.check(o2$3433.i.equals((3)),$$$cl2309.String("assignment 9",12));
    $$$c2310.check((tmp$3436=o1$3432,tmp$3436.x=(tmp$3437=o2$3433,tmp$3437.x=(3),tmp$3437.x),tmp$3436.x).equals((3)),$$$cl2309.String("assignment 10",13));
    var tmp$3436,tmp$3437;
    $$$c2310.check(o1$3432.x.equals((3)),$$$cl2309.String("assignment 11",13));
    $$$c2310.check(o2$3433.x.equals((3)),$$$cl2309.String("assignment 12",13));
};

//MethodDefinition testSegments at operators.ceylon (385:0-412:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (386:4-386:97)
    var seq$3438=$$$cl2309.Tuple($$$cl2309.String("one",3),$$$cl2309.Tuple($$$cl2309.String("two",3),$$$cl2309.Tuple($$$cl2309.String("three",5),$$$cl2309.Tuple($$$cl2309.String("four",4),$$$cl2309.Tuple($$$cl2309.String("five",4),$$$cl2309.Tuple($$$cl2309.String("six",3),$$$cl2309.Tuple($$$cl2309.String("seven",5),$$$cl2309.Tuple($$$cl2309.String("eight",5),$$$cl2309.Tuple($$$cl2309.String("nine",4),$$$cl2309.Tuple($$$cl2309.String("ten",3),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}});
    $$$c2310.check(seq$3438.segment((1),(2)).equals($$$cl2309.Tuple($$$cl2309.String("two",3),$$$cl2309.Tuple($$$cl2309.String("three",5),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}})),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("seq[1:2] ",9),seq$3438.segment((1),(2)).string]).string);
    $$$c2310.check(seq$3438.segment((3),(5)).equals($$$cl2309.Tuple($$$cl2309.String("four",4),$$$cl2309.Tuple($$$cl2309.String("five",4),$$$cl2309.Tuple($$$cl2309.String("six",3),$$$cl2309.Tuple($$$cl2309.String("seven",5),$$$cl2309.Tuple($$$cl2309.String("eight",5),$$$cl2309.getEmpty(),{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}),{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Tuple,a:{Rest:{t:$$$cl2309.Empty},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}}},First:{t:$$$cl2309.String},Element:{t:$$$cl2309.String}})),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("seq[3:5] ",9),seq$3438.segment((3),(5)).string]).string);
    $$$c2310.check($$$cl2309.String("test",4).segment((1),(2)).equals($$$cl2309.String("es",2)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("test[1:2] ",10),$$$cl2309.String("test",4).segment((1),(2)).string]).string);
    $$$c2310.check($$$cl2309.String("hello",5).segment((2),(2)).equals($$$cl2309.String("ll",2)),$$$cl2309.StringBuilder().appendAll([$$$cl2309.String("hello[2:2] ",11),$$$cl2309.String("hello",5).segment((2),(2)).string]).string);
    
    //AttributeDeclaration s2 at operators.ceylon (391:4-391:18)
    var s2$3439=(function(){var tmpvar$3440=(3);
    if (tmpvar$3440>0){
    var tmpvar$3441=(0);
    var tmpvar$3442=tmpvar$3441;
    for (var i=1; i<tmpvar$3440; i++){tmpvar$3442=tmpvar$3442.successor;}
    return $$$cl2309.Range(tmpvar$3441,tmpvar$3442)
    }else return $$$cl2309.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (392:4-392:18)
    var s3$3443=(function(){var tmpvar$3444=(5);
    if (tmpvar$3444>0){
    var tmpvar$3445=(2);
    var tmpvar$3446=tmpvar$3445;
    for (var i=1; i<tmpvar$3444; i++){tmpvar$3446=tmpvar$3446.successor;}
    return $$$cl2309.Range(tmpvar$3445,tmpvar$3446)
    }else return $$$cl2309.getEmpty();}());
    $$$c2310.check(s2$3439.size.equals((3)),$$$cl2309.String("0:3 [1]",7));
    var x$3447;
    if((x$3447=s2$3439.get((0)))!==null){
        $$$c2310.check(x$3447.equals((0)),$$$cl2309.String("0:3 [2]",7));
    }else {
        $$$c2310.fail($$$cl2309.String("0:3 [2]",7));
    }
    var x$3448;
    if((x$3448=s2$3439.get((2)))!==null){
        $$$c2310.check(x$3448.equals((2)),$$$cl2309.String("0:3 [3]",7));
    }else {
        $$$c2310.fail($$$cl2309.String("0:3 [3]",7));
    }
    $$$c2310.check(s3$3443.size.equals((5)),$$$cl2309.String("2:5 [1]",7));
    var x$3449;
    if((x$3449=s3$3443.get((0)))!==null){
        $$$c2310.check(x$3449.equals((2)),$$$cl2309.String("2:5 [1]",7));
    }else {
        $$$c2310.fail($$$cl2309.String("2:5 [1]",7));
    }
    var x$3450;
    if((x$3450=s3$3443.get((2)))!==null){
        $$$c2310.check(x$3450.equals((4)),$$$cl2309.String("2:5 [2]",7));
    }else {
        $$$c2310.fail($$$cl2309.String("2:5 [2]",7));
    }
    var x$3451;
    if((x$3451=s3$3443.get((4)))!==null){
        $$$c2310.check(x$3451.equals((6)),$$$cl2309.String("2:5 [3]",7));
    }else {
        $$$c2310.fail($$$cl2309.String("2:5 [3]",7));
    }
    $$$c2310.check((!$$$cl2309.nonempty((function(){var tmpvar$3452=(0);
    if (tmpvar$3452>0){
    var tmpvar$3453=(1);
    var tmpvar$3454=tmpvar$3453;
    for (var i=1; i<tmpvar$3452; i++){tmpvar$3454=tmpvar$3454.successor;}
    return $$$cl2309.Range(tmpvar$3453,tmpvar$3454)
    }else return $$$cl2309.getEmpty();}()))),$$$cl2309.String("1:0 empty",9));
    $$$c2310.check((!$$$cl2309.nonempty((function(){var tmpvar$3455=(-(1));
    if (tmpvar$3455>0){
    var tmpvar$3456=(1);
    var tmpvar$3457=tmpvar$3456;
    for (var i=1; i<tmpvar$3455; i++){tmpvar$3457=tmpvar$3457.successor;}
    return $$$cl2309.Range(tmpvar$3456,tmpvar$3457)
    }else return $$$cl2309.getEmpty();}()))),$$$cl2309.String("1:-1 empty",10));
};

//MethodDefinition test at operators.ceylon (414:0-428:0)
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
    $$$c2310.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
