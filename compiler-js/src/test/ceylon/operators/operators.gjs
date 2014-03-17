(function(define) { define(function(require, exports, module) {
var $$METAMODEL$$={"$mod-deps":["ceylon.language\/1.0.0","check\/0.1"],"$mod-name":"operators","$mod-version":"0.1","$mod-bin":"6.0","operators":{"testComparisonOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testComparisonOperators"},"NullsafeTest":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$m":{"f":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"$mt":"mthd","$an":{"shared":[]},"$nm":"f"},"f2":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"x"}]],"$mt":"mthd","$an":{"shared":[]},"$m":{"x":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"x"}},"$nm":"f2"}},"$nm":"NullsafeTest"},"testAssignmentOperator":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$c":{"C":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x0":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x0"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"i"}},"$nm":"C"}},"$nm":"testAssignmentOperator"},"testArithmeticAssignOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f":{"$t":{"$pk":"operators","$nm":"C1"},"$mt":"mthd","$nm":"f"}},"$c":{"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x0":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x0"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"i"}},"$nm":"C1"}},"$nm":"testArithmeticAssignOperators"},"test":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$an":{"shared":[]},"$nm":"test"},"compareStringNumber":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"compareStringNumber"},"testOtherOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testOtherOperators"},"testCollectionOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testCollectionOperators"},"testNullsafeOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"obj2_f":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"obj2_f"},"getNullsafe":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$pk":"operators","$nm":"NullsafeTest"}]},"$mt":"mthd","$nm":"getNullsafe"},"f4":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"f4"}},"$nm":"testNullsafeOperators"},"testBooleanOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testBooleanOperators"},"testEnumerations":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testEnumerations"},"testIncDecOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$m":{"f1":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f1"},"f3":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f3"},"f2":{"$t":{"$pk":"operators","$nm":"C1"},"$mt":"mthd","$nm":"f2"},"f5":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f5"},"f4":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"f4"}},"$c":{"C1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$at":{"x0":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"actual":[],"variable":[]},"$nm":"x0"},"x":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"gttr","$an":{"shared":[]},"$nm":"x"},"i":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"},"var":"1","$mt":"attr","$an":{"shared":[],"variable":[]},"$nm":"i"}},"$nm":"C1"}},"$nm":"testIncDecOperators"},"testSegments":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testSegments"},"testFloatOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testFloatOperators"},"OpTest1":{"super":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Basic"},"$mt":"cls","$nm":"OpTest1"},"testIntegerOperators":{"$t":{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Anything"},"$mt":"mthd","$nm":"testIntegerOperators"},"nullsafeTest":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$ps":[[{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"prm","$pt":"f","$nm":"f"}]],"$mt":"mthd","$m":{"f":{"$t":{"comp":"u","$ts":[{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Null"},{"$md":"ceylon.language","$pk":"ceylon.language","$nm":"Integer"}]},"$mt":"mthd","$nm":"f"}},"$nm":"nullsafeTest"}}};
exports.$$METAMODEL$$=function(){return $$METAMODEL$$;};
var $$$cl4138=require('ceylon/language/1.0.0/ceylon.language-1.0.0');
$$$cl4138.$addmod$($$$cl4138,'ceylon.language/1.0.0');
var $$$c4139=require('check/0.1/check-0.1');
$$$cl4138.$addmod$($$$c4139,'check/0.1');

