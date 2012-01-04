var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//InterfaceDefinition X at misc.ceylon (1:0-5:0)
function X($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$x=$$;
    
    //MethodDefinition helloWorld at misc.ceylon (2:4-4:4)
    function helloWorld(){
        $$$cl15.print($$$cl15.String("hello world"));
    }
    $$.helloWorld=helloWorld;
    return $$;
}
this.X=X;

//ClassDefinition Foo at misc.ceylon (7:0-16:0)
function Foo(name, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$foo=$$;
    
    //AttributeDeclaration name at misc.ceylon (8:4-8:29)
    var $name=name;
    function getName(){
        return $name;
    }
    $$.getName=getName;
    
    //AttributeDeclaration counter at misc.ceylon (9:4-9:29)
    var $counter=$$$cl15.Integer(0);
    function getCounter(){
        return $counter;
    }
    $$.getCounter=getCounter;
    function setCounter(counter){
        $counter=counter;
    }
    $$.setCounter=setCounter;
    
    //AttributeGetterDefinition count at misc.ceylon (10:4-10:43)
    function getCount(){
        return getCounter();
    }
    $$.getCount=getCount;
    
    //MethodDefinition inc at misc.ceylon (11:4-11:44)
    function inc(){
        setCounter(getCounter().plus($$$cl15.Integer(1)));
    }
    $$.inc=inc;
    
    //MethodDefinition printName at misc.ceylon (12:4-14:4)
    function printName(){
        $$$cl15.print($$$cl15.String("foo name = ").plus(name));
    }
    $$.printName=printName;
    $$.inc();
    return $$;
}
this.Foo=Foo;

//ClassDefinition Bar at misc.ceylon (18:0-32:0)
function Bar($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$bar=$$;
    Foo($$$cl15.String("Hello"),$$);
    X($$);
    
    //MethodDefinition printName at misc.ceylon (19:4-22:4)
    function printName(){
        $$$cl15.print($$$cl15.String("bar name = ").plus($$bar.getName()));
        $$.printName$();
    }
    $$.printName$=$$.printName;
    $$.printName=printName;
    
    //ClassDefinition Inner at misc.ceylon (23:4-29:4)
    function Inner($$){
        if ($$===undefined)$$=new CeylonObject;
        var $$inner=$$;
        $$$cl15.print($$$cl15.String("creating inner class of :").plus($$bar.getName()));
        
        //MethodDefinition incOuter at misc.ceylon (26:8-28:8)
        function incOuter(){
            $$bar.inc();
        }
        $$.incOuter=incOuter;
        return $$;
    }
    $$.Inner=Inner;
    return $$;
}
this.Bar=Bar;

//MethodDefinition printBoth at misc.ceylon (34:0-36:0)
function printBoth(x,y){
    $$$cl15.print(x.plus($$$cl15.String(", ")).plus(y));
}

//MethodDefinition doIt at misc.ceylon (38:0-40:0)
function doIt(f){
    f();
    f();
}

//ObjectDefinition foob at misc.ceylon (42:0-44:0)
var $foob=function foob(){
    var $$=new CeylonObject;
    
    //AttributeDeclaration name at misc.ceylon (43:4-43:30)
    var $name=$$$cl15.String("Gavin");
    function getName(){
        return $name;
    }
    $$.getName=getName;
    return $$;
}();
function getFoob(){
    return $foob;
}

//MethodDefinition printAll at misc.ceylon (46:0-46:34)
function printAll(strings){}

//ClassDeclaration F at misc.ceylon (48:0-48:26)
var F=Foo;
var $$$cl15=require('ceylon/language/0.1/ceylon.language');
var $$$m7=require('default/members');

//MethodDefinition testit at testit.ceylon (3:0-35:0)
function testit(){
    
    //AttributeDeclaration name at testit.ceylon (4:4-4:24)
    var $name=$$$cl15.String("hello");
    function getName(){
        return $name;
    }
    $$$cl15.print(getName());
    
    //AttributeDeclaration foo at testit.ceylon (6:4-6:24)
    var $foo=F($$$cl15.String("goodbye"));
    function getFoo(){
        return $foo;
    }
    printBoth(getName(),getFoo().getName());
    (function (){var $y=$$$cl15.String("y");var $x=$$$cl15.String("x");return printBoth($x,$y)}());
    getFoo().inc();
    getFoo().inc();
    $$$cl15.print(getFoo().getCount());
    getFoo().printName();
    Bar().printName();
    Bar().Inner();
    doIt(getFoo().inc);
    $$$cl15.print(getFoo().getCount());
    doIt(Bar);
    $$$cl15.print(getFoob().getName());
    
    //ObjectDefinition x at testit.ceylon (18:4-22:4)
    var $x=function x(){
        var $$=new CeylonObject;
        
        //MethodDefinition y at testit.ceylon (19:8-21:8)
        function y(){
            $$$cl15.print($$$cl15.String("xy"));
        }
        $$.y=y;
        return $$;
    }();
    function getX(){
        return $x;
    }
    getX().y();
    
    //AttributeDeclaration b at testit.ceylon (24:4-24:17)
    var $b=Bar();
    function getB(){
        return $b;
    }
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    getB().Inner().incOuter();
    $$$cl15.print(getB().getCount());
    printAll($$$cl15.ArraySequence([$$$cl15.String("hello"),$$$cl15.String("world")]));
    (function (){var $strings=$$$cl15.ArraySequence([$$$cl15.String("hello"),$$$cl15.String("world")]);return printAll($strings)}());
    
    //AttributeDeclaration c at testit.ceylon (32:4-32:26)
    var $c=$$$m7.Counter($$$cl15.Integer(0));
    function getC(){
        return $c;
    }
    getC().inc();
    getC().inc();
    $$$cl15.print(getC().getCount());
}
this.testit=testit;
