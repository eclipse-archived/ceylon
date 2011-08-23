@nomodel
class ReplaceExceptionAtJavaCallSite() {
    void m() {
        JavaThrower jt = JavaThrower();
        Natural n = jt.throwException();
        Natural m = jt.throwThrowable();
        Natural x = jt.throwRuntimeException();
        Natural y = jt.throwError();
        Natural z = jt.throwsMultiple();
    }

}