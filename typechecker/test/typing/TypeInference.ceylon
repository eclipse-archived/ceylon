interface TypeInference {
    
    class Inference() {
    
        @type["String"] function inferredMethod() {
            return "Hello";
        }
        
        @type["String"] value inferredGetter {
            return "Hello";
        }
        
        @type["String"] value inferredAttribute = "Hello";
        
        /*@error value brokenInferredMethod() {
            return "Hello";
        }*/
        
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
    
    class BrokenInference() {
        
        @error value x;
        
        @error value y {}
        
        @error function f() {}
        
        @error function g();
        
        @error class C();
        
        @error interface I;
        
    }

    class UnknownInference() {
        
        @error @type["Void"] value x = burp;
        
        @type["Void"] value y {
            @error return burp;
        }
        
        @type["Void"] function f() {
            @error return burp;
        }
        
        @error @type["Void"] function g() = burp;
        
        @error @type["Sequence<Void>"] value seq = { burp };
        
        @type["Sequence<Void>"] function createSeq() {
            @error @type["Sequence<Void>"] return { hi };
        }
        
        Sequence<T> singleton<T>(T element) {
            return {element};
        }
        
        @type["Sequence<Void>"] @error value sing = singleton(hi);
        
        value hi = "hi";
        
    }

}