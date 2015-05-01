void anythingMinus(Anything arg) {
    if (!is String arg) {
        @type:"Anything" value x = arg;
    }
    if (!is Object arg) {
        @type:"Null" value x = arg;
    }
    if (!is Null arg) {
        @type:"Object" value x = arg;
    }
}


void paramMinus<T>(T arg) {
    if (!is String arg) {
        @type:"T" value x = arg;
    }
    if (!is Object arg) {
        @type:"T&null" value x = arg;
    }
    if (!is Null arg) {
        @type:"T&Object" value x = arg;
    }
}
