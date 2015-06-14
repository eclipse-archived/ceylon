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
void testNativeAttributeLocal() {
    native("jvm") Integer nativeAttributeLocal1 = 1;
    
    native("js") Integer nativeAttributeLocal1 = 2;
    
    
    native("jvm") variable Integer nativeAttributeLocal2 = 1;
    
    native("js") variable Integer nativeAttributeLocal2 = 2;
    
    
    native("jvm") Integer nativeAttributeLocal3 {
        throw Exception("NativeAttributeLocal-JVM");
    }
    
    native("js") Integer nativeAttributeLocal3 {
        throw Exception("NativeAttributeLocal-JS");
    }
    
    value x = nativeAttributeLocal1;
    value y = nativeAttributeLocal2;
    nativeAttributeLocal2 = 3;
    value z = nativeAttributeLocal3;
}
