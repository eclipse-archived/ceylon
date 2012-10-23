shared class MethodClass(){
    shared void method<T>(){
        method<T>();
        method<Integer>();
    }
    shared Callable<Void> methodSpec = method<Integer>;
    
    void test(){
        methodSpec();
    }
    
    shared void methodWithOverloads<T>(Integer a = 2, Integer b = 3){}
    
    void test2(){
        methodWithOverloads<Integer>{};
    }
}

shared void method<T>(){
    method<T>();
    method<Integer>();
}