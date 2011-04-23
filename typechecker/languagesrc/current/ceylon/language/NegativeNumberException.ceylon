//TODO improve input param and set default message?
shared class NegativeNumberException(Exception? cause=null, String? message=null)
        extends Exception(cause, message) {
}