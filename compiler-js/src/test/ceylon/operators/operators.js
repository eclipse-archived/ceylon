(function(define) { define(function(require, exports, module) {
//!!!METAMODEL:{"$mod-deps":["ceylon.language\/0.6","check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}}
var $$$cl1=require('ceylon/language/0.6/ceylon.language-0.6');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$921=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$922=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$923=$$$cl1.String("hola",4).iterator();
        var c$924=$$$cl1.getFinished();
        var next$c$924=function(){return c$924=it$923.next();}
        next$c$924();
        return function(){
            if(c$924!==$$$cl1.getFinished()){
                var c$924$925=c$924;
                var tmpvar$926=c$924$925;
                next$c$924();
                return tmpvar$926;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$927=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$928=$$$cl1.String("hola",4).iterator();
        var c$929=$$$cl1.getFinished();
        var next$c$929=function(){return c$929=it$928.next();}
        next$c$929();
        return function(){
            if(c$929!==$$$cl1.getFinished()){
                var c$929$930=c$929;
                var tmpvar$931=c$929$930;
                next$c$929();
                return tmpvar$931;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}}).sequence;
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$932=[(0)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).chain(seq$921,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$933=[$$$cl1.Character(65)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}).chain(lcomp$922,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$934=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$935=$$$cl1.Tuple((0),seq$921,{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.className(seq$921).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{1,2,3} is not a Tuple but a ",29),$$$cl1.className(seq$921).string]).string);
    $$$c2.check((!$$$cl1.className(lcomp$922).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("lazy comprehension is a Tuple ",30),$$$cl1.className(lcomp$922).string]).string);
    $$$c2.check($$$cl1.className(ecomp$927).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("eager comprehension is not a Tuple but a ",41),$$$cl1.className(ecomp$927).string]).string);
    $$$c2.check((!$$$cl1.className(s2$932).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} is a Tuple ",20),$$$cl1.className(s2$932).string]).string);
    $$$c2.check((!$$$cl1.className(s3$933).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{x,*iter} is a Tuple ",21),$$$cl1.className(s3$933).string]).string);
    $$$c2.check($$$cl1.className(t1$934).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[1,2,3] is not a Tuple but a ",29),$$$cl1.className(t1$934).string]).string);
    $$$c2.check($$$cl1.className(t2$935).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[0,*seq] is not a Tuple but a ",30),$$$cl1.className(t2$935).string]).string);
    $$$c2.check(seq$921.equals(t1$934),$$$cl1.String("{1,2,3} != [1,2,3]",18));
    $$$c2.check((!$$$cl1.className(t2$935).equals($$$cl1.className(s2$932))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} != [0,*seq] ",21),$$$cl1.className(t2$935).string,$$$cl1.String(" vs",3),$$$cl1.className(s2$932).string]).string);
    $$$c2.check(seq$921.size.equals((3)),$$$cl1.String("seq.size!=3",11));
    $$$c2.check(lcomp$922.sequence.size.equals((4)),$$$cl1.String("lcomp.size!=4",13));
    $$$c2.check(ecomp$927.size.equals((4)),$$$cl1.String("ecomp.size!=4",13));
    $$$c2.check(s2$932.size.equals((4)),$$$cl1.String("s2.size!=4",10));
    $$$c2.check(s3$933.sequence.size.equals((5)),$$$cl1.String("s3.size!=5",10));
    $$$c2.check(t1$934.size.equals((3)),$$$cl1.String("t1.size!=3",10));
    $$$c2.check(t2$935.size.equals((4)),$$$cl1.String("t2.size!=4",10));
    $$$c2.check((!$$$cl1.className(lcomp$922).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*comp} is not Tuple but ",25),$$$cl1.className(lcomp$922).string]).string);
    $$$c2.check($$$cl1.className(ecomp$927).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*ecomp} is not Tuple but ",26),$$$cl1.className(ecomp$927).string]).string);
    $$$c2.check($$$cl1.className(seq$921).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*seq} is not Tuple but ",24),$$$cl1.className(seq$921).string]).string);
};testEnumerations.$$metamodel$$={$nm:'testEnumerations',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testEnumerations.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$936=(-(4));
    var setI1$936=function(i1$937){return i1$936=i1$937;};
    i1$936=(-i1$936);
    $$$c2.check(i1$936.equals((4)),$$$cl1.String("negation",8));
    i1$936=(+(-(987654)));
    $$$c2.check(i1$936.equals((-(987654))),$$$cl1.String("positive",8));
    i1$936=(+(0));
    $$$c2.check(i1$936.equals((0)),$$$cl1.String("+0=0",4));
    i1$936=(-(0));
    $$$c2.check(i1$936.equals((0)),$$$cl1.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$938=(123).plus((456));
    var setI2$938=function(i2$939){return i2$938=i2$939;};
    $$$c2.check(i2$938.equals((579)),$$$cl1.String("addition",8));
    i1$936=i2$938.minus((16));
    $$$c2.check(i1$936.equals((563)),$$$cl1.String("subtraction",11));
    i2$938=(-i1$936).plus(i2$938).minus((1));
    $$$c2.check(i2$938.equals((15)),$$$cl1.String("-i1+i2-1",8));
    i1$936=(3).times((7));
    $$$c2.check(i1$936.equals((21)),$$$cl1.String("multiplication",14));
    i2$938=i1$936.times((2));
    $$$c2.check(i2$938.equals((42)),$$$cl1.String("multiplication",14));
    i2$938=(17).divided((4));
    $$$c2.check(i2$938.equals((4)),$$$cl1.String("integer division",16));
    i1$936=i2$938.times((516)).divided((-i1$936));
    $$$c2.check(i1$936.equals((-(98))),$$$cl1.String("i2*516/-i1",10));
    i1$936=(15).remainder((4));
    $$$c2.check(i1$936.equals((3)),$$$cl1.String("modulo",6));
    i2$938=(312).remainder((12));
    $$$c2.check(i2$938.equals((0)),$$$cl1.String("modulo",6));
    i1$936=(2).power((10));
    $$$c2.check(i1$936.equals((1024)),$$$cl1.String("power",5));
    i2$938=(10).power((6));
    $$$c2.check(i2$938.equals((1000000)),$$$cl1.String("power",5));
};testIntegerOperators.$$metamodel$$={$nm:'testIntegerOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testIntegerOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$940=$$$cl1.Float(4.2).negativeValue;
    var setF1$940=function(f1$941){return f1$940=f1$941;};
    f1$940=f1$940.negativeValue;
    $$$c2.check(f1$940.equals($$$cl1.Float(4.2)),$$$cl1.String("negation",8));
    f1$940=(+$$$cl1.Float(987654.9925567).negativeValue);
    $$$c2.check(f1$940.equals($$$cl1.Float(987654.9925567).negativeValue),$$$cl1.String("positive",8));
    f1$940=(+$$$cl1.Float(0.0));
    $$$c2.check(f1$940.equals($$$cl1.Float(0.0)),$$$cl1.String("+0.0=0.0",8));
    f1$940=$$$cl1.Float(0.0).negativeValue;
    $$$c2.check(f1$940.equals($$$cl1.Float(0.0)),$$$cl1.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$942=$$$cl1.Float(3.14159265).plus($$$cl1.Float(456.0));
    var setF2$942=function(f2$943){return f2$942=f2$943;};
    $$$c2.check(f2$942.equals($$$cl1.Float(459.14159265)),$$$cl1.String("addition",8));
    f1$940=f2$942.minus($$$cl1.Float(0.0016));
    $$$c2.check(f1$940.equals($$$cl1.Float(459.13999265)),$$$cl1.String("subtraction",11));
    f2$942=f1$940.negativeValue.plus(f2$942).minus($$$cl1.Float(1.2));
    $$$c2.check(f2$942.equals($$$cl1.Float(1.1984000000000037).negativeValue),$$$cl1.String("-f1+f2-1.2",10));
    f1$940=$$$cl1.Float(3.0).times($$$cl1.Float(0.79));
    $$$c2.check(f1$940.equals($$$cl1.Float(2.37)),$$$cl1.String("multiplication",14));
    f2$942=f1$940.times($$$cl1.Float(2.0e13));
    $$$c2.check(f2$942.equals($$$cl1.Float(47400000000000.0)),$$$cl1.String("multiplication",14));
    f2$942=$$$cl1.Float(17.1).divided($$$cl1.Float(4.0E-18));
    $$$c2.check(f2$942.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("division",8));
    f1$940=f2$942.times($$$cl1.Float(51.6e2)).divided(f1$940.negativeValue);
    $$$c2.check(f2$942.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("f2*51.6e2/-f1",13));
    f1$940=$$$cl1.Float(150.0).power($$$cl1.Float(0.5));
    $$$c2.check(f1$940.equals($$$cl1.Float(12.24744871391589)),$$$cl1.String("power",5));
};testFloatOperators.$$metamodel$$={$nm:'testFloatOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testFloatOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

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
    OpTest1.$$.$$metamodel$$={$nm:'OpTest1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$944=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$945=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$946=(o1$944===o2$945);
    var setB1$946=function(b1$947){return b1$946=b1$947;};
    $$$c2.check((!b1$946),$$$cl1.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$948=(o1$944===o1$944);
    var setB2$948=function(b2$949){return b2$948=b2$949;};
    $$$c2.check(b2$948,$$$cl1.String("identity",8));
    b1$946=o1$944.equals(o2$945);
    $$$c2.check((!b1$946),$$$cl1.String("equals",6));
    b2$948=o1$944.equals(o1$944);
    $$$c2.check(b2$948,$$$cl1.String("equals",6));
    b1$946=(1).equals((2));
    $$$c2.check((!b1$946),$$$cl1.String("equals",6));
    b2$948=(!(1).equals((2)));
    $$$c2.check(b2$948,$$$cl1.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$950=(!b2$948);
    var setB3$950=function(b3$951){return b3$950=b3$951;};
    $$$c2.check((!b3$950),$$$cl1.String("not",3));
    b1$946=(true&&false);
    $$$c2.check((!b1$946),$$$cl1.String("and",3));
    b2$948=(b1$946&&true);
    $$$c2.check((!b2$948),$$$cl1.String("and",3));
    b3$950=(true&&true);
    $$$c2.check(b3$950,$$$cl1.String("and",3));
    b1$946=(true||false);
    $$$c2.check(b1$946,$$$cl1.String("or",2));
    b2$948=(false||b1$946);
    $$$c2.check(b2$948,$$$cl1.String("or",2));
    b3$950=(false||false);
    $$$c2.check((!b3$950),$$$cl1.String("or",2));
};testBooleanOperators.$$metamodel$$={$nm:'testBooleanOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testBooleanOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-152:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$952=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4));
    $$$c2.check(c1$952.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$953=$$$cl1.String("str2",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c2$953.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$954=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c3$954.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$955=$$$cl1.String("",0).compare($$$cl1.String("",0));
    $$$c2.check(c4$955.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$956=$$$cl1.String("str1",4).compare($$$cl1.String("",0));
    $$$c2.check(c5$956.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$957=$$$cl1.String("",0).compare($$$cl1.String("str2",4));
    $$$c2.check(c6$957.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$958=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getSmaller());
    var setB1$958=function(b1$959){return b1$958=b1$959;};
    $$$c2.check(b1$958,$$$cl1.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$960=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getLarger());
    var setB2$960=function(b2$961){return b2$960=b2$961;};
    $$$c2.check((!b2$960),$$$cl1.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$962=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getLarger());
    var setB3$962=function(b3$963){return b3$962=b3$963;};
    $$$c2.check(b3$962,$$$cl1.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$964=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getSmaller());
    var setB4$964=function(b4$965){return b4$964=b4$965;};
    $$$c2.check((!b4$964),$$$cl1.String("large as",8));
    b1$958=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getSmaller());
    $$$c2.check((!b1$958),$$$cl1.String("smaller",7));
    b2$960=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getLarger());
    $$$c2.check((!b2$960),$$$cl1.String("larger",6));
    b3$962=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getLarger());
    $$$c2.check(b3$962,$$$cl1.String("small as",8));
    b4$964=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getSmaller());
    $$$c2.check(b4$964,$$$cl1.String("large as",8));
    
    //AttributeDeclaration a at operators.ceylon (140:4-140:15)
    var a$966=(0);
    
    //AttributeDeclaration c at operators.ceylon (141:4-141:16)
    var c$967=(10);
    $$$c2.check((tmpvar$968=(5),tmpvar$968.compare(a$966)===$$$cl1.getLarger()&&tmpvar$968.compare(c$967)===$$$cl1.getSmaller()),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<5<",3),c$967.string]).string);
    $$$c2.check((tmpvar$969=(0),tmpvar$969.compare(a$966)!==$$$cl1.getSmaller()&&tmpvar$969.compare(c$967)===$$$cl1.getSmaller()),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<=0<",4),c$967.string]).string);
    $$$c2.check((tmpvar$970=(10),tmpvar$970.compare(a$966)===$$$cl1.getLarger()&&tmpvar$970.compare(c$967)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<10<=",5),c$967.string]).string);
    $$$c2.check((tmpvar$971=(0),tmpvar$971.compare(a$966)!==$$$cl1.getSmaller()&&tmpvar$971.compare(c$967)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<=0<=",5),c$967.string]).string);
    $$$c2.check((tmpvar$972=(10),tmpvar$972.compare(a$966)!==$$$cl1.getSmaller()&&tmpvar$972.compare(c$967)!==$$$cl1.getLarger()),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<=10<=",6),c$967.string]).string);
    $$$c2.check((!(tmpvar$973=(15),tmpvar$973.compare(a$966)===$$$cl1.getLarger()&&tmpvar$973.compare(c$967)===$$$cl1.getSmaller())),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<15<",4),c$967.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$974=(10),tmpvar$974.compare(a$966)!==$$$cl1.getSmaller()&&tmpvar$974.compare(c$967)===$$$cl1.getSmaller())),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<=10<",5),c$967.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$975=(0),tmpvar$975.compare(a$966)===$$$cl1.getLarger()&&tmpvar$975.compare(c$967)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<0<=",4),c$967.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$976=(11),tmpvar$976.compare(a$966)!==$$$cl1.getSmaller()&&tmpvar$976.compare(c$967)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<=11<=",6),c$967.string,$$$cl1.String(" WTF",4)]).string);
    $$$c2.check((!(tmpvar$977=(-(1)),tmpvar$977.compare(a$966)!==$$$cl1.getSmaller()&&tmpvar$977.compare(c$967)!==$$$cl1.getLarger())),$$$cl1.StringBuilder().appendAll([a$966.string,$$$cl1.String("<=-1<=",6),c$967.string,$$$cl1.String(" WTF",4)]).string);
};testComparisonOperators.$$metamodel$$={$nm:'testComparisonOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testComparisonOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testOtherOperators at operators.ceylon (154:0-166:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (155:4-155:42)
    var entry$978=$$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}});
    $$$c2.check(entry$978.key.equals((47)),$$$cl1.String("entry key",9));
    $$$c2.check(entry$978.item.equals($$$cl1.String("hi there",8)),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (158:4-158:30)
    var entry2$979=$$$cl1.Entry(true,entry$978,{Key:{t:$$$cl1.true$809},Item:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}}}});
    $$$c2.check(entry2$979.key.equals(true),$$$cl1.String("entry key",9));
    $$$c2.check(entry2$979.item.equals($$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}})),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (162:4-162:41)
    var s1$980=(opt$981=(true?$$$cl1.String("ok",2):null),opt$981!==null?opt$981:$$$cl1.String("noo",3));
    var opt$981;
    $$$c2.check(s1$980.equals($$$cl1.String("ok",2)),$$$cl1.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (164:4-164:47)
    var s2$982=(opt$983=(false?$$$cl1.String("what?",5):null),opt$983!==null?opt$983:$$$cl1.String("great",5));
    var opt$983;
    $$$c2.check(s2$982.equals($$$cl1.String("great",5)),$$$cl1.String("then/else 2",11));
};testOtherOperators.$$metamodel$$={$nm:'testOtherOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testOtherOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testCollectionOperators at operators.ceylon (168:0-180:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (169:4-169:33)
    var seq1$984=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (170:4-170:23)
    var s1$985=seq1$984.get((0));
    $$$c2.check(s1$985.equals($$$cl1.String("one",3)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (172:4-172:28)
    var s2$986=seq1$984.get((2));
    $$$c2.check((!$$$cl1.exists(s2$986)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (174:4-174:29)
    var s3$987=seq1$984.get((-(1)));
    $$$c2.check((!$$$cl1.exists(s3$987)),$$$cl1.String("lookup",6));
};testCollectionOperators.$$metamodel$$={$nm:'testCollectionOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testCollectionOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//ClassDefinition NullsafeTest at operators.ceylon (182:0-187:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    
    //MethodDefinition f at operators.ceylon (183:4-183:33)
    function f(){
        return (1);
    }
    $$nullsafeTest.f=f;
    f.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:$$$cl1.Integer},$ps:[]};//f.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Integer}};
    
    //MethodDefinition f2 at operators.ceylon (184:4-186:4)
    function f2(x$988){
        return x$988();
    }
    $$nullsafeTest.f2=f2;
    f2.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[{$nm:'x',$mt:'prm',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}}]};//f2.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},Element:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}}},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}};
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl1.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl1.Basic);
    }
    NullsafeTest.$$.$$metamodel$$={$nm:'NullsafeTest',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (189:0-191:0)
function nullsafeTest(f$989){
    return f$989();
};nullsafeTest.$$metamodel$$={$nm:'nullsafeTest',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[{$nm:'f',$mt:'prm',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}}]};//nullsafeTest.$$targs$$={Arguments:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},Element:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}}},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}};

//MethodDefinition testNullsafeOperators at operators.ceylon (193:0-234:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (194:4-194:27)
    var seq$990=$$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (195:4-195:34)
    var s1$991=(opt$992=seq$990.get((0)),opt$992!==null?opt$992:$$$cl1.String("null",4));
    var opt$992;
    $$$c2.check(s1$991.equals($$$cl1.String("hi",2)),$$$cl1.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (197:4-197:34)
    var s2$993=(opt$994=seq$990.get((1)),opt$994!==null?opt$994:$$$cl1.String("null",4));
    var opt$994;
    $$$c2.check(s2$993.equals($$$cl1.String("null",4)),$$$cl1.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (200:4-200:21)
    var s3$995=null;
    
    //AttributeDeclaration s4 at operators.ceylon (201:4-201:23)
    var s4$996=$$$cl1.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (202:4-202:42)
    var s5$997=(opt$998=(opt$999=s3$995,opt$999!==null?opt$999.uppercased:null),opt$998!==null?opt$998:$$$cl1.String("null",4));
    var opt$998,opt$999;
    
    //AttributeDeclaration s6 at operators.ceylon (203:4-203:42)
    var s6$1000=(opt$1001=(opt$1002=s4$996,opt$1002!==null?opt$1002.uppercased:null),opt$1001!==null?opt$1001:$$$cl1.String("null",4));
    var opt$1001,opt$1002;
    $$$c2.check(s5$997.equals($$$cl1.String("null",4)),$$$cl1.String("nullsafe member 1",17));
    $$$c2.check(s6$1000.equals($$$cl1.String("TEST",4)),$$$cl1.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (206:4-206:28)
    var obj$1003=null;
    
    //AttributeDeclaration i at operators.ceylon (207:4-207:25)
    var i$1004=(opt$1005=obj$1003,$$$cl1.JsCallable(opt$1005,opt$1005!==null?opt$1005.f:null))();
    var opt$1005;
    $$$c2.check((!$$$cl1.exists(i$1004)),$$$cl1.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (209:4-209:37)
    var f2$1006=$$$cl1.$JsCallable((opt$1007=obj$1003,$$$cl1.JsCallable(opt$1007,opt$1007!==null?opt$1007.f:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}});
    var opt$1007;
    $$$c2.check((!$$$cl1.exists(nullsafeTest($$$cl1.$JsCallable(f2$1006,[],{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}})))),$$$cl1.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (211:4-211:38)
    var f3$1008=$$$cl1.$JsCallable((opt$1009=obj$1003,$$$cl1.JsCallable(opt$1009,opt$1009!==null?opt$1009.f:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}});
    var opt$1009;
    $$$c2.check($$$cl1.exists(f3$1008),$$$cl1.String("nullsafe method ref 2",21));
    (opt$1010=obj$1003,$$$cl1.JsCallable(opt$1010,opt$1010!==null?opt$1010.f:null))();
    var opt$1010;
    $$$c2.check((!$$$cl1.exists((opt$1011=obj$1003,$$$cl1.JsCallable(opt$1011,opt$1011!==null?opt$1011.f:null))())),$$$cl1.String("nullsafe simple call",20));
    var opt$1011;
    
    //MethodDefinition getNullsafe at operators.ceylon (215:4-215:46)
    function getNullsafe$1012(){
        return obj$1003;
    };getNullsafe$1012.$$metamodel$$={$nm:'getNullsafe',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:NullsafeTest}]},$ps:[]};//getNullsafe$1012.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:NullsafeTest}]}};
    
    //MethodDeclaration f4 at operators.ceylon (216:4-216:39)
    var f4$1013=function (){
        return (opt$1014=getNullsafe$1012(),$$$cl1.JsCallable(opt$1014,opt$1014!==null?opt$1014.f:null))();
    };
    f4$1013$$metamodel$$={$nm:'f4',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[]};
    var opt$1014;
    
    //AttributeDeclaration result_f4 at operators.ceylon (217:4-217:29)
    var result_f4$1015=f4$1013();
    $$$c2.check((!$$$cl1.exists(result_f4$1015)),$$$cl1.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (219:4-219:36)
    var i2$1016=(opt$1017=getNullsafe$1012(),$$$cl1.JsCallable(opt$1017,opt$1017!==null?opt$1017.f:null))();
    var opt$1017;
    $$$c2.check((!$$$cl1.exists(i2$1016)),$$$cl1.String("nullsafe invoke 3",17));
    $$$c2.check((!$$$cl1.exists(NullsafeTest().f2($$$cl1.$JsCallable((opt$1018=getNullsafe$1012(),$$$cl1.JsCallable(opt$1018,opt$1018!==null?opt$1018.f:null)),[],{Arguments:{t:$$$cl1.Empty},Return:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]}})))),$$$cl1.String("nullsafe method ref 3",21));
    var opt$1018;
    
    //AttributeDeclaration obj2 at operators.ceylon (222:4-222:39)
    var obj2$1019=NullsafeTest();
    var i3$1020;
    if((i3$1020=(opt$1021=obj2$1019,$$$cl1.JsCallable(opt$1021,opt$1021!==null?opt$1021.f:null))())!==null){
        $$$c2.check(i3$1020.equals((1)),$$$cl1.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe invoke 4 (null)",24));
    }
    var opt$1021;
    
    //MethodDeclaration obj2_f at operators.ceylon (228:4-228:34)
    var obj2_f$1022=function (){
        return (opt$1023=obj2$1019,$$$cl1.JsCallable(opt$1023,opt$1023!==null?opt$1023.f:null))();
    };
    obj2_f$1022$$metamodel$$={$nm:'obj2_f',$mt:'mthd',$t:{ t:'u', l:[{t:$$$cl1.Null},{t:$$$cl1.Integer}]},$ps:[]};
    var opt$1023;
    var i3$1024;
    if((i3$1024=obj2_f$1022())!==null){
        $$$c2.check(i3$1024.equals((1)),$$$cl1.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe method ref 4 (null)",28));
    }
};testNullsafeOperators.$$metamodel$$={$nm:'testNullsafeOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testNullsafeOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testIncDecOperators at operators.ceylon (236:0-311:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (237:4-237:27)
    var x0$1025=(1);
    var setX0$1025=function(x0$1026){return x0$1025=x0$1026;};
    
    //AttributeGetterDefinition x at operators.ceylon (238:4-238:27)
    var getX$1027=function(){
        return x0$1025;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (238:29-238:48)
    var setX$1027=function(x$1028){
        x0$1025=x$1028;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (240:4-240:27)
    var i1$1029=(1);
    var setI1$1029=function(i1$1030){return i1$1029=i1$1030;};
    
    //MethodDefinition f1 at operators.ceylon (241:4-248:4)
    function f1$1031(){
        
        //AttributeDeclaration i2 at operators.ceylon (242:8-242:25)
        var i2$1032=(i1$1029=i1$1029.successor);
        
        //AttributeDeclaration x2 at operators.ceylon (243:8-243:24)
        var x2$1033=(setX$1027(getX$1027().successor),getX$1027());
        $$$c2.check(i1$1029.equals((2)),$$$cl1.String("prefix increment 1",18));
        $$$c2.check(i2$1032.equals((2)),$$$cl1.String("prefix increment 2",18));
        $$$c2.check(getX$1027().equals((2)),$$$cl1.String("prefix increment 3",18));
        $$$c2.check(x2$1033.equals((2)),$$$cl1.String("prefix increment 4",18));
    };f1$1031.$$metamodel$$={$nm:'f1',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//f1$1031.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    f1$1031();
    
    //ClassDefinition C1 at operators.ceylon (251:4-255:4)
    function C1$1034($$c1$1034){
        $init$C1$1034();
        if ($$c1$1034===undefined)$$c1$1034=new C1$1034.$$;
        
        //AttributeDeclaration i at operators.ceylon (252:8-252:37)
        var i=(1);
        $$$cl1.defineAttr($$c1$1034,'i',function(){return i;},function(i$1035){return i=i$1035;});
        
        //AttributeDeclaration x0 at operators.ceylon (253:8-253:31)
        var x0$1036=(1);
        $$$cl1.defineAttr($$c1$1034,'x0$1036',function(){return x0$1036;},function(x0$1037){return x0$1036=x0$1037;});
        
        //AttributeGetterDefinition x at operators.ceylon (254:8-254:38)
        $$$cl1.defineAttr($$c1$1034,'x',function(){
            return x0$1036;
        },function(x$1038){
            x0$1036=x$1038;
        });
        return $$c1$1034;
    }
    function $init$C1$1034(){
        if (C1$1034.$$===undefined){
            $$$cl1.initTypeProto(C1$1034,'operators::testIncDecOperators.C1',$$$cl1.Basic);
        }
        C1$1034.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return C1$1034;
    }
    $init$C1$1034();
    
    //AttributeDeclaration c1 at operators.ceylon (256:4-256:16)
    var c1$1039=C1$1034();
    
    //AttributeDeclaration i3 at operators.ceylon (257:4-257:27)
    var i3$1040=(0);
    var setI3$1040=function(i3$1041){return i3$1040=i3$1041;};
    
    //MethodDefinition f2 at operators.ceylon (258:4-261:4)
    function f2$1042(){
        (i3$1040=i3$1040.successor);
        return c1$1039;
    };f2$1042.$$metamodel$$={$nm:'f2',$mt:'mthd',$t:{t:C1$1034},$ps:[]};//f2$1042.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:C1$1034}};
    
    //AttributeDeclaration i4 at operators.ceylon (262:4-262:25)
    var i4$1043=(tmp$1044=f2$1042(),tmp$1044.i=tmp$1044.i.successor);
    var tmp$1044;
    
    //AttributeDeclaration x4 at operators.ceylon (263:4-263:25)
    var x4$1045=(tmp$1046=f2$1042(),tmp$1046.x=tmp$1046.x.successor,tmp$1046.x);
    var tmp$1046;
    $$$c2.check(i4$1043.equals((2)),$$$cl1.String("prefix increment 5",18));
    $$$c2.check(c1$1039.i.equals((2)),$$$cl1.String("prefix increment 6",18));
    $$$c2.check(x4$1045.equals((2)),$$$cl1.String("prefix increment 7",18));
    $$$c2.check(c1$1039.x.equals((2)),$$$cl1.String("prefix increment 8",18));
    $$$c2.check(i3$1040.equals((2)),$$$cl1.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (270:4-274:4)
    function f3$1047(){
        
        //AttributeDeclaration i2 at operators.ceylon (271:8-271:25)
        var i2$1048=(i1$1029=i1$1029.predecessor);
        $$$c2.check(i1$1029.equals((1)),$$$cl1.String("prefix decrement",16));
        $$$c2.check(i2$1048.equals((1)),$$$cl1.String("prefix decrement",16));
    };f3$1047.$$metamodel$$={$nm:'f3',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//f3$1047.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    f3$1047();
    
    //AttributeDeclaration i5 at operators.ceylon (277:4-277:25)
    var i5$1049=(tmp$1050=f2$1042(),tmp$1050.i=tmp$1050.i.predecessor);
    var tmp$1050;
    $$$c2.check(i5$1049.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(c1$1039.i.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(i3$1040.equals((3)),$$$cl1.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (282:4-289:4)
    function f4$1051(){
        
        //AttributeDeclaration i2 at operators.ceylon (283:8-283:25)
        var i2$1052=(oldi1$1053=i1$1029,i1$1029=oldi1$1053.successor,oldi1$1053);
        var oldi1$1053;
        
        //AttributeDeclaration x2 at operators.ceylon (284:8-284:24)
        var x2$1054=(oldx$1055=getX$1027(),setX$1027(oldx$1055.successor),oldx$1055);
        var oldx$1055;
        $$$c2.check(i1$1029.equals((2)),$$$cl1.String("postfix increment 1",19));
        $$$c2.check(i2$1052.equals((1)),$$$cl1.String("postfix increment 2",19));
        $$$c2.check(getX$1027().equals((3)),$$$cl1.String("postfix increment 3",19));
        $$$c2.check(x2$1054.equals((2)),$$$cl1.String("postfix increment 4",19));
    };f4$1051.$$metamodel$$={$nm:'f4',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//f4$1051.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    f4$1051();
    
    //AttributeDeclaration i6 at operators.ceylon (292:4-292:25)
    var i6$1056=(tmp$1057=f2$1042(),oldi$1058=tmp$1057.i,tmp$1057.i=oldi$1058.successor,oldi$1058);
    var tmp$1057,oldi$1058;
    
    //AttributeDeclaration x6 at operators.ceylon (293:4-293:25)
    var x6$1059=(tmp$1060=f2$1042(),oldx$1061=tmp$1060.x,tmp$1060.x=oldx$1061.successor,oldx$1061);
    var tmp$1060,oldx$1061;
    $$$c2.check(i6$1056.equals((1)),$$$cl1.String("postfix increment 5",19));
    $$$c2.check(c1$1039.i.equals((2)),$$$cl1.String("postfix increment 6",19));
    $$$c2.check(x6$1059.equals((2)),$$$cl1.String("postfix increment 7 ",20));
    $$$c2.check(c1$1039.x.equals((3)),$$$cl1.String("postfix increment 8 ",20));
    $$$c2.check(i3$1040.equals((5)),$$$cl1.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (300:4-304:4)
    function f5$1062(){
        
        //AttributeDeclaration i2 at operators.ceylon (301:8-301:25)
        var i2$1063=(oldi1$1064=i1$1029,i1$1029=oldi1$1064.predecessor,oldi1$1064);
        var oldi1$1064;
        $$$c2.check(i1$1029.equals((1)),$$$cl1.String("postfix decrement",17));
        $$$c2.check(i2$1063.equals((2)),$$$cl1.String("postfix decrement",17));
    };f5$1062.$$metamodel$$={$nm:'f5',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//f5$1062.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
    f5$1062();
    
    //AttributeDeclaration i7 at operators.ceylon (307:4-307:25)
    var i7$1065=(tmp$1066=f2$1042(),oldi$1067=tmp$1066.i,tmp$1066.i=oldi$1067.predecessor,oldi$1067);
    var tmp$1066,oldi$1067;
    $$$c2.check(i7$1065.equals((2)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(c1$1039.i.equals((1)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(i3$1040.equals((6)),$$$cl1.String("postfix decrement",17));
};testIncDecOperators.$$metamodel$$={$nm:'testIncDecOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testIncDecOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (313:0-364:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (314:4-314:27)
    var i1$1068=(1);
    var setI1$1068=function(i1$1069){return i1$1068=i1$1069;};
    
    //AttributeDeclaration x0 at operators.ceylon (315:4-315:27)
    var x0$1070=(1);
    var setX0$1070=function(x0$1071){return x0$1070=x0$1071;};
    
    //AttributeGetterDefinition x at operators.ceylon (316:4-316:27)
    var getX$1072=function(){
        return x0$1070;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (316:29-316:46)
    var setX$1072=function(x$1073){
        x0$1070=x$1073;
    };
    (i1$1068=i1$1068.plus((10)));
    (setX$1072(getX$1072().plus((10))),getX$1072());
    $$$c2.check(i1$1068.equals((11)),$$$cl1.String("+= operator 1",13));
    $$$c2.check(getX$1072().equals((11)),$$$cl1.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (322:4-322:36)
    var i2$1074=(i1$1068=i1$1068.plus((-(5))));
    var setI2$1074=function(i2$1075){return i2$1074=i2$1075;};
    
    //AttributeDeclaration x2 at operators.ceylon (323:4-323:35)
    var x2$1076=(setX$1072(getX$1072().plus((-(5)))),getX$1072());
    var setX2$1076=function(x2$1077){return x2$1076=x2$1077;};
    $$$c2.check(i2$1074.equals((6)),$$$cl1.String("+= operator 3",13));
    $$$c2.check(i1$1068.equals((6)),$$$cl1.String("+= operator 4",13));
    $$$c2.check(x2$1076.equals((6)),$$$cl1.String("+= operator 5",13));
    $$$c2.check(getX$1072().equals((6)),$$$cl1.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (329:4-333:4)
    function C1$1078($$c1$1078){
        $init$C1$1078();
        if ($$c1$1078===undefined)$$c1$1078=new C1$1078.$$;
        
        //AttributeDeclaration i at operators.ceylon (330:8-330:37)
        var i=(1);
        $$$cl1.defineAttr($$c1$1078,'i',function(){return i;},function(i$1079){return i=i$1079;});
        
        //AttributeDeclaration x0 at operators.ceylon (331:8-331:31)
        var x0$1080=(1);
        $$$cl1.defineAttr($$c1$1078,'x0$1080',function(){return x0$1080;},function(x0$1081){return x0$1080=x0$1081;});
        
        //AttributeGetterDefinition x at operators.ceylon (332:8-332:38)
        $$$cl1.defineAttr($$c1$1078,'x',function(){
            return x0$1080;
        },function(x$1082){
            x0$1080=x$1082;
        });
        return $$c1$1078;
    }
    function $init$C1$1078(){
        if (C1$1078.$$===undefined){
            $$$cl1.initTypeProto(C1$1078,'operators::testArithmeticAssignOperators.C1',$$$cl1.Basic);
        }
        C1$1078.$$.$$metamodel$$={$nm:'C1',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return C1$1078;
    }
    $init$C1$1078();
    
    //AttributeDeclaration c1 at operators.ceylon (334:4-334:16)
    var c1$1083=C1$1078();
    
    //AttributeDeclaration i3 at operators.ceylon (335:4-335:27)
    var i3$1084=(0);
    var setI3$1084=function(i3$1085){return i3$1084=i3$1085;};
    
    //MethodDefinition f at operators.ceylon (336:4-339:4)
    function f$1086(){
        (i3$1084=i3$1084.successor);
        return c1$1083;
    };f$1086.$$metamodel$$={$nm:'f',$mt:'mthd',$t:{t:C1$1078},$ps:[]};//f$1086.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:C1$1078}};
    i2$1074=(tmp$1087=f$1086(),tmp$1087.i=tmp$1087.i.plus((11)));
    var tmp$1087;
    x2$1076=(tmp$1088=f$1086(),tmp$1088.x=tmp$1088.x.plus((11)),tmp$1088.x);
    var tmp$1088;
    $$$c2.check(i2$1074.equals((12)),$$$cl1.String("+= operator 7",13));
    $$$c2.check(c1$1083.i.equals((12)),$$$cl1.String("+= operator 8",13));
    $$$c2.check(x2$1076.equals((12)),$$$cl1.String("+= operator 9",13));
    $$$c2.check(c1$1083.x.equals((12)),$$$cl1.String("+= operator 10",14));
    $$$c2.check(i3$1084.equals((2)),$$$cl1.String("+= operator 11",14));
    i2$1074=(i1$1068=i1$1068.minus((14)));
    $$$c2.check(i1$1068.equals((-(8))),$$$cl1.String("-= operator",11));
    $$$c2.check(i2$1074.equals((-(8))),$$$cl1.String("-= operator",11));
    i2$1074=(i1$1068=i1$1068.times((-(3))));
    $$$c2.check(i1$1068.equals((24)),$$$cl1.String("*= operator",11));
    $$$c2.check(i2$1074.equals((24)),$$$cl1.String("*= operator",11));
    i2$1074=(i1$1068=i1$1068.divided((5)));
    $$$c2.check(i1$1068.equals((4)),$$$cl1.String("/= operator",11));
    $$$c2.check(i2$1074.equals((4)),$$$cl1.String("/= operator",11));
    i2$1074=(i1$1068=i1$1068.remainder((3)));
    $$$c2.check(i1$1068.equals((1)),$$$cl1.String("%= operator",11));
    $$$c2.check(i2$1074.equals((1)),$$$cl1.String("%= operator",11));
};testArithmeticAssignOperators.$$metamodel$$={$nm:'testArithmeticAssignOperators',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testArithmeticAssignOperators.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testAssignmentOperator at operators.ceylon (366:0-396:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (367:4-367:27)
    var i1$1089=(1);
    var setI1$1089=function(i1$1090){return i1$1089=i1$1090;};
    
    //AttributeDeclaration i2 at operators.ceylon (368:4-368:27)
    var i2$1091=(2);
    var setI2$1091=function(i2$1092){return i2$1091=i2$1092;};
    
    //AttributeDeclaration i3 at operators.ceylon (369:4-369:27)
    var i3$1093=(3);
    var setI3$1093=function(i3$1094){return i3$1093=i3$1094;};
    $$$c2.check((i1$1089=(i2$1091=i3$1093)).equals((3)),$$$cl1.String("assignment 1",12));
    $$$c2.check(i1$1089.equals((3)),$$$cl1.String("assignment 2",12));
    $$$c2.check(i2$1091.equals((3)),$$$cl1.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (374:4-374:28)
    var getX1$1095=function(){
        return i1$1089;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (374:30-374:51)
    var setX1$1095=function(x1$1096){
        i1$1089=x1$1096;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (375:4-375:28)
    var getX2$1097=function(){
        return i2$1091;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (375:30-375:51)
    var setX2$1097=function(x2$1098){
        i2$1091=x2$1098;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (376:4-376:28)
    var getX3$1099=function(){
        return i3$1093;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (376:30-376:51)
    var setX3$1099=function(x3$1100){
        i3$1093=x3$1100;
    };
    i1$1089=(1);
    i2$1091=(2);
    $$$c2.check((setX1$1095((setX2$1097(getX3$1099()),getX2$1097())),getX1$1095()).equals((3)),$$$cl1.String("assignment 4",12));
    $$$c2.check(getX1$1095().equals((3)),$$$cl1.String("assignment 5",12));
    $$$c2.check(getX2$1097().equals((3)),$$$cl1.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (383:4-387:4)
    function C$1101($$c$1101){
        $init$C$1101();
        if ($$c$1101===undefined)$$c$1101=new C$1101.$$;
        
        //AttributeDeclaration i at operators.ceylon (384:8-384:37)
        var i=(1);
        $$$cl1.defineAttr($$c$1101,'i',function(){return i;},function(i$1102){return i=i$1102;});
        
        //AttributeDeclaration x0 at operators.ceylon (385:8-385:31)
        var x0$1103=(1);
        $$$cl1.defineAttr($$c$1101,'x0$1103',function(){return x0$1103;},function(x0$1104){return x0$1103=x0$1104;});
        
        //AttributeGetterDefinition x at operators.ceylon (386:8-386:38)
        $$$cl1.defineAttr($$c$1101,'x',function(){
            return x0$1103;
        },function(x$1105){
            x0$1103=x$1105;
        });
        return $$c$1101;
    }
    function $init$C$1101(){
        if (C$1101.$$===undefined){
            $$$cl1.initTypeProto(C$1101,'operators::testAssignmentOperator.C',$$$cl1.Basic);
        }
        C$1101.$$.$$metamodel$$={$nm:'C',$mt:'cls','super':{t:$$$cl1.Basic},'satisfies':[]};
        return C$1101;
    }
    $init$C$1101();
    
    //AttributeDeclaration o1 at operators.ceylon (388:4-388:14)
    var o1$1106=C$1101();
    
    //AttributeDeclaration o2 at operators.ceylon (389:4-389:14)
    var o2$1107=C$1101();
    $$$c2.check((o1$1106.i=(o2$1107.i=(3))).equals((3)),$$$cl1.String("assignment 7",12));
    $$$c2.check(o1$1106.i.equals((3)),$$$cl1.String("assignment 8",12));
    $$$c2.check(o2$1107.i.equals((3)),$$$cl1.String("assignment 9",12));
    $$$c2.check((tmp$1108=o1$1106,tmp$1108.x=(tmp$1109=o2$1107,tmp$1109.x=(3),tmp$1109.x),tmp$1108.x).equals((3)),$$$cl1.String("assignment 10",13));
    var tmp$1108,tmp$1109;
    $$$c2.check(o1$1106.x.equals((3)),$$$cl1.String("assignment 11",13));
    $$$c2.check(o2$1107.x.equals((3)),$$$cl1.String("assignment 12",13));
};testAssignmentOperator.$$metamodel$$={$nm:'testAssignmentOperator',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testAssignmentOperator.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

//MethodDefinition testSegments at operators.ceylon (398:0-426:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (399:4-399:97)
    var seq$1110=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.Tuple($$$cl1.String("nine",4),$$$cl1.Tuple($$$cl1.String("ten",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    $$$c2.check(seq$1110.segment((1),(2)).equals($$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[1:2] ",9),seq$1110.segment((1),(2)).string]).string);
    $$$c2.check(seq$1110.segment((3),(5)).equals($$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[3:5] ",9),seq$1110.segment((3),(5)).string]).string);
    $$$c2.check($$$cl1.String("test",4).segment((1),(2)).equals($$$cl1.String("es",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("test[1:2] ",10),$$$cl1.String("test",4).segment((1),(2)).string]).string);
    $$$c2.check($$$cl1.String("hello",5).segment((2),(2)).equals($$$cl1.String("ll",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("hello[2:2] ",11),$$$cl1.String("hello",5).segment((2),(2)).string]).string);
    $$$c2.check((function(){
        //SpreadOp at 404:10-404:24
        var lst$1111=[];
        var it$1112=seq$1110.iterator();
        var elem$1113;
        while ((elem$1113=it$1112.next())!==$$$cl1.getFinished()){
            lst$1111.push(elem$1113.uppercased);
        }
        return $$$cl1.ArraySequence(lst$1111);
    }()).equals($$$cl1.Tuple($$$cl1.String("ONE",3),$$$cl1.Tuple($$$cl1.String("TWO",3),$$$cl1.Tuple($$$cl1.String("THREE",5),$$$cl1.Tuple($$$cl1.String("FOUR",4),$$$cl1.Tuple($$$cl1.String("FIVE",4),$$$cl1.Tuple($$$cl1.String("SIX",3),$$$cl1.Tuple($$$cl1.String("SEVEN",5),$$$cl1.Tuple($$$cl1.String("EIGHT",5),$$$cl1.Tuple($$$cl1.String("NINE",4),$$$cl1.Tuple($$$cl1.String("TEN",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.String("spread op",9));
    
    //AttributeDeclaration s2 at operators.ceylon (405:4-405:18)
    var s2$1114=(function(){var tmpvar$1115=(3);
    if (tmpvar$1115>0){
    var tmpvar$1116=(0);
    var tmpvar$1117=tmpvar$1116;
    for (var i=1; i<tmpvar$1115; i++){tmpvar$1117=tmpvar$1117.successor;}
    return $$$cl1.Range(tmpvar$1116,tmpvar$1117,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (406:4-406:18)
    var s3$1118=(function(){var tmpvar$1119=(5);
    if (tmpvar$1119>0){
    var tmpvar$1120=(2);
    var tmpvar$1121=tmpvar$1120;
    for (var i=1; i<tmpvar$1119; i++){tmpvar$1121=tmpvar$1121.successor;}
    return $$$cl1.Range(tmpvar$1120,tmpvar$1121,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}());
    $$$c2.check(s2$1114.size.equals((3)),$$$cl1.String("0:3 [1]",7));
    var x$1122;
    if((x$1122=s2$1114.get((0)))!==null){
        $$$c2.check(x$1122.equals((0)),$$$cl1.String("0:3 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [2]",7));
    }
    var x$1123;
    if((x$1123=s2$1114.get((2)))!==null){
        $$$c2.check(x$1123.equals((2)),$$$cl1.String("0:3 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [3]",7));
    }
    $$$c2.check(s3$1118.size.equals((5)),$$$cl1.String("2:5 [1]",7));
    var x$1124;
    if((x$1124=s3$1118.get((0)))!==null){
        $$$c2.check(x$1124.equals((2)),$$$cl1.String("2:5 [1]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [1]",7));
    }
    var x$1125;
    if((x$1125=s3$1118.get((2)))!==null){
        $$$c2.check(x$1125.equals((4)),$$$cl1.String("2:5 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [2]",7));
    }
    var x$1126;
    if((x$1126=s3$1118.get((4)))!==null){
        $$$c2.check(x$1126.equals((6)),$$$cl1.String("2:5 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [3]",7));
    }
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1127=(0);
    if (tmpvar$1127>0){
    var tmpvar$1128=(1);
    var tmpvar$1129=tmpvar$1128;
    for (var i=1; i<tmpvar$1127; i++){tmpvar$1129=tmpvar$1129.successor;}
    return $$$cl1.Range(tmpvar$1128,tmpvar$1129,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:0 empty",9));
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1130=(-(1));
    if (tmpvar$1130>0){
    var tmpvar$1131=(1);
    var tmpvar$1132=tmpvar$1131;
    for (var i=1; i<tmpvar$1130; i++){tmpvar$1132=tmpvar$1132.successor;}
    return $$$cl1.Range(tmpvar$1131,tmpvar$1132,{Element:{t:$$$cl1.Integer}})
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:-1 empty",10));
};testSegments.$$metamodel$$={$nm:'testSegments',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//testSegments.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};

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
    $$$c2.results();
}
exports.test=test;
test.$$metamodel$$={$nm:'test',$mt:'mthd',$t:{t:$$$cl1.Anything},$ps:[]};//test.$$targs$$={Arguments:{t:$$$cl1.Empty},Return:{t:$$$cl1.Anything}};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
