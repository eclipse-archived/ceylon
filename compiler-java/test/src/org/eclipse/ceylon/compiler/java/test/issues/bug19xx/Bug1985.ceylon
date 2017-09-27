@noanno
shared void bug1985(){
    void foo(void fp(Integer? b)){
        fp(null);
        
        fp{b=null;};
    }
    void bar(void fp(Integer? b0, Integer?* b)) {
        fp(null);
        fp(null, null);
        fp(null, *[]);
        
        fp{b0=null;};
        fp{b0=null; b=[null];};
        fp{b0=null; b=[];};
    }
    void baz(void fp(Integer?* b)) {
        fp(null);
        fp(null, null);
        fp(*[]);
        fp(null, *[]);
        
        fp{b=[null];};
    }
    foo(void(Integer? b){
        assert(! b exists);
    });
    bar(void(Integer? b0, Integer?* b){
        assert(! b0 exists);
    });
    baz(void(Integer?* b){
        if (nonempty b) {
            assert(! b.first exists);
        } else {
            
        }
    });
}