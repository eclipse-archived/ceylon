(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$889=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$890=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$891=$$$cl1.String("hola",4).iterator;
        var c$892=$$$cl1.getFinished();
        var next$c$892=function(){return c$892=it$891.next();}
        next$c$892();
        return function(){
            if(c$892!==$$$cl1.getFinished()){
                var c$892$893=c$892;
                var tmpvar$894=c$892$893;
                next$c$892();
                return tmpvar$894;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$895=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$896=$$$cl1.String("hola",4).iterator;
        var c$897=$$$cl1.getFinished();
        var next$c$897=function(){return c$897=it$896.next();}
        next$c$897();
        return function(){
            if(c$897!==$$$cl1.getFinished()){
                var c$897$898=c$897;
                var tmpvar$899=c$897$898;
                next$c$897();
                return tmpvar$899;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}).sequence;
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$900=[(0)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).chain(seq$889,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$901=[$$$cl1.Character(65)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}).chain(lcomp$890,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$902=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$903=$$$cl1.Tuple((0),seq$889,{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.className(seq$889).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{1,2,3} is not a Tuple but a ",29),$$$cl1.className(seq$889).string]).string);
    $$$c2.check((!$$$cl1.className(lcomp$890).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("lazy comprehension is a Tuple ",30),$$$cl1.className(lcomp$890).string]).string);
    $$$c2.check($$$cl1.className(ecomp$895).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("eager comprehension is not a Tuple but a ",41),$$$cl1.className(ecomp$895).string]).string);
    $$$c2.check((!$$$cl1.className(s2$900).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} is a Tuple ",20),$$$cl1.className(s2$900).string]).string);
    $$$c2.check((!$$$cl1.className(s3$901).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{x,*iter} is a Tuple ",21),$$$cl1.className(s3$901).string]).string);
    $$$c2.check($$$cl1.className(t1$902).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[1,2,3] is not a Tuple but a ",29),$$$cl1.className(t1$902).string]).string);
    $$$c2.check($$$cl1.className(t2$903).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[0,*seq] is not a Tuple but a ",30),$$$cl1.className(t2$903).string]).string);
    $$$c2.check(seq$889.equals(t1$902),$$$cl1.String("{1,2,3} != [1,2,3]",18));
    $$$c2.check((!$$$cl1.className(t2$903).equals($$$cl1.className(s2$900))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} != [0,*seq] ",21),$$$cl1.className(t2$903).string,$$$cl1.String(" vs",3),$$$cl1.className(s2$900).string]).string);
    $$$c2.check(seq$889.size.equals((3)),$$$cl1.String("seq.size!=3",11));
    $$$c2.check(lcomp$890.sequence.size.equals((4)),$$$cl1.String("lcomp.size!=4",13));
    $$$c2.check(ecomp$895.size.equals((4)),$$$cl1.String("ecomp.size!=4",13));
    $$$c2.check(s2$900.size.equals((4)),$$$cl1.String("s2.size!=4",10));
    $$$c2.check(s3$901.sequence.size.equals((5)),$$$cl1.String("s3.size!=5",10));
    $$$c2.check(t1$902.size.equals((3)),$$$cl1.String("t1.size!=3",10));
    $$$c2.check(t2$903.size.equals((4)),$$$cl1.String("t2.size!=4",10));
    $$$c2.check((!$$$cl1.className(lcomp$890).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*comp} is not Tuple but ",25),$$$cl1.className(lcomp$890).string]).string);
    $$$c2.check($$$cl1.className(ecomp$895).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*ecomp} is not Tuple but ",26),$$$cl1.className(ecomp$895).string]).string);
    $$$c2.check($$$cl1.className(seq$889).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*seq} is not Tuple but ",24),$$$cl1.className(seq$889).string]).string);
};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$904=(-(4));
    var setI1$904=function(i1$905){return i1$904=i1$905;};
    i1$904=(-i1$904);
    $$$c2.check(i1$904.equals((4)),$$$cl1.String("negation",8));
    i1$904=(+(-(987654)));
    $$$c2.check(i1$904.equals((-(987654))),$$$cl1.String("positive",8));
    i1$904=(+(0));
    $$$c2.check(i1$904.equals((0)),$$$cl1.String("+0=0",4));
    i1$904=(-(0));
    $$$c2.check(i1$904.equals((0)),$$$cl1.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$906=(123).plus((456));
    var setI2$906=function(i2$907){return i2$906=i2$907;};
    $$$c2.check(i2$906.equals((579)),$$$cl1.String("addition",8));
    i1$904=i2$906.minus((16));
    $$$c2.check(i1$904.equals((563)),$$$cl1.String("subtraction",11));
    i2$906=(-i1$904).plus(i2$906).minus((1));
    $$$c2.check(i2$906.equals((15)),$$$cl1.String("-i1+i2-1",8));
    i1$904=(3).times((7));
    $$$c2.check(i1$904.equals((21)),$$$cl1.String("multiplication",14));
    i2$906=i1$904.times((2));
    $$$c2.check(i2$906.equals((42)),$$$cl1.String("multiplication",14));
    i2$906=(17).divided((4));
    $$$c2.check(i2$906.equals((4)),$$$cl1.String("integer division",16));
    i1$904=i2$906.times((516)).divided((-i1$904));
    $$$c2.check(i1$904.equals((-(98))),$$$cl1.String("i2*516/-i1",10));
    i1$904=(15).remainder((4));
    $$$c2.check(i1$904.equals((3)),$$$cl1.String("modulo",6));
    i2$906=(312).remainder((12));
    $$$c2.check(i2$906.equals((0)),$$$cl1.String("modulo",6));
    i1$904=(2).power((10));
    $$$c2.check(i1$904.equals((1024)),$$$cl1.String("power",5));
    i2$906=(10).power((6));
    $$$c2.check(i2$906.equals((1000000)),$$$cl1.String("power",5));
};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$908=$$$cl1.Float(4.2).negativeValue;
    var setF1$908=function(f1$909){return f1$908=f1$909;};
    f1$908=f1$908.negativeValue;
    $$$c2.check(f1$908.equals($$$cl1.Float(4.2)),$$$cl1.String("negation",8));
    f1$908=(+$$$cl1.Float(987654.9925567).negativeValue);
    $$$c2.check(f1$908.equals($$$cl1.Float(987654.9925567).negativeValue),$$$cl1.String("positive",8));
    f1$908=(+$$$cl1.Float(0.0));
    $$$c2.check(f1$908.equals($$$cl1.Float(0.0)),$$$cl1.String("+0.0=0.0",8));
    f1$908=$$$cl1.Float(0.0).negativeValue;
    $$$c2.check(f1$908.equals($$$cl1.Float(0.0)),$$$cl1.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$910=$$$cl1.Float(3.14159265).plus($$$cl1.Float(456.0));
    var setF2$910=function(f2$911){return f2$910=f2$911;};
    $$$c2.check(f2$910.equals($$$cl1.Float(459.14159265)),$$$cl1.String("addition",8));
    f1$908=f2$910.minus($$$cl1.Float(0.0016));
    $$$c2.check(f1$908.equals($$$cl1.Float(459.13999265)),$$$cl1.String("subtraction",11));
    f2$910=f1$908.negativeValue.plus(f2$910).minus($$$cl1.Float(1.2));
    $$$c2.check(f2$910.equals($$$cl1.Float(1.1984000000000037).negativeValue),$$$cl1.String("-f1+f2-1.2",10));
    f1$908=$$$cl1.Float(3.0).times($$$cl1.Float(0.79));
    $$$c2.check(f1$908.equals($$$cl1.Float(2.37)),$$$cl1.String("multiplication",14));
    f2$910=f1$908.times($$$cl1.Float(2.0e13));
    $$$c2.check(f2$910.equals($$$cl1.Float(47400000000000.0)),$$$cl1.String("multiplication",14));
    f2$910=$$$cl1.Float(17.1).divided($$$cl1.Float(4.0E-18));
    $$$c2.check(f2$910.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("division",8));
    f1$908=f2$910.times($$$cl1.Float(51.6e2)).divided(f1$908.negativeValue);
    $$$c2.check(f2$910.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("f2*51.6e2/-f1",13));
    f1$908=$$$cl1.Float(150.0).power($$$cl1.Float(0.5));
    $$$c2.check(f1$908.equals($$$cl1.Float(12.24744871391589)),$$$cl1.String("power",5));
};

//ClassDefinition OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl1.initTypeProto(OpTest1,'operators::OpTest1',$$$cl1.Basic);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$912=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$913=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$914=(o1$912===o2$913);
    var setB1$914=function(b1$915){return b1$914=b1$915;};
    $$$c2.check((!b1$914),$$$cl1.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$916=(o1$912===o1$912);
    var setB2$916=function(b2$917){return b2$916=b2$917;};
    $$$c2.check(b2$916,$$$cl1.String("identity",8));
    b1$914=o1$912.equals(o2$913);
    $$$c2.check((!b1$914),$$$cl1.String("equals",6));
    b2$916=o1$912.equals(o1$912);
    $$$c2.check(b2$916,$$$cl1.String("equals",6));
    b1$914=(1).equals((2));
    $$$c2.check((!b1$914),$$$cl1.String("equals",6));
    b2$916=(!(1).equals((2)));
    $$$c2.check(b2$916,$$$cl1.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$918=(!b2$916);
    var setB3$918=function(b3$919){return b3$918=b3$919;};
    $$$c2.check((!b3$918),$$$cl1.String("not",3));
    b1$914=(true&&false);
    $$$c2.check((!b1$914),$$$cl1.String("and",3));
    b2$916=(b1$914&&true);
    $$$c2.check((!b2$916),$$$cl1.String("and",3));
    b3$918=(true&&true);
    $$$c2.check(b3$918,$$$cl1.String("and",3));
    b1$914=(true||false);
    $$$c2.check(b1$914,$$$cl1.String("or",2));
    b2$916=(false||b1$914);
    $$$c2.check(b2$916,$$$cl1.String("or",2));
    b3$918=(false||false);
    $$$c2.check((!b3$918),$$$cl1.String("or",2));
};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-139:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$920=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4));
    $$$c2.check(c1$920.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$921=$$$cl1.String("str2",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c2$921.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$922=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c3$922.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$923=$$$cl1.String("",0).compare($$$cl1.String("",0));
    $$$c2.check(c4$923.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$924=$$$cl1.String("str1",4).compare($$$cl1.String("",0));
    $$$c2.check(c5$924.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$925=$$$cl1.String("",0).compare($$$cl1.String("str2",4));
    $$$c2.check(c6$925.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$926=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getSmaller());
    var setB1$926=function(b1$927){return b1$926=b1$927;};
    $$$c2.check(b1$926,$$$cl1.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$928=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getLarger());
    var setB2$928=function(b2$929){return b2$928=b2$929;};
    $$$c2.check((!b2$928),$$$cl1.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$930=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getLarger());
    var setB3$930=function(b3$931){return b3$930=b3$931;};
    $$$c2.check(b3$930,$$$cl1.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$932=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getSmaller());
    var setB4$932=function(b4$933){return b4$932=b4$933;};
    $$$c2.check((!b4$932),$$$cl1.String("large as",8));
    b1$926=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getSmaller());
    $$$c2.check((!b1$926),$$$cl1.String("smaller",7));
    b2$928=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getLarger());
    $$$c2.check((!b2$928),$$$cl1.String("larger",6));
    b3$930=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getLarger());
    $$$c2.check(b3$930,$$$cl1.String("small as",8));
    b4$932=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getSmaller());
    $$$c2.check(b4$932,$$$cl1.String("large as",8));
};

//MethodDefinition testOtherOperators at operators.ceylon (141:0-153:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (142:4-142:42)
    var entry$934=$$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}});
    $$$c2.check(entry$934.key.equals((47)),$$$cl1.String("entry key",9));
    $$$c2.check(entry$934.item.equals($$$cl1.String("hi there",8)),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (145:4-145:30)
    var entry2$935=$$$cl1.Entry(true,entry$934,{Key:{t:$$$cl1.true$789},Item:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}}}});
    $$$c2.check(entry2$935.key.equals(true),$$$cl1.String("entry key",9));
    $$$c2.check(entry2$935.item.equals($$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}})),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (149:4-149:41)
    var s1$936=(opt$937=(true?$$$cl1.String("ok",2):null),opt$937!==null?opt$937:$$$cl1.String("noo",3));
    var opt$937;
    $$$c2.check(s1$936.equals($$$cl1.String("ok",2)),$$$cl1.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (151:4-151:47)
    var s2$938=(opt$939=(false?$$$cl1.String("what?",5):null),opt$939!==null?opt$939:$$$cl1.String("great",5));
    var opt$939;
    $$$c2.check(s2$938.equals($$$cl1.String("great",5)),$$$cl1.String("then/else 2",11));
};

