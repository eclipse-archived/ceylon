var $ceylon$language=require('../../runtime/ceylon.language.js');
var $members=require('./../members/members.js');

//interface X at misc.ceylon (3:0-7:0)
function X(){
    var $thisX=new CeylonObject();
    
    //function helloWorld at misc.ceylon (4:4-6:4)
    function helloWorld(){
        $ceylon$language.print($ceylon$language.String("hello world"));
    }
    $thisX.helloWorld=helloWorld;
    return $thisX;
}
this.X=X;

//class Foo at misc.ceylon (9:0-18:0)
function Foo(name){
    var $thisFoo=new CeylonObject();
    
    //value name at misc.ceylon (10:4-10:29)
    var $name=name;
    function getName(){
        return $name;
    }
    $thisFoo.getName=getName;
    
    //value counter at misc.ceylon (11:4-11:29)
    var $counter=$ceylon$language.Integer(0);
    function getCounter(){
        return $counter;
    }
    function setCounter(counter){
        $counter=counter;
    }
    
    //value count at misc.ceylon (12:4-12:43)
    function getCount(){
        return getCounter();
    }
    $thisFoo.getCount=getCount;
    
    //function inc at misc.ceylon (13:4-13:44)
    function inc(){
        setCounter(getCounter().plus($ceylon$language.Integer(1)));
    }
    $thisFoo.inc=inc;
    
    //function printName at misc.ceylon (14:4-16:4)
    function printName(){
        $ceylon$language.print($ceylon$language.String("foo name = ").plus(name));
    }
    $thisFoo.printName=printName;
    inc();
    return $thisFoo;
}
this.Foo=Foo;

//class Bar at misc.ceylon (20:0-32:0)
function Bar(){
    var $thisBar=new CeylonObject();
    var $super=Foo($ceylon$language.String("Hello"));
    for(var $m in $super){$thisBar[$m]=$super[$m]}
    var $superX=X();
    for(var $m in $superX){$thisBar[$m]=$superX[$m]}
    
    //function printName at misc.ceylon (21:4-24:4)
    function printName(){
        $ceylon$language.print($ceylon$language.String("bar name = ").plus($thisBar.getName()));
        $super.printName();
    }
    $thisBar.printName=printName;
    
    //class Inner at misc.ceylon (25:4-31:4)
    function Inner(){
        var $thisInner=new CeylonObject();
        $ceylon$language.print($ceylon$language.String("creating inner class of :").plus($thisBar.getName()));
        
        //function incOuter at misc.ceylon (28:8-30:8)
        function incOuter(){
            $thisBar.inc();
        }
        $thisInner.incOuter=incOuter;
        return $thisInner;
    }
    $thisBar.Inner=Inner;
    return $thisBar;
}
this.Bar=Bar;

//function printBoth at misc.ceylon (34:0-36:0)
function printBoth(x,y){
    $ceylon$language.print(x.plus($ceylon$language.String(", ")).plus(y));
}

//function doIt at misc.ceylon (38:0-40:0)
function doIt(f){
    f();
    f();
}

//object foob at misc.ceylon (42:0-44:0)
var $foob=function foob(){
    var $thisfoob=new CeylonObject();
    
    //value name at misc.ceylon (43:4-43:30)
    var $name=$ceylon$language.String("Gavin");
    function getName(){
        return $name;
    }
    $thisfoob.getName=getName;
    return $thisfoob;
}();
function getFoob(){
    return $foob;
}

//function printAll at misc.ceylon (46:0-46:34)
function printAll(strings){}

//function testit at misc.ceylon (48:0-80:0)
function testit(){
    
    //value name at misc.ceylon (49:4-49:24)
    var $name=$ceylon$language.String("hello");
    function getName(){
        return $name;
    }
    $ceylon$language.print(getName());
    
    //value foo at misc.ceylon (51:4-51:28)
    var $foo=Foo($ceylon$language.String("goodbye"));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=$ceylon$language.String("y");var $x=$ceylon$language.String("x");return printBoth($x,$y)}());
    getFoo().inc();
    getFoo().inc();
    $ceylon$language.print(getFoo().getCount());
    getFoo().printName();
    Bar().printName();
    Bar().Inner();
    doIt(getFoo().inc);
    $ceylon$language.print(getFoo().getCount());
    doIt(Bar);
    $ceylon$language.print(getFoob().getName());
    
    //object x at misc.ceylon (63:4-67:4)
    var $x=function x(){
        var $thisx=new CeylonObject();
        
        //function y at misc.ceylon (64:8-66:8)
        function y(){
            $ceylon$language.print($ceylon$language.String("xy"));
        }
        $thisx.y=y;
        return $thisx;
    }();
    function getX(){
        return $x;
    }
    getX().y();
    
    //value b at misc.ceylon (69:4-69:17)
    var $b=Bar();
    function getB(){
        return $b;
    }
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    $ceylon$language.print(getB().getCount());
    printAll($ceylon$language.ArraySequence([$ceylon$language.String("hello"),$ceylon$language.String("world")]));
    (function (){var $strings=$ceylon$language.ArraySequence([$ceylon$language.String("hello"),$ceylon$language.String("world")]);return printAll($strings)}());
    
    //value c at misc.ceylon (77:4-77:26)
    var $c=$members.Counter($ceylon$language.Integer(0));
    function getC(){
        return $c;
    }
    getC().inc();
    getC().inc();
    $ceylon$language.print(getC().getCount());
}
this.testit=testit;
