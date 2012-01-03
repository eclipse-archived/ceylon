var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-18:0)
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

//AttributeDeclaration inner at nesting.ceylon (15:4-15:25)
$Outer.prototype.getInner=function getInner(){
    return this.inner;
}
function Outer(name, $$){
    if ($$===undefined)$$=new $Outer;
    var $$outer=$$;
    $$.name=name;
    
    //AttributeDeclaration int at nesting.ceylon (2:4-2:18)
    $$.int=$$$cl15.Integer(10);
    
    //AttributeDeclaration float at nesting.ceylon (3:4-3:34)
    $$.float=$$.getInt().getFloat();
    
    //ClassDefinition Inner at nesting.ceylon (4:4-14:4)
    function $Inner(){}
    for(var $ in CeylonObject.prototype){$Inner.prototype[$]=CeylonObject.prototype[$]}
    for(var $ in CeylonObject.prototype){$Inner.prototype[$+'$']=CeylonObject.prototype[$]}
    
    //MethodDefinition printName at nesting.ceylon (5:8-7:8)
    $Inner.prototype.printName=function printName(){
        $$$cl15.print($$.name);
    }
    
    //AttributeGetterDefinition int at nesting.ceylon (8:8-10:8)
    $Inner.prototype.getInt=function getInt(){
        return $$outer.getInt();
    }
    
    //AttributeGetterDefinition float at nesting.ceylon (11:8-13:8)
    $Inner.prototype.getFloat=function getFloat(){
        return $$outer.getFloat();
    }
    function Inner($$){
        if ($$===undefined)$$=new $Inner;
        var $$inner=$$;
        return $$;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (15:4-15:25)
    $$.inner=Inner();
    $$$cl15.print($$.getInner().getInt());
    $$$cl15.print($$.getInner().getFloat());
    return $$;
}
this.Outer=Outer;

//MethodDefinition outr at nesting.ceylon (20:0-30:0)
function outr(name){
    
    //AttributeDeclaration uname at nesting.ceylon (21:4-21:34)
    var $uname=name.getUppercased();
    function getUname(){
        return $uname;
    }
    
    //MethodDefinition inr at nesting.ceylon (22:4-24:4)
    function inr(){
        return name;
    }
    
    //AttributeGetterDefinition uinr at nesting.ceylon (25:4-27:4)
    function getUinr(){
        return getUname();
    }
    
    //AttributeDeclaration result at nesting.ceylon (28:4-28:25)
    var $result=inr();
    function getResult(){
        return $result;
    }
    
    //AttributeDeclaration uresult at nesting.ceylon (29:4-29:25)
    var $uresult=getUinr();
    function getUresult(){
        return $uresult;
    }
    
}
this.outr=outr;
