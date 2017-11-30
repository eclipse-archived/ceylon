
@noanno
shared void bug2322() {
    value stream = { for (i in 0..10) i };
    value sequence = [ for (i in 0..10) i ];
}
