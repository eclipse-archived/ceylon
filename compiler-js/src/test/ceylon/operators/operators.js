(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at operators.ceylon (1:0-1:34)
var tmpvar$151=$$$cl15.Integer(0);
var getAssertionCount=function(){return tmpvar$151;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount){tmpvar$151=assertionCount; return tmpvar$151;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at operators.ceylon (2:0-2:32)
var tmpvar$152=$$$cl15.Integer(0);
var getFailureCount=function(){return tmpvar$152;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount){tmpvar$152=failureCount; return tmpvar$152;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at operators.ceylon (4:0-10:0)
function assert(assertion,message){
    if(message===undefined){message=$$$cl15.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl15.Integer(1))),getAssertionCount());
    if ((assertion.equals($$$cl15.getFalse()))===$$$cl15.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl15.Integer(1))),getFailureCount());
        $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertion failed \""),message.getString(),$$$cl15.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at operators.ceylon (12:0-14:0)
function fail(message){
    assert($$$cl15.getFalse(),message);
}
exports.fail=fail;

//MethodDefinition results at operators.ceylon (16:0-19:0)
function results(){
    $$$cl15.print($$$cl15.StringBuilder().appendAll($$$cl15.ArraySequence([$$$cl15.String("assertions ",11),getAssertionCount().getString(),$$$cl15.String(", failures ",11),getFailureCount().getString(),$$$cl15.String("",0)])).getString());
}
exports.results=results;

//MethodDefinition testIntegerOperators at operators.ceylon (21:0-58:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (23:4-23:29)
    var tmpvar$153=$$$cl15.Integer(4).getNegativeValue();
    var setI1=function(i1){tmpvar$153=i1; return tmpvar$153;};
    setI1(tmpvar$153.getNegativeValue());
    assert(tmpvar$153.equals($$$cl15.Integer(4)),$$$cl15.String("negation",8));
    setI1($$$cl15.Integer(987654).getNegativeValue().getPositiveValue());
    assert(tmpvar$153.equals($$$cl15.Integer(987654).getNegativeValue()),$$$cl15.String("positive",8));
    setI1($$$cl15.Integer(0).getPositiveValue());
    assert(tmpvar$153.equals($$$cl15.Integer(0)),$$$cl15.String("+0=0",4));
    setI1($$$cl15.Integer(0).getNegativeValue());
    assert(tmpvar$153.equals($$$cl15.Integer(0)),$$$cl15.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (33:4-33:36)
    var tmpvar$154=$$$cl15.Integer(123).plus($$$cl15.Integer(456));
    var setI2=function(i2){tmpvar$154=i2; return tmpvar$154;};
    assert(tmpvar$154.equals($$$cl15.Integer(579)),$$$cl15.String("addition",8));
    setI1(tmpvar$154.minus($$$cl15.Integer(16)));
    assert(tmpvar$153.equals($$$cl15.Integer(563)),$$$cl15.String("subtraction",11));
    setI2(tmpvar$153.getNegativeValue().plus(tmpvar$154).minus($$$cl15.Integer(1)));
    assert(tmpvar$154.equals($$$cl15.Integer(15)),$$$cl15.String("-i1+i2-1",8));
    setI1($$$cl15.Integer(3).times($$$cl15.Integer(7)));
    assert(tmpvar$153.equals($$$cl15.Integer(21)),$$$cl15.String("multiplication",14));
    setI2(tmpvar$153.times($$$cl15.Integer(2)));
    assert(tmpvar$154.equals($$$cl15.Integer(42)),$$$cl15.String("multiplication",14));
    setI2($$$cl15.Integer(17).divided($$$cl15.Integer(4)));
    assert(tmpvar$154.equals($$$cl15.Integer(4)),$$$cl15.String("integer division",16));
    setI1(tmpvar$154.times($$$cl15.Integer(516)).divided(tmpvar$153.getNegativeValue()));
    assert(tmpvar$153.equals($$$cl15.Integer(98).getNegativeValue()),$$$cl15.String("i2*516/-i1",10));
    setI1($$$cl15.Integer(15).remainder($$$cl15.Integer(4)));
    assert(tmpvar$153.equals($$$cl15.Integer(3)),$$$cl15.String("modulo",6));
    setI2($$$cl15.Integer(312).remainder($$$cl15.Integer(12)));
    assert(tmpvar$154.equals($$$cl15.Integer(0)),$$$cl15.String("modulo",6));
    setI1($$$cl15.Integer(2).power($$$cl15.Integer(10)));
    assert(tmpvar$153.equals($$$cl15.Integer(1024)),$$$cl15.String("power",5));
    setI2($$$cl15.Integer(100).power($$$cl15.Integer(6)));
    assert(tmpvar$154.equals($$$cl15.Integer(1000000000000)),$$$cl15.String("power",5));
}

