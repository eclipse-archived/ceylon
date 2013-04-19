@noanno
shared class Bug1132Class (String f1(String f2()) => f2(), String o = ""){}
@noanno
shared class Bug1132Class2<T> (T f1(T f2()) => f2()){}

@noanno
shared void bug1132<T>(){
    value g1 = function(String g(), String o = "") => g();
    value g2 = function(T g()) => g();
}
