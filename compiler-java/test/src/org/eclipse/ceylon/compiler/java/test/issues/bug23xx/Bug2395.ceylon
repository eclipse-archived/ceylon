@noanno
class Sup2395() {}
@noanno
class Sub2395() extends Sup2395() {}
@noanno
class Other2395() {}
@noanno
void f23951(Sup2395 s) {
    switch (s)
    case (is Sub2395|Other2395) {}
    else {}
}
@noanno
Integer f23952(Sup2395 s)
        => switch (s)
            case (is Sub2395|Other2395) 0
            else 1;