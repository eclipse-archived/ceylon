package com.redhat.ceylon.compiler.java.codegen;

class C<T> {
    void m(T t) {return;}
    final T m$t() {return null;}
}

interface I<T> {
    void m(T t);
}
final class I$impl<T> {
    T m$t(I<T> $this) {return null;}
}

class CInit<T> {
    CInit(T t){return;}
    // see comment below (note we have to copy CInit's type parameters) 
    // static <T> T $init$t() {return null;}
}
final class CInit$impl<T> {
    // This is sub optimal because for a top level there's no
    // environment to capture yet for consistency with the inner and local class
    // cases we end up instantiating a $impl needlessly.
    // We could use a static method to optimize this common case
    // at the expense of complicating the instantiation. See above.
    T $init$t() {return null;}
}

class f {
    public static <T> void f(T t){return;}
    public static <T> T f$t() {return null;}
}

class OC<T> {
    class IC<U> {
        void m(T t, U u) {return;}
        final T m$t() {return null;}
        final U m$u(T t) {return null;}
    }
    
    //inner interface implicity static, see OC$II below
    //interface II<T,U> {
    //    void m(T t, U u);
    //}
    final class II$impl<U> {
        T m$t(OC$II<T,U> $this) {return null;}
        U m$u(OC$II<T,U> $this, T t) {return null;}
    }
    
    class ICInit<U> {
        ICInit(T t, U u) {return;}
    }
    final class ICInit$impl<U> {
        T $init$t() {return null;}
        U $init$u(T t) {return null;}
    }
}

interface OC$II<T,U> {// note we have to copy all the typeparams in sope at the decl  
    void m(T t, U u);
}


interface OI<T> {
    class IC<U> {
        void m(T t, U u) {return;}
        final T m$t() {return null;}
        final U m$u(T t) {return null;}
    }
    
    //inner interface implicity static, see II$II below
    //interface II<T,U> {
    //    void m(T t, U u);
    //}
    final class II$impl<U> {
        T m$t(II$II<T,U> $this) {return null;}
        U m$u(II$II<T,U> $this, T t) {return null;}
    }
    
    class ICInit<U> {
        ICInit(T t, U u) {return;}
    }
    final class ICInit$impl<U> {
        T $init$t() {return null;}
        U $init$u(T t) {return null;}
    }
}

interface II$II<T,U> {// note we have to copy all the typeparams in sope at the decl  
    void m(T t, U u);
}

class Local<T> {
    public <S> void local(final S s, final T t) {
        class LC<U> {
            void m(S s, T t, U u) {return;}
            final S m$s() {return s;}// example use of closure
            final T m$t(S $s) {return t;}// example use of closure
            final U m$u(S $s, T $t) {return null;}
            
            class ILC<V> {
                void m2(S s, T t, U u, V v) {return;}
                final S m2$s() {return s;}// example use of closure
                final T m2$t(S $s) {return t;}// example use of closure
                final U m2$u(S $s, T $t) {return null;}
                final V m2$v(S $s, T $t, U $u) {return null;}
            }
            // Example invocation of ILC.m2() (really we use a Let)
            {
                ILC<Integer> instance = new ILC<Integer>();
                S m2$s = instance.m2$s();
                T m2$t = instance.m2$t(m2$s);
                U m2$u = instance.m2$u(m2$s, m2$t);
                Integer m2$v = instance.m2$v(m2$s, m2$t, m2$u);
                instance.m2(m2$s, m2$t, m2$u, m2$v);
            }
        }
        // Example invocation of LC.m() (really we use a Let)
        {
            LC<Integer> instance = new LC<Integer>();
            S m$s = instance.m$s();
            T m$t = instance.m$t(m$s);
            Integer m$u = instance.m$u(m$s, m$t);
            instance.m(m$s, m$t, m$u);
        }
        
        /* Note: Java prohibits interfaces within inner classes/interfaces
        interface II<S,T,U> {
            void m(T t, U u);
        }*/
        class II$impl<U> {
            T m$t(Local$II<S,T,U> $this) {return null;}
            U m$u(Local$II<S,T,U> $this, T t) {return null;}
        }
        // Example invocation of Local$II (really we use a Let)
        {
            Local$II<S,T,Integer> instance = null;
            II$impl<Integer> impl = new II$impl<Integer>();
            T m$t = impl.m$t(instance);
            Integer m$u = impl.m$u(instance, t);
            instance.m(m$t, m$u);
        }
        class LCInit<U> {
            LCInit(S s, T t, U u) {return;}
        }
        final class LCInit$impl<U> {
            S $init$s() {return s;}// example use of closure
            T $init$t(S $s) {return t;}// example use of closure
            U $init$u(S $s, T $t) {return null;}   
        }
        // Example instantiation of LCInit (really we use a Let)
        {
            LCInit$impl<Integer> init = new LCInit$impl<Integer>();
            S $init$s = init.$init$s();
            T $init$t = init.$init$t($init$s);
            Integer $init$u = init.$init$u($init$s, $init$t);
            LCInit<Integer> instance = new LCInit<Integer>($init$s, $init$t, $init$u);
        }
    }
}

interface Local$II<S,T,U> {
    void m(T t, U u);
}