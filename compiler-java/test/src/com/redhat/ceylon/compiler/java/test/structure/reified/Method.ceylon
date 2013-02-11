shared class MethodClass(){
    shared void method<T>(){
        method<T>();
        method<Integer>();
        method();
    }
    shared Callable<Anything,[]> methodSpec = method<Integer>;
    
    shared Integer method2<T>() => 2;
    
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

shared interface MethodInterface {
    shared void methodWithOverloads<T>(Integer a = 2, Integer b = 3){}
}

shared class MethodInterfaceImpl() satisfies MethodInterface {}

shared void method<T>(){
    method<T>();
    method<Integer>();
    method();
}