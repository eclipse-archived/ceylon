import java.lang{ObjectArray}

@noanno
void bug6632(
    ObjectArray<Integer->Float(Integer)> entries,
    ObjectArray<[Integer, Float(Integer)]> twoTuple,
    ObjectArray<[Integer, Float(Integer)+]> moreTuple) {
    
    for (Integer i -> Float(Integer) aggregator in entries) {
        aggregator(i);
    }
    for ([Integer i, Float(Integer) aggregator] in entries) {
        aggregator(1);
    }
    
    for (Integer i -> Float(Integer) aggregator in twoTuple) {
        aggregator(i);
    }
    
    for ([Integer i, Float(Integer) aggregator] in twoTuple) {
        aggregator(1);
    }
    
    for (Integer i -> [Float(Integer)+] aggregator in moreTuple) {
        for (agg in aggregator) {
            agg(i);
        }
    }
    
    for (Integer i -> [Float(Integer) aggregator, Float(Integer)[] *aggregators] in moreTuple) {
        aggregator(i);
    }
    
    for ([Integer i, Float(Integer) aggregator, Float(Integer) *aggregators] in moreTuple) {
        aggregator(i);
        for (agg in aggregators) {
            agg(i);
        }
        
    }
}