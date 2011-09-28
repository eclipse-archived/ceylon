//TODO improve input param and set default message?
shared class OutOfRangeException(Exception? cause=null, String? description=null)
        extends Exception(cause, description) {
}