List<String>&Text before = ["hello"];
{String...} stringsBefore = before;
alias Text => List<String>;
List<String>&Text after = ["hello"];
{String...} stringsAfter = after;

Defaulted dbefore = Defaulted("hello");
Defaulted<String> dsbefore = dbefore;
@error Defaulted<Integer> edbefore = dbefore;
class Defaulted<T=String>(shared T t) {}
Defaulted dafter = Defaulted("bye");
Defaulted<String> dsafter = dafter;
@error Defaulted<Integer> edafter = dafter;

Optional<String> obefore = Optional("hello", 1);
Optional<String,Integer> osibefore = obefore;
@error Optional<String,Float> eobefore = obefore;
class Optional<X,T=Integer>(X x, T t) {}
Optional<String> oafter = Optional("bye", 2);
Optional<String,Integer> osiafter = oafter;
@error Optional<String,Float> eoafter = oafter;

Defaulted<Optional<String>> do1 = Defaulted(Optional("hello", 1));
Defaulted<Optional<String,Integer>> do2 = do1; 
Defaulted<Optional<String>> do3 = do2; 

interface AlsoDefaulted<T=Float> {
    shared default void accept(T tt) {}
}
class ExtendsDefaulted() extends Defaulted("") satisfies AlsoDefaulted {}
Defaulted d1 = ExtendsDefaulted();
AlsoDefaulted d2 = ExtendsDefaulted();
void check() {
    String s1 = d1.t;
    d2.accept(1.0);
    String s2 = ExtendsDefaulted().t;
    ExtendsDefaulted().accept(1.0);
}
class RefinesDefault() satisfies AlsoDefaulted {
    shared actual void accept(Float f) {}
}