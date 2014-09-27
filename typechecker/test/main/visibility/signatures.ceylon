void xxx() {
    @error function v() {
        class Bar() {}
        return Bar();
    }
}

void yyy() {
    void v(@error Bar bar) {
        class Bar() {}
    }
}

Anything vzz = void (@error Bar bar) {
    class Bar() {}
};

@error Anything vww = () {
    class Bar() {}
    return Bar();
};

void run(@error Anything b = (){class Bar(){} return Bar();}) {}