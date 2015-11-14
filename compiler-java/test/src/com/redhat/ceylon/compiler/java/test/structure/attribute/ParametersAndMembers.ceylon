@noanno
class ParametersAndMembers(
    unsharedUncapturedHidden,
    unsharedCapturedHidden,
    sharedHidden, 
    String unsharedUncapturedVpd,
    String unsharedCapturedVpd,
    shared String sharedVpd) {
    
    String unsharedUncapturedHidden;
    
    print(unsharedUncapturedHidden + unsharedUncapturedVpd);
    
    String unsharedCapturedHidden;
    shared String sharedHidden;
    
    shared void capture() {
        print(unsharedCapturedHidden + unsharedCapturedVpd);
    }
}