//MethodDefinition testCollectionOperators at operators.ceylon (155:0-167:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (156:4-156:33)
    var seq1$940=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (157:4-157:23)
    var s1$941=seq1$940.get((0));
    $$$c2.check(s1$941.equals($$$cl1.String("one",3)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (159:4-159:28)
    var s2$942=seq1$940.get((2));
    $$$c2.check((!$$$cl1.exists(s2$942)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (161:4-161:29)
    var s3$943=seq1$940.get((-(1)));
    $$$c2.check((!$$$cl1.exists(s3$943)),$$$cl1.String("lookup",6));
};

//ClassDefinition NullsafeTest at operators.ceylon (169:0-174:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    
    //MethodDefinition f at operators.ceylon (170:4-170:33)
    function f(){
        return (1);
    }
    $$nullsafeTest.f=f;
    
    //MethodDefinition f2 at operators.ceylon (171:4-173:4)
    function f2(x$944){
        return x$944();
    }
    $$nullsafeTest.f2=f2;
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl1.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl1.Basic);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (176:0-178:0)
function nullsafeTest(f$945){
    return f$945();
};

//MethodDefinition testNullsafeOperators at operators.ceylon (180:0-221:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (181:4-181:27)
    var seq$946=$$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (182:4-182:34)
    var s1$947=(opt$948=seq$946.get((0)),opt$948!==null?opt$948:$$$cl1.String("null",4));
    var opt$948;
    $$$c2.check(s1$947.equals($$$cl1.String("hi",2)),$$$cl1.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (184:4-184:34)
    var s2$949=(opt$950=seq$946.get((1)),opt$950!==null?opt$950:$$$cl1.String("null",4));
    var opt$950;
    $$$c2.check(s2$949.equals($$$cl1.String("null",4)),$$$cl1.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (187:4-187:21)
    var s3$951=null;
    
    //AttributeDeclaration s4 at operators.ceylon (188:4-188:23)
    var s4$952=$$$cl1.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (189:4-189:42)
    var s5$953=(opt$954=(opt$955=s3$951,opt$955!==null?opt$955.uppercased:null),opt$954!==null?opt$954:$$$cl1.String("null",4));
    var opt$954,opt$955;
    
    //AttributeDeclaration s6 at operators.ceylon (190:4-190:42)
    var s6$956=(opt$957=(opt$958=s4$952,opt$958!==null?opt$958.uppercased:null),opt$957!==null?opt$957:$$$cl1.String("null",4));
    var opt$957,opt$958;
    $$$c2.check(s5$953.equals($$$cl1.String("null",4)),$$$cl1.String("nullsafe member 1",17));
    $$$c2.check(s6$956.equals($$$cl1.String("TEST",4)),$$$cl1.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (193:4-193:28)
    var obj$959=null;
    
    //AttributeDeclaration i at operators.ceylon (194:4-194:25)
    var i$960=(opt$961=obj$959,$$$cl1.JsCallable(opt$961,opt$961!==null?opt$961.f:null))();
    var opt$961;
    $$$c2.check((!$$$cl1.exists(i$960)),$$$cl1.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (196:4-196:37)
    var f2$962=(opt$963=obj$959,$$$cl1.JsCallable(opt$963,opt$963!==null?opt$963.f:null));
    var opt$963;
    $$$c2.check((!$$$cl1.exists(nullsafeTest(f2$962))),$$$cl1.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (198:4-198:38)
    var f3$964=(opt$965=obj$959,$$$cl1.JsCallable(opt$965,opt$965!==null?opt$965.f:null));
    var opt$965;
    $$$c2.check($$$cl1.exists(f3$964),$$$cl1.String("nullsafe method ref 2",21));
    (opt$966=obj$959,$$$cl1.JsCallable(opt$966,opt$966!==null?opt$966.f:null))();
    var opt$966;
    $$$c2.check((!$$$cl1.exists((opt$967=obj$959,$$$cl1.JsCallable(opt$967,opt$967!==null?opt$967.f:null))())),$$$cl1.String("nullsafe simple call",20));
    var opt$967;
    
    //MethodDefinition getNullsafe at operators.ceylon (202:4-202:46)
    function getNullsafe$968(){
        return obj$959;
    };
    
    //MethodDeclaration f4 at operators.ceylon (203:4-203:39)
    var f4$969=function (){
        return (opt$970=getNullsafe$968(),$$$cl1.JsCallable(opt$970,opt$970!==null?opt$970.f:null))();
    };
    var opt$970;
    
    //AttributeDeclaration result_f4 at operators.ceylon (204:4-204:29)
    var result_f4$971=f4$969();
    $$$c2.check((!$$$cl1.exists(result_f4$971)),$$$cl1.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (206:4-206:36)
    var i2$972=(opt$973=getNullsafe$968(),$$$cl1.JsCallable(opt$973,opt$973!==null?opt$973.f:null))();
    var opt$973;
    $$$c2.check((!$$$cl1.exists(i2$972)),$$$cl1.String("nullsafe invoke 3",17));
    $$$c2.check((!$$$cl1.exists(NullsafeTest().f2((opt$974=getNullsafe$968(),$$$cl1.JsCallable(opt$974,opt$974!==null?opt$974.f:null))))),$$$cl1.String("nullsafe method ref 3",21));
    var opt$974;
    
    //AttributeDeclaration obj2 at operators.ceylon (209:4-209:39)
    var obj2$975=NullsafeTest();
    var i3$976;
    if((i3$976=(opt$977=obj2$975,$$$cl1.JsCallable(opt$977,opt$977!==null?opt$977.f:null))())!==null){
        $$$c2.check(i3$976.equals((1)),$$$cl1.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe invoke 4 (null)",24));
    }
    var opt$977;
    
    //MethodDeclaration obj2_f at operators.ceylon (215:4-215:34)
    var obj2_f$978=function (){
        return (opt$979=obj2$975,$$$cl1.JsCallable(opt$979,opt$979!==null?opt$979.f:null))();
    };
    var opt$979;
    var i3$980;
    if((i3$980=obj2_f$978())!==null){
        $$$c2.check(i3$980.equals((1)),$$$cl1.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe method ref 4 (null)",28));
    }
};

//MethodDefinition testIncDecOperators at operators.ceylon (223:0-298:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (224:4-224:27)
    var x0$981=(1);
    var setX0$981=function(x0$982){return x0$981=x0$982;};
    
    //AttributeGetterDefinition x at operators.ceylon (225:4-225:27)
    var getX$983=function(){
        return x0$981;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (225:29-225:48)
    var setX$983=function(x$984){
        x0$981=x$984;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (227:4-227:27)
    var i1$985=(1);
    var setI1$985=function(i1$986){return i1$985=i1$986;};
    
    //MethodDefinition f1 at operators.ceylon (228:4-235:4)
    function f1$987(){
        
        //AttributeDeclaration i2 at operators.ceylon (229:8-229:25)
        var i2$988=(i1$985=i1$985.successor,i1$985);
        
        //AttributeDeclaration x2 at operators.ceylon (230:8-230:24)
        var x2$989=(setX$983(getX$983().successor),getX$983());
        $$$c2.check(i1$985.equals((2)),$$$cl1.String("prefix increment 1",18));
        $$$c2.check(i2$988.equals((2)),$$$cl1.String("prefix increment 2",18));
        $$$c2.check(getX$983().equals((2)),$$$cl1.String("prefix increment 3",18));
        $$$c2.check(x2$989.equals((2)),$$$cl1.String("prefix increment 4",18));
    };
    f1$987();
    
    //ClassDefinition C1 at operators.ceylon (238:4-242:4)
    function C1$990($$c1$990){
        $init$C1$990();
        if ($$c1$990===undefined)$$c1$990=new C1$990.$$;
        
        //AttributeDeclaration i at operators.ceylon (239:8-239:37)
        var i=(1);
        $$$cl1.defineAttr($$c1$990,'i',function(){return i;},function(i$991){return i=i$991;});
        
        //AttributeDeclaration x0 at operators.ceylon (240:8-240:31)
        var x0$992=(1);
        $$$cl1.defineAttr($$c1$990,'x0$992',function(){return x0$992;},function(x0$993){return x0$992=x0$993;});
        
        //AttributeGetterDefinition x at operators.ceylon (241:8-241:38)
        $$$cl1.defineAttr($$c1$990,'x',function(){
            return x0$992;
        },function(x$994){
            x0$992=x$994;
        });
        return $$c1$990;
    }
    function $init$C1$990(){
        if (C1$990.$$===undefined){
            $$$cl1.initTypeProto(C1$990,'operators::testIncDecOperators.C1',$$$cl1.Basic);
        }
        return C1$990;
    }
    $init$C1$990();
    
    //AttributeDeclaration c1 at operators.ceylon (243:4-243:16)
    var c1$995=C1$990();
    
    //AttributeDeclaration i3 at operators.ceylon (244:4-244:27)
    var i3$996=(0);
    var setI3$996=function(i3$997){return i3$996=i3$997;};
    
    //MethodDefinition f2 at operators.ceylon (245:4-248:4)
    function f2$998(){
        (i3$996=i3$996.successor,i3$996);
        return c1$995;
    };
    
    //AttributeDeclaration i4 at operators.ceylon (249:4-249:25)
    var i4$999=(tmp$1000=f2$998(),tmp$1000.i=tmp$1000.i.successor,tmp$1000.i);
    var tmp$1000;
    
    //AttributeDeclaration x4 at operators.ceylon (250:4-250:25)
    var x4$1001=(tmp$1002=f2$998(),tmp$1002.x=tmp$1002.x.successor,tmp$1002.x);
    var tmp$1002;
    $$$c2.check(i4$999.equals((2)),$$$cl1.String("prefix increment 5",18));
    $$$c2.check(c1$995.i.equals((2)),$$$cl1.String("prefix increment 6",18));
    $$$c2.check(x4$1001.equals((2)),$$$cl1.String("prefix increment 7",18));
    $$$c2.check(c1$995.x.equals((2)),$$$cl1.String("prefix increment 8",18));
    $$$c2.check(i3$996.equals((2)),$$$cl1.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (257:4-261:4)
    function f3$1003(){
        
        //AttributeDeclaration i2 at operators.ceylon (258:8-258:25)
        var i2$1004=(i1$985=i1$985.predecessor,i1$985);
        $$$c2.check(i1$985.equals((1)),$$$cl1.String("prefix decrement",16));
        $$$c2.check(i2$1004.equals((1)),$$$cl1.String("prefix decrement",16));
    };
    f3$1003();
    
    //AttributeDeclaration i5 at operators.ceylon (264:4-264:25)
    var i5$1005=(tmp$1006=f2$998(),tmp$1006.i=tmp$1006.i.predecessor,tmp$1006.i);
    var tmp$1006;
    $$$c2.check(i5$1005.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(c1$995.i.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(i3$996.equals((3)),$$$cl1.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (269:4-276:4)
    function f4$1007(){
        
        //AttributeDeclaration i2 at operators.ceylon (270:8-270:25)
        var i2$1008=(oldi1$1009=i1$985,i1$985=oldi1$1009.successor,oldi1$1009);
        var oldi1$1009;
        
        //AttributeDeclaration x2 at operators.ceylon (271:8-271:24)
        var x2$1010=(oldx$1011=getX$983(),setX$983(oldx$1011.successor),oldx$1011);
        var oldx$1011;
        $$$c2.check(i1$985.equals((2)),$$$cl1.String("postfix increment 1",19));
        $$$c2.check(i2$1008.equals((1)),$$$cl1.String("postfix increment 2",19));
        $$$c2.check(getX$983().equals((3)),$$$cl1.String("postfix increment 3",19));
        $$$c2.check(x2$1010.equals((2)),$$$cl1.String("postfix increment 4",19));
    };
    f4$1007();
    
    //AttributeDeclaration i6 at operators.ceylon (279:4-279:25)
    var i6$1012=(tmp$1013=f2$998(),oldi$1014=tmp$1013.i,tmp$1013.i=oldi$1014.successor,oldi$1014);
    var tmp$1013,oldi$1014;
    
    //AttributeDeclaration x6 at operators.ceylon (280:4-280:25)
    var x6$1015=(tmp$1016=f2$998(),oldx$1017=tmp$1016.x,tmp$1016.x=oldx$1017.successor,oldx$1017);
    var tmp$1016,oldx$1017;
    $$$c2.check(i6$1012.equals((1)),$$$cl1.String("postfix increment 5",19));
    $$$c2.check(c1$995.i.equals((2)),$$$cl1.String("postfix increment 6",19));
    $$$c2.check(x6$1015.equals((2)),$$$cl1.String("postfix increment 7 ",20));
    $$$c2.check(c1$995.x.equals((3)),$$$cl1.String("postfix increment 8 ",20));
    $$$c2.check(i3$996.equals((5)),$$$cl1.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (287:4-291:4)
    function f5$1018(){
        
        //AttributeDeclaration i2 at operators.ceylon (288:8-288:25)
        var i2$1019=(oldi1$1020=i1$985,i1$985=oldi1$1020.predecessor,oldi1$1020);
        var oldi1$1020;
        $$$c2.check(i1$985.equals((1)),$$$cl1.String("postfix decrement",17));
        $$$c2.check(i2$1019.equals((2)),$$$cl1.String("postfix decrement",17));
    };
    f5$1018();
    
    //AttributeDeclaration i7 at operators.ceylon (294:4-294:25)
    var i7$1021=(tmp$1022=f2$998(),oldi$1023=tmp$1022.i,tmp$1022.i=oldi$1023.predecessor,oldi$1023);
    var tmp$1022,oldi$1023;
    $$$c2.check(i7$1021.equals((2)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(c1$995.i.equals((1)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(i3$996.equals((6)),$$$cl1.String("postfix decrement",17));
};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (300:0-351:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (301:4-301:27)
    var i1$1024=(1);
    var setI1$1024=function(i1$1025){return i1$1024=i1$1025;};
    
    //AttributeDeclaration x0 at operators.ceylon (302:4-302:27)
    var x0$1026=(1);
    var setX0$1026=function(x0$1027){return x0$1026=x0$1027;};
    
    //AttributeGetterDefinition x at operators.ceylon (303:4-303:27)
    var getX$1028=function(){
        return x0$1026;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (303:29-303:46)
    var setX$1028=function(x$1029){
        x0$1026=x$1029;
    };
    (i1$1024=i1$1024.plus((10)),i1$1024);
    (setX$1028(getX$1028().plus((10))),getX$1028());
    $$$c2.check(i1$1024.equals((11)),$$$cl1.String("+= operator 1",13));
    $$$c2.check(getX$1028().equals((11)),$$$cl1.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (309:4-309:36)
    var i2$1030=(i1$1024=i1$1024.plus((-(5))),i1$1024);
    var setI2$1030=function(i2$1031){return i2$1030=i2$1031;};
    
    //AttributeDeclaration x2 at operators.ceylon (310:4-310:35)
    var x2$1032=(setX$1028(getX$1028().plus((-(5)))),getX$1028());
    var setX2$1032=function(x2$1033){return x2$1032=x2$1033;};
    $$$c2.check(i2$1030.equals((6)),$$$cl1.String("+= operator 3",13));
    $$$c2.check(i1$1024.equals((6)),$$$cl1.String("+= operator 4",13));
    $$$c2.check(x2$1032.equals((6)),$$$cl1.String("+= operator 5",13));
    $$$c2.check(getX$1028().equals((6)),$$$cl1.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (316:4-320:4)
    function C1$1034($$c1$1034){
        $init$C1$1034();
        if ($$c1$1034===undefined)$$c1$1034=new C1$1034.$$;
        
        //AttributeDeclaration i at operators.ceylon (317:8-317:37)
        var i=(1);
        $$$cl1.defineAttr($$c1$1034,'i',function(){return i;},function(i$1035){return i=i$1035;});
        
        //AttributeDeclaration x0 at operators.ceylon (318:8-318:31)
        var x0$1036=(1);
        $$$cl1.defineAttr($$c1$1034,'x0$1036',function(){return x0$1036;},function(x0$1037){return x0$1036=x0$1037;});
        
        //AttributeGetterDefinition x at operators.ceylon (319:8-319:38)
        $$$cl1.defineAttr($$c1$1034,'x',function(){
            return x0$1036;
        },function(x$1038){
            x0$1036=x$1038;
        });
        return $$c1$1034;
    }
    function $init$C1$1034(){
        if (C1$1034.$$===undefined){
            $$$cl1.initTypeProto(C1$1034,'operators::testArithmeticAssignOperators.C1',$$$cl1.Basic);
        }
        return C1$1034;
    }
    $init$C1$1034();
    
    //AttributeDeclaration c1 at operators.ceylon (321:4-321:16)
    var c1$1039=C1$1034();
    
    //AttributeDeclaration i3 at operators.ceylon (322:4-322:27)
    var i3$1040=(0);
    var setI3$1040=function(i3$1041){return i3$1040=i3$1041;};
    
    //MethodDefinition f at operators.ceylon (323:4-326:4)
    function f$1042(){
        (i3$1040=i3$1040.successor,i3$1040);
        return c1$1039;
    };
    i2$1030=(tmp$1043=f$1042(),tmp$1043.i=tmp$1043.i.plus((11)),tmp$1043.i);
    var tmp$1043;
    x2$1032=(tmp$1044=f$1042(),tmp$1044.x=tmp$1044.x.plus((11)),tmp$1044.x);
    var tmp$1044;
    $$$c2.check(i2$1030.equals((12)),$$$cl1.String("+= operator 7",13));
    $$$c2.check(c1$1039.i.equals((12)),$$$cl1.String("+= operator 8",13));
    $$$c2.check(x2$1032.equals((12)),$$$cl1.String("+= operator 9",13));
    $$$c2.check(c1$1039.x.equals((12)),$$$cl1.String("+= operator 10",14));
    $$$c2.check(i3$1040.equals((2)),$$$cl1.String("+= operator 11",14));
    i2$1030=(i1$1024=i1$1024.minus((14)),i1$1024);
    $$$c2.check(i1$1024.equals((-(8))),$$$cl1.String("-= operator",11));
    $$$c2.check(i2$1030.equals((-(8))),$$$cl1.String("-= operator",11));
    i2$1030=(i1$1024=i1$1024.times((-(3))),i1$1024);
    $$$c2.check(i1$1024.equals((24)),$$$cl1.String("*= operator",11));
    $$$c2.check(i2$1030.equals((24)),$$$cl1.String("*= operator",11));
    i2$1030=(i1$1024=i1$1024.divided((5)),i1$1024);
    $$$c2.check(i1$1024.equals((4)),$$$cl1.String("/= operator",11));
    $$$c2.check(i2$1030.equals((4)),$$$cl1.String("/= operator",11));
    i2$1030=(i1$1024=i1$1024.remainder((3)),i1$1024);
    $$$c2.check(i1$1024.equals((1)),$$$cl1.String("%= operator",11));
    $$$c2.check(i2$1030.equals((1)),$$$cl1.String("%= operator",11));
};

//MethodDefinition testAssignmentOperator at operators.ceylon (353:0-383:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (354:4-354:27)
    var i1$1045=(1);
    var setI1$1045=function(i1$1046){return i1$1045=i1$1046;};
    
    //AttributeDeclaration i2 at operators.ceylon (355:4-355:27)
    var i2$1047=(2);
    var setI2$1047=function(i2$1048){return i2$1047=i2$1048;};
    
    //AttributeDeclaration i3 at operators.ceylon (356:4-356:27)
    var i3$1049=(3);
    var setI3$1049=function(i3$1050){return i3$1049=i3$1050;};
    $$$c2.check((i1$1045=(i2$1047=i3$1049,i2$1047),i1$1045).equals((3)),$$$cl1.String("assignment 1",12));
    $$$c2.check(i1$1045.equals((3)),$$$cl1.String("assignment 2",12));
    $$$c2.check(i2$1047.equals((3)),$$$cl1.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (361:4-361:28)
    var getX1$1051=function(){
        return i1$1045;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (361:30-361:51)
    var setX1$1051=function(x1$1052){
        i1$1045=x1$1052;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (362:4-362:28)
    var getX2$1053=function(){
        return i2$1047;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (362:30-362:51)
    var setX2$1053=function(x2$1054){
        i2$1047=x2$1054;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (363:4-363:28)
    var getX3$1055=function(){
        return i3$1049;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (363:30-363:51)
    var setX3$1055=function(x3$1056){
        i3$1049=x3$1056;
    };
    i1$1045=(1);
    i2$1047=(2);
    $$$c2.check((setX1$1051((setX2$1053(getX3$1055()),getX2$1053())),getX1$1051()).equals((3)),$$$cl1.String("assignment 4",12));
    $$$c2.check(getX1$1051().equals((3)),$$$cl1.String("assignment 5",12));
    $$$c2.check(getX2$1053().equals((3)),$$$cl1.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (370:4-374:4)
    function C$1057($$c$1057){
        $init$C$1057();
        if ($$c$1057===undefined)$$c$1057=new C$1057.$$;
        
        //AttributeDeclaration i at operators.ceylon (371:8-371:37)
        var i=(1);
        $$$cl1.defineAttr($$c$1057,'i',function(){return i;},function(i$1058){return i=i$1058;});
        
        //AttributeDeclaration x0 at operators.ceylon (372:8-372:31)
        var x0$1059=(1);
        $$$cl1.defineAttr($$c$1057,'x0$1059',function(){return x0$1059;},function(x0$1060){return x0$1059=x0$1060;});
        
        //AttributeGetterDefinition x at operators.ceylon (373:8-373:38)
        $$$cl1.defineAttr($$c$1057,'x',function(){
            return x0$1059;
        },function(x$1061){
            x0$1059=x$1061;
        });
        return $$c$1057;
    }
    function $init$C$1057(){
        if (C$1057.$$===undefined){
            $$$cl1.initTypeProto(C$1057,'operators::testAssignmentOperator.C',$$$cl1.Basic);
        }
        return C$1057;
    }
    $init$C$1057();
    
    //AttributeDeclaration o1 at operators.ceylon (375:4-375:14)
    var o1$1062=C$1057();
    
    //AttributeDeclaration o2 at operators.ceylon (376:4-376:14)
    var o2$1063=C$1057();
    $$$c2.check((tmp$1064=o1$1062,tmp$1064.i=(tmp$1065=o2$1063,tmp$1065.i=(3),tmp$1065.i),tmp$1064.i).equals((3)),$$$cl1.String("assignment 7",12));
    var tmp$1064,tmp$1065;
    $$$c2.check(o1$1062.i.equals((3)),$$$cl1.String("assignment 8",12));
    $$$c2.check(o2$1063.i.equals((3)),$$$cl1.String("assignment 9",12));
    $$$c2.check((tmp$1066=o1$1062,tmp$1066.x=(tmp$1067=o2$1063,tmp$1067.x=(3),tmp$1067.x),tmp$1066.x).equals((3)),$$$cl1.String("assignment 10",13));
    var tmp$1066,tmp$1067;
    $$$c2.check(o1$1062.x.equals((3)),$$$cl1.String("assignment 11",13));
    $$$c2.check(o2$1063.x.equals((3)),$$$cl1.String("assignment 12",13));
};

//MethodDefinition testSegments at operators.ceylon (385:0-412:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (386:4-386:97)
    var seq$1068=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.Tuple($$$cl1.String("nine",4),$$$cl1.Tuple($$$cl1.String("ten",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    $$$c2.check(seq$1068.segment((1),(2)).equals($$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[1:2] ",9),seq$1068.segment((1),(2)).string]).string);
    $$$c2.check(seq$1068.segment((3),(5)).equals($$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[3:5] ",9),seq$1068.segment((3),(5)).string]).string);
    $$$c2.check($$$cl1.String("test",4).segment((1),(2)).equals($$$cl1.String("es",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("test[1:2] ",10),$$$cl1.String("test",4).segment((1),(2)).string]).string);
    $$$c2.check($$$cl1.String("hello",5).segment((2),(2)).equals($$$cl1.String("ll",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("hello[2:2] ",11),$$$cl1.String("hello",5).segment((2),(2)).string]).string);
    
    //AttributeDeclaration s2 at operators.ceylon (391:4-391:18)
    var s2$1069=(function(){var tmpvar$1070=(3);
    if (tmpvar$1070>0){
    var tmpvar$1071=(0);
    var tmpvar$1072=tmpvar$1071;
    for (var i=1; i<tmpvar$1070; i++){tmpvar$1072=tmpvar$1072.successor;}
    return $$$cl1.Range(tmpvar$1071,tmpvar$1072)
    }else return $$$cl1.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (392:4-392:18)
    var s3$1073=(function(){var tmpvar$1074=(5);
    if (tmpvar$1074>0){
    var tmpvar$1075=(2);
    var tmpvar$1076=tmpvar$1075;
    for (var i=1; i<tmpvar$1074; i++){tmpvar$1076=tmpvar$1076.successor;}
    return $$$cl1.Range(tmpvar$1075,tmpvar$1076)
    }else return $$$cl1.getEmpty();}());
    $$$c2.check(s2$1069.size.equals((3)),$$$cl1.String("0:3 [1]",7));
    var x$1077;
    if((x$1077=s2$1069.get((0)))!==null){
        $$$c2.check(x$1077.equals((0)),$$$cl1.String("0:3 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [2]",7));
    }
    var x$1078;
    if((x$1078=s2$1069.get((2)))!==null){
        $$$c2.check(x$1078.equals((2)),$$$cl1.String("0:3 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [3]",7));
    }
    $$$c2.check(s3$1073.size.equals((5)),$$$cl1.String("2:5 [1]",7));
    var x$1079;
    if((x$1079=s3$1073.get((0)))!==null){
        $$$c2.check(x$1079.equals((2)),$$$cl1.String("2:5 [1]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [1]",7));
    }
    var x$1080;
    if((x$1080=s3$1073.get((2)))!==null){
        $$$c2.check(x$1080.equals((4)),$$$cl1.String("2:5 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [2]",7));
    }
    var x$1081;
    if((x$1081=s3$1073.get((4)))!==null){
        $$$c2.check(x$1081.equals((6)),$$$cl1.String("2:5 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [3]",7));
    }
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1082=(0);
    if (tmpvar$1082>0){
    var tmpvar$1083=(1);
    var tmpvar$1084=tmpvar$1083;
    for (var i=1; i<tmpvar$1082; i++){tmpvar$1084=tmpvar$1084.successor;}
    return $$$cl1.Range(tmpvar$1083,tmpvar$1084)
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:0 empty",9));
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1085=(-(1));
    if (tmpvar$1085>0){
    var tmpvar$1086=(1);
    var tmpvar$1087=tmpvar$1086;
    for (var i=1; i<tmpvar$1085; i++){tmpvar$1087=tmpvar$1087.successor;}
    return $$$cl1.Range(tmpvar$1086,tmpvar$1087)
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:-1 empty",10));
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
    $$$c2.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
