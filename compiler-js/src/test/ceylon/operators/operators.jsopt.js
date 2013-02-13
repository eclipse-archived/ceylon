(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}};
var $$$cl2243=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2244=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$3057=$$$cl2243.Tuple((1),$$$cl2243.Tuple((2),$$$cl2243.Tuple((3),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$3058=$$$cl2243.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$3059=$$$cl2243.String("hola",4).getIterator();
        var c$3060=$$$cl2243.getFinished();
        var next$c$3060=function(){return c$3060=it$3059.next();}
        next$c$3060();
        return function(){
            if(c$3060!==$$$cl2243.getFinished()){
                var c$3060$3061=c$3060;
                function getC$3060(){return c$3060$3061;}
                var tmpvar$3062=getC$3060();
                next$c$3060();
                return tmpvar$3062;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$3063=$$$cl2243.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$3064=$$$cl2243.String("hola",4).getIterator();
        var c$3065=$$$cl2243.getFinished();
        var next$c$3065=function(){return c$3065=it$3064.next();}
        next$c$3065();
        return function(){
            if(c$3065!==$$$cl2243.getFinished()){
                var c$3065$3066=c$3065;
                function getC$3065(){return c$3065$3066;}
                var tmpvar$3067=getC$3065();
                next$c$3065();
                return tmpvar$3067;
            }
            return $$$cl2243.getFinished();
        }
    },{Absent:{t:$$$cl2243.Anything},Element:{t:$$$cl2243.Character}}).getSequence();
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$3068=[(0)].reifyCeylonType({Absent:{t:$$$cl2243.Nothing},Element:{t:$$$cl2243.Integer}}).chain(seq$3057,{Absent:{t:$$$cl2243.Null},Element:{t:$$$cl2243.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$3069=[$$$cl2243.Character(65)].reifyCeylonType({Absent:{t:$$$cl2243.Nothing},Element:{t:$$$cl2243.Character}}).chain(lcomp$3058,{Absent:{t:$$$cl2243.Null},Element:{t:$$$cl2243.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$3070=$$$cl2243.Tuple((1),$$$cl2243.Tuple((2),$$$cl2243.Tuple((3),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$3071=$$$cl2243.Tuple((0),seq$3057,{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}}},First:{t:$$$cl2243.Integer},Element:{t:$$$cl2243.Integer}});
    $$$c2244.check($$$cl2243.className(seq$3057).startsWith($$$cl2243.String("ceylon.language::Tuple",22)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{1,2,3} is not a Tuple but a ",29),$$$cl2243.className(seq$3057).getString()]).getString());
    $$$c2244.check((!$$$cl2243.className(lcomp$3058).startsWith($$$cl2243.String("ceylon.language::Tuple",22))),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("lazy comprehension is a Tuple ",30),$$$cl2243.className(lcomp$3058).getString()]).getString());
    $$$c2244.check($$$cl2243.className(ecomp$3063).startsWith($$$cl2243.String("ceylon.language::ArraySequence",30)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("eager comprehension is not a Tuple but a ",41),$$$cl2243.className(ecomp$3063).getString()]).getString());
    $$$c2244.check((!$$$cl2243.className(s2$3068).startsWith($$$cl2243.String("ceylon.language::Tuple",22))),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{0,*seq} is a Tuple ",20),$$$cl2243.className(s2$3068).getString()]).getString());
    $$$c2244.check((!$$$cl2243.className(s3$3069).startsWith($$$cl2243.String("ceylon.language::Tuple",22))),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{x,*iter} is a Tuple ",21),$$$cl2243.className(s3$3069).getString()]).getString());
    $$$c2244.check($$$cl2243.className(t1$3070).startsWith($$$cl2243.String("ceylon.language::Tuple",22)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("[1,2,3] is not a Tuple but a ",29),$$$cl2243.className(t1$3070).getString()]).getString());
    $$$c2244.check($$$cl2243.className(t2$3071).startsWith($$$cl2243.String("ceylon.language::Tuple",22)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("[0,*seq] is not a Tuple but a ",30),$$$cl2243.className(t2$3071).getString()]).getString());
    $$$c2244.check(seq$3057.equals(t1$3070),$$$cl2243.String("{1,2,3} != [1,2,3]",18));
    $$$c2244.check((!$$$cl2243.className(t2$3071).equals($$$cl2243.className(s2$3068))),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{0,*seq} != [0,*seq] ",21),$$$cl2243.className(t2$3071).getString(),$$$cl2243.String(" vs",3),$$$cl2243.className(s2$3068).getString()]).getString());
    $$$c2244.check(seq$3057.getSize().equals((3)),$$$cl2243.String("seq.size!=3",11));
    $$$c2244.check(lcomp$3058.getSequence().getSize().equals((4)),$$$cl2243.String("lcomp.size!=4",13));
    $$$c2244.check(ecomp$3063.getSize().equals((4)),$$$cl2243.String("ecomp.size!=4",13));
    $$$c2244.check(s2$3068.getSize().equals((4)),$$$cl2243.String("s2.size!=4",10));
    $$$c2244.check(s3$3069.getSequence().getSize().equals((5)),$$$cl2243.String("s3.size!=5",10));
    $$$c2244.check(t1$3070.getSize().equals((3)),$$$cl2243.String("t1.size!=3",10));
    $$$c2244.check(t2$3071.getSize().equals((4)),$$$cl2243.String("t2.size!=4",10));
    $$$c2244.check((!$$$cl2243.className(lcomp$3058).startsWith($$$cl2243.String("ceylon.language::Tuple",22))),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{*comp} is not Tuple but ",25),$$$cl2243.className(lcomp$3058).getString()]).getString());
    $$$c2244.check($$$cl2243.className(ecomp$3063).startsWith($$$cl2243.String("ceylon.language::ArraySequence",30)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{*ecomp} is not Tuple but ",26),$$$cl2243.className(ecomp$3063).getString()]).getString());
    $$$c2244.check($$$cl2243.className(seq$3057).startsWith($$$cl2243.String("ceylon.language::Tuple",22)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("{*seq} is not Tuple but ",24),$$$cl2243.className(seq$3057).getString()]).getString());
};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$3072=(-(4));
    var setI1$3072=function(i1$3073){return i1$3072=i1$3073;};
    i1$3072=(-i1$3072);
    $$$c2244.check(i1$3072.equals((4)),$$$cl2243.String("negation",8));
    i1$3072=(+(-(987654)));
    $$$c2244.check(i1$3072.equals((-(987654))),$$$cl2243.String("positive",8));
    i1$3072=(+(0));
    $$$c2244.check(i1$3072.equals((0)),$$$cl2243.String("+0=0",4));
    i1$3072=(-(0));
    $$$c2244.check(i1$3072.equals((0)),$$$cl2243.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$3074=(123).plus((456));
    var setI2$3074=function(i2$3075){return i2$3074=i2$3075;};
    $$$c2244.check(i2$3074.equals((579)),$$$cl2243.String("addition",8));
    i1$3072=i2$3074.minus((16));
    $$$c2244.check(i1$3072.equals((563)),$$$cl2243.String("subtraction",11));
    i2$3074=(-i1$3072).plus(i2$3074).minus((1));
    $$$c2244.check(i2$3074.equals((15)),$$$cl2243.String("-i1+i2-1",8));
    i1$3072=(3).times((7));
    $$$c2244.check(i1$3072.equals((21)),$$$cl2243.String("multiplication",14));
    i2$3074=i1$3072.times((2));
    $$$c2244.check(i2$3074.equals((42)),$$$cl2243.String("multiplication",14));
    i2$3074=(17).divided((4));
    $$$c2244.check(i2$3074.equals((4)),$$$cl2243.String("integer division",16));
    i1$3072=i2$3074.times((516)).divided((-i1$3072));
    $$$c2244.check(i1$3072.equals((-(98))),$$$cl2243.String("i2*516/-i1",10));
    i1$3072=(15).remainder((4));
    $$$c2244.check(i1$3072.equals((3)),$$$cl2243.String("modulo",6));
    i2$3074=(312).remainder((12));
    $$$c2244.check(i2$3074.equals((0)),$$$cl2243.String("modulo",6));
    i1$3072=(2).power((10));
    $$$c2244.check(i1$3072.equals((1024)),$$$cl2243.String("power",5));
    i2$3074=(10).power((6));
    $$$c2244.check(i2$3074.equals((1000000)),$$$cl2243.String("power",5));
};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$3076=$$$cl2243.Float(4.2).getNegativeValue();
    var setF1$3076=function(f1$3077){return f1$3076=f1$3077;};
    f1$3076=f1$3076.getNegativeValue();
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(4.2)),$$$cl2243.String("negation",8));
    f1$3076=(+$$$cl2243.Float(987654.9925567).getNegativeValue());
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(987654.9925567).getNegativeValue()),$$$cl2243.String("positive",8));
    f1$3076=(+$$$cl2243.Float(0.0));
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(0.0)),$$$cl2243.String("+0.0=0.0",8));
    f1$3076=$$$cl2243.Float(0.0).getNegativeValue();
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(0.0)),$$$cl2243.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$3078=$$$cl2243.Float(3.14159265).plus($$$cl2243.Float(456.0));
    var setF2$3078=function(f2$3079){return f2$3078=f2$3079;};
    $$$c2244.check(f2$3078.equals($$$cl2243.Float(459.14159265)),$$$cl2243.String("addition",8));
    f1$3076=f2$3078.minus($$$cl2243.Float(0.0016));
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(459.13999265)),$$$cl2243.String("subtraction",11));
    f2$3078=f1$3076.getNegativeValue().plus(f2$3078).minus($$$cl2243.Float(1.2));
    $$$c2244.check(f2$3078.equals($$$cl2243.Float(1.1984000000000037).getNegativeValue()),$$$cl2243.String("-f1+f2-1.2",10));
    f1$3076=$$$cl2243.Float(3.0).times($$$cl2243.Float(0.79));
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(2.37)),$$$cl2243.String("multiplication",14));
    f2$3078=f1$3076.times($$$cl2243.Float(2.0e13));
    $$$c2244.check(f2$3078.equals($$$cl2243.Float(47400000000000.0)),$$$cl2243.String("multiplication",14));
    f2$3078=$$$cl2243.Float(17.1).divided($$$cl2243.Float(4.0E-18));
    $$$c2244.check(f2$3078.equals($$$cl2243.Float(4275000000000000000.0)),$$$cl2243.String("division",8));
    f1$3076=f2$3078.times($$$cl2243.Float(51.6e2)).divided(f1$3076.getNegativeValue());
    $$$c2244.check(f2$3078.equals($$$cl2243.Float(4275000000000000000.0)),$$$cl2243.String("f2*51.6e2/-f1",13));
    f1$3076=$$$cl2243.Float(150.0).power($$$cl2243.Float(0.5));
    $$$c2244.check(f1$3076.equals($$$cl2243.Float(12.24744871391589)),$$$cl2243.String("power",5));
};

