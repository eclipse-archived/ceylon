@noanno
interface LLVMCodeTarget6213A {
    "Emit a call instruction"
    shared T call<T=Anything>(String name, String* args) {
        return nothing;
    }
    
}
@noanno
class LLVMFunction6213A()
        satisfies LLVMCodeTarget6213A {
    
}