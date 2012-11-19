void functionalParameterMpl2() {
    String f(String s)(Integer i) {
        return i.string + s;
    }
    print(functionalParameterMpl(f));
}