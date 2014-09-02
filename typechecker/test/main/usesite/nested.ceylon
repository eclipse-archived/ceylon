void nested() {
    class Outer<T>(variable T t) {
        shared class Inner() {
            shared T get() => t;
            shared void set(T t) => outer.t = t;
        }
    }
    Outer<out Object>.Inner o = Outer("").Inner();
    Object obj = o.get();
    @error o.set("");
}