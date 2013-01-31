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
interface Bug994 satisfies Correspondence<Integer,Integer> {
    shared default void m(){
        void localMethod(){}
        // don't mess with local declarations
        value b = localMethod;
    }
    void mprivate(){}
    interface Inner {
        void m2(){
            variable Integer x = 0;
            Integer? i1 = outer.get(1);
            Integer? i2 = outer[1];
            Integer? i3 = outer.get(x++);
            Integer? i4 = outer[x++];
            outer.m();
            outer.mprivate();
            Bug994 foo = outer;
            foo.m();
            foo.mprivate();
        }
    }
}
