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
native class NativeCorrectOrder() {
    native shared variable Integer i = 0;
    native shared Integer j = ++i;
    native shared Integer k = ++i;
}

native("jvm") class NativeCorrectOrder() {
    native("jvm") shared variable Integer i = 10;
}

native("js") class NativeCorrectOrder() {
    native("js") shared variable Integer i = 10;
}

shared void testNativeCorrectOrder() {
    value x = NativeCorrectOrder();
    if (x.i == 12 && x.j == 11 && x.k == 12) {
        throw Exception("NativeCorrectOrder-JVM");
    }
}