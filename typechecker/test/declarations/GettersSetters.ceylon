interface GettersSetters {
    
    class Setters() {
        String hi { return "hi"; }
        assign hi {}
        @error assign howdy {  }
        @error assign hi {}
    }
    
    class SettersWithDupeParams(String hi, String howdy) {
        String hi { return "hi"; }
        @error assign howdy {  }
    }
    
    class SettersWithDupeParams2(String hi, String howdy) {
        @error String hi { return "hi"; }
        @error assign howdy {  }
        assign hi {}
        @error assign hi {}
    }
    
    class SettersWithDupeAttributs() {
        String hi; 
        String howdy;
        @error String hi { return "hi"; }
        @error assign hi {}
        @error assign howdy {  }
        @error assign hi {}
    }
    
}