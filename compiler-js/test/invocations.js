
//function invocations at invocations.ceylon (1:0-12:0)
function invocations(){
    helloWorld();
    (function (){return helloWorld()}());
    hello(new String("world"));
    (function (){var $name=new String("world");return hello($name)}());
    helloAll(new ArraySequence([new String("someone"),new String("someone else")]));
    (function (){var $names=new ArraySequence([new String("someone"),new String("someone else")]);return helloAll($names)}());
    
    //value s1 at invocations.ceylon (8:4-8:28)
    var $s1=toString(new Integer(99));
    function getS1(){
        return $s1;
    }
    
    //value s2 at invocations.ceylon (9:4-9:36)
    var $s2=(function (){var $obj=new Integer(99);return toString($obj)}());
    function getS2(){
        return $s2;
    }
    
    //value f1 at invocations.ceylon (10:4-10:29)
    var $f1=add(new Float(1.0',new Float(1.0');
    function getF1(){
        return $f1;
    }
    
    //value f2 at invocations.ceylon (11:4-11:37)
    var $f2=(function (){var $x=new Float(1.0';var $y=new Float(1.0';return add($x,$y)}());
    function getF2(){
        return $f2;
    }
    
}
