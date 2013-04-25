@nomodel
shared class Bug1132_B() {}

@nomodel
Bug1132_B bug1132_defaultParse() {
    return Bug1132_B();
}

@nomodel
shared class Bug1132_X (
    String error() => "",
    Bug1132_B localeParser(String error()) => bug1132_defaultParse()
) 
{
    localeParser(error);
}

@nomodel
void bug1132_testX() {
    Bug1132_X();
}