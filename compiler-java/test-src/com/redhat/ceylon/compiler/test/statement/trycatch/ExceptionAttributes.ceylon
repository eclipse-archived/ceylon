@nomodel
class ExceptionAttributes() {
    void m(Exception e) {
        Exception? c = e.cause;
        if (exists Exception cause = e.cause) {
            Exception? cc = cause.cause;
            String? message = cause.message;
        }
        String message = e.message;
	}
}