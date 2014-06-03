import java.lang{LongArray, IntArray}
@noanno
void bug1559(IntArray a) {
    value h = a.hash;
    value size1 = 1;
    value array1 = LongArray(size1);
    value size2 = array1.size;
    value array2 = LongArray(size2);
    value array3 = LongArray(array1.size);
    value size3 = "".hash;
    value array4 = IntArray(size3, 1);
    
    value x = a.get(1);
    a.set(1, 1);
}