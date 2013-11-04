import ceylon.language.meta.model { Attribute }
import ceylon.language.meta { type }

class Bug318() {}

@test
shared void bug318() {
    Attribute<Bug318,String> attribute = `Bug318.string`;
    print(attribute);
    assert(attribute.string == "ceylon.language::Object.string");
}