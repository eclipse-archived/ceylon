import java.lang{ObjectArray}

@noanno
void bug6632(
    ObjectArray<Integer->Float(Integer)> entries,
    ObjectArray<[Integer, Float(Integer)]> twoTuple,
    ObjectArray<[Integer, Float(Integer)+]> moreTuple) {
    
    for (Integer i -> Float(Integer) aggregator in entries) {
        aggregator(i);
    }
    
    for ([Integer i, Float(Integer) aggregator] in twoTuple) {
        aggregator(1);
    }
}