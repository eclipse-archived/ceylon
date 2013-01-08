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
@nomodel
interface TPETop {
    shared formal void top();
    shared formal Integer topAttribute;
}
@nomodel
interface TPELeft satisfies TPETop {
    shared formal void left();
    shared formal Integer leftAttribute;
}
@nomodel
interface TPERight satisfies TPETop {
    shared formal void right();
    shared formal Integer rightAttribute;
}
@nomodel
class TPECLeft() satisfies TPELeft {
    shared actual void left() {}
    shared actual void top() {}
    shared actual Integer topAttribute = 1;
    shared actual Integer leftAttribute = 1;
}
@nomodel
class TPECMiddle() satisfies TPELeft & TPERight{
    shared actual void left() {}
    shared actual void top() {}
    shared actual void right() {}
    shared actual Integer topAttribute = 1;
    shared actual Integer leftAttribute = 1;
    shared actual Integer rightAttribute = 1;
}

@nomodel
class TypeParameterErasure() {

    T parameterized<T>(T t) {
        return t;
    }

    T parameterizedWithBounds<T>(T t)
    given T satisfies TPERight {
        t.right();
        t.right{};
        return t;
    }

    T parameterizedWithIntersectionBounds<T>(T t)
    given T satisfies TPELeft & TPERight {
        t.left();
        t.left{};
        t.right();
        t.right{};
        return t;
    }

    T parameterizedWithErasedBounds<T>(T t)
    given T satisfies Object {
        t.equals(t);
        return t;
    }

    T parameterizedWithParameterizedBounds<T>(T t) 
        given T satisfies Ordinal<T> & Comparable<T> {
        return t;
    }

    void testTypeParameters(){
        Integer i = parameterized<Integer>(+2);
        Integer i2 = parameterized(+2);
        Integer n = parameterized<Integer>(2);
        Integer n2 = parameterized(2);

        variable TPELeft&TPERight middle;
        variable TPELeft left;
        variable TPERight right;
        middle = TPECMiddle();
        
        left = parameterized(middle);
        right = parameterized(middle);
        middle = parameterized(middle);
        // with explicit non-erased bounds
        left = parameterized<TPELeft>(middle);
        right = parameterized<TPERight>(middle);
        // with explicit erased bounds
        left = parameterized<TPELeft&TPERight>(middle);
        right = parameterized<TPELeft&TPERight>(middle);
        middle = parameterized<TPELeft&TPERight>(middle);

        left = parameterizedWithBounds(middle);
        right = parameterizedWithBounds(middle);
        middle = parameterizedWithBounds(middle);

        left = parameterizedWithIntersectionBounds(middle);
        right = parameterizedWithIntersectionBounds(middle);
        middle = parameterizedWithIntersectionBounds(middle);

        String s = parameterizedWithErasedBounds("");
        String s2 = parameterizedWithErasedBounds<String>("");
        Object e = parameterizedWithErasedBounds("");
        Object e2 = parameterizedWithErasedBounds<Object>("");

        parameterizedWithParameterizedBounds<Integer>(2);
        parameterizedWithParameterizedBounds(2);
    }

    void testTypeParametersNamedArguments(){
        Integer i = parameterized<Integer>{t=+2;};
        Integer i2 = parameterized{t=+2;};
        Integer n = parameterized<Integer>{t=2;};
        Integer n2 = parameterized{t=2;};

        variable TPELeft&TPERight middle;
        variable TPELeft left;
        variable TPERight right;
        middle = TPECMiddle();
        
        left = parameterized{t=middle;};
        right = parameterized{t=middle;};
        middle = parameterized{t=middle;};
        // with explicit non-erased bounds
        left = parameterized<TPELeft>{t=middle;};
        right = parameterized<TPERight>{t=middle;};
        // with explicit erased bounds
        left = parameterized<TPELeft&TPERight>{t=middle;};
        right = parameterized<TPELeft&TPERight>{t=middle;};
        middle = parameterized<TPELeft&TPERight>{t=middle;};

        left = parameterizedWithBounds{t=middle;};
        right = parameterizedWithBounds{t=middle;};
        middle = parameterizedWithBounds{t=middle;};

        left = parameterizedWithIntersectionBounds{t=middle;};
        right = parameterizedWithIntersectionBounds{t=middle;};
        middle = parameterizedWithIntersectionBounds{t=middle;};

        String s = parameterizedWithErasedBounds{t = "";};
        String s2 = parameterizedWithErasedBounds<String>{t = "";};
        Object e = parameterizedWithErasedBounds{t = "";};
        Object e2 = parameterizedWithErasedBounds<Object>{t = "";};

        parameterizedWithParameterizedBounds<Integer>{t=2;};
        parameterizedWithParameterizedBounds{t=2;};
    }

