//TODO improve input param and set default message?
shared class UndefinedKeyException(Exception? cause=null, String? message=null)
        extends Exception(cause, message) {
}