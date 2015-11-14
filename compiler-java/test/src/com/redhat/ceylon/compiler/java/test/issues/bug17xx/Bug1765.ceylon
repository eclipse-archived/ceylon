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
interface Bug1765_I1<out Element> {
    shared default Bug1765_I1<Element> bar1() => nothing;
    shared default Bug1765_I1<Element> bar2() => nothing;
}

@nomodel
interface Bug1765_I2 satisfies Bug1765_I1<Integer>{}

@nomodel
interface Bug1765_I3 satisfies Bug1765_I1<String>{}

@nomodel
class Bug1765_Foo() satisfies Bug1765_I2 & Bug1765_I3 {
    shared actual Bug1765_I1<Integer&String> bar1() => nothing;
    // leave bar2 default
}
