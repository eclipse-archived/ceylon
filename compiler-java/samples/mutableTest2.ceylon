public void mutableTest2 (Process process) {
    mutable String poo := "";
    String arse = poo;
    Integer n = 2;
    optional mutable Integer nn = 2;
    mutable Integer mutt = 4;

    Integer z = mutt;
    
    mutable Integer foo := 0;

    foo++;

    --foo;

    foo := foo++;

    foo := 2;

    Integer n7 = foo * 2;
    mutable Integer n8 := 33;
    n8 := n8**3;
    
    poo := 3;

    b(n);
    if (exists nn) {
        // nn := 5;   Not allowed: nn is not mutable
        b(nn);
        a(nn);
    }

    mutable Test t = Test();
    process.writeLine(t.value().string());

    Integer inner(Integer i) {
        return i+1;
    }

    class T3 () {
        mutable Integer f := 99;
    }

     class T2 () {
        mutable T3 b = T3();
    }

    class T1 () {
        mutable T2 a = T2();
    }

   T1 t1 = T1();

   Integer n1 = t1.a.b.f;

   this.mutt;
}
