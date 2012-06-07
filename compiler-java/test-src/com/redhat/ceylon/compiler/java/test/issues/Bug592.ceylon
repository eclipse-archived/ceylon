import java.nio.file { FileSystems { default } }
import java.nio.file.attribute { UserPrincipalNotFoundException }

void bug592() {
    try {
        default.userPrincipalLookupService.lookupPrincipalByName("gavin");
    }
    catch (UserPrincipalNotFoundException e) {
        e.printStackTrace();
    }
}