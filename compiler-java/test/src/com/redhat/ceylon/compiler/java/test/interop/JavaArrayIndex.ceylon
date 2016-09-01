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
    ObjectArray<String> ar0 = nothing;
    String? r0 = ar0[0];
    String r0x = (ar0[0] = "a");

    ObjectArray<String?> ar1 = nothing;
    String? r1 = ar1[0];
    String r1x = (ar1[0] = "a");
    
    BooleanArray ab0 = nothing;
    Boolean b0 = ab0[0];
    Boolean b0x = (ab0[0] = true);
    
    ByteArray ao0 = nothing;
    Byte o0 = ao0[0];
    Byte o0x = (ao0[0] = 1.byte);
    
    ShortArray as0 = nothing;
    Integer s0 = as0[0];
    Integer s0x = (as0[0] = 1);
    
    IntArray ai0 = nothing;
    Integer i0 = ai0[0];
    Integer i0x = (ai0[0] = 1);
    
    LongArray al0 = nothing;
    Integer l0 = al0[0];
    Integer l0x = (al0[0] = 1);
    
    FloatArray af0 = nothing;
    Float f0 = af0[0];
    Float f0x = (af0[0] = 1.0);
    
    DoubleArray ad0 = nothing;
    Float d0 = ad0[0];
    Float d0x = (ad0[0] = 1.0);
    
    CharArray ac0 = nothing;
    Character c0 = ac0[0];
    Character c0x = (ac0[0] = 'a');
}
