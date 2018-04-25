void parameterVariance() {
    class XX0<in T>(XX0<T> other, t) {
        $error T t;
        T tt = other.t;    
    }
    
    class XX1<in T>(XX1<T> other, $error T t) {
        T tt = other.t;
    }
    
    class XX2<in T>(XX2<T> other, $error shared T t) {}
    
    class XX3<in T>(XX3<T> other) {
        T t = nothing;
    }
    
    class YYY0<out T>(YYY0<T> other, T t) {
        void f($error T t) => print(t);
        other.f(t);
    }
    
    class YYY1<out T>(YYY1<T> other, T t) {
        void f($error variable T t) => print(t);
        other.f(t);
    }
    
    class YYY2<out T>(YYY2<T> other, T t) {
        void f(T t) => print(t);
    }
    
    interface ZZZ<in T> {
        shared void f(g) {
            void g($error T t);
        }
    }
    
    interface ZZZ2<in T> {
        shared void f(void g($error T t)) {}
    }
    
    interface WWW<out T> {
        shared void f(t, tt) {
            $error T t;
            void tt(void ttt($error T t));
        }
    }
    
    interface WWW2<out T> {
        shared void f($error T t, void tt(void ttt($error T t))) {}
    }
}


void sharedParameterVariance() {
    class Sup<out T>(shared void x($error T t)) {}
    
    class Sub() extends Sup<String>(print) {}
    
    (Sub() of Sup<String?>).x(null);
    
    class Bad1<out T, in S>($error shared S(T) y) {}
    class Bad2<out T, in S>($error shared S y($error T t)) {}
    class Bad3<out T, in S>(shared void y($error T t($error S s))) {}

    class Ok1<out T, in S>(S(T) y) {}
    class Ok2<out T, in S>(S y(T t)) {}
    class Ok3<out T, in S>(void y(T t(S s))) {}
}
