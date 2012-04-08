(function(define) { define(function(require, exports, module) {
var $$$cl275=require('ceylon/language/0.2/ceylon.language');

//AttributeDeclaration assertionCount at operators.ceylon (1:0-1:34)
var assertionCount$276=$$$cl275.Integer(0);
var getAssertionCount=function(){return assertionCount$276;};
exports.getAssertionCount=getAssertionCount;
var setAssertionCount=function(assertionCount$277){assertionCount$276=assertionCount$277; return assertionCount$276;};
exports.setAssertionCount=setAssertionCount;

//AttributeDeclaration failureCount at operators.ceylon (2:0-2:32)
var failureCount$278=$$$cl275.Integer(0);
var getFailureCount=function(){return failureCount$278;};
exports.getFailureCount=getFailureCount;
var setFailureCount=function(failureCount$279){failureCount$278=failureCount$279; return failureCount$278;};
exports.setFailureCount=setFailureCount;

//MethodDefinition assert at operators.ceylon (4:0-10:0)
function assert(assertion$280,message$281){
    if(message$281===undefined){message$281=$$$cl275.String("",0);}
    (setAssertionCount(getAssertionCount().plus($$$cl275.Integer(1))),getAssertionCount());
    if ((assertion$280.equals($$$cl275.getFalse()))===$$$cl275.getTrue()){
        (setFailureCount(getFailureCount().plus($$$cl275.Integer(1))),getFailureCount());
        $$$cl275.print($$$cl275.StringBuilder().appendAll($$$cl275.ArraySequence([$$$cl275.String("assertion failed \""),message$281.getString(),$$$cl275.String("\"")])).getString());
    }
    
}
exports.assert=assert;

//MethodDefinition fail at operators.ceylon (12:0-14:0)
function fail(message$282){
    assert($$$cl275.getFalse(),message$282);
}
exports.fail=fail;

//MethodDefinition results at operators.ceylon (16:0-19:0)
function results(){
    $$$cl275.print($$$cl275.StringBuilder().appendAll($$$cl275.ArraySequence([$$$cl275.String("assertions ",11),getAssertionCount().getString(),$$$cl275.String(", failures ",11),getFailureCount().getString(),$$$cl275.String("",0)])).getString());
}
exports.results=results;

//MethodDefinition testIntegerOperators at operators.ceylon (21:0-58:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (23:4-23:29)
    var i1$283=$$$cl275.Integer(4).getNegativeValue();
    var setI1$283=function(i1$284){i1$283=i1$284; return i1$283;};
    setI1$283(i1$283.getNegativeValue());
    assert(i1$283.equals($$$cl275.Integer(4)),$$$cl275.String("negation",8));
    setI1$283($$$cl275.Integer(987654).getNegativeValue().getPositiveValue());
    assert(i1$283.equals($$$cl275.Integer(987654).getNegativeValue()),$$$cl275.String("positive",8));
    setI1$283($$$cl275.Integer(0).getPositiveValue());
    assert(i1$283.equals($$$cl275.Integer(0)),$$$cl275.String("+0=0",4));
    setI1$283($$$cl275.Integer(0).getNegativeValue());
    assert(i1$283.equals($$$cl275.Integer(0)),$$$cl275.String("+0=0",4));
    
    //AttributeDeclaration i2 at operators.ceylon (33:4-33:36)
    var i2$285=$$$cl275.Integer(123).plus($$$cl275.Integer(456));
    var setI2$285=function(i2$286){i2$285=i2$286; return i2$285;};
    assert(i2$285.equals($$$cl275.Integer(579)),$$$cl275.String("addition",8));
    setI1$283(i2$285.minus($$$cl275.Integer(16)));
    assert(i1$283.equals($$$cl275.Integer(563)),$$$cl275.String("subtraction",11));
    setI2$285(i1$283.getNegativeValue().plus(i2$285).minus($$$cl275.Integer(1)));
    assert(i2$285.equals($$$cl275.Integer(15)),$$$cl275.String("-i1+i2-1",8));
    setI1$283($$$cl275.Integer(3).times($$$cl275.Integer(7)));
    assert(i1$283.equals($$$cl275.Integer(21)),$$$cl275.String("multiplication",14));
    setI2$285(i1$283.times($$$cl275.Integer(2)));
    assert(i2$285.equals($$$cl275.Integer(42)),$$$cl275.String("multiplication",14));
    setI2$285($$$cl275.Integer(17).divided($$$cl275.Integer(4)));
    assert(i2$285.equals($$$cl275.Integer(4)),$$$cl275.String("integer division",16));
    setI1$283(i2$285.times($$$cl275.Integer(516)).divided(i1$283.getNegativeValue()));
    assert(i1$283.equals($$$cl275.Integer(98).getNegativeValue()),$$$cl275.String("i2*516/-i1",10));
    setI1$283($$$cl275.Integer(15).remainder($$$cl275.Integer(4)));
    assert(i1$283.equals($$$cl275.Integer(3)),$$$cl275.String("modulo",6));
    setI2$285($$$cl275.Integer(312).remainder($$$cl275.Integer(12)));
    assert(i2$285.equals($$$cl275.Integer(0)),$$$cl275.String("modulo",6));
    setI1$283($$$cl275.Integer(2).power($$$cl275.Integer(10)));
    assert(i1$283.equals($$$cl275.Integer(1024)),$$$cl275.String("power",5));
    setI2$285($$$cl275.Integer(100).power($$$cl275.Integer(6)));
    assert(i2$285.equals($$$cl275.Integer(1000000000000)),$$$cl275.String("power",5));
}

//MethodDefinition testFloatOperators at operators.ceylon (60:0-90:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (62:4-62:29)
    var f1$287=$$$cl275.Float(4.2).getNegativeValue();
    var setF1$287=function(f1$288){f1$287=f1$288; return f1$287;};
    setF1$287(f1$287.getNegativeValue());
    assert(f1$287.equals($$$cl275.Float(4.2)),$$$cl275.String("negation",8));
    setF1$287($$$cl275.Float(987654.9925567).getNegativeValue().getPositiveValue());
    assert(f1$287.equals($$$cl275.Float(987654.9925567).getNegativeValue()),$$$cl275.String("positive",8));
    setF1$287($$$cl275.Float(0.0).getPositiveValue());
    assert(f1$287.equals($$$cl275.Float(0.0)),$$$cl275.String("+0.0=0.0",8));
    setF1$287($$$cl275.Float(0.0).getNegativeValue());
    assert(f1$287.equals($$$cl275.Float(0.0)),$$$cl275.String("-0.0=0.0",8));
    
    //AttributeDeclaration f2 at operators.ceylon (72:4-72:43)
    var f2$289=$$$cl275.Float(3.14159265).plus($$$cl275.Float(456.0));
    var setF2$289=function(f2$290){f2$289=f2$290; return f2$289;};
    assert(f2$289.equals($$$cl275.Float(459.14159265)),$$$cl275.String("addition",8));
    setF1$287(f2$289.minus($$$cl275.Float(0.0016)));
    assert(f1$287.equals($$$cl275.Float(459.13999265)),$$$cl275.String("subtraction",11));
    setF2$289(f1$287.getNegativeValue().plus(f2$289).minus($$$cl275.Float(1.2)));
    assert(f2$289.equals($$$cl275.Float(1.1984000000000037).getNegativeValue()),$$$cl275.String("-f1+f2-1.2",10));
    setF1$287($$$cl275.Float(3.0).times($$$cl275.Float(0.79)));
    assert(f1$287.equals($$$cl275.Float(2.37)),$$$cl275.String("multiplication",14));
    setF2$289(f1$287.times($$$cl275.Float(2.0e13)));
    assert(f2$289.equals($$$cl275.Float(47400000000000.0)),$$$cl275.String("multiplication",14));
    setF2$289($$$cl275.Float(17.1).divided($$$cl275.Float(4.0E-18)));
    assert(f2$289.equals($$$cl275.Float(4275000000000000000.0)),$$$cl275.String("division",8));
    setF1$287(f2$289.times($$$cl275.Float(51.6e2)).divided(f1$287.getNegativeValue()));
    assert(f2$289.equals($$$cl275.Float(4275000000000000000.0)),$$$cl275.String("f2*51.6e2/-f1",13));
    setF1$287($$$cl275.Float(150.0).power($$$cl275.Float(0.5)));
    assert(f1$287.equals($$$cl275.Float(12.24744871391589)),$$$cl275.String("power",5));
}

//ClassDefinition OpTest1 at operators.ceylon (92:0-92:17)
function OpTest1($$opTest1){
    if ($$opTest1===undefined)$$opTest1=new OpTest1.$$;
    return $$opTest1;
}
$$$cl275.initTypeProto(OpTest1,'operators.OpTest1',$$$cl275.IdentifiableObject);

//MethodDefinition testBooleanOperators at operators.ceylon (94:0-125:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (95:4-95:24)
    var o1$291=OpTest1();
    
    //AttributeDeclaration o2 at operators.ceylon (96:4-96:24)
    var o2$292=OpTest1();
    
    //AttributeDeclaration b1 at operators.ceylon (97:4-97:36)
    var b1$293=(o1$291===o2$292?$$$cl275.getTrue():$$$cl275.getFalse());
    var setB1$293=function(b1$294){b1$293=b1$294; return b1$293;};
    assert(b1$293.equals($$$cl275.getFalse()),$$$cl275.String("identity",8));
    
    //AttributeDeclaration b2 at operators.ceylon (99:4-99:36)
    var b2$295=(o1$291===o1$291?$$$cl275.getTrue():$$$cl275.getFalse());
    var setB2$295=function(b2$296){b2$295=b2$296; return b2$295;};
    assert(b2$295,$$$cl275.String("identity",8));
    setB1$293(o1$291.equals(o2$292));
    assert(b1$293.equals($$$cl275.getFalse()),$$$cl275.String("equals",6));
    setB2$295(o1$291.equals(o1$291));
    assert(b2$295,$$$cl275.String("equals",6));
    setB1$293($$$cl275.Integer(1).equals($$$cl275.Integer(2)));
    assert(b1$293.equals($$$cl275.getFalse()),$$$cl275.String("equals",6));
    setB2$295($$$cl275.Integer(1).equals($$$cl275.Integer(2)).equals($$$cl275.getFalse()));
    assert(b2$295,$$$cl275.String("not equal",9));
    
    //AttributeDeclaration b3 at operators.ceylon (110:4-110:30)
    var b3$297=b2$295.equals($$$cl275.getFalse());
    var setB3$297=function(b3$298){b3$297=b3$298; return b3$297;};
    assert(b3$297.equals($$$cl275.getFalse()),$$$cl275.String("not",3));
    setB1$293(($$$cl275.getTrue()===$$$cl275.getTrue()?$$$cl275.getFalse():$$$cl275.getFalse()));
    assert(b1$293.equals($$$cl275.getFalse()),$$$cl275.String("and",3));
    setB2$295((b1$293===$$$cl275.getTrue()?$$$cl275.getTrue():$$$cl275.getFalse()));
    assert(b2$295.equals($$$cl275.getFalse()),$$$cl275.String("and",3));
    setB3$297(($$$cl275.getTrue()===$$$cl275.getTrue()?$$$cl275.getTrue():$$$cl275.getFalse()));
    assert(b3$297,$$$cl275.String("and",3));
    setB1$293(($$$cl275.getTrue()===$$$cl275.getTrue()?$$$cl275.getTrue():$$$cl275.getFalse()));
    assert(b1$293,$$$cl275.String("or",2));
    setB2$295(($$$cl275.getFalse()===$$$cl275.getTrue()?$$$cl275.getTrue():b1$293));
    assert(b2$295,$$$cl275.String("or",2));
    setB3$297(($$$cl275.getFalse()===$$$cl275.getTrue()?$$$cl275.getTrue():$$$cl275.getFalse()));
    assert(b3$297.equals($$$cl275.getFalse()),$$$cl275.String("or",2));
}

//MethodDefinition testComparisonOperators at operators.ceylon (127:0-157:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (128:4-128:37)
    var c1$299=$$$cl275.String("str1",4).compare($$$cl275.String("str2",4));
    assert(c1$299.equals($$$cl275.getSmaller()),$$$cl275.String("compare",7));
    
    //AttributeDeclaration c2 at operators.ceylon (130:4-130:37)
    var c2$300=$$$cl275.String("str2",4).compare($$$cl275.String("str1",4));
    assert(c2$300.equals($$$cl275.getLarger()),$$$cl275.String("compare",7));
    
    //AttributeDeclaration c3 at operators.ceylon (132:4-132:37)
    var c3$301=$$$cl275.String("str1",4).compare($$$cl275.String("str1",4));
    assert(c3$301.equals($$$cl275.getEqual()),$$$cl275.String("compare",7));
    
    //AttributeDeclaration c4 at operators.ceylon (134:4-134:29)
    var c4$302=$$$cl275.String("",0).compare($$$cl275.String("",0));
    assert(c4$302.equals($$$cl275.getEqual()),$$$cl275.String("compare",7));
    
    //AttributeDeclaration c5 at operators.ceylon (136:4-136:33)
    var c5$303=$$$cl275.String("str1",4).compare($$$cl275.String("",0));
    assert(c5$303.equals($$$cl275.getLarger()),$$$cl275.String("compare",7));
    
    //AttributeDeclaration c6 at operators.ceylon (138:4-138:33)
    var c6$304=$$$cl275.String("",0).compare($$$cl275.String("str2",4));
    assert(c6$304.equals($$$cl275.getSmaller()),$$$cl275.String("compare",7));
    
    //AttributeDeclaration b1 at operators.ceylon (141:4-141:42)
    var b1$305=$$$cl275.String("str1",4).compare($$$cl275.String("str2",4)).equals($$$cl275.getSmaller());
    var setB1$305=function(b1$306){b1$305=b1$306; return b1$305;};
    assert(b1$305,$$$cl275.String("smaller",7));
    
    //AttributeDeclaration b2 at operators.ceylon (143:4-143:42)
    var b2$307=$$$cl275.String("str1",4).compare($$$cl275.String("str2",4)).equals($$$cl275.getLarger());
    var setB2$307=function(b2$308){b2$307=b2$308; return b2$307;};
    assert(b2$307.equals($$$cl275.getFalse()),$$$cl275.String("larger",6));
    
    //AttributeDeclaration b3 at operators.ceylon (145:4-145:43)
    var b3$309=($$$cl275.String("str1",4).compare($$$cl275.String("str2",4))!==$$$cl275.getLarger()?$$$cl275.getTrue():$$$cl275.getFalse());
    var setB3$309=function(b3$310){b3$309=b3$310; return b3$309;};
    assert(b3$309,$$$cl275.String("small as",8));
    
    //AttributeDeclaration b4 at operators.ceylon (147:4-147:43)
    var b4$311=($$$cl275.String("str1",4).compare($$$cl275.String("str2",4))!==$$$cl275.getSmaller()?$$$cl275.getTrue():$$$cl275.getFalse());
    var setB4$311=function(b4$312){b4$311=b4$312; return b4$311;};
    assert(b4$311.equals($$$cl275.getFalse()),$$$cl275.String("large as",8));
    setB1$305($$$cl275.String("str1",4).compare($$$cl275.String("str1",4)).equals($$$cl275.getSmaller()));
    assert(b1$305.equals($$$cl275.getFalse()),$$$cl275.String("smaller",7));
    setB2$307($$$cl275.String("str1",4).compare($$$cl275.String("str1",4)).equals($$$cl275.getLarger()));
    assert(b2$307.equals($$$cl275.getFalse()),$$$cl275.String("larger",6));
    setB3$309(($$$cl275.String("str1",4).compare($$$cl275.String("str1",4))!==$$$cl275.getLarger()?$$$cl275.getTrue():$$$cl275.getFalse()));
    assert(b3$309,$$$cl275.String("small as",8));
    setB4$311(($$$cl275.String("str1",4).compare($$$cl275.String("str1",4))!==$$$cl275.getSmaller()?$$$cl275.getTrue():$$$cl275.getFalse()));
    assert(b4$311,$$$cl275.String("large as",8));
}

