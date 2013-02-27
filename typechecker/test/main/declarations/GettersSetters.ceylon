interface GettersSetters {
    
    class Setters() {
        String hi { return "hi"; }
        assign hi {}
        @error assign howdy {  }
        @error assign hi {}
    }
    
    class SettersWithDupeParams(String hi, String howdy) {
        @error String hi { return "hi"; }
        @error assign howdy {  }
    }
    
    class SettersWithDupeParams2(String hi, String howdy) {
        @error String hi { return "hi"; }
        @error assign howdy {  }
        @error assign hi {}
        @error assign hi {}
    }
    
    class SettersWithDupeAttributes() {
        @error String hi; 
        @error String howdy;
        @error String hi { return "hi"; }
        @error assign hi {}
        @error assign howdy {  }
        @error assign hi {}
    }
    
    class SharedGetterSetter() {
        shared String name {
            return "gavin";
        }
        assign name {}
        String greeting {
            return "hello";
        }
        assign greeting {}
    }
    
    void test() {
        SharedGetterSetter().name = "world";
        @error SharedGetterSetter().greeting = "hi";
    }
    
}