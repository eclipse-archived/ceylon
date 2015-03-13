import ceylon.language.meta { type }

class Bug2090() {
    shared void dothing() {
        value meths = type(this).getMethods<Nothing, Object,
        Anything>(`SharedAnnotation`);
    }
    //print(meths);
}

void bug2090() {
    Bug2090().dothing();
}