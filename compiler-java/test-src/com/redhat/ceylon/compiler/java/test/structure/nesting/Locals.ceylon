@nomodel
class Local() {
    Integer mLocal() {
        return 0;
    }
    void local(Integer i = 0) {
        class LC(Integer j = 0) {
            interface LCI {
                shared default Integer m() {
                    return mLocal() + i + j;
                }
            }
            class LCC(Integer k = 0) satisfies LCI {
                shared actual Integer m() {
                    return mLocal() + i + j + k;
                }
            }
        }
        interface LI {
            interface LII {
                shared default Integer m() {
                    return mLocal() + i;
                }
            }
            class LIC(Integer j = 0) satisfies LII {
                shared actual Integer m() {
                    return mLocal() + i + j;
                }
            }        
        }
    }
    class C(Integer i = 0) {
        void local2(Integer j = 0) {
            class CL(Integer k = 0) {
                interface LCI {
                    shared default Integer m() {
                        return mLocal() + i + j + k;
                    }
                }
                class CLC(Integer l = 0) satisfies LCI {
                    shared actual Integer m() {
                        return mLocal() + i + j + k + l;
                    }
                }
            }
        }
    }
    interface I {
        void local3(Integer i = 0) {
            class IL(Integer j = 0) {
                interface ICI {
                    shared default Integer m() {
                        return mLocal() + i + j;
                    }
                }
                class ILC(Integer k = 0) satisfies ICI {
                    shared actual Integer m() {
                        return mLocal() + i + j + k;
                    }
                }

            }
        }    
    }
}