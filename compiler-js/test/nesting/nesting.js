var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-26:0)
function Outer(name, $$outer){
    if ($$outer===undefined)$$outer=new CeylonObject;
    
    //AttributeDeclaration int at nesting.ceylon (2:4-2:18)
    var $int=$$$cl15.Integer(10);
    function getInt(){
        return $int;
    }
    $$outer.getInt=getInt;
    
    //AttributeDeclaration float at nesting.ceylon (3:4-3:34)
    var $float=getInt().getFloat();
    function getFloat(){
        return $float;
    }
    $$outer.getFloat=getFloat;
    
    //MethodDefinition noop at nesting.ceylon (4:4-4:17)
    function noop(){}
    $$outer.noop=noop;
    
    //ClassDefinition Inner at nesting.ceylon (5:4-18:4)
    function Inner($$inner){
        if ($$inner===undefined)$$inner=new CeylonObject;
        
        //MethodDefinition printName at nesting.ceylon (6:8-8:8)
        function printName(){
            $$$cl15.print(name);
        }
        
        //AttributeGetterDefinition int at nesting.ceylon (9:8-11:8)
        function getInt(){
            return $$outer.getInt();
        }
        $$inner.getInt=getInt;
        
        //AttributeGetterDefinition float at nesting.ceylon (12:8-14:8)
        function getFloat(){
            return $$outer.getFloat();
        }
        $$inner.getFloat=getFloat;
        
        //MethodDefinition noop at nesting.ceylon (15:8-17:8)
        function noop(){
            $$outer.noop();
        }
        $$inner.noop=noop;
        return $$inner;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (19:4-19:25)
    var $inner=Inner();
    function getInner(){
        return $inner;
    }
    $$$cl15.print(getInner().getInt());
    $$$cl15.print(getInner().getFloat());
    getInner().noop();
    noop();
    return $$outer;
}
this.Outer=Outer;

//MethodDefinition outr at nesting.ceylon (28:0-40:0)
function outr(name){
    
    //AttributeDeclaration uname at nesting.ceylon (29:4-29:34)
    var $uname=name.getUppercased();
    function getUname(){
        return $uname;
    }
    
    //MethodDefinition inr at nesting.ceylon (30:4-32:4)
    function inr(){
        return name;
    }
    
    //AttributeGetterDefinition uinr at nesting.ceylon (33:4-35:4)
    function getUinr(){
        return getUname();
    }
    
    //AttributeDeclaration result at nesting.ceylon (36:4-36:25)
    var $result=inr();
    function getResult(){
        return $result;
    }
    
    //AttributeDeclaration uresult at nesting.ceylon (37:4-37:25)
    var $uresult=getUinr();
    function getUresult(){
        return $uresult;
    }
    $$$cl15.print(getResult());
    $$$cl15.print(getUresult());
}
this.outr=outr;

//ClassDefinition Holder at nesting.ceylon (42:0-49:0)
function Holder(o, $$holder){
    if ($$holder===undefined)$$holder=new CeylonObject;
    
    //MethodDefinition get at nesting.ceylon (43:4-45:4)
    function get(){
        return o;
    }
    $$holder.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (46:4-48:4)
    function getString(){
        return o.getString();
    }
    $$holder.getString=getString;
    return $$holder;
}
this.Holder=Holder;

//ClassDefinition Wrapper at nesting.ceylon (51:0-59:0)
function Wrapper($$wrapper){
    if ($$wrapper===undefined)$$wrapper=new CeylonObject;
    
    //AttributeDeclaration o at nesting.ceylon (52:4-52:18)
    var $o=$$$cl15.Integer(100);
    function getO(){
        return $o;
    }
    $$wrapper.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (53:4-55:4)
    function get(){
        return getO();
    }
    $$wrapper.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (56:4-58:4)
    function getString(){
        return getO().getString();
    }
    $$wrapper.getString=getString;
    return $$wrapper;
}
this.Wrapper=Wrapper;

//ClassDefinition Unwrapper at nesting.ceylon (61:0-69:0)
function Unwrapper($$unwrapper){
    if ($$unwrapper===undefined)$$unwrapper=new CeylonObject;
    
    //AttributeDeclaration o at nesting.ceylon (62:4-62:27)
    var $o=$$$cl15.Float(23.56);
    function getO(){
        return $o;
    }
    $$unwrapper.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (63:4-65:4)
    function get(){
        return $$unwrapper.getO();
    }
    $$unwrapper.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (66:4-68:4)
    function getString(){
        return $$unwrapper.getO().getString();
    }
    $$unwrapper.getString=getString;
    return $$unwrapper;
}
this.Unwrapper=Unwrapper;

//MethodDefinition producer at nesting.ceylon (71:0-75:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (72:4-72:18)
    var $o=$$$cl15.Integer(123);
    function getO(){
        return $o;
    }
    
    //MethodDefinition produce at nesting.ceylon (73:4-73:35)
    function produce(){
        return getO();
    }
    return produce;
}

