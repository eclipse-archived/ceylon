class ClassWithBrokenMembers(){
    fuuu()();
    
    MissingType brokenMethod() {
        fuuuuuu()();
    }
    
    MissingType brokenGetter {
        fuuuuuu()();
    }

    assign brokenGetter {
        fuuuuuu()();
    }

    MissingType brokenAttribute = fuuuuuu()();

    MissingType obj {
        fu=bar;
    }
}
interface InterfaceWithBrokenMembers{
    fuuu()();
    
    MissingType brokenMethod() {
        fuuuuuu()();
    }
    
    MissingType brokenGetter {
        fuuuuuu()();
    }

    assign brokenGetter {
        fuuuuuu()();
    }

    MissingType brokenAttribute = fuuuuuu()();

    MissingType obj {
        fu=bar;
    }
}
