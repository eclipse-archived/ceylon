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
// Check we don't reeval the iterable expression
void bug1351_reeval() {
    variable Integer end = 3;
    Iterable<Integer> foo {
        end++;
        object bar satisfies Iterable<Integer> {
            shared actual Iterator<Integer> iterator() {
                return (0..end).iterator();
            }
        }
        return bar;
    }
    value lala = {for (x in foo) x};
    assert([0, 1, 2, 3, 4] == [*lala]);
    assert([0, 1, 2, 3, 4, 5] == [*lala]);
}
// Check we can handle dependent iterables
shared Element[] bug1351_dependant<Element>({Element*}* iterables) 
        => [ for (it in iterables) for (val in it) val ];


// Check we don't fuck up when we're in an initializer, or a super
class Bug1351BackwardsBranch(Integer* it) {
    assert([for (num in it*.string) parseInteger(num)] == [*it]);
}
// and we use a sread as an iterable expr in a comprehension
class Bug1351BackwardsBranchSuper() extends Bug1351BackwardsBranch(for (num in 0..10) num+1) {
    
}

void bug1351() {
    bug1351_reeval();
    assert(["", "a"] == bug1351_dependant({}, {""}, {"a"}));
    Bug1351BackwardsBranchSuper();
}