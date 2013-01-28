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
interface InterfaceErasure<Element, Absent>
    given Absent satisfies Null {
    // should be erased
    shared default Absent|Element attr => nothing;
    shared default Absent|Element m() => nothing;

    shared default void defaultedParams(Absent|Element p = nothing, Absent|Element p2 = nothing){}

    shared default class Class(Absent|Element p = nothing, Absent|Element p2 = nothing){}
}

@nomodel
class InterfaceErasureImpl<Element>() satisfies InterfaceErasure<Element, Null> {
    // make sure the generated bridges are appropriately casted
}
