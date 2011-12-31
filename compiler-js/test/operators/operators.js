var $ceylon$language=require('ceylon/language/0.1/ceylon.language');

//function testIntegerOperators at test.ceylon (1:0-17:0)
function testIntegerOperators(){
    
    //value i1 at test.ceylon (3:4-3:29)
    var $i1=$ceylon$language.Integer(4).negativeValue();
    function getI1(){
        return $i1;
    }
    function setI1(i1){
        $i1=i1;
    }
    setI1(getI1().negativeValue());
    setI1($ceylon$language.Integer(987654).positiveValue());
    setI1($ceylon$language.Integer(0).positiveValue());
    setI1($ceylon$language.Integer(0).negativeValue());
    
    //value i2 at test.ceylon (9:4-9:36)
    var $i2=$ceylon$language.Integer(123).plus($ceylon$language.Integer(456));
    function getI2(){
        return $i2;
    }
    function setI2(i2){
        $i2=i2;
    }
    setI1(getI2().minus($ceylon$language.Integer(16)));
    setI2(getI1().negativeValue().plus(getI2()).minus($ceylon$language.Integer(1)));
    setI1($ceylon$language.Integer(3).times($ceylon$language.Integer(7)));
    setI2(getI1().times($ceylon$language.Integer(2)));
    setI2($ceylon$language.Integer(17).divided($ceylon$language.Integer(4)));
    setI1(getI2().times($ceylon$language.Integer(516)).divided(getI1().negativeValue()));
}

//function testFloatOperators at test.ceylon (19:0-36:0)
function testFloatOperators(){
    
    //value f1 at test.ceylon (21:4-21:29)
    var $f1=$ceylon$language.Float(4.2).negativeValue();
    function getF1(){
        return $f1;
    }
    function setF1(f1){
        $f1=f1;
    }
    setF1(getF1().negativeValue());
    setF1($ceylon$language.Float(987654.9925567).positiveValue());
    setF1($ceylon$language.Float(0.0).positiveValue());
    setF1($ceylon$language.Float(0.0).negativeValue());
    
    //value f2 at test.ceylon (27:4-27:43)
    var $f2=$ceylon$language.Float(3.14159265).plus($ceylon$language.Float(456.0));
    function getF2(){
        return $f2;
    }
    function setF2(f2){
        $f2=f2;
    }
    setF1(getF2().minus($ceylon$language.Float(0.0016)));
    setF2(getF1().negativeValue().plus(getF2()).minus($ceylon$language.Float(1.2)));
    setF1($ceylon$language.Float(3.0).times($ceylon$language.Float(0.79)));
    setF2(getF1().times($ceylon$language.Float(2.0e13)));
    setF2($ceylon$language.Float(17.1).divided($ceylon$language.Float(4.0E-18)));
    setF1(getF2().times($ceylon$language.Float(51.6e2)).divided(getF1().negativeValue()));
}