//MethodDef testEnumerations at collections.ceylon (3:0-31:0)
function testEnumerations(){
    
    //AttributeDecl seq at collections.ceylon (4:4-4:21)
    var seq$5147=$$$cl4138.Tuple((1),$$$cl4138.Tuple((2),$$$cl4138.Tuple((3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}});
    
    //AttributeDecl lcomp at collections.ceylon (5:4-5:37)
    var lcomp$5148=$$$cl4138.Comprehension(function(){
        //Comprehension at collections.ceylon (5:17-5:35)
        var it$5149=$$$cl4138.String("hola",4).iterator();
        var c$5150=$$$cl4138.getFinished();
        var next$c$5150=function(){return c$5150=it$5149.next();}
        next$c$5150();
        return function(){
            if(c$5150!==$$$cl4138.getFinished()){
                var c$5150$5151=c$5150;
                var tmpvar$5152=c$5150$5151;
                next$c$5150();
                return tmpvar$5152;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}});
    
    //AttributeDecl ecomp at collections.ceylon (6:4-6:37)
    var ecomp$5153=$$$cl4138.Comprehension(function(){
        //Comprehension at collections.ceylon (6:17-6:35)
        var it$5154=$$$cl4138.String("hola",4).iterator();
        var c$5155=$$$cl4138.getFinished();
        var next$c$5155=function(){return c$5155=it$5154.next();}
        next$c$5155();
        return function(){
            if(c$5155!==$$$cl4138.getFinished()){
                var c$5155$5156=c$5155;
                var tmpvar$5157=c$5155$5156;
                next$c$5155();
                return tmpvar$5157;
            }
            return $$$cl4138.getFinished();
        }
    },{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}}).sequence;
    
    //AttributeDecl s2 at collections.ceylon (7:4-7:24)
    var s2$5158=[(0)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Integer}}).chain(seq$5147,{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Integer}});
    
    //AttributeDecl s3 at collections.ceylon (8:4-8:28)
    var s3$5159=[$$$cl4138.Character(65)].reifyCeylonType({Absent:{t:$$$cl4138.Nothing},Element:{t:$$$cl4138.Character}}).chain(lcomp$5148,{Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.Character}});
    
    //AttributeDecl t1 at collections.ceylon (9:4-9:20)
    var t1$5160=$$$cl4138.Tuple((1),$$$cl4138.Tuple((2),$$$cl4138.Tuple((3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}}),{Rest:{t:'T', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}});
    
    //AttributeDecl t2 at collections.ceylon (10:4-10:22)
    var t2$5161=$$$cl4138.Tuple((0),seq$5147,{Rest:{t:'T', l:[{t:$$$cl4138.Integer},{t:$$$cl4138.Integer},{t:$$$cl4138.Integer}]},First:{t:$$$cl4138.Integer},Element:{t:$$$cl4138.Integer}});
    $$$c4139.check($$$cl4138.className(seq$5147).startsWith($$$cl4138.String("ceylon.language::Tuple",22)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{1,2,3} is not a Tuple but a ",29),$$$cl4138.className(seq$5147).string]).string);
    $$$c4139.check((!$$$cl4138.className(lcomp$5148).startsWith($$$cl4138.String("ceylon.language::Tuple",22))),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("lazy comprehension is a Tuple ",30),$$$cl4138.className(lcomp$5148).string]).string);
    $$$c4139.check($$$cl4138.className(ecomp$5153).startsWith($$$cl4138.String("ceylon.language::ArraySequence",30)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("eager comprehension is not a Tuple but a ",41),$$$cl4138.className(ecomp$5153).string]).string);
    $$$c4139.check((!$$$cl4138.className(s2$5158).startsWith($$$cl4138.String("ceylon.language::Tuple",22))),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{0,*seq} is a Tuple ",20),$$$cl4138.className(s2$5158).string]).string);
    $$$c4139.check((!$$$cl4138.className(s3$5159).startsWith($$$cl4138.String("ceylon.language::Tuple",22))),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{x,*iter} is a Tuple ",21),$$$cl4138.className(s3$5159).string]).string);
    $$$c4139.check($$$cl4138.className(t1$5160).startsWith($$$cl4138.String("ceylon.language::Tuple",22)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("[1,2,3] is not a Tuple but a ",29),$$$cl4138.className(t1$5160).string]).string);
    $$$c4139.check($$$cl4138.className(t2$5161).startsWith($$$cl4138.String("ceylon.language::Tuple",22)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("[0,*seq] is not a Tuple but a ",30),$$$cl4138.className(t2$5161).string]).string);
    $$$c4139.check(seq$5147.equals(t1$5160),$$$cl4138.String("{1,2,3} != [1,2,3]",18));
    $$$c4139.check((!$$$cl4138.className(t2$5161).equals($$$cl4138.className(s2$5158))),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{0,*seq} != [0,*seq] ",21),$$$cl4138.className(t2$5161).string,$$$cl4138.String(" vs",3),$$$cl4138.className(s2$5158).string]).string);
    $$$c4139.check(seq$5147.size.equals((3)),$$$cl4138.String("seq.size!=3",11));
    $$$c4139.check(lcomp$5148.sequence.size.equals((4)),$$$cl4138.String("lcomp.size!=4",13));
    $$$c4139.check(ecomp$5153.size.equals((4)),$$$cl4138.String("ecomp.size!=4",13));
    $$$c4139.check(s2$5158.size.equals((4)),$$$cl4138.String("s2.size!=4",10));
    $$$c4139.check(s3$5159.sequence.size.equals((5)),$$$cl4138.String("s3.size!=5",10));
    $$$c4139.check(t1$5160.size.equals((3)),$$$cl4138.String("t1.size!=3",10));
    $$$c4139.check(t2$5161.size.equals((4)),$$$cl4138.String("t2.size!=4",10));
    $$$c4139.check((!$$$cl4138.className(lcomp$5148).startsWith($$$cl4138.String("ceylon.language::Tuple",22))),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{*comp} is not Tuple but ",25),$$$cl4138.className(lcomp$5148).string]).string);
    $$$c4139.check($$$cl4138.className(ecomp$5153).startsWith($$$cl4138.String("ceylon.language::ArraySequence",30)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{*ecomp} is not Tuple but ",26),$$$cl4138.className(ecomp$5153).string]).string);
    $$$c4139.check($$$cl4138.className(seq$5147).startsWith($$$cl4138.String("ceylon.language::Tuple",22)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("{*seq} is not Tuple but ",24),$$$cl4138.className(seq$5147).string]).string);
};testEnumerations.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testEnumerations']};};
exports.$mod$ans$=function(){return[$$$cl4138.by([$$$cl4138.String("Enrique Zamudio",15),$$$cl4138.String("Ivo Kasiuk",10)].reifyCeylonType({Absent:{t:$$$cl4138.Null},Element:{t:$$$cl4138.String}}))];};
exports.$mod$imps=function(){return{
'check/0.1':[]
};};

//MethodDef testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDecl i1 at operators.ceylon (5:4-5:28)
    var i1$5162=(-(4));
    function setI1$5162(i1$5163){return i1$5162=i1$5163;};
    i1$5162=(-i1$5162);
    $$$c4139.check(i1$5162.equals((4)),$$$cl4138.String("negation",8));
    i1$5162=(+(-(987654)));
    $$$c4139.check(i1$5162.equals((-(987654))),$$$cl4138.String("positive",8));
    i1$5162=(+(0));
    $$$c4139.check(i1$5162.equals((0)),$$$cl4138.String("+0=0",4));
    i1$5162=(-(0));
    $$$c4139.check(i1$5162.equals((0)),$$$cl4138.String("+0=0",4));
    
    //AttributeDecl i2 at operators.ceylon (15:4-15:35)
    var i2$5164=(123).plus((456));
    function setI2$5164(i2$5165){return i2$5164=i2$5165;};
    $$$c4139.check(i2$5164.equals((579)),$$$cl4138.String("addition",8));
    i1$5162=i2$5164.minus((16));
    $$$c4139.check(i1$5162.equals((563)),$$$cl4138.String("subtraction",11));
    i2$5164=(-i1$5162).plus(i2$5164).minus((1));
    $$$c4139.check(i2$5164.equals((15)),$$$cl4138.String("-i1+i2-1",8));
    i1$5162=(3).times((7));
    $$$c4139.check(i1$5162.equals((21)),$$$cl4138.String("multiplication",14));
    i2$5164=i1$5162.times((2));
    $$$c4139.check(i2$5164.equals((42)),$$$cl4138.String("multiplication",14));
    i2$5164=(17).divided((4));
    $$$c4139.check(i2$5164.equals((4)),$$$cl4138.String("integer division",16));
    i1$5162=i2$5164.times((516)).divided((-i1$5162));
    $$$c4139.check(i1$5162.equals((-(98))),$$$cl4138.String("i2*516/-i1",10));
    i1$5162=(15).remainder((4));
    $$$c4139.check(i1$5162.equals((3)),$$$cl4138.String("modulo",6));
    i2$5164=(312).remainder((12));
    $$$c4139.check(i2$5164.equals((0)),$$$cl4138.String("modulo",6));
    i1$5162=(2).power((10));
    $$$c4139.check(i1$5162.equals((1024)),$$$cl4138.String("power",5));
    i2$5164=(10).power((6));
    $$$c4139.check(i2$5164.equals((1000000)),$$$cl4138.String("power",5));
};testIntegerOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testIntegerOperators']};};

//MethodDef testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDecl f1 at operators.ceylon (44:4-44:28)
    var f1$5166=$$$cl4138.Float(4.2).negativeValue;
    function setF1$5166(f1$5167){return f1$5166=f1$5167;};
    f1$5166=f1$5166.negativeValue;
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(4.2)),$$$cl4138.String("negation",8));
    f1$5166=(+$$$cl4138.Float(987654.9925567).negativeValue);
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(987654.9925567).negativeValue),$$$cl4138.String("positive",8));
    f1$5166=(+$$$cl4138.Float(0.0));
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(0.0)),$$$cl4138.String("+0.0=0.0",8));
    f1$5166=$$$cl4138.Float(0.0).negativeValue;
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(0.0)),$$$cl4138.String("-0.0=0.0",8));
    
    //AttributeDecl f2 at operators.ceylon (54:4-54:42)
    var f2$5168=$$$cl4138.Float(3.14159265).plus($$$cl4138.Float(456.0));
    function setF2$5168(f2$5169){return f2$5168=f2$5169;};
    $$$c4139.check(f2$5168.equals($$$cl4138.Float(459.14159265)),$$$cl4138.String("addition",8));
    f1$5166=f2$5168.minus($$$cl4138.Float(0.0016));
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(459.13999265)),$$$cl4138.String("subtraction",11));
    f2$5168=f1$5166.negativeValue.plus(f2$5168).minus($$$cl4138.Float(1.2));
    $$$c4139.check(f2$5168.equals($$$cl4138.Float(1.1984000000000037).negativeValue),$$$cl4138.String("-f1+f2-1.2",10));
    f1$5166=$$$cl4138.Float(3.0).times($$$cl4138.Float(0.79));
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(2.37)),$$$cl4138.String("multiplication",14));
    f2$5168=f1$5166.times($$$cl4138.Float(2.0e13));
    $$$c4139.check(f2$5168.equals($$$cl4138.Float(47400000000000.0)),$$$cl4138.String("multiplication",14));
    f2$5168=$$$cl4138.Float(17.1).divided($$$cl4138.Float(4.0E-18));
    $$$c4139.check(f2$5168.equals($$$cl4138.Float(4275000000000000000.0)),$$$cl4138.String("division",8));
    f1$5166=f2$5168.times($$$cl4138.Float(51.6e2)).divided(f1$5166.negativeValue);
    $$$c4139.check(f2$5168.equals($$$cl4138.Float(4275000000000000000.0)),$$$cl4138.String("f2*51.6e2/-f1",13));
    f1$5166=$$$cl4138.Float(150.0).power($$$cl4138.Float(0.5));
    $$$c4139.check(f1$5166.equals($$$cl4138.Float(12.24744871391589)),$$$cl4138.String("power",5));
};testFloatOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testFloatOperators']};};

//ClassDef OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
OpTest1.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['operators','OpTest1']};};
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl4138.initTypeProto(OpTest1,'operators::OpTest1',$$$cl4138.Basic);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDef testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDecl o1 at operators.ceylon (77:4-77:24)
    var o1$5170=OpTest1();
    
    //AttributeDecl o2 at operators.ceylon (78:4-78:24)
    var o2$5171=OpTest1();
    
    //AttributeDecl b1 at operators.ceylon (79:4-79:35)
    var b1$5172=(o1$5170===o2$5171);
    function setB1$5172(b1$5173){return b1$5172=b1$5173;};
    $$$c4139.check((!b1$5172),$$$cl4138.String("identity",8));
    
    //AttributeDecl b2 at operators.ceylon (81:4-81:35)
    var b2$5174=(o1$5170===o1$5170);
    function setB2$5174(b2$5175){return b2$5174=b2$5175;};
    $$$c4139.check(b2$5174,$$$cl4138.String("identity",8));
    b1$5172=o1$5170.equals(o2$5171);
    $$$c4139.check((!b1$5172),$$$cl4138.String("equals",6));
    b2$5174=o1$5170.equals(o1$5170);
    $$$c4139.check(b2$5174,$$$cl4138.String("equals",6));
    b1$5172=(1).equals((2));
    $$$c4139.check((!b1$5172),$$$cl4138.String("equals",6));
    b2$5174=(!(1).equals((2)));
    $$$c4139.check(b2$5174,$$$cl4138.String("not equal",9));
    
    //AttributeDecl b3 at operators.ceylon (92:4-92:29)
    var b3$5176=(!b2$5174);
    function setB3$5176(b3$5177){return b3$5176=b3$5177;};
    $$$c4139.check((!b3$5176),$$$cl4138.String("not",3));
    b1$5172=(true&&false);
    $$$c4139.check((!b1$5172),$$$cl4138.String("and",3));
    b2$5174=(b1$5172&&true);
    $$$c4139.check((!b2$5174),$$$cl4138.String("and",3));
    b3$5176=(true&&true);
    $$$c4139.check(b3$5176,$$$cl4138.String("and",3));
    b1$5172=(true||false);
    $$$c4139.check(b1$5172,$$$cl4138.String("or",2));
    b2$5174=(false||b1$5172);
    $$$c4139.check(b2$5174,$$$cl4138.String("or",2));
    b3$5176=(false||false);
    $$$c4139.check((!b3$5176),$$$cl4138.String("or",2));
};testBooleanOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testBooleanOperators']};};

