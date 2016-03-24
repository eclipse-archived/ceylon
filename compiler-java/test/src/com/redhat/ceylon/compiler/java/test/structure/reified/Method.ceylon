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
    shared formal void m();
}

shared class MethodInterfaceImpl(MethodClass c) satisfies MethodInterface {
    m = c.method2<Integer>;
}

shared void method<T>(){
    method<T>();
    method<Integer>();
    method();
}