@nomodel
class DefaultedVariableAttribute(firstName=null, lastName=null) {

    variable String? firstName;
    variable String? lastName;

    shared String fullName {
        return " ".join(firstName else "",lastName else "");
    }

    assign fullName {
        value tokens = fullName.split().iterator;
        if (is String first = tokens.next()) {
            firstName := first;
        }
        if (is String last = tokens.next()) {
            lastName := last;
        }
    }

}
