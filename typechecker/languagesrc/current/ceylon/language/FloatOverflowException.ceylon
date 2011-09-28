//TODO improve input param and set default message?
shared class FloatOverflowException(Exception? cause=null, String? description=null)
        extends Exception(cause, description) {
}