// Project bug0, module org.bug0
shared interface Path2302<out Element>{} 

shared interface I2302 {
    shared formal void f(Path2302<String|Integer> relativePath);
}
// Project bug1, module org.bug1
class C2302_1() satisfies I2302 {
    shared actual void f(Path2302<String|Integer> relativePath) {}
}