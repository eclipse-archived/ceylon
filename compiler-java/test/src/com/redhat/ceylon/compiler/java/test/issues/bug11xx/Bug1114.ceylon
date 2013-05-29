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
class Bug1114(Boolean test) {
    //Integer j;
    //if (test) {
    //    j => 1;
    //} else {
    //    j => 2;
    //}
    Float g;
    g => 2.0;
    //void foo() {
    //    Integer i;
    //    if (test) {
    //        i => 1;
    //    } else {
    //        i => 2;
    //    }
    //    Float f;
    //    f => 2.0;
    //    Character c;
    //    c => 'c';
    //    String s;
    //    s => "d";
    //    Bug1114 bug;
    //    bug => Bug1114(test);
    //}
}
