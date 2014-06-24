class Bug1686() satisfies Enumerable<Bug1686>{
    void f() {
        value x = (1..3);
        value y = x.withLeading(0);
        
        value x2 = Bug1686()..Bug1686();
        value y2 = x2.withLeading(Bug1686());
    }
    
    shared actual Bug1686 neighbour(Integer off) => nothing;
    shared actual Integer offset(Bug1686 other) => nothing;
}