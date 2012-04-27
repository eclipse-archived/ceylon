interface I1 = Iterable<String>;
interface I2<S> = Iterable<S>;
class E1(String name, Integer i) = Entry<String,Integer>;
class E2<T>(String name, T t) given T satisfies Object = Entry<String,T>;
void p(Object s) = print;
Entry<String,Integer> e1(String name, Integer i) = Entry<String,Integer>;
Entry<String,T> e2<T>(String name, T t) given T satisfies Object = Entry<String,T>;
class F() = Float;
@error abstract class F1() = Float;

void check() { 
    E1 g1 = E1("gavin",1); 
    E2<String> g2 = E2<String>("gavin","king"); 
    E1 g3 = e1("gavin",1); 
    E2<String> g4 = e2("gavin","king"); 
    @error F();
}