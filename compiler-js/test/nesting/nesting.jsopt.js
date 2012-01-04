var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-25:0)
function $Outer(){}
for(var $ in CeylonObject.prototype){$Outer.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Outer.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration int at nesting.ceylon (2:4-2:18)
$Outer.prototype.getInt=function getInt(){
    return this.int;
}

//AttributeDeclaration float at nesting.ceylon (3:4-3:34)
$Outer.prototype.getFloat=function getFloat(){
    return this.float;
}

//MethodDefinition noop at nesting.ceylon (4:4-4:17)
$Outer.prototype.noop=function noop(){}

//AttributeDeclaration inner at nesting.ceylon (19:4-19:25)
$Outer.prototype.getInner=function getInner(){
    return this.inner;
}
function Outer(name, $$outer){
    if ($$outer===undefined)$$outer=new $Outer;
    $$outer.name=name;
    
    //AttributeDeclaration int at nesting.ceylon (2:4-2:18)
    $$outer.int=$$$cl15.Integer(10);
    
    //AttributeDeclaration float at nesting.ceylon (3:4-3:34)
    $$outer.float=$$outer.getInt().getFloat();
    
    //ClassDefinition Inner at nesting.ceylon (5:4-18:4)
    function $Inner(){}
    for(var $ in CeylonObject.prototype){$Inner.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$Inner.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //MethodDefinition printName at nesting.ceylon (6:8-8:8)
    $Inner.prototype.printName=function printName(){
        var $$inner=this;
        $$$cl15.print($$outer.name);
    }
    
    //AttributeGetterDefinition int at nesting.ceylon (9:8-11:8)
    $Inner.prototype.getInt=function getInt(){
        var $$inner=this;
        return $$outer.getInt();
    }
    
    //AttributeGetterDefinition float at nesting.ceylon (12:8-14:8)
    $Inner.prototype.getFloat=function getFloat(){
        var $$inner=this;
        return $$outer.getFloat();
    }
    
    //MethodDefinition noop at nesting.ceylon (15:8-17:8)
    $Inner.prototype.noop=function noop(){
        var $$inner=this;
        $$outer.noop();
    }
    function Inner($$inner){
        if ($$inner===undefined)$$inner=new $Inner;
        return $$inner;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (19:4-19:25)
    $$outer.inner=Inner();
    $$$cl15.print($$outer.getInner().getInt());
    $$$cl15.print($$outer.getInner().getFloat());
    return $$outer;
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
function $Holder(){}
for(var $ in CeylonObject.prototype){$Holder.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Holder.prototype[$+'$']=CeylonObject.prototype[$]}

//MethodDefinition get at nesting.ceylon (42:4-44:4)
$Holder.prototype.get=function get(){
    var $$holder=this;
    return $$holder.o;
}

//AttributeGetterDefinition string at nesting.ceylon (45:4-47:4)
$Holder.prototype.getString=function getString(){
    var $$holder=this;
    return $$holder.o.getString();
}
function Holder(o, $$holder){
    if ($$holder===undefined)$$holder=new $Holder;
    $$holder.o=o;
    return $$holder;
}
this.Holder=Holder;

//ClassDefinition Wrapper at nesting.ceylon (50:0-58:0)
function $Wrapper(){}
for(var $ in CeylonObject.prototype){$Wrapper.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Wrapper.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration o at nesting.ceylon (51:4-51:18)
$Wrapper.prototype.getO=function getO(){
    return this.o;
}

//MethodDefinition get at nesting.ceylon (52:4-54:4)
$Wrapper.prototype.get=function get(){
    var $$wrapper=this;
    return $$wrapper.getO();
}

//AttributeGetterDefinition string at nesting.ceylon (55:4-57:4)
$Wrapper.prototype.getString=function getString(){
    var $$wrapper=this;
    return $$wrapper.getO().getString();
}
function Wrapper($$wrapper){
    if ($$wrapper===undefined)$$wrapper=new $Wrapper;
    
    //AttributeDeclaration o at nesting.ceylon (51:4-51:18)
    $$wrapper.o=$$$cl15.Integer(100);
    return $$wrapper;
}
this.Wrapper=Wrapper;

//ClassDefinition Unwrapper at nesting.ceylon (60:0-68:0)
function $Unwrapper(){}
for(var $ in CeylonObject.prototype){$Unwrapper.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Unwrapper.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration o at nesting.ceylon (61:4-61:27)
$Unwrapper.prototype.getO=function getO(){
    return this.o;
}

//MethodDefinition get at nesting.ceylon (62:4-64:4)
$Unwrapper.prototype.get=function get(){
    var $$unwrapper=this;
    return $$unwrapper.getO();
}

//AttributeGetterDefinition string at nesting.ceylon (65:4-67:4)
$Unwrapper.prototype.getString=function getString(){
    var $$unwrapper=this;
    return $$unwrapper.getO().getString();
}
function Unwrapper($$unwrapper){
    if ($$unwrapper===undefined)$$unwrapper=new $Unwrapper;
    
    //AttributeDeclaration o at nesting.ceylon (61:4-61:27)
    $$unwrapper.o=$$$cl15.Float(23.56);
    return $$unwrapper;
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
function $A(){}
for(var $ in CeylonObject.prototype){$A.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$A.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration foo at nesting.ceylon (82:4-82:22)
$A.prototype.getFoo=function getFoo(){
    return this.foo;
}

//MethodDefinition baz at nesting.ceylon (94:4-101:4)
$A.prototype.baz=function baz(){
    var $$a=this;
    
    //ClassDefinition Baz at nesting.ceylon (95:8-99:8)
    function $Baz(){}
    for(var $ in CeylonObject.prototype){$Baz.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$Baz.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //MethodDefinition get at nesting.ceylon (96:12-98:12)
    $Baz.prototype.get=function get(){
        var $$baz=this;
        return $$a.getFoo();
    }
    function Baz($$baz){
        if ($$baz===undefined)$$baz=new $Baz;
        return $$baz;
    }
    return Baz().get();
}
function A($$a){
    if ($$a===undefined)$$a=new $A;
    
    //AttributeDeclaration foo at nesting.ceylon (82:4-82:22)
    $$a.foo=$$$cl15.String("foo");
    
    //ClassDefinition B at nesting.ceylon (83:4-93:4)
    function $B(){}
    for(var $ in CeylonObject.prototype){$B.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$B.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //AttributeDeclaration qux at nesting.ceylon (84:8-84:26)
    $B.prototype.getQux=function getQux(){
        return this.qux;
    }
    function B($$b){
        if ($$b===undefined)$$b=new $B;
        
        //AttributeDeclaration qux at nesting.ceylon (84:8-84:26)
        $$b.qux=$$$cl15.String("qux");
        
        //ClassDefinition C at nesting.ceylon (85:8-92:8)
        function $C(){}
        for(var $ in CeylonObject.prototype){$C.prototype[$]=CeylonObject.prototype[$]}
        for(var $ in CeylonObject.prototype){$C.prototype[$+'$']=CeylonObject.prototype[$]}
        
        //MethodDefinition foobar at nesting.ceylon (86:12-88:12)
        $C.prototype.foobar=function foobar(){
            var $$c=this;
            return $$a.getFoo();
        }
        
        //MethodDefinition quxx at nesting.ceylon (89:12-91:12)
        $C.prototype.quxx=function quxx(){
            var $$c=this;
            return $$b.getQux();
        }
        function C($$c){
            if ($$c===undefined)$$c=new $C;
            return $$c;
        }
        $$b.C=C;
        return $$b;
    }
    $$a.B=B;
    return $$a;
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
