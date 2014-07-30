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
    ShortArray,
    LongArray,
    IntArray, 
    ByteArray, 
    DoubleArray, 
    FloatArray, 
    CharArray, 
    BooleanArray,
    ObjectArray
}

@noanno
class JavaArrayIterationStatic() {
    void arrayOfInts(IntArray array) {
        for (element in array.iterable) {
            print(element + 5);
        }
    }
    void arrayOfIntsBy(IntArray array) {
        for (element in array.iterable by 2) {
            print(element + 5);
        }
    }
    void arrayOfIntsDisabled(IntArray array) {
        @disableOptimization:"JavaArrayIterationStatic"
        @disableOptimization:"ArrayIterationDynamic"
        for (element in array.iterable) {
            print(element + 5);
        }
        @disableOptimization
        for (element in array.iterable) {
            print(element + 5);
        }
    }
    void arrayOfChars(CharArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfCharsBy(CharArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    void arrayOfBytes(ByteArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfBytesBy(ByteArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    void arrayOfShorts(ShortArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfShortsBy(ShortArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    void arrayOfLongs(LongArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfLongsBy(LongArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    void arrayOfBooleans(BooleanArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfBooleansBy(BooleanArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    void arrayOfFloats(FloatArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfFloatsBy(FloatArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    void arrayOfDoubles(DoubleArray array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void arrayOfDoublesBy(DoubleArray array) {
        for (element in array.iterable by 2) {
            print(element);
        }
    }
    // native arrays
    void nativeArrayOfObjects(ObjectArray<Object> array) {
        for (element in array.array) {
            print(element);
        }
    }
    void nativeArrayOfObjectsIterable(ObjectArray<Object> array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    void nativeArrayOfObjectsOpt(ObjectArray<Object?> array) {
        for (element in array.array) {
            print(element else "null");
        }
    }
    void nativeArrayOfObjectsOptIterable(ObjectArray<Object?> array) {
        for (element in array.iterable) {
            print(element else "null");
        }
    }
    void nativeArrayOfObjectsTp<T>(ObjectArray<T> array) {
        for (element in array.array) {
            print(element);
        }
    }
    void nativeArrayOfObjectsTpIterable<T>(ObjectArray<T> array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    
    void nativeArrayOfObjectsTpOpt<T>(ObjectArray<T?> array) {
        for (element in array.array) {
            print(element else "null");
        }
    }
    void nativeArrayOfObjectsTpOptIterable<T>(ObjectArray<T?> array) {
        for (element in array.iterable) {
            print(element else "null");
        }
    }
    
    void nativeArrayOfIntegers(ObjectArray<Integer> array) {
        for (element in array.array) {
            print(element);
        }
    }
    void nativeArrayOfIntegersIterable(ObjectArray<Integer> array) {
        for (element in array.iterable) {
            print(element);
        }
    }
    
    void nativeArrayOfIntegersOpt(ObjectArray<Integer> array) {
        for (element in array.array) {
            print(element else 0);
        }
    }
    void nativeArrayOfIntegersOptIterable(ObjectArray<Integer?> array) {
        for (element in array.iterable) {
            print(element else 0);
        }
    }
}