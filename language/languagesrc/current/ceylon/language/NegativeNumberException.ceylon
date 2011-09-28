//TODO improve input param and set default message?
shared class NegativeNumberException(String? description=null, Exception? cause=null)
        extends Exception(description, cause) {
}