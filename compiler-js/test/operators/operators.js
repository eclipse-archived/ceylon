var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition expect at operators.ceylon (1:0-8:0)
function expect(actual,expected,text){
    if ((actual.equals(expected))===$$$cl15.getTrue()){
        $$$cl15.print($$$cl15.String("[ok] ").plus(text).plus($$$cl15.String(": '")).plus(actual.getString()).plus($$$cl15.String("'")));
    }
    else {
        $$$cl15.print($$$cl15.String("[NOT OK] ").plus(text).plus($$$cl15.String(": actual='")).plus(actual.getString()).plus($$$cl15.String("', expected='")).plus(expected.getString()));
    }
    
}

//MethodDefinition testIntegerOperators at operators.ceylon (10:0-47:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (12:4-12:29)
    var $i1=$$$cl15.Integer(4).negativeValue();
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1;
    }
    setI1(getI1().negativeValue());
    expect(getI1(),$$$cl15.Integer(4),$$$cl15.String("negation"));
    setI1($$$cl15.Integer(987654).negativeValue().positiveValue());
    expect(getI1(),$$$cl15.Integer(987654).negativeValue(),$$$cl15.String("positive"));
    setI1($$$cl15.Integer(0).positiveValue());
    expect(getI1(),$$$cl15.Integer(0),$$$cl15.String("+0=0"));
    setI1($$$cl15.Integer(0).negativeValue());
    expect(getI1(),$$$cl15.Integer(0),$$$cl15.String("+0=0"));
    
    //AttributeDeclaration i2 at operators.ceylon (22:4-22:36)
    var $i2=$$$cl15.Integer(123).plus($$$cl15.Integer(456));
    function getI2(){
        return $i2;
    }
    function setI2(i2){
        $i2=i2;
    }
    expect(getI2(),$$$cl15.Integer(579),$$$cl15.String("addition"));
    setI1(getI2().minus($$$cl15.Integer(16)));
    expect(getI1(),$$$cl15.Integer(563),$$$cl15.String("subtraction"));
    setI2(getI1().negativeValue().plus(getI2()).minus($$$cl15.Integer(1)));
    expect(getI2(),$$$cl15.Integer(15),$$$cl15.String("-i1+i2-1"));
    setI1($$$cl15.Integer(3).times($$$cl15.Integer(7)));
    expect(getI1(),$$$cl15.Integer(21),$$$cl15.String("multiplication"));
    setI2(getI1().times($$$cl15.Integer(2)));
    expect(getI2(),$$$cl15.Integer(42),$$$cl15.String("multiplication"));
    setI2($$$cl15.Integer(17).divided($$$cl15.Integer(4)));
    expect(getI2(),$$$cl15.Integer(4),$$$cl15.String("integer division"));
    setI1(getI2().times($$$cl15.Integer(516)).divided(getI1().negativeValue()));
    expect(getI1(),$$$cl15.Integer(98).negativeValue(),$$$cl15.String("i2*516/-i1"));
    setI1($$$cl15.Integer(15).remainder($$$cl15.Integer(4)));
    expect(getI1(),$$$cl15.Integer(3),$$$cl15.String("modulo"));
    setI2($$$cl15.Integer(312).remainder($$$cl15.Integer(12)));
    expect(getI2(),$$$cl15.Integer(0),$$$cl15.String("modulo"));
    setI1($$$cl15.Integer(2).power($$$cl15.Integer(10)));
    expect(getI1(),$$$cl15.Integer(1024),$$$cl15.String("power"));
    setI2($$$cl15.Integer(100).power($$$cl15.Integer(6)));
    expect(getI2(),$$$cl15.Integer(1000000000000),$$$cl15.String("power"));
}

