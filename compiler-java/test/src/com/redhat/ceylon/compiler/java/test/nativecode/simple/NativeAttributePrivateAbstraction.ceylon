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
native Integer nativeAttributePrivateAbstraction1;

native("java") Integer nativeAttributePrivateAbstraction1 = 1;

native("js") Integer nativeAttributePrivateAbstraction1 = 2;


native variable Integer nativeAttributePrivateAbstraction2;

native("java") variable Integer nativeAttributePrivateAbstraction2 = 1;

native("js") variable Integer nativeAttributePrivateAbstraction2 = 2;


native Integer nativeAttributePrivateAbstraction3;

native("java") Integer nativeAttributePrivateAbstraction3 {
    throw Exception("NativeAttributePrivateAbstraction-JVM");
}

native("js") Integer nativeAttributePrivateAbstraction3 {
    throw Exception("NativeAttributePrivateAbstraction-JS");
}


void testNativeAttributePrivateAbstraction() {
    value x = nativeAttributePrivateAbstraction1;
    value y = nativeAttributePrivateAbstraction2;
    nativeAttributePrivateAbstraction2 = 3;
    value z = nativeAttributePrivateAbstraction3;
}