//MethodDefinition testOtherOperators at operators.ceylon (159:0-171:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (160:4-160:42)
    var entry$313=$$$cl275.Entry($$$cl275.Integer(47),$$$cl275.String("hi there",8));
    assert(entry$313.getKey().equals($$$cl275.Integer(47)),$$$cl275.String("entry key",9));
    assert(entry$313.getItem().equals($$$cl275.String("hi there",8)),$$$cl275.String("entry item",10));
    
    //AttributeDeclaration entry2 at operators.ceylon (163:4-163:30)
    var entry2$314=$$$cl275.Entry($$$cl275.getTrue(),entry$313);
    assert(entry2$314.getKey().equals($$$cl275.getTrue()),$$$cl275.String("entry key",9));
    assert(entry2$314.getItem().equals($$$cl275.Entry($$$cl275.Integer(47),$$$cl275.String("hi there",8))),$$$cl275.String("entry item",10));
    
    //AttributeDeclaration s1 at operators.ceylon (167:4-167:41)
    var s1$315=function($){return $!==null?$:$$$cl275.String("noo",3)}(($$$cl275.getTrue()===$$$cl275.getTrue()?$$$cl275.String("ok",2):null));
    assert(s1$315.equals($$$cl275.String("ok",2)),$$$cl275.String("then/else 1",11));
    
    //AttributeDeclaration s2 at operators.ceylon (169:4-169:47)
    var s2$316=function($){return $!==null?$:$$$cl275.String("great",5)}(($$$cl275.getFalse()===$$$cl275.getTrue()?$$$cl275.String("what?",5):null));
    assert(s2$316.equals($$$cl275.String("great",5)),$$$cl275.String("then/else 2",11));
}