//MethodDefinition returner at nesting.ceylon (77:0-80:0)
function returner(o){
    
    //MethodDefinition produce at nesting.ceylon (78:4-78:35)
    function produce(){
        return o;
    }
    return produce;
}

//ClassDefinition A at nesting.ceylon (82:0-103:0)
function A($$a){
    if ($$a===undefined)$$a=new CeylonObject;
    
    //AttributeDeclaration foo at nesting.ceylon (83:4-83:22)
    var $foo=$$$cl15.String("foo");
    function getFoo(){
        return $foo;
    }
    $$a.getFoo=getFoo;
    
    //ClassDefinition B at nesting.ceylon (84:4-94:4)
    function B($$b){
        if ($$b===undefined)$$b=new CeylonObject;
        
        //AttributeDeclaration qux at nesting.ceylon (85:8-85:26)
        var $qux=$$$cl15.String("qux");
        function getQux(){
            return $qux;
        }
        $$b.getQux=getQux;
        
        //ClassDefinition C at nesting.ceylon (86:8-93:8)
        function C($$c){
            if ($$c===undefined)$$c=new CeylonObject;
            
            //MethodDefinition foobar at nesting.ceylon (87:12-89:12)
            function foobar(){
                return getFoo();
            }
            $$c.foobar=foobar;
            
            //MethodDefinition quxx at nesting.ceylon (90:12-92:12)
            function quxx(){
                return getQux();
            }
            $$c.quxx=quxx;
            return $$c;
        }
        $$b.C=C;
        return $$b;
    }
    $$a.B=B;
    
    //MethodDefinition baz at nesting.ceylon (95:4-102:4)
    function baz(){
        
        //ClassDefinition Baz at nesting.ceylon (96:8-100:8)
        function Baz($$baz){
            if ($$baz===undefined)$$baz=new CeylonObject;
            
            //MethodDefinition get at nesting.ceylon (97:12-99:12)
            function get(){
                return getFoo();
            }
            $$baz.get=get;
            return $$baz;
        }
        return Baz().get();
    }
    $$a.baz=baz;
    return $$a;
}

//MethodDefinition test at nesting.ceylon (105:0-120:0)
function test(){
    outr($$$cl15.String("Hello"));
    $$$cl15.print(Holder($$$cl15.String("ok")).get());
    $$$cl15.print(Holder($$$cl15.String("ok")));
    $$$cl15.print(Wrapper().get());
    $$$cl15.print(Wrapper());
    $$$cl15.print(Unwrapper().get());
    $$$cl15.print(Unwrapper().getO());
    $$$cl15.print(Unwrapper());
    $$$cl15.print(producer()());
    $$$cl15.print(returner($$$cl15.String("something"))());
    $$$cl15.print(A().B().C().foobar());
    $$$cl15.print(A().B().C().quxx());
    $$$cl15.print(A().baz());
    Outer($$$cl15.String("Hello"));
}
this.test=test;
