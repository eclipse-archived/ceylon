interface ConcreteString {
    shared void presentYourself(){
        assert(equals(this));
        assert(2 == hash);
        assert("foo" == string);
    }
}
class ConcreteStringC() satisfies ConcreteString {
    string => "foo";
    hash => 2;
}

interface ConcreteString2 {
    shared void presentYourself(){
        assert(equals(this));
        /* Uncomment me when Gavin fixes ceylon/ceylon-spec/issues/793
        assert(3 == hash);
        assert("bar" == string);
        */
    }
    
    string => "bar";
    hash => 3;
}
class ConcreteStringC2() satisfies ConcreteString2 {
    // you'll need to override string and hash here when Gavin fixes ceylon/ceylon-spec/issues/793
}

void concreteString() {
    ConcreteStringC().presentYourself();
    ConcreteStringC2().presentYourself();
}

