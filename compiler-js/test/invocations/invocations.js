var $ceylon$language=require('ceylon/language/0.1/ceylon.language');
var $functions=require('default/functions');

//function invocations at invocations.ceylon (3:0-14:0)
function invocations(){
    $functions.helloWorld();
    (function (){return $functions.helloWorld()}());
    $functions.hello($ceylon$language.String("world"));
    (function (){var $name=$ceylon$language.String("world");return $functions.hello($name)}());
    $functions.helloAll($ceylon$language.ArraySequence([$ceylon$language.String("someone"),$ceylon$language.String("someone else")]));
    (function (){var $names=$ceylon$language.ArraySequence([$ceylon$language.String("someone"),$ceylon$language.String("someone else")]);return $functions.helloAll($names)}());
    
    //value s1 at invocations.ceylon (10:4-10:28)
    var $s1=$functions.toString($ceylon$language.Integer(99));
    function getS1(){
        return $s1;
    }
    
    //value s2 at invocations.ceylon (11:4-11:36)
    var $s2=(function (){var $obj=$ceylon$language.Integer(99);return $functions.toString($obj)}());
    function getS2(){
        return $s2;
    }
    
    //value f1 at invocations.ceylon (12:4-12:29)
    var $f1=$functions.add($ceylon$language.Float(1.0),$ceylon$language.Float(1.0));
    function getF1(){
        return $f1;
    }
    
    //value f2 at invocations.ceylon (13:4-13:37)
    var $f2=(function (){var $x=$ceylon$language.Float(1.0);var $y=$ceylon$language.Float(1.0);return $functions.add($x,$y)}());
    function getF2(){
        return $f2;
    }
    
    //function p at invocations.ceylon (15:0-17:0)
    function p(i){
        $ceylon$language.print(i);
    }
    $functions.repeat($ceylon$language.Integer(10),p);
}
this.invocations=invocations;
