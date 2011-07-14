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
            @type["Natural"] value n = test();
        }
        @type["Natural"] function f() {
            return test();
        }
    }
    
    class BackwardTypeInference(){
        void m(){
            @type["Void"] @error value n = test();
        }
        @type["Void"] function f() {
            @error return test();
        }
        function test(){
            return 0;    
        }
    }

}