@nomodel
interface ValueRefiningGetterSetter {
    shared default Integer i {
        return 0;
    }
    assign i {
    }
}
@nomodel
class ValueRefiningGetterSetter_Sub() satisfies ValueRefiningGetterSetter {
    shared actual variable Integer i := 0;
}