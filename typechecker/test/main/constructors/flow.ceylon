class Foobar1 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    shared new Foo(Integer val) {
        a = val;
        return;        
    }
    
    shared new Bar(Integer val) {
        print(c);
        a = val;
    }
    
}

class Foobar2 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    
    @error return;
    
    @error
    shared new Foo(Integer val) {
        a = val;
        return;        
    }
    
    @error
    shared new Bar(Integer val) {
        print(c);
        a = val;
    }
    
}

class Foobar3 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    shared new Foo(Integer val) {
        @error return;
        @error a = val;
    }
    
    shared new Bar(Integer val) {
        print(c);
        a = val;
    }
    
}