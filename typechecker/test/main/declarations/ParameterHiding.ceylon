interface ParameterHiding {

    class Super(String name) {
        @error shared String name = name;
        class Nested1(Sequence<Character> name) {
            @type:"Sequence<Character>" value n = name;
        }
        class Nested2() {
            Sequence<Character> name = [ 'g', 'a', 'v' ];
            @type:"Sequence<Character>" value n = name;
        }
    }
    
    class Sub(Sequence<Character> name) 
            extends Super("gavin") {
        @type:"Sequence<Character>" value n = name;
    }

    class Hiding(String name) {
        @error shared String name = name;
    }
    
    class HidingWithTypeConstraint<T>(String name)
            given T(String name) {
        @error shared String name = name;
        shared T t = T(name);
    }
    
    class AdvancedHiding(Float x, Float y) {
        @error shared String x = x.string;
        @error shared variable Float y = y;
        @type:"Float" value f = x;
    }
    
    void advancedHiding() {
        @error value s = AdvancedHiding(1.0, 2.0).x;
        @error value f = AdvancedHiding(1.0, 2.0).y;
    }
    
}

interface NewParameterHiding {

    class Super(name) {
        shared String name;
        class Nested1(Sequence<Character> name) {
            @type:"Sequence<Character>" value n = name;
        }
        class Nested2() {
            Sequence<Character> name = [ 'g', 'a', 'v' ];
            @type:"Sequence<Character>" value n = name;
        }
    }
    
    class Sub(Sequence<Character> name) 
            extends Super("gavin") {
        @type:"Sequence<Character>" value n = name;
    }

    class Hiding(name) {
        shared String name;
    }
    
    class HidingWithTypeConstraint<T>(name)
            given T(String name) {
        shared String name;
        shared T t = T(name);
    }
    
    class AdvancedHiding(x, y) {
        shared Float x;
        shared variable Float y;
        @type:"Float" value f = x;
    }
    
    void methodHiding(line) {
        variable String line;
        line=line.uppercased;
        print(line);
    }
    
    void advancedHiding() {
        @type:"Float" value s = AdvancedHiding(1.0, 2.0).x;
        @type:"Float" value f = AdvancedHiding(1.0, 2.0).y;
        methodHiding("hello");
    }
    
    class Up(Float x, Float y) {}
    class Down(x,y) extends Up(x,y) {
        shared Float x; variable shared Float y;
    }
    
    void hrrm(s) {
        print(s);
        String s;
    }
    
    class Broken() {
        @error x = 1.0;
        @error y = 0.0;
        @error z = 2.0;
        @error print(x);
        shared Float x;
        @error shared Float y;
        shared variable Float z;
        x = 2.0;
        z = 0.0;
    }
    
}
