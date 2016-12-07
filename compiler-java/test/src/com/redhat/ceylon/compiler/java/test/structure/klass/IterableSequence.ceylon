interface IterableSequence <out Element=Anything, 
                          out Absent=Null>
        given Absent satisfies Null {
    @nailed:"Sequential<? extends Element>"
    shared default [Element+] | []&Iterable<Element,Absent> sequence()
        => nothing;
}
// check mixin
class IterableSequenceMixin<out Element,out Absent=Null>() 
        satisfies IterableSequence<Element> 
        given Absent satisfies Null {
}
// check overriding
class IterableSequenceOverride<out Element,out Absent=Null>() 
        satisfies IterableSequence<Element> 
        given Absent satisfies Null {
    shared actual [Element+] | []&Iterable<Element,Absent> sequence()
        => nothing;
}
// check overriding+return type refinement
class IterableSequenceRefine<out Element,out Absent=Null>()
        satisfies IterableSequence<Element> 
        given Absent satisfies Null {
    shared actual [Element,Element] sequence()
            => nothing;
}
void iterableSequence<Element>() {
    // callsites
    IterableSequence<Element,Null> possEmpty = nothing;
    IterableSequence<Element,Nothing> defNonempty = nothing;
    IterableSequence<Nothing,Nothing> defEmpty1 = nothing;
    IterableSequence<Nothing,Null> defEmpty2 = nothing;
    
    // positional args call sites
    []|[Element+] x1 = possEmpty.sequence();
    [Element+] x2 = defNonempty.sequence();
    [] x3 = defEmpty1.sequence();
    [] x4 = defEmpty2.sequence();
    
    // name args call sites
    []|[Element+] y1 = possEmpty.sequence{};
    [Element+] y2 = defNonempty.sequence{};
    [] y3 = defEmpty1.sequence{};
    [] y4 = defEmpty2.sequence{};
    
    // method ref
    <[]|[Element+]>() z1 = possEmpty.sequence;
    [Element+]() z2 = defNonempty.sequence;
    []() z3 = defEmpty1.sequence;
    []() z4 = defEmpty2.sequence;
    []|[Element+] z1c = z1();
    [Element+] z2c = z2();
    [] z3c = z3();
    [] z4c = z4();
    
    // static ref callsites
    <[Element+]|[]>()(IterableSequence<Element,Null>) a1 = IterableSequence<Element,Null>.sequence;
    [Element+]()(IterableSequence<Element,Nothing>) a2 = IterableSequence<Element,Nothing>.sequence;
    [Nothing+]()(IterableSequence<Nothing,Nothing>) a3 = IterableSequence<Nothing,Nothing>.sequence;
    []()(IterableSequence<Nothing,Null>) a4 = IterableSequence<Nothing,Null>.sequence;
    [Element+]|[] a1c = a1(possEmpty)();
    [Element+] a2c = a2(defNonempty)();
    [] a3c = a3(defEmpty1)();
    [] a4c = a4(defEmpty2)();
    
    // TODO metamodel refs
    
    // TODO plus subclasses (with and without refinement)
}
void iterableSequenceOverride<Element>() {
    // callsites
    IterableSequenceOverride<Element,Null> possEmpty = nothing;
    IterableSequenceOverride<Element,Nothing> defNonempty = nothing;
    IterableSequenceOverride<Nothing,Nothing> defEmpty1 = nothing;
    IterableSequenceOverride<Nothing,Null> defEmpty2 = nothing;
    
    // positional args call sites
    []|[Element+] x1 = possEmpty.sequence();
    [Element+] x2 = defNonempty.sequence();
    [] x3 = defEmpty1.sequence();
    [] x4 = defEmpty2.sequence();
    
    // name args call sites
    []|[Element+] y1 = possEmpty.sequence{};
    [Element+] y2 = defNonempty.sequence{};
    [] y3 = defEmpty1.sequence{};
    [] y4 = defEmpty2.sequence{};
    
    // method ref
            <[]|[Element+]>() z1 = possEmpty.sequence;
    [Element+]() z2 = defNonempty.sequence;
    []() z3 = defEmpty1.sequence;
    []() z4 = defEmpty2.sequence;
    
    // static ref callsites
    <[Element+]|[]>()(IterableSequenceOverride<Element,Null>) a1 = IterableSequenceOverride<Element,Null>.sequence;
    [Element+]()(IterableSequenceOverride<Element,Nothing>) a2 = IterableSequenceOverride<Element,Nothing>.sequence;
    [Nothing+]()(IterableSequenceOverride<Nothing,Nothing>) a3 = IterableSequenceOverride<Nothing,Nothing>.sequence;
    []()(IterableSequenceOverride<Nothing,Null>) a4 = IterableSequenceOverride<Nothing,Null>.sequence;
    
    // TODO metamodel refs
    
    // TODO plus subclasses (with and without refinement)
}
void iterableSequenceRefine<Element>() {
    // callsites
    IterableSequenceRefine<Element,Null> possEmpty = nothing;
    IterableSequenceRefine<Element,Nothing> defNonempty = nothing;
    IterableSequenceRefine<Nothing,Nothing> defEmpty1 = nothing;
    IterableSequenceRefine<Nothing,Null> defEmpty2 = nothing;
    
    // positional args call sites
    []|[Element+] x1 = possEmpty.sequence();
    [Element+] x2 = defNonempty.sequence();
    [] x3 = defEmpty1.sequence();
    [] x4 = defEmpty2.sequence();
    
    // name args call sites
    []|[Element+] y1 = possEmpty.sequence{};
    [Element+] y2 = defNonempty.sequence{};
    [] y3 = defEmpty1.sequence{};
    [] y4 = defEmpty2.sequence{};
    
    // method ref
    <[]|[Element+]>() z1 = possEmpty.sequence;
    [Element+]() z2 = defNonempty.sequence;
    []() z3 = defEmpty1.sequence;
    []() z4 = defEmpty2.sequence;
    
    // static ref callsites
    <[Element+]|[]>()(IterableSequenceRefine<Element,Null>) a1 = IterableSequenceRefine<Element,Null>.sequence;
    [Element+]()(IterableSequenceRefine<Element,Nothing>) a2 = IterableSequenceRefine<Element,Nothing>.sequence;
    [Nothing+]()(IterableSequenceRefine<Nothing,Nothing>) a3 = IterableSequenceRefine<Nothing,Nothing>.sequence;
    []()(IterableSequenceRefine<Nothing,Null>) a4 = IterableSequenceRefine<Nothing,Null>.sequence;
    
    // TODO metamodel refs
    
    // TODO plus subclasses (with and without refinement)
}

abstract class IterableSequenceAbstract() 
        satisfies IterableSequence<Character> {
}
