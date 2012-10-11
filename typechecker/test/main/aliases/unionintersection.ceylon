alias Number = Integer|Float;
alias ListLike<T> given T satisfies Object = List<T>|Map<Integer,T>;
alias C = Container&Category;

Number n = 1;
Number x = 2.0;
ListLike<Float> list = { 1.0, 2.0 };
C c = list;

shared alias Strings = List<String>;

void local() {
    alias Numbers = List<Number>;
}

class Class() {
    shared alias Cs = Set<C>;
}