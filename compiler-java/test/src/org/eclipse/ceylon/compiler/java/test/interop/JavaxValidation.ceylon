import javax.validation.constraints{
    max__GETTER,
    max__FIELD
}

@nomodel
class JavaxValidationMax() {
    shared variable max__GETTER(5) Integer maxOnGetter = 4;
    shared variable max__FIELD(5) Integer maxOnField = 4;
}