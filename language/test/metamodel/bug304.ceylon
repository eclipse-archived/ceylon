import ceylon.language.meta.model { Attribute, VariableAttribute }
import ceylon.language.meta { type }

class Bug304() {
    shared variable String? nome = "Diego";
}

@test
shared void bug304() {
    value a = `Bug304`;

    value attributeA = a.getAttribute<Bug304, String?>("nome");
    assert( is Attribute<Bug304, String?> attributeA); //works fine

    assert( is VariableAttribute<Bug304, String?> attributeA); //assertion failure.. or if(VariableAttribute) never get in

}