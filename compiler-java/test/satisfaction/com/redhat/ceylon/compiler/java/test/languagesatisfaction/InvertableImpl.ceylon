class InvertableImpl<out Inverse>() satisfies Invertable<Inverse> {
    shared actual Inverse negativeValue = bottom;
    shared actual Inverse positiveValue = bottom;
}