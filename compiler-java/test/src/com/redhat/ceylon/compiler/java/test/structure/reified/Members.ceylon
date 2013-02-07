shared class OuterClass<Outer>(){
    shared class InnerClass<Inner>(){}
}

shared interface OuterInterface<Outer>{
    shared interface InnerInterface<Inner>{}
}

shared void testMembers(Object o){
    if(is OuterClass<Integer>.InnerClass<String> o){}
}