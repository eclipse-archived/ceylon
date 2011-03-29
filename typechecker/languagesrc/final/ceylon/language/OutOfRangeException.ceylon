//TODO improve input param and set default message?
shared class OutOfRangeException(Exception? cause=null, String? message=null)
        extends Exception(cause, message) {
}