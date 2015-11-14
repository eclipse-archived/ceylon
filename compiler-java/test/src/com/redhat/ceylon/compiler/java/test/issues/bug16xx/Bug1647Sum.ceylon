void bug1647Sum() {
    value t0 = system.nanoseconds;
    sum(0..1M);
    if (system.nanoseconds-t0 > 5000M) {//5s
        throw Exception("Too slow sum(0..10M) ran too slow ``(system.nanoseconds-t0)/1000M``s");
    }
}