interface In<T> {}
interface Co<out T> {}
interface Contra<in T> {}

interface My<T> {
    shared formal In<T> foo;
    shared formal In<In<T>> bar;
    shared formal In<Co<T>> baz;
    shared formal In<Contra<T>> qux;
    shared formal void setFoo(In<T> foo);
    shared formal void setBar(In<In<T>> bar);
}

void check(My<out String> myout, My<in String> myin) {
    @type: "In<out String>" value outfoo = myout.foo;
    @type: "In<out In<out String>>" value outbar = myout.bar;
    @type: "In<out Co<String>>" value outbaz = myout.baz;
    @type: "In<out Contra<Nothing>>" value outqux = myout.qux;
    @type: "In<in String>" value infoo = myin.foo;
    @type: "In<out In<in String>>" value inbar = myin.bar;
    @type: "In<out Co<Anything>>" value inbaz = myin.baz;
    @type: "In<out Contra<String>>" value inqux = myin.qux;

    @type: "Callable<Anything,Tuple<Nothing,Nothing,Empty>>" 
    value outsetfoo = myout.setFoo;
    @type: "Callable<Anything,Tuple<Nothing,Nothing,Empty>>" 
    value outsetbar = myout.setBar;
    @type: "Callable<Anything,Tuple<Nothing,Nothing,Empty>>" 
    value insetfoo = myin.setFoo;
    @type: "Callable<Anything,Tuple<Nothing,Nothing,Empty>>" 
    value insetbar = myin.setBar;    
}