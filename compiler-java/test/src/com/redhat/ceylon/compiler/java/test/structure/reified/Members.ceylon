shared class OuterClass<Outer>(){
    shared class InnerClass<Inner>(){}
    shared void outerMethod(Object o){
        class InnerClass<Inner>(){}
        interface InnerInterface<Inner>{}
        if(is InnerClass<Integer> o){}
        if(is InnerInterface<Integer> o){}
    }
}

shared interface OuterInterface<Outer>{
    shared interface InnerInterface<Inner>{}
}

shared void outerMethod(Object o){
    class InnerClass<Inner>(){}
    interface InnerInterface<Inner>{}
    if(is InnerClass<Integer> o){}
    if(is InnerInterface<Integer> o){}
    class InnerClass2(){}
}

shared class OuterClass2(){
    shared class InnerClass(){}
}

shared void testMembers(Object o){
    if(is OuterClass<Integer>.InnerClass<String> o){}
}