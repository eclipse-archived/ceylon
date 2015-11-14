shared interface LegalEntity {
    shared formal String name;
}

shared serializable
class Person(name, address)
        satisfies LegalEntity {
    shared actual variable String name;
    shared variable Address address;
    shared variable Person? spouse = null;
    shared actual default String string {
        variable value s = "``name`` who lives at ``address``";
        if (exists sp = spouse) {
            s = s + " and has spouse ``sp.name``";
        }
        return s;
    }
}

shared serializable
class Address(lines) {
    shared String lines;
    shared actual String string => lines;
}
