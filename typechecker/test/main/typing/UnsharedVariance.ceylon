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
