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
        assert(3 == hash);
        assert("bar" == string);
    }
    
    shared actual default String string => "bar";
    shared actual default Integer hash => 3;
}
class ConcreteStringC2() satisfies ConcreteString2 {
    string => (super of ConcreteString2).string;
    hash => (super of ConcreteString2).hash;
}

void concreteString() {
    ConcreteStringC().presentYourself();
    ConcreteStringC2().presentYourself();
}

