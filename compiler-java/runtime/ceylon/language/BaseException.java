package ceylon.language;

public class BaseException extends RuntimeException {

    public BaseException() {
        this(null, null);
    }
    
    public BaseException(ceylon.language.String message) {
        this(message, null);
    }
    
    public BaseException(java.lang.Throwable cause) {
        this(null, cause);
    }
    
    /*/public BaseException(ceylon.language.String message, ceylon.language.Exception cause) {
        super((message != null) ? message.toJavaString() : null, (java.lang.Throwable)(java.lang.Object)cause);
    }*/
    
    public BaseException(ceylon.language.String message, java.lang.Throwable cause) {
        super((message != null) ? message.toJavaString() : null, cause);
    }

}