//MethodDefinition testCollectionOperators at operators.ceylon (173:0-185:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (174:4-174:33)
    var seq1$317=$$$cl275.ArraySequence([$$$cl275.String("one",3),$$$cl275.String("two",3)]);
    
    //AttributeDeclaration s1 at operators.ceylon (175:4-175:30)
    var s1$318=function($){return $!==null?$:$$$cl275.String("null",4)}(seq1$317.item($$$cl275.Integer(0)));
    assert(s1$318.equals($$$cl275.String("one",3)),$$$cl275.String("lookup",6));
    
    //AttributeDeclaration s2 at operators.ceylon (177:4-177:30)
    var s2$319=function($){return $!==null?$:$$$cl275.String("null",4)}(seq1$317.item($$$cl275.Integer(2)));
    assert(s2$319.equals($$$cl275.String("null",4)),$$$cl275.String("lookup",6));
    
    //AttributeDeclaration s3 at operators.ceylon (179:4-179:31)
    var s3$320=function($){return $!==null?$:$$$cl275.String("null",4)}(seq1$317.item($$$cl275.Integer(1).getNegativeValue()));
    assert(s3$320.equals($$$cl275.String("null",4)),$$$cl275.String("lookup",6));
    
    //AttributeDeclaration unsafe at operators.ceylon (181:4-181:45)
    var unsafe$321=seq1$317;
    var setUnsafe$321=function(unsafe$322){unsafe$321=unsafe$322; return unsafe$321;};
    assert($$$cl275.exists($$$cl275.exists(unsafe$321)===$$$cl275.getTrue()?unsafe$321.item($$$cl275.Integer(0)):$$$cl275.getNull()),$$$cl275.String("safe index",10));
    setUnsafe$321($$$cl275.getNull());
    assert($$$cl275.exists($$$cl275.exists(unsafe$321)===$$$cl275.getTrue()?unsafe$321.item($$$cl275.Integer(0)):$$$cl275.getNull()).equals($$$cl275.getFalse()),$$$cl275.String("safe index",10));
}

