(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$906=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$907=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$908=$$$cl1.String("hola",4).iterator();
        var c$909=$$$cl1.getFinished();
        var next$c$909=function(){return c$909=it$908.next();}
        next$c$909();
        return function(){
            if(c$909!==$$$cl1.getFinished()){
                var c$909$910=c$909;
                var tmpvar$911=c$909$910;
                next$c$909();
                return tmpvar$911;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$912=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$913=$$$cl1.String("hola",4).iterator();
        var c$914=$$$cl1.getFinished();
        var next$c$914=function(){return c$914=it$913.next();}
        next$c$914();
        return function(){
            if(c$914!==$$$cl1.getFinished()){
                var c$914$915=c$914;
                var tmpvar$916=c$914$915;
                next$c$914();
                return tmpvar$916;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}).sequence;
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$917=[(0)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).chain(seq$906,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$918=[$$$cl1.Character(65)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}).chain(lcomp$907,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$919=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$920=$$$cl1.Tuple((0),seq$906,{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.className(seq$906).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{1,2,3} is not a Tuple but a ",29),$$$cl1.className(seq$906).string]).string);
    $$$c2.check((!$$$cl1.className(lcomp$907).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("lazy comprehension is a Tuple ",30),$$$cl1.className(lcomp$907).string]).string);
    $$$c2.check($$$cl1.className(ecomp$912).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("eager comprehension is not a Tuple but a ",41),$$$cl1.className(ecomp$912).string]).string);
    $$$c2.check((!$$$cl1.className(s2$917).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} is a Tuple ",20),$$$cl1.className(s2$917).string]).string);
    $$$c2.check((!$$$cl1.className(s3$918).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{x,*iter} is a Tuple ",21),$$$cl1.className(s3$918).string]).string);
    $$$c2.check($$$cl1.className(t1$919).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[1,2,3] is not a Tuple but a ",29),$$$cl1.className(t1$919).string]).string);
    $$$c2.check($$$cl1.className(t2$920).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[0,*seq] is not a Tuple but a ",30),$$$cl1.className(t2$920).string]).string);
    $$$c2.check(seq$906.equals(t1$919),$$$cl1.String("{1,2,3} != [1,2,3]",18));
    $$$c2.check((!$$$cl1.className(t2$920).equals($$$cl1.className(s2$917))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} != [0,*seq] ",21),$$$cl1.className(t2$920).string,$$$cl1.String(" vs",3),$$$cl1.className(s2$917).string]).string);
    $$$c2.check(seq$906.size.equals((3)),$$$cl1.String("seq.size!=3",11));
    $$$c2.check(lcomp$907.sequence.size.equals((4)),$$$cl1.String("lcomp.size!=4",13));
    $$$c2.check(ecomp$912.size.equals((4)),$$$cl1.String("ecomp.size!=4",13));
    $$$c2.check(s2$917.size.equals((4)),$$$cl1.String("s2.size!=4",10));
    $$$c2.check(s3$918.sequence.size.equals((5)),$$$cl1.String("s3.size!=5",10));
    $$$c2.check(t1$919.size.equals((3)),$$$cl1.String("t1.size!=3",10));
    $$$c2.check(t2$920.size.equals((4)),$$$cl1.String("t2.size!=4",10));
    $$$c2.check((!$$$cl1.className(lcomp$907).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*comp} is not Tuple but ",25),$$$cl1.className(lcomp$907).string]).string);
    $$$c2.check($$$cl1.className(ecomp$912).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*ecomp} is not Tuple but ",26),$$$cl1.className(ecomp$912).string]).string);
    $$$c2.check($$$cl1.className(seq$906).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*seq} is not Tuple but ",24),$$$cl1.className(seq$906).string]).string);
};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$921=(-(4));
    var setI1$921=function(i1$922){return i1$921=i1$922;};
    i1$921=(-i1$921);
    $$$c2.check(i1$921.equals((4)),$$$cl1.String("negation",8));
    i1$921=(+(-(987654)));
    $$$c2.check(i1$921.equals((-(987654))),$$$cl1.String("positive",8));
    i1$921=(+(0));
    $$$c2.check(i1$921.equals((0)),$$$cl1.String("+0=0",4));
    i1$921=(-(0));
    $$$c2.check(i1$921.equals((0)),$$$cl1.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$923=(123).plus((456));
    var setI2$923=function(i2$924){return i2$923=i2$924;};
    $$$c2.check(i2$923.equals((579)),$$$cl1.String("addition",8));
    i1$921=i2$923.minus((16));
    $$$c2.check(i1$921.equals((563)),$$$cl1.String("subtraction",11));
    i2$923=(-i1$921).plus(i2$923).minus((1));
    $$$c2.check(i2$923.equals((15)),$$$cl1.String("-i1+i2-1",8));
    i1$921=(3).times((7));
    $$$c2.check(i1$921.equals((21)),$$$cl1.String("multiplication",14));
    i2$923=i1$921.times((2));
    $$$c2.check(i2$923.equals((42)),$$$cl1.String("multiplication",14));
    i2$923=(17).divided((4));
    $$$c2.check(i2$923.equals((4)),$$$cl1.String("integer division",16));
    i1$921=i2$923.times((516)).divided((-i1$921));
    $$$c2.check(i1$921.equals((-(98))),$$$cl1.String("i2*516/-i1",10));
    i1$921=(15).remainder((4));
    $$$c2.check(i1$921.equals((3)),$$$cl1.String("modulo",6));
    i2$923=(312).remainder((12));
    $$$c2.check(i2$923.equals((0)),$$$cl1.String("modulo",6));
    i1$921=(2).power((10));
    $$$c2.check(i1$921.equals((1024)),$$$cl1.String("power",5));
    i2$923=(10).power((6));
    $$$c2.check(i2$923.equals((1000000)),$$$cl1.String("power",5));
};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$925=$$$cl1.Float(4.2).negativeValue;
    var setF1$925=function(f1$926){return f1$925=f1$926;};
    f1$925=f1$925.negativeValue;
    $$$c2.check(f1$925.equals($$$cl1.Float(4.2)),$$$cl1.String("negation",8));
    f1$925=(+$$$cl1.Float(987654.9925567).negativeValue);
    $$$c2.check(f1$925.equals($$$cl1.Float(987654.9925567).negativeValue),$$$cl1.String("positive",8));
    f1$925=(+$$$cl1.Float(0.0));
    $$$c2.check(f1$925.equals($$$cl1.Float(0.0)),$$$cl1.String("+0.0=0.0",8));
    f1$925=$$$cl1.Float(0.0).negativeValue;
    $$$c2.check(f1$925.equals($$$cl1.Float(0.0)),$$$cl1.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$927=$$$cl1.Float(3.14159265).plus($$$cl1.Float(456.0));
    var setF2$927=function(f2$928){return f2$927=f2$928;};
    $$$c2.check(f2$927.equals($$$cl1.Float(459.14159265)),$$$cl1.String("addition",8));
    f1$925=f2$927.minus($$$cl1.Float(0.0016));
    $$$c2.check(f1$925.equals($$$cl1.Float(459.13999265)),$$$cl1.String("subtraction",11));
    f2$927=f1$925.negativeValue.plus(f2$927).minus($$$cl1.Float(1.2));
    $$$c2.check(f2$927.equals($$$cl1.Float(1.1984000000000037).negativeValue),$$$cl1.String("-f1+f2-1.2",10));
    f1$925=$$$cl1.Float(3.0).times($$$cl1.Float(0.79));
    $$$c2.check(f1$925.equals($$$cl1.Float(2.37)),$$$cl1.String("multiplication",14));
    f2$927=f1$925.times($$$cl1.Float(2.0e13));
    $$$c2.check(f2$927.equals($$$cl1.Float(47400000000000.0)),$$$cl1.String("multiplication",14));
    f2$927=$$$cl1.Float(17.1).divided($$$cl1.Float(4.0E-18));
    $$$c2.check(f2$927.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("division",8));
    f1$925=f2$927.times($$$cl1.Float(51.6e2)).divided(f1$925.negativeValue);
    $$$c2.check(f2$927.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("f2*51.6e2/-f1",13));
    f1$925=$$$cl1.Float(150.0).power($$$cl1.Float(0.5));
    $$$c2.check(f1$925.equals($$$cl1.Float(12.24744871391589)),$$$cl1.String("power",5));
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
    var o1$929=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$930=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$931=(o1$929===o2$930);
    var setB1$931=function(b1$932){return b1$931=b1$932;};
    $$$c2.check((!b1$931),$$$cl1.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$933=(o1$929===o1$929);
    var setB2$933=function(b2$934){return b2$933=b2$934;};
    $$$c2.check(b2$933,$$$cl1.String("identity",8));
    b1$931=o1$929.equals(o2$930);
    $$$c2.check((!b1$931),$$$cl1.String("equals",6));
    b2$933=o1$929.equals(o1$929);
    $$$c2.check(b2$933,$$$cl1.String("equals",6));
    b1$931=(1).equals((2));
    $$$c2.check((!b1$931),$$$cl1.String("equals",6));
    b2$933=(!(1).equals((2)));
    $$$c2.check(b2$933,$$$cl1.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$935=(!b2$933);
    var setB3$935=function(b3$936){return b3$935=b3$936;};
    $$$c2.check((!b3$935),$$$cl1.String("not",3));
    b1$931=(true&&false);
    $$$c2.check((!b1$931),$$$cl1.String("and",3));
    b2$933=(b1$931&&true);
    $$$c2.check((!b2$933),$$$cl1.String("and",3));
    b3$935=(true&&true);
    $$$c2.check(b3$935,$$$cl1.String("and",3));
    b1$931=(true||false);
    $$$c2.check(b1$931,$$$cl1.String("or",2));
    b2$933=(false||b1$931);
    $$$c2.check(b2$933,$$$cl1.String("or",2));
    b3$935=(false||false);
    $$$c2.check((!b3$935),$$$cl1.String("or",2));
};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-152:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$937=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4));
    $$$c2.check(c1$937.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$938=$$$cl1.String("str2",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c2$938.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$939=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c3$939.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$940=$$$cl1.String("",0).compare($$$cl1.String("",0));
    $$$c2.check(c4$940.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$941=$$$cl1.String("str1",4).compare($$$cl1.String("",0));
    $$$c2.check(c5$941.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$942=$$$cl1.String("",0).compare($$$cl1.String("str2",4));
    $$$c2.check(c6$942.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$943=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getSmaller());
    var setB1$943=function(b1$944){return b1$943=b1$944;};
    $$$c2.check(b1$943,$$$cl1.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$945=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getLarger());
    var setB2$945=function(b2$946){return b2$945=b2$946;};
    $$$c2.check((!b2$945),$$$cl1.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$947=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getLarger());
    var setB3$947=function(b3$948){return b3$947=b3$948;};
    $$$c2.check(b3$947,$$$cl1.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$949=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getSmaller());
    var setB4$949=function(b4$950){return b4$949=b4$950;};
    $$$c2.check((!b4$949),$$$cl1.String("large as",8));
    b1$943=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getSmaller());
    $$$c2.check((!b1$943),$$$cl1.String("smaller",7));
    b2$945=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getLarger());
    $$$c2.check((!b2$945),$$$cl1.String("larger",6));
    b3$947=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getLarger());
    $$$c2.check(b3$947,$$$cl1.String("small as",8));
    b4$949=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getSmaller());
    $$$c2.check(b4$949,$$$cl1.String("large as",8));
    
    //AttributeDeclaration a at operators.ceylon (140:4-140:15)
    var a$951=(0);
    
    //AttributeDeclaration c at operators.ceylon (141:4-141:16)
    var c$952=(10);
    $$$c2.check((tmpvar$953=(5),tmpvar$953.compare(a$951)===$$$cl1.getLarger()&&tmpvar$953.compare(c$952)===$$$cl1.getSmaller()),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<5<",3),c$952.string]).string);
    $$$c2.check((tmpvar$954=(0),tmpvar$954.compare(a$951)!==$$$cl1.getSmaller()&&tmpvar$954.compare(c$952)===$$$cl1.getSmaller()),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<=0<",4),c$952.string]).string);
    $$$c2.check((tmpvar$955=(10),tmpvar$955.compare(a$951)===$$$cl1.getLarger()&&tmpvar$955.compare(c$952)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<10<=",5),c$952.string]).string);
    $$$c2.check((tmpvar$956=(0),tmpvar$956.compare(a$951)!==$$$cl1.getSmaller()&&tmpvar$956.compare(c$952)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<=0<=",5),c$952.string]).string);
    $$$c2.check((tmpvar$957=(10),tmpvar$957.compare(a$951)!==$$$cl1.getSmaller()&&tmpvar$957.compare(c$952)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<=10<=",6),c$952.string]).string);
    $$$c2.check((!(tmpvar$958=(15),tmpvar$958.compare(a$951)===$$$cl1.getLarger()&&tmpvar$958.compare(c$952)===$$$cl1.getSmaller())),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<15<",4),c$952.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$959=(10),tmpvar$959.compare(a$951)!==$$$cl1.getSmaller()&&tmpvar$959.compare(c$952)===$$$cl1.getSmaller())),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<=10<",5),c$952.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$960=(0),tmpvar$960.compare(a$951)===$$$cl1.getLarger()&&tmpvar$960.compare(c$952)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<0<=",4),c$952.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$961=(11),tmpvar$961.compare(a$951)!==$$$cl1.getSmaller()&&tmpvar$961.compare(c$952)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<=11<=",6),c$952.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$962=(-(1)),tmpvar$962.compare(a$951)!==$$$cl1.getSmaller()&&tmpvar$962.compare(c$952)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$951.string,$$$cl1.String("<=-1<=",6),c$952.string,$$$cl1.String(" WTF",4)]).string);
};

//MethodDefinition testOtherOperators at operators.ceylon (154:0-166:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (155:4-155:42)
    var entry$963=$$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}});
    $$$c2.check(entry$963.key.equals((47)),$$$cl1.String("entry key",9));
    $$$c2.check(entry$963.item.equals($$$cl1.String("hi there",8)),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (158:4-158:30)
    var entry2$964=$$$cl1.Entry(true,entry$963,{Key:{t:$$$cl1.true$807},Item:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}}}});
    $$$c2.check(entry2$964.key.equals(true),$$$cl1.String("entry key",9));
    $$$c2.check(entry2$964.item.equals($$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}})),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (162:4-162:41)
    var s1$965=(opt$966=(true?$$$cl1.String("ok",2):null),opt$966!==null?opt$966:$$$cl1.String("noo",3));
    var opt$966;
    $$$c2.check(s1$965.equals($$$cl1.String("ok",2)),$$$cl1.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (164:4-164:47)
    var s2$967=(opt$968=(false?$$$cl1.String("what?",5):null),opt$968!==null?opt$968:$$$cl1.String("great",5));
    var opt$968;
    $$$c2.check(s2$967.equals($$$cl1.String("great",5)),$$$cl1.String("then/else 2",11));
};

//MethodDefinition testCollectionOperators at operators.ceylon (168:0-180:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (169:4-169:33)
    var seq1$969=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (170:4-170:23)
    var s1$970=seq1$969.get((0));
    $$$c2.check(s1$970.equals($$$cl1.String("one",3)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (172:4-172:28)
    var s2$971=seq1$969.get((2));
    $$$c2.check((!$$$cl1.exists(s2$971)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (174:4-174:29)
    var s3$972=seq1$969.get((-(1)));
    $$$c2.check((!$$$cl1.exists(s3$972)),$$$cl1.String("lookup",6));
};

//ClassDefinition NullsafeTest at operators.ceylon (182:0-187:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    
    //MethodDefinition f at operators.ceylon (183:4-183:33)
    function f(){
        return (1);
    }
    $$nullsafeTest.f=f;
    
    //MethodDefinition f2 at operators.ceylon (184:4-186:4)
    function f2(x$973){
        return x$973();
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

//MethodDefinition nullsafeTest at operators.ceylon (189:0-191:0)
function nullsafeTest(f$974){
    return f$974();
};

//MethodDefinition testNullsafeOperators at operators.ceylon (193:0-234:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (194:4-194:27)
    var seq$975=$$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (195:4-195:34)
    var s1$976=(opt$977=seq$975.get((0)),opt$977!==null?opt$977:$$$cl1.String("null",4));
    var opt$977;
    $$$c2.check(s1$976.equals($$$cl1.String("hi",2)),$$$cl1.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (197:4-197:34)
    var s2$978=(opt$979=seq$975.get((1)),opt$979!==null?opt$979:$$$cl1.String("null",4));
    var opt$979;
    $$$c2.check(s2$978.equals($$$cl1.String("null",4)),$$$cl1.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (200:4-200:21)
    var s3$980=null;
    
    //AttributeDeclaration s4 at operators.ceylon (201:4-201:23)
    var s4$981=$$$cl1.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (202:4-202:42)
    var s5$982=(opt$983=(opt$984=s3$980,opt$984!==null?opt$984.uppercased:null),opt$983!==null?opt$983:$$$cl1.String("null",4));
    var opt$983,opt$984;
    
    //AttributeDeclaration s6 at operators.ceylon (203:4-203:42)
    var s6$985=(opt$986=(opt$987=s4$981,opt$987!==null?opt$987.uppercased:null),opt$986!==null?opt$986:$$$cl1.String("null",4));
    var opt$986,opt$987;
    $$$c2.check(s5$982.equals($$$cl1.String("null",4)),$$$cl1.String("nullsafe member 1",17));
    $$$c2.check(s6$985.equals($$$cl1.String("TEST",4)),$$$cl1.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (206:4-206:28)
    var obj$988=null;
    
    //AttributeDeclaration i at operators.ceylon (207:4-207:25)
    var i$989=(opt$990=obj$988,$$$cl1.JsCallable(opt$990,opt$990!==null?opt$990.f:null))();
    var opt$990;
    $$$c2.check((!$$$cl1.exists(i$989)),$$$cl1.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (209:4-209:37)
    var f2$991=$$$cl1.$JsCallable((opt$992=obj$988,$$$cl1.JsCallable(opt$992,opt$992!==null?opt$992.f:null)),{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}});
    var opt$992;
    $$$c2.check((!$$$cl1.exists(nullsafeTest($$$cl1.$JsCallable(f2$991,{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}})))),$$$cl1.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (211:4-211:38)
    var f3$993=$$$cl1.$JsCallable((opt$994=obj$988,$$$cl1.JsCallable(opt$994,opt$994!==null?opt$994.f:null)),{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}});
    var opt$994;
    $$$c2.check($$$cl1.exists(f3$993),$$$cl1.String("nullsafe method ref 2",21));
    (opt$995=obj$988,$$$cl1.JsCallable(opt$995,opt$995!==null?opt$995.f:null))();
    var opt$995;
    $$$c2.check((!$$$cl1.exists((opt$996=obj$988,$$$cl1.JsCallable(opt$996,opt$996!==null?opt$996.f:null))())),$$$cl1.String("nullsafe simple call",20));
    var opt$996;
    
    //MethodDefinition getNullsafe at operators.ceylon (215:4-215:46)
    function getNullsafe$997(){
        return obj$988;
    };
    
    //MethodDeclaration f4 at operators.ceylon (216:4-216:39)
    var f4$998=function (){
        return (opt$999=getNullsafe$997(),$$$cl1.JsCallable(opt$999,opt$999!==null?opt$999.f:null))();
    };
    var opt$999;
    
    //AttributeDeclaration result_f4 at operators.ceylon (217:4-217:29)
    var result_f4$1000=f4$998();
    $$$c2.check((!$$$cl1.exists(result_f4$1000)),$$$cl1.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (219:4-219:36)
    var i2$1001=(opt$1002=getNullsafe$997(),$$$cl1.JsCallable(opt$1002,opt$1002!==null?opt$1002.f:null))();
    var opt$1002;
    $$$c2.check((!$$$cl1.exists(i2$1001)),$$$cl1.String("nullsafe invoke 3",17));
    $$$c2.check((!$$$cl1.exists(NullsafeTest().f2($$$cl1.$JsCallable((opt$1003=getNullsafe$997(),$$$cl1.JsCallable(opt$1003,opt$1003!==null?opt$1003.f:null)),{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}})))),$$$cl1.String("nullsafe method ref 3",21));
    var opt$1003;
    
    //AttributeDeclaration obj2 at operators.ceylon (222:4-222:39)
    var obj2$1004=NullsafeTest();
    var i3$1005;
    if((i3$1005=(opt$1006=obj2$1004,$$$cl1.JsCallable(opt$1006,opt$1006!==null?opt$1006.f:null))())!==null){
        $$$c2.check(i3$1005.equals((1)),$$$cl1.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe invoke 4 (null)",24));
    }
    var opt$1006;
    
    //MethodDeclaration obj2_f at operators.ceylon (228:4-228:34)
    var obj2_f$1007=function (){
        return (opt$1008=obj2$1004,$$$cl1.JsCallable(opt$1008,opt$1008!==null?opt$1008.f:null))();
    };
    var opt$1008;
    var i3$1009;
    if((i3$1009=obj2_f$1007())!==null){
        $$$c2.check(i3$1009.equals((1)),$$$cl1.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe method ref 4 (null)",28));
    }
};

//MethodDefinition testIncDecOperators at operators.ceylon (236:0-311:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (237:4-237:27)
    var x0$1010=(1);
    var setX0$1010=function(x0$1011){return x0$1010=x0$1011;};
    
    //AttributeGetterDefinition x at operators.ceylon (238:4-238:27)
    var getX$1012=function(){
        return x0$1010;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (238:29-238:48)
    var setX$1012=function(x$1013){
        x0$1010=x$1013;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (240:4-240:27)
    var i1$1014=(1);
    var setI1$1014=function(i1$1015){return i1$1014=i1$1015;};
    
    //MethodDefinition f1 at operators.ceylon (241:4-248:4)
    function f1$1016(){
        
        //AttributeDeclaration i2 at operators.ceylon (242:8-242:25)
        var i2$1017=(i1$1014=i1$1014.successor);
        
        //AttributeDeclaration x2 at operators.ceylon (243:8-243:24)
        var x2$1018=(setX$1012(getX$1012().successor),getX$1012());
        $$$c2.check(i1$1014.equals((2)),$$$cl1.String("prefix increment 1",18));
        $$$c2.check(i2$1017.equals((2)),$$$cl1.String("prefix increment 2",18));
        $$$c2.check(getX$1012().equals((2)),$$$cl1.String("prefix increment 3",18));
        $$$c2.check(x2$1018.equals((2)),$$$cl1.String("prefix increment 4",18));
    };
    f1$1016();
    
    //ClassDefinition C1 at operators.ceylon (251:4-255:4)
    function C1$1019($$c1$1019){
        $init$C1$1019();
        if ($$c1$1019===undefined)$$c1$1019=new C1$1019.$$;
        
        //AttributeDeclaration i at operators.ceylon (252:8-252:37)
        var i=(1);
        $$$cl1.defineAttr($$c1$1019,'i',function(){return i;},function(i$1020){return i=i$1020;});
        
        //AttributeDeclaration x0 at operators.ceylon (253:8-253:31)
        var x0$1021=(1);
        $$$cl1.defineAttr($$c1$1019,'x0$1021',function(){return x0$1021;},function(x0$1022){return x0$1021=x0$1022;});
        
        //AttributeGetterDefinition x at operators.ceylon (254:8-254:38)
        $$$cl1.defineAttr($$c1$1019,'x',function(){
            return x0$1021;
        },function(x$1023){
            x0$1021=x$1023;
        });
        return $$c1$1019;
    }
    function $init$C1$1019(){
        if (C1$1019.$$===undefined){
            $$$cl1.initTypeProto(C1$1019,'operators::testIncDecOperators.C1',$$$cl1.Basic);
        }
        return C1$1019;
    }
    $init$C1$1019();
    
    //AttributeDeclaration c1 at operators.ceylon (256:4-256:16)
    var c1$1024=C1$1019();
    
    //AttributeDeclaration i3 at operators.ceylon (257:4-257:27)
    var i3$1025=(0);
    var setI3$1025=function(i3$1026){return i3$1025=i3$1026;};
    
    //MethodDefinition f2 at operators.ceylon (258:4-261:4)
    function f2$1027(){
        (i3$1025=i3$1025.successor);
        return c1$1024;
    };
    
    //AttributeDeclaration i4 at operators.ceylon (262:4-262:25)
    var i4$1028=(tmp$1029=f2$1027(),tmp$1029.i=tmp$1029.i.successor);
    var tmp$1029;
    
    //AttributeDeclaration x4 at operators.ceylon (263:4-263:25)
    var x4$1030=(tmp$1031=f2$1027(),tmp$1031.x=tmp$1031.x.successor,tmp$1031.x);
    var tmp$1031;
    $$$c2.check(i4$1028.equals((2)),$$$cl1.String("prefix increment 5",18));
    $$$c2.check(c1$1024.i.equals((2)),$$$cl1.String("prefix increment 6",18));
    $$$c2.check(x4$1030.equals((2)),$$$cl1.String("prefix increment 7",18));
    $$$c2.check(c1$1024.x.equals((2)),$$$cl1.String("prefix increment 8",18));
    $$$c2.check(i3$1025.equals((2)),$$$cl1.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (270:4-274:4)
    function f3$1032(){
        
        //AttributeDeclaration i2 at operators.ceylon (271:8-271:25)
        var i2$1033=(i1$1014=i1$1014.predecessor);
        $$$c2.check(i1$1014.equals((1)),$$$cl1.String("prefix decrement",16));
        $$$c2.check(i2$1033.equals((1)),$$$cl1.String("prefix decrement",16));
    };
    f3$1032();
    
    //AttributeDeclaration i5 at operators.ceylon (277:4-277:25)
    var i5$1034=(tmp$1035=f2$1027(),tmp$1035.i=tmp$1035.i.predecessor);
    var tmp$1035;
    $$$c2.check(i5$1034.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(c1$1024.i.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(i3$1025.equals((3)),$$$cl1.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (282:4-289:4)
    function f4$1036(){
        
        //AttributeDeclaration i2 at operators.ceylon (283:8-283:25)
        var i2$1037=(oldi1$1038=i1$1014,i1$1014=oldi1$1038.successor,oldi1$1038);
        var oldi1$1038;
        
        //AttributeDeclaration x2 at operators.ceylon (284:8-284:24)
        var x2$1039=(oldx$1040=getX$1012(),setX$1012(oldx$1040.successor),oldx$1040);
        var oldx$1040;
        $$$c2.check(i1$1014.equals((2)),$$$cl1.String("postfix increment 1",19));
        $$$c2.check(i2$1037.equals((1)),$$$cl1.String("postfix increment 2",19));
        $$$c2.check(getX$1012().equals((3)),$$$cl1.String("postfix increment 3",19));
        $$$c2.check(x2$1039.equals((2)),$$$cl1.String("postfix increment 4",19));
    };
    f4$1036();
    
    //AttributeDeclaration i6 at operators.ceylon (292:4-292:25)
    var i6$1041=(tmp$1042=f2$1027(),oldi$1043=tmp$1042.i,tmp$1042.i=oldi$1043.successor,oldi$1043);
    var tmp$1042,oldi$1043;
    
    //AttributeDeclaration x6 at operators.ceylon (293:4-293:25)
    var x6$1044=(tmp$1045=f2$1027(),oldx$1046=tmp$1045.x,tmp$1045.x=oldx$1046.successor,oldx$1046);
    var tmp$1045,oldx$1046;
    $$$c2.check(i6$1041.equals((1)),$$$cl1.String("postfix increment 5",19));
    $$$c2.check(c1$1024.i.equals((2)),$$$cl1.String("postfix increment 6",19));
    $$$c2.check(x6$1044.equals((2)),$$$cl1.String("postfix increment 7 ",20));
    $$$c2.check(c1$1024.x.equals((3)),$$$cl1.String("postfix increment 8 ",20));
    $$$c2.check(i3$1025.equals((5)),$$$cl1.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (300:4-304:4)
    function f5$1047(){
        
        //AttributeDeclaration i2 at operators.ceylon (301:8-301:25)
        var i2$1048=(oldi1$1049=i1$1014,i1$1014=oldi1$1049.predecessor,oldi1$1049);
        var oldi1$1049;
        $$$c2.check(i1$1014.equals((1)),$$$cl1.String("postfix decrement",17));
        $$$c2.check(i2$1048.equals((2)),$$$cl1.String("postfix decrement",17));
    };
    f5$1047();
    
    //AttributeDeclaration i7 at operators.ceylon (307:4-307:25)
    var i7$1050=(tmp$1051=f2$1027(),oldi$1052=tmp$1051.i,tmp$1051.i=oldi$1052.predecessor,oldi$1052);
    var tmp$1051,oldi$1052;
    $$$c2.check(i7$1050.equals((2)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(c1$1024.i.equals((1)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(i3$1025.equals((6)),$$$cl1.String("postfix decrement",17));
};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (313:0-364:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (314:4-314:27)
    var i1$1053=(1);
    var setI1$1053=function(i1$1054){return i1$1053=i1$1054;};
    
    //AttributeDeclaration x0 at operators.ceylon (315:4-315:27)
    var x0$1055=(1);
    var setX0$1055=function(x0$1056){return x0$1055=x0$1056;};
    
    //AttributeGetterDefinition x at operators.ceylon (316:4-316:27)
    var getX$1057=function(){
        return x0$1055;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (316:29-316:46)
    var setX$1057=function(x$1058){
        x0$1055=x$1058;
    };
    (i1$1053=i1$1053.plus((10)));
    (setX$1057(getX$1057().plus((10))),getX$1057());
    $$$c2.check(i1$1053.equals((11)),$$$cl1.String("+= operator 1",13));
    $$$c2.check(getX$1057().equals((11)),$$$cl1.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (322:4-322:36)
    var i2$1059=(i1$1053=i1$1053.plus((-(5))));
    var setI2$1059=function(i2$1060){return i2$1059=i2$1060;};
    
    //AttributeDeclaration x2 at operators.ceylon (323:4-323:35)
    var x2$1061=(setX$1057(getX$1057().plus((-(5)))),getX$1057());
    var setX2$1061=function(x2$1062){return x2$1061=x2$1062;};
    $$$c2.check(i2$1059.equals((6)),$$$cl1.String("+= operator 3",13));
    $$$c2.check(i1$1053.equals((6)),$$$cl1.String("+= operator 4",13));
    $$$c2.check(x2$1061.equals((6)),$$$cl1.String("+= operator 5",13));
    $$$c2.check(getX$1057().equals((6)),$$$cl1.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (329:4-333:4)
    function C1$1063($$c1$1063){
        $init$C1$1063();
        if ($$c1$1063===undefined)$$c1$1063=new C1$1063.$$;
        
        //AttributeDeclaration i at operators.ceylon (330:8-330:37)
        var i=(1);
        $$$cl1.defineAttr($$c1$1063,'i',function(){return i;},function(i$1064){return i=i$1064;});
        
        //AttributeDeclaration x0 at operators.ceylon (331:8-331:31)
        var x0$1065=(1);
        $$$cl1.defineAttr($$c1$1063,'x0$1065',function(){return x0$1065;},function(x0$1066){return x0$1065=x0$1066;});
        
        //AttributeGetterDefinition x at operators.ceylon (332:8-332:38)
        $$$cl1.defineAttr($$c1$1063,'x',function(){
            return x0$1065;
        },function(x$1067){
            x0$1065=x$1067;
        });
        return $$c1$1063;
    }
    function $init$C1$1063(){
        if (C1$1063.$$===undefined){
            $$$cl1.initTypeProto(C1$1063,'operators::testArithmeticAssignOperators.C1',$$$cl1.Basic);
        }
        return C1$1063;
    }
    $init$C1$1063();
    
    //AttributeDeclaration c1 at operators.ceylon (334:4-334:16)
    var c1$1068=C1$1063();
    
    //AttributeDeclaration i3 at operators.ceylon (335:4-335:27)
    var i3$1069=(0);
    var setI3$1069=function(i3$1070){return i3$1069=i3$1070;};
    
    //MethodDefinition f at operators.ceylon (336:4-339:4)
    function f$1071(){
        (i3$1069=i3$1069.successor);
        return c1$1068;
    };
    i2$1059=(tmp$1072=f$1071(),tmp$1072.i=tmp$1072.i.plus((11)));
    var tmp$1072;
    x2$1061=(tmp$1073=f$1071(),tmp$1073.x=tmp$1073.x.plus((11)),tmp$1073.x);
    var tmp$1073;
    $$$c2.check(i2$1059.equals((12)),$$$cl1.String("+= operator 7",13));
    $$$c2.check(c1$1068.i.equals((12)),$$$cl1.String("+= operator 8",13));
    $$$c2.check(x2$1061.equals((12)),$$$cl1.String("+= operator 9",13));
    $$$c2.check(c1$1068.x.equals((12)),$$$cl1.String("+= operator 10",14));
    $$$c2.check(i3$1069.equals((2)),$$$cl1.String("+= operator 11",14));
    i2$1059=(i1$1053=i1$1053.minus((14)));
    $$$c2.check(i1$1053.equals((-(8))),$$$cl1.String("-= operator",11));
    $$$c2.check(i2$1059.equals((-(8))),$$$cl1.String("-= operator",11));
    i2$1059=(i1$1053=i1$1053.times((-(3))));
    $$$c2.check(i1$1053.equals((24)),$$$cl1.String("*= operator",11));
    $$$c2.check(i2$1059.equals((24)),$$$cl1.String("*= operator",11));
    i2$1059=(i1$1053=i1$1053.divided((5)));
    $$$c2.check(i1$1053.equals((4)),$$$cl1.String("/= operator",11));
    $$$c2.check(i2$1059.equals((4)),$$$cl1.String("/= operator",11));
    i2$1059=(i1$1053=i1$1053.remainder((3)));
    $$$c2.check(i1$1053.equals((1)),$$$cl1.String("%= operator",11));
    $$$c2.check(i2$1059.equals((1)),$$$cl1.String("%= operator",11));
};

//MethodDefinition testAssignmentOperator at operators.ceylon (366:0-396:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (367:4-367:27)
    var i1$1074=(1);
    var setI1$1074=function(i1$1075){return i1$1074=i1$1075;};
    
    //AttributeDeclaration i2 at operators.ceylon (368:4-368:27)
    var i2$1076=(2);
    var setI2$1076=function(i2$1077){return i2$1076=i2$1077;};
    
    //AttributeDeclaration i3 at operators.ceylon (369:4-369:27)
    var i3$1078=(3);
    var setI3$1078=function(i3$1079){return i3$1078=i3$1079;};
    $$$c2.check((i1$1074=(i2$1076=i3$1078)).equals((3)),$$$cl1.String("assignment 1",12));
    $$$c2.check(i1$1074.equals((3)),$$$cl1.String("assignment 2",12));
    $$$c2.check(i2$1076.equals((3)),$$$cl1.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (374:4-374:28)
    var getX1$1080=function(){
        return i1$1074;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (374:30-374:51)
    var setX1$1080=function(x1$1081){
        i1$1074=x1$1081;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (375:4-375:28)
    var getX2$1082=function(){
        return i2$1076;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (375:30-375:51)
    var setX2$1082=function(x2$1083){
        i2$1076=x2$1083;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (376:4-376:28)
    var getX3$1084=function(){
        return i3$1078;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (376:30-376:51)
    var setX3$1084=function(x3$1085){
        i3$1078=x3$1085;
    };
    i1$1074=(1);
    i2$1076=(2);
    $$$c2.check((setX1$1080((setX2$1082(getX3$1084()),getX2$1082())),getX1$1080()).equals((3)),$$$cl1.String("assignment 4",12));
    $$$c2.check(getX1$1080().equals((3)),$$$cl1.String("assignment 5",12));
    $$$c2.check(getX2$1082().equals((3)),$$$cl1.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (383:4-387:4)
    function C$1086($$c$1086){
        $init$C$1086();
        if ($$c$1086===undefined)$$c$1086=new C$1086.$$;
        
        //AttributeDeclaration i at operators.ceylon (384:8-384:37)
        var i=(1);
        $$$cl1.defineAttr($$c$1086,'i',function(){return i;},function(i$1087){return i=i$1087;});
        
        //AttributeDeclaration x0 at operators.ceylon (385:8-385:31)
        var x0$1088=(1);
        $$$cl1.defineAttr($$c$1086,'x0$1088',function(){return x0$1088;},function(x0$1089){return x0$1088=x0$1089;});
        
        //AttributeGetterDefinition x at operators.ceylon (386:8-386:38)
        $$$cl1.defineAttr($$c$1086,'x',function(){
            return x0$1088;
        },function(x$1090){
            x0$1088=x$1090;
        });
        return $$c$1086;
    }
    function $init$C$1086(){
        if (C$1086.$$===undefined){
            $$$cl1.initTypeProto(C$1086,'operators::testAssignmentOperator.C',$$$cl1.Basic);
        }
        return C$1086;
    }
    $init$C$1086();
    
    //AttributeDeclaration o1 at operators.ceylon (388:4-388:14)
    var o1$1091=C$1086();
    
    //AttributeDeclaration o2 at operators.ceylon (389:4-389:14)
    var o2$1092=C$1086();
    $$$c2.check((o1$1091.i=(o2$1092.i=(3))).equals((3)),$$$cl1.String("assignment 7",12));
    $$$c2.check(o1$1091.i.equals((3)),$$$cl1.String("assignment 8",12));
    $$$c2.check(o2$1092.i.equals((3)),$$$cl1.String("assignment 9",12));
    $$$c2.check((tmp$1093=o1$1091,tmp$1093.x=(tmp$1094=o2$1092,tmp$1094.x=(3),tmp$1094.x),tmp$1093.x).equals((3)),$$$cl1.String("assignment 10",13));
    var tmp$1093,tmp$1094;
    $$$c2.check(o1$1091.x.equals((3)),$$$cl1.String("assignment 11",13));
    $$$c2.check(o2$1092.x.equals((3)),$$$cl1.String("assignment 12",13));
};

//MethodDefinition testSegments at operators.ceylon (398:0-425:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (399:4-399:97)
    var seq$1095=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.Tuple($$$cl1.String("nine",4),$$$cl1.Tuple($$$cl1.String("ten",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    $$$c2.check(seq$1095.segment((1),(2)).equals($$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[1:2] ",9),seq$1095.segment((1),(2)).string]).string);
    $$$c2.check(seq$1095.segment((3),(5)).equals($$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[3:5] ",9),seq$1095.segment((3),(5)).string]).string);
    $$$c2.check($$$cl1.String("test",4).segment((1),(2)).equals($$$cl1.String("es",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("test[1:2] ",10),$$$cl1.String("test",4).segment((1),(2)).string]).string);
    $$$c2.check($$$cl1.String("hello",5).segment((2),(2)).equals($$$cl1.String("ll",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("hello[2:2] ",11),$$$cl1.String("hello",5).segment((2),(2)).string]).string);
    
    //AttributeDeclaration s2 at operators.ceylon (404:4-404:18)
    var s2$1096=(function(){var tmpvar$1097=(3);
    if (tmpvar$1097>0){
    var tmpvar$1098=(0);
    var tmpvar$1099=tmpvar$1098;
    for (var i=1; i<tmpvar$1097; i++){tmpvar$1099=tmpvar$1099.successor;}
    return $$$cl1.Range(tmpvar$1098,tmpvar$1099)
    }else return $$$cl1.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (405:4-405:18)
    var s3$1100=(function(){var tmpvar$1101=(5);
    if (tmpvar$1101>0){
    var tmpvar$1102=(2);
    var tmpvar$1103=tmpvar$1102;
    for (var i=1; i<tmpvar$1101; i++){tmpvar$1103=tmpvar$1103.successor;}
    return $$$cl1.Range(tmpvar$1102,tmpvar$1103)
    }else return $$$cl1.getEmpty();}());
    $$$c2.check(s2$1096.size.equals((3)),$$$cl1.String("0:3 [1]",7));
    var x$1104;
    if((x$1104=s2$1096.get((0)))!==null){
        $$$c2.check(x$1104.equals((0)),$$$cl1.String("0:3 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [2]",7));
    }
    var x$1105;
    if((x$1105=s2$1096.get((2)))!==null){
        $$$c2.check(x$1105.equals((2)),$$$cl1.String("0:3 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [3]",7));
    }
    $$$c2.check(s3$1100.size.equals((5)),$$$cl1.String("2:5 [1]",7));
    var x$1106;
    if((x$1106=s3$1100.get((0)))!==null){
        $$$c2.check(x$1106.equals((2)),$$$cl1.String("2:5 [1]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [1]",7));
    }
    var x$1107;
    if((x$1107=s3$1100.get((2)))!==null){
        $$$c2.check(x$1107.equals((4)),$$$cl1.String("2:5 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [2]",7));
    }
    var x$1108;
    if((x$1108=s3$1100.get((4)))!==null){
        $$$c2.check(x$1108.equals((6)),$$$cl1.String("2:5 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [3]",7));
    }
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1109=(0);
    if (tmpvar$1109>0){
    var tmpvar$1110=(1);
    var tmpvar$1111=tmpvar$1110;
    for (var i=1; i<tmpvar$1109; i++){tmpvar$1111=tmpvar$1111.successor;}
    return $$$cl1.Range(tmpvar$1110,tmpvar$1111)
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:0 empty",9));
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1112=(-(1));
    if (tmpvar$1112>0){
    var tmpvar$1113=(1);
    var tmpvar$1114=tmpvar$1113;
    for (var i=1; i<tmpvar$1112; i++){tmpvar$1114=tmpvar$1114.successor;}
    return $$$cl1.Range(tmpvar$1113,tmpvar$1114)
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:-1 empty",10));
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
    $$$c2.results();
}
exports.test=test;
exports.$$metamodel$$=$$metamodel$$;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
