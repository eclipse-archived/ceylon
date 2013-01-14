interface Cntnr => Container<Anything>; 

alias Number => Integer|Float;
alias ListLike<T> given T satisfies Object => List<T>|Map<Integer,T>;
alias C => Container<Anything>&Category;
alias E => Cntnr&Category;

Number n = 1;
Number x = 2.0;
[Float, Float] pair = nothing;
ListLike<Float> list = pair;
C c = list;
E e = list;

shared alias Strings => List<String>;

void local() {
    alias Numbers => List<Integer|Float>;
    Numbers numbers = [ 1, 3.0 ];
    alias Ns => List<Number>;
    Ns ns = [ 1, 3.0 ];
}

class Outer() {
    shared alias Cs => List<Container<Anything>&Category>;
    shared Cs cs = [ c, c ];
}

Outer.Cs cs = Outer().cs;

class Outer2() {
    shared alias Es => List<E>;
    shared Es es = [ e, e ];
}

Outer2.Es es = Outer2().es;

void testSwitch(Number nn, C cc) {
    switch (nn)
    case (is Integer) {}
    case (is Float) {}
    print(cc.empty);
    print(cc.contains(1.0));
    print("hello" in cc);
}

void testSwitch2(Number nn, E ee) {
    switch (nn)
    case (is Integer) {}
    case (is Float) {}
    print(ee.empty);
    print(ee.contains(1.0));
    print("hello" in ee);
}

void testCanonicalization() {
    Integer|Float i = n;
    Number[] ns = [n];
    @type:"Null|Number" value temp = ns[0];
    print(temp);
    Integer|Float|Null t = temp;
    Number? num = t;
    @type:"Number&Object" Number&Object no = t else 1.0;
    @type:"Number" value no1 = no;
    @type:"Number" Number no2 = no;
    @type:"Number" value noo = temp else 1.0;
    if (is Cntnr num) {
        @type:"Number&Cntnr" value nnn = num;
        Integer&Cntnr|Float&Cntnr numif = num;
        Cntnr&Number nnnn = numif;
        switch (num)
        case (is Integer) {
            @type:"Cntnr&Integer" value nn = num;
        }
        case (is Float) {
            @type:"Cntnr&Float" value nn = num;
        }
    }
    List<Integer|Float> list = ns;
    List<Number> nums = list;
}

alias ItFun<T> => {T()*}&Category;
