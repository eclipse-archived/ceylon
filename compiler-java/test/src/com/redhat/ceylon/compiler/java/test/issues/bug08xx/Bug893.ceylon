@nomodel
shared interface XIterable893<out Element> {
    shared default XIterable893<Element> success { return bottom; }
    shared default XIterable893<Element> fail1 { return bottom; }
    shared formal XIterable893<Element> fail2;
}
@nomodel
shared interface XSequential893<out Element>
        of XEmpty893|XSequence893<Element>
        satisfies XIterable893<Element> {
    
}
@nomodel
shared interface XSequence893<out Element>
        satisfies XSequential893<Element> {
    shared actual formal XEmpty893|XSequence893<Element> success;
    shared actual formal XEmpty893|XSequence893<Element> fail1;
    shared actual formal XEmpty893|XSequence893<Element> fail2;
}
@nomodel
shared interface XEmpty893
           satisfies XSequential893<Bottom> {
}
@nomodel
shared abstract class XSingleton893<out Element>(Element element)
        extends Object()
        satisfies XSequence893<Element>
        given Element satisfies Object {
    shared actual XEmpty893 success { return bottom; }
    shared actual XEmpty893 fail1 => bottom;
    shared actual XEmpty893 fail2 = bottom;
}