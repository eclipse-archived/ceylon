@noanno
class ParametersAndMembers(
    unsharedUncapturedHidden,
    unsharedCapturedHidden,
    sharedHidden, 
    String unsharedUncapturedVpd,
    String unsharedCapturedVpd,
    shared String sharedVpd) {
    
    print(unsharedUncapturedHidden + unsharedUncapturedVpd);
    
    String unsharedUncapturedHidden;
    String unsharedCapturedHidden;
    shared String sharedHidden;
    
    shared void capture() {
        print(unsharedCapturedHidden + unsharedCapturedVpd);
    }
}