
//interface X at jstest.ceylon (1:0-5:0)
function X(){
    var $thisX={};
    
    //function helloWorld at jstest.ceylon (2:4-4:4)
    function helloWorld(){
        print(new String("hello world"));
    }
    $thisX.helloWorld=helloWorld;
    return $thisX;
}

//class Foo at jstest.ceylon (7:0-16:0)
function Foo(name){
    var $thisFoo={};
    
    //value name at jstest.ceylon (8:4-8:29)
    var $name=name;
    function getName(){
        return $name;
    }
    $thisFoo.getName=getName;
    
    //value counter at jstest.ceylon (9:4-9:29)
    var $counter=new Integer(0);
    function getCounter(){
        return $counter;
    }
    function setCounter(counter){
        $counter=counter;
    }
    
    //value count at jstest.ceylon (10:4-10:43)
    function getCount(){
        return getCounter();
    }
    $thisFoo.getCount=getCount;
    
    //function inc at jstest.ceylon (11:4-11:44)
    function inc(){
        setCounter(getCounter().plus(new Integer(1)));
    }
    $thisFoo.inc=inc;
    
    //function printName at jstest.ceylon (12:4-14:4)
    function printName(){
        print(new String("foo name = ").plus(name));
    }
    $thisFoo.printName=printName;
    inc();
    return $thisFoo;
}

//class Bar at jstest.ceylon (18:0-30:0)
function Bar(){
    var $thisBar={};
    var $super=Foo(new String("Hello"));
    for(var $m in $super){$thisBar[$m]=$super[$m]}
    var $superX=X();
    for(var $m in $superX){$thisBar[$m]=$superX[$m]}
    
    //function printName at jstest.ceylon (19:4-22:4)
    function printName(){
        print(new String("bar name = ").plus($thisBar.getName()));
        $super.printName();
    }
    $thisBar.printName=printName;
    
    //class Inner at jstest.ceylon (23:4-29:4)
    function Inner(){
        var $thisInner={};
        print(new String("creating inner class of :").plus($thisBar.getName()));
        
        //function incOuter at jstest.ceylon (26:8-28:8)
        function incOuter(){
            $thisBar.inc();
        }
        $thisInner.incOuter=incOuter;
        return $thisInner;
    }
    $thisBar.Inner=Inner;
    return $thisBar;
}

//function printBoth at jstest.ceylon (32:0-34:0)
function printBoth(x,y){
    print(x.plus(new String(", ")).plus(y));
}

//function doIt at jstest.ceylon (36:0-38:0)
function doIt(f){
    f();
    f();
}

//object foob at jstest.ceylon (40:0-42:0)
var $foob=new function foob(){
    var $thisfoob={};
    
    //value name at jstest.ceylon (41:4-41:30)
    var $name=new String("Gavin");
    function getName(){
        return $name;
    }
    $thisfoob.getName=getName;
    return $thisfoob;
}();
function getFoob(){
    return $foob;
}

//function printAll at jstest.ceylon (44:0-44:34)
function printAll(strings){}

//function testit at jstest.ceylon (46:0-74:0)
function testit(){
    
    //value name at jstest.ceylon (47:4-47:24)
    var $name=new String("hello");
    function getName(){
        return $name;
    }
    print(getName());
    
    //value foo at jstest.ceylon (49:4-49:28)
    var $foo=Foo(new String("goodbye"));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=new String("y");var $x=new String("x");return printBoth($x,$y)}());
    getFoo().inc();
    getFoo().inc();
    print(getFoo().getCount());
    getFoo().printName();
    Bar().printName();
    Bar().Inner();
    doIt(getFoo().inc);
    print(getFoo().getCount());
    doIt(Bar);
    print(getFoob().getName());
    
    //object x at jstest.ceylon (61:4-65:4)
    var $x=new function x(){
        var $thisx={};
        
        //function y at jstest.ceylon (62:8-64:8)
        function y(){
            print(new String("xy"));
        }
        $thisx.y=y;
        return $thisx;
    }();
    function getX(){
        return $x;
    }
    getX().y();
    
    //value b at jstest.ceylon (67:4-67:17)
    var $b=Bar();
    function getB(){
        return $b;
    }
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    print(getB().getCount());
    printAll(new ArraySequence([new String("hello"),new String("world")]));
    (function (){var $strings=new ArraySequence([new String("hello"),new String("world")]);return printAll($strings)}());
}