//ClassDefinition NullsafeTest at operators.ceylon (187:0-192:0)
function NullsafeTest($$nullsafeTest){
    if ($$nullsafeTest===undefined)$$nullsafeTest=new NullsafeTest.$$;
    return $$nullsafeTest;
}
$$$cl275.initTypeProto(NullsafeTest,'operators.NullsafeTest',$$$cl275.IdentifiableObject);
;(function($proto$){
    
    //MethodDefinition f at operators.ceylon (188:4-188:33)
    $proto$.f=function f(){
        var $$nullsafeTest=this;
        return $$$cl275.Integer(1);
    }
    
    //MethodDefinition f2 at operators.ceylon (189:4-191:4)
    $proto$.f2=function f2(x$323){
        var $$nullsafeTest=this;
        return x$323();
    }
    
})(NullsafeTest.$$.prototype);

//MethodDefinition nullsafeTest at operators.ceylon (194:0-196:0)
function nullsafeTest(f$324){
    return f$324();
}

//MethodDefinition testNullsafeOperators at operators.ceylon (198:0-227:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (199:4-199:27)
    var seq$325=$$$cl275.ArraySequence([$$$cl275.String("hi",2)]);
    
    //AttributeDeclaration s1 at operators.ceylon (200:4-200:29)
    var s1$326=function($){return $!==null?$:$$$cl275.String("null",4)}(seq$325.item($$$cl275.Integer(0)));
    assert(s1$326.equals($$$cl275.String("hi",2)),$$$cl275.String("default 1",9));
    
    //AttributeDeclaration s2 at operators.ceylon (202:4-202:29)
    var s2$327=function($){return $!==null?$:$$$cl275.String("null",4)}(seq$325.item($$$cl275.Integer(1)));
    assert(s2$327.equals($$$cl275.String("null",4)),$$$cl275.String("default 2",9));
    
    //AttributeDeclaration s3 at operators.ceylon (205:4-205:21)
    var s3$328=$$$cl275.getNull();
    
    //AttributeDeclaration s4 at operators.ceylon (206:4-206:23)
    var s4$329=$$$cl275.String("test",4);
    
    //AttributeDeclaration s5 at operators.ceylon (207:4-207:39)
    var s5$330=function($){return $!==null?$:$$$cl275.String("null",4)}((function($){return $===null?null:$.getUppercased()}(s3$328)));
    
    //AttributeDeclaration s6 at operators.ceylon (208:4-208:39)
    var s6$331=function($){return $!==null?$:$$$cl275.String("null",4)}((function($){return $===null?null:$.getUppercased()}(s4$329)));
    assert(s5$330.equals($$$cl275.String("null",4)),$$$cl275.String("nullsafe member 1",17));
    assert(s6$331.equals($$$cl275.String("TEST",4)),$$$cl275.String("nullsafe member 2",17));
    
    //AttributeDeclaration obj at operators.ceylon (211:4-211:28)
    var obj$332=$$$cl275.getNull();
    
    //AttributeDeclaration i at operators.ceylon (212:4-212:25)
    var i$333=(function(){var tmpvar$334=obj$332; return $$$cl275.JsCallable(tmpvar$334,tmpvar$334===null?null:tmpvar$334.f);}())();
    assert($$$cl275.exists(i$333).equals($$$cl275.getFalse()),$$$cl275.String("nullsafe invoke",15));
    
    //AttributeDeclaration f2 at operators.ceylon (214:4-214:34)
    var f2$335=(function(){var $=obj$332;return $$$cl275.JsCallable($, $.f)})();
    assert($$$cl275.exists(nullsafeTest(f2$335)).equals($$$cl275.getFalse()),$$$cl275.String("nullsafe method ref",19));
    
    //AttributeDeclaration f3 at operators.ceylon (216:4-216:35)
    var f3$336=(function(){var $=obj$332;return $$$cl275.JsCallable($, $.f)})();
    assert($$$cl275.exists(f3$336),$$$cl275.String("nullsafe method ref 2",21));
    (function(){var tmpvar$337=obj$332; return $$$cl275.JsCallable(tmpvar$337,tmpvar$337===null?null:tmpvar$337.f);}())();
    assert($$$cl275.exists((function(){var tmpvar$338=obj$332; return $$$cl275.JsCallable(tmpvar$338,tmpvar$338===null?null:tmpvar$338.f);}())()).equals($$$cl275.getFalse()),$$$cl275.String("nullsafe simple call",20));
    
    //MethodDefinition getNullsafe at operators.ceylon (220:4-220:46)
    function getNullsafe$339(){
        return obj$332;
    }
    
    //MethodDeclaration f4 at operators.ceylon (221:4-221:36)
    var f4$340=(function(){var $=getNullsafe$339();return $$$cl275.JsCallable($, $.f)})();
    
    //AttributeDeclaration result_f4 at operators.ceylon (222:4-222:29)
    var result_f4$341=f4$340();
    assert($$$cl275.exists(result_f4$341).equals($$$cl275.getFalse()),$$$cl275.String("nullsafe invoke 2",17));
    
    //AttributeDeclaration i2 at operators.ceylon (224:4-224:36)
    var i2$342=(function(){var tmpvar$343=getNullsafe$339(); return $$$cl275.JsCallable(tmpvar$343,tmpvar$343===null?null:tmpvar$343.f);}())();
    assert($$$cl275.exists(i2$342).equals($$$cl275.getFalse()),$$$cl275.String("nullsafe invoke 3",17));
    assert($$$cl275.exists(NullsafeTest().f2((function(){var $=getNullsafe$339();return $$$cl275.JsCallable($, $.f)})())).equals($$$cl275.getFalse()),$$$cl275.String("nullsafe method ref 3",21));
}

