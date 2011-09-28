//TODO improve input param and set default message?
shared class NegativeNumberException(Exception? cause=null, String? description=null)
        extends Exception(cause, description) {
}