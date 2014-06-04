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
@noanno
interface Top {
    shared formal void top();
    shared formal Integer topAttribute;
    shared formal Left&Right leftAndRightAttribute;
    shared formal Left&Right leftAndRightMethod(Left&Right param);
}
@noanno
interface Left satisfies Top {
    shared formal void left();
    shared formal Integer leftAttribute;
}
@noanno
interface Right satisfies Top {
    shared formal void right();
    shared formal Integer rightAttribute;
}
@noanno
class CMiddle() satisfies Left & Right{
    shared actual void left() {}
    shared actual void top() {}
    shared actual void right() {}
    shared actual Integer topAttribute = 1;
    shared actual Integer leftAttribute = 1;
    shared actual Integer rightAttribute = 1;
    shared actual Left&Right leftAndRightAttribute = CMiddle();
    shared actual Left&Right leftAndRightMethod(Left&Right param) {
        return param;
    }
}
@noanno
class CLeft() satisfies Left {
    shared actual void left() {}
    shared actual void top() {}
    shared actual Integer topAttribute = 1;
    shared actual Integer leftAttribute = 1;
    shared actual Left&Right leftAndRightAttribute = CMiddle();
    shared actual Left&Right leftAndRightMethod(Left&Right param) {
        return param;
    }
}
@noanno
interface EmptyInterface {}

@noanno
variable Left topLevelLeftAttribute = CLeft();

@noanno
class MyException(String? m, Exception? x) 
 extends Exception(m, x)
 satisfies EmptyInterface {}

@noanno
interface MyNumeric satisfies Numeric<MyNumeric> & Integral<MyNumeric> & Comparable<MyNumeric> 
        & Exponentiable<MyNumeric,MyNumeric> & Scalable<MyNumeric,MyNumeric> {}

@noanno
class Test(Integer&EmptyInterface n) {
    
    void takesTop(Top top){}
    void takesLeft(Left left){}
    Left & Right givesLeftAndRight(){ return CMiddle(); }
    
    shared variable Left leftAttribute = CLeft();
    shared variable Left&Right middleAttribute = CMiddle();

    void testUnion(){
        Left|Right middle = CLeft();
        middle.top();
        Integer n1 = middle.topAttribute;
        takesTop(middle);
        if(is Left middle){
            Integer n2 = middle.leftAttribute;
            middle.left();
            takesLeft(middle);
        }
    }

    // WARNING: when the typechecker figures out that because Integer is final
    // the Integer&EmptyInterface type cannot exist, we'll need to change it to
    // something else
    Left testIntersection(Obtainable&EmptyInterface p1,
                          Obtainable&EmptyInterface|Null p1OrNull,
                          Sequence<Top&EmptyInterface>&EmptyInterface tops,
                          Null|Sequence<Top&EmptyInterface>&EmptyInterface topsOrNull,
                          Test&EmptyInterface erasedTest){
        Left&Right middle = CMiddle();

        // invocation
        middle.top();
        middle.left();
        middle.right();
        givesLeftAndRight().top();
        CMiddle().top();
        
        // attribute access
        variable Integer sync;
        sync = middle.topAttribute;
        sync = middle.leftAttribute;
        sync = middle.rightAttribute;
        sync = givesLeftAndRight().topAttribute;
        sync = CMiddle().topAttribute;

        // positional param
        takesTop(middle);
        takesLeft(middle);
        
        // named param
        takesTop{top = middle;};
        takesLeft{left = middle;};
        
        // assign
        variable Left&Right middleVar = CMiddle();
        Left left = middleVar;
        Left left2;
        left2 = middleVar;
        variable Left left3 = middleVar;
        left3 = middleVar; 
        leftAttribute = middleVar;
        erasedTest.leftAttribute = middleVar;
        // FIXME: this is broken:
        //topLevelLeftAttribute = middleVar;

        (middleVar = CMiddle()).left();
        (erasedTest.middleAttribute = middleVar).left();
        
        // can't erase boolean types, since Boolean is final and thus can't have
        // intersections with things that can't be simplified to Boolean
        
        // range
        // FIXME: haven't been able to get an erased range bound
        //Integer[] seq = p1..p1;
        
        // entry
        value entry = p1 -> p1;
        
        if(true||true){
            Exception&EmptyInterface x = MyException(null, null);
            x.printStackTrace();
            value cause = x.cause;
            throw x;
        }        

        // return
        return middle;
    }

    void testNullHandlingOperators(Obtainable&EmptyInterface p1,
                                   Obtainable&EmptyInterface|Null p1OrNull){
        // conditions
        if(exists p1OrNull){}
        variable Boolean bSync;
        bSync = p1OrNull exists;
        
        value p2 = p1OrNull else p1;
        Obtainable n = p1OrNull else p1;
        
        // FIXME: those operators are not yet supported
        //value p3 = p1OrNull?.remainder(p1);
        //value p4 = p1OrNull?.zero;
    }

