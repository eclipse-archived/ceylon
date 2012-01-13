@error
class ClassWithBrokenMembers(){
    @error
    fuuu()();
    
    @error
    MissingType brokenMethod() {
        @error
        fuuuuuu()();
    }
    
    @error
    MissingType brokenGetter {
        @error
        fuuuuuu()();
    }

    assign brokenGetter {
        @error
        fuuuuuu()();
    }

    @error
    MissingType brokenAttribute = fuuuuuu()();

    @error
    MissingType obj {
        @error
        fu=bar;
    }
}
@error
interface InterfaceWithBrokenMembers{
    @error
    fuuu()();
    
    @error
    MissingType brokenMethod() {
        @error
        fuuuuuu()();
    }
    
    @error
    MissingType brokenGetter {
        @error
        fuuuuuu()();
    }

    @error
    assign brokenGetter {
        @error
        fuuuuuu()();
    }

    @error
    MissingType brokenAttribute = fuuuuuu()();

    @error
    MissingType obj {
        @error
        fu=bar;
    }
}
