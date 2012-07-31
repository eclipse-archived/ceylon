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
            @type["Integer"] value n = test();
        }
        @type["Integer"] function f() {
            return test();
        }
    }
    
    class BackwardTypeInference(){
        void m(){
            @type["unknown"] @error value n = test();
        }
        @type["unknown"] function f() {
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
        
        @error @type["unknown"] value x = burp;
        
        @type["unknown"] value y {
            @error return burp;
        }
        
        @type["unknown"] function f() {
            @error return burp;
        }
        
        @error @type["unknown"] function g() = burp;
        
        @type["Sequence<unknown>"] value seq = { @error burp };
        
        @type["Sequence<unknown>"] function createSeq() {
            @type["Sequence<unknown>"] return { @error hi };
        }
        
        Sequence<T> singleton<T>(T element) {
            return {element};
        }
        
        @type["Sequence<unknown>"] @error value sing = singleton(hi);
        
        value hi = "hi";
        
    }
    
    class MultipleReturnTypeInference() {
        
        @type["String|Float"] function inferredMethod() {
            if (true) {
                return "Hello";
            }
            else {
                return 0.0;
            }
        }
        
        @type["String|Integer"] value inferredGetter {
            if (true) {
                return "Hello";
            }
            else {
                return +1;
            }
        }
        
        @type["String|Float|Integer"] function method() {
            if (1==0) {
                return inferredMethod();
            }
            else {
                return inferredGetter;
            }
        }
        
        @type["Bottom"] function f() {
            throw;
        }
        
        @type["Bottom"] value v {
            throw;
        }
        
    }
    
    class NamedArgTypeInference() {
        function join(String x, String y, String f(String z)) {
            return f(x+" "+y);
        }
        @type["String"] value hw = join {
            value x { return "Hello"; }
            value y { return "Hello"; }
            function f(String z) { return z.uppercased; }
        };
        @type["String"] value broken = join {
            value x { return "Hello"; }
            @error value y { return 0; }
            @error function f(Integer z) { return z; }
        };
    }

}