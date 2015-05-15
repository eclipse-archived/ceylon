import ceylon.language { Any=Anything }

interface Inv<T> {}
interface I1 => Iterable<String>;
interface I2<S> => Iterable<S>;
interface I3 => Iterable;
@error interface I4 => Inv;
class E1(String name, Integer i) => Entry<String,Integer>(name,i);
class E2<T>(String name, T t) given T satisfies Object => Entry<String,T>(name,t);
class E3<T>(T t, String s) given T satisfies Object => Entry<T,String>(t,s);
void pnt(Any s) => print(s);
@error void throwAway(Float x) => x;
Any prn(Any s) => print(s);
Entry<String,Integer> e1(String name, Integer i) => Entry<String,Integer>(name, i);
Entry<String,T> e2<T>(String name, T t) given T satisfies Object => Entry<String,T>(name,t);
Entry<String,Integer> e3(String name, Integer i) => Entry(name,i);
@error Callable<Entry<Object,Object>,Object,Object> e5 = Entry;
abstract class F2(){}
abstract class F() => F2();

alias EA<K,I> given K satisfies Object => K->I(K,I);

void check() { 
    E1 g1 = E1("gavin",1); 
    E2<String> g2 = E2<String>("gavin","king"); 
    E3<String> g3 = E3<String>("gavin","king"); 
    E1 g4 = e1("gavin",1); 
    E2<String> g5 = e2("gavin","king"); 
    E3<String> g6 = e2("gavin","king"); 
    @error F();
    @type:"<Key, Item> given Key satisfies Object => Callable<Entry<Key,Item>,Tuple<Key|Item,Key,Tuple<Item,Item,Empty>>>" 
    value e4 = Entry;
}