//MethodDefinition testFloatOperators at operators.ceylon (60:0-90:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (62:4-62:29)
    var tmpvar$155=$$$cl15.Float(4.2).getNegativeValue();
    var setF1=function(f1){tmpvar$155=f1; return tmpvar$155;};
    setF1(tmpvar$155.getNegativeValue());
    assert(tmpvar$155.equals($$$cl15.Float(4.2)),$$$cl15.String("negation",8));
    setF1($$$cl15.Float(987654.9925567).getNegativeValue().getPositiveValue());
    assert(tmpvar$155.equals($$$cl15.Float(987654.9925567).getNegativeValue()),$$$cl15.String("positive",8));
    setF1($$$cl15.Float(0.0).getPositiveValue());
    assert(tmpvar$155.equals($$$cl15.Float(0.0)),$$$cl15.String("+0.0=0.0",8));
    setF1($$$cl15.Float(0.0).getNegativeValue());
    assert(tmpvar$155.equals($$$cl15.Float(0.0)),$$$cl15.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (72:4-72:43)
    var tmpvar$156=$$$cl15.Float(3.14159265).plus($$$cl15.Float(456.0));
    var setF2=function(f2){tmpvar$156=f2; return tmpvar$156;};
    assert(tmpvar$156.equals($$$cl15.Float(459.14159265)),$$$cl15.String("addition",8));
    setF1(tmpvar$156.minus($$$cl15.Float(0.0016)));
    assert(tmpvar$155.equals($$$cl15.Float(459.13999265)),$$$cl15.String("subtraction",11));
    setF2(tmpvar$155.getNegativeValue().plus(tmpvar$156).minus($$$cl15.Float(1.2)));
    assert(tmpvar$156.equals($$$cl15.Float(1.1984000000000037).getNegativeValue()),$$$cl15.String("-f1+f2-1.2",10));
    setF1($$$cl15.Float(3.0).times($$$cl15.Float(0.79)));
    assert(tmpvar$155.equals($$$cl15.Float(2.37)),$$$cl15.String("multiplication",14));
    setF2(tmpvar$155.times($$$cl15.Float(2.0e13)));
    assert(tmpvar$156.equals($$$cl15.Float(47400000000000.0)),$$$cl15.String("multiplication",14));
    setF2($$$cl15.Float(17.1).divided($$$cl15.Float(4.0E-18)));
    assert(tmpvar$156.equals($$$cl15.Float(4275000000000000000.0)),$$$cl15.String("division",8));
    setF1(tmpvar$156.times($$$cl15.Float(51.6e2)).divided(tmpvar$155.getNegativeValue()));
    assert(tmpvar$156.equals($$$cl15.Float(4275000000000000000.0)),$$$cl15.String("f2*51.6e2/-f1",13));
    setF1($$$cl15.Float(150.0).power($$$cl15.Float(0.5)));
    assert(tmpvar$155.equals($$$cl15.Float(12.24744871391589)),$$$cl15.String("power",5));
}

