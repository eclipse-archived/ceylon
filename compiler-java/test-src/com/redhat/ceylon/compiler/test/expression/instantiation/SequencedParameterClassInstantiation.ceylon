@nomodel
class SequencedParameterClassInstantiation(Natural a, Natural... args){
    shared void m() {
        SequencedParameterClassInstantiation a = SequencedParameterClassInstantiation(1);
        SequencedParameterClassInstantiation b = SequencedParameterClassInstantiation(1, 2);
        SequencedParameterClassInstantiation c = SequencedParameterClassInstantiation(1, 2, 3);
    }
}
