@noanno
shared class Bug1132_B() {}

@noanno
Bug1132_B bug1132_defaultParse() {
    return Bug1132_B();
}

@noanno
shared class Bug1132_X (
    String error() => "",
    Bug1132_B localeParser(String error()) => bug1132_defaultParse()
) 
{
    localeParser(error);
}

@noanno
void bug1132_testX() {
    Bug1132_X();
}