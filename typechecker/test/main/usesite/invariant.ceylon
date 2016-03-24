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

interface Pair<First, Second> {
    shared formal Pair<First, Second> clone();
    shared formal Pair<out First, out Second> clone2();
    shared formal Pair<in First, in Second> clone3();
    shared formal List<Second> list();
}

void multicheck(Pair<String, out Object> myout, Pair<String, in String> myin) {
    @type:"Pair<String,out Object>" myout.clone();
    @type:"Pair<String,in String>" myin.clone();
    @type:"Pair<out String,out Object>" myout.clone2();
    @type:"Pair<out String,out Anything>" myin.clone2();
    @type:"Pair<in String,in Nothing>" myout.clone3();
    @type:"Pair<in String,in String>" myin.clone3();
    
    
    @type:"List<Object>" myout.list();
    @type:"List<Anything>" myin.list();
}

interface Constrained<X> 
        given X satisfies Identifiable {
    shared formal List<X> list;
    shared formal Constrained<X> clone;
    shared formal Constrained<out X> clone1;
    shared formal Constrained<in X> clone2;
}

class Foo() {}

void constrainedCheck(Constrained<out Foo> myout, Constrained<in Foo> myin) {
    @type:"List<Foo>" value _1 = myout.list;
    @type:"List<Identifiable>" value _2 = myin.list;
    @type:"Constrained<out Foo>" value _3 = myout.clone;
    @type:"Constrained<in Foo>" value _4 = myin.clone;
    @type:"Constrained<out Foo>" value _5 = myout.clone1;
    @type:"Constrained<out Identifiable>" value _6 = myin.clone1;
    @type:"Constrained<in Nothing>" value _7 = myout.clone2;
    @type:"Constrained<in Foo>" value _8 = myin.clone2;
}

void instantiations() {
    
    interface Set<T> {}
    
    Set<String> & Set<out Object> set1 = nothing;
    Set<Object> & Set<in String> set2 = nothing;
    Set<out String> & Set<out Integer> set3 = nothing;
    Set<in Integer> & Set<in String> set4 = nothing;
    Set<Integer> & Set<String> set5 = nothing;
    Set<Integer> & Set<in String> set6 = nothing;
    Set<Integer> & Set<out String> set7 = nothing;
    @type:"Set<out Nothing>" value ss3 = set3;
    @type:"Set<in Integer|String>" value ss4 = set4;
    @type:"Set<String>" value ss1 = set1;
    @type:"Set<Object>" value ss2 = set2;
    @type:"Nothing" value ss5 = set5;
    @type:"Nothing" value ss6 = set5;
    @type:"Nothing" value ss7 = set5;

}