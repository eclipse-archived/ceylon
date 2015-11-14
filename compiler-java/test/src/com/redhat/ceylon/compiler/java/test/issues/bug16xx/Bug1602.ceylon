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
import java.lang { BooleanArray, ByteArray, CharArray, DoubleArray, FloatArray, IntArray, LongArray, ObjectArray, ShortArray }

@noanno
void bug1602() {
    BooleanArray boolArray = Bug1602Java().boolArray;
    print(boolArray.string);
    ByteArray byteArray = Bug1602Java().byteArray;
    print(byteArray.string);
    CharArray charArray = Bug1602Java().charArray;
    print(charArray.string);
    DoubleArray doubleArray = Bug1602Java().doubleArray;
    print(doubleArray.string);
    FloatArray floatArray = Bug1602Java().floatArray;
    print(floatArray.string);
    IntArray intArray = Bug1602Java().intArray;
    print(intArray.string);
    LongArray longArray = Bug1602Java().longArray;
    print(longArray.string);
    ObjectArray<Object> objectArray = Bug1602Java().objectArray;
    print(objectArray.string);
    ShortArray shortArray = Bug1602Java().shortArray;
    print(shortArray.string);
}
