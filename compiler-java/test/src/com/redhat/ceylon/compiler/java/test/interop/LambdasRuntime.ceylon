import java.lang { Class }
import ceylon.language.meta { type }

void classModelCoercionTest() {
    value j = LambdasJava();

    value jklass1 = j.klassMethodParameterised(`LambdasJava`);
    print(jklass1.name);
    assert(jklass1.name == "com.redhat.ceylon.compiler.java.test.interop.LambdasJava");

    assert(j.klassMethodParameterised(`String`).name == "ceylon.language.String");
    assert(j.klassMethodParameterised(`Numeric<Integer>`).name == "ceylon.language.Numeric");

    j.arrays(Array{1, 2}, Array{1.0, 2.0}, Array{1.byte, 2.byte}, Array{true, false}, Array{j, null}, Array{j});
}
