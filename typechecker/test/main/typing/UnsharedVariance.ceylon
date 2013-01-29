class Consumer<in X>() {
    shared void consume(X x) {}
}
class Capturing0<out T>(T t, Capturing1<T> c) {
    Consumer<T> consumer=Consumer<T>();
    shared void callIt() {}
}
class Capturing1<T>(T t, Capturing1<T> c) {
    Consumer<T> consumer=Consumer<T>();
    shared void callIt() { 
        c.consumer.consume(t);
    }
}
class Capturing2<out T>(T t, Capturing2<T> c) {
    @error Consumer<T> consumer=Consumer<T>();
    shared void callIt() { 
        c.consumer.consume(t);
    }
}
class Capturing3<in T>(T t, Capturing3<T> c) {
    Consumer<T> consumer=Consumer<T>();
    shared void callIt() { 
        c.consumer.consume(t);
    }
}
class Capturing4<T>(T t, Capturing4<T> c) {
    @error Consumer<T> consumer;
    shared void callIt() { 
        c.consumer.consume(t);
    }
}
class Capturing5<out T>(T t, Capturing4<T> c) {
    Consumer<T> consumer;
    shared void callIt() {}
}


void unsharedVariance() {
    class Bar<out T>(T t, Bar<T> bar) {
        void accept(T tt) {}
        accept(t);
        //bar.accept(t);
    }    
    class Foo<in T>(T t, Foo<T> foo) {
         T get() => t;
         print(get());
         //foo.get();
    }
    class Baz<out T>(T t, Foo<T> foo) {
         class Inner(T t) {}
         print(Inner(t));
         //foo.Inner(t);
    }
}
void backdoorVariance() {
    class Bar<out T>(T t, Bar<T> bar) {
        void accept(@error T tt) {}
        accept(t);
        bar.accept(t);
    }    
    class Foo<in T>(T t, Foo<T> foo) {
         @error T get() => t;
         print(get());
         foo.get();
    }
    class Baz<out T>(T t, Baz<T> baz) {
         class Inner(@error T t) {}
         print(Inner(t));
         baz.Inner(t);
    }
}