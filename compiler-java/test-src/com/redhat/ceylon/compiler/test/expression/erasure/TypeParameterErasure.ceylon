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

    interface Top {
        shared formal void top();
        shared formal Natural topAttribute;
    }
    interface Left satisfies Top {
        shared formal void left();
        shared formal Natural leftAttribute;
    }
    interface Right satisfies Top {
        shared formal void right();
        shared formal Natural rightAttribute;
    }
    class CLeft() satisfies Left {
        shared actual void left() {}
        shared actual void top() {}
        shared actual Natural topAttribute = 1;
        shared actual Natural leftAttribute = 1;
    }
    class CMiddle() satisfies Left & Right{
        shared actual void left() {}
        shared actual void top() {}
        shared actual void right() {}
        shared actual Natural topAttribute = 1;
        shared actual Natural leftAttribute = 1;
        shared actual Natural rightAttribute = 1;
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

    T parameterizedWithErasedBounds<T>(T t)
    given T satisfies Left & Right {
        t.left();
        t.left{};
        t.right();
        t.right{};
        return t;
    }

    void testTypeParameters(){
        Integer i = parameterized<Integer>(+2);
        Integer i2 = parameterized(+2);
        Natural n = parameterized<Natural>(2);
        Natural n2 = parameterized(2);

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

        left := parameterizedWithErasedBounds(middle);
        right := parameterizedWithErasedBounds(middle);
        middle := parameterizedWithErasedBounds(middle);
    }

    void testTypeParametersNamedArguments(){
        Integer i = parameterized<Integer>{t=+2;};
        Integer i2 = parameterized{t=+2;};
        Natural n = parameterized<Natural>{t=2;};
        Natural n2 = parameterized{t=2;};

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

        left := parameterizedWithErasedBounds{t=middle;};
        right := parameterizedWithErasedBounds{t=middle;};
        middle := parameterizedWithErasedBounds{t=middle;};
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

        shared Inner parameterizedWithErasedBounds<Inner>(Inner i, T t)
        given Inner satisfies Left & Right {
            i.left();
            i.left{};
            i.right();
            i.right{};
            return i;
        }
    }

    class ParameterizedWithBounds<T>(T t)
    given T satisfies Left {
        t.left();
        t.left{};
    }

    class ParameterizedWithErasedBounds<T>(T t)
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
    
    void testTypeParameterInstantiations(){
        Left&Right middle = CMiddle();
        Left left = CLeft();
        
        Parameterized<Left&Right> parameterizedMiddle = Parameterized(middle);
        Parameterized<Left> parameterizedLeft = Parameterized(left);

        Parameterized<Left&Right> parameterizedExplicitMiddle = Parameterized<Left&Right>(middle);
        Parameterized<Left> parameterizedExplicitLeft = Parameterized<Left>(left);

        ParameterizedWithBounds<Left&Right> parameterizedWithBoundsMiddle = ParameterizedWithBounds(middle);
        ParameterizedWithBounds<Left> parameterizedWithBoundsLeft = ParameterizedWithBounds(left);

        ParameterizedWithErasedBounds<Left&Right> parameterizedWithErasedBoundsMiddle = ParameterizedWithErasedBounds(middle);
        ParameterizedWithErasedBounds<Left&Right> parameterizedWithErasedExplicitBoundsMiddle = ParameterizedWithErasedBounds<Left&Right>(middle);
        
        parameterizedMiddle.parameterized(left, middle);
        parameterizedMiddle.parameterized(middle, middle);
        parameterizedMiddle.parameterized<Left>(middle, middle);
        parameterizedMiddle.parameterized<Left&Right>(middle, middle);

        parameterizedMiddle.parameterizedWithBounds(left, middle);
        parameterizedMiddle.parameterizedWithBounds(middle, middle);
        
        parameterizedMiddle.parameterizedWithErasedBounds(middle, middle);
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

        ParameterizedWithErasedBounds<Left&Right> parameterizedWithErasedBoundsMiddle = ParameterizedWithErasedBounds{t=middle;};
        ParameterizedWithErasedBounds<Left&Right> parameterizedWithErasedExplicitBoundsMiddle = ParameterizedWithErasedBounds<Left&Right>{t=middle;};
        
        parameterizedMiddle.parameterized{i=left; t=middle;};
        parameterizedMiddle.parameterized{i=middle; t= middle;};
        parameterizedMiddle.parameterized<Left>{i=middle; t= middle;};
        parameterizedMiddle.parameterized<Left&Right>{i=middle; t=middle;};

        parameterizedMiddle.parameterizedWithBounds{i=left; t=middle;};
        parameterizedMiddle.parameterizedWithBounds{i=middle; t=middle;};
        
        parameterizedMiddle.parameterizedWithErasedBounds{i=middle; t=middle;};
    }
}