//ClassDefinition OpTest1 at operators.ceylon (92:0-92:17)
function OpTest1($$opTest1){
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
$$$cl15.initType(OpTest1,'operators.OpTest1',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(OpTest1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition testBooleanOperators at operators.ceylon (94:0-125:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (95:4-95:24)
    var tmpvar$157=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (96:4-96:24)
    var tmpvar$158=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (97:4-97:36)
    var tmpvar$159=(tmpvar$157===tmpvar$158?$$$cl15.getTrue():$$$cl15.getFalse());
    var setB1=function(b1){tmpvar$159=b1; return tmpvar$159;};
    assert(tmpvar$159.equals($$$cl15.getFalse()),$$$cl15.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (99:4-99:36)
    var tmpvar$160=(tmpvar$157===tmpvar$157?$$$cl15.getTrue():$$$cl15.getFalse());
    var setB2=function(b2){tmpvar$160=b2; return tmpvar$160;};
    assert(tmpvar$160,$$$cl15.String("identity",8));
    setB1(tmpvar$157.equals(tmpvar$158));
    assert(tmpvar$159.equals($$$cl15.getFalse()),$$$cl15.String("equals",6));
    setB2(tmpvar$157.equals(tmpvar$157));
    assert(tmpvar$160,$$$cl15.String("equals",6));
    setB1($$$cl15.Integer(1).equals($$$cl15.Integer(2)));
    assert(tmpvar$159.equals($$$cl15.getFalse()),$$$cl15.String("equals",6));
    setB2($$$cl15.Integer(1).equals($$$cl15.Integer(2)).equals($$$cl15.getFalse()));
    assert(tmpvar$160,$$$cl15.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (110:4-110:30)
    var tmpvar$161=tmpvar$160.equals($$$cl15.getFalse());
    var setB3=function(b3){tmpvar$161=b3; return tmpvar$161;};
    assert(tmpvar$161.equals($$$cl15.getFalse()),$$$cl15.String("not",3));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getFalse():$$$cl15.getFalse()));
    assert(tmpvar$159.equals($$$cl15.getFalse()),$$$cl15.String("and",3));
    setB2((tmpvar$159===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(tmpvar$160.equals($$$cl15.getFalse()),$$$cl15.String("and",3));
    setB3(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(tmpvar$161,$$$cl15.String("and",3));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(tmpvar$159,$$$cl15.String("or",2));
    setB2(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():tmpvar$159));
    assert(tmpvar$160,$$$cl15.String("or",2));
    setB3(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(tmpvar$161.equals($$$cl15.getFalse()),$$$cl15.String("or",2));
}

//MethodDefinition testComparisonOperators at operators.ceylon (127:0-157:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (128:4-128:37)
    var tmpvar$162=$$$cl15.String("str1",4).compare($$$cl15.String("str2",4));
    assert(tmpvar$162.equals($$$cl15.getSmaller()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (130:4-130:37)
    var tmpvar$163=$$$cl15.String("str2",4).compare($$$cl15.String("str1",4));
    assert(tmpvar$163.equals($$$cl15.getLarger()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (132:4-132:37)
    var tmpvar$164=$$$cl15.String("str1",4).compare($$$cl15.String("str1",4));
    assert(tmpvar$164.equals($$$cl15.getEqual()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (134:4-134:29)
    var tmpvar$165=$$$cl15.String("",0).compare($$$cl15.String("",0));
    assert(tmpvar$165.equals($$$cl15.getEqual()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (136:4-136:33)
    var tmpvar$166=$$$cl15.String("str1",4).compare($$$cl15.String("",0));
    assert(tmpvar$166.equals($$$cl15.getLarger()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (138:4-138:33)
    var tmpvar$167=$$$cl15.String("",0).compare($$$cl15.String("str2",4));
    assert(tmpvar$167.equals($$$cl15.getSmaller()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (141:4-141:42)
    var tmpvar$168=$$$cl15.String("str1",4).compare($$$cl15.String("str2",4)).equals($$$cl15.getSmaller());
    var setB1=function(b1){tmpvar$168=b1; return tmpvar$168;};
    assert(tmpvar$168,$$$cl15.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (143:4-143:42)
    var tmpvar$169=$$$cl15.String("str1",4).compare($$$cl15.String("str2",4)).equals($$$cl15.getLarger());
    var setB2=function(b2){tmpvar$169=b2; return tmpvar$169;};
    assert(tmpvar$169.equals($$$cl15.getFalse()),$$$cl15.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (145:4-145:43)
    var tmpvar$170=($$$cl15.String("str1",4).compare($$$cl15.String("str2",4))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse());
    var setB3=function(b3){tmpvar$170=b3; return tmpvar$170;};
    assert(tmpvar$170,$$$cl15.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (147:4-147:43)
    var tmpvar$171=($$$cl15.String("str1",4).compare($$$cl15.String("str2",4))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse());
    var setB4=function(b4){tmpvar$171=b4; return tmpvar$171;};
    assert(tmpvar$171.equals($$$cl15.getFalse()),$$$cl15.String("large as",8));
    setB1($$$cl15.String("str1",4).compare($$$cl15.String("str1",4)).equals($$$cl15.getSmaller()));
    assert(tmpvar$168.equals($$$cl15.getFalse()),$$$cl15.String("smaller",7));
    setB2($$$cl15.String("str1",4).compare($$$cl15.String("str1",4)).equals($$$cl15.getLarger()));
    assert(tmpvar$169.equals($$$cl15.getFalse()),$$$cl15.String("larger",6));
    setB3(($$$cl15.String("str1",4).compare($$$cl15.String("str1",4))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(tmpvar$170,$$$cl15.String("small as",8));
    setB4(($$$cl15.String("str1",4).compare($$$cl15.String("str1",4))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(tmpvar$171,$$$cl15.String("large as",8));
}

//MethodDefinition testOtherOperators at operators.ceylon (159:0-171:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (160:4-160:42)
    var tmpvar$172=$$$cl15.Entry($$$cl15.Integer(47),$$$cl15.String("hi there",8));
    assert(tmpvar$172.getKey().equals($$$cl15.Integer(47)),$$$cl15.String("entry key",9));
    assert(tmpvar$172.getItem().equals($$$cl15.String("hi there",8)),$$$cl15.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (163:4-163:30)
    var tmpvar$173=$$$cl15.Entry($$$cl15.getTrue(),tmpvar$172);
    assert(tmpvar$173.getKey().equals($$$cl15.getTrue()),$$$cl15.String("entry key",9));
    assert(tmpvar$173.getItem().equals($$$cl15.Entry($$$cl15.Integer(47),$$$cl15.String("hi there",8))),$$$cl15.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (167:4-167:41)
    var tmpvar$174=function($){return $!==null?$:$$$cl15.String("noo",3)}(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.String("ok",2):null));
    assert(tmpvar$174.equals($$$cl15.String("ok",2)),$$$cl15.String("then/else",9));
    
    //AttributeDeclaration s2 at operators.ceylon (169:4-169:47)
    var tmpvar$175=function($){return $!==null?$:$$$cl15.String("great",5)}(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.String("what?",5):null));
    assert(tmpvar$175.equals($$$cl15.String("great",5)),$$$cl15.String("then/else",9));
}

//MethodDefinition testCollectionOperators at operators.ceylon (173:0-185:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (174:4-174:33)
    var tmpvar$176=$$$cl15.ArraySequence([$$$cl15.String("one",3),$$$cl15.String("two",3)]);
    
    //AttributeDeclaration s1 at operators.ceylon (175:4-175:30)
    var tmpvar$177=function($){return $!==null?$:$$$cl15.String("null",4)}(tmpvar$176.item($$$cl15.Integer(0)));
    assert(tmpvar$177.equals($$$cl15.String("one",3)),$$$cl15.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (177:4-177:30)
    var tmpvar$178=function($){return $!==null?$:$$$cl15.String("null",4)}(tmpvar$176.item($$$cl15.Integer(2)));
    assert(tmpvar$178.equals($$$cl15.String("null",4)),$$$cl15.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (179:4-179:31)
    var tmpvar$179=function($){return $!==null?$:$$$cl15.String("null",4)}(tmpvar$176.item($$$cl15.Integer(1).getNegativeValue()));
    assert(tmpvar$179.equals($$$cl15.String("null",4)),$$$cl15.String("lookup",6));
    
    //AttributeDeclaration unsafe at operators.ceylon (181:4-181:45)
    var tmpvar$180=tmpvar$176;
    var setUnsafe=function(unsafe){tmpvar$180=unsafe; return tmpvar$180;};
    assert($$$cl15.exists($$$cl15.exists(tmpvar$180)===$$$cl15.getTrue()?tmpvar$180.item($$$cl15.Integer(0)):$$$cl15.getNull()),$$$cl15.String("safe index",10));
    setUnsafe($$$cl15.getNull());
    assert($$$cl15.exists($$$cl15.exists(tmpvar$180)===$$$cl15.getTrue()?tmpvar$180.item($$$cl15.Integer(0)):$$$cl15.getNull()).equals($$$cl15.getFalse()),$$$cl15.String("safe index",10));
}

//ClassDefinition NullsafeTest at operators.ceylon (187:0-192:0)
function NullsafeTest($$nullsafeTest){
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    
    //MethodDefinition f at operators.ceylon (188:4-188:33)
    function f(){
        return $$$cl15.Integer(1);
    }
    $$nullsafeTest.f=f;
    
    //MethodDefinition f2 at operators.ceylon (189:4-191:4)
    function f2(x){
        return x();
    }
    $$nullsafeTest.f2=f2;
    return $$nullsafeTest;
}
$$$cl15.initType(NullsafeTest,'operators.NullsafeTest',$$$cl15.IdentifiableObject);
$$$cl15.inheritProto(NullsafeTest,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');

//MethodDefinition nullsafeTest at operators.ceylon (194:0-196:0)
function nullsafeTest(f){
    return f();
}

//MethodDefinition testNullsafeOperators at operators.ceylon (198:0-227:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (199:4-199:27)
    var tmpvar$181=$$$cl15.ArraySequence([$$$cl15.String("hi",2)]);
    
    //AttributeDeclaration s1 at operators.ceylon (200:4-200:29)
    var tmpvar$182=function($){return $!==null?$:$$$cl15.String("null",4)}(tmpvar$181.item($$$cl15.Integer(0)));
    assert(tmpvar$182.equals($$$cl15.String("hi",2)),$$$cl15.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (202:4-202:29)
    var tmpvar$183=function($){return $!==null?$:$$$cl15.String("null",4)}(tmpvar$181.item($$$cl15.Integer(1)));
    assert(tmpvar$183.equals($$$cl15.String("null",4)),$$$cl15.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (205:4-205:21)
    var tmpvar$184=$$$cl15.getNull();
    
    //AttributeDeclaration s4 at operators.ceylon (206:4-206:23)
    var tmpvar$185=$$$cl15.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (207:4-207:39)
    var tmpvar$186=function($){return $!==null?$:$$$cl15.String("null",4)}((function($){return $===null?null:$.getUppercased()}(tmpvar$184)));
    
    //AttributeDeclaration s6 at operators.ceylon (208:4-208:39)
    var tmpvar$187=function($){return $!==null?$:$$$cl15.String("null",4)}((function($){return $===null?null:$.getUppercased()}(tmpvar$185)));
    assert(tmpvar$186.equals($$$cl15.String("null",4)),$$$cl15.String("nullsafe member 1",17));
    assert(tmpvar$187.equals($$$cl15.String("TEST",4)),$$$cl15.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (211:4-211:28)
    var tmpvar$188=$$$cl15.getNull();
    
    //AttributeDeclaration i at operators.ceylon (212:4-212:25)
    var tmpvar$189=(function(){var tmpvar$190=tmpvar$188; return $$$cl15.JsCallable(tmpvar$190,tmpvar$190===null?null:tmpvar$190.f);}())();
    assert($$$cl15.exists(tmpvar$189).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (214:4-214:34)
    var tmpvar$191=(function(){var tmpvar$192=tmpvar$188; return $$$cl15.JsCallable(tmpvar$192,tmpvar$192===null?null:tmpvar$192.f);}());
    assert($$$cl15.exists(nullsafeTest(tmpvar$191)).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (216:4-216:35)
    var tmpvar$193=(function(){var tmpvar$194=tmpvar$188; return $$$cl15.JsCallable(tmpvar$194,tmpvar$194===null?null:tmpvar$194.f);}());
    assert($$$cl15.exists(tmpvar$193),$$$cl15.String("nullsafe method ref 2",21));
    (function(){var tmpvar$195=tmpvar$188; return $$$cl15.JsCallable(tmpvar$195,tmpvar$195===null?null:tmpvar$195.f);}())();
    assert($$$cl15.exists((function(){var tmpvar$196=tmpvar$188; return $$$cl15.JsCallable(tmpvar$196,tmpvar$196===null?null:tmpvar$196.f);}())()).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe simple call",20));
    
    //MethodDefinition getNullsafe at operators.ceylon (220:4-220:46)
    function getNullsafe(){
        return tmpvar$188;
    }
    
    //MethodDeclaration f4 at operators.ceylon (221:4-221:36)
    var f4=(function(){var tmpvar$197=getNullsafe(); return $$$cl15.JsCallable(tmpvar$197,tmpvar$197===null?null:tmpvar$197.f);}());
    
    //AttributeDeclaration result_f4 at operators.ceylon (222:4-222:29)
    var tmpvar$198=f4();
    assert($$$cl15.exists(tmpvar$198).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (224:4-224:36)
    var tmpvar$199=(function(){var tmpvar$200=getNullsafe(); return $$$cl15.JsCallable(tmpvar$200,tmpvar$200===null?null:tmpvar$200.f);}())();
    assert($$$cl15.exists(tmpvar$199).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe invoke 3",17));
    assert($$$cl15.exists(NullsafeTest().f2((function(){var tmpvar$201=getNullsafe(); return $$$cl15.JsCallable(tmpvar$201,tmpvar$201===null?null:tmpvar$201.f);}()))).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe method ref 3",21));
}

//MethodDefinition testIncDecOperators at operators.ceylon (229:0-285:0)
function testIncDecOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (230:4-230:28)
    var tmpvar$202=$$$cl15.Integer(1);
    var setI1=function(i1){tmpvar$202=i1; return tmpvar$202;};
    
    //MethodDefinition f1 at operators.ceylon (231:4-235:4)
    function f1(){
        
        //AttributeDeclaration i2 at operators.ceylon (232:8-232:25)
        var tmpvar$203=(setI1(tmpvar$202.getSuccessor()),tmpvar$202);
        assert(tmpvar$202.equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
        assert(tmpvar$203.equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
    }
    f1();
    
    //ClassDefinition C1 at operators.ceylon (238:4-238:49)
    function C1($$c1){
        if ($$c1===undefined)$$c1=new C1.$$;
        
        //AttributeDeclaration i at operators.ceylon (238:17-238:47)
        var tmpvar$204=$$$cl15.Integer(1);
        var getI=function(){return tmpvar$204;};
        $$c1.getI=getI;
        var setI=function(i){tmpvar$204=i; return tmpvar$204;};
        $$c1.setI=setI;
        return $$c1;
    }
    $$$cl15.initType(C1,'operators.testIncDecOperators.C1',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(C1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //AttributeDeclaration c1 at operators.ceylon (239:4-239:16)
    var tmpvar$205=C1();
    
    //AttributeDeclaration i3 at operators.ceylon (240:4-240:28)
    var tmpvar$206=$$$cl15.Integer(0);
    var setI3=function(i3){tmpvar$206=i3; return tmpvar$206;};
    
    //MethodDefinition f2 at operators.ceylon (241:4-244:4)
    function f2(){
        (setI3(tmpvar$206.getSuccessor()),tmpvar$206);
        return tmpvar$205;
    }
    
    //AttributeDeclaration i4 at operators.ceylon (245:4-245:25)
    var tmpvar$207=function($){var $2=$.getI().getSuccessor();$.setI($2);return $2}(f2());
    assert(tmpvar$207.equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
    assert(tmpvar$205.getI().equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
    assert(tmpvar$206.equals($$$cl15.Integer(1)),$$$cl15.String("prefix increment",16));
    
    //MethodDefinition f3 at operators.ceylon (250:4-254:4)
    function f3(){
        
        //AttributeDeclaration i2 at operators.ceylon (251:8-251:25)
        var tmpvar$208=(setI1(tmpvar$202.getPredecessor()),tmpvar$202);
        assert(tmpvar$202.equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
        assert(tmpvar$208.equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
    }
    f3();
    
    //AttributeDeclaration i5 at operators.ceylon (257:4-257:25)
    var tmpvar$209=function($){var $2=$.getI().getPredecessor();$.setI($2);return $2}(f2());
    assert(tmpvar$209.equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
    assert(tmpvar$205.getI().equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
    assert(tmpvar$206.equals($$$cl15.Integer(2)),$$$cl15.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (262:4-266:4)
    function f4(){
        
        //AttributeDeclaration i2 at operators.ceylon (263:8-263:25)
        var tmpvar$210=(function($){setI1($.getSuccessor());return $}(tmpvar$202));
        assert(tmpvar$202.equals($$$cl15.Integer(2)),$$$cl15.String("postfix increment",17));
        assert(tmpvar$210.equals($$$cl15.Integer(1)),$$$cl15.String("postfix increment",17));
    }
    f4();
    
    //AttributeDeclaration i6 at operators.ceylon (269:4-269:25)
    var tmpvar$211=function($){var $2=$.getI();$.setI($2.getSuccessor());return $2}(f2());
    assert(tmpvar$211.equals($$$cl15.Integer(1)),$$$cl15.String("postfix increment",17));
    assert(tmpvar$205.getI().equals($$$cl15.Integer(2)),$$$cl15.String("postfix increment",17));
    assert(tmpvar$206.equals($$$cl15.Integer(3)),$$$cl15.String("postfix increment",17));
    
    //MethodDefinition f5 at operators.ceylon (274:4-278:4)
    function f5(){
        
        //AttributeDeclaration i2 at operators.ceylon (275:8-275:25)
        var tmpvar$212=(function($){setI1($.getPredecessor());return $}(tmpvar$202));
        assert(tmpvar$202.equals($$$cl15.Integer(1)),$$$cl15.String("postfix decrement",17));
        assert(tmpvar$212.equals($$$cl15.Integer(2)),$$$cl15.String("postfix decrement",17));
    }
    f5();
    
    //AttributeDeclaration i7 at operators.ceylon (281:4-281:25)
    var tmpvar$213=function($){var $2=$.getI();$.setI($2.getPredecessor());return $2}(f2());
    assert(tmpvar$213.equals($$$cl15.Integer(2)),$$$cl15.String("postfix decrement",17));
    assert(tmpvar$205.getI().equals($$$cl15.Integer(1)),$$$cl15.String("postfix decrement",17));
    assert(tmpvar$206.equals($$$cl15.Integer(4)),$$$cl15.String("postfix decrement",17));
}

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (287:0-324:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (288:4-288:28)
    var tmpvar$214=$$$cl15.Integer(1);
    var setI1=function(i1){tmpvar$214=i1; return tmpvar$214;};
    (setI1(tmpvar$214.plus($$$cl15.Integer(10))),tmpvar$214);
    assert(tmpvar$214.equals($$$cl15.Integer(11)),$$$cl15.String("+= operator",11));
    
    //AttributeDeclaration i2 at operators.ceylon (292:4-292:37)
    var tmpvar$215=(setI1(tmpvar$214.plus($$$cl15.Integer(5).getNegativeValue())),tmpvar$214);
    var setI2=function(i2){tmpvar$215=i2; return tmpvar$215;};
    assert(tmpvar$215.equals($$$cl15.Integer(6)),$$$cl15.String("+= operator",11));
    assert(tmpvar$214.equals($$$cl15.Integer(6)),$$$cl15.String("+= operator",11));
    
    //ClassDefinition C1 at operators.ceylon (296:4-296:49)
    function C1($$c1){
        if ($$c1===undefined)$$c1=new C1.$$;
        
        //AttributeDeclaration i at operators.ceylon (296:17-296:47)
        var tmpvar$216=$$$cl15.Integer(1);
        var getI=function(){return tmpvar$216;};
        $$c1.getI=getI;
        var setI=function(i){tmpvar$216=i; return tmpvar$216;};
        $$c1.setI=setI;
        return $$c1;
    }
    $$$cl15.initType(C1,'operators.testArithmeticAssignOperators.C1',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(C1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //AttributeDeclaration c1 at operators.ceylon (297:4-297:16)
    var tmpvar$217=C1();
    
    //AttributeDeclaration i3 at operators.ceylon (298:4-298:28)
    var tmpvar$218=$$$cl15.Integer(0);
    var setI3=function(i3){tmpvar$218=i3; return tmpvar$218;};
    
    //MethodDefinition f at operators.ceylon (299:4-302:4)
    function f(){
        (setI3(tmpvar$218.getSuccessor()),tmpvar$218);
        return tmpvar$217;
    }
    setI2((function($1,$2){var $=$1.getI().plus($2);$1.setI($);return $}(f(),$$$cl15.Integer(11))));
    assert(tmpvar$215.equals($$$cl15.Integer(12)),$$$cl15.String("+= operator",11));
    assert(tmpvar$217.getI().equals($$$cl15.Integer(12)),$$$cl15.String("+= operator",11));
    assert(tmpvar$218.equals($$$cl15.Integer(1)),$$$cl15.String("+= operator",11));
    setI2((setI1(tmpvar$214.minus($$$cl15.Integer(14))),tmpvar$214));
    assert(tmpvar$214.equals($$$cl15.Integer(8).getNegativeValue()),$$$cl15.String("-= operator",11));
    assert(tmpvar$215.equals($$$cl15.Integer(8).getNegativeValue()),$$$cl15.String("-= operator",11));
    setI2((setI1(tmpvar$214.times($$$cl15.Integer(3).getNegativeValue())),tmpvar$214));
    assert(tmpvar$214.equals($$$cl15.Integer(24)),$$$cl15.String("*= operator",11));
    assert(tmpvar$215.equals($$$cl15.Integer(24)),$$$cl15.String("*= operator",11));
    setI2((setI1(tmpvar$214.divided($$$cl15.Integer(5))),tmpvar$214));
    assert(tmpvar$214.equals($$$cl15.Integer(4)),$$$cl15.String("/= operator",11));
    assert(tmpvar$215.equals($$$cl15.Integer(4)),$$$cl15.String("/= operator",11));
    setI2((setI1(tmpvar$214.remainder($$$cl15.Integer(3))),tmpvar$214));
    assert(tmpvar$214.equals($$$cl15.Integer(1)),$$$cl15.String("%= operator",11));
    assert(tmpvar$215.equals($$$cl15.Integer(1)),$$$cl15.String("%= operator",11));
}

//MethodDefinition test at operators.ceylon (326:0-337:0)
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
    results();
}
exports.test=test;
});
}(typeof define==='function' && define.amd ? define : function (factory) {
if (typeof exports!=='undefined') { factory(require, exports, module);
} else { throw 'no module loader'; }
}));
