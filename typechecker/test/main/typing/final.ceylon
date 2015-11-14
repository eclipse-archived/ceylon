void testFinal() {
    
    final class Broken() {
        @error shared default void foo() {}
        @error shared default String bar => "";
    }
    
    @error abstract final class ReallyBroken() {}
    
    interface X {}
    final class Y() {
        shared void foo() {}
        this.foo();
        foo();
    }
    
    X&Y xy = nothing;
    @type:"Nothing" value n = xy;
}

void testNonfinal() {

    interface X {}
    class Y() {
        shared default void foo() {}
        @error this.foo();
        @error foo();
    }
    
    X&Y xy = nothing;
    @type:"X&Y" value n = xy;
}