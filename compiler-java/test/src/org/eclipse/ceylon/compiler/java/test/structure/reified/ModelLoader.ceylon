void reifiedInstantiateFromModelLoader(){
    value c = Class<Integer,String>();
    value c2 = Class<Integer,String>{};
    value constr = Class<Integer,String>;
    value c3 = constr(3,4);
    value c4 = Class<Integer,String>{b = 5;};
    value c5 = Class();
}

void reifiedMethodsFromModelLoader(){
    method<Integer>();
    method();
    value mc = MethodClass();
    mc.method<Integer>();
    mc.method();
    mc.methodSpec();
    mc.methodWithOverloads<Integer>();
    mc.methodWithOverloads<Integer>(1,2);
    mc.methodWithOverloads(1,2);
}
