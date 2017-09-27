@noanno
class Inner(Anything(Integer) arg) {}

@noanno
class Outer(Anything() arg) extends Inner((num) => arg()) {
}

@noanno
void temp() { }

@noanno
void mwe() {
    Outer(temp);
    Anything() arg = temp;
    value f = (Integer num) => arg();
}
