/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
class C<T> {
    void m(T t) {return;}
    final T m$t() {return null;}
}

interface I<T> {
    void m(T t);
}
final class I$impl {
    static final <T> T m$t(I<T> $this) {return null;}
}

class CInit<T> {
    CInit(T t){return;}
}
class CInit$impl {
    static final <T> T $init$t() {return null;}
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
    static final class II$impl {
        static final <T,U> T m$t(OC$II<T,U> $this, OC<T> $outer) {return $outer.m();}
        static final <T,U> U m$u(OC$II<T,U> $this, OC<T> $outer, T t) {return null;}
    }
    
    class ICInit<U> {
        ICInit(T t, U u) {return;}
    }
    static class ICInit$impl {
        static final <T> T $init$t(OC<T> $outer) {return $outer.m();}
        static final <T,U> U $init$u(OC<T> $outer, T t) {return null;}
    }
    T m() {return null;}
}

interface OC$II<T,U> {// note we have to copy all the typeparams in sope at the decl  
    void m(T t, U u);
}


interface OI<T> {
    T m();
    class IC<T,U> {
        void m(T t, U u) {return;}
        final T m$t(OI<T> $outer) {return $outer.m();}
        final U m$u(OI<T> $outer, T t) {return null;}
    }
    
    //inner interface implicity static, see II$II below
    //interface II<T,U> {
    //    void m(T t, U u);
    //}
    static final class II$impl {
        static final <T,U> T m$t(II$II<T,U> $this, OI<T> $outer) {return $outer.m();}
        static final <T,U> U m$u(II$II<T,U> $this, OI<T> $outer, T t) {return null;}
    }
    
    class ICInit<T,U> {
        ICInit(T t, U u) {return;}
    }
    static final class ICInit$impl<T,U> {
        static final <T> T $init$t(OI<T> $outer) {return $outer.m();}
        static final <T,U> U $init$u(OI<T> $outer, T t) {return null;}
    }
}

interface II$II<T,U> {// note we have to copy all the typeparams in sope at the decl  
    void m(T t, U u);
}

class Local<T> {
    T m() {return null;}
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
        // Example invocation of Local$II (really we use a Let)
        {
            Local$II<S,T,Integer> instance = new Local$II<S,T,Integer>() {
                public void m(T t, Integer u) {}
            };
            T m$t = local$II$impl.m$t(instance);
            Integer m$u = local$II$impl.m$u(instance, t);
            instance.m(m$t, m$u);
        }
        class LCInit<U> {
            LCInit(S s, T t, U u) {return;}
        }
        // Example instantiation of LCInit (really we use a Let)
        {
            S $init$s = local$LCInit$impl.$init$s(this);
            T $init$t = local$LCInit$impl.$init$t(this, $init$s);
            Integer $init$u = local$LCInit$impl.$init$u(this, $init$s, $init$t);
            LCInit<Integer> instance = new LCInit<Integer>($init$s, $init$t, $init$u);
        }
    }

    static final class local$II$impl {
        static final <S,T,U> T m$t(Local$II<S,T,U> $this) {return null;}
        static final <S,T,U> U m$u(Local$II<S,T,U> $this, T t) {return null;}
    }
    
    static final class local$LCInit$impl {
        static final <S,T> S $init$s(Local<T> $outer) {return null;}// example use of closure
        static final <S,T> T $init$t(Local<T> $outer, S $s) {return $outer.m();}// example use of closure
        static final <U,S,T> U $init$u(Local<T> $outer, S $s, T $t) {return null;}   
    }
}

interface Local$II<S,T,U> {
    void m(T t, U u);
}
