import ceylon.language.meta { type }

abstract class Bug329Type() of Bug329SubType | bug329A | bug329B {}

class Bug329SubType() extends Bug329Type(){}

object bug329A extends Bug329Type(){}
object bug329B extends Bug329Type(){}

@test
shared void bug329() {
    assert(`Bug329Type`.caseValues == [bug329A, bug329B]);
}