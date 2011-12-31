
//function helloWorld at functions.ceylon (1:0-3:0)
function helloWorld(){
    print(new String("hello world"));
}

//function hello at functions.ceylon (5:0-7:0)
function hello(name){
    print(new String("hello").plus(name));
}

//function helloAll at functions.ceylon (9:0-9:32)
function helloAll(names){}

//function toString at functions.ceylon (11:0-13:0)
function toString(obj){
    return obj.getString();
}

//function add at functions.ceylon (15:0-17:0)
function add(x,y){
    return x.plus(y);
}
