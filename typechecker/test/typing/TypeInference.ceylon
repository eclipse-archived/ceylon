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