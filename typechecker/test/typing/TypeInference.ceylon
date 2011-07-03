interface TypeInference {
    
    class Inference() {
    
        @type["String"] function inferredMethod() {
            return "Hello";
        }
        
        @type["String"] value inferredGetter {
            return "Hello";
        }
        
        @type["String"] value inferredAttribute = "Hello";
        
        @error value brokenInferredMethod() {
            return "Hello";
        }
        
        @error function brokenInferredGetter {
            return "Hello";
        }
        
        @error function brokenInferredAttribute = "Hello";
        
    }
    
    class Setters() {
        String hi { return "hi"; }
        assign hi {}
        @error assign howdy {  }
        @error assign hi {}
    }
    
    class SettersWithDupeParams(String hi, String howdy) {
        String hi { return "hi"; }
        assign hi {}
        @error assign howdy {  }
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
    
    class ForwardTypeInference(){
        function test(){
            return 0;    
        }
        void m(){
            value n = test();
        }
    }
    
    class BackwardTypeInference(){
        void m(){
            @error value n = test();
        }
        function test(){
            return 0;    
        }
    }

}