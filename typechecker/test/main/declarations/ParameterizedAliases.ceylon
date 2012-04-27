interface I1 = Iterable<String>;
interface I2<S> = Iterable<S>;
class E1(String name, Integer i) = Entry<String,Integer>;
class E2<T>(String name, T t) given T satisfies Object = Entry<String,T>;
void p(Object s) = print;
Entry<String,Integer> e1(String name, Integer i) = Entry<String,Integer>;
Entry<String,T> e2<T>(String name, T t) given T satisfies Object = Entry<String,T>;
