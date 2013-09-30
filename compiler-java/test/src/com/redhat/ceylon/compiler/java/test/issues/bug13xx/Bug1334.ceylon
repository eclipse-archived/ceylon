@noanno
class Bug1334_1() {
    hash => 23;
}
@noanno
class Bug1334_2() {
    hash = 23;
}
@noanno
class Bug1334_3() {
    shared actual Integer hash = 23;
}
@noanno
class Bug1334_4() {
    shared actual Integer hash => 23;
}
@noanno
class Bug1334_5() {
    shared actual Integer hash { return 23; }
}
@noanno
class Bug1334_6(shared actual Integer hash) {
}
@noanno
class Bug1334_7(shared actual Integer hash = 23) {
}
@noanno
class Bug1334_8(hash) {
    shared actual Integer hash;
}
@noanno
class Bug1334_9(hash = 23) {
    shared actual Integer hash;
}
@noanno
class Bug1334_6_2(Integer h) extends Bug1334_6(h){}
@noanno
class Bug1334_10(){
    shared actual variable Integer hash = 23;
}
@noanno
interface Bug1334_11{
    shared actual Integer hash => 23;
}
@noanno
class Bug1334_12() extends Object() satisfies Bug1334_11 {
    shared actual Boolean equals(Object o) => false;
}
