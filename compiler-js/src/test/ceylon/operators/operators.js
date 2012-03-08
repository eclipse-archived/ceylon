(function(define) { define(function(require, exports, module) {
var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//AttributeDeclaration assertionCount at operators.ceylon (1:0-1:34)
var $assertionCount=$$$cl15.Integer(0);
function getAssertionCount(){
    return $assertionCount;
}
exports.getAssertionCount=getAssertionCount;
function setAssertionCount(assertionCount){
    $assertionCount=assertionCount; return assertionCount;
}
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at operators.ceylon (2:0-2:32)
var $failureCount=$$$cl15.Integer(0);
function getFailureCount(){
    return $failureCount;
}
exports.getFailureCount=getFailureCount;
function setFailureCount(failureCount){
    $failureCount=failureCount; return failureCount;
}
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at operators.ceylon (4:0-10:0)
function assert(assertion,message){
    if(message===undefined){message=$$$cl15.String("",0)}
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
    var $i1=$$$cl15.Integer(4).getNegativeValue();
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1; return i1;
    }
    setI1(getI1().getNegativeValue());
    assert(getI1().equals($$$cl15.Integer(4)),$$$cl15.String("negation",8));
    setI1($$$cl15.Integer(987654).getNegativeValue().getPositiveValue());
    assert(getI1().equals($$$cl15.Integer(987654).getNegativeValue()),$$$cl15.String("positive",8));
    setI1($$$cl15.Integer(0).getPositiveValue());
    assert(getI1().equals($$$cl15.Integer(0)),$$$cl15.String("+0=0",4));
    setI1($$$cl15.Integer(0).getNegativeValue());
    assert(getI1().equals($$$cl15.Integer(0)),$$$cl15.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (33:4-33:36)
    var $i2=$$$cl15.Integer(123).plus($$$cl15.Integer(456));
    function getI2(){
        return $i2;
    }
    function setI2(i2){
        $i2=i2; return i2;
    }
    assert(getI2().equals($$$cl15.Integer(579)),$$$cl15.String("addition",8));
    setI1(getI2().minus($$$cl15.Integer(16)));
    assert(getI1().equals($$$cl15.Integer(563)),$$$cl15.String("subtraction",11));
    setI2(getI1().getNegativeValue().plus(getI2()).minus($$$cl15.Integer(1)));
    assert(getI2().equals($$$cl15.Integer(15)),$$$cl15.String("-i1+i2-1",8));
    setI1($$$cl15.Integer(3).times($$$cl15.Integer(7)));
    assert(getI1().equals($$$cl15.Integer(21)),$$$cl15.String("multiplication",14));
    setI2(getI1().times($$$cl15.Integer(2)));
    assert(getI2().equals($$$cl15.Integer(42)),$$$cl15.String("multiplication",14));
    setI2($$$cl15.Integer(17).divided($$$cl15.Integer(4)));
    assert(getI2().equals($$$cl15.Integer(4)),$$$cl15.String("integer division",16));
    setI1(getI2().times($$$cl15.Integer(516)).divided(getI1().getNegativeValue()));
    assert(getI1().equals($$$cl15.Integer(98).getNegativeValue()),$$$cl15.String("i2*516/-i1",10));
    setI1($$$cl15.Integer(15).remainder($$$cl15.Integer(4)));
    assert(getI1().equals($$$cl15.Integer(3)),$$$cl15.String("modulo",6));
    setI2($$$cl15.Integer(312).remainder($$$cl15.Integer(12)));
    assert(getI2().equals($$$cl15.Integer(0)),$$$cl15.String("modulo",6));
    setI1($$$cl15.Integer(2).power($$$cl15.Integer(10)));
    assert(getI1().equals($$$cl15.Integer(1024)),$$$cl15.String("power",5));
    setI2($$$cl15.Integer(100).power($$$cl15.Integer(6)));
    assert(getI2().equals($$$cl15.Integer(1000000000000)),$$$cl15.String("power",5));
}

//MethodDefinition testFloatOperators at operators.ceylon (60:0-90:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (62:4-62:29)
    var $f1=$$$cl15.Float(4.2).getNegativeValue();
    function getF1(){
        return $f1;
    }
    function setF1(f1){
        $f1=f1; return f1;
    }
    setF1(getF1().getNegativeValue());
    assert(getF1().equals($$$cl15.Float(4.2)),$$$cl15.String("negation",8));
    setF1($$$cl15.Float(987654.9925567).getNegativeValue().getPositiveValue());
    assert(getF1().equals($$$cl15.Float(987654.9925567).getNegativeValue()),$$$cl15.String("positive",8));
    setF1($$$cl15.Float(0.0).getPositiveValue());
    assert(getF1().equals($$$cl15.Float(0.0)),$$$cl15.String("+0.0=0.0",8));
    setF1($$$cl15.Float(0.0).getNegativeValue());
    assert(getF1().equals($$$cl15.Float(0.0)),$$$cl15.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (72:4-72:43)
    var $f2=$$$cl15.Float(3.14159265).plus($$$cl15.Float(456.0));
    function getF2(){
        return $f2;
    }
    function setF2(f2){
        $f2=f2; return f2;
    }
    assert(getF2().equals($$$cl15.Float(459.14159265)),$$$cl15.String("addition",8));
    setF1(getF2().minus($$$cl15.Float(0.0016)));
    assert(getF1().equals($$$cl15.Float(459.13999265)),$$$cl15.String("subtraction",11));
    setF2(getF1().getNegativeValue().plus(getF2()).minus($$$cl15.Float(1.2)));
    assert(getF2().equals($$$cl15.Float(1.1984000000000037).getNegativeValue()),$$$cl15.String("-f1+f2-1.2",10));
    setF1($$$cl15.Float(3.0).times($$$cl15.Float(0.79)));
    assert(getF1().equals($$$cl15.Float(2.37)),$$$cl15.String("multiplication",14));
    setF2(getF1().times($$$cl15.Float(2.0e13)));
    assert(getF2().equals($$$cl15.Float(47400000000000.0)),$$$cl15.String("multiplication",14));
    setF2($$$cl15.Float(17.1).divided($$$cl15.Float(4.0E-18)));
    assert(getF2().equals($$$cl15.Float(4275000000000000000.0)),$$$cl15.String("division",8));
    setF1(getF2().times($$$cl15.Float(51.6e2)).divided(getF1().getNegativeValue()));
    assert(getF2().equals($$$cl15.Float(4275000000000000000.0)),$$$cl15.String("f2*51.6e2/-f1",13));
    setF1($$$cl15.Float(150.0).power($$$cl15.Float(0.5)));
    assert(getF1().equals($$$cl15.Float(12.24744871391589)),$$$cl15.String("power",5));
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
    var $o1=OpTest1();
    function getO1(){
        return $o1;
    }
    
    //AttributeDeclaration o2 at operators.ceylon (96:4-96:24)
    var $o2=OpTest1();
    function getO2(){
        return $o2;
    }
    
    //AttributeDeclaration b1 at operators.ceylon (97:4-97:36)
    var $b1=(getO1()===getO2()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB1(){
        return $b1;
    }
    function setB1(b1){
        $b1=b1; return b1;
    }
    assert(getB1().equals($$$cl15.getFalse()),$$$cl15.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (99:4-99:36)
    var $b2=(getO1()===getO1()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB2(){
        return $b2;
    }
    function setB2(b2){
        $b2=b2; return b2;
    }
    assert(getB2(),$$$cl15.String("identity",8));
    setB1(getO1().equals(getO2()));
    assert(getB1().equals($$$cl15.getFalse()),$$$cl15.String("equals",6));
    setB2(getO1().equals(getO1()));
    assert(getB2(),$$$cl15.String("equals",6));
    setB1($$$cl15.Integer(1).equals($$$cl15.Integer(2)));
    assert(getB1().equals($$$cl15.getFalse()),$$$cl15.String("equals",6));
    setB2($$$cl15.Integer(1).equals($$$cl15.Integer(2)).equals($$$cl15.getFalse()));
    assert(getB2(),$$$cl15.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (110:4-110:30)
    var $b3=getB2().equals($$$cl15.getFalse());
    function getB3(){
        return $b3;
    }
    function setB3(b3){
        $b3=b3; return b3;
    }
    assert(getB3().equals($$$cl15.getFalse()),$$$cl15.String("not",3));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getFalse():$$$cl15.getFalse()));
    assert(getB1().equals($$$cl15.getFalse()),$$$cl15.String("and",3));
    setB2((getB1()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(getB2().equals($$$cl15.getFalse()),$$$cl15.String("and",3));
    setB3(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(getB3(),$$$cl15.String("and",3));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(getB1(),$$$cl15.String("or",2));
    setB2(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():getB1()));
    assert(getB2(),$$$cl15.String("or",2));
    setB3(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(getB3().equals($$$cl15.getFalse()),$$$cl15.String("or",2));
}

//MethodDefinition testComparisonOperators at operators.ceylon (127:0-157:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (128:4-128:37)
    var $c1=$$$cl15.String("str1",4).compare($$$cl15.String("str2",4));
    function getC1(){
        return $c1;
    }
    assert(getC1().equals($$$cl15.getSmaller()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (130:4-130:37)
    var $c2=$$$cl15.String("str2",4).compare($$$cl15.String("str1",4));
    function getC2(){
        return $c2;
    }
    assert(getC2().equals($$$cl15.getLarger()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (132:4-132:37)
    var $c3=$$$cl15.String("str1",4).compare($$$cl15.String("str1",4));
    function getC3(){
        return $c3;
    }
    assert(getC3().equals($$$cl15.getEqual()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (134:4-134:29)
    var $c4=$$$cl15.String("",0).compare($$$cl15.String("",0));
    function getC4(){
        return $c4;
    }
    assert(getC4().equals($$$cl15.getEqual()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (136:4-136:33)
    var $c5=$$$cl15.String("str1",4).compare($$$cl15.String("",0));
    function getC5(){
        return $c5;
    }
    assert(getC5().equals($$$cl15.getLarger()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (138:4-138:33)
    var $c6=$$$cl15.String("",0).compare($$$cl15.String("str2",4));
    function getC6(){
        return $c6;
    }
    assert(getC6().equals($$$cl15.getSmaller()),$$$cl15.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (141:4-141:42)
    var $b1=$$$cl15.String("str1",4).compare($$$cl15.String("str2",4)).equals($$$cl15.getSmaller());
    function getB1(){
        return $b1;
    }
    function setB1(b1){
        $b1=b1; return b1;
    }
    assert(getB1(),$$$cl15.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (143:4-143:42)
    var $b2=$$$cl15.String("str1",4).compare($$$cl15.String("str2",4)).equals($$$cl15.getLarger());
    function getB2(){
        return $b2;
    }
    function setB2(b2){
        $b2=b2; return b2;
    }
    assert(getB2().equals($$$cl15.getFalse()),$$$cl15.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (145:4-145:43)
    var $b3=($$$cl15.String("str1",4).compare($$$cl15.String("str2",4))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB3(){
        return $b3;
    }
    function setB3(b3){
        $b3=b3; return b3;
    }
    assert(getB3(),$$$cl15.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (147:4-147:43)
    var $b4=($$$cl15.String("str1",4).compare($$$cl15.String("str2",4))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB4(){
        return $b4;
    }
    function setB4(b4){
        $b4=b4; return b4;
    }
    assert(getB4().equals($$$cl15.getFalse()),$$$cl15.String("large as",8));
    setB1($$$cl15.String("str1",4).compare($$$cl15.String("str1",4)).equals($$$cl15.getSmaller()));
    assert(getB1().equals($$$cl15.getFalse()),$$$cl15.String("smaller",7));
    setB2($$$cl15.String("str1",4).compare($$$cl15.String("str1",4)).equals($$$cl15.getLarger()));
    assert(getB2().equals($$$cl15.getFalse()),$$$cl15.String("larger",6));
    setB3(($$$cl15.String("str1",4).compare($$$cl15.String("str1",4))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(getB3(),$$$cl15.String("small as",8));
    setB4(($$$cl15.String("str1",4).compare($$$cl15.String("str1",4))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse()));
    assert(getB4(),$$$cl15.String("large as",8));
}

//MethodDefinition testOtherOperators at operators.ceylon (159:0-171:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (160:4-160:42)
    var $entry=$$$cl15.Entry($$$cl15.Integer(47),$$$cl15.String("hi there",8));
    function getEntry(){
        return $entry;
    }
    assert(getEntry().getKey().equals($$$cl15.Integer(47)),$$$cl15.String("entry key",9));
    assert(getEntry().getItem().equals($$$cl15.String("hi there",8)),$$$cl15.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (163:4-163:30)
    var $entry2=$$$cl15.Entry($$$cl15.getTrue(),getEntry());
    function getEntry2(){
        return $entry2;
    }
    assert(getEntry2().getKey().equals($$$cl15.getTrue()),$$$cl15.String("entry key",9));
    assert(getEntry2().getItem().equals($$$cl15.Entry($$$cl15.Integer(47),$$$cl15.String("hi there",8))),$$$cl15.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (167:4-167:41)
    var $s1=function($){return $!==null?$:$$$cl15.String("noo",3)}(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.String("ok",2):null));
    function getS1(){
        return $s1;
    }
    assert(getS1().equals($$$cl15.String("ok",2)),$$$cl15.String("then/else",9));
    
    //AttributeDeclaration s2 at operators.ceylon (169:4-169:47)
    var $s2=function($){return $!==null?$:$$$cl15.String("great",5)}(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.String("what?",5):null));
    function getS2(){
        return $s2;
    }
    assert(getS2().equals($$$cl15.String("great",5)),$$$cl15.String("then/else",9));
}

//MethodDefinition testCollectionOperators at operators.ceylon (173:0-185:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (174:4-174:33)
    var $seq1=$$$cl15.ArraySequence([$$$cl15.String("one",3),$$$cl15.String("two",3)]);
    function getSeq1(){
        return $seq1;
    }
    
    //AttributeDeclaration s1 at operators.ceylon (175:4-175:30)
    var $s1=function($){return $!==null?$:$$$cl15.String("null",4)}(getSeq1().item($$$cl15.Integer(0)));
    function getS1(){
        return $s1;
    }
    assert(getS1().equals($$$cl15.String("one",3)),$$$cl15.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (177:4-177:30)
    var $s2=function($){return $!==null?$:$$$cl15.String("null",4)}(getSeq1().item($$$cl15.Integer(2)));
    function getS2(){
        return $s2;
    }
    assert(getS2().equals($$$cl15.String("null",4)),$$$cl15.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (179:4-179:31)
    var $s3=function($){return $!==null?$:$$$cl15.String("null",4)}(getSeq1().item($$$cl15.Integer(1).getNegativeValue()));
    function getS3(){
        return $s3;
    }
    assert(getS3().equals($$$cl15.String("null",4)),$$$cl15.String("lookup",6));
    
    //AttributeDeclaration unsafe at operators.ceylon (181:4-181:45)
    var $unsafe=getSeq1();
    function getUnsafe(){
        return $unsafe;
    }
    function setUnsafe(unsafe){
        $unsafe=unsafe; return unsafe;
    }
    assert($$$cl15.exists($$$cl15.exists(getUnsafe())===$$$cl15.getTrue()?getUnsafe().item($$$cl15.Integer(0)):$$$cl15.getNull()),$$$cl15.String("safe index",10));
    setUnsafe($$$cl15.getNull());
    assert($$$cl15.exists($$$cl15.exists(getUnsafe())===$$$cl15.getTrue()?getUnsafe().item($$$cl15.Integer(0)):$$$cl15.getNull()).equals($$$cl15.getFalse()),$$$cl15.String("safe index",10));
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
    var $seq=$$$cl15.ArraySequence([$$$cl15.String("hi",2)]);
    function getSeq(){
        return $seq;
    }
    
    //AttributeDeclaration s1 at operators.ceylon (200:4-200:29)
    var $s1=function($){return $!==null?$:$$$cl15.String("null",4)}(getSeq().item($$$cl15.Integer(0)));
    function getS1(){
        return $s1;
    }
    assert(getS1().equals($$$cl15.String("hi",2)),$$$cl15.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (202:4-202:29)
    var $s2=function($){return $!==null?$:$$$cl15.String("null",4)}(getSeq().item($$$cl15.Integer(1)));
    function getS2(){
        return $s2;
    }
    assert(getS2().equals($$$cl15.String("null",4)),$$$cl15.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (205:4-205:21)
    var $s3=$$$cl15.getNull();
    function getS3(){
        return $s3;
    }
    
    //AttributeDeclaration s4 at operators.ceylon (206:4-206:23)
    var $s4=$$$cl15.String("test",4);
    function getS4(){
        return $s4;
    }
    
    //AttributeDeclaration s5 at operators.ceylon (207:4-207:39)
    var $s5=function($){return $!==null?$:$$$cl15.String("null",4)}((function($){return $===null?null:$.getUppercased()}(getS3())));
    function getS5(){
        return $s5;
    }
    
    //AttributeDeclaration s6 at operators.ceylon (208:4-208:39)
    var $s6=function($){return $!==null?$:$$$cl15.String("null",4)}((function($){return $===null?null:$.getUppercased()}(getS4())));
    function getS6(){
        return $s6;
    }
    assert(getS5().equals($$$cl15.String("null",4)),$$$cl15.String("nullsafe member 1",17));
    assert(getS6().equals($$$cl15.String("TEST",4)),$$$cl15.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (211:4-211:28)
    var $obj=$$$cl15.getNull();
    function getObj(){
        return $obj;
    }
    
    //AttributeDeclaration i at operators.ceylon (212:4-212:25)
    var $i=(function(){var tmpvar$1=getObj(); return $$$cl15.JsCallable(tmpvar$1,tmpvar$1===null?null:tmpvar$1.f);}())();
    function getI(){
        return $i;
    }
    assert($$$cl15.exists(getI()).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (214:4-214:34)
    var $f2=(function(){var tmpvar$2=getObj(); return $$$cl15.JsCallable(tmpvar$2,tmpvar$2===null?null:tmpvar$2.f);}());
    function getF2(){
        return $f2;
    }
    assert($$$cl15.exists(nullsafeTest(getF2())).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (216:4-216:35)
    var $f3=(function(){var tmpvar$3=getObj(); return $$$cl15.JsCallable(tmpvar$3,tmpvar$3===null?null:tmpvar$3.f);}());
    function getF3(){
        return $f3;
    }
    assert($$$cl15.exists(getF3()),$$$cl15.String("nullsafe method ref 2",21));
    (function(){var tmpvar$4=getObj(); return $$$cl15.JsCallable(tmpvar$4,tmpvar$4===null?null:tmpvar$4.f);}())();
    assert($$$cl15.exists((function(){var tmpvar$5=getObj(); return $$$cl15.JsCallable(tmpvar$5,tmpvar$5===null?null:tmpvar$5.f);}())()).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe simple call",20));
    
    //MethodDefinition getNullsafe at operators.ceylon (220:4-220:46)
    function getNullsafe(){
        return getObj();
    }
    
    //MethodDeclaration f4 at operators.ceylon (221:4-221:36)
    var f4=(function(){var tmpvar$6=getNullsafe(); return $$$cl15.JsCallable(tmpvar$6,tmpvar$6===null?null:tmpvar$6.f);}());
    
    //AttributeDeclaration result_f4 at operators.ceylon (222:4-222:29)
    var $result_f4=f4();
    function getResult_f4(){
        return $result_f4;
    }
    assert($$$cl15.exists(getResult_f4()).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (224:4-224:36)
    var $i2=(function(){var tmpvar$7=getNullsafe(); return $$$cl15.JsCallable(tmpvar$7,tmpvar$7===null?null:tmpvar$7.f);}())();
    function getI2(){
        return $i2;
    }
    assert($$$cl15.exists(getI2()).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe invoke 3",17));
    assert($$$cl15.exists((function(){var $=NullsafeTest();return $$$cl15.JsCallable($, $.f2)})()((function(){var tmpvar$8=getNullsafe(); return $$$cl15.JsCallable(tmpvar$8,tmpvar$8===null?null:tmpvar$8.f);}()))).equals($$$cl15.getFalse()),$$$cl15.String("nullsafe method ref 3",21));
}

//MethodDefinition testIncDecOperators at operators.ceylon (229:0-285:0)
function testIncDecOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (230:4-230:28)
    var $i1=$$$cl15.Integer(1);
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1; return i1;
    }
    
    //MethodDefinition f1 at operators.ceylon (231:4-235:4)
    function f1(){
        
        //AttributeDeclaration i2 at operators.ceylon (232:8-232:25)
        var $i2=(setI1(getI1().getSuccessor()),getI1());
        function getI2(){
            return $i2;
        }
        assert(getI1().equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
        assert(getI2().equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
    }
    f1();
    
    //ClassDefinition C1 at operators.ceylon (238:4-238:49)
    function C1($$c1){
        if ($$c1===undefined)$$c1=new C1.$$;
        
        //AttributeDeclaration i at operators.ceylon (238:17-238:47)
        var $i=$$$cl15.Integer(1);
        function getI(){
            return $i;
        }
        $$c1.getI=getI;
        function setI(i){
            $i=i; return i;
        }
        $$c1.setI=setI;
        return $$c1;
    }
    $$$cl15.initType(C1,'operators.testIncDecOperators.C1',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(C1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //AttributeDeclaration c1 at operators.ceylon (239:4-239:16)
    var $c1=C1();
    function getC1(){
        return $c1;
    }
    
    //AttributeDeclaration i3 at operators.ceylon (240:4-240:28)
    var $i3=$$$cl15.Integer(0);
    function getI3(){
        return $i3;
    }
    function setI3(i3){
        $i3=i3; return i3;
    }
    
    //MethodDefinition f2 at operators.ceylon (241:4-244:4)
    function f2(){
        (setI3(getI3().getSuccessor()),getI3());
        return getC1();
    }
    
    //AttributeDeclaration i4 at operators.ceylon (245:4-245:25)
    var $i4=function($){var $2=$.getI().getSuccessor();$.setI($2);return $2}(f2());
    function getI4(){
        return $i4;
    }
    assert(getI4().equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
    assert(getC1().getI().equals($$$cl15.Integer(2)),$$$cl15.String("prefix increment",16));
    assert(getI3().equals($$$cl15.Integer(1)),$$$cl15.String("prefix increment",16));
    
    //MethodDefinition f3 at operators.ceylon (250:4-254:4)
    function f3(){
        
        //AttributeDeclaration i2 at operators.ceylon (251:8-251:25)
        var $i2=(setI1(getI1().getPredecessor()),getI1());
        function getI2(){
            return $i2;
        }
        assert(getI1().equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
        assert(getI2().equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
    }
    f3();
    
    //AttributeDeclaration i5 at operators.ceylon (257:4-257:25)
    var $i5=function($){var $2=$.getI().getPredecessor();$.setI($2);return $2}(f2());
    function getI5(){
        return $i5;
    }
    assert(getI5().equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
    assert(getC1().getI().equals($$$cl15.Integer(1)),$$$cl15.String("prefix decrement",16));
    assert(getI3().equals($$$cl15.Integer(2)),$$$cl15.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (262:4-266:4)
    function f4(){
        
        //AttributeDeclaration i2 at operators.ceylon (263:8-263:25)
        var $i2=(function($){setI1($.getSuccessor());return $}(getI1()));
        function getI2(){
            return $i2;
        }
        assert(getI1().equals($$$cl15.Integer(2)),$$$cl15.String("postfix increment",17));
        assert(getI2().equals($$$cl15.Integer(1)),$$$cl15.String("postfix increment",17));
    }
    f4();
    
    //AttributeDeclaration i6 at operators.ceylon (269:4-269:25)
    var $i6=function($){var $2=$.getI();$.setI($2.getSuccessor());return $2}(f2());
    function getI6(){
        return $i6;
    }
    assert(getI6().equals($$$cl15.Integer(1)),$$$cl15.String("postfix increment",17));
    assert(getC1().getI().equals($$$cl15.Integer(2)),$$$cl15.String("postfix increment",17));
    assert(getI3().equals($$$cl15.Integer(3)),$$$cl15.String("postfix increment",17));
    
    //MethodDefinition f5 at operators.ceylon (274:4-278:4)
    function f5(){
        
        //AttributeDeclaration i2 at operators.ceylon (275:8-275:25)
        var $i2=(function($){setI1($.getPredecessor());return $}(getI1()));
        function getI2(){
            return $i2;
        }
        assert(getI1().equals($$$cl15.Integer(1)),$$$cl15.String("postfix decrement",17));
        assert(getI2().equals($$$cl15.Integer(2)),$$$cl15.String("postfix decrement",17));
    }
    f5();
    
    //AttributeDeclaration i7 at operators.ceylon (281:4-281:25)
    var $i7=function($){var $2=$.getI();$.setI($2.getPredecessor());return $2}(f2());
    function getI7(){
        return $i7;
    }
    assert(getI7().equals($$$cl15.Integer(2)),$$$cl15.String("postfix decrement",17));
    assert(getC1().getI().equals($$$cl15.Integer(1)),$$$cl15.String("postfix decrement",17));
    assert(getI3().equals($$$cl15.Integer(4)),$$$cl15.String("postfix decrement",17));
}

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (287:0-324:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (288:4-288:28)
    var $i1=$$$cl15.Integer(1);
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1; return i1;
    }
    (setI1(getI1().plus($$$cl15.Integer(10))),getI1());
    assert(getI1().equals($$$cl15.Integer(11)),$$$cl15.String("+= operator",11));
    
    //AttributeDeclaration i2 at operators.ceylon (292:4-292:37)
    var $i2=(setI1(getI1().plus($$$cl15.Integer(5).getNegativeValue())),getI1());
    function getI2(){
        return $i2;
    }
    function setI2(i2){
        $i2=i2; return i2;
    }
    assert(getI2().equals($$$cl15.Integer(6)),$$$cl15.String("+= operator",11));
    assert(getI1().equals($$$cl15.Integer(6)),$$$cl15.String("+= operator",11));
    
    //ClassDefinition C1 at operators.ceylon (296:4-296:49)
    function C1($$c1){
        if ($$c1===undefined)$$c1=new C1.$$;
        
        //AttributeDeclaration i at operators.ceylon (296:17-296:47)
        var $i=$$$cl15.Integer(1);
        function getI(){
            return $i;
        }
        $$c1.getI=getI;
        function setI(i){
            $i=i; return i;
        }
        $$c1.setI=setI;
        return $$c1;
    }
    $$$cl15.initType(C1,'operators.testArithmeticAssignOperators.C1',$$$cl15.IdentifiableObject);
    $$$cl15.inheritProto(C1,$$$cl15.IdentifiableObject,'$$$cl15$IdentifiableObject$');
    
    //AttributeDeclaration c1 at operators.ceylon (297:4-297:16)
    var $c1=C1();
    function getC1(){
        return $c1;
    }
    
    //AttributeDeclaration i3 at operators.ceylon (298:4-298:28)
    var $i3=$$$cl15.Integer(0);
    function getI3(){
        return $i3;
    }
    function setI3(i3){
        $i3=i3; return i3;
    }
    
    //MethodDefinition f at operators.ceylon (299:4-302:4)
    function f(){
        (setI3(getI3().getSuccessor()),getI3());
        return getC1();
    }
    setI2((function($1,$2){var $=$1.getI().plus($2);$1.setI($);return $}(f(),$$$cl15.Integer(11))));
    assert(getI2().equals($$$cl15.Integer(12)),$$$cl15.String("+= operator",11));
    assert(getC1().getI().equals($$$cl15.Integer(12)),$$$cl15.String("+= operator",11));
    assert(getI3().equals($$$cl15.Integer(1)),$$$cl15.String("+= operator",11));
    setI2((setI1(getI1().minus($$$cl15.Integer(14))),getI1()));
    assert(getI1().equals($$$cl15.Integer(8).getNegativeValue()),$$$cl15.String("-= operator",11));
    assert(getI2().equals($$$cl15.Integer(8).getNegativeValue()),$$$cl15.String("-= operator",11));
    setI2((setI1(getI1().times($$$cl15.Integer(3).getNegativeValue())),getI1()));
    assert(getI1().equals($$$cl15.Integer(24)),$$$cl15.String("*= operator",11));
    assert(getI2().equals($$$cl15.Integer(24)),$$$cl15.String("*= operator",11));
    setI2((setI1(getI1().divided($$$cl15.Integer(5))),getI1()));
    assert(getI1().equals($$$cl15.Integer(4)),$$$cl15.String("/= operator",11));
    assert(getI2().equals($$$cl15.Integer(4)),$$$cl15.String("/= operator",11));
    setI2((setI1(getI1().remainder($$$cl15.Integer(3))),getI1()));
    assert(getI1().equals($$$cl15.Integer(1)),$$$cl15.String("%= operator",11));
    assert(getI2().equals($$$cl15.Integer(1)),$$$cl15.String("%= operator",11));
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
