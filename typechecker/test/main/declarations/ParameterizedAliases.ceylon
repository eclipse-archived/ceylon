import ceylon.language { Any=Anything }

interface I1 => Iterable<String>;
interface I2<S> => Iterable<S>;
@error interface I3 => Iterable;
class E1(String name, Integer i) => Entry<String,Integer>(name,i);
class E2<T>(String name, T t) given T satisfies Object => Entry<String,T>(name,t);
class E3<T>(T t, String s) given T satisfies Object => Entry<T,String>(t,s);
@error void pnt(Any s) => print(s);
Any prn(Any s) => print(s);
Entry<String,Integer> e1(String name, Integer i) => Entry<String,Integer>(name, i);
Entry<String,T> e2<T>(String name, T t) given T satisfies Object => Entry<String,T>(name,t);
Entry<String,Integer> e3(String name, Integer i) => Entry(name,i);
@error Callable<Entry<Object,Object>,Object,Object> e5 = Entry;
class F() => Float();
@error abstract class F1() => Float();

void check() { 
    E1 g1 = E1("gavin",1); 
    E2<String> g2 = E2<String>("gavin","king"); 
    E3<String> g3 = E3<String>("gavin","king"); 
    E1 g4 = e1("gavin",1); 
    E2<String> g5 = e2("gavin","king"); 
    E3<String> g6 = e2("gavin","king"); 
    @error F();
    @error value e4 = Entry;
}