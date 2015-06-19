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
native Integer nativeAttributePrivate1;
native("jvm") Integer nativeAttributePrivate1 = 1;
native("js") Integer nativeAttributePrivate1 = 2;

native variable Integer nativeAttributePrivate2;
native("jvm") variable Integer nativeAttributePrivate2 = 1;
native("js") variable Integer nativeAttributePrivate2 = 2;

native Integer nativeAttributePrivate3;
native("jvm") Integer nativeAttributePrivate3 {
    throw Exception("NativeAttributePrivate-JVM");
}
native("js") Integer nativeAttributePrivate3 {
    throw Exception("NativeAttributePrivate-JS");
}


void testNativeAttributePrivate() {
    value x = nativeAttributePrivate1;
    value y = nativeAttributePrivate2;
    nativeAttributePrivate2 = 3;
    value z = nativeAttributePrivate3;
}
