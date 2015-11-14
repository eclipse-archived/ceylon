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
shared class ClassNativeMembersWithImpl() {
    native shared void testShared(Integer i) {
        throw Exception("ClassNativeMembersWithImpl-JVM");
    }
    native("js") shared void testShared(Integer i) {
        throw Exception("ClassNativeMembersWithImpl-JS");
    }

    native shared Integer attrShared => 1;
    native("js") shared Integer attrShared => 2;

    native shared object objectShared {
        native shared Integer test(Integer i) {
            return i;
        }
    }
    native("jvm") shared object objectShared {}
    native("js") shared object objectShared {
        native("js") shared Integer test(Integer i) {
            return i;
        }
    }
}

void testClassNativeMembersWithImpl() {
    value klz = ClassNativeMembersWithImpl();
    value x = klz.attrShared;
    value y = klz.objectShared.test(x);
    klz.testShared(y);
}
