import java.lang {
    SecurityManager,
    System
}

@noanno
shared void bug6782() {
    value securityManager = System.securityManager;
    object o extends SecurityManager() {
        checkMemberAccess = securityManager.checkMemberAccess;
    }
}
