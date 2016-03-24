class Doer(Anything do(Anything t)) {}
void anonymous() {
    variable Doer id = Doer {
        do(Anything t) => t;
    };
    id = Doer {
        function do(Anything t) {
            print("f");
            return t;
        }
    };
}