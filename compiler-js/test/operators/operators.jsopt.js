var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition testIntegerOperators at operators.ceylon (1:0-23:0)
function testIntegerOperators(){
    
    //AttributeDeclaration i1 at operators.ceylon (3:4-3:29)
    var $i1=$$$cl15.Integer(4).negativeValue();
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1;
    }
    setI1(getI1().negativeValue());
    setI1($$$cl15.Integer(987654).positiveValue());
    setI1($$$cl15.Integer(0).positiveValue());
    setI1($$$cl15.Integer(0).negativeValue());
    
    //AttributeDeclaration i2 at operators.ceylon (9:4-9:36)
    var $i2=$$$cl15.Integer(123).plus($$$cl15.Integer(456));
    function getI2(){
        return $i2;
    }
    function setI2(i2){
        $i2=i2;
    }
    setI1(getI2().minus($$$cl15.Integer(16)));
    setI2(getI1().negativeValue().plus(getI2()).minus($$$cl15.Integer(1)));
    setI1($$$cl15.Integer(3).times($$$cl15.Integer(7)));
    setI2(getI1().times($$$cl15.Integer(2)));
    setI2($$$cl15.Integer(17).divided($$$cl15.Integer(4)));
    setI1(getI2().times($$$cl15.Integer(516)).divided(getI1().negativeValue()));
    setI1($$$cl15.Integer(15).remainder($$$cl15.Integer(4)));
    setI2($$$cl15.Integer(312).remainder($$$cl15.Integer(12)));
    setI1($$$cl15.Integer(2).power($$$cl15.Integer(10)));
    setI2($$$cl15.Integer(100).power($$$cl15.Integer(6)));
}

//MethodDefinition testFloatOperators at operators.ceylon (25:0-43:0)
function testFloatOperators(){
    
    //AttributeDeclaration f1 at operators.ceylon (27:4-27:29)
    var $f1=$$$cl15.Float(4.2).negativeValue();
    function getF1(){
        return $f1;
    }
    function setF1(f1){
        $f1=f1;
    }
    setF1(getF1().negativeValue());
    setF1($$$cl15.Float(987654.9925567).positiveValue());
    setF1($$$cl15.Float(0.0).positiveValue());
    setF1($$$cl15.Float(0.0).negativeValue());
    
    //AttributeDeclaration f2 at operators.ceylon (33:4-33:43)
    var $f2=$$$cl15.Float(3.14159265).plus($$$cl15.Float(456.0));
    function getF2(){
        return $f2;
    }
    function setF2(f2){
        $f2=f2;
    }
    setF1(getF2().minus($$$cl15.Float(0.0016)));
    setF2(getF1().negativeValue().plus(getF2()).minus($$$cl15.Float(1.2)));
    setF1($$$cl15.Float(3.0).times($$$cl15.Float(0.79)));
    setF2(getF1().times($$$cl15.Float(2.0e13)));
    setF2($$$cl15.Float(17.1).divided($$$cl15.Float(4.0E-18)));
    setF1(getF2().times($$$cl15.Float(51.6e2)).divided(getF1().negativeValue()));
    setF1($$$cl15.Float(150.0).power($$$cl15.Float(0.5)));
}

//ClassDefinition OpTest1 at operators.ceylon (45:0-45:17)
function $OpTest1(){}
for(var $ in CeylonObject.prototype){$OpTest1.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$OpTest1.prototype[$+'$']=CeylonObject.prototype[$]}
function OpTest1($$){
    if ($$===undefined)$$=new $OpTest1;
    var $$opTest1=$$;
    return $$;
}

