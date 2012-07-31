void testAssignment() {
    count:=1;
    @error amount = 1.2;
    @error amount := 1.2;
    @error count=2;
    ClassWithMutable("hello", "world").x:="goodbye";
    @error ClassWithMutable("hello", "world").x="goodbye";
    @error ClassWithMutable("goodbye", "world").y:="hello";
    @error ClassWithMutable("goodbye", "world").y="hello";
}