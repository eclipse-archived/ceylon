@nomodel
interface AssignOuter {
    Integer a {
        return 0;
    }
    assign a {
    }
    shared formal variable Integer b;
    shared default Integer c {
        return 0;
    }
    assign c {
    }
    shared formal variable Integer? d;
    interface I {
        shared default Integer aDefault {
            return outer.a;
        }
        assign aDefault {
            outer.a := aDefault;
        }
        shared default Integer bDefault {
            return outer.b;
        }
        assign bDefault {
            outer.b := bDefault;
        }
        shared default Integer cDefault {
            return outer.c;
        }
        assign cDefault {
            outer.c := cDefault;
        }
        void m() {
            outer.a++;
            outer.b++;
            outer.c++;
            ++outer.a;
            ++outer.b;
            ++outer.c;
        }
    }
}
@nomodel
abstract class AssignOuterClass() {
     Integer a {
        return 0;
    }
    assign a {
    }
    shared formal variable Integer b;
    shared default Integer c {
        return 0;
    }
    assign c {
    }
    interface I {
        shared default Integer aDefault {
            return outer.a;
        }
        assign aDefault {
            outer.a := aDefault;
        }
        shared default Integer bDefault {
            return outer.b;
        }
        assign bDefault {
            outer.b := bDefault;
        }
        shared default Integer cDefault {
            return outer.c;
        }
        assign cDefault {
            outer.c := cDefault;
        }
        void m() {
            outer.a++;
            outer.b++;
            outer.c++;
            ++outer.a;
            ++outer.b;
            ++outer.c;
        }
    }
}