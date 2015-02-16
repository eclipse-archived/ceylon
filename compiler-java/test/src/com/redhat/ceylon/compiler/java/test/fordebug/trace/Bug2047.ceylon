shared void bug2047() {
    value f = (String name) 
            => name.uppercased;
    suppressWarnings("unusedDeclaration")
    String string;
    string =  
            f("hello");
}