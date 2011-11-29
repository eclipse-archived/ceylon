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
interface Top {
    shared formal void top();
}
@nomodel
interface Left satisfies Top {
    shared formal void left();
}
@nomodel
interface Right satisfies Top {
    shared formal void right();
}
@nomodel
class CLeft() satisfies Left {
    shared actual void left() {}
    shared actual void top() {}
}
@nomodel
class CMiddle() satisfies Left & Right{
    shared actual void left() {}
    shared actual void top() {}
    shared actual void right() {}
}
@nomodel
interface EmptyInterface {}

@nomodel
class Test() {
    void takesTop(Top top){}
    void takesLeft(Left left){}
    Left & Right givesLeftAndRight(){ return CMiddle(); }

    void testUnion(){
        Left|Right middle = CLeft();
        middle.top();
        takesTop(middle);
        if(is Left middle){
            middle.left();
            takesLeft(middle);
        }
    }
    // WARNING: when the typechecker figures out that because Natural is final
    // the Natural&EmptyInterface type cannot exist, we'll need to change it to
    // something else
    Left testIntersection(Natural&EmptyInterface p1){
        Left&Right middle = CMiddle();
        // invocation
        middle.top();
        middle.left();
        middle.right();
        givesLeftAndRight().top();
        CMiddle().top();
        // positional param
        takesTop(middle);
        takesLeft(middle);
        // named param
        takesTop{top = middle;};
        takesLeft{left = middle;};
        // assign
        variable Left&Right middleVar := CMiddle();
        // FIXME: add this back when assignment is a proper expression
        // (middleVar := CMiddle()).left();
        Left left = middleVar;
        Left left2;
        left2 = middleVar;
        variable Left left3 := middleVar;
        left3 := middleVar; 
        // arithmetic operators
        Numeric<Natural>&Ordinal<Natural> n = 1;
        Natural n2 = n + n;
        Natural n3 = n * n;
        
        Integral<Natural>&Invertable<Integer> m = 1;
        Natural n4 = m % m;
        Integer i1 = -m;
        Integer i2 = +m;

        // equality operators
        Boolean b = n == n;
        Boolean b2 = p1 < p1;

        // can't erase boolean types, since Boolean is final and thus can't have
        // intersections with things that can't be simplified to Boolean

        // sequence operators
        Empty|Sequence<Natural&EmptyInterface> naturals = {p1};
        Natural? n5 = naturals[p1];

        // FIXME: this is broken        
        //Numeric<Natural>&Ordinal<Natural>&Integral<Natural> n = 1;
        //Natural n2 = n + n;
        //Natural n3 = n * n;
        //Natural n4 = n % n;
        
        // return
        return middle;
    }
}