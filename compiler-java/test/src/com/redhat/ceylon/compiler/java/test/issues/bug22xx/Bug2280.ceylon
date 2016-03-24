@noanno
shared void bug2280() {
    void bar(String? f) {}
    void baz(Integer? f) {}
    void gee(Comparison? f) {}
    bar(if (true) then null else null);
    baz(if (true) then null else null);
    gee(if (true) then null else null);
    bar { f=if (true) then null else null; };
    Integer i = nothing;
    bar( switch(i) case(1) null else null );
    bar { f=switch(i) case(1) null else null; };
    
}