
//value firstName at attributes.ceylon (1:0-1:26)
var $firstName=new String("Gavin");
function getFirstName(){
    return $firstName;
}

//value lastName at attributes.ceylon (3:0-5:0)
function getLastName(){
    return new String("King");
}

//assign lastName at attributes.ceylon (7:0-9:0)
function setLastName(lastName){
    print(lastName);
}