    class Parameterized<T>(T t) {
        shared Inner parameterized<Inner>(Inner i, T t) {
            return i;
        }

        shared Inner parameterizedWithBounds<Inner>(Inner i, T t)
        given Inner satisfies TPELeft {
            i.left();
            i.left{};
            return i;
        }

        shared Inner parameterizedWithIntersectionBounds<Inner>(Inner i, T t)
        given Inner satisfies TPELeft & TPERight {
            i.left();
            i.left{};
            i.right();
            i.right{};
            return i;
        }
        shared Inner parameterizedWithErasedBounds<Inner>(Inner i, T t)
        given Inner satisfies Object {
            i.equals(i);
            return i;
        }
        shared Inner parameterizedWithParameterizedBounds<Inner>(Inner i, T t) 
            given Inner satisfies Ordinal<Inner> & Comparable<Inner> {
            i.compare(i);
            return i;
        }
    }

    class ParameterizedWithBounds<T>(T t)
    given T satisfies TPELeft {
        t.left();
        t.left{};
    }

    class ParameterizedWithIntersectionBounds<T>(T t)
    given T satisfies TPELeft & TPERight {
        t.left();
        t.left{};
        t.right();
        t.right{};
                
        void requiresCastLeft(TPELeft l){}

        TPELeft&TPERight middle = TPECMiddle();
        requiresCastLeft(t);
        requiresCastLeft{l = t;};
        requiresCastLeft(middle);
        requiresCastLeft{l = middle;};
    }

    class ParameterizedWithErasedBounds<T>(T t)
    given T satisfies Object {
    }

    class ParameterizedWithParameterizedBounds<T>(T t) 
        given T satisfies Ordinal<T> & 
                                Comparable<T> {
    }

    void testTypeParameterInstantiations(){
        TPELeft&TPERight middle = TPECMiddle();
        TPELeft left = TPECLeft();
        
        Parameterized<TPELeft&TPERight> parameterizedMiddle = Parameterized(middle);
        Parameterized<TPELeft> parameterizedLeft = Parameterized(left);

        Parameterized<TPELeft&TPERight> parameterizedExplicitMiddle = Parameterized<TPELeft&TPERight>(middle);
        Parameterized<TPELeft> parameterizedExplicitLeft = Parameterized<TPELeft>(left);

        ParameterizedWithBounds<TPELeft&TPERight> parameterizedWithBoundsMiddle = ParameterizedWithBounds(middle);
        ParameterizedWithBounds<TPELeft> parameterizedWithBoundsLeft = ParameterizedWithBounds(left);

        ParameterizedWithIntersectionBounds<TPELeft&TPERight> parameterizedWithIntersectionBoundsMiddle = ParameterizedWithIntersectionBounds(middle);
        ParameterizedWithIntersectionBounds<TPELeft&TPERight> parameterizedWithErasedExplicitBoundsMiddle = ParameterizedWithIntersectionBounds<TPELeft&TPERight>(middle);
        
        ParameterizedWithErasedBounds<String> parameterizedWithErasedBoundsMiddle = ParameterizedWithErasedBounds("");
        ParameterizedWithErasedBounds<String> parameterizedWithReallyErasedExplicitBoundsMiddle = ParameterizedWithErasedBounds<String>("");
        
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithParameterizedBounds = ParameterizedWithParameterizedBounds(2);
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithExplicitParameterizedBounds = ParameterizedWithParameterizedBounds<Integer>(2);
        
        parameterizedMiddle.parameterized(left, middle);
        parameterizedMiddle.parameterized(middle, middle);
        parameterizedMiddle.parameterized<TPELeft>(middle, middle);
        parameterizedMiddle.parameterized<TPELeft&TPERight>(middle, middle);

        parameterizedMiddle.parameterizedWithBounds(left, middle);
        parameterizedMiddle.parameterizedWithBounds(middle, middle);
        
        parameterizedMiddle.parameterizedWithIntersectionBounds(middle, middle);

        parameterizedMiddle.parameterizedWithErasedBounds("", middle);
        parameterizedMiddle.parameterizedWithErasedBounds<Object>("", middle);
        parameterizedMiddle.parameterizedWithErasedBounds<String>("", middle);

        parameterizedMiddle.parameterizedWithParameterizedBounds(2,middle);
        parameterizedMiddle.parameterizedWithParameterizedBounds<Integer>(2,middle);
    }