//MethodDefinition testFloatOperators at operators.ceylon (49:0-79:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (51:4-51:29)
    var $f1=$$$cl15.Float(4.2).negativeValue();
    function getF1(){
        return $f1;
    }
    function setF1(f1){
        $f1=f1;
    }
    setF1(getF1().negativeValue());
    expect(getF1(),$$$cl15.Float(4.2),$$$cl15.String("negation"));
    setF1($$$cl15.Float(987654.9925567).negativeValue().positiveValue());
    expect(getF1(),$$$cl15.Float(987654.9925567).negativeValue(),$$$cl15.String("positive"));
    setF1($$$cl15.Float(0.0).positiveValue());
    expect(getF1(),$$$cl15.Float(0.0),$$$cl15.String("+0.0=0.0"));
    setF1($$$cl15.Float(0.0).negativeValue());
    expect(getF1(),$$$cl15.Float(0.0),$$$cl15.String("-0.0=0.0"));
    
    //AttributeDeclaration f2 at operators.ceylon (61:4-61:43)
    var $f2=$$$cl15.Float(3.14159265).plus($$$cl15.Float(456.0));
    function getF2(){
        return $f2;
    }
    function setF2(f2){
        $f2=f2;
    }
    expect(getF2(),$$$cl15.Float(459.14159265),$$$cl15.String("addition"));
    setF1(getF2().minus($$$cl15.Float(0.0016)));
    expect(getF1(),$$$cl15.Float(459.13999265),$$$cl15.String("subtraction"));
    setF2(getF1().negativeValue().plus(getF2()).minus($$$cl15.Float(1.2)));
    expect(getF2(),$$$cl15.Float(1.1984000000000037).negativeValue(),$$$cl15.String("-f1+f2-1.2"));
    setF1($$$cl15.Float(3.0).times($$$cl15.Float(0.79)));
    expect(getF1(),$$$cl15.Float(2.37),$$$cl15.String("multiplication"));
    setF2(getF1().times($$$cl15.Float(2.0e13)));
    expect(getF2(),$$$cl15.Float(47400000000000.0),$$$cl15.String("multiplication"));
    setF2($$$cl15.Float(17.1).divided($$$cl15.Float(4.0E-18)));
    expect(getF2(),$$$cl15.Float(4275000000000000000.0),$$$cl15.String("division"));
    setF1(getF2().times($$$cl15.Float(51.6e2)).divided(getF1().negativeValue()));
    expect(getF2(),$$$cl15.Float(4275000000000000000.0),$$$cl15.String("f2*51.6e2/-f1"));
    setF1($$$cl15.Float(150.0).power($$$cl15.Float(0.5)));
    expect(getF1(),$$$cl15.Float(12.24744871391589),$$$cl15.String("power"));
}

//ClassDefinition OpTest1 at operators.ceylon (81:0-81:17)
function OpTest1($$opTest1){
    if ($$opTest1===undefined)$$opTest1=new CeylonObject;
    return $$opTest1;
}