//MethodDefinition testBooleanOperators at operators.ceylon (47:0-65:0)
function testBooleanOperators(){
    
    //AttributeDeclaration o1 at operators.ceylon (48:4-48:24)
    var $o1=OpTest1();
    function getO1(){
        return $o1;
    }
    
    //AttributeDeclaration o2 at operators.ceylon (49:4-49:24)
    var $o2=OpTest1();
    function getO2(){
        return $o2;
    }
    
    //AttributeDeclaration b1 at operators.ceylon (50:4-50:36)
    var $b1=(getO1()===getO2()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB1(){
        return $b1;
    }
    function setB1(b1){
        $b1=b1;
    }
    
    //AttributeDeclaration b2 at operators.ceylon (51:4-51:36)
    var $b2=(getO1()===getO1()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB2(){
        return $b2;
    }
    function setB2(b2){
        $b2=b2;
    }
    setB1(getO1().equals(getO2()));
    setB2(getO1().equals(getO1()));
    setB1($$$cl15.Integer(1).equals($$$cl15.Integer(2)));
    setB2($$$cl15.Integer(1).equals($$$cl15.Integer(2)).equals($$$cl15.getFalse()));
    
    //AttributeDeclaration b3 at operators.ceylon (57:4-57:30)
    var $b3=getB2().equals($$$cl15.getFalse());
    function getB3(){
        return $b3;
    }
    function setB3(b3){
        $b3=b3;
    }
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getFalse():$$$cl15.getFalse()));
    setB2((getB1()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    setB3(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    setB1(($$$cl15.getTrue()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
    setB2(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():getB1()));
    setB3(($$$cl15.getFalse()===$$$cl15.getTrue()?$$$cl15.getTrue():$$$cl15.getFalse()));
}

//MethodDefinition testComparisonOperators at operators.ceylon (67:0-83:0)
function testComparisonOperators(){
    
    //AttributeDeclaration c1 at operators.ceylon (68:4-68:37)
    var $c1=$$$cl15.String("str1").compare($$$cl15.String("str2"));
    function getC1(){
        return $c1;
    }
    
    //AttributeDeclaration c2 at operators.ceylon (69:4-69:37)
    var $c2=$$$cl15.String("str2").compare($$$cl15.String("str1"));
    function getC2(){
        return $c2;
    }
    
    //AttributeDeclaration c3 at operators.ceylon (70:4-70:37)
    var $c3=$$$cl15.String("str1").compare($$$cl15.String("str1"));
    function getC3(){
        return $c3;
    }
    
    //AttributeDeclaration c4 at operators.ceylon (71:4-71:29)
    var $c4=$$$cl15.String("").compare($$$cl15.String(""));
    function getC4(){
        return $c4;
    }
    
    //AttributeDeclaration c5 at operators.ceylon (72:4-72:33)
    var $c5=$$$cl15.String("str1").compare($$$cl15.String(""));
    function getC5(){
        return $c5;
    }
    
    //AttributeDeclaration c6 at operators.ceylon (73:4-73:33)
    var $c6=$$$cl15.String("").compare($$$cl15.String("str2"));
    function getC6(){
        return $c6;
    }
    
    //AttributeDeclaration b1 at operators.ceylon (75:4-75:42)
    var $b1=$$$cl15.String("str1").compare($$$cl15.String("str2")).equals($$$cl15.getSmaller());
    function getB1(){
        return $b1;
    }
    function setB1(b1){
        $b1=b1;
    }
    
    //AttributeDeclaration b2 at operators.ceylon (76:4-76:42)
    var $b2=$$$cl15.String("str1").compare($$$cl15.String("str2")).equals($$$cl15.getLarger());
    function getB2(){
        return $b2;
    }
    function setB2(b2){
        $b2=b2;
    }
    
    //AttributeDeclaration b3 at operators.ceylon (77:4-77:43)
    var $b3=($$$cl15.String("str1").compare($$$cl15.String("str2"))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB3(){
        return $b3;
    }
    function setB3(b3){
        $b3=b3;
    }
    
    //AttributeDeclaration b4 at operators.ceylon (78:4-78:43)
    var $b4=($$$cl15.String("str1").compare($$$cl15.String("str2"))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse());
    function getB4(){
        return $b4;
    }
    function setB4(b4){
        $b4=b4;
    }
    setB1($$$cl15.String("str1").compare($$$cl15.String("str1")).equals($$$cl15.getSmaller()));
    setB2($$$cl15.String("str1").compare($$$cl15.String("str1")).equals($$$cl15.getLarger()));
    setB3(($$$cl15.String("str1").compare($$$cl15.String("str1"))!==$$$cl15.getLarger()?$$$cl15.getTrue():$$$cl15.getFalse()));
    setB4(($$$cl15.String("str1").compare($$$cl15.String("str1"))!==$$$cl15.getSmaller()?$$$cl15.getTrue():$$$cl15.getFalse()));
}
