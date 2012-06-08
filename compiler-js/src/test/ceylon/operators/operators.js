(function(define) { define(function(require, exports, module) {
var $$$cl1=require('ceylon/language/0.3/ceylon.language');
var $$$a12=require('default/assert');

//MethodDefinition testIntegerOperators at operators.ceylon (3:0-40:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (5:4-5:29)
    var i1$386=$$$cl1.Integer(4).getNegativeValue();
    var setI1$386=function(i1$387){i1$386=i1$387; return i1$386;};
    setI1$386(i1$386.getNegativeValue());
    $$$a12.assert(i1$386.equals($$$cl1.Integer(4)),$$$cl1.String("negation",8));
    setI1$386($$$cl1.Integer(987654).getNegativeValue().getPositiveValue());
    $$$a12.assert(i1$386.equals($$$cl1.Integer(987654).getNegativeValue()),$$$cl1.String("positive",8));
    setI1$386($$$cl1.Integer(0).getPositiveValue());
    $$$a12.assert(i1$386.equals($$$cl1.Integer(0)),$$$cl1.String("+0=0",4));
    setI1$386($$$cl1.Integer(0).getNegativeValue());
    $$$a12.assert(i1$386.equals($$$cl1.Integer(0)),$$$cl1.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (15:4-15:36)
    var i2$388=$$$cl1.Integer(123).plus($$$cl1.Integer(456));
    var setI2$388=function(i2$389){i2$388=i2$389; return i2$388;};
    $$$a12.assert(i2$388.equals($$$cl1.Integer(579)),$$$cl1.String("addition",8));
    setI1$386(i2$388.minus($$$cl1.Integer(16)));
    $$$a12.assert(i1$386.equals($$$cl1.Integer(563)),$$$cl1.String("subtraction",11));
    setI2$388(i1$386.getNegativeValue().plus(i2$388).minus($$$cl1.Integer(1)));
    $$$a12.assert(i2$388.equals($$$cl1.Integer(15)),$$$cl1.String("-i1+i2-1",8));
    setI1$386($$$cl1.Integer(3).times($$$cl1.Integer(7)));
    $$$a12.assert(i1$386.equals($$$cl1.Integer(21)),$$$cl1.String("multiplication",14));
    setI2$388(i1$386.times($$$cl1.Integer(2)));
    $$$a12.assert(i2$388.equals($$$cl1.Integer(42)),$$$cl1.String("multiplication",14));
    setI2$388($$$cl1.Integer(17).divided($$$cl1.Integer(4)));
    $$$a12.assert(i2$388.equals($$$cl1.Integer(4)),$$$cl1.String("integer division",16));
    setI1$386(i2$388.times($$$cl1.Integer(516)).divided(i1$386.getNegativeValue()));
    $$$a12.assert(i1$386.equals($$$cl1.Integer(98).getNegativeValue()),$$$cl1.String("i2*516/-i1",10));
    setI1$386($$$cl1.Integer(15).remainder($$$cl1.Integer(4)));
    $$$a12.assert(i1$386.equals($$$cl1.Integer(3)),$$$cl1.String("modulo",6));
    setI2$388($$$cl1.Integer(312).remainder($$$cl1.Integer(12)));
    $$$a12.assert(i2$388.equals($$$cl1.Integer(0)),$$$cl1.String("modulo",6));
    setI1$386($$$cl1.Integer(2).power($$$cl1.Integer(10)));
    $$$a12.assert(i1$386.equals($$$cl1.Integer(1024)),$$$cl1.String("power",5));
    setI2$388($$$cl1.Integer(100).power($$$cl1.Integer(6)));
    $$$a12.assert(i2$388.equals($$$cl1.Integer(1000000000000)),$$$cl1.String("power",5));
}

//MethodDefinition testFloatOperators at operators.ceylon (42:0-72:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (44:4-44:29)
    var f1$390=$$$cl1.Float(4.2).getNegativeValue();
    var setF1$390=function(f1$391){f1$390=f1$391; return f1$390;};
    setF1$390(f1$390.getNegativeValue());
    $$$a12.assert(f1$390.equals($$$cl1.Float(4.2)),$$$cl1.String("negation",8));
    setF1$390($$$cl1.Float(987654.9925567).getNegativeValue().getPositiveValue());
    $$$a12.assert(f1$390.equals($$$cl1.Float(987654.9925567).getNegativeValue()),$$$cl1.String("positive",8));
    setF1$390($$$cl1.Float(0.0).getPositiveValue());
    $$$a12.assert(f1$390.equals($$$cl1.Float(0.0)),$$$cl1.String("+0.0=0.0",8));
    setF1$390($$$cl1.Float(0.0).getNegativeValue());
    $$$a12.assert(f1$390.equals($$$cl1.Float(0.0)),$$$cl1.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (54:4-54:43)
    var f2$392=$$$cl1.Float(3.14159265).plus($$$cl1.Float(456.0));
    var setF2$392=function(f2$393){f2$392=f2$393; return f2$392;};
    $$$a12.assert(f2$392.equals($$$cl1.Float(459.14159265)),$$$cl1.String("addition",8));
    setF1$390(f2$392.minus($$$cl1.Float(0.0016)));
    $$$a12.assert(f1$390.equals($$$cl1.Float(459.13999265)),$$$cl1.String("subtraction",11));
    setF2$392(f1$390.getNegativeValue().plus(f2$392).minus($$$cl1.Float(1.2)));
    $$$a12.assert(f2$392.equals($$$cl1.Float(1.1984000000000037).getNegativeValue()),$$$cl1.String("-f1+f2-1.2",10));
    setF1$390($$$cl1.Float(3.0).times($$$cl1.Float(0.79)));
    $$$a12.assert(f1$390.equals($$$cl1.Float(2.37)),$$$cl1.String("multiplication",14));
    setF2$392(f1$390.times($$$cl1.Float(2.0e13)));
    $$$a12.assert(f2$392.equals($$$cl1.Float(47400000000000.0)),$$$cl1.String("multiplication",14));
    setF2$392($$$cl1.Float(17.1).divided($$$cl1.Float(4.0E-18)));
    $$$a12.assert(f2$392.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("division",8));
    setF1$390(f2$392.times($$$cl1.Float(51.6e2)).divided(f1$390.getNegativeValue()));
    $$$a12.assert(f2$392.equals($$$cl1.Float(4275000000000000000.0)),$$$cl1.String("f2*51.6e2/-f1",13));
    setF1$390($$$cl1.Float(150.0).power($$$cl1.Float(0.5)));
    $$$a12.assert(f1$390.equals($$$cl1.Float(12.24744871391589)),$$$cl1.String("power",5));
}

//ClassDefinition OpTest1 at operators.ceylon (74:0-74:17)
function OpTest1($$opTest1){
    $init$OpTest1();
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
function $init$OpTest1(){
    if (OpTest1.$$===undefined){
        $$$cl1.initTypeProto(OpTest1,'operators.OpTest1',$$$cl1.IdentifiableObject);
    }
    return OpTest1;
}
exports.$init$OpTest1=$init$OpTest1;
$init$OpTest1();

//MethodDefinition testBooleanOperators at operators.ceylon (76:0-107:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (77:4-77:24)
    var o1$394=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (78:4-78:24)
    var o2$395=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (79:4-79:36)
    var b1$396=(o1$394===o2$395?$$$cl1.getTrue():$$$cl1.getFalse());
    var setB1$396=function(b1$397){b1$396=b1$397; return b1$396;};
    $$$a12.assert(b1$396.equals($$$cl1.getFalse()),$$$cl1.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (81:4-81:36)
    var b2$398=(o1$394===o1$394?$$$cl1.getTrue():$$$cl1.getFalse());
    var setB2$398=function(b2$399){b2$398=b2$399; return b2$398;};
    $$$a12.assert(b2$398,$$$cl1.String("identity",8));
    setB1$396(o1$394.equals(o2$395));
    $$$a12.assert(b1$396.equals($$$cl1.getFalse()),$$$cl1.String("equals",6));
    setB2$398(o1$394.equals(o1$394));
    $$$a12.assert(b2$398,$$$cl1.String("equals",6));
    setB1$396($$$cl1.Integer(1).equals($$$cl1.Integer(2)));
    $$$a12.assert(b1$396.equals($$$cl1.getFalse()),$$$cl1.String("equals",6));
    setB2$398($$$cl1.Integer(1).equals($$$cl1.Integer(2)).equals($$$cl1.getFalse()));
    $$$a12.assert(b2$398,$$$cl1.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (92:4-92:30)
    var b3$400=b2$398.equals($$$cl1.getFalse());
    var setB3$400=function(b3$401){b3$400=b3$401; return b3$400;};
    $$$a12.assert(b3$400.equals($$$cl1.getFalse()),$$$cl1.String("not",3));
    setB1$396(($$$cl1.getTrue()===$$$cl1.getTrue()?$$$cl1.getFalse():$$$cl1.getFalse()));
    $$$a12.assert(b1$396.equals($$$cl1.getFalse()),$$$cl1.String("and",3));
    setB2$398((b1$396===$$$cl1.getTrue()?$$$cl1.getTrue():$$$cl1.getFalse()));
    $$$a12.assert(b2$398.equals($$$cl1.getFalse()),$$$cl1.String("and",3));
    setB3$400(($$$cl1.getTrue()===$$$cl1.getTrue()?$$$cl1.getTrue():$$$cl1.getFalse()));
    $$$a12.assert(b3$400,$$$cl1.String("and",3));
    setB1$396(($$$cl1.getTrue()===$$$cl1.getTrue()?$$$cl1.getTrue():$$$cl1.getFalse()));
    $$$a12.assert(b1$396,$$$cl1.String("or",2));
    setB2$398(($$$cl1.getFalse()===$$$cl1.getTrue()?$$$cl1.getTrue():b1$396));
    $$$a12.assert(b2$398,$$$cl1.String("or",2));
    setB3$400(($$$cl1.getFalse()===$$$cl1.getTrue()?$$$cl1.getTrue():$$$cl1.getFalse()));
    $$$a12.assert(b3$400.equals($$$cl1.getFalse()),$$$cl1.String("or",2));
}

//MethodDefinition testComparisonOperators at operators.ceylon (109:0-139:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (110:4-110:37)
    var c1$402=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4));
    $$$a12.assert(c1$402.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (112:4-112:37)
    var c2$403=$$$cl1.String("str2",4).compare($$$cl1.String("str1",4));
    $$$a12.assert(c2$403.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (114:4-114:37)
    var c3$404=$$$cl1.String("str1",4).compare($$$cl1.String("str1",4));
    $$$a12.assert(c3$404.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (116:4-116:29)
    var c4$405=$$$cl1.String("",0).compare($$$cl1.String("",0));
    $$$a12.assert(c4$405.equals($$$cl1.getEqual()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (118:4-118:33)
    var c5$406=$$$cl1.String("str1",4).compare($$$cl1.String("",0));
    $$$a12.assert(c5$406.equals($$$cl1.getLarger()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (120:4-120:33)
    var c6$407=$$$cl1.String("",0).compare($$$cl1.String("str2",4));
    $$$a12.assert(c6$407.equals($$$cl1.getSmaller()),$$$cl1.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (123:4-123:42)
    var b1$408=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getSmaller());
    var setB1$408=function(b1$409){b1$408=b1$409; return b1$408;};
    $$$a12.assert(b1$408,$$$cl1.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (125:4-125:42)
    var b2$410=$$$cl1.String("str1",4).compare($$$cl1.String("str2",4)).equals($$$cl1.getLarger());
    var setB2$410=function(b2$411){b2$410=b2$411; return b2$410;};
    $$$a12.assert(b2$410.equals($$$cl1.getFalse()),$$$cl1.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (127:4-127:43)
    var b3$412=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getLarger()?$$$cl1.getTrue():$$$cl1.getFalse());
    var setB3$412=function(b3$413){b3$412=b3$413; return b3$412;};
    $$$a12.assert(b3$412,$$$cl1.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (129:4-129:43)
    var b4$414=($$$cl1.String("str1",4).compare($$$cl1.String("str2",4))!==$$$cl1.getSmaller()?$$$cl1.getTrue():$$$cl1.getFalse());
    var setB4$414=function(b4$415){b4$414=b4$415; return b4$414;};
    $$$a12.assert(b4$414.equals($$$cl1.getFalse()),$$$cl1.String("large as",8));
    setB1$408($$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getSmaller()));
    $$$a12.assert(b1$408.equals($$$cl1.getFalse()),$$$cl1.String("smaller",7));
    setB2$410($$$cl1.String("str1",4).compare($$$cl1.String("str1",4)).equals($$$cl1.getLarger()));
    $$$a12.assert(b2$410.equals($$$cl1.getFalse()),$$$cl1.String("larger",6));
    setB3$412(($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getLarger()?$$$cl1.getTrue():$$$cl1.getFalse()));
    $$$a12.assert(b3$412,$$$cl1.String("small as",8));
    setB4$414(($$$cl1.String("str1",4).compare($$$cl1.String("str1",4))!==$$$cl1.getSmaller()?$$$cl1.getTrue():$$$cl1.getFalse()));
    $$$a12.assert(b4$414,$$$cl1.String("large as",8));
}

//MethodDefinition testOtherOperators at operators.ceylon (141:0-153:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (142:4-142:42)
    var entry$416=$$$cl1.Entry($$$cl1.Integer(47),$$$cl1.String("hi there",8));
    $$$a12.assert(entry$416.getKey().equals($$$cl1.Integer(47)),$$$cl1.String("entry key",9));
    $$$a12.assert(entry$416.getItem().equals($$$cl1.String("hi there",8)),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (145:4-145:30)
    var entry2$417=$$$cl1.Entry($$$cl1.getTrue(),entry$416);
    $$$a12.assert(entry2$417.getKey().equals($$$cl1.getTrue()),$$$cl1.String("entry key",9));
    $$$a12.assert(entry2$417.getItem().equals($$$cl1.Entry($$$cl1.Integer(47),$$$cl1.String("hi there",8))),$$$cl1.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (149:4-149:41)
    var s1$418=function($){return $!==null?$:$$$cl1.String("noo",3)}(($$$cl1.getTrue()===$$$cl1.getTrue()?$$$cl1.String("ok",2):null));
    $$$a12.assert(s1$418.equals($$$cl1.String("ok",2)),$$$cl1.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (151:4-151:47)
    var s2$419=function($){return $!==null?$:$$$cl1.String("great",5)}(($$$cl1.getFalse()===$$$cl1.getTrue()?$$$cl1.String("what?",5):null));
    $$$a12.assert(s2$419.equals($$$cl1.String("great",5)),$$$cl1.String("then/else 2",11));
}

//MethodDefinition testCollectionOperators at operators.ceylon (155:0-167:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (156:4-156:33)
    var seq1$420=$$$cl1.ArraySequence([$$$cl1.String("one",3),$$$cl1.String("two",3)]);
    
    //AttributeDeclaration s1 at operators.ceylon (157:4-157:30)
    var s1$421=function($){return $!==null?$:$$$cl1.String("null",4)}(seq1$420.item($$$cl1.Integer(0)));
    $$$a12.assert(s1$421.equals($$$cl1.String("one",3)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (159:4-159:30)
    var s2$422=function($){return $!==null?$:$$$cl1.String("null",4)}(seq1$420.item($$$cl1.Integer(2)));
    $$$a12.assert(s2$422.equals($$$cl1.String("null",4)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (161:4-161:31)
    var s3$423=function($){return $!==null?$:$$$cl1.String("null",4)}(seq1$420.item($$$cl1.Integer(1).getNegativeValue()));
    $$$a12.assert(s3$423.equals($$$cl1.String("null",4)),$$$cl1.String("lookup",6));
    
    //AttributeDeclaration unsafe at operators.ceylon (163:4-163:45)
    var unsafe$424=seq1$420;
    var setUnsafe$424=function(unsafe$425){unsafe$424=unsafe$425; return unsafe$424;};
    $$$a12.assert($$$cl1.exists($$$cl1.exists(unsafe$424)===$$$cl1.getTrue()?unsafe$424.item($$$cl1.Integer(0)):$$$cl1.getNull()),$$$cl1.String("safe index",10));
    setUnsafe$424($$$cl1.getNull());
    $$$a12.assert($$$cl1.exists($$$cl1.exists(unsafe$424)===$$$cl1.getTrue()?unsafe$424.item($$$cl1.Integer(0)):$$$cl1.getNull()).equals($$$cl1.getFalse()),$$$cl1.String("safe index",10));
}

//ClassDefinition NullsafeTest at operators.ceylon (169:0-174:0)
function NullsafeTest($$nullsafeTest){
    $init$NullsafeTest();
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    
    //MethodDefinition f at operators.ceylon (170:4-170:33)
    function f(){
        return $$$cl1.Integer(1);
    }
    $$nullsafeTest.f=f;
    
    //MethodDefinition f2 at operators.ceylon (171:4-173:4)
    function f2(x$426){
        return x$426();
    }
    $$nullsafeTest.f2=f2;
    return $$nullsafeTest;
}
function $init$NullsafeTest(){
    if (NullsafeTest.$$===undefined){
        $$$cl1.initTypeProto(NullsafeTest,'operators.NullsafeTest',$$$cl1.IdentifiableObject);
    }
    return NullsafeTest;
}
exports.$init$NullsafeTest=$init$NullsafeTest;
$init$NullsafeTest();

//MethodDefinition nullsafeTest at operators.ceylon (176:0-178:0)
function nullsafeTest(f$427){
    return f$427();
}

//MethodDefinition testNullsafeOperators at operators.ceylon (180:0-221:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (181:4-181:27)
    var seq$428=$$$cl1.ArraySequence([$$$cl1.String("hi",2)]);
    
    //AttributeDeclaration s1 at operators.ceylon (182:4-182:29)
    var s1$429=function($){return $!==null?$:$$$cl1.String("null",4)}(seq$428.item($$$cl1.Integer(0)));
    $$$a12.assert(s1$429.equals($$$cl1.String("hi",2)),$$$cl1.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (184:4-184:29)
    var s2$430=function($){return $!==null?$:$$$cl1.String("null",4)}(seq$428.item($$$cl1.Integer(1)));
    $$$a12.assert(s2$430.equals($$$cl1.String("null",4)),$$$cl1.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (187:4-187:21)
    var s3$431=$$$cl1.getNull();
    
    //AttributeDeclaration s4 at operators.ceylon (188:4-188:23)
    var s4$432=$$$cl1.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (189:4-189:39)
    var s5$433=function($){return $!==null?$:$$$cl1.String("null",4)}((function($){return $===null?null:$.getUppercased()}(s3$431)));
    
    //AttributeDeclaration s6 at operators.ceylon (190:4-190:39)
    var s6$434=function($){return $!==null?$:$$$cl1.String("null",4)}((function($){return $===null?null:$.getUppercased()}(s4$432)));
    $$$a12.assert(s5$433.equals($$$cl1.String("null",4)),$$$cl1.String("nullsafe member 1",17));
    $$$a12.assert(s6$434.equals($$$cl1.String("TEST",4)),$$$cl1.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (193:4-193:28)
    var obj$435=$$$cl1.getNull();
    
    //AttributeDeclaration i at operators.ceylon (194:4-194:25)
    var i$436=(function(){var tmpvar$437=obj$435; return $$$cl1.JsCallable(tmpvar$437,tmpvar$437===null?null:tmpvar$437.f);}())();
    $$$a12.assert($$$cl1.exists(i$436).equals($$$cl1.getFalse()),$$$cl1.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (196:4-196:34)
    var f2$438=(function(){var tmpvar$439=obj$435; return $$$cl1.JsCallable(tmpvar$439,tmpvar$439===null?null:tmpvar$439.f);}());
    $$$a12.assert($$$cl1.exists(nullsafeTest(f2$438)).equals($$$cl1.getFalse()),$$$cl1.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (198:4-198:35)
    var f3$440=(function(){var tmpvar$441=obj$435; return $$$cl1.JsCallable(tmpvar$441,tmpvar$441===null?null:tmpvar$441.f);}());
    $$$a12.assert($$$cl1.exists(f3$440),$$$cl1.String("nullsafe method ref 2",21));
    (function(){var tmpvar$442=obj$435; return $$$cl1.JsCallable(tmpvar$442,tmpvar$442===null?null:tmpvar$442.f);}())();
    $$$a12.assert($$$cl1.exists((function(){var tmpvar$443=obj$435; return $$$cl1.JsCallable(tmpvar$443,tmpvar$443===null?null:tmpvar$443.f);}())()).equals($$$cl1.getFalse()),$$$cl1.String("nullsafe simple call",20));
    
    //MethodDefinition getNullsafe at operators.ceylon (202:4-202:46)
    function getNullsafe$444(){
        return obj$435;
    }
    
    //MethodDeclaration f4 at operators.ceylon (203:4-203:36)
    var f4$445=(function(){var tmpvar$446=getNullsafe$444(); return $$$cl1.JsCallable(tmpvar$446,tmpvar$446===null?null:tmpvar$446.f);}());
    
    //AttributeDeclaration result_f4 at operators.ceylon (204:4-204:29)
    var result_f4$447=f4$445();
    $$$a12.assert($$$cl1.exists(result_f4$447).equals($$$cl1.getFalse()),$$$cl1.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (206:4-206:36)
    var i2$448=(function(){var tmpvar$449=getNullsafe$444(); return $$$cl1.JsCallable(tmpvar$449,tmpvar$449===null?null:tmpvar$449.f);}())();
    $$$a12.assert($$$cl1.exists(i2$448).equals($$$cl1.getFalse()),$$$cl1.String("nullsafe invoke 3",17));
    $$$a12.assert($$$cl1.exists(NullsafeTest().f2((function(){var tmpvar$450=getNullsafe$444(); return $$$cl1.JsCallable(tmpvar$450,tmpvar$450===null?null:tmpvar$450.f);}()))).equals($$$cl1.getFalse()),$$$cl1.String("nullsafe method ref 3",21));
    
    //AttributeDeclaration obj2 at operators.ceylon (209:4-209:39)
    var obj2$451=NullsafeTest();
    var i3$452;
    if((i3$452=(function(){var tmpvar$453=obj2$451; return $$$cl1.JsCallable(tmpvar$453,tmpvar$453===null?null:tmpvar$453.f);}())())!==null){
        $$$a12.assert(i3$452.equals($$$cl1.Integer(1)),$$$cl1.String("nullsafe invoke 4 (result)",26));
    }
    else {
        $$$a12.fail($$$cl1.String("nullsafe invoke 4 (null)",24));
    }
    
    
    //MethodDeclaration obj2_f at operators.ceylon (215:4-215:31)
    var obj2_f$454=(function(){var tmpvar$455=obj2$451; return $$$cl1.JsCallable(tmpvar$455,tmpvar$455===null?null:tmpvar$455.f);}());
    var i3$456;
    if((i3$456=obj2_f$454())!==null){
        $$$a12.assert(i3$456.equals($$$cl1.Integer(1)),$$$cl1.String("nullsafe method ref 4 (result)",30));
    }
    else {
        $$$a12.fail($$$cl1.String("nullsafe method ref 4 (null)",28));
    }
    
}

//MethodDefinition testIncDecOperators at operators.ceylon (223:0-279:0)
function testIncDecOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (224:4-224:28)
    var i1$457=$$$cl1.Integer(1);
    var setI1$457=function(i1$458){i1$457=i1$458; return i1$457;};
    
    //MethodDefinition f1 at operators.ceylon (225:4-229:4)
    function f1$459(){
        
        //AttributeDeclaration i2 at operators.ceylon (226:8-226:25)
        var i2$460=(setI1$457(i1$457.getSuccessor()),i1$457);
        $$$a12.assert(i1$457.equals($$$cl1.Integer(2)),$$$cl1.String("prefix increment",16));
        $$$a12.assert(i2$460.equals($$$cl1.Integer(2)),$$$cl1.String("prefix increment",16));
    }
    f1$459();
    
    //ClassDefinition C1 at operators.ceylon (232:4-232:49)
    function C1$461($$c1$461){
        $init$C1$461();
        if ($$c1$461===undefined)$$c1$461=new C1$461.$$;
        
        //AttributeDeclaration i at operators.ceylon (232:17-232:47)
        var i$462=$$$cl1.Integer(1);
        var getI=function(){return i$462;};
        $$c1$461.getI=getI;
        var setI=function(i$463){i$462=i$463; return i$462;};
        $$c1$461.setI=setI;
        return $$c1$461;
    }
    function $init$C1$461(){
        if (C1$461.$$===undefined){
            $$$cl1.initTypeProto(C1$461,'operators.testIncDecOperators.C1',$$$cl1.IdentifiableObject);
        }
        return C1$461;
    }
    $init$C1$461();
    
    //AttributeDeclaration c1 at operators.ceylon (233:4-233:16)
    var c1$464=C1$461();
    
    //AttributeDeclaration i3 at operators.ceylon (234:4-234:28)
    var i3$465=$$$cl1.Integer(0);
    var setI3$465=function(i3$466){i3$465=i3$466; return i3$465;};
    
    //MethodDefinition f2 at operators.ceylon (235:4-238:4)
    function f2$467(){
        (setI3$465(i3$465.getSuccessor()),i3$465);
        return c1$464;
    }
    
    //AttributeDeclaration i4 at operators.ceylon (239:4-239:25)
    var i4$468=function($){var $2=$.getI().getSuccessor();$.setI($2);return $2}(f2$467());
    $$$a12.assert(i4$468.equals($$$cl1.Integer(2)),$$$cl1.String("prefix increment",16));
    $$$a12.assert(c1$464.getI().equals($$$cl1.Integer(2)),$$$cl1.String("prefix increment",16));
    $$$a12.assert(i3$465.equals($$$cl1.Integer(1)),$$$cl1.String("prefix increment",16));
    
    //MethodDefinition f3 at operators.ceylon (244:4-248:4)
    function f3$469(){
        
        //AttributeDeclaration i2 at operators.ceylon (245:8-245:25)
        var i2$470=(setI1$457(i1$457.getPredecessor()),i1$457);
        $$$a12.assert(i1$457.equals($$$cl1.Integer(1)),$$$cl1.String("prefix decrement",16));
        $$$a12.assert(i2$470.equals($$$cl1.Integer(1)),$$$cl1.String("prefix decrement",16));
    }
    f3$469();
    
    //AttributeDeclaration i5 at operators.ceylon (251:4-251:25)
    var i5$471=function($){var $2=$.getI().getPredecessor();$.setI($2);return $2}(f2$467());
    $$$a12.assert(i5$471.equals($$$cl1.Integer(1)),$$$cl1.String("prefix decrement",16));
    $$$a12.assert(c1$464.getI().equals($$$cl1.Integer(1)),$$$cl1.String("prefix decrement",16));
    $$$a12.assert(i3$465.equals($$$cl1.Integer(2)),$$$cl1.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (256:4-260:4)
    function f4$472(){
        
        //AttributeDeclaration i2 at operators.ceylon (257:8-257:25)
        var i2$473=(function($){setI1$457($.getSuccessor());return $}(i1$457));
        $$$a12.assert(i1$457.equals($$$cl1.Integer(2)),$$$cl1.String("postfix increment",17));
        $$$a12.assert(i2$473.equals($$$cl1.Integer(1)),$$$cl1.String("postfix increment",17));
    }
    f4$472();
    
    //AttributeDeclaration i6 at operators.ceylon (263:4-263:25)
    var i6$474=function($){var $2=$.getI();$.setI($2.getSuccessor());return $2}(f2$467());
    $$$a12.assert(i6$474.equals($$$cl1.Integer(1)),$$$cl1.String("postfix increment",17));
    $$$a12.assert(c1$464.getI().equals($$$cl1.Integer(2)),$$$cl1.String("postfix increment",17));
    $$$a12.assert(i3$465.equals($$$cl1.Integer(3)),$$$cl1.String("postfix increment",17));
    
    //MethodDefinition f5 at operators.ceylon (268:4-272:4)
    function f5$475(){
        
        //AttributeDeclaration i2 at operators.ceylon (269:8-269:25)
        var i2$476=(function($){setI1$457($.getPredecessor());return $}(i1$457));
        $$$a12.assert(i1$457.equals($$$cl1.Integer(1)),$$$cl1.String("postfix decrement",17));
        $$$a12.assert(i2$476.equals($$$cl1.Integer(2)),$$$cl1.String("postfix decrement",17));
    }
    f5$475();
    
    //AttributeDeclaration i7 at operators.ceylon (275:4-275:25)
    var i7$477=function($){var $2=$.getI();$.setI($2.getPredecessor());return $2}(f2$467());
    $$$a12.assert(i7$477.equals($$$cl1.Integer(2)),$$$cl1.String("postfix decrement",17));
    $$$a12.assert(c1$464.getI().equals($$$cl1.Integer(1)),$$$cl1.String("postfix decrement",17));
    $$$a12.assert(i3$465.equals($$$cl1.Integer(4)),$$$cl1.String("postfix decrement",17));
}

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (281:0-318:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (282:4-282:28)
    var i1$478=$$$cl1.Integer(1);
    var setI1$478=function(i1$479){i1$478=i1$479; return i1$478;};
    (setI1$478(i1$478.plus($$$cl1.Integer(10))),i1$478);
    $$$a12.assert(i1$478.equals($$$cl1.Integer(11)),$$$cl1.String("+= operator",11));
    
    //AttributeDeclaration i2 at operators.ceylon (286:4-286:37)
    var i2$480=(setI1$478(i1$478.plus($$$cl1.Integer(5).getNegativeValue())),i1$478);
    var setI2$480=function(i2$481){i2$480=i2$481; return i2$480;};
    $$$a12.assert(i2$480.equals($$$cl1.Integer(6)),$$$cl1.String("+= operator",11));
    $$$a12.assert(i1$478.equals($$$cl1.Integer(6)),$$$cl1.String("+= operator",11));
    
    //ClassDefinition C1 at operators.ceylon (290:4-290:49)
    function C1$482($$c1$482){
        $init$C1$482();
        if ($$c1$482===undefined)$$c1$482=new C1$482.$$;
        
        //AttributeDeclaration i at operators.ceylon (290:17-290:47)
        var i$483=$$$cl1.Integer(1);
        var getI=function(){return i$483;};
        $$c1$482.getI=getI;
        var setI=function(i$484){i$483=i$484; return i$483;};
        $$c1$482.setI=setI;
        return $$c1$482;
    }
    function $init$C1$482(){
        if (C1$482.$$===undefined){
            $$$cl1.initTypeProto(C1$482,'operators.testArithmeticAssignOperators.C1',$$$cl1.IdentifiableObject);
        }
        return C1$482;
    }
    $init$C1$482();
    
    //AttributeDeclaration c1 at operators.ceylon (291:4-291:16)
    var c1$485=C1$482();
    
    //AttributeDeclaration i3 at operators.ceylon (292:4-292:28)
    var i3$486=$$$cl1.Integer(0);
    var setI3$486=function(i3$487){i3$486=i3$487; return i3$486;};
    
    //MethodDefinition f at operators.ceylon (293:4-296:4)
    function f$488(){
        (setI3$486(i3$486.getSuccessor()),i3$486);
        return c1$485;
    }
    setI2$480((function($1,$2){var $=$1.getI().plus($2);$1.setI($);return $}(f$488(),$$$cl1.Integer(11))));
    $$$a12.assert(i2$480.equals($$$cl1.Integer(12)),$$$cl1.String("+= operator",11));
    $$$a12.assert(c1$485.getI().equals($$$cl1.Integer(12)),$$$cl1.String("+= operator",11));
    $$$a12.assert(i3$486.equals($$$cl1.Integer(1)),$$$cl1.String("+= operator",11));
    setI2$480((setI1$478(i1$478.minus($$$cl1.Integer(14))),i1$478));
    $$$a12.assert(i1$478.equals($$$cl1.Integer(8).getNegativeValue()),$$$cl1.String("-= operator",11));
    $$$a12.assert(i2$480.equals($$$cl1.Integer(8).getNegativeValue()),$$$cl1.String("-= operator",11));
    setI2$480((setI1$478(i1$478.times($$$cl1.Integer(3).getNegativeValue())),i1$478));
    $$$a12.assert(i1$478.equals($$$cl1.Integer(24)),$$$cl1.String("*= operator",11));
    $$$a12.assert(i2$480.equals($$$cl1.Integer(24)),$$$cl1.String("*= operator",11));
    setI2$480((setI1$478(i1$478.divided($$$cl1.Integer(5))),i1$478));
    $$$a12.assert(i1$478.equals($$$cl1.Integer(4)),$$$cl1.String("/= operator",11));
    $$$a12.assert(i2$480.equals($$$cl1.Integer(4)),$$$cl1.String("/= operator",11));
    setI2$480((setI1$478(i1$478.remainder($$$cl1.Integer(3))),i1$478));
    $$$a12.assert(i1$478.equals($$$cl1.Integer(1)),$$$cl1.String("%= operator",11));
    $$$a12.assert(i2$480.equals($$$cl1.Integer(1)),$$$cl1.String("%= operator",11));
}

//MethodDefinition test at operators.ceylon (320:0-331:0)
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
    $$$a12.results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
