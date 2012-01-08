var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-26:0)
function $Outer(){}
for(var $ in CeylonObject.prototype){$Outer.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Outer.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration int at nesting.ceylon (2:4-2:18)
$Outer.prototype.getInt$Outer=function getInt$Outer(){
    return this.int$Outer;
}

//AttributeDeclaration float at nesting.ceylon (3:4-3:34)
$Outer.prototype.getFloat=function getFloat(){
    return this.float;
}

//MethodDefinition noop at nesting.ceylon (4:4-4:17)
$Outer.prototype.noop$Outer=function noop$Outer(){}

//AttributeDeclaration inner at nesting.ceylon (19:4-19:25)
$Outer.prototype.getInner$Outer=function getInner$Outer(){
    return this.inner$Outer;
}
function Outer(name$Outer, $$outer){
    if ($$outer===undefined)$$outer=new $Outer;
    $$outer.name$Outer=name$Outer;
    
    //AttributeDeclaration int at nesting.ceylon (2:4-2:18)
    $$outer.int$Outer=$$$cl15.Integer(10);
    
    //AttributeDeclaration float at nesting.ceylon (3:4-3:34)
    $$outer.float=$$outer.getInt$Outer().getFloat();
    
    //ClassDefinition Inner at nesting.ceylon (5:4-18:4)
    function $Inner(){}
    for(var $ in CeylonObject.prototype){$Inner.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$Inner.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //MethodDefinition printName at nesting.ceylon (6:8-8:8)
    $Inner.prototype.printName$Inner=function printName$Inner(){
        var $$inner=this;
        $$$cl15.print($$outer.name$Outer);
    }
    
    //AttributeGetterDefinition int at nesting.ceylon (9:8-11:8)
    $Inner.prototype.getInt=function getInt(){
        var $$inner=this;
        return $$outer.getInt$Outer();
    }
    
    //AttributeGetterDefinition float at nesting.ceylon (12:8-14:8)
    $Inner.prototype.getFloat=function getFloat(){
        var $$inner=this;
        return $$outer.getFloat();
    }
    
    //MethodDefinition noop at nesting.ceylon (15:8-17:8)
    $Inner.prototype.noop=function noop(){
        var $$inner=this;
        $$outer.noop$Outer();
    }
    function Inner$Outer($$inner){
        if ($$inner===undefined)$$inner=new $Inner;
        return $$inner;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (19:4-19:25)
    $$outer.inner$Outer=Inner$Outer();
    $$$cl15.print($$outer.getInner$Outer().getInt());
    $$$cl15.print($$outer.getInner$Outer().getFloat());
    $$outer.getInner$Outer().noop();
    $$outer.noop$Outer();
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
function $Holder(){}
for(var $ in CeylonObject.prototype){$Holder.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Holder.prototype[$+'$']=CeylonObject.prototype[$]}

//MethodDefinition get at nesting.ceylon (43:4-45:4)
$Holder.prototype.get=function get(){
    var $$holder=this;
    return $$holder.o$Holder;
}

//AttributeGetterDefinition string at nesting.ceylon (46:4-48:4)
$Holder.prototype.getString=function getString(){
    var $$holder=this;
    return $$holder.o$Holder.getString();
}
function Holder(o$Holder, $$holder){
    if ($$holder===undefined)$$holder=new $Holder;
    $$holder.o$Holder=o$Holder;
    return $$holder;
}
this.Holder=Holder;

//ClassDefinition Wrapper at nesting.ceylon (51:0-59:0)
function $Wrapper(){}
for(var $ in CeylonObject.prototype){$Wrapper.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Wrapper.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration o at nesting.ceylon (52:4-52:18)
$Wrapper.prototype.getO$Wrapper=function getO$Wrapper(){
    return this.o$Wrapper;
}

//MethodDefinition get at nesting.ceylon (53:4-55:4)
$Wrapper.prototype.get=function get(){
    var $$wrapper=this;
    return $$wrapper.getO$Wrapper();
}

//AttributeGetterDefinition string at nesting.ceylon (56:4-58:4)
$Wrapper.prototype.getString=function getString(){
    var $$wrapper=this;
    return $$wrapper.getO$Wrapper().getString();
}
function Wrapper($$wrapper){
    if ($$wrapper===undefined)$$wrapper=new $Wrapper;
    
    //AttributeDeclaration o at nesting.ceylon (52:4-52:18)
    $$wrapper.o$Wrapper=$$$cl15.Integer(100);
    return $$wrapper;
}
this.Wrapper=Wrapper;

//ClassDefinition Unwrapper at nesting.ceylon (61:0-69:0)
function $Unwrapper(){}
for(var $ in CeylonObject.prototype){$Unwrapper.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$Unwrapper.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration o at nesting.ceylon (62:4-62:27)
$Unwrapper.prototype.getO=function getO(){
    return this.o;
}

//MethodDefinition get at nesting.ceylon (63:4-65:4)
$Unwrapper.prototype.get=function get(){
    var $$unwrapper=this;
    return $$unwrapper.getO();
}

//AttributeGetterDefinition string at nesting.ceylon (66:4-68:4)
$Unwrapper.prototype.getString=function getString(){
    var $$unwrapper=this;
    return $$unwrapper.getO().getString();
}
function Unwrapper($$unwrapper){
    if ($$unwrapper===undefined)$$unwrapper=new $Unwrapper;
    
    //AttributeDeclaration o at nesting.ceylon (62:4-62:27)
    $$unwrapper.o=$$$cl15.Float(23.56);
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
function $A(){}
for(var $ in CeylonObject.prototype){$A.prototype[$]=CeylonObject.prototype[$]}
for(var $ in CeylonObject.prototype){$A.prototype[$+'$']=CeylonObject.prototype[$]}

//AttributeDeclaration foo at nesting.ceylon (83:4-83:22)
$A.prototype.getFoo$A=function getFoo$A(){
    return this.foo$A;
}

//MethodDefinition baz at nesting.ceylon (95:4-102:4)
$A.prototype.baz=function baz(){
    var $$a=this;
    
    //ClassDefinition Baz at nesting.ceylon (96:8-100:8)
    function $Baz(){}
    for(var $ in CeylonObject.prototype){$Baz.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$Baz.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //MethodDefinition get at nesting.ceylon (97:12-99:12)
    $Baz.prototype.get=function get(){
        var $$baz=this;
        return $$a.getFoo$A();
    }
    function Baz($$baz){
        if ($$baz===undefined)$$baz=new $Baz;
        return $$baz;
    }
    return Baz().get();
}
function A($$a){
    if ($$a===undefined)$$a=new $A;
    
    //AttributeDeclaration foo at nesting.ceylon (83:4-83:22)
    $$a.foo$A=$$$cl15.String("foo");
    
    //ClassDefinition B at nesting.ceylon (84:4-94:4)
    function $B(){}
    for(var $ in CeylonObject.prototype){$B.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$B.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //AttributeDeclaration qux at nesting.ceylon (85:8-85:26)
    $B.prototype.getQux$B=function getQux$B(){
        return this.qux$B;
    }
    function B($$b){
        if ($$b===undefined)$$b=new $B;
        
        //AttributeDeclaration qux at nesting.ceylon (85:8-85:26)
        $$b.qux$B=$$$cl15.String("qux");
        
        //ClassDefinition C at nesting.ceylon (86:8-93:8)
        function $C(){}
        for(var $ in CeylonObject.prototype){$C.prototype[$]=CeylonObject.prototype[$]}
        for(var $ in CeylonObject.prototype){$C.prototype[$+'$']=CeylonObject.prototype[$]}
        
        //MethodDefinition foobar at nesting.ceylon (87:12-89:12)
        $C.prototype.foobar=function foobar(){
            var $$c=this;
            return $$a.getFoo$A();
        }
        
        //MethodDefinition quxx at nesting.ceylon (90:12-92:12)
        $C.prototype.quxx=function quxx(){
            var $$c=this;
            return $$b.getQux$B();
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
