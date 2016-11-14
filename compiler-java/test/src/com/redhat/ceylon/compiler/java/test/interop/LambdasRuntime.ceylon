import java.lang { Class }
import ceylon.language.meta { type }

void classModelCoercionTest() {
    value j = LambdasJava();

    value jklass1 = j.klassMethodParameterised(`LambdasJava`);
    print(jklass1.name);
    assert(jklass1.name == "com.redhat.ceylon.compiler.java.test.interop.LambdasJava");

    assert(j.klassMethodParameterised(`String`).name == "ceylon.language.String");
    assert(j.klassMethodParameterised(`Numeric<Integer>`).name == "ceylon.language.Numeric");
}
