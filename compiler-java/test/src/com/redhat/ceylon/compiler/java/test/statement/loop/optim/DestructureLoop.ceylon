import java.lang{JIterable=Iterable, ObjectArray}

@noanno
class DestructureLoop() {
    void iterable(
        Iterable<Integer->Float> entries,
        Iterable<Integer->[Integer,Float]> entryTuple,
        Iterable<[Integer, Integer->Float]> tupleEntry,
        Iterable<[Integer, Float]> twoTuple,
        Iterable<[Integer, Float+]> moreTuple,
        Iterable<[Integer, [Integer, Float]]> nestedTuple) {
        
        for (Integer i -> Float f in entries) {
            print(i+f);
        }
        for (Integer i -> [Integer j, Float f] in entryTuple) {
            print(i+j+f);
        }
        for ([Integer i, Integer j-> Float f] in tupleEntry) {
            print(i+j+f);
        }
        
        for ([Integer i, Float f] in twoTuple) {
            print(i+f);
        }
        
        for ([Integer i, Float* f] in moreTuple) {
            //print(i+f);
        }
        
        for ([Integer i, [Integer j, Float f]] in nestedTuple) {
            print(i+j+f);
        }
    }
    void array(
        Array<Integer->Float> entries,
        Array<Integer->[Integer,Float]> entryTuple,
        Array<[Integer, Integer->Float]> tupleEntry,
        Array<[Integer, Float]> twoTuple,
        Array<[Integer, Float+]> moreTuple,
        Array<[Integer, [Integer, Float]]> nestedTuple) {
        
        for (Integer i -> Float f in entries) {
            print(i+f);
        }
        for (Integer i -> [Integer j, Float f] in entryTuple) {
            print(i+j+f);
        }
        for ([Integer i, Integer j-> Float f] in tupleEntry) {
            print(i+j+f);
        }
        
        for ([Integer i, Float f] in twoTuple) {
            print(i+f);
        }
        
        for ([Integer i, Float* f] in moreTuple) {
            //print(i+f);
        }
        
        for ([Integer i, [Integer j, Float f]] in nestedTuple) {
            print(i+j+f);
        }
    }
    void javaIterable(
        JIterable<Integer->Float> entries,
        JIterable<Integer->[Integer,Float]> entryTuple,
        JIterable<[Integer, Integer->Float]> tupleEntry,
        JIterable<[Integer, Float]> twoTuple,
        JIterable<[Integer, Float+]> moreTuple,
        JIterable<[Integer, [Integer, Float]]> nestedTuple) {
        
        for (Integer i -> Float f in entries) {
            print(i+f);
        }
        for (Integer i -> [Integer j, Float f] in entryTuple) {
            print(i+j+f);
        }
        for ([Integer i, Integer j-> Float f] in tupleEntry) {
            print(i+j+f);
        }
        
        for ([Integer i, Float f] in twoTuple) {
            print(i+f);
        }
        
        for ([Integer i, Float* f] in moreTuple) {
            //print(i+f);
        }
        
        for ([Integer i, [Integer j, Float f]] in nestedTuple) {
            print(i+j+f);
        }
    }
    void objectArray(
        ObjectArray<Integer->Float> entries,
        ObjectArray<Integer->[Integer,Float]> entryTuple,
        ObjectArray<[Integer, Integer->Float]> tupleEntry,
        ObjectArray<[Integer, Float]> twoTuple,
        ObjectArray<[Integer, Float+]> moreTuple,
        ObjectArray<[Integer, [Integer, Float]]> nestedTuple) {
        
        for (Integer i -> Float f in entries) {
            print(i+f);
        }
        for (Integer i -> [Integer j, Float f] in entryTuple) {
            print(i+j+f);
        }
        for ([Integer i, Integer j-> Float f] in tupleEntry) {
            print(i+j+f);
        }
        
        for ([Integer i, Float f] in twoTuple) {
            print(i+f);
        }
        
        for ([Integer i, Float* f] in moreTuple) {
            //print(i+f);
        }
        
        for ([Integer i, [Integer j, Float f]] in nestedTuple) {
            print(i+j+f);
        }
    }
}