void testAssignment() {
    count=1;
    @error amount = 1.2;
    count=2;
    ClassWithMutable("hello", "world").x="goodbye";
    @error ClassWithMutable("goodbye", "world").y="hello";
    Float loc;
    variable Float size;
    void f() {
        size = 1.0;
        @error loc = 0.0;
    }
}