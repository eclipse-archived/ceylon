import ceylon.language.meta { type }

class Bug286() {
    shared variable String? nome = "Diego";
}

@test
shared void bug286() {
    value a = `Bug286`;
    value attribute = a.getAttribute<Bug286, String?>("nome");
    print(type(attribute));
}