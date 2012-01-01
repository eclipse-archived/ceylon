var $$cl15=require('ceylon/language/0.1/ceylon.language');

//interface X at misc.ceylon (3:0-7:0)
function X(){
    var $thisX=new CeylonObject();
    
    //function helloWorld at misc.ceylon (4:4-6:4)
    function helloWorld(){
        $$cl15.print($$cl15.String("hello world"));
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
    var $counter=$$cl15.Integer(0);
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
        setCounter(getCounter().plus($$cl15.Integer(1)));
    }
    $thisFoo.inc=inc;
    
    //function printName at misc.ceylon (14:4-16:4)
    function printName(){
        $$cl15.print($$cl15.String("foo name = ").plus(name));
    }
    $thisFoo.printName=printName;
    inc();
    return $thisFoo;
}
this.Foo=Foo;

//class Bar at misc.ceylon (20:0-32:0)
function Bar(){
    var $thisBar=new CeylonObject();
    var $super=Foo($$cl15.String("Hello"));
    for(var $m in $super){$thisBar[$m]=$super[$m]}
    var $superX=X();
    for(var $m in $superX){$thisBar[$m]=$superX[$m]}
    
    //function printName at misc.ceylon (21:4-24:4)
    function printName(){
        $$cl15.print($$cl15.String("bar name = ").plus($thisBar.getName()));
        $super.printName();
    }
    $thisBar.printName=printName;
    
    //class Inner at misc.ceylon (25:4-31:4)
    function Inner(){
        var $thisInner=new CeylonObject();
        $$cl15.print($$cl15.String("creating inner class of :").plus($thisBar.getName()));
        
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
    $$cl15.print(x.plus($$cl15.String(", ")).plus(y));
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
    var $name=$$cl15.String("Gavin");
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

//class alias F at misc.ceylon (48:0-48:26)
var F=Foo;
var $$cl15=require('ceylon/language/0.1/ceylon.language');
var $$m7=require('default/members');

//function testit at misc.ceylon (50:0-82:0)
function testit(){
    
    //value name at misc.ceylon (51:4-51:24)
    var $name=$$cl15.String("hello");
    function getName(){
        return $name;
    }
    $$cl15.print(getName());
    
    //value foo at misc.ceylon (53:4-53:24)
    var $foo=F($$cl15.String("goodbye"));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=$$cl15.String("y");var $x=$$cl15.String("x");return printBoth($x,$y)}());
    getFoo().inc();
    getFoo().inc();
    $$cl15.print(getFoo().getCount());
    getFoo().printName();
    Bar().printName();
    Bar().Inner();
    doIt(getFoo().inc);
    $$cl15.print(getFoo().getCount());
    doIt(Bar);
    $$cl15.print(getFoob().getName());
    
    //object x at misc.ceylon (65:4-69:4)
    var $x=function x(){
        var $thisx=new CeylonObject();
        
        //function y at misc.ceylon (66:8-68:8)
        function y(){
            $$cl15.print($$cl15.String("xy"));
        }
        $thisx.y=y;
        return $thisx;
    }();
    function getX(){
        return $x;
    }
    getX().y();
    
    //value b at misc.ceylon (71:4-71:17)
    var $b=Bar();
    function getB(){
        return $b;
    }
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    $$cl15.print(getB().getCount());
    printAll($$cl15.ArraySequence([$$cl15.String("hello"),$$cl15.String("world")]));
    (function (){var $strings=$$cl15.ArraySequence([$$cl15.String("hello"),$$cl15.String("world")]);return printAll($strings)}());
    
    //value c at misc.ceylon (79:4-79:26)
    var $c=$$m7.Counter($$cl15.Integer(0));
    function getC(){
        return $c;
    }
    getC().inc();
    getC().inc();
    $$cl15.print(getC().getCount());
}
this.testit=testit;
