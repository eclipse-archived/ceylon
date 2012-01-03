var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-18:0)
function Outer(name, $$){
    if ($$===undefined)$$=new CeylonObject;
    var $$outer=$$;
    
    //AttributeDeclaration int at nesting.ceylon (2:4-2:18)
    var $int=$$$cl15.Integer(10);
    function getInt(){
        return $int;
    }
    
    //AttributeDeclaration float at nesting.ceylon (3:4-3:34)
    var $float=getInt().getFloat();
    function getFloat(){
        return $float;
    }
    $$.getFloat=getFloat;
    
    //ClassDefinition Inner at nesting.ceylon (4:4-14:4)
    function Inner($$){
        if ($$===undefined)$$=new CeylonObject;
        var $$inner=$$;
        
        //MethodDefinition printName at nesting.ceylon (5:8-7:8)
        function printName(){
            $$$cl15.print(name);
        }
        
        //AttributeGetterDefinition int at nesting.ceylon (8:8-10:8)
        function getInt(){
            return $$outer.getInt();
        }
        $$.getInt=getInt;
        
        //AttributeGetterDefinition float at nesting.ceylon (11:8-13:8)
        function getFloat(){
            return $$outer.getFloat();
        }
        $$.getFloat=getFloat;
        return $$;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (15:4-15:25)
    var $inner=Inner();
    function getInner(){
        return $inner;
    }
    $$$cl15.print(getInner().getInt());
    $$$cl15.print(getInner().getFloat());
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
