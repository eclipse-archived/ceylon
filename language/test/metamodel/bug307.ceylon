import ceylon.language.meta.model { Attribute, VariableAttribute }
import ceylon.language.meta { type }

class Bug307() {
    shared variable String? nome = "Diego";
}
void bug307() {
    value a = `Bug307`;

    value attributeA = a.getAttribute<Bug307, String?>("nome");
    assert( is VariableAttribute<Bug307, String?> attributeA);
    attributeA(Bug307()).set(null);
}