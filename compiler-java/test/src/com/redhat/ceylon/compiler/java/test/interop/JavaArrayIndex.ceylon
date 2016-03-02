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
import java.lang{
    ObjectArray,
    IntArray,
    LongArray,
    ShortArray,
    ByteArray,
    BooleanArray,
    FloatArray,
    DoubleArray,
    CharArray
}

@noanno
void javaArrayIndex() {
    String? r0 = (nothing of ObjectArray<String>)[0];
    String? r1 = (nothing of ObjectArray<String?>)[0];
    Boolean b0 = (nothing of BooleanArray)[0];
    Byte o0 = (nothing of ByteArray)[0];
    Integer s0 = (nothing of ShortArray)[0];
    Integer i0 = (nothing of IntArray)[0];
    Integer l0 = (nothing of LongArray)[0];
    Float f0 = (nothing of FloatArray)[0];
    Float d0 = (nothing of DoubleArray)[0];
    Character c0 = (nothing of CharArray)[0];
    
}