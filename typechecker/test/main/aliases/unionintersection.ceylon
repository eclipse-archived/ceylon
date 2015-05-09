interface Arr<Element> 
        satisfies Container<Element>&
        Correspondence<Integer,Element> 
        given Element satisfies Object {}

interface Cntnr => Container<Anything>;
interface Crrspnd => Correspondence<Integer,Object>;

class Inty() {} class Floaty() {}
alias Number => Inty|Floaty;
alias ListLike<T> given T satisfies Object => List<T>|Map<Integer,T>|Arr<T>;
alias CC => Container<Anything>&Category;
alias EE => Cntnr&Category;
alias C => Correspondence<Integer,Object>&Category;
alias E => Crrspnd&Category;

Number n = Inty();
Number x = Floaty();
[Float, Float] pair = nothing;
ListLike<Float> list = pair;
Arr<String> arr = nothing;
C c = list;
E e = list;
CC cc = arr;
EE ee = arr;

shared alias Strings => List<String>;

void local() {
    alias Numbers => List<Inty|Floaty>;
    Numbers numbers = [ Inty(), Floaty() ];
    alias Ns => List<Number>;
    Ns ns = [ Inty(), Floaty() ];
}

class Outer() {
    shared alias Cs => List<Correspondence<Integer,Object>&Category>;
    shared Cs cs = [ c, c ];
    shared alias CCs => List<Container<Anything>&Category>;
    shared CCs ccs = [ cc, cc, cc ];
}

Outer.Cs cs = Outer().cs;

class Outer2() {
    shared alias Es => List<E>;
    shared Es es = [ e, e ];
}

Outer2.Es es = Outer2().es;

void testSwitch(Number nn, C cc) {
    switch (nn)
    case (is Inty) {}
    case (is Floaty) {}
    print(cc.keys);
    print(cc.contains(1.0));
    print("hello" in cc);
}

void testSwitch2(Number nn, E ee) {
    switch (nn)
    case (is Inty) {}
    case (is Floaty) {}
    print(ee.keys);
    print(ee.contains(1.0));
    print("hello" in ee);
}

void testCanonicalization() {
    Inty|Floaty i = n;
    Number[] ns = [n];
    @type:"Null|Number" value temp = ns[0];
    print(temp);
    Inty|Floaty|Null t = temp;
    Number? num = t;
    @type:"Number&Object" Number&Object no = t else Floaty();
    @type:"Number" value no1 = no;
    @type:"Number" Number no2 = no;
    @type:"Number" value noo = temp else Floaty();
    if (is Cntnr num) {
        @type:"Number&Cntnr" value nnn = num;
        Inty&Cntnr|Floaty&Cntnr numif = num;
        Cntnr&Number nnnn = numif;
        switch (num)
        case (is Inty) {
            @type:"Cntnr&Inty" value nn = num;
        }
        case (is Floaty) {
            @type:"Cntnr&Floaty" value nn = num;
        }
    }
    List<Inty|Floaty> list = ns;
    List<Number> nums = list;
}

alias ItFun<T> => {T()*}&Category;
