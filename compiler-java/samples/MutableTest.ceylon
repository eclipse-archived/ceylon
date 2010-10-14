
class Test () {
    Integer value() { return val; }

    Integer val = 99;
}

public class MutableTest () {
    optional mutable Integer nnn = 2;

    void a(mutable Integer x) {
        
    }

    void b(Integer i) {
    }

    public void arrayTest (Process process) {
        mutable String poo := "";
        String arse = poo;
        Integer n = 2;
        optional mutable Integer nn = 2;
        mutable Integer foo := 0;

        Integer n7 = foo * 2;
        mutable Integer n8 := n7;
        n8 := n8**3;

        poo := 3;

        a(1);
        b(n);
        if (exists nn) {
            b(nn);
            a(nn);
        }

        mutable Test t = Test();
        process.writeLine(t.value());
    }

//     public void arrayTest (Process process) {
//         mutable Integer foo := 0;
//         Integer z = foo;
//         foo := 99;
        
// 	// mutable Integer parch = "Hello";
// 	mutable String poo = "Hello";

// 	poo := 3;

//         a(foo);
//         a(77);
//     }

}
