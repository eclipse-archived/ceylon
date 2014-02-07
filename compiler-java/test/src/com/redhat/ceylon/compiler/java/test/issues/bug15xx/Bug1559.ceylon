import java.lang{LongArray}
@noanno
void bug1559(IntArray a) {
    value h = a.hash;
    value size1 = 1;
    value array1 = LongArray(size1);
    value size2 = array1.size;
    value array2 = LongArray(size2);// fails
    value array3 = LongArray(array1.size);
    value size3 = "".hash;//TODO array1.hash
    value array4 = LongArray(size3);
}