//MethodDef testComparisonOperators at operators.ceylon (109:0-152:0)
function testComparisonOperators(){
    
    //AttributeDecl c1 at operators.ceylon (110:4-110:37)
    var c1$5178=$$$cl4138.String("str1",4).compare($$$cl4138.String("str2",4));
    $$$c4139.check(c1$5178.equals($$$cl4138.getSmaller()),$$$cl4138.String("compare",7));
    
    //AttributeDecl c2 at operators.ceylon (112:4-112:37)
    var c2$5179=$$$cl4138.String("str2",4).compare($$$cl4138.String("str1",4));
    $$$c4139.check(c2$5179.equals($$$cl4138.getLarger()),$$$cl4138.String("compare",7));
    
    //AttributeDecl c3 at operators.ceylon (114:4-114:37)
    var c3$5180=$$$cl4138.String("str1",4).compare($$$cl4138.String("str1",4));
    $$$c4139.check(c3$5180.equals($$$cl4138.getEqual()),$$$cl4138.String("compare",7));
    
    //AttributeDecl c4 at operators.ceylon (116:4-116:29)
    var c4$5181=$$$cl4138.String("",0).compare($$$cl4138.String("",0));
    $$$c4139.check(c4$5181.equals($$$cl4138.getEqual()),$$$cl4138.String("compare",7));
    
    //AttributeDecl c5 at operators.ceylon (118:4-118:33)
    var c5$5182=$$$cl4138.String("str1",4).compare($$$cl4138.String("",0));
    $$$c4139.check(c5$5182.equals($$$cl4138.getLarger()),$$$cl4138.String("compare",7));
    
    //AttributeDecl c6 at operators.ceylon (120:4-120:33)
    var c6$5183=$$$cl4138.String("",0).compare($$$cl4138.String("str2",4));
    $$$c4139.check(c6$5183.equals($$$cl4138.getSmaller()),$$$cl4138.String("compare",7));
    
    //AttributeDecl b1 at operators.ceylon (123:4-123:41)
    var b1$5184=$$$cl4138.String("str1",4).compare($$$cl4138.String("str2",4)).equals($$$cl4138.getSmaller());
    function setB1$5184(b1$5185){return b1$5184=b1$5185;};
    $$$c4139.check(b1$5184,$$$cl4138.String("smaller",7));
    
    //AttributeDecl b2 at operators.ceylon (125:4-125:41)
    var b2$5186=$$$cl4138.String("str1",4).compare($$$cl4138.String("str2",4)).equals($$$cl4138.getLarger());
    function setB2$5186(b2$5187){return b2$5186=b2$5187;};
    $$$c4139.check((!b2$5186),$$$cl4138.String("larger",6));
    
    //AttributeDecl b3 at operators.ceylon (127:4-127:42)
    var b3$5188=($$$cl4138.String("str1",4).compare($$$cl4138.String("str2",4))!==$$$cl4138.getLarger());
    function setB3$5188(b3$5189){return b3$5188=b3$5189;};
    $$$c4139.check(b3$5188,$$$cl4138.String("small as",8));
    
    //AttributeDecl b4 at operators.ceylon (129:4-129:42)
    var b4$5190=($$$cl4138.String("str1",4).compare($$$cl4138.String("str2",4))!==$$$cl4138.getSmaller());
    function setB4$5190(b4$5191){return b4$5190=b4$5191;};
    $$$c4139.check((!b4$5190),$$$cl4138.String("large as",8));
    b1$5184=$$$cl4138.String("str1",4).compare($$$cl4138.String("str1",4)).equals($$$cl4138.getSmaller());
    $$$c4139.check((!b1$5184),$$$cl4138.String("smaller",7));
    b2$5186=$$$cl4138.String("str1",4).compare($$$cl4138.String("str1",4)).equals($$$cl4138.getLarger());
    $$$c4139.check((!b2$5186),$$$cl4138.String("larger",6));
    b3$5188=($$$cl4138.String("str1",4).compare($$$cl4138.String("str1",4))!==$$$cl4138.getLarger());
    $$$c4139.check(b3$5188,$$$cl4138.String("small as",8));
    b4$5190=($$$cl4138.String("str1",4).compare($$$cl4138.String("str1",4))!==$$$cl4138.getSmaller());
    $$$c4139.check(b4$5190,$$$cl4138.String("large as",8));
    
    //AttributeDecl a at operators.ceylon (140:4-140:15)
    var a$5192=(0);
    
    //AttributeDecl c at operators.ceylon (141:4-141:16)
    var c$5193=(10);
    $$$c4139.check((tmpvar$5194=(5),tmpvar$5194.compare(a$5192)===$$$cl4138.getLarger()&&tmpvar$5194.compare(c$5193)===$$$cl4138.getSmaller()),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<5<",3),c$5193.string]).string);
    $$$c4139.check((tmpvar$5195=(0),tmpvar$5195.compare(a$5192)!==$$$cl4138.getSmaller()&&tmpvar$5195.compare(c$5193)===$$$cl4138.getSmaller()),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<=0<",4),c$5193.string]).string);
    $$$c4139.check((tmpvar$5196=(10),tmpvar$5196.compare(a$5192)===$$$cl4138.getLarger()&&tmpvar$5196.compare(c$5193)!==$$$cl4138.getLarger()),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<10<=",5),c$5193.string]).string);
    $$$c4139.check((tmpvar$5197=(0),tmpvar$5197.compare(a$5192)!==$$$cl4138.getSmaller()&&tmpvar$5197.compare(c$5193)!==$$$cl4138.getLarger()),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<=0<=",5),c$5193.string]).string);
    $$$c4139.check((tmpvar$5198=(10),tmpvar$5198.compare(a$5192)!==$$$cl4138.getSmaller()&&tmpvar$5198.compare(c$5193)!==$$$cl4138.getLarger()),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<=10<=",6),c$5193.string]).string);
    $$$c4139.check((!(tmpvar$5199=(15),tmpvar$5199.compare(a$5192)===$$$cl4138.getLarger()&&tmpvar$5199.compare(c$5193)===$$$cl4138.getSmaller())),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<15<",4),c$5193.string,$$$cl4138.String(" WTF",4)]).string);
    $$$c4139.check((!(tmpvar$5200=(10),tmpvar$5200.compare(a$5192)!==$$$cl4138.getSmaller()&&tmpvar$5200.compare(c$5193)===$$$cl4138.getSmaller())),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<=10<",5),c$5193.string,$$$cl4138.String(" WTF",4)]).string);
    $$$c4139.check((!(tmpvar$5201=(0),tmpvar$5201.compare(a$5192)===$$$cl4138.getLarger()&&tmpvar$5201.compare(c$5193)!==$$$cl4138.getLarger())),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<0<=",4),c$5193.string,$$$cl4138.String(" WTF",4)]).string);
    $$$c4139.check((!(tmpvar$5202=(11),tmpvar$5202.compare(a$5192)!==$$$cl4138.getSmaller()&&tmpvar$5202.compare(c$5193)!==$$$cl4138.getLarger())),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<=11<=",6),c$5193.string,$$$cl4138.String(" WTF",4)]).string);
    $$$c4139.check((!(tmpvar$5203=(-(1)),tmpvar$5203.compare(a$5192)!==$$$cl4138.getSmaller()&&tmpvar$5203.compare(c$5193)!==$$$cl4138.getLarger())),$$$cl4138.StringBuilder().appendAll([a$5192.string,$$$cl4138.String("<=-1<=",6),c$5193.string,$$$cl4138.String(" WTF",4)]).string);
};testComparisonOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testComparisonOperators']};};

//MethodDef testOtherOperators at operators.ceylon (154:0-166:0)
function testOtherOperators(){
    
    //AttributeDecl entry at operators.ceylon (155:4-155:42)
    var entry$5204=$$$cl4138.Entry((47),$$$cl4138.String("hi there",8),{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.String}});
    $$$c4139.check(entry$5204.key.equals((47)),$$$cl4138.String("entry key",9));
    $$$c4139.check(entry$5204.item.equals($$$cl4138.String("hi there",8)),$$$cl4138.String("entry item",10));
    
    //AttributeDecl entry2 at operators.ceylon (158:4-158:30)
    var entry2$5205=$$$cl4138.Entry(true,entry$5204,{Key:{t:$$$cl4138.true$5003},Item:{t:$$$cl4138.Entry,a:{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.String}}}});
    $$$c4139.check(entry2$5205.key.equals(true),$$$cl4138.String("entry key",9));
    $$$c4139.check(entry2$5205.item.equals($$$cl4138.Entry((47),$$$cl4138.String("hi there",8),{Key:{t:$$$cl4138.Integer},Item:{t:$$$cl4138.String}})),$$$cl4138.String("entry item",10));
    
    //AttributeDecl s1 at operators.ceylon (162:4-162:41)
    var s1$5206=(opt$5207=(true?$$$cl4138.String("ok",2):null),opt$5207!==null?opt$5207:$$$cl4138.String("noo",3));
    var opt$5207;
    $$$c4139.check(s1$5206.equals($$$cl4138.String("ok",2)),$$$cl4138.String("then/else 1",11));
    
    //AttributeDecl s2 at operators.ceylon (164:4-164:47)
    var s2$5208=(opt$5209=(false?$$$cl4138.String("what?",5):null),opt$5209!==null?opt$5209:$$$cl4138.String("great",5));
    var opt$5209;
    $$$c4139.check(s2$5208.equals($$$cl4138.String("great",5)),$$$cl4138.String("then/else 2",11));
};testOtherOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testOtherOperators']};};

//MethodDef testCollectionOperators at operators.ceylon (168:0-180:0)
function testCollectionOperators(){
    
    //AttributeDecl seq1 at operators.ceylon (169:4-169:33)
    var seq1$5210=$$$cl4138.Tuple($$$cl4138.String("one",3),$$$cl4138.Tuple($$$cl4138.String("two",3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}});
    
    //AttributeDecl s1 at operators.ceylon (170:4-170:23)
    var s1$5211=seq1$5210.$get((0));
    $$$c4139.check(s1$5211.equals($$$cl4138.String("one",3)),$$$cl4138.String("lookup",6));
    
    //AttributeDecl s2 at operators.ceylon (172:4-172:28)
    var s2$5212=seq1$5210.$get((2));
    $$$c4139.check((!$$$cl4138.exists(s2$5212)),$$$cl4138.String("lookup",6));
    
    //AttributeDecl s3 at operators.ceylon (174:4-174:29)
    var s3$5213=seq1$5210.$get((-(1)));
    $$$c4139.check((!$$$cl4138.exists(s3$5213)),$$$cl4138.String("lookup",6));
};testCollectionOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testCollectionOperators']};};

//ClassDef NullsafeTest at operators.ceylon (182:0-187:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    
    //MethodDef f at operators.ceylon (183:4-183:33)
    function f(){
        return (1);
    }
    $$nullsafeTest.f=f;
    f.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$ps:[],$cont:NullsafeTest,$an:function(){return[$$$cl4138.shared()];},d:['operators','NullsafeTest','$m','f']};};
    
    //MethodDef f2 at operators.ceylon (184:4-186:4)
    function f2(x$5214){
        return x$5214();
    }
    $$nullsafeTest.f2=f2;
    f2.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$ps:[{$nm:'x',$mt:'prm',$pt:'f',$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$an:function(){return[];}}],$cont:NullsafeTest,$an:function(){return[$$$cl4138.shared()];},d:['operators','NullsafeTest','$m','f2']};};
    return $$nullsafeTest;
}
NullsafeTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['operators','NullsafeTest']};};
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl4138.initTypeProto(NullsafeTest,'operators::NullsafeTest',$$$cl4138.Basic);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDef nullsafeTest at operators.ceylon (189:0-191:0)
function nullsafeTest(f$5215){
    return f$5215();
};nullsafeTest.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$ps:[{$nm:'f',$mt:'prm',$pt:'f',$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$an:function(){return[];}}],d:['operators','nullsafeTest']};};

