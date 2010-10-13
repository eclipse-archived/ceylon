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

        poo := 3;

        a(1);
        b(n);
        if (exists nn) {
            b(nn);
        }
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
