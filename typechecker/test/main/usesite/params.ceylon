void example() {
    ClassOuter<in String> cInString 
            = ClassOuter<Object>(10);
    ClassOuter<in String>.ClassInner dInString 
            = cInString.ClassInner {
        $error void consume(String a) {}
        function produce() => "";
    };
    
    ClassOuter<out Object> cOutAnything 
            = ClassOuter<Integer>(10);
    ClassOuter<out Object>.ClassInner dOutAnything 
            = cOutAnything.ClassInner {
        $error void consume(Nothing a) {}
        function produce() => nothing;
    };
}

shared class ClassOuter<T>(t) {
    shared variable T t;
    shared class ClassInner(consume, produce) {
        shared void consume(T t);
        shared T produce();
    }
}