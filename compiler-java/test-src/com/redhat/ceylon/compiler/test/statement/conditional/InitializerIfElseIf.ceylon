@nomodel
class InitializerIfElseIf(){
    variable Boolean b;
    if(true){
        b := true;
    }else if(false){
        b := false;
    }else{
        b := true;
    }
}