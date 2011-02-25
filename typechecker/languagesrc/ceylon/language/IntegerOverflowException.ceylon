//TODO improve input param and set default message?
shared class IntegerOverflowException(Exception? cause=null, String? message=null)
        extends Exception(cause, message) {
}