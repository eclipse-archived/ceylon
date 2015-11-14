Integer defaultedParameters(
    String s = "", 
    Integer i = 0) {
    return i+s.size;
}
shared void defaultedParametersMain() {
    defaultedParameters();
    defaultedParameters("1");
    defaultedParameters{
        i=1;
    };
}