var $$$cl1=require('ceylon/language/0.3/ceylon.language');

//MethodDefinition m at test.ceylon (1:0-16:0)
function m(){
    
    //MethodDefinition printIf1 at test.ceylon (2:0-8:0)
    function printIf1$2(s$3){
        var s$4;
        if((s$4=s$3)!==null){
            $$$cl1.print(s$4);
        }
        else {
            $$$cl1.print($$$cl1.String("Nothing to print.",17));
        }
        
    }
    
    //AttributeDeclaration s1 at test.ceylon (9:0-9:17)
    var s1$5=$$$cl1.getNull();
    
    //AttributeDeclaration s2 at test.ceylon (10:0-10:25)
    var s2$6=$$$cl1.String("I do exist",10);
    printIf1$2(s1$5);
    printIf1$2(s2$6);
    $$$cl1.print((opt$7=s1$5,opt$7!==null?opt$7:$$$cl1.String("Nothing here...",15)));
    var opt$7;
    $$$cl1.print((opt$8=s2$6,opt$8!==null?opt$8:$$$cl1.String("Nothing here...",15)));
    var opt$8;
}
