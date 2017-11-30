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
class MethodWhileIsNotWithUnionOfTwo() {
    shared void m() {
        String|Integer aint1 = "s";
        String|Integer aint2 = 2;
        while (!is Integer aint1) {
            print(aint1.reversed);
        }
        while (!is String aint2) {
            print(aint2-2);
        }
    }
    shared void mvar() {
        String|Integer aint1 = "s";
        String|Integer aint2 = 2;
        while (!is Integer s=aint1) {
            print(s.reversed);
        }
        while (!is String s=aint2) {
            print(s-2);
        }
    }
    
    shared void m2() {
        while (!is Integer aint1, aint1.size==1) {
            print(aint1.uppercased);
        }
        while (!is String aint2, aint2>0) {
            print(aint2/2);
        }
    }
}
