shared class MethodClass(){
    shared void method<T>(){
        method<T>();
        method<Integer>();
        method();
    }
    shared Callable<Anything,[]> methodSpec = method<Integer>;
    
    void test(){
        methodSpec();
    }
    
    shared void methodWithOverloads<T>(Integer a = 2, Integer b = 3){}
    
    void test2(){
        methodWithOverloads<Integer>{};
        methodWithOverloads<Integer>();
        methodWithOverloads{};
        methodWithOverloads();
    }
}

shared void method<T>(){
    method<T>();
    method<Integer>();
    method();
}