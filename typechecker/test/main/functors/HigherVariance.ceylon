class Variant<C, T>() 
        given C<E> 
        given T satisfies Object {
    shared C<out T> getOut() => nothing;
    shared C<in T> getIn() => nothing;
    
    shared Inv<out C<in T>> getOutIn() => nothing;
    shared C<in Inv<out T>> getInOut() => nothing;
    
    shared Inv<out C<out T>> getOutOut1() => nothing;
    shared C<out Inv<out T>> getOutOut2() => nothing;
    
    shared Inv<in C<in T>> getInIn1() => nothing;
    shared C<in Inv<in T>> getInIn2() => nothing;
    
    shared Inv<out T> getOut0() => nothing;
    shared Inv<in T> getIn0() => nothing; 
    
    shared Inv<out Inv<in T>> getOutIn0() => nothing;
    shared Inv<in Inv<out T>> getInOut0() => nothing;
    
    shared Inv<out Inv<out T>> getOutOut10() => nothing;
    shared Inv<out Inv<out T>> getOutOut20() => nothing;
    
    shared Inv<in Inv<in T>> getInIn10() => nothing;
    shared Inv<in Inv<in T>> getInIn20() => nothing;
    
    shared C<out C<in T>> outIn() => nothing;
    shared C<in C<out T>> inOut() => nothing;
    shared C<out C<out T>> outOut() => nothing;
    shared C<in C<in T>> inIn() => nothing;
    
}

interface Inv<T> {
    shared T get() => nothing;
    shared T accept(T t) => t;
}

void testVariantGet(Variant<Inv, in Integer> foo, 
    Variant<Inv, out Integer> bar) {
    @type:"Object" value fooOut = foo.getOut().get();
    @type:"Anything" value fooIn = foo.getIn().get();
    @type:"Integer" value barOut = bar.getOut().get();
    @type:"Anything" value barIn = bar.getIn().get();
    
    @type:"Object" value fooOut0 = foo.getOut0().get();
    @type:"Anything" value fooIn0 = foo.getIn0().get();
    @type:"Integer" value barOut0 = bar.getOut0().get();
    @type:"Anything" value barIn0 = bar.getIn0().get();
    
    @type:"Anything" value fooOutIn = foo.getOutIn().get().get();
    @error value fooInOut = foo.getInOut().get().get();
    @type:"Anything" value barOutIn = bar.getOutIn().get().get();
    @error value barInOut = bar.getInOut().get().get();
    
    @type:"Anything" value fooOutIn0 = foo.getOutIn0().get().get();
    @error value fooInOut0 = foo.getInOut0().get().get();
    @type:"Anything" value barOutIn0 = bar.getOutIn0().get().get();
    @error value barInOut0 = bar.getInOut0().get().get();
    
    @type:"Object" value fooOutOut1 = foo.getOutOut1().get().get();
    @type:"Object" value fooOutOut2 = foo.getOutOut2().get().get();
    @type:"Integer" value barOutOut1 = bar.getOutOut1().get().get();
    @type:"Integer" value barOutOut2 = bar.getOutOut2().get().get();
    
    @type:"Object" value fooOutOut10 = foo.getOutOut10().get().get();
    @type:"Object" value fooOutOut20 = foo.getOutOut20().get().get();
    @type:"Integer" value barOutOut10 = bar.getOutOut10().get().get();
    @type:"Integer" value barOutOut20 = bar.getOutOut20().get().get();
    
    @error value fooInIn1 = foo.getInIn1().get().get();
    @error value fooInIn2 = foo.getInIn2().get().get();
    @error value barInIn1 = bar.getInIn1().get().get();
    @error value barInIn2 = bar.getInIn2().get().get();
    
    @error value fooInIn10 = foo.getInIn10().get().get();
    @error value fooInIn20 = foo.getInIn20().get().get();
    @error value barInIn10 = bar.getInIn10().get().get();
    @error value barInIn20 = bar.getInIn20().get().get();
    
    @error foo.inOut().get().get();
    @type:"Anything" foo.outIn().get().get();
    @error foo.inIn().get().get();
    @type:"Object" foo.outOut().get().get();
    
    @error bar.inOut().get().get();
    @type:"Anything" bar.outIn().get().get();
    @error bar.inIn().get().get();
    @type:"Integer" bar.outOut().get().get();
}

void testVariantAccept(Variant<Inv, in Integer> foo, 
    Variant<Inv, out Integer> bar) {
    @type:"Object(Nothing)" 
    value fooOut = foo.getOut().accept;
    @type:"Anything(Integer)" 
    value fooIn = foo.getIn().accept;
    @type:"Integer(Nothing)" 
    value barOut = bar.getOut().accept;
    @type:"Anything(Nothing)" 
    value barIn = bar.getIn().accept;
    
    @type:"Object(Nothing)" 
    value fooOut0 = foo.getOut0().accept;
    @type:"Anything(Integer)" 
    value fooIn0 = foo.getIn0().accept;
    @type:"Integer(Nothing)" 
    value barOut0 = bar.getOut0().accept;
    @type:"Anything(Nothing)" 
    value barIn0 = bar.getIn0().accept;
}