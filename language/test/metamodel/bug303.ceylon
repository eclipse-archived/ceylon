import ceylon.language.meta.model { Attribute }
import ceylon.language.meta { type }

class Bug303() {
    shared variable String? nome = "Diego";
}
void bug303() {
    value a = `Bug303`;

    value attributeA = a.getAttribute<Bug303, String?>("nome");
    assert( is Attribute<Bug303, String?> attributeA); //works fine

    value attributeB = a.getAttribute<Nothing, Anything>("nome");
    assert( is Attribute<Bug303, String?> attributeB); //assertion failed...

}
