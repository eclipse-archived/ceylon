var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-25:0)
function Outer(name, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$outer=$$;
    
    //AttributeDeclaration int at nesting.ceylon (2:4-2:18)
    var $int=$$$cl15.Integer(10);
    function getInt(){
        return $int;
    }
    $$.getInt=getInt;
    
    //AttributeDeclaration float at nesting.ceylon (3:4-3:34)
    var $float=getInt().getFloat();
    function getFloat(){
        return $float;
    }
    $$.getFloat=getFloat;
    
    //MethodDefinition noop at nesting.ceylon (4:4-4:17)
    function noop(){}
    
    //ClassDefinition Inner at nesting.ceylon (5:4-18:4)
    function Inner($$){
        if ($$===undefined)$$=new CeylonObject;
        var $$inner=$$;
        
        //MethodDefinition printName at nesting.ceylon (6:8-8:8)
        function printName(){
            $$$cl15.print(name);
        }
        
        //AttributeGetterDefinition int at nesting.ceylon (9:8-11:8)
        function getInt(){
            return $$outer.getInt();
        }
        $$.getInt=getInt;
        
        //AttributeGetterDefinition float at nesting.ceylon (12:8-14:8)
        function getFloat(){
            return $$outer.getFloat();
        }
        $$.getFloat=getFloat;
        
        //MethodDefinition noop at nesting.ceylon (15:8-17:8)
        function noop(){
            $$outer.noop();
        }
        return $$;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (19:4-19:25)
    var $inner=Inner();
    function getInner(){
        return $inner;
    }
    $$$cl15.print(getInner().getInt());
    $$$cl15.print(getInner().getFloat());
    return $$;
}
this.Outer=Outer;

//MethodDefinition outr at nesting.ceylon (27:0-39:0)
function outr(name){
    
    //AttributeDeclaration uname at nesting.ceylon (28:4-28:34)
    var $uname=name.getUppercased();
    function getUname(){
        return $uname;
    }
    
    //MethodDefinition inr at nesting.ceylon (29:4-31:4)
    function inr(){
        return name;
    }
    
    //AttributeGetterDefinition uinr at nesting.ceylon (32:4-34:4)
    function getUinr(){
        return getUname();
    }
    
    //AttributeDeclaration result at nesting.ceylon (35:4-35:25)
    var $result=inr();
    function getResult(){
        return $result;
    }
    
    //AttributeDeclaration uresult at nesting.ceylon (36:4-36:25)
    var $uresult=getUinr();
    function getUresult(){
        return $uresult;
    }
    $$$cl15.print(getResult());
    $$$cl15.print(getUresult());
}
this.outr=outr;

//ClassDefinition Holder at nesting.ceylon (41:0-48:0)
function Holder(o, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$holder=$$;
    
    //MethodDefinition get at nesting.ceylon (42:4-44:4)
    function get(){
        return o;
    }
    $$.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (45:4-47:4)
    function getString(){
        return o.getString();
    }
    $$.getString$=$$.getString;
    $$.getString=getString;
    return $$;
}
this.Holder=Holder;

//ClassDefinition Wrapper at nesting.ceylon (50:0-58:0)
function Wrapper($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$wrapper=$$;
    
    //AttributeDeclaration o at nesting.ceylon (51:4-51:18)
    var $o=$$$cl15.Integer(100);
    function getO(){
        return $o;
    }
    $$.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (52:4-54:4)
    function get(){
        return getO();
    }
    $$.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (55:4-57:4)
    function getString(){
        return getO().getString();
    }
    $$.getString$=$$.getString;
    $$.getString=getString;
    return $$;
}
this.Wrapper=Wrapper;

//ClassDefinition Unwrapper at nesting.ceylon (60:0-68:0)
function Unwrapper($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$unwrapper=$$;
    
    //AttributeDeclaration o at nesting.ceylon (61:4-61:27)
    var $o=$$$cl15.Float(23.56);
    function getO(){
        return $o;
    }
    $$.getO=getO;
    
    //MethodDefinition get at nesting.ceylon (62:4-64:4)
    function get(){
        return $$.getO();
    }
    $$.get=get;
    
    //AttributeGetterDefinition string at nesting.ceylon (65:4-67:4)
    function getString(){
        return $$.getO().getString();
    }
    $$.getString$=$$.getString;
    $$.getString=getString;
    return $$;
}
this.Unwrapper=Unwrapper;

//MethodDefinition producer at nesting.ceylon (70:0-74:0)
function producer(){
    
    //AttributeDeclaration o at nesting.ceylon (71:4-71:18)
    var $o=$$$cl15.Integer(123);
    function getO(){
        return $o;
    }
    
    //MethodDefinition produce at nesting.ceylon (72:4-72:35)
    function produce(){
        return getO();
    }
    return produce;
}

//MethodDefinition returner at nesting.ceylon (76:0-79:0)
function returner(o){
    
    //MethodDefinition produce at nesting.ceylon (77:4-77:35)
    function produce(){
        return o;
    }
    return produce;
}

//ClassDefinition A at nesting.ceylon (81:0-102:0)
function A($$){
    if ($$===undefined)$$=new CeylonObject;
    var $$a=$$;
    
    //AttributeDeclaration foo at nesting.ceylon (82:4-82:22)
    var $foo=$$$cl15.String("foo");
    function getFoo(){
        return $foo;
    }
    $$.getFoo=getFoo;
    
    //ClassDefinition B at nesting.ceylon (83:4-93:4)
    function B($$){
        if ($$===undefined)$$=new CeylonObject;
        var $$b=$$;
        
        //AttributeDeclaration qux at nesting.ceylon (84:8-84:26)
        var $qux=$$$cl15.String("qux");
        function getQux(){
            return $qux;
        }
        $$.getQux=getQux;
        
        //ClassDefinition C at nesting.ceylon (85:8-92:8)
        function C($$){
            if ($$===undefined)$$=new CeylonObject;
            var $$c=$$;
            
            //MethodDefinition foobar at nesting.ceylon (86:12-88:12)
            function foobar(){
                return getFoo();
            }
            $$.foobar=foobar;
            
            //MethodDefinition quxx at nesting.ceylon (89:12-91:12)
            function quxx(){
                return getQux();
            }
            $$.quxx=quxx;
            return $$;
        }
        $$.C=C;
        return $$;
    }
    $$.B=B;
    
    //MethodDefinition baz at nesting.ceylon (94:4-101:4)
    function baz(){
        
        //ClassDefinition Baz at nesting.ceylon (95:8-99:8)
        function Baz($$){
            if ($$===undefined)$$=new CeylonObject;
            var $$baz=$$;
            
            //MethodDefinition get at nesting.ceylon (96:12-98:12)
            function get(){
                return getFoo();
            }
            $$.get=get;
            return $$;
        }
        return Baz().get();
    }
    $$.baz=baz;
    return $$;
}

//MethodDefinition test at nesting.ceylon (104:0-119:0)
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
