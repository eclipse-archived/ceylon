var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//AttributeDeclaration firstName at attributes.ceylon (1:0-1:26)
var $firstName=$$$cl15.String("Gavin");
function getFirstName(){
    return $firstName;
}

//AttributeGetterDefinition lastName at attributes.ceylon (3:0-5:0)
function getLastName(){
    return $$$cl15.String("King");
}

//AttributeSetterDefinition lastName at attributes.ceylon (7:0-9:0)
function setLastName(lastName){
    $$$cl15.print(lastName);
}
