import ceylon.math.whole{Whole}

@nomodel
shared interface Bug2361A<Other> 
        given Other satisfies Bug2361A<Other> {
    shared formal void bar(Other other);
    shared formal void gee<Indirect>(Indirect other)
        given Indirect satisfies Other;
}
@nomodel
interface Bug2361 {}
@nomodel
shared interface Bug2361B<Other> 
        given Other satisfies Bug2361&Bug2361B<Other> {
    shared formal void bar(Other other);
}
@nomodel
shared void bug2361(){
    variable Anything r;
    Bug2361A<Nothing> foo = nothing;
    r = foo.bar;
    r = foo.gee<Nothing>;
    r = (nothing of Bug2361A<Nothing[2]>).bar;
    r = (nothing of Bug2361A<String|Integer>).bar;
    r = (nothing of Bug2361B<Nothing>).bar;
    r = (nothing of Bug2361B<Nothing[2]>).bar;
    r = (nothing of Bug2361B<String|Integer>).bar;
    
    r = Bug2361B<Nothing[2]>.bar;
    r = Bug2361B<Nothing>.bar;
    r = Bug2361A<Nothing[2]>.bar;
    r = Bug2361A<Nothing>.bar;
    r = Bug2361A<Nothing>.gee<Nothing>;
    
    r = plus<Integer>;
    r = Integer.plus;
    r = Whole.plus;
}
