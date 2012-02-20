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
class TypeParameterErasure() {
    @error
    interface Top {
        shared formal void top();
        shared formal Integer topAttribute;
    }
    @error
    interface Left satisfies Top {
        shared formal void left();
        shared formal Integer leftAttribute;
    }
    @error
    interface Right satisfies Top {
        shared formal void right();
        shared formal Integer rightAttribute;
    }
    class CLeft() satisfies Left {
        shared actual void left() {}
        shared actual void top() {}
        shared actual Integer topAttribute = 1;
        shared actual Integer leftAttribute = 1;
    }
    class CMiddle() satisfies Left & Right{
        shared actual void left() {}
        shared actual void top() {}
        shared actual void right() {}
        shared actual Integer topAttribute = 1;
        shared actual Integer leftAttribute = 1;
        shared actual Integer rightAttribute = 1;
    }

    T parameterized<T>(T t) {
        return t;
    }

    T parameterizedWithBounds<T>(T t)
    given T satisfies Right {
        t.right();
        t.right{};
        return t;
    }

    T parameterizedWithIntersectionBounds<T>(T t)
    given T satisfies Left & Right {
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

        variable Left&Right middle;
        variable Left left;
        variable Right right;
        middle := CMiddle();
        
        left := parameterized(middle);
        right := parameterized(middle);
        middle := parameterized(middle);
        // with explicit non-erased bounds
        left := parameterized<Left>(middle);
        right := parameterized<Right>(middle);
        // with explicit erased bounds
        left := parameterized<Left&Right>(middle);
        right := parameterized<Left&Right>(middle);
        middle := parameterized<Left&Right>(middle);

        left := parameterizedWithBounds(middle);
        right := parameterizedWithBounds(middle);
        middle := parameterizedWithBounds(middle);

        left := parameterizedWithIntersectionBounds(middle);
        right := parameterizedWithIntersectionBounds(middle);
        middle := parameterizedWithIntersectionBounds(middle);

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

        variable Left&Right middle;
        variable Left left;
        variable Right right;
        middle := CMiddle();
        
        left := parameterized{t=middle;};
        right := parameterized{t=middle;};
        middle := parameterized{t=middle;};
        // with explicit non-erased bounds
        left := parameterized<Left>{t=middle;};
        right := parameterized<Right>{t=middle;};
        // with explicit erased bounds
        left := parameterized<Left&Right>{t=middle;};
        right := parameterized<Left&Right>{t=middle;};
        middle := parameterized<Left&Right>{t=middle;};

        left := parameterizedWithBounds{t=middle;};
        right := parameterizedWithBounds{t=middle;};
        middle := parameterizedWithBounds{t=middle;};

        left := parameterizedWithIntersectionBounds{t=middle;};
        right := parameterizedWithIntersectionBounds{t=middle;};
        middle := parameterizedWithIntersectionBounds{t=middle;};

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
        given Inner satisfies Left {
            i.left();
            i.left{};
            return i;
        }

        shared Inner parameterizedWithIntersectionBounds<Inner>(Inner i, T t)
        given Inner satisfies Left & Right {
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
    given T satisfies Left {
        t.left();
        t.left{};
    }

    class ParameterizedWithIntersectionBounds<T>(T t)
    given T satisfies Left & Right {
        t.left();
        t.left{};
        t.right();
        t.right{};
                
        void requiresCastLeft(Left l){}

        Left&Right middle = CMiddle();
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
        Left&Right middle = CMiddle();
        Left left = CLeft();
        
        Parameterized<Left&Right> parameterizedMiddle = Parameterized(middle);
        Parameterized<Left> parameterizedLeft = Parameterized(left);

        Parameterized<Left&Right> parameterizedExplicitMiddle = Parameterized<Left&Right>(middle);
        Parameterized<Left> parameterizedExplicitLeft = Parameterized<Left>(left);

        ParameterizedWithBounds<Left&Right> parameterizedWithBoundsMiddle = ParameterizedWithBounds(middle);
        ParameterizedWithBounds<Left> parameterizedWithBoundsLeft = ParameterizedWithBounds(left);

        ParameterizedWithIntersectionBounds<Left&Right> parameterizedWithIntersectionBoundsMiddle = ParameterizedWithIntersectionBounds(middle);
        ParameterizedWithIntersectionBounds<Left&Right> parameterizedWithErasedExplicitBoundsMiddle = ParameterizedWithIntersectionBounds<Left&Right>(middle);
        
        ParameterizedWithErasedBounds<String> parameterizedWithErasedBoundsMiddle = ParameterizedWithErasedBounds("");
        ParameterizedWithErasedBounds<String> parameterizedWithReallyErasedExplicitBoundsMiddle = ParameterizedWithErasedBounds<String>("");
        
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithParameterizedBounds = ParameterizedWithParameterizedBounds(2);
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithExplicitParameterizedBounds = ParameterizedWithParameterizedBounds<Integer>(2);
        
        parameterizedMiddle.parameterized(left, middle);
        parameterizedMiddle.parameterized(middle, middle);
        parameterizedMiddle.parameterized<Left>(middle, middle);
        parameterizedMiddle.parameterized<Left&Right>(middle, middle);

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
        Left&Right middle = CMiddle();
        Left left = CLeft();
        
        Parameterized<Left&Right> parameterizedMiddle = Parameterized{t=middle;};
        Parameterized<Left> parameterizedLeft = Parameterized{t=left;};

        Parameterized<Left&Right> parameterizedExplicitMiddle = Parameterized<Left&Right>{t=middle;};
        Parameterized<Left> parameterizedExplicitLeft = Parameterized<Left>{t=left;};

        ParameterizedWithBounds<Left&Right> parameterizedWithBoundsMiddle = ParameterizedWithBounds{t=middle;};
        ParameterizedWithBounds<Left> parameterizedWithBoundsLeft = ParameterizedWithBounds{t=left;};

        ParameterizedWithIntersectionBounds<Left&Right> parameterizedWithIntersectionBoundsMiddle = ParameterizedWithIntersectionBounds{t=middle;};
        ParameterizedWithIntersectionBounds<Left&Right> parameterizedWithErasedExplicitBoundsMiddle = ParameterizedWithIntersectionBounds<Left&Right>{t=middle;};
        
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithParameterizedBounds = ParameterizedWithParameterizedBounds{t=2;};
        ParameterizedWithParameterizedBounds<Integer> parameterizedWithExplicitParameterizedBounds = ParameterizedWithParameterizedBounds<Integer>{t=2;};
 
        parameterizedMiddle.parameterized{i=left; t=middle;};
        parameterizedMiddle.parameterized{i=middle; t= middle;};
        parameterizedMiddle.parameterized<Left>{i=middle; t= middle;};
        parameterizedMiddle.parameterized<Left&Right>{i=middle; t=middle;};

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
        variable T n := init;
    }
    class TestPrimitiveBound<T>(T init) given T satisfies Integer {
        variable T n := init;
    }
}