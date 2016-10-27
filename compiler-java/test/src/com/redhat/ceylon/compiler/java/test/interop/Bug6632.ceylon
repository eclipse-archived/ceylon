import java.lang{ObjectArray}

void bug6632(ObjectArray<Integer->Float(Integer)> indexed) {
    for (Integer i -> Float(Integer) aggregator in indexed) {
        aggregator(1);
    }
}