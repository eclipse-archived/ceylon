import ceylon.language.meta.model { Attribute }
import ceylon.language.meta { type }

class Bug307() {
    shared variable String nome = "Diego";
}

@test
shared void bug307() {
    value a = `Bug307`;

    value attributeA = a.getAttribute<Bug307, String?>("nome");
    assert( !attributeA is Attribute<Bug307, String?, String?> );
    assert( !attributeA is Attribute<Bug307, String|Integer, String|Integer> );
}