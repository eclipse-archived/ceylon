class Foobar1 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    shared new foo(Integer val) {
        a = val;
        return;        
    }
    
    shared new bar(Integer val) {
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
    shared new foo(Integer val) {
        a = val;
        return;        
    }
    
    @error
    shared new bar(Integer val) {
        print(c);
        a = val;
    }
    
}

class Foobar3 {
    shared Integer a;
    shared Integer c;    
    c = 3;
    
    shared new foo(Integer val) {
        @error return;
        @error a = val;
    }
    
    shared new bar(Integer val) {
        print(c);
        a = val;
    }
    
}