    void testArithmeticOperators(MyNumeric&EmptyInterface p1,
                                 Test&EmptyInterface erasedTest){
        // with boxing
        MyNumeric unboxed = p1;
        MyNumeric&EmptyInterface boxed = this.n;

        // arithmetic operators
        variable MyNumeric&EmptyInterface n = this.n;
        variable MyNumeric sync;
        sync = n + n;
        sync = n - n;
        sync = n * n;
        sync = n % n;
        sync = n / n;
        sync = n ^ n;

        sync += n;
        sync -= n;
        sync *= n;
        sync /= n;
        sync %= n;

        sync = -n;
        sync = +n;
        
        sync = n ** n;
    }

    void testBitwiseAssignmentOperators(Set<Integer>&EmptyInterface p1){
        variable Set<Integer> sync; 
        sync = p1 & p1;
        sync = p1 | p1;
        sync = p1 ~ p1;
        
        sync &= p1;
        sync |= p1;
        sync ~= p1;
    }

    void testComparisonOperators(MyNumeric&EmptyInterface p1,
                                 Test&EmptyInterface erasedTest,
                                 Category&EmptyInterface container){
        // equality operators
        variable Boolean sync;
        sync = erasedTest === erasedTest;
        sync = p1 == p1;
        sync = p1 < p1;
        value cmp = p1 <=> p1;
        
        // in
        sync = p1 in container;
        
        // is
        if(is Category p1){}
        sync = p1 is Category;
    }

    void testSequences(Integer&EmptyInterface p1,
                       Sequence<Left&Right>&EmptyInterface leftsAndRights,
                       Sequence<Entry<Left&Right,Left&Right>>&EmptyInterface leftsAndRightsEntries,
                       Null|Sequence<Left&Right>&EmptyInterface topsOrNull){
        // sequence operators
        Empty|Sequence<Integer&EmptyInterface> naturals = [p1];
        Integer? n5 = naturals[p1];
        Top? t = leftsAndRights[p1];
        Empty|Sequence<Integer&EmptyInterface>|Null naturalsOrNull = [p1];
        
        variable Empty|Sequence<Integer&EmptyInterface> subrange;
        subrange = naturals[p1..p1] of Empty|Sequence<Integer&EmptyInterface>;
        subrange = naturals[p1...] of Empty|Sequence<Integer&EmptyInterface>;
        subrange = naturals[...p1] of Empty|Sequence<Integer&EmptyInterface>;

        // sequence expression
        Integer[] plainIntegers = [p1];

        // iteration
        // FIXME: I couldn't find a way to get a sequence erased to object
        for(Integer&EmptyInterface it in naturals){
            Numeric<Integer> n6 = it;
        }
        for(Left itLeft in leftsAndRights){
            itLeft.top();
            itLeft.left();
        }
        for(Left&Right itErased in leftsAndRights){
            itErased.top();
            itErased.left();
            itErased.right();
        }
        for(itErasedValue in leftsAndRights){
            itErasedValue.top();
            itErasedValue.left();
            itErasedValue.right();
        }
        // same with entries
        for(Left itLeft1 -> Left itLeft2 in leftsAndRightsEntries){
            itLeft1.top();
            itLeft1.left();
            itLeft2.top();
            itLeft2.left();
        }
        for(Left&Right itErased1 -> Left&Right itErased2 in leftsAndRightsEntries){
            itErased1.top();
            itErased1.left();
            itErased1.right();
            itErased2.top();
            itErased2.left();
            itErased2.right();
        }
        for(itErasedValue1 -> itErasedValue2 in leftsAndRightsEntries){
            itErasedValue1.top();
            itErasedValue1.left();
            itErasedValue1.right();
            itErasedValue2.top();
            itErasedValue2.left();
            itErasedValue2.right();
        }
        Sequence<Top> topSequence = [CMiddle()]; 
        for(Top it in topSequence){
            it.top();
        }
        
        // erased type for sequences
        variable Integer sync;
        sync = naturals.size;
        sync = leftsAndRights.size;

        // nonempty tests
        if(nonempty naturals){}
        variable Boolean bSync;
        bSync = naturals nonempty;
        
        // spread op
        Left[]&Right[] spreadMember = leftsAndRights*.leftAndRightAttribute;
        variable Left[]&Right[] spreadInvocation;
        spreadInvocation = leftsAndRights*.leftAndRightMethod(CMiddle());
        spreadInvocation = leftsAndRights*.leftAndRightMethod{param = CMiddle();};
    }
}
