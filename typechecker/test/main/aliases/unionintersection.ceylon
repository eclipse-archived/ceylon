interface Cntnr => Container<Void>; 

alias Number => Integer|Float;
alias ListLike<T> given T satisfies Object => List<T>|Map<Integer,T>;
alias C => Cntnr&Category;

Number n = 1;
Number x = 2.0;
ListLike<Float> list = [ 1.0, 2.0 ];
C c = list;

shared alias Strings => List<String>;

void local() {
    alias Numbers => List<Number>;
    Numbers ns = [ 1, 3.0 ];
}

class Outer() {
    shared alias Cs => List<C>;
    shared Cs cs = [ c, c ];
}

Outer.Cs cs = Outer().cs;

void testSwitch(Number nn, C cc) {
    switch (nn)
    case (is Integer) {}
    case (is Float) {}
    print(cc.empty);
    print(cc.contains(1.0));
    print("hello" in cc);
}

void testCanonicalization() {
    Integer|Float i = n;
    Number[] ns = [n];
    @type:"Nothing|Number" value temp = ns[0];
    print(temp);
    Integer|Float|Nothing t = temp;
    Number? num = t;
    @type:"Number" Number&Object no = t else 1.0;
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

alias ItFun<T> => {T()...}&Category;