//MethodDefinition testIncDecOperators at operators.ceylon (229:0-285:0)
function testIncDecOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (230:4-230:28)
    var i1$344=$$$cl275.Integer(1);
    var setI1$344=function(i1$345){i1$344=i1$345; return i1$344;};
    
    //MethodDefinition f1 at operators.ceylon (231:4-235:4)
    function f1$346(){
        
        //AttributeDeclaration i2 at operators.ceylon (232:8-232:25)
        var i2$347=(setI1$344(i1$344.getSuccessor()),i1$344);
        assert(i1$344.equals($$$cl275.Integer(2)),$$$cl275.String("prefix increment",16));
        assert(i2$347.equals($$$cl275.Integer(2)),$$$cl275.String("prefix increment",16));
    }
    f1$346();
    
    //ClassDefinition C1 at operators.ceylon (238:4-238:49)
    function C1$348($$c1$348){
        if ($$c1$348===undefined)$$c1$348=new C1$348.$$;
        
        //AttributeDeclaration i at operators.ceylon (238:17-238:47)
        $$c1$348.i$349$=$$$cl275.Integer(1);
        return $$c1$348;
    }
    $$$cl275.initTypeProto(C1$348,'operators.testIncDecOperators.C1',$$$cl275.IdentifiableObject);
    ;(function($proto$){
        
        //AttributeDeclaration i at operators.ceylon (238:17-238:47)
        $proto$.getI=function getI(){
            return this.i$349$;
        }
        $proto$.setI=function setI(i$350){
            this.i$349$=i$350; return i$350;
        }
        
    })(C1$348.$$.prototype);
    
    //AttributeDeclaration c1 at operators.ceylon (239:4-239:16)
    var c1$351=C1$348();
    
    //AttributeDeclaration i3 at operators.ceylon (240:4-240:28)
    var i3$352=$$$cl275.Integer(0);
    var setI3$352=function(i3$353){i3$352=i3$353; return i3$352;};
    
    //MethodDefinition f2 at operators.ceylon (241:4-244:4)
    function f2$354(){
        (setI3$352(i3$352.getSuccessor()),i3$352);
        return c1$351;
    }
    
    //AttributeDeclaration i4 at operators.ceylon (245:4-245:25)
    var i4$355=function($){var $2=$.getI().getSuccessor();$.setI($2);return $2}(f2$354());
    assert(i4$355.equals($$$cl275.Integer(2)),$$$cl275.String("prefix increment",16));
    assert(c1$351.getI().equals($$$cl275.Integer(2)),$$$cl275.String("prefix increment",16));
    assert(i3$352.equals($$$cl275.Integer(1)),$$$cl275.String("prefix increment",16));
    
    //MethodDefinition f3 at operators.ceylon (250:4-254:4)
    function f3$356(){
        
        //AttributeDeclaration i2 at operators.ceylon (251:8-251:25)
        var i2$357=(setI1$344(i1$344.getPredecessor()),i1$344);
        assert(i1$344.equals($$$cl275.Integer(1)),$$$cl275.String("prefix decrement",16));
        assert(i2$357.equals($$$cl275.Integer(1)),$$$cl275.String("prefix decrement",16));
    }
    f3$356();
    
    //AttributeDeclaration i5 at operators.ceylon (257:4-257:25)
    var i5$358=function($){var $2=$.getI().getPredecessor();$.setI($2);return $2}(f2$354());
    assert(i5$358.equals($$$cl275.Integer(1)),$$$cl275.String("prefix decrement",16));
    assert(c1$351.getI().equals($$$cl275.Integer(1)),$$$cl275.String("prefix decrement",16));
    assert(i3$352.equals($$$cl275.Integer(2)),$$$cl275.String("prefix decrement",16));
    
    //MethodDefinition f4 at operators.ceylon (262:4-266:4)
    function f4$359(){
        
        //AttributeDeclaration i2 at operators.ceylon (263:8-263:25)
        var i2$360=(function($){setI1$344($.getSuccessor());return $}(i1$344));
        assert(i1$344.equals($$$cl275.Integer(2)),$$$cl275.String("postfix increment",17));
        assert(i2$360.equals($$$cl275.Integer(1)),$$$cl275.String("postfix increment",17));
    }
    f4$359();
    
    //AttributeDeclaration i6 at operators.ceylon (269:4-269:25)
    var i6$361=function($){var $2=$.getI();$.setI($2.getSuccessor());return $2}(f2$354());
    assert(i6$361.equals($$$cl275.Integer(1)),$$$cl275.String("postfix increment",17));
    assert(c1$351.getI().equals($$$cl275.Integer(2)),$$$cl275.String("postfix increment",17));
    assert(i3$352.equals($$$cl275.Integer(3)),$$$cl275.String("postfix increment",17));
    
    //MethodDefinition f5 at operators.ceylon (274:4-278:4)
    function f5$362(){
        
        //AttributeDeclaration i2 at operators.ceylon (275:8-275:25)
        var i2$363=(function($){setI1$344($.getPredecessor());return $}(i1$344));
        assert(i1$344.equals($$$cl275.Integer(1)),$$$cl275.String("postfix decrement",17));
        assert(i2$363.equals($$$cl275.Integer(2)),$$$cl275.String("postfix decrement",17));
    }
    f5$362();
    
    //AttributeDeclaration i7 at operators.ceylon (281:4-281:25)
    var i7$364=function($){var $2=$.getI();$.setI($2.getPredecessor());return $2}(f2$354());
    assert(i7$364.equals($$$cl275.Integer(2)),$$$cl275.String("postfix decrement",17));
    assert(c1$351.getI().equals($$$cl275.Integer(1)),$$$cl275.String("postfix decrement",17));
    assert(i3$352.equals($$$cl275.Integer(4)),$$$cl275.String("postfix decrement",17));
}

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (287:0-324:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (288:4-288:28)
    var i1$365=$$$cl275.Integer(1);
    var setI1$365=function(i1$366){i1$365=i1$366; return i1$365;};
    (setI1$365(i1$365.plus($$$cl275.Integer(10))),i1$365);
    assert(i1$365.equals($$$cl275.Integer(11)),$$$cl275.String("+= operator",11));
    
    //AttributeDeclaration i2 at operators.ceylon (292:4-292:37)
    var i2$367=(setI1$365(i1$365.plus($$$cl275.Integer(5).getNegativeValue())),i1$365);
    var setI2$367=function(i2$368){i2$367=i2$368; return i2$367;};
    assert(i2$367.equals($$$cl275.Integer(6)),$$$cl275.String("+= operator",11));
    assert(i1$365.equals($$$cl275.Integer(6)),$$$cl275.String("+= operator",11));
    
    //ClassDefinition C1 at operators.ceylon (296:4-296:49)
    function C1$369($$c1$369){
        if ($$c1$369===undefined)$$c1$369=new C1$369.$$;
        
        //AttributeDeclaration i at operators.ceylon (296:17-296:47)
        $$c1$369.i$370$=$$$cl275.Integer(1);
        return $$c1$369;
    }
    $$$cl275.initTypeProto(C1$369,'operators.testArithmeticAssignOperators.C1',$$$cl275.IdentifiableObject);
    ;(function($proto$){
        
        //AttributeDeclaration i at operators.ceylon (296:17-296:47)
        $proto$.getI=function getI(){
            return this.i$370$;
        }
        $proto$.setI=function setI(i$371){
            this.i$370$=i$371; return i$371;
        }
        
    })(C1$369.$$.prototype);
    
    //AttributeDeclaration c1 at operators.ceylon (297:4-297:16)
    var c1$372=C1$369();
    
    //AttributeDeclaration i3 at operators.ceylon (298:4-298:28)
    var i3$373=$$$cl275.Integer(0);
    var setI3$373=function(i3$374){i3$373=i3$374; return i3$373;};
    
    //MethodDefinition f at operators.ceylon (299:4-302:4)
    function f$375(){
        (setI3$373(i3$373.getSuccessor()),i3$373);
        return c1$372;
    }
    setI2$367((function($1,$2){var $=$1.getI().plus($2);$1.setI($);return $}(f$375(),$$$cl275.Integer(11))));
    assert(i2$367.equals($$$cl275.Integer(12)),$$$cl275.String("+= operator",11));
    assert(c1$372.getI().equals($$$cl275.Integer(12)),$$$cl275.String("+= operator",11));
    assert(i3$373.equals($$$cl275.Integer(1)),$$$cl275.String("+= operator",11));
    setI2$367((setI1$365(i1$365.minus($$$cl275.Integer(14))),i1$365));
    assert(i1$365.equals($$$cl275.Integer(8).getNegativeValue()),$$$cl275.String("-= operator",11));
    assert(i2$367.equals($$$cl275.Integer(8).getNegativeValue()),$$$cl275.String("-= operator",11));
    setI2$367((setI1$365(i1$365.times($$$cl275.Integer(3).getNegativeValue())),i1$365));
    assert(i1$365.equals($$$cl275.Integer(24)),$$$cl275.String("*= operator",11));
    assert(i2$367.equals($$$cl275.Integer(24)),$$$cl275.String("*= operator",11));
    setI2$367((setI1$365(i1$365.divided($$$cl275.Integer(5))),i1$365));
    assert(i1$365.equals($$$cl275.Integer(4)),$$$cl275.String("/= operator",11));
    assert(i2$367.equals($$$cl275.Integer(4)),$$$cl275.String("/= operator",11));
    setI2$367((setI1$365(i1$365.remainder($$$cl275.Integer(3))),i1$365));
    assert(i1$365.equals($$$cl275.Integer(1)),$$$cl275.String("%= operator",11));
    assert(i2$367.equals($$$cl275.Integer(1)),$$$cl275.String("%= operator",11));
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