    void testTypeParameterInstantiationsNamedArguments(){
        TPELeft&TPERight middle = TPECMiddle();
        TPELeft left = TPECLeft();
        
        Parameterized<TPELeft&TPERight> parameterizedMiddle = Parameterized{t=middle;};
        Parameterized<TPELeft> parameterizedLeft = Parameterized{t=left;};

        Parameterized<TPELeft&TPERight> parameterizedExplicitMiddle = Parameterized<TPELeft&TPERight>{t=middle;};
        Parameterized<TPELeft> parameterizedExplicitLeft = Parameterized<TPELeft>{t=left;};

        ParameterizedWithBounds<TPELeft&TPERight> parameterizedWithBoundsMiddle = ParameterizedWithBounds{t=middle;};
        ParameterizedWithBounds<TPELeft> parameterizedWithBoundsLeft = ParameterizedWithBounds{t=left;};

        ParameterizedWithIntersectionBounds<TPELeft&TPERight> parameterizedWithIntersectionBoundsMiddle = ParameterizedWithIntersectionBounds{t=middle;};
        ParameterizedWithIntersectionBounds<TPELeft&TPERight> parameterizedWithErasedExplicitBoundsMiddle = ParameterizedWithIntersectionBounds<TPELeft&TPERight>{t=middle;};
        
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithParameterizedBounds = ParameterizedWithParameterizedBounds{t=2;};
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithExplicitParameterizedBounds = ParameterizedWithParameterizedBounds<Integer>{t=2;};
 
        parameterizedMiddle.parameterized{i=left; t=middle;};
        parameterizedMiddle.parameterized{i=middle; t= middle;};
        parameterizedMiddle.parameterized<TPELeft>{i=middle; t= middle;};
        parameterizedMiddle.parameterized<TPELeft&TPERight>{i=middle; t=middle;};

        parameterizedMiddle.parameterizedWithBounds{i=left; t=middle;};
        parameterizedMiddle.parameterizedWithBounds{i=middle; t=middle;};
        
        parameterizedMiddle.parameterizedWithIntersectionBounds{i=middle; t=middle;};

        parameterizedMiddle.parameterizedWithErasedBounds{i=""; t=middle;};
        parameterizedMiddle.parameterizedWithErasedBounds<Object>{i=""; t=middle;};
        parameterizedMiddle.parameterizedWithErasedBounds<String>{i=""; t=middle;};
        
        parameterizedMiddle.parameterizedWithParameterizedBounds{i=2;t=middle;};
        parameterizedMiddle.parameterizedWithParameterizedBounds<Integer>{i=2;t=middle;};
    }

    void testPrimitiveBound<T>(T init) given T satisfies Integer {
        variable T n = init;
    }
    class TestPrimitiveBound<T>(T init) given T satisfies Integer {
        variable T n = init;
    }
}