//MethodDef testNullsafeOperators at operators.ceylon (193:0-234:0)
function testNullsafeOperators(){
    
    //AttributeDecl seq at operators.ceylon (194:4-194:27)
    var seq$5216=$$$cl4138.Tuple($$$cl4138.String("hi",2),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}});
    
    //AttributeDecl s1 at operators.ceylon (195:4-195:34)
    var s1$5217=(opt$5218=seq$5216.$get((0)),opt$5218!==null?opt$5218:$$$cl4138.String("null",4));
    var opt$5218;
    $$$c4139.check(s1$5217.equals($$$cl4138.String("hi",2)),$$$cl4138.String("default 1",9));
    
    //AttributeDecl s2 at operators.ceylon (197:4-197:34)
    var s2$5219=(opt$5220=seq$5216.$get((1)),opt$5220!==null?opt$5220:$$$cl4138.String("null",4));
    var opt$5220;
    $$$c4139.check(s2$5219.equals($$$cl4138.String("null",4)),$$$cl4138.String("default 2",9));
    
    //AttributeDecl s3 at operators.ceylon (200:4-200:21)
    var s3$5221=null;
    
    //AttributeDecl s4 at operators.ceylon (201:4-201:23)
    var s4$5222=$$$cl4138.String("test",4);
    
    //AttributeDecl s5 at operators.ceylon (202:4-202:42)
    var s5$5223=(opt$5224=(opt$5225=s3$5221,opt$5225!==null?opt$5225.uppercased:null),opt$5224!==null?opt$5224:$$$cl4138.String("null",4));
    var opt$5224,opt$5225;
    
    //AttributeDecl s6 at operators.ceylon (203:4-203:42)
    var s6$5226=(opt$5227=(opt$5228=s4$5222,opt$5228!==null?opt$5228.uppercased:null),opt$5227!==null?opt$5227:$$$cl4138.String("null",4));
    var opt$5227,opt$5228;
    $$$c4139.check(s5$5223.equals($$$cl4138.String("null",4)),$$$cl4138.String("nullsafe member 1",17));
    $$$c4139.check(s6$5226.equals($$$cl4138.String("TEST",4)),$$$cl4138.String("nullsafe member 2",17));
    
    //AttributeDecl obj at operators.ceylon (206:4-206:28)
    var obj$5229=null;
    
    //AttributeDecl i at operators.ceylon (207:4-207:25)
    var i$5230=(opt$5231=obj$5229,$$$cl4138.JsCallable(opt$5231,opt$5231!==null?opt$5231.f:null))();
    var opt$5231;
    $$$c4139.check((!$$$cl4138.exists(i$5230)),$$$cl4138.String("nullsafe invoke",15));
    
    //AttributeDecl f2 at operators.ceylon (209:4-209:37)
    var f2$5232=$$$cl4138.$JsCallable((opt$5233=obj$5229,$$$cl4138.JsCallable(opt$5233,opt$5233!==null?opt$5233.f:null)),[],{Arguments:{t:$$$cl4138.Empty},Return:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]}});
    var opt$5233;
    $$$c4139.check((!$$$cl4138.exists(nullsafeTest($$$cl4138.$JsCallable(f2$5232,[],{Arguments:{t:$$$cl4138.Empty},Return:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]}})))),$$$cl4138.String("nullsafe method ref",19));
    
    //AttributeDecl f3 at operators.ceylon (211:4-211:38)
    var f3$5234=$$$cl4138.$JsCallable((opt$5235=obj$5229,$$$cl4138.JsCallable(opt$5235,opt$5235!==null?opt$5235.f:null)),[],{Arguments:{t:$$$cl4138.Empty},Return:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]}});
    var opt$5235;
    $$$c4139.check($$$cl4138.exists(f3$5234),$$$cl4138.String("nullsafe method ref 2",21));
    (opt$5236=obj$5229,$$$cl4138.JsCallable(opt$5236,opt$5236!==null?opt$5236.f:null))();
    var opt$5236;
    $$$c4139.check((!$$$cl4138.exists((opt$5237=obj$5229,$$$cl4138.JsCallable(opt$5237,opt$5237!==null?opt$5237.f:null))())),$$$cl4138.String("nullsafe simple call",20));
    var opt$5237;
    
    //MethodDef getNullsafe at operators.ceylon (215:4-215:46)
    function getNullsafe$5238(){
        return obj$5229;
    };getNullsafe$5238.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:NullsafeTest}]},$ps:[],d:['operators','testNullsafeOperators','$m','getNullsafe']};};
    
    //MethodDecl f4 at operators.ceylon (216:4-216:39)
    var f4$5239=function (){
        return (opt$5240=getNullsafe$5238(),$$$cl4138.JsCallable(opt$5240,opt$5240!==null?opt$5240.f:null))();
    };
    f4$5239.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$ps:[],d:['operators','testNullsafeOperators','$m','f4']};};
    var opt$5240;
    
    //AttributeDecl result_f4 at operators.ceylon (217:4-217:29)
    var result_f4$5241=f4$5239();
    $$$c4139.check((!$$$cl4138.exists(result_f4$5241)),$$$cl4138.String("nullsafe invoke 2",17));
    
    //AttributeDecl i2 at operators.ceylon (219:4-219:36)
    var i2$5242=(opt$5243=getNullsafe$5238(),$$$cl4138.JsCallable(opt$5243,opt$5243!==null?opt$5243.f:null))();
    var opt$5243;
    $$$c4139.check((!$$$cl4138.exists(i2$5242)),$$$cl4138.String("nullsafe invoke 3",17));
    $$$c4139.check((!$$$cl4138.exists(NullsafeTest().f2($$$cl4138.$JsCallable((opt$5244=getNullsafe$5238(),$$$cl4138.JsCallable(opt$5244,opt$5244!==null?opt$5244.f:null)),[],{Arguments:{t:$$$cl4138.Empty},Return:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]}})))),$$$cl4138.String("nullsafe method ref 3",21));
    var opt$5244;
    
    //AttributeDecl obj2 at operators.ceylon (222:4-222:39)
    var obj2$5245=NullsafeTest();
    var i3$5246;
    if((i3$5246=(opt$5247=obj2$5245,$$$cl4138.JsCallable(opt$5247,opt$5247!==null?opt$5247.f:null))())!==null){
        $$$c4139.check(i3$5246.equals((1)),$$$cl4138.String("nullsafe invoke 4 (result)",26));
    }else {
        $$$c4139.fail($$$cl4138.String("nullsafe invoke 4 (null)",24));
    }
    var opt$5247;
    
    //MethodDecl obj2_f at operators.ceylon (228:4-228:34)
    var obj2_f$5248=function (){
        return (opt$5249=obj2$5245,$$$cl4138.JsCallable(opt$5249,opt$5249!==null?opt$5249.f:null))();
    };
    obj2_f$5248.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:'u', l:[{t:$$$cl4138.Null},{t:$$$cl4138.Integer}]},$ps:[],d:['operators','testNullsafeOperators','$m','obj2_f']};};
    var opt$5249;
    var i3$5250;
    if((i3$5250=obj2_f$5248())!==null){
        $$$c4139.check(i3$5250.equals((1)),$$$cl4138.String("nullsafe method ref 4 (result)",30));
    }else {
        $$$c4139.fail($$$cl4138.String("nullsafe method ref 4 (null)",28));
    }
};testNullsafeOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testNullsafeOperators']};};

