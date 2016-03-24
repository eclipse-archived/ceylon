@noanno
String reifiedCallable(Integer i){
    value c = reifiedCallable;
    return "";
}

@noanno
void reifiedCallable2(Boolean foo(String i)){
    reifiedCallable2{
        Boolean foo(String i){
            return false;
        }
    };
}

@noanno
void reifiedCallable3(Boolean foo(String i)(Integer i2)){
    reifiedCallable3{
        Boolean foo(String i)(Integer i2){
            return false;
        }
    };
}