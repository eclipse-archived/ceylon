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
    shared formal Natural topAttribute;
}
@nomodel
interface Left satisfies Top {
    shared formal void left();
    shared formal Natural leftAttribute;
}
@nomodel
interface Right satisfies Top {
    shared formal void right();
    shared formal Natural rightAttribute;
}
@nomodel
class CLeft() satisfies Left {
    shared actual void left() {}
    shared actual void top() {}
    shared actual Natural topAttribute = 1;
    shared actual Natural leftAttribute = 1;
}
@nomodel
class CMiddle() satisfies Left & Right{
    shared actual void left() {}
    shared actual void top() {}
    shared actual void right() {}
    shared actual Natural topAttribute = 1;
    shared actual Natural leftAttribute = 1;
    shared actual Natural rightAttribute = 1;
}
@nomodel
interface EmptyInterface {}

@nomodel
variable Left topLevelLeftAttribute := CLeft();

@nomodel
class MyException(String? m, Exception? x) 
 extends Exception(m, x)
 satisfies EmptyInterface {}

@nomodel
class Test() {
    void takesTop(Top top){}
    void takesLeft(Left left){}
    Left & Right givesLeftAndRight(){ return CMiddle(); }
    shared variable Left leftAttribute := CLeft();
    shared variable Numeric<Natural>&Ordinal<Natural>&Subtractable<Natural,Integer> n := 1;
    shared variable Integral<Natural>&Invertable<Integer> m := 1;

    void testUnion(){
        Left|Right middle = CLeft();
        middle.top();
        Natural n1 = middle.topAttribute;
        takesTop(middle);
        if(is Left middle){
            Natural n2 = middle.leftAttribute;
            middle.left();
            takesLeft(middle);
        }
    }

    // WARNING: when the typechecker figures out that because Natural is final
    // the Natural&EmptyInterface type cannot exist, we'll need to change it to
    // something else
    Left testIntersection(Natural&EmptyInterface p1,
                          Natural&EmptyInterface|Nothing p1OrNothing,
                          Sequence<Top&EmptyInterface>&EmptyInterface tops,
                          Nothing|Sequence<Top&EmptyInterface>&EmptyInterface topsOrNothing,
                          Test&EmptyInterface erasedTest){
        Left&Right middle = CMiddle();

        // invocation
        middle.top();
        middle.left();
        middle.right();
        givesLeftAndRight().top();
        CMiddle().top();
        
        // attribute access
        variable Natural sync;
        sync := middle.topAttribute;
        sync := middle.leftAttribute;
        sync := middle.rightAttribute;
        sync := givesLeftAndRight().topAttribute;
        sync := CMiddle().topAttribute;

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
        leftAttribute := middleVar;
        erasedTest.leftAttribute := middleVar;
        // FIXME: this is broken:
        //topLevelLeftAttribute := middleVar;
        
        // can't erase boolean types, since Boolean is final and thus can't have
        // intersections with things that can't be simplified to Boolean
        
        // range
        // FIXME: haven't been able to get an erased range bound
        //Natural[] seq = p1..p1;
        
        // entry
        value entry = p1 -> p1;
        
        // conditions
        if(is Natural p1){}
        if(exists p1OrNothing){}
        variable Boolean bSync;
        bSync := is Natural p1;
        bSync := exists p1OrNothing;

        if(true){
            Exception&EmptyInterface x = MyException(null, null);
            throw x;
        }        

        // return
        return middle;
    }

    void testArithmeticOperators(Natural&EmptyInterface p1,
                                 Test&EmptyInterface erasedTest){
        // with boxing
        Natural unboxed = p1;
        Numeric<Natural>&Ordinal<Natural> boxed = 1;

        // arithmetic operators
        variable Numeric<Natural>&Ordinal<Natural>&Subtractable<Natural,Integer> n := 1;
        Natural n2 = n + n;
        value i0 = n - n;
        Natural n3 = n * n;
        n := n += n;
        n := n -= n;
        n := n *= n;
        erasedTest.n := erasedTest.n += erasedTest.n;
        erasedTest.n := erasedTest.n -= erasedTest.n;
        erasedTest.n := erasedTest.n *= erasedTest.n;
        
        n := n++;
        n := ++n;
        erasedTest.n := erasedTest.n++;
        erasedTest.n := ++erasedTest.n;
                                
        variable Integral<Natural>&Invertable<Integer> m := 1;
        Natural n4 = m % m;
        n := m %= m;
        erasedTest.n := erasedTest.m %= erasedTest.m;
                
        Integer i1 = -m;
        Integer i2 = +m;
    }

    void testComparisonOperators(Natural&EmptyInterface p1,
                                 Test&EmptyInterface erasedTest){
        // equality operators
        Boolean b = n == n;
        Boolean b2 = p1 < p1;
    }

    void testSequences(Natural&EmptyInterface p1,
                       Sequence<Left&Right>&EmptyInterface leftsAndRights,
                       Sequence<Entry<Left&Right&Equality,Left&Right&Equality>&EmptyInterface>&EmptyInterface leftsAndRightsEntries,
                       Nothing|Sequence<Left&Right>&EmptyInterface topsOrNothing){
        // sequence operators
        Empty|Sequence<Natural&EmptyInterface> naturals = {p1};
        Natural? n5 = naturals[p1];
        Top? t = leftsAndRights[p1];
        Empty|Sequence<Natural&EmptyInterface>|Nothing naturalsOrNothing = {p1};
        Natural? n52 = naturalsOrNothing?[p1];
        Top? t2 = topsOrNothing?[p1];

        // sequence expression
        Natural[] plainNaturals = {p1};

        // iteration
        // FIXME: I couldn't find a way to get a sequence erased to object
        for(Natural&EmptyInterface it in naturals){
            Numeric<Natural> n6 = it;
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
        Sequence<Top> topSequence = {CMiddle()}; 
        for(Top it in topSequence){
            it.top();
        }
        
        // erased type for sequences
        variable Natural sync;
        sync := naturals.size;
        sync := leftsAndRights.size;

        // nonempty tests
        if(nonempty naturals){}
        variable Boolean bSync;
        bSync := nonempty naturals;
    }
}