//MethodDefinition testBooleanOperators at operators.ceylon (83:0-114:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (84:4-84:24)
    var $o1=OpTest1();
    function getO1(){
        return $o1;
    }
    
    //AttributeDeclaration o2 at operators.ceylon (85:4-85:24)
    var $o2=OpTest1();
    function getO2(){
        return $o2;
    }
    
    //AttributeDeclaration b1 at operators.ceylon (86:4-86:36)
    var $b1=(getO1()===getO2()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB1(){
        return $b1;
    }
    function setB1(b1){
        $b1=b1;
    }
    expect(getB1(),$$$cl15.getFalse(),$$$cl15.String("identity"));
    
    //AttributeDeclaration b2 at operators.ceylon (88:4-88:36)
    var $b2=(getO1()===getO1()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB2(){
        return $b2;
    }
    function setB2(b2){
        $b2=b2;
    }
    expect(getB2(),$$$cl15.getTrue(),$$$cl15.String("identity"));
    setB1(getO1().equals(getO2()));
    expect(getB1(),$$$cl15.getFalse(),$$$cl15.String("equals"));
    setB2(getO1().equals(getO1()));
    expect(getB2(),$$$cl15.getTrue(),$$$cl15.String("equals"));
    setB1($$$cl15.Integer(1).equals($$$cl15.Integer(2)));
    expect(getB1(),$$$cl15.getFalse(),$$$cl15.String("equals"));
    setB2($$$cl15.Integer(1).equals($$$cl15.Integer(2)).equals($$$cl15.getFalse()));
    expect(getB2(),$$$cl15.getTrue(),$$$cl15.String("not equal"));
    
    //AttributeDeclaration b3 at operators.ceylon (99:4-99:30)
    var $b3=getB2().equals($$$cl15.getFalse());
    function getB3(){
        return $b3;
    }
    function setB3(b3){
        $b3=b3;
    }
    expect(getB3(),$$$cl15.getFalse(),$$$cl15.String("not"));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getFalse():$$$cl15.getFalse()));
    expect(getB1(),$$$cl15.getFalse(),$$$cl15.String("and"));
    setB2((getB1()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    expect(getB2(),$$$cl15.getFalse(),$$$cl15.String("and"));
    setB3(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    expect(getB3(),$$$cl15.getTrue(),$$$cl15.String("and"));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    expect(getB1(),$$$cl15.getTrue(),$$$cl15.String("or"));
    setB2(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():getB1()));
    expect(getB2(),$$$cl15.getTrue(),$$$cl15.String("or"));
    setB3(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    expect(getB3(),$$$cl15.getFalse(),$$$cl15.String("or"));
}

//MethodDefinition testComparisonOperators at operators.ceylon (116:0-146:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (117:4-117:37)
    var $c1=$$$cl15.String("str1").compare($$$cl15.String("str2"));
    function getC1(){
        return $c1;
    }
    expect(getC1(),$$$cl15.getSmaller(),$$$cl15.String("compare"));
    
    //AttributeDeclaration c2 at operators.ceylon (119:4-119:37)
    var $c2=$$$cl15.String("str2").compare($$$cl15.String("str1"));
    function getC2(){
        return $c2;
    }
    expect(getC2(),$$$cl15.getLarger(),$$$cl15.String("compare"));
    
    //AttributeDeclaration c3 at operators.ceylon (121:4-121:37)
    var $c3=$$$cl15.String("str1").compare($$$cl15.String("str1"));
    function getC3(){
        return $c3;
    }
    expect(getC3(),$$$cl15.getEqual(),$$$cl15.String("compare"));
    
    //AttributeDeclaration c4 at operators.ceylon (123:4-123:29)
    var $c4=$$$cl15.String("").compare($$$cl15.String(""));
    function getC4(){
        return $c4;
    }
    expect(getC4(),$$$cl15.getEqual(),$$$cl15.String("compare"));
    
    //AttributeDeclaration c5 at operators.ceylon (125:4-125:33)
    var $c5=$$$cl15.String("str1").compare($$$cl15.String(""));
    function getC5(){
        return $c5;
    }
    expect(getC5(),$$$cl15.getLarger(),$$$cl15.String("compare"));
    
    //AttributeDeclaration c6 at operators.ceylon (127:4-127:33)
    var $c6=$$$cl15.String("").compare($$$cl15.String("str2"));
    function getC6(){
        return $c6;
    }
    expect(getC6(),$$$cl15.getSmaller(),$$$cl15.String("compare"));
    
    //AttributeDeclaration b1 at operators.ceylon (130:4-130:42)
    var $b1=$$$cl15.String("str1").compare($$$cl15.String("str2")).equals($$$cl15.getSmaller());
    function getB1(){
        return $b1;
    }
    function setB1(b1){
        $b1=b1;
    }
    expect(getB1(),$$$cl15.getTrue(),$$$cl15.String("smaller"));
    
    //AttributeDeclaration b2 at operators.ceylon (132:4-132:42)
    var $b2=$$$cl15.String("str1").compare($$$cl15.String("str2")).equals($$$cl15.getLarger());
    function getB2(){
        return $b2;
    }
    function setB2(b2){
        $b2=b2;
    }
    expect(getB2(),$$$cl15.getFalse(),$$$cl15.String("larger"));
    
    //AttributeDeclaration b3 at operators.ceylon (134:4-134:43)
    var $b3=($$$cl15.String("str1").compare($$$cl15.String("str2"))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB3(){
        return $b3;
    }
    function setB3(b3){
        $b3=b3;
    }
    expect(getB3(),$$$cl15.getTrue(),$$$cl15.String("small as"));
    
    //AttributeDeclaration b4 at operators.ceylon (136:4-136:43)
    var $b4=($$$cl15.String("str1").compare($$$cl15.String("str2"))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB4(){
        return $b4;
    }
    function setB4(b4){
        $b4=b4;
    }
    expect(getB4(),$$$cl15.getFalse(),$$$cl15.String("large as"));
    setB1($$$cl15.String("str1").compare($$$cl15.String("str1")).equals($$$cl15.getSmaller()));
    expect(getB1(),$$$cl15.getFalse(),$$$cl15.String("smaller"));
    setB2($$$cl15.String("str1").compare($$$cl15.String("str1")).equals($$$cl15.getLarger()));
    expect(getB2(),$$$cl15.getFalse(),$$$cl15.String("larger"));
    setB3(($$$cl15.String("str1").compare($$$cl15.String("str1"))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse()));
    expect(getB3(),$$$cl15.getTrue(),$$$cl15.String("small as"));
    setB4(($$$cl15.String("str1").compare($$$cl15.String("str1"))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse()));
    expect(getB4(),$$$cl15.getTrue(),$$$cl15.String("large as"));
}

//MethodDefinition testOtherOperators at operators.ceylon (148:0-160:0)
function testOtherOperators(){
    
    //AttributeDeclaration entry at operators.ceylon (149:4-149:42)
    var $entry=$$$cl15.Entry($$$cl15.Integer(47),$$$cl15.String("hi there"));
    function getEntry(){
        return $entry;
    }
    expect(getEntry().getKey(),$$$cl15.Integer(47),$$$cl15.String("entry key"));
    expect(getEntry().getItem(),$$$cl15.String("hi there"),$$$cl15.String("entry item"));
    
    //AttributeDeclaration entry2 at operators.ceylon (152:4-152:30)
    var $entry2=$$$cl15.Entry($$$cl15.getTrue(),getEntry());
    function getEntry2(){
        return $entry2;
    }
    expect(getEntry2().getKey(),$$$cl15.getTrue(),$$$cl15.String("entry key"));
    expect(getEntry2().getItem(),$$$cl15.Entry($$$cl15.Integer(47),$$$cl15.String("hi there")),$$$cl15.String("entry item"));
    
    //AttributeDeclaration s1 at operators.ceylon (156:4-156:41)
    var $s1=function($){return $!==null?$:$$$cl15.String("noo")}(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.String("ok"):null));
    function getS1(){
        return $s1;
    }
    expect(getS1(),$$$cl15.String("ok"),$$$cl15.String("then/else"));
    
    //AttributeDeclaration s2 at operators.ceylon (158:4-158:47)
    var $s2=function($){return $!==null?$:$$$cl15.String("great")}(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.String("what?"):null));
    function getS2(){
        return $s2;
    }
    expect(getS2(),$$$cl15.String("great"),$$$cl15.String("then/else"));
}

//MethodDefinition testCollectionOperators at operators.ceylon (162:0-170:0)
function testCollectionOperators(){
    
    //AttributeDeclaration seq1 at operators.ceylon (163:4-163:33)
    var $seq1=$$$cl15.ArraySequence([$$$cl15.String("one"),$$$cl15.String("two")]);
    function getSeq1(){
        return $seq1;
    }
    
    //AttributeDeclaration s1 at operators.ceylon (164:4-164:30)
    var $s1=function($){return $!==null?$:$$$cl15.String("null")}(getSeq1().item($$$cl15.Integer(0)));
    function getS1(){
        return $s1;
    }
    expect(getS1(),$$$cl15.String("one"),$$$cl15.String("lookup"));
    
    //AttributeDeclaration s2 at operators.ceylon (166:4-166:30)
    var $s2=function($){return $!==null?$:$$$cl15.String("null")}(getSeq1().item($$$cl15.Integer(2)));
    function getS2(){
        return $s2;
    }
    expect(getS2(),$$$cl15.String("null"),$$$cl15.String("lookup"));
    
    //AttributeDeclaration s3 at operators.ceylon (168:4-168:31)
    var $s3=function($){return $!==null?$:$$$cl15.String("null")}(getSeq1().item($$$cl15.Integer(1).negativeValue()));
    function getS3(){
        return $s3;
    }
    expect(getS3(),$$$cl15.String("null"),$$$cl15.String("lookup"));
}

//MethodDefinition testNullsafeOperators at operators.ceylon (172:0-178:0)
function testNullsafeOperators(){
    
    //AttributeDeclaration seq at operators.ceylon (173:4-173:27)
    var $seq=$$$cl15.ArraySequence([$$$cl15.String("hi")]);
    function getSeq(){
        return $seq;
    }
    
    //AttributeDeclaration s1 at operators.ceylon (174:4-174:29)
    var $s1=function($){return $!==null?$:$$$cl15.String("null")}(getSeq().item($$$cl15.Integer(0)));
    function getS1(){
        return $s1;
    }
    expect(getS1(),$$$cl15.String("hi"),$$$cl15.String("default"));
    
    //AttributeDeclaration s2 at operators.ceylon (176:4-176:29)
    var $s2=function($){return $!==null?$:$$$cl15.String("null")}(getSeq().item($$$cl15.Integer(1)));
    function getS2(){
        return $s2;
    }
    expect(getS2(),$$$cl15.String("null"),$$$cl15.String("default"));
}

//MethodDefinition testIncDecOperators at operators.ceylon (180:0-236:0)
function testIncDecOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (181:4-181:28)
    var $i1=$$$cl15.Integer(1);
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1;
    }
    
    //MethodDefinition f1 at operators.ceylon (182:4-186:4)
    function f1(){
        
        //AttributeDeclaration i2 at operators.ceylon (183:8-183:25)
        var $i2=(setI1(getI1().getSuccessor()),getI1());
        function getI2(){
            return $i2;
        }
        expect(getI1(),$$$cl15.Integer(2),$$$cl15.String("prefix increment"));
        expect(getI2(),$$$cl15.Integer(2),$$$cl15.String("prefix increment"));
    }
    f1();
    
    //ClassDefinition C1 at operators.ceylon (189:4-189:49)
    function C1($$c1){
        if ($$c1===undefined)$$c1=new CeylonObject;
        
        //AttributeDeclaration i at operators.ceylon (189:17-189:47)
        var $i=$$$cl15.Integer(1);
        function getI(){
            return $i;
        }
        $$c1.getI=getI;
        function setI(i){
            $i=i;
        }
        $$c1.setI=setI;
        return $$c1;
    }
    
    //AttributeDeclaration c1 at operators.ceylon (190:4-190:16)
    var $c1=C1();
    function getC1(){
        return $c1;
    }
    
    //AttributeDeclaration i3 at operators.ceylon (191:4-191:28)
    var $i3=$$$cl15.Integer(0);
    function getI3(){
        return $i3;
    }
    function setI3(i3){
        $i3=i3;
    }
    
    //MethodDefinition f2 at operators.ceylon (192:4-195:4)
    function f2(){
        (setI3(getI3().getSuccessor()),getI3());
        return getC1();
    }
    
    //AttributeDeclaration i4 at operators.ceylon (196:4-196:25)
    var $i4=function($){var $2=$.getI().getSuccessor();$.setI($2);return $2}(f2());
    function getI4(){
        return $i4;
    }
    expect(getI4(),$$$cl15.Integer(2),$$$cl15.String("prefix increment"));
    expect(getC1().getI(),$$$cl15.Integer(2),$$$cl15.String("prefix increment"));
    expect(getI3(),$$$cl15.Integer(1),$$$cl15.String("prefix increment"));
    
    //MethodDefinition f3 at operators.ceylon (201:4-205:4)
    function f3(){
        
        //AttributeDeclaration i2 at operators.ceylon (202:8-202:25)
        var $i2=(setI1(getI1().getPredecessor()),getI1());
        function getI2(){
            return $i2;
        }
        expect(getI1(),$$$cl15.Integer(1),$$$cl15.String("prefix decrement"));
        expect(getI2(),$$$cl15.Integer(1),$$$cl15.String("prefix decrement"));
    }
    f3();
    
    //AttributeDeclaration i5 at operators.ceylon (208:4-208:25)
    var $i5=function($){var $2=$.getI().getPredecessor();$.setI($2);return $2}(f2());
    function getI5(){
        return $i5;
    }
    expect(getI5(),$$$cl15.Integer(1),$$$cl15.String("prefix decrement"));
    expect(getC1().getI(),$$$cl15.Integer(1),$$$cl15.String("prefix decrement"));
    expect(getI3(),$$$cl15.Integer(2),$$$cl15.String("prefix decrement"));
    
    //MethodDefinition f4 at operators.ceylon (213:4-217:4)
    function f4(){
        
        //AttributeDeclaration i2 at operators.ceylon (214:8-214:25)
        var $i2=function($){setI1($.getSuccessor());return $}(getI1());
        function getI2(){
            return $i2;
        }
        expect(getI1(),$$$cl15.Integer(2),$$$cl15.String("postfix increment"));
        expect(getI2(),$$$cl15.Integer(1),$$$cl15.String("postfix increment"));
    }
    f4();
    
    //AttributeDeclaration i6 at operators.ceylon (220:4-220:25)
    var $i6=function($){var $2=$.getI();$.setI($2.getSuccessor());return $2}(f2());
    function getI6(){
        return $i6;
    }
    expect(getI6(),$$$cl15.Integer(1),$$$cl15.String("postfix increment"));
    expect(getC1().getI(),$$$cl15.Integer(2),$$$cl15.String("postfix increment"));
    expect(getI3(),$$$cl15.Integer(3),$$$cl15.String("postfix increment"));
    
    //MethodDefinition f5 at operators.ceylon (225:4-229:4)
    function f5(){
        
        //AttributeDeclaration i2 at operators.ceylon (226:8-226:25)
        var $i2=function($){setI1($.getPredecessor());return $}(getI1());
        function getI2(){
            return $i2;
        }
        expect(getI1(),$$$cl15.Integer(1),$$$cl15.String("postfix decrement"));
        expect(getI2(),$$$cl15.Integer(2),$$$cl15.String("postfix decrement"));
    }
    f5();
    
    //AttributeDeclaration i7 at operators.ceylon (232:4-232:25)
    var $i7=function($){var $2=$.getI();$.setI($2.getPredecessor());return $2}(f2());
    function getI7(){
        return $i7;
    }
    expect(getI7(),$$$cl15.Integer(2),$$$cl15.String("postfix decrement"));
    expect(getC1().getI(),$$$cl15.Integer(1),$$$cl15.String("postfix decrement"));
    expect(getI3(),$$$cl15.Integer(4),$$$cl15.String("postfix decrement"));
}