//MethodDef testIncDecOperators at operators.ceylon (236:0-311:0)
function testIncDecOperators(){
    
    //AttributeDecl x0 at operators.ceylon (237:4-237:27)
    var x0$5251=(1);
    function setX0$5251(x0$5252){return x0$5251=x0$5252;};
    
    //AttributeGetterDef x at operators.ceylon (238:4-238:27)
    function getX$5253(){
        return x0$5251;
    }
    ;$prop$getX$5253={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['operators','testIncDecOperators','$at','x']};}};
    $prop$getX$5253.get=function(){return x$5253};
    
    //AttributeSetterDef x at operators.ceylon (238:29-238:48)
    var setX$5253=function(x$5254){
        x0$5251=x$5254;
    };setX$5253.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['operators','testIncDecOperators','$at','x']};};
    $prop$getX$5253.set=setX$5253;
    if (setX$5253.$$metamodel$$===undefined)setX$5253.$$metamodel$$=$prop$getX$5253.$$metamodel$$;
    
    //AttributeDecl i1 at operators.ceylon (240:4-240:27)
    var i1$5255=(1);
    function setI1$5255(i1$5256){return i1$5255=i1$5256;};
    
    //MethodDef f1 at operators.ceylon (241:4-248:4)
    function f1$5257(){
        
        //AttributeDecl i2 at operators.ceylon (242:8-242:25)
        var i2$5258=(i1$5255=i1$5255.successor);
        
        //AttributeDecl x2 at operators.ceylon (243:8-243:24)
        var x2$5259=(setX$5253(getX$5253().successor),getX$5253());
        $$$c4139.check(i1$5255.equals((2)),$$$cl4138.String("prefix increment 1",18));
        $$$c4139.check(i2$5258.equals((2)),$$$cl4138.String("prefix increment 2",18));
        $$$c4139.check(getX$5253().equals((2)),$$$cl4138.String("prefix increment 3",18));
        $$$c4139.check(x2$5259.equals((2)),$$$cl4138.String("prefix increment 4",18));
    };f1$5257.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f1']};};
    f1$5257();
    
    //ClassDef C1 at operators.ceylon (251:4-255:4)
    function C1$5260($$c1$5260){
        $init$C1$5260();
        if ($$c1$5260===undefined)$$c1$5260=new C1$5260.$$;
        
        //AttributeDecl i at operators.ceylon (252:8-252:37)
        var i=(1);
        $$$cl4138.defineAttr($$c1$5260,'i',function(){return i;},function(i$5261){return i=i$5261;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5260,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','i']};});
        $$c1$5260.$prop$getI={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5260,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','i']};}};
        $$c1$5260.$prop$getI.get=function(){return i};
        
        //AttributeDecl x0 at operators.ceylon (253:8-253:31)
        var x0$5262=(1);
        $$$cl4138.defineAttr($$c1$5260,'x0$5262',function(){return x0$5262;},function(x0$5263){return x0$5262=x0$5263;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5260,$an:function(){return[$$$cl4138.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','x0']};});
        $$c1$5260.$prop$getX0$5262={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5260,$an:function(){return[$$$cl4138.variable()];},d:['operators','testIncDecOperators','$c','C1','$at','x0']};}};
        $$c1$5260.$prop$getX0$5262.get=function(){return x0$5262};
        
        //AttributeGetterDef x at operators.ceylon (254:8-254:38)
        $$$cl4138.defineAttr($$c1$5260,'x',function(){
            return x0$5262;
        },function(x$5264){
            x0$5262=x$5264;
        },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5260,$an:function(){return[$$$cl4138.shared()];},d:['operators','testIncDecOperators','$c','C1','$at','x']};});
        return $$c1$5260;
    }
    C1$5260.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['operators','testIncDecOperators','$c','C1']};};
    function $init$C1$5260(){
        if (C1$5260.$$===undefined){
            $$$cl4138.initTypeProto(C1$5260,'operators::testIncDecOperators.C1',$$$cl4138.Basic);
        }
        return C1$5260;
    }
    $init$C1$5260();
    
    //AttributeDecl c1 at operators.ceylon (256:4-256:16)
    var c1$5265=C1$5260();
    
    //AttributeDecl i3 at operators.ceylon (257:4-257:27)
    var i3$5266=(0);
    function setI3$5266(i3$5267){return i3$5266=i3$5267;};
    
    //MethodDef f2 at operators.ceylon (258:4-261:4)
    function f2$5268(){
        (i3$5266=i3$5266.successor);
        return c1$5265;
    };f2$5268.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:C1$5260},$ps:[],d:['operators','testIncDecOperators','$m','f2']};};
    
    //AttributeDecl i4 at operators.ceylon (262:4-262:25)
    var i4$5269=(tmp$5270=f2$5268(),tmp$5270.i=tmp$5270.i.successor);
    var tmp$5270;
    
    //AttributeDecl x4 at operators.ceylon (263:4-263:25)
    var x4$5271=(tmp$5272=f2$5268(),tmp$5272.x=tmp$5272.x.successor,tmp$5272.x);
    var tmp$5272;
    $$$c4139.check(i4$5269.equals((2)),$$$cl4138.String("prefix increment 5",18));
    $$$c4139.check(c1$5265.i.equals((2)),$$$cl4138.String("prefix increment 6",18));
    $$$c4139.check(x4$5271.equals((2)),$$$cl4138.String("prefix increment 7",18));
    $$$c4139.check(c1$5265.x.equals((2)),$$$cl4138.String("prefix increment 8",18));
    $$$c4139.check(i3$5266.equals((2)),$$$cl4138.String("prefix increment 9",18));
    
    //MethodDef f3 at operators.ceylon (270:4-274:4)
    function f3$5273(){
        
        //AttributeDecl i2 at operators.ceylon (271:8-271:25)
        var i2$5274=(i1$5255=i1$5255.predecessor);
        $$$c4139.check(i1$5255.equals((1)),$$$cl4138.String("prefix decrement",16));
        $$$c4139.check(i2$5274.equals((1)),$$$cl4138.String("prefix decrement",16));
    };f3$5273.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f3']};};
    f3$5273();
    
    //AttributeDecl i5 at operators.ceylon (277:4-277:25)
    var i5$5275=(tmp$5276=f2$5268(),tmp$5276.i=tmp$5276.i.predecessor);
    var tmp$5276;
    $$$c4139.check(i5$5275.equals((1)),$$$cl4138.String("prefix decrement",16));
    $$$c4139.check(c1$5265.i.equals((1)),$$$cl4138.String("prefix decrement",16));
    $$$c4139.check(i3$5266.equals((3)),$$$cl4138.String("prefix decrement",16));
    
    //MethodDef f4 at operators.ceylon (282:4-289:4)
    function f4$5277(){
        
        //AttributeDecl i2 at operators.ceylon (283:8-283:25)
        var i2$5278=(oldi1$5279=i1$5255,i1$5255=oldi1$5279.successor,oldi1$5279);
        var oldi1$5279;
        
        //AttributeDecl x2 at operators.ceylon (284:8-284:24)
        var x2$5280=(oldx$5281=getX$5253(),setX$5253(oldx$5281.successor),oldx$5281);
        var oldx$5281;
        $$$c4139.check(i1$5255.equals((2)),$$$cl4138.String("postfix increment 1",19));
        $$$c4139.check(i2$5278.equals((1)),$$$cl4138.String("postfix increment 2",19));
        $$$c4139.check(getX$5253().equals((3)),$$$cl4138.String("postfix increment 3",19));
        $$$c4139.check(x2$5280.equals((2)),$$$cl4138.String("postfix increment 4",19));
    };f4$5277.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f4']};};
    f4$5277();
    
    //AttributeDecl i6 at operators.ceylon (292:4-292:25)
    var i6$5282=(tmp$5283=f2$5268(),oldi$5284=tmp$5283.i,tmp$5283.i=oldi$5284.successor,oldi$5284);
    var tmp$5283,oldi$5284;
    
    //AttributeDecl x6 at operators.ceylon (293:4-293:25)
    var x6$5285=(tmp$5286=f2$5268(),oldx$5287=tmp$5286.x,tmp$5286.x=oldx$5287.successor,oldx$5287);
    var tmp$5286,oldx$5287;
    $$$c4139.check(i6$5282.equals((1)),$$$cl4138.String("postfix increment 5",19));
    $$$c4139.check(c1$5265.i.equals((2)),$$$cl4138.String("postfix increment 6",19));
    $$$c4139.check(x6$5285.equals((2)),$$$cl4138.String("postfix increment 7 ",20));
    $$$c4139.check(c1$5265.x.equals((3)),$$$cl4138.String("postfix increment 8 ",20));
    $$$c4139.check(i3$5266.equals((5)),$$$cl4138.String("postfix increment 9",19));
    
    //MethodDef f5 at operators.ceylon (300:4-304:4)
    function f5$5288(){
        
        //AttributeDecl i2 at operators.ceylon (301:8-301:25)
        var i2$5289=(oldi1$5290=i1$5255,i1$5255=oldi1$5290.predecessor,oldi1$5290);
        var oldi1$5290;
        $$$c4139.check(i1$5255.equals((1)),$$$cl4138.String("postfix decrement",17));
        $$$c4139.check(i2$5289.equals((2)),$$$cl4138.String("postfix decrement",17));
    };f5$5288.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testIncDecOperators','$m','f5']};};
    f5$5288();
    
    //AttributeDecl i7 at operators.ceylon (307:4-307:25)
    var i7$5291=(tmp$5292=f2$5268(),oldi$5293=tmp$5292.i,tmp$5292.i=oldi$5293.predecessor,oldi$5293);
    var tmp$5292,oldi$5293;
    $$$c4139.check(i7$5291.equals((2)),$$$cl4138.String("postfix decrement",17));
    $$$c4139.check(c1$5265.i.equals((1)),$$$cl4138.String("postfix decrement",17));
    $$$c4139.check(i3$5266.equals((6)),$$$cl4138.String("postfix decrement",17));
};testIncDecOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testIncDecOperators']};};

