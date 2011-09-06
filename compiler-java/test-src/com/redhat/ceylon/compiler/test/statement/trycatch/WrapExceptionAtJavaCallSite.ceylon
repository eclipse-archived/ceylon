@nomodel
class ReplaceExceptionAtJavaCallSite() {
    void m() {
        JavaThrower jt = JavaThrower();
        Boolean n = jt.throwException();
        Boolean m = jt.throwThrowable();
        Boolean x = jt.throwRuntimeException();
        Boolean y = jt.throwError();
        Boolean z = jt.throwsMultiple();
    }

}