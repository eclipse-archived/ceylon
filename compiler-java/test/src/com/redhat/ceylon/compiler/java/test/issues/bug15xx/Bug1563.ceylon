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
import java.lang { ObjectArray, IntArray }

@noanno
void bug1563(){
    bug1563_helper<Integer,Integer>();
}

@noanno
void bug1563_helper<T,U>(){
    value array1 = ObjectArray<T>(0);
    assert(array1 is ObjectArray<Integer>);
    
    value array2 = ObjectArray<ObjectArray<T>>(0);
    assert(array2 is ObjectArray<ObjectArray<Integer>>);
    
    value array3 = ObjectArray<List<T>>(0);
    // doesn't work due to Java erasure
    //assert(array3 is ObjectArray<List<Integer>>);
    
    value array4 = ObjectArray<T&U>(0);
    assert(array4 is ObjectArray<Integer>);

    Object array5 = ObjectArray<IntArray>(0);
    assert(array5 is ObjectArray<IntArray>);

    Object array6 = ObjectArray<ObjectArray<IntArray>>(0);
    assert(array6 is ObjectArray<ObjectArray<IntArray>>);
    
    value array7 = ObjectArray<Nothing>(0);
}
