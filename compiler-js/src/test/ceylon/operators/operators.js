(function(define) { define(function(require, exports, module) {
var $$metamodel$$={"$mod-deps":["check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$nm":"nullsafeTest"}}};
var $$$cl1=require('ceylon/language/0.5/ceylon.language-0.5');
var $$$c2=require('check/0.1/check-0.1');

//MethodDefinition testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDeclaration seq at collections.ceylon (4:4-4:21)
    var seq$815=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration lcomp at collections.ceylon (5:4-5:37)
    var lcomp$816=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$817=$$$cl1.String("hola",4).getIterator();
        var c$818=$$$cl1.getFinished();
        var next$c$818=function(){return c$818=it$817.next();}
        next$c$818();
        return function(){
            if(c$818!==$$$cl1.getFinished()){
                var c$818$819=c$818;
                function getC$818(){return c$818$819;}
                var tmpvar$820=getC$818();
                next$c$818();
                return tmpvar$820;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration ecomp at collections.ceylon (6:4-6:37)
    var ecomp$821=$$$cl1.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$822=$$$cl1.String("hola",4).getIterator();
        var c$823=$$$cl1.getFinished();
        var next$c$823=function(){return c$823=it$822.next();}
        next$c$823();
        return function(){
            if(c$823!==$$$cl1.getFinished()){
                var c$823$824=c$823;
                function getC$823(){return c$823$824;}
                var tmpvar$825=getC$823();
                next$c$823();
                return tmpvar$825;
            }
            return $$$cl1.getFinished();
        }
    },{Absent:{t:$$$cl1.Anything},Element:{t:$$$cl1.Character}}).getSequence();
    
    //AttributeDeclaration s2 at collections.ceylon (7:4-7:24)
    var s2$826=[(0)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Integer}}).chain(seq$815,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration s3 at collections.ceylon (8:4-8:28)
    var s3$827=[$$$cl1.Character(65)].reifyCeylonType({Absent:{t:$$$cl1.Nothing},Element:{t:$$$cl1.Character}}).chain(lcomp$816,{Absent:{t:$$$cl1.Null},Element:{t:$$$cl1.Character}});
    
    //AttributeDeclaration t1 at collections.ceylon (9:4-9:20)
    var t1$828=$$$cl1.Tuple((1),$$$cl1.Tuple((2),$$$cl1.Tuple((3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    
    //AttributeDeclaration t2 at collections.ceylon (10:4-10:22)
    var t2$829=$$$cl1.Tuple((0),seq$815,{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}}},First:{t:$$$cl1.Integer},Element:{t:$$$cl1.Integer}});
    $$$c2.check($$$cl1.className(seq$815).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{1,2,3} is not a Tuple but a ",29),$$$cl1.className(seq$815).getString()]).getString());
    $$$c2.check((!$$$cl1.className(lcomp$816).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("lazy comprehension is a Tuple ",30),$$$cl1.className(lcomp$816).getString()]).getString());
    $$$c2.check($$$cl1.className(ecomp$821).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("eager comprehension is not a Tuple but a ",41),$$$cl1.className(ecomp$821).getString()]).getString());
    $$$c2.check((!$$$cl1.className(s2$826).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} is a Tuple ",20),$$$cl1.className(s2$826).getString()]).getString());
    $$$c2.check((!$$$cl1.className(s3$827).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{x,*iter} is a Tuple ",21),$$$cl1.className(s3$827).getString()]).getString());
    $$$c2.check($$$cl1.className(t1$828).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[1,2,3] is not a Tuple but a ",29),$$$cl1.className(t1$828).getString()]).getString());
    $$$c2.check($$$cl1.className(t2$829).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("[0,*seq] is not a Tuple but a ",30),$$$cl1.className(t2$829).getString()]).getString());
    $$$c2.check(seq$815.equals(t1$828),$$$cl1.String("{1,2,3} != [1,2,3]",18));
    $$$c2.check((!$$$cl1.className(t2$829).equals($$$cl1.className(s2$826))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{0,*seq} != [0,*seq] ",21),$$$cl1.className(t2$829).getString(),$$$cl1.String(" vs",3),$$$cl1.className(s2$826).getString()]).getString());
    $$$c2.check(seq$815.getSize().equals((3)),$$$cl1.String("seq.size!=3",11));
    $$$c2.check(lcomp$816.getSequence().getSize().equals((4)),$$$cl1.String("lcomp.size!=4",13));
    $$$c2.check(ecomp$821.getSize().equals((4)),$$$cl1.String("ecomp.size!=4",13));
    $$$c2.check(s2$826.getSize().equals((4)),$$$cl1.String("s2.size!=4",10));
    $$$c2.check(s3$827.getSequence().getSize().equals((5)),$$$cl1.String("s3.size!=5",10));
    $$$c2.check(t1$828.getSize().equals((3)),$$$cl1.String("t1.size!=3",10));
    $$$c2.check(t2$829.getSize().equals((4)),$$$cl1.String("t2.size!=4",10));
    $$$c2.check((!$$$cl1.className(lcomp$816).startsWith($$$cl1.String("ceylon.language::Tuple",22))),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*comp} is not Tuple but ",25),$$$cl1.className(lcomp$816).getString()]).getString());
    $$$c2.check($$$cl1.className(ecomp$821).startsWith($$$cl1.String("ceylon.language::ArraySequence",30)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*ecomp} is not Tuple but ",26),$$$cl1.className(ecomp$821).getString()]).getString());
    $$$c2.check($$$cl1.className(seq$815).startsWith($$$cl1.String("ceylon.language::Tuple",22)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("{*seq} is not Tuple but ",24),$$$cl1.className(seq$815).getString()]).getString());
};

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:28)
    var i1$830=(-(4));
    var setI1$830=function(i1$831){return i1$830=i1$831;};
    i1$830=(-i1$830);
    $$$c2.check(i1$830.equals((4)),$$$cl1.String("negation",8));
    i1$830=(+(-(987654)));
    $$$c2.check(i1$830.equals((-(987654))),$$$cl1.String("positive",8));
    i1$830=(+(0));
    $$$c2.check(i1$830.equals((0)),$$$cl1.String("+0=0",4));
    i1$830=(-(0));
    $$$c2.check(i1$830.equals((0)),$$$cl1.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:35)
    var i2$832=(123).plus((456));
    var setI2$832=function(i2$833){return i2$832=i2$833;};
    $$$c2.check(i2$832.equals((579)),$$$cl1.String("addition",8));
    i1$830=i2$832.minus((16));
    $$$c2.check(i1$830.equals((563)),$$$cl1.String("subtraction",11));
    i2$832=(-i1$830).plus(i2$832).minus((1));
    $$$c2.check(i2$832.equals((15)),$$$cl1.String("-i1+i2-1",8));
    i1$830=(3).times((7));
    $$$c2.check(i1$830.equals((21)),$$$cl1.String("multiplication",14));
    i2$832=i1$830.times((2));
    $$$c2.check(i2$832.equals((42)),$$$cl1.String("multiplication",14));
    i2$832=(17).divided((4));
    $$$c2.check(i2$832.equals((4)),$$$cl1.String("integer division",16));
    i1$830=i2$832.times((516)).divided((-i1$830));
    $$$c2.check(i1$830.equals((-(98))),$$$cl1.String("i2*516/-i1",10));
    i1$830=(15).remainder((4));
    $$$c2.check(i1$830.equals((3)),$$$cl1.String("modulo",6));
    i2$832=(312).remainder((12));
    $$$c2.check(i2$832.equals((0)),$$$cl1.String("modulo",6));
    i1$830=(2).power((10));
    $$$c2.check(i1$830.equals((1024)),$$$cl1.String("power",5));
    i2$832=(10).power((6));
    $$$c2.check(i2$832.equals((1000000)),$$$cl1.String("power",5));
};

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:28)
    var f1$834=$$$cl1.Float(4.2).getNegativeValue();
    var setF1$834=function(f1$835){return f1$834=f1$835;};
    f1$834=f1$834.getNegativeValue();
    $$$c2.check(f1$834.equals($$$cl1.Float(4.2)),$$$cl1.String("negation",8));
    f1$834=(+$$$cl1.Float(987654.9925567).getNegativeValue());
    $$$c2.check(f1$834.equals($$$cl1.Float(987654.9925567).getNegativeValue()),$$$cl1.String("positive",8));
    f1$834=(+$$$cl1.Float(0.0));
    $$$c2.check(f1$834.equals($$$cl1.Float(0.0)),$$$cl1.String("+0.0=0.0",8));
    f1$834=$$$cl1.Float(0.0).getNegativeValue();
    $$$c2.check(f1$834.equals($$$cl1.Float(0.0)),$$$cl1.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:42)
    var f2$836=$$$cl1.Float(3.14159265).plus($$$cl1.Float(456.0));
    var setF2$836=function(f2$837){return f2$836=f2$837;};
    $$$c2.check(f2$836.equals($$$cl1.Float(459.14159265)),$$$cl1.String("addition",8));
    f1$834=f2$836.minus($$$cl1.Float(0.0016));
    $$$c2.check(f1$834.equals($$$cl1.Float(459.13999265)),$$$cl1.String("subtraction",11));
    f2$836=f1$834.getNegativeValue().plus(f2$836).minus($$$cl1.Float(1.2));
    $$$c2.check(f2$836.equals($$$cl1.Float(1.1984000000000037).getNegativeValue()),$$$cl1.String("-f1+f2-1.2",10));
    f1$834=$$$cl1.Float(3.0).times($$$cl1.Float(0.79));
    $$$c2.check(f1$834.equals($$$cl1.Float(2.37)),$$$cl1.String("multiplication",14));
    f2$836=f1$834.times($$$cl1.Float(2.0e13));
    $$$c2.check(f2$836.equals($$$cl1.Float(47400000000000.0)),$$$cl1.String("multiplication",14));
    f2$836=$$$cl1.Float(17.1).divided($$$cl1.Float(4.0E-18));
    $$$c2.check(f2$836.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("division",8));
    f1$834=f2$836.times($$$cl1.Float(51.6e2)).divided(f1$834.getNegativeValue());
    $$$c2.check(f2$836.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("f2*51.6e2/-f1",13));
    f1$834=$$$cl1.Float(150.0).power($$$cl1.Float(0.5));
    $$$c2.check(f1$834.equals($$$cl1.Float(12.24744871391589)),$$$cl1.String("power",5));
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
    var o1$838=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$839=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:35)
    var b1$840=(o1$838===o2$839);
    var setB1$840=function(b1$841){return b1$840=b1$841;};
    $$$c2.check((!b1$840),$$$cl1.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:35)
    var b2$842=(o1$838===o1$838);
    var setB2$842=function(b2$843){return b2$842=b2$843;};
    $$$c2.check(b2$842,$$$cl1.String("identity",8));
    b1$840=o1$838.equals(o2$839);
    $$$c2.check((!b1$840),$$$cl1.String("equals",6));
    b2$842=o1$838.equals(o1$838);
    $$$c2.check(b2$842,$$$cl1.String("equals",6));
    b1$840=(1).equals((2));
    $$$c2.check((!b1$840),$$$cl1.String("equals",6));
    b2$842=(!(1).equals((2)));
    $$$c2.check(b2$842,$$$cl1.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:29)
    var b3$844=(!b2$842);
    var setB3$844=function(b3$845){return b3$844=b3$845;};
    $$$c2.check((!b3$844),$$$cl1.String("not",3));
    b1$840=(true&&false);
    $$$c2.check((!b1$840),$$$cl1.String("and",3));
    b2$842=(b1$840&&true);
    $$$c2.check((!b2$842),$$$cl1.String("and",3));
    b3$844=(true&&true);
    $$$c2.check(b3$844,$$$cl1.String("and",3));
    b1$840=(true||false);
    $$$c2.check(b1$840,$$$cl1.String("or",2));
    b2$842=(false||b1$840);
    $$$c2.check(b2$842,$$$cl1.String("or",2));
    b3$844=(false||false);
    $$$c2.check((!b3$844),$$$cl1.String("or",2));
};

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-139:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$846=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4));
    $$$c2.check(c1$846.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$847=$$$cl1.String("str2",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c2$847.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$848=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4));
    $$$c2.check(c3$848.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$849=$$$cl1.String("",0).compare($$$cl1.String("",0));
    $$$c2.check(c4$849.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$850=$$$cl1.String("str1",4).compare($$$cl1.String("",0));
    $$$c2.check(c5$850.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$851=$$$cl1.String("",0).compare($$$cl1.String("str2",4));
    $$$c2.check(c6$851.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:41)
    var b1$852=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getSmaller());
    var setB1$852=function(b1$853){return b1$852=b1$853;};
    $$$c2.check(b1$852,$$$cl1.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:41)
    var b2$854=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getLarger());
    var setB2$854=function(b2$855){return b2$854=b2$855;};
    $$$c2.check((!b2$854),$$$cl1.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:42)
    var b3$856=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getLarger());
    var setB3$856=function(b3$857){return b3$856=b3$857;};
    $$$c2.check(b3$856,$$$cl1.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:42)
    var b4$858=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getSmaller());
    var setB4$858=function(b4$859){return b4$858=b4$859;};
    $$$c2.check((!b4$858),$$$cl1.String("large as",8));
    b1$852=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getSmaller());
    $$$c2.check((!b1$852),$$$cl1.String("smaller",7));
    b2$854=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getLarger());
    $$$c2.check((!b2$854),$$$cl1.String("larger",6));
    b3$856=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getLarger());
    $$$c2.check(b3$856,$$$cl1.String("small as",8));
    b4$858=($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getSmaller());
    $$$c2.check(b4$858,$$$cl1.String("large as",8));
};

