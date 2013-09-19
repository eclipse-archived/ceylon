import ceylon.language.meta.model { Attribute, VariableAttribute }
import ceylon.language.meta { type }

void bug308() {
    Integer|String test = "Diego Coronel";
    print(type(type(test)));
}