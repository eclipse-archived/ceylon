@noanno
interface LLVMValue6213B {
    
}
@noanno
interface I646213B satisfies LLVMValue6213B {
    
}
@noanno
interface Ptr6213B<T> satisfies LLVMValue6213B given T satisfies LLVMValue6213B {
    shared default Ptr6213B<T> offset(I646213B amount) { assert(false); }
    shared formal void store(T val, I646213B? off=null);
}
@noanno
class LLVMFunction6213B() {
    interface PointerImpl6213B<T> satisfies Ptr6213B<T> given T satisfies LLVMValue6213B {
        shared actual Ptr6213B<T> offset(I646213B amount) {
            return nothing;
        }
        shared actual void store(T val, I646213B? off) {
            if (exists off) {
                offset(off).store(val);
            }
        }
    }
    
    shared Ptr6213B<I646213B> register(String? regNameIn = null)
            => object satisfies PointerImpl6213B<I646213B> {};
}