//MethodDefinition testOtherOperators at operators.ceylon (141:0-153:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (142:4-142:42)
    var entry$860=$$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}});
    $$$c2.check(entry$860.getKey().equals((47)),$$$cl1.String("entry key",9));
    $$$c2.check(entry$860.getItem().equals($$$cl1.String("hi there",8)),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (145:4-145:30)
    var entry2$861=$$$cl1.Entry(true,entry$860,{Key:{t:$$$cl1.$true},Item:{t:$$$cl1.Entry,a:{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}}}});
    $$$c2.check(entry2$861.getKey().equals(true),$$$cl1.String("entry key",9));
    $$$c2.check(entry2$861.getItem().equals($$$cl1.Entry((47),$$$cl1.String("hi there",8),{Key:{t:$$$cl1.Integer},Item:{t:$$$cl1.String}})),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (149:4-149:41)
    var s1$862=(opt$863=(true?$$$cl1.String("ok",2):null),opt$863!==null?opt$863:$$$cl1.String("noo",3));
    var opt$863;
    $$$c2.check(s1$862.equals($$$cl1.String("ok",2)),$$$cl1.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (151:4-151:47)
    var s2$864=(opt$865=(false?$$$cl1.String("what?",5):null),opt$865!==null?opt$865:$$$cl1.String("great",5));
    var opt$865;
    $$$c2.check(s2$864.equals($$$cl1.String("great",5)),$$$cl1.String("then/else 2",11));
};

//MethodDefinition testCollectionOperators at operators.ceylon (155:0-167:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (156:4-156:33)
    var seq1$866=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (157:4-157:23)
    var s1$867=seq1$866.get((0));
    $$$c2.check(s1$867.equals($$$cl1.String("one",3)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (159:4-159:28)
    var s2$868=seq1$866.get((2));
    $$$c2.check((!$$$cl1.exists(s2$868)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (161:4-161:29)
    var s3$869=seq1$866.get((-(1)));
    $$$c2.check((!$$$cl1.exists(s3$869)),$$$cl1.String("lookup",6));
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
    function f2(x$870){
        return x$870();
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
function nullsafeTest(f$871){
    return f$871();
};

//MethodDefinition testNullsafeOperators at operators.ceylon (180:0-221:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (181:4-181:27)
    var seq$872=$$$cl1.Tuple($$$cl1.String("hi",2),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    
    //AttributeDeclaration s1 at operators.ceylon (182:4-182:34)
    var s1$873=(opt$874=seq$872.get((0)),opt$874!==null?opt$874:$$$cl1.String("null",4));
    var opt$874;
    $$$c2.check(s1$873.equals($$$cl1.String("hi",2)),$$$cl1.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (184:4-184:34)
    var s2$875=(opt$876=seq$872.get((1)),opt$876!==null?opt$876:$$$cl1.String("null",4));
    var opt$876;
    $$$c2.check(s2$875.equals($$$cl1.String("null",4)),$$$cl1.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (187:4-187:21)
    var s3$877=null;
    
    //AttributeDeclaration s4 at operators.ceylon (188:4-188:23)
    var s4$878=$$$cl1.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (189:4-189:42)
    var s5$879=(opt$880=(opt$881=s3$877,opt$881!==null?opt$881.getUppercased():null),opt$880!==null?opt$880:$$$cl1.String("null",4));
    var opt$880,opt$881;
    
    //AttributeDeclaration s6 at operators.ceylon (190:4-190:42)
    var s6$882=(opt$883=(opt$884=s4$878,opt$884!==null?opt$884.getUppercased():null),opt$883!==null?opt$883:$$$cl1.String("null",4));
    var opt$883,opt$884;
    $$$c2.check(s5$879.equals($$$cl1.String("null",4)),$$$cl1.String("nullsafe member 1",17));
    $$$c2.check(s6$882.equals($$$cl1.String("TEST",4)),$$$cl1.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (193:4-193:28)
    var obj$885=null;
    
    //AttributeDeclaration i at operators.ceylon (194:4-194:25)
    var i$886=(opt$887=obj$885,$$$cl1.JsCallable(opt$887,opt$887!==null?opt$887.f:null))();
    var opt$887;
    $$$c2.check((!$$$cl1.exists(i$886)),$$$cl1.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (196:4-196:37)
    var f2$888=(opt$889=obj$885,$$$cl1.JsCallable(opt$889,opt$889!==null?opt$889.f:null));
    var opt$889;
    $$$c2.check((!$$$cl1.exists(nullsafeTest(f2$888))),$$$cl1.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (198:4-198:38)
    var f3$890=(opt$891=obj$885,$$$cl1.JsCallable(opt$891,opt$891!==null?opt$891.f:null));
    var opt$891;
    $$$c2.check($$$cl1.exists(f3$890),$$$cl1.String("nullsafe method ref 2",21));
    (opt$892=obj$885,$$$cl1.JsCallable(opt$892,opt$892!==null?opt$892.f:null))();
    var opt$892;
    $$$c2.check((!$$$cl1.exists((opt$893=obj$885,$$$cl1.JsCallable(opt$893,opt$893!==null?opt$893.f:null))())),$$$cl1.String("nullsafe simple call",20));
    var opt$893;
    
    //MethodDefinition getNullsafe at operators.ceylon (202:4-202:46)
    function getNullsafe$894(){
        return obj$885;
    };
    
    //MethodDeclaration f4 at operators.ceylon (203:4-203:39)
    var f4$895=function (){
        return (opt$896=getNullsafe$894(),$$$cl1.JsCallable(opt$896,opt$896!==null?opt$896.f:null))();
    };
    var opt$896;
    
    //AttributeDeclaration result_f4 at operators.ceylon (204:4-204:29)
    var result_f4$897=f4$895();
    $$$c2.check((!$$$cl1.exists(result_f4$897)),$$$cl1.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (206:4-206:36)
    var i2$898=(opt$899=getNullsafe$894(),$$$cl1.JsCallable(opt$899,opt$899!==null?opt$899.f:null))();
    var opt$899;
    $$$c2.check((!$$$cl1.exists(i2$898)),$$$cl1.String("nullsafe invoke 3",17));
    $$$c2.check((!$$$cl1.exists(NullsafeTest().f2((opt$900=getNullsafe$894(),$$$cl1.JsCallable(opt$900,opt$900!==null?opt$900.f:null))))),$$$cl1.String("nullsafe method ref 3",21));
    var opt$900;
    
    //AttributeDeclaration obj2 at operators.ceylon (209:4-209:39)
    var obj2$901=NullsafeTest();
    var i3$902;
    if((i3$902=(opt$903=obj2$901,$$$cl1.JsCallable(opt$903,opt$903!==null?opt$903.f:null))())!==null){
        $$$c2.check(i3$902.equals((1)),$$$cl1.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe invoke 4 (null)",24));
    }
    var opt$903;
    
    //MethodDeclaration obj2_f at operators.ceylon (215:4-215:34)
    var obj2_f$904=function (){
        return (opt$905=obj2$901,$$$cl1.JsCallable(opt$905,opt$905!==null?opt$905.f:null))();
    };
    var opt$905;
    var i3$906;
    if((i3$906=obj2_f$904())!==null){
        $$$c2.check(i3$906.equals((1)),$$$cl1.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c2.fail($$$cl1.String("nullsafe method ref 4 (null)",28));
    }
};

//MethodDefinition testIncDecOperators at operators.ceylon (223:0-298:0)
function testIncDecOperators(){
    
    //AttributeDeclaration x0 at operators.ceylon (224:4-224:27)
    var x0$907=(1);
    var setX0$907=function(x0$908){return x0$907=x0$908;};
    
    //AttributeGetterDefinition x at operators.ceylon (225:4-225:27)
    var getX$909=function(){
        return x0$907;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (225:29-225:48)
    var setX$909=function(x$910){
        x0$907=x$910;
    };
    
    //AttributeDeclaration i1 at operators.ceylon (227:4-227:27)
    var i1$911=(1);
    var setI1$911=function(i1$912){return i1$911=i1$912;};
    
    //MethodDefinition f1 at operators.ceylon (228:4-235:4)
    function f1$913(){
        
        //AttributeDeclaration i2 at operators.ceylon (229:8-229:25)
        var i2$914=(i1$911=i1$911.getSuccessor());
        
        //AttributeDeclaration x2 at operators.ceylon (230:8-230:24)
        var x2$915=(setX$909(getX$909().getSuccessor()),getX$909());
        $$$c2.check(i1$911.equals((2)),$$$cl1.String("prefix increment 1",18));
        $$$c2.check(i2$914.equals((2)),$$$cl1.String("prefix increment 2",18));
        $$$c2.check(getX$909().equals((2)),$$$cl1.String("prefix increment 3",18));
        $$$c2.check(x2$915.equals((2)),$$$cl1.String("prefix increment 4",18));
    };
    f1$913();
    
    //ClassDefinition C1 at operators.ceylon (238:4-242:4)
    function C1$916($$c1$916){
        $init$C1$916();
        if ($$c1$916===undefined)$$c1$916=new C1$916.$$;
        
        //AttributeDeclaration i at operators.ceylon (239:8-239:37)
        var i$917=(1);
        var getI=function(){return i$917;};
        $$c1$916.getI=getI;
        var setI=function(i$918){return i$917=i$918;};
        $$c1$916.setI=setI;
        
        //AttributeDeclaration x0 at operators.ceylon (240:8-240:31)
        var x0$919=(1);
        var getX0$919=function(){return x0$919;};
        $$c1$916.getX0$919=getX0$919;
        var setX0$919=function(x0$920){return x0$919=x0$920;};
        $$c1$916.setX0$919=setX0$919;
        
        //AttributeGetterDefinition x at operators.ceylon (241:8-241:38)
        var getX=function(){
            return getX0$919();
        }
        $$c1$916.getX=getX;
        
        //AttributeSetterDefinition x at operators.ceylon (241:40-241:59)
        var setX=function(x$921){
            setX0$919(x$921);
        }
        $$c1$916.setX=setX;
        return $$c1$916;
    }
    function $init$C1$916(){
        if (C1$916.$$===undefined){
            $$$cl1.initTypeProto(C1$916,'operators::testIncDecOperators.C1',$$$cl1.Basic);
        }
        return C1$916;
    }
    $init$C1$916();
    
    //AttributeDeclaration c1 at operators.ceylon (243:4-243:16)
    var c1$922=C1$916();
    
    //AttributeDeclaration i3 at operators.ceylon (244:4-244:27)
    var i3$923=(0);
    var setI3$923=function(i3$924){return i3$923=i3$924;};
    
    //MethodDefinition f2 at operators.ceylon (245:4-248:4)
    function f2$925(){
        (i3$923=i3$923.getSuccessor());
        return c1$922;
    };
    
    //AttributeDeclaration i4 at operators.ceylon (249:4-249:25)
    var i4$926=(tmp$927=f2$925(),tmp$927.setI(tmp$927.getI().getSuccessor()));
    var tmp$927;
    
    //AttributeDeclaration x4 at operators.ceylon (250:4-250:25)
    var x4$928=(tmp$929=f2$925(),tmp$929.setX(tmp$929.getX().getSuccessor()),tmp$929.getX());
    var tmp$929;
    $$$c2.check(i4$926.equals((2)),$$$cl1.String("prefix increment 5",18));
    $$$c2.check(c1$922.getI().equals((2)),$$$cl1.String("prefix increment 6",18));
    $$$c2.check(x4$928.equals((2)),$$$cl1.String("prefix increment 7",18));
    $$$c2.check(c1$922.getX().equals((2)),$$$cl1.String("prefix increment 8",18));
    $$$c2.check(i3$923.equals((2)),$$$cl1.String("prefix increment 9",18));
    
    //MethodDefinition f3 at operators.ceylon (257:4-261:4)
    function f3$930(){
        
        //AttributeDeclaration i2 at operators.ceylon (258:8-258:25)
        var i2$931=(i1$911=i1$911.getPredecessor());
        $$$c2.check(i1$911.equals((1)),$$$cl1.String("prefix decrement",16));
        $$$c2.check(i2$931.equals((1)),$$$cl1.String("prefix decrement",16));
    };
    f3$930();
    
    //AttributeDeclaration i5 at operators.ceylon (264:4-264:25)
    var i5$932=(tmp$933=f2$925(),tmp$933.setI(tmp$933.getI().getPredecessor()));
    var tmp$933;
    $$$c2.check(i5$932.equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(c1$922.getI().equals((1)),$$$cl1.String("prefix decrement",16));
    $$$c2.check(i3$923.equals((3)),$$$cl1.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (269:4-276:4)
    function f4$934(){
        
        //AttributeDeclaration i2 at operators.ceylon (270:8-270:25)
        var i2$935=(oldi1$936=i1$911,i1$911=oldi1$936.getSuccessor(),oldi1$936);
        var oldi1$936;
        
        //AttributeDeclaration x2 at operators.ceylon (271:8-271:24)
        var x2$937=(oldx$938=getX$909(),setX$909(oldx$938.getSuccessor()),oldx$938);
        var oldx$938;
        $$$c2.check(i1$911.equals((2)),$$$cl1.String("postfix increment 1",19));
        $$$c2.check(i2$935.equals((1)),$$$cl1.String("postfix increment 2",19));
        $$$c2.check(getX$909().equals((3)),$$$cl1.String("postfix increment 3",19));
        $$$c2.check(x2$937.equals((2)),$$$cl1.String("postfix increment 4",19));
    };
    f4$934();
    
    //AttributeDeclaration i6 at operators.ceylon (279:4-279:25)
    var i6$939=(tmp$940=f2$925(),oldi$941=tmp$940.getI(),tmp$940.setI(oldi$941.getSuccessor()),oldi$941);
    var tmp$940,oldi$941;
    
    //AttributeDeclaration x6 at operators.ceylon (280:4-280:25)
    var x6$942=(tmp$943=f2$925(),oldx$944=tmp$943.getX(),tmp$943.setX(oldx$944.getSuccessor()),oldx$944);
    var tmp$943,oldx$944;
    $$$c2.check(i6$939.equals((1)),$$$cl1.String("postfix increment 5",19));
    $$$c2.check(c1$922.getI().equals((2)),$$$cl1.String("postfix increment 6",19));
    $$$c2.check(x6$942.equals((2)),$$$cl1.String("postfix increment 7 ",20));
    $$$c2.check(c1$922.getX().equals((3)),$$$cl1.String("postfix increment 8 ",20));
    $$$c2.check(i3$923.equals((5)),$$$cl1.String("postfix increment 9",19));
    
    //MethodDefinition f5 at operators.ceylon (287:4-291:4)
    function f5$945(){
        
        //AttributeDeclaration i2 at operators.ceylon (288:8-288:25)
        var i2$946=(oldi1$947=i1$911,i1$911=oldi1$947.getPredecessor(),oldi1$947);
        var oldi1$947;
        $$$c2.check(i1$911.equals((1)),$$$cl1.String("postfix decrement",17));
        $$$c2.check(i2$946.equals((2)),$$$cl1.String("postfix decrement",17));
    };
    f5$945();
    
    //AttributeDeclaration i7 at operators.ceylon (294:4-294:25)
    var i7$948=(tmp$949=f2$925(),oldi$950=tmp$949.getI(),tmp$949.setI(oldi$950.getPredecessor()),oldi$950);
    var tmp$949,oldi$950;
    $$$c2.check(i7$948.equals((2)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(c1$922.getI().equals((1)),$$$cl1.String("postfix decrement",17));
    $$$c2.check(i3$923.equals((6)),$$$cl1.String("postfix decrement",17));
};

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (300:0-351:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (301:4-301:27)
    var i1$951=(1);
    var setI1$951=function(i1$952){return i1$951=i1$952;};
    
    //AttributeDeclaration x0 at operators.ceylon (302:4-302:27)
    var x0$953=(1);
    var setX0$953=function(x0$954){return x0$953=x0$954;};
    
    //AttributeGetterDefinition x at operators.ceylon (303:4-303:27)
    var getX$955=function(){
        return x0$953;
    };
    
    //AttributeSetterDefinition x at operators.ceylon (303:29-303:46)
    var setX$955=function(x$956){
        x0$953=x$956;
    };
    (i1$951=i1$951.plus((10)));
    (setX$955(getX$955().plus((10))),getX$955());
    $$$c2.check(i1$951.equals((11)),$$$cl1.String("+= operator 1",13));
    $$$c2.check(getX$955().equals((11)),$$$cl1.String("+= operator 2",13));
    
    //AttributeDeclaration i2 at operators.ceylon (309:4-309:36)
    var i2$957=(i1$951=i1$951.plus((-(5))));
    var setI2$957=function(i2$958){return i2$957=i2$958;};
    
    //AttributeDeclaration x2 at operators.ceylon (310:4-310:35)
    var x2$959=(setX$955(getX$955().plus((-(5)))),getX$955());
    var setX2$959=function(x2$960){return x2$959=x2$960;};
    $$$c2.check(i2$957.equals((6)),$$$cl1.String("+= operator 3",13));
    $$$c2.check(i1$951.equals((6)),$$$cl1.String("+= operator 4",13));
    $$$c2.check(x2$959.equals((6)),$$$cl1.String("+= operator 5",13));
    $$$c2.check(getX$955().equals((6)),$$$cl1.String("+= operator 6",13));
    
    //ClassDefinition C1 at operators.ceylon (316:4-320:4)
    function C1$961($$c1$961){
        $init$C1$961();
        if ($$c1$961===undefined)$$c1$961=new C1$961.$$;
        
        //AttributeDeclaration i at operators.ceylon (317:8-317:37)
        var i$962=(1);
        var getI=function(){return i$962;};
        $$c1$961.getI=getI;
        var setI=function(i$963){return i$962=i$963;};
        $$c1$961.setI=setI;
        
        //AttributeDeclaration x0 at operators.ceylon (318:8-318:31)
        var x0$964=(1);
        var getX0$964=function(){return x0$964;};
        $$c1$961.getX0$964=getX0$964;
        var setX0$964=function(x0$965){return x0$964=x0$965;};
        $$c1$961.setX0$964=setX0$964;
        
        //AttributeGetterDefinition x at operators.ceylon (319:8-319:38)
        var getX=function(){
            return getX0$964();
        }
        $$c1$961.getX=getX;
        
        //AttributeSetterDefinition x at operators.ceylon (319:40-319:57)
        var setX=function(x$966){
            setX0$964(x$966);
        }
        $$c1$961.setX=setX;
        return $$c1$961;
    }
    function $init$C1$961(){
        if (C1$961.$$===undefined){
            $$$cl1.initTypeProto(C1$961,'operators::testArithmeticAssignOperators.C1',$$$cl1.Basic);
        }
        return C1$961;
    }
    $init$C1$961();
    
    //AttributeDeclaration c1 at operators.ceylon (321:4-321:16)
    var c1$967=C1$961();
    
    //AttributeDeclaration i3 at operators.ceylon (322:4-322:27)
    var i3$968=(0);
    var setI3$968=function(i3$969){return i3$968=i3$969;};
    
    //MethodDefinition f at operators.ceylon (323:4-326:4)
    function f$970(){
        (i3$968=i3$968.getSuccessor());
        return c1$967;
    };
    i2$957=(tmp$971=f$970(),tmp$971.setI(tmp$971.getI().plus((11))));
    var tmp$971;
    x2$959=(tmp$972=f$970(),tmp$972.setX(tmp$972.getX().plus((11))),tmp$972.getX());
    var tmp$972;
    $$$c2.check(i2$957.equals((12)),$$$cl1.String("+= operator 7",13));
    $$$c2.check(c1$967.getI().equals((12)),$$$cl1.String("+= operator 8",13));
    $$$c2.check(x2$959.equals((12)),$$$cl1.String("+= operator 9",13));
    $$$c2.check(c1$967.getX().equals((12)),$$$cl1.String("+= operator 10",14));
    $$$c2.check(i3$968.equals((2)),$$$cl1.String("+= operator 11",14));
    i2$957=(i1$951=i1$951.minus((14)));
    $$$c2.check(i1$951.equals((-(8))),$$$cl1.String("-= operator",11));
    $$$c2.check(i2$957.equals((-(8))),$$$cl1.String("-= operator",11));
    i2$957=(i1$951=i1$951.times((-(3))));
    $$$c2.check(i1$951.equals((24)),$$$cl1.String("*= operator",11));
    $$$c2.check(i2$957.equals((24)),$$$cl1.String("*= operator",11));
    i2$957=(i1$951=i1$951.divided((5)));
    $$$c2.check(i1$951.equals((4)),$$$cl1.String("/= operator",11));
    $$$c2.check(i2$957.equals((4)),$$$cl1.String("/= operator",11));
    i2$957=(i1$951=i1$951.remainder((3)));
    $$$c2.check(i1$951.equals((1)),$$$cl1.String("%= operator",11));
    $$$c2.check(i2$957.equals((1)),$$$cl1.String("%= operator",11));
};

//MethodDefinition testAssignmentOperator at operators.ceylon (353:0-383:0)
function testAssignmentOperator(){
    
    //AttributeDeclaration i1 at operators.ceylon (354:4-354:27)
    var i1$973=(1);
    var setI1$973=function(i1$974){return i1$973=i1$974;};
    
    //AttributeDeclaration i2 at operators.ceylon (355:4-355:27)
    var i2$975=(2);
    var setI2$975=function(i2$976){return i2$975=i2$976;};
    
    //AttributeDeclaration i3 at operators.ceylon (356:4-356:27)
    var i3$977=(3);
    var setI3$977=function(i3$978){return i3$977=i3$978;};
    $$$c2.check((i1$973=(i2$975=i3$977)).equals((3)),$$$cl1.String("assignment 1",12));
    $$$c2.check(i1$973.equals((3)),$$$cl1.String("assignment 2",12));
    $$$c2.check(i2$975.equals((3)),$$$cl1.String("assignment 3",12));
    
    //AttributeGetterDefinition x1 at operators.ceylon (361:4-361:28)
    var getX1$979=function(){
        return i1$973;
    };
    
    //AttributeSetterDefinition x1 at operators.ceylon (361:30-361:51)
    var setX1$979=function(x1$980){
        i1$973=x1$980;
    };
    
    //AttributeGetterDefinition x2 at operators.ceylon (362:4-362:28)
    var getX2$981=function(){
        return i2$975;
    };
    
    //AttributeSetterDefinition x2 at operators.ceylon (362:30-362:51)
    var setX2$981=function(x2$982){
        i2$975=x2$982;
    };
    
    //AttributeGetterDefinition x3 at operators.ceylon (363:4-363:28)
    var getX3$983=function(){
        return i3$977;
    };
    
    //AttributeSetterDefinition x3 at operators.ceylon (363:30-363:51)
    var setX3$983=function(x3$984){
        i3$977=x3$984;
    };
    i1$973=(1);
    i2$975=(2);
    $$$c2.check((setX1$979((setX2$981(getX3$983()),getX2$981())),getX1$979()).equals((3)),$$$cl1.String("assignment 4",12));
    $$$c2.check(getX1$979().equals((3)),$$$cl1.String("assignment 5",12));
    $$$c2.check(getX2$981().equals((3)),$$$cl1.String("assignment 6",12));
    
    //ClassDefinition C at operators.ceylon (370:4-374:4)
    function C$985($$c$985){
        $init$C$985();
        if ($$c$985===undefined)$$c$985=new C$985.$$;
        
        //AttributeDeclaration i at operators.ceylon (371:8-371:37)
        var i$986=(1);
        var getI=function(){return i$986;};
        $$c$985.getI=getI;
        var setI=function(i$987){return i$986=i$987;};
        $$c$985.setI=setI;
        
        //AttributeDeclaration x0 at operators.ceylon (372:8-372:31)
        var x0$988=(1);
        var getX0$988=function(){return x0$988;};
        $$c$985.getX0$988=getX0$988;
        var setX0$988=function(x0$989){return x0$988=x0$989;};
        $$c$985.setX0$988=setX0$988;
        
        //AttributeGetterDefinition x at operators.ceylon (373:8-373:38)
        var getX=function(){
            return getX0$988();
        }
        $$c$985.getX=getX;
        
        //AttributeSetterDefinition x at operators.ceylon (373:40-373:57)
        var setX=function(x$990){
            setX0$988(x$990);
        }
        $$c$985.setX=setX;
        return $$c$985;
    }
    function $init$C$985(){
        if (C$985.$$===undefined){
            $$$cl1.initTypeProto(C$985,'operators::testAssignmentOperator.C',$$$cl1.Basic);
        }
        return C$985;
    }
    $init$C$985();
    
    //AttributeDeclaration o1 at operators.ceylon (375:4-375:14)
    var o1$991=C$985();
    
    //AttributeDeclaration o2 at operators.ceylon (376:4-376:14)
    var o2$992=C$985();
    $$$c2.check((o1$991.setI((o2$992.setI((3))))).equals((3)),$$$cl1.String("assignment 7",12));
    $$$c2.check(o1$991.getI().equals((3)),$$$cl1.String("assignment 8",12));
    $$$c2.check(o2$992.getI().equals((3)),$$$cl1.String("assignment 9",12));
    $$$c2.check((tmp$993=o1$991,tmp$993.setX((tmp$994=o2$992,tmp$994.setX((3)),tmp$994.getX())),tmp$993.getX()).equals((3)),$$$cl1.String("assignment 10",13));
    var tmp$993,tmp$994;
    $$$c2.check(o1$991.getX().equals((3)),$$$cl1.String("assignment 11",13));
    $$$c2.check(o2$992.getX().equals((3)),$$$cl1.String("assignment 12",13));
};

//MethodDefinition testSegments at operators.ceylon (385:0-412:0)
function testSegments(){
    
    //AttributeDeclaration seq at operators.ceylon (386:4-386:97)
    var seq$995=$$$cl1.Tuple($$$cl1.String("one",3),$$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.Tuple($$$cl1.String("nine",4),$$$cl1.Tuple($$$cl1.String("ten",3),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}});
    $$$c2.check(seq$995.segment((1),(2)).equals($$$cl1.Tuple($$$cl1.String("two",3),$$$cl1.Tuple($$$cl1.String("three",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[1:2] ",9),seq$995.segment((1),(2)).getString()]).getString());
    $$$c2.check(seq$995.segment((3),(5)).equals($$$cl1.Tuple($$$cl1.String("four",4),$$$cl1.Tuple($$$cl1.String("five",4),$$$cl1.Tuple($$$cl1.String("six",3),$$$cl1.Tuple($$$cl1.String("seven",5),$$$cl1.Tuple($$$cl1.String("eight",5),$$$cl1.getEmpty(),{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}),{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Tuple,a:{Rest:{t:$$$cl1.Empty},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}}},First:{t:$$$cl1.String},Element:{t:$$$cl1.String}})),$$$cl1.StringBuilder().appendAll([$$$cl1.String("seq[3:5] ",9),seq$995.segment((3),(5)).getString()]).getString());
    $$$c2.check($$$cl1.String("test",4).segment((1),(2)).equals($$$cl1.String("es",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("test[1:2] ",10),$$$cl1.String("test",4).segment((1),(2)).getString()]).getString());
    $$$c2.check($$$cl1.String("hello",5).segment((2),(2)).equals($$$cl1.String("ll",2)),$$$cl1.StringBuilder().appendAll([$$$cl1.String("hello[2:2] ",11),$$$cl1.String("hello",5).segment((2),(2)).getString()]).getString());
    
    //AttributeDeclaration s2 at operators.ceylon (391:4-391:18)
    var s2$996=(function(){var tmpvar$997=(3);
    if (tmpvar$997>0){
    var tmpvar$998=(0);
    var tmpvar$999=tmpvar$998;
    for (var i=1; i<tmpvar$997; i++){tmpvar$999=tmpvar$999.getSuccessor();}
    return $$$cl1.Range(tmpvar$998,tmpvar$999)
    }else return $$$cl1.getEmpty();}());
    
    //AttributeDeclaration s3 at operators.ceylon (392:4-392:18)
    var s3$1000=(function(){var tmpvar$1001=(5);
    if (tmpvar$1001>0){
    var tmpvar$1002=(2);
    var tmpvar$1003=tmpvar$1002;
    for (var i=1; i<tmpvar$1001; i++){tmpvar$1003=tmpvar$1003.getSuccessor();}
    return $$$cl1.Range(tmpvar$1002,tmpvar$1003)
    }else return $$$cl1.getEmpty();}());
    $$$c2.check(s2$996.getSize().equals((3)),$$$cl1.String("0:3 [1]",7));
    var x$1004;
    if((x$1004=s2$996.get((0)))!==null){
        $$$c2.check(x$1004.equals((0)),$$$cl1.String("0:3 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [2]",7));
    }
    var x$1005;
    if((x$1005=s2$996.get((2)))!==null){
        $$$c2.check(x$1005.equals((2)),$$$cl1.String("0:3 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("0:3 [3]",7));
    }
    $$$c2.check(s3$1000.getSize().equals((5)),$$$cl1.String("2:5 [1]",7));
    var x$1006;
    if((x$1006=s3$1000.get((0)))!==null){
        $$$c2.check(x$1006.equals((2)),$$$cl1.String("2:5 [1]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [1]",7));
    }
    var x$1007;
    if((x$1007=s3$1000.get((2)))!==null){
        $$$c2.check(x$1007.equals((4)),$$$cl1.String("2:5 [2]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [2]",7));
    }
    var x$1008;
    if((x$1008=s3$1000.get((4)))!==null){
        $$$c2.check(x$1008.equals((6)),$$$cl1.String("2:5 [3]",7));
    }else {
        $$$c2.fail($$$cl1.String("2:5 [3]",7));
    }
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1009=(0);
    if (tmpvar$1009>0){
    var tmpvar$1010=(1);
    var tmpvar$1011=tmpvar$1010;
    for (var i=1; i<tmpvar$1009; i++){tmpvar$1011=tmpvar$1011.getSuccessor();}
    return $$$cl1.Range(tmpvar$1010,tmpvar$1011)
    }else return $$$cl1.getEmpty();}()))),$$$cl1.String("1:0 empty",9));
    $$$c2.check((!$$$cl1.nonempty((function(){var tmpvar$1012=(-(1));
    if (tmpvar$1012>0){
    var tmpvar$1013=(1);
    var tmpvar$1014=tmpvar$1013;
    for (var i=1; i<tmpvar$1012; i++){tmpvar$1014=tmpvar$1014.getSuccessor();}
    return $$$cl1.Range(tmpvar$1013,tmpvar$1014)
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