//ClassDefinition OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl2243.initTypeProto(OpTest1,'operators::OpTest1',$$$cl2243.Basic);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$3080=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$3081=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$3082=(o1$3080===o2$3081);
    var setB1$3082=function(b1$3083){return b1$3082=b1$3083;};
    $$$c2244.check((!b1$3082),$$$cl2243.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$3084=(o1$3080===o1$3080);
    var setB2$3084=function(b2$3085){return b2$3084=b2$3085;};
    $$$c2244.check(b2$3084,$$$cl2243.String("identity",8));
    b1$3082=o1$3080.equals(o2$3081);
    $$$c2244.check((!b1$3082),$$$cl2243.String("equals",6));
    b2$3084=o1$3080.equals(o1$3080);
    $$$c2244.check(b2$3084,$$$cl2243.String("equals",6));
    b1$3082=(1).equals((2));
    $$$c2244.check((!b1$3082),$$$cl2243.String("equals",6));
    b2$3084=(!(1).equals((2)));
    $$$c2244.check(b2$3084,$$$cl2243.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$3086=(!b2$3084);
    var setB3$3086=function(b3$3087){return b3$3086=b3$3087;};
    $$$c2244.check((!b3$3086),$$$cl2243.String("not",3));
    b1$3082=(true&&false);
    $$$c2244.check((!b1$3082),$$$cl2243.String("and",3));
    b2$3084=(b1$3082&&true);
    $$$c2244.check((!b2$3084),$$$cl2243.String("and",3));
    b3$3086=(true&&true);
    $$$c2244.check(b3$3086,$$$cl2243.String("and",3));
    b1$3082=(true||false);
    $$$c2244.check(b1$3082,$$$cl2243.String("or",2));
    b2$3084=(false||b1$3082);
    $$$c2244.check(b2$3084,$$$cl2243.String("or",2));
    b3$3086=(false||false);
    $$$c2244.check((!b3$3086),$$$cl2243.String("or",2));
};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-139:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$3088=$$$cl2243.String("str1",4).compare($$$cl2243.String("str2",4));
    $$$c2244.check(c1$3088.equals($$$cl2243.getSmaller()),$$$cl2243.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$3089=$$$cl2243.String("str2",4).compare($$$cl2243.String("str1",4));
    $$$c2244.check(c2$3089.equals($$$cl2243.getLarger()),$$$cl2243.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$3090=$$$cl2243.String("str1",4).compare($$$cl2243.String("str1",4));
    $$$c2244.check(c3$3090.equals($$$cl2243.getEqual()),$$$cl2243.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$3091=$$$cl2243.String("",0).compare($$$cl2243.String("",0));
    $$$c2244.check(c4$3091.equals($$$cl2243.getEqual()),$$$cl2243.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$3092=$$$cl2243.String("str1",4).compare($$$cl2243.String("",0));
    $$$c2244.check(c5$3092.equals($$$cl2243.getLarger()),$$$cl2243.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$3093=$$$cl2243.String("",0).compare($$$cl2243.String("str2",4));
    $$$c2244.check(c6$3093.equals($$$cl2243.getSmaller()),$$$cl2243.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$3094=$$$cl2243.String("str1",4).compare($$$cl2243.String("str2",4)).equals($$$cl2243.getSmaller());
    var setB1$3094=function(b1$3095){return b1$3094=b1$3095;};
    $$$c2244.check(b1$3094,$$$cl2243.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$3096=$$$cl2243.String("str1",4).compare($$$cl2243.String("str2",4)).equals($$$cl2243.getLarger());
    var setB2$3096=function(b2$3097){return b2$3096=b2$3097;};
    $$$c2244.check((!b2$3096),$$$cl2243.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$3098=($$$cl2243.String("str1",4).compare($$$cl2243.String("str2",4))!==$$$cl2243.getLarger());
    var setB3$3098=function(b3$3099){return b3$3098=b3$3099;};
    $$$c2244.check(b3$3098,$$$cl2243.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$3100=($$$cl2243.String("str1",4).compare($$$cl2243.String("str2",4))!==$$$cl2243.getSmaller());
    var setB4$3100=function(b4$3101){return b4$3100=b4$3101;};
    $$$c2244.check((!b4$3100),$$$cl2243.String("large as",8));
    b1$3094=$$$cl2243.String("str1",4).compare($$$cl2243.String("str1",4)).equals($$$cl2243.getSmaller());
    $$$c2244.check((!b1$3094),$$$cl2243.String("smaller",7));
    b2$3096=$$$cl2243.String("str1",4).compare($$$cl2243.String("str1",4)).equals($$$cl2243.getLarger());
    $$$c2244.check((!b2$3096),$$$cl2243.String("larger",6));
    b3$3098=($$$cl2243.String("str1",4).compare($$$cl2243.String("str1",4))!==$$$cl2243.getLarger());
    $$$c2244.check(b3$3098,$$$cl2243.String("small as",8));
    b4$3100=($$$cl2243.String("str1",4).compare($$$cl2243.String("str1",4))!==$$$cl2243.getSmaller());
    $$$c2244.check(b4$3100,$$$cl2243.String("large as",8));
};

//MethodDefinition testOtherOperators at operators.ceylon (141:0-153:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (142:4-142:42)
    var entry$3102=$$$cl2243.Entry((47),$$$cl2243.String("hi there",8),{Key:{t:$$$cl2243.Integer},Item:{t:$$$cl2243.String}});
    $$$c2244.check(entry$3102.getKey().equals((47)),$$$cl2243.String("entry key",9));
    $$$c2244.check(entry$3102.getItem().equals($$$cl2243.String("hi there",8)),$$$cl2243.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (145:4-145:30)
    var entry2$3103=$$$cl2243.Entry(true,entry$3102,{Key:{t:$$$cl2243.$true},Item:{t:$$$cl2243.Entry,a:{Key:{t:$$$cl2243.Integer},Item:{t:$$$cl2243.String}}}});
    $$$c2244.check(entry2$3103.getKey().equals(true),$$$cl2243.String("entry key",9));
    $$$c2244.check(entry2$3103.getItem().equals($$$cl2243.Entry((47),$$$cl2243.String("hi there",8),{Key:{t:$$$cl2243.Integer},Item:{t:$$$cl2243.String}})),$$$cl2243.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (149:4-149:41)
    var s1$3104=(opt$3105=(true?$$$cl2243.String("ok",2):null),opt$3105!==null?opt$3105:$$$cl2243.String("noo",3));
    var opt$3105;
    $$$c2244.check(s1$3104.equals($$$cl2243.String("ok",2)),$$$cl2243.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (151:4-151:47)
    var s2$3106=(opt$3107=(false?$$$cl2243.String("what?",5):null),opt$3107!==null?opt$3107:$$$cl2243.String("great",5));
    var opt$3107;
    $$$c2244.check(s2$3106.equals($$$cl2243.String("great",5)),$$$cl2243.String("then/else 2",11));
};

//MethodDefinition testCollectionOperators at operators.ceylon (155:0-167:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (156:4-156:33)
    var seq1$3108=$$$cl2243.Tuple($$$cl2243.String("one",3),$$$cl2243.Tuple($$$cl2243.String("two",3),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (157:4-157:23)
    var s1$3109=seq1$3108.get((0));
    $$$c2244.check(s1$3109.equals($$$cl2243.String("one",3)),$$$cl2243.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (159:4-159:28)
    var s2$3110=seq1$3108.get((2));
    $$$c2244.check((!$$$cl2243.exists(s2$3110)),$$$cl2243.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (161:4-161:29)
    var s3$3111=seq1$3108.get((-(1)));
    $$$c2244.check((!$$$cl2243.exists(s3$3111)),$$$cl2243.String("lookup",6));
};

//ClassDefinition NullsafeTest at operators.ceylon (169:0-174:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl2243.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl2243.Basic);
        (function($$nullsafeTest){
            
            //MethodDefinition f at operators.ceylon (170:4-170:33)
            $$nullsafeTest.f=function f(){
                var $$nullsafeTest=this;
                return (1);
            };
            //MethodDefinition f2 at operators.ceylon (171:4-173:4)
            $$nullsafeTest.f2=function f2(x$3112){
                var $$nullsafeTest=this;
                return x$3112();
            };
        })(NullsafeTest.$$.prototype);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (176:0-178:0)
function nullsafeTest(f$3113){
    return f$3113();
};

//MethodDefinition testNullsafeOperators at operators.ceylon (180:0-221:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (181:4-181:27)
    var seq$3114=$$$cl2243.Tuple($$$cl2243.String("hi",2),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (182:4-182:34)
    var s1$3115=(opt$3116=seq$3114.get((0)),opt$3116!==null?opt$3116:$$$cl2243.String("null",4));
    var opt$3116;
    $$$c2244.check(s1$3115.equals($$$cl2243.String("hi",2)),$$$cl2243.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (184:4-184:34)
    var s2$3117=(opt$3118=seq$3114.get((1)),opt$3118!==null?opt$3118:$$$cl2243.String("null",4));
    var opt$3118;
    $$$c2244.check(s2$3117.equals($$$cl2243.String("null",4)),$$$cl2243.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (187:4-187:21)
    var s3$3119=null;
    
    //AttributeDeclaration s4 at operators.ceylon (188:4-188:23)
    var s4$3120=$$$cl2243.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (189:4-189:42)
    var s5$3121=(opt$3122=(opt$3123=s3$3119,opt$3123!==null?opt$3123.getUppercased():null),opt$3122!==null?opt$3122:$$$cl2243.String("null",4));
    var opt$3122,opt$3123;
    
    //AttributeDeclaration s6 at operators.ceylon (190:4-190:42)
    var s6$3124=(opt$3125=(opt$3126=s4$3120,opt$3126!==null?opt$3126.getUppercased():null),opt$3125!==null?opt$3125:$$$cl2243.String("null",4));
    var opt$3125,opt$3126;
    $$$c2244.check(s5$3121.equals($$$cl2243.String("null",4)),$$$cl2243.String("nullsafe member 1",17));
    $$$c2244.check(s6$3124.equals($$$cl2243.String("TEST",4)),$$$cl2243.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (193:4-193:28)
    var obj$3127=null;
    
    //AttributeDeclaration i at operators.ceylon (194:4-194:25)
    var i$3128=(opt$3129=obj$3127,$$$cl2243.JsCallable(opt$3129,opt$3129!==null?opt$3129.f:null))();
    var opt$3129;
    $$$c2244.check((!$$$cl2243.exists(i$3128)),$$$cl2243.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (196:4-196:37)
    var f2$3130=(opt$3131=obj$3127,$$$cl2243.JsCallable(opt$3131,opt$3131!==null?opt$3131.f:null));
    var opt$3131;
    $$$c2244.check((!$$$cl2243.exists(nullsafeTest(f2$3130))),$$$cl2243.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (198:4-198:38)
    var f3$3132=(opt$3133=obj$3127,$$$cl2243.JsCallable(opt$3133,opt$3133!==null?opt$3133.f:null));
    var opt$3133;
    $$$c2244.check($$$cl2243.exists(f3$3132),$$$cl2243.String("nullsafe method ref 2",21));
    (opt$3134=obj$3127,$$$cl2243.JsCallable(opt$3134,opt$3134!==null?opt$3134.f:null))();
    var opt$3134;
    $$$c2244.check((!$$$cl2243.exists((opt$3135=obj$3127,$$$cl2243.JsCallable(opt$3135,opt$3135!==null?opt$3135.f:null))())),$$$cl2243.String("nullsafe simple call",20));
    var opt$3135;
    
    //MethodDefinition getNullsafe at operators.ceylon (202:4-202:46)
    function getNullsafe$3136(){
        return obj$3127;
    };
    
    //MethodDeclaration f4 at operators.ceylon (203:4-203:39)
    var f4$3137=function (){
        return (opt$3138=getNullsafe$3136(),$$$cl2243.JsCallable(opt$3138,opt$3138!==null?opt$3138.f:null))();
    };
    var opt$3138;
    
    //AttributeDeclaration result_f4 at operators.ceylon (204:4-204:29)
    var result_f4$3139=f4$3137();
    $$$c2244.check((!$$$cl2243.exists(result_f4$3139)),$$$cl2243.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (206:4-206:36)
    var i2$3140=(opt$3141=getNullsafe$3136(),$$$cl2243.JsCallable(opt$3141,opt$3141!==null?opt$3141.f:null))();
    var opt$3141;
    $$$c2244.check((!$$$cl2243.exists(i2$3140)),$$$cl2243.String("nullsafe invoke 3",17));
    $$$c2244.check((!$$$cl2243.exists(NullsafeTest().f2((opt$3142=getNullsafe$3136(),$$$cl2243.JsCallable(opt$3142,opt$3142!==null?opt$3142.f:null))))),$$$cl2243.String("nullsafe method ref 3",21));
    var opt$3142;
    
    //AttributeDeclaration obj2 at operators.ceylon (209:4-209:39)
    var obj2$3143=NullsafeTest();
    var i3$3144;
    if((i3$3144=(opt$3145=obj2$3143,$$$cl2243.JsCallable(opt$3145,opt$3145!==null?opt$3145.f:null))())!==null){
        $$$c2244.check(i3$3144.equals((1)),$$$cl2243.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2244.fail($$$cl2243.String("nullsafe invoke 4 (null)",24));
    }
    var opt$3145;
    
    //MethodDeclaration obj2_f at operators.ceylon (215:4-215:34)
    var obj2_f$3146=function (){
        return (opt$3147=obj2$3143,$$$cl2243.JsCallable(opt$3147,opt$3147!==null?opt$3147.f:null))();
    };
    var opt$3147;
    var i3$3148;
    if((i3$3148=obj2_f$3146())!==null){
        $$$c2244.check(i3$3148.equals((1)),$$$cl2243.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2244.fail($$$cl2243.String("nullsafe method ref 4 (null)",28));
    }
};

//MethodDefinition testIncDecOperators at operators.ceylon (223:0-298:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (224:4-224:27)
    var x0$3149=(1);
    var setX0$3149=function(x0$3150){return x0$3149=x0$3150;};
    
    //AttributeGetterDefinition x at operators.ceylon (225:4-225:27)
    var getX$3151=function(){
        return x0$3149;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (225:29-225:48)
    var setX$3151=function(x$3152){
        x0$3149=x$3152;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (227:4-227:27)
    var i1$3153=(1);
    var setI1$3153=function(i1$3154){return i1$3153=i1$3154;};
    
    //MethodDefinition f1 at operators.ceylon (228:4-235:4)
    function f1$3155(){
        
        //AttributeDeclaration i2 at operators.ceylon (229:8-229:25)
        var i2$3156=(i1$3153=i1$3153.getSuccessor());
        
        //AttributeDeclaration x2 at operators.ceylon (230:8-230:24)
        var x2$3157=(setX$3151(getX$3151().getSuccessor()),getX$3151());
        $$$c2244.check(i1$3153.equals((2)),$$$cl2243.String("prefix increment 1",18));
        $$$c2244.check(i2$3156.equals((2)),$$$cl2243.String("prefix increment 2",18));
        $$$c2244.check(getX$3151().equals((2)),$$$cl2243.String("prefix increment 3",18));
        $$$c2244.check(x2$3157.equals((2)),$$$cl2243.String("prefix increment 4",18));
    };
    f1$3155();
    
    //ClassDefinition C1 at operators.ceylon (238:4-242:4)
    function C1$3158($$c1$3158){
        $init$C1$3158();
        if ($$c1$3158===undefined)$$c1$3158=new C1$3158.$$;
        
        //AttributeDeclaration i at operators.ceylon (239:8-239:37)
        $$c1$3158.i$3159=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (240:8-240:31)
        $$c1$3158.x0$3160=(1);
        return $$c1$3158;
    }
    function $init$C1$3158(){
        if (C1$3158.$$===undefined){
            $$$cl2243.initTypeProto(C1$3158,'operators::testIncDecOperators.C1',$$$cl2243.Basic);
            (function($$c1$3158){
                
                //AttributeDeclaration i at operators.ceylon (239:8-239:37)
                $$c1$3158.getI=function getI(){
                    return this.i$3159;
                };
                $$c1$3158.setI=function setI(i$3161){
                    return this.i$3159=i$3161;
                };
                
                //AttributeDeclaration x0 at operators.ceylon (240:8-240:31)
                $$c1$3158.getX0$3160=function getX0$3160(){
                    return this.x0$3160;
                };
                $$c1$3158.setX0$3160=function setX0$3160(x0$3162){
                    return this.x0$3160=x0$3162;
                };
                
                //AttributeGetterDefinition x at operators.ceylon (241:8-241:38)
                $$c1$3158.getX=function getX(){
                    var $$c1$3158=this;
                    return $$c1$3158.getX0$3160();
                };
                //AttributeSetterDefinition x at operators.ceylon (241:40-241:59)
                $$c1$3158.setX=function setX(x$3163){
                    var $$c1$3158=this;
                    $$c1$3158.setX0$3160(x$3163);
                };
            })(C1$3158.$$.prototype);
        }
        return C1$3158;
    }
    $init$C1$3158();
    
    //AttributeDeclaration c1 at operators.ceylon (243:4-243:16)
    var c1$3164=C1$3158();
    
    //AttributeDeclaration i3 at operators.ceylon (244:4-244:27)
    var i3$3165=(0);
    var setI3$3165=function(i3$3166){return i3$3165=i3$3166;};
    
    //MethodDefinition f2 at operators.ceylon (245:4-248:4)
    function f2$3167(){
        (i3$3165=i3$3165.getSuccessor());
        return c1$3164;
    };
    
    //AttributeDeclaration i4 at operators.ceylon (249:4-249:25)
    var i4$3168=(tmp$3169=f2$3167(),tmp$3169.setI(tmp$3169.getI().getSuccessor()));
    var tmp$3169;
    
    //AttributeDeclaration x4 at operators.ceylon (250:4-250:25)
    var x4$3170=(tmp$3171=f2$3167(),tmp$3171.setX(tmp$3171.getX().getSuccessor()),tmp$3171.getX());
    var tmp$3171;
    $$$c2244.check(i4$3168.equals((2)),$$$cl2243.String("prefix increment 5",18));
    $$$c2244.check(c1$3164.getI().equals((2)),$$$cl2243.String("prefix increment 6",18));
    $$$c2244.check(x4$3170.equals((2)),$$$cl2243.String("prefix increment 7",18));
    $$$c2244.check(c1$3164.getX().equals((2)),$$$cl2243.String("prefix increment 8",18));
    $$$c2244.check(i3$3165.equals((2)),$$$cl2243.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (257:4-261:4)
    function f3$3172(){
        
        //AttributeDeclaration i2 at operators.ceylon (258:8-258:25)
        var i2$3173=(i1$3153=i1$3153.getPredecessor());
        $$$c2244.check(i1$3153.equals((1)),$$$cl2243.String("prefix decrement",16));
        $$$c2244.check(i2$3173.equals((1)),$$$cl2243.String("prefix decrement",16));
    };
    f3$3172();
    
    //AttributeDeclaration i5 at operators.ceylon (264:4-264:25)
    var i5$3174=(tmp$3175=f2$3167(),tmp$3175.setI(tmp$3175.getI().getPredecessor()));
    var tmp$3175;
    $$$c2244.check(i5$3174.equals((1)),$$$cl2243.String("prefix decrement",16));
    $$$c2244.check(c1$3164.getI().equals((1)),$$$cl2243.String("prefix decrement",16));
    $$$c2244.check(i3$3165.equals((3)),$$$cl2243.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (269:4-276:4)
    function f4$3176(){
        
        //AttributeDeclaration i2 at operators.ceylon (270:8-270:25)
        var i2$3177=(oldi1$3178=i1$3153,i1$3153=oldi1$3178.getSuccessor(),oldi1$3178);
        var oldi1$3178;
        
        //AttributeDeclaration x2 at operators.ceylon (271:8-271:24)
        var x2$3179=(oldx$3180=getX$3151(),setX$3151(oldx$3180.getSuccessor()),oldx$3180);
        var oldx$3180;
        $$$c2244.check(i1$3153.equals((2)),$$$cl2243.String("postfix increment 1",19));
        $$$c2244.check(i2$3177.equals((1)),$$$cl2243.String("postfix increment 2",19));
        $$$c2244.check(getX$3151().equals((3)),$$$cl2243.String("postfix increment 3",19));
        $$$c2244.check(x2$3179.equals((2)),$$$cl2243.String("postfix increment 4",19));
    };
    f4$3176();
    
    //AttributeDeclaration i6 at operators.ceylon (279:4-279:25)
    var i6$3181=(tmp$3182=f2$3167(),oldi$3183=tmp$3182.getI(),tmp$3182.setI(oldi$3183.getSuccessor()),oldi$3183);
    var tmp$3182,oldi$3183;
    
    //AttributeDeclaration x6 at operators.ceylon (280:4-280:25)
    var x6$3184=(tmp$3185=f2$3167(),oldx$3186=tmp$3185.getX(),tmp$3185.setX(oldx$3186.getSuccessor()),oldx$3186);
    var tmp$3185,oldx$3186;
    $$$c2244.check(i6$3181.equals((1)),$$$cl2243.String("postfix increment 5",19));
    $$$c2244.check(c1$3164.getI().equals((2)),$$$cl2243.String("postfix increment 6",19));
    $$$c2244.check(x6$3184.equals((2)),$$$cl2243.String("postfix increment 7 ",20));
    $$$c2244.check(c1$3164.getX().equals((3)),$$$cl2243.String("postfix increment 8 ",20));
    $$$c2244.check(i3$3165.equals((5)),$$$cl2243.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (287:4-291:4)
    function f5$3187(){
        
        //AttributeDeclaration i2 at operators.ceylon (288:8-288:25)
        var i2$3188=(oldi1$3189=i1$3153,i1$3153=oldi1$3189.getPredecessor(),oldi1$3189);
        var oldi1$3189;
        $$$c2244.check(i1$3153.equals((1)),$$$cl2243.String("postfix decrement",17));
        $$$c2244.check(i2$3188.equals((2)),$$$cl2243.String("postfix decrement",17));
    };
    f5$3187();
    
    //AttributeDeclaration i7 at operators.ceylon (294:4-294:25)
    var i7$3190=(tmp$3191=f2$3167(),oldi$3192=tmp$3191.getI(),tmp$3191.setI(oldi$3192.getPredecessor()),oldi$3192);
    var tmp$3191,oldi$3192;
    $$$c2244.check(i7$3190.equals((2)),$$$cl2243.String("postfix decrement",17));
    $$$c2244.check(c1$3164.getI().equals((1)),$$$cl2243.String("postfix decrement",17));
    $$$c2244.check(i3$3165.equals((6)),$$$cl2243.String("postfix decrement",17));
};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (300:0-351:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (301:4-301:27)
    var i1$3193=(1);
    var setI1$3193=function(i1$3194){return i1$3193=i1$3194;};
    
    //AttributeDeclaration x0 at operators.ceylon (302:4-302:27)
    var x0$3195=(1);
    var setX0$3195=function(x0$3196){return x0$3195=x0$3196;};
    
    //AttributeGetterDefinition x at operators.ceylon (303:4-303:27)
    var getX$3197=function(){
        return x0$3195;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (303:29-303:46)
    var setX$3197=function(x$3198){
        x0$3195=x$3198;
    };
    (i1$3193=i1$3193.plus((10)));
    (setX$3197(getX$3197().plus((10))),getX$3197());
    $$$c2244.check(i1$3193.equals((11)),$$$cl2243.String("+= operator 1",13));
    $$$c2244.check(getX$3197().equals((11)),$$$cl2243.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (309:4-309:36)
    var i2$3199=(i1$3193=i1$3193.plus((-(5))));
    var setI2$3199=function(i2$3200){return i2$3199=i2$3200;};
    
    //AttributeDeclaration x2 at operators.ceylon (310:4-310:35)
    var x2$3201=(setX$3197(getX$3197().plus((-(5)))),getX$3197());
    var setX2$3201=function(x2$3202){return x2$3201=x2$3202;};
    $$$c2244.check(i2$3199.equals((6)),$$$cl2243.String("+= operator 3",13));
    $$$c2244.check(i1$3193.equals((6)),$$$cl2243.String("+= operator 4",13));
    $$$c2244.check(x2$3201.equals((6)),$$$cl2243.String("+= operator 5",13));
    $$$c2244.check(getX$3197().equals((6)),$$$cl2243.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (316:4-320:4)
    function C1$3203($$c1$3203){
        $init$C1$3203();
        if ($$c1$3203===undefined)$$c1$3203=new C1$3203.$$;
        
        //AttributeDeclaration i at operators.ceylon (317:8-317:37)
        $$c1$3203.i$3204=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (318:8-318:31)
        $$c1$3203.x0$3205=(1);
        return $$c1$3203;
    }
    function $init$C1$3203(){
        if (C1$3203.$$===undefined){
            $$$cl2243.initTypeProto(C1$3203,'operators::testArithmeticAssignOperators.C1',$$$cl2243.Basic);
            (function($$c1$3203){
                
                //AttributeDeclaration i at operators.ceylon (317:8-317:37)
                $$c1$3203.getI=function getI(){
                    return this.i$3204;
                };
                $$c1$3203.setI=function setI(i$3206){
                    return this.i$3204=i$3206;
                };
                
                //AttributeDeclaration x0 at operators.ceylon (318:8-318:31)
                $$c1$3203.getX0$3205=function getX0$3205(){
                    return this.x0$3205;
                };
                $$c1$3203.setX0$3205=function setX0$3205(x0$3207){
                    return this.x0$3205=x0$3207;
                };
                
                //AttributeGetterDefinition x at operators.ceylon (319:8-319:38)
                $$c1$3203.getX=function getX(){
                    var $$c1$3203=this;
                    return $$c1$3203.getX0$3205();
                };
                //AttributeSetterDefinition x at operators.ceylon (319:40-319:57)
                $$c1$3203.setX=function setX(x$3208){
                    var $$c1$3203=this;
                    $$c1$3203.setX0$3205(x$3208);
                };
            })(C1$3203.$$.prototype);
        }
        return C1$3203;
    }
    $init$C1$3203();
    
    //AttributeDeclaration c1 at operators.ceylon (321:4-321:16)
    var c1$3209=C1$3203();
    
    //AttributeDeclaration i3 at operators.ceylon (322:4-322:27)
    var i3$3210=(0);
    var setI3$3210=function(i3$3211){return i3$3210=i3$3211;};
    
    //MethodDefinition f at operators.ceylon (323:4-326:4)
    function f$3212(){
        (i3$3210=i3$3210.getSuccessor());
        return c1$3209;
    };
    i2$3199=(tmp$3213=f$3212(),tmp$3213.setI(tmp$3213.getI().plus((11))));
    var tmp$3213;
    x2$3201=(tmp$3214=f$3212(),tmp$3214.setX(tmp$3214.getX().plus((11))),tmp$3214.getX());
    var tmp$3214;
    $$$c2244.check(i2$3199.equals((12)),$$$cl2243.String("+= operator 7",13));
    $$$c2244.check(c1$3209.getI().equals((12)),$$$cl2243.String("+= operator 8",13));
    $$$c2244.check(x2$3201.equals((12)),$$$cl2243.String("+= operator 9",13));
    $$$c2244.check(c1$3209.getX().equals((12)),$$$cl2243.String("+= operator 10",14));
    $$$c2244.check(i3$3210.equals((2)),$$$cl2243.String("+= operator 11",14));
    i2$3199=(i1$3193=i1$3193.minus((14)));
    $$$c2244.check(i1$3193.equals((-(8))),$$$cl2243.String("-= operator",11));
    $$$c2244.check(i2$3199.equals((-(8))),$$$cl2243.String("-= operator",11));
    i2$3199=(i1$3193=i1$3193.times((-(3))));
    $$$c2244.check(i1$3193.equals((24)),$$$cl2243.String("*= operator",11));
    $$$c2244.check(i2$3199.equals((24)),$$$cl2243.String("*= operator",11));
    i2$3199=(i1$3193=i1$3193.divided((5)));
    $$$c2244.check(i1$3193.equals((4)),$$$cl2243.String("/= operator",11));
    $$$c2244.check(i2$3199.equals((4)),$$$cl2243.String("/= operator",11));
    i2$3199=(i1$3193=i1$3193.remainder((3)));
    $$$c2244.check(i1$3193.equals((1)),$$$cl2243.String("%= operator",11));
    $$$c2244.check(i2$3199.equals((1)),$$$cl2243.String("%= operator",11));
};

//MethodDefinition testAssignmentOperator at operators.ceylon (353:0-383:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (354:4-354:27)
    var i1$3215=(1);
    var setI1$3215=function(i1$3216){return i1$3215=i1$3216;};
    
    //AttributeDeclaration i2 at operators.ceylon (355:4-355:27)
    var i2$3217=(2);
    var setI2$3217=function(i2$3218){return i2$3217=i2$3218;};
    
    //AttributeDeclaration i3 at operators.ceylon (356:4-356:27)
    var i3$3219=(3);
    var setI3$3219=function(i3$3220){return i3$3219=i3$3220;};
    $$$c2244.check((i1$3215=(i2$3217=i3$3219)).equals((3)),$$$cl2243.String("assignment 1",12));
    $$$c2244.check(i1$3215.equals((3)),$$$cl2243.String("assignment 2",12));
    $$$c2244.check(i2$3217.equals((3)),$$$cl2243.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (361:4-361:28)
    var getX1$3221=function(){
        return i1$3215;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (361:30-361:51)
    var setX1$3221=function(x1$3222){
        i1$3215=x1$3222;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (362:4-362:28)
    var getX2$3223=function(){
        return i2$3217;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (362:30-362:51)
    var setX2$3223=function(x2$3224){
        i2$3217=x2$3224;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (363:4-363:28)
    var getX3$3225=function(){
        return i3$3219;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (363:30-363:51)
    var setX3$3225=function(x3$3226){
        i3$3219=x3$3226;
    };
    i1$3215=(1);
    i2$3217=(2);
    $$$c2244.check((setX1$3221((setX2$3223(getX3$3225()),getX2$3223())),getX1$3221()).equals((3)),$$$cl2243.String("assignment 4",12));
    $$$c2244.check(getX1$3221().equals((3)),$$$cl2243.String("assignment 5",12));
    $$$c2244.check(getX2$3223().equals((3)),$$$cl2243.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (370:4-374:4)
    function C$3227($$c$3227){
        $init$C$3227();
        if ($$c$3227===undefined)$$c$3227=new C$3227.$$;
        
        //AttributeDeclaration i at operators.ceylon (371:8-371:37)
        $$c$3227.i$3228=(1);
        
        //AttributeDeclaration x0 at operators.ceylon (372:8-372:31)
        $$c$3227.x0$3229=(1);
        return $$c$3227;
    }
    function $init$C$3227(){
        if (C$3227.$$===undefined){
            $$$cl2243.initTypeProto(C$3227,'operators::testAssignmentOperator.C',$$$cl2243.Basic);
            (function($$c$3227){
                
                //AttributeDeclaration i at operators.ceylon (371:8-371:37)
                $$c$3227.getI=function getI(){
                    return this.i$3228;
                };
                $$c$3227.setI=function setI(i$3230){
                    return this.i$3228=i$3230;
                };
                
                //AttributeDeclaration x0 at operators.ceylon (372:8-372:31)
                $$c$3227.getX0$3229=function getX0$3229(){
                    return this.x0$3229;
                };
                $$c$3227.setX0$3229=function setX0$3229(x0$3231){
                    return this.x0$3229=x0$3231;
                };
                
                //AttributeGetterDefinition x at operators.ceylon (373:8-373:38)
                $$c$3227.getX=function getX(){
                    var $$c$3227=this;
                    return $$c$3227.getX0$3229();
                };
                //AttributeSetterDefinition x at operators.ceylon (373:40-373:57)
                $$c$3227.setX=function setX(x$3232){
                    var $$c$3227=this;
                    $$c$3227.setX0$3229(x$3232);
                };
            })(C$3227.$$.prototype);
        }
        return C$3227;
    }
    $init$C$3227();
    
    //AttributeDeclaration o1 at operators.ceylon (375:4-375:14)
    var o1$3233=C$3227();
    
    //AttributeDeclaration o2 at operators.ceylon (376:4-376:14)
    var o2$3234=C$3227();
    $$$c2244.check((o1$3233.setI((o2$3234.setI((3))))).equals((3)),$$$cl2243.String("assignment 7",12));
    $$$c2244.check(o1$3233.getI().equals((3)),$$$cl2243.String("assignment 8",12));
    $$$c2244.check(o2$3234.getI().equals((3)),$$$cl2243.String("assignment 9",12));
    $$$c2244.check((tmp$3235=o1$3233,tmp$3235.setX((tmp$3236=o2$3234,tmp$3236.setX((3)),tmp$3236.getX())),tmp$3235.getX()).equals((3)),$$$cl2243.String("assignment 10",13));
    var tmp$3235,tmp$3236;
    $$$c2244.check(o1$3233.getX().equals((3)),$$$cl2243.String("assignment 11",13));
    $$$c2244.check(o2$3234.getX().equals((3)),$$$cl2243.String("assignment 12",13));
};

//MethodDefinition testSegments at operators.ceylon (385:0-412:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (386:4-386:97)
    var seq$3237=$$$cl2243.Tuple($$$cl2243.String("one",3),$$$cl2243.Tuple($$$cl2243.String("two",3),$$$cl2243.Tuple($$$cl2243.String("three",5),$$$cl2243.Tuple($$$cl2243.String("four",4),$$$cl2243.Tuple($$$cl2243.String("five",4),$$$cl2243.Tuple($$$cl2243.String("six",3),$$$cl2243.Tuple($$$cl2243.String("seven",5),$$$cl2243.Tuple($$$cl2243.String("eight",5),$$$cl2243.Tuple($$$cl2243.String("nine",4),$$$cl2243.Tuple($$$cl2243.String("ten",3),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}});
    $$$c2244.check(seq$3237.segment((1),(2)).equals($$$cl2243.Tuple($$$cl2243.String("two",3),$$$cl2243.Tuple($$$cl2243.String("three",5),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}})),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("seq[1:2] ",9),seq$3237.segment((1),(2)).getString()]).getString());
    $$$c2244.check(seq$3237.segment((3),(5)).equals($$$cl2243.Tuple($$$cl2243.String("four",4),$$$cl2243.Tuple($$$cl2243.String("five",4),$$$cl2243.Tuple($$$cl2243.String("six",3),$$$cl2243.Tuple($$$cl2243.String("seven",5),$$$cl2243.Tuple($$$cl2243.String("eight",5),$$$cl2243.getEmpty(),{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}),{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Tuple,a:{Rest:{t:$$$cl2243.Empty},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}}},First:{t:$$$cl2243.String},Element:{t:$$$cl2243.String}})),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("seq[3:5] ",9),seq$3237.segment((3),(5)).getString()]).getString());
    $$$c2244.check($$$cl2243.String("test",4).segment((1),(2)).equals($$$cl2243.String("es",2)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("test[1:2] ",10),$$$cl2243.String("test",4).segment((1),(2)).getString()]).getString());
    $$$c2244.check($$$cl2243.String("hello",5).segment((2),(2)).equals($$$cl2243.String("ll",2)),$$$cl2243.StringBuilder().appendAll([$$$cl2243.String("hello[2:2] ",11),$$$cl2243.String("hello",5).segment((2),(2)).getString()]).getString());
    
    //AttributeDeclaration s2 at operators.ceylon (391:4-391:18)
    var s2$3238=(function(){var tmpvar$3239=(3);
    if (tmpvar$3239>0){
    var tmpvar$3240=(0);
    var tmpvar$3241=tmpvar$3240;
    for (var i=1; i<tmpvar$3239; i++){tmpvar$3241=tmpvar$3241.getSuccessor();}
    return $$$cl2243.Range(tmpvar$3240,tmpvar$3241)
    }else return $$$cl2243.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (392:4-392:18)
    var s3$3242=(function(){var tmpvar$3243=(5);
    if (tmpvar$3243>0){
    var tmpvar$3244=(2);
    var tmpvar$3245=tmpvar$3244;
    for (var i=1; i<tmpvar$3243; i++){tmpvar$3245=tmpvar$3245.getSuccessor();}
    return $$$cl2243.Range(tmpvar$3244,tmpvar$3245)
    }else return $$$cl2243.getEmpty();}());
    $$$c2244.check(s2$3238.getSize().equals((3)),$$$cl2243.String("0:3 [1]",7));
    var x$3246;
    if((x$3246=s2$3238.get((0)))!==null){
        $$$c2244.check(x$3246.equals((0)),$$$cl2243.String("0:3 [2]",7));
    }else {
        $$$c2244.fail($$$cl2243.String("0:3 [2]",7));
    }
    var x$3247;
    if((x$3247=s2$3238.get((2)))!==null){
        $$$c2244.check(x$3247.equals((2)),$$$cl2243.String("0:3 [3]",7));
    }else {
        $$$c2244.fail($$$cl2243.String("0:3 [3]",7));
    }
    $$$c2244.check(s3$3242.getSize().equals((5)),$$$cl2243.String("2:5 [1]",7));
    var x$3248;
    if((x$3248=s3$3242.get((0)))!==null){
        $$$c2244.check(x$3248.equals((2)),$$$cl2243.String("2:5 [1]",7));
    }else {
        $$$c2244.fail($$$cl2243.String("2:5 [1]",7));
    }
    var x$3249;
    if((x$3249=s3$3242.get((2)))!==null){
        $$$c2244.check(x$3249.equals((4)),$$$cl2243.String("2:5 [2]",7));
    }else {
        $$$c2244.fail($$$cl2243.String("2:5 [2]",7));
    }
    var x$3250;
    if((x$3250=s3$3242.get((4)))!==null){
        $$$c2244.check(x$3250.equals((6)),$$$cl2243.String("2:5 [3]",7));
    }else {
        $$$c2244.fail($$$cl2243.String("2:5 [3]",7));
    }
    $$$c2244.check((!$$$cl2243.nonempty((function(){var tmpvar$3251=(0);
    if (tmpvar$3251>0){
    var tmpvar$3252=(1);
    var tmpvar$3253=tmpvar$3252;
    for (var i=1; i<tmpvar$3251; i++){tmpvar$3253=tmpvar$3253.getSuccessor();}
    return $$$cl2243.Range(tmpvar$3252,tmpvar$3253)
    }else return $$$cl2243.getEmpty();}()))),$$$cl2243.String("1:0 empty",9));
    $$$c2244.check((!$$$cl2243.nonempty((function(){var tmpvar$3254=(-(1));
    if (tmpvar$3254>0){
    var tmpvar$3255=(1);
    var tmpvar$3256=tmpvar$3255;
    for (var i=1; i<tmpvar$3254; i++){tmpvar$3256=tmpvar$3256.getSuccessor();}
    return $$$cl2243.Range(tmpvar$3255,tmpvar$3256)
    }else return $$$cl2243.getEmpty();}()))),$$$cl2243.String("1:-1 empty",10));
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
    $$$c2244.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
