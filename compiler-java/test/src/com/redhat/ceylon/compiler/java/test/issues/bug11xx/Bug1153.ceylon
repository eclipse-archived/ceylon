class Bug1153() {
    {String*} strings = {"FormalInnerClass"};
    void bug() {
        variable String found;
        for (String s in strings) {
            if (s == "FormalInnerClass") {
                found = s;
                break;
            }
        } else {
            throw;
        }
        print(found);
    }
    bug();
}