//MethodDef testArithmeticAssignOperators at operators.ceylon (313:0-364:0)
function testArithmeticAssignOperators(){
    
    //AttributeDecl i1 at operators.ceylon (314:4-314:27)
    var i1$5294=(1);
    function setI1$5294(i1$5295){return i1$5294=i1$5295;};
    
    //AttributeDecl x0 at operators.ceylon (315:4-315:27)
    var x0$5296=(1);
    function setX0$5296(x0$5297){return x0$5296=x0$5297;};
    
    //AttributeGetterDef x at operators.ceylon (316:4-316:27)
    function getX$5298(){
        return x0$5296;
    }
    ;$prop$getX$5298={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['operators','testArithmeticAssignOperators','$at','x']};}};
    $prop$getX$5298.get=function(){return x$5298};
    
    //AttributeSetterDef x at operators.ceylon (316:29-316:46)
    var setX$5298=function(x$5299){
        x0$5296=x$5299;
    };setX$5298.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['operators','testArithmeticAssignOperators','$at','x']};};
    $prop$getX$5298.set=setX$5298;
    if (setX$5298.$$metamodel$$===undefined)setX$5298.$$metamodel$$=$prop$getX$5298.$$metamodel$$;
    (i1$5294=i1$5294.plus((10)));
    (setX$5298(getX$5298().plus((10))),getX$5298());
    $$$c4139.check(i1$5294.equals((11)),$$$cl4138.String("+= operator 1",13));
    $$$c4139.check(getX$5298().equals((11)),$$$cl4138.String("+= operator 2",13));
    
    //AttributeDecl i2 at operators.ceylon (322:4-322:36)
    var i2$5300=(i1$5294=i1$5294.plus((-(5))));
    function setI2$5300(i2$5301){return i2$5300=i2$5301;};
    
    //AttributeDecl x2 at operators.ceylon (323:4-323:35)
    var x2$5302=(setX$5298(getX$5298().plus((-(5)))),getX$5298());
    function setX2$5302(x2$5303){return x2$5302=x2$5303;};
    $$$c4139.check(i2$5300.equals((6)),$$$cl4138.String("+= operator 3",13));
    $$$c4139.check(i1$5294.equals((6)),$$$cl4138.String("+= operator 4",13));
    $$$c4139.check(x2$5302.equals((6)),$$$cl4138.String("+= operator 5",13));
    $$$c4139.check(getX$5298().equals((6)),$$$cl4138.String("+= operator 6",13));
    
    //ClassDef C1 at operators.ceylon (329:4-333:4)
    function C1$5304($$c1$5304){
        $init$C1$5304();
        if ($$c1$5304===undefined)$$c1$5304=new C1$5304.$$;
        
        //AttributeDecl i at operators.ceylon (330:8-330:37)
        var i=(1);
        $$$cl4138.defineAttr($$c1$5304,'i',function(){return i;},function(i$5305){return i=i$5305;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5304,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','i']};});
        $$c1$5304.$prop$getI.get=function(){return i};
        
        //AttributeDecl x0 at operators.ceylon (331:8-331:31)
        var x0$5306=(1);
        $$$cl4138.defineAttr($$c1$5304,'x0$5306',function(){return x0$5306;},function(x0$5307){return x0$5306=x0$5307;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5304,$an:function(){return[$$$cl4138.variable()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','x0']};});
        $$c1$5304.$prop$getX0$5306={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5304,$an:function(){return[$$$cl4138.variable()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','x0']};}};
        $$c1$5304.$prop$getX0$5306.get=function(){return x0$5306};
        
        //AttributeGetterDef x at operators.ceylon (332:8-332:38)
        $$$cl4138.defineAttr($$c1$5304,'x',function(){
            return x0$5306;
        },function(x$5308){
            x0$5306=x$5308;
        },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C1$5304,$an:function(){return[$$$cl4138.shared()];},d:['operators','testArithmeticAssignOperators','$c','C1','$at','x']};});
        return $$c1$5304;
    }
    C1$5304.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['operators','testArithmeticAssignOperators','$c','C1']};};
    function $init$C1$5304(){
        if (C1$5304.$$===undefined){
            $$$cl4138.initTypeProto(C1$5304,'operators::testArithmeticAssignOperators.C1',$$$cl4138.Basic);
        }
        return C1$5304;
    }
    $init$C1$5304();
    
    //AttributeDecl c1 at operators.ceylon (334:4-334:16)
    var c1$5309=C1$5304();
    
    //AttributeDecl i3 at operators.ceylon (335:4-335:27)
    var i3$5310=(0);
    function setI3$5310(i3$5311){return i3$5310=i3$5311;};
    
    //MethodDef f at operators.ceylon (336:4-339:4)
    function f$5312(){
        (i3$5310=i3$5310.successor);
        return c1$5309;
    };f$5312.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:C1$5304},$ps:[],d:['operators','testArithmeticAssignOperators','$m','f']};};
    i2$5300=(tmp$5313=f$5312(),tmp$5313.i=tmp$5313.i.plus((11)));
    var tmp$5313;
    x2$5302=(tmp$5314=f$5312(),tmp$5314.x=tmp$5314.x.plus((11)),tmp$5314.x);
    var tmp$5314;
    $$$c4139.check(i2$5300.equals((12)),$$$cl4138.String("+= operator 7",13));
    $$$c4139.check(c1$5309.i.equals((12)),$$$cl4138.String("+= operator 8",13));
    $$$c4139.check(x2$5302.equals((12)),$$$cl4138.String("+= operator 9",13));
    $$$c4139.check(c1$5309.x.equals((12)),$$$cl4138.String("+= operator 10",14));
    $$$c4139.check(i3$5310.equals((2)),$$$cl4138.String("+= operator 11",14));
    i2$5300=(i1$5294=i1$5294.minus((14)));
    $$$c4139.check(i1$5294.equals((-(8))),$$$cl4138.String("-= operator",11));
    $$$c4139.check(i2$5300.equals((-(8))),$$$cl4138.String("-= operator",11));
    i2$5300=(i1$5294=i1$5294.times((-(3))));
    $$$c4139.check(i1$5294.equals((24)),$$$cl4138.String("*= operator",11));
    $$$c4139.check(i2$5300.equals((24)),$$$cl4138.String("*= operator",11));
    i2$5300=(i1$5294=i1$5294.divided((5)));
    $$$c4139.check(i1$5294.equals((4)),$$$cl4138.String("/= operator",11));
    $$$c4139.check(i2$5300.equals((4)),$$$cl4138.String("/= operator",11));
    i2$5300=(i1$5294=i1$5294.remainder((3)));
    $$$c4139.check(i1$5294.equals((1)),$$$cl4138.String("%= operator",11));
    $$$c4139.check(i2$5300.equals((1)),$$$cl4138.String("%= operator",11));
};testArithmeticAssignOperators.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testArithmeticAssignOperators']};};

//MethodDef testAssignmentOperator at operators.ceylon (366:0-396:0)
function testAssignmentOperator(){
    
    //AttributeDecl i1 at operators.ceylon (367:4-367:27)
    var i1$5315=(1);
    function setI1$5315(i1$5316){return i1$5315=i1$5316;};
    
    //AttributeDecl i2 at operators.ceylon (368:4-368:27)
    var i2$5317=(2);
    function setI2$5317(i2$5318){return i2$5317=i2$5318;};
    
    //AttributeDecl i3 at operators.ceylon (369:4-369:27)
    var i3$5319=(3);
    function setI3$5319(i3$5320){return i3$5319=i3$5320;};
    $$$c4139.check((i1$5315=(i2$5317=i3$5319)).equals((3)),$$$cl4138.String("assignment 1",12));
    $$$c4139.check(i1$5315.equals((3)),$$$cl4138.String("assignment 2",12));
    $$$c4139.check(i2$5317.equals((3)),$$$cl4138.String("assignment 3",12));
    
    //AttributeGetterDef x1 at operators.ceylon (374:4-374:28)
    function getX1$5321(){
        return i1$5315;
    }
    ;$prop$getX1$5321={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['operators','testAssignmentOperator','$at','x1']};}};
    $prop$getX1$5321.get=function(){return x1$5321};
    
    //AttributeSetterDef x1 at operators.ceylon (374:30-374:51)
    var setX1$5321=function(x1$5322){
        i1$5315=x1$5322;
    };setX1$5321.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['operators','testAssignmentOperator','$at','x1']};};
    $prop$getX1$5321.set=setX1$5321;
    if (setX1$5321.$$metamodel$$===undefined)setX1$5321.$$metamodel$$=$prop$getX1$5321.$$metamodel$$;
    
    //AttributeGetterDef x2 at operators.ceylon (375:4-375:28)
    function getX2$5323(){
        return i2$5317;
    }
    ;$prop$getX2$5323={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['operators','testAssignmentOperator','$at','x2']};}};
    $prop$getX2$5323.get=function(){return x2$5323};
    
    //AttributeSetterDef x2 at operators.ceylon (375:30-375:51)
    var setX2$5323=function(x2$5324){
        i2$5317=x2$5324;
    };setX2$5323.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['operators','testAssignmentOperator','$at','x2']};};
    $prop$getX2$5323.set=setX2$5323;
    if (setX2$5323.$$metamodel$$===undefined)setX2$5323.$$metamodel$$=$prop$getX2$5323.$$metamodel$$;
    
    //AttributeGetterDef x3 at operators.ceylon (376:4-376:28)
    function getX3$5325(){
        return i3$5319;
    }
    ;$prop$getX3$5325={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},d:['operators','testAssignmentOperator','$at','x3']};}};
    $prop$getX3$5325.get=function(){return x3$5325};
    
    //AttributeSetterDef x3 at operators.ceylon (376:30-376:51)
    var setX3$5325=function(x3$5326){
        i3$5319=x3$5326;
    };setX3$5325.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},d:['operators','testAssignmentOperator','$at','x3']};};
    $prop$getX3$5325.set=setX3$5325;
    if (setX3$5325.$$metamodel$$===undefined)setX3$5325.$$metamodel$$=$prop$getX3$5325.$$metamodel$$;
    i1$5315=(1);
    i2$5317=(2);
    $$$c4139.check((setX1$5321((setX2$5323(getX3$5325()),getX2$5323())),getX1$5321()).equals((3)),$$$cl4138.String("assignment 4",12));
    $$$c4139.check(getX1$5321().equals((3)),$$$cl4138.String("assignment 5",12));
    $$$c4139.check(getX2$5323().equals((3)),$$$cl4138.String("assignment 6",12));
    
    //ClassDef C at operators.ceylon (383:4-387:4)
    function C$5327($$c$5327){
        $init$C$5327();
        if ($$c$5327===undefined)$$c$5327=new C$5327.$$;
        
        //AttributeDecl i at operators.ceylon (384:8-384:37)
        var i=(1);
        $$$cl4138.defineAttr($$c$5327,'i',function(){return i;},function(i$5328){return i=i$5328;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C$5327,$an:function(){return[$$$cl4138.shared(),$$$cl4138.variable()];},d:['operators','testAssignmentOperator','$c','C','$at','i']};});
        $$c$5327.$prop$getI.get=function(){return i};
        
        //AttributeDecl x0 at operators.ceylon (385:8-385:31)
        var x0$5329=(1);
        $$$cl4138.defineAttr($$c$5327,'x0$5329',function(){return x0$5329;},function(x0$5330){return x0$5329=x0$5330;},function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C$5327,$an:function(){return[$$$cl4138.variable()];},d:['operators','testAssignmentOperator','$c','C','$at','x0']};});
        $$c$5327.$prop$getX0$5329={$$metamodel$$:function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C$5327,$an:function(){return[$$$cl4138.variable()];},d:['operators','testAssignmentOperator','$c','C','$at','x0']};}};
        $$c$5327.$prop$getX0$5329.get=function(){return x0$5329};
        
        //AttributeGetterDef x at operators.ceylon (386:8-386:38)
        $$$cl4138.defineAttr($$c$5327,'x',function(){
            return x0$5329;
        },function(x$5331){
            x0$5329=x$5331;
        },function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Integer},$cont:C$5327,$an:function(){return[$$$cl4138.shared()];},d:['operators','testAssignmentOperator','$c','C','$at','x']};});
        return $$c$5327;
    }
    C$5327.$$metamodel$$=function(){return{mod:$$METAMODEL$$,'super':{t:$$$cl4138.Basic},$ps:[],d:['operators','testAssignmentOperator','$c','C']};};
    function $init$C$5327(){
        if (C$5327.$$===undefined){
            $$$cl4138.initTypeProto(C$5327,'operators::testAssignmentOperator.C',$$$cl4138.Basic);
        }
        return C$5327;
    }
    $init$C$5327();
    
    //AttributeDecl o1 at operators.ceylon (388:4-388:14)
    var o1$5332=C$5327();
    
    //AttributeDecl o2 at operators.ceylon (389:4-389:14)
    var o2$5333=C$5327();
    $$$c4139.check((o1$5332.i=(o2$5333.i=(3))).equals((3)),$$$cl4138.String("assignment 7",12));
    $$$c4139.check(o1$5332.i.equals((3)),$$$cl4138.String("assignment 8",12));
    $$$c4139.check(o2$5333.i.equals((3)),$$$cl4138.String("assignment 9",12));
    $$$c4139.check((tmp$5334=o1$5332,tmp$5334.x=(tmp$5335=o2$5333,tmp$5335.x=(3),tmp$5335.x),tmp$5334.x).equals((3)),$$$cl4138.String("assignment 10",13));
    var tmp$5334,tmp$5335;
    $$$c4139.check(o1$5332.x.equals((3)),$$$cl4138.String("assignment 11",13));
    $$$c4139.check(o2$5333.x.equals((3)),$$$cl4138.String("assignment 12",13));
};testAssignmentOperator.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testAssignmentOperator']};};

