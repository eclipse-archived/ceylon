void parameterVariance() {
    class XX0<in T>(XX0<T> other, t) {
        @error T t;
        T tt = other.t;    
    }
    
    class XX1<in T>(XX1<T> other, @error T t) {
        T tt = other.t;
    }
    
    class XX2<in T>(XX2<T> other, @error shared T t) {}
    
    class XX3<in T>(XX3<T> other) {
        T t = nothing;
    }
    
    class YYY0<out T>(YYY0<T> other, T t) {
        void f(@error T t) => print(t);
        other.f(t);
    }
    
    class YYY1<out T>(YYY1<T> other, T t) {
        void f(T t) => print(t);
    }
    
    interface ZZZ<in T> {
        shared void f(g) {
            void g(@error T t);
        }
    }
    
    interface ZZZ2<in T> {
        shared void f(void g(@error T t)) {}
    }
    
    interface WWW<out T> {
        shared void f(t, tt) {
            @error T t;
            void tt(void ttt(@error T t));
        }
    }
    
    interface WWW2<out T> {
        shared void f(@error T t, void tt(void ttt(@error T t))) {}
    }
}