interface In<T> {}
interface Co<out T> {}
interface Contra<in T> {}
interface Mixed<in U, out V> {}

interface My<T> {
    shared formal variable T var;
    shared formal In<T> foo;
    shared formal In<In<T>> bar;
    shared formal In<Co<T>> baz;
    shared formal In<Contra<T>> qux;
    shared formal void setFoo(In<T> foo);
    shared formal void setBar(In<In<T>> bar);
    shared formal Co<T> co;
    shared formal Co<Co<T>> coco;
    shared formal Contra<T> contra;
    shared formal Contra<Contra<T>> contracontra;
    shared formal void setCo(Co<T> co);
    shared formal void setContra(Contra<T> co);
    shared formal Mixed<T,T> mixed;
    shared formal In<Mixed<T,T>> invmixed;
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
    
    @type: "Co<String>" value outco = myout.co;
    @type: "Co<Co<String>>" value outcoco = myout.coco;
    @type: "Contra<Nothing>" value outcontra = myout.contra;
    @type: "Contra<Contra<String>>" value outcontracontra = myout.contracontra;
    @type: "Co<Anything>" value inco = myin.co;
    @type: "Co<Co<Anything>>" value incoco = myin.coco;
    @type: "Contra<String>" value incontra = myin.contra;
    @type: "Contra<Contra<Anything>>" value incontracontra = myin.contracontra;
    
    @type: "Mixed<Nothing,String>" value outmixed = myout.mixed;
    @type: "Mixed<String,Anything>" value inmixed = myin.mixed;
    @type: "In<out Mixed<Nothing,String>>" value outinvmixed = myout.invmixed;
    @type: "In<out Mixed<String,Anything>>" value ininvmixed = myin.invmixed;
    
    @error myout.setCo(outco);
    myin.setCo(outco);
    myout.setContra(incontra);
    @error myin.setContra(incontra);
    
    @type: "String" value outname = myout.var;
    @error myout.var = "";
    @error myout.var = 1;
    @type: "Anything" value inname = myin.var;
    myin.var = "";
    @error myin.var = 1;
}