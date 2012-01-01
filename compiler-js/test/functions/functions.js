var $$cl15=require('ceylon/language/0.1/ceylon.language');

//function helloWorld at functions.ceylon (1:0-3:0)
function helloWorld(){
    $$cl15.print($$cl15.String("hello world"));
}
this.helloWorld=helloWorld;

//function hello at functions.ceylon (5:0-7:0)
function hello(name){
    $$cl15.print($$cl15.String("hello").plus(name));
}
this.hello=hello;

//function helloAll at functions.ceylon (9:0-9:39)
function helloAll(names){}
this.helloAll=helloAll;

//function toString at functions.ceylon (11:0-13:0)
function toString(obj){
    return obj.getString();
}
this.toString=toString;

//function add at functions.ceylon (15:0-17:0)
function add(x,y){
    return x.plus(y);
}
this.add=add;

//function repeat at functions.ceylon (15:0-17:0)
function repeat(times,f){
    f($$cl15.Integer(0));
}
this.repeat=repeat;
