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
import ceylon.language.meta.model{ClassOrInterface}
@noanno
interface IBug2062<out Element> of SBug2062<Element>
        {
    shared formal [Element *] elements;
    
    shared formal SBug2062<Element|Other> child<Other>([Other +] others);
    
    shared default void append<Other>(SBug2062<Other> other) {
        IBug2062<Element|Other> v = if(nonempty others = (other.elements of [Other*])) // error: incompatible types
            then child(others) 
            else this;
        IBug2062<Element|Other> v2 = switch(otherElems = other.elements) 
            case (is [Element+]) child(otherElems)
            else this;
        ClassOrInterface<IBug2062<Element>|SBug2062<Other>> x = if (nonempty others = (other.elements of [Other*]))
            then `IBug2062<Element>` 
            else `SBug2062<Other>`;
        ClassOrInterface<IBug2062<Element>|SBug2062<Other>> x2 = switch(otherElems = other.elements)
            case (is [Element+]) `IBug2062<Element>`
            else `SBug2062<Other>`;
    }
}
@noanno
class SBug2062<out Element>(shared actual [Element +] elements) 
        satisfies IBug2062<Element> 
        {
    shared actual SBug2062<Element|Other> child<Other>([Other +] others) => SBug2062(others);
}