//MethodDef testSegments at operators.ceylon (398:0-426:0)
function testSegments(){
    
    //AttributeDecl seq at operators.ceylon (399:4-399:97)
    var seq$5336=$$$cl4138.Tuple($$$cl4138.String("one",3),$$$cl4138.Tuple($$$cl4138.String("two",3),$$$cl4138.Tuple($$$cl4138.String("three",5),$$$cl4138.Tuple($$$cl4138.String("four",4),$$$cl4138.Tuple($$$cl4138.String("five",4),$$$cl4138.Tuple($$$cl4138.String("six",3),$$$cl4138.Tuple($$$cl4138.String("seven",5),$$$cl4138.Tuple($$$cl4138.String("eight",5),$$$cl4138.Tuple($$$cl4138.String("nine",4),$$$cl4138.Tuple($$$cl4138.String("ten",3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}});
    $$$c4139.check(seq$5336.segment((1),(2)).equals($$$cl4138.Tuple($$$cl4138.String("two",3),$$$cl4138.Tuple($$$cl4138.String("three",5),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}})),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("seq[1:2] ",9),seq$5336.segment((1),(2)).string]).string);
    $$$c4139.check(seq$5336.segment((3),(5)).equals($$$cl4138.Tuple($$$cl4138.String("four",4),$$$cl4138.Tuple($$$cl4138.String("five",4),$$$cl4138.Tuple($$$cl4138.String("six",3),$$$cl4138.Tuple($$$cl4138.String("seven",5),$$$cl4138.Tuple($$$cl4138.String("eight",5),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}})),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("seq[3:5] ",9),seq$5336.segment((3),(5)).string]).string);
    $$$c4139.check($$$cl4138.String("test",4).segment((1),(2)).equals($$$cl4138.String("es",2)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("test[1:2] ",10),$$$cl4138.String("test",4).segment((1),(2)).string]).string);
    $$$c4139.check($$$cl4138.String("hello",5).segment((2),(2)).equals($$$cl4138.String("ll",2)),$$$cl4138.StringBuilder().appendAll([$$$cl4138.String("hello[2:2] ",11),$$$cl4138.String("hello",5).segment((2),(2)).string]).string);
    $$$c4139.check((function(){
        //SpreadOp at 404:10-404:24
        var lst$5337=[];
        var it$5338=seq$5336.iterator();
        var elem$5339;
        while ((elem$5339=it$5338.next())!==$$$cl4138.getFinished()){
            lst$5337.push(elem$5339.uppercased);
        }
        return $$$cl4138.ArraySequence(lst$5337);
    }()).equals($$$cl4138.Tuple($$$cl4138.String("ONE",3),$$$cl4138.Tuple($$$cl4138.String("TWO",3),$$$cl4138.Tuple($$$cl4138.String("THREE",5),$$$cl4138.Tuple($$$cl4138.String("FOUR",4),$$$cl4138.Tuple($$$cl4138.String("FIVE",4),$$$cl4138.Tuple($$$cl4138.String("SIX",3),$$$cl4138.Tuple($$$cl4138.String("SEVEN",5),$$$cl4138.Tuple($$$cl4138.String("EIGHT",5),$$$cl4138.Tuple($$$cl4138.String("NINE",4),$$$cl4138.Tuple($$$cl4138.String("TEN",3),$$$cl4138.getEmpty(),{Rest:{t:$$$cl4138.Empty},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}}),{Rest:{t:'T', l:[{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String},{t:$$$cl4138.String}]},First:{t:$$$cl4138.String},Element:{t:$$$cl4138.String}})),$$$cl4138.String("spread op",9));
    
    //AttributeDecl s2 at operators.ceylon (405:4-405:18)
    var s2$5340=(function(){var tmpvar$5341=(3);
    if (tmpvar$5341>0){
    var tmpvar$5342=(0);
    var tmpvar$5343=tmpvar$5342;
    for (var i=1; i<tmpvar$5341; i++){tmpvar$5343=tmpvar$5343.successor;}
    return $$$cl4138.Range(tmpvar$5342,tmpvar$5343,{Element:{t:$$$cl4138.Integer}})
    }else return $$$cl4138.getEmpty();}());
    
    //AttributeDecl s3 at operators.ceylon (406:4-406:18)
    var s3$5344=(function(){var tmpvar$5345=(5);
    if (tmpvar$5345>0){
    var tmpvar$5346=(2);
    var tmpvar$5347=tmpvar$5346;
    for (var i=1; i<tmpvar$5345; i++){tmpvar$5347=tmpvar$5347.successor;}
    return $$$cl4138.Range(tmpvar$5346,tmpvar$5347,{Element:{t:$$$cl4138.Integer}})
    }else return $$$cl4138.getEmpty();}());
    $$$c4139.check(s2$5340.size.equals((3)),$$$cl4138.String("0:3 [1]",7));
    var x$5348;
    if((x$5348=s2$5340.$get((0)))!==null){
        $$$c4139.check(x$5348.equals((0)),$$$cl4138.String("0:3 [2]",7));
    }else {
        $$$c4139.fail($$$cl4138.String("0:3 [2]",7));
    }
    var x$5349;
    if((x$5349=s2$5340.$get((2)))!==null){
        $$$c4139.check(x$5349.equals((2)),$$$cl4138.String("0:3 [3]",7));
    }else {
        $$$c4139.fail($$$cl4138.String("0:3 [3]",7));
    }
    $$$c4139.check(s3$5344.size.equals((5)),$$$cl4138.String("2:5 [1]",7));
    var x$5350;
    if((x$5350=s3$5344.$get((0)))!==null){
        $$$c4139.check(x$5350.equals((2)),$$$cl4138.String("2:5 [1]",7));
    }else {
        $$$c4139.fail($$$cl4138.String("2:5 [1]",7));
    }
    var x$5351;
    if((x$5351=s3$5344.$get((2)))!==null){
        $$$c4139.check(x$5351.equals((4)),$$$cl4138.String("2:5 [2]",7));
    }else {
        $$$c4139.fail($$$cl4138.String("2:5 [2]",7));
    }
    var x$5352;
    if((x$5352=s3$5344.$get((4)))!==null){
        $$$c4139.check(x$5352.equals((6)),$$$cl4138.String("2:5 [3]",7));
    }else {
        $$$c4139.fail($$$cl4138.String("2:5 [3]",7));
    }
    $$$c4139.check((!$$$cl4138.nonempty((function(){var tmpvar$5353=(0);
    if (tmpvar$5353>0){
    var tmpvar$5354=(1);
    var tmpvar$5355=tmpvar$5354;
    for (var i=1; i<tmpvar$5353; i++){tmpvar$5355=tmpvar$5355.successor;}
    return $$$cl4138.Range(tmpvar$5354,tmpvar$5355,{Element:{t:$$$cl4138.Integer}})
    }else return $$$cl4138.getEmpty();}()))),$$$cl4138.String("1:0 empty",9));
    $$$c4139.check((!$$$cl4138.nonempty((function(){var tmpvar$5356=(-(1));
    if (tmpvar$5356>0){
    var tmpvar$5357=(1);
    var tmpvar$5358=tmpvar$5357;
    for (var i=1; i<tmpvar$5356; i++){tmpvar$5358=tmpvar$5358.successor;}
    return $$$cl4138.Range(tmpvar$5357,tmpvar$5358,{Element:{t:$$$cl4138.Integer}})
    }else return $$$cl4138.getEmpty();}()))),$$$cl4138.String("1:-1 empty",10));
};testSegments.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','testSegments']};};

//MethodDef compareStringNumber at operators.ceylon (428:0-438:0)
function compareStringNumber(){
    
    //AttributeDecl n1 at operators.ceylon (430:4-430:17)
    var n1$5359=(1);
    
    //AttributeDecl s1 at operators.ceylon (431:4-431:19)
    var s1$5360=$$$cl4138.String("1",1);
    
    //AttributeDecl n2 at operators.ceylon (432:4-432:19)
    var n2$5361=$$$cl4138.Float(1.0);
    
    //AttributeDecl s2 at operators.ceylon (433:4-433:21)
    var s2$5362=$$$cl4138.String("1.0",3);
    $$$c4139.check((!n1$5359.equals(s1$5360)),$$$cl4138.String("Integer and String should NOT be equal!",39));
    $$$c4139.check((!s1$5360.equals(n1$5359)),$$$cl4138.String("String and Integer should NOT be equal!",39));
    $$$c4139.check((!n2$5361.equals(s2$5362)),$$$cl4138.String("Float and String sould NOT be equal",35));
    $$$c4139.check((!s2$5362.equals(n2$5361)),$$$cl4138.String("String and Float should NOT be equal",36));
};compareStringNumber.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],d:['operators','compareStringNumber']};};

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
    $$$c4139.results();
}
exports.test=test;
test.$$metamodel$$=function(){return{mod:$$METAMODEL$$,$t:{t:$$$cl4138.Anything},$ps:[],$an:function(){return[$$$cl4138.shared()];},d:['operators','test']};};
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
