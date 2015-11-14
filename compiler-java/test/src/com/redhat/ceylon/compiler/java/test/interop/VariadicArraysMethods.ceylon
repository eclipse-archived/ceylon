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
import java.lang { IntArray, ObjectArray }

@noanno
void variadicArraysMethods() {
    TypesJava java = TypesJava();

    ObjectArray<Object> arr1 = ObjectArray<Object>(1);
    arr1.set(0, "a");
    ObjectArray<Object> arr2 = ObjectArray<Object>(1);
    arr2.set(0, "b");
    Iterable<ObjectArray<Object>> objectArray = {arr1, arr2};
    java.variadicObjectArray(*objectArray);
    
    // Callable has a variadic param which can mess things up
    Callable<Object,[Object]> f = nothing;
    value objectArray1 = ObjectArray<Object>(0);
    // requires an Object cast
    f(objectArray1);
    value intArray = IntArray(0);
    // does not require an Object cast
    f(intArray);
    // also requires an Object cast
    Callable<Object,[Object?]> f2 = nothing;
    f2(null);
    
    java.variadicObject(null); // args[0] == null
    java.variadicObject(arr1); // args[0] == arr1
    java.variadicObject(*arr1.iterable); // args == arr1
    java.variadicInt(*intArray.iterable); // args == intArray
}
