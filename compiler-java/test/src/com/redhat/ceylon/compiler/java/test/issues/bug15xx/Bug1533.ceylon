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
alias X => [Integer];
alias Y => [String];
alias Z<E,Es> given Es satisfies Tuple<E,E,E[]> => Tuple<E,E,Nothing>|Es;

@noanno
class Bug1533() {
    shared R tupleOrTp<R,Es,E>(Callable<R,Tuple<E,E,Nothing>|Es> callable, Es es) 
            given Es satisfies Tuple<E,E,E[]> {
        return callable(*es);
    }
    shared R tpUnionTp<R,A,B>(Callable<R,A|B> callable, A args) 
            given A satisfies Anything[]
            given B satisfies Anything[] {
        return callable(*args);
    }
    shared R union2<R>(Callable<R,[Integer]|[String]> callable, [Integer]|[String] args) {
        return callable(*args);
    }
    shared R union3<R>(Callable<R,[Integer]|[String]|[Boolean]> callable, [Integer]|[String]|[Boolean] args) {
        return callable(*args);
    }
    shared R unionAlias<R>(Callable<R,X|Y> callable, X|Y args) {
        return callable(*args);
    }
    
    shared R tpIntersectTp<R,A,B>(Callable<R,A&B> callable, A&B args) 
            given A satisfies Anything[]
            given B satisfies Anything[] {
        return callable(*args);
    }
    shared R intersect3<R,A,B,C>(Callable<R,A&B&C> callable, A&B&C args) 
            given A satisfies Anything[]
            given B satisfies Anything[]
            given C satisfies Anything[] {
        return callable(*args);
    }
    shared R tpIntersectNothing<R,A>(Callable<R,A>&Callable<R,Nothing> callable, A args) 
            given A satisfies Anything[]{
        return callable(*args);
    }
    shared R tpIntersectEmpty<R,A>(Callable<R,A>&Callable<R,[]> callable, A args) 
            given A satisfies Anything[]{
        return callable(*args);
    }
    shared R tpUnionEmpty<R,A>(Callable<R,A>|Callable<R,[]> callable, A&[] args) 
            given A satisfies Anything[]{
        return callable(*args);
    }
}
@noanno
void bug1533_callsite() {
    value inst = Bug1533();
    assert(1 == inst.tupleOrTp((Integer x)=>x, [1]));
    assert(4 == inst.tupleOrTp((Integer x, Integer y)=>x+y, [2, 2]));
    value v2 = (Integer x, Integer y)=>x+y;
    assert(4 == inst.tupleOrTp(v2, [2, 2]));
    function f2(Integer x, Integer y)=>x+y;
    assert(4 == inst.tupleOrTp(f2, [2, 2]));
    
    assert(4 == inst.tpUnionTp(v2, [2, 2]));
    assert(2 == inst.union2((Integer|String x)=>x, [2]));
    assert(2 == inst.union3((Integer|String|Boolean x)=>x, [2]));
    assert(2 == inst.unionAlias((Integer|String x)=>x, [2]));
}

