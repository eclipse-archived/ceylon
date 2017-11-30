interface I {
    String first1([String, String] pair)
            =>  let ([first,second] = pair) first; // error
    // Statement or initializer may not occur directly in interface body
    
    String first2([String, String] pair) {
        return let ([first,second] = pair) first; // ok
    }
}

class C() {
    String first1([String, String] pair)
            =>  let ([first,second] = pair) first; // error
    // Statement or initializer may not occur directly in interface body
    
    String first2([String, String] pair) {
        return let ([first,second] = pair) first; // ok
    }
}