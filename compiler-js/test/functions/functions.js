var $$$cl15=require('ceylon/language/0.1/ceylon.language');

//MethodDefinition helloWorld at functions.ceylon (1:0-3:0)
function helloWorld(){
    $$$cl15.print($$$cl15.String("hello world",11));
}
this.helloWorld=helloWorld;

//MethodDefinition hello at functions.ceylon (5:0-7:0)
function hello(name){
    $$$cl15.print($$$cl15.String("hello",5).plus(name));
}
this.hello=hello;

//MethodDefinition helloAll at functions.ceylon (9:0-9:39)
function helloAll(names){}
this.helloAll=helloAll;

//MethodDefinition toString at functions.ceylon (11:0-13:0)
function toString(obj){
    return obj.getString();
}
this.toString=toString;

//MethodDefinition add at functions.ceylon (15:0-17:0)
function add(x,y){
    return x.plus(y);
}
this.add=add;

//MethodDefinition repeat at functions.ceylon (19:0-21:0)
function repeat(times,f){
    f($$$cl15.Integer(0));
}
this.repeat=repeat;