//MethodDefinition testArithmeticAssignOperators at operators.ceylon (238:0-275:0)
function testArithmeticAssignOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (239:4-239:28)
    var $i1=$$$cl15.Integer(1);
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1;
    }
    (setI1(getI1().plus($$$cl15.Integer(10))),getI1());
    expect(getI1(),$$$cl15.Integer(11),$$$cl15.String("+= operator"));
    
    //AttributeDeclaration i2 at operators.ceylon (243:4-243:37)
    var $i2=(setI1(getI1().plus($$$cl15.Integer(5).negativeValue())),getI1());
    function getI2(){
        return $i2;
    }
    function setI2(i2){
        $i2=i2;
    }
    expect(getI2(),$$$cl15.Integer(6),$$$cl15.String("+= operator"));
    expect(getI1(),$$$cl15.Integer(6),$$$cl15.String("+= operator"));
    
    //ClassDefinition C1 at operators.ceylon (247:4-247:49)
    function C1($$c1){
        if ($$c1===undefined)$$c1=new CeylonObject;
        
        //AttributeDeclaration i at operators.ceylon (247:17-247:47)
        var $i=$$$cl15.Integer(1);
        function getI(){
            return $i;
        }
        $$c1.getI=getI;
        function setI(i){
            $i=i;
        }
        $$c1.setI=setI;
        return $$c1;
    }
    
    //AttributeDeclaration c1 at operators.ceylon (248:4-248:16)
    var $c1=C1();
    function getC1(){
        return $c1;
    }
    
    //AttributeDeclaration i3 at operators.ceylon (249:4-249:28)
    var $i3=$$$cl15.Integer(0);
    function getI3(){
        return $i3;
    }
    function setI3(i3){
        $i3=i3;
    }
    
    //MethodDefinition f at operators.ceylon (250:4-253:4)
    function f(){
        (setI3(getI3().getSuccessor()),getI3());
        return getC1();
    }
    setI2(function($1,$2){var $=$1.getI().plus($2);$1.setI($);return $}(f(),$$$cl15.Integer(11)));
    expect(getI2(),$$$cl15.Integer(12),$$$cl15.String("+= operator"));
    expect(getC1().getI(),$$$cl15.Integer(12),$$$cl15.String("+= operator"));
    expect(getI3(),$$$cl15.Integer(1),$$$cl15.String("+= operator"));
    setI2((setI1(getI1().minus($$$cl15.Integer(14))),getI1()));
    expect(getI1(),$$$cl15.Integer(8).negativeValue(),$$$cl15.String("-= operator"));
    expect(getI2(),$$$cl15.Integer(8).negativeValue(),$$$cl15.String("-= operator"));
    setI2((setI1(getI1().times($$$cl15.Integer(3).negativeValue())),getI1()));
    expect(getI1(),$$$cl15.Integer(24),$$$cl15.String("*= operator"));
    expect(getI2(),$$$cl15.Integer(24),$$$cl15.String("*= operator"));
    setI2((setI1(getI1().divided($$$cl15.Integer(5))),getI1()));
    expect(getI1(),$$$cl15.Integer(4),$$$cl15.String("/= operator"));
    expect(getI2(),$$$cl15.Integer(4),$$$cl15.String("/= operator"));
    setI2((setI1(getI1().remainder($$$cl15.Integer(3))),getI1()));
    expect(getI1(),$$$cl15.Integer(1),$$$cl15.String("%= operator"));
    expect(getI2(),$$$cl15.Integer(1),$$$cl15.String("%= operator"));
}

//MethodDefinition test at operators.ceylon (277:0-289:0)
function test(){
    $$$cl15.print($$$cl15.String("--- Start Operator Tests ---"));
    testIntegerOperators();
    testFloatOperators();
    testBooleanOperators();
    testComparisonOperators();
    testOtherOperators();
    testCollectionOperators();
    testNullsafeOperators();
    testIncDecOperators();
    testArithmeticAssignOperators();
    $$$cl15.print($$$cl15.String("--- End Operator Tests ---"));
}
this.test=test;
