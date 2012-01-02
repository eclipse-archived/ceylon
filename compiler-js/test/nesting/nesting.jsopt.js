var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//ClassDefinition Outer at nesting.ceylon (1:0-8:0)
function $Outer(){}

//AttributeDeclaration inner at nesting.ceylon (7:4-7:25)
$Outer.prototype.getInner=function getInner(){
    return this.inner;
}
function Outer(name){
    var $$outer=new $Outer;
    $$outer.name=name;
    
    //ClassDefinition Inner at nesting.ceylon (2:4-6:4)
    function $Inner(){}
    
    //MethodDefinition printName at nesting.ceylon (3:8-5:8)
    $Inner.prototype.printName=function printName(){
        $$$cl15.print(this.name);
    }
    function Inner(){
        var $$inner=new $Inner;
        return $$inner;
    }
    
    //AttributeDeclaration inner at nesting.ceylon (7:4-7:25)
    $$outer.inner=$$outer.Inner();
    return $$outer;
}

//MethodDefinition outr at nesting.ceylon (10:0-15:0)
function outr(name){
    
    //MethodDefinition inr at nesting.ceylon (11:4-13:4)
    function inr(){
        return name;
    }
    
    //AttributeDeclaration result at nesting.ceylon (14:4-14:25)
    var $result=inr();
    function getResult(){
        return $result;
    }
    
}
