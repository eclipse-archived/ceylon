import java.lang{IntArray}

class Bug1467() {
    Integer size = 1024*1024/4;
    IntArray buff = IntArray(size);

    void bufferTest() {
        for (i in 0:size-1) {
            buff.set(i, i);
        }
    }

    shared void buffer() {
        Integer startTime = system.nanoseconds;

        for (i in 1..100) {
            bufferTest();
        }

        print("Duration: ``(system.nanoseconds - startTime)/100``\n");
    }
}

void bug1467() {
    Bug1467 b = Bug1467();
    b.buffer();
}