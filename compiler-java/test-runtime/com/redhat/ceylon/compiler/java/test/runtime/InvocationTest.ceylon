class Foo(Integer i = 1, Integer... rest={2, 3, 4}) {
    shared Integer first = i;
    shared Integer[] rest = rest;
}

shared class InvocationTest() {
    @test
    shared void testInitializerDefaultedAndSequenced() {
        variable Foo foo := Foo();
        assertEquals(1, foo.first);
        assertEquals({2, 3, 4}, foo.rest);
        
        foo := Foo(2);
        assertEquals(2, foo.first);
        assertEquals({2, 3, 4}, foo.rest);
        
        foo := Foo(10, 11, 12);
        assertEquals(10, foo.first);
        assertEquals({11, 12}, foo.rest);
    } 
    
}