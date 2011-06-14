class MethodSwitchElse() {
    shared Integer m(Integer n) {
        switch(n) {
            case (1) {
                return 1;
            }
            case (2, 3, 4) {
                return n;
            }
            else {
                return 0;
            }
        }
        return -1;
    }
}