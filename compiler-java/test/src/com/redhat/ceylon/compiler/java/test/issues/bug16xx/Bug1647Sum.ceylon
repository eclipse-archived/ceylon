void bug1647Sum() {
    value t0 = system.nanoseconds;
    sum(0..10M);
    if (system.nanoseconds-t0 > 500M) {//0.5s
        throw Exception("Too slow sum(0..10M) ran too slow");
    }
}